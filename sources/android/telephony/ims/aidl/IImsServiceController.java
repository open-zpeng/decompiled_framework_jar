package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.IImsServiceControllerListener;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import com.android.ims.internal.IImsFeatureStatusCallback;

/* loaded from: classes2.dex */
public interface IImsServiceController extends IInterface {
    IImsMmTelFeature createMmTelFeature(int i, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException;

    IImsRcsFeature createRcsFeature(int i, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException;

    void disableIms(int i) throws RemoteException;

    void enableIms(int i) throws RemoteException;

    IImsConfig getConfig(int i) throws RemoteException;

    IImsRegistration getRegistration(int i) throws RemoteException;

    void notifyImsServiceReadyForFeatureCreation() throws RemoteException;

    ImsFeatureConfiguration querySupportedImsFeatures() throws RemoteException;

    void removeImsFeature(int i, int i2, IImsFeatureStatusCallback iImsFeatureStatusCallback) throws RemoteException;

    void setListener(IImsServiceControllerListener iImsServiceControllerListener) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IImsServiceController {
        @Override // android.telephony.ims.aidl.IImsServiceController
        public void setListener(IImsServiceControllerListener l) throws RemoteException {
        }

        @Override // android.telephony.ims.aidl.IImsServiceController
        public IImsMmTelFeature createMmTelFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
            return null;
        }

        @Override // android.telephony.ims.aidl.IImsServiceController
        public IImsRcsFeature createRcsFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
            return null;
        }

        @Override // android.telephony.ims.aidl.IImsServiceController
        public ImsFeatureConfiguration querySupportedImsFeatures() throws RemoteException {
            return null;
        }

        @Override // android.telephony.ims.aidl.IImsServiceController
        public void notifyImsServiceReadyForFeatureCreation() throws RemoteException {
        }

        @Override // android.telephony.ims.aidl.IImsServiceController
        public void removeImsFeature(int slotId, int featureType, IImsFeatureStatusCallback c) throws RemoteException {
        }

        @Override // android.telephony.ims.aidl.IImsServiceController
        public IImsConfig getConfig(int slotId) throws RemoteException {
            return null;
        }

        @Override // android.telephony.ims.aidl.IImsServiceController
        public IImsRegistration getRegistration(int slotId) throws RemoteException {
            return null;
        }

        @Override // android.telephony.ims.aidl.IImsServiceController
        public void enableIms(int slotId) throws RemoteException {
        }

        @Override // android.telephony.ims.aidl.IImsServiceController
        public void disableIms(int slotId) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IImsServiceController {
        private static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsServiceController";
        static final int TRANSACTION_createMmTelFeature = 2;
        static final int TRANSACTION_createRcsFeature = 3;
        static final int TRANSACTION_disableIms = 10;
        static final int TRANSACTION_enableIms = 9;
        static final int TRANSACTION_getConfig = 7;
        static final int TRANSACTION_getRegistration = 8;
        static final int TRANSACTION_notifyImsServiceReadyForFeatureCreation = 5;
        static final int TRANSACTION_querySupportedImsFeatures = 4;
        static final int TRANSACTION_removeImsFeature = 6;
        static final int TRANSACTION_setListener = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsServiceController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsServiceController)) {
                return (IImsServiceController) iin;
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
                    return "setListener";
                case 2:
                    return "createMmTelFeature";
                case 3:
                    return "createRcsFeature";
                case 4:
                    return "querySupportedImsFeatures";
                case 5:
                    return "notifyImsServiceReadyForFeatureCreation";
                case 6:
                    return "removeImsFeature";
                case 7:
                    return "getConfig";
                case 8:
                    return "getRegistration";
                case 9:
                    return "enableIms";
                case 10:
                    return "disableIms";
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
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IImsServiceControllerListener _arg0 = IImsServiceControllerListener.Stub.asInterface(data.readStrongBinder());
                    setListener(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    IImsFeatureStatusCallback _arg1 = IImsFeatureStatusCallback.Stub.asInterface(data.readStrongBinder());
                    IImsMmTelFeature _result = createMmTelFeature(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    IImsFeatureStatusCallback _arg12 = IImsFeatureStatusCallback.Stub.asInterface(data.readStrongBinder());
                    IImsRcsFeature _result2 = createRcsFeature(_arg03, _arg12);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result2 != null ? _result2.asBinder() : null);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ImsFeatureConfiguration _result3 = querySupportedImsFeatures();
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    notifyImsServiceReadyForFeatureCreation();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    int _arg13 = data.readInt();
                    IImsFeatureStatusCallback _arg2 = IImsFeatureStatusCallback.Stub.asInterface(data.readStrongBinder());
                    removeImsFeature(_arg04, _arg13, _arg2);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    IImsConfig _result4 = getConfig(_arg05);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result4 != null ? _result4.asBinder() : null);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    IImsRegistration _result5 = getRegistration(_arg06);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result5 != null ? _result5.asBinder() : null);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    enableIms(_arg07);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    disableIms(_arg08);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IImsServiceController {
            public static IImsServiceController sDefaultImpl;
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

            @Override // android.telephony.ims.aidl.IImsServiceController
            public void setListener(IImsServiceControllerListener l) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(l != null ? l.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setListener(l);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsServiceController
            public IImsMmTelFeature createMmTelFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createMmTelFeature(slotId, c);
                    }
                    _reply.readException();
                    IImsMmTelFeature _result = IImsMmTelFeature.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsServiceController
            public IImsRcsFeature createRcsFeature(int slotId, IImsFeatureStatusCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createRcsFeature(slotId, c);
                    }
                    _reply.readException();
                    IImsRcsFeature _result = IImsRcsFeature.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsServiceController
            public ImsFeatureConfiguration querySupportedImsFeatures() throws RemoteException {
                ImsFeatureConfiguration _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().querySupportedImsFeatures();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ImsFeatureConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsServiceController
            public void notifyImsServiceReadyForFeatureCreation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyImsServiceReadyForFeatureCreation();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsServiceController
            public void removeImsFeature(int slotId, int featureType, IImsFeatureStatusCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    _data.writeInt(featureType);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeImsFeature(slotId, featureType, c);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsServiceController
            public IImsConfig getConfig(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConfig(slotId);
                    }
                    _reply.readException();
                    IImsConfig _result = IImsConfig.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsServiceController
            public IImsRegistration getRegistration(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRegistration(slotId);
                    }
                    _reply.readException();
                    IImsRegistration _result = IImsRegistration.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsServiceController
            public void enableIms(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableIms(slotId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IImsServiceController
            public void disableIms(int slotId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slotId);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disableIms(slotId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IImsServiceController impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IImsServiceController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
