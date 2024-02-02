package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.ims.internal.uce.common.CapInfo;
/* loaded from: classes3.dex */
public class PresCapInfo implements Parcelable {
    public static final Parcelable.Creator<PresCapInfo> CREATOR = new Parcelable.Creator<PresCapInfo>() { // from class: com.android.ims.internal.uce.presence.PresCapInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresCapInfo createFromParcel(Parcel source) {
            return new PresCapInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresCapInfo[] newArray(int size) {
            return new PresCapInfo[size];
        }
    };
    private CapInfo mCapInfo;
    public protected String mContactUri;

    private protected CapInfo getCapInfo() {
        return this.mCapInfo;
    }

    public synchronized void setCapInfo(CapInfo capInfo) {
        this.mCapInfo = capInfo;
    }

    private protected String getContactUri() {
        return this.mContactUri;
    }

    public synchronized void setContactUri(String contactUri) {
        this.mContactUri = contactUri;
    }

    public synchronized PresCapInfo() {
        this.mContactUri = "";
        this.mCapInfo = new CapInfo();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mContactUri);
        dest.writeParcelable(this.mCapInfo, flags);
    }

    private synchronized PresCapInfo(Parcel source) {
        this.mContactUri = "";
        readFromParcel(source);
    }

    public synchronized void readFromParcel(Parcel source) {
        this.mContactUri = source.readString();
        this.mCapInfo = (CapInfo) source.readParcelable(CapInfo.class.getClassLoader());
    }
}
