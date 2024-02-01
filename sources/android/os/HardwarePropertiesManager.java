package android.os;

import android.content.Context;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public class HardwarePropertiesManager {
    public static final int DEVICE_TEMPERATURE_BATTERY = 2;
    public static final int DEVICE_TEMPERATURE_CPU = 0;
    public static final int DEVICE_TEMPERATURE_GPU = 1;
    public static final int DEVICE_TEMPERATURE_SKIN = 3;
    private static final String TAG = HardwarePropertiesManager.class.getSimpleName();
    public static final int TEMPERATURE_CURRENT = 0;
    public static final int TEMPERATURE_SHUTDOWN = 2;
    public static final int TEMPERATURE_THROTTLING = 1;
    public static final int TEMPERATURE_THROTTLING_BELOW_VR_MIN = 3;
    public static final float UNDEFINED_TEMPERATURE = -3.4028235E38f;
    private final Context mContext;
    private final IHardwarePropertiesManager mService;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface DeviceTemperatureType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface TemperatureSource {
    }

    public HardwarePropertiesManager(Context context, IHardwarePropertiesManager service) {
        this.mContext = context;
        this.mService = service;
    }

    public float[] getDeviceTemperatures(int type, int source) {
        if (type == 0 || type == 1 || type == 2 || type == 3) {
            if (source == 0 || source == 1 || source == 2 || source == 3) {
                try {
                    return this.mService.getDeviceTemperatures(this.mContext.getOpPackageName(), type, source);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            Log.w(TAG, "Unknown device temperature source.");
            return new float[0];
        }
        Log.w(TAG, "Unknown device temperature type.");
        return new float[0];
    }

    public CpuUsageInfo[] getCpuUsages() {
        try {
            return this.mService.getCpuUsages(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public float[] getFanSpeeds() {
        try {
            return this.mService.getFanSpeeds(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
