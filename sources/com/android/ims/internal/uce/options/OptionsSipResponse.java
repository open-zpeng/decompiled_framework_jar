package com.android.ims.internal.uce.options;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class OptionsSipResponse implements Parcelable {
    public static final Parcelable.Creator<OptionsSipResponse> CREATOR = new Parcelable.Creator<OptionsSipResponse>() { // from class: com.android.ims.internal.uce.options.OptionsSipResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OptionsSipResponse createFromParcel(Parcel source) {
            return new OptionsSipResponse(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OptionsSipResponse[] newArray(int size) {
            return new OptionsSipResponse[size];
        }
    };
    private OptionsCmdId mCmdId;
    private String mReasonPhrase;
    private int mRequestId;
    private int mRetryAfter;
    private int mSipResponseCode;

    public synchronized OptionsCmdId getCmdId() {
        return this.mCmdId;
    }

    private protected void setCmdId(OptionsCmdId cmdId) {
        this.mCmdId = cmdId;
    }

    public synchronized int getRequestId() {
        return this.mRequestId;
    }

    private protected void setRequestId(int requestId) {
        this.mRequestId = requestId;
    }

    public synchronized int getSipResponseCode() {
        return this.mSipResponseCode;
    }

    private protected void setSipResponseCode(int sipResponseCode) {
        this.mSipResponseCode = sipResponseCode;
    }

    public synchronized String getReasonPhrase() {
        return this.mReasonPhrase;
    }

    private protected void setReasonPhrase(String reasonPhrase) {
        this.mReasonPhrase = reasonPhrase;
    }

    public synchronized int getRetryAfter() {
        return this.mRetryAfter;
    }

    private protected void setRetryAfter(int retryAfter) {
        this.mRetryAfter = retryAfter;
    }

    private protected OptionsSipResponse() {
        this.mRequestId = 0;
        this.mSipResponseCode = 0;
        this.mRetryAfter = 0;
        this.mReasonPhrase = "";
        this.mCmdId = new OptionsCmdId();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRequestId);
        dest.writeInt(this.mSipResponseCode);
        dest.writeString(this.mReasonPhrase);
        dest.writeParcelable(this.mCmdId, flags);
        dest.writeInt(this.mRetryAfter);
    }

    private synchronized OptionsSipResponse(Parcel source) {
        this.mRequestId = 0;
        this.mSipResponseCode = 0;
        this.mRetryAfter = 0;
        this.mReasonPhrase = "";
        readFromParcel(source);
    }

    public synchronized void readFromParcel(Parcel source) {
        this.mRequestId = source.readInt();
        this.mSipResponseCode = source.readInt();
        this.mReasonPhrase = source.readString();
        this.mCmdId = (OptionsCmdId) source.readParcelable(OptionsCmdId.class.getClassLoader());
        this.mRetryAfter = source.readInt();
    }
}
