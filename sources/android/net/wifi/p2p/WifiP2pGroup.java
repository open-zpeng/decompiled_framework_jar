package android.net.wifi.p2p;

import android.annotation.UnsupportedAppUsage;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class WifiP2pGroup implements Parcelable {
    public static final int PERSISTENT_NET_ID = -2;
    @UnsupportedAppUsage
    public static final int TEMPORARY_NET_ID = -1;
    public static final int TETHER_NET_ID = -3;
    private List<WifiP2pDevice> mClients = new ArrayList();
    private int mFrequency;
    private String mInterface;
    private boolean mIsGroupOwner;
    private int mNetId;
    private String mNetworkName;
    private WifiP2pDevice mOwner;
    private String mPassphrase;
    private static final Pattern groupStartedPattern = Pattern.compile("ssid=\"(.+)\" freq=(\\d+) (?:psk=)?([0-9a-fA-F]{64})?(?:passphrase=)?(?:\"(.{0,63})\")? go_dev_addr=((?:[0-9a-f]{2}:){5}[0-9a-f]{2}) ?(\\[PERSISTENT\\])?");
    public static final Parcelable.Creator<WifiP2pGroup> CREATOR = new Parcelable.Creator<WifiP2pGroup>() { // from class: android.net.wifi.p2p.WifiP2pGroup.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiP2pGroup createFromParcel(Parcel in) {
            WifiP2pGroup group = new WifiP2pGroup();
            group.setNetworkName(in.readString());
            group.setOwner((WifiP2pDevice) in.readParcelable(null));
            group.setIsGroupOwner(in.readByte() == 1);
            int clientCount = in.readInt();
            for (int i = 0; i < clientCount; i++) {
                group.addClient((WifiP2pDevice) in.readParcelable(null));
            }
            group.setPassphrase(in.readString());
            group.setInterface(in.readString());
            group.setNetworkId(in.readInt());
            group.setFrequency(in.readInt());
            return group;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiP2pGroup[] newArray(int size) {
            return new WifiP2pGroup[size];
        }
    };

    public WifiP2pGroup() {
    }

    @UnsupportedAppUsage
    public WifiP2pGroup(String supplicantEvent) throws IllegalArgumentException {
        String[] tokens = supplicantEvent.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Malformed supplicant event");
        }
        if (!tokens[0].startsWith("P2P-GROUP")) {
            if (tokens[0].equals("P2P-INVITATION-RECEIVED")) {
                this.mNetId = -2;
                for (String token : tokens) {
                    String[] nameValue = token.split("=");
                    if (nameValue.length == 2) {
                        if (nameValue[0].equals("sa")) {
                            String sa = nameValue[1];
                            WifiP2pDevice dev = new WifiP2pDevice();
                            dev.deviceAddress = nameValue[1];
                            this.mClients.add(dev);
                        } else if (nameValue[0].equals("go_dev_addr")) {
                            this.mOwner = new WifiP2pDevice(nameValue[1]);
                        } else if (nameValue[0].equals("persistent")) {
                            this.mNetId = Integer.parseInt(nameValue[1]);
                        }
                    }
                }
                return;
            }
            throw new IllegalArgumentException("Malformed supplicant event");
        }
        this.mInterface = tokens[1];
        this.mIsGroupOwner = tokens[2].equals("GO");
        Matcher match = groupStartedPattern.matcher(supplicantEvent);
        if (!match.find()) {
            return;
        }
        this.mNetworkName = match.group(1);
        this.mFrequency = Integer.parseInt(match.group(2));
        this.mPassphrase = match.group(4);
        this.mOwner = new WifiP2pDevice(match.group(5));
        if (match.group(6) != null) {
            this.mNetId = -2;
        } else {
            this.mNetId = -1;
        }
    }

    public void setNetworkName(String networkName) {
        this.mNetworkName = networkName;
    }

    public String getNetworkName() {
        return this.mNetworkName;
    }

    @UnsupportedAppUsage
    public void setIsGroupOwner(boolean isGo) {
        this.mIsGroupOwner = isGo;
    }

    public boolean isGroupOwner() {
        return this.mIsGroupOwner;
    }

    public void setOwner(WifiP2pDevice device) {
        this.mOwner = device;
    }

    public WifiP2pDevice getOwner() {
        return this.mOwner;
    }

    public void addClient(String address) {
        addClient(new WifiP2pDevice(address));
    }

    public void addClient(WifiP2pDevice device) {
        for (WifiP2pDevice client : this.mClients) {
            if (client.equals(device)) {
                return;
            }
        }
        this.mClients.add(device);
    }

    public boolean removeClient(String address) {
        return this.mClients.remove(new WifiP2pDevice(address));
    }

    public boolean removeClient(WifiP2pDevice device) {
        return this.mClients.remove(device);
    }

    @UnsupportedAppUsage
    public boolean isClientListEmpty() {
        return this.mClients.size() == 0;
    }

    public boolean contains(WifiP2pDevice device) {
        if (this.mOwner.equals(device) || this.mClients.contains(device)) {
            return true;
        }
        return false;
    }

    public Collection<WifiP2pDevice> getClientList() {
        return Collections.unmodifiableCollection(this.mClients);
    }

    public void setPassphrase(String passphrase) {
        this.mPassphrase = passphrase;
    }

    public String getPassphrase() {
        return this.mPassphrase;
    }

    @UnsupportedAppUsage
    public void setInterface(String intf) {
        this.mInterface = intf;
    }

    public String getInterface() {
        return this.mInterface;
    }

    @UnsupportedAppUsage
    public int getNetworkId() {
        return this.mNetId;
    }

    @UnsupportedAppUsage
    public void setNetworkId(int netId) {
        this.mNetId = netId;
    }

    public int getFrequency() {
        return this.mFrequency;
    }

    public void setFrequency(int freq) {
        this.mFrequency = freq;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("network: ");
        sbuf.append(this.mNetworkName);
        sbuf.append("\n isGO: ");
        sbuf.append(this.mIsGroupOwner);
        sbuf.append("\n GO: ");
        sbuf.append(this.mOwner);
        for (WifiP2pDevice client : this.mClients) {
            sbuf.append("\n Client: ");
            sbuf.append(client);
        }
        sbuf.append("\n interface: ");
        sbuf.append(this.mInterface);
        sbuf.append("\n networkId: ");
        sbuf.append(this.mNetId);
        sbuf.append("\n frequency: ");
        sbuf.append(this.mFrequency);
        return sbuf.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public WifiP2pGroup(WifiP2pGroup source) {
        if (source != null) {
            this.mNetworkName = source.getNetworkName();
            this.mOwner = new WifiP2pDevice(source.getOwner());
            this.mIsGroupOwner = source.mIsGroupOwner;
            for (WifiP2pDevice d : source.getClientList()) {
                this.mClients.add(d);
            }
            this.mPassphrase = source.getPassphrase();
            this.mInterface = source.getInterface();
            this.mNetId = source.getNetworkId();
            this.mFrequency = source.getFrequency();
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mNetworkName);
        dest.writeParcelable(this.mOwner, flags);
        dest.writeByte(this.mIsGroupOwner ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mClients.size());
        for (WifiP2pDevice client : this.mClients) {
            dest.writeParcelable(client, flags);
        }
        dest.writeString(this.mPassphrase);
        dest.writeString(this.mInterface);
        dest.writeInt(this.mNetId);
        dest.writeInt(this.mFrequency);
    }
}
