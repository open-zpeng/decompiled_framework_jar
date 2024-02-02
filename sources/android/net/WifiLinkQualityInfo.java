package android.net;

import android.os.Parcel;
/* loaded from: classes2.dex */
public class WifiLinkQualityInfo extends LinkQualityInfo {
    private String mBssid;
    private int mType = Integer.MAX_VALUE;
    private int mRssi = Integer.MAX_VALUE;
    private long mTxGood = Long.MAX_VALUE;
    private long mTxBad = Long.MAX_VALUE;

    @Override // android.net.LinkQualityInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 2);
        dest.writeInt(this.mType);
        dest.writeInt(this.mRssi);
        dest.writeLong(this.mTxGood);
        dest.writeLong(this.mTxBad);
        dest.writeString(this.mBssid);
    }

    public static synchronized WifiLinkQualityInfo createFromParcelBody(Parcel in) {
        WifiLinkQualityInfo li = new WifiLinkQualityInfo();
        li.initializeFromParcel(in);
        li.mType = in.readInt();
        li.mRssi = in.readInt();
        li.mTxGood = in.readLong();
        li.mTxBad = in.readLong();
        li.mBssid = in.readString();
        return li;
    }

    public synchronized int getType() {
        return this.mType;
    }

    public synchronized void setType(int type) {
        this.mType = type;
    }

    public synchronized String getBssid() {
        return this.mBssid;
    }

    public synchronized void setBssid(String bssid) {
        this.mBssid = bssid;
    }

    public synchronized int getRssi() {
        return this.mRssi;
    }

    public synchronized void setRssi(int rssi) {
        this.mRssi = rssi;
    }

    public synchronized long getTxGood() {
        return this.mTxGood;
    }

    public synchronized void setTxGood(long txGood) {
        this.mTxGood = txGood;
    }

    public synchronized long getTxBad() {
        return this.mTxBad;
    }

    public synchronized void setTxBad(long txBad) {
        this.mTxBad = txBad;
    }
}
