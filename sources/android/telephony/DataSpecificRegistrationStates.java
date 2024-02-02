package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes2.dex */
public class DataSpecificRegistrationStates implements Parcelable {
    public static final Parcelable.Creator<DataSpecificRegistrationStates> CREATOR = new Parcelable.Creator<DataSpecificRegistrationStates>() { // from class: android.telephony.DataSpecificRegistrationStates.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataSpecificRegistrationStates createFromParcel(Parcel source) {
            return new DataSpecificRegistrationStates(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DataSpecificRegistrationStates[] newArray(int size) {
            return new DataSpecificRegistrationStates[size];
        }
    };
    public final int maxDataCalls;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized DataSpecificRegistrationStates(int maxDataCalls) {
        this.maxDataCalls = maxDataCalls;
    }

    private synchronized DataSpecificRegistrationStates(Parcel source) {
        this.maxDataCalls = source.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxDataCalls);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "DataSpecificRegistrationStates { mMaxDataCalls=" + this.maxDataCalls + "}";
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.maxDataCalls));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof DataSpecificRegistrationStates)) {
            return false;
        }
        DataSpecificRegistrationStates other = (DataSpecificRegistrationStates) o;
        if (this.maxDataCalls == other.maxDataCalls) {
            return true;
        }
        return false;
    }
}
