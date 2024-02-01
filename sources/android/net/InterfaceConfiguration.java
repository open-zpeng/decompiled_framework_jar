package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class InterfaceConfiguration implements Parcelable {
    public static final Parcelable.Creator<InterfaceConfiguration> CREATOR = new Parcelable.Creator<InterfaceConfiguration>() { // from class: android.net.InterfaceConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InterfaceConfiguration createFromParcel(Parcel in) {
            InterfaceConfiguration info = new InterfaceConfiguration();
            info.mHwAddr = in.readString();
            if (in.readByte() == 1) {
                info.mAddr = (LinkAddress) in.readParcelable(null);
            }
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                info.mFlags.add(in.readString());
            }
            return info;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InterfaceConfiguration[] newArray(int size) {
            return new InterfaceConfiguration[size];
        }
    };
    private static final String FLAG_DOWN = "down";
    private static final String FLAG_UP = "up";
    private LinkAddress mAddr;
    private HashSet<String> mFlags = Sets.newHashSet();
    private String mHwAddr;

    private protected InterfaceConfiguration() {
    }

    public String toString() {
        return "mHwAddr=" + this.mHwAddr + " mAddr=" + String.valueOf(this.mAddr) + " mFlags=" + getFlags();
    }

    private protected Iterable<String> getFlags() {
        return this.mFlags;
    }

    public synchronized boolean hasFlag(String flag) {
        validateFlag(flag);
        return this.mFlags.contains(flag);
    }

    private protected void clearFlag(String flag) {
        validateFlag(flag);
        this.mFlags.remove(flag);
    }

    private protected void setFlag(String flag) {
        validateFlag(flag);
        this.mFlags.add(flag);
    }

    private protected void setInterfaceUp() {
        this.mFlags.remove(FLAG_DOWN);
        this.mFlags.add(FLAG_UP);
    }

    private protected void setInterfaceDown() {
        this.mFlags.remove(FLAG_UP);
        this.mFlags.add(FLAG_DOWN);
    }

    public synchronized void ignoreInterfaceUpDownStatus() {
        this.mFlags.remove(FLAG_UP);
        this.mFlags.remove(FLAG_DOWN);
    }

    public synchronized LinkAddress getLinkAddress() {
        return this.mAddr;
    }

    private protected void setLinkAddress(LinkAddress addr) {
        this.mAddr = addr;
    }

    public synchronized String getHardwareAddress() {
        return this.mHwAddr;
    }

    public synchronized void setHardwareAddress(String hwAddr) {
        this.mHwAddr = hwAddr;
    }

    public synchronized boolean isActive() {
        byte[] address;
        try {
            if (isUp()) {
                for (byte b : this.mAddr.getAddress().getAddress()) {
                    if (b != 0) {
                        return true;
                    }
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public synchronized boolean isUp() {
        return hasFlag(FLAG_UP);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mHwAddr);
        if (this.mAddr != null) {
            dest.writeByte((byte) 1);
            dest.writeParcelable(this.mAddr, flags);
        } else {
            dest.writeByte((byte) 0);
        }
        dest.writeInt(this.mFlags.size());
        Iterator<String> it = this.mFlags.iterator();
        while (it.hasNext()) {
            String flag = it.next();
            dest.writeString(flag);
        }
    }

    private static synchronized void validateFlag(String flag) {
        if (flag.indexOf(32) >= 0) {
            throw new IllegalArgumentException("flag contains space: " + flag);
        }
    }
}
