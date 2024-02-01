package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class ModemActivityInfo implements Parcelable {
    public static final Parcelable.Creator<ModemActivityInfo> CREATOR = new Parcelable.Creator<ModemActivityInfo>() { // from class: android.telephony.ModemActivityInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ModemActivityInfo createFromParcel(Parcel in) {
            long timestamp = in.readLong();
            int sleepTimeMs = in.readInt();
            int idleTimeMs = in.readInt();
            int[] txTimeMs = new int[5];
            for (int i = 0; i < 5; i++) {
                txTimeMs[i] = in.readInt();
            }
            int rxTimeMs = in.readInt();
            int energyUsed = in.readInt();
            return new ModemActivityInfo(timestamp, sleepTimeMs, idleTimeMs, txTimeMs, rxTimeMs, energyUsed);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ModemActivityInfo[] newArray(int size) {
            return new ModemActivityInfo[size];
        }
    };
    public static final int TX_POWER_LEVELS = 5;
    private int mEnergyUsed;
    private int mIdleTimeMs;
    private int mRxTimeMs;
    private int mSleepTimeMs;
    private long mTimestamp;
    private int[] mTxTimeMs = new int[5];

    public ModemActivityInfo(long timestamp, int sleepTimeMs, int idleTimeMs, int[] txTimeMs, int rxTimeMs, int energyUsed) {
        this.mTimestamp = timestamp;
        this.mSleepTimeMs = sleepTimeMs;
        this.mIdleTimeMs = idleTimeMs;
        if (txTimeMs != null) {
            System.arraycopy(txTimeMs, 0, this.mTxTimeMs, 0, Math.min(txTimeMs.length, 5));
        }
        this.mRxTimeMs = rxTimeMs;
        this.mEnergyUsed = energyUsed;
    }

    public String toString() {
        return "ModemActivityInfo{ mTimestamp=" + this.mTimestamp + " mSleepTimeMs=" + this.mSleepTimeMs + " mIdleTimeMs=" + this.mIdleTimeMs + " mTxTimeMs[]=" + Arrays.toString(this.mTxTimeMs) + " mRxTimeMs=" + this.mRxTimeMs + " mEnergyUsed=" + this.mEnergyUsed + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mTimestamp);
        dest.writeInt(this.mSleepTimeMs);
        dest.writeInt(this.mIdleTimeMs);
        for (int i = 0; i < 5; i++) {
            dest.writeInt(this.mTxTimeMs[i]);
        }
        int i2 = this.mRxTimeMs;
        dest.writeInt(i2);
        dest.writeInt(this.mEnergyUsed);
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
    }

    public int[] getTxTimeMillis() {
        return this.mTxTimeMs;
    }

    public void setTxTimeMillis(int[] txTimeMs) {
        this.mTxTimeMs = txTimeMs;
    }

    public int getSleepTimeMillis() {
        return this.mSleepTimeMs;
    }

    public void setSleepTimeMillis(int sleepTimeMillis) {
        this.mSleepTimeMs = sleepTimeMillis;
    }

    public int getIdleTimeMillis() {
        return this.mIdleTimeMs;
    }

    public void setIdleTimeMillis(int idleTimeMillis) {
        this.mIdleTimeMs = idleTimeMillis;
    }

    public int getRxTimeMillis() {
        return this.mRxTimeMs;
    }

    public void setRxTimeMillis(int rxTimeMillis) {
        this.mRxTimeMs = rxTimeMillis;
    }

    public int getEnergyUsed() {
        return this.mEnergyUsed;
    }

    public void setEnergyUsed(int energyUsed) {
        this.mEnergyUsed = energyUsed;
    }

    public boolean isValid() {
        int[] txTimeMillis;
        for (int txVal : getTxTimeMillis()) {
            if (txVal < 0) {
                return false;
            }
        }
        return getIdleTimeMillis() >= 0 && getSleepTimeMillis() >= 0 && getRxTimeMillis() >= 0 && getEnergyUsed() >= 0 && !isEmpty();
    }

    private boolean isEmpty() {
        int[] txTimeMillis;
        for (int txVal : getTxTimeMillis()) {
            if (txVal != 0) {
                return false;
            }
        }
        return getIdleTimeMillis() == 0 && getSleepTimeMillis() == 0 && getRxTimeMillis() == 0 && getEnergyUsed() == 0;
    }
}
