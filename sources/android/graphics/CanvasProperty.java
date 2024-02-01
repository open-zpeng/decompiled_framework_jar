package android.graphics;

import com.android.internal.util.VirtualRefBasePtr;
/* loaded from: classes.dex */
public final class CanvasProperty<T> {
    private VirtualRefBasePtr mProperty;

    private static native long nCreateFloat(float f);

    private static native long nCreatePaint(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static CanvasProperty<Float> createFloat(float initialValue) {
        return new CanvasProperty<>(nCreateFloat(initialValue));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static CanvasProperty<Paint> createPaint(Paint initialValue) {
        return new CanvasProperty<>(nCreatePaint(initialValue.getNativeInstance()));
    }

    private synchronized CanvasProperty(long nativeContainer) {
        this.mProperty = new VirtualRefBasePtr(nativeContainer);
    }

    public synchronized long getNativeContainer() {
        return this.mProperty.get();
    }
}
