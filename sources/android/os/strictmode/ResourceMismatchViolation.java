package android.os.strictmode;
/* loaded from: classes2.dex */
public final class ResourceMismatchViolation extends Violation {
    public synchronized ResourceMismatchViolation(Object tag) {
        super(tag.toString());
    }
}
