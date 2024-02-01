package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IGnssStatusListener extends IInterface {
    synchronized void onFirstFix(int i) throws RemoteException;

    synchronized void onGnssStarted() throws RemoteException;

    synchronized void onGnssStopped() throws RemoteException;

    synchronized void onNmeaReceived(long j, String str) throws RemoteException;

    synchronized void onSvStatusChanged(int i, int[] iArr, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IGnssStatusListener {
        private static final String DESCRIPTOR = "android.location.IGnssStatusListener";
        static final int TRANSACTION_onFirstFix = 3;
        static final int TRANSACTION_onGnssStarted = 1;
        static final int TRANSACTION_onGnssStopped = 2;
        static final int TRANSACTION_onNmeaReceived = 5;
        static final int TRANSACTION_onSvStatusChanged = 4;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IGnssStatusListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IGnssStatusListener)) {
                return (IGnssStatusListener) iin;
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
                    onGnssStarted();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onGnssStopped();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    onFirstFix(_arg0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    int[] _arg1 = data.createIntArray();
                    float[] _arg2 = data.createFloatArray();
                    float[] _arg3 = data.createFloatArray();
                    float[] _arg4 = data.createFloatArray();
                    float[] _arg5 = data.createFloatArray();
                    onSvStatusChanged(_arg02, _arg1, _arg2, _arg3, _arg4, _arg5);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg03 = data.readLong();
                    String _arg12 = data.readString();
                    onNmeaReceived(_arg03, _arg12);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IGnssStatusListener {
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

            @Override // android.location.IGnssStatusListener
            public synchronized void onGnssStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.location.IGnssStatusListener
            public synchronized void onGnssStopped() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.location.IGnssStatusListener
            public synchronized void onFirstFix(int ttff) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ttff);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.location.IGnssStatusListener
            public synchronized void onSvStatusChanged(int svCount, int[] svidWithFlags, float[] cn0s, float[] elevations, float[] azimuths, float[] carrierFreqs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(svCount);
                    _data.writeIntArray(svidWithFlags);
                    _data.writeFloatArray(cn0s);
                    _data.writeFloatArray(elevations);
                    _data.writeFloatArray(azimuths);
                    _data.writeFloatArray(carrierFreqs);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.location.IGnssStatusListener
            public synchronized void onNmeaReceived(long timestamp, String nmea) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(timestamp);
                    _data.writeString(nmea);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
