package android.printservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.print.PrintJobInfo;
import android.print.PrinterId;
import android.printservice.IPrintServiceClient;
import java.util.List;
/* loaded from: classes2.dex */
public interface IPrintService extends IInterface {
    synchronized void createPrinterDiscoverySession() throws RemoteException;

    synchronized void destroyPrinterDiscoverySession() throws RemoteException;

    synchronized void onPrintJobQueued(PrintJobInfo printJobInfo) throws RemoteException;

    synchronized void requestCancelPrintJob(PrintJobInfo printJobInfo) throws RemoteException;

    synchronized void requestCustomPrinterIcon(PrinterId printerId) throws RemoteException;

    synchronized void setClient(IPrintServiceClient iPrintServiceClient) throws RemoteException;

    synchronized void startPrinterDiscovery(List<PrinterId> list) throws RemoteException;

    synchronized void startPrinterStateTracking(PrinterId printerId) throws RemoteException;

    synchronized void stopPrinterDiscovery() throws RemoteException;

    synchronized void stopPrinterStateTracking(PrinterId printerId) throws RemoteException;

    synchronized void validatePrinters(List<PrinterId> list) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IPrintService {
        private static final String DESCRIPTOR = "android.printservice.IPrintService";
        static final int TRANSACTION_createPrinterDiscoverySession = 4;
        static final int TRANSACTION_destroyPrinterDiscoverySession = 11;
        static final int TRANSACTION_onPrintJobQueued = 3;
        static final int TRANSACTION_requestCancelPrintJob = 2;
        static final int TRANSACTION_requestCustomPrinterIcon = 9;
        static final int TRANSACTION_setClient = 1;
        static final int TRANSACTION_startPrinterDiscovery = 5;
        static final int TRANSACTION_startPrinterStateTracking = 8;
        static final int TRANSACTION_stopPrinterDiscovery = 6;
        static final int TRANSACTION_stopPrinterStateTracking = 10;
        static final int TRANSACTION_validatePrinters = 7;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IPrintService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IPrintService)) {
                return (IPrintService) iin;
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
                    IPrintServiceClient _arg0 = IPrintServiceClient.Stub.asInterface(data.readStrongBinder());
                    setClient(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    PrintJobInfo _arg02 = data.readInt() != 0 ? PrintJobInfo.CREATOR.createFromParcel(data) : null;
                    requestCancelPrintJob(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    PrintJobInfo _arg03 = data.readInt() != 0 ? PrintJobInfo.CREATOR.createFromParcel(data) : null;
                    onPrintJobQueued(_arg03);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    createPrinterDiscoverySession();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    List<PrinterId> _arg04 = data.createTypedArrayList(PrinterId.CREATOR);
                    startPrinterDiscovery(_arg04);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    stopPrinterDiscovery();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    List<PrinterId> _arg05 = data.createTypedArrayList(PrinterId.CREATOR);
                    validatePrinters(_arg05);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    PrinterId _arg06 = data.readInt() != 0 ? PrinterId.CREATOR.createFromParcel(data) : null;
                    startPrinterStateTracking(_arg06);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    PrinterId _arg07 = data.readInt() != 0 ? PrinterId.CREATOR.createFromParcel(data) : null;
                    requestCustomPrinterIcon(_arg07);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    PrinterId _arg08 = data.readInt() != 0 ? PrinterId.CREATOR.createFromParcel(data) : null;
                    stopPrinterStateTracking(_arg08);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    destroyPrinterDiscoverySession();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IPrintService {
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

            @Override // android.printservice.IPrintService
            public synchronized void setClient(IPrintServiceClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.printservice.IPrintService
            public synchronized void requestCancelPrintJob(PrintJobInfo printJobInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJobInfo != null) {
                        _data.writeInt(1);
                        printJobInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.printservice.IPrintService
            public synchronized void onPrintJobQueued(PrintJobInfo printJobInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJobInfo != null) {
                        _data.writeInt(1);
                        printJobInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.printservice.IPrintService
            public synchronized void createPrinterDiscoverySession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.printservice.IPrintService
            public synchronized void startPrinterDiscovery(List<PrinterId> priorityList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(priorityList);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.printservice.IPrintService
            public synchronized void stopPrinterDiscovery() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.printservice.IPrintService
            public synchronized void validatePrinters(List<PrinterId> printerIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(printerIds);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.printservice.IPrintService
            public synchronized void startPrinterStateTracking(PrinterId printerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printerId != null) {
                        _data.writeInt(1);
                        printerId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.printservice.IPrintService
            public synchronized void requestCustomPrinterIcon(PrinterId printerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printerId != null) {
                        _data.writeInt(1);
                        printerId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.printservice.IPrintService
            public synchronized void stopPrinterStateTracking(PrinterId printerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printerId != null) {
                        _data.writeInt(1);
                        printerId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.printservice.IPrintService
            public synchronized void destroyPrinterDiscoverySession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
