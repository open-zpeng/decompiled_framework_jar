package android.text;

import android.icu.lang.UCharacter;
import android.mtp.MtpConstants;
/* loaded from: classes2.dex */
public class Emoji {
    public static int COMBINING_ENCLOSING_KEYCAP = 8419;
    public static int ZERO_WIDTH_JOINER = MtpConstants.RESPONSE_OBJECT_WRITE_PROTECTED;
    public static int VARIATION_SELECTOR_16 = 65039;
    public static int CANCEL_TAG = 917631;

    public static synchronized boolean isRegionalIndicatorSymbol(int codePoint) {
        return 127462 <= codePoint && codePoint <= 127487;
    }

    public static synchronized boolean isEmojiModifier(int codePoint) {
        return UCharacter.hasBinaryProperty(codePoint, 59);
    }

    public static synchronized boolean isEmojiModifierBase(int c) {
        if (c == 129309 || c == 129340) {
            return true;
        }
        if ((129461 <= c && c <= 129462) || (129464 <= c && c <= 129465)) {
            return true;
        }
        return UCharacter.hasBinaryProperty(c, 60);
    }

    public static synchronized boolean isNewEmoji(int c) {
        if (c < 128761 || c > 129535) {
            return false;
        }
        return c == 9823 || c == 9854 || c == 128761 || c == 129402 || (129357 <= c && c <= 129359) || ((129388 <= c && c <= 129392) || ((129395 <= c && c <= 129398) || ((129404 <= c && c <= 129407) || ((129432 <= c && c <= 129442) || ((129456 <= c && c <= 129465) || ((129473 <= c && c <= 129474) || (129511 <= c && c <= 129535)))))));
    }

    public static synchronized boolean isEmoji(int codePoint) {
        return isNewEmoji(codePoint) || UCharacter.hasBinaryProperty(codePoint, 57);
    }

    public static synchronized boolean isKeycapBase(int codePoint) {
        return (48 <= codePoint && codePoint <= 57) || codePoint == 35 || codePoint == 42;
    }

    public static synchronized boolean isTagSpecChar(int codePoint) {
        return 917536 <= codePoint && codePoint <= 917630;
    }
}
