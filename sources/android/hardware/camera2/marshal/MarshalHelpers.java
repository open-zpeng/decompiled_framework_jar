package android.hardware.camera2.marshal;

import android.util.Rational;
import com.android.internal.util.Preconditions;

/* loaded from: classes.dex */
public final class MarshalHelpers {
    public static final int SIZEOF_BYTE = 1;
    public static final int SIZEOF_DOUBLE = 8;
    public static final int SIZEOF_FLOAT = 4;
    public static final int SIZEOF_INT32 = 4;
    public static final int SIZEOF_INT64 = 8;
    public static final int SIZEOF_RATIONAL = 8;

    public static int getPrimitiveTypeSize(int nativeType) {
        if (nativeType != 0) {
            if (nativeType == 1 || nativeType == 2) {
                return 4;
            }
            if (nativeType == 3 || nativeType == 4 || nativeType == 5) {
                return 8;
            }
            throw new UnsupportedOperationException("Unknown type, can't get size for " + nativeType);
        }
        return 1;
    }

    public static <T> Class<T> checkPrimitiveClass(Class<T> klass) {
        Preconditions.checkNotNull(klass, "klass must not be null");
        if (isPrimitiveClass(klass)) {
            return klass;
        }
        throw new UnsupportedOperationException("Unsupported class '" + klass + "'; expected a metadata primitive class");
    }

    public static <T> boolean isPrimitiveClass(Class<T> klass) {
        if (klass == null) {
            return false;
        }
        return klass == Byte.TYPE || klass == Byte.class || klass == Integer.TYPE || klass == Integer.class || klass == Float.TYPE || klass == Float.class || klass == Long.TYPE || klass == Long.class || klass == Double.TYPE || klass == Double.class || klass == Rational.class;
    }

    public static <T> Class<T> wrapClassIfPrimitive(Class<T> klass) {
        if (klass == Byte.TYPE) {
            return Byte.class;
        }
        if (klass == Integer.TYPE) {
            return Integer.class;
        }
        if (klass == Float.TYPE) {
            return Float.class;
        }
        if (klass == Long.TYPE) {
            return Long.class;
        }
        if (klass == Double.TYPE) {
            return Double.class;
        }
        return klass;
    }

    public static String toStringNativeType(int nativeType) {
        if (nativeType != 0) {
            if (nativeType != 1) {
                if (nativeType != 2) {
                    if (nativeType != 3) {
                        if (nativeType != 4) {
                            if (nativeType == 5) {
                                return "TYPE_RATIONAL";
                            }
                            return "UNKNOWN(" + nativeType + ")";
                        }
                        return "TYPE_DOUBLE";
                    }
                    return "TYPE_INT64";
                }
                return "TYPE_FLOAT";
            }
            return "TYPE_INT32";
        }
        return "TYPE_BYTE";
    }

    public static int checkNativeType(int nativeType) {
        if (nativeType == 0 || nativeType == 1 || nativeType == 2 || nativeType == 3 || nativeType == 4 || nativeType == 5) {
            return nativeType;
        }
        throw new UnsupportedOperationException("Unknown nativeType " + nativeType);
    }

    public static int checkNativeTypeEquals(int expectedNativeType, int actualNativeType) {
        if (expectedNativeType != actualNativeType) {
            throw new UnsupportedOperationException(String.format("Expected native type %d, but got %d", Integer.valueOf(expectedNativeType), Integer.valueOf(actualNativeType)));
        }
        return actualNativeType;
    }

    private MarshalHelpers() {
        throw new AssertionError();
    }
}
