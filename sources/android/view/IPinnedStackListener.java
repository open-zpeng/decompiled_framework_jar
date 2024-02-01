package android.view;

import android.content.pm.ParceledListSlice;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.IPinnedStackController;
/* loaded from: classes2.dex */
public interface IPinnedStackListener extends IInterface {
    synchronized void onActionsChanged(ParceledListSlice parceledListSlice) throws RemoteException;

    synchronized void onImeVisibilityChanged(boolean z, int i) throws RemoteException;

    synchronized void onListenerRegistered(IPinnedStackController iPinnedStackController) throws RemoteException;

    synchronized void onMinimizedStateChanged(boolean z) throws RemoteException;

    synchronized void onMovementBoundsChanged(Rect rect, Rect rect2, Rect rect3, boolean z, boolean z2, int i) throws RemoteException;

    synchronized void onShelfVisibilityChanged(boolean z, int i) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IPinnedStackListener {
        private static final String DESCRIPTOR = "android.view.IPinnedStackListener";
        static final int TRANSACTION_onActionsChanged = 6;
        static final int TRANSACTION_onImeVisibilityChanged = 3;
        static final int TRANSACTION_onListenerRegistered = 1;
        static final int TRANSACTION_onMinimizedStateChanged = 5;
        static final int TRANSACTION_onMovementBoundsChanged = 2;
        static final int TRANSACTION_onShelfVisibilityChanged = 4;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IPinnedStackListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IPinnedStackListener)) {
                return (IPinnedStackListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Rect _arg0;
            Rect _arg1;
            boolean _arg4;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IPinnedStackController _arg02 = IPinnedStackController.Stub.asInterface(data.readStrongBinder());
                    onListenerRegistered(_arg02);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    Rect _arg2 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
                    boolean _arg3 = data.readInt() != 0;
                    _arg4 = data.readInt() != 0;
                    int _arg5 = data.readInt();
                    onMovementBoundsChanged(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg4 = data.readInt() != 0;
                    boolean _arg03 = _arg4;
                    int _arg12 = data.readInt();
                    onImeVisibilityChanged(_arg03, _arg12);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg4 = data.readInt() != 0;
                    boolean _arg04 = _arg4;
                    int _arg13 = data.readInt();
                    onShelfVisibilityChanged(_arg04, _arg13);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _arg4 = data.readInt() != 0;
                    boolean _arg05 = _arg4;
                    onMinimizedStateChanged(_arg05);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    ParceledListSlice _arg06 = data.readInt() != 0 ? ParceledListSlice.CREATOR.createFromParcel(data) : null;
                    onActionsChanged(_arg06);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IPinnedStackListener {
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

            @Override // android.view.IPinnedStackListener
            public synchronized void onListenerRegistered(IPinnedStackController controller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IPinnedStackListener
            public synchronized void onMovementBoundsChanged(Rect insetBounds, Rect normalBounds, Rect animatingBounds, boolean fromImeAdjustment, boolean fromShelfAdjustment, int displayRotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (insetBounds != null) {
                        _data.writeInt(1);
                        insetBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (normalBounds != null) {
                        _data.writeInt(1);
                        normalBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (animatingBounds != null) {
                        _data.writeInt(1);
                        animatingBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(fromImeAdjustment ? 1 : 0);
                    _data.writeInt(fromShelfAdjustment ? 1 : 0);
                    _data.writeInt(displayRotation);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IPinnedStackListener
            public synchronized void onImeVisibilityChanged(boolean imeVisible, int imeHeight) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(imeVisible ? 1 : 0);
                    _data.writeInt(imeHeight);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IPinnedStackListener
            public synchronized void onShelfVisibilityChanged(boolean shelfVisible, int shelfHeight) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(shelfVisible ? 1 : 0);
                    _data.writeInt(shelfHeight);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IPinnedStackListener
            public synchronized void onMinimizedStateChanged(boolean isMinimized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isMinimized ? 1 : 0);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IPinnedStackListener
            public synchronized void onActionsChanged(ParceledListSlice actions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (actions != null) {
                        _data.writeInt(1);
                        actions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
