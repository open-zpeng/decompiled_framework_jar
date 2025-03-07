package android.media;

import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public abstract class Image implements AutoCloseable {
    private Rect mCropRect;
    protected boolean mIsImageValid = false;

    /* loaded from: classes2.dex */
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

    public abstract int getScalingMode();

    public abstract long getTimestamp();

    public abstract int getTransform();

    public abstract int getWidth();

    /* JADX INFO: Access modifiers changed from: protected */
    public void throwISEIfImageIsInvalid() {
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
        Rect rect = this.mCropRect;
        if (rect == null) {
            return new Rect(0, 0, getWidth(), getHeight());
        }
        return new Rect(rect);
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
    public boolean isAttachable() {
        throwISEIfImageIsInvalid();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getOwner() {
        throwISEIfImageIsInvalid();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getNativeContext() {
        throwISEIfImageIsInvalid();
        return 0L;
    }
}
