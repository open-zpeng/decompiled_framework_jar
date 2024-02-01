package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.HighSpeedVideoConfiguration;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class MarshalQueryableHighSpeedVideoConfiguration implements MarshalQueryable<HighSpeedVideoConfiguration> {
    public protected static final int SIZE = 20;

    /* loaded from: classes.dex */
    private class MarshalerHighSpeedVideoConfiguration extends Marshaler<HighSpeedVideoConfiguration> {
        protected MarshalerHighSpeedVideoConfiguration(TypeReference<HighSpeedVideoConfiguration> typeReference, int nativeType) {
            super(MarshalQueryableHighSpeedVideoConfiguration.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(HighSpeedVideoConfiguration value, ByteBuffer buffer) {
            buffer.putInt(value.getWidth());
            buffer.putInt(value.getHeight());
            buffer.putInt(value.getFpsMin());
            buffer.putInt(value.getFpsMax());
            buffer.putInt(value.getBatchSizeMax());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized HighSpeedVideoConfiguration m28unmarshal(ByteBuffer buffer) {
            int width = buffer.getInt();
            int height = buffer.getInt();
            int fpsMin = buffer.getInt();
            int fpsMax = buffer.getInt();
            int batchSizeMax = buffer.getInt();
            return new HighSpeedVideoConfiguration(width, height, fpsMin, fpsMax, batchSizeMax);
        }

        private protected synchronized int getNativeSize() {
            return 20;
        }
    }

    private protected synchronized Marshaler<HighSpeedVideoConfiguration> createMarshaler(TypeReference<HighSpeedVideoConfiguration> managedType, int nativeType) {
        return new MarshalerHighSpeedVideoConfiguration(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<HighSpeedVideoConfiguration> managedType, int nativeType) {
        return nativeType == 1 && managedType.getType().equals(HighSpeedVideoConfiguration.class);
    }
}
