package android.view;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.provider.SettingsStringUtil;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.transition.EpicenterTranslateClipReveal;
import com.xiaopeng.app.xpDialogInfo;
import com.xiaopeng.view.ISharedDisplayListener;
import com.xiaopeng.view.SharedDisplayManager;
import com.xiaopeng.view.xpWindowManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Objects;

/* loaded from: classes3.dex */
public interface WindowManager extends ViewManager {
    public static final int DOCKED_BOTTOM = 4;
    public static final int DOCKED_INVALID = -1;
    public static final int DOCKED_LEFT = 1;
    public static final int DOCKED_RIGHT = 3;
    public static final int DOCKED_TOP = 2;
    public static final int EVENT_DIALOG_DISMISS = 102;
    public static final int EVENT_IN_TRANSACT = 1001;
    public static final int EVENT_OUT_TRANSACT = 1002;
    public static final int EVENT_POLICY_CHANGED = 120;
    public static final int EVENT_SHARED_CHANGED = 4;
    public static final int EVENT_SHARED_DESKTOP = 3;
    public static final int EVENT_SHARED_GESTURE = 5;
    public static final int EVENT_SHARED_METHODS = 8;
    public static final int EVENT_SHARED_SLIDING = 0;
    public static final int EVENT_SHARED_STARTED = 1;
    public static final int EVENT_SHARED_STOPPED = 2;
    public static final int EVENT_SHARED_TOUCHED = 7;
    public static final int EVENT_SHARED_VIRTUAL = 6;
    public static final int EVENT_WAKEUP_ACQUIRE = 200;
    public static final int EVENT_WAKEUP_RELEASE = 201;
    public static final int EVENT_WINDOW_CHANGED = 100;
    public static final int EVENT_WINDOW_DISMISS = 101;
    public static final int ID_SCREEN_FOURTH = 3;
    public static final int ID_SCREEN_PRIMARY = 0;
    public static final int ID_SCREEN_SECONDARY = 1;
    public static final int ID_SCREEN_THIRD = 2;
    public static final int ID_SHARED_PRIMARY = 0;
    public static final int ID_SHARED_SECONDARY = 1;
    public static final int ID_SHARED_UNKNOWN = -1;
    public static final String INPUT_CONSUMER_NAVIGATION = "nav_input_consumer";
    public static final String INPUT_CONSUMER_PIP = "pip_input_consumer";
    public static final String INPUT_CONSUMER_RECENTS_ANIMATION = "recents_animation_input_consumer";
    public static final String INPUT_CONSUMER_WALLPAPER = "wallpaper_input_consumer";
    public static final String PARCEL_KEY_SHORTCUTS_ARRAY = "shortcuts_array";
    public static final int POLICY_BLOCKED = 2;
    public static final int POLICY_DISABLE = 0;
    public static final int POLICY_ENABLED = 1;
    public static final int POLICY_FAIL = 1;
    public static final int POLICY_PASS = 0;
    public static final int POLICY_UNKNOWN = -1;
    public static final int REMOVE_CONTENT_MODE_DESTROY = 2;
    public static final int REMOVE_CONTENT_MODE_MOVE_TO_PRIMARY = 1;
    public static final int REMOVE_CONTENT_MODE_UNDEFINED = 0;
    public static final int TAKE_SCREENSHOT_FULLSCREEN = 1;
    public static final int TAKE_SCREENSHOT_SELECTED_REGION = 2;
    public static final int TRANSIT_ACTIVITY_CLOSE = 7;
    public static final int TRANSIT_ACTIVITY_OPEN = 6;
    public static final int TRANSIT_ACTIVITY_RELAUNCH = 18;
    public static final int TRANSIT_CRASHING_ACTIVITY_CLOSE = 26;
    public static final int TRANSIT_DOCK_TASK_FROM_RECENTS = 19;
    public static final int TRANSIT_FLAG_KEYGUARD_GOING_AWAY_NO_ANIMATION = 2;
    public static final int TRANSIT_FLAG_KEYGUARD_GOING_AWAY_SUBTLE_ANIMATION = 8;
    public static final int TRANSIT_FLAG_KEYGUARD_GOING_AWAY_TO_SHADE = 1;
    public static final int TRANSIT_FLAG_KEYGUARD_GOING_AWAY_WITH_WALLPAPER = 4;
    public static final int TRANSIT_KEYGUARD_GOING_AWAY = 20;
    public static final int TRANSIT_KEYGUARD_GOING_AWAY_ON_WALLPAPER = 21;
    public static final int TRANSIT_KEYGUARD_OCCLUDE = 22;
    public static final int TRANSIT_KEYGUARD_UNOCCLUDE = 23;
    public static final int TRANSIT_NONE = 0;
    public static final int TRANSIT_SHOW_SINGLE_TASK_DISPLAY = 28;
    public static final int TRANSIT_TASK_CHANGE_WINDOWING_MODE = 27;
    public static final int TRANSIT_TASK_CLOSE = 9;
    public static final int TRANSIT_TASK_IN_PLACE = 17;
    public static final int TRANSIT_TASK_OPEN = 8;
    public static final int TRANSIT_TASK_OPEN_BEHIND = 16;
    public static final int TRANSIT_TASK_TO_BACK = 11;
    public static final int TRANSIT_TASK_TO_FRONT = 10;
    public static final int TRANSIT_TRANSLUCENT_ACTIVITY_CLOSE = 25;
    public static final int TRANSIT_TRANSLUCENT_ACTIVITY_OPEN = 24;
    public static final int TRANSIT_UNSET = -1;
    public static final int TRANSIT_WALLPAPER_CLOSE = 12;
    public static final int TRANSIT_WALLPAPER_INTRA_CLOSE = 15;
    public static final int TRANSIT_WALLPAPER_INTRA_OPEN = 14;
    public static final int TRANSIT_WALLPAPER_OPEN = 13;
    public static final int TYPE_DEVICE_ID = -1;
    public static final int TYPE_SCREEN_ID = 0;
    public static final int TYPE_SHARED_ID = 1;

    /* loaded from: classes3.dex */
    public interface KeyboardShortcutsReceiver {
        void onKeyboardShortcutsReceived(List<KeyboardShortcutGroup> list);
    }

    /* loaded from: classes3.dex */
    public @interface RemoveContentMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface TransitionFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface TransitionType {
    }

    @SystemApi
    Region getCurrentImeTouchRegion();

    Display getDefaultDisplay();

    void removeViewImmediate(View view);

    void requestAppKeyboardShortcuts(KeyboardShortcutsReceiver keyboardShortcutsReceiver, int i);

    /* loaded from: classes3.dex */
    public static class BadTokenException extends RuntimeException {
        public BadTokenException() {
        }

        public BadTokenException(String name) {
            super(name);
        }
    }

    /* loaded from: classes3.dex */
    public static class InvalidDisplayException extends RuntimeException {
        public InvalidDisplayException() {
        }

        public InvalidDisplayException(String name) {
            super(name);
        }
    }

    default void setShouldShowWithInsecureKeyguard(int displayId, boolean shouldShow) {
    }

    default void setShouldShowSystemDecors(int displayId, boolean shouldShow) {
    }

    default boolean shouldShowSystemDecors(int displayId) {
        return false;
    }

    default void setShouldShowIme(int displayId, boolean shouldShow) {
    }

    default boolean shouldShowIme(int displayId) {
        return false;
    }

    static boolean isPrimaryId(int sharedId) {
        return SharedDisplayManager.isPrimaryId(sharedId);
    }

    static int findScreenId(int sharedId) {
        return SharedDisplayManager.findScreenId(sharedId);
    }

    static int findScreenId(int index, MotionEvent event) {
        return SharedDisplayManager.findScreenId(index, event);
    }

    default int getScreenId(String packageName) {
        return -1;
    }

    default void setScreenId(String packageName, int screenId) {
    }

    default int getSharedId(String packageName) {
        return -1;
    }

    default void setSharedId(String packageName, int sharedId) {
    }

    default List<String> getSharedPackages() {
        return null;
    }

    default List<String> getFilterPackages(int sharedId) {
        return null;
    }

    default void setSharedEvent(int event) {
    }

    default void setSharedEvent(int event, int sharedId) {
    }

    default void setSharedEvent(int event, int sharedId, String extras) {
    }

    default Rect getActivityBounds(String packageName, boolean fullscreen) {
        return null;
    }

    default String getTopActivity(int type, int id) {
        return "";
    }

    default String getTopWindow() {
        return "";
    }

    default void setModeEvent(int sharedId, int mode, String extra) {
    }

    default void setPackageSettings(String packageName, Bundle extras) {
    }

    default boolean isSharedScreenEnabled(int screenId) {
        return false;
    }

    default boolean isSharedPackageEnabled(String packageName) {
        return false;
    }

    default void setSharedScreenPolicy(int screenId, int policy) {
    }

    default void setSharedPackagePolicy(String packageName, int policy) {
    }

    default xpDialogInfo getTopDialog(Bundle extras) {
        return null;
    }

    default boolean dismissDialog(Bundle extras) {
        return false;
    }

    default int getAppPolicy(Bundle extras) {
        return 0;
    }

    default void registerSharedListener(ISharedDisplayListener listener) {
    }

    default void unregisterSharedListener(ISharedDisplayListener listener) {
    }

    /* loaded from: classes3.dex */
    public static class LayoutParams extends ViewGroup.LayoutParams implements Parcelable {
        public static final int ACCESSIBILITY_ANCHOR_CHANGED = 16777216;
        public static final int ACCESSIBILITY_TITLE_CHANGED = 33554432;
        public static final int ALPHA_CHANGED = 128;
        public static final int ANIMATION_CHANGED = 16;
        public static final float BRIGHTNESS_OVERRIDE_FULL = 1.0f;
        public static final float BRIGHTNESS_OVERRIDE_NONE = -1.0f;
        public static final float BRIGHTNESS_OVERRIDE_OFF = 0.0f;
        public static final int BUTTON_BRIGHTNESS_CHANGED = 8192;
        public static final int COLOR_MODE_CHANGED = 67108864;
        public static final Parcelable.Creator<LayoutParams> CREATOR = new Parcelable.Creator<LayoutParams>() { // from class: android.view.WindowManager.LayoutParams.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public LayoutParams createFromParcel(Parcel in) {
                return new LayoutParams(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public LayoutParams[] newArray(int size) {
                return new LayoutParams[size];
            }
        };
        public static final int DIM_AMOUNT_CHANGED = 32;
        public static final int EVERYTHING_CHANGED = -1;
        public static final int FIRST_APPLICATION_WINDOW = 1;
        public static final int FIRST_SUB_WINDOW = 1000;
        public static final int FIRST_SYSTEM_WINDOW = 2000;
        public static final int FLAGS_CHANGED = 4;
        public static final int FLAG_ALLOW_LOCK_WHILE_SCREEN_ON = 1;
        public static final int FLAG_ALT_FOCUSABLE_IM = 131072;
        @Deprecated
        public static final int FLAG_BLUR_BEHIND = 4;
        public static final int FLAG_DIM_BEHIND = 2;
        @Deprecated
        public static final int FLAG_DISMISS_KEYGUARD = 4194304;
        @Deprecated
        public static final int FLAG_DITHER = 4096;
        public static final int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = Integer.MIN_VALUE;
        public static final int FLAG_FORCE_NOT_FULLSCREEN = 2048;
        public static final int FLAG_FULLSCREEN = 1024;
        public static final int FLAG_HARDWARE_ACCELERATED = 16777216;
        public static final int FLAG_IGNORE_CHEEK_PRESSES = 32768;
        public static final int FLAG_KEEP_SCREEN_ON = 128;
        public static final int FLAG_LAYOUT_ATTACHED_IN_DECOR = 1073741824;
        public static final int FLAG_LAYOUT_INSET_DECOR = 65536;
        public static final int FLAG_LAYOUT_IN_OVERSCAN = 33554432;
        public static final int FLAG_LAYOUT_IN_SCREEN = 256;
        public static final int FLAG_LAYOUT_NO_LIMITS = 512;
        public static final int FLAG_LOCAL_FOCUS_MODE = 268435456;
        public static final int FLAG_NOT_FOCUSABLE = 8;
        public static final int FLAG_NOT_TOUCHABLE = 16;
        public static final int FLAG_NOT_TOUCH_MODAL = 32;
        public static final int FLAG_SCALED = 16384;
        public static final int FLAG_SECURE = 8192;
        public static final int FLAG_SHOW_WALLPAPER = 1048576;
        @Deprecated
        public static final int FLAG_SHOW_WHEN_LOCKED = 524288;
        @UnsupportedAppUsage
        public static final int FLAG_SLIPPERY = 536870912;
        public static final int FLAG_SPLIT_TOUCH = 8388608;
        @Deprecated
        public static final int FLAG_TOUCHABLE_WHEN_WAKING = 64;
        public static final int FLAG_TRANSLUCENT_NAVIGATION = 134217728;
        public static final int FLAG_TRANSLUCENT_STATUS = 67108864;
        @Deprecated
        public static final int FLAG_TURN_SCREEN_ON = 2097152;
        public static final int FLAG_WATCH_OUTSIDE_TOUCH = 262144;
        public static final int FORMAT_CHANGED = 8;
        public static final int INPUT_FEATURES_CHANGED = 65536;
        public static final int INPUT_FEATURE_DISABLE_POINTER_GESTURES = 1;
        @UnsupportedAppUsage
        public static final int INPUT_FEATURE_DISABLE_USER_ACTIVITY = 4;
        public static final int INPUT_FEATURE_NO_INPUT_CHANNEL = 2;
        public static final int INTENT_FLAG_PHYSICAL_FULLSCREEN = 32;
        public static final int INVALID_WINDOW_TYPE = -1;
        public static final int LAST_APPLICATION_WINDOW = 99;
        public static final int LAST_SUB_WINDOW = 1999;
        public static final int LAST_SYSTEM_WINDOW = 2999;
        public static final int LAYOUT_CHANGED = 1;
        @Deprecated
        public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS = 1;
        public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT = 0;
        public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER = 2;
        public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES = 1;
        public static final int MEMORY_TYPE_CHANGED = 256;
        @Deprecated
        public static final int MEMORY_TYPE_GPU = 2;
        @Deprecated
        public static final int MEMORY_TYPE_HARDWARE = 1;
        @Deprecated
        public static final int MEMORY_TYPE_NORMAL = 0;
        @Deprecated
        public static final int MEMORY_TYPE_PUSH_BUFFERS = 3;
        public static final int NEEDS_MENU_KEY_CHANGED = 4194304;
        @UnsupportedAppUsage
        public static final int NEEDS_MENU_SET_FALSE = 2;
        @UnsupportedAppUsage
        public static final int NEEDS_MENU_SET_TRUE = 1;
        public static final int NEEDS_MENU_UNSET = 0;
        public static final int PREFERRED_DISPLAY_MODE_ID = 8388608;
        public static final int PREFERRED_REFRESH_RATE_CHANGED = 2097152;
        public static final int PRIVATE_FLAGS_CHANGED = 131072;
        public static final int PRIVATE_FLAG_COLOR_SPACE_AGNOSTIC = 16777216;
        public static final int PRIVATE_FLAG_COMPATIBLE_WINDOW = 128;
        public static final int PRIVATE_FLAG_DISABLE_WALLPAPER_TOUCH_EVENTS = 2048;
        public static final int PRIVATE_FLAG_FAKE_HARDWARE_ACCELERATED = 1;
        public static final int PRIVATE_FLAG_FORCE_DECOR_VIEW_VISIBILITY = 16384;
        public static final int PRIVATE_FLAG_FORCE_DRAW_BAR_BACKGROUNDS = 131072;
        public static final int PRIVATE_FLAG_FORCE_HARDWARE_ACCELERATED = 2;
        public static final int PRIVATE_FLAG_FORCE_STATUS_BAR_VISIBLE_TRANSPARENT = 4096;
        public static final int PRIVATE_FLAG_INHERIT_TRANSLUCENT_DECOR = 512;
        public static final int PRIVATE_FLAG_IS_ROUNDED_CORNERS_OVERLAY = 1048576;
        public static final int PRIVATE_FLAG_IS_SCREEN_DECOR = 4194304;
        public static final int PRIVATE_FLAG_KEYGUARD = 1024;
        public static final int PRIVATE_FLAG_LAYOUT_CHILD_WINDOW_IN_PARENT_FRAME = 65536;
        public static final int PRIVATE_FLAG_NO_MOVE_ANIMATION = 64;
        public static final int PRIVATE_FLAG_PRESERVE_GEOMETRY = 8192;
        @UnsupportedAppUsage
        public static final int PRIVATE_FLAG_SHOW_FOR_ALL_USERS = 16;
        public static final int PRIVATE_FLAG_STATUS_FORCE_SHOW_NAVIGATION = 8388608;
        public static final int PRIVATE_FLAG_SUSTAINED_PERFORMANCE_MODE = 262144;
        public static final int PRIVATE_FLAG_SYSTEM_ERROR = 256;
        public static final int PRIVATE_FLAG_WANTS_OFFSET_NOTIFICATIONS = 4;
        public static final int PRIVATE_FLAG_WILL_NOT_REPLACE_ON_RELAUNCH = 32768;
        public static final int ROTATION_ANIMATION_CHANGED = 4096;
        public static final int ROTATION_ANIMATION_CROSSFADE = 1;
        public static final int ROTATION_ANIMATION_JUMPCUT = 2;
        public static final int ROTATION_ANIMATION_ROTATE = 0;
        public static final int ROTATION_ANIMATION_SEAMLESS = 3;
        public static final int ROTATION_ANIMATION_UNSPECIFIED = -1;
        public static final int SCREEN_BRIGHTNESS_CHANGED = 2048;
        public static final int SCREEN_ORIENTATION_CHANGED = 1024;
        public static final int SOFT_INPUT_ADJUST_NOTHING = 48;
        public static final int SOFT_INPUT_ADJUST_PAN = 32;
        public static final int SOFT_INPUT_ADJUST_RESIZE = 16;
        public static final int SOFT_INPUT_ADJUST_UNSPECIFIED = 0;
        public static final int SOFT_INPUT_IS_FORWARD_NAVIGATION = 256;
        public static final int SOFT_INPUT_MASK_ADJUST = 240;
        public static final int SOFT_INPUT_MASK_STATE = 15;
        public static final int SOFT_INPUT_MODE_CHANGED = 512;
        public static final int SOFT_INPUT_STATE_ALWAYS_HIDDEN = 3;
        public static final int SOFT_INPUT_STATE_ALWAYS_VISIBLE = 5;
        public static final int SOFT_INPUT_STATE_HIDDEN = 2;
        public static final int SOFT_INPUT_STATE_UNCHANGED = 1;
        public static final int SOFT_INPUT_STATE_UNSPECIFIED = 0;
        public static final int SOFT_INPUT_STATE_VISIBLE = 4;
        public static final int SURFACE_INSETS_CHANGED = 1048576;
        @SystemApi
        public static final int SYSTEM_FLAG_HIDE_NON_SYSTEM_OVERLAY_WINDOWS = 524288;
        public static final int SYSTEM_UI_LISTENER_CHANGED = 32768;
        public static final int SYSTEM_UI_VISIBILITY_CHANGED = 16384;
        public static final int TITLE_CHANGED = 64;
        public static final int TRANSLUCENT_FLAGS_CHANGED = 524288;
        public static final int TYPE_ACCESSIBILITY_OVERLAY = 2032;
        public static final int TYPE_AI_ASSISTANT = 2040;
        public static final int TYPE_APPLICATION = 2;
        public static final int TYPE_APPLICATION_ABOVE_SUB_PANEL = 1005;
        public static final int TYPE_APPLICATION_ATTACHED_DIALOG = 1003;
        public static final int TYPE_APPLICATION_MEDIA = 1001;
        @UnsupportedAppUsage
        public static final int TYPE_APPLICATION_MEDIA_OVERLAY = 1004;
        public static final int TYPE_APPLICATION_OVERLAY = 2038;
        public static final int TYPE_APPLICATION_PANEL = 1000;
        public static final int TYPE_APPLICATION_STARTING = 3;
        public static final int TYPE_APPLICATION_SUB_PANEL = 1002;
        public static final int TYPE_APPLICATION_WINDOW_OVERLAY = 2044;
        public static final int TYPE_AVATAR = 2051;
        public static final int TYPE_BASE_APPLICATION = 1;
        public static final int TYPE_BOOT_PROGRESS = 2021;
        public static final int TYPE_CHANGED = 2;
        @UnsupportedAppUsage
        public static final int TYPE_DISPLAY_OVERLAY = 2026;
        public static final int TYPE_DOCK_DIVIDER = 2034;
        public static final int TYPE_DRAG = 2016;
        public static final int TYPE_DRAWN_APPLICATION = 4;
        public static final int TYPE_DREAM = 2023;
        public static final int TYPE_HOME = 5;
        public static final int TYPE_INFOFLOW = 2039;
        public static final int TYPE_INFORMATION_BAR = 2046;
        public static final int TYPE_INPUT_CONSUMER = 2022;
        public static final int TYPE_INPUT_METHOD = 2011;
        public static final int TYPE_INPUT_METHOD_DIALOG = 2012;
        public static final int TYPE_KEYGUARD = 2004;
        public static final int TYPE_KEYGUARD_DIALOG = 2009;
        public static final int TYPE_LIFECYCLE_DIALOG = 2048;
        public static final int TYPE_MAGNIFICATION_OVERLAY = 2027;
        public static final int TYPE_MINI_PROGRAM = 12;
        public static final int TYPE_NAVIGATION_BAR = 2019;
        public static final int TYPE_NAVIGATION_BAR_PANEL = 2024;
        public static final int TYPE_OSD = 2043;
        @Deprecated
        public static final int TYPE_PHONE = 2002;
        public static final int TYPE_POINTER = 2018;
        public static final int TYPE_PRESENTATION = 2037;
        @Deprecated
        public static final int TYPE_PRIORITY_PHONE = 2007;
        public static final int TYPE_PRIVATE_PRESENTATION = 2030;
        public static final int TYPE_QS_DIALOG = 2035;
        public static final int TYPE_QUICK_PANEL = 2053;
        public static final int TYPE_SCREENSHOT = 2036;
        public static final int TYPE_SEARCH_BAR = 2001;
        public static final int TYPE_SECONDARY_NAVIGATION = 2060;
        public static final int TYPE_SECONDARY_SCREENSHOT = 2061;
        @UnsupportedAppUsage
        public static final int TYPE_SECURE_SYSTEM_OVERLAY = 2015;
        public static final int TYPE_SHARED_APPLICATION = 10;
        public static final int TYPE_SPECTRUM = 2041;
        public static final int TYPE_STATUS_BAR = 2000;
        public static final int TYPE_STATUS_BAR_PANEL = 2014;
        public static final int TYPE_STATUS_BAR_SUB_PANEL = 2017;
        @Deprecated
        public static final int TYPE_SYSTEM_ALERT = 2003;
        public static final int TYPE_SYSTEM_DIALOG = 2008;
        @Deprecated
        public static final int TYPE_SYSTEM_ERROR = 2010;
        @Deprecated
        public static final int TYPE_SYSTEM_OVERLAY = 2006;
        @Deprecated
        public static final int TYPE_TOAST = 2005;
        public static final int TYPE_VOICE_INTERACTION = 2031;
        public static final int TYPE_VOICE_INTERACTION_STARTING = 2033;
        public static final int TYPE_VOLUME_OVERLAY = 2020;
        public static final int TYPE_VUI = 2049;
        public static final int TYPE_VUI_OVERLAY = 2052;
        public static final int TYPE_WALLPAPER = 2013;
        public static final int TYPE_WARNING_DIALOG = 2047;
        public static final int TYPE_WATER_MARK = 2045;
        public static final int TYPE_WINDOW_OVERLAY = 2042;
        public static final int TYPE_XUI_DIALOG = 9;
        public static final int TYPE_XUI_FULLSCREEN = 6;
        public static final int TYPE_XUI_OVERLAY = 7;
        public static final int TYPE_XUI_SECONDARY_HOME = 8;
        public static final int TYPE_XUI_WALLPAPER = 2050;
        public static final int USER_ACTIVITY_TIMEOUT_CHANGED = 262144;
        public static final int XFLAG_SHARED_PRIMARY = 16;
        public static final int XFLAG_SHARED_SECONDARY = 32;
        public static final int XFLAG_WINDOW_AUTOSCREEN = 8;
        public static final int XFLAG_WINDOW_DIALOG = 1;
        public static final int XFLAG_WINDOW_FLOATING = 64;
        public static final int XFLAG_WINDOW_FULLSCREEN = 4;
        public static final int XFLAG_WINDOW_TRANSLUCENT = 128;
        public long accessibilityIdOfAnchor;
        public CharSequence accessibilityTitle;
        public float alpha;
        public float buttonBrightness;
        public float dimAmount;
        public int displayId;
        @ViewDebug.ExportedProperty(flagMapping = {@ViewDebug.FlagToString(equals = 1, mask = 1, name = "ALLOW_LOCK_WHILE_SCREEN_ON"), @ViewDebug.FlagToString(equals = 2, mask = 2, name = "DIM_BEHIND"), @ViewDebug.FlagToString(equals = 4, mask = 4, name = "BLUR_BEHIND"), @ViewDebug.FlagToString(equals = 8, mask = 8, name = "NOT_FOCUSABLE"), @ViewDebug.FlagToString(equals = 16, mask = 16, name = "NOT_TOUCHABLE"), @ViewDebug.FlagToString(equals = 32, mask = 32, name = "NOT_TOUCH_MODAL"), @ViewDebug.FlagToString(equals = 64, mask = 64, name = "TOUCHABLE_WHEN_WAKING"), @ViewDebug.FlagToString(equals = 128, mask = 128, name = "KEEP_SCREEN_ON"), @ViewDebug.FlagToString(equals = 256, mask = 256, name = "LAYOUT_IN_SCREEN"), @ViewDebug.FlagToString(equals = 512, mask = 512, name = "LAYOUT_NO_LIMITS"), @ViewDebug.FlagToString(equals = 1024, mask = 1024, name = "FULLSCREEN"), @ViewDebug.FlagToString(equals = 2048, mask = 2048, name = "FORCE_NOT_FULLSCREEN"), @ViewDebug.FlagToString(equals = 4096, mask = 4096, name = "DITHER"), @ViewDebug.FlagToString(equals = 8192, mask = 8192, name = "SECURE"), @ViewDebug.FlagToString(equals = 16384, mask = 16384, name = "SCALED"), @ViewDebug.FlagToString(equals = 32768, mask = 32768, name = "IGNORE_CHEEK_PRESSES"), @ViewDebug.FlagToString(equals = 65536, mask = 65536, name = "LAYOUT_INSET_DECOR"), @ViewDebug.FlagToString(equals = 131072, mask = 131072, name = "ALT_FOCUSABLE_IM"), @ViewDebug.FlagToString(equals = 262144, mask = 262144, name = "WATCH_OUTSIDE_TOUCH"), @ViewDebug.FlagToString(equals = 524288, mask = 524288, name = "SHOW_WHEN_LOCKED"), @ViewDebug.FlagToString(equals = 1048576, mask = 1048576, name = "SHOW_WALLPAPER"), @ViewDebug.FlagToString(equals = 2097152, mask = 2097152, name = "TURN_SCREEN_ON"), @ViewDebug.FlagToString(equals = 4194304, mask = 4194304, name = "DISMISS_KEYGUARD"), @ViewDebug.FlagToString(equals = 8388608, mask = 8388608, name = "SPLIT_TOUCH"), @ViewDebug.FlagToString(equals = 16777216, mask = 16777216, name = "HARDWARE_ACCELERATED"), @ViewDebug.FlagToString(equals = 33554432, mask = 33554432, name = "LOCAL_FOCUS_MODE"), @ViewDebug.FlagToString(equals = 67108864, mask = 67108864, name = "TRANSLUCENT_STATUS"), @ViewDebug.FlagToString(equals = 134217728, mask = 134217728, name = "TRANSLUCENT_NAVIGATION"), @ViewDebug.FlagToString(equals = 268435456, mask = 268435456, name = "LOCAL_FOCUS_MODE"), @ViewDebug.FlagToString(equals = 536870912, mask = 536870912, name = "FLAG_SLIPPERY"), @ViewDebug.FlagToString(equals = 1073741824, mask = 1073741824, name = "FLAG_LAYOUT_ATTACHED_IN_DECOR"), @ViewDebug.FlagToString(equals = Integer.MIN_VALUE, mask = Integer.MIN_VALUE, name = "DRAWS_SYSTEM_BAR_BACKGROUNDS")}, formatToHexString = true)
        public int flags;
        public int format;
        public int gravity;
        public boolean hasManualSurfaceInsets;
        @UnsupportedAppUsage
        public boolean hasSystemUiListeners;
        @UnsupportedAppUsage
        public long hideTimeoutMilliseconds;
        public float horizontalMargin;
        @ViewDebug.ExportedProperty
        public float horizontalWeight;
        @UnsupportedAppUsage
        public int inputFeatures;
        public int intentFlags;
        public int layoutInDisplayCutoutMode;
        private int mColorMode;
        private int[] mCompatibilityParamsBackup;
        private CharSequence mTitle;
        @Deprecated
        public int memoryType;
        @UnsupportedAppUsage
        public int needsMenuKey;
        public String packageName;
        public int parentType;
        public int preferredDisplayModeId;
        @Deprecated
        public float preferredRefreshRate;
        public boolean preservePreviousSurfaceInsets;
        @ViewDebug.ExportedProperty(flagMapping = {@ViewDebug.FlagToString(equals = 1, mask = 1, name = "FAKE_HARDWARE_ACCELERATED"), @ViewDebug.FlagToString(equals = 2, mask = 2, name = "FORCE_HARDWARE_ACCELERATED"), @ViewDebug.FlagToString(equals = 4, mask = 4, name = "WANTS_OFFSET_NOTIFICATIONS"), @ViewDebug.FlagToString(equals = 16, mask = 16, name = "SHOW_FOR_ALL_USERS"), @ViewDebug.FlagToString(equals = 64, mask = 64, name = "NO_MOVE_ANIMATION"), @ViewDebug.FlagToString(equals = 128, mask = 128, name = "COMPATIBLE_WINDOW"), @ViewDebug.FlagToString(equals = 256, mask = 256, name = "SYSTEM_ERROR"), @ViewDebug.FlagToString(equals = 512, mask = 512, name = "INHERIT_TRANSLUCENT_DECOR"), @ViewDebug.FlagToString(equals = 1024, mask = 1024, name = "KEYGUARD"), @ViewDebug.FlagToString(equals = 2048, mask = 2048, name = "DISABLE_WALLPAPER_TOUCH_EVENTS"), @ViewDebug.FlagToString(equals = 4096, mask = 4096, name = "FORCE_STATUS_BAR_VISIBLE_TRANSPARENT"), @ViewDebug.FlagToString(equals = 8192, mask = 8192, name = "PRESERVE_GEOMETRY"), @ViewDebug.FlagToString(equals = 16384, mask = 16384, name = "FORCE_DECOR_VIEW_VISIBILITY"), @ViewDebug.FlagToString(equals = 32768, mask = 32768, name = "WILL_NOT_REPLACE_ON_RELAUNCH"), @ViewDebug.FlagToString(equals = 65536, mask = 65536, name = "LAYOUT_CHILD_WINDOW_IN_PARENT_FRAME"), @ViewDebug.FlagToString(equals = 131072, mask = 131072, name = "FORCE_DRAW_STATUS_BAR_BACKGROUND"), @ViewDebug.FlagToString(equals = 262144, mask = 262144, name = "SUSTAINED_PERFORMANCE_MODE"), @ViewDebug.FlagToString(equals = 524288, mask = 524288, name = "HIDE_NON_SYSTEM_OVERLAY_WINDOWS"), @ViewDebug.FlagToString(equals = 1048576, mask = 1048576, name = "IS_ROUNDED_CORNERS_OVERLAY"), @ViewDebug.FlagToString(equals = 4194304, mask = 4194304, name = "IS_SCREEN_DECOR"), @ViewDebug.FlagToString(equals = 8388608, mask = 8388608, name = "STATUS_FORCE_SHOW_NAVIGATION"), @ViewDebug.FlagToString(equals = 16777216, mask = 16777216, name = "COLOR_SPACE_AGNOSTIC")})
        public int privateFlags;
        public int rotationAnimation;
        public float screenBrightness;
        public int screenOrientation;
        public int sharedId;
        public int softInputMode;
        @UnsupportedAppUsage
        public int subtreeSystemUiVisibility;
        public final Rect surfaceInsets;
        public int systemUiVisibility;
        public IBinder token;
        @ViewDebug.ExportedProperty(mapping = {@ViewDebug.IntToString(from = 1, to = "BASE_APPLICATION"), @ViewDebug.IntToString(from = 2, to = "APPLICATION"), @ViewDebug.IntToString(from = 3, to = "APPLICATION_STARTING"), @ViewDebug.IntToString(from = 4, to = "DRAWN_APPLICATION"), @ViewDebug.IntToString(from = 1000, to = "APPLICATION_PANEL"), @ViewDebug.IntToString(from = 1001, to = "APPLICATION_MEDIA"), @ViewDebug.IntToString(from = 1002, to = "APPLICATION_SUB_PANEL"), @ViewDebug.IntToString(from = 1005, to = "APPLICATION_ABOVE_SUB_PANEL"), @ViewDebug.IntToString(from = 1003, to = "APPLICATION_ATTACHED_DIALOG"), @ViewDebug.IntToString(from = 1004, to = "APPLICATION_MEDIA_OVERLAY"), @ViewDebug.IntToString(from = 2000, to = "STATUS_BAR"), @ViewDebug.IntToString(from = 2001, to = "SEARCH_BAR"), @ViewDebug.IntToString(from = 2002, to = "PHONE"), @ViewDebug.IntToString(from = 2003, to = "SYSTEM_ALERT"), @ViewDebug.IntToString(from = 2005, to = "TOAST"), @ViewDebug.IntToString(from = 2006, to = "SYSTEM_OVERLAY"), @ViewDebug.IntToString(from = 2007, to = "PRIORITY_PHONE"), @ViewDebug.IntToString(from = 2008, to = "SYSTEM_DIALOG"), @ViewDebug.IntToString(from = 2009, to = "KEYGUARD_DIALOG"), @ViewDebug.IntToString(from = 2010, to = "SYSTEM_ERROR"), @ViewDebug.IntToString(from = 2011, to = "INPUT_METHOD"), @ViewDebug.IntToString(from = 2012, to = "INPUT_METHOD_DIALOG"), @ViewDebug.IntToString(from = 2013, to = "WALLPAPER"), @ViewDebug.IntToString(from = 2014, to = "STATUS_BAR_PANEL"), @ViewDebug.IntToString(from = 2015, to = "SECURE_SYSTEM_OVERLAY"), @ViewDebug.IntToString(from = 2016, to = "DRAG"), @ViewDebug.IntToString(from = 2017, to = "STATUS_BAR_SUB_PANEL"), @ViewDebug.IntToString(from = 2018, to = "POINTER"), @ViewDebug.IntToString(from = 2019, to = "NAVIGATION_BAR"), @ViewDebug.IntToString(from = 2020, to = "VOLUME_OVERLAY"), @ViewDebug.IntToString(from = 2021, to = "BOOT_PROGRESS"), @ViewDebug.IntToString(from = 2022, to = "INPUT_CONSUMER"), @ViewDebug.IntToString(from = 2023, to = "DREAM"), @ViewDebug.IntToString(from = 2024, to = "NAVIGATION_BAR_PANEL"), @ViewDebug.IntToString(from = 2026, to = "DISPLAY_OVERLAY"), @ViewDebug.IntToString(from = 2027, to = "MAGNIFICATION_OVERLAY"), @ViewDebug.IntToString(from = 2037, to = "PRESENTATION"), @ViewDebug.IntToString(from = 2030, to = "PRIVATE_PRESENTATION"), @ViewDebug.IntToString(from = 2031, to = "VOICE_INTERACTION"), @ViewDebug.IntToString(from = 2033, to = "VOICE_INTERACTION_STARTING"), @ViewDebug.IntToString(from = 2034, to = "DOCK_DIVIDER"), @ViewDebug.IntToString(from = 2035, to = "QS_DIALOG"), @ViewDebug.IntToString(from = 2036, to = "SCREENSHOT"), @ViewDebug.IntToString(from = 2038, to = "APPLICATION_OVERLAY"), @ViewDebug.IntToString(from = 2039, to = "TYPE_INFOFLOW"), @ViewDebug.IntToString(from = 2040, to = "TYPE_AI_ASSISTANT"), @ViewDebug.IntToString(from = 2043, to = "TYPE_OSD"), @ViewDebug.IntToString(from = 6, to = "TYPE_XUI_FULLSCREEN")})
        public int type;
        @UnsupportedAppUsage
        public long userActivityTimeout;
        public float verticalMargin;
        @ViewDebug.ExportedProperty
        public float verticalWeight;
        public String winToken;
        public int windowAnimations;
        @ViewDebug.ExportedProperty
        public int x;
        public int xpFlags;
        @ViewDebug.ExportedProperty
        public int y;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes3.dex */
        @interface LayoutInDisplayCutoutMode {
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes3.dex */
        public @interface SoftInputModeFlags {
        }

        @SystemApi
        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes3.dex */
        public @interface SystemFlags {
        }

        public static boolean isSystemAlertWindowType(int type) {
            if (type == 2002 || type == 2003 || type == 2006 || type == 2007 || type == 2010 || type == 2038) {
                return true;
            }
            return false;
        }

        public static boolean mayUseInputMethod(int flags) {
            int i = flags & 131080;
            if (i == 0 || i == 131080) {
                return true;
            }
            return false;
        }

        public LayoutParams() {
            super(-1, -1);
            this.x = -1;
            this.y = -1;
            this.needsMenuKey = 0;
            this.surfaceInsets = new Rect();
            this.preservePreviousSurfaceInsets = true;
            this.alpha = 1.0f;
            this.dimAmount = 1.0f;
            this.screenBrightness = -1.0f;
            this.buttonBrightness = -1.0f;
            this.rotationAnimation = 0;
            this.token = null;
            this.packageName = null;
            this.screenOrientation = -1;
            this.layoutInDisplayCutoutMode = 0;
            this.userActivityTimeout = -1L;
            this.accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
            this.hideTimeoutMilliseconds = -1L;
            this.mColorMode = 0;
            this.xpFlags = 0;
            this.intentFlags = 0;
            this.parentType = 0;
            this.sharedId = -1;
            this.displayId = -1;
            this.winToken = "";
            this.mCompatibilityParamsBackup = null;
            this.mTitle = null;
            this.type = 2;
            this.format = -1;
        }

        public LayoutParams(int _type) {
            super(-1, -1);
            this.x = -1;
            this.y = -1;
            this.needsMenuKey = 0;
            this.surfaceInsets = new Rect();
            this.preservePreviousSurfaceInsets = true;
            this.alpha = 1.0f;
            this.dimAmount = 1.0f;
            this.screenBrightness = -1.0f;
            this.buttonBrightness = -1.0f;
            this.rotationAnimation = 0;
            this.token = null;
            this.packageName = null;
            this.screenOrientation = -1;
            this.layoutInDisplayCutoutMode = 0;
            this.userActivityTimeout = -1L;
            this.accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
            this.hideTimeoutMilliseconds = -1L;
            this.mColorMode = 0;
            this.xpFlags = 0;
            this.intentFlags = 0;
            this.parentType = 0;
            this.sharedId = -1;
            this.displayId = -1;
            this.winToken = "";
            this.mCompatibilityParamsBackup = null;
            this.mTitle = null;
            this.type = _type;
            this.format = -1;
        }

        public LayoutParams(int _type, int _flags) {
            super(-1, -1);
            this.x = -1;
            this.y = -1;
            this.needsMenuKey = 0;
            this.surfaceInsets = new Rect();
            this.preservePreviousSurfaceInsets = true;
            this.alpha = 1.0f;
            this.dimAmount = 1.0f;
            this.screenBrightness = -1.0f;
            this.buttonBrightness = -1.0f;
            this.rotationAnimation = 0;
            this.token = null;
            this.packageName = null;
            this.screenOrientation = -1;
            this.layoutInDisplayCutoutMode = 0;
            this.userActivityTimeout = -1L;
            this.accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
            this.hideTimeoutMilliseconds = -1L;
            this.mColorMode = 0;
            this.xpFlags = 0;
            this.intentFlags = 0;
            this.parentType = 0;
            this.sharedId = -1;
            this.displayId = -1;
            this.winToken = "";
            this.mCompatibilityParamsBackup = null;
            this.mTitle = null;
            this.type = _type;
            this.flags = _flags;
            this.format = -1;
        }

        public LayoutParams(int _type, int _flags, int _format) {
            super(-1, -1);
            this.x = -1;
            this.y = -1;
            this.needsMenuKey = 0;
            this.surfaceInsets = new Rect();
            this.preservePreviousSurfaceInsets = true;
            this.alpha = 1.0f;
            this.dimAmount = 1.0f;
            this.screenBrightness = -1.0f;
            this.buttonBrightness = -1.0f;
            this.rotationAnimation = 0;
            this.token = null;
            this.packageName = null;
            this.screenOrientation = -1;
            this.layoutInDisplayCutoutMode = 0;
            this.userActivityTimeout = -1L;
            this.accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
            this.hideTimeoutMilliseconds = -1L;
            this.mColorMode = 0;
            this.xpFlags = 0;
            this.intentFlags = 0;
            this.parentType = 0;
            this.sharedId = -1;
            this.displayId = -1;
            this.winToken = "";
            this.mCompatibilityParamsBackup = null;
            this.mTitle = null;
            this.type = _type;
            this.flags = _flags;
            this.format = _format;
        }

        public LayoutParams(int w, int h, int _type, int _flags, int _format) {
            super(w, h);
            this.x = -1;
            this.y = -1;
            this.needsMenuKey = 0;
            this.surfaceInsets = new Rect();
            this.preservePreviousSurfaceInsets = true;
            this.alpha = 1.0f;
            this.dimAmount = 1.0f;
            this.screenBrightness = -1.0f;
            this.buttonBrightness = -1.0f;
            this.rotationAnimation = 0;
            this.token = null;
            this.packageName = null;
            this.screenOrientation = -1;
            this.layoutInDisplayCutoutMode = 0;
            this.userActivityTimeout = -1L;
            this.accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
            this.hideTimeoutMilliseconds = -1L;
            this.mColorMode = 0;
            this.xpFlags = 0;
            this.intentFlags = 0;
            this.parentType = 0;
            this.sharedId = -1;
            this.displayId = -1;
            this.winToken = "";
            this.mCompatibilityParamsBackup = null;
            this.mTitle = null;
            this.type = _type;
            this.flags = _flags;
            this.format = _format;
        }

        public LayoutParams(int w, int h, int xpos, int ypos, int _type, int _flags, int _format) {
            super(w, h);
            this.x = -1;
            this.y = -1;
            this.needsMenuKey = 0;
            this.surfaceInsets = new Rect();
            this.preservePreviousSurfaceInsets = true;
            this.alpha = 1.0f;
            this.dimAmount = 1.0f;
            this.screenBrightness = -1.0f;
            this.buttonBrightness = -1.0f;
            this.rotationAnimation = 0;
            this.token = null;
            this.packageName = null;
            this.screenOrientation = -1;
            this.layoutInDisplayCutoutMode = 0;
            this.userActivityTimeout = -1L;
            this.accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
            this.hideTimeoutMilliseconds = -1L;
            this.mColorMode = 0;
            this.xpFlags = 0;
            this.intentFlags = 0;
            this.parentType = 0;
            this.sharedId = -1;
            this.displayId = -1;
            this.winToken = "";
            this.mCompatibilityParamsBackup = null;
            this.mTitle = null;
            this.x = xpos;
            this.y = ypos;
            this.type = _type;
            this.flags = _flags;
            this.format = _format;
        }

        public final void setTitle(CharSequence title) {
            if (title == null) {
                title = "";
            }
            this.mTitle = TextUtils.stringOrSpannedString(title);
        }

        public final CharSequence getTitle() {
            CharSequence charSequence = this.mTitle;
            return charSequence != null ? charSequence : "";
        }

        public final void setSurfaceInsets(View view, boolean manual, boolean preservePrevious) {
            int surfaceInset = (int) Math.ceil(view.getZ() * 2.0f);
            if (surfaceInset == 0) {
                this.surfaceInsets.set(0, 0, 0, 0);
            } else {
                Rect rect = this.surfaceInsets;
                rect.set(Math.max(surfaceInset, rect.left), Math.max(surfaceInset, this.surfaceInsets.top), Math.max(surfaceInset, this.surfaceInsets.right), Math.max(surfaceInset, this.surfaceInsets.bottom));
            }
            this.hasManualSurfaceInsets = manual;
            this.preservePreviousSurfaceInsets = preservePrevious;
        }

        public void setColorMode(int colorMode) {
            this.mColorMode = colorMode;
        }

        public int getColorMode() {
            return this.mColorMode;
        }

        @SystemApi
        public final void setUserActivityTimeout(long timeout) {
            this.userActivityTimeout = timeout;
        }

        @SystemApi
        public final long getUserActivityTimeout() {
            return this.userActivityTimeout;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int parcelableFlags) {
            out.writeInt(this.width);
            out.writeInt(this.height);
            out.writeInt(this.x);
            out.writeInt(this.y);
            out.writeInt(this.type);
            out.writeInt(this.flags);
            out.writeInt(this.privateFlags);
            out.writeInt(this.softInputMode);
            out.writeInt(this.layoutInDisplayCutoutMode);
            out.writeInt(this.gravity);
            out.writeFloat(this.horizontalMargin);
            out.writeFloat(this.verticalMargin);
            out.writeInt(this.format);
            out.writeInt(this.windowAnimations);
            out.writeFloat(this.alpha);
            out.writeFloat(this.dimAmount);
            out.writeFloat(this.screenBrightness);
            out.writeFloat(this.buttonBrightness);
            out.writeInt(this.rotationAnimation);
            out.writeStrongBinder(this.token);
            out.writeString(this.packageName);
            TextUtils.writeToParcel(this.mTitle, out, parcelableFlags);
            out.writeInt(this.screenOrientation);
            out.writeFloat(this.preferredRefreshRate);
            out.writeInt(this.preferredDisplayModeId);
            out.writeInt(this.systemUiVisibility);
            out.writeInt(this.subtreeSystemUiVisibility);
            out.writeInt(this.hasSystemUiListeners ? 1 : 0);
            out.writeInt(this.inputFeatures);
            out.writeLong(this.userActivityTimeout);
            out.writeInt(this.surfaceInsets.left);
            out.writeInt(this.surfaceInsets.top);
            out.writeInt(this.surfaceInsets.right);
            out.writeInt(this.surfaceInsets.bottom);
            out.writeInt(this.hasManualSurfaceInsets ? 1 : 0);
            out.writeInt(this.preservePreviousSurfaceInsets ? 1 : 0);
            out.writeInt(this.needsMenuKey);
            out.writeLong(this.accessibilityIdOfAnchor);
            TextUtils.writeToParcel(this.accessibilityTitle, out, parcelableFlags);
            out.writeInt(this.mColorMode);
            out.writeInt(this.xpFlags);
            out.writeInt(this.intentFlags);
            out.writeInt(this.parentType);
            out.writeInt(this.sharedId);
            out.writeInt(this.displayId);
            out.writeString(this.winToken);
            out.writeLong(this.hideTimeoutMilliseconds);
        }

        public LayoutParams(Parcel in) {
            boolean z;
            boolean z2;
            this.x = -1;
            this.y = -1;
            this.needsMenuKey = 0;
            this.surfaceInsets = new Rect();
            this.preservePreviousSurfaceInsets = true;
            this.alpha = 1.0f;
            this.dimAmount = 1.0f;
            this.screenBrightness = -1.0f;
            this.buttonBrightness = -1.0f;
            this.rotationAnimation = 0;
            this.token = null;
            this.packageName = null;
            this.screenOrientation = -1;
            this.layoutInDisplayCutoutMode = 0;
            this.userActivityTimeout = -1L;
            this.accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
            this.hideTimeoutMilliseconds = -1L;
            this.mColorMode = 0;
            this.xpFlags = 0;
            this.intentFlags = 0;
            this.parentType = 0;
            this.sharedId = -1;
            this.displayId = -1;
            this.winToken = "";
            this.mCompatibilityParamsBackup = null;
            this.mTitle = null;
            this.width = in.readInt();
            this.height = in.readInt();
            this.x = in.readInt();
            this.y = in.readInt();
            this.type = in.readInt();
            this.flags = in.readInt();
            this.privateFlags = in.readInt();
            this.softInputMode = in.readInt();
            this.layoutInDisplayCutoutMode = in.readInt();
            this.gravity = in.readInt();
            this.horizontalMargin = in.readFloat();
            this.verticalMargin = in.readFloat();
            this.format = in.readInt();
            this.windowAnimations = in.readInt();
            this.alpha = in.readFloat();
            this.dimAmount = in.readFloat();
            this.screenBrightness = in.readFloat();
            this.buttonBrightness = in.readFloat();
            this.rotationAnimation = in.readInt();
            this.token = in.readStrongBinder();
            this.packageName = in.readString();
            this.mTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.screenOrientation = in.readInt();
            this.preferredRefreshRate = in.readFloat();
            this.preferredDisplayModeId = in.readInt();
            this.systemUiVisibility = in.readInt();
            this.subtreeSystemUiVisibility = in.readInt();
            if (in.readInt() == 0) {
                z = false;
            } else {
                z = true;
            }
            this.hasSystemUiListeners = z;
            this.inputFeatures = in.readInt();
            this.userActivityTimeout = in.readLong();
            this.surfaceInsets.left = in.readInt();
            this.surfaceInsets.top = in.readInt();
            this.surfaceInsets.right = in.readInt();
            this.surfaceInsets.bottom = in.readInt();
            if (in.readInt() == 0) {
                z2 = false;
            } else {
                z2 = true;
            }
            this.hasManualSurfaceInsets = z2;
            this.preservePreviousSurfaceInsets = in.readInt() != 0;
            this.needsMenuKey = in.readInt();
            this.accessibilityIdOfAnchor = in.readLong();
            this.accessibilityTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            this.mColorMode = in.readInt();
            this.xpFlags = in.readInt();
            this.intentFlags = in.readInt();
            this.parentType = in.readInt();
            this.sharedId = in.readInt();
            this.displayId = in.readInt();
            this.winToken = in.readString();
            this.hideTimeoutMilliseconds = in.readLong();
        }

        public final int copyFrom(LayoutParams o) {
            CharSequence charSequence;
            CharSequence charSequence2;
            int changes = 0;
            if (this.width != o.width) {
                this.width = o.width;
                changes = 0 | 1;
            }
            if (this.height != o.height) {
                this.height = o.height;
                changes |= 1;
            }
            int i = this.x;
            int i2 = o.x;
            if (i != i2) {
                this.x = i2;
                changes |= 1;
            }
            int i3 = this.y;
            int i4 = o.y;
            if (i3 != i4) {
                this.y = i4;
                changes |= 1;
            }
            float f = this.horizontalWeight;
            float f2 = o.horizontalWeight;
            if (f != f2) {
                this.horizontalWeight = f2;
                changes |= 1;
            }
            float f3 = this.verticalWeight;
            float f4 = o.verticalWeight;
            if (f3 != f4) {
                this.verticalWeight = f4;
                changes |= 1;
            }
            float f5 = this.horizontalMargin;
            float f6 = o.horizontalMargin;
            if (f5 != f6) {
                this.horizontalMargin = f6;
                changes |= 1;
            }
            float f7 = this.verticalMargin;
            float f8 = o.verticalMargin;
            if (f7 != f8) {
                this.verticalMargin = f8;
                changes |= 1;
            }
            int i5 = this.type;
            int i6 = o.type;
            if (i5 != i6) {
                this.type = i6;
                changes |= 2;
            }
            int i7 = this.flags;
            int i8 = o.flags;
            if (i7 != i8) {
                int diff = i7 ^ i8;
                if ((201326592 & diff) != 0) {
                    changes |= 524288;
                }
                this.flags = o.flags;
                changes |= 4;
            }
            int diff2 = this.privateFlags;
            int i9 = o.privateFlags;
            if (diff2 != i9) {
                this.privateFlags = i9;
                changes |= 131072;
            }
            int i10 = this.softInputMode;
            int i11 = o.softInputMode;
            if (i10 != i11) {
                this.softInputMode = i11;
                changes |= 512;
            }
            int i12 = this.layoutInDisplayCutoutMode;
            int i13 = o.layoutInDisplayCutoutMode;
            if (i12 != i13) {
                this.layoutInDisplayCutoutMode = i13;
                changes |= 1;
            }
            int i14 = this.gravity;
            int i15 = o.gravity;
            if (i14 != i15) {
                this.gravity = i15;
                changes |= 1;
            }
            int i16 = this.format;
            int i17 = o.format;
            if (i16 != i17) {
                this.format = i17;
                changes |= 8;
            }
            int i18 = this.windowAnimations;
            int i19 = o.windowAnimations;
            if (i18 != i19) {
                this.windowAnimations = i19;
                changes |= 16;
            }
            if (this.token == null) {
                this.token = o.token;
            }
            if (this.packageName == null) {
                this.packageName = o.packageName;
            }
            if (!Objects.equals(this.mTitle, o.mTitle) && (charSequence2 = o.mTitle) != null) {
                this.mTitle = charSequence2;
                changes |= 64;
            }
            float f9 = this.alpha;
            float f10 = o.alpha;
            if (f9 != f10) {
                this.alpha = f10;
                changes |= 128;
            }
            float f11 = this.dimAmount;
            float f12 = o.dimAmount;
            if (f11 != f12) {
                this.dimAmount = f12;
                changes |= 32;
            }
            float f13 = this.screenBrightness;
            float f14 = o.screenBrightness;
            if (f13 != f14) {
                this.screenBrightness = f14;
                changes |= 2048;
            }
            float f15 = this.buttonBrightness;
            float f16 = o.buttonBrightness;
            if (f15 != f16) {
                this.buttonBrightness = f16;
                changes |= 8192;
            }
            int i20 = this.rotationAnimation;
            int i21 = o.rotationAnimation;
            if (i20 != i21) {
                this.rotationAnimation = i21;
                changes |= 4096;
            }
            int i22 = this.screenOrientation;
            int i23 = o.screenOrientation;
            if (i22 != i23) {
                this.screenOrientation = i23;
                changes |= 1024;
            }
            float f17 = this.preferredRefreshRate;
            float f18 = o.preferredRefreshRate;
            if (f17 != f18) {
                this.preferredRefreshRate = f18;
                changes |= 2097152;
            }
            int i24 = this.preferredDisplayModeId;
            int i25 = o.preferredDisplayModeId;
            if (i24 != i25) {
                this.preferredDisplayModeId = i25;
                changes |= 8388608;
            }
            if (this.systemUiVisibility != o.systemUiVisibility || this.subtreeSystemUiVisibility != o.subtreeSystemUiVisibility) {
                this.systemUiVisibility = o.systemUiVisibility;
                this.subtreeSystemUiVisibility = o.subtreeSystemUiVisibility;
                changes |= 16384;
            }
            boolean z = this.hasSystemUiListeners;
            boolean z2 = o.hasSystemUiListeners;
            if (z != z2) {
                this.hasSystemUiListeners = z2;
                changes |= 32768;
            }
            int i26 = this.inputFeatures;
            int i27 = o.inputFeatures;
            if (i26 != i27) {
                this.inputFeatures = i27;
                changes |= 65536;
            }
            long j = this.userActivityTimeout;
            long j2 = o.userActivityTimeout;
            if (j != j2) {
                this.userActivityTimeout = j2;
                changes |= 262144;
            }
            if (!this.surfaceInsets.equals(o.surfaceInsets)) {
                this.surfaceInsets.set(o.surfaceInsets);
                changes |= 1048576;
            }
            boolean z3 = this.hasManualSurfaceInsets;
            boolean z4 = o.hasManualSurfaceInsets;
            if (z3 != z4) {
                this.hasManualSurfaceInsets = z4;
                changes |= 1048576;
            }
            boolean z5 = this.preservePreviousSurfaceInsets;
            boolean z6 = o.preservePreviousSurfaceInsets;
            if (z5 != z6) {
                this.preservePreviousSurfaceInsets = z6;
                changes |= 1048576;
            }
            int i28 = this.needsMenuKey;
            int i29 = o.needsMenuKey;
            if (i28 != i29) {
                this.needsMenuKey = i29;
                changes |= 4194304;
            }
            long j3 = this.accessibilityIdOfAnchor;
            long j4 = o.accessibilityIdOfAnchor;
            if (j3 != j4) {
                this.accessibilityIdOfAnchor = j4;
                changes |= 16777216;
            }
            if (!Objects.equals(this.accessibilityTitle, o.accessibilityTitle) && (charSequence = o.accessibilityTitle) != null) {
                this.accessibilityTitle = charSequence;
                changes |= 33554432;
            }
            int i30 = this.mColorMode;
            int i31 = o.mColorMode;
            if (i30 != i31) {
                this.mColorMode = i31;
                changes |= 67108864;
            }
            int i32 = this.xpFlags;
            int i33 = o.xpFlags;
            if (i32 != i33) {
                this.xpFlags = i33;
                changes |= 131072;
            }
            int i34 = this.intentFlags;
            int i35 = o.intentFlags;
            if (i34 != i35) {
                this.intentFlags = i35;
                changes |= 131072;
            }
            int i36 = this.parentType;
            int i37 = o.parentType;
            if (i36 != i37) {
                this.parentType = i37;
                changes |= 2;
            }
            int i38 = this.sharedId;
            int i39 = o.sharedId;
            if (i38 != i39) {
                this.sharedId = i39;
                changes |= -1;
            }
            int i40 = this.displayId;
            int i41 = o.displayId;
            if (i40 != i41) {
                this.displayId = i41;
                changes |= -1;
            }
            String str = this.winToken;
            String str2 = o.winToken;
            if (str != str2) {
                this.winToken = str2;
                changes |= -1;
            }
            this.hideTimeoutMilliseconds = o.hideTimeoutMilliseconds;
            return changes;
        }

        @Override // android.view.ViewGroup.LayoutParams
        public String debug(String output) {
            Log.d("Debug", output + "Contents of " + this + SettingsStringUtil.DELIMITER);
            String output2 = super.debug("");
            Log.d("Debug", output2);
            Log.d("Debug", "");
            Log.d("Debug", "WindowManager.LayoutParams={title=" + ((Object) this.mTitle) + "}");
            return "";
        }

        public String toString() {
            return toString("");
        }

        public void dumpDimensions(StringBuilder sb) {
            String valueOf;
            sb.append('(');
            sb.append(this.x);
            sb.append(',');
            sb.append(this.y);
            sb.append(")(");
            String str = "wrap";
            if (this.width == -1) {
                valueOf = "fill";
            } else {
                valueOf = this.width == -2 ? "wrap" : String.valueOf(this.width);
            }
            sb.append(valueOf);
            sb.append(EpicenterTranslateClipReveal.StateProperty.TARGET_X);
            if (this.height == -1) {
                str = "fill";
            } else if (this.height != -2) {
                str = String.valueOf(this.height);
            }
            sb.append(str);
            sb.append(")");
        }

        public String toString(String prefix) {
            StringBuilder sb = new StringBuilder(256);
            sb.append('{');
            dumpDimensions(sb);
            if (this.horizontalMargin != 0.0f) {
                sb.append(" hm=");
                sb.append(this.horizontalMargin);
            }
            if (this.verticalMargin != 0.0f) {
                sb.append(" vm=");
                sb.append(this.verticalMargin);
            }
            if (this.gravity != 0) {
                sb.append(" gr=");
                sb.append(Gravity.toString(this.gravity));
            }
            if (this.softInputMode != 0) {
                sb.append(" sim={");
                sb.append(softInputModeToString(this.softInputMode));
                sb.append('}');
            }
            if (this.layoutInDisplayCutoutMode != 0) {
                sb.append(" layoutInDisplayCutoutMode=");
                sb.append(layoutInDisplayCutoutModeToString(this.layoutInDisplayCutoutMode));
            }
            sb.append(" ty=");
            sb.append(ViewDebug.intToString(LayoutParams.class, "type", this.type));
            if (this.format != -1) {
                sb.append(" fmt=");
                sb.append(PixelFormat.formatToString(this.format));
            }
            if (this.windowAnimations != 0) {
                sb.append(" wanim=0x");
                sb.append(Integer.toHexString(this.windowAnimations));
            }
            if (this.screenOrientation != -1) {
                sb.append(" or=");
                sb.append(ActivityInfo.screenOrientationToString(this.screenOrientation));
            }
            if (this.alpha != 1.0f) {
                sb.append(" alpha=");
                sb.append(this.alpha);
            }
            if (this.screenBrightness != -1.0f) {
                sb.append(" sbrt=");
                sb.append(this.screenBrightness);
            }
            if (this.buttonBrightness != -1.0f) {
                sb.append(" bbrt=");
                sb.append(this.buttonBrightness);
            }
            if (this.rotationAnimation != 0) {
                sb.append(" rotAnim=");
                sb.append(rotationAnimationToString(this.rotationAnimation));
            }
            if (this.preferredRefreshRate != 0.0f) {
                sb.append(" preferredRefreshRate=");
                sb.append(this.preferredRefreshRate);
            }
            if (this.preferredDisplayModeId != 0) {
                sb.append(" preferredDisplayMode=");
                sb.append(this.preferredDisplayModeId);
            }
            if (this.hasSystemUiListeners) {
                sb.append(" sysuil=");
                sb.append(this.hasSystemUiListeners);
            }
            if (this.inputFeatures != 0) {
                sb.append(" if=");
                sb.append(inputFeatureToString(this.inputFeatures));
            }
            if (this.userActivityTimeout >= 0) {
                sb.append(" userActivityTimeout=");
                sb.append(this.userActivityTimeout);
            }
            if (this.surfaceInsets.left != 0 || this.surfaceInsets.top != 0 || this.surfaceInsets.right != 0 || this.surfaceInsets.bottom != 0 || this.hasManualSurfaceInsets || !this.preservePreviousSurfaceInsets) {
                sb.append(" surfaceInsets=");
                sb.append(this.surfaceInsets);
                if (this.hasManualSurfaceInsets) {
                    sb.append(" (manual)");
                }
                if (!this.preservePreviousSurfaceInsets) {
                    sb.append(" (!preservePreviousSurfaceInsets)");
                }
            }
            if (this.needsMenuKey == 1) {
                sb.append(" needsMenuKey");
            }
            if (this.mColorMode != 0) {
                sb.append(" colorMode=");
                sb.append(ActivityInfo.colorModeToString(this.mColorMode));
            }
            sb.append(System.lineSeparator());
            sb.append(prefix);
            sb.append("  fl=");
            sb.append(ViewDebug.flagsToString(LayoutParams.class, "flags", this.flags));
            if (this.privateFlags != 0) {
                sb.append(System.lineSeparator());
                sb.append(prefix);
                sb.append("  pfl=");
                sb.append(ViewDebug.flagsToString(LayoutParams.class, "privateFlags", this.privateFlags));
            }
            if (this.systemUiVisibility != 0) {
                sb.append(System.lineSeparator());
                sb.append(prefix);
                sb.append("  sysui=");
                sb.append(ViewDebug.flagsToString(View.class, "mSystemUiVisibility", this.systemUiVisibility));
            }
            if (this.subtreeSystemUiVisibility != 0) {
                sb.append(System.lineSeparator());
                sb.append(prefix);
                sb.append("  vsysui=");
                sb.append(ViewDebug.flagsToString(View.class, "mSystemUiVisibility", this.subtreeSystemUiVisibility));
            }
            if (this.sharedId != -1) {
                sb.append(System.lineSeparator());
                sb.append(prefix);
                sb.append("  sharedId=");
                sb.append(this.sharedId);
                boolean isAlert = xpWindowManager.isAlertWindowType(this);
                sb.append(System.lineSeparator());
                sb.append(prefix);
                sb.append("  isAlert=");
                sb.append(isAlert);
            }
            sb.append('}');
            return sb.toString();
        }

        public void writeToProto(ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, this.type);
            proto.write(1120986464258L, this.x);
            proto.write(1120986464259L, this.y);
            proto.write(1120986464260L, this.width);
            proto.write(1120986464261L, this.height);
            proto.write(1108101562374L, this.horizontalMargin);
            proto.write(WindowLayoutParamsProto.VERTICAL_MARGIN, this.verticalMargin);
            proto.write(1120986464264L, this.gravity);
            proto.write(1120986464265L, this.softInputMode);
            proto.write(1159641169930L, this.format);
            proto.write(1120986464267L, this.windowAnimations);
            proto.write(1108101562380L, this.alpha);
            proto.write(WindowLayoutParamsProto.SCREEN_BRIGHTNESS, this.screenBrightness);
            proto.write(WindowLayoutParamsProto.BUTTON_BRIGHTNESS, this.buttonBrightness);
            proto.write(1159641169935L, this.rotationAnimation);
            proto.write(WindowLayoutParamsProto.PREFERRED_REFRESH_RATE, this.preferredRefreshRate);
            proto.write(1120986464273L, this.preferredDisplayModeId);
            proto.write(1133871366162L, this.hasSystemUiListeners);
            proto.write(WindowLayoutParamsProto.INPUT_FEATURE_FLAGS, this.inputFeatures);
            proto.write(1112396529684L, this.userActivityTimeout);
            proto.write(1159641169942L, this.needsMenuKey);
            proto.write(1159641169943L, this.mColorMode);
            proto.write(WindowLayoutParamsProto.FLAGS, this.flags);
            proto.write(WindowLayoutParamsProto.PRIVATE_FLAGS, this.privateFlags);
            proto.write(WindowLayoutParamsProto.SYSTEM_UI_VISIBILITY_FLAGS, this.systemUiVisibility);
            proto.write(WindowLayoutParamsProto.SUBTREE_SYSTEM_UI_VISIBILITY_FLAGS, this.subtreeSystemUiVisibility);
            proto.end(token);
        }

        public void scale(float scale) {
            this.x = (int) ((this.x * scale) + 0.5f);
            this.y = (int) ((this.y * scale) + 0.5f);
            if (this.width > 0) {
                this.width = (int) ((this.width * scale) + 0.5f);
            }
            if (this.height > 0) {
                this.height = (int) ((this.height * scale) + 0.5f);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @UnsupportedAppUsage
        public void backup() {
            int[] backup = this.mCompatibilityParamsBackup;
            if (backup == null) {
                int[] iArr = new int[4];
                this.mCompatibilityParamsBackup = iArr;
                backup = iArr;
            }
            backup[0] = this.x;
            backup[1] = this.y;
            backup[2] = this.width;
            backup[3] = this.height;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @UnsupportedAppUsage
        public void restore() {
            int[] backup = this.mCompatibilityParamsBackup;
            if (backup != null) {
                this.x = backup[0];
                this.y = backup[1];
                this.width = backup[2];
                this.height = backup[3];
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.view.ViewGroup.LayoutParams
        public void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("x", this.x);
            encoder.addProperty("y", this.y);
            encoder.addProperty("horizontalWeight", this.horizontalWeight);
            encoder.addProperty("verticalWeight", this.verticalWeight);
            encoder.addProperty("type", this.type);
            encoder.addProperty("flags", this.flags);
        }

        public boolean isFullscreen() {
            return this.x == 0 && this.y == 0 && this.width == -1 && this.height == -1;
        }

        private static String layoutInDisplayCutoutModeToString(int mode) {
            if (mode != 0) {
                if (mode != 1) {
                    if (mode == 2) {
                        return "never";
                    }
                    return "unknown(" + mode + ")";
                }
                return "always";
            }
            return "default";
        }

        private static String softInputModeToString(int softInputMode) {
            StringBuilder result = new StringBuilder();
            int state = softInputMode & 15;
            if (state != 0) {
                result.append("state=");
                if (state == 1) {
                    result.append("unchanged");
                } else if (state == 2) {
                    result.append("hidden");
                } else if (state == 3) {
                    result.append("always_hidden");
                } else if (state == 4) {
                    result.append(CalendarContract.CalendarColumns.VISIBLE);
                } else if (state == 5) {
                    result.append("always_visible");
                } else {
                    result.append(state);
                }
                result.append(' ');
            }
            int adjust = softInputMode & 240;
            if (adjust != 0) {
                result.append("adjust=");
                if (adjust == 16) {
                    result.append("resize");
                } else if (adjust == 32) {
                    result.append(TextToSpeech.Engine.KEY_PARAM_PAN);
                } else if (adjust == 48) {
                    result.append("nothing");
                } else {
                    result.append(adjust);
                }
                result.append(' ');
            }
            if ((softInputMode & 256) != 0) {
                result.append("forwardNavigation");
                result.append(' ');
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        }

        private static String rotationAnimationToString(int rotationAnimation) {
            if (rotationAnimation != -1) {
                if (rotationAnimation != 0) {
                    if (rotationAnimation != 1) {
                        if (rotationAnimation != 2) {
                            if (rotationAnimation == 3) {
                                return "SEAMLESS";
                            }
                            return Integer.toString(rotationAnimation);
                        }
                        return "JUMPCUT";
                    }
                    return "CROSSFADE";
                }
                return "ROTATE";
            }
            return "UNSPECIFIED";
        }

        private static String inputFeatureToString(int inputFeature) {
            if (inputFeature != 1) {
                if (inputFeature != 2) {
                    if (inputFeature == 4) {
                        return "DISABLE_USER_ACTIVITY";
                    }
                    return Integer.toString(inputFeature);
                }
                return "NO_INPUT_CHANNEL";
            }
            return "DISABLE_POINTER_GESTURES";
        }
    }
}
