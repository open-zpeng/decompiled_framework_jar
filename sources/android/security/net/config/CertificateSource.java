package android.security.net.config;

import java.security.cert.X509Certificate;
import java.util.Set;
/* loaded from: classes2.dex */
public interface CertificateSource {
    synchronized Set<X509Certificate> findAllByIssuerAndSignature(X509Certificate x509Certificate);

    synchronized X509Certificate findByIssuerAndSignature(X509Certificate x509Certificate);

    synchronized X509Certificate findBySubjectAndPublicKey(X509Certificate x509Certificate);

    synchronized Set<X509Certificate> getCertificates();

    synchronized void handleTrustStorageUpdate();
}
