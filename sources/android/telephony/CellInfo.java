package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public abstract class CellInfo implements Parcelable {
    public static final int CONNECTION_NONE = 0;
    public static final int CONNECTION_PRIMARY_SERVING = 1;
    public static final int CONNECTION_SECONDARY_SERVING = 2;
    public static final int CONNECTION_UNKNOWN = Integer.MAX_VALUE;
    public static final Parcelable.Creator<CellInfo> CREATOR = new Parcelable.Creator<CellInfo>() { // from class: android.telephony.CellInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellInfo createFromParcel(Parcel in) {
            int type = in.readInt();
            switch (type) {
                case 1:
                    return CellInfoGsm.createFromParcelBody(in);
                case 2:
                    return CellInfoCdma.createFromParcelBody(in);
                case 3:
                    return CellInfoLte.createFromParcelBody(in);
                case 4:
                    return CellInfoWcdma.createFromParcelBody(in);
                case 5:
                    return CellInfoTdscdma.createFromParcelBody(in);
                case 6:
                    return CellInfoNr.createFromParcelBody(in);
                default:
                    throw new RuntimeException("Bad CellInfo Parcel");
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellInfo[] newArray(int size) {
            return new CellInfo[size];
        }
    };
    @UnsupportedAppUsage
    public static final int TIMESTAMP_TYPE_ANTENNA = 1;
    @UnsupportedAppUsage
    public static final int TIMESTAMP_TYPE_JAVA_RIL = 4;
    @UnsupportedAppUsage
    public static final int TIMESTAMP_TYPE_MODEM = 2;
    @UnsupportedAppUsage
    public static final int TIMESTAMP_TYPE_OEM_RIL = 3;
    @UnsupportedAppUsage
    public static final int TIMESTAMP_TYPE_UNKNOWN = 0;
    public static final int TYPE_CDMA = 2;
    public static final int TYPE_GSM = 1;
    public static final int TYPE_LTE = 3;
    public static final int TYPE_NR = 6;
    public static final int TYPE_TDSCDMA = 5;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_WCDMA = 4;
    public static final int UNAVAILABLE = Integer.MAX_VALUE;
    public static final long UNAVAILABLE_LONG = Long.MAX_VALUE;
    private int mCellConnectionStatus;
    private boolean mRegistered;
    private long mTimeStamp;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface CellConnectionStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Type {
    }

    public abstract CellIdentity getCellIdentity();

    public abstract CellSignalStrength getCellSignalStrength();

    @Override // android.os.Parcelable
    public abstract void writeToParcel(Parcel parcel, int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public CellInfo() {
        this.mRegistered = false;
        this.mTimeStamp = Long.MAX_VALUE;
        this.mCellConnectionStatus = 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CellInfo(CellInfo ci) {
        this.mRegistered = ci.mRegistered;
        this.mTimeStamp = ci.mTimeStamp;
        this.mCellConnectionStatus = ci.mCellConnectionStatus;
    }

    public boolean isRegistered() {
        return this.mRegistered;
    }

    public void setRegistered(boolean registered) {
        this.mRegistered = registered;
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    @VisibleForTesting
    public void setTimeStamp(long ts) {
        this.mTimeStamp = ts;
    }

    public CellInfo sanitizeLocationInfo() {
        return null;
    }

    public int getCellConnectionStatus() {
        return this.mCellConnectionStatus;
    }

    public void setCellConnectionStatus(int cellConnectionStatus) {
        this.mCellConnectionStatus = cellConnectionStatus;
    }

    public int hashCode() {
        return ((!this.mRegistered ? 1 : 0) * 31) + (((int) (this.mTimeStamp / 1000)) * 31) + (this.mCellConnectionStatus * 31);
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        try {
            CellInfo o = (CellInfo) other;
            if (this.mRegistered == o.mRegistered && this.mTimeStamp == o.mTimeStamp) {
                if (this.mCellConnectionStatus != o.mCellConnectionStatus) {
                    return false;
                }
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("mRegistered=");
        sb.append(this.mRegistered ? "YES" : "NO");
        sb.append(" mTimeStamp=");
        sb.append(this.mTimeStamp);
        sb.append("ns");
        sb.append(" mCellConnectionStatus=");
        sb.append(this.mCellConnectionStatus);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeToParcel(Parcel dest, int flags, int type) {
        dest.writeInt(type);
        dest.writeInt(this.mRegistered ? 1 : 0);
        dest.writeLong(this.mTimeStamp);
        dest.writeInt(this.mCellConnectionStatus);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CellInfo(Parcel in) {
        this.mRegistered = in.readInt() == 1;
        this.mTimeStamp = in.readLong();
        this.mCellConnectionStatus = in.readInt();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CellInfo(android.hardware.radio.V1_0.CellInfo ci) {
        this.mRegistered = ci.registered;
        this.mTimeStamp = ci.timeStamp;
        this.mCellConnectionStatus = Integer.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CellInfo(android.hardware.radio.V1_2.CellInfo ci) {
        this.mRegistered = ci.registered;
        this.mTimeStamp = ci.timeStamp;
        this.mCellConnectionStatus = ci.connectionStatus;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CellInfo(android.hardware.radio.V1_4.CellInfo ci, long timeStamp) {
        this.mRegistered = ci.isRegistered;
        this.mTimeStamp = timeStamp;
        this.mCellConnectionStatus = ci.connectionStatus;
    }

    public static CellInfo create(android.hardware.radio.V1_0.CellInfo ci) {
        if (ci == null) {
            return null;
        }
        int i = ci.cellInfoType;
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i != 4) {
                        if (i != 5) {
                            return null;
                        }
                        return new CellInfoTdscdma(ci);
                    }
                    return new CellInfoWcdma(ci);
                }
                return new CellInfoLte(ci);
            }
            return new CellInfoCdma(ci);
        }
        return new CellInfoGsm(ci);
    }

    public static CellInfo create(android.hardware.radio.V1_2.CellInfo ci) {
        if (ci == null) {
            return null;
        }
        int i = ci.cellInfoType;
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i != 4) {
                        if (i != 5) {
                            return null;
                        }
                        return new CellInfoTdscdma(ci);
                    }
                    return new CellInfoWcdma(ci);
                }
                return new CellInfoLte(ci);
            }
            return new CellInfoCdma(ci);
        }
        return new CellInfoGsm(ci);
    }

    public static CellInfo create(android.hardware.radio.V1_4.CellInfo ci, long timeStamp) {
        if (ci == null) {
            return null;
        }
        byte discriminator = ci.info.getDiscriminator();
        if (discriminator != 0) {
            if (discriminator != 1) {
                if (discriminator != 2) {
                    if (discriminator != 3) {
                        if (discriminator != 4) {
                            return null;
                        }
                        return new CellInfoLte(ci, timeStamp);
                    }
                    return new CellInfoTdscdma(ci, timeStamp);
                }
                return new CellInfoWcdma(ci, timeStamp);
            }
            return new CellInfoCdma(ci, timeStamp);
        }
        return new CellInfoGsm(ci, timeStamp);
    }
}
