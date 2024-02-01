package com.android.framework.protobuf.nano;

import com.android.framework.protobuf.nano.ExtendableMessageNano;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes3.dex */
public class Extension<M extends ExtendableMessageNano<M>, T> {
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
    protected final Class<T> clazz;
    protected final boolean repeated;
    public final int tag;
    protected final int type;

    @Deprecated
    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(int type, Class<T> clazz, int tag) {
        return new Extension<>(type, clazz, tag, false);
    }

    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(int type, Class<T> clazz, long tag) {
        return new Extension<>(type, clazz, (int) tag, false);
    }

    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T[]> createRepeatedMessageTyped(int type, Class<T[]> clazz, long tag) {
        return new Extension<>(type, clazz, (int) tag, true);
    }

    public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createPrimitiveTyped(int type, Class<T> clazz, long tag) {
        return new PrimitiveExtension(type, clazz, (int) tag, false, 0, 0);
    }

    public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createRepeatedPrimitiveTyped(int type, Class<T> clazz, long tag, long nonPackedTag, long packedTag) {
        return new PrimitiveExtension(type, clazz, (int) tag, true, (int) nonPackedTag, (int) packedTag);
    }

    private Extension(int type, Class<T> clazz, int tag, boolean repeated) {
        this.type = type;
        this.clazz = clazz;
        this.tag = tag;
        this.repeated = repeated;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final T getValueFrom(List<UnknownFieldData> unknownFields) {
        if (unknownFields == null) {
            return null;
        }
        return this.repeated ? getRepeatedValueFrom(unknownFields) : getSingularValueFrom(unknownFields);
    }

    private T getRepeatedValueFrom(List<UnknownFieldData> unknownFields) {
        List<Object> resultList = new ArrayList<>();
        for (int i = 0; i < unknownFields.size(); i++) {
            UnknownFieldData data = unknownFields.get(i);
            if (data.bytes.length != 0) {
                readDataInto(data, resultList);
            }
        }
        int resultSize = resultList.size();
        if (resultSize == 0) {
            return null;
        }
        T result = this.clazz.cast(Array.newInstance(this.clazz.getComponentType(), resultSize));
        for (int i2 = 0; i2 < resultSize; i2++) {
            Array.set(result, i2, resultList.get(i2));
        }
        return result;
    }

    private T getSingularValueFrom(List<UnknownFieldData> unknownFields) {
        if (unknownFields.isEmpty()) {
            return null;
        }
        UnknownFieldData lastData = unknownFields.get(unknownFields.size() - 1);
        return this.clazz.cast(readData(CodedInputByteBufferNano.newInstance(lastData.bytes)));
    }

    protected Object readData(CodedInputByteBufferNano input) {
        Class componentType = this.repeated ? this.clazz.getComponentType() : this.clazz;
        try {
            switch (this.type) {
                case 10:
                    MessageNano group = (MessageNano) componentType.newInstance();
                    input.readGroup(group, WireFormatNano.getTagFieldNumber(this.tag));
                    return group;
                case 11:
                    MessageNano message = (MessageNano) componentType.newInstance();
                    input.readMessage(message);
                    return message;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading extension field", e);
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e2);
        } catch (InstantiationException e3) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e3);
        }
    }

    protected void readDataInto(UnknownFieldData data, List<Object> resultList) {
        resultList.add(readData(CodedInputByteBufferNano.newInstance(data.bytes)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeTo(Object value, CodedOutputByteBufferNano output) throws IOException {
        if (this.repeated) {
            writeRepeatedData(value, output);
        } else {
            writeSingularData(value, output);
        }
    }

    protected void writeSingularData(Object value, CodedOutputByteBufferNano out) {
        try {
            out.writeRawVarint32(this.tag);
            switch (this.type) {
                case 10:
                    MessageNano groupValue = (MessageNano) value;
                    int fieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
                    out.writeGroupNoTag(groupValue);
                    out.writeTag(fieldNumber, 4);
                    return;
                case 11:
                    MessageNano messageValue = (MessageNano) value;
                    out.writeMessageNoTag(messageValue);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void writeRepeatedData(Object array, CodedOutputByteBufferNano output) {
        int arrayLength = Array.getLength(array);
        for (int i = 0; i < arrayLength; i++) {
            Object element = Array.get(array, i);
            if (element != null) {
                writeSingularData(element, output);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int computeSerializedSize(Object value) {
        if (this.repeated) {
            return computeRepeatedSerializedSize(value);
        }
        return computeSingularSerializedSize(value);
    }

    protected int computeRepeatedSerializedSize(Object array) {
        int size = 0;
        int arrayLength = Array.getLength(array);
        for (int i = 0; i < arrayLength; i++) {
            Object element = Array.get(array, i);
            if (element != null) {
                size += computeSingularSerializedSize(Array.get(array, i));
            }
        }
        return size;
    }

    protected int computeSingularSerializedSize(Object value) {
        int fieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
        switch (this.type) {
            case 10:
                MessageNano groupValue = (MessageNano) value;
                return CodedOutputByteBufferNano.computeGroupSize(fieldNumber, groupValue);
            case 11:
                MessageNano messageValue = (MessageNano) value;
                return CodedOutputByteBufferNano.computeMessageSize(fieldNumber, messageValue);
            default:
                throw new IllegalArgumentException("Unknown type " + this.type);
        }
    }

    /* loaded from: classes3.dex */
    private static class PrimitiveExtension<M extends ExtendableMessageNano<M>, T> extends Extension<M, T> {
        private final int nonPackedTag;
        private final int packedTag;

        public PrimitiveExtension(int type, Class<T> clazz, int tag, boolean repeated, int nonPackedTag, int packedTag) {
            super(type, clazz, tag, repeated);
            this.nonPackedTag = nonPackedTag;
            this.packedTag = packedTag;
        }

        @Override // com.android.framework.protobuf.nano.Extension
        protected Object readData(CodedInputByteBufferNano input) {
            try {
                return input.readPrimitiveField(this.type);
            } catch (IOException e) {
                throw new IllegalArgumentException("Error reading extension field", e);
            }
        }

        @Override // com.android.framework.protobuf.nano.Extension
        protected void readDataInto(UnknownFieldData data, List<Object> resultList) {
            if (data.tag == this.nonPackedTag) {
                resultList.add(readData(CodedInputByteBufferNano.newInstance(data.bytes)));
                return;
            }
            CodedInputByteBufferNano buffer = CodedInputByteBufferNano.newInstance(data.bytes);
            try {
                buffer.pushLimit(buffer.readRawVarint32());
                while (!buffer.isAtEnd()) {
                    resultList.add(readData(buffer));
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Error reading extension field", e);
            }
        }

        @Override // com.android.framework.protobuf.nano.Extension
        protected final void writeSingularData(Object value, CodedOutputByteBufferNano output) {
            try {
                output.writeRawVarint32(this.tag);
                switch (this.type) {
                    case 1:
                        Double doubleValue = (Double) value;
                        output.writeDoubleNoTag(doubleValue.doubleValue());
                        return;
                    case 2:
                        Float floatValue = (Float) value;
                        output.writeFloatNoTag(floatValue.floatValue());
                        return;
                    case 3:
                        Long int64Value = (Long) value;
                        output.writeInt64NoTag(int64Value.longValue());
                        return;
                    case 4:
                        Long uint64Value = (Long) value;
                        output.writeUInt64NoTag(uint64Value.longValue());
                        return;
                    case 5:
                        Integer int32Value = (Integer) value;
                        output.writeInt32NoTag(int32Value.intValue());
                        return;
                    case 6:
                        Long fixed64Value = (Long) value;
                        output.writeFixed64NoTag(fixed64Value.longValue());
                        return;
                    case 7:
                        Integer fixed32Value = (Integer) value;
                        output.writeFixed32NoTag(fixed32Value.intValue());
                        return;
                    case 8:
                        Boolean boolValue = (Boolean) value;
                        output.writeBoolNoTag(boolValue.booleanValue());
                        return;
                    case 9:
                        String stringValue = (String) value;
                        output.writeStringNoTag(stringValue);
                        return;
                    case 10:
                    case 11:
                    default:
                        throw new IllegalArgumentException("Unknown type " + this.type);
                    case 12:
                        byte[] bytesValue = (byte[]) value;
                        output.writeBytesNoTag(bytesValue);
                        return;
                    case 13:
                        Integer uint32Value = (Integer) value;
                        output.writeUInt32NoTag(uint32Value.intValue());
                        return;
                    case 14:
                        Integer enumValue = (Integer) value;
                        output.writeEnumNoTag(enumValue.intValue());
                        return;
                    case 15:
                        Integer sfixed32Value = (Integer) value;
                        output.writeSFixed32NoTag(sfixed32Value.intValue());
                        return;
                    case 16:
                        Long sfixed64Value = (Long) value;
                        output.writeSFixed64NoTag(sfixed64Value.longValue());
                        return;
                    case 17:
                        Integer sint32Value = (Integer) value;
                        output.writeSInt32NoTag(sint32Value.intValue());
                        return;
                    case 18:
                        Long sint64Value = (Long) value;
                        output.writeSInt64NoTag(sint64Value.longValue());
                        return;
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.android.framework.protobuf.nano.Extension
        protected void writeRepeatedData(Object array, CodedOutputByteBufferNano output) {
            if (this.tag == this.nonPackedTag) {
                super.writeRepeatedData(array, output);
            } else if (this.tag == this.packedTag) {
                int arrayLength = Array.getLength(array);
                int dataSize = computePackedDataSize(array);
                try {
                    output.writeRawVarint32(this.tag);
                    output.writeRawVarint32(dataSize);
                    int i = this.type;
                    int i2 = 0;
                    switch (i) {
                        case 1:
                            while (true) {
                                int i3 = i2;
                                if (i3 < arrayLength) {
                                    output.writeDoubleNoTag(Array.getDouble(array, i3));
                                    i2 = i3 + 1;
                                } else {
                                    return;
                                }
                            }
                        case 2:
                            while (true) {
                                int i4 = i2;
                                if (i4 < arrayLength) {
                                    output.writeFloatNoTag(Array.getFloat(array, i4));
                                    i2 = i4 + 1;
                                } else {
                                    return;
                                }
                            }
                        case 3:
                            while (true) {
                                int i5 = i2;
                                if (i5 < arrayLength) {
                                    output.writeInt64NoTag(Array.getLong(array, i5));
                                    i2 = i5 + 1;
                                } else {
                                    return;
                                }
                            }
                        case 4:
                            while (true) {
                                int i6 = i2;
                                if (i6 < arrayLength) {
                                    output.writeUInt64NoTag(Array.getLong(array, i6));
                                    i2 = i6 + 1;
                                } else {
                                    return;
                                }
                            }
                        case 5:
                            while (true) {
                                int i7 = i2;
                                if (i7 < arrayLength) {
                                    output.writeInt32NoTag(Array.getInt(array, i7));
                                    i2 = i7 + 1;
                                } else {
                                    return;
                                }
                            }
                        case 6:
                            while (true) {
                                int i8 = i2;
                                if (i8 < arrayLength) {
                                    output.writeFixed64NoTag(Array.getLong(array, i8));
                                    i2 = i8 + 1;
                                } else {
                                    return;
                                }
                            }
                        case 7:
                            while (true) {
                                int i9 = i2;
                                if (i9 < arrayLength) {
                                    output.writeFixed32NoTag(Array.getInt(array, i9));
                                    i2 = i9 + 1;
                                } else {
                                    return;
                                }
                            }
                        case 8:
                            while (true) {
                                int i10 = i2;
                                if (i10 < arrayLength) {
                                    output.writeBoolNoTag(Array.getBoolean(array, i10));
                                    i2 = i10 + 1;
                                } else {
                                    return;
                                }
                            }
                        default:
                            switch (i) {
                                case 13:
                                    while (true) {
                                        int i11 = i2;
                                        if (i11 < arrayLength) {
                                            output.writeUInt32NoTag(Array.getInt(array, i11));
                                            i2 = i11 + 1;
                                        } else {
                                            return;
                                        }
                                    }
                                case 14:
                                    while (true) {
                                        int i12 = i2;
                                        if (i12 < arrayLength) {
                                            output.writeEnumNoTag(Array.getInt(array, i12));
                                            i2 = i12 + 1;
                                        } else {
                                            return;
                                        }
                                    }
                                case 15:
                                    while (true) {
                                        int i13 = i2;
                                        if (i13 < arrayLength) {
                                            output.writeSFixed32NoTag(Array.getInt(array, i13));
                                            i2 = i13 + 1;
                                        } else {
                                            return;
                                        }
                                    }
                                case 16:
                                    while (true) {
                                        int i14 = i2;
                                        if (i14 < arrayLength) {
                                            output.writeSFixed64NoTag(Array.getLong(array, i14));
                                            i2 = i14 + 1;
                                        } else {
                                            return;
                                        }
                                    }
                                case 17:
                                    while (true) {
                                        int i15 = i2;
                                        if (i15 < arrayLength) {
                                            output.writeSInt32NoTag(Array.getInt(array, i15));
                                            i2 = i15 + 1;
                                        } else {
                                            return;
                                        }
                                    }
                                case 18:
                                    while (true) {
                                        int i16 = i2;
                                        if (i16 < arrayLength) {
                                            output.writeSInt64NoTag(Array.getLong(array, i16));
                                            i2 = i16 + 1;
                                        } else {
                                            return;
                                        }
                                    }
                                default:
                                    throw new IllegalArgumentException("Unpackable type " + this.type);
                            }
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            } else {
                throw new IllegalArgumentException("Unexpected repeated extension tag " + this.tag + ", unequal to both non-packed variant " + this.nonPackedTag + " and packed variant " + this.packedTag);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:43:0x00a9  */
        /* JADX WARN: Removed duplicated region for block: B:44:0x00ac  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private int computePackedDataSize(java.lang.Object r6) {
            /*
                r5 = this;
                r0 = 0
                int r1 = java.lang.reflect.Array.getLength(r6)
                int r2 = r5.type
                r3 = 0
                switch(r2) {
                    case 1: goto Lac;
                    case 2: goto La9;
                    case 3: goto L97;
                    case 4: goto L85;
                    case 5: goto L73;
                    case 6: goto Lac;
                    case 7: goto La9;
                    case 8: goto L71;
                    default: goto Lb;
                }
            Lb:
                switch(r2) {
                    case 13: goto L5f;
                    case 14: goto L4d;
                    case 15: goto La9;
                    case 16: goto Lac;
                    case 17: goto L3a;
                    case 18: goto L27;
                    default: goto Le;
                }
            Le:
                java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = "Unexpected non-packable type "
                r3.append(r4)
                int r4 = r5.type
                r3.append(r4)
                java.lang.String r3 = r3.toString()
                r2.<init>(r3)
                throw r2
            L27:
            L28:
                r2 = r3
                if (r2 >= r1) goto L38
            L2c:
                long r3 = java.lang.reflect.Array.getLong(r6, r2)
                int r3 = com.android.framework.protobuf.nano.CodedOutputByteBufferNano.computeSInt64SizeNoTag(r3)
                int r0 = r0 + r3
                int r3 = r2 + 1
                goto L28
            L38:
                goto Laf
            L3a:
            L3b:
                r2 = r3
                if (r2 >= r1) goto L4b
            L3f:
                int r3 = java.lang.reflect.Array.getInt(r6, r2)
                int r3 = com.android.framework.protobuf.nano.CodedOutputByteBufferNano.computeSInt32SizeNoTag(r3)
                int r0 = r0 + r3
                int r3 = r2 + 1
                goto L3b
            L4b:
                goto Laf
            L4d:
            L4e:
                r2 = r3
                if (r2 >= r1) goto L5e
            L52:
                int r3 = java.lang.reflect.Array.getInt(r6, r2)
                int r3 = com.android.framework.protobuf.nano.CodedOutputByteBufferNano.computeEnumSizeNoTag(r3)
                int r0 = r0 + r3
                int r3 = r2 + 1
                goto L4e
            L5e:
                goto Laf
            L5f:
            L60:
                r2 = r3
                if (r2 >= r1) goto L70
            L64:
                int r3 = java.lang.reflect.Array.getInt(r6, r2)
                int r3 = com.android.framework.protobuf.nano.CodedOutputByteBufferNano.computeUInt32SizeNoTag(r3)
                int r0 = r0 + r3
                int r3 = r2 + 1
                goto L60
            L70:
                goto Laf
            L71:
                r0 = r1
                goto Laf
            L73:
            L74:
                r2 = r3
                if (r2 >= r1) goto L84
            L78:
                int r3 = java.lang.reflect.Array.getInt(r6, r2)
                int r3 = com.android.framework.protobuf.nano.CodedOutputByteBufferNano.computeInt32SizeNoTag(r3)
                int r0 = r0 + r3
                int r3 = r2 + 1
                goto L74
            L84:
                goto Laf
            L85:
            L86:
                r2 = r3
                if (r2 >= r1) goto L96
            L8a:
                long r3 = java.lang.reflect.Array.getLong(r6, r2)
                int r3 = com.android.framework.protobuf.nano.CodedOutputByteBufferNano.computeUInt64SizeNoTag(r3)
                int r0 = r0 + r3
                int r3 = r2 + 1
                goto L86
            L96:
                goto Laf
            L97:
            L98:
                r2 = r3
                if (r2 >= r1) goto La8
            L9c:
                long r3 = java.lang.reflect.Array.getLong(r6, r2)
                int r3 = com.android.framework.protobuf.nano.CodedOutputByteBufferNano.computeInt64SizeNoTag(r3)
                int r0 = r0 + r3
                int r3 = r2 + 1
                goto L98
            La8:
                goto Laf
            La9:
                int r0 = r1 * 4
                goto Laf
            Lac:
                int r0 = r1 * 8
            Laf:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.framework.protobuf.nano.Extension.PrimitiveExtension.computePackedDataSize(java.lang.Object):int");
        }

        @Override // com.android.framework.protobuf.nano.Extension
        protected int computeRepeatedSerializedSize(Object array) {
            if (this.tag == this.nonPackedTag) {
                return super.computeRepeatedSerializedSize(array);
            }
            if (this.tag == this.packedTag) {
                int dataSize = computePackedDataSize(array);
                int payloadSize = CodedOutputByteBufferNano.computeRawVarint32Size(dataSize) + dataSize;
                return CodedOutputByteBufferNano.computeRawVarint32Size(this.tag) + payloadSize;
            }
            throw new IllegalArgumentException("Unexpected repeated extension tag " + this.tag + ", unequal to both non-packed variant " + this.nonPackedTag + " and packed variant " + this.packedTag);
        }

        @Override // com.android.framework.protobuf.nano.Extension
        protected final int computeSingularSerializedSize(Object value) {
            int fieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
            switch (this.type) {
                case 1:
                    Double doubleValue = (Double) value;
                    return CodedOutputByteBufferNano.computeDoubleSize(fieldNumber, doubleValue.doubleValue());
                case 2:
                    Float floatValue = (Float) value;
                    return CodedOutputByteBufferNano.computeFloatSize(fieldNumber, floatValue.floatValue());
                case 3:
                    Long int64Value = (Long) value;
                    return CodedOutputByteBufferNano.computeInt64Size(fieldNumber, int64Value.longValue());
                case 4:
                    Long uint64Value = (Long) value;
                    return CodedOutputByteBufferNano.computeUInt64Size(fieldNumber, uint64Value.longValue());
                case 5:
                    Integer int32Value = (Integer) value;
                    return CodedOutputByteBufferNano.computeInt32Size(fieldNumber, int32Value.intValue());
                case 6:
                    Long fixed64Value = (Long) value;
                    return CodedOutputByteBufferNano.computeFixed64Size(fieldNumber, fixed64Value.longValue());
                case 7:
                    Integer fixed32Value = (Integer) value;
                    return CodedOutputByteBufferNano.computeFixed32Size(fieldNumber, fixed32Value.intValue());
                case 8:
                    Boolean boolValue = (Boolean) value;
                    return CodedOutputByteBufferNano.computeBoolSize(fieldNumber, boolValue.booleanValue());
                case 9:
                    String stringValue = (String) value;
                    return CodedOutputByteBufferNano.computeStringSize(fieldNumber, stringValue);
                case 10:
                case 11:
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
                case 12:
                    byte[] bytesValue = (byte[]) value;
                    return CodedOutputByteBufferNano.computeBytesSize(fieldNumber, bytesValue);
                case 13:
                    Integer uint32Value = (Integer) value;
                    return CodedOutputByteBufferNano.computeUInt32Size(fieldNumber, uint32Value.intValue());
                case 14:
                    Integer enumValue = (Integer) value;
                    return CodedOutputByteBufferNano.computeEnumSize(fieldNumber, enumValue.intValue());
                case 15:
                    Integer sfixed32Value = (Integer) value;
                    return CodedOutputByteBufferNano.computeSFixed32Size(fieldNumber, sfixed32Value.intValue());
                case 16:
                    Long sfixed64Value = (Long) value;
                    return CodedOutputByteBufferNano.computeSFixed64Size(fieldNumber, sfixed64Value.longValue());
                case 17:
                    Integer sint32Value = (Integer) value;
                    return CodedOutputByteBufferNano.computeSInt32Size(fieldNumber, sint32Value.intValue());
                case 18:
                    Long sint64Value = (Long) value;
                    return CodedOutputByteBufferNano.computeSInt64Size(fieldNumber, sint64Value.longValue());
            }
        }
    }
}
