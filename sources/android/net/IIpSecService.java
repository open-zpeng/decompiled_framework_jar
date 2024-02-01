package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IIpSecService extends IInterface {
    synchronized void addAddressToTunnelInterface(int i, LinkAddress linkAddress, String str) throws RemoteException;

    synchronized IpSecSpiResponse allocateSecurityParameterIndex(String str, int i, IBinder iBinder) throws RemoteException;

    synchronized void applyTransportModeTransform(ParcelFileDescriptor parcelFileDescriptor, int i, int i2) throws RemoteException;

    synchronized void applyTunnelModeTransform(int i, int i2, int i3, String str) throws RemoteException;

    synchronized void closeUdpEncapsulationSocket(int i) throws RemoteException;

    synchronized IpSecTransformResponse createTransform(IpSecConfig ipSecConfig, IBinder iBinder, String str) throws RemoteException;

    synchronized IpSecTunnelInterfaceResponse createTunnelInterface(String str, String str2, Network network, IBinder iBinder, String str3) throws RemoteException;

    synchronized void deleteTransform(int i) throws RemoteException;

    synchronized void deleteTunnelInterface(int i, String str) throws RemoteException;

    synchronized IpSecUdpEncapResponse openUdpEncapsulationSocket(int i, IBinder iBinder) throws RemoteException;

    synchronized void releaseSecurityParameterIndex(int i) throws RemoteException;

    synchronized void removeAddressFromTunnelInterface(int i, LinkAddress linkAddress, String str) throws RemoteException;

    synchronized void removeTransportModeTransforms(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IIpSecService {
        private static final String DESCRIPTOR = "android.net.IIpSecService";
        static final int TRANSACTION_addAddressToTunnelInterface = 6;
        static final int TRANSACTION_allocateSecurityParameterIndex = 1;
        static final int TRANSACTION_applyTransportModeTransform = 11;
        static final int TRANSACTION_applyTunnelModeTransform = 12;
        static final int TRANSACTION_closeUdpEncapsulationSocket = 4;
        static final int TRANSACTION_createTransform = 9;
        static final int TRANSACTION_createTunnelInterface = 5;
        static final int TRANSACTION_deleteTransform = 10;
        static final int TRANSACTION_deleteTunnelInterface = 8;
        static final int TRANSACTION_openUdpEncapsulationSocket = 3;
        static final int TRANSACTION_releaseSecurityParameterIndex = 2;
        static final int TRANSACTION_removeAddressFromTunnelInterface = 7;
        static final int TRANSACTION_removeTransportModeTransforms = 13;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IIpSecService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IIpSecService)) {
                return (IIpSecService) iin;
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
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    IBinder _arg2 = data.readStrongBinder();
                    IpSecSpiResponse _result = allocateSecurityParameterIndex(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    releaseSecurityParameterIndex(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    IBinder _arg12 = data.readStrongBinder();
                    IpSecUdpEncapResponse _result2 = openUdpEncapsulationSocket(_arg03, _arg12);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    closeUdpEncapsulationSocket(_arg04);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _arg13 = data.readString();
                    Network _arg22 = data.readInt() != 0 ? Network.CREATOR.createFromParcel(data) : null;
                    IBinder _arg3 = data.readStrongBinder();
                    String _arg4 = data.readString();
                    IpSecTunnelInterfaceResponse _result3 = createTunnelInterface(_arg05, _arg13, _arg22, _arg3, _arg4);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    LinkAddress _arg14 = data.readInt() != 0 ? LinkAddress.CREATOR.createFromParcel(data) : null;
                    String _arg23 = data.readString();
                    addAddressToTunnelInterface(_arg06, _arg14, _arg23);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    LinkAddress _arg15 = data.readInt() != 0 ? LinkAddress.CREATOR.createFromParcel(data) : null;
                    String _arg24 = data.readString();
                    removeAddressFromTunnelInterface(_arg07, _arg15, _arg24);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    String _arg16 = data.readString();
                    deleteTunnelInterface(_arg08, _arg16);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IpSecConfig _arg09 = data.readInt() != 0 ? IpSecConfig.CREATOR.createFromParcel(data) : null;
                    IBinder _arg17 = data.readStrongBinder();
                    String _arg25 = data.readString();
                    IpSecTransformResponse _result4 = createTransform(_arg09, _arg17, _arg25);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    deleteTransform(_arg010);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _arg011 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    int _arg18 = data.readInt();
                    int _arg26 = data.readInt();
                    applyTransportModeTransform(_arg011, _arg18, _arg26);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    int _arg19 = data.readInt();
                    int _arg27 = data.readInt();
                    String _arg32 = data.readString();
                    applyTunnelModeTransform(_arg012, _arg19, _arg27, _arg32);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _arg013 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    removeTransportModeTransforms(_arg013);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IIpSecService {
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

            @Override // android.net.IIpSecService
            public synchronized IpSecSpiResponse allocateSecurityParameterIndex(String destinationAddress, int requestedSpi, IBinder binder) throws RemoteException {
                IpSecSpiResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(destinationAddress);
                    _data.writeInt(requestedSpi);
                    _data.writeStrongBinder(binder);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = IpSecSpiResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized void releaseSecurityParameterIndex(int resourceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(resourceId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized IpSecUdpEncapResponse openUdpEncapsulationSocket(int port, IBinder binder) throws RemoteException {
                IpSecUdpEncapResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(port);
                    _data.writeStrongBinder(binder);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = IpSecUdpEncapResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized void closeUdpEncapsulationSocket(int resourceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(resourceId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized IpSecTunnelInterfaceResponse createTunnelInterface(String localAddr, String remoteAddr, Network underlyingNetwork, IBinder binder, String callingPackage) throws RemoteException {
                IpSecTunnelInterfaceResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(localAddr);
                    _data.writeString(remoteAddr);
                    if (underlyingNetwork != null) {
                        _data.writeInt(1);
                        underlyingNetwork.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = IpSecTunnelInterfaceResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized void addAddressToTunnelInterface(int tunnelResourceId, LinkAddress localAddr, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tunnelResourceId);
                    if (localAddr != null) {
                        _data.writeInt(1);
                        localAddr.writeToParcel(_data, 0);
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

            @Override // android.net.IIpSecService
            public synchronized void removeAddressFromTunnelInterface(int tunnelResourceId, LinkAddress localAddr, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tunnelResourceId);
                    if (localAddr != null) {
                        _data.writeInt(1);
                        localAddr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized void deleteTunnelInterface(int resourceId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(resourceId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized IpSecTransformResponse createTransform(IpSecConfig c, IBinder binder, String callingPackage) throws RemoteException {
                IpSecTransformResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (c != null) {
                        _data.writeInt(1);
                        c.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(binder);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = IpSecTransformResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized void deleteTransform(int transformId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(transformId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized void applyTransportModeTransform(ParcelFileDescriptor socket, int direction, int transformId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (socket != null) {
                        _data.writeInt(1);
                        socket.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(direction);
                    _data.writeInt(transformId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized void applyTunnelModeTransform(int tunnelResourceId, int direction, int transformResourceId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tunnelResourceId);
                    _data.writeInt(direction);
                    _data.writeInt(transformResourceId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.IIpSecService
            public synchronized void removeTransportModeTransforms(ParcelFileDescriptor socket) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (socket != null) {
                        _data.writeInt(1);
                        socket.writeToParcel(_data, 0);
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
        }
    }
}
