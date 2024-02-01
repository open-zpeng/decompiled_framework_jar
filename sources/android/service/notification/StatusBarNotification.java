package android.service.notification;

import android.annotation.UnsupportedAppUsage;
import android.app.Notification;
import android.app.Person;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.metrics.LogMaker;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.logging.nano.MetricsProto;
import java.util.ArrayList;

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
    static final int MAX_LOG_TAG_LENGTH = 36;
    private String groupKey;
    @UnsupportedAppUsage
    private final int id;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final int initialPid;
    private final String key;
    private Context mContext;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final Notification notification;
    private final String opPkg;
    private String overrideGroupKey;
    @UnsupportedAppUsage
    private final String pkg;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final long postTime;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final String tag;
    @UnsupportedAppUsage
    private final int uid;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final UserHandle user;

    public StatusBarNotification(String pkg, String opPkg, int id, String tag, int uid, int initialPid, Notification notification, UserHandle user, String overrideGroupKey, long postTime) {
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

    private String key() {
        String sbnKey = this.user.getIdentifier() + "|" + this.pkg + "|" + this.id + "|" + this.tag + "|" + this.uid;
        if (this.overrideGroupKey != null && getNotification().isGroupSummary()) {
            return sbnKey + "|" + this.overrideGroupKey;
        }
        return sbnKey;
    }

    private String groupKey() {
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

    public boolean isAppGroup() {
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

    public StatusBarNotification cloneLight() {
        Notification no = new Notification();
        this.notification.cloneInto(no, false);
        return new StatusBarNotification(this.pkg, this.opPkg, this.id, this.tag, this.uid, this.initialPid, no, this.user, this.overrideGroupKey, this.postTime);
    }

    /* renamed from: clone */
    public StatusBarNotification m32clone() {
        return new StatusBarNotification(this.pkg, this.opPkg, this.id, this.tag, this.uid, this.initialPid, this.notification.m8clone(), this.user, this.overrideGroupKey, this.postTime);
    }

    public String toString() {
        return String.format("StatusBarNotification(pkg=%s user=%s id=%d tag=%s key=%s: %s)", this.pkg, this.user, Integer.valueOf(this.id), this.tag, this.key, this.notification);
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

    public int getUid() {
        return this.uid;
    }

    public String getOpPkg() {
        return this.opPkg;
    }

    @UnsupportedAppUsage
    public int getInitialPid() {
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

    public String getGroup() {
        String str = this.overrideGroupKey;
        if (str != null) {
            return str;
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

    public void clearPackageContext() {
        this.mContext = null;
    }

    @UnsupportedAppUsage
    public Context getPackageContext(Context context) {
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
        Notification notification = this.notification;
        if (notification != null) {
            notification.number = number;
        }
    }

    public LogMaker getLogMaker() {
        LogMaker logMaker = new LogMaker(0).setPackageName(getPackageName()).addTaggedData(MetricsProto.MetricsEvent.NOTIFICATION_ID, Integer.valueOf(getId())).addTaggedData(MetricsProto.MetricsEvent.NOTIFICATION_TAG, getTag()).addTaggedData(MetricsProto.MetricsEvent.FIELD_NOTIFICATION_CHANNEL_ID, getChannelIdLogTag()).addTaggedData(MetricsProto.MetricsEvent.FIELD_NOTIFICATION_GROUP_ID, getGroupLogTag()).addTaggedData(MetricsProto.MetricsEvent.FIELD_NOTIFICATION_GROUP_SUMMARY, Integer.valueOf(getNotification().isGroupSummary() ? 1 : 0)).addTaggedData(MetricsProto.MetricsEvent.FIELD_NOTIFICATION_CATEGORY, getNotification().category);
        if (getNotification().extras != null) {
            String template = getNotification().extras.getString(Notification.EXTRA_TEMPLATE);
            if (template != null && !template.isEmpty()) {
                logMaker.addTaggedData(MetricsProto.MetricsEvent.FIELD_NOTIFICATION_STYLE, Integer.valueOf(template.hashCode()));
            }
            ArrayList<Person> people = getNotification().extras.getParcelableArrayList(Notification.EXTRA_PEOPLE_LIST);
            if (people != null && !people.isEmpty()) {
                logMaker.addTaggedData(MetricsProto.MetricsEvent.FIELD_NOTIFICATION_PEOPLE, Integer.valueOf(people.size()));
            }
        }
        return logMaker;
    }

    private String getGroupLogTag() {
        return shortenTag(getGroup());
    }

    private String getChannelIdLogTag() {
        if (this.notification.getChannelId() == null) {
            return null;
        }
        return shortenTag(this.notification.getChannelId());
    }

    private String shortenTag(String logTag) {
        if (logTag == null || logTag.length() <= 36) {
            return logTag;
        }
        String hash = Integer.toHexString(logTag.hashCode());
        return logTag.substring(0, (36 - hash.length()) - 1) + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + hash;
    }
}
