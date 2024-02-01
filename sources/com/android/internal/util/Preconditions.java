package com.android.internal.util;

import android.annotation.UnsupportedAppUsage;
import android.text.TextUtils;
import java.util.Collection;

/* loaded from: classes3.dex */
public class Preconditions {
    @UnsupportedAppUsage
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    @UnsupportedAppUsage
    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public static void checkArgument(boolean expression, String messageTemplate, Object... messageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(messageTemplate, messageArgs));
        }
    }

    public static <T extends CharSequence> T checkStringNotEmpty(T string) {
        if (TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException();
        }
        return string;
    }

    public static <T extends CharSequence> T checkStringNotEmpty(T string, Object errorMessage) {
        if (TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
        return string;
    }

    @UnsupportedAppUsage
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    @UnsupportedAppUsage
    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, String messageTemplate, Object... messageArgs) {
        if (reference == null) {
            throw new NullPointerException(String.format(messageTemplate, messageArgs));
        }
        return reference;
    }

    @UnsupportedAppUsage
    public static void checkState(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    @UnsupportedAppUsage
    public static void checkState(boolean expression) {
        checkState(expression, null);
    }

    public static int checkFlagsArgument(int requestedFlags, int allowedFlags) {
        if ((requestedFlags & allowedFlags) != requestedFlags) {
            throw new IllegalArgumentException("Requested flags 0x" + Integer.toHexString(requestedFlags) + ", but only 0x" + Integer.toHexString(allowedFlags) + " are allowed");
        }
        return requestedFlags;
    }

    public static int checkArgumentNonnegative(int value, String errorMessage) {
        if (value < 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }

    public static int checkArgumentNonnegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    public static long checkArgumentNonnegative(long value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    public static long checkArgumentNonnegative(long value, String errorMessage) {
        if (value < 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }

    public static int checkArgumentPositive(int value, String errorMessage) {
        if (value <= 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }

    public static float checkArgumentNonNegative(float value, String errorMessage) {
        if (value < 0.0f) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }

    public static float checkArgumentPositive(float value, String errorMessage) {
        if (value <= 0.0f) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }

    public static float checkArgumentFinite(float value, String valueName) {
        if (Float.isNaN(value)) {
            throw new IllegalArgumentException(valueName + " must not be NaN");
        } else if (Float.isInfinite(value)) {
            throw new IllegalArgumentException(valueName + " must not be infinite");
        } else {
            return value;
        }
    }

    public static float checkArgumentInRange(float value, float lower, float upper, String valueName) {
        if (Float.isNaN(value)) {
            throw new IllegalArgumentException(valueName + " must not be NaN");
        } else if (value < lower) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%f, %f] (too low)", valueName, Float.valueOf(lower), Float.valueOf(upper)));
        } else {
            if (value > upper) {
                throw new IllegalArgumentException(String.format("%s is out of range of [%f, %f] (too high)", valueName, Float.valueOf(lower), Float.valueOf(upper)));
            }
            return value;
        }
    }

    @UnsupportedAppUsage
    public static int checkArgumentInRange(int value, int lower, int upper, String valueName) {
        if (value < lower) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%d, %d] (too low)", valueName, Integer.valueOf(lower), Integer.valueOf(upper)));
        }
        if (value > upper) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%d, %d] (too high)", valueName, Integer.valueOf(lower), Integer.valueOf(upper)));
        }
        return value;
    }

    public static long checkArgumentInRange(long value, long lower, long upper, String valueName) {
        if (value < lower) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%d, %d] (too low)", valueName, Long.valueOf(lower), Long.valueOf(upper)));
        }
        if (value > upper) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%d, %d] (too high)", valueName, Long.valueOf(lower), Long.valueOf(upper)));
        }
        return value;
    }

    public static <T> T[] checkArrayElementsNotNull(T[] value, String valueName) {
        if (value == null) {
            throw new NullPointerException(valueName + " must not be null");
        }
        for (int i = 0; i < value.length; i++) {
            if (value[i] == null) {
                throw new NullPointerException(String.format("%s[%d] must not be null", valueName, Integer.valueOf(i)));
            }
        }
        return value;
    }

    public static <C extends Collection<T>, T> C checkCollectionElementsNotNull(C value, String valueName) {
        if (value == null) {
            throw new NullPointerException(valueName + " must not be null");
        }
        long ctr = 0;
        for (Object obj : value) {
            if (obj == null) {
                throw new NullPointerException(String.format("%s[%d] must not be null", valueName, Long.valueOf(ctr)));
            }
            ctr++;
        }
        return value;
    }

    public static <T> Collection<T> checkCollectionNotEmpty(Collection<T> value, String valueName) {
        if (value == null) {
            throw new NullPointerException(valueName + " must not be null");
        } else if (value.isEmpty()) {
            throw new IllegalArgumentException(valueName + " is empty");
        } else {
            return value;
        }
    }

    public static float[] checkArrayElementsInRange(float[] value, float lower, float upper, String valueName) {
        checkNotNull(value, valueName + " must not be null");
        for (int i = 0; i < value.length; i++) {
            float v = value[i];
            if (Float.isNaN(v)) {
                throw new IllegalArgumentException(valueName + "[" + i + "] must not be NaN");
            } else if (v < lower) {
                throw new IllegalArgumentException(String.format("%s[%d] is out of range of [%f, %f] (too low)", valueName, Integer.valueOf(i), Float.valueOf(lower), Float.valueOf(upper)));
            } else {
                if (v > upper) {
                    throw new IllegalArgumentException(String.format("%s[%d] is out of range of [%f, %f] (too high)", valueName, Integer.valueOf(i), Float.valueOf(lower), Float.valueOf(upper)));
                }
            }
        }
        return value;
    }

    public static int[] checkArrayElementsInRange(int[] value, int lower, int upper, String valueName) {
        checkNotNull(value, valueName + " must not be null");
        for (int i = 0; i < value.length; i++) {
            int v = value[i];
            if (v < lower) {
                throw new IllegalArgumentException(String.format("%s[%d] is out of range of [%d, %d] (too low)", valueName, Integer.valueOf(i), Integer.valueOf(lower), Integer.valueOf(upper)));
            }
            if (v > upper) {
                throw new IllegalArgumentException(String.format("%s[%d] is out of range of [%d, %d] (too high)", valueName, Integer.valueOf(i), Integer.valueOf(lower), Integer.valueOf(upper)));
            }
        }
        return value;
    }
}
