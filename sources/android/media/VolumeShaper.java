package android.media;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import com.xiaopeng.util.FeatureOption;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes2.dex */
public final class VolumeShaper implements AutoCloseable {
    private int mId;
    private final WeakReference<PlayerBase> mWeakPlayerBase;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VolumeShaper(Configuration configuration, PlayerBase playerBase) {
        this.mWeakPlayerBase = new WeakReference<>(playerBase);
        this.mId = applyPlayer(configuration, new Operation.Builder().defer().build());
    }

    int getId() {
        return this.mId;
    }

    public void apply(Operation operation) {
        applyPlayer(new Configuration(this.mId), operation);
    }

    public void replace(Configuration configuration, Operation operation, boolean join) {
        this.mId = applyPlayer(configuration, new Operation.Builder(operation).replace(this.mId, join).build());
    }

    public float getVolume() {
        return getStatePlayer(this.mId).getVolume();
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        try {
            applyPlayer(new Configuration(this.mId), new Operation.Builder().terminate().build());
        } catch (IllegalStateException e) {
        }
        WeakReference<PlayerBase> weakReference = this.mWeakPlayerBase;
        if (weakReference != null) {
            weakReference.clear();
        }
    }

    protected void finalize() {
        close();
    }

    private int applyPlayer(Configuration configuration, Operation operation) {
        WeakReference<PlayerBase> weakReference = this.mWeakPlayerBase;
        if (weakReference != null) {
            PlayerBase player = weakReference.get();
            if (player == null) {
                throw new IllegalStateException("player deallocated");
            }
            int id = player.playerApplyVolumeShaper(configuration, operation);
            if (id < 0) {
                if (id == -38) {
                    throw new IllegalStateException("player or VolumeShaper deallocated");
                }
                throw new IllegalArgumentException("invalid configuration or operation: " + id);
            }
            return id;
        }
        throw new IllegalStateException("uninitialized shaper");
    }

    private State getStatePlayer(int id) {
        WeakReference<PlayerBase> weakReference = this.mWeakPlayerBase;
        if (weakReference != null) {
            PlayerBase player = weakReference.get();
            if (player == null) {
                throw new IllegalStateException("player deallocated");
            }
            State state = player.playerGetVolumeShaperState(id);
            if (state == null) {
                throw new IllegalStateException("shaper cannot be found");
            }
            return state;
        }
        throw new IllegalStateException("uninitialized shaper");
    }

    /* loaded from: classes2.dex */
    public static final class Configuration implements Parcelable {
        public static final Parcelable.Creator<Configuration> CREATOR;
        public static final int INTERPOLATOR_TYPE_CUBIC = 2;
        public static final int INTERPOLATOR_TYPE_CUBIC_MONOTONIC = 3;
        public static final int INTERPOLATOR_TYPE_LINEAR = 1;
        public static final int INTERPOLATOR_TYPE_STEP = 0;
        private static final int MAXIMUM_CURVE_POINTS = 16;
        public static final int OPTION_FLAG_CLOCK_TIME = 2;
        private static final int OPTION_FLAG_PUBLIC_ALL = 3;
        public static final int OPTION_FLAG_VOLUME_IN_DBFS = 1;
        public static final Configuration SCURVE_RAMP;
        public static final Configuration SINE_RAMP;
        static final int TYPE_ID = 0;
        static final int TYPE_SCALE = 1;
        @UnsupportedAppUsage
        private final double mDurationMs;
        @UnsupportedAppUsage
        private final int mId;
        @UnsupportedAppUsage
        private final int mInterpolatorType;
        @UnsupportedAppUsage
        private final int mOptionFlags;
        @UnsupportedAppUsage
        private final float[] mTimes;
        @UnsupportedAppUsage
        private final int mType;
        @UnsupportedAppUsage
        private final float[] mVolumes;
        public static final Configuration LINEAR_RAMP = new Builder().setInterpolatorType(1).setCurve(new float[]{0.0f, 1.0f}, new float[]{0.0f, 1.0f}).setDuration(1000).build();
        public static final Configuration CUBIC_RAMP = new Builder().setInterpolatorType(2).setCurve(new float[]{0.0f, 1.0f}, new float[]{0.0f, 1.0f}).setDuration(1000).build();

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface InterpolatorType {
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface OptionFlag {
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface Type {
        }

        public static int getMaximumCurvePoints() {
            return 16;
        }

        static {
            float[] times = new float[16];
            float[] sines = new float[16];
            float[] scurve = new float[16];
            for (int i = 0; i < 16; i++) {
                times[i] = i / 15.0f;
                float sine = (float) Math.sin((times[i] * 3.141592653589793d) / 2.0d);
                sines[i] = sine;
                scurve[i] = sine * sine;
            }
            SINE_RAMP = new Builder().setInterpolatorType(2).setCurve(times, sines).setDuration(1000L).build();
            SCURVE_RAMP = new Builder().setInterpolatorType(2).setCurve(times, scurve).setDuration(1000L).build();
            CREATOR = new Parcelable.Creator<Configuration>() { // from class: android.media.VolumeShaper.Configuration.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // android.os.Parcelable.Creator
                public Configuration createFromParcel(Parcel p) {
                    int type = p.readInt();
                    int id = p.readInt();
                    if (type == 0) {
                        return new Configuration(id);
                    }
                    int optionFlags = p.readInt();
                    double durationMs = p.readDouble();
                    int interpolatorType = p.readInt();
                    p.readFloat();
                    p.readFloat();
                    int length = p.readInt();
                    float[] times2 = new float[length];
                    float[] volumes = new float[length];
                    for (int i2 = 0; i2 < length; i2++) {
                        times2[i2] = p.readFloat();
                        volumes[i2] = p.readFloat();
                    }
                    return new Configuration(type, id, optionFlags, durationMs, interpolatorType, times2, volumes);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // android.os.Parcelable.Creator
                public Configuration[] newArray(int size) {
                    return new Configuration[size];
                }
            };
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("VolumeShaper.Configuration{mType = ");
            sb.append(this.mType);
            sb.append(", mId = ");
            sb.append(this.mId);
            String str = "}";
            if (this.mType != 0) {
                str = ", mOptionFlags = 0x" + Integer.toHexString(this.mOptionFlags).toUpperCase() + ", mDurationMs = " + this.mDurationMs + ", mInterpolatorType = " + this.mInterpolatorType + ", mTimes[] = " + Arrays.toString(this.mTimes) + ", mVolumes[] = " + Arrays.toString(this.mVolumes) + "}";
            }
            sb.append(str);
            return sb.toString();
        }

        public int hashCode() {
            int i = this.mType;
            return i == 0 ? Objects.hash(Integer.valueOf(i), Integer.valueOf(this.mId)) : Objects.hash(Integer.valueOf(i), Integer.valueOf(this.mId), Integer.valueOf(this.mOptionFlags), Double.valueOf(this.mDurationMs), Integer.valueOf(this.mInterpolatorType), Integer.valueOf(Arrays.hashCode(this.mTimes)), Integer.valueOf(Arrays.hashCode(this.mVolumes)));
        }

        public boolean equals(Object o) {
            if (o instanceof Configuration) {
                if (o == this) {
                    return true;
                }
                Configuration other = (Configuration) o;
                int i = this.mType;
                if (i == other.mType && this.mId == other.mId) {
                    if (i == 0) {
                        return true;
                    }
                    if (this.mOptionFlags == other.mOptionFlags && this.mDurationMs == other.mDurationMs && this.mInterpolatorType == other.mInterpolatorType && Arrays.equals(this.mTimes, other.mTimes) && Arrays.equals(this.mVolumes, other.mVolumes)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mType);
            dest.writeInt(this.mId);
            if (this.mType != 0) {
                dest.writeInt(this.mOptionFlags);
                dest.writeDouble(this.mDurationMs);
                dest.writeInt(this.mInterpolatorType);
                dest.writeFloat(0.0f);
                dest.writeFloat(0.0f);
                dest.writeInt(this.mTimes.length);
                int i = 0;
                while (true) {
                    float[] fArr = this.mTimes;
                    if (i < fArr.length) {
                        dest.writeFloat(fArr[i]);
                        dest.writeFloat(this.mVolumes[i]);
                        i++;
                    } else {
                        return;
                    }
                }
            }
        }

        public Configuration(int id) {
            if (id < 0) {
                throw new IllegalArgumentException("negative id " + id);
            }
            this.mType = 0;
            this.mId = id;
            this.mInterpolatorType = 0;
            this.mOptionFlags = 0;
            this.mDurationMs = FeatureOption.FO_BOOT_POLICY_CPU;
            this.mTimes = null;
            this.mVolumes = null;
        }

        @UnsupportedAppUsage
        private Configuration(int type, int id, int optionFlags, double durationMs, int interpolatorType, float[] times, float[] volumes) {
            this.mType = type;
            this.mId = id;
            this.mOptionFlags = optionFlags;
            this.mDurationMs = durationMs;
            this.mInterpolatorType = interpolatorType;
            this.mTimes = times;
            this.mVolumes = volumes;
        }

        public int getType() {
            return this.mType;
        }

        public int getId() {
            return this.mId;
        }

        public int getInterpolatorType() {
            return this.mInterpolatorType;
        }

        public int getOptionFlags() {
            return this.mOptionFlags & 3;
        }

        int getAllOptionFlags() {
            return this.mOptionFlags;
        }

        public long getDuration() {
            return (long) this.mDurationMs;
        }

        public float[] getTimes() {
            return this.mTimes;
        }

        public float[] getVolumes() {
            return this.mVolumes;
        }

        private static String checkCurveForErrors(float[] times, float[] volumes, boolean log) {
            if (times == null) {
                return "times array must be non-null";
            }
            if (volumes == null) {
                return "volumes array must be non-null";
            }
            if (times.length != volumes.length) {
                return "array length must match";
            }
            if (times.length < 2) {
                return "array length must be at least 2";
            }
            if (times.length > 16) {
                return "array length must be no larger than 16";
            }
            if (times[0] != 0.0f) {
                return "times must start at 0.f";
            }
            if (times[times.length - 1] != 1.0f) {
                return "times must end at 1.f";
            }
            for (int i = 1; i < times.length; i++) {
                if (times[i] <= times[i - 1]) {
                    return "times not monotonic increasing, check index " + i;
                }
            }
            if (log) {
                for (int i2 = 0; i2 < volumes.length; i2++) {
                    if (volumes[i2] > 0.0f) {
                        return "volumes for log scale cannot be positive, check index " + i2;
                    }
                }
                return null;
            }
            for (int i3 = 0; i3 < volumes.length; i3++) {
                if (volumes[i3] < 0.0f || volumes[i3] > 1.0f) {
                    return "volumes for linear scale must be between 0.f and 1.f, check index " + i3;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void checkCurveForErrorsAndThrowException(float[] times, float[] volumes, boolean log, boolean ise) {
            String error = checkCurveForErrors(times, volumes, log);
            if (error != null) {
                if (ise) {
                    throw new IllegalStateException(error);
                }
                throw new IllegalArgumentException(error);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void checkValidVolumeAndThrowException(float volume, boolean log) {
            if (log) {
                if (volume > 0.0f) {
                    throw new IllegalArgumentException("dbfs volume must be 0.f or less");
                }
            } else if (volume < 0.0f || volume > 1.0f) {
                throw new IllegalArgumentException("volume must be >= 0.f and <= 1.f");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void clampVolume(float[] volumes, boolean log) {
            if (log) {
                for (int i = 0; i < volumes.length; i++) {
                    if (volumes[i] > 0.0f) {
                        volumes[i] = 0.0f;
                    }
                }
                return;
            }
            for (int i2 = 0; i2 < volumes.length; i2++) {
                if (volumes[i2] < 0.0f) {
                    volumes[i2] = 0.0f;
                } else if (volumes[i2] > 1.0f) {
                    volumes[i2] = 1.0f;
                }
            }
        }

        /* loaded from: classes2.dex */
        public static final class Builder {
            private double mDurationMs;
            private int mId;
            private int mInterpolatorType;
            private int mOptionFlags;
            private float[] mTimes;
            private int mType;
            private float[] mVolumes;

            public Builder() {
                this.mType = 1;
                this.mId = -1;
                this.mInterpolatorType = 2;
                this.mOptionFlags = 2;
                this.mDurationMs = 1000.0d;
                this.mTimes = null;
                this.mVolumes = null;
            }

            public Builder(Configuration configuration) {
                this.mType = 1;
                this.mId = -1;
                this.mInterpolatorType = 2;
                this.mOptionFlags = 2;
                this.mDurationMs = 1000.0d;
                this.mTimes = null;
                this.mVolumes = null;
                this.mType = configuration.getType();
                this.mId = configuration.getId();
                this.mOptionFlags = configuration.getAllOptionFlags();
                this.mInterpolatorType = configuration.getInterpolatorType();
                this.mDurationMs = configuration.getDuration();
                this.mTimes = (float[]) configuration.getTimes().clone();
                this.mVolumes = (float[]) configuration.getVolumes().clone();
            }

            public Builder setId(int id) {
                if (id < -1) {
                    throw new IllegalArgumentException("invalid id: " + id);
                }
                this.mId = id;
                return this;
            }

            public Builder setInterpolatorType(int interpolatorType) {
                if (interpolatorType == 0 || interpolatorType == 1 || interpolatorType == 2 || interpolatorType == 3) {
                    this.mInterpolatorType = interpolatorType;
                    return this;
                }
                throw new IllegalArgumentException("invalid interpolatorType: " + interpolatorType);
            }

            public Builder setOptionFlags(int optionFlags) {
                if ((optionFlags & (-4)) != 0) {
                    throw new IllegalArgumentException("invalid bits in flag: " + optionFlags);
                }
                this.mOptionFlags = (this.mOptionFlags & (-4)) | optionFlags;
                return this;
            }

            public Builder setDuration(long durationMillis) {
                if (durationMillis <= 0) {
                    throw new IllegalArgumentException("duration: " + durationMillis + " not positive");
                }
                this.mDurationMs = durationMillis;
                return this;
            }

            public Builder setCurve(float[] times, float[] volumes) {
                boolean log = (this.mOptionFlags & 1) != 0;
                Configuration.checkCurveForErrorsAndThrowException(times, volumes, log, false);
                this.mTimes = (float[]) times.clone();
                this.mVolumes = (float[]) volumes.clone();
                return this;
            }

            public Builder reflectTimes() {
                float[] fArr;
                boolean log = (this.mOptionFlags & 1) != 0;
                Configuration.checkCurveForErrorsAndThrowException(this.mTimes, this.mVolumes, log, true);
                int i = 0;
                while (true) {
                    fArr = this.mTimes;
                    if (i >= fArr.length / 2) {
                        break;
                    }
                    float temp = fArr[i];
                    fArr[i] = 1.0f - fArr[(fArr.length - 1) - i];
                    fArr[(fArr.length - 1) - i] = 1.0f - temp;
                    float[] fArr2 = this.mVolumes;
                    float temp2 = fArr2[i];
                    fArr2[i] = fArr2[(fArr2.length - 1) - i];
                    fArr2[(fArr2.length - 1) - i] = temp2;
                    i++;
                }
                if ((1 & fArr.length) != 0) {
                    fArr[i] = 1.0f - fArr[i];
                }
                return this;
            }

            public Builder invertVolumes() {
                boolean log = (this.mOptionFlags & 1) != 0;
                Configuration.checkCurveForErrorsAndThrowException(this.mTimes, this.mVolumes, log, true);
                float[] fArr = this.mVolumes;
                float min = fArr[0];
                float max = fArr[0];
                int i = 1;
                while (true) {
                    float[] fArr2 = this.mVolumes;
                    if (i >= fArr2.length) {
                        break;
                    }
                    if (fArr2[i] < min) {
                        min = fArr2[i];
                    } else if (fArr2[i] > max) {
                        max = fArr2[i];
                    }
                    i++;
                }
                float maxmin = max + min;
                int i2 = 0;
                while (true) {
                    float[] fArr3 = this.mVolumes;
                    if (i2 < fArr3.length) {
                        fArr3[i2] = maxmin - fArr3[i2];
                        i2++;
                    } else {
                        return this;
                    }
                }
            }

            public Builder scaleToEndVolume(float volume) {
                boolean log = (this.mOptionFlags & 1) != 0;
                Configuration.checkCurveForErrorsAndThrowException(this.mTimes, this.mVolumes, log, true);
                Configuration.checkValidVolumeAndThrowException(volume, log);
                float[] fArr = this.mVolumes;
                float startVolume = fArr[0];
                float endVolume = fArr[fArr.length - 1];
                if (endVolume == startVolume) {
                    float offset = volume - startVolume;
                    int i = 0;
                    while (true) {
                        float[] fArr2 = this.mVolumes;
                        if (i >= fArr2.length) {
                            break;
                        }
                        fArr2[i] = fArr2[i] + (this.mTimes[i] * offset);
                        i++;
                    }
                } else {
                    float scale = (volume - startVolume) / (endVolume - startVolume);
                    int i2 = 0;
                    while (true) {
                        float[] fArr3 = this.mVolumes;
                        if (i2 >= fArr3.length) {
                            break;
                        }
                        fArr3[i2] = ((fArr3[i2] - startVolume) * scale) + startVolume;
                        i2++;
                    }
                }
                Configuration.clampVolume(this.mVolumes, log);
                return this;
            }

            public Builder scaleToStartVolume(float volume) {
                boolean log = (this.mOptionFlags & 1) != 0;
                Configuration.checkCurveForErrorsAndThrowException(this.mTimes, this.mVolumes, log, true);
                Configuration.checkValidVolumeAndThrowException(volume, log);
                float[] fArr = this.mVolumes;
                float startVolume = fArr[0];
                float endVolume = fArr[fArr.length - 1];
                if (endVolume == startVolume) {
                    float offset = volume - startVolume;
                    int i = 0;
                    while (true) {
                        float[] fArr2 = this.mVolumes;
                        if (i >= fArr2.length) {
                            break;
                        }
                        fArr2[i] = fArr2[i] + ((1.0f - this.mTimes[i]) * offset);
                        i++;
                    }
                } else {
                    float scale = (volume - endVolume) / (startVolume - endVolume);
                    int i2 = 0;
                    while (true) {
                        float[] fArr3 = this.mVolumes;
                        if (i2 >= fArr3.length) {
                            break;
                        }
                        fArr3[i2] = ((fArr3[i2] - endVolume) * scale) + endVolume;
                        i2++;
                    }
                }
                Configuration.clampVolume(this.mVolumes, log);
                return this;
            }

            public Configuration build() {
                boolean log = (this.mOptionFlags & 1) != 0;
                Configuration.checkCurveForErrorsAndThrowException(this.mTimes, this.mVolumes, log, true);
                return new Configuration(this.mType, this.mId, this.mOptionFlags, this.mDurationMs, this.mInterpolatorType, this.mTimes, this.mVolumes);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class Operation implements Parcelable {
        private static final int FLAG_CREATE_IF_NEEDED = 16;
        private static final int FLAG_DEFER = 8;
        private static final int FLAG_JOIN = 4;
        private static final int FLAG_NONE = 0;
        private static final int FLAG_PUBLIC_ALL = 3;
        private static final int FLAG_REVERSE = 1;
        private static final int FLAG_TERMINATE = 2;
        @UnsupportedAppUsage
        private final int mFlags;
        @UnsupportedAppUsage
        private final int mReplaceId;
        @UnsupportedAppUsage
        private final float mXOffset;
        public static final Operation PLAY = new Builder().build();
        public static final Operation REVERSE = new Builder().reverse().build();
        public static final Parcelable.Creator<Operation> CREATOR = new Parcelable.Creator<Operation>() { // from class: android.media.VolumeShaper.Operation.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Operation createFromParcel(Parcel p) {
                int flags = p.readInt();
                int replaceId = p.readInt();
                float xOffset = p.readFloat();
                return new Operation(flags, replaceId, xOffset);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Operation[] newArray(int size) {
                return new Operation[size];
            }
        };

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface Flag {
        }

        public String toString() {
            return "VolumeShaper.Operation{mFlags = 0x" + Integer.toHexString(this.mFlags).toUpperCase() + ", mReplaceId = " + this.mReplaceId + ", mXOffset = " + this.mXOffset + "}";
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mFlags), Integer.valueOf(this.mReplaceId), Float.valueOf(this.mXOffset));
        }

        public boolean equals(Object o) {
            if (o instanceof Operation) {
                if (o == this) {
                    return true;
                }
                Operation other = (Operation) o;
                return this.mFlags == other.mFlags && this.mReplaceId == other.mReplaceId && Float.compare(this.mXOffset, other.mXOffset) == 0;
            }
            return false;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mFlags);
            dest.writeInt(this.mReplaceId);
            dest.writeFloat(this.mXOffset);
        }

        @UnsupportedAppUsage
        private Operation(int flags, int replaceId, float xOffset) {
            this.mFlags = flags;
            this.mReplaceId = replaceId;
            this.mXOffset = xOffset;
        }

        /* loaded from: classes2.dex */
        public static final class Builder {
            int mFlags;
            int mReplaceId;
            float mXOffset;

            public Builder() {
                this.mFlags = 0;
                this.mReplaceId = -1;
                this.mXOffset = Float.NaN;
            }

            public Builder(Operation operation) {
                this.mReplaceId = operation.mReplaceId;
                this.mFlags = operation.mFlags;
                this.mXOffset = operation.mXOffset;
            }

            public Builder replace(int id, boolean join) {
                this.mReplaceId = id;
                if (join) {
                    this.mFlags |= 4;
                } else {
                    this.mFlags &= -5;
                }
                return this;
            }

            public Builder defer() {
                this.mFlags |= 8;
                return this;
            }

            public Builder terminate() {
                this.mFlags |= 2;
                return this;
            }

            public Builder reverse() {
                this.mFlags ^= 1;
                return this;
            }

            public Builder createIfNeeded() {
                this.mFlags |= 16;
                return this;
            }

            public Builder setXOffset(float xOffset) {
                if (xOffset < -0.0f) {
                    throw new IllegalArgumentException("Negative xOffset not allowed");
                }
                if (xOffset > 1.0f) {
                    throw new IllegalArgumentException("xOffset > 1.f not allowed");
                }
                this.mXOffset = xOffset;
                return this;
            }

            private Builder setFlags(int flags) {
                if ((flags & (-4)) != 0) {
                    throw new IllegalArgumentException("flag has unknown bits set: " + flags);
                }
                this.mFlags = (this.mFlags & (-4)) | flags;
                return this;
            }

            public Operation build() {
                return new Operation(this.mFlags, this.mReplaceId, this.mXOffset);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class State implements Parcelable {
        public static final Parcelable.Creator<State> CREATOR = new Parcelable.Creator<State>() { // from class: android.media.VolumeShaper.State.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public State createFromParcel(Parcel p) {
                return new State(p.readFloat(), p.readFloat());
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public State[] newArray(int size) {
                return new State[size];
            }
        };
        @UnsupportedAppUsage
        private float mVolume;
        @UnsupportedAppUsage
        private float mXOffset;

        public String toString() {
            return "VolumeShaper.State{mVolume = " + this.mVolume + ", mXOffset = " + this.mXOffset + "}";
        }

        public int hashCode() {
            return Objects.hash(Float.valueOf(this.mVolume), Float.valueOf(this.mXOffset));
        }

        public boolean equals(Object o) {
            if (o instanceof State) {
                if (o == this) {
                    return true;
                }
                State other = (State) o;
                return this.mVolume == other.mVolume && this.mXOffset == other.mXOffset;
            }
            return false;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(this.mVolume);
            dest.writeFloat(this.mXOffset);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @UnsupportedAppUsage
        public State(float volume, float xOffset) {
            this.mVolume = volume;
            this.mXOffset = xOffset;
        }

        public float getVolume() {
            return this.mVolume;
        }

        public float getXOffset() {
            return this.mXOffset;
        }
    }
}
