package android.filterfw.core;

import java.util.Arrays;
/* loaded from: classes.dex */
public class MutableFrameFormat extends FrameFormat {
    public synchronized MutableFrameFormat() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MutableFrameFormat(int baseType, int target) {
        super(baseType, target);
    }

    public synchronized void setBaseType(int baseType) {
        this.mBaseType = baseType;
        this.mBytesPerSample = bytesPerSampleOf(baseType);
    }

    public synchronized void setTarget(int target) {
        this.mTarget = target;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBytesPerSample(int bytesPerSample) {
        this.mBytesPerSample = bytesPerSample;
        this.mSize = -1;
    }

    public synchronized void setDimensions(int[] dimensions) {
        this.mDimensions = dimensions == null ? null : Arrays.copyOf(dimensions, dimensions.length);
        this.mSize = -1;
    }

    public synchronized void setDimensions(int size) {
        int[] dimensions = {size};
        this.mDimensions = dimensions;
        this.mSize = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDimensions(int width, int height) {
        int[] dimensions = {width, height};
        this.mDimensions = dimensions;
        this.mSize = -1;
    }

    public synchronized void setDimensions(int width, int height, int depth) {
        int[] dimensions = {width, height, depth};
        this.mDimensions = dimensions;
        this.mSize = -1;
    }

    public synchronized void setDimensionCount(int count) {
        this.mDimensions = new int[count];
    }

    public synchronized void setObjectClass(Class objectClass) {
        this.mObjectClass = objectClass;
    }

    public synchronized void setMetaValue(String key, Object value) {
        if (this.mMetaData == null) {
            this.mMetaData = new KeyValueMap();
        }
        this.mMetaData.put(key, value);
    }
}
