package android.service.contentcapture;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
/* loaded from: classes2.dex */
public final class ActivityEvent implements Parcelable {
    public static final Parcelable.Creator<ActivityEvent> CREATOR = new Parcelable.Creator<ActivityEvent>() { // from class: android.service.contentcapture.ActivityEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityEvent createFromParcel(Parcel parcel) {
            ComponentName componentName = (ComponentName) parcel.readParcelable(null);
            int eventType = parcel.readInt();
            return new ActivityEvent(componentName, eventType);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityEvent[] newArray(int size) {
            return new ActivityEvent[size];
        }
    };
    public static final int TYPE_ACTIVITY_DESTROYED = 24;
    public static final int TYPE_ACTIVITY_PAUSED = 2;
    public static final int TYPE_ACTIVITY_RESUMED = 1;
    public static final int TYPE_ACTIVITY_STOPPED = 23;
    private final ComponentName mComponentName;
    private final int mType;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ActivityEventType {
    }

    public ActivityEvent(ComponentName componentName, int type) {
        this.mComponentName = componentName;
        this.mType = type;
    }

    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    public int getEventType() {
        return this.mType;
    }

    public static String getTypeAsString(int type) {
        if (type != 1) {
            if (type != 2) {
                if (type != 23) {
                    if (type == 24) {
                        return "ACTIVITY_DESTROYED";
                    }
                    return "UKNOWN_TYPE: " + type;
                }
                return "ACTIVITY_STOPPED";
            }
            return "ACTIVITY_PAUSED";
        }
        return "ACTIVITY_RESUMED";
    }

    public String toString() {
        return "ActivityEvent[" + this.mComponentName.toShortString() + "]:" + getTypeAsString(this.mType);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mComponentName, flags);
        parcel.writeInt(this.mType);
    }
}
