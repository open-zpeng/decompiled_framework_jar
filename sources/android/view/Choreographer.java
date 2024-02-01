package android.view;

import android.hardware.display.DisplayManagerGlobal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.TimeUtils;
import android.view.animation.AnimationUtils;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public final class Choreographer {
    public static final int CALLBACK_ANIMATION = 1;
    public static final int CALLBACK_COMMIT = 3;
    public static final int CALLBACK_INPUT = 0;
    private static final int CALLBACK_LAST = 3;
    public static final int CALLBACK_TRAVERSAL = 2;
    private static final boolean DEBUG_FRAMES = false;
    private static final boolean DEBUG_JANK = false;
    private static final int MSG_DO_FRAME = 0;
    private static final int MSG_DO_SCHEDULE_CALLBACK = 2;
    private static final int MSG_DO_SCHEDULE_VSYNC = 1;
    private static final String TAG = "Choreographer";
    private static volatile Choreographer mMainInstance;
    private CallbackRecord mCallbackPool;
    public protected final CallbackQueue[] mCallbackQueues;
    private boolean mCallbacksRunning;
    private boolean mDebugPrintNextFrameTimeDelta;
    public protected final FrameDisplayEventReceiver mDisplayEventReceiver;
    private int mFPSDivisor;
    FrameInfo mFrameInfo;
    public protected long mFrameIntervalNanos;
    private boolean mFrameScheduled;
    private final FrameHandler mHandler;
    public protected long mLastFrameTimeNanos;
    public protected final Object mLock;
    private final Looper mLooper;
    private static final long DEFAULT_FRAME_DELAY = 10;
    private static volatile long sFrameDelay = DEFAULT_FRAME_DELAY;
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
    public protected static final boolean USE_VSYNC = SystemProperties.getBoolean("debug.choreographer.vsync", true);
    private static final boolean USE_FRAME_TIME = SystemProperties.getBoolean("debug.choreographer.frametime", true);
    private static final int SKIPPED_FRAME_WARNING_LIMIT = SystemProperties.getInt("debug.choreographer.skipwarning", 30);
    private static final Object FRAME_CALLBACK_TOKEN = new Object() { // from class: android.view.Choreographer.3
        public String toString() {
            return "FRAME_CALLBACK_TOKEN";
        }
    };
    private static final String[] CALLBACK_TRACE_TITLES = {"input", "animation", "traversal", "commit"};

    /* loaded from: classes2.dex */
    public interface FrameCallback {
        void doFrame(long j);
    }

    private synchronized Choreographer(Looper looper, int vsyncSource) {
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
        this.mCallbackQueues = new CallbackQueue[4];
        for (int i = 0; i <= 3; i++) {
            this.mCallbackQueues[i] = new CallbackQueue();
        }
        setFPSDivisor(SystemProperties.getInt(ThreadedRenderer.DEBUG_FPS_DIVISOR, 1));
    }

    private static synchronized float getRefreshRate() {
        DisplayInfo di = DisplayManagerGlobal.getInstance().getDisplayInfo(0);
        return di.getMode().getRefreshRate();
    }

    public static Choreographer getInstance() {
        return sThreadInstance.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Choreographer getSfInstance() {
        return sSfThreadInstance.get();
    }

    public static synchronized Choreographer getMainThreadInstance() {
        return mMainInstance;
    }

    public static synchronized void releaseInstance() {
        Choreographer old = sThreadInstance.get();
        sThreadInstance.remove();
        old.dispose();
    }

    private synchronized void dispose() {
        this.mDisplayEventReceiver.dispose();
    }

    public static long getFrameDelay() {
        return sFrameDelay;
    }

    public static void setFrameDelay(long frameDelay) {
        sFrameDelay = frameDelay;
    }

    public static synchronized long subtractFrameDelay(long delayMillis) {
        long frameDelay = sFrameDelay;
        if (delayMillis <= frameDelay) {
            return 0L;
        }
        return delayMillis - frameDelay;
    }

    public synchronized long getFrameIntervalNanos() {
        return this.mFrameIntervalNanos;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dump(String prefix, PrintWriter writer) {
        String innerPrefix = prefix + "  ";
        writer.print(prefix);
        writer.println("Choreographer:");
        writer.print(innerPrefix);
        writer.print("mFrameScheduled=");
        writer.println(this.mFrameScheduled);
        writer.print(innerPrefix);
        writer.print("mLastFrameTime=");
        writer.println(TimeUtils.formatUptime(this.mLastFrameTimeNanos / 1000000));
    }

    public void postCallback(int callbackType, Runnable action, Object token) {
        postCallbackDelayed(callbackType, action, token, 0L);
    }

    public void postCallbackDelayed(int callbackType, Runnable action, Object token, long delayMillis) {
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        }
        if (callbackType < 0 || callbackType > 3) {
            throw new IllegalArgumentException("callbackType is invalid");
        }
        postCallbackDelayedInternal(callbackType, action, token, delayMillis);
    }

    private synchronized void postCallbackDelayedInternal(int callbackType, Object action, Object token, long delayMillis) {
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
        if (callbackType < 0 || callbackType > 3) {
            throw new IllegalArgumentException("callbackType is invalid");
        }
        removeCallbacksInternal(callbackType, action, token);
    }

    private synchronized void removeCallbacksInternal(int callbackType, Object action, Object token) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public long getFrameTime() {
        return getFrameTimeNanos() / 1000000;
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    public synchronized long getLastFrameTimeNanos() {
        long nanoTime;
        synchronized (this.mLock) {
            nanoTime = USE_FRAME_TIME ? this.mLastFrameTimeNanos : System.nanoTime();
        }
        return nanoTime;
    }

    private synchronized void scheduleFrameLocked(long now) {
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
            long nextFrameTime = Math.max((this.mLastFrameTimeNanos / 1000000) + sFrameDelay, now);
            Message msg2 = this.mHandler.obtainMessage(0);
            msg2.setAsynchronous(true);
            this.mHandler.sendMessageAtTime(msg2, nextFrameTime);
        }
    }

    synchronized void setFPSDivisor(int divisor) {
        if (divisor <= 0) {
            divisor = 1;
        }
        this.mFPSDivisor = divisor;
        ThreadedRenderer.setFPSDivisor(divisor);
    }

    public private protected void doFrame(long frameTimeNanos, int frame) {
        long frameTimeNanos2;
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mFrameScheduled) {
                        long startNanos = System.nanoTime();
                        long jitterNanos = startNanos - frameTimeNanos;
                        if (jitterNanos >= this.mFrameIntervalNanos) {
                            long skippedFrames = jitterNanos / this.mFrameIntervalNanos;
                            if (skippedFrames >= SKIPPED_FRAME_WARNING_LIMIT) {
                                Log.i(TAG, "Skipped " + skippedFrames + " frames!  The application may be doing too much work on its main thread.");
                            }
                            long lastFrameOffset = jitterNanos % this.mFrameIntervalNanos;
                            frameTimeNanos2 = startNanos - lastFrameOffset;
                        } else {
                            frameTimeNanos2 = frameTimeNanos;
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
                            AnimationUtils.lockAnimationClock(frameTimeNanos2 / 1000000);
                            this.mFrameInfo.markInputHandlingStart();
                            doCallbacks(0, frameTimeNanos2);
                            this.mFrameInfo.markAnimationsStart();
                            doCallbacks(1, frameTimeNanos2);
                            this.mFrameInfo.markPerformTraversalsStart();
                            doCallbacks(2, frameTimeNanos2);
                            doCallbacks(3, frameTimeNanos2);
                        } finally {
                            AnimationUtils.unlockAnimationClock();
                            Trace.traceEnd(8L);
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0052 A[Catch: all -> 0x0071, TRY_LEAVE, TryCatch #0 {all -> 0x0071, blocks: (B:18:0x0046, B:20:0x0052), top: B:54:0x0046 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x005c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    synchronized void doCallbacks(int r17, long r18) {
        /*
            r16 = this;
            r1 = r16
            r2 = r17
            java.lang.Object r3 = r1.mLock
            monitor-enter(r3)
            long r4 = java.lang.System.nanoTime()     // Catch: java.lang.Throwable -> L8d
            android.view.Choreographer$CallbackQueue[] r0 = r1.mCallbackQueues     // Catch: java.lang.Throwable -> L8d
            r0 = r0[r2]     // Catch: java.lang.Throwable -> L8d
            r6 = 1000000(0xf4240, double:4.940656E-318)
            long r6 = r4 / r6
            android.view.Choreographer$CallbackRecord r0 = r0.extractDueCallbacksLocked(r6)     // Catch: java.lang.Throwable -> L8d
            if (r0 != 0) goto L1c
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L8d
            return
        L1c:
            r6 = 1
            r1.mCallbacksRunning = r6     // Catch: java.lang.Throwable -> L8d
            r6 = 3
            r7 = 8
            if (r2 != r6) goto L41
            long r11 = r4 - r18
            java.lang.String r6 = "jitterNanos"
            int r13 = (int) r11     // Catch: java.lang.Throwable -> L8d
            android.os.Trace.traceCounter(r7, r6, r13)     // Catch: java.lang.Throwable -> L8d
            r13 = 2
            long r7 = r1.mFrameIntervalNanos     // Catch: java.lang.Throwable -> L8d
            long r13 = r13 * r7
            int r6 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r6 < 0) goto L41
            long r6 = r1.mFrameIntervalNanos     // Catch: java.lang.Throwable -> L8d
            long r6 = r11 % r6
            long r13 = r1.mFrameIntervalNanos     // Catch: java.lang.Throwable -> L8d
            long r6 = r6 + r13
            long r8 = r4 - r6
            r1.mLastFrameTimeNanos = r8     // Catch: java.lang.Throwable -> L8a
            goto L43
        L41:
            r8 = r18
        L43:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L8a
            r3 = r0
            r4 = 0
            java.lang.String[] r0 = android.view.Choreographer.CALLBACK_TRACE_TITLES     // Catch: java.lang.Throwable -> L71
            r0 = r0[r2]     // Catch: java.lang.Throwable -> L71
            r5 = 8
            android.os.Trace.traceBegin(r5, r0)     // Catch: java.lang.Throwable -> L71
            r0 = r3
        L50:
            if (r0 == 0) goto L59
            r0.run(r8)     // Catch: java.lang.Throwable -> L71
            android.view.Choreographer$CallbackRecord r5 = r0.next     // Catch: java.lang.Throwable -> L71
            r0 = r5
            goto L50
        L59:
            java.lang.Object r5 = r1.mLock
            monitor-enter(r5)
            r1.mCallbacksRunning = r4     // Catch: java.lang.Throwable -> L6e
        L5e:
            android.view.Choreographer$CallbackRecord r0 = r3.next     // Catch: java.lang.Throwable -> L6e
            r1.recycleCallbackLocked(r3)     // Catch: java.lang.Throwable -> L6e
            r3 = r0
            if (r3 != 0) goto L5e
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L6e
            r4 = 8
            android.os.Trace.traceEnd(r4)
            return
        L6e:
            r0 = move-exception
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L6e
            throw r0
        L71:
            r0 = move-exception
            java.lang.Object r5 = r1.mLock
            monitor-enter(r5)
            r1.mCallbacksRunning = r4     // Catch: java.lang.Throwable -> L87
        L77:
            android.view.Choreographer$CallbackRecord r4 = r3.next     // Catch: java.lang.Throwable -> L87
            r1.recycleCallbackLocked(r3)     // Catch: java.lang.Throwable -> L87
            r3 = r4
            if (r3 == 0) goto L80
            goto L77
        L80:
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L87
            r4 = 8
            android.os.Trace.traceEnd(r4)
            throw r0
        L87:
            r0 = move-exception
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L87
            throw r0
        L8a:
            r0 = move-exception
            r9 = r8
            goto L90
        L8d:
            r0 = move-exception
            r9 = r18
        L90:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L92
            throw r0
        L92:
            r0 = move-exception
            goto L90
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.Choreographer.doCallbacks(int, long):void");
    }

    synchronized void doScheduleVsync() {
        synchronized (this.mLock) {
            if (this.mFrameScheduled) {
                scheduleVsyncLocked();
            }
        }
    }

    synchronized void doScheduleCallback(int callbackType) {
        synchronized (this.mLock) {
            if (!this.mFrameScheduled) {
                long now = SystemClock.uptimeMillis();
                if (this.mCallbackQueues[callbackType].hasDueCallbacksLocked(now)) {
                    scheduleFrameLocked(now);
                }
            }
        }
    }

    public protected void scheduleVsyncLocked() {
        this.mDisplayEventReceiver.scheduleVsync();
    }

    private synchronized boolean isRunningOnLooperThreadLocked() {
        return Looper.myLooper() == this.mLooper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized CallbackRecord obtainCallbackLocked(long dueTime, Object action, Object token) {
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
    public synchronized void recycleCallbackLocked(CallbackRecord callback) {
        callback.action = null;
        callback.token = null;
        callback.next = this.mCallbackPool;
        this.mCallbackPool = callback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class FrameHandler extends Handler {
        public FrameHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Choreographer.this.doFrame(System.nanoTime(), 0);
                    return;
                case 1:
                    Choreographer.this.doScheduleVsync();
                    return;
                case 2:
                    Choreographer.this.doScheduleCallback(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class FrameDisplayEventReceiver extends DisplayEventReceiver implements Runnable {
        private int mFrame;
        private boolean mHavePendingVsync;
        private long mTimestampNanos;

        public FrameDisplayEventReceiver(Looper looper, int vsyncSource) {
            super(looper, vsyncSource);
        }

        public synchronized void onVsync(long timestampNanos, int builtInDisplayId, int frame) {
            if (builtInDisplayId != 0) {
                Log.d(Choreographer.TAG, "Received vsync from secondary display, but we don't support this case yet.  Choreographer needs a way to explicitly request vsync for a specific display to ensure it doesn't lose track of its scheduled vsync.");
                scheduleVsync();
                return;
            }
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
            Choreographer.this.mHandler.sendMessageAtTime(msg, timestampNanos / 1000000);
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mHavePendingVsync = false;
            Choreographer.this.doFrame(this.mTimestampNanos, this.mFrame);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class CallbackRecord {
        public Object action;
        public long dueTime;
        public CallbackRecord next;
        public Object token;

        private synchronized CallbackRecord() {
        }

        private protected void run(long frameTimeNanos) {
            if (this.token == Choreographer.FRAME_CALLBACK_TOKEN) {
                ((FrameCallback) this.action).doFrame(frameTimeNanos);
            } else {
                ((Runnable) this.action).run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class CallbackQueue {
        private CallbackRecord mHead;

        private CallbackQueue() {
        }

        public synchronized boolean hasDueCallbacksLocked(long now) {
            return this.mHead != null && this.mHead.dueTime <= now;
        }

        public synchronized CallbackRecord extractDueCallbacksLocked(long now) {
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

        private protected void addCallbackLocked(long dueTime, Object action, Object token) {
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

        public synchronized void removeCallbacksLocked(Object action, Object token) {
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
