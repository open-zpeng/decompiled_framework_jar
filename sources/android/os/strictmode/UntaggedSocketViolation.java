package android.os.strictmode;

/* loaded from: classes2.dex */
public final class UntaggedSocketViolation extends Violation {
    public UntaggedSocketViolation() {
        super("Untagged socket detected; use TrafficStats.setThreadSocketTag() to track all network usage");
    }
}
