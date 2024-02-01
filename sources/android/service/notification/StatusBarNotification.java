package android.service.notification;

import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
/* loaded from: classes2.dex */
public class StatusBarNotification implements Parcelable {
    public static final Parcelable.Creator<StatusBarNotification> CREATOR = new Parcelable.Creator<StatusBarNotification>() { // from class: android.service.notification.StatusBarNotification.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StatusBarNotification createFromParcel(Parcel parcel) {
            return new StatusBarNotification(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StatusBarNotification[] newArray(int size) {
            return new StatusBarNotification[size];
        }
    };
    private String groupKey;
    public protected final int id;
    public protected final int initialPid;
    private final String key;
    private Context mContext;
    public protected final Notification notification;
    private final String opPkg;
    private String overrideGroupKey;
    public protected final String pkg;
    public protected final long postTime;
    public protected final String tag;
    public protected final int uid;
    public protected final UserHandle user;

    public synchronized StatusBarNotification(String pkg, String opPkg, int id, String tag, int uid, int initialPid, Notification notification, UserHandle user, String overrideGroupKey, long postTime) {
        if (pkg == null) {
            throw new NullPointerException();
        }
        if (notification == null) {
            throw new NullPointerException();
        }
        this.pkg = pkg;
        this.opPkg = opPkg;
        this.id = id;
        this.tag = tag;
        this.uid = uid;
        this.initialPid = initialPid;
        this.notification = notification;
        this.user = user;
        this.postTime = postTime;
        this.overrideGroupKey = overrideGroupKey;
        this.key = key();
        this.groupKey = groupKey();
    }

    @Deprecated
    public StatusBarNotification(String pkg, String opPkg, int id, String tag, int uid, int initialPid, int score, Notification notification, UserHandle user, long postTime) {
        if (pkg == null) {
            throw new NullPointerException();
        }
        if (notification == null) {
            throw new NullPointerException();
        }
        this.pkg = pkg;
        this.opPkg = opPkg;
        this.id = id;
        this.tag = tag;
        this.uid = uid;
        this.initialPid = initialPid;
        this.notification = notification;
        this.user = user;
        this.postTime = postTime;
        this.key = key();
        this.groupKey = groupKey();
    }

    public StatusBarNotification(Parcel in) {
        this.pkg = in.readString();
        this.opPkg = in.readString();
        this.id = in.readInt();
        if (in.readInt() != 0) {
            this.tag = in.readString();
        } else {
            this.tag = null;
        }
        this.uid = in.readInt();
        this.initialPid = in.readInt();
        this.notification = new Notification(in);
        this.user = UserHandle.readFromParcel(in);
        this.postTime = in.readLong();
        if (in.readInt() != 0) {
            this.overrideGroupKey = in.readString();
        } else {
            this.overrideGroupKey = null;
        }
        this.key = key();
        this.groupKey = groupKey();
    }

    private synchronized String key() {
        String sbnKey = this.user.getIdentifier() + "|" + this.pkg + "|" + this.id + "|" + this.tag + "|" + this.uid;
        if (this.overrideGroupKey != null && getNotification().isGroupSummary()) {
            return sbnKey + "|" + this.overrideGroupKey;
        }
        return sbnKey;
    }

    private synchronized String groupKey() {
        String str;
        if (this.overrideGroupKey != null) {
            return this.user.getIdentifier() + "|" + this.pkg + "|g:" + this.overrideGroupKey;
        }
        String group = getNotification().getGroup();
        String sortKey = getNotification().getSortKey();
        if (group == null && sortKey == null) {
            return this.key;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.user.getIdentifier());
        sb.append("|");
        sb.append(this.pkg);
        sb.append("|");
        if (group == null) {
            str = "c:" + this.notification.getChannelId();
        } else {
            str = "g:" + group;
        }
        sb.append(str);
        return sb.toString();
    }

    public boolean isGroup() {
        if (this.overrideGroupKey != null || isAppGroup()) {
            return true;
        }
        return false;
    }

    public synchronized boolean isAppGroup() {
        if (getNotification().getGroup() != null || getNotification().getSortKey() != null) {
            return true;
        }
        return false;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.pkg);
        out.writeString(this.opPkg);
        out.writeInt(this.id);
        if (this.tag != null) {
            out.writeInt(1);
            out.writeString(this.tag);
        } else {
            out.writeInt(0);
        }
        out.writeInt(this.uid);
        out.writeInt(this.initialPid);
        this.notification.writeToParcel(out, flags);
        this.user.writeToParcel(out, flags);
        out.writeLong(this.postTime);
        if (this.overrideGroupKey != null) {
            out.writeInt(1);
            out.writeString(this.overrideGroupKey);
            return;
        }
        out.writeInt(0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public synchronized StatusBarNotification cloneLight() {
        Notification no = new Notification();
        this.notification.cloneInto(no, false);
        return new StatusBarNotification(this.pkg, this.opPkg, this.id, this.tag, this.uid, this.initialPid, no, this.user, this.overrideGroupKey, this.postTime);
    }

    /* renamed from: clone */
    public StatusBarNotification m50clone() {
        return new StatusBarNotification(this.pkg, this.opPkg, this.id, this.tag, this.uid, this.initialPid, this.notification.m8clone(), this.user, this.overrideGroupKey, this.postTime);
    }

    public String toString() {
        return String.format("StatusBarNotification(pkg=%s user=%s id=%d tag=%s key=%s postTime=%d: %s)", this.pkg, this.user, Integer.valueOf(this.id), this.tag, this.key, Long.valueOf(this.postTime), this.notification);
    }

    public boolean isOngoing() {
        return (this.notification.flags & 2) != 0;
    }

    public boolean isClearable() {
        return (this.notification.flags & 2) == 0 && (this.notification.flags & 32) == 0;
    }

    @Deprecated
    public int getUserId() {
        return this.user.getIdentifier();
    }

    public String getPackageName() {
        return this.pkg;
    }

    public int getId() {
        return this.id;
    }

    public String getTag() {
        return this.tag;
    }

    private protected int getUid() {
        return this.uid;
    }

    private protected String getOpPkg() {
        return this.opPkg;
    }

    private protected int getInitialPid() {
        return this.initialPid;
    }

    public Notification getNotification() {
        return this.notification;
    }

    public UserHandle getUser() {
        return this.user;
    }

    public long getPostTime() {
        return this.postTime;
    }

    public String getKey() {
        return this.key;
    }

    public String getGroupKey() {
        return this.groupKey;
    }

    public synchronized String getGroup() {
        if (this.overrideGroupKey != null) {
            return this.overrideGroupKey;
        }
        return getNotification().getGroup();
    }

    public void setOverrideGroupKey(String overrideGroupKey) {
        this.overrideGroupKey = overrideGroupKey;
        this.groupKey = groupKey();
    }

    public String getOverrideGroupKey() {
        return this.overrideGroupKey;
    }

    private protected Context getPackageContext(Context context) {
        if (this.mContext == null) {
            try {
                ApplicationInfo ai = context.getPackageManager().getApplicationInfoAsUser(this.pkg, 8192, getUserId());
                this.mContext = context.createApplicationContext(ai, 4);
            } catch (PackageManager.NameNotFoundException e) {
                this.mContext = null;
            }
        }
        if (this.mContext == null) {
            this.mContext = context;
        }
        return this.mContext;
    }

    public void setNotificationNumber(int number) {
        if (this.notification != null) {
            this.notification.number = number;
        }
    }
}
