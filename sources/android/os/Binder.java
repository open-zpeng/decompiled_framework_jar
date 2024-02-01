package android.os;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.IBinder;
import android.util.ExceptionUtils;
import android.util.Log;
import com.android.internal.os.BinderInternal;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.FunctionalUtils;
import dalvik.annotation.optimization.CriticalNative;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import libcore.io.IoUtils;
import libcore.util.NativeAllocationRegistry;

/* loaded from: classes2.dex */
public class Binder implements IBinder {
    public static final boolean CHECK_PARCEL_SIZE = false;
    private static final boolean FIND_POTENTIAL_LEAKS = false;
    private static final int NATIVE_ALLOCATION_SIZE = 500;
    static final String TAG = "Binder";
    public static final int UNSET_WORKSOURCE = -1;
    private String mDescriptor;
    @UnsupportedAppUsage
    private final long mObject;
    private IInterface mOwner;
    public static boolean LOG_RUNTIME_EXCEPTION = false;
    private static volatile String sDumpDisabled = null;
    private static volatile TransactionTracker sTransactionTracker = null;
    private static BinderInternal.Observer sObserver = null;
    private static volatile boolean sTracingEnabled = false;
    static volatile boolean sWarnOnBlocking = false;
    private static volatile BinderInternal.WorkSourceProvider sWorkSourceProvider = new BinderInternal.WorkSourceProvider() { // from class: android.os.-$$Lambda$Binder$IYUHVkWouPK_9CG2s8VwyWBt5_I
        @Override // com.android.internal.os.BinderInternal.WorkSourceProvider
        public final int resolveWorkSourceUid(int i) {
            int callingUid;
            callingUid = Binder.getCallingUid();
            return callingUid;
        }
    };

    @SystemApi
    /* loaded from: classes2.dex */
    public interface ProxyTransactListener {
        void onTransactEnded(Object obj);

        Object onTransactStarted(IBinder iBinder, int i);
    }

    public static final native void blockUntilThreadAvailable();

    @CriticalNative
    public static final native long clearCallingIdentity();

    @CriticalNative
    public static final native long clearCallingWorkSource();

    public static final native void flushPendingCommands();

    @CriticalNative
    public static final native int getCallingPid();

    @CriticalNative
    public static final native int getCallingUid();

    @CriticalNative
    public static final native int getCallingWorkSourceUid();

    private static native long getFinalizer();

    private static native long getNativeBBinderHolder();

    private static native long getNativeFinalizer();

    @CriticalNative
    public static final native int getThreadStrictModePolicy();

    @CriticalNative
    public static final native boolean isHandlingTransaction();

    public static final native void restoreCallingIdentity(long j);

    @CriticalNative
    public static final native void restoreCallingWorkSource(long j);

    @CriticalNative
    public static final native long setCallingWorkSourceUid(int i);

    @CriticalNative
    public static final native void setThreadStrictModePolicy(int i);

    static /* synthetic */ long access$000() {
        return getNativeFinalizer();
    }

    /* loaded from: classes2.dex */
    private static class NoImagePreloadHolder {
        public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Binder.class.getClassLoader(), Binder.access$000(), 500);

        private NoImagePreloadHolder() {
        }
    }

    public static void enableTracing() {
        sTracingEnabled = true;
    }

    public static void disableTracing() {
        sTracingEnabled = false;
    }

    public static boolean isTracingEnabled() {
        return sTracingEnabled;
    }

    public static synchronized TransactionTracker getTransactionTracker() {
        TransactionTracker transactionTracker;
        synchronized (Binder.class) {
            if (sTransactionTracker == null) {
                sTransactionTracker = new TransactionTracker();
            }
            transactionTracker = sTransactionTracker;
        }
        return transactionTracker;
    }

    public static void setObserver(BinderInternal.Observer observer) {
        sObserver = observer;
    }

    public static void setWarnOnBlocking(boolean warnOnBlocking) {
        sWarnOnBlocking = warnOnBlocking;
    }

    public static IBinder allowBlocking(IBinder binder) {
        try {
            if (binder instanceof BinderProxy) {
                ((BinderProxy) binder).mWarnOnBlocking = false;
            } else if (binder != null && binder.getInterfaceDescriptor() != null && binder.queryLocalInterface(binder.getInterfaceDescriptor()) == null) {
                Log.w(TAG, "Unable to allow blocking on interface " + binder);
            }
        } catch (RemoteException e) {
        }
        return binder;
    }

    public static IBinder defaultBlocking(IBinder binder) {
        if (binder instanceof BinderProxy) {
            ((BinderProxy) binder).mWarnOnBlocking = sWarnOnBlocking;
        }
        return binder;
    }

    public static void copyAllowBlocking(IBinder fromBinder, IBinder toBinder) {
        if ((fromBinder instanceof BinderProxy) && (toBinder instanceof BinderProxy)) {
            ((BinderProxy) toBinder).mWarnOnBlocking = ((BinderProxy) fromBinder).mWarnOnBlocking;
        }
    }

    public static final int getCallingUidOrThrow() {
        if (!isHandlingTransaction()) {
            throw new IllegalStateException("Thread is not in a binder transcation");
        }
        return getCallingUid();
    }

    public static final UserHandle getCallingUserHandle() {
        return UserHandle.of(UserHandle.getUserId(getCallingUid()));
    }

    public static final void withCleanCallingIdentity(FunctionalUtils.ThrowingRunnable action) {
        long callingIdentity = clearCallingIdentity();
        try {
            action.runOrThrow();
            restoreCallingIdentity(callingIdentity);
            if (0 != 0) {
                throw ExceptionUtils.propagate(null);
            }
        } catch (Throwable throwable) {
            restoreCallingIdentity(callingIdentity);
            throw ExceptionUtils.propagate(throwable);
        }
    }

    public static final <T> T withCleanCallingIdentity(FunctionalUtils.ThrowingSupplier<T> action) {
        long callingIdentity = clearCallingIdentity();
        try {
            T orThrow = action.getOrThrow();
            restoreCallingIdentity(callingIdentity);
            if (0 == 0) {
                return orThrow;
            }
            throw ExceptionUtils.propagate(null);
        } catch (Throwable throwable) {
            restoreCallingIdentity(callingIdentity);
            throw ExceptionUtils.propagate(throwable);
        }
    }

    public static final void joinThreadPool() {
        BinderInternal.joinThreadPool();
    }

    public static final boolean isProxy(IInterface iface) {
        return iface.asBinder() != iface;
    }

    public Binder() {
        this(null);
    }

    public Binder(String descriptor) {
        this.mObject = getNativeBBinderHolder();
        NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, this.mObject);
        this.mDescriptor = descriptor;
    }

    public void attachInterface(IInterface owner, String descriptor) {
        this.mOwner = owner;
        this.mDescriptor = descriptor;
    }

    @Override // android.os.IBinder
    public String getInterfaceDescriptor() {
        return this.mDescriptor;
    }

    @Override // android.os.IBinder
    public boolean pingBinder() {
        return true;
    }

    @Override // android.os.IBinder
    public boolean isBinderAlive() {
        return true;
    }

    @Override // android.os.IBinder
    public IInterface queryLocalInterface(String descriptor) {
        String str = this.mDescriptor;
        if (str != null && str.equals(descriptor)) {
            return this.mOwner;
        }
        return null;
    }

    public static void setDumpDisabled(String msg) {
        sDumpDisabled = msg;
    }

    /* loaded from: classes2.dex */
    public static class PropagateWorkSourceTransactListener implements ProxyTransactListener {
        @Override // android.os.Binder.ProxyTransactListener
        public Object onTransactStarted(IBinder binder, int transactionCode) {
            int uid = ThreadLocalWorkSource.getUid();
            if (uid != -1) {
                return Long.valueOf(Binder.setCallingWorkSourceUid(uid));
            }
            return null;
        }

        @Override // android.os.Binder.ProxyTransactListener
        public void onTransactEnded(Object session) {
            if (session != null) {
                long token = ((Long) session).longValue();
                Binder.restoreCallingWorkSource(token);
            }
        }
    }

    @SystemApi
    public static void setProxyTransactListener(ProxyTransactListener listener) {
        BinderProxy.setTransactListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        FileDescriptor fileDescriptor;
        if (code != 1598968902) {
            if (code != 1598311760) {
                if (code == 1598246212) {
                    ParcelFileDescriptor in = data.readFileDescriptor();
                    ParcelFileDescriptor out = data.readFileDescriptor();
                    ParcelFileDescriptor err = data.readFileDescriptor();
                    String[] args = data.readStringArray();
                    ShellCallback shellCallback = ShellCallback.CREATOR.createFromParcel(data);
                    ResultReceiver resultReceiver = ResultReceiver.CREATOR.createFromParcel(data);
                    if (out != null) {
                        if (in == null) {
                            fileDescriptor = null;
                        } else {
                            try {
                                fileDescriptor = in.getFileDescriptor();
                            } catch (Throwable th) {
                                IoUtils.closeQuietly(in);
                                IoUtils.closeQuietly(out);
                                IoUtils.closeQuietly(err);
                                if (reply != null) {
                                    reply.writeNoException();
                                } else {
                                    StrictMode.clearGatheredViolations();
                                }
                                throw th;
                            }
                        }
                        shellCommand(fileDescriptor, out.getFileDescriptor(), err != null ? err.getFileDescriptor() : out.getFileDescriptor(), args, shellCallback, resultReceiver);
                    }
                    IoUtils.closeQuietly(in);
                    IoUtils.closeQuietly(out);
                    IoUtils.closeQuietly(err);
                    if (reply != null) {
                        reply.writeNoException();
                    } else {
                        StrictMode.clearGatheredViolations();
                    }
                    return true;
                }
                return false;
            }
            ParcelFileDescriptor fd = data.readFileDescriptor();
            String[] args2 = data.readStringArray();
            if (fd != null) {
                try {
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    dump(fd.getFileDescriptor(), args2);
                    IoUtils.closeQuietly(fd);
                } catch (Throwable th3) {
                    th = th3;
                    IoUtils.closeQuietly(fd);
                    throw th;
                }
            }
            if (reply != null) {
                reply.writeNoException();
            } else {
                StrictMode.clearGatheredViolations();
            }
            return true;
        }
        reply.writeString(getInterfaceDescriptor());
        return true;
    }

    public String getTransactionName(int transactionCode) {
        return null;
    }

    @Override // android.os.IBinder
    public void dump(FileDescriptor fd, String[] args) {
        FileOutputStream fout = new FileOutputStream(fd);
        PrintWriter pw = new FastPrintWriter(fout);
        try {
            doDump(fd, pw, args);
        } finally {
            pw.flush();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void doDump(FileDescriptor fd, PrintWriter pw, String[] args) {
        String disabled = sDumpDisabled;
        if (disabled == null) {
            try {
                dump(fd, pw, args);
                return;
            } catch (SecurityException e) {
                pw.println("Security exception: " + e.getMessage());
                throw e;
            } catch (Throwable e2) {
                pw.println();
                pw.println("Exception occurred while dumping:");
                e2.printStackTrace(pw);
                return;
            }
        }
        pw.println(sDumpDisabled);
    }

    @Override // android.os.IBinder
    public void dumpAsync(final FileDescriptor fd, final String[] args) {
        FileOutputStream fout = new FileOutputStream(fd);
        final PrintWriter pw = new FastPrintWriter(fout);
        Thread thr = new Thread("Binder.dumpAsync") { // from class: android.os.Binder.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    Binder.this.dump(fd, pw, args);
                } finally {
                    pw.flush();
                }
            }
        };
        thr.start();
    }

    protected void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
    }

    @Override // android.os.IBinder
    public void shellCommand(FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, ResultReceiver resultReceiver) throws RemoteException {
        onShellCommand(in, out, err, args, callback, resultReceiver);
    }

    public void onShellCommand(FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, ResultReceiver resultReceiver) throws RemoteException {
        FileOutputStream fout = new FileOutputStream(err != null ? err : out);
        PrintWriter pw = new FastPrintWriter(fout);
        pw.println("No shell command implementation.");
        pw.flush();
        resultReceiver.send(0, null);
    }

    @Override // android.os.IBinder
    public final boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if (data != null) {
            data.setDataPosition(0);
        }
        boolean r = onTransact(code, data, reply, flags);
        if (reply != null) {
            reply.setDataPosition(0);
        }
        return r;
    }

    @Override // android.os.IBinder
    public void linkToDeath(IBinder.DeathRecipient recipient, int flags) {
    }

    @Override // android.os.IBinder
    public boolean unlinkToDeath(IBinder.DeathRecipient recipient, int flags) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkParcel(IBinder obj, int code, Parcel parcel, String msg) {
    }

    public static void setWorkSourceProvider(BinderInternal.WorkSourceProvider workSourceProvider) {
        if (workSourceProvider == null) {
            throw new IllegalArgumentException("workSourceProvider cannot be null");
        }
        sWorkSourceProvider = workSourceProvider;
    }

    @UnsupportedAppUsage
    private boolean execTransact(int code, long dataObj, long replyObj, int flags) {
        int callingUid = getCallingUid();
        long origWorkSource = ThreadLocalWorkSource.setUid(callingUid);
        try {
            return execTransactInternal(code, dataObj, replyObj, flags, callingUid);
        } finally {
            ThreadLocalWorkSource.restore(origWorkSource);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00a1, code lost:
        if (r4 != null) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00a4, code lost:
        checkParcel(r14, r15, r7, "Unreasonably large binder reply buffer");
        r7.recycle();
        r6.recycle();
        android.os.StrictMode.clearGatheredViolations();
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00b2, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean execTransactInternal(int r15, long r16, long r18, int r20, int r21) {
        /*
            r14 = this;
            r1 = r14
            r2 = r15
            r3 = r20
            com.android.internal.os.BinderInternal$Observer r4 = android.os.Binder.sObserver
            if (r4 == 0) goto Le
            r0 = -1
            com.android.internal.os.BinderInternal$CallSession r0 = r4.callStarted(r14, r15, r0)
            goto Lf
        Le:
            r0 = 0
        Lf:
            r5 = r0
            android.os.Parcel r6 = android.os.Parcel.obtain(r16)
            android.os.Parcel r7 = android.os.Parcel.obtain(r18)
            boolean r8 = isTracingEnabled()
            r9 = 1
            if (r8 == 0) goto L4b
            java.lang.String r0 = r14.getTransactionName(r15)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
            r11.<init>()     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
            java.lang.Class r12 = r14.getClass()     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
            java.lang.String r12 = r12.getName()     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
            r11.append(r12)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
            java.lang.String r12 = ":"
            r11.append(r12)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
            if (r0 == 0) goto L3d
            r12 = r0
            goto L41
        L3d:
            java.lang.Integer r12 = java.lang.Integer.valueOf(r15)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
        L41:
            r11.append(r12)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
            java.lang.String r11 = r11.toString()     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
            android.os.Trace.traceBegin(r9, r11)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
        L4b:
            boolean r0 = r14.onTransact(r15, r6, r7, r3)     // Catch: java.lang.Throwable -> L6c java.lang.Throwable -> L6e
            if (r8 == 0) goto L54
            android.os.Trace.traceEnd(r9)
        L54:
            if (r4 == 0) goto La4
        L56:
            com.android.internal.os.BinderInternal$WorkSourceProvider r9 = android.os.Binder.sWorkSourceProvider
            int r10 = r6.readCallingWorkSourceUid()
            int r9 = r9.resolveWorkSourceUid(r10)
            int r10 = r6.dataSize()
            int r11 = r7.dataSize()
            r4.callEnded(r5, r10, r11, r9)
            goto La4
        L6c:
            r0 = move-exception
            goto Lb3
        L6e:
            r0 = move-exception
            if (r4 == 0) goto L74
            r4.callThrewException(r5, r0)     // Catch: java.lang.Throwable -> L6c
        L74:
            boolean r11 = android.os.Binder.LOG_RUNTIME_EXCEPTION     // Catch: java.lang.Throwable -> L6c
            java.lang.String r12 = "Caught a RuntimeException from the binder stub implementation."
            java.lang.String r13 = "Binder"
            if (r11 == 0) goto L7f
            android.util.Log.w(r13, r12, r0)     // Catch: java.lang.Throwable -> L6c
        L7f:
            r11 = r3 & 1
            if (r11 == 0) goto L91
            boolean r11 = r0 instanceof android.os.RemoteException     // Catch: java.lang.Throwable -> L6c
            if (r11 == 0) goto L8d
            java.lang.String r11 = "Binder call failed."
            android.util.Log.w(r13, r11, r0)     // Catch: java.lang.Throwable -> L6c
            goto L9b
        L8d:
            android.util.Log.w(r13, r12, r0)     // Catch: java.lang.Throwable -> L6c
            goto L9b
        L91:
            r11 = 0
            r7.setDataSize(r11)     // Catch: java.lang.Throwable -> L6c
            r7.setDataPosition(r11)     // Catch: java.lang.Throwable -> L6c
            r7.writeException(r0)     // Catch: java.lang.Throwable -> L6c
        L9b:
            r0 = 1
            if (r8 == 0) goto La1
            android.os.Trace.traceEnd(r9)
        La1:
            if (r4 == 0) goto La4
            goto L56
        La4:
            java.lang.String r9 = "Unreasonably large binder reply buffer"
            checkParcel(r14, r15, r7, r9)
            r7.recycle()
            r6.recycle()
            android.os.StrictMode.clearGatheredViolations()
            return r0
        Lb3:
            if (r8 == 0) goto Lb8
            android.os.Trace.traceEnd(r9)
        Lb8:
            if (r4 == 0) goto Lcf
            com.android.internal.os.BinderInternal$WorkSourceProvider r9 = android.os.Binder.sWorkSourceProvider
            int r10 = r6.readCallingWorkSourceUid()
            int r9 = r9.resolveWorkSourceUid(r10)
            int r10 = r6.dataSize()
            int r11 = r7.dataSize()
            r4.callEnded(r5, r10, r11, r9)
        Lcf:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Binder.execTransactInternal(int, long, long, int, int):boolean");
    }
}
