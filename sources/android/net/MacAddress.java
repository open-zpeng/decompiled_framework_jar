package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.SettingsStringUtil;
import com.android.internal.util.BitUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/* loaded from: classes2.dex */
public final class MacAddress implements Parcelable {
    private static final int ETHER_ADDR_LEN = 6;
    public static final int TYPE_BROADCAST = 3;
    public static final int TYPE_MULTICAST = 2;
    public static final int TYPE_UNICAST = 1;
    public static final int TYPE_UNKNOWN = 0;
    private static final long VALID_LONG_MASK = 281474976710655L;
    private final long mAddr;
    private static final byte[] ETHER_ADDR_BROADCAST = addr(255, 255, 255, 255, 255, 255);
    public static final MacAddress BROADCAST_ADDRESS = fromBytes(ETHER_ADDR_BROADCAST);
    @UnsupportedAppUsage
    public static final MacAddress ALL_ZEROS_ADDRESS = new MacAddress(0);
    private static final long LOCALLY_ASSIGNED_MASK = fromString("2:0:0:0:0:0").mAddr;
    private static final long MULTICAST_MASK = fromString("1:0:0:0:0:0").mAddr;
    private static final long OUI_MASK = fromString("ff:ff:ff:0:0:0").mAddr;
    private static final long NIC_MASK = fromString("0:0:0:ff:ff:ff").mAddr;
    private static final MacAddress BASE_GOOGLE_MAC = fromString("da:a1:19:0:0:0");
    public static final Parcelable.Creator<MacAddress> CREATOR = new Parcelable.Creator<MacAddress>() { // from class: android.net.MacAddress.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MacAddress createFromParcel(Parcel in) {
            return new MacAddress(in.readLong());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MacAddress[] newArray(int size) {
            return new MacAddress[size];
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface MacAddressType {
    }

    private MacAddress(long addr) {
        this.mAddr = VALID_LONG_MASK & addr;
    }

    public int getAddressType() {
        if (equals(BROADCAST_ADDRESS)) {
            return 3;
        }
        if (isMulticastAddress()) {
            return 2;
        }
        return 1;
    }

    public boolean isMulticastAddress() {
        return (this.mAddr & MULTICAST_MASK) != 0;
    }

    public boolean isLocallyAssigned() {
        return (this.mAddr & LOCALLY_ASSIGNED_MASK) != 0;
    }

    public byte[] toByteArray() {
        return byteAddrFromLongAddr(this.mAddr);
    }

    public String toString() {
        return stringAddrFromLongAddr(this.mAddr);
    }

    public String toOuiString() {
        return String.format("%02x:%02x:%02x", Long.valueOf((this.mAddr >> 40) & 255), Long.valueOf((this.mAddr >> 32) & 255), Long.valueOf((this.mAddr >> 24) & 255));
    }

    public int hashCode() {
        long j = this.mAddr;
        return (int) (j ^ (j >> 32));
    }

    public boolean equals(Object o) {
        return (o instanceof MacAddress) && ((MacAddress) o).mAddr == this.mAddr;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.mAddr);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static boolean isMacAddress(byte[] addr) {
        return addr != null && addr.length == 6;
    }

    public static int macAddressType(byte[] addr) {
        if (!isMacAddress(addr)) {
            return 0;
        }
        return fromBytes(addr).getAddressType();
    }

    public static byte[] byteAddrFromStringAddr(String addr) {
        Preconditions.checkNotNull(addr);
        String[] parts = addr.split(SettingsStringUtil.DELIMITER);
        if (parts.length != 6) {
            throw new IllegalArgumentException(addr + " was not a valid MAC address");
        }
        byte[] bytes = new byte[6];
        for (int i = 0; i < 6; i++) {
            int x = Integer.valueOf(parts[i], 16).intValue();
            if (x < 0 || 255 < x) {
                throw new IllegalArgumentException(addr + "was not a valid MAC address");
            }
            bytes[i] = (byte) x;
        }
        return bytes;
    }

    public static String stringAddrFromByteAddr(byte[] addr) {
        if (!isMacAddress(addr)) {
            return null;
        }
        return String.format("%02x:%02x:%02x:%02x:%02x:%02x", Byte.valueOf(addr[0]), Byte.valueOf(addr[1]), Byte.valueOf(addr[2]), Byte.valueOf(addr[3]), Byte.valueOf(addr[4]), Byte.valueOf(addr[5]));
    }

    private static byte[] byteAddrFromLongAddr(long addr) {
        byte[] bytes = new byte[6];
        int index = 6;
        while (true) {
            int index2 = index - 1;
            if (index > 0) {
                bytes[index2] = (byte) addr;
                addr >>= 8;
                index = index2;
            } else {
                return bytes;
            }
        }
    }

    private static long longAddrFromByteAddr(byte[] addr) {
        Preconditions.checkNotNull(addr);
        if (!isMacAddress(addr)) {
            throw new IllegalArgumentException(Arrays.toString(addr) + " was not a valid MAC address");
        }
        long longAddr = 0;
        for (byte b : addr) {
            longAddr = (longAddr << 8) + BitUtils.uint8(b);
        }
        return longAddr;
    }

    private static long longAddrFromStringAddr(String addr) {
        Preconditions.checkNotNull(addr);
        String[] parts = addr.split(SettingsStringUtil.DELIMITER);
        if (parts.length != 6) {
            throw new IllegalArgumentException(addr + " was not a valid MAC address");
        }
        long longAddr = 0;
        for (String str : parts) {
            int x = Integer.valueOf(str, 16).intValue();
            if (x < 0 || 255 < x) {
                throw new IllegalArgumentException(addr + "was not a valid MAC address");
            }
            longAddr = x + (longAddr << 8);
        }
        return longAddr;
    }

    private static String stringAddrFromLongAddr(long addr) {
        return String.format("%02x:%02x:%02x:%02x:%02x:%02x", Long.valueOf((addr >> 40) & 255), Long.valueOf((addr >> 32) & 255), Long.valueOf((addr >> 24) & 255), Long.valueOf((addr >> 16) & 255), Long.valueOf((addr >> 8) & 255), Long.valueOf(addr & 255));
    }

    public static MacAddress fromString(String addr) {
        return new MacAddress(longAddrFromStringAddr(addr));
    }

    public static MacAddress fromBytes(byte[] addr) {
        return new MacAddress(longAddrFromByteAddr(addr));
    }

    public static MacAddress createRandomUnicastAddressWithGoogleBase() {
        return createRandomUnicastAddress(BASE_GOOGLE_MAC, new SecureRandom());
    }

    public static MacAddress createRandomUnicastAddress() {
        SecureRandom r = new SecureRandom();
        long addr = r.nextLong() & VALID_LONG_MASK;
        return new MacAddress((addr | LOCALLY_ASSIGNED_MASK) & (~MULTICAST_MASK));
    }

    public static MacAddress createRandomUnicastAddress(MacAddress base, Random r) {
        long addr = (base.mAddr & OUI_MASK) | (NIC_MASK & r.nextLong());
        return new MacAddress((addr | LOCALLY_ASSIGNED_MASK) & (~MULTICAST_MASK));
    }

    private static byte[] addr(int... in) {
        if (in.length != 6) {
            throw new IllegalArgumentException(Arrays.toString(in) + " was not an array with length equal to 6");
        }
        byte[] out = new byte[6];
        for (int i = 0; i < 6; i++) {
            out[i] = (byte) in[i];
        }
        return out;
    }

    public boolean matches(MacAddress baseAddress, MacAddress mask) {
        Preconditions.checkNotNull(baseAddress);
        Preconditions.checkNotNull(mask);
        long j = this.mAddr;
        long j2 = mask.mAddr;
        return (j & j2) == (j2 & baseAddress.mAddr);
    }

    public Inet6Address getLinkLocalIpv6FromEui48Mac() {
        byte[] macEui48Bytes = toByteArray();
        byte[] addr = {-2, Byte.MIN_VALUE, 0, 0, 0, 0, 0, 0, (byte) (macEui48Bytes[0] ^ 2), macEui48Bytes[1], macEui48Bytes[2], -1, -2, macEui48Bytes[3], macEui48Bytes[4], macEui48Bytes[5]};
        try {
            return Inet6Address.getByAddress((String) null, addr, 0);
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
