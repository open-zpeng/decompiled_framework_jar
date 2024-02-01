package android.app;

import android.app.IWallpaperManagerCallback;
import android.content.ComponentName;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IWallpaperManager extends IInterface {
    synchronized void clearWallpaper(String str, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getHeightHint() throws RemoteException;

    synchronized String getName() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ParcelFileDescriptor getWallpaper(String str, IWallpaperManagerCallback iWallpaperManagerCallback, int i, Bundle bundle, int i2) throws RemoteException;

    synchronized WallpaperColors getWallpaperColors(int i, int i2) throws RemoteException;

    synchronized int getWallpaperIdForUser(int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    WallpaperInfo getWallpaperInfo(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getWidthHint() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean hasNamedWallpaper(String str) throws RemoteException;

    synchronized boolean isSetWallpaperAllowed(String str) throws RemoteException;

    synchronized boolean isWallpaperBackupEligible(int i, int i2) throws RemoteException;

    synchronized boolean isWallpaperSupported(String str) throws RemoteException;

    synchronized void registerWallpaperColorsCallback(IWallpaperManagerCallback iWallpaperManagerCallback, int i) throws RemoteException;

    synchronized void setDimensionHints(int i, int i2, String str) throws RemoteException;

    synchronized void setDisplayPadding(Rect rect, String str) throws RemoteException;

    synchronized void setInAmbientMode(boolean z, boolean z2) throws RemoteException;

    synchronized boolean setLockWallpaperCallback(IWallpaperManagerCallback iWallpaperManagerCallback) throws RemoteException;

    synchronized ParcelFileDescriptor setWallpaper(String str, String str2, Rect rect, boolean z, Bundle bundle, int i, IWallpaperManagerCallback iWallpaperManagerCallback, int i2) throws RemoteException;

    private protected void setWallpaperComponent(ComponentName componentName) throws RemoteException;

    synchronized void setWallpaperComponentChecked(ComponentName componentName, String str, int i) throws RemoteException;

    synchronized void settingsRestored() throws RemoteException;

    synchronized void unregisterWallpaperColorsCallback(IWallpaperManagerCallback iWallpaperManagerCallback, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IWallpaperManager {
        private static final String DESCRIPTOR = "android.app.IWallpaperManager";
        static final int TRANSACTION_clearWallpaper = 7;
        static final int TRANSACTION_getHeightHint = 11;
        static final int TRANSACTION_getName = 13;
        static final int TRANSACTION_getWallpaper = 4;
        static final int TRANSACTION_getWallpaperColors = 19;
        static final int TRANSACTION_getWallpaperIdForUser = 5;
        static final int TRANSACTION_getWallpaperInfo = 6;
        static final int TRANSACTION_getWidthHint = 10;
        static final int TRANSACTION_hasNamedWallpaper = 8;
        static final int TRANSACTION_isSetWallpaperAllowed = 16;
        static final int TRANSACTION_isWallpaperBackupEligible = 17;
        static final int TRANSACTION_isWallpaperSupported = 15;
        static final int TRANSACTION_registerWallpaperColorsCallback = 20;
        static final int TRANSACTION_setDimensionHints = 9;
        static final int TRANSACTION_setDisplayPadding = 12;
        static final int TRANSACTION_setInAmbientMode = 22;
        static final int TRANSACTION_setLockWallpaperCallback = 18;
        static final int TRANSACTION_setWallpaper = 1;
        static final int TRANSACTION_setWallpaperComponent = 3;
        static final int TRANSACTION_setWallpaperComponentChecked = 2;
        static final int TRANSACTION_settingsRestored = 14;
        static final int TRANSACTION_unregisterWallpaperColorsCallback = 21;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IWallpaperManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWallpaperManager)) {
                return (IWallpaperManager) iin;
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
                    String _arg1 = data.readString();
                    Rect _arg2 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
                    boolean _arg3 = data.readInt() != 0;
                    Bundle _arg4 = new Bundle();
                    int _arg5 = data.readInt();
                    IWallpaperManagerCallback _arg6 = IWallpaperManagerCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg7 = data.readInt();
                    ParcelFileDescriptor _result = setWallpaper(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    reply.writeInt(1);
                    _arg4.writeToParcel(reply, 1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg02 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg12 = data.readString();
                    int _arg22 = data.readInt();
                    setWallpaperComponentChecked(_arg02, _arg12, _arg22);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg03 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    setWallpaperComponent(_arg03);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    IWallpaperManagerCallback _arg13 = IWallpaperManagerCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg23 = data.readInt();
                    Bundle _arg32 = new Bundle();
                    ParcelFileDescriptor _result2 = getWallpaper(_arg04, _arg13, _arg23, _arg32, data.readInt());
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    reply.writeInt(1);
                    _arg32.writeToParcel(reply, 1);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    int _arg14 = data.readInt();
                    int _result3 = getWallpaperIdForUser(_arg05, _arg14);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    WallpaperInfo _result4 = getWallpaperInfo(_arg06);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    int _arg15 = data.readInt();
                    int _arg24 = data.readInt();
                    clearWallpaper(_arg07, _arg15, _arg24);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    boolean hasNamedWallpaper = hasNamedWallpaper(_arg08);
                    reply.writeNoException();
                    reply.writeInt(hasNamedWallpaper ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    int _arg16 = data.readInt();
                    String _arg25 = data.readString();
                    setDimensionHints(_arg09, _arg16, _arg25);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _result5 = getWidthHint();
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _result6 = getHeightHint();
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    Rect _arg010 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
                    String _arg17 = data.readString();
                    setDisplayPadding(_arg010, _arg17);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _result7 = getName();
                    reply.writeNoException();
                    reply.writeString(_result7);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    settingsRestored();
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    boolean isWallpaperSupported = isWallpaperSupported(_arg011);
                    reply.writeNoException();
                    reply.writeInt(isWallpaperSupported ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    boolean isSetWallpaperAllowed = isSetWallpaperAllowed(_arg012);
                    reply.writeNoException();
                    reply.writeInt(isSetWallpaperAllowed ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    int _arg18 = data.readInt();
                    boolean isWallpaperBackupEligible = isWallpaperBackupEligible(_arg013, _arg18);
                    reply.writeNoException();
                    reply.writeInt(isWallpaperBackupEligible ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    IWallpaperManagerCallback _arg014 = IWallpaperManagerCallback.Stub.asInterface(data.readStrongBinder());
                    boolean lockWallpaperCallback = setLockWallpaperCallback(_arg014);
                    reply.writeNoException();
                    reply.writeInt(lockWallpaperCallback ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    int _arg19 = data.readInt();
                    WallpaperColors _result8 = getWallpaperColors(_arg015, _arg19);
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    IWallpaperManagerCallback _arg016 = IWallpaperManagerCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg110 = data.readInt();
                    registerWallpaperColorsCallback(_arg016, _arg110);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    IWallpaperManagerCallback _arg017 = IWallpaperManagerCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg111 = data.readInt();
                    unregisterWallpaperColorsCallback(_arg017, _arg111);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg018 = data.readInt() != 0;
                    boolean _arg112 = data.readInt() != 0;
                    setInAmbientMode(_arg018, _arg112);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IWallpaperManager {
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

            @Override // android.app.IWallpaperManager
            public synchronized ParcelFileDescriptor setWallpaper(String name, String callingPackage, Rect cropHint, boolean allowBackup, Bundle extras, int which, IWallpaperManagerCallback completion, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeString(callingPackage);
                    if (cropHint != null) {
                        _data.writeInt(1);
                        cropHint.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(allowBackup ? 1 : 0);
                    _data.writeInt(which);
                    ParcelFileDescriptor _result = null;
                    _data.writeStrongBinder(completion != null ? completion.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    }
                    ParcelFileDescriptor _result2 = _result;
                    if (_reply.readInt() != 0) {
                        extras.readFromParcel(_reply);
                    }
                    return _result2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized void setWallpaperComponentChecked(ComponentName name, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setWallpaperComponent(ComponentName name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized ParcelFileDescriptor getWallpaper(String callingPkg, IWallpaperManagerCallback cb, int which, Bundle outParams, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    ParcelFileDescriptor _result = null;
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    }
                    if (_reply.readInt() != 0) {
                        outParams.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized int getWallpaperIdForUser(int which, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized WallpaperInfo getWallpaperInfo(int userId) throws RemoteException {
                WallpaperInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = WallpaperInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized void clearWallpaper(String callingPackage, int which, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean hasNamedWallpaper(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized void setDimensionHints(int width, int height, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getWidthHint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getHeightHint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized void setDisplayPadding(Rect padding, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (padding != null) {
                        _data.writeInt(1);
                        padding.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized String getName() throws RemoteException {
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

            @Override // android.app.IWallpaperManager
            public synchronized void settingsRestored() throws RemoteException {
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

            @Override // android.app.IWallpaperManager
            public synchronized boolean isWallpaperSupported(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized boolean isSetWallpaperAllowed(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized boolean isWallpaperBackupEligible(int which, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized boolean setLockWallpaperCallback(IWallpaperManagerCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized WallpaperColors getWallpaperColors(int which, int userId) throws RemoteException {
                WallpaperColors _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    _data.writeInt(userId);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = WallpaperColors.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized void registerWallpaperColorsCallback(IWallpaperManagerCallback cb, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized void unregisterWallpaperColorsCallback(IWallpaperManagerCallback cb, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IWallpaperManager
            public synchronized void setInAmbientMode(boolean inAmbientMode, boolean animated) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(inAmbientMode ? 1 : 0);
                    _data.writeInt(animated ? 1 : 0);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
