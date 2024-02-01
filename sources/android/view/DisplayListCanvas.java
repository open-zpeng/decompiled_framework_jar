package android.view;

import android.annotation.UnsupportedAppUsage;
import android.graphics.BaseRecordingCanvas;
import android.graphics.CanvasProperty;
import android.graphics.Paint;

/* loaded from: classes3.dex */
public abstract class DisplayListCanvas extends BaseRecordingCanvas {
    @UnsupportedAppUsage
    public abstract void drawCircle(CanvasProperty<Float> canvasProperty, CanvasProperty<Float> canvasProperty2, CanvasProperty<Float> canvasProperty3, CanvasProperty<Paint> canvasProperty4);

    @UnsupportedAppUsage
    public abstract void drawRoundRect(CanvasProperty<Float> canvasProperty, CanvasProperty<Float> canvasProperty2, CanvasProperty<Float> canvasProperty3, CanvasProperty<Float> canvasProperty4, CanvasProperty<Float> canvasProperty5, CanvasProperty<Float> canvasProperty6, CanvasProperty<Paint> canvasProperty7);

    /* JADX INFO: Access modifiers changed from: protected */
    public DisplayListCanvas(long nativeCanvas) {
        super(nativeCanvas);
    }
}
