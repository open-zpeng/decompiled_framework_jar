package android.telephony;

import android.content.pm.PackageManager;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes2.dex */
public class SignalStrength implements Parcelable {
    private static final boolean DBG = false;
    public static final int INVALID = Integer.MAX_VALUE;
    private static final String LOG_TAG = "SignalStrength";
    private static final int LTE_RSRP_THRESHOLDS_NUM = 4;
    private static final int MAX_LTE_RSRP = -44;
    private static final int MAX_WCDMA_RSCP = -24;
    private static final String MEASUMENT_TYPE_RSCP = "rscp";
    private static final int MIN_LTE_RSRP = -140;
    private static final int MIN_WCDMA_RSCP = -120;
    private protected static final int NUM_SIGNAL_STRENGTH_BINS = 5;
    private protected static final int SIGNAL_STRENGTH_GOOD = 3;
    private protected static final int SIGNAL_STRENGTH_GREAT = 4;
    private protected static final int SIGNAL_STRENGTH_MODERATE = 2;
    private protected static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    private protected static final int SIGNAL_STRENGTH_POOR = 1;
    private static final int WCDMA_RSCP_THRESHOLDS_NUM = 4;
    public protected int mCdmaDbm;
    public protected int mCdmaEcio;
    public protected int mEvdoDbm;
    public protected int mEvdoEcio;
    public protected int mEvdoSnr;
    public protected int mGsmBitErrorRate;
    public protected int mGsmSignalStrength;
    private boolean mIsGsm;
    public protected int mLteCqi;
    public protected int mLteRsrp;
    public protected int mLteRsrpBoost;
    private int[] mLteRsrpThresholds;
    public protected int mLteRsrq;
    public protected int mLteRssnr;
    public protected int mLteSignalStrength;
    public protected int mTdScdmaRscp;
    private boolean mUseOnlyRsrpForLteLevel;
    private String mWcdmaDefaultSignalMeasurement;
    public protected int mWcdmaRscp;
    private int mWcdmaRscpAsu;
    private int[] mWcdmaRscpThresholds;
    private int mWcdmaSignalStrength;
    public static final String[] SIGNAL_STRENGTH_NAMES = {"none", "poor", "moderate", "good", "great"};
    private protected static final Parcelable.Creator<SignalStrength> CREATOR = new Parcelable.Creator() { // from class: android.telephony.SignalStrength.1
        @Override // android.os.Parcelable.Creator
        public SignalStrength createFromParcel(Parcel in) {
            return new SignalStrength(in);
        }

        @Override // android.os.Parcelable.Creator
        public SignalStrength[] newArray(int size) {
            return new SignalStrength[size];
        }
    };

    private protected static SignalStrength newFromBundle(Bundle m) {
        SignalStrength ret = new SignalStrength();
        ret.setFromNotifierBundle(m);
        return ret;
    }

    private protected SignalStrength() {
        this(true);
    }

    private protected SignalStrength(boolean gsmFlag) {
        this.mLteRsrpThresholds = new int[4];
        this.mWcdmaRscpThresholds = new int[4];
        this.mGsmSignalStrength = 99;
        this.mGsmBitErrorRate = -1;
        this.mCdmaDbm = -1;
        this.mCdmaEcio = -1;
        this.mEvdoDbm = -1;
        this.mEvdoEcio = -1;
        this.mEvdoSnr = -1;
        this.mLteSignalStrength = 99;
        this.mLteRsrp = Integer.MAX_VALUE;
        this.mLteRsrq = Integer.MAX_VALUE;
        this.mLteRssnr = Integer.MAX_VALUE;
        this.mLteCqi = Integer.MAX_VALUE;
        this.mTdScdmaRscp = Integer.MAX_VALUE;
        this.mWcdmaSignalStrength = 99;
        this.mWcdmaRscp = Integer.MAX_VALUE;
        this.mWcdmaRscpAsu = 255;
        this.mLteRsrpBoost = 0;
        this.mIsGsm = gsmFlag;
        this.mUseOnlyRsrpForLteLevel = false;
        this.mWcdmaDefaultSignalMeasurement = "";
        setLteRsrpThresholds(getDefaultLteRsrpThresholds());
        setWcdmaRscpThresholds(getDefaultWcdmaRscpThresholds());
    }

    public synchronized SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, int tdScdmaRscp, int wcdmaSignalStrength, int wcdmaRscpAsu, int lteRsrpBoost, boolean gsmFlag, boolean lteLevelBaseOnRsrp, String wcdmaDefaultMeasurement) {
        this.mLteRsrpThresholds = new int[4];
        this.mWcdmaRscpThresholds = new int[4];
        this.mGsmSignalStrength = gsmSignalStrength;
        this.mGsmBitErrorRate = gsmBitErrorRate;
        this.mCdmaDbm = cdmaDbm;
        this.mCdmaEcio = cdmaEcio;
        this.mEvdoDbm = evdoDbm;
        this.mEvdoEcio = evdoEcio;
        this.mEvdoSnr = evdoSnr;
        this.mLteSignalStrength = lteSignalStrength;
        this.mLteRsrp = lteRsrp;
        this.mLteRsrq = lteRsrq;
        this.mLteRssnr = lteRssnr;
        this.mLteCqi = lteCqi;
        this.mTdScdmaRscp = Integer.MAX_VALUE;
        this.mWcdmaSignalStrength = wcdmaSignalStrength;
        this.mWcdmaRscpAsu = wcdmaRscpAsu;
        this.mWcdmaRscp = wcdmaRscpAsu + MIN_WCDMA_RSCP;
        this.mLteRsrpBoost = lteRsrpBoost;
        this.mIsGsm = gsmFlag;
        this.mUseOnlyRsrpForLteLevel = lteLevelBaseOnRsrp;
        this.mWcdmaDefaultSignalMeasurement = wcdmaDefaultMeasurement;
        setLteRsrpThresholds(getDefaultLteRsrpThresholds());
        setWcdmaRscpThresholds(getDefaultWcdmaRscpThresholds());
    }

    public synchronized SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, int tdScdmaRscp) {
        this(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, lteSignalStrength, lteRsrp, lteRsrq, lteRssnr, lteCqi, tdScdmaRscp, 99, Integer.MAX_VALUE, 0, true, false, "");
    }

    public synchronized SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, int tdScdmaRscp, int wcdmaSignalStrength, int wcdmaRscp) {
        this(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, lteSignalStrength, lteRsrp, lteRsrq, lteRssnr, lteCqi, tdScdmaRscp, wcdmaSignalStrength, wcdmaRscp, 0, true, false, "");
    }

    private protected SignalStrength(SignalStrength s) {
        this.mLteRsrpThresholds = new int[4];
        this.mWcdmaRscpThresholds = new int[4];
        copyFrom(s);
    }

    public private void copyFrom(SignalStrength s) {
        this.mGsmSignalStrength = s.mGsmSignalStrength;
        this.mGsmBitErrorRate = s.mGsmBitErrorRate;
        this.mCdmaDbm = s.mCdmaDbm;
        this.mCdmaEcio = s.mCdmaEcio;
        this.mEvdoDbm = s.mEvdoDbm;
        this.mEvdoEcio = s.mEvdoEcio;
        this.mEvdoSnr = s.mEvdoSnr;
        this.mLteSignalStrength = s.mLteSignalStrength;
        this.mLteRsrp = s.mLteRsrp;
        this.mLteRsrq = s.mLteRsrq;
        this.mLteRssnr = s.mLteRssnr;
        this.mLteCqi = s.mLteCqi;
        this.mTdScdmaRscp = s.mTdScdmaRscp;
        this.mWcdmaSignalStrength = s.mWcdmaSignalStrength;
        this.mWcdmaRscpAsu = s.mWcdmaRscpAsu;
        this.mWcdmaRscp = s.mWcdmaRscp;
        this.mLteRsrpBoost = s.mLteRsrpBoost;
        this.mIsGsm = s.mIsGsm;
        this.mUseOnlyRsrpForLteLevel = s.mUseOnlyRsrpForLteLevel;
        this.mWcdmaDefaultSignalMeasurement = s.mWcdmaDefaultSignalMeasurement;
        setLteRsrpThresholds(s.mLteRsrpThresholds);
        setWcdmaRscpThresholds(s.mWcdmaRscpThresholds);
    }

    private protected SignalStrength(Parcel in) {
        this.mLteRsrpThresholds = new int[4];
        this.mWcdmaRscpThresholds = new int[4];
        this.mGsmSignalStrength = in.readInt();
        this.mGsmBitErrorRate = in.readInt();
        this.mCdmaDbm = in.readInt();
        this.mCdmaEcio = in.readInt();
        this.mEvdoDbm = in.readInt();
        this.mEvdoEcio = in.readInt();
        this.mEvdoSnr = in.readInt();
        this.mLteSignalStrength = in.readInt();
        this.mLteRsrp = in.readInt();
        this.mLteRsrq = in.readInt();
        this.mLteRssnr = in.readInt();
        this.mLteCqi = in.readInt();
        this.mTdScdmaRscp = in.readInt();
        this.mWcdmaSignalStrength = in.readInt();
        this.mWcdmaRscpAsu = in.readInt();
        this.mWcdmaRscp = in.readInt();
        this.mLteRsrpBoost = in.readInt();
        this.mIsGsm = in.readBoolean();
        this.mUseOnlyRsrpForLteLevel = in.readBoolean();
        this.mWcdmaDefaultSignalMeasurement = in.readString();
        in.readIntArray(this.mLteRsrpThresholds);
        in.readIntArray(this.mWcdmaRscpThresholds);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mGsmSignalStrength);
        out.writeInt(this.mGsmBitErrorRate);
        out.writeInt(this.mCdmaDbm);
        out.writeInt(this.mCdmaEcio);
        out.writeInt(this.mEvdoDbm);
        out.writeInt(this.mEvdoEcio);
        out.writeInt(this.mEvdoSnr);
        out.writeInt(this.mLteSignalStrength);
        out.writeInt(this.mLteRsrp);
        out.writeInt(this.mLteRsrq);
        out.writeInt(this.mLteRssnr);
        out.writeInt(this.mLteCqi);
        out.writeInt(this.mTdScdmaRscp);
        out.writeInt(this.mWcdmaSignalStrength);
        out.writeInt(this.mWcdmaRscpAsu);
        out.writeInt(this.mWcdmaRscp);
        out.writeInt(this.mLteRsrpBoost);
        out.writeBoolean(this.mIsGsm);
        out.writeBoolean(this.mUseOnlyRsrpForLteLevel);
        out.writeString(this.mWcdmaDefaultSignalMeasurement);
        out.writeIntArray(this.mLteRsrpThresholds);
        out.writeIntArray(this.mWcdmaRscpThresholds);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private protected void validateInput() {
        this.mGsmSignalStrength = this.mGsmSignalStrength >= 0 ? this.mGsmSignalStrength : 99;
        this.mWcdmaSignalStrength = this.mWcdmaSignalStrength >= 0 ? this.mWcdmaSignalStrength : 99;
        this.mLteSignalStrength = this.mLteSignalStrength >= 0 ? this.mLteSignalStrength : 99;
        int i = this.mWcdmaRscpAsu + MIN_WCDMA_RSCP;
        int i2 = MIN_WCDMA_RSCP;
        this.mWcdmaRscpAsu = (i < MIN_WCDMA_RSCP || this.mWcdmaRscpAsu + MIN_WCDMA_RSCP > -24) ? 255 : this.mWcdmaRscpAsu;
        int i3 = Integer.MAX_VALUE;
        this.mWcdmaRscp = (this.mWcdmaRscp < MIN_WCDMA_RSCP || this.mWcdmaRscp > -24) ? Integer.MAX_VALUE : this.mWcdmaRscp;
        this.mCdmaDbm = this.mCdmaDbm > 0 ? -this.mCdmaDbm : MIN_WCDMA_RSCP;
        this.mCdmaEcio = this.mCdmaEcio >= 0 ? -this.mCdmaEcio : -160;
        if (this.mEvdoDbm > 0) {
            i2 = -this.mEvdoDbm;
        }
        this.mEvdoDbm = i2;
        this.mEvdoEcio = this.mEvdoEcio >= 0 ? -this.mEvdoEcio : -160;
        this.mEvdoSnr = (this.mEvdoSnr < 0 || this.mEvdoSnr > 8) ? -1 : this.mEvdoSnr;
        this.mLteRsrp = ((-this.mLteRsrp) < MIN_LTE_RSRP || (-this.mLteRsrp) > -44) ? Integer.MAX_VALUE : -this.mLteRsrp;
        this.mLteRsrq = (this.mLteRsrq < 3 || this.mLteRsrq > 20) ? Integer.MAX_VALUE : -this.mLteRsrq;
        this.mLteRssnr = (this.mLteRssnr < -200 || this.mLteRssnr > 300) ? Integer.MAX_VALUE : this.mLteRssnr;
        if (this.mTdScdmaRscp >= 0 && this.mTdScdmaRscp <= 96) {
            i3 = this.mTdScdmaRscp + MIN_WCDMA_RSCP;
        }
        this.mTdScdmaRscp = i3;
    }

    public synchronized void fixType() {
        this.mIsGsm = getCdmaRelatedSignalStrength() == 0;
    }

    public synchronized void setGsm(boolean gsmFlag) {
        this.mIsGsm = gsmFlag;
    }

    public synchronized void setUseOnlyRsrpForLteLevel(boolean useOnlyRsrpForLteLevel) {
        this.mUseOnlyRsrpForLteLevel = useOnlyRsrpForLteLevel;
    }

    public synchronized void setWcdmaDefaultSignalMeasurement(String defaultMeasurement) {
        this.mWcdmaDefaultSignalMeasurement = defaultMeasurement;
    }

    public synchronized void setLteRsrpBoost(int lteRsrpBoost) {
        this.mLteRsrpBoost = lteRsrpBoost;
    }

    public synchronized void setLteRsrpThresholds(int[] lteRsrpThresholds) {
        if (lteRsrpThresholds == null || lteRsrpThresholds.length != 4) {
            Log.wtf(LOG_TAG, "setLteRsrpThresholds - lteRsrpThresholds is invalid.");
        } else {
            System.arraycopy(lteRsrpThresholds, 0, this.mLteRsrpThresholds, 0, 4);
        }
    }

    public int getGsmSignalStrength() {
        return this.mGsmSignalStrength;
    }

    public int getGsmBitErrorRate() {
        return this.mGsmBitErrorRate;
    }

    public synchronized void setWcdmaRscpThresholds(int[] wcdmaRscpThresholds) {
        if (wcdmaRscpThresholds == null || wcdmaRscpThresholds.length != 4) {
            Log.wtf(LOG_TAG, "setWcdmaRscpThresholds - wcdmaRscpThresholds is invalid.");
        } else {
            System.arraycopy(wcdmaRscpThresholds, 0, this.mWcdmaRscpThresholds, 0, 4);
        }
    }

    public int getCdmaDbm() {
        return this.mCdmaDbm;
    }

    public int getCdmaEcio() {
        return this.mCdmaEcio;
    }

    public int getEvdoDbm() {
        return this.mEvdoDbm;
    }

    public int getEvdoEcio() {
        return this.mEvdoEcio;
    }

    public int getEvdoSnr() {
        return this.mEvdoSnr;
    }

    private protected int getLteSignalStrength() {
        return this.mLteSignalStrength;
    }

    private protected int getLteRsrp() {
        return this.mLteRsrp;
    }

    private protected int getLteRsrq() {
        return this.mLteRsrq;
    }

    private protected int getLteRssnr() {
        return this.mLteRssnr;
    }

    private protected int getLteCqi() {
        return this.mLteCqi;
    }

    public synchronized int getLteRsrpBoost() {
        return this.mLteRsrpBoost;
    }

    public int getLevel() {
        if (this.mIsGsm) {
            int level = getGsmRelatedSignalStrength();
            return level;
        }
        int level2 = getCdmaRelatedSignalStrength();
        return level2;
    }

    private protected int getAsuLevel() {
        if (this.mIsGsm) {
            if (this.mLteRsrp != Integer.MAX_VALUE) {
                int asuLevel = getLteAsuLevel();
                return asuLevel;
            } else if (this.mTdScdmaRscp != Integer.MAX_VALUE) {
                int asuLevel2 = getTdScdmaAsuLevel();
                return asuLevel2;
            } else if (this.mWcdmaRscp != Integer.MAX_VALUE) {
                int asuLevel3 = getWcdmaAsuLevel();
                return asuLevel3;
            } else {
                int asuLevel4 = getGsmAsuLevel();
                return asuLevel4;
            }
        }
        int cdmaAsuLevel = getCdmaAsuLevel();
        int evdoAsuLevel = getEvdoAsuLevel();
        if (evdoAsuLevel == 0) {
            return cdmaAsuLevel;
        }
        if (cdmaAsuLevel == 0) {
            return evdoAsuLevel;
        }
        int asuLevel5 = cdmaAsuLevel < evdoAsuLevel ? cdmaAsuLevel : evdoAsuLevel;
        return asuLevel5;
    }

    private protected int getDbm() {
        if (isGsm()) {
            int dBm = getLteDbm();
            if (dBm == Integer.MAX_VALUE) {
                if (getTdScdmaLevel() == 0) {
                    if (getWcdmaDbm() == Integer.MAX_VALUE) {
                        return getGsmDbm();
                    }
                    return getWcdmaDbm();
                }
                return getTdScdmaDbm();
            }
            return dBm;
        }
        int cdmaDbm = getCdmaDbm();
        int evdoDbm = getEvdoDbm();
        return (evdoDbm != MIN_WCDMA_RSCP && (cdmaDbm == MIN_WCDMA_RSCP || cdmaDbm >= evdoDbm)) ? evdoDbm : cdmaDbm;
    }

    private protected int getGsmDbm() {
        int gsmSignalStrength = getGsmSignalStrength();
        int asu = gsmSignalStrength == 99 ? -1 : gsmSignalStrength;
        if (asu == -1) {
            return -1;
        }
        int dBm = PackageManager.INSTALL_FAILED_NO_MATCHING_ABIS + (2 * asu);
        return dBm;
    }

    private protected int getGsmLevel() {
        int asu = getGsmSignalStrength();
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

    private protected int getGsmAsuLevel() {
        int level = getGsmSignalStrength();
        return level;
    }

    private protected int getCdmaLevel() {
        int levelDbm;
        int cdmaDbm = getCdmaDbm();
        int cdmaEcio = getCdmaEcio();
        int levelEcio = 0;
        if (cdmaDbm >= -75) {
            levelDbm = 4;
        } else if (cdmaDbm >= -85) {
            levelDbm = 3;
        } else if (cdmaDbm >= -95) {
            levelDbm = 2;
        } else {
            levelDbm = cdmaDbm >= -100 ? 1 : 0;
        }
        if (cdmaEcio >= -90) {
            levelEcio = 4;
        } else if (cdmaEcio >= -110) {
            levelEcio = 3;
        } else if (cdmaEcio >= -130) {
            levelEcio = 2;
        } else if (cdmaEcio >= -150) {
            levelEcio = 1;
        }
        if (levelDbm < levelEcio) {
            int level = levelDbm;
            return level;
        }
        int level2 = levelEcio;
        return level2;
    }

    private protected int getCdmaAsuLevel() {
        int cdmaAsuLevel;
        int cdmaDbm = getCdmaDbm();
        int cdmaEcio = getCdmaEcio();
        int ecioAsuLevel = 99;
        if (cdmaDbm >= -75) {
            cdmaAsuLevel = 16;
        } else if (cdmaDbm >= -82) {
            cdmaAsuLevel = 8;
        } else if (cdmaDbm >= -90) {
            cdmaAsuLevel = 4;
        } else if (cdmaDbm >= -95) {
            cdmaAsuLevel = 2;
        } else {
            cdmaAsuLevel = cdmaDbm >= -100 ? 1 : 99;
        }
        if (cdmaEcio >= -90) {
            ecioAsuLevel = 16;
        } else if (cdmaEcio >= -100) {
            ecioAsuLevel = 8;
        } else if (cdmaEcio >= -115) {
            ecioAsuLevel = 4;
        } else if (cdmaEcio >= -130) {
            ecioAsuLevel = 2;
        } else if (cdmaEcio >= -150) {
            ecioAsuLevel = 1;
        }
        if (cdmaAsuLevel < ecioAsuLevel) {
            int level = cdmaAsuLevel;
            return level;
        }
        int level2 = ecioAsuLevel;
        return level2;
    }

    private protected int getEvdoLevel() {
        int levelEvdoDbm;
        int evdoDbm = getEvdoDbm();
        int evdoSnr = getEvdoSnr();
        int levelEvdoSnr = 0;
        if (evdoDbm >= -65) {
            levelEvdoDbm = 4;
        } else if (evdoDbm >= -75) {
            levelEvdoDbm = 3;
        } else if (evdoDbm >= -90) {
            levelEvdoDbm = 2;
        } else {
            levelEvdoDbm = evdoDbm >= -105 ? 1 : 0;
        }
        if (evdoSnr >= 7) {
            levelEvdoSnr = 4;
        } else if (evdoSnr >= 5) {
            levelEvdoSnr = 3;
        } else if (evdoSnr >= 3) {
            levelEvdoSnr = 2;
        } else if (evdoSnr >= 1) {
            levelEvdoSnr = 1;
        }
        if (levelEvdoDbm < levelEvdoSnr) {
            int level = levelEvdoDbm;
            return level;
        }
        int level2 = levelEvdoSnr;
        return level2;
    }

    private protected int getEvdoAsuLevel() {
        int levelEvdoDbm;
        int evdoDbm = getEvdoDbm();
        int evdoSnr = getEvdoSnr();
        int levelEvdoSnr = 99;
        if (evdoDbm >= -65) {
            levelEvdoDbm = 16;
        } else if (evdoDbm >= -75) {
            levelEvdoDbm = 8;
        } else if (evdoDbm >= -85) {
            levelEvdoDbm = 4;
        } else if (evdoDbm >= -95) {
            levelEvdoDbm = 2;
        } else {
            levelEvdoDbm = evdoDbm >= -105 ? 1 : 99;
        }
        if (evdoSnr >= 7) {
            levelEvdoSnr = 16;
        } else if (evdoSnr >= 6) {
            levelEvdoSnr = 8;
        } else if (evdoSnr >= 5) {
            levelEvdoSnr = 4;
        } else if (evdoSnr >= 3) {
            levelEvdoSnr = 2;
        } else if (evdoSnr >= 1) {
            levelEvdoSnr = 1;
        }
        if (levelEvdoDbm < levelEvdoSnr) {
            int level = levelEvdoDbm;
            return level;
        }
        int level2 = levelEvdoSnr;
        return level2;
    }

    private protected int getLteDbm() {
        return this.mLteRsrp;
    }

    private protected int getLteLevel() {
        int rsrpIconLevel = -1;
        int snrIconLevel = -1;
        if (this.mLteRsrp > -44 || this.mLteRsrp < MIN_LTE_RSRP) {
            if (this.mLteRsrp != Integer.MAX_VALUE) {
                Log.wtf(LOG_TAG, "getLteLevel - invalid lte rsrp: mLteRsrp=" + this.mLteRsrp);
            }
        } else if (this.mLteRsrp >= this.mLteRsrpThresholds[3] - this.mLteRsrpBoost) {
            rsrpIconLevel = 4;
        } else if (this.mLteRsrp >= this.mLteRsrpThresholds[2] - this.mLteRsrpBoost) {
            rsrpIconLevel = 3;
        } else if (this.mLteRsrp >= this.mLteRsrpThresholds[1] - this.mLteRsrpBoost) {
            rsrpIconLevel = 2;
        } else if (this.mLteRsrp >= this.mLteRsrpThresholds[0] - this.mLteRsrpBoost) {
            rsrpIconLevel = 1;
        } else {
            rsrpIconLevel = 0;
        }
        if (useOnlyRsrpForLteLevel()) {
            log("getLTELevel - rsrp = " + rsrpIconLevel);
            if (rsrpIconLevel != -1) {
                return rsrpIconLevel;
            }
        }
        if (this.mLteRssnr > 300) {
            snrIconLevel = -1;
        } else if (this.mLteRssnr >= 130) {
            snrIconLevel = 4;
        } else if (this.mLteRssnr >= 45) {
            snrIconLevel = 3;
        } else if (this.mLteRssnr >= 10) {
            snrIconLevel = 2;
        } else if (this.mLteRssnr >= -30) {
            snrIconLevel = 1;
        } else if (this.mLteRssnr >= -200) {
            snrIconLevel = 0;
        }
        if (snrIconLevel != -1 && rsrpIconLevel != -1) {
            return rsrpIconLevel < snrIconLevel ? rsrpIconLevel : snrIconLevel;
        } else if (snrIconLevel != -1) {
            return snrIconLevel;
        } else {
            if (rsrpIconLevel != -1) {
                return rsrpIconLevel;
            }
            if (this.mLteSignalStrength > 63) {
                return 0;
            }
            if (this.mLteSignalStrength >= 12) {
                return 4;
            }
            if (this.mLteSignalStrength >= 8) {
                return 3;
            }
            if (this.mLteSignalStrength >= 5) {
                return 2;
            }
            return this.mLteSignalStrength >= 0 ? 1 : 0;
        }
    }

    private protected int getLteAsuLevel() {
        int lteDbm = getLteDbm();
        if (lteDbm == Integer.MAX_VALUE) {
            return 255;
        }
        int lteAsuLevel = lteDbm + 140;
        return lteAsuLevel;
    }

    public boolean isGsm() {
        return this.mIsGsm;
    }

    public synchronized boolean useOnlyRsrpForLteLevel() {
        return this.mUseOnlyRsrpForLteLevel;
    }

    private protected int getTdScdmaDbm() {
        return this.mTdScdmaRscp;
    }

    private protected int getTdScdmaLevel() {
        int tdScdmaDbm = getTdScdmaDbm();
        if (tdScdmaDbm > -25 || tdScdmaDbm == Integer.MAX_VALUE) {
            return 0;
        }
        if (tdScdmaDbm >= -49) {
            return 4;
        }
        if (tdScdmaDbm >= -73) {
            return 3;
        }
        if (tdScdmaDbm >= -97) {
            return 2;
        }
        return tdScdmaDbm >= -110 ? 1 : 0;
    }

    private protected int getTdScdmaAsuLevel() {
        int tdScdmaDbm = getTdScdmaDbm();
        if (tdScdmaDbm == Integer.MAX_VALUE) {
            return 255;
        }
        int tdScdmaAsuLevel = tdScdmaDbm + 120;
        return tdScdmaAsuLevel;
    }

    public synchronized int getWcdmaRscp() {
        return this.mWcdmaRscp;
    }

    public synchronized int getWcdmaAsuLevel() {
        int wcdmaDbm = getWcdmaDbm();
        if (wcdmaDbm == Integer.MAX_VALUE) {
            return 255;
        }
        int wcdmaAsuLevel = wcdmaDbm + 120;
        return wcdmaAsuLevel;
    }

    public synchronized int getWcdmaDbm() {
        return this.mWcdmaRscp;
    }

    public synchronized int getWcdmaLevel() {
        if (this.mWcdmaDefaultSignalMeasurement == null) {
            Log.wtf(LOG_TAG, "getWcdmaLevel - WCDMA default signal measurement is invalid.");
            return 0;
        }
        String str = this.mWcdmaDefaultSignalMeasurement;
        char c = 65535;
        if (str.hashCode() == 3509870 && str.equals(MEASUMENT_TYPE_RSCP)) {
            c = 0;
        }
        if (c == 0) {
            if (this.mWcdmaRscp < MIN_WCDMA_RSCP || this.mWcdmaRscp > -24) {
                if (this.mWcdmaRscp == Integer.MAX_VALUE) {
                    return 0;
                }
                Log.wtf(LOG_TAG, "getWcdmaLevel - invalid WCDMA RSCP: mWcdmaRscp=" + this.mWcdmaRscp);
                return 0;
            } else if (this.mWcdmaRscp >= this.mWcdmaRscpThresholds[3]) {
                return 4;
            } else {
                if (this.mWcdmaRscp >= this.mWcdmaRscpThresholds[2]) {
                    return 3;
                }
                if (this.mWcdmaRscp >= this.mWcdmaRscpThresholds[1]) {
                    return 2;
                }
                if (this.mWcdmaRscp < this.mWcdmaRscpThresholds[0]) {
                    return 0;
                }
                return 1;
            }
        } else if (this.mWcdmaSignalStrength < 0 || this.mWcdmaSignalStrength > 31) {
            if (this.mWcdmaSignalStrength == 99) {
                return 0;
            }
            Log.wtf(LOG_TAG, "getWcdmaLevel - invalid WCDMA RSSI: mWcdmaSignalStrength=" + this.mWcdmaSignalStrength);
            return 0;
        } else if (this.mWcdmaSignalStrength >= 18) {
            return 4;
        } else {
            if (this.mWcdmaSignalStrength >= 13) {
                return 3;
            }
            if (this.mWcdmaSignalStrength >= 8) {
                return 2;
            }
            if (this.mWcdmaSignalStrength < 3) {
                return 0;
            }
            return 1;
        }
    }

    public int hashCode() {
        return (this.mGsmSignalStrength * 31) + (this.mGsmBitErrorRate * 31) + (this.mCdmaDbm * 31) + (this.mCdmaEcio * 31) + (this.mEvdoDbm * 31) + (this.mEvdoEcio * 31) + (this.mEvdoSnr * 31) + (this.mLteSignalStrength * 31) + (this.mLteRsrp * 31) + (this.mLteRsrq * 31) + (this.mLteRssnr * 31) + (this.mLteCqi * 31) + (this.mLteRsrpBoost * 31) + (this.mTdScdmaRscp * 31) + (this.mWcdmaSignalStrength * 31) + (this.mWcdmaRscpAsu * 31) + (this.mWcdmaRscp * 31) + (this.mIsGsm ? 1 : 0) + (this.mUseOnlyRsrpForLteLevel ? 1 : 0) + Objects.hashCode(this.mWcdmaDefaultSignalMeasurement) + Arrays.hashCode(this.mLteRsrpThresholds) + Arrays.hashCode(this.mWcdmaRscpThresholds);
    }

    public boolean equals(Object o) {
        try {
            SignalStrength s = (SignalStrength) o;
            return o != null && this.mGsmSignalStrength == s.mGsmSignalStrength && this.mGsmBitErrorRate == s.mGsmBitErrorRate && this.mCdmaDbm == s.mCdmaDbm && this.mCdmaEcio == s.mCdmaEcio && this.mEvdoDbm == s.mEvdoDbm && this.mEvdoEcio == s.mEvdoEcio && this.mEvdoSnr == s.mEvdoSnr && this.mLteSignalStrength == s.mLteSignalStrength && this.mLteRsrp == s.mLteRsrp && this.mLteRsrq == s.mLteRsrq && this.mLteRssnr == s.mLteRssnr && this.mLteCqi == s.mLteCqi && this.mLteRsrpBoost == s.mLteRsrpBoost && this.mTdScdmaRscp == s.mTdScdmaRscp && this.mWcdmaSignalStrength == s.mWcdmaSignalStrength && this.mWcdmaRscpAsu == s.mWcdmaRscpAsu && this.mWcdmaRscp == s.mWcdmaRscp && this.mIsGsm == s.mIsGsm && this.mUseOnlyRsrpForLteLevel == s.mUseOnlyRsrpForLteLevel && Objects.equals(this.mWcdmaDefaultSignalMeasurement, s.mWcdmaDefaultSignalMeasurement) && Arrays.equals(this.mLteRsrpThresholds, s.mLteRsrpThresholds) && Arrays.equals(this.mWcdmaRscpThresholds, s.mWcdmaRscpThresholds);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SignalStrength: ");
        sb.append(this.mGsmSignalStrength);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mGsmBitErrorRate);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mCdmaDbm);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mCdmaEcio);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mEvdoDbm);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mEvdoEcio);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mEvdoSnr);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mLteSignalStrength);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mLteRsrp);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mLteRsrq);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mLteRssnr);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mLteCqi);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mLteRsrpBoost);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mTdScdmaRscp);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mWcdmaSignalStrength);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mWcdmaRscpAsu);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mWcdmaRscp);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mIsGsm ? "gsm|lte" : "cdma");
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mUseOnlyRsrpForLteLevel ? "use_only_rsrp_for_lte_level" : "use_rsrp_and_rssnr_for_lte_level");
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mWcdmaDefaultSignalMeasurement);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(Arrays.toString(this.mLteRsrpThresholds));
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(Arrays.toString(this.mWcdmaRscpThresholds));
        return sb.toString();
    }

    private synchronized int getGsmRelatedSignalStrength() {
        int level = getLteLevel();
        if (level == 0) {
            int level2 = getTdScdmaLevel();
            if (level2 == 0) {
                int level3 = getWcdmaLevel();
                if (level3 == 0) {
                    return getGsmLevel();
                }
                return level3;
            }
            return level2;
        }
        return level;
    }

    private synchronized int getCdmaRelatedSignalStrength() {
        int cdmaLevel = getCdmaLevel();
        int evdoLevel = getEvdoLevel();
        if (evdoLevel == 0) {
            return cdmaLevel;
        }
        if (cdmaLevel == 0) {
            return evdoLevel;
        }
        return cdmaLevel < evdoLevel ? cdmaLevel : evdoLevel;
    }

    public protected void setFromNotifierBundle(Bundle m) {
        this.mGsmSignalStrength = m.getInt("GsmSignalStrength");
        this.mGsmBitErrorRate = m.getInt("GsmBitErrorRate");
        this.mCdmaDbm = m.getInt("CdmaDbm");
        this.mCdmaEcio = m.getInt("CdmaEcio");
        this.mEvdoDbm = m.getInt("EvdoDbm");
        this.mEvdoEcio = m.getInt("EvdoEcio");
        this.mEvdoSnr = m.getInt("EvdoSnr");
        this.mLteSignalStrength = m.getInt("LteSignalStrength");
        this.mLteRsrp = m.getInt("LteRsrp");
        this.mLteRsrq = m.getInt("LteRsrq");
        this.mLteRssnr = m.getInt("LteRssnr");
        this.mLteCqi = m.getInt("LteCqi");
        this.mLteRsrpBoost = m.getInt("LteRsrpBoost");
        this.mTdScdmaRscp = m.getInt("TdScdma");
        this.mWcdmaSignalStrength = m.getInt("WcdmaSignalStrength");
        this.mWcdmaRscpAsu = m.getInt("WcdmaRscpAsu");
        this.mWcdmaRscp = m.getInt("WcdmaRscp");
        this.mIsGsm = m.getBoolean("IsGsm");
        this.mUseOnlyRsrpForLteLevel = m.getBoolean("UseOnlyRsrpForLteLevel");
        this.mWcdmaDefaultSignalMeasurement = m.getString("WcdmaDefaultSignalMeasurement");
        ArrayList<Integer> lteRsrpThresholds = m.getIntegerArrayList("lteRsrpThresholds");
        for (int i = 0; i < lteRsrpThresholds.size(); i++) {
            this.mLteRsrpThresholds[i] = lteRsrpThresholds.get(i).intValue();
        }
        ArrayList<Integer> wcdmaRscpThresholds = m.getIntegerArrayList("wcdmaRscpThresholds");
        for (int i2 = 0; i2 < wcdmaRscpThresholds.size(); i2++) {
            this.mWcdmaRscpThresholds[i2] = wcdmaRscpThresholds.get(i2).intValue();
        }
    }

    private protected void fillInNotifierBundle(Bundle m) {
        int[] iArr;
        int[] iArr2;
        m.putInt("GsmSignalStrength", this.mGsmSignalStrength);
        m.putInt("GsmBitErrorRate", this.mGsmBitErrorRate);
        m.putInt("CdmaDbm", this.mCdmaDbm);
        m.putInt("CdmaEcio", this.mCdmaEcio);
        m.putInt("EvdoDbm", this.mEvdoDbm);
        m.putInt("EvdoEcio", this.mEvdoEcio);
        m.putInt("EvdoSnr", this.mEvdoSnr);
        m.putInt("LteSignalStrength", this.mLteSignalStrength);
        m.putInt("LteRsrp", this.mLteRsrp);
        m.putInt("LteRsrq", this.mLteRsrq);
        m.putInt("LteRssnr", this.mLteRssnr);
        m.putInt("LteCqi", this.mLteCqi);
        m.putInt("LteRsrpBoost", this.mLteRsrpBoost);
        m.putInt("TdScdma", this.mTdScdmaRscp);
        m.putInt("WcdmaSignalStrength", this.mWcdmaSignalStrength);
        m.putInt("WcdmaRscpAsu", this.mWcdmaRscpAsu);
        m.putInt("WcdmaRscp", this.mWcdmaRscp);
        m.putBoolean("IsGsm", this.mIsGsm);
        m.putBoolean("UseOnlyRsrpForLteLevel", this.mUseOnlyRsrpForLteLevel);
        m.putString("WcdmaDefaultSignalMeasurement", this.mWcdmaDefaultSignalMeasurement);
        ArrayList<Integer> lteRsrpThresholds = new ArrayList<>();
        for (int value : this.mLteRsrpThresholds) {
            lteRsrpThresholds.add(Integer.valueOf(value));
        }
        m.putIntegerArrayList("lteRsrpThresholds", lteRsrpThresholds);
        ArrayList<Integer> wcdmaRscpThresholds = new ArrayList<>();
        for (int value2 : this.mWcdmaRscpThresholds) {
            wcdmaRscpThresholds.add(Integer.valueOf(value2));
        }
        m.putIntegerArrayList("wcdmaRscpThresholds", wcdmaRscpThresholds);
    }

    private synchronized int[] getDefaultLteRsrpThresholds() {
        return CarrierConfigManager.getDefaultConfig().getIntArray(CarrierConfigManager.KEY_LTE_RSRP_THRESHOLDS_INT_ARRAY);
    }

    private synchronized int[] getDefaultWcdmaRscpThresholds() {
        return CarrierConfigManager.getDefaultConfig().getIntArray(CarrierConfigManager.KEY_WCDMA_RSCP_THRESHOLDS_INT_ARRAY);
    }

    private static synchronized void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
