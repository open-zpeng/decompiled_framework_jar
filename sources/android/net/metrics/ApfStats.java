package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public final class ApfStats implements Parcelable {
    public static final Parcelable.Creator<ApfStats> CREATOR = new Parcelable.Creator<ApfStats>() { // from class: android.net.metrics.ApfStats.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ApfStats createFromParcel(Parcel in) {
            return new ApfStats(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ApfStats[] newArray(int size) {
            return new ApfStats[size];
        }
    };
    private protected int droppedRas;
    private protected long durationMs;
    private protected int matchingRas;
    private protected int maxProgramSize;
    private protected int parseErrors;
    private protected int programUpdates;
    private protected int programUpdatesAll;
    private protected int programUpdatesAllowingMulticast;
    private protected int receivedRas;
    private protected int zeroLifetimeRas;

    private protected ApfStats() {
    }

    private synchronized ApfStats(Parcel in) {
        this.durationMs = in.readLong();
        this.receivedRas = in.readInt();
        this.matchingRas = in.readInt();
        this.droppedRas = in.readInt();
        this.zeroLifetimeRas = in.readInt();
        this.parseErrors = in.readInt();
        this.programUpdates = in.readInt();
        this.programUpdatesAll = in.readInt();
        this.programUpdatesAllowingMulticast = in.readInt();
        this.maxProgramSize = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.durationMs);
        out.writeInt(this.receivedRas);
        out.writeInt(this.matchingRas);
        out.writeInt(this.droppedRas);
        out.writeInt(this.zeroLifetimeRas);
        out.writeInt(this.parseErrors);
        out.writeInt(this.programUpdates);
        out.writeInt(this.programUpdatesAll);
        out.writeInt(this.programUpdatesAllowingMulticast);
        out.writeInt(this.maxProgramSize);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ApfStats(" + String.format("%dms ", Long.valueOf(this.durationMs)) + String.format("%dB RA: {", Integer.valueOf(this.maxProgramSize)) + String.format("%d received, ", Integer.valueOf(this.receivedRas)) + String.format("%d matching, ", Integer.valueOf(this.matchingRas)) + String.format("%d dropped, ", Integer.valueOf(this.droppedRas)) + String.format("%d zero lifetime, ", Integer.valueOf(this.zeroLifetimeRas)) + String.format("%d parse errors}, ", Integer.valueOf(this.parseErrors)) + String.format("updates: {all: %d, RAs: %d, allow multicast: %d})", Integer.valueOf(this.programUpdatesAll), Integer.valueOf(this.programUpdates), Integer.valueOf(this.programUpdatesAllowingMulticast));
    }
}
