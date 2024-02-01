package android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.text.method.KeyListener;
import android.text.method.TextKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;

@Deprecated
/* loaded from: classes3.dex */
public class DialerFilter extends RelativeLayout {
    public static final int DIGITS_AND_LETTERS = 1;
    public static final int DIGITS_AND_LETTERS_NO_DIGITS = 2;
    public static final int DIGITS_AND_LETTERS_NO_LETTERS = 3;
    public static final int DIGITS_ONLY = 4;
    public static final int LETTERS_ONLY = 5;
    EditText mDigits;
    EditText mHint;
    ImageView mIcon;
    InputFilter[] mInputFilters;
    private boolean mIsQwerty;
    EditText mLetters;
    int mMode;
    EditText mPrimary;

    public DialerFilter(Context context) {
        super(context);
    }

    public DialerFilter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mInputFilters = new InputFilter[]{new InputFilter.AllCaps()};
        this.mHint = (EditText) findViewById(16908293);
        EditText editText = this.mHint;
        if (editText == null) {
            throw new IllegalStateException("DialerFilter must have a child EditText named hint");
        }
        editText.setFilters(this.mInputFilters);
        this.mLetters = this.mHint;
        this.mLetters.setKeyListener(TextKeyListener.getInstance());
        this.mLetters.setMovementMethod(null);
        this.mLetters.setFocusable(false);
        this.mPrimary = (EditText) findViewById(16908300);
        EditText editText2 = this.mPrimary;
        if (editText2 == null) {
            throw new IllegalStateException("DialerFilter must have a child EditText named primary");
        }
        editText2.setFilters(this.mInputFilters);
        this.mDigits = this.mPrimary;
        this.mDigits.setKeyListener(DialerKeyListener.getInstance());
        this.mDigits.setMovementMethod(null);
        this.mDigits.setFocusable(false);
        this.mIcon = (ImageView) findViewById(16908294);
        setFocusable(true);
        this.mIsQwerty = true;
        setMode(1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        ImageView imageView = this.mIcon;
        if (imageView != null) {
            imageView.setVisibility(focused ? 0 : 8);
        }
    }

    public boolean isQwertyKeyboard() {
        return this.mIsQwerty;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x001b, code lost:
        if (r1 != 5) goto L43;
     */
    @Override // android.view.View, android.view.KeyEvent.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onKeyDown(int r8, android.view.KeyEvent r9) {
        /*
            r7 = this;
            r0 = 0
            r1 = 66
            r2 = 1
            if (r8 == r1) goto Ld5
            r1 = 67
            r3 = 5
            r4 = 4
            r5 = 3
            r6 = 2
            if (r8 == r1) goto L66
            switch(r8) {
                case 19: goto Ld5;
                case 20: goto Ld5;
                case 21: goto Ld5;
                case 22: goto Ld5;
                case 23: goto Ld5;
                default: goto L11;
            }
        L11:
            int r1 = r7.mMode
            if (r1 == r2) goto L2f
            if (r1 == r6) goto L27
            if (r1 == r5) goto L1f
            if (r1 == r4) goto L1f
            if (r1 == r3) goto L27
            goto Ld6
        L1f:
            android.widget.EditText r1 = r7.mDigits
            boolean r0 = r1.onKeyDown(r8, r9)
            goto Ld6
        L27:
            android.widget.EditText r1 = r7.mLetters
            boolean r0 = r1.onKeyDown(r8, r9)
            goto Ld6
        L2f:
            android.widget.EditText r1 = r7.mLetters
            boolean r0 = r1.onKeyDown(r8, r9)
            boolean r1 = android.view.KeyEvent.isModifierKey(r8)
            if (r1 == 0) goto L43
            android.widget.EditText r1 = r7.mDigits
            r1.onKeyDown(r8, r9)
            r0 = 1
            goto Ld6
        L43:
            boolean r1 = r9.isPrintingKey()
            if (r1 != 0) goto L51
            r3 = 62
            if (r8 == r3) goto L51
            r3 = 61
            if (r8 != r3) goto Ld6
        L51:
            char[] r3 = android.text.method.DialerKeyListener.CHARACTERS
            char r3 = r9.getMatch(r3)
            if (r3 == 0) goto L61
            android.widget.EditText r4 = r7.mDigits
            boolean r4 = r4.onKeyDown(r8, r9)
            r0 = r0 & r4
            goto L64
        L61:
            r7.setMode(r6)
        L64:
            goto Ld6
        L66:
            int r1 = r7.mMode
            if (r1 == r2) goto Lc6
            if (r1 == r6) goto La6
            if (r1 == r5) goto L81
            if (r1 == r4) goto L7a
            if (r1 == r3) goto L73
            goto Ld4
        L73:
            android.widget.EditText r1 = r7.mLetters
            boolean r0 = r1.onKeyDown(r8, r9)
            goto Ld4
        L7a:
            android.widget.EditText r1 = r7.mDigits
            boolean r0 = r1.onKeyDown(r8, r9)
            goto Ld4
        L81:
            android.widget.EditText r1 = r7.mDigits
            android.text.Editable r1 = r1.getText()
            int r1 = r1.length()
            android.widget.EditText r3 = r7.mLetters
            android.text.Editable r3 = r3.getText()
            int r3 = r3.length()
            if (r1 != r3) goto L9f
            android.widget.EditText r1 = r7.mLetters
            r1.onKeyDown(r8, r9)
            r7.setMode(r2)
        L9f:
            android.widget.EditText r1 = r7.mDigits
            boolean r0 = r1.onKeyDown(r8, r9)
            goto Ld4
        La6:
            android.widget.EditText r1 = r7.mLetters
            boolean r0 = r1.onKeyDown(r8, r9)
            android.widget.EditText r1 = r7.mLetters
            android.text.Editable r1 = r1.getText()
            int r1 = r1.length()
            android.widget.EditText r3 = r7.mDigits
            android.text.Editable r3 = r3.getText()
            int r3 = r3.length()
            if (r1 != r3) goto Ld4
            r7.setMode(r2)
            goto Ld4
        Lc6:
            android.widget.EditText r1 = r7.mDigits
            boolean r0 = r1.onKeyDown(r8, r9)
            android.widget.EditText r1 = r7.mLetters
            boolean r1 = r1.onKeyDown(r8, r9)
            r0 = r0 & r1
        Ld4:
            goto Ld6
        Ld5:
        Ld6:
            if (r0 != 0) goto Ldd
            boolean r1 = super.onKeyDown(r8, r9)
            return r1
        Ldd:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.DialerFilter.onKeyDown(int, android.view.KeyEvent):boolean");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean a = this.mLetters.onKeyUp(keyCode, event);
        boolean b = this.mDigits.onKeyUp(keyCode, event);
        return a || b;
    }

    public int getMode() {
        return this.mMode;
    }

    public void setMode(int newMode) {
        if (newMode == 1) {
            makeDigitsPrimary();
            this.mLetters.setVisibility(0);
            this.mDigits.setVisibility(0);
        } else if (newMode == 2) {
            makeLettersPrimary();
            this.mLetters.setVisibility(0);
            this.mDigits.setVisibility(4);
        } else if (newMode == 3) {
            makeDigitsPrimary();
            this.mLetters.setVisibility(4);
            this.mDigits.setVisibility(0);
        } else if (newMode == 4) {
            makeDigitsPrimary();
            this.mLetters.setVisibility(8);
            this.mDigits.setVisibility(0);
        } else if (newMode == 5) {
            makeLettersPrimary();
            this.mLetters.setVisibility(0);
            this.mDigits.setVisibility(8);
        }
        int oldMode = this.mMode;
        this.mMode = newMode;
        onModeChange(oldMode, newMode);
    }

    private void makeLettersPrimary() {
        if (this.mPrimary == this.mDigits) {
            swapPrimaryAndHint(true);
        }
    }

    private void makeDigitsPrimary() {
        if (this.mPrimary == this.mLetters) {
            swapPrimaryAndHint(false);
        }
    }

    private void swapPrimaryAndHint(boolean makeLettersPrimary) {
        Editable lettersText = this.mLetters.getText();
        Editable digitsText = this.mDigits.getText();
        KeyListener lettersInput = this.mLetters.getKeyListener();
        KeyListener digitsInput = this.mDigits.getKeyListener();
        if (makeLettersPrimary) {
            this.mLetters = this.mPrimary;
            this.mDigits = this.mHint;
        } else {
            this.mLetters = this.mHint;
            this.mDigits = this.mPrimary;
        }
        this.mLetters.setKeyListener(lettersInput);
        this.mLetters.setText(lettersText);
        Editable lettersText2 = this.mLetters.getText();
        Selection.setSelection(lettersText2, lettersText2.length());
        this.mDigits.setKeyListener(digitsInput);
        this.mDigits.setText(digitsText);
        Editable digitsText2 = this.mDigits.getText();
        Selection.setSelection(digitsText2, digitsText2.length());
        this.mPrimary.setFilters(this.mInputFilters);
        this.mHint.setFilters(this.mInputFilters);
    }

    public CharSequence getLetters() {
        if (this.mLetters.getVisibility() == 0) {
            return this.mLetters.getText();
        }
        return "";
    }

    public CharSequence getDigits() {
        if (this.mDigits.getVisibility() == 0) {
            return this.mDigits.getText();
        }
        return "";
    }

    public CharSequence getFilterText() {
        if (this.mMode != 4) {
            return getLetters();
        }
        return getDigits();
    }

    public void append(String text) {
        int i = this.mMode;
        if (i == 1) {
            this.mDigits.getText().append((CharSequence) text);
            this.mLetters.getText().append((CharSequence) text);
            return;
        }
        if (i != 2) {
            if (i == 3 || i == 4) {
                this.mDigits.getText().append((CharSequence) text);
                return;
            } else if (i != 5) {
                return;
            }
        }
        this.mLetters.getText().append((CharSequence) text);
    }

    public void clearText() {
        Editable text = this.mLetters.getText();
        text.clear();
        Editable text2 = this.mDigits.getText();
        text2.clear();
        if (this.mIsQwerty) {
            setMode(1);
        } else {
            setMode(4);
        }
    }

    public void setLettersWatcher(TextWatcher watcher) {
        Spannable text = this.mLetters.getText();
        Spannable span = text;
        span.setSpan(watcher, 0, text.length(), 18);
    }

    public void setDigitsWatcher(TextWatcher watcher) {
        Spannable text = this.mDigits.getText();
        Spannable span = text;
        span.setSpan(watcher, 0, text.length(), 18);
    }

    public void setFilterWatcher(TextWatcher watcher) {
        if (this.mMode != 4) {
            setLettersWatcher(watcher);
        } else {
            setDigitsWatcher(watcher);
        }
    }

    public void removeFilterWatcher(TextWatcher watcher) {
        Spannable text;
        if (this.mMode != 4) {
            text = this.mLetters.getText();
        } else {
            text = this.mDigits.getText();
        }
        text.removeSpan(watcher);
    }

    protected void onModeChange(int oldMode, int newMode) {
    }
}
