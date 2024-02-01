package com.xiaopeng.screen;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class ScreenDevice implements Parcelable {
    public static final Parcelable.Creator<ScreenDevice> CREATOR = new Parcelable.Creator<ScreenDevice>() { // from class: com.xiaopeng.screen.ScreenDevice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ScreenDevice createFromParcel(Parcel source) {
            return new ScreenDevice(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ScreenDevice[] newArray(int size) {
            return new ScreenDevice[size];
        }
    };
    public static final String DEVICE_ICM = "xp_mt_icm";
    public static final String DEVICE_IVI = "xp_mt_ivi";
    public static final String DEVICE_PSG = "xp_mt_psg";
    public static final String DEVICE_RSE = "xp_mt_rse";
    public static final int SCREEN_FOURTH = 3;
    public static final int SCREEN_ICM = 2;
    public static final int SCREEN_IVI = 0;
    public static final int SCREEN_PRIMARY = 0;
    public static final int SCREEN_PSG = 1;
    public static final int SCREEN_RSE = 3;
    public static final int SCREEN_SECONDARY = 1;
    public static final int SCREEN_THIRD = 2;
    public static final int TYPE_DISPLAY_DEVICE = 0;
    public static final int TYPE_SHARING_DEVICE = 1;
    public static final String UNIQUE_ICM = "icm";
    public static final String UNIQUE_IVI = "ivi";
    public static final String UNIQUE_PSG = "psg";
    public static final String UNIQUE_RSE = "rse";
    public ScreenItemConfiguration configuration;

    public ScreenDevice(ScreenItemConfiguration configuration) {
        this.configuration = configuration;
    }

    private ScreenDevice(Parcel source) {
        if (source.readInt() != 0) {
            this.configuration = ScreenItemConfiguration.CREATOR.createFromParcel(source);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        if (this.configuration != null) {
            dest.writeInt(1);
            this.configuration.writeToParcel(dest, 0);
            return;
        }
        dest.writeInt(0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int getScreenId(String name) {
        char c;
        switch (name.hashCode()) {
            case 104083:
                if (name.equals(UNIQUE_ICM)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 104668:
                if (name.equals(UNIQUE_IVI)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 111300:
                if (name.equals(UNIQUE_PSG)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 113220:
                if (name.equals(UNIQUE_RSE)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1419202338:
                if (name.equals(DEVICE_ICM)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1419202923:
                if (name.equals("xp_mt_ivi")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1419209555:
                if (name.equals("xp_mt_psg")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1419211475:
                if (name.equals(DEVICE_RSE)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
                return 0;
            case 2:
            case 3:
                return 1;
            case 4:
            case 5:
                return 2;
            case 6:
            case 7:
                return 3;
            default:
                return 0;
        }
    }

    public static String getUniqueName(int screenId) {
        switch (screenId) {
            case 0:
                return UNIQUE_IVI;
            case 1:
                return UNIQUE_PSG;
            case 2:
                return UNIQUE_ICM;
            case 3:
                return UNIQUE_RSE;
            default:
                return UNIQUE_IVI;
        }
    }

    public static String getDeviceName(int screenId) {
        switch (screenId) {
            case 0:
                return "xp_mt_ivi";
            case 1:
                return "xp_mt_psg";
            case 2:
                return DEVICE_ICM;
            case 3:
                return DEVICE_RSE;
            default:
                return "xp_mt_ivi";
        }
    }
}
