package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.MarshalRegistry;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class MarshalQueryablePair<T1, T2> implements MarshalQueryable<Pair<T1, T2>> {

    /* loaded from: classes.dex */
    private class MarshalerPair extends Marshaler<Pair<T1, T2>> {
        public protected final Class<? super Pair<T1, T2>> mClass;
        public protected final Constructor<Pair<T1, T2>> mConstructor;
        public protected final Marshaler<T1> mNestedTypeMarshalerFirst;
        public protected final Marshaler<T2> mNestedTypeMarshalerSecond;

        public /* bridge */ /* synthetic */ int calculateMarshalSize(Object obj) {
            return calculateMarshalSize((Pair) ((Pair) obj));
        }

        public /* bridge */ /* synthetic */ void marshal(Object obj, ByteBuffer byteBuffer) {
            marshal((Pair) ((Pair) obj), byteBuffer);
        }

        protected MarshalerPair(TypeReference<Pair<T1, T2>> typeReference, int nativeType) {
            super(MarshalQueryablePair.this, typeReference, nativeType);
            this.mClass = typeReference.getRawType();
            try {
                ParameterizedType paramType = (ParameterizedType) typeReference.getType();
                Type actualTypeArgument = paramType.getActualTypeArguments()[0];
                TypeReference<?> actualTypeArgToken = TypeReference.createSpecializedTypeReference(actualTypeArgument);
                this.mNestedTypeMarshalerFirst = MarshalRegistry.getMarshaler(actualTypeArgToken, this.mNativeType);
                Type actualTypeArgument2 = paramType.getActualTypeArguments()[1];
                TypeReference<?> actualTypeArgToken2 = TypeReference.createSpecializedTypeReference(actualTypeArgument2);
                this.mNestedTypeMarshalerSecond = MarshalRegistry.getMarshaler(actualTypeArgToken2, this.mNativeType);
                try {
                    this.mConstructor = (Constructor<? super Pair<T1, T2>>) this.mClass.getConstructor(Object.class, Object.class);
                } catch (NoSuchMethodException e) {
                    throw new AssertionError(e);
                }
            } catch (ClassCastException e2) {
                throw new AssertionError("Raw use of Pair is not supported", e2);
            }
        }

        private protected synchronized void marshal(Pair<T1, T2> value, ByteBuffer buffer) {
            if (value.first == null) {
                throw new UnsupportedOperationException("Pair#first must not be null");
            }
            if (value.second == null) {
                throw new UnsupportedOperationException("Pair#second must not be null");
            }
            this.mNestedTypeMarshalerFirst.marshal(value.first, buffer);
            this.mNestedTypeMarshalerSecond.marshal(value.second, buffer);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: unmarshal */
        public synchronized Pair<T1, T2> m31unmarshal(ByteBuffer buffer) {
            T1 first = this.mNestedTypeMarshalerFirst.unmarshal(buffer);
            T2 second = this.mNestedTypeMarshalerSecond.unmarshal(buffer);
            try {
                return this.mConstructor.newInstance(first, second);
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            } catch (IllegalArgumentException e2) {
                throw new AssertionError(e2);
            } catch (InstantiationException e3) {
                throw new AssertionError(e3);
            } catch (InvocationTargetException e4) {
                throw new AssertionError(e4);
            }
        }

        private protected synchronized int getNativeSize() {
            int firstSize = this.mNestedTypeMarshalerFirst.getNativeSize();
            int secondSize = this.mNestedTypeMarshalerSecond.getNativeSize();
            if (firstSize != NATIVE_SIZE_DYNAMIC && secondSize != NATIVE_SIZE_DYNAMIC) {
                return firstSize + secondSize;
            }
            return NATIVE_SIZE_DYNAMIC;
        }

        private protected synchronized int calculateMarshalSize(Pair<T1, T2> value) {
            int nativeSize = getNativeSize();
            if (nativeSize != NATIVE_SIZE_DYNAMIC) {
                return nativeSize;
            }
            int firstSize = this.mNestedTypeMarshalerFirst.calculateMarshalSize(value.first);
            int secondSize = this.mNestedTypeMarshalerSecond.calculateMarshalSize(value.second);
            return firstSize + secondSize;
        }
    }

    private protected synchronized Marshaler<Pair<T1, T2>> createMarshaler(TypeReference<Pair<T1, T2>> managedType, int nativeType) {
        return new MarshalerPair(managedType, nativeType);
    }

    private protected synchronized boolean isTypeMappingSupported(TypeReference<Pair<T1, T2>> managedType, int nativeType) {
        return Pair.class.equals(managedType.getRawType());
    }
}
