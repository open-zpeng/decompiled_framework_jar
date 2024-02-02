package android.os;

import android.content.pm.UserInfo;
import android.graphics.Bitmap;
/* loaded from: classes2.dex */
public abstract class UserManagerInternal {
    public static final int CAMERA_DISABLED_GLOBALLY = 2;
    public static final int CAMERA_DISABLED_LOCALLY = 1;
    public static final int CAMERA_NOT_DISABLED = 0;

    /* loaded from: classes2.dex */
    public interface UserRestrictionsListener {
        synchronized void onUserRestrictionsChanged(int i, Bundle bundle, Bundle bundle2);
    }

    public abstract synchronized void addUserRestrictionsListener(UserRestrictionsListener userRestrictionsListener);

    public abstract synchronized UserInfo createUserEvenWhenDisallowed(String str, int i, String[] strArr);

    public abstract synchronized boolean exists(int i);

    public abstract synchronized Bundle getBaseUserRestrictions(int i);

    public abstract synchronized int getProfileParentId(int i);

    public abstract synchronized int[] getUserIds();

    public abstract synchronized boolean getUserRestriction(int i, String str);

    public abstract synchronized boolean isProfileAccessible(int i, int i2, String str, boolean z);

    public abstract synchronized boolean isSettingRestrictedForUser(String str, int i, String str2, int i2);

    public abstract synchronized boolean isUserInitialized(int i);

    public abstract synchronized boolean isUserRunning(int i);

    public abstract synchronized boolean isUserUnlocked(int i);

    public abstract synchronized boolean isUserUnlockingOrUnlocked(int i);

    public abstract synchronized void onEphemeralUserStop(int i);

    public abstract synchronized void removeAllUsers();

    public abstract synchronized boolean removeUserEvenWhenDisallowed(int i);

    public abstract synchronized void removeUserRestrictionsListener(UserRestrictionsListener userRestrictionsListener);

    public abstract synchronized void removeUserState(int i);

    public abstract synchronized void setBaseUserRestrictionsByDpmsForMigration(int i, Bundle bundle);

    public abstract synchronized void setDeviceManaged(boolean z);

    public abstract synchronized void setDevicePolicyUserRestrictions(int i, Bundle bundle, boolean z, int i2);

    public abstract synchronized void setForceEphemeralUsers(boolean z);

    public abstract synchronized void setUserIcon(int i, Bitmap bitmap);

    public abstract synchronized void setUserManaged(int i, boolean z);

    public abstract synchronized void setUserState(int i, int i2);
}
