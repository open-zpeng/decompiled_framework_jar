package android.net.lowpan;

import android.icu.text.StringPrep;
import android.icu.text.StringPrepParseException;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.android.internal.util.HexDump;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes2.dex */
public class LowpanIdentity implements Parcelable {
    private protected static final int UNSPECIFIED_CHANNEL = -1;
    private protected static final int UNSPECIFIED_PANID = -1;
    public protected static final String TAG = LowpanIdentity.class.getSimpleName();
    private protected static final Parcelable.Creator<LowpanIdentity> CREATOR = new Parcelable.Creator<LowpanIdentity>() { // from class: android.net.lowpan.LowpanIdentity.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LowpanIdentity createFromParcel(Parcel in) {
            Builder builder = new Builder();
            builder.setRawName(in.createByteArray());
            builder.setType(in.readString());
            builder.setXpanid(in.createByteArray());
            builder.setPanid(in.readInt());
            builder.setChannel(in.readInt());
            return builder.build();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LowpanIdentity[] newArray(int size) {
            return new LowpanIdentity[size];
        }
    };
    public protected String mName = "";
    public protected boolean mIsNameValid = true;
    public protected byte[] mRawName = new byte[0];
    public protected String mType = "";
    public protected byte[] mXpanid = new byte[0];
    public protected int mPanid = -1;
    public protected int mChannel = -1;

    /* loaded from: classes2.dex */
    public static class Builder {
        public protected static final StringPrep stringPrep = StringPrep.getInstance(8);
        public private protected final LowpanIdentity mIdentity = new LowpanIdentity();

        public protected static synchronized String escape(byte[] bytes) {
            StringBuffer sb = new StringBuffer();
            for (byte b : bytes) {
                if (b >= 32 && b <= 126) {
                    sb.append((char) b);
                } else {
                    sb.append(String.format("\\0x%02x", Integer.valueOf(b & 255)));
                }
            }
            return sb.toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Builder setLowpanIdentity(LowpanIdentity x) {
            Objects.requireNonNull(x);
            setRawName(x.getRawName());
            setXpanid(x.getXpanid());
            setPanid(x.getPanid());
            setChannel(x.getChannel());
            setType(x.getType());
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Builder setName(String name) {
            Objects.requireNonNull(name);
            try {
                this.mIdentity.mName = stringPrep.prepare(name, 0);
                this.mIdentity.mRawName = this.mIdentity.mName.getBytes(StandardCharsets.UTF_8);
                this.mIdentity.mIsNameValid = true;
            } catch (StringPrepParseException x) {
                Log.w(LowpanIdentity.TAG, x.toString());
                setRawName(name.getBytes(StandardCharsets.UTF_8));
            }
            return this;
        }

        private protected synchronized Builder setRawName(byte[] name) {
            Objects.requireNonNull(name);
            this.mIdentity.mRawName = (byte[]) name.clone();
            this.mIdentity.mName = new String(name, StandardCharsets.UTF_8);
            try {
                String nameCheck = stringPrep.prepare(this.mIdentity.mName, 0);
                this.mIdentity.mIsNameValid = Arrays.equals(nameCheck.getBytes(StandardCharsets.UTF_8), name);
            } catch (StringPrepParseException x) {
                Log.w(LowpanIdentity.TAG, x.toString());
                this.mIdentity.mIsNameValid = false;
            }
            if (!this.mIdentity.mIsNameValid) {
                LowpanIdentity lowpanIdentity = this.mIdentity;
                lowpanIdentity.mName = "«" + escape(name) + "»";
            }
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Builder setXpanid(byte[] x) {
            this.mIdentity.mXpanid = x != null ? (byte[]) x.clone() : null;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Builder setPanid(int x) {
            this.mIdentity.mPanid = x;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Builder setType(String x) {
            this.mIdentity.mType = x;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Builder setChannel(int x) {
            this.mIdentity.mChannel = x;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized LowpanIdentity build() {
            return this.mIdentity;
        }
    }

    private protected synchronized String getName() {
        return this.mName;
    }

    private protected synchronized boolean isNameValid() {
        return this.mIsNameValid;
    }

    private protected synchronized byte[] getRawName() {
        return (byte[]) this.mRawName.clone();
    }

    private protected synchronized byte[] getXpanid() {
        return (byte[]) this.mXpanid.clone();
    }

    private protected synchronized int getPanid() {
        return this.mPanid;
    }

    private protected synchronized String getType() {
        return this.mType;
    }

    private protected synchronized int getChannel() {
        return this.mChannel;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Name:");
        sb.append(getName());
        if (this.mType.length() > 0) {
            sb.append(", Type:");
            sb.append(this.mType);
        }
        if (this.mXpanid.length > 0) {
            sb.append(", XPANID:");
            sb.append(HexDump.toHexString(this.mXpanid));
        }
        if (this.mPanid != -1) {
            sb.append(", PANID:");
            sb.append(String.format("0x%04X", Integer.valueOf(this.mPanid)));
        }
        if (this.mChannel != -1) {
            sb.append(", Channel:");
            sb.append(this.mChannel);
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof LowpanIdentity) {
            LowpanIdentity rhs = (LowpanIdentity) obj;
            return Arrays.equals(this.mRawName, rhs.mRawName) && Arrays.equals(this.mXpanid, rhs.mXpanid) && this.mType.equals(rhs.mType) && this.mPanid == rhs.mPanid && this.mChannel == rhs.mChannel;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(Arrays.hashCode(this.mRawName)), this.mType, Integer.valueOf(Arrays.hashCode(this.mXpanid)), Integer.valueOf(this.mPanid), Integer.valueOf(this.mChannel));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.mRawName);
        dest.writeString(this.mType);
        dest.writeByteArray(this.mXpanid);
        dest.writeInt(this.mPanid);
        dest.writeInt(this.mChannel);
    }
}
