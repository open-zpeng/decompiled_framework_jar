package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
/* loaded from: classes2.dex */
public class SuggestionRangeSpan extends CharacterStyle implements ParcelableSpan {
    private int mBackgroundColor;

    /* JADX INFO: Access modifiers changed from: private */
    public SuggestionRangeSpan() {
        this.mBackgroundColor = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SuggestionRangeSpan(Parcel src) {
        this.mBackgroundColor = src.readInt();
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
        dest.writeInt(this.mBackgroundColor);
    }

    @Override // android.text.ParcelableSpan
    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    @Override // android.text.ParcelableSpan
    public synchronized int getSpanTypeIdInternal() {
        return 21;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint tp) {
        tp.bgColor = this.mBackgroundColor;
    }
}
