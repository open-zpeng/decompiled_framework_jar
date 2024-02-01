package android.telephony.ims;

/* loaded from: classes2.dex */
public abstract class RcsEvent {
    private final long mTimestamp;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void persist(RcsControllerCall rcsControllerCall) throws RcsMessageStoreException;

    /* JADX INFO: Access modifiers changed from: protected */
    public RcsEvent(long timestamp) {
        this.mTimestamp = timestamp;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }
}
