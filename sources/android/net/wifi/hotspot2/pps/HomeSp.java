package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/* loaded from: classes2.dex */
public final class HomeSp implements Parcelable {
    public static final Parcelable.Creator<HomeSp> CREATOR = new Parcelable.Creator<HomeSp>() { // from class: android.net.wifi.hotspot2.pps.HomeSp.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HomeSp createFromParcel(Parcel in) {
            HomeSp homeSp = new HomeSp();
            homeSp.setFqdn(in.readString());
            homeSp.setFriendlyName(in.readString());
            homeSp.setIconUrl(in.readString());
            homeSp.setHomeNetworkIds(readHomeNetworkIds(in));
            homeSp.setMatchAllOis(in.createLongArray());
            homeSp.setMatchAnyOis(in.createLongArray());
            homeSp.setOtherHomePartners(in.createStringArray());
            homeSp.setRoamingConsortiumOis(in.createLongArray());
            return homeSp;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HomeSp[] newArray(int size) {
            return new HomeSp[size];
        }

        private Map<String, Long> readHomeNetworkIds(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            Map<String, Long> networkIds = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                String key = in.readString();
                Long value = null;
                long readValue = in.readLong();
                if (readValue != -1) {
                    value = Long.valueOf(readValue);
                }
                networkIds.put(key, value);
            }
            return networkIds;
        }
    };
    private static final int MAX_SSID_BYTES = 32;
    private static final int NULL_VALUE = -1;
    private static final String TAG = "HomeSp";
    private String mFqdn;
    private String mFriendlyName;
    private Map<String, Long> mHomeNetworkIds;
    private String mIconUrl;
    private long[] mMatchAllOis;
    private long[] mMatchAnyOis;
    private String[] mOtherHomePartners;
    private long[] mRoamingConsortiumOis;

    public void setFqdn(String fqdn) {
        this.mFqdn = fqdn;
    }

    public String getFqdn() {
        return this.mFqdn;
    }

    public void setFriendlyName(String friendlyName) {
        this.mFriendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return this.mFriendlyName;
    }

    public void setIconUrl(String iconUrl) {
        this.mIconUrl = iconUrl;
    }

    public String getIconUrl() {
        return this.mIconUrl;
    }

    public void setHomeNetworkIds(Map<String, Long> homeNetworkIds) {
        this.mHomeNetworkIds = homeNetworkIds;
    }

    public Map<String, Long> getHomeNetworkIds() {
        return this.mHomeNetworkIds;
    }

    public void setMatchAllOis(long[] matchAllOis) {
        this.mMatchAllOis = matchAllOis;
    }

    public long[] getMatchAllOis() {
        return this.mMatchAllOis;
    }

    public void setMatchAnyOis(long[] matchAnyOis) {
        this.mMatchAnyOis = matchAnyOis;
    }

    public long[] getMatchAnyOis() {
        return this.mMatchAnyOis;
    }

    public void setOtherHomePartners(String[] otherHomePartners) {
        this.mOtherHomePartners = otherHomePartners;
    }

    public String[] getOtherHomePartners() {
        return this.mOtherHomePartners;
    }

    public void setRoamingConsortiumOis(long[] roamingConsortiumOis) {
        this.mRoamingConsortiumOis = roamingConsortiumOis;
    }

    public long[] getRoamingConsortiumOis() {
        return this.mRoamingConsortiumOis;
    }

    public HomeSp() {
        this.mFqdn = null;
        this.mFriendlyName = null;
        this.mIconUrl = null;
        this.mHomeNetworkIds = null;
        this.mMatchAllOis = null;
        this.mMatchAnyOis = null;
        this.mOtherHomePartners = null;
        this.mRoamingConsortiumOis = null;
    }

    public HomeSp(HomeSp source) {
        this.mFqdn = null;
        this.mFriendlyName = null;
        this.mIconUrl = null;
        this.mHomeNetworkIds = null;
        this.mMatchAllOis = null;
        this.mMatchAnyOis = null;
        this.mOtherHomePartners = null;
        this.mRoamingConsortiumOis = null;
        if (source == null) {
            return;
        }
        this.mFqdn = source.mFqdn;
        this.mFriendlyName = source.mFriendlyName;
        this.mIconUrl = source.mIconUrl;
        Map<String, Long> map = source.mHomeNetworkIds;
        if (map != null) {
            this.mHomeNetworkIds = Collections.unmodifiableMap(map);
        }
        long[] jArr = source.mMatchAllOis;
        if (jArr != null) {
            this.mMatchAllOis = Arrays.copyOf(jArr, jArr.length);
        }
        long[] jArr2 = source.mMatchAnyOis;
        if (jArr2 != null) {
            this.mMatchAnyOis = Arrays.copyOf(jArr2, jArr2.length);
        }
        String[] strArr = source.mOtherHomePartners;
        if (strArr != null) {
            this.mOtherHomePartners = (String[]) Arrays.copyOf(strArr, strArr.length);
        }
        long[] jArr3 = source.mRoamingConsortiumOis;
        if (jArr3 != null) {
            this.mRoamingConsortiumOis = Arrays.copyOf(jArr3, jArr3.length);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFqdn);
        dest.writeString(this.mFriendlyName);
        dest.writeString(this.mIconUrl);
        writeHomeNetworkIds(dest, this.mHomeNetworkIds);
        dest.writeLongArray(this.mMatchAllOis);
        dest.writeLongArray(this.mMatchAnyOis);
        dest.writeStringArray(this.mOtherHomePartners);
        dest.writeLongArray(this.mRoamingConsortiumOis);
    }

    public boolean equals(Object thatObject) {
        Map<String, Long> map;
        if (this == thatObject) {
            return true;
        }
        if (thatObject instanceof HomeSp) {
            HomeSp that = (HomeSp) thatObject;
            return TextUtils.equals(this.mFqdn, that.mFqdn) && TextUtils.equals(this.mFriendlyName, that.mFriendlyName) && TextUtils.equals(this.mIconUrl, that.mIconUrl) && ((map = this.mHomeNetworkIds) != null ? map.equals(that.mHomeNetworkIds) : that.mHomeNetworkIds == null) && Arrays.equals(this.mMatchAllOis, that.mMatchAllOis) && Arrays.equals(this.mMatchAnyOis, that.mMatchAnyOis) && Arrays.equals(this.mOtherHomePartners, that.mOtherHomePartners) && Arrays.equals(this.mRoamingConsortiumOis, that.mRoamingConsortiumOis);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mFqdn, this.mFriendlyName, this.mIconUrl, this.mHomeNetworkIds, this.mMatchAllOis, this.mMatchAnyOis, this.mOtherHomePartners, this.mRoamingConsortiumOis);
    }

    public String toString() {
        return "FQDN: " + this.mFqdn + "\nFriendlyName: " + this.mFriendlyName + "\nIconURL: " + this.mIconUrl + "\nHomeNetworkIDs: " + this.mHomeNetworkIds + "\nMatchAllOIs: " + this.mMatchAllOis + "\nMatchAnyOIs: " + this.mMatchAnyOis + "\nOtherHomePartners: " + this.mOtherHomePartners + "\nRoamingConsortiumOIs: " + this.mRoamingConsortiumOis + "\n";
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0031  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean validate() {
        /*
            r6 = this;
            java.lang.String r0 = r6.mFqdn
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            r1 = 0
            java.lang.String r2 = "HomeSp"
            if (r0 == 0) goto L11
            java.lang.String r0 = "Missing FQDN"
            android.util.Log.d(r2, r0)
            return r1
        L11:
            java.lang.String r0 = r6.mFriendlyName
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L1f
            java.lang.String r0 = "Missing friendly name"
            android.util.Log.d(r2, r0)
            return r1
        L1f:
            java.util.Map<java.lang.String, java.lang.Long> r0 = r6.mHomeNetworkIds
            if (r0 == 0) goto L56
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
        L2b:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L56
            java.lang.Object r3 = r0.next()
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3
            java.lang.Object r4 = r3.getKey()
            if (r4 == 0) goto L50
            java.lang.Object r4 = r3.getKey()
            java.lang.String r4 = (java.lang.String) r4
            java.nio.charset.Charset r5 = java.nio.charset.StandardCharsets.UTF_8
            byte[] r4 = r4.getBytes(r5)
            int r4 = r4.length
            r5 = 32
            if (r4 <= r5) goto L4f
            goto L50
        L4f:
            goto L2b
        L50:
            java.lang.String r0 = "Invalid SSID in HomeNetworkIDs"
            android.util.Log.d(r2, r0)
            return r1
        L56:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.hotspot2.pps.HomeSp.validate():boolean");
    }

    private static void writeHomeNetworkIds(Parcel dest, Map<String, Long> networkIds) {
        if (networkIds == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(networkIds.size());
        for (Map.Entry<String, Long> entry : networkIds.entrySet()) {
            dest.writeString(entry.getKey());
            if (entry.getValue() == null) {
                dest.writeLong(-1L);
            } else {
                dest.writeLong(entry.getValue().longValue());
            }
        }
    }
}
