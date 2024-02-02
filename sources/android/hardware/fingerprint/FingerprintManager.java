package android.hardware.fingerprint;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.BiometricFingerprintConstants;
import android.hardware.biometrics.IBiometricPromptReceiver;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback;
import android.hardware.fingerprint.IFingerprintServiceReceiver;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Slog;
import android.view.SurfaceView;
import com.android.internal.R;
import java.security.Signature;
import java.util.List;
import java.util.concurrent.Executor;
import javax.crypto.Cipher;
import javax.crypto.Mac;
@Deprecated
/* loaded from: classes.dex */
public class FingerprintManager implements BiometricFingerprintConstants {
    private static final boolean DEBUG = true;
    private static final int MSG_ACQUIRED = 101;
    private static final int MSG_AUTHENTICATION_FAILED = 103;
    private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;
    private static final int MSG_ENROLL_RESULT = 100;
    private static final int MSG_ENUMERATED = 106;
    private static final int MSG_ERROR = 104;
    private static final int MSG_REMOVED = 105;
    private static final String TAG = "FingerprintManager";
    private BiometricAuthenticator.AuthenticationCallback mAuthenticationCallback;
    private Context mContext;
    private android.hardware.biometrics.CryptoObject mCryptoObject;
    private EnrollmentCallback mEnrollmentCallback;
    private EnumerateCallback mEnumerateCallback;
    private Executor mExecutor;
    private Handler mHandler;
    private RemovalCallback mRemovalCallback;
    private Fingerprint mRemovalFingerprint;
    private IFingerprintService mService;
    private IBinder mToken = new Binder();
    private IFingerprintServiceReceiver mServiceReceiver = new AnonymousClass2();

    /* loaded from: classes.dex */
    private class OnEnrollCancelListener implements CancellationSignal.OnCancelListener {
        private OnEnrollCancelListener() {
        }

        /* synthetic */ OnEnrollCancelListener(FingerprintManager x0, AnonymousClass1 x1) {
            this();
        }

        @Override // android.os.CancellationSignal.OnCancelListener
        public void onCancel() {
            FingerprintManager.this.cancelEnrollment();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class OnAuthenticationCancelListener implements CancellationSignal.OnCancelListener {
        private android.hardware.biometrics.CryptoObject mCrypto;

        public OnAuthenticationCancelListener(android.hardware.biometrics.CryptoObject crypto) {
            this.mCrypto = crypto;
        }

        @Override // android.os.CancellationSignal.OnCancelListener
        public void onCancel() {
            FingerprintManager.this.cancelAuthentication(this.mCrypto);
        }
    }

    @Deprecated
    /* loaded from: classes.dex */
    public static final class CryptoObject extends android.hardware.biometrics.CryptoObject {
        public CryptoObject(Signature signature) {
            super(signature);
        }

        public CryptoObject(Cipher cipher) {
            super(cipher);
        }

        public CryptoObject(Mac mac) {
            super(mac);
        }

        @Override // android.hardware.biometrics.CryptoObject
        public Signature getSignature() {
            return super.getSignature();
        }

        @Override // android.hardware.biometrics.CryptoObject
        public Cipher getCipher() {
            return super.getCipher();
        }

        @Override // android.hardware.biometrics.CryptoObject
        public Mac getMac() {
            return super.getMac();
        }
    }

    @Deprecated
    /* loaded from: classes.dex */
    public static class AuthenticationResult {
        private CryptoObject mCryptoObject;
        private Fingerprint mFingerprint;
        private int mUserId;

        public synchronized AuthenticationResult(CryptoObject crypto, Fingerprint fingerprint, int userId) {
            this.mCryptoObject = crypto;
            this.mFingerprint = fingerprint;
            this.mUserId = userId;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }

        private protected Fingerprint getFingerprint() {
            return this.mFingerprint;
        }

        public synchronized int getUserId() {
            return this.mUserId;
        }
    }

    @Deprecated
    /* loaded from: classes.dex */
    public static abstract class AuthenticationCallback extends BiometricAuthenticator.AuthenticationCallback {
        @Override // android.hardware.biometrics.BiometricAuthenticator.AuthenticationCallback
        public void onAuthenticationError(int errorCode, CharSequence errString) {
        }

        @Override // android.hardware.biometrics.BiometricAuthenticator.AuthenticationCallback
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult result) {
        }

        @Override // android.hardware.biometrics.BiometricAuthenticator.AuthenticationCallback
        public void onAuthenticationFailed() {
        }

        @Override // android.hardware.biometrics.BiometricAuthenticator.AuthenticationCallback
        public synchronized void onAuthenticationAcquired(int acquireInfo) {
        }

        @Override // android.hardware.biometrics.BiometricAuthenticator.AuthenticationCallback
        public synchronized void onAuthenticationSucceeded(BiometricAuthenticator.AuthenticationResult result) {
            onAuthenticationSucceeded(new AuthenticationResult((CryptoObject) result.getCryptoObject(), (Fingerprint) result.getId(), result.getUserId()));
        }
    }

    /* loaded from: classes.dex */
    public static abstract class EnrollmentCallback {
        public synchronized void onEnrollmentError(int errMsgId, CharSequence errString) {
        }

        public synchronized void onEnrollmentHelp(int helpMsgId, CharSequence helpString) {
        }

        public synchronized void onEnrollmentProgress(int remaining) {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class RemovalCallback {
        public synchronized void onRemovalError(Fingerprint fp, int errMsgId, CharSequence errString) {
        }

        public synchronized void onRemovalSucceeded(Fingerprint fp, int remaining) {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class EnumerateCallback {
        public synchronized void onEnumerateError(int errMsgId, CharSequence errString) {
        }

        public synchronized void onEnumerate(Fingerprint fingerprint) {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class LockoutResetCallback {
        public synchronized void onLockoutReset() {
        }
    }

    @Deprecated
    public void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler) {
        authenticate(crypto, cancel, flags, callback, handler, this.mContext.getUserId());
    }

    private synchronized void useHandler(Handler handler) {
        if (handler != null) {
            this.mHandler = new MyHandler(this, handler.getLooper(), (AnonymousClass1) null);
        } else if (this.mHandler.getLooper() != this.mContext.getMainLooper()) {
            this.mHandler = new MyHandler(this, this.mContext.getMainLooper(), (AnonymousClass1) null);
        }
    }

    public synchronized void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler, int userId) {
        if (callback == null) {
            throw new IllegalArgumentException("Must supply an authentication callback");
        }
        if (cancel != null) {
            if (cancel.isCanceled()) {
                Slog.w(TAG, "authentication already canceled");
                return;
            }
            cancel.setOnCancelListener(new OnAuthenticationCancelListener(crypto));
        }
        if (this.mService != null) {
            try {
                useHandler(handler);
                this.mAuthenticationCallback = callback;
                this.mCryptoObject = crypto;
                long sessionId = crypto != null ? crypto.getOpId() : 0L;
                this.mService.authenticate(this.mToken, sessionId, userId, this.mServiceReceiver, flags, this.mContext.getOpPackageName(), null, null);
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception while authenticating: ", e);
                if (callback != null) {
                    callback.onAuthenticationError(1, getErrorString(1, 0));
                }
            }
        }
    }

    private synchronized void authenticate(int userId, android.hardware.biometrics.CryptoObject crypto, CancellationSignal cancel, Bundle bundle, Executor executor, IBiometricPromptReceiver receiver, final BiometricAuthenticator.AuthenticationCallback callback) {
        this.mCryptoObject = crypto;
        if (cancel.isCanceled()) {
            Slog.w(TAG, "authentication already canceled");
            return;
        }
        cancel.setOnCancelListener(new OnAuthenticationCancelListener(crypto));
        if (this.mService != null) {
            try {
                this.mExecutor = executor;
                this.mAuthenticationCallback = callback;
                long sessionId = crypto != null ? crypto.getOpId() : 0L;
                this.mService.authenticate(this.mToken, sessionId, userId, this.mServiceReceiver, 0, this.mContext.getOpPackageName(), bundle, receiver);
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception while authenticating", e);
                this.mExecutor.execute(new Runnable() { // from class: android.hardware.fingerprint.-$$Lambda$FingerprintManager$0Q_OnkqSSy_nQ9iUWqvqVi6QjNE
                    @Override // java.lang.Runnable
                    public final void run() {
                        callback.onAuthenticationError(1, FingerprintManager.this.getErrorString(1, 0));
                    }
                });
            }
        }
    }

    public synchronized void authenticate(CancellationSignal cancel, Bundle bundle, Executor executor, IBiometricPromptReceiver receiver, BiometricAuthenticator.AuthenticationCallback callback) {
        if (cancel == null) {
            throw new IllegalArgumentException("Must supply a cancellation signal");
        }
        if (bundle == null) {
            throw new IllegalArgumentException("Must supply a bundle");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Must supply an executor");
        }
        if (receiver == null) {
            throw new IllegalArgumentException("Must supply a receiver");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Must supply a calback");
        }
        authenticate(this.mContext.getUserId(), null, cancel, bundle, executor, receiver, callback);
    }

    public synchronized void authenticate(android.hardware.biometrics.CryptoObject crypto, CancellationSignal cancel, Bundle bundle, Executor executor, IBiometricPromptReceiver receiver, BiometricAuthenticator.AuthenticationCallback callback) {
        if (crypto == null) {
            throw new IllegalArgumentException("Must supply a crypto object");
        }
        if (cancel == null) {
            throw new IllegalArgumentException("Must supply a cancellation signal");
        }
        if (bundle == null) {
            throw new IllegalArgumentException("Must supply a bundle");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Must supply an executor");
        }
        if (receiver == null) {
            throw new IllegalArgumentException("Must supply a receiver");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Must supply a callback");
        }
        authenticate(this.mContext.getUserId(), crypto, cancel, bundle, executor, receiver, callback);
    }

    public synchronized void enroll(byte[] token, CancellationSignal cancel, int flags, int userId, EnrollmentCallback callback) {
        if (userId == -2) {
            userId = getCurrentUserId();
        }
        if (callback == null) {
            throw new IllegalArgumentException("Must supply an enrollment callback");
        }
        if (cancel != null) {
            if (cancel.isCanceled()) {
                Slog.w(TAG, "enrollment already canceled");
                return;
            }
            cancel.setOnCancelListener(new OnEnrollCancelListener(this, null));
        }
        if (this.mService != null) {
            try {
                this.mEnrollmentCallback = callback;
                this.mService.enroll(this.mToken, token, userId, this.mServiceReceiver, flags, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in enroll: ", e);
                if (callback != null) {
                    callback.onEnrollmentError(1, getErrorString(1, 0));
                }
            }
        }
    }

    public synchronized long preEnroll() {
        if (this.mService == null) {
            return 0L;
        }
        try {
            long result = this.mService.preEnroll(this.mToken);
            return result;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized int postEnroll() {
        if (this.mService == null) {
            return 0;
        }
        try {
            int result = this.mService.postEnroll(this.mToken);
            return result;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void setActiveUser(int userId) {
        if (this.mService != null) {
            try {
                this.mService.setActiveUser(userId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public synchronized void remove(Fingerprint fp, int userId, RemovalCallback callback) {
        if (this.mService != null) {
            try {
                this.mRemovalCallback = callback;
                this.mRemovalFingerprint = fp;
                this.mService.remove(this.mToken, fp.getFingerId(), fp.getGroupId(), userId, this.mServiceReceiver);
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in remove: ", e);
                if (callback != null) {
                    callback.onRemovalError(fp, 1, getErrorString(1, 0));
                }
            }
        }
    }

    public synchronized void enumerate(int userId, EnumerateCallback callback) {
        if (this.mService != null) {
            try {
                this.mEnumerateCallback = callback;
                this.mService.enumerate(this.mToken, userId, this.mServiceReceiver);
            } catch (RemoteException e) {
                Slog.w(TAG, "Remote exception in enumerate: ", e);
                if (callback != null) {
                    callback.onEnumerateError(1, getErrorString(1, 0));
                }
            }
        }
    }

    public synchronized void rename(int fpId, int userId, String newName) {
        if (this.mService != null) {
            try {
                this.mService.rename(fpId, userId, newName);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Slog.w(TAG, "rename(): Service not connected!");
    }

    private protected List<Fingerprint> getEnrolledFingerprints(int userId) {
        if (this.mService != null) {
            try {
                return this.mService.getEnrolledFingerprints(userId, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return null;
    }

    private protected List<Fingerprint> getEnrolledFingerprints() {
        return getEnrolledFingerprints(this.mContext.getUserId());
    }

    @Deprecated
    public boolean hasEnrolledFingerprints() {
        if (this.mService != null) {
            try {
                return this.mService.hasEnrolledFingerprints(this.mContext.getUserId(), this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return false;
    }

    public synchronized boolean hasEnrolledFingerprints(int userId) {
        if (this.mService != null) {
            try {
                return this.mService.hasEnrolledFingerprints(userId, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return false;
    }

    @Deprecated
    public boolean isHardwareDetected() {
        if (this.mService != null) {
            try {
                return this.mService.isHardwareDetected(0L, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Slog.w(TAG, "isFingerprintHardwareDetected(): Service not connected!");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getAuthenticatorId() {
        if (this.mService != null) {
            try {
                return this.mService.getAuthenticatorId(this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Slog.w(TAG, "getAuthenticatorId(): Service not connected!");
        return 0L;
    }

    public synchronized void resetTimeout(byte[] token) {
        if (this.mService != null) {
            try {
                this.mService.resetTimeout(token);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Slog.w(TAG, "resetTimeout(): Service not connected!");
    }

    public synchronized void addLockoutResetCallback(LockoutResetCallback callback) {
        if (this.mService != null) {
            try {
                PowerManager powerManager = (PowerManager) this.mContext.getSystemService(PowerManager.class);
                this.mService.addLockoutResetCallback(new AnonymousClass1(powerManager, callback));
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        Slog.w(TAG, "addLockoutResetCallback(): Service not connected!");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.hardware.fingerprint.FingerprintManager$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IFingerprintServiceLockoutResetCallback.Stub {
        final /* synthetic */ LockoutResetCallback val$callback;
        final /* synthetic */ PowerManager val$powerManager;

        AnonymousClass1(PowerManager powerManager, LockoutResetCallback lockoutResetCallback) {
            this.val$powerManager = powerManager;
            this.val$callback = lockoutResetCallback;
        }

        @Override // android.hardware.fingerprint.IFingerprintServiceLockoutResetCallback
        public void onLockoutReset(long deviceId, IRemoteCallback serverCallback) throws RemoteException {
            try {
                final PowerManager.WakeLock wakeLock = this.val$powerManager.newWakeLock(1, "lockoutResetCallback");
                wakeLock.acquire();
                Handler handler = FingerprintManager.this.mHandler;
                final LockoutResetCallback lockoutResetCallback = this.val$callback;
                handler.post(new Runnable() { // from class: android.hardware.fingerprint.-$$Lambda$FingerprintManager$1$4i3tUU8mafgvA9HaB2UPD31L6UY
                    @Override // java.lang.Runnable
                    public final void run() {
                        FingerprintManager.AnonymousClass1.lambda$onLockoutReset$0(FingerprintManager.LockoutResetCallback.this, wakeLock);
                    }
                });
            } finally {
                serverCallback.sendResult(null);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onLockoutReset$0(LockoutResetCallback callback, PowerManager.WakeLock wakeLock) {
            try {
                callback.onLockoutReset();
            } finally {
                wakeLock.release();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MyHandler extends Handler {
        /* synthetic */ MyHandler(FingerprintManager x0, Context x1, AnonymousClass1 x2) {
            this(x1);
        }

        /* synthetic */ MyHandler(FingerprintManager x0, Looper x1, AnonymousClass1 x2) {
            this(x1);
        }

        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        private MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    sendEnrollResult((Fingerprint) msg.obj, msg.arg1);
                    return;
                case 101:
                    FingerprintManager.this.sendAcquiredResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                case 102:
                    FingerprintManager.this.sendAuthenticatedSucceeded((Fingerprint) msg.obj, msg.arg1);
                    return;
                case 103:
                    FingerprintManager.this.sendAuthenticatedFailed();
                    return;
                case 104:
                    FingerprintManager.this.sendErrorResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                case 105:
                    sendRemovedResult((Fingerprint) msg.obj, msg.arg1);
                    return;
                case 106:
                    sendEnumeratedResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                default:
                    return;
            }
        }

        private synchronized void sendRemovedResult(Fingerprint fingerprint, int remaining) {
            if (FingerprintManager.this.mRemovalCallback == null) {
                return;
            }
            if (fingerprint == null) {
                Slog.e(FingerprintManager.TAG, "Received MSG_REMOVED, but fingerprint is null");
                return;
            }
            int fingerId = fingerprint.getFingerId();
            int reqFingerId = FingerprintManager.this.mRemovalFingerprint.getFingerId();
            if (reqFingerId != 0 && fingerId != 0 && fingerId != reqFingerId) {
                Slog.w(FingerprintManager.TAG, "Finger id didn't match: " + fingerId + " != " + reqFingerId);
                return;
            }
            int groupId = fingerprint.getGroupId();
            int reqGroupId = FingerprintManager.this.mRemovalFingerprint.getGroupId();
            if (groupId == reqGroupId) {
                FingerprintManager.this.mRemovalCallback.onRemovalSucceeded(fingerprint, remaining);
                return;
            }
            Slog.w(FingerprintManager.TAG, "Group id didn't match: " + groupId + " != " + reqGroupId);
        }

        private synchronized void sendEnumeratedResult(long deviceId, int fingerId, int groupId) {
            if (FingerprintManager.this.mEnumerateCallback != null) {
                FingerprintManager.this.mEnumerateCallback.onEnumerate(new Fingerprint(null, groupId, fingerId, deviceId));
            }
        }

        private synchronized void sendEnrollResult(Fingerprint fp, int remaining) {
            if (FingerprintManager.this.mEnrollmentCallback != null) {
                FingerprintManager.this.mEnrollmentCallback.onEnrollmentProgress(remaining);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendAuthenticatedSucceeded(Fingerprint fp, int userId) {
        if (this.mAuthenticationCallback != null) {
            BiometricAuthenticator.AuthenticationResult result = new BiometricAuthenticator.AuthenticationResult(this.mCryptoObject, fp, userId);
            this.mAuthenticationCallback.onAuthenticationSucceeded(result);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendAuthenticatedFailed() {
        if (this.mAuthenticationCallback != null) {
            this.mAuthenticationCallback.onAuthenticationFailed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendAcquiredResult(long deviceId, int acquireInfo, int vendorCode) {
        if (this.mAuthenticationCallback != null) {
            this.mAuthenticationCallback.onAuthenticationAcquired(acquireInfo);
        }
        String msg = getAcquiredString(acquireInfo, vendorCode);
        if (msg == null) {
            return;
        }
        int clientInfo = acquireInfo == 6 ? vendorCode + 1000 : acquireInfo;
        if (this.mEnrollmentCallback != null) {
            this.mEnrollmentCallback.onEnrollmentHelp(clientInfo, msg);
        } else if (this.mAuthenticationCallback != null) {
            this.mAuthenticationCallback.onAuthenticationHelp(clientInfo, msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendErrorResult(long deviceId, int errMsgId, int vendorCode) {
        int clientErrMsgId = errMsgId == 8 ? vendorCode + 1000 : errMsgId;
        if (this.mEnrollmentCallback != null) {
            this.mEnrollmentCallback.onEnrollmentError(clientErrMsgId, getErrorString(errMsgId, vendorCode));
        } else if (this.mAuthenticationCallback != null) {
            this.mAuthenticationCallback.onAuthenticationError(clientErrMsgId, getErrorString(errMsgId, vendorCode));
        } else if (this.mRemovalCallback != null) {
            this.mRemovalCallback.onRemovalError(this.mRemovalFingerprint, clientErrMsgId, getErrorString(errMsgId, vendorCode));
        } else if (this.mEnumerateCallback != null) {
            this.mEnumerateCallback.onEnumerateError(clientErrMsgId, getErrorString(errMsgId, vendorCode));
        }
    }

    public synchronized FingerprintManager(Context context, IFingerprintService service) {
        this.mContext = context;
        this.mService = service;
        if (this.mService == null) {
            Slog.v(TAG, "FingerprintManagerService was null");
        }
        this.mHandler = new MyHandler(this, context, (AnonymousClass1) null);
    }

    private synchronized int getCurrentUserId() {
        try {
            return ActivityManager.getService().getCurrentUser().id;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void cancelEnrollment() {
        if (this.mService != null) {
            try {
                this.mService.cancelEnrollment(this.mToken);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void cancelAuthentication(android.hardware.biometrics.CryptoObject cryptoObject) {
        if (this.mService != null) {
            try {
                this.mService.cancelAuthentication(this.mToken, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public synchronized String getErrorString(int errMsg, int vendorCode) {
        switch (errMsg) {
            case 1:
                return this.mContext.getString(R.string.fingerprint_error_hw_not_available);
            case 2:
                return this.mContext.getString(R.string.fingerprint_error_unable_to_process);
            case 3:
                return this.mContext.getString(R.string.fingerprint_error_timeout);
            case 4:
                return this.mContext.getString(R.string.fingerprint_error_no_space);
            case 5:
                return this.mContext.getString(R.string.fingerprint_error_canceled);
            case 7:
                return this.mContext.getString(R.string.fingerprint_error_lockout);
            case 8:
                String[] msgArray = this.mContext.getResources().getStringArray(R.array.fingerprint_error_vendor);
                if (vendorCode < msgArray.length) {
                    return msgArray[vendorCode];
                }
                break;
            case 9:
                return this.mContext.getString(R.string.fingerprint_error_lockout_permanent);
            case 10:
                return this.mContext.getString(R.string.fingerprint_error_user_canceled);
            case 11:
                return this.mContext.getString(R.string.fingerprint_error_no_fingerprints);
            case 12:
                return this.mContext.getString(R.string.fingerprint_error_hw_not_present);
        }
        Slog.w(TAG, "Invalid error message: " + errMsg + ", " + vendorCode);
        return null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public synchronized String getAcquiredString(int acquireInfo, int vendorCode) {
        switch (acquireInfo) {
            case 0:
                return null;
            case 1:
                return this.mContext.getString(R.string.fingerprint_acquired_partial);
            case 2:
                return this.mContext.getString(R.string.fingerprint_acquired_insufficient);
            case 3:
                return this.mContext.getString(R.string.fingerprint_acquired_imager_dirty);
            case 4:
                return this.mContext.getString(R.string.fingerprint_acquired_too_slow);
            case 5:
                return this.mContext.getString(R.string.fingerprint_acquired_too_fast);
            case 6:
                String[] msgArray = this.mContext.getResources().getStringArray(R.array.fingerprint_acquired_vendor);
                if (vendorCode < msgArray.length) {
                    return msgArray[vendorCode];
                }
                break;
        }
        Slog.w(TAG, "Invalid acquired message: " + acquireInfo + ", " + vendorCode);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.hardware.fingerprint.FingerprintManager$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends IFingerprintServiceReceiver.Stub {
        AnonymousClass2() {
        }

        @Override // android.hardware.fingerprint.IFingerprintServiceReceiver
        public void onEnrollResult(long deviceId, int fingerId, int groupId, int remaining) {
            FingerprintManager.this.mHandler.obtainMessage(100, remaining, 0, new Fingerprint(null, groupId, fingerId, deviceId)).sendToTarget();
        }

        @Override // android.hardware.fingerprint.IFingerprintServiceReceiver
        public void onAcquired(final long deviceId, final int acquireInfo, final int vendorCode) {
            if (FingerprintManager.this.mExecutor != null) {
                FingerprintManager.this.mExecutor.execute(new Runnable() { // from class: android.hardware.fingerprint.-$$Lambda$FingerprintManager$2$-CkUh5EAfiFsfsEamQtkeaLZq6M
                    @Override // java.lang.Runnable
                    public final void run() {
                        FingerprintManager.this.sendAcquiredResult(deviceId, acquireInfo, vendorCode);
                    }
                });
            } else {
                FingerprintManager.this.mHandler.obtainMessage(101, acquireInfo, vendorCode, Long.valueOf(deviceId)).sendToTarget();
            }
        }

        @Override // android.hardware.fingerprint.IFingerprintServiceReceiver
        public void onAuthenticationSucceeded(long deviceId, final Fingerprint fp, final int userId) {
            if (FingerprintManager.this.mExecutor != null) {
                FingerprintManager.this.mExecutor.execute(new Runnable() { // from class: android.hardware.fingerprint.-$$Lambda$FingerprintManager$2$O5sigT8DLDwmCzdvD-k13MacOBU
                    @Override // java.lang.Runnable
                    public final void run() {
                        FingerprintManager.this.sendAuthenticatedSucceeded(fp, userId);
                    }
                });
            } else {
                FingerprintManager.this.mHandler.obtainMessage(102, userId, 0, fp).sendToTarget();
            }
        }

        @Override // android.hardware.fingerprint.IFingerprintServiceReceiver
        public void onAuthenticationFailed(long deviceId) {
            if (FingerprintManager.this.mExecutor != null) {
                FingerprintManager.this.mExecutor.execute(new Runnable() { // from class: android.hardware.fingerprint.-$$Lambda$FingerprintManager$2$ycpCnXGQKksU_rpxKvBm1XDbloE
                    @Override // java.lang.Runnable
                    public final void run() {
                        FingerprintManager.this.sendAuthenticatedFailed();
                    }
                });
            } else {
                FingerprintManager.this.mHandler.obtainMessage(103).sendToTarget();
            }
        }

        @Override // android.hardware.fingerprint.IFingerprintServiceReceiver
        public void onError(final long deviceId, final int error, final int vendorCode) {
            if (FingerprintManager.this.mExecutor == null) {
                FingerprintManager.this.mHandler.obtainMessage(104, error, vendorCode, Long.valueOf(deviceId)).sendToTarget();
            } else if (error == 10 || error == 5) {
                FingerprintManager.this.mExecutor.execute(new Runnable() { // from class: android.hardware.fingerprint.-$$Lambda$FingerprintManager$2$iiSGvjInjtzVqJ-wXw-4RQIjKDs
                    @Override // java.lang.Runnable
                    public final void run() {
                        FingerprintManager.this.sendErrorResult(deviceId, error, vendorCode);
                    }
                });
            } else {
                FingerprintManager.this.mHandler.postDelayed(new Runnable() { // from class: android.hardware.fingerprint.-$$Lambda$FingerprintManager$2$n67wlbYWr0PNZwBB3xLLO4RgAq4
                    @Override // java.lang.Runnable
                    public final void run() {
                        FingerprintManager.this.mExecutor.execute(new Runnable() { // from class: android.hardware.fingerprint.-$$Lambda$FingerprintManager$2$DBgvtkIDrK5T5S0UgEanHtoKmOA
                            @Override // java.lang.Runnable
                            public final void run() {
                                FingerprintManager.this.sendErrorResult(r2, r4, r5);
                            }
                        });
                    }
                }, SurfaceView.SurfaceViewFactory.BACKGROUND_TRANSACTION_DELAY);
            }
        }

        @Override // android.hardware.fingerprint.IFingerprintServiceReceiver
        public void onRemoved(long deviceId, int fingerId, int groupId, int remaining) {
            FingerprintManager.this.mHandler.obtainMessage(105, remaining, 0, new Fingerprint(null, groupId, fingerId, deviceId)).sendToTarget();
        }

        @Override // android.hardware.fingerprint.IFingerprintServiceReceiver
        public void onEnumerated(long deviceId, int fingerId, int groupId, int remaining) {
            FingerprintManager.this.mHandler.obtainMessage(106, fingerId, groupId, Long.valueOf(deviceId)).sendToTarget();
        }
    }
}
