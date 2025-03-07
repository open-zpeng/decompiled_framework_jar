package android.hardware.camera2.utils;

import android.annotation.UnsupportedAppUsage;
import android.hardware.camera2.legacy.LegacyCameraDevice;
import android.hardware.camera2.legacy.LegacyExceptionUtils;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class SurfaceUtils {
    public static boolean isSurfaceForPreview(Surface surface) {
        return LegacyCameraDevice.isPreviewConsumer(surface);
    }

    public static boolean isSurfaceForHwVideoEncoder(Surface surface) {
        return LegacyCameraDevice.isVideoEncoderConsumer(surface);
    }

    public static long getSurfaceId(Surface surface) {
        try {
            return LegacyCameraDevice.getSurfaceId(surface);
        } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
            return 0L;
        }
    }

    @UnsupportedAppUsage
    public static Size getSurfaceSize(Surface surface) {
        try {
            return LegacyCameraDevice.getSurfaceSize(surface);
        } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new IllegalArgumentException("Surface was abandoned", e);
        }
    }

    public static int getSurfaceFormat(Surface surface) {
        try {
            return LegacyCameraDevice.detectSurfaceType(surface);
        } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new IllegalArgumentException("Surface was abandoned", e);
        }
    }

    public static int getSurfaceDataspace(Surface surface) {
        try {
            return LegacyCameraDevice.detectSurfaceDataspace(surface);
        } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
            throw new IllegalArgumentException("Surface was abandoned", e);
        }
    }

    public static boolean isFlexibleConsumer(Surface output) {
        return LegacyCameraDevice.isFlexibleConsumer(output);
    }

    private static void checkHighSpeedSurfaceFormat(Surface surface) {
        int surfaceFormat = getSurfaceFormat(surface);
        if (surfaceFormat != 34) {
            throw new IllegalArgumentException("Surface format(" + surfaceFormat + ") is not for preview or hardware video encoding!");
        }
    }

    public static void checkConstrainedHighSpeedSurfaces(Collection<Surface> surfaces, Range<Integer> fpsRange, StreamConfigurationMap config) {
        List<Size> highSpeedSizes;
        if (surfaces == null || surfaces.size() == 0 || surfaces.size() > 2) {
            throw new IllegalArgumentException("Output target surface list must not be null and the size must be 1 or 2");
        }
        if (fpsRange == null) {
            highSpeedSizes = Arrays.asList(config.getHighSpeedVideoSizes());
        } else {
            Range<Integer>[] highSpeedFpsRanges = config.getHighSpeedVideoFpsRanges();
            if (!Arrays.asList(highSpeedFpsRanges).contains(fpsRange)) {
                throw new IllegalArgumentException("Fps range " + fpsRange.toString() + " in the request is not a supported high speed fps range " + Arrays.toString(highSpeedFpsRanges));
            }
            highSpeedSizes = Arrays.asList(config.getHighSpeedVideoSizesFor(fpsRange));
        }
        for (Surface surface : surfaces) {
            checkHighSpeedSurfaceFormat(surface);
            Size surfaceSize = getSurfaceSize(surface);
            if (!highSpeedSizes.contains(surfaceSize)) {
                throw new IllegalArgumentException("Surface size " + surfaceSize.toString() + " is not part of the high speed supported size list " + Arrays.toString(highSpeedSizes.toArray()));
            } else if (!isSurfaceForPreview(surface) && !isSurfaceForHwVideoEncoder(surface)) {
                throw new IllegalArgumentException("This output surface is neither preview nor hardware video encoding surface");
            } else {
                if (isSurfaceForPreview(surface) && isSurfaceForHwVideoEncoder(surface)) {
                    throw new IllegalArgumentException("This output surface can not be both preview and hardware video encoding surface");
                }
            }
        }
        if (surfaces.size() == 2) {
            Iterator<Surface> iterator = surfaces.iterator();
            boolean isFirstSurfacePreview = isSurfaceForPreview(iterator.next());
            boolean isSecondSurfacePreview = isSurfaceForPreview(iterator.next());
            if (isFirstSurfacePreview == isSecondSurfacePreview) {
                throw new IllegalArgumentException("The 2 output surfaces must have different type");
            }
        }
    }
}
