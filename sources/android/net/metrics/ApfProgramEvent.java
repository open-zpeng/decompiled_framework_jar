package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.util.MessageUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
/* loaded from: classes2.dex */
public final class ApfProgramEvent implements Parcelable {
    public static final Parcelable.Creator<ApfProgramEvent> CREATOR = new Parcelable.Creator<ApfProgramEvent>() { // from class: android.net.metrics.ApfProgramEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ApfProgramEvent createFromParcel(Parcel in) {
            return new ApfProgramEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ApfProgramEvent[] newArray(int size) {
            return new ApfProgramEvent[size];
        }
    };
    public static final int FLAG_HAS_IPV4_ADDRESS = 1;
    public static final int FLAG_MULTICAST_FILTER_ON = 0;
    private protected long actualLifetime;
    private protected int currentRas;
    private protected int filteredRas;
    private protected int flags;
    private protected long lifetime;
    private protected int programLength;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Flags {
    }

    private protected ApfProgramEvent() {
    }

    private synchronized ApfProgramEvent(Parcel in) {
        this.lifetime = in.readLong();
        this.actualLifetime = in.readLong();
        this.filteredRas = in.readInt();
        this.currentRas = in.readInt();
        this.programLength = in.readInt();
        this.flags = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.lifetime);
        out.writeLong(this.actualLifetime);
        out.writeInt(this.filteredRas);
        out.writeInt(this.currentRas);
        out.writeInt(this.programLength);
        out.writeInt(flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        String lifetimeString;
        if (this.lifetime < Long.MAX_VALUE) {
            lifetimeString = this.lifetime + "s";
        } else {
            lifetimeString = "forever";
        }
        return String.format("ApfProgramEvent(%d/%d RAs %dB %ds/%s %s)", Integer.valueOf(this.filteredRas), Integer.valueOf(this.currentRas), Integer.valueOf(this.programLength), Long.valueOf(this.actualLifetime), lifetimeString, namesOf(this.flags));
    }

    private protected static int flagsFor(boolean hasIPv4, boolean multicastFilterOn) {
        int bitfield = 0;
        if (hasIPv4) {
            bitfield = 0 | 2;
        }
        if (multicastFilterOn) {
            return bitfield | 1;
        }
        return bitfield;
    }

    private static synchronized String namesOf(int bitfield) {
        List<String> names = new ArrayList<>(Integer.bitCount(bitfield));
        BitSet set = BitSet.valueOf(new long[]{Integer.MAX_VALUE & bitfield});
        for (int bit = set.nextSetBit(0); bit >= 0; bit = set.nextSetBit(bit + 1)) {
            names.add(Decoder.constants.get(bit));
        }
        return TextUtils.join("|", names);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class Decoder {
        static final SparseArray<String> constants = MessageUtils.findMessageNames(new Class[]{ApfProgramEvent.class}, new String[]{"FLAG_"});

        synchronized Decoder() {
        }
    }
}
