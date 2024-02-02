package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.text.TextUtils;
import android.util.proto.ProtoOutputStream;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes2.dex */
public class NetworkRequest implements Parcelable {
    public static final Parcelable.Creator<NetworkRequest> CREATOR = new Parcelable.Creator<NetworkRequest>() { // from class: android.net.NetworkRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkRequest createFromParcel(Parcel in) {
            NetworkCapabilities nc = NetworkCapabilities.CREATOR.createFromParcel(in);
            int legacyType = in.readInt();
            int requestId = in.readInt();
            Type type = Type.valueOf(in.readString());
            NetworkRequest result = new NetworkRequest(nc, legacyType, requestId, type);
            return result;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkRequest[] newArray(int size) {
            return new NetworkRequest[size];
        }
    };
    private protected final int legacyType;
    private protected final NetworkCapabilities networkCapabilities;
    private protected final int requestId;
    public final Type type;

    /* loaded from: classes2.dex */
    public enum Type {
        NONE,
        LISTEN,
        TRACK_DEFAULT,
        REQUEST,
        BACKGROUND_REQUEST
    }

    public synchronized NetworkRequest(NetworkCapabilities nc, int legacyType, int rId, Type type) {
        if (nc == null) {
            throw new NullPointerException();
        }
        this.requestId = rId;
        this.networkCapabilities = nc;
        this.legacyType = legacyType;
        this.type = type;
    }

    public synchronized NetworkRequest(NetworkRequest that) {
        this.networkCapabilities = new NetworkCapabilities(that.networkCapabilities);
        this.requestId = that.requestId;
        this.legacyType = that.legacyType;
        this.type = that.type;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private final NetworkCapabilities mNetworkCapabilities = new NetworkCapabilities();

        public Builder() {
            this.mNetworkCapabilities.setSingleUid(Process.myUid());
        }

        public NetworkRequest build() {
            NetworkCapabilities nc = new NetworkCapabilities(this.mNetworkCapabilities);
            nc.maybeMarkCapabilitiesRestricted();
            return new NetworkRequest(nc, -1, 0, Type.NONE);
        }

        public Builder addCapability(int capability) {
            this.mNetworkCapabilities.addCapability(capability);
            return this;
        }

        public Builder removeCapability(int capability) {
            this.mNetworkCapabilities.removeCapability(capability);
            return this;
        }

        public synchronized Builder setCapabilities(NetworkCapabilities nc) {
            this.mNetworkCapabilities.set(nc);
            return this;
        }

        public synchronized Builder setUids(Set<UidRange> uids) {
            this.mNetworkCapabilities.setUids(uids);
            return this;
        }

        public synchronized Builder addUnwantedCapability(int capability) {
            this.mNetworkCapabilities.addUnwantedCapability(capability);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Builder clearCapabilities() {
            this.mNetworkCapabilities.clearAll();
            return this;
        }

        public Builder addTransportType(int transportType) {
            this.mNetworkCapabilities.addTransportType(transportType);
            return this;
        }

        public Builder removeTransportType(int transportType) {
            this.mNetworkCapabilities.removeTransportType(transportType);
            return this;
        }

        public synchronized Builder setLinkUpstreamBandwidthKbps(int upKbps) {
            this.mNetworkCapabilities.setLinkUpstreamBandwidthKbps(upKbps);
            return this;
        }

        public synchronized Builder setLinkDownstreamBandwidthKbps(int downKbps) {
            this.mNetworkCapabilities.setLinkDownstreamBandwidthKbps(downKbps);
            return this;
        }

        public Builder setNetworkSpecifier(String networkSpecifier) {
            return setNetworkSpecifier(TextUtils.isEmpty(networkSpecifier) ? null : new StringNetworkSpecifier(networkSpecifier));
        }

        public Builder setNetworkSpecifier(NetworkSpecifier networkSpecifier) {
            MatchAllNetworkSpecifier.checkNotMatchAllNetworkSpecifier(networkSpecifier);
            this.mNetworkCapabilities.setNetworkSpecifier(networkSpecifier);
            return this;
        }

        private protected Builder setSignalStrength(int signalStrength) {
            this.mNetworkCapabilities.setSignalStrength(signalStrength);
            return this;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        this.networkCapabilities.writeToParcel(dest, flags);
        dest.writeInt(this.legacyType);
        dest.writeInt(this.requestId);
        dest.writeString(this.type.name());
    }

    public synchronized boolean isListen() {
        return this.type == Type.LISTEN;
    }

    public synchronized boolean isRequest() {
        return isForegroundRequest() || isBackgroundRequest();
    }

    public synchronized boolean isForegroundRequest() {
        return this.type == Type.TRACK_DEFAULT || this.type == Type.REQUEST;
    }

    public synchronized boolean isBackgroundRequest() {
        return this.type == Type.BACKGROUND_REQUEST;
    }

    public boolean hasCapability(int capability) {
        return this.networkCapabilities.hasCapability(capability);
    }

    public synchronized boolean hasUnwantedCapability(int capability) {
        return this.networkCapabilities.hasUnwantedCapability(capability);
    }

    public boolean hasTransport(int transportType) {
        return this.networkCapabilities.hasTransport(transportType);
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("NetworkRequest [ ");
        sb.append(this.type);
        sb.append(" id=");
        sb.append(this.requestId);
        if (this.legacyType != -1) {
            str = ", legacyType=" + this.legacyType;
        } else {
            str = "";
        }
        sb.append(str);
        sb.append(", ");
        sb.append(this.networkCapabilities.toString());
        sb.append(" ]");
        return sb.toString();
    }

    private synchronized int typeToProtoEnum(Type t) {
        switch (t) {
            case NONE:
                return 1;
            case LISTEN:
                return 2;
            case TRACK_DEFAULT:
                return 3;
            case REQUEST:
                return 4;
            case BACKGROUND_REQUEST:
                return 5;
            default:
                return 0;
        }
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1159641169921L, typeToProtoEnum(this.type));
        proto.write(1120986464258L, this.requestId);
        proto.write(1120986464259L, this.legacyType);
        this.networkCapabilities.writeToProto(proto, 1146756268036L);
        proto.end(token);
    }

    public boolean equals(Object obj) {
        if (obj instanceof NetworkRequest) {
            NetworkRequest that = (NetworkRequest) obj;
            return that.legacyType == this.legacyType && that.requestId == this.requestId && that.type == this.type && Objects.equals(that.networkCapabilities, this.networkCapabilities);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.requestId), Integer.valueOf(this.legacyType), this.networkCapabilities, this.type);
    }
}
