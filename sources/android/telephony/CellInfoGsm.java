package android.telephony;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public final class CellInfoGsm extends CellInfo implements Parcelable {
    public static final Parcelable.Creator<CellInfoGsm> CREATOR = new Parcelable.Creator<CellInfoGsm>() { // from class: android.telephony.CellInfoGsm.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellInfoGsm createFromParcel(Parcel in) {
            in.readInt();
            return CellInfoGsm.createFromParcelBody(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellInfoGsm[] newArray(int size) {
            return new CellInfoGsm[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellInfoGsm";
    private CellIdentityGsm mCellIdentityGsm;
    private CellSignalStrengthGsm mCellSignalStrengthGsm;

    private protected CellInfoGsm() {
        this.mCellIdentityGsm = new CellIdentityGsm();
        this.mCellSignalStrengthGsm = new CellSignalStrengthGsm();
    }

    public synchronized CellInfoGsm(CellInfoGsm ci) {
        super(ci);
        this.mCellIdentityGsm = ci.mCellIdentityGsm.copy();
        this.mCellSignalStrengthGsm = ci.mCellSignalStrengthGsm.copy();
    }

    public CellIdentityGsm getCellIdentity() {
        return this.mCellIdentityGsm;
    }

    public synchronized void setCellIdentity(CellIdentityGsm cid) {
        this.mCellIdentityGsm = cid;
    }

    public CellSignalStrengthGsm getCellSignalStrength() {
        return this.mCellSignalStrengthGsm;
    }

    public synchronized void setCellSignalStrength(CellSignalStrengthGsm css) {
        this.mCellSignalStrengthGsm = css;
    }

    @Override // android.telephony.CellInfo
    public int hashCode() {
        return super.hashCode() + this.mCellIdentityGsm.hashCode() + this.mCellSignalStrengthGsm.hashCode();
    }

    @Override // android.telephony.CellInfo
    public boolean equals(Object other) {
        if (super.equals(other)) {
            try {
                CellInfoGsm o = (CellInfoGsm) other;
                if (this.mCellIdentityGsm.equals(o.mCellIdentityGsm)) {
                    return this.mCellSignalStrengthGsm.equals(o.mCellSignalStrengthGsm);
                }
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }
        return false;
    }

    @Override // android.telephony.CellInfo
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CellInfoGsm:{");
        sb.append(super.toString());
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mCellIdentityGsm);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mCellSignalStrengthGsm);
        sb.append("}");
        return sb.toString();
    }

    @Override // android.telephony.CellInfo, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.telephony.CellInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 1);
        this.mCellIdentityGsm.writeToParcel(dest, flags);
        this.mCellSignalStrengthGsm.writeToParcel(dest, flags);
    }

    private synchronized CellInfoGsm(Parcel in) {
        super(in);
        this.mCellIdentityGsm = CellIdentityGsm.CREATOR.createFromParcel(in);
        this.mCellSignalStrengthGsm = CellSignalStrengthGsm.CREATOR.createFromParcel(in);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized CellInfoGsm createFromParcelBody(Parcel in) {
        return new CellInfoGsm(in);
    }

    private static synchronized void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
