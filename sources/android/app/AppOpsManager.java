package android.app;

import android.Manifest;
import android.annotation.SystemApi;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.ArrayMap;
import com.android.internal.app.IAppOpsActiveCallback;
import com.android.internal.app.IAppOpsCallback;
import com.android.internal.app.IAppOpsService;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
public class AppOpsManager {
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_ERRORED = 2;
    private protected static final int MODE_FOREGROUND = 4;
    public static final int MODE_IGNORED = 1;
    private protected static final int OP_ACCEPT_HANDOVER = 74;
    private protected static final int OP_ACCESS_NOTIFICATIONS = 25;
    private protected static final int OP_ACTIVATE_VPN = 47;
    private protected static final int OP_ADD_VOICEMAIL = 52;
    private protected static final int OP_ANSWER_PHONE_CALLS = 69;
    private protected static final int OP_ASSIST_SCREENSHOT = 50;
    private protected static final int OP_ASSIST_STRUCTURE = 49;
    private protected static final int OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    private protected static final int OP_AUDIO_ALARM_VOLUME = 37;
    private protected static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;
    private protected static final int OP_AUDIO_MASTER_VOLUME = 33;
    private protected static final int OP_AUDIO_MEDIA_VOLUME = 36;
    private protected static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;
    private protected static final int OP_AUDIO_RING_VOLUME = 35;
    private protected static final int OP_AUDIO_VOICE_VOLUME = 34;
    private protected static final int OP_BIND_ACCESSIBILITY_SERVICE = 73;
    private protected static final int OP_BLUETOOTH_SCAN = 77;
    private protected static final int OP_BODY_SENSORS = 56;
    private protected static final int OP_CALL_PHONE = 13;
    private protected static final int OP_CAMERA = 26;
    private protected static final int OP_CHANGE_WIFI_STATE = 71;
    private protected static final int OP_COARSE_LOCATION = 0;
    private protected static final int OP_FINE_LOCATION = 1;
    private protected static final int OP_GET_ACCOUNTS = 62;
    private protected static final int OP_GET_USAGE_STATS = 43;
    private protected static final int OP_GPS = 2;
    private protected static final int OP_INSTANT_APP_START_FOREGROUND = 68;
    private protected static final int OP_MANAGE_IPSEC_TUNNELS = 75;
    private protected static final int OP_MOCK_LOCATION = 58;
    private protected static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;
    private protected static final int OP_MONITOR_LOCATION = 41;
    private protected static final int OP_MUTE_MICROPHONE = 44;
    private protected static final int OP_NEIGHBORING_CELLS = 12;
    private protected static final int OP_NONE = -1;
    private protected static final int OP_PICTURE_IN_PICTURE = 67;
    private protected static final int OP_PLAY_AUDIO = 28;
    private protected static final int OP_POST_NOTIFICATION = 11;
    private protected static final int OP_PROCESS_OUTGOING_CALLS = 54;
    private protected static final int OP_PROJECT_MEDIA = 46;
    private protected static final int OP_READ_CALENDAR = 8;
    private protected static final int OP_READ_CALL_LOG = 6;
    private protected static final int OP_READ_CELL_BROADCASTS = 57;
    private protected static final int OP_READ_CLIPBOARD = 29;
    private protected static final int OP_READ_CONTACTS = 4;
    private protected static final int OP_READ_EXTERNAL_STORAGE = 59;
    private protected static final int OP_READ_ICC_SMS = 21;
    private protected static final int OP_READ_PHONE_NUMBERS = 65;
    private protected static final int OP_READ_PHONE_STATE = 51;
    private protected static final int OP_READ_SMS = 14;
    private protected static final int OP_RECEIVE_EMERGECY_SMS = 17;
    private protected static final int OP_RECEIVE_MMS = 18;
    private protected static final int OP_RECEIVE_SMS = 16;
    private protected static final int OP_RECEIVE_WAP_PUSH = 19;
    public static final int OP_RECORD_AUDIO = 27;
    private protected static final int OP_REQUEST_DELETE_PACKAGES = 72;
    private protected static final int OP_REQUEST_INSTALL_PACKAGES = 66;
    private protected static final int OP_RUN_ANY_IN_BACKGROUND = 70;
    private protected static final int OP_RUN_IN_BACKGROUND = 63;
    private protected static final int OP_SEND_SMS = 20;
    private protected static final int OP_START_FOREGROUND = 76;
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    private protected static final int OP_TAKE_AUDIO_FOCUS = 32;
    private protected static final int OP_TAKE_MEDIA_BUTTONS = 31;
    private protected static final int OP_TOAST_WINDOW = 45;
    private protected static final int OP_TURN_SCREEN_ON = 61;
    private protected static final int OP_USE_FINGERPRINT = 55;
    private protected static final int OP_USE_SIP = 53;
    private protected static final int OP_VIBRATE = 3;
    private protected static final int OP_WAKE_LOCK = 40;
    private protected static final int OP_WIFI_SCAN = 10;
    private protected static final int OP_WRITE_CALENDAR = 9;
    private protected static final int OP_WRITE_CALL_LOG = 7;
    private protected static final int OP_WRITE_CLIPBOARD = 30;
    private protected static final int OP_WRITE_CONTACTS = 5;
    private protected static final int OP_WRITE_EXTERNAL_STORAGE = 60;
    private protected static final int OP_WRITE_ICC_SMS = 22;
    private protected static final int OP_WRITE_SETTINGS = 23;
    private protected static final int OP_WRITE_SMS = 15;
    private protected static final int OP_WRITE_WALLPAPER = 48;
    public static final int UID_STATE_BACKGROUND = 4;
    public static final int UID_STATE_CACHED = 5;
    public static final int UID_STATE_FOREGROUND = 3;
    public static final int UID_STATE_FOREGROUND_SERVICE = 2;
    public static final int UID_STATE_LAST_NON_RESTRICTED = 2;
    public static final int UID_STATE_PERSISTENT = 0;
    public static final int UID_STATE_TOP = 1;
    private protected static final int WATCH_FOREGROUND_CHANGES = 1;
    private protected static final int _NUM_OP = 78;
    public static final int _NUM_UID_STATE = 6;
    static IBinder sToken;
    final Context mContext;
    public private protected final IAppOpsService mService;
    public static final String[] MODE_NAMES = {"allow", "ignore", "deny", "default", "foreground"};
    private static final int[] RUNTIME_AND_APPOP_PERMISSIONS_OPS = {4, 5, 62, 8, 9, 20, 16, 14, 19, 18, 57, 59, 60, 0, 1, 51, 65, 13, 6, 7, 52, 53, 54, 69, 74, 27, 26, 56, 25, 24, 23, 66, 76};
    private static int[] sOpToSwitch = {0, 0, 0, 3, 4, 5, 6, 7, 8, 9, 0, 11, 0, 13, 14, 15, 16, 16, 18, 19, 20, 14, 15, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 0, 0, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 0};
    public static final String OPSTR_COARSE_LOCATION = "android:coarse_location";
    public static final String OPSTR_FINE_LOCATION = "android:fine_location";
    @SystemApi
    public static final String OPSTR_GPS = "android:gps";
    @SystemApi
    public static final String OPSTR_VIBRATE = "android:vibrate";
    public static final String OPSTR_READ_CONTACTS = "android:read_contacts";
    public static final String OPSTR_WRITE_CONTACTS = "android:write_contacts";
    public static final String OPSTR_READ_CALL_LOG = "android:read_call_log";
    public static final String OPSTR_WRITE_CALL_LOG = "android:write_call_log";
    public static final String OPSTR_READ_CALENDAR = "android:read_calendar";
    public static final String OPSTR_WRITE_CALENDAR = "android:write_calendar";
    @SystemApi
    public static final String OPSTR_WIFI_SCAN = "android:wifi_scan";
    @SystemApi
    public static final String OPSTR_POST_NOTIFICATION = "android:post_notification";
    @SystemApi
    public static final String OPSTR_NEIGHBORING_CELLS = "android:neighboring_cells";
    public static final String OPSTR_CALL_PHONE = "android:call_phone";
    public static final String OPSTR_READ_SMS = "android:read_sms";
    @SystemApi
    public static final String OPSTR_WRITE_SMS = "android:write_sms";
    public static final String OPSTR_RECEIVE_SMS = "android:receive_sms";
    @SystemApi
    public static final String OPSTR_RECEIVE_EMERGENCY_BROADCAST = "android:receive_emergency_broadcast";
    public static final String OPSTR_RECEIVE_MMS = "android:receive_mms";
    public static final String OPSTR_RECEIVE_WAP_PUSH = "android:receive_wap_push";
    public static final String OPSTR_SEND_SMS = "android:send_sms";
    @SystemApi
    public static final String OPSTR_READ_ICC_SMS = "android:read_icc_sms";
    @SystemApi
    public static final String OPSTR_WRITE_ICC_SMS = "android:write_icc_sms";
    public static final String OPSTR_WRITE_SETTINGS = "android:write_settings";
    public static final String OPSTR_SYSTEM_ALERT_WINDOW = "android:system_alert_window";
    @SystemApi
    public static final String OPSTR_ACCESS_NOTIFICATIONS = "android:access_notifications";
    public static final String OPSTR_CAMERA = "android:camera";
    public static final String OPSTR_RECORD_AUDIO = "android:record_audio";
    @SystemApi
    public static final String OPSTR_PLAY_AUDIO = "android:play_audio";
    @SystemApi
    public static final String OPSTR_READ_CLIPBOARD = "android:read_clipboard";
    @SystemApi
    public static final String OPSTR_WRITE_CLIPBOARD = "android:write_clipboard";
    @SystemApi
    public static final String OPSTR_TAKE_MEDIA_BUTTONS = "android:take_media_buttons";
    @SystemApi
    public static final String OPSTR_TAKE_AUDIO_FOCUS = "android:take_audio_focus";
    @SystemApi
    public static final String OPSTR_AUDIO_MASTER_VOLUME = "android:audio_master_volume";
    @SystemApi
    public static final String OPSTR_AUDIO_VOICE_VOLUME = "android:audio_voice_volume";
    @SystemApi
    public static final String OPSTR_AUDIO_RING_VOLUME = "android:audio_ring_volume";
    @SystemApi
    public static final String OPSTR_AUDIO_MEDIA_VOLUME = "android:audio_media_volume";
    @SystemApi
    public static final String OPSTR_AUDIO_ALARM_VOLUME = "android:audio_alarm_volume";
    @SystemApi
    public static final String OPSTR_AUDIO_NOTIFICATION_VOLUME = "android:audio_notification_volume";
    @SystemApi
    public static final String OPSTR_AUDIO_BLUETOOTH_VOLUME = "android:audio_bluetooth_volume";
    @SystemApi
    public static final String OPSTR_WAKE_LOCK = "android:wake_lock";
    public static final String OPSTR_MONITOR_LOCATION = "android:monitor_location";
    public static final String OPSTR_MONITOR_HIGH_POWER_LOCATION = "android:monitor_location_high_power";
    public static final String OPSTR_GET_USAGE_STATS = "android:get_usage_stats";
    @SystemApi
    public static final String OPSTR_MUTE_MICROPHONE = "android:mute_microphone";
    @SystemApi
    public static final String OPSTR_TOAST_WINDOW = "android:toast_window";
    @SystemApi
    public static final String OPSTR_PROJECT_MEDIA = "android:project_media";
    @SystemApi
    public static final String OPSTR_ACTIVATE_VPN = "android:activate_vpn";
    @SystemApi
    public static final String OPSTR_WRITE_WALLPAPER = "android:write_wallpaper";
    @SystemApi
    public static final String OPSTR_ASSIST_STRUCTURE = "android:assist_structure";
    @SystemApi
    public static final String OPSTR_ASSIST_SCREENSHOT = "android:assist_screenshot";
    public static final String OPSTR_READ_PHONE_STATE = "android:read_phone_state";
    public static final String OPSTR_ADD_VOICEMAIL = "android:add_voicemail";
    public static final String OPSTR_USE_SIP = "android:use_sip";
    public static final String OPSTR_PROCESS_OUTGOING_CALLS = "android:process_outgoing_calls";
    public static final String OPSTR_USE_FINGERPRINT = "android:use_fingerprint";
    public static final String OPSTR_BODY_SENSORS = "android:body_sensors";
    public static final String OPSTR_READ_CELL_BROADCASTS = "android:read_cell_broadcasts";
    public static final String OPSTR_MOCK_LOCATION = "android:mock_location";
    public static final String OPSTR_READ_EXTERNAL_STORAGE = "android:read_external_storage";
    public static final String OPSTR_WRITE_EXTERNAL_STORAGE = "android:write_external_storage";
    @SystemApi
    public static final String OPSTR_TURN_SCREEN_ON = "android:turn_screen_on";
    @SystemApi
    public static final String OPSTR_GET_ACCOUNTS = "android:get_accounts";
    @SystemApi
    public static final String OPSTR_RUN_IN_BACKGROUND = "android:run_in_background";
    @SystemApi
    public static final String OPSTR_AUDIO_ACCESSIBILITY_VOLUME = "android:audio_accessibility_volume";
    public static final String OPSTR_READ_PHONE_NUMBERS = "android:read_phone_numbers";
    @SystemApi
    public static final String OPSTR_REQUEST_INSTALL_PACKAGES = "android:request_install_packages";
    public static final String OPSTR_PICTURE_IN_PICTURE = "android:picture_in_picture";
    @SystemApi
    public static final String OPSTR_INSTANT_APP_START_FOREGROUND = "android:instant_app_start_foreground";
    public static final String OPSTR_ANSWER_PHONE_CALLS = "android:answer_phone_calls";
    @SystemApi
    public static final String OPSTR_RUN_ANY_IN_BACKGROUND = "android:run_any_in_background";
    @SystemApi
    public static final String OPSTR_CHANGE_WIFI_STATE = "android:change_wifi_state";
    @SystemApi
    public static final String OPSTR_REQUEST_DELETE_PACKAGES = "android:request_delete_packages";
    @SystemApi
    public static final String OPSTR_BIND_ACCESSIBILITY_SERVICE = "android:bind_accessibility_service";
    @SystemApi
    public static final String OPSTR_ACCEPT_HANDOVER = "android:accept_handover";
    @SystemApi
    public static final String OPSTR_MANAGE_IPSEC_TUNNELS = "android:manage_ipsec_tunnels";
    @SystemApi
    public static final String OPSTR_START_FOREGROUND = "android:start_foreground";
    public static final String OPSTR_BLUETOOTH_SCAN = "android:bluetooth_scan";
    private static String[] sOpToString = {OPSTR_COARSE_LOCATION, OPSTR_FINE_LOCATION, OPSTR_GPS, OPSTR_VIBRATE, OPSTR_READ_CONTACTS, OPSTR_WRITE_CONTACTS, OPSTR_READ_CALL_LOG, OPSTR_WRITE_CALL_LOG, OPSTR_READ_CALENDAR, OPSTR_WRITE_CALENDAR, OPSTR_WIFI_SCAN, OPSTR_POST_NOTIFICATION, OPSTR_NEIGHBORING_CELLS, OPSTR_CALL_PHONE, OPSTR_READ_SMS, OPSTR_WRITE_SMS, OPSTR_RECEIVE_SMS, OPSTR_RECEIVE_EMERGENCY_BROADCAST, OPSTR_RECEIVE_MMS, OPSTR_RECEIVE_WAP_PUSH, OPSTR_SEND_SMS, OPSTR_READ_ICC_SMS, OPSTR_WRITE_ICC_SMS, OPSTR_WRITE_SETTINGS, OPSTR_SYSTEM_ALERT_WINDOW, OPSTR_ACCESS_NOTIFICATIONS, OPSTR_CAMERA, OPSTR_RECORD_AUDIO, OPSTR_PLAY_AUDIO, OPSTR_READ_CLIPBOARD, OPSTR_WRITE_CLIPBOARD, OPSTR_TAKE_MEDIA_BUTTONS, OPSTR_TAKE_AUDIO_FOCUS, OPSTR_AUDIO_MASTER_VOLUME, OPSTR_AUDIO_VOICE_VOLUME, OPSTR_AUDIO_RING_VOLUME, OPSTR_AUDIO_MEDIA_VOLUME, OPSTR_AUDIO_ALARM_VOLUME, OPSTR_AUDIO_NOTIFICATION_VOLUME, OPSTR_AUDIO_BLUETOOTH_VOLUME, OPSTR_WAKE_LOCK, OPSTR_MONITOR_LOCATION, OPSTR_MONITOR_HIGH_POWER_LOCATION, OPSTR_GET_USAGE_STATS, OPSTR_MUTE_MICROPHONE, OPSTR_TOAST_WINDOW, OPSTR_PROJECT_MEDIA, OPSTR_ACTIVATE_VPN, OPSTR_WRITE_WALLPAPER, OPSTR_ASSIST_STRUCTURE, OPSTR_ASSIST_SCREENSHOT, OPSTR_READ_PHONE_STATE, OPSTR_ADD_VOICEMAIL, OPSTR_USE_SIP, OPSTR_PROCESS_OUTGOING_CALLS, OPSTR_USE_FINGERPRINT, OPSTR_BODY_SENSORS, OPSTR_READ_CELL_BROADCASTS, OPSTR_MOCK_LOCATION, OPSTR_READ_EXTERNAL_STORAGE, OPSTR_WRITE_EXTERNAL_STORAGE, OPSTR_TURN_SCREEN_ON, OPSTR_GET_ACCOUNTS, OPSTR_RUN_IN_BACKGROUND, OPSTR_AUDIO_ACCESSIBILITY_VOLUME, OPSTR_READ_PHONE_NUMBERS, OPSTR_REQUEST_INSTALL_PACKAGES, OPSTR_PICTURE_IN_PICTURE, OPSTR_INSTANT_APP_START_FOREGROUND, OPSTR_ANSWER_PHONE_CALLS, OPSTR_RUN_ANY_IN_BACKGROUND, OPSTR_CHANGE_WIFI_STATE, OPSTR_REQUEST_DELETE_PACKAGES, OPSTR_BIND_ACCESSIBILITY_SERVICE, OPSTR_ACCEPT_HANDOVER, OPSTR_MANAGE_IPSEC_TUNNELS, OPSTR_START_FOREGROUND, OPSTR_BLUETOOTH_SCAN};
    private static String[] sOpNames = {"COARSE_LOCATION", "FINE_LOCATION", "GPS", "VIBRATE", "READ_CONTACTS", "WRITE_CONTACTS", "READ_CALL_LOG", "WRITE_CALL_LOG", "READ_CALENDAR", "WRITE_CALENDAR", "WIFI_SCAN", "POST_NOTIFICATION", "NEIGHBORING_CELLS", "CALL_PHONE", "READ_SMS", "WRITE_SMS", "RECEIVE_SMS", "RECEIVE_EMERGECY_SMS", "RECEIVE_MMS", "RECEIVE_WAP_PUSH", "SEND_SMS", "READ_ICC_SMS", "WRITE_ICC_SMS", "WRITE_SETTINGS", "SYSTEM_ALERT_WINDOW", "ACCESS_NOTIFICATIONS", "CAMERA", "RECORD_AUDIO", "PLAY_AUDIO", "READ_CLIPBOARD", "WRITE_CLIPBOARD", "TAKE_MEDIA_BUTTONS", "TAKE_AUDIO_FOCUS", "AUDIO_MASTER_VOLUME", "AUDIO_VOICE_VOLUME", "AUDIO_RING_VOLUME", "AUDIO_MEDIA_VOLUME", "AUDIO_ALARM_VOLUME", "AUDIO_NOTIFICATION_VOLUME", "AUDIO_BLUETOOTH_VOLUME", "WAKE_LOCK", "MONITOR_LOCATION", "MONITOR_HIGH_POWER_LOCATION", "GET_USAGE_STATS", "MUTE_MICROPHONE", "TOAST_WINDOW", "PROJECT_MEDIA", "ACTIVATE_VPN", "WRITE_WALLPAPER", "ASSIST_STRUCTURE", "ASSIST_SCREENSHOT", "OP_READ_PHONE_STATE", "ADD_VOICEMAIL", "USE_SIP", "PROCESS_OUTGOING_CALLS", "USE_FINGERPRINT", "BODY_SENSORS", "READ_CELL_BROADCASTS", "MOCK_LOCATION", "READ_EXTERNAL_STORAGE", "WRITE_EXTERNAL_STORAGE", "TURN_ON_SCREEN", "GET_ACCOUNTS", "RUN_IN_BACKGROUND", "AUDIO_ACCESSIBILITY_VOLUME", "READ_PHONE_NUMBERS", "REQUEST_INSTALL_PACKAGES", "PICTURE_IN_PICTURE", "INSTANT_APP_START_FOREGROUND", "ANSWER_PHONE_CALLS", "RUN_ANY_IN_BACKGROUND", "CHANGE_WIFI_STATE", "REQUEST_DELETE_PACKAGES", "BIND_ACCESSIBILITY_SERVICE", "ACCEPT_HANDOVER", "MANAGE_IPSEC_TUNNELS", "START_FOREGROUND", "BLUETOOTH_SCAN"};
    public protected static String[] sOpPerms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, null, Manifest.permission.VIBRATE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.ACCESS_WIFI_STATE, null, null, Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS, null, Manifest.permission.RECEIVE_SMS, Manifest.permission.RECEIVE_EMERGENCY_BROADCAST, Manifest.permission.RECEIVE_MMS, Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, null, Manifest.permission.WRITE_SETTINGS, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.ACCESS_NOTIFICATIONS, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, null, null, null, null, null, null, null, null, null, null, null, null, Manifest.permission.WAKE_LOCK, null, null, Manifest.permission.PACKAGE_USAGE_STATS, null, null, null, null, null, null, null, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ADD_VOICEMAIL, Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.USE_FINGERPRINT, Manifest.permission.BODY_SENSORS, Manifest.permission.READ_CELL_BROADCASTS, null, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, null, Manifest.permission.GET_ACCOUNTS, null, null, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.REQUEST_INSTALL_PACKAGES, null, Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE, Manifest.permission.ANSWER_PHONE_CALLS, null, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.REQUEST_DELETE_PACKAGES, Manifest.permission.BIND_ACCESSIBILITY_SERVICE, Manifest.permission.ACCEPT_HANDOVER, null, Manifest.permission.FOREGROUND_SERVICE, null};
    private static String[] sOpRestrictions = {UserManager.DISALLOW_SHARE_LOCATION, UserManager.DISALLOW_SHARE_LOCATION, UserManager.DISALLOW_SHARE_LOCATION, null, null, null, UserManager.DISALLOW_OUTGOING_CALLS, UserManager.DISALLOW_OUTGOING_CALLS, null, null, UserManager.DISALLOW_SHARE_LOCATION, null, null, null, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, null, UserManager.DISALLOW_SMS, null, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, null, UserManager.DISALLOW_CREATE_WINDOWS, null, UserManager.DISALLOW_CAMERA, "no_record_audio", null, null, null, null, null, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, null, UserManager.DISALLOW_SHARE_LOCATION, UserManager.DISALLOW_SHARE_LOCATION, null, UserManager.DISALLOW_UNMUTE_MICROPHONE, UserManager.DISALLOW_CREATE_WINDOWS, null, null, UserManager.DISALLOW_WALLPAPER, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, UserManager.DISALLOW_ADJUST_VOLUME, null, null, null, null, null, null, null, null, null, null, null, null, null};
    private static boolean[] sOpAllowSystemRestrictionBypass = {true, true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true};
    private static int[] sOpDefaultMode = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 2, 0, 0};
    private static boolean[] sOpDisableReset = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private static HashMap<String, Integer> sOpStrToOp = new HashMap<>();
    private static HashMap<String, Integer> sPermToOp = new HashMap<>();
    final ArrayMap<OnOpChangedListener, IAppOpsCallback> mModeWatchers = new ArrayMap<>();
    final ArrayMap<OnOpActiveChangedListener, IAppOpsActiveCallback> mActiveWatchers = new ArrayMap<>();

    /* loaded from: classes.dex */
    public interface OnOpActiveChangedListener {
        void onOpActiveChanged(int i, int i2, String str, boolean z);
    }

    /* loaded from: classes.dex */
    public interface OnOpChangedListener {
        void onOpChanged(String str, String str2);
    }

    static {
        int[] iArr;
        if (sOpToSwitch.length == 78) {
            if (sOpToString.length == 78) {
                if (sOpNames.length == 78) {
                    if (sOpPerms.length == 78) {
                        if (sOpDefaultMode.length == 78) {
                            if (sOpDisableReset.length == 78) {
                                if (sOpRestrictions.length == 78) {
                                    if (sOpAllowSystemRestrictionBypass.length != 78) {
                                        throw new IllegalStateException("sOpAllowSYstemRestrictionsBypass length " + sOpRestrictions.length + " should be 78");
                                    }
                                    for (int i = 0; i < 78; i++) {
                                        if (sOpToString[i] != null) {
                                            sOpStrToOp.put(sOpToString[i], Integer.valueOf(i));
                                        }
                                    }
                                    for (int op : RUNTIME_AND_APPOP_PERMISSIONS_OPS) {
                                        if (sOpPerms[op] != null) {
                                            sPermToOp.put(sOpPerms[op], Integer.valueOf(op));
                                        }
                                    }
                                    return;
                                }
                                throw new IllegalStateException("sOpRestrictions length " + sOpRestrictions.length + " should be 78");
                            }
                            throw new IllegalStateException("sOpDisableReset length " + sOpDisableReset.length + " should be 78");
                        }
                        throw new IllegalStateException("sOpDefaultMode length " + sOpDefaultMode.length + " should be 78");
                    }
                    throw new IllegalStateException("sOpPerms length " + sOpPerms.length + " should be 78");
                }
                throw new IllegalStateException("sOpNames length " + sOpNames.length + " should be 78");
            }
            throw new IllegalStateException("sOpToString length " + sOpToString.length + " should be 78");
        }
        throw new IllegalStateException("sOpToSwitch length " + sOpToSwitch.length + " should be 78");
    }

    private protected static int opToSwitch(int op) {
        return sOpToSwitch[op];
    }

    private protected static String opToName(int op) {
        if (op == -1) {
            return "NONE";
        }
        if (op < sOpNames.length) {
            return sOpNames[op];
        }
        return "Unknown(" + op + ")";
    }

    public static synchronized int strDebugOpToOp(String op) {
        for (int i = 0; i < sOpNames.length; i++) {
            if (sOpNames[i].equals(op)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown operation string: " + op);
    }

    private protected static String opToPermission(int op) {
        return sOpPerms[op];
    }

    public static synchronized String opToRestriction(int op) {
        return sOpRestrictions[op];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int permissionToOpCode(String permission) {
        Integer boxedOpCode = sPermToOp.get(permission);
        if (boxedOpCode != null) {
            return boxedOpCode.intValue();
        }
        return -1;
    }

    public static synchronized boolean opAllowSystemBypassRestriction(int op) {
        return sOpAllowSystemRestrictionBypass[op];
    }

    public static synchronized int opToDefaultMode(int op) {
        return sOpDefaultMode[op];
    }

    public static synchronized String modeToName(int mode) {
        if (mode >= 0 && mode < MODE_NAMES.length) {
            return MODE_NAMES[mode];
        }
        return "mode=" + mode;
    }

    public static synchronized boolean opAllowsReset(int op) {
        return !sOpDisableReset[op];
    }

    /* loaded from: classes.dex */
    public static class PackageOps implements Parcelable {
        private protected static final Parcelable.Creator<PackageOps> CREATOR = new Parcelable.Creator<PackageOps>() { // from class: android.app.AppOpsManager.PackageOps.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public PackageOps createFromParcel(Parcel source) {
                return new PackageOps(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public PackageOps[] newArray(int size) {
                return new PackageOps[size];
            }
        };
        private final List<OpEntry> mEntries;
        private final String mPackageName;
        private final int mUid;

        private protected PackageOps(String packageName, int uid, List<OpEntry> entries) {
            this.mPackageName = packageName;
            this.mUid = uid;
            this.mEntries = entries;
        }

        private protected String getPackageName() {
            return this.mPackageName;
        }

        private protected int getUid() {
            return this.mUid;
        }

        private protected List<OpEntry> getOps() {
            return this.mEntries;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mPackageName);
            dest.writeInt(this.mUid);
            dest.writeInt(this.mEntries.size());
            for (int i = 0; i < this.mEntries.size(); i++) {
                this.mEntries.get(i).writeToParcel(dest, flags);
            }
        }

        synchronized PackageOps(Parcel source) {
            this.mPackageName = source.readString();
            this.mUid = source.readInt();
            this.mEntries = new ArrayList();
            int N = source.readInt();
            for (int i = 0; i < N; i++) {
                this.mEntries.add(OpEntry.CREATOR.createFromParcel(source));
            }
        }
    }

    /* loaded from: classes.dex */
    public static class OpEntry implements Parcelable {
        public static final Parcelable.Creator<OpEntry> CREATOR = new Parcelable.Creator<OpEntry>() { // from class: android.app.AppOpsManager.OpEntry.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public OpEntry createFromParcel(Parcel source) {
                return new OpEntry(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public OpEntry[] newArray(int size) {
                return new OpEntry[size];
            }
        };
        private final int mDuration;
        private final int mMode;
        private final int mOp;
        private final String mProxyPackageName;
        private final int mProxyUid;
        private final long[] mRejectTimes;
        private final boolean mRunning;
        private final long[] mTimes;

        public synchronized OpEntry(int op, int mode, long time, long rejectTime, int duration, int proxyUid, String proxyPackage) {
            this.mOp = op;
            this.mMode = mode;
            this.mTimes = new long[6];
            this.mRejectTimes = new long[6];
            this.mTimes[0] = time;
            this.mRejectTimes[0] = rejectTime;
            this.mDuration = duration;
            this.mRunning = duration == -1;
            this.mProxyUid = proxyUid;
            this.mProxyPackageName = proxyPackage;
        }

        public synchronized OpEntry(int op, int mode, long[] times, long[] rejectTimes, int duration, boolean running, int proxyUid, String proxyPackage) {
            this.mOp = op;
            this.mMode = mode;
            this.mTimes = new long[6];
            this.mRejectTimes = new long[6];
            System.arraycopy(times, 0, this.mTimes, 0, 6);
            System.arraycopy(rejectTimes, 0, this.mRejectTimes, 0, 6);
            this.mDuration = duration;
            this.mRunning = running;
            this.mProxyUid = proxyUid;
            this.mProxyPackageName = proxyPackage;
        }

        public synchronized OpEntry(int op, int mode, long[] times, long[] rejectTimes, int duration, int proxyUid, String proxyPackage) {
            this(op, mode, times, rejectTimes, duration, duration == -1, proxyUid, proxyPackage);
        }

        private protected int getOp() {
            return this.mOp;
        }

        private protected int getMode() {
            return this.mMode;
        }

        private protected long getTime() {
            return AppOpsManager.maxTime(this.mTimes, 0, 6);
        }

        private protected long getLastAccessTime() {
            return AppOpsManager.maxTime(this.mTimes, 0, 6);
        }

        private protected long getLastAccessForegroundTime() {
            return AppOpsManager.maxTime(this.mTimes, 0, 3);
        }

        private protected long getLastAccessBackgroundTime() {
            return AppOpsManager.maxTime(this.mTimes, 3, 6);
        }

        public synchronized long getLastTimeFor(int uidState) {
            return this.mTimes[uidState];
        }

        private protected long getRejectTime() {
            return AppOpsManager.maxTime(this.mRejectTimes, 0, 6);
        }

        private protected long getLastRejectTime() {
            return AppOpsManager.maxTime(this.mRejectTimes, 0, 6);
        }

        private protected long getLastRejectForegroundTime() {
            return AppOpsManager.maxTime(this.mRejectTimes, 0, 3);
        }

        private protected long getLastRejectBackgroundTime() {
            return AppOpsManager.maxTime(this.mRejectTimes, 3, 6);
        }

        public synchronized long getLastRejectTimeFor(int uidState) {
            return this.mRejectTimes[uidState];
        }

        private protected boolean isRunning() {
            return this.mRunning;
        }

        private protected int getDuration() {
            return this.mDuration;
        }

        public synchronized int getProxyUid() {
            return this.mProxyUid;
        }

        public synchronized String getProxyPackageName() {
            return this.mProxyPackageName;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mOp);
            dest.writeInt(this.mMode);
            dest.writeLongArray(this.mTimes);
            dest.writeLongArray(this.mRejectTimes);
            dest.writeInt(this.mDuration);
            dest.writeBoolean(this.mRunning);
            dest.writeInt(this.mProxyUid);
            dest.writeString(this.mProxyPackageName);
        }

        synchronized OpEntry(Parcel source) {
            this.mOp = source.readInt();
            this.mMode = source.readInt();
            this.mTimes = source.createLongArray();
            this.mRejectTimes = source.createLongArray();
            this.mDuration = source.readInt();
            this.mRunning = source.readBoolean();
            this.mProxyUid = source.readInt();
            this.mProxyPackageName = source.readString();
        }
    }

    /* loaded from: classes.dex */
    public static class OnOpChangedInternalListener implements OnOpChangedListener {
        @Override // android.app.AppOpsManager.OnOpChangedListener
        public void onOpChanged(String op, String packageName) {
        }

        public synchronized void onOpChanged(int op, String packageName) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized AppOpsManager(Context context, IAppOpsService service) {
        this.mContext = context;
        this.mService = service;
    }

    private protected List<PackageOps> getPackagesForOps(int[] ops) {
        try {
            return this.mService.getPackagesForOps(ops);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected List<PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) {
        try {
            return this.mService.getOpsForPackage(uid, packageName, ops);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void setUidMode(int code, int uid, int mode) {
        try {
            this.mService.setUidMode(code, uid, mode);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setUidMode(String appOp, int uid, int mode) {
        try {
            this.mService.setUidMode(strOpToOp(appOp), uid, mode);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void setUserRestriction(int code, boolean restricted, IBinder token) {
        setUserRestriction(code, restricted, token, null);
    }

    public synchronized void setUserRestriction(int code, boolean restricted, IBinder token, String[] exceptionPackages) {
        setUserRestrictionForUser(code, restricted, token, exceptionPackages, this.mContext.getUserId());
    }

    public synchronized void setUserRestrictionForUser(int code, boolean restricted, IBinder token, String[] exceptionPackages, int userId) {
        try {
            this.mService.setUserRestriction(code, restricted, token, userId, exceptionPackages);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setMode(int code, int uid, String packageName, int mode) {
        try {
            this.mService.setMode(code, uid, packageName, mode);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setMode(String op, int uid, String packageName, int mode) {
        try {
            this.mService.setMode(strOpToOp(op), uid, packageName, mode);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected void setRestriction(int code, int usage, int mode, String[] exceptionPackages) {
        try {
            int uid = Binder.getCallingUid();
            this.mService.setAudioRestriction(code, usage, uid, mode, exceptionPackages);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected void resetAllModes() {
        try {
            this.mService.resetAllModes(this.mContext.getUserId(), null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static String permissionToOp(String permission) {
        Integer opCode = sPermToOp.get(permission);
        if (opCode == null) {
            return null;
        }
        return sOpToString[opCode.intValue()];
    }

    public void startWatchingMode(String op, String packageName, OnOpChangedListener callback) {
        startWatchingMode(strOpToOp(op), packageName, callback);
    }

    private protected void startWatchingMode(String op, String packageName, int flags, OnOpChangedListener callback) {
        startWatchingMode(strOpToOp(op), packageName, flags, callback);
    }

    public synchronized void startWatchingMode(int op, String packageName, OnOpChangedListener callback) {
        startWatchingMode(op, packageName, 0, callback);
    }

    public synchronized void startWatchingMode(int op, String packageName, int flags, final OnOpChangedListener callback) {
        synchronized (this.mModeWatchers) {
            IAppOpsCallback cb = this.mModeWatchers.get(callback);
            if (cb == null) {
                cb = new IAppOpsCallback.Stub() { // from class: android.app.AppOpsManager.1
                    @Override // com.android.internal.app.IAppOpsCallback
                    public void opChanged(int op2, int uid, String packageName2) {
                        if (callback instanceof OnOpChangedInternalListener) {
                            ((OnOpChangedInternalListener) callback).onOpChanged(op2, packageName2);
                        }
                        if (AppOpsManager.sOpToString[op2] != null) {
                            callback.onOpChanged(AppOpsManager.sOpToString[op2], packageName2);
                        }
                    }
                };
                this.mModeWatchers.put(callback, cb);
            }
            try {
                this.mService.startWatchingModeWithFlags(op, packageName, flags, cb);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void stopWatchingMode(OnOpChangedListener callback) {
        synchronized (this.mModeWatchers) {
            IAppOpsCallback cb = this.mModeWatchers.get(callback);
            if (cb != null) {
                try {
                    this.mService.stopWatchingMode(cb);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    public void startWatchingActive(int[] ops, final OnOpActiveChangedListener callback) {
        Preconditions.checkNotNull(ops, "ops cannot be null");
        Preconditions.checkNotNull(callback, "callback cannot be null");
        synchronized (this.mActiveWatchers) {
            if (this.mActiveWatchers.get(callback) != null) {
                return;
            }
            IAppOpsActiveCallback cb = new IAppOpsActiveCallback.Stub() { // from class: android.app.AppOpsManager.2
                @Override // com.android.internal.app.IAppOpsActiveCallback
                public void opActiveChanged(int op, int uid, String packageName, boolean active) {
                    callback.onOpActiveChanged(op, uid, packageName, active);
                }
            };
            this.mActiveWatchers.put(callback, cb);
            try {
                this.mService.startWatchingActive(ops, cb);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void stopWatchingActive(OnOpActiveChangedListener callback) {
        synchronized (this.mActiveWatchers) {
            IAppOpsActiveCallback cb = this.mActiveWatchers.get(callback);
            if (cb != null) {
                try {
                    this.mService.stopWatchingActive(cb);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    private synchronized String buildSecurityExceptionMsg(int op, int uid, String packageName) {
        return packageName + " from uid " + uid + " not allowed to perform " + sOpNames[op];
    }

    private protected static int strOpToOp(String op) {
        Integer val = sOpStrToOp.get(op);
        if (val == null) {
            throw new IllegalArgumentException("Unknown operation string: " + op);
        }
        return val.intValue();
    }

    public int checkOp(String op, int uid, String packageName) {
        return checkOp(strOpToOp(op), uid, packageName);
    }

    public int checkOpNoThrow(String op, int uid, String packageName) {
        return checkOpNoThrow(strOpToOp(op), uid, packageName);
    }

    private protected int unsafeCheckOpRaw(String op, int uid, String packageName) {
        try {
            return this.mService.checkOperation(strOpToOp(op), uid, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int noteOp(String op, int uid, String packageName) {
        return noteOp(strOpToOp(op), uid, packageName);
    }

    public int noteOpNoThrow(String op, int uid, String packageName) {
        return noteOpNoThrow(strOpToOp(op), uid, packageName);
    }

    public int noteProxyOp(String op, String proxiedPackageName) {
        return noteProxyOp(strOpToOp(op), proxiedPackageName);
    }

    public int noteProxyOpNoThrow(String op, String proxiedPackageName) {
        return noteProxyOpNoThrow(strOpToOp(op), proxiedPackageName);
    }

    public int startOp(String op, int uid, String packageName) {
        return startOp(strOpToOp(op), uid, packageName);
    }

    public int startOpNoThrow(String op, int uid, String packageName) {
        return startOpNoThrow(strOpToOp(op), uid, packageName);
    }

    public void finishOp(String op, int uid, String packageName) {
        finishOp(strOpToOp(op), uid, packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkOp(int op, int uid, String packageName) {
        try {
            int mode = this.mService.checkOperation(op, uid, packageName);
            if (mode == 2) {
                throw new SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
            }
            return mode;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkOpNoThrow(int op, int uid, String packageName) {
        try {
            int mode = this.mService.checkOperation(op, uid, packageName);
            if (mode == 4) {
                return 0;
            }
            return mode;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void checkPackage(int uid, String packageName) {
        try {
            if (this.mService.checkPackage(uid, packageName) != 0) {
                throw new SecurityException("Package " + packageName + " does not belong to " + uid);
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized int checkAudioOp(int op, int stream, int uid, String packageName) {
        try {
            int mode = this.mService.checkAudioOperation(op, stream, uid, packageName);
            if (mode == 2) {
                throw new SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
            }
            return mode;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized int checkAudioOpNoThrow(int op, int stream, int uid, String packageName) {
        try {
            return this.mService.checkAudioOperation(op, stream, uid, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int noteOp(int op, int uid, String packageName) {
        int mode = noteOpNoThrow(op, uid, packageName);
        if (mode == 2) {
            throw new SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
        }
        return mode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int noteProxyOp(int op, String proxiedPackageName) {
        int mode = noteProxyOpNoThrow(op, proxiedPackageName);
        if (mode == 2) {
            throw new SecurityException("Proxy package " + this.mContext.getOpPackageName() + " from uid " + Process.myUid() + " or calling package " + proxiedPackageName + " from uid " + Binder.getCallingUid() + " not allowed to perform " + sOpNames[op]);
        }
        return mode;
    }

    public synchronized int noteProxyOpNoThrow(int op, String proxiedPackageName) {
        try {
            return this.mService.noteProxyOperation(op, this.mContext.getOpPackageName(), Binder.getCallingUid(), proxiedPackageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int noteOpNoThrow(int op, int uid, String packageName) {
        try {
            return this.mService.noteOperation(op, uid, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected int noteOp(int op) {
        return noteOp(op, Process.myUid(), this.mContext.getOpPackageName());
    }

    private protected static IBinder getToken(IAppOpsService service) {
        synchronized (AppOpsManager.class) {
            if (sToken != null) {
                return sToken;
            }
            try {
                sToken = service.getToken(new Binder());
                return sToken;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public synchronized int startOp(int op) {
        return startOp(op, Process.myUid(), this.mContext.getOpPackageName());
    }

    public synchronized int startOp(int op, int uid, String packageName) {
        return startOp(op, uid, packageName, false);
    }

    public synchronized int startOp(int op, int uid, String packageName, boolean startIfModeDefault) {
        int mode = startOpNoThrow(op, uid, packageName, startIfModeDefault);
        if (mode == 2) {
            throw new SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
        }
        return mode;
    }

    public synchronized int startOpNoThrow(int op, int uid, String packageName) {
        return startOpNoThrow(op, uid, packageName, false);
    }

    public synchronized int startOpNoThrow(int op, int uid, String packageName, boolean startIfModeDefault) {
        try {
            return this.mService.startOperation(getToken(this.mService), op, uid, packageName, startIfModeDefault);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void finishOp(int op, int uid, String packageName) {
        try {
            this.mService.finishOperation(getToken(this.mService), op, uid, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void finishOp(int op) {
        finishOp(op, Process.myUid(), this.mContext.getOpPackageName());
    }

    public boolean isOperationActive(int code, int uid, String packageName) {
        try {
            return this.mService.isOperationActive(code, uid, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public static String[] getOpStrs() {
        return (String[]) Arrays.copyOf(sOpToString, sOpToString.length);
    }

    public static synchronized long maxTime(long[] times, int start, int end) {
        long time = 0;
        for (int i = start; i < end; i++) {
            if (times[i] > time) {
                time = times[i];
            }
        }
        return time;
    }
}
