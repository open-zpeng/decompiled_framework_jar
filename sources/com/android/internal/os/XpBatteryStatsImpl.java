package com.android.internal.os;

import android.bluetooth.BluetoothActivityEnergyInfo;
import android.net.wifi.WifiActivityEnergyInfo;
import android.os.WorkSource;
import android.telephony.ModemActivityInfo;
import android.telephony.SignalStrength;
import android.util.SparseLongArray;

/* loaded from: classes3.dex */
public class XpBatteryStatsImpl extends BatteryStatsImpl {
    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteEventLocked(int code, String name, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteCurrentTimeChangedLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteProcessStartLocked(String name, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteProcessCrashLocked(String name, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteProcessAnrLocked(String name, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteUidProcessStateLocked(int uid, int state) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteProcessFinishLocked(String name, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteSyncStartLocked(String name, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteSyncFinishLocked(String name, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteJobStartLocked(String name, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteJobFinishLocked(String name, int uid, int stopReason) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteJobsDeferredLocked(int uid, int numDeferred, long sinceLast) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteAlarmStartLocked(String name, WorkSource workSource, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteAlarmFinishLocked(String name, WorkSource workSource, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWakupAlarmLocked(String packageName, int uid, WorkSource workSource, String tag) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteStartWakeLocked(int uid, int pid, WorkSource.WorkChain wc, String name, String historyName, int type, boolean unimportantForLogging, long elapsedRealtime, long uptime) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteStopWakeLocked(int uid, int pid, WorkSource.WorkChain wc, String name, String historyName, int type, long elapsedRealtime, long uptime) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteStartWakeFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type, boolean unimportantForLogging) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteChangeWakelockFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type, WorkSource newWs, int newPid, String newName, String newHistoryName, int newType, boolean newUnimportantForLogging) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteStopWakeFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteLongPartialWakelockStart(String name, String historyName, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteLongPartialWakelockStartFromSource(String name, String historyName, WorkSource workSource) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteLongPartialWakelockFinish(String name, String historyName, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteLongPartialWakelockFinishFromSource(String name, String historyName, WorkSource workSource) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    void aggregateLastWakeupUptimeLocked(long uptimeMs) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWakeupReasonLocked(String reason) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateProcStateCpuTimes(boolean onBattery, boolean onBatteryScreenOff) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteStartSensorLocked(int uid, int sensor) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteStopSensorLocked(int uid, int sensor) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteGpsChangedLocked(WorkSource oldWs, WorkSource newWs) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteGpsSignalQualityLocked(int signalLevel) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteScreenStateLocked(int state) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteScreenBrightnessLocked(int brightness) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteUserActivityLocked(int uid, int event) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWakeUpLocked(String reason, int reasonUid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteInteractiveLocked(boolean interactive) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteConnectivityChangedLocked(int type, String extra) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public boolean noteMobileRadioPowerStateLocked(int powerState, long timestampNs, int uid) {
        return false;
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void notePowerSaveModeLocked(boolean enabled) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteDeviceIdleModeLocked(int mode, String activeReason, int activeUid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void notePackageInstalledLocked(String pkgName, long versionCode) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void notePackageUninstalledLocked(String pkgName) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteProcessDiedLocked(int uid, int pid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void notePhoneOnLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void notePhoneOffLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void notePhoneStateLocked(int state, int simState) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void notePhoneSignalStrengthLocked(SignalStrength signalStrength) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void notePhoneDataConnectionStateLocked(int dataType, boolean hasData, int serviceType) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiOnLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiOffLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteAudioOnLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteAudioOffLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteVideoOnLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteVideoOffLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteResetAudioLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteResetVideoLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteActivityResumedLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteActivityPausedLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteVibratorOnLocked(int uid, long durationMillis) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteVibratorOffLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteFlashlightOnLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteFlashlightOffLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteCameraOnLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteCameraOffLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteResetCameraLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteResetFlashlightLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteBluetoothScanStartedFromSourceLocked(WorkSource ws, boolean isUnoptimized) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteBluetoothScanStoppedFromSourceLocked(WorkSource ws, boolean isUnoptimized) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteResetBluetoothScanLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteBluetoothScanResultsFromSourceLocked(WorkSource ws, int numNewResults) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiRadioPowerState(int powerState, long timestampNs, int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiRunningLocked(WorkSource ws) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiRunningChangedLocked(WorkSource oldWs, WorkSource newWs) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiStoppedLocked(WorkSource ws) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiStateLocked(int wifiState, String accessPoint) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiSupplicantStateChangedLocked(int supplState, boolean failedAuth) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiRssiChangedLocked(int newRssi) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteFullWifiLockAcquiredLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteFullWifiLockReleasedLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiScanStartedLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiScanStoppedLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiBatchedScanStartedLocked(int uid, int csph) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiBatchedScanStoppedLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiMulticastEnabledLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiMulticastDisabledLocked(int uid) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteFullWifiLockAcquiredFromSourceLocked(WorkSource ws) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteFullWifiLockReleasedFromSourceLocked(WorkSource ws) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiScanStartedFromSourceLocked(WorkSource ws) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiScanStoppedFromSourceLocked(WorkSource ws) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiBatchedScanStartedFromSourceLocked(WorkSource ws, int csph) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteWifiBatchedScanStoppedFromSourceLocked(WorkSource ws) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void noteNetworkInterfaceTypeLocked(String iface, int networkType) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateTimeBasesLocked(boolean unplugged, int screenState, long uptime, long realtime) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateDailyDeadlineLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    void updateDischargeScreenLevelsLocked(int oldState, int newState) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateWifiState(WifiActivityEnergyInfo info) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateMobileRadioState(ModemActivityInfo activityInfo) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateBluetoothStateLocked(BluetoothActivityEnergyInfo info) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateRpmStatsLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateRailStatsLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateKernelWakelocksLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateKernelMemoryBandwidthLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateCpuTimeLocked(boolean onBattery, boolean onBatteryScreenOff) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void updateClusterSpeedTimes(SparseLongArray updatedUids, boolean onBattery) {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void postBatteryNeedsCpuUpdateMsg() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public boolean startAddingCpuLocked() {
        super.startAddingCpuLocked();
        return false;
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void writeAsyncLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    public void writeSyncLocked() {
    }

    @Override // com.android.internal.os.BatteryStatsImpl
    void writeStatsLocked(boolean sync) {
    }
}
