package android.hardware.camera2.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.impl.CameraDeviceImpl;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.SubmitInfo;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public class CameraDeviceImpl extends CameraDevice implements IBinder.DeathRecipient {
    private static final long NANO_PER_SECOND = 1000000000;
    private static final int REQUEST_ID_NONE = -1;
    private final String TAG;
    private final int mAppTargetSdkVersion;
    private final String mCameraId;
    private final CameraCharacteristics mCharacteristics;
    private CameraCaptureSessionCore mCurrentSession;
    private final CameraDevice.StateCallback mDeviceCallback;
    private final Executor mDeviceExecutor;
    private ICameraDeviceUserWrapper mRemoteDevice;
    private volatile StateCallbackKK mSessionStateCallback;
    private final int mTotalPartialCount;
    private final boolean DEBUG = false;
    final Object mInterfaceLock = new Object();
    private final CameraDeviceCallbacks mCallbacks = new CameraDeviceCallbacks();
    private final AtomicBoolean mClosing = new AtomicBoolean();
    private boolean mInError = false;
    private boolean mIdle = true;
    private final SparseArray<CaptureCallbackHolder> mCaptureCallbackMap = new SparseArray<>();
    private int mRepeatingRequestId = -1;
    private AbstractMap.SimpleEntry<Integer, InputConfiguration> mConfiguredInput = new AbstractMap.SimpleEntry<>(-1, null);
    private final SparseArray<OutputConfiguration> mConfiguredOutputs = new SparseArray<>();
    private final List<RequestLastFrameNumbersHolder> mRequestLastFrameNumbersList = new ArrayList();
    private final FrameNumberTracker mFrameNumberTracker = new FrameNumberTracker();
    private int mNextSessionId = 0;
    private final Runnable mCallOnOpened = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onOpened(CameraDeviceImpl.this);
                }
                CameraDeviceImpl.this.mDeviceCallback.onOpened(CameraDeviceImpl.this);
            }
        }
    };
    private final Runnable mCallOnUnconfigured = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.2
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onUnconfigured(CameraDeviceImpl.this);
                }
            }
        }
    };
    private final Runnable mCallOnActive = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.3
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onActive(CameraDeviceImpl.this);
                }
            }
        }
    };
    private final Runnable mCallOnBusy = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.4
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onBusy(CameraDeviceImpl.this);
                }
            }
        }
    };
    private final Runnable mCallOnClosed = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.5
        private boolean mClosedOnce = false;

        @Override // java.lang.Runnable
        public void run() {
            StateCallbackKK sessionCallback;
            if (this.mClosedOnce) {
                throw new AssertionError("Don't post #onClosed more than once");
            }
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onClosed(CameraDeviceImpl.this);
            }
            CameraDeviceImpl.this.mDeviceCallback.onClosed(CameraDeviceImpl.this);
            this.mClosedOnce = true;
        }
    };
    private final Runnable mCallOnIdle = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.6
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onIdle(CameraDeviceImpl.this);
                }
            }
        }
    };
    private final Runnable mCallOnDisconnected = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.7
        @Override // java.lang.Runnable
        public void run() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                StateCallbackKK sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
                if (sessionCallback != null) {
                    sessionCallback.onDisconnected(CameraDeviceImpl.this);
                }
                CameraDeviceImpl.this.mDeviceCallback.onDisconnected(CameraDeviceImpl.this);
            }
        }
    };

    /* loaded from: classes.dex */
    public interface CaptureCallback {
        public static final int NO_FRAMES_CAPTURED = -1;

        synchronized void onCaptureBufferLost(CameraDevice cameraDevice, CaptureRequest captureRequest, Surface surface, long j);

        synchronized void onCaptureCompleted(CameraDevice cameraDevice, CaptureRequest captureRequest, TotalCaptureResult totalCaptureResult);

        synchronized void onCaptureFailed(CameraDevice cameraDevice, CaptureRequest captureRequest, CaptureFailure captureFailure);

        synchronized void onCapturePartial(CameraDevice cameraDevice, CaptureRequest captureRequest, CaptureResult captureResult);

        synchronized void onCaptureProgressed(CameraDevice cameraDevice, CaptureRequest captureRequest, CaptureResult captureResult);

        synchronized void onCaptureSequenceAborted(CameraDevice cameraDevice, int i);

        synchronized void onCaptureSequenceCompleted(CameraDevice cameraDevice, int i, long j);

        synchronized void onCaptureStarted(CameraDevice cameraDevice, CaptureRequest captureRequest, long j, long j2);
    }

    public synchronized CameraDeviceImpl(String cameraId, CameraDevice.StateCallback callback, Executor executor, CameraCharacteristics characteristics, int appTargetSdkVersion) {
        if (cameraId == null || callback == null || executor == null || characteristics == null) {
            throw new IllegalArgumentException("Null argument given");
        }
        this.mCameraId = cameraId;
        this.mDeviceCallback = callback;
        this.mDeviceExecutor = executor;
        this.mCharacteristics = characteristics;
        this.mAppTargetSdkVersion = appTargetSdkVersion;
        String tag = String.format("CameraDevice-JV-%s", this.mCameraId);
        this.TAG = tag.length() > 23 ? tag.substring(0, 23) : tag;
        Integer partialCount = (Integer) this.mCharacteristics.get(CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT);
        if (partialCount == null) {
            this.mTotalPartialCount = 1;
        } else {
            this.mTotalPartialCount = partialCount.intValue();
        }
    }

    public synchronized CameraDeviceCallbacks getCallbacks() {
        return this.mCallbacks;
    }

    public synchronized void setRemoteDevice(ICameraDeviceUser remoteDevice) throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            if (this.mInError) {
                return;
            }
            this.mRemoteDevice = new ICameraDeviceUserWrapper(remoteDevice);
            IBinder remoteDeviceBinder = remoteDevice.asBinder();
            if (remoteDeviceBinder != null) {
                try {
                    remoteDeviceBinder.linkToDeath(this, 0);
                } catch (RemoteException e) {
                    this.mDeviceExecutor.execute(this.mCallOnDisconnected);
                    throw new CameraAccessException(2, "The camera device has encountered a serious error");
                }
            }
            this.mDeviceExecutor.execute(this.mCallOnOpened);
            this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
        }
    }

    public synchronized void setRemoteFailure(ServiceSpecificException failure) {
        int failureCode = 4;
        boolean failureIsError = true;
        int i = failure.errorCode;
        if (i == 4) {
            failureIsError = false;
        } else if (i != 10) {
            switch (i) {
                case 6:
                    failureCode = 3;
                    break;
                case 7:
                    failureCode = 1;
                    break;
                case 8:
                    failureCode = 2;
                    break;
                default:
                    String str = this.TAG;
                    Log.e(str, "Unexpected failure in opening camera device: " + failure.errorCode + failure.getMessage());
                    break;
            }
        } else {
            failureCode = 4;
        }
        final int code = failureCode;
        final boolean isError = failureIsError;
        synchronized (this.mInterfaceLock) {
            this.mInError = true;
            this.mDeviceExecutor.execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.8
                @Override // java.lang.Runnable
                public void run() {
                    if (isError) {
                        CameraDeviceImpl.this.mDeviceCallback.onError(CameraDeviceImpl.this, code);
                    } else {
                        CameraDeviceImpl.this.mDeviceCallback.onDisconnected(CameraDeviceImpl.this);
                    }
                }
            });
        }
    }

    @Override // android.hardware.camera2.CameraDevice
    public String getId() {
        return this.mCameraId;
    }

    public synchronized void configureOutputs(List<Surface> outputs) throws CameraAccessException {
        ArrayList<OutputConfiguration> outputConfigs = new ArrayList<>(outputs.size());
        for (Surface s : outputs) {
            outputConfigs.add(new OutputConfiguration(s));
        }
        configureStreamsChecked(null, outputConfigs, 0, null);
    }

    public synchronized boolean configureStreamsChecked(InputConfiguration inputConfig, List<OutputConfiguration> outputs, int operatingMode, CaptureRequest sessionParams) throws CameraAccessException {
        if (outputs == null) {
            outputs = new ArrayList();
        }
        if (outputs.size() != 0 || inputConfig == null) {
            checkInputConfiguration(inputConfig);
            synchronized (this.mInterfaceLock) {
                checkIfCameraClosedOrInError();
                HashSet<OutputConfiguration> addSet = new HashSet<>(outputs);
                List<Integer> deleteList = new ArrayList<>();
                for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                    int streamId = this.mConfiguredOutputs.keyAt(i);
                    OutputConfiguration outConfig = this.mConfiguredOutputs.valueAt(i);
                    if (outputs.contains(outConfig) && !outConfig.isDeferredConfiguration()) {
                        addSet.remove(outConfig);
                    }
                    deleteList.add(Integer.valueOf(streamId));
                }
                this.mDeviceExecutor.execute(this.mCallOnBusy);
                stopRepeating();
                try {
                    try {
                        waitUntilIdle();
                        this.mRemoteDevice.beginConfigure();
                        InputConfiguration currentInputConfig = this.mConfiguredInput.getValue();
                        if (inputConfig != currentInputConfig && (inputConfig == null || !inputConfig.equals(currentInputConfig))) {
                            if (currentInputConfig != null) {
                                this.mRemoteDevice.deleteStream(this.mConfiguredInput.getKey().intValue());
                                this.mConfiguredInput = new AbstractMap.SimpleEntry<>(-1, null);
                            }
                            if (inputConfig != null) {
                                this.mConfiguredInput = new AbstractMap.SimpleEntry<>(Integer.valueOf(this.mRemoteDevice.createInputStream(inputConfig.getWidth(), inputConfig.getHeight(), inputConfig.getFormat())), inputConfig);
                            }
                        }
                        for (Integer streamId2 : deleteList) {
                            this.mRemoteDevice.deleteStream(streamId2.intValue());
                            this.mConfiguredOutputs.delete(streamId2.intValue());
                        }
                        for (OutputConfiguration outConfig2 : outputs) {
                            if (addSet.contains(outConfig2)) {
                                this.mConfiguredOutputs.put(this.mRemoteDevice.createStream(outConfig2), outConfig2);
                            }
                        }
                        if (sessionParams != null) {
                            this.mRemoteDevice.endConfigure(operatingMode, sessionParams.getNativeCopy());
                        } else {
                            this.mRemoteDevice.endConfigure(operatingMode, null);
                        }
                        if (1 == 0 || outputs.size() <= 0) {
                            this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
                        } else {
                            this.mDeviceExecutor.execute(this.mCallOnIdle);
                        }
                    } catch (IllegalArgumentException e) {
                        Log.w(this.TAG, "Stream configuration failed due to: " + e.getMessage());
                        if (0 == 0 || outputs.size() <= 0) {
                            this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
                        } else {
                            this.mDeviceExecutor.execute(this.mCallOnIdle);
                        }
                        return false;
                    }
                } catch (CameraAccessException e2) {
                    if (e2.getReason() == 4) {
                        throw new IllegalStateException("The camera is currently busy. You must wait until the previous operation completes.", e2);
                    }
                    throw e2;
                }
            }
            return true;
        }
        throw new IllegalArgumentException("cannot configure an input stream without any output streams");
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createCaptureSession(List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        List<OutputConfiguration> outConfigurations = new ArrayList<>(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(null, outConfigurations, callback, checkAndWrapHandler(handler), 0, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createCaptureSessionByOutputConfigurations(List<OutputConfiguration> outputConfigurations, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        List<OutputConfiguration> currentOutputs = new ArrayList<>(outputConfigurations);
        createCaptureSessionInternal(null, currentOutputs, callback, checkAndWrapHandler(handler), 0, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createReprocessableCaptureSession(InputConfiguration inputConfig, List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (inputConfig == null) {
            throw new IllegalArgumentException("inputConfig cannot be null when creating a reprocessable capture session");
        }
        List<OutputConfiguration> outConfigurations = new ArrayList<>(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(inputConfig, outConfigurations, callback, checkAndWrapHandler(handler), 0, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createReprocessableCaptureSessionByConfigurations(InputConfiguration inputConfig, List<OutputConfiguration> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (inputConfig == null) {
            throw new IllegalArgumentException("inputConfig cannot be null when creating a reprocessable capture session");
        }
        if (outputs == null) {
            throw new IllegalArgumentException("Output configurations cannot be null when creating a reprocessable capture session");
        }
        List<OutputConfiguration> currentOutputs = new ArrayList<>();
        for (OutputConfiguration output : outputs) {
            currentOutputs.add(new OutputConfiguration(output));
        }
        createCaptureSessionInternal(inputConfig, currentOutputs, callback, checkAndWrapHandler(handler), 0, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createConstrainedHighSpeedCaptureSession(List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (outputs == null || outputs.size() == 0 || outputs.size() > 2) {
            throw new IllegalArgumentException("Output surface list must not be null and the size must be no more than 2");
        }
        List<OutputConfiguration> outConfigurations = new ArrayList<>(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(null, outConfigurations, callback, checkAndWrapHandler(handler), 1, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createCustomCaptureSession(InputConfiguration inputConfig, List<OutputConfiguration> outputs, int operatingMode, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        List<OutputConfiguration> currentOutputs = new ArrayList<>();
        for (OutputConfiguration output : outputs) {
            currentOutputs.add(new OutputConfiguration(output));
        }
        createCaptureSessionInternal(inputConfig, currentOutputs, callback, checkAndWrapHandler(handler), operatingMode, null);
    }

    @Override // android.hardware.camera2.CameraDevice
    public void createCaptureSession(SessionConfiguration config) throws CameraAccessException {
        if (config == null) {
            throw new IllegalArgumentException("Invalid session configuration");
        }
        List<OutputConfiguration> outputConfigs = config.getOutputConfigurations();
        if (outputConfigs == null) {
            throw new IllegalArgumentException("Invalid output configurations");
        }
        if (config.getExecutor() == null) {
            throw new IllegalArgumentException("Invalid executor");
        }
        createCaptureSessionInternal(config.getInputConfiguration(), outputConfigs, config.getStateCallback(), config.getExecutor(), config.getSessionType(), config.getSessionParameters());
    }

    private synchronized void createCaptureSessionInternal(InputConfiguration inputConfig, List<OutputConfiguration> outputConfigurations, CameraCaptureSession.StateCallback callback, Executor executor, int operatingMode, CaptureRequest sessionParams) throws CameraAccessException {
        Surface input;
        boolean configureSuccess;
        CameraAccessException pendingException;
        CameraCaptureSessionCore newSession;
        synchronized (this.mInterfaceLock) {
            try {
                try {
                    checkIfCameraClosedOrInError();
                    boolean isConstrainedHighSpeed = operatingMode == 1;
                    if (isConstrainedHighSpeed && inputConfig != null) {
                        throw new IllegalArgumentException("Constrained high speed session doesn't support input configuration yet.");
                    }
                    if (this.mCurrentSession != null) {
                        this.mCurrentSession.replaceSessionClose();
                    }
                    Surface input2 = null;
                    try {
                        boolean configureSuccess2 = configureStreamsChecked(inputConfig, outputConfigurations, operatingMode, sessionParams);
                        if (configureSuccess2 && inputConfig != null) {
                            input2 = this.mRemoteDevice.getInputSurface();
                        }
                        configureSuccess = configureSuccess2;
                        pendingException = null;
                        input = input2;
                    } catch (CameraAccessException e) {
                        input = null;
                        configureSuccess = false;
                        pendingException = e;
                    }
                    if (isConstrainedHighSpeed) {
                        ArrayList<Surface> surfaces = new ArrayList<>(outputConfigurations.size());
                        for (OutputConfiguration outConfig : outputConfigurations) {
                            surfaces.add(outConfig.getSurface());
                        }
                        StreamConfigurationMap config = (StreamConfigurationMap) getCharacteristics().get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        SurfaceUtils.checkConstrainedHighSpeedSurfaces(surfaces, null, config);
                        int i = this.mNextSessionId;
                        this.mNextSessionId = i + 1;
                        newSession = new CameraConstrainedHighSpeedCaptureSessionImpl(i, callback, executor, this, this.mDeviceExecutor, configureSuccess, this.mCharacteristics);
                    } else {
                        int i2 = this.mNextSessionId;
                        this.mNextSessionId = i2 + 1;
                        newSession = new CameraCaptureSessionImpl(i2, input, callback, executor, this, this.mDeviceExecutor, configureSuccess);
                    }
                    this.mCurrentSession = newSession;
                    if (pendingException != null) {
                        throw pendingException;
                    }
                    this.mSessionStateCallback = this.mCurrentSession.getDeviceStateCallback();
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public synchronized void setSessionListener(StateCallbackKK sessionCallback) {
        synchronized (this.mInterfaceLock) {
            this.mSessionStateCallback = sessionCallback;
        }
    }

    private synchronized void overrideEnableZsl(CameraMetadataNative request, boolean newValue) {
        Boolean enableZsl = (Boolean) request.get(CaptureRequest.CONTROL_ENABLE_ZSL);
        if (enableZsl == null) {
            return;
        }
        request.set((CaptureRequest.Key<CaptureRequest.Key<Boolean>>) CaptureRequest.CONTROL_ENABLE_ZSL, (CaptureRequest.Key<Boolean>) Boolean.valueOf(newValue));
    }

    @Override // android.hardware.camera2.CameraDevice
    public CaptureRequest.Builder createCaptureRequest(int templateType, Set<String> physicalCameraIdSet) throws CameraAccessException {
        CaptureRequest.Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            for (String physicalId : physicalCameraIdSet) {
                if (physicalId == getId()) {
                    throw new IllegalStateException("Physical id matches the logical id!");
                }
            }
            CameraMetadataNative templatedRequest = this.mRemoteDevice.createDefaultRequest(templateType);
            if (this.mAppTargetSdkVersion < 26 || templateType != 2) {
                overrideEnableZsl(templatedRequest, false);
            }
            builder = new CaptureRequest.Builder(templatedRequest, false, -1, getId(), physicalCameraIdSet);
        }
        return builder;
    }

    @Override // android.hardware.camera2.CameraDevice
    public CaptureRequest.Builder createCaptureRequest(int templateType) throws CameraAccessException {
        CaptureRequest.Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            CameraMetadataNative templatedRequest = this.mRemoteDevice.createDefaultRequest(templateType);
            if (this.mAppTargetSdkVersion < 26 || templateType != 2) {
                overrideEnableZsl(templatedRequest, false);
            }
            builder = new CaptureRequest.Builder(templatedRequest, false, -1, getId(), null);
        }
        return builder;
    }

    @Override // android.hardware.camera2.CameraDevice
    public CaptureRequest.Builder createReprocessCaptureRequest(TotalCaptureResult inputResult) throws CameraAccessException {
        CaptureRequest.Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            CameraMetadataNative resultMetadata = new CameraMetadataNative(inputResult.getNativeCopy());
            builder = new CaptureRequest.Builder(resultMetadata, true, inputResult.getSessionId(), getId(), null);
        }
        return builder;
    }

    public synchronized void prepare(Surface surface) throws CameraAccessException {
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        }
        synchronized (this.mInterfaceLock) {
            int streamId = -1;
            int i = 0;
            while (true) {
                if (i >= this.mConfiguredOutputs.size()) {
                    break;
                }
                List<Surface> surfaces = this.mConfiguredOutputs.valueAt(i).getSurfaces();
                if (!surfaces.contains(surface)) {
                    i++;
                } else {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Surface is not part of this session");
            }
            this.mRemoteDevice.prepare(streamId);
        }
    }

    public synchronized void prepare(int maxCount, Surface surface) throws CameraAccessException {
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        }
        if (maxCount <= 0) {
            throw new IllegalArgumentException("Invalid maxCount given: " + maxCount);
        }
        synchronized (this.mInterfaceLock) {
            int streamId = -1;
            int i = 0;
            while (true) {
                if (i >= this.mConfiguredOutputs.size()) {
                    break;
                } else if (surface != this.mConfiguredOutputs.valueAt(i).getSurface()) {
                    i++;
                } else {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Surface is not part of this session");
            }
            this.mRemoteDevice.prepare2(maxCount, streamId);
        }
    }

    public synchronized void updateOutputConfiguration(OutputConfiguration config) throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            int streamId = -1;
            int i = 0;
            while (true) {
                if (i >= this.mConfiguredOutputs.size()) {
                    break;
                } else if (config.getSurface() != this.mConfiguredOutputs.valueAt(i).getSurface()) {
                    i++;
                } else {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Invalid output configuration");
            }
            this.mRemoteDevice.updateOutputConfiguration(streamId, config);
            this.mConfiguredOutputs.put(streamId, config);
        }
    }

    public synchronized void tearDown(Surface surface) throws CameraAccessException {
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        }
        synchronized (this.mInterfaceLock) {
            int streamId = -1;
            int i = 0;
            while (true) {
                if (i >= this.mConfiguredOutputs.size()) {
                    break;
                } else if (surface != this.mConfiguredOutputs.valueAt(i).getSurface()) {
                    i++;
                } else {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId == -1) {
                throw new IllegalArgumentException("Surface is not part of this session");
            }
            this.mRemoteDevice.tearDown(streamId);
        }
    }

    public synchronized void finalizeOutputConfigs(List<OutputConfiguration> outputConfigs) throws CameraAccessException {
        if (outputConfigs == null || outputConfigs.size() == 0) {
            throw new IllegalArgumentException("deferred config is null or empty");
        }
        synchronized (this.mInterfaceLock) {
            for (OutputConfiguration config : outputConfigs) {
                int streamId = -1;
                int i = 0;
                while (true) {
                    if (i < this.mConfiguredOutputs.size()) {
                        if (!config.equals(this.mConfiguredOutputs.valueAt(i))) {
                            i++;
                        } else {
                            streamId = this.mConfiguredOutputs.keyAt(i);
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (streamId == -1) {
                    throw new IllegalArgumentException("Deferred config is not part of this session");
                }
                if (config.getSurfaces().size() == 0) {
                    throw new IllegalArgumentException("The final config for stream " + streamId + " must have at least 1 surface");
                }
                this.mRemoteDevice.finalizeOutputConfigurations(streamId, config);
                this.mConfiguredOutputs.put(streamId, config);
            }
        }
    }

    public synchronized int capture(CaptureRequest request, CaptureCallback callback, Executor executor) throws CameraAccessException {
        List<CaptureRequest> requestList = new ArrayList<>();
        requestList.add(request);
        return submitCaptureRequest(requestList, callback, executor, false);
    }

    public synchronized int captureBurst(List<CaptureRequest> requests, CaptureCallback callback, Executor executor) throws CameraAccessException {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("At least one request must be given");
        }
        return submitCaptureRequest(requests, callback, executor, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void checkEarlyTriggerSequenceComplete(final int requestId, long lastFrameNumber) {
        if (lastFrameNumber == -1) {
            int index = this.mCaptureCallbackMap.indexOfKey(requestId);
            final CaptureCallbackHolder holder = index >= 0 ? this.mCaptureCallbackMap.valueAt(index) : null;
            if (holder != null) {
                this.mCaptureCallbackMap.removeAt(index);
            }
            if (holder != null) {
                Runnable resultDispatch = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.9
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!CameraDeviceImpl.this.isClosed()) {
                            holder.getCallback().onCaptureSequenceAborted(CameraDeviceImpl.this, requestId);
                        }
                    }
                };
                long ident = Binder.clearCallingIdentity();
                try {
                    holder.getExecutor().execute(resultDispatch);
                    return;
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
            Log.w(this.TAG, String.format("did not register callback to request %d", Integer.valueOf(requestId)));
            return;
        }
        this.mRequestLastFrameNumbersList.add(new RequestLastFrameNumbersHolder(requestId, lastFrameNumber));
        checkAndFireSequenceComplete();
    }

    private synchronized int submitCaptureRequest(List<CaptureRequest> requestList, CaptureCallback callback, Executor executor, boolean repeating) throws CameraAccessException {
        int requestId;
        Executor executor2 = checkExecutor(executor, callback);
        for (CaptureRequest request : requestList) {
            if (request.getTargets().isEmpty()) {
                throw new IllegalArgumentException("Each request must have at least one Surface target");
            }
            for (Surface surface : request.getTargets()) {
                if (surface == null) {
                    throw new IllegalArgumentException("Null Surface targets are not allowed");
                }
                for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                    OutputConfiguration configuration = this.mConfiguredOutputs.valueAt(i);
                    if (configuration.isForPhysicalCamera() && configuration.getSurfaces().contains(surface) && request.isReprocess()) {
                        throw new IllegalArgumentException("Reprocess request on physical stream is not allowed");
                    }
                }
            }
        }
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (repeating) {
                stopRepeating();
            }
            CaptureRequest[] requestArray = (CaptureRequest[]) requestList.toArray(new CaptureRequest[requestList.size()]);
            for (CaptureRequest request2 : requestArray) {
                request2.convertSurfaceToStreamId(this.mConfiguredOutputs);
            }
            SubmitInfo requestInfo = this.mRemoteDevice.submitRequestList(requestArray, repeating);
            for (CaptureRequest request3 : requestArray) {
                request3.recoverStreamIdToSurface();
            }
            if (callback != null) {
                this.mCaptureCallbackMap.put(requestInfo.getRequestId(), new CaptureCallbackHolder(callback, requestList, executor2, repeating, this.mNextSessionId - 1));
            }
            if (repeating) {
                if (this.mRepeatingRequestId != -1) {
                    checkEarlyTriggerSequenceComplete(this.mRepeatingRequestId, requestInfo.getLastFrameNumber());
                }
                this.mRepeatingRequestId = requestInfo.getRequestId();
            } else {
                this.mRequestLastFrameNumbersList.add(new RequestLastFrameNumbersHolder(requestList, requestInfo));
            }
            if (this.mIdle) {
                this.mDeviceExecutor.execute(this.mCallOnActive);
            }
            this.mIdle = false;
            requestId = requestInfo.getRequestId();
        }
        return requestId;
    }

    public synchronized int setRepeatingRequest(CaptureRequest request, CaptureCallback callback, Executor executor) throws CameraAccessException {
        List<CaptureRequest> requestList = new ArrayList<>();
        requestList.add(request);
        return submitCaptureRequest(requestList, callback, executor, true);
    }

    public synchronized int setRepeatingBurst(List<CaptureRequest> requests, CaptureCallback callback, Executor executor) throws CameraAccessException {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("At least one request must be given");
        }
        return submitCaptureRequest(requests, callback, executor, true);
    }

    public synchronized void stopRepeating() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (this.mRepeatingRequestId != -1) {
                int requestId = this.mRepeatingRequestId;
                this.mRepeatingRequestId = -1;
                try {
                    long lastFrameNumber = this.mRemoteDevice.cancelRequest(requestId);
                    checkEarlyTriggerSequenceComplete(requestId, lastFrameNumber);
                } catch (IllegalArgumentException e) {
                }
            }
        }
    }

    private synchronized void waitUntilIdle() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (this.mRepeatingRequestId != -1) {
                throw new IllegalStateException("Active repeating request ongoing");
            }
            this.mRemoteDevice.waitUntilIdle();
        }
    }

    public synchronized void flush() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            this.mDeviceExecutor.execute(this.mCallOnBusy);
            if (this.mIdle) {
                this.mDeviceExecutor.execute(this.mCallOnIdle);
                return;
            }
            long lastFrameNumber = this.mRemoteDevice.flush();
            if (this.mRepeatingRequestId != -1) {
                checkEarlyTriggerSequenceComplete(this.mRepeatingRequestId, lastFrameNumber);
                this.mRepeatingRequestId = -1;
            }
        }
    }

    @Override // android.hardware.camera2.CameraDevice, java.lang.AutoCloseable
    public void close() {
        synchronized (this.mInterfaceLock) {
            if (this.mClosing.getAndSet(true)) {
                return;
            }
            if (this.mRemoteDevice != null) {
                this.mRemoteDevice.disconnect();
                this.mRemoteDevice.unlinkToDeath(this, 0);
            }
            if (this.mRemoteDevice != null || this.mInError) {
                this.mDeviceExecutor.execute(this.mCallOnClosed);
            }
            this.mRemoteDevice = null;
        }
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private synchronized void checkInputConfiguration(InputConfiguration inputConfig) {
        if (inputConfig != null) {
            StreamConfigurationMap configMap = (StreamConfigurationMap) this.mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            int[] inputFormats = configMap.getInputFormats();
            boolean validFormat = false;
            for (int format : inputFormats) {
                if (format == inputConfig.getFormat()) {
                    validFormat = true;
                }
            }
            if (!validFormat) {
                throw new IllegalArgumentException("input format " + inputConfig.getFormat() + " is not valid");
            }
            boolean validSize = false;
            Size[] inputSizes = configMap.getInputSizes(inputConfig.getFormat());
            for (Size s : inputSizes) {
                if (inputConfig.getWidth() == s.getWidth() && inputConfig.getHeight() == s.getHeight()) {
                    validSize = true;
                }
            }
            if (!validSize) {
                throw new IllegalArgumentException("input size " + inputConfig.getWidth() + "x" + inputConfig.getHeight() + " is not valid");
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class StateCallbackKK extends CameraDevice.StateCallback {
        public synchronized void onUnconfigured(CameraDevice camera) {
        }

        public synchronized void onActive(CameraDevice camera) {
        }

        public synchronized void onBusy(CameraDevice camera) {
        }

        public synchronized void onIdle(CameraDevice camera) {
        }

        public synchronized void onRequestQueueEmpty() {
        }

        public synchronized void onSurfacePrepared(Surface surface) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class CaptureCallbackHolder {
        private final CaptureCallback mCallback;
        private final Executor mExecutor;
        private final boolean mHasBatchedOutputs;
        private final boolean mRepeating;
        private final List<CaptureRequest> mRequestList;
        private final int mSessionId;

        synchronized CaptureCallbackHolder(CaptureCallback callback, List<CaptureRequest> requestList, Executor executor, boolean repeating, int sessionId) {
            if (callback == null || executor == null) {
                throw new UnsupportedOperationException("Must have a valid handler and a valid callback");
            }
            this.mRepeating = repeating;
            this.mExecutor = executor;
            this.mRequestList = new ArrayList(requestList);
            this.mCallback = callback;
            this.mSessionId = sessionId;
            boolean hasBatchedOutputs = true;
            int i = 0;
            while (true) {
                if (i >= requestList.size()) {
                    break;
                }
                CaptureRequest request = requestList.get(i);
                if (!request.isPartOfCRequestList()) {
                    hasBatchedOutputs = false;
                    break;
                }
                if (i == 0) {
                    Collection<Surface> targets = request.getTargets();
                    if (targets.size() != 2) {
                        hasBatchedOutputs = false;
                        break;
                    }
                }
                i++;
            }
            this.mHasBatchedOutputs = hasBatchedOutputs;
        }

        public synchronized boolean isRepeating() {
            return this.mRepeating;
        }

        public synchronized CaptureCallback getCallback() {
            return this.mCallback;
        }

        public synchronized CaptureRequest getRequest(int subsequenceId) {
            if (subsequenceId >= this.mRequestList.size()) {
                throw new IllegalArgumentException(String.format("Requested subsequenceId %d is larger than request list size %d.", Integer.valueOf(subsequenceId), Integer.valueOf(this.mRequestList.size())));
            }
            if (subsequenceId < 0) {
                throw new IllegalArgumentException(String.format("Requested subsequenceId %d is negative", Integer.valueOf(subsequenceId)));
            }
            return this.mRequestList.get(subsequenceId);
        }

        public synchronized CaptureRequest getRequest() {
            return getRequest(0);
        }

        public synchronized Executor getExecutor() {
            return this.mExecutor;
        }

        public synchronized int getSessionId() {
            return this.mSessionId;
        }

        public synchronized int getRequestCount() {
            return this.mRequestList.size();
        }

        public synchronized boolean hasBatchedOutputs() {
            return this.mHasBatchedOutputs;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class RequestLastFrameNumbersHolder {
        private final long mLastRegularFrameNumber;
        private final long mLastReprocessFrameNumber;
        private final int mRequestId;

        public synchronized RequestLastFrameNumbersHolder(List<CaptureRequest> requestList, SubmitInfo requestInfo) {
            long lastRegularFrameNumber = -1;
            long lastReprocessFrameNumber = -1;
            long frameNumber = requestInfo.getLastFrameNumber();
            if (requestInfo.getLastFrameNumber() < requestList.size() - 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("lastFrameNumber: ");
                sb.append(requestInfo.getLastFrameNumber());
                sb.append(" should be at least ");
                sb.append(requestList.size() - 1);
                sb.append(" for the number of  requests in the list: ");
                sb.append(requestList.size());
                throw new IllegalArgumentException(sb.toString());
            }
            for (int i = requestList.size() - 1; i >= 0; i--) {
                CaptureRequest request = requestList.get(i);
                if (request.isReprocess() && lastReprocessFrameNumber == -1) {
                    lastReprocessFrameNumber = frameNumber;
                } else if (!request.isReprocess() && lastRegularFrameNumber == -1) {
                    lastRegularFrameNumber = frameNumber;
                }
                if (lastReprocessFrameNumber != -1 && lastRegularFrameNumber != -1) {
                    break;
                }
                frameNumber--;
            }
            this.mLastRegularFrameNumber = lastRegularFrameNumber;
            this.mLastReprocessFrameNumber = lastReprocessFrameNumber;
            this.mRequestId = requestInfo.getRequestId();
        }

        public synchronized RequestLastFrameNumbersHolder(int requestId, long lastRegularFrameNumber) {
            this.mLastRegularFrameNumber = lastRegularFrameNumber;
            this.mLastReprocessFrameNumber = -1L;
            this.mRequestId = requestId;
        }

        public synchronized long getLastRegularFrameNumber() {
            return this.mLastRegularFrameNumber;
        }

        public synchronized long getLastReprocessFrameNumber() {
            return this.mLastReprocessFrameNumber;
        }

        public synchronized long getLastFrameNumber() {
            return Math.max(this.mLastRegularFrameNumber, this.mLastReprocessFrameNumber);
        }

        public synchronized int getRequestId() {
            return this.mRequestId;
        }
    }

    /* loaded from: classes.dex */
    public class FrameNumberTracker {
        private long mCompletedFrameNumber = -1;
        private long mCompletedReprocessFrameNumber = -1;
        private final LinkedList<Long> mSkippedRegularFrameNumbers = new LinkedList<>();
        private final LinkedList<Long> mSkippedReprocessFrameNumbers = new LinkedList<>();
        private final TreeMap<Long, Boolean> mFutureErrorMap = new TreeMap<>();
        private final HashMap<Long, List<CaptureResult>> mPartialResults = new HashMap<>();

        public FrameNumberTracker() {
        }

        private synchronized void update() {
            Iterator iter = this.mFutureErrorMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Long, Boolean> pair = iter.next();
                Long errorFrameNumber = pair.getKey();
                Boolean reprocess = pair.getValue();
                Boolean removeError = true;
                if (reprocess.booleanValue()) {
                    if (errorFrameNumber.longValue() != this.mCompletedReprocessFrameNumber + 1) {
                        if (!this.mSkippedReprocessFrameNumbers.isEmpty() && errorFrameNumber == this.mSkippedReprocessFrameNumbers.element()) {
                            this.mCompletedReprocessFrameNumber = errorFrameNumber.longValue();
                            this.mSkippedReprocessFrameNumbers.remove();
                        } else {
                            removeError = false;
                        }
                    } else {
                        this.mCompletedReprocessFrameNumber = errorFrameNumber.longValue();
                    }
                } else if (errorFrameNumber.longValue() != this.mCompletedFrameNumber + 1) {
                    if (!this.mSkippedRegularFrameNumbers.isEmpty() && errorFrameNumber == this.mSkippedRegularFrameNumbers.element()) {
                        this.mCompletedFrameNumber = errorFrameNumber.longValue();
                        this.mSkippedRegularFrameNumbers.remove();
                    } else {
                        removeError = false;
                    }
                } else {
                    this.mCompletedFrameNumber = errorFrameNumber.longValue();
                }
                if (removeError.booleanValue()) {
                    iter.remove();
                }
            }
        }

        public synchronized void updateTracker(long frameNumber, boolean isError, boolean isReprocess) {
            if (isError) {
                this.mFutureErrorMap.put(Long.valueOf(frameNumber), Boolean.valueOf(isReprocess));
            } else {
                try {
                    if (isReprocess) {
                        updateCompletedReprocessFrameNumber(frameNumber);
                    } else {
                        updateCompletedFrameNumber(frameNumber);
                    }
                } catch (IllegalArgumentException e) {
                    Log.e(CameraDeviceImpl.this.TAG, e.getMessage());
                }
            }
            update();
        }

        public synchronized void updateTracker(long frameNumber, CaptureResult result, boolean partial, boolean isReprocess) {
            if (!partial) {
                updateTracker(frameNumber, false, isReprocess);
            } else if (result == null) {
            } else {
                List<CaptureResult> partials = this.mPartialResults.get(Long.valueOf(frameNumber));
                if (partials == null) {
                    partials = new ArrayList();
                    this.mPartialResults.put(Long.valueOf(frameNumber), partials);
                }
                partials.add(result);
            }
        }

        public synchronized List<CaptureResult> popPartialResults(long frameNumber) {
            return this.mPartialResults.remove(Long.valueOf(frameNumber));
        }

        public synchronized long getCompletedFrameNumber() {
            return this.mCompletedFrameNumber;
        }

        public synchronized long getCompletedReprocessFrameNumber() {
            return this.mCompletedReprocessFrameNumber;
        }

        private synchronized void updateCompletedFrameNumber(long frameNumber) throws IllegalArgumentException {
            if (frameNumber <= this.mCompletedFrameNumber) {
                throw new IllegalArgumentException("frame number " + frameNumber + " is a repeat");
            }
            if (frameNumber <= this.mCompletedReprocessFrameNumber) {
                if (this.mSkippedRegularFrameNumbers.isEmpty() || frameNumber < this.mSkippedRegularFrameNumbers.element().longValue()) {
                    throw new IllegalArgumentException("frame number " + frameNumber + " is a repeat");
                } else if (frameNumber > this.mSkippedRegularFrameNumbers.element().longValue()) {
                    throw new IllegalArgumentException("frame number " + frameNumber + " comes out of order. Expecting " + this.mSkippedRegularFrameNumbers.element());
                } else {
                    this.mSkippedRegularFrameNumbers.remove();
                }
            } else {
                long i = Math.max(this.mCompletedFrameNumber, this.mCompletedReprocessFrameNumber);
                while (true) {
                    i++;
                    if (i >= frameNumber) {
                        break;
                    }
                    this.mSkippedReprocessFrameNumbers.add(Long.valueOf(i));
                }
            }
            this.mCompletedFrameNumber = frameNumber;
        }

        private synchronized void updateCompletedReprocessFrameNumber(long frameNumber) throws IllegalArgumentException {
            if (frameNumber < this.mCompletedReprocessFrameNumber) {
                throw new IllegalArgumentException("frame number " + frameNumber + " is a repeat");
            }
            if (frameNumber < this.mCompletedFrameNumber) {
                if (this.mSkippedReprocessFrameNumbers.isEmpty() || frameNumber < this.mSkippedReprocessFrameNumbers.element().longValue()) {
                    throw new IllegalArgumentException("frame number " + frameNumber + " is a repeat");
                } else if (frameNumber > this.mSkippedReprocessFrameNumbers.element().longValue()) {
                    throw new IllegalArgumentException("frame number " + frameNumber + " comes out of order. Expecting " + this.mSkippedReprocessFrameNumbers.element());
                } else {
                    this.mSkippedReprocessFrameNumbers.remove();
                }
            } else {
                long i = Math.max(this.mCompletedFrameNumber, this.mCompletedReprocessFrameNumber);
                while (true) {
                    i++;
                    if (i >= frameNumber) {
                        break;
                    }
                    this.mSkippedRegularFrameNumbers.add(Long.valueOf(i));
                }
            }
            this.mCompletedReprocessFrameNumber = frameNumber;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:45:0x009d -> B:43:0x009b). Please submit an issue!!! */
    public synchronized void checkAndFireSequenceComplete() {
        CaptureCallbackHolder holder;
        long completedFrameNumber;
        long completedFrameNumber2 = this.mFrameNumberTracker.getCompletedFrameNumber();
        long completedReprocessFrameNumber = this.mFrameNumberTracker.getCompletedReprocessFrameNumber();
        Iterator<RequestLastFrameNumbersHolder> iter = this.mRequestLastFrameNumbersList.iterator();
        while (true) {
            Iterator<RequestLastFrameNumbersHolder> iter2 = iter;
            if (!iter2.hasNext()) {
                return;
            }
            final RequestLastFrameNumbersHolder requestLastFrameNumbers = iter2.next();
            boolean sequenceCompleted = false;
            final int requestId = requestLastFrameNumbers.getRequestId();
            synchronized (this.mInterfaceLock) {
                try {
                    if (this.mRemoteDevice == null) {
                        Log.w(this.TAG, "Camera closed while checking sequences");
                        return;
                    }
                    int index = this.mCaptureCallbackMap.indexOfKey(requestId);
                    if (index >= 0) {
                        try {
                            holder = this.mCaptureCallbackMap.valueAt(index);
                        } catch (Throwable th) {
                            th = th;
                        }
                    } else {
                        holder = null;
                    }
                    if (holder != null) {
                        long lastRegularFrameNumber = requestLastFrameNumbers.getLastRegularFrameNumber();
                        long lastReprocessFrameNumber = requestLastFrameNumbers.getLastReprocessFrameNumber();
                        if (lastRegularFrameNumber <= completedFrameNumber2 && lastReprocessFrameNumber <= completedReprocessFrameNumber) {
                            sequenceCompleted = true;
                            completedFrameNumber = completedFrameNumber2;
                            try {
                                this.mCaptureCallbackMap.removeAt(index);
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        }
                    }
                    completedFrameNumber = completedFrameNumber2;
                    th = th;
                } catch (Throwable th3) {
                    th = th3;
                }
                throw th;
            }
            final CaptureCallbackHolder holder2 = holder;
            if (holder2 == null || sequenceCompleted) {
                iter2.remove();
            }
            if (sequenceCompleted) {
                Runnable resultDispatch = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.10
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!CameraDeviceImpl.this.isClosed()) {
                            holder2.getCallback().onCaptureSequenceCompleted(CameraDeviceImpl.this, requestId, requestLastFrameNumbers.getLastFrameNumber());
                        }
                    }
                };
                long ident = Binder.clearCallingIdentity();
                try {
                    holder2.getExecutor().execute(resultDispatch);
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            }
            iter = iter2;
            completedFrameNumber2 = completedFrameNumber;
        }
    }

    /* loaded from: classes.dex */
    public class CameraDeviceCallbacks extends ICameraDeviceCallbacks.Stub {
        public CameraDeviceCallbacks() {
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks.Stub, android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public synchronized void onDeviceError(int errorCode, CaptureResultExtras resultExtras) {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                switch (errorCode) {
                    case 0:
                        long ident = Binder.clearCallingIdentity();
                        CameraDeviceImpl.this.mDeviceExecutor.execute(CameraDeviceImpl.this.mCallOnDisconnected);
                        Binder.restoreCallingIdentity(ident);
                        break;
                    case 1:
                        scheduleNotifyError(4);
                        break;
                    case 2:
                    default:
                        String str = CameraDeviceImpl.this.TAG;
                        Log.e(str, "Unknown error from camera device: " + errorCode);
                        scheduleNotifyError(5);
                        break;
                    case 3:
                    case 4:
                    case 5:
                        onCaptureErrorLocked(errorCode, resultExtras);
                        break;
                    case 6:
                        scheduleNotifyError(3);
                        break;
                }
            }
        }

        private synchronized void scheduleNotifyError(int code) {
            CameraDeviceImpl.this.mInError = true;
            long ident = Binder.clearCallingIdentity();
            try {
                CameraDeviceImpl.this.mDeviceExecutor.execute(PooledLambda.obtainRunnable(new BiConsumer() { // from class: android.hardware.camera2.impl.-$$Lambda$CameraDeviceImpl$CameraDeviceCallbacks$Sm85frAzwGZVMAK-NE_gwckYXVQ
                    @Override // java.util.function.BiConsumer
                    public final void accept(Object obj, Object obj2) {
                        ((CameraDeviceImpl.CameraDeviceCallbacks) obj).notifyError(((Integer) obj2).intValue());
                    }
                }, this, Integer.valueOf(code)));
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void notifyError(int code) {
            if (!CameraDeviceImpl.this.isClosed()) {
                CameraDeviceImpl.this.mDeviceCallback.onError(CameraDeviceImpl.this, code);
            }
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public synchronized void onRepeatingRequestError(long lastFrameNumber, int repeatingRequestId) {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice != null && CameraDeviceImpl.this.mRepeatingRequestId != -1) {
                    CameraDeviceImpl.this.checkEarlyTriggerSequenceComplete(CameraDeviceImpl.this.mRepeatingRequestId, lastFrameNumber);
                    if (CameraDeviceImpl.this.mRepeatingRequestId == repeatingRequestId) {
                        CameraDeviceImpl.this.mRepeatingRequestId = -1;
                    }
                }
            }
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public synchronized void onDeviceIdle() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                if (!CameraDeviceImpl.this.mIdle) {
                    long ident = Binder.clearCallingIdentity();
                    CameraDeviceImpl.this.mDeviceExecutor.execute(CameraDeviceImpl.this.mCallOnIdle);
                    Binder.restoreCallingIdentity(ident);
                }
                CameraDeviceImpl.this.mIdle = true;
            }
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public synchronized void onCaptureStarted(final CaptureResultExtras resultExtras, final long timestamp) {
            long ident;
            int requestId = resultExtras.getRequestId();
            final long frameNumber = resultExtras.getFrameNumber();
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                try {
                    try {
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    if (CameraDeviceImpl.this.mRemoteDevice == null) {
                        return;
                    }
                    final CaptureCallbackHolder holder = (CaptureCallbackHolder) CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
                    if (holder == null) {
                        return;
                    }
                    if (CameraDeviceImpl.this.isClosed()) {
                        return;
                    }
                    long ident2 = Binder.clearCallingIdentity();
                    try {
                        ident = ident2;
                        try {
                            holder.getExecutor().execute(new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (!CameraDeviceImpl.this.isClosed()) {
                                        int subsequenceId = resultExtras.getSubsequenceId();
                                        CaptureRequest request = holder.getRequest(subsequenceId);
                                        if (holder.hasBatchedOutputs()) {
                                            Range<Integer> fpsRange = (Range) request.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
                                            for (int i = 0; i < holder.getRequestCount(); i++) {
                                                holder.getCallback().onCaptureStarted(CameraDeviceImpl.this, holder.getRequest(i), timestamp - (((subsequenceId - i) * 1000000000) / fpsRange.getUpper().intValue()), frameNumber - (subsequenceId - i));
                                            }
                                            return;
                                        }
                                        holder.getCallback().onCaptureStarted(CameraDeviceImpl.this, holder.getRequest(resultExtras.getSubsequenceId()), timestamp, frameNumber);
                                    }
                                }
                            });
                            Binder.restoreCallingIdentity(ident);
                        } catch (Throwable th3) {
                            th = th3;
                            Binder.restoreCallingIdentity(ident);
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        ident = ident2;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    throw th;
                }
            }
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public synchronized void onResultReceived(CameraMetadataNative result, final CaptureResultExtras resultExtras, PhysicalCaptureResultInfo[] physicalResults) throws RemoteException {
            Object obj;
            CameraMetadataNative resultCopy;
            final CaptureCallbackHolder holder;
            long frameNumber;
            Runnable resultDispatch;
            Object obj2;
            CameraDeviceCallbacks cameraDeviceCallbacks;
            CaptureResult finalResult;
            int requestId = resultExtras.getRequestId();
            long frameNumber2 = resultExtras.getFrameNumber();
            Object obj3 = CameraDeviceImpl.this.mInterfaceLock;
            synchronized (obj3) {
                try {
                    try {
                        try {
                            if (CameraDeviceImpl.this.mRemoteDevice == null) {
                                return;
                            }
                            result.set((CameraCharacteristics.Key<CameraCharacteristics.Key<Size>>) CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE, (CameraCharacteristics.Key<Size>) ((Size) CameraDeviceImpl.this.getCharacteristics().get(CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE)));
                            final CaptureCallbackHolder holder2 = (CaptureCallbackHolder) CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
                            final CaptureRequest request = holder2.getRequest(resultExtras.getSubsequenceId());
                            boolean isPartialResult = resultExtras.getPartialResultCount() < CameraDeviceImpl.this.mTotalPartialCount;
                            boolean isReprocess = request.isReprocess();
                            if (holder2 == null) {
                                CameraDeviceImpl.this.mFrameNumberTracker.updateTracker(frameNumber2, null, isPartialResult, isReprocess);
                            } else if (CameraDeviceImpl.this.isClosed()) {
                                CameraDeviceImpl.this.mFrameNumberTracker.updateTracker(frameNumber2, null, isPartialResult, isReprocess);
                            } else {
                                if (holder2.hasBatchedOutputs()) {
                                    resultCopy = new CameraMetadataNative(result);
                                } else {
                                    resultCopy = null;
                                }
                                final CameraMetadataNative resultCopy2 = resultCopy;
                                if (!isPartialResult) {
                                    final List<CaptureResult> partialResults = CameraDeviceImpl.this.mFrameNumberTracker.popPartialResults(frameNumber2);
                                    final long sensorTimestamp = ((Long) result.get(CaptureResult.SENSOR_TIMESTAMP)).longValue();
                                    holder = holder2;
                                    final Range<Integer> fpsRange = (Range) request.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
                                    frameNumber = frameNumber2;
                                    try {
                                        final int subsequenceId = resultExtras.getSubsequenceId();
                                        final TotalCaptureResult resultAsCapture = new TotalCaptureResult(result, request, resultExtras, partialResults, holder.getSessionId(), physicalResults);
                                        obj2 = obj3;
                                        cameraDeviceCallbacks = this;
                                        resultDispatch = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks.3
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                if (!CameraDeviceImpl.this.isClosed()) {
                                                    if (holder.hasBatchedOutputs()) {
                                                        for (int i = 0; i < holder.getRequestCount(); i++) {
                                                            resultCopy2.set((CaptureResult.Key<CaptureResult.Key<Long>>) CaptureResult.SENSOR_TIMESTAMP, (CaptureResult.Key<Long>) Long.valueOf(sensorTimestamp - (((subsequenceId - i) * 1000000000) / ((Integer) fpsRange.getUpper()).intValue())));
                                                            CameraMetadataNative resultLocal = new CameraMetadataNative(resultCopy2);
                                                            TotalCaptureResult resultInBatch = new TotalCaptureResult(resultLocal, holder.getRequest(i), resultExtras, partialResults, holder.getSessionId(), new PhysicalCaptureResultInfo[0]);
                                                            holder.getCallback().onCaptureCompleted(CameraDeviceImpl.this, holder.getRequest(i), resultInBatch);
                                                        }
                                                        return;
                                                    }
                                                    holder.getCallback().onCaptureCompleted(CameraDeviceImpl.this, request, resultAsCapture);
                                                }
                                            }
                                        };
                                        finalResult = resultAsCapture;
                                    } catch (Throwable th) {
                                        th = th;
                                        obj = obj3;
                                        throw th;
                                    }
                                } else {
                                    final CaptureResult resultAsCapture2 = new CaptureResult(result, request, resultExtras);
                                    Runnable resultDispatch2 = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            if (!CameraDeviceImpl.this.isClosed()) {
                                                if (holder2.hasBatchedOutputs()) {
                                                    for (int i = 0; i < holder2.getRequestCount(); i++) {
                                                        CameraMetadataNative resultLocal = new CameraMetadataNative(resultCopy2);
                                                        CaptureResult resultInBatch = new CaptureResult(resultLocal, holder2.getRequest(i), resultExtras);
                                                        holder2.getCallback().onCaptureProgressed(CameraDeviceImpl.this, holder2.getRequest(i), resultInBatch);
                                                    }
                                                    return;
                                                }
                                                holder2.getCallback().onCaptureProgressed(CameraDeviceImpl.this, request, resultAsCapture2);
                                            }
                                        }
                                    };
                                    obj2 = obj3;
                                    holder = holder2;
                                    frameNumber = frameNumber2;
                                    cameraDeviceCallbacks = this;
                                    finalResult = resultAsCapture2;
                                    resultDispatch = resultDispatch2;
                                }
                                long ident = Binder.clearCallingIdentity();
                                holder.getExecutor().execute(resultDispatch);
                                Binder.restoreCallingIdentity(ident);
                                CameraDeviceImpl.this.mFrameNumberTracker.updateTracker(frameNumber, finalResult, isPartialResult, isReprocess);
                                if (!isPartialResult) {
                                    CameraDeviceImpl.this.checkAndFireSequenceComplete();
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            obj = obj3;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        obj = obj3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public synchronized void onPrepared(int streamId) {
            OutputConfiguration output;
            StateCallbackKK sessionCallback;
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                output = (OutputConfiguration) CameraDeviceImpl.this.mConfiguredOutputs.get(streamId);
                sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
            }
            if (sessionCallback == null) {
                return;
            }
            if (output == null) {
                Log.w(CameraDeviceImpl.this.TAG, "onPrepared invoked for unknown output Surface");
                return;
            }
            List<Surface> surfaces = output.getSurfaces();
            for (Surface surface : surfaces) {
                sessionCallback.onSurfacePrepared(surface);
            }
        }

        @Override // android.hardware.camera2.ICameraDeviceCallbacks
        public synchronized void onRequestQueueEmpty() {
            StateCallbackKK sessionCallback;
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
            }
            if (sessionCallback == null) {
                return;
            }
            sessionCallback.onRequestQueueEmpty();
        }

        private synchronized void onCaptureErrorLocked(int errorCode, CaptureResultExtras resultExtras) {
            long ident;
            int requestId = resultExtras.getRequestId();
            int subsequenceId = resultExtras.getSubsequenceId();
            final long frameNumber = resultExtras.getFrameNumber();
            final CaptureCallbackHolder holder = (CaptureCallbackHolder) CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
            final CaptureRequest request = holder.getRequest(subsequenceId);
            if (errorCode == 5) {
                List<Surface> surfaces = ((OutputConfiguration) CameraDeviceImpl.this.mConfiguredOutputs.get(resultExtras.getErrorStreamId())).getSurfaces();
                Iterator<Surface> it = surfaces.iterator();
                while (it.hasNext()) {
                    final Surface surface = it.next();
                    if (request.containsTarget(surface)) {
                        List<Surface> surfaces2 = surfaces;
                        Iterator<Surface> it2 = it;
                        Runnable failureDispatch = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks.4
                            @Override // java.lang.Runnable
                            public void run() {
                                if (!CameraDeviceImpl.this.isClosed()) {
                                    holder.getCallback().onCaptureBufferLost(CameraDeviceImpl.this, request, surface, frameNumber);
                                }
                            }
                        };
                        ident = Binder.clearCallingIdentity();
                        try {
                            holder.getExecutor().execute(failureDispatch);
                            Binder.restoreCallingIdentity(ident);
                            surfaces = surfaces2;
                            it = it2;
                        } finally {
                        }
                    }
                }
                return;
            }
            boolean mayHaveBuffers = errorCode == 4;
            int reason = (CameraDeviceImpl.this.mCurrentSession == null || !CameraDeviceImpl.this.mCurrentSession.isAborting()) ? 0 : 1;
            final CaptureFailure failure = new CaptureFailure(request, reason, mayHaveBuffers, requestId, frameNumber);
            Runnable failureDispatch2 = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.CameraDeviceCallbacks.5
                @Override // java.lang.Runnable
                public void run() {
                    if (!CameraDeviceImpl.this.isClosed()) {
                        holder.getCallback().onCaptureFailed(CameraDeviceImpl.this, request, failure);
                    }
                }
            };
            CameraDeviceImpl.this.mFrameNumberTracker.updateTracker(frameNumber, true, request.isReprocess());
            CameraDeviceImpl.this.checkAndFireSequenceComplete();
            ident = Binder.clearCallingIdentity();
            try {
                holder.getExecutor().execute(failureDispatch2);
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CameraHandlerExecutor implements Executor {
        private final Handler mHandler;

        public synchronized CameraHandlerExecutor(Handler handler) {
            this.mHandler = (Handler) Preconditions.checkNotNull(handler);
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable command) {
            this.mHandler.post(command);
        }
    }

    static synchronized Executor checkExecutor(Executor executor) {
        return executor == null ? checkAndWrapHandler(null) : executor;
    }

    public static synchronized <T> Executor checkExecutor(Executor executor, T callback) {
        return callback != null ? checkExecutor(executor) : executor;
    }

    public static synchronized Executor checkAndWrapHandler(Handler handler) {
        return new CameraHandlerExecutor(checkHandler(handler));
    }

    static synchronized Handler checkHandler(Handler handler) {
        if (handler == null) {
            Looper looper = Looper.myLooper();
            if (looper == null) {
                throw new IllegalArgumentException("No handler given, and current thread has no looper!");
            }
            return new Handler(looper);
        }
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized <T> Handler checkHandler(Handler handler, T callback) {
        if (callback != null) {
            return checkHandler(handler);
        }
        return handler;
    }

    private synchronized void checkIfCameraClosedOrInError() throws CameraAccessException {
        if (this.mRemoteDevice == null) {
            throw new IllegalStateException("CameraDevice was already closed");
        }
        if (this.mInError) {
            throw new CameraAccessException(3, "The camera device has encountered a serious error");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isClosed() {
        return this.mClosing.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized CameraCharacteristics getCharacteristics() {
        return this.mCharacteristics;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        String str = this.TAG;
        Log.w(str, "CameraDevice " + this.mCameraId + " died unexpectedly");
        if (this.mRemoteDevice == null) {
            return;
        }
        this.mInError = true;
        Runnable r = new Runnable() { // from class: android.hardware.camera2.impl.CameraDeviceImpl.11
            @Override // java.lang.Runnable
            public void run() {
                if (!CameraDeviceImpl.this.isClosed()) {
                    CameraDeviceImpl.this.mDeviceCallback.onError(CameraDeviceImpl.this, 5);
                }
            }
        };
        long ident = Binder.clearCallingIdentity();
        try {
            this.mDeviceExecutor.execute(r);
        } finally {
            Binder.restoreCallingIdentity(ident);
        }
    }
}
