package android.view;

import android.annotation.UnsupportedAppUsage;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public abstract class InputEventSender {
    private static final String TAG = "InputEventSender";
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private InputChannel mInputChannel;
    private MessageQueue mMessageQueue;
    private long mSenderPtr;

    private static native void nativeDispose(long j);

    private static native long nativeInit(WeakReference<InputEventSender> weakReference, InputChannel inputChannel, MessageQueue messageQueue);

    private static native boolean nativeSendKeyEvent(long j, int i, KeyEvent keyEvent);

    private static native boolean nativeSendMotionEvent(long j, int i, MotionEvent motionEvent);

    public InputEventSender(InputChannel inputChannel, Looper looper) {
        if (inputChannel == null) {
            throw new IllegalArgumentException("inputChannel must not be null");
        }
        if (looper == null) {
            throw new IllegalArgumentException("looper must not be null");
        }
        this.mInputChannel = inputChannel;
        this.mMessageQueue = looper.getQueue();
        this.mSenderPtr = nativeInit(new WeakReference(this), inputChannel, this.mMessageQueue);
        this.mCloseGuard.open("dispose");
    }

    protected void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    public void dispose() {
        dispose(false);
    }

    private void dispose(boolean finalized) {
        CloseGuard closeGuard = this.mCloseGuard;
        if (closeGuard != null) {
            if (finalized) {
                closeGuard.warnIfOpen();
            }
            this.mCloseGuard.close();
        }
        long j = this.mSenderPtr;
        if (j != 0) {
            nativeDispose(j);
            this.mSenderPtr = 0L;
        }
        this.mInputChannel = null;
        this.mMessageQueue = null;
    }

    public void onInputEventFinished(int seq, boolean handled) {
    }

    public final boolean sendInputEvent(int seq, InputEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("event must not be null");
        }
        long j = this.mSenderPtr;
        if (j == 0) {
            Log.w(TAG, "Attempted to send an input event but the input event sender has already been disposed.");
            return false;
        } else if (event instanceof KeyEvent) {
            return nativeSendKeyEvent(j, seq, (KeyEvent) event);
        } else {
            return nativeSendMotionEvent(j, seq, (MotionEvent) event);
        }
    }

    @UnsupportedAppUsage
    private void dispatchInputEventFinished(int seq, boolean handled) {
        onInputEventFinished(seq, handled);
    }
}
