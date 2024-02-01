package android.hardware.soundtrigger;

import android.hardware.soundtrigger.SoundTrigger;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IRecognitionStatusCallback extends IInterface {
    synchronized void onError(int i) throws RemoteException;

    synchronized void onGenericSoundTriggerDetected(SoundTrigger.GenericRecognitionEvent genericRecognitionEvent) throws RemoteException;

    synchronized void onKeyphraseDetected(SoundTrigger.KeyphraseRecognitionEvent keyphraseRecognitionEvent) throws RemoteException;

    synchronized void onRecognitionPaused() throws RemoteException;

    synchronized void onRecognitionResumed() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IRecognitionStatusCallback {
        private static final String DESCRIPTOR = "android.hardware.soundtrigger.IRecognitionStatusCallback";
        static final int TRANSACTION_onError = 3;
        static final int TRANSACTION_onGenericSoundTriggerDetected = 2;
        static final int TRANSACTION_onKeyphraseDetected = 1;
        static final int TRANSACTION_onRecognitionPaused = 4;
        static final int TRANSACTION_onRecognitionResumed = 5;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IRecognitionStatusCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IRecognitionStatusCallback)) {
                return (IRecognitionStatusCallback) iin;
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
                    SoundTrigger.KeyphraseRecognitionEvent _arg0 = data.readInt() != 0 ? SoundTrigger.KeyphraseRecognitionEvent.CREATOR.createFromParcel(data) : null;
                    onKeyphraseDetected(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    SoundTrigger.GenericRecognitionEvent _arg02 = data.readInt() != 0 ? SoundTrigger.GenericRecognitionEvent.CREATOR.createFromParcel(data) : null;
                    onGenericSoundTriggerDetected(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    onError(_arg03);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onRecognitionPaused();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onRecognitionResumed();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IRecognitionStatusCallback {
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

            @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
            public synchronized void onKeyphraseDetected(SoundTrigger.KeyphraseRecognitionEvent recognitionEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recognitionEvent != null) {
                        _data.writeInt(1);
                        recognitionEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
            public synchronized void onGenericSoundTriggerDetected(SoundTrigger.GenericRecognitionEvent recognitionEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recognitionEvent != null) {
                        _data.writeInt(1);
                        recognitionEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
            public synchronized void onError(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
            public synchronized void onRecognitionPaused() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.soundtrigger.IRecognitionStatusCallback
            public synchronized void onRecognitionResumed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
