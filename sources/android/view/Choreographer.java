package android.view;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.app.TraceManager;
import android.content.Context;
import android.graphics.FrameInfo;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.telecom.Logging.Session;
import android.util.Log;
import android.util.Slog;
import android.util.TimeUtils;
import android.view.animation.AnimationUtils;
import com.android.internal.os.BackgroundThread;
import com.xiaopeng.app.xpActivityManager;
import java.io.PrintWriter;

/* loaded from: classes3.dex */
public final class Choreographer {
    public static final int CALLBACK_ANIMATION = 1;
    public static final int CALLBACK_COMMIT = 4;
    public static final int CALLBACK_INPUT = 0;
    public static final int CALLBACK_INSETS_ANIMATION = 2;
    private static final int CALLBACK_LAST = 4;
    public static final int CALLBACK_TRAVERSAL = 3;
    private static final boolean DEBUG_FRAMES = false;
    private static final boolean DEBUG_JANK = false;
    private static final long DEFAULT_COLLECT_DELAY = 2000;
    private static final int MSG_DO_FRAME = 0;
    private static final int MSG_DO_SCHEDULE_CALLBACK = 2;
    private static final int MSG_DO_SCHEDULE_VSYNC = 1;
    private static final String TAG = "Choreographer";
    private static volatile Choreographer mMainInstance;
    private CallbackRecord mCallbackPool;
    @UnsupportedAppUsage
    private final CallbackQueue[] mCallbackQueues;
    private boolean mCallbacksRunning;
    private boolean mDebugPrintNextFrameTimeDelta;
    @UnsupportedAppUsage
    private final FrameDisplayEventReceiver mDisplayEventReceiver;
    private int mFPSDivisor;
    FrameInfo mFrameInfo;
    @UnsupportedAppUsage
    private long mFrameIntervalNanos;
    private boolean mFrameScheduled;
    private final FrameHandler mHandler;
    @UnsupportedAppUsage
    private long mLastFrameTimeNanos;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final Object mLock;
    private final Looper mLooper;
    private static final long DEFAULT_FRAME_DELAY = 10;
    private static volatile long sFrameDelay = DEFAULT_FRAME_DELAY;
    private static int skipped_frame_count_1 = 0;
    private static int skipped_frame_count_2 = 0;
    private static int skipped_frame_count_3 = 0;
    private static int skipped_frame_count_4 = 0;
    private static int skipped_frame_count_5 = 0;
    private static int skipped_frame_count_6 = 0;
    private static int skipped_frame_count_7 = 0;
    private static long mLastFrameEndTime = 0;
    private static final ThreadLocal<Choreographer> sThreadInstance = new ThreadLocal<Choreographer>() { // from class: android.view.Choreographer.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Choreographer initialValue() {
            Looper looper = Looper.myLooper();
            if (looper == null) {
                throw new IllegalStateException("The current thread must have a looper!");
            }
            Choreographer choreographer = new Choreographer(looper, 0);
            if (looper == Looper.getMainLooper()) {
                Choreographer unused = Choreographer.mMainInstance = choreographer;
            }
            return choreographer;
        }
    };
    private static final ThreadLocal<Choreographer> sSfThreadInstance = new ThreadLocal<Choreographer>() { // from class: android.view.Choreographer.2
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Choreographer initialValue() {
            Looper looper = Looper.myLooper();
            if (looper == null) {
                throw new IllegalStateException("The current thread must have a looper!");
            }
            return new Choreographer(looper, 1);
        }
    };
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769497)
    private static final boolean USE_VSYNC = SystemProperties.getBoolean("debug.choreographer.vsync", true);
    private static final boolean USE_FRAME_TIME = SystemProperties.getBoolean("debug.choreographer.frametime", true);
    private static final boolean USE_FRAME_SKIPPED_DEBUG = SystemProperties.getBoolean("persist.sys.xperf.framedebug", false);
    private static final int FRAME_SKIPPED_LV1 = SystemProperties.getInt("persist.sys.xperf.framelv1", 0);
    private static final int FRAME_SKIPPED_LV2 = SystemProperties.getInt("persist.sys.xperf.framelv2", 0);
    private static final int FRAME_SKIPPED_LV3 = SystemProperties.getInt("persist.sys.xperf.framelv3", 0);
    private static final int FRAME_SKIPPED_LV4 = SystemProperties.getInt("persist.sys.xperf.framelv4", 0);
    private static final int FRAME_SKIPPED_LV5 = SystemProperties.getInt("persist.sys.xperf.framelv5", 0);
    private static final int FRAME_SKIPPED_LV6 = SystemProperties.getInt("persist.sys.xperf.framelv6", 0);
    private static final int CATCHCATON = SystemProperties.getInt("persist.sys.xperf.caton", 0);
    private static final int SKIPPED_FRAME_WARNING_LIMIT = SystemProperties.getInt("debug.choreographer.skipwarning", 30);
    private static final Object FRAME_CALLBACK_TOKEN = new Object() { // from class: android.view.Choreographer.3
        public String toString() {
            return "FRAME_CALLBACK_TOKEN";
        }
    };
    private static final String[] CALLBACK_TRACE_TITLES = {"input", "animation", "insets_animation", "traversal", "commit"};

    /* loaded from: classes3.dex */
    public interface FrameCallback {
        void doFrame(long j);
    }

    private Choreographer(Looper looper, int vsyncSource) {
        FrameDisplayEventReceiver frameDisplayEventReceiver;
        this.mLock = new Object();
        this.mFPSDivisor = 1;
        this.mFrameInfo = new FrameInfo();
        this.mLooper = looper;
        this.mHandler = new FrameHandler(looper);
        if (USE_VSYNC) {
            frameDisplayEventReceiver = new FrameDisplayEventReceiver(looper, vsyncSource);
        } else {
            frameDisplayEventReceiver = null;
        }
        this.mDisplayEventReceiver = frameDisplayEventReceiver;
        this.mLastFrameTimeNanos = Long.MIN_VALUE;
        this.mFrameIntervalNanos = 1.0E9f / getRefreshRate();
        this.mCallbackQueues = new CallbackQueue[5];
        for (int i = 0; i <= 4; i++) {
            this.mCallbackQueues[i] = new CallbackQueue();
        }
        setFPSDivisor(SystemProperties.getInt(ThreadedRenderer.DEBUG_FPS_DIVISOR, 1));
    }

    private static float getRefreshRate() {
        DisplayInfo di = DisplayManagerGlobal.getInstance().getDisplayInfo(0);
        return di.getMode().getRefreshRate();
    }

    public static Choreographer getInstance() {
        return sThreadInstance.get();
    }

    @UnsupportedAppUsage
    public static Choreographer getSfInstance() {
        return sSfThreadInstance.get();
    }

    public static Choreographer getMainThreadInstance() {
        return mMainInstance;
    }

    public static void releaseInstance() {
        Choreographer old = sThreadInstance.get();
        sThreadInstance.remove();
        old.dispose();
    }

    private void dispose() {
        this.mDisplayEventReceiver.dispose();
    }

    public static long getFrameDelay() {
        return sFrameDelay;
    }

    public static void setFrameDelay(long frameDelay) {
        sFrameDelay = frameDelay;
    }

    public static long subtractFrameDelay(long delayMillis) {
        long frameDelay = sFrameDelay;
        if (delayMillis <= frameDelay) {
            return 0L;
        }
        return delayMillis - frameDelay;
    }

    public long getFrameIntervalNanos() {
        return this.mFrameIntervalNanos;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(String prefix, PrintWriter writer) {
        String innerPrefix = prefix + "  ";
        writer.print(prefix);
        writer.println("Choreographer:");
        writer.print(innerPrefix);
        writer.print("mFrameScheduled=");
        writer.println(this.mFrameScheduled);
        writer.print(innerPrefix);
        writer.print("mLastFrameTime=");
        writer.println(TimeUtils.formatUptime(this.mLastFrameTimeNanos / TimeUtils.NANOS_PER_MS));
    }

    public void postCallback(int callbackType, Runnable action, Object token) {
        postCallbackDelayed(callbackType, action, token, 0L);
    }

    public void postCallbackDelayed(int callbackType, Runnable action, Object token, long delayMillis) {
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        }
        if (callbackType < 0 || callbackType > 4) {
            throw new IllegalArgumentException("callbackType is invalid");
        }
        postCallbackDelayedInternal(callbackType, action, token, delayMillis);
    }

    private void postCallbackDelayedInternal(int callbackType, Object action, Object token, long delayMillis) {
        synchronized (this.mLock) {
            long now = SystemClock.uptimeMillis();
            long dueTime = now + delayMillis;
            this.mCallbackQueues[callbackType].addCallbackLocked(dueTime, action, token);
            if (dueTime <= now) {
                scheduleFrameLocked(now);
            } else {
                Message msg = this.mHandler.obtainMessage(2, action);
                msg.arg1 = callbackType;
                msg.setAsynchronous(true);
                this.mHandler.sendMessageAtTime(msg, dueTime);
            }
        }
    }

    public void removeCallbacks(int callbackType, Runnable action, Object token) {
        if (callbackType < 0 || callbackType > 4) {
            throw new IllegalArgumentException("callbackType is invalid");
        }
        removeCallbacksInternal(callbackType, action, token);
    }

    private void removeCallbacksInternal(int callbackType, Object action, Object token) {
        synchronized (this.mLock) {
            this.mCallbackQueues[callbackType].removeCallbacksLocked(action, token);
            if (action != null && token == null) {
                this.mHandler.removeMessages(2, action);
            }
        }
    }

    public void postFrameCallback(FrameCallback callback) {
        postFrameCallbackDelayed(callback, 0L);
    }

    public void postFrameCallbackDelayed(FrameCallback callback, long delayMillis) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        postCallbackDelayedInternal(1, callback, FRAME_CALLBACK_TOKEN, delayMillis);
    }

    public void removeFrameCallback(FrameCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        removeCallbacksInternal(1, callback, FRAME_CALLBACK_TOKEN);
    }

    @UnsupportedAppUsage
    public long getFrameTime() {
        return getFrameTimeNanos() / TimeUtils.NANOS_PER_MS;
    }

    @UnsupportedAppUsage
    public long getFrameTimeNanos() {
        long nanoTime;
        synchronized (this.mLock) {
            if (!this.mCallbacksRunning) {
                throw new IllegalStateException("This method must only be called as part of a callback while a frame is in progress.");
            }
            nanoTime = USE_FRAME_TIME ? this.mLastFrameTimeNanos : System.nanoTime();
        }
        return nanoTime;
    }

    public long getLastFrameTimeNanos() {
        long nanoTime;
        synchronized (this.mLock) {
            nanoTime = USE_FRAME_TIME ? this.mLastFrameTimeNanos : System.nanoTime();
        }
        return nanoTime;
    }

    private void scheduleFrameLocked(long now) {
        if (!this.mFrameScheduled) {
            this.mFrameScheduled = true;
            if (USE_VSYNC) {
                if (!isRunningOnLooperThreadLocked()) {
                    Message msg = this.mHandler.obtainMessage(1);
                    msg.setAsynchronous(true);
                    this.mHandler.sendMessageAtFrontOfQueue(msg);
                    return;
                }
                scheduleVsyncLocked();
                return;
            }
            long nextFrameTime = Math.max((this.mLastFrameTimeNanos / TimeUtils.NANOS_PER_MS) + sFrameDelay, now);
            Message msg2 = this.mHandler.obtainMessage(0);
            msg2.setAsynchronous(true);
            this.mHandler.sendMessageAtTime(msg2, nextFrameTime);
        }
    }

    void setFPSDivisor(int divisor) {
        if (divisor <= 0) {
            divisor = 1;
        }
        this.mFPSDivisor = divisor;
        ThreadedRenderer.setFPSDivisor(divisor);
    }

    private void skippedFramePrintf(long skippedFrames) {
        Context fallbackContext = ActivityThread.currentApplication();
        if (fallbackContext == null) {
            return;
        }
        int myPid = Process.myPid();
        String process_Name = xpActivityManager.getProcessName(fallbackContext);
        if (skippedFrames <= FRAME_SKIPPED_LV1) {
            skipped_frame_count_1++;
        } else if (skippedFrames <= FRAME_SKIPPED_LV2) {
            skipped_frame_count_2++;
        } else if (skippedFrames <= FRAME_SKIPPED_LV3) {
            skipped_frame_count_3++;
        } else if (skippedFrames <= FRAME_SKIPPED_LV4) {
            skipped_frame_count_4++;
        } else if (skippedFrames <= FRAME_SKIPPED_LV5) {
            skipped_frame_count_5++;
        } else if (skippedFrames <= FRAME_SKIPPED_LV6) {
            skipped_frame_count_6++;
        } else {
            skipped_frame_count_7++;
        }
        Slog.e(TAG, "skipped frame info:  pid: " + myPid + ";  p_name: " + process_Name + " -- LV1: " + skipped_frame_count_1 + "; LV2: " + skipped_frame_count_2 + "; LV3: " + skipped_frame_count_3 + "; LV4: " + skipped_frame_count_4 + "; LV5: " + skipped_frame_count_5 + "; LV6: " + skipped_frame_count_6 + "; >LV6: " + skipped_frame_count_7 + "; dropped frames: " + skippedFrames + ";");
    }

    private void catchCatonTrace(final long skippedFrames) {
        BackgroundThread.getHandler().postDelayed(new Runnable() { // from class: android.view.-$$Lambda$Choreographer$BB51ggQJRUae6FJNiBYkFPadC7w
            @Override // java.lang.Runnable
            public final void run() {
                Choreographer.lambda$catchCatonTrace$0(skippedFrames);
            }
        }, DEFAULT_COLLECT_DELAY);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$catchCatonTrace$0(long skippedFrames) {
        Context fallbackContext = ActivityThread.currentApplication();
        if (fallbackContext == null) {
            Slog.e(TAG, "catchCatonTrace,fail get fallbackContext:");
            return;
        }
        Process.myPid();
        String process_Name = xpActivityManager.getProcessName(fallbackContext);
        Slog.e(TAG, "catchCatonTrace, skippedFrames:" + skippedFrames + ", CATCHCATON:" + CATCHCATON + ", process_Name:" + process_Name);
        if (skippedFrames > CATCHCATON && process_Name != null) {
            try {
                String fileName = process_Name + Session.SESSION_SEPARATION_CHAR_CHILD + System.currentTimeMillis();
                TraceManager.getService().catchCaton(fileName, process_Name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @UnsupportedAppUsage
    void doFrame(long frameTimeNanos, int frame) {
        long frameTimeNanos2;
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mFrameScheduled) {
                        long startNanos = System.nanoTime();
                        long jitterNanos = startNanos - frameTimeNanos;
                        if (jitterNanos < this.mFrameIntervalNanos) {
                            frameTimeNanos2 = frameTimeNanos;
                        } else {
                            long skippedFrames = jitterNanos / this.mFrameIntervalNanos;
                            if (skippedFrames >= SKIPPED_FRAME_WARNING_LIMIT) {
                                Log.i(TAG, "Skipped " + skippedFrames + " frames!  The application may be doing too much work on its main thread.");
                            }
                            long lastFrameOffset = jitterNanos % this.mFrameIntervalNanos;
                            if (USE_FRAME_SKIPPED_DEBUG && frameTimeNanos < mLastFrameEndTime) {
                                skippedFramePrintf(skippedFrames);
                            }
                            if (CATCHCATON != 0) {
                                catchCatonTrace(skippedFrames);
                            }
                            frameTimeNanos2 = startNanos - lastFrameOffset;
                        }
                        if (frameTimeNanos2 < this.mLastFrameTimeNanos) {
                            scheduleVsyncLocked();
                            return;
                        }
                        if (this.mFPSDivisor > 1) {
                            long timeSinceVsync = frameTimeNanos2 - this.mLastFrameTimeNanos;
                            if (timeSinceVsync < this.mFrameIntervalNanos * this.mFPSDivisor && timeSinceVsync > 0) {
                                scheduleVsyncLocked();
                                return;
                            }
                        }
                        this.mFrameInfo.setVsync(frameTimeNanos, frameTimeNanos2);
                        this.mFrameScheduled = false;
                        this.mLastFrameTimeNanos = frameTimeNanos2;
                        try {
                            Trace.traceBegin(8L, "Choreographer#doFrame");
                            AnimationUtils.lockAnimationClock(frameTimeNanos2 / TimeUtils.NANOS_PER_MS);
                            this.mFrameInfo.markInputHandlingStart();
                            doCallbacks(0, frameTimeNanos2);
                            this.mFrameInfo.markAnimationsStart();
                            doCallbacks(1, frameTimeNanos2);
                            doCallbacks(2, frameTimeNanos2);
                            this.mFrameInfo.markPerformTraversalsStart();
                            doCallbacks(3, frameTimeNanos2);
                            doCallbacks(4, frameTimeNanos2);
                            AnimationUtils.unlockAnimationClock();
                            Trace.traceEnd(8L);
                            if (USE_FRAME_SKIPPED_DEBUG) {
                                mLastFrameEndTime = System.nanoTime();
                            }
                        } catch (Throwable th) {
                            AnimationUtils.unlockAnimationClock();
                            Trace.traceEnd(8L);
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x004f A[Catch: all -> 0x006c, TRY_LEAVE, TryCatch #4 {all -> 0x006c, blocks: (B:18:0x0045, B:20:0x004f), top: B:58:0x0045 }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0059 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void doCallbacks(int r16, long r17) {
        /*
            r15 = this;
            r1 = r15
            r2 = r16
            java.lang.Object r3 = r1.mLock
            monitor-enter(r3)
            long r4 = java.lang.System.nanoTime()     // Catch: java.lang.Throwable -> L83
            android.view.Choreographer$CallbackQueue[] r0 = r1.mCallbackQueues     // Catch: java.lang.Throwable -> L83
            r0 = r0[r2]     // Catch: java.lang.Throwable -> L83
            r6 = 1000000(0xf4240, double:4.940656E-318)
            long r6 = r4 / r6
            android.view.Choreographer$CallbackRecord r0 = r0.extractDueCallbacksLocked(r6)     // Catch: java.lang.Throwable -> L83
            r6 = r0
            if (r6 != 0) goto L1c
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L83
            return
        L1c:
            r0 = 1
            r1.mCallbacksRunning = r0     // Catch: java.lang.Throwable -> L83
            r0 = 4
            r7 = 8
            if (r2 != r0) goto L41
            long r9 = r4 - r17
            java.lang.String r0 = "jitterNanos"
            int r11 = (int) r9     // Catch: java.lang.Throwable -> L83
            android.os.Trace.traceCounter(r7, r0, r11)     // Catch: java.lang.Throwable -> L83
            r11 = 2
            long r13 = r1.mFrameIntervalNanos     // Catch: java.lang.Throwable -> L83
            long r13 = r13 * r11
            int r0 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1))
            if (r0 < 0) goto L41
            long r11 = r1.mFrameIntervalNanos     // Catch: java.lang.Throwable -> L83
            long r11 = r9 % r11
            long r13 = r1.mFrameIntervalNanos     // Catch: java.lang.Throwable -> L83
            long r11 = r11 + r13
            long r13 = r4 - r11
            r1.mLastFrameTimeNanos = r13     // Catch: java.lang.Throwable -> L88
            goto L43
        L41:
            r13 = r17
        L43:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L88
            r3 = 0
            java.lang.String[] r0 = android.view.Choreographer.CALLBACK_TRACE_TITLES     // Catch: java.lang.Throwable -> L6c
            r0 = r0[r2]     // Catch: java.lang.Throwable -> L6c
            android.os.Trace.traceBegin(r7, r0)     // Catch: java.lang.Throwable -> L6c
            r0 = r6
        L4d:
            if (r0 == 0) goto L56
            r0.run(r13)     // Catch: java.lang.Throwable -> L6c
            android.view.Choreographer$CallbackRecord r4 = r0.next     // Catch: java.lang.Throwable -> L6c
            r0 = r4
            goto L4d
        L56:
            java.lang.Object r4 = r1.mLock
            monitor-enter(r4)
            r1.mCallbacksRunning = r3     // Catch: java.lang.Throwable -> L69
        L5b:
            android.view.Choreographer$CallbackRecord r0 = r6.next     // Catch: java.lang.Throwable -> L69
            r15.recycleCallbackLocked(r6)     // Catch: java.lang.Throwable -> L69
            r6 = r0
            if (r6 != 0) goto L5b
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L69
            android.os.Trace.traceEnd(r7)
            return
        L69:
            r0 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L69
            throw r0
        L6c:
            r0 = move-exception
            java.lang.Object r4 = r1.mLock
            monitor-enter(r4)
            r1.mCallbacksRunning = r3     // Catch: java.lang.Throwable -> L80
        L72:
            android.view.Choreographer$CallbackRecord r3 = r6.next     // Catch: java.lang.Throwable -> L80
            r15.recycleCallbackLocked(r6)     // Catch: java.lang.Throwable -> L80
            r6 = r3
            if (r6 == 0) goto L7b
            goto L72
        L7b:
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L80
            android.os.Trace.traceEnd(r7)
            throw r0
        L80:
            r0 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L80
            throw r0
        L83:
            r0 = move-exception
            r13 = r17
        L86:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L88
            throw r0
        L88:
            r0 = move-exception
            goto L86
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.Choreographer.doCallbacks(int, long):void");
    }

    void doScheduleVsync() {
        synchronized (this.mLock) {
            if (this.mFrameScheduled) {
                scheduleVsyncLocked();
            }
        }
    }

    void doScheduleCallback(int callbackType) {
        synchronized (this.mLock) {
            if (!this.mFrameScheduled) {
                long now = SystemClock.uptimeMillis();
                if (this.mCallbackQueues[callbackType].hasDueCallbacksLocked(now)) {
                    scheduleFrameLocked(now);
                }
            }
        }
    }

    @UnsupportedAppUsage
    private void scheduleVsyncLocked() {
        this.mDisplayEventReceiver.scheduleVsync();
    }

    private boolean isRunningOnLooperThreadLocked() {
        return Looper.myLooper() == this.mLooper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CallbackRecord obtainCallbackLocked(long dueTime, Object action, Object token) {
        CallbackRecord callback = this.mCallbackPool;
        if (callback == null) {
            callback = new CallbackRecord();
        } else {
            this.mCallbackPool = callback.next;
            callback.next = null;
        }
        callback.dueTime = dueTime;
        callback.action = action;
        callback.token = token;
        return callback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recycleCallbackLocked(CallbackRecord callback) {
        callback.action = null;
        callback.token = null;
        callback.next = this.mCallbackPool;
        this.mCallbackPool = callback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public final class FrameHandler extends Handler {
        public FrameHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 0) {
                Choreographer.this.doFrame(System.nanoTime(), 0);
            } else if (i == 1) {
                Choreographer.this.doScheduleVsync();
            } else if (i == 2) {
                Choreographer.this.doScheduleCallback(msg.arg1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public final class FrameDisplayEventReceiver extends DisplayEventReceiver implements Runnable {
        private int mFrame;
        private boolean mHavePendingVsync;
        private long mTimestampNanos;

        public FrameDisplayEventReceiver(Looper looper, int vsyncSource) {
            super(looper, vsyncSource, 0);
        }

        @Override // android.view.DisplayEventReceiver
        public void onVsync(long timestampNanos, long physicalDisplayId, int frame) {
            long now = System.nanoTime();
            if (timestampNanos > now) {
                Log.w(Choreographer.TAG, "Frame time is " + (((float) (timestampNanos - now)) * 1.0E-6f) + " ms in the future!  Check that graphics HAL is generating vsync timestamps using the correct timebase.");
                timestampNanos = now;
            }
            if (this.mHavePendingVsync) {
                Log.w(Choreographer.TAG, "Already have a pending vsync event.  There should only be one at a time.");
            } else {
                this.mHavePendingVsync = true;
            }
            this.mTimestampNanos = timestampNanos;
            this.mFrame = frame;
            Message msg = Message.obtain(Choreographer.this.mHandler, this);
            msg.setAsynchronous(true);
            Choreographer.this.mHandler.sendMessageAtTime(msg, timestampNanos / TimeUtils.NANOS_PER_MS);
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mHavePendingVsync = false;
            Choreographer.this.doFrame(this.mTimestampNanos, this.mFrame);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class CallbackRecord {
        public Object action;
        public long dueTime;
        public CallbackRecord next;
        public Object token;

        private CallbackRecord() {
        }

        @UnsupportedAppUsage
        public void run(long frameTimeNanos) {
            if (this.token == Choreographer.FRAME_CALLBACK_TOKEN) {
                ((FrameCallback) this.action).doFrame(frameTimeNanos);
            } else {
                ((Runnable) this.action).run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public final class CallbackQueue {
        private CallbackRecord mHead;

        private CallbackQueue() {
        }

        public boolean hasDueCallbacksLocked(long now) {
            CallbackRecord callbackRecord = this.mHead;
            return callbackRecord != null && callbackRecord.dueTime <= now;
        }

        public CallbackRecord extractDueCallbacksLocked(long now) {
            CallbackRecord callbacks = this.mHead;
            if (callbacks == null || callbacks.dueTime > now) {
                return null;
            }
            CallbackRecord last = callbacks;
            CallbackRecord next = last.next;
            while (true) {
                if (next == null) {
                    break;
                } else if (next.dueTime > now) {
                    last.next = null;
                    break;
                } else {
                    last = next;
                    next = next.next;
                }
            }
            this.mHead = next;
            return callbacks;
        }

        @UnsupportedAppUsage
        public void addCallbackLocked(long dueTime, Object action, Object token) {
            CallbackRecord callback = Choreographer.this.obtainCallbackLocked(dueTime, action, token);
            CallbackRecord entry = this.mHead;
            if (entry == null) {
                this.mHead = callback;
            } else if (dueTime < entry.dueTime) {
                callback.next = entry;
                this.mHead = callback;
            } else {
                while (true) {
                    if (entry.next == null) {
                        break;
                    } else if (dueTime < entry.next.dueTime) {
                        callback.next = entry.next;
                        break;
                    } else {
                        entry = entry.next;
                    }
                }
                entry.next = callback;
            }
        }

        public void removeCallbacksLocked(Object action, Object token) {
            CallbackRecord predecessor = null;
            CallbackRecord callback = this.mHead;
            while (callback != null) {
                CallbackRecord next = callback.next;
                if ((action == null || callback.action == action) && (token == null || callback.token == token)) {
                    if (predecessor != null) {
                        predecessor.next = next;
                    } else {
                        this.mHead = next;
                    }
                    Choreographer.this.recycleCallbackLocked(callback);
                } else {
                    predecessor = callback;
                }
                callback = next;
            }
        }
    }
}
