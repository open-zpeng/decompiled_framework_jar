package android.net.wifi.p2p;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Locale;

/* loaded from: classes2.dex */
public class WifiP2pWfdInfo implements Parcelable {
    private static final int COUPLED_SINK_SUPPORT_AT_SINK = 8;
    private static final int COUPLED_SINK_SUPPORT_AT_SOURCE = 4;
    @UnsupportedAppUsage
    public static final Parcelable.Creator<WifiP2pWfdInfo> CREATOR = new Parcelable.Creator<WifiP2pWfdInfo>() { // from class: android.net.wifi.p2p.WifiP2pWfdInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiP2pWfdInfo createFromParcel(Parcel in) {
            WifiP2pWfdInfo device = new WifiP2pWfdInfo();
            device.readFromParcel(in);
            return device;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiP2pWfdInfo[] newArray(int size) {
            return new WifiP2pWfdInfo[size];
        }
    };
    private static final int DEVICE_TYPE = 3;
    public static final int PRIMARY_SINK = 1;
    public static final int SECONDARY_SINK = 2;
    private static final int SESSION_AVAILABLE = 48;
    private static final int SESSION_AVAILABLE_BIT1 = 16;
    private static final int SESSION_AVAILABLE_BIT2 = 32;
    public static final int SOURCE_OR_PRIMARY_SINK = 3;
    private static final String TAG = "WifiP2pWfdInfo";
    public static final int WFD_SOURCE = 0;
    private int mCtrlPort;
    private int mDeviceInfo;
    private int mMaxThroughput;
    private int mR2DeviceInfo;
    private boolean mWfdEnabled;

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public WifiP2pWfdInfo() {
    }

    @UnsupportedAppUsage
    public WifiP2pWfdInfo(int devInfo, int ctrlPort, int maxTput) {
        this.mWfdEnabled = true;
        this.mDeviceInfo = devInfo;
        this.mCtrlPort = ctrlPort;
        this.mMaxThroughput = maxTput;
        this.mR2DeviceInfo = -1;
    }

    @UnsupportedAppUsage
    public boolean isWfdEnabled() {
        return this.mWfdEnabled;
    }

    public boolean isWfdR2Supported() {
        return this.mR2DeviceInfo >= 0;
    }

    @UnsupportedAppUsage
    public void setWfdEnabled(boolean enabled) {
        this.mWfdEnabled = enabled;
    }

    public void setWfdR2Device(int r2DeviceInfo) {
        this.mR2DeviceInfo = r2DeviceInfo;
    }

    @UnsupportedAppUsage
    public int getDeviceType() {
        return this.mDeviceInfo & 3;
    }

    @UnsupportedAppUsage
    public boolean setDeviceType(int deviceType) {
        if (deviceType >= 0 && deviceType <= 3) {
            this.mDeviceInfo &= -4;
            this.mDeviceInfo |= deviceType;
            return true;
        }
        return false;
    }

    public boolean isCoupledSinkSupportedAtSource() {
        return (this.mDeviceInfo & 8) != 0;
    }

    public void setCoupledSinkSupportAtSource(boolean enabled) {
        if (enabled) {
            this.mDeviceInfo |= 8;
        } else {
            this.mDeviceInfo &= -9;
        }
    }

    public boolean isCoupledSinkSupportedAtSink() {
        return (this.mDeviceInfo & 8) != 0;
    }

    public void setCoupledSinkSupportAtSink(boolean enabled) {
        if (enabled) {
            this.mDeviceInfo |= 8;
        } else {
            this.mDeviceInfo &= -9;
        }
    }

    public boolean isSessionAvailable() {
        return (this.mDeviceInfo & 48) != 0;
    }

    @UnsupportedAppUsage
    public void setSessionAvailable(boolean enabled) {
        if (enabled) {
            this.mDeviceInfo |= 16;
            this.mDeviceInfo &= -33;
            return;
        }
        this.mDeviceInfo &= -49;
    }

    public int getControlPort() {
        return this.mCtrlPort;
    }

    @UnsupportedAppUsage
    public void setControlPort(int port) {
        this.mCtrlPort = port;
    }

    @UnsupportedAppUsage
    public void setMaxThroughput(int maxThroughput) {
        this.mMaxThroughput = maxThroughput;
    }

    public int getMaxThroughput() {
        return this.mMaxThroughput;
    }

    public String getDeviceInfoHex() {
        return String.format(Locale.US, "%04x%04x%04x", Integer.valueOf(this.mDeviceInfo), Integer.valueOf(this.mCtrlPort), Integer.valueOf(this.mMaxThroughput));
    }

    public String getR2DeviceInfoHex() {
        return String.format(Locale.US, "%04x%04x", 2, Integer.valueOf(this.mR2DeviceInfo));
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("WFD enabled: ");
        sbuf.append(this.mWfdEnabled);
        sbuf.append("WFD DeviceInfo: ");
        sbuf.append(this.mDeviceInfo);
        sbuf.append("\n WFD CtrlPort: ");
        sbuf.append(this.mCtrlPort);
        sbuf.append("\n WFD MaxThroughput: ");
        sbuf.append(this.mMaxThroughput);
        sbuf.append("\n WFD R2 DeviceInfo: ");
        sbuf.append(this.mR2DeviceInfo);
        return sbuf.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @UnsupportedAppUsage
    public WifiP2pWfdInfo(WifiP2pWfdInfo source) {
        if (source != null) {
            this.mWfdEnabled = source.mWfdEnabled;
            this.mDeviceInfo = source.mDeviceInfo;
            this.mCtrlPort = source.mCtrlPort;
            this.mMaxThroughput = source.mMaxThroughput;
            this.mR2DeviceInfo = source.mR2DeviceInfo;
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mWfdEnabled ? 1 : 0);
        dest.writeInt(this.mDeviceInfo);
        dest.writeInt(this.mCtrlPort);
        dest.writeInt(this.mMaxThroughput);
        dest.writeInt(this.mR2DeviceInfo);
    }

    public void readFromParcel(Parcel in) {
        this.mWfdEnabled = in.readInt() == 1;
        this.mDeviceInfo = in.readInt();
        this.mCtrlPort = in.readInt();
        this.mMaxThroughput = in.readInt();
        this.mR2DeviceInfo = in.readInt();
    }
}
