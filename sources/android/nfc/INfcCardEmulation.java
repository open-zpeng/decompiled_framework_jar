package android.nfc;

import android.content.ComponentName;
import android.nfc.cardemulation.AidGroup;
import android.nfc.cardemulation.ApduServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes2.dex */
public interface INfcCardEmulation extends IInterface {
    synchronized AidGroup getAidGroupForService(int i, ComponentName componentName, String str) throws RemoteException;

    synchronized List<ApduServiceInfo> getServices(int i, String str) throws RemoteException;

    synchronized boolean isDefaultServiceForAid(int i, ComponentName componentName, String str) throws RemoteException;

    synchronized boolean isDefaultServiceForCategory(int i, ComponentName componentName, String str) throws RemoteException;

    synchronized boolean registerAidGroupForService(int i, ComponentName componentName, AidGroup aidGroup) throws RemoteException;

    synchronized boolean removeAidGroupForService(int i, ComponentName componentName, String str) throws RemoteException;

    synchronized boolean setDefaultForNextTap(int i, ComponentName componentName) throws RemoteException;

    synchronized boolean setDefaultServiceForCategory(int i, ComponentName componentName, String str) throws RemoteException;

    synchronized boolean setPreferredService(ComponentName componentName) throws RemoteException;

    synchronized boolean supportsAidPrefixRegistration() throws RemoteException;

    synchronized boolean unsetPreferredService() throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements INfcCardEmulation {
        private static final String DESCRIPTOR = "android.nfc.INfcCardEmulation";
        static final int TRANSACTION_getAidGroupForService = 6;
        static final int TRANSACTION_getServices = 8;
        static final int TRANSACTION_isDefaultServiceForAid = 2;
        static final int TRANSACTION_isDefaultServiceForCategory = 1;
        static final int TRANSACTION_registerAidGroupForService = 5;
        static final int TRANSACTION_removeAidGroupForService = 7;
        static final int TRANSACTION_setDefaultForNextTap = 4;
        static final int TRANSACTION_setDefaultServiceForCategory = 3;
        static final int TRANSACTION_setPreferredService = 9;
        static final int TRANSACTION_supportsAidPrefixRegistration = 11;
        static final int TRANSACTION_unsetPreferredService = 10;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized INfcCardEmulation asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof INfcCardEmulation)) {
                return (INfcCardEmulation) iin;
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
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    ComponentName _arg12 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg2 = data.readString();
                    boolean isDefaultServiceForCategory = isDefaultServiceForCategory(_arg0, _arg12, _arg2);
                    reply.writeNoException();
                    reply.writeInt(isDefaultServiceForCategory ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    ComponentName _arg13 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg22 = data.readString();
                    boolean isDefaultServiceForAid = isDefaultServiceForAid(_arg02, _arg13, _arg22);
                    reply.writeNoException();
                    reply.writeInt(isDefaultServiceForAid ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    ComponentName _arg14 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg23 = data.readString();
                    boolean defaultServiceForCategory = setDefaultServiceForCategory(_arg03, _arg14, _arg23);
                    reply.writeNoException();
                    reply.writeInt(defaultServiceForCategory ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    ComponentName _arg15 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean defaultForNextTap = setDefaultForNextTap(_arg04, _arg15);
                    reply.writeNoException();
                    reply.writeInt(defaultForNextTap ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    AidGroup _arg24 = data.readInt() != 0 ? AidGroup.CREATOR.createFromParcel(data) : null;
                    boolean registerAidGroupForService = registerAidGroupForService(_arg05, _arg1, _arg24);
                    reply.writeNoException();
                    reply.writeInt(registerAidGroupForService ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    ComponentName _arg16 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg25 = data.readString();
                    AidGroup _result = getAidGroupForService(_arg06, _arg16, _arg25);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    ComponentName _arg17 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    String _arg26 = data.readString();
                    boolean removeAidGroupForService = removeAidGroupForService(_arg07, _arg17, _arg26);
                    reply.writeNoException();
                    reply.writeInt(removeAidGroupForService ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    String _arg18 = data.readString();
                    List<ApduServiceInfo> _result2 = getServices(_arg08, _arg18);
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg09 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean preferredService = setPreferredService(_arg09);
                    reply.writeNoException();
                    reply.writeInt(preferredService ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unsetPreferredService = unsetPreferredService();
                    reply.writeNoException();
                    reply.writeInt(unsetPreferredService ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    boolean supportsAidPrefixRegistration = supportsAidPrefixRegistration();
                    reply.writeNoException();
                    reply.writeInt(supportsAidPrefixRegistration ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements INfcCardEmulation {
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

            @Override // android.nfc.INfcCardEmulation
            public synchronized boolean isDefaultServiceForCategory(int userHandle, ComponentName service, String category) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(category);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nfc.INfcCardEmulation
            public synchronized boolean isDefaultServiceForAid(int userHandle, ComponentName service, String aid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(aid);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nfc.INfcCardEmulation
            public synchronized boolean setDefaultServiceForCategory(int userHandle, ComponentName service, String category) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(category);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nfc.INfcCardEmulation
            public synchronized boolean setDefaultForNextTap(int userHandle, ComponentName service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nfc.INfcCardEmulation
            public synchronized boolean registerAidGroupForService(int userHandle, ComponentName service, AidGroup aidGroup) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (aidGroup != null) {
                        _data.writeInt(1);
                        aidGroup.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nfc.INfcCardEmulation
            public synchronized AidGroup getAidGroupForService(int userHandle, ComponentName service, String category) throws RemoteException {
                AidGroup _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(category);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = AidGroup.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nfc.INfcCardEmulation
            public synchronized boolean removeAidGroupForService(int userHandle, ComponentName service, String category) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(category);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nfc.INfcCardEmulation
            public synchronized List<ApduServiceInfo> getServices(int userHandle, String category) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeString(category);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    List<ApduServiceInfo> _result = _reply.createTypedArrayList(ApduServiceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.nfc.INfcCardEmulation
            public synchronized boolean setPreferredService(ComponentName service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
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

            @Override // android.nfc.INfcCardEmulation
            public synchronized boolean unsetPreferredService() throws RemoteException {
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

            @Override // android.nfc.INfcCardEmulation
            public synchronized boolean supportsAidPrefixRegistration() throws RemoteException {
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
        }
    }
}
