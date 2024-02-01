package com.android.internal.widget;

import android.app.PendingIntent;
import android.app.trust.IStrongAuthTracker;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.security.keystore.recovery.KeyChainProtectionParams;
import android.security.keystore.recovery.KeyChainSnapshot;
import android.security.keystore.recovery.RecoveryCertPath;
import android.security.keystore.recovery.WrappedApplicationKey;
import com.android.internal.widget.ICheckCredentialProgressCallback;
import java.util.List;
import java.util.Map;
/* loaded from: classes3.dex */
public interface ILockSettings extends IInterface {
    synchronized VerifyCredentialResponse checkCredential(String str, int i, int i2, ICheckCredentialProgressCallback iCheckCredentialProgressCallback) throws RemoteException;

    synchronized boolean checkVoldPassword(int i) throws RemoteException;

    synchronized void closeSession(String str) throws RemoteException;

    synchronized String generateKey(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean getBoolean(String str, boolean z, int i) throws RemoteException;

    synchronized byte[] getHashFactor(String str, int i) throws RemoteException;

    synchronized String getKey(String str) throws RemoteException;

    synchronized KeyChainSnapshot getKeyChainSnapshot() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    long getLong(String str, long j, int i) throws RemoteException;

    synchronized int[] getRecoverySecretTypes() throws RemoteException;

    synchronized Map getRecoveryStatus() throws RemoteException;

    synchronized boolean getSeparateProfileChallengeEnabled(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    String getString(String str, String str2, int i) throws RemoteException;

    synchronized int getStrongAuthForUser(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean havePassword(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean havePattern(int i) throws RemoteException;

    synchronized String importKey(String str, byte[] bArr) throws RemoteException;

    synchronized void initRecoveryServiceWithSigFile(String str, byte[] bArr, byte[] bArr2) throws RemoteException;

    synchronized Map recoverKeyChainSnapshot(String str, byte[] bArr, List<WrappedApplicationKey> list) throws RemoteException;

    synchronized void registerStrongAuthTracker(IStrongAuthTracker iStrongAuthTracker) throws RemoteException;

    synchronized void removeKey(String str) throws RemoteException;

    synchronized void requireStrongAuth(int i, int i2) throws RemoteException;

    synchronized void resetKeyStore(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setBoolean(String str, boolean z, int i) throws RemoteException;

    synchronized void setLockCredential(String str, int i, String str2, int i2, int i3) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setLong(String str, long j, int i) throws RemoteException;

    synchronized void setRecoverySecretTypes(int[] iArr) throws RemoteException;

    synchronized void setRecoveryStatus(String str, int i) throws RemoteException;

    synchronized void setSeparateProfileChallengeEnabled(int i, boolean z, String str) throws RemoteException;

    synchronized void setServerParams(byte[] bArr) throws RemoteException;

    synchronized void setSnapshotCreatedPendingIntent(PendingIntent pendingIntent) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setString(String str, String str2, int i) throws RemoteException;

    synchronized byte[] startRecoverySessionWithCertPath(String str, String str2, RecoveryCertPath recoveryCertPath, byte[] bArr, byte[] bArr2, List<KeyChainProtectionParams> list) throws RemoteException;

    synchronized void systemReady() throws RemoteException;

    synchronized void unregisterStrongAuthTracker(IStrongAuthTracker iStrongAuthTracker) throws RemoteException;

    synchronized void userPresent(int i) throws RemoteException;

    synchronized VerifyCredentialResponse verifyCredential(String str, int i, long j, int i2) throws RemoteException;

    synchronized VerifyCredentialResponse verifyTiedProfileChallenge(String str, int i, long j, int i2) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ILockSettings {
        private static final String DESCRIPTOR = "com.android.internal.widget.ILockSettings";
        static final int TRANSACTION_checkCredential = 9;
        static final int TRANSACTION_checkVoldPassword = 12;
        static final int TRANSACTION_closeSession = 38;
        static final int TRANSACTION_generateKey = 26;
        static final int TRANSACTION_getBoolean = 4;
        static final int TRANSACTION_getHashFactor = 15;
        static final int TRANSACTION_getKey = 28;
        static final int TRANSACTION_getKeyChainSnapshot = 25;
        static final int TRANSACTION_getLong = 5;
        static final int TRANSACTION_getRecoverySecretTypes = 35;
        static final int TRANSACTION_getRecoveryStatus = 33;
        static final int TRANSACTION_getSeparateProfileChallengeEnabled = 17;
        static final int TRANSACTION_getString = 6;
        static final int TRANSACTION_getStrongAuthForUser = 23;
        static final int TRANSACTION_havePassword = 14;
        static final int TRANSACTION_havePattern = 13;
        static final int TRANSACTION_importKey = 27;
        static final int TRANSACTION_initRecoveryServiceWithSigFile = 24;
        static final int TRANSACTION_recoverKeyChainSnapshot = 37;
        static final int TRANSACTION_registerStrongAuthTracker = 18;
        static final int TRANSACTION_removeKey = 29;
        static final int TRANSACTION_requireStrongAuth = 20;
        static final int TRANSACTION_resetKeyStore = 8;
        static final int TRANSACTION_setBoolean = 1;
        static final int TRANSACTION_setLockCredential = 7;
        static final int TRANSACTION_setLong = 2;
        static final int TRANSACTION_setRecoverySecretTypes = 34;
        static final int TRANSACTION_setRecoveryStatus = 32;
        static final int TRANSACTION_setSeparateProfileChallengeEnabled = 16;
        static final int TRANSACTION_setServerParams = 31;
        static final int TRANSACTION_setSnapshotCreatedPendingIntent = 30;
        static final int TRANSACTION_setString = 3;
        static final int TRANSACTION_startRecoverySessionWithCertPath = 36;
        static final int TRANSACTION_systemReady = 21;
        static final int TRANSACTION_unregisterStrongAuthTracker = 19;
        static final int TRANSACTION_userPresent = 22;
        static final int TRANSACTION_verifyCredential = 10;
        static final int TRANSACTION_verifyTiedProfileChallenge = 11;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static ILockSettings asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILockSettings)) {
                return (ILockSettings) iin;
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
                    String _arg0 = data.readString();
                    boolean _arg1 = data.readInt() != 0;
                    int _arg2 = data.readInt();
                    setBoolean(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    long _arg12 = data.readLong();
                    int _arg22 = data.readInt();
                    setLong(_arg02, _arg12, _arg22);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    String _arg13 = data.readString();
                    int _arg23 = data.readInt();
                    setString(_arg03, _arg13, _arg23);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    boolean _arg14 = data.readInt() != 0;
                    int _arg24 = data.readInt();
                    boolean z = getBoolean(_arg04, _arg14, _arg24);
                    reply.writeNoException();
                    reply.writeInt(z ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    long _arg15 = data.readLong();
                    int _arg25 = data.readInt();
                    long _result = getLong(_arg05, _arg15, _arg25);
                    reply.writeNoException();
                    reply.writeLong(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    String _arg16 = data.readString();
                    int _arg26 = data.readInt();
                    String _result2 = getString(_arg06, _arg16, _arg26);
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    int _arg17 = data.readInt();
                    String _arg27 = data.readString();
                    int _arg3 = data.readInt();
                    int _arg4 = data.readInt();
                    setLockCredential(_arg07, _arg17, _arg27, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    resetKeyStore(_arg08);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    int _arg18 = data.readInt();
                    int _arg28 = data.readInt();
                    ICheckCredentialProgressCallback _arg32 = ICheckCredentialProgressCallback.Stub.asInterface(data.readStrongBinder());
                    VerifyCredentialResponse _result3 = checkCredential(_arg09, _arg18, _arg28, _arg32);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    int _arg19 = data.readInt();
                    long _arg29 = data.readLong();
                    int _arg33 = data.readInt();
                    VerifyCredentialResponse _result4 = verifyCredential(_arg010, _arg19, _arg29, _arg33);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    int _arg110 = data.readInt();
                    long _arg210 = data.readLong();
                    int _arg34 = data.readInt();
                    VerifyCredentialResponse _result5 = verifyTiedProfileChallenge(_arg011, _arg110, _arg210, _arg34);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    boolean checkVoldPassword = checkVoldPassword(_arg012);
                    reply.writeNoException();
                    reply.writeInt(checkVoldPassword ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    boolean havePattern = havePattern(_arg013);
                    reply.writeNoException();
                    reply.writeInt(havePattern ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    boolean havePassword = havePassword(_arg014);
                    reply.writeNoException();
                    reply.writeInt(havePassword ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    int _arg111 = data.readInt();
                    byte[] _result6 = getHashFactor(_arg015, _arg111);
                    reply.writeNoException();
                    reply.writeByteArray(_result6);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    boolean _arg112 = data.readInt() != 0;
                    String _arg211 = data.readString();
                    setSeparateProfileChallengeEnabled(_arg016, _arg112, _arg211);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    boolean separateProfileChallengeEnabled = getSeparateProfileChallengeEnabled(_arg017);
                    reply.writeNoException();
                    reply.writeInt(separateProfileChallengeEnabled ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    IStrongAuthTracker _arg018 = IStrongAuthTracker.Stub.asInterface(data.readStrongBinder());
                    registerStrongAuthTracker(_arg018);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    IStrongAuthTracker _arg019 = IStrongAuthTracker.Stub.asInterface(data.readStrongBinder());
                    unregisterStrongAuthTracker(_arg019);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    int _arg113 = data.readInt();
                    requireStrongAuth(_arg020, _arg113);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    systemReady();
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    userPresent(_arg021);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    int _result7 = getStrongAuthForUser(_arg022);
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg023 = data.readString();
                    byte[] _arg114 = data.createByteArray();
                    byte[] _arg212 = data.createByteArray();
                    initRecoveryServiceWithSigFile(_arg023, _arg114, _arg212);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    KeyChainSnapshot _result8 = getKeyChainSnapshot();
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    String _result9 = generateKey(_arg024);
                    reply.writeNoException();
                    reply.writeString(_result9);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg025 = data.readString();
                    byte[] _arg115 = data.createByteArray();
                    String _result10 = importKey(_arg025, _arg115);
                    reply.writeNoException();
                    reply.writeString(_result10);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg026 = data.readString();
                    String _result11 = getKey(_arg026);
                    reply.writeNoException();
                    reply.writeString(_result11);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg027 = data.readString();
                    removeKey(_arg027);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    PendingIntent _arg028 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    setSnapshotCreatedPendingIntent(_arg028);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg029 = data.createByteArray();
                    setServerParams(_arg029);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg030 = data.readString();
                    int _arg116 = data.readInt();
                    setRecoveryStatus(_arg030, _arg116);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    Map _result12 = getRecoveryStatus();
                    reply.writeNoException();
                    reply.writeMap(_result12);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg031 = data.createIntArray();
                    setRecoverySecretTypes(_arg031);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result13 = getRecoverySecretTypes();
                    reply.writeNoException();
                    reply.writeIntArray(_result13);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    String _arg117 = data.readString();
                    RecoveryCertPath _arg213 = data.readInt() != 0 ? RecoveryCertPath.CREATOR.createFromParcel(data) : null;
                    byte[] _arg35 = data.createByteArray();
                    byte[] _arg42 = data.createByteArray();
                    List<KeyChainProtectionParams> _arg5 = data.createTypedArrayList(KeyChainProtectionParams.CREATOR);
                    byte[] _result14 = startRecoverySessionWithCertPath(_arg032, _arg117, _arg213, _arg35, _arg42, _arg5);
                    reply.writeNoException();
                    reply.writeByteArray(_result14);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    byte[] _arg118 = data.createByteArray();
                    List<WrappedApplicationKey> _arg214 = data.createTypedArrayList(WrappedApplicationKey.CREATOR);
                    Map _result15 = recoverKeyChainSnapshot(_arg033, _arg118, _arg214);
                    reply.writeNoException();
                    reply.writeMap(_result15);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg034 = data.readString();
                    closeSession(_arg034);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements ILockSettings {
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

            public synchronized void setBoolean(String key, boolean value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(value ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setLong(String key, long value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeLong(value);
                    _data.writeInt(userId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setString(String key, String value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(value);
                    _data.writeInt(userId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean getBoolean(String key, boolean defaultValue, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(defaultValue ? 1 : 0);
                    _data.writeInt(userId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized long getLong(String key, long defaultValue, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeLong(defaultValue);
                    _data.writeInt(userId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getString(String key, String defaultValue, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(defaultValue);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void setLockCredential(String credential, int type, String savedCredential, int requestedQuality, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(credential);
                    _data.writeInt(type);
                    _data.writeString(savedCredential);
                    _data.writeInt(requestedQuality);
                    _data.writeInt(userId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void resetKeyStore(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized VerifyCredentialResponse checkCredential(String credential, int type, int userId, ICheckCredentialProgressCallback progressCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(credential);
                    _data.writeInt(type);
                    _data.writeInt(userId);
                    VerifyCredentialResponse _result = null;
                    _data.writeStrongBinder(progressCallback != null ? progressCallback.asBinder() : null);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VerifyCredentialResponse.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized VerifyCredentialResponse verifyCredential(String credential, int type, long challenge, int userId) throws RemoteException {
                VerifyCredentialResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(credential);
                    _data.writeInt(type);
                    _data.writeLong(challenge);
                    _data.writeInt(userId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VerifyCredentialResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized VerifyCredentialResponse verifyTiedProfileChallenge(String credential, int type, long challenge, int userId) throws RemoteException {
                VerifyCredentialResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(credential);
                    _data.writeInt(type);
                    _data.writeLong(challenge);
                    _data.writeInt(userId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VerifyCredentialResponse.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized boolean checkVoldPassword(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean havePattern(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean havePassword(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized byte[] getHashFactor(String currentCredential, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(currentCredential);
                    _data.writeInt(userId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void setSeparateProfileChallengeEnabled(int userId, boolean enabled, String managedUserPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(enabled ? 1 : 0);
                    _data.writeString(managedUserPassword);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized boolean getSeparateProfileChallengeEnabled(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void registerStrongAuthTracker(IStrongAuthTracker tracker) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(tracker != null ? tracker.asBinder() : null);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void unregisterStrongAuthTracker(IStrongAuthTracker tracker) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(tracker != null ? tracker.asBinder() : null);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void requireStrongAuth(int strongAuthReason, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(strongAuthReason);
                    _data.writeInt(userId);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void systemReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void userPresent(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized int getStrongAuthForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void initRecoveryServiceWithSigFile(String rootCertificateAlias, byte[] recoveryServiceCertFile, byte[] recoveryServiceSigFile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rootCertificateAlias);
                    _data.writeByteArray(recoveryServiceCertFile);
                    _data.writeByteArray(recoveryServiceSigFile);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized KeyChainSnapshot getKeyChainSnapshot() throws RemoteException {
                KeyChainSnapshot _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = KeyChainSnapshot.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized String generateKey(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized String importKey(String alias, byte[] keyBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(keyBytes);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized String getKey(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void removeKey(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void setSnapshotCreatedPendingIntent(PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void setServerParams(byte[] serverParams) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(serverParams);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void setRecoveryStatus(String alias, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(status);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized Map getRecoveryStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void setRecoverySecretTypes(int[] secretTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(secretTypes);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized int[] getRecoverySecretTypes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized byte[] startRecoverySessionWithCertPath(String sessionId, String rootCertificateAlias, RecoveryCertPath verifierCertPath, byte[] vaultParams, byte[] vaultChallenge, List<KeyChainProtectionParams> secrets) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sessionId);
                    _data.writeString(rootCertificateAlias);
                    if (verifierCertPath != null) {
                        _data.writeInt(1);
                        verifierCertPath.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(vaultParams);
                    _data.writeByteArray(vaultChallenge);
                    _data.writeTypedList(secrets);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized Map recoverKeyChainSnapshot(String sessionId, byte[] recoveryKeyBlob, List<WrappedApplicationKey> applicationKeys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sessionId);
                    _data.writeByteArray(recoveryKeyBlob);
                    _data.writeTypedList(applicationKeys);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public synchronized void closeSession(String sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sessionId);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
