package android.app.admin;

import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;

/* loaded from: classes.dex */
public abstract class NetworkEvent implements Parcelable {
    public static final Parcelable.Creator<NetworkEvent> CREATOR = new Parcelable.Creator<NetworkEvent>() { // from class: android.app.admin.NetworkEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkEvent createFromParcel(Parcel in) {
            int initialPosition = in.dataPosition();
            int parcelToken = in.readInt();
            in.setDataPosition(initialPosition);
            if (parcelToken != 1) {
                if (parcelToken == 2) {
                    return ConnectEvent.CREATOR.createFromParcel(in);
                }
                throw new ParcelFormatException("Unexpected NetworkEvent token in parcel: " + parcelToken);
            }
            return DnsEvent.CREATOR.createFromParcel(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NetworkEvent[] newArray(int size) {
            return new NetworkEvent[size];
        }
    };
    static final int PARCEL_TOKEN_CONNECT_EVENT = 2;
    static final int PARCEL_TOKEN_DNS_EVENT = 1;
    long mId;
    String mPackageName;
    long mTimestamp;

    @Override // android.os.Parcelable
    public abstract void writeToParcel(Parcel parcel, int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public NetworkEvent() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NetworkEvent(String packageName, long timestamp) {
        this.mPackageName = packageName;
        this.mTimestamp = timestamp;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public long getId() {
        return this.mId;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
