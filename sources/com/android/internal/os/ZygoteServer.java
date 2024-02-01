package com.android.internal.os;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.SystemClock;
import android.os.Trace;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;
import dalvik.system.ZygoteHooks;
import java.io.FileDescriptor;
import java.io.IOException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class ZygoteServer {
    public static final String TAG = "ZygoteServer";
    private static final String USAP_POOL_SIZE_MAX_DEFAULT = "10";
    private static final int USAP_POOL_SIZE_MAX_LIMIT = 100;
    private static final String USAP_POOL_SIZE_MIN_DEFAULT = "1";
    private static final int USAP_POOL_SIZE_MIN_LIMIT = 1;
    private boolean mCloseSocketFd;
    private boolean mIsFirstPropertyCheck;
    private boolean mIsForkChild;
    private long mLastPropCheckTimestamp;
    private boolean mUsapPoolEnabled;
    private FileDescriptor mUsapPoolEventFD;
    private int mUsapPoolRefillThreshold;
    private int mUsapPoolSizeMax;
    private int mUsapPoolSizeMin;
    private LocalServerSocket mUsapPoolSocket;
    private final boolean mUsapPoolSupported;
    private LocalServerSocket mZygoteSocket;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZygoteServer() {
        this.mUsapPoolEnabled = false;
        this.mUsapPoolSizeMax = 0;
        this.mUsapPoolSizeMin = 0;
        this.mUsapPoolRefillThreshold = 0;
        this.mIsFirstPropertyCheck = true;
        this.mLastPropCheckTimestamp = 0L;
        this.mUsapPoolEventFD = null;
        this.mZygoteSocket = null;
        this.mUsapPoolSocket = null;
        this.mUsapPoolSupported = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZygoteServer(boolean isPrimaryZygote) {
        this.mUsapPoolEnabled = false;
        this.mUsapPoolSizeMax = 0;
        this.mUsapPoolSizeMin = 0;
        this.mUsapPoolRefillThreshold = 0;
        this.mIsFirstPropertyCheck = true;
        this.mLastPropCheckTimestamp = 0L;
        this.mUsapPoolEventFD = Zygote.getUsapPoolEventFD();
        if (isPrimaryZygote) {
            this.mZygoteSocket = Zygote.createManagedSocketFromInitSocket(Zygote.PRIMARY_SOCKET_NAME);
            this.mUsapPoolSocket = Zygote.createManagedSocketFromInitSocket(Zygote.USAP_POOL_PRIMARY_SOCKET_NAME);
        } else {
            this.mZygoteSocket = Zygote.createManagedSocketFromInitSocket(Zygote.SECONDARY_SOCKET_NAME);
            this.mUsapPoolSocket = Zygote.createManagedSocketFromInitSocket(Zygote.USAP_POOL_SECONDARY_SOCKET_NAME);
        }
        fetchUsapPoolPolicyProps();
        this.mUsapPoolSupported = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForkChild() {
        this.mIsForkChild = true;
    }

    public boolean isUsapPoolEnabled() {
        return this.mUsapPoolEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerServerSocketAtAbstractName(String socketName) {
        if (this.mZygoteSocket == null) {
            try {
                this.mZygoteSocket = new LocalServerSocket(socketName);
                this.mCloseSocketFd = false;
            } catch (IOException ex) {
                throw new RuntimeException("Error binding to abstract socket '" + socketName + "'", ex);
            }
        }
    }

    private ZygoteConnection acceptCommandPeer(String abiList) {
        try {
            return createNewConnection(this.mZygoteSocket.accept(), abiList);
        } catch (IOException ex) {
            throw new RuntimeException("IOException during accept()", ex);
        }
    }

    protected ZygoteConnection createNewConnection(LocalSocket socket, String abiList) throws IOException {
        return new ZygoteConnection(socket, abiList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeServerSocket() {
        try {
            if (this.mZygoteSocket != null) {
                FileDescriptor fd = this.mZygoteSocket.getFileDescriptor();
                this.mZygoteSocket.close();
                if (fd != null && this.mCloseSocketFd) {
                    Os.close(fd);
                }
            }
        } catch (ErrnoException ex) {
            Log.e(TAG, "Zygote:  error closing descriptor", ex);
        } catch (IOException ex2) {
            Log.e(TAG, "Zygote:  error closing sockets", ex2);
        }
        this.mZygoteSocket = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileDescriptor getZygoteSocketFileDescriptor() {
        return this.mZygoteSocket.getFileDescriptor();
    }

    private void fetchUsapPoolPolicyProps() {
        if (this.mUsapPoolSupported) {
            String usapPoolSizeMaxPropString = Zygote.getConfigurationProperty(ZygoteConfig.USAP_POOL_SIZE_MAX, USAP_POOL_SIZE_MAX_DEFAULT);
            if (!usapPoolSizeMaxPropString.isEmpty()) {
                this.mUsapPoolSizeMax = Integer.min(Integer.parseInt(usapPoolSizeMaxPropString), 100);
            }
            String usapPoolSizeMinPropString = Zygote.getConfigurationProperty(ZygoteConfig.USAP_POOL_SIZE_MIN, "1");
            if (!usapPoolSizeMinPropString.isEmpty()) {
                this.mUsapPoolSizeMin = Integer.max(Integer.parseInt(usapPoolSizeMinPropString), 1);
            }
            String usapPoolRefillThresholdPropString = Zygote.getConfigurationProperty(ZygoteConfig.USAP_POOL_REFILL_THRESHOLD, Integer.toString(this.mUsapPoolSizeMax / 2));
            if (!usapPoolRefillThresholdPropString.isEmpty()) {
                this.mUsapPoolRefillThreshold = Integer.min(Integer.parseInt(usapPoolRefillThresholdPropString), this.mUsapPoolSizeMax);
            }
            if (this.mUsapPoolSizeMin >= this.mUsapPoolSizeMax) {
                Log.w(TAG, "The max size of the USAP pool must be greater than the minimum size.  Restoring default values.");
                this.mUsapPoolSizeMax = Integer.parseInt(USAP_POOL_SIZE_MAX_DEFAULT);
                this.mUsapPoolSizeMin = Integer.parseInt("1");
                this.mUsapPoolRefillThreshold = this.mUsapPoolSizeMax / 2;
            }
        }
    }

    private void fetchUsapPoolPolicyPropsWithMinInterval() {
        long currentTimestamp = SystemClock.elapsedRealtime();
        if (this.mIsFirstPropertyCheck || currentTimestamp - this.mLastPropCheckTimestamp >= 60000) {
            this.mIsFirstPropertyCheck = false;
            this.mLastPropCheckTimestamp = currentTimestamp;
            fetchUsapPoolPolicyProps();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Runnable fillUsapPool(int[] sessionSocketRawFDs) {
        Trace.traceBegin(64L, "Zygote:FillUsapPool");
        fetchUsapPoolPolicyPropsWithMinInterval();
        int usapPoolCount = Zygote.getUsapPoolCount();
        int numUsapsToSpawn = this.mUsapPoolSizeMax - usapPoolCount;
        if (usapPoolCount < this.mUsapPoolSizeMin || numUsapsToSpawn >= this.mUsapPoolRefillThreshold) {
            ZygoteHooks.preFork();
            Zygote.resetNicePriority();
            while (true) {
                int usapPoolCount2 = usapPoolCount + 1;
                if (usapPoolCount < this.mUsapPoolSizeMax) {
                    Runnable caller = Zygote.forkUsap(this.mUsapPoolSocket, sessionSocketRawFDs);
                    if (caller != null) {
                        return caller;
                    }
                    usapPoolCount = usapPoolCount2;
                } else {
                    ZygoteHooks.postForkCommon();
                    Log.i(Zygote.PRIMARY_SOCKET_NAME, "Filled the USAP pool. New USAPs: " + numUsapsToSpawn);
                    break;
                }
            }
        }
        Trace.traceEnd(64L);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Runnable setUsapPoolStatus(boolean newStatus, LocalSocket sessionSocket) {
        if (!this.mUsapPoolSupported) {
            Log.w(TAG, "Attempting to enable a USAP pool for a Zygote that doesn't support it.");
            return null;
        } else if (this.mUsapPoolEnabled == newStatus) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("USAP Pool status change: ");
            sb.append(newStatus ? "ENABLED" : "DISABLED");
            Log.i(TAG, sb.toString());
            this.mUsapPoolEnabled = newStatus;
            if (newStatus) {
                return fillUsapPool(new int[]{sessionSocket.getFileDescriptor().getInt$()});
            }
            Zygote.emptyUsapPool();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x01ab, code lost:
        if (r11 == false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01ae, code lost:
        r0 = r0.subList(1, r0.size()).stream().mapToInt(com.android.internal.os.$$Lambda$ZygoteServer$NJVbduCrCzDq0RHpPga7lyCk4eE.INSTANCE).toArray();
        r6 = fillUsapPool(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01c9, code lost:
        if (r6 == null) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x01cb, code lost:
        return r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Runnable runSelectLoop(java.lang.String r19) {
        /*
            Method dump skipped, instructions count: 474
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteServer.runSelectLoop(java.lang.String):java.lang.Runnable");
    }
}
