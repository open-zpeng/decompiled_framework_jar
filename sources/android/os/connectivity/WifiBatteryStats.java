package android.os.connectivity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
/* loaded from: classes2.dex */
public final class WifiBatteryStats implements Parcelable {
    private protected static final Parcelable.Creator<WifiBatteryStats> CREATOR = new Parcelable.Creator<WifiBatteryStats>() { // from class: android.os.connectivity.WifiBatteryStats.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiBatteryStats createFromParcel(Parcel in) {
            return new WifiBatteryStats(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiBatteryStats[] newArray(int size) {
            return new WifiBatteryStats[size];
        }
    };
    public protected long mEnergyConsumedMaMs;
    public protected long mIdleTimeMs;
    public protected long mKernelActiveTimeMs;
    public protected long mLoggingDurationMs;
    public protected long mNumAppScanRequest;
    public protected long mNumBytesRx;
    public protected long mNumBytesTx;
    public protected long mNumPacketsRx;
    public protected long mNumPacketsTx;
    public protected long mRxTimeMs;
    public protected long mScanTimeMs;
    public protected long mSleepTimeMs;
    public protected long[] mTimeInRxSignalStrengthLevelMs;
    public protected long[] mTimeInStateMs;
    public protected long[] mTimeInSupplicantStateMs;
    public protected long mTxTimeMs;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized WifiBatteryStats() {
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
        out.writeLong(this.mScanTimeMs);
        out.writeLong(this.mIdleTimeMs);
        out.writeLong(this.mRxTimeMs);
        out.writeLong(this.mTxTimeMs);
        out.writeLong(this.mEnergyConsumedMaMs);
        out.writeLong(this.mNumAppScanRequest);
        out.writeLongArray(this.mTimeInStateMs);
        out.writeLongArray(this.mTimeInRxSignalStrengthLevelMs);
        out.writeLongArray(this.mTimeInSupplicantStateMs);
    }

    private protected synchronized void readFromParcel(Parcel in) {
        this.mLoggingDurationMs = in.readLong();
        this.mKernelActiveTimeMs = in.readLong();
        this.mNumPacketsTx = in.readLong();
        this.mNumBytesTx = in.readLong();
        this.mNumPacketsRx = in.readLong();
        this.mNumBytesRx = in.readLong();
        this.mSleepTimeMs = in.readLong();
        this.mScanTimeMs = in.readLong();
        this.mIdleTimeMs = in.readLong();
        this.mRxTimeMs = in.readLong();
        this.mTxTimeMs = in.readLong();
        this.mEnergyConsumedMaMs = in.readLong();
        this.mNumAppScanRequest = in.readLong();
        in.readLongArray(this.mTimeInStateMs);
        in.readLongArray(this.mTimeInRxSignalStrengthLevelMs);
        in.readLongArray(this.mTimeInSupplicantStateMs);
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

    private protected synchronized long getScanTimeMs() {
        return this.mScanTimeMs;
    }

    private protected synchronized long getIdleTimeMs() {
        return this.mIdleTimeMs;
    }

    private protected synchronized long getRxTimeMs() {
        return this.mRxTimeMs;
    }

    private protected synchronized long getTxTimeMs() {
        return this.mTxTimeMs;
    }

    private protected synchronized long getEnergyConsumedMaMs() {
        return this.mEnergyConsumedMaMs;
    }

    private protected synchronized long getNumAppScanRequest() {
        return this.mNumAppScanRequest;
    }

    private protected synchronized long[] getTimeInStateMs() {
        return this.mTimeInStateMs;
    }

    private protected synchronized long[] getTimeInRxSignalStrengthLevelMs() {
        return this.mTimeInRxSignalStrengthLevelMs;
    }

    private protected synchronized long[] getTimeInSupplicantStateMs() {
        return this.mTimeInSupplicantStateMs;
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
    public synchronized void setScanTimeMs(long t) {
        this.mScanTimeMs = t;
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
    public synchronized void setTxTimeMs(long t) {
        this.mTxTimeMs = t;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setEnergyConsumedMaMs(long e) {
        this.mEnergyConsumedMaMs = e;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setNumAppScanRequest(long n) {
        this.mNumAppScanRequest = n;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setTimeInStateMs(long[] t) {
        this.mTimeInStateMs = Arrays.copyOfRange(t, 0, Math.min(t.length, 8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setTimeInRxSignalStrengthLevelMs(long[] t) {
        this.mTimeInRxSignalStrengthLevelMs = Arrays.copyOfRange(t, 0, Math.min(t.length, 5));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setTimeInSupplicantStateMs(long[] t) {
        this.mTimeInSupplicantStateMs = Arrays.copyOfRange(t, 0, Math.min(t.length, 13));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public protected synchronized WifiBatteryStats(Parcel in) {
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
        this.mScanTimeMs = 0L;
        this.mIdleTimeMs = 0L;
        this.mRxTimeMs = 0L;
        this.mTxTimeMs = 0L;
        this.mEnergyConsumedMaMs = 0L;
        this.mNumAppScanRequest = 0L;
        this.mTimeInStateMs = new long[8];
        Arrays.fill(this.mTimeInStateMs, 0L);
        this.mTimeInRxSignalStrengthLevelMs = new long[5];
        Arrays.fill(this.mTimeInRxSignalStrengthLevelMs, 0L);
        this.mTimeInSupplicantStateMs = new long[13];
        Arrays.fill(this.mTimeInSupplicantStateMs, 0L);
    }
}
