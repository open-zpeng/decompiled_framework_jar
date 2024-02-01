package android.view;

import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.PrintWriter;

/* loaded from: classes3.dex */
public class InsetsSource implements Parcelable {
    public static final Parcelable.Creator<InsetsSource> CREATOR = new Parcelable.Creator<InsetsSource>() { // from class: android.view.InsetsSource.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InsetsSource createFromParcel(Parcel in) {
            return new InsetsSource(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InsetsSource[] newArray(int size) {
            return new InsetsSource[size];
        }
    };
    private final Rect mFrame;
    private final Rect mTmpFrame;
    private final int mType;
    private boolean mVisible;

    public InsetsSource(int type) {
        this.mTmpFrame = new Rect();
        this.mType = type;
        this.mFrame = new Rect();
    }

    public InsetsSource(InsetsSource other) {
        this.mTmpFrame = new Rect();
        this.mType = other.mType;
        this.mFrame = new Rect(other.mFrame);
        this.mVisible = other.mVisible;
    }

    public void setFrame(Rect frame) {
        this.mFrame.set(frame);
    }

    public void setVisible(boolean visible) {
        this.mVisible = visible;
    }

    public int getType() {
        return this.mType;
    }

    public Rect getFrame() {
        return this.mFrame;
    }

    public boolean isVisible() {
        return this.mVisible;
    }

    public Insets calculateInsets(Rect relativeFrame, boolean ignoreVisibility) {
        if (!ignoreVisibility && !this.mVisible) {
            return Insets.NONE;
        }
        if (!this.mTmpFrame.setIntersect(this.mFrame, relativeFrame)) {
            return Insets.NONE;
        }
        if (this.mTmpFrame.width() == relativeFrame.width()) {
            if (this.mTmpFrame.top == relativeFrame.top) {
                return Insets.of(0, this.mTmpFrame.height(), 0, 0);
            }
            return Insets.of(0, 0, 0, this.mTmpFrame.height());
        } else if (this.mTmpFrame.height() == relativeFrame.height()) {
            if (this.mTmpFrame.left == relativeFrame.left) {
                return Insets.of(this.mTmpFrame.width(), 0, 0, 0);
            }
            return Insets.of(0, 0, this.mTmpFrame.width(), 0);
        } else {
            return Insets.NONE;
        }
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("InsetsSource type=");
        pw.print(InsetsState.typeToString(this.mType));
        pw.print(" frame=");
        pw.print(this.mFrame.toShortString());
        pw.print(" visible=");
        pw.print(this.mVisible);
        pw.println();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InsetsSource that = (InsetsSource) o;
        if (this.mType != that.mType || this.mVisible != that.mVisible) {
            return false;
        }
        return this.mFrame.equals(that.mFrame);
    }

    public int hashCode() {
        int result = this.mType;
        return (((result * 31) + this.mFrame.hashCode()) * 31) + (this.mVisible ? 1 : 0);
    }

    public InsetsSource(Parcel in) {
        this.mTmpFrame = new Rect();
        this.mType = in.readInt();
        this.mFrame = (Rect) in.readParcelable(null);
        this.mVisible = in.readBoolean();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeParcelable(this.mFrame, 0);
        dest.writeBoolean(this.mVisible);
    }
}
