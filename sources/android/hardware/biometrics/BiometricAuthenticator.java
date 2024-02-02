package android.hardware.biometrics;

import android.os.CancellationSignal;
import android.os.Parcelable;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface BiometricAuthenticator {

    /* loaded from: classes.dex */
    public static abstract class BiometricIdentifier implements Parcelable {
    }

    synchronized void authenticate(CryptoObject cryptoObject, CancellationSignal cancellationSignal, Executor executor, AuthenticationCallback authenticationCallback);

    synchronized void authenticate(CancellationSignal cancellationSignal, Executor executor, AuthenticationCallback authenticationCallback);

    /* loaded from: classes.dex */
    public static class AuthenticationResult {
        private CryptoObject mCryptoObject;
        private BiometricIdentifier mIdentifier;
        private int mUserId;

        public synchronized AuthenticationResult() {
        }

        public synchronized AuthenticationResult(CryptoObject crypto, BiometricIdentifier identifier, int userId) {
            this.mCryptoObject = crypto;
            this.mIdentifier = identifier;
            this.mUserId = userId;
        }

        public synchronized CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }

        public synchronized BiometricIdentifier getId() {
            return this.mIdentifier;
        }

        public synchronized int getUserId() {
            return this.mUserId;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class AuthenticationCallback {
        public synchronized void onAuthenticationError(int errorCode, CharSequence errString) {
        }

        public synchronized void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        }

        public synchronized void onAuthenticationSucceeded(AuthenticationResult result) {
        }

        public synchronized void onAuthenticationFailed() {
        }

        public synchronized void onAuthenticationAcquired(int acquireInfo) {
        }
    }
}
