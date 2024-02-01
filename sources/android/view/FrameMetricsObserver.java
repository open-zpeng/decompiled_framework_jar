package android.view;

import android.annotation.UnsupportedAppUsage;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.Window;
import com.android.internal.util.VirtualRefBasePtr;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class FrameMetricsObserver {
    @UnsupportedAppUsage
    private FrameMetrics mFrameMetrics;
    Window.OnFrameMetricsAvailableListener mListener;
    @UnsupportedAppUsage
    private MessageQueue mMessageQueue;
    public VirtualRefBasePtr mNative;
    private WeakReference<Window> mWindow;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FrameMetricsObserver(Window window, Looper looper, Window.OnFrameMetricsAvailableListener listener) {
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

    @UnsupportedAppUsage
    private void notifyDataAvailable(int dropCount) {
        Window window = this.mWindow.get();
        if (window != null) {
            this.mListener.onFrameMetricsAvailable(window, this.mFrameMetrics, dropCount);
        }
    }
}
