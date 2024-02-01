package android.net.wifi.hotspot2.pps;

import android.net.wifi.ParcelUtil;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes2.dex */
public final class Credential implements Parcelable {
    public static final Parcelable.Creator<Credential> CREATOR = new Parcelable.Creator<Credential>() { // from class: android.net.wifi.hotspot2.pps.Credential.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Credential createFromParcel(Parcel in) {
            Credential credential = new Credential();
            credential.setCreationTimeInMillis(in.readLong());
            credential.setExpirationTimeInMillis(in.readLong());
            credential.setRealm(in.readString());
            credential.setCheckAaaServerCertStatus(in.readInt() != 0);
            credential.setUserCredential((UserCredential) in.readParcelable(null));
            credential.setCertCredential((CertificateCredential) in.readParcelable(null));
            credential.setSimCredential((SimCredential) in.readParcelable(null));
            credential.setCaCertificate(ParcelUtil.readCertificate(in));
            credential.setClientCertificateChain(ParcelUtil.readCertificates(in));
            credential.setClientPrivateKey(ParcelUtil.readPrivateKey(in));
            return credential;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Credential[] newArray(int size) {
            return new Credential[size];
        }
    };
    private static final int MAX_REALM_BYTES = 253;
    private static final String TAG = "Credential";
    private X509Certificate mCaCertificate;
    private CertificateCredential mCertCredential;
    private boolean mCheckAaaServerCertStatus;
    private X509Certificate[] mClientCertificateChain;
    private PrivateKey mClientPrivateKey;
    private long mCreationTimeInMillis;
    private long mExpirationTimeInMillis;
    private String mRealm;
    private SimCredential mSimCredential;
    private UserCredential mUserCredential;

    public synchronized void setCreationTimeInMillis(long creationTimeInMillis) {
        this.mCreationTimeInMillis = creationTimeInMillis;
    }

    public synchronized long getCreationTimeInMillis() {
        return this.mCreationTimeInMillis;
    }

    public synchronized void setExpirationTimeInMillis(long expirationTimeInMillis) {
        this.mExpirationTimeInMillis = expirationTimeInMillis;
    }

    public synchronized long getExpirationTimeInMillis() {
        return this.mExpirationTimeInMillis;
    }

    public void setRealm(String realm) {
        this.mRealm = realm;
    }

    public String getRealm() {
        return this.mRealm;
    }

    public synchronized void setCheckAaaServerCertStatus(boolean checkAaaServerCertStatus) {
        this.mCheckAaaServerCertStatus = checkAaaServerCertStatus;
    }

    public synchronized boolean getCheckAaaServerCertStatus() {
        return this.mCheckAaaServerCertStatus;
    }

    /* loaded from: classes2.dex */
    public static final class UserCredential implements Parcelable {
        private static final int MAX_PASSWORD_BYTES = 255;
        private static final int MAX_USERNAME_BYTES = 63;
        private boolean mAbleToShare;
        private int mEapType;
        private boolean mMachineManaged;
        private String mNonEapInnerMethod;
        private String mPassword;
        private String mSoftTokenApp;
        private String mUsername;
        public static final String AUTH_METHOD_PAP = "PAP";
        public static final String AUTH_METHOD_MSCHAP = "MS-CHAP";
        public static final String AUTH_METHOD_MSCHAPV2 = "MS-CHAP-V2";
        private static final Set<String> SUPPORTED_AUTH = new HashSet(Arrays.asList(AUTH_METHOD_PAP, AUTH_METHOD_MSCHAP, AUTH_METHOD_MSCHAPV2));
        public static final Parcelable.Creator<UserCredential> CREATOR = new Parcelable.Creator<UserCredential>() { // from class: android.net.wifi.hotspot2.pps.Credential.UserCredential.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public UserCredential createFromParcel(Parcel in) {
                UserCredential userCredential = new UserCredential();
                userCredential.setUsername(in.readString());
                userCredential.setPassword(in.readString());
                userCredential.setMachineManaged(in.readInt() != 0);
                userCredential.setSoftTokenApp(in.readString());
                userCredential.setAbleToShare(in.readInt() != 0);
                userCredential.setEapType(in.readInt());
                userCredential.setNonEapInnerMethod(in.readString());
                return userCredential;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public UserCredential[] newArray(int size) {
                return new UserCredential[size];
            }
        };

        public void setUsername(String username) {
            this.mUsername = username;
        }

        public String getUsername() {
            return this.mUsername;
        }

        public void setPassword(String password) {
            this.mPassword = password;
        }

        public String getPassword() {
            return this.mPassword;
        }

        public synchronized void setMachineManaged(boolean machineManaged) {
            this.mMachineManaged = machineManaged;
        }

        public synchronized boolean getMachineManaged() {
            return this.mMachineManaged;
        }

        public synchronized void setSoftTokenApp(String softTokenApp) {
            this.mSoftTokenApp = softTokenApp;
        }

        public synchronized String getSoftTokenApp() {
            return this.mSoftTokenApp;
        }

        public synchronized void setAbleToShare(boolean ableToShare) {
            this.mAbleToShare = ableToShare;
        }

        public synchronized boolean getAbleToShare() {
            return this.mAbleToShare;
        }

        public void setEapType(int eapType) {
            this.mEapType = eapType;
        }

        public int getEapType() {
            return this.mEapType;
        }

        public void setNonEapInnerMethod(String nonEapInnerMethod) {
            this.mNonEapInnerMethod = nonEapInnerMethod;
        }

        public String getNonEapInnerMethod() {
            return this.mNonEapInnerMethod;
        }

        public UserCredential() {
            this.mUsername = null;
            this.mPassword = null;
            this.mMachineManaged = false;
            this.mSoftTokenApp = null;
            this.mAbleToShare = false;
            this.mEapType = Integer.MIN_VALUE;
            this.mNonEapInnerMethod = null;
        }

        public UserCredential(UserCredential source) {
            this.mUsername = null;
            this.mPassword = null;
            this.mMachineManaged = false;
            this.mSoftTokenApp = null;
            this.mAbleToShare = false;
            this.mEapType = Integer.MIN_VALUE;
            this.mNonEapInnerMethod = null;
            if (source != null) {
                this.mUsername = source.mUsername;
                this.mPassword = source.mPassword;
                this.mMachineManaged = source.mMachineManaged;
                this.mSoftTokenApp = source.mSoftTokenApp;
                this.mAbleToShare = source.mAbleToShare;
                this.mEapType = source.mEapType;
                this.mNonEapInnerMethod = source.mNonEapInnerMethod;
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mUsername);
            dest.writeString(this.mPassword);
            dest.writeInt(this.mMachineManaged ? 1 : 0);
            dest.writeString(this.mSoftTokenApp);
            dest.writeInt(this.mAbleToShare ? 1 : 0);
            dest.writeInt(this.mEapType);
            dest.writeString(this.mNonEapInnerMethod);
        }

        public boolean equals(Object thatObject) {
            if (this == thatObject) {
                return true;
            }
            if (thatObject instanceof UserCredential) {
                UserCredential that = (UserCredential) thatObject;
                return TextUtils.equals(this.mUsername, that.mUsername) && TextUtils.equals(this.mPassword, that.mPassword) && this.mMachineManaged == that.mMachineManaged && TextUtils.equals(this.mSoftTokenApp, that.mSoftTokenApp) && this.mAbleToShare == that.mAbleToShare && this.mEapType == that.mEapType && TextUtils.equals(this.mNonEapInnerMethod, that.mNonEapInnerMethod);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.mUsername, this.mPassword, Boolean.valueOf(this.mMachineManaged), this.mSoftTokenApp, Boolean.valueOf(this.mAbleToShare), Integer.valueOf(this.mEapType), this.mNonEapInnerMethod);
        }

        public String toString() {
            return "Username: " + this.mUsername + "\nMachineManaged: " + this.mMachineManaged + "\nSoftTokenApp: " + this.mSoftTokenApp + "\nAbleToShare: " + this.mAbleToShare + "\nEAPType: " + this.mEapType + "\nAuthMethod: " + this.mNonEapInnerMethod + "\n";
        }

        public synchronized boolean validate() {
            if (TextUtils.isEmpty(this.mUsername)) {
                Log.d(Credential.TAG, "Missing username");
                return false;
            } else if (this.mUsername.getBytes(StandardCharsets.UTF_8).length > 63) {
                Log.d(Credential.TAG, "username exceeding maximum length: " + this.mUsername.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else if (TextUtils.isEmpty(this.mPassword)) {
                Log.d(Credential.TAG, "Missing password");
                return false;
            } else if (this.mPassword.getBytes(StandardCharsets.UTF_8).length > 255) {
                Log.d(Credential.TAG, "password exceeding maximum length: " + this.mPassword.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else if (this.mEapType != 21) {
                Log.d(Credential.TAG, "Invalid EAP Type for user credential: " + this.mEapType);
                return false;
            } else if (!SUPPORTED_AUTH.contains(this.mNonEapInnerMethod)) {
                Log.d(Credential.TAG, "Invalid non-EAP inner method for EAP-TTLS: " + this.mNonEapInnerMethod);
                return false;
            } else {
                return true;
            }
        }
    }

    public void setUserCredential(UserCredential userCredential) {
        this.mUserCredential = userCredential;
    }

    public UserCredential getUserCredential() {
        return this.mUserCredential;
    }

    /* loaded from: classes2.dex */
    public static final class CertificateCredential implements Parcelable {
        private static final int CERT_SHA256_FINGER_PRINT_LENGTH = 32;
        public static final String CERT_TYPE_X509V3 = "x509v3";
        public static final Parcelable.Creator<CertificateCredential> CREATOR = new Parcelable.Creator<CertificateCredential>() { // from class: android.net.wifi.hotspot2.pps.Credential.CertificateCredential.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public CertificateCredential createFromParcel(Parcel in) {
                CertificateCredential certCredential = new CertificateCredential();
                certCredential.setCertType(in.readString());
                certCredential.setCertSha256Fingerprint(in.createByteArray());
                return certCredential;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public CertificateCredential[] newArray(int size) {
                return new CertificateCredential[size];
            }
        };
        private byte[] mCertSha256Fingerprint;
        private String mCertType;

        public void setCertType(String certType) {
            this.mCertType = certType;
        }

        public String getCertType() {
            return this.mCertType;
        }

        public void setCertSha256Fingerprint(byte[] certSha256Fingerprint) {
            this.mCertSha256Fingerprint = certSha256Fingerprint;
        }

        public byte[] getCertSha256Fingerprint() {
            return this.mCertSha256Fingerprint;
        }

        public CertificateCredential() {
            this.mCertType = null;
            this.mCertSha256Fingerprint = null;
        }

        public CertificateCredential(CertificateCredential source) {
            this.mCertType = null;
            this.mCertSha256Fingerprint = null;
            if (source != null) {
                this.mCertType = source.mCertType;
                if (source.mCertSha256Fingerprint != null) {
                    this.mCertSha256Fingerprint = Arrays.copyOf(source.mCertSha256Fingerprint, source.mCertSha256Fingerprint.length);
                }
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mCertType);
            dest.writeByteArray(this.mCertSha256Fingerprint);
        }

        public boolean equals(Object thatObject) {
            if (this == thatObject) {
                return true;
            }
            if (thatObject instanceof CertificateCredential) {
                CertificateCredential that = (CertificateCredential) thatObject;
                return TextUtils.equals(this.mCertType, that.mCertType) && Arrays.equals(this.mCertSha256Fingerprint, that.mCertSha256Fingerprint);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.mCertType, this.mCertSha256Fingerprint);
        }

        public String toString() {
            return "CertificateType: " + this.mCertType + "\n";
        }

        public synchronized boolean validate() {
            if (!TextUtils.equals(CERT_TYPE_X509V3, this.mCertType)) {
                Log.d(Credential.TAG, "Unsupported certificate type: " + this.mCertType);
                return false;
            } else if (this.mCertSha256Fingerprint == null || this.mCertSha256Fingerprint.length != 32) {
                Log.d(Credential.TAG, "Invalid SHA-256 fingerprint");
                return false;
            } else {
                return true;
            }
        }
    }

    public void setCertCredential(CertificateCredential certCredential) {
        this.mCertCredential = certCredential;
    }

    public CertificateCredential getCertCredential() {
        return this.mCertCredential;
    }

    /* loaded from: classes2.dex */
    public static final class SimCredential implements Parcelable {
        public static final Parcelable.Creator<SimCredential> CREATOR = new Parcelable.Creator<SimCredential>() { // from class: android.net.wifi.hotspot2.pps.Credential.SimCredential.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SimCredential createFromParcel(Parcel in) {
                SimCredential simCredential = new SimCredential();
                simCredential.setImsi(in.readString());
                simCredential.setEapType(in.readInt());
                return simCredential;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SimCredential[] newArray(int size) {
                return new SimCredential[size];
            }
        };
        private static final int MAX_IMSI_LENGTH = 15;
        private int mEapType;
        private String mImsi;

        public void setImsi(String imsi) {
            this.mImsi = imsi;
        }

        public String getImsi() {
            return this.mImsi;
        }

        public void setEapType(int eapType) {
            this.mEapType = eapType;
        }

        public int getEapType() {
            return this.mEapType;
        }

        public SimCredential() {
            this.mImsi = null;
            this.mEapType = Integer.MIN_VALUE;
        }

        public SimCredential(SimCredential source) {
            this.mImsi = null;
            this.mEapType = Integer.MIN_VALUE;
            if (source != null) {
                this.mImsi = source.mImsi;
                this.mEapType = source.mEapType;
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public boolean equals(Object thatObject) {
            if (this == thatObject) {
                return true;
            }
            if (thatObject instanceof SimCredential) {
                SimCredential that = (SimCredential) thatObject;
                return TextUtils.equals(this.mImsi, that.mImsi) && this.mEapType == that.mEapType;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.mImsi, Integer.valueOf(this.mEapType));
        }

        public String toString() {
            return "IMSI: " + this.mImsi + "\nEAPType: " + this.mEapType + "\n";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mImsi);
            dest.writeInt(this.mEapType);
        }

        public synchronized boolean validate() {
            if (verifyImsi()) {
                if (this.mEapType != 18 && this.mEapType != 23 && this.mEapType != 50) {
                    Log.d(Credential.TAG, "Invalid EAP Type for SIM credential: " + this.mEapType);
                    return false;
                }
                return true;
            }
            return false;
        }

        private synchronized boolean verifyImsi() {
            if (TextUtils.isEmpty(this.mImsi)) {
                Log.d(Credential.TAG, "Missing IMSI");
                return false;
            } else if (this.mImsi.length() > 15) {
                Log.d(Credential.TAG, "IMSI exceeding maximum length: " + this.mImsi.length());
                return false;
            } else {
                char stopChar = 0;
                int nonDigit = 0;
                while (nonDigit < this.mImsi.length() && (stopChar = this.mImsi.charAt(nonDigit)) >= '0' && stopChar <= '9') {
                    nonDigit++;
                }
                if (nonDigit == this.mImsi.length()) {
                    return true;
                }
                return nonDigit == this.mImsi.length() - 1 && stopChar == '*';
            }
        }
    }

    public void setSimCredential(SimCredential simCredential) {
        this.mSimCredential = simCredential;
    }

    public SimCredential getSimCredential() {
        return this.mSimCredential;
    }

    public void setCaCertificate(X509Certificate caCertificate) {
        this.mCaCertificate = caCertificate;
    }

    public X509Certificate getCaCertificate() {
        return this.mCaCertificate;
    }

    public void setClientCertificateChain(X509Certificate[] certificateChain) {
        this.mClientCertificateChain = certificateChain;
    }

    public X509Certificate[] getClientCertificateChain() {
        return this.mClientCertificateChain;
    }

    public void setClientPrivateKey(PrivateKey clientPrivateKey) {
        this.mClientPrivateKey = clientPrivateKey;
    }

    public PrivateKey getClientPrivateKey() {
        return this.mClientPrivateKey;
    }

    public Credential() {
        this.mCreationTimeInMillis = Long.MIN_VALUE;
        this.mExpirationTimeInMillis = Long.MIN_VALUE;
        this.mRealm = null;
        this.mCheckAaaServerCertStatus = false;
        this.mUserCredential = null;
        this.mCertCredential = null;
        this.mSimCredential = null;
        this.mCaCertificate = null;
        this.mClientCertificateChain = null;
        this.mClientPrivateKey = null;
    }

    public Credential(Credential source) {
        this.mCreationTimeInMillis = Long.MIN_VALUE;
        this.mExpirationTimeInMillis = Long.MIN_VALUE;
        this.mRealm = null;
        this.mCheckAaaServerCertStatus = false;
        this.mUserCredential = null;
        this.mCertCredential = null;
        this.mSimCredential = null;
        this.mCaCertificate = null;
        this.mClientCertificateChain = null;
        this.mClientPrivateKey = null;
        if (source != null) {
            this.mCreationTimeInMillis = source.mCreationTimeInMillis;
            this.mExpirationTimeInMillis = source.mExpirationTimeInMillis;
            this.mRealm = source.mRealm;
            this.mCheckAaaServerCertStatus = source.mCheckAaaServerCertStatus;
            if (source.mUserCredential != null) {
                this.mUserCredential = new UserCredential(source.mUserCredential);
            }
            if (source.mCertCredential != null) {
                this.mCertCredential = new CertificateCredential(source.mCertCredential);
            }
            if (source.mSimCredential != null) {
                this.mSimCredential = new SimCredential(source.mSimCredential);
            }
            if (source.mClientCertificateChain != null) {
                this.mClientCertificateChain = (X509Certificate[]) Arrays.copyOf(source.mClientCertificateChain, source.mClientCertificateChain.length);
            }
            this.mCaCertificate = source.mCaCertificate;
            this.mClientPrivateKey = source.mClientPrivateKey;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mCreationTimeInMillis);
        dest.writeLong(this.mExpirationTimeInMillis);
        dest.writeString(this.mRealm);
        dest.writeInt(this.mCheckAaaServerCertStatus ? 1 : 0);
        dest.writeParcelable(this.mUserCredential, flags);
        dest.writeParcelable(this.mCertCredential, flags);
        dest.writeParcelable(this.mSimCredential, flags);
        ParcelUtil.writeCertificate(dest, this.mCaCertificate);
        ParcelUtil.writeCertificates(dest, this.mClientCertificateChain);
        ParcelUtil.writePrivateKey(dest, this.mClientPrivateKey);
    }

    public boolean equals(Object thatObject) {
        if (this == thatObject) {
            return true;
        }
        if (thatObject instanceof Credential) {
            Credential that = (Credential) thatObject;
            return TextUtils.equals(this.mRealm, that.mRealm) && this.mCreationTimeInMillis == that.mCreationTimeInMillis && this.mExpirationTimeInMillis == that.mExpirationTimeInMillis && this.mCheckAaaServerCertStatus == that.mCheckAaaServerCertStatus && (this.mUserCredential != null ? this.mUserCredential.equals(that.mUserCredential) : that.mUserCredential == null) && (this.mCertCredential != null ? this.mCertCredential.equals(that.mCertCredential) : that.mCertCredential == null) && (this.mSimCredential != null ? this.mSimCredential.equals(that.mSimCredential) : that.mSimCredential == null) && isX509CertificateEquals(this.mCaCertificate, that.mCaCertificate) && isX509CertificatesEquals(this.mClientCertificateChain, that.mClientCertificateChain) && isPrivateKeyEquals(this.mClientPrivateKey, that.mClientPrivateKey);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mRealm, Long.valueOf(this.mCreationTimeInMillis), Long.valueOf(this.mExpirationTimeInMillis), Boolean.valueOf(this.mCheckAaaServerCertStatus), this.mUserCredential, this.mCertCredential, this.mSimCredential, this.mCaCertificate, this.mClientCertificateChain, this.mClientPrivateKey);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Realm: ");
        builder.append(this.mRealm);
        builder.append("\n");
        builder.append("CreationTime: ");
        builder.append(this.mCreationTimeInMillis != Long.MIN_VALUE ? new Date(this.mCreationTimeInMillis) : "Not specified");
        builder.append("\n");
        builder.append("ExpirationTime: ");
        builder.append(this.mExpirationTimeInMillis != Long.MIN_VALUE ? new Date(this.mExpirationTimeInMillis) : "Not specified");
        builder.append("\n");
        builder.append("CheckAAAServerStatus: ");
        builder.append(this.mCheckAaaServerCertStatus);
        builder.append("\n");
        if (this.mUserCredential != null) {
            builder.append("UserCredential Begin ---\n");
            builder.append(this.mUserCredential);
            builder.append("UserCredential End ---\n");
        }
        if (this.mCertCredential != null) {
            builder.append("CertificateCredential Begin ---\n");
            builder.append(this.mCertCredential);
            builder.append("CertificateCredential End ---\n");
        }
        if (this.mSimCredential != null) {
            builder.append("SIMCredential Begin ---\n");
            builder.append(this.mSimCredential);
            builder.append("SIMCredential End ---\n");
        }
        return builder.toString();
    }

    public synchronized boolean validate() {
        if (TextUtils.isEmpty(this.mRealm)) {
            Log.d(TAG, "Missing realm");
            return false;
        } else if (this.mRealm.getBytes(StandardCharsets.UTF_8).length > 253) {
            Log.d(TAG, "realm exceeding maximum length: " + this.mRealm.getBytes(StandardCharsets.UTF_8).length);
            return false;
        } else if (this.mUserCredential != null) {
            return verifyUserCredential();
        } else if (this.mCertCredential != null) {
            return verifyCertCredential();
        } else if (this.mSimCredential != null) {
            return verifySimCredential();
        } else {
            Log.d(TAG, "Missing required credential");
            return false;
        }
    }

    private synchronized boolean verifyUserCredential() {
        if (this.mUserCredential == null) {
            Log.d(TAG, "Missing user credential");
            return false;
        } else if (this.mCertCredential != null || this.mSimCredential != null) {
            Log.d(TAG, "Contained more than one type of credential");
            return false;
        } else if (this.mUserCredential.validate()) {
            if (this.mCaCertificate == null) {
                Log.d(TAG, "Missing CA Certificate for user credential");
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private synchronized boolean verifyCertCredential() {
        if (this.mCertCredential == null) {
            Log.d(TAG, "Missing certificate credential");
            return false;
        } else if (this.mUserCredential != null || this.mSimCredential != null) {
            Log.d(TAG, "Contained more than one type of credential");
            return false;
        } else if (this.mCertCredential.validate()) {
            if (this.mCaCertificate == null) {
                Log.d(TAG, "Missing CA Certificate for certificate credential");
                return false;
            } else if (this.mClientPrivateKey == null) {
                Log.d(TAG, "Missing client private key for certificate credential");
                return false;
            } else {
                try {
                    if (!verifySha256Fingerprint(this.mClientCertificateChain, this.mCertCredential.getCertSha256Fingerprint())) {
                        Log.d(TAG, "SHA-256 fingerprint mismatch");
                        return false;
                    }
                    return true;
                } catch (NoSuchAlgorithmException | CertificateEncodingException e) {
                    Log.d(TAG, "Failed to verify SHA-256 fingerprint: " + e.getMessage());
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private synchronized boolean verifySimCredential() {
        if (this.mSimCredential == null) {
            Log.d(TAG, "Missing SIM credential");
            return false;
        } else if (this.mUserCredential != null || this.mCertCredential != null) {
            Log.d(TAG, "Contained more than one type of credential");
            return false;
        } else {
            return this.mSimCredential.validate();
        }
    }

    private static synchronized boolean isPrivateKeyEquals(PrivateKey key1, PrivateKey key2) {
        if (key1 == null && key2 == null) {
            return true;
        }
        if (key1 == null || key2 == null) {
            return false;
        }
        if (TextUtils.equals(key1.getAlgorithm(), key2.getAlgorithm()) && Arrays.equals(key1.getEncoded(), key2.getEncoded())) {
            return true;
        }
        return false;
    }

    private static synchronized boolean isX509CertificateEquals(X509Certificate cert1, X509Certificate cert2) {
        if (cert1 == null && cert2 == null) {
            return true;
        }
        if (cert1 == null || cert2 == null) {
            return false;
        }
        try {
            boolean result = Arrays.equals(cert1.getEncoded(), cert2.getEncoded());
            return result;
        } catch (CertificateEncodingException e) {
            return false;
        }
    }

    private static synchronized boolean isX509CertificatesEquals(X509Certificate[] certs1, X509Certificate[] certs2) {
        if (certs1 == null && certs2 == null) {
            return true;
        }
        if (certs1 == null || certs2 == null || certs1.length != certs2.length) {
            return false;
        }
        for (int i = 0; i < certs1.length; i++) {
            if (!isX509CertificateEquals(certs1[i], certs2[i])) {
                return false;
            }
        }
        return true;
    }

    private static synchronized boolean verifySha256Fingerprint(X509Certificate[] certChain, byte[] expectedFingerprint) throws NoSuchAlgorithmException, CertificateEncodingException {
        if (certChain == null) {
            return false;
        }
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        for (X509Certificate certificate : certChain) {
            digester.reset();
            byte[] fingerprint = digester.digest(certificate.getEncoded());
            if (Arrays.equals(expectedFingerprint, fingerprint)) {
                return true;
            }
        }
        return false;
    }
}
