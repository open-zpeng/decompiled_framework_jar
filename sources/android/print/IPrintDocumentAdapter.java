package android.print;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.print.ILayoutResultCallback;
import android.print.IPrintDocumentAdapterObserver;
import android.print.IWriteResultCallback;
/* loaded from: classes2.dex */
public interface IPrintDocumentAdapter extends IInterface {
    synchronized void finish() throws RemoteException;

    synchronized void kill(String str) throws RemoteException;

    synchronized void layout(PrintAttributes printAttributes, PrintAttributes printAttributes2, ILayoutResultCallback iLayoutResultCallback, Bundle bundle, int i) throws RemoteException;

    synchronized void setObserver(IPrintDocumentAdapterObserver iPrintDocumentAdapterObserver) throws RemoteException;

    synchronized void start() throws RemoteException;

    synchronized void write(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, IWriteResultCallback iWriteResultCallback, int i) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IPrintDocumentAdapter {
        private static final String DESCRIPTOR = "android.print.IPrintDocumentAdapter";
        static final int TRANSACTION_finish = 5;
        static final int TRANSACTION_kill = 6;
        static final int TRANSACTION_layout = 3;
        static final int TRANSACTION_setObserver = 1;
        static final int TRANSACTION_start = 2;
        static final int TRANSACTION_write = 4;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IPrintDocumentAdapter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IPrintDocumentAdapter)) {
                return (IPrintDocumentAdapter) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PrintAttributes _arg0;
            PrintAttributes _arg1;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IPrintDocumentAdapterObserver _arg02 = IPrintDocumentAdapterObserver.Stub.asInterface(data.readStrongBinder());
                    setObserver(_arg02);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    start();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        PrintAttributes _arg03 = PrintAttributes.CREATOR.createFromParcel(data);
                        _arg0 = _arg03;
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        PrintAttributes _arg12 = PrintAttributes.CREATOR.createFromParcel(data);
                        _arg1 = _arg12;
                    } else {
                        _arg1 = null;
                    }
                    ILayoutResultCallback _arg2 = ILayoutResultCallback.Stub.asInterface(data.readStrongBinder());
                    Bundle _arg3 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    int _arg4 = data.readInt();
                    layout(_arg0, _arg1, _arg2, _arg3, _arg4);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    PageRange[] _arg04 = (PageRange[]) data.createTypedArray(PageRange.CREATOR);
                    ParcelFileDescriptor _arg13 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    IWriteResultCallback _arg22 = IWriteResultCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg32 = data.readInt();
                    write(_arg04, _arg13, _arg22, _arg32);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    finish();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    kill(_arg05);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IPrintDocumentAdapter {
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

            @Override // android.print.IPrintDocumentAdapter
            public synchronized void setObserver(IPrintDocumentAdapterObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.print.IPrintDocumentAdapter
            public synchronized void start() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.print.IPrintDocumentAdapter
            public synchronized void layout(PrintAttributes oldAttributes, PrintAttributes newAttributes, ILayoutResultCallback callback, Bundle metadata, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (oldAttributes != null) {
                        _data.writeInt(1);
                        oldAttributes.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (newAttributes != null) {
                        _data.writeInt(1);
                        newAttributes.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (metadata != null) {
                        _data.writeInt(1);
                        metadata.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sequence);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.print.IPrintDocumentAdapter
            public synchronized void write(PageRange[] pages, ParcelFileDescriptor fd, IWriteResultCallback callback, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(pages, 0);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(sequence);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.print.IPrintDocumentAdapter
            public synchronized void finish() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.print.IPrintDocumentAdapter
            public synchronized void kill(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
