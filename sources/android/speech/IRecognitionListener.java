package android.speech;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IRecognitionListener extends IInterface {
    synchronized void onBeginningOfSpeech() throws RemoteException;

    synchronized void onBufferReceived(byte[] bArr) throws RemoteException;

    synchronized void onEndOfSpeech() throws RemoteException;

    synchronized void onError(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void onEvent(int i, Bundle bundle) throws RemoteException;

    synchronized void onPartialResults(Bundle bundle) throws RemoteException;

    synchronized void onReadyForSpeech(Bundle bundle) throws RemoteException;

    synchronized void onResults(Bundle bundle) throws RemoteException;

    synchronized void onRmsChanged(float f) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IRecognitionListener {
        private static final String DESCRIPTOR = "android.speech.IRecognitionListener";
        static final int TRANSACTION_onBeginningOfSpeech = 2;
        static final int TRANSACTION_onBufferReceived = 4;
        static final int TRANSACTION_onEndOfSpeech = 5;
        static final int TRANSACTION_onError = 6;
        static final int TRANSACTION_onEvent = 9;
        static final int TRANSACTION_onPartialResults = 8;
        static final int TRANSACTION_onReadyForSpeech = 1;
        static final int TRANSACTION_onResults = 7;
        static final int TRANSACTION_onRmsChanged = 3;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IRecognitionListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IRecognitionListener)) {
                return (IRecognitionListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bundle _arg1;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onReadyForSpeech(_arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onBeginningOfSpeech();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    float _arg0 = data.readFloat();
                    onRmsChanged(_arg0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg02 = data.createByteArray();
                    onBufferReceived(_arg02);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onEndOfSpeech();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    onError(_arg03);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onResults(_arg1);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onPartialResults(_arg1);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    _arg1 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onEvent(_arg04, _arg1);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IRecognitionListener {
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

            @Override // android.speech.IRecognitionListener
            public synchronized void onReadyForSpeech(Bundle params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionListener
            public synchronized void onBeginningOfSpeech() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionListener
            public synchronized void onRmsChanged(float rmsdB) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(rmsdB);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionListener
            public synchronized void onBufferReceived(byte[] buffer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(buffer);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionListener
            public synchronized void onEndOfSpeech() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionListener
            public synchronized void onError(int error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(error);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionListener
            public synchronized void onResults(Bundle results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (results != null) {
                        _data.writeInt(1);
                        results.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.IRecognitionListener
            public synchronized void onPartialResults(Bundle results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (results != null) {
                        _data.writeInt(1);
                        results.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void onEvent(int eventType, Bundle params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(eventType);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
