package com.android.internal.app;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.SoundTrigger;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
/* loaded from: classes3.dex */
public interface ISoundTriggerService extends IInterface {
    synchronized void deleteSoundModel(ParcelUuid parcelUuid) throws RemoteException;

    synchronized SoundTrigger.GenericSoundModel getSoundModel(ParcelUuid parcelUuid) throws RemoteException;

    synchronized boolean isRecognitionActive(ParcelUuid parcelUuid) throws RemoteException;

    synchronized int loadGenericSoundModel(SoundTrigger.GenericSoundModel genericSoundModel) throws RemoteException;

    synchronized int loadKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel keyphraseSoundModel) throws RemoteException;

    synchronized int startRecognition(ParcelUuid parcelUuid, IRecognitionStatusCallback iRecognitionStatusCallback, SoundTrigger.RecognitionConfig recognitionConfig) throws RemoteException;

    synchronized int startRecognitionForIntent(ParcelUuid parcelUuid, PendingIntent pendingIntent, SoundTrigger.RecognitionConfig recognitionConfig) throws RemoteException;

    synchronized int startRecognitionForService(ParcelUuid parcelUuid, Bundle bundle, ComponentName componentName, SoundTrigger.RecognitionConfig recognitionConfig) throws RemoteException;

    synchronized int stopRecognition(ParcelUuid parcelUuid, IRecognitionStatusCallback iRecognitionStatusCallback) throws RemoteException;

    synchronized int stopRecognitionForIntent(ParcelUuid parcelUuid) throws RemoteException;

    synchronized int unloadSoundModel(ParcelUuid parcelUuid) throws RemoteException;

    synchronized void updateSoundModel(SoundTrigger.GenericSoundModel genericSoundModel) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ISoundTriggerService {
        private static final String DESCRIPTOR = "com.android.internal.app.ISoundTriggerService";
        static final int TRANSACTION_deleteSoundModel = 3;
        static final int TRANSACTION_getSoundModel = 1;
        static final int TRANSACTION_isRecognitionActive = 12;
        static final int TRANSACTION_loadGenericSoundModel = 6;
        static final int TRANSACTION_loadKeyphraseSoundModel = 7;
        static final int TRANSACTION_startRecognition = 4;
        static final int TRANSACTION_startRecognitionForIntent = 8;
        static final int TRANSACTION_startRecognitionForService = 9;
        static final int TRANSACTION_stopRecognition = 5;
        static final int TRANSACTION_stopRecognitionForIntent = 10;
        static final int TRANSACTION_unloadSoundModel = 11;
        static final int TRANSACTION_updateSoundModel = 2;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ISoundTriggerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISoundTriggerService)) {
                return (ISoundTriggerService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ParcelUuid _arg0;
            ParcelUuid _arg02;
            PendingIntent _arg1;
            ParcelUuid _arg03;
            Bundle _arg12;
            ComponentName _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelUuid _arg04 = data.readInt() != 0 ? ParcelUuid.CREATOR.createFromParcel(data) : null;
                    SoundTrigger.GenericSoundModel _result = getSoundModel(_arg04);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    SoundTrigger.GenericSoundModel _arg05 = data.readInt() != 0 ? SoundTrigger.GenericSoundModel.CREATOR.createFromParcel(data) : null;
                    updateSoundModel(_arg05);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelUuid _arg06 = data.readInt() != 0 ? ParcelUuid.CREATOR.createFromParcel(data) : null;
                    deleteSoundModel(_arg06);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    IRecognitionStatusCallback _arg13 = IRecognitionStatusCallback.Stub.asInterface(data.readStrongBinder());
                    SoundTrigger.RecognitionConfig _arg22 = data.readInt() != 0 ? SoundTrigger.RecognitionConfig.CREATOR.createFromParcel(data) : null;
                    int _result2 = startRecognition(_arg0, _arg13, _arg22);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelUuid _arg07 = data.readInt() != 0 ? ParcelUuid.CREATOR.createFromParcel(data) : null;
                    IRecognitionStatusCallback _arg14 = IRecognitionStatusCallback.Stub.asInterface(data.readStrongBinder());
                    int _result3 = stopRecognition(_arg07, _arg14);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    SoundTrigger.GenericSoundModel _arg08 = data.readInt() != 0 ? SoundTrigger.GenericSoundModel.CREATOR.createFromParcel(data) : null;
                    int _result4 = loadGenericSoundModel(_arg08);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    SoundTrigger.KeyphraseSoundModel _arg09 = data.readInt() != 0 ? SoundTrigger.KeyphraseSoundModel.CREATOR.createFromParcel(data) : null;
                    int _result5 = loadKeyphraseSoundModel(_arg09);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    SoundTrigger.RecognitionConfig _arg23 = data.readInt() != 0 ? SoundTrigger.RecognitionConfig.CREATOR.createFromParcel(data) : null;
                    int _result6 = startRecognitionForIntent(_arg02, _arg1, _arg23);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    SoundTrigger.RecognitionConfig _arg3 = data.readInt() != 0 ? SoundTrigger.RecognitionConfig.CREATOR.createFromParcel(data) : null;
                    int _result7 = startRecognitionForService(_arg03, _arg12, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelUuid _arg010 = data.readInt() != 0 ? ParcelUuid.CREATOR.createFromParcel(data) : null;
                    int _result8 = stopRecognitionForIntent(_arg010);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelUuid _arg011 = data.readInt() != 0 ? ParcelUuid.CREATOR.createFromParcel(data) : null;
                    int _result9 = unloadSoundModel(_arg011);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelUuid _arg012 = data.readInt() != 0 ? ParcelUuid.CREATOR.createFromParcel(data) : null;
                    boolean isRecognitionActive = isRecognitionActive(_arg012);
                    reply.writeNoException();
                    reply.writeInt(isRecognitionActive ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements ISoundTriggerService {
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

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized SoundTrigger.GenericSoundModel getSoundModel(ParcelUuid soundModelId) throws RemoteException {
                SoundTrigger.GenericSoundModel _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SoundTrigger.GenericSoundModel.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized void updateSoundModel(SoundTrigger.GenericSoundModel soundModel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModel != null) {
                        _data.writeInt(1);
                        soundModel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized void deleteSoundModel(ParcelUuid soundModelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized int startRecognition(ParcelUuid soundModelId, IRecognitionStatusCallback callback, SoundTrigger.RecognitionConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized int stopRecognition(ParcelUuid soundModelId, IRecognitionStatusCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized int loadGenericSoundModel(SoundTrigger.GenericSoundModel soundModel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModel != null) {
                        _data.writeInt(1);
                        soundModel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized int loadKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel soundModel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModel != null) {
                        _data.writeInt(1);
                        soundModel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized int startRecognitionForIntent(ParcelUuid soundModelId, PendingIntent callbackIntent, SoundTrigger.RecognitionConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized int startRecognitionForService(ParcelUuid soundModelId, Bundle params, ComponentName callbackIntent, SoundTrigger.RecognitionConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized int stopRecognitionForIntent(ParcelUuid soundModelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized int unloadSoundModel(ParcelUuid soundModelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (soundModelId != null) {
                        _data.writeInt(1);
                        soundModelId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.ISoundTriggerService
            public synchronized boolean isRecognitionActive(ParcelUuid parcelUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (parcelUuid != null) {
                        _data.writeInt(1);
                        parcelUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
