package android.service.gatekeeper;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;
/* loaded from: classes2.dex */
public final class GateKeeperResponse implements Parcelable {
    private protected static final Parcelable.Creator<GateKeeperResponse> CREATOR = new Parcelable.Creator<GateKeeperResponse>() { // from class: android.service.gatekeeper.GateKeeperResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GateKeeperResponse createFromParcel(Parcel source) {
            int responseCode = source.readInt();
            if (responseCode == 1) {
                GateKeeperResponse response = GateKeeperResponse.createRetryResponse(source.readInt());
                return response;
            } else if (responseCode == 0) {
                boolean shouldReEnroll = source.readInt() == 1;
                byte[] payload = null;
                int size = source.readInt();
                if (size > 0) {
                    payload = new byte[size];
                    source.readByteArray(payload);
                }
                GateKeeperResponse response2 = GateKeeperResponse.createOkResponse(payload, shouldReEnroll);
                return response2;
            } else {
                GateKeeperResponse response3 = GateKeeperResponse.createGenericResponse(responseCode);
                return response3;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GateKeeperResponse[] newArray(int size) {
            return new GateKeeperResponse[size];
        }
    };
    private protected static final int RESPONSE_ERROR = -1;
    private protected static final int RESPONSE_OK = 0;
    private protected static final int RESPONSE_RETRY = 1;
    public protected byte[] mPayload;
    public protected final int mResponseCode;
    public protected boolean mShouldReEnroll;
    public protected int mTimeout;

    public protected synchronized GateKeeperResponse(int responseCode) {
        this.mResponseCode = responseCode;
    }

    @VisibleForTesting
    private protected static synchronized GateKeeperResponse createGenericResponse(int responseCode) {
        return new GateKeeperResponse(responseCode);
    }

    /* JADX INFO: Access modifiers changed from: public */
    public static synchronized GateKeeperResponse createRetryResponse(int timeout) {
        GateKeeperResponse response = new GateKeeperResponse(1);
        response.mTimeout = timeout;
        return response;
    }

    @VisibleForTesting
    private protected static synchronized GateKeeperResponse createOkResponse(byte[] payload, boolean shouldReEnroll) {
        GateKeeperResponse response = new GateKeeperResponse(0);
        response.mPayload = payload;
        response.mShouldReEnroll = shouldReEnroll;
        return response;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mResponseCode);
        if (this.mResponseCode == 1) {
            dest.writeInt(this.mTimeout);
        } else if (this.mResponseCode == 0) {
            dest.writeInt(this.mShouldReEnroll ? 1 : 0);
            if (this.mPayload != null) {
                dest.writeInt(this.mPayload.length);
                dest.writeByteArray(this.mPayload);
                return;
            }
            dest.writeInt(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized byte[] getPayload() {
        return this.mPayload;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getTimeout() {
        return this.mTimeout;
    }

    private protected synchronized boolean getShouldReEnroll() {
        return this.mShouldReEnroll;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getResponseCode() {
        return this.mResponseCode;
    }
}
