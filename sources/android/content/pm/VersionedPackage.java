package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public final class VersionedPackage implements Parcelable {
    public static final Parcelable.Creator<VersionedPackage> CREATOR = new Parcelable.Creator<VersionedPackage>() { // from class: android.content.pm.VersionedPackage.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VersionedPackage createFromParcel(Parcel source) {
            return new VersionedPackage(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VersionedPackage[] newArray(int size) {
            return new VersionedPackage[size];
        }
    };
    private final String mPackageName;
    private final long mVersionCode;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface VersionCode {
    }

    public VersionedPackage(String packageName, int versionCode) {
        this.mPackageName = packageName;
        this.mVersionCode = versionCode;
    }

    public VersionedPackage(String packageName, long versionCode) {
        this.mPackageName = packageName;
        this.mVersionCode = versionCode;
    }

    private VersionedPackage(Parcel parcel) {
        this.mPackageName = parcel.readString();
        this.mVersionCode = parcel.readLong();
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    @Deprecated
    public int getVersionCode() {
        return (int) (this.mVersionCode & 2147483647L);
    }

    public long getLongVersionCode() {
        return this.mVersionCode;
    }

    public String toString() {
        return "VersionedPackage[" + this.mPackageName + "/" + this.mVersionCode + "]";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mPackageName);
        parcel.writeLong(this.mVersionCode);
    }
}
