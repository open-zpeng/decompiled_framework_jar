package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class NetworkState implements Parcelable {
    private static final boolean SANITY_CHECK_ROAMING = false;
    public final LinkProperties linkProperties;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final Network network;
    public final NetworkCapabilities networkCapabilities;
    public final String networkId;
    public final NetworkInfo networkInfo;
    public final String subscriberId;
    public static final NetworkState EMPTY = new NetworkState(null, null, null, null, null, null);
    @UnsupportedAppUsage
    public static final Parcelable.Creator<NetworkState> CREATOR = new Parcelable.Creator<NetworkState>() { // from class: android.net.NetworkState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkState createFromParcel(Parcel in) {
            return new NetworkState(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkState[] newArray(int size) {
            return new NetworkState[size];
        }
    };

    public NetworkState(NetworkInfo networkInfo, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, Network network, String subscriberId, String networkId) {
        this.networkInfo = networkInfo;
        this.linkProperties = linkProperties;
        this.networkCapabilities = networkCapabilities;
        this.network = network;
        this.subscriberId = subscriberId;
        this.networkId = networkId;
    }

    @UnsupportedAppUsage
    public NetworkState(Parcel in) {
        this.networkInfo = (NetworkInfo) in.readParcelable(null);
        this.linkProperties = (LinkProperties) in.readParcelable(null);
        this.networkCapabilities = (NetworkCapabilities) in.readParcelable(null);
        this.network = (Network) in.readParcelable(null);
        this.subscriberId = in.readString();
        this.networkId = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.networkInfo, flags);
        out.writeParcelable(this.linkProperties, flags);
        out.writeParcelable(this.networkCapabilities, flags);
        out.writeParcelable(this.network, flags);
        out.writeString(this.subscriberId);
        out.writeString(this.networkId);
    }
}
