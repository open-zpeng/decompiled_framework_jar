package android.text;

/* loaded from: classes2.dex */
public class Hyphenator {
    private static native void nInit();

    private Hyphenator() {
    }

    public static void init() {
        nInit();
    }
}
