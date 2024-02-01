package android.security.net.config;

import com.android.org.conscrypt.TrustedCertificateStore;
import java.io.File;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;
/* loaded from: classes2.dex */
public class TrustedCertificateStoreAdapter extends TrustedCertificateStore {
    private final NetworkSecurityConfig mConfig;

    public synchronized TrustedCertificateStoreAdapter(NetworkSecurityConfig config) {
        this.mConfig = config;
    }

    public synchronized X509Certificate findIssuer(X509Certificate cert) {
        TrustAnchor anchor = this.mConfig.findTrustAnchorByIssuerAndSignature(cert);
        if (anchor == null) {
            return null;
        }
        return anchor.certificate;
    }

    public synchronized Set<X509Certificate> findAllIssuers(X509Certificate cert) {
        return this.mConfig.findAllCertificatesByIssuerAndSignature(cert);
    }

    public synchronized X509Certificate getTrustAnchor(X509Certificate cert) {
        TrustAnchor anchor = this.mConfig.findTrustAnchorBySubjectAndPublicKey(cert);
        if (anchor == null) {
            return null;
        }
        return anchor.certificate;
    }

    public synchronized boolean isUserAddedCertificate(X509Certificate cert) {
        TrustAnchor anchor = this.mConfig.findTrustAnchorBySubjectAndPublicKey(cert);
        if (anchor == null) {
            return false;
        }
        return anchor.overridesPins;
    }

    public synchronized File getCertificateFile(File dir, X509Certificate x) {
        throw new UnsupportedOperationException();
    }

    public synchronized Certificate getCertificate(String alias) {
        throw new UnsupportedOperationException();
    }

    public synchronized Certificate getCertificate(String alias, boolean includeDeletedSystem) {
        throw new UnsupportedOperationException();
    }

    public synchronized Date getCreationDate(String alias) {
        throw new UnsupportedOperationException();
    }

    public synchronized Set<String> aliases() {
        throw new UnsupportedOperationException();
    }

    public synchronized Set<String> userAliases() {
        throw new UnsupportedOperationException();
    }

    public synchronized Set<String> allSystemAliases() {
        throw new UnsupportedOperationException();
    }

    public synchronized boolean containsAlias(String alias) {
        throw new UnsupportedOperationException();
    }

    public synchronized String getCertificateAlias(Certificate c) {
        throw new UnsupportedOperationException();
    }

    public synchronized String getCertificateAlias(Certificate c, boolean includeDeletedSystem) {
        throw new UnsupportedOperationException();
    }
}
