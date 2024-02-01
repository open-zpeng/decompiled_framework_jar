package android.app;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IInstantAppResolver extends IInterface {
    void getInstantAppIntentFilterList(Intent intent, int[] iArr, int i, String str, IRemoteCallback iRemoteCallback) throws RemoteException;

    void getInstantAppResolveInfoList(Intent intent, int[] iArr, int i, String str, int i2, IRemoteCallback iRemoteCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IInstantAppResolver {
        @Override // android.app.IInstantAppResolver
        public void getInstantAppResolveInfoList(Intent sanitizedIntent, int[] hostDigestPrefix, int userId, String token, int sequence, IRemoteCallback callback) throws RemoteException {
        }

        @Override // android.app.IInstantAppResolver
        public void getInstantAppIntentFilterList(Intent sanitizedIntent, int[] hostDigestPrefix, int userId, String token, IRemoteCallback callback) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IInstantAppResolver {
        private static final String DESCRIPTOR = "android.app.IInstantAppResolver";
        static final int TRANSACTION_getInstantAppIntentFilterList = 2;
        static final int TRANSACTION_getInstantAppResolveInfoList = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInstantAppResolver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IInstantAppResolver)) {
                return (IInstantAppResolver) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode == 2) {
                    return "getInstantAppIntentFilterList";
                }
                return null;
            }
            return "getInstantAppResolveInfoList";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Intent _arg0;
            Intent _arg02;
            if (code != 1) {
                if (code != 2) {
                    if (code == 1598968902) {
                        reply.writeString(DESCRIPTOR);
                        return true;
                    }
                    return super.onTransact(code, data, reply, flags);
                }
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg02 = Intent.CREATOR.createFromParcel(data);
                } else {
                    _arg02 = null;
                }
                int[] _arg1 = data.createIntArray();
                int _arg2 = data.readInt();
                String _arg3 = data.readString();
                IRemoteCallback _arg4 = IRemoteCallback.Stub.asInterface(data.readStrongBinder());
                getInstantAppIntentFilterList(_arg02, _arg1, _arg2, _arg3, _arg4);
                return true;
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = Intent.CREATOR.createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int[] _arg12 = data.createIntArray();
            int _arg22 = data.readInt();
            String _arg32 = data.readString();
            int _arg42 = data.readInt();
            IRemoteCallback _arg5 = IRemoteCallback.Stub.asInterface(data.readStrongBinder());
            getInstantAppResolveInfoList(_arg0, _arg12, _arg22, _arg32, _arg42, _arg5);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IInstantAppResolver {
            public static IInstantAppResolver sDefaultImpl;
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

            @Override // android.app.IInstantAppResolver
            public void getInstantAppResolveInfoList(Intent sanitizedIntent, int[] hostDigestPrefix, int userId, String token, int sequence, IRemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sanitizedIntent != null) {
                        _data.writeInt(1);
                        sanitizedIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeIntArray(hostDigestPrefix);
                    } catch (Throwable th) {
                        th = th;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(token);
                        try {
                            _data.writeInt(sequence);
                            _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _status = this.mRemote.transact(1, _data, null, 1);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            Stub.getDefaultImpl().getInstantAppResolveInfoList(sanitizedIntent, hostDigestPrefix, userId, token, sequence, callback);
                            _data.recycle();
                            return;
                        }
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            }

            @Override // android.app.IInstantAppResolver
            public void getInstantAppIntentFilterList(Intent sanitizedIntent, int[] hostDigestPrefix, int userId, String token, IRemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sanitizedIntent != null) {
                        _data.writeInt(1);
                        sanitizedIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeIntArray(hostDigestPrefix);
                    _data.writeInt(userId);
                    _data.writeString(token);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getInstantAppIntentFilterList(sanitizedIntent, hostDigestPrefix, userId, token, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IInstantAppResolver impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IInstantAppResolver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
