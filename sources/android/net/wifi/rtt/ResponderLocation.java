package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.location.Address;
import android.location.Location;
import android.net.MacAddress;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.webkit.MimeTypeMap;
import com.xiaopeng.util.FeatureOption;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public final class ResponderLocation implements Parcelable {
    public static final int ALTITUDE_FLOORS = 2;
    private static final int ALTITUDE_FRACTION_BITS = 8;
    public static final int ALTITUDE_METERS = 1;
    private static final int ALTITUDE_UNCERTAINTY_BASE = 21;
    public static final int ALTITUDE_UNDEFINED = 0;
    private static final int BYTES_IN_A_BSSID = 6;
    private static final int BYTE_MASK = 255;
    private static final int CIVIC_COUNTRY_CODE_INDEX = 0;
    private static final int CIVIC_TLV_LIST_INDEX = 2;
    public static final int DATUM_NAD83_MLLW = 3;
    public static final int DATUM_NAD83_NAV88 = 2;
    public static final int DATUM_UNDEFINED = 0;
    public static final int DATUM_WGS84 = 1;
    private static final int LATLNG_FRACTION_BITS = 25;
    private static final int LATLNG_UNCERTAINTY_BASE = 8;
    private static final double LAT_ABS_LIMIT = 90.0d;
    public static final int LCI_VERSION_1 = 1;
    private static final double LNG_ABS_LIMIT = 180.0d;
    public static final int LOCATION_FIXED = 0;
    public static final int LOCATION_MOVEMENT_UNKNOWN = 2;
    private static final String LOCATION_PROVIDER = "WiFi Access Point";
    public static final int LOCATION_RESERVED = 3;
    public static final int LOCATION_VARIABLE = 1;
    private static final int LSB_IN_BYTE = 1;
    private static final int MAP_TYPE_URL_DEFINED = 0;
    private static final int MAX_BUFFER_SIZE = 256;
    private static final byte MEASUREMENT_REPORT_MODE = 0;
    private static final byte MEASUREMENT_TOKEN_AUTONOMOUS = 1;
    private static final byte MEASUREMENT_TYPE_LCI = 8;
    private static final int MIN_BUFFER_SIZE = 3;
    private static final int MSB_IN_BYTE = 128;
    private static final byte SUBELEMENT_BSSID_LIST = 7;
    private static final int SUBELEMENT_BSSID_LIST_INDEX = 1;
    private static final int SUBELEMENT_BSSID_LIST_MIN_BUFFER_LENGTH = 1;
    private static final int SUBELEMENT_BSSID_MAX_INDICATOR_INDEX = 0;
    private static final int SUBELEMENT_IMAGE_MAP_TYPE_INDEX = 0;
    private static final byte SUBELEMENT_LCI = 0;
    private static final int SUBELEMENT_LCI_ALT_INDEX = 6;
    private static final int SUBELEMENT_LCI_ALT_TYPE_INDEX = 4;
    private static final int SUBELEMENT_LCI_ALT_UNCERTAINTY_INDEX = 5;
    private static final int SUBELEMENT_LCI_DATUM_INDEX = 7;
    private static final int SUBELEMENT_LCI_DEPENDENT_STA_INDEX = 10;
    private static final int SUBELEMENT_LCI_LAT_INDEX = 1;
    private static final int SUBELEMENT_LCI_LAT_UNCERTAINTY_INDEX = 0;
    private static final int SUBELEMENT_LCI_LENGTH = 16;
    private static final int SUBELEMENT_LCI_LNG_INDEX = 3;
    private static final int SUBELEMENT_LCI_LNG_UNCERTAINTY_INDEX = 2;
    private static final int SUBELEMENT_LCI_REGLOC_AGREEMENT_INDEX = 8;
    private static final int SUBELEMENT_LCI_REGLOC_DSE_INDEX = 9;
    private static final int SUBELEMENT_LCI_VERSION_INDEX = 11;
    private static final byte SUBELEMENT_LOCATION_CIVIC = 0;
    private static final int SUBELEMENT_LOCATION_CIVIC_MAX_LENGTH = 256;
    private static final int SUBELEMENT_LOCATION_CIVIC_MIN_LENGTH = 2;
    private static final byte SUBELEMENT_MAP_IMAGE = 5;
    private static final int SUBELEMENT_MAP_IMAGE_URL_MAX_LENGTH = 256;
    private static final byte SUBELEMENT_USAGE = 6;
    private static final int SUBELEMENT_USAGE_LENGTH1 = 1;
    private static final int SUBELEMENT_USAGE_LENGTH3 = 3;
    private static final int SUBELEMENT_USAGE_MASK_RETENTION_EXPIRES = 2;
    private static final int SUBELEMENT_USAGE_MASK_RETRANSMIT = 1;
    private static final int SUBELEMENT_USAGE_MASK_STA_LOCATION_POLICY = 4;
    private static final int SUBELEMENT_USAGE_PARAMS_INDEX = 0;
    private static final byte SUBELEMENT_Z = 4;
    private static final int SUBELEMENT_Z_FLOOR_NUMBER_INDEX = 1;
    private static final int SUBELEMENT_Z_HEIGHT_ABOVE_FLOOR_INDEX = 2;
    private static final int SUBELEMENT_Z_HEIGHT_ABOVE_FLOOR_UNCERTAINTY_INDEX = 3;
    private static final int SUBELEMENT_Z_LAT_EXPECTED_TO_MOVE_INDEX = 0;
    private static final int SUBELEMENT_Z_LENGTH = 6;
    private static final int UNCERTAINTY_UNDEFINED = 0;
    private static final int Z_FLOOR_HEIGHT_FRACTION_BITS = 12;
    private static final int Z_FLOOR_NUMBER_FRACTION_BITS = 4;
    private static final int Z_MAX_HEIGHT_UNCERTAINTY_FACTOR = 25;
    private double mAltitude;
    private int mAltitudeType;
    private double mAltitudeUncertainty;
    private ArrayList<MacAddress> mBssidList;
    private CivicLocation mCivicLocation;
    private String mCivicLocationCountryCode;
    private String mCivicLocationString;
    private int mDatum;
    private int mExpectedToMove;
    private double mFloorNumber;
    private double mHeightAboveFloorMeters;
    private double mHeightAboveFloorUncertaintyMeters;
    private boolean mIsBssidListValid;
    private boolean mIsLciValid;
    private boolean mIsLocationCivicValid;
    private boolean mIsMapImageValid;
    private boolean mIsUsageValid;
    private final boolean mIsValid;
    private boolean mIsZValid;
    private double mLatitude;
    private double mLatitudeUncertainty;
    private boolean mLciDependentStation;
    private boolean mLciRegisteredLocationAgreement;
    private boolean mLciRegisteredLocationDse;
    private int mLciVersion;
    private double mLongitude;
    private double mLongitudeUncertainty;
    private int mMapImageType;
    private Uri mMapImageUri;
    private boolean mUsageExtraInfoOnAssociation;
    private boolean mUsageRetentionExpires;
    private boolean mUsageRetransmit;
    private static final byte[] LEAD_LCI_ELEMENT_BYTES = {1, 0, 8};
    private static final int[] SUBELEMENT_LCI_BIT_FIELD_LENGTHS = {6, 34, 6, 34, 4, 6, 30, 3, 1, 1, 1, 2};
    private static final int[] SUBELEMENT_Z_BIT_FIELD_LENGTHS = {2, 14, 24, 8};
    private static final byte MEASUREMENT_TYPE_LCR = 11;
    private static final byte[] LEAD_LCR_ELEMENT_BYTES = {1, 0, MEASUREMENT_TYPE_LCR};
    private static final String[] SUPPORTED_IMAGE_FILE_EXTENSIONS = {"", "png", "gif", "jpg", "svg", "dxf", "dwg", "dwf", "cad", "tif", "gml", "kml", "bmp", "pgm", "ppm", "xbm", "xpm", "ico"};
    public static final Parcelable.Creator<ResponderLocation> CREATOR = new Parcelable.Creator<ResponderLocation>() { // from class: android.net.wifi.rtt.ResponderLocation.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ResponderLocation createFromParcel(Parcel in) {
            return new ResponderLocation(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ResponderLocation[] newArray(int size) {
            return new ResponderLocation[size];
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface AltitudeType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface DatumType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ExpectedToMoveType {
    }

    public ResponderLocation(byte[] lciBuffer, byte[] lcrBuffer) {
        boolean isLciValid;
        boolean isLcrValid;
        boolean z = false;
        this.mIsLciValid = false;
        this.mIsZValid = false;
        this.mIsUsageValid = true;
        this.mIsBssidListValid = false;
        this.mIsLocationCivicValid = false;
        this.mIsMapImageValid = false;
        boolean isLciIeValid = false;
        boolean isLcrIeValid = false;
        setLciSubelementDefaults();
        setZaxisSubelementDefaults();
        setUsageSubelementDefaults();
        setBssidListSubelementDefaults();
        setCivicLocationSubelementDefaults();
        setMapImageSubelementDefaults();
        if (lciBuffer != null) {
            int length = lciBuffer.length;
            byte[] bArr = LEAD_LCI_ELEMENT_BYTES;
            if (length > bArr.length) {
                isLciIeValid = parseInformationElementBuffer(8, lciBuffer, bArr);
            }
        }
        if (lcrBuffer != null) {
            int length2 = lcrBuffer.length;
            byte[] bArr2 = LEAD_LCR_ELEMENT_BYTES;
            if (length2 > bArr2.length) {
                isLcrIeValid = parseInformationElementBuffer(11, lcrBuffer, bArr2);
            }
        }
        if (!isLciIeValid || !this.mIsUsageValid || (!this.mIsLciValid && !this.mIsZValid && !this.mIsBssidListValid)) {
            isLciValid = false;
        } else {
            isLciValid = true;
        }
        if (!isLcrIeValid || !this.mIsUsageValid || (!this.mIsLocationCivicValid && !this.mIsMapImageValid)) {
            isLcrValid = false;
        } else {
            isLcrValid = true;
        }
        this.mIsValid = (isLciValid || isLcrValid) ? true : z;
        if (!this.mIsValid) {
            setLciSubelementDefaults();
            setZaxisSubelementDefaults();
            setCivicLocationSubelementDefaults();
            setMapImageSubelementDefaults();
        }
    }

    private ResponderLocation(Parcel in) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        this.mIsLciValid = false;
        this.mIsZValid = false;
        this.mIsUsageValid = true;
        this.mIsBssidListValid = false;
        this.mIsLocationCivicValid = false;
        this.mIsMapImageValid = false;
        if (in.readByte() == 0) {
            z = false;
        } else {
            z = true;
        }
        this.mIsValid = z;
        if (in.readByte() == 0) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.mIsLciValid = z2;
        if (in.readByte() == 0) {
            z3 = false;
        } else {
            z3 = true;
        }
        this.mIsZValid = z3;
        if (in.readByte() == 0) {
            z4 = false;
        } else {
            z4 = true;
        }
        this.mIsUsageValid = z4;
        if (in.readByte() == 0) {
            z5 = false;
        } else {
            z5 = true;
        }
        this.mIsBssidListValid = z5;
        if (in.readByte() == 0) {
            z6 = false;
        } else {
            z6 = true;
        }
        this.mIsLocationCivicValid = z6;
        if (in.readByte() == 0) {
            z7 = false;
        } else {
            z7 = true;
        }
        this.mIsMapImageValid = z7;
        this.mLatitudeUncertainty = in.readDouble();
        this.mLatitude = in.readDouble();
        this.mLongitudeUncertainty = in.readDouble();
        this.mLongitude = in.readDouble();
        this.mAltitudeType = in.readInt();
        this.mAltitudeUncertainty = in.readDouble();
        this.mAltitude = in.readDouble();
        this.mDatum = in.readInt();
        if (in.readByte() == 0) {
            z8 = false;
        } else {
            z8 = true;
        }
        this.mLciRegisteredLocationAgreement = z8;
        if (in.readByte() == 0) {
            z9 = false;
        } else {
            z9 = true;
        }
        this.mLciRegisteredLocationDse = z9;
        if (in.readByte() == 0) {
            z10 = false;
        } else {
            z10 = true;
        }
        this.mLciDependentStation = z10;
        this.mLciVersion = in.readInt();
        this.mExpectedToMove = in.readInt();
        this.mFloorNumber = in.readDouble();
        this.mHeightAboveFloorMeters = in.readDouble();
        this.mHeightAboveFloorUncertaintyMeters = in.readDouble();
        if (in.readByte() == 0) {
            z11 = false;
        } else {
            z11 = true;
        }
        this.mUsageRetransmit = z11;
        if (in.readByte() == 0) {
            z12 = false;
        } else {
            z12 = true;
        }
        this.mUsageRetentionExpires = z12;
        this.mUsageExtraInfoOnAssociation = in.readByte() != 0;
        this.mBssidList = in.readArrayList(MacAddress.class.getClassLoader());
        this.mCivicLocationCountryCode = in.readString();
        this.mCivicLocationString = in.readString();
        this.mCivicLocation = (CivicLocation) in.readParcelable(getClass().getClassLoader());
        this.mMapImageType = in.readInt();
        String urlString = in.readString();
        if (TextUtils.isEmpty(urlString)) {
            this.mMapImageUri = null;
        } else {
            this.mMapImageUri = Uri.parse(urlString);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeByte(this.mIsValid ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsLciValid ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsZValid ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsUsageValid ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsBssidListValid ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsLocationCivicValid ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsMapImageValid ? (byte) 1 : (byte) 0);
        parcel.writeDouble(this.mLatitudeUncertainty);
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitudeUncertainty);
        parcel.writeDouble(this.mLongitude);
        parcel.writeInt(this.mAltitudeType);
        parcel.writeDouble(this.mAltitudeUncertainty);
        parcel.writeDouble(this.mAltitude);
        parcel.writeInt(this.mDatum);
        parcel.writeByte(this.mLciRegisteredLocationAgreement ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mLciRegisteredLocationDse ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mLciDependentStation ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.mLciVersion);
        parcel.writeInt(this.mExpectedToMove);
        parcel.writeDouble(this.mFloorNumber);
        parcel.writeDouble(this.mHeightAboveFloorMeters);
        parcel.writeDouble(this.mHeightAboveFloorUncertaintyMeters);
        parcel.writeByte(this.mUsageRetransmit ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mUsageRetentionExpires ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mUsageExtraInfoOnAssociation ? (byte) 1 : (byte) 0);
        parcel.writeList(this.mBssidList);
        parcel.writeString(this.mCivicLocationCountryCode);
        parcel.writeString(this.mCivicLocationString);
        parcel.writeParcelable(this.mCivicLocation, flags);
        parcel.writeInt(this.mMapImageType);
        Uri uri = this.mMapImageUri;
        if (uri != null) {
            parcel.writeString(uri.toString());
        } else {
            parcel.writeString("");
        }
    }

    private boolean parseInformationElementBuffer(int ieType, byte[] buffer, byte[] expectedLeadBytes) {
        int bufferLength = buffer.length;
        if (bufferLength >= 3 && bufferLength <= 256) {
            byte[] leadBufferBytes = Arrays.copyOfRange(buffer, 0, expectedLeadBytes.length);
            if (Arrays.equals(leadBufferBytes, expectedLeadBytes)) {
                int bufferPtr = 0 + expectedLeadBytes.length;
                while (bufferPtr + 1 < bufferLength) {
                    int bufferPtr2 = bufferPtr + 1;
                    byte subelement = buffer[bufferPtr];
                    int bufferPtr3 = bufferPtr2 + 1;
                    int subelementLength = buffer[bufferPtr2];
                    if (bufferPtr3 + subelementLength > bufferLength || subelementLength <= 0) {
                        return false;
                    }
                    byte[] subelementData = Arrays.copyOfRange(buffer, bufferPtr3, bufferPtr3 + subelementLength);
                    if (ieType == 8) {
                        if (subelement == 0) {
                            this.mIsLciValid = parseSubelementLci(subelementData);
                            if (!this.mIsLciValid || this.mLciVersion != 1) {
                                setLciSubelementDefaults();
                            }
                        } else if (subelement == 4) {
                            this.mIsZValid = parseSubelementZ(subelementData);
                            if (!this.mIsZValid) {
                                setZaxisSubelementDefaults();
                            }
                        } else if (subelement == 6) {
                            this.mIsUsageValid = parseSubelementUsage(subelementData);
                        } else if (subelement == 7) {
                            this.mIsBssidListValid = parseSubelementBssidList(subelementData);
                            if (!this.mIsBssidListValid) {
                                setBssidListSubelementDefaults();
                            }
                        }
                    } else if (ieType == 11) {
                        if (subelement == 0) {
                            this.mIsLocationCivicValid = parseSubelementLocationCivic(subelementData);
                            if (!this.mIsLocationCivicValid) {
                                setCivicLocationSubelementDefaults();
                            }
                        } else if (subelement == 5) {
                            this.mIsMapImageValid = parseSubelementMapImage(subelementData);
                            if (!this.mIsMapImageValid) {
                                setMapImageSubelementDefaults();
                            }
                        }
                    }
                    bufferPtr = bufferPtr3 + subelementLength;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean parseSubelementLci(byte[] buffer) {
        if (buffer.length > 16) {
            return false;
        }
        swapEndianByteByByte(buffer);
        long[] subelementLciFields = getFieldData(buffer, SUBELEMENT_LCI_BIT_FIELD_LENGTHS);
        if (subelementLciFields == null) {
            return false;
        }
        this.mLatitudeUncertainty = decodeLciLatLngUncertainty(subelementLciFields[0]);
        this.mLatitude = decodeLciLatLng(subelementLciFields, SUBELEMENT_LCI_BIT_FIELD_LENGTHS, 1, LAT_ABS_LIMIT);
        this.mLongitudeUncertainty = decodeLciLatLngUncertainty(subelementLciFields[2]);
        this.mLongitude = decodeLciLatLng(subelementLciFields, SUBELEMENT_LCI_BIT_FIELD_LENGTHS, 3, LNG_ABS_LIMIT);
        this.mAltitudeType = ((int) subelementLciFields[4]) & 255;
        this.mAltitudeUncertainty = decodeLciAltUncertainty(subelementLciFields[5]);
        this.mAltitude = Math.scalb((float) subelementLciFields[6], -8);
        this.mDatum = ((int) subelementLciFields[7]) & 255;
        this.mLciRegisteredLocationAgreement = subelementLciFields[8] == 1;
        this.mLciRegisteredLocationDse = subelementLciFields[9] == 1;
        this.mLciDependentStation = subelementLciFields[10] == 1;
        this.mLciVersion = (int) subelementLciFields[11];
        return true;
    }

    private double decodeLciLatLng(long[] fields, int[] bitFieldSizes, int offset, double limit) {
        double angle;
        if ((fields[offset] & ((long) Math.pow(2.0d, bitFieldSizes[offset] - 1))) != 0) {
            angle = Math.scalb(fields[offset] - Math.pow(2.0d, bitFieldSizes[offset]), -25);
        } else {
            angle = Math.scalb(fields[offset], -25);
        }
        if (angle > limit) {
            return limit;
        }
        if (angle < (-limit)) {
            double angle2 = -limit;
            return angle2;
        }
        return angle;
    }

    private double decodeLciLatLngUncertainty(long encodedValue) {
        return Math.pow(2.0d, 8 - encodedValue);
    }

    private double decodeLciAltUncertainty(long encodedValue) {
        return Math.pow(2.0d, 21 - encodedValue);
    }

    private boolean parseSubelementZ(byte[] buffer) {
        if (buffer.length != 6) {
            return false;
        }
        swapEndianByteByByte(buffer);
        long[] subelementZFields = getFieldData(buffer, SUBELEMENT_Z_BIT_FIELD_LENGTHS);
        if (subelementZFields == null) {
            return false;
        }
        this.mExpectedToMove = ((int) subelementZFields[0]) & 255;
        this.mFloorNumber = decodeZUnsignedToSignedValue(subelementZFields, SUBELEMENT_Z_BIT_FIELD_LENGTHS, 1, 4);
        this.mHeightAboveFloorMeters = decodeZUnsignedToSignedValue(subelementZFields, SUBELEMENT_Z_BIT_FIELD_LENGTHS, 2, 12);
        long zHeightUncertainty = subelementZFields[3];
        if (zHeightUncertainty <= 0 || zHeightUncertainty >= 25) {
            return false;
        }
        this.mHeightAboveFloorUncertaintyMeters = Math.pow(2.0d, (12 - zHeightUncertainty) - 1);
        return true;
    }

    private double decodeZUnsignedToSignedValue(long[] fieldValues, int[] fieldLengths, int index, int fraction) {
        int value = (int) fieldValues[index];
        int maxPositiveValue = ((int) Math.pow(2.0d, fieldLengths[index] - 1)) - 1;
        if (value > maxPositiveValue) {
            value = (int) (value - Math.pow(2.0d, fieldLengths[index]));
        }
        return Math.scalb(value, -fraction);
    }

    private boolean parseSubelementUsage(byte[] buffer) {
        if (buffer.length == 1 || buffer.length == 3) {
            this.mUsageRetransmit = (buffer[0] & 1) != 0;
            this.mUsageRetentionExpires = (buffer[0] & 2) != 0;
            this.mUsageExtraInfoOnAssociation = (buffer[0] & 4) != 0;
            return this.mUsageRetransmit && !this.mUsageRetentionExpires;
        }
        return false;
    }

    private boolean parseSubelementBssidList(byte[] buffer) {
        if (buffer.length >= 1 && (buffer.length - 1) % 6 == 0) {
            int i = buffer[0] & 255;
            int bssidListLength = (buffer.length - 1) / 6;
            int bssidOffset = 1;
            for (int i2 = 0; i2 < bssidListLength; i2++) {
                byte[] bssid = Arrays.copyOfRange(buffer, bssidOffset, bssidOffset + 6);
                MacAddress macAddress = MacAddress.fromBytes(bssid);
                this.mBssidList.add(macAddress);
                bssidOffset += 6;
            }
            return true;
        }
        return false;
    }

    private boolean parseSubelementLocationCivic(byte[] buffer) {
        if (buffer.length < 2 || buffer.length > 256) {
            return false;
        }
        this.mCivicLocationCountryCode = new String(Arrays.copyOfRange(buffer, 0, 2)).toUpperCase();
        CivicLocation civicLocation = new CivicLocation(Arrays.copyOfRange(buffer, 2, buffer.length), this.mCivicLocationCountryCode);
        if (civicLocation.isValid()) {
            this.mCivicLocation = civicLocation;
            this.mCivicLocationString = civicLocation.toString();
            return true;
        }
        return false;
    }

    private boolean parseSubelementMapImage(byte[] buffer) {
        if (buffer.length > 256) {
            return false;
        }
        int mapImageType = buffer[0];
        int supportedTypesMax = SUPPORTED_IMAGE_FILE_EXTENSIONS.length - 1;
        if (mapImageType < 0 || mapImageType > supportedTypesMax) {
            return false;
        }
        this.mMapImageType = mapImageType;
        byte[] urlBytes = Arrays.copyOfRange(buffer, 1, buffer.length);
        this.mMapImageUri = Uri.parse(new String(urlBytes, StandardCharsets.UTF_8));
        return true;
    }

    private String imageTypeToMime(int imageTypeCode, String imageUrl) {
        int supportedExtensionsMax = SUPPORTED_IMAGE_FILE_EXTENSIONS.length - 1;
        if ((imageTypeCode == 0 && imageUrl == null) || imageTypeCode > supportedExtensionsMax) {
            return null;
        }
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        if (imageTypeCode == 0) {
            return mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(imageUrl));
        }
        return mimeTypeMap.getMimeTypeFromExtension(SUPPORTED_IMAGE_FILE_EXTENSIONS[imageTypeCode]);
    }

    private long[] getFieldData(byte[] buffer, int[] bitFieldSizes) {
        int bufferLengthBits = buffer.length * 8;
        int sumBitFieldSizes = 0;
        for (int i : bitFieldSizes) {
            if (i > 64) {
                return null;
            }
            sumBitFieldSizes += i;
        }
        if (bufferLengthBits != sumBitFieldSizes) {
            return null;
        }
        long[] fieldData = new long[bitFieldSizes.length];
        int bufferBitPos = 0;
        for (int fieldIndex = 0; fieldIndex < bitFieldSizes.length; fieldIndex++) {
            int bitFieldSize = bitFieldSizes[fieldIndex];
            long field = 0;
            for (int n = 0; n < bitFieldSize; n++) {
                field |= getBitAtBitOffsetInByteArray(buffer, bufferBitPos + n) << n;
            }
            fieldData[fieldIndex] = field;
            bufferBitPos += bitFieldSize;
        }
        return fieldData;
    }

    private int getBitAtBitOffsetInByteArray(byte[] buffer, int bufferBitOffset) {
        int bufferIndex = bufferBitOffset / 8;
        int bitOffsetInByte = bufferBitOffset % 8;
        return (buffer[bufferIndex] & (128 >> bitOffsetInByte)) == 0 ? 0 : 1;
    }

    private void swapEndianByteByByte(byte[] buffer) {
        for (int n = 0; n < buffer.length; n++) {
            byte currentByte = buffer[n];
            byte reversedByte = 0;
            byte bitSelectorMask = 1;
            for (int i = 0; i < 8; i++) {
                reversedByte = (byte) (reversedByte << 1);
                if ((currentByte & bitSelectorMask) != 0) {
                    reversedByte = (byte) (reversedByte | 1);
                }
                bitSelectorMask = (byte) (bitSelectorMask << 1);
            }
            buffer[n] = reversedByte;
        }
    }

    private void setLciSubelementDefaults() {
        this.mIsLciValid = false;
        this.mLatitudeUncertainty = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mLatitude = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mLongitudeUncertainty = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mLongitude = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mAltitudeType = 0;
        this.mAltitudeUncertainty = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mAltitude = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mDatum = 0;
        this.mLciRegisteredLocationAgreement = false;
        this.mLciRegisteredLocationDse = false;
        this.mLciDependentStation = false;
        this.mLciVersion = 0;
    }

    private void setZaxisSubelementDefaults() {
        this.mIsZValid = false;
        this.mExpectedToMove = 0;
        this.mFloorNumber = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mHeightAboveFloorMeters = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mHeightAboveFloorUncertaintyMeters = FeatureOption.FO_BOOT_POLICY_CPU;
    }

    private void setUsageSubelementDefaults() {
        this.mUsageRetransmit = true;
        this.mUsageRetentionExpires = false;
        this.mUsageExtraInfoOnAssociation = false;
    }

    private void setBssidListSubelementDefaults() {
        this.mIsBssidListValid = false;
        this.mBssidList = new ArrayList<>();
    }

    public void setCivicLocationSubelementDefaults() {
        this.mIsLocationCivicValid = false;
        this.mCivicLocationCountryCode = "";
        this.mCivicLocationString = "";
        this.mCivicLocation = null;
    }

    private void setMapImageSubelementDefaults() {
        this.mIsMapImageValid = false;
        this.mMapImageType = 0;
        this.mMapImageUri = null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ResponderLocation other = (ResponderLocation) obj;
        if (this.mIsValid == other.mIsValid && this.mIsLciValid == other.mIsLciValid && this.mIsZValid == other.mIsZValid && this.mIsUsageValid == other.mIsUsageValid && this.mIsBssidListValid == other.mIsBssidListValid && this.mIsLocationCivicValid == other.mIsLocationCivicValid && this.mIsMapImageValid == other.mIsMapImageValid && this.mLatitudeUncertainty == other.mLatitudeUncertainty && this.mLatitude == other.mLatitude && this.mLongitudeUncertainty == other.mLongitudeUncertainty && this.mLongitude == other.mLongitude && this.mAltitudeType == other.mAltitudeType && this.mAltitudeUncertainty == other.mAltitudeUncertainty && this.mAltitude == other.mAltitude && this.mDatum == other.mDatum && this.mLciRegisteredLocationAgreement == other.mLciRegisteredLocationAgreement && this.mLciRegisteredLocationDse == other.mLciRegisteredLocationDse && this.mLciDependentStation == other.mLciDependentStation && this.mLciVersion == other.mLciVersion && this.mExpectedToMove == other.mExpectedToMove && this.mFloorNumber == other.mFloorNumber && this.mHeightAboveFloorMeters == other.mHeightAboveFloorMeters && this.mHeightAboveFloorUncertaintyMeters == other.mHeightAboveFloorUncertaintyMeters && this.mUsageRetransmit == other.mUsageRetransmit && this.mUsageRetentionExpires == other.mUsageRetentionExpires && this.mUsageExtraInfoOnAssociation == other.mUsageExtraInfoOnAssociation && this.mBssidList.equals(other.mBssidList) && this.mCivicLocationCountryCode.equals(other.mCivicLocationCountryCode) && this.mCivicLocationString.equals(other.mCivicLocationString) && Objects.equals(this.mCivicLocation, other.mCivicLocation) && this.mMapImageType == other.mMapImageType && Objects.equals(this.mMapImageUri, other.mMapImageUri)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.mIsValid), Boolean.valueOf(this.mIsLciValid), Boolean.valueOf(this.mIsZValid), Boolean.valueOf(this.mIsUsageValid), Boolean.valueOf(this.mIsBssidListValid), Boolean.valueOf(this.mIsLocationCivicValid), Boolean.valueOf(this.mIsMapImageValid), Double.valueOf(this.mLatitudeUncertainty), Double.valueOf(this.mLatitude), Double.valueOf(this.mLongitudeUncertainty), Double.valueOf(this.mLongitude), Integer.valueOf(this.mAltitudeType), Double.valueOf(this.mAltitudeUncertainty), Double.valueOf(this.mAltitude), Integer.valueOf(this.mDatum), Boolean.valueOf(this.mLciRegisteredLocationAgreement), Boolean.valueOf(this.mLciRegisteredLocationDse), Boolean.valueOf(this.mLciDependentStation), Integer.valueOf(this.mLciVersion), Integer.valueOf(this.mExpectedToMove), Double.valueOf(this.mFloorNumber), Double.valueOf(this.mHeightAboveFloorMeters), Double.valueOf(this.mHeightAboveFloorUncertaintyMeters), Boolean.valueOf(this.mUsageRetransmit), Boolean.valueOf(this.mUsageRetentionExpires), Boolean.valueOf(this.mUsageExtraInfoOnAssociation), this.mBssidList, this.mCivicLocationCountryCode, this.mCivicLocationString, this.mCivicLocation, Integer.valueOf(this.mMapImageType), this.mMapImageUri);
    }

    public boolean isValid() {
        return this.mIsValid;
    }

    public boolean isLciSubelementValid() {
        return this.mIsLciValid;
    }

    public double getLatitudeUncertainty() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getLatitudeUncertainty(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mLatitudeUncertainty;
    }

    public double getLatitude() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getLatitude(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mLatitude;
    }

    public double getLongitudeUncertainty() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getLongitudeUncertainty(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mLongitudeUncertainty;
    }

    public double getLongitude() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getLatitudeUncertainty(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mLongitude;
    }

    public int getAltitudeType() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getLatitudeUncertainty(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mAltitudeType;
    }

    public double getAltitudeUncertainty() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getLatitudeUncertainty(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mAltitudeUncertainty;
    }

    public double getAltitude() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getAltitude(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mAltitude;
    }

    public int getDatum() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getDatum(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mDatum;
    }

    public boolean getRegisteredLocationAgreementIndication() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getRegisteredLocationAgreementIndication(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mLciRegisteredLocationAgreement;
    }

    public boolean getRegisteredLocationDseIndication() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getRegisteredLocationDseIndication(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mLciRegisteredLocationDse;
    }

    public boolean getDependentStationIndication() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getDependentStationIndication(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mLciDependentStation;
    }

    public int getLciVersion() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("getLciVersion(): invoked on an invalid result: mIsLciValid = false.");
        }
        return this.mLciVersion;
    }

    public Location toLocation() {
        if (!this.mIsLciValid) {
            throw new IllegalStateException("toLocation(): invoked on an invalid result: mIsLciValid = false.");
        }
        Location location = new Location(LOCATION_PROVIDER);
        location.setLatitude(this.mLatitude);
        location.setLongitude(this.mLongitude);
        location.setAccuracy(((float) (this.mLatitudeUncertainty + this.mLongitudeUncertainty)) / 2.0f);
        location.setAltitude(this.mAltitude);
        location.setVerticalAccuracyMeters((float) this.mAltitudeUncertainty);
        location.setTime(System.currentTimeMillis());
        return location;
    }

    public boolean isZaxisSubelementValid() {
        return this.mIsZValid;
    }

    public int getExpectedToMove() {
        if (!this.mIsZValid) {
            throw new IllegalStateException("getExpectedToMove(): invoked on an invalid result: mIsZValid = false.");
        }
        return this.mExpectedToMove;
    }

    public double getFloorNumber() {
        if (!this.mIsZValid) {
            throw new IllegalStateException("getFloorNumber(): invoked on an invalid result: mIsZValid = false)");
        }
        return this.mFloorNumber;
    }

    public double getHeightAboveFloorMeters() {
        if (!this.mIsZValid) {
            throw new IllegalStateException("getHeightAboveFloorMeters(): invoked on an invalid result: mIsZValid = false)");
        }
        return this.mHeightAboveFloorMeters;
    }

    public double getHeightAboveFloorUncertaintyMeters() {
        if (!this.mIsZValid) {
            throw new IllegalStateException("getHeightAboveFloorUncertaintyMeters():invoked on an invalid result: mIsZValid = false)");
        }
        return this.mHeightAboveFloorUncertaintyMeters;
    }

    public boolean getRetransmitPolicyIndication() {
        return this.mUsageRetransmit;
    }

    public boolean getRetentionExpiresIndication() {
        return this.mUsageRetentionExpires;
    }

    @SystemApi
    public boolean getExtraInfoOnAssociationIndication() {
        return this.mUsageExtraInfoOnAssociation;
    }

    public List<MacAddress> getColocatedBssids() {
        return Collections.unmodifiableList(this.mBssidList);
    }

    public Address toCivicLocationAddress() {
        CivicLocation civicLocation = this.mCivicLocation;
        if (civicLocation != null && civicLocation.isValid()) {
            return this.mCivicLocation.toAddress();
        }
        return null;
    }

    public SparseArray toCivicLocationSparseArray() {
        CivicLocation civicLocation = this.mCivicLocation;
        if (civicLocation != null && civicLocation.isValid()) {
            return this.mCivicLocation.toSparseArray();
        }
        return null;
    }

    public String getCivicLocationCountryCode() {
        return this.mCivicLocationCountryCode;
    }

    public String getCivicLocationElementValue(int key) {
        return this.mCivicLocation.getCivicElementValue(key);
    }

    public String getMapImageMimeType() {
        Uri uri = this.mMapImageUri;
        if (uri == null) {
            return null;
        }
        return imageTypeToMime(this.mMapImageType, uri.toString());
    }

    public Uri getMapImageUri() {
        return this.mMapImageUri;
    }
}
