package android.hardware.camera2.marshal;

import android.hardware.camera2.utils.TypeReference;
/* loaded from: classes.dex */
public interface MarshalQueryable<T> {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized Marshaler<T> createMarshaler(TypeReference<T> typeReference, int i);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isTypeMappingSupported(TypeReference<T> typeReference, int i);
}
