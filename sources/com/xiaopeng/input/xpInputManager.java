package com.xiaopeng.input;

import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
/* loaded from: classes3.dex */
public class xpInputManager {
    public static final long INPUT_EVENT_DELAY = 50;
    public static final long INPUT_EVENT_LONG_INTERVAL = 500;
    public static final long INPUT_EVENT_SHORT_INTERVAL = 100;

    public static final void sendEvent(int keycode) {
        try {
            long down1 = SystemClock.uptimeMillis();
            long down2 = SystemClock.uptimeMillis() + 100;
            sendEvent(keycode, down1, 0, 0);
            sendEvent(keycode, down2, 1, 0);
        } catch (Exception e) {
        }
    }

    public static final void sendEvent(int keycode, long down, int action, int flags) {
        long when = 50 + down;
        injectInputEvent(keycode, down, when, action, flags);
    }

    private static final void injectInputEvent(int keycode, long down, long when, int action, int flags) {
        int repeatCount = (flags & 128) != 0 ? 1 : 0;
        KeyEvent event = new KeyEvent(down, when, action, keycode, repeatCount, 0, -1, 0, flags | 72, 257);
        InputManager.getInstance().injectInputEvent(event, 0);
    }

    public static void dispatchToListener(KeyEvent event, int policyFlags) {
        InputManager.getInstance().dispatchKeyToListener(event, policyFlags);
    }

    public static void dispatchGenericMotionToListener(MotionEvent event, int policyFlags) {
        InputManager.getInstance().dispatchGenericMotionToListener(event, policyFlags);
    }
}
