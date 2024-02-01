package android.filterfw.core;
/* loaded from: classes.dex */
public class NativeBuffer {
    private Frame mAttachedFrame;
    private long mDataPointer;
    private boolean mOwnsData;
    private int mRefCount;
    private int mSize;

    private native boolean allocate(int i);

    private native boolean deallocate(boolean z);

    private native boolean nativeCopyTo(NativeBuffer nativeBuffer);

    public synchronized NativeBuffer() {
        this.mDataPointer = 0L;
        this.mSize = 0;
        this.mOwnsData = false;
        this.mRefCount = 1;
    }

    public synchronized NativeBuffer(int count) {
        this.mDataPointer = 0L;
        this.mSize = 0;
        this.mOwnsData = false;
        this.mRefCount = 1;
        allocate(getElementSize() * count);
        this.mOwnsData = true;
    }

    public synchronized NativeBuffer mutableCopy() {
        try {
            Class myClass = getClass();
            NativeBuffer result = (NativeBuffer) myClass.newInstance();
            if (this.mSize <= 0 || nativeCopyTo(result)) {
                return result;
            }
            throw new RuntimeException("Failed to copy NativeBuffer to mutable instance!");
        } catch (Exception e) {
            throw new RuntimeException("Unable to allocate a copy of " + getClass() + "! Make sure the class has a default constructor!");
        }
    }

    public synchronized int size() {
        return this.mSize;
    }

    public synchronized int count() {
        if (this.mDataPointer != 0) {
            return this.mSize / getElementSize();
        }
        return 0;
    }

    public synchronized int getElementSize() {
        return 1;
    }

    public synchronized NativeBuffer retain() {
        if (this.mAttachedFrame != null) {
            this.mAttachedFrame.retain();
        } else if (this.mOwnsData) {
            this.mRefCount++;
        }
        return this;
    }

    public synchronized NativeBuffer release() {
        boolean doDealloc = false;
        if (this.mAttachedFrame != null) {
            doDealloc = this.mAttachedFrame.release() == null;
        } else if (this.mOwnsData) {
            this.mRefCount--;
            doDealloc = this.mRefCount == 0;
        }
        if (doDealloc) {
            deallocate(this.mOwnsData);
            return null;
        }
        return this;
    }

    public synchronized boolean isReadOnly() {
        if (this.mAttachedFrame != null) {
            return this.mAttachedFrame.isReadOnly();
        }
        return false;
    }

    static {
        System.loadLibrary("filterfw");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void attachToFrame(Frame frame) {
        this.mAttachedFrame = frame;
    }

    protected synchronized void assertReadable() {
        if (this.mDataPointer == 0 || this.mSize == 0 || (this.mAttachedFrame != null && !this.mAttachedFrame.hasNativeAllocation())) {
            throw new NullPointerException("Attempting to read from null data frame!");
        }
    }

    protected synchronized void assertWritable() {
        if (isReadOnly()) {
            throw new RuntimeException("Attempting to modify read-only native (structured) data!");
        }
    }
}
