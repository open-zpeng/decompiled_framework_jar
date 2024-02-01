package android.os;

import android.os.Parcelable;
import android.util.EventLog;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
/* loaded from: classes2.dex */
public final class StatsLogEventWrapper implements Parcelable {
    public static final Parcelable.Creator<StatsLogEventWrapper> CREATOR = new Parcelable.Creator<StatsLogEventWrapper>() { // from class: android.os.StatsLogEventWrapper.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StatsLogEventWrapper createFromParcel(Parcel in) {
            EventLog.writeEvent(1397638484, "112550251", Integer.valueOf(Binder.getCallingUid()), "");
            throw new RuntimeException("Not implemented");
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StatsLogEventWrapper[] newArray(int size) {
            EventLog.writeEvent(1397638484, "112550251", Integer.valueOf(Binder.getCallingUid()), "");
            throw new RuntimeException("Not implemented");
        }
    };
    private static final int EVENT_TYPE_FLOAT = 4;
    private static final int EVENT_TYPE_INT = 0;
    private static final int EVENT_TYPE_LIST = 3;
    private static final int EVENT_TYPE_LONG = 1;
    private static final int EVENT_TYPE_STRING = 2;
    private static final int STATS_BUFFER_TAG_ID = 1937006964;
    private ByteArrayOutputStream mStorage = new ByteArrayOutputStream();

    public synchronized StatsLogEventWrapper(long elapsedNanos, int tag, int fields) {
        write4Bytes(STATS_BUFFER_TAG_ID);
        this.mStorage.write(3);
        this.mStorage.write(fields + 2);
        writeLong(elapsedNanos);
        writeInt(tag);
    }

    private synchronized void write4Bytes(int val) {
        this.mStorage.write(val);
        this.mStorage.write(val >>> 8);
        this.mStorage.write(val >>> 16);
        this.mStorage.write(val >>> 24);
    }

    private synchronized void write8Bytes(long val) {
        write4Bytes((int) ((-1) & val));
        write4Bytes((int) (val >>> 32));
    }

    public synchronized void writeInt(int val) {
        this.mStorage.write(0);
        write4Bytes(val);
    }

    public synchronized void writeLong(long val) {
        this.mStorage.write(1);
        write8Bytes(val);
    }

    public synchronized void writeFloat(float val) {
        int v = Float.floatToIntBits(val);
        this.mStorage.write(4);
        write4Bytes(v);
    }

    public synchronized void writeString(String val) {
        this.mStorage.write(2);
        write4Bytes(val.length());
        byte[] bytes = val.getBytes(StandardCharsets.UTF_8);
        this.mStorage.write(bytes, 0, bytes.length);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        this.mStorage.write(10);
        out.writeByteArray(this.mStorage.toByteArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
