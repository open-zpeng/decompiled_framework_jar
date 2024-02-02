package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import android.util.Range;
import android.util.Size;
import com.android.internal.util.Preconditions;
/* loaded from: classes.dex */
public final class HighSpeedVideoConfiguration {
    private static final int HIGH_SPEED_MAX_MINIMAL_FPS = 120;
    private final int mBatchSizeMax;
    private final int mFpsMax;
    private final int mFpsMin;
    private final Range<Integer> mFpsRange;
    private final int mHeight;
    private final Size mSize;
    private final int mWidth;

    public synchronized HighSpeedVideoConfiguration(int width, int height, int fpsMin, int fpsMax, int batchSizeMax) {
        if (fpsMax < 120) {
            throw new IllegalArgumentException("fpsMax must be at least 120");
        }
        this.mFpsMax = fpsMax;
        this.mWidth = Preconditions.checkArgumentPositive(width, "width must be positive");
        this.mHeight = Preconditions.checkArgumentPositive(height, "height must be positive");
        this.mFpsMin = Preconditions.checkArgumentPositive(fpsMin, "fpsMin must be positive");
        this.mSize = new Size(this.mWidth, this.mHeight);
        this.mBatchSizeMax = Preconditions.checkArgumentPositive(batchSizeMax, "batchSizeMax must be positive");
        this.mFpsRange = new Range<>(Integer.valueOf(this.mFpsMin), Integer.valueOf(this.mFpsMax));
    }

    public synchronized int getWidth() {
        return this.mWidth;
    }

    public synchronized int getHeight() {
        return this.mHeight;
    }

    public synchronized int getFpsMin() {
        return this.mFpsMin;
    }

    public synchronized int getFpsMax() {
        return this.mFpsMax;
    }

    public synchronized Size getSize() {
        return this.mSize;
    }

    public synchronized int getBatchSizeMax() {
        return this.mBatchSizeMax;
    }

    public synchronized Range<Integer> getFpsRange() {
        return this.mFpsRange;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HighSpeedVideoConfiguration)) {
            return false;
        }
        HighSpeedVideoConfiguration other = (HighSpeedVideoConfiguration) obj;
        if (this.mWidth != other.mWidth || this.mHeight != other.mHeight || this.mFpsMin != other.mFpsMin || this.mFpsMax != other.mFpsMax || this.mBatchSizeMax != other.mBatchSizeMax) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return HashCodeHelpers.hashCode(this.mWidth, this.mHeight, this.mFpsMin, this.mFpsMax);
    }
}
