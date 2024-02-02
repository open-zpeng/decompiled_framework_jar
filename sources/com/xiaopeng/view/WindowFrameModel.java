package com.xiaopeng.view;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class WindowFrameModel implements Parcelable {
    public static final Parcelable.Creator<WindowFrameModel> CREATOR = new Parcelable.Creator<WindowFrameModel>() { // from class: com.xiaopeng.view.WindowFrameModel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WindowFrameModel createFromParcel(Parcel in) {
            return new WindowFrameModel(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WindowFrameModel[] newArray(int size) {
            return new WindowFrameModel[size];
        }
    };
    public Rect applicationBounds;
    public Rect contentBounds;
    public Rect decorBounds;
    public Rect displayBounds;
    public Rect overscanBounds;
    public Rect parentBounds;
    public Rect physicalBounds;
    public Rect stableBounds;
    public Rect unrestrictedBounds;
    public Rect visibleBounds;
    public Rect windowBounds;

    public WindowFrameModel() {
        this.windowBounds = new Rect();
        this.physicalBounds = new Rect();
        this.parentBounds = new Rect();
        this.displayBounds = new Rect();
        this.contentBounds = new Rect();
        this.visibleBounds = new Rect();
        this.decorBounds = new Rect();
        this.stableBounds = new Rect();
        this.overscanBounds = new Rect();
        this.applicationBounds = new Rect();
        this.unrestrictedBounds = new Rect();
    }

    private WindowFrameModel(Parcel in) {
        this.windowBounds = new Rect();
        this.physicalBounds = new Rect();
        this.parentBounds = new Rect();
        this.displayBounds = new Rect();
        this.contentBounds = new Rect();
        this.visibleBounds = new Rect();
        this.decorBounds = new Rect();
        this.stableBounds = new Rect();
        this.overscanBounds = new Rect();
        this.applicationBounds = new Rect();
        this.unrestrictedBounds = new Rect();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.windowBounds.set((Rect) in.readParcelable(null));
        this.physicalBounds.set((Rect) in.readParcelable(null));
        this.parentBounds.set((Rect) in.readParcelable(null));
        this.displayBounds.set((Rect) in.readParcelable(null));
        this.contentBounds.set((Rect) in.readParcelable(null));
        this.visibleBounds.set((Rect) in.readParcelable(null));
        this.decorBounds.set((Rect) in.readParcelable(null));
        this.stableBounds.set((Rect) in.readParcelable(null));
        this.overscanBounds.set((Rect) in.readParcelable(null));
        this.applicationBounds.set((Rect) in.readParcelable(null));
        this.unrestrictedBounds.set((Rect) in.readParcelable(null));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.windowBounds, flags);
        dest.writeParcelable(this.physicalBounds, flags);
        dest.writeParcelable(this.parentBounds, flags);
        dest.writeParcelable(this.displayBounds, flags);
        dest.writeParcelable(this.contentBounds, flags);
        dest.writeParcelable(this.visibleBounds, flags);
        dest.writeParcelable(this.decorBounds, flags);
        dest.writeParcelable(this.stableBounds, flags);
        dest.writeParcelable(this.overscanBounds, flags);
        dest.writeParcelable(this.applicationBounds, flags);
        dest.writeParcelable(this.unrestrictedBounds, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("");
        buffer.append("WindowFrameModel:");
        buffer.append(" window=" + this.windowBounds);
        buffer.append(" physical=" + this.physicalBounds);
        buffer.append(" parent=" + this.parentBounds);
        buffer.append(" display=" + this.displayBounds);
        buffer.append(" content=" + this.contentBounds);
        buffer.append(" visible=" + this.visibleBounds);
        buffer.append(" decor=" + this.decorBounds);
        buffer.append(" stable=" + this.stableBounds);
        buffer.append(" overscan=" + this.overscanBounds);
        buffer.append(" application=" + this.applicationBounds);
        buffer.append(" unrestricted=" + this.unrestrictedBounds);
        return buffer.toString();
    }
}
