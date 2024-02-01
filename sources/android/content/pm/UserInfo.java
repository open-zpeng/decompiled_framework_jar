package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.SettingsStringUtil;
/* loaded from: classes.dex */
public class UserInfo implements Parcelable {
    private protected static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() { // from class: android.content.pm.UserInfo.1
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
    private protected static final int FLAG_PRIMARY = 1;
    public static final int FLAG_QUIET_MODE = 128;
    public static final int FLAG_RESTRICTED = 8;
    public static final int NO_PROFILE_GROUP_ID = -10000;
    private protected long creationTime;
    private protected int flags;
    private protected boolean guestToRemove;
    private protected String iconPath;
    private protected int id;
    public String lastLoggedInFingerprint;
    private protected long lastLoggedInTime;
    private protected String name;
    private protected boolean partial;
    public int profileBadge;
    private protected int profileGroupId;
    public int restrictedProfileParentId;
    private protected int serialNumber;

    private protected UserInfo(int id, String name, int flags) {
        this(id, name, null, flags);
    }

    private protected UserInfo(int id, String name, String iconPath, int flags) {
        this.id = id;
        this.name = name;
        this.flags = flags;
        this.iconPath = iconPath;
        this.profileGroupId = NO_PROFILE_GROUP_ID;
        this.restrictedProfileParentId = NO_PROFILE_GROUP_ID;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPrimary() {
        return (this.flags & 1) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAdmin() {
        return (this.flags & 2) == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGuest() {
        return (this.flags & 4) == 4;
    }

    private protected boolean isRestricted() {
        return (this.flags & 8) == 8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isManagedProfile() {
        return (this.flags & 32) == 32;
    }

    private protected boolean isEnabled() {
        return (this.flags & 64) != 64;
    }

    public synchronized boolean isQuietModeEnabled() {
        return (this.flags & 128) == 128;
    }

    public synchronized boolean isEphemeral() {
        return (this.flags & 256) == 256;
    }

    public synchronized boolean isInitialized() {
        return (this.flags & 16) == 16;
    }

    public synchronized boolean isDemo() {
        return (this.flags & 512) == 512;
    }

    public synchronized boolean isSystemOnly() {
        return isSystemOnly(this.id);
    }

    public static synchronized boolean isSystemOnly(int userId) {
        return userId == 0 && UserManager.isSplitSystemUser();
    }

    public synchronized boolean supportsSwitchTo() {
        if (isEphemeral() && !isEnabled()) {
            return false;
        }
        return !isManagedProfile();
    }

    public synchronized boolean supportsSwitchToByUser() {
        boolean hideSystemUser = UserManager.isSplitSystemUser();
        return !(hideSystemUser && this.id == 0) && supportsSwitchTo();
    }

    public synchronized boolean canHaveProfile() {
        if (isManagedProfile() || isGuest() || isRestricted()) {
            return false;
        }
        return UserManager.isSplitSystemUser() ? this.id != 0 : this.id == 0;
    }

    public synchronized UserInfo() {
    }

    public synchronized UserInfo(UserInfo orig) {
        this.name = orig.name;
        this.iconPath = orig.iconPath;
        this.id = orig.id;
        this.flags = orig.flags;
        this.serialNumber = orig.serialNumber;
        this.creationTime = orig.creationTime;
        this.lastLoggedInTime = orig.lastLoggedInTime;
        this.lastLoggedInFingerprint = orig.lastLoggedInFingerprint;
        this.partial = orig.partial;
        this.profileGroupId = orig.profileGroupId;
        this.restrictedProfileParentId = orig.restrictedProfileParentId;
        this.guestToRemove = orig.guestToRemove;
        this.profileBadge = orig.profileBadge;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserHandle getUserHandle() {
        return new UserHandle(this.id);
    }

    public String toString() {
        return "UserInfo{" + this.id + SettingsStringUtil.DELIMITER + this.name + SettingsStringUtil.DELIMITER + Integer.toHexString(this.flags) + "}";
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
        dest.writeInt(this.partial ? 1 : 0);
        dest.writeInt(this.profileGroupId);
        dest.writeInt(this.guestToRemove ? 1 : 0);
        dest.writeInt(this.restrictedProfileParentId);
        dest.writeInt(this.profileBadge);
    }

    private synchronized UserInfo(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.iconPath = source.readString();
        this.flags = source.readInt();
        this.serialNumber = source.readInt();
        this.creationTime = source.readLong();
        this.lastLoggedInTime = source.readLong();
        this.lastLoggedInFingerprint = source.readString();
        this.partial = source.readInt() != 0;
        this.profileGroupId = source.readInt();
        this.guestToRemove = source.readInt() != 0;
        this.restrictedProfileParentId = source.readInt();
        this.profileBadge = source.readInt();
    }
}
