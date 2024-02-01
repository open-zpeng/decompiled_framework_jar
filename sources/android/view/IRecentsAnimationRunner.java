package android.view;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.IRecentsAnimationController;

/* loaded from: classes3.dex */
public interface IRecentsAnimationRunner extends IInterface {
    @UnsupportedAppUsage
    void onAnimationCanceled(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void onAnimationStart(IRecentsAnimationController iRecentsAnimationController, RemoteAnimationTarget[] remoteAnimationTargetArr, Rect rect, Rect rect2) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IRecentsAnimationRunner {
        @Override // android.view.IRecentsAnimationRunner
        public void onAnimationCanceled(boolean deferredWithScreenshot) throws RemoteException {
        }

        @Override // android.view.IRecentsAnimationRunner
        public void onAnimationStart(IRecentsAnimationController controller, RemoteAnimationTarget[] apps, Rect homeContentInsets, Rect minimizedHomeBounds) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IRecentsAnimationRunner {
        private static final String DESCRIPTOR = "android.view.IRecentsAnimationRunner";
        static final int TRANSACTION_onAnimationCanceled = 2;
        static final int TRANSACTION_onAnimationStart = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRecentsAnimationRunner asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IRecentsAnimationRunner)) {
                return (IRecentsAnimationRunner) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 2) {
                if (transactionCode == 3) {
                    return "onAnimationStart";
                }
                return null;
            }
            return "onAnimationCanceled";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Rect _arg2;
            Rect _arg3;
            if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                boolean _arg0 = data.readInt() != 0;
                onAnimationCanceled(_arg0);
                return true;
            } else if (code != 3) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                IRecentsAnimationController _arg02 = IRecentsAnimationController.Stub.asInterface(data.readStrongBinder());
                RemoteAnimationTarget[] _arg1 = (RemoteAnimationTarget[]) data.createTypedArray(RemoteAnimationTarget.CREATOR);
                if (data.readInt() != 0) {
                    _arg2 = Rect.CREATOR.createFromParcel(data);
                } else {
                    _arg2 = null;
                }
                if (data.readInt() != 0) {
                    _arg3 = Rect.CREATOR.createFromParcel(data);
                } else {
                    _arg3 = null;
                }
                onAnimationStart(_arg02, _arg1, _arg2, _arg3);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IRecentsAnimationRunner {
            public static IRecentsAnimationRunner sDefaultImpl;
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

            @Override // android.view.IRecentsAnimationRunner
            public void onAnimationCanceled(boolean deferredWithScreenshot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deferredWithScreenshot ? 1 : 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAnimationCanceled(deferredWithScreenshot);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IRecentsAnimationRunner
            public void onAnimationStart(IRecentsAnimationController controller, RemoteAnimationTarget[] apps, Rect homeContentInsets, Rect minimizedHomeBounds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    _data.writeTypedArray(apps, 0);
                    if (homeContentInsets != null) {
                        _data.writeInt(1);
                        homeContentInsets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (minimizedHomeBounds != null) {
                        _data.writeInt(1);
                        minimizedHomeBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAnimationStart(controller, apps, homeContentInsets, minimizedHomeBounds);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IRecentsAnimationRunner impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IRecentsAnimationRunner getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
