package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.telecom.Connection;
/* loaded from: classes2.dex */
public final class ConnectionRequest implements Parcelable {
    public static final Parcelable.Creator<ConnectionRequest> CREATOR = new Parcelable.Creator<ConnectionRequest>() { // from class: android.telecom.ConnectionRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConnectionRequest createFromParcel(Parcel source) {
            return new ConnectionRequest(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConnectionRequest[] newArray(int size) {
            return new ConnectionRequest[size];
        }
    };
    private final PhoneAccountHandle mAccountHandle;
    private final Uri mAddress;
    private final Bundle mExtras;
    private final ParcelFileDescriptor mRttPipeFromInCall;
    private final ParcelFileDescriptor mRttPipeToInCall;
    private Connection.RttTextStream mRttTextStream;
    private final boolean mShouldShowIncomingCallUi;
    private final String mTelecomCallId;
    private final int mVideoState;

    /* loaded from: classes2.dex */
    public static final class Builder {
        private PhoneAccountHandle mAccountHandle;
        private Uri mAddress;
        private Bundle mExtras;
        private ParcelFileDescriptor mRttPipeFromInCall;
        private ParcelFileDescriptor mRttPipeToInCall;
        private String mTelecomCallId;
        private int mVideoState = 0;
        private boolean mShouldShowIncomingCallUi = false;

        public synchronized Builder setAccountHandle(PhoneAccountHandle accountHandle) {
            this.mAccountHandle = accountHandle;
            return this;
        }

        public synchronized Builder setAddress(Uri address) {
            this.mAddress = address;
            return this;
        }

        public synchronized Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public synchronized Builder setVideoState(int videoState) {
            this.mVideoState = videoState;
            return this;
        }

        public synchronized Builder setTelecomCallId(String telecomCallId) {
            this.mTelecomCallId = telecomCallId;
            return this;
        }

        public synchronized Builder setShouldShowIncomingCallUi(boolean shouldShowIncomingCallUi) {
            this.mShouldShowIncomingCallUi = shouldShowIncomingCallUi;
            return this;
        }

        public synchronized Builder setRttPipeFromInCall(ParcelFileDescriptor rttPipeFromInCall) {
            this.mRttPipeFromInCall = rttPipeFromInCall;
            return this;
        }

        public synchronized Builder setRttPipeToInCall(ParcelFileDescriptor rttPipeToInCall) {
            this.mRttPipeToInCall = rttPipeToInCall;
            return this;
        }

        public synchronized ConnectionRequest build() {
            return new ConnectionRequest(this.mAccountHandle, this.mAddress, this.mExtras, this.mVideoState, this.mTelecomCallId, this.mShouldShowIncomingCallUi, this.mRttPipeFromInCall, this.mRttPipeToInCall);
        }
    }

    public ConnectionRequest(PhoneAccountHandle accountHandle, Uri handle, Bundle extras) {
        this(accountHandle, handle, extras, 0, null, false, null, null);
    }

    public ConnectionRequest(PhoneAccountHandle accountHandle, Uri handle, Bundle extras, int videoState) {
        this(accountHandle, handle, extras, videoState, null, false, null, null);
    }

    public synchronized ConnectionRequest(PhoneAccountHandle accountHandle, Uri handle, Bundle extras, int videoState, String telecomCallId, boolean shouldShowIncomingCallUi) {
        this(accountHandle, handle, extras, videoState, telecomCallId, shouldShowIncomingCallUi, null, null);
    }

    private synchronized ConnectionRequest(PhoneAccountHandle accountHandle, Uri handle, Bundle extras, int videoState, String telecomCallId, boolean shouldShowIncomingCallUi, ParcelFileDescriptor rttPipeFromInCall, ParcelFileDescriptor rttPipeToInCall) {
        this.mAccountHandle = accountHandle;
        this.mAddress = handle;
        this.mExtras = extras;
        this.mVideoState = videoState;
        this.mTelecomCallId = telecomCallId;
        this.mShouldShowIncomingCallUi = shouldShowIncomingCallUi;
        this.mRttPipeFromInCall = rttPipeFromInCall;
        this.mRttPipeToInCall = rttPipeToInCall;
    }

    private synchronized ConnectionRequest(Parcel in) {
        this.mAccountHandle = (PhoneAccountHandle) in.readParcelable(getClass().getClassLoader());
        this.mAddress = (Uri) in.readParcelable(getClass().getClassLoader());
        this.mExtras = (Bundle) in.readParcelable(getClass().getClassLoader());
        this.mVideoState = in.readInt();
        this.mTelecomCallId = in.readString();
        this.mShouldShowIncomingCallUi = in.readInt() == 1;
        this.mRttPipeFromInCall = (ParcelFileDescriptor) in.readParcelable(getClass().getClassLoader());
        this.mRttPipeToInCall = (ParcelFileDescriptor) in.readParcelable(getClass().getClassLoader());
    }

    public PhoneAccountHandle getAccountHandle() {
        return this.mAccountHandle;
    }

    public Uri getAddress() {
        return this.mAddress;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public int getVideoState() {
        return this.mVideoState;
    }

    public synchronized String getTelecomCallId() {
        return this.mTelecomCallId;
    }

    public synchronized boolean shouldShowIncomingCallUi() {
        return this.mShouldShowIncomingCallUi;
    }

    public synchronized ParcelFileDescriptor getRttPipeToInCall() {
        return this.mRttPipeToInCall;
    }

    public synchronized ParcelFileDescriptor getRttPipeFromInCall() {
        return this.mRttPipeFromInCall;
    }

    public Connection.RttTextStream getRttTextStream() {
        if (isRequestingRtt()) {
            if (this.mRttTextStream == null) {
                this.mRttTextStream = new Connection.RttTextStream(this.mRttPipeToInCall, this.mRttPipeFromInCall);
            }
            return this.mRttTextStream;
        }
        return null;
    }

    public boolean isRequestingRtt() {
        return (this.mRttPipeFromInCall == null || this.mRttPipeToInCall == null) ? false : true;
    }

    public String toString() {
        Object logSafePhoneNumber;
        Object[] objArr = new Object[2];
        if (this.mAddress == null) {
            logSafePhoneNumber = Uri.EMPTY;
        } else {
            logSafePhoneNumber = Connection.toLogSafePhoneNumber(this.mAddress.toString());
        }
        objArr[0] = logSafePhoneNumber;
        objArr[1] = this.mExtras == null ? "" : this.mExtras;
        return String.format("ConnectionRequest %s %s", objArr);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeParcelable(this.mAccountHandle, 0);
        destination.writeParcelable(this.mAddress, 0);
        destination.writeParcelable(this.mExtras, 0);
        destination.writeInt(this.mVideoState);
        destination.writeString(this.mTelecomCallId);
        destination.writeInt(this.mShouldShowIncomingCallUi ? 1 : 0);
        destination.writeParcelable(this.mRttPipeFromInCall, 0);
        destination.writeParcelable(this.mRttPipeToInCall, 0);
    }
}
