package android.media;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IAudioFocusDispatcher extends IInterface {
    @UnsupportedAppUsage
    void dispatchAudioFocusChange(int i, String str) throws RemoteException;

    void dispatchFocusResultFromExtPolicy(int i, String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IAudioFocusDispatcher {
        @Override // android.media.IAudioFocusDispatcher
        public void dispatchAudioFocusChange(int focusChange, String clientId) throws RemoteException {
        }

        @Override // android.media.IAudioFocusDispatcher
        public void dispatchFocusResultFromExtPolicy(int requestResult, String clientId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IAudioFocusDispatcher {
        private static final String DESCRIPTOR = "android.media.IAudioFocusDispatcher";
        static final int TRANSACTION_dispatchAudioFocusChange = 1;
        static final int TRANSACTION_dispatchFocusResultFromExtPolicy = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAudioFocusDispatcher asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAudioFocusDispatcher)) {
                return (IAudioFocusDispatcher) iin;
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
                    return "dispatchFocusResultFromExtPolicy";
                }
                return null;
            }
            return "dispatchAudioFocusChange";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                int _arg0 = data.readInt();
                String _arg1 = data.readString();
                dispatchAudioFocusChange(_arg0, _arg1);
                return true;
            } else if (code != 2) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg02 = data.readInt();
                String _arg12 = data.readString();
                dispatchFocusResultFromExtPolicy(_arg02, _arg12);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IAudioFocusDispatcher {
            public static IAudioFocusDispatcher sDefaultImpl;
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

            @Override // android.media.IAudioFocusDispatcher
            public void dispatchAudioFocusChange(int focusChange, String clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(focusChange);
                    _data.writeString(clientId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchAudioFocusChange(focusChange, clientId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioFocusDispatcher
            public void dispatchFocusResultFromExtPolicy(int requestResult, String clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestResult);
                    _data.writeString(clientId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchFocusResultFromExtPolicy(requestResult, clientId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IAudioFocusDispatcher impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IAudioFocusDispatcher getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
