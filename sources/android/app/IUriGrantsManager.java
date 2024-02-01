package android.app;

import android.content.pm.ParceledListSlice;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IUriGrantsManager extends IInterface {
    void clearGrantedUriPermissions(String str, int i) throws RemoteException;

    ParceledListSlice getGrantedUriPermissions(String str, int i) throws RemoteException;

    ParceledListSlice getUriPermissions(String str, boolean z, boolean z2) throws RemoteException;

    void grantUriPermissionFromOwner(IBinder iBinder, int i, String str, Uri uri, int i2, int i3, int i4) throws RemoteException;

    void releasePersistableUriPermission(Uri uri, int i, String str, int i2) throws RemoteException;

    void takePersistableUriPermission(Uri uri, int i, String str, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IUriGrantsManager {
        @Override // android.app.IUriGrantsManager
        public void takePersistableUriPermission(Uri uri, int modeFlags, String toPackage, int userId) throws RemoteException {
        }

        @Override // android.app.IUriGrantsManager
        public void releasePersistableUriPermission(Uri uri, int modeFlags, String toPackage, int userId) throws RemoteException {
        }

        @Override // android.app.IUriGrantsManager
        public void grantUriPermissionFromOwner(IBinder owner, int fromUid, String targetPkg, Uri uri, int mode, int sourceUserId, int targetUserId) throws RemoteException {
        }

        @Override // android.app.IUriGrantsManager
        public ParceledListSlice getGrantedUriPermissions(String packageName, int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.IUriGrantsManager
        public void clearGrantedUriPermissions(String packageName, int userId) throws RemoteException {
        }

        @Override // android.app.IUriGrantsManager
        public ParceledListSlice getUriPermissions(String packageName, boolean incoming, boolean persistedOnly) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IUriGrantsManager {
        private static final String DESCRIPTOR = "android.app.IUriGrantsManager";
        static final int TRANSACTION_clearGrantedUriPermissions = 5;
        static final int TRANSACTION_getGrantedUriPermissions = 4;
        static final int TRANSACTION_getUriPermissions = 6;
        static final int TRANSACTION_grantUriPermissionFromOwner = 3;
        static final int TRANSACTION_releasePersistableUriPermission = 2;
        static final int TRANSACTION_takePersistableUriPermission = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUriGrantsManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IUriGrantsManager)) {
                return (IUriGrantsManager) iin;
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
                    return "takePersistableUriPermission";
                case 2:
                    return "releasePersistableUriPermission";
                case 3:
                    return "grantUriPermissionFromOwner";
                case 4:
                    return "getGrantedUriPermissions";
                case 5:
                    return "clearGrantedUriPermissions";
                case 6:
                    return "getUriPermissions";
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
            Uri _arg0;
            Uri _arg02;
            Uri _arg3;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int _arg1 = data.readInt();
                    String _arg2 = data.readString();
                    int _arg32 = data.readInt();
                    takePersistableUriPermission(_arg0, _arg1, _arg2, _arg32);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    int _arg12 = data.readInt();
                    String _arg22 = data.readString();
                    int _arg33 = data.readInt();
                    releasePersistableUriPermission(_arg02, _arg12, _arg22, _arg33);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg03 = data.readStrongBinder();
                    int _arg13 = data.readInt();
                    String _arg23 = data.readString();
                    if (data.readInt() != 0) {
                        _arg3 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    int _arg4 = data.readInt();
                    int _arg5 = data.readInt();
                    int _arg6 = data.readInt();
                    grantUriPermissionFromOwner(_arg03, _arg13, _arg23, _arg3, _arg4, _arg5, _arg6);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    int _arg14 = data.readInt();
                    ParceledListSlice _result = getGrantedUriPermissions(_arg04, _arg14);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    int _arg15 = data.readInt();
                    clearGrantedUriPermissions(_arg05, _arg15);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    boolean _arg16 = data.readInt() != 0;
                    boolean _arg24 = data.readInt() != 0;
                    ParceledListSlice _result2 = getUriPermissions(_arg06, _arg16, _arg24);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IUriGrantsManager {
            public static IUriGrantsManager sDefaultImpl;
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

            @Override // android.app.IUriGrantsManager
            public void takePersistableUriPermission(Uri uri, int modeFlags, String toPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(modeFlags);
                    _data.writeString(toPackage);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().takePersistableUriPermission(uri, modeFlags, toPackage, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IUriGrantsManager
            public void releasePersistableUriPermission(Uri uri, int modeFlags, String toPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(modeFlags);
                    _data.writeString(toPackage);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().releasePersistableUriPermission(uri, modeFlags, toPackage, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IUriGrantsManager
            public void grantUriPermissionFromOwner(IBinder owner, int fromUid, String targetPkg, Uri uri, int mode, int sourceUserId, int targetUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(owner);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(fromUid);
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(targetPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(mode);
                    _data.writeInt(sourceUserId);
                    _data.writeInt(targetUserId);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().grantUriPermissionFromOwner(owner, fromUid, targetPkg, uri, mode, sourceUserId, targetUserId);
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

            @Override // android.app.IUriGrantsManager
            public ParceledListSlice getGrantedUriPermissions(String packageName, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGrantedUriPermissions(packageName, userId);
                    }
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

            @Override // android.app.IUriGrantsManager
            public void clearGrantedUriPermissions(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearGrantedUriPermissions(packageName, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.IUriGrantsManager
            public ParceledListSlice getUriPermissions(String packageName, boolean incoming, boolean persistedOnly) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 1;
                    _data.writeInt(incoming ? 1 : 0);
                    if (!persistedOnly) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUriPermissions(packageName, incoming, persistedOnly);
                    }
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
        }

        public static boolean setDefaultImpl(IUriGrantsManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IUriGrantsManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
