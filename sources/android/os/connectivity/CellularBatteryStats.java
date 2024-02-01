package android.os.connectivity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
/* loaded from: classes2.dex */
public final class CellularBatteryStats implements Parcelable {
    private protected static final Parcelable.Creator<CellularBatteryStats> CREATOR = new Parcelable.Creator<CellularBatteryStats>() { // from class: android.os.connectivity.CellularBatteryStats.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellularBatteryStats createFromParcel(Parcel in) {
            return new CellularBatteryStats(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellularBatteryStats[] newArray(int size) {
            return new CellularBatteryStats[size];
        }
    };
    public protected long mEnergyConsumedMaMs;
    public protected long mIdleTimeMs;
    public protected long mKernelActiveTimeMs;
    public protected long mLoggingDurationMs;
    public protected long mNumBytesRx;
    public protected long mNumBytesTx;
    public protected long mNumPacketsRx;
    public protected long mNumPacketsTx;
    public protected long mRxTimeMs;
    public protected long mSleepTimeMs;
    public protected long[] mTimeInRatMs;
    public protected long[] mTimeInRxSignalStrengthLevelMs;
    public protected long[] mTxTimeMs;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized CellularBatteryStats() {
        initialize();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.mLoggingDurationMs);
        out.writeLong(this.mKernelActiveTimeMs);
        out.writeLong(this.mNumPacketsTx);
        out.writeLong(this.mNumBytesTx);
        out.writeLong(this.mNumPacketsRx);
        out.writeLong(this.mNumBytesRx);
        out.writeLong(this.mSleepTimeMs);
        out.writeLong(this.mIdleTimeMs);
        out.writeLong(this.mRxTimeMs);
        out.writeLong(this.mEnergyConsumedMaMs);
        out.writeLongArray(this.mTimeInRatMs);
        out.writeLongArray(this.mTimeInRxSignalStrengthLevelMs);
        out.writeLongArray(this.mTxTimeMs);
    }

    private protected synchronized void readFromParcel(Parcel in) {
        this.mLoggingDurationMs = in.readLong();
        this.mKernelActiveTimeMs = in.readLong();
        this.mNumPacketsTx = in.readLong();
        this.mNumBytesTx = in.readLong();
        this.mNumPacketsRx = in.readLong();
        this.mNumBytesRx = in.readLong();
        this.mSleepTimeMs = in.readLong();
        this.mIdleTimeMs = in.readLong();
        this.mRxTimeMs = in.readLong();
        this.mEnergyConsumedMaMs = in.readLong();
        in.readLongArray(this.mTimeInRatMs);
        in.readLongArray(this.mTimeInRxSignalStrengthLevelMs);
        in.readLongArray(this.mTxTimeMs);
    }

    private protected synchronized long getLoggingDurationMs() {
        return this.mLoggingDurationMs;
    }

    private protected synchronized long getKernelActiveTimeMs() {
        return this.mKernelActiveTimeMs;
    }

    private protected synchronized long getNumPacketsTx() {
        return this.mNumPacketsTx;
    }

    private protected synchronized long getNumBytesTx() {
        return this.mNumBytesTx;
    }

    private protected synchronized long getNumPacketsRx() {
        return this.mNumPacketsRx;
    }

    private protected synchronized long getNumBytesRx() {
        return this.mNumBytesRx;
    }

    private protected synchronized long getSleepTimeMs() {
        return this.mSleepTimeMs;
    }

    private protected synchronized long getIdleTimeMs() {
        return this.mIdleTimeMs;
    }

    private protected synchronized long getRxTimeMs() {
        return this.mRxTimeMs;
    }

    private protected synchronized long getEnergyConsumedMaMs() {
        return this.mEnergyConsumedMaMs;
    }

    private protected synchronized long[] getTimeInRatMs() {
        return this.mTimeInRatMs;
    }

    private protected synchronized long[] getTimeInRxSignalStrengthLevelMs() {
        return this.mTimeInRxSignalStrengthLevelMs;
    }

    private protected synchronized long[] getTxTimeMs() {
        return this.mTxTimeMs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setLoggingDurationMs(long t) {
        this.mLoggingDurationMs = t;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setKernelActiveTimeMs(long t) {
        this.mKernelActiveTimeMs = t;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setNumPacketsTx(long n) {
        this.mNumPacketsTx = n;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setNumBytesTx(long b) {
        this.mNumBytesTx = b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setNumPacketsRx(long n) {
        this.mNumPacketsRx = n;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setNumBytesRx(long b) {
        this.mNumBytesRx = b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setSleepTimeMs(long t) {
        this.mSleepTimeMs = t;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setIdleTimeMs(long t) {
        this.mIdleTimeMs = t;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setRxTimeMs(long t) {
        this.mRxTimeMs = t;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setEnergyConsumedMaMs(long e) {
        this.mEnergyConsumedMaMs = e;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setTimeInRatMs(long[] t) {
        this.mTimeInRatMs = Arrays.copyOfRange(t, 0, Math.min(t.length, 21));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setTimeInRxSignalStrengthLevelMs(long[] t) {
        this.mTimeInRxSignalStrengthLevelMs = Arrays.copyOfRange(t, 0, Math.min(t.length, 5));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setTxTimeMs(long[] t) {
        this.mTxTimeMs = Arrays.copyOfRange(t, 0, Math.min(t.length, 5));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public protected synchronized CellularBatteryStats(Parcel in) {
        initialize();
        readFromParcel(in);
    }

    public protected synchronized void initialize() {
        this.mLoggingDurationMs = 0L;
        this.mKernelActiveTimeMs = 0L;
        this.mNumPacketsTx = 0L;
        this.mNumBytesTx = 0L;
        this.mNumPacketsRx = 0L;
        this.mNumBytesRx = 0L;
        this.mSleepTimeMs = 0L;
        this.mIdleTimeMs = 0L;
        this.mRxTimeMs = 0L;
        this.mEnergyConsumedMaMs = 0L;
        this.mTimeInRatMs = new long[21];
        Arrays.fill(this.mTimeInRatMs, 0L);
        this.mTimeInRxSignalStrengthLevelMs = new long[5];
        Arrays.fill(this.mTimeInRxSignalStrengthLevelMs, 0L);
        this.mTxTimeMs = new long[5];
        Arrays.fill(this.mTxTimeMs, 0L);
    }
}
