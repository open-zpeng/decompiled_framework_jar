package com.android.internal.location;

import android.annotation.UnsupportedAppUsage;
import android.location.LocationRequest;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.TimeUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public final class ProviderRequest implements Parcelable {
    public static final Parcelable.Creator<ProviderRequest> CREATOR = new Parcelable.Creator<ProviderRequest>() { // from class: com.android.internal.location.ProviderRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProviderRequest createFromParcel(Parcel in) {
            ProviderRequest request = new ProviderRequest();
            request.reportLocation = in.readInt() == 1;
            request.interval = in.readLong();
            request.lowPowerMode = in.readBoolean();
            request.locationSettingsIgnored = in.readBoolean();
            int count = in.readInt();
            for (int i = 0; i < count; i++) {
                request.locationRequests.add(LocationRequest.CREATOR.createFromParcel(in));
            }
            return request;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProviderRequest[] newArray(int size) {
            return new ProviderRequest[size];
        }
    };
    @UnsupportedAppUsage
    public boolean reportLocation = false;
    @UnsupportedAppUsage
    public long interval = Long.MAX_VALUE;
    public boolean locationSettingsIgnored = false;
    public boolean lowPowerMode = false;
    @UnsupportedAppUsage
    public final List<LocationRequest> locationRequests = new ArrayList();

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.reportLocation ? 1 : 0);
        parcel.writeLong(this.interval);
        parcel.writeBoolean(this.lowPowerMode);
        parcel.writeBoolean(this.locationSettingsIgnored);
        parcel.writeInt(this.locationRequests.size());
        for (LocationRequest request : this.locationRequests) {
            request.writeToParcel(parcel, flags);
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("ProviderRequest[");
        if (this.reportLocation) {
            s.append("ON");
            s.append(" interval=");
            TimeUtils.formatDuration(this.interval, s);
            if (this.lowPowerMode) {
                s.append(" lowPowerMode");
            }
            if (this.locationSettingsIgnored) {
                s.append(" locationSettingsIgnored");
            }
        } else {
            s.append("OFF");
        }
        s.append(']');
        return s.toString();
    }
}
