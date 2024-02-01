package android.media.session;

import android.content.Intent;
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
/* loaded from: classes.dex */
public interface ISessionCallback extends IInterface {
    synchronized void onAdjustVolume(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, int i3) throws RemoteException;

    synchronized void onCommand(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle, ResultReceiver resultReceiver) throws RemoteException;

    synchronized void onCustomAction(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    synchronized void onFastForward(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void onMediaButton(String str, int i, int i2, Intent intent, int i3, ResultReceiver resultReceiver) throws RemoteException;

    synchronized void onMediaButtonFromController(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, Intent intent) throws RemoteException;

    synchronized void onNext(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void onPause(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void onPlay(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void onPlayFromMediaId(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    synchronized void onPlayFromSearch(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    synchronized void onPlayFromUri(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, Uri uri, Bundle bundle) throws RemoteException;

    synchronized void onPrepare(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void onPrepareFromMediaId(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    synchronized void onPrepareFromSearch(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, String str2, Bundle bundle) throws RemoteException;

    synchronized void onPrepareFromUri(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, Uri uri, Bundle bundle) throws RemoteException;

    synchronized void onPrevious(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void onRate(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, Rating rating) throws RemoteException;

    synchronized void onRewind(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    synchronized void onSeekTo(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, long j) throws RemoteException;

    synchronized void onSetVolumeTo(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, int i3) throws RemoteException;

    synchronized void onSkipToTrack(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback, long j) throws RemoteException;

    synchronized void onStop(String str, int i, int i2, ISessionControllerCallback iSessionControllerCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISessionCallback {
        private static final String DESCRIPTOR = "android.media.session.ISessionCallback";
        static final int TRANSACTION_onAdjustVolume = 22;
        static final int TRANSACTION_onCommand = 1;
        static final int TRANSACTION_onCustomAction = 21;
        static final int TRANSACTION_onFastForward = 17;
        static final int TRANSACTION_onMediaButton = 2;
        static final int TRANSACTION_onMediaButtonFromController = 3;
        static final int TRANSACTION_onNext = 15;
        static final int TRANSACTION_onPause = 13;
        static final int TRANSACTION_onPlay = 8;
        static final int TRANSACTION_onPlayFromMediaId = 9;
        static final int TRANSACTION_onPlayFromSearch = 10;
        static final int TRANSACTION_onPlayFromUri = 11;
        static final int TRANSACTION_onPrepare = 4;
        static final int TRANSACTION_onPrepareFromMediaId = 5;
        static final int TRANSACTION_onPrepareFromSearch = 6;
        static final int TRANSACTION_onPrepareFromUri = 7;
        static final int TRANSACTION_onPrevious = 16;
        static final int TRANSACTION_onRate = 20;
        static final int TRANSACTION_onRewind = 18;
        static final int TRANSACTION_onSeekTo = 19;
        static final int TRANSACTION_onSetVolumeTo = 23;
        static final int TRANSACTION_onSkipToTrack = 12;
        static final int TRANSACTION_onStop = 14;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ISessionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISessionCallback)) {
                return (ISessionCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bundle _arg5;
            Intent _arg3;
            Uri _arg4;
            Uri _arg42;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    ISessionControllerCallback _arg32 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg43 = data.readString();
                    if (data.readInt() != 0) {
                        Bundle _arg52 = Bundle.CREATOR.createFromParcel(data);
                        _arg5 = _arg52;
                    } else {
                        _arg5 = null;
                    }
                    ResultReceiver _arg6 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
                    onCommand(_arg0, _arg1, _arg2, _arg32, _arg43, _arg5, _arg6);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    int _arg12 = data.readInt();
                    int _arg22 = data.readInt();
                    if (data.readInt() != 0) {
                        Intent _arg33 = Intent.CREATOR.createFromParcel(data);
                        _arg3 = _arg33;
                    } else {
                        _arg3 = null;
                    }
                    int _arg44 = data.readInt();
                    ResultReceiver _arg53 = data.readInt() != 0 ? ResultReceiver.CREATOR.createFromParcel(data) : null;
                    onMediaButton(_arg02, _arg12, _arg22, _arg3, _arg44, _arg53);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    int _arg13 = data.readInt();
                    int _arg23 = data.readInt();
                    ISessionControllerCallback _arg34 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    Intent _arg45 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    onMediaButtonFromController(_arg03, _arg13, _arg23, _arg34, _arg45);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    int _arg14 = data.readInt();
                    int _arg24 = data.readInt();
                    ISessionControllerCallback _arg35 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    onPrepare(_arg04, _arg14, _arg24, _arg35);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    int _arg15 = data.readInt();
                    int _arg25 = data.readInt();
                    ISessionControllerCallback _arg36 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg46 = data.readString();
                    Bundle _arg54 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onPrepareFromMediaId(_arg05, _arg15, _arg25, _arg36, _arg46, _arg54);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    int _arg16 = data.readInt();
                    int _arg26 = data.readInt();
                    ISessionControllerCallback _arg37 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg47 = data.readString();
                    Bundle _arg55 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onPrepareFromSearch(_arg06, _arg16, _arg26, _arg37, _arg47, _arg55);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    int _arg17 = data.readInt();
                    int _arg27 = data.readInt();
                    ISessionControllerCallback _arg38 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        Uri _arg48 = Uri.CREATOR.createFromParcel(data);
                        _arg4 = _arg48;
                    } else {
                        _arg4 = null;
                    }
                    Bundle _arg56 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onPrepareFromUri(_arg07, _arg17, _arg27, _arg38, _arg4, _arg56);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    int _arg18 = data.readInt();
                    int _arg28 = data.readInt();
                    ISessionControllerCallback _arg39 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    onPlay(_arg08, _arg18, _arg28, _arg39);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    int _arg19 = data.readInt();
                    int _arg29 = data.readInt();
                    ISessionControllerCallback _arg310 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg49 = data.readString();
                    Bundle _arg57 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onPlayFromMediaId(_arg09, _arg19, _arg29, _arg310, _arg49, _arg57);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    int _arg110 = data.readInt();
                    int _arg210 = data.readInt();
                    ISessionControllerCallback _arg311 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg410 = data.readString();
                    Bundle _arg58 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onPlayFromSearch(_arg010, _arg110, _arg210, _arg311, _arg410, _arg58);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    int _arg111 = data.readInt();
                    int _arg211 = data.readInt();
                    ISessionControllerCallback _arg312 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        Uri _arg411 = Uri.CREATOR.createFromParcel(data);
                        _arg42 = _arg411;
                    } else {
                        _arg42 = null;
                    }
                    Bundle _arg59 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onPlayFromUri(_arg011, _arg111, _arg211, _arg312, _arg42, _arg59);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    int _arg112 = data.readInt();
                    int _arg212 = data.readInt();
                    ISessionControllerCallback _arg313 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    long _arg412 = data.readLong();
                    onSkipToTrack(_arg012, _arg112, _arg212, _arg313, _arg412);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    int _arg113 = data.readInt();
                    int _arg213 = data.readInt();
                    ISessionControllerCallback _arg314 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    onPause(_arg013, _arg113, _arg213, _arg314);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    int _arg114 = data.readInt();
                    int _arg214 = data.readInt();
                    ISessionControllerCallback _arg315 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    onStop(_arg014, _arg114, _arg214, _arg315);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    int _arg115 = data.readInt();
                    int _arg215 = data.readInt();
                    ISessionControllerCallback _arg316 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    onNext(_arg015, _arg115, _arg215, _arg316);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    int _arg116 = data.readInt();
                    int _arg216 = data.readInt();
                    ISessionControllerCallback _arg317 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    onPrevious(_arg016, _arg116, _arg216, _arg317);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    int _arg117 = data.readInt();
                    int _arg217 = data.readInt();
                    ISessionControllerCallback _arg318 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    onFastForward(_arg017, _arg117, _arg217, _arg318);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    int _arg118 = data.readInt();
                    int _arg218 = data.readInt();
                    ISessionControllerCallback _arg319 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    onRewind(_arg018, _arg118, _arg218, _arg319);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    int _arg119 = data.readInt();
                    int _arg219 = data.readInt();
                    ISessionControllerCallback _arg320 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    long _arg413 = data.readLong();
                    onSeekTo(_arg019, _arg119, _arg219, _arg320, _arg413);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    int _arg120 = data.readInt();
                    int _arg220 = data.readInt();
                    ISessionControllerCallback _arg321 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    Rating _arg414 = data.readInt() != 0 ? Rating.CREATOR.createFromParcel(data) : null;
                    onRate(_arg020, _arg120, _arg220, _arg321, _arg414);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    int _arg121 = data.readInt();
                    int _arg221 = data.readInt();
                    ISessionControllerCallback _arg322 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg415 = data.readString();
                    Bundle _arg510 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onCustomAction(_arg021, _arg121, _arg221, _arg322, _arg415, _arg510);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg022 = data.readString();
                    int _arg122 = data.readInt();
                    int _arg222 = data.readInt();
                    ISessionControllerCallback _arg323 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg416 = data.readInt();
                    onAdjustVolume(_arg022, _arg122, _arg222, _arg323, _arg416);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg023 = data.readString();
                    int _arg123 = data.readInt();
                    int _arg223 = data.readInt();
                    ISessionControllerCallback _arg324 = ISessionControllerCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg417 = data.readInt();
                    onSetVolumeTo(_arg023, _arg123, _arg223, _arg324, _arg417);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements ISessionCallback {
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

            @Override // android.media.session.ISessionCallback
            public synchronized void onCommand(String packageName, int pid, int uid, ISessionControllerCallback caller, String command, Bundle args, ResultReceiver cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
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
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onMediaButton(String packageName, int pid, int uid, Intent mediaButtonIntent, int sequenceNumber, ResultReceiver cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    if (mediaButtonIntent != null) {
                        _data.writeInt(1);
                        mediaButtonIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sequenceNumber);
                    if (cb != null) {
                        _data.writeInt(1);
                        cb.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onMediaButtonFromController(String packageName, int pid, int uid, ISessionControllerCallback caller, Intent mediaButtonIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (mediaButtonIntent != null) {
                        _data.writeInt(1);
                        mediaButtonIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onPrepare(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onPrepareFromMediaId(String packageName, int pid, int uid, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(mediaId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onPrepareFromSearch(String packageName, int pid, int uid, ISessionControllerCallback caller, String query, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(query);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onPrepareFromUri(String packageName, int pid, int uid, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
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
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onPlay(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onPlayFromMediaId(String packageName, int pid, int uid, ISessionControllerCallback caller, String mediaId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(mediaId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onPlayFromSearch(String packageName, int pid, int uid, ISessionControllerCallback caller, String query, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(query);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onPlayFromUri(String packageName, int pid, int uid, ISessionControllerCallback caller, Uri uri, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
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
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onSkipToTrack(String packageName, int pid, int uid, ISessionControllerCallback caller, long id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeLong(id);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onPause(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onStop(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onNext(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onPrevious(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onFastForward(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onRewind(String packageName, int pid, int uid, ISessionControllerCallback caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onSeekTo(String packageName, int pid, int uid, ISessionControllerCallback caller, long pos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeLong(pos);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onRate(String packageName, int pid, int uid, ISessionControllerCallback caller, Rating rating) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (rating != null) {
                        _data.writeInt(1);
                        rating.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onCustomAction(String packageName, int pid, int uid, ISessionControllerCallback caller, String action, Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(action);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onAdjustVolume(String packageName, int pid, int uid, ISessionControllerCallback caller, int direction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeInt(direction);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionCallback
            public synchronized void onSetVolumeTo(String packageName, int pid, int uid, ISessionControllerCallback caller, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeInt(value);
                    this.mRemote.transact(23, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
