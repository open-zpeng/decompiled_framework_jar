package android.media.session;

import android.content.pm.ParceledListSlice;
import android.media.MediaMetadata;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
/* loaded from: classes.dex */
public interface ISessionControllerCallback extends IInterface {
    synchronized void onEvent(String str, Bundle bundle) throws RemoteException;

    synchronized void onExtrasChanged(Bundle bundle) throws RemoteException;

    synchronized void onMetadataChanged(MediaMetadata mediaMetadata) throws RemoteException;

    synchronized void onPlaybackStateChanged(PlaybackState playbackState) throws RemoteException;

    synchronized void onQueueChanged(ParceledListSlice parceledListSlice) throws RemoteException;

    synchronized void onQueueTitleChanged(CharSequence charSequence) throws RemoteException;

    synchronized void onSessionDestroyed() throws RemoteException;

    synchronized void onVolumeInfoChanged(ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISessionControllerCallback {
        private static final String DESCRIPTOR = "android.media.session.ISessionControllerCallback";
        static final int TRANSACTION_onEvent = 1;
        static final int TRANSACTION_onExtrasChanged = 7;
        static final int TRANSACTION_onMetadataChanged = 4;
        static final int TRANSACTION_onPlaybackStateChanged = 3;
        static final int TRANSACTION_onQueueChanged = 5;
        static final int TRANSACTION_onQueueTitleChanged = 6;
        static final int TRANSACTION_onSessionDestroyed = 2;
        static final int TRANSACTION_onVolumeInfoChanged = 8;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ISessionControllerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISessionControllerCallback)) {
                return (ISessionControllerCallback) iin;
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
                    String _arg0 = data.readString();
                    Bundle _arg1 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onEvent(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onSessionDestroyed();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    PlaybackState _arg02 = data.readInt() != 0 ? PlaybackState.CREATOR.createFromParcel(data) : null;
                    onPlaybackStateChanged(_arg02);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    MediaMetadata _arg03 = data.readInt() != 0 ? MediaMetadata.CREATOR.createFromParcel(data) : null;
                    onMetadataChanged(_arg03);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    ParceledListSlice _arg04 = data.readInt() != 0 ? ParceledListSlice.CREATOR.createFromParcel(data) : null;
                    onQueueChanged(_arg04);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    CharSequence _arg05 = data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null;
                    onQueueTitleChanged(_arg05);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg06 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    onExtrasChanged(_arg06);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelableVolumeInfo _arg07 = data.readInt() != 0 ? ParcelableVolumeInfo.CREATOR.createFromParcel(data) : null;
                    onVolumeInfoChanged(_arg07);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ISessionControllerCallback {
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

            @Override // android.media.session.ISessionControllerCallback
            public synchronized void onEvent(String event, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(event);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionControllerCallback
            public synchronized void onSessionDestroyed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionControllerCallback
            public synchronized void onPlaybackStateChanged(PlaybackState state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionControllerCallback
            public synchronized void onMetadataChanged(MediaMetadata metadata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (metadata != null) {
                        _data.writeInt(1);
                        metadata.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionControllerCallback
            public synchronized void onQueueChanged(ParceledListSlice queue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (queue != null) {
                        _data.writeInt(1);
                        queue.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionControllerCallback
            public synchronized void onQueueTitleChanged(CharSequence title) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (title != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(title, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.session.ISessionControllerCallback
            public synchronized void onExtrasChanged(Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            @Override // android.media.session.ISessionControllerCallback
            public synchronized void onVolumeInfoChanged(ParcelableVolumeInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
