package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IHdmiRecordListener extends IInterface {
    byte[] getOneTouchRecordSource(int i) throws RemoteException;

    void onClearTimerRecordingResult(int i, int i2) throws RemoteException;

    void onOneTouchRecordResult(int i, int i2) throws RemoteException;

    void onTimerRecordingResult(int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IHdmiRecordListener {
        @Override // android.hardware.hdmi.IHdmiRecordListener
        public byte[] getOneTouchRecordSource(int recorderAddress) throws RemoteException {
            return null;
        }

        @Override // android.hardware.hdmi.IHdmiRecordListener
        public void onOneTouchRecordResult(int recorderAddress, int result) throws RemoteException {
        }

        @Override // android.hardware.hdmi.IHdmiRecordListener
        public void onTimerRecordingResult(int recorderAddress, int result) throws RemoteException {
        }

        @Override // android.hardware.hdmi.IHdmiRecordListener
        public void onClearTimerRecordingResult(int recorderAddress, int result) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IHdmiRecordListener {
        private static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiRecordListener";
        static final int TRANSACTION_getOneTouchRecordSource = 1;
        static final int TRANSACTION_onClearTimerRecordingResult = 4;
        static final int TRANSACTION_onOneTouchRecordResult = 2;
        static final int TRANSACTION_onTimerRecordingResult = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IHdmiRecordListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IHdmiRecordListener)) {
                return (IHdmiRecordListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode != 2) {
                    if (transactionCode != 3) {
                        if (transactionCode == 4) {
                            return "onClearTimerRecordingResult";
                        }
                        return null;
                    }
                    return "onTimerRecordingResult";
                }
                return "onOneTouchRecordResult";
            }
            return "getOneTouchRecordSource";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                int _arg0 = data.readInt();
                byte[] _result = getOneTouchRecordSource(_arg0);
                reply.writeNoException();
                reply.writeByteArray(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                int _arg02 = data.readInt();
                int _arg1 = data.readInt();
                onOneTouchRecordResult(_arg02, _arg1);
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                int _arg03 = data.readInt();
                int _arg12 = data.readInt();
                onTimerRecordingResult(_arg03, _arg12);
                reply.writeNoException();
                return true;
            } else if (code != 4) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg04 = data.readInt();
                int _arg13 = data.readInt();
                onClearTimerRecordingResult(_arg04, _arg13);
                reply.writeNoException();
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IHdmiRecordListener {
            public static IHdmiRecordListener sDefaultImpl;
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

            @Override // android.hardware.hdmi.IHdmiRecordListener
            public byte[] getOneTouchRecordSource(int recorderAddress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(recorderAddress);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOneTouchRecordSource(recorderAddress);
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.hdmi.IHdmiRecordListener
            public void onOneTouchRecordResult(int recorderAddress, int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(recorderAddress);
                    _data.writeInt(result);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onOneTouchRecordResult(recorderAddress, result);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.hdmi.IHdmiRecordListener
            public void onTimerRecordingResult(int recorderAddress, int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(recorderAddress);
                    _data.writeInt(result);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onTimerRecordingResult(recorderAddress, result);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.hdmi.IHdmiRecordListener
            public void onClearTimerRecordingResult(int recorderAddress, int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(recorderAddress);
                    _data.writeInt(result);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onClearTimerRecordingResult(recorderAddress, result);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IHdmiRecordListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IHdmiRecordListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
