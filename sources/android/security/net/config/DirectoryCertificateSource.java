package android.security.net.config;

import android.util.ArraySet;
import android.util.Log;
import com.android.org.conscrypt.Hex;
import com.android.org.conscrypt.NativeCrypto;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import libcore.io.IoUtils;
/* loaded from: classes2.dex */
abstract class DirectoryCertificateSource implements CertificateSource {
    private static final String LOG_TAG = "DirectoryCertificateSrc";
    private final CertificateFactory mCertFactory;
    private Set<X509Certificate> mCertificates;
    private final File mDir;
    private final Object mLock = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public interface CertSelector {
        synchronized boolean match(X509Certificate x509Certificate);
    }

    protected abstract synchronized boolean isCertMarkedAsRemoved(String str);

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized DirectoryCertificateSource(File caDir) {
        this.mDir = caDir;
        try {
            this.mCertFactory = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            throw new RuntimeException("Failed to obtain X.509 CertificateFactory", e);
        }
    }

    @Override // android.security.net.config.CertificateSource
    public synchronized Set<X509Certificate> getCertificates() {
        String[] list;
        X509Certificate cert;
        synchronized (this.mLock) {
            if (this.mCertificates != null) {
                return this.mCertificates;
            }
            Set<X509Certificate> certs = new ArraySet<>();
            if (this.mDir.isDirectory()) {
                for (String caFile : this.mDir.list()) {
                    if (!isCertMarkedAsRemoved(caFile) && (cert = readCertificate(caFile)) != null) {
                        certs.add(cert);
                    }
                }
            }
            this.mCertificates = certs;
            return this.mCertificates;
        }
    }

    @Override // android.security.net.config.CertificateSource
    public synchronized X509Certificate findBySubjectAndPublicKey(final X509Certificate cert) {
        return findCert(cert.getSubjectX500Principal(), new CertSelector() { // from class: android.security.net.config.DirectoryCertificateSource.1
            @Override // android.security.net.config.DirectoryCertificateSource.CertSelector
            public boolean match(X509Certificate ca) {
                return ca.getPublicKey().equals(cert.getPublicKey());
            }
        });
    }

    @Override // android.security.net.config.CertificateSource
    public synchronized X509Certificate findByIssuerAndSignature(final X509Certificate cert) {
        return findCert(cert.getIssuerX500Principal(), new CertSelector() { // from class: android.security.net.config.DirectoryCertificateSource.2
            @Override // android.security.net.config.DirectoryCertificateSource.CertSelector
            public boolean match(X509Certificate ca) {
                try {
                    cert.verify(ca.getPublicKey());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    @Override // android.security.net.config.CertificateSource
    public synchronized Set<X509Certificate> findAllByIssuerAndSignature(final X509Certificate cert) {
        return findCerts(cert.getIssuerX500Principal(), new CertSelector() { // from class: android.security.net.config.DirectoryCertificateSource.3
            @Override // android.security.net.config.DirectoryCertificateSource.CertSelector
            public boolean match(X509Certificate ca) {
                try {
                    cert.verify(ca.getPublicKey());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    @Override // android.security.net.config.CertificateSource
    public synchronized void handleTrustStorageUpdate() {
        synchronized (this.mLock) {
            this.mCertificates = null;
        }
    }

    private synchronized Set<X509Certificate> findCerts(X500Principal subj, CertSelector selector) {
        X509Certificate cert;
        String hash = getHash(subj);
        Set<X509Certificate> certs = null;
        for (int index = 0; index >= 0; index++) {
            String fileName = hash + "." + index;
            if (!new File(this.mDir, fileName).exists()) {
                break;
            }
            if (!isCertMarkedAsRemoved(fileName) && (cert = readCertificate(fileName)) != null && subj.equals(cert.getSubjectX500Principal()) && selector.match(cert)) {
                if (certs == null) {
                    certs = new ArraySet<>();
                }
                certs.add(cert);
            }
        }
        return certs != null ? certs : Collections.emptySet();
    }

    private synchronized X509Certificate findCert(X500Principal subj, CertSelector selector) {
        X509Certificate cert;
        String hash = getHash(subj);
        for (int index = 0; index >= 0; index++) {
            String fileName = hash + "." + index;
            if (new File(this.mDir, fileName).exists()) {
                if (!isCertMarkedAsRemoved(fileName) && (cert = readCertificate(fileName)) != null && subj.equals(cert.getSubjectX500Principal()) && selector.match(cert)) {
                    return cert;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    private synchronized String getHash(X500Principal name) {
        int hash = NativeCrypto.X509_NAME_hash_old(name);
        return Hex.intToHexString(hash, 8);
    }

    private synchronized X509Certificate readCertificate(String file) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(new File(this.mDir, file)));
            return (X509Certificate) this.mCertFactory.generateCertificate(is);
        } catch (IOException | CertificateException e) {
            Log.e(LOG_TAG, "Failed to read certificate from " + file, e);
            return null;
        } finally {
            IoUtils.closeQuietly(is);
        }
    }
}
