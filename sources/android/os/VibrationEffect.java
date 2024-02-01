package android.os;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.util.MathUtils;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Arrays;
/* loaded from: classes2.dex */
public abstract class VibrationEffect implements Parcelable {
    public static final int DEFAULT_AMPLITUDE = -1;
    public static final int EFFECT_CLICK = 0;
    public static final int EFFECT_DOUBLE_CLICK = 1;
    public static final int EFFECT_HEAVY_CLICK = 5;
    public static final int EFFECT_POP = 4;
    public static final int EFFECT_THUD = 3;
    public static final int EFFECT_TICK = 2;
    public static final int MAX_AMPLITUDE = 255;
    private static final int PARCEL_TOKEN_EFFECT = 3;
    private static final int PARCEL_TOKEN_ONE_SHOT = 1;
    private static final int PARCEL_TOKEN_WAVEFORM = 2;
    @VisibleForTesting
    public static final int[] RINGTONES = {6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
    public static final Parcelable.Creator<VibrationEffect> CREATOR = new Parcelable.Creator<VibrationEffect>() { // from class: android.os.VibrationEffect.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VibrationEffect createFromParcel(Parcel in) {
            int token = in.readInt();
            if (token == 1) {
                return new OneShot(in);
            }
            if (token == 2) {
                return new Waveform(in);
            }
            if (token == 3) {
                return new Prebaked(in);
            }
            throw new IllegalStateException("Unexpected vibration event type token in parcel.");
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VibrationEffect[] newArray(int size) {
            return new VibrationEffect[size];
        }
    };

    public abstract synchronized long getDuration();

    public abstract synchronized void validate();

    public static VibrationEffect createOneShot(long milliseconds, int amplitude) {
        VibrationEffect effect = new OneShot(milliseconds, amplitude);
        effect.validate();
        return effect;
    }

    public static VibrationEffect createWaveform(long[] timings, int repeat) {
        int[] amplitudes = new int[timings.length];
        for (int i = 0; i < timings.length / 2; i++) {
            amplitudes[(i * 2) + 1] = -1;
        }
        return createWaveform(timings, amplitudes, repeat);
    }

    public static VibrationEffect createWaveform(long[] timings, int[] amplitudes, int repeat) {
        VibrationEffect effect = new Waveform(timings, amplitudes, repeat);
        effect.validate();
        return effect;
    }

    public static synchronized VibrationEffect get(int effectId) {
        return get(effectId, true);
    }

    public static synchronized VibrationEffect get(int effectId, boolean fallback) {
        VibrationEffect effect = new Prebaked(effectId, fallback);
        effect.validate();
        return effect;
    }

    public static synchronized VibrationEffect get(Uri uri, Context context) {
        String[] uris = context.getResources().getStringArray(R.array.config_ringtoneEffectUris);
        for (int i = 0; i < uris.length && i < RINGTONES.length; i++) {
            if (uris[i] != null) {
                ContentResolver cr = context.getContentResolver();
                Uri mappedUri = cr.uncanonicalize(Uri.parse(uris[i]));
                if (mappedUri != null && mappedUri.equals(uri)) {
                    return get(RINGTONES[i]);
                }
            }
        }
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected static synchronized int scale(int amplitude, float gamma, int maxAmplitude) {
        float val = MathUtils.pow(amplitude / 255.0f, gamma);
        return (int) (maxAmplitude * val);
    }

    /* loaded from: classes2.dex */
    public static class OneShot extends VibrationEffect implements Parcelable {
        public static final Parcelable.Creator<OneShot> CREATOR = new Parcelable.Creator<OneShot>() { // from class: android.os.VibrationEffect.OneShot.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public OneShot createFromParcel(Parcel in) {
                in.readInt();
                return new OneShot(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public OneShot[] newArray(int size) {
                return new OneShot[size];
            }
        };
        private final int mAmplitude;
        private final long mDuration;

        public synchronized OneShot(Parcel in) {
            this.mDuration = in.readLong();
            this.mAmplitude = in.readInt();
        }

        public synchronized OneShot(long milliseconds, int amplitude) {
            this.mDuration = milliseconds;
            this.mAmplitude = amplitude;
        }

        @Override // android.os.VibrationEffect
        public synchronized long getDuration() {
            return this.mDuration;
        }

        public synchronized int getAmplitude() {
            return this.mAmplitude;
        }

        public synchronized VibrationEffect scale(float gamma, int maxAmplitude) {
            int newAmplitude = scale(this.mAmplitude, gamma, maxAmplitude);
            return new OneShot(this.mDuration, newAmplitude);
        }

        public synchronized OneShot resolve(int defaultAmplitude) {
            if (defaultAmplitude > 255 || defaultAmplitude < 0) {
                throw new IllegalArgumentException("Amplitude is negative or greater than MAX_AMPLITUDE");
            }
            if (this.mAmplitude == -1) {
                return new OneShot(this.mDuration, defaultAmplitude);
            }
            return this;
        }

        @Override // android.os.VibrationEffect
        public synchronized void validate() {
            if (this.mAmplitude < -1 || this.mAmplitude == 0 || this.mAmplitude > 255) {
                throw new IllegalArgumentException("amplitude must either be DEFAULT_AMPLITUDE, or between 1 and 255 inclusive (amplitude=" + this.mAmplitude + ")");
            } else if (this.mDuration <= 0) {
                throw new IllegalArgumentException("duration must be positive (duration=" + this.mDuration + ")");
            }
        }

        public boolean equals(Object o) {
            if (o instanceof OneShot) {
                OneShot other = (OneShot) o;
                return other.mDuration == this.mDuration && other.mAmplitude == this.mAmplitude;
            }
            return false;
        }

        public int hashCode() {
            int result = 17 + (((int) this.mDuration) * 37);
            return result + (37 * this.mAmplitude);
        }

        public String toString() {
            return "OneShot{mDuration=" + this.mDuration + ", mAmplitude=" + this.mAmplitude + "}";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(1);
            out.writeLong(this.mDuration);
            out.writeInt(this.mAmplitude);
        }
    }

    /* loaded from: classes2.dex */
    public static class Waveform extends VibrationEffect implements Parcelable {
        public static final Parcelable.Creator<Waveform> CREATOR = new Parcelable.Creator<Waveform>() { // from class: android.os.VibrationEffect.Waveform.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Waveform createFromParcel(Parcel in) {
                in.readInt();
                return new Waveform(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Waveform[] newArray(int size) {
                return new Waveform[size];
            }
        };
        private final int[] mAmplitudes;
        private final int mRepeat;
        private final long[] mTimings;

        public synchronized Waveform(Parcel in) {
            this(in.createLongArray(), in.createIntArray(), in.readInt());
        }

        public synchronized Waveform(long[] timings, int[] amplitudes, int repeat) {
            this.mTimings = new long[timings.length];
            System.arraycopy(timings, 0, this.mTimings, 0, timings.length);
            this.mAmplitudes = new int[amplitudes.length];
            System.arraycopy(amplitudes, 0, this.mAmplitudes, 0, amplitudes.length);
            this.mRepeat = repeat;
        }

        public synchronized long[] getTimings() {
            return this.mTimings;
        }

        public synchronized int[] getAmplitudes() {
            return this.mAmplitudes;
        }

        public synchronized int getRepeatIndex() {
            return this.mRepeat;
        }

        @Override // android.os.VibrationEffect
        public synchronized long getDuration() {
            long[] jArr;
            if (this.mRepeat >= 0) {
                return Long.MAX_VALUE;
            }
            long duration = 0;
            for (long d : this.mTimings) {
                duration += d;
            }
            return duration;
        }

        public synchronized VibrationEffect scale(float gamma, int maxAmplitude) {
            if (gamma == 1.0f && maxAmplitude == 255) {
                return new Waveform(this.mTimings, this.mAmplitudes, this.mRepeat);
            }
            int[] scaledAmplitudes = Arrays.copyOf(this.mAmplitudes, this.mAmplitudes.length);
            for (int i = 0; i < scaledAmplitudes.length; i++) {
                scaledAmplitudes[i] = scale(scaledAmplitudes[i], gamma, maxAmplitude);
            }
            return new Waveform(this.mTimings, scaledAmplitudes, this.mRepeat);
        }

        public synchronized Waveform resolve(int defaultAmplitude) {
            if (defaultAmplitude > 255 || defaultAmplitude < 0) {
                throw new IllegalArgumentException("Amplitude is negative or greater than MAX_AMPLITUDE");
            }
            int[] resolvedAmplitudes = Arrays.copyOf(this.mAmplitudes, this.mAmplitudes.length);
            for (int i = 0; i < resolvedAmplitudes.length; i++) {
                if (resolvedAmplitudes[i] == -1) {
                    resolvedAmplitudes[i] = defaultAmplitude;
                }
            }
            return new Waveform(this.mTimings, resolvedAmplitudes, this.mRepeat);
        }

        @Override // android.os.VibrationEffect
        public synchronized void validate() {
            long[] jArr;
            int[] iArr;
            if (this.mTimings.length != this.mAmplitudes.length) {
                throw new IllegalArgumentException("timing and amplitude arrays must be of equal length (timings.length=" + this.mTimings.length + ", amplitudes.length=" + this.mAmplitudes.length + ")");
            } else if (!hasNonZeroEntry(this.mTimings)) {
                throw new IllegalArgumentException("at least one timing must be non-zero (timings=" + Arrays.toString(this.mTimings) + ")");
            } else {
                for (long timing : this.mTimings) {
                    if (timing < 0) {
                        throw new IllegalArgumentException("timings must all be >= 0 (timings=" + Arrays.toString(this.mTimings) + ")");
                    }
                }
                for (int amplitude : this.mAmplitudes) {
                    if (amplitude < -1 || amplitude > 255) {
                        throw new IllegalArgumentException("amplitudes must all be DEFAULT_AMPLITUDE or between 0 and 255 (amplitudes=" + Arrays.toString(this.mAmplitudes) + ")");
                    }
                }
                if (this.mRepeat < -1 || this.mRepeat >= this.mTimings.length) {
                    throw new IllegalArgumentException("repeat index must be within the bounds of the timings array (timings.length=" + this.mTimings.length + ", index=" + this.mRepeat + ")");
                }
            }
        }

        public boolean equals(Object o) {
            if (o instanceof Waveform) {
                Waveform other = (Waveform) o;
                return Arrays.equals(this.mTimings, other.mTimings) && Arrays.equals(this.mAmplitudes, other.mAmplitudes) && this.mRepeat == other.mRepeat;
            }
            return false;
        }

        public int hashCode() {
            int result = 17 + (Arrays.hashCode(this.mTimings) * 37);
            return result + (Arrays.hashCode(this.mAmplitudes) * 37) + (37 * this.mRepeat);
        }

        public String toString() {
            return "Waveform{mTimings=" + Arrays.toString(this.mTimings) + ", mAmplitudes=" + Arrays.toString(this.mAmplitudes) + ", mRepeat=" + this.mRepeat + "}";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(2);
            out.writeLongArray(this.mTimings);
            out.writeIntArray(this.mAmplitudes);
            out.writeInt(this.mRepeat);
        }

        private static synchronized boolean hasNonZeroEntry(long[] vals) {
            for (long val : vals) {
                if (val != 0) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public static class Prebaked extends VibrationEffect implements Parcelable {
        public static final Parcelable.Creator<Prebaked> CREATOR = new Parcelable.Creator<Prebaked>() { // from class: android.os.VibrationEffect.Prebaked.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Prebaked createFromParcel(Parcel in) {
                in.readInt();
                return new Prebaked(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Prebaked[] newArray(int size) {
                return new Prebaked[size];
            }
        };
        private final int mEffectId;
        private int mEffectStrength;
        private final boolean mFallback;

        public synchronized Prebaked(Parcel in) {
            this(in.readInt(), in.readByte() != 0);
            this.mEffectStrength = in.readInt();
        }

        public synchronized Prebaked(int effectId, boolean fallback) {
            this.mEffectId = effectId;
            this.mFallback = fallback;
            this.mEffectStrength = 1;
        }

        public synchronized int getId() {
            return this.mEffectId;
        }

        public synchronized boolean shouldFallback() {
            return this.mFallback;
        }

        @Override // android.os.VibrationEffect
        public synchronized long getDuration() {
            return -1L;
        }

        public synchronized void setEffectStrength(int strength) {
            if (!isValidEffectStrength(strength)) {
                throw new IllegalArgumentException("Invalid effect strength: " + strength);
            }
            this.mEffectStrength = strength;
        }

        public synchronized int getEffectStrength() {
            return this.mEffectStrength;
        }

        private static synchronized boolean isValidEffectStrength(int strength) {
            switch (strength) {
                case 0:
                case 1:
                case 2:
                    return true;
                default:
                    return false;
            }
        }

        @Override // android.os.VibrationEffect
        public synchronized void validate() {
            switch (this.mEffectId) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    break;
                default:
                    if (this.mEffectId < RINGTONES[0] || this.mEffectId > RINGTONES[RINGTONES.length - 1]) {
                        throw new IllegalArgumentException("Unknown prebaked effect type (value=" + this.mEffectId + ")");
                    }
                    break;
            }
            if (!isValidEffectStrength(this.mEffectStrength)) {
                throw new IllegalArgumentException("Unknown prebaked effect strength (value=" + this.mEffectStrength + ")");
            }
        }

        public boolean equals(Object o) {
            if (o instanceof Prebaked) {
                Prebaked other = (Prebaked) o;
                return this.mEffectId == other.mEffectId && this.mFallback == other.mFallback && this.mEffectStrength == other.mEffectStrength;
            }
            return false;
        }

        public int hashCode() {
            int result = 17 + (this.mEffectId * 37);
            return result + (37 * this.mEffectStrength);
        }

        public String toString() {
            return "Prebaked{mEffectId=" + this.mEffectId + ", mEffectStrength=" + this.mEffectStrength + ", mFallback=" + this.mFallback + "}";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(3);
            out.writeInt(this.mEffectId);
            out.writeByte(this.mFallback ? (byte) 1 : (byte) 0);
            out.writeInt(this.mEffectStrength);
        }
    }
}
