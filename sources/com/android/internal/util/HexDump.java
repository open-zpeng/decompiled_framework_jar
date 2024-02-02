package com.android.internal.util;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.internal.midi.MidiConstants;
/* loaded from: classes3.dex */
public class HexDump {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_LOWER_CASE_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String dumpHexString(byte[] array) {
        return dumpHexString(array, 0, array.length);
    }

    public static String dumpHexString(byte[] array, int offset, int length) {
        int j;
        StringBuilder result = new StringBuilder();
        byte[] line = new byte[16];
        result.append("\n0x");
        result.append(toHexString(offset));
        int lineIndex = 0;
        int lineIndex2 = offset;
        while (true) {
            j = 0;
            if (lineIndex2 >= offset + length) {
                break;
            }
            if (lineIndex == 16) {
                result.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                while (true) {
                    int j2 = j;
                    if (j2 >= 16) {
                        break;
                    }
                    if (line[j2] > 32 && line[j2] < 126) {
                        result.append(new String(line, j2, 1));
                    } else {
                        result.append(".");
                    }
                    j = j2 + 1;
                }
                result.append("\n0x");
                result.append(toHexString(lineIndex2));
                lineIndex = 0;
            }
            byte b = array[lineIndex2];
            result.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            result.append(HEX_DIGITS[(b >>> 4) & 15]);
            result.append(HEX_DIGITS[b & MidiConstants.STATUS_CHANNEL_MASK]);
            line[lineIndex] = b;
            lineIndex2++;
            lineIndex++;
        }
        if (lineIndex != 16) {
            int count = (16 - lineIndex) * 3;
            int count2 = count + 1;
            for (int i = 0; i < count2; i++) {
                result.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            while (true) {
                int i2 = j;
                if (i2 >= lineIndex) {
                    break;
                }
                if (line[i2] > 32 && line[i2] < 126) {
                    result.append(new String(line, i2, 1));
                } else {
                    result.append(".");
                }
                j = i2 + 1;
            }
        }
        return result.toString();
    }

    public static String toHexString(byte b) {
        return toHexString(toByteArray(b));
    }

    public static String toHexString(byte[] array) {
        return toHexString(array, 0, array.length, true);
    }

    public static String toHexString(byte[] array, boolean upperCase) {
        return toHexString(array, 0, array.length, upperCase);
    }

    public static String toHexString(byte[] array, int offset, int length) {
        return toHexString(array, offset, length, true);
    }

    public static String toHexString(byte[] array, int offset, int length, boolean upperCase) {
        char[] digits = upperCase ? HEX_DIGITS : HEX_LOWER_CASE_DIGITS;
        char[] buf = new char[length * 2];
        int bufIndex = 0;
        for (int bufIndex2 = offset; bufIndex2 < offset + length; bufIndex2++) {
            byte b = array[bufIndex2];
            int bufIndex3 = bufIndex + 1;
            buf[bufIndex] = digits[(b >>> 4) & 15];
            bufIndex = bufIndex3 + 1;
            buf[bufIndex3] = digits[b & MidiConstants.STATUS_CHANNEL_MASK];
        }
        return new String(buf);
    }

    public static String toHexString(int i) {
        return toHexString(toByteArray(i));
    }

    public static byte[] toByteArray(byte b) {
        byte[] array = {b};
        return array;
    }

    public static byte[] toByteArray(int i) {
        byte[] array = {(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
        return array;
    }

    private static int toByte(char c) {
        if (c < '0' || c > '9') {
            if (c < 'A' || c > 'F') {
                if (c < 'a' || c > 'f') {
                    throw new RuntimeException("Invalid hex char '" + c + "'");
                }
                return (c - 'a') + 10;
            }
            return (c - 'A') + 10;
        }
        return c - '0';
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] buffer = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            buffer[i / 2] = (byte) ((toByte(hexString.charAt(i)) << 4) | toByte(hexString.charAt(i + 1)));
        }
        return buffer;
    }

    public static StringBuilder appendByteAsHex(StringBuilder sb, byte b, boolean upperCase) {
        char[] digits = upperCase ? HEX_DIGITS : HEX_LOWER_CASE_DIGITS;
        sb.append(digits[(b >> 4) & 15]);
        sb.append(digits[b & MidiConstants.STATUS_CHANNEL_MASK]);
        return sb;
    }
}
