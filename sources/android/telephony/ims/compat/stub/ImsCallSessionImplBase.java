package android.telephony.ims.compat.stub;

import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsConferenceState;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsSuppServiceNotification;
import android.telephony.ims.aidl.IImsCallSessionListener;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSessionListener;
import com.android.ims.internal.IImsVideoCallProvider;
/* loaded from: classes2.dex */
public class ImsCallSessionImplBase extends IImsCallSession.Stub {
    private protected ImsCallSessionImplBase() {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public final synchronized void setListener(IImsCallSessionListener listener) throws RemoteException {
        setListener(new ImsCallSessionListenerConverter(listener));
    }

    public synchronized void setListener(com.android.ims.internal.IImsCallSessionListener listener) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void close() {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized String getCallId() {
        return null;
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized ImsCallProfile getCallProfile() {
        return null;
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized ImsCallProfile getLocalCallProfile() {
        return null;
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized ImsCallProfile getRemoteCallProfile() {
        return null;
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized String getProperty(String name) {
        return null;
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized int getState() {
        return -1;
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized boolean isInCall() {
        return false;
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void setMute(boolean muted) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void start(String callee, ImsCallProfile profile) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void startConference(String[] participants, ImsCallProfile profile) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void accept(int callType, ImsStreamMediaProfile profile) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void deflect(String deflectNumber) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void reject(int reason) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void terminate(int reason) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void hold(ImsStreamMediaProfile profile) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void resume(ImsStreamMediaProfile profile) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void merge() {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void update(int callType, ImsStreamMediaProfile profile) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void extendToConference(String[] participants) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void inviteParticipants(String[] participants) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void removeParticipants(String[] participants) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void sendDtmf(char c, Message result) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void startDtmf(char c) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void stopDtmf() {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void sendUssd(String ussdMessage) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized IImsVideoCallProvider getVideoCallProvider() {
        return null;
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized boolean isMultiparty() {
        return false;
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void sendRttModifyRequest(ImsCallProfile toProfile) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void sendRttModifyResponse(boolean status) {
    }

    @Override // com.android.ims.internal.IImsCallSession
    public synchronized void sendRttMessage(String rttMessage) {
    }

    /* loaded from: classes2.dex */
    private class ImsCallSessionListenerConverter extends IImsCallSessionListener.Stub {
        private final android.telephony.ims.aidl.IImsCallSessionListener mNewListener;

        public ImsCallSessionListenerConverter(android.telephony.ims.aidl.IImsCallSessionListener listener) {
            this.mNewListener = listener;
        }

        public synchronized void callSessionProgressing(IImsCallSession i, ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException {
            this.mNewListener.callSessionProgressing(imsStreamMediaProfile);
        }

        public synchronized void callSessionStarted(IImsCallSession i, ImsCallProfile imsCallProfile) throws RemoteException {
            this.mNewListener.callSessionInitiated(imsCallProfile);
        }

        public synchronized void callSessionStartFailed(IImsCallSession i, ImsReasonInfo imsReasonInfo) throws RemoteException {
            this.mNewListener.callSessionInitiatedFailed(imsReasonInfo);
        }

        public synchronized void callSessionTerminated(IImsCallSession i, ImsReasonInfo imsReasonInfo) throws RemoteException {
            this.mNewListener.callSessionTerminated(imsReasonInfo);
        }

        public synchronized void callSessionHeld(IImsCallSession i, ImsCallProfile imsCallProfile) throws RemoteException {
            this.mNewListener.callSessionHeld(imsCallProfile);
        }

        public synchronized void callSessionHoldFailed(IImsCallSession i, ImsReasonInfo imsReasonInfo) throws RemoteException {
            this.mNewListener.callSessionHoldFailed(imsReasonInfo);
        }

        public synchronized void callSessionHoldReceived(IImsCallSession i, ImsCallProfile imsCallProfile) throws RemoteException {
            this.mNewListener.callSessionHoldReceived(imsCallProfile);
        }

        public synchronized void callSessionResumed(IImsCallSession i, ImsCallProfile imsCallProfile) throws RemoteException {
            this.mNewListener.callSessionResumed(imsCallProfile);
        }

        public synchronized void callSessionResumeFailed(IImsCallSession i, ImsReasonInfo imsReasonInfo) throws RemoteException {
            this.mNewListener.callSessionResumeFailed(imsReasonInfo);
        }

        public synchronized void callSessionResumeReceived(IImsCallSession i, ImsCallProfile imsCallProfile) throws RemoteException {
            this.mNewListener.callSessionResumeReceived(imsCallProfile);
        }

        public synchronized void callSessionMergeStarted(IImsCallSession i, IImsCallSession newSession, ImsCallProfile profile) throws RemoteException {
            this.mNewListener.callSessionMergeStarted(newSession, profile);
        }

        public synchronized void callSessionMergeComplete(IImsCallSession iImsCallSession) throws RemoteException {
            this.mNewListener.callSessionMergeComplete(iImsCallSession);
        }

        public synchronized void callSessionMergeFailed(IImsCallSession i, ImsReasonInfo imsReasonInfo) throws RemoteException {
            this.mNewListener.callSessionMergeFailed(imsReasonInfo);
        }

        public synchronized void callSessionUpdated(IImsCallSession i, ImsCallProfile imsCallProfile) throws RemoteException {
            this.mNewListener.callSessionUpdated(imsCallProfile);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionUpdateFailed(IImsCallSession i, ImsReasonInfo imsReasonInfo) throws RemoteException {
            this.mNewListener.callSessionUpdateFailed(imsReasonInfo);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionUpdateReceived(IImsCallSession i, ImsCallProfile imsCallProfile) throws RemoteException {
            this.mNewListener.callSessionUpdateReceived(imsCallProfile);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionConferenceExtended(IImsCallSession i, IImsCallSession newSession, ImsCallProfile imsCallProfile) throws RemoteException {
            this.mNewListener.callSessionConferenceExtended(newSession, imsCallProfile);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionConferenceExtendFailed(IImsCallSession i, ImsReasonInfo imsReasonInfo) throws RemoteException {
            this.mNewListener.callSessionConferenceExtendFailed(imsReasonInfo);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionConferenceExtendReceived(IImsCallSession i, IImsCallSession newSession, ImsCallProfile imsCallProfile) throws RemoteException {
            this.mNewListener.callSessionConferenceExtendReceived(newSession, imsCallProfile);
        }

        public synchronized void callSessionInviteParticipantsRequestDelivered(IImsCallSession i) throws RemoteException {
            this.mNewListener.callSessionInviteParticipantsRequestDelivered();
        }

        public synchronized void callSessionInviteParticipantsRequestFailed(IImsCallSession i, ImsReasonInfo imsReasonInfo) throws RemoteException {
            this.mNewListener.callSessionInviteParticipantsRequestFailed(imsReasonInfo);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionRemoveParticipantsRequestDelivered(IImsCallSession i) throws RemoteException {
            this.mNewListener.callSessionRemoveParticipantsRequestDelivered();
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionRemoveParticipantsRequestFailed(IImsCallSession i, ImsReasonInfo imsReasonInfo) throws RemoteException {
            this.mNewListener.callSessionRemoveParticipantsRequestFailed(imsReasonInfo);
        }

        public synchronized void callSessionConferenceStateUpdated(IImsCallSession i, ImsConferenceState imsConferenceState) throws RemoteException {
            this.mNewListener.callSessionConferenceStateUpdated(imsConferenceState);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionUssdMessageReceived(IImsCallSession i, int mode, String message) throws RemoteException {
            this.mNewListener.callSessionUssdMessageReceived(mode, message);
        }

        public synchronized void callSessionHandover(IImsCallSession i, int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) throws RemoteException {
            this.mNewListener.callSessionHandover(srcAccessTech, targetAccessTech, reasonInfo);
        }

        public synchronized void callSessionHandoverFailed(IImsCallSession i, int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) throws RemoteException {
            this.mNewListener.callSessionHandoverFailed(srcAccessTech, targetAccessTech, reasonInfo);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionMayHandover(IImsCallSession i, int srcAccessTech, int targetAccessTech) throws RemoteException {
            this.mNewListener.callSessionMayHandover(srcAccessTech, targetAccessTech);
        }

        public synchronized void callSessionTtyModeReceived(IImsCallSession iImsCallSession, int mode) throws RemoteException {
            this.mNewListener.callSessionTtyModeReceived(mode);
        }

        public synchronized void callSessionMultipartyStateChanged(IImsCallSession i, boolean isMultiparty) throws RemoteException {
            this.mNewListener.callSessionMultipartyStateChanged(isMultiparty);
        }

        public synchronized void callSessionSuppServiceReceived(IImsCallSession i, ImsSuppServiceNotification imsSuppServiceNotification) throws RemoteException {
            this.mNewListener.callSessionSuppServiceReceived(imsSuppServiceNotification);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionRttModifyRequestReceived(IImsCallSession i, ImsCallProfile imsCallProfile) throws RemoteException {
            this.mNewListener.callSessionRttModifyRequestReceived(imsCallProfile);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionRttModifyResponseReceived(int status) throws RemoteException {
            this.mNewListener.callSessionRttModifyResponseReceived(status);
        }

        @Override // com.android.ims.internal.IImsCallSessionListener
        public synchronized void callSessionRttMessageReceived(String rttMessage) throws RemoteException {
            this.mNewListener.callSessionRttMessageReceived(rttMessage);
        }
    }
}
