package android.telephony.ims;

import android.net.Uri;
import android.os.RemoteException;
import android.telephony.ims.RcsControllerCall;
import android.telephony.ims.RcsParticipantQueryParams;
import android.telephony.ims.aidl.IRcs;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes2.dex */
public class RcsGroupThread extends RcsThread {
    public RcsGroupThread(RcsControllerCall rcsControllerCall, int threadId) {
        super(rcsControllerCall, threadId);
    }

    @Override // android.telephony.ims.RcsThread
    public boolean isGroup() {
        return true;
    }

    public String getGroupName() throws RcsMessageStoreException {
        return (String) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$cwnjgWxIgjmTCKAe7pcICt4Voo0
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsGroupThread.this.lambda$getGroupName$0$RcsGroupThread(iRcs, str);
            }
        });
    }

    public /* synthetic */ String lambda$getGroupName$0$RcsGroupThread(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getGroupThreadName(this.mThreadId, callingPackage);
    }

    public void setGroupName(final String groupName) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$ZorE2WcUPTtLCwMm_x5CnWwa7YI
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsGroupThread.this.lambda$setGroupName$1$RcsGroupThread(groupName, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$setGroupName$1$RcsGroupThread(String groupName, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setGroupThreadName(this.mThreadId, groupName, callingPackage);
    }

    public Uri getGroupIcon() throws RcsMessageStoreException {
        return (Uri) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$4K1iTAEPwdeTAbDd4wTsX1Jl4S4
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsGroupThread.this.lambda$getGroupIcon$2$RcsGroupThread(iRcs, str);
            }
        });
    }

    public /* synthetic */ Uri lambda$getGroupIcon$2$RcsGroupThread(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getGroupThreadIcon(this.mThreadId, callingPackage);
    }

    public void setGroupIcon(final Uri groupIcon) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$2-3X4NWEVE7qw298P70JdcMW6oM
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsGroupThread.this.lambda$setGroupIcon$3$RcsGroupThread(groupIcon, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$setGroupIcon$3$RcsGroupThread(Uri groupIcon, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setGroupThreadIcon(this.mThreadId, groupIcon, callingPackage);
    }

    public RcsParticipant getOwner() throws RcsMessageStoreException {
        return new RcsParticipant(this.mRcsControllerCall, ((Integer) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$OMEGtapvlm86Yn7pLPBR5He4UoQ
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsGroupThread.this.lambda$getOwner$4$RcsGroupThread(iRcs, str);
            }
        })).intValue());
    }

    public /* synthetic */ Integer lambda$getOwner$4$RcsGroupThread(IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.getGroupThreadOwner(this.mThreadId, callingPackage));
    }

    public void setOwner(final RcsParticipant participant) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$9QKuv_xqJEallZ-aE2sSumu3POo
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsGroupThread.this.lambda$setOwner$5$RcsGroupThread(participant, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$setOwner$5$RcsGroupThread(RcsParticipant participant, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setGroupThreadOwner(this.mThreadId, participant.getId(), callingPackage);
    }

    public void addParticipant(final RcsParticipant participant) throws RcsMessageStoreException {
        if (participant == null) {
            return;
        }
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$HaJSnZuef49b66N8v9ayzVaOQxQ
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsGroupThread.this.lambda$addParticipant$6$RcsGroupThread(participant, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$addParticipant$6$RcsGroupThread(RcsParticipant participant, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.addParticipantToGroupThread(this.mThreadId, participant.getId(), callingPackage);
    }

    public void removeParticipant(final RcsParticipant participant) throws RcsMessageStoreException {
        if (participant == null) {
            return;
        }
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$xvETBJ_gzJJ5zvelRSNsYZBdXKw
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsGroupThread.this.lambda$removeParticipant$7$RcsGroupThread(participant, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$removeParticipant$7$RcsGroupThread(RcsParticipant participant, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.removeParticipantFromGroupThread(this.mThreadId, participant.getId(), callingPackage);
    }

    public Set<RcsParticipant> getParticipants() throws RcsMessageStoreException {
        final RcsParticipantQueryParams queryParameters = new RcsParticipantQueryParams.Builder().setThread(this).build();
        RcsParticipantQueryResult queryResult = new RcsParticipantQueryResult(this.mRcsControllerCall, (RcsParticipantQueryResultParcelable) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$X2eY_CkF7PfEGF8QwmaD6Cv0PhI
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                RcsParticipantQueryResultParcelable participants;
                participants = iRcs.getParticipants(RcsParticipantQueryParams.this, str);
                return participants;
            }
        }));
        List<RcsParticipant> participantList = queryResult.getParticipants();
        Set<RcsParticipant> participantSet = new LinkedHashSet<>(participantList);
        return Collections.unmodifiableSet(participantSet);
    }

    public Uri getConferenceUri() throws RcsMessageStoreException {
        return (Uri) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$hYpkX2Z60Pf5FiSb6pvoBpmHfXA
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsGroupThread.this.lambda$getConferenceUri$9$RcsGroupThread(iRcs, str);
            }
        });
    }

    public /* synthetic */ Uri lambda$getConferenceUri$9$RcsGroupThread(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getGroupThreadConferenceUri(this.mThreadId, callingPackage);
    }

    public void setConferenceUri(final Uri conferenceUri) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsGroupThread$LhWdWS6noezEn0xijClZdbKHOas
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsGroupThread.this.lambda$setConferenceUri$10$RcsGroupThread(conferenceUri, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$setConferenceUri$10$RcsGroupThread(Uri conferenceUri, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setGroupThreadConferenceUri(this.mThreadId, conferenceUri, callingPackage);
    }
}
