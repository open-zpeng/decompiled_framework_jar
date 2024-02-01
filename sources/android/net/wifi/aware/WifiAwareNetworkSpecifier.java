package android.net.wifi.aware;

import android.net.NetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class WifiAwareNetworkSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Parcelable.Creator<WifiAwareNetworkSpecifier> CREATOR = new Parcelable.Creator<WifiAwareNetworkSpecifier>() { // from class: android.net.wifi.aware.WifiAwareNetworkSpecifier.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiAwareNetworkSpecifier createFromParcel(Parcel in) {
            return new WifiAwareNetworkSpecifier(in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.createByteArray(), in.createByteArray(), in.readString(), in.readInt());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiAwareNetworkSpecifier[] newArray(int size) {
            return new WifiAwareNetworkSpecifier[size];
        }
    };
    public static final int NETWORK_SPECIFIER_TYPE_IB = 0;
    public static final int NETWORK_SPECIFIER_TYPE_IB_ANY_PEER = 1;
    public static final int NETWORK_SPECIFIER_TYPE_MAX_VALID = 3;
    public static final int NETWORK_SPECIFIER_TYPE_OOB = 2;
    public static final int NETWORK_SPECIFIER_TYPE_OOB_ANY_PEER = 3;
    public final int clientId;
    public final String passphrase;
    public final int peerId;
    public final byte[] peerMac;
    public final byte[] pmk;
    public final int requestorUid;
    public final int role;
    public final int sessionId;
    public final int type;

    public synchronized WifiAwareNetworkSpecifier(int type, int role, int clientId, int sessionId, int peerId, byte[] peerMac, byte[] pmk, String passphrase, int requestorUid) {
        this.type = type;
        this.role = role;
        this.clientId = clientId;
        this.sessionId = sessionId;
        this.peerId = peerId;
        this.peerMac = peerMac;
        this.pmk = pmk;
        this.passphrase = passphrase;
        this.requestorUid = requestorUid;
    }

    public synchronized boolean isOutOfBand() {
        return this.type == 2 || this.type == 3;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.role);
        dest.writeInt(this.clientId);
        dest.writeInt(this.sessionId);
        dest.writeInt(this.peerId);
        dest.writeByteArray(this.peerMac);
        dest.writeByteArray(this.pmk);
        dest.writeString(this.passphrase);
        dest.writeInt(this.requestorUid);
    }

    @Override // android.net.NetworkSpecifier
    public synchronized boolean satisfiedBy(NetworkSpecifier other) {
        if (other instanceof WifiAwareAgentNetworkSpecifier) {
            return ((WifiAwareAgentNetworkSpecifier) other).satisfiesAwareNetworkSpecifier(this);
        }
        return equals(other);
    }

    public int hashCode() {
        int result = (31 * 17) + this.type;
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + this.role)) + this.clientId)) + this.sessionId)) + this.peerId)) + Arrays.hashCode(this.peerMac))) + Arrays.hashCode(this.pmk))) + Objects.hashCode(this.passphrase))) + this.requestorUid;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WifiAwareNetworkSpecifier) {
            WifiAwareNetworkSpecifier lhs = (WifiAwareNetworkSpecifier) obj;
            return this.type == lhs.type && this.role == lhs.role && this.clientId == lhs.clientId && this.sessionId == lhs.sessionId && this.peerId == lhs.peerId && Arrays.equals(this.peerMac, lhs.peerMac) && Arrays.equals(this.pmk, lhs.pmk) && Objects.equals(this.passphrase, lhs.passphrase) && this.requestorUid == lhs.requestorUid;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("WifiAwareNetworkSpecifier [");
        sb.append("type=");
        sb.append(this.type);
        sb.append(", role=");
        sb.append(this.role);
        sb.append(", clientId=");
        sb.append(this.clientId);
        sb.append(", sessionId=");
        sb.append(this.sessionId);
        sb.append(", peerId=");
        sb.append(this.peerId);
        sb.append(", peerMac=");
        sb.append(this.peerMac == null ? "<null>" : "<non-null>");
        sb.append(", pmk=");
        sb.append(this.pmk == null ? "<null>" : "<non-null>");
        sb.append(", passphrase=");
        sb.append(this.passphrase == null ? "<null>" : "<non-null>");
        sb.append(", requestorUid=");
        sb.append(this.requestorUid);
        sb.append("]");
        return sb.toString();
    }

    @Override // android.net.NetworkSpecifier
    public synchronized void assertValidFromUid(int requestorUid) {
        if (this.requestorUid != requestorUid) {
            throw new SecurityException("mismatched UIDs");
        }
    }
}
