package android.net;

import android.annotation.UnsupportedAppUsage;
import android.net.DnsResolver;
import android.net.shared.Inet4AddressUtils;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
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
    private static final int[] ADDRESS_FAMILIES = {OsConstants.AF_INET, OsConstants.AF_INET6};
    private static final String TAG = "NetworkUtils";

    public static native void attachDropAllBPFFilter(FileDescriptor fileDescriptor) throws SocketException;

    public static native boolean bindProcessToNetwork(int i);

    @Deprecated
    public static native boolean bindProcessToNetworkForHostResolution(int i);

    public static native int bindSocketToNetwork(int i, int i2);

    public static native void detachBPFFilter(FileDescriptor fileDescriptor) throws SocketException;

    public static native int getBoundNetworkForProcess();

    public static native Network getDnsNetwork() throws ErrnoException;

    public static native TcpRepairWindow getTcpRepairWindow(FileDescriptor fileDescriptor) throws ErrnoException;

    public static native boolean protectFromVpn(int i);

    public static native boolean queryUserAccess(int i, int i2);

    public static native void resNetworkCancel(FileDescriptor fileDescriptor);

    public static native FileDescriptor resNetworkQuery(int i, String str, int i2, int i3, int i4) throws ErrnoException;

    public static native DnsResolver.DnsResponse resNetworkResult(FileDescriptor fileDescriptor) throws ErrnoException;

    public static native FileDescriptor resNetworkSend(int i, byte[] bArr, int i2, int i3) throws ErrnoException;

    public static native void setupRaSocket(FileDescriptor fileDescriptor, int i) throws SocketException;

    @UnsupportedAppUsage
    public static boolean protectFromVpn(FileDescriptor fd) {
        return protectFromVpn(fd.getInt$());
    }

    @UnsupportedAppUsage
    @Deprecated
    public static InetAddress intToInetAddress(int hostAddress) {
        return Inet4AddressUtils.intToInet4AddressHTL(hostAddress);
    }

    @Deprecated
    public static int inetAddressToInt(Inet4Address inetAddr) throws IllegalArgumentException {
        return Inet4AddressUtils.inet4AddressToIntHTL(inetAddr);
    }

    @UnsupportedAppUsage
    @Deprecated
    public static int prefixLengthToNetmaskInt(int prefixLength) throws IllegalArgumentException {
        return Inet4AddressUtils.prefixLengthToV4NetmaskIntHTL(prefixLength);
    }

    public static int netmaskIntToPrefixLength(int netmask) {
        return Integer.bitCount(netmask);
    }

    @UnsupportedAppUsage
    @Deprecated
    public static int netmaskToPrefixLength(Inet4Address netmask) {
        return Inet4AddressUtils.netmaskToPrefixLength(netmask);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public static InetAddress numericToInetAddress(String addrString) throws IllegalArgumentException {
        return InetAddress.parseNumericAddress(addrString);
    }

    public static void maskRawAddress(byte[] array, int prefixLength) {
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

    public static InetAddress getNetworkPart(InetAddress address, int prefixLength) {
        byte[] array = address.getAddress();
        maskRawAddress(array, prefixLength);
        try {
            InetAddress netPart = InetAddress.getByAddress(array);
            return netPart;
        } catch (UnknownHostException e) {
            throw new RuntimeException("getNetworkPart error - " + e.toString());
        }
    }

    @UnsupportedAppUsage
    public static int getImplicitNetmask(Inet4Address address) {
        return Inet4AddressUtils.getImplicitNetmask(address);
    }

    public static Pair<InetAddress, Integer> parseIpAndMask(String ipAndMaskString) {
        InetAddress address = null;
        int prefixLength = -1;
        try {
            String[] pieces = ipAndMaskString.split("/", 2);
            prefixLength = Integer.parseInt(pieces[1]);
            address = InetAddress.parseNumericAddress(pieces[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (IllegalArgumentException e2) {
        } catch (NullPointerException e3) {
        } catch (NumberFormatException e4) {
        }
        if (address == null || prefixLength == -1) {
            throw new IllegalArgumentException("Invalid IP address and mask " + ipAndMaskString);
        }
        return new Pair<>(address, Integer.valueOf(prefixLength));
    }

    public static boolean addressTypeMatches(InetAddress left, InetAddress right) {
        return ((left instanceof Inet4Address) && (right instanceof Inet4Address)) || ((left instanceof Inet6Address) && (right instanceof Inet6Address));
    }

    public static InetAddress hexToInet6Address(String addrHexString) throws IllegalArgumentException {
        try {
            return numericToInetAddress(String.format(Locale.US, "%s:%s:%s:%s:%s:%s:%s:%s", addrHexString.substring(0, 4), addrHexString.substring(4, 8), addrHexString.substring(8, 12), addrHexString.substring(12, 16), addrHexString.substring(16, 20), addrHexString.substring(20, 24), addrHexString.substring(24, 28), addrHexString.substring(28, 32)));
        } catch (Exception e) {
            Log.e(TAG, "error in hexToInet6Address(" + addrHexString + "): " + e);
            throw new IllegalArgumentException(e);
        }
    }

    public static String[] makeStrings(Collection<InetAddress> addrs) {
        String[] result = new String[addrs.size()];
        int i = 0;
        for (InetAddress addr : addrs) {
            result[i] = addr.getHostAddress();
            i++;
        }
        return result;
    }

    @UnsupportedAppUsage
    public static String trimV4AddrZeros(String addr) {
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

    private static TreeSet<IpPrefix> deduplicatePrefixSet(TreeSet<IpPrefix> src) {
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

    public static long routedIPv4AddressCount(TreeSet<IpPrefix> prefixes) {
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

    public static BigInteger routedIPv6AddressCount(TreeSet<IpPrefix> prefixes) {
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

    public static boolean isWeaklyValidatedHostname(String hostname) {
        int[] iArr;
        if (hostname.matches("^[a-zA-Z0-9_.-]+$")) {
            for (int address_family : ADDRESS_FAMILIES) {
                if (Os.inet_pton(address_family, hostname) != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
