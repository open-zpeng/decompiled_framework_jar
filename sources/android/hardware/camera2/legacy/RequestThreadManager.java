package android.hardware.camera2.legacy;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.legacy.LegacyExceptionUtils;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import com.xiaopeng.util.FeatureOption;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class RequestThreadManager {
    public protected static final float ASPECT_RATIO_TOLERANCE = 0.01f;
    public protected static final boolean DEBUG = false;
    public protected static final int JPEG_FRAME_TIMEOUT = 4000;
    public protected static final int MAX_IN_FLIGHT_REQUESTS = 2;
    public protected static final int MSG_CLEANUP = 3;
    public protected static final int MSG_CONFIGURE_OUTPUTS = 1;
    public protected static final int MSG_SUBMIT_CAPTURE_REQUEST = 2;
    public protected static final int PREVIEW_FRAME_TIMEOUT = 1000;
    public protected static final int REQUEST_COMPLETE_TIMEOUT = 4000;
    public protected static final boolean USE_BLOB_FORMAT_OVERRIDE = true;
    public protected static final boolean VERBOSE = false;
    public protected final String TAG;
    public protected Camera mCamera;
    public protected final int mCameraId;
    public protected final CaptureCollector mCaptureCollector;
    public protected final CameraCharacteristics mCharacteristics;
    public protected final CameraDeviceState mDeviceState;
    public protected Surface mDummySurface;
    public protected SurfaceTexture mDummyTexture;
    public protected final LegacyFaceDetectMapper mFaceDetectMapper;
    public protected final LegacyFocusStateMapper mFocusStateMapper;
    public protected GLThreadManager mGLThreadManager;
    public protected Size mIntermediateBufferSize;
    public protected Camera.Parameters mParams;
    public protected SurfaceTexture mPreviewTexture;
    public protected final RequestHandlerThread mRequestThread;
    public protected boolean mPreviewRunning = false;
    public protected final List<Surface> mPreviewOutputs = new ArrayList();
    public protected final List<Surface> mCallbackOutputs = new ArrayList();
    public protected final List<Long> mJpegSurfaceIds = new ArrayList();
    public protected final RequestQueue mRequestQueue = new RequestQueue(this.mJpegSurfaceIds);
    public protected LegacyRequest mLastRequest = null;
    public protected final Object mIdleLock = new Object();
    public protected final FpsCounter mPrevCounter = new FpsCounter("Incoming Preview");
    public protected final FpsCounter mRequestCounter = new FpsCounter("Incoming Requests");
    public protected final AtomicBoolean mQuit = new AtomicBoolean(false);
    public protected final Camera.ErrorCallback mErrorCallback = new Camera.ErrorCallback() { // from class: android.hardware.camera2.legacy.RequestThreadManager.1
        @Override // android.hardware.Camera.ErrorCallback
        public void onError(int i, Camera camera) {
            switch (i) {
                case 2:
                    RequestThreadManager.this.flush();
                    RequestThreadManager.this.mDeviceState.setError(0);
                    return;
                case 3:
                    RequestThreadManager.this.flush();
                    RequestThreadManager.this.mDeviceState.setError(6);
                    return;
                default:
                    String str = RequestThreadManager.this.TAG;
                    Log.e(str, "Received error " + i + " from the Camera1 ErrorCallback");
                    RequestThreadManager.this.mDeviceState.setError(1);
                    return;
            }
        }
    };
    public protected final ConditionVariable mReceivedJpeg = new ConditionVariable(false);
    public protected final Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() { // from class: android.hardware.camera2.legacy.RequestThreadManager.2
        @Override // android.hardware.Camera.PictureCallback
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.i(RequestThreadManager.this.TAG, "Received jpeg.");
            Pair<RequestHolder, Long> captureInfo = RequestThreadManager.this.mCaptureCollector.jpegProduced();
            if (captureInfo == null || captureInfo.first == null) {
                Log.e(RequestThreadManager.this.TAG, "Dropping jpeg frame.");
                return;
            }
            RequestHolder holder = captureInfo.first;
            long timestamp = captureInfo.second.longValue();
            for (Surface s : holder.getHolderTargets()) {
                try {
                    if (LegacyCameraDevice.containsSurfaceId(s, RequestThreadManager.this.mJpegSurfaceIds)) {
                        Log.i(RequestThreadManager.this.TAG, "Producing jpeg buffer...");
                        int totalSize = data.length + LegacyCameraDevice.nativeGetJpegFooterSize();
                        LegacyCameraDevice.setNextTimestamp(s, timestamp);
                        LegacyCameraDevice.setSurfaceFormat(s, 1);
                        int dimen = (((int) Math.ceil(Math.sqrt((totalSize + 3) & (-4)))) + 15) & (-16);
                        LegacyCameraDevice.setSurfaceDimens(s, dimen, dimen);
                        LegacyCameraDevice.produceFrame(s, data, dimen, dimen, 33);
                    }
                } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
                    Log.w(RequestThreadManager.this.TAG, "Surface abandoned, dropping frame. ", e);
                }
            }
            RequestThreadManager.this.mReceivedJpeg.open();
        }
    };
    public protected final Camera.ShutterCallback mJpegShutterCallback = new Camera.ShutterCallback() { // from class: android.hardware.camera2.legacy.RequestThreadManager.3
        @Override // android.hardware.Camera.ShutterCallback
        public void onShutter() {
            RequestThreadManager.this.mCaptureCollector.jpegCaptured(SystemClock.elapsedRealtimeNanos());
        }
    };
    public protected final SurfaceTexture.OnFrameAvailableListener mPreviewCallback = new SurfaceTexture.OnFrameAvailableListener() { // from class: android.hardware.camera2.legacy.RequestThreadManager.4
        @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            RequestThreadManager.this.mGLThreadManager.queueNewFrame();
        }
    };
    public protected final Handler.Callback mRequestHandlerCb = new Handler.Callback() { // from class: android.hardware.camera2.legacy.RequestThreadManager.5
        private boolean mCleanup = false;
        private final LegacyResultMapper mMapper = new LegacyResultMapper();

        /* JADX WARN: Removed duplicated region for block: B:123:0x03cf  */
        @Override // android.os.Handler.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean handleMessage(android.os.Message r25) {
            /*
                Method dump skipped, instructions count: 1146
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.RequestThreadManager.AnonymousClass5.handleMessage(android.os.Message):boolean");
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ConfigureHolder {
        private protected final ConditionVariable condition;
        private protected final Collection<Pair<Surface, Size>> surfaces;

        private protected synchronized ConfigureHolder(ConditionVariable condition, Collection<Pair<Surface, Size>> surfaces) {
            this.condition = condition;
            this.surfaces = surfaces;
        }
    }

    /* loaded from: classes.dex */
    public static class FpsCounter {
        public protected static final long NANO_PER_SECOND = 1000000000;
        public protected static final String TAG = "FpsCounter";
        public protected final String mStreamType;
        public protected int mFrameCount = 0;
        public protected long mLastTime = 0;
        public protected long mLastPrintTime = 0;
        public protected double mLastFps = FeatureOption.FO_BOOT_POLICY_CPU;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized FpsCounter(String streamType) {
            this.mStreamType = streamType;
        }

        private protected synchronized void countFrame() {
            this.mFrameCount++;
            long nextTime = SystemClock.elapsedRealtimeNanos();
            if (this.mLastTime == 0) {
                this.mLastTime = nextTime;
            }
            if (nextTime > this.mLastTime + 1000000000) {
                long elapsed = nextTime - this.mLastTime;
                this.mLastFps = this.mFrameCount * (1.0E9d / elapsed);
                this.mFrameCount = 0;
                this.mLastTime = nextTime;
            }
        }

        private protected synchronized double checkFps() {
            return this.mLastFps;
        }

        private protected synchronized void staggeredLog() {
            if (this.mLastTime > this.mLastPrintTime + 5000000000L) {
                this.mLastPrintTime = this.mLastTime;
                Log.d(TAG, "FPS for " + this.mStreamType + " stream: " + this.mLastFps);
            }
        }

        private protected synchronized void countAndLog() {
            countFrame();
            staggeredLog();
        }
    }

    public protected synchronized void createDummySurface() {
        if (this.mDummyTexture == null || this.mDummySurface == null) {
            this.mDummyTexture = new SurfaceTexture(0);
            this.mDummyTexture.setDefaultBufferSize(640, 480);
            this.mDummySurface = new Surface(this.mDummyTexture);
        }
    }

    public protected synchronized void stopPreview() {
        if (this.mPreviewRunning) {
            this.mCamera.stopPreview();
            this.mPreviewRunning = false;
        }
    }

    public protected synchronized void startPreview() {
        if (!this.mPreviewRunning) {
            this.mCamera.startPreview();
            this.mPreviewRunning = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized void doJpegCapturePrepare(RequestHolder request) throws IOException {
        if (!this.mPreviewRunning) {
            createDummySurface();
            this.mCamera.setPreviewTexture(this.mDummyTexture);
            startPreview();
        }
    }

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized void doJpegCapture(RequestHolder request) {
        this.mCamera.takePicture(this.mJpegShutterCallback, null, this.mJpegCallback);
        this.mPreviewRunning = false;
    }

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized void doPreviewCapture(RequestHolder request) throws IOException {
        if (this.mPreviewRunning) {
            return;
        }
        if (this.mPreviewTexture == null) {
            throw new IllegalStateException("Preview capture called with no preview surfaces configured.");
        }
        this.mPreviewTexture.setDefaultBufferSize(this.mIntermediateBufferSize.getWidth(), this.mIntermediateBufferSize.getHeight());
        this.mCamera.setPreviewTexture(this.mPreviewTexture);
        startPreview();
    }

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized void configureOutputs(Collection<Pair<Surface, Size>> outputs) {
        List<Size> previewOutputSizes;
        try {
            stopPreview();
            try {
                this.mCamera.setPreviewTexture(null);
            } catch (IOException e) {
                Log.w(this.TAG, "Failed to clear prior SurfaceTexture, may cause GL deadlock: ", e);
            } catch (RuntimeException e2) {
                Log.e(this.TAG, "Received device exception in configure call: ", e2);
                this.mDeviceState.setError(1);
                return;
            }
            if (this.mGLThreadManager != null) {
                this.mGLThreadManager.waitUntilStarted();
                this.mGLThreadManager.ignoreNewFrames();
                this.mGLThreadManager.waitUntilIdle();
            }
            resetJpegSurfaceFormats(this.mCallbackOutputs);
            for (Surface s : this.mCallbackOutputs) {
                try {
                    LegacyCameraDevice.disconnectSurface(s);
                } catch (LegacyExceptionUtils.BufferQueueAbandonedException e3) {
                    Log.w(this.TAG, "Surface abandoned, skipping...", e3);
                }
            }
            this.mPreviewOutputs.clear();
            this.mCallbackOutputs.clear();
            this.mJpegSurfaceIds.clear();
            this.mPreviewTexture = null;
            List<Size> previewOutputSizes2 = new ArrayList<>();
            List<Size> callbackOutputSizes = new ArrayList<>();
            int facing = ((Integer) this.mCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue();
            int orientation = ((Integer) this.mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
            if (outputs != null) {
                for (Pair<Surface, Size> outPair : outputs) {
                    Surface s2 = outPair.first;
                    Size outSize = outPair.second;
                    try {
                        int format = LegacyCameraDevice.detectSurfaceType(s2);
                        LegacyCameraDevice.setSurfaceOrientation(s2, facing, orientation);
                        if (format == 33) {
                            LegacyCameraDevice.setSurfaceFormat(s2, 1);
                            this.mJpegSurfaceIds.add(Long.valueOf(LegacyCameraDevice.getSurfaceId(s2)));
                            this.mCallbackOutputs.add(s2);
                            callbackOutputSizes.add(outSize);
                            LegacyCameraDevice.connectSurface(s2);
                        } else {
                            LegacyCameraDevice.setScalingMode(s2, 1);
                            this.mPreviewOutputs.add(s2);
                            previewOutputSizes2.add(outSize);
                        }
                    } catch (LegacyExceptionUtils.BufferQueueAbandonedException e4) {
                        Log.w(this.TAG, "Surface abandoned, skipping...", e4);
                    }
                }
            }
            try {
                this.mParams = this.mCamera.getParameters();
                List<int[]> supportedFpsRanges = this.mParams.getSupportedPreviewFpsRange();
                int[] bestRange = getPhotoPreviewFpsRange(supportedFpsRanges);
                this.mParams.setPreviewFpsRange(bestRange[0], bestRange[1]);
                Size smallestSupportedJpegSize = calculatePictureSize(this.mCallbackOutputs, callbackOutputSizes, this.mParams);
                if (previewOutputSizes2.size() > 0) {
                    Size bestPreviewDimen = android.hardware.camera2.utils.SizeAreaComparator.findLargestByArea(previewOutputSizes2);
                    Size largestJpegDimen = ParameterUtils.getLargestSupportedJpegSizeByArea(this.mParams);
                    Size chosenJpegDimen = smallestSupportedJpegSize != null ? smallestSupportedJpegSize : largestJpegDimen;
                    List<Size> supportedPreviewSizes = ParameterUtils.convertSizeList(this.mParams.getSupportedPreviewSizes());
                    long largestOutputArea = bestPreviewDimen.getHeight() * bestPreviewDimen.getWidth();
                    Size bestPreviewDimen2 = android.hardware.camera2.utils.SizeAreaComparator.findLargestByArea(supportedPreviewSizes);
                    Iterator<Size> it = supportedPreviewSizes.iterator();
                    while (it.hasNext()) {
                        Size largestOutput = it.next();
                        Size largestOutput2 = bestPreviewDimen;
                        Size s3 = largestOutput;
                        Iterator<Size> it2 = it;
                        List<int[]> supportedFpsRanges2 = supportedFpsRanges;
                        long currArea = s3.getWidth() * s3.getHeight();
                        List<Size> previewOutputSizes3 = previewOutputSizes2;
                        long bestArea = bestPreviewDimen2.getWidth() * bestPreviewDimen2.getHeight();
                        if (checkAspectRatiosMatch(chosenJpegDimen, s3) && currArea < bestArea && currArea >= largestOutputArea) {
                            bestPreviewDimen2 = s3;
                        }
                        bestPreviewDimen = largestOutput2;
                        it = it2;
                        supportedFpsRanges = supportedFpsRanges2;
                        previewOutputSizes2 = previewOutputSizes3;
                    }
                    previewOutputSizes = previewOutputSizes2;
                    this.mIntermediateBufferSize = bestPreviewDimen2;
                    this.mParams.setPreviewSize(this.mIntermediateBufferSize.getWidth(), this.mIntermediateBufferSize.getHeight());
                } else {
                    previewOutputSizes = previewOutputSizes2;
                    this.mIntermediateBufferSize = null;
                }
                if (smallestSupportedJpegSize != null) {
                    Log.i(this.TAG, "configureOutputs - set take picture size to " + smallestSupportedJpegSize);
                    this.mParams.setPictureSize(smallestSupportedJpegSize.getWidth(), smallestSupportedJpegSize.getHeight());
                }
                if (this.mGLThreadManager == null) {
                    this.mGLThreadManager = new GLThreadManager(this.mCameraId, facing, this.mDeviceState);
                    this.mGLThreadManager.start();
                }
                this.mGLThreadManager.waitUntilStarted();
                List<Pair<Surface, Size>> previews = new ArrayList<>();
                Iterator<Size> previewSizeIter = previewOutputSizes.iterator();
                for (Surface p : this.mPreviewOutputs) {
                    previews.add(new Pair<>(p, previewSizeIter.next()));
                }
                this.mGLThreadManager.setConfigurationAndWait(previews, this.mCaptureCollector);
                for (Surface p2 : this.mPreviewOutputs) {
                    try {
                        LegacyCameraDevice.setSurfaceOrientation(p2, facing, orientation);
                    } catch (LegacyExceptionUtils.BufferQueueAbandonedException e5) {
                        Log.e(this.TAG, "Surface abandoned, skipping setSurfaceOrientation()", e5);
                    }
                }
                this.mGLThreadManager.allowNewFrames();
                this.mPreviewTexture = this.mGLThreadManager.getCurrentSurfaceTexture();
                if (this.mPreviewTexture != null) {
                    this.mPreviewTexture.setOnFrameAvailableListener(this.mPreviewCallback);
                }
                try {
                    this.mCamera.setParameters(this.mParams);
                } catch (RuntimeException e6) {
                    Log.e(this.TAG, "Received device exception while configuring: ", e6);
                    this.mDeviceState.setError(1);
                }
            } catch (RuntimeException e7) {
                Log.e(this.TAG, "Received device exception: ", e7);
                this.mDeviceState.setError(1);
            }
        } catch (RuntimeException e8) {
            Log.e(this.TAG, "Received device exception in configure call: ", e8);
            this.mDeviceState.setError(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized void resetJpegSurfaceFormats(Collection<Surface> surfaces) {
        if (surfaces == null) {
            return;
        }
        for (Surface s : surfaces) {
            if (s == null || !s.isValid()) {
                Log.w(this.TAG, "Jpeg surface is invalid, skipping...");
            } else {
                try {
                    LegacyCameraDevice.setSurfaceFormat(s, 33);
                } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
                    Log.w(this.TAG, "Surface abandoned, skipping...", e);
                }
            }
        }
    }

    public protected synchronized Size calculatePictureSize(List<Surface> callbackOutputs, List<Size> callbackSizes, Camera.Parameters params) {
        if (callbackOutputs.size() != callbackSizes.size()) {
            throw new IllegalStateException("Input collections must be same length");
        }
        List<Size> configuredJpegSizes = new ArrayList<>();
        Iterator<Size> sizeIterator = callbackSizes.iterator();
        for (Surface callbackSurface : callbackOutputs) {
            Size jpegSize = sizeIterator.next();
            if (LegacyCameraDevice.containsSurfaceId(callbackSurface, this.mJpegSurfaceIds)) {
                configuredJpegSizes.add(jpegSize);
            }
        }
        if (!configuredJpegSizes.isEmpty()) {
            int maxConfiguredJpegWidth = -1;
            int maxConfiguredJpegHeight = -1;
            for (Size jpegSize2 : configuredJpegSizes) {
                maxConfiguredJpegWidth = jpegSize2.getWidth() > maxConfiguredJpegWidth ? jpegSize2.getWidth() : maxConfiguredJpegWidth;
                maxConfiguredJpegHeight = jpegSize2.getHeight() > maxConfiguredJpegHeight ? jpegSize2.getHeight() : maxConfiguredJpegHeight;
            }
            Size smallestBoundJpegSize = new Size(maxConfiguredJpegWidth, maxConfiguredJpegHeight);
            List<Size> supportedJpegSizes = ParameterUtils.convertSizeList(params.getSupportedPictureSizes());
            List<Size> candidateSupportedJpegSizes = new ArrayList<>();
            for (Size supportedJpegSize : supportedJpegSizes) {
                if (supportedJpegSize.getWidth() >= maxConfiguredJpegWidth && supportedJpegSize.getHeight() >= maxConfiguredJpegHeight) {
                    candidateSupportedJpegSizes.add(supportedJpegSize);
                }
            }
            if (candidateSupportedJpegSizes.isEmpty()) {
                throw new AssertionError("Could not find any supported JPEG sizes large enough to fit " + smallestBoundJpegSize);
            }
            Size smallestSupportedJpegSize = (Size) Collections.min(candidateSupportedJpegSizes, new android.hardware.camera2.utils.SizeAreaComparator());
            if (!smallestSupportedJpegSize.equals(smallestBoundJpegSize)) {
                Log.w(this.TAG, String.format("configureOutputs - Will need to crop picture %s into smallest bound size %s", smallestSupportedJpegSize, smallestBoundJpegSize));
            }
            return smallestSupportedJpegSize;
        }
        return null;
    }

    public protected static synchronized boolean checkAspectRatiosMatch(Size a, Size b) {
        float aAspect = a.getWidth() / a.getHeight();
        float bAspect = b.getWidth() / b.getHeight();
        return Math.abs(aAspect - bAspect) < 0.01f;
    }

    public protected synchronized int[] getPhotoPreviewFpsRange(List<int[]> frameRates) {
        if (frameRates.size() == 0) {
            Log.e(this.TAG, "No supported frame rates returned!");
            return null;
        }
        int bestMin = 0;
        int bestMax = 0;
        int bestIndex = 0;
        int index = 0;
        for (int[] rate : frameRates) {
            int minFps = rate[0];
            int maxFps = rate[1];
            if (maxFps > bestMax || (maxFps == bestMax && minFps > bestMin)) {
                bestMin = minFps;
                bestMax = maxFps;
                bestIndex = index;
            }
            index++;
        }
        return frameRates.get(bestIndex);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized RequestThreadManager(int cameraId, Camera camera, CameraCharacteristics characteristics, CameraDeviceState deviceState) {
        this.mCamera = (Camera) Preconditions.checkNotNull(camera, "camera must not be null");
        this.mCameraId = cameraId;
        this.mCharacteristics = (CameraCharacteristics) Preconditions.checkNotNull(characteristics, "characteristics must not be null");
        String name = String.format("RequestThread-%d", Integer.valueOf(cameraId));
        this.TAG = name;
        this.mDeviceState = (CameraDeviceState) Preconditions.checkNotNull(deviceState, "deviceState must not be null");
        this.mFocusStateMapper = new LegacyFocusStateMapper(this.mCamera);
        this.mFaceDetectMapper = new LegacyFaceDetectMapper(this.mCamera, this.mCharacteristics);
        this.mCaptureCollector = new CaptureCollector(2, this.mDeviceState);
        this.mRequestThread = new RequestHandlerThread(name, this.mRequestHandlerCb);
        this.mCamera.setDetailedErrorCallback(this.mErrorCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void start() {
        this.mRequestThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long flush() {
        Log.i(this.TAG, "Flushing all pending requests.");
        long lastFrame = this.mRequestQueue.stopRepeating();
        this.mCaptureCollector.failAll();
        return lastFrame;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void quit() {
        if (!this.mQuit.getAndSet(true)) {
            Handler handler = this.mRequestThread.waitAndGetHandler();
            handler.sendMessageAtFrontOfQueue(handler.obtainMessage(3));
            this.mRequestThread.quitSafely();
            try {
                this.mRequestThread.join();
            } catch (InterruptedException e) {
                Log.e(this.TAG, String.format("Thread %s (%d) interrupted while quitting.", this.mRequestThread.getName(), Long.valueOf(this.mRequestThread.getId())));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized SubmitInfo submitCaptureRequests(CaptureRequest[] requests, boolean repeating) {
        SubmitInfo info;
        Handler handler = this.mRequestThread.waitAndGetHandler();
        synchronized (this.mIdleLock) {
            info = this.mRequestQueue.submit(requests, repeating);
            handler.sendEmptyMessage(2);
        }
        return info;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long cancelRepeating(int requestId) {
        return this.mRequestQueue.stopRepeating(requestId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void configure(Collection<Pair<Surface, Size>> outputs) {
        Handler handler = this.mRequestThread.waitAndGetHandler();
        ConditionVariable condition = new ConditionVariable(false);
        ConfigureHolder holder = new ConfigureHolder(condition, outputs);
        handler.sendMessage(handler.obtainMessage(1, 0, 0, holder));
        condition.block();
    }
}
