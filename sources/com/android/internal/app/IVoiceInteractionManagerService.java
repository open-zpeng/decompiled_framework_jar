package com.android.internal.app;

import android.content.ComponentName;
import android.content.Intent;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.SoundTrigger;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.service.voice.IVoiceInteractionService;
import android.service.voice.IVoiceInteractionSession;
import com.android.internal.app.IVoiceInteractionSessionListener;
import com.android.internal.app.IVoiceInteractionSessionShowCallback;
import com.android.internal.app.IVoiceInteractor;
/* loaded from: classes3.dex */
public interface IVoiceInteractionManagerService extends IInterface {
    synchronized boolean activeServiceSupportsAssist() throws RemoteException;

    synchronized boolean activeServiceSupportsLaunchFromKeyguard() throws RemoteException;

    synchronized void closeSystemDialogs(IBinder iBinder) throws RemoteException;

    synchronized int deleteKeyphraseSoundModel(int i, String str) throws RemoteException;

    synchronized boolean deliverNewSession(IBinder iBinder, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor) throws RemoteException;

    synchronized void finish(IBinder iBinder) throws RemoteException;

    synchronized ComponentName getActiveServiceComponentName() throws RemoteException;

    synchronized int getDisabledShowContext() throws RemoteException;

    synchronized SoundTrigger.ModuleProperties getDspModuleProperties(IVoiceInteractionService iVoiceInteractionService) throws RemoteException;

    private protected SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int i, String str) throws RemoteException;

    synchronized int getUserDisabledShowContext() throws RemoteException;

    synchronized void hideCurrentSession() throws RemoteException;

    synchronized boolean hideSessionFromSession(IBinder iBinder) throws RemoteException;

    synchronized boolean isEnrolledForKeyphrase(IVoiceInteractionService iVoiceInteractionService, int i, String str) throws RemoteException;

    synchronized boolean isSessionRunning() throws RemoteException;

    synchronized void launchVoiceAssistFromKeyguard() throws RemoteException;

    synchronized void onLockscreenShown() throws RemoteException;

    synchronized void registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener iVoiceInteractionSessionListener) throws RemoteException;

    synchronized void setDisabledShowContext(int i) throws RemoteException;

    synchronized void setKeepAwake(IBinder iBinder, boolean z) throws RemoteException;

    synchronized void showSession(IVoiceInteractionService iVoiceInteractionService, Bundle bundle, int i) throws RemoteException;

    synchronized boolean showSessionForActiveService(Bundle bundle, int i, IVoiceInteractionSessionShowCallback iVoiceInteractionSessionShowCallback, IBinder iBinder) throws RemoteException;

    synchronized boolean showSessionFromSession(IBinder iBinder, Bundle bundle, int i) throws RemoteException;

    synchronized int startAssistantActivity(IBinder iBinder, Intent intent, String str) throws RemoteException;

    synchronized int startRecognition(IVoiceInteractionService iVoiceInteractionService, int i, String str, IRecognitionStatusCallback iRecognitionStatusCallback, SoundTrigger.RecognitionConfig recognitionConfig) throws RemoteException;

    synchronized int startVoiceActivity(IBinder iBinder, Intent intent, String str) throws RemoteException;

    synchronized int stopRecognition(IVoiceInteractionService iVoiceInteractionService, int i, IRecognitionStatusCallback iRecognitionStatusCallback) throws RemoteException;

    synchronized int updateKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel keyphraseSoundModel) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IVoiceInteractionManagerService {
        private static final String DESCRIPTOR = "com.android.internal.app.IVoiceInteractionManagerService";
        static final int TRANSACTION_activeServiceSupportsAssist = 25;
        static final int TRANSACTION_activeServiceSupportsLaunchFromKeyguard = 26;
        static final int TRANSACTION_closeSystemDialogs = 8;
        static final int TRANSACTION_deleteKeyphraseSoundModel = 15;
        static final int TRANSACTION_deliverNewSession = 2;
        static final int TRANSACTION_finish = 9;
        static final int TRANSACTION_getActiveServiceComponentName = 20;
        static final int TRANSACTION_getDisabledShowContext = 11;
        static final int TRANSACTION_getDspModuleProperties = 16;
        static final int TRANSACTION_getKeyphraseSoundModel = 13;
        static final int TRANSACTION_getUserDisabledShowContext = 12;
        static final int TRANSACTION_hideCurrentSession = 22;
        static final int TRANSACTION_hideSessionFromSession = 4;
        static final int TRANSACTION_isEnrolledForKeyphrase = 17;
        static final int TRANSACTION_isSessionRunning = 24;
        static final int TRANSACTION_launchVoiceAssistFromKeyguard = 23;
        static final int TRANSACTION_onLockscreenShown = 27;
        static final int TRANSACTION_registerVoiceInteractionSessionListener = 28;
        static final int TRANSACTION_setDisabledShowContext = 10;
        static final int TRANSACTION_setKeepAwake = 7;
        static final int TRANSACTION_showSession = 1;
        static final int TRANSACTION_showSessionForActiveService = 21;
        static final int TRANSACTION_showSessionFromSession = 3;
        static final int TRANSACTION_startAssistantActivity = 6;
        static final int TRANSACTION_startRecognition = 18;
        static final int TRANSACTION_startVoiceActivity = 5;
        static final int TRANSACTION_stopRecognition = 19;
        static final int TRANSACTION_updateKeyphraseSoundModel = 14;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IVoiceInteractionManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IVoiceInteractionManagerService)) {
                return (IVoiceInteractionManagerService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            SoundTrigger.RecognitionConfig _arg4;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractionService _arg0 = IVoiceInteractionService.Stub.asInterface(data.readStrongBinder());
                    Bundle _arg1 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    int _arg2 = data.readInt();
                    showSession(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg02 = data.readStrongBinder();
                    IVoiceInteractionSession _arg12 = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                    IVoiceInteractor _arg22 = IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
                    boolean deliverNewSession = deliverNewSession(_arg02, _arg12, _arg22);
                    reply.writeNoException();
                    reply.writeInt(deliverNewSession ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg03 = data.readStrongBinder();
                    Bundle _arg13 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    int _arg23 = data.readInt();
                    boolean showSessionFromSession = showSessionFromSession(_arg03, _arg13, _arg23);
                    reply.writeNoException();
                    reply.writeInt(showSessionFromSession ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg04 = data.readStrongBinder();
                    boolean hideSessionFromSession = hideSessionFromSession(_arg04);
                    reply.writeNoException();
                    reply.writeInt(hideSessionFromSession ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg05 = data.readStrongBinder();
                    Intent _arg14 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg24 = data.readString();
                    int _result = startVoiceActivity(_arg05, _arg14, _arg24);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg06 = data.readStrongBinder();
                    Intent _arg15 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    String _arg25 = data.readString();
                    int _result2 = startAssistantActivity(_arg06, _arg15, _arg25);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg07 = data.readStrongBinder();
                    boolean _arg16 = data.readInt() != 0;
                    setKeepAwake(_arg07, _arg16);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg08 = data.readStrongBinder();
                    closeSystemDialogs(_arg08);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg09 = data.readStrongBinder();
                    finish(_arg09);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    setDisabledShowContext(_arg010);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _result3 = getDisabledShowContext();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _result4 = getUserDisabledShowContext();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    String _arg17 = data.readString();
                    SoundTrigger.KeyphraseSoundModel _result5 = getKeyphraseSoundModel(_arg011, _arg17);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    SoundTrigger.KeyphraseSoundModel _arg012 = data.readInt() != 0 ? SoundTrigger.KeyphraseSoundModel.CREATOR.createFromParcel(data) : null;
                    int _result6 = updateKeyphraseSoundModel(_arg012);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    String _arg18 = data.readString();
                    int _result7 = deleteKeyphraseSoundModel(_arg013, _arg18);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractionService _arg014 = IVoiceInteractionService.Stub.asInterface(data.readStrongBinder());
                    SoundTrigger.ModuleProperties _result8 = getDspModuleProperties(_arg014);
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractionService _arg015 = IVoiceInteractionService.Stub.asInterface(data.readStrongBinder());
                    int _arg19 = data.readInt();
                    String _arg26 = data.readString();
                    boolean isEnrolledForKeyphrase = isEnrolledForKeyphrase(_arg015, _arg19, _arg26);
                    reply.writeNoException();
                    reply.writeInt(isEnrolledForKeyphrase ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractionService _arg016 = IVoiceInteractionService.Stub.asInterface(data.readStrongBinder());
                    int _arg110 = data.readInt();
                    String _arg27 = data.readString();
                    IRecognitionStatusCallback _arg3 = IRecognitionStatusCallback.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        SoundTrigger.RecognitionConfig _arg42 = SoundTrigger.RecognitionConfig.CREATOR.createFromParcel(data);
                        _arg4 = _arg42;
                    } else {
                        _arg4 = null;
                    }
                    int _result9 = startRecognition(_arg016, _arg110, _arg27, _arg3, _arg4);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractionService _arg017 = IVoiceInteractionService.Stub.asInterface(data.readStrongBinder());
                    int _arg111 = data.readInt();
                    IRecognitionStatusCallback _arg28 = IRecognitionStatusCallback.Stub.asInterface(data.readStrongBinder());
                    int _result10 = stopRecognition(_arg017, _arg111, _arg28);
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result11 = getActiveServiceComponentName();
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg018 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    Bundle _arg019 = _arg018;
                    int _arg112 = data.readInt();
                    IVoiceInteractionSessionShowCallback _arg29 = IVoiceInteractionSessionShowCallback.Stub.asInterface(data.readStrongBinder());
                    IBinder _arg32 = data.readStrongBinder();
                    boolean showSessionForActiveService = showSessionForActiveService(_arg019, _arg112, _arg29, _arg32);
                    reply.writeNoException();
                    reply.writeInt(showSessionForActiveService ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    hideCurrentSession();
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    launchVoiceAssistFromKeyguard();
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSessionRunning = isSessionRunning();
                    reply.writeNoException();
                    reply.writeInt(isSessionRunning ? 1 : 0);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    boolean activeServiceSupportsAssist = activeServiceSupportsAssist();
                    reply.writeNoException();
                    reply.writeInt(activeServiceSupportsAssist ? 1 : 0);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    boolean activeServiceSupportsLaunchFromKeyguard = activeServiceSupportsLaunchFromKeyguard();
                    reply.writeNoException();
                    reply.writeInt(activeServiceSupportsLaunchFromKeyguard ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    onLockscreenShown();
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    IVoiceInteractionSessionListener _arg020 = IVoiceInteractionSessionListener.Stub.asInterface(data.readStrongBinder());
                    registerVoiceInteractionSessionListener(_arg020);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IVoiceInteractionManagerService {
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

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized void showSession(IVoiceInteractionService service, Bundle sessionArgs, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    if (sessionArgs != null) {
                        _data.writeInt(1);
                        sessionArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized boolean deliverNewSession(IBinder token, IVoiceInteractionSession session, IVoiceInteractor interactor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeStrongBinder(interactor != null ? interactor.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected boolean showSessionFromSession(IBinder token, Bundle sessionArgs, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (sessionArgs != null) {
                        _data.writeInt(1);
                        sessionArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized boolean hideSessionFromSession(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized int startVoiceActivity(IBinder token, Intent intent, String resolvedType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized int startAssistantActivity(IBinder token, Intent intent, String resolvedType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized void setKeepAwake(IBinder token, boolean keepAwake) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(keepAwake ? 1 : 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized void closeSystemDialogs(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized void finish(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized void setDisabledShowContext(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized int getDisabledShowContext() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized int getUserDisabledShowContext() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int keyphraseId, String bcp47Locale) throws RemoteException {
                SoundTrigger.KeyphraseSoundModel _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(keyphraseId);
                    _data.writeString(bcp47Locale);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SoundTrigger.KeyphraseSoundModel.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized int updateKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel model) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (model != null) {
                        _data.writeInt(1);
                        model.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized int deleteKeyphraseSoundModel(int keyphraseId, String bcp47Locale) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(keyphraseId);
                    _data.writeString(bcp47Locale);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized SoundTrigger.ModuleProperties getDspModuleProperties(IVoiceInteractionService service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    SoundTrigger.ModuleProperties _result = null;
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SoundTrigger.ModuleProperties.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized boolean isEnrolledForKeyphrase(IVoiceInteractionService service, int keyphraseId, String bcp47Locale) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    _data.writeInt(keyphraseId);
                    _data.writeString(bcp47Locale);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized int startRecognition(IVoiceInteractionService service, int keyphraseId, String bcp47Locale, IRecognitionStatusCallback callback, SoundTrigger.RecognitionConfig recognitionConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    _data.writeInt(keyphraseId);
                    _data.writeString(bcp47Locale);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (recognitionConfig != null) {
                        _data.writeInt(1);
                        recognitionConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized int stopRecognition(IVoiceInteractionService service, int keyphraseId, IRecognitionStatusCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(service != null ? service.asBinder() : null);
                    _data.writeInt(keyphraseId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized ComponentName getActiveServiceComponentName() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized boolean showSessionForActiveService(Bundle args, int sourceFlags, IVoiceInteractionSessionShowCallback showCallback, IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceFlags);
                    _data.writeStrongBinder(showCallback != null ? showCallback.asBinder() : null);
                    _data.writeStrongBinder(activityToken);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized void hideCurrentSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized void launchVoiceAssistFromKeyguard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized boolean isSessionRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized boolean activeServiceSupportsAssist() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized boolean activeServiceSupportsLaunchFromKeyguard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized void onLockscreenShown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.app.IVoiceInteractionManagerService
            public synchronized void registerVoiceInteractionSessionListener(IVoiceInteractionSessionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
