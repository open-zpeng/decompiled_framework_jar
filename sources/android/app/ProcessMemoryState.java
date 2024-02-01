package android.app;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class ProcessMemoryState implements Parcelable {
    public static final Parcelable.Creator<ProcessMemoryState> CREATOR = new Parcelable.Creator<ProcessMemoryState>() { // from class: android.app.ProcessMemoryState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProcessMemoryState createFromParcel(Parcel in) {
            return new ProcessMemoryState(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProcessMemoryState[] newArray(int size) {
            return new ProcessMemoryState[size];
        }
    };
    public long cacheInBytes;
    public int oomScore;
    public long pgfault;
    public long pgmajfault;
    public String processName;
    public long rssInBytes;
    public long swapInBytes;
    public int uid;

    public synchronized ProcessMemoryState(int uid, String processName, int oomScore, long pgfault, long pgmajfault, long rssInBytes, long cacheInBytes, long swapInBytes) {
        this.uid = uid;
        this.processName = processName;
        this.oomScore = oomScore;
        this.pgfault = pgfault;
        this.pgmajfault = pgmajfault;
        this.rssInBytes = rssInBytes;
        this.cacheInBytes = cacheInBytes;
        this.swapInBytes = swapInBytes;
    }

    private synchronized ProcessMemoryState(Parcel in) {
        this.uid = in.readInt();
        this.processName = in.readString();
        this.oomScore = in.readInt();
        this.pgfault = in.readLong();
        this.pgmajfault = in.readLong();
        this.rssInBytes = in.readLong();
        this.cacheInBytes = in.readLong();
        this.swapInBytes = in.readLong();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.uid);
        parcel.writeString(this.processName);
        parcel.writeInt(this.oomScore);
        parcel.writeLong(this.pgfault);
        parcel.writeLong(this.pgmajfault);
        parcel.writeLong(this.rssInBytes);
        parcel.writeLong(this.cacheInBytes);
        parcel.writeLong(this.swapInBytes);
    }
}
