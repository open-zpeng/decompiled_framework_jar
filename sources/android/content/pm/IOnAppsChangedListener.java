package android.content.pm;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;
/* loaded from: classes.dex */
public interface IOnAppsChangedListener extends IInterface {
    synchronized void onPackageAdded(UserHandle userHandle, String str) throws RemoteException;

    synchronized void onPackageChanged(UserHandle userHandle, String str) throws RemoteException;

    synchronized void onPackageRemoved(UserHandle userHandle, String str) throws RemoteException;

    synchronized void onPackagesAvailable(UserHandle userHandle, String[] strArr, boolean z) throws RemoteException;

    synchronized void onPackagesSuspended(UserHandle userHandle, String[] strArr, Bundle bundle) throws RemoteException;

    synchronized void onPackagesUnavailable(UserHandle userHandle, String[] strArr, boolean z) throws RemoteException;

    synchronized void onPackagesUnsuspended(UserHandle userHandle, String[] strArr) throws RemoteException;

    synchronized void onShortcutChanged(UserHandle userHandle, String str, ParceledListSlice parceledListSlice) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IOnAppsChangedListener {
        private static final String DESCRIPTOR = "android.content.pm.IOnAppsChangedListener";
        static final int TRANSACTION_onPackageAdded = 2;
        static final int TRANSACTION_onPackageChanged = 3;
        static final int TRANSACTION_onPackageRemoved = 1;
        static final int TRANSACTION_onPackagesAvailable = 4;
        static final int TRANSACTION_onPackagesSuspended = 6;
        static final int TRANSACTION_onPackagesUnavailable = 5;
        static final int TRANSACTION_onPackagesUnsuspended = 7;
        static final int TRANSACTION_onShortcutChanged = 8;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IOnAppsChangedListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IOnAppsChangedListener)) {
                return (IOnAppsChangedListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg2;
            UserHandle _arg0;
            UserHandle _arg02;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    UserHandle _arg03 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    UserHandle _arg04 = _arg03;
                    String _arg1 = data.readString();
                    onPackageRemoved(_arg04, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    UserHandle _arg05 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    UserHandle _arg06 = _arg05;
                    String _arg12 = data.readString();
                    onPackageAdded(_arg06, _arg12);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    UserHandle _arg07 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    UserHandle _arg08 = _arg07;
                    String _arg13 = data.readString();
                    onPackageChanged(_arg08, _arg13);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    UserHandle _arg09 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    String[] _arg14 = data.createStringArray();
                    _arg2 = data.readInt() != 0;
                    onPackagesAvailable(_arg09, _arg14, _arg2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    UserHandle _arg010 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    String[] _arg15 = data.createStringArray();
                    _arg2 = data.readInt() != 0;
                    onPackagesUnavailable(_arg010, _arg15, _arg2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    String[] _arg16 = data.createStringArray();
                    Bundle _arg22 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onPackagesSuspended(_arg0, _arg16, _arg22);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    UserHandle _arg011 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    UserHandle _arg012 = _arg011;
                    String[] _arg17 = data.createStringArray();
                    onPackagesUnsuspended(_arg012, _arg17);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    String _arg18 = data.readString();
                    ParceledListSlice _arg23 = data.readInt() != 0 ? ParceledListSlice.CREATOR.createFromParcel(data) : null;
                    onShortcutChanged(_arg02, _arg18, _arg23);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IOnAppsChangedListener {
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

            @Override // android.content.pm.IOnAppsChangedListener
            public synchronized void onPackageRemoved(UserHandle user, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IOnAppsChangedListener
            public synchronized void onPackageAdded(UserHandle user, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IOnAppsChangedListener
            public synchronized void onPackageChanged(UserHandle user, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IOnAppsChangedListener
            public synchronized void onPackagesAvailable(UserHandle user, String[] packageNames, boolean replacing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packageNames);
                    _data.writeInt(replacing ? 1 : 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IOnAppsChangedListener
            public synchronized void onPackagesUnavailable(UserHandle user, String[] packageNames, boolean replacing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packageNames);
                    _data.writeInt(replacing ? 1 : 0);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IOnAppsChangedListener
            public synchronized void onPackagesSuspended(UserHandle user, String[] packageNames, Bundle launcherExtras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packageNames);
                    if (launcherExtras != null) {
                        _data.writeInt(1);
                        launcherExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IOnAppsChangedListener
            public synchronized void onPackagesUnsuspended(UserHandle user, String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packageNames);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.content.pm.IOnAppsChangedListener
            public synchronized void onShortcutChanged(UserHandle user, String packageName, ParceledListSlice shortcuts) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (shortcuts != null) {
                        _data.writeInt(1);
                        shortcuts.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
