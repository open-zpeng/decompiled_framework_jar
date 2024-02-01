package android.telephony.mbms;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes2.dex */
public interface IMbmsDownloadSessionCallback extends IInterface {
    synchronized void onError(int i, String str) throws RemoteException;

    synchronized void onFileServicesUpdated(List<FileServiceInfo> list) throws RemoteException;

    synchronized void onMiddlewareReady() throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IMbmsDownloadSessionCallback {
        private static final String DESCRIPTOR = "android.telephony.mbms.IMbmsDownloadSessionCallback";
        static final int TRANSACTION_onError = 1;
        static final int TRANSACTION_onFileServicesUpdated = 2;
        static final int TRANSACTION_onMiddlewareReady = 3;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IMbmsDownloadSessionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMbmsDownloadSessionCallback)) {
                return (IMbmsDownloadSessionCallback) iin;
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
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    onError(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    List<FileServiceInfo> _arg02 = data.createTypedArrayList(FileServiceInfo.CREATOR);
                    onFileServicesUpdated(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onMiddlewareReady();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IMbmsDownloadSessionCallback {
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

            @Override // android.telephony.mbms.IMbmsDownloadSessionCallback
            public synchronized void onError(int errorCode, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    _data.writeString(message);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.IMbmsDownloadSessionCallback
            public synchronized void onFileServicesUpdated(List<FileServiceInfo> services) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(services);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.IMbmsDownloadSessionCallback
            public synchronized void onMiddlewareReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
