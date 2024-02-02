package android.filterfw.core;

import android.graphics.SurfaceTexture;
import android.media.MediaRecorder;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
/* loaded from: classes.dex */
public class GLEnvironment {
    private int glEnvId;
    private boolean mManageContext = true;

    private native boolean nativeActivate();

    private native boolean nativeActivateSurfaceId(int i);

    private native int nativeAddSurface(Surface surface);

    private native int nativeAddSurfaceFromMediaRecorder(MediaRecorder mediaRecorder);

    private native int nativeAddSurfaceWidthHeight(Surface surface, int i, int i2);

    private native boolean nativeAllocate();

    private native boolean nativeDeactivate();

    private native boolean nativeDeallocate();

    private native boolean nativeDisconnectSurfaceMediaSource(MediaRecorder mediaRecorder);

    private native boolean nativeInitWithCurrentContext();

    private native boolean nativeInitWithNewContext();

    private native boolean nativeIsActive();

    private static native boolean nativeIsAnyContextActive();

    private native boolean nativeIsContextActive();

    private native boolean nativeRemoveSurfaceId(int i);

    private native boolean nativeSetSurfaceTimestamp(long j);

    private native boolean nativeSwapBuffers();

    public synchronized GLEnvironment() {
        nativeAllocate();
    }

    private synchronized GLEnvironment(NativeAllocatorTag tag) {
    }

    public synchronized void tearDown() {
        if (this.glEnvId != -1) {
            nativeDeallocate();
            this.glEnvId = -1;
        }
    }

    protected void finalize() throws Throwable {
        tearDown();
    }

    public synchronized void initWithNewContext() {
        this.mManageContext = true;
        if (!nativeInitWithNewContext()) {
            throw new RuntimeException("Could not initialize GLEnvironment with new context!");
        }
    }

    public synchronized void initWithCurrentContext() {
        this.mManageContext = false;
        if (!nativeInitWithCurrentContext()) {
            throw new RuntimeException("Could not initialize GLEnvironment with current context!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isActive() {
        return nativeIsActive();
    }

    public synchronized boolean isContextActive() {
        return nativeIsContextActive();
    }

    public static synchronized boolean isAnyContextActive() {
        return nativeIsAnyContextActive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void activate() {
        if (Looper.myLooper() != null && Looper.myLooper().equals(Looper.getMainLooper())) {
            Log.e("FilterFramework", "Activating GL context in UI thread!");
        }
        if (this.mManageContext && !nativeActivate()) {
            throw new RuntimeException("Could not activate GLEnvironment!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deactivate() {
        if (this.mManageContext && !nativeDeactivate()) {
            throw new RuntimeException("Could not deactivate GLEnvironment!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void swapBuffers() {
        if (!nativeSwapBuffers()) {
            throw new RuntimeException("Error swapping EGL buffers!");
        }
    }

    public synchronized int registerSurface(Surface surface) {
        int result = nativeAddSurface(surface);
        if (result < 0) {
            throw new RuntimeException("Error registering surface " + surface + "!");
        }
        return result;
    }

    public synchronized int registerSurfaceTexture(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
        int result = nativeAddSurfaceWidthHeight(surface, width, height);
        surface.release();
        if (result < 0) {
            throw new RuntimeException("Error registering surfaceTexture " + surfaceTexture + "!");
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int registerSurfaceFromMediaRecorder(MediaRecorder mediaRecorder) {
        int result = nativeAddSurfaceFromMediaRecorder(mediaRecorder);
        if (result < 0) {
            throw new RuntimeException("Error registering surface from MediaRecorder" + mediaRecorder + "!");
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void activateSurfaceWithId(int surfaceId) {
        if (!nativeActivateSurfaceId(surfaceId)) {
            throw new RuntimeException("Could not activate surface " + surfaceId + "!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterSurfaceId(int surfaceId) {
        if (!nativeRemoveSurfaceId(surfaceId)) {
            throw new RuntimeException("Could not unregister surface " + surfaceId + "!");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSurfaceTimestamp(long timestamp) {
        if (!nativeSetSurfaceTimestamp(timestamp)) {
            throw new RuntimeException("Could not set timestamp for current surface!");
        }
    }

    static {
        System.loadLibrary("filterfw");
    }
}
