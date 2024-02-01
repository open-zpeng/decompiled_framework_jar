package android.app.trust;

import android.app.trust.ITrustListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface ITrustManager extends IInterface {
    synchronized void clearAllFingerprints() throws RemoteException;

    synchronized boolean isDeviceLocked(int i) throws RemoteException;

    synchronized boolean isDeviceSecure(int i) throws RemoteException;

    synchronized boolean isTrustUsuallyManaged(int i) throws RemoteException;

    synchronized void registerTrustListener(ITrustListener iTrustListener) throws RemoteException;

    synchronized void reportEnabledTrustAgentsChanged(int i) throws RemoteException;

    synchronized void reportKeyguardShowingChanged() throws RemoteException;

    synchronized void reportUnlockAttempt(boolean z, int i) throws RemoteException;

    synchronized void reportUnlockLockout(int i, int i2) throws RemoteException;

    synchronized void setDeviceLockedForUser(int i, boolean z) throws RemoteException;

    synchronized void unlockedByFingerprintForUser(int i) throws RemoteException;

    synchronized void unregisterTrustListener(ITrustListener iTrustListener) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITrustManager {
        private static final String DESCRIPTOR = "android.app.trust.ITrustManager";
        static final int TRANSACTION_clearAllFingerprints = 12;
        static final int TRANSACTION_isDeviceLocked = 8;
        static final int TRANSACTION_isDeviceSecure = 9;
        static final int TRANSACTION_isTrustUsuallyManaged = 10;
        static final int TRANSACTION_registerTrustListener = 4;
        static final int TRANSACTION_reportEnabledTrustAgentsChanged = 3;
        static final int TRANSACTION_reportKeyguardShowingChanged = 6;
        static final int TRANSACTION_reportUnlockAttempt = 1;
        static final int TRANSACTION_reportUnlockLockout = 2;
        static final int TRANSACTION_setDeviceLockedForUser = 7;
        static final int TRANSACTION_unlockedByFingerprintForUser = 11;
        static final int TRANSACTION_unregisterTrustListener = 5;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ITrustManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITrustManager)) {
                return (ITrustManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg1;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    reportUnlockAttempt(_arg1, data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    reportUnlockLockout(_arg0, data.readInt());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    reportEnabledTrustAgentsChanged(_arg02);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ITrustListener _arg03 = ITrustListener.Stub.asInterface(data.readStrongBinder());
                    registerTrustListener(_arg03);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    ITrustListener _arg04 = ITrustListener.Stub.asInterface(data.readStrongBinder());
                    unregisterTrustListener(_arg04);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    reportKeyguardShowingChanged();
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setDeviceLockedForUser(_arg05, _arg1);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    boolean isDeviceLocked = isDeviceLocked(_arg06);
                    reply.writeNoException();
                    reply.writeInt(isDeviceLocked ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    boolean isDeviceSecure = isDeviceSecure(_arg07);
                    reply.writeNoException();
                    reply.writeInt(isDeviceSecure ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    boolean isTrustUsuallyManaged = isTrustUsuallyManaged(_arg08);
                    reply.writeNoException();
                    reply.writeInt(isTrustUsuallyManaged ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    unlockedByFingerprintForUser(_arg09);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    clearAllFingerprints();
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ITrustManager {
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

            @Override // android.app.trust.ITrustManager
            public synchronized void reportUnlockAttempt(boolean successful, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(successful ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized void reportUnlockLockout(int timeoutMs, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeoutMs);
                    _data.writeInt(userId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized void reportEnabledTrustAgentsChanged(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized void registerTrustListener(ITrustListener trustListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(trustListener != null ? trustListener.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized void unregisterTrustListener(ITrustListener trustListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(trustListener != null ? trustListener.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized void reportKeyguardShowingChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized void setDeviceLockedForUser(int userId, boolean locked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(locked ? 1 : 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized boolean isDeviceLocked(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized boolean isDeviceSecure(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized boolean isTrustUsuallyManaged(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized void unlockedByFingerprintForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.trust.ITrustManager
            public synchronized void clearAllFingerprints() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
