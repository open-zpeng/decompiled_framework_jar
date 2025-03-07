package com.android.internal.app;

import android.content.pm.PackageInfoLite;
import android.content.res.ObbInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.os.IParcelFileDescriptorFactory;

/* loaded from: classes3.dex */
public interface IMediaContainerService extends IInterface {
    long calculateInstalledSize(String str, String str2) throws RemoteException;

    int copyPackage(String str, IParcelFileDescriptorFactory iParcelFileDescriptorFactory) throws RemoteException;

    PackageInfoLite getMinimalPackageInfo(String str, int i, String str2) throws RemoteException;

    ObbInfo getObbInfo(String str) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IMediaContainerService {
        @Override // com.android.internal.app.IMediaContainerService
        public int copyPackage(String packagePath, IParcelFileDescriptorFactory target) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.app.IMediaContainerService
        public PackageInfoLite getMinimalPackageInfo(String packagePath, int flags, String abiOverride) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.IMediaContainerService
        public ObbInfo getObbInfo(String filename) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.app.IMediaContainerService
        public long calculateInstalledSize(String packagePath, String abiOverride) throws RemoteException {
            return 0L;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IMediaContainerService {
        private static final String DESCRIPTOR = "com.android.internal.app.IMediaContainerService";
        static final int TRANSACTION_calculateInstalledSize = 4;
        static final int TRANSACTION_copyPackage = 1;
        static final int TRANSACTION_getMinimalPackageInfo = 2;
        static final int TRANSACTION_getObbInfo = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMediaContainerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMediaContainerService)) {
                return (IMediaContainerService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode != 2) {
                    if (transactionCode != 3) {
                        if (transactionCode == 4) {
                            return "calculateInstalledSize";
                        }
                        return null;
                    }
                    return "getObbInfo";
                }
                return "getMinimalPackageInfo";
            }
            return "copyPackage";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                String _arg0 = data.readString();
                IParcelFileDescriptorFactory _arg1 = IParcelFileDescriptorFactory.Stub.asInterface(data.readStrongBinder());
                int _result = copyPackage(_arg0, _arg1);
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                String _arg02 = data.readString();
                int _arg12 = data.readInt();
                String _arg2 = data.readString();
                PackageInfoLite _result2 = getMinimalPackageInfo(_arg02, _arg12, _arg2);
                reply.writeNoException();
                if (_result2 != null) {
                    reply.writeInt(1);
                    _result2.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                String _arg03 = data.readString();
                ObbInfo _result3 = getObbInfo(_arg03);
                reply.writeNoException();
                if (_result3 != null) {
                    reply.writeInt(1);
                    _result3.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code != 4) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                String _arg04 = data.readString();
                String _arg13 = data.readString();
                long _result4 = calculateInstalledSize(_arg04, _arg13);
                reply.writeNoException();
                reply.writeLong(_result4);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IMediaContainerService {
            public static IMediaContainerService sDefaultImpl;
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

            @Override // com.android.internal.app.IMediaContainerService
            public int copyPackage(String packagePath, IParcelFileDescriptorFactory target) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packagePath);
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().copyPackage(packagePath, target);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IMediaContainerService
            public PackageInfoLite getMinimalPackageInfo(String packagePath, int flags, String abiOverride) throws RemoteException {
                PackageInfoLite _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packagePath);
                    _data.writeInt(flags);
                    _data.writeString(abiOverride);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMinimalPackageInfo(packagePath, flags, abiOverride);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PackageInfoLite.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IMediaContainerService
            public ObbInfo getObbInfo(String filename) throws RemoteException {
                ObbInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(filename);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getObbInfo(filename);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ObbInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IMediaContainerService
            public long calculateInstalledSize(String packagePath, String abiOverride) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packagePath);
                    _data.writeString(abiOverride);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().calculateInstalledSize(packagePath, abiOverride);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IMediaContainerService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IMediaContainerService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
