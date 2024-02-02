package android.app;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Rational;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class PictureInPictureParams implements Parcelable {
    public static final Parcelable.Creator<PictureInPictureParams> CREATOR = new Parcelable.Creator<PictureInPictureParams>() { // from class: android.app.PictureInPictureParams.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureInPictureParams createFromParcel(Parcel in) {
            return new PictureInPictureParams(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureInPictureParams[] newArray(int size) {
            return new PictureInPictureParams[size];
        }
    };
    private Rational mAspectRatio;
    private Rect mSourceRectHint;
    private List<RemoteAction> mUserActions;

    /* loaded from: classes.dex */
    public static class Builder {
        private Rational mAspectRatio;
        private Rect mSourceRectHint;
        private List<RemoteAction> mUserActions;

        public Builder setAspectRatio(Rational aspectRatio) {
            this.mAspectRatio = aspectRatio;
            return this;
        }

        public Builder setActions(List<RemoteAction> actions) {
            if (this.mUserActions != null) {
                this.mUserActions = null;
            }
            if (actions != null) {
                this.mUserActions = new ArrayList(actions);
            }
            return this;
        }

        public Builder setSourceRectHint(Rect launchBounds) {
            if (launchBounds == null) {
                this.mSourceRectHint = null;
            } else {
                this.mSourceRectHint = new Rect(launchBounds);
            }
            return this;
        }

        public PictureInPictureParams build() {
            PictureInPictureParams params = new PictureInPictureParams(this.mAspectRatio, this.mUserActions, this.mSourceRectHint);
            return params;
        }
    }

    synchronized PictureInPictureParams() {
    }

    synchronized PictureInPictureParams(Parcel in) {
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized PictureInPictureParams(Rational aspectRatio, List<RemoteAction> actions, Rect sourceRectHint) {
        this.mAspectRatio = aspectRatio;
        this.mUserActions = actions;
        this.mSourceRectHint = sourceRectHint;
    }

    public synchronized void copyOnlySet(PictureInPictureParams otherArgs) {
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

    private protected float getAspectRatio() {
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

    /* JADX INFO: Access modifiers changed from: private */
    public List<RemoteAction> getActions() {
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

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getSourceRectHint() {
        return this.mSourceRectHint;
    }

    public synchronized boolean hasSourceBoundsHint() {
        return (this.mSourceRectHint == null || this.mSourceRectHint.isEmpty()) ? false : true;
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
}
