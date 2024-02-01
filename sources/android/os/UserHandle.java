package android.os;

import android.annotation.SystemApi;
import android.app.SearchManager;
import android.os.Parcelable;
import android.provider.Telephony;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public final class UserHandle implements Parcelable {
    private protected static final int AID_APP_END = 19999;
    private protected static final int AID_APP_START = 10000;
    private protected static final int AID_CACHE_GID_START = 20000;
    private protected static final int AID_ROOT = 0;
    private protected static final int AID_SHARED_GID_START = 50000;
    private protected static final int ERR_GID = -1;
    private protected static final boolean MU_ENABLED = true;
    private protected static final int PER_USER_RANGE = 100000;
    private protected static final int USER_ALL = -1;
    private protected static final int USER_CURRENT = -2;
    private protected static final int USER_CURRENT_OR_SELF = -3;
    private protected static final int USER_NULL = -10000;
    @Deprecated
    private protected static final int USER_OWNER = 0;
    private protected static final int USER_SERIAL_SYSTEM = 0;
    private protected static final int USER_SYSTEM = 0;
    public private protected final int mHandle;
    private protected static final UserHandle ALL = new UserHandle(-1);
    private protected static final UserHandle CURRENT = new UserHandle(-2);
    private protected static final UserHandle CURRENT_OR_SELF = new UserHandle(-3);
    @Deprecated
    private protected static final UserHandle OWNER = new UserHandle(0);
    public static final UserHandle SYSTEM = new UserHandle(0);
    public static final Parcelable.Creator<UserHandle> CREATOR = new Parcelable.Creator<UserHandle>() { // from class: android.os.UserHandle.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserHandle createFromParcel(Parcel in) {
            return new UserHandle(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserHandle[] newArray(int size) {
            return new UserHandle[size];
        }
    };

    public static synchronized boolean isSameUser(int uid1, int uid2) {
        return getUserId(uid1) == getUserId(uid2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSameApp(int uid1, int uid2) {
        return getAppId(uid1) == getAppId(uid2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isIsolated(int uid) {
        int appId;
        return uid > 0 && (appId = getAppId(uid)) >= 99000 && appId <= 99999;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isApp(int uid) {
        int appId;
        return uid > 0 && (appId = getAppId(uid)) >= 10000 && appId <= 19999;
    }

    public static synchronized boolean isCore(int uid) {
        if (uid < 0) {
            return false;
        }
        int appId = getAppId(uid);
        return appId < 10000;
    }

    public static UserHandle getUserHandleForUid(int uid) {
        return of(getUserId(uid));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getUserId(int uid) {
        return uid / PER_USER_RANGE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getCallingUserId() {
        return getUserId(Binder.getCallingUid());
    }

    public static synchronized int getCallingAppId() {
        return getAppId(Binder.getCallingUid());
    }

    @SystemApi
    public static UserHandle of(int userId) {
        return userId == 0 ? SYSTEM : new UserHandle(userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getUid(int userId, int appId) {
        return (userId * PER_USER_RANGE) + (appId % PER_USER_RANGE);
    }

    public static int getAppId(int uid) {
        return uid % PER_USER_RANGE;
    }

    public static synchronized int getUserGid(int userId) {
        return getUid(userId, Process.SHARED_USER_GID);
    }

    public static synchronized int getSharedAppGid(int uid) {
        return getSharedAppGid(getUserId(uid), getAppId(uid));
    }

    public static synchronized int getSharedAppGid(int userId, int appId) {
        if (appId >= 10000 && appId <= 19999) {
            return (appId - 10000) + 50000;
        }
        if (appId >= 0 && appId <= 10000) {
            return appId;
        }
        return -1;
    }

    private protected static int getAppIdFromSharedAppGid(int gid) {
        int appId = (getAppId(gid) + 10000) - 50000;
        if (appId < 0 || appId >= 50000) {
            return -1;
        }
        return appId;
    }

    public static synchronized int getCacheAppGid(int uid) {
        return getCacheAppGid(getUserId(uid), getAppId(uid));
    }

    public static synchronized int getCacheAppGid(int userId, int appId) {
        if (appId >= 10000 && appId <= 19999) {
            return getUid(userId, (appId - 10000) + 20000);
        }
        return -1;
    }

    public static synchronized void formatUid(StringBuilder sb, int uid) {
        if (uid < 10000) {
            sb.append(uid);
            return;
        }
        sb.append('u');
        sb.append(getUserId(uid));
        int appId = getAppId(uid);
        if (appId >= 99000 && appId <= 99999) {
            sb.append('i');
            sb.append(appId - Process.FIRST_ISOLATED_UID);
        } else if (appId >= 10000) {
            sb.append('a');
            sb.append(appId - 10000);
        } else {
            sb.append(SearchManager.MENU_KEY);
            sb.append(appId);
        }
    }

    public static synchronized String formatUid(int uid) {
        StringBuilder sb = new StringBuilder();
        formatUid(sb, uid);
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void formatUid(PrintWriter pw, int uid) {
        if (uid < 10000) {
            pw.print(uid);
            return;
        }
        pw.print('u');
        pw.print(getUserId(uid));
        int appId = getAppId(uid);
        if (appId >= 99000 && appId <= 99999) {
            pw.print('i');
            pw.print(appId - Process.FIRST_ISOLATED_UID);
        } else if (appId >= 10000) {
            pw.print('a');
            pw.print(appId - 10000);
        } else {
            pw.print(SearchManager.MENU_KEY);
            pw.print(appId);
        }
    }

    public static synchronized int parseUserArg(String arg) {
        if ("all".equals(arg)) {
            return -1;
        }
        if (Telephony.Carriers.CURRENT.equals(arg) || "cur".equals(arg)) {
            return -2;
        }
        try {
            int userId = Integer.parseInt(arg);
            return userId;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bad user number: " + arg);
        }
    }

    @SystemApi
    public static int myUserId() {
        return getUserId(Process.myUid());
    }

    @SystemApi
    @Deprecated
    public boolean isOwner() {
        return equals(OWNER);
    }

    @SystemApi
    public boolean isSystem() {
        return equals(SYSTEM);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public UserHandle(int h) {
        this.mHandle = h;
    }

    @SystemApi
    public int getIdentifier() {
        return this.mHandle;
    }

    public String toString() {
        return "UserHandle{" + this.mHandle + "}";
    }

    public boolean equals(Object obj) {
        if (obj != null) {
            try {
                UserHandle other = (UserHandle) obj;
                return this.mHandle == other.mHandle;
            } catch (ClassCastException e) {
            }
        }
        return false;
    }

    public int hashCode() {
        return this.mHandle;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mHandle);
    }

    public static void writeToParcel(UserHandle h, Parcel out) {
        if (h != null) {
            h.writeToParcel(out, 0);
        } else {
            out.writeInt(-10000);
        }
    }

    public static UserHandle readFromParcel(Parcel in) {
        int h = in.readInt();
        if (h != -10000) {
            return new UserHandle(h);
        }
        return null;
    }

    public UserHandle(Parcel in) {
        this.mHandle = in.readInt();
    }
}
