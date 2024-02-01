package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.MarshalRegistry;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.Log;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class MarshalQueryableArray<T> implements MarshalQueryable<T> {
    public protected static final boolean DEBUG = false;
    public protected static final String TAG = MarshalQueryableArray.class.getSimpleName();

    /* loaded from: classes.dex */
    private class MarshalerArray extends Marshaler<T> {
        public protected final Class<T> mClass;
        public protected final Class<?> mComponentClass;
        public protected final Marshaler<?> mComponentMarshaler;

        protected MarshalerArray(TypeReference<T> typeReference, int nativeType) {
            super(MarshalQueryableArray.this, typeReference, nativeType);
            this.mClass = (Class<? super T>) typeReference.getRawType();
            TypeReference<?> componentToken = typeReference.getComponentType();
            this.mComponentMarshaler = MarshalRegistry.getMarshaler(componentToken, this.mNativeType);
            this.mComponentClass = componentToken.getRawType();
        }

        private protected synchronized void marshal(T value, ByteBuffer buffer) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                marshalArrayElement(this.mComponentMarshaler, buffer, value, i);
            }
        }

        private protected synchronized T unmarshal(ByteBuffer buffer) {
            Object array;
            int elementSize = this.mComponentMarshaler.getNativeSize();
            if (elementSize != Marshaler.NATIVE_SIZE_DYNAMIC) {
                int remaining = buffer.remaining();
                int arraySize = remaining / elementSize;
                if (remaining % elementSize != 0) {
                    throw new UnsupportedOperationException("Arrays for " + this.mTypeReference + " must be packed tighly into a multiple of " + elementSize + "; but there are " + (remaining % elementSize) + " left over bytes");
                }
                array = Array.newInstance(this.mComponentClass, arraySize);
                for (int i = 0; i < arraySize; i++) {
                    Object elem = this.mComponentMarshaler.unmarshal(buffer);
                    Array.set(array, i, elem);
                }
            } else {
                ArrayList<Object> arrayList = new ArrayList<>();
                while (buffer.hasRemaining()) {
                    Object elem2 = this.mComponentMarshaler.unmarshal(buffer);
                    arrayList.add(elem2);
                }
                array = copyListToArray(arrayList, Array.newInstance(this.mComponentClass, arrayList.size()));
            }
            Object array2 = array;
            if (buffer.remaining() != 0) {
                String str = MarshalQueryableArray.TAG;
                Log.e(str, "Trailing bytes (" + buffer.remaining() + ") left over after unpacking " + this.mClass);
            }
            return this.mClass.cast(array2);
        }

        private protected synchronized int getNativeSize() {
            return NATIVE_SIZE_DYNAMIC;
        }

        private protected synchronized int calculateMarshalSize(T value) {
            int elementSize = this.mComponentMarshaler.getNativeSize();
            int arrayLength = Array.getLength(value);
            if (elementSize != Marshaler.NATIVE_SIZE_DYNAMIC) {
                return elementSize * arrayLength;
            }
            int size = 0;
            for (int i = 0; i < arrayLength; i++) {
                size += calculateElementMarshalSize(this.mComponentMarshaler, value, i);
            }
            return size;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public protected synchronized <TElem> void marshalArrayElement(Marshaler<TElem> marshaler, ByteBuffer buffer, Object array, int index) {
            marshaler.marshal(Array.get(array, index), buffer);
        }

        public protected synchronized Object copyListToArray(ArrayList<?> arrayList, Object arrayDest) {
            return arrayList.toArray((Object[]) arrayDest);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public protected synchronized <TElem> int calculateElementMarshalSize(Marshaler<TElem> marshaler, Object array, int index) {
            Object elem = Array.get(array, index);
            return marshaler.calculateMarshalSize(elem);
        }
    }

    private protected synchronized Marshaler<T> createMarshaler(TypeReference<T> managedType, int nativeType) {
        return new MarshalerArray(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<T> managedType, int nativeType) {
        return managedType.getRawType().isArray();
    }
}
