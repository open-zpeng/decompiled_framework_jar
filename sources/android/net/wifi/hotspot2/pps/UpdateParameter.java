package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class UpdateParameter implements Parcelable {
    private static final int CERTIFICATE_SHA256_BYTES = 32;
    public static final Parcelable.Creator<UpdateParameter> CREATOR = new Parcelable.Creator<UpdateParameter>() { // from class: android.net.wifi.hotspot2.pps.UpdateParameter.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UpdateParameter createFromParcel(Parcel in) {
            UpdateParameter updateParam = new UpdateParameter();
            updateParam.setUpdateIntervalInMinutes(in.readLong());
            updateParam.setUpdateMethod(in.readString());
            updateParam.setRestriction(in.readString());
            updateParam.setServerUri(in.readString());
            updateParam.setUsername(in.readString());
            updateParam.setBase64EncodedPassword(in.readString());
            updateParam.setTrustRootCertUrl(in.readString());
            updateParam.setTrustRootCertSha256Fingerprint(in.createByteArray());
            return updateParam;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UpdateParameter[] newArray(int size) {
            return new UpdateParameter[size];
        }
    };
    private static final int MAX_PASSWORD_BYTES = 255;
    private static final int MAX_URI_BYTES = 1023;
    private static final int MAX_URL_BYTES = 1023;
    private static final int MAX_USERNAME_BYTES = 63;
    private static final String TAG = "UpdateParameter";
    public static final long UPDATE_CHECK_INTERVAL_NEVER = 4294967295L;
    public static final String UPDATE_METHOD_OMADM = "OMA-DM-ClientInitiated";
    public static final String UPDATE_METHOD_SSP = "SSP-ClientInitiated";
    public static final String UPDATE_RESTRICTION_HOMESP = "HomeSP";
    public static final String UPDATE_RESTRICTION_ROAMING_PARTNER = "RoamingPartner";
    public static final String UPDATE_RESTRICTION_UNRESTRICTED = "Unrestricted";
    private String mBase64EncodedPassword;
    private String mRestriction;
    private String mServerUri;
    private byte[] mTrustRootCertSha256Fingerprint;
    private String mTrustRootCertUrl;
    private long mUpdateIntervalInMinutes;
    private String mUpdateMethod;
    private String mUsername;

    public synchronized void setUpdateIntervalInMinutes(long updateIntervalInMinutes) {
        this.mUpdateIntervalInMinutes = updateIntervalInMinutes;
    }

    public synchronized long getUpdateIntervalInMinutes() {
        return this.mUpdateIntervalInMinutes;
    }

    public synchronized void setUpdateMethod(String updateMethod) {
        this.mUpdateMethod = updateMethod;
    }

    public synchronized String getUpdateMethod() {
        return this.mUpdateMethod;
    }

    public synchronized void setRestriction(String restriction) {
        this.mRestriction = restriction;
    }

    public synchronized String getRestriction() {
        return this.mRestriction;
    }

    public synchronized void setServerUri(String serverUri) {
        this.mServerUri = serverUri;
    }

    public synchronized String getServerUri() {
        return this.mServerUri;
    }

    public synchronized void setUsername(String username) {
        this.mUsername = username;
    }

    public synchronized String getUsername() {
        return this.mUsername;
    }

    public synchronized void setBase64EncodedPassword(String password) {
        this.mBase64EncodedPassword = password;
    }

    public synchronized String getBase64EncodedPassword() {
        return this.mBase64EncodedPassword;
    }

    public synchronized void setTrustRootCertUrl(String trustRootCertUrl) {
        this.mTrustRootCertUrl = trustRootCertUrl;
    }

    public synchronized String getTrustRootCertUrl() {
        return this.mTrustRootCertUrl;
    }

    public synchronized void setTrustRootCertSha256Fingerprint(byte[] fingerprint) {
        this.mTrustRootCertSha256Fingerprint = fingerprint;
    }

    public synchronized byte[] getTrustRootCertSha256Fingerprint() {
        return this.mTrustRootCertSha256Fingerprint;
    }

    public synchronized UpdateParameter() {
        this.mUpdateIntervalInMinutes = Long.MIN_VALUE;
        this.mUpdateMethod = null;
        this.mRestriction = null;
        this.mServerUri = null;
        this.mUsername = null;
        this.mBase64EncodedPassword = null;
        this.mTrustRootCertUrl = null;
        this.mTrustRootCertSha256Fingerprint = null;
    }

    public synchronized UpdateParameter(UpdateParameter source) {
        this.mUpdateIntervalInMinutes = Long.MIN_VALUE;
        this.mUpdateMethod = null;
        this.mRestriction = null;
        this.mServerUri = null;
        this.mUsername = null;
        this.mBase64EncodedPassword = null;
        this.mTrustRootCertUrl = null;
        this.mTrustRootCertSha256Fingerprint = null;
        if (source == null) {
            return;
        }
        this.mUpdateIntervalInMinutes = source.mUpdateIntervalInMinutes;
        this.mUpdateMethod = source.mUpdateMethod;
        this.mRestriction = source.mRestriction;
        this.mServerUri = source.mServerUri;
        this.mUsername = source.mUsername;
        this.mBase64EncodedPassword = source.mBase64EncodedPassword;
        this.mTrustRootCertUrl = source.mTrustRootCertUrl;
        if (source.mTrustRootCertSha256Fingerprint != null) {
            this.mTrustRootCertSha256Fingerprint = Arrays.copyOf(source.mTrustRootCertSha256Fingerprint, source.mTrustRootCertSha256Fingerprint.length);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mUpdateIntervalInMinutes);
        dest.writeString(this.mUpdateMethod);
        dest.writeString(this.mRestriction);
        dest.writeString(this.mServerUri);
        dest.writeString(this.mUsername);
        dest.writeString(this.mBase64EncodedPassword);
        dest.writeString(this.mTrustRootCertUrl);
        dest.writeByteArray(this.mTrustRootCertSha256Fingerprint);
    }

    public boolean equals(Object thatObject) {
        if (this == thatObject) {
            return true;
        }
        if (thatObject instanceof UpdateParameter) {
            UpdateParameter that = (UpdateParameter) thatObject;
            return this.mUpdateIntervalInMinutes == that.mUpdateIntervalInMinutes && TextUtils.equals(this.mUpdateMethod, that.mUpdateMethod) && TextUtils.equals(this.mRestriction, that.mRestriction) && TextUtils.equals(this.mServerUri, that.mServerUri) && TextUtils.equals(this.mUsername, that.mUsername) && TextUtils.equals(this.mBase64EncodedPassword, that.mBase64EncodedPassword) && TextUtils.equals(this.mTrustRootCertUrl, that.mTrustRootCertUrl) && Arrays.equals(this.mTrustRootCertSha256Fingerprint, that.mTrustRootCertSha256Fingerprint);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mUpdateIntervalInMinutes), this.mUpdateMethod, this.mRestriction, this.mServerUri, this.mUsername, this.mBase64EncodedPassword, this.mTrustRootCertUrl, this.mTrustRootCertSha256Fingerprint);
    }

    public String toString() {
        return "UpdateInterval: " + this.mUpdateIntervalInMinutes + "\nUpdateMethod: " + this.mUpdateMethod + "\nRestriction: " + this.mRestriction + "\nServerURI: " + this.mServerUri + "\nUsername: " + this.mUsername + "\nTrustRootCertURL: " + this.mTrustRootCertUrl + "\n";
    }

    public synchronized boolean validate() {
        if (this.mUpdateIntervalInMinutes == Long.MIN_VALUE) {
            Log.d(TAG, "Update interval not specified");
            return false;
        } else if (this.mUpdateIntervalInMinutes == 4294967295L) {
            return true;
        } else {
            if (!TextUtils.equals(this.mUpdateMethod, UPDATE_METHOD_OMADM) && !TextUtils.equals(this.mUpdateMethod, UPDATE_METHOD_SSP)) {
                Log.d(TAG, "Unknown update method: " + this.mUpdateMethod);
                return false;
            } else if (!TextUtils.equals(this.mRestriction, UPDATE_RESTRICTION_HOMESP) && !TextUtils.equals(this.mRestriction, UPDATE_RESTRICTION_ROAMING_PARTNER) && !TextUtils.equals(this.mRestriction, UPDATE_RESTRICTION_UNRESTRICTED)) {
                Log.d(TAG, "Unknown restriction: " + this.mRestriction);
                return false;
            } else if (TextUtils.isEmpty(this.mServerUri)) {
                Log.d(TAG, "Missing update server URI");
                return false;
            } else if (this.mServerUri.getBytes(StandardCharsets.UTF_8).length > 1023) {
                Log.d(TAG, "URI bytes exceeded the max: " + this.mServerUri.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else if (TextUtils.isEmpty(this.mUsername)) {
                Log.d(TAG, "Missing username");
                return false;
            } else if (this.mUsername.getBytes(StandardCharsets.UTF_8).length > 63) {
                Log.d(TAG, "Username bytes exceeded the max: " + this.mUsername.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else if (TextUtils.isEmpty(this.mBase64EncodedPassword)) {
                Log.d(TAG, "Missing username");
                return false;
            } else if (this.mBase64EncodedPassword.getBytes(StandardCharsets.UTF_8).length > 255) {
                Log.d(TAG, "Password bytes exceeded the max: " + this.mBase64EncodedPassword.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else {
                try {
                    Base64.decode(this.mBase64EncodedPassword, 0);
                    if (TextUtils.isEmpty(this.mTrustRootCertUrl)) {
                        Log.d(TAG, "Missing trust root certificate URL");
                        return false;
                    } else if (this.mTrustRootCertUrl.getBytes(StandardCharsets.UTF_8).length > 1023) {
                        Log.d(TAG, "Trust root cert URL bytes exceeded the max: " + this.mTrustRootCertUrl.getBytes(StandardCharsets.UTF_8).length);
                        return false;
                    } else if (this.mTrustRootCertSha256Fingerprint == null) {
                        Log.d(TAG, "Missing trust root certificate SHA-256 fingerprint");
                        return false;
                    } else if (this.mTrustRootCertSha256Fingerprint.length != 32) {
                        Log.d(TAG, "Incorrect size of trust root certificate SHA-256 fingerprint: " + this.mTrustRootCertSha256Fingerprint.length);
                        return false;
                    } else {
                        return true;
                    }
                } catch (IllegalArgumentException e) {
                    Log.d(TAG, "Invalid encoding for password: " + this.mBase64EncodedPassword);
                    return false;
                }
            }
        }
    }
}
