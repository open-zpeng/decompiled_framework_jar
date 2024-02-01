package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.RcsControllerCall;
import android.telephony.ims.aidl.IRcs;

/* loaded from: classes2.dex */
public class RcsIncomingMessage extends RcsMessage {
    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsIncomingMessage(RcsControllerCall rcsControllerCall, int id) {
        super(rcsControllerCall, id);
    }

    public void setArrivalTimestamp(final long arrivalTimestamp) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsIncomingMessage$OdAmvZkbLfGMknLzGuOOXKVYczw
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsIncomingMessage.this.lambda$setArrivalTimestamp$0$RcsIncomingMessage(arrivalTimestamp, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$setArrivalTimestamp$0$RcsIncomingMessage(long arrivalTimestamp, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setMessageArrivalTimestamp(this.mId, true, arrivalTimestamp, callingPackage);
    }

    public long getArrivalTimestamp() throws RcsMessageStoreException {
        return ((Long) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsIncomingMessage$FSzDY0-cZbSPckAubiU3QaXu_Yg
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsIncomingMessage.this.lambda$getArrivalTimestamp$1$RcsIncomingMessage(iRcs, str);
            }
        })).longValue();
    }

    public /* synthetic */ Long lambda$getArrivalTimestamp$1$RcsIncomingMessage(IRcs iRcs, String callingPackage) throws RemoteException {
        return Long.valueOf(iRcs.getMessageArrivalTimestamp(this.mId, true, callingPackage));
    }

    public void setSeenTimestamp(final long notifiedTimestamp) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsIncomingMessage$OvvfqgFG2FNYN7ohCBbWdETfeuQ
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsIncomingMessage.this.lambda$setSeenTimestamp$2$RcsIncomingMessage(notifiedTimestamp, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$setSeenTimestamp$2$RcsIncomingMessage(long notifiedTimestamp, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setMessageSeenTimestamp(this.mId, true, notifiedTimestamp, callingPackage);
    }

    public long getSeenTimestamp() throws RcsMessageStoreException {
        return ((Long) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsIncomingMessage$21fHX_-vVRTL95x404C5b4eGWok
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsIncomingMessage.this.lambda$getSeenTimestamp$3$RcsIncomingMessage(iRcs, str);
            }
        })).longValue();
    }

    public /* synthetic */ Long lambda$getSeenTimestamp$3$RcsIncomingMessage(IRcs iRcs, String callingPackage) throws RemoteException {
        return Long.valueOf(iRcs.getMessageSeenTimestamp(this.mId, true, callingPackage));
    }

    public RcsParticipant getSenderParticipant() throws RcsMessageStoreException {
        return new RcsParticipant(this.mRcsControllerCall, ((Integer) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsIncomingMessage$ye8KwJqH7fqnRAZlQY1PRVyh2b0
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsIncomingMessage.this.lambda$getSenderParticipant$4$RcsIncomingMessage(iRcs, str);
            }
        })).intValue());
    }

    public /* synthetic */ Integer lambda$getSenderParticipant$4$RcsIncomingMessage(IRcs iRcs, String callingPackage) throws RemoteException {
        return Integer.valueOf(iRcs.getSenderParticipant(this.mId, callingPackage));
    }

    @Override // android.telephony.ims.RcsMessage
    public boolean isIncoming() {
        return true;
    }
}
