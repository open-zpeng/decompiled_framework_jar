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

    public LegacySensorManager(SensorManager sensorManager) {
        this.mSensorManager = sensorManager;
        synchronized (SensorManager.class) {
            if (!sInitialized) {
                sWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
                if (sWindowManager != null) {
                    try {
                        sRotation = sWindowManager.watchRotation(new IRotationWatcher.Stub() { // from class: android.hardware.LegacySensorManager.1
                            @Override // android.view.IRotationWatcher
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

    public int getSensors() {
        int result = 0;
        List<Sensor> fullList = this.mSensorManager.getFullSensorList();
        for (Sensor i : fullList) {
            int type = i.getType();
            if (type == 1) {
                result |= 2;
            } else if (type == 2) {
                result |= 8;
            } else if (type == 3) {
                result |= 129;
            }
        }
        return result;
    }

    public boolean registerListener(SensorListener listener, int sensors, int rate) {
        if (listener == null) {
            return false;
        }
        boolean result = registerLegacyListener(2, 1, listener, sensors, rate) || 0 != 0;
        boolean result2 = registerLegacyListener(8, 2, listener, sensors, rate) || result;
        boolean result3 = registerLegacyListener(128, 3, listener, sensors, rate) || result2;
        boolean result4 = registerLegacyListener(1, 3, listener, sensors, rate) || result3;
        return registerLegacyListener(4, 7, listener, sensors, rate) || result4;
    }

    private boolean registerLegacyListener(int legacyType, int type, SensorListener listener, int sensors, int rate) {
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

    public void unregisterListener(SensorListener listener, int sensors) {
        if (listener == null) {
            return;
        }
        unregisterLegacyListener(2, 1, listener, sensors);
        unregisterLegacyListener(8, 2, listener, sensors);
        unregisterLegacyListener(128, 3, listener, sensors);
        unregisterLegacyListener(1, 3, listener, sensors);
        unregisterLegacyListener(4, 7, listener, sensors);
    }

    private void unregisterLegacyListener(int legacyType, int type, SensorListener listener, int sensors) {
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

    static void onRotationChanged(int rotation) {
        synchronized (SensorManager.class) {
            sRotation = rotation;
        }
    }

    static int getRotation() {
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

        LegacyListener(SensorListener target) {
            this.mTarget = target;
        }

        boolean registerSensor(int legacyType) {
            int i = this.mSensors;
            if ((i & legacyType) != 0) {
                return false;
            }
            boolean alreadyHasOrientationSensor = hasOrientationSensor(i);
            this.mSensors |= legacyType;
            return (alreadyHasOrientationSensor && hasOrientationSensor(legacyType)) ? false : true;
        }

        boolean unregisterSensor(int legacyType) {
            int i = this.mSensors;
            if ((i & legacyType) == 0) {
                return false;
            }
            this.mSensors = i & (~legacyType);
            return (hasOrientationSensor(legacyType) && hasOrientationSensor(this.mSensors)) ? false : true;
        }

        boolean hasSensors() {
            return this.mSensors != 0;
        }

        private static boolean hasOrientationSensor(int sensors) {
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

        /* JADX WARN: Code restructure failed: missing block: B:16:0x0038, code lost:
            if (r10 != 128) goto L18;
         */
        /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:
            if (r10 != 128) goto L6;
         */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0032  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x005a  */
        /* JADX WARN: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void mapSensorDataToWindow(int r10, float[] r11, int r12) {
            /*
                r9 = this;
                r0 = 0
                r1 = r11[r0]
                r2 = 1
                r3 = r11[r2]
                r4 = 2
                r5 = r11[r4]
                r6 = 128(0x80, float:1.8E-43)
                r7 = 8
                if (r10 == r2) goto L1d
                if (r10 == r4) goto L19
                if (r10 == r7) goto L16
                if (r10 == r6) goto L1d
                goto L1f
            L16:
                float r1 = -r1
                float r3 = -r3
                goto L1f
            L19:
                float r1 = -r1
                float r3 = -r3
                float r5 = -r5
                goto L1f
            L1d:
                float r5 = -r5
            L1f:
                r11[r0] = r1
                r11[r2] = r3
                r11[r4] = r5
                r8 = 3
                r11[r8] = r1
                r8 = 4
                r11[r8] = r3
                r8 = 5
                r11[r8] = r5
                r8 = r12 & 1
                if (r8 == 0) goto L56
                if (r10 == r2) goto L43
                if (r10 == r4) goto L3b
                if (r10 == r7) goto L3b
                if (r10 == r6) goto L43
                goto L56
            L3b:
                float r8 = -r3
                r11[r0] = r8
                r11[r2] = r1
                r11[r4] = r5
                goto L56
            L43:
                r8 = 1132920832(0x43870000, float:270.0)
                int r8 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
                if (r8 >= 0) goto L4c
                r8 = 90
                goto L4e
            L4c:
                r8 = -270(0xfffffffffffffef2, float:NaN)
            L4e:
                float r8 = (float) r8
                float r8 = r8 + r1
                r11[r0] = r8
                r11[r2] = r5
                r11[r4] = r3
            L56:
                r8 = r12 & 2
                if (r8 == 0) goto L84
                r1 = r11[r0]
                r3 = r11[r2]
                r5 = r11[r4]
                if (r10 == r2) goto L72
                if (r10 == r4) goto L69
                if (r10 == r7) goto L69
                if (r10 == r6) goto L72
                goto L84
            L69:
                float r6 = -r1
                r11[r0] = r6
                float r0 = -r3
                r11[r2] = r0
                r11[r4] = r5
                goto L84
            L72:
                r6 = 1127481344(0x43340000, float:180.0)
                int r7 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1))
                if (r7 < 0) goto L7b
                float r6 = r1 - r6
                goto L7c
            L7b:
                float r6 = r6 + r1
            L7c:
                r11[r0] = r6
                float r0 = -r3
                r11[r2] = r0
                float r0 = -r5
                r11[r4] = r0
            L84:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.LegacySensorManager.LegacyListener.mapSensorDataToWindow(int, float[], int):void");
        }

        private static int getLegacySensorType(int type) {
            if (type != 1) {
                if (type != 2) {
                    if (type != 3) {
                        if (type == 7) {
                            return 4;
                        }
                        return 0;
                    }
                    return 128;
                }
                return 8;
            }
            return 2;
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

        public float filter(long time, float in) {
            float v = in;
            float v1 = this.mV[this.mIndex];
            if (v - v1 > 180.0f) {
                v -= 360.0f;
            } else if (v1 - v > 180.0f) {
                v += 360.0f;
            }
            this.mIndex++;
            if (this.mIndex >= 24) {
                this.mIndex = 12;
            }
            float[] fArr = this.mV;
            int i = this.mIndex;
            fArr[i] = v;
            long[] jArr = this.mT;
            jArr[i] = time;
            fArr[i - 12] = v;
            jArr[i - 12] = time;
            float E = 0.0f;
            float D = 0.0f;
            float C = 0.0f;
            float B = 0.0f;
            float A = 0.0f;
            for (int i2 = 0; i2 < 11; i2++) {
                int j = (this.mIndex - 1) - i2;
                float Z = this.mV[j];
                long[] jArr2 = this.mT;
                float T = ((float) (((jArr2[j] / 2) + (jArr2[j + 1] / 2)) - time)) * 1.0E-9f;
                float dT = ((float) (jArr2[j] - jArr2[j + 1])) * 1.0E-9f;
                float dT2 = dT * dT;
                A += Z * dT2;
                B += T * dT2 * T;
                C += T * dT2;
                D += T * dT2 * Z;
                E += dT2;
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
