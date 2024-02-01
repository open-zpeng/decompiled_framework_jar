package android.os;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.TraceManager;
import android.app.XpConfigManager;
import android.os.caton.BugHunter;
import android.telecom.Logging.Session;
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
    @UnsupportedAppUsage
    private static Looper sMainLooper;
    private static Observer sObserver;
    @UnsupportedAppUsage
    private Printer mLogging;
    @UnsupportedAppUsage
    final MessageQueue mQueue;
    private long mSlowDeliveryThresholdMs;
    private long mSlowDispatchThresholdMs;
    final Thread mThread = Thread.currentThread();
    private long mTraceTag;
    @UnsupportedAppUsage
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<>();
    private static volatile String packageName = null;
    private static volatile String whiteAppList = null;
    private static long sStartTime = System.currentTimeMillis();
    private static final long CATCHCATON = SystemProperties.getLong("persist.sys.debug.caton", -1);

    /* loaded from: classes2.dex */
    public interface Observer {
        void dispatchingThrewException(Object obj, Message message, Exception exc);

        Object messageDispatchStarting();

        void messageDispatched(Object obj, Message message);
    }

    public static void prepare() {
        prepare(true);
    }

    private static void prepare(boolean quitAllowed) {
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

    public static void setObserver(Observer observer) {
        sObserver = observer;
    }

    /* JADX WARN: Removed duplicated region for block: B:110:0x02c1  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x02cb  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x02b9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x011c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x025c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00f4  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x014c A[Catch: all -> 0x0120, Exception -> 0x0135, TRY_LEAVE, TryCatch #7 {Exception -> 0x0135, all -> 0x0120, blocks: (B:59:0x011c, B:65:0x014c), top: B:119:0x011c }] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0151  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x015f  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0169  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0170  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01c2  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0214  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void loop() {
        /*
            Method dump skipped, instructions count: 729
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Looper.loop():void");
    }

    private static boolean showSlowLog(boolean needTrace, long threshold, long measureStart, long measureEnd, String what, Message msg, long threadTime) {
        long actualTime = measureEnd - measureStart;
        if (actualTime < threshold) {
            return false;
        }
        Slog.w(TAG, "Slow " + what + " took " + actualTime + "ms " + Thread.currentThread().getName() + " h=" + msg.target.getClass().getName() + " c=" + msg.callback + " m=" + msg.what);
        if (needTrace) {
            BugHunter.notifyCaton(actualTime, threadTime, packageName);
        }
        if (CATCHCATON < 0) {
            return true;
        }
        if (myLooper() == getMainLooper() || "system_server".equals(packageName)) {
            final long end = System.currentTimeMillis();
            if (end - sStartTime < DEFAULT_COLLECT_INTERVAL) {
                return true;
            }
            sStartTime = end;
            BackgroundThread.getHandler().postDelayed(new Runnable() { // from class: android.os.-$$Lambda$Looper$j6JdufBj0TcAZ-8immqhoTdymUg
                @Override // java.lang.Runnable
                public final void run() {
                    Looper.lambda$showSlowLog$0(end);
                }
            }, DEFAULT_COLLECT_DELAY);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$showSlowLog$0(long end) {
        if (packageName != null) {
            try {
                int myPid = Process.myPid();
                TraceManager.getService().catchCaton(packageName.replace("com.xiaopeng.", "") + Session.SESSION_SEPARATION_CHAR_CHILD + myPid + Session.SESSION_SEPARATION_CHAR_CHILD + end, packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isNeedCatchCaton() {
        try {
            if (TextUtils.isEmpty(packageName)) {
                int myPid = Process.myPid();
                packageName = getPackageNamesFromPid(myPid);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        if (TextUtils.isEmpty(whiteAppList) && XpConfigManager.getService() != null) {
            whiteAppList = XpConfigManager.getService().getAppList();
        }
        if (!TextUtils.isEmpty(whiteAppList)) {
            if (whiteAppList.contains(packageName)) {
                return true;
            }
        }
        return false;
    }

    private static String getPackageNamesFromPid(int pid) {
        try {
        } catch (Exception e) {
            Log.d(TAG, "ActivityManager.getRunningAppProcesses() failed");
        }
        if (pid == SystemProperties.getInt("sys.system_server.pid", 0)) {
            return "system_server";
        }
        if (ActivityManager.getService() != null && ActivityManager.getService().getRunningAppProcesses() != null) {
            for (ActivityManager.RunningAppProcessInfo info : ActivityManager.getService().getRunningAppProcesses()) {
                if (info != null && info.pid == pid && info.pkgList != null && info.pkgList.length > 0) {
                    return info.pkgList[0];
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

    private Looper(boolean quitAllowed) {
        this.mQueue = new MessageQueue(quitAllowed);
    }

    public boolean isCurrentThread() {
        return Thread.currentThread() == this.mThread;
    }

    public void setMessageLogging(Printer printer) {
        this.mLogging = printer;
    }

    @UnsupportedAppUsage
    public void setTraceTag(long traceTag) {
        this.mTraceTag = traceTag;
    }

    public void setSlowLogThresholdMs(long slowDispatchThresholdMs, long slowDeliveryThresholdMs) {
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

    public void dump(Printer pw, String prefix, Handler handler) {
        pw.println(prefix + toString());
        MessageQueue messageQueue = this.mQueue;
        messageQueue.dump(pw, prefix + "  ", handler);
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long looperToken = proto.start(fieldId);
        proto.write(1138166333441L, this.mThread.getName());
        proto.write(1112396529666L, this.mThread.getId());
        MessageQueue messageQueue = this.mQueue;
        if (messageQueue != null) {
            messageQueue.writeToProto(proto, 1146756268035L);
        }
        proto.end(looperToken);
    }

    public String toString() {
        return "Looper (" + this.mThread.getName() + ", tid " + this.mThread.getId() + ") {" + Integer.toHexString(System.identityHashCode(this)) + "}";
    }
}
