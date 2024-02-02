package android.os;

import android.app.ActivityManager;
import android.app.TraceManager;
import android.net.wifi.WifiEnterpriseConfig;
import android.text.TextUtils;
import android.util.Log;
import android.util.Printer;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.os.BackgroundThread;
/* loaded from: classes2.dex */
public final class Looper {
    private static final long DEFAULT_COLLECT_DELAY = 2000;
    private static final long DEFAULT_COLLECT_INTERVAL = 3000;
    private static final String TAG = "Looper";
    public protected static Looper sMainLooper;
    private static String sPackageName;
    public protected Printer mLogging;
    public private protected final MessageQueue mQueue;
    private long mSlowDeliveryThresholdMs;
    private long mSlowDispatchThresholdMs;
    private long mTraceTag;
    public private protected static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<>();
    private static long sStart = System.currentTimeMillis();
    private static final long CATCHCATON = SystemProperties.getLong("persist.sys.debug.caton", -1);
    private final String DUMP_TRACE = "com.xiaopeng.dumptrace";
    final Thread mThread = Thread.currentThread();

    public static void prepare() {
        prepare(true);
    }

    private static synchronized void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }

    public static void prepareMainLooper() {
        prepare(false);
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }

    public static Looper getMainLooper() {
        Looper looper;
        synchronized (Looper.class) {
            looper = sMainLooper;
        }
        return looper;
    }

    public static void loop() {
        Looper me;
        long slowDispatchThresholdMs;
        long traceTag;
        long dispatchEnd;
        int thresholdOverride;
        MessageQueue queue;
        Message msg;
        Printer logging;
        boolean slowDeliveryDetected;
        Looper me2 = myLooper();
        if (me2 == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        MessageQueue queue2 = me2.mQueue;
        Binder.clearCallingIdentity();
        long ident = Binder.clearCallingIdentity();
        boolean z = false;
        int thresholdOverride2 = SystemProperties.getInt("log.looper." + Process.myUid() + "." + Thread.currentThread().getName() + ".slow", 0);
        boolean isSystemServer = SystemProperties.getInt("sys.system_server.pid", 0) == Process.myPid();
        boolean needTrace = me2 == getMainLooper() || isSystemServer;
        boolean slowDeliveryDetected2 = false;
        while (true) {
            Message msg2 = queue2.next();
            if (msg2 == null) {
                return;
            }
            Printer logging2 = me2.mLogging;
            if (logging2 != null) {
                logging2.println(">>>>> Dispatching to " + msg2.target + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + msg2.callback + ": " + msg2.what);
            }
            long traceTag2 = me2.mTraceTag;
            boolean needTrace2 = needTrace;
            long slowDispatchThresholdMs2 = me2.mSlowDispatchThresholdMs;
            long slowDeliveryThresholdMs = me2.mSlowDeliveryThresholdMs;
            if (thresholdOverride2 > 0) {
                me = me2;
                slowDispatchThresholdMs = thresholdOverride2;
                slowDeliveryThresholdMs = thresholdOverride2;
            } else {
                me = me2;
                slowDispatchThresholdMs = slowDispatchThresholdMs2;
            }
            if (CATCHCATON > 0) {
                slowDispatchThresholdMs = CATCHCATON;
                slowDeliveryThresholdMs = CATCHCATON;
            }
            long slowDispatchThresholdMs3 = slowDispatchThresholdMs;
            long slowDeliveryThresholdMs2 = slowDeliveryThresholdMs;
            boolean logSlowDelivery = (slowDeliveryThresholdMs2 <= 0 || msg2.when <= 0) ? z : true;
            boolean logSlowDispatch = slowDispatchThresholdMs3 > 0 ? true : z;
            boolean needStartTime = (logSlowDelivery || logSlowDispatch) ? true : z;
            if (traceTag2 != 0 && Trace.isTagEnabled(traceTag2)) {
                Trace.traceBegin(traceTag2, msg2.target.getTraceName(msg2));
            }
            long dispatchStart = needStartTime ? SystemClock.uptimeMillis() : 0L;
            try {
                msg2.target.dispatchMessage(msg2);
                if (logSlowDispatch) {
                    try {
                        dispatchEnd = SystemClock.uptimeMillis();
                    } catch (Throwable th) {
                        th = th;
                        traceTag = traceTag2;
                        if (traceTag != 0) {
                            Trace.traceEnd(traceTag);
                        }
                        throw th;
                    }
                } else {
                    dispatchEnd = 0;
                }
                if (traceTag2 != 0) {
                    Trace.traceEnd(traceTag2);
                }
                if (!logSlowDelivery) {
                    thresholdOverride = thresholdOverride2;
                } else if (slowDeliveryDetected2) {
                    thresholdOverride = thresholdOverride2;
                    if (dispatchStart - msg2.when <= 10) {
                        Slog.w(TAG, "Drained");
                        slowDeliveryDetected = false;
                        slowDeliveryDetected2 = slowDeliveryDetected;
                    }
                } else {
                    thresholdOverride = thresholdOverride2;
                    if (showSlowLog(false, slowDeliveryThresholdMs2, msg2.when, dispatchStart, "delivery", msg2)) {
                        slowDeliveryDetected = true;
                        slowDeliveryDetected2 = slowDeliveryDetected;
                    }
                }
                if (logSlowDispatch) {
                    logging = logging2;
                    queue = queue2;
                    msg = msg2;
                    needTrace = needTrace2;
                    showSlowLog(needTrace, slowDispatchThresholdMs3, dispatchStart, dispatchEnd, "dispatch", msg2);
                } else {
                    queue = queue2;
                    msg = msg2;
                    logging = logging2;
                    needTrace = needTrace2;
                }
                if (logging != null) {
                    logging.println("<<<<< Finished to " + msg.target + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + msg.callback);
                }
                long newIdent = Binder.clearCallingIdentity();
                if (ident != newIdent) {
                    Log.wtf(TAG, "Thread identity changed from 0x" + Long.toHexString(ident) + " to 0x" + Long.toHexString(newIdent) + " while dispatching to " + msg.target.getClass().getName() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + msg.callback + " what=" + msg.what);
                }
                msg.recycleUnchecked();
                me2 = me;
                thresholdOverride2 = thresholdOverride;
                queue2 = queue;
                z = false;
            } catch (Throwable th2) {
                th = th2;
                traceTag = traceTag2;
            }
        }
    }

    private static boolean showSlowLog(boolean needTrace, long threshold, long measureStart, long measureEnd, String what, Message msg) {
        long actualTime = measureEnd - measureStart;
        if (actualTime < threshold) {
            return false;
        }
        Slog.w(TAG, "Slow " + what + " took " + actualTime + "ms " + Thread.currentThread().getName() + " h=" + msg.target.getClass().getName() + " c=" + msg.callback + " m=" + msg.what);
        if (!needTrace || CATCHCATON < 0) {
            return true;
        }
        final long end = System.currentTimeMillis();
        if (end - sStart < 3000) {
            return true;
        }
        sStart = end;
        BackgroundThread.getHandler().postDelayed(new Runnable() { // from class: android.os.-$$Lambda$Looper$j6JdufBj0TcAZ-8immqhoTdymUg
            @Override // java.lang.Runnable
            public final void run() {
                Looper.lambda$showSlowLog$0(end);
            }
        }, 2000L);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$showSlowLog$0(long end) {
        int myPid = Process.myPid();
        if (TextUtils.isEmpty(sPackageName)) {
            sPackageName = getPackageNamesFromPid(myPid);
        }
        if (sPackageName != null) {
            try {
                TraceManager.getService().catchCaton(sPackageName.replace("com.xiaopeng.", "") + "_" + myPid + "_" + end, sPackageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getPackageNamesFromPid(int pid) {
        try {
        } catch (RemoteException e) {
            Log.w(TAG, "ActivityManager.getRunningAppProcesses() RemoteException failed");
        } catch (SecurityException e2) {
            Log.w(TAG, "ActivityManager.getRunningAppProcesses() SecurityException failed");
        }
        if (pid == SystemProperties.getInt("sys.system_server.pid", 0)) {
            return "system_server";
        }
        if (ActivityManager.getService() != null && ActivityManager.getService().getRunningAppProcesses() != null) {
            for (ActivityManager.RunningAppProcessInfo proc : ActivityManager.getService().getRunningAppProcesses()) {
                if (proc.pid == pid) {
                    String packageName = "";
                    String[] names = proc.pkgList;
                    if (names != null && names.length != 0) {
                        packageName = names[0];
                    }
                    if (!packageName.isEmpty()) {
                        if (packageName.startsWith("com.xiaopeng.")) {
                            return packageName;
                        }
                    }
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    public static Looper myLooper() {
        return sThreadLocal.get();
    }

    public static MessageQueue myQueue() {
        return myLooper().mQueue;
    }

    private synchronized Looper(boolean quitAllowed) {
        this.mQueue = new MessageQueue(quitAllowed);
    }

    public boolean isCurrentThread() {
        return Thread.currentThread() == this.mThread;
    }

    public void setMessageLogging(Printer printer) {
        this.mLogging = printer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTraceTag(long traceTag) {
        this.mTraceTag = traceTag;
    }

    public synchronized void setSlowLogThresholdMs(long slowDispatchThresholdMs, long slowDeliveryThresholdMs) {
        this.mSlowDispatchThresholdMs = slowDispatchThresholdMs;
        this.mSlowDeliveryThresholdMs = slowDeliveryThresholdMs;
    }

    public void quit() {
        this.mQueue.quit(false);
    }

    public void quitSafely() {
        this.mQueue.quit(true);
    }

    public Thread getThread() {
        return this.mThread;
    }

    public MessageQueue getQueue() {
        return this.mQueue;
    }

    public void dump(Printer pw, String prefix) {
        pw.println(prefix + toString());
        MessageQueue messageQueue = this.mQueue;
        messageQueue.dump(pw, prefix + "  ", null);
    }

    public synchronized void dump(Printer pw, String prefix, Handler handler) {
        pw.println(prefix + toString());
        MessageQueue messageQueue = this.mQueue;
        messageQueue.dump(pw, prefix + "  ", handler);
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
        long looperToken = proto.start(fieldId);
        proto.write(1138166333441L, this.mThread.getName());
        proto.write(1112396529666L, this.mThread.getId());
        this.mQueue.writeToProto(proto, 1146756268035L);
        proto.end(looperToken);
    }

    public String toString() {
        return "Looper (" + this.mThread.getName() + ", tid " + this.mThread.getId() + ") {" + Integer.toHexString(System.identityHashCode(this)) + "}";
    }
}
