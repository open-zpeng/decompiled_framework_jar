package android.telephony.ims;

/* loaded from: classes2.dex */
public abstract class RcsGroupThreadEvent extends RcsEvent {
    private final RcsParticipant mOriginatingParticipant;
    private final RcsGroupThread mRcsGroupThread;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsGroupThreadEvent(long timestamp, RcsGroupThread rcsGroupThread, RcsParticipant originatingParticipant) {
        super(timestamp);
        this.mRcsGroupThread = rcsGroupThread;
        this.mOriginatingParticipant = originatingParticipant;
    }

    public RcsGroupThread getRcsGroupThread() {
        return this.mRcsGroupThread;
    }

    public RcsParticipant getOriginatingParticipant() {
        return this.mOriginatingParticipant;
    }
}
