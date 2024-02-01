package android.view.contentcapture;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.os.IResultReceiver;

/* loaded from: classes3.dex */
public interface IContentCaptureManager extends IInterface {
    void finishSession(int i) throws RemoteException;

    void getContentCaptureConditions(String str, IResultReceiver iResultReceiver) throws RemoteException;

    void getServiceComponentName(IResultReceiver iResultReceiver) throws RemoteException;

    void getServiceSettingsActivity(IResultReceiver iResultReceiver) throws RemoteException;

    void isContentCaptureFeatureEnabled(IResultReceiver iResultReceiver) throws RemoteException;

    void removeData(DataRemovalRequest dataRemovalRequest) throws RemoteException;

    void startSession(IBinder iBinder, ComponentName componentName, int i, int i2, IResultReceiver iResultReceiver) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IContentCaptureManager {
        @Override // android.view.contentcapture.IContentCaptureManager
        public void startSession(IBinder activityToken, ComponentName componentName, int sessionId, int flags, IResultReceiver result) throws RemoteException {
        }

        @Override // android.view.contentcapture.IContentCaptureManager
        public void finishSession(int sessionId) throws RemoteException {
        }

        @Override // android.view.contentcapture.IContentCaptureManager
        public void getServiceComponentName(IResultReceiver result) throws RemoteException {
        }

        @Override // android.view.contentcapture.IContentCaptureManager
        public void removeData(DataRemovalRequest request) throws RemoteException {
        }

        @Override // android.view.contentcapture.IContentCaptureManager
        public void isContentCaptureFeatureEnabled(IResultReceiver result) throws RemoteException {
        }

        @Override // android.view.contentcapture.IContentCaptureManager
        public void getServiceSettingsActivity(IResultReceiver result) throws RemoteException {
        }

        @Override // android.view.contentcapture.IContentCaptureManager
        public void getContentCaptureConditions(String packageName, IResultReceiver result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IContentCaptureManager {
        private static final String DESCRIPTOR = "android.view.contentcapture.IContentCaptureManager";
        static final int TRANSACTION_finishSession = 2;
        static final int TRANSACTION_getContentCaptureConditions = 7;
        static final int TRANSACTION_getServiceComponentName = 3;
        static final int TRANSACTION_getServiceSettingsActivity = 6;
        static final int TRANSACTION_isContentCaptureFeatureEnabled = 5;
        static final int TRANSACTION_removeData = 4;
        static final int TRANSACTION_startSession = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContentCaptureManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IContentCaptureManager)) {
                return (IContentCaptureManager) iin;
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
                    return "startSession";
                case 2:
                    return "finishSession";
                case 3:
                    return "getServiceComponentName";
                case 4:
                    return "removeData";
                case 5:
                    return "isContentCaptureFeatureEnabled";
                case 6:
                    return "getServiceSettingsActivity";
                case 7:
                    return "getContentCaptureConditions";
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
            ComponentName _arg1;
            DataRemovalRequest _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    IResultReceiver _arg4 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    startSession(_arg02, _arg1, _arg2, _arg3, _arg4);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    finishSession(_arg03);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IResultReceiver _arg04 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    getServiceComponentName(_arg04);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = DataRemovalRequest.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    removeData(_arg0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IResultReceiver _arg05 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    isContentCaptureFeatureEnabled(_arg05);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IResultReceiver _arg06 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    getServiceSettingsActivity(_arg06);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    IResultReceiver _arg12 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    getContentCaptureConditions(_arg07, _arg12);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IContentCaptureManager {
            public static IContentCaptureManager sDefaultImpl;
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

            @Override // android.view.contentcapture.IContentCaptureManager
            public void startSession(IBinder activityToken, ComponentName componentName, int sessionId, int flags, IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sessionId);
                    _data.writeInt(flags);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startSession(activityToken, componentName, sessionId, flags, result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.contentcapture.IContentCaptureManager
            public void finishSession(int sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finishSession(sessionId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.contentcapture.IContentCaptureManager
            public void getServiceComponentName(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getServiceComponentName(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.contentcapture.IContentCaptureManager
            public void removeData(DataRemovalRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeData(request);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.contentcapture.IContentCaptureManager
            public void isContentCaptureFeatureEnabled(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().isContentCaptureFeatureEnabled(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.contentcapture.IContentCaptureManager
            public void getServiceSettingsActivity(IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getServiceSettingsActivity(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.contentcapture.IContentCaptureManager
            public void getContentCaptureConditions(String packageName, IResultReceiver result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(result != null ? result.asBinder() : null);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getContentCaptureConditions(packageName, result);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IContentCaptureManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IContentCaptureManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
