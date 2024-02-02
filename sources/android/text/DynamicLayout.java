package android.text;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;
import android.text.style.UpdateLayout;
import android.text.style.WrapTogetherSpan;
import android.util.ArraySet;
import android.util.Pools;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.ref.WeakReference;
/* loaded from: classes2.dex */
public class DynamicLayout extends Layout {
    private static final int BLOCK_MINIMUM_CHARACTER_LENGTH = 400;
    private static final int COLUMNS_ELLIPSIZE = 7;
    private static final int COLUMNS_NORMAL = 5;
    private static final int DESCENT = 2;
    private static final int DIR = 0;
    private static final int DIR_SHIFT = 30;
    private static final int ELLIPSIS_COUNT = 6;
    private static final int ELLIPSIS_START = 5;
    private static final int ELLIPSIS_UNDEFINED = Integer.MIN_VALUE;
    private static final int EXTRA = 3;
    private static final int HYPHEN = 4;
    private static final int HYPHEN_MASK = 255;
    public static final int INVALID_BLOCK_INDEX = -1;
    private static final int MAY_PROTRUDE_FROM_TOP_OR_BOTTOM = 4;
    private static final int MAY_PROTRUDE_FROM_TOP_OR_BOTTOM_MASK = 256;
    private static final int PRIORITY = 128;
    private static final int START = 0;
    private static final int START_MASK = 536870911;
    private static final int TAB = 0;
    private static final int TAB_MASK = 536870912;
    private static final int TOP = 1;
    private CharSequence mBase;
    private int[] mBlockEndLines;
    private int[] mBlockIndices;
    private ArraySet<Integer> mBlocksAlwaysNeedToBeRedrawn;
    private int mBottomPadding;
    private int mBreakStrategy;
    private CharSequence mDisplay;
    private boolean mEllipsize;
    private TextUtils.TruncateAt mEllipsizeAt;
    private int mEllipsizedWidth;
    private boolean mFallbackLineSpacing;
    private int mHyphenationFrequency;
    private boolean mIncludePad;
    private int mIndexFirstChangedBlock;
    private PackedIntVector mInts;
    private int mJustificationMode;
    private int mNumberOfBlocks;
    private PackedObjectVector<Layout.Directions> mObjects;
    private Rect mTempRect;
    private int mTopPadding;
    private ChangeWatcher mWatcher;
    public protected static StaticLayout sStaticLayout = null;
    private static StaticLayout.Builder sBuilder = null;
    private static final Object[] sLock = new Object[0];

    /* loaded from: classes2.dex */
    public static final class Builder {
        private static final Pools.SynchronizedPool<Builder> sPool = new Pools.SynchronizedPool<>(3);
        private Layout.Alignment mAlignment;
        private CharSequence mBase;
        private int mBreakStrategy;
        private CharSequence mDisplay;
        private TextUtils.TruncateAt mEllipsize;
        private int mEllipsizedWidth;
        private boolean mFallbackLineSpacing;
        private final Paint.FontMetricsInt mFontMetricsInt = new Paint.FontMetricsInt();
        private int mHyphenationFrequency;
        private boolean mIncludePad;
        private int mJustificationMode;
        private TextPaint mPaint;
        private float mSpacingAdd;
        private float mSpacingMult;
        private TextDirectionHeuristic mTextDir;
        private int mWidth;

        private synchronized Builder() {
        }

        public static Builder obtain(CharSequence base, TextPaint paint, int width) {
            Builder b = sPool.acquire();
            if (b == null) {
                b = new Builder();
            }
            b.mBase = base;
            b.mDisplay = base;
            b.mPaint = paint;
            b.mWidth = width;
            b.mAlignment = Layout.Alignment.ALIGN_NORMAL;
            b.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            b.mSpacingMult = 1.0f;
            b.mSpacingAdd = 0.0f;
            b.mIncludePad = true;
            b.mFallbackLineSpacing = false;
            b.mEllipsizedWidth = width;
            b.mEllipsize = null;
            b.mBreakStrategy = 0;
            b.mHyphenationFrequency = 0;
            b.mJustificationMode = 0;
            return b;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized void recycle(Builder b) {
            b.mBase = null;
            b.mDisplay = null;
            b.mPaint = null;
            sPool.release(b);
        }

        public Builder setDisplayText(CharSequence display) {
            this.mDisplay = display;
            return this;
        }

        public Builder setAlignment(Layout.Alignment alignment) {
            this.mAlignment = alignment;
            return this;
        }

        public Builder setTextDirection(TextDirectionHeuristic textDir) {
            this.mTextDir = textDir;
            return this;
        }

        public Builder setLineSpacing(float spacingAdd, float spacingMult) {
            this.mSpacingAdd = spacingAdd;
            this.mSpacingMult = spacingMult;
            return this;
        }

        public Builder setIncludePad(boolean includePad) {
            this.mIncludePad = includePad;
            return this;
        }

        public Builder setUseLineSpacingFromFallbacks(boolean useLineSpacingFromFallbacks) {
            this.mFallbackLineSpacing = useLineSpacingFromFallbacks;
            return this;
        }

        public Builder setEllipsizedWidth(int ellipsizedWidth) {
            this.mEllipsizedWidth = ellipsizedWidth;
            return this;
        }

        public Builder setEllipsize(TextUtils.TruncateAt ellipsize) {
            this.mEllipsize = ellipsize;
            return this;
        }

        public Builder setBreakStrategy(int breakStrategy) {
            this.mBreakStrategy = breakStrategy;
            return this;
        }

        public Builder setHyphenationFrequency(int hyphenationFrequency) {
            this.mHyphenationFrequency = hyphenationFrequency;
            return this;
        }

        public Builder setJustificationMode(int justificationMode) {
            this.mJustificationMode = justificationMode;
            return this;
        }

        public DynamicLayout build() {
            DynamicLayout result = new DynamicLayout(this);
            recycle(this);
            return result;
        }
    }

    @Deprecated
    public DynamicLayout(CharSequence base, TextPaint paint, int width, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(base, base, paint, width, align, spacingmult, spacingadd, includepad);
    }

    @Deprecated
    public DynamicLayout(CharSequence base, CharSequence display, TextPaint paint, int width, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(base, display, paint, width, align, spacingmult, spacingadd, includepad, null, 0);
    }

    @Deprecated
    public DynamicLayout(CharSequence base, CharSequence display, TextPaint paint, int width, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        this(base, display, paint, width, align, TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingmult, spacingadd, includepad, 0, 0, 0, ellipsize, ellipsizedWidth);
    }

    @Deprecated
    private protected DynamicLayout(CharSequence base, CharSequence display, TextPaint paint, int width, Layout.Alignment align, TextDirectionHeuristic textDir, float spacingmult, float spacingadd, boolean includepad, int breakStrategy, int hyphenationFrequency, int justificationMode, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        super(createEllipsizer(ellipsize, display), paint, width, align, textDir, spacingmult, spacingadd);
        this.mTempRect = new Rect();
        Builder b = Builder.obtain(base, paint, width).setAlignment(align).setTextDirection(textDir).setLineSpacing(spacingadd, spacingmult).setEllipsizedWidth(ellipsizedWidth).setEllipsize(ellipsize);
        this.mDisplay = display;
        this.mIncludePad = includepad;
        this.mBreakStrategy = breakStrategy;
        this.mJustificationMode = justificationMode;
        this.mHyphenationFrequency = hyphenationFrequency;
        generate(b);
        Builder.recycle(b);
    }

    private synchronized DynamicLayout(Builder b) {
        super(createEllipsizer(b.mEllipsize, b.mDisplay), b.mPaint, b.mWidth, b.mAlignment, b.mTextDir, b.mSpacingMult, b.mSpacingAdd);
        this.mTempRect = new Rect();
        this.mDisplay = b.mDisplay;
        this.mIncludePad = b.mIncludePad;
        this.mBreakStrategy = b.mBreakStrategy;
        this.mJustificationMode = b.mJustificationMode;
        this.mHyphenationFrequency = b.mHyphenationFrequency;
        generate(b);
    }

    private static synchronized CharSequence createEllipsizer(TextUtils.TruncateAt ellipsize, CharSequence display) {
        if (ellipsize == null) {
            return display;
        }
        if (display instanceof Spanned) {
            return new Layout.SpannedEllipsizer(display);
        }
        return new Layout.Ellipsizer(display);
    }

    private synchronized void generate(Builder b) {
        int[] start;
        this.mBase = b.mBase;
        this.mFallbackLineSpacing = b.mFallbackLineSpacing;
        if (b.mEllipsize != null) {
            this.mInts = new PackedIntVector(7);
            this.mEllipsizedWidth = b.mEllipsizedWidth;
            this.mEllipsizeAt = b.mEllipsize;
            Layout.Ellipsizer e = (Layout.Ellipsizer) getText();
            e.mLayout = this;
            e.mWidth = b.mEllipsizedWidth;
            e.mMethod = b.mEllipsize;
            this.mEllipsize = true;
        } else {
            this.mInts = new PackedIntVector(5);
            this.mEllipsizedWidth = b.mWidth;
            this.mEllipsizeAt = null;
        }
        this.mObjects = new PackedObjectVector<>(1);
        if (b.mEllipsize != null) {
            start = new int[7];
            start[5] = Integer.MIN_VALUE;
        } else {
            start = new int[5];
        }
        Layout.Directions[] dirs = {DIRS_ALL_LEFT_TO_RIGHT};
        Paint.FontMetricsInt fm = b.mFontMetricsInt;
        b.mPaint.getFontMetricsInt(fm);
        int asc = fm.ascent;
        int desc = fm.descent;
        start[0] = 1073741824;
        start[1] = 0;
        start[2] = desc;
        this.mInts.insertAt(0, start);
        start[1] = desc - asc;
        this.mInts.insertAt(1, start);
        this.mObjects.insertAt(0, dirs);
        int baseLength = this.mBase.length();
        reflow(this.mBase, 0, 0, baseLength);
        if (this.mBase instanceof Spannable) {
            if (this.mWatcher == null) {
                this.mWatcher = new ChangeWatcher(this);
            }
            Spannable sp = (Spannable) this.mBase;
            ChangeWatcher[] spans = (ChangeWatcher[]) sp.getSpans(0, baseLength, ChangeWatcher.class);
            for (ChangeWatcher changeWatcher : spans) {
                sp.removeSpan(changeWatcher);
            }
            sp.setSpan(this.mWatcher, 0, baseLength, 8388626);
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public synchronized void reflow(CharSequence s, int where, int before, int after) {
        int find;
        int look;
        int before2;
        int where2;
        StaticLayout reflowed;
        StaticLayout.Builder b;
        int[] ints;
        if (s != this.mBase) {
            return;
        }
        CharSequence text = this.mDisplay;
        int len = text.length();
        int find2 = TextUtils.lastIndexOf(text, '\n', where - 1);
        if (find2 < 0) {
            find = 0;
        } else {
            find = find2 + 1;
        }
        int diff = where - find;
        int before3 = before + diff;
        int after2 = after + diff;
        int where3 = where - diff;
        int look2 = TextUtils.indexOf(text, '\n', where3 + after2);
        if (look2 < 0) {
            look = len;
        } else {
            look = look2 + 1;
        }
        int change = look - (where3 + after2);
        int where4 = before3 + change;
        int after3 = after2 + change;
        if (text instanceof Spanned) {
            Spanned sp = (Spanned) text;
            while (true) {
                boolean again = false;
                Object[] force = sp.getSpans(where3, where3 + after3, WrapTogetherSpan.class);
                before2 = where4;
                where2 = where3;
                for (int where5 = 0; where5 < force.length; where5++) {
                    int st = sp.getSpanStart(force[where5]);
                    int en = sp.getSpanEnd(force[where5]);
                    if (st < where2) {
                        again = true;
                        int diff2 = where2 - st;
                        before2 += diff2;
                        after3 += diff2;
                        where2 -= diff2;
                    }
                    if (en > where2 + after3) {
                        int diff3 = en - (where2 + after3);
                        before2 += diff3;
                        after3 += diff3;
                        again = true;
                    }
                }
                if (!again) {
                    break;
                }
                where3 = where2;
                where4 = before2;
            }
        } else {
            before2 = where4;
            where2 = where3;
        }
        int startline = getLineForOffset(where2);
        int startv = getLineTop(startline);
        int endline = getLineForOffset(where2 + before2);
        if (where2 + after3 == len) {
            endline = getLineCount();
        }
        int endline2 = endline;
        int endv = getLineTop(endline2);
        boolean islast = endline2 == getLineCount();
        synchronized (sLock) {
            try {
                StaticLayout reflowed2 = sStaticLayout;
                StaticLayout.Builder b2 = sBuilder;
                try {
                    sStaticLayout = null;
                    sBuilder = null;
                    if (reflowed2 == null) {
                        StaticLayout reflowed3 = new StaticLayout((CharSequence) null);
                        StaticLayout.Builder b3 = StaticLayout.Builder.obtain(text, where2, where2 + after3, getPaint(), getWidth());
                        b = b3;
                        reflowed = reflowed3;
                    } else {
                        reflowed = reflowed2;
                        b = b2;
                    }
                    b.setText(text, where2, where2 + after3).setPaint(getPaint()).setWidth(getWidth()).setTextDirection(getTextDirectionHeuristic()).setLineSpacing(getSpacingAdd(), getSpacingMultiplier()).setUseLineSpacingFromFallbacks(this.mFallbackLineSpacing).setEllipsizedWidth(this.mEllipsizedWidth).setEllipsize(this.mEllipsizeAt).setBreakStrategy(this.mBreakStrategy).setHyphenationFrequency(this.mHyphenationFrequency).setJustificationMode(this.mJustificationMode).setAddLastLineLineSpacing(!islast);
                    reflowed.generate(b, false, true);
                    int n = reflowed.getLineCount();
                    if (where2 + after3 != len && reflowed.getLineStart(n - 1) == where2 + after3) {
                        n--;
                    }
                    int n2 = n;
                    this.mInts.deleteAt(startline, endline2 - startline);
                    this.mObjects.deleteAt(startline, endline2 - startline);
                    int ht = reflowed.getLineTop(n2);
                    int toppad = 0;
                    int botpad = 0;
                    if (this.mIncludePad && startline == 0) {
                        toppad = reflowed.getTopPadding();
                        this.mTopPadding = toppad;
                        ht -= toppad;
                    }
                    if (this.mIncludePad && islast) {
                        int botpad2 = reflowed.getBottomPadding();
                        this.mBottomPadding = botpad2;
                        ht += botpad2;
                        botpad = botpad2;
                    }
                    int ht2 = ht;
                    this.mInts.adjustValuesBelow(startline, 0, after3 - before2);
                    this.mInts.adjustValuesBelow(startline, 1, (startv - endv) + ht2);
                    if (this.mEllipsize) {
                        ints = new int[7];
                        ints[5] = Integer.MIN_VALUE;
                    } else {
                        ints = new int[5];
                    }
                    int[] ints2 = ints;
                    Layout.Directions[] objects = new Layout.Directions[1];
                    int i = 0;
                    while (i < n2) {
                        int ht3 = ht2;
                        int ht4 = reflowed.getLineStart(i);
                        ints2[0] = ht4;
                        ints2[0] = ints2[0] | (reflowed.getParagraphDirection(i) << 30);
                        ints2[0] = ints2[0] | (reflowed.getLineContainsTab(i) ? 536870912 : 0);
                        int top = reflowed.getLineTop(i) + startv;
                        if (i > 0) {
                            top -= toppad;
                        }
                        ints2[1] = top;
                        int desc = reflowed.getLineDescent(i);
                        int startv2 = startv;
                        if (i == n2 - 1) {
                            desc += botpad;
                        }
                        ints2[2] = desc;
                        ints2[3] = reflowed.getLineExtra(i);
                        objects[0] = reflowed.getLineDirections(i);
                        int end = i == n2 + (-1) ? where2 + after3 : reflowed.getLineStart(i + 1);
                        int toppad2 = toppad;
                        ints2[4] = reflowed.getHyphen(i) & 255;
                        ints2[4] = ints2[4] | (contentMayProtrudeFromLineTopOrBottom(text, ht4, end) ? 256 : 0);
                        if (this.mEllipsize) {
                            ints2[5] = reflowed.getEllipsisStart(i);
                            ints2[6] = reflowed.getEllipsisCount(i);
                        }
                        this.mInts.insertAt(startline + i, ints2);
                        this.mObjects.insertAt(startline + i, objects);
                        i++;
                        ht2 = ht3;
                        startv = startv2;
                        toppad = toppad2;
                        text = text;
                    }
                    updateBlocks(startline, endline2 - 1, n2);
                    b.finish();
                    synchronized (sLock) {
                        sStaticLayout = reflowed;
                        sBuilder = b;
                    }
                } catch (Throwable th) {
                    th = th;
                    while (true) {
                        try {
                            break;
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    private synchronized boolean contentMayProtrudeFromLineTopOrBottom(CharSequence text, int start, int end) {
        if (text instanceof Spanned) {
            Spanned spanned = (Spanned) text;
            if (((ReplacementSpan[]) spanned.getSpans(start, end, ReplacementSpan.class)).length > 0) {
                return true;
            }
        }
        Paint paint = getPaint();
        if (text instanceof PrecomputedText) {
            PrecomputedText precomputed = (PrecomputedText) text;
            precomputed.getBounds(start, end, this.mTempRect);
        } else {
            paint.getTextBounds(text, start, end, this.mTempRect);
        }
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        return this.mTempRect.top < fm.top || this.mTempRect.bottom > fm.bottom;
    }

    private synchronized void createBlocks() {
        int offset = 400;
        this.mNumberOfBlocks = 0;
        CharSequence text = this.mDisplay;
        while (true) {
            int offset2 = TextUtils.indexOf(text, '\n', offset);
            if (offset2 < 0) {
                break;
            }
            addBlockAtOffset(offset2);
            offset = offset2 + 400;
        }
        addBlockAtOffset(text.length());
        this.mBlockIndices = new int[this.mBlockEndLines.length];
        for (int i = 0; i < this.mBlockEndLines.length; i++) {
            this.mBlockIndices[i] = -1;
        }
    }

    public synchronized ArraySet<Integer> getBlocksAlwaysNeedToBeRedrawn() {
        return this.mBlocksAlwaysNeedToBeRedrawn;
    }

    private synchronized void updateAlwaysNeedsToBeRedrawn(int blockIndex) {
        int startLine = blockIndex == 0 ? 0 : this.mBlockEndLines[blockIndex - 1] + 1;
        int endLine = this.mBlockEndLines[blockIndex];
        for (int i = startLine; i <= endLine; i++) {
            if (getContentMayProtrudeFromTopOrBottom(i)) {
                if (this.mBlocksAlwaysNeedToBeRedrawn == null) {
                    this.mBlocksAlwaysNeedToBeRedrawn = new ArraySet<>();
                }
                this.mBlocksAlwaysNeedToBeRedrawn.add(Integer.valueOf(blockIndex));
                return;
            }
        }
        if (this.mBlocksAlwaysNeedToBeRedrawn != null) {
            this.mBlocksAlwaysNeedToBeRedrawn.remove(Integer.valueOf(blockIndex));
        }
    }

    private synchronized void addBlockAtOffset(int offset) {
        int line = getLineForOffset(offset);
        if (this.mBlockEndLines == null) {
            this.mBlockEndLines = ArrayUtils.newUnpaddedIntArray(1);
            this.mBlockEndLines[this.mNumberOfBlocks] = line;
            updateAlwaysNeedsToBeRedrawn(this.mNumberOfBlocks);
            this.mNumberOfBlocks++;
            return;
        }
        int previousBlockEndLine = this.mBlockEndLines[this.mNumberOfBlocks - 1];
        if (line > previousBlockEndLine) {
            this.mBlockEndLines = GrowingArrayUtils.append(this.mBlockEndLines, this.mNumberOfBlocks, line);
            updateAlwaysNeedsToBeRedrawn(this.mNumberOfBlocks);
            this.mNumberOfBlocks++;
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public synchronized void updateBlocks(int startLine, int endLine, int newLineCount) {
        int i;
        boolean createBlockBefore;
        boolean createBlock;
        boolean createBlockAfter;
        int lastBlockEndLine;
        boolean createBlock2;
        boolean createBlockAfter2;
        int newFirstChangedBlock;
        if (this.mBlockEndLines == null) {
            createBlocks();
            return;
        }
        int firstBlock = -1;
        int lastBlock = -1;
        int i2 = 0;
        while (true) {
            if (i2 < this.mNumberOfBlocks) {
                if (this.mBlockEndLines[i2] < startLine) {
                    i2++;
                } else {
                    firstBlock = i2;
                    break;
                }
            } else {
                break;
            }
        }
        int i3 = firstBlock;
        while (true) {
            if (i3 < this.mNumberOfBlocks) {
                if (this.mBlockEndLines[i3] < endLine) {
                    i3++;
                } else {
                    lastBlock = i3;
                    break;
                }
            } else {
                break;
            }
        }
        int lastBlockEndLine2 = this.mBlockEndLines[lastBlock];
        if (firstBlock != 0) {
            i = this.mBlockEndLines[firstBlock - 1] + 1;
        } else {
            i = 0;
        }
        if (startLine <= i) {
            createBlockBefore = false;
        } else {
            createBlockBefore = true;
        }
        if (newLineCount <= 0) {
            createBlock = false;
        } else {
            createBlock = true;
        }
        if (endLine >= this.mBlockEndLines[lastBlock]) {
            createBlockAfter = false;
        } else {
            createBlockAfter = true;
        }
        int numAddedBlocks = createBlockBefore ? 0 + 1 : 0;
        if (createBlock) {
            numAddedBlocks++;
        }
        if (createBlockAfter) {
            numAddedBlocks++;
        }
        int numRemovedBlocks = (lastBlock - firstBlock) + 1;
        int newNumberOfBlocks = (this.mNumberOfBlocks + numAddedBlocks) - numRemovedBlocks;
        if (newNumberOfBlocks == 0) {
            this.mBlockEndLines[0] = 0;
            this.mBlockIndices[0] = -1;
            this.mNumberOfBlocks = 1;
            return;
        }
        if (newNumberOfBlocks > this.mBlockEndLines.length) {
            int[] blockEndLines = ArrayUtils.newUnpaddedIntArray(Math.max(this.mBlockEndLines.length * 2, newNumberOfBlocks));
            int[] blockIndices = new int[blockEndLines.length];
            lastBlockEndLine = lastBlockEndLine2;
            System.arraycopy(this.mBlockEndLines, 0, blockEndLines, 0, firstBlock);
            System.arraycopy(this.mBlockIndices, 0, blockIndices, 0, firstBlock);
            createBlockAfter2 = createBlockAfter;
            createBlock2 = createBlock;
            System.arraycopy(this.mBlockEndLines, lastBlock + 1, blockEndLines, firstBlock + numAddedBlocks, (this.mNumberOfBlocks - lastBlock) - 1);
            System.arraycopy(this.mBlockIndices, lastBlock + 1, blockIndices, firstBlock + numAddedBlocks, (this.mNumberOfBlocks - lastBlock) - 1);
            this.mBlockEndLines = blockEndLines;
            this.mBlockIndices = blockIndices;
        } else {
            lastBlockEndLine = lastBlockEndLine2;
            createBlock2 = createBlock;
            createBlockAfter2 = createBlockAfter;
            if (numAddedBlocks + numRemovedBlocks != 0) {
                System.arraycopy(this.mBlockEndLines, lastBlock + 1, this.mBlockEndLines, firstBlock + numAddedBlocks, (this.mNumberOfBlocks - lastBlock) - 1);
                System.arraycopy(this.mBlockIndices, lastBlock + 1, this.mBlockIndices, firstBlock + numAddedBlocks, (this.mNumberOfBlocks - lastBlock) - 1);
            }
        }
        if (numAddedBlocks + numRemovedBlocks != 0 && this.mBlocksAlwaysNeedToBeRedrawn != null) {
            ArraySet<Integer> set = new ArraySet<>();
            int changedBlockCount = numAddedBlocks - numRemovedBlocks;
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 >= this.mBlocksAlwaysNeedToBeRedrawn.size()) {
                    break;
                }
                Integer block = this.mBlocksAlwaysNeedToBeRedrawn.valueAt(i5);
                if (block.intValue() < firstBlock) {
                    set.add(block);
                }
                if (block.intValue() > lastBlock) {
                    set.add(Integer.valueOf(block.intValue() + changedBlockCount));
                }
                i4 = i5 + 1;
            }
            this.mBlocksAlwaysNeedToBeRedrawn = set;
        }
        this.mNumberOfBlocks = newNumberOfBlocks;
        int deltaLines = newLineCount - ((endLine - startLine) + 1);
        if (deltaLines != 0) {
            newFirstChangedBlock = firstBlock + numAddedBlocks;
            for (int i6 = newFirstChangedBlock; i6 < this.mNumberOfBlocks; i6++) {
                int[] iArr = this.mBlockEndLines;
                iArr[i6] = iArr[i6] + deltaLines;
            }
        } else {
            newFirstChangedBlock = this.mNumberOfBlocks;
        }
        this.mIndexFirstChangedBlock = Math.min(this.mIndexFirstChangedBlock, newFirstChangedBlock);
        int blockIndex = firstBlock;
        if (createBlockBefore) {
            this.mBlockEndLines[blockIndex] = startLine - 1;
            updateAlwaysNeedsToBeRedrawn(blockIndex);
            this.mBlockIndices[blockIndex] = -1;
            blockIndex++;
        }
        if (createBlock2) {
            this.mBlockEndLines[blockIndex] = (startLine + newLineCount) - 1;
            updateAlwaysNeedsToBeRedrawn(blockIndex);
            this.mBlockIndices[blockIndex] = -1;
            blockIndex++;
        }
        if (createBlockAfter2) {
            this.mBlockEndLines[blockIndex] = lastBlockEndLine + deltaLines;
            updateAlwaysNeedsToBeRedrawn(blockIndex);
            this.mBlockIndices[blockIndex] = -1;
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public synchronized void setBlocksDataForTest(int[] blockEndLines, int[] blockIndices, int numberOfBlocks, int totalLines) {
        this.mBlockEndLines = new int[blockEndLines.length];
        this.mBlockIndices = new int[blockIndices.length];
        System.arraycopy(blockEndLines, 0, this.mBlockEndLines, 0, blockEndLines.length);
        System.arraycopy(blockIndices, 0, this.mBlockIndices, 0, blockIndices.length);
        this.mNumberOfBlocks = numberOfBlocks;
        while (this.mInts.size() < totalLines) {
            this.mInts.insertAt(this.mInts.size(), new int[5]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getBlockEndLines() {
        return this.mBlockEndLines;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getBlockIndices() {
        return this.mBlockIndices;
    }

    public synchronized int getBlockIndex(int index) {
        return this.mBlockIndices[index];
    }

    public synchronized void setBlockIndex(int index, int blockIndex) {
        this.mBlockIndices[index] = blockIndex;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNumberOfBlocks() {
        return this.mNumberOfBlocks;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getIndexFirstChangedBlock() {
        return this.mIndexFirstChangedBlock;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIndexFirstChangedBlock(int i) {
        this.mIndexFirstChangedBlock = i;
    }

    @Override // android.text.Layout
    public int getLineCount() {
        return this.mInts.size() - 1;
    }

    @Override // android.text.Layout
    public int getLineTop(int line) {
        return this.mInts.getValue(line, 1);
    }

    @Override // android.text.Layout
    public int getLineDescent(int line) {
        return this.mInts.getValue(line, 2);
    }

    @Override // android.text.Layout
    public synchronized int getLineExtra(int line) {
        return this.mInts.getValue(line, 3);
    }

    @Override // android.text.Layout
    public int getLineStart(int line) {
        return this.mInts.getValue(line, 0) & 536870911;
    }

    @Override // android.text.Layout
    public boolean getLineContainsTab(int line) {
        return (this.mInts.getValue(line, 0) & 536870912) != 0;
    }

    @Override // android.text.Layout
    public int getParagraphDirection(int line) {
        return this.mInts.getValue(line, 0) >> 30;
    }

    @Override // android.text.Layout
    public final Layout.Directions getLineDirections(int line) {
        return this.mObjects.getValue(line, 0);
    }

    @Override // android.text.Layout
    public int getTopPadding() {
        return this.mTopPadding;
    }

    @Override // android.text.Layout
    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    @Override // android.text.Layout
    public synchronized int getHyphen(int line) {
        return this.mInts.getValue(line, 4) & 255;
    }

    private synchronized boolean getContentMayProtrudeFromTopOrBottom(int line) {
        return (this.mInts.getValue(line, 4) & 256) != 0;
    }

    @Override // android.text.Layout
    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class ChangeWatcher implements TextWatcher, SpanWatcher {
        private WeakReference<DynamicLayout> mLayout;

        public synchronized ChangeWatcher(DynamicLayout layout) {
            this.mLayout = new WeakReference<>(layout);
        }

        private synchronized void reflow(CharSequence s, int where, int before, int after) {
            DynamicLayout ml = this.mLayout.get();
            if (ml != null) {
                ml.reflow(s, where, before, after);
            } else if (s instanceof Spannable) {
                ((Spannable) s).removeSpan(this);
            }
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int where, int before, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int where, int before, int after) {
            reflow(s, where, before, after);
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
        }

        @Override // android.text.SpanWatcher
        public void onSpanAdded(Spannable s, Object o, int start, int end) {
            if (o instanceof UpdateLayout) {
                reflow(s, start, end - start, end - start);
            }
        }

        @Override // android.text.SpanWatcher
        public void onSpanRemoved(Spannable s, Object o, int start, int end) {
            if (o instanceof UpdateLayout) {
                reflow(s, start, end - start, end - start);
            }
        }

        @Override // android.text.SpanWatcher
        public void onSpanChanged(Spannable s, Object o, int start, int end, int nstart, int nend) {
            if (o instanceof UpdateLayout) {
                if (start > end) {
                    start = 0;
                }
                reflow(s, start, end - start, end - start);
                reflow(s, nstart, nend - nstart, nend - nstart);
            }
        }
    }

    @Override // android.text.Layout
    public int getEllipsisStart(int line) {
        if (this.mEllipsizeAt == null) {
            return 0;
        }
        return this.mInts.getValue(line, 5);
    }

    @Override // android.text.Layout
    public int getEllipsisCount(int line) {
        if (this.mEllipsizeAt == null) {
            return 0;
        }
        return this.mInts.getValue(line, 6);
    }
}
