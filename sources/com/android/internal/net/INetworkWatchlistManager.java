package com.android.internal.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes3.dex */
public interface INetworkWatchlistManager extends IInterface {
    byte[] getWatchlistConfigHash() throws RemoteException;

    void reloadWatchlist() throws RemoteException;

    void reportWatchlistIfNecessary() throws RemoteException;

    boolean startWatchlistLogging() throws RemoteException;

    boolean stopWatchlistLogging() throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements INetworkWatchlistManager {
        @Override // com.android.internal.net.INetworkWatchlistManager
        public boolean startWatchlistLogging() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.net.INetworkWatchlistManager
        public boolean stopWatchlistLogging() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.net.INetworkWatchlistManager
        public void reloadWatchlist() throws RemoteException {
        }

        @Override // com.android.internal.net.INetworkWatchlistManager
        public void reportWatchlistIfNecessary() throws RemoteException {
        }

        @Override // com.android.internal.net.INetworkWatchlistManager
        public byte[] getWatchlistConfigHash() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements INetworkWatchlistManager {
        private static final String DESCRIPTOR = "com.android.internal.net.INetworkWatchlistManager";
        static final int TRANSACTION_getWatchlistConfigHash = 5;
        static final int TRANSACTION_reloadWatchlist = 3;
        static final int TRANSACTION_reportWatchlistIfNecessary = 4;
        static final int TRANSACTION_startWatchlistLogging = 1;
        static final int TRANSACTION_stopWatchlistLogging = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetworkWatchlistManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof INetworkWatchlistManager)) {
                return (INetworkWatchlistManager) iin;
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
                        if (transactionCode != 4) {
                            if (transactionCode == 5) {
                                return "getWatchlistConfigHash";
                            }
                            return null;
                        }
                        return "reportWatchlistIfNecessary";
                    }
                    return "reloadWatchlist";
                }
                return "stopWatchlistLogging";
            }
            return "startWatchlistLogging";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                boolean startWatchlistLogging = startWatchlistLogging();
                reply.writeNoException();
                reply.writeInt(startWatchlistLogging ? 1 : 0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                boolean stopWatchlistLogging = stopWatchlistLogging();
                reply.writeNoException();
                reply.writeInt(stopWatchlistLogging ? 1 : 0);
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                reloadWatchlist();
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                data.enforceInterface(DESCRIPTOR);
                reportWatchlistIfNecessary();
                reply.writeNoException();
                return true;
            } else if (code != 5) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                byte[] _result = getWatchlistConfigHash();
                reply.writeNoException();
                reply.writeByteArray(_result);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements INetworkWatchlistManager {
            public static INetworkWatchlistManager sDefaultImpl;
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

            @Override // com.android.internal.net.INetworkWatchlistManager
            public boolean startWatchlistLogging() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startWatchlistLogging();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.net.INetworkWatchlistManager
            public boolean stopWatchlistLogging() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopWatchlistLogging();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.net.INetworkWatchlistManager
            public void reloadWatchlist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reloadWatchlist();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.net.INetworkWatchlistManager
            public void reportWatchlistIfNecessary() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportWatchlistIfNecessary();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.net.INetworkWatchlistManager
            public byte[] getWatchlistConfigHash() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getWatchlistConfigHash();
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(INetworkWatchlistManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static INetworkWatchlistManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
