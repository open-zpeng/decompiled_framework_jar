package android.filterfw.format;

import android.filterfw.core.MutableFrameFormat;
/* loaded from: classes.dex */
public class PrimitiveFormat {
    public static synchronized MutableFrameFormat createByteFormat(int count, int target) {
        return createFormat(2, count, target);
    }

    public static synchronized MutableFrameFormat createInt16Format(int count, int target) {
        return createFormat(3, count, target);
    }

    public static synchronized MutableFrameFormat createInt32Format(int count, int target) {
        return createFormat(4, count, target);
    }

    public static synchronized MutableFrameFormat createFloatFormat(int count, int target) {
        return createFormat(5, count, target);
    }

    public static synchronized MutableFrameFormat createDoubleFormat(int count, int target) {
        return createFormat(6, count, target);
    }

    public static synchronized MutableFrameFormat createByteFormat(int target) {
        return createFormat(2, target);
    }

    public static synchronized MutableFrameFormat createInt16Format(int target) {
        return createFormat(3, target);
    }

    public static synchronized MutableFrameFormat createInt32Format(int target) {
        return createFormat(4, target);
    }

    public static synchronized MutableFrameFormat createFloatFormat(int target) {
        return createFormat(5, target);
    }

    public static synchronized MutableFrameFormat createDoubleFormat(int target) {
        return createFormat(6, target);
    }

    private static synchronized MutableFrameFormat createFormat(int baseType, int count, int target) {
        MutableFrameFormat result = new MutableFrameFormat(baseType, target);
        result.setDimensions(count);
        return result;
    }

    private static synchronized MutableFrameFormat createFormat(int baseType, int target) {
        MutableFrameFormat result = new MutableFrameFormat(baseType, target);
        result.setDimensionCount(1);
        return result;
    }
}
