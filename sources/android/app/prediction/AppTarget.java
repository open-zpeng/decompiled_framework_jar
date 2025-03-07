package android.app.prediction;

import android.annotation.SystemApi;
import android.content.pm.ShortcutInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
import com.android.internal.util.Preconditions;

@SystemApi
/* loaded from: classes.dex */
public final class AppTarget implements Parcelable {
    public static final Parcelable.Creator<AppTarget> CREATOR = new Parcelable.Creator<AppTarget>() { // from class: android.app.prediction.AppTarget.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppTarget createFromParcel(Parcel parcel) {
            return new AppTarget(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppTarget[] newArray(int size) {
            return new AppTarget[size];
        }
    };
    private final String mClassName;
    private final AppTargetId mId;
    private final String mPackageName;
    private final int mRank;
    private final ShortcutInfo mShortcutInfo;
    private final UserHandle mUser;

    @Deprecated
    public AppTarget(AppTargetId id, String packageName, String className, UserHandle user) {
        this.mId = id;
        this.mShortcutInfo = null;
        this.mPackageName = (String) Preconditions.checkNotNull(packageName);
        this.mClassName = className;
        this.mUser = (UserHandle) Preconditions.checkNotNull(user);
        this.mRank = 0;
    }

    @Deprecated
    public AppTarget(AppTargetId id, ShortcutInfo shortcutInfo, String className) {
        this.mId = id;
        this.mShortcutInfo = (ShortcutInfo) Preconditions.checkNotNull(shortcutInfo);
        this.mPackageName = this.mShortcutInfo.getPackage();
        this.mUser = this.mShortcutInfo.getUserHandle();
        this.mClassName = className;
        this.mRank = 0;
    }

    private AppTarget(AppTargetId id, String packageName, UserHandle user, ShortcutInfo shortcutInfo, String className, int rank) {
        this.mId = id;
        this.mShortcutInfo = shortcutInfo;
        this.mPackageName = packageName;
        this.mClassName = className;
        this.mUser = user;
        this.mRank = rank;
    }

    private AppTarget(Parcel parcel) {
        this.mId = (AppTargetId) parcel.readTypedObject(AppTargetId.CREATOR);
        this.mShortcutInfo = (ShortcutInfo) parcel.readTypedObject(ShortcutInfo.CREATOR);
        ShortcutInfo shortcutInfo = this.mShortcutInfo;
        if (shortcutInfo == null) {
            this.mPackageName = parcel.readString();
            this.mUser = UserHandle.of(parcel.readInt());
        } else {
            this.mPackageName = shortcutInfo.getPackage();
            this.mUser = this.mShortcutInfo.getUserHandle();
        }
        this.mClassName = parcel.readString();
        this.mRank = parcel.readInt();
    }

    public AppTargetId getId() {
        return this.mId;
    }

    public String getClassName() {
        return this.mClassName;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public UserHandle getUser() {
        return this.mUser;
    }

    public ShortcutInfo getShortcutInfo() {
        return this.mShortcutInfo;
    }

    public int getRank() {
        return this.mRank;
    }

    public boolean equals(Object o) {
        String str;
        ShortcutInfo shortcutInfo;
        if (getClass().equals(o != null ? o.getClass() : null)) {
            AppTarget other = (AppTarget) o;
            boolean sameClassName = (this.mClassName == null && other.mClassName == null) || ((str = this.mClassName) != null && str.equals(other.mClassName));
            boolean sameShortcutInfo = (this.mShortcutInfo == null && other.mShortcutInfo == null) || !((shortcutInfo = this.mShortcutInfo) == null || other.mShortcutInfo == null || shortcutInfo.getId() != other.mShortcutInfo.getId());
            return this.mId.equals(other.mId) && this.mPackageName.equals(other.mPackageName) && sameClassName && this.mUser.equals(other.mUser) && sameShortcutInfo && this.mRank == other.mRank;
        }
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(this.mId, flags);
        dest.writeTypedObject(this.mShortcutInfo, flags);
        if (this.mShortcutInfo == null) {
            dest.writeString(this.mPackageName);
            dest.writeInt(this.mUser.getIdentifier());
        }
        dest.writeString(this.mClassName);
        dest.writeInt(this.mRank);
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static final class Builder {
        private String mClassName;
        private final AppTargetId mId;
        private String mPackageName;
        private int mRank;
        private ShortcutInfo mShortcutInfo;
        private UserHandle mUser;

        @SystemApi
        @Deprecated
        public Builder(AppTargetId id) {
            this.mId = id;
        }

        @SystemApi
        public Builder(AppTargetId id, String packageName, UserHandle user) {
            this.mId = (AppTargetId) Preconditions.checkNotNull(id);
            this.mPackageName = (String) Preconditions.checkNotNull(packageName);
            this.mUser = (UserHandle) Preconditions.checkNotNull(user);
        }

        @SystemApi
        public Builder(AppTargetId id, ShortcutInfo info) {
            this.mId = (AppTargetId) Preconditions.checkNotNull(id);
            this.mShortcutInfo = (ShortcutInfo) Preconditions.checkNotNull(info);
            this.mPackageName = info.getPackage();
            this.mUser = info.getUserHandle();
        }

        @Deprecated
        public Builder setTarget(String packageName, UserHandle user) {
            if (this.mPackageName != null) {
                throw new IllegalArgumentException("Target is already set");
            }
            this.mPackageName = (String) Preconditions.checkNotNull(packageName);
            this.mUser = (UserHandle) Preconditions.checkNotNull(user);
            return this;
        }

        @Deprecated
        public Builder setTarget(ShortcutInfo info) {
            setTarget(info.getPackage(), info.getUserHandle());
            this.mShortcutInfo = (ShortcutInfo) Preconditions.checkNotNull(info);
            return this;
        }

        public Builder setClassName(String className) {
            this.mClassName = (String) Preconditions.checkNotNull(className);
            return this;
        }

        public Builder setRank(int rank) {
            if (rank < 0) {
                throw new IllegalArgumentException("rank cannot be a negative value");
            }
            this.mRank = rank;
            return this;
        }

        public AppTarget build() {
            String str = this.mPackageName;
            if (str == null) {
                throw new IllegalStateException("No target is set");
            }
            return new AppTarget(this.mId, str, this.mUser, this.mShortcutInfo, this.mClassName, this.mRank);
        }
    }
}
