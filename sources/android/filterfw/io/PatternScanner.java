package android.filterfw.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class PatternScanner {
    public protected Pattern mIgnorePattern;
    public protected String mInput;
    public protected int mOffset = 0;
    public protected int mLineNo = 0;
    public protected int mStartOfLine = 0;

    private protected synchronized PatternScanner(String input) {
        this.mInput = input;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized PatternScanner(String input, Pattern ignorePattern) {
        this.mInput = input;
        this.mIgnorePattern = ignorePattern;
        skip(this.mIgnorePattern);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String tryEat(Pattern pattern) {
        if (this.mIgnorePattern != null) {
            skip(this.mIgnorePattern);
        }
        Matcher matcher = pattern.matcher(this.mInput);
        matcher.region(this.mOffset, this.mInput.length());
        String result = null;
        if (matcher.lookingAt()) {
            updateLineCount(this.mOffset, matcher.end());
            this.mOffset = matcher.end();
            result = this.mInput.substring(matcher.start(), matcher.end());
        }
        if (result != null && this.mIgnorePattern != null) {
            skip(this.mIgnorePattern);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String eat(Pattern pattern, String tokenName) {
        String result = tryEat(pattern);
        if (result == null) {
            throw new RuntimeException(unexpectedTokenMessage(tokenName));
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean peek(Pattern pattern) {
        if (this.mIgnorePattern != null) {
            skip(this.mIgnorePattern);
        }
        Matcher matcher = pattern.matcher(this.mInput);
        matcher.region(this.mOffset, this.mInput.length());
        return matcher.lookingAt();
    }

    private protected synchronized void skip(Pattern pattern) {
        Matcher matcher = pattern.matcher(this.mInput);
        matcher.region(this.mOffset, this.mInput.length());
        if (matcher.lookingAt()) {
            updateLineCount(this.mOffset, matcher.end());
            this.mOffset = matcher.end();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean atEnd() {
        return this.mOffset >= this.mInput.length();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int lineNo() {
        return this.mLineNo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String unexpectedTokenMessage(String tokenName) {
        String line = this.mInput.substring(this.mStartOfLine, this.mOffset);
        return "Unexpected token on line " + (this.mLineNo + 1) + " after '" + line + "' <- Expected " + tokenName + "!";
    }

    private protected synchronized void updateLineCount(int start, int end) {
        for (int i = start; i < end; i++) {
            if (this.mInput.charAt(i) == '\n') {
                this.mLineNo++;
                this.mStartOfLine = i + 1;
            }
        }
    }
}
