package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArraySet;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.BitUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
/* loaded from: classes2.dex */
public final class NetworkCapabilities implements Parcelable {
    private static final long DEFAULT_CAPABILITIES = 57344;
    private static final long FORCE_RESTRICTED_CAPABILITIES = 4194304;
    private static final int INVALID_UID = -1;
    public static final int LINK_BANDWIDTH_UNSPECIFIED = 0;
    private static final int MAX_NET_CAPABILITY = 22;
    public static final int MAX_TRANSPORT = 6;
    private static final int MIN_NET_CAPABILITY = 0;
    public static final int MIN_TRANSPORT = 0;
    private static final long MUTABLE_CAPABILITIES = 4145152;
    public static final int NET_CAPABILITY_CAPTIVE_PORTAL = 17;
    public static final int NET_CAPABILITY_CBS = 5;
    public static final int NET_CAPABILITY_DUN = 2;
    public static final int NET_CAPABILITY_EIMS = 10;
    public static final int NET_CAPABILITY_FOREGROUND = 19;
    public static final int NET_CAPABILITY_FOTA = 3;
    public static final int NET_CAPABILITY_IA = 7;
    public static final int NET_CAPABILITY_IMS = 4;
    public static final int NET_CAPABILITY_INTERNET = 12;
    public static final int NET_CAPABILITY_MMS = 0;
    public static final int NET_CAPABILITY_NOT_CONGESTED = 20;
    public static final int NET_CAPABILITY_NOT_METERED = 11;
    public static final int NET_CAPABILITY_NOT_RESTRICTED = 13;
    public static final int NET_CAPABILITY_NOT_ROAMING = 18;
    public static final int NET_CAPABILITY_NOT_SUSPENDED = 21;
    public static final int NET_CAPABILITY_NOT_VPN = 15;
    @SystemApi
    public static final int NET_CAPABILITY_OEM_PAID = 22;
    public static final int NET_CAPABILITY_RCS = 8;
    public static final int NET_CAPABILITY_SUPL = 1;
    public static final int NET_CAPABILITY_TRUSTED = 14;
    public static final int NET_CAPABILITY_VALIDATED = 16;
    public static final int NET_CAPABILITY_WIFI_P2P = 6;
    public static final int NET_CAPABILITY_XCAP = 9;
    private static final long NON_REQUESTABLE_CAPABILITIES = 4128768;
    @VisibleForTesting
    static final long RESTRICTED_CAPABILITIES = 1980;
    public static final int SIGNAL_STRENGTH_UNSPECIFIED = Integer.MIN_VALUE;
    private static final String TAG = "NetworkCapabilities";
    public static final int TRANSPORT_BLUETOOTH = 2;
    public static final int TRANSPORT_CELLULAR = 0;
    public static final int TRANSPORT_ETHERNET = 3;
    public static final int TRANSPORT_LOWPAN = 6;
    public static final int TRANSPORT_VPN = 4;
    public static final int TRANSPORT_WIFI = 1;
    public static final int TRANSPORT_WIFI_AWARE = 5;
    @VisibleForTesting
    static final long UNRESTRICTED_CAPABILITIES = 4163;
    public protected long mNetworkCapabilities;
    private String mSSID;
    private long mTransportTypes;
    private long mUnwantedNetworkCapabilities;
    private static final String[] TRANSPORT_NAMES = {"CELLULAR", "WIFI", "BLUETOOTH", "ETHERNET", "VPN", "WIFI_AWARE", "LOWPAN"};
    public static final Parcelable.Creator<NetworkCapabilities> CREATOR = new Parcelable.Creator<NetworkCapabilities>() { // from class: android.net.NetworkCapabilities.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkCapabilities createFromParcel(Parcel in) {
            NetworkCapabilities netCap = new NetworkCapabilities();
            netCap.mNetworkCapabilities = in.readLong();
            netCap.mUnwantedNetworkCapabilities = in.readLong();
            netCap.mTransportTypes = in.readLong();
            netCap.mLinkUpBandwidthKbps = in.readInt();
            netCap.mLinkDownBandwidthKbps = in.readInt();
            netCap.mNetworkSpecifier = (NetworkSpecifier) in.readParcelable(null);
            netCap.mSignalStrength = in.readInt();
            netCap.mUids = in.readArraySet(null);
            netCap.mSSID = in.readString();
            return netCap;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkCapabilities[] newArray(int size) {
            return new NetworkCapabilities[size];
        }
    };
    private int mEstablishingVpnAppUid = -1;
    private int mLinkUpBandwidthKbps = 0;
    private int mLinkDownBandwidthKbps = 0;
    private NetworkSpecifier mNetworkSpecifier = null;
    public protected int mSignalStrength = Integer.MIN_VALUE;
    private ArraySet<UidRange> mUids = null;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public interface NameOf {
        synchronized String nameOf(int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface NetCapability {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Transport {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NetworkCapabilities() {
        clearAll();
        this.mNetworkCapabilities = DEFAULT_CAPABILITIES;
    }

    public NetworkCapabilities(NetworkCapabilities nc) {
        if (nc != null) {
            set(nc);
        }
    }

    public synchronized void clearAll() {
        this.mUnwantedNetworkCapabilities = 0L;
        this.mTransportTypes = 0L;
        this.mNetworkCapabilities = 0L;
        this.mLinkDownBandwidthKbps = 0;
        this.mLinkUpBandwidthKbps = 0;
        this.mNetworkSpecifier = null;
        this.mSignalStrength = Integer.MIN_VALUE;
        this.mUids = null;
        this.mEstablishingVpnAppUid = -1;
        this.mSSID = null;
    }

    public synchronized void set(NetworkCapabilities nc) {
        this.mNetworkCapabilities = nc.mNetworkCapabilities;
        this.mTransportTypes = nc.mTransportTypes;
        this.mLinkUpBandwidthKbps = nc.mLinkUpBandwidthKbps;
        this.mLinkDownBandwidthKbps = nc.mLinkDownBandwidthKbps;
        this.mNetworkSpecifier = nc.mNetworkSpecifier;
        this.mSignalStrength = nc.mSignalStrength;
        setUids(nc.mUids);
        this.mEstablishingVpnAppUid = nc.mEstablishingVpnAppUid;
        this.mUnwantedNetworkCapabilities = nc.mUnwantedNetworkCapabilities;
        this.mSSID = nc.mSSID;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NetworkCapabilities addCapability(int capability) {
        checkValidCapability(capability);
        this.mNetworkCapabilities |= 1 << capability;
        this.mUnwantedNetworkCapabilities &= ~(1 << capability);
        return this;
    }

    public synchronized void addUnwantedCapability(int capability) {
        checkValidCapability(capability);
        this.mUnwantedNetworkCapabilities |= 1 << capability;
        this.mNetworkCapabilities &= ~(1 << capability);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NetworkCapabilities removeCapability(int capability) {
        checkValidCapability(capability);
        long mask = ~(1 << capability);
        this.mNetworkCapabilities &= mask;
        this.mUnwantedNetworkCapabilities &= mask;
        return this;
    }

    public synchronized NetworkCapabilities setCapability(int capability, boolean value) {
        if (value) {
            addCapability(capability);
        } else {
            removeCapability(capability);
        }
        return this;
    }

    public int[] getCapabilities() {
        return BitUtils.unpackBits(this.mNetworkCapabilities);
    }

    public synchronized int[] getUnwantedCapabilities() {
        return BitUtils.unpackBits(this.mUnwantedNetworkCapabilities);
    }

    public synchronized void setCapabilities(int[] capabilities, int[] unwantedCapabilities) {
        this.mNetworkCapabilities = BitUtils.packBits(capabilities);
        this.mUnwantedNetworkCapabilities = BitUtils.packBits(unwantedCapabilities);
    }

    @Deprecated
    public synchronized void setCapabilities(int[] capabilities) {
        setCapabilities(capabilities, new int[0]);
    }

    public boolean hasCapability(int capability) {
        return isValidCapability(capability) && (this.mNetworkCapabilities & ((long) (1 << capability))) != 0;
    }

    public synchronized boolean hasUnwantedCapability(int capability) {
        return isValidCapability(capability) && (this.mUnwantedNetworkCapabilities & ((long) (1 << capability))) != 0;
    }

    private synchronized void combineNetCapabilities(NetworkCapabilities nc) {
        this.mNetworkCapabilities |= nc.mNetworkCapabilities;
        this.mUnwantedNetworkCapabilities |= nc.mUnwantedNetworkCapabilities;
    }

    public synchronized String describeFirstNonRequestableCapability() {
        long nonRequestable = (this.mNetworkCapabilities | this.mUnwantedNetworkCapabilities) & NON_REQUESTABLE_CAPABILITIES;
        if (nonRequestable != 0) {
            return capabilityNameOf(BitUtils.unpackBits(nonRequestable)[0]);
        }
        if (this.mLinkUpBandwidthKbps != 0 || this.mLinkDownBandwidthKbps != 0) {
            return "link bandwidth";
        }
        if (hasSignalStrength()) {
            return "signalStrength";
        }
        return null;
    }

    private synchronized boolean satisfiedByNetCapabilities(NetworkCapabilities nc, boolean onlyImmutable) {
        long requestedCapabilities = this.mNetworkCapabilities;
        long requestedUnwantedCapabilities = this.mUnwantedNetworkCapabilities;
        long providedCapabilities = nc.mNetworkCapabilities;
        if (onlyImmutable) {
            requestedCapabilities &= -4145153;
            requestedUnwantedCapabilities &= -4145153;
        }
        return (providedCapabilities & requestedCapabilities) == requestedCapabilities && (requestedUnwantedCapabilities & providedCapabilities) == 0;
    }

    public synchronized boolean equalsNetCapabilities(NetworkCapabilities nc) {
        return nc.mNetworkCapabilities == this.mNetworkCapabilities && nc.mUnwantedNetworkCapabilities == this.mUnwantedNetworkCapabilities;
    }

    private synchronized boolean equalsNetCapabilitiesRequestable(NetworkCapabilities that) {
        return (this.mNetworkCapabilities & (-4128769)) == (that.mNetworkCapabilities & (-4128769)) && (this.mUnwantedNetworkCapabilities & (-4128769)) == ((-4128769) & that.mUnwantedNetworkCapabilities);
    }

    public synchronized void maybeMarkCapabilitiesRestricted() {
        boolean forceRestrictedCapability = (this.mNetworkCapabilities & 4194304) != 0;
        boolean hasUnrestrictedCapabilities = (this.mNetworkCapabilities & UNRESTRICTED_CAPABILITIES) != 0;
        boolean hasRestrictedCapabilities = (this.mNetworkCapabilities & RESTRICTED_CAPABILITIES) != 0;
        if (forceRestrictedCapability || (hasRestrictedCapabilities && !hasUnrestrictedCapabilities)) {
            removeCapability(13);
        }
    }

    public static synchronized boolean isValidTransport(int transportType) {
        return transportType >= 0 && transportType <= 6;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NetworkCapabilities addTransportType(int transportType) {
        checkValidTransportType(transportType);
        this.mTransportTypes |= 1 << transportType;
        setNetworkSpecifier(this.mNetworkSpecifier);
        return this;
    }

    public synchronized NetworkCapabilities removeTransportType(int transportType) {
        checkValidTransportType(transportType);
        this.mTransportTypes &= ~(1 << transportType);
        setNetworkSpecifier(this.mNetworkSpecifier);
        return this;
    }

    public synchronized NetworkCapabilities setTransportType(int transportType, boolean value) {
        if (value) {
            addTransportType(transportType);
        } else {
            removeTransportType(transportType);
        }
        return this;
    }

    public int[] getTransportTypes() {
        return BitUtils.unpackBits(this.mTransportTypes);
    }

    public synchronized void setTransportTypes(int[] transportTypes) {
        this.mTransportTypes = BitUtils.packBits(transportTypes);
    }

    public boolean hasTransport(int transportType) {
        return isValidTransport(transportType) && (this.mTransportTypes & ((long) (1 << transportType))) != 0;
    }

    private synchronized void combineTransportTypes(NetworkCapabilities nc) {
        this.mTransportTypes |= nc.mTransportTypes;
    }

    private synchronized boolean satisfiedByTransportTypes(NetworkCapabilities nc) {
        return this.mTransportTypes == 0 || (this.mTransportTypes & nc.mTransportTypes) != 0;
    }

    public synchronized boolean equalsTransportTypes(NetworkCapabilities nc) {
        return nc.mTransportTypes == this.mTransportTypes;
    }

    public synchronized void setEstablishingVpnAppUid(int uid) {
        this.mEstablishingVpnAppUid = uid;
    }

    public synchronized NetworkCapabilities setLinkUpstreamBandwidthKbps(int upKbps) {
        this.mLinkUpBandwidthKbps = upKbps;
        return this;
    }

    public int getLinkUpstreamBandwidthKbps() {
        return this.mLinkUpBandwidthKbps;
    }

    public synchronized NetworkCapabilities setLinkDownstreamBandwidthKbps(int downKbps) {
        this.mLinkDownBandwidthKbps = downKbps;
        return this;
    }

    public int getLinkDownstreamBandwidthKbps() {
        return this.mLinkDownBandwidthKbps;
    }

    private synchronized void combineLinkBandwidths(NetworkCapabilities nc) {
        this.mLinkUpBandwidthKbps = Math.max(this.mLinkUpBandwidthKbps, nc.mLinkUpBandwidthKbps);
        this.mLinkDownBandwidthKbps = Math.max(this.mLinkDownBandwidthKbps, nc.mLinkDownBandwidthKbps);
    }

    private synchronized boolean satisfiedByLinkBandwidths(NetworkCapabilities nc) {
        return this.mLinkUpBandwidthKbps <= nc.mLinkUpBandwidthKbps && this.mLinkDownBandwidthKbps <= nc.mLinkDownBandwidthKbps;
    }

    private synchronized boolean equalsLinkBandwidths(NetworkCapabilities nc) {
        return this.mLinkUpBandwidthKbps == nc.mLinkUpBandwidthKbps && this.mLinkDownBandwidthKbps == nc.mLinkDownBandwidthKbps;
    }

    public static synchronized int minBandwidth(int a, int b) {
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        return Math.min(a, b);
    }

    public static synchronized int maxBandwidth(int a, int b) {
        return Math.max(a, b);
    }

    public synchronized NetworkCapabilities setNetworkSpecifier(NetworkSpecifier networkSpecifier) {
        if (networkSpecifier != null && Long.bitCount(this.mTransportTypes) != 1) {
            throw new IllegalStateException("Must have a single transport specified to use setNetworkSpecifier");
        }
        this.mNetworkSpecifier = networkSpecifier;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NetworkSpecifier getNetworkSpecifier() {
        return this.mNetworkSpecifier;
    }

    private synchronized void combineSpecifiers(NetworkCapabilities nc) {
        if (this.mNetworkSpecifier != null && !this.mNetworkSpecifier.equals(nc.mNetworkSpecifier)) {
            throw new IllegalStateException("Can't combine two networkSpecifiers");
        }
        setNetworkSpecifier(nc.mNetworkSpecifier);
    }

    private synchronized boolean satisfiedBySpecifier(NetworkCapabilities nc) {
        return this.mNetworkSpecifier == null || this.mNetworkSpecifier.satisfiedBy(nc.mNetworkSpecifier) || (nc.mNetworkSpecifier instanceof MatchAllNetworkSpecifier);
    }

    private synchronized boolean equalsSpecifier(NetworkCapabilities nc) {
        return Objects.equals(this.mNetworkSpecifier, nc.mNetworkSpecifier);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NetworkCapabilities setSignalStrength(int signalStrength) {
        this.mSignalStrength = signalStrength;
        return this;
    }

    private protected boolean hasSignalStrength() {
        return this.mSignalStrength > Integer.MIN_VALUE;
    }

    private protected int getSignalStrength() {
        return this.mSignalStrength;
    }

    private synchronized void combineSignalStrength(NetworkCapabilities nc) {
        this.mSignalStrength = Math.max(this.mSignalStrength, nc.mSignalStrength);
    }

    private synchronized boolean satisfiedBySignalStrength(NetworkCapabilities nc) {
        return this.mSignalStrength <= nc.mSignalStrength;
    }

    private synchronized boolean equalsSignalStrength(NetworkCapabilities nc) {
        return this.mSignalStrength == nc.mSignalStrength;
    }

    public synchronized NetworkCapabilities setSingleUid(int uid) {
        ArraySet<UidRange> identity = new ArraySet<>(1);
        identity.add(new UidRange(uid, uid));
        setUids(identity);
        return this;
    }

    public synchronized NetworkCapabilities setUids(Set<UidRange> uids) {
        if (uids == null) {
            this.mUids = null;
        } else {
            this.mUids = new ArraySet<>(uids);
        }
        return this;
    }

    public synchronized Set<UidRange> getUids() {
        if (this.mUids == null) {
            return null;
        }
        return new ArraySet((ArraySet) this.mUids);
    }

    public synchronized boolean appliesToUid(int uid) {
        if (this.mUids == null) {
            return true;
        }
        Iterator<UidRange> it = this.mUids.iterator();
        while (it.hasNext()) {
            UidRange range = it.next();
            if (range.contains(uid)) {
                return true;
            }
        }
        return false;
    }

    @VisibleForTesting
    public synchronized boolean equalsUids(NetworkCapabilities nc) {
        Set<UidRange> comparedUids = nc.mUids;
        if (comparedUids == null) {
            return this.mUids == null;
        } else if (this.mUids == null) {
            return false;
        } else {
            Set<UidRange> uids = new ArraySet<>(this.mUids);
            for (UidRange range : comparedUids) {
                if (!uids.contains(range)) {
                    return false;
                }
                uids.remove(range);
            }
            return uids.isEmpty();
        }
    }

    public synchronized boolean satisfiedByUids(NetworkCapabilities nc) {
        if (nc.mUids == null || this.mUids == null) {
            return true;
        }
        Iterator<UidRange> it = this.mUids.iterator();
        while (it.hasNext()) {
            UidRange requiredRange = it.next();
            if (requiredRange.contains(nc.mEstablishingVpnAppUid)) {
                return true;
            }
            if (!nc.appliesToUidRange(requiredRange)) {
                return false;
            }
        }
        return true;
    }

    @VisibleForTesting
    public synchronized boolean appliesToUidRange(UidRange requiredRange) {
        if (this.mUids == null) {
            return true;
        }
        Iterator<UidRange> it = this.mUids.iterator();
        while (it.hasNext()) {
            UidRange uidRange = it.next();
            if (uidRange.containsRange(requiredRange)) {
                return true;
            }
        }
        return false;
    }

    private synchronized void combineUids(NetworkCapabilities nc) {
        if (nc.mUids == null || this.mUids == null) {
            this.mUids = null;
        } else {
            this.mUids.addAll((ArraySet<? extends UidRange>) nc.mUids);
        }
    }

    public synchronized NetworkCapabilities setSSID(String ssid) {
        this.mSSID = ssid;
        return this;
    }

    public synchronized String getSSID() {
        return this.mSSID;
    }

    public synchronized boolean equalsSSID(NetworkCapabilities nc) {
        return Objects.equals(this.mSSID, nc.mSSID);
    }

    public synchronized boolean satisfiedBySSID(NetworkCapabilities nc) {
        return this.mSSID == null || this.mSSID.equals(nc.mSSID);
    }

    private synchronized void combineSSIDs(NetworkCapabilities nc) {
        if (this.mSSID != null && !this.mSSID.equals(nc.mSSID)) {
            throw new IllegalStateException("Can't combine two SSIDs");
        }
        setSSID(nc.mSSID);
    }

    public synchronized void combineCapabilities(NetworkCapabilities nc) {
        combineNetCapabilities(nc);
        combineTransportTypes(nc);
        combineLinkBandwidths(nc);
        combineSpecifiers(nc);
        combineSignalStrength(nc);
        combineUids(nc);
        combineSSIDs(nc);
    }

    private synchronized boolean satisfiedByNetworkCapabilities(NetworkCapabilities nc, boolean onlyImmutable) {
        return nc != null && satisfiedByNetCapabilities(nc, onlyImmutable) && satisfiedByTransportTypes(nc) && (onlyImmutable || satisfiedByLinkBandwidths(nc)) && satisfiedBySpecifier(nc) && ((onlyImmutable || satisfiedBySignalStrength(nc)) && ((onlyImmutable || satisfiedByUids(nc)) && (onlyImmutable || satisfiedBySSID(nc))));
    }

    public synchronized boolean satisfiedByNetworkCapabilities(NetworkCapabilities nc) {
        return satisfiedByNetworkCapabilities(nc, false);
    }

    public synchronized boolean satisfiedByImmutableNetworkCapabilities(NetworkCapabilities nc) {
        return satisfiedByNetworkCapabilities(nc, true);
    }

    public synchronized String describeImmutableDifferences(NetworkCapabilities that) {
        if (that == null) {
            return "other NetworkCapabilities was null";
        }
        StringJoiner joiner = new StringJoiner(", ");
        long oldImmutableCapabilities = this.mNetworkCapabilities & (-4147201);
        long newImmutableCapabilities = (-4147201) & that.mNetworkCapabilities;
        if (oldImmutableCapabilities != newImmutableCapabilities) {
            String before = capabilityNamesOf(BitUtils.unpackBits(oldImmutableCapabilities));
            String after = capabilityNamesOf(BitUtils.unpackBits(newImmutableCapabilities));
            joiner.add(String.format("immutable capabilities changed: %s -> %s", before, after));
        }
        if (!equalsSpecifier(that)) {
            NetworkSpecifier before2 = getNetworkSpecifier();
            NetworkSpecifier after2 = that.getNetworkSpecifier();
            joiner.add(String.format("specifier changed: %s -> %s", before2, after2));
        }
        if (!equalsTransportTypes(that)) {
            String before3 = transportNamesOf(getTransportTypes());
            String after3 = transportNamesOf(that.getTransportTypes());
            joiner.add(String.format("transports changed: %s -> %s", before3, after3));
        }
        String before4 = joiner.toString();
        return before4;
    }

    public synchronized boolean equalRequestableCapabilities(NetworkCapabilities nc) {
        return nc != null && equalsNetCapabilitiesRequestable(nc) && equalsTransportTypes(nc) && equalsSpecifier(nc);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NetworkCapabilities)) {
            return false;
        }
        NetworkCapabilities that = (NetworkCapabilities) obj;
        return equalsNetCapabilities(that) && equalsTransportTypes(that) && equalsLinkBandwidths(that) && equalsSignalStrength(that) && equalsSpecifier(that) && equalsUids(that) && equalsSSID(that);
    }

    public int hashCode() {
        return ((int) (this.mNetworkCapabilities & (-1))) + (((int) (this.mNetworkCapabilities >> 32)) * 3) + (((int) (this.mUnwantedNetworkCapabilities & (-1))) * 5) + (((int) (this.mUnwantedNetworkCapabilities >> 32)) * 7) + (((int) ((-1) & this.mTransportTypes)) * 11) + (((int) (this.mTransportTypes >> 32)) * 13) + (this.mLinkUpBandwidthKbps * 17) + (this.mLinkDownBandwidthKbps * 19) + (Objects.hashCode(this.mNetworkSpecifier) * 23) + (this.mSignalStrength * 29) + (Objects.hashCode(this.mUids) * 31) + (Objects.hashCode(this.mSSID) * 37);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mNetworkCapabilities);
        dest.writeLong(this.mUnwantedNetworkCapabilities);
        dest.writeLong(this.mTransportTypes);
        dest.writeInt(this.mLinkUpBandwidthKbps);
        dest.writeInt(this.mLinkDownBandwidthKbps);
        dest.writeParcelable((Parcelable) this.mNetworkSpecifier, flags);
        dest.writeInt(this.mSignalStrength);
        dest.writeArraySet(this.mUids);
        dest.writeString(this.mSSID);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (0 != this.mTransportTypes) {
            sb.append(" Transports: ");
            appendStringRepresentationOfBitMaskToStringBuilder(sb, this.mTransportTypes, new NameOf() { // from class: android.net.-$$Lambda$FpGXkd3pLxeXY58eJ_84mi1PLWQ
                @Override // android.net.NetworkCapabilities.NameOf
                public final String nameOf(int i) {
                    return NetworkCapabilities.transportNameOf(i);
                }
            }, "|");
        }
        if (0 != this.mNetworkCapabilities) {
            sb.append(" Capabilities: ");
            appendStringRepresentationOfBitMaskToStringBuilder(sb, this.mNetworkCapabilities, new NameOf() { // from class: android.net.-$$Lambda$p1_56lwnt1xBuY1muPblbN1Dtkw
                @Override // android.net.NetworkCapabilities.NameOf
                public final String nameOf(int i) {
                    return NetworkCapabilities.capabilityNameOf(i);
                }
            }, "&");
        }
        if (0 != this.mNetworkCapabilities) {
            sb.append(" Unwanted: ");
            appendStringRepresentationOfBitMaskToStringBuilder(sb, this.mUnwantedNetworkCapabilities, new NameOf() { // from class: android.net.-$$Lambda$p1_56lwnt1xBuY1muPblbN1Dtkw
                @Override // android.net.NetworkCapabilities.NameOf
                public final String nameOf(int i) {
                    return NetworkCapabilities.capabilityNameOf(i);
                }
            }, "&");
        }
        if (this.mLinkUpBandwidthKbps > 0) {
            sb.append(" LinkUpBandwidth>=");
            sb.append(this.mLinkUpBandwidthKbps);
            sb.append("Kbps");
        }
        if (this.mLinkDownBandwidthKbps > 0) {
            sb.append(" LinkDnBandwidth>=");
            sb.append(this.mLinkDownBandwidthKbps);
            sb.append("Kbps");
        }
        if (this.mNetworkSpecifier != null) {
            sb.append(" Specifier: <");
            sb.append(this.mNetworkSpecifier);
            sb.append(">");
        }
        if (hasSignalStrength()) {
            sb.append(" SignalStrength: ");
            sb.append(this.mSignalStrength);
        }
        if (this.mUids != null) {
            if (1 == this.mUids.size() && this.mUids.valueAt(0).count() == 1) {
                sb.append(" Uid: ");
                sb.append(this.mUids.valueAt(0).start);
            } else {
                sb.append(" Uids: <");
                sb.append(this.mUids);
                sb.append(">");
            }
        }
        if (this.mEstablishingVpnAppUid != -1) {
            sb.append(" EstablishingAppUid: ");
            sb.append(this.mEstablishingVpnAppUid);
        }
        if (this.mSSID != null) {
            sb.append(" SSID: ");
            sb.append(this.mSSID);
        }
        sb.append("]");
        return sb.toString();
    }

    public static synchronized void appendStringRepresentationOfBitMaskToStringBuilder(StringBuilder sb, long bitMask, NameOf nameFetcher, String separator) {
        int bitPos = 0;
        boolean firstElementAdded = false;
        while (bitMask != 0) {
            if ((1 & bitMask) != 0) {
                if (firstElementAdded) {
                    sb.append(separator);
                } else {
                    firstElementAdded = true;
                }
                sb.append(nameFetcher.nameOf(bitPos));
            }
            bitMask >>= 1;
            bitPos++;
        }
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
        int[] transportTypes;
        int[] capabilities;
        long token = proto.start(fieldId);
        for (int transport : getTransportTypes()) {
            proto.write(2259152797697L, transport);
        }
        for (int capability : getCapabilities()) {
            proto.write(NetworkCapabilitiesProto.CAPABILITIES, capability);
        }
        proto.write(1120986464259L, this.mLinkUpBandwidthKbps);
        proto.write(1120986464260L, this.mLinkDownBandwidthKbps);
        if (this.mNetworkSpecifier != null) {
            proto.write(1138166333445L, this.mNetworkSpecifier.toString());
        }
        proto.write(1133871366150L, hasSignalStrength());
        proto.write(NetworkCapabilitiesProto.SIGNAL_STRENGTH, this.mSignalStrength);
        proto.end(token);
    }

    public static synchronized String capabilityNamesOf(int[] capabilities) {
        StringJoiner joiner = new StringJoiner("|");
        if (capabilities != null) {
            for (int c : capabilities) {
                joiner.add(capabilityNameOf(c));
            }
        }
        return joiner.toString();
    }

    public static synchronized String capabilityNameOf(int capability) {
        switch (capability) {
            case 0:
                return "MMS";
            case 1:
                return "SUPL";
            case 2:
                return "DUN";
            case 3:
                return "FOTA";
            case 4:
                return "IMS";
            case 5:
                return "CBS";
            case 6:
                return "WIFI_P2P";
            case 7:
                return "IA";
            case 8:
                return "RCS";
            case 9:
                return "XCAP";
            case 10:
                return "EIMS";
            case 11:
                return "NOT_METERED";
            case 12:
                return "INTERNET";
            case 13:
                return "NOT_RESTRICTED";
            case 14:
                return "TRUSTED";
            case 15:
                return "NOT_VPN";
            case 16:
                return "VALIDATED";
            case 17:
                return "CAPTIVE_PORTAL";
            case 18:
                return "NOT_ROAMING";
            case 19:
                return "FOREGROUND";
            case 20:
                return "NOT_CONGESTED";
            case 21:
                return "NOT_SUSPENDED";
            case 22:
                return "OEM_PAID";
            default:
                return Integer.toString(capability);
        }
    }

    private protected static String transportNamesOf(int[] types) {
        StringJoiner joiner = new StringJoiner("|");
        if (types != null) {
            for (int t : types) {
                joiner.add(transportNameOf(t));
            }
        }
        return joiner.toString();
    }

    public static synchronized String transportNameOf(int transport) {
        if (!isValidTransport(transport)) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return TRANSPORT_NAMES[transport];
    }

    private static synchronized void checkValidTransportType(int transport) {
        boolean isValidTransport = isValidTransport(transport);
        Preconditions.checkArgument(isValidTransport, "Invalid TransportType " + transport);
    }

    private static synchronized boolean isValidCapability(int capability) {
        return capability >= 0 && capability <= 22;
    }

    private static synchronized void checkValidCapability(int capability) {
        boolean isValidCapability = isValidCapability(capability);
        Preconditions.checkArgument(isValidCapability, "NetworkCapability " + capability + "out of range");
    }
}
