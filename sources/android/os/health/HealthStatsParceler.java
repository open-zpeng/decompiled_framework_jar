package android.os.health;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class HealthStatsParceler implements Parcelable {
    public static final Parcelable.Creator<HealthStatsParceler> CREATOR = new Parcelable.Creator<HealthStatsParceler>() { // from class: android.os.health.HealthStatsParceler.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HealthStatsParceler createFromParcel(Parcel in) {
            return new HealthStatsParceler(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HealthStatsParceler[] newArray(int size) {
            return new HealthStatsParceler[size];
        }
    };
    private HealthStats mHealthStats;
    private HealthStatsWriter mWriter;

    private protected HealthStatsParceler(HealthStatsWriter writer) {
        this.mWriter = writer;
    }

    private protected HealthStatsParceler(Parcel in) {
        this.mHealthStats = new HealthStats(in);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (this.mWriter != null) {
            this.mWriter.flattenToParcel(out);
            return;
        }
        throw new RuntimeException("Can not re-parcel HealthStatsParceler that was constructed from a Parcel");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HealthStats getHealthStats() {
        if (this.mWriter != null) {
            Parcel parcel = Parcel.obtain();
            this.mWriter.flattenToParcel(parcel);
            parcel.setDataPosition(0);
            this.mHealthStats = new HealthStats(parcel);
            parcel.recycle();
        }
        return this.mHealthStats;
    }
}
