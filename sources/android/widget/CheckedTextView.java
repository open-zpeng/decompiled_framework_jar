package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.R;
/* loaded from: classes3.dex */
public class CheckedTextView extends TextView implements Checkable {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private int mBasePadding;
    public protected Drawable mCheckMarkDrawable;
    public protected int mCheckMarkGravity;
    private int mCheckMarkResource;
    private ColorStateList mCheckMarkTintList;
    private PorterDuff.Mode mCheckMarkTintMode;
    private int mCheckMarkWidth;
    private boolean mChecked;
    private boolean mHasCheckMarkTint;
    private boolean mHasCheckMarkTintMode;
    private boolean mNeedRequestlayout;

    public CheckedTextView(Context context) {
        this(context, null);
    }

    public CheckedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 16843720);
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCheckMarkTintList = null;
        this.mCheckMarkTintMode = null;
        this.mHasCheckMarkTint = false;
        this.mHasCheckMarkTintMode = false;
        this.mCheckMarkGravity = Gravity.END;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckedTextView, defStyleAttr, defStyleRes);
        Drawable d = a.getDrawable(1);
        if (d != null) {
            setCheckMarkDrawable(d);
        }
        if (a.hasValue(3)) {
            this.mCheckMarkTintMode = Drawable.parseTintMode(a.getInt(3, -1), this.mCheckMarkTintMode);
            this.mHasCheckMarkTintMode = true;
        }
        if (a.hasValue(2)) {
            this.mCheckMarkTintList = a.getColorStateList(2);
            this.mHasCheckMarkTint = true;
        }
        this.mCheckMarkGravity = a.getInt(4, Gravity.END);
        boolean checked = a.getBoolean(0, false);
        setChecked(checked);
        a.recycle();
        applyCheckMarkTint();
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setChecked(!this.mChecked);
    }

    @Override // android.widget.Checkable
    @ViewDebug.ExportedProperty
    public boolean isChecked() {
        return this.mChecked;
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean checked) {
        if (this.mChecked != checked) {
            this.mChecked = checked;
            refreshDrawableState();
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    public void setCheckMarkDrawable(int resId) {
        if (resId != 0 && resId == this.mCheckMarkResource) {
            return;
        }
        Drawable d = resId != 0 ? getContext().getDrawable(resId) : null;
        setCheckMarkDrawableInternal(d, resId);
    }

    public void setCheckMarkDrawable(Drawable d) {
        setCheckMarkDrawableInternal(d, 0);
    }

    private synchronized void setCheckMarkDrawableInternal(Drawable d, int resId) {
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.setCallback(null);
            unscheduleDrawable(this.mCheckMarkDrawable);
        }
        this.mNeedRequestlayout = d != this.mCheckMarkDrawable;
        if (d != null) {
            d.setCallback(this);
            d.setVisible(getVisibility() == 0, false);
            d.setState(CHECKED_STATE_SET);
            setMinHeight(d.getIntrinsicHeight());
            this.mCheckMarkWidth = d.getIntrinsicWidth();
            d.setState(getDrawableState());
        } else {
            this.mCheckMarkWidth = 0;
        }
        this.mCheckMarkDrawable = d;
        this.mCheckMarkResource = resId;
        applyCheckMarkTint();
        resolvePadding();
    }

    public void setCheckMarkTintList(ColorStateList tint) {
        this.mCheckMarkTintList = tint;
        this.mHasCheckMarkTint = true;
        applyCheckMarkTint();
    }

    public ColorStateList getCheckMarkTintList() {
        return this.mCheckMarkTintList;
    }

    public void setCheckMarkTintMode(PorterDuff.Mode tintMode) {
        this.mCheckMarkTintMode = tintMode;
        this.mHasCheckMarkTintMode = true;
        applyCheckMarkTint();
    }

    public PorterDuff.Mode getCheckMarkTintMode() {
        return this.mCheckMarkTintMode;
    }

    private synchronized void applyCheckMarkTint() {
        if (this.mCheckMarkDrawable != null) {
            if (this.mHasCheckMarkTint || this.mHasCheckMarkTintMode) {
                this.mCheckMarkDrawable = this.mCheckMarkDrawable.mutate();
                if (this.mHasCheckMarkTint) {
                    this.mCheckMarkDrawable.setTintList(this.mCheckMarkTintList);
                }
                if (this.mHasCheckMarkTintMode) {
                    this.mCheckMarkDrawable.setTintMode(this.mCheckMarkTintMode);
                }
                if (this.mCheckMarkDrawable.isStateful()) {
                    this.mCheckMarkDrawable.setState(getDrawableState());
                }
            }
        }
    }

    @Override // android.view.View
    @RemotableViewMethod
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.setVisible(visibility == 0, false);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.jumpToCurrentState();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public boolean verifyDrawable(Drawable who) {
        return who == this.mCheckMarkDrawable || super.verifyDrawable(who);
    }

    public Drawable getCheckMarkDrawable() {
        return this.mCheckMarkDrawable;
    }

    protected synchronized void internalSetPadding(int left, int top, int right, int bottom) {
        super.internalSetPadding(left, top, right, bottom);
        setBasePadding(isCheckMarkAtStart());
    }

    @Override // android.widget.TextView, android.view.View
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        updatePadding();
    }

    private synchronized void updatePadding() {
        resetPaddingToInitialValues();
        int newPadding = this.mCheckMarkDrawable != null ? this.mCheckMarkWidth + this.mBasePadding : this.mBasePadding;
        if (isCheckMarkAtStart()) {
            this.mNeedRequestlayout |= this.mPaddingLeft != newPadding;
            this.mPaddingLeft = newPadding;
        } else {
            this.mNeedRequestlayout |= this.mPaddingRight != newPadding;
            this.mPaddingRight = newPadding;
        }
        if (this.mNeedRequestlayout) {
            requestLayout();
            this.mNeedRequestlayout = false;
        }
    }

    private synchronized void setBasePadding(boolean checkmarkAtStart) {
        if (checkmarkAtStart) {
            this.mBasePadding = this.mPaddingLeft;
        } else {
            this.mBasePadding = this.mPaddingRight;
        }
    }

    private synchronized boolean isCheckMarkAtStart() {
        int gravity = Gravity.getAbsoluteGravity(this.mCheckMarkGravity, getLayoutDirection());
        int hgrav = gravity & 7;
        return hgrav == 3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        int right;
        int left;
        super.onDraw(canvas);
        Drawable checkMarkDrawable = this.mCheckMarkDrawable;
        if (checkMarkDrawable != null) {
            int verticalGravity = getGravity() & 112;
            int height = checkMarkDrawable.getIntrinsicHeight();
            int y = 0;
            if (verticalGravity == 16) {
                y = (getHeight() - height) / 2;
            } else if (verticalGravity == 80) {
                y = getHeight() - height;
            }
            boolean checkMarkAtStart = isCheckMarkAtStart();
            int width = getWidth();
            int top = y;
            int bottom = top + height;
            if (checkMarkAtStart) {
                left = this.mBasePadding;
                right = this.mCheckMarkWidth + left;
            } else {
                int left2 = this.mBasePadding;
                right = width - left2;
                left = right - this.mCheckMarkWidth;
            }
            checkMarkDrawable.setBounds(this.mScrollX + left, top, this.mScrollX + right, bottom);
            checkMarkDrawable.draw(canvas);
            Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(this.mScrollX + left, top, this.mScrollX + right, bottom);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable checkMarkDrawable = this.mCheckMarkDrawable;
        if (checkMarkDrawable != null && checkMarkDrawable.isStateful() && checkMarkDrawable.setState(getDrawableState())) {
            invalidateDrawable(checkMarkDrawable);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.setHotspot(x, y);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return CheckedTextView.class.getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: android.widget.CheckedTextView.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean checked;

        synchronized SavedState(Parcelable superState) {
            super(superState);
        }

        private synchronized SavedState(Parcel in) {
            super(in);
            this.checked = ((Boolean) in.readValue(null)).booleanValue();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(Boolean.valueOf(this.checked));
        }

        public String toString() {
            return "CheckedTextView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " checked=" + this.checked + "}";
        }
    }

    @Override // android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.checked = isChecked();
        return ss;
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }

    @Override // android.widget.TextView
    public synchronized void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setChecked(this.mChecked);
    }

    @Override // android.widget.TextView, android.view.View
    public synchronized void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setCheckable(true);
        info.setChecked(this.mChecked);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public synchronized void encodeProperties(ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        stream.addProperty("text:checked", isChecked());
    }
}
