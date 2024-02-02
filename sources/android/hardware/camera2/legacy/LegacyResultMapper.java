package android.hardware.camera2.legacy;

import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.legacy.ParameterUtils;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.utils.ParamsUtils;
import android.location.Location;
import android.util.Log;
import android.util.Size;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class LegacyResultMapper {
    public protected static final boolean DEBUG = false;
    public protected static final String TAG = "LegacyResultMapper";
    public protected LegacyRequest mCachedRequest = null;
    public protected CameraMetadataNative mCachedResult = null;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized CameraMetadataNative cachedConvertResultMetadata(LegacyRequest legacyRequest, long timestamp) {
        CameraMetadataNative result;
        if (this.mCachedRequest != null && legacyRequest.parameters.same(this.mCachedRequest.parameters) && legacyRequest.captureRequest.equals((Object) this.mCachedRequest.captureRequest)) {
            result = new CameraMetadataNative(this.mCachedResult);
        } else {
            result = convertResultMetadata(legacyRequest);
            this.mCachedRequest = legacyRequest;
            this.mCachedResult = new CameraMetadataNative(result);
        }
        result.set((CaptureResult.Key<CaptureResult.Key<Long>>) CaptureResult.SENSOR_TIMESTAMP, (CaptureResult.Key<Long>) Long.valueOf(timestamp));
        return result;
    }

    public protected static synchronized CameraMetadataNative convertResultMetadata(LegacyRequest legacyRequest) {
        CameraCharacteristics characteristics = legacyRequest.characteristics;
        CaptureRequest request = legacyRequest.captureRequest;
        Size previewSize = legacyRequest.previewSize;
        Camera.Parameters params = legacyRequest.parameters;
        CameraMetadataNative result = new CameraMetadataNative();
        Rect activeArraySize = (Rect) characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        ParameterUtils.ZoomData zoomData = ParameterUtils.convertScalerCropRegion(activeArraySize, (Rect) request.get(CaptureRequest.SCALER_CROP_REGION), previewSize, params);
        result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.COLOR_CORRECTION_ABERRATION_MODE, (CaptureResult.Key<Integer>) ((Integer) request.get(CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE)));
        mapAe(result, characteristics, request, activeArraySize, zoomData, params);
        mapAf(result, activeArraySize, zoomData, params);
        mapAwb(result, params);
        int i = 1;
        int captureIntent = ((Integer) ParamsUtils.getOrDefault(request, CaptureRequest.CONTROL_CAPTURE_INTENT, 1)).intValue();
        result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_CAPTURE_INTENT, (CaptureResult.Key<Integer>) Integer.valueOf(LegacyRequestMapper.filterSupportedCaptureIntent(captureIntent)));
        int controlMode = ((Integer) ParamsUtils.getOrDefault(request, CaptureRequest.CONTROL_MODE, 1)).intValue();
        if (controlMode != 2) {
            result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_MODE, (CaptureResult.Key<Integer>) 1);
        } else {
            result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_MODE, (CaptureResult.Key<Integer>) 2);
        }
        String legacySceneMode = params.getSceneMode();
        int mode = LegacyMetadataMapper.convertSceneModeFromLegacy(legacySceneMode);
        if (mode != -1) {
            result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_SCENE_MODE, (CaptureResult.Key<Integer>) Integer.valueOf(mode));
        } else {
            Log.w(TAG, "Unknown scene mode " + legacySceneMode + " returned by camera HAL, setting to disabled.");
            result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_SCENE_MODE, (CaptureResult.Key<Integer>) 0);
        }
        String legacyEffectMode = params.getColorEffect();
        int mode2 = LegacyMetadataMapper.convertEffectModeFromLegacy(legacyEffectMode);
        if (mode2 != -1) {
            result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_EFFECT_MODE, (CaptureResult.Key<Integer>) Integer.valueOf(mode2));
        } else {
            Log.w(TAG, "Unknown effect mode " + legacyEffectMode + " returned by camera HAL, setting to off.");
            result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_EFFECT_MODE, (CaptureResult.Key<Integer>) 0);
        }
        if (!params.isVideoStabilizationSupported() || !params.getVideoStabilization()) {
            i = 0;
        }
        int stabMode = i;
        result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_VIDEO_STABILIZATION_MODE, (CaptureResult.Key<Integer>) Integer.valueOf(stabMode));
        if (Camera.Parameters.FOCUS_MODE_INFINITY.equals(params.getFocusMode())) {
            result.set((CaptureResult.Key<CaptureResult.Key<Float>>) CaptureResult.LENS_FOCUS_DISTANCE, (CaptureResult.Key<Float>) Float.valueOf(0.0f));
        }
        result.set((CaptureResult.Key<CaptureResult.Key<Float>>) CaptureResult.LENS_FOCAL_LENGTH, (CaptureResult.Key<Float>) Float.valueOf(params.getFocalLength()));
        result.set((CaptureResult.Key<CaptureResult.Key<Byte>>) CaptureResult.REQUEST_PIPELINE_DEPTH, (CaptureResult.Key<Byte>) ((Byte) characteristics.get(CameraCharacteristics.REQUEST_PIPELINE_MAX_DEPTH)));
        mapScaler(result, zoomData, params);
        result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.SENSOR_TEST_PATTERN_MODE, (CaptureResult.Key<Integer>) 0);
        result.set((CaptureResult.Key<CaptureResult.Key<Location>>) CaptureResult.JPEG_GPS_LOCATION, (CaptureResult.Key<Location>) ((Location) request.get(CaptureRequest.JPEG_GPS_LOCATION)));
        result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.JPEG_ORIENTATION, (CaptureResult.Key<Integer>) ((Integer) request.get(CaptureRequest.JPEG_ORIENTATION)));
        result.set((CaptureResult.Key<CaptureResult.Key<Byte>>) CaptureResult.JPEG_QUALITY, (CaptureResult.Key<Byte>) Byte.valueOf((byte) params.getJpegQuality()));
        result.set((CaptureResult.Key<CaptureResult.Key<Byte>>) CaptureResult.JPEG_THUMBNAIL_QUALITY, (CaptureResult.Key<Byte>) Byte.valueOf((byte) params.getJpegThumbnailQuality()));
        Camera.Size s = params.getJpegThumbnailSize();
        if (s != null) {
            result.set((CaptureResult.Key<CaptureResult.Key<Size>>) CaptureResult.JPEG_THUMBNAIL_SIZE, (CaptureResult.Key<Size>) ParameterUtils.convertSize(s));
        } else {
            Log.w(TAG, "Null thumbnail size received from parameters.");
        }
        result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.NOISE_REDUCTION_MODE, (CaptureResult.Key<Integer>) ((Integer) request.get(CaptureRequest.NOISE_REDUCTION_MODE)));
        return result;
    }

    public protected static synchronized void mapAe(CameraMetadataNative m, CameraCharacteristics characteristics, CaptureRequest request, Rect activeArray, ParameterUtils.ZoomData zoomData, Camera.Parameters p) {
        int antiBandingMode = LegacyMetadataMapper.convertAntiBandingModeOrDefault(p.getAntibanding());
        m.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_AE_ANTIBANDING_MODE, (CaptureResult.Key<Integer>) Integer.valueOf(antiBandingMode));
        m.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION, (CaptureResult.Key<Integer>) Integer.valueOf(p.getExposureCompensation()));
        boolean lock = p.isAutoExposureLockSupported() ? p.getAutoExposureLock() : false;
        m.set((CaptureResult.Key<CaptureResult.Key<Boolean>>) CaptureResult.CONTROL_AE_LOCK, (CaptureResult.Key<Boolean>) Boolean.valueOf(lock));
        Boolean requestLock = (Boolean) request.get(CaptureRequest.CONTROL_AE_LOCK);
        if (requestLock != null && requestLock.booleanValue() != lock) {
            Log.w(TAG, "mapAe - android.control.aeLock was requested to " + requestLock + " but resulted in " + lock);
        }
        mapAeAndFlashMode(m, characteristics, p);
        if (p.getMaxNumMeteringAreas() > 0) {
            MeteringRectangle[] meteringRectArray = getMeteringRectangles(activeArray, zoomData, p.getMeteringAreas(), "AE");
            m.set((CaptureResult.Key<CaptureResult.Key<MeteringRectangle[]>>) CaptureResult.CONTROL_AE_REGIONS, (CaptureResult.Key<MeteringRectangle[]>) meteringRectArray);
        }
    }

    public protected static synchronized void mapAf(CameraMetadataNative m, Rect activeArray, ParameterUtils.ZoomData zoomData, Camera.Parameters p) {
        m.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_AF_MODE, (CaptureResult.Key<Integer>) Integer.valueOf(convertLegacyAfMode(p.getFocusMode())));
        if (p.getMaxNumFocusAreas() > 0) {
            MeteringRectangle[] meteringRectArray = getMeteringRectangles(activeArray, zoomData, p.getFocusAreas(), "AF");
            m.set((CaptureResult.Key<CaptureResult.Key<MeteringRectangle[]>>) CaptureResult.CONTROL_AF_REGIONS, (CaptureResult.Key<MeteringRectangle[]>) meteringRectArray);
        }
    }

    public protected static synchronized void mapAwb(CameraMetadataNative m, Camera.Parameters p) {
        boolean lock = p.isAutoWhiteBalanceLockSupported() ? p.getAutoWhiteBalanceLock() : false;
        m.set((CaptureResult.Key<CaptureResult.Key<Boolean>>) CaptureResult.CONTROL_AWB_LOCK, (CaptureResult.Key<Boolean>) Boolean.valueOf(lock));
        int awbMode = convertLegacyAwbMode(p.getWhiteBalance());
        m.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_AWB_MODE, (CaptureResult.Key<Integer>) Integer.valueOf(awbMode));
    }

    public protected static synchronized MeteringRectangle[] getMeteringRectangles(Rect activeArray, ParameterUtils.ZoomData zoomData, List<Camera.Area> meteringAreaList, String regionName) {
        List<MeteringRectangle> meteringRectList = new ArrayList<>();
        if (meteringAreaList != null) {
            for (Camera.Area area : meteringAreaList) {
                ParameterUtils.WeightedRectangle rect = ParameterUtils.convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, area);
                meteringRectList.add(rect.toMetering());
            }
        }
        return (MeteringRectangle[]) meteringRectList.toArray(new MeteringRectangle[0]);
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0063, code lost:
        if (r4.equals("off") != false) goto L19;
     */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x009f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public protected static synchronized void mapAeAndFlashMode(android.hardware.camera2.impl.CameraMetadataNative r9, android.hardware.camera2.CameraCharacteristics r10, android.hardware.Camera.Parameters r11) {
        /*
            r0 = 0
            android.hardware.camera2.CameraCharacteristics$Key<java.lang.Boolean> r1 = android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE
            java.lang.Object r1 = r10.get(r1)
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            boolean r1 = r1.booleanValue()
            r2 = 0
            if (r1 == 0) goto L12
            r1 = 0
            goto L16
        L12:
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)
        L16:
            r3 = 1
            java.lang.String r4 = r11.getFlashMode()
            if (r4 == 0) goto La2
            r5 = -1
            int r6 = r4.hashCode()
            r7 = 3551(0xddf, float:4.976E-42)
            r8 = 3
            if (r6 == r7) goto L66
            r7 = 109935(0x1ad6f, float:1.54052E-40)
            if (r6 == r7) goto L5c
            r2 = 3005871(0x2dddaf, float:4.212122E-39)
            if (r6 == r2) goto L52
            r2 = 110547964(0x696d3fc, float:5.673521E-35)
            if (r6 == r2) goto L47
            r2 = 1081542389(0x407706f5, float:3.8597996)
            if (r6 == r2) goto L3c
            goto L71
        L3c:
            java.lang.String r2 = "red-eye"
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L71
            r2 = r8
            goto L72
        L47:
            java.lang.String r2 = "torch"
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L71
            r2 = 4
            goto L72
        L52:
            java.lang.String r2 = "auto"
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L71
            r2 = 1
            goto L72
        L5c:
            java.lang.String r6 = "off"
            boolean r6 = r4.equals(r6)
            if (r6 == 0) goto L71
            goto L72
        L66:
            java.lang.String r2 = "on"
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L71
            r2 = 2
            goto L72
        L71:
            r2 = r5
        L72:
            switch(r2) {
                case 0: goto La1;
                case 1: goto L9f;
                case 2: goto L98;
                case 3: goto L96;
                case 4: goto L90;
                default: goto L75;
            }
        L75:
            java.lang.String r2 = "LegacyResultMapper"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "mapAeAndFlashMode - Ignoring unknown flash mode "
            r5.append(r6)
            java.lang.String r6 = r11.getFlashMode()
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            android.util.Log.w(r2, r5)
            goto La2
        L90:
            r0 = 2
            java.lang.Integer r1 = java.lang.Integer.valueOf(r8)
            goto La2
        L96:
            r3 = 4
            goto La2
        L98:
            r0 = 1
            r3 = 3
            java.lang.Integer r1 = java.lang.Integer.valueOf(r8)
            goto La2
        L9f:
            r3 = 2
            goto La2
        La1:
        La2:
            android.hardware.camera2.CaptureResult$Key<java.lang.Integer> r2 = android.hardware.camera2.CaptureResult.FLASH_STATE
            r9.set(r2, r1)
            android.hardware.camera2.CaptureResult$Key<java.lang.Integer> r2 = android.hardware.camera2.CaptureResult.FLASH_MODE
            java.lang.Integer r5 = java.lang.Integer.valueOf(r0)
            r9.set(r2, r5)
            android.hardware.camera2.CaptureResult$Key<java.lang.Integer> r2 = android.hardware.camera2.CaptureResult.CONTROL_AE_MODE
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)
            r9.set(r2, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.LegacyResultMapper.mapAeAndFlashMode(android.hardware.camera2.impl.CameraMetadataNative, android.hardware.camera2.CameraCharacteristics, android.hardware.Camera$Parameters):void");
    }

    public protected static synchronized int convertLegacyAfMode(String mode) {
        if (mode == null) {
            Log.w(TAG, "convertLegacyAfMode - no AF mode, default to OFF");
            return 0;
        }
        char c = 65535;
        switch (mode.hashCode()) {
            case -194628547:
                if (mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    c = 2;
                    break;
                }
                break;
            case 3005871:
                if (mode.equals("auto")) {
                    c = 0;
                    break;
                }
                break;
            case 3108534:
                if (mode.equals(Camera.Parameters.FOCUS_MODE_EDOF)) {
                    c = 3;
                    break;
                }
                break;
            case 97445748:
                if (mode.equals(Camera.Parameters.FOCUS_MODE_FIXED)) {
                    c = 5;
                    break;
                }
                break;
            case 103652300:
                if (mode.equals(Camera.Parameters.FOCUS_MODE_MACRO)) {
                    c = 4;
                    break;
                }
                break;
            case 173173288:
                if (mode.equals(Camera.Parameters.FOCUS_MODE_INFINITY)) {
                    c = 6;
                    break;
                }
                break;
            case 910005312:
                if (mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 1;
            case 1:
                return 4;
            case 2:
                return 3;
            case 3:
                return 5;
            case 4:
                return 2;
            case 5:
                return 0;
            case 6:
                return 0;
            default:
                Log.w(TAG, "convertLegacyAfMode - unknown mode " + mode + " , ignoring");
                return 0;
        }
    }

    public protected static synchronized int convertLegacyAwbMode(String mode) {
        if (mode == null) {
            return 1;
        }
        char c = 65535;
        switch (mode.hashCode()) {
            case -939299377:
                if (mode.equals(Camera.Parameters.WHITE_BALANCE_INCANDESCENT)) {
                    c = 1;
                    break;
                }
                break;
            case -719316704:
                if (mode.equals(Camera.Parameters.WHITE_BALANCE_WARM_FLUORESCENT)) {
                    c = 3;
                    break;
                }
                break;
            case 3005871:
                if (mode.equals("auto")) {
                    c = 0;
                    break;
                }
                break;
            case 109399597:
                if (mode.equals(Camera.Parameters.WHITE_BALANCE_SHADE)) {
                    c = 7;
                    break;
                }
                break;
            case 474934723:
                if (mode.equals(Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT)) {
                    c = 5;
                    break;
                }
                break;
            case 1650323088:
                if (mode.equals(Camera.Parameters.WHITE_BALANCE_TWILIGHT)) {
                    c = 6;
                    break;
                }
                break;
            case 1902580840:
                if (mode.equals(Camera.Parameters.WHITE_BALANCE_FLUORESCENT)) {
                    c = 2;
                    break;
                }
                break;
            case 1942983418:
                if (mode.equals(Camera.Parameters.WHITE_BALANCE_DAYLIGHT)) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
            default:
                Log.w(TAG, "convertAwbMode - unrecognized WB mode " + mode);
                return 1;
        }
    }

    public protected static synchronized void mapScaler(CameraMetadataNative m, ParameterUtils.ZoomData zoomData, Camera.Parameters p) {
        m.set((CaptureResult.Key<CaptureResult.Key<Rect>>) CaptureResult.SCALER_CROP_REGION, (CaptureResult.Key<Rect>) zoomData.reportedCrop);
    }
}
