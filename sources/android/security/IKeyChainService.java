package android.security;

import android.content.pm.StringParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keystore.ParcelableKeyGenParameterSpec;
import java.util.List;
/* loaded from: classes2.dex */
public interface IKeyChainService extends IInterface {
    synchronized int attestKey(String str, byte[] bArr, int[] iArr, KeymasterCertificateChain keymasterCertificateChain) throws RemoteException;

    synchronized boolean containsCaAlias(String str) throws RemoteException;

    synchronized boolean deleteCaCertificate(String str) throws RemoteException;

    synchronized int generateKeyPair(String str, ParcelableKeyGenParameterSpec parcelableKeyGenParameterSpec) throws RemoteException;

    synchronized List<String> getCaCertificateChainAliases(String str, boolean z) throws RemoteException;

    synchronized byte[] getCaCertificates(String str) throws RemoteException;

    synchronized byte[] getCertificate(String str) throws RemoteException;

    synchronized byte[] getEncodedCaCertificate(String str, boolean z) throws RemoteException;

    synchronized StringParceledListSlice getSystemCaAliases() throws RemoteException;

    synchronized StringParceledListSlice getUserCaAliases() throws RemoteException;

    synchronized boolean hasGrant(int i, String str) throws RemoteException;

    synchronized String installCaCertificate(byte[] bArr) throws RemoteException;

    synchronized boolean installKeyPair(byte[] bArr, byte[] bArr2, byte[] bArr3, String str) throws RemoteException;

    synchronized boolean isUserSelectable(String str) throws RemoteException;

    synchronized boolean removeKeyPair(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String requestPrivateKey(String str) throws RemoteException;

    synchronized boolean reset() throws RemoteException;

    synchronized void setGrant(int i, String str, boolean z) throws RemoteException;

    synchronized boolean setKeyPairCertificate(String str, byte[] bArr, byte[] bArr2) throws RemoteException;

    synchronized void setUserSelectable(String str, boolean z) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IKeyChainService {
        private static final String DESCRIPTOR = "android.security.IKeyChainService";
        static final int TRANSACTION_attestKey = 7;
        static final int TRANSACTION_containsCaAlias = 16;
        static final int TRANSACTION_deleteCaCertificate = 12;
        static final int TRANSACTION_generateKeyPair = 6;
        static final int TRANSACTION_getCaCertificateChainAliases = 18;
        static final int TRANSACTION_getCaCertificates = 3;
        static final int TRANSACTION_getCertificate = 2;
        static final int TRANSACTION_getEncodedCaCertificate = 17;
        static final int TRANSACTION_getSystemCaAliases = 15;
        static final int TRANSACTION_getUserCaAliases = 14;
        static final int TRANSACTION_hasGrant = 20;
        static final int TRANSACTION_installCaCertificate = 9;
        static final int TRANSACTION_installKeyPair = 10;
        static final int TRANSACTION_isUserSelectable = 4;
        static final int TRANSACTION_removeKeyPair = 11;
        static final int TRANSACTION_requestPrivateKey = 1;
        static final int TRANSACTION_reset = 13;
        static final int TRANSACTION_setGrant = 19;
        static final int TRANSACTION_setKeyPairCertificate = 8;
        static final int TRANSACTION_setUserSelectable = 5;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IKeyChainService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IKeyChainService)) {
                return (IKeyChainService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg2;
            ParcelableKeyGenParameterSpec _arg1;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    String _result = requestPrivateKey(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    byte[] _result2 = getCertificate(_arg02);
                    reply.writeNoException();
                    reply.writeByteArray(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    byte[] _result3 = getCaCertificates(_arg03);
                    reply.writeNoException();
                    reply.writeByteArray(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    boolean isUserSelectable = isUserSelectable(_arg04);
                    reply.writeNoException();
                    reply.writeInt(isUserSelectable ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    _arg2 = data.readInt() != 0;
                    setUserSelectable(_arg05, _arg2);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = ParcelableKeyGenParameterSpec.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    int _result4 = generateKeyPair(_arg06, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    byte[] _arg12 = data.createByteArray();
                    int[] _arg22 = data.createIntArray();
                    KeymasterCertificateChain _arg3 = new KeymasterCertificateChain();
                    int _result5 = attestKey(_arg07, _arg12, _arg22, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    reply.writeInt(1);
                    _arg3.writeToParcel(reply, 1);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    byte[] _arg13 = data.createByteArray();
                    boolean keyPairCertificate = setKeyPairCertificate(_arg08, _arg13, data.createByteArray());
                    reply.writeNoException();
                    reply.writeInt(keyPairCertificate ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg09 = data.createByteArray();
                    String _result6 = installCaCertificate(_arg09);
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg010 = data.createByteArray();
                    byte[] _arg14 = data.createByteArray();
                    boolean installKeyPair = installKeyPair(_arg010, _arg14, data.createByteArray(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(installKeyPair ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    boolean removeKeyPair = removeKeyPair(_arg011);
                    reply.writeNoException();
                    reply.writeInt(removeKeyPair ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    boolean deleteCaCertificate = deleteCaCertificate(_arg012);
                    reply.writeNoException();
                    reply.writeInt(deleteCaCertificate ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reset = reset();
                    reply.writeNoException();
                    reply.writeInt(reset ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    StringParceledListSlice _result7 = getUserCaAliases();
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    StringParceledListSlice _result8 = getSystemCaAliases();
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    boolean containsCaAlias = containsCaAlias(_arg013);
                    reply.writeNoException();
                    reply.writeInt(containsCaAlias ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    _arg2 = data.readInt() != 0;
                    byte[] _result9 = getEncodedCaCertificate(_arg014, _arg2);
                    reply.writeNoException();
                    reply.writeByteArray(_result9);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    _arg2 = data.readInt() != 0;
                    List<String> _result10 = getCaCertificateChainAliases(_arg015, _arg2);
                    reply.writeNoException();
                    reply.writeStringList(_result10);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    String _arg15 = data.readString();
                    _arg2 = data.readInt() != 0;
                    setGrant(_arg016, _arg15, _arg2);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    String _arg16 = data.readString();
                    boolean hasGrant = hasGrant(_arg017, _arg16);
                    reply.writeNoException();
                    reply.writeInt(hasGrant ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IKeyChainService {
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

            public synchronized String requestPrivateKey(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized byte[] getCertificate(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized byte[] getCaCertificates(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized boolean isUserSelectable(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized void setUserSelectable(String alias, boolean isUserSelectable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(isUserSelectable ? 1 : 0);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized int generateKeyPair(String algorithm, ParcelableKeyGenParameterSpec spec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(algorithm);
                    if (spec != null) {
                        _data.writeInt(1);
                        spec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized int attestKey(String alias, byte[] challenge, int[] idAttestationFlags, KeymasterCertificateChain chain) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(challenge);
                    _data.writeIntArray(idAttestationFlags);
                    this.mRemote.transact(7, _data, _reply, 0);
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

            @Override // android.security.IKeyChainService
            public synchronized boolean setKeyPairCertificate(String alias, byte[] userCert, byte[] certChain) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(userCert);
                    _data.writeByteArray(certChain);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized String installCaCertificate(byte[] caCertificate) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(caCertificate);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized boolean installKeyPair(byte[] privateKey, byte[] userCert, byte[] certChain, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(privateKey);
                    _data.writeByteArray(userCert);
                    _data.writeByteArray(certChain);
                    _data.writeString(alias);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized boolean removeKeyPair(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized boolean deleteCaCertificate(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized boolean reset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized StringParceledListSlice getUserCaAliases() throws RemoteException {
                StringParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StringParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized StringParceledListSlice getSystemCaAliases() throws RemoteException {
                StringParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StringParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized boolean containsCaAlias(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized byte[] getEncodedCaCertificate(String alias, boolean includeDeletedSystem) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(includeDeletedSystem ? 1 : 0);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized List<String> getCaCertificateChainAliases(String rootAlias, boolean includeDeletedSystem) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rootAlias);
                    _data.writeInt(includeDeletedSystem ? 1 : 0);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized void setGrant(int uid, String alias, boolean value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(alias);
                    _data.writeInt(value ? 1 : 0);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.security.IKeyChainService
            public synchronized boolean hasGrant(int uid, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(alias);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
