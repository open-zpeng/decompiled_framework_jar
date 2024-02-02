package android.telecom;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class ConferenceParticipant implements Parcelable {
    public static final Parcelable.Creator<ConferenceParticipant> CREATOR = new Parcelable.Creator<ConferenceParticipant>() { // from class: android.telecom.ConferenceParticipant.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConferenceParticipant createFromParcel(Parcel source) {
            ClassLoader classLoader = ParcelableCall.class.getClassLoader();
            Uri handle = (Uri) source.readParcelable(classLoader);
            String displayName = source.readString();
            Uri endpoint = (Uri) source.readParcelable(classLoader);
            int state = source.readInt();
            return new ConferenceParticipant(handle, displayName, endpoint, state);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConferenceParticipant[] newArray(int size) {
            return new ConferenceParticipant[size];
        }
    };
    private final String mDisplayName;
    private final Uri mEndpoint;
    private final Uri mHandle;
    private final int mState;

    public synchronized ConferenceParticipant(Uri handle, String displayName, Uri endpoint, int state) {
        this.mHandle = handle;
        this.mDisplayName = displayName;
        this.mEndpoint = endpoint;
        this.mState = state;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mHandle, 0);
        dest.writeString(this.mDisplayName);
        dest.writeParcelable(this.mEndpoint, 0);
        dest.writeInt(this.mState);
    }

    public String toString() {
        return "[ConferenceParticipant Handle: " + Log.pii(this.mHandle) + " DisplayName: " + Log.pii(this.mDisplayName) + " Endpoint: " + Log.pii(this.mEndpoint) + " State: " + Connection.stateToString(this.mState) + "]";
    }

    public synchronized Uri getHandle() {
        return this.mHandle;
    }

    public synchronized String getDisplayName() {
        return this.mDisplayName;
    }

    public synchronized Uri getEndpoint() {
        return this.mEndpoint;
    }

    public synchronized int getState() {
        return this.mState;
    }
}
