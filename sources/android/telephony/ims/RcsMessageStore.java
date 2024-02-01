package android.telephony.ims;

import android.content.Context;
import android.net.Uri;
import android.telephony.ims.RcsControllerCall;
import android.telephony.ims.aidl.IRcs;
import java.util.List;

/* loaded from: classes2.dex */
public class RcsMessageStore {
    RcsControllerCall mRcsControllerCall;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsMessageStore(Context context) {
        this.mRcsControllerCall = new RcsControllerCall(context);
    }

    public RcsThreadQueryResult getRcsThreads(final RcsThreadQueryParams queryParameters) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsThreadQueryResult(rcsControllerCall, (RcsThreadQueryResultParcelable) rcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$z090Zf4wxRrBwUxXanwm4N3vb7w
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                RcsThreadQueryResultParcelable rcsThreads;
                rcsThreads = iRcs.getRcsThreads(RcsThreadQueryParams.this, str);
                return rcsThreads;
            }
        }));
    }

    public RcsThreadQueryResult getRcsThreads(final RcsQueryContinuationToken continuationToken) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsThreadQueryResult(rcsControllerCall, (RcsThreadQueryResultParcelable) rcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$XArwINUevYo-Ol_OgZskFwRkGhs
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                RcsThreadQueryResultParcelable rcsThreadsWithToken;
                rcsThreadsWithToken = iRcs.getRcsThreadsWithToken(RcsQueryContinuationToken.this, str);
                return rcsThreadsWithToken;
            }
        }));
    }

    public RcsParticipantQueryResult getRcsParticipants(final RcsParticipantQueryParams queryParameters) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsParticipantQueryResult(rcsControllerCall, (RcsParticipantQueryResultParcelable) rcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$720PbSnOJzhKXiqHw1UEfx5w-6A
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                RcsParticipantQueryResultParcelable participants;
                participants = iRcs.getParticipants(RcsParticipantQueryParams.this, str);
                return participants;
            }
        }));
    }

    public RcsParticipantQueryResult getRcsParticipants(final RcsQueryContinuationToken continuationToken) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsParticipantQueryResult(rcsControllerCall, (RcsParticipantQueryResultParcelable) rcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$tSyQsX68KutSWLEXxfgNSJ47ep0
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                RcsParticipantQueryResultParcelable participantsWithToken;
                participantsWithToken = iRcs.getParticipantsWithToken(RcsQueryContinuationToken.this, str);
                return participantsWithToken;
            }
        }));
    }

    public RcsMessageQueryResult getRcsMessages(final RcsMessageQueryParams queryParams) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsMessageQueryResult(rcsControllerCall, (RcsMessageQueryResultParcelable) rcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$5QXAY7bGFdmsWgLF0pk1tyYYovg
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                RcsMessageQueryResultParcelable messages;
                messages = iRcs.getMessages(RcsMessageQueryParams.this, str);
                return messages;
            }
        }));
    }

    public RcsMessageQueryResult getRcsMessages(final RcsQueryContinuationToken continuationToken) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsMessageQueryResult(rcsControllerCall, (RcsMessageQueryResultParcelable) rcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$fs2V7Gtqd2gkYR7NanLG2NjZNho
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                RcsMessageQueryResultParcelable messagesWithToken;
                messagesWithToken = iRcs.getMessagesWithToken(RcsQueryContinuationToken.this, str);
                return messagesWithToken;
            }
        }));
    }

    public RcsEventQueryResult getRcsEvents(final RcsEventQueryParams queryParams) throws RcsMessageStoreException {
        return ((RcsEventQueryResultDescriptor) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$IvBKppwBc6MDwzIkAi2XJcVB-iI
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                RcsEventQueryResultDescriptor events;
                events = iRcs.getEvents(RcsEventQueryParams.this, str);
                return events;
            }
        })).getRcsEventQueryResult(this.mRcsControllerCall);
    }

    public RcsEventQueryResult getRcsEvents(final RcsQueryContinuationToken continuationToken) throws RcsMessageStoreException {
        return ((RcsEventQueryResultDescriptor) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$RFZerRPNR1WyCuEIu6_yEveDhrk
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                RcsEventQueryResultDescriptor eventsWithToken;
                eventsWithToken = iRcs.getEventsWithToken(RcsQueryContinuationToken.this, str);
                return eventsWithToken;
            }
        })).getRcsEventQueryResult(this.mRcsControllerCall);
    }

    public void persistRcsEvent(RcsEvent rcsEvent) throws RcsMessageStoreException {
        rcsEvent.persist(this.mRcsControllerCall);
    }

    public Rcs1To1Thread createRcs1To1Thread(final RcsParticipant recipient) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new Rcs1To1Thread(rcsControllerCall, ((Integer) rcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$eOFObBGn-N5PMKJvVTBw06iJWQ4
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                Integer valueOf;
                valueOf = Integer.valueOf(iRcs.createRcs1To1Thread(RcsParticipant.this.getId(), str));
                return valueOf;
            }
        })).intValue());
    }

    public RcsGroupThread createGroupThread(List<RcsParticipant> recipients, final String groupName, final Uri groupIcon) throws RcsMessageStoreException {
        int[] recipientIds = null;
        if (recipients != null) {
            recipientIds = new int[recipients.size()];
            for (int i = 0; i < recipients.size(); i++) {
                recipientIds[i] = recipients.get(i).getId();
            }
        }
        final int[] finalRecipientIds = recipientIds;
        int threadId = ((Integer) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$g309WUVpYx8N7s-uWdUAGJXtJOs
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                Integer valueOf;
                valueOf = Integer.valueOf(iRcs.createGroupThread(finalRecipientIds, groupName, groupIcon, str));
                return valueOf;
            }
        })).intValue();
        return new RcsGroupThread(this.mRcsControllerCall, threadId);
    }

    public void deleteThread(final RcsThread thread) throws RcsMessageStoreException {
        if (thread == null) {
            return;
        }
        boolean isDeleteSucceeded = ((Boolean) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$nbXWLR_ux8VCEHNEyE7JO0J05YI
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                Boolean valueOf;
                valueOf = Boolean.valueOf(iRcs.deleteThread(r0.getThreadId(), RcsThread.this.getThreadType(), str));
                return valueOf;
            }
        })).booleanValue();
        if (!isDeleteSucceeded) {
            throw new RcsMessageStoreException("Could not delete RcsThread");
        }
    }

    public RcsParticipant createRcsParticipant(final String canonicalAddress, final String alias) throws RcsMessageStoreException {
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsParticipant(rcsControllerCall, ((Integer) rcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsMessageStore$d1Om4XlR70Dyh7qD9d6F4NZZkQI
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                Integer valueOf;
                valueOf = Integer.valueOf(iRcs.createRcsParticipant(canonicalAddress, alias, str));
                return valueOf;
            }
        })).intValue());
    }
}
