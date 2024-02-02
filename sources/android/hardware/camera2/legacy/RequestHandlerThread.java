package android.hardware.camera2.legacy;

import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.MessageQueue;
/* loaded from: classes.dex */
public class RequestHandlerThread extends HandlerThread {
    private protected static final int MSG_POKE_IDLE_HANDLER = -1;
    public protected Handler.Callback mCallback;
    public protected volatile Handler mHandler;
    public protected final ConditionVariable mIdle;
    public protected final MessageQueue.IdleHandler mIdleHandler;
    public protected final ConditionVariable mStarted;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized RequestHandlerThread(String name, Handler.Callback callback) {
        super(name, 10);
        this.mStarted = new ConditionVariable(false);
        this.mIdle = new ConditionVariable(true);
        this.mIdleHandler = new MessageQueue.IdleHandler() { // from class: android.hardware.camera2.legacy.RequestHandlerThread.1
            @Override // android.os.MessageQueue.IdleHandler
            public boolean queueIdle() {
                RequestHandlerThread.this.mIdle.open();
                return false;
            }
        };
        this.mCallback = callback;
    }

    @Override // android.os.HandlerThread
    protected void onLooperPrepared() {
        this.mHandler = new Handler(getLooper(), this.mCallback);
        this.mStarted.open();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void waitUntilStarted() {
        this.mStarted.block();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Handler getHandler() {
        return this.mHandler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Handler waitAndGetHandler() {
        waitUntilStarted();
        return getHandler();
    }

    private protected synchronized boolean hasAnyMessages(int[] what) {
        synchronized (this.mHandler.getLooper().getQueue()) {
            for (int i : what) {
                if (this.mHandler.hasMessages(i)) {
                    return true;
                }
            }
            return false;
        }
    }

    private protected synchronized void removeMessages(int[] what) {
        synchronized (this.mHandler.getLooper().getQueue()) {
            for (int i : what) {
                this.mHandler.removeMessages(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void waitUntilIdle() {
        Handler handler = waitAndGetHandler();
        MessageQueue queue = handler.getLooper().getQueue();
        if (queue.isIdle()) {
            return;
        }
        this.mIdle.close();
        queue.addIdleHandler(this.mIdleHandler);
        handler.sendEmptyMessage(-1);
        if (queue.isIdle()) {
            return;
        }
        this.mIdle.block();
    }
}
