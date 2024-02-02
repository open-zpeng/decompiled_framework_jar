package android.hardware.camera2.marshal;

import android.hardware.camera2.utils.TypeReference;
import com.android.internal.util.Preconditions;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public abstract class Marshaler<T> {
    private protected static int NATIVE_SIZE_DYNAMIC = -1;
    public private final int mNativeType;
    public private final TypeReference<T> mTypeReference;

    /* JADX INFO: Access modifiers changed from: private */
    public abstract synchronized int getNativeSize();

    /* JADX INFO: Access modifiers changed from: private */
    public abstract synchronized void marshal(T t, ByteBuffer byteBuffer);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract synchronized T unmarshal(ByteBuffer byteBuffer);

    public private synchronized Marshaler(MarshalQueryable<T> query, TypeReference<T> typeReference, int nativeType) {
        this.mTypeReference = (TypeReference) Preconditions.checkNotNull(typeReference, "typeReference must not be null");
        this.mNativeType = MarshalHelpers.checkNativeType(nativeType);
        if (!query.isTypeMappingSupported(typeReference, nativeType)) {
            throw new UnsupportedOperationException("Unsupported type marshaling for managed type " + typeReference + " and native type " + MarshalHelpers.toStringNativeType(nativeType));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int calculateMarshalSize(T value) {
        int nativeSize = getNativeSize();
        if (nativeSize == NATIVE_SIZE_DYNAMIC) {
            throw new AssertionError("Override this function for dynamically-sized objects");
        }
        return nativeSize;
    }

    private protected synchronized TypeReference<T> getTypeReference() {
        return this.mTypeReference;
    }

    private protected synchronized int getNativeType() {
        return this.mNativeType;
    }
}
