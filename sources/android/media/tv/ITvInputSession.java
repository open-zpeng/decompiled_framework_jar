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
    synchronized void appPrivateCommand(String str, Bundle bundle) throws RemoteException;

    synchronized void createOverlayView(IBinder iBinder, Rect rect) throws RemoteException;

    synchronized void dispatchSurfaceChanged(int i, int i2, int i3) throws RemoteException;

    synchronized void relayoutOverlayView(Rect rect) throws RemoteException;

    synchronized void release() throws RemoteException;

    synchronized void removeOverlayView() throws RemoteException;

    synchronized void selectTrack(int i, String str) throws RemoteException;

    synchronized void setCaptionEnabled(boolean z) throws RemoteException;

    synchronized void setMain(boolean z) throws RemoteException;

    synchronized void setSurface(Surface surface) throws RemoteException;

    synchronized void setVolume(float f) throws RemoteException;

    synchronized void startRecording(Uri uri) throws RemoteException;

    synchronized void stopRecording() throws RemoteException;

    synchronized void timeShiftEnablePositionTracking(boolean z) throws RemoteException;

    synchronized void timeShiftPause() throws RemoteException;

    synchronized void timeShiftPlay(Uri uri) throws RemoteException;

    synchronized void timeShiftResume() throws RemoteException;

    synchronized void timeShiftSeekTo(long j) throws RemoteException;

    synchronized void timeShiftSetPlaybackParams(PlaybackParams playbackParams) throws RemoteException;

    synchronized void tune(Uri uri, Bundle bundle) throws RemoteException;

    synchronized void unblockContent(String str) throws RemoteException;

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

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ITvInputSession asInterface(IBinder obj) {
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

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            Uri _arg02;
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
                    setSurface(data.readInt() != 0 ? Surface.CREATOR.createFromParcel(data) : null);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    dispatchSurfaceChanged(_arg03, _arg1, _arg2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    setVolume(data.readFloat());
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    Bundle _arg12 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    tune(_arg02, _arg12);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    setCaptionEnabled(_arg0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    String _arg13 = data.readString();
                    selectTrack(_arg04, _arg13);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    Bundle _arg14 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    appPrivateCommand(_arg05, _arg14);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg06 = data.readStrongBinder();
                    Rect _arg15 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
                    createOverlayView(_arg06, _arg15);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    Rect _arg07 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
                    relayoutOverlayView(_arg07);
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
                    Uri _arg08 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    timeShiftPlay(_arg08);
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
                    PlaybackParams _arg09 = data.readInt() != 0 ? PlaybackParams.CREATOR.createFromParcel(data) : null;
                    timeShiftSetPlaybackParams(_arg09);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    timeShiftEnablePositionTracking(_arg0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    Uri _arg010 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    startRecording(_arg010);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    stopRecording();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ITvInputSession {
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

            @Override // android.media.tv.ITvInputSession
            public synchronized void release() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void setMain(boolean isMain) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isMain ? 1 : 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void setSurface(Surface surface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (surface != null) {
                        _data.writeInt(1);
                        surface.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void dispatchSurfaceChanged(int format, int width, int height) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(format);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void setVolume(float volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(volume);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void tune(Uri channelUri, Bundle params) throws RemoteException {
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
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void setCaptionEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void selectTrack(int type, String trackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(trackId);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void appPrivateCommand(String action, Bundle data) throws RemoteException {
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
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void createOverlayView(IBinder windowToken, Rect frame) throws RemoteException {
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
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void relayoutOverlayView(Rect frame) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (frame != null) {
                        _data.writeInt(1);
                        frame.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void removeOverlayView() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void unblockContent(String unblockedRating) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(unblockedRating);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void timeShiftPlay(Uri recordedProgramUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recordedProgramUri != null) {
                        _data.writeInt(1);
                        recordedProgramUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void timeShiftPause() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void timeShiftResume() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void timeShiftSeekTo(long timeMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timeMs);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void timeShiftSetPlaybackParams(PlaybackParams params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void timeShiftEnablePositionTracking(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void startRecording(Uri programUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (programUri != null) {
                        _data.writeInt(1);
                        programUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSession
            public synchronized void stopRecording() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
