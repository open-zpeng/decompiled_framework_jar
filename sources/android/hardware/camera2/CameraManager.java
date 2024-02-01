package android.hardware.camera2;

import android.content.Context;
import android.hardware.CameraInfo;
import android.hardware.CameraStatus;
import android.hardware.ICameraService;
import android.hardware.ICameraServiceListener;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.impl.CameraDeviceImpl;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.legacy.CameraDeviceUserShim;
import android.hardware.camera2.legacy.LegacyMetadataMapper;
import android.os.Binder;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class CameraManager {
    private static final int API_VERSION_1 = 1;
    private static final int API_VERSION_2 = 2;
    private static final int CAMERA_TYPE_ALL = 1;
    private static final int CAMERA_TYPE_BACKWARD_COMPATIBLE = 0;
    private static final String TAG = "CameraManager";
    private static final int USE_CALLING_UID = -1;
    private final Context mContext;
    private ArrayList<String> mDeviceIdList;
    private final boolean DEBUG = false;
    private final Object mLock = new Object();

    public synchronized CameraManager(Context context) {
        synchronized (this.mLock) {
            this.mContext = context;
        }
    }

    public String[] getCameraIdList() throws CameraAccessException {
        return CameraManagerGlobal.get().getCameraIdList();
    }

    public void registerAvailabilityCallback(AvailabilityCallback callback, Handler handler) {
        CameraManagerGlobal.get().registerAvailabilityCallback(callback, CameraDeviceImpl.checkAndWrapHandler(handler));
    }

    public void registerAvailabilityCallback(Executor executor, AvailabilityCallback callback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor was null");
        }
        CameraManagerGlobal.get().registerAvailabilityCallback(callback, executor);
    }

    public void unregisterAvailabilityCallback(AvailabilityCallback callback) {
        CameraManagerGlobal.get().unregisterAvailabilityCallback(callback);
    }

    public void registerTorchCallback(TorchCallback callback, Handler handler) {
        CameraManagerGlobal.get().registerTorchCallback(callback, CameraDeviceImpl.checkAndWrapHandler(handler));
    }

    public void registerTorchCallback(Executor executor, TorchCallback callback) {
        if (executor == null) {
            throw new IllegalArgumentException("executor was null");
        }
        CameraManagerGlobal.get().registerTorchCallback(callback, executor);
    }

    public void unregisterTorchCallback(TorchCallback callback) {
        CameraManagerGlobal.get().unregisterTorchCallback(callback);
    }

    public CameraCharacteristics getCameraCharacteristics(String cameraId) throws CameraAccessException {
        CameraCharacteristics characteristics = null;
        if (CameraManagerGlobal.sCameraServiceDisabled) {
            throw new IllegalArgumentException("No cameras available on device");
        }
        synchronized (this.mLock) {
            ICameraService cameraService = CameraManagerGlobal.get().getCameraService();
            if (cameraService == null) {
                throw new CameraAccessException(2, "Camera service is currently unavailable");
            }
            try {
                if (!supportsCamera2ApiLocked(cameraId)) {
                    int id = Integer.parseInt(cameraId);
                    String parameters = cameraService.getLegacyParameters(id);
                    CameraInfo info = cameraService.getCameraInfo(id);
                    characteristics = LegacyMetadataMapper.createCharacteristics(parameters, info);
                } else {
                    CameraMetadataNative info2 = cameraService.getCameraCharacteristics(cameraId);
                    characteristics = new CameraCharacteristics(info2);
                }
            } catch (RemoteException e) {
                throw new CameraAccessException(2, "Camera service is currently unavailable", e);
            } catch (ServiceSpecificException e2) {
                throwAsPublicException(e2);
            }
        }
        return characteristics;
    }

    private synchronized CameraDevice openCameraDeviceUserAsync(String cameraId, CameraDevice.StateCallback callback, Executor executor, int uid) throws CameraAccessException {
        ICameraDeviceUser cameraUser;
        CameraCharacteristics characteristics = getCameraCharacteristics(cameraId);
        synchronized (this.mLock) {
            ICameraDeviceUser cameraUser2 = null;
            try {
                try {
                    CameraDeviceImpl deviceImpl = new CameraDeviceImpl(cameraId, callback, executor, characteristics, this.mContext.getApplicationInfo().targetSdkVersion);
                    ICameraDeviceCallbacks callbacks = deviceImpl.getCallbacks();
                    try {
                    } catch (RemoteException e) {
                    } catch (ServiceSpecificException e2) {
                        e = e2;
                    }
                    try {
                        if (supportsCamera2ApiLocked(cameraId)) {
                            ICameraService cameraService = CameraManagerGlobal.get().getCameraService();
                            if (cameraService == null) {
                                throw new ServiceSpecificException(4, "Camera service is currently unavailable");
                            }
                            cameraUser = cameraService.connectDevice(callbacks, cameraId, this.mContext.getOpPackageName(), uid);
                        } else {
                            try {
                                int id = Integer.parseInt(cameraId);
                                Log.i(TAG, "Using legacy camera HAL.");
                                cameraUser = CameraDeviceUserShim.connectBinderShim(callbacks, id);
                            } catch (NumberFormatException e3) {
                                throw new IllegalArgumentException("Expected cameraId to be numeric, but it was: " + cameraId);
                            }
                        }
                        cameraUser2 = cameraUser;
                    } catch (RemoteException e4) {
                        ServiceSpecificException sse = new ServiceSpecificException(4, "Camera service is currently unavailable");
                        deviceImpl.setRemoteFailure(sse);
                        throwAsPublicException(sse);
                        deviceImpl.setRemoteDevice(cameraUser2);
                        return deviceImpl;
                    } catch (ServiceSpecificException e5) {
                        e = e5;
                        if (e.errorCode == 9) {
                            throw new AssertionError("Should've gone down the shim path");
                        }
                        if (e.errorCode != 7 && e.errorCode != 8 && e.errorCode != 6 && e.errorCode != 4 && e.errorCode != 10) {
                            throwAsPublicException(e);
                            deviceImpl.setRemoteDevice(cameraUser2);
                            return deviceImpl;
                        }
                        deviceImpl.setRemoteFailure(e);
                        if (e.errorCode == 6 || e.errorCode == 4 || e.errorCode == 7) {
                            throwAsPublicException(e);
                        }
                        deviceImpl.setRemoteDevice(cameraUser2);
                        return deviceImpl;
                    }
                    deviceImpl.setRemoteDevice(cameraUser2);
                    return deviceImpl;
                } catch (Throwable th) {
                    e = th;
                    throw e;
                }
            } catch (Throwable th2) {
                e = th2;
            }
        }
    }

    public void openCamera(String cameraId, CameraDevice.StateCallback callback, Handler handler) throws CameraAccessException {
        openCameraForUid(cameraId, callback, CameraDeviceImpl.checkAndWrapHandler(handler), -1);
    }

    public void openCamera(String cameraId, Executor executor, CameraDevice.StateCallback callback) throws CameraAccessException {
        if (executor == null) {
            throw new IllegalArgumentException("executor was null");
        }
        openCameraForUid(cameraId, callback, executor, -1);
    }

    public synchronized void openCameraForUid(String cameraId, CameraDevice.StateCallback callback, Executor executor, int clientUid) throws CameraAccessException {
        if (cameraId == null) {
            throw new IllegalArgumentException("cameraId was null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback was null");
        }
        if (CameraManagerGlobal.sCameraServiceDisabled) {
            throw new IllegalArgumentException("No cameras available on device");
        }
        openCameraDeviceUserAsync(cameraId, callback, executor, clientUid);
    }

    public void setTorchMode(String cameraId, boolean enabled) throws CameraAccessException {
        if (CameraManagerGlobal.sCameraServiceDisabled) {
            throw new IllegalArgumentException("No cameras available on device");
        }
        CameraManagerGlobal.get().setTorchMode(cameraId, enabled);
    }

    /* loaded from: classes.dex */
    public static abstract class AvailabilityCallback {
        public void onCameraAvailable(String cameraId) {
        }

        public void onCameraUnavailable(String cameraId) {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class TorchCallback {
        public void onTorchModeUnavailable(String cameraId) {
        }

        public void onTorchModeChanged(String cameraId, boolean enabled) {
        }
    }

    public static synchronized void throwAsPublicException(Throwable t) throws CameraAccessException {
        int reason;
        if (t instanceof ServiceSpecificException) {
            ServiceSpecificException e = (ServiceSpecificException) t;
            switch (e.errorCode) {
                case 1:
                    throw new SecurityException(e.getMessage(), e);
                case 2:
                case 3:
                    throw new IllegalArgumentException(e.getMessage(), e);
                case 4:
                    reason = 2;
                    break;
                case 5:
                default:
                    reason = 3;
                    break;
                case 6:
                    reason = 1;
                    break;
                case 7:
                    reason = 4;
                    break;
                case 8:
                    reason = 5;
                    break;
                case 9:
                    reason = 1000;
                    break;
            }
            throw new CameraAccessException(reason, e.getMessage(), e);
        } else if (t instanceof DeadObjectException) {
            throw new CameraAccessException(2, "Camera service has died unexpectedly", t);
        } else {
            if (t instanceof RemoteException) {
                throw new UnsupportedOperationException("An unknown RemoteException was thrown which should never happen.", t);
            }
            if (t instanceof RuntimeException) {
                throw ((RuntimeException) t);
            }
        }
    }

    private synchronized boolean supportsCamera2ApiLocked(String cameraId) {
        return supportsCameraApiLocked(cameraId, 2);
    }

    private synchronized boolean supportsCameraApiLocked(String cameraId, int apiVersion) {
        try {
            ICameraService cameraService = CameraManagerGlobal.get().getCameraService();
            if (cameraService == null) {
                return false;
            }
            return cameraService.supportsCameraApi(cameraId, apiVersion);
        } catch (RemoteException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class CameraManagerGlobal extends ICameraServiceListener.Stub implements IBinder.DeathRecipient {
        private static final String CAMERA_SERVICE_BINDER_NAME = "media.camera";
        private static final String TAG = "CameraManagerGlobal";
        private static final CameraManagerGlobal gCameraManager = new CameraManagerGlobal();
        public static final boolean sCameraServiceDisabled = SystemProperties.getBoolean("config.disable_cameraservice", false);
        private ICameraService mCameraService;
        private final boolean DEBUG = false;
        private final int CAMERA_SERVICE_RECONNECT_DELAY_MS = 1000;
        private final ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(1);
        private final ArrayMap<String, Integer> mDeviceStatus = new ArrayMap<>();
        private final ArrayMap<AvailabilityCallback, Executor> mCallbackMap = new ArrayMap<>();
        private Binder mTorchClientBinder = new Binder();
        private final ArrayMap<String, Integer> mTorchStatus = new ArrayMap<>();
        private final ArrayMap<TorchCallback, Executor> mTorchCallbackMap = new ArrayMap<>();
        private final Object mLock = new Object();

        private synchronized CameraManagerGlobal() {
        }

        public static synchronized CameraManagerGlobal get() {
            return gCameraManager;
        }

        @Override // android.hardware.ICameraServiceListener.Stub, android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public synchronized ICameraService getCameraService() {
            ICameraService iCameraService;
            synchronized (this.mLock) {
                connectCameraServiceLocked();
                if (this.mCameraService == null && !sCameraServiceDisabled) {
                    Log.e(TAG, "Camera service is unavailable");
                }
                iCameraService = this.mCameraService;
            }
            return iCameraService;
        }

        private synchronized void connectCameraServiceLocked() {
            if (this.mCameraService != null || sCameraServiceDisabled) {
                return;
            }
            Log.i(TAG, "Connecting to camera service");
            IBinder cameraServiceBinder = ServiceManager.getService(CAMERA_SERVICE_BINDER_NAME);
            if (cameraServiceBinder == null) {
                return;
            }
            try {
                cameraServiceBinder.linkToDeath(this, 0);
                ICameraService cameraService = ICameraService.Stub.asInterface(cameraServiceBinder);
                try {
                    CameraMetadataNative.setupGlobalVendorTagDescriptor();
                } catch (ServiceSpecificException e) {
                    handleRecoverableSetupErrors(e);
                }
                try {
                    CameraStatus[] cameraStatuses = cameraService.addListener(this);
                    for (CameraStatus c : cameraStatuses) {
                        onStatusChangedLocked(c.status, c.cameraId);
                    }
                    this.mCameraService = cameraService;
                } catch (RemoteException e2) {
                } catch (ServiceSpecificException e3) {
                    throw new IllegalStateException("Failed to register a camera service listener", e3);
                }
            } catch (RemoteException e4) {
            }
        }

        public synchronized String[] getCameraIdList() {
            String[] cameraIds;
            synchronized (this.mLock) {
                connectCameraServiceLocked();
                int idCount = 0;
                for (int idCount2 = 0; idCount2 < this.mDeviceStatus.size(); idCount2++) {
                    int status = this.mDeviceStatus.valueAt(idCount2).intValue();
                    if (status != 0 && status != 2) {
                        idCount++;
                    }
                }
                cameraIds = new String[idCount];
                int idCount3 = 0;
                for (int i = 0; i < this.mDeviceStatus.size(); i++) {
                    int status2 = this.mDeviceStatus.valueAt(i).intValue();
                    if (status2 != 0 && status2 != 2) {
                        cameraIds[idCount3] = this.mDeviceStatus.keyAt(i);
                        idCount3++;
                    }
                }
            }
            Arrays.sort(cameraIds, new Comparator<String>() { // from class: android.hardware.camera2.CameraManager.CameraManagerGlobal.1
                @Override // java.util.Comparator
                public int compare(String s1, String s2) {
                    int s1Int;
                    int s2Int;
                    try {
                        s1Int = Integer.parseInt(s1);
                    } catch (NumberFormatException e) {
                        s1Int = -1;
                    }
                    try {
                        s2Int = Integer.parseInt(s2);
                    } catch (NumberFormatException e2) {
                        s2Int = -1;
                    }
                    if (s1Int >= 0 && s2Int >= 0) {
                        return s1Int - s2Int;
                    }
                    if (s1Int >= 0) {
                        return -1;
                    }
                    if (s2Int >= 0) {
                        return 1;
                    }
                    return s1.compareTo(s2);
                }
            });
            return cameraIds;
        }

        public synchronized void setTorchMode(String cameraId, boolean enabled) throws CameraAccessException {
            synchronized (this.mLock) {
                if (cameraId == null) {
                    throw new IllegalArgumentException("cameraId was null");
                }
                ICameraService cameraService = getCameraService();
                if (cameraService == null) {
                    throw new CameraAccessException(2, "Camera service is currently unavailable");
                }
                try {
                    try {
                        cameraService.setTorchMode(cameraId, enabled, this.mTorchClientBinder);
                    } catch (RemoteException e) {
                        throw new CameraAccessException(2, "Camera service is currently unavailable");
                    }
                } catch (ServiceSpecificException e2) {
                    CameraManager.throwAsPublicException(e2);
                }
            }
        }

        private synchronized void handleRecoverableSetupErrors(ServiceSpecificException e) {
            if (e.errorCode == 4) {
                Log.w(TAG, e.getMessage());
                return;
            }
            throw new IllegalStateException(e);
        }

        private synchronized boolean isAvailable(int status) {
            if (status == 1) {
                return true;
            }
            return false;
        }

        private synchronized boolean validStatus(int status) {
            if (status != -2) {
                switch (status) {
                    case 0:
                    case 1:
                    case 2:
                        return true;
                    default:
                        return false;
                }
            }
            return true;
        }

        private synchronized boolean validTorchStatus(int status) {
            switch (status) {
                case 0:
                case 1:
                case 2:
                    return true;
                default:
                    return false;
            }
        }

        private synchronized void postSingleUpdate(final AvailabilityCallback callback, Executor executor, final String id, int status) {
            long ident;
            if (isAvailable(status)) {
                ident = Binder.clearCallingIdentity();
                try {
                    executor.execute(new Runnable() { // from class: android.hardware.camera2.CameraManager.CameraManagerGlobal.2
                        @Override // java.lang.Runnable
                        public void run() {
                            callback.onCameraAvailable(id);
                        }
                    });
                    return;
                } finally {
                }
            }
            ident = Binder.clearCallingIdentity();
            try {
                executor.execute(new Runnable() { // from class: android.hardware.camera2.CameraManager.CameraManagerGlobal.3
                    @Override // java.lang.Runnable
                    public void run() {
                        callback.onCameraUnavailable(id);
                    }
                });
            } finally {
            }
        }

        private synchronized void postSingleTorchUpdate(final TorchCallback callback, Executor executor, final String id, final int status) {
            long ident;
            switch (status) {
                case 1:
                case 2:
                    ident = Binder.clearCallingIdentity();
                    try {
                        executor.execute(new Runnable() { // from class: android.hardware.camera2.-$$Lambda$CameraManager$CameraManagerGlobal$CONvadOBAEkcHSpx8j61v67qRGM
                            @Override // java.lang.Runnable
                            public final void run() {
                                CameraManager.TorchCallback torchCallback = CameraManager.TorchCallback.this;
                                String str = id;
                                int i = status;
                                torchCallback.onTorchModeChanged(str, status == 2);
                            }
                        });
                        return;
                    } finally {
                    }
                default:
                    ident = Binder.clearCallingIdentity();
                    try {
                        executor.execute(new Runnable() { // from class: android.hardware.camera2.-$$Lambda$CameraManager$CameraManagerGlobal$6Ptxoe4wF_VCkE_pml8t66mklao
                            @Override // java.lang.Runnable
                            public final void run() {
                                CameraManager.TorchCallback.this.onTorchModeUnavailable(id);
                            }
                        });
                        return;
                    } finally {
                    }
            }
        }

        private synchronized void updateCallbackLocked(AvailabilityCallback callback, Executor executor) {
            for (int i = 0; i < this.mDeviceStatus.size(); i++) {
                String id = this.mDeviceStatus.keyAt(i);
                Integer status = this.mDeviceStatus.valueAt(i);
                postSingleUpdate(callback, executor, id, status.intValue());
            }
        }

        private synchronized void onStatusChangedLocked(int status, String id) {
            Integer oldStatus;
            if (!validStatus(status)) {
                Log.e(TAG, String.format("Ignoring invalid device %s status 0x%x", id, Integer.valueOf(status)));
                return;
            }
            if (status == 0) {
                oldStatus = this.mDeviceStatus.remove(id);
            } else {
                oldStatus = this.mDeviceStatus.put(id, Integer.valueOf(status));
            }
            if (oldStatus != null && oldStatus.intValue() == status) {
                return;
            }
            if (oldStatus != null && isAvailable(status) == isAvailable(oldStatus.intValue())) {
                return;
            }
            int callbackCount = this.mCallbackMap.size();
            for (int i = 0; i < callbackCount; i++) {
                Executor executor = this.mCallbackMap.valueAt(i);
                AvailabilityCallback callback = this.mCallbackMap.keyAt(i);
                postSingleUpdate(callback, executor, id, status);
            }
        }

        private synchronized void updateTorchCallbackLocked(TorchCallback callback, Executor executor) {
            for (int i = 0; i < this.mTorchStatus.size(); i++) {
                String id = this.mTorchStatus.keyAt(i);
                Integer status = this.mTorchStatus.valueAt(i);
                postSingleTorchUpdate(callback, executor, id, status.intValue());
            }
        }

        private synchronized void onTorchStatusChangedLocked(int status, String id) {
            if (!validTorchStatus(status)) {
                Log.e(TAG, String.format("Ignoring invalid device %s torch status 0x%x", id, Integer.valueOf(status)));
                return;
            }
            Integer oldStatus = this.mTorchStatus.put(id, Integer.valueOf(status));
            if (oldStatus != null && oldStatus.intValue() == status) {
                return;
            }
            int callbackCount = this.mTorchCallbackMap.size();
            for (int i = 0; i < callbackCount; i++) {
                Executor executor = this.mTorchCallbackMap.valueAt(i);
                TorchCallback callback = this.mTorchCallbackMap.keyAt(i);
                postSingleTorchUpdate(callback, executor, id, status);
            }
        }

        public synchronized void registerAvailabilityCallback(AvailabilityCallback callback, Executor executor) {
            synchronized (this.mLock) {
                connectCameraServiceLocked();
                Executor oldExecutor = this.mCallbackMap.put(callback, executor);
                if (oldExecutor == null) {
                    updateCallbackLocked(callback, executor);
                }
                if (this.mCameraService == null) {
                    scheduleCameraServiceReconnectionLocked();
                }
            }
        }

        public synchronized void unregisterAvailabilityCallback(AvailabilityCallback callback) {
            synchronized (this.mLock) {
                this.mCallbackMap.remove(callback);
            }
        }

        public synchronized void registerTorchCallback(TorchCallback callback, Executor executor) {
            synchronized (this.mLock) {
                connectCameraServiceLocked();
                Executor oldExecutor = this.mTorchCallbackMap.put(callback, executor);
                if (oldExecutor == null) {
                    updateTorchCallbackLocked(callback, executor);
                }
                if (this.mCameraService == null) {
                    scheduleCameraServiceReconnectionLocked();
                }
            }
        }

        public synchronized void unregisterTorchCallback(TorchCallback callback) {
            synchronized (this.mLock) {
                this.mTorchCallbackMap.remove(callback);
            }
        }

        @Override // android.hardware.ICameraServiceListener
        public synchronized void onStatusChanged(int status, String cameraId) throws RemoteException {
            synchronized (this.mLock) {
                onStatusChangedLocked(status, cameraId);
            }
        }

        @Override // android.hardware.ICameraServiceListener
        public synchronized void onTorchStatusChanged(int status, String cameraId) throws RemoteException {
            synchronized (this.mLock) {
                onTorchStatusChangedLocked(status, cameraId);
            }
        }

        private synchronized void scheduleCameraServiceReconnectionLocked() {
            if (this.mCallbackMap.isEmpty() && this.mTorchCallbackMap.isEmpty()) {
                return;
            }
            try {
                this.mScheduler.schedule(new Runnable() { // from class: android.hardware.camera2.-$$Lambda$CameraManager$CameraManagerGlobal$w1y8myi6vgxAcTEs8WArI-NN3R0
                    @Override // java.lang.Runnable
                    public final void run() {
                        CameraManager.CameraManagerGlobal.lambda$scheduleCameraServiceReconnectionLocked$2(CameraManager.CameraManagerGlobal.this);
                    }
                }, 1000L, TimeUnit.MILLISECONDS);
            } catch (RejectedExecutionException e) {
                Log.e(TAG, "Failed to schedule camera service re-connect: " + e);
            }
        }

        public static /* synthetic */ void lambda$scheduleCameraServiceReconnectionLocked$2(CameraManagerGlobal cameraManagerGlobal) {
            ICameraService cameraService = cameraManagerGlobal.getCameraService();
            if (cameraService == null) {
                synchronized (cameraManagerGlobal.mLock) {
                    cameraManagerGlobal.scheduleCameraServiceReconnectionLocked();
                }
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (this.mLock) {
                if (this.mCameraService == null) {
                    return;
                }
                this.mCameraService = null;
                for (int i = 0; i < this.mDeviceStatus.size(); i++) {
                    String cameraId = this.mDeviceStatus.keyAt(i);
                    onStatusChangedLocked(0, cameraId);
                }
                for (int i2 = 0; i2 < this.mTorchStatus.size(); i2++) {
                    String cameraId2 = this.mTorchStatus.keyAt(i2);
                    onTorchStatusChangedLocked(0, cameraId2);
                }
                scheduleCameraServiceReconnectionLocked();
            }
        }
    }
}
