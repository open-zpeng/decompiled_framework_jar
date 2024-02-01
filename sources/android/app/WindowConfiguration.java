package android.app;

import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.proto.ProtoInputStream;
import android.util.proto.ProtoOutputStream;
import android.util.proto.WireTypeMismatchException;
import android.view.Surface;
import java.io.IOException;

/* loaded from: classes.dex */
public class WindowConfiguration implements Parcelable, Comparable<WindowConfiguration> {
    public static final int ACTIVITY_TYPE_ASSISTANT = 4;
    public static final int ACTIVITY_TYPE_HOME = 2;
    public static final int ACTIVITY_TYPE_RECENTS = 3;
    public static final int ACTIVITY_TYPE_STANDARD = 1;
    public static final int ACTIVITY_TYPE_UNDEFINED = 0;
    private static final int ALWAYS_ON_TOP_OFF = 2;
    private static final int ALWAYS_ON_TOP_ON = 1;
    private static final int ALWAYS_ON_TOP_UNDEFINED = 0;
    public static final Parcelable.Creator<WindowConfiguration> CREATOR = new Parcelable.Creator<WindowConfiguration>() { // from class: android.app.WindowConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WindowConfiguration createFromParcel(Parcel in) {
            return new WindowConfiguration(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WindowConfiguration[] newArray(int size) {
            return new WindowConfiguration[size];
        }
    };
    public static final int PINNED_WINDOWING_MODE_ELEVATION_IN_DIP = 5;
    public static final int ROTATION_UNDEFINED = -1;
    public static final int WINDOWING_MODE_FREEFORM = 5;
    public static final int WINDOWING_MODE_FULLSCREEN = 1;
    public static final int WINDOWING_MODE_FULLSCREEN_OR_SPLIT_SCREEN_SECONDARY = 4;
    public static final int WINDOWING_MODE_PINNED = 2;
    public static final int WINDOWING_MODE_SPLIT_SCREEN_PRIMARY = 3;
    public static final int WINDOWING_MODE_SPLIT_SCREEN_SECONDARY = 4;
    public static final int WINDOWING_MODE_UNDEFINED = 0;
    public static final int WINDOW_CONFIG_ACTIVITY_TYPE = 8;
    public static final int WINDOW_CONFIG_ALWAYS_ON_TOP = 16;
    public static final int WINDOW_CONFIG_APP_BOUNDS = 2;
    public static final int WINDOW_CONFIG_BOUNDS = 1;
    public static final int WINDOW_CONFIG_DISPLAY_WINDOWING_MODE = 64;
    public static final int WINDOW_CONFIG_ROTATION = 32;
    public static final int WINDOW_CONFIG_WINDOWING_MODE = 4;
    @ActivityType
    private int mActivityType;
    @AlwaysOnTop
    private int mAlwaysOnTop;
    private Rect mAppBounds;
    private Rect mBounds;
    @WindowingMode
    private int mDisplayWindowingMode;
    private int mRotation;
    @WindowingMode
    private int mWindowingMode;

    /* loaded from: classes.dex */
    public @interface ActivityType {
    }

    /* loaded from: classes.dex */
    private @interface AlwaysOnTop {
    }

    /* loaded from: classes.dex */
    public @interface WindowConfig {
    }

    /* loaded from: classes.dex */
    public @interface WindowingMode {
    }

    public WindowConfiguration() {
        this.mBounds = new Rect();
        this.mRotation = -1;
        unset();
    }

    public WindowConfiguration(WindowConfiguration configuration) {
        this.mBounds = new Rect();
        this.mRotation = -1;
        setTo(configuration);
    }

    private WindowConfiguration(Parcel in) {
        this.mBounds = new Rect();
        this.mRotation = -1;
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mBounds, flags);
        dest.writeParcelable(this.mAppBounds, flags);
        dest.writeInt(this.mWindowingMode);
        dest.writeInt(this.mActivityType);
        dest.writeInt(this.mAlwaysOnTop);
        dest.writeInt(this.mRotation);
        dest.writeInt(this.mDisplayWindowingMode);
    }

    private void readFromParcel(Parcel source) {
        this.mBounds = (Rect) source.readParcelable(Rect.class.getClassLoader());
        this.mAppBounds = (Rect) source.readParcelable(Rect.class.getClassLoader());
        this.mWindowingMode = source.readInt();
        this.mActivityType = source.readInt();
        this.mAlwaysOnTop = source.readInt();
        this.mRotation = source.readInt();
        this.mDisplayWindowingMode = source.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void setBounds(Rect rect) {
        if (rect == null) {
            this.mBounds.setEmpty();
        } else {
            this.mBounds.set(rect);
        }
    }

    public void setAppBounds(Rect rect) {
        if (rect == null) {
            this.mAppBounds = null;
        } else {
            setAppBounds(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.mAlwaysOnTop = alwaysOnTop ? 1 : 2;
    }

    private void setAlwaysOnTop(@AlwaysOnTop int alwaysOnTop) {
        this.mAlwaysOnTop = alwaysOnTop;
    }

    public void setAppBounds(int left, int top, int right, int bottom) {
        if (this.mAppBounds == null) {
            this.mAppBounds = new Rect();
        }
        this.mAppBounds.set(left, top, right, bottom);
    }

    public Rect getAppBounds() {
        return this.mAppBounds;
    }

    public Rect getBounds() {
        return this.mBounds;
    }

    public int getRotation() {
        return this.mRotation;
    }

    public void setRotation(int rotation) {
        this.mRotation = rotation;
    }

    public void setWindowingMode(@WindowingMode int windowingMode) {
        this.mWindowingMode = windowingMode;
    }

    @WindowingMode
    public int getWindowingMode() {
        return this.mWindowingMode;
    }

    public void setDisplayWindowingMode(@WindowingMode int windowingMode) {
        this.mDisplayWindowingMode = windowingMode;
    }

    public void setActivityType(@ActivityType int activityType) {
        if (this.mActivityType == activityType) {
            return;
        }
        if (ActivityThread.isSystem()) {
            int i = this.mActivityType;
        }
        this.mActivityType = activityType;
    }

    @ActivityType
    public int getActivityType() {
        return this.mActivityType;
    }

    public void setTo(WindowConfiguration other) {
        setBounds(other.mBounds);
        setAppBounds(other.mAppBounds);
        setWindowingMode(other.mWindowingMode);
        setActivityType(other.mActivityType);
        setAlwaysOnTop(other.mAlwaysOnTop);
        setRotation(other.mRotation);
        setDisplayWindowingMode(other.mDisplayWindowingMode);
    }

    public void unset() {
        setToDefaults();
    }

    public void setToDefaults() {
        setAppBounds(null);
        setBounds(null);
        setWindowingMode(0);
        setActivityType(0);
        setAlwaysOnTop(0);
        setRotation(-1);
        setDisplayWindowingMode(0);
    }

    @WindowConfig
    public int updateFrom(WindowConfiguration delta) {
        int changed = 0;
        if (!delta.mBounds.isEmpty() && !delta.mBounds.equals(this.mBounds)) {
            changed = 0 | 1;
            setBounds(delta.mBounds);
        }
        Rect rect = delta.mAppBounds;
        if (rect != null && !rect.equals(this.mAppBounds)) {
            changed |= 2;
            setAppBounds(delta.mAppBounds);
        }
        int i = delta.mWindowingMode;
        if (i != 0 && this.mWindowingMode != i) {
            changed |= 4;
            setWindowingMode(i);
        }
        int i2 = delta.mActivityType;
        if (i2 != 0 && this.mActivityType != i2) {
            changed |= 8;
            setActivityType(i2);
        }
        int i3 = delta.mAlwaysOnTop;
        if (i3 != 0 && this.mAlwaysOnTop != i3) {
            changed |= 16;
            setAlwaysOnTop(i3);
        }
        int i4 = delta.mRotation;
        if (i4 != -1 && i4 != this.mRotation) {
            changed |= 32;
            setRotation(i4);
        }
        int i5 = delta.mDisplayWindowingMode;
        if (i5 != 0 && this.mDisplayWindowingMode != i5) {
            int changed2 = changed | 64;
            setDisplayWindowingMode(i5);
            return changed2;
        }
        return changed;
    }

    @WindowConfig
    public long diff(WindowConfiguration other, boolean compareUndefined) {
        Rect rect;
        Rect rect2;
        long changes = this.mBounds.equals(other.mBounds) ? 0L : 0 | 1;
        if ((compareUndefined || other.mAppBounds != null) && (rect = this.mAppBounds) != (rect2 = other.mAppBounds) && (rect == null || !rect.equals(rect2))) {
            changes |= 2;
        }
        if ((compareUndefined || other.mWindowingMode != 0) && this.mWindowingMode != other.mWindowingMode) {
            changes |= 4;
        }
        if ((compareUndefined || other.mActivityType != 0) && this.mActivityType != other.mActivityType) {
            changes |= 8;
        }
        if ((compareUndefined || other.mAlwaysOnTop != 0) && this.mAlwaysOnTop != other.mAlwaysOnTop) {
            changes |= 16;
        }
        if ((compareUndefined || other.mRotation != -1) && this.mRotation != other.mRotation) {
            changes |= 32;
        }
        if ((compareUndefined || other.mDisplayWindowingMode != 0) && this.mDisplayWindowingMode != other.mDisplayWindowingMode) {
            return changes | 64;
        }
        return changes;
    }

    @Override // java.lang.Comparable
    public int compareTo(WindowConfiguration that) {
        if (this.mAppBounds == null && that.mAppBounds != null) {
            return 1;
        }
        if (this.mAppBounds != null && that.mAppBounds == null) {
            return -1;
        }
        Rect rect = this.mAppBounds;
        if (rect != null && that.mAppBounds != null) {
            int n = rect.left - that.mAppBounds.left;
            if (n != 0) {
                return n;
            }
            int n2 = this.mAppBounds.top - that.mAppBounds.top;
            if (n2 != 0) {
                return n2;
            }
            int n3 = this.mAppBounds.right - that.mAppBounds.right;
            if (n3 != 0) {
                return n3;
            }
            int n4 = this.mAppBounds.bottom - that.mAppBounds.bottom;
            if (n4 != 0) {
                return n4;
            }
        }
        int n5 = this.mBounds.left - that.mBounds.left;
        if (n5 != 0) {
            return n5;
        }
        int n6 = this.mBounds.top - that.mBounds.top;
        if (n6 != 0) {
            return n6;
        }
        int n7 = this.mBounds.right - that.mBounds.right;
        if (n7 != 0) {
            return n7;
        }
        int n8 = this.mBounds.bottom - that.mBounds.bottom;
        if (n8 != 0) {
            return n8;
        }
        int n9 = this.mWindowingMode - that.mWindowingMode;
        if (n9 != 0) {
            return n9;
        }
        int n10 = this.mActivityType - that.mActivityType;
        if (n10 != 0) {
            return n10;
        }
        int n11 = this.mAlwaysOnTop - that.mAlwaysOnTop;
        if (n11 != 0) {
            return n11;
        }
        int n12 = this.mRotation - that.mRotation;
        if (n12 != 0) {
            return n12;
        }
        int n13 = this.mDisplayWindowingMode - that.mDisplayWindowingMode;
        return n13 != 0 ? n13 : n13;
    }

    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (that == this) {
            return true;
        }
        if (!(that instanceof WindowConfiguration) || compareTo((WindowConfiguration) that) != 0) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = 0;
        Rect rect = this.mAppBounds;
        if (rect != null) {
            result = (0 * 31) + rect.hashCode();
        }
        return (((((((((((result * 31) + this.mBounds.hashCode()) * 31) + this.mWindowingMode) * 31) + this.mActivityType) * 31) + this.mAlwaysOnTop) * 31) + this.mRotation) * 31) + this.mDisplayWindowingMode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ mBounds=");
        sb.append(this.mBounds);
        sb.append(" mAppBounds=");
        sb.append(this.mAppBounds);
        sb.append(" mWindowingMode=");
        sb.append(windowingModeToString(this.mWindowingMode));
        sb.append(" mDisplayWindowingMode=");
        sb.append(windowingModeToString(this.mDisplayWindowingMode));
        sb.append(" mActivityType=");
        sb.append(activityTypeToString(this.mActivityType));
        sb.append(" mAlwaysOnTop=");
        sb.append(alwaysOnTopToString(this.mAlwaysOnTop));
        sb.append(" mRotation=");
        int i = this.mRotation;
        sb.append(i == -1 ? "undefined" : Surface.rotationToString(i));
        sb.append("}");
        return sb.toString();
    }

    public void writeToProto(ProtoOutputStream protoOutputStream, long fieldId) {
        long token = protoOutputStream.start(fieldId);
        Rect rect = this.mAppBounds;
        if (rect != null) {
            rect.writeToProto(protoOutputStream, 1146756268033L);
        }
        protoOutputStream.write(1120986464258L, this.mWindowingMode);
        protoOutputStream.write(1120986464259L, this.mActivityType);
        Rect rect2 = this.mBounds;
        if (rect2 != null) {
            rect2.writeToProto(protoOutputStream, 1146756268036L);
        }
        protoOutputStream.end(token);
    }

    public void readFromProto(ProtoInputStream proto, long fieldId) throws IOException, WireTypeMismatchException {
        long token = proto.start(fieldId);
        while (proto.nextField() != -1) {
            try {
                int fieldNumber = proto.getFieldNumber();
                if (fieldNumber == 1) {
                    this.mAppBounds = new Rect();
                    this.mAppBounds.readFromProto(proto, 1146756268033L);
                } else if (fieldNumber == 2) {
                    this.mWindowingMode = proto.readInt(1120986464258L);
                } else if (fieldNumber == 3) {
                    this.mActivityType = proto.readInt(1120986464259L);
                } else if (fieldNumber == 4) {
                    this.mBounds = new Rect();
                    this.mBounds.readFromProto(proto, 1146756268036L);
                }
            } finally {
                proto.end(token);
            }
        }
    }

    public boolean hasWindowShadow() {
        return tasksAreFloating();
    }

    public boolean hasWindowDecorCaption() {
        return this.mActivityType == 1 && (this.mWindowingMode == 5 || this.mDisplayWindowingMode == 5);
    }

    public boolean canResizeTask() {
        return this.mWindowingMode == 5;
    }

    public boolean persistTaskBounds() {
        return this.mWindowingMode == 5;
    }

    public boolean tasksAreFloating() {
        return isFloating(this.mWindowingMode);
    }

    public static boolean isFloating(int windowingMode) {
        return windowingMode == 5 || windowingMode == 2;
    }

    public static boolean isSplitScreenWindowingMode(int windowingMode) {
        return windowingMode == 3 || windowingMode == 4;
    }

    public boolean canReceiveKeys() {
        return this.mWindowingMode != 2;
    }

    public boolean isAlwaysOnTop() {
        int i = this.mWindowingMode;
        if (i != 2) {
            return i == 5 && this.mAlwaysOnTop == 1;
        }
        return true;
    }

    public boolean keepVisibleDeadAppWindowOnScreen() {
        return this.mWindowingMode != 2;
    }

    public boolean useWindowFrameForBackdrop() {
        int i = this.mWindowingMode;
        return i == 5 || i == 2;
    }

    public boolean windowsAreScaleable() {
        return this.mWindowingMode == 2;
    }

    public boolean hasMovementAnimations() {
        return this.mWindowingMode != 2;
    }

    public boolean supportSplitScreenWindowingMode() {
        return supportSplitScreenWindowingMode(this.mActivityType);
    }

    public static boolean supportSplitScreenWindowingMode(int activityType) {
        return activityType != 4;
    }

    public static String windowingModeToString(@WindowingMode int windowingMode) {
        if (windowingMode != 0) {
            if (windowingMode != 1) {
                if (windowingMode != 2) {
                    if (windowingMode != 3) {
                        if (windowingMode != 4) {
                            if (windowingMode == 5) {
                                return "freeform";
                            }
                            return String.valueOf(windowingMode);
                        }
                        return "split-screen-secondary";
                    }
                    return "split-screen-primary";
                }
                return ContactsContract.ContactOptionsColumns.PINNED;
            }
            return "fullscreen";
        }
        return "undefined";
    }

    public static String activityTypeToString(@ActivityType int applicationType) {
        if (applicationType != 0) {
            if (applicationType != 1) {
                if (applicationType != 2) {
                    if (applicationType != 3) {
                        if (applicationType == 4) {
                            return "assistant";
                        }
                        return String.valueOf(applicationType);
                    }
                    return "recents";
                }
                return CalendarContract.CalendarCache.TIMEZONE_TYPE_HOME;
            }
            return "standard";
        }
        return "undefined";
    }

    public static String alwaysOnTopToString(@AlwaysOnTop int alwaysOnTop) {
        if (alwaysOnTop != 0) {
            if (alwaysOnTop != 1) {
                if (alwaysOnTop == 2) {
                    return "off";
                }
                return String.valueOf(alwaysOnTop);
            }
            return Camera.Parameters.FLASH_MODE_ON;
        }
        return "undefined";
    }
}
