package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/* loaded from: classes.dex */
public class BluetoothGattService implements Parcelable {
    public static final Parcelable.Creator<BluetoothGattService> CREATOR = new Parcelable.Creator<BluetoothGattService>() { // from class: android.bluetooth.BluetoothGattService.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothGattService createFromParcel(Parcel in) {
            return new BluetoothGattService(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BluetoothGattService[] newArray(int size) {
            return new BluetoothGattService[size];
        }
    };
    public static final int SERVICE_TYPE_PRIMARY = 0;
    public static final int SERVICE_TYPE_SECONDARY = 1;
    private boolean mAdvertisePreferred;
    protected List<BluetoothGattCharacteristic> mCharacteristics;
    @UnsupportedAppUsage
    protected BluetoothDevice mDevice;
    protected int mHandles;
    protected List<BluetoothGattService> mIncludedServices;
    protected int mInstanceId;
    protected int mServiceType;
    protected UUID mUuid;

    public BluetoothGattService(UUID uuid, int serviceType) {
        this.mHandles = 0;
        this.mDevice = null;
        this.mUuid = uuid;
        this.mInstanceId = 0;
        this.mServiceType = serviceType;
        this.mCharacteristics = new ArrayList();
        this.mIncludedServices = new ArrayList();
    }

    BluetoothGattService(BluetoothDevice device, UUID uuid, int instanceId, int serviceType) {
        this.mHandles = 0;
        this.mDevice = device;
        this.mUuid = uuid;
        this.mInstanceId = instanceId;
        this.mServiceType = serviceType;
        this.mCharacteristics = new ArrayList();
        this.mIncludedServices = new ArrayList();
    }

    public BluetoothGattService(UUID uuid, int instanceId, int serviceType) {
        this.mHandles = 0;
        this.mDevice = null;
        this.mUuid = uuid;
        this.mInstanceId = instanceId;
        this.mServiceType = serviceType;
        this.mCharacteristics = new ArrayList();
        this.mIncludedServices = new ArrayList();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(new ParcelUuid(this.mUuid), 0);
        out.writeInt(this.mInstanceId);
        out.writeInt(this.mServiceType);
        out.writeTypedList(this.mCharacteristics);
        ArrayList<BluetoothGattIncludedService> includedServices = new ArrayList<>(this.mIncludedServices.size());
        for (BluetoothGattService s : this.mIncludedServices) {
            includedServices.add(new BluetoothGattIncludedService(s.getUuid(), s.getInstanceId(), s.getType()));
        }
        out.writeTypedList(includedServices);
    }

    private BluetoothGattService(Parcel in) {
        this.mHandles = 0;
        this.mUuid = ((ParcelUuid) in.readParcelable(null)).getUuid();
        this.mInstanceId = in.readInt();
        this.mServiceType = in.readInt();
        this.mCharacteristics = new ArrayList();
        ArrayList<BluetoothGattCharacteristic> chrcs = in.createTypedArrayList(BluetoothGattCharacteristic.CREATOR);
        if (chrcs != null) {
            Iterator<BluetoothGattCharacteristic> it = chrcs.iterator();
            while (it.hasNext()) {
                BluetoothGattCharacteristic chrc = it.next();
                chrc.setService(this);
                this.mCharacteristics.add(chrc);
            }
        }
        this.mIncludedServices = new ArrayList();
        ArrayList<BluetoothGattIncludedService> inclSvcs = in.createTypedArrayList(BluetoothGattIncludedService.CREATOR);
        if (chrcs != null) {
            Iterator<BluetoothGattIncludedService> it2 = inclSvcs.iterator();
            while (it2.hasNext()) {
                BluetoothGattIncludedService isvc = it2.next();
                this.mIncludedServices.add(new BluetoothGattService(null, isvc.getUuid(), isvc.getInstanceId(), isvc.getType()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDevice(BluetoothDevice device) {
        this.mDevice = device;
    }

    public boolean addService(BluetoothGattService service) {
        this.mIncludedServices.add(service);
        return true;
    }

    public boolean addCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.mCharacteristics.add(characteristic);
        characteristic.setService(this);
        return true;
    }

    BluetoothGattCharacteristic getCharacteristic(UUID uuid, int instanceId) {
        for (BluetoothGattCharacteristic characteristic : this.mCharacteristics) {
            if (uuid.equals(characteristic.getUuid()) && characteristic.getInstanceId() == instanceId) {
                return characteristic;
            }
        }
        return null;
    }

    @UnsupportedAppUsage
    public void setInstanceId(int instanceId) {
        this.mInstanceId = instanceId;
    }

    int getHandles() {
        return this.mHandles;
    }

    public void setHandles(int handles) {
        this.mHandles = handles;
    }

    public void addIncludedService(BluetoothGattService includedService) {
        this.mIncludedServices.add(includedService);
    }

    public UUID getUuid() {
        return this.mUuid;
    }

    public int getInstanceId() {
        return this.mInstanceId;
    }

    public int getType() {
        return this.mServiceType;
    }

    public List<BluetoothGattService> getIncludedServices() {
        return this.mIncludedServices;
    }

    public List<BluetoothGattCharacteristic> getCharacteristics() {
        return this.mCharacteristics;
    }

    public BluetoothGattCharacteristic getCharacteristic(UUID uuid) {
        for (BluetoothGattCharacteristic characteristic : this.mCharacteristics) {
            if (uuid.equals(characteristic.getUuid())) {
                return characteristic;
            }
        }
        return null;
    }

    public boolean isAdvertisePreferred() {
        return this.mAdvertisePreferred;
    }

    @UnsupportedAppUsage
    public void setAdvertisePreferred(boolean advertisePreferred) {
        this.mAdvertisePreferred = advertisePreferred;
    }
}
