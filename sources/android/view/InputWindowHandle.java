package android.view;

import android.graphics.Region;
import android.os.IBinder;
import android.telephony.SmsManager;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public final class InputWindowHandle {
    public boolean canReceiveKeys;
    public final IWindow clientWindow;
    public long dispatchingTimeoutNanos;
    public int displayId;
    public int frameBottom;
    public int frameLeft;
    public int frameRight;
    public int frameTop;
    public boolean hasFocus;
    public boolean hasWallpaper;
    public final InputApplicationHandle inputApplicationHandle;
    public int inputFeatures;
    public int layer;
    public int layoutParamsFlags;
    public int layoutParamsType;
    public String name;
    public int ownerPid;
    public int ownerUid;
    public boolean paused;
    private long ptr;
    public boolean replaceTouchableRegionWithCrop;
    public float scaleFactor;
    public int surfaceInset;
    public IBinder token;
    public boolean visible;
    public final Region touchableRegion = new Region();
    public int portalToDisplayId = -1;
    public WeakReference<IBinder> touchableRegionCropHandle = new WeakReference<>(null);

    private native void nativeDispose();

    public InputWindowHandle(InputApplicationHandle inputApplicationHandle, IWindow clientWindow, int displayId) {
        this.inputApplicationHandle = inputApplicationHandle;
        this.clientWindow = clientWindow;
        this.displayId = displayId;
    }

    public String toString() {
        String str = this.name;
        if (str == null) {
            str = "";
        }
        return str + ", layer=" + this.layer + ", frame=[" + this.frameLeft + SmsManager.REGEX_PREFIX_DELIMITER + this.frameTop + SmsManager.REGEX_PREFIX_DELIMITER + this.frameRight + SmsManager.REGEX_PREFIX_DELIMITER + this.frameBottom + "], touchableRegion=" + this.touchableRegion + ", visible=" + this.visible;
    }

    protected void finalize() throws Throwable {
        try {
            nativeDispose();
        } finally {
            super.finalize();
        }
    }

    public void replaceTouchableRegionWithCrop(SurfaceControl bounds) {
        setTouchableRegionCrop(bounds);
        this.replaceTouchableRegionWithCrop = true;
    }

    public void setTouchableRegionCrop(SurfaceControl bounds) {
        if (bounds != null) {
            this.touchableRegionCropHandle = new WeakReference<>(bounds.getHandle());
        }
    }
}
