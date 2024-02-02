package android.service.notification;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@SystemApi
/* loaded from: classes2.dex */
public final class NotificationStats implements Parcelable {
    public static final Parcelable.Creator<NotificationStats> CREATOR = new Parcelable.Creator<NotificationStats>() { // from class: android.service.notification.NotificationStats.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotificationStats createFromParcel(Parcel in) {
            return new NotificationStats(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotificationStats[] newArray(int size) {
            return new NotificationStats[size];
        }
    };
    public static final int DISMISSAL_AOD = 2;
    public static final int DISMISSAL_NOT_DISMISSED = -1;
    public static final int DISMISSAL_OTHER = 0;
    public static final int DISMISSAL_PEEK = 1;
    public static final int DISMISSAL_SHADE = 3;
    private boolean mDirectReplied;
    private int mDismissalSurface;
    private boolean mExpanded;
    private boolean mInteracted;
    private boolean mSeen;
    private boolean mSnoozed;
    private boolean mViewedSettings;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface DismissalSurface {
    }

    public NotificationStats() {
        this.mDismissalSurface = -1;
    }

    protected NotificationStats(Parcel in) {
        this.mDismissalSurface = -1;
        this.mSeen = in.readByte() != 0;
        this.mExpanded = in.readByte() != 0;
        this.mDirectReplied = in.readByte() != 0;
        this.mSnoozed = in.readByte() != 0;
        this.mViewedSettings = in.readByte() != 0;
        this.mInteracted = in.readByte() != 0;
        this.mDismissalSurface = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.mSeen ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mExpanded ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mDirectReplied ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mSnoozed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mViewedSettings ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mInteracted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mDismissalSurface);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean hasSeen() {
        return this.mSeen;
    }

    public void setSeen() {
        this.mSeen = true;
    }

    public boolean hasExpanded() {
        return this.mExpanded;
    }

    public void setExpanded() {
        this.mExpanded = true;
        this.mInteracted = true;
    }

    public boolean hasDirectReplied() {
        return this.mDirectReplied;
    }

    public void setDirectReplied() {
        this.mDirectReplied = true;
        this.mInteracted = true;
    }

    public boolean hasSnoozed() {
        return this.mSnoozed;
    }

    public void setSnoozed() {
        this.mSnoozed = true;
        this.mInteracted = true;
    }

    public boolean hasViewedSettings() {
        return this.mViewedSettings;
    }

    public void setViewedSettings() {
        this.mViewedSettings = true;
        this.mInteracted = true;
    }

    public boolean hasInteracted() {
        return this.mInteracted;
    }

    public int getDismissalSurface() {
        return this.mDismissalSurface;
    }

    public void setDismissalSurface(int dismissalSurface) {
        this.mDismissalSurface = dismissalSurface;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationStats that = (NotificationStats) o;
        if (this.mSeen == that.mSeen && this.mExpanded == that.mExpanded && this.mDirectReplied == that.mDirectReplied && this.mSnoozed == that.mSnoozed && this.mViewedSettings == that.mViewedSettings && this.mInteracted == that.mInteracted && this.mDismissalSurface == that.mDismissalSurface) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.mSeen ? 1 : 0;
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.mExpanded ? 1 : 0))) + (this.mDirectReplied ? 1 : 0))) + (this.mSnoozed ? 1 : 0))) + (this.mViewedSettings ? 1 : 0))) + (this.mInteracted ? 1 : 0))) + this.mDismissalSurface;
    }

    public String toString() {
        return "NotificationStats{mSeen=" + this.mSeen + ", mExpanded=" + this.mExpanded + ", mDirectReplied=" + this.mDirectReplied + ", mSnoozed=" + this.mSnoozed + ", mViewedSettings=" + this.mViewedSettings + ", mInteracted=" + this.mInteracted + ", mDismissalSurface=" + this.mDismissalSurface + '}';
    }
}
