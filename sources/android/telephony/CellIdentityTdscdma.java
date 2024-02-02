package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class CellIdentityTdscdma extends CellIdentity {
    private static final boolean DBG = false;
    private final int mCid;
    private final int mCpid;
    private final int mLac;
    private static final String TAG = CellIdentityTdscdma.class.getSimpleName();
    public static final Parcelable.Creator<CellIdentityTdscdma> CREATOR = new Parcelable.Creator<CellIdentityTdscdma>() { // from class: android.telephony.CellIdentityTdscdma.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellIdentityTdscdma createFromParcel(Parcel in) {
            in.readInt();
            return CellIdentityTdscdma.createFromParcelBody(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellIdentityTdscdma[] newArray(int size) {
            return new CellIdentityTdscdma[size];
        }
    };

    public synchronized CellIdentityTdscdma() {
        super(TAG, 5, null, null, null, null);
        this.mLac = Integer.MAX_VALUE;
        this.mCid = Integer.MAX_VALUE;
        this.mCpid = Integer.MAX_VALUE;
    }

    public synchronized CellIdentityTdscdma(int mcc, int mnc, int lac, int cid, int cpid) {
        this(String.valueOf(mcc), String.valueOf(mnc), lac, cid, cpid, null, null);
    }

    public synchronized CellIdentityTdscdma(String mcc, String mnc, int lac, int cid, int cpid) {
        super(TAG, 5, mcc, mnc, null, null);
        this.mLac = lac;
        this.mCid = cid;
        this.mCpid = cpid;
    }

    public synchronized CellIdentityTdscdma(String mcc, String mnc, int lac, int cid, int cpid, String alphal, String alphas) {
        super(TAG, 5, mcc, mnc, alphal, alphas);
        this.mLac = lac;
        this.mCid = cid;
        this.mCpid = cpid;
    }

    private synchronized CellIdentityTdscdma(CellIdentityTdscdma cid) {
        this(cid.mMccStr, cid.mMncStr, cid.mLac, cid.mCid, cid.mCpid, cid.mAlphaLong, cid.mAlphaShort);
    }

    synchronized CellIdentityTdscdma copy() {
        return new CellIdentityTdscdma(this);
    }

    public String getMccString() {
        return this.mMccStr;
    }

    public String getMncString() {
        return this.mMncStr;
    }

    public int getLac() {
        return this.mLac;
    }

    public int getCid() {
        return this.mCid;
    }

    public int getCpid() {
        return this.mCpid;
    }

    @Override // android.telephony.CellIdentity
    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mLac), Integer.valueOf(this.mCid), Integer.valueOf(this.mCpid), Integer.valueOf(super.hashCode()));
    }

    @Override // android.telephony.CellIdentity
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof CellIdentityTdscdma) {
            CellIdentityTdscdma o = (CellIdentityTdscdma) other;
            return TextUtils.equals(this.mMccStr, o.mMccStr) && TextUtils.equals(this.mMncStr, o.mMncStr) && this.mLac == o.mLac && this.mCid == o.mCid && this.mCpid == o.mCpid && super.equals(other);
        }
        return false;
    }

    public String toString() {
        return TAG + ":{ mMcc=" + this.mMccStr + " mMnc=" + this.mMncStr + " mLac=" + this.mLac + " mCid=" + this.mCid + " mCpid=" + this.mCpid + " mAlphaLong=" + this.mAlphaLong + " mAlphaShort=" + this.mAlphaShort + "}";
    }

    @Override // android.telephony.CellIdentity, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, 5);
        dest.writeInt(this.mLac);
        dest.writeInt(this.mCid);
        dest.writeInt(this.mCpid);
    }

    private synchronized CellIdentityTdscdma(Parcel in) {
        super(TAG, 5, in);
        this.mLac = in.readInt();
        this.mCid = in.readInt();
        this.mCpid = in.readInt();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized CellIdentityTdscdma createFromParcelBody(Parcel in) {
        return new CellIdentityTdscdma(in);
    }
}
