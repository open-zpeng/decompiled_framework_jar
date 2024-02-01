package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class Policy implements Parcelable {
    public static final Parcelable.Creator<Policy> CREATOR = new Parcelable.Creator<Policy>() { // from class: android.net.wifi.hotspot2.pps.Policy.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Policy createFromParcel(Parcel in) {
            Policy policy = new Policy();
            policy.setMinHomeDownlinkBandwidth(in.readLong());
            policy.setMinHomeUplinkBandwidth(in.readLong());
            policy.setMinRoamingDownlinkBandwidth(in.readLong());
            policy.setMinRoamingUplinkBandwidth(in.readLong());
            policy.setExcludedSsidList(in.createStringArray());
            policy.setRequiredProtoPortMap(readProtoPortMap(in));
            policy.setMaximumBssLoadValue(in.readInt());
            policy.setPreferredRoamingPartnerList(readRoamingPartnerList(in));
            policy.setPolicyUpdate((UpdateParameter) in.readParcelable(null));
            return policy;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Policy[] newArray(int size) {
            return new Policy[size];
        }

        private Map<Integer, String> readProtoPortMap(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            Map<Integer, String> protoPortMap = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                int key = in.readInt();
                String value = in.readString();
                protoPortMap.put(Integer.valueOf(key), value);
            }
            return protoPortMap;
        }

        private List<RoamingPartner> readRoamingPartnerList(Parcel in) {
            int size = in.readInt();
            if (size == -1) {
                return null;
            }
            List<RoamingPartner> partnerList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                partnerList.add((RoamingPartner) in.readParcelable(null));
            }
            return partnerList;
        }
    };
    private static final int MAX_EXCLUSION_SSIDS = 128;
    private static final int MAX_PORT_STRING_BYTES = 64;
    private static final int MAX_SSID_BYTES = 32;
    private static final int NULL_VALUE = -1;
    private static final String TAG = "Policy";
    private String[] mExcludedSsidList;
    private int mMaximumBssLoadValue;
    private long mMinHomeDownlinkBandwidth;
    private long mMinHomeUplinkBandwidth;
    private long mMinRoamingDownlinkBandwidth;
    private long mMinRoamingUplinkBandwidth;
    private UpdateParameter mPolicyUpdate;
    private List<RoamingPartner> mPreferredRoamingPartnerList;
    private Map<Integer, String> mRequiredProtoPortMap;

    public synchronized void setMinHomeDownlinkBandwidth(long minHomeDownlinkBandwidth) {
        this.mMinHomeDownlinkBandwidth = minHomeDownlinkBandwidth;
    }

    public synchronized long getMinHomeDownlinkBandwidth() {
        return this.mMinHomeDownlinkBandwidth;
    }

    public synchronized void setMinHomeUplinkBandwidth(long minHomeUplinkBandwidth) {
        this.mMinHomeUplinkBandwidth = minHomeUplinkBandwidth;
    }

    public synchronized long getMinHomeUplinkBandwidth() {
        return this.mMinHomeUplinkBandwidth;
    }

    public synchronized void setMinRoamingDownlinkBandwidth(long minRoamingDownlinkBandwidth) {
        this.mMinRoamingDownlinkBandwidth = minRoamingDownlinkBandwidth;
    }

    public synchronized long getMinRoamingDownlinkBandwidth() {
        return this.mMinRoamingDownlinkBandwidth;
    }

    public synchronized void setMinRoamingUplinkBandwidth(long minRoamingUplinkBandwidth) {
        this.mMinRoamingUplinkBandwidth = minRoamingUplinkBandwidth;
    }

    public synchronized long getMinRoamingUplinkBandwidth() {
        return this.mMinRoamingUplinkBandwidth;
    }

    public synchronized void setExcludedSsidList(String[] excludedSsidList) {
        this.mExcludedSsidList = excludedSsidList;
    }

    public synchronized String[] getExcludedSsidList() {
        return this.mExcludedSsidList;
    }

    public synchronized void setRequiredProtoPortMap(Map<Integer, String> requiredProtoPortMap) {
        this.mRequiredProtoPortMap = requiredProtoPortMap;
    }

    public synchronized Map<Integer, String> getRequiredProtoPortMap() {
        return this.mRequiredProtoPortMap;
    }

    public synchronized void setMaximumBssLoadValue(int maximumBssLoadValue) {
        this.mMaximumBssLoadValue = maximumBssLoadValue;
    }

    public synchronized int getMaximumBssLoadValue() {
        return this.mMaximumBssLoadValue;
    }

    /* loaded from: classes2.dex */
    public static final class RoamingPartner implements Parcelable {
        public static final Parcelable.Creator<RoamingPartner> CREATOR = new Parcelable.Creator<RoamingPartner>() { // from class: android.net.wifi.hotspot2.pps.Policy.RoamingPartner.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public RoamingPartner createFromParcel(Parcel in) {
                RoamingPartner roamingPartner = new RoamingPartner();
                roamingPartner.setFqdn(in.readString());
                roamingPartner.setFqdnExactMatch(in.readInt() != 0);
                roamingPartner.setPriority(in.readInt());
                roamingPartner.setCountries(in.readString());
                return roamingPartner;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public RoamingPartner[] newArray(int size) {
                return new RoamingPartner[size];
            }
        };
        private String mCountries;
        private String mFqdn;
        private boolean mFqdnExactMatch;
        private int mPriority;

        public synchronized void setFqdn(String fqdn) {
            this.mFqdn = fqdn;
        }

        public synchronized String getFqdn() {
            return this.mFqdn;
        }

        public synchronized void setFqdnExactMatch(boolean fqdnExactMatch) {
            this.mFqdnExactMatch = fqdnExactMatch;
        }

        public synchronized boolean getFqdnExactMatch() {
            return this.mFqdnExactMatch;
        }

        public synchronized void setPriority(int priority) {
            this.mPriority = priority;
        }

        public synchronized int getPriority() {
            return this.mPriority;
        }

        public synchronized void setCountries(String countries) {
            this.mCountries = countries;
        }

        public synchronized String getCountries() {
            return this.mCountries;
        }

        public synchronized RoamingPartner() {
            this.mFqdn = null;
            this.mFqdnExactMatch = false;
            this.mPriority = Integer.MIN_VALUE;
            this.mCountries = null;
        }

        public synchronized RoamingPartner(RoamingPartner source) {
            this.mFqdn = null;
            this.mFqdnExactMatch = false;
            this.mPriority = Integer.MIN_VALUE;
            this.mCountries = null;
            if (source != null) {
                this.mFqdn = source.mFqdn;
                this.mFqdnExactMatch = source.mFqdnExactMatch;
                this.mPriority = source.mPriority;
                this.mCountries = source.mCountries;
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mFqdn);
            dest.writeInt(this.mFqdnExactMatch ? 1 : 0);
            dest.writeInt(this.mPriority);
            dest.writeString(this.mCountries);
        }

        public boolean equals(Object thatObject) {
            if (this == thatObject) {
                return true;
            }
            if (thatObject instanceof RoamingPartner) {
                RoamingPartner that = (RoamingPartner) thatObject;
                return TextUtils.equals(this.mFqdn, that.mFqdn) && this.mFqdnExactMatch == that.mFqdnExactMatch && this.mPriority == that.mPriority && TextUtils.equals(this.mCountries, that.mCountries);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.mFqdn, Boolean.valueOf(this.mFqdnExactMatch), Integer.valueOf(this.mPriority), this.mCountries);
        }

        public String toString() {
            return "FQDN: " + this.mFqdn + "\nExactMatch: mFqdnExactMatch\nPriority: " + this.mPriority + "\nCountries: " + this.mCountries + "\n";
        }

        public synchronized boolean validate() {
            if (TextUtils.isEmpty(this.mFqdn)) {
                Log.d(Policy.TAG, "Missing FQDN");
                return false;
            } else if (TextUtils.isEmpty(this.mCountries)) {
                Log.d(Policy.TAG, "Missing countries");
                return false;
            } else {
                return true;
            }
        }
    }

    public synchronized void setPreferredRoamingPartnerList(List<RoamingPartner> partnerList) {
        this.mPreferredRoamingPartnerList = partnerList;
    }

    public synchronized List<RoamingPartner> getPreferredRoamingPartnerList() {
        return this.mPreferredRoamingPartnerList;
    }

    public synchronized void setPolicyUpdate(UpdateParameter policyUpdate) {
        this.mPolicyUpdate = policyUpdate;
    }

    public synchronized UpdateParameter getPolicyUpdate() {
        return this.mPolicyUpdate;
    }

    public synchronized Policy() {
        this.mMinHomeDownlinkBandwidth = Long.MIN_VALUE;
        this.mMinHomeUplinkBandwidth = Long.MIN_VALUE;
        this.mMinRoamingDownlinkBandwidth = Long.MIN_VALUE;
        this.mMinRoamingUplinkBandwidth = Long.MIN_VALUE;
        this.mExcludedSsidList = null;
        this.mRequiredProtoPortMap = null;
        this.mMaximumBssLoadValue = Integer.MIN_VALUE;
        this.mPreferredRoamingPartnerList = null;
        this.mPolicyUpdate = null;
    }

    public synchronized Policy(Policy source) {
        this.mMinHomeDownlinkBandwidth = Long.MIN_VALUE;
        this.mMinHomeUplinkBandwidth = Long.MIN_VALUE;
        this.mMinRoamingDownlinkBandwidth = Long.MIN_VALUE;
        this.mMinRoamingUplinkBandwidth = Long.MIN_VALUE;
        this.mExcludedSsidList = null;
        this.mRequiredProtoPortMap = null;
        this.mMaximumBssLoadValue = Integer.MIN_VALUE;
        this.mPreferredRoamingPartnerList = null;
        this.mPolicyUpdate = null;
        if (source == null) {
            return;
        }
        this.mMinHomeDownlinkBandwidth = source.mMinHomeDownlinkBandwidth;
        this.mMinHomeUplinkBandwidth = source.mMinHomeUplinkBandwidth;
        this.mMinRoamingDownlinkBandwidth = source.mMinRoamingDownlinkBandwidth;
        this.mMinRoamingUplinkBandwidth = source.mMinRoamingUplinkBandwidth;
        this.mMaximumBssLoadValue = source.mMaximumBssLoadValue;
        if (source.mExcludedSsidList != null) {
            this.mExcludedSsidList = (String[]) Arrays.copyOf(source.mExcludedSsidList, source.mExcludedSsidList.length);
        }
        if (source.mRequiredProtoPortMap != null) {
            this.mRequiredProtoPortMap = Collections.unmodifiableMap(source.mRequiredProtoPortMap);
        }
        if (source.mPreferredRoamingPartnerList != null) {
            this.mPreferredRoamingPartnerList = Collections.unmodifiableList(source.mPreferredRoamingPartnerList);
        }
        if (source.mPolicyUpdate != null) {
            this.mPolicyUpdate = new UpdateParameter(source.mPolicyUpdate);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mMinHomeDownlinkBandwidth);
        dest.writeLong(this.mMinHomeUplinkBandwidth);
        dest.writeLong(this.mMinRoamingDownlinkBandwidth);
        dest.writeLong(this.mMinRoamingUplinkBandwidth);
        dest.writeStringArray(this.mExcludedSsidList);
        writeProtoPortMap(dest, this.mRequiredProtoPortMap);
        dest.writeInt(this.mMaximumBssLoadValue);
        writeRoamingPartnerList(dest, flags, this.mPreferredRoamingPartnerList);
        dest.writeParcelable(this.mPolicyUpdate, flags);
    }

    public boolean equals(Object thatObject) {
        if (this == thatObject) {
            return true;
        }
        if (thatObject instanceof Policy) {
            Policy that = (Policy) thatObject;
            if (this.mMinHomeDownlinkBandwidth == that.mMinHomeDownlinkBandwidth && this.mMinHomeUplinkBandwidth == that.mMinHomeUplinkBandwidth && this.mMinRoamingDownlinkBandwidth == that.mMinRoamingDownlinkBandwidth && this.mMinRoamingUplinkBandwidth == that.mMinRoamingUplinkBandwidth && Arrays.equals(this.mExcludedSsidList, that.mExcludedSsidList) && (this.mRequiredProtoPortMap != null ? this.mRequiredProtoPortMap.equals(that.mRequiredProtoPortMap) : that.mRequiredProtoPortMap == null) && this.mMaximumBssLoadValue == that.mMaximumBssLoadValue && (this.mPreferredRoamingPartnerList != null ? this.mPreferredRoamingPartnerList.equals(that.mPreferredRoamingPartnerList) : that.mPreferredRoamingPartnerList == null)) {
                if (this.mPolicyUpdate == null) {
                    if (that.mPolicyUpdate == null) {
                        return true;
                    }
                } else if (this.mPolicyUpdate.equals(that.mPolicyUpdate)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mMinHomeDownlinkBandwidth), Long.valueOf(this.mMinHomeUplinkBandwidth), Long.valueOf(this.mMinRoamingDownlinkBandwidth), Long.valueOf(this.mMinRoamingUplinkBandwidth), this.mExcludedSsidList, this.mRequiredProtoPortMap, Integer.valueOf(this.mMaximumBssLoadValue), this.mPreferredRoamingPartnerList, this.mPolicyUpdate);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MinHomeDownlinkBandwidth: ");
        builder.append(this.mMinHomeDownlinkBandwidth);
        builder.append("\n");
        builder.append("MinHomeUplinkBandwidth: ");
        builder.append(this.mMinHomeUplinkBandwidth);
        builder.append("\n");
        builder.append("MinRoamingDownlinkBandwidth: ");
        builder.append(this.mMinRoamingDownlinkBandwidth);
        builder.append("\n");
        builder.append("MinRoamingUplinkBandwidth: ");
        builder.append(this.mMinRoamingUplinkBandwidth);
        builder.append("\n");
        builder.append("ExcludedSSIDList: ");
        builder.append(this.mExcludedSsidList);
        builder.append("\n");
        builder.append("RequiredProtoPortMap: ");
        builder.append(this.mRequiredProtoPortMap);
        builder.append("\n");
        builder.append("MaximumBSSLoadValue: ");
        builder.append(this.mMaximumBssLoadValue);
        builder.append("\n");
        builder.append("PreferredRoamingPartnerList: ");
        builder.append(this.mPreferredRoamingPartnerList);
        builder.append("\n");
        if (this.mPolicyUpdate != null) {
            builder.append("PolicyUpdate Begin ---\n");
            builder.append(this.mPolicyUpdate);
            builder.append("PolicyUpdate End ---\n");
        }
        return builder.toString();
    }

    public synchronized boolean validate() {
        String[] strArr;
        if (this.mPolicyUpdate == null) {
            Log.d(TAG, "PolicyUpdate not specified");
            return false;
        } else if (this.mPolicyUpdate.validate()) {
            if (this.mExcludedSsidList != null) {
                if (this.mExcludedSsidList.length > 128) {
                    Log.d(TAG, "SSID exclusion list size exceeded the max: " + this.mExcludedSsidList.length);
                    return false;
                }
                for (String ssid : this.mExcludedSsidList) {
                    if (ssid.getBytes(StandardCharsets.UTF_8).length > 32) {
                        Log.d(TAG, "Invalid SSID: " + ssid);
                        return false;
                    }
                }
            }
            if (this.mRequiredProtoPortMap != null) {
                for (Map.Entry<Integer, String> entry : this.mRequiredProtoPortMap.entrySet()) {
                    String portNumber = entry.getValue();
                    if (portNumber.getBytes(StandardCharsets.UTF_8).length > 64) {
                        Log.d(TAG, "PortNumber string bytes exceeded the max: " + portNumber);
                        return false;
                    }
                }
            }
            if (this.mPreferredRoamingPartnerList != null) {
                for (RoamingPartner partner : this.mPreferredRoamingPartnerList) {
                    if (!partner.validate()) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        } else {
            return false;
        }
    }

    private static synchronized void writeProtoPortMap(Parcel dest, Map<Integer, String> protoPortMap) {
        if (protoPortMap == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(protoPortMap.size());
        for (Map.Entry<Integer, String> entry : protoPortMap.entrySet()) {
            dest.writeInt(entry.getKey().intValue());
            dest.writeString(entry.getValue());
        }
    }

    private static synchronized void writeRoamingPartnerList(Parcel dest, int flags, List<RoamingPartner> partnerList) {
        if (partnerList == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(partnerList.size());
        for (RoamingPartner partner : partnerList) {
            dest.writeParcelable(partner, flags);
        }
    }
}
