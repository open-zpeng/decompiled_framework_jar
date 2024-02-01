package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManagerImpl;

/* loaded from: classes.dex */
public class Presentation extends Dialog {
    private static final int MSG_CANCEL = 1;
    private static final String TAG = "Presentation";
    private final Display mDisplay;
    private final DisplayManager.DisplayListener mDisplayListener;
    private final DisplayManager mDisplayManager;
    private final Handler mHandler;
    private final IBinder mToken;

    public Presentation(Context outerContext, Display display) {
        this(outerContext, display, 0);
    }

    public Presentation(Context outerContext, Display display, int theme) {
        super(createPresentationContext(outerContext, display, theme), theme, false);
        this.mToken = new Binder();
        this.mDisplayListener = new DisplayManager.DisplayListener() { // from class: android.app.Presentation.2
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int displayId) {
                if (displayId == Presentation.this.mDisplay.getDisplayId()) {
                    Presentation.this.handleDisplayRemoved();
                }
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int displayId) {
                if (displayId == Presentation.this.mDisplay.getDisplayId()) {
                    Presentation.this.handleDisplayChanged();
                }
            }
        };
        this.mHandler = new Handler() { // from class: android.app.Presentation.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Presentation.this.cancel();
                }
            }
        };
        this.mDisplay = display;
        this.mDisplayManager = (DisplayManager) getContext().getSystemService(Context.DISPLAY_SERVICE);
        int windowType = (display.getFlags() & 4) != 0 ? 2030 : 2037;
        Window w = getWindow();
        WindowManager.LayoutParams attr = w.getAttributes();
        boolean displayFocusable = displayFocusable(display);
        if (!displayFocusable) {
            attr.flags |= 8;
        }
        Log.i(TAG, "onCreate displayFlags=0x" + Integer.toHexString(display.getFlags()) + " winFlags=0x" + Integer.toHexString(attr.flags) + " displayFocusable=" + displayFocusable);
        attr.token = this.mToken;
        w.setAttributes(attr);
        w.setGravity(119);
        w.setType(windowType);
        setCanceledOnTouchOutside(false);
    }

    private static boolean displayFocusable(Display display) {
        return display == null || (display.getFlags() & 128) != 128;
    }

    public Display getDisplay() {
        return this.mDisplay;
    }

    public Resources getResources() {
        return getContext().getResources();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Dialog
    public void onStart() {
        super.onStart();
        this.mDisplayManager.registerDisplayListener(this.mDisplayListener, this.mHandler);
        if (!isConfigurationStillValid()) {
            Log.i(TAG, "Presentation is being dismissed because the display metrics have changed since it was created.");
            this.mHandler.sendEmptyMessage(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Dialog
    public void onStop() {
        this.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
        super.onStop();
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
    }

    public void onDisplayRemoved() {
    }

    public void onDisplayChanged() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDisplayRemoved() {
        onDisplayRemoved();
        cancel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDisplayChanged() {
        onDisplayChanged();
        if (!isConfigurationStillValid()) {
            Log.i(TAG, "Presentation is being dismissed because the display metrics have changed since it was created.");
            cancel();
        }
    }

    private boolean isConfigurationStillValid() {
        DisplayMetrics dm = new DisplayMetrics();
        this.mDisplay.getMetrics(dm);
        return dm.equalsPhysical(getResources().getDisplayMetrics());
    }

    @UnsupportedAppUsage
    private static Context createPresentationContext(Context outerContext, Display display, int theme) {
        if (outerContext == null) {
            throw new IllegalArgumentException("outerContext must not be null");
        }
        if (display == null) {
            throw new IllegalArgumentException("display must not be null");
        }
        Context displayContext = outerContext.createDisplayContext(display);
        if (theme == 0) {
            TypedValue outValue = new TypedValue();
            displayContext.getTheme().resolveAttribute(16843712, outValue, true);
            theme = outValue.resourceId;
        }
        WindowManagerImpl outerWindowManager = (WindowManagerImpl) outerContext.getSystemService(Context.WINDOW_SERVICE);
        final WindowManagerImpl displayWindowManager = outerWindowManager.createPresentationWindowManager(displayContext);
        return new ContextThemeWrapper(displayContext, theme) { // from class: android.app.Presentation.1
            @Override // android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
            public Object getSystemService(String name) {
                if (Context.WINDOW_SERVICE.equals(name)) {
                    return displayWindowManager;
                }
                return super.getSystemService(name);
            }
        };
    }
}
