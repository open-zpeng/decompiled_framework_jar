package android.os.caton;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class StackTraceCollector implements Collector {
    private static final String CATON_STACK_INFO = "caton_stack_info";
    private static final int COLLECT_SPACE_TIME = 3000;
    private static final int MIN_COLLECT_COUNT = 3;
    private static final int MSG_BEGIN_WATCH = 54;
    private static final int MSG_COLLECT_CONTINUE = 55;
    private static final String TAG = "StackTraceCollector";
    private static final String THREAD_TAG = "-----";
    private long mCollectInterval;
    private volatile CollectorHandler mCollectorHandler;
    private volatile boolean mIsWatching;
    private StackTraceElement[] mLastStackTrace;
    private int mLimitLength;
    private Thread mMainThread;
    private int[] mRepeatTimes;
    private ArrayList<StackTraceElement[]> mStackQueue;

    public StackTraceCollector(long collectInterval) {
        this.mCollectInterval = collectInterval;
        HandlerThread thread = new HandlerThread(TAG);
        thread.setPriority(10);
        thread.start();
        this.mCollectorHandler = new CollectorHandler(thread.getLooper());
        this.mLimitLength = 3;
        this.mStackQueue = new ArrayList<>(this.mLimitLength);
        this.mRepeatTimes = new int[this.mLimitLength];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
        synchronized (this.mStackQueue) {
            if (!this.mStackQueue.isEmpty()) {
                this.mLastStackTrace = null;
                this.mStackQueue.clear();
                Arrays.fill(this.mRepeatTimes, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void increaseRepeatTimes() {
        synchronized (this.mStackQueue) {
            int currentIndex = this.mStackQueue.size() - 1;
            this.mRepeatTimes[currentIndex] = this.mRepeatTimes[currentIndex] + 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trigger(int what) {
        Message message = this.mCollectorHandler.obtainMessage();
        message.obj = this;
        message.what = what;
        this.mCollectorHandler.sendMessageDelayed(message, this.mCollectInterval);
    }

    public boolean isWatching() {
        return this.mIsWatching;
    }

    @Override // android.os.caton.Collector
    public void start() {
        this.mIsWatching = true;
        trigger(54);
    }

    @Override // android.os.caton.Collector
    public void stop() {
        this.mIsWatching = false;
        this.mCollectorHandler.removeMessages(54);
        this.mCollectorHandler.removeMessages(55);
    }

    @Override // android.os.caton.Collector
    public int[] getStackTraceRepeats() {
        int[] repeats;
        synchronized (this.mStackQueue) {
            repeats = Arrays.copyOf(this.mRepeatTimes, this.mRepeatTimes.length);
        }
        return repeats;
    }

    @Override // android.os.caton.Collector
    public StackTraceElement[][] getStackTraceInfo() {
        StackTraceElement[][] stackTraceElementArr;
        synchronized (this.mStackQueue) {
            stackTraceElementArr = (StackTraceElement[][]) this.mStackQueue.toArray((StackTraceElement[][]) Array.newInstance(StackTraceElement.class, 0, 0));
        }
        return stackTraceElementArr;
    }

    @Override // android.os.caton.Collector
    public void add(StackTraceElement[] stackTrace) {
        synchronized (this.mStackQueue) {
            this.mLastStackTrace = stackTrace;
            if (this.mStackQueue.size() >= this.mLimitLength) {
                int maxIndex = this.mLimitLength - 1;
                int removeIndex = maxIndex;
                int minRepeat = this.mRepeatTimes[removeIndex];
                for (int i = maxIndex - 1; i >= 1; i--) {
                    if (minRepeat > this.mRepeatTimes[i]) {
                        removeIndex = i;
                        minRepeat = this.mRepeatTimes[i];
                    }
                }
                this.mStackQueue.remove(removeIndex);
                for (int i2 = removeIndex; i2 < maxIndex; i2++) {
                    this.mRepeatTimes[i2] = this.mRepeatTimes[i2 + 1];
                }
                this.mRepeatTimes[maxIndex] = 0;
            }
            this.mStackQueue.add(stackTrace);
            this.mRepeatTimes[this.mStackQueue.size() - 1] = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class CollectorHandler extends Handler {
        public CollectorHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what != 54 && msg.what != 55) {
                return;
            }
            if (msg.what == 54) {
                StackTraceCollector.this.reset();
            }
            StackTraceElement[] stackTraceElements = StackTraceCollector.this.getMainThreadStackInfo();
            if (StackTraceCollector.isEqualsAndNotNull(stackTraceElements, StackTraceCollector.this.mLastStackTrace)) {
                StackTraceCollector.this.increaseRepeatTimes();
            } else {
                StackTraceCollector.this.add(stackTraceElements);
            }
            if (StackTraceCollector.this.isWatching()) {
                StackTraceCollector.this.trigger(55);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StackTraceElement[] getMainThreadStackInfo() {
        if (this.mMainThread == null) {
            this.mMainThread = Looper.getMainLooper().getThread();
        }
        return this.mMainThread.getStackTrace();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isEqualsAndNotNull(StackTraceElement[] currentStackTraceElements, StackTraceElement[] stackTraceElements) {
        if (currentStackTraceElements == null || stackTraceElements == null) {
            return false;
        }
        int currentStackTraceElementsSize = currentStackTraceElements.length;
        int stackTraceElementsSize = stackTraceElements.length;
        if (currentStackTraceElementsSize != stackTraceElementsSize) {
            return false;
        }
        for (int i = 0; i < currentStackTraceElementsSize; i++) {
            if (!currentStackTraceElements[i].equals(stackTraceElements[i])) {
                return false;
            }
        }
        return true;
    }
}
