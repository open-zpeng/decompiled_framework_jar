package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import android.util.Size;
import com.android.internal.util.Preconditions;
/* loaded from: classes.dex */
public final class StreamConfiguration {
    private final int mFormat;
    private final int mHeight;
    private final boolean mInput;
    private final int mWidth;

    public synchronized StreamConfiguration(int format, int width, int height, boolean input) {
        this.mFormat = StreamConfigurationMap.checkArgumentFormatInternal(format);
        this.mWidth = Preconditions.checkArgumentPositive(width, "width must be positive");
        this.mHeight = Preconditions.checkArgumentPositive(height, "height must be positive");
        this.mInput = input;
    }

    public final synchronized int getFormat() {
        return this.mFormat;
    }

    public synchronized int getWidth() {
        return this.mWidth;
    }

    public synchronized int getHeight() {
        return this.mHeight;
    }

    public synchronized Size getSize() {
        return new Size(this.mWidth, this.mHeight);
    }

    public synchronized boolean isInput() {
        return this.mInput;
    }

    public synchronized boolean isOutput() {
        return !this.mInput;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StreamConfiguration)) {
            return false;
        }
        StreamConfiguration other = (StreamConfiguration) obj;
        if (this.mFormat != other.mFormat || this.mWidth != other.mWidth || this.mHeight != other.mHeight || this.mInput != other.mInput) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return HashCodeHelpers.hashCode(this.mFormat, this.mWidth, this.mHeight, this.mInput ? 1 : 0);
    }
}
