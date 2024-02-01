package android.hardware.fingerprint;

import android.hardware.biometrics.IBiometricPromptReceiver;
import android.hardware.fingerprint.IFingerprintClientActiveCallback;
import android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback;
import android.hardware.fingerprint.IFingerprintServiceReceiver;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes.dex */
public interface IFingerprintService extends IInterface {
    synchronized void addClientActiveCallback(IFingerprintClientActiveCallback iFingerprintClientActiveCallback) throws RemoteException;

    synchronized void addLockoutResetCallback(IFingerprintServiceLockoutResetCallback iFingerprintServiceLockoutResetCallback) throws RemoteException;

    synchronized void authenticate(IBinder iBinder, long j, int i, IFingerprintServiceReceiver iFingerprintServiceReceiver, int i2, String str, Bundle bundle, IBiometricPromptReceiver iBiometricPromptReceiver) throws RemoteException;

    synchronized void cancelAuthentication(IBinder iBinder, String str) throws RemoteException;

    synchronized void cancelEnrollment(IBinder iBinder) throws RemoteException;

    synchronized void enroll(IBinder iBinder, byte[] bArr, int i, IFingerprintServiceReceiver iFingerprintServiceReceiver, int i2, String str) throws RemoteException;

    synchronized void enumerate(IBinder iBinder, int i, IFingerprintServiceReceiver iFingerprintServiceReceiver) throws RemoteException;

    synchronized long getAuthenticatorId(String str) throws RemoteException;

    synchronized List<Fingerprint> getEnrolledFingerprints(int i, String str) throws RemoteException;

    synchronized boolean hasEnrolledFingerprints(int i, String str) throws RemoteException;

    synchronized boolean isClientActive() throws RemoteException;

    synchronized boolean isHardwareDetected(long j, String str) throws RemoteException;

    synchronized int postEnroll(IBinder iBinder) throws RemoteException;

    synchronized long preEnroll(IBinder iBinder) throws RemoteException;

    synchronized void remove(IBinder iBinder, int i, int i2, int i3, IFingerprintServiceReceiver iFingerprintServiceReceiver) throws RemoteException;

    synchronized void removeClientActiveCallback(IFingerprintClientActiveCallback iFingerprintClientActiveCallback) throws RemoteException;

    synchronized void rename(int i, int i2, String str) throws RemoteException;

    synchronized void resetTimeout(byte[] bArr) throws RemoteException;

    synchronized void setActiveUser(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IFingerprintService {
        private static final String DESCRIPTOR = "android.hardware.fingerprint.IFingerprintService";
        static final int TRANSACTION_addClientActiveCallback = 18;
        static final int TRANSACTION_addLockoutResetCallback = 14;
        static final int TRANSACTION_authenticate = 1;
        static final int TRANSACTION_cancelAuthentication = 2;
        static final int TRANSACTION_cancelEnrollment = 4;
        static final int TRANSACTION_enroll = 3;
        static final int TRANSACTION_enumerate = 16;
        static final int TRANSACTION_getAuthenticatorId = 12;
        static final int TRANSACTION_getEnrolledFingerprints = 7;
        static final int TRANSACTION_hasEnrolledFingerprints = 11;
        static final int TRANSACTION_isClientActive = 17;
        static final int TRANSACTION_isHardwareDetected = 8;
        static final int TRANSACTION_postEnroll = 10;
        static final int TRANSACTION_preEnroll = 9;
        static final int TRANSACTION_remove = 5;
        static final int TRANSACTION_removeClientActiveCallback = 19;
        static final int TRANSACTION_rename = 6;
        static final int TRANSACTION_resetTimeout = 13;
        static final int TRANSACTION_setActiveUser = 15;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IFingerprintService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IFingerprintService)) {
                return (IFingerprintService) iin;
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
                    IBinder _arg0 = data.readStrongBinder();
                    long _arg1 = data.readLong();
                    int _arg2 = data.readInt();
                    IFingerprintServiceReceiver _arg3 = IFingerprintServiceReceiver.Stub.asInterface(data.readStrongBinder());
                    int _arg4 = data.readInt();
                    String _arg5 = data.readString();
                    Bundle _arg6 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    IBiometricPromptReceiver _arg7 = IBiometricPromptReceiver.Stub.asInterface(data.readStrongBinder());
                    authenticate(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg02 = data.readStrongBinder();
                    String _arg12 = data.readString();
                    cancelAuthentication(_arg02, _arg12);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg03 = data.readStrongBinder();
                    byte[] _arg13 = data.createByteArray();
                    int _arg22 = data.readInt();
                    IFingerprintServiceReceiver _arg32 = IFingerprintServiceReceiver.Stub.asInterface(data.readStrongBinder());
                    int _arg42 = data.readInt();
                    String _arg52 = data.readString();
                    enroll(_arg03, _arg13, _arg22, _arg32, _arg42, _arg52);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg04 = data.readStrongBinder();
                    cancelEnrollment(_arg04);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg05 = data.readStrongBinder();
                    int _arg14 = data.readInt();
                    int _arg23 = data.readInt();
                    int _arg33 = data.readInt();
                    IFingerprintServiceReceiver _arg43 = IFingerprintServiceReceiver.Stub.asInterface(data.readStrongBinder());
                    remove(_arg05, _arg14, _arg23, _arg33, _arg43);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    int _arg15 = data.readInt();
                    String _arg24 = data.readString();
                    rename(_arg06, _arg15, _arg24);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    String _arg16 = data.readString();
                    List<Fingerprint> _result = getEnrolledFingerprints(_arg07, _arg16);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg08 = data.readLong();
                    String _arg17 = data.readString();
                    boolean isHardwareDetected = isHardwareDetected(_arg08, _arg17);
                    reply.writeNoException();
                    reply.writeInt(isHardwareDetected ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg09 = data.readStrongBinder();
                    long _result2 = preEnroll(_arg09);
                    reply.writeNoException();
                    reply.writeLong(_result2);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg010 = data.readStrongBinder();
                    int _result3 = postEnroll(_arg010);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    String _arg18 = data.readString();
                    boolean hasEnrolledFingerprints = hasEnrolledFingerprints(_arg011, _arg18);
                    reply.writeNoException();
                    reply.writeInt(hasEnrolledFingerprints ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    long _result4 = getAuthenticatorId(_arg012);
                    reply.writeNoException();
                    reply.writeLong(_result4);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg013 = data.createByteArray();
                    resetTimeout(_arg013);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    IFingerprintServiceLockoutResetCallback _arg014 = IFingerprintServiceLockoutResetCallback.Stub.asInterface(data.readStrongBinder());
                    addLockoutResetCallback(_arg014);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    setActiveUser(_arg015);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg016 = data.readStrongBinder();
                    int _arg19 = data.readInt();
                    IFingerprintServiceReceiver _arg25 = IFingerprintServiceReceiver.Stub.asInterface(data.readStrongBinder());
                    enumerate(_arg016, _arg19, _arg25);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isClientActive = isClientActive();
                    reply.writeNoException();
                    reply.writeInt(isClientActive ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    IFingerprintClientActiveCallback _arg017 = IFingerprintClientActiveCallback.Stub.asInterface(data.readStrongBinder());
                    addClientActiveCallback(_arg017);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    IFingerprintClientActiveCallback _arg018 = IFingerprintClientActiveCallback.Stub.asInterface(data.readStrongBinder());
                    removeClientActiveCallback(_arg018);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IFingerprintService {
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

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void authenticate(IBinder token, long sessionId, int userId, IFingerprintServiceReceiver receiver, int flags, String opPackageName, Bundle bundle, IBiometricPromptReceiver dialogReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeLong(sessionId);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeString(opPackageName);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(dialogReceiver != null ? dialogReceiver.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void cancelAuthentication(IBinder token, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void enroll(IBinder token, byte[] cryptoToken, int groupId, IFingerprintServiceReceiver receiver, int flags, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeByteArray(cryptoToken);
                    _data.writeInt(groupId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void cancelEnrollment(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void remove(IBinder token, int fingerId, int groupId, int userId, IFingerprintServiceReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(fingerId);
                    _data.writeInt(groupId);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void rename(int fingerId, int groupId, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fingerId);
                    _data.writeInt(groupId);
                    _data.writeString(name);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized List<Fingerprint> getEnrolledFingerprints(int groupId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    List<Fingerprint> _result = _reply.createTypedArrayList(Fingerprint.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized boolean isHardwareDetected(long deviceId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized long preEnroll(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized int postEnroll(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized boolean hasEnrolledFingerprints(int groupId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized long getAuthenticatorId(String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(opPackageName);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void resetTimeout(byte[] cryptoToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(cryptoToken);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void addLockoutResetCallback(IFingerprintServiceLockoutResetCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void setActiveUser(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void enumerate(IBinder token, int userId, IFingerprintServiceReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized boolean isClientActive() throws RemoteException {
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

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void addClientActiveCallback(IFingerprintClientActiveCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintService
            public synchronized void removeClientActiveCallback(IFingerprintClientActiveCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
