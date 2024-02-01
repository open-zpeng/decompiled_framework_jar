package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class PresRlmiInfo implements Parcelable {
    public static final Parcelable.Creator<PresRlmiInfo> CREATOR = new Parcelable.Creator<PresRlmiInfo>() { // from class: com.android.ims.internal.uce.presence.PresRlmiInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresRlmiInfo createFromParcel(Parcel source) {
            return new PresRlmiInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresRlmiInfo[] newArray(int size) {
            return new PresRlmiInfo[size];
        }
    };
    private boolean mFullState;
    private String mListName;
    private PresSubscriptionState mPresSubscriptionState;
    private int mRequestId;
    private int mSubscriptionExpireTime;
    private String mSubscriptionTerminatedReason;
    private String mUri;
    private int mVersion;

    public synchronized String getUri() {
        return this.mUri;
    }

    private protected void setUri(String uri) {
        this.mUri = uri;
    }

    public synchronized int getVersion() {
        return this.mVersion;
    }

    private protected void setVersion(int version) {
        this.mVersion = version;
    }

    public synchronized boolean isFullState() {
        return this.mFullState;
    }

    private protected void setFullState(boolean fullState) {
        this.mFullState = fullState;
    }

    public synchronized String getListName() {
        return this.mListName;
    }

    private protected void setListName(String listName) {
        this.mListName = listName;
    }

    public synchronized int getRequestId() {
        return this.mRequestId;
    }

    private protected void setRequestId(int requestId) {
        this.mRequestId = requestId;
    }

    public synchronized PresSubscriptionState getPresSubscriptionState() {
        return this.mPresSubscriptionState;
    }

    private protected void setPresSubscriptionState(PresSubscriptionState presSubscriptionState) {
        this.mPresSubscriptionState = presSubscriptionState;
    }

    public synchronized int getSubscriptionExpireTime() {
        return this.mSubscriptionExpireTime;
    }

    private protected void setSubscriptionExpireTime(int subscriptionExpireTime) {
        this.mSubscriptionExpireTime = subscriptionExpireTime;
    }

    public synchronized String getSubscriptionTerminatedReason() {
        return this.mSubscriptionTerminatedReason;
    }

    private protected void setSubscriptionTerminatedReason(String subscriptionTerminatedReason) {
        this.mSubscriptionTerminatedReason = subscriptionTerminatedReason;
    }

    private protected PresRlmiInfo() {
        this.mUri = "";
        this.mListName = "";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUri);
        dest.writeInt(this.mVersion);
        dest.writeInt(this.mFullState ? 1 : 0);
        dest.writeString(this.mListName);
        dest.writeInt(this.mRequestId);
        dest.writeParcelable(this.mPresSubscriptionState, flags);
        dest.writeInt(this.mSubscriptionExpireTime);
        dest.writeString(this.mSubscriptionTerminatedReason);
    }

    private synchronized PresRlmiInfo(Parcel source) {
        this.mUri = "";
        this.mListName = "";
        readFromParcel(source);
    }

    public synchronized void readFromParcel(Parcel source) {
        this.mUri = source.readString();
        this.mVersion = source.readInt();
        this.mFullState = source.readInt() != 0;
        this.mListName = source.readString();
        this.mRequestId = source.readInt();
        this.mPresSubscriptionState = (PresSubscriptionState) source.readParcelable(PresSubscriptionState.class.getClassLoader());
        this.mSubscriptionExpireTime = source.readInt();
        this.mSubscriptionTerminatedReason = source.readString();
    }
}
