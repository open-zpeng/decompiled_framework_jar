package android.hardware;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.MemoryFile;
import android.os.MessageQueue;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.internal.annotations.GuardedBy;
import dalvik.system.CloseGuard;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class SystemSensorManager extends SensorManager {
    private static final boolean DEBUG_DYNAMIC_SENSOR = true;
    private static final int MAX_LISTENER_COUNT = 128;
    private static final int MIN_DIRECT_CHANNEL_BUFFER_SIZE = 104;
    private final Context mContext;
    private BroadcastReceiver mDynamicSensorBroadcastReceiver;
    private final Looper mMainLooper;
    private final long mNativeInstance;
    private final int mTargetSdkLevel;
    private static final Object sLock = new Object();
    @GuardedBy("sLock")
    private static boolean sNativeClassInited = false;
    @GuardedBy("sLock")
    private static InjectEventQueue sInjectEventQueue = null;
    private final ArrayList<Sensor> mFullSensorsList = new ArrayList<>();
    private List<Sensor> mFullDynamicSensorsList = new ArrayList();
    private boolean mDynamicSensorListDirty = true;
    private final HashMap<Integer, Sensor> mHandleToSensor = new HashMap<>();
    private final HashMap<SensorEventListener, SensorEventQueue> mSensorListeners = new HashMap<>();
    private final HashMap<TriggerEventListener, TriggerEventQueue> mTriggerListeners = new HashMap<>();
    private HashMap<SensorManager.DynamicSensorCallback, Handler> mDynamicSensorCallbacks = new HashMap<>();

    private static native void nativeClassInit();

    private static native int nativeConfigDirectChannel(long j, int i, int i2, int i3);

    private static native long nativeCreate(String str);

    private static native int nativeCreateDirectChannel(long j, long j2, int i, int i2, HardwareBuffer hardwareBuffer);

    private static native void nativeDestroyDirectChannel(long j, int i);

    private static native void nativeGetDynamicSensors(long j, List<Sensor> list);

    private static native boolean nativeGetSensorAtIndex(long j, Sensor sensor, int i);

    private static native boolean nativeIsDataInjectionEnabled(long j);

    private static native int nativeSetOperationParameter(long j, int i, int i2, float[] fArr, int[] iArr);

    public synchronized SystemSensorManager(Context context, Looper mainLooper) {
        synchronized (sLock) {
            if (!sNativeClassInited) {
                sNativeClassInited = true;
                nativeClassInit();
            }
        }
        this.mMainLooper = mainLooper;
        this.mTargetSdkLevel = context.getApplicationInfo().targetSdkVersion;
        this.mContext = context;
        this.mNativeInstance = nativeCreate(context.getOpPackageName());
        int index = 0;
        while (true) {
            Sensor sensor = new Sensor();
            if (nativeGetSensorAtIndex(this.mNativeInstance, sensor, index)) {
                this.mFullSensorsList.add(sensor);
                this.mHandleToSensor.put(Integer.valueOf(sensor.getHandle()), sensor);
                index++;
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.hardware.SensorManager
    public synchronized List<Sensor> getFullSensorList() {
        return this.mFullSensorsList;
    }

    @Override // android.hardware.SensorManager
    protected synchronized List<Sensor> getFullDynamicSensorList() {
        setupDynamicSensorBroadcastReceiver();
        updateDynamicSensorList();
        return this.mFullDynamicSensorsList;
    }

    @Override // android.hardware.SensorManager
    protected synchronized boolean registerListenerImpl(SensorEventListener listener, Sensor sensor, int delayUs, Handler handler, int maxBatchReportLatencyUs, int reservedFlags) {
        String fullClassName;
        if (listener == null || sensor == null) {
            Log.e("SensorManager", "sensor or listener is null");
            return false;
        } else if (sensor.getReportingMode() == 2) {
            Log.e("SensorManager", "Trigger Sensors should use the requestTriggerSensor.");
            return false;
        } else if (maxBatchReportLatencyUs < 0 || delayUs < 0) {
            Log.e("SensorManager", "maxBatchReportLatencyUs and delayUs should be non-negative");
            return false;
        } else if (this.mSensorListeners.size() >= 128) {
            throw new IllegalStateException("register failed, the sensor listeners size has exceeded the maximum limit 128");
        } else {
            synchronized (this.mSensorListeners) {
                SensorEventQueue queue = this.mSensorListeners.get(listener);
                if (queue == null) {
                    Looper looper = handler != null ? handler.getLooper() : this.mMainLooper;
                    if (listener.getClass().getEnclosingClass() != null) {
                        fullClassName = listener.getClass().getEnclosingClass().getName();
                    } else {
                        fullClassName = listener.getClass().getName();
                    }
                    SensorEventQueue queue2 = new SensorEventQueue(listener, looper, this, fullClassName);
                    if (!queue2.addSensor(sensor, delayUs, maxBatchReportLatencyUs)) {
                        queue2.dispose();
                        return false;
                    }
                    this.mSensorListeners.put(listener, queue2);
                    return true;
                }
                return queue.addSensor(sensor, delayUs, maxBatchReportLatencyUs);
            }
        }
    }

    @Override // android.hardware.SensorManager
    protected synchronized void unregisterListenerImpl(SensorEventListener listener, Sensor sensor) {
        boolean result;
        if (sensor != null && sensor.getReportingMode() == 2) {
            return;
        }
        synchronized (this.mSensorListeners) {
            SensorEventQueue queue = this.mSensorListeners.get(listener);
            if (queue != null) {
                if (sensor == null) {
                    result = queue.removeAllSensors();
                } else {
                    result = queue.removeSensor(sensor, true);
                }
                if (result && !queue.hasSensors()) {
                    this.mSensorListeners.remove(listener);
                    queue.dispose();
                }
            }
        }
    }

    @Override // android.hardware.SensorManager
    protected synchronized boolean requestTriggerSensorImpl(TriggerEventListener listener, Sensor sensor) {
        String fullClassName;
        if (sensor == null) {
            throw new IllegalArgumentException("sensor cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }
        if (sensor.getReportingMode() != 2) {
            return false;
        }
        if (this.mTriggerListeners.size() >= 128) {
            throw new IllegalStateException("request failed, the trigger listeners size has exceeded the maximum limit 128");
        }
        synchronized (this.mTriggerListeners) {
            TriggerEventQueue queue = this.mTriggerListeners.get(listener);
            if (queue == null) {
                if (listener.getClass().getEnclosingClass() != null) {
                    fullClassName = listener.getClass().getEnclosingClass().getName();
                } else {
                    fullClassName = listener.getClass().getName();
                }
                TriggerEventQueue queue2 = new TriggerEventQueue(listener, this.mMainLooper, this, fullClassName);
                if (!queue2.addSensor(sensor, 0, 0)) {
                    queue2.dispose();
                    return false;
                }
                this.mTriggerListeners.put(listener, queue2);
                return true;
            }
            return queue.addSensor(sensor, 0, 0);
        }
    }

    @Override // android.hardware.SensorManager
    protected synchronized boolean cancelTriggerSensorImpl(TriggerEventListener listener, Sensor sensor, boolean disable) {
        boolean result;
        if (sensor != null && sensor.getReportingMode() != 2) {
            return false;
        }
        synchronized (this.mTriggerListeners) {
            TriggerEventQueue queue = this.mTriggerListeners.get(listener);
            if (queue == null) {
                return false;
            }
            if (sensor == null) {
                result = queue.removeAllSensors();
            } else {
                result = queue.removeSensor(sensor, disable);
            }
            if (result && !queue.hasSensors()) {
                this.mTriggerListeners.remove(listener);
                queue.dispose();
            }
            return result;
        }
    }

    @Override // android.hardware.SensorManager
    protected synchronized boolean flushImpl(SensorEventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }
        synchronized (this.mSensorListeners) {
            SensorEventQueue queue = this.mSensorListeners.get(listener);
            if (queue == null) {
                return false;
            }
            return queue.flush() == 0;
        }
    }

    @Override // android.hardware.SensorManager
    protected synchronized boolean initDataInjectionImpl(boolean enable) {
        synchronized (sLock) {
            boolean z = true;
            try {
                if (enable) {
                    boolean isDataInjectionModeEnabled = nativeIsDataInjectionEnabled(this.mNativeInstance);
                    if (!isDataInjectionModeEnabled) {
                        Log.e("SensorManager", "Data Injection mode not enabled");
                        return false;
                    }
                    if (sInjectEventQueue == null) {
                        try {
                            sInjectEventQueue = new InjectEventQueue(this.mMainLooper, this, this.mContext.getPackageName());
                        } catch (RuntimeException e) {
                            Log.e("SensorManager", "Cannot create InjectEventQueue: " + e);
                        }
                    }
                    if (sInjectEventQueue == null) {
                        z = false;
                    }
                    return z;
                }
                if (sInjectEventQueue != null) {
                    sInjectEventQueue.dispose();
                    sInjectEventQueue = null;
                }
                return true;
            } finally {
            }
        }
    }

    @Override // android.hardware.SensorManager
    protected synchronized boolean injectSensorDataImpl(Sensor sensor, float[] values, int accuracy, long timestamp) {
        synchronized (sLock) {
            if (sInjectEventQueue == null) {
                Log.e("SensorManager", "Data injection mode not activated before calling injectSensorData");
                return false;
            }
            int ret = sInjectEventQueue.injectSensorData(sensor.getHandle(), values, accuracy, timestamp);
            if (ret != 0) {
                sInjectEventQueue.dispose();
                sInjectEventQueue = null;
            }
            return ret == 0;
        }
    }

    private synchronized void cleanupSensorConnection(Sensor sensor) {
        this.mHandleToSensor.remove(Integer.valueOf(sensor.getHandle()));
        if (sensor.getReportingMode() == 2) {
            synchronized (this.mTriggerListeners) {
                HashMap<TriggerEventListener, TriggerEventQueue> triggerListeners = new HashMap<>(this.mTriggerListeners);
                for (TriggerEventListener l : triggerListeners.keySet()) {
                    Log.i("SensorManager", "removed trigger listener" + l.toString() + " due to sensor disconnection");
                    cancelTriggerSensorImpl(l, sensor, true);
                }
            }
            return;
        }
        synchronized (this.mSensorListeners) {
            HashMap<SensorEventListener, SensorEventQueue> sensorListeners = new HashMap<>(this.mSensorListeners);
            for (SensorEventListener l2 : sensorListeners.keySet()) {
                Log.i("SensorManager", "removed event listener" + l2.toString() + " due to sensor disconnection");
                unregisterListenerImpl(l2, sensor);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateDynamicSensorList() {
        synchronized (this.mFullDynamicSensorsList) {
            if (this.mDynamicSensorListDirty) {
                List<Sensor> list = new ArrayList<>();
                nativeGetDynamicSensors(this.mNativeInstance, list);
                List<Sensor> updatedList = new ArrayList<>();
                final List<Sensor> addedList = new ArrayList<>();
                final List<Sensor> removedList = new ArrayList<>();
                boolean changed = diffSortedSensorList(this.mFullDynamicSensorsList, list, updatedList, addedList, removedList);
                if (changed) {
                    Log.i("SensorManager", "DYNS dynamic sensor list cached should be updated");
                    this.mFullDynamicSensorsList = updatedList;
                    for (Sensor s : addedList) {
                        this.mHandleToSensor.put(Integer.valueOf(s.getHandle()), s);
                    }
                    Handler mainHandler = new Handler(this.mContext.getMainLooper());
                    for (Map.Entry<SensorManager.DynamicSensorCallback, Handler> entry : this.mDynamicSensorCallbacks.entrySet()) {
                        final SensorManager.DynamicSensorCallback callback = entry.getKey();
                        Handler handler = entry.getValue() == null ? mainHandler : entry.getValue();
                        handler.post(new Runnable() { // from class: android.hardware.SystemSensorManager.1
                            @Override // java.lang.Runnable
                            public void run() {
                                for (Sensor s2 : addedList) {
                                    callback.onDynamicSensorConnected(s2);
                                }
                                for (Sensor s3 : removedList) {
                                    callback.onDynamicSensorDisconnected(s3);
                                }
                            }
                        });
                    }
                    for (Sensor s2 : removedList) {
                        cleanupSensorConnection(s2);
                    }
                }
                this.mDynamicSensorListDirty = false;
            }
        }
    }

    private synchronized void setupDynamicSensorBroadcastReceiver() {
        if (this.mDynamicSensorBroadcastReceiver == null) {
            this.mDynamicSensorBroadcastReceiver = new BroadcastReceiver() { // from class: android.hardware.SystemSensorManager.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction() == Intent.ACTION_DYNAMIC_SENSOR_CHANGED) {
                        Log.i("SensorManager", "DYNS received DYNAMIC_SENSOR_CHANED broadcast");
                        SystemSensorManager.this.mDynamicSensorListDirty = true;
                        SystemSensorManager.this.updateDynamicSensorList();
                    }
                }
            };
            IntentFilter filter = new IntentFilter("dynamic_sensor_change");
            filter.addAction(Intent.ACTION_DYNAMIC_SENSOR_CHANGED);
            this.mContext.registerReceiver(this.mDynamicSensorBroadcastReceiver, filter);
        }
    }

    private synchronized void teardownDynamicSensorBroadcastReceiver() {
        this.mDynamicSensorCallbacks.clear();
        this.mContext.unregisterReceiver(this.mDynamicSensorBroadcastReceiver);
        this.mDynamicSensorBroadcastReceiver = null;
    }

    @Override // android.hardware.SensorManager
    protected synchronized void registerDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback callback, Handler handler) {
        Log.i("SensorManager", "DYNS Register dynamic sensor callback");
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        }
        if (this.mDynamicSensorCallbacks.containsKey(callback)) {
            return;
        }
        setupDynamicSensorBroadcastReceiver();
        this.mDynamicSensorCallbacks.put(callback, handler);
    }

    @Override // android.hardware.SensorManager
    protected synchronized void unregisterDynamicSensorCallbackImpl(SensorManager.DynamicSensorCallback callback) {
        Log.i("SensorManager", "Removing dynamic sensor listerner");
        this.mDynamicSensorCallbacks.remove(callback);
    }

    private static synchronized boolean diffSortedSensorList(List<Sensor> oldList, List<Sensor> newList, List<Sensor> updated, List<Sensor> added, List<Sensor> removed) {
        boolean changed = false;
        int i = 0;
        int j = 0;
        while (true) {
            if (j < oldList.size() && (i >= newList.size() || newList.get(i).getHandle() > oldList.get(j).getHandle())) {
                changed = true;
                if (removed != null) {
                    removed.add(oldList.get(j));
                }
                j++;
            } else if (i < newList.size() && (j >= oldList.size() || newList.get(i).getHandle() < oldList.get(j).getHandle())) {
                changed = true;
                if (added != null) {
                    added.add(newList.get(i));
                }
                if (updated != null) {
                    updated.add(newList.get(i));
                }
                i++;
            } else if (i >= newList.size() || j >= oldList.size() || newList.get(i).getHandle() != oldList.get(j).getHandle()) {
                break;
            } else {
                if (updated != null) {
                    updated.add(oldList.get(j));
                }
                i++;
                j++;
            }
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.hardware.SensorManager
    public synchronized int configureDirectChannelImpl(SensorDirectChannel channel, Sensor sensor, int rate) {
        if (!channel.isOpen()) {
            throw new IllegalStateException("channel is closed");
        }
        if (rate < 0 || rate > 3) {
            throw new IllegalArgumentException("rate parameter invalid");
        }
        if (sensor == null && rate != 0) {
            throw new IllegalArgumentException("when sensor is null, rate can only be DIRECT_RATE_STOP");
        }
        int sensorHandle = sensor == null ? -1 : sensor.getHandle();
        int ret = nativeConfigDirectChannel(this.mNativeInstance, channel.getNativeHandle(), sensorHandle, rate);
        if (rate == 0) {
            return ret == 0 ? 1 : 0;
        } else if (ret > 0) {
            return ret;
        } else {
            return 0;
        }
    }

    @Override // android.hardware.SensorManager
    protected synchronized SensorDirectChannel createDirectChannelImpl(MemoryFile memoryFile, HardwareBuffer hardwareBuffer) {
        long size;
        int id;
        int type;
        if (memoryFile != null) {
            try {
                int fd = memoryFile.getFileDescriptor().getInt$();
                if (memoryFile.length() < 104) {
                    throw new IllegalArgumentException("Size of MemoryFile has to be greater than 104");
                }
                size = memoryFile.length();
                id = nativeCreateDirectChannel(this.mNativeInstance, size, 1, fd, null);
                if (id <= 0) {
                    throw new UncheckedIOException(new IOException("create MemoryFile direct channel failed " + id));
                }
                type = 1;
            } catch (IOException e) {
                throw new IllegalArgumentException("MemoryFile object is not valid");
            }
        } else if (hardwareBuffer != null) {
            if (hardwareBuffer.getFormat() != 33) {
                throw new IllegalArgumentException("Format of HardwareBuffer must be BLOB");
            }
            if (hardwareBuffer.getHeight() == 1) {
                if (hardwareBuffer.getWidth() < 104) {
                    throw new IllegalArgumentException("Width if HaradwareBuffer must be greater than 104");
                }
                if ((hardwareBuffer.getUsage() & 8388608) == 0) {
                    throw new IllegalArgumentException("HardwareBuffer must set usage flag USAGE_SENSOR_DIRECT_DATA");
                }
                size = hardwareBuffer.getWidth();
                id = nativeCreateDirectChannel(this.mNativeInstance, size, 2, -1, hardwareBuffer);
                if (id <= 0) {
                    throw new UncheckedIOException(new IOException("create HardwareBuffer direct channel failed " + id));
                }
                type = 2;
            } else {
                throw new IllegalArgumentException("Height of HardwareBuffer must be 1");
            }
        } else {
            throw new NullPointerException("shared memory object cannot be null");
        }
        return new SensorDirectChannel(this, id, type, size);
    }

    @Override // android.hardware.SensorManager
    protected synchronized void destroyDirectChannelImpl(SensorDirectChannel channel) {
        if (channel != null) {
            nativeDestroyDirectChannel(this.mNativeInstance, channel.getNativeHandle());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class BaseEventQueue {
        protected static final int OPERATING_MODE_DATA_INJECTION = 1;
        protected static final int OPERATING_MODE_NORMAL = 0;
        protected final SystemSensorManager mManager;
        private long mNativeSensorEventQueue;
        private final SparseBooleanArray mActiveSensors = new SparseBooleanArray();
        protected final SparseIntArray mSensorAccuracies = new SparseIntArray();
        private final CloseGuard mCloseGuard = CloseGuard.get();

        private static native void nativeDestroySensorEventQueue(long j);

        private static native int nativeDisableSensor(long j, int i);

        private static native int nativeEnableSensor(long j, int i, int i2, int i3);

        private static native int nativeFlushSensor(long j);

        private static native long nativeInitBaseEventQueue(long j, WeakReference<BaseEventQueue> weakReference, MessageQueue messageQueue, String str, int i, String str2);

        private static native int nativeInjectSensorData(long j, int i, float[] fArr, int i2, long j2);

        protected abstract synchronized void addSensorEvent(Sensor sensor);

        public private abstract void dispatchFlushCompleteEvent(int i);

        public private abstract void dispatchSensorEvent(int i, float[] fArr, int i2, long j);

        protected abstract synchronized void removeSensorEvent(Sensor sensor);

        synchronized BaseEventQueue(Looper looper, SystemSensorManager manager, int mode, String packageName) {
            this.mNativeSensorEventQueue = nativeInitBaseEventQueue(manager.mNativeInstance, new WeakReference(this), looper.getQueue(), packageName == null ? "" : packageName, mode, manager.mContext.getOpPackageName());
            this.mCloseGuard.open("dispose");
            this.mManager = manager;
        }

        public synchronized void dispose() {
            dispose(false);
        }

        public synchronized boolean addSensor(Sensor sensor, int delayUs, int maxBatchReportLatencyUs) {
            int handle = sensor.getHandle();
            if (this.mActiveSensors.get(handle)) {
                return false;
            }
            this.mActiveSensors.put(handle, true);
            addSensorEvent(sensor);
            if (enableSensor(sensor, delayUs, maxBatchReportLatencyUs) == 0 || (maxBatchReportLatencyUs != 0 && (maxBatchReportLatencyUs <= 0 || enableSensor(sensor, delayUs, 0) == 0))) {
                return true;
            }
            removeSensor(sensor, false);
            return false;
        }

        public synchronized boolean removeAllSensors() {
            for (int i = 0; i < this.mActiveSensors.size(); i++) {
                if (this.mActiveSensors.valueAt(i)) {
                    int handle = this.mActiveSensors.keyAt(i);
                    Sensor sensor = (Sensor) this.mManager.mHandleToSensor.get(Integer.valueOf(handle));
                    if (sensor != null) {
                        disableSensor(sensor);
                        this.mActiveSensors.put(handle, false);
                        removeSensorEvent(sensor);
                    }
                }
            }
            return true;
        }

        public synchronized boolean removeSensor(Sensor sensor, boolean disable) {
            int handle = sensor.getHandle();
            if (this.mActiveSensors.get(handle)) {
                if (disable) {
                    disableSensor(sensor);
                }
                this.mActiveSensors.put(sensor.getHandle(), false);
                removeSensorEvent(sensor);
                return true;
            }
            return false;
        }

        public synchronized int flush() {
            if (this.mNativeSensorEventQueue == 0) {
                throw new NullPointerException();
            }
            return nativeFlushSensor(this.mNativeSensorEventQueue);
        }

        public synchronized boolean hasSensors() {
            return this.mActiveSensors.indexOfValue(true) >= 0;
        }

        protected void finalize() throws Throwable {
            try {
                dispose(true);
            } finally {
                super.finalize();
            }
        }

        private synchronized void dispose(boolean finalized) {
            if (this.mCloseGuard != null) {
                if (finalized) {
                    this.mCloseGuard.warnIfOpen();
                }
                this.mCloseGuard.close();
            }
            if (this.mNativeSensorEventQueue != 0) {
                nativeDestroySensorEventQueue(this.mNativeSensorEventQueue);
                this.mNativeSensorEventQueue = 0L;
            }
        }

        private synchronized int enableSensor(Sensor sensor, int rateUs, int maxBatchReportLatencyUs) {
            if (this.mNativeSensorEventQueue == 0) {
                throw new NullPointerException();
            }
            if (sensor == null) {
                throw new NullPointerException();
            }
            return nativeEnableSensor(this.mNativeSensorEventQueue, sensor.getHandle(), rateUs, maxBatchReportLatencyUs);
        }

        protected synchronized int injectSensorDataBase(int handle, float[] values, int accuracy, long timestamp) {
            return nativeInjectSensorData(this.mNativeSensorEventQueue, handle, values, accuracy, timestamp);
        }

        private synchronized int disableSensor(Sensor sensor) {
            if (this.mNativeSensorEventQueue == 0) {
                throw new NullPointerException();
            }
            if (sensor == null) {
                throw new NullPointerException();
            }
            return nativeDisableSensor(this.mNativeSensorEventQueue, sensor.getHandle());
        }

        public private void dispatchAdditionalInfoEvent(int handle, int type, int serial, float[] floatValues, int[] intValues) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class SensorEventQueue extends BaseEventQueue {
        private final SensorEventListener mListener;
        private final SparseArray<SensorEvent> mSensorsEvents;

        public synchronized SensorEventQueue(SensorEventListener listener, Looper looper, SystemSensorManager manager, String packageName) {
            super(looper, manager, 0, packageName);
            this.mSensorsEvents = new SparseArray<>();
            this.mListener = listener;
        }

        @Override // android.hardware.SystemSensorManager.BaseEventQueue
        public synchronized void addSensorEvent(Sensor sensor) {
            SensorEvent t = new SensorEvent(Sensor.getMaxLengthValuesArray(sensor, this.mManager.mTargetSdkLevel));
            synchronized (this.mSensorsEvents) {
                this.mSensorsEvents.put(sensor.getHandle(), t);
            }
        }

        @Override // android.hardware.SystemSensorManager.BaseEventQueue
        public synchronized void removeSensorEvent(Sensor sensor) {
            synchronized (this.mSensorsEvents) {
                this.mSensorsEvents.delete(sensor.getHandle());
            }
        }

        protected synchronized void dispatchSensorEvent(int handle, float[] values, int inAccuracy, long timestamp) {
            SensorEvent t;
            Sensor sensor = (Sensor) this.mManager.mHandleToSensor.get(Integer.valueOf(handle));
            if (sensor == null) {
                return;
            }
            synchronized (this.mSensorsEvents) {
                t = this.mSensorsEvents.get(handle);
            }
            if (t == null) {
                return;
            }
            System.arraycopy(values, 0, t.values, 0, t.values.length);
            t.timestamp = timestamp;
            t.accuracy = inAccuracy;
            t.sensor = sensor;
            int accuracy = this.mSensorAccuracies.get(handle);
            if (t.accuracy >= 0 && accuracy != t.accuracy) {
                this.mSensorAccuracies.put(handle, t.accuracy);
                this.mListener.onAccuracyChanged(t.sensor, t.accuracy);
            }
            this.mListener.onSensorChanged(t);
        }

        protected synchronized void dispatchFlushCompleteEvent(int handle) {
            Sensor sensor;
            if (!(this.mListener instanceof SensorEventListener2) || (sensor = (Sensor) this.mManager.mHandleToSensor.get(Integer.valueOf(handle))) == null) {
                return;
            }
            ((SensorEventListener2) this.mListener).onFlushCompleted(sensor);
        }

        protected synchronized void dispatchAdditionalInfoEvent(int handle, int type, int serial, float[] floatValues, int[] intValues) {
            Sensor sensor;
            if (!(this.mListener instanceof SensorEventCallback) || (sensor = (Sensor) this.mManager.mHandleToSensor.get(Integer.valueOf(handle))) == null) {
                return;
            }
            SensorAdditionalInfo info = new SensorAdditionalInfo(sensor, type, serial, intValues, floatValues);
            ((SensorEventCallback) this.mListener).onSensorAdditionalInfo(info);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class TriggerEventQueue extends BaseEventQueue {
        private final TriggerEventListener mListener;
        private final SparseArray<TriggerEvent> mTriggerEvents;

        public synchronized TriggerEventQueue(TriggerEventListener listener, Looper looper, SystemSensorManager manager, String packageName) {
            super(looper, manager, 0, packageName);
            this.mTriggerEvents = new SparseArray<>();
            this.mListener = listener;
        }

        @Override // android.hardware.SystemSensorManager.BaseEventQueue
        public synchronized void addSensorEvent(Sensor sensor) {
            TriggerEvent t = new TriggerEvent(Sensor.getMaxLengthValuesArray(sensor, this.mManager.mTargetSdkLevel));
            synchronized (this.mTriggerEvents) {
                this.mTriggerEvents.put(sensor.getHandle(), t);
            }
        }

        @Override // android.hardware.SystemSensorManager.BaseEventQueue
        public synchronized void removeSensorEvent(Sensor sensor) {
            synchronized (this.mTriggerEvents) {
                this.mTriggerEvents.delete(sensor.getHandle());
            }
        }

        protected synchronized void dispatchSensorEvent(int handle, float[] values, int accuracy, long timestamp) {
            TriggerEvent t;
            Sensor sensor = (Sensor) this.mManager.mHandleToSensor.get(Integer.valueOf(handle));
            if (sensor == null) {
                return;
            }
            synchronized (this.mTriggerEvents) {
                t = this.mTriggerEvents.get(handle);
            }
            if (t == null) {
                Log.e("SensorManager", "Error: Trigger Event is null for Sensor: " + sensor);
                return;
            }
            System.arraycopy(values, 0, t.values, 0, t.values.length);
            t.timestamp = timestamp;
            t.sensor = sensor;
            this.mManager.cancelTriggerSensorImpl(this.mListener, sensor, false);
            this.mListener.onTrigger(t);
        }

        protected synchronized void dispatchFlushCompleteEvent(int handle) {
        }
    }

    /* loaded from: classes.dex */
    final class InjectEventQueue extends BaseEventQueue {
        public InjectEventQueue(Looper looper, SystemSensorManager manager, String packageName) {
            super(looper, manager, 1, packageName);
        }

        synchronized int injectSensorData(int handle, float[] values, int accuracy, long timestamp) {
            return injectSensorDataBase(handle, values, accuracy, timestamp);
        }

        protected synchronized void dispatchSensorEvent(int handle, float[] values, int accuracy, long timestamp) {
        }

        protected synchronized void dispatchFlushCompleteEvent(int handle) {
        }

        @Override // android.hardware.SystemSensorManager.BaseEventQueue
        protected synchronized void addSensorEvent(Sensor sensor) {
        }

        @Override // android.hardware.SystemSensorManager.BaseEventQueue
        protected synchronized void removeSensorEvent(Sensor sensor) {
        }
    }

    @Override // android.hardware.SensorManager
    protected synchronized boolean setOperationParameterImpl(SensorAdditionalInfo parameter) {
        int handle = parameter.sensor != null ? parameter.sensor.getHandle() : -1;
        return nativeSetOperationParameter(this.mNativeInstance, handle, parameter.type, parameter.floatValues, parameter.intValues) == 0;
    }
}
