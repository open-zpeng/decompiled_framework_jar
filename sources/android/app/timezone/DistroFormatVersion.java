package android.app.timezone;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class DistroFormatVersion implements Parcelable {
    private protected static final Parcelable.Creator<DistroFormatVersion> CREATOR = new Parcelable.Creator<DistroFormatVersion>() { // from class: android.app.timezone.DistroFormatVersion.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DistroFormatVersion createFromParcel(Parcel in) {
            int majorVersion = in.readInt();
            int minorVersion = in.readInt();
            return new DistroFormatVersion(majorVersion, minorVersion);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DistroFormatVersion[] newArray(int size) {
            return new DistroFormatVersion[size];
        }
    };
    public protected final int mMajorVersion;
    public protected final int mMinorVersion;

    private protected synchronized DistroFormatVersion(int majorVersion, int minorVersion) {
        this.mMajorVersion = Utils.validateVersion("major", majorVersion);
        this.mMinorVersion = Utils.validateVersion("minor", minorVersion);
    }

    private protected synchronized int getMajorVersion() {
        return this.mMajorVersion;
    }

    private protected synchronized int getMinorVersion() {
        return this.mMinorVersion;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mMajorVersion);
        out.writeInt(this.mMinorVersion);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean supports(DistroFormatVersion distroFormatVersion) {
        return this.mMajorVersion == distroFormatVersion.mMajorVersion && this.mMinorVersion <= distroFormatVersion.mMinorVersion;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DistroFormatVersion that = (DistroFormatVersion) o;
        if (this.mMajorVersion == that.mMajorVersion && this.mMinorVersion == that.mMinorVersion) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.mMajorVersion;
        return (31 * result) + this.mMinorVersion;
    }

    public String toString() {
        return "DistroFormatVersion{mMajorVersion=" + this.mMajorVersion + ", mMinorVersion=" + this.mMinorVersion + '}';
    }
}
