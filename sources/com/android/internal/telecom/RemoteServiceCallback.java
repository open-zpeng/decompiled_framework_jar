package com.android.internal.telecom;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes3.dex */
public interface RemoteServiceCallback extends IInterface {
    synchronized void onError() throws RemoteException;

    synchronized void onResult(List<ComponentName> list, List<IBinder> list2) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements RemoteServiceCallback {
        private static final String DESCRIPTOR = "com.android.internal.telecom.RemoteServiceCallback";
        static final int TRANSACTION_onError = 1;
        static final int TRANSACTION_onResult = 2;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized RemoteServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof RemoteServiceCallback)) {
                return (RemoteServiceCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onError();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    List<ComponentName> _arg0 = data.createTypedArrayList(ComponentName.CREATOR);
                    List<IBinder> _arg1 = data.createBinderArrayList();
                    onResult(_arg0, _arg1);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements RemoteServiceCallback {
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

            @Override // com.android.internal.telecom.RemoteServiceCallback
            public synchronized void onError() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.RemoteServiceCallback
            public synchronized void onResult(List<ComponentName> components, List<IBinder> callServices) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(components);
                    _data.writeBinderList(callServices);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
