package android.telephony.ims;

import android.annotation.SystemApi;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import android.telephony.ims.feature.MmTelFeature;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.ITelephony;
import com.android.internal.util.FunctionalUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@SystemApi
/* loaded from: classes2.dex */
public class ImsMmTelManager {
    private static final String TAG = "ImsMmTelManager";
    public static final int WIFI_MODE_CELLULAR_PREFERRED = 1;
    public static final int WIFI_MODE_WIFI_ONLY = 0;
    public static final int WIFI_MODE_WIFI_PREFERRED = 2;
    private int mSubId;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface WiFiCallingMode {
    }

    /* loaded from: classes2.dex */
    public static class RegistrationCallback {
        private final RegistrationBinder mBinder = new RegistrationBinder(this);

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class RegistrationBinder extends IImsRegistrationCallback.Stub {
            private static final Map<Integer, Integer> IMS_REG_TO_ACCESS_TYPE_MAP = new HashMap<Integer, Integer>() { // from class: android.telephony.ims.ImsMmTelManager.RegistrationCallback.RegistrationBinder.1
                {
                    put(-1, -1);
                    put(0, 1);
                    put(1, 2);
                }
            };
            private Executor mExecutor;
            private final RegistrationCallback mLocalCallback;

            RegistrationBinder(RegistrationCallback localCallback) {
                this.mLocalCallback = localCallback;
            }

            @Override // android.telephony.ims.aidl.IImsRegistrationCallback
            public void onRegistered(final int imsRadioTech) {
                if (this.mLocalCallback == null) {
                    return;
                }
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$8xq93ap6i0L56Aegaj-ZEUt9ISc
                    @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                    public final void runOrThrow() {
                        ImsMmTelManager.RegistrationCallback.RegistrationBinder.this.lambda$onRegistered$1$ImsMmTelManager$RegistrationCallback$RegistrationBinder(imsRadioTech);
                    }
                });
            }

            public /* synthetic */ void lambda$onRegistered$1$ImsMmTelManager$RegistrationCallback$RegistrationBinder(final int imsRadioTech) throws Exception {
                this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$oDp7ilyKfflFThUCP4Du9EYoDoQ
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImsMmTelManager.RegistrationCallback.RegistrationBinder.this.lambda$onRegistered$0$ImsMmTelManager$RegistrationCallback$RegistrationBinder(imsRadioTech);
                    }
                });
            }

            public /* synthetic */ void lambda$onRegistered$0$ImsMmTelManager$RegistrationCallback$RegistrationBinder(int imsRadioTech) {
                this.mLocalCallback.onRegistered(getAccessType(imsRadioTech));
            }

            @Override // android.telephony.ims.aidl.IImsRegistrationCallback
            public void onRegistering(final int imsRadioTech) {
                if (this.mLocalCallback == null) {
                    return;
                }
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$iuI3HyNU5eUABA_QRyzQ8Jw2-8g
                    @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                    public final void runOrThrow() {
                        ImsMmTelManager.RegistrationCallback.RegistrationBinder.this.lambda$onRegistering$3$ImsMmTelManager$RegistrationCallback$RegistrationBinder(imsRadioTech);
                    }
                });
            }

            public /* synthetic */ void lambda$onRegistering$3$ImsMmTelManager$RegistrationCallback$RegistrationBinder(final int imsRadioTech) throws Exception {
                this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$J4VhgcUtd6SivHcdkzpurbTuyLc
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImsMmTelManager.RegistrationCallback.RegistrationBinder.this.lambda$onRegistering$2$ImsMmTelManager$RegistrationCallback$RegistrationBinder(imsRadioTech);
                    }
                });
            }

            public /* synthetic */ void lambda$onRegistering$2$ImsMmTelManager$RegistrationCallback$RegistrationBinder(int imsRadioTech) {
                this.mLocalCallback.onRegistering(getAccessType(imsRadioTech));
            }

            @Override // android.telephony.ims.aidl.IImsRegistrationCallback
            public void onDeregistered(final ImsReasonInfo info) {
                if (this.mLocalCallback == null) {
                    return;
                }
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$F58PRHsH__07pmyvC0NTRprfEPU
                    @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                    public final void runOrThrow() {
                        ImsMmTelManager.RegistrationCallback.RegistrationBinder.this.lambda$onDeregistered$5$ImsMmTelManager$RegistrationCallback$RegistrationBinder(info);
                    }
                });
            }

            public /* synthetic */ void lambda$onDeregistered$4$ImsMmTelManager$RegistrationCallback$RegistrationBinder(ImsReasonInfo info) {
                this.mLocalCallback.onUnregistered(info);
            }

            public /* synthetic */ void lambda$onDeregistered$5$ImsMmTelManager$RegistrationCallback$RegistrationBinder(final ImsReasonInfo info) throws Exception {
                this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$q0Uz23ATIYan5EBJYUigIVvwE3g
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImsMmTelManager.RegistrationCallback.RegistrationBinder.this.lambda$onDeregistered$4$ImsMmTelManager$RegistrationCallback$RegistrationBinder(info);
                    }
                });
            }

            @Override // android.telephony.ims.aidl.IImsRegistrationCallback
            public void onTechnologyChangeFailed(final int imsRadioTech, final ImsReasonInfo info) {
                if (this.mLocalCallback == null) {
                    return;
                }
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$Nztp9t3A8T2T3SbLvxLZoInLgWY
                    @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                    public final void runOrThrow() {
                        ImsMmTelManager.RegistrationCallback.RegistrationBinder.this.lambda$onTechnologyChangeFailed$7$ImsMmTelManager$RegistrationCallback$RegistrationBinder(imsRadioTech, info);
                    }
                });
            }

            public /* synthetic */ void lambda$onTechnologyChangeFailed$6$ImsMmTelManager$RegistrationCallback$RegistrationBinder(int imsRadioTech, ImsReasonInfo info) {
                this.mLocalCallback.onTechnologyChangeFailed(getAccessType(imsRadioTech), info);
            }

            public /* synthetic */ void lambda$onTechnologyChangeFailed$7$ImsMmTelManager$RegistrationCallback$RegistrationBinder(final int imsRadioTech, final ImsReasonInfo info) throws Exception {
                this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$IeNlpXTAPM2z2VxFA81E0v9udZw
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImsMmTelManager.RegistrationCallback.RegistrationBinder.this.lambda$onTechnologyChangeFailed$6$ImsMmTelManager$RegistrationCallback$RegistrationBinder(imsRadioTech, info);
                    }
                });
            }

            @Override // android.telephony.ims.aidl.IImsRegistrationCallback
            public void onSubscriberAssociatedUriChanged(final Uri[] uris) {
                if (this.mLocalCallback == null) {
                    return;
                }
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$AhnK6VJjwgpDNC1GXRrwfgtYvkM
                    @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                    public final void runOrThrow() {
                        ImsMmTelManager.RegistrationCallback.RegistrationBinder.this.lambda$onSubscriberAssociatedUriChanged$9$ImsMmTelManager$RegistrationCallback$RegistrationBinder(uris);
                    }
                });
            }

            public /* synthetic */ void lambda$onSubscriberAssociatedUriChanged$9$ImsMmTelManager$RegistrationCallback$RegistrationBinder(final Uri[] uris) throws Exception {
                this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$jAP4lCkBQEdyrlgt5jaNPTlFXlY
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImsMmTelManager.RegistrationCallback.RegistrationBinder.this.lambda$onSubscriberAssociatedUriChanged$8$ImsMmTelManager$RegistrationCallback$RegistrationBinder(uris);
                    }
                });
            }

            public /* synthetic */ void lambda$onSubscriberAssociatedUriChanged$8$ImsMmTelManager$RegistrationCallback$RegistrationBinder(Uri[] uris) {
                this.mLocalCallback.onSubscriberAssociatedUriChanged(uris);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExecutor(Executor executor) {
                this.mExecutor = executor;
            }

            private static int getAccessType(int regType) {
                if (!IMS_REG_TO_ACCESS_TYPE_MAP.containsKey(Integer.valueOf(regType))) {
                    Log.w(ImsMmTelManager.TAG, "RegistrationBinder - invalid regType returned: " + regType);
                    return -1;
                }
                return IMS_REG_TO_ACCESS_TYPE_MAP.get(Integer.valueOf(regType)).intValue();
            }
        }

        public void onRegistered(int imsTransportType) {
        }

        public void onRegistering(int imsTransportType) {
        }

        public void onUnregistered(ImsReasonInfo info) {
        }

        public void onTechnologyChangeFailed(int imsTransportType, ImsReasonInfo info) {
        }

        public void onSubscriberAssociatedUriChanged(Uri[] uris) {
        }

        public final IImsRegistrationCallback getBinder() {
            return this.mBinder;
        }

        public void setExecutor(Executor executor) {
            this.mBinder.setExecutor(executor);
        }
    }

    /* loaded from: classes2.dex */
    public static class CapabilityCallback {
        private final CapabilityBinder mBinder = new CapabilityBinder(this);

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class CapabilityBinder extends IImsCapabilityCallback.Stub {
            private Executor mExecutor;
            private final CapabilityCallback mLocalCallback;

            CapabilityBinder(CapabilityCallback c) {
                this.mLocalCallback = c;
            }

            @Override // android.telephony.ims.aidl.IImsCapabilityCallback
            public void onCapabilitiesStatusChanged(final int config) {
                if (this.mLocalCallback == null) {
                    return;
                }
                Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$CapabilityCallback$CapabilityBinder$gK2iK9ZQ3GDeuMTfhRd7yjiYlO8
                    @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                    public final void runOrThrow() {
                        ImsMmTelManager.CapabilityCallback.CapabilityBinder.this.lambda$onCapabilitiesStatusChanged$1$ImsMmTelManager$CapabilityCallback$CapabilityBinder(config);
                    }
                });
            }

            public /* synthetic */ void lambda$onCapabilitiesStatusChanged$0$ImsMmTelManager$CapabilityCallback$CapabilityBinder(int config) {
                this.mLocalCallback.onCapabilitiesStatusChanged(new MmTelFeature.MmTelCapabilities(config));
            }

            public /* synthetic */ void lambda$onCapabilitiesStatusChanged$1$ImsMmTelManager$CapabilityCallback$CapabilityBinder(final int config) throws Exception {
                this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.-$$Lambda$ImsMmTelManager$CapabilityCallback$CapabilityBinder$4YNlUy9HsD02E7Sbv2VeVtbao08
                    @Override // java.lang.Runnable
                    public final void run() {
                        ImsMmTelManager.CapabilityCallback.CapabilityBinder.this.lambda$onCapabilitiesStatusChanged$0$ImsMmTelManager$CapabilityCallback$CapabilityBinder(config);
                    }
                });
            }

            @Override // android.telephony.ims.aidl.IImsCapabilityCallback
            public void onQueryCapabilityConfiguration(int capability, int radioTech, boolean isEnabled) {
            }

            @Override // android.telephony.ims.aidl.IImsCapabilityCallback
            public void onChangeCapabilityConfigurationError(int capability, int radioTech, int reason) {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExecutor(Executor executor) {
                this.mExecutor = executor;
            }
        }

        public void onCapabilitiesStatusChanged(MmTelFeature.MmTelCapabilities capabilities) {
        }

        public final IImsCapabilityCallback getBinder() {
            return this.mBinder;
        }

        public final void setExecutor(Executor executor) {
            this.mBinder.setExecutor(executor);
        }
    }

    public static ImsMmTelManager createForSubscriptionId(int subId) {
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            throw new IllegalArgumentException("Invalid subscription ID");
        }
        return new ImsMmTelManager(subId);
    }

    @VisibleForTesting
    public ImsMmTelManager(int subId) {
        this.mSubId = subId;
    }

    public void registerImsRegistrationCallback(Executor executor, RegistrationCallback c) throws ImsException {
        if (c == null) {
            throw new IllegalArgumentException("Must include a non-null RegistrationCallback.");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Must include a non-null Executor.");
        }
        if (!isImsAvailableOnDevice()) {
            throw new ImsException("IMS not available on device.", 2);
        }
        c.setExecutor(executor);
        try {
            getITelephony().registerImsRegistrationCallback(this.mSubId, c.getBinder());
        } catch (RemoteException | IllegalStateException e) {
            throw new ImsException(e.getMessage(), 1);
        }
    }

    public void unregisterImsRegistrationCallback(RegistrationCallback c) {
        if (c == null) {
            throw new IllegalArgumentException("Must include a non-null RegistrationCallback.");
        }
        try {
            getITelephony().unregisterImsRegistrationCallback(this.mSubId, c.getBinder());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void registerMmTelCapabilityCallback(Executor executor, CapabilityCallback c) throws ImsException {
        if (c == null) {
            throw new IllegalArgumentException("Must include a non-null RegistrationCallback.");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Must include a non-null Executor.");
        }
        if (!isImsAvailableOnDevice()) {
            throw new ImsException("IMS not available on device.", 2);
        }
        c.setExecutor(executor);
        try {
            getITelephony().registerMmTelCapabilityCallback(this.mSubId, c.getBinder());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } catch (IllegalStateException e2) {
            throw new ImsException(e2.getMessage(), 1);
        }
    }

    public void unregisterMmTelCapabilityCallback(CapabilityCallback c) {
        if (c == null) {
            throw new IllegalArgumentException("Must include a non-null RegistrationCallback.");
        }
        try {
            getITelephony().unregisterMmTelCapabilityCallback(this.mSubId, c.getBinder());
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isAdvancedCallingSettingEnabled() {
        try {
            return getITelephony().isAdvancedCallingSettingEnabled(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setAdvancedCallingSettingEnabled(boolean isEnabled) {
        try {
            getITelephony().setAdvancedCallingSettingEnabled(this.mSubId, isEnabled);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isCapable(int capability, int imsRegTech) {
        try {
            return getITelephony().isCapable(this.mSubId, capability, imsRegTech);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isAvailable(int capability, int imsRegTech) {
        try {
            return getITelephony().isAvailable(this.mSubId, capability, imsRegTech);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isVtSettingEnabled() {
        try {
            return getITelephony().isVtSettingEnabled(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVtSettingEnabled(boolean isEnabled) {
        try {
            getITelephony().setVtSettingEnabled(this.mSubId, isEnabled);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isVoWiFiSettingEnabled() {
        try {
            return getITelephony().isVoWiFiSettingEnabled(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVoWiFiSettingEnabled(boolean isEnabled) {
        try {
            getITelephony().setVoWiFiSettingEnabled(this.mSubId, isEnabled);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public boolean isVoWiFiRoamingSettingEnabled() {
        try {
            return getITelephony().isVoWiFiRoamingSettingEnabled(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVoWiFiRoamingSettingEnabled(boolean isEnabled) {
        try {
            getITelephony().setVoWiFiRoamingSettingEnabled(this.mSubId, isEnabled);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVoWiFiNonPersistent(boolean isCapable, int mode) {
        try {
            getITelephony().setVoWiFiNonPersistent(this.mSubId, isCapable, mode);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public int getVoWiFiModeSetting() {
        try {
            return getITelephony().getVoWiFiModeSetting(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVoWiFiModeSetting(int mode) {
        try {
            getITelephony().setVoWiFiModeSetting(this.mSubId, mode);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public int getVoWiFiRoamingModeSetting() {
        try {
            return getITelephony().getVoWiFiRoamingModeSetting(this.mSubId);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setVoWiFiRoamingModeSetting(int mode) {
        try {
            getITelephony().setVoWiFiRoamingModeSetting(this.mSubId, mode);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    public void setRttCapabilitySetting(boolean isEnabled) {
        try {
            getITelephony().setRttCapabilitySetting(this.mSubId, isEnabled);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    boolean isTtyOverVolteEnabled() {
        try {
            return getITelephony().isTtyOverVolteEnabled(this.mSubId);
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
