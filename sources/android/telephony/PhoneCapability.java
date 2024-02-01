package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class PhoneCapability implements Parcelable {
    public static final Parcelable.Creator<PhoneCapability> CREATOR;
    public static final PhoneCapability DEFAULT_DSDS_CAPABILITY;
    public static final PhoneCapability DEFAULT_SSSS_CAPABILITY;
    public final List<ModemInfo> logicalModemList;
    public final int max5G;
    public final int maxActiveData;
    public final int maxActiveVoiceCalls;
    public final boolean validationBeforeSwitchSupported;

    static {
        ModemInfo modemInfo1 = new ModemInfo(0, 0, true, true);
        ModemInfo modemInfo2 = new ModemInfo(1, 0, true, true);
        List<ModemInfo> logicalModemList = new ArrayList<>();
        logicalModemList.add(modemInfo1);
        logicalModemList.add(modemInfo2);
        DEFAULT_DSDS_CAPABILITY = new PhoneCapability(1, 1, 0, logicalModemList, false);
        List<ModemInfo> logicalModemList2 = new ArrayList<>();
        logicalModemList2.add(modemInfo1);
        DEFAULT_SSSS_CAPABILITY = new PhoneCapability(1, 1, 0, logicalModemList2, false);
        CREATOR = new Parcelable.Creator() { // from class: android.telephony.PhoneCapability.1
            @Override // android.os.Parcelable.Creator
            public PhoneCapability createFromParcel(Parcel in) {
                return new PhoneCapability(in);
            }

            @Override // android.os.Parcelable.Creator
            public PhoneCapability[] newArray(int size) {
                return new PhoneCapability[size];
            }
        };
    }

    public PhoneCapability(int maxActiveVoiceCalls, int maxActiveData, int max5G, List<ModemInfo> logicalModemList, boolean validationBeforeSwitchSupported) {
        this.maxActiveVoiceCalls = maxActiveVoiceCalls;
        this.maxActiveData = maxActiveData;
        this.max5G = max5G;
        this.logicalModemList = logicalModemList == null ? new ArrayList<>() : logicalModemList;
        this.validationBeforeSwitchSupported = validationBeforeSwitchSupported;
    }

    public String toString() {
        return "maxActiveVoiceCalls=" + this.maxActiveVoiceCalls + " maxActiveData=" + this.maxActiveData + " max5G=" + this.max5G + "logicalModemList:" + Arrays.toString(this.logicalModemList.toArray());
    }

    private PhoneCapability(Parcel in) {
        this.maxActiveVoiceCalls = in.readInt();
        this.maxActiveData = in.readInt();
        this.max5G = in.readInt();
        this.validationBeforeSwitchSupported = in.readBoolean();
        this.logicalModemList = new ArrayList();
        in.readList(this.logicalModemList, ModemInfo.class.getClassLoader());
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.maxActiveVoiceCalls), Integer.valueOf(this.maxActiveData), Integer.valueOf(this.max5G), this.logicalModemList, Boolean.valueOf(this.validationBeforeSwitchSupported));
    }

    public boolean equals(Object o) {
        if (o != null && (o instanceof PhoneCapability) && hashCode() == o.hashCode()) {
            if (this == o) {
                return true;
            }
            PhoneCapability s = (PhoneCapability) o;
            if (this.maxActiveVoiceCalls != s.maxActiveVoiceCalls || this.maxActiveData != s.maxActiveData || this.max5G != s.max5G || this.validationBeforeSwitchSupported != s.validationBeforeSwitchSupported || !this.logicalModemList.equals(s.logicalModemList)) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxActiveVoiceCalls);
        dest.writeInt(this.maxActiveData);
        dest.writeInt(this.max5G);
        dest.writeBoolean(this.validationBeforeSwitchSupported);
        dest.writeList(this.logicalModemList);
    }
}
