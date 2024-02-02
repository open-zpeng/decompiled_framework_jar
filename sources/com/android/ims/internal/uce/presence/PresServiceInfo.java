package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class PresServiceInfo implements Parcelable {
    public static final Parcelable.Creator<PresServiceInfo> CREATOR = new Parcelable.Creator<PresServiceInfo>() { // from class: com.android.ims.internal.uce.presence.PresServiceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresServiceInfo createFromParcel(Parcel source) {
            return new PresServiceInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresServiceInfo[] newArray(int size) {
            return new PresServiceInfo[size];
        }
    };
    public static final int UCE_PRES_MEDIA_CAP_FULL_AUDIO_AND_VIDEO = 2;
    public static final int UCE_PRES_MEDIA_CAP_FULL_AUDIO_ONLY = 1;
    public static final int UCE_PRES_MEDIA_CAP_NONE = 0;
    public static final int UCE_PRES_MEDIA_CAP_UNKNOWN = 3;
    private int mMediaCap;
    private String mServiceDesc;
    private String mServiceID;
    private String mServiceVer;

    private protected int getMediaType() {
        return this.mMediaCap;
    }

    public synchronized void setMediaType(int nMediaCap) {
        this.mMediaCap = nMediaCap;
    }

    private protected String getServiceId() {
        return this.mServiceID;
    }

    public synchronized void setServiceId(String serviceID) {
        this.mServiceID = serviceID;
    }

    private protected String getServiceDesc() {
        return this.mServiceDesc;
    }

    public synchronized void setServiceDesc(String serviceDesc) {
        this.mServiceDesc = serviceDesc;
    }

    private protected String getServiceVer() {
        return this.mServiceVer;
    }

    public synchronized void setServiceVer(String serviceVer) {
        this.mServiceVer = serviceVer;
    }

    public synchronized PresServiceInfo() {
        this.mMediaCap = 0;
        this.mServiceID = "";
        this.mServiceDesc = "";
        this.mServiceVer = "";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mServiceID);
        dest.writeString(this.mServiceDesc);
        dest.writeString(this.mServiceVer);
        dest.writeInt(this.mMediaCap);
    }

    private synchronized PresServiceInfo(Parcel source) {
        this.mMediaCap = 0;
        this.mServiceID = "";
        this.mServiceDesc = "";
        this.mServiceVer = "";
        readFromParcel(source);
    }

    public synchronized void readFromParcel(Parcel source) {
        this.mServiceID = source.readString();
        this.mServiceDesc = source.readString();
        this.mServiceVer = source.readString();
        this.mMediaCap = source.readInt();
    }
}
