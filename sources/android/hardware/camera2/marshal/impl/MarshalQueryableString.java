package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public class MarshalQueryableString implements MarshalQueryable<String> {
    public protected static final boolean DEBUG = false;
    public protected static final byte NUL = 0;
    public protected static final String TAG = MarshalQueryableString.class.getSimpleName();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PreloadHolder {
        private protected static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    }

    /* loaded from: classes.dex */
    private class MarshalerString extends Marshaler<String> {
        protected MarshalerString(TypeReference<String> typeReference, int nativeType) {
            super(MarshalQueryableString.this, typeReference, nativeType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void marshal(String value, ByteBuffer buffer) {
            byte[] arr = value.getBytes(PreloadHolder.UTF8_CHARSET);
            buffer.put(arr);
            buffer.put((byte) 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized int calculateMarshalSize(String value) {
            byte[] arr = value.getBytes(PreloadHolder.UTF8_CHARSET);
            return arr.length + 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized String unmarshal(ByteBuffer buffer) {
            buffer.mark();
            boolean foundNull = false;
            int stringLength = 0;
            while (true) {
                if (!buffer.hasRemaining()) {
                    break;
                } else if (buffer.get() == 0) {
                    foundNull = true;
                    break;
                } else {
                    stringLength++;
                }
            }
            if (!foundNull) {
                throw new UnsupportedOperationException("Strings must be null-terminated");
            }
            buffer.reset();
            byte[] strBytes = new byte[stringLength + 1];
            buffer.get(strBytes, 0, stringLength + 1);
            return new String(strBytes, 0, stringLength, PreloadHolder.UTF8_CHARSET);
        }

        private protected synchronized int getNativeSize() {
            return NATIVE_SIZE_DYNAMIC;
        }
    }

    private protected synchronized Marshaler<String> createMarshaler(TypeReference<String> managedType, int nativeType) {
        return new MarshalerString(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<String> managedType, int nativeType) {
        return nativeType == 0 && String.class.equals(managedType.getType());
    }
}
