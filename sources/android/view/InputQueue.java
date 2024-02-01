package android.view;

import android.os.Looper;
import android.os.MessageQueue;
import android.util.LongSparseArray;
import android.util.Pools;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;
/* loaded from: classes2.dex */
public final class InputQueue {
    private final LongSparseArray<ActiveInputEvent> mActiveEventArray = new LongSparseArray<>(20);
    private final Pools.Pool<ActiveInputEvent> mActiveInputEventPool = new Pools.SimplePool(20);
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private long mPtr = nativeInit(new WeakReference(this), Looper.myQueue());

    /* loaded from: classes2.dex */
    public interface Callback {
        void onInputQueueCreated(InputQueue inputQueue);

        void onInputQueueDestroyed(InputQueue inputQueue);
    }

    /* loaded from: classes2.dex */
    public interface FinishedInputEventCallback {
        synchronized void onFinishedInputEvent(Object obj, boolean z);
    }

    private static native void nativeDispose(long j);

    private static native long nativeInit(WeakReference<InputQueue> weakReference, MessageQueue messageQueue);

    private static native long nativeSendKeyEvent(long j, KeyEvent keyEvent, boolean z);

    private static native long nativeSendMotionEvent(long j, MotionEvent motionEvent);

    public synchronized InputQueue() {
        this.mCloseGuard.open("dispose");
    }

    protected void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    public synchronized void dispose() {
        dispose(false);
    }

    public synchronized void dispose(boolean finalized) {
        if (this.mCloseGuard != null) {
            if (finalized) {
                this.mCloseGuard.warnIfOpen();
            }
            this.mCloseGuard.close();
        }
        if (this.mPtr != 0) {
            nativeDispose(this.mPtr);
            this.mPtr = 0L;
        }
    }

    public synchronized long getNativePtr() {
        return this.mPtr;
    }

    public synchronized void sendInputEvent(InputEvent e, Object token, boolean predispatch, FinishedInputEventCallback callback) {
        long id;
        ActiveInputEvent event = obtainActiveInputEvent(token, callback);
        if (e instanceof KeyEvent) {
            id = nativeSendKeyEvent(this.mPtr, (KeyEvent) e, predispatch);
        } else {
            long id2 = this.mPtr;
            id = nativeSendMotionEvent(id2, (MotionEvent) e);
        }
        this.mActiveEventArray.put(id, event);
    }

    public protected void finishInputEvent(long id, boolean handled) {
        int index = this.mActiveEventArray.indexOfKey(id);
        if (index >= 0) {
            ActiveInputEvent e = this.mActiveEventArray.valueAt(index);
            this.mActiveEventArray.removeAt(index);
            e.mCallback.onFinishedInputEvent(e.mToken, handled);
            recycleActiveInputEvent(e);
        }
    }

    private synchronized ActiveInputEvent obtainActiveInputEvent(Object token, FinishedInputEventCallback callback) {
        ActiveInputEvent e = this.mActiveInputEventPool.acquire();
        if (e == null) {
            e = new ActiveInputEvent();
        }
        e.mToken = token;
        e.mCallback = callback;
        return e;
    }

    private synchronized void recycleActiveInputEvent(ActiveInputEvent e) {
        e.recycle();
        this.mActiveInputEventPool.release(e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class ActiveInputEvent {
        public FinishedInputEventCallback mCallback;
        public Object mToken;

        private ActiveInputEvent() {
        }

        public synchronized void recycle() {
            this.mToken = null;
            this.mCallback = null;
        }
    }
}
