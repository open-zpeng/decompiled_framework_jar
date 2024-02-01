package android.net;
/* loaded from: classes2.dex */
public abstract class NetworkSpecifier {
    public abstract synchronized boolean satisfiedBy(NetworkSpecifier networkSpecifier);

    public synchronized void assertValidFromUid(int requestorUid) {
    }
}
