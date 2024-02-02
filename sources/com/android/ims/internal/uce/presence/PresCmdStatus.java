package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.ims.internal.uce.common.StatusCode;
/* loaded from: classes3.dex */
public class PresCmdStatus implements Parcelable {
    public static final Parcelable.Creator<PresCmdStatus> CREATOR = new Parcelable.Creator<PresCmdStatus>() { // from class: com.android.ims.internal.uce.presence.PresCmdStatus.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresCmdStatus createFromParcel(Parcel source) {
            return new PresCmdStatus(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresCmdStatus[] newArray(int size) {
            return new PresCmdStatus[size];
        }
    };
    private PresCmdId mCmdId;
    private int mRequestId;
    private StatusCode mStatus;
    private int mUserData;

    public synchronized PresCmdId getCmdId() {
        return this.mCmdId;
    }

    private protected void setCmdId(PresCmdId cmdId) {
        this.mCmdId = cmdId;
    }

    public synchronized int getUserData() {
        return this.mUserData;
    }

    private protected void setUserData(int userData) {
        this.mUserData = userData;
    }

    public synchronized StatusCode getStatus() {
        return this.mStatus;
    }

    private protected void setStatus(StatusCode status) {
        this.mStatus = status;
    }

    public synchronized int getRequestId() {
        return this.mRequestId;
    }

    private protected void setRequestId(int requestId) {
        this.mRequestId = requestId;
    }

    private protected PresCmdStatus() {
        this.mCmdId = new PresCmdId();
        this.mStatus = new StatusCode();
        this.mStatus = new StatusCode();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mUserData);
        dest.writeInt(this.mRequestId);
        dest.writeParcelable(this.mCmdId, flags);
        dest.writeParcelable(this.mStatus, flags);
    }

    private synchronized PresCmdStatus(Parcel source) {
        this.mCmdId = new PresCmdId();
        this.mStatus = new StatusCode();
        readFromParcel(source);
    }

    public synchronized void readFromParcel(Parcel source) {
        this.mUserData = source.readInt();
        this.mRequestId = source.readInt();
        this.mCmdId = (PresCmdId) source.readParcelable(PresCmdId.class.getClassLoader());
        this.mStatus = (StatusCode) source.readParcelable(StatusCode.class.getClassLoader());
    }
}
