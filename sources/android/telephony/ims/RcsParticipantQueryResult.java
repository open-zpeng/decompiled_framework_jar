package android.telephony.ims;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public final class RcsParticipantQueryResult {
    private final RcsControllerCall mRcsControllerCall;
    private final RcsParticipantQueryResultParcelable mRcsParticipantQueryResultParcelable;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsParticipantQueryResult(RcsControllerCall rcsControllerCall, RcsParticipantQueryResultParcelable rcsParticipantQueryResultParcelable) {
        this.mRcsControllerCall = rcsControllerCall;
        this.mRcsParticipantQueryResultParcelable = rcsParticipantQueryResultParcelable;
    }

    public RcsQueryContinuationToken getContinuationToken() {
        return this.mRcsParticipantQueryResultParcelable.mContinuationToken;
    }

    public List<RcsParticipant> getParticipants() {
        return (List) this.mRcsParticipantQueryResultParcelable.mParticipantIds.stream().map(new Function() { // from class: android.telephony.ims.-$$Lambda$RcsParticipantQueryResult$5cUqqqG-A5Xe8Jrc2zruOvBMj44
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return RcsParticipantQueryResult.this.lambda$getParticipants$0$RcsParticipantQueryResult((Integer) obj);
            }
        }).collect(Collectors.toList());
    }

    public /* synthetic */ RcsParticipant lambda$getParticipants$0$RcsParticipantQueryResult(Integer participantId) {
        return new RcsParticipant(this.mRcsControllerCall, participantId.intValue());
    }
}
