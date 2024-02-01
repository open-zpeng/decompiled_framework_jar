package android.telephony;

import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class CellSignalStrengthWcdma extends CellSignalStrength implements Parcelable {
    public static final Parcelable.Creator<CellSignalStrengthWcdma> CREATOR = new Parcelable.Creator<CellSignalStrengthWcdma>() { // from class: android.telephony.CellSignalStrengthWcdma.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellSignalStrengthWcdma createFromParcel(Parcel in) {
            return new CellSignalStrengthWcdma(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellSignalStrengthWcdma[] newArray(int size) {
            return new CellSignalStrengthWcdma[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellSignalStrengthWcdma";
    private static final int WCDMA_SIGNAL_STRENGTH_GOOD = 8;
    private static final int WCDMA_SIGNAL_STRENGTH_GREAT = 12;
    private static final int WCDMA_SIGNAL_STRENGTH_MODERATE = 5;
    public protected int mBitErrorRate;
    public protected int mSignalStrength;

    public synchronized CellSignalStrengthWcdma() {
        setDefaultValues();
    }

    public synchronized CellSignalStrengthWcdma(int ss, int ber) {
        this.mSignalStrength = ss;
        this.mBitErrorRate = ber;
    }

    public synchronized CellSignalStrengthWcdma(CellSignalStrengthWcdma s) {
        copyFrom(s);
    }

    protected synchronized void copyFrom(CellSignalStrengthWcdma s) {
        this.mSignalStrength = s.mSignalStrength;
        this.mBitErrorRate = s.mBitErrorRate;
    }

    @Override // android.telephony.CellSignalStrength
    public synchronized CellSignalStrengthWcdma copy() {
        return new CellSignalStrengthWcdma(this);
    }

    @Override // android.telephony.CellSignalStrength
    public synchronized void setDefaultValues() {
        this.mSignalStrength = Integer.MAX_VALUE;
        this.mBitErrorRate = Integer.MAX_VALUE;
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
        return Objects.hash(Integer.valueOf(this.mSignalStrength), Integer.valueOf(this.mBitErrorRate));
    }

    @Override // android.telephony.CellSignalStrength
    public boolean equals(Object o) {
        try {
            CellSignalStrengthWcdma s = (CellSignalStrengthWcdma) o;
            return o != null && this.mSignalStrength == s.mSignalStrength && this.mBitErrorRate == s.mBitErrorRate;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        return "CellSignalStrengthWcdma: ss=" + this.mSignalStrength + " ber=" + this.mBitErrorRate;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSignalStrength);
        dest.writeInt(this.mBitErrorRate);
    }

    private synchronized CellSignalStrengthWcdma(Parcel in) {
        this.mSignalStrength = in.readInt();
        this.mBitErrorRate = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private static synchronized void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
