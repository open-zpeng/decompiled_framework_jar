package android.util.apk;

import android.app.backup.FullBackup;
import android.util.ArrayMap;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: classes2.dex */
public class ApkSignatureSchemeV2Verifier {
    private static final int APK_SIGNATURE_SCHEME_V2_BLOCK_ID = 1896449818;
    public static final int SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID = 2;
    private static final int STRIPPING_PROTECTION_ATTR_ID = -1091571699;

    public static synchronized boolean hasSignature(String apkFile) throws IOException {
        try {
            RandomAccessFile apk = new RandomAccessFile(apkFile, FullBackup.ROOT_TREE_TOKEN);
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

    public static synchronized X509Certificate[][] verify(String apkFile) throws SignatureNotFoundException, SecurityException, IOException {
        VerifiedSigner vSigner = verify(apkFile, true);
        return vSigner.certs;
    }

    public static synchronized X509Certificate[][] plsCertsNoVerifyOnlyCerts(String apkFile) throws SignatureNotFoundException, SecurityException, IOException {
        VerifiedSigner vSigner = verify(apkFile, false);
        return vSigner.certs;
    }

    private static synchronized VerifiedSigner verify(String apkFile, boolean verifyIntegrity) throws SignatureNotFoundException, SecurityException, IOException {
        RandomAccessFile apk = new RandomAccessFile(apkFile, FullBackup.ROOT_TREE_TOKEN);
        try {
            VerifiedSigner verify = verify(apk, verifyIntegrity);
            $closeResource(null, apk);
            return verify;
        } finally {
        }
    }

    private static synchronized VerifiedSigner verify(RandomAccessFile apk, boolean verifyIntegrity) throws SignatureNotFoundException, SecurityException, IOException {
        SignatureInfo signatureInfo = findSignature(apk);
        return verify(apk, signatureInfo, verifyIntegrity);
    }

    private static synchronized SignatureInfo findSignature(RandomAccessFile apk) throws IOException, SignatureNotFoundException {
        return ApkSigningBlockUtils.findSignature(apk, APK_SIGNATURE_SCHEME_V2_BLOCK_ID);
    }

    private static synchronized VerifiedSigner verify(RandomAccessFile apk, SignatureInfo signatureInfo, boolean doVerifyIntegrity) throws SecurityException, IOException {
        int signerCount = 0;
        Map<Integer, byte[]> contentDigests = new ArrayMap<>();
        List<X509Certificate[]> signerCerts = new ArrayList<>();
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            try {
                ByteBuffer signers = ApkSigningBlockUtils.getLengthPrefixedSlice(signatureInfo.signatureBlock);
                while (signers.hasRemaining()) {
                    signerCount++;
                    try {
                        ByteBuffer signer = ApkSigningBlockUtils.getLengthPrefixedSlice(signers);
                        X509Certificate[] certs = verifySigner(signer, contentDigests, certFactory);
                        signerCerts.add(certs);
                    } catch (IOException | SecurityException | BufferUnderflowException e) {
                        throw new SecurityException("Failed to parse/verify signer #" + signerCount + " block", e);
                    }
                }
                if (signerCount < 1) {
                    throw new SecurityException("No signers found");
                }
                if (contentDigests.isEmpty()) {
                    throw new SecurityException("No content digests found");
                }
                if (doVerifyIntegrity) {
                    ApkSigningBlockUtils.verifyIntegrity(contentDigests, apk, signatureInfo);
                }
                byte[] verityRootHash = null;
                if (contentDigests.containsKey(3)) {
                    byte[] verityDigest = contentDigests.get(3);
                    verityRootHash = ApkSigningBlockUtils.parseVerityDigestAndVerifySourceLength(verityDigest, apk.length(), signatureInfo);
                }
                return new VerifiedSigner((X509Certificate[][]) signerCerts.toArray(new X509Certificate[signerCerts.size()]), verityRootHash);
            } catch (IOException e2) {
                throw new SecurityException("Failed to read list of signers", e2);
            }
        } catch (CertificateException e3) {
            throw new RuntimeException("Failed to obtain X.509 CertificateFactory", e3);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x0118, code lost:
        throw new java.io.IOException("Record too short");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static synchronized java.security.cert.X509Certificate[] verifySigner(java.nio.ByteBuffer r28, java.util.Map<java.lang.Integer, byte[]> r29, java.security.cert.CertificateFactory r30) throws java.lang.SecurityException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 649
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSignatureSchemeV2Verifier.verifySigner(java.nio.ByteBuffer, java.util.Map, java.security.cert.CertificateFactory):java.security.cert.X509Certificate[]");
    }

    private static synchronized void verifyAdditionalAttributes(ByteBuffer attrs) throws SecurityException, IOException {
        while (attrs.hasRemaining()) {
            ByteBuffer attr = ApkSigningBlockUtils.getLengthPrefixedSlice(attrs);
            if (attr.remaining() < 4) {
                throw new IOException("Remaining buffer too short to contain additional attribute ID. Remaining: " + attr.remaining());
            }
            int id = attr.getInt();
            if (id == STRIPPING_PROTECTION_ATTR_ID) {
                if (attr.remaining() < 4) {
                    throw new IOException("V2 Signature Scheme Stripping Protection Attribute  value too small.  Expected 4 bytes, but found " + attr.remaining());
                }
                int vers = attr.getInt();
                if (vers == 3) {
                    throw new SecurityException("V2 signature indicates APK is signed using APK Signature Scheme v3, but none was found. Signature stripped?");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized byte[] getVerityRootHash(String apkPath) throws IOException, SignatureNotFoundException, SecurityException {
        RandomAccessFile apk = new RandomAccessFile(apkPath, FullBackup.ROOT_TREE_TOKEN);
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
    public static synchronized byte[] generateApkVerity(String apkPath, ByteBufferFactory bufferFactory) throws IOException, SignatureNotFoundException, SecurityException, DigestException, NoSuchAlgorithmException {
        RandomAccessFile apk = new RandomAccessFile(apkPath, FullBackup.ROOT_TREE_TOKEN);
        try {
            SignatureInfo signatureInfo = findSignature(apk);
            byte[] generateApkVerity = ApkSigningBlockUtils.generateApkVerity(apkPath, bufferFactory, signatureInfo);
            $closeResource(null, apk);
            return generateApkVerity;
        } finally {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized byte[] generateFsverityRootHash(String apkPath) throws IOException, SignatureNotFoundException, DigestException, NoSuchAlgorithmException {
        RandomAccessFile apk = new RandomAccessFile(apkPath, FullBackup.ROOT_TREE_TOKEN);
        try {
            SignatureInfo signatureInfo = findSignature(apk);
            VerifiedSigner vSigner = verify(apk, false);
            if (vSigner.verityRootHash == null) {
                $closeResource(null, apk);
                return null;
            }
            byte[] generateFsverityRootHash = ApkVerityBuilder.generateFsverityRootHash(apk, ByteBuffer.wrap(vSigner.verityRootHash), signatureInfo);
            $closeResource(null, apk);
            return generateFsverityRootHash;
        } finally {
        }
    }

    private static synchronized boolean isSupportedSignatureAlgorithm(int sigAlgorithm) {
        if (sigAlgorithm == 769 || sigAlgorithm == 1057 || sigAlgorithm == 1059 || sigAlgorithm == 1061) {
            return true;
        }
        switch (sigAlgorithm) {
            case 257:
            case 258:
            case 259:
            case 260:
                return true;
            default:
                switch (sigAlgorithm) {
                    case 513:
                    case 514:
                        return true;
                    default:
                        return false;
                }
        }
    }

    /* loaded from: classes2.dex */
    public static class VerifiedSigner {
        public final X509Certificate[][] certs;
        public final byte[] verityRootHash;

        public synchronized VerifiedSigner(X509Certificate[][] certs, byte[] verityRootHash) {
            this.certs = certs;
            this.verityRootHash = verityRootHash;
        }
    }
}
