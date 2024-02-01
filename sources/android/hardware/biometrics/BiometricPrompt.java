package android.hardware.biometrics;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.biometrics.IBiometricService;
import android.hardware.biometrics.IBiometricServiceReceiver;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.R;
import java.security.Signature;
import java.util.concurrent.Executor;
import javax.crypto.Cipher;
import javax.crypto.Mac;

/* loaded from: classes.dex */
public class BiometricPrompt implements BiometricAuthenticator, BiometricConstants {
    public static final int DISMISSED_REASON_NEGATIVE = 2;
    public static final int DISMISSED_REASON_POSITIVE = 1;
    public static final int DISMISSED_REASON_USER_CANCEL = 3;
    public static final int HIDE_DIALOG_DELAY = 2000;
    public static final String KEY_ALLOW_DEVICE_CREDENTIAL = "allow_device_credential";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_FROM_CONFIRM_DEVICE_CREDENTIAL = "from_confirm_device_credential";
    public static final String KEY_NEGATIVE_TEXT = "negative_text";
    public static final String KEY_POSITIVE_TEXT = "positive_text";
    public static final String KEY_REQUIRE_CONFIRMATION = "require_confirmation";
    public static final String KEY_SUBTITLE = "subtitle";
    public static final String KEY_TITLE = "title";
    public static final String KEY_USE_DEFAULT_TITLE = "use_default_title";
    private static final String TAG = "BiometricPrompt";
    private AuthenticationCallback mAuthenticationCallback;
    private final IBiometricServiceReceiver mBiometricServiceReceiver;
    private final Bundle mBundle;
    private final Context mContext;
    private CryptoObject mCryptoObject;
    private Executor mExecutor;
    private final ButtonInfo mNegativeButtonInfo;
    private final ButtonInfo mPositiveButtonInfo;
    private final IBiometricService mService;
    private final IBinder mToken;

    /* synthetic */ BiometricPrompt(Context x0, Bundle x1, ButtonInfo x2, ButtonInfo x3, AnonymousClass1 x4) {
        this(x0, x1, x2, x3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ButtonInfo {
        Executor executor;
        DialogInterface.OnClickListener listener;

        ButtonInfo(Executor ex, DialogInterface.OnClickListener l) {
            this.executor = ex;
            this.listener = l;
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final Bundle mBundle = new Bundle();
        private Context mContext;
        private ButtonInfo mNegativeButtonInfo;
        private ButtonInfo mPositiveButtonInfo;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(CharSequence title) {
            this.mBundle.putCharSequence("title", title);
            return this;
        }

        public Builder setUseDefaultTitle() {
            this.mBundle.putBoolean(BiometricPrompt.KEY_USE_DEFAULT_TITLE, true);
            return this;
        }

        public Builder setSubtitle(CharSequence subtitle) {
            this.mBundle.putCharSequence(BiometricPrompt.KEY_SUBTITLE, subtitle);
            return this;
        }

        public Builder setDescription(CharSequence description) {
            this.mBundle.putCharSequence("description", description);
            return this;
        }

        public Builder setPositiveButton(CharSequence text, Executor executor, DialogInterface.OnClickListener listener) {
            if (TextUtils.isEmpty(text)) {
                throw new IllegalArgumentException("Text must be set and non-empty");
            }
            if (executor == null) {
                throw new IllegalArgumentException("Executor must not be null");
            }
            if (listener == null) {
                throw new IllegalArgumentException("Listener must not be null");
            }
            this.mBundle.putCharSequence(BiometricPrompt.KEY_POSITIVE_TEXT, text);
            this.mPositiveButtonInfo = new ButtonInfo(executor, listener);
            return this;
        }

        public Builder setNegativeButton(CharSequence text, Executor executor, DialogInterface.OnClickListener listener) {
            if (TextUtils.isEmpty(text)) {
                throw new IllegalArgumentException("Text must be set and non-empty");
            }
            if (executor == null) {
                throw new IllegalArgumentException("Executor must not be null");
            }
            if (listener == null) {
                throw new IllegalArgumentException("Listener must not be null");
            }
            this.mBundle.putCharSequence(BiometricPrompt.KEY_NEGATIVE_TEXT, text);
            this.mNegativeButtonInfo = new ButtonInfo(executor, listener);
            return this;
        }

        public Builder setConfirmationRequired(boolean requireConfirmation) {
            this.mBundle.putBoolean(BiometricPrompt.KEY_REQUIRE_CONFIRMATION, requireConfirmation);
            return this;
        }

        public Builder setDeviceCredentialAllowed(boolean allowed) {
            this.mBundle.putBoolean(BiometricPrompt.KEY_ALLOW_DEVICE_CREDENTIAL, allowed);
            return this;
        }

        public Builder setFromConfirmDeviceCredential() {
            this.mBundle.putBoolean(BiometricPrompt.KEY_FROM_CONFIRM_DEVICE_CREDENTIAL, true);
            return this;
        }

        public BiometricPrompt build() {
            CharSequence title = this.mBundle.getCharSequence("title");
            CharSequence negative = this.mBundle.getCharSequence(BiometricPrompt.KEY_NEGATIVE_TEXT);
            boolean useDefaultTitle = this.mBundle.getBoolean(BiometricPrompt.KEY_USE_DEFAULT_TITLE);
            boolean enableFallback = this.mBundle.getBoolean(BiometricPrompt.KEY_ALLOW_DEVICE_CREDENTIAL);
            if (TextUtils.isEmpty(title) && !useDefaultTitle) {
                throw new IllegalArgumentException("Title must be set and non-empty");
            }
            if (TextUtils.isEmpty(negative) && !enableFallback) {
                throw new IllegalArgumentException("Negative text must be set and non-empty");
            }
            if (!TextUtils.isEmpty(negative) && enableFallback) {
                throw new IllegalArgumentException("Can't have both negative button behavior and device credential enabled");
            }
            return new BiometricPrompt(this.mContext, this.mBundle, this.mPositiveButtonInfo, this.mNegativeButtonInfo, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class OnAuthenticationCancelListener implements CancellationSignal.OnCancelListener {
        private OnAuthenticationCancelListener() {
        }

        /* synthetic */ OnAuthenticationCancelListener(BiometricPrompt x0, AnonymousClass1 x1) {
            this();
        }

        @Override // android.os.CancellationSignal.OnCancelListener
        public void onCancel() {
            BiometricPrompt.this.cancelAuthentication();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.hardware.biometrics.BiometricPrompt$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IBiometricServiceReceiver.Stub {
        AnonymousClass1() {
        }

        @Override // android.hardware.biometrics.IBiometricServiceReceiver
        public void onAuthenticationSucceeded() throws RemoteException {
            BiometricPrompt.this.mExecutor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$1$_p2Kb7GLaNe_mSDlUdJIRLMJ5kQ
                @Override // java.lang.Runnable
                public final void run() {
                    BiometricPrompt.AnonymousClass1.this.lambda$onAuthenticationSucceeded$0$BiometricPrompt$1();
                }
            });
        }

        public /* synthetic */ void lambda$onAuthenticationSucceeded$0$BiometricPrompt$1() {
            AuthenticationResult result = new AuthenticationResult(BiometricPrompt.this.mCryptoObject);
            BiometricPrompt.this.mAuthenticationCallback.onAuthenticationSucceeded(result);
        }

        @Override // android.hardware.biometrics.IBiometricServiceReceiver
        public void onAuthenticationFailed() throws RemoteException {
            BiometricPrompt.this.mExecutor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$1$AAMJr_dQQ3dkiYxppvTx2AjuvRQ
                @Override // java.lang.Runnable
                public final void run() {
                    BiometricPrompt.AnonymousClass1.this.lambda$onAuthenticationFailed$1$BiometricPrompt$1();
                }
            });
        }

        public /* synthetic */ void lambda$onAuthenticationFailed$1$BiometricPrompt$1() {
            BiometricPrompt.this.mAuthenticationCallback.onAuthenticationFailed();
        }

        @Override // android.hardware.biometrics.IBiometricServiceReceiver
        public void onError(final int error, final String message) throws RemoteException {
            BiometricPrompt.this.mExecutor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$1$aJtOJjyL74ZJt5iM1EsAPg6PHK4
                @Override // java.lang.Runnable
                public final void run() {
                    BiometricPrompt.AnonymousClass1.this.lambda$onError$2$BiometricPrompt$1(error, message);
                }
            });
        }

        public /* synthetic */ void lambda$onError$2$BiometricPrompt$1(int error, String message) {
            BiometricPrompt.this.mAuthenticationCallback.onAuthenticationError(error, message);
        }

        @Override // android.hardware.biometrics.IBiometricServiceReceiver
        public void onAcquired(final int acquireInfo, final String message) throws RemoteException {
            BiometricPrompt.this.mExecutor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$1$yfG83rs6eJM9CDMAlhftsvdKekY
                @Override // java.lang.Runnable
                public final void run() {
                    BiometricPrompt.AnonymousClass1.this.lambda$onAcquired$3$BiometricPrompt$1(acquireInfo, message);
                }
            });
        }

        public /* synthetic */ void lambda$onAcquired$3$BiometricPrompt$1(int acquireInfo, String message) {
            BiometricPrompt.this.mAuthenticationCallback.onAuthenticationHelp(acquireInfo, message);
        }

        @Override // android.hardware.biometrics.IBiometricServiceReceiver
        public void onDialogDismissed(int reason) throws RemoteException {
            if (reason == 1) {
                BiometricPrompt.this.mPositiveButtonInfo.executor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$1$Kmc1otRcCm0Akw6_6yK5trqAv78
                    @Override // java.lang.Runnable
                    public final void run() {
                        BiometricPrompt.AnonymousClass1.this.lambda$onDialogDismissed$4$BiometricPrompt$1();
                    }
                });
            } else if (reason == 2) {
                BiometricPrompt.this.mNegativeButtonInfo.executor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$1$G8c-A1luxVwjcfGpdSp4nNPnavM
                    @Override // java.lang.Runnable
                    public final void run() {
                        BiometricPrompt.AnonymousClass1.this.lambda$onDialogDismissed$5$BiometricPrompt$1();
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onDialogDismissed$4$BiometricPrompt$1() {
            BiometricPrompt.this.mPositiveButtonInfo.listener.onClick(null, -1);
        }

        public /* synthetic */ void lambda$onDialogDismissed$5$BiometricPrompt$1() {
            BiometricPrompt.this.mNegativeButtonInfo.listener.onClick(null, -2);
        }
    }

    private BiometricPrompt(Context context, Bundle bundle, ButtonInfo positiveButtonInfo, ButtonInfo negativeButtonInfo) {
        this.mToken = new Binder();
        this.mBiometricServiceReceiver = new AnonymousClass1();
        this.mContext = context;
        this.mBundle = bundle;
        this.mPositiveButtonInfo = positiveButtonInfo;
        this.mNegativeButtonInfo = negativeButtonInfo;
        this.mService = IBiometricService.Stub.asInterface(ServiceManager.getService(Context.BIOMETRIC_SERVICE));
    }

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

    /* loaded from: classes.dex */
    public static class AuthenticationResult extends BiometricAuthenticator.AuthenticationResult {
        public AuthenticationResult(CryptoObject crypto) {
            super(crypto, null, 0);
        }

        @Override // android.hardware.biometrics.BiometricAuthenticator.AuthenticationResult
        public CryptoObject getCryptoObject() {
            return (CryptoObject) super.getCryptoObject();
        }
    }

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
        public void onAuthenticationAcquired(int acquireInfo) {
        }
    }

    public void authenticateUser(CancellationSignal cancel, Executor executor, AuthenticationCallback callback, int userId, IBiometricConfirmDeviceCredentialCallback confirmDeviceCredentialCallback) {
        if (cancel == null) {
            throw new IllegalArgumentException("Must supply a cancellation signal");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Must supply an executor");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Must supply a callback");
        }
        authenticateInternal(null, cancel, executor, callback, userId, confirmDeviceCredentialCallback);
    }

    public void authenticate(CryptoObject crypto, CancellationSignal cancel, Executor executor, AuthenticationCallback callback) {
        if (crypto == null) {
            throw new IllegalArgumentException("Must supply a crypto object");
        }
        if (cancel == null) {
            throw new IllegalArgumentException("Must supply a cancellation signal");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Must supply an executor");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Must supply a callback");
        }
        if (this.mBundle.getBoolean(KEY_ALLOW_DEVICE_CREDENTIAL)) {
            throw new IllegalArgumentException("Device credential not supported with crypto");
        }
        authenticateInternal(crypto, cancel, executor, callback, this.mContext.getUserId(), null);
    }

    public void authenticate(CancellationSignal cancel, Executor executor, AuthenticationCallback callback) {
        if (cancel == null) {
            throw new IllegalArgumentException("Must supply a cancellation signal");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Must supply an executor");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Must supply a callback");
        }
        authenticateInternal(null, cancel, executor, callback, this.mContext.getUserId(), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAuthentication() {
        IBiometricService iBiometricService = this.mService;
        if (iBiometricService != null) {
            try {
                iBiometricService.cancelAuthentication(this.mToken, this.mContext.getOpPackageName());
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to cancel authentication", e);
            }
        }
    }

    private void authenticateInternal(CryptoObject crypto, CancellationSignal cancel, Executor executor, final AuthenticationCallback callback, int userId, IBiometricConfirmDeviceCredentialCallback confirmDeviceCredentialCallback) {
        try {
            if (!cancel.isCanceled()) {
                try {
                    cancel.setOnCancelListener(new OnAuthenticationCancelListener(this, null));
                    this.mCryptoObject = crypto;
                } catch (RemoteException e) {
                    e = e;
                    Log.e(TAG, "Remote exception while authenticating", e);
                    this.mExecutor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$FhnggONVmg0fSM3ar79llL7ZRYM
                        @Override // java.lang.Runnable
                        public final void run() {
                            BiometricPrompt.this.lambda$authenticateInternal$1$BiometricPrompt(callback);
                        }
                    });
                    return;
                }
                try {
                    this.mExecutor = executor;
                    this.mAuthenticationCallback = callback;
                    long sessionId = crypto != null ? crypto.getOpId() : 0L;
                    if (!BiometricManager.hasBiometrics(this.mContext)) {
                        this.mExecutor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$Dk3E1C_ccte-BJOnzgPmi2l5r0I
                            @Override // java.lang.Runnable
                            public final void run() {
                                BiometricPrompt.this.lambda$authenticateInternal$0$BiometricPrompt(callback);
                            }
                        });
                        return;
                    } else {
                        this.mService.authenticate(this.mToken, sessionId, userId, this.mBiometricServiceReceiver, this.mContext.getOpPackageName(), this.mBundle, confirmDeviceCredentialCallback);
                        return;
                    }
                } catch (RemoteException e2) {
                    e = e2;
                    Log.e(TAG, "Remote exception while authenticating", e);
                    this.mExecutor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$FhnggONVmg0fSM3ar79llL7ZRYM
                        @Override // java.lang.Runnable
                        public final void run() {
                            BiometricPrompt.this.lambda$authenticateInternal$1$BiometricPrompt(callback);
                        }
                    });
                    return;
                }
            }
            Log.w(TAG, "Authentication already canceled");
        } catch (RemoteException e3) {
            e = e3;
        }
    }

    public /* synthetic */ void lambda$authenticateInternal$0$BiometricPrompt(AuthenticationCallback callback) {
        callback.onAuthenticationError(12, this.mContext.getString(R.string.biometric_error_hw_unavailable));
    }

    public /* synthetic */ void lambda$authenticateInternal$1$BiometricPrompt(AuthenticationCallback callback) {
        callback.onAuthenticationError(1, this.mContext.getString(R.string.biometric_error_hw_unavailable));
    }
}
