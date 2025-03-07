package android.os;

import android.os.IIncidentReportStatusListener;
import android.os.IncidentManager;
import java.io.FileDescriptor;
import java.util.List;

/* loaded from: classes2.dex */
public interface IIncidentManager extends IInterface {
    void deleteAllIncidentReports(String str) throws RemoteException;

    void deleteIncidentReports(String str, String str2, String str3) throws RemoteException;

    IncidentManager.IncidentReport getIncidentReport(String str, String str2, String str3) throws RemoteException;

    List<String> getIncidentReportList(String str, String str2) throws RemoteException;

    void reportIncident(IncidentReportArgs incidentReportArgs) throws RemoteException;

    void reportIncidentToStream(IncidentReportArgs incidentReportArgs, IIncidentReportStatusListener iIncidentReportStatusListener, FileDescriptor fileDescriptor) throws RemoteException;

    void systemRunning() throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IIncidentManager {
        @Override // android.os.IIncidentManager
        public void reportIncident(IncidentReportArgs args) throws RemoteException {
        }

        @Override // android.os.IIncidentManager
        public void reportIncidentToStream(IncidentReportArgs args, IIncidentReportStatusListener listener, FileDescriptor stream) throws RemoteException {
        }

        @Override // android.os.IIncidentManager
        public void systemRunning() throws RemoteException {
        }

        @Override // android.os.IIncidentManager
        public List<String> getIncidentReportList(String pkg, String cls) throws RemoteException {
            return null;
        }

        @Override // android.os.IIncidentManager
        public IncidentManager.IncidentReport getIncidentReport(String pkg, String cls, String id) throws RemoteException {
            return null;
        }

        @Override // android.os.IIncidentManager
        public void deleteIncidentReports(String pkg, String cls, String id) throws RemoteException {
        }

        @Override // android.os.IIncidentManager
        public void deleteAllIncidentReports(String pkg) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IIncidentManager {
        private static final String DESCRIPTOR = "android.os.IIncidentManager";
        static final int TRANSACTION_deleteAllIncidentReports = 7;
        static final int TRANSACTION_deleteIncidentReports = 6;
        static final int TRANSACTION_getIncidentReport = 5;
        static final int TRANSACTION_getIncidentReportList = 4;
        static final int TRANSACTION_reportIncident = 1;
        static final int TRANSACTION_reportIncidentToStream = 2;
        static final int TRANSACTION_systemRunning = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IIncidentManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IIncidentManager)) {
                return (IIncidentManager) iin;
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
                    return "reportIncident";
                case 2:
                    return "reportIncidentToStream";
                case 3:
                    return "systemRunning";
                case 4:
                    return "getIncidentReportList";
                case 5:
                    return "getIncidentReport";
                case 6:
                    return "deleteIncidentReports";
                case 7:
                    return "deleteAllIncidentReports";
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
            IncidentReportArgs _arg0;
            IncidentReportArgs _arg02;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = IncidentReportArgs.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    reportIncident(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = IncidentReportArgs.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    IIncidentReportStatusListener _arg1 = IIncidentReportStatusListener.Stub.asInterface(data.readStrongBinder());
                    FileDescriptor _arg2 = data.readRawFileDescriptor();
                    reportIncidentToStream(_arg02, _arg1, _arg2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    systemRunning();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    String _arg12 = data.readString();
                    List<String> _result = getIncidentReportList(_arg03, _arg12);
                    reply.writeNoException();
                    reply.writeStringList(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    String _arg13 = data.readString();
                    String _arg22 = data.readString();
                    IncidentManager.IncidentReport _result2 = getIncidentReport(_arg04, _arg13, _arg22);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _arg14 = data.readString();
                    String _arg23 = data.readString();
                    deleteIncidentReports(_arg05, _arg14, _arg23);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    deleteAllIncidentReports(_arg06);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IIncidentManager {
            public static IIncidentManager sDefaultImpl;
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

            @Override // android.os.IIncidentManager
            public void reportIncident(IncidentReportArgs args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportIncident(args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IIncidentManager
            public void reportIncidentToStream(IncidentReportArgs args, IIncidentReportStatusListener listener, FileDescriptor stream) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeRawFileDescriptor(stream);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportIncidentToStream(args, listener, stream);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IIncidentManager
            public void systemRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().systemRunning();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IIncidentManager
            public List<String> getIncidentReportList(String pkg, String cls) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(cls);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIncidentReportList(pkg, cls);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IIncidentManager
            public IncidentManager.IncidentReport getIncidentReport(String pkg, String cls, String id) throws RemoteException {
                IncidentManager.IncidentReport _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(cls);
                    _data.writeString(id);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIncidentReport(pkg, cls, id);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = IncidentManager.IncidentReport.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IIncidentManager
            public void deleteIncidentReports(String pkg, String cls, String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(cls);
                    _data.writeString(id);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().deleteIncidentReports(pkg, cls, id);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IIncidentManager
            public void deleteAllIncidentReports(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().deleteAllIncidentReports(pkg);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IIncidentManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IIncidentManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
