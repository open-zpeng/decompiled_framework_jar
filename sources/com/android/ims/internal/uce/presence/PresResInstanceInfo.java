package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
/* loaded from: classes3.dex */
public class PresResInstanceInfo implements Parcelable {
    public static final Parcelable.Creator<PresResInstanceInfo> CREATOR = new Parcelable.Creator<PresResInstanceInfo>() { // from class: com.android.ims.internal.uce.presence.PresResInstanceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresResInstanceInfo createFromParcel(Parcel source) {
            return new PresResInstanceInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresResInstanceInfo[] newArray(int size) {
            return new PresResInstanceInfo[size];
        }
    };
    public static final int UCE_PRES_RES_INSTANCE_STATE_ACTIVE = 0;
    public static final int UCE_PRES_RES_INSTANCE_STATE_PENDING = 1;
    public static final int UCE_PRES_RES_INSTANCE_STATE_TERMINATED = 2;
    public static final int UCE_PRES_RES_INSTANCE_STATE_UNKNOWN = 3;
    public static final int UCE_PRES_RES_INSTANCE_UNKNOWN = 4;
    private String mId;
    private String mPresentityUri;
    private String mReason;
    private int mResInstanceState;
    private PresTupleInfo[] mTupleInfoArray;

    public synchronized int getResInstanceState() {
        return this.mResInstanceState;
    }

    private protected void setResInstanceState(int nResInstanceState) {
        this.mResInstanceState = nResInstanceState;
    }

    public synchronized String getResId() {
        return this.mId;
    }

    private protected void setResId(String resourceId) {
        this.mId = resourceId;
    }

    public synchronized String getReason() {
        return this.mReason;
    }

    private protected void setReason(String reason) {
        this.mReason = reason;
    }

    public synchronized String getPresentityUri() {
        return this.mPresentityUri;
    }

    private protected void setPresentityUri(String presentityUri) {
        this.mPresentityUri = presentityUri;
    }

    public synchronized PresTupleInfo[] getTupleInfo() {
        return this.mTupleInfoArray;
    }

    private protected void setTupleInfo(PresTupleInfo[] tupleInfo) {
        this.mTupleInfoArray = new PresTupleInfo[tupleInfo.length];
        this.mTupleInfoArray = tupleInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PresResInstanceInfo() {
        this.mId = "";
        this.mReason = "";
        this.mPresentityUri = "";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mReason);
        dest.writeInt(this.mResInstanceState);
        dest.writeString(this.mPresentityUri);
        dest.writeParcelableArray(this.mTupleInfoArray, flags);
    }

    private synchronized PresResInstanceInfo(Parcel source) {
        this.mId = "";
        this.mReason = "";
        this.mPresentityUri = "";
        readFromParcel(source);
    }

    public synchronized void readFromParcel(Parcel source) {
        this.mId = source.readString();
        this.mReason = source.readString();
        this.mResInstanceState = source.readInt();
        this.mPresentityUri = source.readString();
        Parcelable[] tempParcelableArray = source.readParcelableArray(PresTupleInfo.class.getClassLoader());
        this.mTupleInfoArray = new PresTupleInfo[0];
        if (tempParcelableArray != null) {
            this.mTupleInfoArray = (PresTupleInfo[]) Arrays.copyOf(tempParcelableArray, tempParcelableArray.length, PresTupleInfo[].class);
        }
    }
}
