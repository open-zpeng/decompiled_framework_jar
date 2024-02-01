package android.inputmethodservice;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
/* loaded from: classes.dex */
public class ExtractEditText extends EditText {
    private InputMethodService mIME;
    private int mSettingExtractedText;

    public ExtractEditText(Context context) {
        super(context, null);
    }

    public ExtractEditText(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.editTextStyle);
    }

    public ExtractEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ExtractEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setIME(InputMethodService ime) {
        this.mIME = ime;
    }

    public void startInternalChanges() {
        this.mSettingExtractedText++;
    }

    public void finishInternalChanges() {
        this.mSettingExtractedText--;
    }

    @Override // android.widget.TextView
    public void setExtractedText(ExtractedText text) {
        try {
            this.mSettingExtractedText++;
            super.setExtractedText(text);
        } finally {
            this.mSettingExtractedText--;
        }
    }

    @Override // android.widget.TextView
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (this.mSettingExtractedText == 0 && this.mIME != null && selStart >= 0 && selEnd >= 0) {
            this.mIME.onExtractedSelectionChanged(selStart, selEnd);
        }
    }

    @Override // android.view.View
    public boolean performClick() {
        if (!super.performClick() && this.mIME != null) {
            this.mIME.onExtractedTextClicked();
            return true;
        }
        return false;
    }

    @Override // android.widget.TextView
    public boolean onTextContextMenuItem(int id) {
        if (id == 16908319 || id == 16908340) {
            return super.onTextContextMenuItem(id);
        }
        if (this.mIME != null && this.mIME.onExtractTextContextMenuItem(id)) {
            if (id == 16908321 || id == 16908322) {
                stopTextActionMode();
                return true;
            }
            return true;
        }
        return super.onTextContextMenuItem(id);
    }

    @Override // android.widget.TextView
    public boolean isInputMethodTarget() {
        return true;
    }

    public boolean hasVerticalScrollBar() {
        return computeVerticalScrollRange() > computeVerticalScrollExtent();
    }

    @Override // android.view.View
    public boolean hasWindowFocus() {
        return isEnabled();
    }

    @Override // android.view.View
    public boolean isFocused() {
        return isEnabled();
    }

    @Override // android.view.View
    public boolean hasFocus() {
        return isEnabled();
    }

    @Override // android.widget.TextView
    protected synchronized void viewClicked(InputMethodManager imm) {
        if (this.mIME != null) {
            this.mIME.onViewClicked(false);
        }
    }

    @Override // android.widget.TextView
    public synchronized boolean isInExtractedMode() {
        return true;
    }

    protected synchronized void deleteText_internal(int start, int end) {
        this.mIME.onExtractedDeleteText(start, end);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView
    public synchronized void replaceText_internal(int start, int end, CharSequence text) {
        this.mIME.onExtractedReplaceText(start, end, text);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView
    public synchronized void setSpan_internal(Object span, int start, int end, int flags) {
        this.mIME.onExtractedSetSpan(span, start, end, flags);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView
    public synchronized void setCursorPosition_internal(int start, int end) {
        this.mIME.onExtractedSelectionChanged(start, end);
    }
}
