package android.app;

import android.Manifest;
import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.ArrayMap;
import android.util.LongSparseArray;
import android.util.LongSparseLongArray;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.Immutable;
import com.android.internal.app.IAppOpsActiveCallback;
import com.android.internal.app.IAppOpsCallback;
import com.android.internal.app.IAppOpsNotedCallback;
import com.android.internal.app.IAppOpsService;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Supplier;

/* loaded from: classes.dex */
public class AppOpsManager {
    private static final String DEBUG_LOGGING_ENABLE_PROP = "appops.logging_enabled";
    private static final String DEBUG_LOGGING_OPS_PROP = "appops.logging_ops";
    private static final String DEBUG_LOGGING_PACKAGES_PROP = "appops.logging_packages";
    private static final String DEBUG_LOGGING_TAG = "AppOpsManager";
    private static final int FLAGS_MASK = -1;
    public static final int HISTORICAL_MODE_DISABLED = 0;
    public static final int HISTORICAL_MODE_ENABLED_ACTIVE = 1;
    public static final int HISTORICAL_MODE_ENABLED_PASSIVE = 2;
    public static final String KEY_HISTORICAL_OPS = "historical_ops";
    public static final int MAX_PRIORITY_UID_STATE = 100;
    public static final int MIN_PRIORITY_UID_STATE = 700;
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_ERRORED = 2;
    public static final int MODE_FOREGROUND = 4;
    public static final int MODE_IGNORED = 1;
    @UnsupportedAppUsage
    public static final int OP_ACCEPT_HANDOVER = 74;
    public static final int OP_ACCESS_ACCESSIBILITY = 88;
    public static final int OP_ACCESS_MEDIA_LOCATION = 90;
    @UnsupportedAppUsage
    public static final int OP_ACCESS_NOTIFICATIONS = 25;
    @UnsupportedAppUsage
    public static final int OP_ACTIVATE_VPN = 47;
    public static final int OP_ACTIVITY_RECOGNITION = 79;
    @UnsupportedAppUsage
    public static final int OP_ADD_VOICEMAIL = 52;
    @UnsupportedAppUsage
    public static final int OP_ANSWER_PHONE_CALLS = 69;
    @UnsupportedAppUsage
    public static final int OP_ASSIST_SCREENSHOT = 50;
    @UnsupportedAppUsage
    public static final int OP_ASSIST_STRUCTURE = 49;
    @UnsupportedAppUsage
    public static final int OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    @UnsupportedAppUsage
    public static final int OP_AUDIO_ALARM_VOLUME = 37;
    @UnsupportedAppUsage
    public static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;
    @UnsupportedAppUsage
    public static final int OP_AUDIO_MASTER_VOLUME = 33;
    @UnsupportedAppUsage
    public static final int OP_AUDIO_MEDIA_VOLUME = 36;
    @UnsupportedAppUsage
    public static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;
    @UnsupportedAppUsage
    public static final int OP_AUDIO_RING_VOLUME = 35;
    @UnsupportedAppUsage
    public static final int OP_AUDIO_VOICE_VOLUME = 34;
    @UnsupportedAppUsage
    public static final int OP_BIND_ACCESSIBILITY_SERVICE = 73;
    @UnsupportedAppUsage
    public static final int OP_BLUETOOTH_SCAN = 77;
    @UnsupportedAppUsage
    public static final int OP_BODY_SENSORS = 56;
    @UnsupportedAppUsage
    public static final int OP_CALL_PHONE = 13;
    @UnsupportedAppUsage
    public static final int OP_CAMERA = 26;
    @UnsupportedAppUsage
    public static final int OP_CHANGE_WIFI_STATE = 71;
    public static final int OP_COARSE_LOCATION = 0;
    @UnsupportedAppUsage
    public static final int OP_FINE_LOCATION = 1;
    @SystemApi
    public static final int OP_FLAGS_ALL = 31;
    @SystemApi
    public static final int OP_FLAGS_ALL_TRUSTED = 13;
    @SystemApi
    public static final int OP_FLAG_SELF = 1;
    @SystemApi
    public static final int OP_FLAG_TRUSTED_PROXIED = 8;
    @SystemApi
    public static final int OP_FLAG_TRUSTED_PROXY = 2;
    @SystemApi
    public static final int OP_FLAG_UNTRUSTED_PROXIED = 16;
    @SystemApi
    public static final int OP_FLAG_UNTRUSTED_PROXY = 4;
    @UnsupportedAppUsage
    public static final int OP_GET_ACCOUNTS = 62;
    @UnsupportedAppUsage
    public static final int OP_GET_USAGE_STATS = 43;
    @UnsupportedAppUsage
    public static final int OP_GPS = 2;
    @UnsupportedAppUsage
    public static final int OP_INSTANT_APP_START_FOREGROUND = 68;
    public static final int OP_LEGACY_STORAGE = 87;
    @UnsupportedAppUsage
    public static final int OP_MANAGE_IPSEC_TUNNELS = 75;
    @UnsupportedAppUsage
    public static final int OP_MOCK_LOCATION = 58;
    @UnsupportedAppUsage
    public static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;
    @UnsupportedAppUsage
    public static final int OP_MONITOR_LOCATION = 41;
    @UnsupportedAppUsage
    public static final int OP_MUTE_MICROPHONE = 44;
    @UnsupportedAppUsage
    public static final int OP_NEIGHBORING_CELLS = 12;
    @UnsupportedAppUsage
    public static final int OP_NONE = -1;
    @UnsupportedAppUsage
    public static final int OP_PICTURE_IN_PICTURE = 67;
    @UnsupportedAppUsage
    public static final int OP_PLAY_AUDIO = 28;
    @UnsupportedAppUsage
    public static final int OP_POST_NOTIFICATION = 11;
    @UnsupportedAppUsage
    public static final int OP_PROCESS_OUTGOING_CALLS = 54;
    @UnsupportedAppUsage
    public static final int OP_PROJECT_MEDIA = 46;
    @UnsupportedAppUsage
    public static final int OP_READ_CALENDAR = 8;
    @UnsupportedAppUsage
    public static final int OP_READ_CALL_LOG = 6;
    @UnsupportedAppUsage
    public static final int OP_READ_CELL_BROADCASTS = 57;
    @UnsupportedAppUsage
    public static final int OP_READ_CLIPBOARD = 29;
    @UnsupportedAppUsage
    public static final int OP_READ_CONTACTS = 4;
    public static final int OP_READ_DEVICE_IDENTIFIERS = 89;
    @UnsupportedAppUsage
    public static final int OP_READ_EXTERNAL_STORAGE = 59;
    @UnsupportedAppUsage
    public static final int OP_READ_ICC_SMS = 21;
    public static final int OP_READ_MEDIA_AUDIO = 81;
    public static final int OP_READ_MEDIA_IMAGES = 85;
    public static final int OP_READ_MEDIA_VIDEO = 83;
    @UnsupportedAppUsage
    public static final int OP_READ_PHONE_NUMBERS = 65;
    @UnsupportedAppUsage
    public static final int OP_READ_PHONE_STATE = 51;
    @UnsupportedAppUsage
    public static final int OP_READ_SMS = 14;
    @UnsupportedAppUsage
    public static final int OP_RECEIVE_EMERGECY_SMS = 17;
    @UnsupportedAppUsage
    public static final int OP_RECEIVE_MMS = 18;
    @UnsupportedAppUsage
    public static final int OP_RECEIVE_SMS = 16;
    @UnsupportedAppUsage
    public static final int OP_RECEIVE_WAP_PUSH = 19;
    public static final int OP_RECORD_AUDIO = 27;
    @UnsupportedAppUsage
    public static final int OP_REQUEST_DELETE_PACKAGES = 72;
    @UnsupportedAppUsage
    public static final int OP_REQUEST_INSTALL_PACKAGES = 66;
    @UnsupportedAppUsage
    public static final int OP_RUN_ANY_IN_BACKGROUND = 70;
    @UnsupportedAppUsage
    public static final int OP_RUN_IN_BACKGROUND = 63;
    @UnsupportedAppUsage
    public static final int OP_SEND_SMS = 20;
    public static final int OP_SMS_FINANCIAL_TRANSACTIONS = 80;
    public static final int OP_START_FOREGROUND = 76;
    public static final int OP_SYSTEM_ALERT_WINDOW = 24;
    @UnsupportedAppUsage
    public static final int OP_TAKE_AUDIO_FOCUS = 32;
    @UnsupportedAppUsage
    public static final int OP_TAKE_MEDIA_BUTTONS = 31;
    @UnsupportedAppUsage
    public static final int OP_TOAST_WINDOW = 45;
    @UnsupportedAppUsage
    public static final int OP_TURN_SCREEN_ON = 61;
    public static final int OP_USE_BIOMETRIC = 78;
    @UnsupportedAppUsage
    public static final int OP_USE_FINGERPRINT = 55;
    @UnsupportedAppUsage
    public static final int OP_USE_SIP = 53;
    @UnsupportedAppUsage
    public static final int OP_VIBRATE = 3;
    @UnsupportedAppUsage
    public static final int OP_WAKE_LOCK = 40;
    @UnsupportedAppUsage
    public static final int OP_WIFI_SCAN = 10;
    @UnsupportedAppUsage
    public static final int OP_WRITE_CALENDAR = 9;
    @UnsupportedAppUsage
    public static final int OP_WRITE_CALL_LOG = 7;
    @UnsupportedAppUsage
    public static final int OP_WRITE_CLIPBOARD = 30;
    @UnsupportedAppUsage
    public static final int OP_WRITE_CONTACTS = 5;
    @UnsupportedAppUsage
    public static final int OP_WRITE_EXTERNAL_STORAGE = 60;
    @UnsupportedAppUsage
    public static final int OP_WRITE_ICC_SMS = 22;
    public static final int OP_WRITE_MEDIA_AUDIO = 82;
    public static final int OP_WRITE_MEDIA_IMAGES = 86;
    public static final int OP_WRITE_MEDIA_VIDEO = 84;
    @UnsupportedAppUsage
    public static final int OP_WRITE_SETTINGS = 23;
    @UnsupportedAppUsage
    public static final int OP_WRITE_SMS = 15;
    @UnsupportedAppUsage
    public static final int OP_WRITE_WALLPAPER = 48;
    @SystemApi
    public static final int UID_STATE_BACKGROUND = 600;
    @SystemApi
    public static final int UID_STATE_CACHED = 700;
    @SystemApi
    public static final int UID_STATE_FOREGROUND = 500;
    @SystemApi
    public static final int UID_STATE_FOREGROUND_SERVICE = 400;
    @SystemApi
    public static final int UID_STATE_FOREGROUND_SERVICE_LOCATION = 300;
    public static final int UID_STATE_MAX_LAST_NON_RESTRICTED = 400;
    private static final int UID_STATE_OFFSET = 31;
    @SystemApi
    public static final int UID_STATE_PERSISTENT = 100;
    @SystemApi
    public static final int UID_STATE_TOP = 200;
    public static final int WATCH_FOREGROUND_CHANGES = 1;
    @UnsupportedAppUsage
    public static final int _NUM_OP = 91;
    static IBinder sToken;
    final Context mContext;
    @UnsupportedAppUsage
    final IAppOpsService mService;
    public static final String[] MODE_NAMES = {"allow", "ignore", "deny", "default", "foreground"};
    public static final int[] UID_STATES = {100, 200, 300, 400, 500, 600, 700};
    private static final int[] RUNTIME_AND_APPOP_PERMISSIONS_OPS = {4, 5, 62, 8, 9, 20, 16, 14, 19, 18, 57, 59, 60, 90, 0, 1, 51, 65, 13, 6, 7, 52, 53, 54, 69, 74, 27, 26, 56, 79, 81, 82, 83, 84, 85, 86, 25, 24, 23, 66, 76, 80};
    private static int[] sOpToSwitch = {0, 0, 0, 3, 4, 5, 6, 7, 8, 9, 0, 11, 0, 13, 14, 15, 16, 16, 18, 19, 20, 14, 15, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 0, 0, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 0, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90};
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
    public static final String OPSTR_USE_BIOMETRIC = "android:use_biometric";
    public static final String OPSTR_ACTIVITY_RECOGNITION = "android:activity_recognition";
    public static final String OPSTR_SMS_FINANCIAL_TRANSACTIONS = "android:sms_financial_transactions";
    public static final String OPSTR_READ_MEDIA_AUDIO = "android:read_media_audio";
    public static final String OPSTR_WRITE_MEDIA_AUDIO = "android:write_media_audio";
    public static final String OPSTR_READ_MEDIA_VIDEO = "android:read_media_video";
    public static final String OPSTR_WRITE_MEDIA_VIDEO = "android:write_media_video";
    public static final String OPSTR_READ_MEDIA_IMAGES = "android:read_media_images";
    public static final String OPSTR_WRITE_MEDIA_IMAGES = "android:write_media_images";
    @SystemApi
    public static final String OPSTR_LEGACY_STORAGE = "android:legacy_storage";
    @SystemApi
    public static final String OPSTR_ACCESS_ACCESSIBILITY = "android:access_accessibility";
    public static final String OPSTR_READ_DEVICE_IDENTIFIERS = "android:read_device_identifiers";
    public static final String OPSTR_ACCESS_MEDIA_LOCATION = "android:access_media_location";
    private static String[] sOpToString = {OPSTR_COARSE_LOCATION, OPSTR_FINE_LOCATION, OPSTR_GPS, OPSTR_VIBRATE, OPSTR_READ_CONTACTS, OPSTR_WRITE_CONTACTS, OPSTR_READ_CALL_LOG, OPSTR_WRITE_CALL_LOG, OPSTR_READ_CALENDAR, OPSTR_WRITE_CALENDAR, OPSTR_WIFI_SCAN, OPSTR_POST_NOTIFICATION, OPSTR_NEIGHBORING_CELLS, OPSTR_CALL_PHONE, OPSTR_READ_SMS, OPSTR_WRITE_SMS, OPSTR_RECEIVE_SMS, OPSTR_RECEIVE_EMERGENCY_BROADCAST, OPSTR_RECEIVE_MMS, OPSTR_RECEIVE_WAP_PUSH, OPSTR_SEND_SMS, OPSTR_READ_ICC_SMS, OPSTR_WRITE_ICC_SMS, OPSTR_WRITE_SETTINGS, OPSTR_SYSTEM_ALERT_WINDOW, OPSTR_ACCESS_NOTIFICATIONS, OPSTR_CAMERA, OPSTR_RECORD_AUDIO, OPSTR_PLAY_AUDIO, OPSTR_READ_CLIPBOARD, OPSTR_WRITE_CLIPBOARD, OPSTR_TAKE_MEDIA_BUTTONS, OPSTR_TAKE_AUDIO_FOCUS, OPSTR_AUDIO_MASTER_VOLUME, OPSTR_AUDIO_VOICE_VOLUME, OPSTR_AUDIO_RING_VOLUME, OPSTR_AUDIO_MEDIA_VOLUME, OPSTR_AUDIO_ALARM_VOLUME, OPSTR_AUDIO_NOTIFICATION_VOLUME, OPSTR_AUDIO_BLUETOOTH_VOLUME, OPSTR_WAKE_LOCK, OPSTR_MONITOR_LOCATION, OPSTR_MONITOR_HIGH_POWER_LOCATION, OPSTR_GET_USAGE_STATS, OPSTR_MUTE_MICROPHONE, OPSTR_TOAST_WINDOW, OPSTR_PROJECT_MEDIA, OPSTR_ACTIVATE_VPN, OPSTR_WRITE_WALLPAPER, OPSTR_ASSIST_STRUCTURE, OPSTR_ASSIST_SCREENSHOT, OPSTR_READ_PHONE_STATE, OPSTR_ADD_VOICEMAIL, OPSTR_USE_SIP, OPSTR_PROCESS_OUTGOING_CALLS, OPSTR_USE_FINGERPRINT, OPSTR_BODY_SENSORS, OPSTR_READ_CELL_BROADCASTS, OPSTR_MOCK_LOCATION, OPSTR_READ_EXTERNAL_STORAGE, OPSTR_WRITE_EXTERNAL_STORAGE, OPSTR_TURN_SCREEN_ON, OPSTR_GET_ACCOUNTS, OPSTR_RUN_IN_BACKGROUND, OPSTR_AUDIO_ACCESSIBILITY_VOLUME, OPSTR_READ_PHONE_NUMBERS, OPSTR_REQUEST_INSTALL_PACKAGES, OPSTR_PICTURE_IN_PICTURE, OPSTR_INSTANT_APP_START_FOREGROUND, OPSTR_ANSWER_PHONE_CALLS, OPSTR_RUN_ANY_IN_BACKGROUND, OPSTR_CHANGE_WIFI_STATE, OPSTR_REQUEST_DELETE_PACKAGES, OPSTR_BIND_ACCESSIBILITY_SERVICE, OPSTR_ACCEPT_HANDOVER, OPSTR_MANAGE_IPSEC_TUNNELS, OPSTR_START_FOREGROUND, OPSTR_BLUETOOTH_SCAN, OPSTR_USE_BIOMETRIC, OPSTR_ACTIVITY_RECOGNITION, OPSTR_SMS_FINANCIAL_TRANSACTIONS, OPSTR_READ_MEDIA_AUDIO, OPSTR_WRITE_MEDIA_AUDIO, OPSTR_READ_MEDIA_VIDEO, OPSTR_WRITE_MEDIA_VIDEO, OPSTR_READ_MEDIA_IMAGES, OPSTR_WRITE_MEDIA_IMAGES, OPSTR_LEGACY_STORAGE, OPSTR_ACCESS_ACCESSIBILITY, OPSTR_READ_DEVICE_IDENTIFIERS, OPSTR_ACCESS_MEDIA_LOCATION};
    private static String[] sOpNames = {"COARSE_LOCATION", "FINE_LOCATION", "GPS", "VIBRATE", "READ_CONTACTS", "WRITE_CONTACTS", "READ_CALL_LOG", "WRITE_CALL_LOG", "READ_CALENDAR", "WRITE_CALENDAR", "WIFI_SCAN", "POST_NOTIFICATION", "NEIGHBORING_CELLS", "CALL_PHONE", "READ_SMS", "WRITE_SMS", "RECEIVE_SMS", "RECEIVE_EMERGECY_SMS", "RECEIVE_MMS", "RECEIVE_WAP_PUSH", "SEND_SMS", "READ_ICC_SMS", "WRITE_ICC_SMS", "WRITE_SETTINGS", "SYSTEM_ALERT_WINDOW", "ACCESS_NOTIFICATIONS", "CAMERA", "RECORD_AUDIO", "PLAY_AUDIO", "READ_CLIPBOARD", "WRITE_CLIPBOARD", "TAKE_MEDIA_BUTTONS", "TAKE_AUDIO_FOCUS", "AUDIO_MASTER_VOLUME", "AUDIO_VOICE_VOLUME", "AUDIO_RING_VOLUME", "AUDIO_MEDIA_VOLUME", "AUDIO_ALARM_VOLUME", "AUDIO_NOTIFICATION_VOLUME", "AUDIO_BLUETOOTH_VOLUME", "WAKE_LOCK", "MONITOR_LOCATION", "MONITOR_HIGH_POWER_LOCATION", "GET_USAGE_STATS", "MUTE_MICROPHONE", "TOAST_WINDOW", "PROJECT_MEDIA", "ACTIVATE_VPN", "WRITE_WALLPAPER", "ASSIST_STRUCTURE", "ASSIST_SCREENSHOT", "READ_PHONE_STATE", "ADD_VOICEMAIL", "USE_SIP", "PROCESS_OUTGOING_CALLS", "USE_FINGERPRINT", "BODY_SENSORS", "READ_CELL_BROADCASTS", "MOCK_LOCATION", "READ_EXTERNAL_STORAGE", "WRITE_EXTERNAL_STORAGE", "TURN_ON_SCREEN", "GET_ACCOUNTS", "RUN_IN_BACKGROUND", "AUDIO_ACCESSIBILITY_VOLUME", "READ_PHONE_NUMBERS", "REQUEST_INSTALL_PACKAGES", "PICTURE_IN_PICTURE", "INSTANT_APP_START_FOREGROUND", "ANSWER_PHONE_CALLS", "RUN_ANY_IN_BACKGROUND", "CHANGE_WIFI_STATE", "REQUEST_DELETE_PACKAGES", "BIND_ACCESSIBILITY_SERVICE", "ACCEPT_HANDOVER", "MANAGE_IPSEC_TUNNELS", "START_FOREGROUND", "BLUETOOTH_SCAN", "USE_BIOMETRIC", "ACTIVITY_RECOGNITION", "SMS_FINANCIAL_TRANSACTIONS", "READ_MEDIA_AUDIO", "WRITE_MEDIA_AUDIO", "READ_MEDIA_VIDEO", "WRITE_MEDIA_VIDEO", "READ_MEDIA_IMAGES", "WRITE_MEDIA_IMAGES", "LEGACY_STORAGE", "ACCESS_ACCESSIBILITY", "READ_DEVICE_IDENTIFIERS", "ACCESS_MEDIA_LOCATION"};
    @UnsupportedAppUsage
    private static String[] sOpPerms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, null, Manifest.permission.VIBRATE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.ACCESS_WIFI_STATE, null, null, Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS, null, Manifest.permission.RECEIVE_SMS, Manifest.permission.RECEIVE_EMERGENCY_BROADCAST, Manifest.permission.RECEIVE_MMS, Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, null, Manifest.permission.WRITE_SETTINGS, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.ACCESS_NOTIFICATIONS, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, null, null, null, null, null, null, null, null, null, null, null, null, Manifest.permission.WAKE_LOCK, null, null, Manifest.permission.PACKAGE_USAGE_STATS, null, null, null, null, null, null, null, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ADD_VOICEMAIL, Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.USE_FINGERPRINT, Manifest.permission.BODY_SENSORS, Manifest.permission.READ_CELL_BROADCASTS, null, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, null, Manifest.permission.GET_ACCOUNTS, null, null, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.REQUEST_INSTALL_PACKAGES, null, Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE, Manifest.permission.ANSWER_PHONE_CALLS, null, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.REQUEST_DELETE_PACKAGES, Manifest.permission.BIND_ACCESSIBILITY_SERVICE, Manifest.permission.ACCEPT_HANDOVER, null, Manifest.permission.FOREGROUND_SERVICE, null, Manifest.permission.USE_BIOMETRIC, Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.SMS_FINANCIAL_TRANSACTIONS, null, null, null, null, null, null, null, null, null, Manifest.permission.ACCESS_MEDIA_LOCATION};
    private static String[] sOpRestrictions = {UserManager.DISALLOW_SHARE_LOCATION, UserManager.DISALLOW_SHARE_LOCATION, UserManager.DISALLOW_SHARE_LOCATION, null, null, null, UserManager.DISALLOW_OUTGOING_CALLS, UserManager.DISALLOW_OUTGOING_CALLS, null, null, UserManager.DISALLOW_SHARE_LOCATION, null, null, null, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, null, UserManager.DISALLOW_SMS, null, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, UserManager.DISALLOW_SMS, null, UserManager.DISALLOW_CREATE_WINDOWS, null, UserManager.DISALLOW_CAMERA, UserManager.DISALLOW_RECORD_AUDIO, null, null, null, null, null, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, UserManager.DISALLOW_ADJUST_VOLUME, null, UserManager.DISALLOW_SHARE_LOCATION, UserManager.DISALLOW_SHARE_LOCATION, null, UserManager.DISALLOW_UNMUTE_MICROPHONE, UserManager.DISALLOW_CREATE_WINDOWS, null, null, UserManager.DISALLOW_WALLPAPER, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, UserManager.DISALLOW_ADJUST_VOLUME, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, UserManager.DISALLOW_SMS, null, null, null, null, null, null, null, null, null, null};
    private static boolean[] sOpAllowSystemRestrictionBypass = {true, true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private static int[] sOpDefaultMode = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, getSystemAlertWindowDefault(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0, 2, 0, 2, 0, 2, 3, 0, 2, 0};
    private static boolean[] sOpDisableReset = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private static HashMap<String, Integer> sOpStrToOp = new HashMap<>();
    private static HashMap<String, Integer> sPermToOp = new HashMap<>();
    @GuardedBy({"mModeWatchers"})
    private final ArrayMap<OnOpChangedListener, IAppOpsCallback> mModeWatchers = new ArrayMap<>();
    @GuardedBy({"mActiveWatchers"})
    private final ArrayMap<OnOpActiveChangedListener, IAppOpsActiveCallback> mActiveWatchers = new ArrayMap<>();
    @GuardedBy({"mNotedWatchers"})
    private final ArrayMap<OnOpNotedListener, IAppOpsNotedCallback> mNotedWatchers = new ArrayMap<>();

    @Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface DataBucketKey {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface HistoricalMode {
    }

    /* loaded from: classes.dex */
    public interface HistoricalOpsVisitor {
        void visitHistoricalOp(HistoricalOp historicalOp);

        void visitHistoricalOps(HistoricalOps historicalOps);

        void visitHistoricalPackageOps(HistoricalPackageOps historicalPackageOps);

        void visitHistoricalUidOps(HistoricalUidOps historicalUidOps);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Mode {
    }

    /* loaded from: classes.dex */
    public interface OnOpActiveChangedListener {
        void onOpActiveChanged(int i, int i2, String str, boolean z);
    }

    /* loaded from: classes.dex */
    public interface OnOpChangedListener {
        void onOpChanged(String str, String str2);
    }

    /* loaded from: classes.dex */
    public interface OnOpNotedListener {
        void onOpNoted(int i, int i2, String str, int i3);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface OpFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface UidState {
    }

    static {
        int[] iArr;
        if (sOpToSwitch.length == 91) {
            if (sOpToString.length == 91) {
                if (sOpNames.length == 91) {
                    if (sOpPerms.length == 91) {
                        if (sOpDefaultMode.length == 91) {
                            if (sOpDisableReset.length == 91) {
                                if (sOpRestrictions.length == 91) {
                                    if (sOpAllowSystemRestrictionBypass.length != 91) {
                                        throw new IllegalStateException("sOpAllowSYstemRestrictionsBypass length " + sOpRestrictions.length + " should be 91");
                                    }
                                    for (int i = 0; i < 91; i++) {
                                        String[] strArr = sOpToString;
                                        if (strArr[i] != null) {
                                            sOpStrToOp.put(strArr[i], Integer.valueOf(i));
                                        }
                                    }
                                    for (int op : RUNTIME_AND_APPOP_PERMISSIONS_OPS) {
                                        String[] strArr2 = sOpPerms;
                                        if (strArr2[op] != null) {
                                            sPermToOp.put(strArr2[op], Integer.valueOf(op));
                                        }
                                    }
                                    return;
                                }
                                throw new IllegalStateException("sOpRestrictions length " + sOpRestrictions.length + " should be 91");
                            }
                            throw new IllegalStateException("sOpDisableReset length " + sOpDisableReset.length + " should be 91");
                        }
                        throw new IllegalStateException("sOpDefaultMode length " + sOpDefaultMode.length + " should be 91");
                    }
                    throw new IllegalStateException("sOpPerms length " + sOpPerms.length + " should be 91");
                }
                throw new IllegalStateException("sOpNames length " + sOpNames.length + " should be 91");
            }
            throw new IllegalStateException("sOpToString length " + sOpToString.length + " should be 91");
        }
        throw new IllegalStateException("sOpToSwitch length " + sOpToSwitch.length + " should be 91");
    }

    public static int resolveFirstUnrestrictedUidState(int op) {
        if (op == 0 || op == 1 || op == 41 || op == 42) {
            return 300;
        }
        return 400;
    }

    public static int resolveLastRestrictedUidState(int op) {
        if (op == 0 || op == 1) {
            return 400;
        }
        return 500;
    }

    public static String getUidStateName(int uidState) {
        if (uidState != 100) {
            if (uidState != 200) {
                if (uidState != 300) {
                    if (uidState != 400) {
                        if (uidState != 500) {
                            if (uidState != 600) {
                                if (uidState == 700) {
                                    return "cch";
                                }
                                return "unknown";
                            }
                            return "bg";
                        }
                        return "fg";
                    }
                    return "fgsvc";
                }
                return "fgsvcl";
            }
            return "top";
        }
        return "pers";
    }

    public static final String getFlagName(int flag) {
        if (flag != 1) {
            if (flag != 2) {
                if (flag != 4) {
                    if (flag != 8) {
                        if (flag == 16) {
                            return "upd";
                        }
                        return "unknown";
                    }
                    return "tpd";
                }
                return "up";
            }
            return "tp";
        }
        return "s";
    }

    public static String keyToString(long key) {
        int uidState = extractUidStateFromKey(key);
        int flags = extractFlagsFromKey(key);
        return "[" + getUidStateName(uidState) + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + flagsToString(flags) + "]";
    }

    public static long makeKey(int uidState, int flags) {
        return (uidState << 31) | flags;
    }

    public static int extractUidStateFromKey(long key) {
        return (int) (key >> 31);
    }

    public static int extractFlagsFromKey(long key) {
        return (int) ((-1) & key);
    }

    public static String flagsToString(int flags) {
        StringBuilder flagsBuilder = new StringBuilder();
        while (flags != 0) {
            int flag = 1 << Integer.numberOfTrailingZeros(flags);
            flags &= ~flag;
            if (flagsBuilder.length() > 0) {
                flagsBuilder.append('|');
            }
            flagsBuilder.append(getFlagName(flag));
        }
        return flagsBuilder.toString();
    }

    @UnsupportedAppUsage
    public static int opToSwitch(int op) {
        return sOpToSwitch[op];
    }

    @UnsupportedAppUsage
    public static String opToName(int op) {
        if (op == -1) {
            return "NONE";
        }
        String[] strArr = sOpNames;
        if (op < strArr.length) {
            return strArr[op];
        }
        return "Unknown(" + op + ")";
    }

    public static String opToPublicName(int op) {
        return sOpToString[op];
    }

    public static int strDebugOpToOp(String op) {
        int i = 0;
        while (true) {
            String[] strArr = sOpNames;
            if (i < strArr.length) {
                if (!strArr[i].equals(op)) {
                    i++;
                } else {
                    return i;
                }
            } else {
                throw new IllegalArgumentException("Unknown operation string: " + op);
            }
        }
    }

    public static String opToPermission(int op) {
        return sOpPerms[op];
    }

    @SystemApi
    public static String opToPermission(String op) {
        return opToPermission(strOpToOp(op));
    }

    public static String opToRestriction(int op) {
        return sOpRestrictions[op];
    }

    public static int permissionToOpCode(String permission) {
        Integer boxedOpCode = sPermToOp.get(permission);
        if (boxedOpCode != null) {
            return boxedOpCode.intValue();
        }
        return -1;
    }

    public static boolean opAllowSystemBypassRestriction(int op) {
        return sOpAllowSystemRestrictionBypass[op];
    }

    public static int opToDefaultMode(int op) {
        return sOpDefaultMode[op];
    }

    @SystemApi
    public static int opToDefaultMode(String appOp) {
        return opToDefaultMode(strOpToOp(appOp));
    }

    public static String modeToName(int mode) {
        if (mode >= 0) {
            String[] strArr = MODE_NAMES;
            if (mode < strArr.length) {
                return strArr[mode];
            }
        }
        return "mode=" + mode;
    }

    public static boolean opAllowsReset(int op) {
        return !sOpDisableReset[op];
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static final class PackageOps implements Parcelable {
        public static final Parcelable.Creator<PackageOps> CREATOR = new Parcelable.Creator<PackageOps>() { // from class: android.app.AppOpsManager.PackageOps.1
            @Override // android.os.Parcelable.Creator
            public PackageOps createFromParcel(Parcel source) {
                return new PackageOps(source);
            }

            @Override // android.os.Parcelable.Creator
            public PackageOps[] newArray(int size) {
                return new PackageOps[size];
            }
        };
        private final List<OpEntry> mEntries;
        private final String mPackageName;
        private final int mUid;

        @UnsupportedAppUsage
        public PackageOps(String packageName, int uid, List<OpEntry> entries) {
            this.mPackageName = packageName;
            this.mUid = uid;
            this.mEntries = entries;
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public int getUid() {
            return this.mUid;
        }

        public List<OpEntry> getOps() {
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

        PackageOps(Parcel source) {
            this.mPackageName = source.readString();
            this.mUid = source.readInt();
            this.mEntries = new ArrayList();
            int N = source.readInt();
            for (int i = 0; i < N; i++) {
                this.mEntries.add(OpEntry.CREATOR.createFromParcel(source));
            }
        }
    }

    @SystemApi
    @Immutable
    /* loaded from: classes.dex */
    public static final class OpEntry implements Parcelable {
        public static final Parcelable.Creator<OpEntry> CREATOR = new Parcelable.Creator<OpEntry>() { // from class: android.app.AppOpsManager.OpEntry.1
            @Override // android.os.Parcelable.Creator
            public OpEntry createFromParcel(Parcel source) {
                return new OpEntry(source);
            }

            @Override // android.os.Parcelable.Creator
            public OpEntry[] newArray(int size) {
                return new OpEntry[size];
            }
        };
        private final LongSparseLongArray mAccessTimes;
        private final LongSparseLongArray mDurations;
        private final int mMode;
        private final int mOp;
        private final LongSparseArray<String> mProxyPackageNames;
        private final LongSparseLongArray mProxyUids;
        private final LongSparseLongArray mRejectTimes;
        private final boolean mRunning;

        public OpEntry(int op, boolean running, int mode, LongSparseLongArray accessTimes, LongSparseLongArray rejectTimes, LongSparseLongArray durations, LongSparseLongArray proxyUids, LongSparseArray<String> proxyPackageNames) {
            this.mOp = op;
            this.mRunning = running;
            this.mMode = mode;
            this.mAccessTimes = accessTimes;
            this.mRejectTimes = rejectTimes;
            this.mDurations = durations;
            this.mProxyUids = proxyUids;
            this.mProxyPackageNames = proxyPackageNames;
        }

        public OpEntry(int op, int mode) {
            this.mOp = op;
            this.mMode = mode;
            this.mRunning = false;
            this.mAccessTimes = null;
            this.mRejectTimes = null;
            this.mDurations = null;
            this.mProxyUids = null;
            this.mProxyPackageNames = null;
        }

        public LongSparseArray<Object> collectKeys() {
            LongSparseArray<Object> result = AppOpsManager.collectKeys(this.mAccessTimes, null);
            return AppOpsManager.collectKeys(this.mDurations, AppOpsManager.collectKeys(this.mRejectTimes, result));
        }

        @UnsupportedAppUsage
        public int getOp() {
            return this.mOp;
        }

        public String getOpStr() {
            return AppOpsManager.sOpToString[this.mOp];
        }

        public int getMode() {
            return this.mMode;
        }

        @UnsupportedAppUsage
        public long getTime() {
            return getLastAccessTime(31);
        }

        public long getLastAccessTime(int flags) {
            return AppOpsManager.maxForFlagsInStates(this.mAccessTimes, 100, 700, flags);
        }

        public long getLastAccessForegroundTime(int flags) {
            return AppOpsManager.maxForFlagsInStates(this.mAccessTimes, 100, AppOpsManager.resolveFirstUnrestrictedUidState(this.mOp), flags);
        }

        public long getLastAccessBackgroundTime(int flags) {
            return AppOpsManager.maxForFlagsInStates(this.mAccessTimes, AppOpsManager.resolveLastRestrictedUidState(this.mOp), 700, flags);
        }

        public long getLastAccessTime(int fromUidState, int toUidState, int flags) {
            return AppOpsManager.maxForFlagsInStates(this.mAccessTimes, fromUidState, toUidState, flags);
        }

        @UnsupportedAppUsage
        public long getRejectTime() {
            return getLastRejectTime(31);
        }

        public long getLastRejectTime(int flags) {
            return AppOpsManager.maxForFlagsInStates(this.mRejectTimes, 100, 700, flags);
        }

        public long getLastRejectForegroundTime(int flags) {
            return AppOpsManager.maxForFlagsInStates(this.mRejectTimes, 100, AppOpsManager.resolveFirstUnrestrictedUidState(this.mOp), flags);
        }

        public long getLastRejectBackgroundTime(int flags) {
            return AppOpsManager.maxForFlagsInStates(this.mRejectTimes, AppOpsManager.resolveLastRestrictedUidState(this.mOp), 700, flags);
        }

        public long getLastRejectTime(int fromUidState, int toUidState, int flags) {
            return AppOpsManager.maxForFlagsInStates(this.mRejectTimes, fromUidState, toUidState, flags);
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        public long getDuration() {
            return getLastDuration(100, 700, 31);
        }

        public long getLastForegroundDuration(int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mDurations, 100, AppOpsManager.resolveFirstUnrestrictedUidState(this.mOp), flags);
        }

        public long getLastBackgroundDuration(int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mDurations, AppOpsManager.resolveLastRestrictedUidState(this.mOp), 700, flags);
        }

        public long getLastDuration(int fromUidState, int toUidState, int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mDurations, fromUidState, toUidState, flags);
        }

        public int getProxyUid() {
            return (int) AppOpsManager.findFirstNonNegativeForFlagsInStates(this.mProxyUids, 100, 700, 31);
        }

        public int getProxyUid(int uidState, int flags) {
            return (int) AppOpsManager.findFirstNonNegativeForFlagsInStates(this.mProxyUids, uidState, uidState, flags);
        }

        public String getProxyPackageName() {
            return AppOpsManager.findFirstNonNullForFlagsInStates(this.mProxyPackageNames, 100, 700, 31);
        }

        public String getProxyPackageName(int uidState, int flags) {
            return AppOpsManager.findFirstNonNullForFlagsInStates(this.mProxyPackageNames, uidState, uidState, flags);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mOp);
            dest.writeInt(this.mMode);
            dest.writeBoolean(this.mRunning);
            AppOpsManager.writeLongSparseLongArrayToParcel(this.mAccessTimes, dest);
            AppOpsManager.writeLongSparseLongArrayToParcel(this.mRejectTimes, dest);
            AppOpsManager.writeLongSparseLongArrayToParcel(this.mDurations, dest);
            AppOpsManager.writeLongSparseLongArrayToParcel(this.mProxyUids, dest);
            AppOpsManager.writeLongSparseStringArrayToParcel(this.mProxyPackageNames, dest);
        }

        OpEntry(Parcel source) {
            this.mOp = source.readInt();
            this.mMode = source.readInt();
            this.mRunning = source.readBoolean();
            this.mAccessTimes = AppOpsManager.readLongSparseLongArrayFromParcel(source);
            this.mRejectTimes = AppOpsManager.readLongSparseLongArrayFromParcel(source);
            this.mDurations = AppOpsManager.readLongSparseLongArrayFromParcel(source);
            this.mProxyUids = AppOpsManager.readLongSparseLongArrayFromParcel(source);
            this.mProxyPackageNames = AppOpsManager.readLongSparseStringArrayFromParcel(source);
        }
    }

    @Immutable
    @SystemApi
    /* loaded from: classes.dex */
    public static final class HistoricalOpsRequest {
        private final long mBeginTimeMillis;
        private final long mEndTimeMillis;
        private final int mFlags;
        private final List<String> mOpNames;
        private final String mPackageName;
        private final int mUid;

        private HistoricalOpsRequest(int uid, String packageName, List<String> opNames, long beginTimeMillis, long endTimeMillis, int flags) {
            this.mUid = uid;
            this.mPackageName = packageName;
            this.mOpNames = opNames;
            this.mBeginTimeMillis = beginTimeMillis;
            this.mEndTimeMillis = endTimeMillis;
            this.mFlags = flags;
        }

        @SystemApi
        /* loaded from: classes.dex */
        public static final class Builder {
            private final long mBeginTimeMillis;
            private final long mEndTimeMillis;
            private List<String> mOpNames;
            private String mPackageName;
            private int mUid = -1;
            private int mFlags = 31;

            public Builder(long beginTimeMillis, long endTimeMillis) {
                Preconditions.checkArgument(beginTimeMillis >= 0 && beginTimeMillis < endTimeMillis, "beginTimeMillis must be non negative and lesser than endTimeMillis");
                this.mBeginTimeMillis = beginTimeMillis;
                this.mEndTimeMillis = endTimeMillis;
            }

            public Builder setUid(int uid) {
                Preconditions.checkArgument(uid == -1 || uid >= 0, "uid must be -1 or non negative");
                this.mUid = uid;
                return this;
            }

            public Builder setPackageName(String packageName) {
                this.mPackageName = packageName;
                return this;
            }

            public Builder setOpNames(List<String> opNames) {
                if (opNames != null) {
                    int opCount = opNames.size();
                    for (int i = 0; i < opCount; i++) {
                        Preconditions.checkArgument(AppOpsManager.strOpToOp(opNames.get(i)) != -1);
                    }
                }
                this.mOpNames = opNames;
                return this;
            }

            public Builder setFlags(int flags) {
                Preconditions.checkFlagsArgument(flags, 31);
                this.mFlags = flags;
                return this;
            }

            public HistoricalOpsRequest build() {
                return new HistoricalOpsRequest(this.mUid, this.mPackageName, this.mOpNames, this.mBeginTimeMillis, this.mEndTimeMillis, this.mFlags);
            }
        }
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static final class HistoricalOps implements Parcelable {
        public static final Parcelable.Creator<HistoricalOps> CREATOR = new Parcelable.Creator<HistoricalOps>() { // from class: android.app.AppOpsManager.HistoricalOps.1
            @Override // android.os.Parcelable.Creator
            public HistoricalOps createFromParcel(Parcel parcel) {
                return new HistoricalOps(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public HistoricalOps[] newArray(int size) {
                return new HistoricalOps[size];
            }
        };
        private long mBeginTimeMillis;
        private long mEndTimeMillis;
        private SparseArray<HistoricalUidOps> mHistoricalUidOps;

        public HistoricalOps(long beginTimeMillis, long endTimeMillis) {
            Preconditions.checkState(beginTimeMillis <= endTimeMillis);
            this.mBeginTimeMillis = beginTimeMillis;
            this.mEndTimeMillis = endTimeMillis;
        }

        public HistoricalOps(HistoricalOps other) {
            this.mBeginTimeMillis = other.mBeginTimeMillis;
            this.mEndTimeMillis = other.mEndTimeMillis;
            Preconditions.checkState(this.mBeginTimeMillis <= this.mEndTimeMillis);
            if (other.mHistoricalUidOps != null) {
                int opCount = other.getUidCount();
                for (int i = 0; i < opCount; i++) {
                    HistoricalUidOps origOps = other.getUidOpsAt(i);
                    HistoricalUidOps clonedOps = new HistoricalUidOps(origOps);
                    if (this.mHistoricalUidOps == null) {
                        this.mHistoricalUidOps = new SparseArray<>(opCount);
                    }
                    this.mHistoricalUidOps.put(clonedOps.getUid(), clonedOps);
                }
            }
        }

        private HistoricalOps(Parcel parcel) {
            this.mBeginTimeMillis = parcel.readLong();
            this.mEndTimeMillis = parcel.readLong();
            int[] uids = parcel.createIntArray();
            if (!ArrayUtils.isEmpty(uids)) {
                ParceledListSlice<HistoricalUidOps> listSlice = (ParceledListSlice) parcel.readParcelable(HistoricalOps.class.getClassLoader());
                List<HistoricalUidOps> uidOps = listSlice != null ? listSlice.getList() : null;
                if (uidOps == null) {
                    return;
                }
                for (int i = 0; i < uids.length; i++) {
                    if (this.mHistoricalUidOps == null) {
                        this.mHistoricalUidOps = new SparseArray<>();
                    }
                    this.mHistoricalUidOps.put(uids[i], uidOps.get(i));
                }
            }
        }

        public HistoricalOps spliceFromBeginning(double splicePoint) {
            return splice(splicePoint, true);
        }

        public HistoricalOps spliceFromEnd(double fractionToRemove) {
            return splice(fractionToRemove, false);
        }

        private HistoricalOps splice(double fractionToRemove, boolean beginning) {
            long spliceBeginTimeMills;
            long spliceEndTimeMills;
            if (beginning) {
                spliceBeginTimeMills = this.mBeginTimeMillis;
                spliceEndTimeMills = (long) (this.mBeginTimeMillis + (getDurationMillis() * fractionToRemove));
                this.mBeginTimeMillis = spliceEndTimeMills;
            } else {
                spliceBeginTimeMills = (long) (this.mEndTimeMillis - (getDurationMillis() * fractionToRemove));
                spliceEndTimeMills = this.mEndTimeMillis;
                this.mEndTimeMillis = spliceBeginTimeMills;
            }
            HistoricalOps splice = null;
            int uidCount = getUidCount();
            for (int i = 0; i < uidCount; i++) {
                HistoricalUidOps origOps = getUidOpsAt(i);
                HistoricalUidOps spliceOps = origOps.splice(fractionToRemove);
                if (spliceOps != null) {
                    if (splice == null) {
                        splice = new HistoricalOps(spliceBeginTimeMills, spliceEndTimeMills);
                    }
                    if (splice.mHistoricalUidOps == null) {
                        splice.mHistoricalUidOps = new SparseArray<>();
                    }
                    splice.mHistoricalUidOps.put(spliceOps.getUid(), spliceOps);
                }
            }
            return splice;
        }

        public void merge(HistoricalOps other) {
            this.mBeginTimeMillis = Math.min(this.mBeginTimeMillis, other.mBeginTimeMillis);
            this.mEndTimeMillis = Math.max(this.mEndTimeMillis, other.mEndTimeMillis);
            int uidCount = other.getUidCount();
            for (int i = 0; i < uidCount; i++) {
                HistoricalUidOps otherUidOps = other.getUidOpsAt(i);
                HistoricalUidOps thisUidOps = getUidOps(otherUidOps.getUid());
                if (thisUidOps == null) {
                    if (this.mHistoricalUidOps == null) {
                        this.mHistoricalUidOps = new SparseArray<>();
                    }
                    this.mHistoricalUidOps.put(otherUidOps.getUid(), otherUidOps);
                } else {
                    thisUidOps.merge(otherUidOps);
                }
            }
        }

        public void filter(int uid, String packageName, String[] opNames, long beginTimeMillis, long endTimeMillis) {
            long durationMillis = getDurationMillis();
            this.mBeginTimeMillis = Math.max(this.mBeginTimeMillis, beginTimeMillis);
            this.mEndTimeMillis = Math.min(this.mEndTimeMillis, endTimeMillis);
            double scaleFactor = Math.min((endTimeMillis - beginTimeMillis) / durationMillis, 1.0d);
            int uidCount = getUidCount();
            for (int i = uidCount - 1; i >= 0; i--) {
                HistoricalUidOps uidOp = this.mHistoricalUidOps.valueAt(i);
                if (uid != -1 && uid != uidOp.getUid()) {
                    this.mHistoricalUidOps.removeAt(i);
                } else {
                    uidOp.filter(packageName, opNames, scaleFactor);
                }
            }
        }

        public boolean isEmpty() {
            if (getBeginTimeMillis() >= getEndTimeMillis()) {
                return true;
            }
            int uidCount = getUidCount();
            for (int i = uidCount - 1; i >= 0; i--) {
                HistoricalUidOps uidOp = this.mHistoricalUidOps.valueAt(i);
                if (!uidOp.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public long getDurationMillis() {
            return this.mEndTimeMillis - this.mBeginTimeMillis;
        }

        public void increaseAccessCount(int opCode, int uid, String packageName, int uidState, int flags, long increment) {
            getOrCreateHistoricalUidOps(uid).increaseAccessCount(opCode, packageName, uidState, flags, increment);
        }

        public void increaseRejectCount(int opCode, int uid, String packageName, int uidState, int flags, long increment) {
            getOrCreateHistoricalUidOps(uid).increaseRejectCount(opCode, packageName, uidState, flags, increment);
        }

        public void increaseAccessDuration(int opCode, int uid, String packageName, int uidState, int flags, long increment) {
            getOrCreateHistoricalUidOps(uid).increaseAccessDuration(opCode, packageName, uidState, flags, increment);
        }

        public void offsetBeginAndEndTime(long offsetMillis) {
            this.mBeginTimeMillis += offsetMillis;
            this.mEndTimeMillis += offsetMillis;
        }

        public void setBeginAndEndTime(long beginTimeMillis, long endTimeMillis) {
            this.mBeginTimeMillis = beginTimeMillis;
            this.mEndTimeMillis = endTimeMillis;
        }

        public void setBeginTime(long beginTimeMillis) {
            this.mBeginTimeMillis = beginTimeMillis;
        }

        public void setEndTime(long endTimeMillis) {
            this.mEndTimeMillis = endTimeMillis;
        }

        public long getBeginTimeMillis() {
            return this.mBeginTimeMillis;
        }

        public long getEndTimeMillis() {
            return this.mEndTimeMillis;
        }

        public int getUidCount() {
            SparseArray<HistoricalUidOps> sparseArray = this.mHistoricalUidOps;
            if (sparseArray == null) {
                return 0;
            }
            return sparseArray.size();
        }

        public HistoricalUidOps getUidOpsAt(int index) {
            SparseArray<HistoricalUidOps> sparseArray = this.mHistoricalUidOps;
            if (sparseArray == null) {
                throw new IndexOutOfBoundsException();
            }
            return sparseArray.valueAt(index);
        }

        public HistoricalUidOps getUidOps(int uid) {
            SparseArray<HistoricalUidOps> sparseArray = this.mHistoricalUidOps;
            if (sparseArray == null) {
                return null;
            }
            return sparseArray.get(uid);
        }

        public void clearHistory(int uid, String packageName) {
            HistoricalUidOps historicalUidOps = getOrCreateHistoricalUidOps(uid);
            historicalUidOps.clearHistory(packageName);
            if (historicalUidOps.isEmpty()) {
                this.mHistoricalUidOps.remove(uid);
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeLong(this.mBeginTimeMillis);
            parcel.writeLong(this.mEndTimeMillis);
            SparseArray<HistoricalUidOps> sparseArray = this.mHistoricalUidOps;
            if (sparseArray != null) {
                int uidCount = sparseArray.size();
                parcel.writeInt(uidCount);
                for (int i = 0; i < uidCount; i++) {
                    parcel.writeInt(this.mHistoricalUidOps.keyAt(i));
                }
                List<HistoricalUidOps> opsList = new ArrayList<>(uidCount);
                for (int i2 = 0; i2 < uidCount; i2++) {
                    opsList.add(this.mHistoricalUidOps.valueAt(i2));
                }
                parcel.writeParcelable(new ParceledListSlice(opsList), flags);
                return;
            }
            parcel.writeInt(-1);
        }

        public void accept(HistoricalOpsVisitor visitor) {
            visitor.visitHistoricalOps(this);
            int uidCount = getUidCount();
            for (int i = 0; i < uidCount; i++) {
                getUidOpsAt(i).accept(visitor);
            }
        }

        private HistoricalUidOps getOrCreateHistoricalUidOps(int uid) {
            if (this.mHistoricalUidOps == null) {
                this.mHistoricalUidOps = new SparseArray<>();
            }
            HistoricalUidOps historicalUidOp = this.mHistoricalUidOps.get(uid);
            if (historicalUidOp == null) {
                HistoricalUidOps historicalUidOp2 = new HistoricalUidOps(uid);
                this.mHistoricalUidOps.put(uid, historicalUidOp2);
                return historicalUidOp2;
            }
            return historicalUidOp;
        }

        public static double round(double value) {
            BigDecimal decimalScale = new BigDecimal(value);
            return decimalScale.setScale(0, RoundingMode.HALF_UP).doubleValue();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            HistoricalOps other = (HistoricalOps) obj;
            if (this.mBeginTimeMillis != other.mBeginTimeMillis || this.mEndTimeMillis != other.mEndTimeMillis) {
                return false;
            }
            SparseArray<HistoricalUidOps> sparseArray = this.mHistoricalUidOps;
            if (sparseArray == null) {
                if (other.mHistoricalUidOps != null) {
                    return false;
                }
            } else if (!sparseArray.equals(other.mHistoricalUidOps)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            long j = this.mBeginTimeMillis;
            int result = (int) (j ^ (j >>> 32));
            return (result * 31) + this.mHistoricalUidOps.hashCode();
        }

        public String toString() {
            return getClass().getSimpleName() + "[from:" + this.mBeginTimeMillis + " to:" + this.mEndTimeMillis + "]";
        }
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static final class HistoricalUidOps implements Parcelable {
        public static final Parcelable.Creator<HistoricalUidOps> CREATOR = new Parcelable.Creator<HistoricalUidOps>() { // from class: android.app.AppOpsManager.HistoricalUidOps.1
            @Override // android.os.Parcelable.Creator
            public HistoricalUidOps createFromParcel(Parcel parcel) {
                return new HistoricalUidOps(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public HistoricalUidOps[] newArray(int size) {
                return new HistoricalUidOps[size];
            }
        };
        private ArrayMap<String, HistoricalPackageOps> mHistoricalPackageOps;
        private final int mUid;

        public HistoricalUidOps(int uid) {
            this.mUid = uid;
        }

        private HistoricalUidOps(HistoricalUidOps other) {
            this.mUid = other.mUid;
            int opCount = other.getPackageCount();
            for (int i = 0; i < opCount; i++) {
                HistoricalPackageOps origOps = other.getPackageOpsAt(i);
                HistoricalPackageOps cloneOps = new HistoricalPackageOps(origOps);
                if (this.mHistoricalPackageOps == null) {
                    this.mHistoricalPackageOps = new ArrayMap<>(opCount);
                }
                this.mHistoricalPackageOps.put(cloneOps.getPackageName(), cloneOps);
            }
        }

        private HistoricalUidOps(Parcel parcel) {
            this.mUid = parcel.readInt();
            this.mHistoricalPackageOps = parcel.createTypedArrayMap(HistoricalPackageOps.CREATOR);
        }

        public HistoricalUidOps splice(double fractionToRemove) {
            HistoricalUidOps splice = null;
            int packageCount = getPackageCount();
            for (int i = 0; i < packageCount; i++) {
                HistoricalPackageOps origOps = getPackageOpsAt(i);
                HistoricalPackageOps spliceOps = origOps.splice(fractionToRemove);
                if (spliceOps != null) {
                    if (splice == null) {
                        splice = new HistoricalUidOps(this.mUid);
                    }
                    if (splice.mHistoricalPackageOps == null) {
                        splice.mHistoricalPackageOps = new ArrayMap<>();
                    }
                    splice.mHistoricalPackageOps.put(spliceOps.getPackageName(), spliceOps);
                }
            }
            return splice;
        }

        public void merge(HistoricalUidOps other) {
            int packageCount = other.getPackageCount();
            for (int i = 0; i < packageCount; i++) {
                HistoricalPackageOps otherPackageOps = other.getPackageOpsAt(i);
                HistoricalPackageOps thisPackageOps = getPackageOps(otherPackageOps.getPackageName());
                if (thisPackageOps == null) {
                    if (this.mHistoricalPackageOps == null) {
                        this.mHistoricalPackageOps = new ArrayMap<>();
                    }
                    this.mHistoricalPackageOps.put(otherPackageOps.getPackageName(), otherPackageOps);
                } else {
                    thisPackageOps.merge(otherPackageOps);
                }
            }
        }

        public void filter(String packageName, String[] opNames, double fractionToRemove) {
            int packageCount = getPackageCount();
            for (int i = packageCount - 1; i >= 0; i--) {
                HistoricalPackageOps packageOps = getPackageOpsAt(i);
                if (packageName != null && !packageName.equals(packageOps.getPackageName())) {
                    this.mHistoricalPackageOps.removeAt(i);
                } else {
                    packageOps.filter(opNames, fractionToRemove);
                }
            }
        }

        public boolean isEmpty() {
            int packageCount = getPackageCount();
            for (int i = packageCount - 1; i >= 0; i--) {
                HistoricalPackageOps packageOps = this.mHistoricalPackageOps.valueAt(i);
                if (!packageOps.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public void increaseAccessCount(int opCode, String packageName, int uidState, int flags, long increment) {
            getOrCreateHistoricalPackageOps(packageName).increaseAccessCount(opCode, uidState, flags, increment);
        }

        public void increaseRejectCount(int opCode, String packageName, int uidState, int flags, long increment) {
            getOrCreateHistoricalPackageOps(packageName).increaseRejectCount(opCode, uidState, flags, increment);
        }

        public void increaseAccessDuration(int opCode, String packageName, int uidState, int flags, long increment) {
            getOrCreateHistoricalPackageOps(packageName).increaseAccessDuration(opCode, uidState, flags, increment);
        }

        public int getUid() {
            return this.mUid;
        }

        public int getPackageCount() {
            ArrayMap<String, HistoricalPackageOps> arrayMap = this.mHistoricalPackageOps;
            if (arrayMap == null) {
                return 0;
            }
            return arrayMap.size();
        }

        public HistoricalPackageOps getPackageOpsAt(int index) {
            ArrayMap<String, HistoricalPackageOps> arrayMap = this.mHistoricalPackageOps;
            if (arrayMap == null) {
                throw new IndexOutOfBoundsException();
            }
            return arrayMap.valueAt(index);
        }

        public HistoricalPackageOps getPackageOps(String packageName) {
            ArrayMap<String, HistoricalPackageOps> arrayMap = this.mHistoricalPackageOps;
            if (arrayMap == null) {
                return null;
            }
            return arrayMap.get(packageName);
        }

        public void clearHistory(String packageName) {
            ArrayMap<String, HistoricalPackageOps> arrayMap = this.mHistoricalPackageOps;
            if (arrayMap != null) {
                arrayMap.remove(packageName);
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeInt(this.mUid);
            parcel.writeTypedArrayMap(this.mHistoricalPackageOps, flags);
        }

        public void accept(HistoricalOpsVisitor visitor) {
            visitor.visitHistoricalUidOps(this);
            int packageCount = getPackageCount();
            for (int i = 0; i < packageCount; i++) {
                getPackageOpsAt(i).accept(visitor);
            }
        }

        private HistoricalPackageOps getOrCreateHistoricalPackageOps(String packageName) {
            if (this.mHistoricalPackageOps == null) {
                this.mHistoricalPackageOps = new ArrayMap<>();
            }
            HistoricalPackageOps historicalPackageOp = this.mHistoricalPackageOps.get(packageName);
            if (historicalPackageOp == null) {
                HistoricalPackageOps historicalPackageOp2 = new HistoricalPackageOps(packageName);
                this.mHistoricalPackageOps.put(packageName, historicalPackageOp2);
                return historicalPackageOp2;
            }
            return historicalPackageOp;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            HistoricalUidOps other = (HistoricalUidOps) obj;
            if (this.mUid != other.mUid) {
                return false;
            }
            ArrayMap<String, HistoricalPackageOps> arrayMap = this.mHistoricalPackageOps;
            if (arrayMap == null) {
                if (other.mHistoricalPackageOps != null) {
                    return false;
                }
            } else if (!arrayMap.equals(other.mHistoricalPackageOps)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int result = this.mUid;
            int i = result * 31;
            ArrayMap<String, HistoricalPackageOps> arrayMap = this.mHistoricalPackageOps;
            int result2 = i + (arrayMap != null ? arrayMap.hashCode() : 0);
            return result2;
        }
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static final class HistoricalPackageOps implements Parcelable {
        public static final Parcelable.Creator<HistoricalPackageOps> CREATOR = new Parcelable.Creator<HistoricalPackageOps>() { // from class: android.app.AppOpsManager.HistoricalPackageOps.1
            @Override // android.os.Parcelable.Creator
            public HistoricalPackageOps createFromParcel(Parcel parcel) {
                return new HistoricalPackageOps(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public HistoricalPackageOps[] newArray(int size) {
                return new HistoricalPackageOps[size];
            }
        };
        private ArrayMap<String, HistoricalOp> mHistoricalOps;
        private final String mPackageName;

        public HistoricalPackageOps(String packageName) {
            this.mPackageName = packageName;
        }

        private HistoricalPackageOps(HistoricalPackageOps other) {
            this.mPackageName = other.mPackageName;
            int opCount = other.getOpCount();
            for (int i = 0; i < opCount; i++) {
                HistoricalOp origOp = other.getOpAt(i);
                HistoricalOp cloneOp = new HistoricalOp(origOp);
                if (this.mHistoricalOps == null) {
                    this.mHistoricalOps = new ArrayMap<>(opCount);
                }
                this.mHistoricalOps.put(cloneOp.getOpName(), cloneOp);
            }
        }

        private HistoricalPackageOps(Parcel parcel) {
            this.mPackageName = parcel.readString();
            this.mHistoricalOps = parcel.createTypedArrayMap(HistoricalOp.CREATOR);
        }

        public HistoricalPackageOps splice(double fractionToRemove) {
            HistoricalPackageOps splice = null;
            int opCount = getOpCount();
            for (int i = 0; i < opCount; i++) {
                HistoricalOp origOps = getOpAt(i);
                HistoricalOp spliceOps = origOps.splice(fractionToRemove);
                if (spliceOps != null) {
                    if (splice == null) {
                        splice = new HistoricalPackageOps(this.mPackageName);
                    }
                    if (splice.mHistoricalOps == null) {
                        splice.mHistoricalOps = new ArrayMap<>();
                    }
                    splice.mHistoricalOps.put(spliceOps.getOpName(), spliceOps);
                }
            }
            return splice;
        }

        public void merge(HistoricalPackageOps other) {
            int opCount = other.getOpCount();
            for (int i = 0; i < opCount; i++) {
                HistoricalOp otherOp = other.getOpAt(i);
                HistoricalOp thisOp = getOp(otherOp.getOpName());
                if (thisOp == null) {
                    if (this.mHistoricalOps == null) {
                        this.mHistoricalOps = new ArrayMap<>();
                    }
                    this.mHistoricalOps.put(otherOp.getOpName(), otherOp);
                } else {
                    thisOp.merge(otherOp);
                }
            }
        }

        public void filter(String[] opNames, double scaleFactor) {
            int opCount = getOpCount();
            for (int i = opCount - 1; i >= 0; i--) {
                HistoricalOp op = this.mHistoricalOps.valueAt(i);
                if (opNames != null && !ArrayUtils.contains(opNames, op.getOpName())) {
                    this.mHistoricalOps.removeAt(i);
                } else {
                    op.filter(scaleFactor);
                }
            }
        }

        public boolean isEmpty() {
            int opCount = getOpCount();
            for (int i = opCount - 1; i >= 0; i--) {
                HistoricalOp op = this.mHistoricalOps.valueAt(i);
                if (!op.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public void increaseAccessCount(int opCode, int uidState, int flags, long increment) {
            getOrCreateHistoricalOp(opCode).increaseAccessCount(uidState, flags, increment);
        }

        public void increaseRejectCount(int opCode, int uidState, int flags, long increment) {
            getOrCreateHistoricalOp(opCode).increaseRejectCount(uidState, flags, increment);
        }

        public void increaseAccessDuration(int opCode, int uidState, int flags, long increment) {
            getOrCreateHistoricalOp(opCode).increaseAccessDuration(uidState, flags, increment);
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public int getOpCount() {
            ArrayMap<String, HistoricalOp> arrayMap = this.mHistoricalOps;
            if (arrayMap == null) {
                return 0;
            }
            return arrayMap.size();
        }

        public HistoricalOp getOpAt(int index) {
            ArrayMap<String, HistoricalOp> arrayMap = this.mHistoricalOps;
            if (arrayMap == null) {
                throw new IndexOutOfBoundsException();
            }
            return arrayMap.valueAt(index);
        }

        public HistoricalOp getOp(String opName) {
            ArrayMap<String, HistoricalOp> arrayMap = this.mHistoricalOps;
            if (arrayMap == null) {
                return null;
            }
            return arrayMap.get(opName);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(this.mPackageName);
            parcel.writeTypedArrayMap(this.mHistoricalOps, flags);
        }

        public void accept(HistoricalOpsVisitor visitor) {
            visitor.visitHistoricalPackageOps(this);
            int opCount = getOpCount();
            for (int i = 0; i < opCount; i++) {
                getOpAt(i).accept(visitor);
            }
        }

        private HistoricalOp getOrCreateHistoricalOp(int opCode) {
            if (this.mHistoricalOps == null) {
                this.mHistoricalOps = new ArrayMap<>();
            }
            String opStr = AppOpsManager.sOpToString[opCode];
            HistoricalOp op = this.mHistoricalOps.get(opStr);
            if (op == null) {
                HistoricalOp op2 = new HistoricalOp(opCode);
                this.mHistoricalOps.put(opStr, op2);
                return op2;
            }
            return op;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            HistoricalPackageOps other = (HistoricalPackageOps) obj;
            if (!this.mPackageName.equals(other.mPackageName)) {
                return false;
            }
            ArrayMap<String, HistoricalOp> arrayMap = this.mHistoricalOps;
            if (arrayMap == null) {
                if (other.mHistoricalOps != null) {
                    return false;
                }
            } else if (!arrayMap.equals(other.mHistoricalOps)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            String str = this.mPackageName;
            int result = str != null ? str.hashCode() : 0;
            int i = result * 31;
            ArrayMap<String, HistoricalOp> arrayMap = this.mHistoricalOps;
            int result2 = i + (arrayMap != null ? arrayMap.hashCode() : 0);
            return result2;
        }
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static final class HistoricalOp implements Parcelable {
        public static final Parcelable.Creator<HistoricalOp> CREATOR = new Parcelable.Creator<HistoricalOp>() { // from class: android.app.AppOpsManager.HistoricalOp.1
            @Override // android.os.Parcelable.Creator
            public HistoricalOp createFromParcel(Parcel source) {
                return new HistoricalOp(source);
            }

            @Override // android.os.Parcelable.Creator
            public HistoricalOp[] newArray(int size) {
                return new HistoricalOp[size];
            }
        };
        private LongSparseLongArray mAccessCount;
        private LongSparseLongArray mAccessDuration;
        private final int mOp;
        private LongSparseLongArray mRejectCount;

        public HistoricalOp(int op) {
            this.mOp = op;
        }

        private HistoricalOp(HistoricalOp other) {
            this.mOp = other.mOp;
            LongSparseLongArray longSparseLongArray = other.mAccessCount;
            if (longSparseLongArray != null) {
                this.mAccessCount = longSparseLongArray.m37clone();
            }
            LongSparseLongArray longSparseLongArray2 = other.mRejectCount;
            if (longSparseLongArray2 != null) {
                this.mRejectCount = longSparseLongArray2.m37clone();
            }
            LongSparseLongArray longSparseLongArray3 = other.mAccessDuration;
            if (longSparseLongArray3 != null) {
                this.mAccessDuration = longSparseLongArray3.m37clone();
            }
        }

        private HistoricalOp(Parcel parcel) {
            this.mOp = parcel.readInt();
            this.mAccessCount = AppOpsManager.readLongSparseLongArrayFromParcel(parcel);
            this.mRejectCount = AppOpsManager.readLongSparseLongArrayFromParcel(parcel);
            this.mAccessDuration = AppOpsManager.readLongSparseLongArrayFromParcel(parcel);
        }

        public void filter(double scaleFactor) {
            scale(this.mAccessCount, scaleFactor);
            scale(this.mRejectCount, scaleFactor);
            scale(this.mAccessDuration, scaleFactor);
        }

        public boolean isEmpty() {
            return (hasData(this.mAccessCount) || hasData(this.mRejectCount) || hasData(this.mAccessDuration)) ? false : true;
        }

        private boolean hasData(LongSparseLongArray array) {
            return array != null && array.size() > 0;
        }

        public HistoricalOp splice(double fractionToRemove) {
            HistoricalOp splice = new HistoricalOp(this.mOp);
            LongSparseLongArray longSparseLongArray = this.mAccessCount;
            Objects.requireNonNull(splice);
            splice(longSparseLongArray, new $$Lambda$AppOpsManager$HistoricalOp$HUOLFYs8TiaQIOXcrq6JzjxA6gs(splice), fractionToRemove);
            LongSparseLongArray longSparseLongArray2 = this.mRejectCount;
            Objects.requireNonNull(splice);
            splice(longSparseLongArray2, new $$Lambda$AppOpsManager$HistoricalOp$DkVcBvqB32SMHlxw0sWQPh3GL1A(splice), fractionToRemove);
            LongSparseLongArray longSparseLongArray3 = this.mAccessDuration;
            Objects.requireNonNull(splice);
            splice(longSparseLongArray3, new $$Lambda$AppOpsManager$HistoricalOp$Vs6pDL0wjOBTquwNnreWVbPQrn4(splice), fractionToRemove);
            return splice;
        }

        private static void splice(LongSparseLongArray sourceContainer, Supplier<LongSparseLongArray> destContainerProvider, double fractionToRemove) {
            if (sourceContainer != null) {
                int size = sourceContainer.size();
                for (int i = 0; i < size; i++) {
                    long key = sourceContainer.keyAt(i);
                    long value = sourceContainer.valueAt(i);
                    long removedFraction = Math.round(value * fractionToRemove);
                    if (removedFraction > 0) {
                        destContainerProvider.get().put(key, removedFraction);
                        sourceContainer.put(key, value - removedFraction);
                    }
                }
            }
        }

        public void merge(HistoricalOp other) {
            merge(new $$Lambda$AppOpsManager$HistoricalOp$HUOLFYs8TiaQIOXcrq6JzjxA6gs(this), other.mAccessCount);
            merge(new $$Lambda$AppOpsManager$HistoricalOp$DkVcBvqB32SMHlxw0sWQPh3GL1A(this), other.mRejectCount);
            merge(new $$Lambda$AppOpsManager$HistoricalOp$Vs6pDL0wjOBTquwNnreWVbPQrn4(this), other.mAccessDuration);
        }

        public void increaseAccessCount(int uidState, int flags, long increment) {
            increaseCount(getOrCreateAccessCount(), uidState, flags, increment);
        }

        public void increaseRejectCount(int uidState, int flags, long increment) {
            increaseCount(getOrCreateRejectCount(), uidState, flags, increment);
        }

        public void increaseAccessDuration(int uidState, int flags, long increment) {
            increaseCount(getOrCreateAccessDuration(), uidState, flags, increment);
        }

        private void increaseCount(LongSparseLongArray counts, int uidState, int flags, long increment) {
            while (flags != 0) {
                int flag = 1 << Integer.numberOfTrailingZeros(flags);
                flags &= ~flag;
                long key = AppOpsManager.makeKey(uidState, flag);
                counts.put(key, counts.get(key) + increment);
            }
        }

        public String getOpName() {
            return AppOpsManager.sOpToString[this.mOp];
        }

        public int getOpCode() {
            return this.mOp;
        }

        public long getForegroundAccessCount(int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mAccessCount, 100, AppOpsManager.resolveFirstUnrestrictedUidState(this.mOp), flags);
        }

        public long getBackgroundAccessCount(int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mAccessCount, AppOpsManager.resolveLastRestrictedUidState(this.mOp), 700, flags);
        }

        public long getAccessCount(int fromUidState, int toUidState, int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mAccessCount, fromUidState, toUidState, flags);
        }

        public long getForegroundRejectCount(int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mRejectCount, 100, AppOpsManager.resolveFirstUnrestrictedUidState(this.mOp), flags);
        }

        public long getBackgroundRejectCount(int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mRejectCount, AppOpsManager.resolveLastRestrictedUidState(this.mOp), 700, flags);
        }

        public long getRejectCount(int fromUidState, int toUidState, int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mRejectCount, fromUidState, toUidState, flags);
        }

        public long getForegroundAccessDuration(int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mAccessDuration, 100, AppOpsManager.resolveFirstUnrestrictedUidState(this.mOp), flags);
        }

        public long getBackgroundAccessDuration(int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mAccessDuration, AppOpsManager.resolveLastRestrictedUidState(this.mOp), 700, flags);
        }

        public long getAccessDuration(int fromUidState, int toUidState, int flags) {
            return AppOpsManager.sumForFlagsInStates(this.mAccessDuration, fromUidState, toUidState, flags);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeInt(this.mOp);
            AppOpsManager.writeLongSparseLongArrayToParcel(this.mAccessCount, parcel);
            AppOpsManager.writeLongSparseLongArrayToParcel(this.mRejectCount, parcel);
            AppOpsManager.writeLongSparseLongArrayToParcel(this.mAccessDuration, parcel);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            HistoricalOp other = (HistoricalOp) obj;
            if (this.mOp != other.mOp || !Objects.equals(this.mAccessCount, other.mAccessCount) || !Objects.equals(this.mRejectCount, other.mRejectCount)) {
                return false;
            }
            return Objects.equals(this.mAccessDuration, other.mAccessDuration);
        }

        public int hashCode() {
            int result = this.mOp;
            return (((((result * 31) + Objects.hashCode(this.mAccessCount)) * 31) + Objects.hashCode(this.mRejectCount)) * 31) + Objects.hashCode(this.mAccessDuration);
        }

        public void accept(HistoricalOpsVisitor visitor) {
            visitor.visitHistoricalOp(this);
        }

        public LongSparseLongArray getOrCreateAccessCount() {
            if (this.mAccessCount == null) {
                this.mAccessCount = new LongSparseLongArray();
            }
            return this.mAccessCount;
        }

        public LongSparseLongArray getOrCreateRejectCount() {
            if (this.mRejectCount == null) {
                this.mRejectCount = new LongSparseLongArray();
            }
            return this.mRejectCount;
        }

        public LongSparseLongArray getOrCreateAccessDuration() {
            if (this.mAccessDuration == null) {
                this.mAccessDuration = new LongSparseLongArray();
            }
            return this.mAccessDuration;
        }

        private static void scale(LongSparseLongArray data, double scaleFactor) {
            if (data != null) {
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    data.put(data.keyAt(i), (long) HistoricalOps.round(data.valueAt(i) * scaleFactor));
                }
            }
        }

        private static void merge(Supplier<LongSparseLongArray> thisSupplier, LongSparseLongArray other) {
            if (other != null) {
                int otherSize = other.size();
                for (int i = 0; i < otherSize; i++) {
                    LongSparseLongArray that = thisSupplier.get();
                    long otherKey = other.keyAt(i);
                    long otherValue = other.valueAt(i);
                    that.put(otherKey, that.get(otherKey) + otherValue);
                }
            }
        }

        public LongSparseArray<Object> collectKeys() {
            LongSparseArray<Object> result = AppOpsManager.collectKeys(this.mAccessCount, null);
            return AppOpsManager.collectKeys(this.mAccessDuration, AppOpsManager.collectKeys(this.mRejectCount, result));
        }
    }

    public static long sumForFlagsInStates(LongSparseLongArray counts, int beginUidState, int endUidState, int flags) {
        int[] iArr;
        if (counts == null) {
            return 0L;
        }
        long sum = 0;
        while (flags != 0) {
            int flag = 1 << Integer.numberOfTrailingZeros(flags);
            flags &= ~flag;
            for (int uidState : UID_STATES) {
                if (uidState >= beginUidState && uidState <= endUidState) {
                    long key = makeKey(uidState, flag);
                    sum += counts.get(key);
                }
            }
        }
        return sum;
    }

    public static long findFirstNonNegativeForFlagsInStates(LongSparseLongArray counts, int beginUidState, int endUidState, int flags) {
        int[] iArr;
        if (counts == null) {
            return -1L;
        }
        int flags2 = flags;
        while (flags2 != 0) {
            int flag = 1 << Integer.numberOfTrailingZeros(flags2);
            flags2 &= ~flag;
            for (int uidState : UID_STATES) {
                if (uidState >= beginUidState && uidState <= endUidState) {
                    long key = makeKey(uidState, flag);
                    long value = counts.get(key);
                    if (value >= 0) {
                        return value;
                    }
                }
            }
        }
        return -1L;
    }

    public static String findFirstNonNullForFlagsInStates(LongSparseArray<String> counts, int beginUidState, int endUidState, int flags) {
        int[] iArr;
        if (counts == null) {
            return null;
        }
        while (flags != 0) {
            int flag = 1 << Integer.numberOfTrailingZeros(flags);
            flags &= ~flag;
            for (int uidState : UID_STATES) {
                if (uidState >= beginUidState && uidState <= endUidState) {
                    long key = makeKey(uidState, flag);
                    String value = counts.get(key);
                    if (value != null) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static class OnOpChangedInternalListener implements OnOpChangedListener {
        @Override // android.app.AppOpsManager.OnOpChangedListener
        public void onOpChanged(String op, String packageName) {
        }

        public void onOpChanged(int op, String packageName) {
        }
    }

    public AppOpsManager(Context context, IAppOpsService service) {
        this.mContext = context;
        this.mService = service;
    }

    @SystemApi
    public List<PackageOps> getPackagesForOps(String[] ops) {
        int opCount = ops.length;
        int[] opCodes = new int[opCount];
        for (int i = 0; i < opCount; i++) {
            opCodes[i] = sOpStrToOp.get(ops[i]).intValue();
        }
        List<PackageOps> result = getPackagesForOps(opCodes);
        return result != null ? result : Collections.emptyList();
    }

    @UnsupportedAppUsage
    public List<PackageOps> getPackagesForOps(int[] ops) {
        try {
            return this.mService.getPackagesForOps(ops);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    @Deprecated
    public List<PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) {
        try {
            return this.mService.getOpsForPackage(uid, packageName, ops);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<PackageOps> getOpsForPackage(int uid, String packageName, String... ops) {
        int[] opCodes = null;
        if (ops != null) {
            opCodes = new int[ops.length];
            for (int i = 0; i < ops.length; i++) {
                opCodes[i] = strOpToOp(ops[i]);
            }
        }
        try {
            List<PackageOps> result = this.mService.getOpsForPackage(uid, packageName, opCodes);
            if (result == null) {
                return Collections.emptyList();
            }
            return result;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void getHistoricalOps(HistoricalOpsRequest request, final Executor executor, final Consumer<HistoricalOps> callback) {
        Preconditions.checkNotNull(executor, "executor cannot be null");
        Preconditions.checkNotNull(callback, "callback cannot be null");
        try {
            this.mService.getHistoricalOps(request.mUid, request.mPackageName, request.mOpNames, request.mBeginTimeMillis, request.mEndTimeMillis, request.mFlags, new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.app.-$$Lambda$AppOpsManager$4Zbi7CSLEt0nvOmfJBVYtJkauTQ
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    AppOpsManager.lambda$getHistoricalOps$1(executor, callback, bundle);
                }
            }));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static /* synthetic */ void lambda$getHistoricalOps$1(Executor executor, final Consumer callback, Bundle result) {
        final HistoricalOps ops = (HistoricalOps) result.getParcelable(KEY_HISTORICAL_OPS);
        long identity = Binder.clearCallingIdentity();
        try {
            executor.execute(new Runnable() { // from class: android.app.-$$Lambda$AppOpsManager$frSyqmhVUmNbhMckfMS3PSwTMlw
                @Override // java.lang.Runnable
                public final void run() {
                    callback.accept(ops);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    public void getHistoricalOpsFromDiskRaw(HistoricalOpsRequest request, final Executor executor, final Consumer<HistoricalOps> callback) {
        Preconditions.checkNotNull(executor, "executor cannot be null");
        Preconditions.checkNotNull(callback, "callback cannot be null");
        try {
            this.mService.getHistoricalOpsFromDiskRaw(request.mUid, request.mPackageName, request.mOpNames, request.mBeginTimeMillis, request.mEndTimeMillis, request.mFlags, new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: android.app.-$$Lambda$AppOpsManager$5k42P8tID8pwpGFZvo7VQyru20E
                @Override // android.os.RemoteCallback.OnResultListener
                public final void onResult(Bundle bundle) {
                    AppOpsManager.lambda$getHistoricalOpsFromDiskRaw$3(executor, callback, bundle);
                }
            }));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static /* synthetic */ void lambda$getHistoricalOpsFromDiskRaw$3(Executor executor, final Consumer callback, Bundle result) {
        final HistoricalOps ops = (HistoricalOps) result.getParcelable(KEY_HISTORICAL_OPS);
        long identity = Binder.clearCallingIdentity();
        try {
            executor.execute(new Runnable() { // from class: android.app.-$$Lambda$AppOpsManager$VfNXTtJaOeEFrdIj0oDWr_N9nks
                @Override // java.lang.Runnable
                public final void run() {
                    callback.accept(ops);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    public void reloadNonHistoricalState() {
        try {
            this.mService.reloadNonHistoricalState();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setUidMode(int code, int uid, int mode) {
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

    public void setUserRestriction(int code, boolean restricted, IBinder token) {
        setUserRestriction(code, restricted, token, null);
    }

    public void setUserRestriction(int code, boolean restricted, IBinder token, String[] exceptionPackages) {
        setUserRestrictionForUser(code, restricted, token, exceptionPackages, this.mContext.getUserId());
    }

    public void setUserRestrictionForUser(int code, boolean restricted, IBinder token, String[] exceptionPackages, int userId) {
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

    @UnsupportedAppUsage
    public void setRestriction(int code, int usage, int mode, String[] exceptionPackages) {
        try {
            int uid = Binder.getCallingUid();
            this.mService.setAudioRestriction(code, usage, uid, mode, exceptionPackages);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void resetAllModes() {
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

    public void startWatchingMode(String op, String packageName, int flags, OnOpChangedListener callback) {
        startWatchingMode(strOpToOp(op), packageName, flags, callback);
    }

    public void startWatchingMode(int op, String packageName, OnOpChangedListener callback) {
        startWatchingMode(op, packageName, 0, callback);
    }

    public void startWatchingMode(int op, String packageName, int flags, final OnOpChangedListener callback) {
        synchronized (this.mModeWatchers) {
            IAppOpsCallback cb = this.mModeWatchers.get(callback);
            if (cb == null) {
                cb = new IAppOpsCallback.Stub() { // from class: android.app.AppOpsManager.1
                    {
                        AppOpsManager.this = this;
                    }

                    @Override // com.android.internal.app.IAppOpsCallback
                    public void opChanged(int op2, int uid, String packageName2) {
                        OnOpChangedListener onOpChangedListener = callback;
                        if (onOpChangedListener instanceof OnOpChangedInternalListener) {
                            ((OnOpChangedInternalListener) onOpChangedListener).onOpChanged(op2, packageName2);
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
            IAppOpsCallback cb = this.mModeWatchers.remove(callback);
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
                {
                    AppOpsManager.this = this;
                }

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
            IAppOpsActiveCallback cb = this.mActiveWatchers.remove(callback);
            if (cb != null) {
                try {
                    this.mService.stopWatchingActive(cb);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    public void startWatchingNoted(int[] ops, final OnOpNotedListener callback) {
        synchronized (this.mNotedWatchers) {
            if (this.mNotedWatchers.get(callback) != null) {
                return;
            }
            IAppOpsNotedCallback cb = new IAppOpsNotedCallback.Stub() { // from class: android.app.AppOpsManager.3
                {
                    AppOpsManager.this = this;
                }

                @Override // com.android.internal.app.IAppOpsNotedCallback
                public void opNoted(int op, int uid, String packageName, int mode) {
                    callback.onOpNoted(op, uid, packageName, mode);
                }
            };
            this.mNotedWatchers.put(callback, cb);
            try {
                this.mService.startWatchingNoted(ops, cb);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void stopWatchingNoted(OnOpNotedListener callback) {
        synchronized (this.mNotedWatchers) {
            IAppOpsNotedCallback cb = this.mNotedWatchers.get(callback);
            if (cb != null) {
                try {
                    this.mService.stopWatchingNoted(cb);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    private String buildSecurityExceptionMsg(int op, int uid, String packageName) {
        return packageName + " from uid " + uid + " not allowed to perform " + sOpNames[op];
    }

    public static int strOpToOp(String op) {
        Integer val = sOpStrToOp.get(op);
        if (val == null) {
            throw new IllegalArgumentException("Unknown operation string: " + op);
        }
        return val.intValue();
    }

    public int unsafeCheckOp(String op, int uid, String packageName) {
        return checkOp(strOpToOp(op), uid, packageName);
    }

    @Deprecated
    public int checkOp(String op, int uid, String packageName) {
        return checkOp(strOpToOp(op), uid, packageName);
    }

    public int unsafeCheckOpNoThrow(String op, int uid, String packageName) {
        return checkOpNoThrow(strOpToOp(op), uid, packageName);
    }

    @Deprecated
    public int checkOpNoThrow(String op, int uid, String packageName) {
        return checkOpNoThrow(strOpToOp(op), uid, packageName);
    }

    public int unsafeCheckOpRaw(String op, int uid, String packageName) {
        try {
            return this.mService.checkOperationRaw(strOpToOp(op), uid, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int unsafeCheckOpRawNoThrow(String op, int uid, String packageName) {
        try {
            return this.mService.checkOperationRaw(strOpToOp(op), uid, packageName);
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

    public int noteProxyOpNoThrow(String op, String proxiedPackageName, int proxiedUid) {
        return noteProxyOpNoThrow(strOpToOp(op), proxiedPackageName, proxiedUid);
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

    @UnsupportedAppUsage
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

    @UnsupportedAppUsage
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

    public int checkAudioOp(int op, int stream, int uid, String packageName) {
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

    public int checkAudioOpNoThrow(int op, int stream, int uid, String packageName) {
        try {
            return this.mService.checkAudioOperation(op, stream, uid, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int noteOp(int op, int uid, String packageName) {
        int mode = noteOpNoThrow(op, uid, packageName);
        if (mode == 2) {
            throw new SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
        }
        return mode;
    }

    @UnsupportedAppUsage
    public int noteProxyOp(int op, String proxiedPackageName) {
        int mode = noteProxyOpNoThrow(op, proxiedPackageName);
        if (mode == 2) {
            throw new SecurityException("Proxy package " + this.mContext.getOpPackageName() + " from uid " + Process.myUid() + " or calling package " + proxiedPackageName + " from uid " + Binder.getCallingUid() + " not allowed to perform " + sOpNames[op]);
        }
        return mode;
    }

    public int noteProxyOpNoThrow(int op, String proxiedPackageName, int proxiedUid) {
        try {
            return this.mService.noteProxyOperation(op, Process.myUid(), this.mContext.getOpPackageName(), proxiedUid, proxiedPackageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int noteProxyOpNoThrow(int op, String proxiedPackageName) {
        return noteProxyOpNoThrow(op, proxiedPackageName, Binder.getCallingUid());
    }

    @UnsupportedAppUsage
    public int noteOpNoThrow(int op, int uid, String packageName) {
        try {
            return this.mService.noteOperation(op, uid, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int noteOp(int op) {
        return noteOp(op, Process.myUid(), this.mContext.getOpPackageName());
    }

    @UnsupportedAppUsage
    public static IBinder getToken(IAppOpsService service) {
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

    public int startOp(int op) {
        return startOp(op, Process.myUid(), this.mContext.getOpPackageName());
    }

    public int startOp(int op, int uid, String packageName) {
        return startOp(op, uid, packageName, false);
    }

    public int startOp(int op, int uid, String packageName, boolean startIfModeDefault) {
        int mode = startOpNoThrow(op, uid, packageName, startIfModeDefault);
        if (mode == 2) {
            throw new SecurityException(buildSecurityExceptionMsg(op, uid, packageName));
        }
        return mode;
    }

    public int startOpNoThrow(int op, int uid, String packageName) {
        return startOpNoThrow(op, uid, packageName, false);
    }

    public int startOpNoThrow(int op, int uid, String packageName, boolean startIfModeDefault) {
        try {
            return this.mService.startOperation(getToken(this.mService), op, uid, packageName, startIfModeDefault);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void finishOp(int op, int uid, String packageName) {
        try {
            this.mService.finishOperation(getToken(this.mService), op, uid, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void finishOp(int op) {
        finishOp(op, Process.myUid(), this.mContext.getOpPackageName());
    }

    public boolean isOperationActive(int code, int uid, String packageName) {
        try {
            return this.mService.isOperationActive(code, uid, packageName);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setHistoryParameters(int mode, long baseSnapshotInterval, int compressionStep) {
        try {
            this.mService.setHistoryParameters(mode, baseSnapshotInterval, compressionStep);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void offsetHistory(long offsetMillis) {
        try {
            this.mService.offsetHistory(offsetMillis);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addHistoricalOps(HistoricalOps ops) {
        try {
            this.mService.addHistoricalOps(ops);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void resetHistoryParameters() {
        try {
            this.mService.resetHistoryParameters();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearHistory() {
        try {
            this.mService.clearHistory();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public static String[] getOpStrs() {
        String[] strArr = sOpToString;
        return (String[]) Arrays.copyOf(strArr, strArr.length);
    }

    public static int getNumOps() {
        return 91;
    }

    public static long maxForFlagsInStates(LongSparseLongArray counts, int beginUidState, int endUidState, int flags) {
        int[] iArr;
        if (counts == null) {
            return 0L;
        }
        long max = 0;
        while (flags != 0) {
            int flag = 1 << Integer.numberOfTrailingZeros(flags);
            flags &= ~flag;
            for (int uidState : UID_STATES) {
                if (uidState >= beginUidState && uidState <= endUidState) {
                    long key = makeKey(uidState, flag);
                    max = Math.max(max, counts.get(key));
                }
            }
        }
        return max;
    }

    public static void writeLongSparseLongArrayToParcel(LongSparseLongArray array, Parcel parcel) {
        if (array != null) {
            int size = array.size();
            parcel.writeInt(size);
            for (int i = 0; i < size; i++) {
                parcel.writeLong(array.keyAt(i));
                parcel.writeLong(array.valueAt(i));
            }
            return;
        }
        parcel.writeInt(-1);
    }

    public static LongSparseLongArray readLongSparseLongArrayFromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) {
            return null;
        }
        LongSparseLongArray array = new LongSparseLongArray(size);
        for (int i = 0; i < size; i++) {
            array.append(parcel.readLong(), parcel.readLong());
        }
        return array;
    }

    public static void writeLongSparseStringArrayToParcel(LongSparseArray<String> array, Parcel parcel) {
        if (array != null) {
            int size = array.size();
            parcel.writeInt(size);
            for (int i = 0; i < size; i++) {
                parcel.writeLong(array.keyAt(i));
                parcel.writeString(array.valueAt(i));
            }
            return;
        }
        parcel.writeInt(-1);
    }

    public static LongSparseArray<String> readLongSparseStringArrayFromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) {
            return null;
        }
        LongSparseArray<String> array = new LongSparseArray<>(size);
        for (int i = 0; i < size; i++) {
            array.append(parcel.readLong(), parcel.readString());
        }
        return array;
    }

    public static LongSparseArray<Object> collectKeys(LongSparseLongArray array, LongSparseArray<Object> result) {
        if (array != null) {
            if (result == null) {
                result = new LongSparseArray<>();
            }
            int accessSize = array.size();
            for (int i = 0; i < accessSize; i++) {
                result.put(array.keyAt(i), null);
            }
        }
        return result;
    }

    public static String uidStateToString(int uidState) {
        if (uidState != 100) {
            if (uidState != 200) {
                if (uidState != 300) {
                    if (uidState != 400) {
                        if (uidState != 500) {
                            if (uidState != 600) {
                                if (uidState == 700) {
                                    return "UID_STATE_CACHED";
                                }
                                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
                            }
                            return "UID_STATE_BACKGROUND";
                        }
                        return "UID_STATE_FOREGROUND";
                    }
                    return "UID_STATE_FOREGROUND_SERVICE";
                }
                return "UID_STATE_FOREGROUND_SERVICE_LOCATION";
            }
            return "UID_STATE_TOP";
        }
        return "UID_STATE_PERSISTENT";
    }

    public static int parseHistoricalMode(String mode) {
        char c;
        int hashCode = mode.hashCode();
        if (hashCode != 155185419) {
            if (hashCode == 885538210 && mode.equals("HISTORICAL_MODE_ENABLED_PASSIVE")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (mode.equals("HISTORICAL_MODE_ENABLED_ACTIVE")) {
                c = 0;
            }
            c = 65535;
        }
        if (c != 0) {
            return c != 1 ? 0 : 2;
        }
        return 1;
    }

    public static String historicalModeToString(int mode) {
        if (mode != 0) {
            if (mode != 1) {
                if (mode == 2) {
                    return "HISTORICAL_MODE_ENABLED_PASSIVE";
                }
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            }
            return "HISTORICAL_MODE_ENABLED_ACTIVE";
        }
        return "HISTORICAL_MODE_DISABLED";
    }

    private static int getSystemAlertWindowDefault() {
        Context context = ActivityThread.currentApplication();
        if (context == null) {
            return 3;
        }
        PackageManager pm = context.getPackageManager();
        if (!ActivityManager.isLowRamDeviceStatic() || pm.hasSystemFeature(PackageManager.FEATURE_LEANBACK, 0)) {
            return 3;
        }
        return 1;
    }
}
