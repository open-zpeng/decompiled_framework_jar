package android.speech.tts;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface ITextToSpeechCallback extends IInterface {
    void onAudioAvailable(String str, byte[] bArr) throws RemoteException;

    void onBeginSynthesis(String str, int i, int i2, int i3) throws RemoteException;

    void onError(Bundle bundle, String str, int i) throws RemoteException;

    void onRangeStart(String str, int i, int i2, int i3) throws RemoteException;

    void onStart(Bundle bundle, String str) throws RemoteException;

    void onStateChanged(String str) throws RemoteException;

    void onStop(Bundle bundle, String str, boolean z) throws RemoteException;

    void onSuccess(Bundle bundle, String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements ITextToSpeechCallback {
        @Override // android.speech.tts.ITextToSpeechCallback
        public void onStart(Bundle params, String utteranceId) throws RemoteException {
        }

        @Override // android.speech.tts.ITextToSpeechCallback
        public void onSuccess(Bundle params, String utteranceId) throws RemoteException {
        }

        @Override // android.speech.tts.ITextToSpeechCallback
        public void onStop(Bundle params, String utteranceId, boolean isStarted) throws RemoteException {
        }

        @Override // android.speech.tts.ITextToSpeechCallback
        public void onError(Bundle params, String utteranceId, int errorCode) throws RemoteException {
        }

        @Override // android.speech.tts.ITextToSpeechCallback
        public void onBeginSynthesis(String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) throws RemoteException {
        }

        @Override // android.speech.tts.ITextToSpeechCallback
        public void onAudioAvailable(String utteranceId, byte[] audio) throws RemoteException {
        }

        @Override // android.speech.tts.ITextToSpeechCallback
        public void onRangeStart(String utteranceId, int start, int end, int frame) throws RemoteException {
        }

        @Override // android.speech.tts.ITextToSpeechCallback
        public void onStateChanged(String state) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ITextToSpeechCallback {
        private static final String DESCRIPTOR = "android.speech.tts.ITextToSpeechCallback";
        static final int TRANSACTION_onAudioAvailable = 6;
        static final int TRANSACTION_onBeginSynthesis = 5;
        static final int TRANSACTION_onError = 4;
        static final int TRANSACTION_onRangeStart = 7;
        static final int TRANSACTION_onStart = 1;
        static final int TRANSACTION_onStateChanged = 8;
        static final int TRANSACTION_onStop = 3;
        static final int TRANSACTION_onSuccess = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITextToSpeechCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITextToSpeechCallback)) {
                return (ITextToSpeechCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onStart";
                case 2:
                    return "onSuccess";
                case 3:
                    return "onStop";
                case 4:
                    return "onError";
                case 5:
                    return "onBeginSynthesis";
                case 6:
                    return "onAudioAvailable";
                case 7:
                    return "onRangeStart";
                case 8:
                    return "onStateChanged";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bundle _arg0;
            Bundle _arg02;
            Bundle _arg03;
            Bundle _arg04;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    String _arg1 = data.readString();
                    onStart(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    String _arg12 = data.readString();
                    onSuccess(_arg02, _arg12);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    String _arg13 = data.readString();
                    boolean _arg2 = data.readInt() != 0;
                    onStop(_arg03, _arg13, _arg2);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    String _arg14 = data.readString();
                    int _arg22 = data.readInt();
                    onError(_arg04, _arg14, _arg22);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    int _arg15 = data.readInt();
                    int _arg23 = data.readInt();
                    int _arg3 = data.readInt();
                    onBeginSynthesis(_arg05, _arg15, _arg23, _arg3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    byte[] _arg16 = data.createByteArray();
                    onAudioAvailable(_arg06, _arg16);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    int _arg17 = data.readInt();
                    int _arg24 = data.readInt();
                    int _arg32 = data.readInt();
                    onRangeStart(_arg07, _arg17, _arg24, _arg32);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    onStateChanged(_arg08);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements ITextToSpeechCallback {
            public static ITextToSpeechCallback sDefaultImpl;
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

            @Override // android.speech.tts.ITextToSpeechCallback
            public void onStart(Bundle params, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(utteranceId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStart(params, utteranceId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechCallback
            public void onSuccess(Bundle params, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(utteranceId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSuccess(params, utteranceId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechCallback
            public void onStop(Bundle params, String utteranceId, boolean isStarted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(utteranceId);
                    _data.writeInt(isStarted ? 1 : 0);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStop(params, utteranceId, isStarted);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechCallback
            public void onError(Bundle params, String utteranceId, int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(utteranceId);
                    _data.writeInt(errorCode);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onError(params, utteranceId, errorCode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechCallback
            public void onBeginSynthesis(String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(utteranceId);
                    _data.writeInt(sampleRateInHz);
                    _data.writeInt(audioFormat);
                    _data.writeInt(channelCount);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBeginSynthesis(utteranceId, sampleRateInHz, audioFormat, channelCount);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechCallback
            public void onAudioAvailable(String utteranceId, byte[] audio) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(utteranceId);
                    _data.writeByteArray(audio);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAudioAvailable(utteranceId, audio);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechCallback
            public void onRangeStart(String utteranceId, int start, int end, int frame) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(utteranceId);
                    _data.writeInt(start);
                    _data.writeInt(end);
                    _data.writeInt(frame);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onRangeStart(utteranceId, start, end, frame);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechCallback
            public void onStateChanged(String state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(state);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStateChanged(state);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ITextToSpeechCallback impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ITextToSpeechCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
