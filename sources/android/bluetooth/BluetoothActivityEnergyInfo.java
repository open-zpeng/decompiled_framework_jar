package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class BluetoothActivityEnergyInfo implements Parcelable {
    public static final int BT_STACK_STATE_INVALID = 0;
    public static final int BT_STACK_STATE_STATE_ACTIVE = 1;
    public static final int BT_STACK_STATE_STATE_IDLE = 3;
    public static final int BT_STACK_STATE_STATE_SCANNING = 2;
    public static final Parcelable.Creator<BluetoothActivityEnergyInfo> CREATOR = new Parcelable.Creator<BluetoothActivityEnergyInfo>() { // from class: android.bluetooth.BluetoothActivityEnergyInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothActivityEnergyInfo createFromParcel(Parcel in) {
            return new BluetoothActivityEnergyInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothActivityEnergyInfo[] newArray(int size) {
            return new BluetoothActivityEnergyInfo[size];
        }
    };
    private int mBluetoothStackState;
    private long mControllerEnergyUsed;
    private long mControllerIdleTimeMs;
    private long mControllerRxTimeMs;
    private long mControllerTxTimeMs;
    private final long mTimestamp;
    private UidTraffic[] mUidTraffic;

    public synchronized BluetoothActivityEnergyInfo(long timestamp, int stackState, long txTime, long rxTime, long idleTime, long energyUsed) {
        this.mTimestamp = timestamp;
        this.mBluetoothStackState = stackState;
        this.mControllerTxTimeMs = txTime;
        this.mControllerRxTimeMs = rxTime;
        this.mControllerIdleTimeMs = idleTime;
        this.mControllerEnergyUsed = energyUsed;
    }

    synchronized BluetoothActivityEnergyInfo(Parcel in) {
        this.mTimestamp = in.readLong();
        this.mBluetoothStackState = in.readInt();
        this.mControllerTxTimeMs = in.readLong();
        this.mControllerRxTimeMs = in.readLong();
        this.mControllerIdleTimeMs = in.readLong();
        this.mControllerEnergyUsed = in.readLong();
        this.mUidTraffic = (UidTraffic[]) in.createTypedArray(UidTraffic.CREATOR);
    }

    public String toString() {
        return "BluetoothActivityEnergyInfo{ mTimestamp=" + this.mTimestamp + " mBluetoothStackState=" + this.mBluetoothStackState + " mControllerTxTimeMs=" + this.mControllerTxTimeMs + " mControllerRxTimeMs=" + this.mControllerRxTimeMs + " mControllerIdleTimeMs=" + this.mControllerIdleTimeMs + " mControllerEnergyUsed=" + this.mControllerEnergyUsed + " mUidTraffic=" + Arrays.toString(this.mUidTraffic) + " }";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.mTimestamp);
        out.writeInt(this.mBluetoothStackState);
        out.writeLong(this.mControllerTxTimeMs);
        out.writeLong(this.mControllerRxTimeMs);
        out.writeLong(this.mControllerIdleTimeMs);
        out.writeLong(this.mControllerEnergyUsed);
        out.writeTypedArray(this.mUidTraffic, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public synchronized int getBluetoothStackState() {
        return this.mBluetoothStackState;
    }

    public synchronized long getControllerTxTimeMillis() {
        return this.mControllerTxTimeMs;
    }

    public synchronized long getControllerRxTimeMillis() {
        return this.mControllerRxTimeMs;
    }

    public synchronized long getControllerIdleTimeMillis() {
        return this.mControllerIdleTimeMs;
    }

    public synchronized long getControllerEnergyUsed() {
        return this.mControllerEnergyUsed;
    }

    public synchronized long getTimeStamp() {
        return this.mTimestamp;
    }

    public synchronized UidTraffic[] getUidTraffic() {
        return this.mUidTraffic;
    }

    public synchronized void setUidTraffic(UidTraffic[] traffic) {
        this.mUidTraffic = traffic;
    }

    public synchronized boolean isValid() {
        return this.mControllerTxTimeMs >= 0 && this.mControllerRxTimeMs >= 0 && this.mControllerIdleTimeMs >= 0;
    }
}
