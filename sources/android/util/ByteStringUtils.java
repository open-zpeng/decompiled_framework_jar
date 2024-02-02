package android.util;
/* loaded from: classes2.dex */
public final class ByteStringUtils {
    private static final char[] HEX_LOWERCASE_ARRAY = "0123456789abcdef".toCharArray();
    private static final char[] HEX_UPPERCASE_ARRAY = "0123456789ABCDEF".toCharArray();

    private synchronized ByteStringUtils() {
    }

    public static synchronized String toHexString(byte[] bytes) {
        if (bytes == null || bytes.length == 0 || bytes.length % 2 != 0) {
            return null;
        }
        int byteLength = bytes.length;
        int charCount = 2 * byteLength;
        char[] chars = new char[charCount];
        for (int i = 0; i < byteLength; i++) {
            int byteHex = bytes[i] & 255;
            chars[i * 2] = HEX_UPPERCASE_ARRAY[byteHex >>> 4];
            chars[(i * 2) + 1] = HEX_UPPERCASE_ARRAY[byteHex & 15];
        }
        return new String(chars);
    }

    public static synchronized byte[] fromHexToByteArray(String str) {
        if (str == null || str.length() == 0 || str.length() % 2 != 0) {
            return null;
        }
        char[] chars = str.toCharArray();
        int charLength = chars.length;
        byte[] bytes = new byte[charLength / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (((getIndex(chars[i * 2]) << 4) & 240) | (getIndex(chars[(i * 2) + 1]) & 15));
        }
        return bytes;
    }

    private static synchronized int getIndex(char c) {
        for (int i = 0; i < HEX_UPPERCASE_ARRAY.length; i++) {
            if (HEX_UPPERCASE_ARRAY[i] == c || HEX_LOWERCASE_ARRAY[i] == c) {
                return i;
            }
        }
        return -1;
    }
}
