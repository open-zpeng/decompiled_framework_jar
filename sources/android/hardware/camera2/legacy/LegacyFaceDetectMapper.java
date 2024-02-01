package android.hardware.camera2.legacy;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.legacy.ParameterUtils;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.utils.ParamsUtils;
import android.util.Log;
import android.util.Size;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class LegacyFaceDetectMapper {
    public protected static final boolean DEBUG = false;
    public protected static String TAG = "LegacyFaceDetectMapper";
    public protected final Camera mCamera;
    public protected final boolean mFaceDetectSupported;
    public protected Camera.Face[] mFaces;
    public protected Camera.Face[] mFacesPrev;
    public protected boolean mFaceDetectEnabled = false;
    public protected boolean mFaceDetectScenePriority = false;
    public protected boolean mFaceDetectReporting = false;
    public protected final Object mLock = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LegacyFaceDetectMapper(Camera camera, CameraCharacteristics characteristics) {
        this.mCamera = (Camera) Preconditions.checkNotNull(camera, "camera must not be null");
        Preconditions.checkNotNull(characteristics, "characteristics must not be null");
        this.mFaceDetectSupported = ArrayUtils.contains((int[]) characteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES), 1);
        if (!this.mFaceDetectSupported) {
            return;
        }
        this.mCamera.setFaceDetectionListener(new Camera.FaceDetectionListener() { // from class: android.hardware.camera2.legacy.LegacyFaceDetectMapper.1
            @Override // android.hardware.Camera.FaceDetectionListener
            public void onFaceDetection(Camera.Face[] faces, Camera camera2) {
                int lengthFaces = faces == null ? 0 : faces.length;
                synchronized (LegacyFaceDetectMapper.this.mLock) {
                    if (LegacyFaceDetectMapper.this.mFaceDetectEnabled) {
                        LegacyFaceDetectMapper.this.mFaces = faces;
                    } else if (lengthFaces > 0) {
                        Log.d(LegacyFaceDetectMapper.TAG, "onFaceDetection - Ignored some incoming faces sinceface detection was disabled");
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processFaceDetectMode(CaptureRequest captureRequest, Camera.Parameters parameters) {
        boolean enableFaceDetect;
        boolean z;
        Preconditions.checkNotNull(captureRequest, "captureRequest must not be null");
        boolean z2 = false;
        int fdMode = ((Integer) ParamsUtils.getOrDefault(captureRequest, CaptureRequest.STATISTICS_FACE_DETECT_MODE, 0)).intValue();
        if (fdMode != 0 && !this.mFaceDetectSupported) {
            Log.w(TAG, "processFaceDetectMode - Ignoring statistics.faceDetectMode; face detection is not available");
            return;
        }
        int sceneMode = ((Integer) ParamsUtils.getOrDefault(captureRequest, CaptureRequest.CONTROL_SCENE_MODE, 0)).intValue();
        if (sceneMode == 1 && !this.mFaceDetectSupported) {
            Log.w(TAG, "processFaceDetectMode - ignoring control.sceneMode == FACE_PRIORITY; face detection is not available");
            return;
        }
        switch (fdMode) {
            case 0:
            case 1:
                break;
            default:
                Log.w(TAG, "processFaceDetectMode - ignoring unknown statistics.faceDetectMode = " + fdMode);
                return;
            case 2:
                Log.w(TAG, "processFaceDetectMode - statistics.faceDetectMode == FULL unsupported, downgrading to SIMPLE");
                break;
        }
        if (fdMode == 0 && sceneMode != 1) {
            enableFaceDetect = false;
        } else {
            enableFaceDetect = true;
        }
        synchronized (this.mLock) {
            if (enableFaceDetect != this.mFaceDetectEnabled) {
                if (enableFaceDetect) {
                    this.mCamera.startFaceDetection();
                } else {
                    this.mCamera.stopFaceDetection();
                    this.mFaces = null;
                }
                this.mFaceDetectEnabled = enableFaceDetect;
                if (sceneMode != 1) {
                    z = false;
                } else {
                    z = true;
                }
                this.mFaceDetectScenePriority = z;
                if (fdMode != 0) {
                    z2 = true;
                }
                this.mFaceDetectReporting = z2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void mapResultFaces(CameraMetadataNative result, LegacyRequest legacyRequest) {
        int fdMode;
        Camera.Face[] faces;
        boolean fdScenePriority;
        Preconditions.checkNotNull(result, "result must not be null");
        Preconditions.checkNotNull(legacyRequest, "legacyRequest must not be null");
        synchronized (this.mLock) {
            fdMode = this.mFaceDetectReporting ? 1 : 0;
            if (this.mFaceDetectReporting) {
                faces = this.mFaces;
            } else {
                faces = null;
            }
            fdScenePriority = this.mFaceDetectScenePriority;
            Camera.Face[] faceArr = this.mFacesPrev;
            this.mFacesPrev = faces;
        }
        CameraCharacteristics characteristics = legacyRequest.characteristics;
        CaptureRequest request = legacyRequest.captureRequest;
        Size previewSize = legacyRequest.previewSize;
        Camera.Parameters params = legacyRequest.parameters;
        Rect activeArray = (Rect) characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        ParameterUtils.ZoomData zoomData = ParameterUtils.convertScalerCropRegion(activeArray, (Rect) request.get(CaptureRequest.SCALER_CROP_REGION), previewSize, params);
        List<Face> convertedFaces = new ArrayList<>();
        if (faces != null) {
            for (Camera.Face face : faces) {
                if (face != null) {
                    convertedFaces.add(ParameterUtils.convertFaceFromLegacy(face, activeArray, zoomData));
                } else {
                    Log.w(TAG, "mapResultFaces - read NULL face from camera1 device");
                }
            }
        }
        result.set((CaptureResult.Key<CaptureResult.Key<Face[]>>) CaptureResult.STATISTICS_FACES, (CaptureResult.Key<Face[]>) ((Face[]) convertedFaces.toArray(new Face[0])));
        result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.STATISTICS_FACE_DETECT_MODE, (CaptureResult.Key<Integer>) Integer.valueOf(fdMode));
        if (fdScenePriority) {
            result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_SCENE_MODE, (CaptureResult.Key<Integer>) 1);
        }
    }
}
