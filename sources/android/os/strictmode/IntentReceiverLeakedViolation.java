package android.os.strictmode;
/* loaded from: classes2.dex */
public final class IntentReceiverLeakedViolation extends Violation {
    public synchronized IntentReceiverLeakedViolation(Throwable originStack) {
        super(null);
        setStackTrace(originStack.getStackTrace());
    }
}
