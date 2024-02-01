package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class UsbPortStatus implements Parcelable {
    public static final Parcelable.Creator<UsbPortStatus> CREATOR = new Parcelable.Creator<UsbPortStatus>() { // from class: android.hardware.usb.UsbPortStatus.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsbPortStatus createFromParcel(Parcel in) {
            int currentMode = in.readInt();
            int currentPowerRole = in.readInt();
            int currentDataRole = in.readInt();
            int supportedRoleCombinations = in.readInt();
            return new UsbPortStatus(currentMode, currentPowerRole, currentDataRole, supportedRoleCombinations);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsbPortStatus[] newArray(int size) {
            return new UsbPortStatus[size];
        }
    };
    private final int mCurrentDataRole;
    private final int mCurrentMode;
    private final int mCurrentPowerRole;
    private final int mSupportedRoleCombinations;

    public synchronized UsbPortStatus(int currentMode, int currentPowerRole, int currentDataRole, int supportedRoleCombinations) {
        this.mCurrentMode = currentMode;
        this.mCurrentPowerRole = currentPowerRole;
        this.mCurrentDataRole = currentDataRole;
        this.mSupportedRoleCombinations = supportedRoleCombinations;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isConnected() {
        return this.mCurrentMode != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentMode() {
        return this.mCurrentMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentPowerRole() {
        return this.mCurrentPowerRole;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentDataRole() {
        return this.mCurrentDataRole;
    }

    private protected boolean isRoleCombinationSupported(int powerRole, int dataRole) {
        return (this.mSupportedRoleCombinations & UsbPort.combineRolesAsBit(powerRole, dataRole)) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSupportedRoleCombinations() {
        return this.mSupportedRoleCombinations;
    }

    public String toString() {
        return "UsbPortStatus{connected=" + isConnected() + ", currentMode=" + UsbPort.modeToString(this.mCurrentMode) + ", currentPowerRole=" + UsbPort.powerRoleToString(this.mCurrentPowerRole) + ", currentDataRole=" + UsbPort.dataRoleToString(this.mCurrentDataRole) + ", supportedRoleCombinations=" + UsbPort.roleCombinationsToString(this.mSupportedRoleCombinations) + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mCurrentMode);
        dest.writeInt(this.mCurrentPowerRole);
        dest.writeInt(this.mCurrentDataRole);
        dest.writeInt(this.mSupportedRoleCombinations);
    }
}
