package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.telecom.IVideoProvider;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class ParcelableConnection implements Parcelable {
    public static final Parcelable.Creator<ParcelableConnection> CREATOR = new Parcelable.Creator<ParcelableConnection>() { // from class: android.telecom.ParcelableConnection.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableConnection createFromParcel(Parcel source) {
            ClassLoader classLoader = ParcelableConnection.class.getClassLoader();
            PhoneAccountHandle phoneAccount = (PhoneAccountHandle) source.readParcelable(classLoader);
            int state = source.readInt();
            int capabilities = source.readInt();
            Uri address = (Uri) source.readParcelable(classLoader);
            int addressPresentation = source.readInt();
            String callerDisplayName = source.readString();
            int callerDisplayNamePresentation = source.readInt();
            IVideoProvider videoCallProvider = IVideoProvider.Stub.asInterface(source.readStrongBinder());
            int videoState = source.readInt();
            boolean ringbackRequested = source.readByte() == 1;
            boolean audioModeIsVoip = source.readByte() == 1;
            long connectTimeMillis = source.readLong();
            StatusHints statusHints = (StatusHints) source.readParcelable(classLoader);
            DisconnectCause disconnectCause = (DisconnectCause) source.readParcelable(classLoader);
            List<String> conferenceableConnectionIds = new ArrayList<>();
            source.readStringList(conferenceableConnectionIds);
            Bundle extras = Bundle.setDefusable(source.readBundle(classLoader), true);
            int properties = source.readInt();
            int supportedAudioRoutes = source.readInt();
            String parentCallId = source.readString();
            long connectElapsedTimeMillis = source.readLong();
            return new ParcelableConnection(phoneAccount, state, capabilities, properties, supportedAudioRoutes, address, addressPresentation, callerDisplayName, callerDisplayNamePresentation, videoCallProvider, videoState, ringbackRequested, audioModeIsVoip, connectTimeMillis, connectElapsedTimeMillis, statusHints, disconnectCause, conferenceableConnectionIds, extras, parentCallId);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableConnection[] newArray(int size) {
            return new ParcelableConnection[size];
        }
    };
    private final Uri mAddress;
    private final int mAddressPresentation;
    private final String mCallerDisplayName;
    private final int mCallerDisplayNamePresentation;
    private final List<String> mConferenceableConnectionIds;
    private final long mConnectElapsedTimeMillis;
    private final long mConnectTimeMillis;
    private final int mConnectionCapabilities;
    private final int mConnectionProperties;
    private final DisconnectCause mDisconnectCause;
    private final Bundle mExtras;
    private final boolean mIsVoipAudioMode;
    private String mParentCallId;
    private final PhoneAccountHandle mPhoneAccount;
    private final boolean mRingbackRequested;
    private final int mState;
    private final StatusHints mStatusHints;
    private final int mSupportedAudioRoutes;
    private final IVideoProvider mVideoProvider;
    private final int mVideoState;

    public synchronized ParcelableConnection(PhoneAccountHandle phoneAccount, int state, int capabilities, int properties, int supportedAudioRoutes, Uri address, int addressPresentation, String callerDisplayName, int callerDisplayNamePresentation, IVideoProvider videoProvider, int videoState, boolean ringbackRequested, boolean isVoipAudioMode, long connectTimeMillis, long connectElapsedTimeMillis, StatusHints statusHints, DisconnectCause disconnectCause, List<String> conferenceableConnectionIds, Bundle extras, String parentCallId) {
        this(phoneAccount, state, capabilities, properties, supportedAudioRoutes, address, addressPresentation, callerDisplayName, callerDisplayNamePresentation, videoProvider, videoState, ringbackRequested, isVoipAudioMode, connectTimeMillis, connectElapsedTimeMillis, statusHints, disconnectCause, conferenceableConnectionIds, extras);
        this.mParentCallId = parentCallId;
    }

    public synchronized ParcelableConnection(PhoneAccountHandle phoneAccount, int state, int capabilities, int properties, int supportedAudioRoutes, Uri address, int addressPresentation, String callerDisplayName, int callerDisplayNamePresentation, IVideoProvider videoProvider, int videoState, boolean ringbackRequested, boolean isVoipAudioMode, long connectTimeMillis, long connectElapsedTimeMillis, StatusHints statusHints, DisconnectCause disconnectCause, List<String> conferenceableConnectionIds, Bundle extras) {
        this.mPhoneAccount = phoneAccount;
        this.mState = state;
        this.mConnectionCapabilities = capabilities;
        this.mConnectionProperties = properties;
        this.mSupportedAudioRoutes = supportedAudioRoutes;
        this.mAddress = address;
        this.mAddressPresentation = addressPresentation;
        this.mCallerDisplayName = callerDisplayName;
        this.mCallerDisplayNamePresentation = callerDisplayNamePresentation;
        this.mVideoProvider = videoProvider;
        this.mVideoState = videoState;
        this.mRingbackRequested = ringbackRequested;
        this.mIsVoipAudioMode = isVoipAudioMode;
        this.mConnectTimeMillis = connectTimeMillis;
        this.mConnectElapsedTimeMillis = connectElapsedTimeMillis;
        this.mStatusHints = statusHints;
        this.mDisconnectCause = disconnectCause;
        this.mConferenceableConnectionIds = conferenceableConnectionIds;
        this.mExtras = extras;
        this.mParentCallId = null;
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

    public synchronized int getSupportedAudioRoutes() {
        return this.mSupportedAudioRoutes;
    }

    public synchronized Uri getHandle() {
        return this.mAddress;
    }

    public synchronized int getHandlePresentation() {
        return this.mAddressPresentation;
    }

    public synchronized String getCallerDisplayName() {
        return this.mCallerDisplayName;
    }

    public synchronized int getCallerDisplayNamePresentation() {
        return this.mCallerDisplayNamePresentation;
    }

    public synchronized IVideoProvider getVideoProvider() {
        return this.mVideoProvider;
    }

    public synchronized int getVideoState() {
        return this.mVideoState;
    }

    public synchronized boolean isRingbackRequested() {
        return this.mRingbackRequested;
    }

    public synchronized boolean getIsVoipAudioMode() {
        return this.mIsVoipAudioMode;
    }

    public synchronized long getConnectTimeMillis() {
        return this.mConnectTimeMillis;
    }

    public synchronized long getConnectElapsedTimeMillis() {
        return this.mConnectElapsedTimeMillis;
    }

    public final synchronized StatusHints getStatusHints() {
        return this.mStatusHints;
    }

    public final synchronized DisconnectCause getDisconnectCause() {
        return this.mDisconnectCause;
    }

    public final synchronized List<String> getConferenceableConnectionIds() {
        return this.mConferenceableConnectionIds;
    }

    public final synchronized Bundle getExtras() {
        return this.mExtras;
    }

    public final synchronized String getParentCallId() {
        return this.mParentCallId;
    }

    public String toString() {
        return "ParcelableConnection [act:" + this.mPhoneAccount + "], state:" + this.mState + ", capabilities:" + Connection.capabilitiesToString(this.mConnectionCapabilities) + ", properties:" + Connection.propertiesToString(this.mConnectionProperties) + ", extras:" + this.mExtras + ", parent:" + this.mParentCallId;
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
        destination.writeParcelable(this.mAddress, 0);
        destination.writeInt(this.mAddressPresentation);
        destination.writeString(this.mCallerDisplayName);
        destination.writeInt(this.mCallerDisplayNamePresentation);
        destination.writeStrongBinder(this.mVideoProvider != null ? this.mVideoProvider.asBinder() : null);
        destination.writeInt(this.mVideoState);
        destination.writeByte(this.mRingbackRequested ? (byte) 1 : (byte) 0);
        destination.writeByte(this.mIsVoipAudioMode ? (byte) 1 : (byte) 0);
        destination.writeLong(this.mConnectTimeMillis);
        destination.writeParcelable(this.mStatusHints, 0);
        destination.writeParcelable(this.mDisconnectCause, 0);
        destination.writeStringList(this.mConferenceableConnectionIds);
        destination.writeBundle(this.mExtras);
        destination.writeInt(this.mConnectionProperties);
        destination.writeInt(this.mSupportedAudioRoutes);
        destination.writeString(this.mParentCallId);
        destination.writeLong(this.mConnectElapsedTimeMillis);
    }
}
