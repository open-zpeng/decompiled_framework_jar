package android.util;

import android.app.backup.FullBackup;

/* loaded from: classes2.dex */
public class TypedValue {
    public static final int COMPLEX_MANTISSA_MASK = 16777215;
    public static final int COMPLEX_MANTISSA_SHIFT = 8;
    public static final int COMPLEX_RADIX_0p23 = 3;
    public static final int COMPLEX_RADIX_16p7 = 1;
    public static final int COMPLEX_RADIX_23p0 = 0;
    public static final int COMPLEX_RADIX_8p15 = 2;
    public static final int COMPLEX_RADIX_MASK = 3;
    public static final int COMPLEX_RADIX_SHIFT = 4;
    public static final int COMPLEX_UNIT_DIP = 1;
    public static final int COMPLEX_UNIT_FRACTION = 0;
    public static final int COMPLEX_UNIT_FRACTION_PARENT = 1;
    public static final int COMPLEX_UNIT_IN = 4;
    public static final int COMPLEX_UNIT_MASK = 15;
    public static final int COMPLEX_UNIT_MM = 5;
    public static final int COMPLEX_UNIT_PT = 3;
    public static final int COMPLEX_UNIT_PX = 0;
    public static final int COMPLEX_UNIT_SHIFT = 0;
    public static final int COMPLEX_UNIT_SP = 2;
    public static final int DATA_NULL_EMPTY = 1;
    public static final int DATA_NULL_UNDEFINED = 0;
    public static final int DENSITY_DEFAULT = 0;
    public static final int DENSITY_NONE = 65535;
    public static final int TYPE_ATTRIBUTE = 2;
    public static final int TYPE_DIMENSION = 5;
    public static final int TYPE_FIRST_COLOR_INT = 28;
    public static final int TYPE_FIRST_INT = 16;
    public static final int TYPE_FLOAT = 4;
    public static final int TYPE_FRACTION = 6;
    public static final int TYPE_INT_BOOLEAN = 18;
    public static final int TYPE_INT_COLOR_ARGB4 = 30;
    public static final int TYPE_INT_COLOR_ARGB8 = 28;
    public static final int TYPE_INT_COLOR_RGB4 = 31;
    public static final int TYPE_INT_COLOR_RGB8 = 29;
    public static final int TYPE_INT_DEC = 16;
    public static final int TYPE_INT_HEX = 17;
    public static final int TYPE_LAST_COLOR_INT = 31;
    public static final int TYPE_LAST_INT = 31;
    public static final int TYPE_NULL = 0;
    public static final int TYPE_REFERENCE = 1;
    public static final int TYPE_STRING = 3;
    public int assetCookie;
    public int changingConfigurations = -1;
    public int data;
    public int density;
    public int resourceId;
    public int sourceResourceId;
    public CharSequence string;
    public int type;
    private static final float MANTISSA_MULT = 0.00390625f;
    private static final float[] RADIX_MULTS = {MANTISSA_MULT, 3.0517578E-5f, 1.1920929E-7f, 4.656613E-10f};
    private static final String[] DIMENSION_UNIT_STRS = {"px", "dip", FullBackup.SHAREDPREFS_TREE_TOKEN, "pt", "in", "mm"};
    private static final String[] FRACTION_UNIT_STRS = {"%", "%p"};

    public final float getFloat() {
        return Float.intBitsToFloat(this.data);
    }

    public boolean isColorType() {
        int i = this.type;
        return i >= 28 && i <= 31;
    }

    public static float complexToFloat(int complex) {
        return (complex & (-256)) * RADIX_MULTS[(complex >> 4) & 3];
    }

    public static float complexToDimension(int data, DisplayMetrics metrics) {
        return applyDimension((data >> 0) & 15, complexToFloat(data), metrics);
    }

    public static int complexToDimensionPixelOffset(int data, DisplayMetrics metrics) {
        return (int) applyDimension((data >> 0) & 15, complexToFloat(data), metrics);
    }

    public static int complexToDimensionPixelSize(int data, DisplayMetrics metrics) {
        float value = complexToFloat(data);
        float f = applyDimension((data >> 0) & 15, value, metrics);
        int res = (int) (f >= 0.0f ? 0.5f + f : f - 0.5f);
        if (res != 0) {
            return res;
        }
        if (value == 0.0f) {
            return 0;
        }
        return value > 0.0f ? 1 : -1;
    }

    @Deprecated
    public static float complexToDimensionNoisy(int data, DisplayMetrics metrics) {
        return complexToDimension(data, metrics);
    }

    public int getComplexUnit() {
        return (this.data >> 0) & 15;
    }

    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        if (unit != 0) {
            if (unit != 1) {
                if (unit != 2) {
                    if (unit != 3) {
                        if (unit != 4) {
                            if (unit == 5) {
                                return metrics.xdpi * value * 0.03937008f;
                            }
                            return 0.0f;
                        }
                        return metrics.xdpi * value;
                    }
                    return metrics.xdpi * value * 0.013888889f;
                }
                return metrics.scaledDensity * value;
            }
            return metrics.density * value;
        }
        return value;
    }

    public float getDimension(DisplayMetrics metrics) {
        return complexToDimension(this.data, metrics);
    }

    public static float complexToFraction(int data, float base, float pbase) {
        int i = (data >> 0) & 15;
        if (i != 0) {
            if (i == 1) {
                return complexToFloat(data) * pbase;
            }
            return 0.0f;
        }
        return complexToFloat(data) * base;
    }

    public float getFraction(float base, float pbase) {
        return complexToFraction(this.data, base, pbase);
    }

    public final CharSequence coerceToString() {
        int t = this.type;
        if (t == 3) {
            return this.string;
        }
        return coerceToString(t, this.data);
    }

    public static final String coerceToString(int type, int data) {
        if (type != 0) {
            if (type == 1) {
                return "@" + data;
            } else if (type == 2) {
                return "?" + data;
            } else if (type != 4) {
                if (type == 5) {
                    return Float.toString(complexToFloat(data)) + DIMENSION_UNIT_STRS[(data >> 0) & 15];
                } else if (type == 6) {
                    return Float.toString(complexToFloat(data) * 100.0f) + FRACTION_UNIT_STRS[(data >> 0) & 15];
                } else if (type == 17) {
                    return "0x" + Integer.toHexString(data);
                } else if (type == 18) {
                    return data != 0 ? "true" : "false";
                } else if (type >= 28 && type <= 31) {
                    return "#" + Integer.toHexString(data);
                } else if (type < 16 || type > 31) {
                    return null;
                } else {
                    return Integer.toString(data);
                }
            } else {
                return Float.toString(Float.intBitsToFloat(data));
            }
        }
        return null;
    }

    public void setTo(TypedValue other) {
        this.type = other.type;
        this.string = other.string;
        this.data = other.data;
        this.assetCookie = other.assetCookie;
        this.resourceId = other.resourceId;
        this.density = other.density;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TypedValue{t=0x");
        sb.append(Integer.toHexString(this.type));
        sb.append("/d=0x");
        sb.append(Integer.toHexString(this.data));
        if (this.type == 3) {
            sb.append(" \"");
            CharSequence charSequence = this.string;
            if (charSequence == null) {
                charSequence = "<null>";
            }
            sb.append(charSequence);
            sb.append("\"");
        }
        if (this.assetCookie != 0) {
            sb.append(" a=");
            sb.append(this.assetCookie);
        }
        if (this.resourceId != 0) {
            sb.append(" r=0x");
            sb.append(Integer.toHexString(this.resourceId));
        }
        sb.append("}");
        return sb.toString();
    }
}
