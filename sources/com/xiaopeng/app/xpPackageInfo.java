package com.xiaopeng.app;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemProperties;

/* loaded from: classes3.dex */
public class xpPackageInfo implements Parcelable {
    public static final int ADJUST_VOLUME_ALLOW = 1;
    public static final int CONTROL_CAR_KEYEVENT = 2;
    public static final int CONTROL_JOYSTICK_MOCK = 4;
    public static final int CONTROL_STEERING_WHEEL = 1;
    public static final Parcelable.Creator<xpPackageInfo> CREATOR = new Parcelable.Creator<xpPackageInfo>() { // from class: com.xiaopeng.app.xpPackageInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public xpPackageInfo createFromParcel(Parcel source) {
            return new xpPackageInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public xpPackageInfo[] newArray(int size) {
            return new xpPackageInfo[size];
        }
    };
    public static final int FLAG_GEAR_LEVEL_D = 1;
    public static final int FLAG_GEAR_LEVEL_N = 2;
    public static final int FLAG_GEAR_LEVEL_P = 8;
    public static final int FLAG_GEAR_LEVEL_R = 4;
    public static final int FLAG_SCREEN_DIALOG = 8;
    public static final int FLAG_SCREEN_INFOFLOW = 2;
    public static final int FLAG_SCREEN_NAVIGATION = 4;
    public static final int FLAG_SCREEN_STATUS = 1;
    public static final int GEAR_LEVEL_D = 1;
    public static final int GEAR_LEVEL_N = 2;
    public static final int GEAR_LEVEL_P = 4;
    public static final int GEAR_LEVEL_R = 3;
    public static final int GRANT_APP_PERMISSION = 1;
    public static final int GRANT_SENSITIVE_PERMISSION = 4;
    public static final int GRANT_USB_PERMISSION = 2;
    public static final int MODE_EXECUTE_GAME = 1;
    public static final int MODE_EXECUTE_NORMAL = 0;
    public static final int NAV_KEY_BACK = 1;
    public static final int NAV_KEY_DEFAULT = 0;
    public static final int NAV_KEY_MENU = 2;
    public static final int NOTIFICATION_SHOW_MED = 1;
    public static final int NOTIFICATION_SHOW_NONE = 0;
    public static final int PERMISSION_DEFAULT = 0;
    public static final int PERMISSION_DENIED_AND_STOP_CHECK = 2;
    public static final int PERMISSION_GRANTED = 1;
    public static final String XP_INSTALL_PERMISSION = "persist.sys.3rdapp.installpermission";
    public static final String XP_KEY_GAME_MODE_FLAG = "xp.key.gamemode.flag";
    public int adjustVolumeValue;
    public String apkSignInfo;
    public int appType;
    public int backgroundStatus;
    public int controlFlags;
    public int densityDpi;
    public int enableVisualizer;
    public int executeMode;
    public int floatingWindow;
    public float fontScale;
    public String gameImageUri;
    public int gearLevel;
    public int gearLevelFlags;
    public int installPermission;
    public String keyEvent;
    public int limitFullScreenInDGear;
    public float maxAspectRatio;
    public String mediaArtistKey;
    public String mediaTitleKey;
    public long moduleAuthFlags;
    public int navigation;
    public int navigationKey;
    public int notificationFlag;
    public int orientation;
    public String packageName;
    public int permissionGrant;
    public String permissionGroup;
    public int requestAudioFocus;
    public int screenAdaption;
    public int screenFlag;
    public int screenHeightDp;
    public int screenLayout;
    public int screenWidthDp;
    public int supportGamePad;
    public int versionCode;
    public String versionName;

    public xpPackageInfo(String packageName) {
        this.packageName = packageName;
    }

    private xpPackageInfo(Parcel source) {
        this.packageName = source.readString();
        this.versionName = source.readString();
        this.versionCode = source.readInt();
        this.executeMode = source.readInt();
        this.maxAspectRatio = source.readFloat();
        this.screenFlag = source.readInt();
        this.permissionGrant = source.readInt();
        this.navigationKey = source.readInt();
        this.floatingWindow = source.readInt();
        this.fontScale = source.readFloat();
        this.screenLayout = source.readInt();
        this.navigation = source.readInt();
        this.orientation = source.readInt();
        this.screenWidthDp = source.readInt();
        this.screenHeightDp = source.readInt();
        this.densityDpi = source.readInt();
        this.keyEvent = source.readString();
        this.gearLevel = source.readInt();
        this.notificationFlag = source.readInt();
        this.permissionGroup = source.readString();
        this.backgroundStatus = source.readInt();
        this.enableVisualizer = source.readInt();
        this.screenAdaption = source.readInt();
        this.limitFullScreenInDGear = source.readInt();
        this.appType = source.readInt();
        this.mediaTitleKey = source.readString();
        this.mediaArtistKey = source.readString();
        this.installPermission = source.readInt();
        this.gearLevelFlags = source.readInt();
        this.requestAudioFocus = source.readInt();
        this.controlFlags = source.readInt();
        this.moduleAuthFlags = source.readLong();
        this.gameImageUri = source.readString();
        this.supportGamePad = source.readInt();
        this.adjustVolumeValue = source.readInt();
        this.apkSignInfo = source.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeString(this.packageName);
        dest.writeString(this.versionName);
        dest.writeInt(this.versionCode);
        dest.writeInt(this.executeMode);
        dest.writeFloat(this.maxAspectRatio);
        dest.writeInt(this.screenFlag);
        dest.writeInt(this.permissionGrant);
        dest.writeInt(this.navigationKey);
        dest.writeInt(this.floatingWindow);
        dest.writeFloat(this.fontScale);
        dest.writeInt(this.screenLayout);
        dest.writeInt(this.navigation);
        dest.writeInt(this.orientation);
        dest.writeInt(this.screenWidthDp);
        dest.writeInt(this.screenHeightDp);
        dest.writeInt(this.densityDpi);
        dest.writeString(this.keyEvent);
        dest.writeInt(this.gearLevel);
        dest.writeInt(this.notificationFlag);
        dest.writeString(this.permissionGroup);
        dest.writeInt(this.backgroundStatus);
        dest.writeInt(this.enableVisualizer);
        dest.writeInt(this.screenAdaption);
        dest.writeInt(this.limitFullScreenInDGear);
        dest.writeInt(this.appType);
        dest.writeString(this.mediaTitleKey);
        dest.writeString(this.mediaArtistKey);
        dest.writeInt(this.installPermission);
        dest.writeInt(this.gearLevelFlags);
        dest.writeInt(this.requestAudioFocus);
        dest.writeInt(this.controlFlags);
        dest.writeLong(this.moduleAuthFlags);
        dest.writeString(this.gameImageUri);
        dest.writeInt(this.supportGamePad);
        dest.writeInt(this.adjustVolumeValue);
        dest.writeString(this.apkSignInfo);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "xpPackageInfo{" + Integer.toHexString(System.identityHashCode(this)) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.packageName + " &screenFlag:" + this.screenFlag + " &screenWidthDp:" + this.screenWidthDp + " &screenHeightDp:" + this.screenHeightDp + " }";
    }

    public static boolean isXpGameModeEnable() {
        return SystemProperties.getBoolean("xp.key.gamemode.flag", false);
    }

    public static void setXpGameModeEnable(boolean enable) {
        SystemProperties.set("xp.key.gamemode.flag", enable ? WifiEnterpriseConfig.ENGINE_ENABLE : WifiEnterpriseConfig.ENGINE_DISABLE);
    }

    public static boolean isFullscreen(xpPackageInfo pi) {
        if (pi == null) {
            return false;
        }
        boolean hideStatus = false;
        boolean hideNavigation = false;
        int screenFlag = pi.screenFlag;
        if ((screenFlag & 1) == 1) {
            hideStatus = true;
        }
        if ((screenFlag & 4) == 4) {
            hideNavigation = true;
        }
        return hideStatus && hideNavigation;
    }

    public static boolean packageInstallEnable(xpPackageInfo pi) {
        if (pi == null || pi.installPermission != 1) {
            return SystemProperties.getBoolean(XP_INSTALL_PERMISSION, false);
        }
        return true;
    }
}
