package com.xpeng.security;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.security.KeyChain;
import android.util.Log;
import com.android.org.bouncycastle.util.io.pem.PemObject;
import com.android.org.bouncycastle.util.io.pem.PemWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
/* loaded from: classes3.dex */
public class Credentials {
    public static final String CA_CERTIFICATE = "CACERT_";
    public static final String EXTENSION_CER = ".cer";
    public static final String EXTENSION_CRT = ".crt";
    public static final String EXTENSION_P12 = ".p12";
    public static final String EXTENSION_PFX = ".pfx";
    public static final String EXTRA_CA_CERTIFICATES_DATA = "ca_certificates_data";
    public static final String EXTRA_CA_CERTIFICATES_NAME = "ca_certificates_name";
    public static final String EXTRA_INSTALL_AS_UID = "install_as_uid";
    public static final String EXTRA_PRIVATE_KEY = "PKEY";
    public static final String EXTRA_PUBLIC_KEY = "KEY";
    public static final String EXTRA_USER_CERTIFICATE_DATA = "user_certificate_data";
    public static final String EXTRA_USER_CERTIFICATE_NAME = "user_certificate_name";
    public static final String EXTRA_USER_PRIVATE_KEY_DATA = "user_private_key_data";
    public static final String EXTRA_USER_PRIVATE_KEY_NAME = "user_private_key_name";
    public static final String INSTALL_ACTION = "android.credentials.INSTALL";
    public static final String INSTALL_AS_USER_ACTION = "android.credentials.INSTALL_AS_USER";
    public static final String LOCKDOWN_VPN = "LOCKDOWN_VPN";
    private static final String LOGTAG = "Credentials";
    public static final String UNLOCK_ACTION = "com.android.credentials.UNLOCK";
    public static final String USER_CERTIFICATE = "USRCERT_";
    public static final String USER_PRIVATE_KEY = "USRPKEY_";
    public static final String USER_SECRET_KEY = "USRSKEY_";
    public static final String VPN = "VPN_";
    public static final String WIFI = "WIFI_";
    private static Credentials singleton;

    public static byte[] convertToPem(Certificate... objects) throws IOException, CertificateEncodingException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(bao, StandardCharsets.US_ASCII);
        PemWriter pw = new PemWriter(writer);
        for (Certificate o : objects) {
            pw.writeObject(new PemObject("CERTIFICATE", o.getEncoded()));
        }
        pw.close();
        return bao.toByteArray();
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x005d, code lost:
        throw new java.lang.IllegalArgumentException("Unknown type " + r5.getType());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<java.security.cert.X509Certificate> convertFromPem(byte[] r9) throws java.io.IOException, java.security.cert.CertificateException {
        /*
            java.io.ByteArrayInputStream r0 = new java.io.ByteArrayInputStream
            r0.<init>(r9)
            java.io.InputStreamReader r1 = new java.io.InputStreamReader
            java.nio.charset.Charset r2 = java.nio.charset.StandardCharsets.US_ASCII
            r1.<init>(r0, r2)
            com.android.org.bouncycastle.util.io.pem.PemReader r2 = new com.android.org.bouncycastle.util.io.pem.PemReader
            r2.<init>(r1)
            java.lang.String r3 = "X509"
            java.security.cert.CertificateFactory r3 = java.security.cert.CertificateFactory.getInstance(r3)     // Catch: java.lang.Throwable -> L63
            java.util.ArrayList r4 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L63
            r4.<init>()     // Catch: java.lang.Throwable -> L63
        L1c:
            com.android.org.bouncycastle.util.io.pem.PemObject r5 = r2.readPemObject()     // Catch: java.lang.Throwable -> L63
            r6 = r5
            if (r5 == 0) goto L5e
            java.lang.String r5 = r6.getType()     // Catch: java.lang.Throwable -> L63
            java.lang.String r7 = "CERTIFICATE"
            boolean r5 = r5.equals(r7)     // Catch: java.lang.Throwable -> L63
            if (r5 == 0) goto L43
            java.io.ByteArrayInputStream r5 = new java.io.ByteArrayInputStream     // Catch: java.lang.Throwable -> L63
            byte[] r7 = r6.getContent()     // Catch: java.lang.Throwable -> L63
            r5.<init>(r7)     // Catch: java.lang.Throwable -> L63
            java.security.cert.Certificate r5 = r3.generateCertificate(r5)     // Catch: java.lang.Throwable -> L63
            r7 = r5
            java.security.cert.X509Certificate r7 = (java.security.cert.X509Certificate) r7     // Catch: java.lang.Throwable -> L63
            r4.add(r7)     // Catch: java.lang.Throwable -> L63
            goto L1c
        L43:
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> L63
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L63
            r7.<init>()     // Catch: java.lang.Throwable -> L63
            java.lang.String r8 = "Unknown type "
            r7.append(r8)     // Catch: java.lang.Throwable -> L63
            java.lang.String r8 = r6.getType()     // Catch: java.lang.Throwable -> L63
            r7.append(r8)     // Catch: java.lang.Throwable -> L63
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> L63
            r5.<init>(r7)     // Catch: java.lang.Throwable -> L63
            throw r5     // Catch: java.lang.Throwable -> L63
        L5e:
            r2.close()
            return r4
        L63:
            r3 = move-exception
            r2.close()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xpeng.security.Credentials.convertFromPem(byte[]):java.util.List");
    }

    public static Credentials getInstance() {
        if (singleton == null) {
            singleton = new Credentials();
        }
        return singleton;
    }

    public void unlock(Context context) {
        try {
            Intent intent = new Intent("com.android.credentials.UNLOCK");
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(LOGTAG, e.toString());
        }
    }

    public void install(Context context) {
        try {
            Intent intent = KeyChain.createInstallIntent();
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(LOGTAG, e.toString());
        }
    }

    public void install(Context context, KeyPair pair) {
        try {
            Intent intent = KeyChain.createInstallIntent();
            intent.putExtra("PKEY", pair.getPrivate().getEncoded());
            intent.putExtra("KEY", pair.getPublic().getEncoded());
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(LOGTAG, e.toString());
        }
    }

    public void install(Context context, String type, byte[] value) {
        try {
            Intent intent = KeyChain.createInstallIntent();
            intent.putExtra(type, value);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.w(LOGTAG, e.toString());
        }
    }

    public static boolean deleteAllTypesForAlias(KeyStore keystore, String alias) {
        return deleteAllTypesForAlias(keystore, alias, -1);
    }

    public static boolean deleteAllTypesForAlias(KeyStore keystore, String alias, int uid) {
        return deleteUserKeyTypeForAlias(keystore, alias, uid) & deleteCertificateTypesForAlias(keystore, alias, uid);
    }

    public static boolean deleteCertificateTypesForAlias(KeyStore keystore, String alias) {
        return deleteCertificateTypesForAlias(keystore, alias, -1);
    }

    public static boolean deleteCertificateTypesForAlias(KeyStore keystore, String alias, int uid) {
        boolean delete = keystore.delete("USRCERT_" + alias, uid);
        return delete & keystore.delete("CACERT_" + alias, uid);
    }

    public static boolean deleteUserKeyTypeForAlias(KeyStore keystore, String alias) {
        return deleteUserKeyTypeForAlias(keystore, alias, -1);
    }

    public static boolean deleteUserKeyTypeForAlias(KeyStore keystore, String alias, int uid) {
        if (!keystore.delete("USRPKEY_" + alias, uid)) {
            if (!keystore.delete("USRSKEY_" + alias, uid)) {
                return false;
            }
        }
        return true;
    }

    public static boolean deleteLegacyKeyForAlias(KeyStore keystore, String alias, int uid) {
        return keystore.delete("USRSKEY_" + alias, uid);
    }
}
