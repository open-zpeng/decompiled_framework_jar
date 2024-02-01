package android.util;
@Deprecated
/* loaded from: classes2.dex */
public class FloatMath {
    private synchronized FloatMath() {
    }

    private protected static float floor(float value) {
        return (float) Math.floor(value);
    }

    private protected static float ceil(float value) {
        return (float) Math.ceil(value);
    }

    private protected static float sin(float angle) {
        return (float) Math.sin(angle);
    }

    private protected static float cos(float angle) {
        return (float) Math.cos(angle);
    }

    private protected static float sqrt(float value) {
        return (float) Math.sqrt(value);
    }

    private protected static float exp(float value) {
        return (float) Math.exp(value);
    }

    private protected static float pow(float x, float y) {
        return (float) Math.pow(x, y);
    }

    private protected static float hypot(float x, float y) {
        return (float) Math.hypot(x, y);
    }
}
