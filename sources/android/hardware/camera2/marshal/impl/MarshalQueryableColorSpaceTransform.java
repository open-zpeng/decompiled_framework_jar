package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class MarshalQueryableColorSpaceTransform implements MarshalQueryable<ColorSpaceTransform> {
    public protected static final int ELEMENTS_INT32 = 18;
    public protected static final int SIZE = 72;

    /* loaded from: classes.dex */
    private class MarshalerColorSpaceTransform extends Marshaler<ColorSpaceTransform> {
        protected MarshalerColorSpaceTransform(TypeReference<ColorSpaceTransform> typeReference, int nativeType) {
            super(MarshalQueryableColorSpaceTransform.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(ColorSpaceTransform value, ByteBuffer buffer) {
            int[] transformAsArray = new int[18];
            value.copyElements(transformAsArray, 0);
            for (int i = 0; i < 18; i++) {
                buffer.putInt(transformAsArray[i]);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized ColorSpaceTransform m26unmarshal(ByteBuffer buffer) {
            int[] transformAsArray = new int[18];
            for (int i = 0; i < 18; i++) {
                transformAsArray[i] = buffer.getInt();
            }
            return new ColorSpaceTransform(transformAsArray);
        }

        private protected synchronized int getNativeSize() {
            return 72;
        }
    }

    private protected synchronized Marshaler<ColorSpaceTransform> createMarshaler(TypeReference<ColorSpaceTransform> managedType, int nativeType) {
        return new MarshalerColorSpaceTransform(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<ColorSpaceTransform> managedType, int nativeType) {
        return nativeType == 5 && ColorSpaceTransform.class.equals(managedType.getType());
    }
}
