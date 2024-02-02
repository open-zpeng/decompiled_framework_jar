package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class PresTupleInfo implements Parcelable {
    public static final Parcelable.Creator<PresTupleInfo> CREATOR = new Parcelable.Creator<PresTupleInfo>() { // from class: com.android.ims.internal.uce.presence.PresTupleInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresTupleInfo createFromParcel(Parcel source) {
            return new PresTupleInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresTupleInfo[] newArray(int size) {
            return new PresTupleInfo[size];
        }
    };
    private String mContactUri;
    private String mFeatureTag;
    private String mTimestamp;

    public synchronized String getFeatureTag() {
        return this.mFeatureTag;
    }

    private protected void setFeatureTag(String featureTag) {
        this.mFeatureTag = featureTag;
    }

    public synchronized String getContactUri() {
        return this.mContactUri;
    }

    private protected void setContactUri(String contactUri) {
        this.mContactUri = contactUri;
    }

    public synchronized String getTimestamp() {
        return this.mTimestamp;
    }

    private protected void setTimestamp(String timestamp) {
        this.mTimestamp = timestamp;
    }

    private protected PresTupleInfo() {
        this.mFeatureTag = "";
        this.mContactUri = "";
        this.mTimestamp = "";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFeatureTag);
        dest.writeString(this.mContactUri);
        dest.writeString(this.mTimestamp);
    }

    private synchronized PresTupleInfo(Parcel source) {
        this.mFeatureTag = "";
        this.mContactUri = "";
        this.mTimestamp = "";
        readFromParcel(source);
    }

    public synchronized void readFromParcel(Parcel source) {
        this.mFeatureTag = source.readString();
        this.mContactUri = source.readString();
        this.mTimestamp = source.readString();
    }
}
