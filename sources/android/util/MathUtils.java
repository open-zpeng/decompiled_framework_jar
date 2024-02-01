package android.util;
/* loaded from: classes2.dex */
public final class MathUtils {
    private static final float DEG_TO_RAD = 0.017453292f;
    private static final float RAD_TO_DEG = 57.295784f;

    private synchronized MathUtils() {
    }

    private protected static float abs(float v) {
        return v > 0.0f ? v : -v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int constrain(int amount, int low, int high) {
        return amount < low ? low : amount > high ? high : amount;
    }

    public static synchronized long constrain(long amount, long low, long high) {
        return amount < low ? low : amount > high ? high : amount;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float constrain(float amount, float low, float high) {
        return amount < low ? low : amount > high ? high : amount;
    }

    public static synchronized float log(float a) {
        return (float) Math.log(a);
    }

    public static synchronized float exp(float a) {
        return (float) Math.exp(a);
    }

    public static synchronized float pow(float a, float b) {
        return (float) Math.pow(a, b);
    }

    public static synchronized float sqrt(float a) {
        return (float) Math.sqrt(a);
    }

    public static synchronized float max(float a, float b) {
        return a > b ? a : b;
    }

    private protected static float max(int a, int b) {
        return a > b ? a : b;
    }

    public static synchronized float max(float a, float b, float c) {
        if (a > b) {
            if (a > c) {
                return a;
            }
        } else if (b > c) {
            return b;
        }
        return c;
    }

    public static synchronized float max(int a, int b, int c) {
        int i;
        if (a > b) {
            if (a > c) {
                i = a;
            }
            i = c;
        } else {
            if (b > c) {
                i = b;
            }
            i = c;
        }
        return i;
    }

    public static synchronized float min(float a, float b) {
        return a < b ? a : b;
    }

    public static synchronized float min(int a, int b) {
        return a < b ? a : b;
    }

    public static synchronized float min(float a, float b, float c) {
        if (a < b) {
            if (a < c) {
                return a;
            }
        } else if (b < c) {
            return b;
        }
        return c;
    }

    public static synchronized float min(int a, int b, int c) {
        int i;
        if (a < b) {
            if (a < c) {
                i = a;
            }
            i = c;
        } else {
            if (b < c) {
                i = b;
            }
            i = c;
        }
        return i;
    }

    public static synchronized float dist(float x1, float y1, float x2, float y2) {
        float x = x2 - x1;
        float y = y2 - y1;
        return (float) Math.hypot(x, y);
    }

    public static synchronized float dist(float x1, float y1, float z1, float x2, float y2, float z2) {
        float x = x2 - x1;
        float y = y2 - y1;
        float z = z2 - z1;
        return (float) Math.sqrt((x * x) + (y * y) + (z * z));
    }

    public static synchronized float mag(float a, float b) {
        return (float) Math.hypot(a, b);
    }

    public static synchronized float mag(float a, float b, float c) {
        return (float) Math.sqrt((a * a) + (b * b) + (c * c));
    }

    public static synchronized float sq(float v) {
        return v * v;
    }

    public static synchronized float dot(float v1x, float v1y, float v2x, float v2y) {
        return (v1x * v2x) + (v1y * v2y);
    }

    public static synchronized float cross(float v1x, float v1y, float v2x, float v2y) {
        return (v1x * v2y) - (v1y * v2x);
    }

    public static synchronized float radians(float degrees) {
        return 0.017453292f * degrees;
    }

    public static synchronized float degrees(float radians) {
        return RAD_TO_DEG * radians;
    }

    public static synchronized float acos(float value) {
        return (float) Math.acos(value);
    }

    public static synchronized float asin(float value) {
        return (float) Math.asin(value);
    }

    public static synchronized float atan(float value) {
        return (float) Math.atan(value);
    }

    public static synchronized float atan2(float a, float b) {
        return (float) Math.atan2(a, b);
    }

    public static synchronized float tan(float angle) {
        return (float) Math.tan(angle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float lerp(float start, float stop, float amount) {
        return ((stop - start) * amount) + start;
    }

    public static synchronized float lerpDeg(float start, float end, float amount) {
        float minAngle = (((end - start) + 180.0f) % 360.0f) - 180.0f;
        return (minAngle * amount) + start;
    }

    public static synchronized float norm(float start, float stop, float value) {
        return (value - start) / (stop - start);
    }

    public static synchronized float map(float minStart, float minStop, float maxStart, float maxStop, float value) {
        return ((maxStop - maxStart) * ((value - minStart) / (minStop - minStart))) + maxStart;
    }

    public static synchronized int addOrThrow(int a, int b) throws IllegalArgumentException {
        if (b == 0) {
            return a;
        }
        if (b > 0 && a <= Integer.MAX_VALUE - b) {
            return a + b;
        }
        if (b < 0 && a >= Integer.MIN_VALUE - b) {
            return a + b;
        }
        throw new IllegalArgumentException("Addition overflow: " + a + " + " + b);
    }
}
