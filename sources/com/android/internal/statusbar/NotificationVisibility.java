package com.android.internal.statusbar;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayDeque;
/* loaded from: classes3.dex */
public class NotificationVisibility implements Parcelable {
    private static final int MAX_POOL_SIZE = 25;
    private static final String TAG = "NoViz";
    public int count;
    int id;
    public String key;
    public int rank;
    public boolean visible;
    private static ArrayDeque<NotificationVisibility> sPool = new ArrayDeque<>(25);
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

    private synchronized NotificationVisibility() {
        this.visible = true;
        int i = sNexrId;
        sNexrId = i + 1;
        this.id = i;
    }

    private synchronized NotificationVisibility(String key, int rank, int count, boolean visibile) {
        this();
        this.key = key;
        this.rank = rank;
        this.count = count;
        this.visible = visibile;
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
        sb.append(" )");
        return sb.toString();
    }

    /* renamed from: clone */
    public NotificationVisibility m73clone() {
        return obtain(this.key, this.rank, this.count, this.visible);
    }

    public int hashCode() {
        if (this.key == null) {
            return 0;
        }
        return this.key.hashCode();
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
    }

    private synchronized void readFromParcel(Parcel in) {
        this.key = in.readString();
        this.rank = in.readInt();
        this.count = in.readInt();
        this.visible = in.readInt() != 0;
    }

    public static synchronized NotificationVisibility obtain(String key, int rank, int count, boolean visible) {
        NotificationVisibility vo = obtain();
        vo.key = key;
        vo.rank = rank;
        vo.count = count;
        vo.visible = visible;
        return vo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized NotificationVisibility obtain(Parcel in) {
        NotificationVisibility vo = obtain();
        vo.readFromParcel(in);
        return vo;
    }

    private static synchronized NotificationVisibility obtain() {
        synchronized (sPool) {
            if (!sPool.isEmpty()) {
                return sPool.poll();
            }
            return new NotificationVisibility();
        }
    }

    public synchronized void recycle() {
        if (this.key == null) {
            return;
        }
        this.key = null;
        if (sPool.size() < 25) {
            synchronized (sPool) {
                sPool.offer(this);
            }
        }
    }
}
