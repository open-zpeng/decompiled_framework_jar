package com.android.ims.internal.uce.presence;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class PresSipResponse implements Parcelable {
    public static final Parcelable.Creator<PresSipResponse> CREATOR = new Parcelable.Creator<PresSipResponse>() { // from class: com.android.ims.internal.uce.presence.PresSipResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresSipResponse createFromParcel(Parcel source) {
            return new PresSipResponse(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PresSipResponse[] newArray(int size) {
            return new PresSipResponse[size];
        }
    };
    private PresCmdId mCmdId;
    private String mReasonPhrase;
    private int mRequestId;
    private int mRetryAfter;
    private int mSipResponseCode;

    private protected PresCmdId getCmdId() {
        return this.mCmdId;
    }

    private protected void setCmdId(PresCmdId cmdId) {
        this.mCmdId = cmdId;
    }

    private protected int getRequestId() {
        return this.mRequestId;
    }

    private protected void setRequestId(int requestId) {
        this.mRequestId = requestId;
    }

    private protected int getSipResponseCode() {
        return this.mSipResponseCode;
    }

    private protected void setSipResponseCode(int sipResponseCode) {
        this.mSipResponseCode = sipResponseCode;
    }

    private protected String getReasonPhrase() {
        return this.mReasonPhrase;
    }

    private protected void setReasonPhrase(String reasonPhrase) {
        this.mReasonPhrase = reasonPhrase;
    }

    private protected int getRetryAfter() {
        return this.mRetryAfter;
    }

    private protected void setRetryAfter(int retryAfter) {
        this.mRetryAfter = retryAfter;
    }

    private protected PresSipResponse() {
        this.mCmdId = new PresCmdId();
        this.mRequestId = 0;
        this.mSipResponseCode = 0;
        this.mRetryAfter = 0;
        this.mReasonPhrase = "";
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

    private synchronized PresSipResponse(Parcel source) {
        this.mCmdId = new PresCmdId();
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
        this.mCmdId = (PresCmdId) source.readParcelable(PresCmdId.class.getClassLoader());
        this.mRetryAfter = source.readInt();
    }
}
