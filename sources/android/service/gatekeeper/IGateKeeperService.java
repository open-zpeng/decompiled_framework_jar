package android.service.gatekeeper;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IGateKeeperService extends IInterface {
    private protected synchronized void clearSecureUserId(int i) throws RemoteException;

    private protected synchronized GateKeeperResponse enroll(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized long getSecureUserId(int i) throws RemoteException;

    private protected synchronized void reportDeviceSetupComplete() throws RemoteException;

    private protected synchronized GateKeeperResponse verify(int i, byte[] bArr, byte[] bArr2) throws RemoteException;

    private protected synchronized GateKeeperResponse verifyChallenge(int i, long j, byte[] bArr, byte[] bArr2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IGateKeeperService {
        public protected static final String DESCRIPTOR = "android.service.gatekeeper.IGateKeeperService";
        public private protected static final int TRANSACTION_clearSecureUserId = 5;
        public private protected static final int TRANSACTION_enroll = 1;
        public private protected static final int TRANSACTION_getSecureUserId = 4;
        public private protected static final int TRANSACTION_reportDeviceSetupComplete = 6;
        public private protected static final int TRANSACTION_verify = 2;
        public private protected static final int TRANSACTION_verifyChallenge = 3;

        private protected synchronized Stub() {
            attachInterface(this, "android.service.gatekeeper.IGateKeeperService");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized IGateKeeperService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface("android.service.gatekeeper.IGateKeeperService");
            if (iin != null && (iin instanceof IGateKeeperService)) {
                return (IGateKeeperService) iin;
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
                reply.writeString("android.service.gatekeeper.IGateKeeperService");
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface("android.service.gatekeeper.IGateKeeperService");
                    int _arg0 = data.readInt();
                    byte[] _arg1 = data.createByteArray();
                    byte[] _arg2 = data.createByteArray();
                    byte[] _arg3 = data.createByteArray();
                    GateKeeperResponse _result = enroll(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface("android.service.gatekeeper.IGateKeeperService");
                    int _arg02 = data.readInt();
                    byte[] _arg12 = data.createByteArray();
                    byte[] _arg22 = data.createByteArray();
                    GateKeeperResponse _result2 = verify(_arg02, _arg12, _arg22);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    data.enforceInterface("android.service.gatekeeper.IGateKeeperService");
                    int _arg03 = data.readInt();
                    long _arg13 = data.readLong();
                    byte[] _arg23 = data.createByteArray();
                    byte[] _arg32 = data.createByteArray();
                    GateKeeperResponse _result3 = verifyChallenge(_arg03, _arg13, _arg23, _arg32);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface("android.service.gatekeeper.IGateKeeperService");
                    int _arg04 = data.readInt();
                    long _result4 = getSecureUserId(_arg04);
                    reply.writeNoException();
                    reply.writeLong(_result4);
                    return true;
                case 5:
                    data.enforceInterface("android.service.gatekeeper.IGateKeeperService");
                    int _arg05 = data.readInt();
                    clearSecureUserId(_arg05);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface("android.service.gatekeeper.IGateKeeperService");
                    reportDeviceSetupComplete();
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IGateKeeperService {
            public protected IBinder mRemote;

            public private protected synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            private protected synchronized String getInterfaceDescriptor() {
                return "android.service.gatekeeper.IGateKeeperService";
            }

            private protected synchronized GateKeeperResponse enroll(int uid, byte[] currentPasswordHandle, byte[] currentPassword, byte[] desiredPassword) throws RemoteException {
                GateKeeperResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    _data.writeInt(uid);
                    _data.writeByteArray(currentPasswordHandle);
                    _data.writeByteArray(currentPassword);
                    _data.writeByteArray(desiredPassword);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = GateKeeperResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized GateKeeperResponse verify(int uid, byte[] enrolledPasswordHandle, byte[] providedPassword) throws RemoteException {
                GateKeeperResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    _data.writeInt(uid);
                    _data.writeByteArray(enrolledPasswordHandle);
                    _data.writeByteArray(providedPassword);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = GateKeeperResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized GateKeeperResponse verifyChallenge(int uid, long challenge, byte[] enrolledPasswordHandle, byte[] providedPassword) throws RemoteException {
                GateKeeperResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    _data.writeInt(uid);
                    _data.writeLong(challenge);
                    _data.writeByteArray(enrolledPasswordHandle);
                    _data.writeByteArray(providedPassword);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = GateKeeperResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized long getSecureUserId(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    _data.writeInt(uid);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void clearSecureUserId(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    _data.writeInt(uid);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void reportDeviceSetupComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("android.service.gatekeeper.IGateKeeperService");
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
