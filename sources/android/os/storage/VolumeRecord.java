package android.os.storage;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.DebugUtils;
import android.util.TimeUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import java.util.Objects;
/* loaded from: classes2.dex */
public class VolumeRecord implements Parcelable {
    private protected static final Parcelable.Creator<VolumeRecord> CREATOR = new Parcelable.Creator<VolumeRecord>() { // from class: android.os.storage.VolumeRecord.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VolumeRecord createFromParcel(Parcel in) {
            return new VolumeRecord(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VolumeRecord[] newArray(int size) {
            return new VolumeRecord[size];
        }
    };
    public static final String EXTRA_FS_UUID = "android.os.storage.extra.FS_UUID";
    public static final int USER_FLAG_INITED = 1;
    public static final int USER_FLAG_SNOOZED = 2;
    public long createdMillis;
    public final String fsUuid;
    public long lastBenchMillis;
    public long lastTrimMillis;
    public String nickname;
    public String partGuid;
    public final int type;
    public int userFlags;

    public synchronized VolumeRecord(int type, String fsUuid) {
        this.type = type;
        this.fsUuid = (String) Preconditions.checkNotNull(fsUuid);
    }

    private protected VolumeRecord(Parcel parcel) {
        this.type = parcel.readInt();
        this.fsUuid = parcel.readString();
        this.partGuid = parcel.readString();
        this.nickname = parcel.readString();
        this.userFlags = parcel.readInt();
        this.createdMillis = parcel.readLong();
        this.lastTrimMillis = parcel.readLong();
        this.lastBenchMillis = parcel.readLong();
    }

    public synchronized int getType() {
        return this.type;
    }

    public synchronized String getFsUuid() {
        return this.fsUuid;
    }

    public synchronized String getNickname() {
        return this.nickname;
    }

    public synchronized boolean isInited() {
        return (this.userFlags & 1) != 0;
    }

    public synchronized boolean isSnoozed() {
        return (this.userFlags & 2) != 0;
    }

    public synchronized void dump(IndentingPrintWriter pw) {
        pw.println("VolumeRecord:");
        pw.increaseIndent();
        pw.printPair("type", DebugUtils.valueToString(VolumeInfo.class, "TYPE_", this.type));
        pw.printPair("fsUuid", this.fsUuid);
        pw.printPair("partGuid", this.partGuid);
        pw.println();
        pw.printPair("nickname", this.nickname);
        pw.printPair("userFlags", DebugUtils.flagsToString(VolumeRecord.class, "USER_FLAG_", this.userFlags));
        pw.println();
        pw.printPair("createdMillis", TimeUtils.formatForLogging(this.createdMillis));
        pw.printPair("lastTrimMillis", TimeUtils.formatForLogging(this.lastTrimMillis));
        pw.printPair("lastBenchMillis", TimeUtils.formatForLogging(this.lastBenchMillis));
        pw.decreaseIndent();
        pw.println();
    }

    /* renamed from: clone */
    public VolumeRecord m47clone() {
        Parcel temp = Parcel.obtain();
        try {
            writeToParcel(temp, 0);
            temp.setDataPosition(0);
            return CREATOR.createFromParcel(temp);
        } finally {
            temp.recycle();
        }
    }

    public boolean equals(Object o) {
        if (o instanceof VolumeRecord) {
            return Objects.equals(this.fsUuid, ((VolumeRecord) o).fsUuid);
        }
        return false;
    }

    public int hashCode() {
        return this.fsUuid.hashCode();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.type);
        parcel.writeString(this.fsUuid);
        parcel.writeString(this.partGuid);
        parcel.writeString(this.nickname);
        parcel.writeInt(this.userFlags);
        parcel.writeLong(this.createdMillis);
        parcel.writeLong(this.lastTrimMillis);
        parcel.writeLong(this.lastBenchMillis);
    }
}
