package android.view;

import android.app.WindowConfiguration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes2.dex */
public class RemoteAnimationTarget implements Parcelable {
    public static final Parcelable.Creator<RemoteAnimationTarget> CREATOR = new Parcelable.Creator<RemoteAnimationTarget>() { // from class: android.view.RemoteAnimationTarget.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteAnimationTarget createFromParcel(Parcel in) {
            return new RemoteAnimationTarget(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RemoteAnimationTarget[] newArray(int size) {
            return new RemoteAnimationTarget[size];
        }
    };
    public static final int MODE_CLOSING = 1;
    public static final int MODE_OPENING = 0;
    private protected final Rect clipRect;
    private protected final Rect contentInsets;
    private protected boolean isNotInRecents;
    private protected final boolean isTranslucent;
    private protected final SurfaceControl leash;
    private protected final int mode;
    private protected final Point position;
    private protected final int prefixOrderIndex;
    private protected final Rect sourceContainerBounds;
    private protected final int taskId;
    private protected final WindowConfiguration windowConfiguration;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Mode {
    }

    public synchronized RemoteAnimationTarget(int taskId, int mode, SurfaceControl leash, boolean isTranslucent, Rect clipRect, Rect contentInsets, int prefixOrderIndex, Point position, Rect sourceContainerBounds, WindowConfiguration windowConfig, boolean isNotInRecents) {
        this.mode = mode;
        this.taskId = taskId;
        this.leash = leash;
        this.isTranslucent = isTranslucent;
        this.clipRect = new Rect(clipRect);
        this.contentInsets = new Rect(contentInsets);
        this.prefixOrderIndex = prefixOrderIndex;
        this.position = new Point(position);
        this.sourceContainerBounds = new Rect(sourceContainerBounds);
        this.windowConfiguration = windowConfig;
        this.isNotInRecents = isNotInRecents;
    }

    public synchronized RemoteAnimationTarget(Parcel in) {
        this.taskId = in.readInt();
        this.mode = in.readInt();
        this.leash = (SurfaceControl) in.readParcelable(null);
        this.isTranslucent = in.readBoolean();
        this.clipRect = (Rect) in.readParcelable(null);
        this.contentInsets = (Rect) in.readParcelable(null);
        this.prefixOrderIndex = in.readInt();
        this.position = (Point) in.readParcelable(null);
        this.sourceContainerBounds = (Rect) in.readParcelable(null);
        this.windowConfiguration = (WindowConfiguration) in.readParcelable(null);
        this.isNotInRecents = in.readBoolean();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.taskId);
        dest.writeInt(this.mode);
        dest.writeParcelable(this.leash, 0);
        dest.writeBoolean(this.isTranslucent);
        dest.writeParcelable(this.clipRect, 0);
        dest.writeParcelable(this.contentInsets, 0);
        dest.writeInt(this.prefixOrderIndex);
        dest.writeParcelable(this.position, 0);
        dest.writeParcelable(this.sourceContainerBounds, 0);
        dest.writeParcelable(this.windowConfiguration, 0);
        dest.writeBoolean(this.isNotInRecents);
    }

    public synchronized void dump(PrintWriter pw, String prefix) {
        pw.print(prefix);
        pw.print("mode=");
        pw.print(this.mode);
        pw.print(" taskId=");
        pw.print(this.taskId);
        pw.print(" isTranslucent=");
        pw.print(this.isTranslucent);
        pw.print(" clipRect=");
        this.clipRect.printShortString(pw);
        pw.print(" contentInsets=");
        this.contentInsets.printShortString(pw);
        pw.print(" prefixOrderIndex=");
        pw.print(this.prefixOrderIndex);
        pw.print(" position=");
        this.position.printShortString(pw);
        pw.print(" sourceContainerBounds=");
        this.sourceContainerBounds.printShortString(pw);
        pw.println();
        pw.print(prefix);
        pw.print("windowConfiguration=");
        pw.println(this.windowConfiguration);
        pw.print(prefix);
        pw.print("leash=");
        pw.println(this.leash);
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.taskId);
        proto.write(1120986464258L, this.mode);
        this.leash.writeToProto(proto, 1146756268035L);
        proto.write(1133871366148L, this.isTranslucent);
        this.clipRect.writeToProto(proto, 1146756268037L);
        this.contentInsets.writeToProto(proto, 1146756268038L);
        proto.write(1120986464263L, this.prefixOrderIndex);
        this.position.writeToProto(proto, 1146756268040L);
        this.sourceContainerBounds.writeToProto(proto, 1146756268041L);
        this.windowConfiguration.writeToProto(proto, 1146756268042L);
        proto.end(token);
    }
}
