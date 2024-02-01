package android.net;

import android.net.NetworkStats;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.MathUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import java.io.CharArrayWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.Random;
import libcore.util.EmptyArray;
/* loaded from: classes2.dex */
public class NetworkStatsHistory implements Parcelable {
    private protected static final Parcelable.Creator<NetworkStatsHistory> CREATOR = new Parcelable.Creator<NetworkStatsHistory>() { // from class: android.net.NetworkStatsHistory.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkStatsHistory createFromParcel(Parcel in) {
            return new NetworkStatsHistory(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkStatsHistory[] newArray(int size) {
            return new NetworkStatsHistory[size];
        }
    };
    public static final int FIELD_ACTIVE_TIME = 1;
    public static final int FIELD_ALL = -1;
    public static final int FIELD_OPERATIONS = 32;
    public static final int FIELD_RX_BYTES = 2;
    public static final int FIELD_RX_PACKETS = 4;
    public static final int FIELD_TX_BYTES = 8;
    public static final int FIELD_TX_PACKETS = 16;
    private static final int VERSION_ADD_ACTIVE = 3;
    private static final int VERSION_ADD_PACKETS = 2;
    private static final int VERSION_INIT = 1;
    private long[] activeTime;
    private int bucketCount;
    private long bucketDuration;
    private long[] bucketStart;
    private long[] operations;
    private long[] rxBytes;
    private long[] rxPackets;
    private long totalBytes;
    private long[] txBytes;
    private long[] txPackets;

    /* loaded from: classes2.dex */
    public static class Entry {
        public static final long UNKNOWN = -1;
        public long activeTime;
        private protected long bucketDuration;
        private protected long bucketStart;
        public long operations;
        private protected long rxBytes;
        private protected long rxPackets;
        private protected long txBytes;
        private protected long txPackets;
    }

    private protected NetworkStatsHistory(long bucketDuration) {
        this(bucketDuration, 10, -1);
    }

    public synchronized NetworkStatsHistory(long bucketDuration, int initialSize) {
        this(bucketDuration, initialSize, -1);
    }

    public synchronized NetworkStatsHistory(long bucketDuration, int initialSize, int fields) {
        this.bucketDuration = bucketDuration;
        this.bucketStart = new long[initialSize];
        if ((fields & 1) != 0) {
            this.activeTime = new long[initialSize];
        }
        if ((fields & 2) != 0) {
            this.rxBytes = new long[initialSize];
        }
        if ((fields & 4) != 0) {
            this.rxPackets = new long[initialSize];
        }
        if ((fields & 8) != 0) {
            this.txBytes = new long[initialSize];
        }
        if ((fields & 16) != 0) {
            this.txPackets = new long[initialSize];
        }
        if ((fields & 32) != 0) {
            this.operations = new long[initialSize];
        }
        this.bucketCount = 0;
        this.totalBytes = 0L;
    }

    public synchronized NetworkStatsHistory(NetworkStatsHistory existing, long bucketDuration) {
        this(bucketDuration, existing.estimateResizeBuckets(bucketDuration));
        recordEntireHistory(existing);
    }

    private protected NetworkStatsHistory(Parcel in) {
        this.bucketDuration = in.readLong();
        this.bucketStart = ParcelUtils.readLongArray(in);
        this.activeTime = ParcelUtils.readLongArray(in);
        this.rxBytes = ParcelUtils.readLongArray(in);
        this.rxPackets = ParcelUtils.readLongArray(in);
        this.txBytes = ParcelUtils.readLongArray(in);
        this.txPackets = ParcelUtils.readLongArray(in);
        this.operations = ParcelUtils.readLongArray(in);
        this.bucketCount = this.bucketStart.length;
        this.totalBytes = in.readLong();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.bucketDuration);
        ParcelUtils.writeLongArray(out, this.bucketStart, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.activeTime, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.rxBytes, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.rxPackets, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.txBytes, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.txPackets, this.bucketCount);
        ParcelUtils.writeLongArray(out, this.operations, this.bucketCount);
        out.writeLong(this.totalBytes);
    }

    public synchronized NetworkStatsHistory(DataInputStream in) throws IOException {
        int version = in.readInt();
        switch (version) {
            case 1:
                this.bucketDuration = in.readLong();
                this.bucketStart = DataStreamUtils.readFullLongArray(in);
                this.rxBytes = DataStreamUtils.readFullLongArray(in);
                this.rxPackets = new long[this.bucketStart.length];
                this.txBytes = DataStreamUtils.readFullLongArray(in);
                this.txPackets = new long[this.bucketStart.length];
                this.operations = new long[this.bucketStart.length];
                this.bucketCount = this.bucketStart.length;
                this.totalBytes = ArrayUtils.total(this.rxBytes) + ArrayUtils.total(this.txBytes);
                break;
            case 2:
            case 3:
                this.bucketDuration = in.readLong();
                this.bucketStart = DataStreamUtils.readVarLongArray(in);
                this.activeTime = version >= 3 ? DataStreamUtils.readVarLongArray(in) : new long[this.bucketStart.length];
                this.rxBytes = DataStreamUtils.readVarLongArray(in);
                this.rxPackets = DataStreamUtils.readVarLongArray(in);
                this.txBytes = DataStreamUtils.readVarLongArray(in);
                this.txPackets = DataStreamUtils.readVarLongArray(in);
                this.operations = DataStreamUtils.readVarLongArray(in);
                this.bucketCount = this.bucketStart.length;
                this.totalBytes = ArrayUtils.total(this.rxBytes) + ArrayUtils.total(this.txBytes);
                break;
            default:
                throw new ProtocolException("unexpected version: " + version);
        }
        if (this.bucketStart.length != this.bucketCount || this.rxBytes.length != this.bucketCount || this.rxPackets.length != this.bucketCount || this.txBytes.length != this.bucketCount || this.txPackets.length != this.bucketCount || this.operations.length != this.bucketCount) {
            throw new ProtocolException("Mismatched history lengths");
        }
    }

    public synchronized void writeToStream(DataOutputStream out) throws IOException {
        out.writeInt(3);
        out.writeLong(this.bucketDuration);
        DataStreamUtils.writeVarLongArray(out, this.bucketStart, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.activeTime, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.rxBytes, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.rxPackets, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.txBytes, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.txPackets, this.bucketCount);
        DataStreamUtils.writeVarLongArray(out, this.operations, this.bucketCount);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int size() {
        return this.bucketCount;
    }

    public synchronized long getBucketDuration() {
        return this.bucketDuration;
    }

    private protected long getStart() {
        if (this.bucketCount > 0) {
            return this.bucketStart[0];
        }
        return Long.MAX_VALUE;
    }

    private protected long getEnd() {
        if (this.bucketCount > 0) {
            return this.bucketStart[this.bucketCount - 1] + this.bucketDuration;
        }
        return Long.MIN_VALUE;
    }

    public synchronized long getTotalBytes() {
        return this.totalBytes;
    }

    private protected int getIndexBefore(long time) {
        int index;
        int index2 = Arrays.binarySearch(this.bucketStart, 0, this.bucketCount, time);
        if (index2 < 0) {
            index = (~index2) - 1;
        } else {
            index = index2 - 1;
        }
        return MathUtils.constrain(index, 0, this.bucketCount - 1);
    }

    public synchronized int getIndexAfter(long time) {
        int index;
        int index2 = Arrays.binarySearch(this.bucketStart, 0, this.bucketCount, time);
        if (index2 < 0) {
            index = ~index2;
        } else {
            index = index2 + 1;
        }
        return MathUtils.constrain(index, 0, this.bucketCount - 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Entry getValues(int i, Entry recycle) {
        Entry entry = recycle != null ? recycle : new Entry();
        entry.bucketStart = this.bucketStart[i];
        entry.bucketDuration = this.bucketDuration;
        entry.activeTime = getLong(this.activeTime, i, -1L);
        entry.rxBytes = getLong(this.rxBytes, i, -1L);
        entry.rxPackets = getLong(this.rxPackets, i, -1L);
        entry.txBytes = getLong(this.txBytes, i, -1L);
        entry.txPackets = getLong(this.txPackets, i, -1L);
        entry.operations = getLong(this.operations, i, -1L);
        return entry;
    }

    public synchronized void setValues(int i, Entry entry) {
        if (this.rxBytes != null) {
            this.totalBytes -= this.rxBytes[i];
        }
        if (this.txBytes != null) {
            this.totalBytes -= this.txBytes[i];
        }
        this.bucketStart[i] = entry.bucketStart;
        setLong(this.activeTime, i, entry.activeTime);
        setLong(this.rxBytes, i, entry.rxBytes);
        setLong(this.rxPackets, i, entry.rxPackets);
        setLong(this.txBytes, i, entry.txBytes);
        setLong(this.txPackets, i, entry.txPackets);
        setLong(this.operations, i, entry.operations);
        if (this.rxBytes != null) {
            this.totalBytes += this.rxBytes[i];
        }
        if (this.txBytes != null) {
            this.totalBytes += this.txBytes[i];
        }
    }

    @Deprecated
    public synchronized void recordData(long start, long end, long rxBytes, long txBytes) {
        recordData(start, end, new NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, rxBytes, 0L, txBytes, 0L, 0L));
    }

    public synchronized void recordData(long start, long end, NetworkStats.Entry entry) {
        int i;
        long txPackets;
        long j = start;
        long j2 = end;
        long rxBytes = entry.rxBytes;
        long rxPackets = entry.rxPackets;
        long txBytes = entry.txBytes;
        long txPackets2 = entry.txPackets;
        long fracTxBytes = entry.operations;
        if (entry.isNegative()) {
            throw new IllegalArgumentException("tried recording negative data");
        }
        if (entry.isEmpty()) {
            return;
        }
        ensureBuckets(start, end);
        long duration = j2 - j;
        int startIndex = getIndexAfter(j2);
        long rxBytes2 = rxBytes;
        int i2 = startIndex;
        while (i2 >= 0) {
            long operations = fracTxBytes;
            long operations2 = this.bucketStart[i2];
            int i3 = i2;
            long curEnd = this.bucketDuration + operations2;
            if (curEnd < j) {
                break;
            }
            if (operations2 <= j2) {
                long overlap = Math.min(curEnd, j2) - Math.max(operations2, j);
                if (overlap > 0) {
                    long fracRxBytes = (rxBytes2 * overlap) / duration;
                    long curEnd2 = (rxPackets * overlap) / duration;
                    long curStart = (txBytes * overlap) / duration;
                    long txPackets3 = txPackets2;
                    long txPackets4 = (txPackets2 * overlap) / duration;
                    long fracTxPackets = (operations * overlap) / duration;
                    i = i3;
                    addLong(this.activeTime, i, overlap);
                    addLong(this.rxBytes, i, fracRxBytes);
                    rxBytes2 -= fracRxBytes;
                    addLong(this.rxPackets, i, curEnd2);
                    rxPackets -= curEnd2;
                    addLong(this.txBytes, i, curStart);
                    txBytes -= curStart;
                    addLong(this.txPackets, i, txPackets4);
                    txPackets = txPackets3 - txPackets4;
                    addLong(this.operations, i, fracTxPackets);
                    duration -= overlap;
                    fracTxBytes = operations - fracTxPackets;
                    i2 = i - 1;
                    txPackets2 = txPackets;
                    j = start;
                    j2 = end;
                }
            }
            fracTxBytes = operations;
            long j3 = txPackets2;
            i = i3;
            txPackets = j3;
            i2 = i - 1;
            txPackets2 = txPackets;
            j = start;
            j2 = end;
        }
        this.totalBytes += entry.rxBytes + entry.txBytes;
    }

    private protected void recordEntireHistory(NetworkStatsHistory input) {
        recordHistory(input, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public synchronized void recordHistory(NetworkStatsHistory input, long start, long end) {
        NetworkStats.Entry entry = new NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, 0L, 0L, 0L, 0L, 0L);
        for (int i = 0; i < input.bucketCount; i++) {
            long bucketStart = input.bucketStart[i];
            long bucketEnd = input.bucketDuration + bucketStart;
            if (bucketStart >= start && bucketEnd <= end) {
                entry.rxBytes = getLong(input.rxBytes, i, 0L);
                entry.rxPackets = getLong(input.rxPackets, i, 0L);
                entry.txBytes = getLong(input.txBytes, i, 0L);
                entry.txPackets = getLong(input.txPackets, i, 0L);
                entry.operations = getLong(input.operations, i, 0L);
                recordData(bucketStart, bucketEnd, entry);
            }
        }
    }

    private synchronized void ensureBuckets(long start, long end) {
        long start2 = start - (start % this.bucketDuration);
        long end2 = end + ((this.bucketDuration - (end % this.bucketDuration)) % this.bucketDuration);
        long now = start2;
        while (now < end2) {
            int index = Arrays.binarySearch(this.bucketStart, 0, this.bucketCount, now);
            if (index < 0) {
                insertBucket(~index, now);
            }
            now += this.bucketDuration;
        }
    }

    private synchronized void insertBucket(int index, long start) {
        if (this.bucketCount >= this.bucketStart.length) {
            int newLength = (Math.max(this.bucketStart.length, 10) * 3) / 2;
            this.bucketStart = Arrays.copyOf(this.bucketStart, newLength);
            if (this.activeTime != null) {
                this.activeTime = Arrays.copyOf(this.activeTime, newLength);
            }
            if (this.rxBytes != null) {
                this.rxBytes = Arrays.copyOf(this.rxBytes, newLength);
            }
            if (this.rxPackets != null) {
                this.rxPackets = Arrays.copyOf(this.rxPackets, newLength);
            }
            if (this.txBytes != null) {
                this.txBytes = Arrays.copyOf(this.txBytes, newLength);
            }
            if (this.txPackets != null) {
                this.txPackets = Arrays.copyOf(this.txPackets, newLength);
            }
            if (this.operations != null) {
                this.operations = Arrays.copyOf(this.operations, newLength);
            }
        }
        if (index < this.bucketCount) {
            int dstPos = index + 1;
            int length = this.bucketCount - index;
            System.arraycopy(this.bucketStart, index, this.bucketStart, dstPos, length);
            if (this.activeTime != null) {
                System.arraycopy(this.activeTime, index, this.activeTime, dstPos, length);
            }
            if (this.rxBytes != null) {
                System.arraycopy(this.rxBytes, index, this.rxBytes, dstPos, length);
            }
            if (this.rxPackets != null) {
                System.arraycopy(this.rxPackets, index, this.rxPackets, dstPos, length);
            }
            if (this.txBytes != null) {
                System.arraycopy(this.txBytes, index, this.txBytes, dstPos, length);
            }
            if (this.txPackets != null) {
                System.arraycopy(this.txPackets, index, this.txPackets, dstPos, length);
            }
            if (this.operations != null) {
                System.arraycopy(this.operations, index, this.operations, dstPos, length);
            }
        }
        this.bucketStart[index] = start;
        setLong(this.activeTime, index, 0L);
        setLong(this.rxBytes, index, 0L);
        setLong(this.rxPackets, index, 0L);
        setLong(this.txBytes, index, 0L);
        setLong(this.txPackets, index, 0L);
        setLong(this.operations, index, 0L);
        this.bucketCount++;
    }

    public synchronized void clear() {
        this.bucketStart = EmptyArray.LONG;
        if (this.activeTime != null) {
            this.activeTime = EmptyArray.LONG;
        }
        if (this.rxBytes != null) {
            this.rxBytes = EmptyArray.LONG;
        }
        if (this.rxPackets != null) {
            this.rxPackets = EmptyArray.LONG;
        }
        if (this.txBytes != null) {
            this.txBytes = EmptyArray.LONG;
        }
        if (this.txPackets != null) {
            this.txPackets = EmptyArray.LONG;
        }
        if (this.operations != null) {
            this.operations = EmptyArray.LONG;
        }
        this.bucketCount = 0;
        this.totalBytes = 0L;
    }

    @Deprecated
    public synchronized void removeBucketsBefore(long cutoff) {
        int i = 0;
        while (i < this.bucketCount) {
            long curStart = this.bucketStart[i];
            long curEnd = this.bucketDuration + curStart;
            if (curEnd > cutoff) {
                break;
            }
            i++;
        }
        if (i > 0) {
            int length = this.bucketStart.length;
            this.bucketStart = Arrays.copyOfRange(this.bucketStart, i, length);
            if (this.activeTime != null) {
                this.activeTime = Arrays.copyOfRange(this.activeTime, i, length);
            }
            if (this.rxBytes != null) {
                this.rxBytes = Arrays.copyOfRange(this.rxBytes, i, length);
            }
            if (this.rxPackets != null) {
                this.rxPackets = Arrays.copyOfRange(this.rxPackets, i, length);
            }
            if (this.txBytes != null) {
                this.txBytes = Arrays.copyOfRange(this.txBytes, i, length);
            }
            if (this.txPackets != null) {
                this.txPackets = Arrays.copyOfRange(this.txPackets, i, length);
            }
            if (this.operations != null) {
                this.operations = Arrays.copyOfRange(this.operations, i, length);
            }
            this.bucketCount -= i;
        }
    }

    private protected Entry getValues(long start, long end, Entry recycle) {
        return getValues(start, end, Long.MAX_VALUE, recycle);
    }

    private protected Entry getValues(long start, long end, long now, Entry recycle) {
        long overlapEnd;
        long j = start;
        long j2 = end;
        Entry entry = recycle != null ? recycle : new Entry();
        entry.bucketDuration = j2 - j;
        entry.bucketStart = j;
        entry.activeTime = this.activeTime != null ? 0L : -1L;
        entry.rxBytes = this.rxBytes != null ? 0L : -1L;
        entry.rxPackets = this.rxPackets != null ? 0L : -1L;
        entry.txBytes = this.txBytes != null ? 0L : -1L;
        entry.txPackets = this.txPackets != null ? 0L : -1L;
        entry.operations = this.operations != null ? 0L : -1L;
        int startIndex = getIndexAfter(j2);
        int i = startIndex;
        while (i >= 0) {
            long curStart = this.bucketStart[i];
            long curEnd = curStart + this.bucketDuration;
            if (curEnd <= j) {
                break;
            }
            if (curStart < j2) {
                boolean activeBucket = curStart < now && curEnd > now;
                if (activeBucket) {
                    overlapEnd = this.bucketDuration;
                } else {
                    long overlapEnd2 = curEnd < j2 ? curEnd : j2;
                    long overlapStart = curStart > j ? curStart : j;
                    overlapEnd = overlapEnd2 - overlapStart;
                }
                if (overlapEnd > 0) {
                    if (this.activeTime != null) {
                        entry.activeTime += (this.activeTime[i] * overlapEnd) / this.bucketDuration;
                    }
                    if (this.rxBytes != null) {
                        entry.rxBytes += (this.rxBytes[i] * overlapEnd) / this.bucketDuration;
                    }
                    if (this.rxPackets != null) {
                        entry.rxPackets += (this.rxPackets[i] * overlapEnd) / this.bucketDuration;
                    }
                    if (this.txBytes != null) {
                        entry.txBytes += (this.txBytes[i] * overlapEnd) / this.bucketDuration;
                    }
                    if (this.txPackets != null) {
                        entry.txPackets += (this.txPackets[i] * overlapEnd) / this.bucketDuration;
                    }
                    if (this.operations != null) {
                        entry.operations += (this.operations[i] * overlapEnd) / this.bucketDuration;
                    }
                }
            }
            i--;
            j = start;
            j2 = end;
        }
        return entry;
    }

    @Deprecated
    public synchronized void generateRandom(long start, long end, long bytes) {
        Random r = new Random();
        float fractionRx = r.nextFloat();
        long rxBytes = ((float) bytes) * fractionRx;
        long txBytes = ((float) bytes) * (1.0f - fractionRx);
        long rxPackets = rxBytes / 1024;
        long txPackets = txBytes / 1024;
        long operations = rxBytes / 2048;
        generateRandom(start, end, rxBytes, rxPackets, txBytes, txPackets, operations, r);
    }

    @Deprecated
    public synchronized void generateRandom(long start, long end, long rxBytes, long rxPackets, long txBytes, long txPackets, long operations, Random r) {
        long j = end;
        ensureBuckets(start, end);
        NetworkStats.Entry entry = new NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, 0L, 0L, 0L, 0L, 0L);
        long rxBytes2 = rxBytes;
        long rxPackets2 = rxPackets;
        long txBytes2 = txBytes;
        long txPackets2 = txPackets;
        long operations2 = operations;
        while (true) {
            NetworkStats.Entry entry2 = entry;
            if (rxBytes2 > 1024 || rxPackets2 > 128 || txBytes2 > 1024 || txPackets2 > 128 || operations2 > 32) {
                long curStart = randomLong(r, start, j);
                long curEnd = curStart + randomLong(r, 0L, (j - curStart) / 2);
                long curEnd2 = randomLong(r, 0L, rxBytes2);
                entry2.rxBytes = curEnd2;
                entry2.rxPackets = randomLong(r, 0L, rxPackets2);
                entry2.txBytes = randomLong(r, 0L, txBytes2);
                entry2.txPackets = randomLong(r, 0L, txPackets2);
                entry2.operations = randomLong(r, 0L, operations2);
                rxBytes2 -= entry2.rxBytes;
                rxPackets2 -= entry2.rxPackets;
                txBytes2 -= entry2.txBytes;
                txPackets2 -= entry2.txPackets;
                operations2 -= entry2.operations;
                recordData(curStart, curEnd, entry2);
                entry = entry2;
                j = end;
            } else {
                return;
            }
        }
    }

    public static synchronized long randomLong(Random r, long start, long end) {
        return ((float) start) + (r.nextFloat() * ((float) (end - start)));
    }

    public synchronized boolean intersects(long start, long end) {
        long dataStart = getStart();
        long dataEnd = getEnd();
        if (start < dataStart || start > dataEnd) {
            if (end < dataStart || end > dataEnd) {
                if (dataStart < start || dataStart > end) {
                    return dataEnd >= start && dataEnd <= end;
                }
                return true;
            }
            return true;
        }
        return true;
    }

    public synchronized void dump(IndentingPrintWriter pw, boolean fullHistory) {
        pw.print("NetworkStatsHistory: bucketDuration=");
        pw.println(this.bucketDuration / 1000);
        pw.increaseIndent();
        int start = fullHistory ? 0 : Math.max(0, this.bucketCount - 32);
        if (start > 0) {
            pw.print("(omitting ");
            pw.print(start);
            pw.println(" buckets)");
        }
        for (int i = start; i < this.bucketCount; i++) {
            pw.print("st=");
            pw.print(this.bucketStart[i] / 1000);
            if (this.rxBytes != null) {
                pw.print(" rb=");
                pw.print(this.rxBytes[i]);
            }
            if (this.rxPackets != null) {
                pw.print(" rp=");
                pw.print(this.rxPackets[i]);
            }
            if (this.txBytes != null) {
                pw.print(" tb=");
                pw.print(this.txBytes[i]);
            }
            if (this.txPackets != null) {
                pw.print(" tp=");
                pw.print(this.txPackets[i]);
            }
            if (this.operations != null) {
                pw.print(" op=");
                pw.print(this.operations[i]);
            }
            pw.println();
        }
        pw.decreaseIndent();
    }

    public synchronized void dumpCheckin(PrintWriter pw) {
        pw.print("d,");
        pw.print(this.bucketDuration / 1000);
        pw.println();
        for (int i = 0; i < this.bucketCount; i++) {
            pw.print("b,");
            pw.print(this.bucketStart[i] / 1000);
            pw.print(',');
            if (this.rxBytes != null) {
                pw.print(this.rxBytes[i]);
            } else {
                pw.print(PhoneConstants.APN_TYPE_ALL);
            }
            pw.print(',');
            if (this.rxPackets != null) {
                pw.print(this.rxPackets[i]);
            } else {
                pw.print(PhoneConstants.APN_TYPE_ALL);
            }
            pw.print(',');
            if (this.txBytes != null) {
                pw.print(this.txBytes[i]);
            } else {
                pw.print(PhoneConstants.APN_TYPE_ALL);
            }
            pw.print(',');
            if (this.txPackets != null) {
                pw.print(this.txPackets[i]);
            } else {
                pw.print(PhoneConstants.APN_TYPE_ALL);
            }
            pw.print(',');
            if (this.operations != null) {
                pw.print(this.operations[i]);
            } else {
                pw.print(PhoneConstants.APN_TYPE_ALL);
            }
            pw.println();
        }
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long tag) {
        long start = proto.start(tag);
        proto.write(1112396529665L, this.bucketDuration);
        for (int i = 0; i < this.bucketCount; i++) {
            long startBucket = proto.start(2246267895810L);
            proto.write(1112396529665L, this.bucketStart[i]);
            writeToProto(proto, 1112396529666L, this.rxBytes, i);
            writeToProto(proto, 1112396529667L, this.rxPackets, i);
            writeToProto(proto, 1112396529668L, this.txBytes, i);
            writeToProto(proto, 1112396529669L, this.txPackets, i);
            writeToProto(proto, 1112396529670L, this.operations, i);
            proto.end(startBucket);
        }
        proto.end(start);
    }

    private static synchronized void writeToProto(ProtoOutputStream proto, long tag, long[] array, int index) {
        if (array != null) {
            proto.write(tag, array[index]);
        }
    }

    public String toString() {
        CharArrayWriter writer = new CharArrayWriter();
        dump(new IndentingPrintWriter(writer, "  "), false);
        return writer.toString();
    }

    private static synchronized long getLong(long[] array, int i, long value) {
        return array != null ? array[i] : value;
    }

    private static synchronized void setLong(long[] array, int i, long value) {
        if (array != null) {
            array[i] = value;
        }
    }

    private static synchronized void addLong(long[] array, int i, long value) {
        if (array != null) {
            array[i] = array[i] + value;
        }
    }

    public synchronized int estimateResizeBuckets(long newBucketDuration) {
        return (int) ((size() * getBucketDuration()) / newBucketDuration);
    }

    /* loaded from: classes2.dex */
    public static class DataStreamUtils {
        @Deprecated
        public static synchronized long[] readFullLongArray(DataInputStream in) throws IOException {
            int size = in.readInt();
            if (size < 0) {
                throw new ProtocolException("negative array size");
            }
            long[] values = new long[size];
            for (int i = 0; i < values.length; i++) {
                values[i] = in.readLong();
            }
            return values;
        }

        public static synchronized long readVarLong(DataInputStream in) throws IOException {
            long result = 0;
            for (int shift = 0; shift < 64; shift += 7) {
                byte b = in.readByte();
                result |= (b & Byte.MAX_VALUE) << shift;
                if ((b & 128) == 0) {
                    return result;
                }
            }
            throw new ProtocolException("malformed long");
        }

        public static synchronized void writeVarLong(DataOutputStream out, long value) throws IOException {
            while (((-128) & value) != 0) {
                out.writeByte((((int) value) & 127) | 128);
                value >>>= 7;
            }
            out.writeByte((int) value);
        }

        public static synchronized long[] readVarLongArray(DataInputStream in) throws IOException {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            if (size < 0) {
                throw new ProtocolException("negative array size");
            }
            long[] values = new long[size];
            for (int i = 0; i < values.length; i++) {
                values[i] = readVarLong(in);
            }
            return values;
        }

        public static synchronized void writeVarLongArray(DataOutputStream out, long[] values, int size) throws IOException {
            if (values == null) {
                out.writeInt(-1);
            } else if (size > values.length) {
                throw new IllegalArgumentException("size larger than length");
            } else {
                out.writeInt(size);
                for (int i = 0; i < size; i++) {
                    writeVarLong(out, values[i]);
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class ParcelUtils {
        public static synchronized long[] readLongArray(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            long[] values = new long[size];
            for (int i = 0; i < values.length; i++) {
                values[i] = in.readLong();
            }
            return values;
        }

        public static synchronized void writeLongArray(Parcel out, long[] values, int size) {
            if (values == null) {
                out.writeInt(-1);
            } else if (size > values.length) {
                throw new IllegalArgumentException("size larger than length");
            } else {
                out.writeInt(size);
                for (int i = 0; i < size; i++) {
                    out.writeLong(values[i]);
                }
            }
        }
    }
}
