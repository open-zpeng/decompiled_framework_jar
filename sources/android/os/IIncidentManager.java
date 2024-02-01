package android.os;

import android.os.IIncidentReportStatusListener;
import java.io.FileDescriptor;
/* loaded from: classes2.dex */
public interface IIncidentManager extends IInterface {
    synchronized void reportIncident(IncidentReportArgs incidentReportArgs) throws RemoteException;

    synchronized void reportIncidentToStream(IncidentReportArgs incidentReportArgs, IIncidentReportStatusListener iIncidentReportStatusListener, FileDescriptor fileDescriptor) throws RemoteException;

    synchronized void systemRunning() throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IIncidentManager {
        private static final String DESCRIPTOR = "android.os.IIncidentManager";
        static final int TRANSACTION_reportIncident = 1;
        static final int TRANSACTION_reportIncidentToStream = 2;
        static final int TRANSACTION_systemRunning = 3;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IIncidentManager asInterface(IBinder obj) {
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

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IncidentReportArgs _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0 ? IncidentReportArgs.CREATOR.createFromParcel(data) : null;
                    reportIncident(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0 ? IncidentReportArgs.CREATOR.createFromParcel(data) : null;
                    IIncidentReportStatusListener _arg1 = IIncidentReportStatusListener.Stub.asInterface(data.readStrongBinder());
                    FileDescriptor _arg2 = data.readRawFileDescriptor();
                    reportIncidentToStream(_arg0, _arg1, _arg2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    systemRunning();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IIncidentManager {
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

            @Override // android.os.IIncidentManager
            public synchronized void reportIncident(IncidentReportArgs args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IIncidentManager
            public synchronized void reportIncidentToStream(IncidentReportArgs args, IIncidentReportStatusListener listener, FileDescriptor stream) throws RemoteException {
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
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.IIncidentManager
            public synchronized void systemRunning() throws RemoteException {
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
