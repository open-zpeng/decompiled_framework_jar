package com.xiaopeng.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes3.dex */
public interface ISharedDisplayListener extends IInterface {
    void onActivityChanged(int i, String str) throws RemoteException;

    void onChanged(String str, int i) throws RemoteException;

    void onEventChanged(int i, String str) throws RemoteException;

    void onPositionChanged(String str, int i, int i2, int i3) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements ISharedDisplayListener {
        @Override // com.xiaopeng.view.ISharedDisplayListener
        public void onChanged(String packageName, int sharedId) throws RemoteException {
        }

        @Override // com.xiaopeng.view.ISharedDisplayListener
        public void onPositionChanged(String packageName, int event, int from, int to) throws RemoteException {
        }

        @Override // com.xiaopeng.view.ISharedDisplayListener
        public void onActivityChanged(int screenId, String property) throws RemoteException {
        }

        @Override // com.xiaopeng.view.ISharedDisplayListener
        public void onEventChanged(int event, String property) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ISharedDisplayListener {
        private static final String DESCRIPTOR = "com.xiaopeng.view.ISharedDisplayListener";
        static final int TRANSACTION_onActivityChanged = 3;
        static final int TRANSACTION_onChanged = 1;
        static final int TRANSACTION_onEventChanged = 4;
        static final int TRANSACTION_onPositionChanged = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISharedDisplayListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISharedDisplayListener)) {
                return (ISharedDisplayListener) iin;
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
                            return "onEventChanged";
                        }
                        return null;
                    }
                    return "onActivityChanged";
                }
                return "onPositionChanged";
            }
            return "onChanged";
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
                int _arg1 = data.readInt();
                onChanged(_arg0, _arg1);
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                String _arg02 = data.readString();
                int _arg12 = data.readInt();
                int _arg2 = data.readInt();
                int _arg3 = data.readInt();
                onPositionChanged(_arg02, _arg12, _arg2, _arg3);
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                int _arg03 = data.readInt();
                String _arg13 = data.readString();
                onActivityChanged(_arg03, _arg13);
                reply.writeNoException();
                return true;
            } else if (code != 4) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg04 = data.readInt();
                String _arg14 = data.readString();
                onEventChanged(_arg04, _arg14);
                reply.writeNoException();
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements ISharedDisplayListener {
            public static ISharedDisplayListener sDefaultImpl;
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

            @Override // com.xiaopeng.view.ISharedDisplayListener
            public void onChanged(String packageName, int sharedId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(sharedId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onChanged(packageName, sharedId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.view.ISharedDisplayListener
            public void onPositionChanged(String packageName, int event, int from, int to) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(event);
                    _data.writeInt(from);
                    _data.writeInt(to);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onPositionChanged(packageName, event, from, to);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.view.ISharedDisplayListener
            public void onActivityChanged(int screenId, String property) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(screenId);
                    _data.writeString(property);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityChanged(screenId, property);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.view.ISharedDisplayListener
            public void onEventChanged(int event, String property) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(event);
                    _data.writeString(property);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onEventChanged(event, property);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISharedDisplayListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ISharedDisplayListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
