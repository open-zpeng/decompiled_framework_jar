package android.security;

import android.content.ContentResolver;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.security.IConfirmationPromptCallback;
import android.text.TextUtils;
import android.util.Log;
import java.util.Locale;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class ConfirmationPrompt {
    private static final String TAG = "ConfirmationPrompt";
    private static final int UI_OPTION_ACCESSIBILITY_INVERTED_FLAG = 1;
    private static final int UI_OPTION_ACCESSIBILITY_MAGNIFIED_FLAG = 2;
    private ConfirmationCallback mCallback;
    private final IBinder mCallbackBinder;
    private Context mContext;
    private Executor mExecutor;
    private byte[] mExtraData;
    private final KeyStore mKeyStore;
    private CharSequence mPromptText;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void doCallback(int responseCode, byte[] dataThatWasConfirmed, ConfirmationCallback callback) {
        if (responseCode != 5) {
            switch (responseCode) {
                case 0:
                    callback.onConfirmed(dataThatWasConfirmed);
                    return;
                case 1:
                    callback.onDismissed();
                    return;
                case 2:
                    callback.onCanceled();
                    return;
                default:
                    callback.onError(new Exception("Unexpected responseCode=" + responseCode + " from onConfirmtionPromptCompleted() callback."));
                    return;
            }
        }
        callback.onError(new Exception("System error returned by ConfirmationUI."));
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private Context mContext;
        private byte[] mExtraData;
        private CharSequence mPromptText;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setPromptText(CharSequence promptText) {
            this.mPromptText = promptText;
            return this;
        }

        public Builder setExtraData(byte[] extraData) {
            this.mExtraData = extraData;
            return this;
        }

        public ConfirmationPrompt build() {
            if (TextUtils.isEmpty(this.mPromptText)) {
                throw new IllegalArgumentException("prompt text must be set and non-empty");
            }
            if (this.mExtraData == null) {
                throw new IllegalArgumentException("extraData must be set");
            }
            return new ConfirmationPrompt(this.mContext, this.mPromptText, this.mExtraData);
        }
    }

    private synchronized ConfirmationPrompt(Context context, CharSequence promptText, byte[] extraData) {
        this.mKeyStore = KeyStore.getInstance();
        this.mCallbackBinder = new IConfirmationPromptCallback.Stub() { // from class: android.security.ConfirmationPrompt.1
            @Override // android.security.IConfirmationPromptCallback
            public void onConfirmationPromptCompleted(final int responseCode, final byte[] dataThatWasConfirmed) throws RemoteException {
                if (ConfirmationPrompt.this.mCallback != null) {
                    final ConfirmationCallback callback = ConfirmationPrompt.this.mCallback;
                    Executor executor = ConfirmationPrompt.this.mExecutor;
                    ConfirmationPrompt.this.mCallback = null;
                    ConfirmationPrompt.this.mExecutor = null;
                    if (executor == null) {
                        ConfirmationPrompt.this.doCallback(responseCode, dataThatWasConfirmed, callback);
                    } else {
                        executor.execute(new Runnable() { // from class: android.security.ConfirmationPrompt.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ConfirmationPrompt.this.doCallback(responseCode, dataThatWasConfirmed, callback);
                            }
                        });
                    }
                }
            }
        };
        this.mContext = context;
        this.mPromptText = promptText;
        this.mExtraData = extraData;
    }

    private synchronized int getUiOptionsAsFlags() {
        int uiOptionsAsFlags = 0;
        try {
            ContentResolver contentResolver = this.mContext.getContentResolver();
            int inversionEnabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED);
            if (inversionEnabled == 1) {
                uiOptionsAsFlags = 0 | 1;
            }
            float fontScale = Settings.System.getFloat(contentResolver, Settings.System.FONT_SCALE);
            if (fontScale > 1.0d) {
                return uiOptionsAsFlags | 2;
            }
            return uiOptionsAsFlags;
        } catch (Settings.SettingNotFoundException e) {
            Log.w(TAG, "Unexpected SettingNotFoundException");
            return uiOptionsAsFlags;
        }
    }

    private static synchronized boolean isAccessibilityServiceRunning(Context context) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            int a11yEnabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED);
            if (a11yEnabled != 1) {
                return false;
            }
            return true;
        } catch (Settings.SettingNotFoundException e) {
            Log.w(TAG, "Unexpected SettingNotFoundException");
            e.printStackTrace();
            return false;
        }
    }

    public void presentPrompt(Executor executor, ConfirmationCallback callback) throws ConfirmationAlreadyPresentingException, ConfirmationNotAvailableException {
        if (this.mCallback != null) {
            throw new ConfirmationAlreadyPresentingException();
        }
        if (isAccessibilityServiceRunning(this.mContext)) {
            throw new ConfirmationNotAvailableException();
        }
        this.mCallback = callback;
        this.mExecutor = executor;
        int uiOptionsAsFlags = getUiOptionsAsFlags();
        String locale = Locale.getDefault().toLanguageTag();
        int responseCode = this.mKeyStore.presentConfirmationPrompt(this.mCallbackBinder, this.mPromptText.toString(), this.mExtraData, locale, uiOptionsAsFlags);
        if (responseCode != 0) {
            if (responseCode == 3) {
                throw new ConfirmationAlreadyPresentingException();
            }
            if (responseCode == 6) {
                throw new ConfirmationNotAvailableException();
            }
            if (responseCode == 65536) {
                throw new IllegalArgumentException();
            }
            Log.w(TAG, "Unexpected responseCode=" + responseCode + " from presentConfirmationPrompt() call.");
            throw new IllegalArgumentException();
        }
    }

    public void cancelPrompt() {
        int responseCode = this.mKeyStore.cancelConfirmationPrompt(this.mCallbackBinder);
        if (responseCode == 0) {
            return;
        }
        if (responseCode == 3) {
            throw new IllegalStateException();
        }
        Log.w(TAG, "Unexpected responseCode=" + responseCode + " from cancelConfirmationPrompt() call.");
        throw new IllegalStateException();
    }

    public static boolean isSupported(Context context) {
        if (isAccessibilityServiceRunning(context)) {
            return false;
        }
        return KeyStore.getInstance().isConfirmationPromptSupported();
    }
}
