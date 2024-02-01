package com.android.ims.internal.uce.common;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class CapInfo implements Parcelable {
    public static final Parcelable.Creator<CapInfo> CREATOR = new Parcelable.Creator<CapInfo>() { // from class: com.android.ims.internal.uce.common.CapInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CapInfo createFromParcel(Parcel source) {
            return new CapInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CapInfo[] newArray(int size) {
            return new CapInfo[size];
        }
    };
    private long mCapTimestamp;
    private boolean mCdViaPresenceSupported;
    private String[] mExts;
    private boolean mFtHttpSupported;
    private boolean mFtSnFSupported;
    private boolean mFtSupported;
    private boolean mFtThumbSupported;
    private boolean mFullSnFGroupChatSupported;
    private boolean mGeoPullFtSupported;
    private boolean mGeoPullSupported;
    private boolean mGeoPushSupported;
    private boolean mImSupported;
    private boolean mIpVideoSupported;
    private boolean mIpVoiceSupported;
    private boolean mIsSupported;
    private boolean mRcsIpVideoCallSupported;
    private boolean mRcsIpVideoOnlyCallSupported;
    private boolean mRcsIpVoiceCallSupported;
    private boolean mSmSupported;
    private boolean mSpSupported;
    private boolean mVsDuringCSSupported;
    private boolean mVsSupported;

    /* JADX INFO: Access modifiers changed from: private */
    public CapInfo() {
        this.mImSupported = false;
        this.mFtSupported = false;
        this.mFtThumbSupported = false;
        this.mFtSnFSupported = false;
        this.mFtHttpSupported = false;
        this.mIsSupported = false;
        this.mVsDuringCSSupported = false;
        this.mVsSupported = false;
        this.mSpSupported = false;
        this.mCdViaPresenceSupported = false;
        this.mIpVoiceSupported = false;
        this.mIpVideoSupported = false;
        this.mGeoPullFtSupported = false;
        this.mGeoPullSupported = false;
        this.mGeoPushSupported = false;
        this.mSmSupported = false;
        this.mFullSnFGroupChatSupported = false;
        this.mRcsIpVoiceCallSupported = false;
        this.mRcsIpVideoCallSupported = false;
        this.mRcsIpVideoOnlyCallSupported = false;
        this.mExts = new String[10];
        this.mCapTimestamp = 0L;
    }

    private protected boolean isImSupported() {
        return this.mImSupported;
    }

    private protected void setImSupported(boolean imSupported) {
        this.mImSupported = imSupported;
    }

    private protected boolean isFtThumbSupported() {
        return this.mFtThumbSupported;
    }

    private protected void setFtThumbSupported(boolean ftThumbSupported) {
        this.mFtThumbSupported = ftThumbSupported;
    }

    private protected boolean isFtSnFSupported() {
        return this.mFtSnFSupported;
    }

    private protected void setFtSnFSupported(boolean ftSnFSupported) {
        this.mFtSnFSupported = ftSnFSupported;
    }

    private protected boolean isFtHttpSupported() {
        return this.mFtHttpSupported;
    }

    private protected void setFtHttpSupported(boolean ftHttpSupported) {
        this.mFtHttpSupported = ftHttpSupported;
    }

    private protected boolean isFtSupported() {
        return this.mFtSupported;
    }

    private protected void setFtSupported(boolean ftSupported) {
        this.mFtSupported = ftSupported;
    }

    private protected boolean isIsSupported() {
        return this.mIsSupported;
    }

    private protected void setIsSupported(boolean isSupported) {
        this.mIsSupported = isSupported;
    }

    private protected boolean isVsDuringCSSupported() {
        return this.mVsDuringCSSupported;
    }

    private protected void setVsDuringCSSupported(boolean vsDuringCSSupported) {
        this.mVsDuringCSSupported = vsDuringCSSupported;
    }

    private protected boolean isVsSupported() {
        return this.mVsSupported;
    }

    private protected void setVsSupported(boolean vsSupported) {
        this.mVsSupported = vsSupported;
    }

    private protected boolean isSpSupported() {
        return this.mSpSupported;
    }

    private protected void setSpSupported(boolean spSupported) {
        this.mSpSupported = spSupported;
    }

    private protected boolean isCdViaPresenceSupported() {
        return this.mCdViaPresenceSupported;
    }

    private protected void setCdViaPresenceSupported(boolean cdViaPresenceSupported) {
        this.mCdViaPresenceSupported = cdViaPresenceSupported;
    }

    private protected boolean isIpVoiceSupported() {
        return this.mIpVoiceSupported;
    }

    private protected void setIpVoiceSupported(boolean ipVoiceSupported) {
        this.mIpVoiceSupported = ipVoiceSupported;
    }

    private protected boolean isIpVideoSupported() {
        return this.mIpVideoSupported;
    }

    private protected void setIpVideoSupported(boolean ipVideoSupported) {
        this.mIpVideoSupported = ipVideoSupported;
    }

    private protected boolean isGeoPullFtSupported() {
        return this.mGeoPullFtSupported;
    }

    private protected void setGeoPullFtSupported(boolean geoPullFtSupported) {
        this.mGeoPullFtSupported = geoPullFtSupported;
    }

    private protected boolean isGeoPullSupported() {
        return this.mGeoPullSupported;
    }

    private protected void setGeoPullSupported(boolean geoPullSupported) {
        this.mGeoPullSupported = geoPullSupported;
    }

    private protected boolean isGeoPushSupported() {
        return this.mGeoPushSupported;
    }

    private protected void setGeoPushSupported(boolean geoPushSupported) {
        this.mGeoPushSupported = geoPushSupported;
    }

    private protected boolean isSmSupported() {
        return this.mSmSupported;
    }

    private protected void setSmSupported(boolean smSupported) {
        this.mSmSupported = smSupported;
    }

    private protected boolean isFullSnFGroupChatSupported() {
        return this.mFullSnFGroupChatSupported;
    }

    private protected boolean isRcsIpVoiceCallSupported() {
        return this.mRcsIpVoiceCallSupported;
    }

    private protected boolean isRcsIpVideoCallSupported() {
        return this.mRcsIpVideoCallSupported;
    }

    private protected boolean isRcsIpVideoOnlyCallSupported() {
        return this.mRcsIpVideoOnlyCallSupported;
    }

    private protected void setFullSnFGroupChatSupported(boolean fullSnFGroupChatSupported) {
        this.mFullSnFGroupChatSupported = fullSnFGroupChatSupported;
    }

    private protected void setRcsIpVoiceCallSupported(boolean rcsIpVoiceCallSupported) {
        this.mRcsIpVoiceCallSupported = rcsIpVoiceCallSupported;
    }

    private protected void setRcsIpVideoCallSupported(boolean rcsIpVideoCallSupported) {
        this.mRcsIpVideoCallSupported = rcsIpVideoCallSupported;
    }

    private protected void setRcsIpVideoOnlyCallSupported(boolean rcsIpVideoOnlyCallSupported) {
        this.mRcsIpVideoOnlyCallSupported = rcsIpVideoOnlyCallSupported;
    }

    public synchronized String[] getExts() {
        return this.mExts;
    }

    private protected void setExts(String[] exts) {
        this.mExts = exts;
    }

    private protected long getCapTimestamp() {
        return this.mCapTimestamp;
    }

    private protected void setCapTimestamp(long capTimestamp) {
        this.mCapTimestamp = capTimestamp;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mImSupported ? 1 : 0);
        dest.writeInt(this.mFtSupported ? 1 : 0);
        dest.writeInt(this.mFtThumbSupported ? 1 : 0);
        dest.writeInt(this.mFtSnFSupported ? 1 : 0);
        dest.writeInt(this.mFtHttpSupported ? 1 : 0);
        dest.writeInt(this.mIsSupported ? 1 : 0);
        dest.writeInt(this.mVsDuringCSSupported ? 1 : 0);
        dest.writeInt(this.mVsSupported ? 1 : 0);
        dest.writeInt(this.mSpSupported ? 1 : 0);
        dest.writeInt(this.mCdViaPresenceSupported ? 1 : 0);
        dest.writeInt(this.mIpVoiceSupported ? 1 : 0);
        dest.writeInt(this.mIpVideoSupported ? 1 : 0);
        dest.writeInt(this.mGeoPullFtSupported ? 1 : 0);
        dest.writeInt(this.mGeoPullSupported ? 1 : 0);
        dest.writeInt(this.mGeoPushSupported ? 1 : 0);
        dest.writeInt(this.mSmSupported ? 1 : 0);
        dest.writeInt(this.mFullSnFGroupChatSupported ? 1 : 0);
        dest.writeInt(this.mRcsIpVoiceCallSupported ? 1 : 0);
        dest.writeInt(this.mRcsIpVideoCallSupported ? 1 : 0);
        dest.writeInt(this.mRcsIpVideoOnlyCallSupported ? 1 : 0);
        dest.writeStringArray(this.mExts);
        dest.writeLong(this.mCapTimestamp);
    }

    private synchronized CapInfo(Parcel source) {
        this.mImSupported = false;
        this.mFtSupported = false;
        this.mFtThumbSupported = false;
        this.mFtSnFSupported = false;
        this.mFtHttpSupported = false;
        this.mIsSupported = false;
        this.mVsDuringCSSupported = false;
        this.mVsSupported = false;
        this.mSpSupported = false;
        this.mCdViaPresenceSupported = false;
        this.mIpVoiceSupported = false;
        this.mIpVideoSupported = false;
        this.mGeoPullFtSupported = false;
        this.mGeoPullSupported = false;
        this.mGeoPushSupported = false;
        this.mSmSupported = false;
        this.mFullSnFGroupChatSupported = false;
        this.mRcsIpVoiceCallSupported = false;
        this.mRcsIpVideoCallSupported = false;
        this.mRcsIpVideoOnlyCallSupported = false;
        this.mExts = new String[10];
        this.mCapTimestamp = 0L;
        readFromParcel(source);
    }

    public synchronized void readFromParcel(Parcel source) {
        this.mImSupported = source.readInt() != 0;
        this.mFtSupported = source.readInt() != 0;
        this.mFtThumbSupported = source.readInt() != 0;
        this.mFtSnFSupported = source.readInt() != 0;
        this.mFtHttpSupported = source.readInt() != 0;
        this.mIsSupported = source.readInt() != 0;
        this.mVsDuringCSSupported = source.readInt() != 0;
        this.mVsSupported = source.readInt() != 0;
        this.mSpSupported = source.readInt() != 0;
        this.mCdViaPresenceSupported = source.readInt() != 0;
        this.mIpVoiceSupported = source.readInt() != 0;
        this.mIpVideoSupported = source.readInt() != 0;
        this.mGeoPullFtSupported = source.readInt() != 0;
        this.mGeoPullSupported = source.readInt() != 0;
        this.mGeoPushSupported = source.readInt() != 0;
        this.mSmSupported = source.readInt() != 0;
        this.mFullSnFGroupChatSupported = source.readInt() != 0;
        this.mRcsIpVoiceCallSupported = source.readInt() != 0;
        this.mRcsIpVideoCallSupported = source.readInt() != 0;
        this.mRcsIpVideoOnlyCallSupported = source.readInt() != 0;
        this.mExts = source.createStringArray();
        this.mCapTimestamp = source.readLong();
    }
}
