package android.hardware.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
/* loaded from: classes.dex */
public final class GeofenceHardwareRequestParcelable implements Parcelable {
    public static final Parcelable.Creator<GeofenceHardwareRequestParcelable> CREATOR = new Parcelable.Creator<GeofenceHardwareRequestParcelable>() { // from class: android.hardware.location.GeofenceHardwareRequestParcelable.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GeofenceHardwareRequestParcelable createFromParcel(Parcel parcel) {
            int geofenceType = parcel.readInt();
            if (geofenceType != 0) {
                Log.e("GeofenceHardwareRequest", String.format("Invalid Geofence type: %d", Integer.valueOf(geofenceType)));
                return null;
            }
            GeofenceHardwareRequest request = GeofenceHardwareRequest.createCircularGeofence(parcel.readDouble(), parcel.readDouble(), parcel.readDouble());
            request.setLastTransition(parcel.readInt());
            request.setMonitorTransitions(parcel.readInt());
            request.setUnknownTimer(parcel.readInt());
            request.setNotificationResponsiveness(parcel.readInt());
            request.setSourceTechnologies(parcel.readInt());
            int id = parcel.readInt();
            return new GeofenceHardwareRequestParcelable(id, request);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GeofenceHardwareRequestParcelable[] newArray(int size) {
            return new GeofenceHardwareRequestParcelable[size];
        }
    };
    private int mId;
    private GeofenceHardwareRequest mRequest;

    public synchronized GeofenceHardwareRequestParcelable(int id, GeofenceHardwareRequest request) {
        this.mId = id;
        this.mRequest = request;
    }

    public synchronized int getId() {
        return this.mId;
    }

    public synchronized double getLatitude() {
        return this.mRequest.getLatitude();
    }

    public synchronized double getLongitude() {
        return this.mRequest.getLongitude();
    }

    public synchronized double getRadius() {
        return this.mRequest.getRadius();
    }

    public synchronized int getMonitorTransitions() {
        return this.mRequest.getMonitorTransitions();
    }

    public synchronized int getUnknownTimer() {
        return this.mRequest.getUnknownTimer();
    }

    public synchronized int getNotificationResponsiveness() {
        return this.mRequest.getNotificationResponsiveness();
    }

    public synchronized int getLastTransition() {
        return this.mRequest.getLastTransition();
    }

    synchronized int getType() {
        return this.mRequest.getType();
    }

    synchronized int getSourceTechnologies() {
        return this.mRequest.getSourceTechnologies();
    }

    public String toString() {
        return "id=" + this.mId + ", type=" + this.mRequest.getType() + ", latitude=" + this.mRequest.getLatitude() + ", longitude=" + this.mRequest.getLongitude() + ", radius=" + this.mRequest.getRadius() + ", lastTransition=" + this.mRequest.getLastTransition() + ", unknownTimer=" + this.mRequest.getUnknownTimer() + ", monitorTransitions=" + this.mRequest.getMonitorTransitions() + ", notificationResponsiveness=" + this.mRequest.getNotificationResponsiveness() + ", sourceTechnologies=" + this.mRequest.getSourceTechnologies();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getType());
        parcel.writeDouble(getLatitude());
        parcel.writeDouble(getLongitude());
        parcel.writeDouble(getRadius());
        parcel.writeInt(getLastTransition());
        parcel.writeInt(getMonitorTransitions());
        parcel.writeInt(getUnknownTimer());
        parcel.writeInt(getNotificationResponsiveness());
        parcel.writeInt(getSourceTechnologies());
        parcel.writeInt(getId());
    }
}
