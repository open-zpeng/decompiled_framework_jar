package android.hardware.camera2.legacy;

import android.app.Instrumentation;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.utils.ParamsUtils;
import android.media.tv.TvContract;
import android.net.wifi.WifiEnterpriseConfig;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class ParameterUtils {
    public protected static final double ASPECT_RATIO_TOLERANCE = 0.05000000074505806d;
    public protected static final boolean DEBUG = false;
    private protected static final int NORMALIZED_RECTANGLE_MAX = 1000;
    private protected static final int NORMALIZED_RECTANGLE_MIN = -1000;
    public protected static final String TAG = "ParameterUtils";
    public protected static final int ZOOM_RATIO_MULTIPLIER = 100;
    private protected static final Rect NORMALIZED_RECTANGLE_DEFAULT = new Rect(-1000, -1000, 1000, 1000);
    private protected static final Camera.Area CAMERA_AREA_DEFAULT = new Camera.Area(new Rect(NORMALIZED_RECTANGLE_DEFAULT), 1);
    private protected static final Rect RECTANGLE_EMPTY = new Rect(0, 0, 0, 0);

    /* loaded from: classes.dex */
    public static class ZoomData {
        private protected final Rect previewCrop;
        private protected final Rect reportedCrop;
        private protected final int zoomIndex;

        private protected synchronized ZoomData(int zoomIndex, Rect previewCrop, Rect reportedCrop) {
            this.zoomIndex = zoomIndex;
            this.previewCrop = previewCrop;
            this.reportedCrop = reportedCrop;
        }
    }

    /* loaded from: classes.dex */
    public static class MeteringData {
        private protected final Camera.Area meteringArea;
        private protected final Rect previewMetering;
        private protected final Rect reportedMetering;

        private protected synchronized MeteringData(Camera.Area meteringArea, Rect previewMetering, Rect reportedMetering) {
            this.meteringArea = meteringArea;
            this.previewMetering = previewMetering;
            this.reportedMetering = reportedMetering;
        }
    }

    /* loaded from: classes.dex */
    public static class WeightedRectangle {
        private protected final Rect rect;
        private protected final int weight;

        private protected synchronized WeightedRectangle(Rect rect, int weight) {
            this.rect = (Rect) Preconditions.checkNotNull(rect, "rect must not be null");
            this.weight = weight;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized MeteringRectangle toMetering() {
            int weight = clip(this.weight, 0, 1000, this.rect, TvContract.PreviewPrograms.COLUMN_WEIGHT);
            int x = clipLower(this.rect.left, 0, this.rect, "left");
            int y = clipLower(this.rect.top, 0, this.rect, "top");
            int w = clipLower(this.rect.width(), 0, this.rect, "width");
            int h = clipLower(this.rect.height(), 0, this.rect, "height");
            return new MeteringRectangle(x, y, w, h, weight);
        }

        private protected synchronized Face toFace(int id, Point leftEyePosition, Point rightEyePosition, Point mouthPosition) {
            int idSafe = clipLower(id, 0, this.rect, Instrumentation.REPORT_KEY_IDENTIFIER);
            int score = clip(this.weight, 1, 100, this.rect, "score");
            return new Face(this.rect, score, idSafe, leftEyePosition, rightEyePosition, mouthPosition);
        }

        private protected synchronized Face toFace() {
            int score = clip(this.weight, 1, 100, this.rect, "score");
            return new Face(this.rect, score);
        }

        public protected static synchronized int clipLower(int value, int lo, Rect rect, String name) {
            return clip(value, lo, Integer.MAX_VALUE, rect, name);
        }

        public protected static synchronized int clip(int value, int lo, int hi, Rect rect, String name) {
            if (value < lo) {
                Log.w(ParameterUtils.TAG, "toMetering - Rectangle " + rect + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + name + " too small, clip to " + lo);
                return lo;
            } else if (value > hi) {
                Log.w(ParameterUtils.TAG, "toMetering - Rectangle " + rect + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + name + " too small, clip to " + hi);
                return hi;
            } else {
                return value;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized Size convertSize(Camera.Size size) {
        Preconditions.checkNotNull(size, "size must not be null");
        return new Size(size.width, size.height);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized List<Size> convertSizeList(List<Camera.Size> sizeList) {
        Preconditions.checkNotNull(sizeList, "sizeList must not be null");
        List<Size> sizes = new ArrayList<>(sizeList.size());
        for (Camera.Size s : sizeList) {
            sizes.add(new Size(s.width, s.height));
        }
        return sizes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized Size[] convertSizeListToArray(List<Camera.Size> sizeList) {
        Preconditions.checkNotNull(sizeList, "sizeList must not be null");
        Size[] array = new Size[sizeList.size()];
        int ctr = 0;
        for (Camera.Size s : sizeList) {
            array[ctr] = new Size(s.width, s.height);
            ctr++;
        }
        return array;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean containsSize(List<Camera.Size> sizeList, int width, int height) {
        Preconditions.checkNotNull(sizeList, "sizeList must not be null");
        for (Camera.Size s : sizeList) {
            if (s.height == height && s.width == width) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized Size getLargestSupportedJpegSizeByArea(Camera.Parameters params) {
        Preconditions.checkNotNull(params, "params must not be null");
        List<Size> supportedJpegSizes = convertSizeList(params.getSupportedPictureSizes());
        return android.hardware.camera2.utils.SizeAreaComparator.findLargestByArea(supportedJpegSizes);
    }

    private protected static synchronized String stringFromArea(Camera.Area area) {
        if (area == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Rect r = area.rect;
        sb.setLength(0);
        sb.append("([");
        sb.append(r.left);
        sb.append(',');
        sb.append(r.top);
        sb.append("][");
        sb.append(r.right);
        sb.append(',');
        sb.append(r.bottom);
        sb.append(']');
        sb.append(',');
        sb.append(area.weight);
        sb.append(')');
        return sb.toString();
    }

    private protected static synchronized String stringFromAreaList(List<Camera.Area> areaList) {
        StringBuilder sb = new StringBuilder();
        if (areaList == null) {
            return null;
        }
        int i = 0;
        for (Camera.Area area : areaList) {
            if (area == null) {
                sb.append("null");
            } else {
                sb.append(stringFromArea(area));
            }
            if (i != areaList.size() - 1) {
                sb.append(", ");
            }
            i++;
        }
        return sb.toString();
    }

    private protected static synchronized int getClosestAvailableZoomCrop(Camera.Parameters params, Rect activeArray, Size streamSize, Rect cropRegion, Rect reportedCropRegion, Rect previewCropRegion) {
        Rect actualCrop;
        boolean isBest;
        Preconditions.checkNotNull(params, "params must not be null");
        Preconditions.checkNotNull(activeArray, "activeArray must not be null");
        Preconditions.checkNotNull(streamSize, "streamSize must not be null");
        Preconditions.checkNotNull(reportedCropRegion, "reportedCropRegion must not be null");
        Preconditions.checkNotNull(previewCropRegion, "previewCropRegion must not be null");
        Rect actualCrop2 = new Rect(cropRegion);
        if (!actualCrop2.intersect(activeArray)) {
            Log.w(TAG, "getClosestAvailableZoomCrop - Crop region out of range; setting to active array size");
            actualCrop2.set(activeArray);
        }
        Rect previewCrop = getPreviewCropRectangleUnzoomed(activeArray, streamSize);
        Rect cropRegionAsPreview = shrinkToSameAspectRatioCentered(previewCrop, actualCrop2);
        int bestZoomIndex = -1;
        List<Rect> availableReportedCropRegions = getAvailableZoomCropRectangles(params, activeArray);
        List<Rect> availablePreviewCropRegions = getAvailablePreviewZoomCropRectangles(params, activeArray, streamSize);
        if (availableReportedCropRegions.size() != availablePreviewCropRegions.size()) {
            throw new AssertionError("available reported/preview crop region size mismatch");
        }
        Rect bestPreviewCropRegion = null;
        Rect bestReportedCropRegion = null;
        int i = 0;
        while (i < availableReportedCropRegions.size()) {
            Rect currentPreviewCropRegion = availablePreviewCropRegions.get(i);
            Rect currentReportedCropRegion = availableReportedCropRegions.get(i);
            if (bestZoomIndex != -1) {
                actualCrop = actualCrop2;
                if (currentPreviewCropRegion.width() >= cropRegionAsPreview.width() && currentPreviewCropRegion.height() >= cropRegionAsPreview.height()) {
                    isBest = true;
                } else {
                    isBest = false;
                }
            } else {
                actualCrop = actualCrop2;
                isBest = true;
            }
            if (!isBest) {
                break;
            }
            bestPreviewCropRegion = currentPreviewCropRegion;
            bestReportedCropRegion = currentReportedCropRegion;
            bestZoomIndex = i;
            i++;
            actualCrop2 = actualCrop;
        }
        if (bestZoomIndex == -1) {
            throw new AssertionError("Should've found at least one valid zoom index");
        }
        reportedCropRegion.set(bestReportedCropRegion);
        previewCropRegion.set(bestPreviewCropRegion);
        return bestZoomIndex;
    }

    public protected static synchronized Rect getPreviewCropRectangleUnzoomed(Rect activeArray, Size previewSize) {
        float cropW;
        float cropH;
        if (previewSize.getWidth() > activeArray.width()) {
            throw new IllegalArgumentException("previewSize must not be wider than activeArray");
        }
        if (previewSize.getHeight() > activeArray.height()) {
            throw new IllegalArgumentException("previewSize must not be taller than activeArray");
        }
        float aspectRatioArray = (activeArray.width() * 1.0f) / activeArray.height();
        float aspectRatioPreview = (previewSize.getWidth() * 1.0f) / previewSize.getHeight();
        if (Math.abs(aspectRatioPreview - aspectRatioArray) < 0.05000000074505806d) {
            cropH = activeArray.height();
            cropW = activeArray.width();
        } else if (aspectRatioPreview < aspectRatioArray) {
            cropH = activeArray.height();
            cropW = cropH * aspectRatioPreview;
        } else {
            cropW = activeArray.width();
            cropH = cropW / aspectRatioPreview;
        }
        Matrix translateMatrix = new Matrix();
        RectF cropRect = new RectF(0.0f, 0.0f, cropW, cropH);
        translateMatrix.setTranslate(activeArray.exactCenterX(), activeArray.exactCenterY());
        translateMatrix.postTranslate(-cropRect.centerX(), -cropRect.centerY());
        translateMatrix.mapRect(cropRect);
        return ParamsUtils.createRect(cropRect);
    }

    public protected static synchronized Rect shrinkToSameAspectRatioCentered(Rect reference, Rect shrinkTarget) {
        float cropW;
        float cropH;
        float aspectRatioReference = (reference.width() * 1.0f) / reference.height();
        float aspectRatioShrinkTarget = (shrinkTarget.width() * 1.0f) / shrinkTarget.height();
        if (aspectRatioShrinkTarget < aspectRatioReference) {
            cropH = reference.height();
            cropW = cropH * aspectRatioShrinkTarget;
        } else {
            cropW = reference.width();
            cropH = cropW / aspectRatioShrinkTarget;
        }
        Matrix translateMatrix = new Matrix();
        RectF shrunkRect = new RectF(shrinkTarget);
        translateMatrix.setScale(cropW / reference.width(), cropH / reference.height(), shrinkTarget.exactCenterX(), shrinkTarget.exactCenterY());
        translateMatrix.mapRect(shrunkRect);
        return ParamsUtils.createRect(shrunkRect);
    }

    private protected static synchronized List<Rect> getAvailableZoomCropRectangles(Camera.Parameters params, Rect activeArray) {
        Preconditions.checkNotNull(params, "params must not be null");
        Preconditions.checkNotNull(activeArray, "activeArray must not be null");
        return getAvailableCropRectangles(params, activeArray, ParamsUtils.createSize(activeArray));
    }

    private protected static synchronized List<Rect> getAvailablePreviewZoomCropRectangles(Camera.Parameters params, Rect activeArray, Size previewSize) {
        Preconditions.checkNotNull(params, "params must not be null");
        Preconditions.checkNotNull(activeArray, "activeArray must not be null");
        Preconditions.checkNotNull(previewSize, "previewSize must not be null");
        return getAvailableCropRectangles(params, activeArray, previewSize);
    }

    public protected static synchronized List<Rect> getAvailableCropRectangles(Camera.Parameters params, Rect activeArray, Size streamSize) {
        Preconditions.checkNotNull(params, "params must not be null");
        Preconditions.checkNotNull(activeArray, "activeArray must not be null");
        Preconditions.checkNotNull(streamSize, "streamSize must not be null");
        Rect unzoomedStreamCrop = getPreviewCropRectangleUnzoomed(activeArray, streamSize);
        if (!params.isZoomSupported()) {
            return new ArrayList(Arrays.asList(unzoomedStreamCrop));
        }
        List<Rect> zoomCropRectangles = new ArrayList<>(params.getMaxZoom() + 1);
        Matrix scaleMatrix = new Matrix();
        RectF scaledRect = new RectF();
        for (Integer num : params.getZoomRatios()) {
            int zoom = num.intValue();
            float shrinkRatio = 100.0f / zoom;
            ParamsUtils.convertRectF(unzoomedStreamCrop, scaledRect);
            scaleMatrix.setScale(shrinkRatio, shrinkRatio, activeArray.exactCenterX(), activeArray.exactCenterY());
            scaleMatrix.mapRect(scaledRect);
            Rect intRect = ParamsUtils.createRect(scaledRect);
            zoomCropRectangles.add(intRect);
        }
        return zoomCropRectangles;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized float getMaxZoomRatio(Camera.Parameters params) {
        if (params.isZoomSupported()) {
            List<Integer> zoomRatios = params.getZoomRatios();
            int zoom = zoomRatios.get(zoomRatios.size() - 1).intValue();
            float zoomRatio = (zoom * 1.0f) / 100.0f;
            return zoomRatio;
        }
        return 1.0f;
    }

    public protected static synchronized SizeF getZoomRatio(Size activeArraySize, Size cropSize) {
        Preconditions.checkNotNull(activeArraySize, "activeArraySize must not be null");
        Preconditions.checkNotNull(cropSize, "cropSize must not be null");
        Preconditions.checkArgumentPositive(cropSize.getWidth(), "cropSize.width must be positive");
        Preconditions.checkArgumentPositive(cropSize.getHeight(), "cropSize.height must be positive");
        float zoomRatioWidth = (activeArraySize.getWidth() * 1.0f) / cropSize.getWidth();
        float zoomRatioHeight = (activeArraySize.getHeight() * 1.0f) / cropSize.getHeight();
        return new SizeF(zoomRatioWidth, zoomRatioHeight);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized ZoomData convertScalerCropRegion(Rect activeArraySize, Rect cropRegion, Size previewSize, Camera.Parameters params) {
        Rect activeArraySizeOnly = new Rect(0, 0, activeArraySize.width(), activeArraySize.height());
        Rect userCropRegion = cropRegion;
        if (userCropRegion == null) {
            userCropRegion = activeArraySizeOnly;
        }
        Rect userCropRegion2 = userCropRegion;
        Rect reportedCropRegion = new Rect();
        Rect previewCropRegion = new Rect();
        int zoomIdx = getClosestAvailableZoomCrop(params, activeArraySizeOnly, previewSize, userCropRegion2, reportedCropRegion, previewCropRegion);
        return new ZoomData(zoomIdx, previewCropRegion, reportedCropRegion);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized MeteringData convertMeteringRectangleToLegacy(Rect activeArray, MeteringRectangle meteringRect, ZoomData zoomData) {
        Camera.Area meteringArea;
        Rect previewCrop = zoomData.previewCrop;
        float scaleW = 2000.0f / previewCrop.width();
        float scaleH = 2000.0f / previewCrop.height();
        Matrix transform = new Matrix();
        transform.setTranslate(-previewCrop.left, -previewCrop.top);
        transform.postScale(scaleW, scaleH);
        transform.postTranslate(-1000.0f, -1000.0f);
        Rect normalizedRegionUnbounded = ParamsUtils.mapRect(transform, meteringRect.getRect());
        Rect normalizedIntersected = new Rect(normalizedRegionUnbounded);
        if (!normalizedIntersected.intersect(NORMALIZED_RECTANGLE_DEFAULT)) {
            Log.w(TAG, "convertMeteringRectangleToLegacy - metering rectangle too small, no metering will be done");
            normalizedIntersected.set(RECTANGLE_EMPTY);
            meteringArea = new Camera.Area(RECTANGLE_EMPTY, 0);
        } else {
            meteringArea = new Camera.Area(normalizedIntersected, meteringRect.getMeteringWeight());
        }
        Rect previewMetering = meteringRect.getRect();
        if (!previewMetering.intersect(previewCrop)) {
            previewMetering.set(RECTANGLE_EMPTY);
        }
        Camera.Area normalizedAreaUnbounded = new Camera.Area(normalizedRegionUnbounded, meteringRect.getMeteringWeight());
        WeightedRectangle reportedMeteringRect = convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, normalizedAreaUnbounded, false);
        Rect reportedMetering = reportedMeteringRect.rect;
        return new MeteringData(meteringArea, previewMetering, reportedMetering);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized WeightedRectangle convertCameraAreaToActiveArrayRectangle(Rect activeArray, ZoomData zoomData, Camera.Area area) {
        return convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, area, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized Face convertFaceFromLegacy(Camera.Face face, Rect activeArray, ZoomData zoomData) {
        Preconditions.checkNotNull(face, "face must not be null");
        Camera.Area fakeArea = new Camera.Area(face.rect, 1);
        WeightedRectangle faceRect = convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, fakeArea);
        Point leftEye = face.leftEye;
        Point rightEye = face.rightEye;
        Point mouth = face.mouth;
        if (leftEye != null && rightEye != null && mouth != null && leftEye.x != -2000 && leftEye.y != -2000 && rightEye.x != -2000 && rightEye.y != -2000 && mouth.x != -2000 && mouth.y != -2000) {
            Point leftEye2 = convertCameraPointToActiveArrayPoint(activeArray, zoomData, leftEye, true);
            Face api2Face = faceRect.toFace(face.id, leftEye2, convertCameraPointToActiveArrayPoint(activeArray, zoomData, leftEye2, true), convertCameraPointToActiveArrayPoint(activeArray, zoomData, leftEye2, true));
            return api2Face;
        }
        Face api2Face2 = faceRect.toFace();
        return api2Face2;
    }

    public protected static synchronized Point convertCameraPointToActiveArrayPoint(Rect activeArray, ZoomData zoomData, Point point, boolean usePreviewCrop) {
        Rect pointedRect = new Rect(point.x, point.y, point.x, point.y);
        Camera.Area pointedArea = new Camera.Area(pointedRect, 1);
        WeightedRectangle adjustedRect = convertCameraAreaToActiveArrayRectangle(activeArray, zoomData, pointedArea, usePreviewCrop);
        Point transformedPoint = new Point(adjustedRect.rect.left, adjustedRect.rect.top);
        return transformedPoint;
    }

    public protected static synchronized WeightedRectangle convertCameraAreaToActiveArrayRectangle(Rect activeArray, ZoomData zoomData, Camera.Area area, boolean usePreviewCrop) {
        Rect previewCrop = zoomData.previewCrop;
        Rect reportedCrop = zoomData.reportedCrop;
        float scaleW = (previewCrop.width() * 1.0f) / 2000.0f;
        float scaleH = (previewCrop.height() * 1.0f) / 2000.0f;
        Matrix transform = new Matrix();
        transform.setTranslate(1000.0f, 1000.0f);
        transform.postScale(scaleW, scaleH);
        transform.postTranslate(previewCrop.left, previewCrop.top);
        Rect cropToIntersectAgainst = usePreviewCrop ? previewCrop : reportedCrop;
        Rect reportedMetering = ParamsUtils.mapRect(transform, area.rect);
        if (!reportedMetering.intersect(cropToIntersectAgainst)) {
            reportedMetering.set(RECTANGLE_EMPTY);
        }
        int weight = area.weight;
        if (weight < 0) {
            Log.w(TAG, "convertCameraAreaToMeteringRectangle - rectangle " + stringFromArea(area) + " has too small weight, clip to 0");
        }
        return new WeightedRectangle(reportedMetering, area.weight);
    }

    public protected synchronized ParameterUtils() {
        throw new AssertionError();
    }
}
