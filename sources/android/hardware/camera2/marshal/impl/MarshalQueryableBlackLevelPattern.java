package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.BlackLevelPattern;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class MarshalQueryableBlackLevelPattern implements MarshalQueryable<BlackLevelPattern> {
    public protected static final int SIZE = 16;

    /* loaded from: classes.dex */
    private class MarshalerBlackLevelPattern extends Marshaler<BlackLevelPattern> {
        protected MarshalerBlackLevelPattern(TypeReference<BlackLevelPattern> typeReference, int nativeType) {
            super(MarshalQueryableBlackLevelPattern.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(BlackLevelPattern value, ByteBuffer buffer) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    buffer.putInt(value.getOffsetForIndex(j, i));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized BlackLevelPattern m24unmarshal(ByteBuffer buffer) {
            int[] channelOffsets = new int[4];
            for (int i = 0; i < 4; i++) {
                channelOffsets[i] = buffer.getInt();
            }
            return new BlackLevelPattern(channelOffsets);
        }

        private protected synchronized int getNativeSize() {
            return 16;
        }
    }

    private protected synchronized Marshaler<BlackLevelPattern> createMarshaler(TypeReference<BlackLevelPattern> managedType, int nativeType) {
        return new MarshalerBlackLevelPattern(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<BlackLevelPattern> managedType, int nativeType) {
        return nativeType == 1 && BlackLevelPattern.class.equals(managedType.getType());
    }
}
