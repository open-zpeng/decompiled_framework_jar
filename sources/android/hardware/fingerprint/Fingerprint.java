package android.hardware.fingerprint;

import android.hardware.biometrics.BiometricAuthenticator;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class Fingerprint extends BiometricAuthenticator.BiometricIdentifier {
    public static final Parcelable.Creator<Fingerprint> CREATOR = new Parcelable.Creator<Fingerprint>() { // from class: android.hardware.fingerprint.Fingerprint.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Fingerprint createFromParcel(Parcel in) {
            return new Fingerprint(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Fingerprint[] newArray(int size) {
            return new Fingerprint[size];
        }
    };
    private long mDeviceId;
    private int mFingerId;
    private int mGroupId;
    private CharSequence mName;

    public synchronized Fingerprint(CharSequence name, int groupId, int fingerId, long deviceId) {
        this.mName = name;
        this.mGroupId = groupId;
        this.mFingerId = fingerId;
        this.mDeviceId = deviceId;
    }

    private synchronized Fingerprint(Parcel in) {
        this.mName = in.readString();
        this.mGroupId = in.readInt();
        this.mFingerId = in.readInt();
        this.mDeviceId = in.readLong();
    }

    private protected CharSequence getName() {
        return this.mName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getFingerId() {
        return this.mFingerId;
    }

    public synchronized int getGroupId() {
        return this.mGroupId;
    }

    public synchronized long getDeviceId() {
        return this.mDeviceId;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mName.toString());
        out.writeInt(this.mGroupId);
        out.writeInt(this.mFingerId);
        out.writeLong(this.mDeviceId);
    }
}
