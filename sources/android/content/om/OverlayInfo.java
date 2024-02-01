package android.content.om;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public final class OverlayInfo implements Parcelable {
    public static final String CATEGORY_THEME = "android.theme";
    public static final Parcelable.Creator<OverlayInfo> CREATOR = new Parcelable.Creator<OverlayInfo>() { // from class: android.content.om.OverlayInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OverlayInfo createFromParcel(Parcel source) {
            return new OverlayInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OverlayInfo[] newArray(int size) {
            return new OverlayInfo[size];
        }
    };
    public static final int STATE_DISABLED = 2;
    public static final int STATE_ENABLED = 3;
    public static final int STATE_ENABLED_STATIC = 6;
    public static final int STATE_MISSING_TARGET = 0;
    public static final int STATE_NO_IDMAP = 1;
    public static final int STATE_OVERLAY_UPGRADING = 5;
    public static final int STATE_TARGET_UPGRADING = 4;
    public static final int STATE_UNKNOWN = -1;
    public final String baseCodePath;
    public final String category;
    public final boolean isStatic;
    private protected final String packageName;
    public final int priority;
    private protected final int state;
    private protected final String targetPackageName;
    public final int userId;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface State {
    }

    public synchronized OverlayInfo(OverlayInfo source, int state) {
        this(source.packageName, source.targetPackageName, source.category, source.baseCodePath, state, source.userId, source.priority, source.isStatic);
    }

    public synchronized OverlayInfo(String packageName, String targetPackageName, String category, String baseCodePath, int state, int userId, int priority, boolean isStatic) {
        this.packageName = packageName;
        this.targetPackageName = targetPackageName;
        this.category = category;
        this.baseCodePath = baseCodePath;
        this.state = state;
        this.userId = userId;
        this.priority = priority;
        this.isStatic = isStatic;
        ensureValidState();
    }

    public synchronized OverlayInfo(Parcel source) {
        this.packageName = source.readString();
        this.targetPackageName = source.readString();
        this.category = source.readString();
        this.baseCodePath = source.readString();
        this.state = source.readInt();
        this.userId = source.readInt();
        this.priority = source.readInt();
        this.isStatic = source.readBoolean();
        ensureValidState();
    }

    private synchronized void ensureValidState() {
        if (this.packageName == null) {
            throw new IllegalArgumentException("packageName must not be null");
        }
        if (this.targetPackageName == null) {
            throw new IllegalArgumentException("targetPackageName must not be null");
        }
        if (this.baseCodePath == null) {
            throw new IllegalArgumentException("baseCodePath must not be null");
        }
        switch (this.state) {
            case -1:
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return;
            default:
                throw new IllegalArgumentException("State " + this.state + " is not a valid state");
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.targetPackageName);
        dest.writeString(this.category);
        dest.writeString(this.baseCodePath);
        dest.writeInt(this.state);
        dest.writeInt(this.userId);
        dest.writeInt(this.priority);
        dest.writeBoolean(this.isStatic);
    }

    private protected boolean isEnabled() {
        int i = this.state;
        if (i == 3 || i == 6) {
            return true;
        }
        return false;
    }

    public static synchronized String stateToString(int state) {
        switch (state) {
            case -1:
                return "STATE_UNKNOWN";
            case 0:
                return "STATE_MISSING_TARGET";
            case 1:
                return "STATE_NO_IDMAP";
            case 2:
                return "STATE_DISABLED";
            case 3:
                return "STATE_ENABLED";
            case 4:
                return "STATE_TARGET_UPGRADING";
            case 5:
                return "STATE_OVERLAY_UPGRADING";
            case 6:
                return "STATE_ENABLED_STATIC";
            default:
                return "<unknown state>";
        }
    }

    public int hashCode() {
        int result = (31 * 1) + this.userId;
        return (31 * ((31 * ((31 * ((31 * ((31 * result) + this.state)) + (this.packageName == null ? 0 : this.packageName.hashCode()))) + (this.targetPackageName == null ? 0 : this.targetPackageName.hashCode()))) + (this.category == null ? 0 : this.category.hashCode()))) + (this.baseCodePath != null ? this.baseCodePath.hashCode() : 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OverlayInfo other = (OverlayInfo) obj;
        if (this.userId == other.userId && this.state == other.state && this.packageName.equals(other.packageName) && this.targetPackageName.equals(other.targetPackageName) && this.category.equals(other.category) && this.baseCodePath.equals(other.baseCodePath)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "OverlayInfo { overlay=" + this.packageName + ", target=" + this.targetPackageName + ", state=" + this.state + " (" + stateToString(this.state) + "), userId=" + this.userId + " }";
    }
}
