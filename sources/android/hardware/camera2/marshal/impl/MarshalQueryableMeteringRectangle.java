package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class MarshalQueryableMeteringRectangle implements MarshalQueryable<MeteringRectangle> {
    public protected static final int SIZE = 20;

    /* loaded from: classes.dex */
    private class MarshalerMeteringRectangle extends Marshaler<MeteringRectangle> {
        protected MarshalerMeteringRectangle(TypeReference<MeteringRectangle> typeReference, int nativeType) {
            super(MarshalQueryableMeteringRectangle.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(MeteringRectangle value, ByteBuffer buffer) {
            int xMin = value.getX();
            int yMin = value.getY();
            int xMax = value.getWidth() + xMin;
            int yMax = value.getHeight() + yMin;
            int weight = value.getMeteringWeight();
            buffer.putInt(xMin);
            buffer.putInt(yMin);
            buffer.putInt(xMax);
            buffer.putInt(yMax);
            buffer.putInt(weight);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized MeteringRectangle m29unmarshal(ByteBuffer buffer) {
            int xMin = buffer.getInt();
            int yMin = buffer.getInt();
            int xMax = buffer.getInt();
            int yMax = buffer.getInt();
            int weight = buffer.getInt();
            int width = xMax - xMin;
            int height = yMax - yMin;
            return new MeteringRectangle(xMin, yMin, width, height, weight);
        }

        private protected synchronized int getNativeSize() {
            return 20;
        }
    }

    private protected synchronized Marshaler<MeteringRectangle> createMarshaler(TypeReference<MeteringRectangle> managedType, int nativeType) {
        return new MarshalerMeteringRectangle(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<MeteringRectangle> managedType, int nativeType) {
        return nativeType == 1 && MeteringRectangle.class.equals(managedType.getType());
    }
}
