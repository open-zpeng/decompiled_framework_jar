package android.hardware;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.IRotationWatcher;
import android.view.IWindowManager;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
final class LegacySensorManager {
    private static boolean sInitialized;
    private static int sRotation = 0;
    private static IWindowManager sWindowManager;
    private final HashMap<SensorListener, LegacyListener> mLegacyListenersMap = new HashMap<>();
    private final SensorManager mSensorManager;

    public synchronized LegacySensorManager(SensorManager sensorManager) {
        this.mSensorManager = sensorManager;
        synchronized (SensorManager.class) {
            if (!sInitialized) {
                sWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
                if (sWindowManager != null) {
                    try {
                        sRotation = sWindowManager.watchRotation(new IRotationWatcher.Stub() { // from class: android.hardware.LegacySensorManager.1
                            public void onRotationChanged(int rotation) {
                                LegacySensorManager.onRotationChanged(rotation);
                            }
                        }, 0);
                    } catch (RemoteException e) {
                    }
                }
            }
        }
    }

    public synchronized int getSensors() {
        int result = 0;
        List<Sensor> fullList = this.mSensorManager.getFullSensorList();
        for (Sensor i : fullList) {
            switch (i.getType()) {
                case 1:
                    result |= 2;
                    break;
                case 2:
                    result |= 8;
                    break;
                case 3:
                    result |= 129;
                    break;
            }
        }
        return result;
    }

    public synchronized boolean registerListener(SensorListener listener, int sensors, int rate) {
        if (listener == null) {
            return false;
        }
        boolean result = registerLegacyListener(2, 1, listener, sensors, rate) || 0 != 0;
        boolean result2 = registerLegacyListener(8, 2, listener, sensors, rate) || result;
        boolean result3 = registerLegacyListener(128, 3, listener, sensors, rate) || result2;
        boolean result4 = registerLegacyListener(1, 3, listener, sensors, rate) || result3;
        return registerLegacyListener(4, 7, listener, sensors, rate) || result4;
    }

    private synchronized boolean registerLegacyListener(int legacyType, int type, SensorListener listener, int sensors, int rate) {
        Sensor sensor;
        boolean result = false;
        if ((sensors & legacyType) != 0 && (sensor = this.mSensorManager.getDefaultSensor(type)) != null) {
            synchronized (this.mLegacyListenersMap) {
                LegacyListener legacyListener = this.mLegacyListenersMap.get(listener);
                if (legacyListener == null) {
                    legacyListener = new LegacyListener(listener);
                    this.mLegacyListenersMap.put(listener, legacyListener);
                }
                if (legacyListener.registerSensor(legacyType)) {
                    result = this.mSensorManager.registerListener(legacyListener, sensor, rate);
                } else {
                    result = true;
                }
            }
        }
        return result;
    }

    public synchronized void unregisterListener(SensorListener listener, int sensors) {
        if (listener == null) {
            return;
        }
        unregisterLegacyListener(2, 1, listener, sensors);
        unregisterLegacyListener(8, 2, listener, sensors);
        unregisterLegacyListener(128, 3, listener, sensors);
        unregisterLegacyListener(1, 3, listener, sensors);
        unregisterLegacyListener(4, 7, listener, sensors);
    }

    private synchronized void unregisterLegacyListener(int legacyType, int type, SensorListener listener, int sensors) {
        Sensor sensor;
        if ((sensors & legacyType) != 0 && (sensor = this.mSensorManager.getDefaultSensor(type)) != null) {
            synchronized (this.mLegacyListenersMap) {
                LegacyListener legacyListener = this.mLegacyListenersMap.get(listener);
                if (legacyListener != null && legacyListener.unregisterSensor(legacyType)) {
                    this.mSensorManager.unregisterListener(legacyListener, sensor);
                    if (!legacyListener.hasSensors()) {
                        this.mLegacyListenersMap.remove(listener);
                    }
                }
            }
        }
    }

    static synchronized void onRotationChanged(int rotation) {
        synchronized (SensorManager.class) {
            sRotation = rotation;
        }
    }

    static synchronized int getRotation() {
        int i;
        synchronized (SensorManager.class) {
            i = sRotation;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class LegacyListener implements SensorEventListener {
        private SensorListener mTarget;
        private float[] mValues = new float[6];
        private final LmsFilter mYawfilter = new LmsFilter();
        private int mSensors = 0;

        synchronized LegacyListener(SensorListener target) {
            this.mTarget = target;
        }

        synchronized boolean registerSensor(int legacyType) {
            if ((this.mSensors & legacyType) != 0) {
                return false;
            }
            boolean alreadyHasOrientationSensor = hasOrientationSensor(this.mSensors);
            this.mSensors |= legacyType;
            return (alreadyHasOrientationSensor && hasOrientationSensor(legacyType)) ? false : true;
        }

        synchronized boolean unregisterSensor(int legacyType) {
            if ((this.mSensors & legacyType) == 0) {
                return false;
            }
            this.mSensors &= ~legacyType;
            return (hasOrientationSensor(legacyType) && hasOrientationSensor(this.mSensors)) ? false : true;
        }

        synchronized boolean hasSensors() {
            return this.mSensors != 0;
        }

        private static synchronized boolean hasOrientationSensor(int sensors) {
            return (sensors & 129) != 0;
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            try {
                this.mTarget.onAccuracyChanged(getLegacySensorType(sensor.getType()), accuracy);
            } catch (AbstractMethodError e) {
            }
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent event) {
            float[] v = this.mValues;
            v[0] = event.values[0];
            v[1] = event.values[1];
            v[2] = event.values[2];
            int type = event.sensor.getType();
            int legacyType = getLegacySensorType(type);
            mapSensorDataToWindow(legacyType, v, LegacySensorManager.getRotation());
            if (type == 3) {
                if ((this.mSensors & 128) != 0) {
                    this.mTarget.onSensorChanged(128, v);
                }
                if ((this.mSensors & 1) != 0) {
                    v[0] = this.mYawfilter.filter(event.timestamp, v[0]);
                    this.mTarget.onSensorChanged(1, v);
                    return;
                }
                return;
            }
            this.mTarget.onSensorChanged(legacyType, v);
        }

        private synchronized void mapSensorDataToWindow(int sensor, float[] values, int orientation) {
            float x = values[0];
            float y = values[1];
            float z = values[2];
            if (sensor != 8) {
                if (sensor != 128) {
                    switch (sensor) {
                        case 2:
                            x = -x;
                            y = -y;
                            z = -z;
                            break;
                    }
                }
                z = -z;
            } else {
                x = -x;
                y = -y;
            }
            values[0] = x;
            values[1] = y;
            values[2] = z;
            values[3] = x;
            values[4] = y;
            values[5] = z;
            if ((orientation & 1) != 0) {
                if (sensor != 8) {
                    if (sensor != 128) {
                        switch (sensor) {
                        }
                    }
                    values[0] = (x < 270.0f ? 90 : -270) + x;
                    values[1] = z;
                    values[2] = y;
                }
                values[0] = -y;
                values[1] = x;
                values[2] = z;
            }
            if ((orientation & 2) != 0) {
                float x2 = values[0];
                float y2 = values[1];
                float z2 = values[2];
                if (sensor != 8) {
                    if (sensor != 128) {
                        switch (sensor) {
                            case 1:
                                break;
                            case 2:
                                break;
                            default:
                                return;
                        }
                    }
                    values[0] = x2 >= 180.0f ? x2 - 180.0f : 180.0f + x2;
                    values[1] = -y2;
                    values[2] = -z2;
                    return;
                }
                values[0] = -x2;
                values[1] = -y2;
                values[2] = z2;
            }
        }

        private static synchronized int getLegacySensorType(int type) {
            if (type != 7) {
                switch (type) {
                    case 1:
                        return 2;
                    case 2:
                        return 8;
                    case 3:
                        return 128;
                    default:
                        return 0;
                }
            }
            return 4;
        }
    }

    /* loaded from: classes.dex */
    private static final class LmsFilter {
        private static final int COUNT = 12;
        private static final float PREDICTION_RATIO = 0.33333334f;
        private static final float PREDICTION_TIME = 0.08f;
        private static final int SENSORS_RATE_MS = 20;
        private float[] mV = new float[24];
        private long[] mT = new long[24];
        private int mIndex = 12;

        public synchronized float filter(long time, float in) {
            LmsFilter lmsFilter = this;
            float v = in;
            float v1 = lmsFilter.mV[lmsFilter.mIndex];
            if (v - v1 > 180.0f) {
                v -= 360.0f;
            } else if (v1 - v > 180.0f) {
                v += 360.0f;
            }
            lmsFilter.mIndex++;
            if (lmsFilter.mIndex >= 24) {
                lmsFilter.mIndex = 12;
            }
            lmsFilter.mV[lmsFilter.mIndex] = v;
            lmsFilter.mT[lmsFilter.mIndex] = time;
            lmsFilter.mV[lmsFilter.mIndex - 12] = v;
            lmsFilter.mT[lmsFilter.mIndex - 12] = time;
            float E = 0.0f;
            float D = 0.0f;
            float C = 0.0f;
            float B = 0.0f;
            float A = 0.0f;
            int i = 0;
            while (i < 11) {
                int j = (lmsFilter.mIndex - 1) - i;
                float Z = lmsFilter.mV[j];
                float T = ((float) (((lmsFilter.mT[j] / 2) + (lmsFilter.mT[j + 1] / 2)) - time)) * 1.0E-9f;
                float dT = ((float) (lmsFilter.mT[j] - lmsFilter.mT[j + 1])) * 1.0E-9f;
                float dT2 = dT * dT;
                A += Z * dT2;
                B += T * dT2 * T;
                C += T * dT2;
                D += T * dT2 * Z;
                E += dT2;
                i++;
                lmsFilter = this;
            }
            float E2 = E;
            float b = ((A * B) + (C * D)) / ((E2 * B) + (C * C));
            float a = ((E2 * b) - A) / C;
            float f = ((PREDICTION_TIME * a) + b) * 0.0027777778f;
            if ((f >= 0.0f ? f : -f) >= 0.5f) {
                f = (f - ((float) Math.ceil(0.5f + f))) + 1.0f;
            }
            if (f < 0.0f) {
                f += 1.0f;
            }
            return f * 360.0f;
        }
    }
}
