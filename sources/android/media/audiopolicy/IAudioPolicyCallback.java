package android.media.audiopolicy;

import android.media.AudioFocusInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IAudioPolicyCallback extends IInterface {
    synchronized void notifyAudioFocusAbandon(AudioFocusInfo audioFocusInfo) throws RemoteException;

    synchronized void notifyAudioFocusGrant(AudioFocusInfo audioFocusInfo, int i) throws RemoteException;

    synchronized void notifyAudioFocusLoss(AudioFocusInfo audioFocusInfo, boolean z) throws RemoteException;

    synchronized void notifyAudioFocusRequest(AudioFocusInfo audioFocusInfo, int i) throws RemoteException;

    synchronized void notifyMixStateUpdate(String str, int i) throws RemoteException;

    synchronized void notifyVolumeAdjust(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAudioPolicyCallback {
        private static final String DESCRIPTOR = "android.media.audiopolicy.IAudioPolicyCallback";
        static final int TRANSACTION_notifyAudioFocusAbandon = 4;
        static final int TRANSACTION_notifyAudioFocusGrant = 1;
        static final int TRANSACTION_notifyAudioFocusLoss = 2;
        static final int TRANSACTION_notifyAudioFocusRequest = 3;
        static final int TRANSACTION_notifyMixStateUpdate = 5;
        static final int TRANSACTION_notifyVolumeAdjust = 6;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IAudioPolicyCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAudioPolicyCallback)) {
                return (IAudioPolicyCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            AudioFocusInfo _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0 ? AudioFocusInfo.CREATOR.createFromParcel(data) : null;
                    int _arg1 = data.readInt();
                    notifyAudioFocusGrant(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0 ? AudioFocusInfo.CREATOR.createFromParcel(data) : null;
                    boolean _arg12 = data.readInt() != 0;
                    notifyAudioFocusLoss(_arg0, _arg12);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0 ? AudioFocusInfo.CREATOR.createFromParcel(data) : null;
                    int _arg13 = data.readInt();
                    notifyAudioFocusRequest(_arg0, _arg13);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0 ? AudioFocusInfo.CREATOR.createFromParcel(data) : null;
                    notifyAudioFocusAbandon(_arg0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    int _arg14 = data.readInt();
                    notifyMixStateUpdate(_arg02, _arg14);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    notifyVolumeAdjust(data.readInt());
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IAudioPolicyCallback {
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

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public synchronized void notifyAudioFocusGrant(AudioFocusInfo afi, int requestResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (afi != null) {
                        _data.writeInt(1);
                        afi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(requestResult);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public synchronized void notifyAudioFocusLoss(AudioFocusInfo afi, boolean wasNotified) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (afi != null) {
                        _data.writeInt(1);
                        afi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(wasNotified ? 1 : 0);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public synchronized void notifyAudioFocusRequest(AudioFocusInfo afi, int requestResult) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (afi != null) {
                        _data.writeInt(1);
                        afi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(requestResult);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public synchronized void notifyAudioFocusAbandon(AudioFocusInfo afi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (afi != null) {
                        _data.writeInt(1);
                        afi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public synchronized void notifyMixStateUpdate(String regId, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(regId);
                    _data.writeInt(state);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.audiopolicy.IAudioPolicyCallback
            public synchronized void notifyVolumeAdjust(int adjustment) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(adjustment);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
