package com.xpeng.security.keystore;

import android.security.KeyStoreException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public interface KeyStoreCryptoOperationStreamer {
    byte[] doFinal(byte[] bArr, int i, int i2, byte[] bArr2, byte[] bArr3) throws KeyStoreException;

    long getConsumedInputSizeBytes();

    long getProducedOutputSizeBytes();

    byte[] update(byte[] bArr, int i, int i2) throws KeyStoreException;
}
