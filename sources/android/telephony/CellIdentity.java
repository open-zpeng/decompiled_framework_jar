package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
/* loaded from: classes2.dex */
public abstract class CellIdentity implements Parcelable {
    public static final Parcelable.Creator<CellIdentity> CREATOR = new Parcelable.Creator<CellIdentity>() { // from class: android.telephony.CellIdentity.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellIdentity createFromParcel(Parcel in) {
            int type = in.readInt();
            switch (type) {
                case 1:
                    return CellIdentityGsm.createFromParcelBody(in);
                case 2:
                    return CellIdentityCdma.createFromParcelBody(in);
                case 3:
                    return CellIdentityLte.createFromParcelBody(in);
                case 4:
                    return CellIdentityWcdma.createFromParcelBody(in);
                case 5:
                    return CellIdentityTdscdma.createFromParcelBody(in);
                default:
                    throw new IllegalArgumentException("Bad Cell identity Parcel");
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellIdentity[] newArray(int size) {
            return new CellIdentity[size];
        }
    };
    public static final int INVALID_CHANNEL_NUMBER = -1;
    public static final int TYPE_CDMA = 2;
    public static final int TYPE_GSM = 1;
    public static final int TYPE_LTE = 3;
    public static final int TYPE_TDSCDMA = 5;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_WCDMA = 4;
    protected final String mAlphaLong;
    protected final String mAlphaShort;
    protected final String mMccStr;
    protected final String mMncStr;
    protected final String mTag;
    protected final int mType;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Type {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized CellIdentity(String tag, int type, String mcc, String mnc, String alphal, String alphas) {
        this.mTag = tag;
        this.mType = type;
        if (mcc == null || mcc.matches("^[0-9]{3}$")) {
            this.mMccStr = mcc;
        } else if (mcc.isEmpty() || mcc.equals(String.valueOf(Integer.MAX_VALUE))) {
            this.mMccStr = null;
        } else {
            this.mMccStr = null;
            log("invalid MCC format: " + mcc);
        }
        if (mnc == null || mnc.matches("^[0-9]{2,3}$")) {
            this.mMncStr = mnc;
        } else if (mnc.isEmpty() || mnc.equals(String.valueOf(Integer.MAX_VALUE))) {
            this.mMncStr = null;
        } else {
            this.mMncStr = null;
            log("invalid MNC format: " + mnc);
        }
        this.mAlphaLong = alphal;
        this.mAlphaShort = alphas;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public synchronized int getType() {
        return this.mType;
    }

    public synchronized int getChannelNumber() {
        return -1;
    }

    public CharSequence getOperatorAlphaLong() {
        return this.mAlphaLong;
    }

    public CharSequence getOperatorAlphaShort() {
        return this.mAlphaShort;
    }

    public boolean equals(Object other) {
        if (other instanceof CellIdentity) {
            CellIdentity o = (CellIdentity) other;
            return TextUtils.equals(this.mAlphaLong, o.mAlphaLong) && TextUtils.equals(this.mAlphaShort, o.mAlphaShort);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mAlphaLong, this.mAlphaShort, this.mMccStr, this.mMncStr, Integer.valueOf(this.mType));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int type) {
        dest.writeInt(type);
        dest.writeString(this.mMccStr);
        dest.writeString(this.mMncStr);
        dest.writeString(this.mAlphaLong);
        dest.writeString(this.mAlphaShort);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized CellIdentity(String tag, int type, Parcel source) {
        this(tag, type, source.readString(), source.readString(), source.readString(), source.readString());
    }

    protected synchronized void log(String s) {
        Rlog.w(this.mTag, s);
    }
}
