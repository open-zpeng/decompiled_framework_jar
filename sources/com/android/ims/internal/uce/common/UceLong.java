package com.android.ims.internal.uce.common;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class UceLong implements Parcelable {
    public static final Parcelable.Creator<UceLong> CREATOR = new Parcelable.Creator<UceLong>() { // from class: com.android.ims.internal.uce.common.UceLong.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UceLong createFromParcel(Parcel source) {
            return new UceLong(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UceLong[] newArray(int size) {
            return new UceLong[size];
        }
    };
    private int mClientId;
    private long mUceLong;

    private protected UceLong() {
        this.mClientId = 1001;
    }

    private protected long getUceLong() {
        return this.mUceLong;
    }

    private protected void setUceLong(long uceLong) {
        this.mUceLong = uceLong;
    }

    private protected int getClientId() {
        return this.mClientId;
    }

    private protected void setClientId(int nClientId) {
        this.mClientId = nClientId;
    }

    public static synchronized UceLong getUceLongInstance() {
        return new UceLong();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        writeToParcel(dest);
    }

    private synchronized void writeToParcel(Parcel out) {
        out.writeLong(this.mUceLong);
        out.writeInt(this.mClientId);
    }

    private synchronized UceLong(Parcel source) {
        this.mClientId = 1001;
        readFromParcel(source);
    }

    public synchronized void readFromParcel(Parcel source) {
        this.mUceLong = source.readLong();
        this.mClientId = source.readInt();
    }
}
