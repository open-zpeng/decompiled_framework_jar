package android.security.keystore;

import android.security.KeyStoreException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public interface KeyStoreCryptoOperationStreamer {
    synchronized byte[] doFinal(byte[] bArr, int i, int i2, byte[] bArr2, byte[] bArr3) throws KeyStoreException;

    synchronized long getConsumedInputSizeBytes();

    synchronized long getProducedOutputSizeBytes();

    synchronized byte[] update(byte[] bArr, int i, int i2) throws KeyStoreException;
}
