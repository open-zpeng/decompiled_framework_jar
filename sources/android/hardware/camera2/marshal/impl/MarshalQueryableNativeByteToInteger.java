package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class MarshalQueryableNativeByteToInteger implements MarshalQueryable<Integer> {
    public protected static final int UINT8_MASK = 255;

    /* loaded from: classes.dex */
    private class MarshalerNativeByteToInteger extends Marshaler<Integer> {
        protected MarshalerNativeByteToInteger(TypeReference<Integer> typeReference, int nativeType) {
            super(MarshalQueryableNativeByteToInteger.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(Integer value, ByteBuffer buffer) {
            buffer.put((byte) value.intValue());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized Integer m30unmarshal(ByteBuffer buffer) {
            return Integer.valueOf(buffer.get() & 255);
        }

        private protected synchronized int getNativeSize() {
            return 1;
        }
    }

    private protected synchronized Marshaler<Integer> createMarshaler(TypeReference<Integer> managedType, int nativeType) {
        return new MarshalerNativeByteToInteger(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<Integer> managedType, int nativeType) {
        return (Integer.class.equals(managedType.getType()) || Integer.TYPE.equals(managedType.getType())) && nativeType == 0;
    }
}
