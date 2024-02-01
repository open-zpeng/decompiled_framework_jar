package android.hardware.camera2.marshal;

import android.hardware.camera2.utils.TypeReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class MarshalRegistry {
    public protected static final Object sMarshalLock = new Object();
    public protected static final List<MarshalQueryable<?>> sRegisteredMarshalQueryables = new ArrayList();
    public protected static final HashMap<MarshalToken<?>, Marshaler<?>> sMarshalerMap = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized <T> void registerMarshalQueryable(MarshalQueryable<T> queryable) {
        synchronized (sMarshalLock) {
            sRegisteredMarshalQueryables.add(queryable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized <T> Marshaler<T> getMarshaler(TypeReference<T> typeToken, int nativeType) {
        Marshaler<T> marshaler;
        synchronized (sMarshalLock) {
            MarshalToken<?> marshalToken = new MarshalToken<>(typeToken, nativeType);
            marshaler = (Marshaler<T>) sMarshalerMap.get(marshalToken);
            if (marshaler == null) {
                if (sRegisteredMarshalQueryables.size() == 0) {
                    throw new AssertionError("No available query marshalers registered");
                }
                Iterator<MarshalQueryable<?>> it = sRegisteredMarshalQueryables.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    MarshalQueryable<?> potentialMarshaler = it.next();
                    if (potentialMarshaler.isTypeMappingSupported(typeToken, nativeType)) {
                        marshaler = (Marshaler<T>) potentialMarshaler.createMarshaler(typeToken, nativeType);
                        break;
                    }
                }
                if (marshaler == null) {
                    throw new UnsupportedOperationException("Could not find marshaler that matches the requested combination of type reference " + typeToken + " and native type " + MarshalHelpers.toStringNativeType(nativeType));
                }
                sMarshalerMap.put(marshalToken, marshaler);
            }
        }
        return marshaler;
    }

    /* loaded from: classes.dex */
    private static class MarshalToken<T> {
        public protected final int hash;
        public private protected final int nativeType;
        public private protected final TypeReference<T> typeReference;

        private protected synchronized MarshalToken(TypeReference<T> typeReference, int nativeType) {
            this.typeReference = typeReference;
            this.nativeType = nativeType;
            this.hash = typeReference.hashCode() ^ nativeType;
        }

        public boolean equals(Object other) {
            if (other instanceof MarshalToken) {
                MarshalToken<?> otherToken = (MarshalToken) other;
                return this.typeReference.equals(otherToken.typeReference) && this.nativeType == otherToken.nativeType;
            }
            return false;
        }

        public int hashCode() {
            return this.hash;
        }
    }

    public protected synchronized MarshalRegistry() {
        throw new AssertionError();
    }
}
