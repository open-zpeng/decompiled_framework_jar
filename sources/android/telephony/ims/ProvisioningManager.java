package android.telephony.ims;

import android.annotation.SystemApi;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ProvisioningManager;
import android.telephony.ims.aidl.IImsConfigCallback;
import com.android.internal.telephony.ITelephony;
import com.android.internal.util.FunctionalUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Executor;

@SystemApi
/* loaded from: classes2.dex */
public class ProvisioningManager {
    public static final int KEY_VOICE_OVER_WIFI_MODE_OVERRIDE = 27;
    public static final int KEY_VOICE_OVER_WIFI_ROAMING_ENABLED_OVERRIDE = 26;
    public static final int PROVISIONING_VALUE_DISABLED = 0;
    public static final int PROVISIONING_VALUE_ENABLED = 1;
    public static final String STRING_QUERY_RESULT_ERROR_GENERIC = "STRING_QUERY_RESULT_ERROR_GENERIC";
    public static final String STRING_QUERY_RESULT_ERROR_NOT_READY = "STRING_QUERY_RESULT_ERROR_NOT_READY";
    private int mSubId;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface StringResultError {
    }

    /* loaded from: classes2.dex */
    public static class Callback {
        private final CallbackBinder mBinder = new CallbackBinder();

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class CallbackBinder extends IImsConfigCallback.Stub {
            private Executor mExecutor;
            private final Callback mLocalConfigurationCallback;

            private CallbackBinder(Callback localConfigurationCallback) {
                this.mLocalConfigurationCallback = localConfigurationCallback;
            }

            @Override // android.telephony.ims.aidl.IImsConfigCallback
            public final void onIntConfigChanged(final int item, final int value) {
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.ims.-$$Lambda$ProvisioningManager$Callback$CallbackBinder$rMBayJlNIko46WAqcRq_ggxbfrY
                    @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                    public final void runOrThrow() {
                        ProvisioningManager.Callback.CallbackBinder.this.lambda$onIntConfigChanged$1$ProvisioningManager$Callback$CallbackBinder(item, value);
                    }
                });
            }

            public /* synthetic */ void lambda$onIntConfigChanged$1$ProvisioningManager$Callback$CallbackBinder(final int item, final int value) throws Exception {
                this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.-$$Lambda$ProvisioningManager$Callback$CallbackBinder$R_8jXQuOM7aV7dIwYBzcWwV-YpM
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProvisioningManager.Callback.CallbackBinder.this.lambda$onIntConfigChanged$0$ProvisioningManager$Callback$CallbackBinder(item, value);
                    }
                });
            }

            public /* synthetic */ void lambda$onIntConfigChanged$0$ProvisioningManager$Callback$CallbackBinder(int item, int value) {
                this.mLocalConfigurationCallback.onProvisioningIntChanged(item, value);
            }

            @Override // android.telephony.ims.aidl.IImsConfigCallback
            public final void onStringConfigChanged(final int item, final String value) {
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.ims.-$$Lambda$ProvisioningManager$Callback$CallbackBinder$Jkes2onT-fqeBCDl6FWK1nXcIt0
                    @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                    public final void runOrThrow() {
                        ProvisioningManager.Callback.CallbackBinder.this.lambda$onStringConfigChanged$3$ProvisioningManager$Callback$CallbackBinder(item, value);
                    }
                });
            }

            public /* synthetic */ void lambda$onStringConfigChanged$3$ProvisioningManager$Callback$CallbackBinder(final int item, final String value) throws Exception {
                this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.-$$Lambda$ProvisioningManager$Callback$CallbackBinder$rsWuitP9riQDO6nFxj5wJBdYX40
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProvisioningManager.Callback.CallbackBinder.this.lambda$onStringConfigChanged$2$ProvisioningManager$Callback$CallbackBinder(item, value);
                    }
                });
            }

            public /* synthetic */ void lambda$onStringConfigChanged$2$ProvisioningManager$Callback$CallbackBinder(int item, String value) {
                this.mLocalConfigurationCallback.onProvisioningStringChanged(item, value);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExecutor(Executor executor) {
                this.mExecutor = executor;
            }
        }

        public void onProvisioningIntChanged(int item, int value) {
        }

        public void onProvisioningStringChanged(int item, String value) {
        }

        public final IImsConfigCallback getBinder() {
            return this.mBinder;
        }

        public void setExecutor(Executor executor) {
            this.mBinder.setExecutor(executor);
        }
    }

    public static ProvisioningManager createForSubscriptionId(int subId) {
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            throw new IllegalArgumentException("Invalid subscription ID");
        }
        return new ProvisioningManager(subId);
    }

    private ProvisioningManager(int subId) {
        this.mSubId = subId;
    }

    public void registerProvisioningChangedCallback(Executor executor, Callback callback) throws ImsException {
        if (!isImsAvailableOnDevice()) {
            throw new ImsException("IMS not available on device.", 2);
        }
        callback.setExecutor(executor);
        try {
            getITelephony().registerImsProvisioningChangedCallback(this.mSubId, callback.getBinder());
        } catch (RemoteException | IllegalStateException e) {
            throw new ImsException(e.getMessage(), 1);
        }
    }

    public void unregisterProvisioningChangedCallback(Callback callback) {
        try {
            getITelephony().unregisterImsProvisioningChangedCallback(this.mSubId, callback.getBinder());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public int getProvisioningIntValue(int key) {
        try {
            return getITelephony().getImsProvisioningInt(this.mSubId, key);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public String getProvisioningStringValue(int key) {
        try {
            return getITelephony().getImsProvisioningString(this.mSubId, key);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public int setProvisioningIntValue(int key, int value) {
        try {
            return getITelephony().setImsProvisioningInt(this.mSubId, key, value);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public int setProvisioningStringValue(int key, String value) {
        try {
            return getITelephony().setImsProvisioningString(this.mSubId, key, value);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setProvisioningStatusForCapability(int capability, int tech, boolean isProvisioned) {
        try {
            getITelephony().setImsProvisioningStatusForCapability(this.mSubId, capability, tech, isProvisioned);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean getProvisioningStatusForCapability(int capability, int tech) {
        try {
            return getITelephony().getImsProvisioningStatusForCapability(this.mSubId, capability, tech);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    private static boolean isImsAvailableOnDevice() {
        IPackageManager pm = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
        if (pm == null) {
            return true;
        }
        try {
            return pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_IMS, 0);
        } catch (RemoteException e) {
            return true;
        }
    }

    private static ITelephony getITelephony() {
        ITelephony binder = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
        if (binder == null) {
            throw new RuntimeException("Could not find Telephony Service.");
        }
        return binder;
    }
}
