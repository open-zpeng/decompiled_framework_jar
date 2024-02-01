package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.midi.MidiConstants;
import com.android.internal.util.ArrayUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
/* loaded from: classes.dex */
public class Signature implements Parcelable {
    public static final Parcelable.Creator<Signature> CREATOR = new Parcelable.Creator<Signature>() { // from class: android.content.pm.Signature.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Signature createFromParcel(Parcel source) {
            return new Signature(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Signature[] newArray(int size) {
            return new Signature[size];
        }
    };
    private Certificate[] mCertificateChain;
    private int mHashCode;
    private boolean mHaveHashCode;
    private final byte[] mSignature;
    private SoftReference<String> mStringRef;

    public Signature(byte[] signature) {
        this.mSignature = (byte[]) signature.clone();
        this.mCertificateChain = null;
    }

    public synchronized Signature(Certificate[] certificateChain) throws CertificateEncodingException {
        this.mSignature = certificateChain[0].getEncoded();
        if (certificateChain.length > 1) {
            this.mCertificateChain = (Certificate[]) Arrays.copyOfRange(certificateChain, 1, certificateChain.length);
        }
    }

    private static final synchronized int parseHexDigit(int nibble) {
        if (48 <= nibble && nibble <= 57) {
            return nibble - 48;
        }
        if (97 <= nibble && nibble <= 102) {
            return (nibble - 97) + 10;
        }
        if (65 <= nibble && nibble <= 70) {
            return (nibble - 65) + 10;
        }
        throw new IllegalArgumentException("Invalid character " + nibble + " in hex string");
    }

    public Signature(String text) {
        byte[] input = text.getBytes();
        int N = input.length;
        if (N % 2 != 0) {
            throw new IllegalArgumentException("text size " + N + " is not even");
        }
        byte[] sig = new byte[N / 2];
        int sigIndex = 0;
        int i = 0;
        while (i < N) {
            int i2 = i + 1;
            int hi = parseHexDigit(input[i]);
            int i3 = i2 + 1;
            int lo = parseHexDigit(input[i2]);
            sig[sigIndex] = (byte) ((hi << 4) | lo);
            i = i3;
            sigIndex++;
        }
        this.mSignature = sig;
    }

    public char[] toChars() {
        return toChars(null, null);
    }

    public char[] toChars(char[] existingArray, int[] outLen) {
        byte[] sig = this.mSignature;
        int N = sig.length;
        int N2 = N * 2;
        char[] text = (existingArray == null || N2 > existingArray.length) ? new char[N2] : existingArray;
        for (int j = 0; j < N; j++) {
            byte v = sig[j];
            int d = (v >> 4) & 15;
            text[j * 2] = (char) (d >= 10 ? (97 + d) - 10 : 48 + d);
            int d2 = v & MidiConstants.STATUS_CHANNEL_MASK;
            text[(j * 2) + 1] = (char) (d2 >= 10 ? (97 + d2) - 10 : 48 + d2);
        }
        if (outLen != null) {
            outLen[0] = N;
        }
        return text;
    }

    public String toCharsString() {
        String str = this.mStringRef == null ? null : this.mStringRef.get();
        if (str != null) {
            return str;
        }
        String str2 = new String(toChars());
        this.mStringRef = new SoftReference<>(str2);
        return str2;
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[this.mSignature.length];
        System.arraycopy(this.mSignature, 0, bytes, 0, this.mSignature.length);
        return bytes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PublicKey getPublicKey() throws CertificateException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream bais = new ByteArrayInputStream(this.mSignature);
        Certificate cert = certFactory.generateCertificate(bais);
        return cert.getPublicKey();
    }

    public synchronized Signature[] getChainSignatures() throws CertificateEncodingException {
        int i = 0;
        if (this.mCertificateChain == null) {
            return new Signature[]{this};
        }
        Signature[] chain = new Signature[1 + this.mCertificateChain.length];
        chain[0] = this;
        int i2 = 1;
        Certificate[] certificateArr = this.mCertificateChain;
        int length = certificateArr.length;
        while (i < length) {
            Certificate c = certificateArr[i];
            chain[i2] = new Signature(c.getEncoded());
            i++;
            i2++;
        }
        return chain;
    }

    public boolean equals(Object obj) {
        if (obj != null) {
            try {
                Signature other = (Signature) obj;
                if (this != other) {
                    if (!Arrays.equals(this.mSignature, other.mSignature)) {
                        return false;
                    }
                }
                return true;
            } catch (ClassCastException e) {
            }
        }
        return false;
    }

    public int hashCode() {
        if (this.mHaveHashCode) {
            return this.mHashCode;
        }
        this.mHashCode = Arrays.hashCode(this.mSignature);
        this.mHaveHashCode = true;
        return this.mHashCode;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeByteArray(this.mSignature);
    }

    private synchronized Signature(Parcel source) {
        this.mSignature = source.createByteArray();
    }

    public static synchronized boolean areExactMatch(Signature[] a, Signature[] b) {
        return a.length == b.length && ArrayUtils.containsAll(a, b) && ArrayUtils.containsAll(b, a);
    }

    public static synchronized boolean areEffectiveMatch(Signature[] a, Signature[] b) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Signature[] aPrime = new Signature[a.length];
        for (int i = 0; i < a.length; i++) {
            aPrime[i] = bounce(cf, a[i]);
        }
        int i2 = b.length;
        Signature[] bPrime = new Signature[i2];
        for (int i3 = 0; i3 < b.length; i3++) {
            bPrime[i3] = bounce(cf, b[i3]);
        }
        return areExactMatch(aPrime, bPrime);
    }

    public static synchronized boolean areEffectiveMatch(Signature a, Signature b) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Signature aPrime = bounce(cf, a);
        Signature bPrime = bounce(cf, b);
        return aPrime.equals(bPrime);
    }

    public static synchronized Signature bounce(CertificateFactory cf, Signature s) throws CertificateException {
        InputStream is = new ByteArrayInputStream(s.mSignature);
        X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
        Signature sPrime = new Signature(cert.getEncoded());
        if (Math.abs(sPrime.mSignature.length - s.mSignature.length) > 2) {
            throw new CertificateException("Bounced cert length looks fishy; before " + s.mSignature.length + ", after " + sPrime.mSignature.length);
        }
        return sPrime;
    }
}
