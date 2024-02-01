package android.bluetooth;

import android.app.PendingIntent;
import android.bluetooth.IBluetoothGattCallback;
import android.bluetooth.IBluetoothGattServerCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.IAdvertisingSetCallback;
import android.bluetooth.le.IPeriodicAdvertisingCallback;
import android.bluetooth.le.IScannerCallback;
import android.bluetooth.le.PeriodicAdvertisingParameters;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.os.WorkSource;
import java.util.List;
/* loaded from: classes.dex */
public interface IBluetoothGatt extends IInterface {
    synchronized void addService(int i, BluetoothGattService bluetoothGattService) throws RemoteException;

    synchronized void beginReliableWrite(int i, String str) throws RemoteException;

    synchronized void clearServices(int i) throws RemoteException;

    synchronized void clientConnect(int i, String str, boolean z, int i2, boolean z2, int i3) throws RemoteException;

    synchronized void clientDisconnect(int i, String str) throws RemoteException;

    synchronized void clientReadPhy(int i, String str) throws RemoteException;

    synchronized void clientSetPreferredPhy(int i, String str, int i2, int i3, int i4) throws RemoteException;

    synchronized void configureMTU(int i, String str, int i2) throws RemoteException;

    synchronized void connectionParameterUpdate(int i, String str, int i2) throws RemoteException;

    synchronized void disconnectAll() throws RemoteException;

    synchronized void discoverServiceByUuid(int i, String str, ParcelUuid parcelUuid) throws RemoteException;

    synchronized void discoverServices(int i, String str) throws RemoteException;

    synchronized void enableAdvertisingSet(int i, boolean z, int i2, int i3) throws RemoteException;

    synchronized void endReliableWrite(int i, String str, boolean z) throws RemoteException;

    synchronized void flushPendingBatchResults(int i) throws RemoteException;

    synchronized List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] iArr) throws RemoteException;

    synchronized void getOwnAddress(int i) throws RemoteException;

    synchronized void leConnectionUpdate(int i, String str, int i2, int i3, int i4, int i5, int i6, int i7) throws RemoteException;

    synchronized int numHwTrackFiltersAvailable() throws RemoteException;

    synchronized void readCharacteristic(int i, String str, int i2, int i3) throws RemoteException;

    synchronized void readDescriptor(int i, String str, int i2, int i3) throws RemoteException;

    synchronized void readRemoteRssi(int i, String str) throws RemoteException;

    synchronized void readUsingCharacteristicUuid(int i, String str, ParcelUuid parcelUuid, int i2, int i3, int i4) throws RemoteException;

    synchronized void refreshDevice(int i, String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void registerClient(ParcelUuid parcelUuid, IBluetoothGattCallback iBluetoothGattCallback) throws RemoteException;

    synchronized void registerForNotification(int i, String str, int i2, boolean z) throws RemoteException;

    synchronized void registerScanner(IScannerCallback iScannerCallback, WorkSource workSource) throws RemoteException;

    synchronized void registerServer(ParcelUuid parcelUuid, IBluetoothGattServerCallback iBluetoothGattServerCallback) throws RemoteException;

    synchronized void registerSync(ScanResult scanResult, int i, int i2, IPeriodicAdvertisingCallback iPeriodicAdvertisingCallback) throws RemoteException;

    synchronized void removeService(int i, int i2) throws RemoteException;

    synchronized void sendNotification(int i, String str, int i2, boolean z, byte[] bArr) throws RemoteException;

    synchronized void sendResponse(int i, String str, int i2, int i3, int i4, byte[] bArr) throws RemoteException;

    synchronized void serverConnect(int i, String str, boolean z, int i2) throws RemoteException;

    synchronized void serverDisconnect(int i, String str) throws RemoteException;

    synchronized void serverReadPhy(int i, String str) throws RemoteException;

    synchronized void serverSetPreferredPhy(int i, String str, int i2, int i3, int i4) throws RemoteException;

    synchronized void setAdvertisingData(int i, AdvertiseData advertiseData) throws RemoteException;

    synchronized void setAdvertisingParameters(int i, AdvertisingSetParameters advertisingSetParameters) throws RemoteException;

    synchronized void setPeriodicAdvertisingData(int i, AdvertiseData advertiseData) throws RemoteException;

    synchronized void setPeriodicAdvertisingEnable(int i, boolean z) throws RemoteException;

    synchronized void setPeriodicAdvertisingParameters(int i, PeriodicAdvertisingParameters periodicAdvertisingParameters) throws RemoteException;

    synchronized void setScanResponseData(int i, AdvertiseData advertiseData) throws RemoteException;

    void startAdvertisingSet(AdvertisingSetParameters advertisingSetParameters, AdvertiseData advertiseData, AdvertiseData advertiseData2, PeriodicAdvertisingParameters periodicAdvertisingParameters, AdvertiseData advertiseData3, int i, int i2, IAdvertisingSetCallback iAdvertisingSetCallback, List<BluetoothDevice> list) throws RemoteException;

    synchronized void startScan(int i, ScanSettings scanSettings, List<ScanFilter> list, List list2, String str) throws RemoteException;

    synchronized void startScanForIntent(PendingIntent pendingIntent, ScanSettings scanSettings, List<ScanFilter> list, String str) throws RemoteException;

    synchronized void stopAdvertisingSet(IAdvertisingSetCallback iAdvertisingSetCallback) throws RemoteException;

    synchronized void stopScan(int i) throws RemoteException;

    synchronized void stopScanForIntent(PendingIntent pendingIntent, String str) throws RemoteException;

    synchronized void unregAll() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void unregisterClient(int i) throws RemoteException;

    synchronized void unregisterScanner(int i) throws RemoteException;

    synchronized void unregisterServer(int i) throws RemoteException;

    synchronized void unregisterSync(IPeriodicAdvertisingCallback iPeriodicAdvertisingCallback) throws RemoteException;

    void updateAdvertisingWhiteList(int i, BluetoothDevice bluetoothDevice, boolean z) throws RemoteException;

    synchronized void writeCharacteristic(int i, String str, int i2, int i3, int i4, byte[] bArr) throws RemoteException;

    synchronized void writeDescriptor(int i, String str, int i2, int i3, byte[] bArr) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetoothGatt {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothGatt";
        static final int TRANSACTION_addService = 49;
        static final int TRANSACTION_beginReliableWrite = 37;
        static final int TRANSACTION_clearServices = 51;
        static final int TRANSACTION_clientConnect = 24;
        static final int TRANSACTION_clientDisconnect = 25;
        static final int TRANSACTION_clientReadPhy = 27;
        static final int TRANSACTION_clientSetPreferredPhy = 26;
        static final int TRANSACTION_configureMTU = 40;
        static final int TRANSACTION_connectionParameterUpdate = 41;
        static final int TRANSACTION_disconnectAll = 54;
        static final int TRANSACTION_discoverServiceByUuid = 30;
        static final int TRANSACTION_discoverServices = 29;
        static final int TRANSACTION_enableAdvertisingSet = 12;
        static final int TRANSACTION_endReliableWrite = 38;
        static final int TRANSACTION_flushPendingBatchResults = 8;
        static final int TRANSACTION_getDevicesMatchingConnectionStates = 1;
        static final int TRANSACTION_getOwnAddress = 11;
        static final int TRANSACTION_leConnectionUpdate = 42;
        static final int TRANSACTION_numHwTrackFiltersAvailable = 56;
        static final int TRANSACTION_readCharacteristic = 31;
        static final int TRANSACTION_readDescriptor = 34;
        static final int TRANSACTION_readRemoteRssi = 39;
        static final int TRANSACTION_readUsingCharacteristicUuid = 32;
        static final int TRANSACTION_refreshDevice = 28;
        static final int TRANSACTION_registerClient = 22;
        static final int TRANSACTION_registerForNotification = 36;
        static final int TRANSACTION_registerScanner = 2;
        static final int TRANSACTION_registerServer = 43;
        static final int TRANSACTION_registerSync = 20;
        static final int TRANSACTION_removeService = 50;
        static final int TRANSACTION_sendNotification = 53;
        static final int TRANSACTION_sendResponse = 52;
        static final int TRANSACTION_serverConnect = 45;
        static final int TRANSACTION_serverDisconnect = 46;
        static final int TRANSACTION_serverReadPhy = 48;
        static final int TRANSACTION_serverSetPreferredPhy = 47;
        static final int TRANSACTION_setAdvertisingData = 13;
        static final int TRANSACTION_setAdvertisingParameters = 15;
        static final int TRANSACTION_setPeriodicAdvertisingData = 17;
        static final int TRANSACTION_setPeriodicAdvertisingEnable = 18;
        static final int TRANSACTION_setPeriodicAdvertisingParameters = 16;
        static final int TRANSACTION_setScanResponseData = 14;
        static final int TRANSACTION_startAdvertisingSet = 9;
        static final int TRANSACTION_startScan = 4;
        static final int TRANSACTION_startScanForIntent = 5;
        static final int TRANSACTION_stopAdvertisingSet = 10;
        static final int TRANSACTION_stopScan = 7;
        static final int TRANSACTION_stopScanForIntent = 6;
        static final int TRANSACTION_unregAll = 55;
        static final int TRANSACTION_unregisterClient = 23;
        static final int TRANSACTION_unregisterScanner = 3;
        static final int TRANSACTION_unregisterServer = 44;
        static final int TRANSACTION_unregisterSync = 21;
        static final int TRANSACTION_updateAdvertisingWhiteList = 19;
        static final int TRANSACTION_writeCharacteristic = 33;
        static final int TRANSACTION_writeDescriptor = 35;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IBluetoothGatt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetoothGatt)) {
                return (IBluetoothGatt) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ScanSettings _arg1;
            PendingIntent _arg0;
            AdvertisingSetParameters _arg02;
            AdvertiseData _arg12;
            PeriodicAdvertisingParameters _arg3;
            boolean _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            AdvertiseData _arg4 = null;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg03 = data.createIntArray();
                    List<BluetoothDevice> _result = getDevicesMatchingConnectionStates(_arg03);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IScannerCallback _arg04 = IScannerCallback.Stub.asInterface(data.readStrongBinder());
                    WorkSource _arg13 = data.readInt() != 0 ? WorkSource.CREATOR.createFromParcel(data) : null;
                    registerScanner(_arg04, _arg13);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    unregisterScanner(_arg05);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    if (data.readInt() != 0) {
                        ScanSettings _arg14 = ScanSettings.CREATOR.createFromParcel(data);
                        _arg1 = _arg14;
                    } else {
                        _arg1 = null;
                    }
                    List<ScanFilter> _arg22 = data.createTypedArrayList(ScanFilter.CREATOR);
                    ClassLoader cl = getClass().getClassLoader();
                    List _arg32 = data.readArrayList(cl);
                    String _arg42 = data.readString();
                    startScan(_arg06, _arg1, _arg22, _arg32, _arg42);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    ScanSettings _arg15 = data.readInt() != 0 ? ScanSettings.CREATOR.createFromParcel(data) : null;
                    List<ScanFilter> _arg23 = data.createTypedArrayList(ScanFilter.CREATOR);
                    String _arg33 = data.readString();
                    startScanForIntent(_arg0, _arg15, _arg23, _arg33);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    PendingIntent _arg07 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    String _arg16 = data.readString();
                    stopScanForIntent(_arg07, _arg16);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    stopScan(_arg08);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    flushPendingBatchResults(_arg09);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        AdvertisingSetParameters _arg010 = AdvertisingSetParameters.CREATOR.createFromParcel(data);
                        _arg02 = _arg010;
                    } else {
                        _arg02 = null;
                    }
                    if (data.readInt() != 0) {
                        AdvertiseData _arg17 = AdvertiseData.CREATOR.createFromParcel(data);
                        _arg12 = _arg17;
                    } else {
                        _arg12 = null;
                    }
                    AdvertiseData _arg24 = data.readInt() != 0 ? AdvertiseData.CREATOR.createFromParcel(data) : null;
                    if (data.readInt() != 0) {
                        PeriodicAdvertisingParameters _arg34 = PeriodicAdvertisingParameters.CREATOR.createFromParcel(data);
                        _arg3 = _arg34;
                    } else {
                        _arg3 = null;
                    }
                    if (data.readInt() != 0) {
                        AdvertiseData _arg43 = AdvertiseData.CREATOR.createFromParcel(data);
                        _arg4 = _arg43;
                    }
                    int _arg5 = data.readInt();
                    int _arg6 = data.readInt();
                    IAdvertisingSetCallback _arg7 = IAdvertisingSetCallback.Stub.asInterface(data.readStrongBinder());
                    List<BluetoothDevice> _arg8 = data.createTypedArrayList(BluetoothDevice.CREATOR);
                    startAdvertisingSet(_arg02, _arg12, _arg24, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IAdvertisingSetCallback _arg011 = IAdvertisingSetCallback.Stub.asInterface(data.readStrongBinder());
                    stopAdvertisingSet(_arg011);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    getOwnAddress(_arg012);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    int _arg25 = data.readInt();
                    int _arg35 = data.readInt();
                    enableAdvertisingSet(_arg013, _arg2, _arg25, _arg35);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    AdvertiseData _arg18 = data.readInt() != 0 ? AdvertiseData.CREATOR.createFromParcel(data) : null;
                    setAdvertisingData(_arg014, _arg18);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    AdvertiseData _arg19 = data.readInt() != 0 ? AdvertiseData.CREATOR.createFromParcel(data) : null;
                    setScanResponseData(_arg015, _arg19);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    AdvertisingSetParameters _arg110 = data.readInt() != 0 ? AdvertisingSetParameters.CREATOR.createFromParcel(data) : null;
                    setAdvertisingParameters(_arg016, _arg110);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    PeriodicAdvertisingParameters _arg111 = data.readInt() != 0 ? PeriodicAdvertisingParameters.CREATOR.createFromParcel(data) : null;
                    setPeriodicAdvertisingParameters(_arg017, _arg111);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg018 = data.readInt();
                    AdvertiseData _arg112 = data.readInt() != 0 ? AdvertiseData.CREATOR.createFromParcel(data) : null;
                    setPeriodicAdvertisingData(_arg018, _arg112);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg019 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    setPeriodicAdvertisingEnable(_arg019, _arg2);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    BluetoothDevice _arg113 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    BluetoothDevice _arg114 = _arg113;
                    _arg2 = data.readInt() != 0;
                    updateAdvertisingWhiteList(_arg020, _arg114, _arg2);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    ScanResult _arg021 = data.readInt() != 0 ? ScanResult.CREATOR.createFromParcel(data) : null;
                    int _arg115 = data.readInt();
                    int _arg26 = data.readInt();
                    IPeriodicAdvertisingCallback _arg36 = IPeriodicAdvertisingCallback.Stub.asInterface(data.readStrongBinder());
                    registerSync(_arg021, _arg115, _arg26, _arg36);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    IPeriodicAdvertisingCallback _arg022 = IPeriodicAdvertisingCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterSync(_arg022);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelUuid _arg023 = data.readInt() != 0 ? ParcelUuid.CREATOR.createFromParcel(data) : null;
                    IBluetoothGattCallback _arg116 = IBluetoothGattCallback.Stub.asInterface(data.readStrongBinder());
                    registerClient(_arg023, _arg116);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    unregisterClient(_arg024);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    String _arg117 = data.readString();
                    boolean _arg27 = data.readInt() != 0;
                    int _arg37 = data.readInt();
                    boolean _arg44 = data.readInt() != 0;
                    int _arg52 = data.readInt();
                    clientConnect(_arg025, _arg117, _arg27, _arg37, _arg44, _arg52);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    String _arg118 = data.readString();
                    clientDisconnect(_arg026, _arg118);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg027 = data.readInt();
                    String _arg119 = data.readString();
                    int _arg28 = data.readInt();
                    int _arg38 = data.readInt();
                    int _arg45 = data.readInt();
                    clientSetPreferredPhy(_arg027, _arg119, _arg28, _arg38, _arg45);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg028 = data.readInt();
                    String _arg120 = data.readString();
                    clientReadPhy(_arg028, _arg120);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg029 = data.readInt();
                    String _arg121 = data.readString();
                    refreshDevice(_arg029, _arg121);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg030 = data.readInt();
                    String _arg122 = data.readString();
                    discoverServices(_arg030, _arg122);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg031 = data.readInt();
                    String _arg123 = data.readString();
                    discoverServiceByUuid(_arg031, _arg123, data.readInt() != 0 ? ParcelUuid.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg032 = data.readInt();
                    String _arg124 = data.readString();
                    int _arg29 = data.readInt();
                    int _arg39 = data.readInt();
                    readCharacteristic(_arg032, _arg124, _arg29, _arg39);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg033 = data.readInt();
                    String _arg125 = data.readString();
                    ParcelUuid _arg210 = data.readInt() != 0 ? ParcelUuid.CREATOR.createFromParcel(data) : null;
                    int _arg310 = data.readInt();
                    int _arg46 = data.readInt();
                    int _arg53 = data.readInt();
                    readUsingCharacteristicUuid(_arg033, _arg125, _arg210, _arg310, _arg46, _arg53);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg034 = data.readInt();
                    String _arg126 = data.readString();
                    int _arg211 = data.readInt();
                    int _arg311 = data.readInt();
                    int _arg47 = data.readInt();
                    byte[] _arg54 = data.createByteArray();
                    writeCharacteristic(_arg034, _arg126, _arg211, _arg311, _arg47, _arg54);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg035 = data.readInt();
                    String _arg127 = data.readString();
                    int _arg212 = data.readInt();
                    int _arg312 = data.readInt();
                    readDescriptor(_arg035, _arg127, _arg212, _arg312);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    String _arg128 = data.readString();
                    int _arg213 = data.readInt();
                    int _arg313 = data.readInt();
                    byte[] _arg48 = data.createByteArray();
                    writeDescriptor(_arg036, _arg128, _arg213, _arg313, _arg48);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    String _arg129 = data.readString();
                    int _arg214 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    registerForNotification(_arg037, _arg129, _arg214, _arg2);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg038 = data.readInt();
                    String _arg130 = data.readString();
                    beginReliableWrite(_arg038, _arg130);
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg039 = data.readInt();
                    String _arg131 = data.readString();
                    _arg2 = data.readInt() != 0;
                    endReliableWrite(_arg039, _arg131, _arg2);
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg040 = data.readInt();
                    String _arg132 = data.readString();
                    readRemoteRssi(_arg040, _arg132);
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg041 = data.readInt();
                    String _arg133 = data.readString();
                    configureMTU(_arg041, _arg133, data.readInt());
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg042 = data.readInt();
                    String _arg134 = data.readString();
                    connectionParameterUpdate(_arg042, _arg134, data.readInt());
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg043 = data.readInt();
                    String _arg135 = data.readString();
                    int _arg215 = data.readInt();
                    int _arg314 = data.readInt();
                    int _arg49 = data.readInt();
                    int _arg55 = data.readInt();
                    int _arg62 = data.readInt();
                    int _arg72 = data.readInt();
                    leConnectionUpdate(_arg043, _arg135, _arg215, _arg314, _arg49, _arg55, _arg62, _arg72);
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelUuid _arg044 = data.readInt() != 0 ? ParcelUuid.CREATOR.createFromParcel(data) : null;
                    IBluetoothGattServerCallback _arg136 = IBluetoothGattServerCallback.Stub.asInterface(data.readStrongBinder());
                    registerServer(_arg044, _arg136);
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg045 = data.readInt();
                    unregisterServer(_arg045);
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg046 = data.readInt();
                    String _arg137 = data.readString();
                    _arg2 = data.readInt() != 0;
                    int _arg315 = data.readInt();
                    serverConnect(_arg046, _arg137, _arg2, _arg315);
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg047 = data.readInt();
                    String _arg138 = data.readString();
                    serverDisconnect(_arg047, _arg138);
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg048 = data.readInt();
                    String _arg139 = data.readString();
                    int _arg216 = data.readInt();
                    int _arg316 = data.readInt();
                    int _arg410 = data.readInt();
                    serverSetPreferredPhy(_arg048, _arg139, _arg216, _arg316, _arg410);
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg049 = data.readInt();
                    String _arg140 = data.readString();
                    serverReadPhy(_arg049, _arg140);
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg050 = data.readInt();
                    BluetoothGattService _arg141 = data.readInt() != 0 ? BluetoothGattService.CREATOR.createFromParcel(data) : null;
                    addService(_arg050, _arg141);
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg051 = data.readInt();
                    int _arg142 = data.readInt();
                    removeService(_arg051, _arg142);
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg052 = data.readInt();
                    clearServices(_arg052);
                    reply.writeNoException();
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg053 = data.readInt();
                    String _arg143 = data.readString();
                    int _arg217 = data.readInt();
                    int _arg317 = data.readInt();
                    int _arg411 = data.readInt();
                    byte[] _arg56 = data.createByteArray();
                    sendResponse(_arg053, _arg143, _arg217, _arg317, _arg411, _arg56);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg054 = data.readInt();
                    String _arg144 = data.readString();
                    int _arg218 = data.readInt();
                    boolean _arg318 = data.readInt() != 0;
                    byte[] _arg412 = data.createByteArray();
                    sendNotification(_arg054, _arg144, _arg218, _arg318, _arg412);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    disconnectAll();
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    unregAll();
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = numHwTrackFiltersAvailable();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IBluetoothGatt {
            private IBinder mRemote;

            synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(states);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<BluetoothDevice> _result = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void registerScanner(IScannerCallback callback, WorkSource workSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void unregisterScanner(int scannerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scannerId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void startScan(int scannerId, ScanSettings settings, List<ScanFilter> filters, List scanStorages, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scannerId);
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedList(filters);
                    _data.writeList(scanStorages);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void startScanForIntent(PendingIntent intent, ScanSettings settings, List<ScanFilter> filters, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedList(filters);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void stopScanForIntent(PendingIntent intent, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void stopScan(int scannerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scannerId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void flushPendingBatchResults(int scannerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scannerId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, int duration, int maxExtAdvEvents, IAdvertisingSetCallback callback, List<BluetoothDevice> btDevices) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (parameters != null) {
                        _data.writeInt(1);
                        parameters.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (advertiseData != null) {
                        _data.writeInt(1);
                        advertiseData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (scanResponse != null) {
                        _data.writeInt(1);
                        scanResponse.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (periodicParameters != null) {
                        _data.writeInt(1);
                        periodicParameters.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (periodicData != null) {
                        _data.writeInt(1);
                        periodicData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(duration);
                    _data.writeInt(maxExtAdvEvents);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeTypedList(btDevices);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void stopAdvertisingSet(IAdvertisingSetCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void getOwnAddress(int advertiserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void enableAdvertisingSet(int advertiserId, boolean enable, int duration, int maxExtAdvEvents) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeInt(duration);
                    _data.writeInt(maxExtAdvEvents);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void setAdvertisingData(int advertiserId, AdvertiseData data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void setScanResponseData(int advertiserId, AdvertiseData data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void setAdvertisingParameters(int advertiserId, AdvertisingSetParameters parameters) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (parameters != null) {
                        _data.writeInt(1);
                        parameters.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void setPeriodicAdvertisingParameters(int advertiserId, PeriodicAdvertisingParameters parameters) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (parameters != null) {
                        _data.writeInt(1);
                        parameters.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void setPeriodicAdvertisingData(int advertiserId, AdvertiseData data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void setPeriodicAdvertisingEnable(int advertiserId, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public void updateAdvertisingWhiteList(int advertiserId, BluetoothDevice btDevice, boolean toAdd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(advertiserId);
                    if (btDevice != null) {
                        _data.writeInt(1);
                        btDevice.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(toAdd ? 1 : 0);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void registerSync(ScanResult scanResult, int skip, int timeout, IPeriodicAdvertisingCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (scanResult != null) {
                        _data.writeInt(1);
                        scanResult.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(skip);
                    _data.writeInt(timeout);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void unregisterSync(IPeriodicAdvertisingCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void registerClient(ParcelUuid appId, IBluetoothGattCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appId != null) {
                        _data.writeInt(1);
                        appId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void unregisterClient(int clientIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void clientConnect(int clientIf, String address, boolean isDirect, int transport, boolean opportunistic, int phy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(isDirect ? 1 : 0);
                    _data.writeInt(transport);
                    _data.writeInt(opportunistic ? 1 : 0);
                    _data.writeInt(phy);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void clientDisconnect(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void clientSetPreferredPhy(int clientIf, String address, int txPhy, int rxPhy, int phyOptions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(txPhy);
                    _data.writeInt(rxPhy);
                    _data.writeInt(phyOptions);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void clientReadPhy(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void refreshDevice(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void discoverServices(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void discoverServiceByUuid(int clientIf, String address, ParcelUuid uuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void readCharacteristic(int clientIf, String address, int handle, int authReq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(authReq);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void readUsingCharacteristicUuid(int clientIf, String address, ParcelUuid uuid, int startHandle, int endHandle, int authReq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(startHandle);
                    _data.writeInt(endHandle);
                    _data.writeInt(authReq);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void writeCharacteristic(int clientIf, String address, int handle, int writeType, int authReq, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(writeType);
                    _data.writeInt(authReq);
                    _data.writeByteArray(value);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void readDescriptor(int clientIf, String address, int handle, int authReq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(authReq);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void writeDescriptor(int clientIf, String address, int handle, int authReq, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(authReq);
                    _data.writeByteArray(value);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void registerForNotification(int clientIf, String address, int handle, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void beginReliableWrite(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void endReliableWrite(int clientIf, String address, boolean execute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(execute ? 1 : 0);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void readRemoteRssi(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void configureMTU(int clientIf, String address, int mtu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(mtu);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void connectionParameterUpdate(int clientIf, String address, int connectionPriority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(connectionPriority);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void leConnectionUpdate(int clientIf, String address, int minInterval, int maxInterval, int slaveLatency, int supervisionTimeout, int minConnectionEventLen, int maxConnectionEventLen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(minInterval);
                    _data.writeInt(maxInterval);
                    _data.writeInt(slaveLatency);
                    _data.writeInt(supervisionTimeout);
                    _data.writeInt(minConnectionEventLen);
                    _data.writeInt(maxConnectionEventLen);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void registerServer(ParcelUuid appId, IBluetoothGattServerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appId != null) {
                        _data.writeInt(1);
                        appId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void unregisterServer(int serverIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void serverConnect(int serverIf, String address, boolean isDirect, int transport) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    _data.writeInt(isDirect ? 1 : 0);
                    _data.writeInt(transport);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void serverDisconnect(int serverIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void serverSetPreferredPhy(int clientIf, String address, int txPhy, int rxPhy, int phyOptions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    _data.writeInt(txPhy);
                    _data.writeInt(rxPhy);
                    _data.writeInt(phyOptions);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void serverReadPhy(int clientIf, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientIf);
                    _data.writeString(address);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void addService(int serverIf, BluetoothGattService service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void removeService(int serverIf, int handle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeInt(handle);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void clearServices(int serverIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void sendResponse(int serverIf, String address, int requestId, int status, int offset, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    _data.writeInt(requestId);
                    _data.writeInt(status);
                    _data.writeInt(offset);
                    _data.writeByteArray(value);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void sendNotification(int serverIf, String address, int handle, boolean confirm, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(serverIf);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeInt(confirm ? 1 : 0);
                    _data.writeByteArray(value);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void disconnectAll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized void unregAll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGatt
            public synchronized int numHwTrackFiltersAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
