package android.print;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;
/* loaded from: classes2.dex */
public final class PrinterId implements Parcelable {
    public static final Parcelable.Creator<PrinterId> CREATOR = new Parcelable.Creator<PrinterId>() { // from class: android.print.PrinterId.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrinterId createFromParcel(Parcel parcel) {
            return new PrinterId(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrinterId[] newArray(int size) {
            return new PrinterId[size];
        }
    };
    private final String mLocalId;
    private final ComponentName mServiceName;

    public synchronized PrinterId(ComponentName serviceName, String localId) {
        this.mServiceName = serviceName;
        this.mLocalId = localId;
    }

    private synchronized PrinterId(Parcel parcel) {
        this.mServiceName = (ComponentName) Preconditions.checkNotNull((ComponentName) parcel.readParcelable(null));
        this.mLocalId = (String) Preconditions.checkNotNull(parcel.readString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ComponentName getServiceName() {
        return this.mServiceName;
    }

    public String getLocalId() {
        return this.mLocalId;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mServiceName, flags);
        parcel.writeString(this.mLocalId);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PrinterId other = (PrinterId) object;
        if (this.mServiceName.equals(other.mServiceName) && this.mLocalId.equals(other.mLocalId)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = (31 * 1) + this.mServiceName.hashCode();
        return (31 * hashCode) + this.mLocalId.hashCode();
    }

    public String toString() {
        return "PrinterId{serviceName=" + this.mServiceName.flattenToString() + ", localId=" + this.mLocalId + '}';
    }
}
