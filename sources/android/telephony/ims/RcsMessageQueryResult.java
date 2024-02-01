package android.telephony.ims;

import com.android.ims.RcsTypeIdPair;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public final class RcsMessageQueryResult {
    private final RcsControllerCall mRcsControllerCall;
    private final RcsMessageQueryResultParcelable mRcsMessageQueryResultParcelable;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsMessageQueryResult(RcsControllerCall rcsControllerCall, RcsMessageQueryResultParcelable rcsMessageQueryResultParcelable) {
        this.mRcsControllerCall = rcsControllerCall;
        this.mRcsMessageQueryResultParcelable = rcsMessageQueryResultParcelable;
    }

    public RcsQueryContinuationToken getContinuationToken() {
        return this.mRcsMessageQueryResultParcelable.mContinuationToken;
    }

    public List<RcsMessage> getMessages() {
        return (List) this.mRcsMessageQueryResultParcelable.mMessageTypeIdPairs.stream().map(new Function() { // from class: android.telephony.ims.-$$Lambda$RcsMessageQueryResult$20XnTdVu75hlh0utIOyf1L-ZpTE
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return RcsMessageQueryResult.this.lambda$getMessages$0$RcsMessageQueryResult((RcsTypeIdPair) obj);
            }
        }).collect(Collectors.toList());
    }

    public /* synthetic */ RcsMessage lambda$getMessages$0$RcsMessageQueryResult(RcsTypeIdPair typeIdPair) {
        if (typeIdPair.getType() == 1) {
            return new RcsIncomingMessage(this.mRcsControllerCall, typeIdPair.getId());
        }
        return new RcsOutgoingMessage(this.mRcsControllerCall, typeIdPair.getId());
    }
}
