package android.media;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class BufferingParams implements Parcelable {
    private static final int BUFFERING_NO_MARK = -1;
    public static final Parcelable.Creator<BufferingParams> CREATOR = new Parcelable.Creator<BufferingParams>() { // from class: android.media.BufferingParams.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BufferingParams createFromParcel(Parcel in) {
            return new BufferingParams(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BufferingParams[] newArray(int size) {
            return new BufferingParams[size];
        }
    };
    private int mInitialMarkMs;
    private int mResumePlaybackMarkMs;

    private synchronized BufferingParams() {
        this.mInitialMarkMs = -1;
        this.mResumePlaybackMarkMs = -1;
    }

    public int getInitialMarkMs() {
        return this.mInitialMarkMs;
    }

    public int getResumePlaybackMarkMs() {
        return this.mResumePlaybackMarkMs;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private int mInitialMarkMs;
        private int mResumePlaybackMarkMs;

        public Builder() {
            this.mInitialMarkMs = -1;
            this.mResumePlaybackMarkMs = -1;
        }

        public Builder(BufferingParams bp) {
            this.mInitialMarkMs = -1;
            this.mResumePlaybackMarkMs = -1;
            this.mInitialMarkMs = bp.mInitialMarkMs;
            this.mResumePlaybackMarkMs = bp.mResumePlaybackMarkMs;
        }

        public BufferingParams build() {
            BufferingParams bp = new BufferingParams();
            bp.mInitialMarkMs = this.mInitialMarkMs;
            bp.mResumePlaybackMarkMs = this.mResumePlaybackMarkMs;
            return bp;
        }

        public Builder setInitialMarkMs(int markMs) {
            this.mInitialMarkMs = markMs;
            return this;
        }

        public Builder setResumePlaybackMarkMs(int markMs) {
            this.mResumePlaybackMarkMs = markMs;
            return this;
        }
    }

    private synchronized BufferingParams(Parcel in) {
        this.mInitialMarkMs = -1;
        this.mResumePlaybackMarkMs = -1;
        this.mInitialMarkMs = in.readInt();
        this.mResumePlaybackMarkMs = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mInitialMarkMs);
        dest.writeInt(this.mResumePlaybackMarkMs);
    }
}
