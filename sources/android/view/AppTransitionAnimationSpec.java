package android.view;

import android.graphics.GraphicBuffer;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class AppTransitionAnimationSpec implements Parcelable {
    public static final Parcelable.Creator<AppTransitionAnimationSpec> CREATOR = new Parcelable.Creator<AppTransitionAnimationSpec>() { // from class: android.view.AppTransitionAnimationSpec.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppTransitionAnimationSpec createFromParcel(Parcel in) {
            return new AppTransitionAnimationSpec(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppTransitionAnimationSpec[] newArray(int size) {
            return new AppTransitionAnimationSpec[size];
        }
    };
    public final GraphicBuffer buffer;
    public final Rect rect;
    public final int taskId;

    private protected AppTransitionAnimationSpec(int taskId, GraphicBuffer buffer, Rect rect) {
        this.taskId = taskId;
        this.rect = rect;
        this.buffer = buffer;
    }

    public synchronized AppTransitionAnimationSpec(Parcel in) {
        this.taskId = in.readInt();
        this.rect = (Rect) in.readParcelable(null);
        this.buffer = (GraphicBuffer) in.readParcelable(null);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.taskId);
        dest.writeParcelable(this.rect, 0);
        dest.writeParcelable(this.buffer, 0);
    }

    public String toString() {
        return "{taskId: " + this.taskId + ", buffer: " + this.buffer + ", rect: " + this.rect + "}";
    }
}
