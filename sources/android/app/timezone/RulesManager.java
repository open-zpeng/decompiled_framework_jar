package android.app.timezone;

import android.app.timezone.ICallback;
import android.app.timezone.IRulesManager;
import android.app.timezone.RulesManager;
import android.content.Context;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class RulesManager {
    private protected static final String ACTION_RULES_UPDATE_OPERATION = "com.android.intent.action.timezone.RULES_UPDATE_OPERATION";
    public protected static final boolean DEBUG = false;
    private protected static final int ERROR_OPERATION_IN_PROGRESS = 1;
    private protected static final int ERROR_UNKNOWN_FAILURE = 2;
    private protected static final String EXTRA_OPERATION_STAGED = "staged";
    private protected static final int SUCCESS = 0;
    public protected static final String TAG = "timezone.RulesManager";
    public protected final Context mContext;
    public protected final IRulesManager mIRulesManager = IRulesManager.Stub.asInterface(ServiceManager.getService(Context.TIME_ZONE_RULES_MANAGER_SERVICE));

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ResultCode {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized RulesManager(Context context) {
        this.mContext = context;
    }

    private protected synchronized RulesState getRulesState() {
        try {
            logDebug("mIRulesManager.getRulesState()");
            RulesState rulesState = this.mIRulesManager.getRulesState();
            logDebug("mIRulesManager.getRulesState() returned " + rulesState);
            return rulesState;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected synchronized int requestInstall(ParcelFileDescriptor distroFileDescriptor, byte[] checkToken, Callback callback) throws IOException {
        ICallback iCallback = new CallbackWrapper(this.mContext, callback);
        try {
            logDebug("mIRulesManager.requestInstall()");
            return this.mIRulesManager.requestInstall(distroFileDescriptor, checkToken, iCallback);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected synchronized int requestUninstall(byte[] checkToken, Callback callback) {
        ICallback iCallback = new CallbackWrapper(this.mContext, callback);
        try {
            logDebug("mIRulesManager.requestUninstall()");
            return this.mIRulesManager.requestUninstall(checkToken, iCallback);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CallbackWrapper extends ICallback.Stub {
        public private protected final Callback mCallback;
        public private protected final Handler mHandler;

        CallbackWrapper(Context context, Callback callback) {
            this.mCallback = callback;
            this.mHandler = new Handler(context.getMainLooper());
        }

        private protected synchronized void onFinished(final int status) {
            RulesManager.logDebug("mCallback.onFinished(status), status=" + status);
            this.mHandler.post(new Runnable() { // from class: android.app.timezone.-$$Lambda$RulesManager$CallbackWrapper$t7a48uTTxaRuSo3YBKxBIbPQznY
                @Override // java.lang.Runnable
                public final void run() {
                    RulesManager.CallbackWrapper.this.mCallback.onFinished(status);
                }
            });
        }
    }

    private protected synchronized void requestNothing(byte[] checkToken, boolean succeeded) {
        try {
            logDebug("mIRulesManager.requestNothing() with token=" + Arrays.toString(checkToken));
            this.mIRulesManager.requestNothing(checkToken, succeeded);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public private protected static synchronized void logDebug(String msg) {
    }
}
