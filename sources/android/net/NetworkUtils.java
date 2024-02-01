package android.net;

import android.os.Parcel;
import android.util.Log;
import android.util.Pair;
import java.io.FileDescriptor;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
/* loaded from: classes2.dex */
public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

    private protected static native void attachControlPacketFilter(FileDescriptor fileDescriptor, int i) throws SocketException;

    private protected static native void attachDhcpFilter(FileDescriptor fileDescriptor) throws SocketException;

    private protected static native void attachRaFilter(FileDescriptor fileDescriptor, int i) throws SocketException;

    public static native boolean bindProcessToNetwork(int i);

    @Deprecated
    public static native boolean bindProcessToNetworkForHostResolution(int i);

    public static native int bindSocketToNetwork(int i, int i2);

    public static native int getBoundNetworkForProcess();

    public static native boolean protectFromVpn(int i);

    public static native boolean queryUserAccess(int i, int i2);

    public static native void setupRaSocket(FileDescriptor fileDescriptor, int i) throws SocketException;

    private protected static boolean protectFromVpn(FileDescriptor fd) {
        return protectFromVpn(fd.getInt$());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = {(byte) (255 & hostAddress), (byte) ((hostAddress >> 8) & 255), (byte) ((hostAddress >> 16) & 255), (byte) (255 & (hostAddress >> 24))};
        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }

    public static synchronized int inetAddressToInt(Inet4Address inetAddr) throws IllegalArgumentException {
        byte[] addr = inetAddr.getAddress();
        return ((addr[3] & 255) << 24) | ((addr[2] & 255) << 16) | ((addr[1] & 255) << 8) | (addr[0] & 255);
    }

    private protected static int prefixLengthToNetmaskInt(int prefixLength) throws IllegalArgumentException {
        if (prefixLength < 0 || prefixLength > 32) {
            throw new IllegalArgumentException("Invalid prefix length (0 <= prefix <= 32)");
        }
        int value = (-1) << (32 - prefixLength);
        return Integer.reverseBytes(value);
    }

    public static synchronized int netmaskIntToPrefixLength(int netmask) {
        return Integer.bitCount(netmask);
    }

    private protected static int netmaskToPrefixLength(Inet4Address netmask) {
        int i = Integer.reverseBytes(inetAddressToInt(netmask));
        int prefixLength = Integer.bitCount(i);
        int trailingZeros = Integer.numberOfTrailingZeros(i);
        if (trailingZeros != 32 - prefixLength) {
            throw new IllegalArgumentException("Non-contiguous netmask: " + Integer.toHexString(i));
        }
        return prefixLength;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static InetAddress numericToInetAddress(String addrString) throws IllegalArgumentException {
        return InetAddress.parseNumericAddress(addrString);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized void parcelInetAddress(Parcel parcel, InetAddress address, int flags) {
        byte[] addressArray = address != null ? address.getAddress() : null;
        parcel.writeByteArray(addressArray);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized InetAddress unparcelInetAddress(Parcel in) {
        byte[] addressArray = in.createByteArray();
        if (addressArray == null) {
            return null;
        }
        try {
            return InetAddress.getByAddress(addressArray);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static synchronized void maskRawAddress(byte[] array, int prefixLength) {
        if (prefixLength < 0 || prefixLength > array.length * 8) {
            throw new RuntimeException("IP address with " + array.length + " bytes has invalid prefix length " + prefixLength);
        }
        int offset = prefixLength / 8;
        int remainder = prefixLength % 8;
        byte mask = (byte) (255 << (8 - remainder));
        if (offset < array.length) {
            array[offset] = (byte) (array[offset] & mask);
        }
        while (true) {
            offset++;
            if (offset < array.length) {
                array[offset] = 0;
            } else {
                return;
            }
        }
    }

    public static synchronized InetAddress getNetworkPart(InetAddress address, int prefixLength) {
        byte[] array = address.getAddress();
        maskRawAddress(array, prefixLength);
        try {
            InetAddress netPart = InetAddress.getByAddress(array);
            return netPart;
        } catch (UnknownHostException e) {
            throw new RuntimeException("getNetworkPart error - " + e.toString());
        }
    }

    private protected static int getImplicitNetmask(Inet4Address address) {
        int firstByte = address.getAddress()[0] & 255;
        if (firstByte < 128) {
            return 8;
        }
        if (firstByte < 192) {
            return 16;
        }
        if (firstByte < 224) {
            return 24;
        }
        return 32;
    }

    public static synchronized Pair<InetAddress, Integer> parseIpAndMask(String ipAndMaskString) {
        InetAddress address = null;
        int prefixLength = -1;
        try {
            String[] pieces = ipAndMaskString.split("/", 2);
            prefixLength = Integer.parseInt(pieces[1]);
            address = InetAddress.parseNumericAddress(pieces[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NullPointerException e2) {
        } catch (NumberFormatException e3) {
        } catch (IllegalArgumentException e4) {
        }
        if (address == null || prefixLength == -1) {
            throw new IllegalArgumentException("Invalid IP address and mask " + ipAndMaskString);
        }
        return new Pair<>(address, Integer.valueOf(prefixLength));
    }

    public static synchronized boolean addressTypeMatches(InetAddress left, InetAddress right) {
        return ((left instanceof Inet4Address) && (right instanceof Inet4Address)) || ((left instanceof Inet6Address) && (right instanceof Inet6Address));
    }

    public static synchronized InetAddress hexToInet6Address(String addrHexString) throws IllegalArgumentException {
        try {
            return numericToInetAddress(String.format(Locale.US, "%s:%s:%s:%s:%s:%s:%s:%s", addrHexString.substring(0, 4), addrHexString.substring(4, 8), addrHexString.substring(8, 12), addrHexString.substring(12, 16), addrHexString.substring(16, 20), addrHexString.substring(20, 24), addrHexString.substring(24, 28), addrHexString.substring(28, 32)));
        } catch (Exception e) {
            Log.e(TAG, "error in hexToInet6Address(" + addrHexString + "): " + e);
            throw new IllegalArgumentException(e);
        }
    }

    public static synchronized String[] makeStrings(Collection<InetAddress> addrs) {
        String[] result = new String[addrs.size()];
        int i = 0;
        for (InetAddress addr : addrs) {
            result[i] = addr.getHostAddress();
            i++;
        }
        return result;
    }

    private protected static String trimV4AddrZeros(String addr) {
        if (addr == null) {
            return null;
        }
        String[] octets = addr.split("\\.");
        if (octets.length != 4) {
            return addr;
        }
        StringBuilder builder = new StringBuilder(16);
        for (int i = 0; i < 4; i++) {
            try {
                if (octets[i].length() > 3) {
                    return addr;
                }
                builder.append(Integer.parseInt(octets[i]));
                if (i < 3) {
                    builder.append('.');
                }
            } catch (NumberFormatException e) {
                return addr;
            }
        }
        String result = builder.toString();
        return result;
    }

    private static synchronized TreeSet<IpPrefix> deduplicatePrefixSet(TreeSet<IpPrefix> src) {
        TreeSet<IpPrefix> dst = new TreeSet<>(src.comparator());
        Iterator<IpPrefix> it = src.iterator();
        while (it.hasNext()) {
            IpPrefix newPrefix = it.next();
            Iterator<IpPrefix> it2 = dst.iterator();
            while (true) {
                if (it2.hasNext()) {
                    IpPrefix existingPrefix = it2.next();
                    if (existingPrefix.containsPrefix(newPrefix)) {
                        break;
                    }
                } else {
                    dst.add(newPrefix);
                    break;
                }
            }
        }
        return dst;
    }

    public static synchronized long routedIPv4AddressCount(TreeSet<IpPrefix> prefixes) {
        long routedIPCount = 0;
        Iterator<IpPrefix> it = deduplicatePrefixSet(prefixes).iterator();
        while (it.hasNext()) {
            IpPrefix prefix = it.next();
            if (!prefix.isIPv4()) {
                Log.wtf(TAG, "Non-IPv4 prefix in routedIPv4AddressCount");
            }
            int rank = 32 - prefix.getPrefixLength();
            routedIPCount += 1 << rank;
        }
        return routedIPCount;
    }

    public static synchronized BigInteger routedIPv6AddressCount(TreeSet<IpPrefix> prefixes) {
        BigInteger routedIPCount = BigInteger.ZERO;
        Iterator<IpPrefix> it = deduplicatePrefixSet(prefixes).iterator();
        while (it.hasNext()) {
            IpPrefix prefix = it.next();
            if (!prefix.isIPv6()) {
                Log.wtf(TAG, "Non-IPv6 prefix in routedIPv6AddressCount");
            }
            int rank = 128 - prefix.getPrefixLength();
            routedIPCount = routedIPCount.add(BigInteger.ONE.shiftLeft(rank));
        }
        return routedIPCount;
    }
}
