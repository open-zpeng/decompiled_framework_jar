package android.util.apk;

import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.Signature;
import android.os.Trace;
import android.util.apk.ApkSignatureSchemeV3Verifier;
import android.util.jar.StrictJarFile;
import com.android.internal.util.ArrayUtils;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import libcore.io.IoUtils;
/* loaded from: classes2.dex */
public class ApkSignatureVerifier {
    private static final AtomicReference<byte[]> sBuffer = new AtomicReference<>();

    public static synchronized PackageParser.SigningDetails verify(String apkPath, @PackageParser.SigningDetails.SignatureSchemeVersion int minSignatureSchemeVersion) throws PackageParser.PackageParserException {
        if (minSignatureSchemeVersion > 3) {
            throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "No signature found in package of version " + minSignatureSchemeVersion + " or newer for package " + apkPath);
        }
        Trace.traceBegin(262144L, "verifyV3");
        try {
            try {
                try {
                    ApkSignatureSchemeV3Verifier.VerifiedSigner vSigner = ApkSignatureSchemeV3Verifier.verify(apkPath);
                    Certificate[][] signerCerts = {vSigner.certs};
                    Signature[] signerSigs = convertToSignatures(signerCerts);
                    Signature[] pastSignerSigs = null;
                    int[] pastSignerSigsFlags = null;
                    if (vSigner.por != null) {
                        pastSignerSigs = new Signature[vSigner.por.certs.size()];
                        pastSignerSigsFlags = new int[vSigner.por.flagsList.size()];
                        for (int i = 0; i < pastSignerSigs.length; i++) {
                            pastSignerSigs[i] = new Signature(vSigner.por.certs.get(i).getEncoded());
                            pastSignerSigsFlags[i] = vSigner.por.flagsList.get(i).intValue();
                        }
                    }
                    PackageParser.SigningDetails signingDetails = new PackageParser.SigningDetails(signerSigs, 3, pastSignerSigs, pastSignerSigsFlags);
                    Trace.traceEnd(262144L);
                    return signingDetails;
                } catch (Throwable th) {
                    Trace.traceEnd(262144L);
                    throw th;
                }
            } catch (SignatureNotFoundException e) {
                if (minSignatureSchemeVersion >= 3) {
                    throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "No APK Signature Scheme v3 signature in package " + apkPath, e);
                }
                Trace.traceEnd(262144L);
                if (minSignatureSchemeVersion > 2) {
                    throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "No signature found in package of version " + minSignatureSchemeVersion + " or newer for package " + apkPath);
                }
                Trace.traceBegin(262144L, "verifyV2");
                try {
                    try {
                        try {
                            Signature[] signerSigs2 = convertToSignatures(ApkSignatureSchemeV2Verifier.verify(apkPath));
                            PackageParser.SigningDetails signingDetails2 = new PackageParser.SigningDetails(signerSigs2, 2);
                            Trace.traceEnd(262144L);
                            return signingDetails2;
                        } catch (SignatureNotFoundException e2) {
                            if (minSignatureSchemeVersion >= 2) {
                                throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "No APK Signature Scheme v2 signature in package " + apkPath, e2);
                            }
                            Trace.traceEnd(262144L);
                            if (minSignatureSchemeVersion <= 1) {
                                return verifyV1Signature(apkPath, true);
                            }
                            throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "No signature found in package of version " + minSignatureSchemeVersion + " or newer for package " + apkPath);
                        }
                    } catch (Exception e3) {
                        throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Failed to collect certificates from " + apkPath + " using APK Signature Scheme v2", e3);
                    }
                } catch (Throwable th2) {
                    Trace.traceEnd(262144L);
                    throw th2;
                }
            }
        } catch (Exception e4) {
            throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Failed to collect certificates from " + apkPath + " using APK Signature Scheme v3", e4);
        }
    }

    private static synchronized PackageParser.SigningDetails verifyV1Signature(String apkPath, boolean verifyFull) throws PackageParser.PackageParserException {
        StrictJarFile jarFile = null;
        try {
            try {
                try {
                    Trace.traceBegin(262144L, "strictJarFileCtor");
                    jarFile = new StrictJarFile(apkPath, true, verifyFull);
                    List<ZipEntry> toVerify = new ArrayList<>();
                    ZipEntry manifestEntry = jarFile.findEntry(PackageParser.ANDROID_MANIFEST_FILENAME);
                    if (manifestEntry == null) {
                        throw new PackageParser.PackageParserException(-101, "Package " + apkPath + " has no manifest");
                    }
                    Certificate[][] lastCerts = loadCertificates(jarFile, manifestEntry);
                    if (ArrayUtils.isEmpty(lastCerts)) {
                        throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Package " + apkPath + " has no certificates at entry " + PackageParser.ANDROID_MANIFEST_FILENAME);
                    }
                    Signature[] lastSigs = convertToSignatures(lastCerts);
                    if (verifyFull) {
                        Iterator<ZipEntry> i = jarFile.iterator();
                        while (i.hasNext()) {
                            ZipEntry entry = i.next();
                            if (!entry.isDirectory()) {
                                String entryName = entry.getName();
                                if (!entryName.startsWith("META-INF/") && !entryName.equals(PackageParser.ANDROID_MANIFEST_FILENAME)) {
                                    toVerify.add(entry);
                                }
                            }
                        }
                        for (ZipEntry entry2 : toVerify) {
                            Certificate[][] entryCerts = loadCertificates(jarFile, entry2);
                            if (ArrayUtils.isEmpty(entryCerts)) {
                                throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Package " + apkPath + " has no certificates at entry " + entry2.getName());
                            }
                            Signature[] entrySigs = convertToSignatures(entryCerts);
                            if (!Signature.areExactMatch(lastSigs, entrySigs)) {
                                throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES, "Package " + apkPath + " has mismatched certificates at entry " + entry2.getName());
                            }
                        }
                    }
                    return new PackageParser.SigningDetails(lastSigs, 1);
                } catch (IOException | RuntimeException e) {
                    throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Failed to collect certificates from " + apkPath, e);
                }
            } catch (GeneralSecurityException e2) {
                throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING, "Failed to collect certificates from " + apkPath, e2);
            }
        } finally {
            Trace.traceEnd(262144L);
            closeQuietly(jarFile);
        }
    }

    private static synchronized Certificate[][] loadCertificates(StrictJarFile jarFile, ZipEntry entry) throws PackageParser.PackageParserException {
        InputStream is = null;
        try {
            try {
                is = jarFile.getInputStream(entry);
                readFullyIgnoringContents(is);
                return jarFile.getCertificateChains(entry);
            } catch (IOException | RuntimeException e) {
                throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed reading " + entry.getName() + " in " + jarFile, e);
            }
        } finally {
            IoUtils.closeQuietly(is);
        }
    }

    private static synchronized void readFullyIgnoringContents(InputStream in) throws IOException {
        byte[] buffer = sBuffer.getAndSet(null);
        if (buffer == null) {
            buffer = new byte[4096];
        }
        int n = 0;
        while (true) {
            int n2 = in.read(buffer, 0, buffer.length);
            if (n2 != -1) {
                n += n2;
            } else {
                sBuffer.set(buffer);
                return;
            }
        }
    }

    public static synchronized Signature[] convertToSignatures(Certificate[][] certs) throws CertificateEncodingException {
        Signature[] res = new Signature[certs.length];
        for (int i = 0; i < certs.length; i++) {
            res[i] = new Signature(certs[i]);
        }
        return res;
    }

    private static synchronized void closeQuietly(StrictJarFile jarFile) {
        if (jarFile != null) {
            try {
                jarFile.close();
            } catch (Exception e) {
            }
        }
    }

    public static synchronized PackageParser.SigningDetails plsCertsNoVerifyOnlyCerts(String apkPath, int minSignatureSchemeVersion) throws PackageParser.PackageParserException {
        if (minSignatureSchemeVersion > 3) {
            throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "No signature found in package of version " + minSignatureSchemeVersion + " or newer for package " + apkPath);
        }
        Trace.traceBegin(262144L, "certsOnlyV3");
        try {
            try {
                ApkSignatureSchemeV3Verifier.VerifiedSigner vSigner = ApkSignatureSchemeV3Verifier.plsCertsNoVerifyOnlyCerts(apkPath);
                Certificate[][] signerCerts = {vSigner.certs};
                Signature[] signerSigs = convertToSignatures(signerCerts);
                Signature[] pastSignerSigs = null;
                int[] pastSignerSigsFlags = null;
                if (vSigner.por != null) {
                    pastSignerSigs = new Signature[vSigner.por.certs.size()];
                    pastSignerSigsFlags = new int[vSigner.por.flagsList.size()];
                    for (int i = 0; i < pastSignerSigs.length; i++) {
                        pastSignerSigs[i] = new Signature(vSigner.por.certs.get(i).getEncoded());
                        pastSignerSigsFlags[i] = vSigner.por.flagsList.get(i).intValue();
                    }
                }
                PackageParser.SigningDetails signingDetails = new PackageParser.SigningDetails(signerSigs, 3, pastSignerSigs, pastSignerSigsFlags);
                Trace.traceEnd(262144L);
                return signingDetails;
            } catch (SignatureNotFoundException e) {
                if (minSignatureSchemeVersion >= 3) {
                    throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "No APK Signature Scheme v3 signature in package " + apkPath, e);
                }
                Trace.traceEnd(262144L);
                if (minSignatureSchemeVersion > 2) {
                    throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "No signature found in package of version " + minSignatureSchemeVersion + " or newer for package " + apkPath);
                }
                Trace.traceBegin(262144L, "certsOnlyV2");
                try {
                    try {
                        try {
                            Signature[] signerSigs2 = convertToSignatures(ApkSignatureSchemeV2Verifier.plsCertsNoVerifyOnlyCerts(apkPath));
                            PackageParser.SigningDetails signingDetails2 = new PackageParser.SigningDetails(signerSigs2, 2);
                            Trace.traceEnd(262144L);
                            return signingDetails2;
                        } catch (SignatureNotFoundException e2) {
                            if (minSignatureSchemeVersion >= 2) {
                                throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "No APK Signature Scheme v2 signature in package " + apkPath, e2);
                            }
                            Trace.traceEnd(262144L);
                            if (minSignatureSchemeVersion <= 1) {
                                return verifyV1Signature(apkPath, false);
                            }
                            throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "No signature found in package of version " + minSignatureSchemeVersion + " or newer for package " + apkPath);
                        }
                    } catch (Exception e3) {
                        throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Failed to collect certificates from " + apkPath + " using APK Signature Scheme v2", e3);
                    }
                } catch (Throwable th) {
                    Trace.traceEnd(262144L);
                    throw th;
                }
            } catch (Exception e4) {
                throw new PackageParser.PackageParserException(PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES, "Failed to collect certificates from " + apkPath + " using APK Signature Scheme v3", e4);
            }
        } catch (Throwable th2) {
            Trace.traceEnd(262144L);
            throw th2;
        }
    }

    public static synchronized byte[] getVerityRootHash(String apkPath) throws IOException, SignatureNotFoundException, SecurityException {
        try {
            return ApkSignatureSchemeV3Verifier.getVerityRootHash(apkPath);
        } catch (SignatureNotFoundException e) {
            return ApkSignatureSchemeV2Verifier.getVerityRootHash(apkPath);
        }
    }

    public static synchronized byte[] generateApkVerity(String apkPath, ByteBufferFactory bufferFactory) throws IOException, SignatureNotFoundException, SecurityException, DigestException, NoSuchAlgorithmException {
        try {
            return ApkSignatureSchemeV3Verifier.generateApkVerity(apkPath, bufferFactory);
        } catch (SignatureNotFoundException e) {
            return ApkSignatureSchemeV2Verifier.generateApkVerity(apkPath, bufferFactory);
        }
    }

    public static synchronized byte[] generateFsverityRootHash(String apkPath) throws NoSuchAlgorithmException, DigestException, IOException {
        try {
            return ApkSignatureSchemeV3Verifier.generateFsverityRootHash(apkPath);
        } catch (SignatureNotFoundException e) {
            try {
                return ApkSignatureSchemeV2Verifier.generateFsverityRootHash(apkPath);
            } catch (SignatureNotFoundException e2) {
                return null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Result {
        public final Certificate[][] certs;
        public final int signatureSchemeVersion;
        public final Signature[] sigs;

        public synchronized Result(Certificate[][] certs, Signature[] sigs, int signingVersion) {
            this.certs = certs;
            this.sigs = sigs;
            this.signatureSchemeVersion = signingVersion;
        }
    }
}
