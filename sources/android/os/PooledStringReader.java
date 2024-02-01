package android.os;
/* loaded from: classes2.dex */
public class PooledStringReader {
    private final Parcel mIn;
    private final String[] mPool;

    public synchronized PooledStringReader(Parcel in) {
        this.mIn = in;
        int size = in.readInt();
        this.mPool = new String[size];
    }

    public synchronized int getStringCount() {
        return this.mPool.length;
    }

    public synchronized String readString() {
        int idx = this.mIn.readInt();
        if (idx >= 0) {
            return this.mPool[idx];
        }
        String str = this.mIn.readString();
        this.mPool[(-idx) - 1] = str;
        return str;
    }
}
