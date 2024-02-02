package android.telephony.ims;

import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsCallSessionListener;
import android.util.Log;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsVideoCallProvider;
import com.android.internal.telephony.IccCardConstants;
import java.util.Objects;
/* loaded from: classes2.dex */
public class ImsCallSession {
    private static final String TAG = "ImsCallSession";
    private boolean mClosed;
    private Listener mListener;
    private final IImsCallSession miSession;

    /* loaded from: classes2.dex */
    public static class State {
        public static final int ESTABLISHED = 4;
        public static final int ESTABLISHING = 3;
        public static final int IDLE = 0;
        public static final int INITIATED = 1;
        public static final int INVALID = -1;
        public static final int NEGOTIATING = 2;
        public static final int REESTABLISHING = 6;
        public static final int RENEGOTIATING = 5;
        public static final int TERMINATED = 8;
        public static final int TERMINATING = 7;

        public static synchronized String toString(int state) {
            switch (state) {
                case 0:
                    return "IDLE";
                case 1:
                    return "INITIATED";
                case 2:
                    return "NEGOTIATING";
                case 3:
                    return "ESTABLISHING";
                case 4:
                    return "ESTABLISHED";
                case 5:
                    return "RENEGOTIATING";
                case 6:
                    return "REESTABLISHING";
                case 7:
                    return "TERMINATING";
                case 8:
                    return "TERMINATED";
                default:
                    return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            }
        }

        private synchronized State() {
        }
    }

    /* loaded from: classes2.dex */
    public static class Listener {
        public synchronized void callSessionProgressing(ImsCallSession session, ImsStreamMediaProfile profile) {
        }

        public synchronized void callSessionStarted(ImsCallSession session, ImsCallProfile profile) {
        }

        public synchronized void callSessionStartFailed(ImsCallSession session, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionTerminated(ImsCallSession session, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionHeld(ImsCallSession session, ImsCallProfile profile) {
        }

        public synchronized void callSessionHoldFailed(ImsCallSession session, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionHoldReceived(ImsCallSession session, ImsCallProfile profile) {
        }

        public synchronized void callSessionResumed(ImsCallSession session, ImsCallProfile profile) {
        }

        public synchronized void callSessionResumeFailed(ImsCallSession session, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionResumeReceived(ImsCallSession session, ImsCallProfile profile) {
        }

        public synchronized void callSessionMergeStarted(ImsCallSession session, ImsCallSession newSession, ImsCallProfile profile) {
        }

        public synchronized void callSessionMergeComplete(ImsCallSession session) {
        }

        public synchronized void callSessionMergeFailed(ImsCallSession session, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionUpdated(ImsCallSession session, ImsCallProfile profile) {
        }

        public synchronized void callSessionUpdateFailed(ImsCallSession session, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionUpdateReceived(ImsCallSession session, ImsCallProfile profile) {
        }

        public synchronized void callSessionConferenceExtended(ImsCallSession session, ImsCallSession newSession, ImsCallProfile profile) {
        }

        public synchronized void callSessionConferenceExtendFailed(ImsCallSession session, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionConferenceExtendReceived(ImsCallSession session, ImsCallSession newSession, ImsCallProfile profile) {
        }

        public synchronized void callSessionInviteParticipantsRequestDelivered(ImsCallSession session) {
        }

        public synchronized void callSessionInviteParticipantsRequestFailed(ImsCallSession session, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionRemoveParticipantsRequestDelivered(ImsCallSession session) {
        }

        public synchronized void callSessionRemoveParticipantsRequestFailed(ImsCallSession session, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionConferenceStateUpdated(ImsCallSession session, ImsConferenceState state) {
        }

        public synchronized void callSessionUssdMessageReceived(ImsCallSession session, int mode, String ussdMessage) {
        }

        public synchronized void callSessionMayHandover(ImsCallSession session, int srcAccessTech, int targetAccessTech) {
        }

        public synchronized void callSessionHandover(ImsCallSession session, int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionHandoverFailed(ImsCallSession session, int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) {
        }

        public synchronized void callSessionTtyModeReceived(ImsCallSession session, int mode) {
        }

        public synchronized void callSessionMultipartyStateChanged(ImsCallSession session, boolean isMultiParty) {
        }

        public synchronized void callSessionSuppServiceReceived(ImsCallSession session, ImsSuppServiceNotification suppServiceInfo) {
        }

        public synchronized void callSessionRttModifyRequestReceived(ImsCallSession session, ImsCallProfile callProfile) {
        }

        public synchronized void callSessionRttModifyResponseReceived(int status) {
        }

        public synchronized void callSessionRttMessageReceived(String rttMessage) {
        }
    }

    public synchronized ImsCallSession(IImsCallSession iSession) {
        this.mClosed = false;
        this.miSession = iSession;
        if (iSession != null) {
            try {
                iSession.setListener(new IImsCallSessionListenerProxy());
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        this.mClosed = true;
    }

    public synchronized ImsCallSession(IImsCallSession iSession, Listener listener) {
        this(iSession);
        setListener(listener);
    }

    public synchronized void close() {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.close();
            this.mClosed = true;
        } catch (RemoteException e) {
        }
    }

    public synchronized String getCallId() {
        if (this.mClosed) {
            return null;
        }
        try {
            return this.miSession.getCallId();
        } catch (RemoteException e) {
            return null;
        }
    }

    public synchronized ImsCallProfile getCallProfile() {
        if (this.mClosed) {
            return null;
        }
        try {
            return this.miSession.getCallProfile();
        } catch (RemoteException e) {
            return null;
        }
    }

    public synchronized ImsCallProfile getLocalCallProfile() {
        if (this.mClosed) {
            return null;
        }
        try {
            return this.miSession.getLocalCallProfile();
        } catch (RemoteException e) {
            return null;
        }
    }

    public synchronized ImsCallProfile getRemoteCallProfile() {
        if (this.mClosed) {
            return null;
        }
        try {
            return this.miSession.getRemoteCallProfile();
        } catch (RemoteException e) {
            return null;
        }
    }

    public synchronized IImsVideoCallProvider getVideoCallProvider() {
        if (this.mClosed) {
            return null;
        }
        try {
            return this.miSession.getVideoCallProvider();
        } catch (RemoteException e) {
            return null;
        }
    }

    public synchronized String getProperty(String name) {
        if (this.mClosed) {
            return null;
        }
        try {
            return this.miSession.getProperty(name);
        } catch (RemoteException e) {
            return null;
        }
    }

    public synchronized int getState() {
        if (this.mClosed) {
            return -1;
        }
        try {
            return this.miSession.getState();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public synchronized boolean isAlive() {
        if (this.mClosed) {
            return false;
        }
        int state = getState();
        switch (state) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    public synchronized IImsCallSession getSession() {
        return this.miSession;
    }

    public synchronized boolean isInCall() {
        if (this.mClosed) {
            return false;
        }
        try {
            return this.miSession.isInCall();
        } catch (RemoteException e) {
            return false;
        }
    }

    public synchronized void setListener(Listener listener) {
        this.mListener = listener;
    }

    public synchronized void setMute(boolean muted) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.setMute(muted);
        } catch (RemoteException e) {
        }
    }

    public synchronized void start(String callee, ImsCallProfile profile) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.start(callee, profile);
        } catch (RemoteException e) {
        }
    }

    public synchronized void start(String[] participants, ImsCallProfile profile) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.startConference(participants, profile);
        } catch (RemoteException e) {
        }
    }

    public synchronized void accept(int callType, ImsStreamMediaProfile profile) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.accept(callType, profile);
        } catch (RemoteException e) {
        }
    }

    public synchronized void deflect(String number) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.deflect(number);
        } catch (RemoteException e) {
        }
    }

    public synchronized void reject(int reason) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.reject(reason);
        } catch (RemoteException e) {
        }
    }

    public synchronized void terminate(int reason) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.terminate(reason);
        } catch (RemoteException e) {
        }
    }

    public synchronized void hold(ImsStreamMediaProfile profile) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.hold(profile);
        } catch (RemoteException e) {
        }
    }

    public synchronized void resume(ImsStreamMediaProfile profile) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.resume(profile);
        } catch (RemoteException e) {
        }
    }

    public synchronized void merge() {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.merge();
        } catch (RemoteException e) {
        }
    }

    public synchronized void update(int callType, ImsStreamMediaProfile profile) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.update(callType, profile);
        } catch (RemoteException e) {
        }
    }

    public synchronized void extendToConference(String[] participants) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.extendToConference(participants);
        } catch (RemoteException e) {
        }
    }

    public synchronized void inviteParticipants(String[] participants) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.inviteParticipants(participants);
        } catch (RemoteException e) {
        }
    }

    public synchronized void removeParticipants(String[] participants) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.removeParticipants(participants);
        } catch (RemoteException e) {
        }
    }

    public synchronized void sendDtmf(char c, Message result) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.sendDtmf(c, result);
        } catch (RemoteException e) {
        }
    }

    public synchronized void startDtmf(char c) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.startDtmf(c);
        } catch (RemoteException e) {
        }
    }

    public synchronized void stopDtmf() {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.stopDtmf();
        } catch (RemoteException e) {
        }
    }

    public synchronized void sendUssd(String ussdMessage) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.sendUssd(ussdMessage);
        } catch (RemoteException e) {
        }
    }

    public synchronized boolean isMultiparty() {
        if (this.mClosed) {
            return false;
        }
        try {
            return this.miSession.isMultiparty();
        } catch (RemoteException e) {
            return false;
        }
    }

    public synchronized void sendRttMessage(String rttMessage) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.sendRttMessage(rttMessage);
        } catch (RemoteException e) {
        }
    }

    public synchronized void sendRttModifyRequest(ImsCallProfile to) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.sendRttModifyRequest(to);
        } catch (RemoteException e) {
        }
    }

    public synchronized void sendRttModifyResponse(boolean response) {
        if (this.mClosed) {
            return;
        }
        try {
            this.miSession.sendRttModifyResponse(response);
        } catch (RemoteException e) {
        }
    }

    /* loaded from: classes2.dex */
    private class IImsCallSessionListenerProxy extends IImsCallSessionListener.Stub {
        private IImsCallSessionListenerProxy() {
        }

        public synchronized void callSessionProgressing(ImsStreamMediaProfile profile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionProgressing(ImsCallSession.this, profile);
            }
        }

        public synchronized void callSessionInitiated(ImsCallProfile profile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionStarted(ImsCallSession.this, profile);
            }
        }

        public synchronized void callSessionInitiatedFailed(ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionStartFailed(ImsCallSession.this, reasonInfo);
            }
        }

        public synchronized void callSessionTerminated(ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionTerminated(ImsCallSession.this, reasonInfo);
            }
        }

        public synchronized void callSessionHeld(ImsCallProfile profile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionHeld(ImsCallSession.this, profile);
            }
        }

        public synchronized void callSessionHoldFailed(ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionHoldFailed(ImsCallSession.this, reasonInfo);
            }
        }

        public synchronized void callSessionHoldReceived(ImsCallProfile profile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionHoldReceived(ImsCallSession.this, profile);
            }
        }

        public synchronized void callSessionResumed(ImsCallProfile profile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionResumed(ImsCallSession.this, profile);
            }
        }

        public synchronized void callSessionResumeFailed(ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionResumeFailed(ImsCallSession.this, reasonInfo);
            }
        }

        public synchronized void callSessionResumeReceived(ImsCallProfile profile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionResumeReceived(ImsCallSession.this, profile);
            }
        }

        public synchronized void callSessionMergeStarted(IImsCallSession newSession, ImsCallProfile profile) {
            Log.d(ImsCallSession.TAG, "callSessionMergeStarted");
        }

        public synchronized void callSessionMergeComplete(IImsCallSession newSession) {
            if (ImsCallSession.this.mListener != null) {
                if (newSession == null) {
                    ImsCallSession.this.mListener.callSessionMergeComplete(null);
                    return;
                }
                ImsCallSession validActiveSession = ImsCallSession.this;
                try {
                    if (!Objects.equals(ImsCallSession.this.miSession.getCallId(), newSession.getCallId())) {
                        validActiveSession = new ImsCallSession(newSession);
                    }
                } catch (RemoteException e) {
                    Log.e(ImsCallSession.TAG, "callSessionMergeComplete: exception for getCallId!");
                }
                ImsCallSession.this.mListener.callSessionMergeComplete(validActiveSession);
            }
        }

        public synchronized void callSessionMergeFailed(ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionMergeFailed(ImsCallSession.this, reasonInfo);
            }
        }

        public synchronized void callSessionUpdated(ImsCallProfile profile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionUpdated(ImsCallSession.this, profile);
            }
        }

        public synchronized void callSessionUpdateFailed(ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionUpdateFailed(ImsCallSession.this, reasonInfo);
            }
        }

        public synchronized void callSessionUpdateReceived(ImsCallProfile profile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionUpdateReceived(ImsCallSession.this, profile);
            }
        }

        public synchronized void callSessionConferenceExtended(IImsCallSession newSession, ImsCallProfile profile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionConferenceExtended(ImsCallSession.this, new ImsCallSession(newSession), profile);
            }
        }

        public synchronized void callSessionConferenceExtendFailed(ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionConferenceExtendFailed(ImsCallSession.this, reasonInfo);
            }
        }

        public synchronized void callSessionConferenceExtendReceived(IImsCallSession newSession, ImsCallProfile profile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionConferenceExtendReceived(ImsCallSession.this, new ImsCallSession(newSession), profile);
            }
        }

        public synchronized void callSessionInviteParticipantsRequestDelivered() {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionInviteParticipantsRequestDelivered(ImsCallSession.this);
            }
        }

        public synchronized void callSessionInviteParticipantsRequestFailed(ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionInviteParticipantsRequestFailed(ImsCallSession.this, reasonInfo);
            }
        }

        public synchronized void callSessionRemoveParticipantsRequestDelivered() {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionRemoveParticipantsRequestDelivered(ImsCallSession.this);
            }
        }

        public synchronized void callSessionRemoveParticipantsRequestFailed(ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionRemoveParticipantsRequestFailed(ImsCallSession.this, reasonInfo);
            }
        }

        public synchronized void callSessionConferenceStateUpdated(ImsConferenceState state) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionConferenceStateUpdated(ImsCallSession.this, state);
            }
        }

        public synchronized void callSessionUssdMessageReceived(int mode, String ussdMessage) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionUssdMessageReceived(ImsCallSession.this, mode, ussdMessage);
            }
        }

        public synchronized void callSessionMayHandover(int srcAccessTech, int targetAccessTech) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionMayHandover(ImsCallSession.this, srcAccessTech, targetAccessTech);
            }
        }

        public synchronized void callSessionHandover(int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionHandover(ImsCallSession.this, srcAccessTech, targetAccessTech, reasonInfo);
            }
        }

        public synchronized void callSessionHandoverFailed(int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionHandoverFailed(ImsCallSession.this, srcAccessTech, targetAccessTech, reasonInfo);
            }
        }

        public synchronized void callSessionTtyModeReceived(int mode) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionTtyModeReceived(ImsCallSession.this, mode);
            }
        }

        public synchronized void callSessionMultipartyStateChanged(boolean isMultiParty) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionMultipartyStateChanged(ImsCallSession.this, isMultiParty);
            }
        }

        public synchronized void callSessionSuppServiceReceived(ImsSuppServiceNotification suppServiceInfo) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionSuppServiceReceived(ImsCallSession.this, suppServiceInfo);
            }
        }

        public synchronized void callSessionRttModifyRequestReceived(ImsCallProfile callProfile) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionRttModifyRequestReceived(ImsCallSession.this, callProfile);
            }
        }

        public synchronized void callSessionRttModifyResponseReceived(int status) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionRttModifyResponseReceived(status);
            }
        }

        public synchronized void callSessionRttMessageReceived(String rttMessage) {
            if (ImsCallSession.this.mListener != null) {
                ImsCallSession.this.mListener.callSessionRttMessageReceived(rttMessage);
            }
        }
    }

    public String toString() {
        return "[ImsCallSession objId:" + System.identityHashCode(this) + " state:" + State.toString(getState()) + " callId:" + getCallId() + "]";
    }
}
