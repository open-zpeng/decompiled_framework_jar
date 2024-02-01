package android.net.wifi;

import android.os.Parcel;
import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/* loaded from: classes2.dex */
public class ParcelUtil {
    public static void writePrivateKey(Parcel dest, PrivateKey key) {
        if (key == null) {
            dest.writeString(null);
            return;
        }
        dest.writeString(key.getAlgorithm());
        dest.writeByteArray(key.getEncoded());
    }

    public static PrivateKey readPrivateKey(Parcel in) {
        String algorithm = in.readString();
        if (algorithm == null) {
            return null;
        }
        byte[] userKeyBytes = in.createByteArray();
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(userKeyBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }

    public static void writeCertificate(Parcel dest, X509Certificate cert) {
        byte[] certBytes = null;
        if (cert != null) {
            try {
                certBytes = cert.getEncoded();
            } catch (CertificateEncodingException e) {
            }
        }
        dest.writeByteArray(certBytes);
    }

    public static X509Certificate readCertificate(Parcel in) {
        byte[] certBytes = in.createByteArray();
        if (certBytes == null) {
            return null;
        }
        try {
            CertificateFactory cFactory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cFactory.generateCertificate(new ByteArrayInputStream(certBytes));
        } catch (CertificateException e) {
            return null;
        }
    }

    public static void writeCertificates(Parcel dest, X509Certificate[] certs) {
        if (certs == null || certs.length == 0) {
            dest.writeInt(0);
            return;
        }
        dest.writeInt(certs.length);
        for (X509Certificate x509Certificate : certs) {
            writeCertificate(dest, x509Certificate);
        }
    }

    public static X509Certificate[] readCertificates(Parcel in) {
        int length = in.readInt();
        if (length == 0) {
            return null;
        }
        X509Certificate[] certs = new X509Certificate[length];
        for (int i = 0; i < length; i++) {
            certs[i] = readCertificate(in);
        }
        return certs;
    }
}
