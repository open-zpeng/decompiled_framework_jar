package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class MarshalQueryableBoolean implements MarshalQueryable<Boolean> {

    /* loaded from: classes.dex */
    private class MarshalerBoolean extends Marshaler<Boolean> {
        protected MarshalerBoolean(TypeReference<Boolean> typeReference, int nativeType) {
            super(MarshalQueryableBoolean.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(Boolean value, ByteBuffer buffer) {
            buffer.put(value.booleanValue() ? (byte) 1 : (byte) 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized Boolean m25unmarshal(ByteBuffer buffer) {
            return Boolean.valueOf(buffer.get() != 0);
        }

        private protected synchronized int getNativeSize() {
            return 1;
        }
    }

    private protected synchronized Marshaler<Boolean> createMarshaler(TypeReference<Boolean> managedType, int nativeType) {
        return new MarshalerBoolean(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<Boolean> managedType, int nativeType) {
        return (Boolean.class.equals(managedType.getType()) || Boolean.TYPE.equals(managedType.getType())) && nativeType == 0;
    }
}
