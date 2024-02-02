package com.xpeng.security;

import android.accounts.AccountManager;
import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.security.GateKeeper;
import android.security.IKeystoreService;
import android.security.KeyStoreException;
import android.security.KeystoreArguments;
import android.security.keymaster.ExportResult;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterBlob;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keymaster.KeymasterDefs;
import android.security.keymaster.OperationResult;
import android.security.keystore.KeyExpiredException;
import android.security.keystore.KeyNotYetValidException;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.Locale;
/* loaded from: classes3.dex */
public class KeyStore {
    public static final int CANNOT_ATTEST_IDS = -66;
    public static final int CONFIRMATIONUI_ABORTED = 2;
    public static final int CONFIRMATIONUI_CANCELED = 1;
    public static final int CONFIRMATIONUI_IGNORED = 4;
    public static final int CONFIRMATIONUI_OK = 0;
    public static final int CONFIRMATIONUI_OPERATION_PENDING = 3;
    public static final int CONFIRMATIONUI_SYSTEM_ERROR = 5;
    public static final int CONFIRMATIONUI_UIERROR = 65536;
    public static final int CONFIRMATIONUI_UIERROR_MALFORMED_UTF8_ENCODING = 65539;
    public static final int CONFIRMATIONUI_UIERROR_MESSAGE_TOO_LONG = 65538;
    public static final int CONFIRMATIONUI_UIERROR_MISSING_GLYPH = 65537;
    public static final int CONFIRMATIONUI_UNEXPECTED = 7;
    public static final int CONFIRMATIONUI_UNIMPLEMENTED = 6;
    public static final int FLAG_CRITICAL_TO_DEVICE_ENCRYPTION = 8;
    public static final int FLAG_ENCRYPTED = 1;
    public static final int FLAG_NONE = 0;
    public static final int FLAG_SOFTWARE = 2;
    public static final int FLAG_STRONGBOX = 16;
    public static final int HARDWARE_TYPE_UNAVAILABLE = -68;
    public static final int KEY_NOT_FOUND = 7;
    public static final int LOCKED = 2;
    public static final int NO_ERROR = 1;
    public static final int OP_AUTH_NEEDED = 15;
    public static final int PERMISSION_DENIED = 6;
    public static final int PROTOCOL_ERROR = 5;
    public static final int SYSTEM_ERROR = 4;
    private static final String TAG = "KeyStore";
    public static final int UID_SELF = -1;
    public static final int UNDEFINED_ACTION = 9;
    public static final int UNINITIALIZED = 3;
    public static final int VALUE_CORRUPTED = 8;
    public static final int WRONG_PASSWORD = 10;
    private final IKeystoreService mBinder;
    private IBinder mToken;
    private int mError = 1;
    private final Context mContext = getApplicationContext();

    /* loaded from: classes3.dex */
    public enum State {
        UNLOCKED,
        LOCKED,
        UNINITIALIZED
    }

    private KeyStore(IKeystoreService binder) {
        this.mBinder = binder;
    }

    public static Context getApplicationContext() {
        Application application = ActivityThread.currentApplication();
        if (application == null) {
            throw new IllegalStateException("Failed to obtain application Context from ActivityThread");
        }
        return application;
    }

    public static KeyStore getInstance() {
        IKeystoreService keystore = IKeystoreService.Stub.asInterface(ServiceManager.getService("com.xpeng.security.keystore"));
        return new KeyStore(keystore);
    }

    private synchronized IBinder getToken() {
        if (this.mToken == null) {
            this.mToken = new Binder();
        }
        return this.mToken;
    }

    public State state(int userId) {
        if (isDebugKeystore()) {
            Log.i(TAG, "state: " + userId);
        }
        try {
            int ret = this.mBinder.getState(userId);
            switch (ret) {
                case 1:
                    return State.UNLOCKED;
                case 2:
                    return State.LOCKED;
                case 3:
                    return State.UNINITIALIZED;
                default:
                    throw new AssertionError(this.mError);
            }
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            throw new AssertionError(e);
        }
    }

    public State state() {
        return state(UserHandle.myUserId());
    }

    public boolean isUnlocked() {
        if (isDebugKeystore()) {
            Log.i(TAG, "isUnlocked");
        }
        return state() == State.UNLOCKED;
    }

    public byte[] get(String key, int uid) {
        if (isDebugKeystore()) {
            Log.i(TAG, "get key: " + key + ", uid:" + uid);
        }
        try {
            return this.mBinder.get(key != null ? key : "", uid);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        } catch (ServiceSpecificException e2) {
            Log.w(TAG, "KeyStore exception errorcode: " + e2.errorCode);
            return null;
        }
    }

    public byte[] get(String key) {
        return get(key, -1);
    }

    public boolean put(String key, byte[] value, int uid, int flags) {
        return insert(key, value, uid, flags) == 1;
    }

    public int insert(String key, byte[] value, int uid, int flags) {
        if (isDebugKeystore()) {
            Log.i(TAG, "insert key: " + key + ", uid:" + uid + ", flags:" + flags);
        }
        if (value == null) {
            try {
                value = new byte[0];
            } catch (RemoteException e) {
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            }
        }
        return this.mBinder.insert(key, value, uid, flags);
    }

    public boolean delete(String key, int uid) {
        if (isDebugKeystore()) {
            Log.i(TAG, "delete key: " + key + ", uid:" + uid);
        }
        try {
            int ret = this.mBinder.del(key, uid);
            if (ret != 1 && ret != 7) {
                return false;
            }
            return true;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean delete(String key) {
        return delete(key, -1);
    }

    public boolean contains(String key, int uid) {
        if (isDebugKeystore()) {
            Log.i(TAG, "contains key: " + key + ", uid:" + uid);
        }
        try {
            return this.mBinder.exist(key, uid) == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean contains(String key) {
        return contains(key, -1);
    }

    public String[] list(String prefix, int uid) {
        if (isDebugKeystore()) {
            Log.i(TAG, "list prefix: " + prefix + ", uid:" + uid);
        }
        try {
            return this.mBinder.list(prefix, uid);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        } catch (ServiceSpecificException e2) {
            Log.w(TAG, "KeyStore exception", e2);
            return null;
        }
    }

    public String[] list(String prefix) {
        return list(prefix, -1);
    }

    public boolean reset() {
        if (isDebugKeystore()) {
            Log.i(TAG, "reset");
        }
        try {
            return this.mBinder.reset() == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean lock(int userId) {
        if (isDebugKeystore()) {
            Log.i(TAG, "lock userId: " + userId);
        }
        try {
            return this.mBinder.lock(userId) == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean lock() {
        return lock(UserHandle.myUserId());
    }

    public boolean unlock(int userId, String password) {
        if (isDebugKeystore()) {
            Log.i(TAG, "unlock userId: " + userId + ", password:" + password);
        }
        try {
            this.mError = this.mBinder.unlock(userId, password != null ? password : "");
            return this.mError == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean unlock(String password) {
        return unlock(UserHandle.getUserId(Process.myUid()), password);
    }

    public boolean isEmpty(int userId) {
        if (isDebugKeystore()) {
            Log.i(TAG, "isEmpty userId: " + userId);
        }
        try {
            return this.mBinder.isEmpty(userId) != 0;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean isEmpty() {
        return isEmpty(UserHandle.myUserId());
    }

    public boolean generate(String key, int uid, int keyType, int keySize, int flags, byte[][] args) {
        if (isDebugKeystore()) {
            Log.i(TAG, "generate key: " + key + ", uid:" + uid + ", keyType:" + keyType + ", keySize:" + keySize + ", flags:" + flags);
        }
        try {
            return this.mBinder.generate(key, uid, keyType, keySize, flags, new KeystoreArguments(args)) == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean importKey(String keyName, byte[] key, int uid, int flags) {
        if (isDebugKeystore()) {
            Log.i(TAG, "importKey keyName: " + keyName + ", uid:" + uid + ", flags:" + flags);
        }
        try {
            return this.mBinder.import_key(keyName, key, uid, flags) == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public byte[] sign(String key, byte[] data) {
        if (isDebugKeystore()) {
            Log.i(TAG, "sign key: " + key);
        }
        try {
            return this.mBinder.sign(key, data);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        } catch (ServiceSpecificException e2) {
            Log.w(TAG, "KeyStore exception", e2);
            return null;
        }
    }

    public boolean verify(String key, byte[] data, byte[] signature) {
        byte[] signature2;
        if (isDebugKeystore()) {
            Log.i(TAG, "verify key: " + key);
        }
        if (signature != null) {
            signature2 = signature;
        } else {
            try {
                signature2 = new byte[0];
            } catch (RemoteException e) {
                Log.w(TAG, "Cannot connect to keystore", e);
                return false;
            } catch (ServiceSpecificException e2) {
                Log.w(TAG, "KeyStore exception", e2);
                return false;
            }
        }
        return this.mBinder.verify(key, data, signature2) == 1;
    }

    public String grant(String key, int uid) {
        if (isDebugKeystore()) {
            Log.i(TAG, "grant key: " + key + ", uid:" + uid);
        }
        try {
            String grantAlias = this.mBinder.grant(key, uid);
            if (grantAlias == "") {
                return null;
            }
            return grantAlias;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public boolean ungrant(String key, int uid) {
        if (isDebugKeystore()) {
            Log.i(TAG, "ungrant key: " + key + ", uid:" + uid);
        }
        try {
            return this.mBinder.ungrant(key, uid) == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public long getmtime(String key, int uid) {
        if (isDebugKeystore()) {
            Log.i(TAG, "getmtime key: " + key + ", uid:" + uid);
        }
        try {
            long millis = this.mBinder.getmtime(key, uid);
            if (millis == -1) {
                return -1L;
            }
            return 1000 * millis;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return -1L;
        }
    }

    public long getmtime(String key) {
        return getmtime(key, -1);
    }

    public boolean isHardwareBacked() {
        return isHardwareBacked("RSA");
    }

    public boolean isHardwareBacked(String keyType) {
        if (isDebugKeystore()) {
            Log.i(TAG, "isHardwareBacked keyType: " + keyType);
        }
        try {
            return this.mBinder.is_hardware_backed(keyType.toUpperCase(Locale.US)) == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean clearUid(int uid) {
        if (isDebugKeystore()) {
            Log.i(TAG, "clearUid uid: " + uid);
        }
        try {
            return this.mBinder.clear_uid((long) uid) == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public int getLastError() {
        return this.mError;
    }

    public boolean addRngEntropy(byte[] data, int flags) {
        if (isDebugKeystore()) {
            Log.i(TAG, "addRngEntropy flags: " + flags);
        }
        try {
            return this.mBinder.addRngEntropy(data, flags) == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public int generateKey(String alias, KeymasterArguments args, byte[] entropy, int uid, int flags, KeyCharacteristics outCharacteristics) {
        RemoteException e;
        byte[] entropy2;
        KeymasterArguments args2;
        if (isDebugKeystore()) {
            Log.i(TAG, "generateKey alias: " + alias + ", uid:" + uid + ", flags:" + flags);
        }
        if (entropy != null) {
            entropy2 = entropy;
        } else {
            try {
                entropy2 = new byte[0];
            } catch (RemoteException e2) {
                e = e2;
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            }
        }
        if (args != null) {
            args2 = args;
        } else {
            try {
                args2 = new KeymasterArguments();
            } catch (RemoteException e3) {
                e = e3;
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            }
        }
        try {
            return this.mBinder.generateKey(alias, args2, entropy2, uid, flags, outCharacteristics);
        } catch (RemoteException e4) {
            e = e4;
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public int generateKey(String alias, KeymasterArguments args, byte[] entropy, int flags, KeyCharacteristics outCharacteristics) {
        return generateKey(alias, args, entropy, -1, flags, outCharacteristics);
    }

    public int getKeyCharacteristics(String alias, KeymasterBlob clientId, KeymasterBlob appId, int uid, KeyCharacteristics outCharacteristics) {
        RemoteException e;
        KeymasterBlob clientId2;
        KeymasterBlob appId2;
        if (isDebugKeystore()) {
            Log.i(TAG, "getKeyCharacteristics alias: " + alias + ", uid:" + uid);
        }
        if (clientId != null) {
            clientId2 = clientId;
        } else {
            try {
                clientId2 = new KeymasterBlob(new byte[0]);
            } catch (RemoteException e2) {
                e = e2;
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            }
        }
        if (appId != null) {
            appId2 = appId;
        } else {
            try {
                appId2 = new KeymasterBlob(new byte[0]);
            } catch (RemoteException e3) {
                e = e3;
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            }
        }
        try {
            return this.mBinder.getKeyCharacteristics(alias, clientId2, appId2, uid, outCharacteristics);
        } catch (RemoteException e4) {
            e = e4;
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public int getKeyCharacteristics(String alias, KeymasterBlob clientId, KeymasterBlob appId, KeyCharacteristics outCharacteristics) {
        return getKeyCharacteristics(alias, clientId, appId, -1, outCharacteristics);
    }

    public int importKey(String alias, KeymasterArguments args, int format, byte[] keyData, int uid, int flags, KeyCharacteristics outCharacteristics) {
        String str;
        int i;
        int i2;
        int i3;
        if (isDebugKeystore()) {
            StringBuilder sb = new StringBuilder();
            sb.append("importKey alias: ");
            str = alias;
            sb.append(str);
            sb.append(", uid:");
            i2 = uid;
            sb.append(i2);
            sb.append(", format:");
            i = format;
            sb.append(i);
            sb.append(", flags:");
            i3 = flags;
            sb.append(i3);
            Log.i(TAG, sb.toString());
        } else {
            str = alias;
            i = format;
            i2 = uid;
            i3 = flags;
        }
        try {
            return this.mBinder.importKey(str, args, i, keyData, i2, i3, outCharacteristics);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public int importKey(String alias, KeymasterArguments args, int format, byte[] keyData, int flags, KeyCharacteristics outCharacteristics) {
        return importKey(alias, args, format, keyData, -1, flags, outCharacteristics);
    }

    public int importWrappedKey(String wrappedKeyAlias, byte[] wrappedKey, String wrappingKeyAlias, byte[] maskingKey, KeymasterArguments args, long rootSid, long fingerprintSid, int uid, KeyCharacteristics outCharacteristics) {
        String str;
        long j;
        if (isDebugKeystore()) {
            StringBuilder sb = new StringBuilder();
            sb.append("importWrappedKey wrappedKeyAlias: ");
            str = wrappedKeyAlias;
            sb.append(str);
            sb.append(", uid:");
            sb.append(uid);
            sb.append(", rootSid:");
            j = rootSid;
            sb.append(j);
            sb.append(", fingerprintSid:");
            sb.append(fingerprintSid);
            Log.i(TAG, sb.toString());
        } else {
            str = wrappedKeyAlias;
            j = rootSid;
        }
        try {
            return this.mBinder.importWrappedKey(str, wrappedKey, wrappingKeyAlias, maskingKey, args, j, fingerprintSid, outCharacteristics);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public ExportResult exportKey(String alias, int format, KeymasterBlob clientId, KeymasterBlob appId, int uid) {
        RemoteException e;
        KeymasterBlob clientId2;
        KeymasterBlob appId2;
        if (isDebugKeystore()) {
            Log.i(TAG, "exportKey alias: " + alias + ", uid:" + uid + ", format:" + format);
        }
        if (clientId != null) {
            clientId2 = clientId;
        } else {
            try {
                clientId2 = new KeymasterBlob(new byte[0]);
            } catch (RemoteException e2) {
                e = e2;
                Log.w(TAG, "Cannot connect to keystore", e);
                return null;
            }
        }
        if (appId != null) {
            appId2 = appId;
        } else {
            try {
                appId2 = new KeymasterBlob(new byte[0]);
            } catch (RemoteException e3) {
                e = e3;
                Log.w(TAG, "Cannot connect to keystore", e);
                return null;
            }
        }
        try {
            return this.mBinder.exportKey(alias, format, clientId2, appId2, uid);
        } catch (RemoteException e4) {
            e = e4;
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public ExportResult exportKey(String alias, int format, KeymasterBlob clientId, KeymasterBlob appId) {
        return exportKey(alias, format, clientId, appId, -1);
    }

    public OperationResult begin(String alias, int purpose, boolean pruneable, KeymasterArguments args, byte[] entropy, int uid) {
        RemoteException e;
        KeymasterArguments args2;
        byte[] entropy2;
        if (isDebugKeystore()) {
            Log.i(TAG, "begin alias: " + alias + ", uid:" + uid + ", purpose:" + purpose + ", pruneable:" + pruneable);
        }
        if (args != null) {
            args2 = args;
        } else {
            try {
                args2 = new KeymasterArguments();
            } catch (RemoteException e2) {
                e = e2;
                Log.w(TAG, "Cannot connect to keystore", e);
                return null;
            }
        }
        if (entropy != null) {
            entropy2 = entropy;
        } else {
            try {
                entropy2 = new byte[0];
            } catch (RemoteException e3) {
                e = e3;
                Log.w(TAG, "Cannot connect to keystore", e);
                return null;
            }
        }
        try {
            return this.mBinder.begin(getToken(), alias, purpose, pruneable, args2, entropy2, uid);
        } catch (RemoteException e4) {
            e = e4;
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public OperationResult begin(String alias, int purpose, boolean pruneable, KeymasterArguments args, byte[] entropy) {
        return begin(alias, purpose, pruneable, args != null ? args : new KeymasterArguments(), entropy != null ? entropy : new byte[0], -1);
    }

    public OperationResult update(IBinder token, KeymasterArguments arguments, byte[] input) {
        KeymasterArguments arguments2;
        if (isDebugKeystore()) {
            Log.i(TAG, AccountManager.USER_DATA_EXTRA_UPDATE);
        }
        if (arguments != null) {
            arguments2 = arguments;
        } else {
            try {
                arguments2 = new KeymasterArguments();
            } catch (RemoteException e) {
                Log.w(TAG, "Cannot connect to keystore", e);
                return null;
            }
        }
        return this.mBinder.update(token, arguments2, input != null ? input : new byte[0]);
    }

    public OperationResult finish(IBinder token, KeymasterArguments arguments, byte[] signature, byte[] entropy) {
        KeymasterArguments arguments2;
        if (isDebugKeystore()) {
            Log.i(TAG, "finish");
        }
        if (arguments != null) {
            arguments2 = arguments;
        } else {
            try {
                arguments2 = new KeymasterArguments();
            } catch (RemoteException e) {
                Log.w(TAG, "Cannot connect to keystore", e);
                return null;
            }
        }
        return this.mBinder.finish(token, arguments2, signature != null ? signature : new byte[0], entropy != null ? entropy : new byte[0]);
    }

    public OperationResult finish(IBinder token, KeymasterArguments arguments, byte[] signature) {
        return finish(token, arguments, signature, null);
    }

    public int abort(IBinder token) {
        if (isDebugKeystore()) {
            Log.i(TAG, "abort");
        }
        try {
            return this.mBinder.abort(token);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public boolean isOperationAuthorized(IBinder token) {
        if (isDebugKeystore()) {
            Log.i(TAG, "isOperationAuthorized");
        }
        try {
            return this.mBinder.isOperationAuthorized(token);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public int addAuthToken(byte[] authToken) {
        if (isDebugKeystore()) {
            Log.i(TAG, "addAuthToken");
        }
        try {
            return this.mBinder.addAuthToken(authToken);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public boolean onUserPasswordChanged(int userId, String newPassword) {
        if (isDebugKeystore()) {
            Log.i(TAG, "onUserPasswordChanged userId:" + userId + ", newPassword:" + newPassword);
        }
        if (newPassword == null) {
            newPassword = "";
        }
        try {
            return this.mBinder.onUserPasswordChanged(userId, newPassword) == 1;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public void onUserAdded(int userId, int parentId) {
        if (isDebugKeystore()) {
            Log.i(TAG, "onUserAdded userId:" + userId + ", parentId:" + parentId);
        }
        try {
            this.mBinder.onUserAdded(userId, parentId);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public void onUserAdded(int userId) {
        onUserAdded(userId, -1);
    }

    public void onUserRemoved(int userId) {
        if (isDebugKeystore()) {
            Log.i(TAG, "onUserRemoved userId:" + userId);
        }
        try {
            this.mBinder.onUserRemoved(userId);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public boolean onUserPasswordChanged(String newPassword) {
        return onUserPasswordChanged(UserHandle.getUserId(Process.myUid()), newPassword);
    }

    public int attestKey(String alias, KeymasterArguments params, KeymasterCertificateChain outChain) {
        if (isDebugKeystore()) {
            Log.i(TAG, "attestKey alias:" + alias);
        }
        if (params == null) {
            try {
                params = new KeymasterArguments();
            } catch (RemoteException e) {
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            }
        }
        if (outChain == null) {
            outChain = new KeymasterCertificateChain();
        }
        return this.mBinder.attestKey(alias, params, outChain);
    }

    public int attestDeviceIds(KeymasterArguments params, KeymasterCertificateChain outChain) {
        if (params == null) {
            try {
                params = new KeymasterArguments();
            } catch (RemoteException e) {
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            }
        }
        if (outChain == null) {
            outChain = new KeymasterCertificateChain();
        }
        return this.mBinder.attestDeviceIds(params, outChain);
    }

    public void onDeviceOffBody() {
        if (isDebugKeystore()) {
            Log.i(TAG, "onDeviceOffBody");
        }
        try {
            this.mBinder.onDeviceOffBody();
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public int presentConfirmationPrompt(IBinder listener, String promptText, byte[] extraData, String locale, int uiOptionsAsFlags) {
        if (isDebugKeystore()) {
            Log.i(TAG, "presentConfirmationPrompt promptText:" + promptText + ", locale" + locale + ", uiOptionsAsFlags" + uiOptionsAsFlags);
        }
        try {
            return this.mBinder.presentConfirmationPrompt(listener, promptText, extraData, locale, uiOptionsAsFlags);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 5;
        }
    }

    public int cancelConfirmationPrompt(IBinder listener) {
        if (isDebugKeystore()) {
            Log.i(TAG, "cancelConfirmationPrompt");
        }
        try {
            return this.mBinder.cancelConfirmationPrompt(listener);
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 5;
        }
    }

    public boolean isConfirmationPromptSupported() {
        if (isDebugKeystore()) {
            Log.i(TAG, "isConfirmationPromptSupported");
        }
        try {
            return this.mBinder.isConfirmationPromptSupported();
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public static KeyStoreException getKeyStoreException(int errorCode) {
        if (errorCode > 0) {
            if (errorCode != 15) {
                switch (errorCode) {
                    case 1:
                        return new KeyStoreException(errorCode, "OK");
                    case 2:
                        return new KeyStoreException(errorCode, "User authentication required");
                    case 3:
                        return new KeyStoreException(errorCode, "Keystore not initialized");
                    case 4:
                        return new KeyStoreException(errorCode, "System error");
                    default:
                        switch (errorCode) {
                            case 6:
                                return new KeyStoreException(errorCode, "Permission denied");
                            case 7:
                                return new KeyStoreException(errorCode, "Key not found");
                            case 8:
                                return new KeyStoreException(errorCode, "Key blob corrupted");
                            default:
                                return new KeyStoreException(errorCode, String.valueOf(errorCode));
                        }
                }
            }
            return new KeyStoreException(errorCode, "Operation requires authorization");
        } else if (errorCode == -16) {
            return new KeyStoreException(errorCode, "Invalid user authentication validity duration");
        } else {
            return new KeyStoreException(errorCode, KeymasterDefs.getErrorMessage(errorCode));
        }
    }

    public InvalidKeyException getInvalidKeyException(String keystoreKeyAlias, int uid, KeyStoreException e) {
        int errorCode = e.getErrorCode();
        if (errorCode != 15) {
            switch (errorCode) {
                case -26:
                    break;
                case -25:
                    return new KeyExpiredException();
                case -24:
                    return new KeyNotYetValidException();
                default:
                    switch (errorCode) {
                        case 2:
                            return new UserNotAuthenticatedException();
                        case 3:
                            return new KeyPermanentlyInvalidatedException();
                        default:
                            return new InvalidKeyException("Keystore operation failed", e);
                    }
            }
        }
        KeyCharacteristics keyCharacteristics = new KeyCharacteristics();
        int getKeyCharacteristicsErrorCode = getKeyCharacteristics(keystoreKeyAlias, null, null, uid, keyCharacteristics);
        if (getKeyCharacteristicsErrorCode != 1) {
            return new InvalidKeyException("Failed to obtained key characteristics", getKeyStoreException(getKeyCharacteristicsErrorCode));
        }
        List<BigInteger> keySids = keyCharacteristics.getUnsignedLongs(KeymasterDefs.KM_TAG_USER_SECURE_ID);
        if (keySids.isEmpty()) {
            return new KeyPermanentlyInvalidatedException();
        }
        long rootSid = GateKeeper.getSecureUserId();
        if (rootSid != 0 && keySids.contains(KeymasterArguments.toUint64(rootSid))) {
            return new UserNotAuthenticatedException();
        }
        long fingerprintOnlySid = getFingerprintOnlySid();
        if (fingerprintOnlySid != 0 && keySids.contains(KeymasterArguments.toUint64(fingerprintOnlySid))) {
            return new UserNotAuthenticatedException();
        }
        return new KeyPermanentlyInvalidatedException();
    }

    private long getFingerprintOnlySid() {
        FingerprintManager fingerprintManager;
        PackageManager packageManager = this.mContext.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) && (fingerprintManager = (FingerprintManager) this.mContext.getSystemService(FingerprintManager.class)) != null) {
            return fingerprintManager.getAuthenticatorId();
        }
        return 0L;
    }

    public InvalidKeyException getInvalidKeyException(String keystoreKeyAlias, int uid, int errorCode) {
        return getInvalidKeyException(keystoreKeyAlias, uid, getKeyStoreException(errorCode));
    }

    private boolean isDebugKeystore() {
        return SystemProperties.getBoolean("persist.sys.xpeng.keystoredbg", false);
    }
}
