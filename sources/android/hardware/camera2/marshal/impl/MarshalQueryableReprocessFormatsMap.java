package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.ReprocessFormatsMap;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
/* loaded from: classes.dex */
public class MarshalQueryableReprocessFormatsMap implements MarshalQueryable<ReprocessFormatsMap> {

    /* loaded from: classes.dex */
    private class MarshalerReprocessFormatsMap extends Marshaler<ReprocessFormatsMap> {
        protected MarshalerReprocessFormatsMap(TypeReference<ReprocessFormatsMap> typeReference, int nativeType) {
            super(MarshalQueryableReprocessFormatsMap.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(ReprocessFormatsMap value, ByteBuffer buffer) {
            int[] inputs = StreamConfigurationMap.imageFormatToInternal(value.getInputs());
            for (int input : inputs) {
                buffer.putInt(input);
                int[] outputs = StreamConfigurationMap.imageFormatToInternal(value.getOutputs(input));
                buffer.putInt(outputs.length);
                for (int output : outputs) {
                    buffer.putInt(output);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized ReprocessFormatsMap m35unmarshal(ByteBuffer buffer) {
            int len = buffer.remaining() / 4;
            if (buffer.remaining() % 4 != 0) {
                throw new AssertionError("ReprocessFormatsMap was not TYPE_INT32");
            }
            int[] entries = new int[len];
            IntBuffer intBuffer = buffer.asIntBuffer();
            intBuffer.get(entries);
            return new ReprocessFormatsMap(entries);
        }

        private protected synchronized int getNativeSize() {
            return NATIVE_SIZE_DYNAMIC;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized int calculateMarshalSize(ReprocessFormatsMap value) {
            int length = 0;
            int[] inputs = value.getInputs();
            for (int input : inputs) {
                int[] outputs = value.getOutputs(input);
                length = length + 1 + 1 + outputs.length;
            }
            return length * 4;
        }
    }

    private protected synchronized Marshaler<ReprocessFormatsMap> createMarshaler(TypeReference<ReprocessFormatsMap> managedType, int nativeType) {
        return new MarshalerReprocessFormatsMap(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<ReprocessFormatsMap> managedType, int nativeType) {
        return nativeType == 1 && managedType.getType().equals(ReprocessFormatsMap.class);
    }
}
