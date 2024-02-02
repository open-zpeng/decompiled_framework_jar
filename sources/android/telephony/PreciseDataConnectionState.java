package android.telephony;

import android.net.LinkProperties;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class PreciseDataConnectionState implements Parcelable {
    public static final Parcelable.Creator<PreciseDataConnectionState> CREATOR = new Parcelable.Creator<PreciseDataConnectionState>() { // from class: android.telephony.PreciseDataConnectionState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PreciseDataConnectionState createFromParcel(Parcel in) {
            return new PreciseDataConnectionState(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PreciseDataConnectionState[] newArray(int size) {
            return new PreciseDataConnectionState[size];
        }
    };
    private String mAPN;
    private String mAPNType;
    private String mFailCause;
    private LinkProperties mLinkProperties;
    private int mNetworkType;
    private String mReason;
    private int mState;

    private protected PreciseDataConnectionState(int state, int networkType, String apnType, String apn, String reason, LinkProperties linkProperties, String failCause) {
        this.mState = -1;
        this.mNetworkType = 0;
        this.mAPNType = "";
        this.mAPN = "";
        this.mReason = "";
        this.mLinkProperties = null;
        this.mFailCause = "";
        this.mState = state;
        this.mNetworkType = networkType;
        this.mAPNType = apnType;
        this.mAPN = apn;
        this.mReason = reason;
        this.mLinkProperties = linkProperties;
        this.mFailCause = failCause;
    }

    public synchronized PreciseDataConnectionState() {
        this.mState = -1;
        this.mNetworkType = 0;
        this.mAPNType = "";
        this.mAPN = "";
        this.mReason = "";
        this.mLinkProperties = null;
        this.mFailCause = "";
    }

    private synchronized PreciseDataConnectionState(Parcel in) {
        this.mState = -1;
        this.mNetworkType = 0;
        this.mAPNType = "";
        this.mAPN = "";
        this.mReason = "";
        this.mLinkProperties = null;
        this.mFailCause = "";
        this.mState = in.readInt();
        this.mNetworkType = in.readInt();
        this.mAPNType = in.readString();
        this.mAPN = in.readString();
        this.mReason = in.readString();
        this.mLinkProperties = (LinkProperties) in.readParcelable(null);
        this.mFailCause = in.readString();
    }

    private protected int getDataConnectionState() {
        return this.mState;
    }

    private protected int getDataConnectionNetworkType() {
        return this.mNetworkType;
    }

    private protected String getDataConnectionAPNType() {
        return this.mAPNType;
    }

    private protected String getDataConnectionAPN() {
        return this.mAPN;
    }

    private protected String getDataConnectionChangeReason() {
        return this.mReason;
    }

    private protected LinkProperties getDataConnectionLinkProperties() {
        return this.mLinkProperties;
    }

    private protected String getDataConnectionFailCause() {
        return this.mFailCause;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mState);
        out.writeInt(this.mNetworkType);
        out.writeString(this.mAPNType);
        out.writeString(this.mAPN);
        out.writeString(this.mReason);
        out.writeParcelable(this.mLinkProperties, flags);
        out.writeString(this.mFailCause);
    }

    public int hashCode() {
        int result = (31 * 1) + this.mState;
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + this.mNetworkType)) + (this.mAPNType == null ? 0 : this.mAPNType.hashCode()))) + (this.mAPN == null ? 0 : this.mAPN.hashCode()))) + (this.mReason == null ? 0 : this.mReason.hashCode()))) + (this.mLinkProperties == null ? 0 : this.mLinkProperties.hashCode()))) + (this.mFailCause != null ? this.mFailCause.hashCode() : 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PreciseDataConnectionState other = (PreciseDataConnectionState) obj;
        if (this.mAPN == null) {
            if (other.mAPN != null) {
                return false;
            }
        } else if (!this.mAPN.equals(other.mAPN)) {
            return false;
        }
        if (this.mAPNType == null) {
            if (other.mAPNType != null) {
                return false;
            }
        } else if (!this.mAPNType.equals(other.mAPNType)) {
            return false;
        }
        if (this.mFailCause == null) {
            if (other.mFailCause != null) {
                return false;
            }
        } else if (!this.mFailCause.equals(other.mFailCause)) {
            return false;
        }
        if (this.mLinkProperties == null) {
            if (other.mLinkProperties != null) {
                return false;
            }
        } else if (!this.mLinkProperties.equals(other.mLinkProperties)) {
            return false;
        }
        if (this.mNetworkType != other.mNetworkType) {
            return false;
        }
        if (this.mReason == null) {
            if (other.mReason != null) {
                return false;
            }
        } else if (!this.mReason.equals(other.mReason)) {
            return false;
        }
        if (this.mState == other.mState) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Data Connection state: " + this.mState);
        sb.append(", Network type: " + this.mNetworkType);
        sb.append(", APN type: " + this.mAPNType);
        sb.append(", APN: " + this.mAPN);
        sb.append(", Change reason: " + this.mReason);
        sb.append(", Link properties: " + this.mLinkProperties);
        sb.append(", Fail cause: " + this.mFailCause);
        return sb.toString();
    }
}
