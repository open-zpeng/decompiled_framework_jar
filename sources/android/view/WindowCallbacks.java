package android.view;

import android.graphics.RecordingCanvas;
import android.graphics.Rect;

/* loaded from: classes3.dex */
public interface WindowCallbacks {
    public static final int RESIZE_MODE_DOCKED_DIVIDER = 1;
    public static final int RESIZE_MODE_FREEFORM = 0;
    public static final int RESIZE_MODE_INVALID = -1;

    boolean onContentDrawn(int i, int i2, int i3, int i4);

    void onPostDraw(RecordingCanvas recordingCanvas);

    void onRequestDraw(boolean z);

    void onWindowDragResizeEnd();

    void onWindowDragResizeStart(Rect rect, boolean z, Rect rect2, Rect rect3, int i);

    void onWindowSizeIsChanging(Rect rect, boolean z, Rect rect2, Rect rect3);
}
