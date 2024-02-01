package android.hardware.camera2.legacy;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.legacy.RequestThreadManager;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.util.Collection;
/* loaded from: classes.dex */
public class GLThreadManager {
    public protected static final boolean DEBUG = false;
    public protected static final int MSG_ALLOW_FRAMES = 5;
    public protected static final int MSG_CLEANUP = 3;
    public protected static final int MSG_DROP_FRAMES = 4;
    public protected static final int MSG_NEW_CONFIGURATION = 1;
    public protected static final int MSG_NEW_FRAME = 2;
    public protected final String TAG;
    public protected CaptureCollector mCaptureCollector;
    public protected final CameraDeviceState mDeviceState;
    public protected final RequestHandlerThread mGLHandlerThread;
    public protected final SurfaceTextureRenderer mTextureRenderer;
    public protected final RequestThreadManager.FpsCounter mPrevCounter = new RequestThreadManager.FpsCounter("GL Preview Producer");
    public protected final Handler.Callback mGLHandlerCb = new Handler.Callback() { // from class: android.hardware.camera2.legacy.GLThreadManager.1
        private boolean mCleanup = false;
        private boolean mConfigured = false;
        private boolean mDroppingFrames = false;

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message msg) {
            if (this.mCleanup) {
                return true;
            }
            try {
                int i = msg.what;
                if (i != -1) {
                    switch (i) {
                        case 1:
                            ConfigureHolder configure = (ConfigureHolder) msg.obj;
                            GLThreadManager.this.mTextureRenderer.cleanupEGLContext();
                            GLThreadManager.this.mTextureRenderer.configureSurfaces(configure.surfaces);
                            GLThreadManager.this.mCaptureCollector = (CaptureCollector) Preconditions.checkNotNull(configure.collector);
                            configure.condition.open();
                            this.mConfigured = true;
                            break;
                        case 2:
                            if (this.mDroppingFrames) {
                                Log.w(GLThreadManager.this.TAG, "Ignoring frame.");
                                break;
                            } else {
                                if (!this.mConfigured) {
                                    Log.e(GLThreadManager.this.TAG, "Dropping frame, EGL context not configured!");
                                }
                                GLThreadManager.this.mTextureRenderer.drawIntoSurfaces(GLThreadManager.this.mCaptureCollector);
                                break;
                            }
                        case 3:
                            GLThreadManager.this.mTextureRenderer.cleanupEGLContext();
                            this.mCleanup = true;
                            this.mConfigured = false;
                            break;
                        case 4:
                            this.mDroppingFrames = true;
                            break;
                        case 5:
                            this.mDroppingFrames = false;
                            break;
                        default:
                            String str = GLThreadManager.this.TAG;
                            Log.e(str, "Unhandled message " + msg.what + " on GLThread.");
                            break;
                    }
                }
            } catch (Exception e) {
                Log.e(GLThreadManager.this.TAG, "Received exception on GL render thread: ", e);
                GLThreadManager.this.mDeviceState.setError(1);
            }
            return true;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ConfigureHolder {
        private protected final CaptureCollector collector;
        private protected final ConditionVariable condition;
        private protected final Collection<Pair<Surface, Size>> surfaces;

        private protected synchronized ConfigureHolder(ConditionVariable condition, Collection<Pair<Surface, Size>> surfaces, CaptureCollector collector) {
            this.condition = condition;
            this.surfaces = surfaces;
            this.collector = collector;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized GLThreadManager(int cameraId, int facing, CameraDeviceState state) {
        this.mTextureRenderer = new SurfaceTextureRenderer(facing);
        this.TAG = String.format("CameraDeviceGLThread-%d", Integer.valueOf(cameraId));
        this.mGLHandlerThread = new RequestHandlerThread(this.TAG, this.mGLHandlerCb);
        this.mDeviceState = state;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void start() {
        this.mGLHandlerThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void waitUntilStarted() {
        this.mGLHandlerThread.waitUntilStarted();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void quit() {
        Handler handler = this.mGLHandlerThread.getHandler();
        handler.sendMessageAtFrontOfQueue(handler.obtainMessage(3));
        this.mGLHandlerThread.quitSafely();
        try {
            this.mGLHandlerThread.join();
        } catch (InterruptedException e) {
            Log.e(this.TAG, String.format("Thread %s (%d) interrupted while quitting.", this.mGLHandlerThread.getName(), Long.valueOf(this.mGLHandlerThread.getId())));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void queueNewFrame() {
        Handler handler = this.mGLHandlerThread.getHandler();
        if (!handler.hasMessages(2)) {
            handler.sendMessage(handler.obtainMessage(2));
        } else {
            Log.e(this.TAG, "GLThread dropping frame.  Not consuming frames quickly enough!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setConfigurationAndWait(Collection<Pair<Surface, Size>> surfaces, CaptureCollector collector) {
        Preconditions.checkNotNull(collector, "collector must not be null");
        Handler handler = this.mGLHandlerThread.getHandler();
        ConditionVariable condition = new ConditionVariable(false);
        ConfigureHolder configure = new ConfigureHolder(condition, surfaces, collector);
        Message m = handler.obtainMessage(1, 0, 0, configure);
        handler.sendMessage(m);
        condition.block();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized SurfaceTexture getCurrentSurfaceTexture() {
        return this.mTextureRenderer.getSurfaceTexture();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void ignoreNewFrames() {
        this.mGLHandlerThread.getHandler().sendEmptyMessage(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void waitUntilIdle() {
        this.mGLHandlerThread.waitUntilIdle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void allowNewFrames() {
        this.mGLHandlerThread.getHandler().sendEmptyMessage(5);
    }
}
