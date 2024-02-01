package android.database.sqlite;

import android.database.sqlite.SQLiteDebug;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.OperationCanceledException;
import android.os.SystemClock;
import android.provider.SettingsStringUtil;
import android.text.TextUtils;
import android.util.Log;
import android.util.PrefixPrinter;
import android.util.Printer;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;
/* loaded from: classes.dex */
public final class SQLiteConnectionPool implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int CONNECTION_FLAG_INTERACTIVE = 4;
    public static final int CONNECTION_FLAG_PRIMARY_CONNECTION_AFFINITY = 2;
    public static final int CONNECTION_FLAG_READ_ONLY = 1;
    private static final long CONNECTION_POOL_BUSY_MILLIS = 30000;
    private static final String TAG = "SQLiteConnectionPool";
    private SQLiteConnection mAvailablePrimaryConnection;
    private final SQLiteDatabaseConfiguration mConfiguration;
    private ConnectionWaiter mConnectionWaiterPool;
    private ConnectionWaiter mConnectionWaiterQueue;
    @GuardedBy("mLock")
    private IdleConnectionHandler mIdleConnectionHandler;
    private boolean mIsOpen;
    private int mMaxConnectionPoolSize;
    private int mNextConnectionId;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final Object mLock = new Object();
    private final AtomicBoolean mConnectionLeaked = new AtomicBoolean();
    private final ArrayList<SQLiteConnection> mAvailableNonPrimaryConnections = new ArrayList<>();
    private final AtomicLong mTotalExecutionTimeCounter = new AtomicLong(0);
    private final WeakHashMap<SQLiteConnection, AcquiredConnectionStatus> mAcquiredConnections = new WeakHashMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum AcquiredConnectionStatus {
        NORMAL,
        RECONFIGURE,
        DISCARD
    }

    private synchronized SQLiteConnectionPool(SQLiteDatabaseConfiguration configuration) {
        this.mConfiguration = new SQLiteDatabaseConfiguration(configuration);
        setMaxConnectionPoolSizeLocked();
        if (this.mConfiguration.idleConnectionTimeoutMs != Long.MAX_VALUE) {
            setupIdleConnectionHandler(Looper.getMainLooper(), this.mConfiguration.idleConnectionTimeoutMs);
        }
    }

    protected void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    public static synchronized SQLiteConnectionPool open(SQLiteDatabaseConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration must not be null.");
        }
        SQLiteConnectionPool pool = new SQLiteConnectionPool(configuration);
        pool.open();
        return pool;
    }

    private synchronized void open() {
        this.mAvailablePrimaryConnection = openConnectionLocked(this.mConfiguration, true);
        synchronized (this.mLock) {
            if (this.mIdleConnectionHandler != null) {
                this.mIdleConnectionHandler.connectionReleased(this.mAvailablePrimaryConnection);
            }
        }
        this.mIsOpen = true;
        this.mCloseGuard.open("close");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        dispose(false);
    }

    private synchronized void dispose(boolean finalized) {
        if (this.mCloseGuard != null) {
            if (finalized) {
                this.mCloseGuard.warnIfOpen();
            }
            this.mCloseGuard.close();
        }
        if (!finalized) {
            synchronized (this.mLock) {
                throwIfClosedLocked();
                this.mIsOpen = false;
                closeAvailableConnectionsAndLogExceptionsLocked();
                int pendingCount = this.mAcquiredConnections.size();
                if (pendingCount != 0) {
                    Log.i(TAG, "The connection pool for " + this.mConfiguration.label + " has been closed but there are still " + pendingCount + " connections in use.  They will be closed as they are released back to the pool.");
                }
                wakeConnectionWaitersLocked();
            }
        }
    }

    public synchronized void reconfigure(SQLiteDatabaseConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration must not be null.");
        }
        synchronized (this.mLock) {
            throwIfClosedLocked();
            boolean walModeChanged = ((configuration.openFlags ^ this.mConfiguration.openFlags) & 536870912) != 0;
            if (walModeChanged) {
                if (!this.mAcquiredConnections.isEmpty()) {
                    throw new IllegalStateException("Write Ahead Logging (WAL) mode cannot be enabled or disabled while there are transactions in progress.  Finish all transactions and release all active database connections first.");
                }
                closeAvailableNonPrimaryConnectionsAndLogExceptionsLocked();
            }
            boolean foreignKeyModeChanged = configuration.foreignKeyConstraintsEnabled != this.mConfiguration.foreignKeyConstraintsEnabled;
            if (foreignKeyModeChanged && !this.mAcquiredConnections.isEmpty()) {
                throw new IllegalStateException("Foreign Key Constraints cannot be enabled or disabled while there are transactions in progress.  Finish all transactions and release all active database connections first.");
            }
            boolean onlyCompatWalChanged = (this.mConfiguration.openFlags ^ configuration.openFlags) == 1073741824;
            if (!onlyCompatWalChanged && this.mConfiguration.openFlags != configuration.openFlags) {
                if (walModeChanged) {
                    closeAvailableConnectionsAndLogExceptionsLocked();
                }
                SQLiteConnection newPrimaryConnection = openConnectionLocked(configuration, true);
                closeAvailableConnectionsAndLogExceptionsLocked();
                discardAcquiredConnectionsLocked();
                this.mAvailablePrimaryConnection = newPrimaryConnection;
                this.mConfiguration.updateParametersFrom(configuration);
                setMaxConnectionPoolSizeLocked();
            } else {
                this.mConfiguration.updateParametersFrom(configuration);
                setMaxConnectionPoolSizeLocked();
                closeExcessConnectionsAndLogExceptionsLocked();
                reconfigureAllConnectionsLocked();
            }
            wakeConnectionWaitersLocked();
        }
    }

    public synchronized SQLiteConnection acquireConnection(String sql, int connectionFlags, CancellationSignal cancellationSignal) {
        SQLiteConnection con = waitForConnection(sql, connectionFlags, cancellationSignal);
        synchronized (this.mLock) {
            if (this.mIdleConnectionHandler != null) {
                this.mIdleConnectionHandler.connectionAcquired(con);
            }
        }
        return con;
    }

    public synchronized void releaseConnection(SQLiteConnection connection) {
        synchronized (this.mLock) {
            if (this.mIdleConnectionHandler != null) {
                this.mIdleConnectionHandler.connectionReleased(connection);
            }
            AcquiredConnectionStatus status = this.mAcquiredConnections.remove(connection);
            if (status == null) {
                throw new IllegalStateException("Cannot perform this operation because the specified connection was not acquired from this pool or has already been released.");
            }
            if (!this.mIsOpen) {
                closeConnectionAndLogExceptionsLocked(connection);
            } else if (connection.isPrimaryConnection()) {
                if (recycleConnectionLocked(connection, status)) {
                    this.mAvailablePrimaryConnection = connection;
                }
                wakeConnectionWaitersLocked();
            } else if (this.mAvailableNonPrimaryConnections.size() >= this.mMaxConnectionPoolSize - 1) {
                closeConnectionAndLogExceptionsLocked(connection);
            } else {
                if (recycleConnectionLocked(connection, status)) {
                    this.mAvailableNonPrimaryConnections.add(connection);
                }
                wakeConnectionWaitersLocked();
            }
        }
    }

    @GuardedBy("mLock")
    private synchronized boolean recycleConnectionLocked(SQLiteConnection connection, AcquiredConnectionStatus status) {
        if (status == AcquiredConnectionStatus.RECONFIGURE) {
            try {
                connection.reconfigure(this.mConfiguration);
            } catch (RuntimeException ex) {
                Log.e(TAG, "Failed to reconfigure released connection, closing it: " + connection, ex);
                status = AcquiredConnectionStatus.DISCARD;
            }
        }
        if (status == AcquiredConnectionStatus.DISCARD) {
            closeConnectionAndLogExceptionsLocked(connection);
            return false;
        }
        return true;
    }

    public synchronized boolean shouldYieldConnection(SQLiteConnection connection, int connectionFlags) {
        synchronized (this.mLock) {
            if (!this.mAcquiredConnections.containsKey(connection)) {
                throw new IllegalStateException("Cannot perform this operation because the specified connection was not acquired from this pool or has already been released.");
            }
            if (!this.mIsOpen) {
                return false;
            }
            return isSessionBlockingImportantConnectionWaitersLocked(connection.isPrimaryConnection(), connectionFlags);
        }
    }

    public synchronized void collectDbStats(ArrayList<SQLiteDebug.DbStats> dbStatsList) {
        synchronized (this.mLock) {
            if (this.mAvailablePrimaryConnection != null) {
                this.mAvailablePrimaryConnection.collectDbStats(dbStatsList);
            }
            Iterator<SQLiteConnection> it = this.mAvailableNonPrimaryConnections.iterator();
            while (it.hasNext()) {
                SQLiteConnection connection = it.next();
                connection.collectDbStats(dbStatsList);
            }
            for (SQLiteConnection connection2 : this.mAcquiredConnections.keySet()) {
                connection2.collectDbStatsUnsafe(dbStatsList);
            }
        }
    }

    private synchronized SQLiteConnection openConnectionLocked(SQLiteDatabaseConfiguration configuration, boolean primaryConnection) {
        int connectionId = this.mNextConnectionId;
        this.mNextConnectionId = connectionId + 1;
        return SQLiteConnection.open(this, configuration, connectionId, primaryConnection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onConnectionLeaked() {
        Log.w(TAG, "A SQLiteConnection object for database '" + this.mConfiguration.label + "' was leaked!  Please fix your application to end transactions in progress properly and to close the database when it is no longer needed.");
        this.mConnectionLeaked.set(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onStatementExecuted(long executionTimeMs) {
        this.mTotalExecutionTimeCounter.addAndGet(executionTimeMs);
    }

    @GuardedBy("mLock")
    private synchronized void closeAvailableConnectionsAndLogExceptionsLocked() {
        closeAvailableNonPrimaryConnectionsAndLogExceptionsLocked();
        if (this.mAvailablePrimaryConnection != null) {
            closeConnectionAndLogExceptionsLocked(this.mAvailablePrimaryConnection);
            this.mAvailablePrimaryConnection = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy("mLock")
    public synchronized boolean closeAvailableConnectionLocked(int connectionId) {
        int count = this.mAvailableNonPrimaryConnections.size();
        for (int i = count - 1; i >= 0; i--) {
            SQLiteConnection c = this.mAvailableNonPrimaryConnections.get(i);
            if (c.getConnectionId() == connectionId) {
                closeConnectionAndLogExceptionsLocked(c);
                this.mAvailableNonPrimaryConnections.remove(i);
                return true;
            }
        }
        if (this.mAvailablePrimaryConnection != null && this.mAvailablePrimaryConnection.getConnectionId() == connectionId) {
            closeConnectionAndLogExceptionsLocked(this.mAvailablePrimaryConnection);
            this.mAvailablePrimaryConnection = null;
            return true;
        }
        return false;
    }

    @GuardedBy("mLock")
    private synchronized void closeAvailableNonPrimaryConnectionsAndLogExceptionsLocked() {
        int count = this.mAvailableNonPrimaryConnections.size();
        for (int i = 0; i < count; i++) {
            closeConnectionAndLogExceptionsLocked(this.mAvailableNonPrimaryConnections.get(i));
        }
        this.mAvailableNonPrimaryConnections.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void closeAvailableNonPrimaryConnectionsAndLogExceptions() {
        synchronized (this.mLock) {
            closeAvailableNonPrimaryConnectionsAndLogExceptionsLocked();
        }
    }

    @GuardedBy("mLock")
    private synchronized void closeExcessConnectionsAndLogExceptionsLocked() {
        int availableCount = this.mAvailableNonPrimaryConnections.size();
        while (true) {
            int availableCount2 = availableCount - 1;
            if (availableCount > this.mMaxConnectionPoolSize - 1) {
                SQLiteConnection connection = this.mAvailableNonPrimaryConnections.remove(availableCount2);
                closeConnectionAndLogExceptionsLocked(connection);
                availableCount = availableCount2;
            } else {
                return;
            }
        }
    }

    @GuardedBy("mLock")
    private synchronized void closeConnectionAndLogExceptionsLocked(SQLiteConnection connection) {
        try {
            connection.close();
            if (this.mIdleConnectionHandler != null) {
                this.mIdleConnectionHandler.connectionClosed(connection);
            }
        } catch (RuntimeException ex) {
            Log.e(TAG, "Failed to close connection, its fate is now in the hands of the merciful GC: " + connection, ex);
        }
    }

    private synchronized void discardAcquiredConnectionsLocked() {
        markAcquiredConnectionsLocked(AcquiredConnectionStatus.DISCARD);
    }

    @GuardedBy("mLock")
    private synchronized void reconfigureAllConnectionsLocked() {
        if (this.mAvailablePrimaryConnection != null) {
            try {
                this.mAvailablePrimaryConnection.reconfigure(this.mConfiguration);
            } catch (RuntimeException ex) {
                Log.e(TAG, "Failed to reconfigure available primary connection, closing it: " + this.mAvailablePrimaryConnection, ex);
                closeConnectionAndLogExceptionsLocked(this.mAvailablePrimaryConnection);
                this.mAvailablePrimaryConnection = null;
            }
        }
        int count = this.mAvailableNonPrimaryConnections.size();
        int i = 0;
        while (i < count) {
            SQLiteConnection connection = this.mAvailableNonPrimaryConnections.get(i);
            try {
                connection.reconfigure(this.mConfiguration);
            } catch (RuntimeException ex2) {
                Log.e(TAG, "Failed to reconfigure available non-primary connection, closing it: " + connection, ex2);
                closeConnectionAndLogExceptionsLocked(connection);
                this.mAvailableNonPrimaryConnections.remove(i);
                count += -1;
                i--;
            }
            i++;
        }
        markAcquiredConnectionsLocked(AcquiredConnectionStatus.RECONFIGURE);
    }

    private synchronized void markAcquiredConnectionsLocked(AcquiredConnectionStatus status) {
        if (!this.mAcquiredConnections.isEmpty()) {
            ArrayList<SQLiteConnection> keysToUpdate = new ArrayList<>(this.mAcquiredConnections.size());
            for (Map.Entry<SQLiteConnection, AcquiredConnectionStatus> entry : this.mAcquiredConnections.entrySet()) {
                AcquiredConnectionStatus oldStatus = entry.getValue();
                if (status != oldStatus && oldStatus != AcquiredConnectionStatus.DISCARD) {
                    keysToUpdate.add(entry.getKey());
                }
            }
            int updateCount = keysToUpdate.size();
            for (int i = 0; i < updateCount; i++) {
                this.mAcquiredConnections.put(keysToUpdate.get(i), status);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:70:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x00eb  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:97:? -> B:79:0x00e2). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized android.database.sqlite.SQLiteConnection waitForConnection(java.lang.String r21, int r22, android.os.CancellationSignal r23) {
        /*
            Method dump skipped, instructions count: 247
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteConnectionPool.waitForConnection(java.lang.String, int, android.os.CancellationSignal):android.database.sqlite.SQLiteConnection");
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy("mLock")
    public synchronized void cancelConnectionWaiterLocked(ConnectionWaiter waiter) {
        if (waiter.mAssignedConnection != null || waiter.mException != null) {
            return;
        }
        ConnectionWaiter predecessor = null;
        for (ConnectionWaiter current = this.mConnectionWaiterQueue; current != waiter; current = current.mNext) {
            predecessor = current;
        }
        if (predecessor != null) {
            predecessor.mNext = waiter.mNext;
        } else {
            this.mConnectionWaiterQueue = waiter.mNext;
        }
        waiter.mException = new OperationCanceledException();
        LockSupport.unpark(waiter.mThread);
        wakeConnectionWaitersLocked();
    }

    private synchronized void logConnectionPoolBusyLocked(long waitMillis, int connectionFlags) {
        Thread thread = Thread.currentThread();
        StringBuilder msg = new StringBuilder();
        msg.append("The connection pool for database '");
        msg.append(this.mConfiguration.label);
        msg.append("' has been unable to grant a connection to thread ");
        msg.append(thread.getId());
        msg.append(" (");
        msg.append(thread.getName());
        msg.append(") ");
        msg.append("with flags 0x");
        msg.append(Integer.toHexString(connectionFlags));
        msg.append(" for ");
        msg.append(((float) waitMillis) * 0.001f);
        msg.append(" seconds.\n");
        ArrayList<String> requests = new ArrayList<>();
        int activeConnections = 0;
        int idleConnections = 0;
        if (!this.mAcquiredConnections.isEmpty()) {
            for (SQLiteConnection connection : this.mAcquiredConnections.keySet()) {
                String description = connection.describeCurrentOperationUnsafe();
                if (description != null) {
                    requests.add(description);
                    activeConnections++;
                } else {
                    idleConnections++;
                }
            }
        }
        int availableConnections = this.mAvailableNonPrimaryConnections.size();
        if (this.mAvailablePrimaryConnection != null) {
            availableConnections++;
        }
        msg.append("Connections: ");
        msg.append(activeConnections);
        msg.append(" active, ");
        msg.append(idleConnections);
        msg.append(" idle, ");
        msg.append(availableConnections);
        msg.append(" available.\n");
        if (!requests.isEmpty()) {
            msg.append("\nRequests in progress:\n");
            Iterator<String> it = requests.iterator();
            while (it.hasNext()) {
                String request = it.next();
                msg.append("  ");
                msg.append(request);
                msg.append("\n");
            }
        }
        Log.w(TAG, msg.toString());
    }

    @GuardedBy("mLock")
    private synchronized void wakeConnectionWaitersLocked() {
        ConnectionWaiter predecessor = null;
        ConnectionWaiter waiter = this.mConnectionWaiterQueue;
        boolean primaryConnectionNotAvailable = false;
        boolean nonPrimaryConnectionNotAvailable = false;
        while (waiter != null) {
            boolean unpark = false;
            if (!this.mIsOpen) {
                unpark = true;
            } else {
                SQLiteConnection connection = null;
                try {
                    if (!waiter.mWantPrimaryConnection && !nonPrimaryConnectionNotAvailable && (connection = tryAcquireNonPrimaryConnectionLocked(waiter.mSql, waiter.mConnectionFlags)) == null) {
                        nonPrimaryConnectionNotAvailable = true;
                    }
                    if (connection == null && !primaryConnectionNotAvailable && (connection = tryAcquirePrimaryConnectionLocked(waiter.mConnectionFlags)) == null) {
                        primaryConnectionNotAvailable = true;
                    }
                    if (connection != null) {
                        waiter.mAssignedConnection = connection;
                        unpark = true;
                    } else if (nonPrimaryConnectionNotAvailable && primaryConnectionNotAvailable) {
                        return;
                    }
                } catch (RuntimeException ex) {
                    waiter.mException = ex;
                    unpark = true;
                }
            }
            ConnectionWaiter successor = waiter.mNext;
            if (unpark) {
                if (predecessor != null) {
                    predecessor.mNext = successor;
                } else {
                    this.mConnectionWaiterQueue = successor;
                }
                waiter.mNext = null;
                LockSupport.unpark(waiter.mThread);
            } else {
                predecessor = waiter;
            }
            waiter = successor;
        }
    }

    @GuardedBy("mLock")
    private synchronized SQLiteConnection tryAcquirePrimaryConnectionLocked(int connectionFlags) {
        SQLiteConnection connection = this.mAvailablePrimaryConnection;
        if (connection != null) {
            this.mAvailablePrimaryConnection = null;
            finishAcquireConnectionLocked(connection, connectionFlags);
            return connection;
        }
        for (SQLiteConnection acquiredConnection : this.mAcquiredConnections.keySet()) {
            if (acquiredConnection.isPrimaryConnection()) {
                return null;
            }
        }
        SQLiteConnection connection2 = openConnectionLocked(this.mConfiguration, true);
        finishAcquireConnectionLocked(connection2, connectionFlags);
        return connection2;
    }

    @GuardedBy("mLock")
    private synchronized SQLiteConnection tryAcquireNonPrimaryConnectionLocked(String sql, int connectionFlags) {
        int availableCount = this.mAvailableNonPrimaryConnections.size();
        if (availableCount > 1 && sql != null) {
            for (int i = 0; i < availableCount; i++) {
                SQLiteConnection connection = this.mAvailableNonPrimaryConnections.get(i);
                if (connection.isPreparedStatementInCache(sql)) {
                    this.mAvailableNonPrimaryConnections.remove(i);
                    finishAcquireConnectionLocked(connection, connectionFlags);
                    return connection;
                }
            }
        }
        if (availableCount > 0) {
            SQLiteConnection connection2 = this.mAvailableNonPrimaryConnections.remove(availableCount - 1);
            finishAcquireConnectionLocked(connection2, connectionFlags);
            return connection2;
        }
        int openConnections = this.mAcquiredConnections.size();
        if (this.mAvailablePrimaryConnection != null) {
            openConnections++;
        }
        if (openConnections < this.mMaxConnectionPoolSize) {
            SQLiteConnection connection3 = openConnectionLocked(this.mConfiguration, false);
            finishAcquireConnectionLocked(connection3, connectionFlags);
            return connection3;
        }
        return null;
    }

    @GuardedBy("mLock")
    private synchronized void finishAcquireConnectionLocked(SQLiteConnection connection, int connectionFlags) {
        boolean readOnly = (connectionFlags & 1) != 0;
        try {
            connection.setOnlyAllowReadOnlyOperations(readOnly);
            this.mAcquiredConnections.put(connection, AcquiredConnectionStatus.NORMAL);
        } catch (RuntimeException ex) {
            Log.e(TAG, "Failed to prepare acquired connection for session, closing it: " + connection + ", connectionFlags=" + connectionFlags);
            closeConnectionAndLogExceptionsLocked(connection);
            throw ex;
        }
    }

    private synchronized boolean isSessionBlockingImportantConnectionWaitersLocked(boolean holdingPrimaryConnection, int connectionFlags) {
        ConnectionWaiter waiter = this.mConnectionWaiterQueue;
        if (waiter != null) {
            int priority = getPriority(connectionFlags);
            while (priority <= waiter.mPriority) {
                if (holdingPrimaryConnection || !waiter.mWantPrimaryConnection) {
                    return true;
                }
                waiter = waiter.mNext;
                if (waiter == null) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    private static synchronized int getPriority(int connectionFlags) {
        return (connectionFlags & 4) != 0 ? 1 : 0;
    }

    private synchronized void setMaxConnectionPoolSizeLocked() {
        if (!this.mConfiguration.isInMemoryDb() && (this.mConfiguration.openFlags & 536870912) != 0) {
            this.mMaxConnectionPoolSize = SQLiteGlobal.getWALConnectionPoolSize();
        } else {
            this.mMaxConnectionPoolSize = 1;
        }
    }

    @VisibleForTesting
    public synchronized void setupIdleConnectionHandler(Looper looper, long timeoutMs) {
        synchronized (this.mLock) {
            this.mIdleConnectionHandler = new IdleConnectionHandler(looper, timeoutMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void disableIdleConnectionHandler() {
        synchronized (this.mLock) {
            this.mIdleConnectionHandler = null;
        }
    }

    private synchronized void throwIfClosedLocked() {
        if (!this.mIsOpen) {
            throw new IllegalStateException("Cannot perform this operation because the connection pool has been closed.");
        }
    }

    private synchronized ConnectionWaiter obtainConnectionWaiterLocked(Thread thread, long startTime, int priority, boolean wantPrimaryConnection, String sql, int connectionFlags) {
        ConnectionWaiter waiter = this.mConnectionWaiterPool;
        if (waiter != null) {
            this.mConnectionWaiterPool = waiter.mNext;
            waiter.mNext = null;
        } else {
            waiter = new ConnectionWaiter();
        }
        waiter.mThread = thread;
        waiter.mStartTime = startTime;
        waiter.mPriority = priority;
        waiter.mWantPrimaryConnection = wantPrimaryConnection;
        waiter.mSql = sql;
        waiter.mConnectionFlags = connectionFlags;
        return waiter;
    }

    private synchronized void recycleConnectionWaiterLocked(ConnectionWaiter waiter) {
        waiter.mNext = this.mConnectionWaiterPool;
        waiter.mThread = null;
        waiter.mSql = null;
        waiter.mAssignedConnection = null;
        waiter.mException = null;
        waiter.mNonce++;
        this.mConnectionWaiterPool = waiter;
    }

    public synchronized void dump(Printer printer, boolean verbose) {
        Printer indentedPrinter = PrefixPrinter.create(printer, "    ");
        synchronized (this.mLock) {
            printer.println("Connection pool for " + this.mConfiguration.path + SettingsStringUtil.DELIMITER);
            StringBuilder sb = new StringBuilder();
            sb.append("  Open: ");
            sb.append(this.mIsOpen);
            printer.println(sb.toString());
            printer.println("  Max connections: " + this.mMaxConnectionPoolSize);
            printer.println("  Total execution time: " + this.mTotalExecutionTimeCounter);
            printer.println("  Configuration: openFlags=" + this.mConfiguration.openFlags + ", useCompatibilityWal=" + this.mConfiguration.useCompatibilityWal() + ", journalMode=" + TextUtils.emptyIfNull(this.mConfiguration.journalMode) + ", syncMode=" + TextUtils.emptyIfNull(this.mConfiguration.syncMode));
            if (SQLiteCompatibilityWalFlags.areFlagsSet()) {
                printer.println("  Compatibility WAL settings: compatibility_wal_supported=" + SQLiteCompatibilityWalFlags.isCompatibilityWalSupported() + ", wal_syncmode=" + SQLiteCompatibilityWalFlags.getWALSyncMode());
            }
            if (this.mConfiguration.isLookasideConfigSet()) {
                printer.println("  Lookaside config: sz=" + this.mConfiguration.lookasideSlotSize + " cnt=" + this.mConfiguration.lookasideSlotCount);
            }
            if (this.mConfiguration.idleConnectionTimeoutMs != Long.MAX_VALUE) {
                printer.println("  Idle connection timeout: " + this.mConfiguration.idleConnectionTimeoutMs);
            }
            printer.println("  Available primary connection:");
            if (this.mAvailablePrimaryConnection != null) {
                this.mAvailablePrimaryConnection.dump(indentedPrinter, verbose);
            } else {
                indentedPrinter.println("<none>");
            }
            printer.println("  Available non-primary connections:");
            if (!this.mAvailableNonPrimaryConnections.isEmpty()) {
                int count = this.mAvailableNonPrimaryConnections.size();
                for (int i = 0; i < count; i++) {
                    this.mAvailableNonPrimaryConnections.get(i).dump(indentedPrinter, verbose);
                }
            } else {
                indentedPrinter.println("<none>");
            }
            printer.println("  Acquired connections:");
            if (!this.mAcquiredConnections.isEmpty()) {
                for (Map.Entry<SQLiteConnection, AcquiredConnectionStatus> entry : this.mAcquiredConnections.entrySet()) {
                    SQLiteConnection connection = entry.getKey();
                    connection.dumpUnsafe(indentedPrinter, verbose);
                    indentedPrinter.println("  Status: " + entry.getValue());
                }
            } else {
                indentedPrinter.println("<none>");
            }
            printer.println("  Connection waiters:");
            if (this.mConnectionWaiterQueue != null) {
                int i2 = 0;
                long now = SystemClock.uptimeMillis();
                ConnectionWaiter waiter = this.mConnectionWaiterQueue;
                while (waiter != null) {
                    indentedPrinter.println(i2 + ": waited for " + (((float) (now - waiter.mStartTime)) * 0.001f) + " ms - thread=" + waiter.mThread + ", priority=" + waiter.mPriority + ", sql='" + waiter.mSql + "'");
                    waiter = waiter.mNext;
                    i2++;
                }
            } else {
                indentedPrinter.println("<none>");
            }
        }
    }

    public String toString() {
        return "SQLiteConnectionPool: " + this.mConfiguration.path;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ConnectionWaiter {
        public SQLiteConnection mAssignedConnection;
        public int mConnectionFlags;
        public RuntimeException mException;
        public ConnectionWaiter mNext;
        public int mNonce;
        public int mPriority;
        public String mSql;
        public long mStartTime;
        public Thread mThread;
        public boolean mWantPrimaryConnection;

        private synchronized ConnectionWaiter() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class IdleConnectionHandler extends Handler {
        private final long mTimeout;

        IdleConnectionHandler(Looper looper, long timeout) {
            super(looper);
            this.mTimeout = timeout;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            synchronized (SQLiteConnectionPool.this.mLock) {
                if (this != SQLiteConnectionPool.this.mIdleConnectionHandler) {
                    return;
                }
                if (SQLiteConnectionPool.this.closeAvailableConnectionLocked(msg.what) && Log.isLoggable(SQLiteConnectionPool.TAG, 3)) {
                    Log.d(SQLiteConnectionPool.TAG, "Closed idle connection " + SQLiteConnectionPool.this.mConfiguration.label + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + msg.what + " after " + this.mTimeout);
                }
            }
        }

        synchronized void connectionReleased(SQLiteConnection con) {
            sendEmptyMessageDelayed(con.getConnectionId(), this.mTimeout);
        }

        synchronized void connectionAcquired(SQLiteConnection con) {
            removeMessages(con.getConnectionId());
        }

        synchronized void connectionClosed(SQLiteConnection con) {
            removeMessages(con.getConnectionId());
        }
    }
}
