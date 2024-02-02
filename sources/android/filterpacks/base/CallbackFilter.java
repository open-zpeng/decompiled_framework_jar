package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.os.Handler;
import android.os.Looper;
/* loaded from: classes.dex */
public class CallbackFilter extends Filter {
    @GenerateFinalPort(hasDefault = true, name = "callUiThread")
    public protected boolean mCallbacksOnUiThread;
    @GenerateFieldPort(hasDefault = true, name = "listener")
    public protected FilterContext.OnFrameReceivedListener mListener;
    public protected Handler mUiThreadHandler;
    @GenerateFieldPort(hasDefault = true, name = "userData")
    public protected Object mUserData;

    /* loaded from: classes.dex */
    private class CallbackRunnable implements Runnable {
        public protected Filter mFilter;
        public protected Frame mFrame;
        public protected FilterContext.OnFrameReceivedListener mListener;
        public protected Object mUserData;

        public CallbackRunnable(FilterContext.OnFrameReceivedListener listener, Filter filter, Frame frame, Object userData) {
            this.mListener = listener;
            this.mFilter = filter;
            this.mFrame = frame;
            this.mUserData = userData;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mListener.onFrameReceived(this.mFilter, this.mFrame, this.mUserData);
            this.mFrame.release();
        }
    }

    private protected synchronized CallbackFilter(String name) {
        super(name);
        this.mCallbacksOnUiThread = true;
    }

    private protected synchronized void setupPorts() {
        addInputPort("frame");
    }

    private protected synchronized void prepare(FilterContext context) {
        if (this.mCallbacksOnUiThread) {
            this.mUiThreadHandler = new Handler(Looper.getMainLooper());
        }
    }

    private protected synchronized void process(FilterContext context) {
        Frame input = pullInput("frame");
        if (this.mListener != null) {
            if (this.mCallbacksOnUiThread) {
                input.retain();
                CallbackRunnable uiRunnable = new CallbackRunnable(this.mListener, this, input, this.mUserData);
                if (!this.mUiThreadHandler.post(uiRunnable)) {
                    throw new RuntimeException("Unable to send callback to UI thread!");
                }
                return;
            }
            this.mListener.onFrameReceived(this, input, this.mUserData);
            return;
        }
        throw new RuntimeException("CallbackFilter received frame, but no listener set!");
    }
}
