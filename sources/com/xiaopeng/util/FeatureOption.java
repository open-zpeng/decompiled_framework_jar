package com.xiaopeng.util;

import android.os.SystemProperties;
import android.util.Log;
import com.xiaopeng.util.FeatureFactory;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class FeatureOption {
    public static final int FO_AMP_TYPE;
    public static final int FO_AUDIO_EFFECT_DYNAUDIO_DEFTPE;
    public static final int FO_AUDIO_EFFECT_FORM;
    public static final int FO_AUDIO_EFFECT_XSOUND_DEFTPE;
    public static final boolean FO_AUDIO_STEREO_ENABLED;
    public static final int FO_AUDIO_SYSTEM_VOLUME;
    public static final boolean FO_AVAS_SAYHI_DEFAULT;
    public static final int FO_AVAS_SUPPORT_TYPE;
    public static final int FO_EXTERNAL_DISPLAY_DENSITY;
    public static final int FO_FRAGRANCE_PROTOCAL;
    public static final int FO_GESTURE_SLIDE_TYPE;
    public static final boolean FO_INDEPENDENT_AUTOMODE_ENABLED;
    public static final int FO_MEDIA_LAUNCHER_POLICY;
    public static final int FO_MEDIA_RESTORE_VOLUME;
    public static final int FO_MEDIA_RESTORE_VOLUME_AMP;
    public static final boolean FO_NATIVE_E_CALL_ENABLE;
    public static final int FO_PASSENGER_BT_AUDIO_EXIST;
    public static final int FO_PROJECT_UI_TYPE;
    public static final boolean FO_RESTORE_USER_BRIGHTNESS;
    public static final int FO_ROUND_CORNER_RADIUS;
    public static final int FO_SCREEN_NUM;
    public static final String FO_SECONDARY_HOME_COMPONENT;
    public static final boolean FO_SHARED_DISPLAY_ENABLED;
    public static final boolean FO_SYSTEMUI_SPEECHUI_ASR_OLD;
    private static final String PROPERTY_NOT_SUPPORT = "0";
    private static final String PROPERTY_SUPPORT = "1";
    public static final String TAG = "FeatureOption";
    private static HashMap<String, Boolean> sFeatureSupport = new HashMap<>();
    private static HashMap<String, String> sFeatureType = new HashMap<>();
    private static final FeatureFactory.IFeatureInterface sInterface = new FeatureFactory.DefaultFeatureInterface();
    public static final boolean FO_HOME_DESKTOP_SUPPORT = getBoolean("fo.home.desktop.support", true);
    public static final boolean FO_ACTIVITY_ZOOM_ENABLED = getBoolean("fo.activity.zoom.enabled", true);
    public static final boolean FO_ACTIVITY_AUTO_ADAPTION_ENABLED = getBoolean("fo.activity.auto.adaption.enabled", false);
    public static final boolean FO_PACKAGE_FORCE_STOP_ENABLED = getBoolean("fo.package.force.stop.enable", false);
    public static final boolean FO_DEVICE_INTERNATIONAL_ENABLED = getBoolean("fo.device.international.enabled", false);
    public static final boolean FO_A2DP_AUTODISCONNECT_ENABLED = getBoolean("fo.a2dp.autodisconnect.enabled", true);
    public static final boolean FO_MINIRPOG_ENABLED = getBoolean("fo.mini.program.enabled", false);
    public static final String FO_MINI_PROGRAM_PACKAGES = getString("fo.mini.program.packages", "");
    public static final boolean FO_INPUT_RESYNTHESIZED_LISTENER_ENABLED = getBoolean("fo.input.resynthesized.listener.enabled", true);
    public static final boolean FO_FLY_GAME_ENABLED = getBoolean("fo.fly.game.enabled", true);
    public static final boolean FO_BOOT_POLICY_ENABLED = getBoolean("fo.boot.policy.enabled", false);
    public static final double FO_BOOT_POLICY_CPU = getDouble("fo.boot.policy.cpu", 0.5d);
    public static final int FO_BOOT_POLICY_TIMEOUT = getInt("fo.boot.policy.timeout", 2);
    public static final int FO_PRODUCT_SERIES = getInt("fo.product.series", 1);
    public static final int FO_ICM_TYPE = getInt("fo.device.icm.type", 2);
    public static final boolean FO_FACE_ANGLE_ENABLED = getBoolean("fo.face.angle.enabled", false);
    public static final boolean FO_SCREEN_ROTATION_ENABLED = getBoolean("fo.screen.rotation.enabled", false);
    public static final int FO_AUTO_BRIGHTNESS_TYPE = getInt("fo.auto.brightness.type", 0);
    public static final boolean FO_APP_ICON_OVERLAY_ENABLED = getBoolean("fo.app.icon.overlay.enabled", true);
    public static final int FO_APP_ICON_DENSITY = getInt("fo.app.icon.density", 320);
    public static final boolean FO_ANIMA_SUPPORT = getBoolean("fo.animation.support", true);
    public static final boolean FO_XUI_PROCESS_HELP_LAUNCH = getBoolean("fo.xui.process.help.launch", false);
    public static final boolean FO_REMOTE_SERVICE_ENABLE = getBoolean("fo.xui.remoteservice.enable", true);
    public static final boolean FO_SUPPORT_INFOFLOW = getBoolean("fo.support.infoflow", true);
    public static final boolean FO_SUPPORT_PHONE_MENTION = getBoolean("fo.support.phonemention", false);
    public static final int FO_EPS_TYPE = getInt("fo.device.eps.type", 1);
    public static final int FO_PARKING_FMB_LIGHT_AUTO_TYPE = getInt("fo.parking.fmb.auto.type", 0);
    public static final int FO_LLU_TYPE = getInt("fo.device.llu.type", 1);
    public static final int FO_LLU_FRONT_NUM = getInt("fo.device.llu.front.num", 112);
    public static final int FO_LLU_REAR_NUM = getInt("fo.device.llu.rear.num", 124);
    public static final boolean FO_AI_LLU_ENABLED = getBoolean("fo.ai.llu.enabled", false);
    public static final int FO_ATL_TYPE = getInt("fo.device.atl.type", 2);
    public static final int FO_BOOT_SOUND_SUPPORT_TYPE = getInt("fo.boot.sound.support.type", 1);
    public static final int FO_BACKLIGHT_OVERTEMP_PROTECT = getInt("fo.backlight.overtemp.protect", 0);
    public static final String FO_CFC_CODE_PROPERTY = getString("fo.cfccode.property", "persist.sys.xiaopeng.cfcIndex");

    static {
        boolean z;
        if (getInt("persist.sys.xp.shared_display.enable", 0, true) == 1) {
            z = true;
        } else {
            z = false;
        }
        FO_SHARED_DISPLAY_ENABLED = z;
        FO_SCREEN_NUM = getInt("fo.screen.number", 2);
        FO_ROUND_CORNER_RADIUS = getInt("fo.window.round.corner.radius", 16);
        FO_SECONDARY_HOME_COMPONENT = getString("fo.secondary.home.component", "com.xiaopeng.instrument/com.xiaopeng.instrument.view.MainActivity");
        FO_AVAS_SUPPORT_TYPE = getInt("fo.avas.support.type", 1);
        FO_AVAS_SAYHI_DEFAULT = getBoolean("fo.avas.sayhi.default", true);
        FO_FRAGRANCE_PROTOCAL = getInt("fo.fragrance.protocal", 1);
        FO_PROJECT_UI_TYPE = getInt("fo.ui.type", 2);
        FO_AMP_TYPE = getInt("fo.amp.type", 1);
        FO_AUDIO_EFFECT_FORM = getInt("fo.audio.effect.form", 2);
        FO_AUDIO_STEREO_ENABLED = getBoolean("fo.audio.stereo.enabled", true);
        FO_PASSENGER_BT_AUDIO_EXIST = getInt("fo.audio.passager.btaudio", 0);
        FO_AUDIO_SYSTEM_VOLUME = getInt("fo.audio.system.volume", 15);
        FO_MEDIA_RESTORE_VOLUME = getInt("fo.media.restore.volume", 8);
        FO_MEDIA_RESTORE_VOLUME_AMP = getInt("fo.media.restore.volume.amp", 5);
        FO_AUDIO_EFFECT_XSOUND_DEFTPE = getInt("fo.audio.effect.xsound.type", 1);
        FO_AUDIO_EFFECT_DYNAUDIO_DEFTPE = getInt("fo.audio.effect.dynaudio.type", 3);
        FO_MEDIA_LAUNCHER_POLICY = getInt("fo.media.launcher.policy", 3);
        FO_GESTURE_SLIDE_TYPE = getInt("fo.gesture.slide.type", 0);
        FO_INDEPENDENT_AUTOMODE_ENABLED = getBoolean("fo.systemui.independent.automode", true);
        FO_NATIVE_E_CALL_ENABLE = getBoolean("fo.native.ecall.enable", false);
        FO_SYSTEMUI_SPEECHUI_ASR_OLD = getBoolean("fo.systemui.speechui.asr.old", false);
        FO_RESTORE_USER_BRIGHTNESS = getBoolean("fo.systemui.restore.user.brigtness", false);
        FO_EXTERNAL_DISPLAY_DENSITY = getInt("fo.external.display.density", 0);
    }

    public static int getInt(String key, int defaultValue) {
        return sInterface.getInt(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        return sInterface.getLong(key, defaultValue);
    }

    public static double getDouble(String key, double defaultValue) {
        return sInterface.getDouble(key, defaultValue);
    }

    public static String getString(String key, String defaultValue) {
        return sInterface.getString(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sInterface.getBoolean(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue, boolean useProperty) {
        return useProperty ? SystemProperties.getInt(key, defaultValue) : getInt(key, defaultValue);
    }

    public static boolean hasFeature(String featureName) {
        Boolean support = sFeatureSupport.get(featureName);
        if (support == null) {
            support = Boolean.valueOf("1".equals(SystemProperties.get("persist.sys.xiaopeng." + featureName, "0")));
            sFeatureSupport.put(featureName, support);
            StringBuilder sb = new StringBuilder();
            sb.append("The car ");
            sb.append(support.booleanValue() ? "support feature: " : " not support feature: ");
            sb.append(featureName);
            Log.d(TAG, sb.toString());
        }
        return support.booleanValue();
    }

    public static String getFeatureType(String featureName) {
        String type = sFeatureType.get(featureName);
        if (type == null) {
            String type2 = SystemProperties.get("persist.sys.xiaopeng." + featureName, "0");
            sFeatureType.put(featureName, type2);
            Log.d(TAG, featureName + " feature type is " + type2);
            return type2;
        }
        return type;
    }
}
