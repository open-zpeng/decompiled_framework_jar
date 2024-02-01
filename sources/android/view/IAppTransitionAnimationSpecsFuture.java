package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IAppTransitionAnimationSpecsFuture extends IInterface {
    synchronized AppTransitionAnimationSpec[] get() throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IAppTransitionAnimationSpecsFuture {
        private static final String DESCRIPTOR = "android.view.IAppTransitionAnimationSpecsFuture";
        static final int TRANSACTION_get = 1;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IAppTransitionAnimationSpecsFuture asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAppTransitionAnimationSpecsFuture)) {
                return (IAppTransitionAnimationSpecsFuture) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
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
            AppTransitionAnimationSpec[] _result = get();
            reply.writeNoException();
            reply.writeTypedArray(_result, 1);
            return true;
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IAppTransitionAnimationSpecsFuture {
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

            @Override // android.view.IAppTransitionAnimationSpecsFuture
            public synchronized AppTransitionAnimationSpec[] get() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    AppTransitionAnimationSpec[] _result = (AppTransitionAnimationSpec[]) _reply.createTypedArray(AppTransitionAnimationSpec.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
