package android.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.RcsControllerCall;
import android.telephony.ims.aidl.IRcs;

/* loaded from: classes2.dex */
public class RcsParticipant {
    private final int mId;
    private final RcsControllerCall mRcsControllerCall;

    public RcsParticipant(RcsControllerCall rcsControllerCall, int id) {
        this.mRcsControllerCall = rcsControllerCall;
        this.mId = id;
    }

    public String getCanonicalAddress() throws RcsMessageStoreException {
        return (String) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsParticipant$T35onLZnU-uRTl7zQ7ZWRFtFvx4
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsParticipant.this.lambda$getCanonicalAddress$0$RcsParticipant(iRcs, str);
            }
        });
    }

    public /* synthetic */ String lambda$getCanonicalAddress$0$RcsParticipant(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getRcsParticipantCanonicalAddress(this.mId, callingPackage);
    }

    public String getAlias() throws RcsMessageStoreException {
        return (String) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsParticipant$MNtRFbM6h-ycH3bPEUZgB5f56zs
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsParticipant.this.lambda$getAlias$1$RcsParticipant(iRcs, str);
            }
        });
    }

    public /* synthetic */ String lambda$getAlias$1$RcsParticipant(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getRcsParticipantAlias(this.mId, callingPackage);
    }

    public void setAlias(final String alias) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsParticipant$xir-e-NE3auWDac4dOx89mKtRKU
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsParticipant.this.lambda$setAlias$2$RcsParticipant(alias, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$setAlias$2$RcsParticipant(String alias, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setRcsParticipantAlias(this.mId, alias, callingPackage);
    }

    public String getContactId() throws RcsMessageStoreException {
        return (String) this.mRcsControllerCall.call(new RcsControllerCall.RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsParticipant$up5zUlvCkFUru1_1NfgXrzNmBic
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsParticipant.this.lambda$getContactId$3$RcsParticipant(iRcs, str);
            }
        });
    }

    public /* synthetic */ String lambda$getContactId$3$RcsParticipant(IRcs iRcs, String callingPackage) throws RemoteException {
        return iRcs.getRcsParticipantContactId(this.mId, callingPackage);
    }

    public void setContactId(final String contactId) throws RcsMessageStoreException {
        this.mRcsControllerCall.callWithNoReturn(new RcsControllerCall.RcsServiceCallWithNoReturn() { // from class: android.telephony.ims.-$$Lambda$RcsParticipant$HgHlMU15W2RReyvhk-UQ-432pfA
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCallWithNoReturn
            public final void methodOnIRcs(IRcs iRcs, String str) {
                RcsParticipant.this.lambda$setContactId$4$RcsParticipant(contactId, iRcs, str);
            }
        });
    }

    public /* synthetic */ void lambda$setContactId$4$RcsParticipant(String contactId, IRcs iRcs, String callingPackage) throws RemoteException {
        iRcs.setRcsParticipantContactId(this.mId, contactId, callingPackage);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RcsParticipant) {
            RcsParticipant other = (RcsParticipant) obj;
            return this.mId == other.mId;
        }
        return false;
    }

    public int hashCode() {
        return this.mId;
    }

    public int getId() {
        return this.mId;
    }
}
