package com.android.ims.internal.uce.options;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.ims.internal.uce.common.CapInfo;
import com.android.ims.internal.uce.common.StatusCode;
/* loaded from: classes3.dex */
public class OptionsCmdStatus implements Parcelable {
    public static final Parcelable.Creator<OptionsCmdStatus> CREATOR = new Parcelable.Creator<OptionsCmdStatus>() { // from class: com.android.ims.internal.uce.options.OptionsCmdStatus.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OptionsCmdStatus createFromParcel(Parcel source) {
            return new OptionsCmdStatus(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OptionsCmdStatus[] newArray(int size) {
            return new OptionsCmdStatus[size];
        }
    };
    private CapInfo mCapInfo;
    private OptionsCmdId mCmdId;
    private StatusCode mStatus;
    private int mUserData;

    public synchronized OptionsCmdId getCmdId() {
        return this.mCmdId;
    }

    private protected void setCmdId(OptionsCmdId cmdId) {
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

    private protected OptionsCmdStatus() {
        this.mStatus = new StatusCode();
        this.mCapInfo = new CapInfo();
        this.mCmdId = new OptionsCmdId();
        this.mUserData = 0;
    }

    public synchronized CapInfo getCapInfo() {
        return this.mCapInfo;
    }

    private protected void setCapInfo(CapInfo capInfo) {
        this.mCapInfo = capInfo;
    }

    public static synchronized OptionsCmdStatus getOptionsCmdStatusInstance() {
        return new OptionsCmdStatus();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mUserData);
        dest.writeParcelable(this.mCmdId, flags);
        dest.writeParcelable(this.mStatus, flags);
        dest.writeParcelable(this.mCapInfo, flags);
    }

    private synchronized OptionsCmdStatus(Parcel source) {
        readFromParcel(source);
    }

    public synchronized void readFromParcel(Parcel source) {
        this.mUserData = source.readInt();
        this.mCmdId = (OptionsCmdId) source.readParcelable(OptionsCmdId.class.getClassLoader());
        this.mStatus = (StatusCode) source.readParcelable(StatusCode.class.getClassLoader());
        this.mCapInfo = (CapInfo) source.readParcelable(CapInfo.class.getClassLoader());
    }
}
