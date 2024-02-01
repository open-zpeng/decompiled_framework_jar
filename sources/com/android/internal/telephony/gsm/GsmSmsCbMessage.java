package com.android.internal.telephony.gsm;

import android.content.Context;
import android.content.res.Resources;
import android.telephony.SmsCbLocation;
import android.telephony.SmsCbMessage;
import android.telephony.SmsManager;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.R;
import com.android.internal.telephony.CbGeoUtils;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.gsm.GsmSmsCbMessage;
import com.android.internal.telephony.gsm.SmsCbHeader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes3.dex */
public class GsmSmsCbMessage {
    private static final char CARRIAGE_RETURN = '\r';
    private static final int PDU_BODY_PAGE_LENGTH = 82;
    private static final String TAG = GsmSmsCbMessage.class.getSimpleName();

    private GsmSmsCbMessage() {
    }

    private static String getEtwsPrimaryMessage(Context context, int category) {
        Resources r = context.getResources();
        if (category != 0) {
            if (category != 1) {
                if (category != 2) {
                    if (category != 3) {
                        if (category == 4) {
                            return r.getString(R.string.etws_primary_default_message_others);
                        }
                        return "";
                    }
                    return r.getString(R.string.etws_primary_default_message_test);
                }
                return r.getString(R.string.etws_primary_default_message_earthquake_and_tsunami);
            }
            return r.getString(R.string.etws_primary_default_message_tsunami);
        }
        return r.getString(R.string.etws_primary_default_message_earthquake);
    }

    public static SmsCbMessage createSmsCbMessage(Context context, SmsCbHeader header, SmsCbLocation location, byte[][] pdus) throws IllegalArgumentException {
        List<CbGeoUtils.Geometry> geometries;
        int maximumWaitingTimeSec;
        long receivedTimeMillis = System.currentTimeMillis();
        if (header.isEtwsPrimaryNotification()) {
            return new SmsCbMessage(1, header.getGeographicalScope(), header.getSerialNumber(), location, header.getServiceCategory(), null, getEtwsPrimaryMessage(context, header.getEtwsInfo().getWarningType()), 3, header.getEtwsInfo(), header.getCmasInfo(), 0, null, receivedTimeMillis);
        }
        if (header.isUmtsFormat()) {
            byte[] pdu = pdus[0];
            Pair<String, String> cbData = parseUmtsBody(header, pdu);
            String language = cbData.first;
            String body = cbData.second;
            int priority = header.isEmergencyMessage() ? 3 : 0;
            int nrPages = pdu[6];
            int wacDataOffset = (nrPages * 83) + 7;
            int maximumWaitingTimeSec2 = 255;
            if (pdu.length <= wacDataOffset) {
                geometries = null;
                maximumWaitingTimeSec = 255;
            } else {
                try {
                    Pair<Integer, List<CbGeoUtils.Geometry>> wac = parseWarningAreaCoordinates(pdu, wacDataOffset);
                    maximumWaitingTimeSec2 = wac.first.intValue();
                    List<CbGeoUtils.Geometry> geometries2 = wac.second;
                    geometries = geometries2;
                    maximumWaitingTimeSec = maximumWaitingTimeSec2;
                } catch (Exception ex) {
                    Slog.e(TAG, "Can't parse warning area coordinates, ex = " + ex.toString());
                    geometries = null;
                    maximumWaitingTimeSec = maximumWaitingTimeSec2;
                }
            }
            return new SmsCbMessage(1, header.getGeographicalScope(), header.getSerialNumber(), location, header.getServiceCategory(), language, body, priority, header.getEtwsInfo(), header.getCmasInfo(), maximumWaitingTimeSec, geometries, receivedTimeMillis);
        }
        StringBuilder sb = new StringBuilder();
        String language2 = null;
        for (byte[] pdu2 : pdus) {
            Pair<String, String> p = parseGsmBody(header, pdu2);
            String language3 = p.first;
            language2 = language3;
            sb.append(p.second);
        }
        int priority2 = header.isEmergencyMessage() ? 3 : 0;
        return new SmsCbMessage(1, header.getGeographicalScope(), header.getSerialNumber(), location, header.getServiceCategory(), language2, sb.toString(), priority2, header.getEtwsInfo(), header.getCmasInfo(), 0, null, receivedTimeMillis);
    }

    public static GeoFencingTriggerMessage createGeoFencingTriggerMessage(byte[] pdu) {
        try {
            BitStreamReader bitReader = new BitStreamReader(pdu, 7);
            int type = bitReader.read(4);
            int length = bitReader.read(7);
            bitReader.skip();
            int messageIdentifierCount = ((length - 2) * 8) / 32;
            List<GeoFencingTriggerMessage.CellBroadcastIdentity> cbIdentifiers = new ArrayList<>();
            for (int i = 0; i < messageIdentifierCount; i++) {
                int messageIdentifier = bitReader.read(16);
                int serialNumber = bitReader.read(16);
                cbIdentifiers.add(new GeoFencingTriggerMessage.CellBroadcastIdentity(messageIdentifier, serialNumber));
            }
            return new GeoFencingTriggerMessage(type, cbIdentifiers);
        } catch (Exception ex) {
            String str = TAG;
            Slog.e(str, "create geo-fencing trigger failed, ex = " + ex.toString());
            return null;
        }
    }

    private static Pair<Integer, List<CbGeoUtils.Geometry>> parseWarningAreaCoordinates(byte[] pdu, int wacOffset) {
        int wacDataLength = ((pdu[wacOffset + 1] & 255) << 8) | (pdu[wacOffset] & 255);
        int offset = wacOffset + 2;
        if (offset + wacDataLength > pdu.length) {
            throw new IllegalArgumentException("Invalid wac data, expected the length of pdu atleast " + offset + wacDataLength + ", actual is " + pdu.length);
        }
        BitStreamReader bitReader = new BitStreamReader(pdu, offset);
        int maximumWaitTimeSec = 255;
        List<CbGeoUtils.Geometry> geo = new ArrayList<>();
        int remainedBytes = wacDataLength;
        while (remainedBytes > 0) {
            int type = bitReader.read(4);
            int length = bitReader.read(10);
            remainedBytes -= length;
            bitReader.skip();
            if (type == 1) {
                maximumWaitTimeSec = bitReader.read(8);
            } else if (type == 2) {
                List<CbGeoUtils.LatLng> latLngs = new ArrayList<>();
                int n = ((length - 2) * 8) / 44;
                for (int i = 0; i < n; i++) {
                    latLngs.add(getLatLng(bitReader));
                }
                bitReader.skip();
                geo.add(new CbGeoUtils.Polygon(latLngs));
            } else if (type == 3) {
                CbGeoUtils.LatLng center = getLatLng(bitReader);
                double radius = ((bitReader.read(20) * 1.0d) / 64.0d) * 1000.0d;
                geo.add(new CbGeoUtils.Circle(center, radius));
            } else {
                throw new IllegalArgumentException("Unsupported geoType = " + type);
            }
        }
        return new Pair<>(Integer.valueOf(maximumWaitTimeSec), geo);
    }

    private static CbGeoUtils.LatLng getLatLng(BitStreamReader bitReader) {
        int wacLat = bitReader.read(22);
        int wacLng = bitReader.read(22);
        return new CbGeoUtils.LatLng(((wacLat * 180.0d) / 4194304.0d) - 90.0d, ((wacLng * 360.0d) / 4194304.0d) - 180.0d);
    }

    private static Pair<String, String> parseUmtsBody(SmsCbHeader header, byte[] pdu) {
        int nrPages = pdu[6];
        String language = header.getDataCodingSchemeStructedData().language;
        if (pdu.length < (nrPages * 83) + 7) {
            throw new IllegalArgumentException("Pdu length " + pdu.length + " does not match " + nrPages + " pages");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nrPages; i++) {
            int offset = (i * 83) + 7;
            int length = pdu[offset + 82];
            if (length > 82) {
                throw new IllegalArgumentException("Page length " + length + " exceeds maximum value 82");
            }
            Pair<String, String> p = unpackBody(pdu, offset, length, header.getDataCodingSchemeStructedData());
            String language2 = p.first;
            language = language2;
            sb.append(p.second);
        }
        return new Pair<>(language, sb.toString());
    }

    private static Pair<String, String> parseGsmBody(SmsCbHeader header, byte[] pdu) {
        int length = pdu.length - 6;
        return unpackBody(pdu, 6, length, header.getDataCodingSchemeStructedData());
    }

    private static Pair<String, String> unpackBody(byte[] pdu, int offset, int length, SmsCbHeader.DataCodingScheme dcs) {
        String body = null;
        String language = dcs.language;
        int i = dcs.encoding;
        if (i == 1) {
            body = GsmAlphabet.gsm7BitPackedToString(pdu, offset, (length * 8) / 7);
            if (dcs.hasLanguageIndicator && body != null && body.length() > 2) {
                language = body.substring(0, 2);
                body = body.substring(3);
            }
        } else if (i == 3) {
            if (dcs.hasLanguageIndicator && pdu.length >= offset + 2) {
                language = GsmAlphabet.gsm7BitPackedToString(pdu, offset, 2);
                offset += 2;
                length -= 2;
            }
            try {
                body = new String(pdu, offset, 65534 & length, "utf-16");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Error decoding UTF-16 message", e);
            }
        }
        if (body != null) {
            int i2 = body.length() - 1;
            while (true) {
                if (i2 < 0) {
                    break;
                } else if (body.charAt(i2) == '\r') {
                    i2--;
                } else {
                    body = body.substring(0, i2 + 1);
                    break;
                }
            }
        } else {
            body = "";
        }
        return new Pair<>(language, body);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class BitStreamReader {
        private int mCurrentOffset;
        private final byte[] mData;
        private int mRemainedBit = 8;

        BitStreamReader(byte[] data, int offset) {
            this.mData = data;
            this.mCurrentOffset = offset;
        }

        public int read(int count) throws IndexOutOfBoundsException {
            int val = 0;
            while (count > 0) {
                int i = this.mRemainedBit;
                if (count >= i) {
                    byte[] bArr = this.mData;
                    int i2 = this.mCurrentOffset;
                    val = (val << i) | (bArr[i2] & ((1 << i) - 1));
                    count -= i;
                    this.mRemainedBit = 8;
                    this.mCurrentOffset = i2 + 1;
                } else {
                    val = (val << count) | ((this.mData[this.mCurrentOffset] & ((1 << i) - 1)) >> (i - count));
                    this.mRemainedBit = i - count;
                    count = 0;
                }
            }
            return val;
        }

        public void skip() {
            if (this.mRemainedBit < 8) {
                this.mRemainedBit = 8;
                this.mCurrentOffset++;
            }
        }
    }

    /* loaded from: classes3.dex */
    static final class GeoFencingTriggerMessage {
        public static final int TYPE_ACTIVE_ALERT_SHARE_WAC = 2;
        public final List<CellBroadcastIdentity> cbIdentifiers;
        public final int type;

        GeoFencingTriggerMessage(int type, List<CellBroadcastIdentity> cbIdentifiers) {
            this.type = type;
            this.cbIdentifiers = cbIdentifiers;
        }

        boolean shouldShareBroadcastArea() {
            return this.type == 2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public static final class CellBroadcastIdentity {
            public final int messageIdentifier;
            public final int serialNumber;

            CellBroadcastIdentity(int messageIdentifier, int serialNumber) {
                this.messageIdentifier = messageIdentifier;
                this.serialNumber = serialNumber;
            }
        }

        public String toString() {
            String identifiers = (String) this.cbIdentifiers.stream().map(new Function() { // from class: com.android.internal.telephony.gsm.-$$Lambda$GsmSmsCbMessage$GeoFencingTriggerMessage$pWnGG2lYYLGGDO9XCPIlkTZ5Yd0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String format;
                    format = String.format("(msgId = %d, serial = %d)", Integer.valueOf(r1.messageIdentifier), Integer.valueOf(((GsmSmsCbMessage.GeoFencingTriggerMessage.CellBroadcastIdentity) obj).serialNumber));
                    return format;
                }
            }).collect(Collectors.joining(SmsManager.REGEX_PREFIX_DELIMITER));
            return "triggerType=" + this.type + " identifiers=" + identifiers;
        }
    }
}
