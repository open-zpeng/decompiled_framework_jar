package android.hardware.camera2.legacy;

import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CaptureResultExtras;
import android.hardware.camera2.impl.PhysicalCaptureResultInfo;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.system.OsConstants;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import java.util.List;

/* loaded from: classes.dex */
public class CameraDeviceUserShim implements ICameraDeviceUser {
    private static final boolean DEBUG = false;
    private static final int OPEN_CAMERA_TIMEOUT_MS = 5000;
    private static final String TAG = "CameraDeviceUserShim";
    private final CameraCallbackThread mCameraCallbacks;
    private final CameraCharacteristics mCameraCharacteristics;
    private final CameraLooper mCameraInit;
    private final LegacyCameraDevice mLegacyDevice;
    private final Object mConfigureLock = new Object();
    private boolean mConfiguring = false;
    private final SparseArray<Surface> mSurfaces = new SparseArray<>();
    private int mSurfaceIdCounter = 0;

    protected CameraDeviceUserShim(int cameraId, LegacyCameraDevice legacyCamera, CameraCharacteristics characteristics, CameraLooper cameraInit, CameraCallbackThread cameraCallbacks) {
        this.mLegacyDevice = legacyCamera;
        this.mCameraCharacteristics = characteristics;
        this.mCameraInit = cameraInit;
        this.mCameraCallbacks = cameraCallbacks;
    }

    private static int translateErrorsFromCamera1(int errorCode) {
        if (errorCode == (-OsConstants.EACCES)) {
            return 1;
        }
        return errorCode;
    }

    /* loaded from: classes.dex */
    private static class CameraLooper implements Runnable, AutoCloseable {
        private final int mCameraId;
        private volatile int mInitErrors;
        private Looper mLooper;
        private final Camera mCamera = Camera.openUninitialized();
        private final ConditionVariable mStartDone = new ConditionVariable();
        private final Thread mThread = new Thread(this);

        public CameraLooper(int cameraId) {
            this.mCameraId = cameraId;
            this.mThread.start();
        }

        public Camera getCamera() {
            return this.mCamera;
        }

        @Override // java.lang.Runnable
        public void run() {
            Looper.prepare();
            this.mLooper = Looper.myLooper();
            this.mInitErrors = this.mCamera.cameraInitUnspecified(this.mCameraId);
            this.mStartDone.open();
            Looper.loop();
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            Looper looper = this.mLooper;
            if (looper == null) {
                return;
            }
            looper.quitSafely();
            try {
                this.mThread.join();
                this.mLooper = null;
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }

        public int waitForOpen(int timeoutMs) {
            if (!this.mStartDone.block(timeoutMs)) {
                Log.e(CameraDeviceUserShim.TAG, "waitForOpen - Camera failed to open after timeout of 5000 ms");
                try {
                    this.mCamera.release();
                } catch (RuntimeException e) {
                    Log.e(CameraDeviceUserShim.TAG, "connectBinderShim - Failed to release camera after timeout ", e);
                }
                throw new ServiceSpecificException(10);
            }
            return this.mInitErrors;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CameraCallbackThread implements ICameraDeviceCallbacks {
        private static final int CAMERA_ERROR = 0;
        private static final int CAMERA_IDLE = 1;
        private static final int CAPTURE_STARTED = 2;
        private static final int PREPARED = 4;
        private static final int REPEATING_REQUEST_ERROR = 5;
        private static final int REQUEST_QUEUE_EMPTY = 6;
        private static final int RESULT_RECEIVED = 3;
        private final ICameraDeviceCallbacks mCallbacks;
        private Handler mHandler;
        private final HandlerThread mHandlerThread = new HandlerThread("LegacyCameraCallback");

        public CameraCallbackThread(ICameraDeviceCallbacks callbacks) {
            this.mCallbacks = callbacks;
            this.mHandlerThread.start();
        }

        public void close() {
            this.mHandlerThread.quitSafely();
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onDeviceError(int errorCode, CaptureResultExtras resultExtras) {
            Message msg = getHandler().obtainMessage(0, errorCode, 0, resultExtras);
            getHandler().sendMessage(msg);
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onDeviceIdle() {
            Message msg = getHandler().obtainMessage(1);
            getHandler().sendMessage(msg);
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onCaptureStarted(CaptureResultExtras resultExtras, long timestamp) {
            Message msg = getHandler().obtainMessage(2, (int) (timestamp & 4294967295L), (int) (4294967295L & (timestamp >> 32)), resultExtras);
            getHandler().sendMessage(msg);
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onResultReceived(CameraMetadataNative result, CaptureResultExtras resultExtras, PhysicalCaptureResultInfo[] physicalResults) {
            Object[] resultArray = {result, resultExtras};
            Message msg = getHandler().obtainMessage(3, resultArray);
            getHandler().sendMessage(msg);
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onPrepared(int streamId) {
            Message msg = getHandler().obtainMessage(4, streamId, 0);
            getHandler().sendMessage(msg);
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onRepeatingRequestError(long lastFrameNumber, int repeatingRequestId) {
            Object[] objArray = {Long.valueOf(lastFrameNumber), Integer.valueOf(repeatingRequestId)};
            Message msg = getHandler().obtainMessage(5, objArray);
            getHandler().sendMessage(msg);
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public void onRequestQueueEmpty() {
            Message msg = getHandler().obtainMessage(6, 0, 0);
            getHandler().sendMessage(msg);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        private Handler getHandler() {
            if (this.mHandler == null) {
                this.mHandler = new CallbackHandler(this.mHandlerThread.getLooper());
            }
            return this.mHandler;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public class CallbackHandler extends Handler {
            public CallbackHandler(Looper l) {
                super(l);
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                try {
                    switch (msg.what) {
                        case 0:
                            int errorCode = msg.arg1;
                            CaptureResultExtras resultExtras = (CaptureResultExtras) msg.obj;
                            CameraCallbackThread.this.mCallbacks.onDeviceError(errorCode, resultExtras);
                            return;
                        case 1:
                            CameraCallbackThread.this.mCallbacks.onDeviceIdle();
                            return;
                        case 2:
                            long timestamp = msg.arg2 & 4294967295L;
                            long timestamp2 = (timestamp << 32) | (4294967295L & msg.arg1);
                            CaptureResultExtras resultExtras2 = (CaptureResultExtras) msg.obj;
                            CameraCallbackThread.this.mCallbacks.onCaptureStarted(resultExtras2, timestamp2);
                            return;
                        case 3:
                            Object[] resultArray = (Object[]) msg.obj;
                            CameraMetadataNative result = (CameraMetadataNative) resultArray[0];
                            CaptureResultExtras resultExtras3 = (CaptureResultExtras) resultArray[1];
                            CameraCallbackThread.this.mCallbacks.onResultReceived(result, resultExtras3, new PhysicalCaptureResultInfo[0]);
                            return;
                        case 4:
                            int streamId = msg.arg1;
                            CameraCallbackThread.this.mCallbacks.onPrepared(streamId);
                            return;
                        case 5:
                            Object[] objArray = (Object[]) msg.obj;
                            long lastFrameNumber = ((Long) objArray[0]).longValue();
                            int repeatingRequestId = ((Integer) objArray[1]).intValue();
                            CameraCallbackThread.this.mCallbacks.onRepeatingRequestError(lastFrameNumber, repeatingRequestId);
                            return;
                        case 6:
                            CameraCallbackThread.this.mCallbacks.onRequestQueueEmpty();
                            return;
                        default:
                            throw new IllegalArgumentException("Unknown callback message " + msg.what);
                    }
                } catch (RemoteException e) {
                    throw new IllegalStateException("Received remote exception during camera callback " + msg.what, e);
                }
            }
        }
    }

    public static CameraDeviceUserShim connectBinderShim(ICameraDeviceCallbacks callbacks, int cameraId, Size displaySize) {
        CameraLooper init = new CameraLooper(cameraId);
        CameraCallbackThread threadCallbacks = new CameraCallbackThread(callbacks);
        int initErrors = init.waitForOpen(5000);
        Camera legacyCamera = init.getCamera();
        LegacyExceptionUtils.throwOnServiceError(initErrors);
        legacyCamera.disableShutterSound();
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        try {
            Camera.Parameters legacyParameters = legacyCamera.getParameters();
            CameraCharacteristics characteristics = LegacyMetadataMapper.createCharacteristics(legacyParameters, info, cameraId, displaySize);
            LegacyCameraDevice device = new LegacyCameraDevice(cameraId, legacyCamera, characteristics, threadCallbacks);
            return new CameraDeviceUserShim(cameraId, device, characteristics, init, threadCallbacks);
        } catch (RuntimeException e) {
            throw new ServiceSpecificException(10, "Unable to get initial parameters: " + e.getMessage());
        }
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public void disconnect() {
        if (this.mLegacyDevice.isClosed()) {
            Log.w(TAG, "Cannot disconnect, device has already been closed.");
        }
        try {
            this.mLegacyDevice.close();
        } finally {
            this.mCameraInit.close();
            this.mCameraCallbacks.close();
        }
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public SubmitInfo submitRequest(CaptureRequest request, boolean streaming) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot submit request, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot submit request, device has been closed.");
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot submit request, configuration change in progress.");
                throw new ServiceSpecificException(10, "Cannot submit request, configuration change in progress.");
            }
        }
        return this.mLegacyDevice.submitRequest(request, streaming);
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public SubmitInfo submitRequestList(CaptureRequest[] request, boolean streaming) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot submit request list, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot submit request list, device has been closed.");
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot submit request, configuration change in progress.");
                throw new ServiceSpecificException(10, "Cannot submit request, configuration change in progress.");
            }
        }
        return this.mLegacyDevice.submitRequestList(request, streaming);
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public long cancelRequest(int requestId) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot cancel request, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot cancel request, device has been closed.");
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot cancel request, configuration change in progress.");
                throw new ServiceSpecificException(10, "Cannot cancel request, configuration change in progress.");
            }
        }
        return this.mLegacyDevice.cancelRequest(requestId);
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public boolean isSessionConfigurationSupported(SessionConfiguration sessionConfig) {
        if (sessionConfig.getSessionType() != 0) {
            Log.e(TAG, "Session type: " + sessionConfig.getSessionType() + " is different from  regular. Legacy devices support only regular session types!");
            return false;
        } else if (sessionConfig.getInputConfiguration() != null) {
            Log.e(TAG, "Input configuration present, legacy devices do not support this feature!");
            return false;
        } else {
            List<OutputConfiguration> outputConfigs = sessionConfig.getOutputConfigurations();
            if (outputConfigs.isEmpty()) {
                Log.e(TAG, "Empty output configuration list!");
                return false;
            }
            SparseArray<Surface> surfaces = new SparseArray<>(outputConfigs.size());
            int idx = 0;
            for (OutputConfiguration outputConfig : outputConfigs) {
                List<Surface> surfaceList = outputConfig.getSurfaces();
                if (surfaceList.isEmpty() || surfaceList.size() > 1) {
                    Log.e(TAG, "Legacy devices do not support deferred or shared surfaces!");
                    return false;
                }
                surfaces.put(idx, outputConfig.getSurface());
                idx++;
            }
            int ret = this.mLegacyDevice.configureOutputs(surfaces, true);
            return ret == 0;
        }
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public void beginConfigure() {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot begin configure, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot begin configure, device has been closed.");
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot begin configure, configuration change already in progress.");
                throw new ServiceSpecificException(10, "Cannot begin configure, configuration change already in progress.");
            }
            this.mConfiguring = true;
        }
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public void endConfigure(int operatingMode, CameraMetadataNative sessionParams) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot end configure, device has been closed.");
            synchronized (this.mConfigureLock) {
                this.mConfiguring = false;
            }
            throw new ServiceSpecificException(4, "Cannot end configure, device has been closed.");
        } else if (operatingMode != 0) {
            Log.e(TAG, "LEGACY devices do not support this operating mode");
            synchronized (this.mConfigureLock) {
                this.mConfiguring = false;
            }
            throw new ServiceSpecificException(3, "LEGACY devices do not support this operating mode");
        } else {
            SparseArray<Surface> surfaces = null;
            synchronized (this.mConfigureLock) {
                if (!this.mConfiguring) {
                    Log.e(TAG, "Cannot end configure, no configuration change in progress.");
                    throw new ServiceSpecificException(10, "Cannot end configure, no configuration change in progress.");
                }
                if (this.mSurfaces != null) {
                    surfaces = this.mSurfaces.m38clone();
                }
                this.mConfiguring = false;
            }
            this.mLegacyDevice.configureOutputs(surfaces);
        }
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public void deleteStream(int streamId) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot delete stream, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot delete stream, device has been closed.");
        }
        synchronized (this.mConfigureLock) {
            if (!this.mConfiguring) {
                Log.e(TAG, "Cannot delete stream, no configuration change in progress.");
                throw new ServiceSpecificException(10, "Cannot delete stream, no configuration change in progress.");
            }
            int index = this.mSurfaces.indexOfKey(streamId);
            if (index < 0) {
                String err = "Cannot delete stream, stream id " + streamId + " doesn't exist.";
                Log.e(TAG, err);
                throw new ServiceSpecificException(3, err);
            }
            this.mSurfaces.removeAt(index);
        }
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public int createStream(OutputConfiguration outputConfiguration) {
        int id;
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot create stream, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot create stream, device has been closed.");
        }
        synchronized (this.mConfigureLock) {
            if (!this.mConfiguring) {
                Log.e(TAG, "Cannot create stream, beginConfigure hasn't been called yet.");
                throw new ServiceSpecificException(10, "Cannot create stream, beginConfigure hasn't been called yet.");
            } else if (outputConfiguration.getRotation() != 0) {
                Log.e(TAG, "Cannot create stream, stream rotation is not supported.");
                throw new ServiceSpecificException(3, "Cannot create stream, stream rotation is not supported.");
            } else {
                id = this.mSurfaceIdCounter + 1;
                this.mSurfaceIdCounter = id;
                this.mSurfaces.put(id, outputConfiguration.getSurface());
            }
        }
        return id;
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public void finalizeOutputConfigurations(int steamId, OutputConfiguration config) {
        Log.e(TAG, "Finalizing output configuration is not supported on legacy devices");
        throw new ServiceSpecificException(10, "Finalizing output configuration is not supported on legacy devices");
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public int createInputStream(int width, int height, int format) {
        Log.e(TAG, "Creating input stream is not supported on legacy devices");
        throw new ServiceSpecificException(10, "Creating input stream is not supported on legacy devices");
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public Surface getInputSurface() {
        Log.e(TAG, "Getting input surface is not supported on legacy devices");
        throw new ServiceSpecificException(10, "Getting input surface is not supported on legacy devices");
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public CameraMetadataNative createDefaultRequest(int templateId) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot create default request, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot create default request, device has been closed.");
        }
        try {
            CameraMetadataNative template = LegacyMetadataMapper.createRequestTemplate(this.mCameraCharacteristics, templateId);
            return template;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "createDefaultRequest - invalid templateId specified");
            throw new ServiceSpecificException(3, "createDefaultRequest - invalid templateId specified");
        }
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public CameraMetadataNative getCameraInfo() {
        Log.e(TAG, "getCameraInfo unimplemented.");
        return null;
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public void updateOutputConfiguration(int streamId, OutputConfiguration config) {
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public void waitUntilIdle() throws RemoteException {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot wait until idle, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot wait until idle, device has been closed.");
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot wait until idle, configuration change in progress.");
                throw new ServiceSpecificException(10, "Cannot wait until idle, configuration change in progress.");
            }
        }
        this.mLegacyDevice.waitUntilIdle();
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public long flush() {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot flush, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot flush, device has been closed.");
        }
        synchronized (this.mConfigureLock) {
            if (this.mConfiguring) {
                Log.e(TAG, "Cannot flush, configuration change in progress.");
                throw new ServiceSpecificException(10, "Cannot flush, configuration change in progress.");
            }
        }
        return this.mLegacyDevice.flush();
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public void prepare(int streamId) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot prepare stream, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot prepare stream, device has been closed.");
        } else {
            this.mCameraCallbacks.onPrepared(streamId);
        }
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public void prepare2(int maxCount, int streamId) {
        prepare(streamId);
    }

    @Override // android.hardware.camera2.ICameraDeviceUser
    public void tearDown(int streamId) {
        if (this.mLegacyDevice.isClosed()) {
            Log.e(TAG, "Cannot tear down stream, device has been closed.");
            throw new ServiceSpecificException(4, "Cannot tear down stream, device has been closed.");
        }
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return null;
    }
}
