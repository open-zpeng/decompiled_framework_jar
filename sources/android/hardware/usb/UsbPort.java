package android.hardware.usb;

import android.app.slice.Slice;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;
/* loaded from: classes.dex */
public final class UsbPort implements Parcelable {
    public static final Parcelable.Creator<UsbPort> CREATOR = new Parcelable.Creator<UsbPort>() { // from class: android.hardware.usb.UsbPort.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsbPort createFromParcel(Parcel in) {
            String id = in.readString();
            int supportedModes = in.readInt();
            return new UsbPort(id, supportedModes);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsbPort[] newArray(int size) {
            return new UsbPort[size];
        }
    };
    public static final int DATA_ROLE_DEVICE = 2;
    public static final int DATA_ROLE_HOST = 1;
    public static final int DATA_ROLE_NONE = 0;
    public static final int MODE_AUDIO_ACCESSORY = 4;
    public static final int MODE_DEBUG_ACCESSORY = 8;
    public static final int MODE_DFP = 2;
    public static final int MODE_DUAL = 3;
    public static final int MODE_NONE = 0;
    public static final int MODE_UFP = 1;
    private static final int NUM_DATA_ROLES = 3;
    public static final int POWER_ROLE_NONE = 0;
    private static final int POWER_ROLE_OFFSET = 0;
    public static final int POWER_ROLE_SINK = 2;
    public static final int POWER_ROLE_SOURCE = 1;
    private final String mId;
    private final int mSupportedModes;

    public synchronized UsbPort(String id, int supportedModes) {
        this.mId = id;
        this.mSupportedModes = supportedModes;
    }

    public synchronized String getId() {
        return this.mId;
    }

    public synchronized int getSupportedModes() {
        return this.mSupportedModes;
    }

    public static synchronized int combineRolesAsBit(int powerRole, int dataRole) {
        checkRoles(powerRole, dataRole);
        int index = ((powerRole + 0) * 3) + dataRole;
        return 1 << index;
    }

    public static synchronized String modeToString(int mode) {
        StringBuilder modeString = new StringBuilder();
        if (mode == 0) {
            return "none";
        }
        if ((mode & 3) == 3) {
            modeString.append("dual, ");
        } else if ((mode & 2) == 2) {
            modeString.append("dfp, ");
        } else if ((mode & 1) == 1) {
            modeString.append("ufp, ");
        }
        if ((mode & 4) == 4) {
            modeString.append("audio_acc, ");
        }
        if ((mode & 8) == 8) {
            modeString.append("debug_acc, ");
        }
        if (modeString.length() == 0) {
            return Integer.toString(mode);
        }
        return modeString.substring(0, modeString.length() - 2);
    }

    public static synchronized String powerRoleToString(int role) {
        switch (role) {
            case 0:
                return "no-power";
            case 1:
                return Slice.SUBTYPE_SOURCE;
            case 2:
                return "sink";
            default:
                return Integer.toString(role);
        }
    }

    public static synchronized String dataRoleToString(int role) {
        switch (role) {
            case 0:
                return "no-data";
            case 1:
                return "host";
            case 2:
                return UsbManager.EXTRA_DEVICE;
            default:
                return Integer.toString(role);
        }
    }

    public static synchronized String roleCombinationsToString(int combo) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        int combo2 = combo;
        boolean first = true;
        while (combo2 != 0) {
            int index = Integer.numberOfTrailingZeros(combo2);
            combo2 &= ~(1 << index);
            int powerRole = (index / 3) + 0;
            int dataRole = index % 3;
            if (first) {
                first = false;
            } else {
                result.append(", ");
            }
            result.append(powerRoleToString(powerRole));
            result.append(':');
            result.append(dataRoleToString(dataRole));
        }
        result.append("]");
        return result.toString();
    }

    public static synchronized void checkMode(int powerRole) {
        Preconditions.checkArgumentInRange(powerRole, 0, 3, "portMode");
    }

    public static synchronized void checkPowerRole(int dataRole) {
        Preconditions.checkArgumentInRange(dataRole, 0, 2, "powerRole");
    }

    public static synchronized void checkDataRole(int mode) {
        Preconditions.checkArgumentInRange(mode, 0, 2, "powerRole");
    }

    public static synchronized void checkRoles(int powerRole, int dataRole) {
        Preconditions.checkArgumentInRange(powerRole, 0, 2, "powerRole");
        Preconditions.checkArgumentInRange(dataRole, 0, 2, "dataRole");
    }

    public synchronized boolean isModeSupported(int mode) {
        return (this.mSupportedModes & mode) == mode;
    }

    public String toString() {
        return "UsbPort{id=" + this.mId + ", supportedModes=" + modeToString(this.mSupportedModes) + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeInt(this.mSupportedModes);
    }
}
