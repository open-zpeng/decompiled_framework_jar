package android.companion;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.OneTimeUseBuilder;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AssociationRequest implements Parcelable {
    public static final Parcelable.Creator<AssociationRequest> CREATOR = new Parcelable.Creator<AssociationRequest>() { // from class: android.companion.AssociationRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AssociationRequest createFromParcel(Parcel in) {
            return new AssociationRequest(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AssociationRequest[] newArray(int size) {
            return new AssociationRequest[size];
        }
    };
    private final List<DeviceFilter<?>> mDeviceFilters;
    private final boolean mSingleDevice;

    private synchronized AssociationRequest(boolean singleDevice, List<DeviceFilter<?>> deviceFilters) {
        this.mSingleDevice = singleDevice;
        this.mDeviceFilters = CollectionUtils.emptyIfNull(deviceFilters);
    }

    private synchronized AssociationRequest(Parcel in) {
        this(in.readByte() != 0, in.readParcelableList(new ArrayList(), AssociationRequest.class.getClassLoader()));
    }

    private protected boolean isSingleDevice() {
        return this.mSingleDevice;
    }

    private protected List<DeviceFilter<?>> getDeviceFilters() {
        return this.mDeviceFilters;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssociationRequest that = (AssociationRequest) o;
        if (this.mSingleDevice == that.mSingleDevice && Objects.equals(this.mDeviceFilters, that.mDeviceFilters)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.mSingleDevice), this.mDeviceFilters);
    }

    public String toString() {
        return "AssociationRequest{mSingleDevice=" + this.mSingleDevice + ", mDeviceFilters=" + this.mDeviceFilters + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.mSingleDevice ? (byte) 1 : (byte) 0);
        dest.writeParcelableList(this.mDeviceFilters, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* loaded from: classes.dex */
    public static final class Builder extends OneTimeUseBuilder<AssociationRequest> {
        private boolean mSingleDevice = false;
        private ArrayList<DeviceFilter<?>> mDeviceFilters = null;

        public Builder setSingleDevice(boolean singleDevice) {
            checkNotUsed();
            this.mSingleDevice = singleDevice;
            return this;
        }

        public Builder addDeviceFilter(DeviceFilter<?> deviceFilter) {
            checkNotUsed();
            if (deviceFilter != null) {
                this.mDeviceFilters = ArrayUtils.add(this.mDeviceFilters, deviceFilter);
            }
            return this;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.provider.OneTimeUseBuilder
        public AssociationRequest build() {
            markUsed();
            return new AssociationRequest(this.mSingleDevice, this.mDeviceFilters);
        }
    }
}
