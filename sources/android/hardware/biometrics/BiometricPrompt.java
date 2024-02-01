package android.hardware.biometrics;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.IBiometricPromptReceiver;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
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
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_NEGATIVE_TEXT = "negative_text";
    public static final String KEY_POSITIVE_TEXT = "positive_text";
    public static final String KEY_SUBTITLE = "subtitle";
    public static final String KEY_TITLE = "title";
    private Bundle mBundle;
    IBiometricPromptReceiver mDialogReceiver;
    private FingerprintManager mFingerprintManager;
    private ButtonInfo mNegativeButtonInfo;
    private PackageManager mPackageManager;
    private ButtonInfo mPositiveButtonInfo;

    /* synthetic */ BiometricPrompt(Context x0, Bundle x1, ButtonInfo x2, ButtonInfo x3, AnonymousClass1 x4) {
        this(x0, x1, x2, x3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ButtonInfo {
        Executor executor;
        DialogInterface.OnClickListener listener;

        synchronized ButtonInfo(Executor ex, DialogInterface.OnClickListener l) {
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

        public Builder setSubtitle(CharSequence subtitle) {
            this.mBundle.putCharSequence(BiometricPrompt.KEY_SUBTITLE, subtitle);
            return this;
        }

        public Builder setDescription(CharSequence description) {
            this.mBundle.putCharSequence("description", description);
            return this;
        }

        public synchronized Builder setPositiveButton(CharSequence text, Executor executor, DialogInterface.OnClickListener listener) {
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

        public BiometricPrompt build() {
            CharSequence title = this.mBundle.getCharSequence("title");
            CharSequence negative = this.mBundle.getCharSequence(BiometricPrompt.KEY_NEGATIVE_TEXT);
            if (TextUtils.isEmpty(title)) {
                throw new IllegalArgumentException("Title must be set and non-empty");
            }
            if (TextUtils.isEmpty(negative)) {
                throw new IllegalArgumentException("Negative text must be set and non-empty");
            }
            return new BiometricPrompt(this.mContext, this.mBundle, this.mPositiveButtonInfo, this.mNegativeButtonInfo, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.hardware.biometrics.BiometricPrompt$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IBiometricPromptReceiver.Stub {
        AnonymousClass1() {
        }

        @Override // android.hardware.biometrics.IBiometricPromptReceiver
        public void onDialogDismissed(int reason) {
            if (reason == 1) {
                BiometricPrompt.this.mPositiveButtonInfo.executor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$1$C3fuslKNv7eJTZG9_jFRfCo5_Y4
                    @Override // java.lang.Runnable
                    public final void run() {
                        BiometricPrompt.this.mPositiveButtonInfo.listener.onClick(null, -1);
                    }
                });
            } else if (reason == 2) {
                BiometricPrompt.this.mNegativeButtonInfo.executor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$1$J5PqpiT8xZNiNN1gy9VraVgknaQ
                    @Override // java.lang.Runnable
                    public final void run() {
                        BiometricPrompt.this.mNegativeButtonInfo.listener.onClick(null, -2);
                    }
                });
            }
        }
    }

    private synchronized BiometricPrompt(Context context, Bundle bundle, ButtonInfo positiveButtonInfo, ButtonInfo negativeButtonInfo) {
        this.mDialogReceiver = new AnonymousClass1();
        this.mBundle = bundle;
        this.mPositiveButtonInfo = positiveButtonInfo;
        this.mNegativeButtonInfo = negativeButtonInfo;
        this.mFingerprintManager = (FingerprintManager) context.getSystemService(FingerprintManager.class);
        this.mPackageManager = context.getPackageManager();
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
        public synchronized AuthenticationResult(CryptoObject crypto, BiometricAuthenticator.BiometricIdentifier identifier, int userId) {
            super(crypto, identifier, userId);
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
        public synchronized void onAuthenticationAcquired(int acquireInfo) {
        }

        @Override // android.hardware.biometrics.BiometricAuthenticator.AuthenticationCallback
        public synchronized void onAuthenticationSucceeded(BiometricAuthenticator.AuthenticationResult result) {
            onAuthenticationSucceeded(new AuthenticationResult((CryptoObject) result.getCryptoObject(), result.getId(), result.getUserId()));
        }
    }

    @Override // android.hardware.biometrics.BiometricAuthenticator
    public synchronized void authenticate(android.hardware.biometrics.CryptoObject crypto, CancellationSignal cancel, Executor executor, BiometricAuthenticator.AuthenticationCallback callback) {
        if (!(callback instanceof AuthenticationCallback)) {
            throw new IllegalArgumentException("Callback cannot be casted");
        }
        authenticate(crypto, cancel, executor, (AuthenticationCallback) callback);
    }

    @Override // android.hardware.biometrics.BiometricAuthenticator
    public synchronized void authenticate(CancellationSignal cancel, Executor executor, BiometricAuthenticator.AuthenticationCallback callback) {
        if (!(callback instanceof AuthenticationCallback)) {
            throw new IllegalArgumentException("Callback cannot be casted");
        }
        authenticate(cancel, executor, (AuthenticationCallback) callback);
    }

    public void authenticate(CryptoObject crypto, CancellationSignal cancel, Executor executor, AuthenticationCallback callback) {
        if (handlePreAuthenticationErrors(callback, executor)) {
            return;
        }
        this.mFingerprintManager.authenticate(crypto, cancel, this.mBundle, executor, this.mDialogReceiver, callback);
    }

    public void authenticate(CancellationSignal cancel, Executor executor, AuthenticationCallback callback) {
        if (handlePreAuthenticationErrors(callback, executor)) {
            return;
        }
        this.mFingerprintManager.authenticate(cancel, this.mBundle, executor, this.mDialogReceiver, callback);
    }

    private synchronized boolean handlePreAuthenticationErrors(AuthenticationCallback callback, Executor executor) {
        if (!this.mPackageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            sendError(12, callback, executor);
            return true;
        } else if (!this.mFingerprintManager.isHardwareDetected()) {
            sendError(1, callback, executor);
            return true;
        } else if (!this.mFingerprintManager.hasEnrolledFingerprints()) {
            sendError(11, callback, executor);
            return true;
        } else {
            return false;
        }
    }

    private synchronized void sendError(final int error, final AuthenticationCallback callback, Executor executor) {
        executor.execute(new Runnable() { // from class: android.hardware.biometrics.-$$Lambda$BiometricPrompt$HqBGXtBUWNc-v8NoHYsj2gLfaRw
            @Override // java.lang.Runnable
            public final void run() {
                callback.onAuthenticationError(r2, BiometricPrompt.this.mFingerprintManager.getErrorString(error, 0));
            }
        });
    }
}
