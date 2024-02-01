package android.telephony.ims.aidl;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsReasonInfo;
/* loaded from: classes2.dex */
public interface IImsRegistrationCallback extends IInterface {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onDeregistered(ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onRegistered(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onRegistering(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onSubscriberAssociatedUriChanged(Uri[] uriArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onTechnologyChangeFailed(int i, ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IImsRegistrationCallback {
        public protected static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsRegistrationCallback";
        public private protected static final int TRANSACTION_onDeregistered = 3;
        public private protected static final int TRANSACTION_onRegistered = 1;
        public private protected static final int TRANSACTION_onRegistering = 2;
        public private protected static final int TRANSACTION_onSubscriberAssociatedUriChanged = 5;
        public private protected static final int TRANSACTION_onTechnologyChangeFailed = 4;

        private protected synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized IImsRegistrationCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsRegistrationCallback)) {
                return (IImsRegistrationCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ImsReasonInfo _arg1;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    onRegistered(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    onRegistering(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    onDeregistered(_arg1);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    _arg1 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    onTechnologyChangeFailed(_arg03, _arg1);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    Uri[] _arg04 = (Uri[]) data.createTypedArray(Uri.CREATOR);
                    onSubscriberAssociatedUriChanged(_arg04);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IImsRegistrationCallback {
            public protected IBinder mRemote;

            public private protected synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            private protected synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            private protected synchronized void onRegistered(int imsRadioTech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(imsRadioTech);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onRegistering(int imsRadioTech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(imsRadioTech);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onDeregistered(ImsReasonInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onTechnologyChangeFailed(int imsRadioTech, ImsReasonInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(imsRadioTech);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void onSubscriberAssociatedUriChanged(Uri[] uris) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(uris, 0);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
