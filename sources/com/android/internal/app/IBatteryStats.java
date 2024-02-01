package com.android.internal.app;

import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothActivityEnergyInfo;
import android.net.wifi.WifiActivityEnergyInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.WorkSource;
import android.os.connectivity.CellularBatteryStats;
import android.os.connectivity.GpsBatteryStats;
import android.os.connectivity.WifiBatteryStats;
import android.os.health.HealthStatsParceler;
import android.telephony.ModemActivityInfo;
import android.telephony.SignalStrength;

/* loaded from: classes3.dex */
public interface IBatteryStats extends IInterface {
    long computeBatteryTimeRemaining() throws RemoteException;

    @UnsupportedAppUsage
    long computeChargeTimeRemaining() throws RemoteException;

    @UnsupportedAppUsage
    long getAwakeTimeBattery() throws RemoteException;

    long getAwakeTimePlugged() throws RemoteException;

    CellularBatteryStats getCellularBatteryStats() throws RemoteException;

    GpsBatteryStats getGpsBatteryStats() throws RemoteException;

    @UnsupportedAppUsage
    byte[] getStatistics() throws RemoteException;

    ParcelFileDescriptor getStatisticsStream() throws RemoteException;

    WifiBatteryStats getWifiBatteryStats() throws RemoteException;

    @UnsupportedAppUsage
    boolean isCharging() throws RemoteException;

    void noteBleScanResults(WorkSource workSource, int i) throws RemoteException;

    void noteBleScanStarted(WorkSource workSource, boolean z) throws RemoteException;

    void noteBleScanStopped(WorkSource workSource, boolean z) throws RemoteException;

    void noteBluetoothControllerActivity(BluetoothActivityEnergyInfo bluetoothActivityEnergyInfo) throws RemoteException;

    void noteChangeWakelockFromSource(WorkSource workSource, int i, String str, String str2, int i2, WorkSource workSource2, int i3, String str3, String str4, int i4, boolean z) throws RemoteException;

    void noteConnectivityChanged(int i, String str) throws RemoteException;

    void noteDeviceIdleMode(int i, String str, int i2) throws RemoteException;

    void noteEvent(int i, String str, int i2) throws RemoteException;

    void noteFlashlightOff(int i) throws RemoteException;

    void noteFlashlightOn(int i) throws RemoteException;

    void noteFullWifiLockAcquired(int i) throws RemoteException;

    void noteFullWifiLockAcquiredFromSource(WorkSource workSource) throws RemoteException;

    void noteFullWifiLockReleased(int i) throws RemoteException;

    void noteFullWifiLockReleasedFromSource(WorkSource workSource) throws RemoteException;

    void noteGpsChanged(WorkSource workSource, WorkSource workSource2) throws RemoteException;

    void noteGpsSignalQuality(int i) throws RemoteException;

    void noteInteractive(boolean z) throws RemoteException;

    void noteJobFinish(String str, int i, int i2, int i3, int i4) throws RemoteException;

    void noteJobStart(String str, int i, int i2, int i3) throws RemoteException;

    void noteLongPartialWakelockFinish(String str, String str2, int i) throws RemoteException;

    void noteLongPartialWakelockFinishFromSource(String str, String str2, WorkSource workSource) throws RemoteException;

    void noteLongPartialWakelockStart(String str, String str2, int i) throws RemoteException;

    void noteLongPartialWakelockStartFromSource(String str, String str2, WorkSource workSource) throws RemoteException;

    void noteMobileRadioPowerState(int i, long j, int i2) throws RemoteException;

    void noteModemControllerActivity(ModemActivityInfo modemActivityInfo) throws RemoteException;

    void noteNetworkInterfaceType(String str, int i) throws RemoteException;

    void noteNetworkStatsEnabled() throws RemoteException;

    void notePhoneDataConnectionState(int i, boolean z, int i2) throws RemoteException;

    void notePhoneOff() throws RemoteException;

    void notePhoneOn() throws RemoteException;

    void notePhoneSignalStrength(SignalStrength signalStrength) throws RemoteException;

    void notePhoneState(int i) throws RemoteException;

    void noteResetAudio() throws RemoteException;

    void noteResetBleScan() throws RemoteException;

    void noteResetCamera() throws RemoteException;

    void noteResetFlashlight() throws RemoteException;

    void noteResetVideo() throws RemoteException;

    void noteScreenBrightness(int i) throws RemoteException;

    void noteScreenState(int i) throws RemoteException;

    void noteStartAudio(int i) throws RemoteException;

    void noteStartCamera(int i) throws RemoteException;

    void noteStartSensor(int i, int i2) throws RemoteException;

    void noteStartVideo(int i) throws RemoteException;

    void noteStartWakelock(int i, int i2, String str, String str2, int i3, boolean z) throws RemoteException;

    void noteStartWakelockFromSource(WorkSource workSource, int i, String str, String str2, int i2, boolean z) throws RemoteException;

    void noteStopAudio(int i) throws RemoteException;

    void noteStopCamera(int i) throws RemoteException;

    void noteStopSensor(int i, int i2) throws RemoteException;

    void noteStopVideo(int i) throws RemoteException;

    void noteStopWakelock(int i, int i2, String str, String str2, int i3) throws RemoteException;

    void noteStopWakelockFromSource(WorkSource workSource, int i, String str, String str2, int i2) throws RemoteException;

    void noteSyncFinish(String str, int i) throws RemoteException;

    void noteSyncStart(String str, int i) throws RemoteException;

    void noteUserActivity(int i, int i2) throws RemoteException;

    void noteVibratorOff(int i) throws RemoteException;

    void noteVibratorOn(int i, long j) throws RemoteException;

    void noteWakeUp(String str, int i) throws RemoteException;

    void noteWifiBatchedScanStartedFromSource(WorkSource workSource, int i) throws RemoteException;

    void noteWifiBatchedScanStoppedFromSource(WorkSource workSource) throws RemoteException;

    void noteWifiControllerActivity(WifiActivityEnergyInfo wifiActivityEnergyInfo) throws RemoteException;

    void noteWifiMulticastDisabled(int i) throws RemoteException;

    void noteWifiMulticastEnabled(int i) throws RemoteException;

    void noteWifiOff() throws RemoteException;

    void noteWifiOn() throws RemoteException;

    void noteWifiRadioPowerState(int i, long j, int i2) throws RemoteException;

    void noteWifiRssiChanged(int i) throws RemoteException;

    void noteWifiRunning(WorkSource workSource) throws RemoteException;

    void noteWifiRunningChanged(WorkSource workSource, WorkSource workSource2) throws RemoteException;

    void noteWifiScanStarted(int i) throws RemoteException;

    void noteWifiScanStartedFromSource(WorkSource workSource) throws RemoteException;

    void noteWifiScanStopped(int i) throws RemoteException;

    void noteWifiScanStoppedFromSource(WorkSource workSource) throws RemoteException;

    void noteWifiState(int i, String str) throws RemoteException;

    void noteWifiStopped(WorkSource workSource) throws RemoteException;

    void noteWifiSupplicantStateChanged(int i, boolean z) throws RemoteException;

    void setBatteryState(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) throws RemoteException;

    boolean setChargingStateUpdateDelayMillis(int i) throws RemoteException;

    HealthStatsParceler takeUidSnapshot(int i) throws RemoteException;

    HealthStatsParceler[] takeUidSnapshots(int[] iArr) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IBatteryStats {
        @Override // com.android.internal.app.IBatteryStats
        public void noteStartSensor(int uid, int sensor) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStopSensor(int uid, int sensor) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStartVideo(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStopVideo(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStartAudio(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStopAudio(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteResetVideo() throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteResetAudio() throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteFlashlightOn(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteFlashlightOff(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStartCamera(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStopCamera(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteResetCamera() throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteResetFlashlight() throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public byte[] getStatistics() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.IBatteryStats
        public ParcelFileDescriptor getStatisticsStream() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.IBatteryStats
        public boolean isCharging() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.app.IBatteryStats
        public long computeBatteryTimeRemaining() throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.app.IBatteryStats
        public long computeChargeTimeRemaining() throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteEvent(int code, String name, int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteSyncStart(String name, int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteSyncFinish(String name, int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteJobStart(String name, int uid, int standbyBucket, int jobid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteJobFinish(String name, int uid, int stopReason, int standbyBucket, int jobid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStartWakelock(int uid, int pid, String name, String historyName, int type, boolean unimportantForLogging) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStopWakelock(int uid, int pid, String name, String historyName, int type) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStartWakelockFromSource(WorkSource ws, int pid, String name, String historyName, int type, boolean unimportantForLogging) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteChangeWakelockFromSource(WorkSource ws, int pid, String name, String histyoryName, int type, WorkSource newWs, int newPid, String newName, String newHistoryName, int newType, boolean newUnimportantForLogging) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteStopWakelockFromSource(WorkSource ws, int pid, String name, String historyName, int type) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteLongPartialWakelockStart(String name, String historyName, int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteLongPartialWakelockStartFromSource(String name, String historyName, WorkSource workSource) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteLongPartialWakelockFinish(String name, String historyName, int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteLongPartialWakelockFinishFromSource(String name, String historyName, WorkSource workSource) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteVibratorOn(int uid, long durationMillis) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteVibratorOff(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteGpsChanged(WorkSource oldSource, WorkSource newSource) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteGpsSignalQuality(int signalLevel) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteScreenState(int state) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteScreenBrightness(int brightness) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteUserActivity(int uid, int event) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWakeUp(String reason, int reasonUid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteInteractive(boolean interactive) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteConnectivityChanged(int type, String extra) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteMobileRadioPowerState(int powerState, long timestampNs, int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void notePhoneOn() throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void notePhoneOff() throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void notePhoneSignalStrength(SignalStrength signalStrength) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void notePhoneDataConnectionState(int dataType, boolean hasData, int serviceType) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void notePhoneState(int phoneState) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiOn() throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiOff() throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiRunning(WorkSource ws) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiRunningChanged(WorkSource oldWs, WorkSource newWs) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiStopped(WorkSource ws) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiState(int wifiState, String accessPoint) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiSupplicantStateChanged(int supplState, boolean failedAuth) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiRssiChanged(int newRssi) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteFullWifiLockAcquired(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteFullWifiLockReleased(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiScanStarted(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiScanStopped(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiMulticastEnabled(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiMulticastDisabled(int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteFullWifiLockAcquiredFromSource(WorkSource ws) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteFullWifiLockReleasedFromSource(WorkSource ws) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiScanStartedFromSource(WorkSource ws) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiScanStoppedFromSource(WorkSource ws) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiBatchedScanStartedFromSource(WorkSource ws, int csph) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiBatchedScanStoppedFromSource(WorkSource ws) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiRadioPowerState(int powerState, long timestampNs, int uid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteNetworkInterfaceType(String iface, int type) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteNetworkStatsEnabled() throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteDeviceIdleMode(int mode, String activeReason, int activeUid) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void setBatteryState(int status, int health, int plugType, int level, int temp, int volt, int chargeUAh, int chargeFullUAh) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public long getAwakeTimeBattery() throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.app.IBatteryStats
        public long getAwakeTimePlugged() throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteBleScanStarted(WorkSource ws, boolean isUnoptimized) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteBleScanStopped(WorkSource ws, boolean isUnoptimized) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteResetBleScan() throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteBleScanResults(WorkSource ws, int numNewResults) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public CellularBatteryStats getCellularBatteryStats() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.IBatteryStats
        public WifiBatteryStats getWifiBatteryStats() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.IBatteryStats
        public GpsBatteryStats getGpsBatteryStats() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.IBatteryStats
        public HealthStatsParceler takeUidSnapshot(int uid) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.IBatteryStats
        public HealthStatsParceler[] takeUidSnapshots(int[] uid) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteBluetoothControllerActivity(BluetoothActivityEnergyInfo info) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteModemControllerActivity(ModemActivityInfo info) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public void noteWifiControllerActivity(WifiActivityEnergyInfo info) throws RemoteException {
        }

        @Override // com.android.internal.app.IBatteryStats
        public boolean setChargingStateUpdateDelayMillis(int delay) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IBatteryStats {
        private static final String DESCRIPTOR = "com.android.internal.app.IBatteryStats";
        static final int TRANSACTION_computeBatteryTimeRemaining = 18;
        static final int TRANSACTION_computeChargeTimeRemaining = 19;
        static final int TRANSACTION_getAwakeTimeBattery = 75;
        static final int TRANSACTION_getAwakeTimePlugged = 76;
        static final int TRANSACTION_getCellularBatteryStats = 81;
        static final int TRANSACTION_getGpsBatteryStats = 83;
        static final int TRANSACTION_getStatistics = 15;
        static final int TRANSACTION_getStatisticsStream = 16;
        static final int TRANSACTION_getWifiBatteryStats = 82;
        static final int TRANSACTION_isCharging = 17;
        static final int TRANSACTION_noteBleScanResults = 80;
        static final int TRANSACTION_noteBleScanStarted = 77;
        static final int TRANSACTION_noteBleScanStopped = 78;
        static final int TRANSACTION_noteBluetoothControllerActivity = 86;
        static final int TRANSACTION_noteChangeWakelockFromSource = 28;
        static final int TRANSACTION_noteConnectivityChanged = 43;
        static final int TRANSACTION_noteDeviceIdleMode = 73;
        static final int TRANSACTION_noteEvent = 20;
        static final int TRANSACTION_noteFlashlightOff = 10;
        static final int TRANSACTION_noteFlashlightOn = 9;
        static final int TRANSACTION_noteFullWifiLockAcquired = 58;
        static final int TRANSACTION_noteFullWifiLockAcquiredFromSource = 64;
        static final int TRANSACTION_noteFullWifiLockReleased = 59;
        static final int TRANSACTION_noteFullWifiLockReleasedFromSource = 65;
        static final int TRANSACTION_noteGpsChanged = 36;
        static final int TRANSACTION_noteGpsSignalQuality = 37;
        static final int TRANSACTION_noteInteractive = 42;
        static final int TRANSACTION_noteJobFinish = 24;
        static final int TRANSACTION_noteJobStart = 23;
        static final int TRANSACTION_noteLongPartialWakelockFinish = 32;
        static final int TRANSACTION_noteLongPartialWakelockFinishFromSource = 33;
        static final int TRANSACTION_noteLongPartialWakelockStart = 30;
        static final int TRANSACTION_noteLongPartialWakelockStartFromSource = 31;
        static final int TRANSACTION_noteMobileRadioPowerState = 44;
        static final int TRANSACTION_noteModemControllerActivity = 87;
        static final int TRANSACTION_noteNetworkInterfaceType = 71;
        static final int TRANSACTION_noteNetworkStatsEnabled = 72;
        static final int TRANSACTION_notePhoneDataConnectionState = 48;
        static final int TRANSACTION_notePhoneOff = 46;
        static final int TRANSACTION_notePhoneOn = 45;
        static final int TRANSACTION_notePhoneSignalStrength = 47;
        static final int TRANSACTION_notePhoneState = 49;
        static final int TRANSACTION_noteResetAudio = 8;
        static final int TRANSACTION_noteResetBleScan = 79;
        static final int TRANSACTION_noteResetCamera = 13;
        static final int TRANSACTION_noteResetFlashlight = 14;
        static final int TRANSACTION_noteResetVideo = 7;
        static final int TRANSACTION_noteScreenBrightness = 39;
        static final int TRANSACTION_noteScreenState = 38;
        static final int TRANSACTION_noteStartAudio = 5;
        static final int TRANSACTION_noteStartCamera = 11;
        static final int TRANSACTION_noteStartSensor = 1;
        static final int TRANSACTION_noteStartVideo = 3;
        static final int TRANSACTION_noteStartWakelock = 25;
        static final int TRANSACTION_noteStartWakelockFromSource = 27;
        static final int TRANSACTION_noteStopAudio = 6;
        static final int TRANSACTION_noteStopCamera = 12;
        static final int TRANSACTION_noteStopSensor = 2;
        static final int TRANSACTION_noteStopVideo = 4;
        static final int TRANSACTION_noteStopWakelock = 26;
        static final int TRANSACTION_noteStopWakelockFromSource = 29;
        static final int TRANSACTION_noteSyncFinish = 22;
        static final int TRANSACTION_noteSyncStart = 21;
        static final int TRANSACTION_noteUserActivity = 40;
        static final int TRANSACTION_noteVibratorOff = 35;
        static final int TRANSACTION_noteVibratorOn = 34;
        static final int TRANSACTION_noteWakeUp = 41;
        static final int TRANSACTION_noteWifiBatchedScanStartedFromSource = 68;
        static final int TRANSACTION_noteWifiBatchedScanStoppedFromSource = 69;
        static final int TRANSACTION_noteWifiControllerActivity = 88;
        static final int TRANSACTION_noteWifiMulticastDisabled = 63;
        static final int TRANSACTION_noteWifiMulticastEnabled = 62;
        static final int TRANSACTION_noteWifiOff = 51;
        static final int TRANSACTION_noteWifiOn = 50;
        static final int TRANSACTION_noteWifiRadioPowerState = 70;
        static final int TRANSACTION_noteWifiRssiChanged = 57;
        static final int TRANSACTION_noteWifiRunning = 52;
        static final int TRANSACTION_noteWifiRunningChanged = 53;
        static final int TRANSACTION_noteWifiScanStarted = 60;
        static final int TRANSACTION_noteWifiScanStartedFromSource = 66;
        static final int TRANSACTION_noteWifiScanStopped = 61;
        static final int TRANSACTION_noteWifiScanStoppedFromSource = 67;
        static final int TRANSACTION_noteWifiState = 55;
        static final int TRANSACTION_noteWifiStopped = 54;
        static final int TRANSACTION_noteWifiSupplicantStateChanged = 56;
        static final int TRANSACTION_setBatteryState = 74;
        static final int TRANSACTION_setChargingStateUpdateDelayMillis = 89;
        static final int TRANSACTION_takeUidSnapshot = 84;
        static final int TRANSACTION_takeUidSnapshots = 85;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBatteryStats asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBatteryStats)) {
                return (IBatteryStats) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "noteStartSensor";
                case 2:
                    return "noteStopSensor";
                case 3:
                    return "noteStartVideo";
                case 4:
                    return "noteStopVideo";
                case 5:
                    return "noteStartAudio";
                case 6:
                    return "noteStopAudio";
                case 7:
                    return "noteResetVideo";
                case 8:
                    return "noteResetAudio";
                case 9:
                    return "noteFlashlightOn";
                case 10:
                    return "noteFlashlightOff";
                case 11:
                    return "noteStartCamera";
                case 12:
                    return "noteStopCamera";
                case 13:
                    return "noteResetCamera";
                case 14:
                    return "noteResetFlashlight";
                case 15:
                    return "getStatistics";
                case 16:
                    return "getStatisticsStream";
                case 17:
                    return "isCharging";
                case 18:
                    return "computeBatteryTimeRemaining";
                case 19:
                    return "computeChargeTimeRemaining";
                case 20:
                    return "noteEvent";
                case 21:
                    return "noteSyncStart";
                case 22:
                    return "noteSyncFinish";
                case 23:
                    return "noteJobStart";
                case 24:
                    return "noteJobFinish";
                case 25:
                    return "noteStartWakelock";
                case 26:
                    return "noteStopWakelock";
                case 27:
                    return "noteStartWakelockFromSource";
                case 28:
                    return "noteChangeWakelockFromSource";
                case 29:
                    return "noteStopWakelockFromSource";
                case 30:
                    return "noteLongPartialWakelockStart";
                case 31:
                    return "noteLongPartialWakelockStartFromSource";
                case 32:
                    return "noteLongPartialWakelockFinish";
                case 33:
                    return "noteLongPartialWakelockFinishFromSource";
                case 34:
                    return "noteVibratorOn";
                case 35:
                    return "noteVibratorOff";
                case 36:
                    return "noteGpsChanged";
                case 37:
                    return "noteGpsSignalQuality";
                case 38:
                    return "noteScreenState";
                case 39:
                    return "noteScreenBrightness";
                case 40:
                    return "noteUserActivity";
                case 41:
                    return "noteWakeUp";
                case 42:
                    return "noteInteractive";
                case 43:
                    return "noteConnectivityChanged";
                case 44:
                    return "noteMobileRadioPowerState";
                case 45:
                    return "notePhoneOn";
                case 46:
                    return "notePhoneOff";
                case 47:
                    return "notePhoneSignalStrength";
                case 48:
                    return "notePhoneDataConnectionState";
                case 49:
                    return "notePhoneState";
                case 50:
                    return "noteWifiOn";
                case 51:
                    return "noteWifiOff";
                case 52:
                    return "noteWifiRunning";
                case 53:
                    return "noteWifiRunningChanged";
                case 54:
                    return "noteWifiStopped";
                case 55:
                    return "noteWifiState";
                case 56:
                    return "noteWifiSupplicantStateChanged";
                case 57:
                    return "noteWifiRssiChanged";
                case 58:
                    return "noteFullWifiLockAcquired";
                case 59:
                    return "noteFullWifiLockReleased";
                case 60:
                    return "noteWifiScanStarted";
                case 61:
                    return "noteWifiScanStopped";
                case 62:
                    return "noteWifiMulticastEnabled";
                case 63:
                    return "noteWifiMulticastDisabled";
                case 64:
                    return "noteFullWifiLockAcquiredFromSource";
                case 65:
                    return "noteFullWifiLockReleasedFromSource";
                case 66:
                    return "noteWifiScanStartedFromSource";
                case 67:
                    return "noteWifiScanStoppedFromSource";
                case 68:
                    return "noteWifiBatchedScanStartedFromSource";
                case 69:
                    return "noteWifiBatchedScanStoppedFromSource";
                case 70:
                    return "noteWifiRadioPowerState";
                case 71:
                    return "noteNetworkInterfaceType";
                case 72:
                    return "noteNetworkStatsEnabled";
                case 73:
                    return "noteDeviceIdleMode";
                case 74:
                    return "setBatteryState";
                case 75:
                    return "getAwakeTimeBattery";
                case 76:
                    return "getAwakeTimePlugged";
                case 77:
                    return "noteBleScanStarted";
                case 78:
                    return "noteBleScanStopped";
                case 79:
                    return "noteResetBleScan";
                case 80:
                    return "noteBleScanResults";
                case 81:
                    return "getCellularBatteryStats";
                case 82:
                    return "getWifiBatteryStats";
                case 83:
                    return "getGpsBatteryStats";
                case 84:
                    return "takeUidSnapshot";
                case 85:
                    return "takeUidSnapshots";
                case 86:
                    return "noteBluetoothControllerActivity";
                case 87:
                    return "noteModemControllerActivity";
                case 88:
                    return "noteWifiControllerActivity";
                case 89:
                    return "setChargingStateUpdateDelayMillis";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            WorkSource _arg0;
            WorkSource _arg02;
            WorkSource _arg5;
            WorkSource _arg03;
            WorkSource _arg2;
            WorkSource _arg22;
            WorkSource _arg04;
            WorkSource _arg1;
            boolean _arg12;
            SignalStrength _arg05;
            WorkSource _arg06;
            WorkSource _arg07;
            WorkSource _arg13;
            WorkSource _arg08;
            WorkSource _arg09;
            WorkSource _arg010;
            WorkSource _arg011;
            WorkSource _arg012;
            WorkSource _arg013;
            WorkSource _arg014;
            WorkSource _arg015;
            WorkSource _arg016;
            WorkSource _arg017;
            BluetoothActivityEnergyInfo _arg018;
            ModemActivityInfo _arg019;
            WifiActivityEnergyInfo _arg020;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    noteStartSensor(_arg021, data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    noteStopSensor(_arg022, data.readInt());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    noteStartVideo(_arg023);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    noteStopVideo(_arg024);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    noteStartAudio(_arg025);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    noteStopAudio(_arg026);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    noteResetVideo();
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    noteResetAudio();
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg027 = data.readInt();
                    noteFlashlightOn(_arg027);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg028 = data.readInt();
                    noteFlashlightOff(_arg028);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg029 = data.readInt();
                    noteStartCamera(_arg029);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg030 = data.readInt();
                    noteStopCamera(_arg030);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    noteResetCamera();
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    noteResetFlashlight();
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _result = getStatistics();
                    reply.writeNoException();
                    reply.writeByteArray(_result);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _result2 = getStatisticsStream();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isCharging = isCharging();
                    reply.writeNoException();
                    reply.writeInt(isCharging ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    long _result3 = computeBatteryTimeRemaining();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    long _result4 = computeChargeTimeRemaining();
                    reply.writeNoException();
                    reply.writeLong(_result4);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg031 = data.readInt();
                    String _arg14 = data.readString();
                    int _arg23 = data.readInt();
                    noteEvent(_arg031, _arg14, _arg23);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    noteSyncStart(_arg032, data.readInt());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    noteSyncFinish(_arg033, data.readInt());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg034 = data.readString();
                    int _arg15 = data.readInt();
                    int _arg24 = data.readInt();
                    int _arg3 = data.readInt();
                    noteJobStart(_arg034, _arg15, _arg24, _arg3);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg035 = data.readString();
                    int _arg16 = data.readInt();
                    int _arg25 = data.readInt();
                    int _arg32 = data.readInt();
                    int _arg4 = data.readInt();
                    noteJobFinish(_arg035, _arg16, _arg25, _arg32, _arg4);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    int _arg17 = data.readInt();
                    String _arg26 = data.readString();
                    String _arg33 = data.readString();
                    int _arg42 = data.readInt();
                    boolean _arg52 = data.readInt() != 0;
                    noteStartWakelock(_arg036, _arg17, _arg26, _arg33, _arg42, _arg52);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    int _arg18 = data.readInt();
                    String _arg27 = data.readString();
                    String _arg34 = data.readString();
                    int _arg43 = data.readInt();
                    noteStopWakelock(_arg037, _arg18, _arg27, _arg34, _arg43);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int _arg19 = data.readInt();
                    String _arg28 = data.readString();
                    String _arg35 = data.readString();
                    int _arg44 = data.readInt();
                    boolean _arg53 = data.readInt() != 0;
                    noteStartWakelockFromSource(_arg0, _arg19, _arg28, _arg35, _arg44, _arg53);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    int _arg110 = data.readInt();
                    String _arg29 = data.readString();
                    String _arg36 = data.readString();
                    int _arg45 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg5 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    int _arg6 = data.readInt();
                    String _arg7 = data.readString();
                    String _arg8 = data.readString();
                    int _arg9 = data.readInt();
                    boolean _arg10 = data.readInt() != 0;
                    noteChangeWakelockFromSource(_arg02, _arg110, _arg29, _arg36, _arg45, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    int _arg111 = data.readInt();
                    String _arg210 = data.readString();
                    String _arg37 = data.readString();
                    int _arg46 = data.readInt();
                    noteStopWakelockFromSource(_arg03, _arg111, _arg210, _arg37, _arg46);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg038 = data.readString();
                    String _arg112 = data.readString();
                    int _arg211 = data.readInt();
                    noteLongPartialWakelockStart(_arg038, _arg112, _arg211);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg039 = data.readString();
                    String _arg113 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    noteLongPartialWakelockStartFromSource(_arg039, _arg113, _arg2);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg040 = data.readString();
                    String _arg114 = data.readString();
                    int _arg212 = data.readInt();
                    noteLongPartialWakelockFinish(_arg040, _arg114, _arg212);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg041 = data.readString();
                    String _arg115 = data.readString();
                    if (data.readInt() != 0) {
                        _arg22 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    noteLongPartialWakelockFinishFromSource(_arg041, _arg115, _arg22);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg042 = data.readInt();
                    noteVibratorOn(_arg042, data.readLong());
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg043 = data.readInt();
                    noteVibratorOff(_arg043);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    noteGpsChanged(_arg04, _arg1);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg044 = data.readInt();
                    noteGpsSignalQuality(_arg044);
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg045 = data.readInt();
                    noteScreenState(_arg045);
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg046 = data.readInt();
                    noteScreenBrightness(_arg046);
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg047 = data.readInt();
                    noteUserActivity(_arg047, data.readInt());
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg048 = data.readString();
                    noteWakeUp(_arg048, data.readInt());
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    noteInteractive(_arg12);
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg049 = data.readInt();
                    noteConnectivityChanged(_arg049, data.readString());
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg050 = data.readInt();
                    long _arg116 = data.readLong();
                    int _arg213 = data.readInt();
                    noteMobileRadioPowerState(_arg050, _arg116, _arg213);
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    notePhoneOn();
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    notePhoneOff();
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = SignalStrength.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    notePhoneSignalStrength(_arg05);
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg051 = data.readInt();
                    _arg12 = data.readInt() != 0;
                    int _arg214 = data.readInt();
                    notePhoneDataConnectionState(_arg051, _arg12, _arg214);
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg052 = data.readInt();
                    notePhoneState(_arg052);
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    noteWifiOn();
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    noteWifiOff();
                    reply.writeNoException();
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    noteWifiRunning(_arg06);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    noteWifiRunningChanged(_arg07, _arg13);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    noteWifiStopped(_arg08);
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg053 = data.readInt();
                    noteWifiState(_arg053, data.readString());
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg054 = data.readInt();
                    _arg12 = data.readInt() != 0;
                    noteWifiSupplicantStateChanged(_arg054, _arg12);
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg055 = data.readInt();
                    noteWifiRssiChanged(_arg055);
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg056 = data.readInt();
                    noteFullWifiLockAcquired(_arg056);
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg057 = data.readInt();
                    noteFullWifiLockReleased(_arg057);
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg058 = data.readInt();
                    noteWifiScanStarted(_arg058);
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg059 = data.readInt();
                    noteWifiScanStopped(_arg059);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg060 = data.readInt();
                    noteWifiMulticastEnabled(_arg060);
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg061 = data.readInt();
                    noteWifiMulticastDisabled(_arg061);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    noteFullWifiLockAcquiredFromSource(_arg09);
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    noteFullWifiLockReleasedFromSource(_arg010);
                    reply.writeNoException();
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg011 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg011 = null;
                    }
                    noteWifiScanStartedFromSource(_arg011);
                    reply.writeNoException();
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg012 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg012 = null;
                    }
                    noteWifiScanStoppedFromSource(_arg012);
                    reply.writeNoException();
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg013 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg013 = null;
                    }
                    noteWifiBatchedScanStartedFromSource(_arg013, data.readInt());
                    reply.writeNoException();
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg014 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg014 = null;
                    }
                    noteWifiBatchedScanStoppedFromSource(_arg014);
                    reply.writeNoException();
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg062 = data.readInt();
                    long _arg117 = data.readLong();
                    int _arg215 = data.readInt();
                    noteWifiRadioPowerState(_arg062, _arg117, _arg215);
                    reply.writeNoException();
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg063 = data.readString();
                    noteNetworkInterfaceType(_arg063, data.readInt());
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    noteNetworkStatsEnabled();
                    reply.writeNoException();
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg064 = data.readInt();
                    String _arg118 = data.readString();
                    int _arg216 = data.readInt();
                    noteDeviceIdleMode(_arg064, _arg118, _arg216);
                    reply.writeNoException();
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg065 = data.readInt();
                    int _arg119 = data.readInt();
                    int _arg217 = data.readInt();
                    int _arg38 = data.readInt();
                    int _arg47 = data.readInt();
                    int _arg54 = data.readInt();
                    int _arg62 = data.readInt();
                    int _arg72 = data.readInt();
                    setBatteryState(_arg065, _arg119, _arg217, _arg38, _arg47, _arg54, _arg62, _arg72);
                    reply.writeNoException();
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    long _result5 = getAwakeTimeBattery();
                    reply.writeNoException();
                    reply.writeLong(_result5);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    long _result6 = getAwakeTimePlugged();
                    reply.writeNoException();
                    reply.writeLong(_result6);
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg015 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg015 = null;
                    }
                    _arg12 = data.readInt() != 0;
                    noteBleScanStarted(_arg015, _arg12);
                    reply.writeNoException();
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg016 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg016 = null;
                    }
                    _arg12 = data.readInt() != 0;
                    noteBleScanStopped(_arg016, _arg12);
                    reply.writeNoException();
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    noteResetBleScan();
                    reply.writeNoException();
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg017 = WorkSource.CREATOR.createFromParcel(data);
                    } else {
                        _arg017 = null;
                    }
                    noteBleScanResults(_arg017, data.readInt());
                    reply.writeNoException();
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    CellularBatteryStats _result7 = getCellularBatteryStats();
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    WifiBatteryStats _result8 = getWifiBatteryStats();
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    GpsBatteryStats _result9 = getGpsBatteryStats();
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg066 = data.readInt();
                    HealthStatsParceler _result10 = takeUidSnapshot(_arg066);
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg067 = data.createIntArray();
                    HealthStatsParceler[] _result11 = takeUidSnapshots(_arg067);
                    reply.writeNoException();
                    reply.writeTypedArray(_result11, 1);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg018 = BluetoothActivityEnergyInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg018 = null;
                    }
                    noteBluetoothControllerActivity(_arg018);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg019 = ModemActivityInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg019 = null;
                    }
                    noteModemControllerActivity(_arg019);
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg020 = WifiActivityEnergyInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg020 = null;
                    }
                    noteWifiControllerActivity(_arg020);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg068 = data.readInt();
                    boolean chargingStateUpdateDelayMillis = setChargingStateUpdateDelayMillis(_arg068);
                    reply.writeNoException();
                    reply.writeInt(chargingStateUpdateDelayMillis ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IBatteryStats {
            public static IBatteryStats sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStartSensor(int uid, int sensor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(sensor);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteStartSensor(uid, sensor);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStopSensor(int uid, int sensor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(sensor);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteStopSensor(uid, sensor);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStartVideo(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteStartVideo(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStopVideo(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteStopVideo(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStartAudio(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteStartAudio(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStopAudio(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteStopAudio(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteResetVideo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteResetVideo();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteResetAudio() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteResetAudio();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteFlashlightOn(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteFlashlightOn(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteFlashlightOff(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteFlashlightOff(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStartCamera(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteStartCamera(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStopCamera(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteStopCamera(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteResetCamera() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteResetCamera();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteResetFlashlight() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteResetFlashlight();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public byte[] getStatistics() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStatistics();
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public ParcelFileDescriptor getStatisticsStream() throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStatisticsStream();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public boolean isCharging() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCharging();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public long computeBatteryTimeRemaining() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().computeBatteryTimeRemaining();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public long computeChargeTimeRemaining() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().computeChargeTimeRemaining();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteEvent(int code, String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteEvent(code, name, uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteSyncStart(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteSyncStart(name, uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteSyncFinish(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteSyncFinish(name, uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteJobStart(String name, int uid, int standbyBucket, int jobid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    _data.writeInt(standbyBucket);
                    _data.writeInt(jobid);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteJobStart(name, uid, standbyBucket, jobid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteJobFinish(String name, int uid, int stopReason, int standbyBucket, int jobid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    _data.writeInt(stopReason);
                    _data.writeInt(standbyBucket);
                    _data.writeInt(jobid);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteJobFinish(name, uid, stopReason, standbyBucket, jobid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStartWakelock(int uid, int pid, String name, String historyName, int type, boolean unimportantForLogging) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(uid);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(pid);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(name);
                    try {
                        _data.writeString(historyName);
                        try {
                            _data.writeInt(type);
                            _data.writeInt(unimportantForLogging ? 1 : 0);
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().noteStartWakelock(uid, pid, name, historyName, type, unimportantForLogging);
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th6) {
                        th = th6;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStopWakelock(int uid, int pid, String name, String historyName, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteStopWakelock(uid, pid, name, historyName, type);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStartWakelockFromSource(WorkSource ws, int pid, String name, String historyName, int type, boolean unimportantForLogging) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(pid);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(name);
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(historyName);
                        try {
                            _data.writeInt(type);
                            if (!unimportantForLogging) {
                                i = 0;
                            }
                            _data.writeInt(i);
                            try {
                                boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    Stub.getDefaultImpl().noteStartWakelockFromSource(ws, pid, name, historyName, type, unimportantForLogging);
                                    _reply.recycle();
                                    _data.recycle();
                                    return;
                                }
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                            } catch (Throwable th3) {
                                th = th3;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteChangeWakelockFromSource(WorkSource ws, int pid, String name, String histyoryName, int type, WorkSource newWs, int newPid, String newName, String newHistoryName, int newType, boolean newUnimportantForLogging) throws RemoteException {
                Parcel _reply;
                boolean _status;
                Parcel _data = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (ws != null) {
                        try {
                            _data.writeInt(1);
                            ws.writeToParcel(_data, 0);
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(pid);
                    _data.writeString(name);
                    _data.writeString(histyoryName);
                    _data.writeInt(type);
                    if (newWs != null) {
                        _data.writeInt(1);
                        newWs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(newPid);
                    _data.writeString(newName);
                    _data.writeString(newHistoryName);
                    _data.writeInt(newType);
                    if (!newUnimportantForLogging) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _status = this.mRemote.transact(28, _data, _reply2, 0);
                } catch (Throwable th2) {
                    th = th2;
                    _reply = _reply2;
                }
                try {
                    if (_status || Stub.getDefaultImpl() == null) {
                        _reply2.readException();
                        _reply2.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteChangeWakelockFromSource(ws, pid, name, histyoryName, type, newWs, newPid, newName, newHistoryName, newType, newUnimportantForLogging);
                    _reply2.recycle();
                    _data.recycle();
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteStopWakelockFromSource(WorkSource ws, int pid, String name, String historyName, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(pid);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteStopWakelockFromSource(ws, pid, name, historyName, type);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteLongPartialWakelockStart(String name, String historyName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteLongPartialWakelockStart(name, historyName, uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteLongPartialWakelockStartFromSource(String name, String historyName, WorkSource workSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteLongPartialWakelockStartFromSource(name, historyName, workSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteLongPartialWakelockFinish(String name, String historyName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteLongPartialWakelockFinish(name, historyName, uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteLongPartialWakelockFinishFromSource(String name, String historyName, WorkSource workSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteLongPartialWakelockFinishFromSource(name, historyName, workSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteVibratorOn(int uid, long durationMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeLong(durationMillis);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteVibratorOn(uid, durationMillis);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteVibratorOff(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteVibratorOff(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteGpsChanged(WorkSource oldSource, WorkSource newSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (oldSource != null) {
                        _data.writeInt(1);
                        oldSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (newSource != null) {
                        _data.writeInt(1);
                        newSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteGpsChanged(oldSource, newSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteGpsSignalQuality(int signalLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(signalLevel);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteGpsSignalQuality(signalLevel);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteScreenState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteScreenState(state);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteScreenBrightness(int brightness) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(brightness);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteScreenBrightness(brightness);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteUserActivity(int uid, int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(event);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteUserActivity(uid, event);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWakeUp(String reason, int reasonUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    _data.writeInt(reasonUid);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWakeUp(reason, reasonUid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteInteractive(boolean interactive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(interactive ? 1 : 0);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteInteractive(interactive);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteConnectivityChanged(int type, String extra) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(extra);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteConnectivityChanged(type, extra);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteMobileRadioPowerState(int powerState, long timestampNs, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(powerState);
                    _data.writeLong(timestampNs);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteMobileRadioPowerState(powerState, timestampNs, uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void notePhoneOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notePhoneOn();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void notePhoneOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notePhoneOff();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void notePhoneSignalStrength(SignalStrength signalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (signalStrength != null) {
                        _data.writeInt(1);
                        signalStrength.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notePhoneSignalStrength(signalStrength);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void notePhoneDataConnectionState(int dataType, boolean hasData, int serviceType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(dataType);
                    _data.writeInt(hasData ? 1 : 0);
                    _data.writeInt(serviceType);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notePhoneDataConnectionState(dataType, hasData, serviceType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void notePhoneState(int phoneState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneState);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notePhoneState(phoneState);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiOn();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiOff();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiRunning(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiRunning(ws);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiRunningChanged(WorkSource oldWs, WorkSource newWs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (oldWs != null) {
                        _data.writeInt(1);
                        oldWs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (newWs != null) {
                        _data.writeInt(1);
                        newWs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiRunningChanged(oldWs, newWs);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiStopped(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiStopped(ws);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiState(int wifiState, String accessPoint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(wifiState);
                    _data.writeString(accessPoint);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiState(wifiState, accessPoint);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiSupplicantStateChanged(int supplState, boolean failedAuth) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(supplState);
                    _data.writeInt(failedAuth ? 1 : 0);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiSupplicantStateChanged(supplState, failedAuth);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiRssiChanged(int newRssi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newRssi);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiRssiChanged(newRssi);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteFullWifiLockAcquired(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteFullWifiLockAcquired(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteFullWifiLockReleased(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteFullWifiLockReleased(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiScanStarted(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiScanStarted(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiScanStopped(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiScanStopped(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiMulticastEnabled(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiMulticastEnabled(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiMulticastDisabled(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiMulticastDisabled(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteFullWifiLockAcquiredFromSource(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteFullWifiLockAcquiredFromSource(ws);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteFullWifiLockReleasedFromSource(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteFullWifiLockReleasedFromSource(ws);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiScanStartedFromSource(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiScanStartedFromSource(ws);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiScanStoppedFromSource(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiScanStoppedFromSource(ws);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiBatchedScanStartedFromSource(WorkSource ws, int csph) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(csph);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiBatchedScanStartedFromSource(ws, csph);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiBatchedScanStoppedFromSource(WorkSource ws) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiBatchedScanStoppedFromSource(ws);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiRadioPowerState(int powerState, long timestampNs, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(powerState);
                    _data.writeLong(timestampNs);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiRadioPowerState(powerState, timestampNs, uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteNetworkInterfaceType(String iface, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteNetworkInterfaceType(iface, type);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteNetworkStatsEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteNetworkStatsEnabled();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteDeviceIdleMode(int mode, String activeReason, int activeUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeString(activeReason);
                    _data.writeInt(activeUid);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteDeviceIdleMode(mode, activeReason, activeUid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void setBatteryState(int status, int health, int plugType, int level, int temp, int volt, int chargeUAh, int chargeFullUAh) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(status);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(health);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(plugType);
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(level);
                    _data.writeInt(temp);
                    _data.writeInt(volt);
                    _data.writeInt(chargeUAh);
                    _data.writeInt(chargeFullUAh);
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBatteryState(status, health, plugType, level, temp, volt, chargeUAh, chargeFullUAh);
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public long getAwakeTimeBattery() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAwakeTimeBattery();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public long getAwakeTimePlugged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAwakeTimePlugged();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteBleScanStarted(WorkSource ws, boolean isUnoptimized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!isUnoptimized) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteBleScanStarted(ws, isUnoptimized);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteBleScanStopped(WorkSource ws, boolean isUnoptimized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!isUnoptimized) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteBleScanStopped(ws, isUnoptimized);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteResetBleScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteResetBleScan();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteBleScanResults(WorkSource ws, int numNewResults) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ws != null) {
                        _data.writeInt(1);
                        ws.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(numNewResults);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteBleScanResults(ws, numNewResults);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public CellularBatteryStats getCellularBatteryStats() throws RemoteException {
                CellularBatteryStats _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCellularBatteryStats();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = CellularBatteryStats.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public WifiBatteryStats getWifiBatteryStats() throws RemoteException {
                WifiBatteryStats _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getWifiBatteryStats();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = WifiBatteryStats.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public GpsBatteryStats getGpsBatteryStats() throws RemoteException {
                GpsBatteryStats _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGpsBatteryStats();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = GpsBatteryStats.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public HealthStatsParceler takeUidSnapshot(int uid) throws RemoteException {
                HealthStatsParceler _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().takeUidSnapshot(uid);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = HealthStatsParceler.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public HealthStatsParceler[] takeUidSnapshots(int[] uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(uid);
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().takeUidSnapshots(uid);
                    }
                    _reply.readException();
                    HealthStatsParceler[] _result = (HealthStatsParceler[]) _reply.createTypedArray(HealthStatsParceler.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteBluetoothControllerActivity(BluetoothActivityEnergyInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(86, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteBluetoothControllerActivity(info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteModemControllerActivity(ModemActivityInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(87, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteModemControllerActivity(info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public void noteWifiControllerActivity(WifiActivityEnergyInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(88, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().noteWifiControllerActivity(info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public boolean setChargingStateUpdateDelayMillis(int delay) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(delay);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setChargingStateUpdateDelayMillis(delay);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBatteryStats impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBatteryStats getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
