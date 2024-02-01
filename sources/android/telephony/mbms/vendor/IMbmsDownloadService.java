package android.telephony.mbms.vendor;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.mbms.DownloadRequest;
import android.telephony.mbms.FileInfo;
import android.telephony.mbms.IDownloadProgressListener;
import android.telephony.mbms.IDownloadStatusListener;
import android.telephony.mbms.IMbmsDownloadSessionCallback;
import java.util.List;
/* loaded from: classes2.dex */
public interface IMbmsDownloadService extends IInterface {
    synchronized int addProgressListener(DownloadRequest downloadRequest, IDownloadProgressListener iDownloadProgressListener) throws RemoteException;

    synchronized int addStatusListener(DownloadRequest downloadRequest, IDownloadStatusListener iDownloadStatusListener) throws RemoteException;

    synchronized int cancelDownload(DownloadRequest downloadRequest) throws RemoteException;

    synchronized void dispose(int i) throws RemoteException;

    synchronized int download(DownloadRequest downloadRequest) throws RemoteException;

    synchronized int initialize(int i, IMbmsDownloadSessionCallback iMbmsDownloadSessionCallback) throws RemoteException;

    synchronized List<DownloadRequest> listPendingDownloads(int i) throws RemoteException;

    synchronized int removeProgressListener(DownloadRequest downloadRequest, IDownloadProgressListener iDownloadProgressListener) throws RemoteException;

    synchronized int removeStatusListener(DownloadRequest downloadRequest, IDownloadStatusListener iDownloadStatusListener) throws RemoteException;

    synchronized int requestDownloadState(DownloadRequest downloadRequest, FileInfo fileInfo) throws RemoteException;

    synchronized int requestUpdateFileServices(int i, List<String> list) throws RemoteException;

    synchronized int resetDownloadKnowledge(DownloadRequest downloadRequest) throws RemoteException;

    synchronized int setTempFileRootDirectory(int i, String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IMbmsDownloadService {
        private static final String DESCRIPTOR = "android.telephony.mbms.vendor.IMbmsDownloadService";
        static final int TRANSACTION_addProgressListener = 7;
        static final int TRANSACTION_addStatusListener = 5;
        static final int TRANSACTION_cancelDownload = 10;
        static final int TRANSACTION_dispose = 13;
        static final int TRANSACTION_download = 4;
        static final int TRANSACTION_initialize = 1;
        static final int TRANSACTION_listPendingDownloads = 9;
        static final int TRANSACTION_removeProgressListener = 8;
        static final int TRANSACTION_removeStatusListener = 6;
        static final int TRANSACTION_requestDownloadState = 11;
        static final int TRANSACTION_requestUpdateFileServices = 2;
        static final int TRANSACTION_resetDownloadKnowledge = 12;
        static final int TRANSACTION_setTempFileRootDirectory = 3;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IMbmsDownloadService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMbmsDownloadService)) {
                return (IMbmsDownloadService) iin;
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
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    IMbmsDownloadSessionCallback _arg1 = IMbmsDownloadSessionCallback.Stub.asInterface(data.readStrongBinder());
                    int _result = initialize(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    List<String> _arg12 = data.createStringArrayList();
                    int _result2 = requestUpdateFileServices(_arg03, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    String _arg13 = data.readString();
                    int _result3 = setTempFileRootDirectory(_arg04, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    DownloadRequest _arg05 = data.readInt() != 0 ? DownloadRequest.CREATOR.createFromParcel(data) : null;
                    int _result4 = download(_arg05);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    DownloadRequest _arg06 = data.readInt() != 0 ? DownloadRequest.CREATOR.createFromParcel(data) : null;
                    IDownloadStatusListener _arg14 = IDownloadStatusListener.Stub.asInterface(data.readStrongBinder());
                    int _result5 = addStatusListener(_arg06, _arg14);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    DownloadRequest _arg07 = data.readInt() != 0 ? DownloadRequest.CREATOR.createFromParcel(data) : null;
                    IDownloadStatusListener _arg15 = IDownloadStatusListener.Stub.asInterface(data.readStrongBinder());
                    int _result6 = removeStatusListener(_arg07, _arg15);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    DownloadRequest _arg08 = data.readInt() != 0 ? DownloadRequest.CREATOR.createFromParcel(data) : null;
                    IDownloadProgressListener _arg16 = IDownloadProgressListener.Stub.asInterface(data.readStrongBinder());
                    int _result7 = addProgressListener(_arg08, _arg16);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    DownloadRequest _arg09 = data.readInt() != 0 ? DownloadRequest.CREATOR.createFromParcel(data) : null;
                    IDownloadProgressListener _arg17 = IDownloadProgressListener.Stub.asInterface(data.readStrongBinder());
                    int _result8 = removeProgressListener(_arg09, _arg17);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    List<DownloadRequest> _result9 = listPendingDownloads(_arg010);
                    reply.writeNoException();
                    reply.writeTypedList(_result9);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    DownloadRequest _arg011 = data.readInt() != 0 ? DownloadRequest.CREATOR.createFromParcel(data) : null;
                    int _result10 = cancelDownload(_arg011);
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = DownloadRequest.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    FileInfo _arg18 = data.readInt() != 0 ? FileInfo.CREATOR.createFromParcel(data) : null;
                    int _result11 = requestDownloadState(_arg0, _arg18);
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    DownloadRequest _arg012 = data.readInt() != 0 ? DownloadRequest.CREATOR.createFromParcel(data) : null;
                    int _result12 = resetDownloadKnowledge(_arg012);
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    dispose(_arg013);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IMbmsDownloadService {
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

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int initialize(int subId, IMbmsDownloadSessionCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int requestUpdateFileServices(int subId, List<String> serviceClasses) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeStringList(serviceClasses);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int setTempFileRootDirectory(int subId, String rootDirectoryPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(rootDirectoryPath);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int download(DownloadRequest downloadRequest) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int addStatusListener(DownloadRequest downloadRequest, IDownloadStatusListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int removeStatusListener(DownloadRequest downloadRequest, IDownloadStatusListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int addProgressListener(DownloadRequest downloadRequest, IDownloadProgressListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int removeProgressListener(DownloadRequest downloadRequest, IDownloadProgressListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized List<DownloadRequest> listPendingDownloads(int subscriptionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subscriptionId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    List<DownloadRequest> _result = _reply.createTypedArrayList(DownloadRequest.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int cancelDownload(DownloadRequest downloadRequest) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int requestDownloadState(DownloadRequest downloadRequest, FileInfo fileInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fileInfo != null) {
                        _data.writeInt(1);
                        fileInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized int resetDownloadKnowledge(DownloadRequest downloadRequest) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (downloadRequest != null) {
                        _data.writeInt(1);
                        downloadRequest.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.mbms.vendor.IMbmsDownloadService
            public synchronized void dispose(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
