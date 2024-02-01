package android.telephony;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.text.TextUtils;
import com.android.internal.telephony.IccCardConstants;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes2.dex */
public class ServiceState implements Parcelable {
    public static final Parcelable.Creator<ServiceState> CREATOR = new Parcelable.Creator<ServiceState>() { // from class: android.telephony.ServiceState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ServiceState createFromParcel(Parcel in) {
            return new ServiceState(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ServiceState[] newArray(int size) {
            return new ServiceState[size];
        }
    };
    static final boolean DBG = false;
    public static final int DUPLEX_MODE_FDD = 1;
    public static final int DUPLEX_MODE_TDD = 2;
    public static final int DUPLEX_MODE_UNKNOWN = 0;
    static final String LOG_TAG = "PHONE";
    private static final int NEXT_RIL_RADIO_TECHNOLOGY = 20;
    public static final int RIL_RADIO_CDMA_TECHNOLOGY_BITMASK = 6392;
    public static final int RIL_RADIO_TECHNOLOGY_1xRTT = 6;
    public static final int RIL_RADIO_TECHNOLOGY_EDGE = 2;
    public static final int RIL_RADIO_TECHNOLOGY_EHRPD = 13;
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_0 = 7;
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_A = 8;
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_B = 12;
    public static final int RIL_RADIO_TECHNOLOGY_GPRS = 1;
    public static final int RIL_RADIO_TECHNOLOGY_GSM = 16;
    public static final int RIL_RADIO_TECHNOLOGY_HSDPA = 9;
    public static final int RIL_RADIO_TECHNOLOGY_HSPA = 11;
    public static final int RIL_RADIO_TECHNOLOGY_HSPAP = 15;
    public static final int RIL_RADIO_TECHNOLOGY_HSUPA = 10;
    public static final int RIL_RADIO_TECHNOLOGY_IS95A = 4;
    public static final int RIL_RADIO_TECHNOLOGY_IS95B = 5;
    private protected static final int RIL_RADIO_TECHNOLOGY_IWLAN = 18;
    public static final int RIL_RADIO_TECHNOLOGY_LTE = 14;
    public static final int RIL_RADIO_TECHNOLOGY_LTE_CA = 19;
    public static final int RIL_RADIO_TECHNOLOGY_TD_SCDMA = 17;
    public static final int RIL_RADIO_TECHNOLOGY_UMTS = 3;
    public static final int RIL_RADIO_TECHNOLOGY_UNKNOWN = 0;
    public static final int ROAMING_TYPE_DOMESTIC = 2;
    public static final int ROAMING_TYPE_INTERNATIONAL = 3;
    public static final int ROAMING_TYPE_NOT_ROAMING = 0;
    public static final int ROAMING_TYPE_UNKNOWN = 1;
    public static final int STATE_EMERGENCY_ONLY = 2;
    public static final int STATE_IN_SERVICE = 0;
    public static final int STATE_OUT_OF_SERVICE = 1;
    public static final int STATE_POWER_OFF = 3;
    public static final int UNKNOWN_ID = -1;
    static final boolean VDBG = false;
    public protected int mCdmaDefaultRoamingIndicator;
    public protected int mCdmaEriIconIndex;
    public protected int mCdmaEriIconMode;
    public protected int mCdmaRoamingIndicator;
    private int[] mCellBandwidths;
    private int mChannelNumber;
    public protected boolean mCssIndicator;
    private String mDataOperatorAlphaLong;
    private String mDataOperatorAlphaShort;
    private String mDataOperatorNumeric;
    private int mDataRegState;
    private int mDataRoamingType;
    private boolean mIsDataRoamingFromRegistration;
    private boolean mIsEmergencyOnly;
    public protected boolean mIsManualNetworkSelection;
    public protected boolean mIsUsingCarrierAggregation;
    private int mLteEarfcnRsrpBoost;
    public protected int mNetworkId;
    private List<NetworkRegistrationState> mNetworkRegistrationStates;
    private int mRilDataRadioTechnology;
    private int mRilVoiceRadioTechnology;
    public protected int mSystemId;
    private String mVoiceOperatorAlphaLong;
    private String mVoiceOperatorAlphaShort;
    private String mVoiceOperatorNumeric;
    private int mVoiceRegState;
    private int mVoiceRoamingType;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface DuplexMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface RilRadioTechnology {
    }

    public static final synchronized String getRoamingLogString(int roamingType) {
        switch (roamingType) {
            case 0:
                return CalendarContract.CalendarCache.TIMEZONE_TYPE_HOME;
            case 1:
                return "roaming";
            case 2:
                return "Domestic Roaming";
            case 3:
                return "International Roaming";
            default:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
    }

    private protected static ServiceState newFromBundle(Bundle m) {
        ServiceState ret = new ServiceState();
        ret.setFromNotifierBundle(m);
        return ret;
    }

    public ServiceState() {
        this.mVoiceRegState = 1;
        this.mDataRegState = 1;
        this.mCellBandwidths = new int[0];
        this.mLteEarfcnRsrpBoost = 0;
        this.mNetworkRegistrationStates = new ArrayList();
    }

    public ServiceState(ServiceState s) {
        this.mVoiceRegState = 1;
        this.mDataRegState = 1;
        this.mCellBandwidths = new int[0];
        this.mLteEarfcnRsrpBoost = 0;
        this.mNetworkRegistrationStates = new ArrayList();
        copyFrom(s);
    }

    protected void copyFrom(ServiceState s) {
        this.mVoiceRegState = s.mVoiceRegState;
        this.mDataRegState = s.mDataRegState;
        this.mVoiceRoamingType = s.mVoiceRoamingType;
        this.mDataRoamingType = s.mDataRoamingType;
        this.mVoiceOperatorAlphaLong = s.mVoiceOperatorAlphaLong;
        this.mVoiceOperatorAlphaShort = s.mVoiceOperatorAlphaShort;
        this.mVoiceOperatorNumeric = s.mVoiceOperatorNumeric;
        this.mDataOperatorAlphaLong = s.mDataOperatorAlphaLong;
        this.mDataOperatorAlphaShort = s.mDataOperatorAlphaShort;
        this.mDataOperatorNumeric = s.mDataOperatorNumeric;
        this.mIsManualNetworkSelection = s.mIsManualNetworkSelection;
        this.mRilVoiceRadioTechnology = s.mRilVoiceRadioTechnology;
        this.mRilDataRadioTechnology = s.mRilDataRadioTechnology;
        this.mCssIndicator = s.mCssIndicator;
        this.mNetworkId = s.mNetworkId;
        this.mSystemId = s.mSystemId;
        this.mCdmaRoamingIndicator = s.mCdmaRoamingIndicator;
        this.mCdmaDefaultRoamingIndicator = s.mCdmaDefaultRoamingIndicator;
        this.mCdmaEriIconIndex = s.mCdmaEriIconIndex;
        this.mCdmaEriIconMode = s.mCdmaEriIconMode;
        this.mIsEmergencyOnly = s.mIsEmergencyOnly;
        this.mIsDataRoamingFromRegistration = s.mIsDataRoamingFromRegistration;
        this.mIsUsingCarrierAggregation = s.mIsUsingCarrierAggregation;
        this.mChannelNumber = s.mChannelNumber;
        this.mCellBandwidths = Arrays.copyOf(s.mCellBandwidths, s.mCellBandwidths.length);
        this.mLteEarfcnRsrpBoost = s.mLteEarfcnRsrpBoost;
        this.mNetworkRegistrationStates = new ArrayList(s.mNetworkRegistrationStates);
    }

    public ServiceState(Parcel in) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        this.mVoiceRegState = 1;
        this.mDataRegState = 1;
        this.mCellBandwidths = new int[0];
        this.mLteEarfcnRsrpBoost = 0;
        this.mNetworkRegistrationStates = new ArrayList();
        this.mVoiceRegState = in.readInt();
        this.mDataRegState = in.readInt();
        this.mVoiceRoamingType = in.readInt();
        this.mDataRoamingType = in.readInt();
        this.mVoiceOperatorAlphaLong = in.readString();
        this.mVoiceOperatorAlphaShort = in.readString();
        this.mVoiceOperatorNumeric = in.readString();
        this.mDataOperatorAlphaLong = in.readString();
        this.mDataOperatorAlphaShort = in.readString();
        this.mDataOperatorNumeric = in.readString();
        if (in.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mIsManualNetworkSelection = z;
        this.mRilVoiceRadioTechnology = in.readInt();
        this.mRilDataRadioTechnology = in.readInt();
        if (in.readInt() != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mCssIndicator = z2;
        this.mNetworkId = in.readInt();
        this.mSystemId = in.readInt();
        this.mCdmaRoamingIndicator = in.readInt();
        this.mCdmaDefaultRoamingIndicator = in.readInt();
        this.mCdmaEriIconIndex = in.readInt();
        this.mCdmaEriIconMode = in.readInt();
        if (in.readInt() != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.mIsEmergencyOnly = z3;
        if (in.readInt() != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.mIsDataRoamingFromRegistration = z4;
        this.mIsUsingCarrierAggregation = in.readInt() != 0;
        this.mLteEarfcnRsrpBoost = in.readInt();
        this.mNetworkRegistrationStates = new ArrayList();
        in.readList(this.mNetworkRegistrationStates, NetworkRegistrationState.class.getClassLoader());
        this.mChannelNumber = in.readInt();
        this.mCellBandwidths = in.createIntArray();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mVoiceRegState);
        out.writeInt(this.mDataRegState);
        out.writeInt(this.mVoiceRoamingType);
        out.writeInt(this.mDataRoamingType);
        out.writeString(this.mVoiceOperatorAlphaLong);
        out.writeString(this.mVoiceOperatorAlphaShort);
        out.writeString(this.mVoiceOperatorNumeric);
        out.writeString(this.mDataOperatorAlphaLong);
        out.writeString(this.mDataOperatorAlphaShort);
        out.writeString(this.mDataOperatorNumeric);
        out.writeInt(this.mIsManualNetworkSelection ? 1 : 0);
        out.writeInt(this.mRilVoiceRadioTechnology);
        out.writeInt(this.mRilDataRadioTechnology);
        out.writeInt(this.mCssIndicator ? 1 : 0);
        out.writeInt(this.mNetworkId);
        out.writeInt(this.mSystemId);
        out.writeInt(this.mCdmaRoamingIndicator);
        out.writeInt(this.mCdmaDefaultRoamingIndicator);
        out.writeInt(this.mCdmaEriIconIndex);
        out.writeInt(this.mCdmaEriIconMode);
        out.writeInt(this.mIsEmergencyOnly ? 1 : 0);
        out.writeInt(this.mIsDataRoamingFromRegistration ? 1 : 0);
        out.writeInt(this.mIsUsingCarrierAggregation ? 1 : 0);
        out.writeInt(this.mLteEarfcnRsrpBoost);
        out.writeList(this.mNetworkRegistrationStates);
        out.writeInt(this.mChannelNumber);
        out.writeIntArray(this.mCellBandwidths);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getState() {
        return getVoiceRegState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVoiceRegState() {
        return this.mVoiceRegState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDataRegState() {
        return this.mDataRegState;
    }

    public int getDuplexMode() {
        if (!isLte(this.mRilDataRadioTechnology)) {
            return 0;
        }
        int band = AccessNetworkUtils.getOperatingBandForEarfcn(this.mChannelNumber);
        return AccessNetworkUtils.getDuplexModeForEutranBand(band);
    }

    public int getChannelNumber() {
        return this.mChannelNumber;
    }

    public int[] getCellBandwidths() {
        return this.mCellBandwidths == null ? new int[0] : this.mCellBandwidths;
    }

    public boolean getRoaming() {
        return getVoiceRoaming() || getDataRoaming();
    }

    private protected boolean getVoiceRoaming() {
        return this.mVoiceRoamingType != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVoiceRoamingType() {
        return this.mVoiceRoamingType;
    }

    private protected boolean getDataRoaming() {
        return this.mDataRoamingType != 0;
    }

    private protected void setDataRoamingFromRegistration(boolean dataRoaming) {
        this.mIsDataRoamingFromRegistration = dataRoaming;
    }

    public synchronized boolean getDataRoamingFromRegistration() {
        return this.mIsDataRoamingFromRegistration;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDataRoamingType() {
        return this.mDataRoamingType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEmergencyOnly() {
        return this.mIsEmergencyOnly;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCdmaRoamingIndicator() {
        return this.mCdmaRoamingIndicator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCdmaDefaultRoamingIndicator() {
        return this.mCdmaDefaultRoamingIndicator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCdmaEriIconIndex() {
        return this.mCdmaEriIconIndex;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCdmaEriIconMode() {
        return this.mCdmaEriIconMode;
    }

    public String getOperatorAlphaLong() {
        return this.mVoiceOperatorAlphaLong;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getVoiceOperatorAlphaLong() {
        return this.mVoiceOperatorAlphaLong;
    }

    public synchronized String getDataOperatorAlphaLong() {
        return this.mDataOperatorAlphaLong;
    }

    public String getOperatorAlphaShort() {
        return this.mVoiceOperatorAlphaShort;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getVoiceOperatorAlphaShort() {
        return this.mVoiceOperatorAlphaShort;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDataOperatorAlphaShort() {
        return this.mDataOperatorAlphaShort;
    }

    public synchronized String getOperatorAlpha() {
        if (TextUtils.isEmpty(this.mVoiceOperatorAlphaLong)) {
            return this.mVoiceOperatorAlphaShort;
        }
        return this.mVoiceOperatorAlphaLong;
    }

    public String getOperatorNumeric() {
        return this.mVoiceOperatorNumeric;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getVoiceOperatorNumeric() {
        return this.mVoiceOperatorNumeric;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDataOperatorNumeric() {
        return this.mDataOperatorNumeric;
    }

    public boolean getIsManualSelection() {
        return this.mIsManualNetworkSelection;
    }

    public int hashCode() {
        return (this.mVoiceRegState * 31) + (this.mDataRegState * 37) + this.mVoiceRoamingType + this.mDataRoamingType + this.mChannelNumber + Arrays.hashCode(this.mCellBandwidths) + (this.mIsManualNetworkSelection ? 1 : 0) + (this.mVoiceOperatorAlphaLong == null ? 0 : this.mVoiceOperatorAlphaLong.hashCode()) + (this.mVoiceOperatorAlphaShort == null ? 0 : this.mVoiceOperatorAlphaShort.hashCode()) + (this.mVoiceOperatorNumeric == null ? 0 : this.mVoiceOperatorNumeric.hashCode()) + (this.mDataOperatorAlphaLong == null ? 0 : this.mDataOperatorAlphaLong.hashCode()) + (this.mDataOperatorAlphaShort == null ? 0 : this.mDataOperatorAlphaShort.hashCode()) + (this.mDataOperatorNumeric != null ? this.mDataOperatorNumeric.hashCode() : 0) + this.mCdmaRoamingIndicator + this.mCdmaDefaultRoamingIndicator + (this.mIsEmergencyOnly ? 1 : 0) + (this.mIsDataRoamingFromRegistration ? 1 : 0);
    }

    public boolean equals(Object o) {
        try {
            ServiceState s = (ServiceState) o;
            return o != null && this.mVoiceRegState == s.mVoiceRegState && this.mDataRegState == s.mDataRegState && this.mIsManualNetworkSelection == s.mIsManualNetworkSelection && this.mVoiceRoamingType == s.mVoiceRoamingType && this.mDataRoamingType == s.mDataRoamingType && this.mChannelNumber == s.mChannelNumber && Arrays.equals(this.mCellBandwidths, s.mCellBandwidths) && equalsHandlesNulls(this.mVoiceOperatorAlphaLong, s.mVoiceOperatorAlphaLong) && equalsHandlesNulls(this.mVoiceOperatorAlphaShort, s.mVoiceOperatorAlphaShort) && equalsHandlesNulls(this.mVoiceOperatorNumeric, s.mVoiceOperatorNumeric) && equalsHandlesNulls(this.mDataOperatorAlphaLong, s.mDataOperatorAlphaLong) && equalsHandlesNulls(this.mDataOperatorAlphaShort, s.mDataOperatorAlphaShort) && equalsHandlesNulls(this.mDataOperatorNumeric, s.mDataOperatorNumeric) && equalsHandlesNulls(Integer.valueOf(this.mRilVoiceRadioTechnology), Integer.valueOf(s.mRilVoiceRadioTechnology)) && equalsHandlesNulls(Integer.valueOf(this.mRilDataRadioTechnology), Integer.valueOf(s.mRilDataRadioTechnology)) && equalsHandlesNulls(Boolean.valueOf(this.mCssIndicator), Boolean.valueOf(s.mCssIndicator)) && equalsHandlesNulls(Integer.valueOf(this.mNetworkId), Integer.valueOf(s.mNetworkId)) && equalsHandlesNulls(Integer.valueOf(this.mSystemId), Integer.valueOf(s.mSystemId)) && equalsHandlesNulls(Integer.valueOf(this.mCdmaRoamingIndicator), Integer.valueOf(s.mCdmaRoamingIndicator)) && equalsHandlesNulls(Integer.valueOf(this.mCdmaDefaultRoamingIndicator), Integer.valueOf(s.mCdmaDefaultRoamingIndicator)) && this.mIsEmergencyOnly == s.mIsEmergencyOnly && this.mIsDataRoamingFromRegistration == s.mIsDataRoamingFromRegistration && this.mIsUsingCarrierAggregation == s.mIsUsingCarrierAggregation && this.mNetworkRegistrationStates.containsAll(s.mNetworkRegistrationStates);
        } catch (ClassCastException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String rilRadioTechnologyToString(int rt) {
        switch (rt) {
            case 0:
                return "Unknown";
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA-IS95A";
            case 5:
                return "CDMA-IS95B";
            case 6:
                return "1xRTT";
            case 7:
                return "EvDo-rev.0";
            case 8:
                return "EvDo-rev.A";
            case 9:
                return "HSDPA";
            case 10:
                return "HSUPA";
            case 11:
                return "HSPA";
            case 12:
                return "EvDo-rev.B";
            case 13:
                return "eHRPD";
            case 14:
                return "LTE";
            case 15:
                return "HSPAP";
            case 16:
                return "GSM";
            case 17:
                return "TD-SCDMA";
            case 18:
                return "IWLAN";
            case 19:
                return "LTE_CA";
            default:
                Rlog.w(LOG_TAG, "Unexpected radioTechnology=" + rt);
                return "Unexpected";
        }
    }

    public static synchronized String rilServiceStateToString(int serviceState) {
        switch (serviceState) {
            case 0:
                return "IN_SERVICE";
            case 1:
                return "OUT_OF_SERVICE";
            case 2:
                return "EMERGENCY_ONLY";
            case 3:
                return "POWER_OFF";
            default:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{mVoiceRegState=");
        sb.append(this.mVoiceRegState);
        sb.append("(" + rilServiceStateToString(this.mVoiceRegState) + ")");
        sb.append(", mDataRegState=");
        sb.append(this.mDataRegState);
        sb.append("(" + rilServiceStateToString(this.mDataRegState) + ")");
        sb.append(", mChannelNumber=");
        sb.append(this.mChannelNumber);
        sb.append(", duplexMode()=");
        sb.append(getDuplexMode());
        sb.append(", mCellBandwidths=");
        sb.append(Arrays.toString(this.mCellBandwidths));
        sb.append(", mVoiceRoamingType=");
        sb.append(getRoamingLogString(this.mVoiceRoamingType));
        sb.append(", mDataRoamingType=");
        sb.append(getRoamingLogString(this.mDataRoamingType));
        sb.append(", mVoiceOperatorAlphaLong=");
        sb.append(this.mVoiceOperatorAlphaLong);
        sb.append(", mVoiceOperatorAlphaShort=");
        sb.append(this.mVoiceOperatorAlphaShort);
        sb.append(", mDataOperatorAlphaLong=");
        sb.append(this.mDataOperatorAlphaLong);
        sb.append(", mDataOperatorAlphaShort=");
        sb.append(this.mDataOperatorAlphaShort);
        sb.append(", isManualNetworkSelection=");
        sb.append(this.mIsManualNetworkSelection);
        sb.append(this.mIsManualNetworkSelection ? "(manual)" : "(automatic)");
        sb.append(", mRilVoiceRadioTechnology=");
        sb.append(this.mRilVoiceRadioTechnology);
        sb.append("(" + rilRadioTechnologyToString(this.mRilVoiceRadioTechnology) + ")");
        sb.append(", mRilDataRadioTechnology=");
        sb.append(this.mRilDataRadioTechnology);
        sb.append("(" + rilRadioTechnologyToString(this.mRilDataRadioTechnology) + ")");
        sb.append(", mCssIndicator=");
        sb.append(this.mCssIndicator ? "supported" : "unsupported");
        sb.append(", mNetworkId=");
        sb.append(this.mNetworkId);
        sb.append(", mSystemId=");
        sb.append(this.mSystemId);
        sb.append(", mCdmaRoamingIndicator=");
        sb.append(this.mCdmaRoamingIndicator);
        sb.append(", mCdmaDefaultRoamingIndicator=");
        sb.append(this.mCdmaDefaultRoamingIndicator);
        sb.append(", mIsEmergencyOnly=");
        sb.append(this.mIsEmergencyOnly);
        sb.append(", mIsDataRoamingFromRegistration=");
        sb.append(this.mIsDataRoamingFromRegistration);
        sb.append(", mIsUsingCarrierAggregation=");
        sb.append(this.mIsUsingCarrierAggregation);
        sb.append(", mLteEarfcnRsrpBoost=");
        sb.append(this.mLteEarfcnRsrpBoost);
        sb.append(", mNetworkRegistrationStates=");
        sb.append(this.mNetworkRegistrationStates);
        sb.append("}");
        return sb.toString();
    }

    private synchronized void setNullState(int state) {
        this.mVoiceRegState = state;
        this.mDataRegState = state;
        this.mVoiceRoamingType = 0;
        this.mDataRoamingType = 0;
        this.mChannelNumber = -1;
        this.mCellBandwidths = new int[0];
        this.mVoiceOperatorAlphaLong = null;
        this.mVoiceOperatorAlphaShort = null;
        this.mVoiceOperatorNumeric = null;
        this.mDataOperatorAlphaLong = null;
        this.mDataOperatorAlphaShort = null;
        this.mDataOperatorNumeric = null;
        this.mIsManualNetworkSelection = false;
        this.mRilVoiceRadioTechnology = 0;
        this.mRilDataRadioTechnology = 0;
        this.mCssIndicator = false;
        this.mNetworkId = -1;
        this.mSystemId = -1;
        this.mCdmaRoamingIndicator = -1;
        this.mCdmaDefaultRoamingIndicator = -1;
        this.mCdmaEriIconIndex = -1;
        this.mCdmaEriIconMode = -1;
        this.mIsEmergencyOnly = false;
        this.mIsDataRoamingFromRegistration = false;
        this.mIsUsingCarrierAggregation = false;
        this.mLteEarfcnRsrpBoost = 0;
        this.mNetworkRegistrationStates = new ArrayList();
    }

    public void setStateOutOfService() {
        setNullState(1);
    }

    public void setStateOff() {
        setNullState(3);
    }

    public void setState(int state) {
        setVoiceRegState(state);
    }

    private protected void setVoiceRegState(int state) {
        this.mVoiceRegState = state;
    }

    private protected void setDataRegState(int state) {
        this.mDataRegState = state;
    }

    public void setCellBandwidths(int[] bandwidths) {
        this.mCellBandwidths = bandwidths;
    }

    public void setChannelNumber(int channelNumber) {
        this.mChannelNumber = channelNumber;
    }

    public void setRoaming(boolean roaming) {
        this.mVoiceRoamingType = roaming ? 1 : 0;
        this.mDataRoamingType = this.mVoiceRoamingType;
    }

    private protected void setVoiceRoaming(boolean roaming) {
        this.mVoiceRoamingType = roaming ? 1 : 0;
    }

    private protected void setVoiceRoamingType(int type) {
        this.mVoiceRoamingType = type;
    }

    private protected void setDataRoaming(boolean dataRoaming) {
        this.mDataRoamingType = dataRoaming ? 1 : 0;
    }

    private protected void setDataRoamingType(int type) {
        this.mDataRoamingType = type;
    }

    private protected void setEmergencyOnly(boolean emergencyOnly) {
        this.mIsEmergencyOnly = emergencyOnly;
    }

    private protected void setCdmaRoamingIndicator(int roaming) {
        this.mCdmaRoamingIndicator = roaming;
    }

    private protected void setCdmaDefaultRoamingIndicator(int roaming) {
        this.mCdmaDefaultRoamingIndicator = roaming;
    }

    private protected void setCdmaEriIconIndex(int index) {
        this.mCdmaEriIconIndex = index;
    }

    private protected void setCdmaEriIconMode(int mode) {
        this.mCdmaEriIconMode = mode;
    }

    public void setOperatorName(String longName, String shortName, String numeric) {
        this.mVoiceOperatorAlphaLong = longName;
        this.mVoiceOperatorAlphaShort = shortName;
        this.mVoiceOperatorNumeric = numeric;
        this.mDataOperatorAlphaLong = longName;
        this.mDataOperatorAlphaShort = shortName;
        this.mDataOperatorNumeric = numeric;
    }

    public synchronized void setVoiceOperatorName(String longName, String shortName, String numeric) {
        this.mVoiceOperatorAlphaLong = longName;
        this.mVoiceOperatorAlphaShort = shortName;
        this.mVoiceOperatorNumeric = numeric;
    }

    public synchronized void setDataOperatorName(String longName, String shortName, String numeric) {
        this.mDataOperatorAlphaLong = longName;
        this.mDataOperatorAlphaShort = shortName;
        this.mDataOperatorNumeric = numeric;
    }

    private protected void setOperatorAlphaLong(String longName) {
        this.mVoiceOperatorAlphaLong = longName;
        this.mDataOperatorAlphaLong = longName;
    }

    public synchronized void setVoiceOperatorAlphaLong(String longName) {
        this.mVoiceOperatorAlphaLong = longName;
    }

    public synchronized void setDataOperatorAlphaLong(String longName) {
        this.mDataOperatorAlphaLong = longName;
    }

    public void setIsManualSelection(boolean isManual) {
        this.mIsManualNetworkSelection = isManual;
    }

    public protected static boolean equalsHandlesNulls(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    public protected void setFromNotifierBundle(Bundle m) {
        this.mVoiceRegState = m.getInt("voiceRegState");
        this.mDataRegState = m.getInt("dataRegState");
        this.mVoiceRoamingType = m.getInt("voiceRoamingType");
        this.mDataRoamingType = m.getInt("dataRoamingType");
        this.mVoiceOperatorAlphaLong = m.getString("operator-alpha-long");
        this.mVoiceOperatorAlphaShort = m.getString("operator-alpha-short");
        this.mVoiceOperatorNumeric = m.getString("operator-numeric");
        this.mDataOperatorAlphaLong = m.getString("data-operator-alpha-long");
        this.mDataOperatorAlphaShort = m.getString("data-operator-alpha-short");
        this.mDataOperatorNumeric = m.getString("data-operator-numeric");
        this.mIsManualNetworkSelection = m.getBoolean("manual");
        this.mRilVoiceRadioTechnology = m.getInt("radioTechnology");
        this.mRilDataRadioTechnology = m.getInt("dataRadioTechnology");
        this.mCssIndicator = m.getBoolean("cssIndicator");
        this.mNetworkId = m.getInt("networkId");
        this.mSystemId = m.getInt("systemId");
        this.mCdmaRoamingIndicator = m.getInt("cdmaRoamingIndicator");
        this.mCdmaDefaultRoamingIndicator = m.getInt("cdmaDefaultRoamingIndicator");
        this.mIsEmergencyOnly = m.getBoolean("emergencyOnly");
        this.mIsDataRoamingFromRegistration = m.getBoolean("isDataRoamingFromRegistration");
        this.mIsUsingCarrierAggregation = m.getBoolean("isUsingCarrierAggregation");
        this.mLteEarfcnRsrpBoost = m.getInt("LteEarfcnRsrpBoost");
        this.mChannelNumber = m.getInt("ChannelNumber");
        this.mCellBandwidths = m.getIntArray("CellBandwidths");
    }

    private protected void fillInNotifierBundle(Bundle m) {
        m.putInt("voiceRegState", this.mVoiceRegState);
        m.putInt("dataRegState", this.mDataRegState);
        m.putInt("voiceRoamingType", this.mVoiceRoamingType);
        m.putInt("dataRoamingType", this.mDataRoamingType);
        m.putString("operator-alpha-long", this.mVoiceOperatorAlphaLong);
        m.putString("operator-alpha-short", this.mVoiceOperatorAlphaShort);
        m.putString("operator-numeric", this.mVoiceOperatorNumeric);
        m.putString("data-operator-alpha-long", this.mDataOperatorAlphaLong);
        m.putString("data-operator-alpha-short", this.mDataOperatorAlphaShort);
        m.putString("data-operator-numeric", this.mDataOperatorNumeric);
        m.putBoolean("manual", this.mIsManualNetworkSelection);
        m.putInt("radioTechnology", this.mRilVoiceRadioTechnology);
        m.putInt("dataRadioTechnology", this.mRilDataRadioTechnology);
        m.putBoolean("cssIndicator", this.mCssIndicator);
        m.putInt("networkId", this.mNetworkId);
        m.putInt("systemId", this.mSystemId);
        m.putInt("cdmaRoamingIndicator", this.mCdmaRoamingIndicator);
        m.putInt("cdmaDefaultRoamingIndicator", this.mCdmaDefaultRoamingIndicator);
        m.putBoolean("emergencyOnly", this.mIsEmergencyOnly);
        m.putBoolean("isDataRoamingFromRegistration", this.mIsDataRoamingFromRegistration);
        m.putBoolean("isUsingCarrierAggregation", this.mIsUsingCarrierAggregation);
        m.putInt("LteEarfcnRsrpBoost", this.mLteEarfcnRsrpBoost);
        m.putInt("ChannelNumber", this.mChannelNumber);
        m.putIntArray("CellBandwidths", this.mCellBandwidths);
    }

    public void setRilVoiceRadioTechnology(int rt) {
        if (rt == 19) {
            rt = 14;
        }
        this.mRilVoiceRadioTechnology = rt;
    }

    public void setRilDataRadioTechnology(int rt) {
        if (rt == 19) {
            rt = 14;
            this.mIsUsingCarrierAggregation = true;
        } else {
            this.mIsUsingCarrierAggregation = false;
        }
        this.mRilDataRadioTechnology = rt;
    }

    public synchronized boolean isUsingCarrierAggregation() {
        return this.mIsUsingCarrierAggregation;
    }

    public synchronized void setIsUsingCarrierAggregation(boolean ca) {
        this.mIsUsingCarrierAggregation = ca;
    }

    public synchronized int getLteEarfcnRsrpBoost() {
        return this.mLteEarfcnRsrpBoost;
    }

    public synchronized void setLteEarfcnRsrpBoost(int LteEarfcnRsrpBoost) {
        this.mLteEarfcnRsrpBoost = LteEarfcnRsrpBoost;
    }

    private protected void setCssIndicator(int css) {
        this.mCssIndicator = css != 0;
    }

    public void setCdmaSystemAndNetworkId(int systemId, int networkId) {
        this.mSystemId = systemId;
        this.mNetworkId = networkId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRilVoiceRadioTechnology() {
        return this.mRilVoiceRadioTechnology;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRilDataRadioTechnology() {
        return this.mRilDataRadioTechnology;
    }

    private protected int getRadioTechnology() {
        Rlog.e(LOG_TAG, "ServiceState.getRadioTechnology() DEPRECATED will be removed *******");
        return getRilDataRadioTechnology();
    }

    public static synchronized int rilRadioTechnologyToNetworkType(int rt) {
        switch (rt) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
            case 5:
                return 4;
            case 6:
                return 7;
            case 7:
                return 5;
            case 8:
                return 6;
            case 9:
                return 8;
            case 10:
                return 9;
            case 11:
                return 10;
            case 12:
                return 12;
            case 13:
                return 14;
            case 14:
                return 13;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            default:
                return 0;
        }
    }

    public static synchronized int rilRadioTechnologyToAccessNetworkType(int rt) {
        switch (rt) {
            case 1:
            case 2:
            case 16:
                return 1;
            case 3:
            case 9:
            case 10:
            case 11:
            case 15:
            case 17:
                return 2;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 12:
            case 13:
                return 4;
            case 14:
            case 19:
                return 3;
            case 18:
                return 5;
            default:
                return 0;
        }
    }

    public static synchronized int networkTypeToRilRadioTechnology(int networkType) {
        switch (networkType) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 7;
            case 6:
                return 8;
            case 7:
                return 6;
            case 8:
                return 9;
            case 9:
                return 10;
            case 10:
                return 11;
            case 11:
            default:
                return 0;
            case 12:
                return 12;
            case 13:
                return 14;
            case 14:
                return 13;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
        }
    }

    private protected int getDataNetworkType() {
        return rilRadioTechnologyToNetworkType(this.mRilDataRadioTechnology);
    }

    private protected int getVoiceNetworkType() {
        return rilRadioTechnologyToNetworkType(this.mRilVoiceRadioTechnology);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCssIndicator() {
        return this.mCssIndicator ? 1 : 0;
    }

    public int getCdmaNetworkId() {
        return this.mNetworkId;
    }

    public int getCdmaSystemId() {
        return this.mSystemId;
    }

    private protected static boolean isGsm(int radioTechnology) {
        return radioTechnology == 1 || radioTechnology == 2 || radioTechnology == 3 || radioTechnology == 9 || radioTechnology == 10 || radioTechnology == 11 || radioTechnology == 14 || radioTechnology == 15 || radioTechnology == 16 || radioTechnology == 17 || radioTechnology == 18 || radioTechnology == 19;
    }

    private protected static boolean isCdma(int radioTechnology) {
        return radioTechnology == 4 || radioTechnology == 5 || radioTechnology == 6 || radioTechnology == 7 || radioTechnology == 8 || radioTechnology == 12 || radioTechnology == 13;
    }

    public static synchronized boolean isLte(int radioTechnology) {
        return radioTechnology == 14 || radioTechnology == 19;
    }

    private protected static boolean bearerBitmapHasCdma(int radioTechnologyBitmap) {
        return (6392 & radioTechnologyBitmap) != 0;
    }

    private protected static boolean bitmaskHasTech(int bearerBitmask, int radioTech) {
        if (bearerBitmask == 0) {
            return true;
        }
        if (radioTech >= 1 && ((1 << (radioTech - 1)) & bearerBitmask) != 0) {
            return true;
        }
        return false;
    }

    public static synchronized int getBitmaskForTech(int radioTech) {
        if (radioTech >= 1) {
            return 1 << (radioTech - 1);
        }
        return 0;
    }

    public static synchronized int getBitmaskFromString(String bearerList) {
        String[] bearers = bearerList.split("\\|");
        int bearerBitmask = 0;
        for (String bearer : bearers) {
            try {
                int bearerInt = Integer.parseInt(bearer.trim());
                if (bearerInt == 0) {
                    return 0;
                }
                bearerBitmask |= getBitmaskForTech(bearerInt);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return bearerBitmask;
    }

    public static synchronized int convertNetworkTypeBitmaskToBearerBitmask(int networkTypeBitmask) {
        if (networkTypeBitmask == 0) {
            return 0;
        }
        int bearerBitmask = 0;
        for (int bearerInt = 0; bearerInt < 20; bearerInt++) {
            if (bitmaskHasTech(networkTypeBitmask, rilRadioTechnologyToNetworkType(bearerInt))) {
                bearerBitmask |= getBitmaskForTech(bearerInt);
            }
        }
        return bearerBitmask;
    }

    public static synchronized int convertBearerBitmaskToNetworkTypeBitmask(int bearerBitmask) {
        if (bearerBitmask == 0) {
            return 0;
        }
        int networkTypeBitmask = 0;
        for (int bearerInt = 0; bearerInt < 20; bearerInt++) {
            if (bitmaskHasTech(bearerBitmask, bearerInt)) {
                networkTypeBitmask |= getBitmaskForTech(rilRadioTechnologyToNetworkType(bearerInt));
            }
        }
        return networkTypeBitmask;
    }

    private protected static ServiceState mergeServiceStates(ServiceState baseSs, ServiceState voiceSs) {
        if (voiceSs.mVoiceRegState != 0) {
            return baseSs;
        }
        ServiceState newSs = new ServiceState(baseSs);
        newSs.mVoiceRegState = voiceSs.mVoiceRegState;
        newSs.mIsEmergencyOnly = false;
        return newSs;
    }

    public synchronized List<NetworkRegistrationState> getNetworkRegistrationStates() {
        ArrayList arrayList;
        synchronized (this.mNetworkRegistrationStates) {
            arrayList = new ArrayList(this.mNetworkRegistrationStates);
        }
        return arrayList;
    }

    public synchronized List<NetworkRegistrationState> getNetworkRegistrationStates(int transportType) {
        List<NetworkRegistrationState> list = new ArrayList<>();
        synchronized (this.mNetworkRegistrationStates) {
            for (NetworkRegistrationState networkRegistrationState : this.mNetworkRegistrationStates) {
                if (networkRegistrationState.getTransportType() == transportType) {
                    list.add(networkRegistrationState);
                }
            }
        }
        return list;
    }

    public synchronized NetworkRegistrationState getNetworkRegistrationStates(int transportType, int domain) {
        synchronized (this.mNetworkRegistrationStates) {
            for (NetworkRegistrationState networkRegistrationState : this.mNetworkRegistrationStates) {
                if (networkRegistrationState.getTransportType() == transportType && networkRegistrationState.getDomain() == domain) {
                    return networkRegistrationState;
                }
            }
            return null;
        }
    }

    public synchronized void addNetworkRegistrationState(NetworkRegistrationState regState) {
        if (regState == null) {
            return;
        }
        synchronized (this.mNetworkRegistrationStates) {
            int i = 0;
            while (true) {
                if (i >= this.mNetworkRegistrationStates.size()) {
                    break;
                }
                NetworkRegistrationState curRegState = this.mNetworkRegistrationStates.get(i);
                if (curRegState.getTransportType() != regState.getTransportType() || curRegState.getDomain() != regState.getDomain()) {
                    i++;
                } else {
                    this.mNetworkRegistrationStates.remove(i);
                    break;
                }
            }
            this.mNetworkRegistrationStates.add(regState);
        }
    }
}
