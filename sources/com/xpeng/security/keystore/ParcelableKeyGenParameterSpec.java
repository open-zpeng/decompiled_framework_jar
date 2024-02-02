package com.xpeng.security.keystore;

import android.os.Parcel;
import android.os.Parcelable;
import android.security.keystore.KeyGenParameterSpec;
import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
/* loaded from: classes3.dex */
public final class ParcelableKeyGenParameterSpec implements Parcelable {
    private static final int ALGORITHM_PARAMETER_SPEC_EC = 3;
    private static final int ALGORITHM_PARAMETER_SPEC_NONE = 1;
    private static final int ALGORITHM_PARAMETER_SPEC_RSA = 2;
    public static final Parcelable.Creator<ParcelableKeyGenParameterSpec> CREATOR = new Parcelable.Creator<ParcelableKeyGenParameterSpec>() { // from class: com.xpeng.security.keystore.ParcelableKeyGenParameterSpec.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableKeyGenParameterSpec createFromParcel(Parcel in) {
            return new ParcelableKeyGenParameterSpec(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableKeyGenParameterSpec[] newArray(int size) {
            return new ParcelableKeyGenParameterSpec[size];
        }
    };
    private final KeyGenParameterSpec mSpec;

    public ParcelableKeyGenParameterSpec(KeyGenParameterSpec spec) {
        this.mSpec = spec;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private static void writeOptionalDate(Parcel out, Date date) {
        if (date != null) {
            out.writeBoolean(true);
            out.writeLong(date.getTime());
            return;
        }
        out.writeBoolean(false);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mSpec.getKeystoreAlias());
        out.writeInt(this.mSpec.getPurposes());
        out.writeInt(this.mSpec.getUid());
        out.writeInt(this.mSpec.getKeySize());
        AlgorithmParameterSpec algoSpec = this.mSpec.getAlgorithmParameterSpec();
        if (algoSpec == null) {
            out.writeInt(1);
        } else if (algoSpec instanceof RSAKeyGenParameterSpec) {
            RSAKeyGenParameterSpec rsaSpec = (RSAKeyGenParameterSpec) algoSpec;
            out.writeInt(2);
            out.writeInt(rsaSpec.getKeysize());
            out.writeByteArray(rsaSpec.getPublicExponent().toByteArray());
        } else if (algoSpec instanceof ECGenParameterSpec) {
            ECGenParameterSpec ecSpec = (ECGenParameterSpec) algoSpec;
            out.writeInt(3);
            out.writeString(ecSpec.getName());
        } else {
            throw new IllegalArgumentException(String.format("Unknown algorithm parameter spec: %s", algoSpec.getClass()));
        }
        out.writeByteArray(this.mSpec.getCertificateSubject().getEncoded());
        out.writeByteArray(this.mSpec.getCertificateSerialNumber().toByteArray());
        out.writeLong(this.mSpec.getCertificateNotBefore().getTime());
        out.writeLong(this.mSpec.getCertificateNotAfter().getTime());
        writeOptionalDate(out, this.mSpec.getKeyValidityStart());
        writeOptionalDate(out, this.mSpec.getKeyValidityForOriginationEnd());
        writeOptionalDate(out, this.mSpec.getKeyValidityForConsumptionEnd());
        if (this.mSpec.isDigestsSpecified()) {
            out.writeStringArray(this.mSpec.getDigests());
        } else {
            out.writeStringArray(null);
        }
        out.writeStringArray(this.mSpec.getEncryptionPaddings());
        out.writeStringArray(this.mSpec.getSignaturePaddings());
        out.writeStringArray(this.mSpec.getBlockModes());
        out.writeBoolean(this.mSpec.isRandomizedEncryptionRequired());
        out.writeBoolean(this.mSpec.isUserAuthenticationRequired());
        out.writeInt(this.mSpec.getUserAuthenticationValidityDurationSeconds());
        out.writeByteArray(this.mSpec.getAttestationChallenge());
        out.writeBoolean(this.mSpec.isUniqueIdIncluded());
        out.writeBoolean(this.mSpec.isUserAuthenticationValidWhileOnBody());
        out.writeBoolean(this.mSpec.isInvalidatedByBiometricEnrollment());
        out.writeBoolean(this.mSpec.isUserPresenceRequired());
    }

    private static Date readDateOrNull(Parcel in) {
        boolean hasDate = in.readBoolean();
        if (hasDate) {
            return new Date(in.readLong());
        }
        return null;
    }

    private ParcelableKeyGenParameterSpec(Parcel in) {
        AlgorithmParameterSpec algorithmSpec;
        String keystoreAlias = in.readString();
        int purposes = in.readInt();
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keystoreAlias, purposes);
        builder.setUid(in.readInt());
        int keySize = in.readInt();
        if (keySize >= 0) {
            builder.setKeySize(keySize);
        }
        int keySpecType = in.readInt();
        if (keySpecType == 1) {
            algorithmSpec = null;
        } else if (keySpecType == 2) {
            int rsaKeySize = in.readInt();
            BigInteger publicExponent = new BigInteger(in.createByteArray());
            algorithmSpec = new RSAKeyGenParameterSpec(rsaKeySize, publicExponent);
        } else if (keySpecType == 3) {
            String stdName = in.readString();
            algorithmSpec = new ECGenParameterSpec(stdName);
        } else {
            throw new IllegalArgumentException(String.format("Unknown algorithm parameter spec: %d", Integer.valueOf(keySpecType)));
        }
        if (algorithmSpec != null) {
            builder.setAlgorithmParameterSpec(algorithmSpec);
        }
        builder.setCertificateSubject(new X500Principal(in.createByteArray()));
        builder.setCertificateSerialNumber(new BigInteger(in.createByteArray()));
        builder.setCertificateNotBefore(new Date(in.readLong()));
        builder.setCertificateNotAfter(new Date(in.readLong()));
        builder.setKeyValidityStart(readDateOrNull(in));
        builder.setKeyValidityForOriginationEnd(readDateOrNull(in));
        builder.setKeyValidityForConsumptionEnd(readDateOrNull(in));
        String[] digests = in.createStringArray();
        if (digests != null) {
            builder.setDigests(digests);
        }
        builder.setEncryptionPaddings(in.createStringArray());
        builder.setSignaturePaddings(in.createStringArray());
        builder.setBlockModes(in.createStringArray());
        builder.setRandomizedEncryptionRequired(in.readBoolean());
        builder.setUserAuthenticationRequired(in.readBoolean());
        builder.setUserAuthenticationValidityDurationSeconds(in.readInt());
        builder.setAttestationChallenge(in.createByteArray());
        builder.setUniqueIdIncluded(in.readBoolean());
        builder.setUserAuthenticationValidWhileOnBody(in.readBoolean());
        builder.setInvalidatedByBiometricEnrollment(in.readBoolean());
        builder.setUserPresenceRequired(in.readBoolean());
        this.mSpec = builder.build();
    }

    public KeyGenParameterSpec getSpec() {
        return this.mSpec;
    }
}
