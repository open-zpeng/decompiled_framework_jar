package android.os;

import android.os.IBinder;
import android.util.ExceptionUtils;
import android.util.Log;
import com.android.internal.os.BinderInternal;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.FunctionalUtils;
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
    private String mDescriptor;
    public protected final long mObject = getNativeBBinderHolder();
    private IInterface mOwner;
    public static boolean LOG_RUNTIME_EXCEPTION = false;
    private static volatile String sDumpDisabled = null;
    private static volatile TransactionTracker sTransactionTracker = null;
    private static volatile boolean sTracingEnabled = false;
    static volatile boolean sWarnOnBlocking = false;

    public static final native void blockUntilThreadAvailable();

    public static final native long clearCallingIdentity();

    public static final native void flushPendingCommands();

    public static final native int getCallingPid();

    public static final native int getCallingUid();

    private static native long getFinalizer();

    private static native long getNativeBBinderHolder();

    private static native long getNativeFinalizer();

    public static final native int getThreadStrictModePolicy();

    public static final native void restoreCallingIdentity(long j);

    public static final native void setThreadStrictModePolicy(int i);

    static /* synthetic */ long access$000() {
        return getNativeFinalizer();
    }

    /* loaded from: classes2.dex */
    private static class NoImagePreloadHolder {
        public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Binder.class.getClassLoader(), Binder.access$000(), 500);

        private synchronized NoImagePreloadHolder() {
        }
    }

    public static synchronized void enableTracing() {
        sTracingEnabled = true;
    }

    public static synchronized void disableTracing() {
        sTracingEnabled = false;
    }

    public static synchronized boolean isTracingEnabled() {
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

    public static synchronized void setWarnOnBlocking(boolean warnOnBlocking) {
        sWarnOnBlocking = warnOnBlocking;
    }

    public static synchronized IBinder allowBlocking(IBinder binder) {
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

    public static synchronized IBinder defaultBlocking(IBinder binder) {
        if (binder instanceof BinderProxy) {
            ((BinderProxy) binder).mWarnOnBlocking = sWarnOnBlocking;
        }
        return binder;
    }

    public static synchronized void copyAllowBlocking(IBinder fromBinder, IBinder toBinder) {
        if ((fromBinder instanceof BinderProxy) && (toBinder instanceof BinderProxy)) {
            ((BinderProxy) toBinder).mWarnOnBlocking = ((BinderProxy) fromBinder).mWarnOnBlocking;
        }
    }

    public static final UserHandle getCallingUserHandle() {
        return UserHandle.of(UserHandle.getUserId(getCallingUid()));
    }

    public static final synchronized void withCleanCallingIdentity(FunctionalUtils.ThrowingRunnable action) {
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

    public static final synchronized <T> T withCleanCallingIdentity(FunctionalUtils.ThrowingSupplier<T> action) {
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

    public static final synchronized boolean isProxy(IInterface iface) {
        return iface.asBinder() != iface;
    }

    public Binder() {
        NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, this.mObject);
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
        if (this.mDescriptor != null && this.mDescriptor.equals(descriptor)) {
            return this.mOwner;
        }
        return null;
    }

    public static synchronized void setDumpDisabled(String msg) {
        sDumpDisabled = msg;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        FileDescriptor fileDescriptor;
        if (code == 1598968902) {
            reply.writeString(getInterfaceDescriptor());
            return true;
        } else if (code == 1598311760) {
            ParcelFileDescriptor fd = data.readFileDescriptor();
            String[] args = data.readStringArray();
            if (fd != null) {
                try {
                    try {
                        dump(fd.getFileDescriptor(), args);
                        IoUtils.closeQuietly(fd);
                    } catch (Throwable th) {
                        th = th;
                        IoUtils.closeQuietly(fd);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            if (reply != null) {
                reply.writeNoException();
            } else {
                StrictMode.clearGatheredViolations();
            }
            return true;
        } else if (code == 1598246212) {
            ParcelFileDescriptor in = data.readFileDescriptor();
            ParcelFileDescriptor out = data.readFileDescriptor();
            ParcelFileDescriptor err = data.readFileDescriptor();
            String[] args2 = data.readStringArray();
            ShellCallback shellCallback = ShellCallback.CREATOR.createFromParcel(data);
            ResultReceiver resultReceiver = ResultReceiver.CREATOR.createFromParcel(data);
            if (out != null) {
                if (in == null) {
                    fileDescriptor = null;
                } else {
                    try {
                        fileDescriptor = in.getFileDescriptor();
                    } catch (Throwable th3) {
                        IoUtils.closeQuietly(in);
                        IoUtils.closeQuietly(out);
                        IoUtils.closeQuietly(err);
                        if (reply != null) {
                            reply.writeNoException();
                        } else {
                            StrictMode.clearGatheredViolations();
                        }
                        throw th3;
                    }
                }
                shellCommand(fileDescriptor, out.getFileDescriptor(), err != null ? err.getFileDescriptor() : out.getFileDescriptor(), args2, shellCallback, resultReceiver);
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
        } else {
            return false;
        }
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
    public synchronized void doDump(FileDescriptor fd, PrintWriter pw, String[] args) {
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
    public synchronized void shellCommand(FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, ResultReceiver resultReceiver) throws RemoteException {
        onShellCommand(in, out, err, args, callback, resultReceiver);
    }

    public synchronized void onShellCommand(FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, ResultReceiver resultReceiver) throws RemoteException {
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
    public static synchronized void checkParcel(IBinder obj, int code, Parcel parcel, String msg) {
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0045, code lost:
        if (r0 != false) goto L5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0047, code lost:
        android.os.Trace.traceEnd(1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x007a, code lost:
        if (r0 == false) goto L6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x007d, code lost:
        checkParcel(r13, r14, r7, "Unreasonably large binder reply buffer");
        r7.recycle();
        r6.recycle();
        android.os.StrictMode.clearGatheredViolations();
        r4.callEnded(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x008f, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public protected boolean execTransact(int r14, long r15, long r17, int r19) {
        /*
            r13 = this;
            r1 = r13
            r2 = r14
            r3 = r19
            com.android.internal.os.BinderCallsStats r4 = com.android.internal.os.BinderCallsStats.getInstance()
            com.android.internal.os.BinderCallsStats$CallSession r5 = r4.callStarted(r1, r2)
            android.os.Parcel r6 = android.os.Parcel.obtain(r15)
            android.os.Parcel r7 = android.os.Parcel.obtain(r17)
            boolean r0 = isTracingEnabled()
            r8 = r0
            r9 = 1
            if (r8 == 0) goto L41
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L3f
            r0.<init>()     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L3f
            java.lang.Class r11 = r1.getClass()     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L3f
            java.lang.String r11 = r11.getName()     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L3f
            r0.append(r11)     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L3f
            java.lang.String r11 = ":"
            r0.append(r11)     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L3f
            r0.append(r2)     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L3f
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L3f
            android.os.Trace.traceBegin(r9, r0)     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L3f
            goto L41
        L3d:
            r0 = move-exception
            goto L90
        L3f:
            r0 = move-exception
            goto L4b
        L41:
            boolean r0 = r1.onTransact(r2, r6, r7, r3)     // Catch: java.lang.Throwable -> L3d java.lang.Throwable -> L3f
            if (r8 == 0) goto L7d
        L47:
            android.os.Trace.traceEnd(r9)
            goto L7d
        L4b:
            boolean r11 = android.os.Binder.LOG_RUNTIME_EXCEPTION     // Catch: java.lang.Throwable -> L3d
            if (r11 == 0) goto L57
            java.lang.String r11 = "Binder"
            java.lang.String r12 = "Caught a RuntimeException from the binder stub implementation."
            android.util.Log.w(r11, r12, r0)     // Catch: java.lang.Throwable -> L3d
        L57:
            r11 = r3 & 1
            if (r11 == 0) goto L6f
            boolean r11 = r0 instanceof android.os.RemoteException     // Catch: java.lang.Throwable -> L3d
            if (r11 == 0) goto L67
            java.lang.String r11 = "Binder"
            java.lang.String r12 = "Binder call failed."
            android.util.Log.w(r11, r12, r0)     // Catch: java.lang.Throwable -> L3d
            goto L79
        L67:
            java.lang.String r11 = "Binder"
            java.lang.String r12 = "Caught a RuntimeException from the binder stub implementation."
            android.util.Log.w(r11, r12, r0)     // Catch: java.lang.Throwable -> L3d
            goto L79
        L6f:
            r11 = 0
            r7.setDataSize(r11)     // Catch: java.lang.Throwable -> L3d
            r7.setDataPosition(r11)     // Catch: java.lang.Throwable -> L3d
            r7.writeException(r0)     // Catch: java.lang.Throwable -> L3d
        L79:
            r0 = 1
            if (r8 == 0) goto L7d
            goto L47
        L7d:
            java.lang.String r9 = "Unreasonably large binder reply buffer"
            checkParcel(r1, r2, r7, r9)
            r7.recycle()
            r6.recycle()
            android.os.StrictMode.clearGatheredViolations()
            r4.callEnded(r5)
            return r0
        L90:
            if (r8 == 0) goto L95
            android.os.Trace.traceEnd(r9)
        L95:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Binder.execTransact(int, long, long, int):boolean");
    }
}
