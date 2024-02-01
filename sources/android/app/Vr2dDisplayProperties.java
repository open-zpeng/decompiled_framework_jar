package android.app;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public final class Vr2dDisplayProperties implements Parcelable {
    public static final Parcelable.Creator<Vr2dDisplayProperties> CREATOR = new Parcelable.Creator<Vr2dDisplayProperties>() { // from class: android.app.Vr2dDisplayProperties.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Vr2dDisplayProperties createFromParcel(Parcel source) {
            return new Vr2dDisplayProperties(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Vr2dDisplayProperties[] newArray(int size) {
            return new Vr2dDisplayProperties[size];
        }
    };
    public static final int FLAG_VIRTUAL_DISPLAY_ENABLED = 1;
    private final int mAddedFlags;
    private final int mDpi;
    private final int mHeight;
    private final int mRemovedFlags;
    private final int mWidth;

    private protected Vr2dDisplayProperties(int width, int height, int dpi) {
        this(width, height, dpi, 0, 0);
    }

    private synchronized Vr2dDisplayProperties(int width, int height, int dpi, int flags, int removedFlags) {
        this.mWidth = width;
        this.mHeight = height;
        this.mDpi = dpi;
        this.mAddedFlags = flags;
        this.mRemovedFlags = removedFlags;
    }

    public int hashCode() {
        int result = getWidth();
        return (31 * ((31 * result) + getHeight())) + getDpi();
    }

    public String toString() {
        return "Vr2dDisplayProperties{mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + ", mDpi=" + this.mDpi + ", flags=" + toReadableFlags(this.mAddedFlags) + ", removed_flags=" + toReadableFlags(this.mRemovedFlags) + "}";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vr2dDisplayProperties that = (Vr2dDisplayProperties) o;
        if (getFlags() == that.getFlags() && getRemovedFlags() == that.getRemovedFlags() && getWidth() == that.getWidth() && getHeight() == that.getHeight() && getDpi() == that.getDpi()) {
            return true;
        }
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mHeight);
        dest.writeInt(this.mDpi);
        dest.writeInt(this.mAddedFlags);
        dest.writeInt(this.mRemovedFlags);
    }

    private synchronized Vr2dDisplayProperties(Parcel source) {
        this.mWidth = source.readInt();
        this.mHeight = source.readInt();
        this.mDpi = source.readInt();
        this.mAddedFlags = source.readInt();
        this.mRemovedFlags = source.readInt();
    }

    public synchronized void dump(PrintWriter pw, String prefix) {
        pw.println(prefix + toString());
    }

    public synchronized int getWidth() {
        return this.mWidth;
    }

    public synchronized int getHeight() {
        return this.mHeight;
    }

    public synchronized int getDpi() {
        return this.mDpi;
    }

    public synchronized int getFlags() {
        return this.mAddedFlags;
    }

    public synchronized int getRemovedFlags() {
        return this.mRemovedFlags;
    }

    private static synchronized String toReadableFlags(int flags) {
        String retval = "{";
        if ((flags & 1) == 1) {
            retval = "{enabled";
        }
        return retval + "}";
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private int mAddedFlags = 0;
        private int mRemovedFlags = 0;
        private int mWidth = -1;
        private int mHeight = -1;
        private int mDpi = -1;

        private protected Builder() {
        }

        public synchronized Builder setDimensions(int width, int height, int dpi) {
            this.mWidth = width;
            this.mHeight = height;
            this.mDpi = dpi;
            return this;
        }

        private protected Builder setEnabled(boolean enabled) {
            if (enabled) {
                addFlags(1);
            } else {
                removeFlags(1);
            }
            return this;
        }

        public synchronized Builder addFlags(int flags) {
            this.mAddedFlags |= flags;
            this.mRemovedFlags &= ~flags;
            return this;
        }

        public synchronized Builder removeFlags(int flags) {
            this.mRemovedFlags |= flags;
            this.mAddedFlags &= ~flags;
            return this;
        }

        private protected Vr2dDisplayProperties build() {
            return new Vr2dDisplayProperties(this.mWidth, this.mHeight, this.mDpi, this.mAddedFlags, this.mRemovedFlags);
        }
    }
}
