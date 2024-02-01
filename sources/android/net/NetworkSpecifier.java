package android.net;

/* loaded from: classes2.dex */
public abstract class NetworkSpecifier {
    public abstract boolean satisfiedBy(NetworkSpecifier networkSpecifier);

    public void assertValidFromUid(int requestorUid) {
    }

    public NetworkSpecifier redact() {
        return this;
    }
}
