package android.net.wifi.hotspot2;

import android.net.wifi.hotspot2.omadm.PpsMoParser;
import android.security.KeyChain;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes2.dex */
public final class ConfigParser {
    private static final String BOUNDARY = "boundary=";
    private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String ENCODING_BASE64 = "base64";
    private static final String TAG = "ConfigParser";
    private static final String TYPE_CA_CERT = "application/x-x509-ca-cert";
    private static final String TYPE_MULTIPART_MIXED = "multipart/mixed";
    private static final String TYPE_PASSPOINT_PROFILE = "application/x-passpoint-profile";
    private static final String TYPE_PKCS12 = "application/x-pkcs12";
    private static final String TYPE_WIFI_CONFIG = "application/x-wifi-config";

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class MimePart {
        public byte[] data;
        public boolean isLast;
        public String type;

        private synchronized MimePart() {
            this.type = null;
            this.data = null;
            this.isLast = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class MimeHeader {
        public String boundary;
        public String contentType;
        public String encodingType;

        private synchronized MimeHeader() {
            this.contentType = null;
            this.boundary = null;
            this.encodingType = null;
        }
    }

    public static PasspointConfiguration parsePasspointConfig(String mimeType, byte[] data) {
        if (!TextUtils.equals(mimeType, TYPE_WIFI_CONFIG)) {
            Log.e(TAG, "Unexpected MIME type: " + mimeType);
            return null;
        }
        try {
            byte[] decodedData = Base64.decode(new String(data, StandardCharsets.ISO_8859_1), 0);
            Map<String, byte[]> mimeParts = parseMimeMultipartMessage(new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(decodedData), StandardCharsets.ISO_8859_1)));
            return createPasspointConfig(mimeParts);
        } catch (IOException | IllegalArgumentException e) {
            Log.e(TAG, "Failed to parse installation file: " + e.getMessage());
            return null;
        }
    }

    private static synchronized PasspointConfiguration createPasspointConfig(Map<String, byte[]> mimeParts) throws IOException {
        byte[] profileData = mimeParts.get(TYPE_PASSPOINT_PROFILE);
        if (profileData == null) {
            throw new IOException("Missing Passpoint Profile");
        }
        PasspointConfiguration config = PpsMoParser.parseMoText(new String(profileData));
        if (config == null) {
            throw new IOException("Failed to parse Passpoint profile");
        }
        if (config.getCredential() == null) {
            throw new IOException("Passpoint profile missing credential");
        }
        byte[] caCertData = mimeParts.get(TYPE_CA_CERT);
        if (caCertData != null) {
            try {
                config.getCredential().setCaCertificate(parseCACert(caCertData));
            } catch (CertificateException e) {
                throw new IOException("Failed to parse CA Certificate");
            }
        }
        byte[] pkcs12Data = mimeParts.get(TYPE_PKCS12);
        if (pkcs12Data != null) {
            try {
                Pair<PrivateKey, List<X509Certificate>> clientKey = parsePkcs12(pkcs12Data);
                config.getCredential().setClientPrivateKey(clientKey.first);
                config.getCredential().setClientCertificateChain((X509Certificate[]) clientKey.second.toArray(new X509Certificate[clientKey.second.size()]));
            } catch (IOException | GeneralSecurityException e2) {
                throw new IOException("Failed to parse PCKS12 string");
            }
        }
        return config;
    }

    private static synchronized Map<String, byte[]> parseMimeMultipartMessage(LineNumberReader in) throws IOException {
        String line;
        boolean isLast;
        MimeHeader header = parseHeaders(in);
        if (!TextUtils.equals(header.contentType, TYPE_MULTIPART_MIXED)) {
            throw new IOException("Invalid content type: " + header.contentType);
        } else if (TextUtils.isEmpty(header.boundary)) {
            throw new IOException("Missing boundary string");
        } else {
            if (!TextUtils.equals(header.encodingType, ENCODING_BASE64)) {
                throw new IOException("Unexpected encoding: " + header.encodingType);
            }
            do {
                line = in.readLine();
                if (line == null) {
                    throw new IOException("Unexpected EOF before first boundary @ " + in.getLineNumber());
                }
            } while (!line.equals("--" + header.boundary));
            Map<String, byte[]> mimeParts = new HashMap<>();
            do {
                MimePart mimePart = parseMimePart(in, header.boundary);
                mimeParts.put(mimePart.type, mimePart.data);
                isLast = mimePart.isLast;
            } while (!isLast);
            return mimeParts;
        }
    }

    private static synchronized MimePart parseMimePart(LineNumberReader in, String boundary) throws IOException {
        MimeHeader header = parseHeaders(in);
        if (!TextUtils.equals(header.encodingType, ENCODING_BASE64)) {
            throw new IOException("Unexpected encoding type: " + header.encodingType);
        } else if (!TextUtils.equals(header.contentType, TYPE_PASSPOINT_PROFILE) && !TextUtils.equals(header.contentType, TYPE_CA_CERT) && !TextUtils.equals(header.contentType, TYPE_PKCS12)) {
            throw new IOException("Unexpected content type: " + header.contentType);
        } else {
            StringBuilder text = new StringBuilder();
            boolean isLast = false;
            String partBoundary = "--" + boundary;
            String endBoundary = partBoundary + "--";
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    throw new IOException("Unexpected EOF file in body @ " + in.getLineNumber());
                } else if (line.startsWith(partBoundary)) {
                    if (line.equals(endBoundary)) {
                        isLast = true;
                    }
                    MimePart part = new MimePart();
                    part.type = header.contentType;
                    part.data = Base64.decode(text.toString(), 0);
                    part.isLast = isLast;
                    return part;
                } else {
                    text.append(line);
                }
            }
        }
    }

    private static synchronized MimeHeader parseHeaders(LineNumberReader in) throws IOException {
        MimeHeader header = new MimeHeader();
        Map<String, String> headers = readHeaders(in);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            char c = 65535;
            int hashCode = key.hashCode();
            if (hashCode != 747297921) {
                if (hashCode == 949037134 && key.equals(CONTENT_TYPE)) {
                    c = 0;
                }
            } else if (key.equals(CONTENT_TRANSFER_ENCODING)) {
                c = 1;
            }
            switch (c) {
                case 0:
                    Pair<String, String> value = parseContentType(entry.getValue());
                    header.contentType = value.first;
                    header.boundary = value.second;
                    break;
                case 1:
                    header.encodingType = entry.getValue();
                    break;
                default:
                    Log.d(TAG, "Ignore header: " + entry.getKey());
                    break;
            }
        }
        return header;
    }

    private static synchronized Pair<String, String> parseContentType(String contentType) throws IOException {
        String[] attributes = contentType.split(";");
        if (attributes.length < 1) {
            throw new IOException("Invalid Content-Type: " + contentType);
        }
        String type = attributes[0].trim();
        String boundary = null;
        for (int i = 1; i < attributes.length; i++) {
            String attribute = attributes[i].trim();
            if (!attribute.startsWith(BOUNDARY)) {
                Log.d(TAG, "Ignore Content-Type attribute: " + attributes[i]);
            } else {
                boundary = attribute.substring(BOUNDARY.length());
                if (boundary.length() > 1 && boundary.startsWith("\"") && boundary.endsWith("\"")) {
                    boundary = boundary.substring(1, boundary.length() - 1);
                }
            }
        }
        return new Pair<>(type, boundary);
    }

    private static synchronized Map<String, String> readHeaders(LineNumberReader in) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String name = null;
        StringBuilder value = null;
        while (true) {
            String line = in.readLine();
            if (line == null) {
                throw new IOException("Missing line @ " + in.getLineNumber());
            } else if (line.length() == 0 || line.trim().length() == 0) {
                break;
            } else {
                int nameEnd = line.indexOf(58);
                if (nameEnd < 0) {
                    if (value != null) {
                        value.append(' ');
                        value.append(line.trim());
                    } else {
                        throw new IOException("Bad header line: '" + line + "' @ " + in.getLineNumber());
                    }
                } else if (Character.isWhitespace(line.charAt(0))) {
                    throw new IOException("Illegal blank prefix in header line '" + line + "' @ " + in.getLineNumber());
                } else {
                    if (name != null) {
                        headers.put(name, value.toString());
                    }
                    name = line.substring(0, nameEnd).trim();
                    value = new StringBuilder();
                    value.append(line.substring(nameEnd + 1).trim());
                }
            }
        }
        if (name != null) {
            headers.put(name, value.toString());
        }
        return headers;
    }

    private static synchronized X509Certificate parseCACert(byte[] octets) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(octets));
    }

    private static synchronized Pair<PrivateKey, List<X509Certificate>> parsePkcs12(byte[] octets) throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance(KeyChain.EXTRA_PKCS12);
        ByteArrayInputStream in = new ByteArrayInputStream(octets);
        ks.load(in, new char[0]);
        in.close();
        if (ks.size() != 1) {
            throw new IOException("Unexpected key size: " + ks.size());
        }
        String alias = ks.aliases().nextElement();
        if (alias == null) {
            throw new IOException("No alias found");
        }
        PrivateKey clientKey = (PrivateKey) ks.getKey(alias, null);
        List<X509Certificate> clientCertificateChain = null;
        Certificate[] chain = ks.getCertificateChain(alias);
        if (chain != null) {
            clientCertificateChain = new ArrayList<>();
            for (Certificate certificate : chain) {
                if (!(certificate instanceof X509Certificate)) {
                    throw new IOException("Unexpceted certificate type: " + certificate.getClass());
                }
                clientCertificateChain.add((X509Certificate) certificate);
            }
        }
        return new Pair<>(clientKey, clientCertificateChain);
    }
}
