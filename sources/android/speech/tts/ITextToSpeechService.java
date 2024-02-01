package android.speech.tts;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.speech.tts.ITextToSpeechCallback;
import android.text.TextUtils;
import java.util.List;
/* loaded from: classes2.dex */
public interface ITextToSpeechService extends IInterface {
    synchronized String[] getClientDefaultLanguage() throws RemoteException;

    synchronized String getDefaultVoiceNameFor(String str, String str2, String str3) throws RemoteException;

    synchronized String[] getFeaturesForLanguage(String str, String str2, String str3) throws RemoteException;

    synchronized String[] getLanguage() throws RemoteException;

    synchronized List<Voice> getVoices() throws RemoteException;

    synchronized int isLanguageAvailable(String str, String str2, String str3) throws RemoteException;

    synchronized boolean isSpeaking() throws RemoteException;

    synchronized int loadLanguage(IBinder iBinder, String str, String str2, String str3) throws RemoteException;

    synchronized int loadVoice(IBinder iBinder, String str) throws RemoteException;

    synchronized int playAudio(IBinder iBinder, Uri uri, int i, Bundle bundle, String str) throws RemoteException;

    synchronized int playSilence(IBinder iBinder, long j, int i, String str) throws RemoteException;

    synchronized void setCallback(IBinder iBinder, ITextToSpeechCallback iTextToSpeechCallback) throws RemoteException;

    int setListenOnStateChanged(IBinder iBinder) throws RemoteException;

    int setSoloMode(IBinder iBinder, boolean z) throws RemoteException;

    synchronized int speak(IBinder iBinder, CharSequence charSequence, int i, Bundle bundle, String str) throws RemoteException;

    int stop(IBinder iBinder, String str) throws RemoteException;

    synchronized int synthesizeToFileDescriptor(IBinder iBinder, CharSequence charSequence, ParcelFileDescriptor parcelFileDescriptor, Bundle bundle, String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ITextToSpeechService {
        private static final String DESCRIPTOR = "android.speech.tts.ITextToSpeechService";
        static final int TRANSACTION_getClientDefaultLanguage = 8;
        static final int TRANSACTION_getDefaultVoiceNameFor = 15;
        static final int TRANSACTION_getFeaturesForLanguage = 10;
        static final int TRANSACTION_getLanguage = 7;
        static final int TRANSACTION_getVoices = 13;
        static final int TRANSACTION_isLanguageAvailable = 9;
        static final int TRANSACTION_isSpeaking = 5;
        static final int TRANSACTION_loadLanguage = 11;
        static final int TRANSACTION_loadVoice = 14;
        static final int TRANSACTION_playAudio = 3;
        static final int TRANSACTION_playSilence = 4;
        static final int TRANSACTION_setCallback = 12;
        static final int TRANSACTION_setListenOnStateChanged = 17;
        static final int TRANSACTION_setSoloMode = 16;
        static final int TRANSACTION_speak = 1;
        static final int TRANSACTION_stop = 6;
        static final int TRANSACTION_synthesizeToFileDescriptor = 2;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ITextToSpeechService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITextToSpeechService)) {
                return (ITextToSpeechService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            CharSequence _arg1;
            CharSequence _arg12;
            ParcelFileDescriptor _arg2;
            Uri _arg13;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg0 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        CharSequence _arg14 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                        _arg1 = _arg14;
                    } else {
                        _arg1 = null;
                    }
                    int _arg22 = data.readInt();
                    Bundle _arg3 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    String _arg4 = data.readString();
                    int _result = speak(_arg0, _arg1, _arg22, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        CharSequence _arg15 = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                        _arg12 = _arg15;
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        ParcelFileDescriptor _arg23 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                        _arg2 = _arg23;
                    } else {
                        _arg2 = null;
                    }
                    Bundle _arg32 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    String _arg42 = data.readString();
                    int _result2 = synthesizeToFileDescriptor(_arg02, _arg12, _arg2, _arg32, _arg42);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg03 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        Uri _arg16 = Uri.CREATOR.createFromParcel(data);
                        _arg13 = _arg16;
                    } else {
                        _arg13 = null;
                    }
                    int _arg24 = data.readInt();
                    Bundle _arg33 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    String _arg43 = data.readString();
                    int _result3 = playAudio(_arg03, _arg13, _arg24, _arg33, _arg43);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg04 = data.readStrongBinder();
                    long _arg17 = data.readLong();
                    int _arg25 = data.readInt();
                    String _arg34 = data.readString();
                    int _result4 = playSilence(_arg04, _arg17, _arg25, _arg34);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSpeaking = isSpeaking();
                    reply.writeNoException();
                    reply.writeInt(isSpeaking ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg05 = data.readStrongBinder();
                    String _arg18 = data.readString();
                    int _result5 = stop(_arg05, _arg18);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result6 = getLanguage();
                    reply.writeNoException();
                    reply.writeStringArray(_result6);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _result7 = getClientDefaultLanguage();
                    reply.writeNoException();
                    reply.writeStringArray(_result7);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    String _arg19 = data.readString();
                    String _arg26 = data.readString();
                    int _result8 = isLanguageAvailable(_arg06, _arg19, _arg26);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _arg110 = data.readString();
                    String _arg27 = data.readString();
                    String[] _result9 = getFeaturesForLanguage(_arg07, _arg110, _arg27);
                    reply.writeNoException();
                    reply.writeStringArray(_result9);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg08 = data.readStrongBinder();
                    String _arg111 = data.readString();
                    String _arg28 = data.readString();
                    String _arg35 = data.readString();
                    int _result10 = loadLanguage(_arg08, _arg111, _arg28, _arg35);
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg09 = data.readStrongBinder();
                    ITextToSpeechCallback _arg112 = ITextToSpeechCallback.Stub.asInterface(data.readStrongBinder());
                    setCallback(_arg09, _arg112);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    List<Voice> _result11 = getVoices();
                    reply.writeNoException();
                    reply.writeTypedList(_result11);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg010 = data.readStrongBinder();
                    String _arg113 = data.readString();
                    int _result12 = loadVoice(_arg010, _arg113);
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    String _arg114 = data.readString();
                    String _arg29 = data.readString();
                    String _result13 = getDefaultVoiceNameFor(_arg011, _arg114, _arg29);
                    reply.writeNoException();
                    reply.writeString(_result13);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg012 = data.readStrongBinder();
                    boolean _arg115 = data.readInt() != 0;
                    int _result14 = setSoloMode(_arg012, _arg115);
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg013 = data.readStrongBinder();
                    int _result15 = setListenOnStateChanged(_arg013);
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ITextToSpeechService {
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

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized int speak(IBinder callingInstance, CharSequence text, int queueMode, Bundle params, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingInstance);
                    if (text != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(text, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(queueMode);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(utteranceId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized int synthesizeToFileDescriptor(IBinder callingInstance, CharSequence text, ParcelFileDescriptor fileDescriptor, Bundle params, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingInstance);
                    if (text != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(text, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fileDescriptor != null) {
                        _data.writeInt(1);
                        fileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(utteranceId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized int playAudio(IBinder callingInstance, Uri audioUri, int queueMode, Bundle params, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingInstance);
                    if (audioUri != null) {
                        _data.writeInt(1);
                        audioUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(queueMode);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(utteranceId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized int playSilence(IBinder callingInstance, long duration, int queueMode, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingInstance);
                    _data.writeLong(duration);
                    _data.writeInt(queueMode);
                    _data.writeString(utteranceId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized boolean isSpeaking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public int stop(IBinder callingInstance, String utteranceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingInstance);
                    _data.writeString(utteranceId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized String[] getLanguage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized String[] getClientDefaultLanguage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized int isLanguageAvailable(String lang, String country, String variant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(lang);
                    _data.writeString(country);
                    _data.writeString(variant);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized String[] getFeaturesForLanguage(String lang, String country, String variant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(lang);
                    _data.writeString(country);
                    _data.writeString(variant);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized int loadLanguage(IBinder caller, String lang, String country, String variant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller);
                    _data.writeString(lang);
                    _data.writeString(country);
                    _data.writeString(variant);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized void setCallback(IBinder caller, ITextToSpeechCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized List<Voice> getVoices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    List<Voice> _result = _reply.createTypedArrayList(Voice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized int loadVoice(IBinder caller, String voiceName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller);
                    _data.writeString(voiceName);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public synchronized String getDefaultVoiceNameFor(String lang, String country, String variant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(lang);
                    _data.writeString(country);
                    _data.writeString(variant);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public int setSoloMode(IBinder caller, boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller);
                    _data.writeInt(on ? 1 : 0);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.speech.tts.ITextToSpeechService
            public int setListenOnStateChanged(IBinder caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
