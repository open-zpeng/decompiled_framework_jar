package android.speech.tts;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.logging.nano.MetricsProto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
/* loaded from: classes2.dex */
public class Voice implements Parcelable {
    public static final Parcelable.Creator<Voice> CREATOR = new Parcelable.Creator<Voice>() { // from class: android.speech.tts.Voice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Voice createFromParcel(Parcel in) {
            return new Voice(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Voice[] newArray(int size) {
            return new Voice[size];
        }
    };
    public static final int LATENCY_HIGH = 400;
    public static final int LATENCY_LOW = 200;
    public static final int LATENCY_NORMAL = 300;
    public static final int LATENCY_VERY_HIGH = 500;
    public static final int LATENCY_VERY_LOW = 100;
    public static final int QUALITY_HIGH = 400;
    public static final int QUALITY_LOW = 200;
    public static final int QUALITY_NORMAL = 300;
    public static final int QUALITY_VERY_HIGH = 500;
    public static final int QUALITY_VERY_LOW = 100;
    private final Set<String> mFeatures;
    private final int mLatency;
    private final Locale mLocale;
    private final String mName;
    private final int mQuality;
    private final boolean mRequiresNetworkConnection;

    public Voice(String name, Locale locale, int quality, int latency, boolean requiresNetworkConnection, Set<String> features) {
        this.mName = name;
        this.mLocale = locale;
        this.mQuality = quality;
        this.mLatency = latency;
        this.mRequiresNetworkConnection = requiresNetworkConnection;
        this.mFeatures = features;
    }

    private synchronized Voice(Parcel in) {
        this.mName = in.readString();
        this.mLocale = (Locale) in.readSerializable();
        this.mQuality = in.readInt();
        this.mLatency = in.readInt();
        this.mRequiresNetworkConnection = in.readByte() == 1;
        this.mFeatures = new HashSet();
        Collections.addAll(this.mFeatures, in.readStringArray());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeSerializable(this.mLocale);
        dest.writeInt(this.mQuality);
        dest.writeInt(this.mLatency);
        dest.writeByte(this.mRequiresNetworkConnection ? (byte) 1 : (byte) 0);
        dest.writeStringList(new ArrayList(this.mFeatures));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public int getQuality() {
        return this.mQuality;
    }

    public int getLatency() {
        return this.mLatency;
    }

    public boolean isNetworkConnectionRequired() {
        return this.mRequiresNetworkConnection;
    }

    public String getName() {
        return this.mName;
    }

    public Set<String> getFeatures() {
        return this.mFeatures;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(64);
        builder.append("Voice[Name: ");
        builder.append(this.mName);
        builder.append(", locale: ");
        builder.append(this.mLocale);
        builder.append(", quality: ");
        builder.append(this.mQuality);
        builder.append(", latency: ");
        builder.append(this.mLatency);
        builder.append(", requiresNetwork: ");
        builder.append(this.mRequiresNetworkConnection);
        builder.append(", features: ");
        builder.append(this.mFeatures.toString());
        builder.append("]");
        return builder.toString();
    }

    public int hashCode() {
        int result = (31 * 1) + (this.mFeatures == null ? 0 : this.mFeatures.hashCode());
        return (31 * ((31 * ((31 * ((31 * ((31 * result) + this.mLatency)) + (this.mLocale == null ? 0 : this.mLocale.hashCode()))) + (this.mName != null ? this.mName.hashCode() : 0))) + this.mQuality)) + (this.mRequiresNetworkConnection ? MetricsProto.MetricsEvent.AUTOFILL_SERVICE_DISABLED_APP : MetricsProto.MetricsEvent.ANOMALY_TYPE_UNOPTIMIZED_BT);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Voice other = (Voice) obj;
        if (this.mFeatures == null) {
            if (other.mFeatures != null) {
                return false;
            }
        } else if (!this.mFeatures.equals(other.mFeatures)) {
            return false;
        }
        if (this.mLatency != other.mLatency) {
            return false;
        }
        if (this.mLocale == null) {
            if (other.mLocale != null) {
                return false;
            }
        } else if (!this.mLocale.equals(other.mLocale)) {
            return false;
        }
        if (this.mName == null) {
            if (other.mName != null) {
                return false;
            }
        } else if (!this.mName.equals(other.mName)) {
            return false;
        }
        if (this.mQuality == other.mQuality && this.mRequiresNetworkConnection == other.mRequiresNetworkConnection) {
            return true;
        }
        return false;
    }
}
