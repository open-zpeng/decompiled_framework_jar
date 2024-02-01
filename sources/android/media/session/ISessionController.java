package android.media.session;

import android.app.PendingIntent;
import android.content.pm.ParceledListSlice;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.session.ISessionControllerCallback;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.KeyEvent;
/* loaded from: classes.dex */
public interface ISessionController extends IInterface {
    synchronized void adjustVolume(String str, ISessionControllerCallback iSessionControllerCallback, boolean z, int i, int i2) throws RemoteException;

    synchronized void fastForward(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized Bundle getExtras() throws RemoteException;

    synchronized long getFlags() throws RemoteException;

    synchronized PendingIntent getLaunchPendingIntent() throws RemoteException;

    synchronized MediaMetadata getMetadata() throws RemoteException;

    synchronized String getPackageName() throws RemoteException;

    synchronized PlaybackState getPlaybackState() throws RemoteException;

    synchronized ParceledListSlice getQueue() throws RemoteException;

    synchronized CharSequence getQueueTitle() throws RemoteException;

    synchronized int getRatingType() throws RemoteException;

    synchronized String getTag() throws RemoteException;

    synchronized ParcelableVolumeInfo getVolumeAttributes() throws RemoteException;

    synchronized boolean isTransportControlEnabled() throws RemoteException;

    synchronized void next(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void pause(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void play(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void playFromMediaId(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    synchronized void playFromSearch(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    synchronized void playFromUri(String str, ISessionControllerCallback iSessionControllerCallback, Uri uri, Bundle bundle) throws RemoteException;

    synchronized void prepare(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void prepareFromMediaId(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    synchronized void prepareFromSearch(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    synchronized void prepareFromUri(String str, ISessionControllerCallback iSessionControllerCallback, Uri uri, Bundle bundle) throws RemoteException;

    synchronized void previous(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void rate(String str, ISessionControllerCallback iSessionControllerCallback, Rating rating) throws RemoteException;

    synchronized void registerCallbackListener(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void rewind(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void seekTo(String str, ISessionControllerCallback iSessionControllerCallback, long j) throws RemoteException;

    synchronized void sendCommand(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle, ResultReceiver resultReceiver) throws RemoteException;

    synchronized void sendCustomAction(String str, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    synchronized boolean sendMediaButton(String str, ISessionControllerCallback iSessionControllerCallback, boolean z, KeyEvent keyEvent) throws RemoteException;

    synchronized void setVolumeTo(String str, ISessionControllerCallback iSessionControllerCallback, int i, int i2) throws RemoteException;

    synchronized void skipToQueueItem(String str, ISessionControllerCallback iSessionControllerCallback, long j) throws RemoteException;

    synchronized void stop(String str, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void unregisterCallbackListener(ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISessionController {
        private static final String DESCRIPTOR = "android.media.session.ISessionController";
        static final int TRANSACTION_adjustVolume = 11;
        static final int TRANSACTION_fastForward = 26;
        static final int TRANSACTION_getExtras = 35;
        static final int TRANSACTION_getFlags = 9;
        static final int TRANSACTION_getLaunchPendingIntent = 8;
        static final int TRANSACTION_getMetadata = 31;
        static final int TRANSACTION_getPackageName = 6;
        static final int TRANSACTION_getPlaybackState = 32;
        static final int TRANSACTION_getQueue = 33;
        static final int TRANSACTION_getQueueTitle = 34;
        static final int TRANSACTION_getRatingType = 36;
        static final int TRANSACTION_getTag = 7;
        static final int TRANSACTION_getVolumeAttributes = 10;
        static final int TRANSACTION_isTransportControlEnabled = 5;
        static final int TRANSACTION_next = 24;
        static final int TRANSACTION_pause = 22;
        static final int TRANSACTION_play = 17;
        static final int TRANSACTION_playFromMediaId = 18;
        static final int TRANSACTION_playFromSearch = 19;
        static final int TRANSACTION_playFromUri = 20;
        static final int TRANSACTION_prepare = 13;
        static final int TRANSACTION_prepareFromMediaId = 14;
        static final int TRANSACTION_prepareFromSearch = 15;
        static final int TRANSACTION_prepareFromUri = 16;
        static final int TRANSACTION_previous = 25;
        static final int TRANSACTION_rate = 29;
        static final int TRANSACTION_registerCallbackListener = 3;
        static final int TRANSACTION_rewind = 27;
        static final int TRANSACTION_seekTo = 28;
        static final int TRANSACTION_sendCommand = 1;
        static final int TRANSACTION_sendCustomAction = 30;
        static final int TRANSACTION_sendMediaButton = 2;
        static final int TRANSACTION_setVolumeTo = 12;
        static final int TRANSACTION_skipToQueueItem = 21;
        static final int TRANSACTION_stop = 23;
        static final int TRANSACTION_unregisterCallbackListener = 4;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ISessionController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISessionController)) {
                return (ISessionController) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bundle _arg3;
            ResultReceiver _arg4;
            Uri _arg2;
            Uri _arg22;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    ISessionControllerCallback _arg1 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg23 = data.readString();
                    if (data.readInt() != 0) {
                        Bundle _arg32 = Bundle.CREATOR.createFromParcel(data);
                        _arg3 = _arg32;
                    } else {
                        _arg3 = null;
                    }
                    if (data.readInt() != 0) {
                        ResultReceiver _arg42 = ResultReceiver.CREATOR.createFromParcel(data);
                        _arg4 = _arg42;
                    } else {
                        _arg4 = null;
                    }
                    sendCommand(_arg0, _arg1, _arg23, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    ISessionControllerCallback _arg12 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    boolean _arg24 = data.readInt() != 0;
                    KeyEvent _arg33 = data.readInt() != 0 ? KeyEvent.CREATOR.createFromParcel(data) : null;
                    boolean sendMediaButton = sendMediaButton(_arg02, _arg12, _arg24, _arg33);
                    reply.writeNoException();
                    reply.writeInt(sendMediaButton ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    ISessionControllerCallback _arg13 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    registerCallbackListener(_arg03, _arg13);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ISessionControllerCallback _arg04 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterCallbackListener(_arg04);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isTransportControlEnabled = isTransportControlEnabled();
                    reply.writeNoException();
                    reply.writeInt(isTransportControlEnabled ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getPackageName();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = getTag();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    PendingIntent _result3 = getLaunchPendingIntent();
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    long _result4 = getFlags();
                    reply.writeNoException();
                    reply.writeLong(_result4);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelableVolumeInfo _result5 = getVolumeAttributes();
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    ISessionControllerCallback _arg14 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    boolean _arg25 = data.readInt() != 0;
                    int _arg34 = data.readInt();
                    int _arg43 = data.readInt();
                    adjustVolume(_arg05, _arg14, _arg25, _arg34, _arg43);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    ISessionControllerCallback _arg15 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg26 = data.readInt();
                    int _arg35 = data.readInt();
                    setVolumeTo(_arg06, _arg15, _arg26, _arg35);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    ISessionControllerCallback _arg16 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    prepare(_arg07, _arg16);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    ISessionControllerCallback _arg17 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg27 = data.readString();
                    Bundle _arg36 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    prepareFromMediaId(_arg08, _arg17, _arg27, _arg36);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    ISessionControllerCallback _arg18 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg28 = data.readString();
                    Bundle _arg37 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    prepareFromSearch(_arg09, _arg18, _arg28, _arg37);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    ISessionControllerCallback _arg19 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg2 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    Bundle _arg38 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    prepareFromUri(_arg010, _arg19, _arg2, _arg38);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    ISessionControllerCallback _arg110 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    play(_arg011, _arg110);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    ISessionControllerCallback _arg111 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg29 = data.readString();
                    Bundle _arg39 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    playFromMediaId(_arg012, _arg111, _arg29, _arg39);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    ISessionControllerCallback _arg112 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg210 = data.readString();
                    Bundle _arg310 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    playFromSearch(_arg013, _arg112, _arg210, _arg310);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    ISessionControllerCallback _arg113 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg22 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    Bundle _arg311 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    playFromUri(_arg014, _arg113, _arg22, _arg311);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    ISessionControllerCallback _arg114 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    long _arg211 = data.readLong();
                    skipToQueueItem(_arg015, _arg114, _arg211);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    ISessionControllerCallback _arg115 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    pause(_arg016, _arg115);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    ISessionControllerCallback _arg116 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    stop(_arg017, _arg116);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    ISessionControllerCallback _arg117 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    next(_arg018, _arg117);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    ISessionControllerCallback _arg118 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    previous(_arg019, _arg118);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    ISessionControllerCallback _arg119 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    fastForward(_arg020, _arg119);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    ISessionControllerCallback _arg120 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    rewind(_arg021, _arg120);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg022 = data.readString();
                    ISessionControllerCallback _arg121 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    long _arg212 = data.readLong();
                    seekTo(_arg022, _arg121, _arg212);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg023 = data.readString();
                    ISessionControllerCallback _arg122 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    Rating _arg213 = data.readInt() != 0 ? Rating.CREATOR.createFromParcel(data) : null;
                    rate(_arg023, _arg122, _arg213);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    ISessionControllerCallback _arg123 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg214 = data.readString();
                    Bundle _arg312 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    sendCustomAction(_arg024, _arg123, _arg214, _arg312);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    MediaMetadata _result6 = getMetadata();
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    PlaybackState _result7 = getPlaybackState();
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    ParceledListSlice _result8 = getQueue();
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    CharSequence _result9 = getQueueTitle();
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        TextUtils.writeToParcel(_result9, reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _result10 = getExtras();
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    int _result11 = getRatingType();
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ISessionController {
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

            @Override // android.media.session.ISessionController
            public synchronized void sendCommand(String packageName, ISessionControllerCallback caller, String command, Bundle args, ResultReceiver cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(command);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (cb != null) {
                        _data.writeInt(1);
                        cb.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized boolean sendMediaButton(String packageName, ISessionControllerCallback caller, boolean asSystemService, KeyEvent mediaButton) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeInt(asSystemService ? 1 : 0);
                    if (mediaButton != null) {
                        _data.writeInt(1);
                        mediaButton.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void registerCallbackListener(String packageName, ISessionControllerCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void unregisterCallbackListener(ISessionControllerCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized boolean isTransportControlEnabled() throws RemoteException {
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

            @Override // android.media.session.ISessionController
            public synchronized String getPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized String getTag() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized PendingIntent getLaunchPendingIntent() throws RemoteException {
                PendingIntent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PendingIntent.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized long getFlags() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized ParcelableVolumeInfo getVolumeAttributes() throws RemoteException {
                ParcelableVolumeInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelableVolumeInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void adjustVolume(String packageName, ISessionControllerCallback caller, boolean asSystemService, int direction, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeInt(asSystemService ? 1 : 0);
                    _data.writeInt(direction);
                    _data.writeInt(flags);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void setVolumeTo(String packageName, ISessionControllerCallback caller, int value, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeInt(value);
                    _data.writeInt(flags);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void prepare(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void prepareFromMediaId(String packageName, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(mediaId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void prepareFromSearch(String packageName, ISessionControllerCallback caller, String string, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(string);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void prepareFromUri(String packageName, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void play(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void playFromMediaId(String packageName, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(mediaId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void playFromSearch(String packageName, ISessionControllerCallback caller, String string, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(string);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void playFromUri(String packageName, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void skipToQueueItem(String packageName, ISessionControllerCallback caller, long id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeLong(id);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void pause(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void stop(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void next(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void previous(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void fastForward(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void rewind(String packageName, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void seekTo(String packageName, ISessionControllerCallback caller, long pos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeLong(pos);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void rate(String packageName, ISessionControllerCallback caller, Rating rating) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (rating != null) {
                        _data.writeInt(1);
                        rating.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized void sendCustomAction(String packageName, ISessionControllerCallback caller, String action, Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(action);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized MediaMetadata getMetadata() throws RemoteException {
                MediaMetadata _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = MediaMetadata.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized PlaybackState getPlaybackState() throws RemoteException {
                PlaybackState _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PlaybackState.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized ParceledListSlice getQueue() throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized CharSequence getQueueTitle() throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized Bundle getExtras() throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionController
            public synchronized int getRatingType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
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
