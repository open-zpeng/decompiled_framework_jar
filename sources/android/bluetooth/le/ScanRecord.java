package android.bluetooth.le;

import android.bluetooth.BluetoothUuid;
import android.os.ParcelUuid;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class ScanRecord {
    private static final int DATA_TYPE_FLAGS = 1;
    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 9;
    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 8;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 255;
    private static final int DATA_TYPE_SERVICE_DATA_128_BIT = 33;
    private static final int DATA_TYPE_SERVICE_DATA_16_BIT = 22;
    private static final int DATA_TYPE_SERVICE_DATA_32_BIT = 32;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 7;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 6;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 3;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 2;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 5;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 4;
    private static final int DATA_TYPE_TX_POWER_LEVEL = 10;
    private static final String TAG = "ScanRecord";
    private final int mAdvertiseFlags;
    private final byte[] mBytes;
    private final String mDeviceName;
    private final SparseArray<byte[]> mManufacturerSpecificData;
    private final Map<ParcelUuid, byte[]> mServiceData;
    private final List<ParcelUuid> mServiceUuids;
    private final int mTxPowerLevel;

    public int getAdvertiseFlags() {
        return this.mAdvertiseFlags;
    }

    public List<ParcelUuid> getServiceUuids() {
        return this.mServiceUuids;
    }

    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.mManufacturerSpecificData;
    }

    public byte[] getManufacturerSpecificData(int manufacturerId) {
        return this.mManufacturerSpecificData.get(manufacturerId);
    }

    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.mServiceData;
    }

    public byte[] getServiceData(ParcelUuid serviceDataUuid) {
        if (serviceDataUuid == null) {
            return null;
        }
        return this.mServiceData.get(serviceDataUuid);
    }

    public int getTxPowerLevel() {
        return this.mTxPowerLevel;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public byte[] getBytes() {
        return this.mBytes;
    }

    private synchronized ScanRecord(List<ParcelUuid> serviceUuids, SparseArray<byte[]> manufacturerData, Map<ParcelUuid, byte[]> serviceData, int advertiseFlags, int txPowerLevel, String localName, byte[] bytes) {
        this.mServiceUuids = serviceUuids;
        this.mManufacturerSpecificData = manufacturerData;
        this.mServiceData = serviceData;
        this.mDeviceName = localName;
        this.mAdvertiseFlags = advertiseFlags;
        this.mTxPowerLevel = txPowerLevel;
        this.mBytes = bytes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ScanRecord parseFromBytes(byte[] scanRecord) {
        Map<ParcelUuid, byte[]> serviceData;
        if (scanRecord == null) {
            return null;
        }
        List<ParcelUuid> serviceUuids = new ArrayList<>();
        SparseArray<byte[]> manufacturerData = new SparseArray<>();
        Map<ParcelUuid, byte[]> serviceData2 = new ArrayMap<>();
        int advertiseFlag = -1;
        String localName = null;
        int txPowerLevel = -2147483648;
        int serviceUuidLength = 0;
        while (true) {
            serviceData = serviceData2;
            try {
                if (serviceUuidLength < scanRecord.length) {
                    int currentPos = serviceUuidLength + 1;
                    try {
                        int length = scanRecord[serviceUuidLength] & 255;
                        if (length != 0) {
                            int dataLength = length - 1;
                            int currentPos2 = currentPos + 1;
                            try {
                                int fieldType = scanRecord[currentPos] & 255;
                                if (fieldType != 22) {
                                    if (fieldType != 255) {
                                        switch (fieldType) {
                                            case 1:
                                                advertiseFlag = scanRecord[currentPos2] & 255;
                                                break;
                                            case 2:
                                            case 3:
                                                parseServiceUuid(scanRecord, currentPos2, dataLength, 2, serviceUuids);
                                                break;
                                            case 4:
                                            case 5:
                                                parseServiceUuid(scanRecord, currentPos2, dataLength, 4, serviceUuids);
                                                break;
                                            case 6:
                                            case 7:
                                                parseServiceUuid(scanRecord, currentPos2, dataLength, 16, serviceUuids);
                                                break;
                                            case 8:
                                            case 9:
                                                localName = new String(extractBytes(scanRecord, currentPos2, dataLength));
                                                break;
                                            case 10:
                                                txPowerLevel = scanRecord[currentPos2];
                                                break;
                                            default:
                                                switch (fieldType) {
                                                }
                                        }
                                    } else {
                                        int manufacturerId = ((scanRecord[currentPos2 + 1] & 255) << 8) + (255 & scanRecord[currentPos2]);
                                        byte[] manufacturerDataBytes = extractBytes(scanRecord, currentPos2 + 2, dataLength - 2);
                                        manufacturerData.put(manufacturerId, manufacturerDataBytes);
                                    }
                                    serviceUuidLength = currentPos2 + dataLength;
                                    serviceData2 = serviceData;
                                }
                                int serviceUuidLength2 = 2;
                                if (fieldType == 32) {
                                    serviceUuidLength2 = 4;
                                } else if (fieldType == 33) {
                                    serviceUuidLength2 = 16;
                                }
                                byte[] serviceDataUuidBytes = extractBytes(scanRecord, currentPos2, serviceUuidLength2);
                                ParcelUuid serviceDataUuid = BluetoothUuid.parseUuidFrom(serviceDataUuidBytes);
                                byte[] serviceDataArray = extractBytes(scanRecord, currentPos2 + serviceUuidLength2, dataLength - serviceUuidLength2);
                                serviceData.put(serviceDataUuid, serviceDataArray);
                                serviceUuidLength = currentPos2 + dataLength;
                                serviceData2 = serviceData;
                            } catch (Exception e) {
                                Log.e(TAG, "unable to parse scan record: " + Arrays.toString(scanRecord));
                                return new ScanRecord(null, null, null, -1, Integer.MIN_VALUE, null, scanRecord);
                            }
                        }
                    } catch (Exception e2) {
                    }
                }
            } catch (Exception e3) {
            }
        }
        try {
        } catch (Exception e4) {
            Log.e(TAG, "unable to parse scan record: " + Arrays.toString(scanRecord));
            return new ScanRecord(null, null, null, -1, Integer.MIN_VALUE, null, scanRecord);
        }
        try {
            return new ScanRecord(serviceUuids.isEmpty() ? null : serviceUuids, manufacturerData, serviceData, advertiseFlag, txPowerLevel, localName, scanRecord);
        } catch (Exception e5) {
            Log.e(TAG, "unable to parse scan record: " + Arrays.toString(scanRecord));
            return new ScanRecord(null, null, null, -1, Integer.MIN_VALUE, null, scanRecord);
        }
    }

    public String toString() {
        return "ScanRecord [mAdvertiseFlags=" + this.mAdvertiseFlags + ", mServiceUuids=" + this.mServiceUuids + ", mManufacturerSpecificData=" + BluetoothLeUtils.toString(this.mManufacturerSpecificData) + ", mServiceData=" + BluetoothLeUtils.toString(this.mServiceData) + ", mTxPowerLevel=" + this.mTxPowerLevel + ", mDeviceName=" + this.mDeviceName + "]";
    }

    private static synchronized int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength, int uuidLength, List<ParcelUuid> serviceUuids) {
        while (dataLength > 0) {
            byte[] uuidBytes = extractBytes(scanRecord, currentPos, uuidLength);
            serviceUuids.add(BluetoothUuid.parseUuidFrom(uuidBytes));
            dataLength -= uuidLength;
            currentPos += uuidLength;
        }
        return currentPos;
    }

    private static synchronized byte[] extractBytes(byte[] scanRecord, int start, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(scanRecord, start, bytes, 0, length);
        return bytes;
    }
}
