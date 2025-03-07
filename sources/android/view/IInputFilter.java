package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.IInputFilterHost;

/* loaded from: classes3.dex */
public interface IInputFilter extends IInterface {
    void filterInputEvent(InputEvent inputEvent, int i) throws RemoteException;

    void install(IInputFilterHost iInputFilterHost) throws RemoteException;

    void uninstall() throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IInputFilter {
        @Override // android.view.IInputFilter
        public void install(IInputFilterHost host) throws RemoteException {
        }

        @Override // android.view.IInputFilter
        public void uninstall() throws RemoteException {
        }

        @Override // android.view.IInputFilter
        public void filterInputEvent(InputEvent event, int policyFlags) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IInputFilter {
        private static final String DESCRIPTOR = "android.view.IInputFilter";
        static final int TRANSACTION_filterInputEvent = 3;
        static final int TRANSACTION_install = 1;
        static final int TRANSACTION_uninstall = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInputFilter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IInputFilter)) {
                return (IInputFilter) iin;
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
                    if (transactionCode == 3) {
                        return "filterInputEvent";
                    }
                    return null;
                }
                return "uninstall";
            }
            return "install";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            InputEvent _arg0;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                IInputFilterHost _arg02 = IInputFilterHost.Stub.asInterface(data.readStrongBinder());
                install(_arg02);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                uninstall();
                return true;
            } else if (code != 3) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                if (data.readInt() != 0) {
                    _arg0 = InputEvent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                int _arg1 = data.readInt();
                filterInputEvent(_arg0, _arg1);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IInputFilter {
            public static IInputFilter sDefaultImpl;
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

            @Override // android.view.IInputFilter
            public void install(IInputFilterHost host) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(host != null ? host.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().install(host);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IInputFilter
            public void uninstall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().uninstall();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IInputFilter
            public void filterInputEvent(InputEvent event, int policyFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(policyFlags);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().filterInputEvent(event, policyFlags);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IInputFilter impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IInputFilter getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
