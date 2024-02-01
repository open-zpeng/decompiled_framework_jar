package android.net.apf;

import android.annotation.SystemApi;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.R;

@SystemApi
/* loaded from: classes2.dex */
public final class ApfCapabilities implements Parcelable {
    public static final Parcelable.Creator<ApfCapabilities> CREATOR = new Parcelable.Creator<ApfCapabilities>() { // from class: android.net.apf.ApfCapabilities.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ApfCapabilities createFromParcel(Parcel in) {
            return new ApfCapabilities(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ApfCapabilities[] newArray(int size) {
            return new ApfCapabilities[size];
        }
    };
    public final int apfPacketFormat;
    public final int apfVersionSupported;
    public final int maximumApfProgramSize;

    public ApfCapabilities(int apfVersionSupported, int maximumApfProgramSize, int apfPacketFormat) {
        this.apfVersionSupported = apfVersionSupported;
        this.maximumApfProgramSize = maximumApfProgramSize;
        this.apfPacketFormat = apfPacketFormat;
    }

    private ApfCapabilities(Parcel in) {
        this.apfVersionSupported = in.readInt();
        this.maximumApfProgramSize = in.readInt();
        this.apfPacketFormat = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.apfVersionSupported);
        dest.writeInt(this.maximumApfProgramSize);
        dest.writeInt(this.apfPacketFormat);
    }

    public String toString() {
        return String.format("%s{version: %d, maxSize: %d, format: %d}", getClass().getSimpleName(), Integer.valueOf(this.apfVersionSupported), Integer.valueOf(this.maximumApfProgramSize), Integer.valueOf(this.apfPacketFormat));
    }

    public boolean equals(Object obj) {
        if (obj instanceof ApfCapabilities) {
            ApfCapabilities other = (ApfCapabilities) obj;
            return this.apfVersionSupported == other.apfVersionSupported && this.maximumApfProgramSize == other.maximumApfProgramSize && this.apfPacketFormat == other.apfPacketFormat;
        }
        return false;
    }

    public boolean hasDataAccess() {
        return this.apfVersionSupported >= 4;
    }

    public static boolean getApfDrop8023Frames() {
        return Resources.getSystem().getBoolean(R.bool.config_apfDrop802_3Frames);
    }

    public static int[] getApfEtherTypeBlackList() {
        return Resources.getSystem().getIntArray(R.array.config_apfEthTypeBlackList);
    }
}
