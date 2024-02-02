package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.content.NativeLibraryHelper;
/* loaded from: classes2.dex */
public final class UidRange implements Parcelable {
    public static final Parcelable.Creator<UidRange> CREATOR = new Parcelable.Creator<UidRange>() { // from class: android.net.UidRange.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UidRange createFromParcel(Parcel in) {
            int start = in.readInt();
            int stop = in.readInt();
            return new UidRange(start, stop);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UidRange[] newArray(int size) {
            return new UidRange[size];
        }
    };
    public final int start;
    public final int stop;

    public synchronized UidRange(int startUid, int stopUid) {
        if (startUid < 0) {
            throw new IllegalArgumentException("Invalid start UID.");
        }
        if (stopUid < 0) {
            throw new IllegalArgumentException("Invalid stop UID.");
        }
        if (startUid > stopUid) {
            throw new IllegalArgumentException("Invalid UID range.");
        }
        this.start = startUid;
        this.stop = stopUid;
    }

    public static synchronized UidRange createForUser(int userId) {
        return new UidRange(userId * 100000, ((userId + 1) * 100000) - 1);
    }

    public synchronized int getStartUser() {
        return this.start / 100000;
    }

    public synchronized boolean contains(int uid) {
        return this.start <= uid && uid <= this.stop;
    }

    public synchronized int count() {
        return (1 + this.stop) - this.start;
    }

    public synchronized boolean containsRange(UidRange other) {
        return this.start <= other.start && other.stop <= this.stop;
    }

    public int hashCode() {
        int result = (31 * 17) + this.start;
        return (31 * result) + this.stop;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UidRange) {
            UidRange other = (UidRange) o;
            return this.start == other.start && this.stop == other.stop;
        }
        return false;
    }

    public String toString() {
        return this.start + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + this.stop;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.start);
        dest.writeInt(this.stop);
    }
}
