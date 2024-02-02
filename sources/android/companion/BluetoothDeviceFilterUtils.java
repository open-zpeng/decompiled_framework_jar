package android.companion;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanFilter;
import android.net.wifi.ScanResult;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class BluetoothDeviceFilterUtils {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "BluetoothDeviceFilterUtils";

    private synchronized BluetoothDeviceFilterUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized String patternToString(Pattern p) {
        if (p == null) {
            return null;
        }
        return p.pattern();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized Pattern patternFromString(String s) {
        if (s == null) {
            return null;
        }
        return Pattern.compile(s);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized boolean matches(ScanFilter filter, BluetoothDevice device) {
        return matchesAddress(filter.getDeviceAddress(), device) && matchesServiceUuid(filter.getServiceUuid(), filter.getServiceUuidMask(), device);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized boolean matchesAddress(String deviceAddress, BluetoothDevice device) {
        return deviceAddress == null || (device != null && deviceAddress.equals(device.getAddress()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized boolean matchesServiceUuids(List<ParcelUuid> serviceUuids, List<ParcelUuid> serviceUuidMasks, BluetoothDevice device) {
        for (int i = 0; i < serviceUuids.size(); i++) {
            ParcelUuid uuid = serviceUuids.get(i);
            ParcelUuid uuidMask = serviceUuidMasks.get(i);
            if (!matchesServiceUuid(uuid, uuidMask, device)) {
                return false;
            }
        }
        return true;
    }

    static synchronized boolean matchesServiceUuid(ParcelUuid serviceUuid, ParcelUuid serviceUuidMask, BluetoothDevice device) {
        return serviceUuid == null || ScanFilter.matchesServiceUuids(serviceUuid, serviceUuidMask, Arrays.asList(device.getUuids()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized boolean matchesName(Pattern namePattern, BluetoothDevice device) {
        if (namePattern == null) {
            return true;
        }
        if (device == null) {
            return false;
        }
        String name = device.getName();
        boolean result = name != null && namePattern.matcher(name).find();
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized boolean matchesName(Pattern namePattern, ScanResult device) {
        if (namePattern == null) {
            return true;
        }
        if (device == null) {
            return false;
        }
        String name = device.SSID;
        boolean result = name != null && namePattern.matcher(name).find();
        return result;
    }

    private static synchronized void debugLogMatchResult(boolean result, BluetoothDevice device, Object criteria) {
        StringBuilder sb = new StringBuilder();
        sb.append(getDeviceDisplayNameInternal(device));
        sb.append(result ? " ~ " : " !~ ");
        sb.append(criteria);
        Log.i(LOG_TAG, sb.toString());
    }

    private static synchronized void debugLogMatchResult(boolean result, ScanResult device, Object criteria) {
        StringBuilder sb = new StringBuilder();
        sb.append(getDeviceDisplayNameInternal(device));
        sb.append(result ? " ~ " : " !~ ");
        sb.append(criteria);
        Log.i(LOG_TAG, sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getDeviceDisplayNameInternal(BluetoothDevice device) {
        return TextUtils.firstNotEmpty(device.getAliasName(), device.getAddress());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getDeviceDisplayNameInternal(ScanResult device) {
        return TextUtils.firstNotEmpty(device.SSID, device.BSSID);
    }

    private protected static String getDeviceMacAddress(Parcelable device) {
        if (device instanceof BluetoothDevice) {
            return ((BluetoothDevice) device).getAddress();
        }
        if (device instanceof ScanResult) {
            return ((ScanResult) device).BSSID;
        }
        if (device instanceof android.bluetooth.le.ScanResult) {
            return getDeviceMacAddress(((android.bluetooth.le.ScanResult) device).getDevice());
        }
        throw new IllegalArgumentException("Unknown device type: " + device);
    }
}
