package android.net.wifi.rtt;

import android.location.Address;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

/* loaded from: classes2.dex */
public final class CivicLocation implements Parcelable {
    private static final int ADDRESS_LINE_0_ROOM_DESK_FLOOR = 0;
    private static final int ADDRESS_LINE_1_NUMBER_ROAD_SUFFIX_APT = 1;
    private static final int ADDRESS_LINE_2_CITY = 2;
    private static final int ADDRESS_LINE_3_STATE_POSTAL_CODE = 3;
    private static final int ADDRESS_LINE_4_COUNTRY = 4;
    private static final int BYTE_MASK = 255;
    private static final int COUNTRY_CODE_LENGTH = 2;
    public static final Parcelable.Creator<CivicLocation> CREATOR = new Parcelable.Creator<CivicLocation>() { // from class: android.net.wifi.rtt.CivicLocation.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CivicLocation createFromParcel(Parcel in) {
            return new CivicLocation(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CivicLocation[] newArray(int size) {
            return new CivicLocation[size];
        }
    };
    private static final int MAX_CIVIC_BUFFER_SIZE = 256;
    private static final int MIN_CIVIC_BUFFER_SIZE = 3;
    private static final int TLV_LENGTH_INDEX = 1;
    private static final int TLV_TYPE_INDEX = 0;
    private static final int TLV_VALUE_INDEX = 2;
    private SparseArray<String> mCivicAddressElements;
    private final String mCountryCode;
    private final boolean mIsValid;

    public CivicLocation(byte[] civicTLVs, String countryCode) {
        this.mCivicAddressElements = new SparseArray<>(3);
        this.mCountryCode = countryCode;
        if (countryCode == null || countryCode.length() != 2) {
            this.mIsValid = false;
            return;
        }
        boolean isValid = false;
        if (civicTLVs != null && civicTLVs.length >= 3 && civicTLVs.length < 256) {
            isValid = parseCivicTLVs(civicTLVs);
        }
        this.mIsValid = isValid;
    }

    private CivicLocation(Parcel in) {
        this.mCivicAddressElements = new SparseArray<>(3);
        this.mIsValid = in.readByte() != 0;
        this.mCountryCode = in.readString();
        this.mCivicAddressElements = in.readSparseArray(getClass().getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeByte(this.mIsValid ? (byte) 1 : (byte) 0);
        parcel.writeString(this.mCountryCode);
        parcel.writeSparseArray(this.mCivicAddressElements);
    }

    private boolean parseCivicTLVs(byte[] civicTLVs) {
        int bufferPtr = 0;
        int bufferLength = civicTLVs.length;
        while (bufferPtr < bufferLength) {
            int civicAddressType = civicTLVs[bufferPtr + 0] & 255;
            int civicAddressTypeLength = civicTLVs[bufferPtr + 1];
            if (civicAddressTypeLength != 0) {
                if (bufferPtr + 2 + civicAddressTypeLength > bufferLength) {
                    return false;
                }
                this.mCivicAddressElements.put(civicAddressType, new String(civicTLVs, bufferPtr + 2, civicAddressTypeLength, StandardCharsets.UTF_8));
            }
            bufferPtr += civicAddressTypeLength + 2;
        }
        return true;
    }

    public String getCivicElementValue(int key) {
        return this.mCivicAddressElements.get(key);
    }

    public SparseArray<String> toSparseArray() {
        return this.mCivicAddressElements;
    }

    public String toString() {
        return this.mCivicAddressElements.toString();
    }

    public Address toAddress() {
        if (!this.mIsValid) {
            return null;
        }
        Address address = new Address(Locale.US);
        String room = formatAddressElement("Room: ", getCivicElementValue(28));
        String desk = formatAddressElement(" Desk: ", getCivicElementValue(33));
        String floor = formatAddressElement(", Flr: ", getCivicElementValue(27));
        String houseNumber = formatAddressElement("", getCivicElementValue(19));
        String houseNumberSuffix = formatAddressElement("", getCivicElementValue(20));
        String road = formatAddressElement(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, getCivicElementValue(34));
        String roadSuffix = formatAddressElement(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, getCivicElementValue(18));
        String apt = formatAddressElement(", Apt: ", getCivicElementValue(26));
        String city = formatAddressElement("", getCivicElementValue(3));
        String state = formatAddressElement("", getCivicElementValue(1));
        String postalCode = formatAddressElement(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, getCivicElementValue(24));
        String addressLine0 = room + desk + floor;
        String addressLine1 = houseNumber + houseNumberSuffix + road + roadSuffix + apt;
        String addressLine3 = state + postalCode;
        String room2 = this.mCountryCode;
        address.setAddressLine(0, addressLine0);
        address.setAddressLine(1, addressLine1);
        address.setAddressLine(2, city);
        address.setAddressLine(3, addressLine3);
        address.setAddressLine(4, room2);
        address.setFeatureName(getCivicElementValue(23));
        address.setSubThoroughfare(getCivicElementValue(19));
        address.setThoroughfare(getCivicElementValue(34));
        address.setSubLocality(getCivicElementValue(5));
        address.setSubAdminArea(getCivicElementValue(2));
        address.setAdminArea(getCivicElementValue(1));
        address.setPostalCode(getCivicElementValue(24));
        address.setCountryCode(this.mCountryCode);
        return address;
    }

    private String formatAddressElement(String label, String value) {
        if (value != null) {
            return label + value;
        }
        return "";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CivicLocation) {
            CivicLocation other = (CivicLocation) obj;
            return this.mIsValid == other.mIsValid && Objects.equals(this.mCountryCode, other.mCountryCode) && isSparseArrayStringEqual(this.mCivicAddressElements, other.mCivicAddressElements);
        }
        return false;
    }

    public int hashCode() {
        int[] civicAddressKeys = getSparseArrayKeys(this.mCivicAddressElements);
        String[] civicAddressValues = getSparseArrayValues(this.mCivicAddressElements);
        return Objects.hash(Boolean.valueOf(this.mIsValid), this.mCountryCode, civicAddressKeys, civicAddressValues);
    }

    public boolean isValid() {
        return this.mIsValid;
    }

    private boolean isSparseArrayStringEqual(SparseArray<String> sa1, SparseArray<String> sa2) {
        int size = sa1.size();
        if (size != sa2.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            String sa1Value = sa1.valueAt(i);
            String sa2Value = sa2.valueAt(i);
            if (!sa1Value.equals(sa2Value)) {
                return false;
            }
        }
        return true;
    }

    private int[] getSparseArrayKeys(SparseArray<String> sa) {
        int size = sa.size();
        int[] keys = new int[size];
        for (int i = 0; i < size; i++) {
            keys[i] = sa.keyAt(i);
        }
        return keys;
    }

    private String[] getSparseArrayValues(SparseArray<String> sa) {
        int size = sa.size();
        String[] values = new String[size];
        for (int i = 0; i < size; i++) {
            values[i] = sa.valueAt(i);
        }
        return values;
    }
}
