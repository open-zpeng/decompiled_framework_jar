package android.widget;

import android.animation.ObjectAnimator;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.AllCapsTransformationMethod;
import android.text.method.TransformationMethod2;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;

/* loaded from: classes3.dex */
public class Switch extends CompoundButton {
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int THUMB_ANIMATION_DURATION = 250;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private static final int TOUCH_MODE_IDLE = 0;
    private boolean mHasThumbTint;
    private boolean mHasThumbTintMode;
    private boolean mHasTrackTint;
    private boolean mHasTrackTintMode;
    private int mMinFlingVelocity;
    @UnsupportedAppUsage
    private Layout mOffLayout;
    @UnsupportedAppUsage
    private Layout mOnLayout;
    private ObjectAnimator mPositionAnimator;
    private boolean mShowText;
    private boolean mSplitTrack;
    private int mSwitchBottom;
    @UnsupportedAppUsage
    private int mSwitchHeight;
    private int mSwitchLeft;
    @UnsupportedAppUsage
    private int mSwitchMinWidth;
    private int mSwitchPadding;
    private int mSwitchRight;
    private int mSwitchTop;
    private TransformationMethod2 mSwitchTransformationMethod;
    @UnsupportedAppUsage
    private int mSwitchWidth;
    private final Rect mTempRect;
    private ColorStateList mTextColors;
    private CharSequence mTextOff;
    private CharSequence mTextOn;
    private TextPaint mTextPaint;
    private BlendMode mThumbBlendMode;
    @UnsupportedAppUsage
    private Drawable mThumbDrawable;
    private float mThumbPosition;
    private int mThumbTextPadding;
    private ColorStateList mThumbTintList;
    @UnsupportedAppUsage
    private int mThumbWidth;
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private BlendMode mTrackBlendMode;
    @UnsupportedAppUsage
    private Drawable mTrackDrawable;
    private ColorStateList mTrackTintList;
    private boolean mUseFallbackLineSpacing;
    private VelocityTracker mVelocityTracker;
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final FloatProperty<Switch> THUMB_POS = new FloatProperty<Switch>("thumbPos") { // from class: android.widget.Switch.1
        @Override // android.util.Property
        public Float get(Switch object) {
            return Float.valueOf(object.mThumbPosition);
        }

        @Override // android.util.FloatProperty
        public void setValue(Switch object, float value) {
            object.setThumbPosition(value);
        }
    };

    /* loaded from: classes3.dex */
    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<Switch> {
        private boolean mPropertiesMapped = false;
        private int mShowTextId;
        private int mSplitTrackId;
        private int mSwitchMinWidthId;
        private int mSwitchPaddingId;
        private int mTextOffId;
        private int mTextOnId;
        private int mThumbId;
        private int mThumbTextPaddingId;
        private int mThumbTintBlendModeId;
        private int mThumbTintId;
        private int mThumbTintModeId;
        private int mTrackId;
        private int mTrackTintBlendModeId;
        private int mTrackTintId;
        private int mTrackTintModeId;

        @Override // android.view.inspector.InspectionCompanion
        public void mapProperties(PropertyMapper propertyMapper) {
            this.mShowTextId = propertyMapper.mapBoolean("showText", 16843949);
            this.mSplitTrackId = propertyMapper.mapBoolean("splitTrack", 16843852);
            this.mSwitchMinWidthId = propertyMapper.mapInt("switchMinWidth", 16843632);
            this.mSwitchPaddingId = propertyMapper.mapInt("switchPadding", 16843633);
            this.mTextOffId = propertyMapper.mapObject("textOff", 16843045);
            this.mTextOnId = propertyMapper.mapObject("textOn", 16843044);
            this.mThumbId = propertyMapper.mapObject("thumb", 16843074);
            this.mThumbTextPaddingId = propertyMapper.mapInt("thumbTextPadding", 16843634);
            this.mThumbTintId = propertyMapper.mapObject("thumbTint", 16843889);
            this.mThumbTintBlendModeId = propertyMapper.mapObject("thumbTintBlendMode", 10);
            this.mThumbTintModeId = propertyMapper.mapObject("thumbTintMode", 16843890);
            this.mTrackId = propertyMapper.mapObject(MediaStore.Audio.AudioColumns.TRACK, 16843631);
            this.mTrackTintId = propertyMapper.mapObject("trackTint", 16843993);
            this.mTrackTintBlendModeId = propertyMapper.mapObject("trackTintBlendMode", 13);
            this.mTrackTintModeId = propertyMapper.mapObject("trackTintMode", 16843994);
            this.mPropertiesMapped = true;
        }

        @Override // android.view.inspector.InspectionCompanion
        public void readProperties(Switch node, PropertyReader propertyReader) {
            if (!this.mPropertiesMapped) {
                throw new InspectionCompanion.UninitializedPropertyMapException();
            }
            propertyReader.readBoolean(this.mShowTextId, node.getShowText());
            propertyReader.readBoolean(this.mSplitTrackId, node.getSplitTrack());
            propertyReader.readInt(this.mSwitchMinWidthId, node.getSwitchMinWidth());
            propertyReader.readInt(this.mSwitchPaddingId, node.getSwitchPadding());
            propertyReader.readObject(this.mTextOffId, node.getTextOff());
            propertyReader.readObject(this.mTextOnId, node.getTextOn());
            propertyReader.readObject(this.mThumbId, node.getThumbDrawable());
            propertyReader.readInt(this.mThumbTextPaddingId, node.getThumbTextPadding());
            propertyReader.readObject(this.mThumbTintId, node.getThumbTintList());
            propertyReader.readObject(this.mThumbTintBlendModeId, node.getThumbTintBlendMode());
            propertyReader.readObject(this.mThumbTintModeId, node.getThumbTintMode());
            propertyReader.readObject(this.mTrackId, node.getTrackDrawable());
            propertyReader.readObject(this.mTrackTintId, node.getTrackTintList());
            propertyReader.readObject(this.mTrackTintBlendModeId, node.getTrackTintBlendMode());
            propertyReader.readObject(this.mTrackTintModeId, node.getTrackTintMode());
        }
    }

    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, 16843839);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mThumbTintList = null;
        this.mThumbBlendMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTrackTintList = null;
        this.mTrackBlendMode = null;
        this.mHasTrackTint = false;
        this.mHasTrackTintMode = false;
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mTempRect = new Rect();
        this.mTextPaint = new TextPaint(1);
        Resources res = getResources();
        this.mTextPaint.density = res.getDisplayMetrics().density;
        this.mTextPaint.setCompatibilityScaling(res.getCompatibilityInfo().applicationScale);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Switch, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.Switch, attrs, a, defStyleAttr, defStyleRes);
        this.mThumbDrawable = a.getDrawable(2);
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        this.mTrackDrawable = a.getDrawable(4);
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            drawable2.setCallback(this);
        }
        this.mTextOn = a.getText(0);
        this.mTextOff = a.getText(1);
        this.mShowText = a.getBoolean(11, true);
        this.mThumbTextPadding = a.getDimensionPixelSize(7, 0);
        this.mSwitchMinWidth = a.getDimensionPixelSize(5, 0);
        this.mSwitchPadding = a.getDimensionPixelSize(6, 0);
        this.mSplitTrack = a.getBoolean(8, false);
        this.mUseFallbackLineSpacing = context.getApplicationInfo().targetSdkVersion >= 28;
        ColorStateList thumbTintList = a.getColorStateList(9);
        if (thumbTintList != null) {
            this.mThumbTintList = thumbTintList;
            this.mHasThumbTint = true;
        }
        BlendMode thumbTintMode = Drawable.parseBlendMode(a.getInt(10, -1), null);
        if (this.mThumbBlendMode != thumbTintMode) {
            this.mThumbBlendMode = thumbTintMode;
            this.mHasThumbTintMode = true;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            applyThumbTint();
        }
        ColorStateList trackTintList = a.getColorStateList(12);
        if (trackTintList != null) {
            this.mTrackTintList = trackTintList;
            this.mHasTrackTint = true;
        }
        BlendMode trackTintMode = Drawable.parseBlendMode(a.getInt(13, -1), null);
        if (this.mTrackBlendMode != trackTintMode) {
            this.mTrackBlendMode = trackTintMode;
            this.mHasTrackTintMode = true;
        }
        if (this.mHasTrackTint || this.mHasTrackTintMode) {
            applyTrackTint();
        }
        int appearance = a.getResourceId(3, 0);
        if (appearance != 0) {
            setSwitchTextAppearance(context, appearance);
        }
        a.recycle();
        ViewConfiguration config = ViewConfiguration.get(context);
        this.mTouchSlop = config.getScaledTouchSlop();
        this.mMinFlingVelocity = config.getScaledMinimumFlingVelocity();
        refreshDrawableState();
        setChecked(isChecked());
    }

    public void setSwitchTextAppearance(Context context, int resid) {
        TypedArray appearance = context.obtainStyledAttributes(resid, R.styleable.TextAppearance);
        ColorStateList colors = appearance.getColorStateList(3);
        if (colors != null) {
            this.mTextColors = colors;
        } else {
            this.mTextColors = getTextColors();
        }
        int ts = appearance.getDimensionPixelSize(0, 0);
        if (ts != 0 && ts != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize(ts);
            requestLayout();
        }
        int typefaceIndex = appearance.getInt(1, -1);
        int styleIndex = appearance.getInt(2, -1);
        setSwitchTypefaceByIndex(typefaceIndex, styleIndex);
        boolean allCaps = appearance.getBoolean(11, false);
        if (allCaps) {
            this.mSwitchTransformationMethod = new AllCapsTransformationMethod(getContext());
            this.mSwitchTransformationMethod.setLengthChangesAllowed(true);
        } else {
            this.mSwitchTransformationMethod = null;
        }
        appearance.recycle();
    }

    private void setSwitchTypefaceByIndex(int typefaceIndex, int styleIndex) {
        Typeface tf = null;
        if (typefaceIndex == 1) {
            tf = Typeface.SANS_SERIF;
        } else if (typefaceIndex == 2) {
            tf = Typeface.SERIF;
        } else if (typefaceIndex == 3) {
            tf = Typeface.MONOSPACE;
        }
        setSwitchTypeface(tf, styleIndex);
    }

    public void setSwitchTypeface(Typeface tf, int style) {
        Typeface tf2;
        if (style > 0) {
            if (tf == null) {
                tf2 = Typeface.defaultFromStyle(style);
            } else {
                tf2 = Typeface.create(tf, style);
            }
            setSwitchTypeface(tf2);
            int typefaceStyle = tf2 != null ? tf2.getStyle() : 0;
            int need = (~typefaceStyle) & style;
            this.mTextPaint.setFakeBoldText((need & 1) != 0);
            this.mTextPaint.setTextSkewX((need & 2) != 0 ? -0.25f : 0.0f);
            return;
        }
        this.mTextPaint.setFakeBoldText(false);
        this.mTextPaint.setTextSkewX(0.0f);
        setSwitchTypeface(tf);
    }

    public void setSwitchTypeface(Typeface tf) {
        if (this.mTextPaint.getTypeface() != tf) {
            this.mTextPaint.setTypeface(tf);
            requestLayout();
            invalidate();
        }
    }

    public void setSwitchPadding(int pixels) {
        this.mSwitchPadding = pixels;
        requestLayout();
    }

    public int getSwitchPadding() {
        return this.mSwitchPadding;
    }

    public void setSwitchMinWidth(int pixels) {
        this.mSwitchMinWidth = pixels;
        requestLayout();
    }

    public int getSwitchMinWidth() {
        return this.mSwitchMinWidth;
    }

    public void setThumbTextPadding(int pixels) {
        this.mThumbTextPadding = pixels;
        requestLayout();
    }

    public int getThumbTextPadding() {
        return this.mThumbTextPadding;
    }

    public void setTrackDrawable(Drawable track) {
        Drawable drawable = this.mTrackDrawable;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        this.mTrackDrawable = track;
        if (track != null) {
            track.setCallback(this);
        }
        requestLayout();
    }

    public void setTrackResource(int resId) {
        setTrackDrawable(getContext().getDrawable(resId));
    }

    public Drawable getTrackDrawable() {
        return this.mTrackDrawable;
    }

    public void setTrackTintList(ColorStateList tint) {
        this.mTrackTintList = tint;
        this.mHasTrackTint = true;
        applyTrackTint();
    }

    public ColorStateList getTrackTintList() {
        return this.mTrackTintList;
    }

    public void setTrackTintMode(PorterDuff.Mode tintMode) {
        setTrackTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setTrackTintBlendMode(BlendMode blendMode) {
        this.mTrackBlendMode = blendMode;
        this.mHasTrackTintMode = true;
        applyTrackTint();
    }

    public PorterDuff.Mode getTrackTintMode() {
        BlendMode mode = getTrackTintBlendMode();
        if (mode != null) {
            return BlendMode.blendModeToPorterDuffMode(mode);
        }
        return null;
    }

    public BlendMode getTrackTintBlendMode() {
        return this.mTrackBlendMode;
    }

    private void applyTrackTint() {
        if (this.mTrackDrawable != null) {
            if (this.mHasTrackTint || this.mHasTrackTintMode) {
                this.mTrackDrawable = this.mTrackDrawable.mutate();
                if (this.mHasTrackTint) {
                    this.mTrackDrawable.setTintList(this.mTrackTintList);
                }
                if (this.mHasTrackTintMode) {
                    this.mTrackDrawable.setTintBlendMode(this.mTrackBlendMode);
                }
                if (this.mTrackDrawable.isStateful()) {
                    this.mTrackDrawable.setState(getDrawableState());
                }
            }
        }
    }

    public void setThumbDrawable(Drawable thumb) {
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        this.mThumbDrawable = thumb;
        if (thumb != null) {
            thumb.setCallback(this);
        }
        requestLayout();
    }

    public void setThumbResource(int resId) {
        setThumbDrawable(getContext().getDrawable(resId));
    }

    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }

    public void setThumbTintList(ColorStateList tint) {
        this.mThumbTintList = tint;
        this.mHasThumbTint = true;
        applyThumbTint();
    }

    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }

    public void setThumbTintMode(PorterDuff.Mode tintMode) {
        setThumbTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setThumbTintBlendMode(BlendMode blendMode) {
        this.mThumbBlendMode = blendMode;
        this.mHasThumbTintMode = true;
        applyThumbTint();
    }

    public PorterDuff.Mode getThumbTintMode() {
        BlendMode mode = getThumbTintBlendMode();
        if (mode != null) {
            return BlendMode.blendModeToPorterDuffMode(mode);
        }
        return null;
    }

    public BlendMode getThumbTintBlendMode() {
        return this.mThumbBlendMode;
    }

    private void applyThumbTint() {
        if (this.mThumbDrawable != null) {
            if (this.mHasThumbTint || this.mHasThumbTintMode) {
                this.mThumbDrawable = this.mThumbDrawable.mutate();
                if (this.mHasThumbTint) {
                    this.mThumbDrawable.setTintList(this.mThumbTintList);
                }
                if (this.mHasThumbTintMode) {
                    this.mThumbDrawable.setTintBlendMode(this.mThumbBlendMode);
                }
                if (this.mThumbDrawable.isStateful()) {
                    this.mThumbDrawable.setState(getDrawableState());
                }
            }
        }
    }

    public void setSplitTrack(boolean splitTrack) {
        this.mSplitTrack = splitTrack;
        invalidate();
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public CharSequence getTextOn() {
        return this.mTextOn;
    }

    public void setTextOn(CharSequence textOn) {
        this.mTextOn = textOn;
        requestLayout();
    }

    public CharSequence getTextOff() {
        return this.mTextOff;
    }

    public void setTextOff(CharSequence textOff) {
        this.mTextOff = textOff;
        requestLayout();
    }

    public void setShowText(boolean showText) {
        if (this.mShowText != showText) {
            this.mShowText = showText;
            requestLayout();
        }
    }

    public boolean getShowText() {
        return this.mShowText;
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int thumbWidth;
        int thumbHeight;
        int maxTextWidth;
        int trackHeight;
        if (this.mShowText) {
            if (this.mOnLayout == null) {
                this.mOnLayout = makeLayout(this.mTextOn);
            }
            if (this.mOffLayout == null) {
                this.mOffLayout = makeLayout(this.mTextOff);
            }
        }
        Rect padding = this.mTempRect;
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.getPadding(padding);
            thumbWidth = (this.mThumbDrawable.getIntrinsicWidth() - padding.left) - padding.right;
            thumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        } else {
            thumbWidth = 0;
            thumbHeight = 0;
        }
        if (this.mShowText) {
            maxTextWidth = Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + (this.mThumbTextPadding * 2);
        } else {
            maxTextWidth = 0;
        }
        this.mThumbWidth = Math.max(maxTextWidth, thumbWidth);
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            drawable2.getPadding(padding);
            trackHeight = this.mTrackDrawable.getIntrinsicHeight();
        } else {
            padding.setEmpty();
            trackHeight = 0;
        }
        int paddingLeft = padding.left;
        int paddingRight = padding.right;
        Drawable drawable3 = this.mThumbDrawable;
        if (drawable3 != null) {
            Insets inset = drawable3.getOpticalInsets();
            paddingLeft = Math.max(paddingLeft, inset.left);
            paddingRight = Math.max(paddingRight, inset.right);
        }
        int switchWidth = Math.max(this.mSwitchMinWidth, (this.mThumbWidth * 2) + paddingLeft + paddingRight);
        int switchHeight = Math.max(trackHeight, thumbHeight);
        this.mSwitchWidth = switchWidth;
        this.mSwitchHeight = switchHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();
        if (measuredHeight < switchHeight) {
            setMeasuredDimension(getMeasuredWidthAndState(), switchHeight);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        CharSequence text = isChecked() ? this.mTextOn : this.mTextOff;
        if (text != null) {
            event.getText().add(text);
        }
    }

    private Layout makeLayout(CharSequence text) {
        CharSequence transformed;
        TransformationMethod2 transformationMethod2 = this.mSwitchTransformationMethod;
        if (transformationMethod2 != null) {
            transformed = transformationMethod2.getTransformation(text, this);
        } else {
            transformed = text;
        }
        int width = (int) Math.ceil(Layout.getDesiredWidth(transformed, 0, transformed.length(), this.mTextPaint, getTextDirectionHeuristic()));
        return StaticLayout.Builder.obtain(transformed, 0, transformed.length(), this.mTextPaint, width).setUseLineSpacingFromFallbacks(this.mUseFallbackLineSpacing).build();
    }

    private boolean hitThumb(float x, float y) {
        if (this.mThumbDrawable == null) {
            return false;
        }
        int thumbOffset = getThumbOffset();
        this.mThumbDrawable.getPadding(this.mTempRect);
        int i = this.mSwitchTop;
        int i2 = this.mTouchSlop;
        int thumbTop = i - i2;
        int thumbLeft = (this.mSwitchLeft + thumbOffset) - i2;
        int i3 = this.mThumbWidth + thumbLeft + this.mTempRect.left + this.mTempRect.right;
        int i4 = this.mTouchSlop;
        int thumbRight = i3 + i4;
        int thumbBottom = this.mSwitchBottom + i4;
        return x > ((float) thumbLeft) && x < ((float) thumbRight) && y > ((float) thumbTop) && y < ((float) thumbBottom);
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0012, code lost:
        if (r0 != 3) goto L8;
     */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r10) {
        /*
            r9 = this;
            android.view.VelocityTracker r0 = r9.mVelocityTracker
            r0.addMovement(r10)
            int r0 = r10.getActionMasked()
            r1 = 1
            if (r0 == 0) goto La0
            r2 = 2
            if (r0 == r1) goto L8c
            if (r0 == r2) goto L16
            r3 = 3
            if (r0 == r3) goto L8c
            goto Lba
        L16:
            int r3 = r9.mTouchMode
            if (r3 == 0) goto L8a
            if (r3 == r1) goto L56
            if (r3 == r2) goto L1f
            goto L8b
        L1f:
            float r2 = r10.getX()
            int r3 = r9.getThumbScrollRange()
            float r4 = r9.mTouchX
            float r4 = r2 - r4
            r5 = 1065353216(0x3f800000, float:1.0)
            r6 = 0
            if (r3 == 0) goto L34
            float r7 = (float) r3
            float r7 = r4 / r7
            goto L3c
        L34:
            int r7 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r7 <= 0) goto L3a
            r7 = r5
            goto L3c
        L3a:
            r7 = -1082130432(0xffffffffbf800000, float:-1.0)
        L3c:
            boolean r8 = r9.isLayoutRtl()
            if (r8 == 0) goto L43
            float r7 = -r7
        L43:
            float r8 = r9.mThumbPosition
            float r8 = r8 + r7
            float r5 = android.util.MathUtils.constrain(r8, r6, r5)
            float r6 = r9.mThumbPosition
            int r6 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r6 == 0) goto L55
            r9.mTouchX = r2
            r9.setThumbPosition(r5)
        L55:
            return r1
        L56:
            float r3 = r10.getX()
            float r4 = r10.getY()
            float r5 = r9.mTouchX
            float r5 = r3 - r5
            float r5 = java.lang.Math.abs(r5)
            int r6 = r9.mTouchSlop
            float r6 = (float) r6
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 > 0) goto L7c
            float r5 = r9.mTouchY
            float r5 = r4 - r5
            float r5 = java.lang.Math.abs(r5)
            int r6 = r9.mTouchSlop
            float r6 = (float) r6
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 <= 0) goto L8b
        L7c:
            r9.mTouchMode = r2
            android.view.ViewParent r2 = r9.getParent()
            r2.requestDisallowInterceptTouchEvent(r1)
            r9.mTouchX = r3
            r9.mTouchY = r4
            return r1
        L8a:
        L8b:
            goto Lba
        L8c:
            int r3 = r9.mTouchMode
            if (r3 != r2) goto L97
            r9.stopDrag(r10)
            super.onTouchEvent(r10)
            return r1
        L97:
            r1 = 0
            r9.mTouchMode = r1
            android.view.VelocityTracker r1 = r9.mVelocityTracker
            r1.clear()
            goto Lba
        La0:
            float r2 = r10.getX()
            float r3 = r10.getY()
            boolean r4 = r9.isEnabled()
            if (r4 == 0) goto Lba
            boolean r4 = r9.hitThumb(r2, r3)
            if (r4 == 0) goto Lba
            r9.mTouchMode = r1
            r9.mTouchX = r2
            r9.mTouchY = r3
        Lba:
            boolean r1 = super.onTouchEvent(r10)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.Switch.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private void cancelSuperTouch(MotionEvent ev) {
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(3);
        super.onTouchEvent(cancel);
        cancel.recycle();
    }

    private void stopDrag(MotionEvent ev) {
        this.mTouchMode = 0;
        boolean newState = true;
        boolean commitChange = ev.getAction() == 1 && isEnabled();
        boolean oldState = isChecked();
        if (commitChange) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float xvel = this.mVelocityTracker.getXVelocity();
            if (Math.abs(xvel) > this.mMinFlingVelocity) {
                if (!isLayoutRtl() ? xvel <= 0.0f : xvel >= 0.0f) {
                    newState = false;
                }
            } else {
                newState = getTargetCheckedState();
            }
        } else {
            newState = oldState;
        }
        if (newState != oldState) {
            playSoundEffect(0);
        }
        setChecked(newState);
        cancelSuperTouch(ev);
    }

    private void animateThumbToCheckedState(boolean newCheckedState) {
        float targetPosition = newCheckedState ? 1.0f : 0.0f;
        this.mPositionAnimator = ObjectAnimator.ofFloat(this, THUMB_POS, targetPosition);
        this.mPositionAnimator.setDuration(250L);
        this.mPositionAnimator.setAutoCancel(true);
        this.mPositionAnimator.start();
    }

    @UnsupportedAppUsage
    private void cancelPositionAnimator() {
        ObjectAnimator objectAnimator = this.mPositionAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    private boolean getTargetCheckedState() {
        return this.mThumbPosition > 0.5f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @UnsupportedAppUsage
    public void setThumbPosition(float position) {
        this.mThumbPosition = position;
        invalidate();
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        boolean checked2 = isChecked();
        if (isAttachedToWindow() && isLaidOut()) {
            animateThumbToCheckedState(checked2);
            return;
        }
        cancelPositionAnimator();
        setThumbPosition(checked2 ? 1.0f : 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int switchRight;
        int switchLeft;
        int switchTop;
        int switchBottom;
        super.onLayout(changed, left, top, right, bottom);
        int opticalInsetLeft = 0;
        int opticalInsetRight = 0;
        if (this.mThumbDrawable != null) {
            Rect trackPadding = this.mTempRect;
            Drawable drawable = this.mTrackDrawable;
            if (drawable != null) {
                drawable.getPadding(trackPadding);
            } else {
                trackPadding.setEmpty();
            }
            Insets insets = this.mThumbDrawable.getOpticalInsets();
            opticalInsetLeft = Math.max(0, insets.left - trackPadding.left);
            opticalInsetRight = Math.max(0, insets.right - trackPadding.right);
        }
        if (isLayoutRtl()) {
            switchLeft = getPaddingLeft() + opticalInsetLeft;
            switchRight = ((this.mSwitchWidth + switchLeft) - opticalInsetLeft) - opticalInsetRight;
        } else {
            int switchLeft2 = getWidth();
            switchRight = (switchLeft2 - getPaddingRight()) - opticalInsetRight;
            switchLeft = (switchRight - this.mSwitchWidth) + opticalInsetLeft + opticalInsetRight;
        }
        int gravity = getGravity() & 112;
        if (gravity == 16) {
            int i = this.mSwitchHeight;
            switchTop = (((getPaddingTop() + getHeight()) - getPaddingBottom()) / 2) - (i / 2);
            switchBottom = i + switchTop;
        } else if (gravity != 80) {
            switchTop = getPaddingTop();
            switchBottom = this.mSwitchHeight + switchTop;
        } else {
            switchBottom = getHeight() - getPaddingBottom();
            switchTop = switchBottom - this.mSwitchHeight;
        }
        this.mSwitchLeft = switchLeft;
        this.mSwitchTop = switchTop;
        this.mSwitchBottom = switchBottom;
        this.mSwitchRight = switchRight;
    }

    @Override // android.view.View
    public void draw(Canvas c) {
        Insets thumbInsets;
        Rect padding = this.mTempRect;
        int switchLeft = this.mSwitchLeft;
        int switchTop = this.mSwitchTop;
        int switchRight = this.mSwitchRight;
        int switchBottom = this.mSwitchBottom;
        int thumbInitialLeft = getThumbOffset() + switchLeft;
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            thumbInsets = drawable.getOpticalInsets();
        } else {
            thumbInsets = Insets.NONE;
        }
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            drawable2.getPadding(padding);
            thumbInitialLeft += padding.left;
            int trackLeft = switchLeft;
            int trackTop = switchTop;
            int trackRight = switchRight;
            int trackBottom = switchBottom;
            if (thumbInsets != Insets.NONE) {
                if (thumbInsets.left > padding.left) {
                    trackLeft += thumbInsets.left - padding.left;
                }
                if (thumbInsets.top > padding.top) {
                    trackTop += thumbInsets.top - padding.top;
                }
                if (thumbInsets.right > padding.right) {
                    trackRight -= thumbInsets.right - padding.right;
                }
                if (thumbInsets.bottom > padding.bottom) {
                    trackBottom -= thumbInsets.bottom - padding.bottom;
                }
            }
            this.mTrackDrawable.setBounds(trackLeft, trackTop, trackRight, trackBottom);
        }
        Drawable drawable3 = this.mThumbDrawable;
        if (drawable3 != null) {
            drawable3.getPadding(padding);
            int thumbLeft = thumbInitialLeft - padding.left;
            int thumbRight = this.mThumbWidth + thumbInitialLeft + padding.right;
            this.mThumbDrawable.setBounds(thumbLeft, switchTop, thumbRight, switchBottom);
            Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(thumbLeft, switchTop, thumbRight, switchBottom);
            }
        }
        super.draw(c);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        int cX;
        super.onDraw(canvas);
        Rect padding = this.mTempRect;
        Drawable trackDrawable = this.mTrackDrawable;
        if (trackDrawable != null) {
            trackDrawable.getPadding(padding);
        } else {
            padding.setEmpty();
        }
        int switchTop = this.mSwitchTop;
        int switchBottom = this.mSwitchBottom;
        int switchInnerTop = padding.top + switchTop;
        int switchInnerBottom = switchBottom - padding.bottom;
        Drawable thumbDrawable = this.mThumbDrawable;
        if (trackDrawable != null) {
            if (this.mSplitTrack && thumbDrawable != null) {
                Insets insets = thumbDrawable.getOpticalInsets();
                thumbDrawable.copyBounds(padding);
                padding.left += insets.left;
                padding.right -= insets.right;
                int saveCount = canvas.save();
                canvas.clipRect(padding, Region.Op.DIFFERENCE);
                trackDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            } else {
                trackDrawable.draw(canvas);
            }
        }
        int saveCount2 = canvas.save();
        if (thumbDrawable != null) {
            thumbDrawable.draw(canvas);
        }
        Layout switchText = getTargetCheckedState() ? this.mOnLayout : this.mOffLayout;
        if (switchText != null) {
            int[] drawableState = getDrawableState();
            ColorStateList colorStateList = this.mTextColors;
            if (colorStateList != null) {
                this.mTextPaint.setColor(colorStateList.getColorForState(drawableState, 0));
            }
            this.mTextPaint.drawableState = drawableState;
            if (thumbDrawable != null) {
                Rect bounds = thumbDrawable.getBounds();
                cX = bounds.left + bounds.right;
            } else {
                cX = getWidth();
            }
            int left = (cX / 2) - (switchText.getWidth() / 2);
            int top = ((switchInnerTop + switchInnerBottom) / 2) - (switchText.getHeight() / 2);
            canvas.translate(left, top);
            switchText.draw(canvas);
        }
        canvas.restoreToCount(saveCount2);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingLeft() {
        if (!isLayoutRtl()) {
            return super.getCompoundPaddingLeft();
        }
        int padding = super.getCompoundPaddingLeft() + this.mSwitchWidth;
        if (!TextUtils.isEmpty(getText())) {
            return padding + this.mSwitchPadding;
        }
        return padding;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingRight() {
        if (isLayoutRtl()) {
            return super.getCompoundPaddingRight();
        }
        int padding = super.getCompoundPaddingRight() + this.mSwitchWidth;
        if (!TextUtils.isEmpty(getText())) {
            return padding + this.mSwitchPadding;
        }
        return padding;
    }

    private int getThumbOffset() {
        float thumbPosition;
        if (isLayoutRtl()) {
            thumbPosition = 1.0f - this.mThumbPosition;
        } else {
            thumbPosition = this.mThumbPosition;
        }
        return (int) ((getThumbScrollRange() * thumbPosition) + 0.5f);
    }

    private int getThumbScrollRange() {
        Insets insets;
        Drawable drawable = this.mTrackDrawable;
        if (drawable != null) {
            Rect padding = this.mTempRect;
            drawable.getPadding(padding);
            Drawable drawable2 = this.mThumbDrawable;
            if (drawable2 != null) {
                insets = drawable2.getOpticalInsets();
            } else {
                insets = Insets.NONE;
            }
            return ((((this.mSwitchWidth - this.mThumbWidth) - padding.left) - padding.right) - insets.left) - insets.right;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] state = getDrawableState();
        boolean changed = false;
        Drawable thumbDrawable = this.mThumbDrawable;
        if (thumbDrawable != null && thumbDrawable.isStateful()) {
            changed = false | thumbDrawable.setState(state);
        }
        Drawable trackDrawable = this.mTrackDrawable;
        if (trackDrawable != null && trackDrawable.isStateful()) {
            changed |= trackDrawable.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            drawable2.setHotspot(x, y);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mThumbDrawable || who == this.mTrackDrawable;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        ObjectAnimator objectAnimator = this.mPositionAnimator;
        if (objectAnimator != null && objectAnimator.isStarted()) {
            this.mPositionAnimator.end();
            this.mPositionAnimator = null;
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Button, android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return Switch.class.getName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onProvideStructure(ViewStructure structure, int viewFor, int flags) {
        CharSequence switchText = isChecked() ? this.mTextOn : this.mTextOff;
        if (!TextUtils.isEmpty(switchText)) {
            CharSequence oldText = structure.getText();
            if (TextUtils.isEmpty(oldText)) {
                structure.setText(switchText);
                return;
            }
            StringBuilder newText = new StringBuilder();
            newText.append(oldText);
            newText.append(' ');
            newText.append(switchText);
            structure.setText(newText);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        CharSequence switchText = isChecked() ? this.mTextOn : this.mTextOff;
        if (!TextUtils.isEmpty(switchText)) {
            CharSequence oldText = info.getText();
            if (TextUtils.isEmpty(oldText)) {
                info.setText(switchText);
                return;
            }
            StringBuilder newText = new StringBuilder();
            newText.append(oldText);
            newText.append(' ');
            newText.append(switchText);
            info.setText(newText);
        }
    }
}
