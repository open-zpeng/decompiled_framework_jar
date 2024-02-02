package android.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.util.Pair;
import com.android.internal.logging.nano.MetricsProto;
import com.xiaopeng.util.FeatureOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
/* loaded from: classes.dex */
public abstract class ColorSpace {
    public static final int MAX_ID = 63;
    public static final int MIN_ID = -1;
    private final int mId;
    private final Model mModel;
    private final String mName;
    public static final float[] ILLUMINANT_A = {0.44757f, 0.40745f};
    public static final float[] ILLUMINANT_B = {0.34842f, 0.35161f};
    public static final float[] ILLUMINANT_C = {0.31006f, 0.31616f};
    public static final float[] ILLUMINANT_D50 = {0.34567f, 0.3585f};
    public static final float[] ILLUMINANT_D55 = {0.33242f, 0.34743f};
    public static final float[] ILLUMINANT_D60 = {0.32168f, 0.33767f};
    public static final float[] ILLUMINANT_D65 = {0.31271f, 0.32902f};
    public static final float[] ILLUMINANT_D75 = {0.29902f, 0.31485f};
    public static final float[] ILLUMINANT_E = {0.33333f, 0.33333f};
    private static final float[] SRGB_PRIMARIES = {0.64f, 0.33f, 0.3f, 0.6f, 0.15f, 0.06f};
    private static final float[] NTSC_1953_PRIMARIES = {0.67f, 0.33f, 0.21f, 0.71f, 0.14f, 0.08f};
    private static final float[] ILLUMINANT_D50_XYZ = {0.964212f, 1.0f, 0.825188f};
    private static final ColorSpace[] sNamedColorSpaces = new ColorSpace[Named.values().length];

    /* loaded from: classes.dex */
    public enum Named {
        SRGB,
        LINEAR_SRGB,
        EXTENDED_SRGB,
        LINEAR_EXTENDED_SRGB,
        BT709,
        BT2020,
        DCI_P3,
        DISPLAY_P3,
        NTSC_1953,
        SMPTE_C,
        ADOBE_RGB,
        PRO_PHOTO_RGB,
        ACES,
        ACESCG,
        CIE_XYZ,
        CIE_LAB
    }

    /* loaded from: classes.dex */
    public enum RenderIntent {
        PERCEPTUAL,
        RELATIVE,
        SATURATION,
        ABSOLUTE
    }

    public abstract float[] fromXyz(float[] fArr);

    public abstract float getMaxValue(int i);

    public abstract float getMinValue(int i);

    public abstract boolean isWideGamut();

    public abstract float[] toXyz(float[] fArr);

    static {
        sNamedColorSpaces[Named.SRGB.ordinal()] = new Rgb("sRGB IEC61966-2.1", SRGB_PRIMARIES, ILLUMINANT_D65, new Rgb.TransferParameters(0.9478672985781991d, 0.05213270142180095d, 0.07739938080495357d, 0.04045d, 2.4d), Named.SRGB.ordinal());
        sNamedColorSpaces[Named.LINEAR_SRGB.ordinal()] = new Rgb("sRGB IEC61966-2.1 (Linear)", SRGB_PRIMARIES, ILLUMINANT_D65, 1.0d, 0.0f, 1.0f, Named.LINEAR_SRGB.ordinal());
        sNamedColorSpaces[Named.EXTENDED_SRGB.ordinal()] = new Rgb("scRGB-nl IEC 61966-2-2:2003", SRGB_PRIMARIES, ILLUMINANT_D65, new DoubleUnaryOperator() { // from class: android.graphics.-$$Lambda$ColorSpace$BNp-1CyCzsQzfE-Ads9uc4rJDfw
            @Override // java.util.function.DoubleUnaryOperator
            public final double applyAsDouble(double d) {
                double absRcpResponse;
                absRcpResponse = ColorSpace.absRcpResponse(d, 0.9478672985781991d, 0.05213270142180095d, 0.07739938080495357d, 0.04045d, 2.4d);
                return absRcpResponse;
            }
        }, new DoubleUnaryOperator() { // from class: android.graphics.-$$Lambda$ColorSpace$S2rlqJvkXGTpUF6mZhvkElds8JE
            @Override // java.util.function.DoubleUnaryOperator
            public final double applyAsDouble(double d) {
                double absResponse;
                absResponse = ColorSpace.absResponse(d, 0.9478672985781991d, 0.05213270142180095d, 0.07739938080495357d, 0.04045d, 2.4d);
                return absResponse;
            }
        }, -0.799f, 2.399f, Named.EXTENDED_SRGB.ordinal());
        sNamedColorSpaces[Named.LINEAR_EXTENDED_SRGB.ordinal()] = new Rgb("scRGB IEC 61966-2-2:2003", SRGB_PRIMARIES, ILLUMINANT_D65, 1.0d, -0.5f, 7.499f, Named.LINEAR_EXTENDED_SRGB.ordinal());
        sNamedColorSpaces[Named.BT709.ordinal()] = new Rgb("Rec. ITU-R BT.709-5", new float[]{0.64f, 0.33f, 0.3f, 0.6f, 0.15f, 0.06f}, ILLUMINANT_D65, new Rgb.TransferParameters(0.9099181073703367d, 0.09008189262966333d, 0.2222222222222222d, 0.081d, 2.2222222222222223d), Named.BT709.ordinal());
        sNamedColorSpaces[Named.BT2020.ordinal()] = new Rgb("Rec. ITU-R BT.2020-1", new float[]{0.708f, 0.292f, 0.17f, 0.797f, 0.131f, 0.046f}, ILLUMINANT_D65, new Rgb.TransferParameters(0.9096697898662786d, 0.09033021013372146d, 0.2222222222222222d, 0.08145d, 2.2222222222222223d), Named.BT2020.ordinal());
        sNamedColorSpaces[Named.DCI_P3.ordinal()] = new Rgb("SMPTE RP 431-2-2007 DCI (P3)", new float[]{0.68f, 0.32f, 0.265f, 0.69f, 0.15f, 0.06f}, new float[]{0.314f, 0.351f}, 2.6d, 0.0f, 1.0f, Named.DCI_P3.ordinal());
        sNamedColorSpaces[Named.DISPLAY_P3.ordinal()] = new Rgb("Display P3", new float[]{0.68f, 0.32f, 0.265f, 0.69f, 0.15f, 0.06f}, ILLUMINANT_D65, new Rgb.TransferParameters(0.9478672985781991d, 0.05213270142180095d, 0.07739938080495357d, 0.039d, 2.4d), Named.DISPLAY_P3.ordinal());
        sNamedColorSpaces[Named.NTSC_1953.ordinal()] = new Rgb("NTSC (1953)", NTSC_1953_PRIMARIES, ILLUMINANT_C, new Rgb.TransferParameters(0.9099181073703367d, 0.09008189262966333d, 0.2222222222222222d, 0.081d, 2.2222222222222223d), Named.NTSC_1953.ordinal());
        sNamedColorSpaces[Named.SMPTE_C.ordinal()] = new Rgb("SMPTE-C RGB", new float[]{0.63f, 0.34f, 0.31f, 0.595f, 0.155f, 0.07f}, ILLUMINANT_D65, new Rgb.TransferParameters(0.9099181073703367d, 0.09008189262966333d, 0.2222222222222222d, 0.081d, 2.2222222222222223d), Named.SMPTE_C.ordinal());
        sNamedColorSpaces[Named.ADOBE_RGB.ordinal()] = new Rgb("Adobe RGB (1998)", new float[]{0.64f, 0.33f, 0.21f, 0.71f, 0.15f, 0.06f}, ILLUMINANT_D65, 2.2d, 0.0f, 1.0f, Named.ADOBE_RGB.ordinal());
        sNamedColorSpaces[Named.PRO_PHOTO_RGB.ordinal()] = new Rgb("ROMM RGB ISO 22028-2:2013", new float[]{0.7347f, 0.2653f, 0.1596f, 0.8404f, 0.0366f, 1.0E-4f}, ILLUMINANT_D50, new Rgb.TransferParameters(1.0d, FeatureOption.FO_BOOT_POLICY_CPU, 0.0625d, 0.031248d, 1.8d), Named.PRO_PHOTO_RGB.ordinal());
        sNamedColorSpaces[Named.ACES.ordinal()] = new Rgb("SMPTE ST 2065-1:2012 ACES", new float[]{0.7347f, 0.2653f, 0.0f, 1.0f, 1.0E-4f, -0.077f}, ILLUMINANT_D60, 1.0d, -65504.0f, 65504.0f, Named.ACES.ordinal());
        sNamedColorSpaces[Named.ACESCG.ordinal()] = new Rgb("Academy S-2014-004 ACEScg", new float[]{0.713f, 0.293f, 0.165f, 0.83f, 0.128f, 0.044f}, ILLUMINANT_D60, 1.0d, -65504.0f, 65504.0f, Named.ACESCG.ordinal());
        sNamedColorSpaces[Named.CIE_XYZ.ordinal()] = new Xyz("Generic XYZ", Named.CIE_XYZ.ordinal());
        sNamedColorSpaces[Named.CIE_LAB.ordinal()] = new Lab("Generic L*a*b*", Named.CIE_LAB.ordinal());
    }

    /* loaded from: classes.dex */
    public enum Adaptation {
        BRADFORD(new float[]{0.8951f, -0.7502f, 0.0389f, 0.2664f, 1.7135f, -0.0685f, -0.1614f, 0.0367f, 1.0296f}),
        VON_KRIES(new float[]{0.40024f, -0.2263f, 0.0f, 0.7076f, 1.16532f, 0.0f, -0.08081f, 0.0457f, 0.91822f}),
        CIECAT02(new float[]{0.7328f, -0.7036f, 0.003f, 0.4296f, 1.6975f, 0.0136f, -0.1624f, 0.0061f, 0.9834f});
        
        final float[] mTransform;

        Adaptation(float[] transform) {
            this.mTransform = transform;
        }
    }

    /* loaded from: classes.dex */
    public enum Model {
        RGB(3),
        XYZ(3),
        LAB(3),
        CMYK(4);
        
        private final int mComponentCount;

        Model(int componentCount) {
            this.mComponentCount = componentCount;
        }

        public int getComponentCount() {
            return this.mComponentCount;
        }
    }

    private synchronized ColorSpace(String name, Model model, int id) {
        if (name == null || name.length() < 1) {
            throw new IllegalArgumentException("The name of a color space cannot be null and must contain at least 1 character");
        }
        if (model == null) {
            throw new IllegalArgumentException("A color space must have a model");
        }
        if (id < -1 || id > 63) {
            throw new IllegalArgumentException("The id must be between -1 and 63");
        }
        this.mName = name;
        this.mModel = model;
        this.mId = id;
    }

    public String getName() {
        return this.mName;
    }

    public int getId() {
        return this.mId;
    }

    public Model getModel() {
        return this.mModel;
    }

    public int getComponentCount() {
        return this.mModel.getComponentCount();
    }

    public boolean isSrgb() {
        return false;
    }

    public float[] toXyz(float r, float g, float b) {
        return toXyz(new float[]{r, g, b});
    }

    public float[] fromXyz(float x, float y, float z) {
        float[] xyz = new float[this.mModel.getComponentCount()];
        xyz[0] = x;
        xyz[1] = y;
        xyz[2] = z;
        return fromXyz(xyz);
    }

    public String toString() {
        return this.mName + " (id=" + this.mId + ", model=" + this.mModel + ")";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ColorSpace that = (ColorSpace) o;
        if (this.mId == that.mId && this.mName.equals(that.mName) && this.mModel == that.mModel) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.mName.hashCode();
        return (31 * ((31 * result) + this.mModel.hashCode())) + this.mId;
    }

    public static Connector connect(ColorSpace source, ColorSpace destination) {
        return connect(source, destination, RenderIntent.PERCEPTUAL);
    }

    public static Connector connect(ColorSpace source, ColorSpace destination, RenderIntent intent) {
        if (source.equals(destination)) {
            return Connector.identity(source);
        }
        if (source.getModel() == Model.RGB && destination.getModel() == Model.RGB) {
            return new Connector.Rgb((Rgb) source, (Rgb) destination, intent);
        }
        return new Connector(source, destination, intent);
    }

    public static Connector connect(ColorSpace source) {
        return connect(source, RenderIntent.PERCEPTUAL);
    }

    public static Connector connect(ColorSpace source, RenderIntent intent) {
        if (source.isSrgb()) {
            return Connector.identity(source);
        }
        if (source.getModel() == Model.RGB) {
            return new Connector.Rgb((Rgb) source, (Rgb) get(Named.SRGB), intent);
        }
        return new Connector(source, get(Named.SRGB), intent);
    }

    public static ColorSpace adapt(ColorSpace colorSpace, float[] whitePoint) {
        return adapt(colorSpace, whitePoint, Adaptation.BRADFORD);
    }

    public static ColorSpace adapt(ColorSpace colorSpace, float[] whitePoint, Adaptation adaptation) {
        if (colorSpace.getModel() == Model.RGB) {
            Rgb rgb = (Rgb) colorSpace;
            if (compare(rgb.mWhitePoint, whitePoint)) {
                return colorSpace;
            }
            float[] xyz = whitePoint.length == 3 ? Arrays.copyOf(whitePoint, 3) : xyYToXyz(whitePoint);
            float[] adaptationTransform = chromaticAdaptation(adaptation.mTransform, xyYToXyz(rgb.getWhitePoint()), xyz);
            float[] transform = mul3x3(adaptationTransform, rgb.mTransform);
            return new Rgb(rgb, transform, whitePoint);
        }
        return colorSpace;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized ColorSpace get(int index) {
        if (index < 0 || index > Named.values().length) {
            throw new IllegalArgumentException("Invalid ID, must be in the range [0.." + Named.values().length + "]");
        }
        return sNamedColorSpaces[index];
    }

    public static ColorSpace get(Named name) {
        return sNamedColorSpaces[name.ordinal()];
    }

    public static ColorSpace match(float[] toXYZD50, Rgb.TransferParameters function) {
        ColorSpace[] colorSpaceArr;
        for (ColorSpace colorSpace : sNamedColorSpaces) {
            if (colorSpace.getModel() == Model.RGB) {
                Rgb rgb = (Rgb) adapt(colorSpace, ILLUMINANT_D50_XYZ);
                if (compare(toXYZD50, rgb.mTransform) && compare(function, rgb.mTransferParameters)) {
                    return colorSpace;
                }
            }
        }
        return null;
    }

    public static synchronized Renderer createRenderer() {
        return new Renderer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized double rcpResponse(double x, double a, double b, double c, double d, double g) {
        return x >= d * c ? (Math.pow(x, 1.0d / g) - b) / a : x / c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized double response(double x, double a, double b, double c, double d, double g) {
        return x >= d ? Math.pow((a * x) + b, g) : c * x;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized double rcpResponse(double x, double a, double b, double c, double d, double e, double f, double g) {
        return x >= d * c ? (Math.pow(x - e, 1.0d / g) - b) / a : (x - f) / c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized double response(double x, double a, double b, double c, double d, double e, double f, double g) {
        return x >= d ? Math.pow((a * x) + b, g) + e : (c * x) + f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized double absRcpResponse(double x, double a, double b, double c, double d, double g) {
        return Math.copySign(rcpResponse(x < FeatureOption.FO_BOOT_POLICY_CPU ? -x : x, a, b, c, d, g), x);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized double absResponse(double x, double a, double b, double c, double d, double g) {
        return Math.copySign(response(x < FeatureOption.FO_BOOT_POLICY_CPU ? -x : x, a, b, c, d, g), x);
    }

    private static synchronized boolean compare(Rgb.TransferParameters a, Rgb.TransferParameters b) {
        if (a == null && b == null) {
            return true;
        }
        return a != null && b != null && Math.abs(a.a - b.a) < 0.001d && Math.abs(a.b - b.b) < 0.001d && Math.abs(a.c - b.c) < 0.001d && Math.abs(a.d - b.d) < 0.002d && Math.abs(a.e - b.e) < 0.001d && Math.abs(a.f - b.f) < 0.001d && Math.abs(a.g - b.g) < 0.001d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean compare(float[] a, float[] b) {
        if (a == b) {
            return true;
        }
        for (int i = 0; i < a.length; i++) {
            if (Float.compare(a[i], b[i]) != 0 && Math.abs(a[i] - b[i]) > 0.001f) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized float[] inverse3x3(float[] m) {
        float a = m[0];
        float b = m[3];
        float c = m[6];
        float d = m[1];
        float e = m[4];
        float f = m[7];
        float g = m[2];
        float h = m[5];
        float i = m[8];
        float A = (e * i) - (f * h);
        float B = (f * g) - (d * i);
        float C = (d * h) - (e * g);
        float det = (a * A) + (b * B) + (c * C);
        float[] inverted = new float[m.length];
        inverted[0] = A / det;
        inverted[1] = B / det;
        inverted[2] = C / det;
        inverted[3] = ((c * h) - (b * i)) / det;
        inverted[4] = ((a * i) - (c * g)) / det;
        inverted[5] = ((b * g) - (a * h)) / det;
        inverted[6] = ((b * f) - (c * e)) / det;
        inverted[7] = ((c * d) - (a * f)) / det;
        inverted[8] = ((a * e) - (b * d)) / det;
        return inverted;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized float[] mul3x3(float[] lhs, float[] rhs) {
        float[] r = {(lhs[0] * rhs[0]) + (lhs[3] * rhs[1]) + (lhs[6] * rhs[2]), (lhs[1] * rhs[0]) + (lhs[4] * rhs[1]) + (lhs[7] * rhs[2]), (lhs[2] * rhs[0]) + (lhs[5] * rhs[1]) + (lhs[8] * rhs[2]), (lhs[0] * rhs[3]) + (lhs[3] * rhs[4]) + (lhs[6] * rhs[5]), (lhs[1] * rhs[3]) + (lhs[4] * rhs[4]) + (lhs[7] * rhs[5]), (lhs[2] * rhs[3]) + (lhs[5] * rhs[4]) + (lhs[8] * rhs[5]), (lhs[0] * rhs[6]) + (lhs[3] * rhs[7]) + (lhs[6] * rhs[8]), (lhs[1] * rhs[6]) + (lhs[4] * rhs[7]) + (lhs[7] * rhs[8]), (lhs[2] * rhs[6]) + (lhs[5] * rhs[7]) + (lhs[8] * rhs[8])};
        return r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized float[] mul3x3Float3(float[] lhs, float[] rhs) {
        float r0 = rhs[0];
        float r1 = rhs[1];
        float r2 = rhs[2];
        rhs[0] = (lhs[0] * r0) + (lhs[3] * r1) + (lhs[6] * r2);
        rhs[1] = (lhs[1] * r0) + (lhs[4] * r1) + (lhs[7] * r2);
        rhs[2] = (lhs[2] * r0) + (lhs[5] * r1) + (lhs[8] * r2);
        return rhs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized float[] mul3x3Diag(float[] lhs, float[] rhs) {
        return new float[]{lhs[0] * rhs[0], lhs[1] * rhs[1], lhs[2] * rhs[2], lhs[0] * rhs[3], lhs[1] * rhs[4], lhs[2] * rhs[5], lhs[0] * rhs[6], lhs[1] * rhs[7], lhs[2] * rhs[8]};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized float[] xyYToXyz(float[] xyY) {
        return new float[]{xyY[0] / xyY[1], 1.0f, ((1.0f - xyY[0]) - xyY[1]) / xyY[1]};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void xyYToUv(float[] xyY) {
        for (int i = 0; i < xyY.length; i += 2) {
            float x = xyY[i];
            float y = xyY[i + 1];
            float d = ((-2.0f) * x) + (12.0f * y) + 3.0f;
            float u = (4.0f * x) / d;
            float v = (9.0f * y) / d;
            xyY[i] = u;
            xyY[i + 1] = v;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized float[] chromaticAdaptation(float[] matrix, float[] srcWhitePoint, float[] dstWhitePoint) {
        float[] srcLMS = mul3x3Float3(matrix, srcWhitePoint);
        float[] dstLMS = mul3x3Float3(matrix, dstWhitePoint);
        float[] LMS = {dstLMS[0] / srcLMS[0], dstLMS[1] / srcLMS[1], dstLMS[2] / srcLMS[2]};
        return mul3x3(inverse3x3(matrix), mul3x3Diag(LMS, matrix));
    }

    /* loaded from: classes.dex */
    private static final class Xyz extends ColorSpace {
        private synchronized Xyz(String name, int id) {
            super(name, Model.XYZ, id);
        }

        @Override // android.graphics.ColorSpace
        public boolean isWideGamut() {
            return true;
        }

        @Override // android.graphics.ColorSpace
        public float getMinValue(int component) {
            return -2.0f;
        }

        @Override // android.graphics.ColorSpace
        public float getMaxValue(int component) {
            return 2.0f;
        }

        @Override // android.graphics.ColorSpace
        public float[] toXyz(float[] v) {
            v[0] = clamp(v[0]);
            v[1] = clamp(v[1]);
            v[2] = clamp(v[2]);
            return v;
        }

        @Override // android.graphics.ColorSpace
        public float[] fromXyz(float[] v) {
            v[0] = clamp(v[0]);
            v[1] = clamp(v[1]);
            v[2] = clamp(v[2]);
            return v;
        }

        private static synchronized float clamp(float x) {
            if (x < -2.0f) {
                return -2.0f;
            }
            if (x > 2.0f) {
                return 2.0f;
            }
            return x;
        }
    }

    /* loaded from: classes.dex */
    private static final class Lab extends ColorSpace {
        private static final float A = 0.008856452f;
        private static final float B = 7.787037f;
        private static final float C = 0.13793103f;
        private static final float D = 0.20689656f;

        private synchronized Lab(String name, int id) {
            super(name, Model.LAB, id);
        }

        @Override // android.graphics.ColorSpace
        public boolean isWideGamut() {
            return true;
        }

        @Override // android.graphics.ColorSpace
        public float getMinValue(int component) {
            return component == 0 ? 0.0f : -128.0f;
        }

        @Override // android.graphics.ColorSpace
        public float getMaxValue(int component) {
            return component == 0 ? 100.0f : 128.0f;
        }

        @Override // android.graphics.ColorSpace
        public float[] toXyz(float[] v) {
            v[0] = clamp(v[0], 0.0f, 100.0f);
            v[1] = clamp(v[1], -128.0f, 128.0f);
            v[2] = clamp(v[2], -128.0f, 128.0f);
            float fy = (v[0] + 16.0f) / 116.0f;
            float fx = (v[1] * 0.002f) + fy;
            float fz = fy - (v[2] * 0.005f);
            float X = fx > D ? fx * fx * fx : (fx - C) * 0.12841855f;
            float Y = fy > D ? fy * fy * fy : (fy - C) * 0.12841855f;
            float Z = fz > D ? fz * fz * fz : (fz - C) * 0.12841855f;
            v[0] = ColorSpace.ILLUMINANT_D50_XYZ[0] * X;
            v[1] = ColorSpace.ILLUMINANT_D50_XYZ[1] * Y;
            v[2] = ColorSpace.ILLUMINANT_D50_XYZ[2] * Z;
            return v;
        }

        @Override // android.graphics.ColorSpace
        public float[] fromXyz(float[] v) {
            float X = v[0] / ColorSpace.ILLUMINANT_D50_XYZ[0];
            float Y = v[1] / ColorSpace.ILLUMINANT_D50_XYZ[1];
            float Z = v[2] / ColorSpace.ILLUMINANT_D50_XYZ[2];
            float fx = X > A ? (float) Math.pow(X, 0.3333333333333333d) : (B * X) + C;
            float fy = Y > A ? (float) Math.pow(Y, 0.3333333333333333d) : (B * Y) + C;
            float fz = Z > A ? (float) Math.pow(Z, 0.3333333333333333d) : (B * Z) + C;
            float L = (116.0f * fy) - 16.0f;
            float a = 500.0f * (fx - fy);
            float b = 200.0f * (fy - fz);
            v[0] = clamp(L, 0.0f, 100.0f);
            v[1] = clamp(a, -128.0f, 128.0f);
            v[2] = clamp(b, -128.0f, 128.0f);
            return v;
        }

        private static synchronized float clamp(float x, float min, float max) {
            return x < min ? min : x > max ? max : x;
        }
    }

    /* loaded from: classes.dex */
    public static class Rgb extends ColorSpace {
        private final DoubleUnaryOperator mClampedEotf;
        private final DoubleUnaryOperator mClampedOetf;
        private final DoubleUnaryOperator mEotf;
        private final float[] mInverseTransform;
        private final boolean mIsSrgb;
        private final boolean mIsWideGamut;
        private final float mMax;
        private final float mMin;
        private final DoubleUnaryOperator mOetf;
        private final float[] mPrimaries;
        private TransferParameters mTransferParameters;
        private final float[] mTransform;
        private final float[] mWhitePoint;

        /* loaded from: classes.dex */
        public static class TransferParameters {
            public final double a;
            public final double b;
            public final double c;
            public final double d;
            public final double e;
            public final double f;
            public final double g;

            public TransferParameters(double a, double b, double c, double d, double g) {
                this(a, b, c, d, FeatureOption.FO_BOOT_POLICY_CPU, FeatureOption.FO_BOOT_POLICY_CPU, g);
            }

            public TransferParameters(double a, double b, double c, double d, double e, double f, double g) {
                if (Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c) || Double.isNaN(d) || Double.isNaN(e) || Double.isNaN(f) || Double.isNaN(g)) {
                    throw new IllegalArgumentException("Parameters cannot be NaN");
                }
                if (d < FeatureOption.FO_BOOT_POLICY_CPU || d > 1.0f + Math.ulp(1.0f)) {
                    throw new IllegalArgumentException("Parameter d must be in the range [0..1], was " + d);
                } else if (d == FeatureOption.FO_BOOT_POLICY_CPU && (a == FeatureOption.FO_BOOT_POLICY_CPU || g == FeatureOption.FO_BOOT_POLICY_CPU)) {
                    throw new IllegalArgumentException("Parameter a or g is zero, the transfer function is constant");
                } else {
                    if (d >= 1.0d && c == FeatureOption.FO_BOOT_POLICY_CPU) {
                        throw new IllegalArgumentException("Parameter c is zero, the transfer function is constant");
                    }
                    if ((a == FeatureOption.FO_BOOT_POLICY_CPU || g == FeatureOption.FO_BOOT_POLICY_CPU) && c == FeatureOption.FO_BOOT_POLICY_CPU) {
                        throw new IllegalArgumentException("Parameter a or g is zero, and c is zero, the transfer function is constant");
                    }
                    if (c < FeatureOption.FO_BOOT_POLICY_CPU) {
                        throw new IllegalArgumentException("The transfer function must be increasing");
                    }
                    if (a < FeatureOption.FO_BOOT_POLICY_CPU || g < FeatureOption.FO_BOOT_POLICY_CPU) {
                        throw new IllegalArgumentException("The transfer function must be positive or increasing");
                    }
                    this.a = a;
                    this.b = b;
                    this.c = c;
                    this.d = d;
                    this.e = e;
                    this.f = f;
                    this.g = g;
                }
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                TransferParameters that = (TransferParameters) o;
                if (Double.compare(that.a, this.a) == 0 && Double.compare(that.b, this.b) == 0 && Double.compare(that.c, this.c) == 0 && Double.compare(that.d, this.d) == 0 && Double.compare(that.e, this.e) == 0 && Double.compare(that.f, this.f) == 0 && Double.compare(that.g, this.g) == 0) {
                    return true;
                }
                return false;
            }

            public int hashCode() {
                long temp = Double.doubleToLongBits(this.a);
                int result = (int) ((temp >>> 32) ^ temp);
                long temp2 = Double.doubleToLongBits(this.b);
                int result2 = (31 * result) + ((int) ((temp2 >>> 32) ^ temp2));
                long temp3 = Double.doubleToLongBits(this.c);
                int result3 = (31 * result2) + ((int) ((temp3 >>> 32) ^ temp3));
                long temp4 = Double.doubleToLongBits(this.d);
                int result4 = (31 * result3) + ((int) ((temp4 >>> 32) ^ temp4));
                long temp5 = Double.doubleToLongBits(this.e);
                int result5 = (31 * result4) + ((int) ((temp5 >>> 32) ^ temp5));
                long temp6 = Double.doubleToLongBits(this.f);
                int result6 = (31 * result5) + ((int) ((temp6 >>> 32) ^ temp6));
                long temp7 = Double.doubleToLongBits(this.g);
                return (31 * result6) + ((int) ((temp7 >>> 32) ^ temp7));
            }
        }

        public Rgb(String name, float[] toXYZ, DoubleUnaryOperator oetf, DoubleUnaryOperator eotf) {
            this(name, computePrimaries(toXYZ), computeWhitePoint(toXYZ), oetf, eotf, 0.0f, 1.0f, -1);
        }

        public Rgb(String name, float[] primaries, float[] whitePoint, DoubleUnaryOperator oetf, DoubleUnaryOperator eotf, float min, float max) {
            this(name, primaries, whitePoint, oetf, eotf, min, max, -1);
        }

        public Rgb(String name, float[] toXYZ, TransferParameters function) {
            this(name, computePrimaries(toXYZ), computeWhitePoint(toXYZ), function, -1);
        }

        public Rgb(String name, float[] primaries, float[] whitePoint, TransferParameters function) {
            this(name, primaries, whitePoint, function, -1);
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private synchronized Rgb(java.lang.String r15, float[] r16, float[] r17, final android.graphics.ColorSpace.Rgb.TransferParameters r18, int r19) {
            /*
                r14 = this;
                r0 = r18
                double r1 = r0.e
                r3 = 0
                int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                if (r1 != 0) goto L17
                double r1 = r0.f
                int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                if (r1 != 0) goto L17
                android.graphics.-$$Lambda$ColorSpace$Rgb$bWzafC8vMHNuVmRuTUPEFUMlfuY r1 = new android.graphics.-$$Lambda$ColorSpace$Rgb$bWzafC8vMHNuVmRuTUPEFUMlfuY
                r1.<init>()
                goto L1c
            L17:
                android.graphics.-$$Lambda$ColorSpace$Rgb$V_0lmM2WEpxGBDV_1G1wvvidn7Y r1 = new android.graphics.-$$Lambda$ColorSpace$Rgb$V_0lmM2WEpxGBDV_1G1wvvidn7Y
                r1.<init>()
            L1c:
                r9 = r1
                double r1 = r0.e
                int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                if (r1 != 0) goto L2f
                double r1 = r0.f
                int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                if (r1 != 0) goto L2f
                android.graphics.-$$Lambda$ColorSpace$Rgb$b9VGKuNnse0bbguR9jbOM_wK2Ac r1 = new android.graphics.-$$Lambda$ColorSpace$Rgb$b9VGKuNnse0bbguR9jbOM_wK2Ac
                r1.<init>()
                goto L34
            L2f:
                android.graphics.-$$Lambda$ColorSpace$Rgb$iMkODTKa3_8kPZUnZZerD2Lv-yo r1 = new android.graphics.-$$Lambda$ColorSpace$Rgb$iMkODTKa3_8kPZUnZZerD2Lv-yo
                r1.<init>()
            L34:
                r10 = r1
                r11 = 0
                r12 = 1065353216(0x3f800000, float:1.0)
                r5 = r14
                r6 = r15
                r7 = r16
                r8 = r17
                r13 = r19
                r5.<init>(r6, r7, r8, r9, r10, r11, r12, r13)
                r1 = r14
                r1.mTransferParameters = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.graphics.ColorSpace.Rgb.<init>(java.lang.String, float[], float[], android.graphics.ColorSpace$Rgb$TransferParameters, int):void");
        }

        public Rgb(String name, float[] toXYZ, double gamma) {
            this(name, computePrimaries(toXYZ), computeWhitePoint(toXYZ), gamma, 0.0f, 1.0f, -1);
        }

        public Rgb(String name, float[] primaries, float[] whitePoint, double gamma) {
            this(name, primaries, whitePoint, gamma, 0.0f, 1.0f, -1);
        }

        private synchronized Rgb(String name, float[] primaries, float[] whitePoint, final double gamma, float min, float max, int id) {
            this(name, primaries, whitePoint, gamma == 1.0d ? DoubleUnaryOperator.identity() : new DoubleUnaryOperator() { // from class: android.graphics.-$$Lambda$ColorSpace$Rgb$CqKld6797g7__JnuY0NeFz5q4_E
                @Override // java.util.function.DoubleUnaryOperator
                public final double applyAsDouble(double d) {
                    return ColorSpace.Rgb.lambda$new$4(gamma, d);
                }
            }, gamma == 1.0d ? DoubleUnaryOperator.identity() : new DoubleUnaryOperator() { // from class: android.graphics.-$$Lambda$ColorSpace$Rgb$ZvS77aTfobOSa2o9MTqYMph4Rcg
                @Override // java.util.function.DoubleUnaryOperator
                public final double applyAsDouble(double d) {
                    return ColorSpace.Rgb.lambda$new$5(gamma, d);
                }
            }, min, max, id);
            TransferParameters transferParameters;
            if (gamma == 1.0d) {
                transferParameters = new TransferParameters(FeatureOption.FO_BOOT_POLICY_CPU, FeatureOption.FO_BOOT_POLICY_CPU, 1.0d, Math.ulp(1.0f) + 1.0d, gamma);
            } else {
                transferParameters = new TransferParameters(1.0d, FeatureOption.FO_BOOT_POLICY_CPU, FeatureOption.FO_BOOT_POLICY_CPU, FeatureOption.FO_BOOT_POLICY_CPU, gamma);
            }
            this.mTransferParameters = transferParameters;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ double lambda$new$4(double gamma, double x) {
            double d = FeatureOption.FO_BOOT_POLICY_CPU;
            if (x >= FeatureOption.FO_BOOT_POLICY_CPU) {
                d = x;
            }
            return Math.pow(d, 1.0d / gamma);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ double lambda$new$5(double gamma, double x) {
            double d = FeatureOption.FO_BOOT_POLICY_CPU;
            if (x >= FeatureOption.FO_BOOT_POLICY_CPU) {
                d = x;
            }
            return Math.pow(d, gamma);
        }

        private synchronized Rgb(String name, float[] primaries, float[] whitePoint, DoubleUnaryOperator oetf, DoubleUnaryOperator eotf, float min, float max, int id) {
            super(name, Model.RGB, id);
            if (primaries == null || (primaries.length != 6 && primaries.length != 9)) {
                throw new IllegalArgumentException("The color space's primaries must be defined as an array of 6 floats in xyY or 9 floats in XYZ");
            }
            if (whitePoint == null || (whitePoint.length != 2 && whitePoint.length != 3)) {
                throw new IllegalArgumentException("The color space's white point must be defined as an array of 2 floats in xyY or 3 float in XYZ");
            }
            if (oetf == null || eotf == null) {
                throw new IllegalArgumentException("The transfer functions of a color space cannot be null");
            }
            if (min >= max) {
                throw new IllegalArgumentException("Invalid range: min=" + min + ", max=" + max + "; min must be strictly < max");
            }
            this.mWhitePoint = xyWhitePoint(whitePoint);
            this.mPrimaries = xyPrimaries(primaries);
            this.mTransform = computeXYZMatrix(this.mPrimaries, this.mWhitePoint);
            this.mInverseTransform = ColorSpace.inverse3x3(this.mTransform);
            this.mOetf = oetf;
            this.mEotf = eotf;
            this.mMin = min;
            this.mMax = max;
            DoubleUnaryOperator clamp = new DoubleUnaryOperator() { // from class: android.graphics.-$$Lambda$ColorSpace$Rgb$8EkhO2jIf14tuA3BvrmYJMa7YXM
                @Override // java.util.function.DoubleUnaryOperator
                public final double applyAsDouble(double d) {
                    double clamp2;
                    clamp2 = ColorSpace.Rgb.this.clamp(d);
                    return clamp2;
                }
            };
            this.mClampedOetf = oetf.andThen(clamp);
            this.mClampedEotf = clamp.andThen(eotf);
            this.mIsWideGamut = isWideGamut(this.mPrimaries, min, max);
            this.mIsSrgb = isSrgb(this.mPrimaries, this.mWhitePoint, oetf, eotf, min, max, id);
        }

        private synchronized Rgb(Rgb colorSpace, float[] transform, float[] whitePoint) {
            super(colorSpace.getName(), Model.RGB, -1);
            this.mWhitePoint = xyWhitePoint(whitePoint);
            this.mPrimaries = colorSpace.mPrimaries;
            this.mTransform = transform;
            this.mInverseTransform = ColorSpace.inverse3x3(transform);
            this.mMin = colorSpace.mMin;
            this.mMax = colorSpace.mMax;
            this.mOetf = colorSpace.mOetf;
            this.mEotf = colorSpace.mEotf;
            this.mClampedOetf = colorSpace.mClampedOetf;
            this.mClampedEotf = colorSpace.mClampedEotf;
            this.mIsWideGamut = colorSpace.mIsWideGamut;
            this.mIsSrgb = colorSpace.mIsSrgb;
            this.mTransferParameters = colorSpace.mTransferParameters;
        }

        public float[] getWhitePoint(float[] whitePoint) {
            whitePoint[0] = this.mWhitePoint[0];
            whitePoint[1] = this.mWhitePoint[1];
            return whitePoint;
        }

        public float[] getWhitePoint() {
            return Arrays.copyOf(this.mWhitePoint, this.mWhitePoint.length);
        }

        public float[] getPrimaries(float[] primaries) {
            System.arraycopy(this.mPrimaries, 0, primaries, 0, this.mPrimaries.length);
            return primaries;
        }

        public float[] getPrimaries() {
            return Arrays.copyOf(this.mPrimaries, this.mPrimaries.length);
        }

        public float[] getTransform(float[] transform) {
            System.arraycopy(this.mTransform, 0, transform, 0, this.mTransform.length);
            return transform;
        }

        public float[] getTransform() {
            return Arrays.copyOf(this.mTransform, this.mTransform.length);
        }

        public float[] getInverseTransform(float[] inverseTransform) {
            System.arraycopy(this.mInverseTransform, 0, inverseTransform, 0, this.mInverseTransform.length);
            return inverseTransform;
        }

        public float[] getInverseTransform() {
            return Arrays.copyOf(this.mInverseTransform, this.mInverseTransform.length);
        }

        public DoubleUnaryOperator getOetf() {
            return this.mClampedOetf;
        }

        public DoubleUnaryOperator getEotf() {
            return this.mClampedEotf;
        }

        public TransferParameters getTransferParameters() {
            return this.mTransferParameters;
        }

        @Override // android.graphics.ColorSpace
        public boolean isSrgb() {
            return this.mIsSrgb;
        }

        @Override // android.graphics.ColorSpace
        public boolean isWideGamut() {
            return this.mIsWideGamut;
        }

        @Override // android.graphics.ColorSpace
        public float getMinValue(int component) {
            return this.mMin;
        }

        @Override // android.graphics.ColorSpace
        public float getMaxValue(int component) {
            return this.mMax;
        }

        public float[] toLinear(float r, float g, float b) {
            return toLinear(new float[]{r, g, b});
        }

        public float[] toLinear(float[] v) {
            v[0] = (float) this.mClampedEotf.applyAsDouble(v[0]);
            v[1] = (float) this.mClampedEotf.applyAsDouble(v[1]);
            v[2] = (float) this.mClampedEotf.applyAsDouble(v[2]);
            return v;
        }

        public float[] fromLinear(float r, float g, float b) {
            return fromLinear(new float[]{r, g, b});
        }

        public float[] fromLinear(float[] v) {
            v[0] = (float) this.mClampedOetf.applyAsDouble(v[0]);
            v[1] = (float) this.mClampedOetf.applyAsDouble(v[1]);
            v[2] = (float) this.mClampedOetf.applyAsDouble(v[2]);
            return v;
        }

        @Override // android.graphics.ColorSpace
        public float[] toXyz(float[] v) {
            v[0] = (float) this.mClampedEotf.applyAsDouble(v[0]);
            v[1] = (float) this.mClampedEotf.applyAsDouble(v[1]);
            v[2] = (float) this.mClampedEotf.applyAsDouble(v[2]);
            return ColorSpace.mul3x3Float3(this.mTransform, v);
        }

        @Override // android.graphics.ColorSpace
        public float[] fromXyz(float[] v) {
            ColorSpace.mul3x3Float3(this.mInverseTransform, v);
            v[0] = (float) this.mClampedOetf.applyAsDouble(v[0]);
            v[1] = (float) this.mClampedOetf.applyAsDouble(v[1]);
            v[2] = (float) this.mClampedOetf.applyAsDouble(v[2]);
            return v;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized double clamp(double x) {
            float f;
            if (x < this.mMin) {
                f = this.mMin;
            } else if (x <= this.mMax) {
                return x;
            } else {
                f = this.mMax;
            }
            return f;
        }

        @Override // android.graphics.ColorSpace
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass() || !super.equals(o)) {
                return false;
            }
            Rgb rgb = (Rgb) o;
            if (Float.compare(rgb.mMin, this.mMin) != 0 || Float.compare(rgb.mMax, this.mMax) != 0 || !Arrays.equals(this.mWhitePoint, rgb.mWhitePoint) || !Arrays.equals(this.mPrimaries, rgb.mPrimaries)) {
                return false;
            }
            if (this.mTransferParameters != null) {
                return this.mTransferParameters.equals(rgb.mTransferParameters);
            }
            if (rgb.mTransferParameters == null) {
                return true;
            }
            if (!this.mOetf.equals(rgb.mOetf)) {
                return false;
            }
            return this.mEotf.equals(rgb.mEotf);
        }

        @Override // android.graphics.ColorSpace
        public int hashCode() {
            int result = (31 * ((31 * ((31 * ((31 * ((31 * super.hashCode()) + Arrays.hashCode(this.mWhitePoint))) + Arrays.hashCode(this.mPrimaries))) + (this.mMin != 0.0f ? Float.floatToIntBits(this.mMin) : 0))) + (this.mMax != 0.0f ? Float.floatToIntBits(this.mMax) : 0))) + (this.mTransferParameters != null ? this.mTransferParameters.hashCode() : 0);
            if (this.mTransferParameters == null) {
                return this.mEotf.hashCode() + (31 * ((31 * result) + this.mOetf.hashCode()));
            }
            return result;
        }

        private static synchronized boolean isSrgb(float[] primaries, float[] whitePoint, DoubleUnaryOperator OETF, DoubleUnaryOperator EOTF, float min, float max, int id) {
            if (id == 0) {
                return true;
            }
            return ColorSpace.compare(primaries, ColorSpace.SRGB_PRIMARIES) && ColorSpace.compare(whitePoint, ILLUMINANT_D65) && OETF.applyAsDouble(0.5d) >= 0.5001d && EOTF.applyAsDouble(0.5d) <= 0.5001d && min == 0.0f && max == 1.0f;
        }

        private static synchronized boolean isWideGamut(float[] primaries, float min, float max) {
            return (area(primaries) / area(ColorSpace.NTSC_1953_PRIMARIES) > 0.9f && contains(primaries, ColorSpace.SRGB_PRIMARIES)) || (min < 0.0f && max > 1.0f);
        }

        private static synchronized float area(float[] primaries) {
            float Rx = primaries[0];
            float Ry = primaries[1];
            float Gx = primaries[2];
            float Gy = primaries[3];
            float Bx = primaries[4];
            float By = primaries[5];
            float det = (((((Rx * Gy) + (Ry * Bx)) + (Gx * By)) - (Gy * Bx)) - (Ry * Gx)) - (Rx * By);
            float r = 0.5f * det;
            return r < 0.0f ? -r : r;
        }

        private static synchronized float cross(float ax, float ay, float bx, float by) {
            return (ax * by) - (ay * bx);
        }

        private static synchronized boolean contains(float[] p1, float[] p2) {
            float[] p0 = {p1[0] - p2[0], p1[1] - p2[1], p1[2] - p2[2], p1[3] - p2[3], p1[4] - p2[4], p1[5] - p2[5]};
            return cross(p0[0], p0[1], p2[0] - p2[4], p2[1] - p2[5]) >= 0.0f && cross(p2[0] - p2[2], p2[1] - p2[3], p0[0], p0[1]) >= 0.0f && cross(p0[2], p0[3], p2[2] - p2[0], p2[3] - p2[1]) >= 0.0f && cross(p2[2] - p2[4], p2[3] - p2[5], p0[2], p0[3]) >= 0.0f && cross(p0[4], p0[5], p2[4] - p2[2], p2[5] - p2[3]) >= 0.0f && cross(p2[4] - p2[0], p2[5] - p2[1], p0[4], p0[5]) >= 0.0f;
        }

        private static synchronized float[] computePrimaries(float[] toXYZ) {
            float[] r = ColorSpace.mul3x3Float3(toXYZ, new float[]{1.0f, 0.0f, 0.0f});
            float[] g = ColorSpace.mul3x3Float3(toXYZ, new float[]{0.0f, 1.0f, 0.0f});
            float[] b = ColorSpace.mul3x3Float3(toXYZ, new float[]{0.0f, 0.0f, 1.0f});
            float rSum = r[0] + r[1] + r[2];
            float gSum = g[0] + g[1] + g[2];
            float bSum = b[0] + b[1] + b[2];
            return new float[]{r[0] / rSum, r[1] / rSum, g[0] / gSum, g[1] / gSum, b[0] / bSum, b[1] / bSum};
        }

        private static synchronized float[] computeWhitePoint(float[] toXYZ) {
            float[] w = ColorSpace.mul3x3Float3(toXYZ, new float[]{1.0f, 1.0f, 1.0f});
            float sum = w[0] + w[1] + w[2];
            return new float[]{w[0] / sum, w[1] / sum};
        }

        private static synchronized float[] xyPrimaries(float[] primaries) {
            float[] xyPrimaries = new float[6];
            if (primaries.length == 9) {
                float sum = primaries[0] + primaries[1] + primaries[2];
                xyPrimaries[0] = primaries[0] / sum;
                xyPrimaries[1] = primaries[1] / sum;
                float sum2 = primaries[3] + primaries[4] + primaries[5];
                xyPrimaries[2] = primaries[3] / sum2;
                xyPrimaries[3] = primaries[4] / sum2;
                float sum3 = primaries[6] + primaries[7] + primaries[8];
                xyPrimaries[4] = primaries[6] / sum3;
                xyPrimaries[5] = primaries[7] / sum3;
            } else {
                System.arraycopy(primaries, 0, xyPrimaries, 0, 6);
            }
            return xyPrimaries;
        }

        private static synchronized float[] xyWhitePoint(float[] whitePoint) {
            float[] xyWhitePoint = new float[2];
            if (whitePoint.length == 3) {
                float sum = whitePoint[0] + whitePoint[1] + whitePoint[2];
                xyWhitePoint[0] = whitePoint[0] / sum;
                xyWhitePoint[1] = whitePoint[1] / sum;
            } else {
                System.arraycopy(whitePoint, 0, xyWhitePoint, 0, 2);
            }
            return xyWhitePoint;
        }

        private static synchronized float[] computeXYZMatrix(float[] primaries, float[] whitePoint) {
            float Rx = primaries[0];
            float Ry = primaries[1];
            float Gx = primaries[2];
            float Gy = primaries[3];
            float Bx = primaries[4];
            float By = primaries[5];
            float Wx = whitePoint[0];
            float Wy = whitePoint[1];
            float oneRxRy = (1.0f - Rx) / Ry;
            float oneGxGy = (1.0f - Gx) / Gy;
            float oneBxBy = (1.0f - Bx) / By;
            float oneWxWy = (1.0f - Wx) / Wy;
            float RxRy = Rx / Ry;
            float GxGy = Gx / Gy;
            float BxBy = Bx / By;
            float WxWy = Wx / Wy;
            float BY = (((oneWxWy - oneRxRy) * (GxGy - RxRy)) - ((WxWy - RxRy) * (oneGxGy - oneRxRy))) / (((oneBxBy - oneRxRy) * (GxGy - RxRy)) - ((BxBy - RxRy) * (oneGxGy - oneRxRy)));
            float GY = ((WxWy - RxRy) - ((BxBy - RxRy) * BY)) / (GxGy - RxRy);
            float RY = (1.0f - GY) - BY;
            float RYRy = RY / Ry;
            float GYGy = GY / Gy;
            float BYBy = BY / By;
            return new float[]{RYRy * Rx, RY, ((1.0f - Rx) - Ry) * RYRy, GYGy * Gx, GY, ((1.0f - Gx) - Gy) * GYGy, BYBy * Bx, BY, ((1.0f - Bx) - By) * BYBy};
        }
    }

    /* loaded from: classes.dex */
    public static class Connector {
        private final ColorSpace mDestination;
        private final RenderIntent mIntent;
        private final ColorSpace mSource;
        private final float[] mTransform;
        private final ColorSpace mTransformDestination;
        private final ColorSpace mTransformSource;

        synchronized Connector(ColorSpace source, ColorSpace destination, RenderIntent intent) {
            this(source, destination, source.getModel() == Model.RGB ? ColorSpace.adapt(source, ColorSpace.ILLUMINANT_D50_XYZ) : source, destination.getModel() == Model.RGB ? ColorSpace.adapt(destination, ColorSpace.ILLUMINANT_D50_XYZ) : destination, intent, computeTransform(source, destination, intent));
        }

        private synchronized Connector(ColorSpace source, ColorSpace destination, ColorSpace transformSource, ColorSpace transformDestination, RenderIntent intent, float[] transform) {
            this.mSource = source;
            this.mDestination = destination;
            this.mTransformSource = transformSource;
            this.mTransformDestination = transformDestination;
            this.mIntent = intent;
            this.mTransform = transform;
        }

        private static synchronized float[] computeTransform(ColorSpace source, ColorSpace destination, RenderIntent intent) {
            if (intent != RenderIntent.ABSOLUTE) {
                return null;
            }
            boolean srcRGB = source.getModel() == Model.RGB;
            boolean dstRGB = destination.getModel() == Model.RGB;
            if (srcRGB && dstRGB) {
                return null;
            }
            if (srcRGB || dstRGB) {
                Rgb rgb = (Rgb) (srcRGB ? source : destination);
                float[] srcXYZ = srcRGB ? ColorSpace.xyYToXyz(rgb.mWhitePoint) : ColorSpace.ILLUMINANT_D50_XYZ;
                float[] dstXYZ = dstRGB ? ColorSpace.xyYToXyz(rgb.mWhitePoint) : ColorSpace.ILLUMINANT_D50_XYZ;
                return new float[]{srcXYZ[0] / dstXYZ[0], srcXYZ[1] / dstXYZ[1], srcXYZ[2] / dstXYZ[2]};
            }
            return null;
        }

        public ColorSpace getSource() {
            return this.mSource;
        }

        public ColorSpace getDestination() {
            return this.mDestination;
        }

        public RenderIntent getRenderIntent() {
            return this.mIntent;
        }

        public float[] transform(float r, float g, float b) {
            return transform(new float[]{r, g, b});
        }

        public float[] transform(float[] v) {
            float[] xyz = this.mTransformSource.toXyz(v);
            if (this.mTransform != null) {
                xyz[0] = xyz[0] * this.mTransform[0];
                xyz[1] = xyz[1] * this.mTransform[1];
                xyz[2] = xyz[2] * this.mTransform[2];
            }
            return this.mTransformDestination.fromXyz(xyz);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Rgb extends Connector {
            private final Rgb mDestination;
            private final Rgb mSource;
            private final float[] mTransform;

            synchronized Rgb(Rgb source, Rgb destination, RenderIntent intent) {
                super(destination, source, destination, intent, null);
                this.mSource = source;
                this.mDestination = destination;
                this.mTransform = computeTransform(source, destination, intent);
            }

            @Override // android.graphics.ColorSpace.Connector
            public float[] transform(float[] rgb) {
                rgb[0] = (float) this.mSource.mClampedEotf.applyAsDouble(rgb[0]);
                rgb[1] = (float) this.mSource.mClampedEotf.applyAsDouble(rgb[1]);
                rgb[2] = (float) this.mSource.mClampedEotf.applyAsDouble(rgb[2]);
                ColorSpace.mul3x3Float3(this.mTransform, rgb);
                rgb[0] = (float) this.mDestination.mClampedOetf.applyAsDouble(rgb[0]);
                rgb[1] = (float) this.mDestination.mClampedOetf.applyAsDouble(rgb[1]);
                rgb[2] = (float) this.mDestination.mClampedOetf.applyAsDouble(rgb[2]);
                return rgb;
            }

            private static synchronized float[] computeTransform(Rgb source, Rgb destination, RenderIntent intent) {
                if (ColorSpace.compare(source.mWhitePoint, destination.mWhitePoint)) {
                    return ColorSpace.mul3x3(destination.mInverseTransform, source.mTransform);
                }
                float[] transform = source.mTransform;
                float[] inverseTransform = destination.mInverseTransform;
                float[] srcXYZ = ColorSpace.xyYToXyz(source.mWhitePoint);
                float[] dstXYZ = ColorSpace.xyYToXyz(destination.mWhitePoint);
                if (!ColorSpace.compare(source.mWhitePoint, ColorSpace.ILLUMINANT_D50)) {
                    float[] srcAdaptation = ColorSpace.chromaticAdaptation(Adaptation.BRADFORD.mTransform, srcXYZ, Arrays.copyOf(ColorSpace.ILLUMINANT_D50_XYZ, 3));
                    transform = ColorSpace.mul3x3(srcAdaptation, source.mTransform);
                }
                float[] srcAdaptation2 = destination.mWhitePoint;
                if (!ColorSpace.compare(srcAdaptation2, ColorSpace.ILLUMINANT_D50)) {
                    float[] dstAdaptation = ColorSpace.chromaticAdaptation(Adaptation.BRADFORD.mTransform, dstXYZ, Arrays.copyOf(ColorSpace.ILLUMINANT_D50_XYZ, 3));
                    inverseTransform = ColorSpace.inverse3x3(ColorSpace.mul3x3(dstAdaptation, destination.mTransform));
                }
                if (intent == RenderIntent.ABSOLUTE) {
                    transform = ColorSpace.mul3x3Diag(new float[]{srcXYZ[0] / dstXYZ[0], srcXYZ[1] / dstXYZ[1], srcXYZ[2] / dstXYZ[2]}, transform);
                }
                return ColorSpace.mul3x3(inverseTransform, transform);
            }
        }

        static synchronized Connector identity(ColorSpace source) {
            return new Connector(source, source, RenderIntent.RELATIVE) { // from class: android.graphics.ColorSpace.Connector.1
                @Override // android.graphics.ColorSpace.Connector
                public float[] transform(float[] v) {
                    return v;
                }
            };
        }
    }

    /* loaded from: classes.dex */
    public static class Renderer {
        private static final int CHROMATICITY_RESOLUTION = 32;
        private static final int NATIVE_SIZE = 1440;
        private static final double ONE_THIRD = 0.3333333333333333d;
        private static final float[] SPECTRUM_LOCUS_X = {0.175596f, 0.172787f, 0.170806f, 0.170085f, 0.160343f, 0.146958f, 0.139149f, 0.133536f, 0.126688f, 0.11583f, 0.109616f, 0.099146f, 0.09131f, 0.07813f, 0.068717f, 0.054675f, 0.040763f, 0.027497f, 0.01627f, 0.008169f, 0.004876f, 0.003983f, 0.003859f, 0.004646f, 0.007988f, 0.01387f, 0.022244f, 0.027273f, 0.03282f, 0.038851f, 0.045327f, 0.052175f, 0.059323f, 0.066713f, 0.074299f, 0.089937f, 0.114155f, 0.138695f, 0.154714f, 0.192865f, 0.229607f, 0.26576f, 0.301588f, 0.337346f, 0.373083f, 0.408717f, 0.444043f, 0.478755f, 0.512467f, 0.544767f, 0.575132f, 0.602914f, 0.627018f, 0.648215f, 0.665746f, 0.680061f, 0.691487f, 0.700589f, 0.707901f, 0.714015f, 0.719017f, 0.723016f, 0.734674f, 0.717203f, 0.699732f, 0.68226f, 0.664789f, 0.647318f, 0.629847f, 0.612376f, 0.594905f, 0.577433f, 0.559962f, 0.542491f, 0.52502f, 0.507549f, 0.490077f, 0.472606f, 0.455135f, 0.437664f, 0.420193f, 0.402721f, 0.38525f, 0.367779f, 0.350308f, 0.332837f, 0.315366f, 0.297894f, 0.280423f, 0.262952f, 0.245481f, 0.22801f, 0.210538f, 0.193067f, 0.175596f};
        private static final float[] SPECTRUM_LOCUS_Y = {0.005295f, 0.0048f, 0.005472f, 0.005976f, 0.014496f, 0.026643f, 0.035211f, 0.042704f, 0.053441f, 0.073601f, 0.086866f, 0.112037f, 0.132737f, 0.170464f, 0.200773f, 0.254155f, 0.317049f, 0.387997f, 0.463035f, 0.538504f, 0.587196f, 0.610526f, 0.654897f, 0.67597f, 0.715407f, 0.750246f, 0.779682f, 0.792153f, 0.802971f, 0.812059f, 0.81943f, 0.8252f, 0.82946f, 0.832306f, 0.833833f, 0.833316f, 0.826231f, 0.814796f, 0.805884f, 0.781648f, 0.754347f, 0.724342f, 0.692326f, 0.658867f, 0.62447f, 0.589626f, 0.554734f, 0.520222f, 0.486611f, 0.454454f, 0.424252f, 0.396516f, 0.37251f, 0.351413f, 0.334028f, 0.319765f, 0.308359f, 0.299317f, 0.292044f, 0.285945f, 0.280951f, 0.276964f, 0.265326f, 0.2572f, 0.249074f, 0.240948f, 0.232822f, 0.224696f, 0.21657f, 0.208444f, 0.200318f, 0.192192f, 0.184066f, 0.17594f, 0.167814f, 0.159688f, 0.151562f, 0.143436f, 0.135311f, 0.127185f, 0.119059f, 0.110933f, 0.102807f, 0.094681f, 0.086555f, 0.078429f, 0.070303f, 0.062177f, 0.054051f, 0.045925f, 0.037799f, 0.029673f, 0.021547f, 0.013421f, 0.005295f};
        private static final float UCS_SCALE = 1.5f;
        private boolean mClip;
        private final List<Pair<ColorSpace, Integer>> mColorSpaces;
        private final List<Point> mPoints;
        private boolean mShowWhitePoint;
        private int mSize;
        private boolean mUcs;

        private synchronized Renderer() {
            this.mSize = 1024;
            this.mShowWhitePoint = true;
            this.mClip = false;
            this.mUcs = false;
            this.mColorSpaces = new ArrayList(2);
            this.mPoints = new ArrayList(0);
        }

        public synchronized Renderer clip(boolean clip) {
            this.mClip = clip;
            return this;
        }

        public synchronized Renderer uniformChromaticityScale(boolean ucs) {
            this.mUcs = ucs;
            return this;
        }

        public synchronized Renderer size(int size) {
            this.mSize = Math.max(128, size);
            return this;
        }

        public synchronized Renderer showWhitePoint(boolean show) {
            this.mShowWhitePoint = show;
            return this;
        }

        public synchronized Renderer add(ColorSpace colorSpace, int color) {
            this.mColorSpaces.add(new Pair<>(colorSpace, Integer.valueOf(color)));
            return this;
        }

        public synchronized Renderer add(ColorSpace colorSpace, float r, float g, float b, int pointColor) {
            this.mPoints.add(new Point(colorSpace, new float[]{r, g, b}, pointColor));
            return this;
        }

        public synchronized Bitmap render() {
            Paint paint = new Paint(1);
            Bitmap bitmap = Bitmap.createBitmap(this.mSize, this.mSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            float[] primaries = new float[6];
            float[] whitePoint = new float[2];
            Path path = new Path();
            setTransform(canvas, 1440, 1440, primaries);
            drawBox(canvas, 1440, 1440, paint, path);
            setUcsTransform(canvas, 1440);
            drawLocus(canvas, 1440, 1440, paint, path, primaries);
            drawGamuts(canvas, 1440, 1440, paint, path, primaries, whitePoint);
            drawPoints(canvas, 1440, 1440, paint);
            return bitmap;
        }

        private synchronized void drawPoints(Canvas canvas, int width, int height, Paint paint) {
            paint.setStyle(Paint.Style.FILL);
            float radius = 4.0f / (this.mUcs ? UCS_SCALE : 1.0f);
            float[] v = new float[3];
            float[] xy = new float[2];
            for (Point point : this.mPoints) {
                v[0] = point.mRgb[0];
                v[1] = point.mRgb[1];
                v[2] = point.mRgb[2];
                point.mColorSpace.toXyz(v);
                paint.setColor(point.mColor);
                float sum = v[0] + v[1] + v[2];
                xy[0] = v[0] / sum;
                xy[1] = v[1] / sum;
                if (this.mUcs) {
                    ColorSpace.xyYToUv(xy);
                }
                canvas.drawCircle(width * xy[0], height - (height * xy[1]), radius, paint);
            }
        }

        private synchronized void drawGamuts(Canvas canvas, int width, int height, Paint paint, Path path, float[] primaries, float[] whitePoint) {
            float radius = 4.0f / (this.mUcs ? UCS_SCALE : 1.0f);
            Iterator<Pair<ColorSpace, Integer>> it = this.mColorSpaces.iterator();
            while (it.hasNext()) {
                Pair<ColorSpace, Integer> item = it.next();
                ColorSpace colorSpace = item.first;
                int color = item.second.intValue();
                if (colorSpace.getModel() == Model.RGB) {
                    Rgb rgb = (Rgb) colorSpace;
                    getPrimaries(rgb, primaries, this.mUcs);
                    path.rewind();
                    Iterator<Pair<ColorSpace, Integer>> it2 = it;
                    path.moveTo(width * primaries[0], height - (height * primaries[1]));
                    path.lineTo(width * primaries[2], height - (height * primaries[3]));
                    path.lineTo(width * primaries[4], height - (height * primaries[5]));
                    path.close();
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(color);
                    canvas.drawPath(path, paint);
                    if (this.mShowWhitePoint) {
                        rgb.getWhitePoint(whitePoint);
                        if (this.mUcs) {
                            ColorSpace.xyYToUv(whitePoint);
                        }
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(color);
                        canvas.drawCircle(width * whitePoint[0], height - (height * whitePoint[1]), radius, paint);
                    }
                    it = it2;
                }
            }
        }

        private static synchronized void getPrimaries(Rgb rgb, float[] primaries, boolean asUcs) {
            if (rgb.equals(ColorSpace.get(Named.EXTENDED_SRGB)) || rgb.equals(ColorSpace.get(Named.LINEAR_EXTENDED_SRGB))) {
                primaries[0] = 1.41f;
                primaries[1] = 0.33f;
                primaries[2] = 0.27f;
                primaries[3] = 1.24f;
                primaries[4] = -0.23f;
                primaries[5] = -0.57f;
            } else {
                rgb.getPrimaries(primaries);
            }
            if (asUcs) {
                ColorSpace.xyYToUv(primaries);
            }
        }

        private synchronized void drawLocus(Canvas canvas, int width, int height, Paint paint, Path path, float[] primaries) {
            float[] vertices;
            int vertexCount = SPECTRUM_LOCUS_X.length * 32 * 6;
            float[] vertices2 = new float[vertexCount * 2];
            int[] colors = new int[vertices2.length];
            computeChromaticityMesh(vertices2, colors);
            if (this.mUcs) {
                ColorSpace.xyYToUv(vertices2);
            }
            for (int i = 0; i < vertices2.length; i += 2) {
                vertices2[i] = vertices2[i] * width;
                vertices2[i + 1] = height - (vertices2[i + 1] * height);
            }
            int x = 2;
            if (this.mClip && this.mColorSpaces.size() > 0) {
                Iterator<Pair<ColorSpace, Integer>> it = this.mColorSpaces.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Pair<ColorSpace, Integer> item = it.next();
                    ColorSpace colorSpace = item.first;
                    if (colorSpace.getModel() == Model.RGB) {
                        Rgb rgb = (Rgb) colorSpace;
                        getPrimaries(rgb, primaries, this.mUcs);
                        break;
                    }
                }
                path.rewind();
                path.moveTo(width * primaries[0], height - (height * primaries[1]));
                path.lineTo(width * primaries[2], height - (height * primaries[3]));
                path.lineTo(width * primaries[4], height - (height * primaries[5]));
                path.close();
                int[] solid = new int[colors.length];
                Arrays.fill(solid, -9671572);
                canvas.drawVertices(Canvas.VertexMode.TRIANGLES, vertices2.length, vertices2, 0, null, 0, solid, 0, null, 0, 0, paint);
                canvas.save();
                canvas.clipPath(path);
                canvas.drawVertices(Canvas.VertexMode.TRIANGLES, vertices2.length, vertices2, 0, null, 0, colors, 0, null, 0, 0, paint);
                canvas.restore();
                vertices = vertices2;
            } else {
                vertices = vertices2;
                canvas.drawVertices(Canvas.VertexMode.TRIANGLES, vertices2.length, vertices2, 0, null, 0, colors, 0, null, 0, 0, paint);
            }
            int index = MetricsProto.MetricsEvent.ACTION_SETTINGS_CONDITION_DISMISS;
            path.reset();
            path.moveTo(vertices[372], vertices[MetricsProto.MetricsEvent.ACTION_SETTINGS_CONDITION_DISMISS + 1]);
            while (true) {
                int x2 = x;
                if (x2 >= SPECTRUM_LOCUS_X.length) {
                    break;
                }
                index += MetricsProto.MetricsEvent.ACTION_SHOW_SETTINGS_SUGGESTION;
                path.lineTo(vertices[index], vertices[index + 1]);
                x = x2 + 1;
            }
            path.close();
            paint.setStrokeWidth(4.0f / (this.mUcs ? UCS_SCALE : 1.0f));
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(-16777216);
            canvas.drawPath(path, paint);
        }

        private synchronized void drawBox(Canvas canvas, int width, int height, Paint paint, Path path) {
            int lineCount = 10;
            float scale = 1.0f;
            if (this.mUcs) {
                lineCount = 7;
                scale = UCS_SCALE;
            }
            int lineCount2 = lineCount;
            float scale2 = scale;
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2.0f);
            paint.setColor(-4144960);
            int i = 1;
            int i2 = 1;
            while (true) {
                int i3 = i2;
                int i4 = lineCount2 - 1;
                if (i3 >= i4) {
                    break;
                }
                float v = i3 / 10.0f;
                float x = width * v * scale2;
                float y = height - ((height * v) * scale2);
                canvas.drawLine(0.0f, y, 0.9f * width, y, paint);
                canvas.drawLine(x, height, x, 0.1f * height, paint);
                i2 = i3 + 1;
            }
            paint.setStrokeWidth(4.0f);
            paint.setColor(-16777216);
            int i5 = 1;
            while (true) {
                int i6 = i5;
                int i7 = lineCount2 - 1;
                if (i6 >= i7) {
                    break;
                }
                float v2 = i6 / 10.0f;
                float x2 = width * v2 * scale2;
                float y2 = height - ((height * v2) * scale2);
                canvas.drawLine(0.0f, y2, width / 100.0f, y2, paint);
                canvas.drawLine(x2, height, x2, height - (height / 100.0f), paint);
                i5 = i6 + 1;
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36.0f);
            int i8 = 0;
            paint.setTypeface(Typeface.create("sans-serif-light", 0));
            Rect bounds = new Rect();
            while (true) {
                int i9 = i;
                if (i9 >= lineCount2 - 1) {
                    paint.setStyle(Paint.Style.STROKE);
                    path.moveTo(0.0f, height);
                    path.lineTo(width * 0.9f, height);
                    path.lineTo(width * 0.9f, height * 0.1f);
                    path.lineTo(0.0f, height * 0.1f);
                    path.close();
                    canvas.drawPath(path, paint);
                    return;
                }
                String text = "0." + i9;
                paint.getTextBounds(text, i8, text.length(), bounds);
                float v3 = i9 / 10.0f;
                canvas.drawText(text, ((-0.05f) * width) + 10.0f, (bounds.height() / 2.0f) + (height - ((height * v3) * scale2)), paint);
                canvas.drawText(text, ((width * v3) * scale2) - (bounds.width() / 2.0f), bounds.height() + height + 16, paint);
                i = i9 + 1;
                i8 = 0;
            }
        }

        private synchronized void setTransform(Canvas canvas, int width, int height, float[] primaries) {
            RectF primariesBounds = new RectF();
            for (Pair<ColorSpace, Integer> item : this.mColorSpaces) {
                ColorSpace colorSpace = item.first;
                if (colorSpace.getModel() == Model.RGB) {
                    Rgb rgb = (Rgb) colorSpace;
                    getPrimaries(rgb, primaries, this.mUcs);
                    primariesBounds.left = Math.min(primariesBounds.left, primaries[4]);
                    primariesBounds.top = Math.min(primariesBounds.top, primaries[5]);
                    primariesBounds.right = Math.max(primariesBounds.right, primaries[0]);
                    primariesBounds.bottom = Math.max(primariesBounds.bottom, primaries[3]);
                }
            }
            float max = this.mUcs ? 0.6f : 0.9f;
            primariesBounds.left = Math.min(0.0f, primariesBounds.left);
            primariesBounds.top = Math.min(0.0f, primariesBounds.top);
            primariesBounds.right = Math.max(max, primariesBounds.right);
            primariesBounds.bottom = Math.max(max, primariesBounds.bottom);
            float scaleX = max / primariesBounds.width();
            float scaleY = max / primariesBounds.height();
            float scale = Math.min(scaleX, scaleY);
            canvas.scale(this.mSize / 1440.0f, this.mSize / 1440.0f);
            canvas.scale(scale, scale);
            canvas.translate(((primariesBounds.width() - max) * width) / 2.0f, ((primariesBounds.height() - max) * height) / 2.0f);
            canvas.translate(0.05f * width, (-0.05f) * height);
        }

        private synchronized void setUcsTransform(Canvas canvas, int height) {
            if (this.mUcs) {
                canvas.translate(0.0f, height - (height * UCS_SCALE));
                canvas.scale(UCS_SCALE, UCS_SCALE);
            }
        }

        private static synchronized void computeChromaticityMesh(float[] vertices, int[] colors) {
            ColorSpace colorSpace = ColorSpace.get(Named.SRGB);
            float[] color = new float[3];
            int vertexIndex = 0;
            int colorIndex = 0;
            int x = 0;
            while (x < SPECTRUM_LOCUS_X.length) {
                int nextX = (x % (SPECTRUM_LOCUS_X.length - 1)) + 1;
                float a1 = (float) Math.atan2(SPECTRUM_LOCUS_Y[x] - ONE_THIRD, SPECTRUM_LOCUS_X[x] - ONE_THIRD);
                float a12 = a1;
                float a2 = (float) Math.atan2(SPECTRUM_LOCUS_Y[nextX] - ONE_THIRD, SPECTRUM_LOCUS_X[nextX] - ONE_THIRD);
                float radius1 = (float) Math.pow(sqr(SPECTRUM_LOCUS_X[x] - ONE_THIRD) + sqr(SPECTRUM_LOCUS_Y[x] - ONE_THIRD), 0.5d);
                int vertexIndex2 = vertexIndex;
                float radius2 = (float) Math.pow(sqr(SPECTRUM_LOCUS_X[nextX] - ONE_THIRD) + sqr(SPECTRUM_LOCUS_Y[nextX] - ONE_THIRD), 0.5d);
                colorIndex = colorIndex;
                int c = 1;
                while (true) {
                    int c2 = c;
                    if (c2 <= 32) {
                        float f1 = c2 / 32.0f;
                        float f2 = (c2 - 1) / 32.0f;
                        int x2 = x;
                        int nextX2 = nextX;
                        float a13 = a12;
                        double cr1 = radius1 * Math.cos(a13);
                        float radius12 = radius1;
                        double sr1 = radius1 * Math.sin(a13);
                        double cr2 = radius2 * Math.cos(a2);
                        float radius22 = radius2;
                        int colorIndex2 = colorIndex;
                        double sr2 = radius2 * Math.sin(a2);
                        float v1x = (float) (ONE_THIRD + (f1 * cr1));
                        ColorSpace colorSpace2 = colorSpace;
                        float[] color2 = color;
                        float v1y = (float) (ONE_THIRD + (f1 * sr1));
                        float v1z = (1.0f - v1x) - v1y;
                        float v2x = (float) (ONE_THIRD + (f2 * cr1));
                        float v2y = (float) (ONE_THIRD + (f2 * sr1));
                        float v2z = (1.0f - v2x) - v2y;
                        float v3x = (float) (ONE_THIRD + (f2 * cr2));
                        float a22 = a2;
                        float v3y = (float) (ONE_THIRD + (f2 * sr2));
                        float v3z = (1.0f - v3x) - v3y;
                        float v4x = (float) (ONE_THIRD + (f1 * cr2));
                        float v4y = (float) (ONE_THIRD + (f1 * sr2));
                        float v4z = (1.0f - v4x) - v4y;
                        colors[colorIndex2] = computeColor(color2, v1x, v1y, v1z, colorSpace2);
                        colors[colorIndex2 + 1] = computeColor(color2, v2x, v2y, v2z, colorSpace2);
                        colors[colorIndex2 + 2] = computeColor(color2, v3x, v3y, v3z, colorSpace2);
                        colors[colorIndex2 + 3] = colors[colorIndex2];
                        colors[colorIndex2 + 4] = colors[colorIndex2 + 2];
                        colors[colorIndex2 + 5] = computeColor(color2, v4x, v4y, v4z, colorSpace2);
                        int vertexIndex3 = vertexIndex2 + 1;
                        vertices[vertexIndex2] = v1x;
                        int vertexIndex4 = vertexIndex3 + 1;
                        vertices[vertexIndex3] = v1y;
                        int vertexIndex5 = vertexIndex4 + 1;
                        vertices[vertexIndex4] = v2x;
                        int vertexIndex6 = vertexIndex5 + 1;
                        vertices[vertexIndex5] = v2y;
                        int vertexIndex7 = vertexIndex6 + 1;
                        vertices[vertexIndex6] = v3x;
                        int vertexIndex8 = vertexIndex7 + 1;
                        vertices[vertexIndex7] = v3y;
                        int vertexIndex9 = vertexIndex8 + 1;
                        vertices[vertexIndex8] = v1x;
                        int vertexIndex10 = vertexIndex9 + 1;
                        vertices[vertexIndex9] = v1y;
                        int vertexIndex11 = vertexIndex10 + 1;
                        vertices[vertexIndex10] = v3x;
                        int vertexIndex12 = vertexIndex11 + 1;
                        vertices[vertexIndex11] = v3y;
                        int vertexIndex13 = vertexIndex12 + 1;
                        vertices[vertexIndex12] = v4x;
                        vertexIndex2 = vertexIndex13 + 1;
                        vertices[vertexIndex13] = v4y;
                        c = c2 + 1;
                        colorSpace = colorSpace2;
                        color = color2;
                        colorIndex = colorIndex2 + 6;
                        x = x2;
                        nextX = nextX2;
                        radius1 = radius12;
                        radius2 = radius22;
                        a2 = a22;
                        a12 = a13;
                    }
                }
                x++;
                vertexIndex = vertexIndex2;
            }
        }

        private static synchronized int computeColor(float[] color, float x, float y, float z, ColorSpace cs) {
            color[0] = x;
            color[1] = y;
            color[2] = z;
            cs.fromXyz(color);
            return ((((int) (color[0] * 255.0f)) & 255) << 16) | (-16777216) | ((((int) (color[1] * 255.0f)) & 255) << 8) | (((int) (color[2] * 255.0f)) & 255);
        }

        private static synchronized double sqr(double v) {
            return v * v;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Point {
            final int mColor;
            final ColorSpace mColorSpace;
            final float[] mRgb;

            synchronized Point(ColorSpace colorSpace, float[] rgb, int color) {
                this.mColorSpace = colorSpace;
                this.mRgb = rgb;
                this.mColor = color;
            }
        }
    }
}
