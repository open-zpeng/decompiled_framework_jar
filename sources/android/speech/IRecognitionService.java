package android.speech;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.speech.IRecognitionListener;
/* loaded from: classes2.dex */
public interface IRecognitionService extends IInterface {
    synchronized void cancel(IRecognitionListener iRecognitionListener) throws RemoteException;

    void initRecognizer(Bundle bundle, IRecognitionListener iRecognitionListener) throws RemoteException;

    void setParameters(Bundle bundle) throws RemoteException;

    synchronized void startListening(Intent intent, IRecognitionListener iRecognitionListener) throws RemoteException;

    synchronized void stopListening(IRecognitionListener iRecognitionListener) throws RemoteException;

    void updateVocab(boolean z, int i, String str, IRecognitionListener iRecognitionListener) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IRecognitionService {
        private static final String DESCRIPTOR = "android.speech.IRecognitionService";
        static final int TRANSACTION_cancel = 3;
        static final int TRANSACTION_initRecognizer = 4;
        static final int TRANSACTION_setParameters = 6;
        static final int TRANSACTION_startListening = 1;
        static final int TRANSACTION_stopListening = 2;
        static final int TRANSACTION_updateVocab = 5;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IRecognitionService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IRecognitionService)) {
                return (IRecognitionService) iin;
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
                    Intent _arg0 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    IRecognitionListener _arg1 = IRecognitionListener.Stub.asInterface(data.readStrongBinder());
                    startListening(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IRecognitionListener _arg02 = IRecognitionListener.Stub.asInterface(data.readStrongBinder());
                    stopListening(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IRecognitionListener _arg03 = IRecognitionListener.Stub.asInterface(data.readStrongBinder());
                    cancel(_arg03);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg04 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    IRecognitionListener _arg12 = IRecognitionListener.Stub.asInterface(data.readStrongBinder());
                    initRecognizer(_arg04, _arg12);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg05 = data.readInt() != 0;
                    int _arg13 = data.readInt();
                    String _arg2 = data.readString();
                    IRecognitionListener _arg3 = IRecognitionListener.Stub.asInterface(data.readStrongBinder());
                    updateVocab(_arg05, _arg13, _arg2, _arg3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg06 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    setParameters(_arg06);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IRecognitionService {
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

            @Override // android.speech.IRecognitionService
            public synchronized void startListening(Intent recognizerIntent, IRecognitionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recognizerIntent != null) {
                        _data.writeInt(1);
                        recognizerIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionService
            public synchronized void stopListening(IRecognitionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionService
            public synchronized void cancel(IRecognitionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionService
            public void initRecognizer(Bundle params, IRecognitionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionService
            public void updateVocab(boolean addOrDelete, int type, String vocab, IRecognitionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(addOrDelete ? 1 : 0);
                    _data.writeInt(type);
                    _data.writeString(vocab);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionService
            public void setParameters(Bundle params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
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
