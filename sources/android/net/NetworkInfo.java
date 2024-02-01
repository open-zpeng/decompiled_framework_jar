package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;
import java.util.EnumMap;

@Deprecated
/* loaded from: classes2.dex */
public class NetworkInfo implements Parcelable {
    public static final Parcelable.Creator<NetworkInfo> CREATOR;
    private static final EnumMap<DetailedState, State> stateMap = new EnumMap<>(DetailedState.class);
    private DetailedState mDetailedState;
    private String mExtraInfo;
    private boolean mIsAvailable;
    private boolean mIsFailover;
    private boolean mIsPersist;
    private boolean mIsRoaming;
    private int mNetworkType;
    private String mReason;
    private State mState;
    private int mSubtype;
    private String mSubtypeName;
    private String mTypeName;

    @Deprecated
    /* loaded from: classes2.dex */
    public enum DetailedState {
        IDLE,
        SCANNING,
        CONNECTING,
        AUTHENTICATING,
        OBTAINING_IPADDR,
        CONNECTED,
        SUSPENDED,
        DISCONNECTING,
        DISCONNECTED,
        FAILED,
        BLOCKED,
        VERIFYING_POOR_LINK,
        CAPTIVE_PORTAL_CHECK
    }

    @Deprecated
    /* loaded from: classes2.dex */
    public enum State {
        CONNECTING,
        CONNECTED,
        SUSPENDED,
        DISCONNECTING,
        DISCONNECTED,
        UNKNOWN
    }

    static {
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.IDLE, (DetailedState) State.DISCONNECTED);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.SCANNING, (DetailedState) State.CONNECTING);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.CONNECTING, (DetailedState) State.CONNECTING);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.AUTHENTICATING, (DetailedState) State.CONNECTING);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.OBTAINING_IPADDR, (DetailedState) State.CONNECTING);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.VERIFYING_POOR_LINK, (DetailedState) State.CONNECTING);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.CAPTIVE_PORTAL_CHECK, (DetailedState) State.CONNECTING);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.CONNECTED, (DetailedState) State.CONNECTED);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.SUSPENDED, (DetailedState) State.SUSPENDED);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.DISCONNECTING, (DetailedState) State.DISCONNECTING);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.DISCONNECTED, (DetailedState) State.DISCONNECTED);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.FAILED, (DetailedState) State.DISCONNECTED);
        stateMap.put((EnumMap<DetailedState, State>) DetailedState.BLOCKED, (DetailedState) State.DISCONNECTED);
        CREATOR = new Parcelable.Creator<NetworkInfo>() { // from class: android.net.NetworkInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public NetworkInfo createFromParcel(Parcel in) {
                int netType = in.readInt();
                int subtype = in.readInt();
                String typeName = in.readString();
                String subtypeName = in.readString();
                NetworkInfo netInfo = new NetworkInfo(netType, subtype, typeName, subtypeName);
                netInfo.mState = State.valueOf(in.readString());
                netInfo.mDetailedState = DetailedState.valueOf(in.readString());
                netInfo.mIsFailover = in.readInt() != 0;
                netInfo.mIsAvailable = in.readInt() != 0;
                netInfo.mIsRoaming = in.readInt() != 0;
                netInfo.mIsPersist = in.readInt() != 0;
                netInfo.mReason = in.readString();
                netInfo.mExtraInfo = in.readString();
                return netInfo;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public NetworkInfo[] newArray(int size) {
                return new NetworkInfo[size];
            }
        };
    }

    @UnsupportedAppUsage
    public NetworkInfo(int type, int subtype, String typeName, String subtypeName) {
        if (!ConnectivityManager.isNetworkTypeValid(type) && type != -1) {
            throw new IllegalArgumentException("Invalid network type: " + type);
        }
        this.mNetworkType = type;
        this.mSubtype = subtype;
        this.mTypeName = typeName;
        this.mSubtypeName = subtypeName;
        setDetailedState(DetailedState.IDLE, null, null);
        this.mState = State.UNKNOWN;
    }

    @UnsupportedAppUsage
    public NetworkInfo(NetworkInfo source) {
        if (source != null) {
            synchronized (source) {
                this.mNetworkType = source.mNetworkType;
                this.mSubtype = source.mSubtype;
                this.mTypeName = source.mTypeName;
                this.mSubtypeName = source.mSubtypeName;
                this.mState = source.mState;
                this.mDetailedState = source.mDetailedState;
                this.mReason = source.mReason;
                this.mExtraInfo = source.mExtraInfo;
                this.mIsFailover = source.mIsFailover;
                this.mIsAvailable = source.mIsAvailable;
                this.mIsRoaming = source.mIsRoaming;
                this.mIsPersist = source.mIsPersist;
            }
        }
    }

    @Deprecated
    public int getType() {
        int i;
        synchronized (this) {
            i = this.mNetworkType;
        }
        return i;
    }

    @Deprecated
    public void setType(int type) {
        synchronized (this) {
            this.mNetworkType = type;
        }
    }

    @Deprecated
    public int getSubtype() {
        int i;
        synchronized (this) {
            i = this.mSubtype;
        }
        return i;
    }

    @UnsupportedAppUsage
    public void setSubtype(int subtype, String subtypeName) {
        synchronized (this) {
            this.mSubtype = subtype;
            this.mSubtypeName = subtypeName;
        }
    }

    @Deprecated
    public String getTypeName() {
        String str;
        synchronized (this) {
            str = this.mTypeName;
        }
        return str;
    }

    @Deprecated
    public String getSubtypeName() {
        String str;
        synchronized (this) {
            str = this.mSubtypeName;
        }
        return str;
    }

    @Deprecated
    public boolean isConnectedOrConnecting() {
        boolean z;
        synchronized (this) {
            z = this.mState == State.CONNECTED || this.mState == State.CONNECTING;
        }
        return z;
    }

    @Deprecated
    public boolean isConnected() {
        boolean z;
        synchronized (this) {
            z = this.mState == State.CONNECTED;
        }
        return z;
    }

    @Deprecated
    public boolean isAvailable() {
        boolean z;
        synchronized (this) {
            z = this.mIsAvailable;
        }
        return z;
    }

    @UnsupportedAppUsage
    @Deprecated
    public void setIsAvailable(boolean isAvailable) {
        synchronized (this) {
            this.mIsAvailable = isAvailable;
        }
    }

    @Deprecated
    public boolean isFailover() {
        boolean z;
        synchronized (this) {
            z = this.mIsFailover;
        }
        return z;
    }

    @UnsupportedAppUsage
    @Deprecated
    public void setFailover(boolean isFailover) {
        synchronized (this) {
            this.mIsFailover = isFailover;
        }
    }

    @Deprecated
    public boolean isRoaming() {
        boolean z;
        synchronized (this) {
            z = this.mIsRoaming;
        }
        return z;
    }

    @UnsupportedAppUsage
    @VisibleForTesting
    @Deprecated
    public void setRoaming(boolean isRoaming) {
        synchronized (this) {
            this.mIsRoaming = isRoaming;
        }
    }

    @Deprecated
    public State getState() {
        State state;
        synchronized (this) {
            state = this.mState;
        }
        return state;
    }

    @Deprecated
    public DetailedState getDetailedState() {
        DetailedState detailedState;
        synchronized (this) {
            detailedState = this.mDetailedState;
        }
        return detailedState;
    }

    @UnsupportedAppUsage
    @Deprecated
    public void setDetailedState(DetailedState detailedState, String reason, String extraInfo) {
        synchronized (this) {
            this.mDetailedState = detailedState;
            this.mState = stateMap.get(detailedState);
            this.mReason = reason;
            this.mExtraInfo = extraInfo;
        }
    }

    @Deprecated
    public void setExtraInfo(String extraInfo) {
        synchronized (this) {
            this.mExtraInfo = extraInfo;
        }
    }

    public String getReason() {
        String str;
        synchronized (this) {
            str = this.mReason;
        }
        return str;
    }

    @Deprecated
    public String getExtraInfo() {
        String str;
        synchronized (this) {
            str = this.mExtraInfo;
        }
        return str;
    }

    public void setPersist(boolean isPersist) {
        synchronized (this) {
            this.mIsPersist = isPersist;
        }
    }

    public boolean isPersist() {
        boolean z;
        synchronized (this) {
            z = this.mIsPersist;
        }
        return z;
    }

    public String toString() {
        String sb;
        synchronized (this) {
            StringBuilder builder = new StringBuilder("[");
            builder.append("type: ");
            builder.append(getTypeName());
            builder.append("[");
            builder.append(getSubtypeName());
            builder.append("], state: ");
            builder.append(this.mState);
            builder.append("/");
            builder.append(this.mDetailedState);
            builder.append(", reason: ");
            builder.append(this.mReason == null ? "(unspecified)" : this.mReason);
            builder.append(", extra: ");
            builder.append(this.mExtraInfo == null ? "(none)" : this.mExtraInfo);
            builder.append(", failover: ");
            builder.append(this.mIsFailover);
            builder.append(", available: ");
            builder.append(this.mIsAvailable);
            builder.append(", roaming: ");
            builder.append(this.mIsRoaming);
            builder.append(", persistance: ");
            builder.append(this.mIsPersist);
            builder.append("]");
            sb = builder.toString();
        }
        return sb;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        synchronized (this) {
            dest.writeInt(this.mNetworkType);
            dest.writeInt(this.mSubtype);
            dest.writeString(this.mTypeName);
            dest.writeString(this.mSubtypeName);
            dest.writeString(this.mState.name());
            dest.writeString(this.mDetailedState.name());
            int i = 1;
            dest.writeInt(this.mIsFailover ? 1 : 0);
            dest.writeInt(this.mIsAvailable ? 1 : 0);
            dest.writeInt(this.mIsRoaming ? 1 : 0);
            if (!this.mIsPersist) {
                i = 0;
            }
            dest.writeInt(i);
            dest.writeString(this.mReason);
            dest.writeString(this.mExtraInfo);
        }
    }
}
