package android.content.pm;

import android.app.IApplicationThread;
import android.content.ComponentName;
import android.content.IntentSender;
import android.content.pm.IOnAppsChangedListener;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.UserHandle;
import java.util.List;
/* loaded from: classes.dex */
public interface ILauncherApps extends IInterface {
    synchronized void addOnAppsChangedListener(String str, IOnAppsChangedListener iOnAppsChangedListener) throws RemoteException;

    synchronized ApplicationInfo getApplicationInfo(String str, String str2, int i, UserHandle userHandle) throws RemoteException;

    synchronized ParceledListSlice getLauncherActivities(String str, String str2, UserHandle userHandle) throws RemoteException;

    synchronized ParceledListSlice getShortcutConfigActivities(String str, String str2, UserHandle userHandle) throws RemoteException;

    synchronized IntentSender getShortcutConfigActivityIntent(String str, ComponentName componentName, UserHandle userHandle) throws RemoteException;

    synchronized ParcelFileDescriptor getShortcutIconFd(String str, String str2, String str3, int i) throws RemoteException;

    synchronized int getShortcutIconResId(String str, String str2, String str3, int i) throws RemoteException;

    synchronized ParceledListSlice getShortcuts(String str, long j, String str2, List list, ComponentName componentName, int i, UserHandle userHandle) throws RemoteException;

    synchronized Bundle getSuspendedPackageLauncherExtras(String str, UserHandle userHandle) throws RemoteException;

    synchronized boolean hasShortcutHostPermission(String str) throws RemoteException;

    synchronized boolean isActivityEnabled(String str, ComponentName componentName, UserHandle userHandle) throws RemoteException;

    synchronized boolean isPackageEnabled(String str, String str2, UserHandle userHandle) throws RemoteException;

    synchronized void pinShortcuts(String str, String str2, List<String> list, UserHandle userHandle) throws RemoteException;

    synchronized void removeOnAppsChangedListener(IOnAppsChangedListener iOnAppsChangedListener) throws RemoteException;

    synchronized ActivityInfo resolveActivity(String str, ComponentName componentName, UserHandle userHandle) throws RemoteException;

    synchronized void showAppDetailsAsUser(IApplicationThread iApplicationThread, String str, ComponentName componentName, Rect rect, Bundle bundle, UserHandle userHandle) throws RemoteException;

    synchronized void startActivityAsUser(IApplicationThread iApplicationThread, String str, ComponentName componentName, Rect rect, Bundle bundle, UserHandle userHandle) throws RemoteException;

    synchronized boolean startShortcut(String str, String str2, String str3, Rect rect, Bundle bundle, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ILauncherApps {
        private static final String DESCRIPTOR = "android.content.pm.ILauncherApps";
        static final int TRANSACTION_addOnAppsChangedListener = 1;
        static final int TRANSACTION_getApplicationInfo = 10;
        static final int TRANSACTION_getLauncherActivities = 3;
        static final int TRANSACTION_getShortcutConfigActivities = 17;
        static final int TRANSACTION_getShortcutConfigActivityIntent = 18;
        static final int TRANSACTION_getShortcutIconFd = 15;
        static final int TRANSACTION_getShortcutIconResId = 14;
        static final int TRANSACTION_getShortcuts = 11;
        static final int TRANSACTION_getSuspendedPackageLauncherExtras = 8;
        static final int TRANSACTION_hasShortcutHostPermission = 16;
        static final int TRANSACTION_isActivityEnabled = 9;
        static final int TRANSACTION_isPackageEnabled = 7;
        static final int TRANSACTION_pinShortcuts = 12;
        static final int TRANSACTION_removeOnAppsChangedListener = 2;
        static final int TRANSACTION_resolveActivity = 4;
        static final int TRANSACTION_showAppDetailsAsUser = 6;
        static final int TRANSACTION_startActivityAsUser = 5;
        static final int TRANSACTION_startShortcut = 13;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ILauncherApps asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILauncherApps)) {
                return (ILauncherApps) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ComponentName _arg1;
            ComponentName _arg2;
            Rect _arg3;
            Bundle _arg4;
            ComponentName _arg22;
            Rect _arg32;
            Bundle _arg42;
            ComponentName _arg12;
            ComponentName _arg43;
            Rect _arg33;
            ComponentName _arg13;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    IOnAppsChangedListener _arg14 = IOnAppsChangedListener.Stub.asInterface(data.readStrongBinder());
                    addOnAppsChangedListener(_arg0, _arg14);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IOnAppsChangedListener _arg02 = IOnAppsChangedListener.Stub.asInterface(data.readStrongBinder());
                    removeOnAppsChangedListener(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    String _arg15 = data.readString();
                    UserHandle _arg23 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    ParceledListSlice _result = getLauncherActivities(_arg03, _arg15, _arg23);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    UserHandle _arg24 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    ActivityInfo _result2 = resolveActivity(_arg04, _arg1, _arg24);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg05 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg16 = data.readString();
                    if (data.readInt() != 0) {
                        ComponentName _arg25 = ComponentName.CREATOR.createFromParcel(data);
                        _arg2 = _arg25;
                    } else {
                        _arg2 = null;
                    }
                    if (data.readInt() != 0) {
                        Rect _arg34 = Rect.CREATOR.createFromParcel(data);
                        _arg3 = _arg34;
                    } else {
                        _arg3 = null;
                    }
                    if (data.readInt() != 0) {
                        Bundle _arg44 = Bundle.CREATOR.createFromParcel(data);
                        _arg4 = _arg44;
                    } else {
                        _arg4 = null;
                    }
                    UserHandle _arg5 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    startActivityAsUser(_arg05, _arg16, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IApplicationThread _arg06 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
                    String _arg17 = data.readString();
                    if (data.readInt() != 0) {
                        ComponentName _arg26 = ComponentName.CREATOR.createFromParcel(data);
                        _arg22 = _arg26;
                    } else {
                        _arg22 = null;
                    }
                    if (data.readInt() != 0) {
                        Rect _arg35 = Rect.CREATOR.createFromParcel(data);
                        _arg32 = _arg35;
                    } else {
                        _arg32 = null;
                    }
                    if (data.readInt() != 0) {
                        Bundle _arg45 = Bundle.CREATOR.createFromParcel(data);
                        _arg42 = _arg45;
                    } else {
                        _arg42 = null;
                    }
                    UserHandle _arg52 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    showAppDetailsAsUser(_arg06, _arg17, _arg22, _arg32, _arg42, _arg52);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _arg18 = data.readString();
                    UserHandle _arg27 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    boolean isPackageEnabled = isPackageEnabled(_arg07, _arg18, _arg27);
                    reply.writeNoException();
                    reply.writeInt(isPackageEnabled ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    UserHandle _arg19 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    Bundle _result3 = getSuspendedPackageLauncherExtras(_arg08, _arg19);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    UserHandle _arg28 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    boolean isActivityEnabled = isActivityEnabled(_arg09, _arg12, _arg28);
                    reply.writeNoException();
                    reply.writeInt(isActivityEnabled ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    String _arg110 = data.readString();
                    int _arg29 = data.readInt();
                    UserHandle _arg36 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    ApplicationInfo _result4 = getApplicationInfo(_arg010, _arg110, _arg29, _arg36);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    long _arg111 = data.readLong();
                    String _arg210 = data.readString();
                    ClassLoader cl = getClass().getClassLoader();
                    List _arg37 = data.readArrayList(cl);
                    if (data.readInt() != 0) {
                        ComponentName _arg46 = ComponentName.CREATOR.createFromParcel(data);
                        _arg43 = _arg46;
                    } else {
                        _arg43 = null;
                    }
                    int _arg53 = data.readInt();
                    UserHandle _arg6 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    ParceledListSlice _result5 = getShortcuts(_arg011, _arg111, _arg210, _arg37, _arg43, _arg53, _arg6);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    String _arg112 = data.readString();
                    List<String> _arg211 = data.createStringArrayList();
                    UserHandle _arg38 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    pinShortcuts(_arg012, _arg112, _arg211, _arg38);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    String _arg113 = data.readString();
                    String _arg212 = data.readString();
                    if (data.readInt() != 0) {
                        Rect _arg39 = Rect.CREATOR.createFromParcel(data);
                        _arg33 = _arg39;
                    } else {
                        _arg33 = null;
                    }
                    Bundle _arg47 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    int _arg54 = data.readInt();
                    boolean startShortcut = startShortcut(_arg013, _arg113, _arg212, _arg33, _arg47, _arg54);
                    reply.writeNoException();
                    reply.writeInt(startShortcut ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    String _arg114 = data.readString();
                    String _arg213 = data.readString();
                    int _arg310 = data.readInt();
                    int _result6 = getShortcutIconResId(_arg014, _arg114, _arg213, _arg310);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    String _arg115 = data.readString();
                    String _arg214 = data.readString();
                    int _arg311 = data.readInt();
                    ParcelFileDescriptor _result7 = getShortcutIconFd(_arg015, _arg115, _arg214, _arg311);
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    boolean hasShortcutHostPermission = hasShortcutHostPermission(_arg016);
                    reply.writeNoException();
                    reply.writeInt(hasShortcutHostPermission ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    String _arg116 = data.readString();
                    UserHandle _arg215 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    ParceledListSlice _result8 = getShortcutConfigActivities(_arg017, _arg116, _arg215);
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    if (data.readInt() != 0) {
                        _arg13 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    UserHandle _arg216 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    IntentSender _result9 = getShortcutConfigActivityIntent(_arg018, _arg13, _arg216);
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ILauncherApps {
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

            @Override // android.content.pm.ILauncherApps
            public synchronized void addOnAppsChangedListener(String callingPackage, IOnAppsChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized void removeOnAppsChangedListener(IOnAppsChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized ParceledListSlice getLauncherActivities(String callingPackage, String packageName, UserHandle user) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
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

            @Override // android.content.pm.ILauncherApps
            public synchronized ActivityInfo resolveActivity(String callingPackage, ComponentName component, UserHandle user) throws RemoteException {
                ActivityInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ActivityInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized void startActivityAsUser(IApplicationThread caller, String callingPackage, ComponentName component, Rect sourceBounds, Bundle opts, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(callingPackage);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sourceBounds != null) {
                        _data.writeInt(1);
                        sourceBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (opts != null) {
                        _data.writeInt(1);
                        opts.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized void showAppDetailsAsUser(IApplicationThread caller, String callingPackage, ComponentName component, Rect sourceBounds, Bundle opts, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(callingPackage);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sourceBounds != null) {
                        _data.writeInt(1);
                        sourceBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (opts != null) {
                        _data.writeInt(1);
                        opts.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized boolean isPackageEnabled(String callingPackage, String packageName, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized Bundle getSuspendedPackageLauncherExtras(String packageName, UserHandle user) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized boolean isActivityEnabled(String callingPackage, ComponentName component, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized ApplicationInfo getApplicationInfo(String callingPackage, String packageName, int flags, UserHandle user) throws RemoteException {
                ApplicationInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ApplicationInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized ParceledListSlice getShortcuts(String callingPackage, long changedSince, String packageName, List shortcutIds, ComponentName componentName, int flags, UserHandle user) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeLong(changedSince);
                    _data.writeString(packageName);
                    _data.writeList(shortcutIds);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
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

            @Override // android.content.pm.ILauncherApps
            public synchronized void pinShortcuts(String callingPackage, String packageName, List<String> shortcutIds, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    _data.writeStringList(shortcutIds);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized boolean startShortcut(String callingPackage, String packageName, String id, Rect sourceBounds, Bundle startActivityOptions, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    _data.writeString(id);
                    if (sourceBounds != null) {
                        _data.writeInt(1);
                        sourceBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (startActivityOptions != null) {
                        _data.writeInt(1);
                        startActivityOptions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized int getShortcutIconResId(String callingPackage, String packageName, String id, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    _data.writeString(id);
                    _data.writeInt(userId);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized ParcelFileDescriptor getShortcutIconFd(String callingPackage, String packageName, String id, int userId) throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    _data.writeString(id);
                    _data.writeInt(userId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.content.pm.ILauncherApps
            public synchronized boolean hasShortcutHostPermission(String callingPackage) throws RemoteException {
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

            @Override // android.content.pm.ILauncherApps
            public synchronized ParceledListSlice getShortcutConfigActivities(String callingPackage, String packageName, UserHandle user) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
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

            @Override // android.content.pm.ILauncherApps
            public synchronized IntentSender getShortcutConfigActivityIntent(String callingPackage, ComponentName component, UserHandle user) throws RemoteException {
                IntentSender _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = IntentSender.CREATOR.createFromParcel(_reply);
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
    }
}
