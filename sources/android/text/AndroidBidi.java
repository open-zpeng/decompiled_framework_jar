package android.text;

import android.icu.lang.UCharacter;
import android.icu.text.Bidi;
import android.icu.text.BidiClassifier;
import android.text.Layout;
import com.android.internal.annotations.VisibleForTesting;
@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: classes2.dex */
public class AndroidBidi {
    private static final EmojiBidiOverride sEmojiBidiOverride = new EmojiBidiOverride();

    /* loaded from: classes2.dex */
    public static class EmojiBidiOverride extends BidiClassifier {
        private static final int NO_OVERRIDE = UCharacter.getIntPropertyMaxValue(4096) + 1;

        public synchronized EmojiBidiOverride() {
            super(null);
        }

        @Override // android.icu.text.BidiClassifier
        public synchronized int classify(int c) {
            if (Emoji.isNewEmoji(c)) {
                return 10;
            }
            return NO_OVERRIDE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int bidi(int dir, char[] chs, byte[] chInfo) {
        byte paraLevel;
        if (chs == null || chInfo == null) {
            throw new NullPointerException();
        }
        int length = chs.length;
        if (chInfo.length < length) {
            throw new IndexOutOfBoundsException();
        }
        switch (dir) {
            case -2:
                paraLevel = Byte.MAX_VALUE;
                break;
            case -1:
                paraLevel = 1;
                break;
            case 0:
            default:
                paraLevel = 0;
                break;
            case 1:
                paraLevel = 0;
                break;
            case 2:
                paraLevel = 126;
                break;
        }
        Bidi icuBidi = new Bidi(length, 0);
        icuBidi.setCustomClassifier(sEmojiBidiOverride);
        icuBidi.setPara(chs, paraLevel, (byte[]) null);
        for (int i = 0; i < length; i++) {
            chInfo[i] = icuBidi.getLevelAt(i);
        }
        byte result = icuBidi.getParaLevel();
        return (result & 1) == 0 ? 1 : -1;
    }

    public static synchronized Layout.Directions directions(int dir, byte[] levels, int lstart, char[] chars, int cstart, int len) {
        boolean swap;
        int minLevel;
        int i;
        int e;
        if (len == 0) {
            return Layout.DIRS_ALL_LEFT_TO_RIGHT;
        }
        int baseLevel = dir == 1 ? 0 : 1;
        int curLevel = levels[lstart];
        int curLevel2 = curLevel;
        int runCount = 1;
        int e2 = lstart + len;
        for (int i2 = lstart + 1; i2 < e2; i2++) {
            int level = levels[i2];
            if (level != curLevel) {
                curLevel = level;
                runCount++;
            }
        }
        int visLen = len;
        if ((curLevel & 1) != (baseLevel & 1)) {
            while (true) {
                visLen--;
                if (visLen < 0) {
                    break;
                }
                char ch = chars[cstart + visLen];
                if (ch == '\n') {
                    visLen--;
                    break;
                } else if (ch != ' ' && ch != '\t') {
                    break;
                }
            }
            visLen++;
            if (visLen != len) {
                runCount++;
            }
        }
        if (runCount == 1 && curLevel2 == baseLevel) {
            if ((curLevel2 & 1) != 0) {
                return Layout.DIRS_ALL_RIGHT_TO_LEFT;
            }
            return Layout.DIRS_ALL_LEFT_TO_RIGHT;
        }
        int[] ld = new int[runCount * 2];
        int maxLevel = curLevel2;
        int levelBits = curLevel2 << 26;
        int n = 1;
        int prev = lstart;
        int e3 = lstart + visLen;
        int minLevel2 = curLevel2;
        int curLevel3 = lstart;
        while (true) {
            int e4 = e3;
            if (curLevel3 >= e4) {
                break;
            }
            int e5 = levels[curLevel3];
            if (e5 != curLevel2) {
                curLevel2 = e5;
                if (e5 > maxLevel) {
                    maxLevel = e5;
                } else if (e5 < minLevel2) {
                    minLevel2 = e5;
                }
                int n2 = n + 1;
                ld[n] = (curLevel3 - prev) | levelBits;
                n = n2 + 1;
                ld[n2] = curLevel3 - lstart;
                levelBits = curLevel2 << 26;
                int level2 = curLevel3;
                prev = level2;
            }
            curLevel3++;
            e3 = e4;
        }
        ld[n] = ((lstart + visLen) - prev) | levelBits;
        if (visLen < len) {
            int n3 = n + 1;
            ld[n3] = visLen;
            ld[n3 + 1] = (len - visLen) | (baseLevel << 26);
        }
        if ((minLevel2 & 1) == baseLevel) {
            minLevel2++;
            swap = maxLevel > minLevel2;
        } else {
            swap = true;
            if (runCount <= 1) {
                swap = false;
            }
        }
        if (swap) {
            for (int level3 = maxLevel - 1; level3 >= minLevel2; level3--) {
                int i3 = 0;
                while (true) {
                    int i4 = i3;
                    if (i4 < ld.length) {
                        if (levels[ld[i4]] < level3) {
                            minLevel = minLevel2;
                            i = i4;
                        } else {
                            int e6 = i4 + 2;
                            while (true) {
                                e = e6;
                                minLevel = minLevel2;
                                int minLevel3 = ld.length;
                                if (e >= minLevel3 || levels[ld[e]] < level3) {
                                    break;
                                }
                                e6 = e + 2;
                                minLevel2 = minLevel;
                            }
                            int low = i4;
                            int hi = e - 2;
                            while (true) {
                                int hi2 = hi;
                                if (low >= hi2) {
                                    break;
                                }
                                int x = ld[low];
                                ld[low] = ld[hi2];
                                ld[hi2] = x;
                                int x2 = ld[low + 1];
                                ld[low + 1] = ld[hi2 + 1];
                                ld[hi2 + 1] = x2;
                                low += 2;
                                hi = hi2 - 2;
                            }
                            i = e + 2;
                        }
                        i3 = i + 2;
                        minLevel2 = minLevel;
                    }
                }
            }
        }
        return new Layout.Directions(ld);
    }
}
