package android.telephony.ims.feature;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsMmTelListener;
import android.telephony.ims.aidl.IImsSmsListener;
import android.telephony.ims.feature.ImsFeature;
import android.telephony.ims.stub.ImsCallSessionImplBase;
import android.telephony.ims.stub.ImsEcbmImplBase;
import android.telephony.ims.stub.ImsMultiEndpointImplBase;
import android.telephony.ims.stub.ImsSmsImplBase;
import android.telephony.ims.stub.ImsUtImplBase;
import android.util.Log;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsUt;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
/* loaded from: classes2.dex */
public class MmTelFeature extends ImsFeature {
    private static final String LOG_TAG = "MmTelFeature";
    public static final int PROCESS_CALL_CSFB = 1;
    public static final int PROCESS_CALL_IMS = 0;
    private final IImsMmTelFeature mImsMMTelBinder = new IImsMmTelFeature.Stub() { // from class: android.telephony.ims.feature.MmTelFeature.1
        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void setListener(IImsMmTelListener l) {
            MmTelFeature.this.setListener(l);
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public int getFeatureState() throws RemoteException {
            try {
                return MmTelFeature.this.getFeatureState();
            } catch (Exception e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public ImsCallProfile createCallProfile(int callSessionType, int callType) throws RemoteException {
            ImsCallProfile createCallProfile;
            synchronized (MmTelFeature.this.mLock) {
                try {
                    try {
                        createCallProfile = MmTelFeature.this.createCallProfile(callSessionType, callType);
                    } catch (Exception e) {
                        throw new RemoteException(e.getMessage());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return createCallProfile;
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public IImsCallSession createCallSession(ImsCallProfile profile) throws RemoteException {
            IImsCallSession createCallSessionInterface;
            synchronized (MmTelFeature.this.mLock) {
                createCallSessionInterface = MmTelFeature.this.createCallSessionInterface(profile);
            }
            return createCallSessionInterface;
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public int shouldProcessCall(String[] numbers) {
            int shouldProcessCall;
            synchronized (MmTelFeature.this.mLock) {
                shouldProcessCall = MmTelFeature.this.shouldProcessCall(numbers);
            }
            return shouldProcessCall;
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public IImsUt getUtInterface() throws RemoteException {
            IImsUt utInterface;
            synchronized (MmTelFeature.this.mLock) {
                utInterface = MmTelFeature.this.getUtInterface();
            }
            return utInterface;
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public IImsEcbm getEcbmInterface() throws RemoteException {
            IImsEcbm ecbmInterface;
            synchronized (MmTelFeature.this.mLock) {
                ecbmInterface = MmTelFeature.this.getEcbmInterface();
            }
            return ecbmInterface;
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void setUiTtyMode(int uiTtyMode, Message onCompleteMessage) throws RemoteException {
            synchronized (MmTelFeature.this.mLock) {
                try {
                    try {
                        MmTelFeature.this.setUiTtyMode(uiTtyMode, onCompleteMessage);
                    } catch (Exception e) {
                        throw new RemoteException(e.getMessage());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException {
            IImsMultiEndpoint multiEndpointInterface;
            synchronized (MmTelFeature.this.mLock) {
                multiEndpointInterface = MmTelFeature.this.getMultiEndpointInterface();
            }
            return multiEndpointInterface;
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public int queryCapabilityStatus() {
            return MmTelFeature.this.queryCapabilityStatus().mCapabilities;
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void addCapabilityCallback(IImsCapabilityCallback c) {
            MmTelFeature.this.addCapabilityCallback(c);
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void removeCapabilityCallback(IImsCapabilityCallback c) {
            MmTelFeature.this.removeCapabilityCallback(c);
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void changeCapabilitiesConfiguration(CapabilityChangeRequest request, IImsCapabilityCallback c) {
            synchronized (MmTelFeature.this.mLock) {
                MmTelFeature.this.requestChangeEnabledCapabilities(request, c);
            }
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void queryCapabilityConfiguration(int capability, int radioTech, IImsCapabilityCallback c) {
            synchronized (MmTelFeature.this.mLock) {
                MmTelFeature.this.queryCapabilityConfigurationInternal(capability, radioTech, c);
            }
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void setSmsListener(IImsSmsListener l) {
            MmTelFeature.this.setSmsListener(l);
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void sendSms(int token, int messageRef, String format, String smsc, boolean retry, byte[] pdu) {
            synchronized (MmTelFeature.this.mLock) {
                MmTelFeature.this.sendSms(token, messageRef, format, smsc, retry, pdu);
            }
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void acknowledgeSms(int token, int messageRef, int result) {
            synchronized (MmTelFeature.this.mLock) {
                MmTelFeature.this.acknowledgeSms(token, messageRef, result);
            }
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void acknowledgeSmsReport(int token, int messageRef, int result) {
            synchronized (MmTelFeature.this.mLock) {
                MmTelFeature.this.acknowledgeSmsReport(token, messageRef, result);
            }
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public String getSmsFormat() {
            String smsFormat;
            synchronized (MmTelFeature.this.mLock) {
                smsFormat = MmTelFeature.this.getSmsFormat();
            }
            return smsFormat;
        }

        @Override // android.telephony.ims.aidl.IImsMmTelFeature
        public void onSmsReady() {
            synchronized (MmTelFeature.this.mLock) {
                MmTelFeature.this.onSmsReady();
            }
        }
    };
    private IImsMmTelListener mListener;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ProcessCallResult {
    }

    /* loaded from: classes2.dex */
    public static class MmTelCapabilities extends ImsFeature.Capabilities {
        public static final int CAPABILITY_TYPE_SMS = 8;
        public static final int CAPABILITY_TYPE_UT = 4;
        public static final int CAPABILITY_TYPE_VIDEO = 2;
        public static final int CAPABILITY_TYPE_VOICE = 1;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface MmTelCapability {
        }

        public MmTelCapabilities() {
        }

        @Deprecated
        public MmTelCapabilities(ImsFeature.Capabilities c) {
            this.mCapabilities = c.mCapabilities;
        }

        public MmTelCapabilities(int capabilities) {
            this.mCapabilities = capabilities;
        }

        @Override // android.telephony.ims.feature.ImsFeature.Capabilities
        public final void addCapabilities(int capabilities) {
            super.addCapabilities(capabilities);
        }

        @Override // android.telephony.ims.feature.ImsFeature.Capabilities
        public final void removeCapabilities(int capability) {
            super.removeCapabilities(capability);
        }

        @Override // android.telephony.ims.feature.ImsFeature.Capabilities
        public final boolean isCapable(int capabilities) {
            return super.isCapable(capabilities);
        }

        @Override // android.telephony.ims.feature.ImsFeature.Capabilities
        public String toString() {
            return "MmTel Capabilities - [Voice: " + isCapable(1) + " Video: " + isCapable(2) + " UT: " + isCapable(4) + " SMS: " + isCapable(8) + "]";
        }
    }

    /* loaded from: classes2.dex */
    public static class Listener extends IImsMmTelListener.Stub {
        @Override // android.telephony.ims.aidl.IImsMmTelListener
        public void onIncomingCall(IImsCallSession c, Bundle extras) {
        }

        @Override // android.telephony.ims.aidl.IImsMmTelListener
        public void onRejectedCall(ImsCallProfile callProfile, ImsReasonInfo reason) {
        }

        @Override // android.telephony.ims.aidl.IImsMmTelListener
        public void onVoiceMessageCountUpdate(int count) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setListener(IImsMmTelListener listener) {
        synchronized (this.mLock) {
            this.mListener = listener;
            if (this.mListener != null) {
                onFeatureReady();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queryCapabilityConfigurationInternal(int capability, int radioTech, IImsCapabilityCallback c) {
        boolean enabled = queryCapabilityConfiguration(capability, radioTech);
        if (c != null) {
            try {
                c.onQueryCapabilityConfiguration(capability, radioTech, enabled);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "queryCapabilityConfigurationInternal called on dead binder!");
            }
        }
    }

    @Override // android.telephony.ims.feature.ImsFeature
    public final MmTelCapabilities queryCapabilityStatus() {
        return new MmTelCapabilities(super.queryCapabilityStatus());
    }

    public final void notifyCapabilitiesStatusChanged(MmTelCapabilities c) {
        if (c == null) {
            throw new IllegalArgumentException("MmTelCapabilities must be non-null!");
        }
        super.notifyCapabilitiesStatusChanged((ImsFeature.Capabilities) c);
    }

    public final void notifyIncomingCall(ImsCallSessionImplBase c, Bundle extras) {
        if (c == null || extras == null) {
            throw new IllegalArgumentException("ImsCallSessionImplBase and Bundle can not be null.");
        }
        synchronized (this.mLock) {
            if (this.mListener == null) {
                throw new IllegalStateException("Session is not available.");
            }
            try {
                this.mListener.onIncomingCall(c.getServiceImpl(), extras);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public final void notifyRejectedCall(ImsCallProfile callProfile, ImsReasonInfo reason) {
        if (callProfile == null || reason == null) {
            throw new IllegalArgumentException("ImsCallProfile and ImsReasonInfo must not be null.");
        }
        synchronized (this.mLock) {
            if (this.mListener == null) {
                throw new IllegalStateException("Session is not available.");
            }
            try {
                this.mListener.onRejectedCall(callProfile, reason);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public final void notifyIncomingCallSession(IImsCallSession c, Bundle extras) {
        synchronized (this.mLock) {
            if (this.mListener == null) {
                throw new IllegalStateException("Session is not available.");
            }
            try {
                this.mListener.onIncomingCall(c, extras);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public final void notifyVoiceMessageCountUpdate(int count) {
        synchronized (this.mLock) {
            if (this.mListener == null) {
                throw new IllegalStateException("Session is not available.");
            }
            try {
                this.mListener.onVoiceMessageCountUpdate(count);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean queryCapabilityConfiguration(int capability, int radioTech) {
        return false;
    }

    @Override // android.telephony.ims.feature.ImsFeature
    public void changeEnabledCapabilities(CapabilityChangeRequest request, ImsFeature.CapabilityCallbackProxy c) {
    }

    public ImsCallProfile createCallProfile(int callSessionType, int callType) {
        return null;
    }

    public IImsCallSession createCallSessionInterface(ImsCallProfile profile) throws RemoteException {
        ImsCallSessionImplBase s = createCallSession(profile);
        if (s != null) {
            return s.getServiceImpl();
        }
        return null;
    }

    public ImsCallSessionImplBase createCallSession(ImsCallProfile profile) {
        return null;
    }

    public int shouldProcessCall(String[] numbers) {
        return 0;
    }

    protected IImsUt getUtInterface() throws RemoteException {
        ImsUtImplBase utImpl = getUt();
        if (utImpl != null) {
            return utImpl.getInterface();
        }
        return null;
    }

    protected IImsEcbm getEcbmInterface() throws RemoteException {
        ImsEcbmImplBase ecbmImpl = getEcbm();
        if (ecbmImpl != null) {
            return ecbmImpl.getImsEcbm();
        }
        return null;
    }

    public IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException {
        ImsMultiEndpointImplBase multiendpointImpl = getMultiEndpoint();
        if (multiendpointImpl != null) {
            return multiendpointImpl.getIImsMultiEndpoint();
        }
        return null;
    }

    public ImsUtImplBase getUt() {
        return new ImsUtImplBase();
    }

    public ImsEcbmImplBase getEcbm() {
        return new ImsEcbmImplBase();
    }

    public ImsMultiEndpointImplBase getMultiEndpoint() {
        return new ImsMultiEndpointImplBase();
    }

    public void setUiTtyMode(int mode, Message onCompleteMessage) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSmsListener(IImsSmsListener listener) {
        getSmsImplementation().registerSmsListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSms(int token, int messageRef, String format, String smsc, boolean isRetry, byte[] pdu) {
        getSmsImplementation().sendSms(token, messageRef, format, smsc, isRetry, pdu);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void acknowledgeSms(int token, int messageRef, int result) {
        getSmsImplementation().acknowledgeSms(token, messageRef, result);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void acknowledgeSmsReport(int token, int messageRef, int result) {
        getSmsImplementation().acknowledgeSmsReport(token, messageRef, result);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSmsReady() {
        getSmsImplementation().onReady();
    }

    public ImsSmsImplBase getSmsImplementation() {
        return new ImsSmsImplBase();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getSmsFormat() {
        return getSmsImplementation().getSmsFormat();
    }

    @Override // android.telephony.ims.feature.ImsFeature
    public void onFeatureRemoved() {
    }

    @Override // android.telephony.ims.feature.ImsFeature
    public void onFeatureReady() {
    }

    @Override // android.telephony.ims.feature.ImsFeature
    public final IImsMmTelFeature getBinder() {
        return this.mImsMMTelBinder;
    }
}
