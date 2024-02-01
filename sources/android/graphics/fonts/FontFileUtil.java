package android.graphics.fonts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes.dex */
public class FontFileUtil {
    private static final int ANALYZE_ERROR = -1;
    private static final int OS2_TABLE_TAG = 1330851634;
    private static final int SFNT_VERSION_1 = 65536;
    private static final int SFNT_VERSION_OTTO = 1330926671;
    private static final int TTC_TAG = 1953784678;

    private FontFileUtil() {
    }

    public static int unpackWeight(int packed) {
        return 65535 & packed;
    }

    public static boolean unpackItalic(int packed) {
        return (65536 & packed) != 0;
    }

    public static boolean isSuccess(int packed) {
        return packed != -1;
    }

    private static int pack(int weight, boolean italic) {
        return (italic ? 65536 : 0) | weight;
    }

    public static final int analyzeStyle(ByteBuffer buffer, int ttcIndex, FontVariationAxis[] varSettings) {
        int italic;
        int italic2;
        boolean z;
        if (varSettings != null) {
            int length = varSettings.length;
            italic = -1;
            italic2 = -1;
            int weight = 0;
            while (weight < length) {
                FontVariationAxis axis = varSettings[weight];
                if ("wght".equals(axis.getTag())) {
                    italic2 = (int) axis.getStyleValue();
                } else if ("ital".equals(axis.getTag())) {
                    italic = axis.getStyleValue() == 1.0f ? 1 : 0;
                }
                weight++;
                italic2 = italic2;
            }
        } else {
            italic = -1;
            italic2 = -1;
        }
        if (italic2 != -1 && italic != -1) {
            return pack(italic2, italic == 1);
        }
        ByteOrder originalOrder = buffer.order();
        buffer.order(ByteOrder.BIG_ENDIAN);
        int fontFileOffset = 0;
        try {
            int magicNumber = buffer.getInt(0);
            if (magicNumber == TTC_TAG) {
                if (ttcIndex >= buffer.getInt(8)) {
                    return -1;
                }
                fontFileOffset = buffer.getInt((ttcIndex * 4) + 12);
            }
            int sfntVersion = buffer.getInt(fontFileOffset);
            if (sfntVersion == 65536 || sfntVersion == SFNT_VERSION_OTTO) {
                int numTables = buffer.getShort(fontFileOffset + 4);
                int os2TableOffset = -1;
                int i = 0;
                while (true) {
                    if (i >= numTables) {
                        break;
                    }
                    int tableOffset = fontFileOffset + 12 + (i * 16);
                    if (buffer.getInt(tableOffset) == OS2_TABLE_TAG) {
                        os2TableOffset = buffer.getInt(tableOffset + 8);
                        break;
                    }
                    i++;
                }
                if (os2TableOffset == -1) {
                    return pack(400, false);
                }
                int weightFromOS2 = buffer.getShort(os2TableOffset + 4);
                boolean italicFromOS2 = (buffer.getShort(os2TableOffset + 62) & 1) != 0;
                int i2 = italic2 == -1 ? weightFromOS2 : italic2;
                if (italic == -1) {
                    z = italicFromOS2;
                } else {
                    z = true;
                    if (italic != 1) {
                        z = false;
                    }
                }
                return pack(i2, z);
            }
            return -1;
        } finally {
            buffer.order(originalOrder);
        }
    }
}
