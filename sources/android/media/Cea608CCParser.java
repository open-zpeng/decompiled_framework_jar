package android.media;

import android.bluetooth.BluetoothHidDevice;
import android.net.wifi.WifiScanner;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.accessibility.CaptioningManager;
import com.android.internal.telephony.PhoneConstants;
import java.util.ArrayList;
import java.util.Arrays;
/* compiled from: ClosedCaptionRenderer.java */
/* loaded from: classes.dex */
class Cea608CCParser {
    private static final int AOF = 34;
    private static final int AON = 35;
    private static final int BS = 33;
    private static final int CR = 45;
    private static final int DER = 36;
    private static final int EDM = 44;
    private static final int ENM = 46;
    private static final int EOC = 47;
    private static final int FON = 40;
    private static final int INVALID = -1;
    public static final int MAX_COLS = 32;
    public static final int MAX_ROWS = 15;
    private static final int MODE_PAINT_ON = 1;
    private static final int MODE_POP_ON = 3;
    private static final int MODE_ROLL_UP = 2;
    private static final int MODE_TEXT = 4;
    private static final int MODE_UNKNOWN = 0;
    private static final int RCL = 32;
    private static final int RDC = 41;
    private static final int RTD = 43;
    private static final int RU2 = 37;
    private static final int RU3 = 38;
    private static final int RU4 = 39;
    private static final int TR = 42;
    private static final char TS = 160;
    private final DisplayListener mListener;
    private static final String TAG = "Cea608CCParser";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private int mMode = 1;
    private int mRollUpSize = 4;
    private int mPrevCtrlCode = -1;
    private CCMemory mDisplay = new CCMemory();
    private CCMemory mNonDisplay = new CCMemory();
    private CCMemory mTextMem = new CCMemory();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClosedCaptionRenderer.java */
    /* loaded from: classes.dex */
    public interface DisplayListener {
        synchronized CaptioningManager.CaptionStyle getCaptionStyle();

        synchronized void onDisplayChanged(SpannableStringBuilder[] spannableStringBuilderArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Cea608CCParser(DisplayListener listener) {
        this.mListener = listener;
    }

    public synchronized void parse(byte[] data) {
        CCData[] ccData = CCData.fromByteArray(data);
        for (int i = 0; i < ccData.length; i++) {
            if (DEBUG) {
                Log.d(TAG, ccData[i].toString());
            }
            if (!handleCtrlCode(ccData[i]) && !handleTabOffsets(ccData[i]) && !handlePACCode(ccData[i]) && !handleMidRowCode(ccData[i])) {
                handleDisplayableChars(ccData[i]);
            }
        }
    }

    private synchronized CCMemory getMemory() {
        switch (this.mMode) {
            case 1:
            case 2:
                return this.mDisplay;
            case 3:
                return this.mNonDisplay;
            case 4:
                return this.mTextMem;
            default:
                Log.w(TAG, "unrecoginized mode: " + this.mMode);
                return this.mDisplay;
        }
    }

    private synchronized boolean handleDisplayableChars(CCData ccData) {
        if (!ccData.isDisplayableChar()) {
            return false;
        }
        if (ccData.isExtendedChar()) {
            getMemory().bs();
        }
        getMemory().writeText(ccData.getDisplayText());
        if (this.mMode == 1 || this.mMode == 2) {
            updateDisplay();
        }
        return true;
    }

    private synchronized boolean handleMidRowCode(CCData ccData) {
        StyleCode m = ccData.getMidRow();
        if (m != null) {
            getMemory().writeMidRowCode(m);
            return true;
        }
        return false;
    }

    private synchronized boolean handlePACCode(CCData ccData) {
        PAC pac = ccData.getPAC();
        if (pac != null) {
            if (this.mMode == 2) {
                getMemory().moveBaselineTo(pac.getRow(), this.mRollUpSize);
            }
            getMemory().writePAC(pac);
            return true;
        }
        return false;
    }

    private synchronized boolean handleTabOffsets(CCData ccData) {
        int tabs = ccData.getTabOffset();
        if (tabs > 0) {
            getMemory().tab(tabs);
            return true;
        }
        return false;
    }

    private synchronized boolean handleCtrlCode(CCData ccData) {
        int ctrlCode = ccData.getCtrlCode();
        if (this.mPrevCtrlCode != -1 && this.mPrevCtrlCode == ctrlCode) {
            this.mPrevCtrlCode = -1;
            return true;
        }
        switch (ctrlCode) {
            case 32:
                this.mMode = 3;
                break;
            case 33:
                getMemory().bs();
                break;
            case 34:
            case 35:
            default:
                this.mPrevCtrlCode = -1;
                return false;
            case 36:
                getMemory().der();
                break;
            case 37:
            case 38:
            case 39:
                this.mRollUpSize = ctrlCode - 35;
                if (this.mMode != 2) {
                    this.mDisplay.erase();
                    this.mNonDisplay.erase();
                }
                this.mMode = 2;
                break;
            case 40:
                Log.i(TAG, "Flash On");
                break;
            case 41:
                this.mMode = 1;
                break;
            case 42:
                this.mMode = 4;
                this.mTextMem.erase();
                break;
            case 43:
                this.mMode = 4;
                break;
            case 44:
                this.mDisplay.erase();
                updateDisplay();
                break;
            case 45:
                if (this.mMode == 2) {
                    getMemory().rollUp(this.mRollUpSize);
                } else {
                    getMemory().cr();
                }
                if (this.mMode == 2) {
                    updateDisplay();
                    break;
                }
                break;
            case 46:
                this.mNonDisplay.erase();
                break;
            case 47:
                swapMemory();
                this.mMode = 3;
                updateDisplay();
                break;
        }
        this.mPrevCtrlCode = ctrlCode;
        return true;
    }

    private synchronized void updateDisplay() {
        if (this.mListener != null) {
            CaptioningManager.CaptionStyle captionStyle = this.mListener.getCaptionStyle();
            this.mListener.onDisplayChanged(this.mDisplay.getStyledText(captionStyle));
        }
    }

    private synchronized void swapMemory() {
        CCMemory temp = this.mDisplay;
        this.mDisplay = this.mNonDisplay;
        this.mNonDisplay = temp;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ClosedCaptionRenderer.java */
    /* loaded from: classes.dex */
    public static class StyleCode {
        static final int COLOR_BLUE = 2;
        static final int COLOR_CYAN = 3;
        static final int COLOR_GREEN = 1;
        static final int COLOR_INVALID = 7;
        static final int COLOR_MAGENTA = 6;
        static final int COLOR_RED = 4;
        static final int COLOR_WHITE = 0;
        static final int COLOR_YELLOW = 5;
        static final int STYLE_ITALICS = 1;
        static final int STYLE_UNDERLINE = 2;
        static final String[] mColorMap = {"WHITE", "GREEN", "BLUE", "CYAN", "RED", "YELLOW", "MAGENTA", "INVALID"};
        final int mColor;
        final int mStyle;

        static synchronized StyleCode fromByte(byte data2) {
            int style = 0;
            int color = (data2 >> 1) & 7;
            if ((data2 & 1) != 0) {
                style = 0 | 2;
            }
            if (color == 7) {
                color = 0;
                style |= 1;
            }
            return new StyleCode(style, color);
        }

        synchronized StyleCode(int style, int color) {
            this.mStyle = style;
            this.mColor = color;
        }

        synchronized boolean isItalics() {
            return (this.mStyle & 1) != 0;
        }

        synchronized boolean isUnderline() {
            return (this.mStyle & 2) != 0;
        }

        synchronized int getColor() {
            return this.mColor;
        }

        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("{");
            str.append(mColorMap[this.mColor]);
            if ((this.mStyle & 1) != 0) {
                str.append(", ITALICS");
            }
            if ((this.mStyle & 2) != 0) {
                str.append(", UNDERLINE");
            }
            str.append("}");
            return str.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ClosedCaptionRenderer.java */
    /* loaded from: classes.dex */
    public static class PAC extends StyleCode {
        final int mCol;
        final int mRow;

        static synchronized PAC fromBytes(byte data1, byte data2) {
            int[] rowTable = {11, 1, 3, 12, 14, 5, 7, 9};
            int row = rowTable[data1 & 7] + ((data2 & 32) >> 5);
            int style = 0;
            if ((data2 & 1) != 0) {
                style = 0 | 2;
            }
            if ((data2 & WifiScanner.PnoSettings.PnoNetwork.FLAG_SAME_NETWORK) != 0) {
                int indent = (data2 >> 1) & 7;
                return new PAC(row, indent * 4, style, 0);
            }
            int indent2 = data2 >> 1;
            int color = indent2 & 7;
            if (color == 7) {
                color = 0;
                style |= 1;
            }
            return new PAC(row, -1, style, color);
        }

        synchronized PAC(int row, int col, int style, int color) {
            super(style, color);
            this.mRow = row;
            this.mCol = col;
        }

        synchronized boolean isIndentPAC() {
            return this.mCol >= 0;
        }

        synchronized int getRow() {
            return this.mRow;
        }

        synchronized int getCol() {
            return this.mCol;
        }

        @Override // android.media.Cea608CCParser.StyleCode
        public String toString() {
            return String.format("{%d, %d}, %s", Integer.valueOf(this.mRow), Integer.valueOf(this.mCol), super.toString());
        }
    }

    /* compiled from: ClosedCaptionRenderer.java */
    /* loaded from: classes.dex */
    public static class MutableBackgroundColorSpan extends CharacterStyle implements UpdateAppearance {
        private int mColor;

        public synchronized MutableBackgroundColorSpan(int color) {
            this.mColor = color;
        }

        public synchronized void setBackgroundColor(int color) {
            this.mColor = color;
        }

        public synchronized int getBackgroundColor() {
            return this.mColor;
        }

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint ds) {
            ds.bgColor = this.mColor;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ClosedCaptionRenderer.java */
    /* loaded from: classes.dex */
    public static class CCLineBuilder {
        private final StringBuilder mDisplayChars;
        private final StyleCode[] mMidRowStyles;
        private final StyleCode[] mPACStyles;

        synchronized CCLineBuilder(String str) {
            this.mDisplayChars = new StringBuilder(str);
            this.mMidRowStyles = new StyleCode[this.mDisplayChars.length()];
            this.mPACStyles = new StyleCode[this.mDisplayChars.length()];
        }

        synchronized void setCharAt(int index, char ch) {
            this.mDisplayChars.setCharAt(index, ch);
            this.mMidRowStyles[index] = null;
        }

        synchronized void setMidRowAt(int index, StyleCode m) {
            this.mDisplayChars.setCharAt(index, ' ');
            this.mMidRowStyles[index] = m;
        }

        synchronized void setPACAt(int index, PAC pac) {
            this.mPACStyles[index] = pac;
        }

        synchronized char charAt(int index) {
            return this.mDisplayChars.charAt(index);
        }

        synchronized int length() {
            return this.mDisplayChars.length();
        }

        synchronized void applyStyleSpan(SpannableStringBuilder styledText, StyleCode s, int start, int end) {
            if (s.isItalics()) {
                styledText.setSpan(new StyleSpan(2), start, end, 33);
            }
            if (s.isUnderline()) {
                styledText.setSpan(new UnderlineSpan(), start, end, 33);
            }
        }

        synchronized SpannableStringBuilder getStyledText(CaptioningManager.CaptionStyle captionStyle) {
            SpannableStringBuilder styledText = new SpannableStringBuilder(this.mDisplayChars);
            int start = -1;
            int styleStart = -1;
            StyleCode curStyle = null;
            for (int next = 0; next < this.mDisplayChars.length(); next++) {
                StyleCode newStyle = null;
                if (this.mMidRowStyles[next] != null) {
                    newStyle = this.mMidRowStyles[next];
                } else if (this.mPACStyles[next] != null && (styleStart < 0 || start < 0)) {
                    newStyle = this.mPACStyles[next];
                }
                if (newStyle != null) {
                    curStyle = newStyle;
                    if (styleStart >= 0 && start >= 0) {
                        applyStyleSpan(styledText, newStyle, styleStart, next);
                    }
                    styleStart = next;
                }
                if (this.mDisplayChars.charAt(next) != 160) {
                    if (start < 0) {
                        start = next;
                    }
                } else if (start >= 0) {
                    int expandedStart = this.mDisplayChars.charAt(start) == ' ' ? start : start - 1;
                    int expandedEnd = this.mDisplayChars.charAt(next + (-1)) == ' ' ? next : next + 1;
                    styledText.setSpan(new MutableBackgroundColorSpan(captionStyle.backgroundColor), expandedStart, expandedEnd, 33);
                    if (styleStart >= 0) {
                        applyStyleSpan(styledText, curStyle, styleStart, expandedEnd);
                    }
                    start = -1;
                }
            }
            return styledText;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ClosedCaptionRenderer.java */
    /* loaded from: classes.dex */
    public static class CCMemory {
        private final String mBlankLine;
        private int mCol;
        private final CCLineBuilder[] mLines = new CCLineBuilder[17];
        private int mRow;

        synchronized CCMemory() {
            char[] blank = new char[34];
            Arrays.fill(blank, (char) Cea608CCParser.TS);
            this.mBlankLine = new String(blank);
        }

        synchronized void erase() {
            for (int i = 0; i < this.mLines.length; i++) {
                this.mLines[i] = null;
            }
            this.mRow = 15;
            this.mCol = 1;
        }

        synchronized void der() {
            if (this.mLines[this.mRow] != null) {
                for (int i = 0; i < this.mCol; i++) {
                    if (this.mLines[this.mRow].charAt(i) != 160) {
                        for (int j = this.mCol; j < this.mLines[this.mRow].length(); j++) {
                            this.mLines[j].setCharAt(j, Cea608CCParser.TS);
                        }
                        return;
                    }
                }
                this.mLines[this.mRow] = null;
            }
        }

        synchronized void tab(int tabs) {
            moveCursorByCol(tabs);
        }

        synchronized void bs() {
            moveCursorByCol(-1);
            if (this.mLines[this.mRow] != null) {
                this.mLines[this.mRow].setCharAt(this.mCol, Cea608CCParser.TS);
                if (this.mCol == 31) {
                    this.mLines[this.mRow].setCharAt(32, Cea608CCParser.TS);
                }
            }
        }

        synchronized void cr() {
            moveCursorTo(this.mRow + 1, 1);
        }

        synchronized void rollUp(int windowSize) {
            for (int i = 0; i <= this.mRow - windowSize; i++) {
                this.mLines[i] = null;
            }
            int startRow = (this.mRow - windowSize) + 1;
            if (startRow < 1) {
                startRow = 1;
            }
            for (int i2 = startRow; i2 < this.mRow; i2++) {
                this.mLines[i2] = this.mLines[i2 + 1];
            }
            for (int i3 = this.mRow; i3 < this.mLines.length; i3++) {
                this.mLines[i3] = null;
            }
            this.mCol = 1;
        }

        synchronized void writeText(String text) {
            for (int i = 0; i < text.length(); i++) {
                getLineBuffer(this.mRow).setCharAt(this.mCol, text.charAt(i));
                moveCursorByCol(1);
            }
        }

        synchronized void writeMidRowCode(StyleCode m) {
            getLineBuffer(this.mRow).setMidRowAt(this.mCol, m);
            moveCursorByCol(1);
        }

        synchronized void writePAC(PAC pac) {
            if (pac.isIndentPAC()) {
                moveCursorTo(pac.getRow(), pac.getCol());
            } else {
                moveCursorTo(pac.getRow(), 1);
            }
            getLineBuffer(this.mRow).setPACAt(this.mCol, pac);
        }

        synchronized SpannableStringBuilder[] getStyledText(CaptioningManager.CaptionStyle captionStyle) {
            ArrayList<SpannableStringBuilder> rows = new ArrayList<>(15);
            for (int i = 1; i <= 15; i++) {
                rows.add(this.mLines[i] != null ? this.mLines[i].getStyledText(captionStyle) : null);
            }
            return (SpannableStringBuilder[]) rows.toArray(new SpannableStringBuilder[15]);
        }

        private static synchronized int clamp(int x, int min, int max) {
            return x < min ? min : x > max ? max : x;
        }

        private synchronized void moveCursorTo(int row, int col) {
            this.mRow = clamp(row, 1, 15);
            this.mCol = clamp(col, 1, 32);
        }

        private synchronized void moveCursorToRow(int row) {
            this.mRow = clamp(row, 1, 15);
        }

        private synchronized void moveCursorByCol(int col) {
            this.mCol = clamp(this.mCol + col, 1, 32);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void moveBaselineTo(int baseRow, int windowSize) {
            if (this.mRow == baseRow) {
                return;
            }
            int actualWindowSize = windowSize;
            if (baseRow < actualWindowSize) {
                actualWindowSize = baseRow;
            }
            if (this.mRow < actualWindowSize) {
                actualWindowSize = this.mRow;
            }
            if (baseRow < this.mRow) {
                for (int i = actualWindowSize - 1; i >= 0; i--) {
                    this.mLines[baseRow - i] = this.mLines[this.mRow - i];
                }
            } else {
                for (int i2 = 0; i2 < actualWindowSize; i2++) {
                    this.mLines[baseRow - i2] = this.mLines[this.mRow - i2];
                }
            }
            for (int i3 = 0; i3 <= baseRow - windowSize; i3++) {
                this.mLines[i3] = null;
            }
            for (int i4 = baseRow + 1; i4 < this.mLines.length; i4++) {
                this.mLines[i4] = null;
            }
        }

        private synchronized CCLineBuilder getLineBuffer(int row) {
            if (this.mLines[row] == null) {
                this.mLines[row] = new CCLineBuilder(this.mBlankLine);
            }
            return this.mLines[row];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ClosedCaptionRenderer.java */
    /* loaded from: classes.dex */
    public static class CCData {
        private final byte mData1;
        private final byte mData2;
        private final byte mType;
        private static final String[] mCtrlCodeMap = {"RCL", "BS", "AOF", "AON", "DER", "RU2", "RU3", "RU4", "FON", "RDC", "TR", "RTD", "EDM", "CR", "ENM", "EOC"};
        private static final String[] mSpecialCharMap = {"®", "°", "½", "¿", "™", "¢", "£", "♪", "à", " ", "è", "â", "ê", "î", "ô", "û"};
        private static final String[] mSpanishCharMap = {"Á", "É", "Ó", "Ú", "Ü", "ü", "‘", "¡", PhoneConstants.APN_TYPE_ALL, "'", "—", "©", "℠", "•", "“", "”", "À", "Â", "Ç", "È", "Ê", "Ë", "ë", "Î", "Ï", "ï", "Ô", "Ù", "ù", "Û", "«", "»"};
        private static final String[] mProtugueseCharMap = {"Ã", "ã", "Í", "Ì", "ì", "Ò", "ò", "Õ", "õ", "{", "}", "\\", "^", "_", "|", "~", "Ä", "ä", "Ö", "ö", "ß", "¥", "¤", "│", "Å", "å", "Ø", "ø", "┌", "┐", "└", "┘"};

        static synchronized CCData[] fromByteArray(byte[] data) {
            CCData[] ccData = new CCData[data.length / 3];
            for (int i = 0; i < ccData.length; i++) {
                ccData[i] = new CCData(data[i * 3], data[(i * 3) + 1], data[(i * 3) + 2]);
            }
            return ccData;
        }

        synchronized CCData(byte type, byte data1, byte data2) {
            this.mType = type;
            this.mData1 = data1;
            this.mData2 = data2;
        }

        synchronized int getCtrlCode() {
            if ((this.mData1 == 20 || this.mData1 == 28) && this.mData2 >= 32 && this.mData2 <= 47) {
                return this.mData2;
            }
            return -1;
        }

        synchronized StyleCode getMidRow() {
            if ((this.mData1 == 17 || this.mData1 == 25) && this.mData2 >= 32 && this.mData2 <= 47) {
                return StyleCode.fromByte(this.mData2);
            }
            return null;
        }

        synchronized PAC getPAC() {
            if ((this.mData1 & 112) == 16 && (this.mData2 & BluetoothHidDevice.SUBCLASS1_KEYBOARD) == 64) {
                if ((this.mData1 & 7) != 0 || (this.mData2 & 32) == 0) {
                    return PAC.fromBytes(this.mData1, this.mData2);
                }
                return null;
            }
            return null;
        }

        synchronized int getTabOffset() {
            if ((this.mData1 == 23 || this.mData1 == 31) && this.mData2 >= 33 && this.mData2 <= 35) {
                return this.mData2 & 3;
            }
            return 0;
        }

        synchronized boolean isDisplayableChar() {
            return isBasicChar() || isSpecialChar() || isExtendedChar();
        }

        synchronized String getDisplayText() {
            String str = getBasicChars();
            if (str == null) {
                String str2 = getSpecialChar();
                if (str2 == null) {
                    return getExtendedChar();
                }
                return str2;
            }
            return str;
        }

        private synchronized String ctrlCodeToString(int ctrlCode) {
            return mCtrlCodeMap[ctrlCode - 32];
        }

        private synchronized boolean isBasicChar() {
            return this.mData1 >= 32 && this.mData1 <= Byte.MAX_VALUE;
        }

        private synchronized boolean isSpecialChar() {
            return (this.mData1 == 17 || this.mData1 == 25) && this.mData2 >= 48 && this.mData2 <= 63;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean isExtendedChar() {
            return (this.mData1 == 18 || this.mData1 == 26 || this.mData1 == 19 || this.mData1 == 27) && this.mData2 >= 32 && this.mData2 <= 63;
        }

        private synchronized char getBasicChar(byte data) {
            if (data != 42) {
                if (data == 92) {
                    return (char) 233;
                }
                switch (data) {
                    case 94:
                        return (char) 237;
                    case 95:
                        return (char) 243;
                    case 96:
                        return (char) 250;
                    default:
                        switch (data) {
                            case 123:
                                return (char) 231;
                            case 124:
                                return (char) 247;
                            case 125:
                                return (char) 209;
                            case 126:
                                return (char) 241;
                            case Byte.MAX_VALUE:
                                return (char) 9608;
                            default:
                                char c = (char) data;
                                return c;
                        }
                }
            }
            return (char) 225;
        }

        private synchronized String getBasicChars() {
            if (this.mData1 >= 32 && this.mData1 <= Byte.MAX_VALUE) {
                StringBuilder builder = new StringBuilder(2);
                builder.append(getBasicChar(this.mData1));
                if (this.mData2 >= 32 && this.mData2 <= Byte.MAX_VALUE) {
                    builder.append(getBasicChar(this.mData2));
                }
                return builder.toString();
            }
            return null;
        }

        private synchronized String getSpecialChar() {
            if ((this.mData1 == 17 || this.mData1 == 25) && this.mData2 >= 48 && this.mData2 <= 63) {
                return mSpecialCharMap[this.mData2 - 48];
            }
            return null;
        }

        private synchronized String getExtendedChar() {
            if ((this.mData1 == 18 || this.mData1 == 26) && this.mData2 >= 32 && this.mData2 <= 63) {
                return mSpanishCharMap[this.mData2 - 32];
            }
            if ((this.mData1 == 19 || this.mData1 == 27) && this.mData2 >= 32 && this.mData2 <= 63) {
                return mProtugueseCharMap[this.mData2 - 32];
            }
            return null;
        }

        public String toString() {
            if (this.mData1 < 16 && this.mData2 < 16) {
                return String.format("[%d]Null: %02x %02x", Byte.valueOf(this.mType), Byte.valueOf(this.mData1), Byte.valueOf(this.mData2));
            }
            int ctrlCode = getCtrlCode();
            if (ctrlCode != -1) {
                return String.format("[%d]%s", Byte.valueOf(this.mType), ctrlCodeToString(ctrlCode));
            }
            int tabOffset = getTabOffset();
            if (tabOffset > 0) {
                return String.format("[%d]Tab%d", Byte.valueOf(this.mType), Integer.valueOf(tabOffset));
            }
            PAC pac = getPAC();
            if (pac != null) {
                return String.format("[%d]PAC: %s", Byte.valueOf(this.mType), pac.toString());
            }
            StyleCode m = getMidRow();
            if (m != null) {
                return String.format("[%d]Mid-row: %s", Byte.valueOf(this.mType), m.toString());
            }
            if (isDisplayableChar()) {
                return String.format("[%d]Displayable: %s (%02x %02x)", Byte.valueOf(this.mType), getDisplayText(), Byte.valueOf(this.mData1), Byte.valueOf(this.mData2));
            }
            return String.format("[%d]Invalid: %02x %02x", Byte.valueOf(this.mType), Byte.valueOf(this.mData1), Byte.valueOf(this.mData2));
        }
    }
}
