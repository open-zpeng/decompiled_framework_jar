package com.xiaopeng;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.InputDevice;
import com.xiaopeng.app.xpPackageInfo;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes3.dex */
public class xpInputDeviceWrapper {
    private static final int MOCK_TYPE_JOYSTICK = 1;
    private static final int MOCK_TYPE_KEYBOARD = 0;
    private static final int MOCK_TYPE_NONE = -1;
    private static final String TAG = "xpInputDeviceWrapper";
    public static final String XP_KEY_GAME_MODE_FLAG = "xp.key.gamemode.flag";
    public static final String XP_KEY_GAME_SOURCE_TYPE = "xp.key.game.source.type";
    private static xpInputDeviceWrapper sInputDWrapper;
    private Wrapper mWrapper;
    private static final boolean DEBUG = Log.isLoggable("xpInputDeviceWrapper.DEBUG", 3);
    private static int mMockDeviceId = -1;
    private static int sType = 1;

    public static xpInputDeviceWrapper getInstance() {
        if (sInputDWrapper == null) {
            synchronized (xpInputDeviceWrapper.class) {
                if (sInputDWrapper == null) {
                    sInputDWrapper = new xpInputDeviceWrapper();
                }
            }
        }
        return sInputDWrapper;
    }

    public static boolean isMockDevice(int id) {
        return mMockDeviceId == id && isGameModeEnable();
    }

    public static int getKeyboardType() {
        return getInstance().getWrapper().mKeyboardType;
    }

    public static int getSource() {
        return getInstance().getWrapper().mSources;
    }

    public static boolean isVirtual() {
        return getInstance().getWrapper().mVirtual;
    }

    public static List<InputDevice.MotionRange> getMotionRanges() {
        return getInstance().getWrapper().mMotionRanges;
    }

    public static boolean isGameModeEnable() {
        boolean enable = xpPackageInfo.isXpGameModeEnable();
        if (enable) {
            updateSourceType();
            if (getInstance().getWrapper() == null || sType != getInstance().getWrapper().mType) {
                getInstance().initWrapper();
            }
        }
        return enable;
    }

    public static boolean ignoreByXpGameMode(String id) {
        if (isGameModeEnable() && !TextUtils.isEmpty(id) && !"xp.key.gamemode.flag".equals(id)) {
            return true;
        }
        return false;
    }

    private static void updateSourceType() {
        int type = SystemProperties.getInt(XP_KEY_GAME_SOURCE_TYPE, sType);
        if (type != sType) {
            sType = type;
        }
    }

    private void initWrapper() {
        this.mWrapper = buildWrapper(sType);
    }

    private Wrapper getWrapper() {
        return this.mWrapper;
    }

    private Wrapper buildWrapper(int type) {
        Wrapper wrapper = new Wrapper();
        switch (type) {
            case 0:
                wrapper.mock = true;
                wrapper.mId = mMockDeviceId;
                wrapper.mVirtual = false;
                wrapper.mKeyboardType = 2;
                wrapper.mSources = 257;
                wrapper.mType = 0;
                break;
            case 1:
                wrapper.mock = true;
                wrapper.mId = mMockDeviceId;
                wrapper.mVirtual = true;
                wrapper.mKeyboardType = 1;
                wrapper.mSources = InputDevice.SOURCE_JOYSTICK;
                wrapper.mType = 1;
                wrapper.mMotionRanges.add(InputDevice.MotionRange.build(0, 16, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f));
                wrapper.mMotionRanges.add(InputDevice.MotionRange.build(1, 16, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f));
                break;
        }
        return wrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Wrapper {
        int mId;
        int mKeyboardType;
        ArrayList<InputDevice.MotionRange> mMotionRanges;
        int mSources;
        int mType;
        boolean mVirtual;
        boolean mock;

        private Wrapper() {
            this.mMotionRanges = new ArrayList<>();
        }
    }
}
