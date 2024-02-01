package android.os;

import android.os.Parcelable;
import android.util.proto.ProtoOutputStream;

/* loaded from: classes2.dex */
public class PatternMatcher implements Parcelable {
    private static final int MAX_PATTERN_STORAGE = 2048;
    private static final int NO_MATCH = -1;
    private static final int PARSED_MODIFIER_ONE_OR_MORE = -8;
    private static final int PARSED_MODIFIER_RANGE_START = -5;
    private static final int PARSED_MODIFIER_RANGE_STOP = -6;
    private static final int PARSED_MODIFIER_ZERO_OR_MORE = -7;
    private static final int PARSED_TOKEN_CHAR_ANY = -4;
    private static final int PARSED_TOKEN_CHAR_SET_INVERSE_START = -2;
    private static final int PARSED_TOKEN_CHAR_SET_START = -1;
    private static final int PARSED_TOKEN_CHAR_SET_STOP = -3;
    public static final int PATTERN_ADVANCED_GLOB = 3;
    public static final int PATTERN_LITERAL = 0;
    public static final int PATTERN_PREFIX = 1;
    public static final int PATTERN_SIMPLE_GLOB = 2;
    private static final String TAG = "PatternMatcher";
    private static final int TOKEN_TYPE_ANY = 1;
    private static final int TOKEN_TYPE_INVERSE_SET = 3;
    private static final int TOKEN_TYPE_LITERAL = 0;
    private static final int TOKEN_TYPE_SET = 2;
    private final int[] mParsedPattern;
    private final String mPattern;
    private final int mType;
    private static final int[] sParsedPatternScratch = new int[2048];
    public static final Parcelable.Creator<PatternMatcher> CREATOR = new Parcelable.Creator<PatternMatcher>() { // from class: android.os.PatternMatcher.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PatternMatcher createFromParcel(Parcel source) {
            return new PatternMatcher(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PatternMatcher[] newArray(int size) {
            return new PatternMatcher[size];
        }
    };

    public PatternMatcher(String pattern, int type) {
        this.mPattern = pattern;
        this.mType = type;
        if (this.mType == 3) {
            this.mParsedPattern = parseAndVerifyAdvancedPattern(pattern);
        } else {
            this.mParsedPattern = null;
        }
    }

    public final String getPath() {
        return this.mPattern;
    }

    public final int getType() {
        return this.mType;
    }

    public boolean match(String str) {
        return matchPattern(str, this.mPattern, this.mParsedPattern, this.mType);
    }

    public String toString() {
        String type = "? ";
        int i = this.mType;
        if (i == 0) {
            type = "LITERAL: ";
        } else if (i == 1) {
            type = "PREFIX: ";
        } else if (i == 2) {
            type = "GLOB: ";
        } else if (i == 3) {
            type = "ADVANCED: ";
        }
        return "PatternMatcher{" + type + this.mPattern + "}";
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, this.mPattern);
        proto.write(1159641169922L, this.mType);
        proto.end(token);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPattern);
        dest.writeInt(this.mType);
        dest.writeIntArray(this.mParsedPattern);
    }

    public PatternMatcher(Parcel src) {
        this.mPattern = src.readString();
        this.mType = src.readInt();
        this.mParsedPattern = src.createIntArray();
    }

    static boolean matchPattern(String match, String pattern, int[] parsedPattern, int type) {
        if (match == null) {
            return false;
        }
        if (type == 0) {
            return pattern.equals(match);
        }
        if (type == 1) {
            return match.startsWith(pattern);
        }
        if (type == 2) {
            return matchGlobPattern(pattern, match);
        }
        if (type != 3) {
            return false;
        }
        return matchAdvancedPattern(parsedPattern, match);
    }

    static boolean matchGlobPattern(String pattern, String match) {
        int NP = pattern.length();
        if (NP <= 0) {
            return match.length() <= 0;
        }
        int NM = match.length();
        int ip = 0;
        int im = 0;
        char nextChar = pattern.charAt(0);
        while (ip < NP && im < NM) {
            char c = nextChar;
            ip++;
            nextChar = ip < NP ? pattern.charAt(ip) : (char) 0;
            boolean escaped = c == '\\';
            if (escaped) {
                c = nextChar;
                ip++;
                nextChar = ip < NP ? pattern.charAt(ip) : (char) 0;
            }
            if (nextChar == '*') {
                if (!escaped && c == '.') {
                    if (ip >= NP - 1) {
                        return true;
                    }
                    int ip2 = ip + 1;
                    char nextChar2 = pattern.charAt(ip2);
                    if (nextChar2 == '\\') {
                        ip2++;
                        nextChar2 = ip2 < NP ? pattern.charAt(ip2) : (char) 0;
                    }
                    while (match.charAt(im) != nextChar2 && (im = im + 1) < NM) {
                    }
                    if (im == NM) {
                        return false;
                    }
                    ip = ip2 + 1;
                    nextChar = ip < NP ? pattern.charAt(ip) : (char) 0;
                    im++;
                } else {
                    while (match.charAt(im) == c && (im = im + 1) < NM) {
                    }
                    ip++;
                    nextChar = ip < NP ? pattern.charAt(ip) : (char) 0;
                }
            } else if (c != '.' && match.charAt(im) != c) {
                return false;
            } else {
                im++;
            }
        }
        if (ip < NP || im < NM) {
            return ip == NP + (-2) && pattern.charAt(ip) == '.' && pattern.charAt(ip + 1) == '*';
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:143:0x0121 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0160  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static synchronized int[] parseAndVerifyAdvancedPattern(java.lang.String r17) {
        /*
            Method dump skipped, instructions count: 520
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.PatternMatcher.parseAndVerifyAdvancedPattern(java.lang.String):int[]");
    }

    private static boolean isParsedModifier(int parsedChar) {
        return parsedChar == -8 || parsedChar == -7 || parsedChar == -6 || parsedChar == -5;
    }

    static boolean matchAdvancedPattern(int[] parsedPattern, String match) {
        int ip;
        int charSetStart;
        int charSetEnd;
        int tokenType;
        int ip2;
        int minRepetition;
        int maxRepetition;
        int tokenType2;
        int ip3 = 0;
        int LP = parsedPattern.length;
        int LM = match.length();
        int charSetStart2 = 0;
        int charSetEnd2 = 0;
        int im = 0;
        while (ip3 < LP) {
            int patternChar = parsedPattern[ip3];
            if (patternChar == -4) {
                ip = ip3 + 1;
                charSetStart = charSetStart2;
                charSetEnd = charSetEnd2;
                tokenType = 1;
            } else if (patternChar == -2 || patternChar == -1) {
                if (patternChar == -1) {
                    tokenType2 = 2;
                } else {
                    tokenType2 = 3;
                }
                int charSetStart3 = ip3 + 1;
                do {
                    ip3++;
                    if (ip3 >= LP) {
                        break;
                    }
                } while (parsedPattern[ip3] != -3);
                int charSetEnd3 = ip3 - 1;
                ip = ip3 + 1;
                charSetStart = charSetStart3;
                charSetEnd = charSetEnd3;
                tokenType = tokenType2;
            } else {
                int charSetStart4 = ip3;
                ip = ip3 + 1;
                charSetStart = charSetStart4;
                charSetEnd = charSetEnd2;
                tokenType = 0;
            }
            if (ip >= LP) {
                ip2 = ip;
                minRepetition = 1;
                maxRepetition = 1;
            } else {
                int patternChar2 = parsedPattern[ip];
                if (patternChar2 == -8) {
                    ip2 = ip + 1;
                    minRepetition = 1;
                    maxRepetition = Integer.MAX_VALUE;
                } else if (patternChar2 == -7) {
                    ip2 = ip + 1;
                    minRepetition = 0;
                    maxRepetition = Integer.MAX_VALUE;
                } else if (patternChar2 == -5) {
                    int ip4 = ip + 1;
                    int minRepetition2 = parsedPattern[ip4];
                    int ip5 = ip4 + 1;
                    int maxRepetition2 = parsedPattern[ip5];
                    ip2 = ip5 + 2;
                    maxRepetition = maxRepetition2;
                    minRepetition = minRepetition2;
                } else {
                    ip2 = ip;
                    minRepetition = 1;
                    maxRepetition = 1;
                }
            }
            if (minRepetition > maxRepetition) {
                return false;
            }
            int i = minRepetition;
            int i2 = maxRepetition;
            int maxRepetition3 = charSetStart;
            int minRepetition3 = charSetEnd;
            int matched = matchChars(match, im, LM, tokenType, i, i2, parsedPattern, maxRepetition3, minRepetition3);
            if (matched == -1) {
                return false;
            }
            im += matched;
            charSetStart2 = charSetStart;
            charSetEnd2 = charSetEnd;
            ip3 = ip2;
        }
        return ip3 >= LP && im >= LM;
    }

    private static int matchChars(String match, int im, int lm, int tokenType, int minRepetition, int maxRepetition, int[] parsedPattern, int tokenStart, int tokenEnd) {
        int matched = 0;
        while (matched < maxRepetition && matchChar(match, im + matched, lm, tokenType, parsedPattern, tokenStart, tokenEnd)) {
            matched++;
        }
        if (matched < minRepetition) {
            return -1;
        }
        return matched;
    }

    private static boolean matchChar(String match, int im, int lm, int tokenType, int[] parsedPattern, int tokenStart, int tokenEnd) {
        if (im >= lm) {
            return false;
        }
        if (tokenType != 0) {
            if (tokenType != 1) {
                if (tokenType == 2) {
                    for (int i = tokenStart; i < tokenEnd; i += 2) {
                        char matchChar = match.charAt(im);
                        if (matchChar >= parsedPattern[i] && matchChar <= parsedPattern[i + 1]) {
                            return true;
                        }
                    }
                    return false;
                } else if (tokenType != 3) {
                    return false;
                } else {
                    for (int i2 = tokenStart; i2 < tokenEnd; i2 += 2) {
                        char matchChar2 = match.charAt(im);
                        if (matchChar2 >= parsedPattern[i2] && matchChar2 <= parsedPattern[i2 + 1]) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return true;
        } else if (match.charAt(im) != parsedPattern[tokenStart]) {
            return false;
        } else {
            return true;
        }
    }
}
