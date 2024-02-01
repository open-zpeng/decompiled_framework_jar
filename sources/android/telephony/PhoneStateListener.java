package android.telephony;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.ImsReasonInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.IPhoneStateListener;
import com.android.internal.util.FunctionalUtils;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public class PhoneStateListener {
    private static final boolean DBG = false;
    public static final int LISTEN_ACTIVE_DATA_SUBSCRIPTION_ID_CHANGE = 4194304;
    @SystemApi
    public static final int LISTEN_CALL_ATTRIBUTES_CHANGED = 67108864;
    @SystemApi
    public static final int LISTEN_CALL_DISCONNECT_CAUSES = 33554432;
    public static final int LISTEN_CALL_FORWARDING_INDICATOR = 8;
    public static final int LISTEN_CALL_STATE = 32;
    public static final int LISTEN_CARRIER_NETWORK_CHANGE = 65536;
    public static final int LISTEN_CELL_INFO = 1024;
    public static final int LISTEN_CELL_LOCATION = 16;
    public static final int LISTEN_DATA_ACTIVATION_STATE = 262144;
    public static final int LISTEN_DATA_ACTIVITY = 128;
    @Deprecated
    public static final int LISTEN_DATA_CONNECTION_REAL_TIME_INFO = 8192;
    public static final int LISTEN_DATA_CONNECTION_STATE = 64;
    public static final int LISTEN_EMERGENCY_NUMBER_LIST = 16777216;
    @SystemApi
    public static final int LISTEN_IMS_CALL_DISCONNECT_CAUSES = 134217728;
    public static final int LISTEN_MESSAGE_WAITING_INDICATOR = 4;
    public static final int LISTEN_NONE = 0;
    @Deprecated
    public static final int LISTEN_OEM_HOOK_RAW_EVENT = 32768;
    public static final int LISTEN_OTASP_CHANGED = 512;
    public static final int LISTEN_PHONE_CAPABILITY_CHANGE = 2097152;
    public static final int LISTEN_PHYSICAL_CHANNEL_CONFIGURATION = 1048576;
    @SystemApi
    public static final int LISTEN_PRECISE_CALL_STATE = 2048;
    @SystemApi
    public static final int LISTEN_PRECISE_DATA_CONNECTION_STATE = 4096;
    @SystemApi
    public static final int LISTEN_RADIO_POWER_STATE_CHANGED = 8388608;
    public static final int LISTEN_SERVICE_STATE = 1;
    @Deprecated
    public static final int LISTEN_SIGNAL_STRENGTH = 2;
    public static final int LISTEN_SIGNAL_STRENGTHS = 256;
    @SystemApi
    public static final int LISTEN_SRVCC_STATE_CHANGED = 16384;
    public static final int LISTEN_USER_MOBILE_DATA_STATE = 524288;
    @SystemApi
    public static final int LISTEN_VOICE_ACTIVATION_STATE = 131072;
    private static final String LOG_TAG = "PhoneStateListener";
    public static final int PER_PID_REGISTRATION_LIMIT = 50;
    @UnsupportedAppUsage
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public final IPhoneStateListener callback;
    @UnsupportedAppUsage
    protected Integer mSubId;

    public PhoneStateListener() {
        this((Integer) null, Looper.myLooper());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public PhoneStateListener(Looper looper) {
        this((Integer) null, looper);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public PhoneStateListener(Integer subId) {
        this(subId, Looper.myLooper());
        if (subId != null && VMRuntime.getRuntime().getTargetSdkVersion() >= 29) {
            throw new IllegalArgumentException("PhoneStateListener with subId: " + subId + " is not supported, use default constructor");
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public PhoneStateListener(Integer subId, Looper looper) {
        this(subId, new HandlerExecutor(new Handler(looper)));
        if (subId != null && VMRuntime.getRuntime().getTargetSdkVersion() >= 29) {
            throw new IllegalArgumentException("PhoneStateListener with subId: " + subId + " is not supported, use default constructor");
        }
    }

    public PhoneStateListener(Executor executor) {
        this((Integer) null, executor);
    }

    private PhoneStateListener(Integer subId, Executor e) {
        if (e == null) {
            throw new IllegalArgumentException("PhoneStateListener Executor must be non-null");
        }
        this.mSubId = subId;
        this.callback = new IPhoneStateListenerStub(this, e);
    }

    public void onServiceStateChanged(ServiceState serviceState) {
    }

    @Deprecated
    public void onSignalStrengthChanged(int asu) {
    }

    public void onMessageWaitingIndicatorChanged(boolean mwi) {
    }

    public void onCallForwardingIndicatorChanged(boolean cfi) {
    }

    public void onCellLocationChanged(CellLocation location) {
    }

    public void onCallStateChanged(int state, String phoneNumber) {
    }

    public void onDataConnectionStateChanged(int state) {
    }

    public void onDataConnectionStateChanged(int state, int networkType) {
    }

    public void onDataActivity(int direction) {
    }

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
    }

    @UnsupportedAppUsage
    public void onOtaspChanged(int otaspMode) {
    }

    public void onCellInfoChanged(List<CellInfo> cellInfo) {
    }

    @SystemApi
    public void onPreciseCallStateChanged(PreciseCallState callState) {
    }

    @SystemApi
    public void onCallDisconnectCauseChanged(int disconnectCause, int preciseDisconnectCause) {
    }

    @SystemApi
    public void onImsCallDisconnectCauseChanged(ImsReasonInfo imsReasonInfo) {
    }

    @SystemApi
    public void onPreciseDataConnectionStateChanged(PreciseDataConnectionState dataConnectionState) {
    }

    @UnsupportedAppUsage
    public void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo dcRtInfo) {
    }

    @SystemApi
    public void onSrvccStateChanged(int srvccState) {
    }

    @SystemApi
    public void onVoiceActivationStateChanged(int state) {
    }

    public void onDataActivationStateChanged(int state) {
    }

    public void onUserMobileDataStateChanged(boolean enabled) {
    }

    public void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> configs) {
    }

    public void onEmergencyNumberListChanged(Map<Integer, List<EmergencyNumber>> emergencyNumberList) {
    }

    @UnsupportedAppUsage
    public void onOemHookRawEvent(byte[] rawData) {
    }

    public void onPhoneCapabilityChanged(PhoneCapability capability) {
    }

    public void onActiveDataSubscriptionIdChanged(int subId) {
    }

    @SystemApi
    public void onCallAttributesChanged(CallAttributes callAttributes) {
    }

    @SystemApi
    public void onRadioPowerStateChanged(int state) {
    }

    public void onCarrierNetworkChange(boolean active) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class IPhoneStateListenerStub extends IPhoneStateListener.Stub {
        private Executor mExecutor;
        private WeakReference<PhoneStateListener> mPhoneStateListenerWeakRef;

        IPhoneStateListenerStub(PhoneStateListener phoneStateListener, Executor executor) {
            this.mPhoneStateListenerWeakRef = new WeakReference<>(phoneStateListener);
            this.mExecutor = executor;
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onServiceStateChanged(final ServiceState serviceState) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$uC5syhzl229gIpaK7Jfs__OCJxQ
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onServiceStateChanged$1$PhoneStateListener$IPhoneStateListenerStub(psl, serviceState);
                }
            });
        }

        public /* synthetic */ void lambda$onServiceStateChanged$1$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final ServiceState serviceState) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nrGqSRBJrc3_EwotCDNwfKeizIo
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onServiceStateChanged(serviceState);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onSignalStrengthChanged(final int asu) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$M39is_Zyt8D7Camw2NS4EGTDn-s
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onSignalStrengthChanged$3$PhoneStateListener$IPhoneStateListenerStub(psl, asu);
                }
            });
        }

        public /* synthetic */ void lambda$onSignalStrengthChanged$3$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int asu) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5J-sdvem6pUpdVwRdm8IbDhvuv8
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onSignalStrengthChanged(asu);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onMessageWaitingIndicatorChanged(final boolean mwi) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$okPCYOx4UxYuvUHlM2iS425QGIg
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onMessageWaitingIndicatorChanged$5$PhoneStateListener$IPhoneStateListenerStub(psl, mwi);
                }
            });
        }

        public /* synthetic */ void lambda$onMessageWaitingIndicatorChanged$5$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final boolean mwi) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$TqrkuLPlaG_ucU7VbLS4tnf8hG8
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onMessageWaitingIndicatorChanged(mwi);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onCallForwardingIndicatorChanged(final boolean cfi) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$1M3m0i6211i2YjWyTDT7l0bJm3I
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onCallForwardingIndicatorChanged$7$PhoneStateListener$IPhoneStateListenerStub(psl, cfi);
                }
            });
        }

        public /* synthetic */ void lambda$onCallForwardingIndicatorChanged$7$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final boolean cfi) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$WYWtWHdkZDxBd9anjoxyZozPWHc
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onCallForwardingIndicatorChanged(cfi);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onCellLocationChanged(Bundle bundle) {
            final CellLocation location = CellLocation.newFromBundle(bundle);
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Hbn6-eZxY2p3rjOfStodI04A8E8
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onCellLocationChanged$9$PhoneStateListener$IPhoneStateListenerStub(psl, location);
                }
            });
        }

        public /* synthetic */ void lambda$onCellLocationChanged$9$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final CellLocation location) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$2cMrwdqnKBpixpApeIX38rmRLak
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onCellLocationChanged(location);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onCallStateChanged(final int state, final String incomingNumber) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$oDAZqs8paeefe_3k_uRKV5plQW4
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onCallStateChanged$11$PhoneStateListener$IPhoneStateListenerStub(psl, state, incomingNumber);
                }
            });
        }

        public /* synthetic */ void lambda$onCallStateChanged$11$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int state, final String incomingNumber) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$6czWSGzxct0CXPVO54T0aq05qls
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onCallStateChanged(state, incomingNumber);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onDataConnectionStateChanged(final int state, final int networkType) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$2VMO21pFQN-JN3kpn6vQN1zPFEU
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onDataConnectionStateChanged$13$PhoneStateListener$IPhoneStateListenerStub(psl, state, networkType);
                }
            });
        }

        public /* synthetic */ void lambda$onDataConnectionStateChanged$13$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int state, final int networkType) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$dUc3j82sK9P9Zpaq-91n9bk_Rpc
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.IPhoneStateListenerStub.lambda$onDataConnectionStateChanged$12(PhoneStateListener.this, state, networkType);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onDataConnectionStateChanged$12(PhoneStateListener psl, int state, int networkType) {
            psl.onDataConnectionStateChanged(state, networkType);
            psl.onDataConnectionStateChanged(state);
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onDataActivity(final int direction) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$XyayAGWQZC2dNjwr697SfSGBBOc
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onDataActivity$15$PhoneStateListener$IPhoneStateListenerStub(psl, direction);
                }
            });
        }

        public /* synthetic */ void lambda$onDataActivity$15$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int direction) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$JalixlMNdjktPsNntP_JT9pymhs
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onDataActivity(direction);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onSignalStrengthsChanged(final SignalStrength signalStrength) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$aysbwPqxcLV_5w6LP0TzZu2D-ew
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onSignalStrengthsChanged$17$PhoneStateListener$IPhoneStateListenerStub(psl, signalStrength);
                }
            });
        }

        public /* synthetic */ void lambda$onSignalStrengthsChanged$17$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final SignalStrength signalStrength) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$0s34qsuHFsa43jUHrTkD62ni6Ds
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onSignalStrengthsChanged(signalStrength);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onOtaspChanged(final int otaspMode) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$i4r8mBqOfCy4bnbF_JG7ujDXEOQ
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onOtaspChanged$19$PhoneStateListener$IPhoneStateListenerStub(psl, otaspMode);
                }
            });
        }

        public /* synthetic */ void lambda$onOtaspChanged$19$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int otaspMode) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$H1CbxYUcdxs1WggP_RRULTY01K8
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onOtaspChanged(otaspMode);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onCellInfoChanged(final List<CellInfo> cellInfo) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$yvQnAlFGg5EWDG2vcA9X-4xnalA
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onCellInfoChanged$21$PhoneStateListener$IPhoneStateListenerStub(psl, cellInfo);
                }
            });
        }

        public /* synthetic */ void lambda$onCellInfoChanged$21$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final List cellInfo) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Q2A8FgYlU8_D6PD78tThGut_rTc
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onCellInfoChanged(cellInfo);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onPreciseCallStateChanged(final PreciseCallState callState) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$bELzxgwsPigyVKYkAXBO2BjcSm8
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onPreciseCallStateChanged$23$PhoneStateListener$IPhoneStateListenerStub(psl, callState);
                }
            });
        }

        public /* synthetic */ void lambda$onPreciseCallStateChanged$23$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final PreciseCallState callState) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$4NHt5Shg_DHV-T1IxfcQLHP5-j0
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onPreciseCallStateChanged(callState);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onCallDisconnectCauseChanged(final int disconnectCause, final int preciseDisconnectCause) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$icX71zgNszuMfnDaCmahcqWacFM
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onCallDisconnectCauseChanged$25$PhoneStateListener$IPhoneStateListenerStub(psl, disconnectCause, preciseDisconnectCause);
                }
            });
        }

        public /* synthetic */ void lambda$onCallDisconnectCauseChanged$25$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int disconnectCause, final int preciseDisconnectCause) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$hxq77a5O_MUfoptHg15ipzFvMkI
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onCallDisconnectCauseChanged(disconnectCause, preciseDisconnectCause);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onPreciseDataConnectionStateChanged(final PreciseDataConnectionState dataConnectionState) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$RC2x2ijetA-pQrLa4QakzMBjh_k
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onPreciseDataConnectionStateChanged$27$PhoneStateListener$IPhoneStateListenerStub(psl, dataConnectionState);
                }
            });
        }

        public /* synthetic */ void lambda$onPreciseDataConnectionStateChanged$27$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final PreciseDataConnectionState dataConnectionState) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$HEcWn-J1WRb0wLERu2qoMIZDfjY
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onPreciseDataConnectionStateChanged(dataConnectionState);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onDataConnectionRealTimeInfoChanged(final DataConnectionRealTimeInfo dcRtInfo) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$OfwFKKtcQHRmtv70FCopw6FDAAU
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onDataConnectionRealTimeInfoChanged$29$PhoneStateListener$IPhoneStateListenerStub(psl, dcRtInfo);
                }
            });
        }

        public /* synthetic */ void lambda$onDataConnectionRealTimeInfoChanged$29$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final DataConnectionRealTimeInfo dcRtInfo) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$IU278K5QbmReF-mbpcNVAvVlhFI
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onDataConnectionRealTimeInfoChanged(dcRtInfo);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onSrvccStateChanged(final int state) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nR7W5ox6SCgPxtH9IRcENwKeFI4
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onSrvccStateChanged$31$PhoneStateListener$IPhoneStateListenerStub(psl, state);
                }
            });
        }

        public /* synthetic */ void lambda$onSrvccStateChanged$31$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int state) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$ygzOWFRiY4sZQ4WYUPIefqgiGvM
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onSrvccStateChanged(state);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onVoiceActivationStateChanged(final int activationState) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5rF2IFj8mrb7uZc0HMKiuCodUn0
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onVoiceActivationStateChanged$33$PhoneStateListener$IPhoneStateListenerStub(psl, activationState);
                }
            });
        }

        public /* synthetic */ void lambda$onVoiceActivationStateChanged$33$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int activationState) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$y-tK7my_uXPo_oQ7AytfnekGEbU
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onVoiceActivationStateChanged(activationState);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onDataActivationStateChanged(final int activationState) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$t2gWJ_jA36kAdNXSmlzw85aU-tM
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onDataActivationStateChanged$35$PhoneStateListener$IPhoneStateListenerStub(psl, activationState);
                }
            });
        }

        public /* synthetic */ void lambda$onDataActivationStateChanged$35$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int activationState) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$W65ui1dCCc-JnQa7gon1I7Bz7Sk
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onDataActivationStateChanged(activationState);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onUserMobileDataStateChanged(final boolean enabled) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5uu-05j4ojTh9mEHkN-ynQqQRGM
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onUserMobileDataStateChanged$37$PhoneStateListener$IPhoneStateListenerStub(psl, enabled);
                }
            });
        }

        public /* synthetic */ void lambda$onUserMobileDataStateChanged$37$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final boolean enabled) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5Uf5OZWCyPD0lZtySzbYw18FWhU
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onUserMobileDataStateChanged(enabled);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onOemHookRawEvent(final byte[] rawData) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jNtyZYh5ZAuvyDZA_6f30zhW_dI
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onOemHookRawEvent$39$PhoneStateListener$IPhoneStateListenerStub(psl, rawData);
                }
            });
        }

        public /* synthetic */ void lambda$onOemHookRawEvent$39$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final byte[] rawData) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jclAV5yU3RtV94suRvvhafvGuhw
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onOemHookRawEvent(rawData);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onCarrierNetworkChange(final boolean active) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$YY3srkIkMm8vTSFJZHoiKzUUrGs
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onCarrierNetworkChange$41$PhoneStateListener$IPhoneStateListenerStub(psl, active);
                }
            });
        }

        public /* synthetic */ void lambda$onCarrierNetworkChange$41$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final boolean active) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jlNX9JiqGSNg9W49vDcKucKdeCI
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onCarrierNetworkChange(active);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onPhysicalChannelConfigurationChanged(final List<PhysicalChannelConfig> configs) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$OIAjnTzp_YIf6Y7jPFABi9BXZvs
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onPhysicalChannelConfigurationChanged$43$PhoneStateListener$IPhoneStateListenerStub(psl, configs);
                }
            });
        }

        public /* synthetic */ void lambda$onPhysicalChannelConfigurationChanged$43$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final List configs) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nMiL2eSbUjYsM-AZ8joz_n4dLz0
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onPhysicalChannelConfigurationChanged(configs);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onEmergencyNumberListChanged(final Map emergencyNumberList) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$d9DVwzLraeX80tegF_wEzf_k2FI
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onEmergencyNumberListChanged$45$PhoneStateListener$IPhoneStateListenerStub(psl, emergencyNumberList);
                }
            });
        }

        public /* synthetic */ void lambda$onEmergencyNumberListChanged$45$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final Map emergencyNumberList) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jGj-qFMdpjbsKaUErqJEeOALEGo
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onEmergencyNumberListChanged(emergencyNumberList);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onPhoneCapabilityChanged(final PhoneCapability capability) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$-CiOzgf6ys4EwlCYOVUsuz9YQ5c
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onPhoneCapabilityChanged$47$PhoneStateListener$IPhoneStateListenerStub(psl, capability);
                }
            });
        }

        public /* synthetic */ void lambda$onPhoneCapabilityChanged$47$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final PhoneCapability capability) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$lHL69WZlO89JjNC1LLvFWp2OuKY
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onPhoneCapabilityChanged(capability);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onRadioPowerStateChanged(final int state) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$TYOBpOfoS3xjFssrzOJyHTelndw
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onRadioPowerStateChanged$49$PhoneStateListener$IPhoneStateListenerStub(psl, state);
                }
            });
        }

        public /* synthetic */ void lambda$onRadioPowerStateChanged$49$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int state) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$bI97h5HT-IYvguXIcngwUrpGxrw
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onRadioPowerStateChanged(state);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onCallAttributesChanged(final CallAttributes callAttributes) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Q_Cpm8aB8qYt8lGxD5PXek_-4bA
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onCallAttributesChanged$51$PhoneStateListener$IPhoneStateListenerStub(psl, callAttributes);
                }
            });
        }

        public /* synthetic */ void lambda$onCallAttributesChanged$51$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final CallAttributes callAttributes) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5t7yF_frkRH7MdItRlwmP00irsM
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onCallAttributesChanged(callAttributes);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onActiveDataSubIdChanged(final int subId) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$ipH9N0fJiGE9EBJHahQeXcCZXzo
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onActiveDataSubIdChanged$53$PhoneStateListener$IPhoneStateListenerStub(psl, subId);
                }
            });
        }

        public /* synthetic */ void lambda$onActiveDataSubIdChanged$53$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final int subId) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nnG75RvQ1_1KZGJk1ySeCH1JJRg
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onActiveDataSubscriptionIdChanged(subId);
                }
            });
        }

        @Override // com.android.internal.telephony.IPhoneStateListener
        public void onImsCallDisconnectCauseChanged(final ImsReasonInfo disconnectCause) {
            final PhoneStateListener psl = this.mPhoneStateListenerWeakRef.get();
            if (psl == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Bzok3Q_pjLC0O4ulkDfbWru0v6w
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    PhoneStateListener.IPhoneStateListenerStub.this.lambda$onImsCallDisconnectCauseChanged$55$PhoneStateListener$IPhoneStateListenerStub(psl, disconnectCause);
                }
            });
        }

        public /* synthetic */ void lambda$onImsCallDisconnectCauseChanged$55$PhoneStateListener$IPhoneStateListenerStub(final PhoneStateListener psl, final ImsReasonInfo disconnectCause) throws Exception {
            this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$PhoneStateListener$IPhoneStateListenerStub$eYTgM6ABgThWqEatVha4ZuIpI0A
                @Override // java.lang.Runnable
                public final void run() {
                    PhoneStateListener.this.onImsCallDisconnectCauseChanged(disconnectCause);
                }
            });
        }
    }

    private void log(String s) {
        Rlog.d(LOG_TAG, s);
    }
}
