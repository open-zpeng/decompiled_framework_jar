package android.hardware.camera2.utils;
/* loaded from: classes.dex */
public class UncheckedThrow {
    public static synchronized void throwAnyException(Exception e) {
        throwAnyImpl(e);
    }

    public static synchronized void throwAnyException(Throwable e) {
        throwAnyImpl(e);
    }

    private static synchronized <T extends Throwable> void throwAnyImpl(Throwable e) throws Throwable {
        throw e;
    }
}
