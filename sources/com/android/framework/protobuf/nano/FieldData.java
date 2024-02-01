package com.android.framework.protobuf.nano;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class FieldData implements Cloneable {
    private Extension<?, ?> cachedExtension;
    private List<UnknownFieldData> unknownFieldData;
    private Object value;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public <T> FieldData(Extension<?, T> extension, T newValue) {
        this.cachedExtension = extension;
        this.value = newValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldData() {
        this.unknownFieldData = new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addUnknownField(UnknownFieldData unknownField) {
        this.unknownFieldData.add(unknownField);
    }

    UnknownFieldData getUnknownField(int index) {
        List<UnknownFieldData> list = this.unknownFieldData;
        if (list != null && index < list.size()) {
            return this.unknownFieldData.get(index);
        }
        return null;
    }

    int getUnknownFieldSize() {
        List<UnknownFieldData> list = this.unknownFieldData;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getValue(Extension<?, T> extension) {
        if (this.value != null) {
            if (this.cachedExtension != extension) {
                throw new IllegalStateException("Tried to getExtension with a differernt Extension.");
            }
        } else {
            this.cachedExtension = extension;
            this.value = extension.getValueFrom(this.unknownFieldData);
            this.unknownFieldData = null;
        }
        return (T) this.value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public <T> void setValue(Extension<?, T> extension, T newValue) {
        this.cachedExtension = extension;
        this.value = newValue;
        this.unknownFieldData = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int computeSerializedSize() {
        int size = 0;
        Object obj = this.value;
        if (obj != null) {
            int size2 = this.cachedExtension.computeSerializedSize(obj);
            return size2;
        }
        for (UnknownFieldData unknownField : this.unknownFieldData) {
            size += unknownField.computeSerializedSize();
        }
        return size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeTo(CodedOutputByteBufferNano output) throws IOException {
        Object obj = this.value;
        if (obj != null) {
            this.cachedExtension.writeTo(obj, output);
            return;
        }
        for (UnknownFieldData unknownField : this.unknownFieldData) {
            unknownField.writeTo(output);
        }
    }

    public boolean equals(Object o) {
        List<UnknownFieldData> list;
        if (o == this) {
            return true;
        }
        if (o instanceof FieldData) {
            FieldData other = (FieldData) o;
            if (this.value != null && other.value != null) {
                Extension<?, ?> extension = this.cachedExtension;
                if (extension != other.cachedExtension) {
                    return false;
                }
                if (!extension.clazz.isArray()) {
                    return this.value.equals(other.value);
                }
                Object obj = this.value;
                if (obj instanceof byte[]) {
                    return Arrays.equals((byte[]) obj, (byte[]) other.value);
                }
                if (obj instanceof int[]) {
                    return Arrays.equals((int[]) obj, (int[]) other.value);
                }
                if (obj instanceof long[]) {
                    return Arrays.equals((long[]) obj, (long[]) other.value);
                }
                if (obj instanceof float[]) {
                    return Arrays.equals((float[]) obj, (float[]) other.value);
                }
                if (obj instanceof double[]) {
                    return Arrays.equals((double[]) obj, (double[]) other.value);
                }
                if (obj instanceof boolean[]) {
                    return Arrays.equals((boolean[]) obj, (boolean[]) other.value);
                }
                return Arrays.deepEquals((Object[]) obj, (Object[]) other.value);
            }
            List<UnknownFieldData> list2 = this.unknownFieldData;
            if (list2 != null && (list = other.unknownFieldData) != null) {
                return list2.equals(list);
            }
            try {
                return Arrays.equals(toByteArray(), other.toByteArray());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    public int hashCode() {
        try {
            int result = (17 * 31) + Arrays.hashCode(toByteArray());
            return result;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] toByteArray() throws IOException {
        byte[] result = new byte[computeSerializedSize()];
        CodedOutputByteBufferNano output = CodedOutputByteBufferNano.newInstance(result);
        writeTo(output);
        return result;
    }

    /* renamed from: clone */
    public final FieldData m51clone() {
        FieldData clone = new FieldData();
        try {
            clone.cachedExtension = this.cachedExtension;
            if (this.unknownFieldData == null) {
                clone.unknownFieldData = null;
            } else {
                clone.unknownFieldData.addAll(this.unknownFieldData);
            }
            if (this.value != null) {
                if (this.value instanceof MessageNano) {
                    clone.value = ((MessageNano) this.value).mo49clone();
                } else if (this.value instanceof byte[]) {
                    clone.value = ((byte[]) this.value).clone();
                } else {
                    int i = 0;
                    if (this.value instanceof byte[][]) {
                        byte[][] valueArray = (byte[][]) this.value;
                        byte[][] cloneArray = new byte[valueArray.length];
                        clone.value = cloneArray;
                        while (i < valueArray.length) {
                            cloneArray[i] = (byte[]) valueArray[i].clone();
                            i++;
                        }
                    } else if (this.value instanceof boolean[]) {
                        clone.value = ((boolean[]) this.value).clone();
                    } else if (this.value instanceof int[]) {
                        clone.value = ((int[]) this.value).clone();
                    } else if (this.value instanceof long[]) {
                        clone.value = ((long[]) this.value).clone();
                    } else if (this.value instanceof float[]) {
                        clone.value = ((float[]) this.value).clone();
                    } else if (this.value instanceof double[]) {
                        clone.value = ((double[]) this.value).clone();
                    } else if (this.value instanceof MessageNano[]) {
                        MessageNano[] valueArray2 = (MessageNano[]) this.value;
                        MessageNano[] cloneArray2 = new MessageNano[valueArray2.length];
                        clone.value = cloneArray2;
                        while (i < valueArray2.length) {
                            cloneArray2[i] = valueArray2[i].mo49clone();
                            i++;
                        }
                    }
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
