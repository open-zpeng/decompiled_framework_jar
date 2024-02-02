package android.view;

import android.graphics.Rect;
/* loaded from: classes2.dex */
public interface WindowCallbacks {
    public static final int RESIZE_MODE_DOCKED_DIVIDER = 1;
    public static final int RESIZE_MODE_FREEFORM = 0;
    public static final int RESIZE_MODE_INVALID = -1;

    synchronized boolean onContentDrawn(int i, int i2, int i3, int i4);

    synchronized void onPostDraw(DisplayListCanvas displayListCanvas);

    synchronized void onRequestDraw(boolean z);

    synchronized void onWindowDragResizeEnd();

    synchronized void onWindowDragResizeStart(Rect rect, boolean z, Rect rect2, Rect rect3, int i);

    synchronized void onWindowSizeIsChanging(Rect rect, boolean z, Rect rect2, Rect rect3);
}
