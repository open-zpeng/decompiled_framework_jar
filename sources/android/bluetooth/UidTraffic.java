package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class UidTraffic implements Cloneable, Parcelable {
    public static final Parcelable.Creator<UidTraffic> CREATOR = new Parcelable.Creator<UidTraffic>() { // from class: android.bluetooth.UidTraffic.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UidTraffic createFromParcel(Parcel source) {
            return new UidTraffic(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UidTraffic[] newArray(int size) {
            return new UidTraffic[size];
        }
    };
    private final int mAppUid;
    private long mRxBytes;
    private long mTxBytes;

    public synchronized UidTraffic(int appUid) {
        this.mAppUid = appUid;
    }

    public synchronized UidTraffic(int appUid, long rx, long tx) {
        this.mAppUid = appUid;
        this.mRxBytes = rx;
        this.mTxBytes = tx;
    }

    synchronized UidTraffic(Parcel in) {
        this.mAppUid = in.readInt();
        this.mRxBytes = in.readLong();
        this.mTxBytes = in.readLong();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mAppUid);
        dest.writeLong(this.mRxBytes);
        dest.writeLong(this.mTxBytes);
    }

    public synchronized void setRxBytes(long bytes) {
        this.mRxBytes = bytes;
    }

    public synchronized void setTxBytes(long bytes) {
        this.mTxBytes = bytes;
    }

    public synchronized void addRxBytes(long bytes) {
        this.mRxBytes += bytes;
    }

    public synchronized void addTxBytes(long bytes) {
        this.mTxBytes += bytes;
    }

    public synchronized int getUid() {
        return this.mAppUid;
    }

    public synchronized long getRxBytes() {
        return this.mRxBytes;
    }

    public synchronized long getTxBytes() {
        return this.mTxBytes;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* renamed from: clone */
    public UidTraffic m16clone() {
        return new UidTraffic(this.mAppUid, this.mRxBytes, this.mTxBytes);
    }

    public String toString() {
        return "UidTraffic{mAppUid=" + this.mAppUid + ", mRxBytes=" + this.mRxBytes + ", mTxBytes=" + this.mTxBytes + '}';
    }
}
