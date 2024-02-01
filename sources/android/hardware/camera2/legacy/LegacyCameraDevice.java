package android.hardware.camera2.legacy;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.ICameraDeviceCallbacks;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.CaptureResultExtras;
import android.hardware.camera2.impl.PhysicalCaptureResultInfo;
import android.hardware.camera2.legacy.CameraDeviceState;
import android.hardware.camera2.legacy.LegacyExceptionUtils;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.ArrayUtils;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
/* loaded from: classes.dex */
public class LegacyCameraDevice implements AutoCloseable {
    public protected static final boolean DEBUG = false;
    public protected static final int GRALLOC_USAGE_HW_COMPOSER = 2048;
    public protected static final int GRALLOC_USAGE_HW_RENDER = 512;
    public protected static final int GRALLOC_USAGE_HW_TEXTURE = 256;
    public protected static final int GRALLOC_USAGE_HW_VIDEO_ENCODER = 65536;
    public protected static final int GRALLOC_USAGE_RENDERSCRIPT = 1048576;
    public protected static final int GRALLOC_USAGE_SW_READ_OFTEN = 3;
    public protected static final int ILLEGAL_VALUE = -1;
    private protected static final int MAX_DIMEN_FOR_ROUNDING = 1920;
    private protected static final int NATIVE_WINDOW_SCALING_MODE_SCALE_TO_WINDOW = 1;
    public protected final String TAG;
    public protected final Handler mCallbackHandler;
    public protected final int mCameraId;
    public protected SparseArray<Surface> mConfiguredSurfaces;
    public protected final ICameraDeviceCallbacks mDeviceCallbacks;
    public protected final RequestThreadManager mRequestThreadManager;
    public protected final Handler mResultHandler;
    public protected final CameraCharacteristics mStaticCharacteristics;
    public protected final CameraDeviceState mDeviceState = new CameraDeviceState();
    public protected boolean mClosed = false;
    public protected final ConditionVariable mIdle = new ConditionVariable(true);
    public protected final HandlerThread mResultThread = new HandlerThread("ResultThread");
    public protected final HandlerThread mCallbackHandlerThread = new HandlerThread("CallbackThread");
    public protected final CameraDeviceState.CameraDeviceStateListener mStateListener = new CameraDeviceState.CameraDeviceStateListener() { // from class: android.hardware.camera2.legacy.LegacyCameraDevice.1
        public void onError(final int errorCode, Object errorArg, final RequestHolder holder) {
            switch (errorCode) {
                case 0:
                case 1:
                case 2:
                    LegacyCameraDevice.this.mIdle.open();
                    break;
            }
            final CaptureResultExtras extras = LegacyCameraDevice.this.getExtrasFromRequest(holder, errorCode, errorArg);
            LegacyCameraDevice.this.mResultHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.LegacyCameraDevice.1.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        LegacyCameraDevice.this.mDeviceCallbacks.onDeviceError(errorCode, extras);
                    } catch (RemoteException e) {
                        throw new IllegalStateException("Received remote exception during onCameraError callback: ", e);
                    }
                }
            });
        }

        public void onConfiguring() {
        }

        public void onIdle() {
            LegacyCameraDevice.this.mIdle.open();
            LegacyCameraDevice.this.mResultHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.LegacyCameraDevice.1.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        LegacyCameraDevice.this.mDeviceCallbacks.onDeviceIdle();
                    } catch (RemoteException e) {
                        throw new IllegalStateException("Received remote exception during onCameraIdle callback: ", e);
                    }
                }
            });
        }

        public void onBusy() {
            LegacyCameraDevice.this.mIdle.close();
        }

        public void onCaptureStarted(final RequestHolder holder, final long timestamp) {
            final CaptureResultExtras extras = LegacyCameraDevice.this.getExtrasFromRequest(holder);
            LegacyCameraDevice.this.mResultHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.LegacyCameraDevice.1.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        LegacyCameraDevice.this.mDeviceCallbacks.onCaptureStarted(extras, timestamp);
                    } catch (RemoteException e) {
                        throw new IllegalStateException("Received remote exception during onCameraError callback: ", e);
                    }
                }
            });
        }

        public void onRequestQueueEmpty() {
            LegacyCameraDevice.this.mResultHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.LegacyCameraDevice.1.4
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        LegacyCameraDevice.this.mDeviceCallbacks.onRequestQueueEmpty();
                    } catch (RemoteException e) {
                        throw new IllegalStateException("Received remote exception during onRequestQueueEmpty callback: ", e);
                    }
                }
            });
        }

        public void onCaptureResult(final CameraMetadataNative result, final RequestHolder holder) {
            final CaptureResultExtras extras = LegacyCameraDevice.this.getExtrasFromRequest(holder);
            LegacyCameraDevice.this.mResultHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.LegacyCameraDevice.1.5
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        LegacyCameraDevice.this.mDeviceCallbacks.onResultReceived(result, extras, new PhysicalCaptureResultInfo[0]);
                    } catch (RemoteException e) {
                        throw new IllegalStateException("Received remote exception during onCameraError callback: ", e);
                    }
                }
            });
        }

        public void onRepeatingRequestError(final long lastFrameNumber, final int repeatingRequestId) {
            LegacyCameraDevice.this.mResultHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.LegacyCameraDevice.1.6
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        LegacyCameraDevice.this.mDeviceCallbacks.onRepeatingRequestError(lastFrameNumber, repeatingRequestId);
                    } catch (RemoteException e) {
                        throw new IllegalStateException("Received remote exception during onRepeatingRequestError callback: ", e);
                    }
                }
            });
        }
    };

    public protected static native int nativeConnectSurface(Surface surface);

    public protected static native int nativeDetectSurfaceDataspace(Surface surface);

    public protected static native int nativeDetectSurfaceDimens(Surface surface, int[] iArr);

    public protected static native int nativeDetectSurfaceType(Surface surface);

    public protected static native int nativeDetectSurfaceUsageFlags(Surface surface);

    public protected static native int nativeDetectTextureDimens(SurfaceTexture surfaceTexture, int[] iArr);

    public protected static native int nativeDisconnectSurface(Surface surface);

    public private protected static native int nativeGetJpegFooterSize();

    public protected static native long nativeGetSurfaceId(Surface surface);

    public protected static native int nativeProduceFrame(Surface surface, byte[] bArr, int i, int i2, int i3);

    public protected static native int nativeSetNextTimestamp(Surface surface, long j);

    public protected static native int nativeSetScalingMode(Surface surface, int i);

    public protected static native int nativeSetSurfaceDimens(Surface surface, int i, int i2);

    public protected static native int nativeSetSurfaceFormat(Surface surface, int i);

    public protected static native int nativeSetSurfaceOrientation(Surface surface, int i, int i2);

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized CaptureResultExtras getExtrasFromRequest(RequestHolder holder) {
        return getExtrasFromRequest(holder, -1, null);
    }

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized CaptureResultExtras getExtrasFromRequest(RequestHolder holder, int errorCode, Object errorArg) {
        int errorStreamId = -1;
        if (errorCode == 5) {
            Surface errorTarget = (Surface) errorArg;
            int indexOfTarget = this.mConfiguredSurfaces.indexOfValue(errorTarget);
            if (indexOfTarget < 0) {
                Log.e(this.TAG, "Buffer drop error reported for unknown Surface");
            } else {
                errorStreamId = this.mConfiguredSurfaces.keyAt(indexOfTarget);
            }
        }
        if (holder == null) {
            return new CaptureResultExtras(-1, -1, -1, -1, -1L, -1, -1);
        }
        return new CaptureResultExtras(holder.getRequestId(), holder.getSubsequeceId(), 0, 0, holder.getFrameNumber(), 1, errorStreamId);
    }

    public private protected static synchronized boolean needsConversion(Surface s) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        int nativeType = detectSurfaceType(s);
        return nativeType == 35 || nativeType == 842094169 || nativeType == 17;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LegacyCameraDevice(int cameraId, Camera camera, CameraCharacteristics characteristics, ICameraDeviceCallbacks callbacks) {
        this.mCameraId = cameraId;
        this.mDeviceCallbacks = callbacks;
        this.TAG = String.format("CameraDevice-%d-LE", Integer.valueOf(this.mCameraId));
        this.mResultThread.start();
        this.mResultHandler = new Handler(this.mResultThread.getLooper());
        this.mCallbackHandlerThread.start();
        this.mCallbackHandler = new Handler(this.mCallbackHandlerThread.getLooper());
        this.mDeviceState.setCameraDeviceCallbacks(this.mCallbackHandler, this.mStateListener);
        this.mStaticCharacteristics = characteristics;
        this.mRequestThreadManager = new RequestThreadManager(cameraId, camera, characteristics, this.mDeviceState);
        this.mRequestThreadManager.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int configureOutputs(SparseArray<Surface> outputs) {
        List<Pair<Surface, Size>> sizedSurfaces = new ArrayList<>();
        if (outputs != null) {
            int count = outputs.size();
            for (int i = 0; i < count; i++) {
                Surface output = outputs.valueAt(i);
                if (output == null) {
                    Log.e(this.TAG, "configureOutputs - null outputs are not allowed");
                    return LegacyExceptionUtils.BAD_VALUE;
                } else if (!output.isValid()) {
                    Log.e(this.TAG, "configureOutputs - invalid output surfaces are not allowed");
                    return LegacyExceptionUtils.BAD_VALUE;
                } else {
                    StreamConfigurationMap streamConfigurations = (StreamConfigurationMap) this.mStaticCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    try {
                        Size s = getSurfaceSize(output);
                        int surfaceType = detectSurfaceType(output);
                        boolean flexibleConsumer = isFlexibleConsumer(output);
                        Size[] sizes = streamConfigurations.getOutputSizes(surfaceType);
                        if (sizes == null) {
                            if (surfaceType == 34) {
                                sizes = streamConfigurations.getOutputSizes(35);
                            } else if (surfaceType == 33) {
                                sizes = streamConfigurations.getOutputSizes(256);
                            }
                        }
                        if (!ArrayUtils.contains(sizes, s)) {
                            if (flexibleConsumer) {
                                Size findClosestSize = findClosestSize(s, sizes);
                                s = findClosestSize;
                                if (findClosestSize != null) {
                                    sizedSurfaces.add(new Pair<>(output, s));
                                }
                            }
                            String reason = sizes == null ? "format is invalid." : "size not in valid set: " + Arrays.toString(sizes);
                            Log.e(this.TAG, String.format("Surface with size (w=%d, h=%d) and format 0x%x is not valid, %s", Integer.valueOf(s.getWidth()), Integer.valueOf(s.getHeight()), Integer.valueOf(surfaceType), reason));
                            return LegacyExceptionUtils.BAD_VALUE;
                        }
                        sizedSurfaces.add(new Pair<>(output, s));
                        setSurfaceDimens(output, s.getWidth(), s.getHeight());
                    } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
                        Log.e(this.TAG, "Surface bufferqueue is abandoned, cannot configure as output: ", e);
                        return LegacyExceptionUtils.BAD_VALUE;
                    }
                }
            }
        }
        boolean success = false;
        if (this.mDeviceState.setConfiguring()) {
            this.mRequestThreadManager.configure(sizedSurfaces);
            success = this.mDeviceState.setIdle();
        }
        if (success) {
            this.mConfiguredSurfaces = outputs;
            return 0;
        }
        return LegacyExceptionUtils.INVALID_OPERATION;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized SubmitInfo submitRequestList(CaptureRequest[] requestList, boolean repeating) {
        if (requestList == null || requestList.length == 0) {
            Log.e(this.TAG, "submitRequestList - Empty/null requests are not allowed");
            throw new ServiceSpecificException(LegacyExceptionUtils.BAD_VALUE, "submitRequestList - Empty/null requests are not allowed");
        }
        try {
            List<Long> surfaceIds = this.mConfiguredSurfaces == null ? new ArrayList<>() : getSurfaceIds(this.mConfiguredSurfaces);
            for (CaptureRequest request : requestList) {
                if (request.getTargets().isEmpty()) {
                    Log.e(this.TAG, "submitRequestList - Each request must have at least one Surface target");
                    throw new ServiceSpecificException(LegacyExceptionUtils.BAD_VALUE, "submitRequestList - Each request must have at least one Surface target");
                }
                for (Surface surface : request.getTargets()) {
                    if (surface == null) {
                        Log.e(this.TAG, "submitRequestList - Null Surface targets are not allowed");
                        throw new ServiceSpecificException(LegacyExceptionUtils.BAD_VALUE, "submitRequestList - Null Surface targets are not allowed");
                    } else if (this.mConfiguredSurfaces == null) {
                        Log.e(this.TAG, "submitRequestList - must configure  device with valid surfaces before submitting requests");
                        throw new ServiceSpecificException(LegacyExceptionUtils.INVALID_OPERATION, "submitRequestList - must configure  device with valid surfaces before submitting requests");
                    } else if (!containsSurfaceId(surface, surfaceIds)) {
                        Log.e(this.TAG, "submitRequestList - cannot use a surface that wasn't configured");
                        throw new ServiceSpecificException(LegacyExceptionUtils.BAD_VALUE, "submitRequestList - cannot use a surface that wasn't configured");
                    }
                }
            }
            this.mIdle.close();
            return this.mRequestThreadManager.submitCaptureRequests(requestList, repeating);
        } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new ServiceSpecificException(LegacyExceptionUtils.BAD_VALUE, "submitRequestList - configured surface is abandoned.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized SubmitInfo submitRequest(CaptureRequest request, boolean repeating) {
        CaptureRequest[] requestList = {request};
        return submitRequestList(requestList, repeating);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long cancelRequest(int requestId) {
        return this.mRequestThreadManager.cancelRepeating(requestId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void waitUntilIdle() {
        this.mIdle.block();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long flush() {
        long lastFrame = this.mRequestThreadManager.flush();
        waitUntilIdle();
        return lastFrame;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isClosed() {
        return this.mClosed;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mRequestThreadManager.quit();
        this.mCallbackHandlerThread.quitSafely();
        this.mResultThread.quitSafely();
        try {
            this.mCallbackHandlerThread.join();
        } catch (InterruptedException e) {
            Log.e(this.TAG, String.format("Thread %s (%d) interrupted while quitting.", this.mCallbackHandlerThread.getName(), Long.valueOf(this.mCallbackHandlerThread.getId())));
        }
        try {
            this.mResultThread.join();
        } catch (InterruptedException e2) {
            Log.e(this.TAG, String.format("Thread %s (%d) interrupted while quitting.", this.mResultThread.getName(), Long.valueOf(this.mResultThread.getId())));
        }
        this.mClosed = true;
    }

    protected void finalize() throws Throwable {
        try {
            try {
                close();
            } catch (ServiceSpecificException e) {
                String str = this.TAG;
                Log.e(str, "Got error while trying to finalize, ignoring: " + e.getMessage());
            }
        } finally {
            super.finalize();
        }
    }

    public private protected static synchronized long findEuclidDistSquare(Size a, Size b) {
        long d0 = a.getWidth() - b.getWidth();
        long d1 = a.getHeight() - b.getHeight();
        return (d0 * d0) + (d1 * d1);
    }

    public private protected static synchronized Size findClosestSize(Size size, Size[] supportedSizes) {
        if (size == null || supportedSizes == null) {
            return null;
        }
        Size bestSize = null;
        for (Size s : supportedSizes) {
            if (s.equals(size)) {
                return size;
            }
            if (s.getWidth() <= MAX_DIMEN_FOR_ROUNDING && (bestSize == null || findEuclidDistSquare(size, s) < findEuclidDistSquare(bestSize, s))) {
                bestSize = s;
            }
        }
        return bestSize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized Size getSurfaceSize(Surface surface) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        int[] dimens = new int[2];
        LegacyExceptionUtils.throwOnError(nativeDetectSurfaceDimens(surface, dimens));
        return new Size(dimens[0], dimens[1]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isFlexibleConsumer(Surface output) {
        int usageFlags = detectSurfaceUsageFlags(output);
        return (usageFlags & 1114112) == 0 && (usageFlags & 2307) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isPreviewConsumer(Surface output) {
        int usageFlags = detectSurfaceUsageFlags(output);
        boolean previewConsumer = (usageFlags & 1114115) == 0 && (usageFlags & 2816) != 0;
        try {
            detectSurfaceType(output);
            return previewConsumer;
        } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new IllegalArgumentException("Surface was abandoned", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isVideoEncoderConsumer(Surface output) {
        int usageFlags = detectSurfaceUsageFlags(output);
        boolean videoEncoderConsumer = (usageFlags & 1050883) == 0 && (usageFlags & 65536) != 0;
        try {
            detectSurfaceType(output);
            return videoEncoderConsumer;
        } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new IllegalArgumentException("Surface was abandoned", e);
        }
    }

    public private protected static synchronized int detectSurfaceUsageFlags(Surface surface) {
        Preconditions.checkNotNull(surface);
        return nativeDetectSurfaceUsageFlags(surface);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized int detectSurfaceType(Surface surface) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        int surfaceType = nativeDetectSurfaceType(surface);
        if (surfaceType >= 1 && surfaceType <= 5) {
            surfaceType = 34;
        }
        return LegacyExceptionUtils.throwOnError(surfaceType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized int detectSurfaceDataspace(Surface surface) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        return LegacyExceptionUtils.throwOnError(nativeDetectSurfaceDataspace(surface));
    }

    public private protected static synchronized void connectSurface(Surface surface) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        LegacyExceptionUtils.throwOnError(nativeConnectSurface(surface));
    }

    public private protected static synchronized void disconnectSurface(Surface surface) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        if (surface == null) {
            return;
        }
        LegacyExceptionUtils.throwOnError(nativeDisconnectSurface(surface));
    }

    public private protected static synchronized void produceFrame(Surface surface, byte[] pixelBuffer, int width, int height, int pixelFormat) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        Preconditions.checkNotNull(pixelBuffer);
        Preconditions.checkArgumentPositive(width, "width must be positive.");
        Preconditions.checkArgumentPositive(height, "height must be positive.");
        LegacyExceptionUtils.throwOnError(nativeProduceFrame(surface, pixelBuffer, width, height, pixelFormat));
    }

    public private protected static synchronized void setSurfaceFormat(Surface surface, int pixelFormat) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        LegacyExceptionUtils.throwOnError(nativeSetSurfaceFormat(surface, pixelFormat));
    }

    public private protected static synchronized void setSurfaceDimens(Surface surface, int width, int height) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        Preconditions.checkArgumentPositive(width, "width must be positive.");
        Preconditions.checkArgumentPositive(height, "height must be positive.");
        LegacyExceptionUtils.throwOnError(nativeSetSurfaceDimens(surface, width, height));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized long getSurfaceId(Surface surface) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        try {
            return nativeGetSurfaceId(surface);
        } catch (IllegalArgumentException e) {
            throw new LegacyExceptionUtils.BufferQueueAbandonedException();
        }
    }

    public private protected static synchronized List<Long> getSurfaceIds(SparseArray<Surface> surfaces) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        if (surfaces == null) {
            throw new NullPointerException("Null argument surfaces");
        }
        List<Long> surfaceIds = new ArrayList<>();
        int count = surfaces.size();
        for (int i = 0; i < count; i++) {
            long id = getSurfaceId(surfaces.valueAt(i));
            if (id == 0) {
                throw new IllegalStateException("Configured surface had null native GraphicBufferProducer pointer!");
            }
            surfaceIds.add(Long.valueOf(id));
        }
        return surfaceIds;
    }

    public private protected static synchronized List<Long> getSurfaceIds(Collection<Surface> surfaces) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        if (surfaces == null) {
            throw new NullPointerException("Null argument surfaces");
        }
        List<Long> surfaceIds = new ArrayList<>();
        for (Surface s : surfaces) {
            long id = getSurfaceId(s);
            if (id == 0) {
                throw new IllegalStateException("Configured surface had null native GraphicBufferProducer pointer!");
            }
            surfaceIds.add(Long.valueOf(id));
        }
        return surfaceIds;
    }

    public private protected static synchronized boolean containsSurfaceId(Surface s, Collection<Long> ids) {
        try {
            long id = getSurfaceId(s);
            return ids.contains(Long.valueOf(id));
        } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
            return false;
        }
    }

    public private protected static synchronized void setSurfaceOrientation(Surface surface, int facing, int sensorOrientation) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        LegacyExceptionUtils.throwOnError(nativeSetSurfaceOrientation(surface, facing, sensorOrientation));
    }

    public private protected static synchronized Size getTextureSize(SurfaceTexture surfaceTexture) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surfaceTexture);
        int[] dimens = new int[2];
        LegacyExceptionUtils.throwOnError(nativeDetectTextureDimens(surfaceTexture, dimens));
        return new Size(dimens[0], dimens[1]);
    }

    public private protected static synchronized void setNextTimestamp(Surface surface, long timestamp) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        LegacyExceptionUtils.throwOnError(nativeSetNextTimestamp(surface, timestamp));
    }

    public private protected static synchronized void setScalingMode(Surface surface, int mode) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        Preconditions.checkNotNull(surface);
        LegacyExceptionUtils.throwOnError(nativeSetScalingMode(surface, mode));
    }
}
