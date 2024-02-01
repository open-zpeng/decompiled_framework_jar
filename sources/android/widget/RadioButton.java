package android.widget;

import android.content.Context;
import android.util.AttributeSet;
/* loaded from: classes3.dex */
public class RadioButton extends CompoundButton {
    public RadioButton(Context context) {
        this(context, null);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 16842878);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void toggle() {
        if (!isChecked()) {
            super.toggle();
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return RadioButton.class.getName();
    }
}
