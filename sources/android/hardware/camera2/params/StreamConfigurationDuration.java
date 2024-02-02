package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import android.util.Size;
import com.android.internal.util.Preconditions;
/* loaded from: classes.dex */
public final class StreamConfigurationDuration {
    private final long mDurationNs;
    private final int mFormat;
    private final int mHeight;
    private final int mWidth;

    public synchronized StreamConfigurationDuration(int format, int width, int height, long durationNs) {
        this.mFormat = StreamConfigurationMap.checkArgumentFormatInternal(format);
        this.mWidth = Preconditions.checkArgumentPositive(width, "width must be positive");
        this.mHeight = Preconditions.checkArgumentPositive(height, "height must be positive");
        this.mDurationNs = Preconditions.checkArgumentNonnegative(durationNs, "durationNs must be non-negative");
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

    public synchronized long getDuration() {
        return this.mDurationNs;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StreamConfigurationDuration)) {
            return false;
        }
        StreamConfigurationDuration other = (StreamConfigurationDuration) obj;
        if (this.mFormat != other.mFormat || this.mWidth != other.mWidth || this.mHeight != other.mHeight || this.mDurationNs != other.mDurationNs) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return HashCodeHelpers.hashCode(this.mFormat, this.mWidth, this.mHeight, (int) this.mDurationNs, (int) (this.mDurationNs >>> 32));
    }
}
