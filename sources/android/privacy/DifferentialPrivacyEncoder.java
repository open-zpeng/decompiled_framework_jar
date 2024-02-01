package android.privacy;
/* loaded from: classes2.dex */
public interface DifferentialPrivacyEncoder {
    private protected synchronized byte[] encodeBits(byte[] bArr);

    private protected synchronized byte[] encodeBoolean(boolean z);

    private protected synchronized byte[] encodeString(String str);

    private protected synchronized DifferentialPrivacyConfig getConfig();

    private protected synchronized boolean isInsecureEncoderForTest();
}
