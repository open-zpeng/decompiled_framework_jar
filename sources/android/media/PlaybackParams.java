package android.media;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public final class PlaybackParams implements Parcelable {
    public static final int AUDIO_FALLBACK_MODE_DEFAULT = 0;
    public static final int AUDIO_FALLBACK_MODE_FAIL = 2;
    public static final int AUDIO_FALLBACK_MODE_MUTE = 1;
    public static final int AUDIO_STRETCH_MODE_DEFAULT = 0;
    public static final int AUDIO_STRETCH_MODE_VOICE = 1;
    public static final Parcelable.Creator<PlaybackParams> CREATOR = new Parcelable.Creator<PlaybackParams>() { // from class: android.media.PlaybackParams.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PlaybackParams createFromParcel(Parcel in) {
            return new PlaybackParams(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PlaybackParams[] newArray(int size) {
            return new PlaybackParams[size];
        }
    };
    @UnsupportedAppUsage
    private static final int SET_AUDIO_FALLBACK_MODE = 4;
    @UnsupportedAppUsage
    private static final int SET_AUDIO_STRETCH_MODE = 8;
    @UnsupportedAppUsage
    private static final int SET_PITCH = 2;
    @UnsupportedAppUsage
    private static final int SET_SPEED = 1;
    @UnsupportedAppUsage
    private int mAudioFallbackMode;
    @UnsupportedAppUsage
    private int mAudioStretchMode;
    @UnsupportedAppUsage
    private float mPitch;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mSet;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private float mSpeed;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface AudioFallbackMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface AudioStretchMode {
    }

    public PlaybackParams() {
        this.mSet = 0;
        this.mAudioFallbackMode = 0;
        this.mAudioStretchMode = 0;
        this.mPitch = 1.0f;
        this.mSpeed = 1.0f;
    }

    private PlaybackParams(Parcel in) {
        this.mSet = 0;
        this.mAudioFallbackMode = 0;
        this.mAudioStretchMode = 0;
        this.mPitch = 1.0f;
        this.mSpeed = 1.0f;
        this.mSet = in.readInt();
        this.mAudioFallbackMode = in.readInt();
        this.mAudioStretchMode = in.readInt();
        this.mPitch = in.readFloat();
        if (this.mPitch < 0.0f) {
            this.mPitch = 0.0f;
        }
        this.mSpeed = in.readFloat();
    }

    public PlaybackParams allowDefaults() {
        this.mSet |= 15;
        return this;
    }

    public PlaybackParams setAudioFallbackMode(int audioFallbackMode) {
        this.mAudioFallbackMode = audioFallbackMode;
        this.mSet |= 4;
        return this;
    }

    public int getAudioFallbackMode() {
        if ((this.mSet & 4) == 0) {
            throw new IllegalStateException("audio fallback mode not set");
        }
        return this.mAudioFallbackMode;
    }

    public PlaybackParams setAudioStretchMode(int audioStretchMode) {
        this.mAudioStretchMode = audioStretchMode;
        this.mSet |= 8;
        return this;
    }

    public int getAudioStretchMode() {
        if ((this.mSet & 8) == 0) {
            throw new IllegalStateException("audio stretch mode not set");
        }
        return this.mAudioStretchMode;
    }

    public PlaybackParams setPitch(float pitch) {
        if (pitch < 0.0f) {
            throw new IllegalArgumentException("pitch must not be negative");
        }
        this.mPitch = pitch;
        this.mSet |= 2;
        return this;
    }

    public float getPitch() {
        if ((this.mSet & 2) == 0) {
            throw new IllegalStateException("pitch not set");
        }
        return this.mPitch;
    }

    public PlaybackParams setSpeed(float speed) {
        this.mSpeed = speed;
        this.mSet |= 1;
        return this;
    }

    public float getSpeed() {
        if ((this.mSet & 1) == 0) {
            throw new IllegalStateException("speed not set");
        }
        return this.mSpeed;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSet);
        dest.writeInt(this.mAudioFallbackMode);
        dest.writeInt(this.mAudioStretchMode);
        dest.writeFloat(this.mPitch);
        dest.writeFloat(this.mSpeed);
    }
}
