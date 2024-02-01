package android.text.method;

import android.icu.lang.UCharacter;
import android.icu.text.DecimalFormatSymbols;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
/* loaded from: classes2.dex */
public class DigitsKeyListener extends NumberKeyListener {
    private static final int DECIMAL = 2;
    private static final String DEFAULT_DECIMAL_POINT_CHARS = ".";
    private static final String DEFAULT_SIGN_CHARS = "-+";
    private static final char EN_DASH = 8211;
    private static final char MINUS_SIGN = 8722;
    private static final int SIGN = 1;
    private char[] mAccepted;
    private final boolean mDecimal;
    private String mDecimalPointChars;
    private final Locale mLocale;
    private boolean mNeedsAdvancedInput;
    private final boolean mSign;
    private String mSignChars;
    private final boolean mStringMode;
    private static final char HYPHEN_MINUS = '-';
    private static final char[][] COMPATIBILITY_CHARACTERS = {new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}, new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', HYPHEN_MINUS, '+'}, new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'}, new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', HYPHEN_MINUS, '+', '.'}};
    private static final Object sLocaleCacheLock = new Object();
    @GuardedBy("sLocaleCacheLock")
    private static final HashMap<Locale, DigitsKeyListener[]> sLocaleInstanceCache = new HashMap<>();
    private static final Object sStringCacheLock = new Object();
    @GuardedBy("sStringCacheLock")
    private static final HashMap<String, DigitsKeyListener> sStringInstanceCache = new HashMap<>();

    @Override // android.text.method.NumberKeyListener
    protected char[] getAcceptedChars() {
        return this.mAccepted;
    }

    private synchronized boolean isSignChar(char c) {
        return this.mSignChars.indexOf(c) != -1;
    }

    private synchronized boolean isDecimalPointChar(char c) {
        return this.mDecimalPointChars.indexOf(c) != -1;
    }

    @Deprecated
    public DigitsKeyListener() {
        this(null, false, false);
    }

    @Deprecated
    public DigitsKeyListener(boolean sign, boolean decimal) {
        this(null, sign, decimal);
    }

    public DigitsKeyListener(Locale locale) {
        this(locale, false, false);
    }

    private synchronized void setToCompat() {
        this.mDecimalPointChars = DEFAULT_DECIMAL_POINT_CHARS;
        this.mSignChars = DEFAULT_SIGN_CHARS;
        int kind = (this.mSign ? 1 : 0) | (this.mDecimal ? 2 : 0);
        this.mAccepted = COMPATIBILITY_CHARACTERS[kind];
        this.mNeedsAdvancedInput = false;
    }

    private synchronized void calculateNeedForAdvancedInput() {
        int kind = (this.mSign ? 1 : 0) | (this.mDecimal ? 2 : 0);
        this.mNeedsAdvancedInput = !ArrayUtils.containsAll(COMPATIBILITY_CHARACTERS[kind], this.mAccepted);
    }

    private static synchronized String stripBidiControls(String sign) {
        String result = "";
        for (int i = 0; i < sign.length(); i++) {
            char c = sign.charAt(i);
            if (!UCharacter.hasBinaryProperty(c, 2)) {
                result = result.isEmpty() ? String.valueOf(c) : result + c;
            }
        }
        return result;
    }

    public DigitsKeyListener(Locale locale, boolean sign, boolean decimal) {
        this.mDecimalPointChars = DEFAULT_DECIMAL_POINT_CHARS;
        this.mSignChars = DEFAULT_SIGN_CHARS;
        this.mSign = sign;
        this.mDecimal = decimal;
        this.mStringMode = false;
        this.mLocale = locale;
        if (locale == null) {
            setToCompat();
            return;
        }
        LinkedHashSet<Character> chars = new LinkedHashSet<>();
        boolean success = NumberKeyListener.addDigits(chars, locale);
        if (!success) {
            setToCompat();
            return;
        }
        if (sign || decimal) {
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
            if (sign) {
                String minusString = stripBidiControls(symbols.getMinusSignString());
                String plusString = stripBidiControls(symbols.getPlusSignString());
                if (minusString.length() > 1 || plusString.length() > 1) {
                    setToCompat();
                    return;
                }
                char minus = minusString.charAt(0);
                char plus = plusString.charAt(0);
                chars.add(Character.valueOf(minus));
                chars.add(Character.valueOf(plus));
                this.mSignChars = "" + minus + plus;
                if (minus == 8722 || minus == 8211) {
                    chars.add(Character.valueOf(HYPHEN_MINUS));
                    this.mSignChars += HYPHEN_MINUS;
                }
            }
            if (decimal) {
                String separatorString = symbols.getDecimalSeparatorString();
                if (separatorString.length() > 1) {
                    setToCompat();
                    return;
                }
                Character separatorChar = Character.valueOf(separatorString.charAt(0));
                chars.add(separatorChar);
                this.mDecimalPointChars = separatorChar.toString();
            }
        }
        this.mAccepted = NumberKeyListener.collectionToArray(chars);
        calculateNeedForAdvancedInput();
    }

    private synchronized DigitsKeyListener(String accepted) {
        this.mDecimalPointChars = DEFAULT_DECIMAL_POINT_CHARS;
        this.mSignChars = DEFAULT_SIGN_CHARS;
        this.mSign = false;
        this.mDecimal = false;
        this.mStringMode = true;
        this.mLocale = null;
        this.mAccepted = new char[accepted.length()];
        accepted.getChars(0, accepted.length(), this.mAccepted, 0);
        this.mNeedsAdvancedInput = false;
    }

    @Deprecated
    public static DigitsKeyListener getInstance() {
        return getInstance(false, false);
    }

    @Deprecated
    public static DigitsKeyListener getInstance(boolean sign, boolean decimal) {
        return getInstance(null, sign, decimal);
    }

    public static DigitsKeyListener getInstance(Locale locale) {
        return getInstance(locale, false, false);
    }

    public static DigitsKeyListener getInstance(Locale locale, boolean sign, boolean decimal) {
        int kind = (decimal ? 2 : 0) | (sign ? 1 : 0);
        synchronized (sLocaleCacheLock) {
            DigitsKeyListener[] cachedValue = sLocaleInstanceCache.get(locale);
            if (cachedValue != null && cachedValue[kind] != null) {
                return cachedValue[kind];
            }
            if (cachedValue == null) {
                cachedValue = new DigitsKeyListener[4];
                sLocaleInstanceCache.put(locale, cachedValue);
            }
            DigitsKeyListener digitsKeyListener = new DigitsKeyListener(locale, sign, decimal);
            cachedValue[kind] = digitsKeyListener;
            return digitsKeyListener;
        }
    }

    public static DigitsKeyListener getInstance(String accepted) {
        DigitsKeyListener result;
        synchronized (sStringCacheLock) {
            result = sStringInstanceCache.get(accepted);
            if (result == null) {
                result = new DigitsKeyListener(accepted);
                sStringInstanceCache.put(accepted, result);
            }
        }
        return result;
    }

    public static synchronized DigitsKeyListener getInstance(Locale locale, DigitsKeyListener listener) {
        if (listener.mStringMode) {
            return listener;
        }
        return getInstance(locale, listener.mSign, listener.mDecimal);
    }

    @Override // android.text.method.KeyListener
    public int getInputType() {
        if (this.mNeedsAdvancedInput) {
            return 1;
        }
        int contentType = 2;
        if (this.mSign) {
            contentType = 2 | 4096;
        }
        if (this.mDecimal) {
            return contentType | 8192;
        }
        return contentType;
    }

    @Override // android.text.method.NumberKeyListener, android.text.InputFilter
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence source2;
        int start2;
        int end2;
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);
        if (!this.mSign && !this.mDecimal) {
            return out;
        }
        if (out != null) {
            source2 = out;
            start2 = 0;
            end2 = out.length();
        } else {
            source2 = source;
            start2 = start;
            end2 = end;
        }
        int sign = -1;
        int decimal = -1;
        int dlen = dest.length();
        for (int i = 0; i < dstart; i++) {
            char c = dest.charAt(i);
            if (isSignChar(c)) {
                sign = i;
            } else if (isDecimalPointChar(c)) {
                decimal = i;
            }
        }
        int decimal2 = decimal;
        for (int decimal3 = dend; decimal3 < dlen; decimal3++) {
            char c2 = dest.charAt(decimal3);
            if (isSignChar(c2)) {
                return "";
            }
            if (isDecimalPointChar(c2)) {
                decimal2 = decimal3;
            }
        }
        SpannableStringBuilder stripped = null;
        for (int i2 = end2 - 1; i2 >= start2; i2--) {
            char c3 = source2.charAt(i2);
            boolean strip = false;
            if (isSignChar(c3)) {
                if (i2 != start2 || dstart != 0) {
                    strip = true;
                } else if (sign >= 0) {
                    strip = true;
                } else {
                    sign = i2;
                }
            } else if (isDecimalPointChar(c3)) {
                if (decimal2 >= 0) {
                    strip = true;
                } else {
                    decimal2 = i2;
                }
            }
            if (strip) {
                if (end2 == start2 + 1) {
                    return "";
                }
                if (stripped == null) {
                    stripped = new SpannableStringBuilder(source2, start2, end2);
                }
                stripped.delete(i2 - start2, (i2 + 1) - start2);
            }
        }
        if (stripped != null) {
            return stripped;
        }
        if (out != null) {
            return out;
        }
        return null;
    }
}
