package android.util.apk;

import android.os.Build;
import android.util.ArrayMap;
import android.util.Pair;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class ApkSignatureSchemeV3Verifier {
    private static final int APK_SIGNATURE_SCHEME_V3_BLOCK_ID = -262969152;
    private static final int PROOF_OF_ROTATION_ATTR_ID = 1000370060;
    public static final int SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID = 3;

    public static boolean hasSignature(String apkFile) throws IOException {
        try {
            RandomAccessFile apk = new RandomAccessFile(apkFile, "r");
            findSignature(apk);
            $closeResource(null, apk);
            return true;
        } catch (SignatureNotFoundException e) {
            return false;
        }
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    public static VerifiedSigner verify(String apkFile) throws SignatureNotFoundException, SecurityException, IOException {
        return verify(apkFile, true);
    }

    public static VerifiedSigner unsafeGetCertsWithoutVerification(String apkFile) throws SignatureNotFoundException, SecurityException, IOException {
        return verify(apkFile, false);
    }

    private static VerifiedSigner verify(String apkFile, boolean verifyIntegrity) throws SignatureNotFoundException, SecurityException, IOException {
        RandomAccessFile apk = new RandomAccessFile(apkFile, "r");
        try {
            VerifiedSigner verify = verify(apk, verifyIntegrity);
            $closeResource(null, apk);
            return verify;
        } finally {
        }
    }

    private static VerifiedSigner verify(RandomAccessFile apk, boolean verifyIntegrity) throws SignatureNotFoundException, SecurityException, IOException {
        SignatureInfo signatureInfo = findSignature(apk);
        return verify(apk, signatureInfo, verifyIntegrity);
    }

    private static SignatureInfo findSignature(RandomAccessFile apk) throws IOException, SignatureNotFoundException {
        return ApkSigningBlockUtils.findSignature(apk, APK_SIGNATURE_SCHEME_V3_BLOCK_ID);
    }

    private static VerifiedSigner verify(RandomAccessFile apk, SignatureInfo signatureInfo, boolean doVerifyIntegrity) throws SecurityException, IOException {
        int signerCount = 0;
        Map<Integer, byte[]> contentDigests = new ArrayMap<>();
        VerifiedSigner result = null;
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            try {
                ByteBuffer signers = ApkSigningBlockUtils.getLengthPrefixedSlice(signatureInfo.signatureBlock);
                while (signers.hasRemaining()) {
                    try {
                        ByteBuffer signer = ApkSigningBlockUtils.getLengthPrefixedSlice(signers);
                        result = verifySigner(signer, contentDigests, certFactory);
                        signerCount++;
                    } catch (PlatformNotSupportedException e) {
                    } catch (IOException | SecurityException | BufferUnderflowException e2) {
                        throw new SecurityException("Failed to parse/verify signer #" + signerCount + " block", e2);
                    }
                }
                if (signerCount < 1 || result == null) {
                    throw new SecurityException("No signers found");
                }
                if (signerCount != 1) {
                    throw new SecurityException("APK Signature Scheme V3 only supports one signer: multiple signers found.");
                }
                if (contentDigests.isEmpty()) {
                    throw new SecurityException("No content digests found");
                }
                if (doVerifyIntegrity) {
                    ApkSigningBlockUtils.verifyIntegrity(contentDigests, apk, signatureInfo);
                }
                if (contentDigests.containsKey(3)) {
                    byte[] verityDigest = contentDigests.get(3);
                    result.verityRootHash = ApkSigningBlockUtils.parseVerityDigestAndVerifySourceLength(verityDigest, apk.length(), signatureInfo);
                }
                return result;
            } catch (IOException e3) {
                throw new SecurityException("Failed to read list of signers", e3);
            }
        } catch (CertificateException e4) {
            throw new RuntimeException("Failed to obtain X.509 CertificateFactory", e4);
        }
    }

    private static VerifiedSigner verifySigner(ByteBuffer signerBlock, Map<Integer, byte[]> contentDigests, CertificateFactory certFactory) throws SecurityException, IOException, PlatformNotSupportedException {
        ByteBuffer signedData = ApkSigningBlockUtils.getLengthPrefixedSlice(signerBlock);
        int minSdkVersion = signerBlock.getInt();
        int maxSdkVersion = signerBlock.getInt();
        if (Build.VERSION.SDK_INT < minSdkVersion || Build.VERSION.SDK_INT > maxSdkVersion) {
            throw new PlatformNotSupportedException("Signer not supported by this platform version. This platform: " + Build.VERSION.SDK_INT + ", signer minSdkVersion: " + minSdkVersion + ", maxSdkVersion: " + maxSdkVersion);
        }
        ByteBuffer signatures = ApkSigningBlockUtils.getLengthPrefixedSlice(signerBlock);
        byte[] publicKeyBytes = ApkSigningBlockUtils.readLengthPrefixedByteArray(signerBlock);
        int bestSigAlgorithm = -1;
        List<Integer> signaturesSigAlgorithms = new ArrayList<>();
        byte[] bestSigAlgorithmSignatureBytes = null;
        int signatureCount = 0;
        while (signatures.hasRemaining()) {
            signatureCount++;
            try {
                ByteBuffer signature = ApkSigningBlockUtils.getLengthPrefixedSlice(signatures);
                if (signature.remaining() < 8) {
                    throw new SecurityException("Signature record too short");
                }
                int sigAlgorithm = signature.getInt();
                signaturesSigAlgorithms.add(Integer.valueOf(sigAlgorithm));
                if (isSupportedSignatureAlgorithm(sigAlgorithm)) {
                    if (bestSigAlgorithm == -1 || ApkSigningBlockUtils.compareSignatureAlgorithm(sigAlgorithm, bestSigAlgorithm) > 0) {
                        bestSigAlgorithm = sigAlgorithm;
                        bestSigAlgorithmSignatureBytes = ApkSigningBlockUtils.readLengthPrefixedByteArray(signature);
                    }
                }
            } catch (IOException | BufferUnderflowException e) {
                throw new SecurityException("Failed to parse signature record #" + signatureCount, e);
            }
        }
        if (bestSigAlgorithm == -1) {
            if (signatureCount == 0) {
                throw new SecurityException("No signatures found");
            }
            throw new SecurityException("No supported signatures found");
        }
        String keyAlgorithm = ApkSigningBlockUtils.getSignatureAlgorithmJcaKeyAlgorithm(bestSigAlgorithm);
        Pair<String, ? extends AlgorithmParameterSpec> signatureAlgorithmParams = ApkSigningBlockUtils.getSignatureAlgorithmJcaSignatureAlgorithm(bestSigAlgorithm);
        String jcaSignatureAlgorithm = signatureAlgorithmParams.first;
        AlgorithmParameterSpec jcaSignatureAlgorithmParams = (AlgorithmParameterSpec) signatureAlgorithmParams.second;
        try {
            PublicKey publicKey = KeyFactory.getInstance(keyAlgorithm).generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            Signature sig = Signature.getInstance(jcaSignatureAlgorithm);
            sig.initVerify(publicKey);
            if (jcaSignatureAlgorithmParams != null) {
                try {
                    sig.setParameter(jcaSignatureAlgorithmParams);
                } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException e2) {
                    e = e2;
                    throw new SecurityException("Failed to verify " + jcaSignatureAlgorithm + " signature", e);
                }
            }
            sig.update(signedData);
            boolean sigVerified = sig.verify(bestSigAlgorithmSignatureBytes);
            if (!sigVerified) {
                throw new SecurityException(jcaSignatureAlgorithm + " signature did not verify");
            }
            signedData.clear();
            ByteBuffer digests = ApkSigningBlockUtils.getLengthPrefixedSlice(signedData);
            List<Integer> digestsSigAlgorithms = new ArrayList<>();
            int digestCount = 0;
            byte[] contentDigest = null;
            while (digests.hasRemaining()) {
                int signatureCount2 = signatureCount;
                int signatureCount3 = digestCount + 1;
                try {
                    ByteBuffer digest = ApkSigningBlockUtils.getLengthPrefixedSlice(digests);
                    byte[] bestSigAlgorithmSignatureBytes2 = bestSigAlgorithmSignatureBytes;
                    try {
                        boolean sigVerified2 = sigVerified;
                        if (digest.remaining() < 8) {
                            throw new IOException("Record too short");
                        }
                        try {
                            int sigAlgorithm2 = digest.getInt();
                            String keyAlgorithm2 = keyAlgorithm;
                            List<Integer> digestsSigAlgorithms2 = digestsSigAlgorithms;
                            try {
                                digestsSigAlgorithms2.add(Integer.valueOf(sigAlgorithm2));
                                if (sigAlgorithm2 == bestSigAlgorithm) {
                                    contentDigest = ApkSigningBlockUtils.readLengthPrefixedByteArray(digest);
                                }
                                digestCount = signatureCount3;
                                digestsSigAlgorithms = digestsSigAlgorithms2;
                                signatureCount = signatureCount2;
                                bestSigAlgorithmSignatureBytes = bestSigAlgorithmSignatureBytes2;
                                sigVerified = sigVerified2;
                                keyAlgorithm = keyAlgorithm2;
                            } catch (IOException | BufferUnderflowException e3) {
                                e = e3;
                            }
                        } catch (IOException | BufferUnderflowException e4) {
                            e = e4;
                        }
                        e = e3;
                    } catch (IOException | BufferUnderflowException e5) {
                        e = e5;
                    }
                } catch (IOException | BufferUnderflowException e6) {
                    e = e6;
                }
                throw new IOException("Failed to parse digest record #" + signatureCount3, e);
            }
            if (!signaturesSigAlgorithms.equals(digestsSigAlgorithms)) {
                throw new SecurityException("Signature algorithms don't match between digests and signatures records");
            }
            int digestAlgorithm = ApkSigningBlockUtils.getSignatureAlgorithmContentDigestAlgorithm(bestSigAlgorithm);
            byte[] previousSignerDigest = contentDigests.put(Integer.valueOf(digestAlgorithm), contentDigest);
            if (previousSignerDigest != null && !MessageDigest.isEqual(previousSignerDigest, contentDigest)) {
                throw new SecurityException(ApkSigningBlockUtils.getContentDigestAlgorithmJcaDigestAlgorithm(digestAlgorithm) + " contents digest does not match the digest specified by a preceding signer");
            }
            List<X509Certificate> certs = new ArrayList<>();
            int certificateCount = 0;
            for (ByteBuffer certificates = ApkSigningBlockUtils.getLengthPrefixedSlice(signedData); certificates.hasRemaining(); certificates = certificates) {
                int bestSigAlgorithm2 = bestSigAlgorithm;
                int bestSigAlgorithm3 = certificateCount + 1;
                int digestAlgorithm2 = digestAlgorithm;
                byte[] encodedCert = ApkSigningBlockUtils.readLengthPrefixedByteArray(certificates);
                try {
                    X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(encodedCert));
                    certs.add(new VerbatimX509Certificate(certificate, encodedCert));
                    certificateCount = bestSigAlgorithm3;
                    bestSigAlgorithm = bestSigAlgorithm2;
                    digestAlgorithm = digestAlgorithm2;
                } catch (CertificateException e7) {
                    throw new SecurityException("Failed to decode certificate #" + bestSigAlgorithm3, e7);
                }
            }
            if (certs.isEmpty()) {
                throw new SecurityException("No certificates listed");
            }
            X509Certificate mainCertificate = certs.get(0);
            byte[] certificatePublicKeyBytes = mainCertificate.getPublicKey().getEncoded();
            if (Arrays.equals(publicKeyBytes, certificatePublicKeyBytes)) {
                int signedMinSDK = signedData.getInt();
                if (signedMinSDK == minSdkVersion) {
                    int signedMaxSDK = signedData.getInt();
                    if (signedMaxSDK == maxSdkVersion) {
                        ByteBuffer additionalAttrs = ApkSigningBlockUtils.getLengthPrefixedSlice(signedData);
                        return verifyAdditionalAttributes(additionalAttrs, certs, certFactory);
                    }
                    throw new SecurityException("maxSdkVersion mismatch between signed and unsigned in v3 signer block.");
                }
                throw new SecurityException("minSdkVersion mismatch between signed and unsigned in v3 signer block.");
            }
            throw new SecurityException("Public key mismatch between certificate and signature record");
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException e8) {
            e = e8;
        }
    }

    private static VerifiedSigner verifyAdditionalAttributes(ByteBuffer attrs, List<X509Certificate> certs, CertificateFactory certFactory) throws IOException {
        X509Certificate[] certChain = (X509Certificate[]) certs.toArray(new X509Certificate[certs.size()]);
        VerifiedProofOfRotation por = null;
        while (attrs.hasRemaining()) {
            ByteBuffer attr = ApkSigningBlockUtils.getLengthPrefixedSlice(attrs);
            if (attr.remaining() < 4) {
                throw new IOException("Remaining buffer too short to contain additional attribute ID. Remaining: " + attr.remaining());
            }
            int id = attr.getInt();
            if (id == PROOF_OF_ROTATION_ATTR_ID) {
                if (por != null) {
                    throw new SecurityException("Encountered multiple Proof-of-rotation records when verifying APK Signature Scheme v3 signature");
                }
                por = verifyProofOfRotationStruct(attr, certFactory);
                try {
                    if (por.certs.size() > 0 && !Arrays.equals(por.certs.get(por.certs.size() - 1).getEncoded(), certChain[0].getEncoded())) {
                        throw new SecurityException("Terminal certificate in Proof-of-rotation record does not match APK signing certificate");
                    }
                } catch (CertificateEncodingException e) {
                    throw new SecurityException("Failed to encode certificate when comparing Proof-of-rotation record and signing certificate", e);
                }
            }
        }
        return new VerifiedSigner(certChain, por);
    }

    private static VerifiedProofOfRotation verifyProofOfRotationStruct(ByteBuffer porBuf, CertificateFactory certFactory) throws SecurityException, IOException {
        int levelCount = 0;
        int lastSigAlgorithm = -1;
        X509Certificate lastCert = null;
        List<X509Certificate> certs = new ArrayList<>();
        List<Integer> flagsList = new ArrayList<>();
        try {
            porBuf.getInt();
            HashSet<X509Certificate> certHistorySet = new HashSet<>();
            while (porBuf.hasRemaining()) {
                levelCount++;
                ByteBuffer level = ApkSigningBlockUtils.getLengthPrefixedSlice(porBuf);
                ByteBuffer signedData = ApkSigningBlockUtils.getLengthPrefixedSlice(level);
                int flags = level.getInt();
                int sigAlgorithm = level.getInt();
                byte[] signature = ApkSigningBlockUtils.readLengthPrefixedByteArray(level);
                if (lastCert != null) {
                    Pair<String, ? extends AlgorithmParameterSpec> sigAlgParams = ApkSigningBlockUtils.getSignatureAlgorithmJcaSignatureAlgorithm(lastSigAlgorithm);
                    PublicKey publicKey = lastCert.getPublicKey();
                    Signature sig = Signature.getInstance(sigAlgParams.first);
                    sig.initVerify(publicKey);
                    if (sigAlgParams.second != 0) {
                        sig.setParameter((AlgorithmParameterSpec) sigAlgParams.second);
                    }
                    sig.update(signedData);
                    if (!sig.verify(signature)) {
                        throw new SecurityException("Unable to verify signature of certificate #" + levelCount + " using " + sigAlgParams.first + " when verifying Proof-of-rotation record");
                    }
                }
                signedData.rewind();
                byte[] encodedCert = ApkSigningBlockUtils.readLengthPrefixedByteArray(signedData);
                int signedSigAlgorithm = signedData.getInt();
                if (lastCert != null && lastSigAlgorithm != signedSigAlgorithm) {
                    throw new SecurityException("Signing algorithm ID mismatch for certificate #" + levelCount + " when verifying Proof-of-rotation record");
                }
                try {
                    X509Certificate lastCert2 = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(encodedCert));
                    lastCert = new VerbatimX509Certificate(lastCert2, encodedCert);
                    lastSigAlgorithm = sigAlgorithm;
                    if (certHistorySet.contains(lastCert)) {
                        throw new SecurityException("Encountered duplicate entries in Proof-of-rotation record at certificate #" + levelCount + ".  All signing certificates should be unique");
                    }
                    certHistorySet.add(lastCert);
                    certs.add(lastCert);
                    flagsList.add(Integer.valueOf(flags));
                } catch (IOException | BufferUnderflowException e) {
                    e = e;
                    throw new IOException("Failed to parse Proof-of-rotation record", e);
                } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException e2) {
                    e = e2;
                    throw new SecurityException("Failed to verify signature over signed data for certificate #" + levelCount + " when verifying Proof-of-rotation record", e);
                } catch (CertificateException e3) {
                    e = e3;
                    throw new SecurityException("Failed to decode certificate #" + levelCount + " when verifying Proof-of-rotation record", e);
                }
            }
            return new VerifiedProofOfRotation(certs, flagsList);
        } catch (IOException | BufferUnderflowException e4) {
            e = e4;
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException e5) {
            e = e5;
        } catch (CertificateException e6) {
            e = e6;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getVerityRootHash(String apkPath) throws IOException, SignatureNotFoundException, SecurityException {
        RandomAccessFile apk = new RandomAccessFile(apkPath, "r");
        try {
            findSignature(apk);
            VerifiedSigner vSigner = verify(apk, false);
            byte[] bArr = vSigner.verityRootHash;
            $closeResource(null, apk);
            return bArr;
        } finally {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] generateApkVerity(String apkPath, ByteBufferFactory bufferFactory) throws IOException, SignatureNotFoundException, SecurityException, DigestException, NoSuchAlgorithmException {
        RandomAccessFile apk = new RandomAccessFile(apkPath, "r");
        try {
            SignatureInfo signatureInfo = findSignature(apk);
            byte[] generateApkVerity = VerityBuilder.generateApkVerity(apkPath, bufferFactory, signatureInfo);
            $closeResource(null, apk);
            return generateApkVerity;
        } finally {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] generateApkVerityRootHash(String apkPath) throws NoSuchAlgorithmException, DigestException, IOException, SignatureNotFoundException {
        RandomAccessFile apk = new RandomAccessFile(apkPath, "r");
        try {
            SignatureInfo signatureInfo = findSignature(apk);
            VerifiedSigner vSigner = verify(apk, false);
            if (vSigner.verityRootHash != null) {
                byte[] generateApkVerityRootHash = VerityBuilder.generateApkVerityRootHash(apk, ByteBuffer.wrap(vSigner.verityRootHash), signatureInfo);
                $closeResource(null, apk);
                return generateApkVerityRootHash;
            }
            $closeResource(null, apk);
            return null;
        } finally {
        }
    }

    private static boolean isSupportedSignatureAlgorithm(int sigAlgorithm) {
        if (sigAlgorithm == 513 || sigAlgorithm == 514 || sigAlgorithm == 769 || sigAlgorithm == 1057 || sigAlgorithm == 1059 || sigAlgorithm == 1061) {
            return true;
        }
        switch (sigAlgorithm) {
            case 257:
            case 258:
            case 259:
            case 260:
                return true;
            default:
                return false;
        }
    }

    /* loaded from: classes2.dex */
    public static class VerifiedProofOfRotation {
        public final List<X509Certificate> certs;
        public final List<Integer> flagsList;

        public VerifiedProofOfRotation(List<X509Certificate> certs, List<Integer> flagsList) {
            this.certs = certs;
            this.flagsList = flagsList;
        }
    }

    /* loaded from: classes2.dex */
    public static class VerifiedSigner {
        public final X509Certificate[] certs;
        public final VerifiedProofOfRotation por;
        public byte[] verityRootHash;

        public VerifiedSigner(X509Certificate[] certs, VerifiedProofOfRotation por) {
            this.certs = certs;
            this.por = por;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class PlatformNotSupportedException extends Exception {
        PlatformNotSupportedException(String s) {
            super(s);
        }
    }
}
