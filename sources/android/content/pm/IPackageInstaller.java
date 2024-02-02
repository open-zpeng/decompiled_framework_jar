package android.content.pm;

import android.content.IntentSender;
import android.content.pm.IPackageInstallerCallback;
import android.content.pm.IPackageInstallerSession;
import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IPackageInstaller extends IInterface {
    synchronized void abandonSession(int i) throws RemoteException;

    synchronized int createSession(PackageInstaller.SessionParams sessionParams, String str, int i) throws RemoteException;

    synchronized ParceledListSlice getAllSessions(int i) throws RemoteException;

    synchronized ParceledListSlice getMySessions(String str, int i) throws RemoteException;

    synchronized PackageInstaller.SessionInfo getSessionInfo(int i) throws RemoteException;

    synchronized IPackageInstallerSession openSession(int i) throws RemoteException;

    synchronized void registerCallback(IPackageInstallerCallback iPackageInstallerCallback, int i) throws RemoteException;

    synchronized void setPermissionsResult(int i, boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void uninstall(VersionedPackage versionedPackage, String str, int i, IntentSender intentSender, int i2) throws RemoteException;

    synchronized void unregisterCallback(IPackageInstallerCallback iPackageInstallerCallback) throws RemoteException;

    synchronized void updateSessionAppIcon(int i, Bitmap bitmap) throws RemoteException;

    synchronized void updateSessionAppLabel(int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPackageInstaller {
        private static final String DESCRIPTOR = "android.content.pm.IPackageInstaller";
        static final int TRANSACTION_abandonSession = 4;
        static final int TRANSACTION_createSession = 1;
        static final int TRANSACTION_getAllSessions = 7;
        static final int TRANSACTION_getMySessions = 8;
        static final int TRANSACTION_getSessionInfo = 6;
        static final int TRANSACTION_openSession = 5;
        static final int TRANSACTION_registerCallback = 9;
        static final int TRANSACTION_setPermissionsResult = 12;
        static final int TRANSACTION_uninstall = 11;
        static final int TRANSACTION_unregisterCallback = 10;
        static final int TRANSACTION_updateSessionAppIcon = 2;
        static final int TRANSACTION_updateSessionAppLabel = 3;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IPackageInstaller asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IPackageInstaller)) {
                return (IPackageInstaller) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            VersionedPackage _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    PackageInstaller.SessionParams _arg02 = data.readInt() != 0 ? PackageInstaller.SessionParams.CREATOR.createFromParcel(data) : null;
                    PackageInstaller.SessionParams _arg03 = _arg02;
                    String _arg1 = data.readString();
                    int _arg2 = data.readInt();
                    int _result = createSession(_arg03, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    Bitmap _arg12 = data.readInt() != 0 ? Bitmap.CREATOR.createFromParcel(data) : null;
                    updateSessionAppIcon(_arg04, _arg12);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    String _arg13 = data.readString();
                    updateSessionAppLabel(_arg05, _arg13);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    abandonSession(_arg06);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    IPackageInstallerSession _result2 = openSession(_arg07);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result2 != null ? _result2.asBinder() : null);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    PackageInstaller.SessionInfo _result3 = getSessionInfo(_arg08);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    ParceledListSlice _result4 = getAllSessions(_arg09);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    int _arg14 = data.readInt();
                    ParceledListSlice _result5 = getMySessions(_arg010, _arg14);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IPackageInstallerCallback _arg011 = IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg15 = data.readInt();
                    registerCallback(_arg011, _arg15);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IPackageInstallerCallback _arg012 = IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterCallback(_arg012);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        VersionedPackage _arg013 = VersionedPackage.CREATOR.createFromParcel(data);
                        _arg0 = _arg013;
                    } else {
                        _arg0 = null;
                    }
                    String _arg16 = data.readString();
                    int _arg22 = data.readInt();
                    IntentSender _arg3 = data.readInt() != 0 ? IntentSender.CREATOR.createFromParcel(data) : null;
                    int _arg4 = data.readInt();
                    uninstall(_arg0, _arg16, _arg22, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    boolean _arg17 = data.readInt() != 0;
                    setPermissionsResult(_arg014, _arg17);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IPackageInstaller {
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

            @Override // android.content.pm.IPackageInstaller
            public synchronized int createSession(PackageInstaller.SessionParams params, String installerPackageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(installerPackageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstaller
            public synchronized void updateSessionAppIcon(int sessionId, Bitmap appIcon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (appIcon != null) {
                        _data.writeInt(1);
                        appIcon.writeToParcel(_data, 0);
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

            @Override // android.content.pm.IPackageInstaller
            public synchronized void updateSessionAppLabel(int sessionId, String appLabel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeString(appLabel);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstaller
            public synchronized void abandonSession(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstaller
            public synchronized IPackageInstallerSession openSession(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    IPackageInstallerSession _result = IPackageInstallerSession.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstaller
            public synchronized PackageInstaller.SessionInfo getSessionInfo(int sessionId) throws RemoteException {
                PackageInstaller.SessionInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PackageInstaller.SessionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstaller
            public synchronized ParceledListSlice getAllSessions(int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstaller
            public synchronized ParceledListSlice getMySessions(String installerPackageName, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(installerPackageName);
                    _data.writeInt(userId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstaller
            public synchronized void registerCallback(IPackageInstallerCallback callback, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstaller
            public synchronized void unregisterCallback(IPackageInstallerCallback callback) throws RemoteException {
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

            public synchronized void uninstall(VersionedPackage versionedPackage, String callerPackageName, int flags, IntentSender statusReceiver, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (versionedPackage != null) {
                        _data.writeInt(1);
                        versionedPackage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackageName);
                    _data.writeInt(flags);
                    if (statusReceiver != null) {
                        _data.writeInt(1);
                        statusReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IPackageInstaller
            public synchronized void setPermissionsResult(int sessionId, boolean accepted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(accepted ? 1 : 0);
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
