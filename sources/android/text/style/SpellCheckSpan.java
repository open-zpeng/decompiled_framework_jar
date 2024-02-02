package android.text.style;

import android.os.Parcel;
import android.text.ParcelableSpan;
/* loaded from: classes2.dex */
public class SpellCheckSpan implements ParcelableSpan {
    private boolean mSpellCheckInProgress;

    /* JADX INFO: Access modifiers changed from: private */
    public SpellCheckSpan() {
        this.mSpellCheckInProgress = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SpellCheckSpan(Parcel src) {
        this.mSpellCheckInProgress = src.readInt() != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSpellCheckInProgress(boolean inProgress) {
        this.mSpellCheckInProgress = inProgress;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSpellCheckInProgress() {
        return this.mSpellCheckInProgress;
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
        dest.writeInt(this.mSpellCheckInProgress ? 1 : 0);
    }

    @Override // android.text.ParcelableSpan
    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    @Override // android.text.ParcelableSpan
    public synchronized int getSpanTypeIdInternal() {
        return 20;
    }
}
