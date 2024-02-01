package com.xiaopeng.cardiagnosis.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes3.dex */
public interface IDealCaton extends IInterface {
    void dealCaton(String str, String str2, boolean z, String str3, long j, long j2) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IDealCaton {
        @Override // com.xiaopeng.cardiagnosis.aidl.IDealCaton
        public void dealCaton(String stackTraces, String stackTracesforMd5, boolean needCheckAnr, String packageName, long stuckElapseTime, long threadElapseTime) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IDealCaton {
        private static final String DESCRIPTOR = "com.xiaopeng.cardiagnosis.aidl.IDealCaton";
        static final int TRANSACTION_dealCaton = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDealCaton asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDealCaton)) {
                return (IDealCaton) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "dealCaton";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            String _arg1 = data.readString();
            boolean _arg2 = data.readInt() != 0;
            String _arg3 = data.readString();
            long _arg4 = data.readLong();
            long _arg5 = data.readLong();
            dealCaton(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IDealCaton {
            public static IDealCaton sDefaultImpl;
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

            @Override // com.xiaopeng.cardiagnosis.aidl.IDealCaton
            public void dealCaton(String stackTraces, String stackTracesforMd5, boolean needCheckAnr, String packageName, long stuckElapseTime, long threadElapseTime) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(stackTraces);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(stackTracesforMd5);
                        _data.writeInt(needCheckAnr ? 1 : 0);
                        try {
                            _data.writeString(packageName);
                            _data.writeLong(stuckElapseTime);
                            _data.writeLong(threadElapseTime);
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().dealCaton(stackTraces, stackTracesforMd5, needCheckAnr, packageName, stuckElapseTime, threadElapseTime);
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }
        }

        public static boolean setDefaultImpl(IDealCaton impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IDealCaton getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
