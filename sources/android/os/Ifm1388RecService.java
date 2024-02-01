package android.os;

/* loaded from: classes2.dex */
public interface Ifm1388RecService extends IInterface {
    int get_frame_size() throws RemoteException;

    int get_mode(byte[] bArr) throws RemoteException;

    int get_mode_by_index(int i, byte[] bArr) throws RemoteException;

    int get_mode_number() throws RemoteException;

    int get_sample_rate() throws RemoteException;

    int get_sdcard_path(byte[] bArr) throws RemoteException;

    int set_mode(int i) throws RemoteException;

    int start_record(int i, byte[] bArr, byte[] bArr2) throws RemoteException;

    int stop_record() throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements Ifm1388RecService {
        @Override // android.os.Ifm1388RecService
        public int start_record(int channel_num, byte[] channel_idx, byte[] filepath) throws RemoteException {
            return 0;
        }

        @Override // android.os.Ifm1388RecService
        public int stop_record() throws RemoteException {
            return 0;
        }

        @Override // android.os.Ifm1388RecService
        public int get_mode(byte[] dsp_mode_string) throws RemoteException {
            return 0;
        }

        @Override // android.os.Ifm1388RecService
        public int get_mode_number() throws RemoteException {
            return 0;
        }

        @Override // android.os.Ifm1388RecService
        public int get_mode_by_index(int index, byte[] dsp_mode_string) throws RemoteException {
            return 0;
        }

        @Override // android.os.Ifm1388RecService
        public int set_mode(int mode_index) throws RemoteException {
            return 0;
        }

        @Override // android.os.Ifm1388RecService
        public int get_sample_rate() throws RemoteException {
            return 0;
        }

        @Override // android.os.Ifm1388RecService
        public int get_frame_size() throws RemoteException {
            return 0;
        }

        @Override // android.os.Ifm1388RecService
        public int get_sdcard_path(byte[] sdcard_path) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements Ifm1388RecService {
        private static final String DESCRIPTOR = "android.os.Ifm1388RecService";
        static final int TRANSACTION_get_frame_size = 8;
        static final int TRANSACTION_get_mode = 3;
        static final int TRANSACTION_get_mode_by_index = 5;
        static final int TRANSACTION_get_mode_number = 4;
        static final int TRANSACTION_get_sample_rate = 7;
        static final int TRANSACTION_get_sdcard_path = 9;
        static final int TRANSACTION_set_mode = 6;
        static final int TRANSACTION_start_record = 1;
        static final int TRANSACTION_stop_record = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static Ifm1388RecService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof Ifm1388RecService)) {
                return (Ifm1388RecService) iin;
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
                    return "start_record";
                case 2:
                    return "stop_record";
                case 3:
                    return "get_mode";
                case 4:
                    return "get_mode_number";
                case 5:
                    return "get_mode_by_index";
                case 6:
                    return "set_mode";
                case 7:
                    return "get_sample_rate";
                case 8:
                    return "get_frame_size";
                case 9:
                    return "get_sdcard_path";
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
            byte[] _arg0;
            byte[] _arg1;
            byte[] _arg02;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    byte[] _arg12 = data.createByteArray();
                    byte[] _arg2 = data.createByteArray();
                    int _result = start_record(_arg03, _arg12, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = stop_record();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0_length = data.readInt();
                    if (_arg0_length < 0) {
                        _arg0 = null;
                    } else {
                        _arg0 = new byte[_arg0_length];
                    }
                    int _result3 = get_mode(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    reply.writeByteArray(_arg0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _result4 = get_mode_number();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new byte[_arg1_length];
                    }
                    int _result5 = get_mode_by_index(_arg04, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    reply.writeByteArray(_arg1);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    int _result6 = set_mode(_arg05);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _result7 = get_sample_rate();
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _result8 = get_frame_size();
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0_length2 = data.readInt();
                    if (_arg0_length2 < 0) {
                        _arg02 = null;
                    } else {
                        _arg02 = new byte[_arg0_length2];
                    }
                    int _result9 = get_sdcard_path(_arg02);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    reply.writeByteArray(_arg02);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements Ifm1388RecService {
            public static Ifm1388RecService sDefaultImpl;
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

            @Override // android.os.Ifm1388RecService
            public int start_record(int channel_num, byte[] channel_idx, byte[] filepath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(channel_num);
                    _data.writeByteArray(channel_idx);
                    _data.writeByteArray(filepath);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().start_record(channel_num, channel_idx, filepath);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388RecService
            public int stop_record() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stop_record();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388RecService
            public int get_mode(byte[] dsp_mode_string) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (dsp_mode_string == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(dsp_mode_string.length);
                    }
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().get_mode(dsp_mode_string);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(dsp_mode_string);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388RecService
            public int get_mode_number() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().get_mode_number();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388RecService
            public int get_mode_by_index(int index, byte[] dsp_mode_string) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(index);
                    if (dsp_mode_string == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(dsp_mode_string.length);
                    }
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().get_mode_by_index(index, dsp_mode_string);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(dsp_mode_string);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388RecService
            public int set_mode(int mode_index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode_index);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().set_mode(mode_index);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388RecService
            public int get_sample_rate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().get_sample_rate();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388RecService
            public int get_frame_size() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().get_frame_size();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.Ifm1388RecService
            public int get_sdcard_path(byte[] sdcard_path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (sdcard_path == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(sdcard_path.length);
                    }
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().get_sdcard_path(sdcard_path);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(sdcard_path);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(Ifm1388RecService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static Ifm1388RecService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
