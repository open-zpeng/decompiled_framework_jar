package android.service.contentcapture;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.contentcapture.ContentCaptureContext;
import android.view.contentcapture.DataRemovalRequest;
import com.android.internal.os.IResultReceiver;

/* loaded from: classes2.dex */
public interface IContentCaptureService extends IInterface {
    void onActivityEvent(ActivityEvent activityEvent) throws RemoteException;

    void onActivitySnapshot(int i, SnapshotData snapshotData) throws RemoteException;

    void onConnected(IBinder iBinder, boolean z, boolean z2) throws RemoteException;

    void onDataRemovalRequest(DataRemovalRequest dataRemovalRequest) throws RemoteException;

    void onDisconnected() throws RemoteException;

    void onSessionFinished(int i) throws RemoteException;

    void onSessionStarted(ContentCaptureContext contentCaptureContext, int i, int i2, IResultReceiver iResultReceiver, int i3) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IContentCaptureService {
        @Override // android.service.contentcapture.IContentCaptureService
        public void onConnected(IBinder callback, boolean verbose, boolean debug) throws RemoteException {
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onDisconnected() throws RemoteException {
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onSessionStarted(ContentCaptureContext context, int sessionId, int uid, IResultReceiver clientReceiver, int initialState) throws RemoteException {
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onSessionFinished(int sessionId) throws RemoteException {
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onActivitySnapshot(int sessionId, SnapshotData snapshotData) throws RemoteException {
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onDataRemovalRequest(DataRemovalRequest request) throws RemoteException {
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onActivityEvent(ActivityEvent event) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IContentCaptureService {
        private static final String DESCRIPTOR = "android.service.contentcapture.IContentCaptureService";
        static final int TRANSACTION_onActivityEvent = 7;
        static final int TRANSACTION_onActivitySnapshot = 5;
        static final int TRANSACTION_onConnected = 1;
        static final int TRANSACTION_onDataRemovalRequest = 6;
        static final int TRANSACTION_onDisconnected = 2;
        static final int TRANSACTION_onSessionFinished = 4;
        static final int TRANSACTION_onSessionStarted = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContentCaptureService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IContentCaptureService)) {
                return (IContentCaptureService) iin;
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
                    return "onConnected";
                case 2:
                    return "onDisconnected";
                case 3:
                    return "onSessionStarted";
                case 4:
                    return "onSessionFinished";
                case 5:
                    return "onActivitySnapshot";
                case 6:
                    return "onDataRemovalRequest";
                case 7:
                    return "onActivityEvent";
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
            ContentCaptureContext _arg0;
            SnapshotData _arg1;
            DataRemovalRequest _arg02;
            ActivityEvent _arg03;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg04 = data.readStrongBinder();
                    boolean _arg12 = data.readInt() != 0;
                    boolean _arg2 = data.readInt() != 0;
                    onConnected(_arg04, _arg12, _arg2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onDisconnected();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = ContentCaptureContext.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int _arg13 = data.readInt();
                    int _arg22 = data.readInt();
                    IResultReceiver _arg3 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    int _arg4 = data.readInt();
                    onSessionStarted(_arg0, _arg13, _arg22, _arg3, _arg4);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    onSessionFinished(_arg05);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = SnapshotData.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    onActivitySnapshot(_arg06, _arg1);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = DataRemovalRequest.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    onDataRemovalRequest(_arg02);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = ActivityEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    onActivityEvent(_arg03);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IContentCaptureService {
            public static IContentCaptureService sDefaultImpl;
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

            @Override // android.service.contentcapture.IContentCaptureService
            public void onConnected(IBinder callback, boolean verbose, boolean debug) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback);
                    _data.writeInt(verbose ? 1 : 0);
                    _data.writeInt(debug ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onConnected(callback, verbose, debug);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.contentcapture.IContentCaptureService
            public void onDisconnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onDisconnected();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.contentcapture.IContentCaptureService
            public void onSessionStarted(ContentCaptureContext context, int sessionId, int uid, IResultReceiver clientReceiver, int initialState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (context != null) {
                        _data.writeInt(1);
                        context.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sessionId);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(clientReceiver != null ? clientReceiver.asBinder() : null);
                    _data.writeInt(initialState);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSessionStarted(context, sessionId, uid, clientReceiver, initialState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.contentcapture.IContentCaptureService
            public void onSessionFinished(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSessionFinished(sessionId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.contentcapture.IContentCaptureService
            public void onActivitySnapshot(int sessionId, SnapshotData snapshotData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    if (snapshotData != null) {
                        _data.writeInt(1);
                        snapshotData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivitySnapshot(sessionId, snapshotData);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.contentcapture.IContentCaptureService
            public void onDataRemovalRequest(DataRemovalRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onDataRemovalRequest(request);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.contentcapture.IContentCaptureService
            public void onActivityEvent(ActivityEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onActivityEvent(event);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IContentCaptureService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IContentCaptureService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
