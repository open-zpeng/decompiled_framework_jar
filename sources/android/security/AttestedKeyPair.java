package android.security;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public final class AttestedKeyPair {
    private final Certificate[] mAttestationRecord;
    private final KeyPair mKeyPair;

    public AttestedKeyPair(KeyPair keyPair, Certificate[] attestationRecord) {
        this.mKeyPair = keyPair;
        this.mAttestationRecord = attestationRecord;
    }

    public KeyPair getKeyPair() {
        return this.mKeyPair;
    }

    public List<Certificate> getAttestationRecord() {
        Certificate[] certificateArr = this.mAttestationRecord;
        if (certificateArr == null) {
            return new ArrayList();
        }
        return Arrays.asList(certificateArr);
    }
}
