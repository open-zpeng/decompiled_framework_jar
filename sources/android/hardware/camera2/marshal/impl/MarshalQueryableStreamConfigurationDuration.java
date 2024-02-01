package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.StreamConfigurationDuration;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class MarshalQueryableStreamConfigurationDuration implements MarshalQueryable<StreamConfigurationDuration> {
    public protected static final long MASK_UNSIGNED_INT = 4294967295L;
    public protected static final int SIZE = 32;

    /* loaded from: classes.dex */
    private class MarshalerStreamConfigurationDuration extends Marshaler<StreamConfigurationDuration> {
        protected MarshalerStreamConfigurationDuration(TypeReference<StreamConfigurationDuration> typeReference, int nativeType) {
            super(MarshalQueryableStreamConfigurationDuration.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(StreamConfigurationDuration value, ByteBuffer buffer) {
            buffer.putLong(value.getFormat() & 4294967295L);
            buffer.putLong(value.getWidth());
            buffer.putLong(value.getHeight());
            buffer.putLong(value.getDuration());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized StreamConfigurationDuration m40unmarshal(ByteBuffer buffer) {
            int format = (int) buffer.getLong();
            int width = (int) buffer.getLong();
            int height = (int) buffer.getLong();
            long durationNs = buffer.getLong();
            return new StreamConfigurationDuration(format, width, height, durationNs);
        }

        private protected synchronized int getNativeSize() {
            return 32;
        }
    }

    private protected synchronized Marshaler<StreamConfigurationDuration> createMarshaler(TypeReference<StreamConfigurationDuration> managedType, int nativeType) {
        return new MarshalerStreamConfigurationDuration(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<StreamConfigurationDuration> managedType, int nativeType) {
        return nativeType == 3 && StreamConfigurationDuration.class.equals(managedType.getType());
    }
}
