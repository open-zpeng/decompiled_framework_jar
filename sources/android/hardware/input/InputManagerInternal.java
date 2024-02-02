package android.hardware.input;

import android.hardware.display.DisplayViewport;
import android.view.InputEvent;
import java.util.List;
/* loaded from: classes.dex */
public abstract class InputManagerInternal {
    public abstract synchronized boolean injectInputEvent(InputEvent inputEvent, int i, int i2);

    public abstract void setDisplayViewports(DisplayViewport displayViewport, DisplayViewport displayViewport2, DisplayViewport displayViewport3, List<DisplayViewport> list);

    public abstract synchronized void setDisplayViewports(DisplayViewport displayViewport, DisplayViewport displayViewport2, List<DisplayViewport> list);

    public abstract synchronized void setInteractive(boolean z);

    public abstract synchronized void setPulseGestureEnabled(boolean z);

    public abstract synchronized void toggleCapsLock(int i);
}
