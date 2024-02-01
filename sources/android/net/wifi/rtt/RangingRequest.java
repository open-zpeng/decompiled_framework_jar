package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.wifi.ScanResult;
import android.net.wifi.aware.PeerHandle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/* loaded from: classes2.dex */
public final class RangingRequest implements Parcelable {
    public static final Parcelable.Creator<RangingRequest> CREATOR = new Parcelable.Creator<RangingRequest>() { // from class: android.net.wifi.rtt.RangingRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RangingRequest[] newArray(int size) {
            return new RangingRequest[size];
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RangingRequest createFromParcel(Parcel in) {
            return new RangingRequest(in.readArrayList(null));
        }
    };
    private static final int MAX_PEERS = 10;
    public final List<ResponderConfig> mRttPeers;

    public static int getMaxPeers() {
        return 10;
    }

    private RangingRequest(List<ResponderConfig> rttPeers) {
        this.mRttPeers = rttPeers;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mRttPeers);
    }

    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "RangingRequest: mRttPeers=[", "]");
        for (ResponderConfig rc : this.mRttPeers) {
            sj.add(rc.toString());
        }
        return sj.toString();
    }

    public void enforceValidity(boolean awareSupported) {
        if (this.mRttPeers.size() > 10) {
            throw new IllegalArgumentException("Ranging to too many peers requested. Use getMaxPeers() API to get limit.");
        }
        for (ResponderConfig peer : this.mRttPeers) {
            if (!peer.isValid(awareSupported)) {
                throw new IllegalArgumentException("Invalid Responder specification");
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private List<ResponderConfig> mRttPeers = new ArrayList();

        public Builder addAccessPoint(ScanResult apInfo) {
            if (apInfo == null) {
                throw new IllegalArgumentException("Null ScanResult!");
            }
            return addResponder(ResponderConfig.fromScanResult(apInfo));
        }

        public Builder addAccessPoints(List<ScanResult> apInfos) {
            if (apInfos == null) {
                throw new IllegalArgumentException("Null list of ScanResults!");
            }
            for (ScanResult scanResult : apInfos) {
                addAccessPoint(scanResult);
            }
            return this;
        }

        public Builder addWifiAwarePeer(MacAddress peerMacAddress) {
            if (peerMacAddress == null) {
                throw new IllegalArgumentException("Null peer MAC address");
            }
            return addResponder(ResponderConfig.fromWifiAwarePeerMacAddressWithDefaults(peerMacAddress));
        }

        public Builder addWifiAwarePeer(PeerHandle peerHandle) {
            if (peerHandle == null) {
                throw new IllegalArgumentException("Null peer handler (identifier)");
            }
            return addResponder(ResponderConfig.fromWifiAwarePeerHandleWithDefaults(peerHandle));
        }

        @SystemApi
        public Builder addResponder(ResponderConfig responder) {
            if (responder == null) {
                throw new IllegalArgumentException("Null Responder!");
            }
            this.mRttPeers.add(responder);
            return this;
        }

        public RangingRequest build() {
            return new RangingRequest(this.mRttPeers);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof RangingRequest) {
            RangingRequest lhs = (RangingRequest) o;
            return this.mRttPeers.size() == lhs.mRttPeers.size() && this.mRttPeers.containsAll(lhs.mRttPeers);
        }
        return false;
    }

    public int hashCode() {
        return this.mRttPeers.hashCode();
    }
}
