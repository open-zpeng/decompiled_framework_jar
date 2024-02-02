package android.os;

import android.app.backup.FullBackup;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.hardware.contexthub.V1_0.HostEndPoint;
import android.location.LocationManager;
import android.net.TrafficStats;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiScanner;
import android.os.SystemPropertiesProto;
import android.os.SystemProto;
import android.os.UidProto;
import android.provider.SettingsStringUtil;
import android.provider.Telephony;
import android.telephony.SignalStrength;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.LongSparseArray;
import android.util.MutableBoolean;
import android.util.Pair;
import android.util.Printer;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.os.BatterySipper;
import com.android.internal.os.BatteryStatsHelper;
import com.android.internal.telephony.PhoneConstants;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes2.dex */
public abstract class BatteryStats implements Parcelable {
    private static final String AGGREGATED_WAKELOCK_DATA = "awl";
    public static final int AGGREGATED_WAKE_TYPE_PARTIAL = 20;
    private static final String APK_DATA = "apk";
    private static final String AUDIO_DATA = "aud";
    public static final int AUDIO_TURNED_ON = 15;
    private static final String BATTERY_DATA = "bt";
    private static final String BATTERY_DISCHARGE_DATA = "dc";
    private static final String BATTERY_LEVEL_DATA = "lv";
    private static final int BATTERY_STATS_CHECKIN_VERSION = 9;
    private static final String BLUETOOTH_CONTROLLER_DATA = "ble";
    private static final String BLUETOOTH_MISC_DATA = "blem";
    public static final int BLUETOOTH_SCAN_ON = 19;
    public static final int BLUETOOTH_UNOPTIMIZED_SCAN_ON = 21;
    private static final long BYTES_PER_GB = 1073741824;
    private static final long BYTES_PER_KB = 1024;
    private static final long BYTES_PER_MB = 1048576;
    private static final String CAMERA_DATA = "cam";
    public static final int CAMERA_TURNED_ON = 17;
    private static final String CELLULAR_CONTROLLER_NAME = "Cellular";
    private static final String CHARGE_STEP_DATA = "csd";
    private static final String CHARGE_TIME_REMAIN_DATA = "ctr";
    static final int CHECKIN_VERSION = 32;
    private static final String CPU_DATA = "cpu";
    private static final String CPU_TIMES_AT_FREQ_DATA = "ctf";
    private static final String DATA_CONNECTION_COUNT_DATA = "dcc";
    public static final int DATA_CONNECTION_NONE = 0;
    public static final int DATA_CONNECTION_OTHER = 20;
    private static final String DATA_CONNECTION_TIME_DATA = "dct";
    public static final int DEVICE_IDLE_MODE_DEEP = 2;
    public static final int DEVICE_IDLE_MODE_LIGHT = 1;
    public static final int DEVICE_IDLE_MODE_OFF = 0;
    private static final String DISCHARGE_STEP_DATA = "dsd";
    private static final String DISCHARGE_TIME_REMAIN_DATA = "dtr";
    public static final int DUMP_CHARGED_ONLY = 2;
    public static final int DUMP_DAILY_ONLY = 4;
    public static final int DUMP_DEVICE_WIFI_ONLY = 64;
    public static final int DUMP_HISTORY_ONLY = 8;
    public static final int DUMP_INCLUDE_HISTORY = 16;
    public static final int DUMP_VERBOSE = 32;
    private static final String FLASHLIGHT_DATA = "fla";
    public static final int FLASHLIGHT_TURNED_ON = 16;
    public static final int FOREGROUND_ACTIVITY = 10;
    public static final int FOREGROUND_SERVICE = 22;
    private static final String FOREGROUND_SERVICE_DATA = "fgs";
    public static final int FULL_WIFI_LOCK = 5;
    private static final String GLOBAL_BLUETOOTH_CONTROLLER_DATA = "gble";
    private static final String GLOBAL_CPU_FREQ_DATA = "gcf";
    private static final String GLOBAL_MODEM_CONTROLLER_DATA = "gmcd";
    private static final String GLOBAL_NETWORK_DATA = "gn";
    private static final String GLOBAL_WIFI_CONTROLLER_DATA = "gwfcd";
    private static final String GLOBAL_WIFI_DATA = "gwfl";
    private static final String HISTORY_DATA = "h";
    private static final String HISTORY_STRING_POOL = "hsp";
    public static final int JOB = 14;
    private static final String JOBS_DEFERRED_DATA = "jbd";
    private static final String JOB_COMPLETION_DATA = "jbc";
    private static final String JOB_DATA = "jb";
    private static final String KERNEL_WAKELOCK_DATA = "kwl";
    private static final boolean LOCAL_LOGV = false;
    public static final int MAX_TRACKED_SCREEN_STATE = 4;
    private static final String MISC_DATA = "m";
    private static final String MODEM_CONTROLLER_DATA = "mcd";
    public static final int NETWORK_BT_RX_DATA = 4;
    public static final int NETWORK_BT_TX_DATA = 5;
    private static final String NETWORK_DATA = "nt";
    public static final int NETWORK_MOBILE_BG_RX_DATA = 6;
    public static final int NETWORK_MOBILE_BG_TX_DATA = 7;
    public static final int NETWORK_MOBILE_RX_DATA = 0;
    public static final int NETWORK_MOBILE_TX_DATA = 1;
    public static final int NETWORK_WIFI_BG_RX_DATA = 8;
    public static final int NETWORK_WIFI_BG_TX_DATA = 9;
    public static final int NETWORK_WIFI_RX_DATA = 2;
    public static final int NETWORK_WIFI_TX_DATA = 3;
    private protected static final int NUM_DATA_CONNECTION_TYPES = 21;
    public static final int NUM_NETWORK_ACTIVITY_TYPES = 10;
    private protected static final int NUM_SCREEN_BRIGHTNESS_BINS = 5;
    public static final int NUM_WIFI_SIGNAL_STRENGTH_BINS = 5;
    public static final int NUM_WIFI_STATES = 8;
    public static final int NUM_WIFI_SUPPL_STATES = 13;
    private static final String POWER_USE_ITEM_DATA = "pwi";
    private static final String POWER_USE_SUMMARY_DATA = "pws";
    private static final String PROCESS_DATA = "pr";
    public static final int PROCESS_STATE = 12;
    private static final String RESOURCE_POWER_MANAGER_DATA = "rpm";
    public static final String RESULT_RECEIVER_CONTROLLER_KEY = "controller_activity";
    public static final int SCREEN_BRIGHTNESS_BRIGHT = 4;
    public static final int SCREEN_BRIGHTNESS_DARK = 0;
    private static final String SCREEN_BRIGHTNESS_DATA = "br";
    public static final int SCREEN_BRIGHTNESS_DIM = 1;
    public static final int SCREEN_BRIGHTNESS_LIGHT = 3;
    public static final int SCREEN_BRIGHTNESS_MEDIUM = 2;
    protected static final boolean SCREEN_OFF_RPM_STATS_ENABLED = false;
    public static final int SENSOR = 3;
    private static final String SENSOR_DATA = "sr";
    public static final String SERVICE_NAME = "batterystats";
    private static final String SIGNAL_SCANNING_TIME_DATA = "sst";
    private static final String SIGNAL_STRENGTH_COUNT_DATA = "sgc";
    private static final String SIGNAL_STRENGTH_TIME_DATA = "sgt";
    private static final String STATE_TIME_DATA = "st";
    private protected static final int STATS_CURRENT = 1;
    public static final int STATS_SINCE_CHARGED = 0;
    public static final int STATS_SINCE_UNPLUGGED = 2;
    public static final long STEP_LEVEL_INITIAL_MODE_MASK = 71776119061217280L;
    public static final int STEP_LEVEL_INITIAL_MODE_SHIFT = 48;
    public static final long STEP_LEVEL_LEVEL_MASK = 280375465082880L;
    public static final int STEP_LEVEL_LEVEL_SHIFT = 40;
    public static final int STEP_LEVEL_MODE_DEVICE_IDLE = 8;
    public static final int STEP_LEVEL_MODE_POWER_SAVE = 4;
    public static final int STEP_LEVEL_MODE_SCREEN_STATE = 3;
    public static final long STEP_LEVEL_MODIFIED_MODE_MASK = -72057594037927936L;
    public static final int STEP_LEVEL_MODIFIED_MODE_SHIFT = 56;
    public static final long STEP_LEVEL_TIME_MASK = 1099511627775L;
    public static final int SYNC = 13;
    private static final String SYNC_DATA = "sy";
    private static final String TAG = "BatteryStats";
    private static final String UID_DATA = "uid";
    @VisibleForTesting
    public static final String UID_TIMES_TYPE_ALL = "A";
    private static final String USER_ACTIVITY_DATA = "ua";
    private static final String VERSION_DATA = "vers";
    private static final String VIBRATOR_DATA = "vib";
    public static final int VIBRATOR_ON = 9;
    private static final String VIDEO_DATA = "vid";
    public static final int VIDEO_TURNED_ON = 8;
    private static final String WAKELOCK_DATA = "wl";
    private static final String WAKEUP_ALARM_DATA = "wua";
    private static final String WAKEUP_REASON_DATA = "wr";
    public static final int WAKE_TYPE_DRAW = 18;
    public static final int WAKE_TYPE_FULL = 1;
    private protected static final int WAKE_TYPE_PARTIAL = 0;
    public static final int WAKE_TYPE_WINDOW = 2;
    public static final int WIFI_AGGREGATE_MULTICAST_ENABLED = 23;
    public static final int WIFI_BATCHED_SCAN = 11;
    private static final String WIFI_CONTROLLER_DATA = "wfcd";
    private static final String WIFI_CONTROLLER_NAME = "WiFi";
    private static final String WIFI_DATA = "wfl";
    private static final String WIFI_MULTICAST_DATA = "wmc";
    public static final int WIFI_MULTICAST_ENABLED = 7;
    private static final String WIFI_MULTICAST_TOTAL_DATA = "wmct";
    public static final int WIFI_RUNNING = 4;
    public static final int WIFI_SCAN = 6;
    private static final String WIFI_SIGNAL_STRENGTH_COUNT_DATA = "wsgc";
    private static final String WIFI_SIGNAL_STRENGTH_TIME_DATA = "wsgt";
    private static final String WIFI_STATE_COUNT_DATA = "wsc";
    public static final int WIFI_STATE_OFF = 0;
    public static final int WIFI_STATE_OFF_SCANNING = 1;
    public static final int WIFI_STATE_ON_CONNECTED_P2P = 5;
    public static final int WIFI_STATE_ON_CONNECTED_STA = 4;
    public static final int WIFI_STATE_ON_CONNECTED_STA_P2P = 6;
    public static final int WIFI_STATE_ON_DISCONNECTED = 3;
    public static final int WIFI_STATE_ON_NO_NETWORKS = 2;
    public static final int WIFI_STATE_SOFT_AP = 7;
    private static final String WIFI_STATE_TIME_DATA = "wst";
    public static final int WIFI_SUPPL_STATE_ASSOCIATED = 7;
    public static final int WIFI_SUPPL_STATE_ASSOCIATING = 6;
    public static final int WIFI_SUPPL_STATE_AUTHENTICATING = 5;
    public static final int WIFI_SUPPL_STATE_COMPLETED = 10;
    private static final String WIFI_SUPPL_STATE_COUNT_DATA = "wssc";
    public static final int WIFI_SUPPL_STATE_DISCONNECTED = 1;
    public static final int WIFI_SUPPL_STATE_DORMANT = 11;
    public static final int WIFI_SUPPL_STATE_FOUR_WAY_HANDSHAKE = 8;
    public static final int WIFI_SUPPL_STATE_GROUP_HANDSHAKE = 9;
    public static final int WIFI_SUPPL_STATE_INACTIVE = 3;
    public static final int WIFI_SUPPL_STATE_INTERFACE_DISABLED = 2;
    public static final int WIFI_SUPPL_STATE_INVALID = 0;
    public static final int WIFI_SUPPL_STATE_SCANNING = 4;
    private static final String WIFI_SUPPL_STATE_TIME_DATA = "wsst";
    public static final int WIFI_SUPPL_STATE_UNINITIALIZED = 12;
    private final StringBuilder mFormatBuilder = new StringBuilder(32);
    private final Formatter mFormatter = new Formatter(this.mFormatBuilder);
    private static final String[] STAT_NAMES = {"l", FullBackup.CACHE_TREE_TOKEN, "u"};
    public static final long[] JOB_FRESHNESS_BUCKETS = {3600000, 7200000, 14400000, 28800000, Long.MAX_VALUE};
    static final String[] SCREEN_BRIGHTNESS_NAMES = {"dark", "dim", "medium", "light", "bright"};
    static final String[] SCREEN_BRIGHTNESS_SHORT_NAMES = {"0", "1", "2", "3", "4"};
    static final String[] DATA_CONNECTION_NAMES = {"none", "gprs", "edge", "umts", "cdma", "evdo_0", "evdo_A", "1xrtt", "hsdpa", "hsupa", "hspa", "iden", "evdo_b", "lte", "ehrpd", "hspap", "gsm", "td_scdma", "iwlan", "lte_ca", "other"};
    static final String[] WIFI_SUPPL_STATE_NAMES = {"invalid", "disconn", "disabled", "inactive", "scanning", "authenticating", "associating", "associated", "4-way-handshake", "group-handshake", "completed", "dormant", "uninit"};
    static final String[] WIFI_SUPPL_STATE_SHORT_NAMES = {"inv", "dsc", "dis", "inact", "scan", "auth", "ascing", "asced", "4-way", WifiConfiguration.GroupCipher.varName, "compl", "dorm", "uninit"};
    public static final BitDescription[] HISTORY_STATE_DESCRIPTIONS = {new BitDescription(Integer.MIN_VALUE, "running", FullBackup.ROOT_TREE_TOKEN), new BitDescription(1073741824, "wake_lock", "w"), new BitDescription(8388608, Context.SENSOR_SERVICE, "s"), new BitDescription(536870912, LocationManager.GPS_PROVIDER, "g"), new BitDescription(268435456, "wifi_full_lock", "Wl"), new BitDescription(134217728, "wifi_scan", "Ws"), new BitDescription(65536, "wifi_multicast", "Wm"), new BitDescription(67108864, "wifi_radio", "Wr"), new BitDescription(33554432, "mobile_radio", "Pr"), new BitDescription(2097152, "phone_scanning", "Psc"), new BitDescription(4194304, "audio", FullBackup.APK_TREE_TOKEN), new BitDescription(1048576, "screen", "S"), new BitDescription(524288, BatteryManager.EXTRA_PLUGGED, "BP"), new BitDescription(262144, "screen_doze", "Sd"), new BitDescription(HistoryItem.STATE_DATA_CONNECTION_MASK, 9, "data_conn", "Pcn", DATA_CONNECTION_NAMES, DATA_CONNECTION_NAMES), new BitDescription(448, 6, "phone_state", "Pst", new String[]{"in", "out", PhoneConstants.APN_TYPE_EMERGENCY, "off"}, new String[]{"in", "out", "em", "off"}), new BitDescription(56, 3, "phone_signal_strength", "Pss", SignalStrength.SIGNAL_STRENGTH_NAMES, new String[]{"0", "1", "2", "3", "4"}), new BitDescription(7, 0, "brightness", "Sb", SCREEN_BRIGHTNESS_NAMES, SCREEN_BRIGHTNESS_SHORT_NAMES)};
    public static final BitDescription[] HISTORY_STATE2_DESCRIPTIONS = {new BitDescription(Integer.MIN_VALUE, "power_save", "ps"), new BitDescription(1073741824, "video", Telephony.BaseMmsColumns.MMS_VERSION), new BitDescription(536870912, "wifi_running", "Ww"), new BitDescription(268435456, "wifi", "W"), new BitDescription(134217728, "flashlight", "fl"), new BitDescription(HistoryItem.STATE2_DEVICE_IDLE_MASK, 25, "device_idle", "di", new String[]{"off", "light", "full", "???"}, new String[]{"off", "light", "full", "???"}), new BitDescription(16777216, "charging", "ch"), new BitDescription(262144, "usb_data", "Ud"), new BitDescription(8388608, "phone_in_call", "Pcl"), new BitDescription(4194304, "bluetooth", "b"), new BitDescription(112, 4, "wifi_signal_strength", "Wss", new String[]{"0", "1", "2", "3", "4"}, new String[]{"0", "1", "2", "3", "4"}), new BitDescription(15, 0, "wifi_suppl", "Wsp", WIFI_SUPPL_STATE_NAMES, WIFI_SUPPL_STATE_SHORT_NAMES), new BitDescription(2097152, Context.CAMERA_SERVICE, "ca"), new BitDescription(1048576, "ble_scan", "bles"), new BitDescription(524288, "cellular_high_tx_power", "Chtp"), new BitDescription(128, 7, "gps_signal_quality", "Gss", new String[]{"poor", "good"}, new String[]{"poor", "good"})};
    private static final String FOREGROUND_ACTIVITY_DATA = "fg";
    public static final String[] HISTORY_EVENT_NAMES = {"null", "proc", FOREGROUND_ACTIVITY_DATA, "top", "sync", "wake_lock_in", "job", "user", "userfg", "conn", "active", "pkginst", "pkgunin", "alarm", Context.STATS_MANAGER, "pkginactive", "pkgactive", "tmpwhitelist", "screenwake", "wakeupap", "longwake", "est_capacity"};
    public static final String[] HISTORY_EVENT_CHECKIN_NAMES = {"Enl", "Epr", "Efg", "Etp", "Esy", "Ewl", "Ejb", "Eur", "Euf", "Ecn", "Eac", "Epi", "Epu", "Eal", "Est", "Eai", "Eaa", "Etw", "Esw", "Ewa", "Elw", "Eec"};
    private static final IntToString sUidToString = new IntToString() { // from class: android.os.-$$Lambda$IyvVQC-0mKtsfXbnO0kDL64hrk0
        @Override // android.os.BatteryStats.IntToString
        public final String applyAsString(int i) {
            return UserHandle.formatUid(i);
        }
    };
    private static final IntToString sIntToString = new IntToString() { // from class: android.os.-$$Lambda$BatteryStats$q1UvBdLgHRZVzc68BxdksTmbuCw
        @Override // android.os.BatteryStats.IntToString
        public final String applyAsString(int i) {
            String num;
            num = Integer.toString(i);
            return num;
        }
    };
    public static final IntToString[] HISTORY_EVENT_INT_FORMATTERS = {sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sIntToString};
    static final String[] WIFI_STATE_NAMES = {"off", "scanning", "no_net", "disconn", "sta", "p2p", "sta_p2p", "soft_ap"};
    public static final int[] STEP_LEVEL_MODES_OF_INTEREST = {7, 15, 11, 7, 7, 7, 7, 7, 15, 11};
    public static final int[] STEP_LEVEL_MODE_VALUES = {0, 4, 8, 1, 5, 2, 6, 3, 7, 11};
    public static final String[] STEP_LEVEL_MODE_LABELS = {"screen off", "screen off power save", "screen off device idle", "screen on", "screen on power save", "screen doze", "screen doze power save", "screen doze-suspend", "screen doze-suspend power save", "screen doze-suspend device idle"};

    /* loaded from: classes2.dex */
    public static abstract class ControllerActivityCounter {
        public abstract synchronized LongCounter getIdleTimeCounter();

        public abstract synchronized LongCounter getPowerCounter();

        public abstract synchronized LongCounter getRxTimeCounter();

        public abstract synchronized LongCounter getScanTimeCounter();

        public abstract synchronized LongCounter getSleepTimeCounter();

        public abstract synchronized LongCounter[] getTxTimeCounters();
    }

    /* loaded from: classes2.dex */
    public static abstract class Counter {
        private protected abstract int getCountLocked(int i);

        public abstract synchronized void logState(Printer printer, String str);
    }

    /* loaded from: classes2.dex */
    public static final class DailyItem {
        public LevelStepTracker mChargeSteps;
        public LevelStepTracker mDischargeSteps;
        public long mEndTime;
        public ArrayList<PackageChange> mPackageChanges;
        public long mStartTime;
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface IntToString {
        synchronized String applyAsString(int i);
    }

    /* loaded from: classes2.dex */
    public static abstract class LongCounter {
        public abstract synchronized long getCountLocked(int i);

        public abstract synchronized void logState(Printer printer, String str);
    }

    /* loaded from: classes2.dex */
    public static abstract class LongCounterArray {
        public abstract synchronized long[] getCountsLocked(int i);

        public abstract synchronized void logState(Printer printer, String str);
    }

    /* loaded from: classes2.dex */
    public static final class PackageChange {
        public String mPackageName;
        public boolean mUpdate;
        public long mVersionCode;
    }

    public abstract synchronized void commitCurrentHistoryBatchLocked();

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long computeBatteryRealtime(long j, int i);

    public abstract synchronized long computeBatteryScreenOffRealtime(long j, int i);

    public abstract synchronized long computeBatteryScreenOffUptime(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long computeBatteryTimeRemaining(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long computeBatteryUptime(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long computeChargeTimeRemaining(long j);

    public abstract synchronized long computeRealtime(long j, int i);

    public abstract synchronized long computeUptime(long j, int i);

    public abstract synchronized void finishIteratingHistoryLocked();

    public abstract synchronized void finishIteratingOldHistoryLocked();

    public abstract synchronized long getBatteryRealtime(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long getBatteryUptime(long j);

    public abstract synchronized ControllerActivityCounter getBluetoothControllerActivity();

    public abstract synchronized long getBluetoothScanTime(long j, int i);

    public abstract synchronized long getCameraOnTime(long j, int i);

    public abstract synchronized LevelStepTracker getChargeLevelStepTracker();

    public abstract synchronized long[] getCpuFreqs();

    public abstract synchronized long getCurrentDailyStartTime();

    public abstract synchronized LevelStepTracker getDailyChargeLevelStepTracker();

    public abstract synchronized LevelStepTracker getDailyDischargeLevelStepTracker();

    public abstract synchronized DailyItem getDailyItemLocked(int i);

    public abstract synchronized ArrayList<PackageChange> getDailyPackageChanges();

    public abstract synchronized int getDeviceIdleModeCount(int i, int i2);

    public abstract synchronized long getDeviceIdleModeTime(int i, long j, int i2);

    public abstract synchronized int getDeviceIdlingCount(int i, int i2);

    public abstract synchronized long getDeviceIdlingTime(int i, long j, int i2);

    public abstract synchronized int getDischargeAmount(int i);

    public abstract synchronized int getDischargeAmountScreenDoze();

    public abstract synchronized int getDischargeAmountScreenDozeSinceCharge();

    public abstract synchronized int getDischargeAmountScreenOff();

    public abstract synchronized int getDischargeAmountScreenOffSinceCharge();

    public abstract synchronized int getDischargeAmountScreenOn();

    public abstract synchronized int getDischargeAmountScreenOnSinceCharge();

    public abstract synchronized int getDischargeCurrentLevel();

    public abstract synchronized LevelStepTracker getDischargeLevelStepTracker();

    public abstract synchronized int getDischargeStartLevel();

    public abstract synchronized String getEndPlatformVersion();

    public abstract synchronized int getEstimatedBatteryCapacity();

    public abstract synchronized long getFlashlightOnCount(int i);

    public abstract synchronized long getFlashlightOnTime(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long getGlobalWifiRunningTime(long j, int i);

    public abstract synchronized long getGpsBatteryDrainMaMs();

    public abstract synchronized long getGpsSignalQualityTime(int i, long j, int i2);

    public abstract synchronized int getHighDischargeAmountSinceCharge();

    public abstract synchronized long getHistoryBaseTime();

    public abstract synchronized int getHistoryStringPoolBytes();

    public abstract synchronized int getHistoryStringPoolSize();

    public abstract synchronized String getHistoryTagPoolString(int i);

    public abstract synchronized int getHistoryTagPoolUid(int i);

    public abstract synchronized int getHistoryTotalSize();

    public abstract synchronized int getHistoryUsedSize();

    public abstract synchronized long getInteractiveTime(long j, int i);

    public abstract synchronized boolean getIsOnBattery();

    public abstract synchronized LongSparseArray<? extends Timer> getKernelMemoryStats();

    public abstract synchronized Map<String, ? extends Timer> getKernelWakelockStats();

    public abstract synchronized long getLongestDeviceIdleModeTime(int i);

    public abstract synchronized int getLowDischargeAmountSinceCharge();

    public abstract synchronized int getMaxLearnedBatteryCapacity();

    public abstract synchronized int getMinLearnedBatteryCapacity();

    public abstract synchronized long getMobileRadioActiveAdjustedTime(int i);

    public abstract synchronized int getMobileRadioActiveCount(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long getMobileRadioActiveTime(long j, int i);

    public abstract synchronized int getMobileRadioActiveUnknownCount(int i);

    public abstract synchronized long getMobileRadioActiveUnknownTime(int i);

    public abstract synchronized ControllerActivityCounter getModemControllerActivity();

    private protected abstract long getNetworkActivityBytes(int i, int i2);

    public abstract synchronized long getNetworkActivityPackets(int i, int i2);

    private protected abstract boolean getNextHistoryLocked(HistoryItem historyItem);

    public abstract synchronized long getNextMaxDailyDeadline();

    public abstract synchronized long getNextMinDailyDeadline();

    public abstract synchronized boolean getNextOldHistoryLocked(HistoryItem historyItem);

    public abstract synchronized int getNumConnectivityChange(int i);

    public abstract synchronized int getParcelVersion();

    public abstract synchronized int getPhoneDataConnectionCount(int i, int i2);

    public abstract synchronized long getPhoneDataConnectionTime(int i, long j, int i2);

    public abstract synchronized Timer getPhoneDataConnectionTimer(int i);

    public abstract synchronized int getPhoneOnCount(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long getPhoneOnTime(long j, int i);

    public abstract synchronized long getPhoneSignalScanningTime(long j, int i);

    public abstract synchronized Timer getPhoneSignalScanningTimer();

    public abstract synchronized int getPhoneSignalStrengthCount(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long getPhoneSignalStrengthTime(int i, long j, int i2);

    protected abstract synchronized Timer getPhoneSignalStrengthTimer(int i);

    public abstract synchronized int getPowerSaveModeEnabledCount(int i);

    public abstract synchronized long getPowerSaveModeEnabledTime(long j, int i);

    public abstract synchronized Map<String, ? extends Timer> getRpmStats();

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long getScreenBrightnessTime(int i, long j, int i2);

    public abstract synchronized Timer getScreenBrightnessTimer(int i);

    public abstract synchronized int getScreenDozeCount(int i);

    public abstract synchronized long getScreenDozeTime(long j, int i);

    public abstract synchronized Map<String, ? extends Timer> getScreenOffRpmStats();

    public abstract synchronized int getScreenOnCount(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract long getScreenOnTime(long j, int i);

    public abstract synchronized long getStartClockTime();

    public abstract synchronized int getStartCount();

    public abstract synchronized String getStartPlatformVersion();

    public abstract synchronized long getUahDischarge(int i);

    public abstract synchronized long getUahDischargeDeepDoze(int i);

    public abstract synchronized long getUahDischargeLightDoze(int i);

    public abstract synchronized long getUahDischargeScreenDoze(int i);

    public abstract synchronized long getUahDischargeScreenOff(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract SparseArray<? extends Uid> getUidStats();

    public abstract synchronized Map<String, ? extends Timer> getWakeupReasonStats();

    public abstract synchronized long getWifiActiveTime(long j, int i);

    public abstract synchronized ControllerActivityCounter getWifiControllerActivity();

    public abstract synchronized int getWifiMulticastWakelockCount(int i);

    public abstract synchronized long getWifiMulticastWakelockTime(long j, int i);

    private protected abstract long getWifiOnTime(long j, int i);

    public abstract synchronized int getWifiSignalStrengthCount(int i, int i2);

    public abstract synchronized long getWifiSignalStrengthTime(int i, long j, int i2);

    public abstract synchronized Timer getWifiSignalStrengthTimer(int i);

    public abstract synchronized int getWifiStateCount(int i, int i2);

    public abstract synchronized long getWifiStateTime(int i, long j, int i2);

    public abstract synchronized Timer getWifiStateTimer(int i);

    public abstract synchronized int getWifiSupplStateCount(int i, int i2);

    public abstract synchronized long getWifiSupplStateTime(int i, long j, int i2);

    public abstract synchronized Timer getWifiSupplStateTimer(int i);

    public abstract synchronized boolean hasBluetoothActivityReporting();

    public abstract synchronized boolean hasModemActivityReporting();

    public abstract synchronized boolean hasWifiActivityReporting();

    private protected abstract boolean startIteratingHistoryLocked();

    public abstract synchronized boolean startIteratingOldHistoryLocked();

    public abstract synchronized void writeToParcelWithoutUids(Parcel parcel, int i);

    /* loaded from: classes2.dex */
    public static abstract class Timer {
        private protected abstract int getCountLocked(int i);

        public abstract synchronized long getTimeSinceMarkLocked(long j);

        /* JADX INFO: Access modifiers changed from: private */
        public abstract long getTotalTimeLocked(long j, int i);

        public abstract synchronized void logState(Printer printer, String str);

        public synchronized long getMaxDurationMsLocked(long elapsedRealtimeMs) {
            return -1L;
        }

        public synchronized long getCurrentDurationMsLocked(long elapsedRealtimeMs) {
            return -1L;
        }

        public synchronized long getTotalDurationMsLocked(long elapsedRealtimeMs) {
            return -1L;
        }

        public synchronized Timer getSubTimer() {
            return null;
        }

        public synchronized boolean isRunningLocked() {
            return false;
        }
    }

    public static synchronized int mapToInternalProcessState(int procState) {
        if (procState == 19) {
            return 19;
        }
        if (procState == 2) {
            return 0;
        }
        if (procState == 3) {
            return 1;
        }
        if (procState <= 5) {
            return 2;
        }
        if (procState <= 10) {
            return 3;
        }
        if (procState <= 11) {
            return 4;
        }
        if (procState <= 12) {
            return 5;
        }
        return 6;
    }

    /* loaded from: classes2.dex */
    public static abstract class Uid {
        public static final int NUM_PROCESS_STATE = 7;
        public static final int NUM_USER_ACTIVITY_TYPES = 4;
        public static final int NUM_WIFI_BATCHED_SCAN_BINS = 5;
        public static final int PROCESS_STATE_BACKGROUND = 3;
        public static final int PROCESS_STATE_CACHED = 6;
        public static final int PROCESS_STATE_FOREGROUND = 2;
        public static final int PROCESS_STATE_FOREGROUND_SERVICE = 1;
        public static final int PROCESS_STATE_HEAVY_WEIGHT = 5;
        public static final int PROCESS_STATE_TOP = 0;
        public static final int PROCESS_STATE_TOP_SLEEPING = 4;
        static final String[] PROCESS_STATE_NAMES = {"Top", "Fg Service", "Foreground", "Background", "Top Sleeping", "Heavy Weight", "Cached"};
        @VisibleForTesting
        public static final String[] UID_PROCESS_TYPES = {"T", "FS", "F", "B", "TS", "HW", "C"};
        public static final int[] CRITICAL_PROC_STATES = {0, 1, 2};
        static final String[] USER_ACTIVITY_TYPES = {"other", "button", "touch", Context.ACCESSIBILITY_SERVICE};

        /* loaded from: classes2.dex */
        public static abstract class Pkg {

            /* loaded from: classes2.dex */
            public static abstract class Serv {
                private protected abstract int getLaunches(int i);

                private protected abstract long getStartTime(long j, int i);

                private protected abstract int getStarts(int i);
            }

            private protected abstract ArrayMap<String, ? extends Serv> getServiceStats();

            private protected abstract ArrayMap<String, ? extends Counter> getWakeupAlarmStats();
        }

        /* loaded from: classes2.dex */
        public static abstract class Proc {

            /* loaded from: classes2.dex */
            public static class ExcessivePower {
                public static final int TYPE_CPU = 2;
                public static final int TYPE_WAKE = 1;
                private protected long overTime;
                private protected int type;
                private protected long usedTime;
            }

            private protected abstract int countExcessivePowers();

            private protected abstract ExcessivePower getExcessivePower(int i);

            /* JADX INFO: Access modifiers changed from: private */
            public abstract long getForegroundTime(int i);

            public abstract synchronized int getNumAnrs(int i);

            public abstract synchronized int getNumCrashes(int i);

            private protected abstract int getStarts(int i);

            /* JADX INFO: Access modifiers changed from: private */
            public abstract long getSystemTime(int i);

            /* JADX INFO: Access modifiers changed from: private */
            public abstract long getUserTime(int i);

            public abstract synchronized boolean isActive();
        }

        /* loaded from: classes2.dex */
        public static abstract class Sensor {
            private protected static final int GPS = -10000;

            private protected abstract int getHandle();

            public abstract synchronized Timer getSensorBackgroundTime();

            /* JADX INFO: Access modifiers changed from: private */
            public abstract Timer getSensorTime();
        }

        /* loaded from: classes2.dex */
        public static abstract class Wakelock {
            /* JADX INFO: Access modifiers changed from: private */
            public abstract Timer getWakeTime(int i);
        }

        public abstract synchronized Timer getAggregatedPartialWakelockTimer();

        /* JADX INFO: Access modifiers changed from: private */
        public abstract Timer getAudioTurnedOnTimer();

        public abstract synchronized ControllerActivityCounter getBluetoothControllerActivity();

        public abstract synchronized Timer getBluetoothScanBackgroundTimer();

        public abstract synchronized Counter getBluetoothScanResultBgCounter();

        public abstract synchronized Counter getBluetoothScanResultCounter();

        public abstract synchronized Timer getBluetoothScanTimer();

        public abstract synchronized Timer getBluetoothUnoptimizedScanBackgroundTimer();

        public abstract synchronized Timer getBluetoothUnoptimizedScanTimer();

        public abstract synchronized Timer getCameraTurnedOnTimer();

        public abstract synchronized long getCpuActiveTime();

        public abstract synchronized long[] getCpuClusterTimes();

        public abstract synchronized long[] getCpuFreqTimes(int i);

        public abstract synchronized long[] getCpuFreqTimes(int i, int i2);

        public abstract synchronized void getDeferredJobsCheckinLineLocked(StringBuilder sb, int i);

        public abstract synchronized void getDeferredJobsLineLocked(StringBuilder sb, int i);

        public abstract synchronized Timer getFlashlightTurnedOnTimer();

        public abstract synchronized Timer getForegroundActivityTimer();

        public abstract synchronized Timer getForegroundServiceTimer();

        private protected abstract long getFullWifiLockTime(long j, int i);

        public abstract synchronized ArrayMap<String, SparseIntArray> getJobCompletionStats();

        public abstract synchronized ArrayMap<String, ? extends Timer> getJobStats();

        public abstract synchronized int getMobileRadioActiveCount(int i);

        /* JADX INFO: Access modifiers changed from: private */
        public abstract long getMobileRadioActiveTime(int i);

        public abstract synchronized long getMobileRadioApWakeupCount(int i);

        public abstract synchronized ControllerActivityCounter getModemControllerActivity();

        public abstract synchronized Timer getMulticastWakelockStats();

        /* JADX INFO: Access modifiers changed from: private */
        public abstract long getNetworkActivityBytes(int i, int i2);

        public abstract synchronized long getNetworkActivityPackets(int i, int i2);

        private protected abstract ArrayMap<String, ? extends Pkg> getPackageStats();

        public abstract synchronized SparseArray<? extends Pid> getPidStats();

        public abstract synchronized long getProcessStateTime(int i, long j, int i2);

        public abstract synchronized Timer getProcessStateTimer(int i);

        /* JADX INFO: Access modifiers changed from: private */
        public abstract ArrayMap<String, ? extends Proc> getProcessStats();

        public abstract synchronized long[] getScreenOffCpuFreqTimes(int i);

        public abstract synchronized long[] getScreenOffCpuFreqTimes(int i, int i2);

        /* JADX INFO: Access modifiers changed from: private */
        public abstract SparseArray<? extends Sensor> getSensorStats();

        public abstract synchronized ArrayMap<String, ? extends Timer> getSyncStats();

        public abstract synchronized long getSystemCpuTimeUs(int i);

        public abstract synchronized long getTimeAtCpuSpeed(int i, int i2, int i3);

        /* JADX INFO: Access modifiers changed from: private */
        public abstract int getUid();

        public abstract synchronized int getUserActivityCount(int i, int i2);

        public abstract synchronized long getUserCpuTimeUs(int i);

        public abstract synchronized Timer getVibratorOnTimer();

        /* JADX INFO: Access modifiers changed from: private */
        public abstract Timer getVideoTurnedOnTimer();

        /* JADX INFO: Access modifiers changed from: private */
        public abstract ArrayMap<String, ? extends Wakelock> getWakelockStats();

        public abstract synchronized int getWifiBatchedScanCount(int i, int i2);

        /* JADX INFO: Access modifiers changed from: private */
        public abstract long getWifiBatchedScanTime(int i, long j, int i2);

        public abstract synchronized ControllerActivityCounter getWifiControllerActivity();

        private protected abstract long getWifiMulticastTime(long j, int i);

        public abstract synchronized long getWifiRadioApWakeupCount(int i);

        /* JADX INFO: Access modifiers changed from: private */
        public abstract long getWifiRunningTime(long j, int i);

        public abstract synchronized long getWifiScanActualTime(long j);

        public abstract synchronized int getWifiScanBackgroundCount(int i);

        public abstract synchronized long getWifiScanBackgroundTime(long j);

        public abstract synchronized Timer getWifiScanBackgroundTimer();

        public abstract synchronized int getWifiScanCount(int i);

        /* JADX INFO: Access modifiers changed from: private */
        public abstract long getWifiScanTime(long j, int i);

        public abstract synchronized Timer getWifiScanTimer();

        public abstract synchronized boolean hasNetworkActivity();

        public abstract synchronized boolean hasUserActivity();

        public abstract synchronized void noteActivityPausedLocked(long j);

        public abstract synchronized void noteActivityResumedLocked(long j);

        public abstract synchronized void noteFullWifiLockAcquiredLocked(long j);

        public abstract synchronized void noteFullWifiLockReleasedLocked(long j);

        public abstract synchronized void noteUserActivityLocked(int i);

        public abstract synchronized void noteWifiBatchedScanStartedLocked(int i, long j);

        public abstract synchronized void noteWifiBatchedScanStoppedLocked(long j);

        public abstract synchronized void noteWifiMulticastDisabledLocked(long j);

        public abstract synchronized void noteWifiMulticastEnabledLocked(long j);

        public abstract synchronized void noteWifiRunningLocked(long j);

        public abstract synchronized void noteWifiScanStartedLocked(long j);

        public abstract synchronized void noteWifiScanStoppedLocked(long j);

        public abstract synchronized void noteWifiStoppedLocked(long j);

        /* loaded from: classes2.dex */
        public class Pid {
            public int mWakeNesting;
            public long mWakeStartMs;
            public long mWakeSumMs;

            public Pid() {
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class LevelStepTracker {
        public long mLastStepTime = -1;
        public int mNumStepDurations;
        public final long[] mStepDurations;

        public synchronized LevelStepTracker(int maxLevelSteps) {
            this.mStepDurations = new long[maxLevelSteps];
        }

        public synchronized LevelStepTracker(int numSteps, long[] steps) {
            this.mNumStepDurations = numSteps;
            this.mStepDurations = new long[numSteps];
            System.arraycopy(steps, 0, this.mStepDurations, 0, numSteps);
        }

        public synchronized long getDurationAt(int index) {
            return this.mStepDurations[index] & BatteryStats.STEP_LEVEL_TIME_MASK;
        }

        public synchronized int getLevelAt(int index) {
            return (int) ((this.mStepDurations[index] & BatteryStats.STEP_LEVEL_LEVEL_MASK) >> 40);
        }

        public synchronized int getInitModeAt(int index) {
            return (int) ((this.mStepDurations[index] & BatteryStats.STEP_LEVEL_INITIAL_MODE_MASK) >> 48);
        }

        public synchronized int getModModeAt(int index) {
            return (int) ((this.mStepDurations[index] & BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK) >> 56);
        }

        private synchronized void appendHex(long val, int topOffset, StringBuilder out) {
            boolean hasData = false;
            while (topOffset >= 0) {
                int digit = (int) ((val >> topOffset) & 15);
                topOffset -= 4;
                if (hasData || digit != 0) {
                    hasData = true;
                    if (digit >= 0 && digit <= 9) {
                        out.append((char) (48 + digit));
                    } else {
                        out.append((char) ((97 + digit) - 10));
                    }
                }
            }
        }

        public synchronized void encodeEntryAt(int index, StringBuilder out) {
            long item = this.mStepDurations[index];
            long duration = BatteryStats.STEP_LEVEL_TIME_MASK & item;
            int level = (int) ((BatteryStats.STEP_LEVEL_LEVEL_MASK & item) >> 40);
            int initMode = (int) ((BatteryStats.STEP_LEVEL_INITIAL_MODE_MASK & item) >> 48);
            int modMode = (int) ((BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK & item) >> 56);
            switch ((initMode & 3) + 1) {
                case 1:
                    out.append('f');
                    break;
                case 2:
                    out.append('o');
                    break;
                case 3:
                    out.append('d');
                    break;
                case 4:
                    out.append('z');
                    break;
            }
            if ((initMode & 4) != 0) {
                out.append('p');
            }
            if ((initMode & 8) != 0) {
                out.append('i');
            }
            switch ((modMode & 3) + 1) {
                case 1:
                    out.append('F');
                    break;
                case 2:
                    out.append('O');
                    break;
                case 3:
                    out.append('D');
                    break;
                case 4:
                    out.append('Z');
                    break;
            }
            if ((modMode & 4) != 0) {
                out.append('P');
            }
            if ((modMode & 8) != 0) {
                out.append('I');
            }
            out.append('-');
            appendHex(level, 4, out);
            out.append('-');
            appendHex(duration, 36, out);
        }

        public synchronized void decodeEntryAt(int index, String value) {
            char c;
            char c2;
            long level;
            char c3;
            char c4;
            int N = value.length();
            int i = 0;
            long out = 0;
            while (true) {
                c = '-';
                if (i < N && (c4 = value.charAt(i)) != '-') {
                    i++;
                    switch (c4) {
                        case 'D':
                            out |= 144115188075855872L;
                            break;
                        case 'F':
                            out |= 0;
                            break;
                        case 'I':
                            out |= 576460752303423488L;
                            break;
                        case 'O':
                            out |= 72057594037927936L;
                            break;
                        case 'P':
                            out |= 288230376151711744L;
                            break;
                        case 'Z':
                            out |= 216172782113783808L;
                            break;
                        case 'd':
                            out |= 562949953421312L;
                            break;
                        case 'f':
                            out |= 0;
                            break;
                        case 'i':
                            out |= 2251799813685248L;
                            break;
                        case 'o':
                            out |= 281474976710656L;
                            break;
                        case 'p':
                            out |= TrafficStats.PB_IN_BYTES;
                            break;
                        case 'z':
                            out |= 844424930131968L;
                            break;
                    }
                }
            }
            int i2 = i + 1;
            long level2 = 0;
            while (true) {
                c2 = '0';
                if (i2 < N && (c3 = value.charAt(i2)) != '-') {
                    i2++;
                    level2 <<= 4;
                    if (c3 >= '0' && c3 <= '9') {
                        level2 += c3 - '0';
                    } else if (c3 >= 'a' && c3 <= 'f') {
                        level2 += (c3 - 'a') + 10;
                    } else if (c3 >= 'A' && c3 <= 'F') {
                        level2 += (c3 - 'A') + 10;
                    }
                }
            }
            int i3 = i2 + 1;
            long out2 = out | ((level2 << 40) & BatteryStats.STEP_LEVEL_LEVEL_MASK);
            long duration = 0;
            while (i3 < N) {
                char c5 = value.charAt(i3);
                if (c5 != c) {
                    i3++;
                    duration <<= 4;
                    if (c5 >= c2 && c5 <= '9') {
                        level = level2;
                        duration += c5 - '0';
                    } else {
                        level = level2;
                        if (c5 >= 'a' && c5 <= 'f') {
                            duration += (c5 - 'a') + 10;
                        } else if (c5 >= 'A' && c5 <= 'F') {
                            duration += (c5 - 'A') + 10;
                        }
                    }
                    level2 = level;
                    c2 = '0';
                    c = '-';
                } else {
                    this.mStepDurations[index] = (duration & BatteryStats.STEP_LEVEL_TIME_MASK) | out2;
                }
            }
            this.mStepDurations[index] = (duration & BatteryStats.STEP_LEVEL_TIME_MASK) | out2;
        }

        public synchronized void init() {
            this.mLastStepTime = -1L;
            this.mNumStepDurations = 0;
        }

        public synchronized void clearTime() {
            this.mLastStepTime = -1L;
        }

        public synchronized long computeTimePerLevel() {
            long[] steps = this.mStepDurations;
            int numSteps = this.mNumStepDurations;
            if (numSteps <= 0) {
                return -1L;
            }
            long total = 0;
            for (int i = 0; i < numSteps; i++) {
                total += steps[i] & BatteryStats.STEP_LEVEL_TIME_MASK;
            }
            return total / numSteps;
        }

        public synchronized long computeTimeEstimate(long modesOfInterest, long modeValues, int[] outNumOfInterest) {
            long[] steps = this.mStepDurations;
            int count = this.mNumStepDurations;
            if (count <= 0) {
                return -1L;
            }
            int numOfInterest = 0;
            long total = 0;
            for (int i = 0; i < count; i++) {
                long initMode = (steps[i] & BatteryStats.STEP_LEVEL_INITIAL_MODE_MASK) >> 48;
                long modMode = (steps[i] & BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK) >> 56;
                if ((modMode & modesOfInterest) == 0 && (initMode & modesOfInterest) == modeValues) {
                    numOfInterest++;
                    total += steps[i] & BatteryStats.STEP_LEVEL_TIME_MASK;
                }
            }
            if (numOfInterest <= 0) {
                return -1L;
            }
            if (outNumOfInterest != null) {
                outNumOfInterest[0] = numOfInterest;
            }
            return (total / numOfInterest) * 100;
        }

        public synchronized void addLevelSteps(int numStepLevels, long modeBits, long elapsedRealtime) {
            int stepCount = this.mNumStepDurations;
            long lastStepTime = this.mLastStepTime;
            if (lastStepTime >= 0 && numStepLevels > 0) {
                long[] steps = this.mStepDurations;
                long duration = elapsedRealtime - lastStepTime;
                long duration2 = duration;
                for (int i = 0; i < numStepLevels; i++) {
                    System.arraycopy(steps, 0, steps, 1, steps.length - 1);
                    long thisDuration = duration2 / (numStepLevels - i);
                    duration2 -= thisDuration;
                    if (thisDuration > BatteryStats.STEP_LEVEL_TIME_MASK) {
                        thisDuration = BatteryStats.STEP_LEVEL_TIME_MASK;
                    }
                    steps[0] = thisDuration | modeBits;
                }
                stepCount += numStepLevels;
                if (stepCount > steps.length) {
                    stepCount = steps.length;
                }
            }
            this.mNumStepDurations = stepCount;
            this.mLastStepTime = elapsedRealtime;
        }

        public synchronized void readFromParcel(Parcel in) {
            int N = in.readInt();
            if (N > this.mStepDurations.length) {
                throw new ParcelFormatException("more step durations than available: " + N);
            }
            this.mNumStepDurations = N;
            for (int i = 0; i < N; i++) {
                this.mStepDurations[i] = in.readLong();
            }
        }

        public synchronized void writeToParcel(Parcel out) {
            int N = this.mNumStepDurations;
            out.writeInt(N);
            for (int i = 0; i < N; i++) {
                out.writeLong(this.mStepDurations[i]);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class HistoryTag {
        public int poolIdx;
        public String string;
        public int uid;

        public synchronized void setTo(HistoryTag o) {
            this.string = o.string;
            this.uid = o.uid;
            this.poolIdx = o.poolIdx;
        }

        public synchronized void setTo(String _string, int _uid) {
            this.string = _string;
            this.uid = _uid;
            this.poolIdx = -1;
        }

        public synchronized void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.string);
            dest.writeInt(this.uid);
        }

        public synchronized void readFromParcel(Parcel src) {
            this.string = src.readString();
            this.uid = src.readInt();
            this.poolIdx = -1;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            HistoryTag that = (HistoryTag) o;
            if (this.uid == that.uid && this.string.equals(that.string)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result = this.string.hashCode();
            return (31 * result) + this.uid;
        }
    }

    /* loaded from: classes2.dex */
    public static final class HistoryStepDetails {
        public int appCpuSTime1;
        public int appCpuSTime2;
        public int appCpuSTime3;
        public int appCpuUTime1;
        public int appCpuUTime2;
        public int appCpuUTime3;
        public int appCpuUid1;
        public int appCpuUid2;
        public int appCpuUid3;
        public int statIOWaitTime;
        public int statIdlTime;
        public int statIrqTime;
        public String statPlatformIdleState;
        public int statSoftIrqTime;
        public String statSubsystemPowerState;
        public int statSystemTime;
        public int statUserTime;
        public int systemTime;
        public int userTime;

        public synchronized HistoryStepDetails() {
            clear();
        }

        public synchronized void clear() {
            this.systemTime = 0;
            this.userTime = 0;
            this.appCpuUid3 = -1;
            this.appCpuUid2 = -1;
            this.appCpuUid1 = -1;
            this.appCpuSTime3 = 0;
            this.appCpuUTime3 = 0;
            this.appCpuSTime2 = 0;
            this.appCpuUTime2 = 0;
            this.appCpuSTime1 = 0;
            this.appCpuUTime1 = 0;
        }

        public synchronized void writeToParcel(Parcel out) {
            out.writeInt(this.userTime);
            out.writeInt(this.systemTime);
            out.writeInt(this.appCpuUid1);
            out.writeInt(this.appCpuUTime1);
            out.writeInt(this.appCpuSTime1);
            out.writeInt(this.appCpuUid2);
            out.writeInt(this.appCpuUTime2);
            out.writeInt(this.appCpuSTime2);
            out.writeInt(this.appCpuUid3);
            out.writeInt(this.appCpuUTime3);
            out.writeInt(this.appCpuSTime3);
            out.writeInt(this.statUserTime);
            out.writeInt(this.statSystemTime);
            out.writeInt(this.statIOWaitTime);
            out.writeInt(this.statIrqTime);
            out.writeInt(this.statSoftIrqTime);
            out.writeInt(this.statIdlTime);
            out.writeString(this.statPlatformIdleState);
            out.writeString(this.statSubsystemPowerState);
        }

        public synchronized void readFromParcel(Parcel in) {
            this.userTime = in.readInt();
            this.systemTime = in.readInt();
            this.appCpuUid1 = in.readInt();
            this.appCpuUTime1 = in.readInt();
            this.appCpuSTime1 = in.readInt();
            this.appCpuUid2 = in.readInt();
            this.appCpuUTime2 = in.readInt();
            this.appCpuSTime2 = in.readInt();
            this.appCpuUid3 = in.readInt();
            this.appCpuUTime3 = in.readInt();
            this.appCpuSTime3 = in.readInt();
            this.statUserTime = in.readInt();
            this.statSystemTime = in.readInt();
            this.statIOWaitTime = in.readInt();
            this.statIrqTime = in.readInt();
            this.statSoftIrqTime = in.readInt();
            this.statIdlTime = in.readInt();
            this.statPlatformIdleState = in.readString();
            this.statSubsystemPowerState = in.readString();
        }
    }

    /* loaded from: classes2.dex */
    public static final class HistoryItem implements Parcelable {
        public static final byte CMD_CURRENT_TIME = 5;
        public static final byte CMD_NULL = -1;
        public static final byte CMD_OVERFLOW = 6;
        public static final byte CMD_RESET = 7;
        public static final byte CMD_SHUTDOWN = 8;
        public static final byte CMD_START = 4;
        private protected static final byte CMD_UPDATE = 0;
        public static final int EVENT_ACTIVE = 10;
        public static final int EVENT_ALARM = 13;
        public static final int EVENT_ALARM_FINISH = 16397;
        public static final int EVENT_ALARM_START = 32781;
        public static final int EVENT_COLLECT_EXTERNAL_STATS = 14;
        public static final int EVENT_CONNECTIVITY_CHANGED = 9;
        public static final int EVENT_COUNT = 22;
        public static final int EVENT_FLAG_FINISH = 16384;
        public static final int EVENT_FLAG_START = 32768;
        public static final int EVENT_FOREGROUND = 2;
        public static final int EVENT_FOREGROUND_FINISH = 16386;
        public static final int EVENT_FOREGROUND_START = 32770;
        public static final int EVENT_JOB = 6;
        public static final int EVENT_JOB_FINISH = 16390;
        public static final int EVENT_JOB_START = 32774;
        public static final int EVENT_LONG_WAKE_LOCK = 20;
        public static final int EVENT_LONG_WAKE_LOCK_FINISH = 16404;
        public static final int EVENT_LONG_WAKE_LOCK_START = 32788;
        public static final int EVENT_NONE = 0;
        public static final int EVENT_PACKAGE_ACTIVE = 16;
        public static final int EVENT_PACKAGE_INACTIVE = 15;
        public static final int EVENT_PACKAGE_INSTALLED = 11;
        public static final int EVENT_PACKAGE_UNINSTALLED = 12;
        public static final int EVENT_PROC = 1;
        public static final int EVENT_PROC_FINISH = 16385;
        public static final int EVENT_PROC_START = 32769;
        public static final int EVENT_SCREEN_WAKE_UP = 18;
        public static final int EVENT_SYNC = 4;
        public static final int EVENT_SYNC_FINISH = 16388;
        public static final int EVENT_SYNC_START = 32772;
        public static final int EVENT_TEMP_WHITELIST = 17;
        public static final int EVENT_TEMP_WHITELIST_FINISH = 16401;
        public static final int EVENT_TEMP_WHITELIST_START = 32785;
        public static final int EVENT_TOP = 3;
        public static final int EVENT_TOP_FINISH = 16387;
        public static final int EVENT_TOP_START = 32771;
        public static final int EVENT_TYPE_MASK = -49153;
        public static final int EVENT_USER_FOREGROUND = 8;
        public static final int EVENT_USER_FOREGROUND_FINISH = 16392;
        public static final int EVENT_USER_FOREGROUND_START = 32776;
        public static final int EVENT_USER_RUNNING = 7;
        public static final int EVENT_USER_RUNNING_FINISH = 16391;
        public static final int EVENT_USER_RUNNING_START = 32775;
        public static final int EVENT_WAKEUP_AP = 19;
        public static final int EVENT_WAKE_LOCK = 5;
        public static final int EVENT_WAKE_LOCK_FINISH = 16389;
        public static final int EVENT_WAKE_LOCK_START = 32773;
        public static final int MOST_INTERESTING_STATES = 1835008;
        public static final int MOST_INTERESTING_STATES2 = -1749024768;
        public static final int SETTLE_TO_ZERO_STATES = -1900544;
        public static final int SETTLE_TO_ZERO_STATES2 = 1748959232;
        public static final int STATE2_BLUETOOTH_ON_FLAG = 4194304;
        public static final int STATE2_BLUETOOTH_SCAN_FLAG = 1048576;
        public static final int STATE2_CAMERA_FLAG = 2097152;
        public static final int STATE2_CELLULAR_HIGH_TX_POWER_FLAG = 524288;
        public static final int STATE2_CHARGING_FLAG = 16777216;
        public static final int STATE2_DEVICE_IDLE_MASK = 100663296;
        public static final int STATE2_DEVICE_IDLE_SHIFT = 25;
        public static final int STATE2_FLASHLIGHT_FLAG = 134217728;
        public static final int STATE2_GPS_SIGNAL_QUALITY_MASK = 128;
        public static final int STATE2_GPS_SIGNAL_QUALITY_SHIFT = 7;
        public static final int STATE2_PHONE_IN_CALL_FLAG = 8388608;
        public static final int STATE2_POWER_SAVE_FLAG = Integer.MIN_VALUE;
        public static final int STATE2_USB_DATA_LINK_FLAG = 262144;
        public static final int STATE2_VIDEO_ON_FLAG = 1073741824;
        public static final int STATE2_WIFI_ON_FLAG = 268435456;
        public static final int STATE2_WIFI_RUNNING_FLAG = 536870912;
        public static final int STATE2_WIFI_SIGNAL_STRENGTH_MASK = 112;
        public static final int STATE2_WIFI_SIGNAL_STRENGTH_SHIFT = 4;
        public static final int STATE2_WIFI_SUPPL_STATE_MASK = 15;
        public static final int STATE2_WIFI_SUPPL_STATE_SHIFT = 0;
        public static final int STATE_AUDIO_ON_FLAG = 4194304;
        public static final int STATE_BATTERY_PLUGGED_FLAG = 524288;
        public static final int STATE_BRIGHTNESS_MASK = 7;
        public static final int STATE_BRIGHTNESS_SHIFT = 0;
        public static final int STATE_CPU_RUNNING_FLAG = Integer.MIN_VALUE;
        public static final int STATE_DATA_CONNECTION_MASK = 15872;
        public static final int STATE_DATA_CONNECTION_SHIFT = 9;
        public static final int STATE_GPS_ON_FLAG = 536870912;
        public static final int STATE_MOBILE_RADIO_ACTIVE_FLAG = 33554432;
        public static final int STATE_PHONE_SCANNING_FLAG = 2097152;
        public static final int STATE_PHONE_SIGNAL_STRENGTH_MASK = 56;
        public static final int STATE_PHONE_SIGNAL_STRENGTH_SHIFT = 3;
        public static final int STATE_PHONE_STATE_MASK = 448;
        public static final int STATE_PHONE_STATE_SHIFT = 6;
        private static final int STATE_RESERVED_0 = 16777216;
        public static final int STATE_SCREEN_DOZE_FLAG = 262144;
        public static final int STATE_SCREEN_ON_FLAG = 1048576;
        public static final int STATE_SENSOR_ON_FLAG = 8388608;
        public static final int STATE_WAKE_LOCK_FLAG = 1073741824;
        public static final int STATE_WIFI_FULL_LOCK_FLAG = 268435456;
        public static final int STATE_WIFI_MULTICAST_ON_FLAG = 65536;
        public static final int STATE_WIFI_RADIO_ACTIVE_FLAG = 67108864;
        public static final int STATE_WIFI_SCAN_FLAG = 134217728;
        public int batteryChargeUAh;
        private protected byte batteryHealth;
        private protected byte batteryLevel;
        private protected byte batteryPlugType;
        private protected byte batteryStatus;
        public short batteryTemperature;
        private protected char batteryVoltage;
        private protected byte cmd;
        public long currentTime;
        public int eventCode;
        public HistoryTag eventTag;
        public final HistoryTag localEventTag;
        public final HistoryTag localWakeReasonTag;
        public final HistoryTag localWakelockTag;
        private protected HistoryItem next;
        public int numReadInts;
        private protected int states;
        private protected int states2;
        public HistoryStepDetails stepDetails;
        private protected long time;
        public HistoryTag wakeReasonTag;
        public HistoryTag wakelockTag;

        public synchronized boolean isDeltaData() {
            return this.cmd == 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public HistoryItem() {
            this.cmd = (byte) -1;
            this.localWakelockTag = new HistoryTag();
            this.localWakeReasonTag = new HistoryTag();
            this.localEventTag = new HistoryTag();
        }

        public synchronized HistoryItem(long time, Parcel src) {
            this.cmd = (byte) -1;
            this.localWakelockTag = new HistoryTag();
            this.localWakeReasonTag = new HistoryTag();
            this.localEventTag = new HistoryTag();
            this.time = time;
            this.numReadInts = 2;
            readFromParcel(src);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.time);
            int bat = (this.cmd & 255) | ((this.batteryLevel << 8) & 65280) | ((this.batteryStatus << WifiScanner.PnoSettings.PnoNetwork.FLAG_SAME_NETWORK) & SurfaceControl.FX_SURFACE_MASK) | ((this.batteryHealth << 20) & 15728640) | ((this.batteryPlugType << 24) & 251658240) | (this.wakelockTag != null ? 268435456 : 0) | (this.wakeReasonTag != null ? 536870912 : 0) | (this.eventCode != 0 ? 1073741824 : 0);
            dest.writeInt(bat);
            int bat2 = (this.batteryTemperature & HostEndPoint.BROADCAST) | ((this.batteryVoltage << 16) & (-65536));
            dest.writeInt(bat2);
            dest.writeInt(this.batteryChargeUAh);
            dest.writeInt(this.states);
            dest.writeInt(this.states2);
            if (this.wakelockTag != null) {
                this.wakelockTag.writeToParcel(dest, flags);
            }
            if (this.wakeReasonTag != null) {
                this.wakeReasonTag.writeToParcel(dest, flags);
            }
            if (this.eventCode != 0) {
                dest.writeInt(this.eventCode);
                this.eventTag.writeToParcel(dest, flags);
            }
            if (this.cmd == 5 || this.cmd == 7) {
                dest.writeLong(this.currentTime);
            }
        }

        public synchronized void readFromParcel(Parcel src) {
            int start = src.dataPosition();
            int bat = src.readInt();
            this.cmd = (byte) (bat & 255);
            this.batteryLevel = (byte) ((bat >> 8) & 255);
            this.batteryStatus = (byte) ((bat >> 16) & 15);
            this.batteryHealth = (byte) ((bat >> 20) & 15);
            this.batteryPlugType = (byte) ((bat >> 24) & 15);
            int bat2 = src.readInt();
            this.batteryTemperature = (short) (bat2 & 65535);
            this.batteryVoltage = (char) (65535 & (bat2 >> 16));
            this.batteryChargeUAh = src.readInt();
            this.states = src.readInt();
            this.states2 = src.readInt();
            if ((268435456 & bat) != 0) {
                this.wakelockTag = this.localWakelockTag;
                this.wakelockTag.readFromParcel(src);
            } else {
                this.wakelockTag = null;
            }
            if ((536870912 & bat) != 0) {
                this.wakeReasonTag = this.localWakeReasonTag;
                this.wakeReasonTag.readFromParcel(src);
            } else {
                this.wakeReasonTag = null;
            }
            if ((1073741824 & bat) != 0) {
                this.eventCode = src.readInt();
                this.eventTag = this.localEventTag;
                this.eventTag.readFromParcel(src);
            } else {
                this.eventCode = 0;
                this.eventTag = null;
            }
            if (this.cmd == 5 || this.cmd == 7) {
                this.currentTime = src.readLong();
            } else {
                this.currentTime = 0L;
            }
            this.numReadInts += (src.dataPosition() - start) / 4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clear() {
            this.time = 0L;
            this.cmd = (byte) -1;
            this.batteryLevel = (byte) 0;
            this.batteryStatus = (byte) 0;
            this.batteryHealth = (byte) 0;
            this.batteryPlugType = (byte) 0;
            this.batteryTemperature = (short) 0;
            this.batteryVoltage = (char) 0;
            this.batteryChargeUAh = 0;
            this.states = 0;
            this.states2 = 0;
            this.wakelockTag = null;
            this.wakeReasonTag = null;
            this.eventCode = 0;
            this.eventTag = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setTo(HistoryItem o) {
            this.time = o.time;
            this.cmd = o.cmd;
            setToCommon(o);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setTo(long time, byte cmd, HistoryItem o) {
            this.time = time;
            this.cmd = cmd;
            setToCommon(o);
        }

        private synchronized void setToCommon(HistoryItem o) {
            this.batteryLevel = o.batteryLevel;
            this.batteryStatus = o.batteryStatus;
            this.batteryHealth = o.batteryHealth;
            this.batteryPlugType = o.batteryPlugType;
            this.batteryTemperature = o.batteryTemperature;
            this.batteryVoltage = o.batteryVoltage;
            this.batteryChargeUAh = o.batteryChargeUAh;
            this.states = o.states;
            this.states2 = o.states2;
            if (o.wakelockTag != null) {
                this.wakelockTag = this.localWakelockTag;
                this.wakelockTag.setTo(o.wakelockTag);
            } else {
                this.wakelockTag = null;
            }
            if (o.wakeReasonTag != null) {
                this.wakeReasonTag = this.localWakeReasonTag;
                this.wakeReasonTag.setTo(o.wakeReasonTag);
            } else {
                this.wakeReasonTag = null;
            }
            this.eventCode = o.eventCode;
            if (o.eventTag != null) {
                this.eventTag = this.localEventTag;
                this.eventTag.setTo(o.eventTag);
            } else {
                this.eventTag = null;
            }
            this.currentTime = o.currentTime;
        }

        public synchronized boolean sameNonEvent(HistoryItem o) {
            return this.batteryLevel == o.batteryLevel && this.batteryStatus == o.batteryStatus && this.batteryHealth == o.batteryHealth && this.batteryPlugType == o.batteryPlugType && this.batteryTemperature == o.batteryTemperature && this.batteryVoltage == o.batteryVoltage && this.batteryChargeUAh == o.batteryChargeUAh && this.states == o.states && this.states2 == o.states2 && this.currentTime == o.currentTime;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean same(HistoryItem o) {
            if (sameNonEvent(o) && this.eventCode == o.eventCode) {
                if (this.wakelockTag == o.wakelockTag || !(this.wakelockTag == null || o.wakelockTag == null || !this.wakelockTag.equals(o.wakelockTag))) {
                    if (this.wakeReasonTag == o.wakeReasonTag || !(this.wakeReasonTag == null || o.wakeReasonTag == null || !this.wakeReasonTag.equals(o.wakeReasonTag))) {
                        if (this.eventTag != o.eventTag) {
                            return (this.eventTag == null || o.eventTag == null || !this.eventTag.equals(o.eventTag)) ? false : true;
                        }
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public static final class HistoryEventTracker {
        private final HashMap<String, SparseIntArray>[] mActiveEvents = new HashMap[22];

        public synchronized boolean updateState(int code, String name, int uid, int poolIdx) {
            SparseIntArray uids;
            int idx;
            if ((32768 & code) == 0) {
                if ((code & 16384) != 0) {
                    HashMap<String, SparseIntArray> active = this.mActiveEvents[code & HistoryItem.EVENT_TYPE_MASK];
                    if (active == null || (uids = active.get(name)) == null || (idx = uids.indexOfKey(uid)) < 0) {
                        return false;
                    }
                    uids.removeAt(idx);
                    if (uids.size() <= 0) {
                        active.remove(name);
                        return true;
                    }
                    return true;
                }
                return true;
            }
            int idx2 = code & HistoryItem.EVENT_TYPE_MASK;
            HashMap<String, SparseIntArray> active2 = this.mActiveEvents[idx2];
            if (active2 == null) {
                active2 = new HashMap<>();
                this.mActiveEvents[idx2] = active2;
            }
            SparseIntArray uids2 = active2.get(name);
            if (uids2 == null) {
                uids2 = new SparseIntArray();
                active2.put(name, uids2);
            }
            if (uids2.indexOfKey(uid) >= 0) {
                return false;
            }
            uids2.put(uid, poolIdx);
            return true;
        }

        public synchronized void removeEvents(int code) {
            int idx = (-49153) & code;
            this.mActiveEvents[idx] = null;
        }

        public synchronized HashMap<String, SparseIntArray> getStateForEvent(int code) {
            return this.mActiveEvents[code];
        }
    }

    /* loaded from: classes2.dex */
    public static final class BitDescription {
        public final int mask;
        public final String name;
        public final int shift;
        public final String shortName;
        public final String[] shortValues;
        public final String[] values;

        public synchronized BitDescription(int mask, String name, String shortName) {
            this.mask = mask;
            this.shift = -1;
            this.name = name;
            this.shortName = shortName;
            this.values = null;
            this.shortValues = null;
        }

        public synchronized BitDescription(int mask, int shift, String name, String shortName, String[] values, String[] shortValues) {
            this.mask = mask;
            this.shift = shift;
            this.name = name;
            this.shortName = shortName;
            this.values = values;
            this.shortValues = shortValues;
        }
    }

    private static final synchronized void formatTimeRaw(StringBuilder out, long seconds) {
        long days = seconds / 86400;
        if (days != 0) {
            out.append(days);
            out.append("d ");
        }
        long used = days * 60 * 60 * 24;
        long hours = (seconds - used) / 3600;
        if (hours != 0 || used != 0) {
            out.append(hours);
            out.append("h ");
        }
        long used2 = used + (hours * 60 * 60);
        long mins = (seconds - used2) / 60;
        if (mins != 0 || used2 != 0) {
            out.append(mins);
            out.append("m ");
        }
        long used3 = used2 + (60 * mins);
        if (seconds != 0 || used3 != 0) {
            out.append(seconds - used3);
            out.append("s ");
        }
    }

    public static final synchronized void formatTimeMs(StringBuilder sb, long time) {
        long sec = time / 1000;
        formatTimeRaw(sb, sec);
        sb.append(time - (1000 * sec));
        sb.append("ms ");
    }

    public static final synchronized void formatTimeMsNoSpace(StringBuilder sb, long time) {
        long sec = time / 1000;
        formatTimeRaw(sb, sec);
        sb.append(time - (1000 * sec));
        sb.append("ms");
    }

    public final synchronized String formatRatioLocked(long num, long den) {
        if (den == 0) {
            return "--%";
        }
        float perc = (((float) num) / ((float) den)) * 100.0f;
        this.mFormatBuilder.setLength(0);
        this.mFormatter.format("%.1f%%", Float.valueOf(perc));
        return this.mFormatBuilder.toString();
    }

    final synchronized String formatBytesLocked(long bytes) {
        this.mFormatBuilder.setLength(0);
        if (bytes < 1024) {
            return bytes + "B";
        } else if (bytes < 1048576) {
            this.mFormatter.format("%.2fKB", Double.valueOf(bytes / 1024.0d));
            return this.mFormatBuilder.toString();
        } else if (bytes < 1073741824) {
            this.mFormatter.format("%.2fMB", Double.valueOf(bytes / 1048576.0d));
            return this.mFormatBuilder.toString();
        } else {
            this.mFormatter.format("%.2fGB", Double.valueOf(bytes / 1.073741824E9d));
            return this.mFormatBuilder.toString();
        }
    }

    private static synchronized long roundUsToMs(long timeUs) {
        return (500 + timeUs) / 1000;
    }

    private static synchronized long computeWakeLock(Timer timer, long elapsedRealtimeUs, int which) {
        if (timer != null) {
            long totalTimeMicros = timer.getTotalTimeLocked(elapsedRealtimeUs, which);
            long totalTimeMillis = (500 + totalTimeMicros) / 1000;
            return totalTimeMillis;
        }
        return 0L;
    }

    private static final synchronized String printWakeLock(StringBuilder sb, Timer timer, long elapsedRealtimeUs, String name, int which, String linePrefix) {
        if (timer != null) {
            long totalTimeMillis = computeWakeLock(timer, elapsedRealtimeUs, which);
            int count = timer.getCountLocked(which);
            if (totalTimeMillis != 0) {
                sb.append(linePrefix);
                formatTimeMs(sb, totalTimeMillis);
                if (name != null) {
                    sb.append(name);
                    sb.append(' ');
                }
                sb.append('(');
                sb.append(count);
                sb.append(" times)");
                long maxDurationMs = timer.getMaxDurationMsLocked(elapsedRealtimeUs / 1000);
                if (maxDurationMs >= 0) {
                    sb.append(" max=");
                    sb.append(maxDurationMs);
                }
                long totalDurMs = timer.getTotalDurationMsLocked(elapsedRealtimeUs / 1000);
                if (totalDurMs > totalTimeMillis) {
                    sb.append(" actual=");
                    sb.append(totalDurMs);
                }
                if (timer.isRunningLocked()) {
                    long currentMs = timer.getCurrentDurationMsLocked(elapsedRealtimeUs / 1000);
                    if (currentMs >= 0) {
                        sb.append(" (running for ");
                        sb.append(currentMs);
                        sb.append("ms)");
                        return ", ";
                    }
                    sb.append(" (running)");
                    return ", ";
                }
                return ", ";
            }
        }
        return linePrefix;
    }

    private static final synchronized boolean printTimer(PrintWriter pw, StringBuilder sb, Timer timer, long rawRealtimeUs, int which, String prefix, String type) {
        if (timer != null) {
            long totalTimeMs = (timer.getTotalTimeLocked(rawRealtimeUs, which) + 500) / 1000;
            int count = timer.getCountLocked(which);
            if (totalTimeMs != 0) {
                sb.setLength(0);
                sb.append(prefix);
                sb.append("    ");
                sb.append(type);
                sb.append(": ");
                formatTimeMs(sb, totalTimeMs);
                sb.append("realtime (");
                sb.append(count);
                sb.append(" times)");
                long maxDurationMs = timer.getMaxDurationMsLocked(rawRealtimeUs / 1000);
                if (maxDurationMs >= 0) {
                    sb.append(" max=");
                    sb.append(maxDurationMs);
                }
                if (timer.isRunningLocked()) {
                    long currentMs = timer.getCurrentDurationMsLocked(rawRealtimeUs / 1000);
                    if (currentMs >= 0) {
                        sb.append(" (running for ");
                        sb.append(currentMs);
                        sb.append("ms)");
                    } else {
                        sb.append(" (running)");
                    }
                }
                pw.println(sb.toString());
                return true;
            }
        }
        return false;
    }

    private static final synchronized String printWakeLockCheckin(StringBuilder sb, Timer timer, long elapsedRealtimeUs, String name, int which, String linePrefix) {
        long totalTimeMicros;
        String str;
        int count = 0;
        long max = 0;
        long current = 0;
        long totalDuration = 0;
        if (timer != null) {
            long totalTimeMicros2 = timer.getTotalTimeLocked(elapsedRealtimeUs, which);
            count = timer.getCountLocked(which);
            totalTimeMicros = totalTimeMicros2;
            current = timer.getCurrentDurationMsLocked(elapsedRealtimeUs / 1000);
            max = timer.getMaxDurationMsLocked(elapsedRealtimeUs / 1000);
            totalDuration = timer.getTotalDurationMsLocked(elapsedRealtimeUs / 1000);
        } else {
            totalTimeMicros = 0;
        }
        sb.append(linePrefix);
        sb.append((totalTimeMicros + 500) / 1000);
        sb.append(',');
        if (name != null) {
            str = name + ",";
        } else {
            str = "";
        }
        sb.append(str);
        sb.append(count);
        sb.append(',');
        sb.append(current);
        sb.append(',');
        sb.append(max);
        if (name != null) {
            sb.append(',');
            sb.append(totalDuration);
            return ",";
        }
        return ",";
    }

    private static final synchronized void dumpLineHeader(PrintWriter pw, int uid, String category, String type) {
        pw.print(9);
        pw.print(',');
        pw.print(uid);
        pw.print(',');
        pw.print(category);
        pw.print(',');
        pw.print(type);
    }

    private static final void dumpLine(PrintWriter pw, int uid, String category, String type, Object... args) {
        dumpLineHeader(pw, uid, category, type);
        for (Object arg : args) {
            pw.print(',');
            pw.print(arg);
        }
        pw.println();
    }

    private static final synchronized void dumpTimer(PrintWriter pw, int uid, String category, String type, Timer timer, long rawRealtime, int which) {
        if (timer != null) {
            long totalTime = roundUsToMs(timer.getTotalTimeLocked(rawRealtime, which));
            int count = timer.getCountLocked(which);
            if (totalTime != 0 || count != 0) {
                dumpLine(pw, uid, category, type, Long.valueOf(totalTime), Integer.valueOf(count));
            }
        }
    }

    private static synchronized void dumpTimer(ProtoOutputStream proto, long fieldId, Timer timer, long rawRealtimeUs, int which) {
        if (timer == null) {
            return;
        }
        long timeMs = roundUsToMs(timer.getTotalTimeLocked(rawRealtimeUs, which));
        int count = timer.getCountLocked(which);
        long maxDurationMs = timer.getMaxDurationMsLocked(rawRealtimeUs / 1000);
        long curDurationMs = timer.getCurrentDurationMsLocked(rawRealtimeUs / 1000);
        long totalDurationMs = timer.getTotalDurationMsLocked(rawRealtimeUs / 1000);
        if (timeMs != 0 || count != 0 || maxDurationMs != -1 || curDurationMs != -1 || totalDurationMs != -1) {
            long token = proto.start(fieldId);
            proto.write(1112396529665L, timeMs);
            proto.write(1112396529666L, count);
            if (maxDurationMs != -1) {
                proto.write(1112396529667L, maxDurationMs);
            }
            if (curDurationMs != -1) {
                proto.write(1112396529668L, curDurationMs);
            }
            if (totalDurationMs != -1) {
                proto.write(1112396529669L, totalDurationMs);
            }
            proto.end(token);
        }
    }

    private static synchronized boolean controllerActivityHasData(ControllerActivityCounter counter, int which) {
        LongCounter[] txTimeCounters;
        if (counter == null) {
            return false;
        }
        if (counter.getIdleTimeCounter().getCountLocked(which) == 0 && counter.getRxTimeCounter().getCountLocked(which) == 0 && counter.getPowerCounter().getCountLocked(which) == 0) {
            for (LongCounter c : counter.getTxTimeCounters()) {
                if (c.getCountLocked(which) != 0) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private static final synchronized void dumpControllerActivityLine(PrintWriter pw, int uid, String category, String type, ControllerActivityCounter counter, int which) {
        LongCounter[] txTimeCounters;
        if (!controllerActivityHasData(counter, which)) {
            return;
        }
        dumpLineHeader(pw, uid, category, type);
        pw.print(",");
        pw.print(counter.getIdleTimeCounter().getCountLocked(which));
        pw.print(",");
        pw.print(counter.getRxTimeCounter().getCountLocked(which));
        pw.print(",");
        pw.print(counter.getPowerCounter().getCountLocked(which) / 3600000);
        for (LongCounter c : counter.getTxTimeCounters()) {
            pw.print(",");
            pw.print(c.getCountLocked(which));
        }
        pw.println();
    }

    private static synchronized void dumpControllerActivityProto(ProtoOutputStream proto, long fieldId, ControllerActivityCounter counter, int which) {
        if (!controllerActivityHasData(counter, which)) {
            return;
        }
        long cToken = proto.start(fieldId);
        proto.write(1112396529665L, counter.getIdleTimeCounter().getCountLocked(which));
        proto.write(1112396529666L, counter.getRxTimeCounter().getCountLocked(which));
        proto.write(1112396529667L, counter.getPowerCounter().getCountLocked(which) / 3600000);
        LongCounter[] txCounters = counter.getTxTimeCounters();
        for (int i = 0; i < txCounters.length; i++) {
            LongCounter c = txCounters[i];
            long tToken = proto.start(2246267895812L);
            proto.write(1120986464257L, i);
            proto.write(1112396529666L, c.getCountLocked(which));
            proto.end(tToken);
        }
        proto.end(cToken);
    }

    private final synchronized void printControllerActivityIfInteresting(PrintWriter pw, StringBuilder sb, String prefix, String controllerName, ControllerActivityCounter counter, int which) {
        if (controllerActivityHasData(counter, which)) {
            printControllerActivity(pw, sb, prefix, controllerName, counter, which);
        }
    }

    private final synchronized void printControllerActivity(PrintWriter pw, StringBuilder sb, String prefix, String controllerName, ControllerActivityCounter counter, int which) {
        long idleTimeMs = counter.getIdleTimeCounter().getCountLocked(which);
        long rxTimeMs = counter.getRxTimeCounter().getCountLocked(which);
        long powerDrainMaMs = counter.getPowerCounter().getCountLocked(which);
        long totalControllerActivityTimeMs = computeBatteryRealtime(SystemClock.elapsedRealtime() * 1000, which) / 1000;
        LongCounter[] txTimeCounters = counter.getTxTimeCounters();
        long totalTxTimeMs = 0;
        int length = txTimeCounters.length;
        int i = 0;
        while (i < length) {
            int i2 = length;
            LongCounter txState = txTimeCounters[i];
            totalTxTimeMs += txState.getCountLocked(which);
            i++;
            length = i2;
        }
        if (controllerName.equals(WIFI_CONTROLLER_NAME)) {
            long scanTimeMs = counter.getScanTimeCounter().getCountLocked(which);
            sb.setLength(0);
            sb.append(prefix);
            sb.append("     ");
            sb.append(controllerName);
            sb.append(" Scan time:  ");
            formatTimeMs(sb, scanTimeMs);
            sb.append("(");
            sb.append(formatRatioLocked(scanTimeMs, totalControllerActivityTimeMs));
            sb.append(")");
            pw.println(sb.toString());
            long scanTimeMs2 = totalControllerActivityTimeMs - ((idleTimeMs + rxTimeMs) + totalTxTimeMs);
            sb.setLength(0);
            sb.append(prefix);
            sb.append("     ");
            sb.append(controllerName);
            sb.append(" Sleep time:  ");
            formatTimeMs(sb, scanTimeMs2);
            sb.append("(");
            sb.append(formatRatioLocked(scanTimeMs2, totalControllerActivityTimeMs));
            sb.append(")");
            pw.println(sb.toString());
        }
        if (controllerName.equals(CELLULAR_CONTROLLER_NAME)) {
            long sleepTimeMs = counter.getSleepTimeCounter().getCountLocked(which);
            sb.setLength(0);
            sb.append(prefix);
            sb.append("     ");
            sb.append(controllerName);
            sb.append(" Sleep time:  ");
            formatTimeMs(sb, sleepTimeMs);
            sb.append("(");
            sb.append(formatRatioLocked(sleepTimeMs, totalControllerActivityTimeMs));
            sb.append(")");
            pw.println(sb.toString());
        }
        sb.setLength(0);
        sb.append(prefix);
        sb.append("     ");
        sb.append(controllerName);
        sb.append(" Idle time:   ");
        formatTimeMs(sb, idleTimeMs);
        sb.append("(");
        sb.append(formatRatioLocked(idleTimeMs, totalControllerActivityTimeMs));
        sb.append(")");
        pw.println(sb.toString());
        sb.setLength(0);
        sb.append(prefix);
        sb.append("     ");
        sb.append(controllerName);
        sb.append(" Rx time:     ");
        formatTimeMs(sb, rxTimeMs);
        sb.append("(");
        sb.append(formatRatioLocked(rxTimeMs, totalControllerActivityTimeMs));
        sb.append(")");
        pw.println(sb.toString());
        sb.setLength(0);
        sb.append(prefix);
        sb.append("     ");
        sb.append(controllerName);
        sb.append(" Tx time:     ");
        char c = 65535;
        if (controllerName.hashCode() == -851952246 && controllerName.equals(CELLULAR_CONTROLLER_NAME)) {
            c = 0;
        }
        String[] powerLevel = c != 0 ? new String[]{"[0]", "[1]", "[2]", "[3]", "[4]"} : new String[]{"   less than 0dBm: ", "   0dBm to 8dBm: ", "   8dBm to 15dBm: ", "   15dBm to 20dBm: ", "   above 20dBm: "};
        int numTxLvls = Math.min(counter.getTxTimeCounters().length, powerLevel.length);
        if (numTxLvls <= 1) {
            long txLvlTimeMs = counter.getTxTimeCounters()[0].getCountLocked(which);
            formatTimeMs(sb, txLvlTimeMs);
            sb.append("(");
            sb.append(formatRatioLocked(txLvlTimeMs, totalControllerActivityTimeMs));
            sb.append(")");
            pw.println(sb.toString());
        } else {
            pw.println(sb.toString());
            int lvl = 0;
            while (lvl < numTxLvls) {
                long txLvlTimeMs2 = counter.getTxTimeCounters()[lvl].getCountLocked(which);
                sb.setLength(0);
                sb.append(prefix);
                sb.append("    ");
                sb.append(powerLevel[lvl]);
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                formatTimeMs(sb, txLvlTimeMs2);
                sb.append("(");
                sb.append(formatRatioLocked(txLvlTimeMs2, totalControllerActivityTimeMs));
                sb.append(")");
                pw.println(sb.toString());
                lvl++;
                numTxLvls = numTxLvls;
            }
        }
        if (powerDrainMaMs > 0) {
            sb.setLength(0);
            sb.append(prefix);
            sb.append("     ");
            sb.append(controllerName);
            sb.append(" Battery drain: ");
            sb.append(BatteryStatsHelper.makemAh(powerDrainMaMs / 3600000.0d));
            sb.append("mAh");
            pw.println(sb.toString());
        }
    }

    public final synchronized void dumpCheckinLocked(Context context, PrintWriter pw, int which, int reqUid) {
        dumpCheckinLocked(context, pw, which, reqUid, BatteryStatsHelper.checkWifiOnly(context));
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: SSATransform
        java.lang.IndexOutOfBoundsException: bitIndex < 0: -124
        	at java.base/java.util.BitSet.get(BitSet.java:626)
        	at jadx.core.dex.visitors.ssa.LiveVarAnalysis.fillBasicBlockInfo(LiveVarAnalysis.java:65)
        	at jadx.core.dex.visitors.ssa.LiveVarAnalysis.runAnalysis(LiveVarAnalysis.java:36)
        	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:55)
        	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:41)
        */
    public final synchronized void dumpCheckinLocked(android.content.Context r372, java.io.PrintWriter r373, int r374, int r375, boolean r376) {
        /*
            Method dump skipped, instructions count: 5964
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.BatteryStats.dumpCheckinLocked(android.content.Context, java.io.PrintWriter, int, int, boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class TimerEntry {
        final int mId;
        final String mName;
        final long mTime;
        final Timer mTimer;

        synchronized TimerEntry(String name, int id, Timer timer, long time) {
            this.mName = name;
            this.mId = id;
            this.mTimer = timer;
            this.mTime = time;
        }
    }

    private synchronized void printmAh(PrintWriter printer, double power) {
        printer.print(BatteryStatsHelper.makemAh(power));
    }

    private synchronized void printmAh(StringBuilder sb, double power) {
        sb.append(BatteryStatsHelper.makemAh(power));
    }

    public final synchronized void dumpLocked(Context context, PrintWriter pw, String prefix, int which, int reqUid) {
        dumpLocked(context, pw, prefix, which, reqUid, BatteryStatsHelper.checkWifiOnly(context));
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: SSATransform
        java.lang.IndexOutOfBoundsException: bitIndex < 0: -125
        	at java.base/java.util.BitSet.get(BitSet.java:626)
        	at jadx.core.dex.visitors.ssa.LiveVarAnalysis.fillBasicBlockInfo(LiveVarAnalysis.java:65)
        	at jadx.core.dex.visitors.ssa.LiveVarAnalysis.runAnalysis(LiveVarAnalysis.java:36)
        	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:55)
        	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:41)
        */
    public final synchronized void dumpLocked(android.content.Context r422, java.io.PrintWriter r423, java.lang.String r424, int r425, int r426, boolean r427) {
        /*
            Method dump skipped, instructions count: 9688
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.BatteryStats.dumpLocked(android.content.Context, java.io.PrintWriter, java.lang.String, int, int, boolean):void");
    }

    static synchronized void printBitDescriptions(StringBuilder sb, int oldval, int newval, HistoryTag wakelockTag, BitDescription[] descriptions, boolean longNames) {
        int diff = oldval ^ newval;
        if (diff == 0) {
            return;
        }
        boolean didWake = false;
        for (BitDescription bd : descriptions) {
            if ((bd.mask & diff) != 0) {
                sb.append(longNames ? WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER : ",");
                if (bd.shift < 0) {
                    sb.append((bd.mask & newval) != 0 ? "+" : NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                    sb.append(longNames ? bd.name : bd.shortName);
                    if (bd.mask == 1073741824 && wakelockTag != null) {
                        didWake = true;
                        sb.append("=");
                        if (longNames) {
                            UserHandle.formatUid(sb, wakelockTag.uid);
                            sb.append(":\"");
                            sb.append(wakelockTag.string);
                            sb.append("\"");
                        } else {
                            sb.append(wakelockTag.poolIdx);
                        }
                    }
                } else {
                    sb.append(longNames ? bd.name : bd.shortName);
                    sb.append("=");
                    int val = (bd.mask & newval) >> bd.shift;
                    if (bd.values != null && val >= 0 && val < bd.values.length) {
                        sb.append(longNames ? bd.values[val] : bd.shortValues[val]);
                    } else {
                        sb.append(val);
                    }
                }
            }
        }
        if (!didWake && wakelockTag != null) {
            sb.append(longNames ? " wake_lock=" : ",w=");
            if (longNames) {
                UserHandle.formatUid(sb, wakelockTag.uid);
                sb.append(":\"");
                sb.append(wakelockTag.string);
                sb.append("\"");
                return;
            }
            sb.append(wakelockTag.poolIdx);
        }
    }

    public synchronized void prepareForDumpLocked() {
    }

    /* loaded from: classes2.dex */
    public static class HistoryPrinter {
        int oldState = 0;
        int oldState2 = 0;
        int oldLevel = -1;
        int oldStatus = -1;
        int oldHealth = -1;
        int oldPlug = -1;
        int oldTemp = -1;
        int oldVolt = -1;
        int oldChargeMAh = -1;
        long lastTime = -1;

        synchronized void reset() {
            this.oldState2 = 0;
            this.oldState = 0;
            this.oldLevel = -1;
            this.oldStatus = -1;
            this.oldHealth = -1;
            this.oldPlug = -1;
            this.oldTemp = -1;
            this.oldVolt = -1;
            this.oldChargeMAh = -1;
        }

        public synchronized void printNextItem(PrintWriter pw, HistoryItem rec, long baseTime, boolean checkin, boolean verbose) {
            pw.print(printNextItem(rec, baseTime, checkin, verbose));
        }

        public synchronized void printNextItem(ProtoOutputStream proto, HistoryItem rec, long baseTime, boolean verbose) {
            String[] split;
            String item = printNextItem(rec, baseTime, true, verbose);
            for (String line : item.split("\n")) {
                proto.write(SystemPropertiesProto.Ro.Product.CPU_ABILIST64, line);
            }
        }

        private synchronized String printNextItem(HistoryItem rec, long baseTime, boolean checkin, boolean verbose) {
            StringBuilder item = new StringBuilder();
            if (!checkin) {
                item.append("  ");
                TimeUtils.formatDuration(rec.time - baseTime, item, 19);
                item.append(" (");
                item.append(rec.numReadInts);
                item.append(") ");
            } else {
                item.append(9);
                item.append(',');
                item.append(BatteryStats.HISTORY_DATA);
                item.append(',');
                if (this.lastTime < 0) {
                    item.append(rec.time - baseTime);
                } else {
                    item.append(rec.time - this.lastTime);
                }
                this.lastTime = rec.time;
            }
            if (rec.cmd == 4) {
                if (checkin) {
                    item.append(SettingsStringUtil.DELIMITER);
                }
                item.append("START\n");
                reset();
            } else if (rec.cmd == 5 || rec.cmd == 7) {
                if (checkin) {
                    item.append(SettingsStringUtil.DELIMITER);
                }
                if (rec.cmd == 7) {
                    item.append("RESET:");
                    reset();
                }
                item.append("TIME:");
                if (checkin) {
                    item.append(rec.currentTime);
                    item.append("\n");
                } else {
                    item.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    item.append(DateFormat.format("yyyy-MM-dd-HH-mm-ss", rec.currentTime).toString());
                    item.append("\n");
                }
            } else if (rec.cmd == 8) {
                if (checkin) {
                    item.append(SettingsStringUtil.DELIMITER);
                }
                item.append("SHUTDOWN\n");
            } else if (rec.cmd == 6) {
                if (checkin) {
                    item.append(SettingsStringUtil.DELIMITER);
                }
                item.append("*OVERFLOW*\n");
            } else {
                if (!checkin) {
                    if (rec.batteryLevel < 10) {
                        item.append("00");
                    } else if (rec.batteryLevel < 100) {
                        item.append("0");
                    }
                    item.append(rec.batteryLevel);
                    if (verbose) {
                        item.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        if (rec.states >= 0) {
                            if (rec.states < 16) {
                                item.append("0000000");
                            } else if (rec.states < 256) {
                                item.append("000000");
                            } else if (rec.states < 4096) {
                                item.append("00000");
                            } else if (rec.states < 65536) {
                                item.append("0000");
                            } else if (rec.states < 1048576) {
                                item.append("000");
                            } else if (rec.states < 16777216) {
                                item.append("00");
                            } else if (rec.states < 268435456) {
                                item.append("0");
                            }
                        }
                        item.append(Integer.toHexString(rec.states));
                    }
                } else if (this.oldLevel != rec.batteryLevel) {
                    this.oldLevel = rec.batteryLevel;
                    item.append(",Bl=");
                    item.append(rec.batteryLevel);
                }
                if (this.oldStatus != rec.batteryStatus) {
                    this.oldStatus = rec.batteryStatus;
                    item.append(checkin ? ",Bs=" : " status=");
                    switch (this.oldStatus) {
                        case 1:
                            item.append(checkin ? "?" : "unknown");
                            break;
                        case 2:
                            item.append(checkin ? FullBackup.CACHE_TREE_TOKEN : "charging");
                            break;
                        case 3:
                            item.append(checkin ? "d" : "discharging");
                            break;
                        case 4:
                            item.append(checkin ? "n" : "not-charging");
                            break;
                        case 5:
                            item.append(checkin ? FullBackup.FILES_TREE_TOKEN : "full");
                            break;
                        default:
                            item.append(this.oldStatus);
                            break;
                    }
                }
                if (this.oldHealth != rec.batteryHealth) {
                    this.oldHealth = rec.batteryHealth;
                    item.append(checkin ? ",Bh=" : " health=");
                    switch (this.oldHealth) {
                        case 1:
                            item.append(checkin ? "?" : "unknown");
                            break;
                        case 2:
                            item.append(checkin ? "g" : "good");
                            break;
                        case 3:
                            item.append(checkin ? BatteryStats.HISTORY_DATA : "overheat");
                            break;
                        case 4:
                            item.append(checkin ? "d" : "dead");
                            break;
                        case 5:
                            item.append(checkin ? Telephony.BaseMmsColumns.MMS_VERSION : "over-voltage");
                            break;
                        case 6:
                            item.append(checkin ? FullBackup.FILES_TREE_TOKEN : "failure");
                            break;
                        case 7:
                            item.append(checkin ? FullBackup.CACHE_TREE_TOKEN : "cold");
                            break;
                        default:
                            item.append(this.oldHealth);
                            break;
                    }
                }
                if (this.oldPlug != rec.batteryPlugType) {
                    this.oldPlug = rec.batteryPlugType;
                    item.append(checkin ? ",Bp=" : " plug=");
                    int i = this.oldPlug;
                    if (i != 4) {
                        switch (i) {
                            case 0:
                                item.append(checkin ? "n" : "none");
                                break;
                            case 1:
                                item.append(checkin ? FullBackup.APK_TREE_TOKEN : "ac");
                                break;
                            case 2:
                                item.append(checkin ? "u" : Context.USB_SERVICE);
                                break;
                            default:
                                item.append(this.oldPlug);
                                break;
                        }
                    } else {
                        item.append(checkin ? "w" : "wireless");
                    }
                }
                if (this.oldTemp != rec.batteryTemperature) {
                    this.oldTemp = rec.batteryTemperature;
                    item.append(checkin ? ",Bt=" : " temp=");
                    item.append(this.oldTemp);
                }
                if (this.oldVolt != rec.batteryVoltage) {
                    this.oldVolt = rec.batteryVoltage;
                    item.append(checkin ? ",Bv=" : " volt=");
                    item.append(this.oldVolt);
                }
                int chargeMAh = rec.batteryChargeUAh / 1000;
                if (this.oldChargeMAh != chargeMAh) {
                    this.oldChargeMAh = chargeMAh;
                    item.append(checkin ? ",Bcc=" : " charge=");
                    item.append(this.oldChargeMAh);
                }
                BatteryStats.printBitDescriptions(item, this.oldState, rec.states, rec.wakelockTag, BatteryStats.HISTORY_STATE_DESCRIPTIONS, !checkin);
                BatteryStats.printBitDescriptions(item, this.oldState2, rec.states2, null, BatteryStats.HISTORY_STATE2_DESCRIPTIONS, !checkin);
                if (rec.wakeReasonTag != null) {
                    if (checkin) {
                        item.append(",wr=");
                        item.append(rec.wakeReasonTag.poolIdx);
                    } else {
                        item.append(" wake_reason=");
                        item.append(rec.wakeReasonTag.uid);
                        item.append(":\"");
                        item.append(rec.wakeReasonTag.string);
                        item.append("\"");
                    }
                }
                if (rec.eventCode != 0) {
                    item.append(checkin ? "," : WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    if ((rec.eventCode & 32768) != 0) {
                        item.append("+");
                    } else if ((rec.eventCode & 16384) != 0) {
                        item.append(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                    }
                    String[] eventNames = checkin ? BatteryStats.HISTORY_EVENT_CHECKIN_NAMES : BatteryStats.HISTORY_EVENT_NAMES;
                    int idx = rec.eventCode & HistoryItem.EVENT_TYPE_MASK;
                    if (idx >= 0 && idx < eventNames.length) {
                        item.append(eventNames[idx]);
                    } else {
                        item.append(checkin ? "Ev" : "event");
                        item.append(idx);
                    }
                    item.append("=");
                    if (checkin) {
                        item.append(rec.eventTag.poolIdx);
                    } else {
                        item.append(BatteryStats.HISTORY_EVENT_INT_FORMATTERS[idx].applyAsString(rec.eventTag.uid));
                        item.append(":\"");
                        item.append(rec.eventTag.string);
                        item.append("\"");
                    }
                }
                item.append("\n");
                if (rec.stepDetails != null) {
                    if (!checkin) {
                        item.append("                 Details: cpu=");
                        item.append(rec.stepDetails.userTime);
                        item.append("u+");
                        item.append(rec.stepDetails.systemTime);
                        item.append("s");
                        if (rec.stepDetails.appCpuUid1 >= 0) {
                            item.append(" (");
                            printStepCpuUidDetails(item, rec.stepDetails.appCpuUid1, rec.stepDetails.appCpuUTime1, rec.stepDetails.appCpuSTime1);
                            if (rec.stepDetails.appCpuUid2 >= 0) {
                                item.append(", ");
                                printStepCpuUidDetails(item, rec.stepDetails.appCpuUid2, rec.stepDetails.appCpuUTime2, rec.stepDetails.appCpuSTime2);
                            }
                            if (rec.stepDetails.appCpuUid3 >= 0) {
                                item.append(", ");
                                printStepCpuUidDetails(item, rec.stepDetails.appCpuUid3, rec.stepDetails.appCpuUTime3, rec.stepDetails.appCpuSTime3);
                            }
                            item.append(')');
                        }
                        item.append("\n");
                        item.append("                          /proc/stat=");
                        item.append(rec.stepDetails.statUserTime);
                        item.append(" usr, ");
                        item.append(rec.stepDetails.statSystemTime);
                        item.append(" sys, ");
                        item.append(rec.stepDetails.statIOWaitTime);
                        item.append(" io, ");
                        item.append(rec.stepDetails.statIrqTime);
                        item.append(" irq, ");
                        item.append(rec.stepDetails.statSoftIrqTime);
                        item.append(" sirq, ");
                        item.append(rec.stepDetails.statIdlTime);
                        item.append(" idle");
                        int totalRun = rec.stepDetails.statUserTime + rec.stepDetails.statSystemTime + rec.stepDetails.statIOWaitTime + rec.stepDetails.statIrqTime + rec.stepDetails.statSoftIrqTime;
                        int total = rec.stepDetails.statIdlTime + totalRun;
                        if (total > 0) {
                            item.append(" (");
                            float perc = (totalRun / total) * 100.0f;
                            item.append(String.format("%.1f%%", Float.valueOf(perc)));
                            item.append(" of ");
                            StringBuilder sb = new StringBuilder(64);
                            BatteryStats.formatTimeMsNoSpace(sb, total * 10);
                            item.append((CharSequence) sb);
                            item.append(")");
                        }
                        item.append(", PlatformIdleStat ");
                        item.append(rec.stepDetails.statPlatformIdleState);
                        item.append("\n");
                        item.append(", SubsystemPowerState ");
                        item.append(rec.stepDetails.statSubsystemPowerState);
                        item.append("\n");
                    } else {
                        item.append(9);
                        item.append(',');
                        item.append(BatteryStats.HISTORY_DATA);
                        item.append(",0,Dcpu=");
                        item.append(rec.stepDetails.userTime);
                        item.append(SettingsStringUtil.DELIMITER);
                        item.append(rec.stepDetails.systemTime);
                        if (rec.stepDetails.appCpuUid1 >= 0) {
                            printStepCpuUidCheckinDetails(item, rec.stepDetails.appCpuUid1, rec.stepDetails.appCpuUTime1, rec.stepDetails.appCpuSTime1);
                            if (rec.stepDetails.appCpuUid2 >= 0) {
                                printStepCpuUidCheckinDetails(item, rec.stepDetails.appCpuUid2, rec.stepDetails.appCpuUTime2, rec.stepDetails.appCpuSTime2);
                            }
                            if (rec.stepDetails.appCpuUid3 >= 0) {
                                printStepCpuUidCheckinDetails(item, rec.stepDetails.appCpuUid3, rec.stepDetails.appCpuUTime3, rec.stepDetails.appCpuSTime3);
                            }
                        }
                        item.append("\n");
                        item.append(9);
                        item.append(',');
                        item.append(BatteryStats.HISTORY_DATA);
                        item.append(",0,Dpst=");
                        item.append(rec.stepDetails.statUserTime);
                        item.append(',');
                        item.append(rec.stepDetails.statSystemTime);
                        item.append(',');
                        item.append(rec.stepDetails.statIOWaitTime);
                        item.append(',');
                        item.append(rec.stepDetails.statIrqTime);
                        item.append(',');
                        item.append(rec.stepDetails.statSoftIrqTime);
                        item.append(',');
                        item.append(rec.stepDetails.statIdlTime);
                        item.append(',');
                        if (rec.stepDetails.statPlatformIdleState != null) {
                            item.append(rec.stepDetails.statPlatformIdleState);
                            if (rec.stepDetails.statSubsystemPowerState != null) {
                                item.append(',');
                            }
                        }
                        if (rec.stepDetails.statSubsystemPowerState != null) {
                            item.append(rec.stepDetails.statSubsystemPowerState);
                        }
                        item.append("\n");
                    }
                }
                this.oldState = rec.states;
                this.oldState2 = rec.states2;
            }
            return item.toString();
        }

        private synchronized void printStepCpuUidDetails(StringBuilder sb, int uid, int utime, int stime) {
            UserHandle.formatUid(sb, uid);
            sb.append("=");
            sb.append(utime);
            sb.append("u+");
            sb.append(stime);
            sb.append("s");
        }

        private synchronized void printStepCpuUidCheckinDetails(StringBuilder sb, int uid, int utime, int stime) {
            sb.append('/');
            sb.append(uid);
            sb.append(SettingsStringUtil.DELIMITER);
            sb.append(utime);
            sb.append(SettingsStringUtil.DELIMITER);
            sb.append(stime);
        }
    }

    private synchronized void printSizeValue(PrintWriter pw, long size) {
        float result = (float) size;
        String suffix = "";
        if (result >= 10240.0f) {
            suffix = "KB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "MB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "GB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "TB";
            result /= 1024.0f;
        }
        if (result >= 10240.0f) {
            suffix = "PB";
            result /= 1024.0f;
        }
        pw.print((int) result);
        pw.print(suffix);
    }

    private static synchronized boolean dumpTimeEstimate(PrintWriter pw, String label1, String label2, String label3, long estimatedTime) {
        if (estimatedTime < 0) {
            return false;
        }
        pw.print(label1);
        pw.print(label2);
        pw.print(label3);
        StringBuilder sb = new StringBuilder(64);
        formatTimeMs(sb, estimatedTime);
        pw.print(sb);
        pw.println();
        return true;
    }

    private static synchronized boolean dumpDurationSteps(PrintWriter pw, String prefix, String header, LevelStepTracker steps, boolean checkin) {
        int count;
        int count2;
        String str = header;
        LevelStepTracker levelStepTracker = steps;
        char c = 0;
        if (levelStepTracker == null || (count = levelStepTracker.mNumStepDurations) <= 0) {
            return false;
        }
        if (!checkin) {
            pw.println(str);
        }
        String[] lineArgs = new String[5];
        int i = 0;
        while (i < count) {
            long duration = levelStepTracker.getDurationAt(i);
            int level = levelStepTracker.getLevelAt(i);
            long initMode = levelStepTracker.getInitModeAt(i);
            long modMode = levelStepTracker.getModModeAt(i);
            if (checkin) {
                lineArgs[c] = Long.toString(duration);
                lineArgs[1] = Integer.toString(level);
                if ((modMode & 3) == 0) {
                    count2 = count;
                    switch (((int) (initMode & 3)) + 1) {
                        case 1:
                            lineArgs[2] = "s-";
                            break;
                        case 2:
                            lineArgs[2] = "s+";
                            break;
                        case 3:
                            lineArgs[2] = "sd";
                            break;
                        case 4:
                            lineArgs[2] = "sds";
                            break;
                        default:
                            lineArgs[2] = "?";
                            break;
                    }
                } else {
                    count2 = count;
                    lineArgs[2] = "";
                }
                if ((modMode & 4) == 0) {
                    lineArgs[3] = (initMode & 4) != 0 ? "p+" : "p-";
                } else {
                    lineArgs[3] = "";
                }
                if ((modMode & 8) == 0) {
                    lineArgs[4] = (8 & initMode) != 0 ? "i+" : "i-";
                } else {
                    lineArgs[4] = "";
                }
                dumpLine(pw, 0, "i", str, lineArgs);
            } else {
                count2 = count;
                pw.print(prefix);
                pw.print("#");
                pw.print(i);
                pw.print(": ");
                TimeUtils.formatDuration(duration, pw);
                pw.print(" to ");
                pw.print(level);
                boolean haveModes = false;
                if ((modMode & 3) == 0) {
                    pw.print(" (");
                    switch (((int) (initMode & 3)) + 1) {
                        case 1:
                            pw.print("screen-off");
                            break;
                        case 2:
                            pw.print("screen-on");
                            break;
                        case 3:
                            pw.print("screen-doze");
                            break;
                        case 4:
                            pw.print("screen-doze-suspend");
                            break;
                        default:
                            pw.print("screen-?");
                            break;
                    }
                    haveModes = true;
                }
                if ((modMode & 4) == 0) {
                    pw.print(haveModes ? ", " : " (");
                    pw.print((initMode & 4) != 0 ? "power-save-on" : "power-save-off");
                    haveModes = true;
                }
                if ((modMode & 8) == 0) {
                    pw.print(haveModes ? ", " : " (");
                    pw.print((initMode & 8) != 0 ? "device-idle-on" : "device-idle-off");
                    haveModes = true;
                }
                if (haveModes) {
                    pw.print(")");
                }
                pw.println();
            }
            i++;
            count = count2;
            str = header;
            levelStepTracker = steps;
            c = 0;
        }
        return true;
    }

    private static synchronized void dumpDurationSteps(ProtoOutputStream proto, long fieldId, LevelStepTracker steps) {
        if (steps == null) {
            return;
        }
        int count = steps.mNumStepDurations;
        for (int i = 0; i < count; i++) {
            long token = proto.start(fieldId);
            proto.write(1112396529665L, steps.getDurationAt(i));
            proto.write(1120986464258L, steps.getLevelAt(i));
            long initMode = steps.getInitModeAt(i);
            long modMode = steps.getModModeAt(i);
            int ds = 0;
            if ((modMode & 3) == 0) {
                switch (((int) (3 & initMode)) + 1) {
                    case 1:
                        ds = 2;
                        break;
                    case 2:
                        ds = 1;
                        break;
                    case 3:
                        ds = 3;
                        break;
                    case 4:
                        ds = 4;
                        break;
                    default:
                        ds = 5;
                        break;
                }
            }
            proto.write(1159641169923L, ds);
            int psm = 0;
            if ((modMode & 4) == 0) {
                psm = (4 & initMode) == 0 ? 2 : 1;
            }
            proto.write(1159641169924L, psm);
            int im = 0;
            if ((modMode & 8) == 0) {
                im = (8 & initMode) == 0 ? 3 : 2;
            }
            proto.write(1159641169925L, im);
            proto.end(token);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0183  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized void dumpHistoryLocked(java.io.PrintWriter r29, int r30, long r31, boolean r33) {
        /*
            Method dump skipped, instructions count: 457
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.BatteryStats.dumpHistoryLocked(java.io.PrintWriter, int, long, boolean):void");
    }

    private synchronized void dumpDailyLevelStepSummary(PrintWriter pw, String prefix, String label, LevelStepTracker steps, StringBuilder tmpSb, int[] tmpOutInt) {
        if (steps == null) {
            return;
        }
        long timeRemaining = steps.computeTimeEstimate(0L, 0L, tmpOutInt);
        if (timeRemaining >= 0) {
            pw.print(prefix);
            pw.print(label);
            pw.print(" total time: ");
            tmpSb.setLength(0);
            formatTimeMs(tmpSb, timeRemaining);
            pw.print(tmpSb);
            pw.print(" (from ");
            pw.print(tmpOutInt[0]);
            pw.println(" steps)");
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < STEP_LEVEL_MODES_OF_INTEREST.length) {
                long estimatedTime = steps.computeTimeEstimate(STEP_LEVEL_MODES_OF_INTEREST[i2], STEP_LEVEL_MODE_VALUES[i2], tmpOutInt);
                if (estimatedTime > 0) {
                    pw.print(prefix);
                    pw.print(label);
                    pw.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    pw.print(STEP_LEVEL_MODE_LABELS[i2]);
                    pw.print(" time: ");
                    tmpSb.setLength(0);
                    formatTimeMs(tmpSb, estimatedTime);
                    pw.print(tmpSb);
                    pw.print(" (from ");
                    pw.print(tmpOutInt[0]);
                    pw.println(" steps)");
                }
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    private synchronized void dumpDailyPackageChanges(PrintWriter pw, String prefix, ArrayList<PackageChange> changes) {
        if (changes == null) {
            return;
        }
        pw.print(prefix);
        pw.println("Package changes:");
        for (int i = 0; i < changes.size(); i++) {
            PackageChange pc = changes.get(i);
            if (pc.mUpdate) {
                pw.print(prefix);
                pw.print("  Update ");
                pw.print(pc.mPackageName);
                pw.print(" vers=");
                pw.println(pc.mVersionCode);
            } else {
                pw.print(prefix);
                pw.print("  Uninstall ");
                pw.println(pc.mPackageName);
            }
        }
    }

    public synchronized void dumpLocked(Context context, PrintWriter pw, int flags, int reqUid, long histStart) {
        boolean z;
        ArrayList<PackageChange> pkgc;
        LevelStepTracker csteps;
        LevelStepTracker dsteps;
        boolean z2;
        int[] outInt;
        DailyItem dit;
        prepareForDumpLocked();
        boolean filtering = (flags & 14) != 0;
        if ((flags & 8) != 0 || !filtering) {
            long historyTotalSize = getHistoryTotalSize();
            long historyUsedSize = getHistoryUsedSize();
            if (startIteratingHistoryLocked()) {
                try {
                    pw.print("Battery History (");
                    pw.print((100 * historyUsedSize) / historyTotalSize);
                    pw.print("% used, ");
                    printSizeValue(pw, historyUsedSize);
                    pw.print(" used of ");
                    printSizeValue(pw, historyTotalSize);
                    pw.print(", ");
                    pw.print(getHistoryStringPoolSize());
                    pw.print(" strings using ");
                    printSizeValue(pw, getHistoryStringPoolBytes());
                    pw.println("):");
                    try {
                        dumpHistoryLocked(pw, flags, histStart, false);
                        pw.println();
                        finishIteratingHistoryLocked();
                    } catch (Throwable th) {
                        th = th;
                        finishIteratingHistoryLocked();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            if (startIteratingOldHistoryLocked()) {
                try {
                    HistoryItem rec = new HistoryItem();
                    pw.println("Old battery History:");
                    HistoryPrinter hprinter = new HistoryPrinter();
                    long baseTime = -1;
                    while (getNextOldHistoryLocked(rec)) {
                        if (baseTime < 0) {
                            baseTime = rec.time;
                        }
                        long baseTime2 = baseTime;
                        hprinter.printNextItem(pw, rec, baseTime2, false, (flags & 32) != 0);
                        baseTime = baseTime2;
                    }
                    pw.println();
                } finally {
                    finishIteratingOldHistoryLocked();
                }
            }
        }
        if (filtering && (flags & 6) == 0) {
            return;
        }
        if (!filtering) {
            SparseArray<? extends Uid> uidStats = getUidStats();
            int NU = uidStats.size();
            long nowRealtime = SystemClock.elapsedRealtime();
            boolean j = false;
            for (int i = 0; i < NU; i++) {
                Uid uid = uidStats.valueAt(i);
                SparseArray<? extends Uid.Pid> pids = uid.getPidStats();
                if (pids != null) {
                    boolean didPid = j;
                    for (int j2 = 0; j2 < pids.size(); j2++) {
                        Uid.Pid pid = pids.valueAt(j2);
                        if (!didPid) {
                            pw.println("Per-PID Stats:");
                            didPid = true;
                        }
                        long time = pid.mWakeSumMs + (pid.mWakeNesting > 0 ? nowRealtime - pid.mWakeStartMs : 0L);
                        pw.print("  PID ");
                        pw.print(pids.keyAt(j2));
                        pw.print(" wake time: ");
                        TimeUtils.formatDuration(time, pw);
                        pw.println("");
                    }
                    j = didPid;
                }
            }
            if (j) {
                pw.println();
            }
        }
        if (filtering && (flags & 2) == 0) {
            z = false;
        } else {
            if (dumpDurationSteps(pw, "  ", "Discharge step durations:", getDischargeLevelStepTracker(), false)) {
                long timeRemaining = computeBatteryTimeRemaining(SystemClock.elapsedRealtime() * 1000);
                if (timeRemaining >= 0) {
                    pw.print("  Estimated discharge time remaining: ");
                    TimeUtils.formatDuration(timeRemaining / 1000, pw);
                    pw.println();
                }
                LevelStepTracker steps = getDischargeLevelStepTracker();
                int i2 = 0;
                while (true) {
                    int i3 = i2;
                    if (i3 >= STEP_LEVEL_MODES_OF_INTEREST.length) {
                        break;
                    }
                    dumpTimeEstimate(pw, "  Estimated ", STEP_LEVEL_MODE_LABELS[i3], " time: ", steps.computeTimeEstimate(STEP_LEVEL_MODES_OF_INTEREST[i3], STEP_LEVEL_MODE_VALUES[i3], null));
                    i2 = i3 + 1;
                }
                pw.println();
            }
            z = false;
            if (dumpDurationSteps(pw, "  ", "Charge step durations:", getChargeLevelStepTracker(), false)) {
                long timeRemaining2 = computeChargeTimeRemaining(SystemClock.elapsedRealtime() * 1000);
                if (timeRemaining2 >= 0) {
                    pw.print("  Estimated charge time remaining: ");
                    TimeUtils.formatDuration(timeRemaining2 / 1000, pw);
                    pw.println();
                }
                pw.println();
            }
        }
        if (filtering && (flags & 4) == 0) {
            z2 = z;
        } else {
            pw.println("Daily stats:");
            pw.print("  Current start time: ");
            pw.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getCurrentDailyStartTime()).toString());
            pw.print("  Next min deadline: ");
            pw.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getNextMinDailyDeadline()).toString());
            pw.print("  Next max deadline: ");
            pw.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getNextMaxDailyDeadline()).toString());
            StringBuilder sb = new StringBuilder(64);
            int[] outInt2 = new int[1];
            LevelStepTracker dsteps2 = getDailyDischargeLevelStepTracker();
            LevelStepTracker csteps2 = getDailyChargeLevelStepTracker();
            ArrayList<PackageChange> pkgc2 = getDailyPackageChanges();
            if (dsteps2.mNumStepDurations > 0 || csteps2.mNumStepDurations > 0 || pkgc2 != null) {
                if ((flags & 4) != 0) {
                    pkgc = pkgc2;
                    csteps = csteps2;
                    dsteps = dsteps2;
                    z2 = z;
                    outInt = outInt2;
                } else if (filtering) {
                    pw.println("  Current daily steps:");
                    dumpDailyLevelStepSummary(pw, "    ", "Discharge", dsteps2, sb, outInt2);
                    z2 = z;
                    outInt = outInt2;
                    dumpDailyLevelStepSummary(pw, "    ", "Charge", csteps2, sb, outInt2);
                } else {
                    pkgc = pkgc2;
                    csteps = csteps2;
                    dsteps = dsteps2;
                    z2 = z;
                    outInt = outInt2;
                }
                if (dumpDurationSteps(pw, "    ", "  Current daily discharge step durations:", dsteps, z2)) {
                    dumpDailyLevelStepSummary(pw, "      ", "Discharge", dsteps, sb, outInt);
                }
                if (dumpDurationSteps(pw, "    ", "  Current daily charge step durations:", csteps, z2)) {
                    dumpDailyLevelStepSummary(pw, "      ", "Charge", csteps, sb, outInt);
                }
                dumpDailyPackageChanges(pw, "    ", pkgc);
            } else {
                z2 = z;
                outInt = outInt2;
            }
            int curIndex = z2;
            while (true) {
                DailyItem dit2 = getDailyItemLocked(curIndex);
                if (dit2 == null) {
                    break;
                }
                int curIndex2 = curIndex + 1;
                int curIndex3 = flags & 4;
                if (curIndex3 != 0) {
                    pw.println();
                }
                pw.print("  Daily from ");
                pw.print(DateFormat.format("yyyy-MM-dd-HH-mm-ss", dit2.mStartTime).toString());
                pw.print(" to ");
                pw.print(DateFormat.format("yyyy-MM-dd-HH-mm-ss", dit2.mEndTime).toString());
                pw.println(SettingsStringUtil.DELIMITER);
                if ((flags & 4) != 0) {
                    dit = dit2;
                } else if (filtering) {
                    int[] iArr = outInt;
                    dumpDailyLevelStepSummary(pw, "    ", "Discharge", dit2.mDischargeSteps, sb, iArr);
                    dumpDailyLevelStepSummary(pw, "    ", "Charge", dit2.mChargeSteps, sb, iArr);
                    curIndex = curIndex2;
                } else {
                    dit = dit2;
                }
                if (dumpDurationSteps(pw, "      ", "    Discharge step durations:", dit.mDischargeSteps, z2)) {
                    dumpDailyLevelStepSummary(pw, "        ", "Discharge", dit.mDischargeSteps, sb, outInt);
                }
                if (dumpDurationSteps(pw, "      ", "    Charge step durations:", dit.mChargeSteps, z2)) {
                    dumpDailyLevelStepSummary(pw, "        ", "Charge", dit.mChargeSteps, sb, outInt);
                }
                dumpDailyPackageChanges(pw, "    ", dit.mPackageChanges);
                curIndex = curIndex2;
            }
            pw.println();
        }
        if (filtering && (flags & 2) == 0) {
            return;
        }
        pw.println("Statistics since last charge:");
        pw.println("  System starts: " + getStartCount() + ", currently on battery: " + getIsOnBattery());
        dumpLocked(context, pw, "", 0, reqUid, (flags & 64) != 0 ? true : z2);
        pw.println();
    }

    public synchronized void dumpCheckinLocked(Context context, PrintWriter pw, List<ApplicationInfo> apps, int flags, long histStart) {
        prepareForDumpLocked();
        boolean z = true;
        dumpLine(pw, 0, "i", VERSION_DATA, 32, Integer.valueOf(getParcelVersion()), getStartPlatformVersion(), getEndPlatformVersion());
        long historyBaseTime = getHistoryBaseTime() + SystemClock.elapsedRealtime();
        if ((flags & 24) != 0 && startIteratingHistoryLocked()) {
            for (int i = 0; i < getHistoryStringPoolSize(); i++) {
                try {
                    pw.print(9);
                    pw.print(',');
                    pw.print(HISTORY_STRING_POOL);
                    pw.print(',');
                    pw.print(i);
                    pw.print(",");
                    pw.print(getHistoryTagPoolUid(i));
                    pw.print(",\"");
                    String str = getHistoryTagPoolString(i);
                    pw.print(str.replace("\\", "\\\\").replace("\"", "\\\""));
                    pw.print("\"");
                    pw.println();
                } finally {
                    finishIteratingHistoryLocked();
                }
            }
            dumpHistoryLocked(pw, flags, histStart, true);
        }
        if ((flags & 8) != 0) {
            return;
        }
        if (apps != null) {
            SparseArray<Pair<ArrayList<String>, MutableBoolean>> uids = new SparseArray<>();
            for (int i2 = 0; i2 < apps.size(); i2++) {
                ApplicationInfo ai = apps.get(i2);
                Pair<ArrayList<String>, MutableBoolean> pkgs = uids.get(UserHandle.getAppId(ai.uid));
                if (pkgs == null) {
                    pkgs = new Pair<>(new ArrayList(), new MutableBoolean(false));
                    uids.put(UserHandle.getAppId(ai.uid), pkgs);
                }
                ((ArrayList) pkgs.first).add(ai.packageName);
            }
            SparseArray<? extends Uid> uidStats = getUidStats();
            int NU = uidStats.size();
            String[] lineArgs = new String[2];
            int i3 = 0;
            while (i3 < NU) {
                int uid = UserHandle.getAppId(uidStats.keyAt(i3));
                Pair<ArrayList<String>, MutableBoolean> pkgs2 = uids.get(uid);
                if (pkgs2 != null && !((MutableBoolean) pkgs2.second).value) {
                    ((MutableBoolean) pkgs2.second).value = z;
                    int j = 0;
                    while (j < ((ArrayList) pkgs2.first).size()) {
                        lineArgs[0] = Integer.toString(uid);
                        lineArgs[1] = (String) ((ArrayList) pkgs2.first).get(j);
                        dumpLine(pw, 0, "i", "uid", lineArgs);
                        j++;
                        uids = uids;
                        uidStats = uidStats;
                    }
                }
                i3++;
                uids = uids;
                uidStats = uidStats;
                z = true;
            }
        }
        if ((flags & 4) == 0) {
            dumpDurationSteps(pw, "", DISCHARGE_STEP_DATA, getDischargeLevelStepTracker(), true);
            String[] lineArgs2 = new String[1];
            long timeRemaining = computeBatteryTimeRemaining(SystemClock.elapsedRealtime() * 1000);
            if (timeRemaining >= 0) {
                lineArgs2[0] = Long.toString(timeRemaining);
                dumpLine(pw, 0, "i", DISCHARGE_TIME_REMAIN_DATA, lineArgs2);
            }
            dumpDurationSteps(pw, "", CHARGE_STEP_DATA, getChargeLevelStepTracker(), true);
            long timeRemaining2 = computeChargeTimeRemaining(SystemClock.elapsedRealtime() * 1000);
            if (timeRemaining2 >= 0) {
                lineArgs2[0] = Long.toString(timeRemaining2);
                dumpLine(pw, 0, "i", CHARGE_TIME_REMAIN_DATA, lineArgs2);
            }
            dumpCheckinLocked(context, pw, 0, -1, (flags & 64) != 0);
        }
    }

    public synchronized void dumpProtoLocked(Context context, FileDescriptor fd, List<ApplicationInfo> apps, int flags, long histStart) {
        ProtoOutputStream proto = new ProtoOutputStream(fd);
        prepareForDumpLocked();
        if ((flags & 24) != 0) {
            dumpProtoHistoryLocked(proto, flags, histStart);
            proto.flush();
            return;
        }
        long bToken = proto.start(1146756268033L);
        proto.write(1120986464257L, 32);
        proto.write(1112396529666L, getParcelVersion());
        proto.write(1138166333443L, getStartPlatformVersion());
        proto.write(1138166333444L, getEndPlatformVersion());
        if ((flags & 4) == 0) {
            BatteryStatsHelper helper = new BatteryStatsHelper(context, false, (flags & 64) != 0);
            helper.create(this);
            helper.refreshStats(0, -1);
            dumpProtoAppsLocked(proto, helper, apps);
            dumpProtoSystemLocked(proto, helper);
        }
        proto.end(bToken);
        proto.flush();
    }

    private synchronized void dumpProtoAppsLocked(ProtoOutputStream proto, BatteryStatsHelper helper, List<ApplicationInfo> apps) {
        ArrayList<String> pkgs;
        ArrayList<String> pkgs2;
        long rawRealtimeMs;
        long j;
        SparseArray<BatterySipper> uidToSipper;
        ArrayMap<String, ? extends Timer> jobs;
        ArrayMap<String, ? extends Timer> syncs;
        long nToken;
        BatterySipper bs;
        int uid;
        long[] cpuFreqs;
        ArrayMap<String, SparseIntArray> completions;
        long cpuToken;
        long[] cpuFreqTimeMs;
        int i;
        int i2;
        SparseArray<? extends Uid> uidStats;
        long rawUptimeUs;
        long rawRealtimeUs;
        SparseArray<ArrayList<String>> aidToPackages;
        long batteryUptimeUs;
        Uid u;
        SparseArray<BatterySipper> uidToSipper2;
        ArrayList<String> pkgs3;
        long rawRealtimeMs2;
        Uid u2;
        SparseArray<BatterySipper> uidToSipper3;
        ArrayList<String> pkgs4;
        ArrayList<String> pkgs5;
        List<ApplicationInfo> list = apps;
        boolean z = false;
        long rawUptimeUs2 = SystemClock.uptimeMillis() * 1000;
        long rawRealtimeMs3 = SystemClock.elapsedRealtime();
        long rawRealtimeUs2 = rawRealtimeMs3 * 1000;
        long batteryUptimeUs2 = getBatteryUptime(rawUptimeUs2);
        SparseArray<ArrayList<String>> aidToPackages2 = new SparseArray<>();
        if (list != null) {
            int i3 = 0;
            while (i3 < apps.size()) {
                ApplicationInfo ai = list.get(i3);
                int aid = UserHandle.getAppId(ai.uid);
                ArrayList<String> pkgs6 = aidToPackages2.get(aid);
                if (pkgs6 == null) {
                    pkgs5 = new ArrayList<>();
                    aidToPackages2.put(aid, pkgs5);
                } else {
                    pkgs5 = pkgs6;
                }
                pkgs5.add(ai.packageName);
                i3++;
                list = apps;
            }
        }
        SparseArray<BatterySipper> uidToSipper4 = new SparseArray<>();
        List<BatterySipper> sippers = helper.getUsageList();
        if (sippers != null) {
            int i4 = 0;
            while (i4 < sippers.size()) {
                BatterySipper bs2 = sippers.get(i4);
                List<BatterySipper> sippers2 = sippers;
                boolean z2 = z;
                if (bs2.drainType == BatterySipper.DrainType.APP) {
                    uidToSipper4.put(bs2.uidObj.getUid(), bs2);
                }
                i4++;
                sippers = sippers2;
                z = z2;
            }
        }
        List<BatterySipper> sippers3 = sippers;
        SparseArray<? extends Uid> uidStats2 = getUidStats();
        int n = uidStats2.size();
        int iu = 0;
        while (true) {
            int iu2 = iu;
            if (iu2 >= n) {
                return;
            }
            int n2 = n;
            long uTkn = proto.start(2246267895813L);
            Uid u3 = uidStats2.valueAt(iu2);
            int uid2 = uidStats2.keyAt(iu2);
            proto.write(1120986464257L, uid2);
            SparseArray<ArrayList<String>> aidToPackages3 = aidToPackages2;
            ArrayList<String> pkgs7 = aidToPackages3.get(UserHandle.getAppId(uid2));
            if (pkgs7 == null) {
                pkgs = new ArrayList<>();
            } else {
                pkgs = pkgs7;
            }
            ArrayMap<String, ? extends Uid.Pkg> packageStats = u3.getPackageStats();
            int ipkg = packageStats.size() - 1;
            while (true) {
                int ipkg2 = ipkg;
                pkgs2 = pkgs;
                if (ipkg2 < 0) {
                    break;
                }
                String pkg = packageStats.keyAt(ipkg2);
                ArrayMap<String, ? extends Uid.Pkg> packageStats2 = packageStats;
                ArrayMap<String, ? extends Uid.Pkg.Serv> serviceStats = packageStats.valueAt(ipkg2).getServiceStats();
                if (serviceStats.size() == 0) {
                    aidToPackages = aidToPackages3;
                    batteryUptimeUs = batteryUptimeUs2;
                    u = u3;
                    uidToSipper2 = uidToSipper4;
                    uidStats = uidStats2;
                    rawUptimeUs = rawUptimeUs2;
                    rawRealtimeMs2 = rawRealtimeMs3;
                    rawRealtimeUs = rawRealtimeUs2;
                    pkgs3 = pkgs2;
                } else {
                    uidStats = uidStats2;
                    rawUptimeUs = rawUptimeUs2;
                    rawRealtimeUs = rawRealtimeUs2;
                    long rawRealtimeUs3 = proto.start(2246267895810L);
                    proto.write(1138166333441L, pkg);
                    ArrayList<String> pkgs8 = pkgs2;
                    pkgs8.remove(pkg);
                    int isvc = serviceStats.size() - 1;
                    while (isvc >= 0) {
                        Uid.Pkg.Serv ss = serviceStats.valueAt(isvc);
                        String pkg2 = pkg;
                        long rawRealtimeMs4 = rawRealtimeMs3;
                        long startTimeMs = roundUsToMs(ss.getStartTime(batteryUptimeUs2, 0));
                        SparseArray<ArrayList<String>> aidToPackages4 = aidToPackages3;
                        int starts = ss.getStarts(0);
                        long batteryUptimeUs3 = batteryUptimeUs2;
                        int launches = ss.getLaunches(0);
                        if (startTimeMs == 0 && starts == 0 && launches == 0) {
                            u2 = u3;
                            uidToSipper3 = uidToSipper4;
                            pkgs4 = pkgs8;
                        } else {
                            u2 = u3;
                            long sToken = proto.start(2246267895810L);
                            uidToSipper3 = uidToSipper4;
                            pkgs4 = pkgs8;
                            proto.write(1138166333441L, serviceStats.keyAt(isvc));
                            proto.write(1112396529666L, startTimeMs);
                            proto.write(1120986464259L, starts);
                            proto.write(1120986464260L, launches);
                            proto.end(sToken);
                        }
                        isvc--;
                        pkg = pkg2;
                        rawRealtimeMs3 = rawRealtimeMs4;
                        aidToPackages3 = aidToPackages4;
                        batteryUptimeUs2 = batteryUptimeUs3;
                        u3 = u2;
                        uidToSipper4 = uidToSipper3;
                        pkgs8 = pkgs4;
                    }
                    aidToPackages = aidToPackages3;
                    batteryUptimeUs = batteryUptimeUs2;
                    u = u3;
                    uidToSipper2 = uidToSipper4;
                    pkgs3 = pkgs8;
                    rawRealtimeMs2 = rawRealtimeMs3;
                    proto.end(rawRealtimeUs3);
                }
                ipkg = ipkg2 - 1;
                packageStats = packageStats2;
                rawUptimeUs2 = rawUptimeUs;
                uidStats2 = uidStats;
                rawRealtimeUs2 = rawRealtimeUs;
                rawRealtimeMs3 = rawRealtimeMs2;
                aidToPackages3 = aidToPackages;
                batteryUptimeUs2 = batteryUptimeUs;
                u3 = u;
                uidToSipper4 = uidToSipper2;
                pkgs = pkgs3;
            }
            ArrayMap<String, ? extends Uid.Pkg> packageStats3 = packageStats;
            SparseArray<ArrayList<String>> aidToPackages5 = aidToPackages3;
            long batteryUptimeUs4 = batteryUptimeUs2;
            Uid u4 = u3;
            SparseArray<BatterySipper> uidToSipper5 = uidToSipper4;
            SparseArray<? extends Uid> uidStats3 = uidStats2;
            long rawUptimeUs3 = rawUptimeUs2;
            long rawRealtimeMs5 = rawRealtimeMs3;
            long rawRealtimeUs4 = rawRealtimeUs2;
            Iterator<String> it = pkgs2.iterator();
            while (it.hasNext()) {
                String p = it.next();
                long pToken = proto.start(2246267895810L);
                proto.write(1138166333441L, p);
                proto.end(pToken);
            }
            if (u4.getAggregatedPartialWakelockTimer() != null) {
                Timer timer = u4.getAggregatedPartialWakelockTimer();
                rawRealtimeMs = rawRealtimeMs5;
                long totTimeMs = timer.getTotalDurationMsLocked(rawRealtimeMs);
                Timer bgTimer = timer.getSubTimer();
                long bgTimeMs = bgTimer != null ? bgTimer.getTotalDurationMsLocked(rawRealtimeMs) : 0L;
                long awToken = proto.start(1146756268056L);
                proto.write(1112396529665L, totTimeMs);
                proto.write(1112396529666L, bgTimeMs);
                proto.end(awToken);
            } else {
                rawRealtimeMs = rawRealtimeMs5;
            }
            long uTkn2 = uTkn;
            ArrayMap<String, ? extends Uid.Pkg> packageStats4 = packageStats3;
            List<BatterySipper> sippers4 = sippers3;
            int n3 = n2;
            dumpTimer(proto, 1146756268040L, u4.getAudioTurnedOnTimer(), rawRealtimeUs4, 0);
            dumpControllerActivityProto(proto, 1146756268035L, u4.getBluetoothControllerActivity(), 0);
            Timer bleTimer = u4.getBluetoothScanTimer();
            if (bleTimer != null) {
                long bmToken = proto.start(1146756268038L);
                dumpTimer(proto, 1146756268033L, bleTimer, rawRealtimeUs4, 0);
                dumpTimer(proto, 1146756268034L, u4.getBluetoothScanBackgroundTimer(), rawRealtimeUs4, 0);
                dumpTimer(proto, 1146756268035L, u4.getBluetoothUnoptimizedScanTimer(), rawRealtimeUs4, 0);
                dumpTimer(proto, 1146756268036L, u4.getBluetoothUnoptimizedScanBackgroundTimer(), rawRealtimeUs4, 0);
                if (u4.getBluetoothScanResultCounter() != null) {
                    i = u4.getBluetoothScanResultCounter().getCountLocked(0);
                } else {
                    i = 0;
                }
                proto.write(1120986464261L, i);
                if (u4.getBluetoothScanResultBgCounter() != null) {
                    i2 = u4.getBluetoothScanResultBgCounter().getCountLocked(0);
                } else {
                    i2 = 0;
                }
                proto.write(1120986464262L, i2);
                proto.end(bmToken);
            }
            dumpTimer(proto, 1146756268041L, u4.getCameraTurnedOnTimer(), rawRealtimeUs4, 0);
            long cpuToken2 = proto.start(1146756268039L);
            proto.write(1112396529665L, roundUsToMs(u4.getUserCpuTimeUs(0)));
            proto.write(1112396529666L, roundUsToMs(u4.getSystemCpuTimeUs(0)));
            long[] cpuFreqs2 = getCpuFreqs();
            if (cpuFreqs2 != null && (cpuFreqTimeMs = u4.getCpuFreqTimes(0)) != null && cpuFreqTimeMs.length == cpuFreqs2.length) {
                long[] screenOffCpuFreqTimeMs = u4.getScreenOffCpuFreqTimes(0);
                if (screenOffCpuFreqTimeMs == null) {
                    screenOffCpuFreqTimeMs = new long[cpuFreqTimeMs.length];
                }
                int ic = 0;
                while (ic < cpuFreqTimeMs.length) {
                    long cToken = proto.start(2246267895811L);
                    proto.write(1120986464257L, ic + 1);
                    proto.write(1112396529666L, cpuFreqTimeMs[ic]);
                    proto.write(1112396529667L, screenOffCpuFreqTimeMs[ic]);
                    proto.end(cToken);
                    ic++;
                    packageStats4 = packageStats4;
                    rawRealtimeMs = rawRealtimeMs;
                    n3 = n3;
                    uTkn2 = uTkn2;
                }
            }
            ArrayMap<String, ? extends Uid.Pkg> packageStats5 = packageStats4;
            int n4 = n3;
            long rawRealtimeMs6 = rawRealtimeMs;
            long uTkn3 = uTkn2;
            int procState = 0;
            while (true) {
                j = 1159641169921L;
                if (procState >= 7) {
                    break;
                }
                long[] timesMs = u4.getCpuFreqTimes(0, procState);
                if (timesMs == null || timesMs.length != cpuFreqs2.length) {
                    cpuToken = cpuToken2;
                } else {
                    long[] screenOffTimesMs = u4.getScreenOffCpuFreqTimes(0, procState);
                    if (screenOffTimesMs == null) {
                        screenOffTimesMs = new long[timesMs.length];
                    }
                    long procToken = proto.start(2246267895812L);
                    proto.write(1159641169921L, procState);
                    int ic2 = 0;
                    while (ic2 < timesMs.length) {
                        long cToken2 = proto.start(2246267895810L);
                        proto.write(1120986464257L, ic2 + 1);
                        proto.write(1112396529666L, timesMs[ic2]);
                        proto.write(1112396529667L, screenOffTimesMs[ic2]);
                        proto.end(cToken2);
                        ic2++;
                        cpuToken2 = cpuToken2;
                    }
                    cpuToken = cpuToken2;
                    proto.end(procToken);
                }
                procState++;
                cpuToken2 = cpuToken;
            }
            proto.end(cpuToken2);
            long[] cpuFreqs3 = cpuFreqs2;
            dumpTimer(proto, 1146756268042L, u4.getFlashlightTurnedOnTimer(), rawRealtimeUs4, 0);
            dumpTimer(proto, 1146756268043L, u4.getForegroundActivityTimer(), rawRealtimeUs4, 0);
            dumpTimer(proto, 1146756268044L, u4.getForegroundServiceTimer(), rawRealtimeUs4, 0);
            ArrayMap<String, SparseIntArray> completions2 = u4.getJobCompletionStats();
            int[] reasons = {0, 1, 2, 3, 4};
            int ic3 = 0;
            while (ic3 < completions2.size()) {
                SparseIntArray types = completions2.valueAt(ic3);
                if (types == null) {
                    cpuFreqs = cpuFreqs3;
                    completions = completions2;
                } else {
                    long jcToken = proto.start(2246267895824L);
                    proto.write(1138166333441L, completions2.keyAt(ic3));
                    int length = reasons.length;
                    int i5 = 0;
                    while (i5 < length) {
                        int r = reasons[i5];
                        long[] cpuFreqs4 = cpuFreqs3;
                        long rToken = proto.start(2246267895810L);
                        proto.write(j, r);
                        proto.write(1120986464258L, types.get(r, 0));
                        proto.end(rToken);
                        i5++;
                        cpuFreqs3 = cpuFreqs4;
                        completions2 = completions2;
                        j = 1159641169921L;
                    }
                    cpuFreqs = cpuFreqs3;
                    completions = completions2;
                    proto.end(jcToken);
                }
                ic3++;
                cpuFreqs3 = cpuFreqs;
                completions2 = completions;
                j = 1159641169921L;
            }
            ArrayMap<String, ? extends Timer> jobs2 = u4.getJobStats();
            int ij = jobs2.size() - 1;
            while (true) {
                int ij2 = ij;
                if (ij2 < 0) {
                    break;
                }
                Timer timer2 = jobs2.valueAt(ij2);
                Timer bgTimer2 = timer2.getSubTimer();
                long jToken = proto.start(UidProto.JOBS);
                proto.write(1138166333441L, jobs2.keyAt(ij2));
                dumpTimer(proto, 1146756268034L, timer2, rawRealtimeUs4, 0);
                dumpTimer(proto, 1146756268035L, bgTimer2, rawRealtimeUs4, 0);
                proto.end(jToken);
                ij = ij2 - 1;
            }
            dumpControllerActivityProto(proto, 1146756268036L, u4.getModemControllerActivity(), 0);
            long nToken2 = proto.start(1146756268049L);
            proto.write(1112396529665L, u4.getNetworkActivityBytes(0, 0));
            proto.write(1112396529666L, u4.getNetworkActivityBytes(1, 0));
            proto.write(1112396529667L, u4.getNetworkActivityBytes(2, 0));
            proto.write(1112396529668L, u4.getNetworkActivityBytes(3, 0));
            proto.write(1112396529669L, u4.getNetworkActivityBytes(4, 0));
            proto.write(1112396529670L, u4.getNetworkActivityBytes(5, 0));
            proto.write(1112396529671L, u4.getNetworkActivityPackets(0, 0));
            proto.write(1112396529672L, u4.getNetworkActivityPackets(1, 0));
            proto.write(1112396529673L, u4.getNetworkActivityPackets(2, 0));
            proto.write(1112396529674L, u4.getNetworkActivityPackets(3, 0));
            proto.write(1112396529675L, roundUsToMs(u4.getMobileRadioActiveTime(0)));
            proto.write(1120986464268L, u4.getMobileRadioActiveCount(0));
            proto.write(1120986464269L, u4.getMobileRadioApWakeupCount(0));
            proto.write(UidProto.Network.WIFI_WAKEUP_COUNT, u4.getWifiRadioApWakeupCount(0));
            proto.write(UidProto.Network.MOBILE_BYTES_BG_RX, u4.getNetworkActivityBytes(6, 0));
            proto.write(1112396529680L, u4.getNetworkActivityBytes(7, 0));
            proto.write(1112396529681L, u4.getNetworkActivityBytes(8, 0));
            proto.write(UidProto.Network.WIFI_BYTES_BG_TX, u4.getNetworkActivityBytes(9, 0));
            proto.write(1112396529683L, u4.getNetworkActivityPackets(6, 0));
            proto.write(1112396529684L, u4.getNetworkActivityPackets(7, 0));
            proto.write(1112396529685L, u4.getNetworkActivityPackets(8, 0));
            proto.write(UidProto.Network.WIFI_PACKETS_BG_TX, u4.getNetworkActivityPackets(9, 0));
            proto.end(nToken2);
            int uid3 = uid2;
            BatterySipper bs3 = uidToSipper5.get(uid3);
            if (bs3 != null) {
                long bsToken = proto.start(1146756268050L);
                uidToSipper = uidToSipper5;
                proto.write(1103806595073L, bs3.totalPowerMah);
                proto.write(1133871366146L, bs3.shouldHide);
                proto.write(1103806595075L, bs3.screenPowerMah);
                proto.write(1103806595076L, bs3.proportionalSmearMah);
                proto.end(bsToken);
            } else {
                uidToSipper = uidToSipper5;
            }
            ArrayMap<String, ? extends Uid.Proc> processStats = u4.getProcessStats();
            int ipr = processStats.size() - 1;
            while (ipr >= 0) {
                Uid.Proc ps = processStats.valueAt(ipr);
                long prToken = proto.start(2246267895827L);
                proto.write(1138166333441L, processStats.keyAt(ipr));
                proto.write(1112396529666L, ps.getUserTime(0));
                proto.write(1112396529667L, ps.getSystemTime(0));
                proto.write(1112396529668L, ps.getForegroundTime(0));
                proto.write(1120986464261L, ps.getStarts(0));
                proto.write(1120986464262L, ps.getNumAnrs(0));
                proto.write(1120986464263L, ps.getNumCrashes(0));
                proto.end(prToken);
                ipr--;
                processStats = processStats;
                uid3 = uid3;
            }
            int uid4 = uid3;
            SparseArray<? extends Uid.Sensor> sensors = u4.getSensorStats();
            int ise = 0;
            while (true) {
                int ise2 = ise;
                int ise3 = sensors.size();
                if (ise2 >= ise3) {
                    break;
                }
                Uid.Sensor se = sensors.valueAt(ise2);
                Timer timer3 = se.getSensorTime();
                if (timer3 == null) {
                    bs = bs3;
                    uid = uid4;
                } else {
                    Timer bgTimer3 = se.getSensorBackgroundTime();
                    int sensorNumber = sensors.keyAt(ise2);
                    long seToken = proto.start(UidProto.SENSORS);
                    proto.write(1120986464257L, sensorNumber);
                    bs = bs3;
                    uid = uid4;
                    dumpTimer(proto, 1146756268034L, timer3, rawRealtimeUs4, 0);
                    dumpTimer(proto, 1146756268035L, bgTimer3, rawRealtimeUs4, 0);
                    proto.end(seToken);
                }
                ise = ise2 + 1;
                bs3 = bs;
                uid4 = uid;
            }
            int ips = 0;
            while (ips < 7) {
                long rawRealtimeUs5 = rawRealtimeUs4;
                long durMs = roundUsToMs(u4.getProcessStateTime(ips, rawRealtimeUs5, 0));
                if (durMs == 0) {
                    nToken = nToken2;
                } else {
                    long stToken = proto.start(2246267895828L);
                    proto.write(1159641169921L, ips);
                    nToken = nToken2;
                    proto.write(1112396529666L, durMs);
                    proto.end(stToken);
                }
                ips++;
                rawRealtimeUs4 = rawRealtimeUs5;
                nToken2 = nToken;
            }
            long rawRealtimeUs6 = rawRealtimeUs4;
            ArrayMap<String, ? extends Timer> syncs2 = u4.getSyncStats();
            int isy = syncs2.size() - 1;
            while (true) {
                int isy2 = isy;
                if (isy2 < 0) {
                    break;
                }
                Timer timer4 = syncs2.valueAt(isy2);
                Timer bgTimer4 = timer4.getSubTimer();
                long syToken = proto.start(2246267895830L);
                proto.write(1138166333441L, syncs2.keyAt(isy2));
                dumpTimer(proto, 1146756268034L, timer4, rawRealtimeUs6, 0);
                dumpTimer(proto, 1146756268035L, bgTimer4, rawRealtimeUs6, 0);
                proto.end(syToken);
                isy = isy2 - 1;
            }
            if (u4.hasUserActivity()) {
                int i6 = 0;
                while (i6 < 4) {
                    int val = u4.getUserActivityCount(i6, 0);
                    if (val == 0) {
                        syncs = syncs2;
                    } else {
                        long uaToken = proto.start(UidProto.USER_ACTIVITY);
                        proto.write(1159641169921L, i6);
                        syncs = syncs2;
                        proto.write(1120986464258L, val);
                        proto.end(uaToken);
                    }
                    i6++;
                    syncs2 = syncs;
                }
            }
            dumpTimer(proto, 1146756268045L, u4.getVibratorOnTimer(), rawRealtimeUs6, 0);
            dumpTimer(proto, UidProto.VIDEO, u4.getVideoTurnedOnTimer(), rawRealtimeUs6, 0);
            ArrayMap<String, ? extends Uid.Wakelock> wakelocks = u4.getWakelockStats();
            int iw = wakelocks.size() - 1;
            while (true) {
                int iw2 = iw;
                if (iw2 < 0) {
                    break;
                }
                Uid.Wakelock wl = wakelocks.valueAt(iw2);
                long wToken = proto.start(2246267895833L);
                proto.write(1138166333441L, wakelocks.keyAt(iw2));
                ArrayMap<String, ? extends Uid.Wakelock> wakelocks2 = wakelocks;
                dumpTimer(proto, 1146756268034L, wl.getWakeTime(1), rawRealtimeUs6, 0);
                Timer pTimer = wl.getWakeTime(0);
                if (pTimer != null) {
                    jobs = jobs2;
                    dumpTimer(proto, 1146756268035L, pTimer, rawRealtimeUs6, 0);
                    dumpTimer(proto, 1146756268036L, pTimer.getSubTimer(), rawRealtimeUs6, 0);
                } else {
                    jobs = jobs2;
                }
                dumpTimer(proto, 1146756268037L, wl.getWakeTime(2), rawRealtimeUs6, 0);
                proto.end(wToken);
                iw = iw2 - 1;
                wakelocks = wakelocks2;
                jobs2 = jobs;
            }
            dumpTimer(proto, UidProto.WIFI_MULTICAST_WAKELOCK, u4.getMulticastWakelockStats(), rawRealtimeUs6, 0);
            for (int ipkg3 = packageStats5.size() - 1; ipkg3 >= 0; ipkg3--) {
                ArrayMap<String, ? extends Counter> alarms = packageStats5.valueAt(ipkg3).getWakeupAlarmStats();
                for (int iwa = alarms.size() - 1; iwa >= 0; iwa--) {
                    long waToken = proto.start(2246267895834L);
                    proto.write(1138166333441L, alarms.keyAt(iwa));
                    proto.write(1120986464258L, alarms.valueAt(iwa).getCountLocked(0));
                    proto.end(waToken);
                }
            }
            dumpControllerActivityProto(proto, 1146756268037L, u4.getWifiControllerActivity(), 0);
            long wToken2 = proto.start(1146756268059L);
            proto.write(1112396529665L, roundUsToMs(u4.getFullWifiLockTime(rawRealtimeUs6, 0)));
            dumpTimer(proto, 1146756268035L, u4.getWifiScanTimer(), rawRealtimeUs6, 0);
            proto.write(1112396529666L, roundUsToMs(u4.getWifiRunningTime(rawRealtimeUs6, 0)));
            dumpTimer(proto, 1146756268036L, u4.getWifiScanBackgroundTimer(), rawRealtimeUs6, 0);
            proto.end(wToken2);
            proto.end(uTkn3);
            iu = iu2 + 1;
            rawRealtimeUs2 = rawRealtimeUs6;
            aidToPackages2 = aidToPackages5;
            batteryUptimeUs2 = batteryUptimeUs4;
            rawUptimeUs2 = rawUptimeUs3;
            uidStats2 = uidStats3;
            sippers3 = sippers4;
            rawRealtimeMs3 = rawRealtimeMs6;
            n = n4;
            uidToSipper4 = uidToSipper;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00f2 A[Catch: all -> 0x0247, TryCatch #0 {all -> 0x0247, blocks: (B:6:0x003b, B:8:0x0041, B:9:0x0067, B:10:0x007a, B:12:0x0083, B:15:0x008b, B:20:0x0099, B:22:0x009e, B:24:0x00a3, B:26:0x00a8, B:29:0x00af, B:31:0x00b5, B:35:0x00c4, B:43:0x00f2, B:45:0x00f6, B:49:0x00fe, B:51:0x010a, B:54:0x011e, B:70:0x01ce, B:57:0x0131, B:58:0x0139, B:60:0x013f, B:61:0x014e, B:63:0x015e, B:67:0x0185, B:71:0x01d9, B:74:0x01f5, B:78:0x01fd, B:37:0x00d9, B:41:0x00e2, B:82:0x0220), top: B:88:0x003b }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01e8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized void dumpProtoHistoryLocked(android.util.proto.ProtoOutputStream r30, int r31, long r32) {
        /*
            Method dump skipped, instructions count: 588
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.BatteryStats.dumpProtoHistoryLocked(android.util.proto.ProtoOutputStream, int, long):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private synchronized void dumpProtoSystemLocked(ProtoOutputStream proto, BatteryStatsHelper helper) {
        long rawRealtimeUs;
        int i;
        long pdcToken;
        long sToken = proto.start(1146756268038L);
        long rawUptimeUs = SystemClock.uptimeMillis() * 1000;
        long rawRealtimeMs = SystemClock.elapsedRealtime();
        long rawRealtimeUs2 = rawRealtimeMs * 1000;
        long bToken = proto.start(1146756268033L);
        proto.write(1112396529665L, getStartClockTime());
        proto.write(1112396529666L, getStartCount());
        proto.write(1112396529667L, computeRealtime(rawRealtimeUs2, 0) / 1000);
        proto.write(1112396529668L, computeUptime(rawUptimeUs, 0) / 1000);
        proto.write(1112396529669L, computeBatteryRealtime(rawRealtimeUs2, 0) / 1000);
        proto.write(1112396529670L, computeBatteryUptime(rawUptimeUs, 0) / 1000);
        proto.write(1112396529671L, computeBatteryScreenOffRealtime(rawRealtimeUs2, 0) / 1000);
        proto.write(1112396529672L, computeBatteryScreenOffUptime(rawUptimeUs, 0) / 1000);
        proto.write(1112396529673L, getScreenDozeTime(rawRealtimeUs2, 0) / 1000);
        proto.write(1112396529674L, getEstimatedBatteryCapacity());
        proto.write(1112396529675L, getMinLearnedBatteryCapacity());
        proto.write(1112396529676L, getMaxLearnedBatteryCapacity());
        proto.end(bToken);
        long bdToken = proto.start(1146756268034L);
        proto.write(1120986464257L, getLowDischargeAmountSinceCharge());
        proto.write(1120986464258L, getHighDischargeAmountSinceCharge());
        proto.write(1120986464259L, getDischargeAmountScreenOnSinceCharge());
        proto.write(1120986464260L, getDischargeAmountScreenOffSinceCharge());
        proto.write(1120986464261L, getDischargeAmountScreenDozeSinceCharge());
        proto.write(1112396529670L, getUahDischarge(0) / 1000);
        proto.write(1112396529671L, getUahDischargeScreenOff(0) / 1000);
        proto.write(1112396529672L, getUahDischargeScreenDoze(0) / 1000);
        proto.write(1112396529673L, getUahDischargeLightDoze(0) / 1000);
        proto.write(1112396529674L, getUahDischargeDeepDoze(0) / 1000);
        proto.end(bdToken);
        long timeRemainingUs = computeChargeTimeRemaining(rawRealtimeUs2);
        if (timeRemainingUs >= 0) {
            proto.write(1112396529667L, timeRemainingUs / 1000);
        } else {
            timeRemainingUs = computeBatteryTimeRemaining(rawRealtimeUs2);
            if (timeRemainingUs >= 0) {
                proto.write(1112396529668L, timeRemainingUs / 1000);
            } else {
                proto.write(1112396529668L, -1);
            }
        }
        dumpDurationSteps(proto, 2246267895813L, getChargeLevelStepTracker());
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= 21) {
                break;
            }
            boolean isNone = i3 == 0;
            int telephonyNetworkType = i3;
            if (i3 == 20) {
                telephonyNetworkType = 0;
            }
            int telephonyNetworkType2 = telephonyNetworkType;
            long rawRealtimeUs3 = rawRealtimeUs2;
            long pdcToken2 = proto.start(2246267895816L);
            if (isNone) {
                pdcToken = pdcToken2;
                proto.write(1133871366146L, isNone);
            } else {
                pdcToken = pdcToken2;
                proto.write(1159641169921L, telephonyNetworkType2);
            }
            rawRealtimeUs2 = rawRealtimeUs3;
            dumpTimer(proto, 1146756268035L, getPhoneDataConnectionTimer(i3), rawRealtimeUs2, 0);
            proto.end(pdcToken);
            i2 = i3 + 1;
            bdToken = bdToken;
        }
        long rawRealtimeUs4 = rawRealtimeUs2;
        dumpDurationSteps(proto, 2246267895814L, getDischargeLevelStepTracker());
        long[] cpuFreqs = getCpuFreqs();
        if (cpuFreqs != null) {
            for (long i4 : cpuFreqs) {
                proto.write(SystemProto.CPU_FREQUENCY, i4);
            }
        }
        dumpControllerActivityProto(proto, 1146756268041L, getBluetoothControllerActivity(), 0);
        dumpControllerActivityProto(proto, 1146756268042L, getModemControllerActivity(), 0);
        long gnToken = proto.start(1146756268044L);
        proto.write(1112396529665L, getNetworkActivityBytes(0, 0));
        proto.write(1112396529666L, getNetworkActivityBytes(1, 0));
        proto.write(1112396529669L, getNetworkActivityPackets(0, 0));
        proto.write(1112396529670L, getNetworkActivityPackets(1, 0));
        proto.write(1112396529667L, getNetworkActivityBytes(2, 0));
        proto.write(1112396529668L, getNetworkActivityBytes(3, 0));
        proto.write(1112396529671L, getNetworkActivityPackets(2, 0));
        proto.write(1112396529672L, getNetworkActivityPackets(3, 0));
        proto.write(1112396529673L, getNetworkActivityBytes(4, 0));
        proto.write(1112396529674L, getNetworkActivityBytes(5, 0));
        proto.end(gnToken);
        dumpControllerActivityProto(proto, 1146756268043L, getWifiControllerActivity(), 0);
        long gwToken = proto.start(1146756268045L);
        long rawRealtimeUs5 = rawRealtimeUs4;
        proto.write(1112396529665L, getWifiOnTime(rawRealtimeUs4, 0) / 1000);
        proto.write(1112396529666L, getGlobalWifiRunningTime(rawRealtimeUs5, 0) / 1000);
        proto.end(gwToken);
        Map<String, ? extends Timer> kernelWakelocks = getKernelWakelockStats();
        for (Map.Entry<String, ? extends Timer> ent : kernelWakelocks.entrySet()) {
            long kwToken = proto.start(SystemProto.KERNEL_WAKELOCK);
            proto.write(1138166333441L, ent.getKey());
            dumpTimer(proto, 1146756268034L, ent.getValue(), rawRealtimeUs5, 0);
            proto.end(kwToken);
            gwToken = gwToken;
            kernelWakelocks = kernelWakelocks;
        }
        int i5 = 1;
        SparseArray<? extends Uid> uidStats = getUidStats();
        long fullWakeLockTimeTotalUs = 0;
        long partialWakeLockTimeTotalUs = 0;
        int iu = 0;
        while (iu < uidStats.size()) {
            Uid u = uidStats.valueAt(iu);
            ArrayMap<String, ? extends Uid.Wakelock> wakelocks = u.getWakelockStats();
            int iw = wakelocks.size() - i5;
            while (iw >= 0) {
                Uid.Wakelock wl = wakelocks.valueAt(iw);
                Timer fullWakeTimer = wl.getWakeTime(i5);
                if (fullWakeTimer != null) {
                    i = 0;
                    fullWakeLockTimeTotalUs += fullWakeTimer.getTotalTimeLocked(rawRealtimeUs5, 0);
                } else {
                    i = 0;
                }
                Timer partialWakeTimer = wl.getWakeTime(i);
                if (partialWakeTimer != null) {
                    partialWakeLockTimeTotalUs += partialWakeTimer.getTotalTimeLocked(rawRealtimeUs5, i);
                }
                iw--;
                i5 = 1;
            }
            iu++;
            i5 = 1;
        }
        long mToken = proto.start(SystemProto.MISC);
        proto.write(1112396529665L, getScreenOnTime(rawRealtimeUs5, 0) / 1000);
        proto.write(1112396529666L, getPhoneOnTime(rawRealtimeUs5, 0) / 1000);
        proto.write(1112396529667L, fullWakeLockTimeTotalUs / 1000);
        proto.write(1112396529668L, partialWakeLockTimeTotalUs / 1000);
        proto.write(1112396529669L, getMobileRadioActiveTime(rawRealtimeUs5, 0) / 1000);
        proto.write(1112396529670L, getMobileRadioActiveAdjustedTime(0) / 1000);
        proto.write(1120986464263L, getMobileRadioActiveCount(0));
        proto.write(1120986464264L, getMobileRadioActiveUnknownTime(0) / 1000);
        proto.write(1112396529673L, getInteractiveTime(rawRealtimeUs5, 0) / 1000);
        proto.write(1112396529674L, getPowerSaveModeEnabledTime(rawRealtimeUs5, 0) / 1000);
        proto.write(1120986464267L, getNumConnectivityChange(0));
        proto.write(1112396529676L, getDeviceIdleModeTime(2, rawRealtimeUs5, 0) / 1000);
        proto.write(1120986464269L, getDeviceIdleModeCount(2, 0));
        proto.write(SystemProto.Misc.DEEP_DOZE_IDLING_DURATION_MS, getDeviceIdlingTime(2, rawRealtimeUs5, 0) / 1000);
        proto.write(SystemProto.Misc.DEEP_DOZE_IDLING_COUNT, getDeviceIdlingCount(2, 0));
        proto.write(1112396529680L, getLongestDeviceIdleModeTime(2));
        proto.write(1112396529681L, getDeviceIdleModeTime(1, rawRealtimeUs5, 0) / 1000);
        proto.write(1120986464274L, getDeviceIdleModeCount(1, 0));
        proto.write(1112396529683L, getDeviceIdlingTime(1, rawRealtimeUs5, 0) / 1000);
        proto.write(SystemProto.Misc.LIGHT_DOZE_IDLING_COUNT, getDeviceIdlingCount(1, 0));
        proto.write(1112396529685L, getLongestDeviceIdleModeTime(1));
        proto.end(mToken);
        long multicastWakeLockTimeTotalUs = getWifiMulticastWakelockTime(rawRealtimeUs5, 0);
        int multicastWakeLockCountTotal = getWifiMulticastWakelockCount(0);
        long wmctToken = proto.start(SystemProto.WIFI_MULTICAST_WAKELOCK_TOTAL);
        proto.write(1112396529665L, multicastWakeLockTimeTotalUs / 1000);
        proto.write(1120986464258L, multicastWakeLockCountTotal);
        proto.end(wmctToken);
        List<BatterySipper> sippers = helper.getUsageList();
        if (sippers != null) {
            int i6 = 0;
            while (i6 < sippers.size()) {
                BatterySipper bs = sippers.get(i6);
                int n = 0;
                int uid = 0;
                long wmctToken2 = wmctToken;
                switch (bs.drainType) {
                    case AMBIENT_DISPLAY:
                        n = 13;
                        break;
                    case IDLE:
                        n = 1;
                        break;
                    case CELL:
                        n = 2;
                        break;
                    case PHONE:
                        n = 3;
                        break;
                    case WIFI:
                        n = 4;
                        break;
                    case BLUETOOTH:
                        n = 5;
                        break;
                    case SCREEN:
                        n = 7;
                        break;
                    case FLASHLIGHT:
                        n = 6;
                        break;
                    case APP:
                        rawRealtimeUs = rawRealtimeUs5;
                        continue;
                        i6++;
                        wmctToken = wmctToken2;
                        rawRealtimeUs5 = rawRealtimeUs;
                    case USER:
                        n = 8;
                        uid = UserHandle.getUid(bs.userId, 0);
                        break;
                    case UNACCOUNTED:
                        n = 9;
                        break;
                    case OVERCOUNTED:
                        n = 10;
                        break;
                    case CAMERA:
                        n = 11;
                        break;
                    case MEMORY:
                        n = 12;
                        break;
                }
                long puiToken = proto.start(SystemProto.POWER_USE_ITEM);
                rawRealtimeUs = rawRealtimeUs5;
                proto.write(1159641169921L, n);
                proto.write(1120986464258L, uid);
                proto.write(1103806595075L, bs.totalPowerMah);
                proto.write(1133871366148L, bs.shouldHide);
                proto.write(SystemProto.PowerUseItem.SCREEN_POWER_MAH, bs.screenPowerMah);
                proto.write(SystemProto.PowerUseItem.PROPORTIONAL_SMEAR_MAH, bs.proportionalSmearMah);
                proto.end(puiToken);
                i6++;
                wmctToken = wmctToken2;
                rawRealtimeUs5 = rawRealtimeUs;
            }
        }
        long rawRealtimeUs6 = rawRealtimeUs5;
        long pusToken = proto.start(1146756268050L);
        proto.write(1103806595073L, helper.getPowerProfile().getBatteryCapacity());
        proto.write(SystemProto.PowerUseSummary.COMPUTED_POWER_MAH, helper.getComputedPower());
        proto.write(1103806595075L, helper.getMinDrainedPower());
        proto.write(1103806595076L, helper.getMaxDrainedPower());
        proto.end(pusToken);
        Map<String, ? extends Timer> rpmStats = getRpmStats();
        Map<String, ? extends Timer> screenOffRpmStats = getScreenOffRpmStats();
        Iterator<Map.Entry<String, ? extends Timer>> it = rpmStats.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ? extends Timer> ent2 = it.next();
            long rpmToken = proto.start(2246267895827L);
            proto.write(1138166333441L, ent2.getKey());
            Map<String, ? extends Timer> screenOffRpmStats2 = screenOffRpmStats;
            dumpTimer(proto, 1146756268034L, ent2.getValue(), rawRealtimeUs6, 0);
            dumpTimer(proto, 1146756268035L, screenOffRpmStats2.get(ent2.getKey()), rawRealtimeUs6, 0);
            proto.end(rpmToken);
            it = it;
            multicastWakeLockCountTotal = multicastWakeLockCountTotal;
            screenOffRpmStats = screenOffRpmStats2;
        }
        int i7 = 0;
        while (true) {
            int i8 = i7;
            if (i8 < 5) {
                long sbToken = proto.start(2246267895828L);
                proto.write(1159641169921L, i8);
                dumpTimer(proto, 1146756268034L, getScreenBrightnessTimer(i8), rawRealtimeUs6, 0);
                proto.end(sbToken);
                i7 = i8 + 1;
            } else {
                dumpTimer(proto, 1146756268053L, getPhoneSignalScanningTimer(), rawRealtimeUs6, 0);
                int i9 = 0;
                while (true) {
                    int i10 = i9;
                    if (i10 < 5) {
                        long pssToken = proto.start(2246267895824L);
                        proto.write(1159641169921L, i10);
                        dumpTimer(proto, 1146756268034L, getPhoneSignalStrengthTimer(i10), rawRealtimeUs6, 0);
                        proto.end(pssToken);
                        i9 = i10 + 1;
                    } else {
                        Map<String, ? extends Timer> wakeupReasons = getWakeupReasonStats();
                        Iterator<Map.Entry<String, ? extends Timer>> it2 = wakeupReasons.entrySet().iterator();
                        while (it2.hasNext()) {
                            Map.Entry<String, ? extends Timer> ent3 = it2.next();
                            long wrToken = proto.start(2246267895830L);
                            proto.write(1138166333441L, ent3.getKey());
                            dumpTimer(proto, 1146756268034L, ent3.getValue(), rawRealtimeUs6, 0);
                            proto.end(wrToken);
                            it2 = it2;
                            wakeupReasons = wakeupReasons;
                        }
                        int i11 = 0;
                        while (true) {
                            int i12 = i11;
                            if (i12 < 5) {
                                long wssToken = proto.start(SystemProto.WIFI_SIGNAL_STRENGTH);
                                proto.write(1159641169921L, i12);
                                dumpTimer(proto, 1146756268034L, getWifiSignalStrengthTimer(i12), rawRealtimeUs6, 0);
                                proto.end(wssToken);
                                i11 = i12 + 1;
                            } else {
                                int i13 = 0;
                                while (true) {
                                    int i14 = i13;
                                    if (i14 < 8) {
                                        long wsToken = proto.start(2246267895833L);
                                        proto.write(1159641169921L, i14);
                                        dumpTimer(proto, 1146756268034L, getWifiStateTimer(i14), rawRealtimeUs6, 0);
                                        proto.end(wsToken);
                                        i13 = i14 + 1;
                                    } else {
                                        int i15 = 0;
                                        while (true) {
                                            int i16 = i15;
                                            if (i16 < 13) {
                                                long wssToken2 = proto.start(2246267895834L);
                                                proto.write(1159641169921L, i16);
                                                dumpTimer(proto, 1146756268034L, getWifiSupplStateTimer(i16), rawRealtimeUs6, 0);
                                                proto.end(wssToken2);
                                                i15 = i16 + 1;
                                            } else {
                                                proto.end(sToken);
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
