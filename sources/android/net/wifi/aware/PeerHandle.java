package android.net.wifi.aware;
/* loaded from: classes2.dex */
public class PeerHandle {
    public int peerId;

    public synchronized PeerHandle(int peerId) {
        this.peerId = peerId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return (o instanceof PeerHandle) && this.peerId == ((PeerHandle) o).peerId;
    }

    public int hashCode() {
        return this.peerId;
    }
}
