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
    synchronized void getInstantAppIntentFilterList(Intent intent, int[] iArr, String str, IRemoteCallback iRemoteCallback) throws RemoteException;

    synchronized void getInstantAppResolveInfoList(Intent intent, int[] iArr, String str, int i, IRemoteCallback iRemoteCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IInstantAppResolver {
        private static final String DESCRIPTOR = "android.app.IInstantAppResolver";
        static final int TRANSACTION_getInstantAppIntentFilterList = 2;
        static final int TRANSACTION_getInstantAppResolveInfoList = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IInstantAppResolver asInterface(IBinder obj) {
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

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Intent _arg0;
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        _arg0 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                        Intent _arg02 = _arg0;
                        int[] _arg1 = data.createIntArray();
                        String _arg2 = data.readString();
                        int _arg3 = data.readInt();
                        IRemoteCallback _arg4 = IRemoteCallback.Stub.asInterface(data.readStrongBinder());
                        getInstantAppResolveInfoList(_arg02, _arg1, _arg2, _arg3, _arg4);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        _arg0 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                        int[] _arg12 = data.createIntArray();
                        String _arg22 = data.readString();
                        IRemoteCallback _arg32 = IRemoteCallback.Stub.asInterface(data.readStrongBinder());
                        getInstantAppIntentFilterList(_arg0, _arg12, _arg22, _arg32);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(DESCRIPTOR);
            return true;
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IInstantAppResolver {
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

            @Override // android.app.IInstantAppResolver
            public synchronized void getInstantAppResolveInfoList(Intent sanitizedIntent, int[] hostDigestPrefix, String token, int sequence, IRemoteCallback callback) throws RemoteException {
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
                    _data.writeString(token);
                    _data.writeInt(sequence);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IInstantAppResolver
            public synchronized void getInstantAppIntentFilterList(Intent sanitizedIntent, int[] hostDigestPrefix, String token, IRemoteCallback callback) throws RemoteException {
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
                    _data.writeString(token);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
