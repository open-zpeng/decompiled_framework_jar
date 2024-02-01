package android.filterfw.core;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public abstract class Frame {
    public static final int NO_BINDING = 0;
    public static final long TIMESTAMP_NOT_SET = -2;
    public static final long TIMESTAMP_UNKNOWN = -1;
    private long mBindingId;
    private int mBindingType;
    private FrameFormat mFormat;
    private FrameManager mFrameManager;
    private boolean mReadOnly;
    private int mRefCount;
    private boolean mReusable;
    private long mTimestamp;

    /* JADX INFO: Access modifiers changed from: private */
    public abstract Bitmap getBitmap();

    public abstract synchronized ByteBuffer getData();

    public abstract synchronized float[] getFloats();

    public abstract synchronized int[] getInts();

    public abstract synchronized Object getObjectValue();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract synchronized boolean hasNativeAllocation();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract synchronized void releaseNativeAllocation();

    public abstract synchronized void setBitmap(Bitmap bitmap);

    public abstract synchronized void setData(ByteBuffer byteBuffer, int i, int i2);

    public abstract synchronized void setFloats(float[] fArr);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract void setInts(int[] iArr);

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Frame(FrameFormat format, FrameManager frameManager) {
        this.mReadOnly = false;
        this.mReusable = false;
        this.mRefCount = 1;
        this.mBindingType = 0;
        this.mBindingId = 0L;
        this.mTimestamp = -2L;
        this.mFormat = format.mutableCopy();
        this.mFrameManager = frameManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Frame(FrameFormat format, FrameManager frameManager, int bindingType, long bindingId) {
        this.mReadOnly = false;
        this.mReusable = false;
        this.mRefCount = 1;
        this.mBindingType = 0;
        this.mBindingId = 0L;
        this.mTimestamp = -2L;
        this.mFormat = format.mutableCopy();
        this.mFrameManager = frameManager;
        this.mBindingType = bindingType;
        this.mBindingId = bindingId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FrameFormat getFormat() {
        return this.mFormat;
    }

    public synchronized int getCapacity() {
        return getFormat().getSize();
    }

    public synchronized boolean isReadOnly() {
        return this.mReadOnly;
    }

    public synchronized int getBindingType() {
        return this.mBindingType;
    }

    public synchronized long getBindingId() {
        return this.mBindingId;
    }

    public synchronized void setObjectValue(Object object) {
        assertFrameMutable();
        if (object instanceof int[]) {
            setInts((int[]) object);
        } else if (object instanceof float[]) {
            setFloats((float[]) object);
        } else if (object instanceof ByteBuffer) {
            setData((ByteBuffer) object);
        } else if (object instanceof Bitmap) {
            setBitmap((Bitmap) object);
        } else {
            setGenericObjectValue(object);
        }
    }

    public synchronized void setData(ByteBuffer buffer) {
        setData(buffer, 0, buffer.limit());
    }

    public synchronized void setData(byte[] bytes, int offset, int length) {
        setData(ByteBuffer.wrap(bytes, offset, length));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getTimestamp() {
        return this.mTimestamp;
    }

    public synchronized void setDataFromFrame(Frame frame) {
        setData(frame.getData());
    }

    protected synchronized boolean requestResize(int[] newDimensions) {
        return false;
    }

    public synchronized int getRefCount() {
        return this.mRefCount;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Frame release() {
        if (this.mFrameManager != null) {
            return this.mFrameManager.releaseFrame(this);
        }
        return this;
    }

    public synchronized Frame retain() {
        if (this.mFrameManager != null) {
            return this.mFrameManager.retainFrame(this);
        }
        return this;
    }

    public synchronized FrameManager getFrameManager() {
        return this.mFrameManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void assertFrameMutable() {
        if (isReadOnly()) {
            throw new RuntimeException("Attempting to modify read-only frame!");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void setReusable(boolean reusable) {
        this.mReusable = reusable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void setFormat(FrameFormat format) {
        this.mFormat = format.mutableCopy();
    }

    protected synchronized void setGenericObjectValue(Object value) {
        throw new RuntimeException("Cannot set object value of unsupported type: " + value.getClass());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized Bitmap convertBitmapToRGBA(Bitmap bitmap) {
        if (bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
            return bitmap;
        }
        Bitmap result = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        if (result == null) {
            throw new RuntimeException("Error converting bitmap to RGBA!");
        }
        if (result.getRowBytes() != result.getWidth() * 4) {
            throw new RuntimeException("Unsupported row byte count in bitmap!");
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void reset(FrameFormat newFormat) {
        this.mFormat = newFormat.mutableCopy();
        this.mReadOnly = false;
        this.mRefCount = 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void onFrameStore() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void onFrameFetch() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized int incRefCount() {
        this.mRefCount++;
        return this.mRefCount;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized int decRefCount() {
        this.mRefCount--;
        return this.mRefCount;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized boolean isReusable() {
        return this.mReusable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void markReadOnly() {
        this.mReadOnly = true;
    }
}
