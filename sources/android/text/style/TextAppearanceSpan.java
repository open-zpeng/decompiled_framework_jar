package android.text.style;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.LeakyTypefaceStorage;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import com.android.internal.R;
/* loaded from: classes2.dex */
public class TextAppearanceSpan extends MetricAffectingSpan implements ParcelableSpan {
    private final String mFamilyName;
    private final int mStyle;
    private final ColorStateList mTextColor;
    private final ColorStateList mTextColorLink;
    private final int mTextSize;
    private final Typeface mTypeface;

    public TextAppearanceSpan(Context context, int appearance) {
        this(context, appearance, -1);
    }

    public TextAppearanceSpan(Context context, int appearance, int colorList) {
        TypedArray a = context.obtainStyledAttributes(appearance, R.styleable.TextAppearance);
        ColorStateList textColor = a.getColorStateList(3);
        this.mTextColorLink = a.getColorStateList(6);
        this.mTextSize = a.getDimensionPixelSize(0, -1);
        this.mStyle = a.getInt(2, 0);
        if (!context.isRestricted() && context.canLoadUnsafeResources()) {
            this.mTypeface = a.getFont(12);
        } else {
            this.mTypeface = null;
        }
        if (this.mTypeface != null) {
            this.mFamilyName = null;
        } else {
            String family = a.getString(12);
            if (family == null) {
                int tf = a.getInt(1, 0);
                switch (tf) {
                    case 1:
                        this.mFamilyName = "sans";
                        break;
                    case 2:
                        this.mFamilyName = "serif";
                        break;
                    case 3:
                        this.mFamilyName = "monospace";
                        break;
                    default:
                        this.mFamilyName = null;
                        break;
                }
            } else {
                this.mFamilyName = family;
            }
        }
        a.recycle();
        if (colorList >= 0) {
            TypedArray a2 = context.obtainStyledAttributes(android.R.style.Theme, R.styleable.Theme);
            textColor = a2.getColorStateList(colorList);
            a2.recycle();
        }
        this.mTextColor = textColor;
    }

    public TextAppearanceSpan(String family, int style, int size, ColorStateList color, ColorStateList linkColor) {
        this.mFamilyName = family;
        this.mStyle = style;
        this.mTextSize = size;
        this.mTextColor = color;
        this.mTextColorLink = linkColor;
        this.mTypeface = null;
    }

    public TextAppearanceSpan(Parcel src) {
        this.mFamilyName = src.readString();
        this.mStyle = src.readInt();
        this.mTextSize = src.readInt();
        if (src.readInt() != 0) {
            this.mTextColor = ColorStateList.CREATOR.createFromParcel(src);
        } else {
            this.mTextColor = null;
        }
        if (src.readInt() != 0) {
            this.mTextColorLink = ColorStateList.CREATOR.createFromParcel(src);
        } else {
            this.mTextColorLink = null;
        }
        this.mTypeface = LeakyTypefaceStorage.readTypefaceFromParcel(src);
    }

    @Override // android.text.ParcelableSpan
    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    @Override // android.text.ParcelableSpan
    public synchronized int getSpanTypeIdInternal() {
        return 17;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    @Override // android.text.ParcelableSpan
    public synchronized void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeString(this.mFamilyName);
        dest.writeInt(this.mStyle);
        dest.writeInt(this.mTextSize);
        if (this.mTextColor != null) {
            dest.writeInt(1);
            this.mTextColor.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        if (this.mTextColorLink != null) {
            dest.writeInt(1);
            this.mTextColorLink.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        LeakyTypefaceStorage.writeTypefaceToParcel(this.mTypeface, dest);
    }

    public String getFamily() {
        return this.mFamilyName;
    }

    public ColorStateList getTextColor() {
        return this.mTextColor;
    }

    public ColorStateList getLinkTextColor() {
        return this.mTextColorLink;
    }

    public int getTextSize() {
        return this.mTextSize;
    }

    public int getTextStyle() {
        return this.mStyle;
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint ds) {
        updateMeasureState(ds);
        if (this.mTextColor != null) {
            ds.setColor(this.mTextColor.getColorForState(ds.drawableState, 0));
        }
        if (this.mTextColorLink != null) {
            ds.linkColor = this.mTextColorLink.getColorForState(ds.drawableState, 0);
        }
    }

    @Override // android.text.style.MetricAffectingSpan
    public void updateMeasureState(TextPaint ds) {
        Typeface styledTypeface;
        Typeface styledTypeface2;
        int style = 0;
        if (this.mTypeface != null) {
            style = this.mStyle;
            styledTypeface2 = Typeface.create(this.mTypeface, style);
        } else if (this.mFamilyName != null || this.mStyle != 0) {
            Typeface tf = ds.getTypeface();
            if (tf != null) {
                style = tf.getStyle();
            }
            style |= this.mStyle;
            if (this.mFamilyName != null) {
                styledTypeface = Typeface.create(this.mFamilyName, style);
            } else if (tf == null) {
                styledTypeface = Typeface.defaultFromStyle(style);
            } else {
                styledTypeface = Typeface.create(tf, style);
            }
            styledTypeface2 = styledTypeface;
        } else {
            styledTypeface2 = null;
        }
        if (styledTypeface2 != null) {
            int fake = (~styledTypeface2.getStyle()) & style;
            if ((fake & 1) != 0) {
                ds.setFakeBoldText(true);
            }
            if ((fake & 2) != 0) {
                ds.setTextSkewX(-0.25f);
            }
            ds.setTypeface(styledTypeface2);
        }
        if (this.mTextSize > 0) {
            ds.setTextSize(this.mTextSize);
        }
    }
}
