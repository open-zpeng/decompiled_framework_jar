package android.hardware.location;

import android.annotation.UnsupportedAppUsage;
import android.hardware.location.IActivityRecognitionHardware;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IActivityRecognitionHardwareClient extends IInterface {
    @UnsupportedAppUsage
    void onAvailabilityChanged(boolean z, IActivityRecognitionHardware iActivityRecognitionHardware) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IActivityRecognitionHardwareClient {
        @Override // android.hardware.location.IActivityRecognitionHardwareClient
        public void onAvailabilityChanged(boolean isSupported, IActivityRecognitionHardware instance) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IActivityRecognitionHardwareClient {
        private static final String DESCRIPTOR = "android.hardware.location.IActivityRecognitionHardwareClient";
        static final int TRANSACTION_onAvailabilityChanged = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IActivityRecognitionHardwareClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IActivityRecognitionHardwareClient)) {
                return (IActivityRecognitionHardwareClient) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onAvailabilityChanged";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            boolean _arg0 = data.readInt() != 0;
            IActivityRecognitionHardware _arg1 = IActivityRecognitionHardware.Stub.asInterface(data.readStrongBinder());
            onAvailabilityChanged(_arg0, _arg1);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IActivityRecognitionHardwareClient {
            public static IActivityRecognitionHardwareClient sDefaultImpl;
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

            @Override // android.hardware.location.IActivityRecognitionHardwareClient
            public void onAvailabilityChanged(boolean isSupported, IActivityRecognitionHardware instance) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isSupported ? 1 : 0);
                    _data.writeStrongBinder(instance != null ? instance.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAvailabilityChanged(isSupported, instance);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IActivityRecognitionHardwareClient impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IActivityRecognitionHardwareClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
