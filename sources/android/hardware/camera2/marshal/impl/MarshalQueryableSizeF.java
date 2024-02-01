package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.SizeF;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class MarshalQueryableSizeF implements MarshalQueryable<SizeF> {
    public protected static final int SIZE = 8;

    /* loaded from: classes.dex */
    private class MarshalerSizeF extends Marshaler<SizeF> {
        protected MarshalerSizeF(TypeReference<SizeF> typeReference, int nativeType) {
            super(MarshalQueryableSizeF.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(SizeF value, ByteBuffer buffer) {
            buffer.putFloat(value.getWidth());
            buffer.putFloat(value.getHeight());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized SizeF m38unmarshal(ByteBuffer buffer) {
            float width = buffer.getFloat();
            float height = buffer.getFloat();
            return new SizeF(width, height);
        }

        private protected synchronized int getNativeSize() {
            return 8;
        }
    }

    private protected synchronized Marshaler<SizeF> createMarshaler(TypeReference<SizeF> managedType, int nativeType) {
        return new MarshalerSizeF(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<SizeF> managedType, int nativeType) {
        return nativeType == 2 && SizeF.class.equals(managedType.getType());
    }
}
