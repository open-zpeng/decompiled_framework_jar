package android.telephony.data;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
/* loaded from: classes2.dex */
public final class DataProfile implements Parcelable {
    public static final Parcelable.Creator<DataProfile> CREATOR = new Parcelable.Creator<DataProfile>() { // from class: android.telephony.data.DataProfile.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataProfile createFromParcel(Parcel source) {
            return new DataProfile(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataProfile[] newArray(int size) {
            return new DataProfile[size];
        }
    };
    public static final int TYPE_3GPP = 1;
    public static final int TYPE_3GPP2 = 2;
    public static final int TYPE_COMMON = 0;
    private final String mApn;
    private final int mAuthType;
    private final int mBearerBitmap;
    private final boolean mEnabled;
    private final int mMaxConns;
    private final int mMaxConnsTime;
    private final boolean mModemCognitive;
    private final int mMtu;
    private final String mMvnoMatchData;
    private final String mMvnoType;
    private final String mPassword;
    private final int mProfileId;
    private final String mProtocol;
    private final String mRoamingProtocol;
    private final int mSupportedApnTypesBitmap;
    private final int mType;
    private final String mUserName;
    private final int mWaitTime;

    public synchronized DataProfile(int profileId, String apn, String protocol, int authType, String userName, String password, int type, int maxConnsTime, int maxConns, int waitTime, boolean enabled, int supportedApnTypesBitmap, String roamingProtocol, int bearerBitmap, int mtu, String mvnoType, String mvnoMatchData, boolean modemCognitive) {
        int authType2;
        this.mProfileId = profileId;
        this.mApn = apn;
        this.mProtocol = protocol;
        if (authType == -1) {
            authType2 = TextUtils.isEmpty(userName) ? 0 : 3;
        } else {
            authType2 = authType;
        }
        this.mAuthType = authType2;
        this.mUserName = userName;
        this.mPassword = password;
        this.mType = type;
        this.mMaxConnsTime = maxConnsTime;
        this.mMaxConns = maxConns;
        this.mWaitTime = waitTime;
        this.mEnabled = enabled;
        this.mSupportedApnTypesBitmap = supportedApnTypesBitmap;
        this.mRoamingProtocol = roamingProtocol;
        this.mBearerBitmap = bearerBitmap;
        this.mMtu = mtu;
        this.mMvnoType = mvnoType;
        this.mMvnoMatchData = mvnoMatchData;
        this.mModemCognitive = modemCognitive;
    }

    public synchronized DataProfile(Parcel source) {
        this.mProfileId = source.readInt();
        this.mApn = source.readString();
        this.mProtocol = source.readString();
        this.mAuthType = source.readInt();
        this.mUserName = source.readString();
        this.mPassword = source.readString();
        this.mType = source.readInt();
        this.mMaxConnsTime = source.readInt();
        this.mMaxConns = source.readInt();
        this.mWaitTime = source.readInt();
        this.mEnabled = source.readBoolean();
        this.mSupportedApnTypesBitmap = source.readInt();
        this.mRoamingProtocol = source.readString();
        this.mBearerBitmap = source.readInt();
        this.mMtu = source.readInt();
        this.mMvnoType = source.readString();
        this.mMvnoMatchData = source.readString();
        this.mModemCognitive = source.readBoolean();
    }

    public synchronized int getProfileId() {
        return this.mProfileId;
    }

    public synchronized String getApn() {
        return this.mApn;
    }

    public synchronized String getProtocol() {
        return this.mProtocol;
    }

    public synchronized int getAuthType() {
        return this.mAuthType;
    }

    public synchronized String getUserName() {
        return this.mUserName;
    }

    public synchronized String getPassword() {
        return this.mPassword;
    }

    public synchronized int getType() {
        return this.mType;
    }

    public synchronized int getMaxConnsTime() {
        return this.mMaxConnsTime;
    }

    public synchronized int getMaxConns() {
        return this.mMaxConns;
    }

    public synchronized int getWaitTime() {
        return this.mWaitTime;
    }

    public synchronized boolean isEnabled() {
        return this.mEnabled;
    }

    public synchronized int getSupportedApnTypesBitmap() {
        return this.mSupportedApnTypesBitmap;
    }

    public synchronized String getRoamingProtocol() {
        return this.mRoamingProtocol;
    }

    public synchronized int getBearerBitmap() {
        return this.mBearerBitmap;
    }

    public synchronized int getMtu() {
        return this.mMtu;
    }

    public synchronized String getMvnoType() {
        return this.mMvnoType;
    }

    public synchronized String getMvnoMatchData() {
        return this.mMvnoMatchData;
    }

    public synchronized boolean isModemCognitive() {
        return this.mModemCognitive;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("DataProfile=");
        sb.append(this.mProfileId);
        sb.append("/");
        sb.append(this.mProtocol);
        sb.append("/");
        sb.append(this.mAuthType);
        sb.append("/");
        if (Build.IS_USER) {
            str = "***/***/***";
        } else {
            str = this.mApn + "/" + this.mUserName + "/" + this.mPassword;
        }
        sb.append(str);
        sb.append("/");
        sb.append(this.mType);
        sb.append("/");
        sb.append(this.mMaxConnsTime);
        sb.append("/");
        sb.append(this.mMaxConns);
        sb.append("/");
        sb.append(this.mWaitTime);
        sb.append("/");
        sb.append(this.mEnabled);
        sb.append("/");
        sb.append(this.mSupportedApnTypesBitmap);
        sb.append("/");
        sb.append(this.mRoamingProtocol);
        sb.append("/");
        sb.append(this.mBearerBitmap);
        sb.append("/");
        sb.append(this.mMtu);
        sb.append("/");
        sb.append(this.mMvnoType);
        sb.append("/");
        sb.append(this.mMvnoMatchData);
        sb.append("/");
        sb.append(this.mModemCognitive);
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof DataProfile) {
            return o == this || toString().equals(o.toString());
        }
        return false;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mProfileId);
        dest.writeString(this.mApn);
        dest.writeString(this.mProtocol);
        dest.writeInt(this.mAuthType);
        dest.writeString(this.mUserName);
        dest.writeString(this.mPassword);
        dest.writeInt(this.mType);
        dest.writeInt(this.mMaxConnsTime);
        dest.writeInt(this.mMaxConns);
        dest.writeInt(this.mWaitTime);
        dest.writeBoolean(this.mEnabled);
        dest.writeInt(this.mSupportedApnTypesBitmap);
        dest.writeString(this.mRoamingProtocol);
        dest.writeInt(this.mBearerBitmap);
        dest.writeInt(this.mMtu);
        dest.writeString(this.mMvnoType);
        dest.writeString(this.mMvnoMatchData);
        dest.writeBoolean(this.mModemCognitive);
    }
}
