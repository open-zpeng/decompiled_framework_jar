package android.telephony;

import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class CellSignalStrengthGsm extends CellSignalStrength implements Parcelable {
    public static final Parcelable.Creator<CellSignalStrengthGsm> CREATOR = new Parcelable.Creator<CellSignalStrengthGsm>() { // from class: android.telephony.CellSignalStrengthGsm.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellSignalStrengthGsm createFromParcel(Parcel in) {
            return new CellSignalStrengthGsm(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellSignalStrengthGsm[] newArray(int size) {
            return new CellSignalStrengthGsm[size];
        }
    };
    private static final boolean DBG = false;
    private static final int GSM_SIGNAL_STRENGTH_GOOD = 8;
    private static final int GSM_SIGNAL_STRENGTH_GREAT = 12;
    private static final int GSM_SIGNAL_STRENGTH_MODERATE = 5;
    private static final String LOG_TAG = "CellSignalStrengthGsm";
    public protected int mBitErrorRate;
    public protected int mSignalStrength;
    public protected int mTimingAdvance;

    /* JADX INFO: Access modifiers changed from: private */
    public CellSignalStrengthGsm() {
        setDefaultValues();
    }

    public synchronized CellSignalStrengthGsm(int ss, int ber) {
        this(ss, ber, Integer.MAX_VALUE);
    }

    public synchronized CellSignalStrengthGsm(int ss, int ber, int ta) {
        this.mSignalStrength = ss;
        this.mBitErrorRate = ber;
        this.mTimingAdvance = ta;
    }

    public synchronized CellSignalStrengthGsm(CellSignalStrengthGsm s) {
        copyFrom(s);
    }

    protected synchronized void copyFrom(CellSignalStrengthGsm s) {
        this.mSignalStrength = s.mSignalStrength;
        this.mBitErrorRate = s.mBitErrorRate;
        this.mTimingAdvance = s.mTimingAdvance;
    }

    @Override // android.telephony.CellSignalStrength
    public synchronized CellSignalStrengthGsm copy() {
        return new CellSignalStrengthGsm(this);
    }

    @Override // android.telephony.CellSignalStrength
    public synchronized void setDefaultValues() {
        this.mSignalStrength = Integer.MAX_VALUE;
        this.mBitErrorRate = Integer.MAX_VALUE;
        this.mTimingAdvance = Integer.MAX_VALUE;
    }

    @Override // android.telephony.CellSignalStrength
    public int getLevel() {
        int asu = this.mSignalStrength;
        if (asu <= 2 || asu == 99) {
            return 0;
        }
        if (asu >= 12) {
            return 4;
        }
        if (asu >= 8) {
            return 3;
        }
        return asu >= 5 ? 2 : 1;
    }

    public int getTimingAdvance() {
        return this.mTimingAdvance;
    }

    @Override // android.telephony.CellSignalStrength
    public int getDbm() {
        int level = this.mSignalStrength;
        int asu = level == 99 ? Integer.MAX_VALUE : level;
        if (asu == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        int dBm = PackageManager.INSTALL_FAILED_NO_MATCHING_ABIS + (2 * asu);
        return dBm;
    }

    @Override // android.telephony.CellSignalStrength
    public int getAsuLevel() {
        int level = this.mSignalStrength;
        return level;
    }

    @Override // android.telephony.CellSignalStrength
    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mSignalStrength), Integer.valueOf(this.mBitErrorRate), Integer.valueOf(this.mTimingAdvance));
    }

    @Override // android.telephony.CellSignalStrength
    public boolean equals(Object o) {
        try {
            CellSignalStrengthGsm s = (CellSignalStrengthGsm) o;
            return o != null && this.mSignalStrength == s.mSignalStrength && this.mBitErrorRate == s.mBitErrorRate && s.mTimingAdvance == this.mTimingAdvance;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        return "CellSignalStrengthGsm: ss=" + this.mSignalStrength + " ber=" + this.mBitErrorRate + " mTa=" + this.mTimingAdvance;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSignalStrength);
        dest.writeInt(this.mBitErrorRate);
        dest.writeInt(this.mTimingAdvance);
    }

    private synchronized CellSignalStrengthGsm(Parcel in) {
        this.mSignalStrength = in.readInt();
        this.mBitErrorRate = in.readInt();
        this.mTimingAdvance = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private static synchronized void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
