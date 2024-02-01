package android.os;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.Notification;
import android.os.Parcelable;
import android.provider.Telephony;
import android.text.format.DateFormat;
import java.io.PrintWriter;

/* loaded from: classes2.dex */
public final class UserHandle implements Parcelable {
    @UnsupportedAppUsage
    public static final int AID_APP_END = 19999;
    @UnsupportedAppUsage
    public static final int AID_APP_START = 10000;
    @UnsupportedAppUsage
    public static final int AID_CACHE_GID_START = 20000;
    @UnsupportedAppUsage
    public static final int AID_ROOT = 0;
    @UnsupportedAppUsage
    public static final int AID_SHARED_GID_START = 50000;
    @UnsupportedAppUsage
    public static final int ERR_GID = -1;
    @UnsupportedAppUsage
    public static final boolean MU_ENABLED = true;
    @UnsupportedAppUsage
    public static final int PER_USER_RANGE = 100000;
    @UnsupportedAppUsage
    public static final int USER_ALL = -1;
    @UnsupportedAppUsage
    public static final int USER_CURRENT = -2;
    @UnsupportedAppUsage
    public static final int USER_CURRENT_OR_SELF = -3;
    @UnsupportedAppUsage
    public static final int USER_NULL = -10000;
    @UnsupportedAppUsage
    @Deprecated
    public static final int USER_OWNER = 0;
    @UnsupportedAppUsage
    public static final int USER_SERIAL_SYSTEM = 0;
    @UnsupportedAppUsage
    public static final int USER_SYSTEM = 0;
    @UnsupportedAppUsage
    final int mHandle;
    @SystemApi
    public static final UserHandle ALL = new UserHandle(-1);
    @SystemApi
    public static final UserHandle CURRENT = new UserHandle(-2);
    @UnsupportedAppUsage
    public static final UserHandle CURRENT_OR_SELF = new UserHandle(-3);
    @UnsupportedAppUsage
    @Deprecated
    public static final UserHandle OWNER = new UserHandle(0);
    @SystemApi
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

    public static boolean isSameUser(int uid1, int uid2) {
        return getUserId(uid1) == getUserId(uid2);
    }

    @UnsupportedAppUsage
    public static boolean isSameApp(int uid1, int uid2) {
        return getAppId(uid1) == getAppId(uid2);
    }

    @UnsupportedAppUsage
    public static boolean isIsolated(int uid) {
        if (uid > 0) {
            return Process.isIsolated(uid);
        }
        return false;
    }

    public static boolean isApp(int uid) {
        int appId;
        return uid > 0 && (appId = getAppId(uid)) >= 10000 && appId <= 19999;
    }

    public static boolean isCore(int uid) {
        if (uid < 0) {
            return false;
        }
        int appId = getAppId(uid);
        return appId < 10000;
    }

    public static UserHandle getUserHandleForUid(int uid) {
        return of(getUserId(uid));
    }

    @UnsupportedAppUsage
    public static int getUserId(int uid) {
        return uid / PER_USER_RANGE;
    }

    @UnsupportedAppUsage
    public static int getCallingUserId() {
        return getUserId(Binder.getCallingUid());
    }

    public static int getCallingAppId() {
        return getAppId(Binder.getCallingUid());
    }

    @SystemApi
    public static UserHandle of(int userId) {
        return userId == 0 ? SYSTEM : new UserHandle(userId);
    }

    @UnsupportedAppUsage
    public static int getUid(int userId, int appId) {
        return (userId * PER_USER_RANGE) + (appId % PER_USER_RANGE);
    }

    @SystemApi
    public static int getAppId(int uid) {
        return uid % PER_USER_RANGE;
    }

    public static int getUserGid(int userId) {
        return getUid(userId, Process.SHARED_USER_GID);
    }

    public static int getSharedAppGid(int uid) {
        return getSharedAppGid(getUserId(uid), getAppId(uid));
    }

    public static int getSharedAppGid(int userId, int appId) {
        if (appId >= 10000 && appId <= 19999) {
            return (appId - 10000) + 50000;
        }
        if (appId >= 0 && appId <= 10000) {
            return appId;
        }
        return -1;
    }

    @UnsupportedAppUsage
    public static int getAppIdFromSharedAppGid(int gid) {
        int appId = (getAppId(gid) + 10000) - 50000;
        if (appId < 0 || appId >= 50000) {
            return -1;
        }
        return appId;
    }

    public static int getCacheAppGid(int uid) {
        return getCacheAppGid(getUserId(uid), getAppId(uid));
    }

    public static int getCacheAppGid(int userId, int appId) {
        if (appId >= 10000 && appId <= 19999) {
            return getUid(userId, (appId - 10000) + 20000);
        }
        return -1;
    }

    public static void formatUid(StringBuilder sb, int uid) {
        if (uid < 10000) {
            sb.append(uid);
            return;
        }
        sb.append('u');
        sb.append(getUserId(uid));
        int appId = getAppId(uid);
        if (isIsolated(appId)) {
            if (appId > 99000) {
                sb.append('i');
                sb.append(appId - Process.FIRST_ISOLATED_UID);
                return;
            }
            sb.append(Notification.TYPE_AI);
            sb.append(appId - Process.FIRST_APP_ZYGOTE_ISOLATED_UID);
        } else if (appId >= 10000) {
            sb.append(DateFormat.AM_PM);
            sb.append(appId - 10000);
        } else {
            sb.append('s');
            sb.append(appId);
        }
    }

    public static String formatUid(int uid) {
        StringBuilder sb = new StringBuilder();
        formatUid(sb, uid);
        return sb.toString();
    }

    public static void formatUid(PrintWriter pw, int uid) {
        if (uid < 10000) {
            pw.print(uid);
            return;
        }
        pw.print('u');
        pw.print(getUserId(uid));
        int appId = getAppId(uid);
        if (isIsolated(appId)) {
            if (appId > 99000) {
                pw.print('i');
                pw.print(appId - Process.FIRST_ISOLATED_UID);
                return;
            }
            pw.print(Notification.TYPE_AI);
            pw.print(appId - Process.FIRST_APP_ZYGOTE_ISOLATED_UID);
        } else if (appId >= 10000) {
            pw.print(DateFormat.AM_PM);
            pw.print(appId - 10000);
        } else {
            pw.print('s');
            pw.print(appId);
        }
    }

    public static int parseUserArg(String arg) {
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

    @UnsupportedAppUsage
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
