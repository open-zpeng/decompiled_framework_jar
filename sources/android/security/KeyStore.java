package android.security;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.UserHandle;
import android.security.keymaster.ExportResult;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterBlob;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keymaster.KeymasterDefs;
import android.security.keymaster.OperationResult;
import android.security.keystore.IKeystoreCertificateChainCallback;
import android.security.keystore.IKeystoreExportKeyCallback;
import android.security.keystore.IKeystoreKeyCharacteristicsCallback;
import android.security.keystore.IKeystoreOperationResultCallback;
import android.security.keystore.IKeystoreResponseCallback;
import android.security.keystore.IKeystoreService;
import android.security.keystore.KeyExpiredException;
import android.security.keystore.KeyNotYetValidException;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.security.keystore.KeystoreResponse;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import com.android.org.bouncycastle.asn1.ASN1InputStream;
import com.android.org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.ToIntFunction;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;

/* loaded from: classes2.dex */
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
    public static final int KEY_ALREADY_EXISTS = 16;
    public static final int KEY_NOT_FOUND = 7;
    public static final int KEY_PERMANENTLY_INVALIDATED = 17;
    public static final int LOCKED = 2;
    @UnsupportedAppUsage
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

    /* loaded from: classes2.dex */
    public enum State {
        UNLOCKED,
        LOCKED,
        UNINITIALIZED
    }

    private KeyStore(IKeystoreService binder) {
        this.mBinder = binder;
    }

    @UnsupportedAppUsage
    public static Context getApplicationContext() {
        Application application = ActivityThread.currentApplication();
        if (application == null) {
            throw new IllegalStateException("Failed to obtain application Context from ActivityThread");
        }
        return application;
    }

    @UnsupportedAppUsage
    public static KeyStore getInstance() {
        IKeystoreService keystore = IKeystoreService.Stub.asInterface(ServiceManager.getService("android.security.keystore"));
        return new KeyStore(keystore);
    }

    private synchronized IBinder getToken() {
        if (this.mToken == null) {
            this.mToken = new Binder();
        }
        return this.mToken;
    }

    @UnsupportedAppUsage
    public State state(int userId) {
        try {
            int ret = this.mBinder.getState(userId);
            if (ret != 1) {
                if (ret != 2) {
                    if (ret == 3) {
                        return State.UNINITIALIZED;
                    }
                    throw new AssertionError(this.mError);
                }
                return State.LOCKED;
            }
            return State.UNLOCKED;
        } catch (RemoteException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            throw new AssertionError(e);
        } catch (NullPointerException e2) {
            Log.w(TAG, "state: keystore null ", e2);
            return State.UNINITIALIZED;
        }
    }

    @UnsupportedAppUsage
    public State state() {
        return state(UserHandle.myUserId());
    }

    public boolean isUnlocked() {
        return state() == State.UNLOCKED;
    }

    public byte[] get(String key, int uid) {
        return get(key, uid, false);
    }

    @UnsupportedAppUsage
    public byte[] get(String key) {
        return get(key, -1);
    }

    public byte[] get(String key, int uid, boolean suppressKeyNotFoundWarning) {
        try {
            return this.mBinder.get(key != null ? key : "", uid);
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        } catch (ServiceSpecificException e2) {
            if (!suppressKeyNotFoundWarning || e2.errorCode != 7) {
                Log.w(TAG, "KeyStore exception", e2);
            }
            return null;
        }
    }

    public byte[] get(String key, boolean suppressKeyNotFoundWarning) {
        return get(key, -1, suppressKeyNotFoundWarning);
    }

    public boolean put(String key, byte[] value, int uid, int flags) {
        return insert(key, value, uid, flags) == 1;
    }

    public int insert(String key, byte[] value, int uid, int flags) {
        if (value == null) {
            try {
                value = new byte[0];
            } catch (RemoteException | NullPointerException e) {
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            }
        }
        int error = this.mBinder.insert(key, value, uid, flags);
        if (error == 16) {
            this.mBinder.del(key, uid);
            return this.mBinder.insert(key, value, uid, flags);
        }
        return error;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int delete2(String key, int uid) {
        try {
            return this.mBinder.del(key, uid);
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public boolean delete(String key, int uid) {
        int ret = delete2(key, uid);
        return ret == 1 || ret == 7;
    }

    @UnsupportedAppUsage
    public boolean delete(String key) {
        return delete(key, -1);
    }

    public boolean contains(String key, int uid) {
        try {
            return this.mBinder.exist(key, uid) == 1;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean contains(String key) {
        return contains(key, -1);
    }

    public String[] list(String prefix, int uid) {
        try {
            return this.mBinder.list(prefix, uid);
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        } catch (ServiceSpecificException e2) {
            Log.w(TAG, "KeyStore exception", e2);
            return null;
        }
    }

    @UnsupportedAppUsage
    public int[] listUidsOfAuthBoundKeys() {
        List<String> uidsOut = new ArrayList<>();
        try {
            int rc = this.mBinder.listUidsOfAuthBoundKeys(uidsOut);
            if (rc != 1) {
                Log.w(TAG, String.format("listUidsOfAuthBoundKeys failed with error code %d", Integer.valueOf(rc)));
                return null;
            }
            return uidsOut.stream().mapToInt(new ToIntFunction() { // from class: android.security.-$$Lambda$wddj3-hVVrg0MkscpMtYt3BzY8Y
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return Integer.parseInt((String) obj);
                }
            }).toArray();
        } catch (RemoteException | NullPointerException e) {
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

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public boolean reset() {
        try {
            return this.mBinder.reset() == 1;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean lock(int userId) {
        try {
            return this.mBinder.lock(userId) == 1;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean lock() {
        return lock(UserHandle.myUserId());
    }

    public boolean unlock(int userId, String password) {
        try {
            this.mError = this.mBinder.unlock(userId, password != null ? password : "");
            return this.mError == 1;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean unlock(String password) {
        return unlock(UserHandle.getUserId(Process.myUid()), password);
    }

    public boolean isEmpty(int userId) {
        try {
            return this.mBinder.isEmpty(userId) != 0;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public boolean isEmpty() {
        return isEmpty(UserHandle.myUserId());
    }

    public String grant(String key, int uid) {
        try {
            String grantAlias = this.mBinder.grant(key, uid);
            if (grantAlias == "") {
                return null;
            }
            return grantAlias;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        }
    }

    public boolean ungrant(String key, int uid) {
        try {
            return this.mBinder.ungrant(key, uid) == 1;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public long getmtime(String key, int uid) {
        try {
            long millis = this.mBinder.getmtime(key, uid);
            if (millis == -1) {
                return -1L;
            }
            return 1000 * millis;
        } catch (RemoteException | NullPointerException e) {
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
        try {
            return this.mBinder.is_hardware_backed(keyType.toUpperCase(Locale.US)) == 1;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public boolean clearUid(int uid) {
        try {
            return this.mBinder.clear_uid((long) uid) == 1;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public int getLastError() {
        return this.mError;
    }

    public boolean addRngEntropy(byte[] data, int flags) {
        KeystoreResultPromise promise = new KeystoreResultPromise();
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            int errorCode = this.mBinder.addRngEntropy(promise, data, flags);
            if (errorCode == 1) {
                return promise.getFuture().get().getErrorCode() == 1;
            }
            return false;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        } catch (InterruptedException | ExecutionException e2) {
            Log.e(TAG, "AddRngEntropy completed with exception", e2);
            return false;
        } finally {
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class KeyCharacteristicsCallbackResult {
        private KeyCharacteristics keyCharacteristics;
        private KeystoreResponse keystoreResponse;

        public KeyCharacteristicsCallbackResult(KeystoreResponse keystoreResponse, KeyCharacteristics keyCharacteristics) {
            this.keystoreResponse = keystoreResponse;
            this.keyCharacteristics = keyCharacteristics;
        }

        public KeystoreResponse getKeystoreResponse() {
            return this.keystoreResponse;
        }

        public void setKeystoreResponse(KeystoreResponse keystoreResponse) {
            this.keystoreResponse = keystoreResponse;
        }

        public KeyCharacteristics getKeyCharacteristics() {
            return this.keyCharacteristics;
        }

        public void setKeyCharacteristics(KeyCharacteristics keyCharacteristics) {
            this.keyCharacteristics = keyCharacteristics;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class KeyCharacteristicsPromise extends IKeystoreKeyCharacteristicsCallback.Stub implements IBinder.DeathRecipient {
        private final CompletableFuture<KeyCharacteristicsCallbackResult> future;

        private KeyCharacteristicsPromise() {
            this.future = new CompletableFuture<>();
        }

        @Override // android.security.keystore.IKeystoreKeyCharacteristicsCallback
        public void onFinished(KeystoreResponse keystoreResponse, KeyCharacteristics keyCharacteristics) throws RemoteException {
            this.future.complete(new KeyCharacteristicsCallbackResult(keystoreResponse, keyCharacteristics));
        }

        public final CompletableFuture<KeyCharacteristicsCallbackResult> getFuture() {
            return this.future;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.future.completeExceptionally(new RemoteException("Keystore died"));
        }
    }

    private int generateKeyInternal(String alias, KeymasterArguments args, byte[] entropy, int uid, int flags, KeyCharacteristics outCharacteristics) throws RemoteException, ExecutionException, InterruptedException {
        KeyCharacteristicsPromise promise = new KeyCharacteristicsPromise();
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            int error = this.mBinder.generateKey(promise, alias, args, entropy, uid, flags);
            if (error != 1) {
                Log.e(TAG, "generateKeyInternal failed on request " + error);
                return error;
            }
            KeyCharacteristicsCallbackResult result = promise.getFuture().get();
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
            int error2 = result.getKeystoreResponse().getErrorCode();
            if (error2 != 1) {
                Log.e(TAG, "generateKeyInternal failed on response " + error2);
                return error2;
            }
            KeyCharacteristics characteristics = result.getKeyCharacteristics();
            if (characteristics == null) {
                Log.e(TAG, "generateKeyInternal got empty key cheractariestics " + error2);
                return 4;
            }
            outCharacteristics.shallowCopyFrom(characteristics);
            return 1;
        } finally {
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
        }
    }

    public int generateKey(String alias, KeymasterArguments args, byte[] entropy, int uid, int flags, KeyCharacteristics outCharacteristics) {
        byte[] entropy2;
        KeymasterArguments args2;
        if (entropy != null) {
            entropy2 = entropy;
        } else {
            try {
                entropy2 = new byte[0];
            } catch (RemoteException | NullPointerException e) {
                e = e;
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            } catch (InterruptedException | ExecutionException e2) {
                e = e2;
                Log.e(TAG, "generateKey completed with exception", e);
                return 4;
            }
        }
        if (args != null) {
            args2 = args;
        } else {
            try {
                args2 = new KeymasterArguments();
            } catch (RemoteException | NullPointerException e3) {
                e = e3;
                Log.w(TAG, "Cannot connect to keystore", e);
                return 4;
            } catch (InterruptedException | ExecutionException e4) {
                e = e4;
                Log.e(TAG, "generateKey completed with exception", e);
                return 4;
            }
        }
        try {
            int error = generateKeyInternal(alias, args2, entropy2, uid, flags, outCharacteristics);
            if (error == 16) {
                try {
                    try {
                        this.mBinder.del(alias, uid);
                        return generateKeyInternal(alias, args2, entropy2, uid, flags, outCharacteristics);
                    } catch (RemoteException | NullPointerException e5) {
                        e = e5;
                        Log.w(TAG, "Cannot connect to keystore", e);
                        return 4;
                    } catch (InterruptedException | ExecutionException e6) {
                        e = e6;
                        Log.e(TAG, "generateKey completed with exception", e);
                        return 4;
                    }
                } catch (RemoteException | NullPointerException e7) {
                    e = e7;
                    Log.w(TAG, "Cannot connect to keystore", e);
                    return 4;
                } catch (InterruptedException | ExecutionException e8) {
                    e = e8;
                    Log.e(TAG, "generateKey completed with exception", e);
                    return 4;
                }
            }
            return error;
        } catch (RemoteException | NullPointerException e9) {
            e = e9;
        } catch (InterruptedException | ExecutionException e10) {
            e = e10;
        }
    }

    public int generateKey(String alias, KeymasterArguments args, byte[] entropy, int flags, KeyCharacteristics outCharacteristics) {
        return generateKey(alias, args, entropy, -1, flags, outCharacteristics);
    }

    public int getKeyCharacteristics(String alias, KeymasterBlob clientId, KeymasterBlob appId, int uid, KeyCharacteristics outCharacteristics) {
        Exception e;
        Throwable e2;
        Throwable e3;
        KeymasterBlob clientId2;
        KeymasterBlob appId2;
        KeyCharacteristicsPromise promise = new KeyCharacteristicsPromise();
        try {
            try {
                this.mBinder.asBinder().linkToDeath(promise, 0);
                clientId2 = clientId != null ? clientId : new KeymasterBlob(new byte[0]);
                if (appId != null) {
                    appId2 = appId;
                } else {
                    try {
                        appId2 = new KeymasterBlob(new byte[0]);
                    } catch (RemoteException | NullPointerException e4) {
                        e3 = e4;
                        Log.w(TAG, "Cannot connect to keystore", e3);
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return 4;
                    } catch (InterruptedException | ExecutionException e5) {
                        e2 = e5;
                        Log.e(TAG, "GetKeyCharacteristics completed with exception", e2);
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return 4;
                    }
                }
            } catch (RemoteException | NullPointerException e6) {
                e3 = e6;
            } catch (InterruptedException | ExecutionException e7) {
                e2 = e7;
            } catch (Throwable th) {
                e = th;
            }
            try {
                try {
                    int error = this.mBinder.getKeyCharacteristics(promise, alias, clientId2, appId2, uid);
                    if (error != 1) {
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return error;
                    }
                    KeyCharacteristicsCallbackResult result = promise.getFuture().get();
                    int error2 = result.getKeystoreResponse().getErrorCode();
                    if (error2 != 1) {
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return error2;
                    }
                    KeyCharacteristics characteristics = result.getKeyCharacteristics();
                    if (characteristics == null) {
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return 4;
                    }
                    outCharacteristics.shallowCopyFrom(characteristics);
                    this.mBinder.asBinder().unlinkToDeath(promise, 0);
                    return 1;
                } catch (RemoteException | NullPointerException e8) {
                    e3 = e8;
                    Log.w(TAG, "Cannot connect to keystore", e3);
                    this.mBinder.asBinder().unlinkToDeath(promise, 0);
                    return 4;
                } catch (Throwable th2) {
                    e = th2;
                    this.mBinder.asBinder().unlinkToDeath(promise, 0);
                    throw e;
                }
            } catch (InterruptedException | ExecutionException e9) {
                e2 = e9;
                Log.e(TAG, "GetKeyCharacteristics completed with exception", e2);
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return 4;
            }
        } catch (Throwable th3) {
            e = th3;
        }
    }

    public int getKeyCharacteristics(String alias, KeymasterBlob clientId, KeymasterBlob appId, KeyCharacteristics outCharacteristics) {
        return getKeyCharacteristics(alias, clientId, appId, -1, outCharacteristics);
    }

    private int importKeyInternal(String alias, KeymasterArguments args, int format, byte[] keyData, int uid, int flags, KeyCharacteristics outCharacteristics) throws RemoteException, ExecutionException, InterruptedException {
        KeyCharacteristicsPromise promise = new KeyCharacteristicsPromise();
        this.mBinder.asBinder().linkToDeath(promise, 0);
        try {
            int error = this.mBinder.importKey(promise, alias, args, format, keyData, uid, flags);
            if (error != 1) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return error;
            }
            KeyCharacteristicsCallbackResult result = promise.getFuture().get();
            int error2 = result.getKeystoreResponse().getErrorCode();
            if (error2 != 1) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return error2;
            }
            KeyCharacteristics characteristics = result.getKeyCharacteristics();
            if (characteristics == null) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return 4;
            }
            try {
                outCharacteristics.shallowCopyFrom(characteristics);
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return 1;
            } catch (Throwable th) {
                th = th;
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public int importKey(String alias, KeymasterArguments args, int format, byte[] keyData, int uid, int flags, KeyCharacteristics outCharacteristics) {
        try {
            int error = importKeyInternal(alias, args, format, keyData, uid, flags, outCharacteristics);
            if (error == 16) {
                this.mBinder.del(alias, uid);
                return importKeyInternal(alias, args, format, keyData, uid, flags, outCharacteristics);
            }
            return error;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        } catch (InterruptedException | ExecutionException e2) {
            Log.e(TAG, "ImportKey completed with exception", e2);
            return 4;
        }
    }

    public int importKey(String alias, KeymasterArguments args, int format, byte[] keyData, int flags, KeyCharacteristics outCharacteristics) {
        return importKey(alias, args, format, keyData, -1, flags, outCharacteristics);
    }

    private String getAlgorithmFromPKCS8(byte[] keyData) {
        try {
            ASN1InputStream bIn = new ASN1InputStream(new ByteArrayInputStream(keyData));
            PrivateKeyInfo pki = PrivateKeyInfo.getInstance(bIn.readObject());
            String algOid = pki.getPrivateKeyAlgorithm().getAlgorithm().getId();
            return new AlgorithmId(new ObjectIdentifier(algOid)).getName();
        } catch (IOException e) {
            Log.e(TAG, "getAlgorithmFromPKCS8 Failed to parse key data");
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    private KeymasterArguments makeLegacyArguments(String algorithm) {
        KeymasterArguments args = new KeymasterArguments();
        args.addEnum(KeymasterDefs.KM_TAG_ALGORITHM, KeyProperties.KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(algorithm));
        args.addEnum(KeymasterDefs.KM_TAG_PURPOSE, 2);
        args.addEnum(KeymasterDefs.KM_TAG_PURPOSE, 3);
        args.addEnum(KeymasterDefs.KM_TAG_PURPOSE, 0);
        args.addEnum(KeymasterDefs.KM_TAG_PURPOSE, 1);
        args.addEnum(KeymasterDefs.KM_TAG_PADDING, 1);
        if (algorithm.equalsIgnoreCase("RSA")) {
            args.addEnum(KeymasterDefs.KM_TAG_PADDING, 2);
            args.addEnum(KeymasterDefs.KM_TAG_PADDING, 4);
            args.addEnum(KeymasterDefs.KM_TAG_PADDING, 5);
            args.addEnum(KeymasterDefs.KM_TAG_PADDING, 3);
        }
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 0);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 1);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 2);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 3);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 4);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 5);
        args.addEnum(KeymasterDefs.KM_TAG_DIGEST, 6);
        args.addBoolean(KeymasterDefs.KM_TAG_NO_AUTH_REQUIRED);
        args.addDate(KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, new Date(Long.MAX_VALUE));
        args.addDate(KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, new Date(Long.MAX_VALUE));
        args.addDate(KeymasterDefs.KM_TAG_ACTIVE_DATETIME, new Date(0L));
        return args;
    }

    public boolean importKey(String alias, byte[] keyData, int uid, int flags) {
        String algorithm = getAlgorithmFromPKCS8(keyData);
        if (algorithm == null) {
            return false;
        }
        KeymasterArguments args = makeLegacyArguments(algorithm);
        KeyCharacteristics out = new KeyCharacteristics();
        int result = importKey(alias, args, 1, keyData, uid, flags, out);
        if (result == 1) {
            return true;
        }
        Log.e(TAG, Log.getStackTraceString(new KeyStoreException(result, "legacy key import failed")));
        return false;
    }

    private int importWrappedKeyInternal(String wrappedKeyAlias, byte[] wrappedKey, String wrappingKeyAlias, byte[] maskingKey, KeymasterArguments args, long rootSid, long fingerprintSid, KeyCharacteristics outCharacteristics) throws RemoteException, ExecutionException, InterruptedException {
        KeyCharacteristicsPromise promise = new KeyCharacteristicsPromise();
        this.mBinder.asBinder().linkToDeath(promise, 0);
        try {
            int error = this.mBinder.importWrappedKey(promise, wrappedKeyAlias, wrappedKey, wrappingKeyAlias, maskingKey, args, rootSid, fingerprintSid);
            if (error != 1) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return error;
            }
            KeyCharacteristicsCallbackResult result = promise.getFuture().get();
            int error2 = result.getKeystoreResponse().getErrorCode();
            if (error2 != 1) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return error2;
            }
            KeyCharacteristics characteristics = result.getKeyCharacteristics();
            if (characteristics == null) {
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return 4;
            }
            try {
                outCharacteristics.shallowCopyFrom(characteristics);
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return 1;
            } catch (Throwable th) {
                th = th;
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public int importWrappedKey(String wrappedKeyAlias, byte[] wrappedKey, String wrappingKeyAlias, byte[] maskingKey, KeymasterArguments args, long rootSid, long fingerprintSid, int uid, KeyCharacteristics outCharacteristics) {
        try {
            int error = importWrappedKeyInternal(wrappedKeyAlias, wrappedKey, wrappingKeyAlias, maskingKey, args, rootSid, fingerprintSid, outCharacteristics);
            if (error == 16) {
                try {
                    try {
                        this.mBinder.del(wrappedKeyAlias, -1);
                        return importWrappedKeyInternal(wrappedKeyAlias, wrappedKey, wrappingKeyAlias, maskingKey, args, rootSid, fingerprintSid, outCharacteristics);
                    } catch (RemoteException | NullPointerException e) {
                        e = e;
                        Log.w(TAG, "Cannot connect to keystore", e);
                        return 4;
                    } catch (InterruptedException | ExecutionException e2) {
                        e = e2;
                        Log.e(TAG, "ImportWrappedKey completed with exception", e);
                        return 4;
                    }
                } catch (RemoteException | NullPointerException e3) {
                    e = e3;
                    Log.w(TAG, "Cannot connect to keystore", e);
                    return 4;
                } catch (InterruptedException | ExecutionException e4) {
                    e = e4;
                    Log.e(TAG, "ImportWrappedKey completed with exception", e);
                    return 4;
                }
            }
            return error;
        } catch (RemoteException | NullPointerException e5) {
            e = e5;
        } catch (InterruptedException | ExecutionException e6) {
            e = e6;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ExportKeyPromise extends IKeystoreExportKeyCallback.Stub implements IBinder.DeathRecipient {
        private final CompletableFuture<ExportResult> future;

        private ExportKeyPromise() {
            this.future = new CompletableFuture<>();
        }

        @Override // android.security.keystore.IKeystoreExportKeyCallback
        public void onFinished(ExportResult exportKeyResult) throws RemoteException {
            this.future.complete(exportKeyResult);
        }

        public final CompletableFuture<ExportResult> getFuture() {
            return this.future;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.future.completeExceptionally(new RemoteException("Keystore died"));
        }
    }

    public ExportResult exportKey(String alias, int format, KeymasterBlob clientId, KeymasterBlob appId, int uid) {
        KeymasterBlob clientId2;
        KeymasterBlob appId2;
        ExportKeyPromise promise = new ExportKeyPromise();
        try {
            try {
                this.mBinder.asBinder().linkToDeath(promise, 0);
                clientId2 = clientId != null ? clientId : new KeymasterBlob(new byte[0]);
                if (appId != null) {
                    appId2 = appId;
                } else {
                    try {
                        appId2 = new KeymasterBlob(new byte[0]);
                    } catch (RemoteException | NullPointerException e) {
                        e = e;
                        Log.w(TAG, "Cannot connect to keystore", e);
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return null;
                    } catch (InterruptedException | ExecutionException e2) {
                        e = e2;
                        Log.e(TAG, "ExportKey completed with exception", e);
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return null;
                    } catch (Throwable th) {
                        e = th;
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        throw e;
                    }
                }
            } catch (RemoteException | NullPointerException e3) {
                e = e3;
            } catch (InterruptedException | ExecutionException e4) {
                e = e4;
            } catch (Throwable th2) {
                e = th2;
            }
            try {
                try {
                    int error = this.mBinder.exportKey(promise, alias, format, clientId2, appId2, uid);
                    if (error == 1) {
                        ExportResult exportResult = promise.getFuture().get();
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return exportResult;
                    }
                    ExportResult exportResult2 = new ExportResult(error);
                    this.mBinder.asBinder().unlinkToDeath(promise, 0);
                    return exportResult2;
                } catch (InterruptedException | ExecutionException e5) {
                    e = e5;
                    Log.e(TAG, "ExportKey completed with exception", e);
                    this.mBinder.asBinder().unlinkToDeath(promise, 0);
                    return null;
                }
            } catch (RemoteException | NullPointerException e6) {
                e = e6;
                Log.w(TAG, "Cannot connect to keystore", e);
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return null;
            }
        } catch (Throwable th3) {
            e = th3;
        }
    }

    public ExportResult exportKey(String alias, int format, KeymasterBlob clientId, KeymasterBlob appId) {
        return exportKey(alias, format, clientId, appId, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class OperationPromise extends IKeystoreOperationResultCallback.Stub implements IBinder.DeathRecipient {
        private final CompletableFuture<OperationResult> future;

        private OperationPromise() {
            this.future = new CompletableFuture<>();
        }

        @Override // android.security.keystore.IKeystoreOperationResultCallback
        public void onFinished(OperationResult operationResult) throws RemoteException {
            this.future.complete(operationResult);
        }

        public final CompletableFuture<OperationResult> getFuture() {
            return this.future;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.future.completeExceptionally(new RemoteException("Keystore died"));
        }
    }

    public OperationResult begin(String alias, int purpose, boolean pruneable, KeymasterArguments args, byte[] entropy, int uid) {
        KeymasterArguments args2;
        byte[] entropy2;
        OperationPromise promise = new OperationPromise();
        try {
            try {
                this.mBinder.asBinder().linkToDeath(promise, 0);
                args2 = args != null ? args : new KeymasterArguments();
                if (entropy != null) {
                    entropy2 = entropy;
                } else {
                    try {
                        entropy2 = new byte[0];
                    } catch (RemoteException | NullPointerException e) {
                        e = e;
                        Log.w(TAG, "Cannot connect to keystore", e);
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return null;
                    } catch (InterruptedException | ExecutionException e2) {
                        e = e2;
                        Log.e(TAG, "Begin completed with exception", e);
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return null;
                    } catch (Throwable th) {
                        e = th;
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        throw e;
                    }
                }
            } catch (RemoteException | NullPointerException e3) {
                e = e3;
            } catch (InterruptedException | ExecutionException e4) {
                e = e4;
            } catch (Throwable th2) {
                e = th2;
            }
            try {
                try {
                    int errorCode = this.mBinder.begin(promise, getToken(), alias, purpose, pruneable, args2, entropy2, uid);
                    if (errorCode == 1) {
                        OperationResult operationResult = promise.getFuture().get();
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return operationResult;
                    }
                    OperationResult operationResult2 = new OperationResult(errorCode);
                    this.mBinder.asBinder().unlinkToDeath(promise, 0);
                    return operationResult2;
                } catch (InterruptedException | ExecutionException e5) {
                    e = e5;
                    Log.e(TAG, "Begin completed with exception", e);
                    this.mBinder.asBinder().unlinkToDeath(promise, 0);
                    return null;
                }
            } catch (RemoteException | NullPointerException e6) {
                e = e6;
                Log.w(TAG, "Cannot connect to keystore", e);
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return null;
            }
        } catch (Throwable th3) {
            e = th3;
        }
    }

    public OperationResult begin(String alias, int purpose, boolean pruneable, KeymasterArguments args, byte[] entropy) {
        return begin(alias, purpose, pruneable, args != null ? args : new KeymasterArguments(), entropy != null ? entropy : new byte[0], -1);
    }

    public OperationResult update(IBinder token, KeymasterArguments arguments, byte[] input) {
        OperationPromise promise = new OperationPromise();
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            int errorCode = this.mBinder.update(promise, token, arguments != null ? arguments : new KeymasterArguments(), input != null ? input : new byte[0]);
            return errorCode == 1 ? promise.getFuture().get() : new OperationResult(errorCode);
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return null;
        } catch (InterruptedException | ExecutionException e2) {
            Log.e(TAG, "Update completed with exception", e2);
            return null;
        } finally {
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
        }
    }

    public OperationResult finish(IBinder token, KeymasterArguments arguments, byte[] signature, byte[] entropy) {
        Exception e;
        Throwable e2;
        Throwable e3;
        KeymasterArguments arguments2;
        byte[] entropy2;
        byte[] signature2;
        OperationPromise promise = new OperationPromise();
        try {
            try {
                this.mBinder.asBinder().linkToDeath(promise, 0);
                arguments2 = arguments != null ? arguments : new KeymasterArguments();
                if (entropy != null) {
                    entropy2 = entropy;
                } else {
                    try {
                        entropy2 = new byte[0];
                    } catch (RemoteException | NullPointerException e4) {
                        e3 = e4;
                        Log.w(TAG, "Cannot connect to keystore", e3);
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return null;
                    } catch (InterruptedException | ExecutionException e5) {
                        e2 = e5;
                        Log.e(TAG, "Finish completed with exception", e2);
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return null;
                    }
                }
                if (signature != null) {
                    signature2 = signature;
                } else {
                    try {
                        signature2 = new byte[0];
                    } catch (RemoteException | NullPointerException e6) {
                        e3 = e6;
                        Log.w(TAG, "Cannot connect to keystore", e3);
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return null;
                    } catch (InterruptedException | ExecutionException e7) {
                        e2 = e7;
                        Log.e(TAG, "Finish completed with exception", e2);
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return null;
                    } catch (Throwable th) {
                        e = th;
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        throw e;
                    }
                }
            } catch (RemoteException | NullPointerException e8) {
                e3 = e8;
            } catch (InterruptedException | ExecutionException e9) {
                e2 = e9;
            } catch (Throwable th2) {
                e = th2;
            }
            try {
                try {
                    int errorCode = this.mBinder.finish(promise, token, arguments2, signature2, entropy2);
                    if (errorCode == 1) {
                        OperationResult operationResult = promise.getFuture().get();
                        this.mBinder.asBinder().unlinkToDeath(promise, 0);
                        return operationResult;
                    }
                    OperationResult operationResult2 = new OperationResult(errorCode);
                    this.mBinder.asBinder().unlinkToDeath(promise, 0);
                    return operationResult2;
                } catch (RemoteException | NullPointerException e10) {
                    e3 = e10;
                    Log.w(TAG, "Cannot connect to keystore", e3);
                    this.mBinder.asBinder().unlinkToDeath(promise, 0);
                    return null;
                } catch (Throwable th3) {
                    e = th3;
                    this.mBinder.asBinder().unlinkToDeath(promise, 0);
                    throw e;
                }
            } catch (InterruptedException | ExecutionException e11) {
                e2 = e11;
                Log.e(TAG, "Finish completed with exception", e2);
                this.mBinder.asBinder().unlinkToDeath(promise, 0);
                return null;
            }
        } catch (Throwable th4) {
            e = th4;
        }
    }

    public OperationResult finish(IBinder token, KeymasterArguments arguments, byte[] signature) {
        return finish(token, arguments, signature, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class KeystoreResultPromise extends IKeystoreResponseCallback.Stub implements IBinder.DeathRecipient {
        private final CompletableFuture<KeystoreResponse> future;

        private KeystoreResultPromise() {
            this.future = new CompletableFuture<>();
        }

        @Override // android.security.keystore.IKeystoreResponseCallback
        public void onFinished(KeystoreResponse keystoreResponse) throws RemoteException {
            this.future.complete(keystoreResponse);
        }

        public final CompletableFuture<KeystoreResponse> getFuture() {
            return this.future;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.future.completeExceptionally(new RemoteException("Keystore died"));
        }
    }

    public int abort(IBinder token) {
        KeystoreResultPromise promise = new KeystoreResultPromise();
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            int errorCode = this.mBinder.abort(promise, token);
            return errorCode == 1 ? promise.getFuture().get().getErrorCode() : errorCode;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        } catch (InterruptedException | ExecutionException e2) {
            Log.e(TAG, "Abort completed with exception", e2);
            return 4;
        } finally {
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
        }
    }

    public int addAuthToken(byte[] authToken) {
        try {
            return this.mBinder.addAuthToken(authToken);
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        }
    }

    public boolean onUserPasswordChanged(int userId, String newPassword) {
        if (newPassword == null) {
            newPassword = "";
        }
        try {
            return this.mBinder.onUserPasswordChanged(userId, newPassword) == 1;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    public void onUserAdded(int userId, int parentId) {
        try {
            this.mBinder.onUserAdded(userId, parentId);
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public void onUserAdded(int userId) {
        onUserAdded(userId, -1);
    }

    public void onUserRemoved(int userId) {
        try {
            this.mBinder.onUserRemoved(userId);
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public boolean onUserPasswordChanged(String newPassword) {
        return onUserPasswordChanged(UserHandle.getUserId(Process.myUid()), newPassword);
    }

    public void onUserLockedStateChanged(int userHandle, boolean locked) {
        try {
            this.mBinder.onKeyguardVisibilityChanged(locked, userHandle);
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to update user locked state " + userHandle, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class KeyAttestationCallbackResult {
        private KeymasterCertificateChain certificateChain;
        private KeystoreResponse keystoreResponse;

        public KeyAttestationCallbackResult(KeystoreResponse keystoreResponse, KeymasterCertificateChain certificateChain) {
            this.keystoreResponse = keystoreResponse;
            this.certificateChain = certificateChain;
        }

        public KeystoreResponse getKeystoreResponse() {
            return this.keystoreResponse;
        }

        public void setKeystoreResponse(KeystoreResponse keystoreResponse) {
            this.keystoreResponse = keystoreResponse;
        }

        public KeymasterCertificateChain getCertificateChain() {
            return this.certificateChain;
        }

        public void setCertificateChain(KeymasterCertificateChain certificateChain) {
            this.certificateChain = certificateChain;
        }
    }

    /* loaded from: classes2.dex */
    private class CertificateChainPromise extends IKeystoreCertificateChainCallback.Stub implements IBinder.DeathRecipient {
        private final CompletableFuture<KeyAttestationCallbackResult> future;

        private CertificateChainPromise() {
            this.future = new CompletableFuture<>();
        }

        @Override // android.security.keystore.IKeystoreCertificateChainCallback
        public void onFinished(KeystoreResponse keystoreResponse, KeymasterCertificateChain certificateChain) throws RemoteException {
            this.future.complete(new KeyAttestationCallbackResult(keystoreResponse, certificateChain));
        }

        public final CompletableFuture<KeyAttestationCallbackResult> getFuture() {
            return this.future;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.future.completeExceptionally(new RemoteException("Keystore died"));
        }
    }

    public int attestKey(String alias, KeymasterArguments params, KeymasterCertificateChain outChain) {
        CertificateChainPromise promise = new CertificateChainPromise();
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            if (params == null) {
                params = new KeymasterArguments();
            }
            if (outChain == null) {
                outChain = new KeymasterCertificateChain();
            }
            int error = this.mBinder.attestKey(promise, alias, params);
            if (error != 1) {
                return error;
            }
            KeyAttestationCallbackResult result = promise.getFuture().get();
            int error2 = result.getKeystoreResponse().getErrorCode();
            if (error2 == 1) {
                outChain.shallowCopyFrom(result.getCertificateChain());
            }
            return error2;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        } catch (InterruptedException | ExecutionException e2) {
            Log.e(TAG, "AttestKey completed with exception", e2);
            return 4;
        } finally {
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
        }
    }

    public int attestDeviceIds(KeymasterArguments params, KeymasterCertificateChain outChain) {
        CertificateChainPromise promise = new CertificateChainPromise();
        try {
            this.mBinder.asBinder().linkToDeath(promise, 0);
            if (params == null) {
                params = new KeymasterArguments();
            }
            if (outChain == null) {
                outChain = new KeymasterCertificateChain();
            }
            int error = this.mBinder.attestDeviceIds(promise, params);
            if (error != 1) {
                return error;
            }
            KeyAttestationCallbackResult result = promise.getFuture().get();
            int error2 = result.getKeystoreResponse().getErrorCode();
            if (error2 == 1) {
                outChain.shallowCopyFrom(result.getCertificateChain());
            }
            return error2;
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 4;
        } catch (InterruptedException | ExecutionException e2) {
            Log.e(TAG, "AttestDevicdeIds completed with exception", e2);
            return 4;
        } finally {
            this.mBinder.asBinder().unlinkToDeath(promise, 0);
        }
    }

    public void onDeviceOffBody() {
        try {
            this.mBinder.onDeviceOffBody();
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
        }
    }

    public int presentConfirmationPrompt(IBinder listener, String promptText, byte[] extraData, String locale, int uiOptionsAsFlags) {
        try {
            return this.mBinder.presentConfirmationPrompt(listener, promptText, extraData, locale, uiOptionsAsFlags);
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 5;
        }
    }

    public int cancelConfirmationPrompt(IBinder listener) {
        try {
            return this.mBinder.cancelConfirmationPrompt(listener);
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return 5;
        }
    }

    public boolean isConfirmationPromptSupported() {
        try {
            return this.mBinder.isConfirmationPromptSupported();
        } catch (RemoteException | NullPointerException e) {
            Log.w(TAG, "Cannot connect to keystore", e);
            return false;
        }
    }

    @UnsupportedAppUsage
    public static KeyStoreException getKeyStoreException(int errorCode) {
        if (errorCode > 0) {
            if (errorCode != 1) {
                if (errorCode != 2) {
                    if (errorCode != 3) {
                        if (errorCode != 4) {
                            if (errorCode != 6) {
                                if (errorCode != 7) {
                                    if (errorCode != 8) {
                                        if (errorCode != 15) {
                                            if (errorCode == 17) {
                                                return new KeyStoreException(errorCode, "Key permanently invalidated");
                                            }
                                            return new KeyStoreException(errorCode, String.valueOf(errorCode));
                                        }
                                        return new KeyStoreException(errorCode, "Operation requires authorization");
                                    }
                                    return new KeyStoreException(errorCode, "Key blob corrupted");
                                }
                                return new KeyStoreException(errorCode, "Key not found");
                            }
                            return new KeyStoreException(errorCode, "Permission denied");
                        }
                        return new KeyStoreException(errorCode, "System error");
                    }
                    return new KeyStoreException(errorCode, "Keystore not initialized");
                }
                return new KeyStoreException(errorCode, "User authentication required");
            }
            return new KeyStoreException(errorCode, "OK");
        } else if (errorCode == -16) {
            return new KeyStoreException(errorCode, "Invalid user authentication validity duration");
        } else {
            return new KeyStoreException(errorCode, KeymasterDefs.getErrorMessage(errorCode));
        }
    }

    public InvalidKeyException getInvalidKeyException(String keystoreKeyAlias, int uid, KeyStoreException e) {
        int errorCode = e.getErrorCode();
        if (errorCode != 2) {
            if (errorCode != 3) {
                if (errorCode != 15) {
                    switch (errorCode) {
                        case -26:
                            break;
                        case -25:
                            return new KeyExpiredException();
                        case -24:
                            return new KeyNotYetValidException();
                        default:
                            return new InvalidKeyException("Keystore operation failed", e);
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
                long faceOnlySid = getFaceOnlySid();
                if (faceOnlySid != 0 && keySids.contains(KeymasterArguments.toUint64(faceOnlySid))) {
                    return new UserNotAuthenticatedException();
                }
                return new KeyPermanentlyInvalidatedException();
            }
            return new KeyPermanentlyInvalidatedException();
        }
        return new UserNotAuthenticatedException();
    }

    private long getFaceOnlySid() {
        FaceManager faceManager;
        PackageManager packageManager = this.mContext.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_FACE) && (faceManager = (FaceManager) this.mContext.getSystemService(FaceManager.class)) != null) {
            return faceManager.getAuthenticatorId();
        }
        return 0L;
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
}
