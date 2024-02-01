package android.view;

import android.os.Looper;
import android.os.MessageQueue;
import android.view.Window;
import com.android.internal.util.VirtualRefBasePtr;
import java.lang.ref.WeakReference;
/* loaded from: classes2.dex */
public class FrameMetricsObserver {
    public protected FrameMetrics mFrameMetrics;
    Window.OnFrameMetricsAvailableListener mListener;
    public protected MessageQueue mMessageQueue;
    VirtualRefBasePtr mNative;
    private WeakReference<Window> mWindow;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized FrameMetricsObserver(Window window, Looper looper, Window.OnFrameMetricsAvailableListener listener) {
        if (looper == null) {
            throw new NullPointerException("looper cannot be null");
        }
        this.mMessageQueue = looper.getQueue();
        if (this.mMessageQueue == null) {
            throw new IllegalStateException("invalid looper, null message queue\n");
        }
        this.mFrameMetrics = new FrameMetrics();
        this.mWindow = new WeakReference<>(window);
        this.mListener = listener;
    }

    public protected void notifyDataAvailable(int dropCount) {
        Window window = this.mWindow.get();
        if (window != null) {
            this.mListener.onFrameMetricsAvailable(window, this.mFrameMetrics, dropCount);
        }
    }
}
