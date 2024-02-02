package android.os.connectivity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
/* loaded from: classes2.dex */
public final class GpsBatteryStats implements Parcelable {
    private protected static final Parcelable.Creator<GpsBatteryStats> CREATOR = new Parcelable.Creator<GpsBatteryStats>() { // from class: android.os.connectivity.GpsBatteryStats.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GpsBatteryStats createFromParcel(Parcel in) {
            return new GpsBatteryStats(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GpsBatteryStats[] newArray(int size) {
            return new GpsBatteryStats[size];
        }
    };
    public protected long mEnergyConsumedMaMs;
    public protected long mLoggingDurationMs;
    public protected long[] mTimeInGpsSignalQualityLevel;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized GpsBatteryStats() {
        initialize();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.mLoggingDurationMs);
        out.writeLong(this.mEnergyConsumedMaMs);
        out.writeLongArray(this.mTimeInGpsSignalQualityLevel);
    }

    private protected synchronized void readFromParcel(Parcel in) {
        this.mLoggingDurationMs = in.readLong();
        this.mEnergyConsumedMaMs = in.readLong();
        in.readLongArray(this.mTimeInGpsSignalQualityLevel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long getLoggingDurationMs() {
        return this.mLoggingDurationMs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long getEnergyConsumedMaMs() {
        return this.mEnergyConsumedMaMs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long[] getTimeInGpsSignalQualityLevel() {
        return this.mTimeInGpsSignalQualityLevel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setLoggingDurationMs(long t) {
        this.mLoggingDurationMs = t;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setEnergyConsumedMaMs(long e) {
        this.mEnergyConsumedMaMs = e;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setTimeInGpsSignalQualityLevel(long[] t) {
        this.mTimeInGpsSignalQualityLevel = Arrays.copyOfRange(t, 0, Math.min(t.length, 2));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public protected synchronized GpsBatteryStats(Parcel in) {
        initialize();
        readFromParcel(in);
    }

    public protected synchronized void initialize() {
        this.mLoggingDurationMs = 0L;
        this.mEnergyConsumedMaMs = 0L;
        this.mTimeInGpsSignalQualityLevel = new long[2];
    }
}
