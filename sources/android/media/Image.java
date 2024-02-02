package android.media;

import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public abstract class Image implements AutoCloseable {
    private Rect mCropRect;
    protected boolean mIsImageValid = false;

    /* loaded from: classes.dex */
    public static abstract class Plane {
        public abstract ByteBuffer getBuffer();

        public abstract int getPixelStride();

        public abstract int getRowStride();
    }

    @Override // java.lang.AutoCloseable
    public abstract void close();

    public abstract int getFormat();

    public abstract int getHeight();

    public abstract Plane[] getPlanes();

    public abstract synchronized int getScalingMode();

    public abstract long getTimestamp();

    public abstract synchronized int getTransform();

    public abstract int getWidth();

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void throwISEIfImageIsInvalid() {
        if (!this.mIsImageValid) {
            throw new IllegalStateException("Image is already closed");
        }
    }

    public HardwareBuffer getHardwareBuffer() {
        throwISEIfImageIsInvalid();
        return null;
    }

    public void setTimestamp(long timestamp) {
        throwISEIfImageIsInvalid();
    }

    public Rect getCropRect() {
        throwISEIfImageIsInvalid();
        if (this.mCropRect == null) {
            return new Rect(0, 0, getWidth(), getHeight());
        }
        return new Rect(this.mCropRect);
    }

    public void setCropRect(Rect cropRect) {
        throwISEIfImageIsInvalid();
        if (cropRect != null) {
            cropRect = new Rect(cropRect);
            if (!cropRect.intersect(0, 0, getWidth(), getHeight())) {
                cropRect.setEmpty();
            }
        }
        this.mCropRect = cropRect;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isAttachable() {
        throwISEIfImageIsInvalid();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Object getOwner() {
        throwISEIfImageIsInvalid();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized long getNativeContext() {
        throwISEIfImageIsInvalid();
        return 0L;
    }
}
