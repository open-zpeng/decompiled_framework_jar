package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IDockedStackListener extends IInterface {
    synchronized void onAdjustedForImeChanged(boolean z, long j) throws RemoteException;

    synchronized void onDividerVisibilityChanged(boolean z) throws RemoteException;

    synchronized void onDockSideChanged(int i) throws RemoteException;

    synchronized void onDockedStackExistsChanged(boolean z) throws RemoteException;

    synchronized void onDockedStackMinimizedChanged(boolean z, long j, boolean z2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IDockedStackListener {
        private static final String DESCRIPTOR = "android.view.IDockedStackListener";
        static final int TRANSACTION_onAdjustedForImeChanged = 4;
        static final int TRANSACTION_onDividerVisibilityChanged = 1;
        static final int TRANSACTION_onDockSideChanged = 5;
        static final int TRANSACTION_onDockedStackExistsChanged = 2;
        static final int TRANSACTION_onDockedStackMinimizedChanged = 3;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IDockedStackListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDockedStackListener)) {
                return (IDockedStackListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onDividerVisibilityChanged(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onDockedStackExistsChanged(_arg0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg02 = data.readInt() != 0;
                    long _arg1 = data.readLong();
                    _arg0 = data.readInt() != 0;
                    onDockedStackMinimizedChanged(_arg02, _arg1, _arg0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    long _arg12 = data.readLong();
                    onAdjustedForImeChanged(_arg0, _arg12);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onDockSideChanged(data.readInt());
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IDockedStackListener {
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

            @Override // android.view.IDockedStackListener
            public synchronized void onDividerVisibilityChanged(boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visible ? 1 : 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IDockedStackListener
            public synchronized void onDockedStackExistsChanged(boolean exists) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(exists ? 1 : 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IDockedStackListener
            public synchronized void onDockedStackMinimizedChanged(boolean minimized, long animDuration, boolean isHomeStackResizable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(minimized ? 1 : 0);
                    _data.writeLong(animDuration);
                    _data.writeInt(isHomeStackResizable ? 1 : 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IDockedStackListener
            public synchronized void onAdjustedForImeChanged(boolean adjustedForIme, long animDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(adjustedForIme ? 1 : 0);
                    _data.writeLong(animDuration);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IDockedStackListener
            public synchronized void onDockSideChanged(int newDockSide) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newDockSide);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
