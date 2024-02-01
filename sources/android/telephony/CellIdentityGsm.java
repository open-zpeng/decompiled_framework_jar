package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class CellIdentityGsm extends CellIdentity {
    private static final boolean DBG = false;
    public protected final int mArfcn;
    public protected final int mBsic;
    private final int mCid;
    private final int mLac;
    private static final String TAG = CellIdentityGsm.class.getSimpleName();
    public static final Parcelable.Creator<CellIdentityGsm> CREATOR = new Parcelable.Creator<CellIdentityGsm>() { // from class: android.telephony.CellIdentityGsm.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellIdentityGsm createFromParcel(Parcel in) {
            in.readInt();
            return CellIdentityGsm.createFromParcelBody(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellIdentityGsm[] newArray(int size) {
            return new CellIdentityGsm[size];
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public CellIdentityGsm() {
        super(TAG, 1, null, null, null, null);
        this.mLac = Integer.MAX_VALUE;
        this.mCid = Integer.MAX_VALUE;
        this.mArfcn = Integer.MAX_VALUE;
        this.mBsic = Integer.MAX_VALUE;
    }

    public synchronized CellIdentityGsm(int mcc, int mnc, int lac, int cid) {
        this(lac, cid, Integer.MAX_VALUE, Integer.MAX_VALUE, String.valueOf(mcc), String.valueOf(mnc), null, null);
    }

    public synchronized CellIdentityGsm(int mcc, int mnc, int lac, int cid, int arfcn, int bsic) {
        this(lac, cid, arfcn, bsic, String.valueOf(mcc), String.valueOf(mnc), null, null);
    }

    public synchronized CellIdentityGsm(int lac, int cid, int arfcn, int bsic, String mccStr, String mncStr, String alphal, String alphas) {
        super(TAG, 1, mccStr, mncStr, alphal, alphas);
        this.mLac = lac;
        this.mCid = cid;
        this.mArfcn = arfcn;
        this.mBsic = bsic == 255 ? Integer.MAX_VALUE : bsic;
    }

    private synchronized CellIdentityGsm(CellIdentityGsm cid) {
        this(cid.mLac, cid.mCid, cid.mArfcn, cid.mBsic, cid.mMccStr, cid.mMncStr, cid.mAlphaLong, cid.mAlphaShort);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized CellIdentityGsm copy() {
        return new CellIdentityGsm(this);
    }

    @Deprecated
    public int getMcc() {
        if (this.mMccStr != null) {
            return Integer.valueOf(this.mMccStr).intValue();
        }
        return Integer.MAX_VALUE;
    }

    @Deprecated
    public int getMnc() {
        if (this.mMncStr != null) {
            return Integer.valueOf(this.mMncStr).intValue();
        }
        return Integer.MAX_VALUE;
    }

    public int getLac() {
        return this.mLac;
    }

    public int getCid() {
        return this.mCid;
    }

    public int getArfcn() {
        return this.mArfcn;
    }

    public int getBsic() {
        return this.mBsic;
    }

    public String getMobileNetworkOperator() {
        if (this.mMccStr == null || this.mMncStr == null) {
            return null;
        }
        return this.mMccStr + this.mMncStr;
    }

    public String getMccString() {
        return this.mMccStr;
    }

    public String getMncString() {
        return this.mMncStr;
    }

    @Override // android.telephony.CellIdentity
    public synchronized int getChannelNumber() {
        return this.mArfcn;
    }

    @Deprecated
    public int getPsc() {
        return Integer.MAX_VALUE;
    }

    @Override // android.telephony.CellIdentity
    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mLac), Integer.valueOf(this.mCid), Integer.valueOf(super.hashCode()));
    }

    @Override // android.telephony.CellIdentity
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof CellIdentityGsm) {
            CellIdentityGsm o = (CellIdentityGsm) other;
            return this.mLac == o.mLac && this.mCid == o.mCid && this.mArfcn == o.mArfcn && this.mBsic == o.mBsic && TextUtils.equals(this.mMccStr, o.mMccStr) && TextUtils.equals(this.mMncStr, o.mMncStr) && super.equals(other);
        }
        return false;
    }

    public String toString() {
        return TAG + ":{ mLac=" + this.mLac + " mCid=" + this.mCid + " mArfcn=" + this.mArfcn + " mBsic=0x" + Integer.toHexString(this.mBsic) + " mMcc=" + this.mMccStr + " mMnc=" + this.mMncStr + " mAlphaLong=" + this.mAlphaLong + " mAlphaShort=" + this.mAlphaShort + "}";
    }

    @Override // android.telephony.CellIdentity, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, 1);
        dest.writeInt(this.mLac);
        dest.writeInt(this.mCid);
        dest.writeInt(this.mArfcn);
        dest.writeInt(this.mBsic);
    }

    private synchronized CellIdentityGsm(Parcel in) {
        super(TAG, 1, in);
        this.mLac = in.readInt();
        this.mCid = in.readInt();
        this.mArfcn = in.readInt();
        this.mBsic = in.readInt();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized CellIdentityGsm createFromParcelBody(Parcel in) {
        return new CellIdentityGsm(in);
    }
}
