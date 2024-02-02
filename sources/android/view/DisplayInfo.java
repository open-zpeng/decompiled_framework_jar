package android.view;

import android.app.ActivityThread;
import android.content.Context;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ServiceManager;
import android.util.ArraySet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.IWindowManager;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class DisplayInfo implements Parcelable {
    public static final Parcelable.Creator<DisplayInfo> CREATOR = new Parcelable.Creator<DisplayInfo>() { // from class: android.view.DisplayInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DisplayInfo createFromParcel(Parcel source) {
            return new DisplayInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DisplayInfo[] newArray(int size) {
            return new DisplayInfo[size];
        }
    };
    private static IWindowManager mWindowManager = null;
    private static Configuration mXuiConfiguration = null;
    public String address;
    public int appHeight;
    public long appVsyncOffsetNanos;
    public int appWidth;
    public int colorMode;
    public int defaultModeId;
    private protected DisplayCutout displayCutout;
    public int flags;
    public Display.HdrCapabilities hdrCapabilities;
    public int largestNominalAppHeight;
    public int largestNominalAppWidth;
    public int layerStack;
    public int logicalDensityDpi;
    private protected int logicalHeight;
    private protected int logicalWidth;
    public int modeId;
    public String name;
    public int overscanBottom;
    public int overscanLeft;
    public int overscanRight;
    public int overscanTop;
    public String ownerPackageName;
    public int ownerUid;
    public float physicalXDpi;
    public float physicalYDpi;
    public long presentationDeadlineNanos;
    public int removeMode;
    private protected int rotation;
    public int smallestNominalAppHeight;
    public int smallestNominalAppWidth;
    public int state;
    public int[] supportedColorModes;
    public Display.Mode[] supportedModes;
    public int type;
    public String uniqueId;

    /* JADX INFO: Access modifiers changed from: private */
    public DisplayInfo() {
        this.supportedModes = Display.Mode.EMPTY_ARRAY;
        this.supportedColorModes = new int[]{0};
        this.removeMode = 0;
    }

    public synchronized DisplayInfo(DisplayInfo other) {
        this.supportedModes = Display.Mode.EMPTY_ARRAY;
        this.supportedColorModes = new int[]{0};
        this.removeMode = 0;
        copyFrom(other);
    }

    private synchronized DisplayInfo(Parcel source) {
        this.supportedModes = Display.Mode.EMPTY_ARRAY;
        this.supportedColorModes = new int[]{0};
        this.removeMode = 0;
        readFromParcel(source);
    }

    public boolean equals(Object o) {
        return (o instanceof DisplayInfo) && equals((DisplayInfo) o);
    }

    public synchronized boolean equals(DisplayInfo other) {
        return other != null && this.layerStack == other.layerStack && this.flags == other.flags && this.type == other.type && Objects.equals(this.address, other.address) && Objects.equals(this.uniqueId, other.uniqueId) && this.appWidth == other.appWidth && this.appHeight == other.appHeight && this.smallestNominalAppWidth == other.smallestNominalAppWidth && this.smallestNominalAppHeight == other.smallestNominalAppHeight && this.largestNominalAppWidth == other.largestNominalAppWidth && this.largestNominalAppHeight == other.largestNominalAppHeight && this.logicalWidth == other.logicalWidth && this.logicalHeight == other.logicalHeight && this.overscanLeft == other.overscanLeft && this.overscanTop == other.overscanTop && this.overscanRight == other.overscanRight && this.overscanBottom == other.overscanBottom && Objects.equals(this.displayCutout, other.displayCutout) && this.rotation == other.rotation && this.modeId == other.modeId && this.defaultModeId == other.defaultModeId && this.colorMode == other.colorMode && Arrays.equals(this.supportedColorModes, other.supportedColorModes) && Objects.equals(this.hdrCapabilities, other.hdrCapabilities) && this.logicalDensityDpi == other.logicalDensityDpi && this.physicalXDpi == other.physicalXDpi && this.physicalYDpi == other.physicalYDpi && this.appVsyncOffsetNanos == other.appVsyncOffsetNanos && this.presentationDeadlineNanos == other.presentationDeadlineNanos && this.state == other.state && this.ownerUid == other.ownerUid && Objects.equals(this.ownerPackageName, other.ownerPackageName) && this.removeMode == other.removeMode;
    }

    public int hashCode() {
        return 0;
    }

    public synchronized void copyFrom(DisplayInfo other) {
        this.layerStack = other.layerStack;
        this.flags = other.flags;
        this.type = other.type;
        this.address = other.address;
        this.name = other.name;
        this.uniqueId = other.uniqueId;
        this.appWidth = other.appWidth;
        this.appHeight = other.appHeight;
        this.smallestNominalAppWidth = other.smallestNominalAppWidth;
        this.smallestNominalAppHeight = other.smallestNominalAppHeight;
        this.largestNominalAppWidth = other.largestNominalAppWidth;
        this.largestNominalAppHeight = other.largestNominalAppHeight;
        this.logicalWidth = other.logicalWidth;
        this.logicalHeight = other.logicalHeight;
        this.overscanLeft = other.overscanLeft;
        this.overscanTop = other.overscanTop;
        this.overscanRight = other.overscanRight;
        this.overscanBottom = other.overscanBottom;
        this.displayCutout = other.displayCutout;
        this.rotation = other.rotation;
        this.modeId = other.modeId;
        this.defaultModeId = other.defaultModeId;
        this.supportedModes = (Display.Mode[]) Arrays.copyOf(other.supportedModes, other.supportedModes.length);
        this.colorMode = other.colorMode;
        this.supportedColorModes = Arrays.copyOf(other.supportedColorModes, other.supportedColorModes.length);
        this.hdrCapabilities = other.hdrCapabilities;
        this.logicalDensityDpi = other.logicalDensityDpi;
        this.physicalXDpi = other.physicalXDpi;
        this.physicalYDpi = other.physicalYDpi;
        this.appVsyncOffsetNanos = other.appVsyncOffsetNanos;
        this.presentationDeadlineNanos = other.presentationDeadlineNanos;
        this.state = other.state;
        this.ownerUid = other.ownerUid;
        this.ownerPackageName = other.ownerPackageName;
        this.removeMode = other.removeMode;
    }

    public synchronized void readFromParcel(Parcel source) {
        this.layerStack = source.readInt();
        this.flags = source.readInt();
        this.type = source.readInt();
        this.address = source.readString();
        this.name = source.readString();
        this.appWidth = source.readInt();
        this.appHeight = source.readInt();
        this.smallestNominalAppWidth = source.readInt();
        this.smallestNominalAppHeight = source.readInt();
        this.largestNominalAppWidth = source.readInt();
        this.largestNominalAppHeight = source.readInt();
        this.logicalWidth = source.readInt();
        this.logicalHeight = source.readInt();
        this.overscanLeft = source.readInt();
        this.overscanTop = source.readInt();
        this.overscanRight = source.readInt();
        this.overscanBottom = source.readInt();
        this.displayCutout = DisplayCutout.ParcelableWrapper.readCutoutFromParcel(source);
        this.rotation = source.readInt();
        this.modeId = source.readInt();
        this.defaultModeId = source.readInt();
        int nModes = source.readInt();
        this.supportedModes = new Display.Mode[nModes];
        for (int i = 0; i < nModes; i++) {
            this.supportedModes[i] = Display.Mode.CREATOR.createFromParcel(source);
        }
        int i2 = source.readInt();
        this.colorMode = i2;
        int nColorModes = source.readInt();
        this.supportedColorModes = new int[nColorModes];
        for (int i3 = 0; i3 < nColorModes; i3++) {
            this.supportedColorModes[i3] = source.readInt();
        }
        this.hdrCapabilities = (Display.HdrCapabilities) source.readParcelable(null);
        this.logicalDensityDpi = source.readInt();
        this.physicalXDpi = source.readFloat();
        this.physicalYDpi = source.readFloat();
        this.appVsyncOffsetNanos = source.readLong();
        this.presentationDeadlineNanos = source.readLong();
        this.state = source.readInt();
        this.ownerUid = source.readInt();
        this.ownerPackageName = source.readString();
        this.uniqueId = source.readString();
        this.removeMode = source.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.layerStack);
        dest.writeInt(this.flags);
        dest.writeInt(this.type);
        dest.writeString(this.address);
        dest.writeString(this.name);
        dest.writeInt(this.appWidth);
        dest.writeInt(this.appHeight);
        dest.writeInt(this.smallestNominalAppWidth);
        dest.writeInt(this.smallestNominalAppHeight);
        dest.writeInt(this.largestNominalAppWidth);
        dest.writeInt(this.largestNominalAppHeight);
        dest.writeInt(this.logicalWidth);
        dest.writeInt(this.logicalHeight);
        dest.writeInt(this.overscanLeft);
        dest.writeInt(this.overscanTop);
        dest.writeInt(this.overscanRight);
        dest.writeInt(this.overscanBottom);
        DisplayCutout.ParcelableWrapper.writeCutoutToParcel(this.displayCutout, dest, flags);
        dest.writeInt(this.rotation);
        dest.writeInt(this.modeId);
        dest.writeInt(this.defaultModeId);
        dest.writeInt(this.supportedModes.length);
        for (int i = 0; i < this.supportedModes.length; i++) {
            this.supportedModes[i].writeToParcel(dest, flags);
        }
        int i2 = this.colorMode;
        dest.writeInt(i2);
        dest.writeInt(this.supportedColorModes.length);
        for (int i3 = 0; i3 < this.supportedColorModes.length; i3++) {
            dest.writeInt(this.supportedColorModes[i3]);
        }
        dest.writeParcelable(this.hdrCapabilities, flags);
        dest.writeInt(this.logicalDensityDpi);
        dest.writeFloat(this.physicalXDpi);
        dest.writeFloat(this.physicalYDpi);
        dest.writeLong(this.appVsyncOffsetNanos);
        dest.writeLong(this.presentationDeadlineNanos);
        dest.writeInt(this.state);
        dest.writeInt(this.ownerUid);
        dest.writeString(this.ownerPackageName);
        dest.writeString(this.uniqueId);
        dest.writeInt(this.removeMode);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public synchronized Display.Mode getMode() {
        return findMode(this.modeId);
    }

    public synchronized Display.Mode getDefaultMode() {
        return findMode(this.defaultModeId);
    }

    private synchronized Display.Mode findMode(int id) {
        for (int i = 0; i < this.supportedModes.length; i++) {
            if (this.supportedModes[i].getModeId() == id) {
                return this.supportedModes[i];
            }
        }
        throw new IllegalStateException("Unable to locate mode " + id);
    }

    public synchronized int findDefaultModeByRefreshRate(float refreshRate) {
        Display.Mode[] modes = this.supportedModes;
        Display.Mode defaultMode = getDefaultMode();
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].matches(defaultMode.getPhysicalWidth(), defaultMode.getPhysicalHeight(), refreshRate)) {
                return modes[i].getModeId();
            }
        }
        return 0;
    }

    public synchronized float[] getDefaultRefreshRates() {
        Display.Mode[] modes = this.supportedModes;
        ArraySet<Float> rates = new ArraySet<>();
        Display.Mode defaultMode = getDefaultMode();
        for (Display.Mode mode : modes) {
            if (mode.getPhysicalWidth() == defaultMode.getPhysicalWidth() && mode.getPhysicalHeight() == defaultMode.getPhysicalHeight()) {
                rates.add(Float.valueOf(mode.getRefreshRate()));
            }
        }
        int i = rates.size();
        float[] result = new float[i];
        int i2 = 0;
        Iterator<Float> it = rates.iterator();
        while (it.hasNext()) {
            Float rate = it.next();
            result[i2] = rate.floatValue();
            i2++;
        }
        return result;
    }

    public synchronized void getAppMetrics(DisplayMetrics outMetrics) {
        getAppMetrics(outMetrics, CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO, null);
    }

    public synchronized void getAppMetrics(DisplayMetrics outMetrics, DisplayAdjustments displayAdjustments) {
        getMetricsWithSize(outMetrics, displayAdjustments.getCompatibilityInfo(), displayAdjustments.getConfiguration(), this.appWidth, this.appHeight, false);
    }

    public synchronized void getAppMetrics(DisplayMetrics outMetrics, CompatibilityInfo ci, Configuration configuration) {
        getMetricsWithSize(outMetrics, ci, configuration, this.appWidth, this.appHeight, false);
    }

    public synchronized void getLogicalMetrics(DisplayMetrics outMetrics, CompatibilityInfo compatInfo, Configuration configuration) {
        getMetricsWithSize(outMetrics, compatInfo, configuration, this.logicalWidth, this.logicalHeight, true);
    }

    public synchronized int getNaturalWidth() {
        return (this.rotation == 0 || this.rotation == 2) ? this.logicalWidth : this.logicalHeight;
    }

    public synchronized int getNaturalHeight() {
        return (this.rotation == 0 || this.rotation == 2) ? this.logicalHeight : this.logicalWidth;
    }

    public synchronized boolean isHdr() {
        int[] types = this.hdrCapabilities != null ? this.hdrCapabilities.getSupportedHdrTypes() : null;
        return types != null && types.length > 0;
    }

    public synchronized boolean isWideColorGamut() {
        int[] iArr;
        for (int colorMode : this.supportedColorModes) {
            if (colorMode == 6 || colorMode > 7) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean hasAccess(int uid) {
        return Display.hasAccess(uid, this.flags, this.ownerUid);
    }

    private Configuration getXuiConfiguration() {
        IBinder b;
        if (mXuiConfiguration == null) {
            try {
                if (mWindowManager == null && (b = ServiceManager.getService(Context.WINDOW_SERVICE)) != null) {
                    mWindowManager = IWindowManager.Stub.asInterface(b);
                }
                if (mWindowManager != null) {
                    mXuiConfiguration = mWindowManager.getXuiConfiguration(ActivityThread.currentPackageName());
                } else {
                    Log.d("XUIWINDOW", "getConfiguration mWindowManager==null!!!!!!!!");
                }
            } catch (Exception e) {
                Log.d("XUIWINDOW", "getDisplayMetrics() exception:" + e);
                e.printStackTrace();
            }
        }
        return mXuiConfiguration;
    }

    private Rect getDisplaySize() {
        String pkgName = ActivityThread.currentPackageName();
        int winType = "com.alipay.arome.app".equals(pkgName) ? 12 : 2;
        try {
            if (mWindowManager != null) {
                return mWindowManager.getXuiRectByType(winType);
            }
            return null;
        } catch (Exception e) {
            Log.d("XUIWINDOW", "getDisplaySize() exception:" + e);
            e.printStackTrace();
            return null;
        }
    }

    private void getMetricsWithSize(DisplayMetrics outMetrics, CompatibilityInfo compatInfo, Configuration configuration, int width, int height, boolean logicalSize) {
        int i = this.logicalDensityDpi;
        outMetrics.noncompatDensityDpi = i;
        outMetrics.densityDpi = i;
        float f = this.logicalDensityDpi * 0.00625f;
        outMetrics.noncompatDensity = f;
        outMetrics.density = f;
        float f2 = outMetrics.density;
        outMetrics.noncompatScaledDensity = f2;
        outMetrics.scaledDensity = f2;
        float f3 = this.physicalXDpi;
        outMetrics.noncompatXdpi = f3;
        outMetrics.xdpi = f3;
        float f4 = this.physicalYDpi;
        outMetrics.noncompatYdpi = f4;
        outMetrics.ydpi = f4;
        if (mXuiConfiguration == null) {
            mXuiConfiguration = getXuiConfiguration();
        }
        if (logicalSize) {
            if (mXuiConfiguration != null) {
                int i2 = mXuiConfiguration.screenWidthDp;
                outMetrics.widthPixels = i2;
                outMetrics.noncompatWidthPixels = i2;
                int i3 = mXuiConfiguration.screenHeightDp;
                outMetrics.heightPixels = i3;
                outMetrics.noncompatHeightPixels = i3;
            } else {
                outMetrics.widthPixels = width;
                outMetrics.noncompatWidthPixels = width;
                outMetrics.heightPixels = height;
                outMetrics.noncompatHeightPixels = height;
            }
        } else if (mXuiConfiguration != null) {
            int i4 = mXuiConfiguration.densityDpi;
            outMetrics.noncompatDensityDpi = i4;
            outMetrics.densityDpi = i4;
            float f5 = mXuiConfiguration.densityDpi * 0.00625f;
            outMetrics.noncompatDensity = f5;
            outMetrics.density = f5;
            float f6 = outMetrics.density;
            outMetrics.noncompatScaledDensity = f6;
            outMetrics.scaledDensity = f6;
            int i5 = mXuiConfiguration.screenWidthDp;
            outMetrics.widthPixels = i5;
            outMetrics.noncompatWidthPixels = i5;
            int i6 = mXuiConfiguration.screenHeightDp;
            outMetrics.heightPixels = i6;
            outMetrics.noncompatHeightPixels = i6;
        } else {
            Rect appBounds = getDisplaySize();
            if (appBounds == null) {
                appBounds = configuration != null ? configuration.windowConfiguration.getAppBounds() : null;
            }
            int width2 = appBounds != null ? appBounds.width() : width;
            int height2 = appBounds != null ? appBounds.height() : height;
            outMetrics.widthPixels = width2;
            outMetrics.noncompatWidthPixels = width2;
            outMetrics.heightPixels = height2;
            outMetrics.noncompatHeightPixels = height2;
        }
        if (!compatInfo.equals(CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO)) {
            compatInfo.applyToDisplayMetrics(outMetrics);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DisplayInfo{\"");
        sb.append(this.name);
        sb.append("\", uniqueId \"");
        sb.append(this.uniqueId);
        sb.append("\", app ");
        sb.append(this.appWidth);
        sb.append(" x ");
        sb.append(this.appHeight);
        sb.append(", real ");
        sb.append(this.logicalWidth);
        sb.append(" x ");
        sb.append(this.logicalHeight);
        if (this.overscanLeft != 0 || this.overscanTop != 0 || this.overscanRight != 0 || this.overscanBottom != 0) {
            sb.append(", overscan (");
            sb.append(this.overscanLeft);
            sb.append(",");
            sb.append(this.overscanTop);
            sb.append(",");
            sb.append(this.overscanRight);
            sb.append(",");
            sb.append(this.overscanBottom);
            sb.append(")");
        }
        sb.append(", largest app ");
        sb.append(this.largestNominalAppWidth);
        sb.append(" x ");
        sb.append(this.largestNominalAppHeight);
        sb.append(", smallest app ");
        sb.append(this.smallestNominalAppWidth);
        sb.append(" x ");
        sb.append(this.smallestNominalAppHeight);
        sb.append(", mode ");
        sb.append(this.modeId);
        sb.append(", defaultMode ");
        sb.append(this.defaultModeId);
        sb.append(", modes ");
        sb.append(Arrays.toString(this.supportedModes));
        sb.append(", colorMode ");
        sb.append(this.colorMode);
        sb.append(", supportedColorModes ");
        sb.append(Arrays.toString(this.supportedColorModes));
        sb.append(", hdrCapabilities ");
        sb.append(this.hdrCapabilities);
        sb.append(", rotation ");
        sb.append(this.rotation);
        sb.append(", density ");
        sb.append(this.logicalDensityDpi);
        sb.append(" (");
        sb.append(this.physicalXDpi);
        sb.append(" x ");
        sb.append(this.physicalYDpi);
        sb.append(") dpi, layerStack ");
        sb.append(this.layerStack);
        sb.append(", appVsyncOff ");
        sb.append(this.appVsyncOffsetNanos);
        sb.append(", presDeadline ");
        sb.append(this.presentationDeadlineNanos);
        sb.append(", type ");
        sb.append(Display.typeToString(this.type));
        if (this.address != null) {
            sb.append(", address ");
            sb.append(this.address);
        }
        sb.append(", state ");
        sb.append(Display.stateToString(this.state));
        if (this.ownerUid != 0 || this.ownerPackageName != null) {
            sb.append(", owner ");
            sb.append(this.ownerPackageName);
            sb.append(" (uid ");
            sb.append(this.ownerUid);
            sb.append(")");
        }
        sb.append(flagsToString(this.flags));
        sb.append(", removeMode ");
        sb.append(this.removeMode);
        sb.append("}");
        return sb.toString();
    }

    public synchronized void writeToProto(ProtoOutputStream protoOutputStream, long fieldId) {
        long token = protoOutputStream.start(fieldId);
        protoOutputStream.write(1120986464257L, this.logicalWidth);
        protoOutputStream.write(1120986464258L, this.logicalHeight);
        protoOutputStream.write(1120986464259L, this.appWidth);
        protoOutputStream.write(1120986464260L, this.appHeight);
        protoOutputStream.write(1138166333445L, this.name);
        protoOutputStream.end(token);
    }

    private static synchronized String flagsToString(int flags) {
        StringBuilder result = new StringBuilder();
        if ((flags & 2) != 0) {
            result.append(", FLAG_SECURE");
        }
        if ((flags & 1) != 0) {
            result.append(", FLAG_SUPPORTS_PROTECTED_BUFFERS");
        }
        if ((flags & 4) != 0) {
            result.append(", FLAG_PRIVATE");
        }
        if ((flags & 8) != 0) {
            result.append(", FLAG_PRESENTATION");
        }
        if ((1073741824 & flags) != 0) {
            result.append(", FLAG_SCALING_DISABLED");
        }
        if ((flags & 16) != 0) {
            result.append(", FLAG_ROUND");
        }
        return result.toString();
    }
}
