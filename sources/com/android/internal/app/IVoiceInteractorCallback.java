package com.android.internal.app;

import android.app.VoiceInteractor;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.app.IVoiceInteractorRequest;
/* loaded from: classes3.dex */
public interface IVoiceInteractorCallback extends IInterface {
    synchronized void deliverAbortVoiceResult(IVoiceInteractorRequest iVoiceInteractorRequest, Bundle bundle) throws RemoteException;

    synchronized void deliverCancel(IVoiceInteractorRequest iVoiceInteractorRequest) throws RemoteException;

    synchronized void deliverCommandResult(IVoiceInteractorRequest iVoiceInteractorRequest, boolean z, Bundle bundle) throws RemoteException;

    synchronized void deliverCompleteVoiceResult(IVoiceInteractorRequest iVoiceInteractorRequest, Bundle bundle) throws RemoteException;

    synchronized void deliverConfirmationResult(IVoiceInteractorRequest iVoiceInteractorRequest, boolean z, Bundle bundle) throws RemoteException;

    synchronized void deliverPickOptionResult(IVoiceInteractorRequest iVoiceInteractorRequest, boolean z, VoiceInteractor.PickOptionRequest.Option[] optionArr, Bundle bundle) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IVoiceInteractorCallback {
        private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractorCallback";
        static final int TRANSACTION_deliverAbortVoiceResult = 4;
        static final int TRANSACTION_deliverCancel = 6;
        static final int TRANSACTION_deliverCommandResult = 5;
        static final int TRANSACTION_deliverCompleteVoiceResult = 3;
        static final int TRANSACTION_deliverConfirmationResult = 1;
        static final int TRANSACTION_deliverPickOptionResult = 2;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IVoiceInteractorCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IVoiceInteractorCallback)) {
                return (IVoiceInteractorCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg1;
            Bundle _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractorRequest _arg0 = IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readInt() != 0;
                    _arg2 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    deliverConfirmationResult(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractorRequest _arg02 = IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readInt() != 0;
                    VoiceInteractor.PickOptionRequest.Option[] _arg22 = (VoiceInteractor.PickOptionRequest.Option[]) data.createTypedArray(VoiceInteractor.PickOptionRequest.Option.CREATOR);
                    _arg2 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    deliverPickOptionResult(_arg02, _arg1, _arg22, _arg2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractorRequest _arg03 = IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                    _arg2 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    deliverCompleteVoiceResult(_arg03, _arg2);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractorRequest _arg04 = IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                    _arg2 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    deliverAbortVoiceResult(_arg04, _arg2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractorRequest _arg05 = IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                    _arg1 = data.readInt() != 0;
                    _arg2 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    deliverCommandResult(_arg05, _arg1, _arg2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractorRequest _arg06 = IVoiceInteractorRequest.Stub.asInterface(data.readStrongBinder());
                    deliverCancel(_arg06);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IVoiceInteractorCallback {
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

            @Override // com.android.internal.app.IVoiceInteractorCallback
            public synchronized void deliverConfirmationResult(IVoiceInteractorRequest request, boolean confirmed, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    _data.writeInt(confirmed ? 1 : 0);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractorCallback
            public synchronized void deliverPickOptionResult(IVoiceInteractorRequest request, boolean finished, VoiceInteractor.PickOptionRequest.Option[] selections, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    _data.writeInt(finished ? 1 : 0);
                    _data.writeTypedArray(selections, 0);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractorCallback
            public synchronized void deliverCompleteVoiceResult(IVoiceInteractorRequest request, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractorCallback
            public synchronized void deliverAbortVoiceResult(IVoiceInteractorRequest request, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractorCallback
            public synchronized void deliverCommandResult(IVoiceInteractorRequest request, boolean finished, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    _data.writeInt(finished ? 1 : 0);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractorCallback
            public synchronized void deliverCancel(IVoiceInteractorRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(request != null ? request.asBinder() : null);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
