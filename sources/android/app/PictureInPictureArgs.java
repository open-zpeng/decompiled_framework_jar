package android.app;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Rational;
import java.util.ArrayList;
import java.util.List;
@Deprecated
/* loaded from: classes.dex */
public final class PictureInPictureArgs implements Parcelable {
    private protected static final Parcelable.Creator<PictureInPictureArgs> CREATOR = new Parcelable.Creator<PictureInPictureArgs>() { // from class: android.app.PictureInPictureArgs.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureInPictureArgs createFromParcel(Parcel in) {
            return new PictureInPictureArgs(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureInPictureArgs[] newArray(int size) {
            return new PictureInPictureArgs[size];
        }
    };
    private Rational mAspectRatio;
    private Rect mSourceRectHint;
    private Rect mSourceRectHintInsets;
    private List<RemoteAction> mUserActions;

    /* loaded from: classes.dex */
    public static class Builder {
        private Rational mAspectRatio;
        private Rect mSourceRectHint;
        private List<RemoteAction> mUserActions;

        private protected Builder() {
        }

        private protected Builder setAspectRatio(Rational aspectRatio) {
            this.mAspectRatio = aspectRatio;
            return this;
        }

        private protected Builder setActions(List<RemoteAction> actions) {
            if (this.mUserActions != null) {
                this.mUserActions = null;
            }
            if (actions != null) {
                this.mUserActions = new ArrayList(actions);
            }
            return this;
        }

        private protected Builder setSourceRectHint(Rect launchBounds) {
            if (launchBounds == null) {
                this.mSourceRectHint = null;
            } else {
                this.mSourceRectHint = new Rect(launchBounds);
            }
            return this;
        }

        private protected PictureInPictureArgs build() {
            PictureInPictureArgs args = new PictureInPictureArgs(this.mAspectRatio, this.mUserActions, this.mSourceRectHint);
            return args;
        }
    }

    @Deprecated
    private protected PictureInPictureArgs() {
    }

    @Deprecated
    public synchronized PictureInPictureArgs(float aspectRatio, List<RemoteAction> actions) {
        setAspectRatio(aspectRatio);
        setActions(actions);
    }

    private synchronized PictureInPictureArgs(Parcel in) {
        if (in.readInt() != 0) {
            this.mAspectRatio = new Rational(in.readInt(), in.readInt());
        }
        if (in.readInt() != 0) {
            this.mUserActions = new ArrayList();
            in.readParcelableList(this.mUserActions, RemoteAction.class.getClassLoader());
        }
        if (in.readInt() != 0) {
            this.mSourceRectHint = Rect.CREATOR.createFromParcel(in);
        }
    }

    private synchronized PictureInPictureArgs(Rational aspectRatio, List<RemoteAction> actions, Rect sourceRectHint) {
        this.mAspectRatio = aspectRatio;
        this.mUserActions = actions;
        this.mSourceRectHint = sourceRectHint;
    }

    @Deprecated
    private protected void setAspectRatio(float aspectRatio) {
        this.mAspectRatio = new Rational((int) (1.0E9f * aspectRatio), 1000000000);
    }

    @Deprecated
    private protected void setActions(List<RemoteAction> actions) {
        if (this.mUserActions != null) {
            this.mUserActions = null;
        }
        if (actions != null) {
            this.mUserActions = new ArrayList(actions);
        }
    }

    @Deprecated
    public synchronized void setSourceRectHint(Rect launchBounds) {
        if (launchBounds == null) {
            this.mSourceRectHint = null;
        } else {
            this.mSourceRectHint = new Rect(launchBounds);
        }
    }

    public synchronized void copyOnlySet(PictureInPictureArgs otherArgs) {
        if (otherArgs.hasSetAspectRatio()) {
            this.mAspectRatio = otherArgs.mAspectRatio;
        }
        if (otherArgs.hasSetActions()) {
            this.mUserActions = otherArgs.mUserActions;
        }
        if (otherArgs.hasSourceBoundsHint()) {
            this.mSourceRectHint = new Rect(otherArgs.getSourceRectHint());
        }
    }

    public synchronized float getAspectRatio() {
        if (this.mAspectRatio != null) {
            return this.mAspectRatio.floatValue();
        }
        return 0.0f;
    }

    public synchronized Rational getAspectRatioRational() {
        return this.mAspectRatio;
    }

    public synchronized boolean hasSetAspectRatio() {
        return this.mAspectRatio != null;
    }

    public synchronized List<RemoteAction> getActions() {
        return this.mUserActions;
    }

    public synchronized boolean hasSetActions() {
        return this.mUserActions != null;
    }

    public synchronized void truncateActions(int size) {
        if (hasSetActions()) {
            this.mUserActions = this.mUserActions.subList(0, Math.min(this.mUserActions.size(), size));
        }
    }

    @Deprecated
    public synchronized void setSourceRectHintInsets(Rect insets) {
        if (insets == null) {
            this.mSourceRectHintInsets = null;
        } else {
            this.mSourceRectHintInsets = new Rect(insets);
        }
    }

    public synchronized Rect getSourceRectHint() {
        return this.mSourceRectHint;
    }

    public synchronized Rect getSourceRectHintInsets() {
        return this.mSourceRectHintInsets;
    }

    public synchronized boolean hasSourceBoundsHint() {
        return (this.mSourceRectHint == null || this.mSourceRectHint.isEmpty()) ? false : true;
    }

    public synchronized boolean hasSourceBoundsHintInsets() {
        return this.mSourceRectHintInsets != null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (this.mAspectRatio != null) {
            out.writeInt(1);
            out.writeInt(this.mAspectRatio.getNumerator());
            out.writeInt(this.mAspectRatio.getDenominator());
        } else {
            out.writeInt(0);
        }
        if (this.mUserActions != null) {
            out.writeInt(1);
            out.writeParcelableList(this.mUserActions, 0);
        } else {
            out.writeInt(0);
        }
        if (this.mSourceRectHint != null) {
            out.writeInt(1);
            this.mSourceRectHint.writeToParcel(out, 0);
            return;
        }
        out.writeInt(0);
    }

    private protected static PictureInPictureArgs convert(PictureInPictureParams params) {
        return new PictureInPictureArgs(params.getAspectRatioRational(), params.getActions(), params.getSourceRectHint());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PictureInPictureParams convert(PictureInPictureArgs args) {
        return new PictureInPictureParams(args.getAspectRatioRational(), args.getActions(), args.getSourceRectHint());
    }
}
