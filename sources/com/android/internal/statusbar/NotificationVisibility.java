package com.android.internal.statusbar;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes3.dex */
public class NotificationVisibility implements Parcelable {
    private static final int MAX_POOL_SIZE = 25;
    private static final String TAG = "NoViz";
    public int count;
    int id;
    public String key;
    public NotificationLocation location;
    public int rank;
    public boolean visible;
    private static int sNexrId = 0;
    public static final Parcelable.Creator<NotificationVisibility> CREATOR = new Parcelable.Creator<NotificationVisibility>() { // from class: com.android.internal.statusbar.NotificationVisibility.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotificationVisibility createFromParcel(Parcel parcel) {
            return NotificationVisibility.obtain(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotificationVisibility[] newArray(int size) {
            return new NotificationVisibility[size];
        }
    };

    /* loaded from: classes3.dex */
    public enum NotificationLocation {
        LOCATION_UNKNOWN(0),
        LOCATION_FIRST_HEADS_UP(1),
        LOCATION_HIDDEN_TOP(2),
        LOCATION_MAIN_AREA(3),
        LOCATION_BOTTOM_STACK_PEEKING(4),
        LOCATION_BOTTOM_STACK_HIDDEN(5),
        LOCATION_GONE(6);
        
        private final int mMetricsEventNotificationLocation;

        NotificationLocation(int metricsEventNotificationLocation) {
            this.mMetricsEventNotificationLocation = metricsEventNotificationLocation;
        }

        public int toMetricsEventEnum() {
            return this.mMetricsEventNotificationLocation;
        }
    }

    private NotificationVisibility() {
        this.visible = true;
        int i = sNexrId;
        sNexrId = i + 1;
        this.id = i;
    }

    private NotificationVisibility(String key, int rank, int count, boolean visible, NotificationLocation location) {
        this();
        this.key = key;
        this.rank = rank;
        this.count = count;
        this.visible = visible;
        this.location = location;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NotificationVisibility(id=");
        sb.append(this.id);
        sb.append(" key=");
        sb.append(this.key);
        sb.append(" rank=");
        sb.append(this.rank);
        sb.append(" count=");
        sb.append(this.count);
        sb.append(this.visible ? " visible" : "");
        sb.append(" location=");
        sb.append(this.location.name());
        sb.append(" )");
        return sb.toString();
    }

    /* renamed from: clone */
    public NotificationVisibility m53clone() {
        return obtain(this.key, this.rank, this.count, this.visible, this.location);
    }

    public int hashCode() {
        String str = this.key;
        if (str == null) {
            return 0;
        }
        return str.hashCode();
    }

    public boolean equals(Object that) {
        if (that instanceof NotificationVisibility) {
            NotificationVisibility thatViz = (NotificationVisibility) that;
            return (this.key == null && thatViz.key == null) || this.key.equals(thatViz.key);
        }
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.key);
        out.writeInt(this.rank);
        out.writeInt(this.count);
        out.writeInt(this.visible ? 1 : 0);
        out.writeString(this.location.name());
    }

    private void readFromParcel(Parcel in) {
        this.key = in.readString();
        this.rank = in.readInt();
        this.count = in.readInt();
        this.visible = in.readInt() != 0;
        this.location = NotificationLocation.valueOf(in.readString());
    }

    public static NotificationVisibility obtain(String key, int rank, int count, boolean visible) {
        return obtain(key, rank, count, visible, NotificationLocation.LOCATION_UNKNOWN);
    }

    public static NotificationVisibility obtain(String key, int rank, int count, boolean visible, NotificationLocation location) {
        NotificationVisibility vo = obtain();
        vo.key = key;
        vo.rank = rank;
        vo.count = count;
        vo.visible = visible;
        vo.location = location;
        return vo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static NotificationVisibility obtain(Parcel in) {
        NotificationVisibility vo = obtain();
        vo.readFromParcel(in);
        return vo;
    }

    private static NotificationVisibility obtain() {
        return new NotificationVisibility();
    }

    public void recycle() {
    }
}
