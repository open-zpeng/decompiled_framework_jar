package android.net.lowpan;

import android.net.IpPrefix;
import android.net.lowpan.ILowpanEnergyScanCallback;
import android.net.lowpan.ILowpanInterfaceListener;
import android.net.lowpan.ILowpanNetScanCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;
/* loaded from: classes2.dex */
public interface ILowpanInterface extends IInterface {
    private protected static final int ERROR_ALREADY = 9;
    private protected static final int ERROR_BUSY = 8;
    private protected static final int ERROR_CANCELED = 10;
    private protected static final int ERROR_DISABLED = 3;
    private protected static final int ERROR_FEATURE_NOT_SUPPORTED = 11;
    private protected static final int ERROR_FORM_FAILED_AT_SCAN = 15;
    private protected static final int ERROR_INVALID_ARGUMENT = 2;
    private protected static final int ERROR_IO_FAILURE = 6;
    private protected static final int ERROR_JOIN_FAILED_AT_AUTH = 14;
    private protected static final int ERROR_JOIN_FAILED_AT_SCAN = 13;
    private protected static final int ERROR_JOIN_FAILED_UNKNOWN = 12;
    private protected static final int ERROR_NCP_PROBLEM = 7;
    private protected static final int ERROR_TIMEOUT = 5;
    private protected static final int ERROR_UNSPECIFIED = 1;
    private protected static final int ERROR_WRONG_STATE = 4;
    private protected static final String KEY_CHANNEL_MASK = "android.net.lowpan.property.CHANNEL_MASK";
    private protected static final String KEY_MAX_TX_POWER = "android.net.lowpan.property.MAX_TX_POWER";
    private protected static final String NETWORK_TYPE_THREAD_V1 = "org.threadgroup.thread.v1";
    private protected static final String NETWORK_TYPE_UNKNOWN = "unknown";
    private protected static final String PERM_ACCESS_LOWPAN_STATE = "android.permission.ACCESS_LOWPAN_STATE";
    private protected static final String PERM_CHANGE_LOWPAN_STATE = "android.permission.CHANGE_LOWPAN_STATE";
    private protected static final String PERM_READ_LOWPAN_CREDENTIAL = "android.permission.READ_LOWPAN_CREDENTIAL";
    private protected static final String ROLE_COORDINATOR = "coordinator";
    private protected static final String ROLE_DETACHED = "detached";
    private protected static final String ROLE_END_DEVICE = "end-device";
    private protected static final String ROLE_LEADER = "leader";
    private protected static final String ROLE_ROUTER = "router";
    private protected static final String ROLE_SLEEPY_END_DEVICE = "sleepy-end-device";
    private protected static final String ROLE_SLEEPY_ROUTER = "sleepy-router";
    private protected static final String STATE_ATTACHED = "attached";
    private protected static final String STATE_ATTACHING = "attaching";
    private protected static final String STATE_COMMISSIONING = "commissioning";
    private protected static final String STATE_FAULT = "fault";
    private protected static final String STATE_OFFLINE = "offline";

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void addExternalRoute(IpPrefix ipPrefix, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void addListener(ILowpanInterfaceListener iLowpanInterfaceListener) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void addOnMeshPrefix(IpPrefix ipPrefix, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void attach(LowpanProvision lowpanProvision) throws RemoteException;

    private protected synchronized void beginLowPower() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void closeCommissioningSession() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void form(LowpanProvision lowpanProvision) throws RemoteException;

    private protected synchronized String getDriverVersion() throws RemoteException;

    private protected synchronized byte[] getExtendedAddress() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String[] getLinkAddresses() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized IpPrefix[] getLinkNetworks() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized LowpanCredential getLowpanCredential() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized LowpanIdentity getLowpanIdentity() throws RemoteException;

    private protected synchronized byte[] getMacAddress() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getName() throws RemoteException;

    private protected synchronized String getNcpVersion() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getPartitionId() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getRole() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getState() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized LowpanChannelInfo[] getSupportedChannels() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String[] getSupportedNetworkTypes() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isCommissioned() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isConnected() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isEnabled() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isUp() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void join(LowpanProvision lowpanProvision) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void leave() throws RemoteException;

    private protected synchronized void onHostWake() throws RemoteException;

    private protected synchronized void pollForData() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void removeExternalRoute(IpPrefix ipPrefix) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void removeListener(ILowpanInterfaceListener iLowpanInterfaceListener) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void removeOnMeshPrefix(IpPrefix ipPrefix) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void reset() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void sendToCommissioner(byte[] bArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setEnabled(boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void startCommissioningSession(LowpanBeaconInfo lowpanBeaconInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void startEnergyScan(Map map, ILowpanEnergyScanCallback iLowpanEnergyScanCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void startNetScan(Map map, ILowpanNetScanCallback iLowpanNetScanCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void stopEnergyScan() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void stopNetScan() throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ILowpanInterface {
        public protected static final String DESCRIPTOR = "android.net.lowpan.ILowpanInterface";
        public private protected static final int TRANSACTION_addExternalRoute = 39;
        public private protected static final int TRANSACTION_addListener = 31;
        public private protected static final int TRANSACTION_addOnMeshPrefix = 37;
        public private protected static final int TRANSACTION_attach = 22;
        public private protected static final int TRANSACTION_beginLowPower = 28;
        public private protected static final int TRANSACTION_closeCommissioningSession = 26;
        public private protected static final int TRANSACTION_form = 21;
        public private protected static final int TRANSACTION_getDriverVersion = 3;
        public private protected static final int TRANSACTION_getExtendedAddress = 15;
        public private protected static final int TRANSACTION_getLinkAddresses = 18;
        public private protected static final int TRANSACTION_getLinkNetworks = 19;
        public private protected static final int TRANSACTION_getLowpanCredential = 17;
        public private protected static final int TRANSACTION_getLowpanIdentity = 16;
        public private protected static final int TRANSACTION_getMacAddress = 6;
        public private protected static final int TRANSACTION_getName = 1;
        public private protected static final int TRANSACTION_getNcpVersion = 2;
        public private protected static final int TRANSACTION_getPartitionId = 14;
        public private protected static final int TRANSACTION_getRole = 13;
        public private protected static final int TRANSACTION_getState = 12;
        public private protected static final int TRANSACTION_getSupportedChannels = 4;
        public private protected static final int TRANSACTION_getSupportedNetworkTypes = 5;
        public private protected static final int TRANSACTION_isCommissioned = 10;
        public private protected static final int TRANSACTION_isConnected = 11;
        public private protected static final int TRANSACTION_isEnabled = 7;
        public private protected static final int TRANSACTION_isUp = 9;
        public private protected static final int TRANSACTION_join = 20;
        public private protected static final int TRANSACTION_leave = 23;
        public private protected static final int TRANSACTION_onHostWake = 30;
        public private protected static final int TRANSACTION_pollForData = 29;
        public private protected static final int TRANSACTION_removeExternalRoute = 40;
        public private protected static final int TRANSACTION_removeListener = 32;
        public private protected static final int TRANSACTION_removeOnMeshPrefix = 38;
        public private protected static final int TRANSACTION_reset = 24;
        public private protected static final int TRANSACTION_sendToCommissioner = 27;
        public private protected static final int TRANSACTION_setEnabled = 8;
        public private protected static final int TRANSACTION_startCommissioningSession = 25;
        public private protected static final int TRANSACTION_startEnergyScan = 35;
        public private protected static final int TRANSACTION_startNetScan = 33;
        public private protected static final int TRANSACTION_stopEnergyScan = 36;
        public private protected static final int TRANSACTION_stopNetScan = 34;

        private protected synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized ILowpanInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILowpanInterface)) {
                return (ILowpanInterface) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getName();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = getNcpVersion();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _result3 = getDriverVersion();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    LowpanChannelInfo[] _result4 = getSupportedChannels();
                    reply.writeNoException();
                    reply.writeTypedArray(_result4, 1);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result5 = getSupportedNetworkTypes();
                    reply.writeNoException();
                    reply.writeStringArray(_result5);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _result6 = getMacAddress();
                    reply.writeNoException();
                    reply.writeByteArray(_result6);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isEnabled = isEnabled();
                    reply.writeNoException();
                    reply.writeInt(isEnabled ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0 = data.readInt() != 0;
                    setEnabled(_arg0);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isUp = isUp();
                    reply.writeNoException();
                    reply.writeInt(isUp ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isCommissioned = isCommissioned();
                    reply.writeNoException();
                    reply.writeInt(isCommissioned ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isConnected = isConnected();
                    reply.writeNoException();
                    reply.writeInt(isConnected ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _result7 = getState();
                    reply.writeNoException();
                    reply.writeString(_result7);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _result8 = getRole();
                    reply.writeNoException();
                    reply.writeString(_result8);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _result9 = getPartitionId();
                    reply.writeNoException();
                    reply.writeString(_result9);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _result10 = getExtendedAddress();
                    reply.writeNoException();
                    reply.writeByteArray(_result10);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    LowpanIdentity _result11 = getLowpanIdentity();
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    LowpanCredential _result12 = getLowpanCredential();
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result13 = getLinkAddresses();
                    reply.writeNoException();
                    reply.writeStringArray(_result13);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    IpPrefix[] _result14 = getLinkNetworks();
                    reply.writeNoException();
                    reply.writeTypedArray(_result14, 1);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    LowpanProvision _arg02 = data.readInt() != 0 ? LowpanProvision.CREATOR.createFromParcel(data) : null;
                    join(_arg02);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    LowpanProvision _arg03 = data.readInt() != 0 ? LowpanProvision.CREATOR.createFromParcel(data) : null;
                    form(_arg03);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    LowpanProvision _arg04 = data.readInt() != 0 ? LowpanProvision.CREATOR.createFromParcel(data) : null;
                    attach(_arg04);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    leave();
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    reset();
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    LowpanBeaconInfo _arg05 = data.readInt() != 0 ? LowpanBeaconInfo.CREATOR.createFromParcel(data) : null;
                    startCommissioningSession(_arg05);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    closeCommissioningSession();
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg06 = data.createByteArray();
                    sendToCommissioner(_arg06);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    beginLowPower();
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    pollForData();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    onHostWake();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    ILowpanInterfaceListener _arg07 = ILowpanInterfaceListener.Stub.asInterface(data.readStrongBinder());
                    addListener(_arg07);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    ILowpanInterfaceListener _arg08 = ILowpanInterfaceListener.Stub.asInterface(data.readStrongBinder());
                    removeListener(_arg08);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    ClassLoader cl = getClass().getClassLoader();
                    Map _arg09 = data.readHashMap(cl);
                    ILowpanNetScanCallback _arg1 = ILowpanNetScanCallback.Stub.asInterface(data.readStrongBinder());
                    startNetScan(_arg09, _arg1);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    stopNetScan();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    ClassLoader cl2 = getClass().getClassLoader();
                    Map _arg010 = data.readHashMap(cl2);
                    ILowpanEnergyScanCallback _arg12 = ILowpanEnergyScanCallback.Stub.asInterface(data.readStrongBinder());
                    startEnergyScan(_arg010, _arg12);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    stopEnergyScan();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    IpPrefix _arg011 = data.readInt() != 0 ? IpPrefix.CREATOR.createFromParcel(data) : null;
                    IpPrefix _arg012 = _arg011;
                    int _arg13 = data.readInt();
                    addOnMeshPrefix(_arg012, _arg13);
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    IpPrefix _arg013 = data.readInt() != 0 ? IpPrefix.CREATOR.createFromParcel(data) : null;
                    removeOnMeshPrefix(_arg013);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    IpPrefix _arg014 = data.readInt() != 0 ? IpPrefix.CREATOR.createFromParcel(data) : null;
                    IpPrefix _arg015 = _arg014;
                    int _arg14 = data.readInt();
                    addExternalRoute(_arg015, _arg14);
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    IpPrefix _arg016 = data.readInt() != 0 ? IpPrefix.CREATOR.createFromParcel(data) : null;
                    removeExternalRoute(_arg016);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ILowpanInterface {
            public protected IBinder mRemote;

            public private protected synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            private protected synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            private protected synchronized String getName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized String getNcpVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized String getDriverVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized LowpanChannelInfo[] getSupportedChannels() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    LowpanChannelInfo[] _result = (LowpanChannelInfo[]) _reply.createTypedArray(LowpanChannelInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized String[] getSupportedNetworkTypes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized byte[] getMacAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized boolean isEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void setEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized boolean isUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized boolean isCommissioned() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized boolean isConnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized String getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized String getRole() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized String getPartitionId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized byte[] getExtendedAddress() throws RemoteException {
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

            private protected synchronized LowpanIdentity getLowpanIdentity() throws RemoteException {
                LowpanIdentity _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = LowpanIdentity.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized LowpanCredential getLowpanCredential() throws RemoteException {
                LowpanCredential _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = LowpanCredential.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized String[] getLinkAddresses() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized IpPrefix[] getLinkNetworks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    IpPrefix[] _result = (IpPrefix[]) _reply.createTypedArray(IpPrefix.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void join(LowpanProvision provision) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provision != null) {
                        _data.writeInt(1);
                        provision.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void form(LowpanProvision provision) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provision != null) {
                        _data.writeInt(1);
                        provision.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void attach(LowpanProvision provision) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provision != null) {
                        _data.writeInt(1);
                        provision.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void leave() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void reset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void startCommissioningSession(LowpanBeaconInfo beaconInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (beaconInfo != null) {
                        _data.writeInt(1);
                        beaconInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void closeCommissioningSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void sendToCommissioner(byte[] packet) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(packet);
                    this.mRemote.transact(27, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void beginLowPower() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void pollForData() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onHostWake() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void addListener(ILowpanInterfaceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void removeListener(ILowpanInterfaceListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(32, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void startNetScan(Map properties, ILowpanNetScanCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(properties);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void stopNetScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void startEnergyScan(Map properties, ILowpanEnergyScanCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(properties);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void stopEnergyScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void addOnMeshPrefix(IpPrefix prefix, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (prefix != null) {
                        _data.writeInt(1);
                        prefix.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void removeOnMeshPrefix(IpPrefix prefix) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (prefix != null) {
                        _data.writeInt(1);
                        prefix.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(38, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void addExternalRoute(IpPrefix prefix, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (prefix != null) {
                        _data.writeInt(1);
                        prefix.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void removeExternalRoute(IpPrefix prefix) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (prefix != null) {
                        _data.writeInt(1);
                        prefix.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(40, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
