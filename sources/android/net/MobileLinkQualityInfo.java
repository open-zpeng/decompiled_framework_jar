package android.net;

import android.os.Parcel;
/* loaded from: classes2.dex */
public class MobileLinkQualityInfo extends LinkQualityInfo {
    private int mMobileNetworkType = Integer.MAX_VALUE;
    private int mRssi = Integer.MAX_VALUE;
    private int mGsmErrorRate = Integer.MAX_VALUE;
    private int mCdmaDbm = Integer.MAX_VALUE;
    private int mCdmaEcio = Integer.MAX_VALUE;
    private int mEvdoDbm = Integer.MAX_VALUE;
    private int mEvdoEcio = Integer.MAX_VALUE;
    private int mEvdoSnr = Integer.MAX_VALUE;
    private int mLteSignalStrength = Integer.MAX_VALUE;
    private int mLteRsrp = Integer.MAX_VALUE;
    private int mLteRsrq = Integer.MAX_VALUE;
    private int mLteRssnr = Integer.MAX_VALUE;
    private int mLteCqi = Integer.MAX_VALUE;

    private protected MobileLinkQualityInfo() {
    }

    @Override // android.net.LinkQualityInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 3);
        dest.writeInt(this.mMobileNetworkType);
        dest.writeInt(this.mRssi);
        dest.writeInt(this.mGsmErrorRate);
        dest.writeInt(this.mCdmaDbm);
        dest.writeInt(this.mCdmaEcio);
        dest.writeInt(this.mEvdoDbm);
        dest.writeInt(this.mEvdoEcio);
        dest.writeInt(this.mEvdoSnr);
        dest.writeInt(this.mLteSignalStrength);
        dest.writeInt(this.mLteRsrp);
        dest.writeInt(this.mLteRsrq);
        dest.writeInt(this.mLteRssnr);
        dest.writeInt(this.mLteCqi);
    }

    public static synchronized MobileLinkQualityInfo createFromParcelBody(Parcel in) {
        MobileLinkQualityInfo li = new MobileLinkQualityInfo();
        li.initializeFromParcel(in);
        li.mMobileNetworkType = in.readInt();
        li.mRssi = in.readInt();
        li.mGsmErrorRate = in.readInt();
        li.mCdmaDbm = in.readInt();
        li.mCdmaEcio = in.readInt();
        li.mEvdoDbm = in.readInt();
        li.mEvdoEcio = in.readInt();
        li.mEvdoSnr = in.readInt();
        li.mLteSignalStrength = in.readInt();
        li.mLteRsrp = in.readInt();
        li.mLteRsrq = in.readInt();
        li.mLteRssnr = in.readInt();
        li.mLteCqi = in.readInt();
        return li;
    }

    private protected int getMobileNetworkType() {
        return this.mMobileNetworkType;
    }

    private protected void setMobileNetworkType(int mobileNetworkType) {
        this.mMobileNetworkType = mobileNetworkType;
    }

    public synchronized int getRssi() {
        return this.mRssi;
    }

    private protected void setRssi(int Rssi) {
        this.mRssi = Rssi;
    }

    public synchronized int getGsmErrorRate() {
        return this.mGsmErrorRate;
    }

    private protected void setGsmErrorRate(int gsmErrorRate) {
        this.mGsmErrorRate = gsmErrorRate;
    }

    public synchronized int getCdmaDbm() {
        return this.mCdmaDbm;
    }

    private protected void setCdmaDbm(int cdmaDbm) {
        this.mCdmaDbm = cdmaDbm;
    }

    public synchronized int getCdmaEcio() {
        return this.mCdmaEcio;
    }

    private protected void setCdmaEcio(int cdmaEcio) {
        this.mCdmaEcio = cdmaEcio;
    }

    public synchronized int getEvdoDbm() {
        return this.mEvdoDbm;
    }

    private protected void setEvdoDbm(int evdoDbm) {
        this.mEvdoDbm = evdoDbm;
    }

    public synchronized int getEvdoEcio() {
        return this.mEvdoEcio;
    }

    private protected void setEvdoEcio(int evdoEcio) {
        this.mEvdoEcio = evdoEcio;
    }

    public synchronized int getEvdoSnr() {
        return this.mEvdoSnr;
    }

    private protected void setEvdoSnr(int evdoSnr) {
        this.mEvdoSnr = evdoSnr;
    }

    public synchronized int getLteSignalStrength() {
        return this.mLteSignalStrength;
    }

    private protected void setLteSignalStrength(int lteSignalStrength) {
        this.mLteSignalStrength = lteSignalStrength;
    }

    public synchronized int getLteRsrp() {
        return this.mLteRsrp;
    }

    private protected void setLteRsrp(int lteRsrp) {
        this.mLteRsrp = lteRsrp;
    }

    public synchronized int getLteRsrq() {
        return this.mLteRsrq;
    }

    private protected void setLteRsrq(int lteRsrq) {
        this.mLteRsrq = lteRsrq;
    }

    public synchronized int getLteRssnr() {
        return this.mLteRssnr;
    }

    private protected void setLteRssnr(int lteRssnr) {
        this.mLteRssnr = lteRssnr;
    }

    public synchronized int getLteCqi() {
        return this.mLteCqi;
    }

    private protected void setLteCqi(int lteCqi) {
        this.mLteCqi = lteCqi;
    }
}
