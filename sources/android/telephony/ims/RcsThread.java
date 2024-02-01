package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.RcsControllerCall;
import android.telephony.ims.RcsMessageQueryParams;
import android.telephony.ims.aidl.IRcs;
import com.android.internal.annotations.VisibleForTesting;

/* loaded from: classes2.dex */
public abstract class RcsThread {
    protected final RcsControllerCall mRcsControllerCall;
    protected int mThreadId;

    public abstract boolean isGroup();

    /* JADX INFO: Access modifiers changed from: protected */
    public RcsThread(RcsControllerCall rcsControllerCall, int threadId) {
        this.mThreadId = threadId;
        this.mRcsControllerCall = rcsControllerCall;
    }

    public RcsMessageSnippet getSnippet() throws RcsMessageStoreException {
        return (RcsMessageSnippet) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsThread$TwqOqnkLjl05BhB2arTpJkBo73Y
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsThread.this.lambda$getSnippet$0$RcsThread(iRcs, str);
            }
        });
    }

    public /* synthetic */ RcsMessageSnippet lambda$getSnippet$0$RcsThread(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getMessageSnippet(this.mThreadId, callingPackage);
    }

    public RcsIncomingMessage addIncomingMessage(final RcsIncomingMessageCreationParams rcsIncomingMessageCreationParams) throws RcsMessageStoreException {
        int messageId = ((Integer) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsThread$9gFw0KtL-BczxOxCksL2zOV2xHM
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsThread.this.lambda$addIncomingMessage$1$RcsThread(rcsIncomingMessageCreationParams, iRcs, str);
            }
        })).intValue();
        return new RcsIncomingMessage(this.mRcsControllerCall, messageId);
    }

    public /* synthetic */ Integer lambda$addIncomingMessage$1$RcsThread(RcsIncomingMessageCreationParams rcsIncomingMessageCreationParams, IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.addIncomingMessage(this.mThreadId, rcsIncomingMessageCreationParams, callingPackage));
    }

    public RcsOutgoingMessage addOutgoingMessage(final RcsOutgoingMessageCreationParams rcsOutgoingMessageCreationParams) throws RcsMessageStoreException {
        int messageId = ((Integer) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsThread$_9zf-uqUJl6VjAbIMvQwKcAyzUs
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsThread.this.lambda$addOutgoingMessage$2$RcsThread(rcsOutgoingMessageCreationParams, iRcs, str);
            }
        })).intValue();
        return new RcsOutgoingMessage(this.mRcsControllerCall, messageId);
    }

    public /* synthetic */ Integer lambda$addOutgoingMessage$2$RcsThread(RcsOutgoingMessageCreationParams rcsOutgoingMessageCreationParams, IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.addOutgoingMessage(this.mThreadId, rcsOutgoingMessageCreationParams, callingPackage));
    }

    public void deleteMessage(final RcsMessage rcsMessage) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsThread$uAkHFwrvypgP5w5y0Uy4uwQ6blY
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsThread.this.lambda$deleteMessage$3$RcsThread(rcsMessage, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$deleteMessage$3$RcsThread(RcsMessage rcsMessage, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.deleteMessage(rcsMessage.getId(), rcsMessage.isIncoming(), this.mThreadId, isGroup(), callingPackage);
    }

    public RcsMessageQueryResult getMessages() throws RcsMessageStoreException {
        final RcsMessageQueryParams queryParams = new RcsMessageQueryParams.Builder().setThread(this).build();
        RcsControllerCall rcsControllerCall = this.mRcsControllerCall;
        return new RcsMessageQueryResult(rcsControllerCall, (RcsMessageQueryResultParcelable) rcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsThread$A9iPL3bU3iiRv1xCYNUNP76n6Vw
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                RcsMessageQueryResultParcelable messages;
                messages = iRcs.getMessages(RcsMessageQueryParams.this, str);
                return messages;
            }
        }));
    }

    @VisibleForTesting
    public int getThreadId() {
        return this.mThreadId;
    }

    public int getThreadType() {
        return isGroup() ? 1 : 0;
    }
}
