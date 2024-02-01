package android.hardware.display;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import java.util.Objects;
@SystemApi
/* loaded from: classes.dex */
public final class BrightnessConfiguration implements Parcelable {
    public static final Parcelable.Creator<BrightnessConfiguration> CREATOR = new Parcelable.Creator<BrightnessConfiguration>() { // from class: android.hardware.display.BrightnessConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BrightnessConfiguration createFromParcel(Parcel in) {
            float[] lux = in.createFloatArray();
            float[] nits = in.createFloatArray();
            Builder builder = new Builder(lux, nits);
            builder.setDescription(in.readString());
            return builder.build();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BrightnessConfiguration[] newArray(int size) {
            return new BrightnessConfiguration[size];
        }
    };
    private final String mDescription;
    private final float[] mLux;
    private final float[] mNits;

    private synchronized BrightnessConfiguration(float[] lux, float[] nits, String description) {
        this.mLux = lux;
        this.mNits = nits;
        this.mDescription = description;
    }

    public Pair<float[], float[]> getCurve() {
        return Pair.create(Arrays.copyOf(this.mLux, this.mLux.length), Arrays.copyOf(this.mNits, this.mNits.length));
    }

    public synchronized String getDescription() {
        return this.mDescription;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloatArray(this.mLux);
        dest.writeFloatArray(this.mNits);
        dest.writeString(this.mDescription);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("BrightnessConfiguration{[");
        int size = this.mLux.length;
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("(");
            sb.append(this.mLux[i]);
            sb.append(", ");
            sb.append(this.mNits[i]);
            sb.append(")");
        }
        sb.append("], '");
        if (this.mDescription != null) {
            sb.append(this.mDescription);
        }
        sb.append("'}");
        return sb.toString();
    }

    public int hashCode() {
        int result = (((1 * 31) + Arrays.hashCode(this.mLux)) * 31) + Arrays.hashCode(this.mNits);
        if (this.mDescription != null) {
            return (result * 31) + this.mDescription.hashCode();
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BrightnessConfiguration) {
            BrightnessConfiguration other = (BrightnessConfiguration) o;
            return Arrays.equals(this.mLux, other.mLux) && Arrays.equals(this.mNits, other.mNits) && Objects.equals(this.mDescription, other.mDescription);
        }
        return false;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private float[] mCurveLux;
        private float[] mCurveNits;
        private String mDescription;

        public synchronized Builder() {
        }

        public Builder(float[] lux, float[] nits) {
            setCurve(lux, nits);
        }

        public synchronized Builder setCurve(float[] lux, float[] nits) {
            Preconditions.checkNotNull(lux);
            Preconditions.checkNotNull(nits);
            if (lux.length == 0 || nits.length == 0) {
                throw new IllegalArgumentException("Lux and nits arrays must not be empty");
            }
            if (lux.length != nits.length) {
                throw new IllegalArgumentException("Lux and nits arrays must be the same length");
            }
            if (lux[0] != 0.0f) {
                throw new IllegalArgumentException("Initial control point must be for 0 lux");
            }
            Preconditions.checkArrayElementsInRange(lux, 0.0f, Float.MAX_VALUE, "lux");
            Preconditions.checkArrayElementsInRange(nits, 0.0f, Float.MAX_VALUE, "nits");
            checkMonotonic(lux, true, "lux");
            checkMonotonic(nits, false, "nits");
            this.mCurveLux = lux;
            this.mCurveNits = nits;
            return this;
        }

        public Builder setDescription(String description) {
            this.mDescription = description;
            return this;
        }

        public BrightnessConfiguration build() {
            if (this.mCurveLux == null || this.mCurveNits == null) {
                throw new IllegalStateException("A curve must be set!");
            }
            return new BrightnessConfiguration(this.mCurveLux, this.mCurveNits, this.mDescription);
        }

        private static synchronized void checkMonotonic(float[] vals, boolean strictlyIncreasing, String name) {
            if (vals.length <= 1) {
                return;
            }
            float prev = vals[0];
            for (int i = 1; i < vals.length; i++) {
                if (prev > vals[i] || (prev == vals[i] && strictlyIncreasing)) {
                    String condition = strictlyIncreasing ? "strictly increasing" : "monotonic";
                    throw new IllegalArgumentException(name + " values must be " + condition);
                }
                prev = vals[i];
            }
        }
    }
}
