package com.xiaopeng.input;

import android.hardware.input.IInputManager;
import android.hardware.input.InputManager;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.xiaopeng.view.SharedDisplayManager;

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

    public static final void sendEvent(int keycode, int source) {
        try {
            long down1 = SystemClock.uptimeMillis();
            long down2 = SystemClock.uptimeMillis() + 100;
            sendEvent(keycode, down1, 0, 0, source);
            sendEvent(keycode, down2, 1, 0, source);
        } catch (Exception e) {
        }
    }

    public static final void sendEvent(int keycode, long down, int action, int flags) {
        long when = 50 + down;
        injectInputEvent(keycode, down, when, action, flags, 257);
    }

    public static final void sendEvent(int keycode, long down, int action, int flags, int source) {
        long when = 50 + down;
        injectInputEvent(keycode, down, when, action, flags, source);
    }

    private static final void injectInputEvent(int keycode, long down, long when, int action, int flags, int source) {
        int repeatCount = (flags & 128) != 0 ? 1 : 0;
        KeyEvent event = new KeyEvent(down, when, action, keycode, repeatCount, 0, -1, 0, flags | 72, source);
        InputManager.getInstance().injectInputEvent(event, 0);
    }

    public static void dispatchToListener(KeyEvent event, int policyFlags) {
        InputManager.getInstance().dispatchKeyToListener(event, policyFlags);
    }

    public static void dispatchGenericMotionToListener(MotionEvent event, int policyFlags) {
        InputManager.getInstance().dispatchGenericMotionToListener(event, policyFlags);
    }

    public static void setInputPolicy(int policy) {
        try {
            IInputManager im = IInputManager.Stub.asInterface(ServiceManager.getServiceOrThrow("input"));
            im.setInputPolicy(policy);
        } catch (Exception e) {
        }
    }

    public static int getInputPolicy() {
        try {
            IInputManager im = IInputManager.Stub.asInterface(ServiceManager.getServiceOrThrow("input"));
            return im.getInputPolicy();
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean hasInputWindowFocus(InputEvent event, boolean focus) {
        if (event == null) {
            return focus;
        }
        if (((event instanceof KeyEvent) || (event instanceof MotionEvent)) && SharedDisplayManager.enable()) {
            return true;
        }
        return focus;
    }

    public static void setInputSourcePolicy(int inputSource, int policy) {
        try {
            IInputManager im = IInputManager.Stub.asInterface(ServiceManager.getServiceOrThrow("input"));
            im.setInputSourcePolicy(inputSource, policy);
        } catch (Exception e) {
        }
    }
}
