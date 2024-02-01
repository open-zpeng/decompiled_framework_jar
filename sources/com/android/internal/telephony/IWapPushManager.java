package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes3.dex */
public interface IWapPushManager extends IInterface {
    @UnsupportedAppUsage
    boolean addPackage(String str, String str2, String str3, String str4, int i, boolean z, boolean z2) throws RemoteException;

    @UnsupportedAppUsage
    boolean deletePackage(String str, String str2, String str3, String str4) throws RemoteException;

    int processMessage(String str, String str2, Intent intent) throws RemoteException;

    @UnsupportedAppUsage
    boolean updatePackage(String str, String str2, String str3, String str4, int i, boolean z, boolean z2) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IWapPushManager {
        @Override // com.android.internal.telephony.IWapPushManager
        public int processMessage(String app_id, String content_type, Intent intent) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.telephony.IWapPushManager
        public boolean addPackage(String x_app_id, String content_type, String package_name, String class_name, int app_type, boolean need_signature, boolean further_processing) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IWapPushManager
        public boolean updatePackage(String x_app_id, String content_type, String package_name, String class_name, int app_type, boolean need_signature, boolean further_processing) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.IWapPushManager
        public boolean deletePackage(String x_app_id, String content_type, String package_name, String class_name) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IWapPushManager {
        private static final String DESCRIPTOR = "com.android.internal.telephony.IWapPushManager";
        static final int TRANSACTION_addPackage = 2;
        static final int TRANSACTION_deletePackage = 4;
        static final int TRANSACTION_processMessage = 1;
        static final int TRANSACTION_updatePackage = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWapPushManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWapPushManager)) {
                return (IWapPushManager) iin;
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
                            return "deletePackage";
                        }
                        return null;
                    }
                    return "updatePackage";
                }
                return "addPackage";
            }
            return "processMessage";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Intent _arg2;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                String _arg0 = data.readString();
                String _arg1 = data.readString();
                if (data.readInt() != 0) {
                    _arg2 = Intent.CREATOR.createFromParcel(data);
                } else {
                    _arg2 = null;
                }
                int _result = processMessage(_arg0, _arg1, _arg2);
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                String _arg02 = data.readString();
                String _arg12 = data.readString();
                String _arg22 = data.readString();
                String _arg3 = data.readString();
                int _arg4 = data.readInt();
                boolean _arg5 = data.readInt() != 0;
                boolean _arg6 = data.readInt() != 0;
                boolean addPackage = addPackage(_arg02, _arg12, _arg22, _arg3, _arg4, _arg5, _arg6);
                reply.writeNoException();
                reply.writeInt(addPackage ? 1 : 0);
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                String _arg03 = data.readString();
                String _arg13 = data.readString();
                String _arg23 = data.readString();
                String _arg32 = data.readString();
                int _arg42 = data.readInt();
                boolean _arg52 = data.readInt() != 0;
                boolean _arg62 = data.readInt() != 0;
                boolean updatePackage = updatePackage(_arg03, _arg13, _arg23, _arg32, _arg42, _arg52, _arg62);
                reply.writeNoException();
                reply.writeInt(updatePackage ? 1 : 0);
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
                String _arg14 = data.readString();
                String _arg24 = data.readString();
                String _arg33 = data.readString();
                boolean deletePackage = deletePackage(_arg04, _arg14, _arg24, _arg33);
                reply.writeNoException();
                reply.writeInt(deletePackage ? 1 : 0);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IWapPushManager {
            public static IWapPushManager sDefaultImpl;
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

            @Override // com.android.internal.telephony.IWapPushManager
            public int processMessage(String app_id, String content_type, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(app_id);
                    _data.writeString(content_type);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().processMessage(app_id, content_type, intent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IWapPushManager
            public boolean addPackage(String x_app_id, String content_type, String package_name, String class_name, int app_type, boolean need_signature, boolean further_processing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(x_app_id);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(content_type);
                    try {
                        _data.writeString(package_name);
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(class_name);
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(app_type);
                        _data.writeInt(need_signature ? 1 : 0);
                        _data.writeInt(further_processing ? 1 : 0);
                        boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            boolean addPackage = Stub.getDefaultImpl().addPackage(x_app_id, content_type, package_name, class_name, app_type, need_signature, further_processing);
                            _reply.recycle();
                            _data.recycle();
                            return addPackage;
                        }
                        _reply.readException();
                        boolean _result = _reply.readInt() != 0;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.IWapPushManager
            public boolean updatePackage(String x_app_id, String content_type, String package_name, String class_name, int app_type, boolean need_signature, boolean further_processing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(x_app_id);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(content_type);
                    try {
                        _data.writeString(package_name);
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(class_name);
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(app_type);
                        _data.writeInt(need_signature ? 1 : 0);
                        _data.writeInt(further_processing ? 1 : 0);
                        boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            boolean updatePackage = Stub.getDefaultImpl().updatePackage(x_app_id, content_type, package_name, class_name, app_type, need_signature, further_processing);
                            _reply.recycle();
                            _data.recycle();
                            return updatePackage;
                        }
                        _reply.readException();
                        boolean _result = _reply.readInt() != 0;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.IWapPushManager
            public boolean deletePackage(String x_app_id, String content_type, String package_name, String class_name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(x_app_id);
                    _data.writeString(content_type);
                    _data.writeString(package_name);
                    _data.writeString(class_name);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().deletePackage(x_app_id, content_type, package_name, class_name);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IWapPushManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IWapPushManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
