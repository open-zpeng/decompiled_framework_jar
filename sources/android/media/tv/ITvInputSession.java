package android.media.tv;

import android.graphics.Rect;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.Surface;

/* loaded from: classes2.dex */
public interface ITvInputSession extends IInterface {
    void appPrivateCommand(String str, Bundle bundle) throws RemoteException;

    void createOverlayView(IBinder iBinder, Rect rect) throws RemoteException;

    void dispatchSurfaceChanged(int i, int i2, int i3) throws RemoteException;

    void relayoutOverlayView(Rect rect) throws RemoteException;

    void release() throws RemoteException;

    void removeOverlayView() throws RemoteException;

    void selectTrack(int i, String str) throws RemoteException;

    void setCaptionEnabled(boolean z) throws RemoteException;

    void setMain(boolean z) throws RemoteException;

    void setSurface(Surface surface) throws RemoteException;

    void setVolume(float f) throws RemoteException;

    void startRecording(Uri uri) throws RemoteException;

    void stopRecording() throws RemoteException;

    void timeShiftEnablePositionTracking(boolean z) throws RemoteException;

    void timeShiftPause() throws RemoteException;

    void timeShiftPlay(Uri uri) throws RemoteException;

    void timeShiftResume() throws RemoteException;

    void timeShiftSeekTo(long j) throws RemoteException;

    void timeShiftSetPlaybackParams(PlaybackParams playbackParams) throws RemoteException;

    void tune(Uri uri, Bundle bundle) throws RemoteException;

    void unblockContent(String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements ITvInputSession {
        @Override // android.media.tv.ITvInputSession
        public void release() throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void setMain(boolean isMain) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void setSurface(Surface surface) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void dispatchSurfaceChanged(int format, int width, int height) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void setVolume(float volume) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void tune(Uri channelUri, Bundle params) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void setCaptionEnabled(boolean enabled) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void selectTrack(int type, String trackId) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void appPrivateCommand(String action, Bundle data) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void createOverlayView(IBinder windowToken, Rect frame) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void relayoutOverlayView(Rect frame) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void removeOverlayView() throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void unblockContent(String unblockedRating) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void timeShiftPlay(Uri recordedProgramUri) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void timeShiftPause() throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void timeShiftResume() throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void timeShiftSeekTo(long timeMs) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void timeShiftSetPlaybackParams(PlaybackParams params) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void timeShiftEnablePositionTracking(boolean enable) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void startRecording(Uri programUri) throws RemoteException {
        }

        @Override // android.media.tv.ITvInputSession
        public void stopRecording() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ITvInputSession {
        private static final String DESCRIPTOR = "android.media.tv.ITvInputSession";
        static final int TRANSACTION_appPrivateCommand = 9;
        static final int TRANSACTION_createOverlayView = 10;
        static final int TRANSACTION_dispatchSurfaceChanged = 4;
        static final int TRANSACTION_relayoutOverlayView = 11;
        static final int TRANSACTION_release = 1;
        static final int TRANSACTION_removeOverlayView = 12;
        static final int TRANSACTION_selectTrack = 8;
        static final int TRANSACTION_setCaptionEnabled = 7;
        static final int TRANSACTION_setMain = 2;
        static final int TRANSACTION_setSurface = 3;
        static final int TRANSACTION_setVolume = 5;
        static final int TRANSACTION_startRecording = 20;
        static final int TRANSACTION_stopRecording = 21;
        static final int TRANSACTION_timeShiftEnablePositionTracking = 19;
        static final int TRANSACTION_timeShiftPause = 15;
        static final int TRANSACTION_timeShiftPlay = 14;
        static final int TRANSACTION_timeShiftResume = 16;
        static final int TRANSACTION_timeShiftSeekTo = 17;
        static final int TRANSACTION_timeShiftSetPlaybackParams = 18;
        static final int TRANSACTION_tune = 6;
        static final int TRANSACTION_unblockContent = 13;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITvInputSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITvInputSession)) {
                return (ITvInputSession) iin;
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
                    return "release";
                case 2:
                    return "setMain";
                case 3:
                    return "setSurface";
                case 4:
                    return "dispatchSurfaceChanged";
                case 5:
                    return "setVolume";
                case 6:
                    return "tune";
                case 7:
                    return "setCaptionEnabled";
                case 8:
                    return "selectTrack";
                case 9:
                    return "appPrivateCommand";
                case 10:
                    return "createOverlayView";
                case 11:
                    return "relayoutOverlayView";
                case 12:
                    return "removeOverlayView";
                case 13:
                    return "unblockContent";
                case 14:
                    return "timeShiftPlay";
                case 15:
                    return "timeShiftPause";
                case 16:
                    return "timeShiftResume";
                case 17:
                    return "timeShiftSeekTo";
                case 18:
                    return "timeShiftSetPlaybackParams";
                case 19:
                    return "timeShiftEnablePositionTracking";
                case 20:
                    return "startRecording";
                case 21:
                    return "stopRecording";
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
            boolean _arg0;
            Surface _arg02;
            Uri _arg03;
            Bundle _arg1;
            Bundle _arg12;
            Rect _arg13;
            Rect _arg04;
            Uri _arg05;
            PlaybackParams _arg06;
            Uri _arg07;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    release();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    setMain(_arg0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Surface.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    setSurface(_arg02);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    int _arg14 = data.readInt();
                    int _arg2 = data.readInt();
                    dispatchSurfaceChanged(_arg08, _arg14, _arg2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    setVolume(data.readFloat());
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    tune(_arg03, _arg1);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    setCaptionEnabled(_arg0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    String _arg15 = data.readString();
                    selectTrack(_arg09, _arg15);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    appPrivateCommand(_arg010, _arg12);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg011 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg13 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    createOverlayView(_arg011, _arg13);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    relayoutOverlayView(_arg04);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    removeOverlayView();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    unblockContent(data.readString());
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    timeShiftPlay(_arg05);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    timeShiftPause();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    timeShiftResume();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    timeShiftSeekTo(data.readLong());
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = PlaybackParams.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    timeShiftSetPlaybackParams(_arg06);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    timeShiftEnablePositionTracking(_arg0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    startRecording(_arg07);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    stopRecording();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements ITvInputSession {
            public static ITvInputSession sDefaultImpl;
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

            @Override // android.media.tv.ITvInputSession
            public void release() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().release();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void setMain(boolean isMain) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isMain ? 1 : 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMain(isMain);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void setSurface(Surface surface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (surface != null) {
                        _data.writeInt(1);
                        surface.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSurface(surface);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void dispatchSurfaceChanged(int format, int width, int height) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchSurfaceChanged(format, width, height);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void setVolume(float volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(volume);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVolume(volume);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void tune(Uri channelUri, Bundle params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (channelUri != null) {
                        _data.writeInt(1);
                        channelUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().tune(channelUri, params);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void setCaptionEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCaptionEnabled(enabled);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void selectTrack(int type, String trackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(trackId);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().selectTrack(type, trackId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void appPrivateCommand(String action, Bundle data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().appPrivateCommand(action, data);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void createOverlayView(IBinder windowToken, Rect frame) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    if (frame != null) {
                        _data.writeInt(1);
                        frame.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().createOverlayView(windowToken, frame);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void relayoutOverlayView(Rect frame) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (frame != null) {
                        _data.writeInt(1);
                        frame.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().relayoutOverlayView(frame);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void removeOverlayView() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeOverlayView();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void unblockContent(String unblockedRating) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(unblockedRating);
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unblockContent(unblockedRating);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void timeShiftPlay(Uri recordedProgramUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recordedProgramUri != null) {
                        _data.writeInt(1);
                        recordedProgramUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().timeShiftPlay(recordedProgramUri);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void timeShiftPause() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().timeShiftPause();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void timeShiftResume() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().timeShiftResume();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void timeShiftSeekTo(long timeMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timeMs);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().timeShiftSeekTo(timeMs);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void timeShiftSetPlaybackParams(PlaybackParams params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().timeShiftSetPlaybackParams(params);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void timeShiftEnablePositionTracking(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().timeShiftEnablePositionTracking(enable);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void startRecording(Uri programUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (programUri != null) {
                        _data.writeInt(1);
                        programUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startRecording(programUri);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public void stopRecording() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopRecording();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ITvInputSession impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ITvInputSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
