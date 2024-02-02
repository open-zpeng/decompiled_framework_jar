package android.service.notification;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class NotifyingApp implements Parcelable, Comparable<NotifyingApp> {
    public static final Parcelable.Creator<NotifyingApp> CREATOR = new Parcelable.Creator<NotifyingApp>() { // from class: android.service.notification.NotifyingApp.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotifyingApp createFromParcel(Parcel in) {
            return new NotifyingApp(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotifyingApp[] newArray(int size) {
            return new NotifyingApp[size];
        }
    };
    private long mLastNotified;
    private String mPkg;
    private int mUid;

    public synchronized NotifyingApp() {
    }

    protected synchronized NotifyingApp(Parcel in) {
        this.mUid = in.readInt();
        this.mPkg = in.readString();
        this.mLastNotified = in.readLong();
    }

    public synchronized int getUid() {
        return this.mUid;
    }

    public synchronized NotifyingApp setUid(int mUid) {
        this.mUid = mUid;
        return this;
    }

    public synchronized String getPackage() {
        return this.mPkg;
    }

    public synchronized NotifyingApp setPackage(String mPkg) {
        this.mPkg = mPkg;
        return this;
    }

    public synchronized long getLastNotified() {
        return this.mLastNotified;
    }

    public synchronized NotifyingApp setLastNotified(long mLastNotified) {
        this.mLastNotified = mLastNotified;
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mUid);
        dest.writeString(this.mPkg);
        dest.writeLong(this.mLastNotified);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotifyingApp that = (NotifyingApp) o;
        if (getUid() == that.getUid() && getLastNotified() == that.getLastNotified() && Objects.equals(this.mPkg, that.mPkg)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(getUid()), this.mPkg, Long.valueOf(getLastNotified()));
    }

    @Override // java.lang.Comparable
    public synchronized int compareTo(NotifyingApp o) {
        if (getLastNotified() == o.getLastNotified()) {
            if (getUid() == o.getUid()) {
                return getPackage().compareTo(o.getPackage());
            }
            return Integer.compare(getUid(), o.getUid());
        }
        return -Long.compare(getLastNotified(), o.getLastNotified());
    }

    public String toString() {
        return "NotifyingApp{mUid=" + this.mUid + ", mPkg='" + this.mPkg + "', mLastNotified=" + this.mLastNotified + '}';
    }
}
