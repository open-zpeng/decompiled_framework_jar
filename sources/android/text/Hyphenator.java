package android.text;
/* loaded from: classes2.dex */
public class Hyphenator {
    private static native void nInit();

    public static synchronized void init() {
        nInit();
    }
}
