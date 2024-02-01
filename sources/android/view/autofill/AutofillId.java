package android.view.autofill;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public final class AutofillId implements Parcelable {
    public static final Parcelable.Creator<AutofillId> CREATOR = new Parcelable.Creator<AutofillId>() { // from class: android.view.autofill.AutofillId.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutofillId createFromParcel(Parcel source) {
            return new AutofillId(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutofillId[] newArray(int size) {
            return new AutofillId[size];
        }
    };
    private final int mViewId;
    private final boolean mVirtual;
    private final int mVirtualId;

    public AutofillId(int id) {
        this.mVirtual = false;
        this.mViewId = id;
        this.mVirtualId = -1;
    }

    public AutofillId(AutofillId parent, int virtualChildId) {
        this.mVirtual = true;
        this.mViewId = parent.mViewId;
        this.mVirtualId = virtualChildId;
    }

    public synchronized AutofillId(int parentId, int virtualChildId) {
        this.mVirtual = true;
        this.mViewId = parentId;
        this.mVirtualId = virtualChildId;
    }

    public synchronized int getViewId() {
        return this.mViewId;
    }

    public synchronized int getVirtualChildId() {
        return this.mVirtualId;
    }

    public synchronized boolean isVirtual() {
        return this.mVirtual;
    }

    public int hashCode() {
        int result = (31 * 1) + this.mViewId;
        return (31 * result) + this.mVirtualId;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AutofillId other = (AutofillId) obj;
        if (this.mViewId == other.mViewId && this.mVirtualId == other.mVirtualId) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder().append(this.mViewId);
        if (this.mVirtual) {
            builder.append(':');
            builder.append(this.mVirtualId);
        }
        return builder.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mViewId);
        parcel.writeInt(this.mVirtual ? 1 : 0);
        parcel.writeInt(this.mVirtualId);
    }

    private synchronized AutofillId(Parcel parcel) {
        this.mViewId = parcel.readInt();
        this.mVirtual = parcel.readInt() == 1;
        this.mVirtualId = parcel.readInt();
    }
}
