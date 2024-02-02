package com.xiaopeng.audio;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
@SuppressLint({"ParcelCreator"})
/* loaded from: classes3.dex */
public class xpAudioSessionInfo implements Parcelable {
    public static final Parcelable.Creator<xpAudioSessionInfo> CREATOR = new Parcelable.Creator<xpAudioSessionInfo>() { // from class: com.xiaopeng.audio.xpAudioSessionInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public xpAudioSessionInfo createFromParcel(Parcel source) {
            return new xpAudioSessionInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public xpAudioSessionInfo[] newArray(int size) {
            return new xpAudioSessionInfo[size];
        }
    };
    private String packageName;
    private int sessionId;
    private long startTime;
    private int usageType;

    public xpAudioSessionInfo(int sessionId, int usageType, long startTime, String packageName) {
        this.sessionId = sessionId;
        this.usageType = usageType;
        this.startTime = startTime;
        this.packageName = packageName;
    }

    public xpAudioSessionInfo() {
    }

    private xpAudioSessionInfo(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sessionId);
        dest.writeInt(this.usageType);
        dest.writeLong(this.startTime);
        dest.writeString(this.packageName);
    }

    public void readFromParcel(Parcel source) {
        this.sessionId = source.readInt();
        this.usageType = source.readInt();
        this.startTime = source.readLong();
        this.packageName = source.readString();
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getUsageType() {
        return this.usageType;
    }

    public void setUsageType(int usageType) {
        this.usageType = usageType;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
