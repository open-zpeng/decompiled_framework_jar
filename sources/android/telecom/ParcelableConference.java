package android.telecom;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.telecom.IVideoProvider;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class ParcelableConference implements Parcelable {
    public static final Parcelable.Creator<ParcelableConference> CREATOR = new Parcelable.Creator<ParcelableConference>() { // from class: android.telecom.ParcelableConference.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableConference createFromParcel(Parcel source) {
            ClassLoader classLoader = ParcelableConference.class.getClassLoader();
            PhoneAccountHandle phoneAccount = (PhoneAccountHandle) source.readParcelable(classLoader);
            int state = source.readInt();
            int capabilities = source.readInt();
            List<String> connectionIds = new ArrayList<>(2);
            source.readList(connectionIds, classLoader);
            long connectTimeMillis = source.readLong();
            IVideoProvider videoCallProvider = IVideoProvider.Stub.asInterface(source.readStrongBinder());
            int videoState = source.readInt();
            StatusHints statusHints = (StatusHints) source.readParcelable(classLoader);
            Bundle extras = source.readBundle(classLoader);
            int properties = source.readInt();
            long connectElapsedTimeMillis = source.readLong();
            return new ParcelableConference(phoneAccount, state, capabilities, properties, connectionIds, videoCallProvider, videoState, connectTimeMillis, connectElapsedTimeMillis, statusHints, extras);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableConference[] newArray(int size) {
            return new ParcelableConference[size];
        }
    };
    private long mConnectElapsedTimeMillis;
    private long mConnectTimeMillis;
    private int mConnectionCapabilities;
    private List<String> mConnectionIds;
    private int mConnectionProperties;
    private Bundle mExtras;
    private PhoneAccountHandle mPhoneAccount;
    private int mState;
    private StatusHints mStatusHints;
    private final IVideoProvider mVideoProvider;
    private final int mVideoState;

    public synchronized ParcelableConference(PhoneAccountHandle phoneAccount, int state, int connectionCapabilities, int connectionProperties, List<String> connectionIds, IVideoProvider videoProvider, int videoState, long connectTimeMillis, long connectElapsedTimeMillis, StatusHints statusHints, Bundle extras) {
        this.mConnectTimeMillis = 0L;
        this.mConnectElapsedTimeMillis = 0L;
        this.mPhoneAccount = phoneAccount;
        this.mState = state;
        this.mConnectionCapabilities = connectionCapabilities;
        this.mConnectionProperties = connectionProperties;
        this.mConnectionIds = connectionIds;
        this.mVideoProvider = videoProvider;
        this.mVideoState = videoState;
        this.mConnectTimeMillis = connectTimeMillis;
        this.mStatusHints = statusHints;
        this.mExtras = extras;
        this.mConnectElapsedTimeMillis = connectElapsedTimeMillis;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("account: ");
        stringBuffer.append(this.mPhoneAccount);
        stringBuffer.append(", state: ");
        stringBuffer.append(Connection.stateToString(this.mState));
        stringBuffer.append(", capabilities: ");
        stringBuffer.append(Connection.capabilitiesToString(this.mConnectionCapabilities));
        stringBuffer.append(", properties: ");
        stringBuffer.append(Connection.propertiesToString(this.mConnectionProperties));
        stringBuffer.append(", connectTime: ");
        stringBuffer.append(this.mConnectTimeMillis);
        stringBuffer.append(", children: ");
        stringBuffer.append(this.mConnectionIds);
        stringBuffer.append(", VideoState: ");
        stringBuffer.append(this.mVideoState);
        stringBuffer.append(", VideoProvider: ");
        stringBuffer.append(this.mVideoProvider);
        return stringBuffer.toString();
    }

    public synchronized PhoneAccountHandle getPhoneAccount() {
        return this.mPhoneAccount;
    }

    public synchronized int getState() {
        return this.mState;
    }

    public synchronized int getConnectionCapabilities() {
        return this.mConnectionCapabilities;
    }

    public synchronized int getConnectionProperties() {
        return this.mConnectionProperties;
    }

    public synchronized List<String> getConnectionIds() {
        return this.mConnectionIds;
    }

    public synchronized long getConnectTimeMillis() {
        return this.mConnectTimeMillis;
    }

    public synchronized long getConnectElapsedTimeMillis() {
        return this.mConnectElapsedTimeMillis;
    }

    public synchronized IVideoProvider getVideoProvider() {
        return this.mVideoProvider;
    }

    public synchronized int getVideoState() {
        return this.mVideoState;
    }

    public synchronized StatusHints getStatusHints() {
        return this.mStatusHints;
    }

    public synchronized Bundle getExtras() {
        return this.mExtras;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeParcelable(this.mPhoneAccount, 0);
        destination.writeInt(this.mState);
        destination.writeInt(this.mConnectionCapabilities);
        destination.writeList(this.mConnectionIds);
        destination.writeLong(this.mConnectTimeMillis);
        destination.writeStrongBinder(this.mVideoProvider != null ? this.mVideoProvider.asBinder() : null);
        destination.writeInt(this.mVideoState);
        destination.writeParcelable(this.mStatusHints, 0);
        destination.writeBundle(this.mExtras);
        destination.writeInt(this.mConnectionProperties);
        destination.writeLong(this.mConnectElapsedTimeMillis);
    }
}
