package android.media.tv;

import android.media.tv.ITvInputSession;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes2.dex */
public interface ITvInputSessionCallback extends IInterface {
    synchronized void onChannelRetuned(Uri uri) throws RemoteException;

    synchronized void onContentAllowed() throws RemoteException;

    synchronized void onContentBlocked(String str) throws RemoteException;

    synchronized void onError(int i) throws RemoteException;

    synchronized void onLayoutSurface(int i, int i2, int i3, int i4) throws RemoteException;

    synchronized void onRecordingStopped(Uri uri) throws RemoteException;

    synchronized void onSessionCreated(ITvInputSession iTvInputSession, IBinder iBinder) throws RemoteException;

    synchronized void onSessionEvent(String str, Bundle bundle) throws RemoteException;

    synchronized void onTimeShiftCurrentPositionChanged(long j) throws RemoteException;

    synchronized void onTimeShiftStartPositionChanged(long j) throws RemoteException;

    synchronized void onTimeShiftStatusChanged(int i) throws RemoteException;

    synchronized void onTrackSelected(int i, String str) throws RemoteException;

    synchronized void onTracksChanged(List<TvTrackInfo> list) throws RemoteException;

    synchronized void onTuned(Uri uri) throws RemoteException;

    synchronized void onVideoAvailable() throws RemoteException;

    synchronized void onVideoUnavailable(int i) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ITvInputSessionCallback {
        private static final String DESCRIPTOR = "android.media.tv.ITvInputSessionCallback";
        static final int TRANSACTION_onChannelRetuned = 3;
        static final int TRANSACTION_onContentAllowed = 8;
        static final int TRANSACTION_onContentBlocked = 9;
        static final int TRANSACTION_onError = 16;
        static final int TRANSACTION_onLayoutSurface = 10;
        static final int TRANSACTION_onRecordingStopped = 15;
        static final int TRANSACTION_onSessionCreated = 1;
        static final int TRANSACTION_onSessionEvent = 2;
        static final int TRANSACTION_onTimeShiftCurrentPositionChanged = 13;
        static final int TRANSACTION_onTimeShiftStartPositionChanged = 12;
        static final int TRANSACTION_onTimeShiftStatusChanged = 11;
        static final int TRANSACTION_onTrackSelected = 5;
        static final int TRANSACTION_onTracksChanged = 4;
        static final int TRANSACTION_onTuned = 14;
        static final int TRANSACTION_onVideoAvailable = 6;
        static final int TRANSACTION_onVideoUnavailable = 7;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ITvInputSessionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITvInputSessionCallback)) {
                return (ITvInputSessionCallback) iin;
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
                    ITvInputSession _arg0 = ITvInputSession.Stub.asInterface(data.readStrongBinder());
                    IBinder _arg1 = data.readStrongBinder();
                    onSessionCreated(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    Bundle _arg12 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onSessionEvent(_arg02, _arg12);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    Uri _arg03 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    onChannelRetuned(_arg03);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    List<TvTrackInfo> _arg04 = data.createTypedArrayList(TvTrackInfo.CREATOR);
                    onTracksChanged(_arg04);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    String _arg13 = data.readString();
                    onTrackSelected(_arg05, _arg13);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    onVideoAvailable();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    onVideoUnavailable(_arg06);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    onContentAllowed();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    onContentBlocked(_arg07);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    int _arg14 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    onLayoutSurface(_arg08, _arg14, _arg2, _arg3);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    onTimeShiftStatusChanged(_arg09);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg010 = data.readLong();
                    onTimeShiftStartPositionChanged(_arg010);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg011 = data.readLong();
                    onTimeShiftCurrentPositionChanged(_arg011);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    Uri _arg012 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    onTuned(_arg012);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    Uri _arg013 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    onRecordingStopped(_arg013);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    onError(_arg014);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ITvInputSessionCallback {
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

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onSessionCreated(ITvInputSession session, IBinder hardwareSessionToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeStrongBinder(hardwareSessionToken);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onSessionEvent(String name, Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onChannelRetuned(Uri channelUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (channelUri != null) {
                        _data.writeInt(1);
                        channelUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onTracksChanged(List<TvTrackInfo> tracks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(tracks);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onTrackSelected(int type, String trackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(trackId);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onVideoAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onVideoUnavailable(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onContentAllowed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onContentBlocked(String rating) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rating);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onLayoutSurface(int left, int top, int right, int bottom) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(left);
                    _data.writeInt(top);
                    _data.writeInt(right);
                    _data.writeInt(bottom);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onTimeShiftStatusChanged(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onTimeShiftStartPositionChanged(long timeMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timeMs);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onTimeShiftCurrentPositionChanged(long timeMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timeMs);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onTuned(Uri channelUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (channelUri != null) {
                        _data.writeInt(1);
                        channelUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onRecordingStopped(Uri recordedProgramUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recordedProgramUri != null) {
                        _data.writeInt(1);
                        recordedProgramUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.tv.ITvInputSessionCallback
            public synchronized void onError(int error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(error);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
