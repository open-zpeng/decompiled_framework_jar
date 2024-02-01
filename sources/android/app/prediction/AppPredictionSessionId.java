package android.app.prediction;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;

@SystemApi
/* loaded from: classes.dex */
public final class AppPredictionSessionId implements Parcelable {
    public static final Parcelable.Creator<AppPredictionSessionId> CREATOR = new Parcelable.Creator<AppPredictionSessionId>() { // from class: android.app.prediction.AppPredictionSessionId.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppPredictionSessionId createFromParcel(Parcel parcel) {
            return new AppPredictionSessionId(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppPredictionSessionId[] newArray(int size) {
            return new AppPredictionSessionId[size];
        }
    };
    private final String mId;

    public AppPredictionSessionId(String id) {
        this.mId = id;
    }

    private AppPredictionSessionId(Parcel p) {
        this.mId = p.readString();
    }

    public boolean equals(Object o) {
        if (getClass().equals(o != null ? o.getClass() : null)) {
            AppPredictionSessionId other = (AppPredictionSessionId) o;
            return this.mId.equals(other.mId);
        }
        return false;
    }

    public String toString() {
        return this.mId;
    }

    public int hashCode() {
        return this.mId.hashCode();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
    }
}
