package com.android.internal.app;

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
    synchronized long computeBatteryTimeRemaining() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    long computeChargeTimeRemaining() throws RemoteException;

    private protected long getAwakeTimeBattery() throws RemoteException;

    synchronized long getAwakeTimePlugged() throws RemoteException;

    synchronized CellularBatteryStats getCellularBatteryStats() throws RemoteException;

    synchronized GpsBatteryStats getGpsBatteryStats() throws RemoteException;

    private protected byte[] getStatistics() throws RemoteException;

    synchronized ParcelFileDescriptor getStatisticsStream() throws RemoteException;

    synchronized WifiBatteryStats getWifiBatteryStats() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean isCharging() throws RemoteException;

    synchronized void noteBleScanResults(WorkSource workSource, int i) throws RemoteException;

    synchronized void noteBleScanStarted(WorkSource workSource, boolean z) throws RemoteException;

    synchronized void noteBleScanStopped(WorkSource workSource, boolean z) throws RemoteException;

    synchronized void noteBluetoothControllerActivity(BluetoothActivityEnergyInfo bluetoothActivityEnergyInfo) throws RemoteException;

    synchronized void noteChangeWakelockFromSource(WorkSource workSource, int i, String str, String str2, int i2, WorkSource workSource2, int i3, String str3, String str4, int i4, boolean z) throws RemoteException;

    synchronized void noteConnectivityChanged(int i, String str) throws RemoteException;

    synchronized void noteDeviceIdleMode(int i, String str, int i2) throws RemoteException;

    synchronized void noteEvent(int i, String str, int i2) throws RemoteException;

    synchronized void noteFlashlightOff(int i) throws RemoteException;

    synchronized void noteFlashlightOn(int i) throws RemoteException;

    private protected void noteFullWifiLockAcquired(int i) throws RemoteException;

    synchronized void noteFullWifiLockAcquiredFromSource(WorkSource workSource) throws RemoteException;

    private protected void noteFullWifiLockReleased(int i) throws RemoteException;

    synchronized void noteFullWifiLockReleasedFromSource(WorkSource workSource) throws RemoteException;

    synchronized void noteGpsChanged(WorkSource workSource, WorkSource workSource2) throws RemoteException;

    synchronized void noteGpsSignalQuality(int i) throws RemoteException;

    synchronized void noteInteractive(boolean z) throws RemoteException;

    synchronized void noteJobFinish(String str, int i, int i2) throws RemoteException;

    synchronized void noteJobStart(String str, int i) throws RemoteException;

    synchronized void noteLongPartialWakelockFinish(String str, String str2, int i) throws RemoteException;

    synchronized void noteLongPartialWakelockFinishFromSource(String str, String str2, WorkSource workSource) throws RemoteException;

    synchronized void noteLongPartialWakelockStart(String str, String str2, int i) throws RemoteException;

    synchronized void noteLongPartialWakelockStartFromSource(String str, String str2, WorkSource workSource) throws RemoteException;

    synchronized void noteMobileRadioPowerState(int i, long j, int i2) throws RemoteException;

    synchronized void noteModemControllerActivity(ModemActivityInfo modemActivityInfo) throws RemoteException;

    synchronized void noteNetworkInterfaceType(String str, int i) throws RemoteException;

    synchronized void noteNetworkStatsEnabled() throws RemoteException;

    private protected void notePhoneDataConnectionState(int i, boolean z) throws RemoteException;

    private protected void notePhoneOff() throws RemoteException;

    private protected void notePhoneOn() throws RemoteException;

    private protected void notePhoneSignalStrength(SignalStrength signalStrength) throws RemoteException;

    private protected void notePhoneState(int i) throws RemoteException;

    synchronized void noteResetAudio() throws RemoteException;

    synchronized void noteResetBleScan() throws RemoteException;

    synchronized void noteResetCamera() throws RemoteException;

    synchronized void noteResetFlashlight() throws RemoteException;

    synchronized void noteResetVideo() throws RemoteException;

    private protected void noteScreenBrightness(int i) throws RemoteException;

    synchronized void noteScreenState(int i) throws RemoteException;

    synchronized void noteStartAudio(int i) throws RemoteException;

    synchronized void noteStartCamera(int i) throws RemoteException;

    private protected void noteStartSensor(int i, int i2) throws RemoteException;

    synchronized void noteStartVideo(int i) throws RemoteException;

    synchronized void noteStartWakelock(int i, int i2, String str, String str2, int i3, boolean z) throws RemoteException;

    synchronized void noteStartWakelockFromSource(WorkSource workSource, int i, String str, String str2, int i2, boolean z) throws RemoteException;

    synchronized void noteStopAudio(int i) throws RemoteException;

    synchronized void noteStopCamera(int i) throws RemoteException;

    private protected void noteStopSensor(int i, int i2) throws RemoteException;

    synchronized void noteStopVideo(int i) throws RemoteException;

    synchronized void noteStopWakelock(int i, int i2, String str, String str2, int i3) throws RemoteException;

    synchronized void noteStopWakelockFromSource(WorkSource workSource, int i, String str, String str2, int i2) throws RemoteException;

    synchronized void noteSyncFinish(String str, int i) throws RemoteException;

    synchronized void noteSyncStart(String str, int i) throws RemoteException;

    private protected void noteUserActivity(int i, int i2) throws RemoteException;

    synchronized void noteVibratorOff(int i) throws RemoteException;

    synchronized void noteVibratorOn(int i, long j) throws RemoteException;

    synchronized void noteWakeUp(String str, int i) throws RemoteException;

    synchronized void noteWifiBatchedScanStartedFromSource(WorkSource workSource, int i) throws RemoteException;

    synchronized void noteWifiBatchedScanStoppedFromSource(WorkSource workSource) throws RemoteException;

    synchronized void noteWifiControllerActivity(WifiActivityEnergyInfo wifiActivityEnergyInfo) throws RemoteException;

    private protected void noteWifiMulticastDisabled(int i) throws RemoteException;

    private protected void noteWifiMulticastEnabled(int i) throws RemoteException;

    synchronized void noteWifiOff() throws RemoteException;

    synchronized void noteWifiOn() throws RemoteException;

    synchronized void noteWifiRadioPowerState(int i, long j, int i2) throws RemoteException;

    synchronized void noteWifiRssiChanged(int i) throws RemoteException;

    synchronized void noteWifiRunning(WorkSource workSource) throws RemoteException;

    synchronized void noteWifiRunningChanged(WorkSource workSource, WorkSource workSource2) throws RemoteException;

    synchronized void noteWifiScanStarted(int i) throws RemoteException;

    synchronized void noteWifiScanStartedFromSource(WorkSource workSource) throws RemoteException;

    synchronized void noteWifiScanStopped(int i) throws RemoteException;

    synchronized void noteWifiScanStoppedFromSource(WorkSource workSource) throws RemoteException;

    synchronized void noteWifiState(int i, String str) throws RemoteException;

    synchronized void noteWifiStopped(WorkSource workSource) throws RemoteException;

    synchronized void noteWifiSupplicantStateChanged(int i, boolean z) throws RemoteException;

    synchronized void setBatteryState(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) throws RemoteException;

    synchronized HealthStatsParceler takeUidSnapshot(int i) throws RemoteException;

    synchronized HealthStatsParceler[] takeUidSnapshots(int[] iArr) throws RemoteException;

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
        static final int TRANSACTION_takeUidSnapshot = 84;
        static final int TRANSACTION_takeUidSnapshots = 85;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
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

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            WorkSource _arg0;
            WorkSource _arg02;
            WorkSource _arg03;
            WorkSource _arg04;
            boolean _arg1;
            WorkSource _arg05;
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg06 = data.readInt();
                        noteStartSensor(_arg06, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg07 = data.readInt();
                        noteStopSensor(_arg07, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg08 = data.readInt();
                        noteStartVideo(_arg08);
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg09 = data.readInt();
                        noteStopVideo(_arg09);
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg010 = data.readInt();
                        noteStartAudio(_arg010);
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg011 = data.readInt();
                        noteStopAudio(_arg011);
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
                        int _arg012 = data.readInt();
                        noteFlashlightOn(_arg012);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg013 = data.readInt();
                        noteFlashlightOff(_arg013);
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg014 = data.readInt();
                        noteStartCamera(_arg014);
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg015 = data.readInt();
                        noteStopCamera(_arg015);
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
                        int _arg016 = data.readInt();
                        String _arg12 = data.readString();
                        int _arg2 = data.readInt();
                        noteEvent(_arg016, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 21:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg017 = data.readString();
                        noteSyncStart(_arg017, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 22:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg018 = data.readString();
                        noteSyncFinish(_arg018, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg019 = data.readString();
                        noteJobStart(_arg019, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 24:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg020 = data.readString();
                        int _arg13 = data.readInt();
                        int _arg22 = data.readInt();
                        noteJobFinish(_arg020, _arg13, _arg22);
                        reply.writeNoException();
                        return true;
                    case 25:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg021 = data.readInt();
                        int _arg14 = data.readInt();
                        String _arg23 = data.readString();
                        String _arg3 = data.readString();
                        int _arg4 = data.readInt();
                        boolean _arg5 = data.readInt() != 0;
                        noteStartWakelock(_arg021, _arg14, _arg23, _arg3, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg022 = data.readInt();
                        int _arg15 = data.readInt();
                        String _arg24 = data.readString();
                        String _arg32 = data.readString();
                        int _arg42 = data.readInt();
                        noteStopWakelock(_arg022, _arg15, _arg24, _arg32, _arg42);
                        reply.writeNoException();
                        return true;
                    case 27:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg0 = WorkSource.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        int _arg16 = data.readInt();
                        String _arg25 = data.readString();
                        String _arg33 = data.readString();
                        int _arg43 = data.readInt();
                        boolean _arg52 = data.readInt() != 0;
                        noteStartWakelockFromSource(_arg0, _arg16, _arg25, _arg33, _arg43, _arg52);
                        reply.writeNoException();
                        return true;
                    case 28:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg02 = WorkSource.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        int _arg17 = data.readInt();
                        String _arg26 = data.readString();
                        String _arg34 = data.readString();
                        int _arg44 = data.readInt();
                        WorkSource _arg53 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        int _arg6 = data.readInt();
                        String _arg7 = data.readString();
                        String _arg8 = data.readString();
                        int _arg9 = data.readInt();
                        boolean _arg10 = data.readInt() != 0;
                        noteChangeWakelockFromSource(_arg02, _arg17, _arg26, _arg34, _arg44, _arg53, _arg6, _arg7, _arg8, _arg9, _arg10);
                        reply.writeNoException();
                        return true;
                    case 29:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            WorkSource _arg023 = WorkSource.CREATOR.createFromParcel(data);
                            _arg03 = _arg023;
                        } else {
                            _arg03 = null;
                        }
                        int _arg18 = data.readInt();
                        String _arg27 = data.readString();
                        String _arg35 = data.readString();
                        int _arg45 = data.readInt();
                        noteStopWakelockFromSource(_arg03, _arg18, _arg27, _arg35, _arg45);
                        reply.writeNoException();
                        return true;
                    case 30:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg024 = data.readString();
                        String _arg19 = data.readString();
                        int _arg28 = data.readInt();
                        noteLongPartialWakelockStart(_arg024, _arg19, _arg28);
                        reply.writeNoException();
                        return true;
                    case 31:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg025 = data.readString();
                        String _arg110 = data.readString();
                        WorkSource _arg29 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteLongPartialWakelockStartFromSource(_arg025, _arg110, _arg29);
                        reply.writeNoException();
                        return true;
                    case 32:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg026 = data.readString();
                        String _arg111 = data.readString();
                        int _arg210 = data.readInt();
                        noteLongPartialWakelockFinish(_arg026, _arg111, _arg210);
                        reply.writeNoException();
                        return true;
                    case 33:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg027 = data.readString();
                        String _arg112 = data.readString();
                        WorkSource _arg211 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteLongPartialWakelockFinishFromSource(_arg027, _arg112, _arg211);
                        reply.writeNoException();
                        return true;
                    case 34:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg028 = data.readInt();
                        noteVibratorOn(_arg028, data.readLong());
                        reply.writeNoException();
                        return true;
                    case 35:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg029 = data.readInt();
                        noteVibratorOff(_arg029);
                        reply.writeNoException();
                        return true;
                    case 36:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg04 = WorkSource.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        noteGpsChanged(_arg04, data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null);
                        reply.writeNoException();
                        return true;
                    case 37:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg030 = data.readInt();
                        noteGpsSignalQuality(_arg030);
                        reply.writeNoException();
                        return true;
                    case 38:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg031 = data.readInt();
                        noteScreenState(_arg031);
                        reply.writeNoException();
                        return true;
                    case 39:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg032 = data.readInt();
                        noteScreenBrightness(_arg032);
                        reply.writeNoException();
                        return true;
                    case 40:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg033 = data.readInt();
                        noteUserActivity(_arg033, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 41:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg034 = data.readString();
                        noteWakeUp(_arg034, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 42:
                        data.enforceInterface(DESCRIPTOR);
                        _arg1 = data.readInt() != 0;
                        noteInteractive(_arg1);
                        reply.writeNoException();
                        return true;
                    case 43:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg035 = data.readInt();
                        noteConnectivityChanged(_arg035, data.readString());
                        reply.writeNoException();
                        return true;
                    case 44:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg036 = data.readInt();
                        long _arg113 = data.readLong();
                        int _arg212 = data.readInt();
                        noteMobileRadioPowerState(_arg036, _arg113, _arg212);
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
                        SignalStrength _arg037 = data.readInt() != 0 ? SignalStrength.CREATOR.createFromParcel(data) : null;
                        notePhoneSignalStrength(_arg037);
                        reply.writeNoException();
                        return true;
                    case 48:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg038 = data.readInt();
                        _arg1 = data.readInt() != 0;
                        notePhoneDataConnectionState(_arg038, _arg1);
                        reply.writeNoException();
                        return true;
                    case 49:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg039 = data.readInt();
                        notePhoneState(_arg039);
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
                        WorkSource _arg040 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteWifiRunning(_arg040);
                        reply.writeNoException();
                        return true;
                    case 53:
                        data.enforceInterface(DESCRIPTOR);
                        if (data.readInt() != 0) {
                            _arg05 = WorkSource.CREATOR.createFromParcel(data);
                        } else {
                            _arg05 = null;
                        }
                        noteWifiRunningChanged(_arg05, data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null);
                        reply.writeNoException();
                        return true;
                    case 54:
                        data.enforceInterface(DESCRIPTOR);
                        WorkSource _arg041 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteWifiStopped(_arg041);
                        reply.writeNoException();
                        return true;
                    case 55:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg042 = data.readInt();
                        noteWifiState(_arg042, data.readString());
                        reply.writeNoException();
                        return true;
                    case 56:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg043 = data.readInt();
                        _arg1 = data.readInt() != 0;
                        noteWifiSupplicantStateChanged(_arg043, _arg1);
                        reply.writeNoException();
                        return true;
                    case 57:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg044 = data.readInt();
                        noteWifiRssiChanged(_arg044);
                        reply.writeNoException();
                        return true;
                    case 58:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg045 = data.readInt();
                        noteFullWifiLockAcquired(_arg045);
                        reply.writeNoException();
                        return true;
                    case 59:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg046 = data.readInt();
                        noteFullWifiLockReleased(_arg046);
                        reply.writeNoException();
                        return true;
                    case 60:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg047 = data.readInt();
                        noteWifiScanStarted(_arg047);
                        reply.writeNoException();
                        return true;
                    case 61:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg048 = data.readInt();
                        noteWifiScanStopped(_arg048);
                        reply.writeNoException();
                        return true;
                    case 62:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg049 = data.readInt();
                        noteWifiMulticastEnabled(_arg049);
                        reply.writeNoException();
                        return true;
                    case 63:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg050 = data.readInt();
                        noteWifiMulticastDisabled(_arg050);
                        reply.writeNoException();
                        return true;
                    case 64:
                        data.enforceInterface(DESCRIPTOR);
                        WorkSource _arg051 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteFullWifiLockAcquiredFromSource(_arg051);
                        reply.writeNoException();
                        return true;
                    case 65:
                        data.enforceInterface(DESCRIPTOR);
                        WorkSource _arg052 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteFullWifiLockReleasedFromSource(_arg052);
                        reply.writeNoException();
                        return true;
                    case 66:
                        data.enforceInterface(DESCRIPTOR);
                        WorkSource _arg053 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteWifiScanStartedFromSource(_arg053);
                        reply.writeNoException();
                        return true;
                    case 67:
                        data.enforceInterface(DESCRIPTOR);
                        WorkSource _arg054 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteWifiScanStoppedFromSource(_arg054);
                        reply.writeNoException();
                        return true;
                    case 68:
                        data.enforceInterface(DESCRIPTOR);
                        WorkSource _arg055 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteWifiBatchedScanStartedFromSource(_arg055, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 69:
                        data.enforceInterface(DESCRIPTOR);
                        WorkSource _arg056 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteWifiBatchedScanStoppedFromSource(_arg056);
                        reply.writeNoException();
                        return true;
                    case 70:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg057 = data.readInt();
                        long _arg114 = data.readLong();
                        int _arg213 = data.readInt();
                        noteWifiRadioPowerState(_arg057, _arg114, _arg213);
                        reply.writeNoException();
                        return true;
                    case 71:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg058 = data.readString();
                        noteNetworkInterfaceType(_arg058, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 72:
                        data.enforceInterface(DESCRIPTOR);
                        noteNetworkStatsEnabled();
                        reply.writeNoException();
                        return true;
                    case 73:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg059 = data.readInt();
                        String _arg115 = data.readString();
                        int _arg214 = data.readInt();
                        noteDeviceIdleMode(_arg059, _arg115, _arg214);
                        reply.writeNoException();
                        return true;
                    case 74:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg060 = data.readInt();
                        int _arg116 = data.readInt();
                        int _arg215 = data.readInt();
                        int _arg36 = data.readInt();
                        int _arg46 = data.readInt();
                        int _arg54 = data.readInt();
                        int _arg62 = data.readInt();
                        int _arg72 = data.readInt();
                        setBatteryState(_arg060, _arg116, _arg215, _arg36, _arg46, _arg54, _arg62, _arg72);
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
                        WorkSource _arg061 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        WorkSource _arg062 = _arg061;
                        _arg1 = data.readInt() != 0;
                        noteBleScanStarted(_arg062, _arg1);
                        reply.writeNoException();
                        return true;
                    case 78:
                        data.enforceInterface(DESCRIPTOR);
                        WorkSource _arg063 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        WorkSource _arg064 = _arg063;
                        _arg1 = data.readInt() != 0;
                        noteBleScanStopped(_arg064, _arg1);
                        reply.writeNoException();
                        return true;
                    case 79:
                        data.enforceInterface(DESCRIPTOR);
                        noteResetBleScan();
                        reply.writeNoException();
                        return true;
                    case 80:
                        data.enforceInterface(DESCRIPTOR);
                        WorkSource _arg065 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                        noteBleScanResults(_arg065, data.readInt());
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
                        BluetoothActivityEnergyInfo _arg068 = data.readInt() != 0 ? BluetoothActivityEnergyInfo.CREATOR.createFromParcel(data) : null;
                        noteBluetoothControllerActivity(_arg068);
                        return true;
                    case 87:
                        data.enforceInterface(DESCRIPTOR);
                        ModemActivityInfo _arg069 = data.readInt() != 0 ? ModemActivityInfo.CREATOR.createFromParcel(data) : null;
                        noteModemControllerActivity(_arg069);
                        return true;
                    case 88:
                        data.enforceInterface(DESCRIPTOR);
                        WifiActivityEnergyInfo _arg070 = data.readInt() != 0 ? WifiActivityEnergyInfo.CREATOR.createFromParcel(data) : null;
                        noteWifiControllerActivity(_arg070);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(DESCRIPTOR);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IBatteryStats {
            private IBinder mRemote;

            public private protected Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public synchronized void noteStartSensor(int uid, int sensor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(sensor);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void noteStopSensor(int uid, int sensor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(sensor);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteStartVideo(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteStopVideo(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteStartAudio(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteStopAudio(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteResetVideo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteResetAudio() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteFlashlightOn(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteFlashlightOff(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteStartCamera(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteStopCamera(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteResetCamera() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteResetFlashlight() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized byte[] getStatistics() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized ParcelFileDescriptor getStatisticsStream() throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
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

            public synchronized boolean isCharging() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized long computeBatteryTimeRemaining() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized long computeChargeTimeRemaining() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteEvent(int code, String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteSyncStart(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteSyncFinish(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteJobStart(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteJobFinish(String name, int uid, int stopReason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    _data.writeInt(stopReason);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteStartWakelock(int uid, int pid, String name, String historyName, int type, boolean unimportantForLogging) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(type);
                    _data.writeInt(unimportantForLogging ? 1 : 0);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteStopWakelock(int uid, int pid, String name, String historyName, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(type);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteStartWakelockFromSource(WorkSource ws, int pid, String name, String historyName, int type, boolean unimportantForLogging) throws RemoteException {
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
                    _data.writeInt(unimportantForLogging ? 1 : 0);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteChangeWakelockFromSource(WorkSource ws, int pid, String name, String histyoryName, int type, WorkSource newWs, int newPid, String newName, String newHistoryName, int newType, boolean newUnimportantForLogging) throws RemoteException {
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
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(pid);
                    try {
                        _data.writeString(name);
                        try {
                            _data.writeString(histyoryName);
                            try {
                                _data.writeInt(type);
                                if (newWs != null) {
                                    _data.writeInt(1);
                                    newWs.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(newPid);
                        try {
                            _data.writeString(newName);
                            try {
                                _data.writeString(newHistoryName);
                                try {
                                    _data.writeInt(newType);
                                    try {
                                        _data.writeInt(newUnimportantForLogging ? 1 : 0);
                                        try {
                                            this.mRemote.transact(28, _data, _reply, 0);
                                            _reply.readException();
                                            _reply.recycle();
                                            _data.recycle();
                                        } catch (Throwable th5) {
                                            th = th5;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    } catch (Throwable th6) {
                                        th = th6;
                                    }
                                } catch (Throwable th7) {
                                    th = th7;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th8) {
                                th = th8;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th9) {
                            th = th9;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th10) {
                        th = th10;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th11) {
                    th = th11;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteStopWakelockFromSource(WorkSource ws, int pid, String name, String historyName, int type) throws RemoteException {
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
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteLongPartialWakelockStart(String name, String historyName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(uid);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteLongPartialWakelockStartFromSource(String name, String historyName, WorkSource workSource) throws RemoteException {
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
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteLongPartialWakelockFinish(String name, String historyName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(historyName);
                    _data.writeInt(uid);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteLongPartialWakelockFinishFromSource(String name, String historyName, WorkSource workSource) throws RemoteException {
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
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteVibratorOn(int uid, long durationMillis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeLong(durationMillis);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteVibratorOff(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteGpsChanged(WorkSource oldSource, WorkSource newSource) throws RemoteException {
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
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteGpsSignalQuality(int signalLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(signalLevel);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteScreenState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void noteScreenBrightness(int brightness) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(brightness);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void noteUserActivity(int uid, int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(event);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWakeUp(String reason, int reasonUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    _data.writeInt(reasonUid);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteInteractive(boolean interactive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(interactive ? 1 : 0);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteConnectivityChanged(int type, String extra) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(extra);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteMobileRadioPowerState(int powerState, long timestampNs, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(powerState);
                    _data.writeLong(timestampNs);
                    _data.writeInt(uid);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void notePhoneOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void notePhoneOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void notePhoneSignalStrength(SignalStrength signalStrength) throws RemoteException {
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
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void notePhoneDataConnectionState(int dataType, boolean hasData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(dataType);
                    _data.writeInt(hasData ? 1 : 0);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void notePhoneState(int phoneState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneState);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiRunning(WorkSource ws) throws RemoteException {
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
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiRunningChanged(WorkSource oldWs, WorkSource newWs) throws RemoteException {
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
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiStopped(WorkSource ws) throws RemoteException {
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
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiState(int wifiState, String accessPoint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(wifiState);
                    _data.writeString(accessPoint);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiSupplicantStateChanged(int supplState, boolean failedAuth) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(supplState);
                    _data.writeInt(failedAuth ? 1 : 0);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiRssiChanged(int newRssi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newRssi);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void noteFullWifiLockAcquired(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void noteFullWifiLockReleased(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiScanStarted(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiScanStopped(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void noteWifiMulticastEnabled(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void noteWifiMulticastDisabled(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteFullWifiLockAcquiredFromSource(WorkSource ws) throws RemoteException {
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
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteFullWifiLockReleasedFromSource(WorkSource ws) throws RemoteException {
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
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiScanStartedFromSource(WorkSource ws) throws RemoteException {
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
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiScanStoppedFromSource(WorkSource ws) throws RemoteException {
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
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiBatchedScanStartedFromSource(WorkSource ws, int csph) throws RemoteException {
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
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiBatchedScanStoppedFromSource(WorkSource ws) throws RemoteException {
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
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiRadioPowerState(int powerState, long timestampNs, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(powerState);
                    _data.writeLong(timestampNs);
                    _data.writeInt(uid);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteNetworkInterfaceType(String iface, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(iface);
                    _data.writeInt(type);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteNetworkStatsEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteDeviceIdleMode(int mode, String activeReason, int activeUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeString(activeReason);
                    _data.writeInt(activeUid);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void setBatteryState(int status, int health, int plugType, int level, int temp, int volt, int chargeUAh, int chargeFullUAh) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(health);
                    _data.writeInt(plugType);
                    _data.writeInt(level);
                    _data.writeInt(temp);
                    _data.writeInt(volt);
                    _data.writeInt(chargeUAh);
                    _data.writeInt(chargeFullUAh);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized long getAwakeTimeBattery() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized long getAwakeTimePlugged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteBleScanStarted(WorkSource ws, boolean isUnoptimized) throws RemoteException {
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
                    _data.writeInt(isUnoptimized ? 1 : 0);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteBleScanStopped(WorkSource ws, boolean isUnoptimized) throws RemoteException {
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
                    _data.writeInt(isUnoptimized ? 1 : 0);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteResetBleScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteBleScanResults(WorkSource ws, int numNewResults) throws RemoteException {
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
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized CellularBatteryStats getCellularBatteryStats() throws RemoteException {
                CellularBatteryStats _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(81, _data, _reply, 0);
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
            public synchronized WifiBatteryStats getWifiBatteryStats() throws RemoteException {
                WifiBatteryStats _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(82, _data, _reply, 0);
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
            public synchronized GpsBatteryStats getGpsBatteryStats() throws RemoteException {
                GpsBatteryStats _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(83, _data, _reply, 0);
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
            public synchronized HealthStatsParceler takeUidSnapshot(int uid) throws RemoteException {
                HealthStatsParceler _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(84, _data, _reply, 0);
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
            public synchronized HealthStatsParceler[] takeUidSnapshots(int[] uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(uid);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                    HealthStatsParceler[] _result = (HealthStatsParceler[]) _reply.createTypedArray(HealthStatsParceler.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteBluetoothControllerActivity(BluetoothActivityEnergyInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(86, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteModemControllerActivity(ModemActivityInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(87, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IBatteryStats
            public synchronized void noteWifiControllerActivity(WifiActivityEnergyInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(88, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
