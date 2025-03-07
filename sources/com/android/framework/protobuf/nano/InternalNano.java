package com.android.framework.protobuf.nano;

import com.android.framework.protobuf.nano.MapFactories;
import com.xiaopeng.util.FeatureOption;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

/* loaded from: classes3.dex */
public final class InternalNano {
    public static final int TYPE_BOOL = 8;
    public static final int TYPE_BYTES = 12;
    public static final int TYPE_DOUBLE = 1;
    public static final int TYPE_ENUM = 14;
    public static final int TYPE_FIXED32 = 7;
    public static final int TYPE_FIXED64 = 6;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_GROUP = 10;
    public static final int TYPE_INT32 = 5;
    public static final int TYPE_INT64 = 3;
    public static final int TYPE_MESSAGE = 11;
    public static final int TYPE_SFIXED32 = 15;
    public static final int TYPE_SFIXED64 = 16;
    public static final int TYPE_SINT32 = 17;
    public static final int TYPE_SINT64 = 18;
    public static final int TYPE_STRING = 9;
    public static final int TYPE_UINT32 = 13;
    public static final int TYPE_UINT64 = 4;
    protected static final Charset UTF_8 = Charset.forName("UTF-8");
    protected static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Object LAZY_INIT_LOCK = new Object();

    private InternalNano() {
    }

    public static String stringDefaultValue(String bytes) {
        return new String(bytes.getBytes(ISO_8859_1), UTF_8);
    }

    public static byte[] bytesDefaultValue(String bytes) {
        return bytes.getBytes(ISO_8859_1);
    }

    public static byte[] copyFromUtf8(String text) {
        return text.getBytes(UTF_8);
    }

    public static boolean equals(int[] field1, int[] field2) {
        if (field1 == null || field1.length == 0) {
            return field2 == null || field2.length == 0;
        }
        return Arrays.equals(field1, field2);
    }

    public static boolean equals(long[] field1, long[] field2) {
        if (field1 == null || field1.length == 0) {
            return field2 == null || field2.length == 0;
        }
        return Arrays.equals(field1, field2);
    }

    public static boolean equals(float[] field1, float[] field2) {
        if (field1 == null || field1.length == 0) {
            return field2 == null || field2.length == 0;
        }
        return Arrays.equals(field1, field2);
    }

    public static boolean equals(double[] field1, double[] field2) {
        if (field1 == null || field1.length == 0) {
            return field2 == null || field2.length == 0;
        }
        return Arrays.equals(field1, field2);
    }

    public static boolean equals(boolean[] field1, boolean[] field2) {
        if (field1 == null || field1.length == 0) {
            return field2 == null || field2.length == 0;
        }
        return Arrays.equals(field1, field2);
    }

    public static boolean equals(byte[][] field1, byte[][] field2) {
        boolean atEndOf1;
        boolean atEndOf2;
        int index1 = 0;
        int length1 = field1 == null ? 0 : field1.length;
        int index2 = 0;
        int length2 = field2 == null ? 0 : field2.length;
        while (true) {
            if (index1 < length1 && field1[index1] == null) {
                index1++;
            } else {
                while (index2 < length2 && field2[index2] == null) {
                    index2++;
                }
                if (index1 < length1) {
                    atEndOf1 = false;
                } else {
                    atEndOf1 = true;
                }
                if (index2 < length2) {
                    atEndOf2 = false;
                } else {
                    atEndOf2 = true;
                }
                if (atEndOf1 && atEndOf2) {
                    return true;
                }
                if (atEndOf1 != atEndOf2 || !Arrays.equals(field1[index1], field2[index2])) {
                    return false;
                }
                index1++;
                index2++;
            }
        }
    }

    public static boolean equals(Object[] field1, Object[] field2) {
        boolean atEndOf1;
        boolean atEndOf2;
        int index1 = 0;
        int length1 = field1 == null ? 0 : field1.length;
        int index2 = 0;
        int length2 = field2 == null ? 0 : field2.length;
        while (true) {
            if (index1 < length1 && field1[index1] == null) {
                index1++;
            } else {
                while (index2 < length2 && field2[index2] == null) {
                    index2++;
                }
                if (index1 < length1) {
                    atEndOf1 = false;
                } else {
                    atEndOf1 = true;
                }
                if (index2 < length2) {
                    atEndOf2 = false;
                } else {
                    atEndOf2 = true;
                }
                if (atEndOf1 && atEndOf2) {
                    return true;
                }
                if (atEndOf1 != atEndOf2 || !field1[index1].equals(field2[index2])) {
                    return false;
                }
                index1++;
                index2++;
            }
        }
    }

    public static int hashCode(int[] field) {
        if (field == null || field.length == 0) {
            return 0;
        }
        return Arrays.hashCode(field);
    }

    public static int hashCode(long[] field) {
        if (field == null || field.length == 0) {
            return 0;
        }
        return Arrays.hashCode(field);
    }

    public static int hashCode(float[] field) {
        if (field == null || field.length == 0) {
            return 0;
        }
        return Arrays.hashCode(field);
    }

    public static int hashCode(double[] field) {
        if (field == null || field.length == 0) {
            return 0;
        }
        return Arrays.hashCode(field);
    }

    public static int hashCode(boolean[] field) {
        if (field == null || field.length == 0) {
            return 0;
        }
        return Arrays.hashCode(field);
    }

    public static int hashCode(byte[][] field) {
        int result = 0;
        int size = field == null ? 0 : field.length;
        for (int i = 0; i < size; i++) {
            byte[] element = field[i];
            if (element != null) {
                result = (result * 31) + Arrays.hashCode(element);
            }
        }
        return result;
    }

    public static int hashCode(Object[] field) {
        int result = 0;
        int size = field == null ? 0 : field.length;
        for (int i = 0; i < size; i++) {
            Object element = field[i];
            if (element != null) {
                result = (result * 31) + element.hashCode();
            }
        }
        return result;
    }

    private static Object primitiveDefaultValue(int type) {
        switch (type) {
            case 1:
                return Double.valueOf((double) FeatureOption.FO_BOOT_POLICY_CPU);
            case 2:
                return Float.valueOf(0.0f);
            case 3:
            case 4:
            case 6:
            case 16:
            case 18:
                return 0L;
            case 5:
            case 7:
            case 13:
            case 14:
            case 15:
            case 17:
                return 0;
            case 8:
                return Boolean.FALSE;
            case 9:
                return "";
            case 10:
            case 11:
            default:
                throw new IllegalArgumentException("Type: " + type + " is not a primitive type.");
            case 12:
                return WireFormatNano.EMPTY_BYTES;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v2, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r10v7, types: [java.lang.Object] */
    public static final <K, V> Map<K, V> mergeMapEntry(CodedInputByteBufferNano input, Map<K, V> map, MapFactories.MapFactory mapFactory, int keyType, int valueType, V value, int keyTag, int valueTag) throws IOException {
        Map<K, V> map2 = mapFactory.forMap(map);
        int length = input.readRawVarint32();
        int oldLimit = input.pushLimit(length);
        Object obj = null;
        while (true) {
            int tag = input.readTag();
            if (tag == 0) {
                break;
            } else if (tag == keyTag) {
                obj = input.readPrimitiveField(keyType);
            } else if (tag == valueTag) {
                if (valueType == 11) {
                    input.readMessage(value);
                } else {
                    value = input.readPrimitiveField(valueType);
                }
            } else if (!input.skipField(tag)) {
                break;
            }
        }
        input.checkLastTagWas(0);
        input.popLimit(oldLimit);
        if (obj == null) {
            obj = primitiveDefaultValue(keyType);
        }
        if (value == 0) {
            value = (V) primitiveDefaultValue(valueType);
        }
        map2.put(obj, (Object) value);
        return map2;
    }

    public static <K, V> void serializeMapField(CodedOutputByteBufferNano output, Map<K, V> map, int number, int keyType, int valueType) throws IOException {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (key == null || value == null) {
                throw new IllegalStateException("keys and values in maps cannot be null");
            }
            int entrySize = CodedOutputByteBufferNano.computeFieldSize(1, keyType, key) + CodedOutputByteBufferNano.computeFieldSize(2, valueType, value);
            output.writeTag(number, 2);
            output.writeRawVarint32(entrySize);
            output.writeField(1, keyType, key);
            output.writeField(2, valueType, value);
        }
    }

    public static <K, V> int computeMapFieldSize(Map<K, V> map, int number, int keyType, int valueType) {
        int size = 0;
        int tagSize = CodedOutputByteBufferNano.computeTagSize(number);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (key == null || value == null) {
                throw new IllegalStateException("keys and values in maps cannot be null");
            }
            int entrySize = CodedOutputByteBufferNano.computeFieldSize(1, keyType, key) + CodedOutputByteBufferNano.computeFieldSize(2, valueType, value);
            size += tagSize + entrySize + CodedOutputByteBufferNano.computeRawVarint32Size(entrySize);
        }
        return size;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0034  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <K, V> boolean equals(java.util.Map<K, V> r6, java.util.Map<K, V> r7) {
        /*
            r0 = 1
            if (r6 != r7) goto L4
            return r0
        L4:
            r1 = 0
            if (r6 != 0) goto L10
            int r2 = r7.size()
            if (r2 != 0) goto Le
            goto Lf
        Le:
            r0 = r1
        Lf:
            return r0
        L10:
            if (r7 != 0) goto L1b
            int r2 = r6.size()
            if (r2 != 0) goto L19
            goto L1a
        L19:
            r0 = r1
        L1a:
            return r0
        L1b:
            int r2 = r6.size()
            int r3 = r7.size()
            if (r2 == r3) goto L26
            return r1
        L26:
            java.util.Set r2 = r6.entrySet()
            java.util.Iterator r2 = r2.iterator()
        L2e:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L59
            java.lang.Object r3 = r2.next()
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3
            java.lang.Object r4 = r3.getKey()
            boolean r4 = r7.containsKey(r4)
            if (r4 != 0) goto L45
            return r1
        L45:
            java.lang.Object r4 = r3.getValue()
            java.lang.Object r5 = r3.getKey()
            java.lang.Object r5 = r7.get(r5)
            boolean r4 = equalsMapValue(r4, r5)
            if (r4 != 0) goto L58
            return r1
        L58:
            goto L2e
        L59:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.framework.protobuf.nano.InternalNano.equals(java.util.Map, java.util.Map):boolean");
    }

    private static boolean equalsMapValue(Object a, Object b) {
        if (a == null || b == null) {
            throw new IllegalStateException("keys and values in maps cannot be null");
        }
        if ((a instanceof byte[]) && (b instanceof byte[])) {
            return Arrays.equals((byte[]) a, (byte[]) b);
        }
        return a.equals(b);
    }

    public static <K, V> int hashCode(Map<K, V> map) {
        if (map == null) {
            return 0;
        }
        int result = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            result += hashCodeForMap(entry.getKey()) ^ hashCodeForMap(entry.getValue());
        }
        return result;
    }

    private static int hashCodeForMap(Object o) {
        if (o instanceof byte[]) {
            return Arrays.hashCode((byte[]) o);
        }
        return o.hashCode();
    }

    public static void cloneUnknownFieldData(ExtendableMessageNano original, ExtendableMessageNano cloned) {
        if (original.unknownFieldData != null) {
            cloned.unknownFieldData = original.unknownFieldData.m50clone();
        }
    }
}
