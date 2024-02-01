package android.util.proto;

import android.net.wifi.WifiEnterpriseConfig;
import android.provider.Telephony;
import android.util.Log;
import com.android.internal.logging.EventLogTags;
import com.android.internal.logging.nano.MetricsProto;
import com.xiaopeng.util.FeatureOption;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
/* loaded from: classes2.dex */
public final class ProtoOutputStream {
    public static final long FIELD_COUNT_MASK = 16492674416640L;
    public static final long FIELD_COUNT_PACKED = 5497558138880L;
    public static final long FIELD_COUNT_REPEATED = 2199023255552L;
    public static final int FIELD_COUNT_SHIFT = 40;
    public static final long FIELD_COUNT_SINGLE = 1099511627776L;
    public static final long FIELD_COUNT_UNKNOWN = 0;
    public static final int FIELD_ID_MASK = -8;
    public static final int FIELD_ID_SHIFT = 3;
    public static final long FIELD_TYPE_BOOL = 34359738368L;
    public static final long FIELD_TYPE_BYTES = 51539607552L;
    public static final long FIELD_TYPE_DOUBLE = 4294967296L;
    public static final long FIELD_TYPE_ENUM = 60129542144L;
    public static final long FIELD_TYPE_FIXED32 = 30064771072L;
    public static final long FIELD_TYPE_FIXED64 = 25769803776L;
    public static final long FIELD_TYPE_FLOAT = 8589934592L;
    public static final long FIELD_TYPE_INT32 = 21474836480L;
    public static final long FIELD_TYPE_INT64 = 12884901888L;
    public static final long FIELD_TYPE_MASK = 1095216660480L;
    public static final long FIELD_TYPE_MESSAGE = 47244640256L;
    private static final String[] FIELD_TYPE_NAMES = {"Double", "Float", "Int64", "UInt64", "Int32", "Fixed64", "Fixed32", "Bool", "String", "Group", "Message", "Bytes", "UInt32", "Enum", "SFixed32", "SFixed64", "SInt32", "SInt64"};
    public static final long FIELD_TYPE_SFIXED32 = 64424509440L;
    public static final long FIELD_TYPE_SFIXED64 = 68719476736L;
    public static final int FIELD_TYPE_SHIFT = 32;
    public static final long FIELD_TYPE_SINT32 = 73014444032L;
    public static final long FIELD_TYPE_SINT64 = 77309411328L;
    public static final long FIELD_TYPE_STRING = 38654705664L;
    public static final long FIELD_TYPE_UINT32 = 55834574848L;
    public static final long FIELD_TYPE_UINT64 = 17179869184L;
    public static final long FIELD_TYPE_UNKNOWN = 0;
    public static final String TAG = "ProtoOutputStream";
    public static final int WIRE_TYPE_END_GROUP = 4;
    public static final int WIRE_TYPE_FIXED32 = 5;
    public static final int WIRE_TYPE_FIXED64 = 1;
    public static final int WIRE_TYPE_LENGTH_DELIMITED = 2;
    public static final int WIRE_TYPE_MASK = 7;
    public static final int WIRE_TYPE_START_GROUP = 3;
    public static final int WIRE_TYPE_VARINT = 0;
    private EncodedBuffer mBuffer;
    private boolean mCompacted;
    private int mCopyBegin;
    private int mDepth;
    private long mExpectedObjectToken;
    private int mNextObjectId;
    private OutputStream mStream;

    public ProtoOutputStream() {
        this(0);
    }

    public ProtoOutputStream(int chunkSize) {
        this.mNextObjectId = -1;
        this.mBuffer = new EncodedBuffer(chunkSize);
    }

    public ProtoOutputStream(OutputStream stream) {
        this();
        this.mStream = stream;
    }

    public ProtoOutputStream(FileDescriptor fd) {
        this(new FileOutputStream(fd));
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0090  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void write(long r7, double r9) {
        /*
            Method dump skipped, instructions count: 332
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.proto.ProtoOutputStream.write(long, double):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003e  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x008e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void write(long r6, float r8) {
        /*
            Method dump skipped, instructions count: 330
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.proto.ProtoOutputStream.write(long, float):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0058  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0085  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void write(long r5, int r7) {
        /*
            r4 = this;
            r4.assertNotCompacted()
            int r0 = (int) r5
            r1 = 17587891077120(0xfff00000000, double:8.689572764003E-311)
            long r1 = r1 & r5
            r3 = 32
            long r1 = r1 >> r3
            int r1 = (int) r1
            r2 = 0
            r3 = 1
            switch(r1) {
                case 257: goto Lc8;
                case 258: goto Lc3;
                case 259: goto Lbe;
                case 260: goto Lb9;
                case 261: goto Lb5;
                case 262: goto Lb0;
                case 263: goto Lac;
                case 264: goto La4;
                default: goto L13;
            }
        L13:
            switch(r1) {
                case 269: goto La0;
                case 270: goto L9c;
                case 271: goto L98;
                case 272: goto L93;
                case 273: goto L8f;
                case 274: goto L8a;
                default: goto L16;
            }
        L16:
            switch(r1) {
                case 513: goto L85;
                case 514: goto L80;
                case 515: goto L7b;
                case 516: goto L76;
                case 517: goto L71;
                case 518: goto L6b;
                case 519: goto L66;
                case 520: goto L5d;
                default: goto L19;
            }
        L19:
            switch(r1) {
                case 525: goto L58;
                case 526: goto L53;
                case 527: goto L4e;
                case 528: goto L48;
                case 529: goto L43;
                case 530: goto L3d;
                default: goto L1c;
            }
        L1c:
            switch(r1) {
                case 1281: goto L85;
                case 1282: goto L80;
                case 1283: goto L7b;
                case 1284: goto L76;
                case 1285: goto L71;
                case 1286: goto L6b;
                case 1287: goto L66;
                case 1288: goto L5d;
                default: goto L1f;
            }
        L1f:
            switch(r1) {
                case 1293: goto L58;
                case 1294: goto L53;
                case 1295: goto L4e;
                case 1296: goto L48;
                case 1297: goto L43;
                case 1298: goto L3d;
                default: goto L22;
            }
        L22:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Attempt to call write(long, int) with "
            r2.append(r3)
            java.lang.String r3 = r4.getFieldIdString(r5)
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L3d:
            long r1 = (long) r7
            r4.writeRepeatedSInt64Impl(r0, r1)
            goto Lcd
        L43:
            r4.writeRepeatedSInt32Impl(r0, r7)
            goto Lcd
        L48:
            long r1 = (long) r7
            r4.writeRepeatedSFixed64Impl(r0, r1)
            goto Lcd
        L4e:
            r4.writeRepeatedSFixed32Impl(r0, r7)
            goto Lcd
        L53:
            r4.writeRepeatedEnumImpl(r0, r7)
            goto Lcd
        L58:
            r4.writeRepeatedUInt32Impl(r0, r7)
            goto Lcd
        L5d:
            if (r7 == 0) goto L61
            r2 = r3
        L61:
            r4.writeRepeatedBoolImpl(r0, r2)
            goto Lcd
        L66:
            r4.writeRepeatedFixed32Impl(r0, r7)
            goto Lcd
        L6b:
            long r1 = (long) r7
            r4.writeRepeatedFixed64Impl(r0, r1)
            goto Lcd
        L71:
            r4.writeRepeatedInt32Impl(r0, r7)
            goto Lcd
        L76:
            long r1 = (long) r7
            r4.writeRepeatedUInt64Impl(r0, r1)
            goto Lcd
        L7b:
            long r1 = (long) r7
            r4.writeRepeatedInt64Impl(r0, r1)
            goto Lcd
        L80:
            float r1 = (float) r7
            r4.writeRepeatedFloatImpl(r0, r1)
            goto Lcd
        L85:
            double r1 = (double) r7
            r4.writeRepeatedDoubleImpl(r0, r1)
            goto Lcd
        L8a:
            long r1 = (long) r7
            r4.writeSInt64Impl(r0, r1)
            goto Lcd
        L8f:
            r4.writeSInt32Impl(r0, r7)
            goto Lcd
        L93:
            long r1 = (long) r7
            r4.writeSFixed64Impl(r0, r1)
            goto Lcd
        L98:
            r4.writeSFixed32Impl(r0, r7)
            goto Lcd
        L9c:
            r4.writeEnumImpl(r0, r7)
            goto Lcd
        La0:
            r4.writeUInt32Impl(r0, r7)
            goto Lcd
        La4:
            if (r7 == 0) goto La8
            r2 = r3
        La8:
            r4.writeBoolImpl(r0, r2)
            goto Lcd
        Lac:
            r4.writeFixed32Impl(r0, r7)
            goto Lcd
        Lb0:
            long r1 = (long) r7
            r4.writeFixed64Impl(r0, r1)
            goto Lcd
        Lb5:
            r4.writeInt32Impl(r0, r7)
            goto Lcd
        Lb9:
            long r1 = (long) r7
            r4.writeUInt64Impl(r0, r1)
            goto Lcd
        Lbe:
            long r1 = (long) r7
            r4.writeInt64Impl(r0, r1)
            goto Lcd
        Lc3:
            float r1 = (float) r7
            r4.writeFloatImpl(r0, r1)
            goto Lcd
        Lc8:
            double r1 = (double) r7
            r4.writeDoubleImpl(r0, r1)
        Lcd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.proto.ProtoOutputStream.write(long, int):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x008a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void write(long r7, long r9) {
        /*
            Method dump skipped, instructions count: 322
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.proto.ProtoOutputStream.write(long, long):void");
    }

    public void write(long fieldId, boolean val) {
        assertNotCompacted();
        int id = (int) fieldId;
        int i = (int) ((17587891077120L & fieldId) >> 32);
        if (i == 264) {
            writeBoolImpl(id, val);
        } else if (i == 520 || i == 1288) {
            writeRepeatedBoolImpl(id, val);
        } else {
            throw new IllegalArgumentException("Attempt to call write(long, boolean) with " + getFieldIdString(fieldId));
        }
    }

    public void write(long fieldId, String val) {
        assertNotCompacted();
        int id = (int) fieldId;
        int i = (int) ((17587891077120L & fieldId) >> 32);
        if (i == 265) {
            writeStringImpl(id, val);
        } else if (i == 521 || i == 1289) {
            writeRepeatedStringImpl(id, val);
        } else {
            throw new IllegalArgumentException("Attempt to call write(long, String) with " + getFieldIdString(fieldId));
        }
    }

    public void write(long fieldId, byte[] val) {
        assertNotCompacted();
        int id = (int) fieldId;
        switch ((int) ((17587891077120L & fieldId) >> 32)) {
            case 267:
                writeObjectImpl(id, val);
                return;
            case 268:
                writeBytesImpl(id, val);
                return;
            case 523:
            case MetricsProto.MetricsEvent.USB_DEVICE_DETAILS /* 1291 */:
                writeRepeatedObjectImpl(id, val);
                return;
            case 524:
            case MetricsProto.MetricsEvent.ACCESSIBILITY_VIBRATION /* 1292 */:
                writeRepeatedBytesImpl(id, val);
                return;
            default:
                throw new IllegalArgumentException("Attempt to call write(long, byte[]) with " + getFieldIdString(fieldId));
        }
    }

    public long start(long fieldId) {
        assertNotCompacted();
        int id = (int) fieldId;
        if ((FIELD_TYPE_MASK & fieldId) == FIELD_TYPE_MESSAGE) {
            long count = FIELD_COUNT_MASK & fieldId;
            if (count == 1099511627776L) {
                return startObjectImpl(id, false);
            }
            if (count == FIELD_COUNT_REPEATED || count == FIELD_COUNT_PACKED) {
                return startObjectImpl(id, true);
            }
        }
        throw new IllegalArgumentException("Attempt to call start(long) with " + getFieldIdString(fieldId));
    }

    public void end(long token) {
        endObjectImpl(token, getRepeatedFromToken(token));
    }

    @Deprecated
    public void writeDouble(long fieldId, double val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1103806595072L);
        writeDoubleImpl(id, val);
    }

    private synchronized void writeDoubleImpl(int id, double val) {
        if (val != FeatureOption.FO_BOOT_POLICY_CPU) {
            writeTag(id, 1);
            this.mBuffer.writeRawFixed64(Double.doubleToLongBits(val));
        }
    }

    @Deprecated
    public void writeRepeatedDouble(long fieldId, double val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2203318222848L);
        writeRepeatedDoubleImpl(id, val);
    }

    private synchronized void writeRepeatedDoubleImpl(int id, double val) {
        writeTag(id, 1);
        this.mBuffer.writeRawFixed64(Double.doubleToLongBits(val));
    }

    @Deprecated
    public void writePackedDouble(long fieldId, double[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5501853106176L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 8);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed64(Double.doubleToLongBits(val[i]));
            }
        }
    }

    @Deprecated
    public void writeFloat(long fieldId, float val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1108101562368L);
        writeFloatImpl(id, val);
    }

    private synchronized void writeFloatImpl(int id, float val) {
        if (val != 0.0f) {
            writeTag(id, 5);
            this.mBuffer.writeRawFixed32(Float.floatToIntBits(val));
        }
    }

    @Deprecated
    public void writeRepeatedFloat(long fieldId, float val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2207613190144L);
        writeRepeatedFloatImpl(id, val);
    }

    private synchronized void writeRepeatedFloatImpl(int id, float val) {
        writeTag(id, 5);
        this.mBuffer.writeRawFixed32(Float.floatToIntBits(val));
    }

    @Deprecated
    public void writePackedFloat(long fieldId, float[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5506148073472L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 4);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed32(Float.floatToIntBits(val[i]));
            }
        }
    }

    private synchronized void writeUnsignedVarintFromSignedInt(int val) {
        if (val >= 0) {
            this.mBuffer.writeRawVarint32(val);
        } else {
            this.mBuffer.writeRawVarint64(val);
        }
    }

    @Deprecated
    public void writeInt32(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1120986464256L);
        writeInt32Impl(id, val);
    }

    private synchronized void writeInt32Impl(int id, int val) {
        if (val != 0) {
            writeTag(id, 0);
            writeUnsignedVarintFromSignedInt(val);
        }
    }

    @Deprecated
    public void writeRepeatedInt32(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2220498092032L);
        writeRepeatedInt32Impl(id, val);
    }

    private synchronized void writeRepeatedInt32Impl(int id, int val) {
        writeTag(id, 0);
        writeUnsignedVarintFromSignedInt(val);
    }

    @Deprecated
    public void writePackedInt32(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5519032975360L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int size = 0;
            for (int size2 = 0; size2 < N; size2++) {
                int v = val[size2];
                size += v >= 0 ? EncodedBuffer.getRawVarint32Size(v) : 10;
            }
            writeKnownLengthHeader(id, size);
            for (int i = 0; i < N; i++) {
                writeUnsignedVarintFromSignedInt(val[i]);
            }
        }
    }

    @Deprecated
    public void writeInt64(long fieldId, long val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1112396529664L);
        writeInt64Impl(id, val);
    }

    private synchronized void writeInt64Impl(int id, long val) {
        if (val != 0) {
            writeTag(id, 0);
            this.mBuffer.writeRawVarint64(val);
        }
    }

    @Deprecated
    public void writeRepeatedInt64(long fieldId, long val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2211908157440L);
        writeRepeatedInt64Impl(id, val);
    }

    private synchronized void writeRepeatedInt64Impl(int id, long val) {
        writeTag(id, 0);
        this.mBuffer.writeRawVarint64(val);
    }

    @Deprecated
    public void writePackedInt64(long fieldId, long[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5510443040768L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int size = 0;
            for (int size2 = 0; size2 < N; size2++) {
                size += EncodedBuffer.getRawVarint64Size(val[size2]);
            }
            writeKnownLengthHeader(id, size);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawVarint64(val[i]);
            }
        }
    }

    @Deprecated
    public void writeUInt32(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1155346202624L);
        writeUInt32Impl(id, val);
    }

    private synchronized void writeUInt32Impl(int id, int val) {
        if (val != 0) {
            writeTag(id, 0);
            this.mBuffer.writeRawVarint32(val);
        }
    }

    @Deprecated
    public void writeRepeatedUInt32(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2254857830400L);
        writeRepeatedUInt32Impl(id, val);
    }

    private synchronized void writeRepeatedUInt32Impl(int id, int val) {
        writeTag(id, 0);
        this.mBuffer.writeRawVarint32(val);
    }

    @Deprecated
    public void writePackedUInt32(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5553392713728L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int size = 0;
            for (int size2 = 0; size2 < N; size2++) {
                size += EncodedBuffer.getRawVarint32Size(val[size2]);
            }
            writeKnownLengthHeader(id, size);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawVarint32(val[i]);
            }
        }
    }

    @Deprecated
    public void writeUInt64(long fieldId, long val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1116691496960L);
        writeUInt64Impl(id, val);
    }

    private synchronized void writeUInt64Impl(int id, long val) {
        if (val != 0) {
            writeTag(id, 0);
            this.mBuffer.writeRawVarint64(val);
        }
    }

    @Deprecated
    public void writeRepeatedUInt64(long fieldId, long val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2216203124736L);
        writeRepeatedUInt64Impl(id, val);
    }

    private synchronized void writeRepeatedUInt64Impl(int id, long val) {
        writeTag(id, 0);
        this.mBuffer.writeRawVarint64(val);
    }

    @Deprecated
    public void writePackedUInt64(long fieldId, long[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5514738008064L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int size = 0;
            for (int size2 = 0; size2 < N; size2++) {
                size += EncodedBuffer.getRawVarint64Size(val[size2]);
            }
            writeKnownLengthHeader(id, size);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawVarint64(val[i]);
            }
        }
    }

    @Deprecated
    public void writeSInt32(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1172526071808L);
        writeSInt32Impl(id, val);
    }

    private synchronized void writeSInt32Impl(int id, int val) {
        if (val != 0) {
            writeTag(id, 0);
            this.mBuffer.writeRawZigZag32(val);
        }
    }

    @Deprecated
    public void writeRepeatedSInt32(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2272037699584L);
        writeRepeatedSInt32Impl(id, val);
    }

    private synchronized void writeRepeatedSInt32Impl(int id, int val) {
        writeTag(id, 0);
        this.mBuffer.writeRawZigZag32(val);
    }

    @Deprecated
    public void writePackedSInt32(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5570572582912L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int size = 0;
            for (int size2 = 0; size2 < N; size2++) {
                size += EncodedBuffer.getRawZigZag32Size(val[size2]);
            }
            writeKnownLengthHeader(id, size);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawZigZag32(val[i]);
            }
        }
    }

    @Deprecated
    public void writeSInt64(long fieldId, long val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1176821039104L);
        writeSInt64Impl(id, val);
    }

    private synchronized void writeSInt64Impl(int id, long val) {
        if (val != 0) {
            writeTag(id, 0);
            this.mBuffer.writeRawZigZag64(val);
        }
    }

    @Deprecated
    public void writeRepeatedSInt64(long fieldId, long val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2276332666880L);
        writeRepeatedSInt64Impl(id, val);
    }

    private synchronized void writeRepeatedSInt64Impl(int id, long val) {
        writeTag(id, 0);
        this.mBuffer.writeRawZigZag64(val);
    }

    @Deprecated
    public void writePackedSInt64(long fieldId, long[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5574867550208L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int size = 0;
            for (int size2 = 0; size2 < N; size2++) {
                size += EncodedBuffer.getRawZigZag64Size(val[size2]);
            }
            writeKnownLengthHeader(id, size);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawZigZag64(val[i]);
            }
        }
    }

    @Deprecated
    public void writeFixed32(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1129576398848L);
        writeFixed32Impl(id, val);
    }

    private synchronized void writeFixed32Impl(int id, int val) {
        if (val != 0) {
            writeTag(id, 5);
            this.mBuffer.writeRawFixed32(val);
        }
    }

    @Deprecated
    public void writeRepeatedFixed32(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2229088026624L);
        writeRepeatedFixed32Impl(id, val);
    }

    private synchronized void writeRepeatedFixed32Impl(int id, int val) {
        writeTag(id, 5);
        this.mBuffer.writeRawFixed32(val);
    }

    @Deprecated
    public void writePackedFixed32(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5527622909952L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 4);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed32(val[i]);
            }
        }
    }

    @Deprecated
    public void writeFixed64(long fieldId, long val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1125281431552L);
        writeFixed64Impl(id, val);
    }

    private synchronized void writeFixed64Impl(int id, long val) {
        if (val != 0) {
            writeTag(id, 1);
            this.mBuffer.writeRawFixed64(val);
        }
    }

    @Deprecated
    public void writeRepeatedFixed64(long fieldId, long val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2224793059328L);
        writeRepeatedFixed64Impl(id, val);
    }

    private synchronized void writeRepeatedFixed64Impl(int id, long val) {
        writeTag(id, 1);
        this.mBuffer.writeRawFixed64(val);
    }

    @Deprecated
    public void writePackedFixed64(long fieldId, long[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5523327942656L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 8);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed64(val[i]);
            }
        }
    }

    @Deprecated
    public void writeSFixed32(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1163936137216L);
        writeSFixed32Impl(id, val);
    }

    private synchronized void writeSFixed32Impl(int id, int val) {
        if (val != 0) {
            writeTag(id, 5);
            this.mBuffer.writeRawFixed32(val);
        }
    }

    @Deprecated
    public void writeRepeatedSFixed32(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2263447764992L);
        writeRepeatedSFixed32Impl(id, val);
    }

    private synchronized void writeRepeatedSFixed32Impl(int id, int val) {
        writeTag(id, 5);
        this.mBuffer.writeRawFixed32(val);
    }

    @Deprecated
    public void writePackedSFixed32(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5561982648320L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 4);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed32(val[i]);
            }
        }
    }

    @Deprecated
    public void writeSFixed64(long fieldId, long val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1168231104512L);
        writeSFixed64Impl(id, val);
    }

    private synchronized void writeSFixed64Impl(int id, long val) {
        if (val != 0) {
            writeTag(id, 1);
            this.mBuffer.writeRawFixed64(val);
        }
    }

    @Deprecated
    public void writeRepeatedSFixed64(long fieldId, long val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2267742732288L);
        writeRepeatedSFixed64Impl(id, val);
    }

    private synchronized void writeRepeatedSFixed64Impl(int id, long val) {
        writeTag(id, 1);
        this.mBuffer.writeRawFixed64(val);
    }

    @Deprecated
    public void writePackedSFixed64(long fieldId, long[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5566277615616L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N * 8);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawFixed64(val[i]);
            }
        }
    }

    @Deprecated
    public void writeBool(long fieldId, boolean val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1133871366144L);
        writeBoolImpl(id, val);
    }

    private synchronized void writeBoolImpl(int id, boolean val) {
        if (val) {
            writeTag(id, 0);
            this.mBuffer.writeRawByte((byte) 1);
        }
    }

    @Deprecated
    public void writeRepeatedBool(long fieldId, boolean val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2233382993920L);
        writeRepeatedBoolImpl(id, val);
    }

    private synchronized void writeRepeatedBoolImpl(int id, boolean val) {
        writeTag(id, 0);
        this.mBuffer.writeRawByte(val ? (byte) 1 : (byte) 0);
    }

    @Deprecated
    public void writePackedBool(long fieldId, boolean[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5531917877248L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            writeKnownLengthHeader(id, N);
            for (int i = 0; i < N; i++) {
                this.mBuffer.writeRawByte(val[i] ? (byte) 1 : (byte) 0);
            }
        }
    }

    @Deprecated
    public void writeString(long fieldId, String val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1138166333440L);
        writeStringImpl(id, val);
    }

    private synchronized void writeStringImpl(int id, String val) {
        if (val != null && val.length() > 0) {
            writeUtf8String(id, val);
        }
    }

    @Deprecated
    public void writeRepeatedString(long fieldId, String val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2237677961216L);
        writeRepeatedStringImpl(id, val);
    }

    private synchronized void writeRepeatedStringImpl(int id, String val) {
        if (val == null || val.length() == 0) {
            writeKnownLengthHeader(id, 0);
        } else {
            writeUtf8String(id, val);
        }
    }

    private synchronized void writeUtf8String(int id, String val) {
        try {
            byte[] buf = val.getBytes("UTF-8");
            writeKnownLengthHeader(id, buf.length);
            this.mBuffer.writeRawBuffer(buf);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("not possible");
        }
    }

    @Deprecated
    public void writeBytes(long fieldId, byte[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1151051235328L);
        writeBytesImpl(id, val);
    }

    private synchronized void writeBytesImpl(int id, byte[] val) {
        if (val != null && val.length > 0) {
            writeKnownLengthHeader(id, val.length);
            this.mBuffer.writeRawBuffer(val);
        }
    }

    @Deprecated
    public void writeRepeatedBytes(long fieldId, byte[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2250562863104L);
        writeRepeatedBytesImpl(id, val);
    }

    private synchronized void writeRepeatedBytesImpl(int id, byte[] val) {
        writeKnownLengthHeader(id, val == null ? 0 : val.length);
        this.mBuffer.writeRawBuffer(val);
    }

    @Deprecated
    public void writeEnum(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1159641169920L);
        writeEnumImpl(id, val);
    }

    private synchronized void writeEnumImpl(int id, int val) {
        if (val != 0) {
            writeTag(id, 0);
            writeUnsignedVarintFromSignedInt(val);
        }
    }

    @Deprecated
    public void writeRepeatedEnum(long fieldId, int val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2259152797696L);
        writeRepeatedEnumImpl(id, val);
    }

    private synchronized void writeRepeatedEnumImpl(int id, int val) {
        writeTag(id, 0);
        writeUnsignedVarintFromSignedInt(val);
    }

    @Deprecated
    public void writePackedEnum(long fieldId, int[] val) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 5557687681024L);
        int N = val != null ? val.length : 0;
        if (N > 0) {
            int size = 0;
            for (int size2 = 0; size2 < N; size2++) {
                int v = val[size2];
                size += v >= 0 ? EncodedBuffer.getRawVarint32Size(v) : 10;
            }
            writeKnownLengthHeader(id, size);
            for (int i = 0; i < N; i++) {
                writeUnsignedVarintFromSignedInt(val[i]);
            }
        }
    }

    public static long makeToken(int tagSize, boolean repeated, int depth, int objectId, int sizePos) {
        return ((tagSize & 7) << 61) | (repeated ? 1152921504606846976L : 0L) | ((511 & depth) << 51) | ((524287 & objectId) << 32) | (4294967295L & sizePos);
    }

    public static int getTagSizeFromToken(long token) {
        return (int) ((token >> 61) & 7);
    }

    public static boolean getRepeatedFromToken(long token) {
        return ((token >> 60) & 1) != 0;
    }

    public static int getDepthFromToken(long token) {
        return (int) ((token >> 51) & 511);
    }

    public static int getObjectIdFromToken(long token) {
        return (int) ((token >> 32) & 524287);
    }

    public static int getSizePosFromToken(long token) {
        return (int) token;
    }

    public static int convertObjectIdToOrdinal(int objectId) {
        return EventLogTags.SYSUI_VIEW_VISIBILITY - objectId;
    }

    public static String token2String(long token) {
        if (token == 0) {
            return "Token(0)";
        }
        return "Token(val=0x" + Long.toHexString(token) + " depth=" + getDepthFromToken(token) + " object=" + convertObjectIdToOrdinal(getObjectIdFromToken(token)) + " tagSize=" + getTagSizeFromToken(token) + " sizePos=" + getSizePosFromToken(token) + ')';
    }

    @Deprecated
    public long startObject(long fieldId) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1146756268032L);
        return startObjectImpl(id, false);
    }

    @Deprecated
    public void endObject(long token) {
        assertNotCompacted();
        endObjectImpl(token, false);
    }

    @Deprecated
    public long startRepeatedObject(long fieldId) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2246267895808L);
        return startObjectImpl(id, true);
    }

    @Deprecated
    public void endRepeatedObject(long token) {
        assertNotCompacted();
        endObjectImpl(token, true);
    }

    private synchronized long startObjectImpl(int id, boolean repeated) {
        writeTag(id, 2);
        int sizePos = this.mBuffer.getWritePos();
        this.mDepth++;
        this.mNextObjectId--;
        this.mBuffer.writeRawFixed32((int) (this.mExpectedObjectToken >> 32));
        this.mBuffer.writeRawFixed32((int) this.mExpectedObjectToken);
        long j = this.mExpectedObjectToken;
        this.mExpectedObjectToken = makeToken(getTagSize(id), repeated, this.mDepth, this.mNextObjectId, sizePos);
        return this.mExpectedObjectToken;
    }

    private synchronized void endObjectImpl(long token, boolean repeated) {
        int depth = getDepthFromToken(token);
        boolean expectedRepeated = getRepeatedFromToken(token);
        int sizePos = getSizePosFromToken(token);
        int childRawSize = (this.mBuffer.getWritePos() - sizePos) - 8;
        if (repeated != expectedRepeated) {
            if (repeated) {
                throw new IllegalArgumentException("endRepeatedObject called where endObject should have been");
            }
            throw new IllegalArgumentException("endObject called where endRepeatedObject should have been");
        } else if ((this.mDepth & 511) != depth || this.mExpectedObjectToken != token) {
            throw new IllegalArgumentException("Mismatched startObject/endObject calls. Current depth " + this.mDepth + " token=" + token2String(token) + " expectedToken=" + token2String(this.mExpectedObjectToken));
        } else {
            this.mExpectedObjectToken = (this.mBuffer.getRawFixed32At(sizePos) << 32) | (4294967295L & this.mBuffer.getRawFixed32At(sizePos + 4));
            this.mDepth--;
            if (childRawSize > 0) {
                this.mBuffer.editRawFixed32(sizePos, -childRawSize);
                this.mBuffer.editRawFixed32(sizePos + 4, -1);
            } else if (repeated) {
                this.mBuffer.editRawFixed32(sizePos, 0);
                this.mBuffer.editRawFixed32(sizePos + 4, 0);
            } else {
                this.mBuffer.rewindWriteTo(sizePos - getTagSizeFromToken(token));
            }
        }
    }

    @Deprecated
    public void writeObject(long fieldId, byte[] value) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 1146756268032L);
        writeObjectImpl(id, value);
    }

    synchronized void writeObjectImpl(int id, byte[] value) {
        if (value != null && value.length != 0) {
            writeKnownLengthHeader(id, value.length);
            this.mBuffer.writeRawBuffer(value);
        }
    }

    @Deprecated
    public void writeRepeatedObject(long fieldId, byte[] value) {
        assertNotCompacted();
        int id = checkFieldId(fieldId, 2246267895808L);
        writeRepeatedObjectImpl(id, value);
    }

    synchronized void writeRepeatedObjectImpl(int id, byte[] value) {
        writeKnownLengthHeader(id, value == null ? 0 : value.length);
        this.mBuffer.writeRawBuffer(value);
    }

    public static long makeFieldId(int id, long fieldFlags) {
        return (id & 4294967295L) | fieldFlags;
    }

    public static int checkFieldId(long fieldId, long expectedFlags) {
        long fieldCount = fieldId & FIELD_COUNT_MASK;
        long fieldType = fieldId & FIELD_TYPE_MASK;
        long expectedCount = expectedFlags & FIELD_COUNT_MASK;
        long expectedType = expectedFlags & FIELD_TYPE_MASK;
        if (((int) fieldId) == 0) {
            throw new IllegalArgumentException("Invalid proto field " + ((int) fieldId) + " fieldId=" + Long.toHexString(fieldId));
        } else if (fieldType != expectedType || (fieldCount != expectedCount && (fieldCount != FIELD_COUNT_PACKED || expectedCount != FIELD_COUNT_REPEATED))) {
            String countString = getFieldCountString(fieldCount);
            String typeString = getFieldTypeString(fieldType);
            if (typeString != null && countString != null) {
                StringBuilder sb = new StringBuilder();
                if (expectedType == FIELD_TYPE_MESSAGE) {
                    sb.append(Telephony.BaseMmsColumns.START);
                } else {
                    sb.append("write");
                }
                sb.append(getFieldCountString(expectedCount));
                sb.append(getFieldTypeString(expectedType));
                sb.append(" called for field ");
                sb.append((int) fieldId);
                sb.append(" which should be used with ");
                if (fieldType == FIELD_TYPE_MESSAGE) {
                    sb.append(Telephony.BaseMmsColumns.START);
                } else {
                    sb.append("write");
                }
                sb.append(countString);
                sb.append(typeString);
                if (fieldCount == FIELD_COUNT_PACKED) {
                    sb.append(" or writeRepeated");
                    sb.append(typeString);
                }
                sb.append('.');
                throw new IllegalArgumentException(sb.toString());
            }
            StringBuilder sb2 = new StringBuilder();
            if (expectedType == FIELD_TYPE_MESSAGE) {
                sb2.append(Telephony.BaseMmsColumns.START);
            } else {
                sb2.append("write");
            }
            sb2.append(getFieldCountString(expectedCount));
            sb2.append(getFieldTypeString(expectedType));
            sb2.append(" called with an invalid fieldId: 0x");
            sb2.append(Long.toHexString(fieldId));
            sb2.append(". The proto field ID might be ");
            sb2.append((int) fieldId);
            sb2.append('.');
            throw new IllegalArgumentException(sb2.toString());
        } else {
            return (int) fieldId;
        }
    }

    private static synchronized String getFieldTypeString(long fieldType) {
        int index = ((int) ((FIELD_TYPE_MASK & fieldType) >>> 32)) - 1;
        if (index >= 0 && index < FIELD_TYPE_NAMES.length) {
            return FIELD_TYPE_NAMES[index];
        }
        return null;
    }

    private static synchronized String getFieldCountString(long fieldCount) {
        if (fieldCount == 1099511627776L) {
            return "";
        }
        if (fieldCount == FIELD_COUNT_REPEATED) {
            return "Repeated";
        }
        if (fieldCount == FIELD_COUNT_PACKED) {
            return "Packed";
        }
        return null;
    }

    private synchronized String getFieldIdString(long fieldId) {
        long fieldCount = FIELD_COUNT_MASK & fieldId;
        String countString = getFieldCountString(fieldCount);
        if (countString == null) {
            countString = "fieldCount=" + fieldCount;
        }
        if (countString.length() > 0) {
            countString = countString + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        }
        long fieldType = FIELD_TYPE_MASK & fieldId;
        String typeString = getFieldTypeString(fieldType);
        if (typeString == null) {
            typeString = "fieldType=" + fieldType;
        }
        return countString + typeString + " tag=" + ((int) fieldId) + " fieldId=0x" + Long.toHexString(fieldId);
    }

    private static synchronized int getTagSize(int id) {
        return EncodedBuffer.getRawVarint32Size(id << 3);
    }

    public void writeTag(int id, int wireType) {
        this.mBuffer.writeRawVarint32((id << 3) | wireType);
    }

    private synchronized void writeKnownLengthHeader(int id, int size) {
        writeTag(id, 2);
        this.mBuffer.writeRawFixed32(size);
        this.mBuffer.writeRawFixed32(size);
    }

    private synchronized void assertNotCompacted() {
        if (this.mCompacted) {
            throw new IllegalArgumentException("write called after compact");
        }
    }

    public byte[] getBytes() {
        compactIfNecessary();
        return this.mBuffer.getBytes(this.mBuffer.getReadableSize());
    }

    private synchronized void compactIfNecessary() {
        if (!this.mCompacted) {
            if (this.mDepth != 0) {
                throw new IllegalArgumentException("Trying to compact with " + this.mDepth + " missing calls to endObject");
            }
            this.mBuffer.startEditing();
            int readableSize = this.mBuffer.getReadableSize();
            editEncodedSize(readableSize);
            this.mBuffer.rewindRead();
            compactSizes(readableSize);
            if (this.mCopyBegin < readableSize) {
                this.mBuffer.writeFromThisBuffer(this.mCopyBegin, readableSize - this.mCopyBegin);
            }
            this.mBuffer.startEditing();
            this.mCompacted = true;
        }
    }

    private synchronized int editEncodedSize(int rawSize) {
        int objectStart = this.mBuffer.getReadPos();
        int objectEnd = objectStart + rawSize;
        int encodedSize = 0;
        while (true) {
            int tagPos = this.mBuffer.getReadPos();
            if (tagPos < objectEnd) {
                int tag = readRawTag();
                encodedSize += EncodedBuffer.getRawVarint32Size(tag);
                int wireType = tag & 7;
                switch (wireType) {
                    case 0:
                        while (true) {
                            encodedSize++;
                            if ((this.mBuffer.readRawByte() & 128) != 0) {
                            }
                        }
                        break;
                    case 1:
                        encodedSize += 8;
                        this.mBuffer.skipRead(8);
                        break;
                    case 2:
                        int childRawSize = this.mBuffer.readRawFixed32();
                        int childEncodedSizePos = this.mBuffer.getReadPos();
                        int childEncodedSize = this.mBuffer.readRawFixed32();
                        if (childRawSize >= 0) {
                            if (childEncodedSize != childRawSize) {
                                throw new RuntimeException("Pre-computed size where the precomputed size and the raw size in the buffer don't match! childRawSize=" + childRawSize + " childEncodedSize=" + childEncodedSize + " childEncodedSizePos=" + childEncodedSizePos);
                            }
                            this.mBuffer.skipRead(childRawSize);
                        } else {
                            childEncodedSize = editEncodedSize(-childRawSize);
                            this.mBuffer.editRawFixed32(childEncodedSizePos, childEncodedSize);
                        }
                        encodedSize += EncodedBuffer.getRawVarint32Size(childEncodedSize) + childEncodedSize;
                        break;
                    case 3:
                    case 4:
                        throw new RuntimeException("groups not supported at index " + tagPos);
                    case 5:
                        encodedSize += 4;
                        this.mBuffer.skipRead(4);
                        break;
                    default:
                        throw new ProtoParseException("editEncodedSize Bad tag tag=0x" + Integer.toHexString(tag) + " wireType=" + wireType + " -- " + this.mBuffer.getDebugString());
                }
            } else {
                return encodedSize;
            }
        }
    }

    private synchronized void compactSizes(int rawSize) {
        int objectStart = this.mBuffer.getReadPos();
        int objectEnd = objectStart + rawSize;
        while (true) {
            int tagPos = this.mBuffer.getReadPos();
            if (tagPos < objectEnd) {
                int tag = readRawTag();
                int wireType = tag & 7;
                switch (wireType) {
                    case 0:
                        do {
                        } while ((this.mBuffer.readRawByte() & 128) != 0);
                        break;
                    case 1:
                        this.mBuffer.skipRead(8);
                        break;
                    case 2:
                        this.mBuffer.writeFromThisBuffer(this.mCopyBegin, this.mBuffer.getReadPos() - this.mCopyBegin);
                        int childRawSize = this.mBuffer.readRawFixed32();
                        int childEncodedSize = this.mBuffer.readRawFixed32();
                        this.mBuffer.writeRawVarint32(childEncodedSize);
                        this.mCopyBegin = this.mBuffer.getReadPos();
                        if (childRawSize >= 0) {
                            this.mBuffer.skipRead(childEncodedSize);
                            break;
                        } else {
                            compactSizes(-childRawSize);
                            break;
                        }
                    case 3:
                    case 4:
                        throw new RuntimeException("groups not supported at index " + tagPos);
                    case 5:
                        this.mBuffer.skipRead(4);
                        break;
                    default:
                        throw new ProtoParseException("compactSizes Bad tag tag=0x" + Integer.toHexString(tag) + " wireType=" + wireType + " -- " + this.mBuffer.getDebugString());
                }
            } else {
                return;
            }
        }
    }

    public void flush() {
        if (this.mStream == null || this.mDepth != 0 || this.mCompacted) {
            return;
        }
        compactIfNecessary();
        byte[] data = this.mBuffer.getBytes(this.mBuffer.getReadableSize());
        try {
            this.mStream.write(data);
            this.mStream.flush();
        } catch (IOException ex) {
            throw new RuntimeException("Error flushing proto to stream", ex);
        }
    }

    private synchronized int readRawTag() {
        if (this.mBuffer.getReadPos() == this.mBuffer.getReadableSize()) {
            return 0;
        }
        return (int) this.mBuffer.readRawUnsigned();
    }

    public void dump(String tag) {
        Log.d(tag, this.mBuffer.getDebugString());
        this.mBuffer.dumpBuffers(tag);
    }
}
