package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.bluetooth.BluetoothActivityEnergyInfo;
import android.bluetooth.UidTraffic;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.INetworkStatsService;
import android.net.NetworkStats;
import android.net.Uri;
import android.net.wifi.WifiActivityEnergyInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.BatteryStats;
import android.os.Build;
import android.os.Handler;
import android.os.IBatteryPropertiesRegistrar;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.WorkSource;
import android.os.connectivity.CellularBatteryStats;
import android.os.connectivity.GpsBatteryStats;
import android.os.connectivity.WifiBatteryStats;
import android.provider.Settings;
import android.provider.Telephony;
import android.telecom.ParcelableCallAnalytics;
import android.telephony.ModemActivityInfo;
import android.telephony.SignalStrength;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.IntArray;
import android.util.KeyValueListParser;
import android.util.Log;
import android.util.LogWriter;
import android.util.LongSparseArray;
import android.util.LongSparseLongArray;
import android.util.MutableInt;
import android.util.Pools;
import android.util.Printer;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.StatsLog;
import android.util.TimeUtils;
import com.android.ims.ImsConfig;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.EventLogTags;
import com.android.internal.os.KernelCpuUidTimeReader;
import com.android.internal.os.KernelWakelockStats;
import com.android.internal.os.RpmStats;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.FastXmlSerializer;
import com.android.internal.util.XmlUtils;
import com.xiaopeng.util.FeatureOption;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import libcore.util.EmptyArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: classes3.dex */
public class BatteryStatsImpl extends BatteryStats {
    static final int BATTERY_DELTA_LEVEL_FLAG = 1;
    public static final int BATTERY_PLUGGED_NONE = 0;
    @UnsupportedAppUsage
    public static final Parcelable.Creator<BatteryStatsImpl> CREATOR;
    private static final boolean DEBUG = false;
    public static final boolean DEBUG_ENERGY = false;
    private static final boolean DEBUG_ENERGY_CPU = false;
    private static final boolean DEBUG_HISTORY = false;
    private static final boolean DEBUG_MEMORY = false;
    static final long DELAY_UPDATE_WAKELOCKS = 5000;
    static final int DELTA_BATTERY_CHARGE_FLAG = 16777216;
    static final int DELTA_BATTERY_LEVEL_FLAG = 524288;
    static final int DELTA_EVENT_FLAG = 8388608;
    static final int DELTA_STATE2_FLAG = 2097152;
    static final int DELTA_STATE_FLAG = 1048576;
    static final int DELTA_STATE_MASK = -33554432;
    static final int DELTA_TIME_ABS = 524285;
    static final int DELTA_TIME_INT = 524286;
    static final int DELTA_TIME_LONG = 524287;
    static final int DELTA_TIME_MASK = 524287;
    static final int DELTA_WAKELOCK_FLAG = 4194304;
    private static final int MAGIC = -1166707595;
    static final int MAX_DAILY_ITEMS = 10;
    static final int MAX_LEVEL_STEPS = 200;
    private static final int MAX_WAKELOCKS_PER_UID;
    private static final double MILLISECONDS_IN_HOUR = 3600000.0d;
    private static final long MILLISECONDS_IN_YEAR = 31536000000L;
    static final int MSG_REPORT_CHARGING = 3;
    static final int MSG_REPORT_CPU_UPDATE_NEEDED = 1;
    static final int MSG_REPORT_POWER_CHANGE = 2;
    static final int MSG_REPORT_RESET_STATS = 4;
    private static final int NUM_BT_TX_LEVELS = 1;
    private static final int NUM_WIFI_TX_LEVELS = 1;
    private static final long RPM_STATS_UPDATE_FREQ_MS = 1000;
    static final int STATE_BATTERY_HEALTH_MASK = 7;
    static final int STATE_BATTERY_HEALTH_SHIFT = 26;
    static final int STATE_BATTERY_MASK = -16777216;
    static final int STATE_BATTERY_PLUG_MASK = 3;
    static final int STATE_BATTERY_PLUG_SHIFT = 24;
    static final int STATE_BATTERY_STATUS_MASK = 7;
    static final int STATE_BATTERY_STATUS_SHIFT = 29;
    private static final String TAG = "BatteryStatsImpl";
    private static final int USB_DATA_CONNECTED = 2;
    private static final int USB_DATA_DISCONNECTED = 1;
    private static final int USB_DATA_UNKNOWN = 0;
    private static final boolean USE_OLD_HISTORY = false;
    static final int VERSION = 186;
    @VisibleForTesting
    public static final int WAKE_LOCK_WEIGHT = 50;
    final BatteryStats.HistoryEventTracker mActiveEvents;
    int mActiveHistoryStates;
    int mActiveHistoryStates2;
    int mAudioOnNesting;
    StopwatchTimer mAudioOnTimer;
    final ArrayList<StopwatchTimer> mAudioTurnedOnTimers;
    final BatteryStatsHistory mBatteryStatsHistory;
    ControllerActivityCounterImpl mBluetoothActivity;
    int mBluetoothScanNesting;
    final ArrayList<StopwatchTimer> mBluetoothScanOnTimers;
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    protected StopwatchTimer mBluetoothScanTimer;
    private BatteryCallback mCallback;
    int mCameraOnNesting;
    StopwatchTimer mCameraOnTimer;
    final ArrayList<StopwatchTimer> mCameraTurnedOnTimers;
    int mChangedStates;
    int mChangedStates2;
    final BatteryStats.LevelStepTracker mChargeStepTracker;
    boolean mCharging;
    public final AtomicFile mCheckinFile;
    protected Clocks mClocks;
    @GuardedBy({"this"})
    final Constants mConstants;
    private long[] mCpuFreqs;
    @GuardedBy({"this"})
    private long mCpuTimeReadsTrackingStartTime;
    @VisibleForTesting
    protected KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader mCpuUidActiveTimeReader;
    @VisibleForTesting
    protected KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader mCpuUidClusterTimeReader;
    @VisibleForTesting
    protected KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader mCpuUidFreqTimeReader;
    @VisibleForTesting
    protected KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader mCpuUidUserSysTimeReader;
    final BatteryStats.HistoryStepDetails mCurHistoryStepDetails;
    long mCurStepCpuSystemTime;
    long mCurStepCpuUserTime;
    int mCurStepMode;
    long mCurStepStatIOWaitTime;
    long mCurStepStatIdleTime;
    long mCurStepStatIrqTime;
    long mCurStepStatSoftIrqTime;
    long mCurStepStatSystemTime;
    long mCurStepStatUserTime;
    int mCurrentBatteryLevel;
    final BatteryStats.LevelStepTracker mDailyChargeStepTracker;
    final BatteryStats.LevelStepTracker mDailyDischargeStepTracker;
    public final AtomicFile mDailyFile;
    final ArrayList<BatteryStats.DailyItem> mDailyItems;
    ArrayList<BatteryStats.PackageChange> mDailyPackageChanges;
    long mDailyStartTime;
    private final Runnable mDeferSetCharging;
    int mDeviceIdleMode;
    StopwatchTimer mDeviceIdleModeFullTimer;
    StopwatchTimer mDeviceIdleModeLightTimer;
    boolean mDeviceIdling;
    StopwatchTimer mDeviceIdlingTimer;
    boolean mDeviceLightIdling;
    StopwatchTimer mDeviceLightIdlingTimer;
    int mDischargeAmountScreenDoze;
    int mDischargeAmountScreenDozeSinceCharge;
    int mDischargeAmountScreenOff;
    int mDischargeAmountScreenOffSinceCharge;
    int mDischargeAmountScreenOn;
    int mDischargeAmountScreenOnSinceCharge;
    private LongSamplingCounter mDischargeCounter;
    int mDischargeCurrentLevel;
    private LongSamplingCounter mDischargeDeepDozeCounter;
    private LongSamplingCounter mDischargeLightDozeCounter;
    int mDischargePlugLevel;
    private LongSamplingCounter mDischargeScreenDozeCounter;
    int mDischargeScreenDozeUnplugLevel;
    private LongSamplingCounter mDischargeScreenOffCounter;
    int mDischargeScreenOffUnplugLevel;
    int mDischargeScreenOnUnplugLevel;
    int mDischargeStartLevel;
    final BatteryStats.LevelStepTracker mDischargeStepTracker;
    int mDischargeUnplugLevel;
    boolean mDistributeWakelockCpu;
    final ArrayList<StopwatchTimer> mDrawTimers;
    String mEndPlatformVersion;
    private int mEstimatedBatteryCapacity;
    private ExternalStatsSync mExternalSync;
    int mFlashlightOnNesting;
    StopwatchTimer mFlashlightOnTimer;
    final ArrayList<StopwatchTimer> mFlashlightTurnedOnTimers;
    @UnsupportedAppUsage
    final ArrayList<StopwatchTimer> mFullTimers;
    final ArrayList<StopwatchTimer> mFullWifiLockTimers;
    boolean mGlobalWifiRunning;
    StopwatchTimer mGlobalWifiRunningTimer;
    int mGpsNesting;
    int mGpsSignalQualityBin;
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    protected final StopwatchTimer[] mGpsSignalQualityTimer;
    public Handler mHandler;
    boolean mHasBluetoothReporting;
    boolean mHasModemReporting;
    boolean mHasWifiReporting;
    protected boolean mHaveBatteryLevel;
    int mHighDischargeAmountSinceCharge;
    BatteryStats.HistoryItem mHistory;
    final BatteryStats.HistoryItem mHistoryAddTmp;
    long mHistoryBaseTime;
    final Parcel mHistoryBuffer;
    int mHistoryBufferLastPos;
    BatteryStats.HistoryItem mHistoryCache;
    final BatteryStats.HistoryItem mHistoryCur;
    BatteryStats.HistoryItem mHistoryEnd;
    private BatteryStats.HistoryItem mHistoryIterator;
    BatteryStats.HistoryItem mHistoryLastEnd;
    final BatteryStats.HistoryItem mHistoryLastLastWritten;
    final BatteryStats.HistoryItem mHistoryLastWritten;
    final BatteryStats.HistoryItem mHistoryReadTmp;
    final HashMap<BatteryStats.HistoryTag, Integer> mHistoryTagPool;
    int mInitStepMode;
    private String mInitialAcquireWakeName;
    private int mInitialAcquireWakeUid;
    boolean mInteractive;
    StopwatchTimer mInteractiveTimer;
    @GuardedBy({"this"})
    private boolean mIsPerProcessStateCpuDataStale;
    final SparseIntArray mIsolatedUids;
    private boolean mIteratingHistory;
    @VisibleForTesting
    protected KernelCpuSpeedReader[] mKernelCpuSpeedReaders;
    private final KernelMemoryBandwidthStats mKernelMemoryBandwidthStats;
    private final LongSparseArray<SamplingTimer> mKernelMemoryStats;
    @VisibleForTesting
    protected KernelSingleUidTimeReader mKernelSingleUidTimeReader;
    private final KernelWakelockReader mKernelWakelockReader;
    private final HashMap<String, SamplingTimer> mKernelWakelockStats;
    private final BluetoothActivityInfoCache mLastBluetoothActivityInfo;
    int mLastChargeStepLevel;
    int mLastChargingStateLevel;
    int mLastDischargeStepLevel;
    long mLastHistoryElapsedRealtime;
    BatteryStats.HistoryStepDetails mLastHistoryStepDetails;
    byte mLastHistoryStepLevel;
    long mLastIdleTimeStart;
    private ModemActivityInfo mLastModemActivityInfo;
    @GuardedBy({"mModemNetworkLock"})
    private NetworkStats mLastModemNetworkStats;
    @VisibleForTesting
    protected ArrayList<StopwatchTimer> mLastPartialTimers;
    private long mLastRpmStatsUpdateTimeMs;
    long mLastStepCpuSystemTime;
    long mLastStepCpuUserTime;
    long mLastStepStatIOWaitTime;
    long mLastStepStatIdleTime;
    long mLastStepStatIrqTime;
    long mLastStepStatSoftIrqTime;
    long mLastStepStatSystemTime;
    long mLastStepStatUserTime;
    String mLastWakeupReason;
    long mLastWakeupUptimeMs;
    @GuardedBy({"mWifiNetworkLock"})
    private NetworkStats mLastWifiNetworkStats;
    long mLastWriteTime;
    long mLongestFullIdleTime;
    long mLongestLightIdleTime;
    int mLowDischargeAmountSinceCharge;
    int mMaxChargeStepLevel;
    private int mMaxLearnedBatteryCapacity;
    int mMinDischargeStepLevel;
    private int mMinLearnedBatteryCapacity;
    LongSamplingCounter mMobileRadioActiveAdjustedTime;
    StopwatchTimer mMobileRadioActivePerAppTimer;
    long mMobileRadioActiveStartTime;
    StopwatchTimer mMobileRadioActiveTimer;
    LongSamplingCounter mMobileRadioActiveUnknownCount;
    LongSamplingCounter mMobileRadioActiveUnknownTime;
    int mMobileRadioPowerState;
    int mModStepMode;
    ControllerActivityCounterImpl mModemActivity;
    @GuardedBy({"mModemNetworkLock"})
    private String[] mModemIfaces;
    private final Object mModemNetworkLock;
    final LongSamplingCounter[] mNetworkByteActivityCounters;
    final LongSamplingCounter[] mNetworkPacketActivityCounters;
    private final Pools.Pool<NetworkStats> mNetworkStatsPool;
    int mNextHistoryTagIdx;
    long mNextMaxDailyDeadline;
    long mNextMinDailyDeadline;
    boolean mNoAutoReset;
    @GuardedBy({"this"})
    private int mNumAllUidCpuTimeReads;
    @GuardedBy({"this"})
    private long mNumBatchedSingleUidCpuTimeReads;
    private int mNumConnectivityChange;
    int mNumHistoryItems;
    int mNumHistoryTagChars;
    @GuardedBy({"this"})
    private long mNumSingleUidCpuTimeReads;
    @GuardedBy({"this"})
    private int mNumUidsRemoved;
    boolean mOnBattery;
    @VisibleForTesting
    protected boolean mOnBatteryInternal;
    protected final TimeBase mOnBatteryScreenOffTimeBase;
    protected final TimeBase mOnBatteryTimeBase;
    @UnsupportedAppUsage
    @VisibleForTesting
    protected ArrayList<StopwatchTimer> mPartialTimers;
    @GuardedBy({"this"})
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    protected Queue<UidToRemove> mPendingRemovedUids;
    @GuardedBy({"this"})
    @VisibleForTesting
    protected final SparseIntArray mPendingUids;
    @GuardedBy({"this"})
    public boolean mPerProcStateCpuTimesAvailable;
    int mPhoneDataConnectionType;
    final StopwatchTimer[] mPhoneDataConnectionsTimer;
    boolean mPhoneOn;
    StopwatchTimer mPhoneOnTimer;
    private int mPhoneServiceState;
    private int mPhoneServiceStateRaw;
    StopwatchTimer mPhoneSignalScanningTimer;
    int mPhoneSignalStrengthBin;
    int mPhoneSignalStrengthBinRaw;
    final StopwatchTimer[] mPhoneSignalStrengthsTimer;
    private int mPhoneSimStateRaw;
    private final PlatformIdleStateCallback mPlatformIdleStateCallback;
    @VisibleForTesting
    protected PowerProfile mPowerProfile;
    boolean mPowerSaveModeEnabled;
    StopwatchTimer mPowerSaveModeEnabledTimer;
    boolean mPretendScreenOff;
    public final RailEnergyDataCallback mRailEnergyDataCallback;
    int mReadHistoryChars;
    final BatteryStats.HistoryStepDetails mReadHistoryStepDetails;
    String[] mReadHistoryStrings;
    int[] mReadHistoryUids;
    private boolean mReadOverflow;
    long mRealtime;
    long mRealtimeStart;
    public boolean mRecordAllHistory;
    protected boolean mRecordingHistory;
    private final HashMap<String, SamplingTimer> mRpmStats;
    int mScreenBrightnessBin;
    final StopwatchTimer[] mScreenBrightnessTimer;
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    protected StopwatchTimer mScreenDozeTimer;
    private final HashMap<String, SamplingTimer> mScreenOffRpmStats;
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    protected StopwatchTimer mScreenOnTimer;
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    protected int mScreenState;
    int mSensorNesting;
    final SparseArray<ArrayList<StopwatchTimer>> mSensorTimers;
    boolean mShuttingDown;
    long mStartClockTime;
    int mStartCount;
    String mStartPlatformVersion;
    private final AtomicFile mStatsFile;
    long mTempTotalCpuSystemTimeUs;
    long mTempTotalCpuUserTimeUs;
    final BatteryStats.HistoryStepDetails mTmpHistoryStepDetails;
    private final RailStats mTmpRailStats;
    private final RpmStats mTmpRpmStats;
    private final KernelWakelockStats mTmpWakelockStats;
    long mTrackRunningHistoryElapsedRealtime;
    long mTrackRunningHistoryUptime;
    final SparseArray<Uid> mUidStats;
    long mUptime;
    long mUptimeStart;
    int mUsbDataState;
    @VisibleForTesting
    protected UserInfoProvider mUserInfoProvider;
    int mVideoOnNesting;
    StopwatchTimer mVideoOnTimer;
    final ArrayList<StopwatchTimer> mVideoTurnedOnTimers;
    long[][] mWakeLockAllocationsUs;
    boolean mWakeLockImportant;
    int mWakeLockNesting;
    private final HashMap<String, SamplingTimer> mWakeupReasonStats;
    StopwatchTimer mWifiActiveTimer;
    ControllerActivityCounterImpl mWifiActivity;
    final SparseArray<ArrayList<StopwatchTimer>> mWifiBatchedScanTimers;
    int mWifiFullLockNesting;
    @GuardedBy({"mWifiNetworkLock"})
    private String[] mWifiIfaces;
    int mWifiMulticastNesting;
    final ArrayList<StopwatchTimer> mWifiMulticastTimers;
    StopwatchTimer mWifiMulticastWakelockTimer;
    private final Object mWifiNetworkLock;
    boolean mWifiOn;
    StopwatchTimer mWifiOnTimer;
    int mWifiRadioPowerState;
    final ArrayList<StopwatchTimer> mWifiRunningTimers;
    int mWifiScanNesting;
    final ArrayList<StopwatchTimer> mWifiScanTimers;
    int mWifiSignalStrengthBin;
    final StopwatchTimer[] mWifiSignalStrengthsTimer;
    int mWifiState;
    final StopwatchTimer[] mWifiStateTimer;
    int mWifiSupplState;
    final StopwatchTimer[] mWifiSupplStateTimer;
    @UnsupportedAppUsage
    final ArrayList<StopwatchTimer> mWindowTimers;
    final ReentrantLock mWriteLock;

    /* loaded from: classes3.dex */
    public interface BatteryCallback {
        void batteryNeedsCpuUpdate();

        void batteryPowerChanged(boolean z);

        void batterySendBroadcast(Intent intent);

        void batteryStatsReset();
    }

    /* loaded from: classes3.dex */
    public interface Clocks {
        long elapsedRealtime();

        long uptimeMillis();
    }

    /* loaded from: classes3.dex */
    public interface ExternalStatsSync {
        public static final int UPDATE_ALL = 31;
        public static final int UPDATE_BT = 8;
        public static final int UPDATE_CPU = 1;
        public static final int UPDATE_RADIO = 4;
        public static final int UPDATE_RPM = 16;
        public static final int UPDATE_WIFI = 2;

        void cancelCpuSyncDueToWakelockChange();

        Future<?> scheduleCopyFromAllUidsCpuTimes(boolean z, boolean z2);

        Future<?> scheduleCpuSyncDueToRemovedUid(int i);

        Future<?> scheduleCpuSyncDueToScreenStateChange(boolean z, boolean z2);

        Future<?> scheduleCpuSyncDueToSettingChange();

        Future<?> scheduleCpuSyncDueToWakelockChange(long j);

        Future<?> scheduleReadProcStateCpuTimes(boolean z, boolean z2, long j);

        Future<?> scheduleSync(String str, int i);

        Future<?> scheduleSyncDueToBatteryLevelChange(long j);
    }

    /* loaded from: classes3.dex */
    public interface PlatformIdleStateCallback {
        void fillLowPowerStats(RpmStats rpmStats);

        String getPlatformLowPowerStats();

        String getSubsystemLowPowerStats();
    }

    /* loaded from: classes3.dex */
    public interface RailEnergyDataCallback {
        void fillRailDataStats(RailStats railStats);
    }

    /* loaded from: classes3.dex */
    public interface TimeBaseObs {
        void detach();

        void onTimeStarted(long j, long j2, long j3);

        void onTimeStopped(long j, long j2, long j3);

        boolean reset(boolean z);
    }

    static /* synthetic */ int access$008(BatteryStatsImpl x0) {
        int i = x0.mNumUidsRemoved;
        x0.mNumUidsRemoved = i + 1;
        return i;
    }

    static /* synthetic */ long access$1708(BatteryStatsImpl x0) {
        long j = x0.mNumSingleUidCpuTimeReads;
        x0.mNumSingleUidCpuTimeReads = 1 + j;
        return j;
    }

    static /* synthetic */ long access$1808(BatteryStatsImpl x0) {
        long j = x0.mNumBatchedSingleUidCpuTimeReads;
        x0.mNumBatchedSingleUidCpuTimeReads = 1 + j;
        return j;
    }

    static {
        if (ActivityManager.isLowRamDeviceStatic()) {
            MAX_WAKELOCKS_PER_UID = 40;
        } else {
            MAX_WAKELOCKS_PER_UID = 200;
        }
        CREATOR = new Parcelable.Creator<BatteryStatsImpl>() { // from class: com.android.internal.os.BatteryStatsImpl.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public BatteryStatsImpl createFromParcel(Parcel in) {
                return new BatteryStatsImpl(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public BatteryStatsImpl[] newArray(int size) {
                return new BatteryStatsImpl[size];
            }
        };
    }

    @Override // android.os.BatteryStats
    public LongSparseArray<SamplingTimer> getKernelMemoryStats() {
        return this.mKernelMemoryStats;
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public final class UidToRemove {
        int endUid;
        int startUid;
        long timeAddedInQueue;

        public UidToRemove(BatteryStatsImpl this$0, int uid, long timestamp) {
            this(uid, uid, timestamp);
        }

        public UidToRemove(int startUid, int endUid, long timestamp) {
            this.startUid = startUid;
            this.endUid = endUid;
            this.timeAddedInQueue = timestamp;
        }

        void remove() {
            int i = this.startUid;
            int i2 = this.endUid;
            if (i == i2) {
                BatteryStatsImpl.this.mCpuUidUserSysTimeReader.removeUid(this.startUid);
                BatteryStatsImpl.this.mCpuUidFreqTimeReader.removeUid(this.startUid);
                if (BatteryStatsImpl.this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
                    BatteryStatsImpl.this.mCpuUidActiveTimeReader.removeUid(this.startUid);
                    BatteryStatsImpl.this.mCpuUidClusterTimeReader.removeUid(this.startUid);
                }
                if (BatteryStatsImpl.this.mKernelSingleUidTimeReader != null) {
                    BatteryStatsImpl.this.mKernelSingleUidTimeReader.removeUid(this.startUid);
                }
                BatteryStatsImpl.access$008(BatteryStatsImpl.this);
            } else if (i < i2) {
                BatteryStatsImpl.this.mCpuUidFreqTimeReader.removeUidsInRange(this.startUid, this.endUid);
                BatteryStatsImpl.this.mCpuUidUserSysTimeReader.removeUidsInRange(this.startUid, this.endUid);
                if (BatteryStatsImpl.this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
                    BatteryStatsImpl.this.mCpuUidActiveTimeReader.removeUidsInRange(this.startUid, this.endUid);
                    BatteryStatsImpl.this.mCpuUidClusterTimeReader.removeUidsInRange(this.startUid, this.endUid);
                }
                if (BatteryStatsImpl.this.mKernelSingleUidTimeReader != null) {
                    BatteryStatsImpl.this.mKernelSingleUidTimeReader.removeUidsInRange(this.startUid, this.endUid);
                }
                BatteryStatsImpl.access$008(BatteryStatsImpl.this);
            } else {
                Slog.w(BatteryStatsImpl.TAG, "End UID " + this.endUid + " is smaller than start UID " + this.startUid);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class UserInfoProvider {
        private int[] userIds;

        protected abstract int[] getUserIds();

        @VisibleForTesting
        public final void refreshUserIds() {
            this.userIds = getUserIds();
        }

        @VisibleForTesting
        public boolean exists(int userId) {
            int[] iArr = this.userIds;
            if (iArr != null) {
                return ArrayUtils.contains(iArr, userId);
            }
            return true;
        }
    }

    /* loaded from: classes3.dex */
    final class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            String action;
            BatteryCallback cb = BatteryStatsImpl.this.mCallback;
            int i = msg.what;
            if (i == 1) {
                if (cb != null) {
                    cb.batteryNeedsCpuUpdate();
                }
            } else if (i == 2) {
                if (cb != null) {
                    cb.batteryPowerChanged(msg.arg1 != 0);
                }
            } else if (i == 3) {
                if (cb != null) {
                    synchronized (BatteryStatsImpl.this) {
                        action = BatteryStatsImpl.this.mCharging ? BatteryManager.ACTION_CHARGING : BatteryManager.ACTION_DISCHARGING;
                    }
                    Intent intent = new Intent(action);
                    intent.addFlags(67108864);
                    cb.batterySendBroadcast(intent);
                }
            } else if (i == 4 && cb != null) {
                cb.batteryStatsReset();
            }
        }
    }

    public void postBatteryNeedsCpuUpdateMsg() {
        this.mHandler.sendEmptyMessage(1);
    }

    public void updateProcStateCpuTimes(boolean onBattery, boolean onBatteryScreenOff) {
        int[] isolatedUids;
        synchronized (this) {
            if (this.mConstants.TRACK_CPU_TIMES_BY_PROC_STATE) {
                if (initKernelSingleUidTimeReaderLocked()) {
                    if (this.mIsPerProcessStateCpuDataStale) {
                        this.mPendingUids.clear();
                    } else if (this.mPendingUids.size() != 0) {
                        SparseIntArray uidStates = this.mPendingUids.m40clone();
                        this.mPendingUids.clear();
                        for (int i = uidStates.size() - 1; i >= 0; i--) {
                            int uid = uidStates.keyAt(i);
                            int procState = uidStates.valueAt(i);
                            synchronized (this) {
                                Uid u = getAvailableUidStatsLocked(uid);
                                if (u != null) {
                                    if (u.mChildUids == null) {
                                        isolatedUids = null;
                                    } else {
                                        isolatedUids = u.mChildUids.toArray();
                                        for (int j = isolatedUids.length - 1; j >= 0; j--) {
                                            isolatedUids[j] = u.mChildUids.get(j);
                                        }
                                    }
                                    long[] cpuTimesMs = this.mKernelSingleUidTimeReader.readDeltaMs(uid);
                                    if (isolatedUids != null) {
                                        for (int j2 = isolatedUids.length - 1; j2 >= 0; j2--) {
                                            cpuTimesMs = addCpuTimes(cpuTimesMs, this.mKernelSingleUidTimeReader.readDeltaMs(isolatedUids[j2]));
                                        }
                                    }
                                    if (onBattery && cpuTimesMs != null) {
                                        synchronized (this) {
                                            u.addProcStateTimesMs(procState, cpuTimesMs, onBattery);
                                            u.addProcStateScreenOffTimesMs(procState, cpuTimesMs, onBatteryScreenOff);
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

    public void clearPendingRemovedUids() {
        long cutOffTime = this.mClocks.elapsedRealtime() - this.mConstants.UID_REMOVE_DELAY_MS;
        while (!this.mPendingRemovedUids.isEmpty() && this.mPendingRemovedUids.peek().timeAddedInQueue < cutOffTime) {
            this.mPendingRemovedUids.poll().remove();
        }
    }

    public void copyFromAllUidsCpuTimes() {
        synchronized (this) {
            copyFromAllUidsCpuTimes(this.mOnBatteryTimeBase.isRunning(), this.mOnBatteryScreenOffTimeBase.isRunning());
        }
    }

    public void copyFromAllUidsCpuTimes(boolean onBattery, boolean onBatteryScreenOff) {
        long[] cpuTimesMs;
        int procState;
        synchronized (this) {
            if (this.mConstants.TRACK_CPU_TIMES_BY_PROC_STATE) {
                if (initKernelSingleUidTimeReaderLocked()) {
                    SparseArray<long[]> allUidCpuFreqTimesMs = this.mCpuUidFreqTimeReader.getAllUidCpuFreqTimeMs();
                    if (this.mIsPerProcessStateCpuDataStale) {
                        this.mKernelSingleUidTimeReader.setAllUidsCpuTimesMs(allUidCpuFreqTimesMs);
                        this.mIsPerProcessStateCpuDataStale = false;
                        this.mPendingUids.clear();
                        return;
                    }
                    for (int i = allUidCpuFreqTimesMs.size() - 1; i >= 0; i--) {
                        int uid = allUidCpuFreqTimesMs.keyAt(i);
                        Uid u = getAvailableUidStatsLocked(mapUid(uid));
                        if (u != null && (cpuTimesMs = allUidCpuFreqTimesMs.valueAt(i)) != null) {
                            long[] deltaTimesMs = this.mKernelSingleUidTimeReader.computeDelta(uid, (long[]) cpuTimesMs.clone());
                            if (onBattery && deltaTimesMs != null) {
                                int idx = this.mPendingUids.indexOfKey(uid);
                                if (idx >= 0) {
                                    procState = this.mPendingUids.valueAt(idx);
                                    this.mPendingUids.removeAt(idx);
                                } else {
                                    procState = u.mProcessState;
                                }
                                if (procState >= 0 && procState < 7) {
                                    u.addProcStateTimesMs(procState, deltaTimesMs, onBattery);
                                    u.addProcStateScreenOffTimesMs(procState, deltaTimesMs, onBatteryScreenOff);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @VisibleForTesting
    public long[] addCpuTimes(long[] timesA, long[] timesB) {
        if (timesA != null && timesB != null) {
            for (int i = timesA.length - 1; i >= 0; i--) {
                timesA[i] = timesA[i] + timesB[i];
            }
            return timesA;
        } else if (timesA == null) {
            if (timesB == null) {
                return null;
            }
            return timesB;
        } else {
            return timesA;
        }
    }

    @GuardedBy({"this"})
    private boolean initKernelSingleUidTimeReaderLocked() {
        boolean z = false;
        if (this.mKernelSingleUidTimeReader == null) {
            PowerProfile powerProfile = this.mPowerProfile;
            if (powerProfile == null) {
                return false;
            }
            if (this.mCpuFreqs == null) {
                this.mCpuFreqs = this.mCpuUidFreqTimeReader.readFreqs(powerProfile);
            }
            long[] jArr = this.mCpuFreqs;
            if (jArr != null) {
                this.mKernelSingleUidTimeReader = new KernelSingleUidTimeReader(jArr.length);
            } else {
                this.mPerProcStateCpuTimesAvailable = this.mCpuUidFreqTimeReader.allUidTimesAvailable();
                return false;
            }
        }
        if (this.mCpuUidFreqTimeReader.allUidTimesAvailable() && this.mKernelSingleUidTimeReader.singleUidCpuTimesAvailable()) {
            z = true;
        }
        this.mPerProcStateCpuTimesAvailable = z;
        return true;
    }

    /* loaded from: classes3.dex */
    public static class SystemClocks implements Clocks {
        @Override // com.android.internal.os.BatteryStatsImpl.Clocks
        public long elapsedRealtime() {
            return SystemClock.elapsedRealtime();
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Clocks
        public long uptimeMillis() {
            return SystemClock.uptimeMillis();
        }
    }

    @Override // android.os.BatteryStats
    public Map<String, ? extends Timer> getRpmStats() {
        return this.mRpmStats;
    }

    @Override // android.os.BatteryStats
    public Map<String, ? extends Timer> getScreenOffRpmStats() {
        return this.mScreenOffRpmStats;
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public Map<String, ? extends Timer> getKernelWakelockStats() {
        return this.mKernelWakelockStats;
    }

    @Override // android.os.BatteryStats
    public Map<String, ? extends Timer> getWakeupReasonStats() {
        return this.mWakeupReasonStats;
    }

    @Override // android.os.BatteryStats
    public long getUahDischarge(int which) {
        return this.mDischargeCounter.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getUahDischargeScreenOff(int which) {
        return this.mDischargeScreenOffCounter.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getUahDischargeScreenDoze(int which) {
        return this.mDischargeScreenDozeCounter.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getUahDischargeLightDoze(int which) {
        return this.mDischargeLightDozeCounter.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getUahDischargeDeepDoze(int which) {
        return this.mDischargeDeepDozeCounter.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public int getEstimatedBatteryCapacity() {
        return this.mEstimatedBatteryCapacity;
    }

    @Override // android.os.BatteryStats
    public int getMinLearnedBatteryCapacity() {
        return this.mMinLearnedBatteryCapacity;
    }

    @Override // android.os.BatteryStats
    public int getMaxLearnedBatteryCapacity() {
        return this.mMaxLearnedBatteryCapacity;
    }

    public BatteryStatsImpl() {
        this(new SystemClocks());
    }

    public BatteryStatsImpl(Clocks clocks) {
        this.mKernelWakelockReader = new KernelWakelockReader();
        this.mTmpWakelockStats = new KernelWakelockStats();
        this.mCpuUidUserSysTimeReader = new KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader(true);
        this.mCpuUidFreqTimeReader = new KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader(true);
        this.mCpuUidActiveTimeReader = new KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader(true);
        this.mCpuUidClusterTimeReader = new KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader(true);
        this.mKernelMemoryBandwidthStats = new KernelMemoryBandwidthStats();
        this.mKernelMemoryStats = new LongSparseArray<>();
        this.mPerProcStateCpuTimesAvailable = true;
        this.mPendingUids = new SparseIntArray();
        this.mCpuTimeReadsTrackingStartTime = SystemClock.uptimeMillis();
        this.mTmpRpmStats = new RpmStats();
        this.mLastRpmStatsUpdateTimeMs = -1000L;
        this.mTmpRailStats = new RailStats();
        this.mPendingRemovedUids = new LinkedList();
        this.mDeferSetCharging = new Runnable() { // from class: com.android.internal.os.BatteryStatsImpl.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (BatteryStatsImpl.this) {
                    if (BatteryStatsImpl.this.mOnBattery) {
                        return;
                    }
                    boolean changed = BatteryStatsImpl.this.setChargingLocked(true);
                    if (changed) {
                        long uptime = BatteryStatsImpl.this.mClocks.uptimeMillis();
                        long elapsedRealtime = BatteryStatsImpl.this.mClocks.elapsedRealtime();
                        BatteryStatsImpl.this.addHistoryRecordLocked(elapsedRealtime, uptime);
                    }
                }
            }
        };
        this.mExternalSync = null;
        this.mUserInfoProvider = null;
        this.mIsolatedUids = new SparseIntArray();
        this.mUidStats = new SparseArray<>();
        this.mPartialTimers = new ArrayList<>();
        this.mFullTimers = new ArrayList<>();
        this.mWindowTimers = new ArrayList<>();
        this.mDrawTimers = new ArrayList<>();
        this.mSensorTimers = new SparseArray<>();
        this.mWifiRunningTimers = new ArrayList<>();
        this.mFullWifiLockTimers = new ArrayList<>();
        this.mWifiMulticastTimers = new ArrayList<>();
        this.mWifiScanTimers = new ArrayList<>();
        this.mWifiBatchedScanTimers = new SparseArray<>();
        this.mAudioTurnedOnTimers = new ArrayList<>();
        this.mVideoTurnedOnTimers = new ArrayList<>();
        this.mFlashlightTurnedOnTimers = new ArrayList<>();
        this.mCameraTurnedOnTimers = new ArrayList<>();
        this.mBluetoothScanOnTimers = new ArrayList<>();
        this.mLastPartialTimers = new ArrayList<>();
        this.mOnBatteryTimeBase = new TimeBase(true);
        this.mOnBatteryScreenOffTimeBase = new TimeBase(true);
        this.mActiveEvents = new BatteryStats.HistoryEventTracker();
        this.mHaveBatteryLevel = false;
        this.mRecordingHistory = false;
        this.mHistoryBuffer = Parcel.obtain();
        this.mHistoryLastWritten = new BatteryStats.HistoryItem();
        this.mHistoryLastLastWritten = new BatteryStats.HistoryItem();
        this.mHistoryReadTmp = new BatteryStats.HistoryItem();
        this.mHistoryAddTmp = new BatteryStats.HistoryItem();
        this.mHistoryTagPool = new HashMap<>();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
        this.mLastHistoryElapsedRealtime = 0L;
        this.mTrackRunningHistoryElapsedRealtime = 0L;
        this.mTrackRunningHistoryUptime = 0L;
        this.mHistoryCur = new BatteryStats.HistoryItem();
        this.mLastHistoryStepDetails = null;
        this.mLastHistoryStepLevel = (byte) 0;
        this.mCurHistoryStepDetails = new BatteryStats.HistoryStepDetails();
        this.mReadHistoryStepDetails = new BatteryStats.HistoryStepDetails();
        this.mTmpHistoryStepDetails = new BatteryStats.HistoryStepDetails();
        this.mScreenState = 0;
        this.mScreenBrightnessBin = -1;
        this.mScreenBrightnessTimer = new StopwatchTimer[5];
        this.mUsbDataState = 0;
        this.mGpsSignalQualityBin = -1;
        this.mGpsSignalQualityTimer = new StopwatchTimer[2];
        this.mPhoneSignalStrengthBin = -1;
        this.mPhoneSignalStrengthBinRaw = -1;
        this.mPhoneSignalStrengthsTimer = new StopwatchTimer[5];
        this.mPhoneDataConnectionType = -1;
        this.mPhoneDataConnectionsTimer = new StopwatchTimer[23];
        this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
        this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
        this.mHasWifiReporting = false;
        this.mHasBluetoothReporting = false;
        this.mHasModemReporting = false;
        this.mWifiState = -1;
        this.mWifiStateTimer = new StopwatchTimer[8];
        this.mWifiSupplState = -1;
        this.mWifiSupplStateTimer = new StopwatchTimer[13];
        this.mWifiSignalStrengthBin = -1;
        this.mWifiSignalStrengthsTimer = new StopwatchTimer[5];
        this.mMobileRadioPowerState = 1;
        this.mWifiRadioPowerState = 1;
        this.mCharging = true;
        this.mInitStepMode = 0;
        this.mCurStepMode = 0;
        this.mModStepMode = 0;
        this.mDischargeStepTracker = new BatteryStats.LevelStepTracker(200);
        this.mDailyDischargeStepTracker = new BatteryStats.LevelStepTracker(400);
        this.mChargeStepTracker = new BatteryStats.LevelStepTracker(200);
        this.mDailyChargeStepTracker = new BatteryStats.LevelStepTracker(400);
        this.mDailyStartTime = 0L;
        this.mNextMinDailyDeadline = 0L;
        this.mNextMaxDailyDeadline = 0L;
        this.mDailyItems = new ArrayList<>();
        this.mLastWriteTime = 0L;
        this.mPhoneServiceState = -1;
        this.mPhoneServiceStateRaw = -1;
        this.mPhoneSimStateRaw = -1;
        this.mEstimatedBatteryCapacity = -1;
        this.mMinLearnedBatteryCapacity = -1;
        this.mMaxLearnedBatteryCapacity = -1;
        this.mRpmStats = new HashMap<>();
        this.mScreenOffRpmStats = new HashMap<>();
        this.mKernelWakelockStats = new HashMap<>();
        this.mLastWakeupReason = null;
        this.mLastWakeupUptimeMs = 0L;
        this.mWakeupReasonStats = new HashMap<>();
        this.mChangedStates = 0;
        this.mChangedStates2 = 0;
        this.mInitialAcquireWakeUid = -1;
        this.mWifiFullLockNesting = 0;
        this.mWifiScanNesting = 0;
        this.mWifiMulticastNesting = 0;
        this.mNetworkStatsPool = new Pools.SynchronizedPool(6);
        this.mWifiNetworkLock = new Object();
        this.mWifiIfaces = EmptyArray.STRING;
        this.mLastWifiNetworkStats = new NetworkStats(0L, -1);
        this.mModemNetworkLock = new Object();
        this.mModemIfaces = EmptyArray.STRING;
        this.mLastModemNetworkStats = new NetworkStats(0L, -1);
        this.mLastModemActivityInfo = new ModemActivityInfo(0L, 0, 0, new int[0], 0, 0);
        this.mLastBluetoothActivityInfo = new BluetoothActivityInfoCache();
        this.mWriteLock = new ReentrantLock();
        init(clocks);
        this.mStatsFile = null;
        this.mCheckinFile = null;
        this.mDailyFile = null;
        this.mBatteryStatsHistory = null;
        this.mHandler = null;
        this.mPlatformIdleStateCallback = null;
        this.mRailEnergyDataCallback = null;
        this.mUserInfoProvider = null;
        this.mConstants = new Constants(this.mHandler);
        clearHistoryLocked();
    }

    private void init(Clocks clocks) {
        this.mClocks = clocks;
    }

    /* loaded from: classes3.dex */
    public static class TimeBase {
        protected final Collection<TimeBaseObs> mObservers;
        protected long mPastRealtime;
        protected long mPastUptime;
        protected long mRealtime;
        protected long mRealtimeStart;
        protected boolean mRunning;
        protected long mUnpluggedRealtime;
        protected long mUnpluggedUptime;
        protected long mUptime;
        protected long mUptimeStart;

        public void dump(PrintWriter pw, String prefix) {
            StringBuilder sb = new StringBuilder(128);
            pw.print(prefix);
            pw.print("mRunning=");
            pw.println(this.mRunning);
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mUptime=");
            BatteryStats.formatTimeMs(sb, this.mUptime / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mRealtime=");
            BatteryStats.formatTimeMs(sb, this.mRealtime / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mPastUptime=");
            BatteryStats.formatTimeMs(sb, this.mPastUptime / 1000);
            sb.append("mUptimeStart=");
            BatteryStats.formatTimeMs(sb, this.mUptimeStart / 1000);
            sb.append("mUnpluggedUptime=");
            BatteryStats.formatTimeMs(sb, this.mUnpluggedUptime / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mPastRealtime=");
            BatteryStats.formatTimeMs(sb, this.mPastRealtime / 1000);
            sb.append("mRealtimeStart=");
            BatteryStats.formatTimeMs(sb, this.mRealtimeStart / 1000);
            sb.append("mUnpluggedRealtime=");
            BatteryStats.formatTimeMs(sb, this.mUnpluggedRealtime / 1000);
            pw.println(sb.toString());
        }

        public TimeBase(boolean isLongList) {
            this.mObservers = isLongList ? new HashSet<>() : new ArrayList<>();
        }

        public TimeBase() {
            this(false);
        }

        public void add(TimeBaseObs observer) {
            this.mObservers.add(observer);
        }

        public void remove(TimeBaseObs observer) {
            this.mObservers.remove(observer);
        }

        public boolean hasObserver(TimeBaseObs observer) {
            return this.mObservers.contains(observer);
        }

        public void init(long uptime, long realtime) {
            this.mRealtime = 0L;
            this.mUptime = 0L;
            this.mPastUptime = 0L;
            this.mPastRealtime = 0L;
            this.mUptimeStart = uptime;
            this.mRealtimeStart = realtime;
            this.mUnpluggedUptime = getUptime(this.mUptimeStart);
            this.mUnpluggedRealtime = getRealtime(this.mRealtimeStart);
        }

        public void reset(long uptime, long realtime) {
            if (!this.mRunning) {
                this.mPastUptime = 0L;
                this.mPastRealtime = 0L;
                return;
            }
            this.mUptimeStart = uptime;
            this.mRealtimeStart = realtime;
            this.mUnpluggedUptime = getUptime(uptime);
            this.mUnpluggedRealtime = getRealtime(realtime);
        }

        public long computeUptime(long curTime, int which) {
            return this.mUptime + getUptime(curTime);
        }

        public long computeRealtime(long curTime, int which) {
            return this.mRealtime + getRealtime(curTime);
        }

        public long getUptime(long curTime) {
            long time = this.mPastUptime;
            if (this.mRunning) {
                return time + (curTime - this.mUptimeStart);
            }
            return time;
        }

        public long getRealtime(long curTime) {
            long time = this.mPastRealtime;
            if (this.mRunning) {
                return time + (curTime - this.mRealtimeStart);
            }
            return time;
        }

        public long getUptimeStart() {
            return this.mUptimeStart;
        }

        public long getRealtimeStart() {
            return this.mRealtimeStart;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        public boolean setRunning(boolean running, long uptime, long realtime) {
            if (this.mRunning != running) {
                this.mRunning = running;
                if (!running) {
                    this.mPastUptime += uptime - this.mUptimeStart;
                    this.mPastRealtime += realtime - this.mRealtimeStart;
                    long batteryUptime = getUptime(uptime);
                    long batteryRealtime = getRealtime(realtime);
                    for (TimeBaseObs timeBaseObs : this.mObservers) {
                        timeBaseObs.onTimeStopped(realtime, batteryUptime, batteryRealtime);
                    }
                    return true;
                }
                this.mUptimeStart = uptime;
                this.mRealtimeStart = realtime;
                long batteryUptime2 = getUptime(uptime);
                this.mUnpluggedUptime = batteryUptime2;
                long batteryRealtime2 = getRealtime(realtime);
                this.mUnpluggedRealtime = batteryRealtime2;
                for (TimeBaseObs timeBaseObs2 : this.mObservers) {
                    timeBaseObs2.onTimeStarted(realtime, batteryUptime2, batteryRealtime2);
                }
                return true;
            }
            return false;
        }

        public void readSummaryFromParcel(Parcel in) {
            this.mUptime = in.readLong();
            this.mRealtime = in.readLong();
        }

        public void writeSummaryToParcel(Parcel out, long uptime, long realtime) {
            out.writeLong(computeUptime(uptime, 0));
            out.writeLong(computeRealtime(realtime, 0));
        }

        public void readFromParcel(Parcel in) {
            this.mRunning = false;
            this.mUptime = in.readLong();
            this.mPastUptime = in.readLong();
            this.mUptimeStart = in.readLong();
            this.mRealtime = in.readLong();
            this.mPastRealtime = in.readLong();
            this.mRealtimeStart = in.readLong();
            this.mUnpluggedUptime = in.readLong();
            this.mUnpluggedRealtime = in.readLong();
        }

        public void writeToParcel(Parcel out, long uptime, long realtime) {
            long runningUptime = getUptime(uptime);
            long runningRealtime = getRealtime(realtime);
            out.writeLong(this.mUptime);
            out.writeLong(runningUptime);
            out.writeLong(this.mUptimeStart);
            out.writeLong(this.mRealtime);
            out.writeLong(runningRealtime);
            out.writeLong(this.mRealtimeStart);
            out.writeLong(this.mUnpluggedUptime);
            out.writeLong(this.mUnpluggedRealtime);
        }
    }

    /* loaded from: classes3.dex */
    public static class Counter extends BatteryStats.Counter implements TimeBaseObs {
        @UnsupportedAppUsage
        final AtomicInteger mCount = new AtomicInteger();
        final TimeBase mTimeBase;

        public Counter(TimeBase timeBase, Parcel in) {
            this.mTimeBase = timeBase;
            this.mCount.set(in.readInt());
            timeBase.add(this);
        }

        public Counter(TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(Parcel out) {
            out.writeInt(this.mCount.get());
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
        }

        public static void writeCounterToParcel(Parcel out, Counter counter) {
            if (counter == null) {
                out.writeInt(0);
                return;
            }
            out.writeInt(1);
            counter.writeToParcel(out);
        }

        public static Counter readCounterFromParcel(TimeBase timeBase, Parcel in) {
            if (in.readInt() == 0) {
                return null;
            }
            return new Counter(timeBase, in);
        }

        @Override // android.os.BatteryStats.Counter
        public int getCountLocked(int which) {
            return this.mCount.get();
        }

        @Override // android.os.BatteryStats.Counter
        public void logState(Printer pw, String prefix) {
            pw.println(prefix + "mCount=" + this.mCount.get());
        }

        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public void stepAtomic() {
            if (this.mTimeBase.isRunning()) {
                this.mCount.incrementAndGet();
            }
        }

        void addAtomic(int delta) {
            if (this.mTimeBase.isRunning()) {
                this.mCount.addAndGet(delta);
            }
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset) {
            this.mCount.set(0);
            if (detachIfReset) {
                detach();
                return true;
            }
            return true;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            this.mTimeBase.remove(this);
        }

        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public void writeSummaryFromParcelLocked(Parcel out) {
            out.writeInt(this.mCount.get());
        }

        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public void readSummaryFromParcelLocked(Parcel in) {
            this.mCount.set(in.readInt());
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class LongSamplingCounterArray extends BatteryStats.LongCounterArray implements TimeBaseObs {
        public long[] mCounts;
        final TimeBase mTimeBase;

        private LongSamplingCounterArray(TimeBase timeBase, Parcel in) {
            this.mTimeBase = timeBase;
            this.mCounts = in.createLongArray();
            timeBase.add(this);
        }

        public LongSamplingCounterArray(TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeToParcel(Parcel out) {
            out.writeLongArray(this.mCounts);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealTime, long baseUptime, long baseRealtime) {
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
        }

        @Override // android.os.BatteryStats.LongCounterArray
        public long[] getCountsLocked(int which) {
            long[] jArr = this.mCounts;
            if (jArr == null) {
                return null;
            }
            return Arrays.copyOf(jArr, jArr.length);
        }

        @Override // android.os.BatteryStats.LongCounterArray
        public void logState(Printer pw, String prefix) {
            pw.println(prefix + "mCounts=" + Arrays.toString(this.mCounts));
        }

        public void addCountLocked(long[] counts) {
            addCountLocked(counts, this.mTimeBase.isRunning());
        }

        public void addCountLocked(long[] counts, boolean isRunning) {
            if (counts != null && isRunning) {
                if (this.mCounts == null) {
                    this.mCounts = new long[counts.length];
                }
                for (int i = 0; i < counts.length; i++) {
                    long[] jArr = this.mCounts;
                    jArr[i] = jArr[i] + counts[i];
                }
            }
        }

        public int getSize() {
            long[] jArr = this.mCounts;
            if (jArr == null) {
                return 0;
            }
            return jArr.length;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset) {
            long[] jArr = this.mCounts;
            if (jArr != null) {
                Arrays.fill(jArr, 0L);
            }
            if (detachIfReset) {
                detach();
                return true;
            }
            return true;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            this.mTimeBase.remove(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeSummaryToParcelLocked(Parcel out) {
            out.writeLongArray(this.mCounts);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void readSummaryFromParcelLocked(Parcel in) {
            this.mCounts = in.createLongArray();
        }

        public static void writeToParcel(Parcel out, LongSamplingCounterArray counterArray) {
            if (counterArray != null) {
                out.writeInt(1);
                counterArray.writeToParcel(out);
                return;
            }
            out.writeInt(0);
        }

        public static LongSamplingCounterArray readFromParcel(Parcel in, TimeBase timeBase) {
            if (in.readInt() != 0) {
                return new LongSamplingCounterArray(timeBase, in);
            }
            return null;
        }

        public static void writeSummaryToParcelLocked(Parcel out, LongSamplingCounterArray counterArray) {
            if (counterArray != null) {
                out.writeInt(1);
                counterArray.writeSummaryToParcelLocked(out);
                return;
            }
            out.writeInt(0);
        }

        public static LongSamplingCounterArray readSummaryFromParcelLocked(Parcel in, TimeBase timeBase) {
            if (in.readInt() != 0) {
                LongSamplingCounterArray counterArray = new LongSamplingCounterArray(timeBase);
                counterArray.readSummaryFromParcelLocked(in);
                return counterArray;
            }
            return null;
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class LongSamplingCounter extends BatteryStats.LongCounter implements TimeBaseObs {
        private long mCount;
        final TimeBase mTimeBase;

        public LongSamplingCounter(TimeBase timeBase, Parcel in) {
            this.mTimeBase = timeBase;
            this.mCount = in.readLong();
            timeBase.add(this);
        }

        public LongSamplingCounter(TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(Parcel out) {
            out.writeLong(this.mCount);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
        }

        @Override // android.os.BatteryStats.LongCounter
        public long getCountLocked(int which) {
            return this.mCount;
        }

        @Override // android.os.BatteryStats.LongCounter
        public void logState(Printer pw, String prefix) {
            pw.println(prefix + "mCount=" + this.mCount);
        }

        public void addCountLocked(long count) {
            addCountLocked(count, this.mTimeBase.isRunning());
        }

        public void addCountLocked(long count, boolean isRunning) {
            if (isRunning) {
                this.mCount += count;
            }
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset) {
            this.mCount = 0L;
            if (detachIfReset) {
                detach();
                return true;
            }
            return true;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            this.mTimeBase.remove(this);
        }

        public void writeSummaryFromParcelLocked(Parcel out) {
            out.writeLong(this.mCount);
        }

        public void readSummaryFromParcelLocked(Parcel in) {
            this.mCount = in.readLong();
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Timer extends BatteryStats.Timer implements TimeBaseObs {
        protected final Clocks mClocks;
        protected int mCount;
        protected final TimeBase mTimeBase;
        protected long mTimeBeforeMark;
        protected long mTotalTime;
        protected final int mType;

        protected abstract int computeCurrentCountLocked();

        protected abstract long computeRunTimeLocked(long j);

        public Timer(Clocks clocks, int type, TimeBase timeBase, Parcel in) {
            this.mClocks = clocks;
            this.mType = type;
            this.mTimeBase = timeBase;
            this.mCount = in.readInt();
            this.mTotalTime = in.readLong();
            this.mTimeBeforeMark = in.readLong();
            timeBase.add(this);
        }

        public Timer(Clocks clocks, int type, TimeBase timeBase) {
            this.mClocks = clocks;
            this.mType = type;
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            out.writeInt(computeCurrentCountLocked());
            out.writeLong(computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs)));
            out.writeLong(this.mTimeBeforeMark);
        }

        public boolean reset(boolean detachIfReset) {
            this.mTimeBeforeMark = 0L;
            this.mTotalTime = 0L;
            this.mCount = 0;
            if (detachIfReset) {
                detach();
                return true;
            }
            return true;
        }

        public void detach() {
            this.mTimeBase.remove(this);
        }

        public void onTimeStarted(long elapsedRealtime, long timeBaseUptime, long baseRealtime) {
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            this.mTotalTime = computeRunTimeLocked(baseRealtime);
            this.mCount = computeCurrentCountLocked();
        }

        @UnsupportedAppUsage
        public static void writeTimerToParcel(Parcel out, Timer timer, long elapsedRealtimeUs) {
            if (timer == null) {
                out.writeInt(0);
                return;
            }
            out.writeInt(1);
            timer.writeToParcel(out, elapsedRealtimeUs);
        }

        @Override // android.os.BatteryStats.Timer
        @UnsupportedAppUsage
        public long getTotalTimeLocked(long elapsedRealtimeUs, int which) {
            return computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs));
        }

        @Override // android.os.BatteryStats.Timer
        @UnsupportedAppUsage
        public int getCountLocked(int which) {
            return computeCurrentCountLocked();
        }

        @Override // android.os.BatteryStats.Timer
        public long getTimeSinceMarkLocked(long elapsedRealtimeUs) {
            long val = computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs));
            return val - this.mTimeBeforeMark;
        }

        @Override // android.os.BatteryStats.Timer
        public void logState(Printer pw, String prefix) {
            pw.println(prefix + "mCount=" + this.mCount);
            pw.println(prefix + "mTotalTime=" + this.mTotalTime);
        }

        public void writeSummaryFromParcelLocked(Parcel out, long elapsedRealtimeUs) {
            long runTime = computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs));
            out.writeLong(runTime);
            out.writeInt(computeCurrentCountLocked());
        }

        public void readSummaryFromParcelLocked(Parcel in) {
            this.mTotalTime = in.readLong();
            this.mCount = in.readInt();
            this.mTimeBeforeMark = this.mTotalTime;
        }
    }

    /* loaded from: classes3.dex */
    public static class SamplingTimer extends Timer {
        int mCurrentReportedCount;
        long mCurrentReportedTotalTime;
        boolean mTimeBaseRunning;
        boolean mTrackingReportedValues;
        int mUnpluggedReportedCount;
        long mUnpluggedReportedTotalTime;
        int mUpdateVersion;

        @VisibleForTesting
        public SamplingTimer(Clocks clocks, TimeBase timeBase, Parcel in) {
            super(clocks, 0, timeBase, in);
            this.mCurrentReportedCount = in.readInt();
            this.mUnpluggedReportedCount = in.readInt();
            this.mCurrentReportedTotalTime = in.readLong();
            this.mUnpluggedReportedTotalTime = in.readLong();
            this.mTrackingReportedValues = in.readInt() == 1;
            this.mTimeBaseRunning = timeBase.isRunning();
        }

        @VisibleForTesting
        public SamplingTimer(Clocks clocks, TimeBase timeBase) {
            super(clocks, 0, timeBase);
            this.mTrackingReportedValues = false;
            this.mTimeBaseRunning = timeBase.isRunning();
        }

        public void endSample() {
            this.mTotalTime = computeRunTimeLocked(0L);
            this.mCount = computeCurrentCountLocked();
            this.mCurrentReportedTotalTime = 0L;
            this.mUnpluggedReportedTotalTime = 0L;
            this.mCurrentReportedCount = 0;
            this.mUnpluggedReportedCount = 0;
            this.mTrackingReportedValues = false;
        }

        public void setUpdateVersion(int version) {
            this.mUpdateVersion = version;
        }

        public int getUpdateVersion() {
            return this.mUpdateVersion;
        }

        public void update(long totalTime, int count) {
            if (this.mTimeBaseRunning && !this.mTrackingReportedValues) {
                this.mUnpluggedReportedTotalTime = totalTime;
                this.mUnpluggedReportedCount = count;
            }
            this.mTrackingReportedValues = true;
            if (totalTime < this.mCurrentReportedTotalTime || count < this.mCurrentReportedCount) {
                endSample();
            }
            this.mCurrentReportedTotalTime = totalTime;
            this.mCurrentReportedCount = count;
        }

        public void add(long deltaTime, int deltaCount) {
            update(this.mCurrentReportedTotalTime + deltaTime, this.mCurrentReportedCount + deltaCount);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            super.onTimeStarted(elapsedRealtime, baseUptime, baseRealtime);
            if (this.mTrackingReportedValues) {
                this.mUnpluggedReportedTotalTime = this.mCurrentReportedTotalTime;
                this.mUnpluggedReportedCount = this.mCurrentReportedCount;
            }
            this.mTimeBaseRunning = true;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            super.onTimeStopped(elapsedRealtime, baseUptime, baseRealtime);
            this.mTimeBaseRunning = false;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, android.os.BatteryStats.Timer
        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
            pw.println(prefix + "mCurrentReportedCount=" + this.mCurrentReportedCount + " mUnpluggedReportedCount=" + this.mUnpluggedReportedCount + " mCurrentReportedTotalTime=" + this.mCurrentReportedTotalTime + " mUnpluggedReportedTotalTime=" + this.mUnpluggedReportedTotalTime);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        protected long computeRunTimeLocked(long curBatteryRealtime) {
            return this.mTotalTime + ((this.mTimeBaseRunning && this.mTrackingReportedValues) ? this.mCurrentReportedTotalTime - this.mUnpluggedReportedTotalTime : 0L);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        protected int computeCurrentCountLocked() {
            return this.mCount + ((this.mTimeBaseRunning && this.mTrackingReportedValues) ? this.mCurrentReportedCount - this.mUnpluggedReportedCount : 0);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeInt(this.mCurrentReportedCount);
            out.writeInt(this.mUnpluggedReportedCount);
            out.writeLong(this.mCurrentReportedTotalTime);
            out.writeLong(this.mUnpluggedReportedTotalTime);
            out.writeInt(this.mTrackingReportedValues ? 1 : 0);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset) {
            super.reset(detachIfReset);
            this.mTrackingReportedValues = false;
            this.mUnpluggedReportedTotalTime = 0L;
            this.mUnpluggedReportedCount = 0;
            return true;
        }
    }

    /* loaded from: classes3.dex */
    public static class BatchTimer extends Timer {
        boolean mInDischarge;
        long mLastAddedDuration;
        long mLastAddedTime;
        final Uid mUid;

        BatchTimer(Clocks clocks, Uid uid, int type, TimeBase timeBase, Parcel in) {
            super(clocks, type, timeBase, in);
            this.mUid = uid;
            this.mLastAddedTime = in.readLong();
            this.mLastAddedDuration = in.readLong();
            this.mInDischarge = timeBase.isRunning();
        }

        BatchTimer(Clocks clocks, Uid uid, int type, TimeBase timeBase) {
            super(clocks, type, timeBase);
            this.mUid = uid;
            this.mInDischarge = timeBase.isRunning();
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(this.mLastAddedTime);
            out.writeLong(this.mLastAddedDuration);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            recomputeLastDuration(this.mClocks.elapsedRealtime() * 1000, false);
            this.mInDischarge = false;
            super.onTimeStopped(elapsedRealtime, baseUptime, baseRealtime);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            recomputeLastDuration(elapsedRealtime, false);
            this.mInDischarge = true;
            if (this.mLastAddedTime == elapsedRealtime) {
                this.mTotalTime += this.mLastAddedDuration;
            }
            super.onTimeStarted(elapsedRealtime, baseUptime, baseRealtime);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, android.os.BatteryStats.Timer
        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
            pw.println(prefix + "mLastAddedTime=" + this.mLastAddedTime + " mLastAddedDuration=" + this.mLastAddedDuration);
        }

        private long computeOverage(long curTime) {
            if (this.mLastAddedTime > 0) {
                return this.mLastAddedDuration - curTime;
            }
            return 0L;
        }

        private void recomputeLastDuration(long curTime, boolean abort) {
            long overage = computeOverage(curTime);
            if (overage > 0) {
                if (this.mInDischarge) {
                    this.mTotalTime -= overage;
                }
                if (abort) {
                    this.mLastAddedTime = 0L;
                    return;
                }
                this.mLastAddedTime = curTime;
                this.mLastAddedDuration -= overage;
            }
        }

        public void addDuration(BatteryStatsImpl stats, long durationMillis) {
            long now = this.mClocks.elapsedRealtime() * 1000;
            recomputeLastDuration(now, true);
            this.mLastAddedTime = now;
            this.mLastAddedDuration = 1000 * durationMillis;
            if (this.mInDischarge) {
                this.mTotalTime += this.mLastAddedDuration;
                this.mCount++;
            }
        }

        public void abortLastDuration(BatteryStatsImpl stats) {
            long now = this.mClocks.elapsedRealtime() * 1000;
            recomputeLastDuration(now, true);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        protected int computeCurrentCountLocked() {
            return this.mCount;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        protected long computeRunTimeLocked(long curBatteryRealtime) {
            long overage = computeOverage(this.mClocks.elapsedRealtime() * 1000);
            if (overage > 0) {
                this.mTotalTime = overage;
                return overage;
            }
            return this.mTotalTime;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset) {
            long now = this.mClocks.elapsedRealtime() * 1000;
            recomputeLastDuration(now, true);
            boolean stillActive = this.mLastAddedTime == now;
            super.reset(!stillActive && detachIfReset);
            return !stillActive;
        }
    }

    /* loaded from: classes3.dex */
    public static class DurationTimer extends StopwatchTimer {
        long mCurrentDurationMs;
        long mMaxDurationMs;
        long mStartTimeMs;
        long mTotalDurationMs;

        public DurationTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase, Parcel in) {
            super(clocks, uid, type, timerPool, timeBase, in);
            this.mStartTimeMs = -1L;
            this.mMaxDurationMs = in.readLong();
            this.mTotalDurationMs = in.readLong();
            this.mCurrentDurationMs = in.readLong();
        }

        public DurationTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase) {
            super(clocks, uid, type, timerPool, timeBase);
            this.mStartTimeMs = -1L;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.StopwatchTimer, com.android.internal.os.BatteryStatsImpl.Timer
        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(getMaxDurationMsLocked(elapsedRealtimeUs / 1000));
            out.writeLong(this.mTotalDurationMs);
            out.writeLong(getCurrentDurationMsLocked(elapsedRealtimeUs / 1000));
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        public void writeSummaryFromParcelLocked(Parcel out, long elapsedRealtimeUs) {
            super.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
            out.writeLong(getMaxDurationMsLocked(elapsedRealtimeUs / 1000));
            out.writeLong(getTotalDurationMsLocked(elapsedRealtimeUs / 1000));
        }

        @Override // com.android.internal.os.BatteryStatsImpl.StopwatchTimer, com.android.internal.os.BatteryStatsImpl.Timer
        public void readSummaryFromParcelLocked(Parcel in) {
            super.readSummaryFromParcelLocked(in);
            this.mMaxDurationMs = in.readLong();
            this.mTotalDurationMs = in.readLong();
            this.mStartTimeMs = -1L;
            this.mCurrentDurationMs = 0L;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStarted(long elapsedRealtimeUs, long baseUptime, long baseRealtime) {
            super.onTimeStarted(elapsedRealtimeUs, baseUptime, baseRealtime);
            if (this.mNesting > 0) {
                this.mStartTimeMs = baseRealtime / 1000;
            }
        }

        @Override // com.android.internal.os.BatteryStatsImpl.StopwatchTimer, com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtimeUs, long baseUptime, long baseRealtimeUs) {
            super.onTimeStopped(elapsedRealtimeUs, baseUptime, baseRealtimeUs);
            if (this.mNesting > 0) {
                this.mCurrentDurationMs += (baseRealtimeUs / 1000) - this.mStartTimeMs;
            }
            this.mStartTimeMs = -1L;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.StopwatchTimer, com.android.internal.os.BatteryStatsImpl.Timer, android.os.BatteryStats.Timer
        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.StopwatchTimer
        public void startRunningLocked(long elapsedRealtimeMs) {
            super.startRunningLocked(elapsedRealtimeMs);
            if (this.mNesting == 1 && this.mTimeBase.isRunning()) {
                this.mStartTimeMs = this.mTimeBase.getRealtime(elapsedRealtimeMs * 1000) / 1000;
            }
        }

        @Override // com.android.internal.os.BatteryStatsImpl.StopwatchTimer
        public void stopRunningLocked(long elapsedRealtimeMs) {
            if (this.mNesting == 1) {
                long durationMs = getCurrentDurationMsLocked(elapsedRealtimeMs);
                this.mTotalDurationMs += durationMs;
                if (durationMs > this.mMaxDurationMs) {
                    this.mMaxDurationMs = durationMs;
                }
                this.mStartTimeMs = -1L;
                this.mCurrentDurationMs = 0L;
            }
            super.stopRunningLocked(elapsedRealtimeMs);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.StopwatchTimer, com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset) {
            boolean result = super.reset(detachIfReset);
            this.mMaxDurationMs = 0L;
            this.mTotalDurationMs = 0L;
            this.mCurrentDurationMs = 0L;
            if (this.mNesting > 0) {
                this.mStartTimeMs = this.mTimeBase.getRealtime(this.mClocks.elapsedRealtime() * 1000) / 1000;
            } else {
                this.mStartTimeMs = -1L;
            }
            return result;
        }

        @Override // android.os.BatteryStats.Timer
        public long getMaxDurationMsLocked(long elapsedRealtimeMs) {
            if (this.mNesting > 0) {
                long durationMs = getCurrentDurationMsLocked(elapsedRealtimeMs);
                if (durationMs > this.mMaxDurationMs) {
                    return durationMs;
                }
            }
            return this.mMaxDurationMs;
        }

        @Override // android.os.BatteryStats.Timer
        public long getCurrentDurationMsLocked(long elapsedRealtimeMs) {
            long durationMs = this.mCurrentDurationMs;
            if (this.mNesting > 0 && this.mTimeBase.isRunning()) {
                return durationMs + ((this.mTimeBase.getRealtime(elapsedRealtimeMs * 1000) / 1000) - this.mStartTimeMs);
            }
            return durationMs;
        }

        @Override // android.os.BatteryStats.Timer
        public long getTotalDurationMsLocked(long elapsedRealtimeMs) {
            return this.mTotalDurationMs + getCurrentDurationMsLocked(elapsedRealtimeMs);
        }
    }

    /* loaded from: classes3.dex */
    public static class StopwatchTimer extends Timer {
        long mAcquireTime;
        @VisibleForTesting
        public boolean mInList;
        int mNesting;
        long mTimeout;
        final ArrayList<StopwatchTimer> mTimerPool;
        final Uid mUid;
        long mUpdateTime;

        public StopwatchTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase, Parcel in) {
            super(clocks, type, timeBase, in);
            this.mAcquireTime = -1L;
            this.mUid = uid;
            this.mTimerPool = timerPool;
            this.mUpdateTime = in.readLong();
        }

        public StopwatchTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase) {
            super(clocks, type, timeBase);
            this.mAcquireTime = -1L;
            this.mUid = uid;
            this.mTimerPool = timerPool;
        }

        public void setTimeout(long timeout) {
            this.mTimeout = timeout;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(this.mUpdateTime);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            if (this.mNesting > 0) {
                super.onTimeStopped(elapsedRealtime, baseUptime, baseRealtime);
                this.mUpdateTime = baseRealtime;
            }
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, android.os.BatteryStats.Timer
        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
            pw.println(prefix + "mNesting=" + this.mNesting + " mUpdateTime=" + this.mUpdateTime + " mAcquireTime=" + this.mAcquireTime);
        }

        public void startRunningLocked(long elapsedRealtimeMs) {
            int i = this.mNesting;
            this.mNesting = i + 1;
            if (i == 0) {
                long batteryRealtime = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
                this.mUpdateTime = batteryRealtime;
                ArrayList<StopwatchTimer> arrayList = this.mTimerPool;
                if (arrayList != null) {
                    refreshTimersLocked(batteryRealtime, arrayList, null);
                    this.mTimerPool.add(this);
                }
                if (this.mTimeBase.isRunning()) {
                    this.mCount++;
                    this.mAcquireTime = this.mTotalTime;
                    return;
                }
                this.mAcquireTime = -1L;
            }
        }

        @Override // android.os.BatteryStats.Timer
        public boolean isRunningLocked() {
            return this.mNesting > 0;
        }

        public void stopRunningLocked(long elapsedRealtimeMs) {
            int i = this.mNesting;
            if (i == 0) {
                return;
            }
            int i2 = i - 1;
            this.mNesting = i2;
            if (i2 == 0) {
                long batteryRealtime = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
                ArrayList<StopwatchTimer> arrayList = this.mTimerPool;
                if (arrayList != null) {
                    refreshTimersLocked(batteryRealtime, arrayList, null);
                    this.mTimerPool.remove(this);
                } else {
                    this.mNesting = 1;
                    this.mTotalTime = computeRunTimeLocked(batteryRealtime);
                    this.mNesting = 0;
                }
                if (this.mAcquireTime >= 0 && this.mTotalTime == this.mAcquireTime) {
                    this.mCount--;
                }
            }
        }

        public void stopAllRunningLocked(long elapsedRealtimeMs) {
            if (this.mNesting > 0) {
                this.mNesting = 1;
                stopRunningLocked(elapsedRealtimeMs);
            }
        }

        private static long refreshTimersLocked(long batteryRealtime, ArrayList<StopwatchTimer> pool, StopwatchTimer self) {
            long selfTime = 0;
            int N = pool.size();
            for (int i = N - 1; i >= 0; i--) {
                StopwatchTimer t = pool.get(i);
                long heldTime = batteryRealtime - t.mUpdateTime;
                if (heldTime > 0) {
                    long myTime = heldTime / N;
                    if (t == self) {
                        selfTime = myTime;
                    }
                    t.mTotalTime += myTime;
                }
                t.mUpdateTime = batteryRealtime;
            }
            return selfTime;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        protected long computeRunTimeLocked(long curBatteryRealtime) {
            long j = this.mTimeout;
            long j2 = 0;
            if (j > 0) {
                long j3 = this.mUpdateTime;
                if (curBatteryRealtime > j3 + j) {
                    curBatteryRealtime = j3 + j;
                }
            }
            long j4 = this.mTotalTime;
            if (this.mNesting > 0) {
                long j5 = curBatteryRealtime - this.mUpdateTime;
                ArrayList<StopwatchTimer> arrayList = this.mTimerPool;
                j2 = j5 / (arrayList != null ? arrayList.size() : 1);
            }
            return j4 + j2;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        protected int computeCurrentCountLocked() {
            return this.mCount;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset) {
            boolean z = true;
            boolean canDetach = this.mNesting <= 0;
            if (!canDetach || !detachIfReset) {
                z = false;
            }
            super.reset(z);
            if (this.mNesting > 0) {
                this.mUpdateTime = this.mTimeBase.getRealtime(this.mClocks.elapsedRealtime() * 1000);
            }
            this.mAcquireTime = -1L;
            return canDetach;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        @UnsupportedAppUsage
        public void detach() {
            super.detach();
            ArrayList<StopwatchTimer> arrayList = this.mTimerPool;
            if (arrayList != null) {
                arrayList.remove(this);
            }
        }

        @Override // com.android.internal.os.BatteryStatsImpl.Timer
        public void readSummaryFromParcelLocked(Parcel in) {
            super.readSummaryFromParcelLocked(in);
            this.mNesting = 0;
        }

        public void setMark(long elapsedRealtimeMs) {
            long batteryRealtime = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
            if (this.mNesting > 0) {
                ArrayList<StopwatchTimer> arrayList = this.mTimerPool;
                if (arrayList != null) {
                    refreshTimersLocked(batteryRealtime, arrayList, this);
                } else {
                    this.mTotalTime += batteryRealtime - this.mUpdateTime;
                    this.mUpdateTime = batteryRealtime;
                }
            }
            this.mTimeBeforeMark = this.mTotalTime;
        }
    }

    /* loaded from: classes3.dex */
    public static class DualTimer extends DurationTimer {
        private final DurationTimer mSubTimer;

        public DualTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase, TimeBase subTimeBase, Parcel in) {
            super(clocks, uid, type, timerPool, timeBase, in);
            this.mSubTimer = new DurationTimer(clocks, uid, type, null, subTimeBase, in);
        }

        public DualTimer(Clocks clocks, Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase, TimeBase subTimeBase) {
            super(clocks, uid, type, timerPool, timeBase);
            this.mSubTimer = new DurationTimer(clocks, uid, type, null, subTimeBase);
        }

        @Override // android.os.BatteryStats.Timer
        public DurationTimer getSubTimer() {
            return this.mSubTimer;
        }

        @Override // com.android.internal.os.BatteryStatsImpl.DurationTimer, com.android.internal.os.BatteryStatsImpl.StopwatchTimer
        public void startRunningLocked(long elapsedRealtimeMs) {
            super.startRunningLocked(elapsedRealtimeMs);
            this.mSubTimer.startRunningLocked(elapsedRealtimeMs);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.DurationTimer, com.android.internal.os.BatteryStatsImpl.StopwatchTimer
        public void stopRunningLocked(long elapsedRealtimeMs) {
            super.stopRunningLocked(elapsedRealtimeMs);
            this.mSubTimer.stopRunningLocked(elapsedRealtimeMs);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.StopwatchTimer
        public void stopAllRunningLocked(long elapsedRealtimeMs) {
            super.stopAllRunningLocked(elapsedRealtimeMs);
            this.mSubTimer.stopAllRunningLocked(elapsedRealtimeMs);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.DurationTimer, com.android.internal.os.BatteryStatsImpl.StopwatchTimer, com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public boolean reset(boolean detachIfReset) {
            boolean active = false | (!this.mSubTimer.reset(false));
            return !(active | (super.reset(detachIfReset) ^ true));
        }

        @Override // com.android.internal.os.BatteryStatsImpl.StopwatchTimer, com.android.internal.os.BatteryStatsImpl.Timer, com.android.internal.os.BatteryStatsImpl.TimeBaseObs
        public void detach() {
            this.mSubTimer.detach();
            super.detach();
        }

        @Override // com.android.internal.os.BatteryStatsImpl.DurationTimer, com.android.internal.os.BatteryStatsImpl.StopwatchTimer, com.android.internal.os.BatteryStatsImpl.Timer
        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            this.mSubTimer.writeToParcel(out, elapsedRealtimeUs);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.DurationTimer, com.android.internal.os.BatteryStatsImpl.Timer
        public void writeSummaryFromParcelLocked(Parcel out, long elapsedRealtimeUs) {
            super.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
            this.mSubTimer.writeSummaryFromParcelLocked(out, elapsedRealtimeUs);
        }

        @Override // com.android.internal.os.BatteryStatsImpl.DurationTimer, com.android.internal.os.BatteryStatsImpl.StopwatchTimer, com.android.internal.os.BatteryStatsImpl.Timer
        public void readSummaryFromParcelLocked(Parcel in) {
            super.readSummaryFromParcelLocked(in);
            this.mSubTimer.readSummaryFromParcelLocked(in);
        }
    }

    /* loaded from: classes3.dex */
    public abstract class OverflowArrayMap<T> {
        private static final String OVERFLOW_NAME = "*overflow*";
        ArrayMap<String, MutableInt> mActiveOverflow;
        T mCurOverflow;
        long mLastCleanupTime;
        long mLastClearTime;
        long mLastOverflowFinishTime;
        long mLastOverflowTime;
        final ArrayMap<String, T> mMap = new ArrayMap<>();
        final int mUid;

        public abstract T instantiateObject();

        public OverflowArrayMap(int uid) {
            this.mUid = uid;
        }

        public ArrayMap<String, T> getMap() {
            return this.mMap;
        }

        public void clear() {
            this.mLastClearTime = SystemClock.elapsedRealtime();
            this.mMap.clear();
            this.mCurOverflow = null;
            this.mActiveOverflow = null;
        }

        public void add(String name, T obj) {
            if (name == null) {
                name = "";
            }
            this.mMap.put(name, obj);
            if (OVERFLOW_NAME.equals(name)) {
                this.mCurOverflow = obj;
            }
        }

        public void cleanup() {
            this.mLastCleanupTime = SystemClock.elapsedRealtime();
            ArrayMap<String, MutableInt> arrayMap = this.mActiveOverflow;
            if (arrayMap != null && arrayMap.size() == 0) {
                this.mActiveOverflow = null;
            }
            if (this.mActiveOverflow == null) {
                if (this.mMap.containsKey(OVERFLOW_NAME)) {
                    Slog.wtf(BatteryStatsImpl.TAG, "Cleaning up with no active overflow, but have overflow entry " + this.mMap.get(OVERFLOW_NAME));
                    this.mMap.remove(OVERFLOW_NAME);
                }
                this.mCurOverflow = null;
            } else if (this.mCurOverflow == null || !this.mMap.containsKey(OVERFLOW_NAME)) {
                Slog.wtf(BatteryStatsImpl.TAG, "Cleaning up with active overflow, but no overflow entry: cur=" + this.mCurOverflow + " map=" + this.mMap.get(OVERFLOW_NAME));
            }
        }

        public T startObject(String name) {
            MutableInt over;
            if (name == null) {
                name = "";
            }
            T obj = this.mMap.get(name);
            if (obj != null) {
                return obj;
            }
            ArrayMap<String, MutableInt> arrayMap = this.mActiveOverflow;
            if (arrayMap != null && (over = arrayMap.get(name)) != null) {
                T obj2 = this.mCurOverflow;
                if (obj2 == null) {
                    Slog.wtf(BatteryStatsImpl.TAG, "Have active overflow " + name + " but null overflow");
                    T instantiateObject = instantiateObject();
                    this.mCurOverflow = instantiateObject;
                    obj2 = instantiateObject;
                    this.mMap.put(OVERFLOW_NAME, obj2);
                }
                over.value++;
                return obj2;
            }
            int N = this.mMap.size();
            if (N >= BatteryStatsImpl.MAX_WAKELOCKS_PER_UID) {
                T obj3 = this.mCurOverflow;
                if (obj3 == null) {
                    T instantiateObject2 = instantiateObject();
                    this.mCurOverflow = instantiateObject2;
                    obj3 = instantiateObject2;
                    this.mMap.put(OVERFLOW_NAME, obj3);
                }
                if (this.mActiveOverflow == null) {
                    this.mActiveOverflow = new ArrayMap<>();
                }
                this.mActiveOverflow.put(name, new MutableInt(1));
                this.mLastOverflowTime = SystemClock.elapsedRealtime();
                return obj3;
            }
            T obj4 = instantiateObject();
            this.mMap.put(name, obj4);
            return obj4;
        }

        public T stopObject(String name) {
            MutableInt over;
            T obj;
            if (name == null) {
                name = "";
            }
            T obj2 = this.mMap.get(name);
            if (obj2 != null) {
                return obj2;
            }
            ArrayMap<String, MutableInt> arrayMap = this.mActiveOverflow;
            if (arrayMap != null && (over = arrayMap.get(name)) != null && (obj = this.mCurOverflow) != null) {
                over.value--;
                if (over.value <= 0) {
                    this.mActiveOverflow.remove(name);
                    this.mLastOverflowFinishTime = SystemClock.elapsedRealtime();
                }
                return obj;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find object for ");
            sb.append(name);
            sb.append(" in uid ");
            sb.append(this.mUid);
            sb.append(" mapsize=");
            sb.append(this.mMap.size());
            sb.append(" activeoverflow=");
            sb.append(this.mActiveOverflow);
            sb.append(" curoverflow=");
            sb.append(this.mCurOverflow);
            long now = SystemClock.elapsedRealtime();
            if (this.mLastOverflowTime != 0) {
                sb.append(" lastOverflowTime=");
                TimeUtils.formatDuration(this.mLastOverflowTime - now, sb);
            }
            if (this.mLastOverflowFinishTime != 0) {
                sb.append(" lastOverflowFinishTime=");
                TimeUtils.formatDuration(this.mLastOverflowFinishTime - now, sb);
            }
            if (this.mLastClearTime != 0) {
                sb.append(" lastClearTime=");
                TimeUtils.formatDuration(this.mLastClearTime - now, sb);
            }
            if (this.mLastCleanupTime != 0) {
                sb.append(" lastCleanupTime=");
                TimeUtils.formatDuration(this.mLastCleanupTime - now, sb);
            }
            Slog.wtf(BatteryStatsImpl.TAG, sb.toString());
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static class ControllerActivityCounterImpl extends BatteryStats.ControllerActivityCounter implements Parcelable {
        private final LongSamplingCounter mIdleTimeMillis;
        private final LongSamplingCounter mMonitoredRailChargeConsumedMaMs;
        private final LongSamplingCounter mPowerDrainMaMs;
        private final LongSamplingCounter mRxTimeMillis;
        private final LongSamplingCounter mScanTimeMillis;
        private final LongSamplingCounter mSleepTimeMillis;
        private final LongSamplingCounter[] mTxTimeMillis;

        public ControllerActivityCounterImpl(TimeBase timeBase, int numTxStates) {
            this.mIdleTimeMillis = new LongSamplingCounter(timeBase);
            this.mScanTimeMillis = new LongSamplingCounter(timeBase);
            this.mSleepTimeMillis = new LongSamplingCounter(timeBase);
            this.mRxTimeMillis = new LongSamplingCounter(timeBase);
            this.mTxTimeMillis = new LongSamplingCounter[numTxStates];
            for (int i = 0; i < numTxStates; i++) {
                this.mTxTimeMillis[i] = new LongSamplingCounter(timeBase);
            }
            this.mPowerDrainMaMs = new LongSamplingCounter(timeBase);
            this.mMonitoredRailChargeConsumedMaMs = new LongSamplingCounter(timeBase);
        }

        public ControllerActivityCounterImpl(TimeBase timeBase, int numTxStates, Parcel in) {
            this.mIdleTimeMillis = new LongSamplingCounter(timeBase, in);
            this.mScanTimeMillis = new LongSamplingCounter(timeBase, in);
            this.mSleepTimeMillis = new LongSamplingCounter(timeBase, in);
            this.mRxTimeMillis = new LongSamplingCounter(timeBase, in);
            int recordedTxStates = in.readInt();
            if (recordedTxStates != numTxStates) {
                throw new ParcelFormatException("inconsistent tx state lengths");
            }
            this.mTxTimeMillis = new LongSamplingCounter[numTxStates];
            for (int i = 0; i < numTxStates; i++) {
                this.mTxTimeMillis[i] = new LongSamplingCounter(timeBase, in);
            }
            this.mPowerDrainMaMs = new LongSamplingCounter(timeBase, in);
            this.mMonitoredRailChargeConsumedMaMs = new LongSamplingCounter(timeBase, in);
        }

        public void readSummaryFromParcel(Parcel in) {
            this.mIdleTimeMillis.readSummaryFromParcelLocked(in);
            this.mScanTimeMillis.readSummaryFromParcelLocked(in);
            this.mSleepTimeMillis.readSummaryFromParcelLocked(in);
            this.mRxTimeMillis.readSummaryFromParcelLocked(in);
            int recordedTxStates = in.readInt();
            LongSamplingCounter[] longSamplingCounterArr = this.mTxTimeMillis;
            if (recordedTxStates != longSamplingCounterArr.length) {
                throw new ParcelFormatException("inconsistent tx state lengths");
            }
            for (LongSamplingCounter counter : longSamplingCounterArr) {
                counter.readSummaryFromParcelLocked(in);
            }
            this.mPowerDrainMaMs.readSummaryFromParcelLocked(in);
            this.mMonitoredRailChargeConsumedMaMs.readSummaryFromParcelLocked(in);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public void writeSummaryToParcel(Parcel dest) {
            LongSamplingCounter[] longSamplingCounterArr;
            this.mIdleTimeMillis.writeSummaryFromParcelLocked(dest);
            this.mScanTimeMillis.writeSummaryFromParcelLocked(dest);
            this.mSleepTimeMillis.writeSummaryFromParcelLocked(dest);
            this.mRxTimeMillis.writeSummaryFromParcelLocked(dest);
            dest.writeInt(this.mTxTimeMillis.length);
            for (LongSamplingCounter counter : this.mTxTimeMillis) {
                counter.writeSummaryFromParcelLocked(dest);
            }
            this.mPowerDrainMaMs.writeSummaryFromParcelLocked(dest);
            this.mMonitoredRailChargeConsumedMaMs.writeSummaryFromParcelLocked(dest);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            LongSamplingCounter[] longSamplingCounterArr;
            this.mIdleTimeMillis.writeToParcel(dest);
            this.mScanTimeMillis.writeToParcel(dest);
            this.mSleepTimeMillis.writeToParcel(dest);
            this.mRxTimeMillis.writeToParcel(dest);
            dest.writeInt(this.mTxTimeMillis.length);
            for (LongSamplingCounter counter : this.mTxTimeMillis) {
                counter.writeToParcel(dest);
            }
            this.mPowerDrainMaMs.writeToParcel(dest);
            this.mMonitoredRailChargeConsumedMaMs.writeToParcel(dest);
        }

        public void reset(boolean detachIfReset) {
            LongSamplingCounter[] longSamplingCounterArr;
            this.mIdleTimeMillis.reset(detachIfReset);
            this.mScanTimeMillis.reset(detachIfReset);
            this.mSleepTimeMillis.reset(detachIfReset);
            this.mRxTimeMillis.reset(detachIfReset);
            for (LongSamplingCounter counter : this.mTxTimeMillis) {
                counter.reset(detachIfReset);
            }
            this.mPowerDrainMaMs.reset(detachIfReset);
            this.mMonitoredRailChargeConsumedMaMs.reset(detachIfReset);
        }

        public void detach() {
            LongSamplingCounter[] longSamplingCounterArr;
            this.mIdleTimeMillis.detach();
            this.mScanTimeMillis.detach();
            this.mSleepTimeMillis.detach();
            this.mRxTimeMillis.detach();
            for (LongSamplingCounter counter : this.mTxTimeMillis) {
                counter.detach();
            }
            this.mPowerDrainMaMs.detach();
            this.mMonitoredRailChargeConsumedMaMs.detach();
        }

        @Override // android.os.BatteryStats.ControllerActivityCounter
        public LongSamplingCounter getIdleTimeCounter() {
            return this.mIdleTimeMillis;
        }

        @Override // android.os.BatteryStats.ControllerActivityCounter
        public LongSamplingCounter getScanTimeCounter() {
            return this.mScanTimeMillis;
        }

        @Override // android.os.BatteryStats.ControllerActivityCounter
        public LongSamplingCounter getSleepTimeCounter() {
            return this.mSleepTimeMillis;
        }

        @Override // android.os.BatteryStats.ControllerActivityCounter
        public LongSamplingCounter getRxTimeCounter() {
            return this.mRxTimeMillis;
        }

        @Override // android.os.BatteryStats.ControllerActivityCounter
        public LongSamplingCounter[] getTxTimeCounters() {
            return this.mTxTimeMillis;
        }

        @Override // android.os.BatteryStats.ControllerActivityCounter
        public LongSamplingCounter getPowerCounter() {
            return this.mPowerDrainMaMs;
        }

        @Override // android.os.BatteryStats.ControllerActivityCounter
        public LongSamplingCounter getMonitoredRailChargeConsumedMaMs() {
            return this.mMonitoredRailChargeConsumedMaMs;
        }
    }

    public SamplingTimer getRpmTimerLocked(String name) {
        SamplingTimer rpmt = this.mRpmStats.get(name);
        if (rpmt == null) {
            SamplingTimer rpmt2 = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase);
            this.mRpmStats.put(name, rpmt2);
            return rpmt2;
        }
        return rpmt;
    }

    public SamplingTimer getScreenOffRpmTimerLocked(String name) {
        SamplingTimer rpmt = this.mScreenOffRpmStats.get(name);
        if (rpmt == null) {
            SamplingTimer rpmt2 = new SamplingTimer(this.mClocks, this.mOnBatteryScreenOffTimeBase);
            this.mScreenOffRpmStats.put(name, rpmt2);
            return rpmt2;
        }
        return rpmt;
    }

    public SamplingTimer getWakeupReasonTimerLocked(String name) {
        SamplingTimer timer = this.mWakeupReasonStats.get(name);
        if (timer == null) {
            SamplingTimer timer2 = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase);
            this.mWakeupReasonStats.put(name, timer2);
            return timer2;
        }
        return timer;
    }

    public SamplingTimer getKernelWakelockTimerLocked(String name) {
        SamplingTimer kwlt = this.mKernelWakelockStats.get(name);
        if (kwlt == null) {
            SamplingTimer kwlt2 = new SamplingTimer(this.mClocks, this.mOnBatteryScreenOffTimeBase);
            this.mKernelWakelockStats.put(name, kwlt2);
            return kwlt2;
        }
        return kwlt;
    }

    public SamplingTimer getKernelMemoryTimerLocked(long bucket) {
        SamplingTimer kmt = this.mKernelMemoryStats.get(bucket);
        if (kmt == null) {
            SamplingTimer kmt2 = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase);
            this.mKernelMemoryStats.put(bucket, kmt2);
            return kmt2;
        }
        return kmt;
    }

    private int writeHistoryTag(BatteryStats.HistoryTag tag) {
        Integer idxObj = this.mHistoryTagPool.get(tag);
        if (idxObj != null) {
            return idxObj.intValue();
        }
        int idx = this.mNextHistoryTagIdx;
        BatteryStats.HistoryTag key = new BatteryStats.HistoryTag();
        key.setTo(tag);
        tag.poolIdx = idx;
        this.mHistoryTagPool.put(key, Integer.valueOf(idx));
        this.mNextHistoryTagIdx++;
        this.mNumHistoryTagChars += key.string.length() + 1;
        return idx;
    }

    private void readHistoryTag(int index, BatteryStats.HistoryTag tag) {
        String[] strArr = this.mReadHistoryStrings;
        if (index < strArr.length) {
            tag.string = strArr[index];
            tag.uid = this.mReadHistoryUids[index];
        } else {
            tag.string = null;
            tag.uid = 0;
        }
        tag.poolIdx = index;
    }

    public void writeHistoryDelta(Parcel dest, BatteryStats.HistoryItem cur, BatteryStats.HistoryItem last) {
        int deltaTimeToken;
        int wakeLockIndex;
        int wakeReasonIndex;
        if (last == null || cur.cmd != 0) {
            dest.writeInt(DELTA_TIME_ABS);
            cur.writeToParcel(dest, 0);
            return;
        }
        long deltaTime = cur.time - last.time;
        int lastBatteryLevelInt = buildBatteryLevelInt(last);
        int lastStateInt = buildStateInt(last);
        if (deltaTime < 0 || deltaTime > 2147483647L) {
            deltaTimeToken = EventLogTags.SYSUI_VIEW_VISIBILITY;
        } else if (deltaTime >= 524285) {
            deltaTimeToken = DELTA_TIME_INT;
        } else {
            deltaTimeToken = (int) deltaTime;
        }
        int firstToken = (cur.states & DELTA_STATE_MASK) | deltaTimeToken;
        int includeStepDetails = this.mLastHistoryStepLevel > cur.batteryLevel ? 1 : 0;
        boolean computeStepDetails = includeStepDetails != 0 || this.mLastHistoryStepDetails == null;
        int batteryLevelInt = buildBatteryLevelInt(cur) | includeStepDetails;
        boolean batteryLevelIntChanged = batteryLevelInt != lastBatteryLevelInt;
        if (batteryLevelIntChanged) {
            firstToken |= 524288;
        }
        int stateInt = buildStateInt(cur);
        boolean stateIntChanged = stateInt != lastStateInt;
        if (stateIntChanged) {
            firstToken |= 1048576;
        }
        int i = cur.states2;
        int lastBatteryLevelInt2 = last.states2;
        boolean state2IntChanged = i != lastBatteryLevelInt2;
        if (state2IntChanged) {
            firstToken |= 2097152;
        }
        if (cur.wakelockTag != null || cur.wakeReasonTag != null) {
            firstToken |= 4194304;
        }
        if (cur.eventCode != 0) {
            firstToken |= 8388608;
        }
        int i2 = cur.batteryChargeUAh;
        int lastStateInt2 = last.batteryChargeUAh;
        boolean batteryChargeChanged = i2 != lastStateInt2;
        if (batteryChargeChanged) {
            firstToken |= 16777216;
        }
        dest.writeInt(firstToken);
        if (deltaTimeToken >= DELTA_TIME_INT) {
            if (deltaTimeToken == DELTA_TIME_INT) {
                dest.writeInt((int) deltaTime);
            } else {
                dest.writeLong(deltaTime);
            }
        }
        if (batteryLevelIntChanged) {
            dest.writeInt(batteryLevelInt);
        }
        if (stateIntChanged) {
            dest.writeInt(stateInt);
        }
        if (state2IntChanged) {
            dest.writeInt(cur.states2);
        }
        if (cur.wakelockTag != null || cur.wakeReasonTag != null) {
            if (cur.wakelockTag != null) {
                wakeLockIndex = writeHistoryTag(cur.wakelockTag);
            } else {
                wakeLockIndex = 65535;
            }
            if (cur.wakeReasonTag != null) {
                wakeReasonIndex = writeHistoryTag(cur.wakeReasonTag);
            } else {
                wakeReasonIndex = 65535;
            }
            dest.writeInt((wakeReasonIndex << 16) | wakeLockIndex);
        }
        if (cur.eventCode != 0) {
            int index = writeHistoryTag(cur.eventTag);
            int codeAndIndex = (cur.eventCode & 65535) | (index << 16);
            dest.writeInt(codeAndIndex);
        }
        if (computeStepDetails) {
            PlatformIdleStateCallback platformIdleStateCallback = this.mPlatformIdleStateCallback;
            if (platformIdleStateCallback != null) {
                this.mCurHistoryStepDetails.statPlatformIdleState = platformIdleStateCallback.getPlatformLowPowerStats();
                this.mCurHistoryStepDetails.statSubsystemPowerState = this.mPlatformIdleStateCallback.getSubsystemLowPowerStats();
            }
            computeHistoryStepDetails(this.mCurHistoryStepDetails, this.mLastHistoryStepDetails);
            if (includeStepDetails != 0) {
                this.mCurHistoryStepDetails.writeToParcel(dest);
            }
            BatteryStats.HistoryStepDetails historyStepDetails = this.mCurHistoryStepDetails;
            cur.stepDetails = historyStepDetails;
            this.mLastHistoryStepDetails = historyStepDetails;
        } else {
            cur.stepDetails = null;
        }
        if (this.mLastHistoryStepLevel < cur.batteryLevel) {
            this.mLastHistoryStepDetails = null;
        }
        this.mLastHistoryStepLevel = cur.batteryLevel;
        if (batteryChargeChanged) {
            dest.writeInt(cur.batteryChargeUAh);
        }
        dest.writeDouble(cur.modemRailChargeMah);
        dest.writeDouble(cur.wifiRailChargeMah);
    }

    private int buildBatteryLevelInt(BatteryStats.HistoryItem h) {
        return ((h.batteryLevel << 25) & DELTA_STATE_MASK) | ((h.batteryTemperature << 15) & 33521664) | ((h.batteryVoltage << 1) & 32766);
    }

    private void readBatteryLevelInt(int batteryLevelInt, BatteryStats.HistoryItem out) {
        out.batteryLevel = (byte) ((DELTA_STATE_MASK & batteryLevelInt) >>> 25);
        out.batteryTemperature = (short) ((33521664 & batteryLevelInt) >>> 15);
        out.batteryVoltage = (char) ((batteryLevelInt & 32766) >>> 1);
    }

    private int buildStateInt(BatteryStats.HistoryItem h) {
        int plugType = 0;
        if ((h.batteryPlugType & 1) != 0) {
            plugType = 1;
        } else if ((h.batteryPlugType & 2) != 0) {
            plugType = 2;
        } else if ((h.batteryPlugType & 4) != 0) {
            plugType = 3;
        }
        return ((h.batteryStatus & 7) << 29) | ((h.batteryHealth & 7) << 26) | ((plugType & 3) << 24) | (h.states & 16777215);
    }

    private void computeHistoryStepDetails(BatteryStats.HistoryStepDetails out, BatteryStats.HistoryStepDetails last) {
        BatteryStats.HistoryStepDetails tmp = last != null ? this.mTmpHistoryStepDetails : out;
        requestImmediateCpuUpdate();
        if (last == null) {
            int NU = this.mUidStats.size();
            for (int i = 0; i < NU; i++) {
                Uid uid = this.mUidStats.valueAt(i);
                uid.mLastStepUserTime = uid.mCurStepUserTime;
                uid.mLastStepSystemTime = uid.mCurStepSystemTime;
            }
            this.mLastStepCpuUserTime = this.mCurStepCpuUserTime;
            this.mLastStepCpuSystemTime = this.mCurStepCpuSystemTime;
            this.mLastStepStatUserTime = this.mCurStepStatUserTime;
            this.mLastStepStatSystemTime = this.mCurStepStatSystemTime;
            this.mLastStepStatIOWaitTime = this.mCurStepStatIOWaitTime;
            this.mLastStepStatIrqTime = this.mCurStepStatIrqTime;
            this.mLastStepStatSoftIrqTime = this.mCurStepStatSoftIrqTime;
            this.mLastStepStatIdleTime = this.mCurStepStatIdleTime;
            tmp.clear();
            return;
        }
        out.userTime = (int) (this.mCurStepCpuUserTime - this.mLastStepCpuUserTime);
        out.systemTime = (int) (this.mCurStepCpuSystemTime - this.mLastStepCpuSystemTime);
        out.statUserTime = (int) (this.mCurStepStatUserTime - this.mLastStepStatUserTime);
        out.statSystemTime = (int) (this.mCurStepStatSystemTime - this.mLastStepStatSystemTime);
        out.statIOWaitTime = (int) (this.mCurStepStatIOWaitTime - this.mLastStepStatIOWaitTime);
        out.statIrqTime = (int) (this.mCurStepStatIrqTime - this.mLastStepStatIrqTime);
        out.statSoftIrqTime = (int) (this.mCurStepStatSoftIrqTime - this.mLastStepStatSoftIrqTime);
        out.statIdlTime = (int) (this.mCurStepStatIdleTime - this.mLastStepStatIdleTime);
        out.appCpuUid3 = -1;
        out.appCpuUid2 = -1;
        out.appCpuUid1 = -1;
        out.appCpuUTime3 = 0;
        out.appCpuUTime2 = 0;
        out.appCpuUTime1 = 0;
        out.appCpuSTime3 = 0;
        out.appCpuSTime2 = 0;
        out.appCpuSTime1 = 0;
        int NU2 = this.mUidStats.size();
        for (int i2 = 0; i2 < NU2; i2++) {
            Uid uid2 = this.mUidStats.valueAt(i2);
            int totalUTime = (int) (uid2.mCurStepUserTime - uid2.mLastStepUserTime);
            int totalSTime = (int) (uid2.mCurStepSystemTime - uid2.mLastStepSystemTime);
            int totalTime = totalUTime + totalSTime;
            uid2.mLastStepUserTime = uid2.mCurStepUserTime;
            uid2.mLastStepSystemTime = uid2.mCurStepSystemTime;
            if (totalTime > out.appCpuUTime3 + out.appCpuSTime3) {
                if (totalTime <= out.appCpuUTime2 + out.appCpuSTime2) {
                    out.appCpuUid3 = uid2.mUid;
                    out.appCpuUTime3 = totalUTime;
                    out.appCpuSTime3 = totalSTime;
                } else {
                    out.appCpuUid3 = out.appCpuUid2;
                    out.appCpuUTime3 = out.appCpuUTime2;
                    out.appCpuSTime3 = out.appCpuSTime2;
                    if (totalTime <= out.appCpuUTime1 + out.appCpuSTime1) {
                        out.appCpuUid2 = uid2.mUid;
                        out.appCpuUTime2 = totalUTime;
                        out.appCpuSTime2 = totalSTime;
                    } else {
                        out.appCpuUid2 = out.appCpuUid1;
                        out.appCpuUTime2 = out.appCpuUTime1;
                        out.appCpuSTime2 = out.appCpuSTime1;
                        out.appCpuUid1 = uid2.mUid;
                        out.appCpuUTime1 = totalUTime;
                        out.appCpuSTime1 = totalSTime;
                    }
                }
            }
        }
        this.mLastStepCpuUserTime = this.mCurStepCpuUserTime;
        this.mLastStepCpuSystemTime = this.mCurStepCpuSystemTime;
        this.mLastStepStatUserTime = this.mCurStepStatUserTime;
        this.mLastStepStatSystemTime = this.mCurStepStatSystemTime;
        this.mLastStepStatIOWaitTime = this.mCurStepStatIOWaitTime;
        this.mLastStepStatIrqTime = this.mCurStepStatIrqTime;
        this.mLastStepStatSoftIrqTime = this.mCurStepStatSoftIrqTime;
        this.mLastStepStatIdleTime = this.mCurStepStatIdleTime;
    }

    public void readHistoryDelta(Parcel src, BatteryStats.HistoryItem cur) {
        int batteryLevelInt;
        int firstToken = src.readInt();
        int deltaTimeToken = 524287 & firstToken;
        cur.cmd = (byte) 0;
        cur.numReadInts = 1;
        if (deltaTimeToken < DELTA_TIME_ABS) {
            cur.time += deltaTimeToken;
        } else if (deltaTimeToken == DELTA_TIME_ABS) {
            cur.time = src.readLong();
            cur.numReadInts += 2;
            cur.readFromParcel(src);
            return;
        } else if (deltaTimeToken == DELTA_TIME_INT) {
            int delta = src.readInt();
            cur.time += delta;
            cur.numReadInts++;
        } else {
            long delta2 = src.readLong();
            cur.time += delta2;
            cur.numReadInts += 2;
        }
        if ((524288 & firstToken) != 0) {
            batteryLevelInt = src.readInt();
            readBatteryLevelInt(batteryLevelInt, cur);
            cur.numReadInts++;
        } else {
            batteryLevelInt = 0;
        }
        if ((1048576 & firstToken) != 0) {
            int stateInt = src.readInt();
            cur.states = (16777215 & stateInt) | (DELTA_STATE_MASK & firstToken);
            cur.batteryStatus = (byte) ((stateInt >> 29) & 7);
            cur.batteryHealth = (byte) ((stateInt >> 26) & 7);
            cur.batteryPlugType = (byte) ((stateInt >> 24) & 3);
            byte b = cur.batteryPlugType;
            if (b == 1) {
                cur.batteryPlugType = (byte) 1;
            } else if (b == 2) {
                cur.batteryPlugType = (byte) 2;
            } else if (b == 3) {
                cur.batteryPlugType = (byte) 4;
            }
            cur.numReadInts++;
        } else {
            cur.states = (firstToken & DELTA_STATE_MASK) | (cur.states & 16777215);
        }
        if ((2097152 & firstToken) != 0) {
            cur.states2 = src.readInt();
        }
        if ((4194304 & firstToken) != 0) {
            int indexes = src.readInt();
            int wakeLockIndex = indexes & 65535;
            int wakeReasonIndex = (indexes >> 16) & 65535;
            if (wakeLockIndex != 65535) {
                cur.wakelockTag = cur.localWakelockTag;
                readHistoryTag(wakeLockIndex, cur.wakelockTag);
            } else {
                cur.wakelockTag = null;
            }
            if (wakeReasonIndex != 65535) {
                cur.wakeReasonTag = cur.localWakeReasonTag;
                readHistoryTag(wakeReasonIndex, cur.wakeReasonTag);
            } else {
                cur.wakeReasonTag = null;
            }
            cur.numReadInts++;
        } else {
            cur.wakelockTag = null;
            cur.wakeReasonTag = null;
        }
        if ((8388608 & firstToken) != 0) {
            cur.eventTag = cur.localEventTag;
            int codeAndIndex = src.readInt();
            cur.eventCode = codeAndIndex & 65535;
            int index = (codeAndIndex >> 16) & 65535;
            readHistoryTag(index, cur.eventTag);
            cur.numReadInts++;
        } else {
            cur.eventCode = 0;
        }
        if ((batteryLevelInt & 1) != 0) {
            cur.stepDetails = this.mReadHistoryStepDetails;
            cur.stepDetails.readFromParcel(src);
        } else {
            cur.stepDetails = null;
        }
        if ((16777216 & firstToken) != 0) {
            cur.batteryChargeUAh = src.readInt();
        }
        cur.modemRailChargeMah = src.readDouble();
        cur.wifiRailChargeMah = src.readDouble();
    }

    @Override // android.os.BatteryStats
    public void commitCurrentHistoryBatchLocked() {
        this.mHistoryLastWritten.cmd = (byte) -1;
    }

    public void createFakeHistoryEvents(long numEvents) {
        for (long i = 0; i < numEvents; i++) {
            noteLongPartialWakelockStart("name1", "historyName1", 1000);
            noteLongPartialWakelockFinish("name1", "historyName1", 1000);
        }
    }

    void addHistoryBufferLocked(long elapsedRealtimeMs, BatteryStats.HistoryItem cur) {
        long elapsedRealtimeMs2;
        if (this.mHaveBatteryLevel && this.mRecordingHistory) {
            long timeDiff = (this.mHistoryBaseTime + elapsedRealtimeMs) - this.mHistoryLastWritten.time;
            int diffStates = this.mHistoryLastWritten.states ^ (cur.states & this.mActiveHistoryStates);
            int diffStates2 = this.mHistoryLastWritten.states2 ^ (cur.states2 & this.mActiveHistoryStates2);
            int lastDiffStates = this.mHistoryLastWritten.states ^ this.mHistoryLastLastWritten.states;
            int lastDiffStates2 = this.mHistoryLastWritten.states2 ^ this.mHistoryLastLastWritten.states2;
            if (this.mHistoryBufferLastPos >= 0 && this.mHistoryLastWritten.cmd == 0 && timeDiff < 1000 && (diffStates & lastDiffStates) == 0 && (diffStates2 & lastDiffStates2) == 0 && ((this.mHistoryLastWritten.wakelockTag == null || cur.wakelockTag == null) && ((this.mHistoryLastWritten.wakeReasonTag == null || cur.wakeReasonTag == null) && this.mHistoryLastWritten.stepDetails == null && ((this.mHistoryLastWritten.eventCode == 0 || cur.eventCode == 0) && this.mHistoryLastWritten.batteryLevel == cur.batteryLevel && this.mHistoryLastWritten.batteryStatus == cur.batteryStatus && this.mHistoryLastWritten.batteryHealth == cur.batteryHealth && this.mHistoryLastWritten.batteryPlugType == cur.batteryPlugType && this.mHistoryLastWritten.batteryTemperature == cur.batteryTemperature && this.mHistoryLastWritten.batteryVoltage == cur.batteryVoltage)))) {
                this.mHistoryBuffer.setDataSize(this.mHistoryBufferLastPos);
                this.mHistoryBuffer.setDataPosition(this.mHistoryBufferLastPos);
                this.mHistoryBufferLastPos = -1;
                long elapsedRealtimeMs3 = this.mHistoryLastWritten.time - this.mHistoryBaseTime;
                if (this.mHistoryLastWritten.wakelockTag != null) {
                    cur.wakelockTag = cur.localWakelockTag;
                    cur.wakelockTag.setTo(this.mHistoryLastWritten.wakelockTag);
                }
                if (this.mHistoryLastWritten.wakeReasonTag != null) {
                    cur.wakeReasonTag = cur.localWakeReasonTag;
                    cur.wakeReasonTag.setTo(this.mHistoryLastWritten.wakeReasonTag);
                }
                if (this.mHistoryLastWritten.eventCode != 0) {
                    cur.eventCode = this.mHistoryLastWritten.eventCode;
                    cur.eventTag = cur.localEventTag;
                    cur.eventTag.setTo(this.mHistoryLastWritten.eventTag);
                }
                this.mHistoryLastWritten.setTo(this.mHistoryLastLastWritten);
                elapsedRealtimeMs2 = elapsedRealtimeMs3;
            } else {
                elapsedRealtimeMs2 = elapsedRealtimeMs;
            }
            int dataSize = this.mHistoryBuffer.dataSize();
            if (dataSize >= this.mConstants.MAX_HISTORY_BUFFER) {
                SystemClock.uptimeMillis();
                writeHistoryLocked(true);
                this.mBatteryStatsHistory.startNextFile();
                this.mHistoryBuffer.setDataSize(0);
                this.mHistoryBuffer.setDataPosition(0);
                this.mHistoryBuffer.setDataCapacity(this.mConstants.MAX_HISTORY_BUFFER / 2);
                this.mHistoryBufferLastPos = -1;
                long elapsedRealtime = this.mClocks.elapsedRealtime();
                long uptime = this.mClocks.uptimeMillis();
                BatteryStats.HistoryItem newItem = new BatteryStats.HistoryItem();
                newItem.setTo(cur);
                startRecordingHistory(elapsedRealtime, uptime, false);
                addHistoryBufferLocked(elapsedRealtimeMs2, (byte) 0, newItem);
                return;
            }
            if (dataSize == 0) {
                cur.currentTime = System.currentTimeMillis();
                addHistoryBufferLocked(elapsedRealtimeMs2, (byte) 7, cur);
            }
            addHistoryBufferLocked(elapsedRealtimeMs2, (byte) 0, cur);
        }
    }

    private void addHistoryBufferLocked(long elapsedRealtimeMs, byte cmd, BatteryStats.HistoryItem cur) {
        if (this.mIteratingHistory) {
            throw new IllegalStateException("Can't do this while iterating history!");
        }
        this.mHistoryBufferLastPos = this.mHistoryBuffer.dataPosition();
        this.mHistoryLastLastWritten.setTo(this.mHistoryLastWritten);
        this.mHistoryLastWritten.setTo(this.mHistoryBaseTime + elapsedRealtimeMs, cmd, cur);
        this.mHistoryLastWritten.states &= this.mActiveHistoryStates;
        this.mHistoryLastWritten.states2 &= this.mActiveHistoryStates2;
        writeHistoryDelta(this.mHistoryBuffer, this.mHistoryLastWritten, this.mHistoryLastLastWritten);
        this.mLastHistoryElapsedRealtime = elapsedRealtimeMs;
        cur.wakelockTag = null;
        cur.wakeReasonTag = null;
        cur.eventCode = 0;
        cur.eventTag = null;
    }

    void addHistoryRecordLocked(long elapsedRealtimeMs, long uptimeMs) {
        long j = this.mTrackRunningHistoryElapsedRealtime;
        if (j != 0) {
            long diffElapsed = elapsedRealtimeMs - j;
            long diffUptime = uptimeMs - this.mTrackRunningHistoryUptime;
            if (diffUptime < diffElapsed - 20) {
                long wakeElapsedTime = elapsedRealtimeMs - (diffElapsed - diffUptime);
                this.mHistoryAddTmp.setTo(this.mHistoryLastWritten);
                BatteryStats.HistoryItem historyItem = this.mHistoryAddTmp;
                historyItem.wakelockTag = null;
                historyItem.wakeReasonTag = null;
                historyItem.eventCode = 0;
                historyItem.states &= Integer.MAX_VALUE;
                addHistoryRecordInnerLocked(wakeElapsedTime, this.mHistoryAddTmp);
            }
        }
        this.mHistoryCur.states |= Integer.MIN_VALUE;
        this.mTrackRunningHistoryElapsedRealtime = elapsedRealtimeMs;
        this.mTrackRunningHistoryUptime = uptimeMs;
        addHistoryRecordInnerLocked(elapsedRealtimeMs, this.mHistoryCur);
    }

    void addHistoryRecordInnerLocked(long elapsedRealtimeMs, BatteryStats.HistoryItem cur) {
        addHistoryBufferLocked(elapsedRealtimeMs, cur);
    }

    public void addHistoryEventLocked(long elapsedRealtimeMs, long uptimeMs, int code, String name, int uid) {
        BatteryStats.HistoryItem historyItem = this.mHistoryCur;
        historyItem.eventCode = code;
        historyItem.eventTag = historyItem.localEventTag;
        this.mHistoryCur.eventTag.string = name;
        this.mHistoryCur.eventTag.uid = uid;
        addHistoryRecordLocked(elapsedRealtimeMs, uptimeMs);
    }

    void addHistoryRecordLocked(long elapsedRealtimeMs, long uptimeMs, byte cmd, BatteryStats.HistoryItem cur) {
        BatteryStats.HistoryItem rec = this.mHistoryCache;
        if (rec != null) {
            this.mHistoryCache = rec.next;
        } else {
            rec = new BatteryStats.HistoryItem();
        }
        rec.setTo(this.mHistoryBaseTime + elapsedRealtimeMs, cmd, cur);
        addHistoryRecordLocked(rec);
    }

    void addHistoryRecordLocked(BatteryStats.HistoryItem rec) {
        this.mNumHistoryItems++;
        rec.next = null;
        BatteryStats.HistoryItem historyItem = this.mHistoryEnd;
        this.mHistoryLastEnd = historyItem;
        if (historyItem != null) {
            historyItem.next = rec;
            this.mHistoryEnd = rec;
            return;
        }
        this.mHistoryEnd = rec;
        this.mHistory = rec;
    }

    void clearHistoryLocked() {
        this.mHistoryBaseTime = 0L;
        this.mLastHistoryElapsedRealtime = 0L;
        this.mTrackRunningHistoryElapsedRealtime = 0L;
        this.mTrackRunningHistoryUptime = 0L;
        this.mHistoryBuffer.setDataSize(0);
        this.mHistoryBuffer.setDataPosition(0);
        this.mHistoryBuffer.setDataCapacity(this.mConstants.MAX_HISTORY_BUFFER / 2);
        this.mHistoryLastLastWritten.clear();
        this.mHistoryLastWritten.clear();
        this.mHistoryTagPool.clear();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
    }

    @GuardedBy({"this"})
    public void updateTimeBasesLocked(boolean unplugged, int screenState, long uptime, long realtime) {
        boolean screenOff = !isScreenOn(screenState);
        boolean updateOnBatteryTimeBase = unplugged != this.mOnBatteryTimeBase.isRunning();
        boolean updateOnBatteryScreenOffTimeBase = (unplugged && screenOff) != this.mOnBatteryScreenOffTimeBase.isRunning();
        if (updateOnBatteryScreenOffTimeBase || updateOnBatteryTimeBase) {
            if (updateOnBatteryScreenOffTimeBase) {
                updateKernelWakelocksLocked();
                updateBatteryPropertiesLocked();
            }
            if (updateOnBatteryTimeBase) {
                updateRpmStatsLocked();
            }
            this.mOnBatteryTimeBase.setRunning(unplugged, uptime, realtime);
            if (updateOnBatteryTimeBase) {
                for (int i = this.mUidStats.size() - 1; i >= 0; i--) {
                    this.mUidStats.valueAt(i).updateOnBatteryBgTimeBase(uptime, realtime);
                }
            }
            if (updateOnBatteryScreenOffTimeBase) {
                this.mOnBatteryScreenOffTimeBase.setRunning(unplugged && screenOff, uptime, realtime);
                for (int i2 = this.mUidStats.size() - 1; i2 >= 0; i2--) {
                    this.mUidStats.valueAt(i2).updateOnBatteryScreenOffBgTimeBase(uptime, realtime);
                }
            }
        }
    }

    private void updateBatteryPropertiesLocked() {
        try {
            IBatteryPropertiesRegistrar registrar = IBatteryPropertiesRegistrar.Stub.asInterface(ServiceManager.getService("batteryproperties"));
            if (registrar != null) {
                registrar.scheduleUpdate();
            }
        } catch (RemoteException e) {
        }
    }

    public void addIsolatedUidLocked(int isolatedUid, int appUid) {
        this.mIsolatedUids.put(isolatedUid, appUid);
        Uid u = getUidStatsLocked(appUid);
        u.addIsolatedUid(isolatedUid);
    }

    public void scheduleRemoveIsolatedUidLocked(int isolatedUid, int appUid) {
        ExternalStatsSync externalStatsSync;
        int curUid = this.mIsolatedUids.get(isolatedUid, -1);
        if (curUid == appUid && (externalStatsSync = this.mExternalSync) != null) {
            externalStatsSync.scheduleCpuSyncDueToRemovedUid(isolatedUid);
        }
    }

    @GuardedBy({"this"})
    public void removeIsolatedUidLocked(int isolatedUid) {
        int idx = this.mIsolatedUids.indexOfKey(isolatedUid);
        if (idx >= 0) {
            int ownerUid = this.mIsolatedUids.valueAt(idx);
            Uid u = getUidStatsLocked(ownerUid);
            u.removeIsolatedUid(isolatedUid);
            this.mIsolatedUids.removeAt(idx);
        }
        this.mPendingRemovedUids.add(new UidToRemove(this, isolatedUid, this.mClocks.elapsedRealtime()));
    }

    public int mapUid(int uid) {
        int isolated = this.mIsolatedUids.get(uid, -1);
        return isolated > 0 ? isolated : uid;
    }

    public void noteEventLocked(int code, String name, int uid) {
        int uid2 = mapUid(uid);
        if (!this.mActiveEvents.updateState(code, name, uid2, 0)) {
            return;
        }
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        addHistoryEventLocked(elapsedRealtime, uptime, code, name, uid2);
    }

    public void noteCurrentTimeChangedLocked() {
        long currentTime = System.currentTimeMillis();
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        recordCurrentTimeChangeLocked(currentTime, elapsedRealtime, uptime);
    }

    public void noteProcessStartLocked(String name, int uid) {
        int uid2 = mapUid(uid);
        if (isOnBattery()) {
            Uid u = getUidStatsLocked(uid2);
            u.getProcessStatsLocked(name).incStartsLocked();
        }
        if (!this.mActiveEvents.updateState(32769, name, uid2, 0) || !this.mRecordAllHistory) {
            return;
        }
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        addHistoryEventLocked(elapsedRealtime, uptime, 32769, name, uid2);
    }

    public void noteProcessCrashLocked(String name, int uid) {
        int uid2 = mapUid(uid);
        if (isOnBattery()) {
            Uid u = getUidStatsLocked(uid2);
            u.getProcessStatsLocked(name).incNumCrashesLocked();
        }
    }

    public void noteProcessAnrLocked(String name, int uid) {
        int uid2 = mapUid(uid);
        if (isOnBattery()) {
            Uid u = getUidStatsLocked(uid2);
            u.getProcessStatsLocked(name).incNumAnrsLocked();
        }
    }

    public void noteUidProcessStateLocked(int uid, int state) {
        int parentUid = mapUid(uid);
        if (uid != parentUid) {
            return;
        }
        getUidStatsLocked(uid).updateUidProcessStateLocked(state);
    }

    public void noteProcessFinishLocked(String name, int uid) {
        int uid2 = mapUid(uid);
        if (!this.mActiveEvents.updateState(16385, name, uid2, 0) || !this.mRecordAllHistory) {
            return;
        }
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        addHistoryEventLocked(elapsedRealtime, uptime, 16385, name, uid2);
    }

    public void noteSyncStartLocked(String name, int uid) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        getUidStatsLocked(uid2).noteStartSyncLocked(name, elapsedRealtime);
        if (!this.mActiveEvents.updateState(32772, name, uid2, 0)) {
            return;
        }
        addHistoryEventLocked(elapsedRealtime, uptime, 32772, name, uid2);
    }

    public void noteSyncFinishLocked(String name, int uid) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        getUidStatsLocked(uid2).noteStopSyncLocked(name, elapsedRealtime);
        if (!this.mActiveEvents.updateState(16388, name, uid2, 0)) {
            return;
        }
        addHistoryEventLocked(elapsedRealtime, uptime, 16388, name, uid2);
    }

    public void noteJobStartLocked(String name, int uid) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        getUidStatsLocked(uid2).noteStartJobLocked(name, elapsedRealtime);
        if (!this.mActiveEvents.updateState(32774, name, uid2, 0)) {
            return;
        }
        addHistoryEventLocked(elapsedRealtime, uptime, 32774, name, uid2);
    }

    public void noteJobFinishLocked(String name, int uid, int stopReason) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        getUidStatsLocked(uid2).noteStopJobLocked(name, elapsedRealtime, stopReason);
        if (!this.mActiveEvents.updateState(16390, name, uid2, 0)) {
            return;
        }
        addHistoryEventLocked(elapsedRealtime, uptime, 16390, name, uid2);
    }

    public void noteJobsDeferredLocked(int uid, int numDeferred, long sinceLast) {
        getUidStatsLocked(mapUid(uid)).noteJobsDeferredLocked(numDeferred, sinceLast);
    }

    public void noteAlarmStartLocked(String name, WorkSource workSource, int uid) {
        noteAlarmStartOrFinishLocked(BatteryStats.HistoryItem.EVENT_ALARM_START, name, workSource, uid);
    }

    public void noteAlarmFinishLocked(String name, WorkSource workSource, int uid) {
        noteAlarmStartOrFinishLocked(16397, name, workSource, uid);
    }

    private void noteAlarmStartOrFinishLocked(int historyItem, String name, WorkSource workSource, int uid) {
        int uid2;
        int i;
        List<WorkSource.WorkChain> workChains;
        int uid3;
        int i2;
        int i3;
        WorkSource workSource2 = workSource;
        if (!this.mRecordAllHistory) {
            return;
        }
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i4 = 0;
        if (workSource2 == null) {
            int uid4 = mapUid(uid);
            if (this.mActiveEvents.updateState(historyItem, name, uid4, 0)) {
                addHistoryEventLocked(elapsedRealtime, uptime, historyItem, name, uid4);
                return;
            }
            return;
        }
        int i5 = 0;
        int uid5 = uid;
        while (i5 < workSource.size()) {
            int uid6 = mapUid(workSource2.get(i5));
            if (!this.mActiveEvents.updateState(historyItem, name, uid6, i4)) {
                uid3 = uid6;
                i2 = i5;
                i3 = i4;
            } else {
                uid3 = uid6;
                i2 = i5;
                i3 = i4;
                addHistoryEventLocked(elapsedRealtime, uptime, historyItem, name, uid3);
            }
            i5 = i2 + 1;
            i4 = i3;
            uid5 = uid3;
            workSource2 = workSource;
        }
        int i6 = i4;
        List<WorkSource.WorkChain> workChains2 = workSource.getWorkChains();
        if (workChains2 != null) {
            int i7 = 0;
            while (i7 < workChains2.size()) {
                int uid7 = mapUid(workChains2.get(i7).getAttributionUid());
                if (!this.mActiveEvents.updateState(historyItem, name, uid7, i6)) {
                    uid2 = uid7;
                    i = i7;
                    workChains = workChains2;
                } else {
                    uid2 = uid7;
                    i = i7;
                    workChains = workChains2;
                    addHistoryEventLocked(elapsedRealtime, uptime, historyItem, name, uid2);
                }
                i7 = i + 1;
                uid5 = uid2;
                workChains2 = workChains;
            }
        }
    }

    public void noteWakupAlarmLocked(String packageName, int uid, WorkSource workSource, String tag) {
        if (workSource != null) {
            for (int i = 0; i < workSource.size(); i++) {
                int uid2 = workSource.get(i);
                String workSourceName = workSource.getName(i);
                if (isOnBattery()) {
                    Uid.Pkg pkg = getPackageStatsLocked(uid2, workSourceName != null ? workSourceName : packageName);
                    pkg.noteWakeupAlarmLocked(tag);
                }
            }
            ArrayList<WorkSource.WorkChain> workChains = workSource.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    WorkSource.WorkChain wc = workChains.get(i2);
                    int uid3 = wc.getAttributionUid();
                    if (isOnBattery()) {
                        Uid.Pkg pkg2 = getPackageStatsLocked(uid3, packageName);
                        pkg2.noteWakeupAlarmLocked(tag);
                    }
                }
            }
        } else if (isOnBattery()) {
            Uid.Pkg pkg3 = getPackageStatsLocked(uid, packageName);
            pkg3.noteWakeupAlarmLocked(tag);
        }
    }

    private void requestWakelockCpuUpdate() {
        this.mExternalSync.scheduleCpuSyncDueToWakelockChange(5000L);
    }

    private void requestImmediateCpuUpdate() {
        this.mExternalSync.scheduleCpuSyncDueToWakelockChange(0L);
    }

    public void setRecordAllHistoryLocked(boolean enabled) {
        this.mRecordAllHistory = enabled;
        if (enabled) {
            HashMap<String, SparseIntArray> active = this.mActiveEvents.getStateForEvent(1);
            if (active != null) {
                long mSecRealtime = this.mClocks.elapsedRealtime();
                long mSecUptime = this.mClocks.uptimeMillis();
                for (Map.Entry<String, SparseIntArray> ent : active.entrySet()) {
                    int j = 0;
                    for (SparseIntArray uids = ent.getValue(); j < uids.size(); uids = uids) {
                        addHistoryEventLocked(mSecRealtime, mSecUptime, 32769, ent.getKey(), uids.keyAt(j));
                        j++;
                    }
                }
                return;
            }
            return;
        }
        this.mActiveEvents.removeEvents(5);
        this.mActiveEvents.removeEvents(13);
        HashMap<String, SparseIntArray> active2 = this.mActiveEvents.getStateForEvent(1);
        if (active2 != null) {
            long mSecRealtime2 = this.mClocks.elapsedRealtime();
            long mSecUptime2 = this.mClocks.uptimeMillis();
            for (Map.Entry<String, SparseIntArray> ent2 : active2.entrySet()) {
                int j2 = 0;
                for (SparseIntArray uids2 = ent2.getValue(); j2 < uids2.size(); uids2 = uids2) {
                    addHistoryEventLocked(mSecRealtime2, mSecUptime2, 16385, ent2.getKey(), uids2.keyAt(j2));
                    j2++;
                }
            }
        }
    }

    public void setNoAutoReset(boolean enabled) {
        this.mNoAutoReset = enabled;
    }

    public void setPretendScreenOff(boolean pretendScreenOff) {
        if (this.mPretendScreenOff != pretendScreenOff) {
            this.mPretendScreenOff = pretendScreenOff;
            noteScreenStateLocked(pretendScreenOff ? 1 : 2);
        }
    }

    public void noteStartWakeLocked(int uid, int pid, WorkSource.WorkChain wc, String name, String historyName, int type, boolean unimportantForLogging, long elapsedRealtime, long uptime) {
        String historyName2;
        int uid2 = mapUid(uid);
        if (type == 0) {
            aggregateLastWakeupUptimeLocked(uptime);
            if (historyName != null) {
                historyName2 = historyName;
            } else {
                historyName2 = name;
            }
            if (this.mRecordAllHistory && this.mActiveEvents.updateState(32773, historyName2, uid2, 0)) {
                addHistoryEventLocked(elapsedRealtime, uptime, 32773, historyName2, uid2);
            }
            if (this.mWakeLockNesting == 0) {
                this.mHistoryCur.states |= 1073741824;
                BatteryStats.HistoryItem historyItem = this.mHistoryCur;
                historyItem.wakelockTag = historyItem.localWakelockTag;
                BatteryStats.HistoryTag historyTag = this.mHistoryCur.wakelockTag;
                this.mInitialAcquireWakeName = historyName2;
                historyTag.string = historyName2;
                BatteryStats.HistoryTag historyTag2 = this.mHistoryCur.wakelockTag;
                this.mInitialAcquireWakeUid = uid2;
                historyTag2.uid = uid2;
                this.mWakeLockImportant = !unimportantForLogging;
                addHistoryRecordLocked(elapsedRealtime, uptime);
            } else if (!this.mWakeLockImportant && !unimportantForLogging && this.mHistoryLastWritten.cmd == 0) {
                if (this.mHistoryLastWritten.wakelockTag != null) {
                    this.mHistoryLastWritten.wakelockTag = null;
                    BatteryStats.HistoryItem historyItem2 = this.mHistoryCur;
                    historyItem2.wakelockTag = historyItem2.localWakelockTag;
                    BatteryStats.HistoryTag historyTag3 = this.mHistoryCur.wakelockTag;
                    this.mInitialAcquireWakeName = historyName2;
                    historyTag3.string = historyName2;
                    BatteryStats.HistoryTag historyTag4 = this.mHistoryCur.wakelockTag;
                    this.mInitialAcquireWakeUid = uid2;
                    historyTag4.uid = uid2;
                    addHistoryRecordLocked(elapsedRealtime, uptime);
                }
                this.mWakeLockImportant = true;
            }
            this.mWakeLockNesting++;
        }
        if (uid2 >= 0) {
            if (this.mOnBatteryScreenOffTimeBase.isRunning()) {
                requestWakelockCpuUpdate();
            }
            getUidStatsLocked(uid2).noteStartWakeLocked(pid, name, type, elapsedRealtime);
            if (wc != null) {
                StatsLog.write(10, wc.getUids(), wc.getTags(), getPowerManagerWakeLockLevel(type), name, 1);
            } else {
                StatsLog.write_non_chained(10, uid2, (String) null, getPowerManagerWakeLockLevel(type), name, 1);
            }
        }
    }

    public void noteStopWakeLocked(int uid, int pid, WorkSource.WorkChain wc, String name, String historyName, int type, long elapsedRealtime, long uptime) {
        String historyName2;
        int uid2 = mapUid(uid);
        if (type == 0) {
            this.mWakeLockNesting--;
            if (this.mRecordAllHistory) {
                if (historyName != null) {
                    historyName2 = historyName;
                } else {
                    historyName2 = name;
                }
                if (this.mActiveEvents.updateState(16389, historyName2, uid2, 0)) {
                    addHistoryEventLocked(elapsedRealtime, uptime, 16389, historyName2, uid2);
                }
            }
            if (this.mWakeLockNesting == 0) {
                this.mHistoryCur.states &= -1073741825;
                this.mInitialAcquireWakeName = null;
                this.mInitialAcquireWakeUid = -1;
                addHistoryRecordLocked(elapsedRealtime, uptime);
            }
        }
        if (uid2 >= 0) {
            if (this.mOnBatteryScreenOffTimeBase.isRunning()) {
                requestWakelockCpuUpdate();
            }
            getUidStatsLocked(uid2).noteStopWakeLocked(pid, name, type, elapsedRealtime);
            if (wc != null) {
                StatsLog.write(10, wc.getUids(), wc.getTags(), getPowerManagerWakeLockLevel(type), name, 0);
            } else {
                StatsLog.write_non_chained(10, uid2, (String) null, getPowerManagerWakeLockLevel(type), name, 0);
            }
        }
    }

    private int getPowerManagerWakeLockLevel(int battertStatsWakelockType) {
        if (battertStatsWakelockType != 0) {
            if (battertStatsWakelockType != 1) {
                if (battertStatsWakelockType == 2) {
                    Slog.e(TAG, "Illegal window wakelock type observed in batterystats.");
                    return -1;
                } else if (battertStatsWakelockType == 18) {
                    return 128;
                } else {
                    Slog.e(TAG, "Illegal wakelock type in batterystats: " + battertStatsWakelockType);
                    return -1;
                }
            }
            return 26;
        }
        return 1;
    }

    public void noteStartWakeFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type, boolean unimportantForLogging) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = 0;
        for (int N = ws.size(); i < N; N = N) {
            noteStartWakeLocked(ws.get(i), pid, null, name, historyName, type, unimportantForLogging, elapsedRealtime, uptime);
            i++;
        }
        List<WorkSource.WorkChain> wcs = ws.getWorkChains();
        if (wcs != null) {
            int i2 = 0;
            while (i2 < wcs.size()) {
                WorkSource.WorkChain wc = wcs.get(i2);
                noteStartWakeLocked(wc.getAttributionUid(), pid, wc, name, historyName, type, unimportantForLogging, elapsedRealtime, uptime);
                i2++;
                wcs = wcs;
            }
        }
    }

    public void noteChangeWakelockFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type, WorkSource newWs, int newPid, String newName, String newHistoryName, int newType, boolean newUnimportantForLogging) {
        List<WorkSource.WorkChain> goneChains;
        List<WorkSource.WorkChain> newChains;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        List<WorkSource.WorkChain>[] diffChains = WorkSource.diffChains(ws, newWs);
        int i = 0;
        for (int NN = newWs.size(); i < NN; NN = NN) {
            noteStartWakeLocked(newWs.get(i), newPid, null, newName, newHistoryName, newType, newUnimportantForLogging, elapsedRealtime, uptime);
            i++;
        }
        if (diffChains != null && (newChains = diffChains[0]) != null) {
            int i2 = 0;
            for (newChains = diffChains[0]; i2 < newChains.size(); newChains = newChains) {
                WorkSource.WorkChain newChain = newChains.get(i2);
                noteStartWakeLocked(newChain.getAttributionUid(), newPid, newChain, newName, newHistoryName, newType, newUnimportantForLogging, elapsedRealtime, uptime);
                i2++;
            }
        }
        int NO = ws.size();
        for (int i3 = 0; i3 < NO; i3++) {
            noteStopWakeLocked(ws.get(i3), pid, null, name, historyName, type, elapsedRealtime, uptime);
        }
        if (diffChains != null && (goneChains = diffChains[1]) != null) {
            int i4 = 0;
            for (goneChains = diffChains[1]; i4 < goneChains.size(); goneChains = goneChains) {
                WorkSource.WorkChain goneChain = goneChains.get(i4);
                noteStopWakeLocked(goneChain.getAttributionUid(), pid, goneChain, name, historyName, type, elapsedRealtime, uptime);
                i4++;
            }
        }
    }

    public void noteStopWakeFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = 0;
        for (int N = ws.size(); i < N; N = N) {
            noteStopWakeLocked(ws.get(i), pid, null, name, historyName, type, elapsedRealtime, uptime);
            i++;
        }
        List<WorkSource.WorkChain> wcs = ws.getWorkChains();
        if (wcs != null) {
            int i2 = 0;
            while (i2 < wcs.size()) {
                WorkSource.WorkChain wc = wcs.get(i2);
                noteStopWakeLocked(wc.getAttributionUid(), pid, wc, name, historyName, type, elapsedRealtime, uptime);
                i2++;
                wcs = wcs;
            }
        }
    }

    public void noteLongPartialWakelockStart(String name, String historyName, int uid) {
        noteLongPartialWakeLockStartInternal(name, historyName, mapUid(uid));
    }

    public void noteLongPartialWakelockStartFromSource(String name, String historyName, WorkSource workSource) {
        int N = workSource.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(workSource.get(i));
            noteLongPartialWakeLockStartInternal(name, historyName, uid);
        }
        ArrayList<WorkSource.WorkChain> workChains = workSource.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = workChain.getAttributionUid();
                noteLongPartialWakeLockStartInternal(name, historyName, uid2);
            }
        }
    }

    private void noteLongPartialWakeLockStartInternal(String name, String historyName, int uid) {
        String historyName2;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (historyName != null) {
            historyName2 = historyName;
        } else {
            historyName2 = name;
        }
        if (!this.mActiveEvents.updateState(BatteryStats.HistoryItem.EVENT_LONG_WAKE_LOCK_START, historyName2, uid, 0)) {
            return;
        }
        addHistoryEventLocked(elapsedRealtime, uptime, BatteryStats.HistoryItem.EVENT_LONG_WAKE_LOCK_START, historyName2, uid);
    }

    public void noteLongPartialWakelockFinish(String name, String historyName, int uid) {
        noteLongPartialWakeLockFinishInternal(name, historyName, mapUid(uid));
    }

    public void noteLongPartialWakelockFinishFromSource(String name, String historyName, WorkSource workSource) {
        int N = workSource.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(workSource.get(i));
            noteLongPartialWakeLockFinishInternal(name, historyName, uid);
        }
        ArrayList<WorkSource.WorkChain> workChains = workSource.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = workChain.getAttributionUid();
                noteLongPartialWakeLockFinishInternal(name, historyName, uid2);
            }
        }
    }

    private void noteLongPartialWakeLockFinishInternal(String name, String historyName, int uid) {
        String historyName2;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (historyName != null) {
            historyName2 = historyName;
        } else {
            historyName2 = name;
        }
        if (!this.mActiveEvents.updateState(BatteryStats.HistoryItem.EVENT_LONG_WAKE_LOCK_FINISH, historyName2, uid, 0)) {
            return;
        }
        addHistoryEventLocked(elapsedRealtime, uptime, BatteryStats.HistoryItem.EVENT_LONG_WAKE_LOCK_FINISH, historyName2, uid);
    }

    void aggregateLastWakeupUptimeLocked(long uptimeMs) {
        String str = this.mLastWakeupReason;
        if (str != null) {
            long deltaUptime = uptimeMs - this.mLastWakeupUptimeMs;
            SamplingTimer timer = getWakeupReasonTimerLocked(str);
            timer.add(deltaUptime * 1000, 1);
            StatsLog.write(36, this.mLastWakeupReason, 1000 * deltaUptime);
            this.mLastWakeupReason = null;
        }
    }

    public void noteWakeupReasonLocked(String reason) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        aggregateLastWakeupUptimeLocked(uptime);
        BatteryStats.HistoryItem historyItem = this.mHistoryCur;
        historyItem.wakeReasonTag = historyItem.localWakeReasonTag;
        this.mHistoryCur.wakeReasonTag.string = reason;
        this.mHistoryCur.wakeReasonTag.uid = 0;
        this.mLastWakeupReason = reason;
        this.mLastWakeupUptimeMs = uptime;
        addHistoryRecordLocked(elapsedRealtime, uptime);
    }

    public boolean startAddingCpuLocked() {
        this.mExternalSync.cancelCpuSyncDueToWakelockChange();
        return this.mOnBatteryInternal;
    }

    public void finishAddingCpuLocked(int totalUTime, int totalSTime, int statUserTime, int statSystemTime, int statIOWaitTime, int statIrqTime, int statSoftIrqTime, int statIdleTime) {
        this.mCurStepCpuUserTime += totalUTime;
        this.mCurStepCpuSystemTime += totalSTime;
        this.mCurStepStatUserTime += statUserTime;
        this.mCurStepStatSystemTime += statSystemTime;
        this.mCurStepStatIOWaitTime += statIOWaitTime;
        this.mCurStepStatIrqTime += statIrqTime;
        this.mCurStepStatSoftIrqTime += statSoftIrqTime;
        this.mCurStepStatIdleTime += statIdleTime;
    }

    public void noteProcessDiedLocked(int uid, int pid) {
        Uid u = this.mUidStats.get(mapUid(uid));
        if (u != null) {
            u.mPids.remove(pid);
        }
    }

    public long getProcessWakeTime(int uid, int pid, long realtime) {
        BatteryStats.Uid.Pid p;
        Uid u = this.mUidStats.get(mapUid(uid));
        if (u == null || (p = u.mPids.get(pid)) == null) {
            return 0L;
        }
        return p.mWakeSumMs + (p.mWakeNesting > 0 ? realtime - p.mWakeStartMs : 0L);
    }

    public void reportExcessiveCpuLocked(int uid, String proc, long overTime, long usedTime) {
        Uid u = this.mUidStats.get(mapUid(uid));
        if (u != null) {
            u.reportExcessiveCpuLocked(proc, overTime, usedTime);
        }
    }

    public void noteStartSensorLocked(int uid, int sensor) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mSensorNesting == 0) {
            this.mHistoryCur.states |= 8388608;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mSensorNesting++;
        getUidStatsLocked(uid2).noteStartSensor(sensor, elapsedRealtime);
    }

    public void noteStopSensorLocked(int uid, int sensor) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mSensorNesting--;
        if (this.mSensorNesting == 0) {
            this.mHistoryCur.states &= -8388609;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid2).noteStopSensor(sensor, elapsedRealtime);
    }

    public void noteGpsChangedLocked(WorkSource oldWs, WorkSource newWs) {
        for (int i = 0; i < newWs.size(); i++) {
            noteStartGpsLocked(newWs.get(i), null);
        }
        for (int i2 = 0; i2 < oldWs.size(); i2++) {
            noteStopGpsLocked(oldWs.get(i2), null);
        }
        List<WorkSource.WorkChain>[] wcs = WorkSource.diffChains(oldWs, newWs);
        if (wcs != null) {
            if (wcs[0] != null) {
                List<WorkSource.WorkChain> newChains = wcs[0];
                for (int i3 = 0; i3 < newChains.size(); i3++) {
                    noteStartGpsLocked(-1, newChains.get(i3));
                }
            }
            if (wcs[1] != null) {
                List<WorkSource.WorkChain> goneChains = wcs[1];
                for (int i4 = 0; i4 < goneChains.size(); i4++) {
                    noteStopGpsLocked(-1, goneChains.get(i4));
                }
            }
        }
    }

    private void noteStartGpsLocked(int uid, WorkSource.WorkChain workChain) {
        int uid2 = getAttributionUid(uid, workChain);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mGpsNesting == 0) {
            this.mHistoryCur.states |= 536870912;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mGpsNesting++;
        if (workChain == null) {
            StatsLog.write_non_chained(6, uid2, null, 1);
        } else {
            StatsLog.write(6, workChain.getUids(), workChain.getTags(), 1);
        }
        getUidStatsLocked(uid2).noteStartGps(elapsedRealtime);
    }

    private void noteStopGpsLocked(int uid, WorkSource.WorkChain workChain) {
        int uid2 = getAttributionUid(uid, workChain);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mGpsNesting--;
        if (this.mGpsNesting == 0) {
            this.mHistoryCur.states &= -536870913;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            stopAllGpsSignalQualityTimersLocked(-1);
            this.mGpsSignalQualityBin = -1;
        }
        if (workChain == null) {
            StatsLog.write_non_chained(6, uid2, null, 0);
        } else {
            StatsLog.write(6, workChain.getUids(), workChain.getTags(), 0);
        }
        getUidStatsLocked(uid2).noteStopGps(elapsedRealtime);
    }

    public void noteGpsSignalQualityLocked(int signalLevel) {
        if (this.mGpsNesting == 0) {
            return;
        }
        if (signalLevel < 0 || signalLevel >= 2) {
            stopAllGpsSignalQualityTimersLocked(-1);
            return;
        }
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = this.mGpsSignalQualityBin;
        if (i != signalLevel) {
            if (i >= 0) {
                this.mGpsSignalQualityTimer[i].stopRunningLocked(elapsedRealtime);
            }
            if (!this.mGpsSignalQualityTimer[signalLevel].isRunningLocked()) {
                this.mGpsSignalQualityTimer[signalLevel].startRunningLocked(elapsedRealtime);
            }
            BatteryStats.HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 = (historyItem.states2 & (-129)) | (signalLevel << 7);
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mGpsSignalQualityBin = signalLevel;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    @com.android.internal.annotations.GuardedBy({"this"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void noteScreenStateLocked(int r23) {
        /*
            Method dump skipped, instructions count: 357
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.noteScreenStateLocked(int):void");
    }

    @UnsupportedAppUsage
    public void noteScreenBrightnessLocked(int brightness) {
        int bin = brightness / 51;
        if (bin < 0) {
            bin = 0;
        } else if (bin >= 5) {
            bin = 4;
        }
        if (this.mScreenBrightnessBin != bin) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            BatteryStats.HistoryItem historyItem = this.mHistoryCur;
            historyItem.states = (historyItem.states & (-8)) | (bin << 0);
            addHistoryRecordLocked(elapsedRealtime, uptime);
            if (this.mScreenState == 2) {
                int i = this.mScreenBrightnessBin;
                if (i >= 0) {
                    this.mScreenBrightnessTimer[i].stopRunningLocked(elapsedRealtime);
                }
                this.mScreenBrightnessTimer[bin].startRunningLocked(elapsedRealtime);
            }
            this.mScreenBrightnessBin = bin;
        }
    }

    @UnsupportedAppUsage
    public void noteUserActivityLocked(int uid, int event) {
        if (this.mOnBatteryInternal) {
            getUidStatsLocked(mapUid(uid)).noteUserActivityLocked(event);
        }
    }

    public void noteWakeUpLocked(String reason, int reasonUid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        addHistoryEventLocked(elapsedRealtime, uptime, 18, reason, reasonUid);
    }

    public void noteInteractiveLocked(boolean interactive) {
        if (this.mInteractive != interactive) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            this.mInteractive = interactive;
            if (interactive) {
                this.mInteractiveTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mInteractiveTimer.stopRunningLocked(elapsedRealtime);
            }
        }
    }

    public void noteConnectivityChangedLocked(int type, String extra) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        addHistoryEventLocked(elapsedRealtime, uptime, 9, extra, type);
        this.mNumConnectivityChange++;
    }

    private void noteMobileRadioApWakeupLocked(long elapsedRealtimeMillis, long uptimeMillis, int uid) {
        int uid2 = mapUid(uid);
        addHistoryEventLocked(elapsedRealtimeMillis, uptimeMillis, 19, "", uid2);
        getUidStatsLocked(uid2).noteMobileRadioApWakeupLocked();
    }

    public boolean noteMobileRadioPowerStateLocked(int powerState, long timestampNs, int uid) {
        long realElapsedRealtimeMs;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mMobileRadioPowerState != powerState) {
            boolean active = powerState == 2 || powerState == 3;
            if (active) {
                if (uid > 0) {
                    noteMobileRadioApWakeupLocked(elapsedRealtime, uptime, uid);
                }
                long realElapsedRealtimeMs2 = timestampNs / TimeUtils.NANOS_PER_MS;
                this.mMobileRadioActiveStartTime = realElapsedRealtimeMs2;
                this.mHistoryCur.states |= 33554432;
                realElapsedRealtimeMs = realElapsedRealtimeMs2;
            } else {
                realElapsedRealtimeMs = timestampNs / TimeUtils.NANOS_PER_MS;
                long lastUpdateTimeMs = this.mMobileRadioActiveStartTime;
                if (realElapsedRealtimeMs < lastUpdateTimeMs) {
                    Slog.wtf(TAG, "Data connection inactive timestamp " + realElapsedRealtimeMs + " is before start time " + lastUpdateTimeMs);
                    realElapsedRealtimeMs = elapsedRealtime;
                } else if (realElapsedRealtimeMs < elapsedRealtime) {
                    this.mMobileRadioActiveAdjustedTime.addCountLocked(elapsedRealtime - realElapsedRealtimeMs);
                }
                this.mHistoryCur.states &= -33554433;
            }
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mMobileRadioPowerState = powerState;
            if (active) {
                this.mMobileRadioActiveTimer.startRunningLocked(elapsedRealtime);
                this.mMobileRadioActivePerAppTimer.startRunningLocked(elapsedRealtime);
                return false;
            }
            this.mMobileRadioActiveTimer.stopRunningLocked(realElapsedRealtimeMs);
            this.mMobileRadioActivePerAppTimer.stopRunningLocked(realElapsedRealtimeMs);
            return true;
        }
        return false;
    }

    public void notePowerSaveModeLocked(boolean enabled) {
        if (this.mPowerSaveModeEnabled != enabled) {
            int i = 0;
            int stepState = enabled ? 4 : 0;
            int i2 = this.mModStepMode;
            int i3 = this.mCurStepMode;
            this.mModStepMode = i2 | ((i3 & 4) ^ stepState);
            this.mCurStepMode = (i3 & (-5)) | stepState;
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mPowerSaveModeEnabled = enabled;
            if (enabled) {
                this.mHistoryCur.states2 |= Integer.MIN_VALUE;
                this.mPowerSaveModeEnabledTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mHistoryCur.states2 &= Integer.MAX_VALUE;
                this.mPowerSaveModeEnabledTimer.stopRunningLocked(elapsedRealtime);
            }
            addHistoryRecordLocked(elapsedRealtime, uptime);
            if (enabled) {
                i = 1;
            }
            StatsLog.write(20, i);
        }
    }

    public void noteDeviceIdleModeLocked(int mode, String activeReason, int activeUid) {
        boolean nowIdling;
        boolean nowLightIdling;
        boolean nowLightIdling2;
        boolean nowIdling2;
        int statsmode;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        boolean nowIdling3 = mode == 2;
        if (this.mDeviceIdling && !nowIdling3 && activeReason == null) {
            nowIdling = true;
        } else {
            nowIdling = nowIdling3;
        }
        boolean nowLightIdling3 = mode == 1;
        if (this.mDeviceLightIdling && !nowLightIdling3 && !nowIdling && activeReason == null) {
            nowLightIdling = true;
        } else {
            nowLightIdling = nowLightIdling3;
        }
        if (activeReason != null) {
            if (this.mDeviceIdling || this.mDeviceLightIdling) {
                nowLightIdling2 = nowLightIdling;
                nowIdling2 = nowIdling;
                addHistoryEventLocked(elapsedRealtime, uptime, 10, activeReason, activeUid);
            } else {
                nowLightIdling2 = nowLightIdling;
                nowIdling2 = nowIdling;
            }
        } else {
            nowLightIdling2 = nowLightIdling;
            nowIdling2 = nowIdling;
        }
        if (this.mDeviceIdling != nowIdling2 || this.mDeviceLightIdling != nowLightIdling2) {
            if (nowIdling2) {
                statsmode = 2;
            } else {
                statsmode = nowLightIdling2 ? 1 : 0;
            }
            StatsLog.write(22, statsmode);
        }
        if (this.mDeviceIdling != nowIdling2) {
            this.mDeviceIdling = nowIdling2;
            int stepState = nowIdling2 ? 8 : 0;
            int i = this.mModStepMode;
            int i2 = this.mCurStepMode;
            this.mModStepMode = i | ((i2 & 8) ^ stepState);
            this.mCurStepMode = (i2 & (-9)) | stepState;
            if (nowIdling2) {
                this.mDeviceIdlingTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mDeviceIdlingTimer.stopRunningLocked(elapsedRealtime);
            }
        }
        if (this.mDeviceLightIdling != nowLightIdling2) {
            this.mDeviceLightIdling = nowLightIdling2;
            if (nowLightIdling2) {
                this.mDeviceLightIdlingTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mDeviceLightIdlingTimer.stopRunningLocked(elapsedRealtime);
            }
        }
        if (this.mDeviceIdleMode != mode) {
            BatteryStats.HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 = (historyItem.states2 & (-100663297)) | (mode << 25);
            addHistoryRecordLocked(elapsedRealtime, uptime);
            long lastDuration = elapsedRealtime - this.mLastIdleTimeStart;
            this.mLastIdleTimeStart = elapsedRealtime;
            int i3 = this.mDeviceIdleMode;
            if (i3 == 1) {
                if (lastDuration > this.mLongestLightIdleTime) {
                    this.mLongestLightIdleTime = lastDuration;
                }
                this.mDeviceIdleModeLightTimer.stopRunningLocked(elapsedRealtime);
            } else if (i3 == 2) {
                if (lastDuration > this.mLongestFullIdleTime) {
                    this.mLongestFullIdleTime = lastDuration;
                }
                this.mDeviceIdleModeFullTimer.stopRunningLocked(elapsedRealtime);
            }
            if (mode == 1) {
                this.mDeviceIdleModeLightTimer.startRunningLocked(elapsedRealtime);
            } else if (mode == 2) {
                this.mDeviceIdleModeFullTimer.startRunningLocked(elapsedRealtime);
            }
            this.mDeviceIdleMode = mode;
            StatsLog.write(21, mode);
        }
    }

    public void notePackageInstalledLocked(String pkgName, long versionCode) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        addHistoryEventLocked(elapsedRealtime, uptime, 11, pkgName, (int) versionCode);
        BatteryStats.PackageChange pc = new BatteryStats.PackageChange();
        pc.mPackageName = pkgName;
        pc.mUpdate = true;
        pc.mVersionCode = versionCode;
        addPackageChange(pc);
    }

    public void notePackageUninstalledLocked(String pkgName) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        addHistoryEventLocked(elapsedRealtime, uptime, 12, pkgName, 0);
        BatteryStats.PackageChange pc = new BatteryStats.PackageChange();
        pc.mPackageName = pkgName;
        pc.mUpdate = true;
        addPackageChange(pc);
    }

    private void addPackageChange(BatteryStats.PackageChange pc) {
        if (this.mDailyPackageChanges == null) {
            this.mDailyPackageChanges = new ArrayList<>();
        }
        this.mDailyPackageChanges.add(pc);
    }

    void stopAllGpsSignalQualityTimersLocked(int except) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        for (int i = 0; i < 2; i++) {
            if (i != except) {
                while (this.mGpsSignalQualityTimer[i].isRunningLocked()) {
                    this.mGpsSignalQualityTimer[i].stopRunningLocked(elapsedRealtime);
                }
            }
        }
    }

    @UnsupportedAppUsage
    public void notePhoneOnLocked() {
        if (!this.mPhoneOn) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mHistoryCur.states2 |= 8388608;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mPhoneOn = true;
            this.mPhoneOnTimer.startRunningLocked(elapsedRealtime);
        }
    }

    @UnsupportedAppUsage
    public void notePhoneOffLocked() {
        if (this.mPhoneOn) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mHistoryCur.states2 &= -8388609;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mPhoneOn = false;
            this.mPhoneOnTimer.stopRunningLocked(elapsedRealtime);
        }
    }

    private void registerUsbStateReceiver(Context context) {
        IntentFilter usbStateFilter = new IntentFilter();
        usbStateFilter.addAction(UsbManager.ACTION_USB_STATE);
        context.registerReceiver(new BroadcastReceiver() { // from class: com.android.internal.os.BatteryStatsImpl.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                boolean state = intent.getBooleanExtra("connected", false);
                synchronized (BatteryStatsImpl.this) {
                    BatteryStatsImpl.this.noteUsbConnectionStateLocked(state);
                }
            }
        }, usbStateFilter);
        synchronized (this) {
            if (this.mUsbDataState == 0) {
                Intent usbState = context.registerReceiver(null, usbStateFilter);
                boolean initState = false;
                if (usbState != null && usbState.getBooleanExtra("connected", false)) {
                    initState = true;
                }
                noteUsbConnectionStateLocked(initState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void noteUsbConnectionStateLocked(boolean connected) {
        int newState = connected ? 2 : 1;
        if (this.mUsbDataState != newState) {
            this.mUsbDataState = newState;
            if (connected) {
                this.mHistoryCur.states2 |= 262144;
            } else {
                this.mHistoryCur.states2 &= -262145;
            }
            addHistoryRecordLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis());
        }
    }

    void stopAllPhoneSignalStrengthTimersLocked(int except) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        for (int i = 0; i < 5; i++) {
            if (i != except) {
                while (this.mPhoneSignalStrengthsTimer[i].isRunningLocked()) {
                    this.mPhoneSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtime);
                }
            }
        }
    }

    private int fixPhoneServiceState(int state, int signalBin) {
        if (this.mPhoneSimStateRaw == 1 && state == 1 && signalBin > 0) {
            return 0;
        }
        return state;
    }

    private void updateAllPhoneStateLocked(int state, int simState, int strengthBin) {
        boolean scanning = false;
        boolean newHistory = false;
        this.mPhoneServiceStateRaw = state;
        this.mPhoneSimStateRaw = simState;
        this.mPhoneSignalStrengthBinRaw = strengthBin;
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (simState == 1 && state == 1 && strengthBin > 0) {
            state = 0;
        }
        if (state == 3) {
            strengthBin = -1;
        } else if (state != 0 && state == 1) {
            scanning = true;
            strengthBin = 0;
            if (!this.mPhoneSignalScanningTimer.isRunningLocked()) {
                this.mHistoryCur.states |= 2097152;
                newHistory = true;
                this.mPhoneSignalScanningTimer.startRunningLocked(elapsedRealtime);
                StatsLog.write(94, state, simState, 0);
            }
        }
        if (!scanning && this.mPhoneSignalScanningTimer.isRunningLocked()) {
            this.mHistoryCur.states &= -2097153;
            newHistory = true;
            this.mPhoneSignalScanningTimer.stopRunningLocked(elapsedRealtime);
            StatsLog.write(94, state, simState, strengthBin);
        }
        if (this.mPhoneServiceState != state) {
            BatteryStats.HistoryItem historyItem = this.mHistoryCur;
            historyItem.states = (historyItem.states & (-449)) | (state << 6);
            newHistory = true;
            this.mPhoneServiceState = state;
        }
        int i = this.mPhoneSignalStrengthBin;
        if (i != strengthBin) {
            if (i >= 0) {
                this.mPhoneSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtime);
            }
            if (strengthBin >= 0) {
                if (!this.mPhoneSignalStrengthsTimer[strengthBin].isRunningLocked()) {
                    this.mPhoneSignalStrengthsTimer[strengthBin].startRunningLocked(elapsedRealtime);
                }
                BatteryStats.HistoryItem historyItem2 = this.mHistoryCur;
                historyItem2.states = (historyItem2.states & (-57)) | (strengthBin << 3);
                newHistory = true;
                StatsLog.write(40, strengthBin);
            } else {
                stopAllPhoneSignalStrengthTimersLocked(-1);
            }
            this.mPhoneSignalStrengthBin = strengthBin;
        }
        if (newHistory) {
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
    }

    public void notePhoneStateLocked(int state, int simState) {
        updateAllPhoneStateLocked(state, simState, this.mPhoneSignalStrengthBinRaw);
    }

    @UnsupportedAppUsage
    public void notePhoneSignalStrengthLocked(SignalStrength signalStrength) {
        int bin = signalStrength.getLevel();
        updateAllPhoneStateLocked(this.mPhoneServiceStateRaw, this.mPhoneSimStateRaw, bin);
    }

    @UnsupportedAppUsage
    public void notePhoneDataConnectionStateLocked(int dataType, boolean hasData, int serviceType) {
        int bin = 0;
        if (hasData) {
            if (dataType > 0 && dataType <= 20) {
                bin = dataType;
            } else if (serviceType == 1) {
                bin = 0;
            } else if (serviceType == 2) {
                bin = 21;
            } else {
                bin = 22;
            }
        }
        if (this.mPhoneDataConnectionType != bin) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            BatteryStats.HistoryItem historyItem = this.mHistoryCur;
            historyItem.states = (historyItem.states & (-15873)) | (bin << 9);
            addHistoryRecordLocked(elapsedRealtime, uptime);
            int i = this.mPhoneDataConnectionType;
            if (i >= 0) {
                this.mPhoneDataConnectionsTimer[i].stopRunningLocked(elapsedRealtime);
            }
            this.mPhoneDataConnectionType = bin;
            this.mPhoneDataConnectionsTimer[bin].startRunningLocked(elapsedRealtime);
        }
    }

    public void noteWifiOnLocked() {
        if (!this.mWifiOn) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mHistoryCur.states2 |= 268435456;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mWifiOn = true;
            this.mWifiOnTimer.startRunningLocked(elapsedRealtime);
            scheduleSyncExternalStatsLocked("wifi-off", 2);
        }
    }

    public void noteWifiOffLocked() {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mWifiOn) {
            this.mHistoryCur.states2 &= -268435457;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mWifiOn = false;
            this.mWifiOnTimer.stopRunningLocked(elapsedRealtime);
            scheduleSyncExternalStatsLocked("wifi-on", 2);
        }
    }

    @UnsupportedAppUsage
    public void noteAudioOnLocked(int uid) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mAudioOnNesting == 0) {
            this.mHistoryCur.states |= 4194304;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mAudioOnTimer.startRunningLocked(elapsedRealtime);
        }
        this.mAudioOnNesting++;
        getUidStatsLocked(uid2).noteAudioTurnedOnLocked(elapsedRealtime);
    }

    @UnsupportedAppUsage
    public void noteAudioOffLocked(int uid) {
        if (this.mAudioOnNesting == 0) {
            return;
        }
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = this.mAudioOnNesting - 1;
        this.mAudioOnNesting = i;
        if (i == 0) {
            this.mHistoryCur.states &= -4194305;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mAudioOnTimer.stopRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid2).noteAudioTurnedOffLocked(elapsedRealtime);
    }

    @UnsupportedAppUsage
    public void noteVideoOnLocked(int uid) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mVideoOnNesting == 0) {
            this.mHistoryCur.states2 |= 1073741824;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mVideoOnTimer.startRunningLocked(elapsedRealtime);
        }
        this.mVideoOnNesting++;
        getUidStatsLocked(uid2).noteVideoTurnedOnLocked(elapsedRealtime);
    }

    @UnsupportedAppUsage
    public void noteVideoOffLocked(int uid) {
        if (this.mVideoOnNesting == 0) {
            return;
        }
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = this.mVideoOnNesting - 1;
        this.mVideoOnNesting = i;
        if (i == 0) {
            this.mHistoryCur.states2 &= -1073741825;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mVideoOnTimer.stopRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid2).noteVideoTurnedOffLocked(elapsedRealtime);
    }

    public void noteResetAudioLocked() {
        if (this.mAudioOnNesting > 0) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mAudioOnNesting = 0;
            this.mHistoryCur.states &= -4194305;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mAudioOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                Uid uid = this.mUidStats.valueAt(i);
                uid.noteResetAudioLocked(elapsedRealtime);
            }
        }
    }

    public void noteResetVideoLocked() {
        if (this.mVideoOnNesting > 0) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mAudioOnNesting = 0;
            this.mHistoryCur.states2 &= -1073741825;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mVideoOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                Uid uid = this.mUidStats.valueAt(i);
                uid.noteResetVideoLocked(elapsedRealtime);
            }
        }
    }

    public void noteActivityResumedLocked(int uid) {
        getUidStatsLocked(mapUid(uid)).noteActivityResumedLocked(this.mClocks.elapsedRealtime());
    }

    public void noteActivityPausedLocked(int uid) {
        getUidStatsLocked(mapUid(uid)).noteActivityPausedLocked(this.mClocks.elapsedRealtime());
    }

    public void noteVibratorOnLocked(int uid, long durationMillis) {
        getUidStatsLocked(mapUid(uid)).noteVibratorOnLocked(durationMillis);
    }

    public void noteVibratorOffLocked(int uid) {
        getUidStatsLocked(mapUid(uid)).noteVibratorOffLocked();
    }

    public void noteFlashlightOnLocked(int uid) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = this.mFlashlightOnNesting;
        this.mFlashlightOnNesting = i + 1;
        if (i == 0) {
            this.mHistoryCur.states2 |= 134217728;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mFlashlightOnTimer.startRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid2).noteFlashlightTurnedOnLocked(elapsedRealtime);
    }

    public void noteFlashlightOffLocked(int uid) {
        if (this.mFlashlightOnNesting == 0) {
            return;
        }
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = this.mFlashlightOnNesting - 1;
        this.mFlashlightOnNesting = i;
        if (i == 0) {
            this.mHistoryCur.states2 &= -134217729;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mFlashlightOnTimer.stopRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid2).noteFlashlightTurnedOffLocked(elapsedRealtime);
    }

    public void noteCameraOnLocked(int uid) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = this.mCameraOnNesting;
        this.mCameraOnNesting = i + 1;
        if (i == 0) {
            this.mHistoryCur.states2 |= 2097152;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mCameraOnTimer.startRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid2).noteCameraTurnedOnLocked(elapsedRealtime);
    }

    public void noteCameraOffLocked(int uid) {
        if (this.mCameraOnNesting == 0) {
            return;
        }
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        int i = this.mCameraOnNesting - 1;
        this.mCameraOnNesting = i;
        if (i == 0) {
            this.mHistoryCur.states2 &= -2097153;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mCameraOnTimer.stopRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid2).noteCameraTurnedOffLocked(elapsedRealtime);
    }

    public void noteResetCameraLocked() {
        if (this.mCameraOnNesting > 0) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mCameraOnNesting = 0;
            this.mHistoryCur.states2 &= -2097153;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mCameraOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                Uid uid = this.mUidStats.valueAt(i);
                uid.noteResetCameraLocked(elapsedRealtime);
            }
        }
    }

    public void noteResetFlashlightLocked() {
        if (this.mFlashlightOnNesting > 0) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mFlashlightOnNesting = 0;
            this.mHistoryCur.states2 &= -134217729;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mFlashlightOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                Uid uid = this.mUidStats.valueAt(i);
                uid.noteResetFlashlightLocked(elapsedRealtime);
            }
        }
    }

    private void noteBluetoothScanStartedLocked(WorkSource.WorkChain workChain, int uid, boolean isUnoptimized) {
        int uid2 = getAttributionUid(uid, workChain);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mBluetoothScanNesting == 0) {
            this.mHistoryCur.states2 |= 1048576;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mBluetoothScanTimer.startRunningLocked(elapsedRealtime);
        }
        this.mBluetoothScanNesting++;
        getUidStatsLocked(uid2).noteBluetoothScanStartedLocked(elapsedRealtime, isUnoptimized);
    }

    public void noteBluetoothScanStartedFromSourceLocked(WorkSource ws, boolean isUnoptimized) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteBluetoothScanStartedLocked(null, ws.get(i), isUnoptimized);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteBluetoothScanStartedLocked(workChains.get(i2), -1, isUnoptimized);
            }
        }
    }

    private void noteBluetoothScanStoppedLocked(WorkSource.WorkChain workChain, int uid, boolean isUnoptimized) {
        int uid2 = getAttributionUid(uid, workChain);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mBluetoothScanNesting--;
        if (this.mBluetoothScanNesting == 0) {
            this.mHistoryCur.states2 &= -1048577;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mBluetoothScanTimer.stopRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid2).noteBluetoothScanStoppedLocked(elapsedRealtime, isUnoptimized);
    }

    private int getAttributionUid(int uid, WorkSource.WorkChain workChain) {
        if (workChain != null) {
            return mapUid(workChain.getAttributionUid());
        }
        return mapUid(uid);
    }

    public void noteBluetoothScanStoppedFromSourceLocked(WorkSource ws, boolean isUnoptimized) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteBluetoothScanStoppedLocked(null, ws.get(i), isUnoptimized);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteBluetoothScanStoppedLocked(workChains.get(i2), -1, isUnoptimized);
            }
        }
    }

    public void noteResetBluetoothScanLocked() {
        if (this.mBluetoothScanNesting > 0) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mBluetoothScanNesting = 0;
            this.mHistoryCur.states2 &= -1048577;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mBluetoothScanTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                Uid uid = this.mUidStats.valueAt(i);
                uid.noteResetBluetoothScanLocked(elapsedRealtime);
            }
        }
    }

    public void noteBluetoothScanResultsFromSourceLocked(WorkSource ws, int numNewResults) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(ws.get(i));
            getUidStatsLocked(uid).noteBluetoothScanResultsLocked(numNewResults);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                WorkSource.WorkChain wc = workChains.get(i2);
                int uid2 = mapUid(wc.getAttributionUid());
                getUidStatsLocked(uid2).noteBluetoothScanResultsLocked(numNewResults);
            }
        }
    }

    private void noteWifiRadioApWakeupLocked(long elapsedRealtimeMillis, long uptimeMillis, int uid) {
        int uid2 = mapUid(uid);
        addHistoryEventLocked(elapsedRealtimeMillis, uptimeMillis, 19, "", uid2);
        getUidStatsLocked(uid2).noteWifiRadioApWakeupLocked();
    }

    public void noteWifiRadioPowerState(int powerState, long timestampNs, int uid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mWifiRadioPowerState != powerState) {
            boolean active = powerState == 2 || powerState == 3;
            if (active) {
                if (uid > 0) {
                    noteWifiRadioApWakeupLocked(elapsedRealtime, uptime, uid);
                }
                this.mHistoryCur.states |= 67108864;
                this.mWifiActiveTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mHistoryCur.states &= -67108865;
                this.mWifiActiveTimer.stopRunningLocked(timestampNs / TimeUtils.NANOS_PER_MS);
            }
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mWifiRadioPowerState = powerState;
        }
    }

    public void noteWifiRunningLocked(WorkSource ws) {
        if (!this.mGlobalWifiRunning) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mHistoryCur.states2 |= 536870912;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mGlobalWifiRunning = true;
            this.mGlobalWifiRunningTimer.startRunningLocked(elapsedRealtime);
            int N = ws.size();
            for (int i = 0; i < N; i++) {
                int uid = mapUid(ws.get(i));
                getUidStatsLocked(uid).noteWifiRunningLocked(elapsedRealtime);
            }
            List<WorkSource.WorkChain> workChains = ws.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    int uid2 = mapUid(workChains.get(i2).getAttributionUid());
                    getUidStatsLocked(uid2).noteWifiRunningLocked(elapsedRealtime);
                }
            }
            scheduleSyncExternalStatsLocked("wifi-running", 2);
            return;
        }
        Log.w(TAG, "noteWifiRunningLocked -- called while WIFI running");
    }

    public void noteWifiRunningChangedLocked(WorkSource oldWs, WorkSource newWs) {
        if (this.mGlobalWifiRunning) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            int N = oldWs.size();
            for (int i = 0; i < N; i++) {
                int uid = mapUid(oldWs.get(i));
                getUidStatsLocked(uid).noteWifiStoppedLocked(elapsedRealtime);
            }
            List<WorkSource.WorkChain> workChains = oldWs.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    int uid2 = mapUid(workChains.get(i2).getAttributionUid());
                    getUidStatsLocked(uid2).noteWifiStoppedLocked(elapsedRealtime);
                }
            }
            int N2 = newWs.size();
            for (int i3 = 0; i3 < N2; i3++) {
                int uid3 = mapUid(newWs.get(i3));
                getUidStatsLocked(uid3).noteWifiRunningLocked(elapsedRealtime);
            }
            List<WorkSource.WorkChain> workChains2 = newWs.getWorkChains();
            if (workChains2 != null) {
                for (int i4 = 0; i4 < workChains2.size(); i4++) {
                    int uid4 = mapUid(workChains2.get(i4).getAttributionUid());
                    getUidStatsLocked(uid4).noteWifiRunningLocked(elapsedRealtime);
                }
                return;
            }
            return;
        }
        Log.w(TAG, "noteWifiRunningChangedLocked -- called while WIFI not running");
    }

    public void noteWifiStoppedLocked(WorkSource ws) {
        if (this.mGlobalWifiRunning) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            this.mHistoryCur.states2 &= -536870913;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mGlobalWifiRunning = false;
            this.mGlobalWifiRunningTimer.stopRunningLocked(elapsedRealtime);
            int N = ws.size();
            for (int i = 0; i < N; i++) {
                int uid = mapUid(ws.get(i));
                getUidStatsLocked(uid).noteWifiStoppedLocked(elapsedRealtime);
            }
            List<WorkSource.WorkChain> workChains = ws.getWorkChains();
            if (workChains != null) {
                for (int i2 = 0; i2 < workChains.size(); i2++) {
                    int uid2 = mapUid(workChains.get(i2).getAttributionUid());
                    getUidStatsLocked(uid2).noteWifiStoppedLocked(elapsedRealtime);
                }
            }
            scheduleSyncExternalStatsLocked("wifi-stopped", 2);
            return;
        }
        Log.w(TAG, "noteWifiStoppedLocked -- called while WIFI not running");
    }

    public void noteWifiStateLocked(int wifiState, String accessPoint) {
        if (this.mWifiState != wifiState) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            int i = this.mWifiState;
            if (i >= 0) {
                this.mWifiStateTimer[i].stopRunningLocked(elapsedRealtime);
            }
            this.mWifiState = wifiState;
            this.mWifiStateTimer[wifiState].startRunningLocked(elapsedRealtime);
            scheduleSyncExternalStatsLocked("wifi-state", 2);
        }
    }

    public void noteWifiSupplicantStateChangedLocked(int supplState, boolean failedAuth) {
        if (this.mWifiSupplState != supplState) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int i = this.mWifiSupplState;
            if (i >= 0) {
                this.mWifiSupplStateTimer[i].stopRunningLocked(elapsedRealtime);
            }
            this.mWifiSupplState = supplState;
            this.mWifiSupplStateTimer[supplState].startRunningLocked(elapsedRealtime);
            BatteryStats.HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 = (historyItem.states2 & (-16)) | (supplState << 0);
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
    }

    void stopAllWifiSignalStrengthTimersLocked(int except) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        for (int i = 0; i < 5; i++) {
            if (i != except) {
                while (this.mWifiSignalStrengthsTimer[i].isRunningLocked()) {
                    this.mWifiSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtime);
                }
            }
        }
    }

    public void noteWifiRssiChangedLocked(int newRssi) {
        int strengthBin = WifiManager.calculateSignalLevel(newRssi, 5);
        if (this.mWifiSignalStrengthBin != strengthBin) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int i = this.mWifiSignalStrengthBin;
            if (i >= 0) {
                this.mWifiSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtime);
            }
            if (strengthBin >= 0) {
                if (!this.mWifiSignalStrengthsTimer[strengthBin].isRunningLocked()) {
                    this.mWifiSignalStrengthsTimer[strengthBin].startRunningLocked(elapsedRealtime);
                }
                BatteryStats.HistoryItem historyItem = this.mHistoryCur;
                historyItem.states2 = (historyItem.states2 & (-113)) | (strengthBin << 4);
                addHistoryRecordLocked(elapsedRealtime, uptime);
            } else {
                stopAllWifiSignalStrengthTimersLocked(-1);
            }
            this.mWifiSignalStrengthBin = strengthBin;
        }
    }

    @UnsupportedAppUsage
    public void noteFullWifiLockAcquiredLocked(int uid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mWifiFullLockNesting == 0) {
            this.mHistoryCur.states |= 268435456;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mWifiFullLockNesting++;
        getUidStatsLocked(uid).noteFullWifiLockAcquiredLocked(elapsedRealtime);
    }

    @UnsupportedAppUsage
    public void noteFullWifiLockReleasedLocked(int uid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mWifiFullLockNesting--;
        if (this.mWifiFullLockNesting == 0) {
            this.mHistoryCur.states &= -268435457;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid).noteFullWifiLockReleasedLocked(elapsedRealtime);
    }

    public void noteWifiScanStartedLocked(int uid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mWifiScanNesting == 0) {
            this.mHistoryCur.states |= 134217728;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mWifiScanNesting++;
        getUidStatsLocked(uid).noteWifiScanStartedLocked(elapsedRealtime);
    }

    public void noteWifiScanStoppedLocked(int uid) {
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mWifiScanNesting--;
        if (this.mWifiScanNesting == 0) {
            this.mHistoryCur.states &= -134217729;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid).noteWifiScanStoppedLocked(elapsedRealtime);
    }

    public void noteWifiBatchedScanStartedLocked(int uid, int csph) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        getUidStatsLocked(uid2).noteWifiBatchedScanStartedLocked(csph, elapsedRealtime);
    }

    public void noteWifiBatchedScanStoppedLocked(int uid) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        getUidStatsLocked(uid2).noteWifiBatchedScanStoppedLocked(elapsedRealtime);
    }

    @UnsupportedAppUsage
    public void noteWifiMulticastEnabledLocked(int uid) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        if (this.mWifiMulticastNesting == 0) {
            this.mHistoryCur.states |= 65536;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            if (!this.mWifiMulticastWakelockTimer.isRunningLocked()) {
                this.mWifiMulticastWakelockTimer.startRunningLocked(elapsedRealtime);
            }
        }
        this.mWifiMulticastNesting++;
        getUidStatsLocked(uid2).noteWifiMulticastEnabledLocked(elapsedRealtime);
    }

    @UnsupportedAppUsage
    public void noteWifiMulticastDisabledLocked(int uid) {
        int uid2 = mapUid(uid);
        long elapsedRealtime = this.mClocks.elapsedRealtime();
        long uptime = this.mClocks.uptimeMillis();
        this.mWifiMulticastNesting--;
        if (this.mWifiMulticastNesting == 0) {
            this.mHistoryCur.states &= -65537;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            if (this.mWifiMulticastWakelockTimer.isRunningLocked()) {
                this.mWifiMulticastWakelockTimer.stopRunningLocked(elapsedRealtime);
            }
        }
        getUidStatsLocked(uid2).noteWifiMulticastDisabledLocked(elapsedRealtime);
    }

    public void noteFullWifiLockAcquiredFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(ws.get(i));
            noteFullWifiLockAcquiredLocked(uid);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = mapUid(workChain.getAttributionUid());
                noteFullWifiLockAcquiredLocked(uid2);
            }
        }
    }

    public void noteFullWifiLockReleasedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(ws.get(i));
            noteFullWifiLockReleasedLocked(uid);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = mapUid(workChain.getAttributionUid());
                noteFullWifiLockReleasedLocked(uid2);
            }
        }
    }

    public void noteWifiScanStartedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(ws.get(i));
            noteWifiScanStartedLocked(uid);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = mapUid(workChain.getAttributionUid());
                noteWifiScanStartedLocked(uid2);
            }
        }
    }

    public void noteWifiScanStoppedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            int uid = mapUid(ws.get(i));
            noteWifiScanStoppedLocked(uid);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                WorkSource.WorkChain workChain = workChains.get(i2);
                int uid2 = mapUid(workChain.getAttributionUid());
                noteWifiScanStoppedLocked(uid2);
            }
        }
    }

    public void noteWifiBatchedScanStartedFromSourceLocked(WorkSource ws, int csph) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiBatchedScanStartedLocked(ws.get(i), csph);
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteWifiBatchedScanStartedLocked(workChains.get(i2).getAttributionUid(), csph);
            }
        }
    }

    public void noteWifiBatchedScanStoppedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiBatchedScanStoppedLocked(ws.get(i));
        }
        List<WorkSource.WorkChain> workChains = ws.getWorkChains();
        if (workChains != null) {
            for (int i2 = 0; i2 < workChains.size(); i2++) {
                noteWifiBatchedScanStoppedLocked(workChains.get(i2).getAttributionUid());
            }
        }
    }

    private static String[] includeInStringArray(String[] array, String str) {
        if (ArrayUtils.indexOf(array, str) >= 0) {
            return array;
        }
        String[] newArray = new String[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = str;
        return newArray;
    }

    private static String[] excludeFromStringArray(String[] array, String str) {
        int index = ArrayUtils.indexOf(array, str);
        if (index >= 0) {
            String[] newArray = new String[array.length - 1];
            if (index > 0) {
                System.arraycopy(array, 0, newArray, 0, index);
            }
            if (index < array.length - 1) {
                System.arraycopy(array, index + 1, newArray, index, (array.length - index) - 1);
            }
            return newArray;
        }
        return array;
    }

    public void noteNetworkInterfaceTypeLocked(String iface, int networkType) {
        if (TextUtils.isEmpty(iface)) {
            return;
        }
        synchronized (this.mModemNetworkLock) {
            if (ConnectivityManager.isNetworkTypeMobile(networkType)) {
                this.mModemIfaces = includeInStringArray(this.mModemIfaces, iface);
            } else {
                this.mModemIfaces = excludeFromStringArray(this.mModemIfaces, iface);
            }
        }
        synchronized (this.mWifiNetworkLock) {
            if (ConnectivityManager.isNetworkTypeWifi(networkType)) {
                this.mWifiIfaces = includeInStringArray(this.mWifiIfaces, iface);
            } else {
                this.mWifiIfaces = excludeFromStringArray(this.mWifiIfaces, iface);
            }
        }
    }

    public String[] getWifiIfaces() {
        String[] strArr;
        synchronized (this.mWifiNetworkLock) {
            strArr = this.mWifiIfaces;
        }
        return strArr;
    }

    public String[] getMobileIfaces() {
        String[] strArr;
        synchronized (this.mModemNetworkLock) {
            strArr = this.mModemIfaces;
        }
        return strArr;
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getScreenOnTime(long elapsedRealtimeUs, int which) {
        return this.mScreenOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getScreenOnCount(int which) {
        return this.mScreenOnTimer.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getScreenDozeTime(long elapsedRealtimeUs, int which) {
        return this.mScreenDozeTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getScreenDozeCount(int which) {
        return this.mScreenDozeTimer.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getScreenBrightnessTime(int brightnessBin, long elapsedRealtimeUs, int which) {
        return this.mScreenBrightnessTimer[brightnessBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public Timer getScreenBrightnessTimer(int brightnessBin) {
        return this.mScreenBrightnessTimer[brightnessBin];
    }

    @Override // android.os.BatteryStats
    public long getInteractiveTime(long elapsedRealtimeUs, int which) {
        return this.mInteractiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public long getPowerSaveModeEnabledTime(long elapsedRealtimeUs, int which) {
        return this.mPowerSaveModeEnabledTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getPowerSaveModeEnabledCount(int which) {
        return this.mPowerSaveModeEnabledTimer.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getDeviceIdleModeTime(int mode, long elapsedRealtimeUs, int which) {
        if (mode != 1) {
            if (mode == 2) {
                return this.mDeviceIdleModeFullTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
            }
            return 0L;
        }
        return this.mDeviceIdleModeLightTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getDeviceIdleModeCount(int mode, int which) {
        if (mode != 1) {
            if (mode == 2) {
                return this.mDeviceIdleModeFullTimer.getCountLocked(which);
            }
            return 0;
        }
        return this.mDeviceIdleModeLightTimer.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getLongestDeviceIdleModeTime(int mode) {
        if (mode != 1) {
            if (mode == 2) {
                return this.mLongestFullIdleTime;
            }
            return 0L;
        }
        return this.mLongestLightIdleTime;
    }

    @Override // android.os.BatteryStats
    public long getDeviceIdlingTime(int mode, long elapsedRealtimeUs, int which) {
        if (mode != 1) {
            if (mode == 2) {
                return this.mDeviceIdlingTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
            }
            return 0L;
        }
        return this.mDeviceLightIdlingTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getDeviceIdlingCount(int mode, int which) {
        if (mode != 1) {
            if (mode == 2) {
                return this.mDeviceIdlingTimer.getCountLocked(which);
            }
            return 0;
        }
        return this.mDeviceLightIdlingTimer.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public int getNumConnectivityChange(int which) {
        return this.mNumConnectivityChange;
    }

    @Override // android.os.BatteryStats
    public long getGpsSignalQualityTime(int strengthBin, long elapsedRealtimeUs, int which) {
        if (strengthBin < 0 || strengthBin >= 2) {
            return 0L;
        }
        return this.mGpsSignalQualityTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public long getGpsBatteryDrainMaMs() {
        double opVolt = this.mPowerProfile.getAveragePower(PowerProfile.POWER_GPS_OPERATING_VOLTAGE) / 1000.0d;
        if (opVolt == FeatureOption.FO_BOOT_POLICY_CPU) {
            return 0L;
        }
        double energyUsedMaMs = FeatureOption.FO_BOOT_POLICY_CPU;
        long rawRealtime = SystemClock.elapsedRealtime() * 1000;
        for (int i = 0; i < 2; i++) {
            energyUsedMaMs += this.mPowerProfile.getAveragePower(PowerProfile.POWER_GPS_SIGNAL_QUALITY_BASED, i) * (getGpsSignalQualityTime(i, rawRealtime, 0) / 1000);
        }
        return (long) energyUsedMaMs;
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getPhoneOnTime(long elapsedRealtimeUs, int which) {
        return this.mPhoneOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getPhoneOnCount(int which) {
        return this.mPhoneOnTimer.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getPhoneSignalStrengthTime(int strengthBin, long elapsedRealtimeUs, int which) {
        return this.mPhoneSignalStrengthsTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getPhoneSignalScanningTime(long elapsedRealtimeUs, int which) {
        return this.mPhoneSignalScanningTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public Timer getPhoneSignalScanningTimer() {
        return this.mPhoneSignalScanningTimer;
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public int getPhoneSignalStrengthCount(int strengthBin, int which) {
        return this.mPhoneSignalStrengthsTimer[strengthBin].getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public Timer getPhoneSignalStrengthTimer(int strengthBin) {
        return this.mPhoneSignalStrengthsTimer[strengthBin];
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getPhoneDataConnectionTime(int dataType, long elapsedRealtimeUs, int which) {
        return this.mPhoneDataConnectionsTimer[dataType].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public int getPhoneDataConnectionCount(int dataType, int which) {
        return this.mPhoneDataConnectionsTimer[dataType].getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public Timer getPhoneDataConnectionTimer(int dataType) {
        return this.mPhoneDataConnectionsTimer[dataType];
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getMobileRadioActiveTime(long elapsedRealtimeUs, int which) {
        return this.mMobileRadioActiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getMobileRadioActiveCount(int which) {
        return this.mMobileRadioActiveTimer.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getMobileRadioActiveAdjustedTime(int which) {
        return this.mMobileRadioActiveAdjustedTime.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getMobileRadioActiveUnknownTime(int which) {
        return this.mMobileRadioActiveUnknownTime.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public int getMobileRadioActiveUnknownCount(int which) {
        return (int) this.mMobileRadioActiveUnknownCount.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getWifiMulticastWakelockTime(long elapsedRealtimeUs, int which) {
        return this.mWifiMulticastWakelockTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getWifiMulticastWakelockCount(int which) {
        return this.mWifiMulticastWakelockTimer.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getWifiOnTime(long elapsedRealtimeUs, int which) {
        return this.mWifiOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public long getWifiActiveTime(long elapsedRealtimeUs, int which) {
        return this.mWifiActiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getGlobalWifiRunningTime(long elapsedRealtimeUs, int which) {
        return this.mGlobalWifiRunningTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public long getWifiStateTime(int wifiState, long elapsedRealtimeUs, int which) {
        return this.mWifiStateTimer[wifiState].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getWifiStateCount(int wifiState, int which) {
        return this.mWifiStateTimer[wifiState].getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public Timer getWifiStateTimer(int wifiState) {
        return this.mWifiStateTimer[wifiState];
    }

    @Override // android.os.BatteryStats
    public long getWifiSupplStateTime(int state, long elapsedRealtimeUs, int which) {
        return this.mWifiSupplStateTimer[state].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getWifiSupplStateCount(int state, int which) {
        return this.mWifiSupplStateTimer[state].getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public Timer getWifiSupplStateTimer(int state) {
        return this.mWifiSupplStateTimer[state];
    }

    @Override // android.os.BatteryStats
    public long getWifiSignalStrengthTime(int strengthBin, long elapsedRealtimeUs, int which) {
        return this.mWifiSignalStrengthsTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public int getWifiSignalStrengthCount(int strengthBin, int which) {
        return this.mWifiSignalStrengthsTimer[strengthBin].getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public Timer getWifiSignalStrengthTimer(int strengthBin) {
        return this.mWifiSignalStrengthsTimer[strengthBin];
    }

    @Override // android.os.BatteryStats
    public BatteryStats.ControllerActivityCounter getBluetoothControllerActivity() {
        return this.mBluetoothActivity;
    }

    @Override // android.os.BatteryStats
    public BatteryStats.ControllerActivityCounter getWifiControllerActivity() {
        return this.mWifiActivity;
    }

    @Override // android.os.BatteryStats
    public BatteryStats.ControllerActivityCounter getModemControllerActivity() {
        return this.mModemActivity;
    }

    @Override // android.os.BatteryStats
    public boolean hasBluetoothActivityReporting() {
        return this.mHasBluetoothReporting;
    }

    @Override // android.os.BatteryStats
    public boolean hasWifiActivityReporting() {
        return this.mHasWifiReporting;
    }

    @Override // android.os.BatteryStats
    public boolean hasModemActivityReporting() {
        return this.mHasModemReporting;
    }

    @Override // android.os.BatteryStats
    public long getFlashlightOnTime(long elapsedRealtimeUs, int which) {
        return this.mFlashlightOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public long getFlashlightOnCount(int which) {
        return this.mFlashlightOnTimer.getCountLocked(which);
    }

    @Override // android.os.BatteryStats
    public long getCameraOnTime(long elapsedRealtimeUs, int which) {
        return this.mCameraOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    public long getBluetoothScanTime(long elapsedRealtimeUs, int which) {
        return this.mBluetoothScanTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getNetworkActivityBytes(int type, int which) {
        if (type >= 0) {
            LongSamplingCounter[] longSamplingCounterArr = this.mNetworkByteActivityCounters;
            if (type < longSamplingCounterArr.length) {
                return longSamplingCounterArr[type].getCountLocked(which);
            }
            return 0L;
        }
        return 0L;
    }

    @Override // android.os.BatteryStats
    public long getNetworkActivityPackets(int type, int which) {
        if (type >= 0) {
            LongSamplingCounter[] longSamplingCounterArr = this.mNetworkPacketActivityCounters;
            if (type < longSamplingCounterArr.length) {
                return longSamplingCounterArr[type].getCountLocked(which);
            }
            return 0L;
        }
        return 0L;
    }

    @Override // android.os.BatteryStats
    public long getStartClockTime() {
        long currentTime = System.currentTimeMillis();
        if (currentTime <= MILLISECONDS_IN_YEAR || this.mStartClockTime >= currentTime - MILLISECONDS_IN_YEAR) {
            long j = this.mStartClockTime;
            if (j <= currentTime) {
                return j;
            }
        }
        recordCurrentTimeChangeLocked(currentTime, this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis());
        return currentTime - (this.mClocks.elapsedRealtime() - (this.mRealtimeStart / 1000));
    }

    @Override // android.os.BatteryStats
    public String getStartPlatformVersion() {
        return this.mStartPlatformVersion;
    }

    @Override // android.os.BatteryStats
    public String getEndPlatformVersion() {
        return this.mEndPlatformVersion;
    }

    @Override // android.os.BatteryStats
    public int getParcelVersion() {
        return 186;
    }

    @Override // android.os.BatteryStats
    public boolean getIsOnBattery() {
        return this.mOnBattery;
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public SparseArray<? extends BatteryStats.Uid> getUidStats() {
        return this.mUidStats;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends TimeBaseObs> boolean resetIfNotNull(T t, boolean detachIfReset) {
        if (t != null) {
            return t.reset(detachIfReset);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends TimeBaseObs> boolean resetIfNotNull(T[] t, boolean detachIfReset) {
        if (t != null) {
            boolean ret = true;
            for (T t2 : t) {
                ret &= resetIfNotNull(t2, detachIfReset);
            }
            return ret;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends TimeBaseObs> boolean resetIfNotNull(T[][] t, boolean detachIfReset) {
        if (t != null) {
            boolean ret = true;
            for (T[] tArr : t) {
                ret &= resetIfNotNull(tArr, detachIfReset);
            }
            return ret;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean resetIfNotNull(ControllerActivityCounterImpl counter, boolean detachIfReset) {
        if (counter != null) {
            counter.reset(detachIfReset);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends TimeBaseObs> void detachIfNotNull(T t) {
        if (t != null) {
            t.detach();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends TimeBaseObs> void detachIfNotNull(T[] t) {
        if (t != null) {
            for (T t2 : t) {
                detachIfNotNull(t2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends TimeBaseObs> void detachIfNotNull(T[][] t) {
        if (t != null) {
            for (T[] tArr : t) {
                detachIfNotNull(tArr);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void detachIfNotNull(ControllerActivityCounterImpl counter) {
        if (counter != null) {
            counter.detach();
        }
    }

    /* loaded from: classes3.dex */
    public static class Uid extends BatteryStats.Uid {
        static final int NO_BATCHED_SCAN_STARTED = -1;
        DualTimer mAggregatedPartialWakelockTimer;
        StopwatchTimer mAudioTurnedOnTimer;
        private ControllerActivityCounterImpl mBluetoothControllerActivity;
        Counter mBluetoothScanResultBgCounter;
        Counter mBluetoothScanResultCounter;
        DualTimer mBluetoothScanTimer;
        DualTimer mBluetoothUnoptimizedScanTimer;
        protected BatteryStatsImpl mBsi;
        StopwatchTimer mCameraTurnedOnTimer;
        IntArray mChildUids;
        LongSamplingCounter mCpuActiveTimeMs;
        LongSamplingCounter[][] mCpuClusterSpeedTimesUs;
        LongSamplingCounterArray mCpuClusterTimesMs;
        LongSamplingCounterArray mCpuFreqTimeMs;
        long mCurStepSystemTime;
        long mCurStepUserTime;
        StopwatchTimer mFlashlightTurnedOnTimer;
        StopwatchTimer mForegroundActivityTimer;
        StopwatchTimer mForegroundServiceTimer;
        boolean mFullWifiLockOut;
        StopwatchTimer mFullWifiLockTimer;
        final OverflowArrayMap<DualTimer> mJobStats;
        Counter mJobsDeferredCount;
        Counter mJobsDeferredEventCount;
        final Counter[] mJobsFreshnessBuckets;
        LongSamplingCounter mJobsFreshnessTimeMs;
        long mLastStepSystemTime;
        long mLastStepUserTime;
        LongSamplingCounter mMobileRadioActiveCount;
        LongSamplingCounter mMobileRadioActiveTime;
        private LongSamplingCounter mMobileRadioApWakeupCount;
        private ControllerActivityCounterImpl mModemControllerActivity;
        LongSamplingCounter[] mNetworkByteActivityCounters;
        LongSamplingCounter[] mNetworkPacketActivityCounters;
        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public final TimeBase mOnBatteryScreenOffBackgroundTimeBase;
        LongSamplingCounterArray[] mProcStateScreenOffTimeMs;
        LongSamplingCounterArray[] mProcStateTimeMs;
        StopwatchTimer[] mProcessStateTimer;
        LongSamplingCounterArray mScreenOffCpuFreqTimeMs;
        final OverflowArrayMap<DualTimer> mSyncStats;
        LongSamplingCounter mSystemCpuTime;
        final int mUid;
        Counter[] mUserActivityCounters;
        LongSamplingCounter mUserCpuTime;
        BatchTimer mVibratorOnTimer;
        StopwatchTimer mVideoTurnedOnTimer;
        final OverflowArrayMap<Wakelock> mWakelockStats;
        StopwatchTimer[] mWifiBatchedScanTimer;
        private ControllerActivityCounterImpl mWifiControllerActivity;
        StopwatchTimer mWifiMulticastTimer;
        int mWifiMulticastWakelockCount;
        private LongSamplingCounter mWifiRadioApWakeupCount;
        boolean mWifiRunning;
        StopwatchTimer mWifiRunningTimer;
        boolean mWifiScanStarted;
        DualTimer mWifiScanTimer;
        int mWifiBatchedScanBinStarted = -1;
        int mProcessState = 21;
        boolean mInForegroundService = false;
        final ArrayMap<String, SparseIntArray> mJobCompletions = new ArrayMap<>();
        final SparseArray<Sensor> mSensorStats = new SparseArray<>();
        final ArrayMap<String, Proc> mProcessStats = new ArrayMap<>();
        final ArrayMap<String, Pkg> mPackageStats = new ArrayMap<>();
        final SparseArray<BatteryStats.Uid.Pid> mPids = new SparseArray<>();
        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public final TimeBase mOnBatteryBackgroundTimeBase = new TimeBase(false);

        public Uid(BatteryStatsImpl bsi, int uid) {
            this.mBsi = bsi;
            this.mUid = uid;
            this.mOnBatteryBackgroundTimeBase.init(this.mBsi.mClocks.uptimeMillis() * 1000, this.mBsi.mClocks.elapsedRealtime() * 1000);
            this.mOnBatteryScreenOffBackgroundTimeBase = new TimeBase(false);
            this.mOnBatteryScreenOffBackgroundTimeBase.init(this.mBsi.mClocks.uptimeMillis() * 1000, this.mBsi.mClocks.elapsedRealtime() * 1000);
            this.mUserCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mSystemCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mCpuActiveTimeMs = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mCpuClusterTimesMs = new LongSamplingCounterArray(this.mBsi.mOnBatteryTimeBase);
            BatteryStatsImpl batteryStatsImpl = this.mBsi;
            Objects.requireNonNull(batteryStatsImpl);
            this.mWakelockStats = new OverflowArrayMap<Wakelock>(batteryStatsImpl, uid) { // from class: com.android.internal.os.BatteryStatsImpl.Uid.1
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(uid);
                    Objects.requireNonNull(batteryStatsImpl);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.internal.os.BatteryStatsImpl.OverflowArrayMap
                public Wakelock instantiateObject() {
                    return new Wakelock(Uid.this.mBsi, Uid.this);
                }
            };
            BatteryStatsImpl batteryStatsImpl2 = this.mBsi;
            Objects.requireNonNull(batteryStatsImpl2);
            this.mSyncStats = new OverflowArrayMap<DualTimer>(batteryStatsImpl2, uid) { // from class: com.android.internal.os.BatteryStatsImpl.Uid.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(uid);
                    Objects.requireNonNull(batteryStatsImpl2);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.internal.os.BatteryStatsImpl.OverflowArrayMap
                public DualTimer instantiateObject() {
                    Clocks clocks = Uid.this.mBsi.mClocks;
                    Uid uid2 = Uid.this;
                    return new DualTimer(clocks, uid2, 13, null, uid2.mBsi.mOnBatteryTimeBase, Uid.this.mOnBatteryBackgroundTimeBase);
                }
            };
            BatteryStatsImpl batteryStatsImpl3 = this.mBsi;
            Objects.requireNonNull(batteryStatsImpl3);
            this.mJobStats = new OverflowArrayMap<DualTimer>(batteryStatsImpl3, uid) { // from class: com.android.internal.os.BatteryStatsImpl.Uid.3
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(uid);
                    Objects.requireNonNull(batteryStatsImpl3);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.android.internal.os.BatteryStatsImpl.OverflowArrayMap
                public DualTimer instantiateObject() {
                    Clocks clocks = Uid.this.mBsi.mClocks;
                    Uid uid2 = Uid.this;
                    return new DualTimer(clocks, uid2, 14, null, uid2.mBsi.mOnBatteryTimeBase, Uid.this.mOnBatteryBackgroundTimeBase);
                }
            };
            this.mWifiRunningTimer = new StopwatchTimer(this.mBsi.mClocks, this, 4, this.mBsi.mWifiRunningTimers, this.mBsi.mOnBatteryTimeBase);
            this.mFullWifiLockTimer = new StopwatchTimer(this.mBsi.mClocks, this, 5, this.mBsi.mFullWifiLockTimers, this.mBsi.mOnBatteryTimeBase);
            this.mWifiScanTimer = new DualTimer(this.mBsi.mClocks, this, 6, this.mBsi.mWifiScanTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            this.mWifiBatchedScanTimer = new StopwatchTimer[5];
            this.mWifiMulticastTimer = new StopwatchTimer(this.mBsi.mClocks, this, 7, this.mBsi.mWifiMulticastTimers, this.mBsi.mOnBatteryTimeBase);
            this.mProcessStateTimer = new StopwatchTimer[7];
            this.mJobsDeferredEventCount = new Counter(this.mBsi.mOnBatteryTimeBase);
            this.mJobsDeferredCount = new Counter(this.mBsi.mOnBatteryTimeBase);
            this.mJobsFreshnessTimeMs = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            this.mJobsFreshnessBuckets = new Counter[BatteryStats.JOB_FRESHNESS_BUCKETS.length];
        }

        @VisibleForTesting
        public void setProcessStateForTest(int procState) {
            this.mProcessState = procState;
        }

        @Override // android.os.BatteryStats.Uid
        public long[] getCpuFreqTimes(int which) {
            return nullIfAllZeros(this.mCpuFreqTimeMs, which);
        }

        @Override // android.os.BatteryStats.Uid
        public long[] getScreenOffCpuFreqTimes(int which) {
            return nullIfAllZeros(this.mScreenOffCpuFreqTimeMs, which);
        }

        @Override // android.os.BatteryStats.Uid
        public long getCpuActiveTime() {
            return this.mCpuActiveTimeMs.getCountLocked(0);
        }

        @Override // android.os.BatteryStats.Uid
        public long[] getCpuClusterTimes() {
            return nullIfAllZeros(this.mCpuClusterTimesMs, 0);
        }

        @Override // android.os.BatteryStats.Uid
        public long[] getCpuFreqTimes(int which, int procState) {
            if (which < 0 || which >= 7 || this.mProcStateTimeMs == null) {
                return null;
            }
            if (!this.mBsi.mPerProcStateCpuTimesAvailable) {
                this.mProcStateTimeMs = null;
                return null;
            }
            return nullIfAllZeros(this.mProcStateTimeMs[procState], which);
        }

        @Override // android.os.BatteryStats.Uid
        public long[] getScreenOffCpuFreqTimes(int which, int procState) {
            if (which < 0 || which >= 7 || this.mProcStateScreenOffTimeMs == null) {
                return null;
            }
            if (!this.mBsi.mPerProcStateCpuTimesAvailable) {
                this.mProcStateScreenOffTimeMs = null;
                return null;
            }
            return nullIfAllZeros(this.mProcStateScreenOffTimeMs[procState], which);
        }

        public void addIsolatedUid(int isolatedUid) {
            IntArray intArray = this.mChildUids;
            if (intArray == null) {
                this.mChildUids = new IntArray();
            } else if (intArray.indexOf(isolatedUid) >= 0) {
                return;
            }
            this.mChildUids.add(isolatedUid);
        }

        public void removeIsolatedUid(int isolatedUid) {
            IntArray intArray = this.mChildUids;
            int idx = intArray == null ? -1 : intArray.indexOf(isolatedUid);
            if (idx < 0) {
                return;
            }
            this.mChildUids.remove(idx);
        }

        private long[] nullIfAllZeros(LongSamplingCounterArray cpuTimesMs, int which) {
            long[] counts;
            if (cpuTimesMs == null || (counts = cpuTimesMs.getCountsLocked(which)) == null) {
                return null;
            }
            for (int i = counts.length - 1; i >= 0; i--) {
                if (counts[i] != 0) {
                    return counts;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addProcStateTimesMs(int procState, long[] cpuTimesMs, boolean onBattery) {
            if (this.mProcStateTimeMs == null) {
                this.mProcStateTimeMs = new LongSamplingCounterArray[7];
            }
            LongSamplingCounterArray[] longSamplingCounterArrayArr = this.mProcStateTimeMs;
            if (longSamplingCounterArrayArr[procState] == null || longSamplingCounterArrayArr[procState].getSize() != cpuTimesMs.length) {
                BatteryStatsImpl.detachIfNotNull(this.mProcStateTimeMs[procState]);
                this.mProcStateTimeMs[procState] = new LongSamplingCounterArray(this.mBsi.mOnBatteryTimeBase);
            }
            this.mProcStateTimeMs[procState].addCountLocked(cpuTimesMs, onBattery);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addProcStateScreenOffTimesMs(int procState, long[] cpuTimesMs, boolean onBatteryScreenOff) {
            if (this.mProcStateScreenOffTimeMs == null) {
                this.mProcStateScreenOffTimeMs = new LongSamplingCounterArray[7];
            }
            LongSamplingCounterArray[] longSamplingCounterArrayArr = this.mProcStateScreenOffTimeMs;
            if (longSamplingCounterArrayArr[procState] == null || longSamplingCounterArrayArr[procState].getSize() != cpuTimesMs.length) {
                BatteryStatsImpl.detachIfNotNull(this.mProcStateScreenOffTimeMs[procState]);
                this.mProcStateScreenOffTimeMs[procState] = new LongSamplingCounterArray(this.mBsi.mOnBatteryScreenOffTimeBase);
            }
            this.mProcStateScreenOffTimeMs[procState].addCountLocked(cpuTimesMs, onBatteryScreenOff);
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getAggregatedPartialWakelockTimer() {
            return this.mAggregatedPartialWakelockTimer;
        }

        @Override // android.os.BatteryStats.Uid
        @UnsupportedAppUsage
        public ArrayMap<String, ? extends BatteryStats.Uid.Wakelock> getWakelockStats() {
            return this.mWakelockStats.getMap();
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getMulticastWakelockStats() {
            return this.mWifiMulticastTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public ArrayMap<String, ? extends BatteryStats.Timer> getSyncStats() {
            return this.mSyncStats.getMap();
        }

        @Override // android.os.BatteryStats.Uid
        public ArrayMap<String, ? extends BatteryStats.Timer> getJobStats() {
            return this.mJobStats.getMap();
        }

        @Override // android.os.BatteryStats.Uid
        public ArrayMap<String, SparseIntArray> getJobCompletionStats() {
            return this.mJobCompletions;
        }

        @Override // android.os.BatteryStats.Uid
        @UnsupportedAppUsage
        public SparseArray<? extends BatteryStats.Uid.Sensor> getSensorStats() {
            return this.mSensorStats;
        }

        @Override // android.os.BatteryStats.Uid
        @UnsupportedAppUsage
        public ArrayMap<String, ? extends BatteryStats.Uid.Proc> getProcessStats() {
            return this.mProcessStats;
        }

        @Override // android.os.BatteryStats.Uid
        public ArrayMap<String, ? extends BatteryStats.Uid.Pkg> getPackageStats() {
            return this.mPackageStats;
        }

        @Override // android.os.BatteryStats.Uid
        @UnsupportedAppUsage
        public int getUid() {
            return this.mUid;
        }

        @Override // android.os.BatteryStats.Uid
        public void noteWifiRunningLocked(long elapsedRealtimeMs) {
            if (!this.mWifiRunning) {
                this.mWifiRunning = true;
                if (this.mWifiRunningTimer == null) {
                    this.mWifiRunningTimer = new StopwatchTimer(this.mBsi.mClocks, this, 4, this.mBsi.mWifiRunningTimers, this.mBsi.mOnBatteryTimeBase);
                }
                this.mWifiRunningTimer.startRunningLocked(elapsedRealtimeMs);
            }
        }

        @Override // android.os.BatteryStats.Uid
        public void noteWifiStoppedLocked(long elapsedRealtimeMs) {
            if (this.mWifiRunning) {
                this.mWifiRunning = false;
                this.mWifiRunningTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        @Override // android.os.BatteryStats.Uid
        public void noteFullWifiLockAcquiredLocked(long elapsedRealtimeMs) {
            if (!this.mFullWifiLockOut) {
                this.mFullWifiLockOut = true;
                if (this.mFullWifiLockTimer == null) {
                    this.mFullWifiLockTimer = new StopwatchTimer(this.mBsi.mClocks, this, 5, this.mBsi.mFullWifiLockTimers, this.mBsi.mOnBatteryTimeBase);
                }
                this.mFullWifiLockTimer.startRunningLocked(elapsedRealtimeMs);
            }
        }

        @Override // android.os.BatteryStats.Uid
        public void noteFullWifiLockReleasedLocked(long elapsedRealtimeMs) {
            if (this.mFullWifiLockOut) {
                this.mFullWifiLockOut = false;
                this.mFullWifiLockTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        @Override // android.os.BatteryStats.Uid
        public void noteWifiScanStartedLocked(long elapsedRealtimeMs) {
            if (!this.mWifiScanStarted) {
                this.mWifiScanStarted = true;
                if (this.mWifiScanTimer == null) {
                    this.mWifiScanTimer = new DualTimer(this.mBsi.mClocks, this, 6, this.mBsi.mWifiScanTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
                }
                this.mWifiScanTimer.startRunningLocked(elapsedRealtimeMs);
            }
        }

        @Override // android.os.BatteryStats.Uid
        public void noteWifiScanStoppedLocked(long elapsedRealtimeMs) {
            if (this.mWifiScanStarted) {
                this.mWifiScanStarted = false;
                this.mWifiScanTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        @Override // android.os.BatteryStats.Uid
        public void noteWifiBatchedScanStartedLocked(int csph, long elapsedRealtimeMs) {
            int bin = 0;
            while (csph > 8 && bin < 4) {
                csph >>= 3;
                bin++;
            }
            int i = this.mWifiBatchedScanBinStarted;
            if (i == bin) {
                return;
            }
            if (i != -1) {
                this.mWifiBatchedScanTimer[i].stopRunningLocked(elapsedRealtimeMs);
            }
            this.mWifiBatchedScanBinStarted = bin;
            if (this.mWifiBatchedScanTimer[bin] == null) {
                makeWifiBatchedScanBin(bin, null);
            }
            this.mWifiBatchedScanTimer[bin].startRunningLocked(elapsedRealtimeMs);
        }

        @Override // android.os.BatteryStats.Uid
        public void noteWifiBatchedScanStoppedLocked(long elapsedRealtimeMs) {
            int i = this.mWifiBatchedScanBinStarted;
            if (i != -1) {
                this.mWifiBatchedScanTimer[i].stopRunningLocked(elapsedRealtimeMs);
                this.mWifiBatchedScanBinStarted = -1;
            }
        }

        @Override // android.os.BatteryStats.Uid
        public void noteWifiMulticastEnabledLocked(long elapsedRealtimeMs) {
            if (this.mWifiMulticastWakelockCount == 0) {
                if (this.mWifiMulticastTimer == null) {
                    this.mWifiMulticastTimer = new StopwatchTimer(this.mBsi.mClocks, this, 7, this.mBsi.mWifiMulticastTimers, this.mBsi.mOnBatteryTimeBase);
                }
                this.mWifiMulticastTimer.startRunningLocked(elapsedRealtimeMs);
            }
            this.mWifiMulticastWakelockCount++;
        }

        @Override // android.os.BatteryStats.Uid
        public void noteWifiMulticastDisabledLocked(long elapsedRealtimeMs) {
            int i = this.mWifiMulticastWakelockCount;
            if (i == 0) {
                return;
            }
            this.mWifiMulticastWakelockCount = i - 1;
            if (this.mWifiMulticastWakelockCount == 0) {
                this.mWifiMulticastTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        @Override // android.os.BatteryStats.Uid
        public BatteryStats.ControllerActivityCounter getWifiControllerActivity() {
            return this.mWifiControllerActivity;
        }

        @Override // android.os.BatteryStats.Uid
        public BatteryStats.ControllerActivityCounter getBluetoothControllerActivity() {
            return this.mBluetoothControllerActivity;
        }

        @Override // android.os.BatteryStats.Uid
        public BatteryStats.ControllerActivityCounter getModemControllerActivity() {
            return this.mModemControllerActivity;
        }

        public ControllerActivityCounterImpl getOrCreateWifiControllerActivityLocked() {
            if (this.mWifiControllerActivity == null) {
                this.mWifiControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 1);
            }
            return this.mWifiControllerActivity;
        }

        public ControllerActivityCounterImpl getOrCreateBluetoothControllerActivityLocked() {
            if (this.mBluetoothControllerActivity == null) {
                this.mBluetoothControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 1);
            }
            return this.mBluetoothControllerActivity;
        }

        public ControllerActivityCounterImpl getOrCreateModemControllerActivityLocked() {
            if (this.mModemControllerActivity == null) {
                this.mModemControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 5);
            }
            return this.mModemControllerActivity;
        }

        public StopwatchTimer createAudioTurnedOnTimerLocked() {
            if (this.mAudioTurnedOnTimer == null) {
                this.mAudioTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 15, this.mBsi.mAudioTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mAudioTurnedOnTimer;
        }

        public void noteAudioTurnedOnLocked(long elapsedRealtimeMs) {
            createAudioTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteAudioTurnedOffLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mAudioTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetAudioLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mAudioTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public StopwatchTimer createVideoTurnedOnTimerLocked() {
            if (this.mVideoTurnedOnTimer == null) {
                this.mVideoTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 8, this.mBsi.mVideoTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mVideoTurnedOnTimer;
        }

        public void noteVideoTurnedOnLocked(long elapsedRealtimeMs) {
            createVideoTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteVideoTurnedOffLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mVideoTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetVideoLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mVideoTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public StopwatchTimer createFlashlightTurnedOnTimerLocked() {
            if (this.mFlashlightTurnedOnTimer == null) {
                this.mFlashlightTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 16, this.mBsi.mFlashlightTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mFlashlightTurnedOnTimer;
        }

        public void noteFlashlightTurnedOnLocked(long elapsedRealtimeMs) {
            createFlashlightTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteFlashlightTurnedOffLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mFlashlightTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetFlashlightLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mFlashlightTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public StopwatchTimer createCameraTurnedOnTimerLocked() {
            if (this.mCameraTurnedOnTimer == null) {
                this.mCameraTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 17, this.mBsi.mCameraTurnedOnTimers, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mCameraTurnedOnTimer;
        }

        public void noteCameraTurnedOnLocked(long elapsedRealtimeMs) {
            createCameraTurnedOnTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteCameraTurnedOffLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mCameraTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetCameraLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mCameraTurnedOnTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public StopwatchTimer createForegroundActivityTimerLocked() {
            if (this.mForegroundActivityTimer == null) {
                this.mForegroundActivityTimer = new StopwatchTimer(this.mBsi.mClocks, this, 10, null, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mForegroundActivityTimer;
        }

        public StopwatchTimer createForegroundServiceTimerLocked() {
            if (this.mForegroundServiceTimer == null) {
                this.mForegroundServiceTimer = new StopwatchTimer(this.mBsi.mClocks, this, 22, null, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mForegroundServiceTimer;
        }

        public DualTimer createAggregatedPartialWakelockTimerLocked() {
            if (this.mAggregatedPartialWakelockTimer == null) {
                this.mAggregatedPartialWakelockTimer = new DualTimer(this.mBsi.mClocks, this, 20, null, this.mBsi.mOnBatteryScreenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase);
            }
            return this.mAggregatedPartialWakelockTimer;
        }

        public DualTimer createBluetoothScanTimerLocked() {
            if (this.mBluetoothScanTimer == null) {
                this.mBluetoothScanTimer = new DualTimer(this.mBsi.mClocks, this, 19, this.mBsi.mBluetoothScanOnTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            }
            return this.mBluetoothScanTimer;
        }

        public DualTimer createBluetoothUnoptimizedScanTimerLocked() {
            if (this.mBluetoothUnoptimizedScanTimer == null) {
                this.mBluetoothUnoptimizedScanTimer = new DualTimer(this.mBsi.mClocks, this, 21, null, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            }
            return this.mBluetoothUnoptimizedScanTimer;
        }

        public void noteBluetoothScanStartedLocked(long elapsedRealtimeMs, boolean isUnoptimized) {
            createBluetoothScanTimerLocked().startRunningLocked(elapsedRealtimeMs);
            if (isUnoptimized) {
                createBluetoothUnoptimizedScanTimerLocked().startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteBluetoothScanStoppedLocked(long elapsedRealtimeMs, boolean isUnoptimized) {
            DualTimer dualTimer;
            DualTimer dualTimer2 = this.mBluetoothScanTimer;
            if (dualTimer2 != null) {
                dualTimer2.stopRunningLocked(elapsedRealtimeMs);
            }
            if (isUnoptimized && (dualTimer = this.mBluetoothUnoptimizedScanTimer) != null) {
                dualTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteResetBluetoothScanLocked(long elapsedRealtimeMs) {
            DualTimer dualTimer = this.mBluetoothScanTimer;
            if (dualTimer != null) {
                dualTimer.stopAllRunningLocked(elapsedRealtimeMs);
            }
            DualTimer dualTimer2 = this.mBluetoothUnoptimizedScanTimer;
            if (dualTimer2 != null) {
                dualTimer2.stopAllRunningLocked(elapsedRealtimeMs);
            }
        }

        public Counter createBluetoothScanResultCounterLocked() {
            if (this.mBluetoothScanResultCounter == null) {
                this.mBluetoothScanResultCounter = new Counter(this.mBsi.mOnBatteryTimeBase);
            }
            return this.mBluetoothScanResultCounter;
        }

        public Counter createBluetoothScanResultBgCounterLocked() {
            if (this.mBluetoothScanResultBgCounter == null) {
                this.mBluetoothScanResultBgCounter = new Counter(this.mOnBatteryBackgroundTimeBase);
            }
            return this.mBluetoothScanResultBgCounter;
        }

        public void noteBluetoothScanResultsLocked(int numNewResults) {
            createBluetoothScanResultCounterLocked().addAtomic(numNewResults);
            createBluetoothScanResultBgCounterLocked().addAtomic(numNewResults);
        }

        @Override // android.os.BatteryStats.Uid
        public void noteActivityResumedLocked(long elapsedRealtimeMs) {
            createForegroundActivityTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        @Override // android.os.BatteryStats.Uid
        public void noteActivityPausedLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mForegroundActivityTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteForegroundServiceResumedLocked(long elapsedRealtimeMs) {
            createForegroundServiceTimerLocked().startRunningLocked(elapsedRealtimeMs);
        }

        public void noteForegroundServicePausedLocked(long elapsedRealtimeMs) {
            StopwatchTimer stopwatchTimer = this.mForegroundServiceTimer;
            if (stopwatchTimer != null) {
                stopwatchTimer.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public BatchTimer createVibratorOnTimerLocked() {
            if (this.mVibratorOnTimer == null) {
                this.mVibratorOnTimer = new BatchTimer(this.mBsi.mClocks, this, 9, this.mBsi.mOnBatteryTimeBase);
            }
            return this.mVibratorOnTimer;
        }

        public void noteVibratorOnLocked(long durationMillis) {
            createVibratorOnTimerLocked().addDuration(this.mBsi, durationMillis);
        }

        public void noteVibratorOffLocked() {
            BatchTimer batchTimer = this.mVibratorOnTimer;
            if (batchTimer != null) {
                batchTimer.abortLastDuration(this.mBsi);
            }
        }

        @Override // android.os.BatteryStats.Uid
        @UnsupportedAppUsage
        public long getWifiRunningTime(long elapsedRealtimeUs, int which) {
            StopwatchTimer stopwatchTimer = this.mWifiRunningTimer;
            if (stopwatchTimer == null) {
                return 0L;
            }
            return stopwatchTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        @Override // android.os.BatteryStats.Uid
        public long getFullWifiLockTime(long elapsedRealtimeUs, int which) {
            StopwatchTimer stopwatchTimer = this.mFullWifiLockTimer;
            if (stopwatchTimer == null) {
                return 0L;
            }
            return stopwatchTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        @Override // android.os.BatteryStats.Uid
        @UnsupportedAppUsage
        public long getWifiScanTime(long elapsedRealtimeUs, int which) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null) {
                return 0L;
            }
            return dualTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        @Override // android.os.BatteryStats.Uid
        public int getWifiScanCount(int which) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null) {
                return 0;
            }
            return dualTimer.getCountLocked(which);
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getWifiScanTimer() {
            return this.mWifiScanTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public int getWifiScanBackgroundCount(int which) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null || dualTimer.getSubTimer() == null) {
                return 0;
            }
            return this.mWifiScanTimer.getSubTimer().getCountLocked(which);
        }

        @Override // android.os.BatteryStats.Uid
        public long getWifiScanActualTime(long elapsedRealtimeUs) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null) {
                return 0L;
            }
            long elapsedRealtimeMs = (500 + elapsedRealtimeUs) / 1000;
            return dualTimer.getTotalDurationMsLocked(elapsedRealtimeMs) * 1000;
        }

        @Override // android.os.BatteryStats.Uid
        public long getWifiScanBackgroundTime(long elapsedRealtimeUs) {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null || dualTimer.getSubTimer() == null) {
                return 0L;
            }
            long elapsedRealtimeMs = (500 + elapsedRealtimeUs) / 1000;
            return this.mWifiScanTimer.getSubTimer().getTotalDurationMsLocked(elapsedRealtimeMs) * 1000;
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getWifiScanBackgroundTimer() {
            DualTimer dualTimer = this.mWifiScanTimer;
            if (dualTimer == null) {
                return null;
            }
            return dualTimer.getSubTimer();
        }

        @Override // android.os.BatteryStats.Uid
        public long getWifiBatchedScanTime(int csphBin, long elapsedRealtimeUs, int which) {
            if (csphBin < 0 || csphBin >= 5) {
                return 0L;
            }
            StopwatchTimer[] stopwatchTimerArr = this.mWifiBatchedScanTimer;
            if (stopwatchTimerArr[csphBin] == null) {
                return 0L;
            }
            return stopwatchTimerArr[csphBin].getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        @Override // android.os.BatteryStats.Uid
        public int getWifiBatchedScanCount(int csphBin, int which) {
            if (csphBin < 0 || csphBin >= 5) {
                return 0;
            }
            StopwatchTimer[] stopwatchTimerArr = this.mWifiBatchedScanTimer;
            if (stopwatchTimerArr[csphBin] == null) {
                return 0;
            }
            return stopwatchTimerArr[csphBin].getCountLocked(which);
        }

        @Override // android.os.BatteryStats.Uid
        public long getWifiMulticastTime(long elapsedRealtimeUs, int which) {
            StopwatchTimer stopwatchTimer = this.mWifiMulticastTimer;
            if (stopwatchTimer == null) {
                return 0L;
            }
            return stopwatchTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getAudioTurnedOnTimer() {
            return this.mAudioTurnedOnTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getVideoTurnedOnTimer() {
            return this.mVideoTurnedOnTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getFlashlightTurnedOnTimer() {
            return this.mFlashlightTurnedOnTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getCameraTurnedOnTimer() {
            return this.mCameraTurnedOnTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getForegroundActivityTimer() {
            return this.mForegroundActivityTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getForegroundServiceTimer() {
            return this.mForegroundServiceTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getBluetoothScanTimer() {
            return this.mBluetoothScanTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getBluetoothScanBackgroundTimer() {
            DualTimer dualTimer = this.mBluetoothScanTimer;
            if (dualTimer == null) {
                return null;
            }
            return dualTimer.getSubTimer();
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getBluetoothUnoptimizedScanTimer() {
            return this.mBluetoothUnoptimizedScanTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getBluetoothUnoptimizedScanBackgroundTimer() {
            DualTimer dualTimer = this.mBluetoothUnoptimizedScanTimer;
            if (dualTimer == null) {
                return null;
            }
            return dualTimer.getSubTimer();
        }

        @Override // android.os.BatteryStats.Uid
        public Counter getBluetoothScanResultCounter() {
            return this.mBluetoothScanResultCounter;
        }

        @Override // android.os.BatteryStats.Uid
        public Counter getBluetoothScanResultBgCounter() {
            return this.mBluetoothScanResultBgCounter;
        }

        void makeProcessState(int i, Parcel in) {
            if (i >= 0 && i < 7) {
                BatteryStatsImpl.detachIfNotNull(this.mProcessStateTimer[i]);
                if (in == null) {
                    this.mProcessStateTimer[i] = new StopwatchTimer(this.mBsi.mClocks, this, 12, null, this.mBsi.mOnBatteryTimeBase);
                } else {
                    this.mProcessStateTimer[i] = new StopwatchTimer(this.mBsi.mClocks, this, 12, null, this.mBsi.mOnBatteryTimeBase, in);
                }
            }
        }

        @Override // android.os.BatteryStats.Uid
        public long getProcessStateTime(int state, long elapsedRealtimeUs, int which) {
            if (state < 0 || state >= 7) {
                return 0L;
            }
            StopwatchTimer[] stopwatchTimerArr = this.mProcessStateTimer;
            if (stopwatchTimerArr[state] == null) {
                return 0L;
            }
            return stopwatchTimerArr[state].getTotalTimeLocked(elapsedRealtimeUs, which);
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getProcessStateTimer(int state) {
            if (state < 0 || state >= 7) {
                return null;
            }
            return this.mProcessStateTimer[state];
        }

        @Override // android.os.BatteryStats.Uid
        public Timer getVibratorOnTimer() {
            return this.mVibratorOnTimer;
        }

        @Override // android.os.BatteryStats.Uid
        public void noteUserActivityLocked(int type) {
            if (this.mUserActivityCounters == null) {
                initUserActivityLocked();
            }
            if (type >= 0 && type < NUM_USER_ACTIVITY_TYPES) {
                this.mUserActivityCounters[type].stepAtomic();
                return;
            }
            Slog.w(BatteryStatsImpl.TAG, "Unknown user activity type " + type + " was specified.", new Throwable());
        }

        @Override // android.os.BatteryStats.Uid
        public boolean hasUserActivity() {
            return this.mUserActivityCounters != null;
        }

        @Override // android.os.BatteryStats.Uid
        public int getUserActivityCount(int type, int which) {
            Counter[] counterArr = this.mUserActivityCounters;
            if (counterArr == null) {
                return 0;
            }
            return counterArr[type].getCountLocked(which);
        }

        void makeWifiBatchedScanBin(int i, Parcel in) {
            if (i < 0 || i >= 5) {
                return;
            }
            ArrayList<StopwatchTimer> collected = this.mBsi.mWifiBatchedScanTimers.get(i);
            if (collected == null) {
                collected = new ArrayList<>();
                this.mBsi.mWifiBatchedScanTimers.put(i, collected);
            }
            BatteryStatsImpl.detachIfNotNull(this.mWifiBatchedScanTimer[i]);
            if (in == null) {
                this.mWifiBatchedScanTimer[i] = new StopwatchTimer(this.mBsi.mClocks, this, 11, collected, this.mBsi.mOnBatteryTimeBase);
                return;
            }
            this.mWifiBatchedScanTimer[i] = new StopwatchTimer(this.mBsi.mClocks, this, 11, collected, this.mBsi.mOnBatteryTimeBase, in);
        }

        void initUserActivityLocked() {
            BatteryStatsImpl.detachIfNotNull(this.mUserActivityCounters);
            this.mUserActivityCounters = new Counter[NUM_USER_ACTIVITY_TYPES];
            for (int i = 0; i < NUM_USER_ACTIVITY_TYPES; i++) {
                this.mUserActivityCounters[i] = new Counter(this.mBsi.mOnBatteryTimeBase);
            }
        }

        void noteNetworkActivityLocked(int type, long deltaBytes, long deltaPackets) {
            if (this.mNetworkByteActivityCounters == null) {
                initNetworkActivityLocked();
            }
            if (type >= 0 && type < 10) {
                this.mNetworkByteActivityCounters[type].addCountLocked(deltaBytes);
                this.mNetworkPacketActivityCounters[type].addCountLocked(deltaPackets);
                return;
            }
            Slog.w(BatteryStatsImpl.TAG, "Unknown network activity type " + type + " was specified.", new Throwable());
        }

        void noteMobileRadioActiveTimeLocked(long batteryUptime) {
            if (this.mNetworkByteActivityCounters == null) {
                initNetworkActivityLocked();
            }
            this.mMobileRadioActiveTime.addCountLocked(batteryUptime);
            this.mMobileRadioActiveCount.addCountLocked(1L);
        }

        @Override // android.os.BatteryStats.Uid
        public boolean hasNetworkActivity() {
            return this.mNetworkByteActivityCounters != null;
        }

        @Override // android.os.BatteryStats.Uid
        public long getNetworkActivityBytes(int type, int which) {
            LongSamplingCounter[] longSamplingCounterArr = this.mNetworkByteActivityCounters;
            if (longSamplingCounterArr != null && type >= 0 && type < longSamplingCounterArr.length) {
                return longSamplingCounterArr[type].getCountLocked(which);
            }
            return 0L;
        }

        @Override // android.os.BatteryStats.Uid
        public long getNetworkActivityPackets(int type, int which) {
            LongSamplingCounter[] longSamplingCounterArr = this.mNetworkPacketActivityCounters;
            if (longSamplingCounterArr != null && type >= 0 && type < longSamplingCounterArr.length) {
                return longSamplingCounterArr[type].getCountLocked(which);
            }
            return 0L;
        }

        @Override // android.os.BatteryStats.Uid
        public long getMobileRadioActiveTime(int which) {
            LongSamplingCounter longSamplingCounter = this.mMobileRadioActiveTime;
            if (longSamplingCounter != null) {
                return longSamplingCounter.getCountLocked(which);
            }
            return 0L;
        }

        @Override // android.os.BatteryStats.Uid
        public int getMobileRadioActiveCount(int which) {
            LongSamplingCounter longSamplingCounter = this.mMobileRadioActiveCount;
            if (longSamplingCounter != null) {
                return (int) longSamplingCounter.getCountLocked(which);
            }
            return 0;
        }

        @Override // android.os.BatteryStats.Uid
        public long getUserCpuTimeUs(int which) {
            return this.mUserCpuTime.getCountLocked(which);
        }

        @Override // android.os.BatteryStats.Uid
        public long getSystemCpuTimeUs(int which) {
            return this.mSystemCpuTime.getCountLocked(which);
        }

        @Override // android.os.BatteryStats.Uid
        public long getTimeAtCpuSpeed(int cluster, int step, int which) {
            LongSamplingCounter[] cpuSpeedTimesUs;
            LongSamplingCounter c;
            LongSamplingCounter[][] longSamplingCounterArr = this.mCpuClusterSpeedTimesUs;
            if (longSamplingCounterArr != null && cluster >= 0 && cluster < longSamplingCounterArr.length && (cpuSpeedTimesUs = longSamplingCounterArr[cluster]) != null && step >= 0 && step < cpuSpeedTimesUs.length && (c = cpuSpeedTimesUs[step]) != null) {
                return c.getCountLocked(which);
            }
            return 0L;
        }

        public void noteMobileRadioApWakeupLocked() {
            if (this.mMobileRadioApWakeupCount == null) {
                this.mMobileRadioApWakeupCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            }
            this.mMobileRadioApWakeupCount.addCountLocked(1L);
        }

        @Override // android.os.BatteryStats.Uid
        public long getMobileRadioApWakeupCount(int which) {
            LongSamplingCounter longSamplingCounter = this.mMobileRadioApWakeupCount;
            if (longSamplingCounter != null) {
                return longSamplingCounter.getCountLocked(which);
            }
            return 0L;
        }

        public void noteWifiRadioApWakeupLocked() {
            if (this.mWifiRadioApWakeupCount == null) {
                this.mWifiRadioApWakeupCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            }
            this.mWifiRadioApWakeupCount.addCountLocked(1L);
        }

        @Override // android.os.BatteryStats.Uid
        public long getWifiRadioApWakeupCount(int which) {
            LongSamplingCounter longSamplingCounter = this.mWifiRadioApWakeupCount;
            if (longSamplingCounter != null) {
                return longSamplingCounter.getCountLocked(which);
            }
            return 0L;
        }

        @Override // android.os.BatteryStats.Uid
        public void getDeferredJobsCheckinLineLocked(StringBuilder sb, int which) {
            sb.setLength(0);
            int deferredEventCount = this.mJobsDeferredEventCount.getCountLocked(which);
            if (deferredEventCount == 0) {
                return;
            }
            int deferredCount = this.mJobsDeferredCount.getCountLocked(which);
            long totalLatency = this.mJobsFreshnessTimeMs.getCountLocked(which);
            sb.append(deferredEventCount);
            sb.append(',');
            sb.append(deferredCount);
            sb.append(',');
            sb.append(totalLatency);
            for (int i = 0; i < BatteryStats.JOB_FRESHNESS_BUCKETS.length; i++) {
                if (this.mJobsFreshnessBuckets[i] == null) {
                    sb.append(",0");
                } else {
                    sb.append(SmsManager.REGEX_PREFIX_DELIMITER);
                    sb.append(this.mJobsFreshnessBuckets[i].getCountLocked(which));
                }
            }
        }

        @Override // android.os.BatteryStats.Uid
        public void getDeferredJobsLineLocked(StringBuilder sb, int which) {
            sb.setLength(0);
            int deferredEventCount = this.mJobsDeferredEventCount.getCountLocked(which);
            if (deferredEventCount == 0) {
                return;
            }
            int deferredCount = this.mJobsDeferredCount.getCountLocked(which);
            long totalLatency = this.mJobsFreshnessTimeMs.getCountLocked(which);
            sb.append("times=");
            sb.append(deferredEventCount);
            sb.append(", ");
            sb.append("count=");
            sb.append(deferredCount);
            sb.append(", ");
            sb.append("totalLatencyMs=");
            sb.append(totalLatency);
            sb.append(", ");
            for (int i = 0; i < BatteryStats.JOB_FRESHNESS_BUCKETS.length; i++) {
                sb.append("<");
                sb.append(BatteryStats.JOB_FRESHNESS_BUCKETS[i]);
                sb.append("ms=");
                Counter[] counterArr = this.mJobsFreshnessBuckets;
                if (counterArr[i] == null) {
                    sb.append(WifiEnterpriseConfig.ENGINE_DISABLE);
                } else {
                    sb.append(counterArr[i].getCountLocked(which));
                }
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
        }

        void initNetworkActivityLocked() {
            BatteryStatsImpl.detachIfNotNull(this.mNetworkByteActivityCounters);
            this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
            BatteryStatsImpl.detachIfNotNull(this.mNetworkPacketActivityCounters);
            this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
            for (int i = 0; i < 10; i++) {
                this.mNetworkByteActivityCounters[i] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
                this.mNetworkPacketActivityCounters[i] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            }
            BatteryStatsImpl.detachIfNotNull(this.mMobileRadioActiveTime);
            this.mMobileRadioActiveTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
            BatteryStatsImpl.detachIfNotNull(this.mMobileRadioActiveCount);
            this.mMobileRadioActiveCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase);
        }

        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public boolean reset(long uptime, long realtime) {
            StopwatchTimer stopwatchTimer;
            StopwatchTimer stopwatchTimer2;
            DualTimer dualTimer;
            StopwatchTimer stopwatchTimer3;
            StopwatchTimer[] stopwatchTimerArr;
            boolean active = false;
            this.mOnBatteryBackgroundTimeBase.init(uptime, realtime);
            this.mOnBatteryScreenOffBackgroundTimeBase.init(uptime, realtime);
            if (this.mWifiRunningTimer != null) {
                boolean active2 = false | (!stopwatchTimer.reset(false));
                active = active2 | this.mWifiRunning;
            }
            if (this.mFullWifiLockTimer != null) {
                active = active | (!stopwatchTimer2.reset(false)) | this.mFullWifiLockOut;
            }
            if (this.mWifiScanTimer != null) {
                active = active | (!dualTimer.reset(false)) | this.mWifiScanStarted;
            }
            if (this.mWifiBatchedScanTimer != null) {
                for (int i = 0; i < 5; i++) {
                    if (this.mWifiBatchedScanTimer[i] != null) {
                        active |= !stopwatchTimerArr[i].reset(false);
                    }
                }
                int i2 = this.mWifiBatchedScanBinStarted;
                active |= i2 != -1;
            }
            if (this.mWifiMulticastTimer != null) {
                active = active | (!stopwatchTimer3.reset(false)) | (this.mWifiMulticastWakelockCount > 0);
            }
            boolean active3 = active | (!BatteryStatsImpl.resetIfNotNull(this.mAudioTurnedOnTimer, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mVideoTurnedOnTimer, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mFlashlightTurnedOnTimer, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mCameraTurnedOnTimer, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mForegroundActivityTimer, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mForegroundServiceTimer, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mAggregatedPartialWakelockTimer, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mBluetoothScanTimer, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mBluetoothUnoptimizedScanTimer, false));
            BatteryStatsImpl.resetIfNotNull(this.mBluetoothScanResultCounter, false);
            BatteryStatsImpl.resetIfNotNull(this.mBluetoothScanResultBgCounter, false);
            if (this.mProcessStateTimer != null) {
                for (int i3 = 0; i3 < 7; i3++) {
                    active3 |= !BatteryStatsImpl.resetIfNotNull(this.mProcessStateTimer[i3], false);
                }
                int i4 = this.mProcessState;
                active3 |= i4 != 21;
            }
            BatchTimer batchTimer = this.mVibratorOnTimer;
            if (batchTimer != null) {
                if (batchTimer.reset(false)) {
                    this.mVibratorOnTimer.detach();
                    this.mVibratorOnTimer = null;
                } else {
                    active3 = true;
                }
            }
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mUserActivityCounters, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mNetworkByteActivityCounters, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mNetworkPacketActivityCounters, false);
            BatteryStatsImpl.resetIfNotNull(this.mMobileRadioActiveTime, false);
            BatteryStatsImpl.resetIfNotNull(this.mMobileRadioActiveCount, false);
            BatteryStatsImpl.resetIfNotNull(this.mWifiControllerActivity, false);
            BatteryStatsImpl.resetIfNotNull(this.mBluetoothControllerActivity, false);
            BatteryStatsImpl.resetIfNotNull(this.mModemControllerActivity, false);
            BatteryStatsImpl.resetIfNotNull(this.mUserCpuTime, false);
            BatteryStatsImpl.resetIfNotNull(this.mSystemCpuTime, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[][]) this.mCpuClusterSpeedTimesUs, false);
            BatteryStatsImpl.resetIfNotNull(this.mCpuFreqTimeMs, false);
            BatteryStatsImpl.resetIfNotNull(this.mScreenOffCpuFreqTimeMs, false);
            BatteryStatsImpl.resetIfNotNull(this.mCpuActiveTimeMs, false);
            BatteryStatsImpl.resetIfNotNull(this.mCpuClusterTimesMs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mProcStateTimeMs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mProcStateScreenOffTimeMs, false);
            BatteryStatsImpl.resetIfNotNull(this.mMobileRadioApWakeupCount, false);
            BatteryStatsImpl.resetIfNotNull(this.mWifiRadioApWakeupCount, false);
            ArrayMap<String, Wakelock> wakeStats = this.mWakelockStats.getMap();
            for (int iw = wakeStats.size() - 1; iw >= 0; iw--) {
                Wakelock wl = wakeStats.valueAt(iw);
                if (wl.reset()) {
                    wakeStats.removeAt(iw);
                } else {
                    active3 = true;
                }
            }
            this.mWakelockStats.cleanup();
            ArrayMap<String, DualTimer> syncStats = this.mSyncStats.getMap();
            for (int is = syncStats.size() - 1; is >= 0; is--) {
                DualTimer timer = syncStats.valueAt(is);
                if (timer.reset(false)) {
                    syncStats.removeAt(is);
                    timer.detach();
                } else {
                    active3 = true;
                }
            }
            this.mSyncStats.cleanup();
            ArrayMap<String, DualTimer> jobStats = this.mJobStats.getMap();
            for (int ij = jobStats.size() - 1; ij >= 0; ij--) {
                DualTimer timer2 = jobStats.valueAt(ij);
                if (timer2.reset(false)) {
                    jobStats.removeAt(ij);
                    timer2.detach();
                } else {
                    active3 = true;
                }
            }
            this.mJobStats.cleanup();
            this.mJobCompletions.clear();
            BatteryStatsImpl.resetIfNotNull(this.mJobsDeferredEventCount, false);
            BatteryStatsImpl.resetIfNotNull(this.mJobsDeferredCount, false);
            BatteryStatsImpl.resetIfNotNull(this.mJobsFreshnessTimeMs, false);
            BatteryStatsImpl.resetIfNotNull((TimeBaseObs[]) this.mJobsFreshnessBuckets, false);
            for (int ise = this.mSensorStats.size() - 1; ise >= 0; ise--) {
                Sensor s = this.mSensorStats.valueAt(ise);
                if (s.reset()) {
                    this.mSensorStats.removeAt(ise);
                } else {
                    active3 = true;
                }
            }
            for (int ip = this.mProcessStats.size() - 1; ip >= 0; ip--) {
                Proc proc = this.mProcessStats.valueAt(ip);
                proc.detach();
            }
            this.mProcessStats.clear();
            for (int i5 = this.mPids.size() - 1; i5 >= 0; i5--) {
                BatteryStats.Uid.Pid pid = this.mPids.valueAt(i5);
                if (pid.mWakeNesting > 0) {
                    active3 = true;
                } else {
                    this.mPids.removeAt(i5);
                }
            }
            for (int i6 = this.mPackageStats.size() - 1; i6 >= 0; i6--) {
                Pkg p = this.mPackageStats.valueAt(i6);
                p.detach();
            }
            this.mPackageStats.clear();
            this.mLastStepSystemTime = 0L;
            this.mLastStepUserTime = 0L;
            this.mCurStepSystemTime = 0L;
            this.mCurStepUserTime = 0L;
            return !active3;
        }

        void detachFromTimeBase() {
            BatteryStatsImpl.detachIfNotNull(this.mWifiRunningTimer);
            BatteryStatsImpl.detachIfNotNull(this.mFullWifiLockTimer);
            BatteryStatsImpl.detachIfNotNull(this.mWifiScanTimer);
            BatteryStatsImpl.detachIfNotNull(this.mWifiBatchedScanTimer);
            BatteryStatsImpl.detachIfNotNull(this.mWifiMulticastTimer);
            BatteryStatsImpl.detachIfNotNull(this.mAudioTurnedOnTimer);
            BatteryStatsImpl.detachIfNotNull(this.mVideoTurnedOnTimer);
            BatteryStatsImpl.detachIfNotNull(this.mFlashlightTurnedOnTimer);
            BatteryStatsImpl.detachIfNotNull(this.mCameraTurnedOnTimer);
            BatteryStatsImpl.detachIfNotNull(this.mForegroundActivityTimer);
            BatteryStatsImpl.detachIfNotNull(this.mForegroundServiceTimer);
            BatteryStatsImpl.detachIfNotNull(this.mAggregatedPartialWakelockTimer);
            BatteryStatsImpl.detachIfNotNull(this.mBluetoothScanTimer);
            BatteryStatsImpl.detachIfNotNull(this.mBluetoothUnoptimizedScanTimer);
            BatteryStatsImpl.detachIfNotNull(this.mBluetoothScanResultCounter);
            BatteryStatsImpl.detachIfNotNull(this.mBluetoothScanResultBgCounter);
            BatteryStatsImpl.detachIfNotNull(this.mProcessStateTimer);
            BatteryStatsImpl.detachIfNotNull(this.mVibratorOnTimer);
            BatteryStatsImpl.detachIfNotNull(this.mUserActivityCounters);
            BatteryStatsImpl.detachIfNotNull(this.mNetworkByteActivityCounters);
            BatteryStatsImpl.detachIfNotNull(this.mNetworkPacketActivityCounters);
            BatteryStatsImpl.detachIfNotNull(this.mMobileRadioActiveTime);
            BatteryStatsImpl.detachIfNotNull(this.mMobileRadioActiveCount);
            BatteryStatsImpl.detachIfNotNull(this.mMobileRadioApWakeupCount);
            BatteryStatsImpl.detachIfNotNull(this.mWifiRadioApWakeupCount);
            BatteryStatsImpl.detachIfNotNull(this.mWifiControllerActivity);
            BatteryStatsImpl.detachIfNotNull(this.mBluetoothControllerActivity);
            BatteryStatsImpl.detachIfNotNull(this.mModemControllerActivity);
            this.mPids.clear();
            BatteryStatsImpl.detachIfNotNull(this.mUserCpuTime);
            BatteryStatsImpl.detachIfNotNull(this.mSystemCpuTime);
            BatteryStatsImpl.detachIfNotNull(this.mCpuClusterSpeedTimesUs);
            BatteryStatsImpl.detachIfNotNull(this.mCpuActiveTimeMs);
            BatteryStatsImpl.detachIfNotNull(this.mCpuFreqTimeMs);
            BatteryStatsImpl.detachIfNotNull(this.mScreenOffCpuFreqTimeMs);
            BatteryStatsImpl.detachIfNotNull(this.mCpuClusterTimesMs);
            BatteryStatsImpl.detachIfNotNull(this.mProcStateTimeMs);
            BatteryStatsImpl.detachIfNotNull(this.mProcStateScreenOffTimeMs);
            ArrayMap<String, Wakelock> wakeStats = this.mWakelockStats.getMap();
            for (int iw = wakeStats.size() - 1; iw >= 0; iw--) {
                Wakelock wl = wakeStats.valueAt(iw);
                wl.detachFromTimeBase();
            }
            ArrayMap<String, DualTimer> syncStats = this.mSyncStats.getMap();
            for (int is = syncStats.size() - 1; is >= 0; is--) {
                DualTimer timer = syncStats.valueAt(is);
                BatteryStatsImpl.detachIfNotNull(timer);
            }
            ArrayMap<String, DualTimer> jobStats = this.mJobStats.getMap();
            for (int ij = jobStats.size() - 1; ij >= 0; ij--) {
                DualTimer timer2 = jobStats.valueAt(ij);
                BatteryStatsImpl.detachIfNotNull(timer2);
            }
            BatteryStatsImpl.detachIfNotNull(this.mJobsDeferredEventCount);
            BatteryStatsImpl.detachIfNotNull(this.mJobsDeferredCount);
            BatteryStatsImpl.detachIfNotNull(this.mJobsFreshnessTimeMs);
            BatteryStatsImpl.detachIfNotNull(this.mJobsFreshnessBuckets);
            for (int ise = this.mSensorStats.size() - 1; ise >= 0; ise--) {
                Sensor s = this.mSensorStats.valueAt(ise);
                s.detachFromTimeBase();
            }
            for (int ip = this.mProcessStats.size() - 1; ip >= 0; ip--) {
                Proc proc = this.mProcessStats.valueAt(ip);
                proc.detach();
            }
            this.mProcessStats.clear();
            for (int i = this.mPackageStats.size() - 1; i >= 0; i--) {
                Pkg p = this.mPackageStats.valueAt(i);
                p.detach();
            }
            this.mPackageStats.clear();
        }

        void writeJobCompletionsToParcelLocked(Parcel out) {
            int NJC = this.mJobCompletions.size();
            out.writeInt(NJC);
            for (int ijc = 0; ijc < NJC; ijc++) {
                out.writeString(this.mJobCompletions.keyAt(ijc));
                SparseIntArray types = this.mJobCompletions.valueAt(ijc);
                int NT = types.size();
                out.writeInt(NT);
                for (int it = 0; it < NT; it++) {
                    out.writeInt(types.keyAt(it));
                    out.writeInt(types.valueAt(it));
                }
            }
        }

        void writeToParcelLocked(Parcel out, long uptimeUs, long elapsedRealtimeUs) {
            LongSamplingCounterArray[] longSamplingCounterArrayArr;
            LongSamplingCounterArray[] longSamplingCounterArrayArr2;
            ArrayMap<String, Wakelock> wakeStats;
            int NW;
            ArrayMap<String, DualTimer> syncStats;
            ArrayMap<String, DualTimer> syncStats2;
            this.mOnBatteryBackgroundTimeBase.writeToParcel(out, uptimeUs, elapsedRealtimeUs);
            this.mOnBatteryScreenOffBackgroundTimeBase.writeToParcel(out, uptimeUs, elapsedRealtimeUs);
            ArrayMap<String, Wakelock> wakeStats2 = this.mWakelockStats.getMap();
            int NW2 = wakeStats2.size();
            out.writeInt(NW2);
            for (int iw = 0; iw < NW2; iw++) {
                out.writeString(wakeStats2.keyAt(iw));
                Wakelock wakelock = wakeStats2.valueAt(iw);
                wakelock.writeToParcelLocked(out, elapsedRealtimeUs);
            }
            ArrayMap<String, DualTimer> syncStats3 = this.mSyncStats.getMap();
            int NS = syncStats3.size();
            out.writeInt(NS);
            for (int is = 0; is < NS; is++) {
                out.writeString(syncStats3.keyAt(is));
                DualTimer timer = syncStats3.valueAt(is);
                Timer.writeTimerToParcel(out, timer, elapsedRealtimeUs);
            }
            ArrayMap<String, DualTimer> jobStats = this.mJobStats.getMap();
            int NJ = jobStats.size();
            out.writeInt(NJ);
            for (int ij = 0; ij < NJ; ij++) {
                out.writeString(jobStats.keyAt(ij));
                DualTimer timer2 = jobStats.valueAt(ij);
                Timer.writeTimerToParcel(out, timer2, elapsedRealtimeUs);
            }
            writeJobCompletionsToParcelLocked(out);
            this.mJobsDeferredEventCount.writeToParcel(out);
            this.mJobsDeferredCount.writeToParcel(out);
            this.mJobsFreshnessTimeMs.writeToParcel(out);
            for (int i = 0; i < BatteryStats.JOB_FRESHNESS_BUCKETS.length; i++) {
                Counter.writeCounterToParcel(out, this.mJobsFreshnessBuckets[i]);
            }
            int NSE = this.mSensorStats.size();
            out.writeInt(NSE);
            for (int ise = 0; ise < NSE; ise++) {
                out.writeInt(this.mSensorStats.keyAt(ise));
                Sensor sensor = this.mSensorStats.valueAt(ise);
                sensor.writeToParcelLocked(out, elapsedRealtimeUs);
            }
            int NP = this.mProcessStats.size();
            out.writeInt(NP);
            for (int ip = 0; ip < NP; ip++) {
                out.writeString(this.mProcessStats.keyAt(ip));
                Proc proc = this.mProcessStats.valueAt(ip);
                proc.writeToParcelLocked(out);
            }
            out.writeInt(this.mPackageStats.size());
            for (Map.Entry<String, Pkg> pkgEntry : this.mPackageStats.entrySet()) {
                out.writeString(pkgEntry.getKey());
                Pkg pkg = pkgEntry.getValue();
                pkg.writeToParcelLocked(out);
            }
            int i2 = 0;
            if (this.mWifiRunningTimer != null) {
                out.writeInt(1);
                this.mWifiRunningTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mFullWifiLockTimer != null) {
                out.writeInt(1);
                this.mFullWifiLockTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mWifiScanTimer != null) {
                out.writeInt(1);
                this.mWifiScanTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            for (int i3 = 0; i3 < 5; i3++) {
                if (this.mWifiBatchedScanTimer[i3] != null) {
                    out.writeInt(1);
                    this.mWifiBatchedScanTimer[i3].writeToParcel(out, elapsedRealtimeUs);
                } else {
                    out.writeInt(0);
                }
            }
            if (this.mWifiMulticastTimer != null) {
                out.writeInt(1);
                this.mWifiMulticastTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mAudioTurnedOnTimer != null) {
                out.writeInt(1);
                this.mAudioTurnedOnTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mVideoTurnedOnTimer != null) {
                out.writeInt(1);
                this.mVideoTurnedOnTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mFlashlightTurnedOnTimer != null) {
                out.writeInt(1);
                this.mFlashlightTurnedOnTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mCameraTurnedOnTimer != null) {
                out.writeInt(1);
                this.mCameraTurnedOnTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mForegroundActivityTimer != null) {
                out.writeInt(1);
                this.mForegroundActivityTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mForegroundServiceTimer != null) {
                out.writeInt(1);
                this.mForegroundServiceTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mAggregatedPartialWakelockTimer != null) {
                out.writeInt(1);
                this.mAggregatedPartialWakelockTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mBluetoothScanTimer != null) {
                out.writeInt(1);
                this.mBluetoothScanTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mBluetoothUnoptimizedScanTimer != null) {
                out.writeInt(1);
                this.mBluetoothUnoptimizedScanTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mBluetoothScanResultCounter != null) {
                out.writeInt(1);
                this.mBluetoothScanResultCounter.writeToParcel(out);
            } else {
                out.writeInt(0);
            }
            if (this.mBluetoothScanResultBgCounter != null) {
                out.writeInt(1);
                this.mBluetoothScanResultBgCounter.writeToParcel(out);
            } else {
                out.writeInt(0);
            }
            for (int i4 = 0; i4 < 7; i4++) {
                if (this.mProcessStateTimer[i4] != null) {
                    out.writeInt(1);
                    this.mProcessStateTimer[i4].writeToParcel(out, elapsedRealtimeUs);
                } else {
                    out.writeInt(0);
                }
            }
            if (this.mVibratorOnTimer != null) {
                out.writeInt(1);
                this.mVibratorOnTimer.writeToParcel(out, elapsedRealtimeUs);
            } else {
                out.writeInt(0);
            }
            if (this.mUserActivityCounters != null) {
                out.writeInt(1);
                for (int i5 = 0; i5 < NUM_USER_ACTIVITY_TYPES; i5++) {
                    this.mUserActivityCounters[i5].writeToParcel(out);
                }
            } else {
                out.writeInt(0);
            }
            if (this.mNetworkByteActivityCounters != null) {
                out.writeInt(1);
                for (int i6 = 0; i6 < 10; i6++) {
                    this.mNetworkByteActivityCounters[i6].writeToParcel(out);
                    this.mNetworkPacketActivityCounters[i6].writeToParcel(out);
                }
                this.mMobileRadioActiveTime.writeToParcel(out);
                this.mMobileRadioActiveCount.writeToParcel(out);
            } else {
                out.writeInt(0);
            }
            if (this.mWifiControllerActivity != null) {
                out.writeInt(1);
                this.mWifiControllerActivity.writeToParcel(out, 0);
            } else {
                out.writeInt(0);
            }
            if (this.mBluetoothControllerActivity != null) {
                out.writeInt(1);
                this.mBluetoothControllerActivity.writeToParcel(out, 0);
            } else {
                out.writeInt(0);
            }
            if (this.mModemControllerActivity != null) {
                out.writeInt(1);
                this.mModemControllerActivity.writeToParcel(out, 0);
            } else {
                out.writeInt(0);
            }
            this.mUserCpuTime.writeToParcel(out);
            this.mSystemCpuTime.writeToParcel(out);
            if (this.mCpuClusterSpeedTimesUs != null) {
                out.writeInt(1);
                out.writeInt(this.mCpuClusterSpeedTimesUs.length);
                LongSamplingCounter[][] longSamplingCounterArr = this.mCpuClusterSpeedTimesUs;
                int length = longSamplingCounterArr.length;
                while (i2 < length) {
                    LongSamplingCounter[] cpuSpeeds = longSamplingCounterArr[i2];
                    if (cpuSpeeds != null) {
                        wakeStats = wakeStats2;
                        out.writeInt(1);
                        out.writeInt(cpuSpeeds.length);
                        int length2 = cpuSpeeds.length;
                        NW = NW2;
                        int NW3 = 0;
                        while (NW3 < length2) {
                            int i7 = length2;
                            LongSamplingCounter c = cpuSpeeds[NW3];
                            if (c != null) {
                                syncStats2 = syncStats3;
                                out.writeInt(1);
                                c.writeToParcel(out);
                            } else {
                                syncStats2 = syncStats3;
                                out.writeInt(0);
                            }
                            NW3++;
                            length2 = i7;
                            syncStats3 = syncStats2;
                        }
                        syncStats = syncStats3;
                    } else {
                        wakeStats = wakeStats2;
                        NW = NW2;
                        syncStats = syncStats3;
                        out.writeInt(0);
                    }
                    i2++;
                    wakeStats2 = wakeStats;
                    NW2 = NW;
                    syncStats3 = syncStats;
                }
            } else {
                out.writeInt(0);
            }
            LongSamplingCounterArray.writeToParcel(out, this.mCpuFreqTimeMs);
            LongSamplingCounterArray.writeToParcel(out, this.mScreenOffCpuFreqTimeMs);
            this.mCpuActiveTimeMs.writeToParcel(out);
            this.mCpuClusterTimesMs.writeToParcel(out);
            LongSamplingCounterArray[] longSamplingCounterArrayArr3 = this.mProcStateTimeMs;
            if (longSamplingCounterArrayArr3 != null) {
                out.writeInt(longSamplingCounterArrayArr3.length);
                for (LongSamplingCounterArray counters : this.mProcStateTimeMs) {
                    LongSamplingCounterArray.writeToParcel(out, counters);
                }
            } else {
                out.writeInt(0);
            }
            LongSamplingCounterArray[] longSamplingCounterArrayArr4 = this.mProcStateScreenOffTimeMs;
            if (longSamplingCounterArrayArr4 != null) {
                out.writeInt(longSamplingCounterArrayArr4.length);
                for (LongSamplingCounterArray counters2 : this.mProcStateScreenOffTimeMs) {
                    LongSamplingCounterArray.writeToParcel(out, counters2);
                }
            } else {
                out.writeInt(0);
            }
            if (this.mMobileRadioApWakeupCount != null) {
                out.writeInt(1);
                this.mMobileRadioApWakeupCount.writeToParcel(out);
            } else {
                out.writeInt(0);
            }
            if (this.mWifiRadioApWakeupCount != null) {
                out.writeInt(1);
                this.mWifiRadioApWakeupCount.writeToParcel(out);
                return;
            }
            out.writeInt(0);
        }

        void readJobCompletionsFromParcelLocked(Parcel in) {
            int numJobCompletions = in.readInt();
            this.mJobCompletions.clear();
            for (int j = 0; j < numJobCompletions; j++) {
                String jobName = in.readString();
                int numTypes = in.readInt();
                if (numTypes > 0) {
                    SparseIntArray types = new SparseIntArray();
                    for (int k = 0; k < numTypes; k++) {
                        int type = in.readInt();
                        int count = in.readInt();
                        types.put(type, count);
                    }
                    this.mJobCompletions.put(jobName, types);
                }
            }
        }

        void readFromParcelLocked(TimeBase timeBase, TimeBase screenOffTimeBase, Parcel in) {
            StopwatchTimer stopwatchTimer;
            int i;
            int numJobs;
            int numWakelocks;
            this.mOnBatteryBackgroundTimeBase.readFromParcel(in);
            this.mOnBatteryScreenOffBackgroundTimeBase.readFromParcel(in);
            int numWakelocks2 = in.readInt();
            this.mWakelockStats.clear();
            for (int j = 0; j < numWakelocks2; j++) {
                String wakelockName = in.readString();
                Wakelock wakelock = new Wakelock(this.mBsi, this);
                wakelock.readFromParcelLocked(timeBase, screenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase, in);
                this.mWakelockStats.add(wakelockName, wakelock);
            }
            int numSyncs = in.readInt();
            this.mSyncStats.clear();
            int j2 = 0;
            while (j2 < numSyncs) {
                String syncName = in.readString();
                if (in.readInt() != 0) {
                    numWakelocks = numWakelocks2;
                    this.mSyncStats.add(syncName, new DualTimer(this.mBsi.mClocks, this, 13, null, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in));
                } else {
                    numWakelocks = numWakelocks2;
                }
                j2++;
                numWakelocks2 = numWakelocks;
            }
            int numJobs2 = in.readInt();
            this.mJobStats.clear();
            int j3 = 0;
            while (j3 < numJobs2) {
                String jobName = in.readString();
                if (in.readInt() != 0) {
                    numJobs = numJobs2;
                    this.mJobStats.add(jobName, new DualTimer(this.mBsi.mClocks, this, 14, null, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in));
                } else {
                    numJobs = numJobs2;
                }
                j3++;
                numJobs2 = numJobs;
            }
            readJobCompletionsFromParcelLocked(in);
            this.mJobsDeferredEventCount = new Counter(this.mBsi.mOnBatteryTimeBase, in);
            this.mJobsDeferredCount = new Counter(this.mBsi.mOnBatteryTimeBase, in);
            this.mJobsFreshnessTimeMs = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
            for (int i2 = 0; i2 < BatteryStats.JOB_FRESHNESS_BUCKETS.length; i2++) {
                this.mJobsFreshnessBuckets[i2] = Counter.readCounterFromParcel(this.mBsi.mOnBatteryTimeBase, in);
            }
            int numSensors = in.readInt();
            this.mSensorStats.clear();
            for (int k = 0; k < numSensors; k++) {
                int sensorNumber = in.readInt();
                Sensor sensor = new Sensor(this.mBsi, this, sensorNumber);
                sensor.readFromParcelLocked(this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in);
                this.mSensorStats.put(sensorNumber, sensor);
            }
            int numProcs = in.readInt();
            this.mProcessStats.clear();
            for (int k2 = 0; k2 < numProcs; k2++) {
                String processName = in.readString();
                Proc proc = new Proc(this.mBsi, processName);
                proc.readFromParcelLocked(in);
                this.mProcessStats.put(processName, proc);
            }
            int numPkgs = in.readInt();
            this.mPackageStats.clear();
            for (int l = 0; l < numPkgs; l++) {
                String packageName = in.readString();
                Pkg pkg = new Pkg(this.mBsi);
                pkg.readFromParcelLocked(in);
                this.mPackageStats.put(packageName, pkg);
            }
            this.mWifiRunning = false;
            if (in.readInt() != 0) {
                stopwatchTimer = null;
                this.mWifiRunningTimer = new StopwatchTimer(this.mBsi.mClocks, this, 4, this.mBsi.mWifiRunningTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                stopwatchTimer = null;
                this.mWifiRunningTimer = null;
            }
            this.mFullWifiLockOut = false;
            if (in.readInt() != 0) {
                this.mFullWifiLockTimer = new StopwatchTimer(this.mBsi.mClocks, this, 5, this.mBsi.mFullWifiLockTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mFullWifiLockTimer = stopwatchTimer;
            }
            this.mWifiScanStarted = false;
            if (in.readInt() != 0) {
                i = 0;
                this.mWifiScanTimer = new DualTimer(this.mBsi.mClocks, this, 6, this.mBsi.mWifiScanTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in);
            } else {
                i = 0;
                this.mWifiScanTimer = null;
            }
            this.mWifiBatchedScanBinStarted = -1;
            for (int i3 = 0; i3 < 5; i3++) {
                if (in.readInt() != 0) {
                    makeWifiBatchedScanBin(i3, in);
                } else {
                    this.mWifiBatchedScanTimer[i3] = null;
                }
            }
            this.mWifiMulticastWakelockCount = i;
            if (in.readInt() != 0) {
                this.mWifiMulticastTimer = new StopwatchTimer(this.mBsi.mClocks, this, 7, this.mBsi.mWifiMulticastTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mWifiMulticastTimer = null;
            }
            if (in.readInt() != 0) {
                this.mAudioTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 15, this.mBsi.mAudioTurnedOnTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mAudioTurnedOnTimer = null;
            }
            if (in.readInt() != 0) {
                this.mVideoTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 8, this.mBsi.mVideoTurnedOnTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mVideoTurnedOnTimer = null;
            }
            if (in.readInt() != 0) {
                this.mFlashlightTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 16, this.mBsi.mFlashlightTurnedOnTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mFlashlightTurnedOnTimer = null;
            }
            if (in.readInt() != 0) {
                this.mCameraTurnedOnTimer = new StopwatchTimer(this.mBsi.mClocks, this, 17, this.mBsi.mCameraTurnedOnTimers, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mCameraTurnedOnTimer = null;
            }
            if (in.readInt() != 0) {
                this.mForegroundActivityTimer = new StopwatchTimer(this.mBsi.mClocks, this, 10, null, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mForegroundActivityTimer = null;
            }
            if (in.readInt() != 0) {
                this.mForegroundServiceTimer = new StopwatchTimer(this.mBsi.mClocks, this, 22, null, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mForegroundServiceTimer = null;
            }
            if (in.readInt() != 0) {
                this.mAggregatedPartialWakelockTimer = new DualTimer(this.mBsi.mClocks, this, 20, null, this.mBsi.mOnBatteryScreenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase, in);
            } else {
                this.mAggregatedPartialWakelockTimer = null;
            }
            if (in.readInt() != 0) {
                this.mBluetoothScanTimer = new DualTimer(this.mBsi.mClocks, this, 19, this.mBsi.mBluetoothScanOnTimers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in);
            } else {
                this.mBluetoothScanTimer = null;
            }
            if (in.readInt() != 0) {
                this.mBluetoothUnoptimizedScanTimer = new DualTimer(this.mBsi.mClocks, this, 21, null, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase, in);
            } else {
                this.mBluetoothUnoptimizedScanTimer = null;
            }
            if (in.readInt() != 0) {
                this.mBluetoothScanResultCounter = new Counter(this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mBluetoothScanResultCounter = null;
            }
            if (in.readInt() != 0) {
                this.mBluetoothScanResultBgCounter = new Counter(this.mOnBatteryBackgroundTimeBase, in);
            } else {
                this.mBluetoothScanResultBgCounter = null;
            }
            this.mProcessState = 21;
            for (int i4 = 0; i4 < 7; i4++) {
                if (in.readInt() != 0) {
                    makeProcessState(i4, in);
                } else {
                    this.mProcessStateTimer[i4] = null;
                }
            }
            int i5 = in.readInt();
            if (i5 != 0) {
                this.mVibratorOnTimer = new BatchTimer(this.mBsi.mClocks, this, 9, this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mVibratorOnTimer = null;
            }
            if (in.readInt() != 0) {
                this.mUserActivityCounters = new Counter[NUM_USER_ACTIVITY_TYPES];
                for (int i6 = 0; i6 < NUM_USER_ACTIVITY_TYPES; i6++) {
                    this.mUserActivityCounters[i6] = new Counter(this.mBsi.mOnBatteryTimeBase, in);
                }
            } else {
                this.mUserActivityCounters = null;
            }
            if (in.readInt() != 0) {
                this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
                this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
                for (int i7 = 0; i7 < 10; i7++) {
                    this.mNetworkByteActivityCounters[i7] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
                    this.mNetworkPacketActivityCounters[i7] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
                }
                this.mMobileRadioActiveTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
                this.mMobileRadioActiveCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mNetworkByteActivityCounters = null;
                this.mNetworkPacketActivityCounters = null;
            }
            if (in.readInt() != 0) {
                this.mWifiControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 1, in);
            } else {
                this.mWifiControllerActivity = null;
            }
            if (in.readInt() != 0) {
                this.mBluetoothControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 1, in);
            } else {
                this.mBluetoothControllerActivity = null;
            }
            if (in.readInt() != 0) {
                this.mModemControllerActivity = new ControllerActivityCounterImpl(this.mBsi.mOnBatteryTimeBase, 5, in);
            } else {
                this.mModemControllerActivity = null;
            }
            this.mUserCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
            this.mSystemCpuTime = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
            if (in.readInt() == 0) {
                this.mCpuClusterSpeedTimesUs = null;
            } else {
                int numCpuClusters = in.readInt();
                if (this.mBsi.mPowerProfile == null || this.mBsi.mPowerProfile.getNumCpuClusters() == numCpuClusters) {
                    this.mCpuClusterSpeedTimesUs = new LongSamplingCounter[numCpuClusters];
                    for (int cluster = 0; cluster < numCpuClusters; cluster++) {
                        if (in.readInt() != 0) {
                            int numSpeeds = in.readInt();
                            if (this.mBsi.mPowerProfile != null && this.mBsi.mPowerProfile.getNumSpeedStepsInCpuCluster(cluster) != numSpeeds) {
                                throw new ParcelFormatException("Incompatible number of cpu speeds");
                            }
                            LongSamplingCounter[] cpuSpeeds = new LongSamplingCounter[numSpeeds];
                            this.mCpuClusterSpeedTimesUs[cluster] = cpuSpeeds;
                            for (int speed = 0; speed < numSpeeds; speed++) {
                                if (in.readInt() != 0) {
                                    cpuSpeeds[speed] = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
                                }
                            }
                        } else {
                            this.mCpuClusterSpeedTimesUs[cluster] = null;
                        }
                    }
                } else {
                    throw new ParcelFormatException("Incompatible number of cpu clusters");
                }
            }
            this.mCpuFreqTimeMs = LongSamplingCounterArray.readFromParcel(in, this.mBsi.mOnBatteryTimeBase);
            this.mScreenOffCpuFreqTimeMs = LongSamplingCounterArray.readFromParcel(in, this.mBsi.mOnBatteryScreenOffTimeBase);
            this.mCpuActiveTimeMs = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
            this.mCpuClusterTimesMs = new LongSamplingCounterArray(this.mBsi.mOnBatteryTimeBase, in);
            int length = in.readInt();
            if (length == 7) {
                this.mProcStateTimeMs = new LongSamplingCounterArray[length];
                for (int procState = 0; procState < length; procState++) {
                    this.mProcStateTimeMs[procState] = LongSamplingCounterArray.readFromParcel(in, this.mBsi.mOnBatteryTimeBase);
                }
            } else {
                this.mProcStateTimeMs = null;
            }
            int length2 = in.readInt();
            if (length2 == 7) {
                this.mProcStateScreenOffTimeMs = new LongSamplingCounterArray[length2];
                for (int procState2 = 0; procState2 < length2; procState2++) {
                    this.mProcStateScreenOffTimeMs[procState2] = LongSamplingCounterArray.readFromParcel(in, this.mBsi.mOnBatteryScreenOffTimeBase);
                }
            } else {
                this.mProcStateScreenOffTimeMs = null;
            }
            if (in.readInt() != 0) {
                this.mMobileRadioApWakeupCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mMobileRadioApWakeupCount = null;
            }
            if (in.readInt() != 0) {
                this.mWifiRadioApWakeupCount = new LongSamplingCounter(this.mBsi.mOnBatteryTimeBase, in);
            } else {
                this.mWifiRadioApWakeupCount = null;
            }
        }

        public void noteJobsDeferredLocked(int numDeferred, long sinceLast) {
            this.mJobsDeferredEventCount.addAtomic(1);
            this.mJobsDeferredCount.addAtomic(numDeferred);
            if (sinceLast != 0) {
                this.mJobsFreshnessTimeMs.addCountLocked(sinceLast);
                for (int i = 0; i < BatteryStats.JOB_FRESHNESS_BUCKETS.length; i++) {
                    if (sinceLast < BatteryStats.JOB_FRESHNESS_BUCKETS[i]) {
                        Counter[] counterArr = this.mJobsFreshnessBuckets;
                        if (counterArr[i] == null) {
                            counterArr[i] = new Counter(this.mBsi.mOnBatteryTimeBase);
                        }
                        this.mJobsFreshnessBuckets[i].addAtomic(1);
                        return;
                    }
                }
            }
        }

        /* loaded from: classes3.dex */
        public static class Wakelock extends BatteryStats.Uid.Wakelock {
            protected BatteryStatsImpl mBsi;
            StopwatchTimer mTimerDraw;
            StopwatchTimer mTimerFull;
            DualTimer mTimerPartial;
            StopwatchTimer mTimerWindow;
            protected Uid mUid;

            public Wakelock(BatteryStatsImpl bsi, Uid uid) {
                this.mBsi = bsi;
                this.mUid = uid;
            }

            private StopwatchTimer readStopwatchTimerFromParcel(int type, ArrayList<StopwatchTimer> pool, TimeBase timeBase, Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                return new StopwatchTimer(this.mBsi.mClocks, this.mUid, type, pool, timeBase, in);
            }

            private DualTimer readDualTimerFromParcel(int type, ArrayList<StopwatchTimer> pool, TimeBase timeBase, TimeBase bgTimeBase, Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                return new DualTimer(this.mBsi.mClocks, this.mUid, type, pool, timeBase, bgTimeBase, in);
            }

            boolean reset() {
                boolean wlactive = false | (!BatteryStatsImpl.resetIfNotNull(this.mTimerFull, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mTimerPartial, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mTimerWindow, false)) | (!BatteryStatsImpl.resetIfNotNull(this.mTimerDraw, false));
                if (!wlactive) {
                    BatteryStatsImpl.detachIfNotNull(this.mTimerFull);
                    this.mTimerFull = null;
                    BatteryStatsImpl.detachIfNotNull(this.mTimerPartial);
                    this.mTimerPartial = null;
                    BatteryStatsImpl.detachIfNotNull(this.mTimerWindow);
                    this.mTimerWindow = null;
                    BatteryStatsImpl.detachIfNotNull(this.mTimerDraw);
                    this.mTimerDraw = null;
                }
                return !wlactive;
            }

            void readFromParcelLocked(TimeBase timeBase, TimeBase screenOffTimeBase, TimeBase screenOffBgTimeBase, Parcel in) {
                this.mTimerPartial = readDualTimerFromParcel(0, this.mBsi.mPartialTimers, screenOffTimeBase, screenOffBgTimeBase, in);
                this.mTimerFull = readStopwatchTimerFromParcel(1, this.mBsi.mFullTimers, timeBase, in);
                this.mTimerWindow = readStopwatchTimerFromParcel(2, this.mBsi.mWindowTimers, timeBase, in);
                this.mTimerDraw = readStopwatchTimerFromParcel(18, this.mBsi.mDrawTimers, timeBase, in);
            }

            void writeToParcelLocked(Parcel out, long elapsedRealtimeUs) {
                Timer.writeTimerToParcel(out, this.mTimerPartial, elapsedRealtimeUs);
                Timer.writeTimerToParcel(out, this.mTimerFull, elapsedRealtimeUs);
                Timer.writeTimerToParcel(out, this.mTimerWindow, elapsedRealtimeUs);
                Timer.writeTimerToParcel(out, this.mTimerDraw, elapsedRealtimeUs);
            }

            @Override // android.os.BatteryStats.Uid.Wakelock
            @UnsupportedAppUsage
            public Timer getWakeTime(int type) {
                if (type != 0) {
                    if (type != 1) {
                        if (type != 2) {
                            if (type == 18) {
                                return this.mTimerDraw;
                            }
                            throw new IllegalArgumentException("type = " + type);
                        }
                        return this.mTimerWindow;
                    }
                    return this.mTimerFull;
                }
                return this.mTimerPartial;
            }

            public void detachFromTimeBase() {
                BatteryStatsImpl.detachIfNotNull(this.mTimerPartial);
                BatteryStatsImpl.detachIfNotNull(this.mTimerFull);
                BatteryStatsImpl.detachIfNotNull(this.mTimerWindow);
                BatteryStatsImpl.detachIfNotNull(this.mTimerDraw);
            }
        }

        /* loaded from: classes3.dex */
        public static class Sensor extends BatteryStats.Uid.Sensor {
            protected BatteryStatsImpl mBsi;
            final int mHandle;
            DualTimer mTimer;
            protected Uid mUid;

            public Sensor(BatteryStatsImpl bsi, Uid uid, int handle) {
                this.mBsi = bsi;
                this.mUid = uid;
                this.mHandle = handle;
            }

            private DualTimer readTimersFromParcel(TimeBase timeBase, TimeBase bgTimeBase, Parcel in) {
                if (in.readInt() == 0) {
                    return null;
                }
                ArrayList<StopwatchTimer> pool = this.mBsi.mSensorTimers.get(this.mHandle);
                if (pool == null) {
                    pool = new ArrayList<>();
                    this.mBsi.mSensorTimers.put(this.mHandle, pool);
                }
                return new DualTimer(this.mBsi.mClocks, this.mUid, 0, pool, timeBase, bgTimeBase, in);
            }

            boolean reset() {
                if (this.mTimer.reset(true)) {
                    this.mTimer = null;
                    return true;
                }
                return false;
            }

            void readFromParcelLocked(TimeBase timeBase, TimeBase bgTimeBase, Parcel in) {
                this.mTimer = readTimersFromParcel(timeBase, bgTimeBase, in);
            }

            void writeToParcelLocked(Parcel out, long elapsedRealtimeUs) {
                Timer.writeTimerToParcel(out, this.mTimer, elapsedRealtimeUs);
            }

            @Override // android.os.BatteryStats.Uid.Sensor
            @UnsupportedAppUsage
            public Timer getSensorTime() {
                return this.mTimer;
            }

            @Override // android.os.BatteryStats.Uid.Sensor
            public Timer getSensorBackgroundTime() {
                DualTimer dualTimer = this.mTimer;
                if (dualTimer == null) {
                    return null;
                }
                return dualTimer.getSubTimer();
            }

            @Override // android.os.BatteryStats.Uid.Sensor
            @UnsupportedAppUsage
            public int getHandle() {
                return this.mHandle;
            }

            public void detachFromTimeBase() {
                BatteryStatsImpl.detachIfNotNull(this.mTimer);
            }
        }

        /* loaded from: classes3.dex */
        public static class Proc extends BatteryStats.Uid.Proc implements TimeBaseObs {
            boolean mActive = true;
            protected BatteryStatsImpl mBsi;
            ArrayList<BatteryStats.Uid.Proc.ExcessivePower> mExcessivePower;
            long mForegroundTime;
            final String mName;
            int mNumAnrs;
            int mNumCrashes;
            int mStarts;
            long mSystemTime;
            long mUserTime;

            public Proc(BatteryStatsImpl bsi, String name) {
                this.mBsi = bsi;
                this.mName = name;
                this.mBsi.mOnBatteryTimeBase.add(this);
            }

            @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
            public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            }

            @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
            public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            }

            @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
            public boolean reset(boolean detachIfReset) {
                if (detachIfReset) {
                    detach();
                    return true;
                }
                return true;
            }

            @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
            public void detach() {
                this.mActive = false;
                this.mBsi.mOnBatteryTimeBase.remove(this);
            }

            @Override // android.os.BatteryStats.Uid.Proc
            public int countExcessivePowers() {
                ArrayList<BatteryStats.Uid.Proc.ExcessivePower> arrayList = this.mExcessivePower;
                if (arrayList != null) {
                    return arrayList.size();
                }
                return 0;
            }

            @Override // android.os.BatteryStats.Uid.Proc
            public BatteryStats.Uid.Proc.ExcessivePower getExcessivePower(int i) {
                ArrayList<BatteryStats.Uid.Proc.ExcessivePower> arrayList = this.mExcessivePower;
                if (arrayList != null) {
                    return arrayList.get(i);
                }
                return null;
            }

            public void addExcessiveCpu(long overTime, long usedTime) {
                if (this.mExcessivePower == null) {
                    this.mExcessivePower = new ArrayList<>();
                }
                BatteryStats.Uid.Proc.ExcessivePower ew = new BatteryStats.Uid.Proc.ExcessivePower();
                ew.type = 2;
                ew.overTime = overTime;
                ew.usedTime = usedTime;
                this.mExcessivePower.add(ew);
            }

            void writeExcessivePowerToParcelLocked(Parcel out) {
                ArrayList<BatteryStats.Uid.Proc.ExcessivePower> arrayList = this.mExcessivePower;
                if (arrayList == null) {
                    out.writeInt(0);
                    return;
                }
                int N = arrayList.size();
                out.writeInt(N);
                for (int i = 0; i < N; i++) {
                    BatteryStats.Uid.Proc.ExcessivePower ew = this.mExcessivePower.get(i);
                    out.writeInt(ew.type);
                    out.writeLong(ew.overTime);
                    out.writeLong(ew.usedTime);
                }
            }

            void readExcessivePowerFromParcelLocked(Parcel in) {
                int N = in.readInt();
                if (N == 0) {
                    this.mExcessivePower = null;
                } else if (N > 10000) {
                    throw new ParcelFormatException("File corrupt: too many excessive power entries " + N);
                } else {
                    this.mExcessivePower = new ArrayList<>();
                    for (int i = 0; i < N; i++) {
                        BatteryStats.Uid.Proc.ExcessivePower ew = new BatteryStats.Uid.Proc.ExcessivePower();
                        ew.type = in.readInt();
                        ew.overTime = in.readLong();
                        ew.usedTime = in.readLong();
                        this.mExcessivePower.add(ew);
                    }
                }
            }

            void writeToParcelLocked(Parcel out) {
                out.writeLong(this.mUserTime);
                out.writeLong(this.mSystemTime);
                out.writeLong(this.mForegroundTime);
                out.writeInt(this.mStarts);
                out.writeInt(this.mNumCrashes);
                out.writeInt(this.mNumAnrs);
                writeExcessivePowerToParcelLocked(out);
            }

            void readFromParcelLocked(Parcel in) {
                this.mUserTime = in.readLong();
                this.mSystemTime = in.readLong();
                this.mForegroundTime = in.readLong();
                this.mStarts = in.readInt();
                this.mNumCrashes = in.readInt();
                this.mNumAnrs = in.readInt();
                readExcessivePowerFromParcelLocked(in);
            }

            @UnsupportedAppUsage
            public void addCpuTimeLocked(int utime, int stime) {
                addCpuTimeLocked(utime, stime, this.mBsi.mOnBatteryTimeBase.isRunning());
            }

            public void addCpuTimeLocked(int utime, int stime, boolean isRunning) {
                if (isRunning) {
                    this.mUserTime += utime;
                    this.mSystemTime += stime;
                }
            }

            @UnsupportedAppUsage
            public void addForegroundTimeLocked(long ttime) {
                this.mForegroundTime += ttime;
            }

            @UnsupportedAppUsage
            public void incStartsLocked() {
                this.mStarts++;
            }

            public void incNumCrashesLocked() {
                this.mNumCrashes++;
            }

            public void incNumAnrsLocked() {
                this.mNumAnrs++;
            }

            @Override // android.os.BatteryStats.Uid.Proc
            public boolean isActive() {
                return this.mActive;
            }

            @Override // android.os.BatteryStats.Uid.Proc
            @UnsupportedAppUsage
            public long getUserTime(int which) {
                return this.mUserTime;
            }

            @Override // android.os.BatteryStats.Uid.Proc
            @UnsupportedAppUsage
            public long getSystemTime(int which) {
                return this.mSystemTime;
            }

            @Override // android.os.BatteryStats.Uid.Proc
            @UnsupportedAppUsage
            public long getForegroundTime(int which) {
                return this.mForegroundTime;
            }

            @Override // android.os.BatteryStats.Uid.Proc
            @UnsupportedAppUsage
            public int getStarts(int which) {
                return this.mStarts;
            }

            @Override // android.os.BatteryStats.Uid.Proc
            public int getNumCrashes(int which) {
                return this.mNumCrashes;
            }

            @Override // android.os.BatteryStats.Uid.Proc
            public int getNumAnrs(int which) {
                return this.mNumAnrs;
            }
        }

        /* loaded from: classes3.dex */
        public static class Pkg extends BatteryStats.Uid.Pkg implements TimeBaseObs {
            protected BatteryStatsImpl mBsi;
            ArrayMap<String, Counter> mWakeupAlarms = new ArrayMap<>();
            final ArrayMap<String, Serv> mServiceStats = new ArrayMap<>();

            public Pkg(BatteryStatsImpl bsi) {
                this.mBsi = bsi;
                this.mBsi.mOnBatteryScreenOffTimeBase.add(this);
            }

            @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
            public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            }

            @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
            public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            }

            @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
            public boolean reset(boolean detachIfReset) {
                if (detachIfReset) {
                    detach();
                    return true;
                }
                return true;
            }

            @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
            public void detach() {
                this.mBsi.mOnBatteryScreenOffTimeBase.remove(this);
                for (int j = this.mWakeupAlarms.size() - 1; j >= 0; j--) {
                    BatteryStatsImpl.detachIfNotNull(this.mWakeupAlarms.valueAt(j));
                }
                for (int j2 = this.mServiceStats.size() - 1; j2 >= 0; j2--) {
                    BatteryStatsImpl.detachIfNotNull(this.mServiceStats.valueAt(j2));
                }
            }

            void readFromParcelLocked(Parcel in) {
                int numWA = in.readInt();
                this.mWakeupAlarms.clear();
                for (int i = 0; i < numWA; i++) {
                    String tag = in.readString();
                    this.mWakeupAlarms.put(tag, new Counter(this.mBsi.mOnBatteryScreenOffTimeBase, in));
                }
                int numServs = in.readInt();
                this.mServiceStats.clear();
                for (int m = 0; m < numServs; m++) {
                    String serviceName = in.readString();
                    Serv serv = new Serv(this.mBsi);
                    this.mServiceStats.put(serviceName, serv);
                    serv.readFromParcelLocked(in);
                }
            }

            void writeToParcelLocked(Parcel out) {
                int numWA = this.mWakeupAlarms.size();
                out.writeInt(numWA);
                for (int i = 0; i < numWA; i++) {
                    out.writeString(this.mWakeupAlarms.keyAt(i));
                    this.mWakeupAlarms.valueAt(i).writeToParcel(out);
                }
                int NS = this.mServiceStats.size();
                out.writeInt(NS);
                for (int i2 = 0; i2 < NS; i2++) {
                    out.writeString(this.mServiceStats.keyAt(i2));
                    Serv serv = this.mServiceStats.valueAt(i2);
                    serv.writeToParcelLocked(out);
                }
            }

            @Override // android.os.BatteryStats.Uid.Pkg
            public ArrayMap<String, ? extends BatteryStats.Counter> getWakeupAlarmStats() {
                return this.mWakeupAlarms;
            }

            public void noteWakeupAlarmLocked(String tag) {
                Counter c = this.mWakeupAlarms.get(tag);
                if (c == null) {
                    c = new Counter(this.mBsi.mOnBatteryScreenOffTimeBase);
                    this.mWakeupAlarms.put(tag, c);
                }
                c.stepAtomic();
            }

            @Override // android.os.BatteryStats.Uid.Pkg
            public ArrayMap<String, ? extends BatteryStats.Uid.Pkg.Serv> getServiceStats() {
                return this.mServiceStats;
            }

            /* loaded from: classes3.dex */
            public static class Serv extends BatteryStats.Uid.Pkg.Serv implements TimeBaseObs {
                protected BatteryStatsImpl mBsi;
                protected boolean mLaunched;
                protected long mLaunchedSince;
                protected long mLaunchedTime;
                protected int mLaunches;
                protected Pkg mPkg;
                protected boolean mRunning;
                protected long mRunningSince;
                protected long mStartTime;
                protected int mStarts;

                public Serv(BatteryStatsImpl bsi) {
                    this.mBsi = bsi;
                    this.mBsi.mOnBatteryTimeBase.add(this);
                }

                @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
                public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
                }

                @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
                public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
                }

                @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
                public boolean reset(boolean detachIfReset) {
                    if (detachIfReset) {
                        detach();
                        return true;
                    }
                    return true;
                }

                @Override // com.android.internal.os.BatteryStatsImpl.TimeBaseObs
                public void detach() {
                    this.mBsi.mOnBatteryTimeBase.remove(this);
                }

                public void readFromParcelLocked(Parcel in) {
                    this.mStartTime = in.readLong();
                    this.mRunningSince = in.readLong();
                    this.mRunning = in.readInt() != 0;
                    this.mStarts = in.readInt();
                    this.mLaunchedTime = in.readLong();
                    this.mLaunchedSince = in.readLong();
                    this.mLaunched = in.readInt() != 0;
                    this.mLaunches = in.readInt();
                }

                public void writeToParcelLocked(Parcel out) {
                    out.writeLong(this.mStartTime);
                    out.writeLong(this.mRunningSince);
                    out.writeInt(this.mRunning ? 1 : 0);
                    out.writeInt(this.mStarts);
                    out.writeLong(this.mLaunchedTime);
                    out.writeLong(this.mLaunchedSince);
                    out.writeInt(this.mLaunched ? 1 : 0);
                    out.writeInt(this.mLaunches);
                }

                public long getLaunchTimeToNowLocked(long batteryUptime) {
                    return !this.mLaunched ? this.mLaunchedTime : (this.mLaunchedTime + batteryUptime) - this.mLaunchedSince;
                }

                public long getStartTimeToNowLocked(long batteryUptime) {
                    return !this.mRunning ? this.mStartTime : (this.mStartTime + batteryUptime) - this.mRunningSince;
                }

                @UnsupportedAppUsage
                public void startLaunchedLocked() {
                    if (!this.mLaunched) {
                        this.mLaunches++;
                        this.mLaunchedSince = this.mBsi.getBatteryUptimeLocked();
                        this.mLaunched = true;
                    }
                }

                @UnsupportedAppUsage
                public void stopLaunchedLocked() {
                    if (this.mLaunched) {
                        long time = this.mBsi.getBatteryUptimeLocked() - this.mLaunchedSince;
                        if (time > 0) {
                            this.mLaunchedTime += time;
                        } else {
                            this.mLaunches--;
                        }
                        this.mLaunched = false;
                    }
                }

                @UnsupportedAppUsage
                public void startRunningLocked() {
                    if (!this.mRunning) {
                        this.mStarts++;
                        this.mRunningSince = this.mBsi.getBatteryUptimeLocked();
                        this.mRunning = true;
                    }
                }

                @UnsupportedAppUsage
                public void stopRunningLocked() {
                    if (this.mRunning) {
                        long time = this.mBsi.getBatteryUptimeLocked() - this.mRunningSince;
                        if (time > 0) {
                            this.mStartTime += time;
                        } else {
                            this.mStarts--;
                        }
                        this.mRunning = false;
                    }
                }

                @UnsupportedAppUsage
                public BatteryStatsImpl getBatteryStats() {
                    return this.mBsi;
                }

                @Override // android.os.BatteryStats.Uid.Pkg.Serv
                public int getLaunches(int which) {
                    return this.mLaunches;
                }

                @Override // android.os.BatteryStats.Uid.Pkg.Serv
                public long getStartTime(long now, int which) {
                    return getStartTimeToNowLocked(now);
                }

                @Override // android.os.BatteryStats.Uid.Pkg.Serv
                public int getStarts(int which) {
                    return this.mStarts;
                }
            }

            final Serv newServiceStatsLocked() {
                return new Serv(this.mBsi);
            }
        }

        public Proc getProcessStatsLocked(String name) {
            Proc ps = this.mProcessStats.get(name);
            if (ps == null) {
                Proc ps2 = new Proc(this.mBsi, name);
                this.mProcessStats.put(name, ps2);
                return ps2;
            }
            return ps;
        }

        @GuardedBy({"mBsi"})
        public void updateUidProcessStateLocked(int procState) {
            boolean userAwareService = ActivityManager.isForegroundService(procState);
            int uidRunningState = BatteryStats.mapToInternalProcessState(procState);
            if (this.mProcessState == uidRunningState && userAwareService == this.mInForegroundService) {
                return;
            }
            long elapsedRealtimeMs = this.mBsi.mClocks.elapsedRealtime();
            if (this.mProcessState != uidRunningState) {
                long uptimeMs = this.mBsi.mClocks.uptimeMillis();
                int i = this.mProcessState;
                if (i != 21) {
                    this.mProcessStateTimer[i].stopRunningLocked(elapsedRealtimeMs);
                    if (this.mBsi.trackPerProcStateCpuTimes()) {
                        if (this.mBsi.mPendingUids.size() == 0) {
                            this.mBsi.mExternalSync.scheduleReadProcStateCpuTimes(this.mBsi.mOnBatteryTimeBase.isRunning(), this.mBsi.mOnBatteryScreenOffTimeBase.isRunning(), this.mBsi.mConstants.PROC_STATE_CPU_TIMES_READ_DELAY_MS);
                            BatteryStatsImpl.access$1708(this.mBsi);
                        } else {
                            BatteryStatsImpl.access$1808(this.mBsi);
                        }
                        if (this.mBsi.mPendingUids.indexOfKey(this.mUid) < 0 || ArrayUtils.contains(CRITICAL_PROC_STATES, this.mProcessState)) {
                            this.mBsi.mPendingUids.put(this.mUid, this.mProcessState);
                        }
                    } else {
                        this.mBsi.mPendingUids.clear();
                    }
                }
                this.mProcessState = uidRunningState;
                if (uidRunningState != 21) {
                    if (this.mProcessStateTimer[uidRunningState] == null) {
                        makeProcessState(uidRunningState, null);
                    }
                    this.mProcessStateTimer[uidRunningState].startRunningLocked(elapsedRealtimeMs);
                }
                updateOnBatteryBgTimeBase(uptimeMs * 1000, elapsedRealtimeMs * 1000);
                updateOnBatteryScreenOffBgTimeBase(uptimeMs * 1000, 1000 * elapsedRealtimeMs);
            }
            if (userAwareService != this.mInForegroundService) {
                if (userAwareService) {
                    noteForegroundServiceResumedLocked(elapsedRealtimeMs);
                } else {
                    noteForegroundServicePausedLocked(elapsedRealtimeMs);
                }
                this.mInForegroundService = userAwareService;
            }
        }

        public boolean isInBackground() {
            return this.mProcessState >= 3;
        }

        public boolean updateOnBatteryBgTimeBase(long uptimeUs, long realtimeUs) {
            boolean on = this.mBsi.mOnBatteryTimeBase.isRunning() && isInBackground();
            return this.mOnBatteryBackgroundTimeBase.setRunning(on, uptimeUs, realtimeUs);
        }

        public boolean updateOnBatteryScreenOffBgTimeBase(long uptimeUs, long realtimeUs) {
            boolean on = this.mBsi.mOnBatteryScreenOffTimeBase.isRunning() && isInBackground();
            return this.mOnBatteryScreenOffBackgroundTimeBase.setRunning(on, uptimeUs, realtimeUs);
        }

        @Override // android.os.BatteryStats.Uid
        public SparseArray<? extends BatteryStats.Uid.Pid> getPidStats() {
            return this.mPids;
        }

        public BatteryStats.Uid.Pid getPidStatsLocked(int pid) {
            BatteryStats.Uid.Pid p = this.mPids.get(pid);
            if (p == null) {
                BatteryStats.Uid.Pid p2 = new BatteryStats.Uid.Pid();
                this.mPids.put(pid, p2);
                return p2;
            }
            return p;
        }

        public Pkg getPackageStatsLocked(String name) {
            Pkg ps = this.mPackageStats.get(name);
            if (ps == null) {
                Pkg ps2 = new Pkg(this.mBsi);
                this.mPackageStats.put(name, ps2);
                return ps2;
            }
            return ps;
        }

        public Pkg.Serv getServiceStatsLocked(String pkg, String serv) {
            Pkg ps = getPackageStatsLocked(pkg);
            Pkg.Serv ss = ps.mServiceStats.get(serv);
            if (ss == null) {
                Pkg.Serv ss2 = ps.newServiceStatsLocked();
                ps.mServiceStats.put(serv, ss2);
                return ss2;
            }
            return ss;
        }

        public void readSyncSummaryFromParcelLocked(String name, Parcel in) {
            DualTimer timer = this.mSyncStats.instantiateObject();
            timer.readSummaryFromParcelLocked(in);
            this.mSyncStats.add(name, timer);
        }

        public void readJobSummaryFromParcelLocked(String name, Parcel in) {
            DualTimer timer = this.mJobStats.instantiateObject();
            timer.readSummaryFromParcelLocked(in);
            this.mJobStats.add(name, timer);
        }

        public void readWakeSummaryFromParcelLocked(String wlName, Parcel in) {
            Wakelock wl = new Wakelock(this.mBsi, this);
            this.mWakelockStats.add(wlName, wl);
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 1).readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 0).readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 2).readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                getWakelockTimerLocked(wl, 18).readSummaryFromParcelLocked(in);
            }
        }

        public DualTimer getSensorTimerLocked(int sensor, boolean create) {
            Sensor se = this.mSensorStats.get(sensor);
            if (se == null) {
                if (!create) {
                    return null;
                }
                se = new Sensor(this.mBsi, this, sensor);
                this.mSensorStats.put(sensor, se);
            }
            DualTimer t = se.mTimer;
            if (t != null) {
                return t;
            }
            ArrayList<StopwatchTimer> timers = this.mBsi.mSensorTimers.get(sensor);
            if (timers == null) {
                timers = new ArrayList<>();
                this.mBsi.mSensorTimers.put(sensor, timers);
            }
            DualTimer t2 = new DualTimer(this.mBsi.mClocks, this, 3, timers, this.mBsi.mOnBatteryTimeBase, this.mOnBatteryBackgroundTimeBase);
            se.mTimer = t2;
            return t2;
        }

        public void noteStartSyncLocked(String name, long elapsedRealtimeMs) {
            DualTimer t = this.mSyncStats.startObject(name);
            if (t != null) {
                t.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStopSyncLocked(String name, long elapsedRealtimeMs) {
            DualTimer t = this.mSyncStats.stopObject(name);
            if (t != null) {
                t.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStartJobLocked(String name, long elapsedRealtimeMs) {
            DualTimer t = this.mJobStats.startObject(name);
            if (t != null) {
                t.startRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStopJobLocked(String name, long elapsedRealtimeMs, int stopReason) {
            DualTimer t = this.mJobStats.stopObject(name);
            if (t != null) {
                t.stopRunningLocked(elapsedRealtimeMs);
            }
            if (this.mBsi.mOnBatteryTimeBase.isRunning()) {
                SparseIntArray types = this.mJobCompletions.get(name);
                if (types == null) {
                    types = new SparseIntArray();
                    this.mJobCompletions.put(name, types);
                }
                int last = types.get(stopReason, 0);
                types.put(stopReason, last + 1);
            }
        }

        public StopwatchTimer getWakelockTimerLocked(Wakelock wl, int type) {
            if (wl == null) {
                return null;
            }
            if (type == 0) {
                StopwatchTimer t = wl.mTimerPartial;
                if (t == null) {
                    DualTimer t2 = new DualTimer(this.mBsi.mClocks, this, 0, this.mBsi.mPartialTimers, this.mBsi.mOnBatteryScreenOffTimeBase, this.mOnBatteryScreenOffBackgroundTimeBase);
                    wl.mTimerPartial = t2;
                    return t2;
                }
                return t;
            } else if (type == 1) {
                StopwatchTimer t3 = wl.mTimerFull;
                if (t3 == null) {
                    StopwatchTimer t4 = new StopwatchTimer(this.mBsi.mClocks, this, 1, this.mBsi.mFullTimers, this.mBsi.mOnBatteryTimeBase);
                    wl.mTimerFull = t4;
                    return t4;
                }
                return t3;
            } else if (type == 2) {
                StopwatchTimer t5 = wl.mTimerWindow;
                if (t5 == null) {
                    StopwatchTimer t6 = new StopwatchTimer(this.mBsi.mClocks, this, 2, this.mBsi.mWindowTimers, this.mBsi.mOnBatteryTimeBase);
                    wl.mTimerWindow = t6;
                    return t6;
                }
                return t5;
            } else if (type == 18) {
                StopwatchTimer t7 = wl.mTimerDraw;
                if (t7 == null) {
                    StopwatchTimer t8 = new StopwatchTimer(this.mBsi.mClocks, this, 18, this.mBsi.mDrawTimers, this.mBsi.mOnBatteryTimeBase);
                    wl.mTimerDraw = t8;
                    return t8;
                }
                return t7;
            } else {
                throw new IllegalArgumentException("type=" + type);
            }
        }

        public void noteStartWakeLocked(int pid, String name, int type, long elapsedRealtimeMs) {
            Wakelock wl = this.mWakelockStats.startObject(name);
            if (wl != null) {
                getWakelockTimerLocked(wl, type).startRunningLocked(elapsedRealtimeMs);
            }
            if (type == 0) {
                createAggregatedPartialWakelockTimerLocked().startRunningLocked(elapsedRealtimeMs);
                if (pid >= 0) {
                    BatteryStats.Uid.Pid p = getPidStatsLocked(pid);
                    int i = p.mWakeNesting;
                    p.mWakeNesting = i + 1;
                    if (i == 0) {
                        p.mWakeStartMs = elapsedRealtimeMs;
                    }
                }
            }
        }

        public void noteStopWakeLocked(int pid, String name, int type, long elapsedRealtimeMs) {
            BatteryStats.Uid.Pid p;
            Wakelock wl = this.mWakelockStats.stopObject(name);
            if (wl != null) {
                StopwatchTimer wlt = getWakelockTimerLocked(wl, type);
                wlt.stopRunningLocked(elapsedRealtimeMs);
            }
            if (type == 0) {
                DualTimer dualTimer = this.mAggregatedPartialWakelockTimer;
                if (dualTimer != null) {
                    dualTimer.stopRunningLocked(elapsedRealtimeMs);
                }
                if (pid >= 0 && (p = this.mPids.get(pid)) != null && p.mWakeNesting > 0) {
                    int i = p.mWakeNesting;
                    p.mWakeNesting = i - 1;
                    if (i == 1) {
                        p.mWakeSumMs += elapsedRealtimeMs - p.mWakeStartMs;
                        p.mWakeStartMs = 0L;
                    }
                }
            }
        }

        public void reportExcessiveCpuLocked(String proc, long overTime, long usedTime) {
            Proc p = getProcessStatsLocked(proc);
            if (p != null) {
                p.addExcessiveCpu(overTime, usedTime);
            }
        }

        public void noteStartSensor(int sensor, long elapsedRealtimeMs) {
            DualTimer t = getSensorTimerLocked(sensor, true);
            t.startRunningLocked(elapsedRealtimeMs);
        }

        public void noteStopSensor(int sensor, long elapsedRealtimeMs) {
            DualTimer t = getSensorTimerLocked(sensor, false);
            if (t != null) {
                t.stopRunningLocked(elapsedRealtimeMs);
            }
        }

        public void noteStartGps(long elapsedRealtimeMs) {
            noteStartSensor(-10000, elapsedRealtimeMs);
        }

        public void noteStopGps(long elapsedRealtimeMs) {
            noteStopSensor(-10000, elapsedRealtimeMs);
        }

        public BatteryStatsImpl getBatteryStats() {
            return this.mBsi;
        }
    }

    @Override // android.os.BatteryStats
    public long[] getCpuFreqs() {
        return this.mCpuFreqs;
    }

    public BatteryStatsImpl(File systemDir, Handler handler, PlatformIdleStateCallback cb, RailEnergyDataCallback railStatsCb, UserInfoProvider userInfoProvider) {
        this(new SystemClocks(), systemDir, handler, cb, railStatsCb, userInfoProvider);
    }

    private BatteryStatsImpl(Clocks clocks, File systemDir, Handler handler, PlatformIdleStateCallback cb, RailEnergyDataCallback railStatsCb, UserInfoProvider userInfoProvider) {
        this.mKernelWakelockReader = new KernelWakelockReader();
        this.mTmpWakelockStats = new KernelWakelockStats();
        this.mCpuUidUserSysTimeReader = new KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader(true);
        this.mCpuUidFreqTimeReader = new KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader(true);
        this.mCpuUidActiveTimeReader = new KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader(true);
        this.mCpuUidClusterTimeReader = new KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader(true);
        this.mKernelMemoryBandwidthStats = new KernelMemoryBandwidthStats();
        this.mKernelMemoryStats = new LongSparseArray<>();
        this.mPerProcStateCpuTimesAvailable = true;
        this.mPendingUids = new SparseIntArray();
        this.mCpuTimeReadsTrackingStartTime = SystemClock.uptimeMillis();
        this.mTmpRpmStats = new RpmStats();
        this.mLastRpmStatsUpdateTimeMs = -1000L;
        this.mTmpRailStats = new RailStats();
        this.mPendingRemovedUids = new LinkedList();
        this.mDeferSetCharging = new Runnable() { // from class: com.android.internal.os.BatteryStatsImpl.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (BatteryStatsImpl.this) {
                    if (BatteryStatsImpl.this.mOnBattery) {
                        return;
                    }
                    boolean changed = BatteryStatsImpl.this.setChargingLocked(true);
                    if (changed) {
                        long uptime = BatteryStatsImpl.this.mClocks.uptimeMillis();
                        long elapsedRealtime = BatteryStatsImpl.this.mClocks.elapsedRealtime();
                        BatteryStatsImpl.this.addHistoryRecordLocked(elapsedRealtime, uptime);
                    }
                }
            }
        };
        this.mExternalSync = null;
        this.mUserInfoProvider = null;
        this.mIsolatedUids = new SparseIntArray();
        this.mUidStats = new SparseArray<>();
        this.mPartialTimers = new ArrayList<>();
        this.mFullTimers = new ArrayList<>();
        this.mWindowTimers = new ArrayList<>();
        this.mDrawTimers = new ArrayList<>();
        this.mSensorTimers = new SparseArray<>();
        this.mWifiRunningTimers = new ArrayList<>();
        this.mFullWifiLockTimers = new ArrayList<>();
        this.mWifiMulticastTimers = new ArrayList<>();
        this.mWifiScanTimers = new ArrayList<>();
        this.mWifiBatchedScanTimers = new SparseArray<>();
        this.mAudioTurnedOnTimers = new ArrayList<>();
        this.mVideoTurnedOnTimers = new ArrayList<>();
        this.mFlashlightTurnedOnTimers = new ArrayList<>();
        this.mCameraTurnedOnTimers = new ArrayList<>();
        this.mBluetoothScanOnTimers = new ArrayList<>();
        this.mLastPartialTimers = new ArrayList<>();
        this.mOnBatteryTimeBase = new TimeBase(true);
        this.mOnBatteryScreenOffTimeBase = new TimeBase(true);
        this.mActiveEvents = new BatteryStats.HistoryEventTracker();
        this.mHaveBatteryLevel = false;
        this.mRecordingHistory = false;
        this.mHistoryBuffer = Parcel.obtain();
        this.mHistoryLastWritten = new BatteryStats.HistoryItem();
        this.mHistoryLastLastWritten = new BatteryStats.HistoryItem();
        this.mHistoryReadTmp = new BatteryStats.HistoryItem();
        this.mHistoryAddTmp = new BatteryStats.HistoryItem();
        this.mHistoryTagPool = new HashMap<>();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
        this.mLastHistoryElapsedRealtime = 0L;
        this.mTrackRunningHistoryElapsedRealtime = 0L;
        this.mTrackRunningHistoryUptime = 0L;
        this.mHistoryCur = new BatteryStats.HistoryItem();
        this.mLastHistoryStepDetails = null;
        this.mLastHistoryStepLevel = (byte) 0;
        this.mCurHistoryStepDetails = new BatteryStats.HistoryStepDetails();
        this.mReadHistoryStepDetails = new BatteryStats.HistoryStepDetails();
        this.mTmpHistoryStepDetails = new BatteryStats.HistoryStepDetails();
        this.mScreenState = 0;
        this.mScreenBrightnessBin = -1;
        this.mScreenBrightnessTimer = new StopwatchTimer[5];
        this.mUsbDataState = 0;
        this.mGpsSignalQualityBin = -1;
        this.mGpsSignalQualityTimer = new StopwatchTimer[2];
        this.mPhoneSignalStrengthBin = -1;
        this.mPhoneSignalStrengthBinRaw = -1;
        this.mPhoneSignalStrengthsTimer = new StopwatchTimer[5];
        this.mPhoneDataConnectionType = -1;
        this.mPhoneDataConnectionsTimer = new StopwatchTimer[23];
        this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
        this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
        this.mHasWifiReporting = false;
        this.mHasBluetoothReporting = false;
        this.mHasModemReporting = false;
        this.mWifiState = -1;
        this.mWifiStateTimer = new StopwatchTimer[8];
        this.mWifiSupplState = -1;
        this.mWifiSupplStateTimer = new StopwatchTimer[13];
        this.mWifiSignalStrengthBin = -1;
        this.mWifiSignalStrengthsTimer = new StopwatchTimer[5];
        this.mMobileRadioPowerState = 1;
        this.mWifiRadioPowerState = 1;
        this.mCharging = true;
        this.mInitStepMode = 0;
        this.mCurStepMode = 0;
        this.mModStepMode = 0;
        this.mDischargeStepTracker = new BatteryStats.LevelStepTracker(200);
        this.mDailyDischargeStepTracker = new BatteryStats.LevelStepTracker(400);
        this.mChargeStepTracker = new BatteryStats.LevelStepTracker(200);
        this.mDailyChargeStepTracker = new BatteryStats.LevelStepTracker(400);
        this.mDailyStartTime = 0L;
        this.mNextMinDailyDeadline = 0L;
        this.mNextMaxDailyDeadline = 0L;
        this.mDailyItems = new ArrayList<>();
        this.mLastWriteTime = 0L;
        this.mPhoneServiceState = -1;
        this.mPhoneServiceStateRaw = -1;
        this.mPhoneSimStateRaw = -1;
        this.mEstimatedBatteryCapacity = -1;
        this.mMinLearnedBatteryCapacity = -1;
        this.mMaxLearnedBatteryCapacity = -1;
        this.mRpmStats = new HashMap<>();
        this.mScreenOffRpmStats = new HashMap<>();
        this.mKernelWakelockStats = new HashMap<>();
        this.mLastWakeupReason = null;
        this.mLastWakeupUptimeMs = 0L;
        this.mWakeupReasonStats = new HashMap<>();
        this.mChangedStates = 0;
        this.mChangedStates2 = 0;
        this.mInitialAcquireWakeUid = -1;
        this.mWifiFullLockNesting = 0;
        this.mWifiScanNesting = 0;
        this.mWifiMulticastNesting = 0;
        this.mNetworkStatsPool = new Pools.SynchronizedPool(6);
        this.mWifiNetworkLock = new Object();
        this.mWifiIfaces = EmptyArray.STRING;
        this.mLastWifiNetworkStats = new NetworkStats(0L, -1);
        this.mModemNetworkLock = new Object();
        this.mModemIfaces = EmptyArray.STRING;
        this.mLastModemNetworkStats = new NetworkStats(0L, -1);
        this.mLastModemActivityInfo = new ModemActivityInfo(0L, 0, 0, new int[0], 0, 0);
        this.mLastBluetoothActivityInfo = new BluetoothActivityInfoCache();
        this.mWriteLock = new ReentrantLock();
        init(clocks);
        if (systemDir != null) {
            this.mStatsFile = new AtomicFile(new File(systemDir, "batterystats.bin"));
            this.mBatteryStatsHistory = new BatteryStatsHistory(this, systemDir, this.mHistoryBuffer);
        } else {
            this.mStatsFile = null;
            this.mBatteryStatsHistory = new BatteryStatsHistory(this, this.mHistoryBuffer);
        }
        this.mCheckinFile = new AtomicFile(new File(systemDir, "batterystats-checkin.bin"));
        this.mDailyFile = new AtomicFile(new File(systemDir, "batterystats-daily.xml"));
        this.mHandler = new MyHandler(handler.getLooper());
        this.mConstants = new Constants(this.mHandler);
        this.mStartCount++;
        this.mScreenOnTimer = new StopwatchTimer(this.mClocks, null, -1, null, this.mOnBatteryTimeBase);
        this.mScreenDozeTimer = new StopwatchTimer(this.mClocks, null, -1, null, this.mOnBatteryTimeBase);
        for (int i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i] = new StopwatchTimer(this.mClocks, null, (-100) - i, null, this.mOnBatteryTimeBase);
        }
        this.mInteractiveTimer = new StopwatchTimer(this.mClocks, null, -10, null, this.mOnBatteryTimeBase);
        this.mPowerSaveModeEnabledTimer = new StopwatchTimer(this.mClocks, null, -2, null, this.mOnBatteryTimeBase);
        this.mDeviceIdleModeLightTimer = new StopwatchTimer(this.mClocks, null, -11, null, this.mOnBatteryTimeBase);
        this.mDeviceIdleModeFullTimer = new StopwatchTimer(this.mClocks, null, -14, null, this.mOnBatteryTimeBase);
        this.mDeviceLightIdlingTimer = new StopwatchTimer(this.mClocks, null, -15, null, this.mOnBatteryTimeBase);
        this.mDeviceIdlingTimer = new StopwatchTimer(this.mClocks, null, -12, null, this.mOnBatteryTimeBase);
        this.mPhoneOnTimer = new StopwatchTimer(this.mClocks, null, -3, null, this.mOnBatteryTimeBase);
        for (int i2 = 0; i2 < 5; i2++) {
            this.mPhoneSignalStrengthsTimer[i2] = new StopwatchTimer(this.mClocks, null, (-200) - i2, null, this.mOnBatteryTimeBase);
        }
        this.mPhoneSignalScanningTimer = new StopwatchTimer(this.mClocks, null, -199, null, this.mOnBatteryTimeBase);
        for (int i3 = 0; i3 < 23; i3++) {
            this.mPhoneDataConnectionsTimer[i3] = new StopwatchTimer(this.mClocks, null, (-300) - i3, null, this.mOnBatteryTimeBase);
        }
        for (int i4 = 0; i4 < 10; i4++) {
            this.mNetworkByteActivityCounters[i4] = new LongSamplingCounter(this.mOnBatteryTimeBase);
            this.mNetworkPacketActivityCounters[i4] = new LongSamplingCounter(this.mOnBatteryTimeBase);
        }
        this.mWifiActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 1);
        this.mBluetoothActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 1);
        this.mModemActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 5);
        this.mMobileRadioActiveTimer = new StopwatchTimer(this.mClocks, null, -400, null, this.mOnBatteryTimeBase);
        this.mMobileRadioActivePerAppTimer = new StopwatchTimer(this.mClocks, null, -401, null, this.mOnBatteryTimeBase);
        this.mMobileRadioActiveAdjustedTime = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mMobileRadioActiveUnknownTime = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mMobileRadioActiveUnknownCount = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mWifiMulticastWakelockTimer = new StopwatchTimer(this.mClocks, null, 23, null, this.mOnBatteryTimeBase);
        this.mWifiOnTimer = new StopwatchTimer(this.mClocks, null, -4, null, this.mOnBatteryTimeBase);
        this.mGlobalWifiRunningTimer = new StopwatchTimer(this.mClocks, null, -5, null, this.mOnBatteryTimeBase);
        for (int i5 = 0; i5 < 8; i5++) {
            this.mWifiStateTimer[i5] = new StopwatchTimer(this.mClocks, null, (-600) - i5, null, this.mOnBatteryTimeBase);
        }
        for (int i6 = 0; i6 < 13; i6++) {
            this.mWifiSupplStateTimer[i6] = new StopwatchTimer(this.mClocks, null, (-700) - i6, null, this.mOnBatteryTimeBase);
        }
        for (int i7 = 0; i7 < 5; i7++) {
            this.mWifiSignalStrengthsTimer[i7] = new StopwatchTimer(this.mClocks, null, (-800) - i7, null, this.mOnBatteryTimeBase);
        }
        this.mWifiActiveTimer = new StopwatchTimer(this.mClocks, null, -900, null, this.mOnBatteryTimeBase);
        for (int i8 = 0; i8 < 2; i8++) {
            this.mGpsSignalQualityTimer[i8] = new StopwatchTimer(this.mClocks, null, (-1000) - i8, null, this.mOnBatteryTimeBase);
        }
        this.mAudioOnTimer = new StopwatchTimer(this.mClocks, null, -7, null, this.mOnBatteryTimeBase);
        this.mVideoOnTimer = new StopwatchTimer(this.mClocks, null, -8, null, this.mOnBatteryTimeBase);
        this.mFlashlightOnTimer = new StopwatchTimer(this.mClocks, null, -9, null, this.mOnBatteryTimeBase);
        this.mCameraOnTimer = new StopwatchTimer(this.mClocks, null, -13, null, this.mOnBatteryTimeBase);
        this.mBluetoothScanTimer = new StopwatchTimer(this.mClocks, null, -14, null, this.mOnBatteryTimeBase);
        this.mDischargeScreenOffCounter = new LongSamplingCounter(this.mOnBatteryScreenOffTimeBase);
        this.mDischargeScreenDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mDischargeLightDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mDischargeDeepDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mDischargeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mOnBatteryInternal = false;
        this.mOnBattery = false;
        long uptime = this.mClocks.uptimeMillis() * 1000;
        long realtime = this.mClocks.elapsedRealtime() * 1000;
        initTimes(uptime, realtime);
        String str = Build.ID;
        this.mEndPlatformVersion = str;
        this.mStartPlatformVersion = str;
        this.mDischargeStartLevel = 0;
        this.mDischargeUnplugLevel = 0;
        this.mDischargePlugLevel = -1;
        this.mDischargeCurrentLevel = 0;
        this.mCurrentBatteryLevel = 0;
        initDischarge();
        clearHistoryLocked();
        updateDailyDeadlineLocked();
        this.mPlatformIdleStateCallback = cb;
        this.mRailEnergyDataCallback = railStatsCb;
        this.mUserInfoProvider = userInfoProvider;
    }

    @UnsupportedAppUsage
    public BatteryStatsImpl(Parcel p) {
        this(new SystemClocks(), p);
    }

    public BatteryStatsImpl(Clocks clocks, Parcel p) {
        this.mKernelWakelockReader = new KernelWakelockReader();
        this.mTmpWakelockStats = new KernelWakelockStats();
        this.mCpuUidUserSysTimeReader = new KernelCpuUidTimeReader.KernelCpuUidUserSysTimeReader(true);
        this.mCpuUidFreqTimeReader = new KernelCpuUidTimeReader.KernelCpuUidFreqTimeReader(true);
        this.mCpuUidActiveTimeReader = new KernelCpuUidTimeReader.KernelCpuUidActiveTimeReader(true);
        this.mCpuUidClusterTimeReader = new KernelCpuUidTimeReader.KernelCpuUidClusterTimeReader(true);
        this.mKernelMemoryBandwidthStats = new KernelMemoryBandwidthStats();
        this.mKernelMemoryStats = new LongSparseArray<>();
        this.mPerProcStateCpuTimesAvailable = true;
        this.mPendingUids = new SparseIntArray();
        this.mCpuTimeReadsTrackingStartTime = SystemClock.uptimeMillis();
        this.mTmpRpmStats = new RpmStats();
        this.mLastRpmStatsUpdateTimeMs = -1000L;
        this.mTmpRailStats = new RailStats();
        this.mPendingRemovedUids = new LinkedList();
        this.mDeferSetCharging = new Runnable() { // from class: com.android.internal.os.BatteryStatsImpl.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (BatteryStatsImpl.this) {
                    if (BatteryStatsImpl.this.mOnBattery) {
                        return;
                    }
                    boolean changed = BatteryStatsImpl.this.setChargingLocked(true);
                    if (changed) {
                        long uptime = BatteryStatsImpl.this.mClocks.uptimeMillis();
                        long elapsedRealtime = BatteryStatsImpl.this.mClocks.elapsedRealtime();
                        BatteryStatsImpl.this.addHistoryRecordLocked(elapsedRealtime, uptime);
                    }
                }
            }
        };
        this.mExternalSync = null;
        this.mUserInfoProvider = null;
        this.mIsolatedUids = new SparseIntArray();
        this.mUidStats = new SparseArray<>();
        this.mPartialTimers = new ArrayList<>();
        this.mFullTimers = new ArrayList<>();
        this.mWindowTimers = new ArrayList<>();
        this.mDrawTimers = new ArrayList<>();
        this.mSensorTimers = new SparseArray<>();
        this.mWifiRunningTimers = new ArrayList<>();
        this.mFullWifiLockTimers = new ArrayList<>();
        this.mWifiMulticastTimers = new ArrayList<>();
        this.mWifiScanTimers = new ArrayList<>();
        this.mWifiBatchedScanTimers = new SparseArray<>();
        this.mAudioTurnedOnTimers = new ArrayList<>();
        this.mVideoTurnedOnTimers = new ArrayList<>();
        this.mFlashlightTurnedOnTimers = new ArrayList<>();
        this.mCameraTurnedOnTimers = new ArrayList<>();
        this.mBluetoothScanOnTimers = new ArrayList<>();
        this.mLastPartialTimers = new ArrayList<>();
        this.mOnBatteryTimeBase = new TimeBase(true);
        this.mOnBatteryScreenOffTimeBase = new TimeBase(true);
        this.mActiveEvents = new BatteryStats.HistoryEventTracker();
        this.mHaveBatteryLevel = false;
        this.mRecordingHistory = false;
        this.mHistoryBuffer = Parcel.obtain();
        this.mHistoryLastWritten = new BatteryStats.HistoryItem();
        this.mHistoryLastLastWritten = new BatteryStats.HistoryItem();
        this.mHistoryReadTmp = new BatteryStats.HistoryItem();
        this.mHistoryAddTmp = new BatteryStats.HistoryItem();
        this.mHistoryTagPool = new HashMap<>();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
        this.mLastHistoryElapsedRealtime = 0L;
        this.mTrackRunningHistoryElapsedRealtime = 0L;
        this.mTrackRunningHistoryUptime = 0L;
        this.mHistoryCur = new BatteryStats.HistoryItem();
        this.mLastHistoryStepDetails = null;
        this.mLastHistoryStepLevel = (byte) 0;
        this.mCurHistoryStepDetails = new BatteryStats.HistoryStepDetails();
        this.mReadHistoryStepDetails = new BatteryStats.HistoryStepDetails();
        this.mTmpHistoryStepDetails = new BatteryStats.HistoryStepDetails();
        this.mScreenState = 0;
        this.mScreenBrightnessBin = -1;
        this.mScreenBrightnessTimer = new StopwatchTimer[5];
        this.mUsbDataState = 0;
        this.mGpsSignalQualityBin = -1;
        this.mGpsSignalQualityTimer = new StopwatchTimer[2];
        this.mPhoneSignalStrengthBin = -1;
        this.mPhoneSignalStrengthBinRaw = -1;
        this.mPhoneSignalStrengthsTimer = new StopwatchTimer[5];
        this.mPhoneDataConnectionType = -1;
        this.mPhoneDataConnectionsTimer = new StopwatchTimer[23];
        this.mNetworkByteActivityCounters = new LongSamplingCounter[10];
        this.mNetworkPacketActivityCounters = new LongSamplingCounter[10];
        this.mHasWifiReporting = false;
        this.mHasBluetoothReporting = false;
        this.mHasModemReporting = false;
        this.mWifiState = -1;
        this.mWifiStateTimer = new StopwatchTimer[8];
        this.mWifiSupplState = -1;
        this.mWifiSupplStateTimer = new StopwatchTimer[13];
        this.mWifiSignalStrengthBin = -1;
        this.mWifiSignalStrengthsTimer = new StopwatchTimer[5];
        this.mMobileRadioPowerState = 1;
        this.mWifiRadioPowerState = 1;
        this.mCharging = true;
        this.mInitStepMode = 0;
        this.mCurStepMode = 0;
        this.mModStepMode = 0;
        this.mDischargeStepTracker = new BatteryStats.LevelStepTracker(200);
        this.mDailyDischargeStepTracker = new BatteryStats.LevelStepTracker(400);
        this.mChargeStepTracker = new BatteryStats.LevelStepTracker(200);
        this.mDailyChargeStepTracker = new BatteryStats.LevelStepTracker(400);
        this.mDailyStartTime = 0L;
        this.mNextMinDailyDeadline = 0L;
        this.mNextMaxDailyDeadline = 0L;
        this.mDailyItems = new ArrayList<>();
        this.mLastWriteTime = 0L;
        this.mPhoneServiceState = -1;
        this.mPhoneServiceStateRaw = -1;
        this.mPhoneSimStateRaw = -1;
        this.mEstimatedBatteryCapacity = -1;
        this.mMinLearnedBatteryCapacity = -1;
        this.mMaxLearnedBatteryCapacity = -1;
        this.mRpmStats = new HashMap<>();
        this.mScreenOffRpmStats = new HashMap<>();
        this.mKernelWakelockStats = new HashMap<>();
        this.mLastWakeupReason = null;
        this.mLastWakeupUptimeMs = 0L;
        this.mWakeupReasonStats = new HashMap<>();
        this.mChangedStates = 0;
        this.mChangedStates2 = 0;
        this.mInitialAcquireWakeUid = -1;
        this.mWifiFullLockNesting = 0;
        this.mWifiScanNesting = 0;
        this.mWifiMulticastNesting = 0;
        this.mNetworkStatsPool = new Pools.SynchronizedPool(6);
        this.mWifiNetworkLock = new Object();
        this.mWifiIfaces = EmptyArray.STRING;
        this.mLastWifiNetworkStats = new NetworkStats(0L, -1);
        this.mModemNetworkLock = new Object();
        this.mModemIfaces = EmptyArray.STRING;
        this.mLastModemNetworkStats = new NetworkStats(0L, -1);
        this.mLastModemActivityInfo = new ModemActivityInfo(0L, 0, 0, new int[0], 0, 0);
        this.mLastBluetoothActivityInfo = new BluetoothActivityInfoCache();
        this.mWriteLock = new ReentrantLock();
        init(clocks);
        this.mStatsFile = null;
        this.mCheckinFile = null;
        this.mDailyFile = null;
        this.mHandler = null;
        this.mExternalSync = null;
        this.mConstants = new Constants(this.mHandler);
        clearHistoryLocked();
        this.mBatteryStatsHistory = new BatteryStatsHistory(this, this.mHistoryBuffer);
        readFromParcel(p);
        this.mPlatformIdleStateCallback = null;
        this.mRailEnergyDataCallback = null;
    }

    public void setPowerProfileLocked(PowerProfile profile) {
        this.mPowerProfile = profile;
        int numClusters = this.mPowerProfile.getNumCpuClusters();
        this.mKernelCpuSpeedReaders = new KernelCpuSpeedReader[numClusters];
        int firstCpuOfCluster = 0;
        for (int i = 0; i < numClusters; i++) {
            int numSpeedSteps = this.mPowerProfile.getNumSpeedStepsInCpuCluster(i);
            this.mKernelCpuSpeedReaders[i] = new KernelCpuSpeedReader(firstCpuOfCluster, numSpeedSteps);
            firstCpuOfCluster += this.mPowerProfile.getNumCoresInCpuCluster(i);
        }
        int i2 = this.mEstimatedBatteryCapacity;
        if (i2 == -1) {
            this.mEstimatedBatteryCapacity = (int) this.mPowerProfile.getBatteryCapacity();
        }
    }

    public void setCallback(BatteryCallback cb) {
        this.mCallback = cb;
    }

    public void setRadioScanningTimeoutLocked(long timeout) {
        StopwatchTimer stopwatchTimer = this.mPhoneSignalScanningTimer;
        if (stopwatchTimer != null) {
            stopwatchTimer.setTimeout(timeout);
        }
    }

    public void setExternalStatsSyncLocked(ExternalStatsSync sync) {
        this.mExternalSync = sync;
    }

    public void updateDailyDeadlineLocked() {
        long currentTime = System.currentTimeMillis();
        this.mDailyStartTime = currentTime;
        Calendar calDeadline = Calendar.getInstance();
        calDeadline.setTimeInMillis(currentTime);
        calDeadline.set(6, calDeadline.get(6) + 1);
        calDeadline.set(14, 0);
        calDeadline.set(13, 0);
        calDeadline.set(12, 0);
        calDeadline.set(11, 1);
        this.mNextMinDailyDeadline = calDeadline.getTimeInMillis();
        calDeadline.set(11, 3);
        this.mNextMaxDailyDeadline = calDeadline.getTimeInMillis();
    }

    public void recordDailyStatsIfNeededLocked(boolean settled) {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= this.mNextMaxDailyDeadline) {
            recordDailyStatsLocked();
        } else if (settled && currentTime >= this.mNextMinDailyDeadline) {
            recordDailyStatsLocked();
        } else if (currentTime < this.mDailyStartTime - 86400000) {
            recordDailyStatsLocked();
        }
    }

    public void recordDailyStatsLocked() {
        BatteryStats.DailyItem item = new BatteryStats.DailyItem();
        item.mStartTime = this.mDailyStartTime;
        item.mEndTime = System.currentTimeMillis();
        boolean hasData = false;
        if (this.mDailyDischargeStepTracker.mNumStepDurations > 0) {
            hasData = true;
            item.mDischargeSteps = new BatteryStats.LevelStepTracker(this.mDailyDischargeStepTracker.mNumStepDurations, this.mDailyDischargeStepTracker.mStepDurations);
        }
        if (this.mDailyChargeStepTracker.mNumStepDurations > 0) {
            hasData = true;
            item.mChargeSteps = new BatteryStats.LevelStepTracker(this.mDailyChargeStepTracker.mNumStepDurations, this.mDailyChargeStepTracker.mStepDurations);
        }
        ArrayList<BatteryStats.PackageChange> arrayList = this.mDailyPackageChanges;
        if (arrayList != null) {
            hasData = true;
            item.mPackageChanges = arrayList;
            this.mDailyPackageChanges = null;
        }
        this.mDailyDischargeStepTracker.init();
        this.mDailyChargeStepTracker.init();
        updateDailyDeadlineLocked();
        if (hasData) {
            long startTime = SystemClock.uptimeMillis();
            this.mDailyItems.add(item);
            while (this.mDailyItems.size() > 10) {
                this.mDailyItems.remove(0);
            }
            final ByteArrayOutputStream memStream = new ByteArrayOutputStream();
            try {
                XmlSerializer out = new FastXmlSerializer();
                out.setOutput(memStream, StandardCharsets.UTF_8.name());
                writeDailyItemsLocked(out);
                final long initialTime = SystemClock.uptimeMillis() - startTime;
                BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.os.BatteryStatsImpl.3
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (BatteryStatsImpl.this.mCheckinFile) {
                            long startTime2 = SystemClock.uptimeMillis();
                            FileOutputStream stream = null;
                            try {
                                stream = BatteryStatsImpl.this.mDailyFile.startWrite();
                                memStream.writeTo(stream);
                                stream.flush();
                                BatteryStatsImpl.this.mDailyFile.finishWrite(stream);
                                EventLogTags.writeCommitSysConfigFile("batterystats-daily", (initialTime + SystemClock.uptimeMillis()) - startTime2);
                            } catch (IOException e) {
                                Slog.w("BatteryStats", "Error writing battery daily items", e);
                                BatteryStatsImpl.this.mDailyFile.failWrite(stream);
                            }
                        }
                    }
                });
            } catch (IOException e) {
            }
        }
    }

    private void writeDailyItemsLocked(XmlSerializer out) throws IOException {
        StringBuilder sb = new StringBuilder(64);
        out.startDocument(null, true);
        out.startTag(null, "daily-items");
        for (int i = 0; i < this.mDailyItems.size(); i++) {
            BatteryStats.DailyItem dit = this.mDailyItems.get(i);
            out.startTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
            out.attribute(null, Telephony.BaseMmsColumns.START, Long.toString(dit.mStartTime));
            out.attribute(null, "end", Long.toString(dit.mEndTime));
            writeDailyLevelSteps(out, "dis", dit.mDischargeSteps, sb);
            writeDailyLevelSteps(out, "chg", dit.mChargeSteps, sb);
            if (dit.mPackageChanges != null) {
                for (int j = 0; j < dit.mPackageChanges.size(); j++) {
                    BatteryStats.PackageChange pc = dit.mPackageChanges.get(j);
                    if (pc.mUpdate) {
                        out.startTag(null, "upd");
                        out.attribute(null, "pkg", pc.mPackageName);
                        out.attribute(null, "ver", Long.toString(pc.mVersionCode));
                        out.endTag(null, "upd");
                    } else {
                        out.startTag(null, "rem");
                        out.attribute(null, "pkg", pc.mPackageName);
                        out.endTag(null, "rem");
                    }
                }
            }
            out.endTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
        }
        out.endTag(null, "daily-items");
        out.endDocument();
    }

    private void writeDailyLevelSteps(XmlSerializer out, String tag, BatteryStats.LevelStepTracker steps, StringBuilder tmpBuilder) throws IOException {
        if (steps != null) {
            out.startTag(null, tag);
            out.attribute(null, "n", Integer.toString(steps.mNumStepDurations));
            for (int i = 0; i < steps.mNumStepDurations; i++) {
                out.startTag(null, "s");
                tmpBuilder.setLength(0);
                steps.encodeEntryAt(i, tmpBuilder);
                out.attribute(null, Telephony.BaseMmsColumns.MMS_VERSION, tmpBuilder.toString());
                out.endTag(null, "s");
            }
            out.endTag(null, tag);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:15:0x0048
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1234)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:1018)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:55)
        */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x0048 -> B:16:0x004a). Please submit an issue!!! */
    public void readDailyStatsLocked() {
        /*
            r3 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Reading daily items from "
            r0.append(r1)
            com.android.internal.os.AtomicFile r1 = r3.mDailyFile
            java.io.File r1 = r1.getBaseFile()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "BatteryStatsImpl"
            android.util.Slog.d(r1, r0)
            java.util.ArrayList<android.os.BatteryStats$DailyItem> r0 = r3.mDailyItems
            r0.clear()
            com.android.internal.os.AtomicFile r0 = r3.mDailyFile     // Catch: java.io.FileNotFoundException -> L4b
            java.io.FileInputStream r0 = r0.openRead()     // Catch: java.io.FileNotFoundException -> L4b
            org.xmlpull.v1.XmlPullParser r1 = android.util.Xml.newPullParser()     // Catch: java.lang.Throwable -> L3c org.xmlpull.v1.XmlPullParserException -> L43
            java.nio.charset.Charset r2 = java.nio.charset.StandardCharsets.UTF_8     // Catch: java.lang.Throwable -> L3c org.xmlpull.v1.XmlPullParserException -> L43
            java.lang.String r2 = r2.name()     // Catch: java.lang.Throwable -> L3c org.xmlpull.v1.XmlPullParserException -> L43
            r1.setInput(r0, r2)     // Catch: java.lang.Throwable -> L3c org.xmlpull.v1.XmlPullParserException -> L43
            r3.readDailyItemsLocked(r1)     // Catch: java.lang.Throwable -> L3c org.xmlpull.v1.XmlPullParserException -> L43
            r0.close()     // Catch: java.io.IOException -> L48
            goto L47
        L3c:
            r1 = move-exception
            r0.close()     // Catch: java.io.IOException -> L41
            goto L42
        L41:
            r2 = move-exception
        L42:
            throw r1
        L43:
            r1 = move-exception
            r0.close()     // Catch: java.io.IOException -> L48
        L47:
            goto L4a
        L48:
            r1 = move-exception
        L4a:
            return
        L4b:
            r0 = move-exception
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.readDailyStatsLocked():void");
    }

    private void readDailyItemsLocked(XmlPullParser parser) {
        int type;
        while (true) {
            try {
                type = parser.next();
                if (type == 2 || type == 1) {
                    break;
                }
            } catch (IOException e) {
                Slog.w(TAG, "Failed parsing daily " + e);
                return;
            } catch (IllegalStateException e2) {
                Slog.w(TAG, "Failed parsing daily " + e2);
                return;
            } catch (IndexOutOfBoundsException e3) {
                Slog.w(TAG, "Failed parsing daily " + e3);
                return;
            } catch (NullPointerException e4) {
                Slog.w(TAG, "Failed parsing daily " + e4);
                return;
            } catch (NumberFormatException e5) {
                Slog.w(TAG, "Failed parsing daily " + e5);
                return;
            } catch (XmlPullParserException e6) {
                Slog.w(TAG, "Failed parsing daily " + e6);
                return;
            }
        }
        if (type != 2) {
            throw new IllegalStateException("no start tag found");
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int type2 = parser.next();
            if (type2 != 1) {
                if (type2 != 3 || parser.getDepth() > outerDepth) {
                    if (type2 != 3 && type2 != 4) {
                        String tagName = parser.getName();
                        if (tagName.equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                            readDailyItemTagLocked(parser);
                        } else {
                            Slog.w(TAG, "Unknown element under <daily-items>: " + parser.getName());
                            XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    void readDailyItemTagLocked(XmlPullParser parser) throws NumberFormatException, XmlPullParserException, IOException {
        BatteryStats.DailyItem dit = new BatteryStats.DailyItem();
        String attr = parser.getAttributeValue(null, Telephony.BaseMmsColumns.START);
        if (attr != null) {
            dit.mStartTime = Long.parseLong(attr);
        }
        String attr2 = parser.getAttributeValue(null, "end");
        if (attr2 != null) {
            dit.mEndTime = Long.parseLong(attr2);
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            } else if (type != 3 && type != 4) {
                String tagName = parser.getName();
                if (tagName.equals("dis")) {
                    readDailyItemTagDetailsLocked(parser, dit, false, "dis");
                } else if (tagName.equals("chg")) {
                    readDailyItemTagDetailsLocked(parser, dit, true, "chg");
                } else if (tagName.equals("upd")) {
                    if (dit.mPackageChanges == null) {
                        dit.mPackageChanges = new ArrayList<>();
                    }
                    BatteryStats.PackageChange pc = new BatteryStats.PackageChange();
                    pc.mUpdate = true;
                    pc.mPackageName = parser.getAttributeValue(null, "pkg");
                    String verStr = parser.getAttributeValue(null, "ver");
                    pc.mVersionCode = verStr != null ? Long.parseLong(verStr) : 0L;
                    dit.mPackageChanges.add(pc);
                    XmlUtils.skipCurrentTag(parser);
                } else if (tagName.equals("rem")) {
                    if (dit.mPackageChanges == null) {
                        dit.mPackageChanges = new ArrayList<>();
                    }
                    BatteryStats.PackageChange pc2 = new BatteryStats.PackageChange();
                    pc2.mUpdate = false;
                    pc2.mPackageName = parser.getAttributeValue(null, "pkg");
                    dit.mPackageChanges.add(pc2);
                    XmlUtils.skipCurrentTag(parser);
                } else {
                    Slog.w(TAG, "Unknown element under <item>: " + parser.getName());
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        this.mDailyItems.add(dit);
    }

    void readDailyItemTagDetailsLocked(XmlPullParser parser, BatteryStats.DailyItem dit, boolean isCharge, String tag) throws NumberFormatException, XmlPullParserException, IOException {
        String valueAttr;
        String numAttr = parser.getAttributeValue(null, "n");
        if (numAttr == null) {
            Slog.w(TAG, "Missing 'n' attribute at " + parser.getPositionDescription());
            XmlUtils.skipCurrentTag(parser);
            return;
        }
        int num = Integer.parseInt(numAttr);
        BatteryStats.LevelStepTracker steps = new BatteryStats.LevelStepTracker(num);
        if (isCharge) {
            dit.mChargeSteps = steps;
        } else {
            dit.mDischargeSteps = steps;
        }
        int i = 0;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            } else if (type != 3 && type != 4) {
                String tagName = parser.getName();
                if ("s".equals(tagName)) {
                    if (i < num && (valueAttr = parser.getAttributeValue(null, Telephony.BaseMmsColumns.MMS_VERSION)) != null) {
                        steps.decodeEntryAt(i, valueAttr);
                        i++;
                    }
                } else {
                    Slog.w(TAG, "Unknown element under <" + tag + ">: " + parser.getName());
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        steps.mNumStepDurations = i;
    }

    @Override // android.os.BatteryStats
    public BatteryStats.DailyItem getDailyItemLocked(int daysAgo) {
        int index = (this.mDailyItems.size() - 1) - daysAgo;
        if (index >= 0) {
            return this.mDailyItems.get(index);
        }
        return null;
    }

    @Override // android.os.BatteryStats
    public long getCurrentDailyStartTime() {
        return this.mDailyStartTime;
    }

    @Override // android.os.BatteryStats
    public long getNextMinDailyDeadline() {
        return this.mNextMinDailyDeadline;
    }

    @Override // android.os.BatteryStats
    public long getNextMaxDailyDeadline() {
        return this.mNextMaxDailyDeadline;
    }

    @Override // android.os.BatteryStats
    public boolean startIteratingOldHistoryLocked() {
        BatteryStats.HistoryItem historyItem = this.mHistory;
        this.mHistoryIterator = historyItem;
        if (historyItem == null) {
            return false;
        }
        this.mHistoryBuffer.setDataPosition(0);
        this.mHistoryReadTmp.clear();
        this.mReadOverflow = false;
        this.mIteratingHistory = true;
        return true;
    }

    @Override // android.os.BatteryStats
    public boolean getNextOldHistoryLocked(BatteryStats.HistoryItem out) {
        boolean end = this.mHistoryBuffer.dataPosition() >= this.mHistoryBuffer.dataSize();
        if (!end) {
            readHistoryDelta(this.mHistoryBuffer, this.mHistoryReadTmp);
            this.mReadOverflow |= this.mHistoryReadTmp.cmd == 6;
        }
        BatteryStats.HistoryItem cur = this.mHistoryIterator;
        if (cur == null) {
            if (!this.mReadOverflow && !end) {
                Slog.w(TAG, "Old history ends before new history!");
            }
            return false;
        }
        out.setTo(cur);
        this.mHistoryIterator = cur.next;
        if (!this.mReadOverflow) {
            if (end) {
                Slog.w(TAG, "New history ends before old history!");
            } else if (!out.same(this.mHistoryReadTmp)) {
                PrintWriter pw = new FastPrintWriter(new LogWriter(5, TAG));
                pw.println("Histories differ!");
                pw.println("Old history:");
                new BatteryStats.HistoryPrinter().printNextItem(pw, out, 0L, false, true);
                pw.println("New history:");
                new BatteryStats.HistoryPrinter().printNextItem(pw, this.mHistoryReadTmp, 0L, false, true);
                pw.flush();
            }
        }
        return true;
    }

    @Override // android.os.BatteryStats
    public void finishIteratingOldHistoryLocked() {
        this.mIteratingHistory = false;
        Parcel parcel = this.mHistoryBuffer;
        parcel.setDataPosition(parcel.dataSize());
        this.mHistoryIterator = null;
    }

    @Override // android.os.BatteryStats
    public int getHistoryTotalSize() {
        return this.mConstants.MAX_HISTORY_BUFFER * this.mConstants.MAX_HISTORY_FILES;
    }

    @Override // android.os.BatteryStats
    public int getHistoryUsedSize() {
        return this.mBatteryStatsHistory.getHistoryUsedSize();
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public boolean startIteratingHistoryLocked() {
        this.mBatteryStatsHistory.startIteratingHistory();
        this.mReadOverflow = false;
        this.mIteratingHistory = true;
        this.mReadHistoryStrings = new String[this.mHistoryTagPool.size()];
        this.mReadHistoryUids = new int[this.mHistoryTagPool.size()];
        this.mReadHistoryChars = 0;
        for (Map.Entry<BatteryStats.HistoryTag, Integer> ent : this.mHistoryTagPool.entrySet()) {
            BatteryStats.HistoryTag tag = ent.getKey();
            int idx = ent.getValue().intValue();
            this.mReadHistoryStrings[idx] = tag.string;
            this.mReadHistoryUids[idx] = tag.uid;
            this.mReadHistoryChars += tag.string.length() + 1;
        }
        return true;
    }

    @Override // android.os.BatteryStats
    public int getHistoryStringPoolSize() {
        return this.mReadHistoryStrings.length;
    }

    @Override // android.os.BatteryStats
    public int getHistoryStringPoolBytes() {
        return (this.mReadHistoryStrings.length * 12) + (this.mReadHistoryChars * 2);
    }

    @Override // android.os.BatteryStats
    public String getHistoryTagPoolString(int index) {
        return this.mReadHistoryStrings[index];
    }

    @Override // android.os.BatteryStats
    public int getHistoryTagPoolUid(int index) {
        return this.mReadHistoryUids[index];
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public boolean getNextHistoryLocked(BatteryStats.HistoryItem out) {
        Parcel p = this.mBatteryStatsHistory.getNextParcel(out);
        if (p == null) {
            return false;
        }
        long lastRealtime = out.time;
        long lastWalltime = out.currentTime;
        readHistoryDelta(p, out);
        if (out.cmd != 5 && out.cmd != 7 && lastWalltime != 0) {
            out.currentTime = (out.time - lastRealtime) + lastWalltime;
            return true;
        }
        return true;
    }

    @Override // android.os.BatteryStats
    public void finishIteratingHistoryLocked() {
        this.mBatteryStatsHistory.finishIteratingHistory();
        this.mIteratingHistory = false;
        this.mReadHistoryStrings = null;
        this.mReadHistoryUids = null;
    }

    @Override // android.os.BatteryStats
    public long getHistoryBaseTime() {
        return this.mHistoryBaseTime;
    }

    @Override // android.os.BatteryStats
    public int getStartCount() {
        return this.mStartCount;
    }

    @UnsupportedAppUsage
    public boolean isOnBattery() {
        return this.mOnBattery;
    }

    public boolean isCharging() {
        return this.mCharging;
    }

    public boolean isScreenOn(int state) {
        return state == 2 || state == 5 || state == 6;
    }

    public boolean isScreenOff(int state) {
        return state == 1;
    }

    public boolean isScreenDoze(int state) {
        return state == 3 || state == 4;
    }

    void initTimes(long uptime, long realtime) {
        this.mStartClockTime = System.currentTimeMillis();
        this.mOnBatteryTimeBase.init(uptime, realtime);
        this.mOnBatteryScreenOffTimeBase.init(uptime, realtime);
        this.mRealtime = 0L;
        this.mUptime = 0L;
        this.mRealtimeStart = realtime;
        this.mUptimeStart = uptime;
    }

    void initDischarge() {
        this.mLowDischargeAmountSinceCharge = 0;
        this.mHighDischargeAmountSinceCharge = 0;
        this.mDischargeAmountScreenOn = 0;
        this.mDischargeAmountScreenOnSinceCharge = 0;
        this.mDischargeAmountScreenOff = 0;
        this.mDischargeAmountScreenOffSinceCharge = 0;
        this.mDischargeAmountScreenDoze = 0;
        this.mDischargeAmountScreenDozeSinceCharge = 0;
        this.mDischargeStepTracker.init();
        this.mChargeStepTracker.init();
        this.mDischargeScreenOffCounter.reset(false);
        this.mDischargeScreenDozeCounter.reset(false);
        this.mDischargeLightDozeCounter.reset(false);
        this.mDischargeDeepDozeCounter.reset(false);
        this.mDischargeCounter.reset(false);
    }

    public void resetAllStatsCmdLocked() {
        resetAllStatsLocked();
        long mSecUptime = this.mClocks.uptimeMillis();
        long uptime = mSecUptime * 1000;
        long mSecRealtime = this.mClocks.elapsedRealtime();
        long realtime = 1000 * mSecRealtime;
        this.mDischargeStartLevel = this.mHistoryCur.batteryLevel;
        pullPendingStateUpdatesLocked();
        addHistoryRecordLocked(mSecRealtime, mSecUptime);
        byte b = this.mHistoryCur.batteryLevel;
        this.mCurrentBatteryLevel = b;
        this.mDischargePlugLevel = b;
        this.mDischargeUnplugLevel = b;
        this.mDischargeCurrentLevel = b;
        this.mOnBatteryTimeBase.reset(uptime, realtime);
        this.mOnBatteryScreenOffTimeBase.reset(uptime, realtime);
        if ((this.mHistoryCur.states & 524288) == 0) {
            if (isScreenOn(this.mScreenState)) {
                this.mDischargeScreenOnUnplugLevel = this.mHistoryCur.batteryLevel;
                this.mDischargeScreenDozeUnplugLevel = 0;
                this.mDischargeScreenOffUnplugLevel = 0;
            } else if (isScreenDoze(this.mScreenState)) {
                this.mDischargeScreenOnUnplugLevel = 0;
                this.mDischargeScreenDozeUnplugLevel = this.mHistoryCur.batteryLevel;
                this.mDischargeScreenOffUnplugLevel = 0;
            } else {
                this.mDischargeScreenOnUnplugLevel = 0;
                this.mDischargeScreenDozeUnplugLevel = 0;
                this.mDischargeScreenOffUnplugLevel = this.mHistoryCur.batteryLevel;
            }
            this.mDischargeAmountScreenOn = 0;
            this.mDischargeAmountScreenOff = 0;
            this.mDischargeAmountScreenDoze = 0;
        }
        initActiveHistoryEventsLocked(mSecRealtime, mSecUptime);
    }

    private void resetAllStatsLocked() {
        long uptimeMillis = this.mClocks.uptimeMillis();
        long elapsedRealtimeMillis = this.mClocks.elapsedRealtime();
        this.mStartCount = 0;
        initTimes(uptimeMillis * 1000, elapsedRealtimeMillis * 1000);
        this.mScreenOnTimer.reset(false);
        this.mScreenDozeTimer.reset(false);
        for (int i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i].reset(false);
        }
        PowerProfile powerProfile = this.mPowerProfile;
        if (powerProfile != null) {
            this.mEstimatedBatteryCapacity = (int) powerProfile.getBatteryCapacity();
        } else {
            this.mEstimatedBatteryCapacity = -1;
        }
        this.mMinLearnedBatteryCapacity = -1;
        this.mMaxLearnedBatteryCapacity = -1;
        this.mInteractiveTimer.reset(false);
        this.mPowerSaveModeEnabledTimer.reset(false);
        this.mLastIdleTimeStart = elapsedRealtimeMillis;
        this.mLongestLightIdleTime = 0L;
        this.mLongestFullIdleTime = 0L;
        this.mDeviceIdleModeLightTimer.reset(false);
        this.mDeviceIdleModeFullTimer.reset(false);
        this.mDeviceLightIdlingTimer.reset(false);
        this.mDeviceIdlingTimer.reset(false);
        this.mPhoneOnTimer.reset(false);
        this.mAudioOnTimer.reset(false);
        this.mVideoOnTimer.reset(false);
        this.mFlashlightOnTimer.reset(false);
        this.mCameraOnTimer.reset(false);
        this.mBluetoothScanTimer.reset(false);
        for (int i2 = 0; i2 < 5; i2++) {
            this.mPhoneSignalStrengthsTimer[i2].reset(false);
        }
        this.mPhoneSignalScanningTimer.reset(false);
        for (int i3 = 0; i3 < 23; i3++) {
            this.mPhoneDataConnectionsTimer[i3].reset(false);
        }
        for (int i4 = 0; i4 < 10; i4++) {
            this.mNetworkByteActivityCounters[i4].reset(false);
            this.mNetworkPacketActivityCounters[i4].reset(false);
        }
        this.mMobileRadioActiveTimer.reset(false);
        this.mMobileRadioActivePerAppTimer.reset(false);
        this.mMobileRadioActiveAdjustedTime.reset(false);
        this.mMobileRadioActiveUnknownTime.reset(false);
        this.mMobileRadioActiveUnknownCount.reset(false);
        this.mWifiOnTimer.reset(false);
        this.mGlobalWifiRunningTimer.reset(false);
        for (int i5 = 0; i5 < 8; i5++) {
            this.mWifiStateTimer[i5].reset(false);
        }
        for (int i6 = 0; i6 < 13; i6++) {
            this.mWifiSupplStateTimer[i6].reset(false);
        }
        for (int i7 = 0; i7 < 5; i7++) {
            this.mWifiSignalStrengthsTimer[i7].reset(false);
        }
        this.mWifiMulticastWakelockTimer.reset(false);
        this.mWifiActiveTimer.reset(false);
        this.mWifiActivity.reset(false);
        for (int i8 = 0; i8 < 2; i8++) {
            this.mGpsSignalQualityTimer[i8].reset(false);
        }
        this.mBluetoothActivity.reset(false);
        this.mModemActivity.reset(false);
        this.mNumConnectivityChange = 0;
        int i9 = 0;
        while (i9 < this.mUidStats.size()) {
            if (this.mUidStats.valueAt(i9).reset(uptimeMillis * 1000, elapsedRealtimeMillis * 1000)) {
                this.mUidStats.valueAt(i9).detachFromTimeBase();
                SparseArray<Uid> sparseArray = this.mUidStats;
                sparseArray.remove(sparseArray.keyAt(i9));
                i9--;
            }
            i9++;
        }
        if (this.mRpmStats.size() > 0) {
            for (SamplingTimer timer : this.mRpmStats.values()) {
                this.mOnBatteryTimeBase.remove(timer);
            }
            this.mRpmStats.clear();
        }
        if (this.mScreenOffRpmStats.size() > 0) {
            for (SamplingTimer timer2 : this.mScreenOffRpmStats.values()) {
                this.mOnBatteryScreenOffTimeBase.remove(timer2);
            }
            this.mScreenOffRpmStats.clear();
        }
        if (this.mKernelWakelockStats.size() > 0) {
            for (SamplingTimer timer3 : this.mKernelWakelockStats.values()) {
                this.mOnBatteryScreenOffTimeBase.remove(timer3);
            }
            this.mKernelWakelockStats.clear();
        }
        if (this.mKernelMemoryStats.size() > 0) {
            for (int i10 = 0; i10 < this.mKernelMemoryStats.size(); i10++) {
                this.mOnBatteryTimeBase.remove(this.mKernelMemoryStats.valueAt(i10));
            }
            this.mKernelMemoryStats.clear();
        }
        if (this.mWakeupReasonStats.size() > 0) {
            for (SamplingTimer timer4 : this.mWakeupReasonStats.values()) {
                this.mOnBatteryTimeBase.remove(timer4);
            }
            this.mWakeupReasonStats.clear();
        }
        this.mTmpRailStats.reset();
        this.mLastHistoryStepDetails = null;
        this.mLastStepCpuSystemTime = 0L;
        this.mLastStepCpuUserTime = 0L;
        this.mCurStepCpuSystemTime = 0L;
        this.mCurStepCpuUserTime = 0L;
        this.mCurStepCpuUserTime = 0L;
        this.mLastStepCpuUserTime = 0L;
        this.mCurStepCpuSystemTime = 0L;
        this.mLastStepCpuSystemTime = 0L;
        this.mCurStepStatUserTime = 0L;
        this.mLastStepStatUserTime = 0L;
        this.mCurStepStatSystemTime = 0L;
        this.mLastStepStatSystemTime = 0L;
        this.mCurStepStatIOWaitTime = 0L;
        this.mLastStepStatIOWaitTime = 0L;
        this.mCurStepStatIrqTime = 0L;
        this.mLastStepStatIrqTime = 0L;
        this.mCurStepStatSoftIrqTime = 0L;
        this.mLastStepStatSoftIrqTime = 0L;
        this.mCurStepStatIdleTime = 0L;
        this.mLastStepStatIdleTime = 0L;
        this.mNumAllUidCpuTimeReads = 0;
        this.mNumUidsRemoved = 0;
        initDischarge();
        clearHistoryLocked();
        this.mBatteryStatsHistory.resetAllFiles();
        this.mHandler.sendEmptyMessage(4);
    }

    private void initActiveHistoryEventsLocked(long elapsedRealtimeMs, long uptimeMs) {
        HashMap<String, SparseIntArray> active;
        for (int i = 0; i < 22; i++) {
            if ((this.mRecordAllHistory || i != 1) && (active = this.mActiveEvents.getStateForEvent(i)) != null) {
                for (Map.Entry<String, SparseIntArray> ent : active.entrySet()) {
                    SparseIntArray uids = ent.getValue();
                    for (int j = 0; j < uids.size(); j++) {
                        addHistoryEventLocked(elapsedRealtimeMs, uptimeMs, i, ent.getKey(), uids.keyAt(j));
                    }
                }
            }
        }
    }

    void updateDischargeScreenLevelsLocked(int oldState, int newState) {
        updateOldDischargeScreenLevelLocked(oldState);
        updateNewDischargeScreenLevelLocked(newState);
    }

    private void updateOldDischargeScreenLevelLocked(int state) {
        int diff;
        if (isScreenOn(state)) {
            int diff2 = this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            if (diff2 > 0) {
                this.mDischargeAmountScreenOn += diff2;
                this.mDischargeAmountScreenOnSinceCharge += diff2;
            }
        } else if (isScreenDoze(state)) {
            int diff3 = this.mDischargeScreenDozeUnplugLevel - this.mDischargeCurrentLevel;
            if (diff3 > 0) {
                this.mDischargeAmountScreenDoze += diff3;
                this.mDischargeAmountScreenDozeSinceCharge += diff3;
            }
        } else if (isScreenOff(state) && (diff = this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel) > 0) {
            this.mDischargeAmountScreenOff += diff;
            this.mDischargeAmountScreenOffSinceCharge += diff;
        }
    }

    private void updateNewDischargeScreenLevelLocked(int state) {
        if (isScreenOn(state)) {
            this.mDischargeScreenOnUnplugLevel = this.mDischargeCurrentLevel;
            this.mDischargeScreenOffUnplugLevel = 0;
            this.mDischargeScreenDozeUnplugLevel = 0;
        } else if (isScreenDoze(state)) {
            this.mDischargeScreenOnUnplugLevel = 0;
            this.mDischargeScreenDozeUnplugLevel = this.mDischargeCurrentLevel;
            this.mDischargeScreenOffUnplugLevel = 0;
        } else if (isScreenOff(state)) {
            this.mDischargeScreenOnUnplugLevel = 0;
            this.mDischargeScreenDozeUnplugLevel = 0;
            this.mDischargeScreenOffUnplugLevel = this.mDischargeCurrentLevel;
        }
    }

    public void pullPendingStateUpdatesLocked() {
        if (this.mOnBatteryInternal) {
            int i = this.mScreenState;
            updateDischargeScreenLevelsLocked(i, i);
        }
    }

    private NetworkStats readNetworkStatsLocked(String[] ifaces) {
        try {
            if (!ArrayUtils.isEmpty(ifaces)) {
                INetworkStatsService statsService = INetworkStatsService.Stub.asInterface(ServiceManager.getService(Context.NETWORK_STATS_SERVICE));
                if (statsService == null) {
                    Slog.e(TAG, "Failed to get networkStatsService ");
                    return null;
                }
                return statsService.getDetailedUidStats(ifaces);
            }
            return null;
        } catch (RemoteException e) {
            Slog.e(TAG, "failed to read network stats for ifaces: " + Arrays.toString(ifaces) + e);
            return null;
        }
    }

    public void updateWifiState(WifiActivityEnergyInfo info) {
        long elapsedRealtimeMs;
        NetworkStats delta;
        long rxTimeMs;
        int uidStatsSize;
        long elapsedRealtimeMs2;
        Uid uid;
        long txTimeMs;
        long scanTimeSinceMarkMs;
        long txTimeMs2;
        long elapsedRealtimeMs3;
        NetworkStats delta2 = null;
        synchronized (this.mWifiNetworkLock) {
            NetworkStats latestStats = readNetworkStatsLocked(this.mWifiIfaces);
            if (latestStats != null) {
                delta2 = NetworkStats.subtract(latestStats, this.mLastWifiNetworkStats, null, null, this.mNetworkStatsPool.acquire());
                this.mNetworkStatsPool.release(this.mLastWifiNetworkStats);
                this.mLastWifiNetworkStats = latestStats;
            }
        }
        synchronized (this) {
            try {
                if (!this.mOnBatteryInternal) {
                    if (delta2 != null) {
                        this.mNetworkStatsPool.release(delta2);
                    }
                    return;
                }
                long elapsedRealtimeMs4 = this.mClocks.elapsedRealtime();
                SparseLongArray rxPackets = new SparseLongArray();
                SparseLongArray txPackets = new SparseLongArray();
                long totalTxPackets = 0;
                long totalRxPackets = 0;
                if (delta2 == null) {
                    elapsedRealtimeMs = elapsedRealtimeMs4;
                } else {
                    NetworkStats.Entry entry = new NetworkStats.Entry();
                    int size = delta2.size();
                    int i = 0;
                    while (i < size) {
                        entry = delta2.getValues(i, entry);
                        if (entry.rxBytes != 0 || entry.txBytes != 0) {
                            Uid u = getUidStatsLocked(mapUid(entry.uid));
                            if (entry.rxBytes == 0) {
                                elapsedRealtimeMs3 = elapsedRealtimeMs4;
                            } else {
                                elapsedRealtimeMs3 = elapsedRealtimeMs4;
                                u.noteNetworkActivityLocked(2, entry.rxBytes, entry.rxPackets);
                                if (entry.set == 0) {
                                    u.noteNetworkActivityLocked(8, entry.rxBytes, entry.rxPackets);
                                }
                                this.mNetworkByteActivityCounters[2].addCountLocked(entry.rxBytes);
                                this.mNetworkPacketActivityCounters[2].addCountLocked(entry.rxPackets);
                                rxPackets.put(u.getUid(), entry.rxPackets);
                                totalRxPackets += entry.rxPackets;
                            }
                            if (entry.txBytes != 0) {
                                u.noteNetworkActivityLocked(3, entry.txBytes, entry.txPackets);
                                if (entry.set == 0) {
                                    u.noteNetworkActivityLocked(9, entry.txBytes, entry.txPackets);
                                }
                                this.mNetworkByteActivityCounters[3].addCountLocked(entry.txBytes);
                                this.mNetworkPacketActivityCounters[3].addCountLocked(entry.txPackets);
                                txPackets.put(u.getUid(), entry.txPackets);
                                totalTxPackets += entry.txPackets;
                            }
                        } else {
                            elapsedRealtimeMs3 = elapsedRealtimeMs4;
                        }
                        i++;
                        elapsedRealtimeMs4 = elapsedRealtimeMs3;
                    }
                    elapsedRealtimeMs = elapsedRealtimeMs4;
                    this.mNetworkStatsPool.release(delta2);
                    delta2 = null;
                }
                if (info != null) {
                    try {
                        this.mHasWifiReporting = true;
                        long txTimeMs3 = info.getControllerTxTimeMillis();
                        long rxTimeMs2 = info.getControllerRxTimeMillis();
                        long scanTimeMs = info.getControllerScanTimeMillis();
                        long idleTimeMs = info.getControllerIdleTimeMillis();
                        long j = txTimeMs3 + rxTimeMs2 + idleTimeMs;
                        long leftOverRxTimeMs = rxTimeMs2;
                        long leftOverTxTimeMs = txTimeMs3;
                        long totalWifiLockTimeMs = 0;
                        long totalScanTimeMs = 0;
                        int uidStatsSize2 = this.mUidStats.size();
                        int i2 = 0;
                        while (i2 < uidStatsSize2) {
                            delta = delta2;
                            try {
                                Uid uid2 = this.mUidStats.valueAt(i2);
                                totalScanTimeMs += uid2.mWifiScanTimer.getTimeSinceMarkLocked(elapsedRealtimeMs * 1000) / 1000;
                                totalWifiLockTimeMs += uid2.mFullWifiLockTimer.getTimeSinceMarkLocked(elapsedRealtimeMs * 1000) / 1000;
                                i2++;
                                delta2 = delta;
                                scanTimeMs = scanTimeMs;
                                totalRxPackets = totalRxPackets;
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                        delta = delta2;
                        long totalRxPackets2 = totalRxPackets;
                        int i3 = 0;
                        while (i3 < uidStatsSize2) {
                            Uid uid3 = this.mUidStats.valueAt(i3);
                            long scanTimeSinceMarkMs2 = uid3.mWifiScanTimer.getTimeSinceMarkLocked(elapsedRealtimeMs * 1000) / 1000;
                            if (scanTimeSinceMarkMs2 > 0) {
                                uid = uid3;
                                uidStatsSize = uidStatsSize2;
                                elapsedRealtimeMs2 = elapsedRealtimeMs;
                                uid.mWifiScanTimer.setMark(elapsedRealtimeMs2);
                                if (totalScanTimeMs <= rxTimeMs2) {
                                    scanTimeSinceMarkMs = scanTimeSinceMarkMs2;
                                } else {
                                    long scanRxTimeSinceMarkMs = (rxTimeMs2 * scanTimeSinceMarkMs2) / totalScanTimeMs;
                                    scanTimeSinceMarkMs = scanRxTimeSinceMarkMs;
                                }
                                if (totalScanTimeMs <= txTimeMs3) {
                                    txTimeMs = txTimeMs3;
                                    txTimeMs2 = scanTimeSinceMarkMs2;
                                } else {
                                    long scanTxTimeSinceMarkMs = (txTimeMs3 * scanTimeSinceMarkMs2) / totalScanTimeMs;
                                    txTimeMs = txTimeMs3;
                                    txTimeMs2 = scanTxTimeSinceMarkMs;
                                }
                                ControllerActivityCounterImpl activityCounter = uid.getOrCreateWifiControllerActivityLocked();
                                rxTimeMs = rxTimeMs2;
                                activityCounter.getRxTimeCounter().addCountLocked(scanTimeSinceMarkMs);
                                activityCounter.getTxTimeCounters()[0].addCountLocked(txTimeMs2);
                                leftOverRxTimeMs -= scanTimeSinceMarkMs;
                                leftOverTxTimeMs -= txTimeMs2;
                            } else {
                                rxTimeMs = rxTimeMs2;
                                uidStatsSize = uidStatsSize2;
                                elapsedRealtimeMs2 = elapsedRealtimeMs;
                                uid = uid3;
                                txTimeMs = txTimeMs3;
                            }
                            long wifiLockTimeSinceMarkMs = uid.mFullWifiLockTimer.getTimeSinceMarkLocked(elapsedRealtimeMs2 * 1000) / 1000;
                            if (wifiLockTimeSinceMarkMs > 0) {
                                uid.mFullWifiLockTimer.setMark(elapsedRealtimeMs2);
                                long myIdleTimeMs = (wifiLockTimeSinceMarkMs * idleTimeMs) / totalWifiLockTimeMs;
                                uid.getOrCreateWifiControllerActivityLocked().getIdleTimeCounter().addCountLocked(myIdleTimeMs);
                            }
                            i3++;
                            txTimeMs3 = txTimeMs;
                            rxTimeMs2 = rxTimeMs;
                            elapsedRealtimeMs = elapsedRealtimeMs2;
                            uidStatsSize2 = uidStatsSize;
                        }
                        for (int i4 = 0; i4 < txPackets.size(); i4++) {
                            Uid uid4 = getUidStatsLocked(txPackets.keyAt(i4));
                            long myTxTimeMs = (txPackets.valueAt(i4) * leftOverTxTimeMs) / totalTxPackets;
                            uid4.getOrCreateWifiControllerActivityLocked().getTxTimeCounters()[0].addCountLocked(myTxTimeMs);
                        }
                        for (int i5 = 0; i5 < rxPackets.size(); i5++) {
                            Uid uid5 = getUidStatsLocked(rxPackets.keyAt(i5));
                            long myRxTimeMs = (rxPackets.valueAt(i5) * leftOverRxTimeMs) / totalRxPackets2;
                            uid5.getOrCreateWifiControllerActivityLocked().getRxTimeCounter().addCountLocked(myRxTimeMs);
                        }
                        this.mWifiActivity.getRxTimeCounter().addCountLocked(info.getControllerRxTimeMillis());
                        this.mWifiActivity.getTxTimeCounters()[0].addCountLocked(info.getControllerTxTimeMillis());
                        this.mWifiActivity.getScanTimeCounter().addCountLocked(info.getControllerScanTimeMillis());
                        this.mWifiActivity.getIdleTimeCounter().addCountLocked(info.getControllerIdleTimeMillis());
                        double opVolt = this.mPowerProfile.getAveragePower(PowerProfile.POWER_WIFI_CONTROLLER_OPERATING_VOLTAGE) / 1000.0d;
                        if (opVolt != FeatureOption.FO_BOOT_POLICY_CPU) {
                            this.mWifiActivity.getPowerCounter().addCountLocked((long) (info.getControllerEnergyUsed() / opVolt));
                        }
                        long monitoredRailChargeConsumedMaMs = (long) (this.mTmpRailStats.getWifiTotalEnergyUseduWs() / opVolt);
                        this.mWifiActivity.getMonitoredRailChargeConsumedMaMs().addCountLocked(monitoredRailChargeConsumedMaMs);
                        this.mHistoryCur.wifiRailChargeMah += monitoredRailChargeConsumedMaMs / 3600000.0d;
                        addHistoryRecordLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis());
                        this.mTmpRailStats.resetWifiTotalEnergyUsed();
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                } else {
                    delta = delta2;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    private ModemActivityInfo getDeltaModemActivityInfo(ModemActivityInfo activityInfo) {
        if (activityInfo == null) {
            return null;
        }
        int[] txTimeMs = new int[5];
        for (int i = 0; i < 5; i++) {
            txTimeMs[i] = activityInfo.getTxTimeMillis()[i] - this.mLastModemActivityInfo.getTxTimeMillis()[i];
        }
        ModemActivityInfo deltaInfo = new ModemActivityInfo(activityInfo.getTimestamp(), activityInfo.getSleepTimeMillis() - this.mLastModemActivityInfo.getSleepTimeMillis(), activityInfo.getIdleTimeMillis() - this.mLastModemActivityInfo.getIdleTimeMillis(), txTimeMs, activityInfo.getRxTimeMillis() - this.mLastModemActivityInfo.getRxTimeMillis(), activityInfo.getEnergyUsed() - this.mLastModemActivityInfo.getEnergyUsed());
        this.mLastModemActivityInfo = activityInfo;
        return deltaInfo;
    }

    public void updateMobileRadioState(ModemActivityInfo activityInfo) {
        long radioTime;
        BatteryStatsImpl batteryStatsImpl;
        int i;
        long totalTxPackets;
        ModemActivityInfo deltaInfo;
        long totalPackets;
        long elapsedRealtimeMs;
        long radioTime2;
        boolean z;
        BatteryStatsImpl batteryStatsImpl2 = this;
        ModemActivityInfo deltaInfo2 = getDeltaModemActivityInfo(activityInfo);
        batteryStatsImpl2.addModemTxPowerToHistory(deltaInfo2);
        NetworkStats delta = null;
        synchronized (batteryStatsImpl2.mModemNetworkLock) {
            try {
                NetworkStats latestStats = batteryStatsImpl2.readNetworkStatsLocked(batteryStatsImpl2.mModemIfaces);
                if (latestStats != null) {
                    try {
                        delta = NetworkStats.subtract(latestStats, batteryStatsImpl2.mLastModemNetworkStats, null, null, batteryStatsImpl2.mNetworkStatsPool.acquire());
                        batteryStatsImpl2.mNetworkStatsPool.release(batteryStatsImpl2.mLastModemNetworkStats);
                        batteryStatsImpl2.mLastModemNetworkStats = latestStats;
                    } catch (Throwable th) {
                        th = th;
                        while (true) {
                            try {
                                break;
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        }
                        throw th;
                    }
                }
                synchronized (this) {
                    try {
                        try {
                            try {
                                if (!batteryStatsImpl2.mOnBatteryInternal) {
                                    if (delta != null) {
                                        batteryStatsImpl2.mNetworkStatsPool.release(delta);
                                    }
                                    return;
                                }
                                if (deltaInfo2 != null) {
                                    batteryStatsImpl2.mHasModemReporting = true;
                                    batteryStatsImpl2.mModemActivity.getIdleTimeCounter().addCountLocked(deltaInfo2.getIdleTimeMillis());
                                    batteryStatsImpl2.mModemActivity.getSleepTimeCounter().addCountLocked(deltaInfo2.getSleepTimeMillis());
                                    batteryStatsImpl2.mModemActivity.getRxTimeCounter().addCountLocked(deltaInfo2.getRxTimeMillis());
                                    for (int lvl = 0; lvl < 5; lvl++) {
                                        batteryStatsImpl2.mModemActivity.getTxTimeCounters()[lvl].addCountLocked(deltaInfo2.getTxTimeMillis()[lvl]);
                                    }
                                    double opVolt = batteryStatsImpl2.mPowerProfile.getAveragePower(PowerProfile.POWER_MODEM_CONTROLLER_OPERATING_VOLTAGE) / 1000.0d;
                                    if (opVolt != FeatureOption.FO_BOOT_POLICY_CPU) {
                                        double energyUsed = (deltaInfo2.getSleepTimeMillis() * batteryStatsImpl2.mPowerProfile.getAveragePower(PowerProfile.POWER_MODEM_CONTROLLER_SLEEP)) + (deltaInfo2.getIdleTimeMillis() * batteryStatsImpl2.mPowerProfile.getAveragePower(PowerProfile.POWER_MODEM_CONTROLLER_IDLE)) + (deltaInfo2.getRxTimeMillis() * batteryStatsImpl2.mPowerProfile.getAveragePower(PowerProfile.POWER_MODEM_CONTROLLER_RX));
                                        int[] txTimeMs = deltaInfo2.getTxTimeMillis();
                                        for (int i2 = 0; i2 < Math.min(txTimeMs.length, 5); i2++) {
                                            energyUsed += txTimeMs[i2] * batteryStatsImpl2.mPowerProfile.getAveragePower(PowerProfile.POWER_MODEM_CONTROLLER_TX, i2);
                                        }
                                        batteryStatsImpl2.mModemActivity.getPowerCounter().addCountLocked((long) energyUsed);
                                        long monitoredRailChargeConsumedMaMs = (long) (batteryStatsImpl2.mTmpRailStats.getCellularTotalEnergyUseduWs() / opVolt);
                                        batteryStatsImpl2.mModemActivity.getMonitoredRailChargeConsumedMaMs().addCountLocked(monitoredRailChargeConsumedMaMs);
                                        batteryStatsImpl2.mHistoryCur.modemRailChargeMah += monitoredRailChargeConsumedMaMs / 3600000.0d;
                                        batteryStatsImpl2.addHistoryRecordLocked(batteryStatsImpl2.mClocks.elapsedRealtime(), batteryStatsImpl2.mClocks.uptimeMillis());
                                        batteryStatsImpl2.mTmpRailStats.resetCellularTotalEnergyUsed();
                                    }
                                }
                                long elapsedRealtimeMs2 = batteryStatsImpl2.mClocks.elapsedRealtime();
                                long radioTime3 = batteryStatsImpl2.mMobileRadioActivePerAppTimer.getTimeSinceMarkLocked(1000 * elapsedRealtimeMs2);
                                batteryStatsImpl2.mMobileRadioActivePerAppTimer.setMark(elapsedRealtimeMs2);
                                long totalRxPackets = 0;
                                long totalTxPackets2 = 0;
                                if (delta != null) {
                                    NetworkStats.Entry entry = new NetworkStats.Entry();
                                    int size = delta.size();
                                    int i3 = 0;
                                    while (i3 < size) {
                                        entry = delta.getValues(i3, entry);
                                        try {
                                            if (entry.rxPackets == 0 && entry.txPackets == 0) {
                                                batteryStatsImpl2 = this;
                                                elapsedRealtimeMs = elapsedRealtimeMs2;
                                                radioTime2 = radioTime3;
                                                z = false;
                                            } else {
                                                totalRxPackets += entry.rxPackets;
                                                totalTxPackets2 += entry.txPackets;
                                                batteryStatsImpl2 = this;
                                                Uid u = batteryStatsImpl2.getUidStatsLocked(batteryStatsImpl2.mapUid(entry.uid));
                                                elapsedRealtimeMs = elapsedRealtimeMs2;
                                                long elapsedRealtimeMs3 = entry.rxBytes;
                                                radioTime2 = radioTime3;
                                                long radioTime4 = entry.rxPackets;
                                                u.noteNetworkActivityLocked(0, elapsedRealtimeMs3, radioTime4);
                                                u.noteNetworkActivityLocked(1, entry.txBytes, entry.txPackets);
                                                if (entry.set == 0) {
                                                    u.noteNetworkActivityLocked(6, entry.rxBytes, entry.rxPackets);
                                                    u.noteNetworkActivityLocked(7, entry.txBytes, entry.txPackets);
                                                }
                                                batteryStatsImpl2.mNetworkByteActivityCounters[0].addCountLocked(entry.rxBytes);
                                                batteryStatsImpl2.mNetworkByteActivityCounters[1].addCountLocked(entry.txBytes);
                                                z = false;
                                                batteryStatsImpl2.mNetworkPacketActivityCounters[0].addCountLocked(entry.rxPackets);
                                                batteryStatsImpl2.mNetworkPacketActivityCounters[1].addCountLocked(entry.txPackets);
                                            }
                                            i3++;
                                            elapsedRealtimeMs2 = elapsedRealtimeMs;
                                            radioTime3 = radioTime2;
                                        } catch (Throwable th3) {
                                            th = th3;
                                            throw th;
                                        }
                                    }
                                    long radioTime5 = radioTime3;
                                    long totalPackets2 = totalRxPackets + totalTxPackets2;
                                    if (totalPackets2 > 0) {
                                        int i4 = 0;
                                        radioTime = radioTime5;
                                        while (i4 < size) {
                                            try {
                                                entry = delta.getValues(i4, entry);
                                                if (entry.rxPackets == 0 && entry.txPackets == 0) {
                                                    i = i4;
                                                    deltaInfo = deltaInfo2;
                                                    totalTxPackets = totalTxPackets2;
                                                } else {
                                                    Uid u2 = batteryStatsImpl2.getUidStatsLocked(batteryStatsImpl2.mapUid(entry.uid));
                                                    i = i4;
                                                    long j = entry.rxPackets;
                                                    totalTxPackets = totalTxPackets2;
                                                    long totalTxPackets3 = entry.txPackets;
                                                    long appPackets = j + totalTxPackets3;
                                                    long appRadioTime = (radioTime * appPackets) / totalPackets2;
                                                    u2.noteMobileRadioActiveTimeLocked(appRadioTime);
                                                    radioTime -= appRadioTime;
                                                    long totalPackets3 = totalPackets2 - appPackets;
                                                    if (deltaInfo2 != null) {
                                                        ControllerActivityCounterImpl activityCounter = u2.getOrCreateModemControllerActivityLocked();
                                                        if (totalRxPackets <= 0) {
                                                            totalPackets = totalPackets3;
                                                        } else if (entry.rxPackets > 0) {
                                                            totalPackets = totalPackets3;
                                                            long rxMs = (entry.rxPackets * deltaInfo2.getRxTimeMillis()) / totalRxPackets;
                                                            activityCounter.getRxTimeCounter().addCountLocked(rxMs);
                                                        } else {
                                                            totalPackets = totalPackets3;
                                                        }
                                                        if (totalTxPackets <= 0 || entry.txPackets <= 0) {
                                                            deltaInfo = deltaInfo2;
                                                        } else {
                                                            int lvl2 = 0;
                                                            while (lvl2 < 5) {
                                                                ModemActivityInfo deltaInfo3 = deltaInfo2;
                                                                long txMs = entry.txPackets * deltaInfo2.getTxTimeMillis()[lvl2];
                                                                try {
                                                                    activityCounter.getTxTimeCounters()[lvl2].addCountLocked(txMs / totalTxPackets);
                                                                    lvl2++;
                                                                    deltaInfo2 = deltaInfo3;
                                                                } catch (Throwable th4) {
                                                                    th = th4;
                                                                    throw th;
                                                                }
                                                            }
                                                            deltaInfo = deltaInfo2;
                                                        }
                                                    } else {
                                                        deltaInfo = deltaInfo2;
                                                        totalPackets = totalPackets3;
                                                    }
                                                    totalPackets2 = totalPackets;
                                                }
                                                i4 = i + 1;
                                                batteryStatsImpl2 = this;
                                                deltaInfo2 = deltaInfo;
                                                totalTxPackets2 = totalTxPackets;
                                            } catch (Throwable th5) {
                                                th = th5;
                                            }
                                        }
                                    } else {
                                        radioTime = radioTime5;
                                    }
                                    if (radioTime > 0) {
                                        batteryStatsImpl = this;
                                        batteryStatsImpl.mMobileRadioActiveUnknownTime.addCountLocked(radioTime);
                                        batteryStatsImpl.mMobileRadioActiveUnknownCount.addCountLocked(1L);
                                    } else {
                                        batteryStatsImpl = this;
                                    }
                                    batteryStatsImpl.mNetworkStatsPool.release(delta);
                                }
                            } catch (Throwable th6) {
                                th = th6;
                            }
                        } catch (Throwable th7) {
                            th = th7;
                        }
                    } catch (Throwable th8) {
                        th = th8;
                    }
                }
            } catch (Throwable th9) {
                th = th9;
            }
        }
    }

    private synchronized void addModemTxPowerToHistory(ModemActivityInfo activityInfo) {
        if (activityInfo == null) {
            return;
        }
        int[] txTimeMs = activityInfo.getTxTimeMillis();
        if (txTimeMs != null && txTimeMs.length == 5) {
            long elapsedRealtime = this.mClocks.elapsedRealtime();
            long uptime = this.mClocks.uptimeMillis();
            int levelMaxTimeSpent = 0;
            for (int i = 1; i < txTimeMs.length; i++) {
                if (txTimeMs[i] > txTimeMs[levelMaxTimeSpent]) {
                    levelMaxTimeSpent = i;
                }
            }
            if (levelMaxTimeSpent == 4) {
                this.mHistoryCur.states2 |= 524288;
                addHistoryRecordLocked(elapsedRealtime, uptime);
            }
        }
    }

    /* loaded from: classes3.dex */
    private final class BluetoothActivityInfoCache {
        long energy;
        long idleTimeMs;
        long rxTimeMs;
        long txTimeMs;
        SparseLongArray uidRxBytes;
        SparseLongArray uidTxBytes;

        private BluetoothActivityInfoCache() {
            this.uidRxBytes = new SparseLongArray();
            this.uidTxBytes = new SparseLongArray();
        }

        void set(BluetoothActivityEnergyInfo info) {
            UidTraffic[] uidTraffic;
            this.idleTimeMs = info.getControllerIdleTimeMillis();
            this.rxTimeMs = info.getControllerRxTimeMillis();
            this.txTimeMs = info.getControllerTxTimeMillis();
            this.energy = info.getControllerEnergyUsed();
            if (info.getUidTraffic() != null) {
                for (UidTraffic traffic : info.getUidTraffic()) {
                    this.uidRxBytes.put(traffic.getUid(), traffic.getRxBytes());
                    this.uidTxBytes.put(traffic.getUid(), traffic.getTxBytes());
                }
            }
        }
    }

    public void updateBluetoothStateLocked(BluetoothActivityEnergyInfo info) {
        long totalRxBytes;
        UidTraffic[] uidTraffic;
        long idleTimeMs;
        boolean normalizeScanRxTime;
        long elapsedRealtimeMs;
        long elapsedRealtimeMs2;
        long scanTimeSinceMarkMs;
        if (info != null && this.mOnBatteryInternal) {
            this.mHasBluetoothReporting = true;
            long elapsedRealtimeMs3 = this.mClocks.elapsedRealtime();
            long rxTimeMs = info.getControllerRxTimeMillis() - this.mLastBluetoothActivityInfo.rxTimeMs;
            long txTimeMs = info.getControllerTxTimeMillis() - this.mLastBluetoothActivityInfo.txTimeMs;
            long idleTimeMs2 = info.getControllerIdleTimeMillis() - this.mLastBluetoothActivityInfo.idleTimeMs;
            long totalScanTimeMs = 0;
            int uidCount = this.mUidStats.size();
            for (int i = 0; i < uidCount; i++) {
                Uid u = this.mUidStats.valueAt(i);
                if (u.mBluetoothScanTimer != null) {
                    totalScanTimeMs += u.mBluetoothScanTimer.getTimeSinceMarkLocked(elapsedRealtimeMs3 * 1000) / 1000;
                }
            }
            boolean normalizeScanRxTime2 = totalScanTimeMs > rxTimeMs;
            boolean normalizeScanTxTime = totalScanTimeMs > txTimeMs;
            long leftOverTxTimeMs = txTimeMs;
            int i2 = 0;
            long leftOverRxTimeMs = rxTimeMs;
            while (i2 < uidCount) {
                Uid u2 = this.mUidStats.valueAt(i2);
                int uidCount2 = uidCount;
                if (u2.mBluetoothScanTimer == null) {
                    normalizeScanRxTime = normalizeScanRxTime2;
                    elapsedRealtimeMs = elapsedRealtimeMs3;
                    idleTimeMs = idleTimeMs2;
                } else {
                    idleTimeMs = idleTimeMs2;
                    long idleTimeMs3 = elapsedRealtimeMs3 * 1000;
                    long scanTimeSinceMarkMs2 = u2.mBluetoothScanTimer.getTimeSinceMarkLocked(idleTimeMs3) / 1000;
                    if (scanTimeSinceMarkMs2 <= 0) {
                        normalizeScanRxTime = normalizeScanRxTime2;
                        elapsedRealtimeMs = elapsedRealtimeMs3;
                    } else {
                        u2.mBluetoothScanTimer.setMark(elapsedRealtimeMs3);
                        if (!normalizeScanRxTime2) {
                            elapsedRealtimeMs = elapsedRealtimeMs3;
                            elapsedRealtimeMs2 = scanTimeSinceMarkMs2;
                        } else {
                            long scanTimeRxSinceMarkMs = (rxTimeMs * scanTimeSinceMarkMs2) / totalScanTimeMs;
                            elapsedRealtimeMs = elapsedRealtimeMs3;
                            elapsedRealtimeMs2 = scanTimeRxSinceMarkMs;
                        }
                        if (!normalizeScanTxTime) {
                            scanTimeSinceMarkMs = scanTimeSinceMarkMs2;
                        } else {
                            long scanTimeTxSinceMarkMs = (txTimeMs * scanTimeSinceMarkMs2) / totalScanTimeMs;
                            scanTimeSinceMarkMs = scanTimeTxSinceMarkMs;
                        }
                        ControllerActivityCounterImpl counter = u2.getOrCreateBluetoothControllerActivityLocked();
                        normalizeScanRxTime = normalizeScanRxTime2;
                        counter.getRxTimeCounter().addCountLocked(elapsedRealtimeMs2);
                        counter.getTxTimeCounters()[0].addCountLocked(scanTimeSinceMarkMs);
                        leftOverRxTimeMs -= elapsedRealtimeMs2;
                        leftOverTxTimeMs -= scanTimeSinceMarkMs;
                    }
                }
                i2++;
                uidCount = uidCount2;
                idleTimeMs2 = idleTimeMs;
                normalizeScanRxTime2 = normalizeScanRxTime;
                elapsedRealtimeMs3 = elapsedRealtimeMs;
            }
            long idleTimeMs4 = idleTimeMs2;
            long totalTxBytes = 0;
            long totalRxBytes2 = 0;
            UidTraffic[] uidTraffic2 = info.getUidTraffic();
            int numUids = uidTraffic2 != null ? uidTraffic2.length : 0;
            int i3 = 0;
            while (i3 < numUids) {
                UidTraffic traffic = uidTraffic2[i3];
                long totalScanTimeMs2 = totalScanTimeMs;
                long rxBytes = traffic.getRxBytes() - this.mLastBluetoothActivityInfo.uidRxBytes.get(traffic.getUid());
                boolean normalizeScanTxTime2 = normalizeScanTxTime;
                long txTimeMs2 = txTimeMs;
                long txBytes = traffic.getTxBytes() - this.mLastBluetoothActivityInfo.uidTxBytes.get(traffic.getUid());
                this.mNetworkByteActivityCounters[4].addCountLocked(rxBytes);
                this.mNetworkByteActivityCounters[5].addCountLocked(txBytes);
                Uid u3 = getUidStatsLocked(mapUid(traffic.getUid()));
                u3.noteNetworkActivityLocked(4, rxBytes, 0L);
                u3.noteNetworkActivityLocked(5, txBytes, 0L);
                totalRxBytes2 += rxBytes;
                totalTxBytes += txBytes;
                i3++;
                normalizeScanTxTime = normalizeScanTxTime2;
                totalScanTimeMs = totalScanTimeMs2;
                txTimeMs = txTimeMs2;
            }
            long txTimeMs3 = txTimeMs;
            if ((totalTxBytes != 0 || totalRxBytes2 != 0) && (leftOverRxTimeMs != 0 || leftOverTxTimeMs != 0)) {
                int i4 = 0;
                while (i4 < numUids) {
                    UidTraffic traffic2 = uidTraffic2[i4];
                    int uid = traffic2.getUid();
                    long rxBytes2 = traffic2.getRxBytes() - this.mLastBluetoothActivityInfo.uidRxBytes.get(uid);
                    long txBytes2 = traffic2.getTxBytes() - this.mLastBluetoothActivityInfo.uidTxBytes.get(uid);
                    ControllerActivityCounterImpl counter2 = getUidStatsLocked(mapUid(uid)).getOrCreateBluetoothControllerActivityLocked();
                    if (totalRxBytes2 <= 0 || rxBytes2 <= 0) {
                        totalRxBytes = totalRxBytes2;
                        uidTraffic = uidTraffic2;
                    } else {
                        uidTraffic = uidTraffic2;
                        long timeRxMs = (leftOverRxTimeMs * rxBytes2) / totalRxBytes2;
                        totalRxBytes = totalRxBytes2;
                        counter2.getRxTimeCounter().addCountLocked(timeRxMs);
                    }
                    if (totalTxBytes > 0 && txBytes2 > 0) {
                        long timeTxMs = (leftOverTxTimeMs * txBytes2) / totalTxBytes;
                        counter2.getTxTimeCounters()[0].addCountLocked(timeTxMs);
                    }
                    i4++;
                    totalRxBytes2 = totalRxBytes;
                    uidTraffic2 = uidTraffic;
                }
            }
            this.mBluetoothActivity.getRxTimeCounter().addCountLocked(rxTimeMs);
            this.mBluetoothActivity.getTxTimeCounters()[0].addCountLocked(txTimeMs3);
            this.mBluetoothActivity.getIdleTimeCounter().addCountLocked(idleTimeMs4);
            double opVolt = this.mPowerProfile.getAveragePower(PowerProfile.POWER_BLUETOOTH_CONTROLLER_OPERATING_VOLTAGE) / 1000.0d;
            if (opVolt != FeatureOption.FO_BOOT_POLICY_CPU) {
                LongSamplingCounter powerCounter = this.mBluetoothActivity.getPowerCounter();
                long controllerEnergyUsed = info.getControllerEnergyUsed();
                long totalTxBytes2 = this.mLastBluetoothActivityInfo.energy;
                powerCounter.addCountLocked((long) ((controllerEnergyUsed - totalTxBytes2) / opVolt));
            }
            this.mLastBluetoothActivityInfo.set(info);
        }
    }

    public void updateRpmStatsLocked() {
        if (this.mPlatformIdleStateCallback == null) {
            return;
        }
        long vTimeUs = SystemClock.elapsedRealtime();
        long j = 1000;
        if (vTimeUs - this.mLastRpmStatsUpdateTimeMs >= 1000) {
            this.mPlatformIdleStateCallback.fillLowPowerStats(this.mTmpRpmStats);
            this.mLastRpmStatsUpdateTimeMs = vTimeUs;
        }
        for (Map.Entry<String, RpmStats.PowerStatePlatformSleepState> pstate : this.mTmpRpmStats.mPlatformLowPowerStats.entrySet()) {
            String pName = pstate.getKey();
            long pTimeUs = pstate.getValue().mTimeMs * j;
            int pCount = pstate.getValue().mCount;
            getRpmTimerLocked(pName).update(pTimeUs, pCount);
            for (Map.Entry<String, RpmStats.PowerStateElement> voter : pstate.getValue().mVoters.entrySet()) {
                String vName = pName + "." + voter.getKey();
                long now = vTimeUs;
                long now2 = voter.getValue().mTimeMs;
                int vCount = voter.getValue().mCount;
                getRpmTimerLocked(vName).update(now2 * j, vCount);
                vTimeUs = now;
                j = 1000;
            }
            j = 1000;
        }
        for (Map.Entry<String, RpmStats.PowerStateSubsystem> subsys : this.mTmpRpmStats.mSubsystemLowPowerStats.entrySet()) {
            String subsysName = subsys.getKey();
            for (Map.Entry<String, RpmStats.PowerStateElement> sstate : subsys.getValue().mStates.entrySet()) {
                String name = subsysName + "." + sstate.getKey();
                long timeUs = sstate.getValue().mTimeMs * 1000;
                int count = sstate.getValue().mCount;
                getRpmTimerLocked(name).update(timeUs, count);
            }
        }
    }

    public void updateRailStatsLocked() {
        if (this.mRailEnergyDataCallback == null || !this.mTmpRailStats.isRailStatsAvailable()) {
            return;
        }
        this.mRailEnergyDataCallback.fillRailDataStats(this.mTmpRailStats);
    }

    public void updateKernelWakelocksLocked() {
        KernelWakelockStats wakelockStats = this.mKernelWakelockReader.readKernelWakelockStats(this.mTmpWakelockStats);
        if (wakelockStats == null) {
            Slog.w(TAG, "Couldn't get kernel wake lock stats");
            return;
        }
        for (Map.Entry<String, KernelWakelockStats.Entry> ent : wakelockStats.entrySet()) {
            String name = ent.getKey();
            KernelWakelockStats.Entry kws = ent.getValue();
            SamplingTimer kwlt = this.mKernelWakelockStats.get(name);
            if (kwlt == null) {
                kwlt = new SamplingTimer(this.mClocks, this.mOnBatteryScreenOffTimeBase);
                this.mKernelWakelockStats.put(name, kwlt);
            }
            kwlt.update(kws.mTotalTime, kws.mCount);
            kwlt.setUpdateVersion(kws.mVersion);
        }
        int numWakelocksSetStale = 0;
        for (Map.Entry<String, SamplingTimer> ent2 : this.mKernelWakelockStats.entrySet()) {
            SamplingTimer st = ent2.getValue();
            if (st.getUpdateVersion() != wakelockStats.kernelWakelockVersion) {
                st.endSample();
                numWakelocksSetStale++;
            }
        }
        if (wakelockStats.isEmpty()) {
            Slog.wtf(TAG, "All kernel wakelocks had time of zero");
        }
        if (numWakelocksSetStale == this.mKernelWakelockStats.size()) {
            Slog.wtf(TAG, "All kernel wakelocks were set stale. new version=" + wakelockStats.kernelWakelockVersion);
        }
    }

    public void updateKernelMemoryBandwidthLocked() {
        SamplingTimer timer;
        this.mKernelMemoryBandwidthStats.updateStats();
        LongSparseLongArray bandwidthEntries = this.mKernelMemoryBandwidthStats.getBandwidthEntries();
        int bandwidthEntryCount = bandwidthEntries.size();
        for (int i = 0; i < bandwidthEntryCount; i++) {
            int index = this.mKernelMemoryStats.indexOfKey(bandwidthEntries.keyAt(i));
            if (index >= 0) {
                timer = this.mKernelMemoryStats.valueAt(index);
            } else {
                timer = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase);
                this.mKernelMemoryStats.put(bandwidthEntries.keyAt(i), timer);
            }
            timer.update(bandwidthEntries.valueAt(i), 1);
        }
    }

    public boolean isOnBatteryLocked() {
        return this.mOnBatteryTimeBase.isRunning();
    }

    public boolean isOnBatteryScreenOffLocked() {
        return this.mOnBatteryScreenOffTimeBase.isRunning();
    }

    @GuardedBy({"this"})
    public void updateCpuTimeLocked(boolean onBattery, boolean onBatteryScreenOff) {
        PowerProfile powerProfile = this.mPowerProfile;
        if (powerProfile == null) {
            return;
        }
        if (this.mCpuFreqs == null) {
            this.mCpuFreqs = this.mCpuUidFreqTimeReader.readFreqs(powerProfile);
        }
        ArrayList<StopwatchTimer> partialTimersToConsider = null;
        if (onBatteryScreenOff) {
            partialTimersToConsider = new ArrayList<>();
            for (int i = this.mPartialTimers.size() - 1; i >= 0; i--) {
                StopwatchTimer timer = this.mPartialTimers.get(i);
                if (timer.mInList && timer.mUid != null && timer.mUid.mUid != 1000) {
                    partialTimersToConsider.add(timer);
                }
            }
        }
        markPartialTimersAsEligible();
        if (!onBattery) {
            this.mCpuUidUserSysTimeReader.readDelta(null);
            this.mCpuUidFreqTimeReader.readDelta(null);
            this.mNumAllUidCpuTimeReads += 2;
            if (this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
                this.mCpuUidActiveTimeReader.readDelta(null);
                this.mCpuUidClusterTimeReader.readDelta(null);
                this.mNumAllUidCpuTimeReads += 2;
            }
            for (int cluster = this.mKernelCpuSpeedReaders.length - 1; cluster >= 0; cluster--) {
                this.mKernelCpuSpeedReaders[cluster].readDelta();
            }
            return;
        }
        this.mUserInfoProvider.refreshUserIds();
        SparseLongArray updatedUids = this.mCpuUidFreqTimeReader.perClusterTimesAvailable() ? null : new SparseLongArray();
        readKernelUidCpuTimesLocked(partialTimersToConsider, updatedUids, onBattery);
        if (updatedUids != null) {
            updateClusterSpeedTimes(updatedUids, onBattery);
        }
        readKernelUidCpuFreqTimesLocked(partialTimersToConsider, onBattery, onBatteryScreenOff);
        this.mNumAllUidCpuTimeReads += 2;
        if (this.mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME) {
            readKernelUidCpuActiveTimesLocked(onBattery);
            readKernelUidCpuClusterTimesLocked(onBattery);
            this.mNumAllUidCpuTimeReads += 2;
        }
    }

    @VisibleForTesting
    public void markPartialTimersAsEligible() {
        if (ArrayUtils.referenceEquals(this.mPartialTimers, this.mLastPartialTimers)) {
            for (int i = this.mPartialTimers.size() - 1; i >= 0; i--) {
                this.mPartialTimers.get(i).mInList = true;
            }
            return;
        }
        for (int i2 = this.mLastPartialTimers.size() - 1; i2 >= 0; i2--) {
            this.mLastPartialTimers.get(i2).mInList = false;
        }
        this.mLastPartialTimers.clear();
        int numPartialTimers = this.mPartialTimers.size();
        for (int i3 = 0; i3 < numPartialTimers; i3++) {
            StopwatchTimer timer = this.mPartialTimers.get(i3);
            timer.mInList = true;
            this.mLastPartialTimers.add(timer);
        }
    }

    @VisibleForTesting
    public void updateClusterSpeedTimes(SparseLongArray updatedUids, boolean onBattery) {
        SparseLongArray sparseLongArray = updatedUids;
        long totalCpuClustersTimeMs = 0;
        long[][] clusterSpeedTimesMs = new long[this.mKernelCpuSpeedReaders.length];
        int cluster = 0;
        while (true) {
            KernelCpuSpeedReader[] kernelCpuSpeedReaderArr = this.mKernelCpuSpeedReaders;
            if (cluster >= kernelCpuSpeedReaderArr.length) {
                break;
            }
            clusterSpeedTimesMs[cluster] = kernelCpuSpeedReaderArr[cluster].readDelta();
            if (clusterSpeedTimesMs[cluster] != null) {
                for (int speed = clusterSpeedTimesMs[cluster].length - 1; speed >= 0; speed--) {
                    totalCpuClustersTimeMs += clusterSpeedTimesMs[cluster][speed];
                }
            }
            cluster++;
        }
        if (totalCpuClustersTimeMs != 0) {
            int updatedUidsCount = updatedUids.size();
            int i = 0;
            while (i < updatedUidsCount) {
                Uid u = getUidStatsLocked(sparseLongArray.keyAt(i));
                long appCpuTimeUs = sparseLongArray.valueAt(i);
                int numClusters = this.mPowerProfile.getNumCpuClusters();
                if (u.mCpuClusterSpeedTimesUs == null || u.mCpuClusterSpeedTimesUs.length != numClusters) {
                    u.mCpuClusterSpeedTimesUs = new LongSamplingCounter[numClusters];
                }
                for (int cluster2 = 0; cluster2 < clusterSpeedTimesMs.length; cluster2++) {
                    int speedsInCluster = clusterSpeedTimesMs[cluster2].length;
                    if (u.mCpuClusterSpeedTimesUs[cluster2] == null || speedsInCluster != u.mCpuClusterSpeedTimesUs[cluster2].length) {
                        u.mCpuClusterSpeedTimesUs[cluster2] = new LongSamplingCounter[speedsInCluster];
                    }
                    LongSamplingCounter[] cpuSpeeds = u.mCpuClusterSpeedTimesUs[cluster2];
                    int speed2 = 0;
                    while (speed2 < speedsInCluster) {
                        if (cpuSpeeds[speed2] == null) {
                            cpuSpeeds[speed2] = new LongSamplingCounter(this.mOnBatteryTimeBase);
                        }
                        cpuSpeeds[speed2].addCountLocked((clusterSpeedTimesMs[cluster2][speed2] * appCpuTimeUs) / totalCpuClustersTimeMs, onBattery);
                        speed2++;
                        clusterSpeedTimesMs = clusterSpeedTimesMs;
                        updatedUidsCount = updatedUidsCount;
                    }
                }
                i++;
                sparseLongArray = updatedUids;
            }
        }
    }

    @VisibleForTesting
    public void readKernelUidCpuTimesLocked(ArrayList<StopwatchTimer> partialTimers, SparseLongArray updatedUids, final boolean onBattery) {
        ArrayList<StopwatchTimer> arrayList = partialTimers;
        final SparseLongArray sparseLongArray = updatedUids;
        this.mTempTotalCpuSystemTimeUs = 0L;
        this.mTempTotalCpuUserTimeUs = 0L;
        final int numWakelocks = arrayList == null ? 0 : partialTimers.size();
        long startTimeMs = this.mClocks.uptimeMillis();
        this.mCpuUidUserSysTimeReader.readDelta(new KernelCpuUidTimeReader.Callback() { // from class: com.android.internal.os.-$$Lambda$BatteryStatsImpl$7bfIWpn8X2h-hSzLD6dcuK4Ljuw
            @Override // com.android.internal.os.KernelCpuUidTimeReader.Callback
            public final void onUidCpuTime(int i, Object obj) {
                BatteryStatsImpl.this.lambda$readKernelUidCpuTimesLocked$0$BatteryStatsImpl(numWakelocks, onBattery, sparseLongArray, i, (long[]) obj);
            }
        });
        long elapsedTimeMs = this.mClocks.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs >= 100) {
            Slog.d(TAG, "Reading cpu stats took " + elapsedTimeMs + "ms");
        }
        if (numWakelocks > 0) {
            this.mTempTotalCpuUserTimeUs = (this.mTempTotalCpuUserTimeUs * 50) / 100;
            this.mTempTotalCpuSystemTimeUs = (this.mTempTotalCpuSystemTimeUs * 50) / 100;
            int i = 0;
            while (i < numWakelocks) {
                StopwatchTimer timer = arrayList.get(i);
                int userTimeUs = (int) (this.mTempTotalCpuUserTimeUs / (numWakelocks - i));
                int numWakelocks2 = numWakelocks;
                int systemTimeUs = (int) (this.mTempTotalCpuSystemTimeUs / (numWakelocks - i));
                timer.mUid.mUserCpuTime.addCountLocked(userTimeUs, onBattery);
                timer.mUid.mSystemCpuTime.addCountLocked(systemTimeUs, onBattery);
                if (sparseLongArray != null) {
                    int uid = timer.mUid.getUid();
                    sparseLongArray.put(uid, sparseLongArray.get(uid, 0L) + userTimeUs + systemTimeUs);
                }
                Uid.Proc proc = timer.mUid.getProcessStatsLocked("*wakelock*");
                proc.addCpuTimeLocked(userTimeUs / 1000, systemTimeUs / 1000, onBattery);
                this.mTempTotalCpuUserTimeUs -= userTimeUs;
                this.mTempTotalCpuSystemTimeUs -= systemTimeUs;
                i++;
                arrayList = partialTimers;
                sparseLongArray = updatedUids;
                numWakelocks = numWakelocks2;
            }
        }
    }

    public /* synthetic */ void lambda$readKernelUidCpuTimesLocked$0$BatteryStatsImpl(int numWakelocks, boolean onBattery, SparseLongArray updatedUids, int uid, long[] timesUs) {
        long userTimeUs = timesUs[0];
        long systemTimeUs = timesUs[1];
        int uid2 = mapUid(uid);
        if (Process.isIsolated(uid2)) {
            this.mCpuUidUserSysTimeReader.removeUid(uid2);
            Slog.d(TAG, "Got readings for an isolated uid with no mapping: " + uid2);
        } else if (!this.mUserInfoProvider.exists(UserHandle.getUserId(uid2))) {
            Slog.d(TAG, "Got readings for an invalid user's uid " + uid2);
            this.mCpuUidUserSysTimeReader.removeUid(uid2);
        } else {
            Uid u = getUidStatsLocked(uid2);
            this.mTempTotalCpuUserTimeUs += userTimeUs;
            this.mTempTotalCpuSystemTimeUs += systemTimeUs;
            StringBuilder sb = null;
            if (numWakelocks > 0) {
                userTimeUs = (userTimeUs * 50) / 100;
                systemTimeUs = (50 * systemTimeUs) / 100;
            }
            if (0 != 0) {
                sb.append("  adding to uid=");
                sb.append(u.mUid);
                sb.append(": u=");
                TimeUtils.formatDuration(userTimeUs / 1000, (StringBuilder) null);
                sb.append(" s=");
                TimeUtils.formatDuration(systemTimeUs / 1000, (StringBuilder) null);
                Slog.d(TAG, sb.toString());
            }
            u.mUserCpuTime.addCountLocked(userTimeUs, onBattery);
            u.mSystemCpuTime.addCountLocked(systemTimeUs, onBattery);
            if (updatedUids != null) {
                updatedUids.put(u.getUid(), userTimeUs + systemTimeUs);
            }
        }
    }

    @VisibleForTesting
    public void readKernelUidCpuFreqTimesLocked(ArrayList<StopwatchTimer> partialTimers, final boolean onBattery, final boolean onBatteryScreenOff) {
        long elapsedTimeMs;
        ArrayList<StopwatchTimer> arrayList = partialTimers;
        final boolean perClusterTimesAvailable = this.mCpuUidFreqTimeReader.perClusterTimesAvailable();
        final int numWakelocks = arrayList == null ? 0 : partialTimers.size();
        final int numClusters = this.mPowerProfile.getNumCpuClusters();
        this.mWakeLockAllocationsUs = null;
        long startTimeMs = this.mClocks.uptimeMillis();
        this.mCpuUidFreqTimeReader.readDelta(new KernelCpuUidTimeReader.Callback() { // from class: com.android.internal.os.-$$Lambda$BatteryStatsImpl$B-TmZhQb712ePnuJTxvMe7P-YwQ
            @Override // com.android.internal.os.KernelCpuUidTimeReader.Callback
            public final void onUidCpuTime(int i, Object obj) {
                BatteryStatsImpl.this.lambda$readKernelUidCpuFreqTimesLocked$1$BatteryStatsImpl(onBattery, onBatteryScreenOff, perClusterTimesAvailable, numClusters, numWakelocks, i, (long[]) obj);
            }
        });
        long elapsedTimeMs2 = this.mClocks.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs2 >= 100) {
            Slog.d(TAG, "Reading cpu freq times took " + elapsedTimeMs2 + "ms");
        }
        if (this.mWakeLockAllocationsUs != null) {
            int i = 0;
            while (i < numWakelocks) {
                Uid u = arrayList.get(i).mUid;
                if (u.mCpuClusterSpeedTimesUs == null || u.mCpuClusterSpeedTimesUs.length != numClusters) {
                    detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                    u.mCpuClusterSpeedTimesUs = new LongSamplingCounter[numClusters];
                }
                int cluster = 0;
                while (cluster < numClusters) {
                    int speedsInCluster = this.mPowerProfile.getNumSpeedStepsInCpuCluster(cluster);
                    if (u.mCpuClusterSpeedTimesUs[cluster] == null || u.mCpuClusterSpeedTimesUs[cluster].length != speedsInCluster) {
                        detachIfNotNull(u.mCpuClusterSpeedTimesUs[cluster]);
                        u.mCpuClusterSpeedTimesUs[cluster] = new LongSamplingCounter[speedsInCluster];
                    }
                    LongSamplingCounter[] cpuTimeUs = u.mCpuClusterSpeedTimesUs[cluster];
                    int speed = 0;
                    while (speed < speedsInCluster) {
                        if (cpuTimeUs[speed] != null) {
                            elapsedTimeMs = elapsedTimeMs2;
                        } else {
                            elapsedTimeMs = elapsedTimeMs2;
                            cpuTimeUs[speed] = new LongSamplingCounter(this.mOnBatteryTimeBase);
                        }
                        long allocationUs = this.mWakeLockAllocationsUs[cluster][speed] / (numWakelocks - i);
                        cpuTimeUs[speed].addCountLocked(allocationUs, onBattery);
                        long[] jArr = this.mWakeLockAllocationsUs[cluster];
                        jArr[speed] = jArr[speed] - allocationUs;
                        speed++;
                        elapsedTimeMs2 = elapsedTimeMs;
                        perClusterTimesAvailable = perClusterTimesAvailable;
                    }
                    cluster++;
                    perClusterTimesAvailable = perClusterTimesAvailable;
                }
                i++;
                arrayList = partialTimers;
                perClusterTimesAvailable = perClusterTimesAvailable;
            }
        }
    }

    public /* synthetic */ void lambda$readKernelUidCpuFreqTimesLocked$1$BatteryStatsImpl(boolean onBattery, boolean onBatteryScreenOff, boolean perClusterTimesAvailable, int numClusters, int numWakelocks, int uid, long[] cpuFreqTimeMs) {
        long appAllocationUs;
        int uid2 = mapUid(uid);
        if (Process.isIsolated(uid2)) {
            this.mCpuUidFreqTimeReader.removeUid(uid2);
            Slog.d(TAG, "Got freq readings for an isolated uid with no mapping: " + uid2);
        } else if (!this.mUserInfoProvider.exists(UserHandle.getUserId(uid2))) {
            Slog.d(TAG, "Got freq readings for an invalid user's uid " + uid2);
            this.mCpuUidFreqTimeReader.removeUid(uid2);
        } else {
            Uid u = getUidStatsLocked(uid2);
            if (u.mCpuFreqTimeMs == null || u.mCpuFreqTimeMs.getSize() != cpuFreqTimeMs.length) {
                detachIfNotNull(u.mCpuFreqTimeMs);
                u.mCpuFreqTimeMs = new LongSamplingCounterArray(this.mOnBatteryTimeBase);
            }
            u.mCpuFreqTimeMs.addCountLocked(cpuFreqTimeMs, onBattery);
            if (u.mScreenOffCpuFreqTimeMs == null || u.mScreenOffCpuFreqTimeMs.getSize() != cpuFreqTimeMs.length) {
                detachIfNotNull(u.mScreenOffCpuFreqTimeMs);
                u.mScreenOffCpuFreqTimeMs = new LongSamplingCounterArray(this.mOnBatteryScreenOffTimeBase);
            }
            u.mScreenOffCpuFreqTimeMs.addCountLocked(cpuFreqTimeMs, onBatteryScreenOff);
            if (perClusterTimesAvailable) {
                if (u.mCpuClusterSpeedTimesUs == null || u.mCpuClusterSpeedTimesUs.length != numClusters) {
                    detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                    u.mCpuClusterSpeedTimesUs = new LongSamplingCounter[numClusters];
                }
                if (numWakelocks > 0 && this.mWakeLockAllocationsUs == null) {
                    this.mWakeLockAllocationsUs = new long[numClusters];
                }
                int freqIndex = 0;
                for (int cluster = 0; cluster < numClusters; cluster++) {
                    int speedsInCluster = this.mPowerProfile.getNumSpeedStepsInCpuCluster(cluster);
                    if (u.mCpuClusterSpeedTimesUs[cluster] == null || u.mCpuClusterSpeedTimesUs[cluster].length != speedsInCluster) {
                        detachIfNotNull(u.mCpuClusterSpeedTimesUs[cluster]);
                        u.mCpuClusterSpeedTimesUs[cluster] = new LongSamplingCounter[speedsInCluster];
                    }
                    if (numWakelocks > 0) {
                        long[][] jArr = this.mWakeLockAllocationsUs;
                        if (jArr[cluster] == null) {
                            jArr[cluster] = new long[speedsInCluster];
                        }
                    }
                    LongSamplingCounter[] cpuTimesUs = u.mCpuClusterSpeedTimesUs[cluster];
                    for (int speed = 0; speed < speedsInCluster; speed++) {
                        if (cpuTimesUs[speed] == null) {
                            cpuTimesUs[speed] = new LongSamplingCounter(this.mOnBatteryTimeBase);
                        }
                        long[][] jArr2 = this.mWakeLockAllocationsUs;
                        if (jArr2 != null) {
                            appAllocationUs = ((cpuFreqTimeMs[freqIndex] * 1000) * 50) / 100;
                            long[] jArr3 = jArr2[cluster];
                            jArr3[speed] = jArr3[speed] + ((cpuFreqTimeMs[freqIndex] * 1000) - appAllocationUs);
                        } else {
                            long appAllocationUs2 = cpuFreqTimeMs[freqIndex];
                            appAllocationUs = appAllocationUs2 * 1000;
                        }
                        cpuTimesUs[speed].addCountLocked(appAllocationUs, onBattery);
                        freqIndex++;
                    }
                }
            }
        }
    }

    @VisibleForTesting
    public void readKernelUidCpuActiveTimesLocked(final boolean onBattery) {
        long startTimeMs = this.mClocks.uptimeMillis();
        this.mCpuUidActiveTimeReader.readDelta(new KernelCpuUidTimeReader.Callback() { // from class: com.android.internal.os.-$$Lambda$BatteryStatsImpl$_l2oiaRDRhjCXI_PwXPsAhrgegI
            @Override // com.android.internal.os.KernelCpuUidTimeReader.Callback
            public final void onUidCpuTime(int i, Object obj) {
                BatteryStatsImpl.this.lambda$readKernelUidCpuActiveTimesLocked$2$BatteryStatsImpl(onBattery, i, (Long) obj);
            }
        });
        long elapsedTimeMs = this.mClocks.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs >= 100) {
            Slog.d(TAG, "Reading cpu active times took " + elapsedTimeMs + "ms");
        }
    }

    public /* synthetic */ void lambda$readKernelUidCpuActiveTimesLocked$2$BatteryStatsImpl(boolean onBattery, int uid, Long cpuActiveTimesMs) {
        int uid2 = mapUid(uid);
        if (Process.isIsolated(uid2)) {
            this.mCpuUidActiveTimeReader.removeUid(uid2);
            Slog.w(TAG, "Got active times for an isolated uid with no mapping: " + uid2);
        } else if (!this.mUserInfoProvider.exists(UserHandle.getUserId(uid2))) {
            Slog.w(TAG, "Got active times for an invalid user's uid " + uid2);
            this.mCpuUidActiveTimeReader.removeUid(uid2);
        } else {
            Uid u = getUidStatsLocked(uid2);
            u.mCpuActiveTimeMs.addCountLocked(cpuActiveTimesMs.longValue(), onBattery);
        }
    }

    @VisibleForTesting
    public void readKernelUidCpuClusterTimesLocked(final boolean onBattery) {
        long startTimeMs = this.mClocks.uptimeMillis();
        this.mCpuUidClusterTimeReader.readDelta(new KernelCpuUidTimeReader.Callback() { // from class: com.android.internal.os.-$$Lambda$BatteryStatsImpl$Xvt9xdVPtevMWGIjcbxXf0_mr_c
            @Override // com.android.internal.os.KernelCpuUidTimeReader.Callback
            public final void onUidCpuTime(int i, Object obj) {
                BatteryStatsImpl.this.lambda$readKernelUidCpuClusterTimesLocked$3$BatteryStatsImpl(onBattery, i, (long[]) obj);
            }
        });
        long elapsedTimeMs = this.mClocks.uptimeMillis() - startTimeMs;
        if (elapsedTimeMs >= 100) {
            Slog.d(TAG, "Reading cpu cluster times took " + elapsedTimeMs + "ms");
        }
    }

    public /* synthetic */ void lambda$readKernelUidCpuClusterTimesLocked$3$BatteryStatsImpl(boolean onBattery, int uid, long[] cpuClusterTimesMs) {
        int uid2 = mapUid(uid);
        if (Process.isIsolated(uid2)) {
            this.mCpuUidClusterTimeReader.removeUid(uid2);
            Slog.w(TAG, "Got cluster times for an isolated uid with no mapping: " + uid2);
        } else if (!this.mUserInfoProvider.exists(UserHandle.getUserId(uid2))) {
            Slog.w(TAG, "Got cluster times for an invalid user's uid " + uid2);
            this.mCpuUidClusterTimeReader.removeUid(uid2);
        } else {
            Uid u = getUidStatsLocked(uid2);
            u.mCpuClusterTimesMs.addCountLocked(cpuClusterTimesMs, onBattery);
        }
    }

    boolean setChargingLocked(boolean charging) {
        this.mHandler.removeCallbacks(this.mDeferSetCharging);
        if (this.mCharging != charging) {
            this.mCharging = charging;
            if (charging) {
                this.mHistoryCur.states2 |= 16777216;
            } else {
                this.mHistoryCur.states2 &= -16777217;
            }
            this.mHandler.sendEmptyMessage(3);
            return true;
        }
        return false;
    }

    @GuardedBy({"this"})
    protected void setOnBatteryLocked(long mSecRealtime, long mSecUptime, boolean onBattery, int oldStatus, int level, int chargeUAh) {
        boolean reset;
        int i;
        boolean doWrite = false;
        Message m = this.mHandler.obtainMessage(2);
        m.arg1 = onBattery ? 1 : 0;
        this.mHandler.sendMessage(m);
        long uptime = mSecUptime * 1000;
        long realtime = mSecRealtime * 1000;
        int screenState = this.mScreenState;
        if (!onBattery) {
            this.mLastChargingStateLevel = level;
            this.mOnBatteryInternal = false;
            this.mOnBattery = false;
            pullPendingStateUpdatesLocked();
            BatteryStats.HistoryItem historyItem = this.mHistoryCur;
            historyItem.batteryLevel = (byte) level;
            historyItem.states |= 524288;
            addHistoryRecordLocked(mSecRealtime, mSecUptime);
            this.mDischargePlugLevel = level;
            this.mDischargeCurrentLevel = level;
            int i2 = this.mDischargeUnplugLevel;
            if (level < i2) {
                this.mLowDischargeAmountSinceCharge += (i2 - level) - 1;
                this.mHighDischargeAmountSinceCharge += i2 - level;
            }
            updateDischargeScreenLevelsLocked(screenState, screenState);
            updateTimeBasesLocked(false, screenState, uptime, realtime);
            this.mChargeStepTracker.init();
            this.mLastChargeStepLevel = level;
            this.mMaxChargeStepLevel = level;
            this.mInitStepMode = this.mCurStepMode;
            this.mModStepMode = 0;
        } else {
            if (!this.mNoAutoReset) {
                if (oldStatus != 5 && level < 90 && (this.mDischargeCurrentLevel >= 20 || level < 80)) {
                    reset = false;
                } else {
                    Slog.i(TAG, "Resetting battery stats: level=" + level + " status=" + oldStatus + " dischargeLevel=" + this.mDischargeCurrentLevel + " lowAmount=" + getLowDischargeAmountSinceCharge() + " highAmount=" + getHighDischargeAmountSinceCharge());
                    if (getLowDischargeAmountSinceCharge() >= 20) {
                        long startTime = SystemClock.uptimeMillis();
                        final Parcel parcel = Parcel.obtain();
                        writeSummaryToParcel(parcel, true);
                        final long initialTime = SystemClock.uptimeMillis() - startTime;
                        BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.os.BatteryStatsImpl.4
                            @Override // java.lang.Runnable
                            public void run() {
                                Parcel parcel2;
                                synchronized (BatteryStatsImpl.this.mCheckinFile) {
                                    long startTime2 = SystemClock.uptimeMillis();
                                    FileOutputStream stream = null;
                                    try {
                                        stream = BatteryStatsImpl.this.mCheckinFile.startWrite();
                                        stream.write(parcel.marshall());
                                        stream.flush();
                                        BatteryStatsImpl.this.mCheckinFile.finishWrite(stream);
                                        EventLogTags.writeCommitSysConfigFile("batterystats-checkin", (initialTime + SystemClock.uptimeMillis()) - startTime2);
                                        parcel2 = parcel;
                                    } catch (IOException e) {
                                        Slog.w("BatteryStats", "Error writing checkin battery statistics", e);
                                        BatteryStatsImpl.this.mCheckinFile.failWrite(stream);
                                        parcel2 = parcel;
                                    }
                                    parcel2.recycle();
                                }
                            }
                        });
                    }
                    resetAllStatsLocked();
                    if (chargeUAh > 0 && level > 0) {
                        this.mEstimatedBatteryCapacity = (int) ((chargeUAh / 1000) / (level / 100.0d));
                    }
                    this.mDischargeStartLevel = level;
                    this.mDischargeStepTracker.init();
                    doWrite = true;
                    reset = true;
                }
            } else {
                reset = false;
            }
            if (this.mCharging) {
                setChargingLocked(false);
            }
            this.mLastChargingStateLevel = level;
            this.mOnBatteryInternal = true;
            this.mOnBattery = true;
            this.mLastDischargeStepLevel = level;
            this.mMinDischargeStepLevel = level;
            this.mDischargeStepTracker.clearTime();
            this.mDailyDischargeStepTracker.clearTime();
            this.mInitStepMode = this.mCurStepMode;
            this.mModStepMode = 0;
            pullPendingStateUpdatesLocked();
            BatteryStats.HistoryItem historyItem2 = this.mHistoryCur;
            historyItem2.batteryLevel = (byte) level;
            historyItem2.states &= -524289;
            if (reset) {
                this.mRecordingHistory = true;
                i = 0;
                startRecordingHistory(mSecRealtime, mSecUptime, reset);
            } else {
                i = 0;
            }
            addHistoryRecordLocked(mSecRealtime, mSecUptime);
            this.mDischargeUnplugLevel = level;
            this.mDischargeCurrentLevel = level;
            if (isScreenOn(screenState)) {
                this.mDischargeScreenOnUnplugLevel = level;
                this.mDischargeScreenDozeUnplugLevel = i;
                this.mDischargeScreenOffUnplugLevel = i;
            } else if (isScreenDoze(screenState)) {
                this.mDischargeScreenOnUnplugLevel = i;
                this.mDischargeScreenDozeUnplugLevel = level;
                this.mDischargeScreenOffUnplugLevel = i;
            } else {
                this.mDischargeScreenOnUnplugLevel = i;
                this.mDischargeScreenDozeUnplugLevel = i;
                this.mDischargeScreenOffUnplugLevel = level;
            }
            this.mDischargeAmountScreenOn = i;
            this.mDischargeAmountScreenDoze = i;
            this.mDischargeAmountScreenOff = i;
            updateTimeBasesLocked(true, screenState, uptime, realtime);
        }
        if ((doWrite || this.mLastWriteTime + 60000 < mSecRealtime) && this.mStatsFile != null && this.mBatteryStatsHistory.getActiveFile() != null) {
            writeAsyncLocked();
        }
    }

    private void startRecordingHistory(long elapsedRealtimeMs, long uptimeMs, boolean reset) {
        this.mRecordingHistory = true;
        this.mHistoryCur.currentTime = System.currentTimeMillis();
        addHistoryBufferLocked(elapsedRealtimeMs, reset ? (byte) 7 : (byte) 5, this.mHistoryCur);
        this.mHistoryCur.currentTime = 0L;
        if (reset) {
            initActiveHistoryEventsLocked(elapsedRealtimeMs, uptimeMs);
        }
    }

    private void recordCurrentTimeChangeLocked(long currentTime, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mRecordingHistory) {
            BatteryStats.HistoryItem historyItem = this.mHistoryCur;
            historyItem.currentTime = currentTime;
            addHistoryBufferLocked(elapsedRealtimeMs, (byte) 5, historyItem);
            this.mHistoryCur.currentTime = 0L;
        }
    }

    private void recordShutdownLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (this.mRecordingHistory) {
            this.mHistoryCur.currentTime = System.currentTimeMillis();
            addHistoryBufferLocked(elapsedRealtimeMs, (byte) 8, this.mHistoryCur);
            this.mHistoryCur.currentTime = 0L;
        }
    }

    private void scheduleSyncExternalStatsLocked(String reason, int updateFlags) {
        ExternalStatsSync externalStatsSync = this.mExternalSync;
        if (externalStatsSync != null) {
            externalStatsSync.scheduleSync(reason, updateFlags);
        }
    }

    @GuardedBy({"this"})
    public void setBatteryStateLocked(int status, int health, int plugType, int level, int temp, int volt, int chargeUAh, int chargeFullUAh) {
        long elapsedRealtime;
        long uptime;
        boolean onBattery;
        boolean onBattery2;
        boolean z;
        int i;
        int temp2 = Math.max(0, temp);
        reportChangesToStatsLog(this.mHaveBatteryLevel ? this.mHistoryCur : null, status, plugType, level);
        boolean onBattery3 = isOnBattery(plugType, status);
        long uptime2 = this.mClocks.uptimeMillis();
        long elapsedRealtime2 = this.mClocks.elapsedRealtime();
        if (!this.mHaveBatteryLevel) {
            this.mHaveBatteryLevel = true;
            if (onBattery3 == this.mOnBattery) {
                if (onBattery3) {
                    this.mHistoryCur.states &= -524289;
                } else {
                    this.mHistoryCur.states |= 524288;
                }
            }
            this.mHistoryCur.states2 |= 16777216;
            BatteryStats.HistoryItem historyItem = this.mHistoryCur;
            historyItem.batteryStatus = (byte) status;
            historyItem.batteryLevel = (byte) level;
            historyItem.batteryChargeUAh = chargeUAh;
            this.mLastDischargeStepLevel = level;
            this.mLastChargeStepLevel = level;
            this.mMinDischargeStepLevel = level;
            this.mMaxChargeStepLevel = level;
            this.mLastChargingStateLevel = level;
        } else if (this.mCurrentBatteryLevel != level || this.mOnBattery != onBattery3) {
            recordDailyStatsIfNeededLocked(level >= 100 && onBattery3);
        }
        int oldStatus = this.mHistoryCur.batteryStatus;
        if (onBattery3) {
            this.mDischargeCurrentLevel = level;
            if (this.mRecordingHistory) {
                elapsedRealtime = elapsedRealtime2;
                uptime = uptime2;
                onBattery = onBattery3;
            } else {
                this.mRecordingHistory = true;
                elapsedRealtime = elapsedRealtime2;
                uptime = uptime2;
                onBattery = onBattery3;
                startRecordingHistory(elapsedRealtime2, uptime2, true);
            }
        } else {
            elapsedRealtime = elapsedRealtime2;
            uptime = uptime2;
            onBattery = onBattery3;
            if (level < 96 && status != 1 && !this.mRecordingHistory) {
                this.mRecordingHistory = true;
                startRecordingHistory(elapsedRealtime, uptime, true);
            }
        }
        this.mCurrentBatteryLevel = level;
        if (this.mDischargePlugLevel < 0) {
            this.mDischargePlugLevel = level;
        }
        boolean onBattery4 = onBattery;
        if (onBattery4 != this.mOnBattery) {
            BatteryStats.HistoryItem historyItem2 = this.mHistoryCur;
            historyItem2.batteryLevel = (byte) level;
            historyItem2.batteryStatus = (byte) status;
            historyItem2.batteryHealth = (byte) health;
            historyItem2.batteryPlugType = (byte) plugType;
            historyItem2.batteryTemperature = (short) temp2;
            historyItem2.batteryVoltage = (char) volt;
            if (chargeUAh < historyItem2.batteryChargeUAh) {
                long chargeDiff = this.mHistoryCur.batteryChargeUAh - chargeUAh;
                this.mDischargeCounter.addCountLocked(chargeDiff);
                this.mDischargeScreenOffCounter.addCountLocked(chargeDiff);
                if (isScreenDoze(this.mScreenState)) {
                    this.mDischargeScreenDozeCounter.addCountLocked(chargeDiff);
                }
                int i2 = this.mDeviceIdleMode;
                if (i2 == 1) {
                    this.mDischargeLightDozeCounter.addCountLocked(chargeDiff);
                } else if (i2 == 2) {
                    this.mDischargeDeepDozeCounter.addCountLocked(chargeDiff);
                }
            }
            this.mHistoryCur.batteryChargeUAh = chargeUAh;
            onBattery2 = onBattery4;
            setOnBatteryLocked(elapsedRealtime, uptime, onBattery4, oldStatus, level, chargeUAh);
            z = true;
        } else {
            onBattery2 = onBattery4;
            boolean changed = false;
            if (this.mHistoryCur.batteryLevel != level) {
                this.mHistoryCur.batteryLevel = (byte) level;
                changed = true;
                this.mExternalSync.scheduleSyncDueToBatteryLevelChange(this.mConstants.BATTERY_LEVEL_COLLECTION_DELAY_MS);
            }
            if (this.mHistoryCur.batteryStatus != status) {
                this.mHistoryCur.batteryStatus = (byte) status;
                changed = true;
            }
            if (this.mHistoryCur.batteryHealth != health) {
                this.mHistoryCur.batteryHealth = (byte) health;
                changed = true;
            }
            if (this.mHistoryCur.batteryPlugType != plugType) {
                this.mHistoryCur.batteryPlugType = (byte) plugType;
                changed = true;
            }
            if (temp2 >= this.mHistoryCur.batteryTemperature + 10 || temp2 <= this.mHistoryCur.batteryTemperature - 10) {
                this.mHistoryCur.batteryTemperature = (short) temp2;
                changed = true;
            }
            if (volt > this.mHistoryCur.batteryVoltage + 20 || volt < this.mHistoryCur.batteryVoltage - 20) {
                this.mHistoryCur.batteryVoltage = (char) volt;
                changed = true;
            }
            if (chargeUAh >= this.mHistoryCur.batteryChargeUAh + 10 || chargeUAh <= this.mHistoryCur.batteryChargeUAh - 10) {
                if (chargeUAh >= this.mHistoryCur.batteryChargeUAh) {
                    z = true;
                } else {
                    long chargeDiff2 = this.mHistoryCur.batteryChargeUAh - chargeUAh;
                    this.mDischargeCounter.addCountLocked(chargeDiff2);
                    this.mDischargeScreenOffCounter.addCountLocked(chargeDiff2);
                    if (isScreenDoze(this.mScreenState)) {
                        this.mDischargeScreenDozeCounter.addCountLocked(chargeDiff2);
                    }
                    int i3 = this.mDeviceIdleMode;
                    z = true;
                    if (i3 == 1) {
                        this.mDischargeLightDozeCounter.addCountLocked(chargeDiff2);
                    } else if (i3 == 2) {
                        this.mDischargeDeepDozeCounter.addCountLocked(chargeDiff2);
                    }
                }
                this.mHistoryCur.batteryChargeUAh = chargeUAh;
                changed = true;
            } else {
                z = true;
            }
            long modeBits = (this.mInitStepMode << 48) | (this.mModStepMode << 56) | ((level & 255) << 40);
            if (onBattery2) {
                changed |= setChargingLocked(false);
                int i4 = this.mLastDischargeStepLevel;
                if (i4 != level && this.mMinDischargeStepLevel > level) {
                    long j = elapsedRealtime;
                    this.mDischargeStepTracker.addLevelSteps(i4 - level, modeBits, j);
                    this.mDailyDischargeStepTracker.addLevelSteps(this.mLastDischargeStepLevel - level, modeBits, j);
                    this.mLastDischargeStepLevel = level;
                    this.mMinDischargeStepLevel = level;
                    this.mInitStepMode = this.mCurStepMode;
                    this.mModStepMode = 0;
                }
            } else {
                if (level >= 90) {
                    changed |= setChargingLocked(z);
                } else if (this.mCharging) {
                    if (this.mLastChargeStepLevel > level) {
                        changed |= setChargingLocked(false);
                    }
                } else {
                    int i5 = this.mLastChargeStepLevel;
                    if (i5 < level) {
                        if (!this.mHandler.hasCallbacks(this.mDeferSetCharging)) {
                            this.mHandler.postDelayed(this.mDeferSetCharging, this.mConstants.BATTERY_CHARGED_DELAY_MS);
                        }
                    } else if (i5 > level) {
                        this.mHandler.removeCallbacks(this.mDeferSetCharging);
                    }
                }
                int i6 = this.mLastChargeStepLevel;
                if (i6 != level && this.mMaxChargeStepLevel < level) {
                    long j2 = elapsedRealtime;
                    this.mChargeStepTracker.addLevelSteps(level - i6, modeBits, j2);
                    this.mDailyChargeStepTracker.addLevelSteps(level - this.mLastChargeStepLevel, modeBits, j2);
                    this.mMaxChargeStepLevel = level;
                    this.mInitStepMode = this.mCurStepMode;
                    this.mModStepMode = 0;
                }
                this.mLastChargeStepLevel = level;
            }
            if (changed) {
                addHistoryRecordLocked(elapsedRealtime, uptime);
            }
        }
        if (!onBattery2 && (status == 5 || status == z)) {
            this.mRecordingHistory = false;
        }
        int i7 = this.mMinLearnedBatteryCapacity;
        if (i7 == -1) {
            i = chargeFullUAh;
            this.mMinLearnedBatteryCapacity = i;
        } else {
            i = chargeFullUAh;
            this.mMinLearnedBatteryCapacity = Math.min(i7, i);
        }
        this.mMaxLearnedBatteryCapacity = Math.max(this.mMaxLearnedBatteryCapacity, i);
    }

    public static boolean isOnBattery(int plugType, int status) {
        return plugType == 0 && status != 1;
    }

    private void reportChangesToStatsLog(BatteryStats.HistoryItem recentPast, int status, int plugType, int level) {
        if (recentPast == null || recentPast.batteryStatus != status) {
            StatsLog.write(31, status);
        }
        if (recentPast == null || recentPast.batteryPlugType != plugType) {
            StatsLog.write(32, plugType);
        }
        if (recentPast == null || recentPast.batteryLevel != level) {
            StatsLog.write(30, level);
        }
    }

    @UnsupportedAppUsage
    public long getAwakeTimeBattery() {
        return getBatteryUptimeLocked();
    }

    @UnsupportedAppUsage
    public long getAwakeTimePlugged() {
        return (this.mClocks.uptimeMillis() * 1000) - getAwakeTimeBattery();
    }

    @Override // android.os.BatteryStats
    public long computeUptime(long curTime, int which) {
        return this.mUptime + (curTime - this.mUptimeStart);
    }

    @Override // android.os.BatteryStats
    public long computeRealtime(long curTime, int which) {
        return this.mRealtime + (curTime - this.mRealtimeStart);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long computeBatteryUptime(long curTime, int which) {
        return this.mOnBatteryTimeBase.computeUptime(curTime, which);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long computeBatteryRealtime(long curTime, int which) {
        return this.mOnBatteryTimeBase.computeRealtime(curTime, which);
    }

    @Override // android.os.BatteryStats
    public long computeBatteryScreenOffUptime(long curTime, int which) {
        return this.mOnBatteryScreenOffTimeBase.computeUptime(curTime, which);
    }

    @Override // android.os.BatteryStats
    public long computeBatteryScreenOffRealtime(long curTime, int which) {
        return this.mOnBatteryScreenOffTimeBase.computeRealtime(curTime, which);
    }

    private long computeTimePerLevel(long[] steps, int numSteps) {
        if (numSteps <= 0) {
            return -1L;
        }
        long total = 0;
        for (int i = 0; i < numSteps; i++) {
            total += steps[i] & BatteryStats.STEP_LEVEL_TIME_MASK;
        }
        return total / numSteps;
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long computeBatteryTimeRemaining(long curTime) {
        if (this.mOnBattery && this.mDischargeStepTracker.mNumStepDurations >= 1) {
            long msPerLevel = this.mDischargeStepTracker.computeTimePerLevel();
            if (msPerLevel <= 0) {
                return -1L;
            }
            return this.mCurrentBatteryLevel * msPerLevel * 1000;
        }
        return -1L;
    }

    @Override // android.os.BatteryStats
    public BatteryStats.LevelStepTracker getDischargeLevelStepTracker() {
        return this.mDischargeStepTracker;
    }

    @Override // android.os.BatteryStats
    public BatteryStats.LevelStepTracker getDailyDischargeLevelStepTracker() {
        return this.mDailyDischargeStepTracker;
    }

    @Override // android.os.BatteryStats
    public long computeChargeTimeRemaining(long curTime) {
        if (!this.mOnBattery && this.mChargeStepTracker.mNumStepDurations >= 1) {
            long msPerLevel = this.mChargeStepTracker.computeTimePerLevel();
            if (msPerLevel <= 0) {
                return -1L;
            }
            return (100 - this.mCurrentBatteryLevel) * msPerLevel * 1000;
        }
        return -1L;
    }

    public CellularBatteryStats getCellularBatteryStats() {
        long monitoredRailChargeConsumedMaMs;
        CellularBatteryStats s = new CellularBatteryStats();
        int which = 0;
        long rawRealTime = SystemClock.elapsedRealtime() * 1000;
        BatteryStats.ControllerActivityCounter counter = getModemControllerActivity();
        long sleepTimeMs = counter.getSleepTimeCounter().getCountLocked(0);
        long idleTimeMs = counter.getIdleTimeCounter().getCountLocked(0);
        long rxTimeMs = counter.getRxTimeCounter().getCountLocked(0);
        long energyConsumedMaMs = counter.getPowerCounter().getCountLocked(0);
        long monitoredRailChargeConsumedMaMs2 = counter.getMonitoredRailChargeConsumedMaMs().getCountLocked(0);
        long[] timeInRatMs = new long[23];
        int i = 0;
        while (true) {
            int which2 = which;
            int which3 = timeInRatMs.length;
            if (i >= which3) {
                break;
            }
            timeInRatMs[i] = getPhoneDataConnectionTime(i, rawRealTime, 0) / 1000;
            i++;
            which = which2;
        }
        long[] timeInRxSignalStrengthLevelMs = new long[5];
        int i2 = 0;
        while (true) {
            monitoredRailChargeConsumedMaMs = monitoredRailChargeConsumedMaMs2;
            if (i2 >= timeInRxSignalStrengthLevelMs.length) {
                break;
            }
            timeInRxSignalStrengthLevelMs[i2] = getPhoneSignalStrengthTime(i2, rawRealTime, 0) / 1000;
            i2++;
            monitoredRailChargeConsumedMaMs2 = monitoredRailChargeConsumedMaMs;
        }
        long[] txTimeMs = new long[Math.min(5, counter.getTxTimeCounters().length)];
        int i3 = 0;
        long totalTxTimeMs = 0;
        while (i3 < txTimeMs.length) {
            txTimeMs[i3] = counter.getTxTimeCounters()[i3].getCountLocked(0);
            totalTxTimeMs += txTimeMs[i3];
            i3++;
            counter = counter;
        }
        s.setLoggingDurationMs(computeBatteryRealtime(rawRealTime, 0) / 1000);
        s.setKernelActiveTimeMs(getMobileRadioActiveTime(rawRealTime, 0) / 1000);
        s.setNumPacketsTx(getNetworkActivityPackets(1, 0));
        s.setNumBytesTx(getNetworkActivityBytes(1, 0));
        s.setNumPacketsRx(getNetworkActivityPackets(0, 0));
        s.setNumBytesRx(getNetworkActivityBytes(0, 0));
        s.setSleepTimeMs(sleepTimeMs);
        s.setIdleTimeMs(idleTimeMs);
        s.setRxTimeMs(rxTimeMs);
        s.setEnergyConsumedMaMs(energyConsumedMaMs);
        s.setTimeInRatMs(timeInRatMs);
        s.setTimeInRxSignalStrengthLevelMs(timeInRxSignalStrengthLevelMs);
        s.setTxTimeMs(txTimeMs);
        s.setMonitoredRailChargeConsumedMaMs(monitoredRailChargeConsumedMaMs);
        return s;
    }

    public WifiBatteryStats getWifiBatteryStats() {
        WifiBatteryStats s = new WifiBatteryStats();
        boolean z = false;
        long rawRealTime = SystemClock.elapsedRealtime() * 1000;
        BatteryStats.ControllerActivityCounter counter = getWifiControllerActivity();
        long idleTimeMs = counter.getIdleTimeCounter().getCountLocked(0);
        long scanTimeMs = counter.getScanTimeCounter().getCountLocked(0);
        long rxTimeMs = counter.getRxTimeCounter().getCountLocked(0);
        long txTimeMs = counter.getTxTimeCounters()[0].getCountLocked(0);
        long scanTimeMs2 = SystemClock.elapsedRealtime() * 1000;
        long totalControllerActivityTimeMs = computeBatteryRealtime(scanTimeMs2, 0) / 1000;
        long sleepTimeMs = totalControllerActivityTimeMs - ((idleTimeMs + rxTimeMs) + txTimeMs);
        long energyConsumedMaMs = counter.getPowerCounter().getCountLocked(0);
        long monitoredRailChargeConsumedMaMs = counter.getMonitoredRailChargeConsumedMaMs().getCountLocked(0);
        int i = 0;
        long numAppScanRequest = 0;
        while (i < this.mUidStats.size()) {
            numAppScanRequest += this.mUidStats.valueAt(i).mWifiScanTimer.getCountLocked(0);
            i++;
            z = z;
            rawRealTime = rawRealTime;
        }
        long rawRealTime2 = rawRealTime;
        long[] timeInStateMs = new long[8];
        int i2 = 0;
        for (int which = 8; i2 < which; which = 8) {
            long[] timeInStateMs2 = timeInStateMs;
            long rawRealTime3 = rawRealTime2;
            timeInStateMs2[i2] = getWifiStateTime(i2, rawRealTime3, 0) / 1000;
            i2++;
            rawRealTime2 = rawRealTime3;
            timeInStateMs = timeInStateMs2;
        }
        long[] timeInStateMs3 = timeInStateMs;
        long rawRealTime4 = rawRealTime2;
        long[] timeInSupplStateMs = new long[13];
        int i3 = 0;
        for (int i4 = 13; i3 < i4; i4 = 13) {
            timeInSupplStateMs[i3] = getWifiSupplStateTime(i3, rawRealTime4, 0) / 1000;
            i3++;
        }
        long[] timeSignalStrengthTimeMs = new long[5];
        for (int i5 = 0; i5 < 5; i5++) {
            timeSignalStrengthTimeMs[i5] = getWifiSignalStrengthTime(i5, rawRealTime4, 0) / 1000;
        }
        s.setLoggingDurationMs(computeBatteryRealtime(rawRealTime4, 0) / 1000);
        s.setKernelActiveTimeMs(getWifiActiveTime(rawRealTime4, 0) / 1000);
        long rawRealTime5 = getNetworkActivityPackets(3, 0);
        s.setNumPacketsTx(rawRealTime5);
        s.setNumBytesTx(getNetworkActivityBytes(3, 0));
        s.setNumPacketsRx(getNetworkActivityPackets(2, 0));
        s.setNumBytesRx(getNetworkActivityBytes(2, 0));
        s.setSleepTimeMs(sleepTimeMs);
        s.setIdleTimeMs(idleTimeMs);
        s.setRxTimeMs(rxTimeMs);
        s.setTxTimeMs(txTimeMs);
        s.setScanTimeMs(scanTimeMs);
        s.setEnergyConsumedMaMs(energyConsumedMaMs);
        s.setNumAppScanRequest(numAppScanRequest);
        s.setTimeInStateMs(timeInStateMs3);
        s.setTimeInSupplicantStateMs(timeInSupplStateMs);
        s.setTimeInRxSignalStrengthLevelMs(timeSignalStrengthTimeMs);
        s.setMonitoredRailChargeConsumedMaMs(monitoredRailChargeConsumedMaMs);
        return s;
    }

    public GpsBatteryStats getGpsBatteryStats() {
        GpsBatteryStats s = new GpsBatteryStats();
        long rawRealTime = SystemClock.elapsedRealtime() * 1000;
        s.setLoggingDurationMs(computeBatteryRealtime(rawRealTime, 0) / 1000);
        s.setEnergyConsumedMaMs(getGpsBatteryDrainMaMs());
        long[] time = new long[2];
        for (int i = 0; i < time.length; i++) {
            time[i] = getGpsSignalQualityTime(i, rawRealTime, 0) / 1000;
        }
        s.setTimeInGpsSignalQualityLevel(time);
        return s;
    }

    @Override // android.os.BatteryStats
    public BatteryStats.LevelStepTracker getChargeLevelStepTracker() {
        return this.mChargeStepTracker;
    }

    @Override // android.os.BatteryStats
    public BatteryStats.LevelStepTracker getDailyChargeLevelStepTracker() {
        return this.mDailyChargeStepTracker;
    }

    @Override // android.os.BatteryStats
    public ArrayList<BatteryStats.PackageChange> getDailyPackageChanges() {
        return this.mDailyPackageChanges;
    }

    protected long getBatteryUptimeLocked() {
        return this.mOnBatteryTimeBase.getUptime(this.mClocks.uptimeMillis() * 1000);
    }

    @Override // android.os.BatteryStats
    public long getBatteryUptime(long curTime) {
        return this.mOnBatteryTimeBase.getUptime(curTime);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public long getBatteryRealtime(long curTime) {
        return this.mOnBatteryTimeBase.getRealtime(curTime);
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public int getDischargeStartLevel() {
        int dischargeStartLevelLocked;
        synchronized (this) {
            dischargeStartLevelLocked = getDischargeStartLevelLocked();
        }
        return dischargeStartLevelLocked;
    }

    public int getDischargeStartLevelLocked() {
        return this.mDischargeUnplugLevel;
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public int getDischargeCurrentLevel() {
        int dischargeCurrentLevelLocked;
        synchronized (this) {
            dischargeCurrentLevelLocked = getDischargeCurrentLevelLocked();
        }
        return dischargeCurrentLevelLocked;
    }

    public int getDischargeCurrentLevelLocked() {
        return this.mDischargeCurrentLevel;
    }

    @Override // android.os.BatteryStats
    public int getLowDischargeAmountSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mLowDischargeAmountSinceCharge;
            if (this.mOnBattery && this.mDischargeCurrentLevel < this.mDischargeUnplugLevel) {
                val += (this.mDischargeUnplugLevel - this.mDischargeCurrentLevel) - 1;
            }
        }
        return val;
    }

    @Override // android.os.BatteryStats
    public int getHighDischargeAmountSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mHighDischargeAmountSinceCharge;
            if (this.mOnBattery && this.mDischargeCurrentLevel < this.mDischargeUnplugLevel) {
                val += this.mDischargeUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public int getDischargeAmount(int which) {
        int dischargeAmount;
        if (which == 0) {
            dischargeAmount = getHighDischargeAmountSinceCharge();
        } else {
            dischargeAmount = getDischargeStartLevel() - getDischargeCurrentLevel();
        }
        if (dischargeAmount < 0) {
            return 0;
        }
        return dischargeAmount;
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public int getDischargeAmountScreenOn() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenOn;
            if (this.mOnBattery && isScreenOn(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOnUnplugLevel) {
                val += this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    @Override // android.os.BatteryStats
    public int getDischargeAmountScreenOnSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenOnSinceCharge;
            if (this.mOnBattery && isScreenOn(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOnUnplugLevel) {
                val += this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    @Override // android.os.BatteryStats
    @UnsupportedAppUsage
    public int getDischargeAmountScreenOff() {
        int dischargeAmountScreenDoze;
        synchronized (this) {
            int val = this.mDischargeAmountScreenOff;
            if (this.mOnBattery && isScreenOff(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOffUnplugLevel) {
                val += this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel;
            }
            dischargeAmountScreenDoze = getDischargeAmountScreenDoze() + val;
        }
        return dischargeAmountScreenDoze;
    }

    @Override // android.os.BatteryStats
    public int getDischargeAmountScreenOffSinceCharge() {
        int dischargeAmountScreenDozeSinceCharge;
        synchronized (this) {
            int val = this.mDischargeAmountScreenOffSinceCharge;
            if (this.mOnBattery && isScreenOff(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenOffUnplugLevel) {
                val += this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel;
            }
            dischargeAmountScreenDozeSinceCharge = getDischargeAmountScreenDozeSinceCharge() + val;
        }
        return dischargeAmountScreenDozeSinceCharge;
    }

    @Override // android.os.BatteryStats
    public int getDischargeAmountScreenDoze() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenDoze;
            if (this.mOnBattery && isScreenDoze(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenDozeUnplugLevel) {
                val += this.mDischargeScreenDozeUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    @Override // android.os.BatteryStats
    public int getDischargeAmountScreenDozeSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenDozeSinceCharge;
            if (this.mOnBattery && isScreenDoze(this.mScreenState) && this.mDischargeCurrentLevel < this.mDischargeScreenDozeUnplugLevel) {
                val += this.mDischargeScreenDozeUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    @UnsupportedAppUsage
    public Uid getUidStatsLocked(int uid) {
        Uid u = this.mUidStats.get(uid);
        if (u == null) {
            Uid u2 = new Uid(this, uid);
            this.mUidStats.put(uid, u2);
            return u2;
        }
        return u;
    }

    public Uid getAvailableUidStatsLocked(int uid) {
        Uid u = this.mUidStats.get(uid);
        return u;
    }

    public void onCleanupUserLocked(int userId) {
        int firstUidForUser = UserHandle.getUid(userId, 0);
        int lastUidForUser = UserHandle.getUid(userId, Process.LAST_ISOLATED_UID);
        this.mPendingRemovedUids.add(new UidToRemove(firstUidForUser, lastUidForUser, this.mClocks.elapsedRealtime()));
    }

    public void onUserRemovedLocked(int userId) {
        int firstUidForUser = UserHandle.getUid(userId, 0);
        int lastUidForUser = UserHandle.getUid(userId, Process.LAST_ISOLATED_UID);
        this.mUidStats.put(firstUidForUser, null);
        this.mUidStats.put(lastUidForUser, null);
        int firstIndex = this.mUidStats.indexOfKey(firstUidForUser);
        int lastIndex = this.mUidStats.indexOfKey(lastUidForUser);
        for (int i = firstIndex; i <= lastIndex; i++) {
            Uid uid = this.mUidStats.valueAt(i);
            if (uid != null) {
                uid.detachFromTimeBase();
            }
        }
        this.mUidStats.removeAtRange(firstIndex, (lastIndex - firstIndex) + 1);
    }

    @UnsupportedAppUsage
    public void removeUidStatsLocked(int uid) {
        Uid u = this.mUidStats.get(uid);
        if (u != null) {
            u.detachFromTimeBase();
        }
        this.mUidStats.remove(uid);
        this.mPendingRemovedUids.add(new UidToRemove(this, uid, this.mClocks.elapsedRealtime()));
    }

    @UnsupportedAppUsage
    public Uid.Proc getProcessStatsLocked(int uid, String name) {
        Uid u = getUidStatsLocked(mapUid(uid));
        return u.getProcessStatsLocked(name);
    }

    @UnsupportedAppUsage
    public Uid.Pkg getPackageStatsLocked(int uid, String pkg) {
        Uid u = getUidStatsLocked(mapUid(uid));
        return u.getPackageStatsLocked(pkg);
    }

    @UnsupportedAppUsage
    public Uid.Pkg.Serv getServiceStatsLocked(int uid, String pkg, String name) {
        Uid u = getUidStatsLocked(mapUid(uid));
        return u.getServiceStatsLocked(pkg, name);
    }

    public void shutdownLocked() {
        recordShutdownLocked(this.mClocks.elapsedRealtime(), this.mClocks.uptimeMillis());
        writeSyncLocked();
        this.mShuttingDown = true;
    }

    public boolean trackPerProcStateCpuTimes() {
        return this.mConstants.TRACK_CPU_TIMES_BY_PROC_STATE && this.mPerProcStateCpuTimesAvailable;
    }

    public void systemServicesReady(Context context) {
        this.mConstants.startObserving(context.getContentResolver());
        registerUsbStateReceiver(context);
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public final class Constants extends ContentObserver {
        private static final int DEFAULT_BATTERY_CHARGED_DELAY_MS = 900000;
        private static final long DEFAULT_BATTERY_LEVEL_COLLECTION_DELAY_MS = 300000;
        private static final long DEFAULT_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = 600000;
        private static final long DEFAULT_KERNEL_UID_READERS_THROTTLE_TIME = 1000;
        private static final int DEFAULT_MAX_HISTORY_BUFFER_KB = 128;
        private static final int DEFAULT_MAX_HISTORY_BUFFER_LOW_RAM_DEVICE_KB = 64;
        private static final int DEFAULT_MAX_HISTORY_FILES = 32;
        private static final int DEFAULT_MAX_HISTORY_FILES_LOW_RAM_DEVICE = 64;
        private static final long DEFAULT_PROC_STATE_CPU_TIMES_READ_DELAY_MS = 5000;
        private static final boolean DEFAULT_TRACK_CPU_ACTIVE_CLUSTER_TIME = true;
        private static final boolean DEFAULT_TRACK_CPU_TIMES_BY_PROC_STATE = false;
        private static final long DEFAULT_UID_REMOVE_DELAY_MS = 300000;
        public static final String KEY_BATTERY_CHARGED_DELAY_MS = "battery_charged_delay_ms";
        public static final String KEY_BATTERY_LEVEL_COLLECTION_DELAY_MS = "battery_level_collection_delay_ms";
        public static final String KEY_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = "external_stats_collection_rate_limit_ms";
        public static final String KEY_KERNEL_UID_READERS_THROTTLE_TIME = "kernel_uid_readers_throttle_time";
        public static final String KEY_MAX_HISTORY_BUFFER_KB = "max_history_buffer_kb";
        public static final String KEY_MAX_HISTORY_FILES = "max_history_files";
        public static final String KEY_PROC_STATE_CPU_TIMES_READ_DELAY_MS = "proc_state_cpu_times_read_delay_ms";
        public static final String KEY_TRACK_CPU_ACTIVE_CLUSTER_TIME = "track_cpu_active_cluster_time";
        public static final String KEY_TRACK_CPU_TIMES_BY_PROC_STATE = "track_cpu_times_by_proc_state";
        public static final String KEY_UID_REMOVE_DELAY_MS = "uid_remove_delay_ms";
        public int BATTERY_CHARGED_DELAY_MS;
        public long BATTERY_LEVEL_COLLECTION_DELAY_MS;
        public long EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS;
        public long KERNEL_UID_READERS_THROTTLE_TIME;
        public int MAX_HISTORY_BUFFER;
        public int MAX_HISTORY_FILES;
        public long PROC_STATE_CPU_TIMES_READ_DELAY_MS;
        public boolean TRACK_CPU_ACTIVE_CLUSTER_TIME;
        public boolean TRACK_CPU_TIMES_BY_PROC_STATE;
        public long UID_REMOVE_DELAY_MS;
        private final KeyValueListParser mParser;
        private ContentResolver mResolver;

        public Constants(Handler handler) {
            super(handler);
            this.TRACK_CPU_TIMES_BY_PROC_STATE = false;
            this.TRACK_CPU_ACTIVE_CLUSTER_TIME = true;
            this.PROC_STATE_CPU_TIMES_READ_DELAY_MS = 5000L;
            this.UID_REMOVE_DELAY_MS = ParcelableCallAnalytics.MILLIS_IN_5_MINUTES;
            this.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = DEFAULT_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS;
            this.BATTERY_LEVEL_COLLECTION_DELAY_MS = ParcelableCallAnalytics.MILLIS_IN_5_MINUTES;
            this.BATTERY_CHARGED_DELAY_MS = DEFAULT_BATTERY_CHARGED_DELAY_MS;
            this.mParser = new KeyValueListParser(',');
            if (ActivityManager.isLowRamDeviceStatic()) {
                this.MAX_HISTORY_FILES = 64;
                this.MAX_HISTORY_BUFFER = 65536;
                return;
            }
            this.MAX_HISTORY_FILES = 32;
            this.MAX_HISTORY_BUFFER = 131072;
        }

        public void startObserving(ContentResolver resolver) {
            this.mResolver = resolver;
            this.mResolver.registerContentObserver(Settings.Global.getUriFor(Settings.Global.BATTERY_STATS_CONSTANTS), false, this);
            this.mResolver.registerContentObserver(Settings.Global.getUriFor(Settings.Global.BATTERY_CHARGING_STATE_UPDATE_DELAY), false, this);
            updateConstants();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            if (uri.equals(Settings.Global.getUriFor(Settings.Global.BATTERY_CHARGING_STATE_UPDATE_DELAY))) {
                synchronized (BatteryStatsImpl.this) {
                    updateBatteryChargedDelayMsLocked();
                }
                return;
            }
            updateConstants();
        }

        private void updateConstants() {
            int i;
            synchronized (BatteryStatsImpl.this) {
                try {
                    this.mParser.setString(Settings.Global.getString(this.mResolver, Settings.Global.BATTERY_STATS_CONSTANTS));
                } catch (IllegalArgumentException e) {
                    Slog.e(BatteryStatsImpl.TAG, "Bad batterystats settings", e);
                }
                updateTrackCpuTimesByProcStateLocked(this.TRACK_CPU_TIMES_BY_PROC_STATE, this.mParser.getBoolean(KEY_TRACK_CPU_TIMES_BY_PROC_STATE, false));
                this.TRACK_CPU_ACTIVE_CLUSTER_TIME = this.mParser.getBoolean(KEY_TRACK_CPU_ACTIVE_CLUSTER_TIME, true);
                updateProcStateCpuTimesReadDelayMs(this.PROC_STATE_CPU_TIMES_READ_DELAY_MS, this.mParser.getLong(KEY_PROC_STATE_CPU_TIMES_READ_DELAY_MS, 5000L));
                updateKernelUidReadersThrottleTime(this.KERNEL_UID_READERS_THROTTLE_TIME, this.mParser.getLong(KEY_KERNEL_UID_READERS_THROTTLE_TIME, 1000L));
                updateUidRemoveDelay(this.mParser.getLong(KEY_UID_REMOVE_DELAY_MS, ParcelableCallAnalytics.MILLIS_IN_5_MINUTES));
                this.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = this.mParser.getLong(KEY_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS, DEFAULT_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS);
                this.BATTERY_LEVEL_COLLECTION_DELAY_MS = this.mParser.getLong(KEY_BATTERY_LEVEL_COLLECTION_DELAY_MS, ParcelableCallAnalytics.MILLIS_IN_5_MINUTES);
                KeyValueListParser keyValueListParser = this.mParser;
                int i2 = 64;
                if (ActivityManager.isLowRamDeviceStatic()) {
                    i = 64;
                } else {
                    i = 32;
                }
                this.MAX_HISTORY_FILES = keyValueListParser.getInt(KEY_MAX_HISTORY_FILES, i);
                KeyValueListParser keyValueListParser2 = this.mParser;
                if (!ActivityManager.isLowRamDeviceStatic()) {
                    i2 = 128;
                }
                this.MAX_HISTORY_BUFFER = keyValueListParser2.getInt(KEY_MAX_HISTORY_BUFFER_KB, i2) * 1024;
                updateBatteryChargedDelayMsLocked();
            }
        }

        private void updateBatteryChargedDelayMsLocked() {
            int delay = Settings.Global.getInt(this.mResolver, Settings.Global.BATTERY_CHARGING_STATE_UPDATE_DELAY, -1);
            this.BATTERY_CHARGED_DELAY_MS = delay >= 0 ? delay : this.mParser.getInt(KEY_BATTERY_CHARGED_DELAY_MS, DEFAULT_BATTERY_CHARGED_DELAY_MS);
        }

        private void updateTrackCpuTimesByProcStateLocked(boolean wasEnabled, boolean isEnabled) {
            this.TRACK_CPU_TIMES_BY_PROC_STATE = isEnabled;
            if (isEnabled && !wasEnabled) {
                BatteryStatsImpl.this.mIsPerProcessStateCpuDataStale = true;
                BatteryStatsImpl.this.mExternalSync.scheduleCpuSyncDueToSettingChange();
                BatteryStatsImpl.this.mNumSingleUidCpuTimeReads = 0L;
                BatteryStatsImpl.this.mNumBatchedSingleUidCpuTimeReads = 0L;
                BatteryStatsImpl batteryStatsImpl = BatteryStatsImpl.this;
                batteryStatsImpl.mCpuTimeReadsTrackingStartTime = batteryStatsImpl.mClocks.uptimeMillis();
            }
        }

        private void updateProcStateCpuTimesReadDelayMs(long oldDelayMillis, long newDelayMillis) {
            this.PROC_STATE_CPU_TIMES_READ_DELAY_MS = newDelayMillis;
            if (oldDelayMillis != newDelayMillis) {
                BatteryStatsImpl.this.mNumSingleUidCpuTimeReads = 0L;
                BatteryStatsImpl.this.mNumBatchedSingleUidCpuTimeReads = 0L;
                BatteryStatsImpl batteryStatsImpl = BatteryStatsImpl.this;
                batteryStatsImpl.mCpuTimeReadsTrackingStartTime = batteryStatsImpl.mClocks.uptimeMillis();
            }
        }

        private void updateKernelUidReadersThrottleTime(long oldTimeMs, long newTimeMs) {
            this.KERNEL_UID_READERS_THROTTLE_TIME = newTimeMs;
            if (oldTimeMs != newTimeMs) {
                BatteryStatsImpl.this.mCpuUidUserSysTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
                BatteryStatsImpl.this.mCpuUidFreqTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
                BatteryStatsImpl.this.mCpuUidActiveTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
                BatteryStatsImpl.this.mCpuUidClusterTimeReader.setThrottle(this.KERNEL_UID_READERS_THROTTLE_TIME);
            }
        }

        private void updateUidRemoveDelay(long newTimeMs) {
            this.UID_REMOVE_DELAY_MS = newTimeMs;
            BatteryStatsImpl.this.clearPendingRemovedUids();
        }

        public void dumpLocked(PrintWriter pw) {
            pw.print(KEY_TRACK_CPU_TIMES_BY_PROC_STATE);
            pw.print("=");
            pw.println(this.TRACK_CPU_TIMES_BY_PROC_STATE);
            pw.print(KEY_TRACK_CPU_ACTIVE_CLUSTER_TIME);
            pw.print("=");
            pw.println(this.TRACK_CPU_ACTIVE_CLUSTER_TIME);
            pw.print(KEY_PROC_STATE_CPU_TIMES_READ_DELAY_MS);
            pw.print("=");
            pw.println(this.PROC_STATE_CPU_TIMES_READ_DELAY_MS);
            pw.print(KEY_KERNEL_UID_READERS_THROTTLE_TIME);
            pw.print("=");
            pw.println(this.KERNEL_UID_READERS_THROTTLE_TIME);
            pw.print(KEY_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS);
            pw.print("=");
            pw.println(this.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS);
            pw.print(KEY_BATTERY_LEVEL_COLLECTION_DELAY_MS);
            pw.print("=");
            pw.println(this.BATTERY_LEVEL_COLLECTION_DELAY_MS);
            pw.print(KEY_MAX_HISTORY_FILES);
            pw.print("=");
            pw.println(this.MAX_HISTORY_FILES);
            pw.print(KEY_MAX_HISTORY_BUFFER_KB);
            pw.print("=");
            pw.println(this.MAX_HISTORY_BUFFER / 1024);
            pw.print(KEY_BATTERY_CHARGED_DELAY_MS);
            pw.print("=");
            pw.println(this.BATTERY_CHARGED_DELAY_MS);
        }
    }

    public long getExternalStatsCollectionRateLimitMs() {
        long j;
        synchronized (this) {
            j = this.mConstants.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS;
        }
        return j;
    }

    @GuardedBy({"this"})
    public void dumpConstantsLocked(PrintWriter pw) {
        this.mConstants.dumpLocked(pw);
    }

    @GuardedBy({"this"})
    public void dumpCpuStatsLocked(PrintWriter pw) {
        int size = this.mUidStats.size();
        pw.println("Per UID CPU user & system time in ms:");
        for (int i = 0; i < size; i++) {
            int u = this.mUidStats.keyAt(i);
            Uid uid = this.mUidStats.get(u);
            pw.print("  ");
            pw.print(u);
            pw.print(": ");
            pw.print(uid.getUserCpuTimeUs(0) / 1000);
            pw.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            pw.println(uid.getSystemCpuTimeUs(0) / 1000);
        }
        pw.println("Per UID CPU active time in ms:");
        for (int i2 = 0; i2 < size; i2++) {
            int u2 = this.mUidStats.keyAt(i2);
            Uid uid2 = this.mUidStats.get(u2);
            if (uid2.getCpuActiveTime() > 0) {
                pw.print("  ");
                pw.print(u2);
                pw.print(": ");
                pw.println(uid2.getCpuActiveTime());
            }
        }
        pw.println("Per UID CPU cluster time in ms:");
        for (int i3 = 0; i3 < size; i3++) {
            int u3 = this.mUidStats.keyAt(i3);
            long[] times = this.mUidStats.get(u3).getCpuClusterTimes();
            if (times != null) {
                pw.print("  ");
                pw.print(u3);
                pw.print(": ");
                pw.println(Arrays.toString(times));
            }
        }
        pw.println("Per UID CPU frequency time in ms:");
        for (int i4 = 0; i4 < size; i4++) {
            int u4 = this.mUidStats.keyAt(i4);
            long[] times2 = this.mUidStats.get(u4).getCpuFreqTimes(0);
            if (times2 != null) {
                pw.print("  ");
                pw.print(u4);
                pw.print(": ");
                pw.println(Arrays.toString(times2));
            }
        }
    }

    public void writeAsyncLocked() {
        writeStatsLocked(false);
        writeHistoryLocked(false);
    }

    public void writeSyncLocked() {
        writeStatsLocked(true);
        writeHistoryLocked(true);
    }

    void writeStatsLocked(boolean sync) {
        if (this.mStatsFile == null) {
            Slog.w(TAG, "writeStatsLocked: no file associated with this instance");
        } else if (this.mShuttingDown) {
        } else {
            Parcel p = Parcel.obtain();
            SystemClock.uptimeMillis();
            writeSummaryToParcel(p, false);
            this.mLastWriteTime = this.mClocks.elapsedRealtime();
            writeParcelToFileLocked(p, this.mStatsFile, sync);
        }
    }

    void writeHistoryLocked(boolean sync) {
        if (this.mBatteryStatsHistory.getActiveFile() == null) {
            Slog.w(TAG, "writeHistoryLocked: no history file associated with this instance");
        } else if (this.mShuttingDown) {
        } else {
            Parcel p = Parcel.obtain();
            SystemClock.uptimeMillis();
            writeHistoryBuffer(p, true, true);
            writeParcelToFileLocked(p, this.mBatteryStatsHistory.getActiveFile(), sync);
        }
    }

    void writeParcelToFileLocked(final Parcel p, final AtomicFile file, boolean sync) {
        if (sync) {
            commitPendingDataToDisk(p, file);
        } else {
            BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.internal.os.BatteryStatsImpl.5
                @Override // java.lang.Runnable
                public void run() {
                    BatteryStatsImpl.this.commitPendingDataToDisk(p, file);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void commitPendingDataToDisk(Parcel p, AtomicFile file) {
        this.mWriteLock.lock();
        FileOutputStream fos = null;
        try {
            try {
                long startTime = SystemClock.uptimeMillis();
                fos = file.startWrite();
                fos.write(p.marshall());
                fos.flush();
                file.finishWrite(fos);
                EventLogTags.writeCommitSysConfigFile(BatteryStats.SERVICE_NAME, SystemClock.uptimeMillis() - startTime);
            } catch (IOException e) {
                Slog.w(TAG, "Error writing battery statistics", e);
                file.failWrite(fos);
            }
        } finally {
            p.recycle();
            this.mWriteLock.unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Throwable, java.lang.Exception] */
    /* JADX WARN: Type inference failed for: r3v4, types: [long] */
    @UnsupportedAppUsage
    public void readLocked() {
        Parcel history;
        if (this.mDailyFile != null) {
            readDailyStatsLocked();
        }
        if (this.mStatsFile == null) {
            Slog.w(TAG, "readLocked: no file associated with this instance");
        } else if (this.mBatteryStatsHistory.getActiveFile() == null) {
            Slog.w(TAG, "readLocked: no history file associated with this instance");
        } else {
            this.mUidStats.clear();
            Parcel stats = Parcel.obtain();
            try {
                try {
                    ?? uptimeMillis = SystemClock.uptimeMillis();
                    byte[] raw = this.mStatsFile.readFully();
                    stats.unmarshall(raw, 0, raw.length);
                    stats.setDataPosition(0);
                    readSummaryFromParcel(stats);
                    history = uptimeMillis;
                } catch (Exception e) {
                    Slog.e(TAG, "Error reading battery statistics", e);
                    resetAllStatsLocked();
                    history = e;
                }
                try {
                    stats.recycle();
                    history = Parcel.obtain();
                    try {
                        SystemClock.uptimeMillis();
                        byte[] raw2 = this.mBatteryStatsHistory.getActiveFile().readFully();
                        if (raw2.length > 0) {
                            history.unmarshall(raw2, 0, raw2.length);
                            history.setDataPosition(0);
                            readHistoryBuffer(history, true);
                        }
                    } catch (Exception e2) {
                        Slog.e(TAG, "Error reading battery history", e2);
                        clearHistoryLocked();
                        this.mBatteryStatsHistory.resetAllFiles();
                    }
                    this.mEndPlatformVersion = Build.ID;
                    if (this.mHistoryBuffer.dataPosition() > 0 || this.mBatteryStatsHistory.getFilesNumbers().size() > 1) {
                        this.mRecordingHistory = true;
                        long elapsedRealtime = this.mClocks.elapsedRealtime();
                        long uptime = this.mClocks.uptimeMillis();
                        addHistoryBufferLocked(elapsedRealtime, (byte) 4, this.mHistoryCur);
                        startRecordingHistory(elapsedRealtime, uptime, false);
                    }
                    recordDailyStatsIfNeededLocked(false);
                } finally {
                    history.recycle();
                }
            } finally {
                stats.recycle();
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    void readHistoryBuffer(Parcel in, boolean andOldHistory) throws ParcelFormatException {
        int version = in.readInt();
        if (version != 186) {
            Slog.w("BatteryStats", "readHistoryBuffer: version got " + version + ", expected 186; erasing old stats");
            return;
        }
        long historyBaseTime = in.readLong();
        this.mHistoryBuffer.setDataSize(0);
        this.mHistoryBuffer.setDataPosition(0);
        int bufSize = in.readInt();
        int curPos = in.dataPosition();
        if (bufSize >= this.mConstants.MAX_HISTORY_BUFFER * 100) {
            throw new ParcelFormatException("File corrupt: history data buffer too large " + bufSize);
        } else if ((bufSize & (-4)) != bufSize) {
            throw new ParcelFormatException("File corrupt: history data buffer not aligned " + bufSize);
        } else {
            this.mHistoryBuffer.appendFrom(in, curPos, bufSize);
            in.setDataPosition(curPos + bufSize);
            if (andOldHistory) {
                readOldHistory(in);
            }
            this.mHistoryBaseTime = historyBaseTime;
            if (this.mHistoryBaseTime > 0) {
                long oldnow = this.mClocks.elapsedRealtime();
                this.mHistoryBaseTime = (this.mHistoryBaseTime - oldnow) + 1;
            }
        }
    }

    void readOldHistory(Parcel in) {
    }

    void writeHistoryBuffer(Parcel out, boolean inclData, boolean andOldHistory) {
        out.writeInt(186);
        out.writeLong(this.mHistoryBaseTime + this.mLastHistoryElapsedRealtime);
        if (!inclData) {
            out.writeInt(0);
            out.writeInt(0);
            return;
        }
        out.writeInt(this.mHistoryBuffer.dataSize());
        Parcel parcel = this.mHistoryBuffer;
        out.appendFrom(parcel, 0, parcel.dataSize());
        if (andOldHistory) {
            writeOldHistory(out);
        }
    }

    void writeOldHistory(Parcel out) {
    }

    public void readSummaryFromParcel(Parcel in) throws ParcelFormatException {
        boolean z;
        boolean z2;
        boolean z3;
        int version;
        boolean inclHistory;
        int numTags;
        int NPKG;
        int uid;
        int length;
        int NMS;
        boolean inclHistory2;
        int numClusters;
        int numTags2;
        int NPKG2;
        int NPKG3;
        int NWR;
        boolean z4;
        BatteryStatsImpl batteryStatsImpl = this;
        Parcel parcel = in;
        int version2 = in.readInt();
        if (version2 != 186) {
            Slog.w("BatteryStats", "readFromParcel: version got " + version2 + ", expected 186; erasing old stats");
            return;
        }
        boolean inclHistory3 = in.readBoolean();
        if (inclHistory3) {
            batteryStatsImpl.readHistoryBuffer(parcel, true);
            batteryStatsImpl.mBatteryStatsHistory.readFromParcel(parcel);
        }
        batteryStatsImpl.mHistoryTagPool.clear();
        boolean z5 = false;
        batteryStatsImpl.mNextHistoryTagIdx = 0;
        batteryStatsImpl.mNumHistoryTagChars = 0;
        int numTags3 = in.readInt();
        for (int i = 0; i < numTags3; i++) {
            int idx = in.readInt();
            String str = in.readString();
            if (str == null) {
                throw new ParcelFormatException("null history tag string");
            }
            int uid2 = in.readInt();
            BatteryStats.HistoryTag tag = new BatteryStats.HistoryTag();
            tag.string = str;
            tag.uid = uid2;
            tag.poolIdx = idx;
            batteryStatsImpl.mHistoryTagPool.put(tag, Integer.valueOf(idx));
            if (idx >= batteryStatsImpl.mNextHistoryTagIdx) {
                batteryStatsImpl.mNextHistoryTagIdx = idx + 1;
            }
            batteryStatsImpl.mNumHistoryTagChars += tag.string.length() + 1;
        }
        int i2 = in.readInt();
        batteryStatsImpl.mStartCount = i2;
        batteryStatsImpl.mUptime = in.readLong();
        batteryStatsImpl.mRealtime = in.readLong();
        batteryStatsImpl.mStartClockTime = in.readLong();
        batteryStatsImpl.mStartPlatformVersion = in.readString();
        batteryStatsImpl.mEndPlatformVersion = in.readString();
        batteryStatsImpl.mOnBatteryTimeBase.readSummaryFromParcel(parcel);
        batteryStatsImpl.mOnBatteryScreenOffTimeBase.readSummaryFromParcel(parcel);
        batteryStatsImpl.mDischargeUnplugLevel = in.readInt();
        batteryStatsImpl.mDischargePlugLevel = in.readInt();
        batteryStatsImpl.mDischargeCurrentLevel = in.readInt();
        batteryStatsImpl.mCurrentBatteryLevel = in.readInt();
        batteryStatsImpl.mEstimatedBatteryCapacity = in.readInt();
        batteryStatsImpl.mMinLearnedBatteryCapacity = in.readInt();
        batteryStatsImpl.mMaxLearnedBatteryCapacity = in.readInt();
        batteryStatsImpl.mLowDischargeAmountSinceCharge = in.readInt();
        batteryStatsImpl.mHighDischargeAmountSinceCharge = in.readInt();
        batteryStatsImpl.mDischargeAmountScreenOnSinceCharge = in.readInt();
        batteryStatsImpl.mDischargeAmountScreenOffSinceCharge = in.readInt();
        batteryStatsImpl.mDischargeAmountScreenDozeSinceCharge = in.readInt();
        batteryStatsImpl.mDischargeStepTracker.readFromParcel(parcel);
        batteryStatsImpl.mChargeStepTracker.readFromParcel(parcel);
        batteryStatsImpl.mDailyDischargeStepTracker.readFromParcel(parcel);
        batteryStatsImpl.mDailyChargeStepTracker.readFromParcel(parcel);
        batteryStatsImpl.mDischargeCounter.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeScreenOffCounter.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeScreenDozeCounter.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeLightDozeCounter.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDischargeDeepDozeCounter.readSummaryFromParcelLocked(parcel);
        int NPKG4 = in.readInt();
        if (NPKG4 > 0) {
            batteryStatsImpl.mDailyPackageChanges = new ArrayList<>(NPKG4);
            while (NPKG4 > 0) {
                NPKG4--;
                BatteryStats.PackageChange pc = new BatteryStats.PackageChange();
                pc.mPackageName = in.readString();
                if (in.readInt() != 0) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                pc.mUpdate = z4;
                pc.mVersionCode = in.readLong();
                batteryStatsImpl.mDailyPackageChanges.add(pc);
            }
        } else {
            batteryStatsImpl.mDailyPackageChanges = null;
        }
        batteryStatsImpl.mDailyStartTime = in.readLong();
        batteryStatsImpl.mNextMinDailyDeadline = in.readLong();
        batteryStatsImpl.mNextMaxDailyDeadline = in.readLong();
        batteryStatsImpl.mStartCount++;
        batteryStatsImpl.mScreenState = 0;
        batteryStatsImpl.mScreenOnTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mScreenDozeTimer.readSummaryFromParcelLocked(parcel);
        for (int i3 = 0; i3 < 5; i3++) {
            batteryStatsImpl.mScreenBrightnessTimer[i3].readSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mInteractive = false;
        batteryStatsImpl.mInteractiveTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mPhoneOn = false;
        batteryStatsImpl.mPowerSaveModeEnabledTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mLongestLightIdleTime = in.readLong();
        batteryStatsImpl.mLongestFullIdleTime = in.readLong();
        batteryStatsImpl.mDeviceIdleModeLightTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDeviceIdleModeFullTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDeviceLightIdlingTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mDeviceIdlingTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mPhoneOnTimer.readSummaryFromParcelLocked(parcel);
        for (int i4 = 0; i4 < 5; i4++) {
            batteryStatsImpl.mPhoneSignalStrengthsTimer[i4].readSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mPhoneSignalScanningTimer.readSummaryFromParcelLocked(parcel);
        for (int i5 = 0; i5 < 23; i5++) {
            batteryStatsImpl.mPhoneDataConnectionsTimer[i5].readSummaryFromParcelLocked(parcel);
        }
        for (int i6 = 0; i6 < 10; i6++) {
            batteryStatsImpl.mNetworkByteActivityCounters[i6].readSummaryFromParcelLocked(parcel);
            batteryStatsImpl.mNetworkPacketActivityCounters[i6].readSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mMobileRadioPowerState = 1;
        batteryStatsImpl.mMobileRadioActiveTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mMobileRadioActivePerAppTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mMobileRadioActiveAdjustedTime.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mMobileRadioActiveUnknownTime.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mMobileRadioActiveUnknownCount.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mWifiMulticastWakelockTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mWifiRadioPowerState = 1;
        batteryStatsImpl.mWifiOn = false;
        batteryStatsImpl.mWifiOnTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mGlobalWifiRunning = false;
        batteryStatsImpl.mGlobalWifiRunningTimer.readSummaryFromParcelLocked(parcel);
        for (int i7 = 0; i7 < 8; i7++) {
            batteryStatsImpl.mWifiStateTimer[i7].readSummaryFromParcelLocked(parcel);
        }
        for (int i8 = 0; i8 < 13; i8++) {
            batteryStatsImpl.mWifiSupplStateTimer[i8].readSummaryFromParcelLocked(parcel);
        }
        for (int i9 = 0; i9 < 5; i9++) {
            batteryStatsImpl.mWifiSignalStrengthsTimer[i9].readSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mWifiActiveTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mWifiActivity.readSummaryFromParcel(parcel);
        for (int i10 = 0; i10 < 2; i10++) {
            batteryStatsImpl.mGpsSignalQualityTimer[i10].readSummaryFromParcelLocked(parcel);
        }
        batteryStatsImpl.mBluetoothActivity.readSummaryFromParcel(parcel);
        batteryStatsImpl.mModemActivity.readSummaryFromParcel(parcel);
        if (in.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        batteryStatsImpl.mHasWifiReporting = z;
        if (in.readInt() != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        batteryStatsImpl.mHasBluetoothReporting = z2;
        if (in.readInt() != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        batteryStatsImpl.mHasModemReporting = z3;
        batteryStatsImpl.mNumConnectivityChange = in.readInt();
        batteryStatsImpl.mFlashlightOnNesting = 0;
        batteryStatsImpl.mFlashlightOnTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mCameraOnNesting = 0;
        batteryStatsImpl.mCameraOnTimer.readSummaryFromParcelLocked(parcel);
        batteryStatsImpl.mBluetoothScanNesting = 0;
        batteryStatsImpl.mBluetoothScanTimer.readSummaryFromParcelLocked(parcel);
        int NRPMS = in.readInt();
        if (NRPMS > 10000) {
            throw new ParcelFormatException("File corrupt: too many rpm stats " + NRPMS);
        }
        for (int irpm = 0; irpm < NRPMS; irpm++) {
            if (in.readInt() != 0) {
                String rpmName = in.readString();
                batteryStatsImpl.getRpmTimerLocked(rpmName).readSummaryFromParcelLocked(parcel);
            }
        }
        int NSORPMS = in.readInt();
        if (NSORPMS > 10000) {
            throw new ParcelFormatException("File corrupt: too many screen-off rpm stats " + NSORPMS);
        }
        for (int irpm2 = 0; irpm2 < NSORPMS; irpm2++) {
            if (in.readInt() != 0) {
                String rpmName2 = in.readString();
                batteryStatsImpl.getScreenOffRpmTimerLocked(rpmName2).readSummaryFromParcelLocked(parcel);
            }
        }
        int NKW = in.readInt();
        if (NKW > 10000) {
            throw new ParcelFormatException("File corrupt: too many kernel wake locks " + NKW);
        }
        for (int ikw = 0; ikw < NKW; ikw++) {
            if (in.readInt() != 0) {
                String kwltName = in.readString();
                batteryStatsImpl.getKernelWakelockTimerLocked(kwltName).readSummaryFromParcelLocked(parcel);
            }
        }
        int NWR2 = in.readInt();
        if (NWR2 > 10000) {
            throw new ParcelFormatException("File corrupt: too many wakeup reasons " + NWR2);
        }
        for (int iwr = 0; iwr < NWR2; iwr++) {
            if (in.readInt() != 0) {
                String reasonName = in.readString();
                batteryStatsImpl.getWakeupReasonTimerLocked(reasonName).readSummaryFromParcelLocked(parcel);
            }
        }
        int NMS2 = in.readInt();
        int ims = 0;
        while (ims < NMS2) {
            if (in.readInt() == 0) {
                NWR = NWR2;
            } else {
                NWR = NWR2;
                long kmstName = in.readLong();
                batteryStatsImpl.getKernelMemoryTimerLocked(kmstName).readSummaryFromParcelLocked(parcel);
            }
            ims++;
            NWR2 = NWR;
        }
        int NU = in.readInt();
        if (NU > 10000) {
            throw new ParcelFormatException("File corrupt: too many uids " + NU);
        }
        int iu = 0;
        while (iu < NU) {
            int uid3 = in.readInt();
            Uid u = new Uid(batteryStatsImpl, uid3);
            batteryStatsImpl.mUidStats.put(uid3, u);
            u.mOnBatteryBackgroundTimeBase.readSummaryFromParcel(parcel);
            u.mOnBatteryScreenOffBackgroundTimeBase.readSummaryFromParcel(parcel);
            u.mWifiRunning = z5;
            if (in.readInt() != 0) {
                u.mWifiRunningTimer.readSummaryFromParcelLocked(parcel);
            }
            u.mFullWifiLockOut = z5;
            if (in.readInt() != 0) {
                u.mFullWifiLockTimer.readSummaryFromParcelLocked(parcel);
            }
            u.mWifiScanStarted = z5;
            if (in.readInt() != 0) {
                u.mWifiScanTimer.readSummaryFromParcelLocked(parcel);
            }
            u.mWifiBatchedScanBinStarted = -1;
            for (int i11 = 0; i11 < 5; i11++) {
                if (in.readInt() != 0) {
                    u.makeWifiBatchedScanBin(i11, null);
                    u.mWifiBatchedScanTimer[i11].readSummaryFromParcelLocked(parcel);
                }
            }
            u.mWifiMulticastWakelockCount = 0;
            if (in.readInt() != 0) {
                u.mWifiMulticastTimer.readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createAudioTurnedOnTimerLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createVideoTurnedOnTimerLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createFlashlightTurnedOnTimerLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createCameraTurnedOnTimerLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createForegroundActivityTimerLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createForegroundServiceTimerLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createAggregatedPartialWakelockTimerLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createBluetoothScanTimerLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createBluetoothUnoptimizedScanTimerLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createBluetoothScanResultCounterLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                u.createBluetoothScanResultBgCounterLocked().readSummaryFromParcelLocked(parcel);
            }
            u.mProcessState = 21;
            for (int i12 = 0; i12 < 7; i12++) {
                if (in.readInt() != 0) {
                    u.makeProcessState(i12, null);
                    u.mProcessStateTimer[i12].readSummaryFromParcelLocked(parcel);
                }
            }
            int i13 = in.readInt();
            if (i13 != 0) {
                u.createVibratorOnTimerLocked().readSummaryFromParcelLocked(parcel);
            }
            if (in.readInt() != 0) {
                if (u.mUserActivityCounters == null) {
                    u.initUserActivityLocked();
                }
                for (int i14 = 0; i14 < Uid.NUM_USER_ACTIVITY_TYPES; i14++) {
                    u.mUserActivityCounters[i14].readSummaryFromParcelLocked(parcel);
                }
            }
            if (in.readInt() != 0) {
                if (u.mNetworkByteActivityCounters == null) {
                    u.initNetworkActivityLocked();
                }
                for (int i15 = 0; i15 < 10; i15++) {
                    u.mNetworkByteActivityCounters[i15].readSummaryFromParcelLocked(parcel);
                    u.mNetworkPacketActivityCounters[i15].readSummaryFromParcelLocked(parcel);
                }
                u.mMobileRadioActiveTime.readSummaryFromParcelLocked(parcel);
                u.mMobileRadioActiveCount.readSummaryFromParcelLocked(parcel);
            }
            u.mUserCpuTime.readSummaryFromParcelLocked(parcel);
            u.mSystemCpuTime.readSummaryFromParcelLocked(parcel);
            if (in.readInt() != 0) {
                int numClusters2 = in.readInt();
                PowerProfile powerProfile = batteryStatsImpl.mPowerProfile;
                if (powerProfile == null) {
                    version = version2;
                } else if (powerProfile.getNumCpuClusters() != numClusters2) {
                    throw new ParcelFormatException("Incompatible cpu cluster arrangement");
                } else {
                    version = version2;
                }
                detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                u.mCpuClusterSpeedTimesUs = new LongSamplingCounter[numClusters2];
                int cluster = 0;
                while (cluster < numClusters2) {
                    if (in.readInt() != 0) {
                        int NSB = in.readInt();
                        inclHistory2 = inclHistory3;
                        PowerProfile powerProfile2 = batteryStatsImpl.mPowerProfile;
                        if (powerProfile2 == null) {
                            numClusters = numClusters2;
                            numTags2 = numTags3;
                        } else if (powerProfile2.getNumSpeedStepsInCpuCluster(cluster) != NSB) {
                            throw new ParcelFormatException("File corrupt: too many speed bins " + NSB);
                        } else {
                            numClusters = numClusters2;
                            numTags2 = numTags3;
                        }
                        u.mCpuClusterSpeedTimesUs[cluster] = new LongSamplingCounter[NSB];
                        int speed = 0;
                        while (speed < NSB) {
                            if (in.readInt() == 0) {
                                NPKG3 = NPKG4;
                            } else {
                                NPKG3 = NPKG4;
                                u.mCpuClusterSpeedTimesUs[cluster][speed] = new LongSamplingCounter(batteryStatsImpl.mOnBatteryTimeBase);
                                u.mCpuClusterSpeedTimesUs[cluster][speed].readSummaryFromParcelLocked(parcel);
                            }
                            speed++;
                            NPKG4 = NPKG3;
                        }
                        NPKG2 = NPKG4;
                    } else {
                        inclHistory2 = inclHistory3;
                        numClusters = numClusters2;
                        numTags2 = numTags3;
                        NPKG2 = NPKG4;
                        u.mCpuClusterSpeedTimesUs[cluster] = null;
                    }
                    cluster++;
                    inclHistory3 = inclHistory2;
                    numClusters2 = numClusters;
                    numTags3 = numTags2;
                    NPKG4 = NPKG2;
                }
                inclHistory = inclHistory3;
                numTags = numTags3;
                NPKG = NPKG4;
            } else {
                version = version2;
                inclHistory = inclHistory3;
                numTags = numTags3;
                NPKG = NPKG4;
                detachIfNotNull(u.mCpuClusterSpeedTimesUs);
                u.mCpuClusterSpeedTimesUs = null;
            }
            detachIfNotNull(u.mCpuFreqTimeMs);
            u.mCpuFreqTimeMs = LongSamplingCounterArray.readSummaryFromParcelLocked(parcel, batteryStatsImpl.mOnBatteryTimeBase);
            detachIfNotNull(u.mScreenOffCpuFreqTimeMs);
            u.mScreenOffCpuFreqTimeMs = LongSamplingCounterArray.readSummaryFromParcelLocked(parcel, batteryStatsImpl.mOnBatteryScreenOffTimeBase);
            u.mCpuActiveTimeMs.readSummaryFromParcelLocked(parcel);
            u.mCpuClusterTimesMs.readSummaryFromParcelLocked(parcel);
            int length2 = in.readInt();
            if (length2 == 7) {
                detachIfNotNull(u.mProcStateTimeMs);
                u.mProcStateTimeMs = new LongSamplingCounterArray[length2];
                for (int procState = 0; procState < length2; procState++) {
                    u.mProcStateTimeMs[procState] = LongSamplingCounterArray.readSummaryFromParcelLocked(parcel, batteryStatsImpl.mOnBatteryTimeBase);
                }
            } else {
                detachIfNotNull(u.mProcStateTimeMs);
                u.mProcStateTimeMs = null;
            }
            int length3 = in.readInt();
            if (length3 == 7) {
                detachIfNotNull(u.mProcStateScreenOffTimeMs);
                u.mProcStateScreenOffTimeMs = new LongSamplingCounterArray[length3];
                for (int procState2 = 0; procState2 < length3; procState2++) {
                    u.mProcStateScreenOffTimeMs[procState2] = LongSamplingCounterArray.readSummaryFromParcelLocked(parcel, batteryStatsImpl.mOnBatteryScreenOffTimeBase);
                }
            } else {
                detachIfNotNull(u.mProcStateScreenOffTimeMs);
                u.mProcStateScreenOffTimeMs = null;
            }
            if (in.readInt() != 0) {
                detachIfNotNull(u.mMobileRadioApWakeupCount);
                u.mMobileRadioApWakeupCount = new LongSamplingCounter(batteryStatsImpl.mOnBatteryTimeBase);
                u.mMobileRadioApWakeupCount.readSummaryFromParcelLocked(parcel);
            } else {
                detachIfNotNull(u.mMobileRadioApWakeupCount);
                u.mMobileRadioApWakeupCount = null;
            }
            if (in.readInt() != 0) {
                detachIfNotNull(u.mWifiRadioApWakeupCount);
                u.mWifiRadioApWakeupCount = new LongSamplingCounter(batteryStatsImpl.mOnBatteryTimeBase);
                u.mWifiRadioApWakeupCount.readSummaryFromParcelLocked(parcel);
            } else {
                detachIfNotNull(u.mWifiRadioApWakeupCount);
                u.mWifiRadioApWakeupCount = null;
            }
            int NW = in.readInt();
            if (NW > MAX_WAKELOCKS_PER_UID + 1) {
                throw new ParcelFormatException("File corrupt: too many wake locks " + NW);
            }
            for (int iw = 0; iw < NW; iw++) {
                String wlName = in.readString();
                u.readWakeSummaryFromParcelLocked(wlName, parcel);
            }
            int NS = in.readInt();
            if (NS > MAX_WAKELOCKS_PER_UID + 1) {
                throw new ParcelFormatException("File corrupt: too many syncs " + NS);
            }
            for (int is = 0; is < NS; is++) {
                String name = in.readString();
                u.readSyncSummaryFromParcelLocked(name, parcel);
            }
            int NJ = in.readInt();
            if (NJ > MAX_WAKELOCKS_PER_UID + 1) {
                throw new ParcelFormatException("File corrupt: too many job timers " + NJ);
            }
            for (int ij = 0; ij < NJ; ij++) {
                String name2 = in.readString();
                u.readJobSummaryFromParcelLocked(name2, parcel);
            }
            u.readJobCompletionsFromParcelLocked(parcel);
            u.mJobsDeferredEventCount.readSummaryFromParcelLocked(parcel);
            u.mJobsDeferredCount.readSummaryFromParcelLocked(parcel);
            u.mJobsFreshnessTimeMs.readSummaryFromParcelLocked(parcel);
            detachIfNotNull(u.mJobsFreshnessBuckets);
            int i16 = 0;
            while (i16 < JOB_FRESHNESS_BUCKETS.length) {
                if (in.readInt() == 0) {
                    length = length3;
                    NMS = NMS2;
                } else {
                    length = length3;
                    NMS = NMS2;
                    u.mJobsFreshnessBuckets[i16] = new Counter(u.mBsi.mOnBatteryTimeBase);
                    u.mJobsFreshnessBuckets[i16].readSummaryFromParcelLocked(parcel);
                }
                i16++;
                length3 = length;
                NMS2 = NMS;
            }
            int NMS3 = NMS2;
            int NP = in.readInt();
            if (NP > 1000) {
                throw new ParcelFormatException("File corrupt: too many sensors " + NP);
            }
            int is2 = 0;
            while (is2 < NP) {
                int seNumber = in.readInt();
                if (in.readInt() == 0) {
                    uid = uid3;
                } else {
                    uid = uid3;
                    u.getSensorTimerLocked(seNumber, true).readSummaryFromParcelLocked(parcel);
                }
                is2++;
                uid3 = uid;
            }
            int NP2 = in.readInt();
            if (NP2 > 1000) {
                throw new ParcelFormatException("File corrupt: too many processes " + NP2);
            }
            int ip = 0;
            while (ip < NP2) {
                String procName = in.readString();
                Uid.Proc p = u.getProcessStatsLocked(procName);
                p.mUserTime = in.readLong();
                p.mSystemTime = in.readLong();
                p.mForegroundTime = in.readLong();
                p.mStarts = in.readInt();
                p.mNumCrashes = in.readInt();
                p.mNumAnrs = in.readInt();
                p.readExcessivePowerFromParcelLocked(parcel);
                ip++;
                NKW = NKW;
            }
            int NKW2 = NKW;
            int NP3 = in.readInt();
            if (NP3 > 10000) {
                throw new ParcelFormatException("File corrupt: too many packages " + NP3);
            }
            int ip2 = 0;
            while (ip2 < NP3) {
                String pkgName = in.readString();
                detachIfNotNull(u.mPackageStats.get(pkgName));
                Uid.Pkg p2 = u.getPackageStatsLocked(pkgName);
                int NWA = in.readInt();
                if (NWA > 10000) {
                    throw new ParcelFormatException("File corrupt: too many wakeup alarms " + NWA);
                }
                p2.mWakeupAlarms.clear();
                int iwa = 0;
                while (iwa < NWA) {
                    int NS2 = NS;
                    String tag2 = in.readString();
                    int NRPMS2 = NRPMS;
                    Counter c = new Counter(batteryStatsImpl.mOnBatteryScreenOffTimeBase);
                    c.readSummaryFromParcelLocked(parcel);
                    p2.mWakeupAlarms.put(tag2, c);
                    iwa++;
                    NS = NS2;
                    NRPMS = NRPMS2;
                    NSORPMS = NSORPMS;
                }
                int NRPMS3 = NRPMS;
                int NSORPMS2 = NSORPMS;
                NS = in.readInt();
                if (NS > 10000) {
                    throw new ParcelFormatException("File corrupt: too many services " + NS);
                }
                for (int is3 = 0; is3 < NS; is3++) {
                    String servName = in.readString();
                    Uid.Pkg.Serv s = u.getServiceStatsLocked(pkgName, servName);
                    s.mStartTime = in.readLong();
                    s.mStarts = in.readInt();
                    s.mLaunches = in.readInt();
                }
                ip2++;
                batteryStatsImpl = this;
                parcel = in;
                NRPMS = NRPMS3;
                NSORPMS = NSORPMS2;
            }
            iu++;
            batteryStatsImpl = this;
            parcel = in;
            version2 = version;
            inclHistory3 = inclHistory;
            NMS2 = NMS3;
            numTags3 = numTags;
            NPKG4 = NPKG;
            NKW = NKW2;
            z5 = false;
        }
    }

    public void writeSummaryToParcel(Parcel out, boolean inclHistory) {
        int i;
        BatteryStatsImpl batteryStatsImpl = this;
        pullPendingStateUpdatesLocked();
        getStartClockTime();
        long NOW_SYS = batteryStatsImpl.mClocks.uptimeMillis() * 1000;
        long NOWREAL_SYS = batteryStatsImpl.mClocks.elapsedRealtime() * 1000;
        out.writeInt(186);
        out.writeBoolean(inclHistory);
        int i2 = 1;
        if (inclHistory) {
            batteryStatsImpl.writeHistoryBuffer(out, true, true);
            batteryStatsImpl.mBatteryStatsHistory.writeToParcel(out);
        }
        out.writeInt(batteryStatsImpl.mHistoryTagPool.size());
        for (Map.Entry<BatteryStats.HistoryTag, Integer> ent : batteryStatsImpl.mHistoryTagPool.entrySet()) {
            BatteryStats.HistoryTag tag = ent.getKey();
            out.writeInt(ent.getValue().intValue());
            out.writeString(tag.string);
            out.writeInt(tag.uid);
        }
        out.writeInt(batteryStatsImpl.mStartCount);
        int i3 = 0;
        out.writeLong(batteryStatsImpl.computeUptime(NOW_SYS, 0));
        out.writeLong(batteryStatsImpl.computeRealtime(NOWREAL_SYS, 0));
        out.writeLong(batteryStatsImpl.mStartClockTime);
        out.writeString(batteryStatsImpl.mStartPlatformVersion);
        out.writeString(batteryStatsImpl.mEndPlatformVersion);
        batteryStatsImpl.mOnBatteryTimeBase.writeSummaryToParcel(out, NOW_SYS, NOWREAL_SYS);
        batteryStatsImpl.mOnBatteryScreenOffTimeBase.writeSummaryToParcel(out, NOW_SYS, NOWREAL_SYS);
        out.writeInt(batteryStatsImpl.mDischargeUnplugLevel);
        out.writeInt(batteryStatsImpl.mDischargePlugLevel);
        out.writeInt(batteryStatsImpl.mDischargeCurrentLevel);
        out.writeInt(batteryStatsImpl.mCurrentBatteryLevel);
        out.writeInt(batteryStatsImpl.mEstimatedBatteryCapacity);
        out.writeInt(batteryStatsImpl.mMinLearnedBatteryCapacity);
        out.writeInt(batteryStatsImpl.mMaxLearnedBatteryCapacity);
        out.writeInt(getLowDischargeAmountSinceCharge());
        out.writeInt(getHighDischargeAmountSinceCharge());
        out.writeInt(getDischargeAmountScreenOnSinceCharge());
        out.writeInt(getDischargeAmountScreenOffSinceCharge());
        out.writeInt(getDischargeAmountScreenDozeSinceCharge());
        batteryStatsImpl.mDischargeStepTracker.writeToParcel(out);
        batteryStatsImpl.mChargeStepTracker.writeToParcel(out);
        batteryStatsImpl.mDailyDischargeStepTracker.writeToParcel(out);
        batteryStatsImpl.mDailyChargeStepTracker.writeToParcel(out);
        batteryStatsImpl.mDischargeCounter.writeSummaryFromParcelLocked(out);
        batteryStatsImpl.mDischargeScreenOffCounter.writeSummaryFromParcelLocked(out);
        batteryStatsImpl.mDischargeScreenDozeCounter.writeSummaryFromParcelLocked(out);
        batteryStatsImpl.mDischargeLightDozeCounter.writeSummaryFromParcelLocked(out);
        batteryStatsImpl.mDischargeDeepDozeCounter.writeSummaryFromParcelLocked(out);
        ArrayList<BatteryStats.PackageChange> arrayList = batteryStatsImpl.mDailyPackageChanges;
        if (arrayList != null) {
            int NPKG = arrayList.size();
            out.writeInt(NPKG);
            for (int i4 = 0; i4 < NPKG; i4++) {
                BatteryStats.PackageChange pc = batteryStatsImpl.mDailyPackageChanges.get(i4);
                out.writeString(pc.mPackageName);
                out.writeInt(pc.mUpdate ? 1 : 0);
                out.writeLong(pc.mVersionCode);
            }
        } else {
            out.writeInt(0);
        }
        out.writeLong(batteryStatsImpl.mDailyStartTime);
        out.writeLong(batteryStatsImpl.mNextMinDailyDeadline);
        out.writeLong(batteryStatsImpl.mNextMaxDailyDeadline);
        batteryStatsImpl.mScreenOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mScreenDozeTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        int i5 = 0;
        while (true) {
            i = 5;
            if (i5 >= 5) {
                break;
            }
            batteryStatsImpl.mScreenBrightnessTimer[i5].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            i5++;
        }
        batteryStatsImpl.mInteractiveTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mPowerSaveModeEnabledTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        out.writeLong(batteryStatsImpl.mLongestLightIdleTime);
        out.writeLong(batteryStatsImpl.mLongestFullIdleTime);
        batteryStatsImpl.mDeviceIdleModeLightTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mDeviceIdleModeFullTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mDeviceLightIdlingTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mDeviceIdlingTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mPhoneOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        for (int i6 = 0; i6 < 5; i6++) {
            batteryStatsImpl.mPhoneSignalStrengthsTimer[i6].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        batteryStatsImpl.mPhoneSignalScanningTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        for (int i7 = 0; i7 < 23; i7++) {
            batteryStatsImpl.mPhoneDataConnectionsTimer[i7].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        for (int i8 = 0; i8 < 10; i8++) {
            batteryStatsImpl.mNetworkByteActivityCounters[i8].writeSummaryFromParcelLocked(out);
            batteryStatsImpl.mNetworkPacketActivityCounters[i8].writeSummaryFromParcelLocked(out);
        }
        batteryStatsImpl.mMobileRadioActiveTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mMobileRadioActivePerAppTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mMobileRadioActiveAdjustedTime.writeSummaryFromParcelLocked(out);
        batteryStatsImpl.mMobileRadioActiveUnknownTime.writeSummaryFromParcelLocked(out);
        batteryStatsImpl.mMobileRadioActiveUnknownCount.writeSummaryFromParcelLocked(out);
        batteryStatsImpl.mWifiMulticastWakelockTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mWifiOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mGlobalWifiRunningTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        for (int i9 = 0; i9 < 8; i9++) {
            batteryStatsImpl.mWifiStateTimer[i9].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        for (int i10 = 0; i10 < 13; i10++) {
            batteryStatsImpl.mWifiSupplStateTimer[i10].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        for (int i11 = 0; i11 < 5; i11++) {
            batteryStatsImpl.mWifiSignalStrengthsTimer[i11].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        batteryStatsImpl.mWifiActiveTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mWifiActivity.writeSummaryToParcel(out);
        for (int i12 = 0; i12 < 2; i12++) {
            batteryStatsImpl.mGpsSignalQualityTimer[i12].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        batteryStatsImpl.mBluetoothActivity.writeSummaryToParcel(out);
        batteryStatsImpl.mModemActivity.writeSummaryToParcel(out);
        out.writeInt(batteryStatsImpl.mHasWifiReporting ? 1 : 0);
        out.writeInt(batteryStatsImpl.mHasBluetoothReporting ? 1 : 0);
        out.writeInt(batteryStatsImpl.mHasModemReporting ? 1 : 0);
        out.writeInt(batteryStatsImpl.mNumConnectivityChange);
        batteryStatsImpl.mFlashlightOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mCameraOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        batteryStatsImpl.mBluetoothScanTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        out.writeInt(batteryStatsImpl.mRpmStats.size());
        for (Map.Entry<String, SamplingTimer> ent2 : batteryStatsImpl.mRpmStats.entrySet()) {
            Timer rpmt = ent2.getValue();
            if (rpmt != null) {
                out.writeInt(1);
                out.writeString(ent2.getKey());
                rpmt.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
        }
        out.writeInt(batteryStatsImpl.mScreenOffRpmStats.size());
        for (Map.Entry<String, SamplingTimer> ent3 : batteryStatsImpl.mScreenOffRpmStats.entrySet()) {
            Timer rpmt2 = ent3.getValue();
            if (rpmt2 != null) {
                out.writeInt(1);
                out.writeString(ent3.getKey());
                rpmt2.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
        }
        out.writeInt(batteryStatsImpl.mKernelWakelockStats.size());
        for (Map.Entry<String, SamplingTimer> ent4 : batteryStatsImpl.mKernelWakelockStats.entrySet()) {
            Timer kwlt = ent4.getValue();
            if (kwlt != null) {
                out.writeInt(1);
                out.writeString(ent4.getKey());
                kwlt.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
        }
        out.writeInt(batteryStatsImpl.mWakeupReasonStats.size());
        for (Map.Entry<String, SamplingTimer> ent5 : batteryStatsImpl.mWakeupReasonStats.entrySet()) {
            SamplingTimer timer = ent5.getValue();
            if (timer != null) {
                out.writeInt(1);
                out.writeString(ent5.getKey());
                timer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
        }
        out.writeInt(batteryStatsImpl.mKernelMemoryStats.size());
        for (int i13 = 0; i13 < batteryStatsImpl.mKernelMemoryStats.size(); i13++) {
            Timer kmt = batteryStatsImpl.mKernelMemoryStats.valueAt(i13);
            if (kmt != null) {
                out.writeInt(1);
                out.writeLong(batteryStatsImpl.mKernelMemoryStats.keyAt(i13));
                kmt.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
        }
        int NU = batteryStatsImpl.mUidStats.size();
        out.writeInt(NU);
        int NJ = 0;
        while (NJ < NU) {
            out.writeInt(batteryStatsImpl.mUidStats.keyAt(NJ));
            Uid u = batteryStatsImpl.mUidStats.valueAt(NJ);
            int NU2 = NU;
            int iu = NJ;
            u.mOnBatteryBackgroundTimeBase.writeSummaryToParcel(out, NOW_SYS, NOWREAL_SYS);
            u.mOnBatteryScreenOffBackgroundTimeBase.writeSummaryToParcel(out, NOW_SYS, NOWREAL_SYS);
            if (u.mWifiRunningTimer != null) {
                out.writeInt(i2);
                u.mWifiRunningTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mFullWifiLockTimer != null) {
                out.writeInt(i2);
                u.mFullWifiLockTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mWifiScanTimer != null) {
                out.writeInt(i2);
                u.mWifiScanTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            for (int i14 = 0; i14 < i; i14++) {
                if (u.mWifiBatchedScanTimer[i14] != null) {
                    out.writeInt(i2);
                    u.mWifiBatchedScanTimer[i14].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(i3);
                }
            }
            if (u.mWifiMulticastTimer != null) {
                out.writeInt(i2);
                u.mWifiMulticastTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mAudioTurnedOnTimer != null) {
                out.writeInt(i2);
                u.mAudioTurnedOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mVideoTurnedOnTimer != null) {
                out.writeInt(i2);
                u.mVideoTurnedOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mFlashlightTurnedOnTimer != null) {
                out.writeInt(i2);
                u.mFlashlightTurnedOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mCameraTurnedOnTimer != null) {
                out.writeInt(i2);
                u.mCameraTurnedOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mForegroundActivityTimer != null) {
                out.writeInt(i2);
                u.mForegroundActivityTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mForegroundServiceTimer != null) {
                out.writeInt(i2);
                u.mForegroundServiceTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mAggregatedPartialWakelockTimer != null) {
                out.writeInt(i2);
                u.mAggregatedPartialWakelockTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mBluetoothScanTimer != null) {
                out.writeInt(i2);
                u.mBluetoothScanTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mBluetoothUnoptimizedScanTimer != null) {
                out.writeInt(i2);
                u.mBluetoothUnoptimizedScanTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mBluetoothScanResultCounter != null) {
                out.writeInt(i2);
                u.mBluetoothScanResultCounter.writeSummaryFromParcelLocked(out);
            } else {
                out.writeInt(i3);
            }
            if (u.mBluetoothScanResultBgCounter != null) {
                out.writeInt(i2);
                u.mBluetoothScanResultBgCounter.writeSummaryFromParcelLocked(out);
            } else {
                out.writeInt(i3);
            }
            for (int i15 = 0; i15 < 7; i15++) {
                if (u.mProcessStateTimer[i15] != null) {
                    out.writeInt(i2);
                    u.mProcessStateTimer[i15].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(i3);
                }
            }
            if (u.mVibratorOnTimer != null) {
                out.writeInt(i2);
                u.mVibratorOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(i3);
            }
            if (u.mUserActivityCounters == null) {
                out.writeInt(i3);
            } else {
                out.writeInt(i2);
                for (int i16 = 0; i16 < Uid.NUM_USER_ACTIVITY_TYPES; i16++) {
                    u.mUserActivityCounters[i16].writeSummaryFromParcelLocked(out);
                }
            }
            if (u.mNetworkByteActivityCounters == null) {
                out.writeInt(i3);
            } else {
                out.writeInt(i2);
                for (int i17 = 0; i17 < 10; i17++) {
                    u.mNetworkByteActivityCounters[i17].writeSummaryFromParcelLocked(out);
                    u.mNetworkPacketActivityCounters[i17].writeSummaryFromParcelLocked(out);
                }
                u.mMobileRadioActiveTime.writeSummaryFromParcelLocked(out);
                u.mMobileRadioActiveCount.writeSummaryFromParcelLocked(out);
            }
            u.mUserCpuTime.writeSummaryFromParcelLocked(out);
            u.mSystemCpuTime.writeSummaryFromParcelLocked(out);
            if (u.mCpuClusterSpeedTimesUs != null) {
                out.writeInt(i2);
                out.writeInt(u.mCpuClusterSpeedTimesUs.length);
                LongSamplingCounter[][] longSamplingCounterArr = u.mCpuClusterSpeedTimesUs;
                int length = longSamplingCounterArr.length;
                for (int i18 = i3; i18 < length; i18++) {
                    LongSamplingCounter[] cpuSpeeds = longSamplingCounterArr[i18];
                    if (cpuSpeeds != null) {
                        out.writeInt(i2);
                        out.writeInt(cpuSpeeds.length);
                        int length2 = cpuSpeeds.length;
                        for (int i19 = i3; i19 < length2; i19++) {
                            LongSamplingCounter c = cpuSpeeds[i19];
                            if (c != null) {
                                out.writeInt(i2);
                                c.writeSummaryFromParcelLocked(out);
                            } else {
                                out.writeInt(i3);
                            }
                        }
                    } else {
                        out.writeInt(i3);
                    }
                }
            } else {
                out.writeInt(i3);
            }
            LongSamplingCounterArray.writeSummaryToParcelLocked(out, u.mCpuFreqTimeMs);
            LongSamplingCounterArray.writeSummaryToParcelLocked(out, u.mScreenOffCpuFreqTimeMs);
            u.mCpuActiveTimeMs.writeSummaryFromParcelLocked(out);
            u.mCpuClusterTimesMs.writeSummaryToParcelLocked(out);
            if (u.mProcStateTimeMs != null) {
                out.writeInt(u.mProcStateTimeMs.length);
                LongSamplingCounterArray[] longSamplingCounterArrayArr = u.mProcStateTimeMs;
                int length3 = longSamplingCounterArrayArr.length;
                for (int i20 = i3; i20 < length3; i20++) {
                    LongSamplingCounterArray counters = longSamplingCounterArrayArr[i20];
                    LongSamplingCounterArray.writeSummaryToParcelLocked(out, counters);
                }
            } else {
                out.writeInt(i3);
            }
            if (u.mProcStateScreenOffTimeMs != null) {
                out.writeInt(u.mProcStateScreenOffTimeMs.length);
                LongSamplingCounterArray[] longSamplingCounterArrayArr2 = u.mProcStateScreenOffTimeMs;
                int length4 = longSamplingCounterArrayArr2.length;
                for (int i21 = i3; i21 < length4; i21++) {
                    LongSamplingCounterArray counters2 = longSamplingCounterArrayArr2[i21];
                    LongSamplingCounterArray.writeSummaryToParcelLocked(out, counters2);
                }
            } else {
                out.writeInt(i3);
            }
            if (u.mMobileRadioApWakeupCount != null) {
                out.writeInt(i2);
                u.mMobileRadioApWakeupCount.writeSummaryFromParcelLocked(out);
            } else {
                out.writeInt(i3);
            }
            if (u.mWifiRadioApWakeupCount != null) {
                out.writeInt(i2);
                u.mWifiRadioApWakeupCount.writeSummaryFromParcelLocked(out);
            } else {
                out.writeInt(i3);
            }
            ArrayMap<String, Uid.Wakelock> wakeStats = u.mWakelockStats.getMap();
            int NW = wakeStats.size();
            out.writeInt(NW);
            for (int iw = 0; iw < NW; iw++) {
                out.writeString(wakeStats.keyAt(iw));
                Uid.Wakelock wl = wakeStats.valueAt(iw);
                if (wl.mTimerFull != null) {
                    out.writeInt(i2);
                    wl.mTimerFull.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(i3);
                }
                if (wl.mTimerPartial != null) {
                    out.writeInt(i2);
                    wl.mTimerPartial.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(i3);
                }
                if (wl.mTimerWindow != null) {
                    out.writeInt(i2);
                    wl.mTimerWindow.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(i3);
                }
                if (wl.mTimerDraw != null) {
                    out.writeInt(i2);
                    wl.mTimerDraw.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(i3);
                }
            }
            ArrayMap<String, DualTimer> syncStats = u.mSyncStats.getMap();
            int NS = syncStats.size();
            out.writeInt(NS);
            for (int is = 0; is < NS; is++) {
                out.writeString(syncStats.keyAt(is));
                syncStats.valueAt(is).writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            }
            ArrayMap<String, DualTimer> jobStats = u.mJobStats.getMap();
            int NJ2 = jobStats.size();
            out.writeInt(NJ2);
            for (int ij = 0; ij < NJ2; ij++) {
                out.writeString(jobStats.keyAt(ij));
                jobStats.valueAt(ij).writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            }
            u.writeJobCompletionsToParcelLocked(out);
            u.mJobsDeferredEventCount.writeSummaryFromParcelLocked(out);
            u.mJobsDeferredCount.writeSummaryFromParcelLocked(out);
            u.mJobsFreshnessTimeMs.writeSummaryFromParcelLocked(out);
            for (int i22 = 0; i22 < JOB_FRESHNESS_BUCKETS.length; i22++) {
                if (u.mJobsFreshnessBuckets[i22] != null) {
                    out.writeInt(i2);
                    u.mJobsFreshnessBuckets[i22].writeSummaryFromParcelLocked(out);
                } else {
                    out.writeInt(0);
                }
            }
            int NSE = u.mSensorStats.size();
            out.writeInt(NSE);
            int ise = 0;
            while (ise < NSE) {
                out.writeInt(u.mSensorStats.keyAt(ise));
                Uid.Sensor se = u.mSensorStats.valueAt(ise);
                ArrayMap<String, Uid.Wakelock> wakeStats2 = wakeStats;
                if (se.mTimer != null) {
                    out.writeInt(1);
                    se.mTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(0);
                }
                ise++;
                wakeStats = wakeStats2;
            }
            int NP = u.mProcessStats.size();
            out.writeInt(NP);
            int ip = 0;
            while (ip < NP) {
                out.writeString(u.mProcessStats.keyAt(ip));
                Uid.Proc ps = u.mProcessStats.valueAt(ip);
                out.writeLong(ps.mUserTime);
                out.writeLong(ps.mSystemTime);
                out.writeLong(ps.mForegroundTime);
                out.writeInt(ps.mStarts);
                out.writeInt(ps.mNumCrashes);
                out.writeInt(ps.mNumAnrs);
                ps.writeExcessivePowerToParcelLocked(out);
                ip++;
                NW = NW;
                syncStats = syncStats;
            }
            int NP2 = u.mPackageStats.size();
            out.writeInt(NP2);
            if (NP2 > 0) {
                Iterator<Map.Entry<String, Uid.Pkg>> it = u.mPackageStats.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Uid.Pkg> ent6 = it.next();
                    out.writeString(ent6.getKey());
                    Uid.Pkg ps2 = ent6.getValue();
                    int NWA = ps2.mWakeupAlarms.size();
                    out.writeInt(NWA);
                    int NP3 = NP2;
                    int NP4 = 0;
                    while (NP4 < NWA) {
                        out.writeString(ps2.mWakeupAlarms.keyAt(NP4));
                        ps2.mWakeupAlarms.valueAt(NP4).writeSummaryFromParcelLocked(out);
                        NP4++;
                        it = it;
                    }
                    Iterator<Map.Entry<String, Uid.Pkg>> it2 = it;
                    int NS2 = ps2.mServiceStats.size();
                    out.writeInt(NS2);
                    int is2 = 0;
                    while (is2 < NS2) {
                        out.writeString(ps2.mServiceStats.keyAt(is2));
                        Uid.Pkg.Serv ss = ps2.mServiceStats.valueAt(is2);
                        Map.Entry<String, Uid.Pkg> ent7 = ent6;
                        long time = ss.getStartTimeToNowLocked(batteryStatsImpl.mOnBatteryTimeBase.getUptime(NOW_SYS));
                        out.writeLong(time);
                        out.writeInt(ss.mStarts);
                        out.writeInt(ss.mLaunches);
                        is2++;
                        batteryStatsImpl = this;
                        ent6 = ent7;
                        NS2 = NS2;
                    }
                    batteryStatsImpl = this;
                    it = it2;
                    NP2 = NP3;
                }
            }
            NJ = iu + 1;
            i2 = 1;
            i3 = 0;
            i = 5;
            batteryStatsImpl = this;
            NU = NU2;
        }
    }

    public void readFromParcel(Parcel in) {
        readFromParcelLocked(in);
    }

    void readFromParcelLocked(Parcel in) {
        int magic = in.readInt();
        if (magic == MAGIC) {
            readHistoryBuffer(in, false);
            this.mBatteryStatsHistory.readFromParcel(in);
            this.mStartCount = in.readInt();
            this.mStartClockTime = in.readLong();
            this.mStartPlatformVersion = in.readString();
            this.mEndPlatformVersion = in.readString();
            this.mUptime = in.readLong();
            this.mUptimeStart = in.readLong();
            this.mRealtime = in.readLong();
            this.mRealtimeStart = in.readLong();
            this.mOnBattery = in.readInt() != 0;
            this.mEstimatedBatteryCapacity = in.readInt();
            this.mMinLearnedBatteryCapacity = in.readInt();
            this.mMaxLearnedBatteryCapacity = in.readInt();
            this.mOnBatteryInternal = false;
            this.mOnBatteryTimeBase.readFromParcel(in);
            this.mOnBatteryScreenOffTimeBase.readFromParcel(in);
            this.mScreenState = 0;
            this.mScreenOnTimer = new StopwatchTimer(this.mClocks, null, -1, null, this.mOnBatteryTimeBase, in);
            this.mScreenDozeTimer = new StopwatchTimer(this.mClocks, null, -1, null, this.mOnBatteryTimeBase, in);
            for (int i = 0; i < 5; i++) {
                this.mScreenBrightnessTimer[i] = new StopwatchTimer(this.mClocks, null, (-100) - i, null, this.mOnBatteryTimeBase, in);
            }
            this.mInteractive = false;
            this.mInteractiveTimer = new StopwatchTimer(this.mClocks, null, -10, null, this.mOnBatteryTimeBase, in);
            this.mPhoneOn = false;
            this.mPowerSaveModeEnabledTimer = new StopwatchTimer(this.mClocks, null, -2, null, this.mOnBatteryTimeBase, in);
            this.mLongestLightIdleTime = in.readLong();
            this.mLongestFullIdleTime = in.readLong();
            this.mDeviceIdleModeLightTimer = new StopwatchTimer(this.mClocks, null, -14, null, this.mOnBatteryTimeBase, in);
            this.mDeviceIdleModeFullTimer = new StopwatchTimer(this.mClocks, null, -11, null, this.mOnBatteryTimeBase, in);
            this.mDeviceLightIdlingTimer = new StopwatchTimer(this.mClocks, null, -15, null, this.mOnBatteryTimeBase, in);
            this.mDeviceIdlingTimer = new StopwatchTimer(this.mClocks, null, -12, null, this.mOnBatteryTimeBase, in);
            this.mPhoneOnTimer = new StopwatchTimer(this.mClocks, null, -3, null, this.mOnBatteryTimeBase, in);
            for (int i2 = 0; i2 < 5; i2++) {
                this.mPhoneSignalStrengthsTimer[i2] = new StopwatchTimer(this.mClocks, null, (-200) - i2, null, this.mOnBatteryTimeBase, in);
            }
            this.mPhoneSignalScanningTimer = new StopwatchTimer(this.mClocks, null, -199, null, this.mOnBatteryTimeBase, in);
            for (int i3 = 0; i3 < 23; i3++) {
                this.mPhoneDataConnectionsTimer[i3] = new StopwatchTimer(this.mClocks, null, (-300) - i3, null, this.mOnBatteryTimeBase, in);
            }
            for (int i4 = 0; i4 < 10; i4++) {
                this.mNetworkByteActivityCounters[i4] = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
                this.mNetworkPacketActivityCounters[i4] = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
            }
            this.mMobileRadioPowerState = 1;
            this.mMobileRadioActiveTimer = new StopwatchTimer(this.mClocks, null, -400, null, this.mOnBatteryTimeBase, in);
            this.mMobileRadioActivePerAppTimer = new StopwatchTimer(this.mClocks, null, -401, null, this.mOnBatteryTimeBase, in);
            this.mMobileRadioActiveAdjustedTime = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
            this.mMobileRadioActiveUnknownTime = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
            this.mMobileRadioActiveUnknownCount = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
            this.mWifiMulticastWakelockTimer = new StopwatchTimer(this.mClocks, null, -4, null, this.mOnBatteryTimeBase, in);
            this.mWifiRadioPowerState = 1;
            this.mWifiOn = false;
            this.mWifiOnTimer = new StopwatchTimer(this.mClocks, null, -4, null, this.mOnBatteryTimeBase, in);
            this.mGlobalWifiRunning = false;
            this.mGlobalWifiRunningTimer = new StopwatchTimer(this.mClocks, null, -5, null, this.mOnBatteryTimeBase, in);
            for (int i5 = 0; i5 < 8; i5++) {
                this.mWifiStateTimer[i5] = new StopwatchTimer(this.mClocks, null, (-600) - i5, null, this.mOnBatteryTimeBase, in);
            }
            for (int i6 = 0; i6 < 13; i6++) {
                this.mWifiSupplStateTimer[i6] = new StopwatchTimer(this.mClocks, null, (-700) - i6, null, this.mOnBatteryTimeBase, in);
            }
            for (int i7 = 0; i7 < 5; i7++) {
                this.mWifiSignalStrengthsTimer[i7] = new StopwatchTimer(this.mClocks, null, (-800) - i7, null, this.mOnBatteryTimeBase, in);
            }
            this.mWifiActiveTimer = new StopwatchTimer(this.mClocks, null, -900, null, this.mOnBatteryTimeBase, in);
            this.mWifiActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 1, in);
            for (int i8 = 0; i8 < 2; i8++) {
                this.mGpsSignalQualityTimer[i8] = new StopwatchTimer(this.mClocks, null, (-1000) - i8, null, this.mOnBatteryTimeBase, in);
            }
            this.mBluetoothActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 1, in);
            this.mModemActivity = new ControllerActivityCounterImpl(this.mOnBatteryTimeBase, 5, in);
            this.mHasWifiReporting = in.readInt() != 0;
            this.mHasBluetoothReporting = in.readInt() != 0;
            this.mHasModemReporting = in.readInt() != 0;
            this.mNumConnectivityChange = in.readInt();
            this.mAudioOnNesting = 0;
            this.mAudioOnTimer = new StopwatchTimer(this.mClocks, null, -7, null, this.mOnBatteryTimeBase);
            this.mVideoOnNesting = 0;
            this.mVideoOnTimer = new StopwatchTimer(this.mClocks, null, -8, null, this.mOnBatteryTimeBase);
            this.mFlashlightOnNesting = 0;
            this.mFlashlightOnTimer = new StopwatchTimer(this.mClocks, null, -9, null, this.mOnBatteryTimeBase, in);
            this.mCameraOnNesting = 0;
            this.mCameraOnTimer = new StopwatchTimer(this.mClocks, null, -13, null, this.mOnBatteryTimeBase, in);
            this.mBluetoothScanNesting = 0;
            this.mBluetoothScanTimer = new StopwatchTimer(this.mClocks, null, -14, null, this.mOnBatteryTimeBase, in);
            this.mDischargeUnplugLevel = in.readInt();
            this.mDischargePlugLevel = in.readInt();
            this.mDischargeCurrentLevel = in.readInt();
            this.mCurrentBatteryLevel = in.readInt();
            this.mLowDischargeAmountSinceCharge = in.readInt();
            this.mHighDischargeAmountSinceCharge = in.readInt();
            this.mDischargeAmountScreenOn = in.readInt();
            this.mDischargeAmountScreenOnSinceCharge = in.readInt();
            this.mDischargeAmountScreenOff = in.readInt();
            this.mDischargeAmountScreenOffSinceCharge = in.readInt();
            this.mDischargeAmountScreenDoze = in.readInt();
            this.mDischargeAmountScreenDozeSinceCharge = in.readInt();
            this.mDischargeStepTracker.readFromParcel(in);
            this.mChargeStepTracker.readFromParcel(in);
            this.mDischargeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
            this.mDischargeScreenOffCounter = new LongSamplingCounter(this.mOnBatteryScreenOffTimeBase, in);
            this.mDischargeScreenDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
            this.mDischargeLightDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
            this.mDischargeDeepDozeCounter = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
            this.mLastWriteTime = in.readLong();
            this.mRpmStats.clear();
            int NRPMS = in.readInt();
            for (int irpm = 0; irpm < NRPMS; irpm++) {
                if (in.readInt() != 0) {
                    String rpmName = in.readString();
                    SamplingTimer rpmt = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase, in);
                    this.mRpmStats.put(rpmName, rpmt);
                }
            }
            this.mScreenOffRpmStats.clear();
            int NSORPMS = in.readInt();
            for (int irpm2 = 0; irpm2 < NSORPMS; irpm2++) {
                if (in.readInt() != 0) {
                    String rpmName2 = in.readString();
                    SamplingTimer rpmt2 = new SamplingTimer(this.mClocks, this.mOnBatteryScreenOffTimeBase, in);
                    this.mScreenOffRpmStats.put(rpmName2, rpmt2);
                }
            }
            this.mKernelWakelockStats.clear();
            int NKW = in.readInt();
            for (int ikw = 0; ikw < NKW; ikw++) {
                if (in.readInt() != 0) {
                    String wakelockName = in.readString();
                    SamplingTimer kwlt = new SamplingTimer(this.mClocks, this.mOnBatteryScreenOffTimeBase, in);
                    this.mKernelWakelockStats.put(wakelockName, kwlt);
                }
            }
            this.mWakeupReasonStats.clear();
            int NWR = in.readInt();
            for (int iwr = 0; iwr < NWR; iwr++) {
                if (in.readInt() != 0) {
                    String reasonName = in.readString();
                    SamplingTimer timer = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase, in);
                    this.mWakeupReasonStats.put(reasonName, timer);
                }
            }
            this.mKernelMemoryStats.clear();
            int nmt = in.readInt();
            for (int imt = 0; imt < nmt; imt++) {
                if (in.readInt() != 0) {
                    Long bucket = Long.valueOf(in.readLong());
                    SamplingTimer kmt = new SamplingTimer(this.mClocks, this.mOnBatteryTimeBase, in);
                    this.mKernelMemoryStats.put(bucket.longValue(), kmt);
                }
            }
            this.mPartialTimers.clear();
            this.mFullTimers.clear();
            this.mWindowTimers.clear();
            this.mWifiRunningTimers.clear();
            this.mFullWifiLockTimers.clear();
            this.mWifiScanTimers.clear();
            this.mWifiBatchedScanTimers.clear();
            this.mWifiMulticastTimers.clear();
            this.mAudioTurnedOnTimers.clear();
            this.mVideoTurnedOnTimers.clear();
            this.mFlashlightTurnedOnTimers.clear();
            this.mCameraTurnedOnTimers.clear();
            int numUids = in.readInt();
            this.mUidStats.clear();
            for (int i9 = 0; i9 < numUids; i9++) {
                int uid = in.readInt();
                Uid u = new Uid(this, uid);
                u.readFromParcelLocked(this.mOnBatteryTimeBase, this.mOnBatteryScreenOffTimeBase, in);
                this.mUidStats.append(uid, u);
            }
            return;
        }
        throw new ParcelFormatException("Bad magic number: #" + Integer.toHexString(magic));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        writeToParcelLocked(out, true, flags);
    }

    @Override // android.os.BatteryStats
    public void writeToParcelWithoutUids(Parcel out, int flags) {
        writeToParcelLocked(out, false, flags);
    }

    void writeToParcelLocked(Parcel out, boolean inclUids, int flags) {
        pullPendingStateUpdatesLocked();
        getStartClockTime();
        long uSecUptime = this.mClocks.uptimeMillis() * 1000;
        long uSecRealtime = this.mClocks.elapsedRealtime() * 1000;
        this.mOnBatteryTimeBase.getRealtime(uSecRealtime);
        this.mOnBatteryScreenOffTimeBase.getRealtime(uSecRealtime);
        out.writeInt(MAGIC);
        writeHistoryBuffer(out, true, false);
        this.mBatteryStatsHistory.writeToParcel(out);
        out.writeInt(this.mStartCount);
        out.writeLong(this.mStartClockTime);
        out.writeString(this.mStartPlatformVersion);
        out.writeString(this.mEndPlatformVersion);
        out.writeLong(this.mUptime);
        out.writeLong(this.mUptimeStart);
        out.writeLong(this.mRealtime);
        out.writeLong(this.mRealtimeStart);
        out.writeInt(this.mOnBattery ? 1 : 0);
        out.writeInt(this.mEstimatedBatteryCapacity);
        out.writeInt(this.mMinLearnedBatteryCapacity);
        out.writeInt(this.mMaxLearnedBatteryCapacity);
        this.mOnBatteryTimeBase.writeToParcel(out, uSecUptime, uSecRealtime);
        this.mOnBatteryScreenOffTimeBase.writeToParcel(out, uSecUptime, uSecRealtime);
        this.mScreenOnTimer.writeToParcel(out, uSecRealtime);
        this.mScreenDozeTimer.writeToParcel(out, uSecRealtime);
        for (int i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i].writeToParcel(out, uSecRealtime);
        }
        this.mInteractiveTimer.writeToParcel(out, uSecRealtime);
        this.mPowerSaveModeEnabledTimer.writeToParcel(out, uSecRealtime);
        out.writeLong(this.mLongestLightIdleTime);
        out.writeLong(this.mLongestFullIdleTime);
        this.mDeviceIdleModeLightTimer.writeToParcel(out, uSecRealtime);
        this.mDeviceIdleModeFullTimer.writeToParcel(out, uSecRealtime);
        this.mDeviceLightIdlingTimer.writeToParcel(out, uSecRealtime);
        this.mDeviceIdlingTimer.writeToParcel(out, uSecRealtime);
        this.mPhoneOnTimer.writeToParcel(out, uSecRealtime);
        for (int i2 = 0; i2 < 5; i2++) {
            this.mPhoneSignalStrengthsTimer[i2].writeToParcel(out, uSecRealtime);
        }
        this.mPhoneSignalScanningTimer.writeToParcel(out, uSecRealtime);
        for (int i3 = 0; i3 < 23; i3++) {
            this.mPhoneDataConnectionsTimer[i3].writeToParcel(out, uSecRealtime);
        }
        for (int i4 = 0; i4 < 10; i4++) {
            this.mNetworkByteActivityCounters[i4].writeToParcel(out);
            this.mNetworkPacketActivityCounters[i4].writeToParcel(out);
        }
        this.mMobileRadioActiveTimer.writeToParcel(out, uSecRealtime);
        this.mMobileRadioActivePerAppTimer.writeToParcel(out, uSecRealtime);
        this.mMobileRadioActiveAdjustedTime.writeToParcel(out);
        this.mMobileRadioActiveUnknownTime.writeToParcel(out);
        this.mMobileRadioActiveUnknownCount.writeToParcel(out);
        this.mWifiMulticastWakelockTimer.writeToParcel(out, uSecRealtime);
        this.mWifiOnTimer.writeToParcel(out, uSecRealtime);
        this.mGlobalWifiRunningTimer.writeToParcel(out, uSecRealtime);
        for (int i5 = 0; i5 < 8; i5++) {
            this.mWifiStateTimer[i5].writeToParcel(out, uSecRealtime);
        }
        for (int i6 = 0; i6 < 13; i6++) {
            this.mWifiSupplStateTimer[i6].writeToParcel(out, uSecRealtime);
        }
        for (int i7 = 0; i7 < 5; i7++) {
            this.mWifiSignalStrengthsTimer[i7].writeToParcel(out, uSecRealtime);
        }
        this.mWifiActiveTimer.writeToParcel(out, uSecRealtime);
        this.mWifiActivity.writeToParcel(out, 0);
        for (int i8 = 0; i8 < 2; i8++) {
            this.mGpsSignalQualityTimer[i8].writeToParcel(out, uSecRealtime);
        }
        this.mBluetoothActivity.writeToParcel(out, 0);
        this.mModemActivity.writeToParcel(out, 0);
        out.writeInt(this.mHasWifiReporting ? 1 : 0);
        out.writeInt(this.mHasBluetoothReporting ? 1 : 0);
        out.writeInt(this.mHasModemReporting ? 1 : 0);
        out.writeInt(this.mNumConnectivityChange);
        this.mFlashlightOnTimer.writeToParcel(out, uSecRealtime);
        this.mCameraOnTimer.writeToParcel(out, uSecRealtime);
        this.mBluetoothScanTimer.writeToParcel(out, uSecRealtime);
        out.writeInt(this.mDischargeUnplugLevel);
        out.writeInt(this.mDischargePlugLevel);
        out.writeInt(this.mDischargeCurrentLevel);
        out.writeInt(this.mCurrentBatteryLevel);
        out.writeInt(this.mLowDischargeAmountSinceCharge);
        out.writeInt(this.mHighDischargeAmountSinceCharge);
        out.writeInt(this.mDischargeAmountScreenOn);
        out.writeInt(this.mDischargeAmountScreenOnSinceCharge);
        out.writeInt(this.mDischargeAmountScreenOff);
        out.writeInt(this.mDischargeAmountScreenOffSinceCharge);
        out.writeInt(this.mDischargeAmountScreenDoze);
        out.writeInt(this.mDischargeAmountScreenDozeSinceCharge);
        this.mDischargeStepTracker.writeToParcel(out);
        this.mChargeStepTracker.writeToParcel(out);
        this.mDischargeCounter.writeToParcel(out);
        this.mDischargeScreenOffCounter.writeToParcel(out);
        this.mDischargeScreenDozeCounter.writeToParcel(out);
        this.mDischargeLightDozeCounter.writeToParcel(out);
        this.mDischargeDeepDozeCounter.writeToParcel(out);
        out.writeLong(this.mLastWriteTime);
        out.writeInt(this.mRpmStats.size());
        for (Map.Entry<String, SamplingTimer> ent : this.mRpmStats.entrySet()) {
            SamplingTimer rpmt = ent.getValue();
            if (rpmt != null) {
                out.writeInt(1);
                out.writeString(ent.getKey());
                rpmt.writeToParcel(out, uSecRealtime);
            } else {
                out.writeInt(0);
            }
        }
        out.writeInt(this.mScreenOffRpmStats.size());
        for (Map.Entry<String, SamplingTimer> ent2 : this.mScreenOffRpmStats.entrySet()) {
            SamplingTimer rpmt2 = ent2.getValue();
            if (rpmt2 != null) {
                out.writeInt(1);
                out.writeString(ent2.getKey());
                rpmt2.writeToParcel(out, uSecRealtime);
            } else {
                out.writeInt(0);
            }
        }
        if (inclUids) {
            out.writeInt(this.mKernelWakelockStats.size());
            for (Map.Entry<String, SamplingTimer> ent3 : this.mKernelWakelockStats.entrySet()) {
                SamplingTimer kwlt = ent3.getValue();
                if (kwlt != null) {
                    out.writeInt(1);
                    out.writeString(ent3.getKey());
                    kwlt.writeToParcel(out, uSecRealtime);
                } else {
                    out.writeInt(0);
                }
            }
            out.writeInt(this.mWakeupReasonStats.size());
            for (Map.Entry<String, SamplingTimer> ent4 : this.mWakeupReasonStats.entrySet()) {
                SamplingTimer timer = ent4.getValue();
                if (timer != null) {
                    out.writeInt(1);
                    out.writeString(ent4.getKey());
                    timer.writeToParcel(out, uSecRealtime);
                } else {
                    out.writeInt(0);
                }
            }
        } else {
            out.writeInt(0);
            out.writeInt(0);
        }
        out.writeInt(this.mKernelMemoryStats.size());
        for (int i9 = 0; i9 < this.mKernelMemoryStats.size(); i9++) {
            SamplingTimer kmt = this.mKernelMemoryStats.valueAt(i9);
            if (kmt != null) {
                out.writeInt(1);
                out.writeLong(this.mKernelMemoryStats.keyAt(i9));
                kmt.writeToParcel(out, uSecRealtime);
            } else {
                out.writeInt(0);
            }
        }
        if (inclUids) {
            int size = this.mUidStats.size();
            out.writeInt(size);
            for (int i10 = 0; i10 < size; i10++) {
                out.writeInt(this.mUidStats.keyAt(i10));
                Uid uid = this.mUidStats.valueAt(i10);
                uid.writeToParcelLocked(out, uSecUptime, uSecRealtime);
            }
            return;
        }
        out.writeInt(0);
    }

    @Override // android.os.BatteryStats
    public void prepareForDumpLocked() {
        pullPendingStateUpdatesLocked();
        getStartClockTime();
    }

    @Override // android.os.BatteryStats
    public void dumpLocked(Context context, PrintWriter pw, int flags, int reqUid, long histStart) {
        super.dumpLocked(context, pw, flags, reqUid, histStart);
        pw.print("Total cpu time reads: ");
        pw.println(this.mNumSingleUidCpuTimeReads);
        pw.print("Batched cpu time reads: ");
        pw.println(this.mNumBatchedSingleUidCpuTimeReads);
        pw.print("Batching Duration (min): ");
        pw.println((this.mClocks.uptimeMillis() - this.mCpuTimeReadsTrackingStartTime) / 60000);
        pw.print("All UID cpu time reads since the later of device start or stats reset: ");
        pw.println(this.mNumAllUidCpuTimeReads);
        pw.print("UIDs removed since the later of device start or stats reset: ");
        pw.println(this.mNumUidsRemoved);
    }
}
