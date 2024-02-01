package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.RcsControllerCall;
import android.telephony.ims.aidl.IRcs;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class RcsOutgoingMessage extends RcsMessage {
    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsOutgoingMessage(RcsControllerCall rcsControllerCall, int id) {
        super(rcsControllerCall, id);
    }

    public List<RcsOutgoingMessageDelivery> getOutgoingDeliveries() throws RcsMessageStoreException {
        List<RcsOutgoingMessageDelivery> messageDeliveries = new ArrayList<>();
        int[] deliveryParticipants = (int[]) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsOutgoingMessage$uP-7yJmMalJRjXgq_qS_YvAUKuo
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsOutgoingMessage.this.lambda$getOutgoingDeliveries$0$RcsOutgoingMessage(iRcs, str);
            }
        });
        if (deliveryParticipants != null) {
            for (int i : deliveryParticipants) {
                Integer deliveryParticipant = Integer.valueOf(i);
                messageDeliveries.add(new RcsOutgoingMessageDelivery(this.mRcsControllerCall, deliveryParticipant.intValue(), this.mId));
            }
        }
        return messageDeliveries;
    }

    public /* synthetic */ int[] lambda$getOutgoingDeliveries$0$RcsOutgoingMessage(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getMessageRecipients(this.mId, callingPackage);
    }

    @Override // android.telephony.ims.RcsMessage
    public boolean isIncoming() {
        return false;
    }
}
