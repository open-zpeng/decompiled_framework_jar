package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;
/* loaded from: classes2.dex */
public final class IpSecConfig implements Parcelable {
    public static final Parcelable.Creator<IpSecConfig> CREATOR = new Parcelable.Creator<IpSecConfig>() { // from class: android.net.IpSecConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IpSecConfig createFromParcel(Parcel in) {
            return new IpSecConfig(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IpSecConfig[] newArray(int size) {
            return new IpSecConfig[size];
        }
    };
    private static final String TAG = "IpSecConfig";
    private IpSecAlgorithm mAuthenticatedEncryption;
    private IpSecAlgorithm mAuthentication;
    private String mDestinationAddress;
    private int mEncapRemotePort;
    private int mEncapSocketResourceId;
    private int mEncapType;
    private IpSecAlgorithm mEncryption;
    private int mMarkMask;
    private int mMarkValue;
    private int mMode;
    private int mNattKeepaliveInterval;
    private Network mNetwork;
    private String mSourceAddress;
    private int mSpiResourceId;

    public synchronized void setMode(int mode) {
        this.mMode = mode;
    }

    public synchronized void setSourceAddress(String sourceAddress) {
        this.mSourceAddress = sourceAddress;
    }

    public synchronized void setDestinationAddress(String destinationAddress) {
        this.mDestinationAddress = destinationAddress;
    }

    public synchronized void setSpiResourceId(int resourceId) {
        this.mSpiResourceId = resourceId;
    }

    public synchronized void setEncryption(IpSecAlgorithm encryption) {
        this.mEncryption = encryption;
    }

    public synchronized void setAuthentication(IpSecAlgorithm authentication) {
        this.mAuthentication = authentication;
    }

    public synchronized void setAuthenticatedEncryption(IpSecAlgorithm authenticatedEncryption) {
        this.mAuthenticatedEncryption = authenticatedEncryption;
    }

    public synchronized void setNetwork(Network network) {
        this.mNetwork = network;
    }

    public synchronized void setEncapType(int encapType) {
        this.mEncapType = encapType;
    }

    public synchronized void setEncapSocketResourceId(int resourceId) {
        this.mEncapSocketResourceId = resourceId;
    }

    public synchronized void setEncapRemotePort(int port) {
        this.mEncapRemotePort = port;
    }

    public synchronized void setNattKeepaliveInterval(int interval) {
        this.mNattKeepaliveInterval = interval;
    }

    public synchronized void setMarkValue(int mark) {
        this.mMarkValue = mark;
    }

    public synchronized void setMarkMask(int mask) {
        this.mMarkMask = mask;
    }

    public synchronized int getMode() {
        return this.mMode;
    }

    public synchronized String getSourceAddress() {
        return this.mSourceAddress;
    }

    public synchronized int getSpiResourceId() {
        return this.mSpiResourceId;
    }

    public synchronized String getDestinationAddress() {
        return this.mDestinationAddress;
    }

    public synchronized IpSecAlgorithm getEncryption() {
        return this.mEncryption;
    }

    public synchronized IpSecAlgorithm getAuthentication() {
        return this.mAuthentication;
    }

    public synchronized IpSecAlgorithm getAuthenticatedEncryption() {
        return this.mAuthenticatedEncryption;
    }

    public synchronized Network getNetwork() {
        return this.mNetwork;
    }

    public synchronized int getEncapType() {
        return this.mEncapType;
    }

    public synchronized int getEncapSocketResourceId() {
        return this.mEncapSocketResourceId;
    }

    public synchronized int getEncapRemotePort() {
        return this.mEncapRemotePort;
    }

    public synchronized int getNattKeepaliveInterval() {
        return this.mNattKeepaliveInterval;
    }

    public synchronized int getMarkValue() {
        return this.mMarkValue;
    }

    public synchronized int getMarkMask() {
        return this.mMarkMask;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mMode);
        out.writeString(this.mSourceAddress);
        out.writeString(this.mDestinationAddress);
        out.writeParcelable(this.mNetwork, flags);
        out.writeInt(this.mSpiResourceId);
        out.writeParcelable(this.mEncryption, flags);
        out.writeParcelable(this.mAuthentication, flags);
        out.writeParcelable(this.mAuthenticatedEncryption, flags);
        out.writeInt(this.mEncapType);
        out.writeInt(this.mEncapSocketResourceId);
        out.writeInt(this.mEncapRemotePort);
        out.writeInt(this.mNattKeepaliveInterval);
        out.writeInt(this.mMarkValue);
        out.writeInt(this.mMarkMask);
    }

    @VisibleForTesting
    public synchronized IpSecConfig() {
        this.mMode = 0;
        this.mSourceAddress = "";
        this.mDestinationAddress = "";
        this.mSpiResourceId = -1;
        this.mEncapType = 0;
        this.mEncapSocketResourceId = -1;
    }

    @VisibleForTesting
    public synchronized IpSecConfig(IpSecConfig c) {
        this.mMode = 0;
        this.mSourceAddress = "";
        this.mDestinationAddress = "";
        this.mSpiResourceId = -1;
        this.mEncapType = 0;
        this.mEncapSocketResourceId = -1;
        this.mMode = c.mMode;
        this.mSourceAddress = c.mSourceAddress;
        this.mDestinationAddress = c.mDestinationAddress;
        this.mNetwork = c.mNetwork;
        this.mSpiResourceId = c.mSpiResourceId;
        this.mEncryption = c.mEncryption;
        this.mAuthentication = c.mAuthentication;
        this.mAuthenticatedEncryption = c.mAuthenticatedEncryption;
        this.mEncapType = c.mEncapType;
        this.mEncapSocketResourceId = c.mEncapSocketResourceId;
        this.mEncapRemotePort = c.mEncapRemotePort;
        this.mNattKeepaliveInterval = c.mNattKeepaliveInterval;
        this.mMarkValue = c.mMarkValue;
        this.mMarkMask = c.mMarkMask;
    }

    private synchronized IpSecConfig(Parcel in) {
        this.mMode = 0;
        this.mSourceAddress = "";
        this.mDestinationAddress = "";
        this.mSpiResourceId = -1;
        this.mEncapType = 0;
        this.mEncapSocketResourceId = -1;
        this.mMode = in.readInt();
        this.mSourceAddress = in.readString();
        this.mDestinationAddress = in.readString();
        this.mNetwork = (Network) in.readParcelable(Network.class.getClassLoader());
        this.mSpiResourceId = in.readInt();
        this.mEncryption = (IpSecAlgorithm) in.readParcelable(IpSecAlgorithm.class.getClassLoader());
        this.mAuthentication = (IpSecAlgorithm) in.readParcelable(IpSecAlgorithm.class.getClassLoader());
        this.mAuthenticatedEncryption = (IpSecAlgorithm) in.readParcelable(IpSecAlgorithm.class.getClassLoader());
        this.mEncapType = in.readInt();
        this.mEncapSocketResourceId = in.readInt();
        this.mEncapRemotePort = in.readInt();
        this.mNattKeepaliveInterval = in.readInt();
        this.mMarkValue = in.readInt();
        this.mMarkMask = in.readInt();
    }

    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{mMode=");
        strBuilder.append(this.mMode == 1 ? "TUNNEL" : "TRANSPORT");
        strBuilder.append(", mSourceAddress=");
        strBuilder.append(this.mSourceAddress);
        strBuilder.append(", mDestinationAddress=");
        strBuilder.append(this.mDestinationAddress);
        strBuilder.append(", mNetwork=");
        strBuilder.append(this.mNetwork);
        strBuilder.append(", mEncapType=");
        strBuilder.append(this.mEncapType);
        strBuilder.append(", mEncapSocketResourceId=");
        strBuilder.append(this.mEncapSocketResourceId);
        strBuilder.append(", mEncapRemotePort=");
        strBuilder.append(this.mEncapRemotePort);
        strBuilder.append(", mNattKeepaliveInterval=");
        strBuilder.append(this.mNattKeepaliveInterval);
        strBuilder.append("{mSpiResourceId=");
        strBuilder.append(this.mSpiResourceId);
        strBuilder.append(", mEncryption=");
        strBuilder.append(this.mEncryption);
        strBuilder.append(", mAuthentication=");
        strBuilder.append(this.mAuthentication);
        strBuilder.append(", mAuthenticatedEncryption=");
        strBuilder.append(this.mAuthenticatedEncryption);
        strBuilder.append(", mMarkValue=");
        strBuilder.append(this.mMarkValue);
        strBuilder.append(", mMarkMask=");
        strBuilder.append(this.mMarkMask);
        strBuilder.append("}");
        return strBuilder.toString();
    }

    @VisibleForTesting
    public static synchronized boolean equals(IpSecConfig lhs, IpSecConfig rhs) {
        if (lhs == null || rhs == null) {
            return lhs == rhs;
        } else if (lhs.mMode == rhs.mMode && lhs.mSourceAddress.equals(rhs.mSourceAddress) && lhs.mDestinationAddress.equals(rhs.mDestinationAddress)) {
            return ((lhs.mNetwork != null && lhs.mNetwork.equals(rhs.mNetwork)) || lhs.mNetwork == rhs.mNetwork) && lhs.mEncapType == rhs.mEncapType && lhs.mEncapSocketResourceId == rhs.mEncapSocketResourceId && lhs.mEncapRemotePort == rhs.mEncapRemotePort && lhs.mNattKeepaliveInterval == rhs.mNattKeepaliveInterval && lhs.mSpiResourceId == rhs.mSpiResourceId && IpSecAlgorithm.equals(lhs.mEncryption, rhs.mEncryption) && IpSecAlgorithm.equals(lhs.mAuthenticatedEncryption, rhs.mAuthenticatedEncryption) && IpSecAlgorithm.equals(lhs.mAuthentication, rhs.mAuthentication) && lhs.mMarkValue == rhs.mMarkValue && lhs.mMarkMask == rhs.mMarkMask;
        } else {
            return false;
        }
    }
}
