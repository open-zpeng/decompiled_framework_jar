package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.SettingsStringUtil;
import android.util.DebugUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public class UserInfo implements Parcelable {
    @UnsupportedAppUsage
    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() { // from class: android.content.pm.UserInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
    public static final int FLAG_ADMIN = 2;
    public static final int FLAG_DEMO = 512;
    public static final int FLAG_DISABLED = 64;
    public static final int FLAG_EPHEMERAL = 256;
    public static final int FLAG_GUEST = 4;
    public static final int FLAG_INITIALIZED = 16;
    public static final int FLAG_MANAGED_PROFILE = 32;
    public static final int FLAG_MASK_USER_TYPE = 65535;
    @UnsupportedAppUsage
    public static final int FLAG_PRIMARY = 1;
    public static final int FLAG_QUIET_MODE = 128;
    public static final int FLAG_RESTRICTED = 8;
    public static final int NO_PROFILE_GROUP_ID = -10000;
    @UnsupportedAppUsage
    public long creationTime;
    @UnsupportedAppUsage
    public int flags;
    @UnsupportedAppUsage
    public boolean guestToRemove;
    @UnsupportedAppUsage
    public String iconPath;
    @UnsupportedAppUsage
    public int id;
    public String lastLoggedInFingerprint;
    @UnsupportedAppUsage
    public long lastLoggedInTime;
    @UnsupportedAppUsage
    public String name;
    @UnsupportedAppUsage
    public boolean partial;
    public boolean preCreated;
    public int profileBadge;
    @UnsupportedAppUsage
    public int profileGroupId;
    public int restrictedProfileParentId;
    @UnsupportedAppUsage
    public int serialNumber;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface UserInfoFlag {
    }

    @UnsupportedAppUsage
    public UserInfo(int id, String name, int flags) {
        this(id, name, null, flags);
    }

    @UnsupportedAppUsage
    public UserInfo(int id, String name, String iconPath, int flags) {
        this.id = id;
        this.name = name;
        this.flags = flags;
        this.iconPath = iconPath;
        this.profileGroupId = -10000;
        this.restrictedProfileParentId = -10000;
    }

    @UnsupportedAppUsage
    public boolean isPrimary() {
        return (this.flags & 1) == 1;
    }

    @UnsupportedAppUsage
    public boolean isAdmin() {
        return (this.flags & 2) == 2;
    }

    @UnsupportedAppUsage
    public boolean isGuest() {
        return isGuest(this.flags);
    }

    public static boolean isGuest(int flags) {
        return (flags & 4) == 4;
    }

    @UnsupportedAppUsage
    public boolean isRestricted() {
        return (this.flags & 8) == 8;
    }

    @UnsupportedAppUsage
    public boolean isManagedProfile() {
        return isManagedProfile(this.flags);
    }

    public static boolean isManagedProfile(int flags) {
        return (flags & 32) == 32;
    }

    @UnsupportedAppUsage
    public boolean isEnabled() {
        return (this.flags & 64) != 64;
    }

    public boolean isQuietModeEnabled() {
        return (this.flags & 128) == 128;
    }

    public boolean isEphemeral() {
        return (this.flags & 256) == 256;
    }

    public boolean isInitialized() {
        return (this.flags & 16) == 16;
    }

    public boolean isDemo() {
        return (this.flags & 512) == 512;
    }

    public boolean isSystemOnly() {
        return isSystemOnly(this.id);
    }

    public static boolean isSystemOnly(int userId) {
        return userId == 0 && UserManager.isSplitSystemUser();
    }

    public boolean supportsSwitchTo() {
        if (isEphemeral() && !isEnabled()) {
            return false;
        }
        return !isManagedProfile();
    }

    public boolean supportsSwitchToByUser() {
        boolean hideSystemUser = UserManager.isSplitSystemUser();
        return !(hideSystemUser && this.id == 0) && supportsSwitchTo();
    }

    public boolean canHaveProfile() {
        if (isManagedProfile() || isGuest() || isRestricted()) {
            return false;
        }
        return UserManager.isSplitSystemUser() ? this.id != 0 : this.id == 0;
    }

    public UserInfo() {
    }

    public UserInfo(UserInfo orig) {
        this.name = orig.name;
        this.iconPath = orig.iconPath;
        this.id = orig.id;
        this.flags = orig.flags;
        this.serialNumber = orig.serialNumber;
        this.creationTime = orig.creationTime;
        this.lastLoggedInTime = orig.lastLoggedInTime;
        this.lastLoggedInFingerprint = orig.lastLoggedInFingerprint;
        this.partial = orig.partial;
        this.preCreated = orig.preCreated;
        this.profileGroupId = orig.profileGroupId;
        this.restrictedProfileParentId = orig.restrictedProfileParentId;
        this.guestToRemove = orig.guestToRemove;
        this.profileBadge = orig.profileBadge;
    }

    @UnsupportedAppUsage
    public UserHandle getUserHandle() {
        return new UserHandle(this.id);
    }

    public String toString() {
        return "UserInfo{" + this.id + SettingsStringUtil.DELIMITER + this.name + SettingsStringUtil.DELIMITER + Integer.toHexString(this.flags) + "}";
    }

    public String toFullString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserInfo[id=");
        sb.append(this.id);
        sb.append(", name=");
        sb.append(this.name);
        sb.append(", flags=");
        sb.append(flagsToString(this.flags));
        sb.append(this.preCreated ? " (pre-created)" : "");
        sb.append(this.partial ? " (partial)" : "");
        sb.append("]");
        return sb.toString();
    }

    public static String flagsToString(int flags) {
        return DebugUtils.flagsToString(UserInfo.class, "FLAG_", flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.iconPath);
        dest.writeInt(this.flags);
        dest.writeInt(this.serialNumber);
        dest.writeLong(this.creationTime);
        dest.writeLong(this.lastLoggedInTime);
        dest.writeString(this.lastLoggedInFingerprint);
        dest.writeBoolean(this.partial);
        dest.writeBoolean(this.preCreated);
        dest.writeInt(this.profileGroupId);
        dest.writeBoolean(this.guestToRemove);
        dest.writeInt(this.restrictedProfileParentId);
        dest.writeInt(this.profileBadge);
    }

    private UserInfo(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.iconPath = source.readString();
        this.flags = source.readInt();
        this.serialNumber = source.readInt();
        this.creationTime = source.readLong();
        this.lastLoggedInTime = source.readLong();
        this.lastLoggedInFingerprint = source.readString();
        this.partial = source.readBoolean();
        this.preCreated = source.readBoolean();
        this.profileGroupId = source.readInt();
        this.guestToRemove = source.readBoolean();
        this.restrictedProfileParentId = source.readInt();
        this.profileBadge = source.readInt();
    }
}
