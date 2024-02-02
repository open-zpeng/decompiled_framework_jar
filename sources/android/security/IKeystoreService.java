package android.security;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.security.keymaster.ExportResult;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterBlob;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keymaster.OperationResult;
/* loaded from: classes2.dex */
public interface IKeystoreService extends IInterface {
    synchronized int abort(IBinder iBinder) throws RemoteException;

    synchronized int addAuthToken(byte[] bArr) throws RemoteException;

    synchronized int addRngEntropy(byte[] bArr, int i) throws RemoteException;

    synchronized int attestDeviceIds(KeymasterArguments keymasterArguments, KeymasterCertificateChain keymasterCertificateChain) throws RemoteException;

    synchronized int attestKey(String str, KeymasterArguments keymasterArguments, KeymasterCertificateChain keymasterCertificateChain) throws RemoteException;

    synchronized OperationResult begin(IBinder iBinder, String str, int i, boolean z, KeymasterArguments keymasterArguments, byte[] bArr, int i2) throws RemoteException;

    synchronized int cancelConfirmationPrompt(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int clear_uid(long j) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int del(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int exist(String str, int i) throws RemoteException;

    synchronized ExportResult exportKey(String str, int i, KeymasterBlob keymasterBlob, KeymasterBlob keymasterBlob2, int i2) throws RemoteException;

    synchronized OperationResult finish(IBinder iBinder, KeymasterArguments keymasterArguments, byte[] bArr, byte[] bArr2) throws RemoteException;

    synchronized int generate(String str, int i, int i2, int i3, int i4, KeystoreArguments keystoreArguments) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int generateKey(String str, KeymasterArguments keymasterArguments, byte[] bArr, int i, int i2, KeyCharacteristics keyCharacteristics) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    byte[] get(String str, int i) throws RemoteException;

    synchronized int getKeyCharacteristics(String str, KeymasterBlob keymasterBlob, KeymasterBlob keymasterBlob2, int i, KeyCharacteristics keyCharacteristics) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getState(int i) throws RemoteException;

    private protected byte[] get_pubkey(String str) throws RemoteException;

    synchronized long getmtime(String str, int i) throws RemoteException;

    synchronized String grant(String str, int i) throws RemoteException;

    synchronized int importKey(String str, KeymasterArguments keymasterArguments, int i, byte[] bArr, int i2, int i3, KeyCharacteristics keyCharacteristics) throws RemoteException;

    synchronized int importWrappedKey(String str, byte[] bArr, String str2, byte[] bArr2, KeymasterArguments keymasterArguments, long j, long j2, KeyCharacteristics keyCharacteristics) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int import_key(String str, byte[] bArr, int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int insert(String str, byte[] bArr, int i, int i2) throws RemoteException;

    synchronized boolean isConfirmationPromptSupported() throws RemoteException;

    synchronized int isEmpty(int i) throws RemoteException;

    synchronized boolean isOperationAuthorized(IBinder iBinder) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int is_hardware_backed(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String[] list(String str, int i) throws RemoteException;

    synchronized int lock(int i) throws RemoteException;

    synchronized int onDeviceOffBody() throws RemoteException;

    synchronized int onKeyguardVisibilityChanged(boolean z, int i) throws RemoteException;

    synchronized int onUserAdded(int i, int i2) throws RemoteException;

    synchronized int onUserPasswordChanged(int i, String str) throws RemoteException;

    synchronized int onUserRemoved(int i) throws RemoteException;

    synchronized int presentConfirmationPrompt(IBinder iBinder, String str, byte[] bArr, String str2, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int reset() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    byte[] sign(String str, byte[] bArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int ungrant(String str, int i) throws RemoteException;

    synchronized int unlock(int i, String str) throws RemoteException;

    synchronized OperationResult update(IBinder iBinder, KeymasterArguments keymasterArguments, byte[] bArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int verify(String str, byte[] bArr, byte[] bArr2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IKeystoreService {
        private static final String DESCRIPTOR = "android.security.IKeystoreService";
        static final int TRANSACTION_abort = 30;
        static final int TRANSACTION_addAuthToken = 32;
        static final int TRANSACTION_addRngEntropy = 22;
        static final int TRANSACTION_attestDeviceIds = 36;
        static final int TRANSACTION_attestKey = 35;
        static final int TRANSACTION_begin = 27;
        static final int TRANSACTION_cancelConfirmationPrompt = 40;
        static final int TRANSACTION_clear_uid = 21;
        static final int TRANSACTION_del = 4;
        static final int TRANSACTION_exist = 5;
        static final int TRANSACTION_exportKey = 26;
        static final int TRANSACTION_finish = 29;
        static final int TRANSACTION_generate = 12;
        static final int TRANSACTION_generateKey = 23;
        static final int TRANSACTION_get = 2;
        static final int TRANSACTION_getKeyCharacteristics = 24;
        static final int TRANSACTION_getState = 1;
        static final int TRANSACTION_get_pubkey = 16;
        static final int TRANSACTION_getmtime = 19;
        static final int TRANSACTION_grant = 17;
        static final int TRANSACTION_importKey = 25;
        static final int TRANSACTION_importWrappedKey = 38;
        static final int TRANSACTION_import_key = 13;
        static final int TRANSACTION_insert = 3;
        static final int TRANSACTION_isConfirmationPromptSupported = 41;
        static final int TRANSACTION_isEmpty = 11;
        static final int TRANSACTION_isOperationAuthorized = 31;
        static final int TRANSACTION_is_hardware_backed = 20;
        static final int TRANSACTION_list = 6;
        static final int TRANSACTION_lock = 9;
        static final int TRANSACTION_onDeviceOffBody = 37;
        static final int TRANSACTION_onKeyguardVisibilityChanged = 42;
        static final int TRANSACTION_onUserAdded = 33;
        static final int TRANSACTION_onUserPasswordChanged = 8;
        static final int TRANSACTION_onUserRemoved = 34;
        static final int TRANSACTION_presentConfirmationPrompt = 39;
        static final int TRANSACTION_reset = 7;
        static final int TRANSACTION_sign = 14;
        static final int TRANSACTION_ungrant = 18;
        static final int TRANSACTION_unlock = 10;
        static final int TRANSACTION_update = 28;
        static final int TRANSACTION_verify = 15;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IKeystoreService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IKeystoreService)) {
                return (IKeystoreService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            KeymasterBlob _arg1;
            KeymasterBlob _arg2;
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg0 = data.readInt();
                        int _result = getState(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg02 = data.readString();
                        int _arg12 = data.readInt();
                        byte[] _result2 = get(_arg02, _arg12);
                        reply.writeNoException();
                        reply.writeByteArray(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg03 = data.readString();
                        byte[] _arg13 = data.createByteArray();
                        int _arg22 = data.readInt();
                        int _arg3 = data.readInt();
                        int _result3 = insert(_arg03, _arg13, _arg22, _arg3);
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg04 = data.readString();
                        int _arg14 = data.readInt();
                        int _result4 = del(_arg04, _arg14);
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg05 = data.readString();
                        int _arg15 = data.readInt();
                        int _result5 = exist(_arg05, _arg15);
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg06 = data.readString();
                        int _arg16 = data.readInt();
                        String[] _result6 = list(_arg06, _arg16);
                        reply.writeNoException();
                        reply.writeStringArray(_result6);
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        int _result7 = reset();
                        reply.writeNoException();
                        reply.writeInt(_result7);
                        return true;
                    case 8:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg07 = data.readInt();
                        String _arg17 = data.readString();
                        int _result8 = onUserPasswordChanged(_arg07, _arg17);
                        reply.writeNoException();
                        reply.writeInt(_result8);
                        return true;
                    case 9:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg08 = data.readInt();
                        int _result9 = lock(_arg08);
                        reply.writeNoException();
                        reply.writeInt(_result9);
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg09 = data.readInt();
                        String _arg18 = data.readString();
                        int _result10 = unlock(_arg09, _arg18);
                        reply.writeNoException();
                        reply.writeInt(_result10);
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg010 = data.readInt();
                        int _result11 = isEmpty(_arg010);
                        reply.writeNoException();
                        reply.writeInt(_result11);
                        return true;
                    case 12:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg011 = data.readString();
                        int _arg19 = data.readInt();
                        int _arg23 = data.readInt();
                        int _arg32 = data.readInt();
                        int _arg4 = data.readInt();
                        int _result12 = generate(_arg011, _arg19, _arg23, _arg32, _arg4, data.readInt() != 0 ? KeystoreArguments.CREATOR.createFromParcel(data) : null);
                        reply.writeNoException();
                        reply.writeInt(_result12);
                        return true;
                    case 13:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg012 = data.readString();
                        byte[] _arg110 = data.createByteArray();
                        int _arg24 = data.readInt();
                        int _arg33 = data.readInt();
                        int _result13 = import_key(_arg012, _arg110, _arg24, _arg33);
                        reply.writeNoException();
                        reply.writeInt(_result13);
                        return true;
                    case 14:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg013 = data.readString();
                        byte[] _arg111 = data.createByteArray();
                        byte[] _result14 = sign(_arg013, _arg111);
                        reply.writeNoException();
                        reply.writeByteArray(_result14);
                        return true;
                    case 15:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg014 = data.readString();
                        byte[] _arg112 = data.createByteArray();
                        byte[] _arg25 = data.createByteArray();
                        int _result15 = verify(_arg014, _arg112, _arg25);
                        reply.writeNoException();
                        reply.writeInt(_result15);
                        return true;
                    case 16:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg015 = data.readString();
                        byte[] _result16 = get_pubkey(_arg015);
                        reply.writeNoException();
                        reply.writeByteArray(_result16);
                        return true;
                    case 17:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg016 = data.readString();
                        int _arg113 = data.readInt();
                        String _result17 = grant(_arg016, _arg113);
                        reply.writeNoException();
                        reply.writeString(_result17);
                        return true;
                    case 18:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg017 = data.readString();
                        int _arg114 = data.readInt();
                        int _result18 = ungrant(_arg017, _arg114);
                        reply.writeNoException();
                        reply.writeInt(_result18);
                        return true;
                    case 19:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg018 = data.readString();
                        int _arg115 = data.readInt();
                        long _result19 = getmtime(_arg018, _arg115);
                        reply.writeNoException();
                        reply.writeLong(_result19);
                        return true;
                    case 20:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg019 = data.readString();
                        int _result20 = is_hardware_backed(_arg019);
                        reply.writeNoException();
                        reply.writeInt(_result20);
                        return true;
                    case 21:
                        data.enforceInterface(DESCRIPTOR);
                        long _arg020 = data.readLong();
                        int _result21 = clear_uid(_arg020);
                        reply.writeNoException();
                        reply.writeInt(_result21);
                        return true;
                    case 22:
                        data.enforceInterface(DESCRIPTOR);
                        byte[] _arg021 = data.createByteArray();
                        int _arg116 = data.readInt();
                        int _result22 = addRngEntropy(_arg021, _arg116);
                        reply.writeNoException();
                        reply.writeInt(_result22);
                        return true;
                    case 23:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg022 = data.readString();
                        KeymasterArguments _arg117 = data.readInt() != 0 ? KeymasterArguments.CREATOR.createFromParcel(data) : null;
                        byte[] _arg26 = data.createByteArray();
                        int _arg34 = data.readInt();
                        int _arg42 = data.readInt();
                        KeyCharacteristics _arg5 = new KeyCharacteristics();
                        int _result23 = generateKey(_arg022, _arg117, _arg26, _arg34, _arg42, _arg5);
                        reply.writeNoException();
                        reply.writeInt(_result23);
                        reply.writeInt(1);
                        _arg5.writeToParcel(reply, 1);
                        return true;
                    case 24:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg023 = data.readString();
                        if (data.readInt() != 0) {
                            KeymasterBlob _arg118 = KeymasterBlob.CREATOR.createFromParcel(data);
                            _arg1 = _arg118;
                        } else {
                            _arg1 = null;
                        }
                        KeymasterBlob _arg27 = data.readInt() != 0 ? KeymasterBlob.CREATOR.createFromParcel(data) : null;
                        int _arg35 = data.readInt();
                        KeyCharacteristics _arg43 = new KeyCharacteristics();
                        int _result24 = getKeyCharacteristics(_arg023, _arg1, _arg27, _arg35, _arg43);
                        reply.writeNoException();
                        reply.writeInt(_result24);
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                        return true;
                    case 25:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg024 = data.readString();
                        KeymasterArguments _arg119 = data.readInt() != 0 ? KeymasterArguments.CREATOR.createFromParcel(data) : null;
                        int _arg28 = data.readInt();
                        byte[] _arg36 = data.createByteArray();
                        int _arg44 = data.readInt();
                        int _arg52 = data.readInt();
                        KeyCharacteristics _arg6 = new KeyCharacteristics();
                        int _result25 = importKey(_arg024, _arg119, _arg28, _arg36, _arg44, _arg52, _arg6);
                        reply.writeNoException();
                        reply.writeInt(_result25);
                        reply.writeInt(1);
                        _arg6.writeToParcel(reply, 1);
                        return true;
                    case 26:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg025 = data.readString();
                        int _arg120 = data.readInt();
                        if (data.readInt() != 0) {
                            KeymasterBlob _arg29 = KeymasterBlob.CREATOR.createFromParcel(data);
                            _arg2 = _arg29;
                        } else {
                            _arg2 = null;
                        }
                        KeymasterBlob _arg37 = data.readInt() != 0 ? KeymasterBlob.CREATOR.createFromParcel(data) : null;
                        int _arg45 = data.readInt();
                        ExportResult _result26 = exportKey(_arg025, _arg120, _arg2, _arg37, _arg45);
                        reply.writeNoException();
                        if (_result26 != null) {
                            reply.writeInt(1);
                            _result26.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 27:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg026 = data.readStrongBinder();
                        String _arg121 = data.readString();
                        int _arg210 = data.readInt();
                        boolean _arg38 = data.readInt() != 0;
                        KeymasterArguments _arg46 = data.readInt() != 0 ? KeymasterArguments.CREATOR.createFromParcel(data) : null;
                        OperationResult _result27 = begin(_arg026, _arg121, _arg210, _arg38, _arg46, data.createByteArray(), data.readInt());
                        reply.writeNoException();
                        if (_result27 != null) {
                            reply.writeInt(1);
                            _result27.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 28:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg027 = data.readStrongBinder();
                        KeymasterArguments _arg122 = data.readInt() != 0 ? KeymasterArguments.CREATOR.createFromParcel(data) : null;
                        byte[] _arg211 = data.createByteArray();
                        OperationResult _result28 = update(_arg027, _arg122, _arg211);
                        reply.writeNoException();
                        if (_result28 != null) {
                            reply.writeInt(1);
                            _result28.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 29:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg028 = data.readStrongBinder();
                        KeymasterArguments _arg123 = data.readInt() != 0 ? KeymasterArguments.CREATOR.createFromParcel(data) : null;
                        byte[] _arg212 = data.createByteArray();
                        byte[] _arg39 = data.createByteArray();
                        OperationResult _result29 = finish(_arg028, _arg123, _arg212, _arg39);
                        reply.writeNoException();
                        if (_result29 != null) {
                            reply.writeInt(1);
                            _result29.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 30:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg029 = data.readStrongBinder();
                        int _result30 = abort(_arg029);
                        reply.writeNoException();
                        reply.writeInt(_result30);
                        return true;
                    case 31:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg030 = data.readStrongBinder();
                        boolean isOperationAuthorized = isOperationAuthorized(_arg030);
                        reply.writeNoException();
                        reply.writeInt(isOperationAuthorized ? 1 : 0);
                        return true;
                    case 32:
                        data.enforceInterface(DESCRIPTOR);
                        byte[] _arg031 = data.createByteArray();
                        int _result31 = addAuthToken(_arg031);
                        reply.writeNoException();
                        reply.writeInt(_result31);
                        return true;
                    case 33:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg032 = data.readInt();
                        int _arg124 = data.readInt();
                        int _result32 = onUserAdded(_arg032, _arg124);
                        reply.writeNoException();
                        reply.writeInt(_result32);
                        return true;
                    case 34:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg033 = data.readInt();
                        int _result33 = onUserRemoved(_arg033);
                        reply.writeNoException();
                        reply.writeInt(_result33);
                        return true;
                    case 35:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg034 = data.readString();
                        KeymasterArguments _arg125 = data.readInt() != 0 ? KeymasterArguments.CREATOR.createFromParcel(data) : null;
                        KeymasterCertificateChain _arg213 = new KeymasterCertificateChain();
                        int _result34 = attestKey(_arg034, _arg125, _arg213);
                        reply.writeNoException();
                        reply.writeInt(_result34);
                        reply.writeInt(1);
                        _arg213.writeToParcel(reply, 1);
                        return true;
                    case 36:
                        data.enforceInterface(DESCRIPTOR);
                        KeymasterArguments _arg035 = data.readInt() != 0 ? KeymasterArguments.CREATOR.createFromParcel(data) : null;
                        KeymasterCertificateChain _arg126 = new KeymasterCertificateChain();
                        int _result35 = attestDeviceIds(_arg035, _arg126);
                        reply.writeNoException();
                        reply.writeInt(_result35);
                        reply.writeInt(1);
                        _arg126.writeToParcel(reply, 1);
                        return true;
                    case 37:
                        data.enforceInterface(DESCRIPTOR);
                        int _result36 = onDeviceOffBody();
                        reply.writeNoException();
                        reply.writeInt(_result36);
                        return true;
                    case 38:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg036 = data.readString();
                        byte[] _arg127 = data.createByteArray();
                        String _arg214 = data.readString();
                        byte[] _arg310 = data.createByteArray();
                        KeymasterArguments _arg47 = data.readInt() != 0 ? KeymasterArguments.CREATOR.createFromParcel(data) : null;
                        long _arg53 = data.readLong();
                        long _arg62 = data.readLong();
                        KeyCharacteristics _arg7 = new KeyCharacteristics();
                        int _result37 = importWrappedKey(_arg036, _arg127, _arg214, _arg310, _arg47, _arg53, _arg62, _arg7);
                        reply.writeNoException();
                        reply.writeInt(_result37);
                        reply.writeInt(1);
                        _arg7.writeToParcel(reply, 1);
                        return true;
                    case 39:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg037 = data.readStrongBinder();
                        String _arg128 = data.readString();
                        byte[] _arg215 = data.createByteArray();
                        String _arg311 = data.readString();
                        int _arg48 = data.readInt();
                        int _result38 = presentConfirmationPrompt(_arg037, _arg128, _arg215, _arg311, _arg48);
                        reply.writeNoException();
                        reply.writeInt(_result38);
                        return true;
                    case 40:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg038 = data.readStrongBinder();
                        int _result39 = cancelConfirmationPrompt(_arg038);
                        reply.writeNoException();
                        reply.writeInt(_result39);
                        return true;
                    case 41:
                        data.enforceInterface(DESCRIPTOR);
                        boolean isConfirmationPromptSupported = isConfirmationPromptSupported();
                        reply.writeNoException();
                        reply.writeInt(isConfirmationPromptSupported ? 1 : 0);
                        return true;
                    case 42:
                        data.enforceInterface(DESCRIPTOR);
                        boolean _arg039 = data.readInt() != 0;
                        int _arg129 = data.readInt();
                        int _result40 = onKeyguardVisibilityChanged(_arg039, _arg129);
                        reply.writeNoException();
                        reply.writeInt(_result40);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(DESCRIPTOR);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IKeystoreService {
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

            public synchronized int getState(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized byte[] get(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int insert(String name, byte[] item, int uid, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeByteArray(item);
                    _data.writeInt(uid);
                    _data.writeInt(flags);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int del(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int exist(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String[] list(String namePrefix, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(namePrefix);
                    _data.writeInt(uid);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int reset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int onUserPasswordChanged(int userId, String newPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(newPassword);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int lock(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int unlock(int userId, String userPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(userPassword);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int isEmpty(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int generate(String name, int uid, int keyType, int keySize, int flags, KeystoreArguments args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    _data.writeInt(keyType);
                    _data.writeInt(keySize);
                    _data.writeInt(flags);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int import_key(String name, byte[] data, int uid, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeByteArray(data);
                    _data.writeInt(uid);
                    _data.writeInt(flags);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized byte[] sign(String name, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeByteArray(data);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int verify(String name, byte[] data, byte[] signature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeByteArray(data);
                    _data.writeByteArray(signature);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized byte[] get_pubkey(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized String grant(String name, int granteeUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(granteeUid);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int ungrant(String name, int granteeUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(granteeUid);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized long getmtime(String name, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(uid);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int is_hardware_backed(String string) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(string);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int clear_uid(long uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(uid);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int addRngEntropy(byte[] data, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(data);
                    _data.writeInt(flags);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int generateKey(String alias, KeymasterArguments arguments, byte[] entropy, int uid, int flags, KeyCharacteristics characteristics) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    if (arguments != null) {
                        _data.writeInt(1);
                        arguments.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(entropy);
                    _data.writeInt(uid);
                    _data.writeInt(flags);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        characteristics.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int getKeyCharacteristics(String alias, KeymasterBlob clientId, KeymasterBlob appData, int uid, KeyCharacteristics characteristics) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    if (clientId != null) {
                        _data.writeInt(1);
                        clientId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (appData != null) {
                        _data.writeInt(1);
                        appData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        characteristics.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int importKey(String alias, KeymasterArguments arguments, int format, byte[] keyData, int uid, int flags, KeyCharacteristics characteristics) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    if (arguments != null) {
                        _data.writeInt(1);
                        arguments.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(format);
                    _data.writeByteArray(keyData);
                    _data.writeInt(uid);
                    _data.writeInt(flags);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        characteristics.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized ExportResult exportKey(String alias, int format, KeymasterBlob clientId, KeymasterBlob appData, int uid) throws RemoteException {
                ExportResult _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(format);
                    if (clientId != null) {
                        _data.writeInt(1);
                        clientId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (appData != null) {
                        _data.writeInt(1);
                        appData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ExportResult.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized OperationResult begin(IBinder appToken, String alias, int purpose, boolean pruneable, KeymasterArguments params, byte[] entropy, int uid) throws RemoteException {
                OperationResult _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(appToken);
                    _data.writeString(alias);
                    _data.writeInt(purpose);
                    _data.writeInt(pruneable ? 1 : 0);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(entropy);
                    _data.writeInt(uid);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = OperationResult.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized OperationResult update(IBinder token, KeymasterArguments params, byte[] input) throws RemoteException {
                OperationResult _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(input);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = OperationResult.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized OperationResult finish(IBinder token, KeymasterArguments params, byte[] signature, byte[] entropy) throws RemoteException {
                OperationResult _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(signature);
                    _data.writeByteArray(entropy);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = OperationResult.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int abort(IBinder handle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(handle);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized boolean isOperationAuthorized(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int addAuthToken(byte[] authToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(authToken);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int onUserAdded(int userId, int parentId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(parentId);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int onUserRemoved(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int attestKey(String alias, KeymasterArguments params, KeymasterCertificateChain chain) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        chain.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int attestDeviceIds(KeymasterArguments params, KeymasterCertificateChain chain) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        chain.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int onDeviceOffBody() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int importWrappedKey(String wrappedKeyAlias, byte[] wrappedKey, String wrappingKeyAlias, byte[] maskingKey, KeymasterArguments arguments, long rootSid, long fingerprintSid, KeyCharacteristics characteristics) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(wrappedKeyAlias);
                    _data.writeByteArray(wrappedKey);
                    _data.writeString(wrappingKeyAlias);
                    _data.writeByteArray(maskingKey);
                    if (arguments != null) {
                        _data.writeInt(1);
                        arguments.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(rootSid);
                    _data.writeLong(fingerprintSid);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        characteristics.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int presentConfirmationPrompt(IBinder listener, String promptText, byte[] extraData, String locale, int uiOptionsAsFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener);
                    _data.writeString(promptText);
                    _data.writeByteArray(extraData);
                    _data.writeString(locale);
                    _data.writeInt(uiOptionsAsFlags);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int cancelConfirmationPrompt(IBinder listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized boolean isConfirmationPromptSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeystoreService
            public synchronized int onKeyguardVisibilityChanged(boolean isShowing, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isShowing ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
