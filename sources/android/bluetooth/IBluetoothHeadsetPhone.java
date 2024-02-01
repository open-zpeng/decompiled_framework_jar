package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IBluetoothHeadsetPhone extends IInterface {
    boolean answerCall() throws RemoteException;

    void cdmaSetSecondCallState(boolean z) throws RemoteException;

    void cdmaSwapSecondCallState() throws RemoteException;

    String getNetworkOperator() throws RemoteException;

    String getSubscriberNumber() throws RemoteException;

    boolean hangupCall() throws RemoteException;

    boolean listCurrentCalls() throws RemoteException;

    boolean processChld(int i) throws RemoteException;

    boolean queryPhoneState() throws RemoteException;

    boolean sendDtmf(int i) throws RemoteException;

    void updateBtHandsfreeAfterRadioTechnologyChange() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBluetoothHeadsetPhone {
        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public boolean answerCall() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public boolean hangupCall() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public boolean sendDtmf(int dtmf) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public boolean processChld(int chld) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public String getNetworkOperator() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public String getSubscriberNumber() throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public boolean listCurrentCalls() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public boolean queryPhoneState() throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public void updateBtHandsfreeAfterRadioTechnologyChange() throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public void cdmaSwapSecondCallState() throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothHeadsetPhone
        public void cdmaSetSecondCallState(boolean state) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetoothHeadsetPhone {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothHeadsetPhone";
        static final int TRANSACTION_answerCall = 1;
        static final int TRANSACTION_cdmaSetSecondCallState = 11;
        static final int TRANSACTION_cdmaSwapSecondCallState = 10;
        static final int TRANSACTION_getNetworkOperator = 5;
        static final int TRANSACTION_getSubscriberNumber = 6;
        static final int TRANSACTION_hangupCall = 2;
        static final int TRANSACTION_listCurrentCalls = 7;
        static final int TRANSACTION_processChld = 4;
        static final int TRANSACTION_queryPhoneState = 8;
        static final int TRANSACTION_sendDtmf = 3;
        static final int TRANSACTION_updateBtHandsfreeAfterRadioTechnologyChange = 9;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothHeadsetPhone asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetoothHeadsetPhone)) {
                return (IBluetoothHeadsetPhone) iin;
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
                    return "answerCall";
                case 2:
                    return "hangupCall";
                case 3:
                    return "sendDtmf";
                case 4:
                    return "processChld";
                case 5:
                    return "getNetworkOperator";
                case 6:
                    return "getSubscriberNumber";
                case 7:
                    return "listCurrentCalls";
                case 8:
                    return "queryPhoneState";
                case 9:
                    return "updateBtHandsfreeAfterRadioTechnologyChange";
                case 10:
                    return "cdmaSwapSecondCallState";
                case 11:
                    return "cdmaSetSecondCallState";
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
                    boolean answerCall = answerCall();
                    reply.writeNoException();
                    reply.writeInt(answerCall ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hangupCall = hangupCall();
                    reply.writeNoException();
                    reply.writeInt(hangupCall ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    boolean sendDtmf = sendDtmf(_arg0);
                    reply.writeNoException();
                    reply.writeInt(sendDtmf ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    boolean processChld = processChld(_arg02);
                    reply.writeNoException();
                    reply.writeInt(processChld ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getNetworkOperator();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = getSubscriberNumber();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    boolean listCurrentCalls = listCurrentCalls();
                    reply.writeNoException();
                    reply.writeInt(listCurrentCalls ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    boolean queryPhoneState = queryPhoneState();
                    reply.writeNoException();
                    reply.writeInt(queryPhoneState ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    updateBtHandsfreeAfterRadioTechnologyChange();
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    cdmaSwapSecondCallState();
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg03 = data.readInt() != 0;
                    cdmaSetSecondCallState(_arg03);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBluetoothHeadsetPhone {
            public static IBluetoothHeadsetPhone sDefaultImpl;
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

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public boolean answerCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().answerCall();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public boolean hangupCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hangupCall();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public boolean sendDtmf(int dtmf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(dtmf);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().sendDtmf(dtmf);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public boolean processChld(int chld) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(chld);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().processChld(chld);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public String getNetworkOperator() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNetworkOperator();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public String getSubscriberNumber() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSubscriberNumber();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public boolean listCurrentCalls() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().listCurrentCalls();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public boolean queryPhoneState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().queryPhoneState();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public void updateBtHandsfreeAfterRadioTechnologyChange() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateBtHandsfreeAfterRadioTechnologyChange();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public void cdmaSwapSecondCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cdmaSwapSecondCallState();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public void cdmaSetSecondCallState(boolean state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state ? 1 : 0);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cdmaSetSecondCallState(state);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBluetoothHeadsetPhone impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBluetoothHeadsetPhone getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
