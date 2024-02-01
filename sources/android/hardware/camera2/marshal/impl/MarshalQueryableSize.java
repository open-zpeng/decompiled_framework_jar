package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.Size;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class MarshalQueryableSize implements MarshalQueryable<Size> {
    public protected static final int SIZE = 8;

    /* loaded from: classes.dex */
    private class MarshalerSize extends Marshaler<Size> {
        protected MarshalerSize(TypeReference<Size> typeReference, int nativeType) {
            super(MarshalQueryableSize.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(Size value, ByteBuffer buffer) {
            buffer.putInt(value.getWidth());
            buffer.putInt(value.getHeight());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized Size m37unmarshal(ByteBuffer buffer) {
            int width = buffer.getInt();
            int height = buffer.getInt();
            return new Size(width, height);
        }

        private protected synchronized int getNativeSize() {
            return 8;
        }
    }

    private protected synchronized Marshaler<Size> createMarshaler(TypeReference<Size> managedType, int nativeType) {
        return new MarshalerSize(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<Size> managedType, int nativeType) {
        return nativeType == 1 && Size.class.equals(managedType.getType());
    }
}
