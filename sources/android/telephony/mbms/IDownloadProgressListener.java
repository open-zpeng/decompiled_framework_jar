package android.telephony.mbms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IDownloadProgressListener extends IInterface {
    synchronized void onProgressUpdated(DownloadRequest downloadRequest, FileInfo fileInfo, int i, int i2, int i3, int i4) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IDownloadProgressListener {
        private static final String DESCRIPTOR = "android.telephony.mbms.IDownloadProgressListener";
        static final int TRANSACTION_onProgressUpdated = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IDownloadProgressListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDownloadProgressListener)) {
                return (IDownloadProgressListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            DownloadRequest _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                DownloadRequest _arg02 = DownloadRequest.CREATOR.createFromParcel(data);
                _arg0 = _arg02;
            } else {
                _arg0 = null;
            }
            FileInfo _arg1 = data.readInt() != 0 ? FileInfo.CREATOR.createFromParcel(data) : null;
            int _arg2 = data.readInt();
            int _arg3 = data.readInt();
            int _arg4 = data.readInt();
            int _arg5 = data.readInt();
            onProgressUpdated(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            return true;
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IDownloadProgressListener {
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

            @Override // android.telephony.mbms.IDownloadProgressListener
            public synchronized void onProgressUpdated(DownloadRequest request, FileInfo fileInfo, int currentDownloadSize, int fullDownloadSize, int currentDecodedSize, int fullDecodedSize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fileInfo != null) {
                        _data.writeInt(1);
                        fileInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(currentDownloadSize);
                    _data.writeInt(fullDownloadSize);
                    _data.writeInt(currentDecodedSize);
                    _data.writeInt(fullDecodedSize);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
