package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class ReprocessFormatsMap {
    private final int[] mEntry;
    private final int mInputCount;

    public synchronized ReprocessFormatsMap(int[] entry) {
        Preconditions.checkNotNull(entry, "entry must not be null");
        int left = entry.length;
        int numInputs = 0;
        int i = 0;
        while (i < entry.length) {
            int inputFormat = StreamConfigurationMap.checkArgumentFormatInternal(entry[i]);
            int left2 = left - 1;
            int i2 = i + 1;
            if (left2 < 1) {
                throw new IllegalArgumentException(String.format("Input %x had no output format length listed", Integer.valueOf(inputFormat)));
            }
            int length = entry[i2];
            left = left2 - 1;
            i = i2 + 1;
            for (int j = 0; j < length; j++) {
                int outputFormat = entry[i + j];
                StreamConfigurationMap.checkArgumentFormatInternal(outputFormat);
            }
            if (length > 0) {
                if (left < length) {
                    throw new IllegalArgumentException(String.format("Input %x had too few output formats listed (actual: %d, expected: %d)", Integer.valueOf(inputFormat), Integer.valueOf(left), Integer.valueOf(length)));
                }
                i += length;
                left -= length;
            }
            numInputs++;
        }
        this.mEntry = entry;
        this.mInputCount = numInputs;
    }

    public synchronized int[] getInputs() {
        int[] inputs = new int[this.mInputCount];
        int left = this.mEntry.length;
        int i = 0;
        int left2 = left;
        int left3 = 0;
        while (i < this.mEntry.length) {
            int format = this.mEntry[i];
            int left4 = left2 - 1;
            int i2 = i + 1;
            if (left4 < 1) {
                throw new AssertionError(String.format("Input %x had no output format length listed", Integer.valueOf(format)));
            }
            int length = this.mEntry[i2];
            left2 = left4 - 1;
            i = i2 + 1;
            if (length > 0) {
                if (left2 < length) {
                    throw new AssertionError(String.format("Input %x had too few output formats listed (actual: %d, expected: %d)", Integer.valueOf(format), Integer.valueOf(left2), Integer.valueOf(length)));
                }
                i += length;
                left2 -= length;
            }
            inputs[left3] = format;
            left3++;
        }
        return StreamConfigurationMap.imageFormatToPublic(inputs);
    }

    public synchronized int[] getOutputs(int format) {
        int left = this.mEntry.length;
        int left2 = left;
        int left3 = 0;
        while (left3 < this.mEntry.length) {
            int inputFormat = this.mEntry[left3];
            int left4 = left2 - 1;
            int i = left3 + 1;
            if (left4 < 1) {
                throw new AssertionError(String.format("Input %x had no output format length listed", Integer.valueOf(format)));
            }
            int length = this.mEntry[i];
            int left5 = left4 - 1;
            int i2 = i + 1;
            if (length > 0 && left5 < length) {
                throw new AssertionError(String.format("Input %x had too few output formats listed (actual: %d, expected: %d)", Integer.valueOf(format), Integer.valueOf(left5), Integer.valueOf(length)));
            }
            if (inputFormat == format) {
                int[] outputs = new int[length];
                for (int k = 0; k < length; k++) {
                    outputs[k] = this.mEntry[i2 + k];
                }
                return StreamConfigurationMap.imageFormatToPublic(outputs);
            }
            left3 = i2 + length;
            left2 = left5 - length;
        }
        throw new IllegalArgumentException(String.format("Input format %x was not one in #getInputs", Integer.valueOf(format)));
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ReprocessFormatsMap)) {
            return false;
        }
        ReprocessFormatsMap other = (ReprocessFormatsMap) obj;
        return Arrays.equals(this.mEntry, other.mEntry);
    }

    public int hashCode() {
        return HashCodeHelpers.hashCode(this.mEntry);
    }
}
