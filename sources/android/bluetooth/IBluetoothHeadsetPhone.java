package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IBluetoothHeadsetPhone extends IInterface {
    synchronized boolean answerCall() throws RemoteException;

    synchronized void cdmaSetSecondCallState(boolean z) throws RemoteException;

    synchronized void cdmaSwapSecondCallState() throws RemoteException;

    synchronized String getNetworkOperator() throws RemoteException;

    synchronized String getSubscriberNumber() throws RemoteException;

    synchronized boolean hangupCall() throws RemoteException;

    synchronized boolean listCurrentCalls() throws RemoteException;

    synchronized boolean processChld(int i) throws RemoteException;

    synchronized boolean queryPhoneState() throws RemoteException;

    synchronized boolean sendDtmf(int i) throws RemoteException;

    synchronized void updateBtHandsfreeAfterRadioTechnologyChange() throws RemoteException;

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

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IBluetoothHeadsetPhone asInterface(IBinder obj) {
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

        /* loaded from: classes.dex */
        private static class Proxy implements IBluetoothHeadsetPhone {
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

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized boolean answerCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized boolean hangupCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized boolean sendDtmf(int dtmf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(dtmf);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized boolean processChld(int chld) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(chld);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized String getNetworkOperator() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized String getSubscriberNumber() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized boolean listCurrentCalls() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized boolean queryPhoneState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized void updateBtHandsfreeAfterRadioTechnologyChange() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized void cdmaSwapSecondCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothHeadsetPhone
            public synchronized void cdmaSetSecondCallState(boolean state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state ? 1 : 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
