package com.xiaopeng.view;

import android.app.Activity;
import android.app.ActivityThread;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.IPackageManager;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.hardware.display.DisplayManagerGlobal;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Display;
import android.view.IWindowManager;
import android.view.SurfaceControl;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.xiaopeng.app.ActivityInfoManager;
import com.xiaopeng.app.MiniProgramManager;
import com.xiaopeng.app.xpActivityInfo;
import com.xiaopeng.app.xpActivityManager;
import com.xiaopeng.app.xpPackageInfo;
import com.xiaopeng.util.FeatureOption;
import com.xiaopeng.util.xpLogger;
import com.xiaopeng.view.SharedDisplayManager;

/* loaded from: classes3.dex */
public class xpWindowManager {
    public static final boolean CFG_FREEFORM_TOGGLE_ENABLED = false;
    public static final boolean CFG_WINDOW_ANIMATION_ENABLED = false;
    private static final boolean DEBUG = false;
    public static final boolean LETTER_BOX_ENABLED = false;
    private static final String TAG = "xpWindowManager";
    public static final long TOAST_DURATION_LONG = 4000;
    public static final long TOAST_DURATION_LONGER = 6000;
    public static final long TOAST_DURATION_SHORT = 2500;
    private static final String DEVICE = SystemProperties.get("ro.product.device", "");
    private static final int ROUND_CORNER_RADIUS = FeatureOption.FO_ROUND_CORNER_RADIUS;
    private static final String[] SKIP_SYSTEMUI_VISIBILITY_PROCESS = {"com.xiaopeng.carcontrol:unity"};

    /* loaded from: classes3.dex */
    public static final class WindowErrorInfo {
        public String className;
        public String errorType;
        public String packageName;
    }

    /* loaded from: classes3.dex */
    public static final class WindowInfo {
        public Rect dimmerBounds;
        public Rect rect;
        public int[] roundCorner;
        public Rect touchRegion;
        public int windowType = 0;
        public int parentType = 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0054  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x006c A[Catch: Exception -> 0x00e7, TryCatch #0 {Exception -> 0x00e7, blocks: (B:6:0x0009, B:10:0x0031, B:23:0x0047, B:26:0x0056, B:28:0x005a, B:29:0x005c, B:32:0x0062, B:34:0x0066, B:38:0x006e, B:42:0x0075, B:48:0x0083, B:50:0x0088, B:53:0x008e, B:55:0x0093, B:54:0x0091, B:49:0x0086, B:57:0x0097, B:63:0x00a2, B:65:0x00a7, B:68:0x00ad, B:70:0x00b2, B:69:0x00b0, B:64:0x00a5, B:72:0x00b6, B:74:0x00bc, B:75:0x00ce, B:41:0x0073, B:37:0x006c), top: B:80:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0073 A[Catch: Exception -> 0x00e7, TryCatch #0 {Exception -> 0x00e7, blocks: (B:6:0x0009, B:10:0x0031, B:23:0x0047, B:26:0x0056, B:28:0x005a, B:29:0x005c, B:32:0x0062, B:34:0x0066, B:38:0x006e, B:42:0x0075, B:48:0x0083, B:50:0x0088, B:53:0x008e, B:55:0x0093, B:54:0x0091, B:49:0x0086, B:57:0x0097, B:63:0x00a2, B:65:0x00a7, B:68:0x00ad, B:70:0x00b2, B:69:0x00b0, B:64:0x00a5, B:72:0x00b6, B:74:0x00bc, B:75:0x00ce, B:41:0x0073, B:37:0x006c), top: B:80:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x00bc A[Catch: Exception -> 0x00e7, TryCatch #0 {Exception -> 0x00e7, blocks: (B:6:0x0009, B:10:0x0031, B:23:0x0047, B:26:0x0056, B:28:0x005a, B:29:0x005c, B:32:0x0062, B:34:0x0066, B:38:0x006e, B:42:0x0075, B:48:0x0083, B:50:0x0088, B:53:0x008e, B:55:0x0093, B:54:0x0091, B:49:0x0086, B:57:0x0097, B:63:0x00a2, B:65:0x00a7, B:68:0x00ad, B:70:0x00b2, B:69:0x00b0, B:64:0x00a5, B:72:0x00b6, B:74:0x00bc, B:75:0x00ce, B:41:0x0073, B:37:0x006c), top: B:80:0x0009 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.view.WindowManager.LayoutParams getOverrideLayoutParams(android.view.WindowManager.LayoutParams r13, android.view.View r14, android.view.IWindowManager r15) {
        /*
            Method dump skipped, instructions count: 253
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.view.xpWindowManager.getOverrideLayoutParams(android.view.WindowManager$LayoutParams, android.view.View, android.view.IWindowManager):android.view.WindowManager$LayoutParams");
    }

    public static WindowManager.LayoutParams getOverrideLayoutParams(ActivityThread.ActivityClientRecord r) {
        int i;
        if (r != null) {
            Intent intent = r.getIntent();
            Window window = r.getWindow();
            if (intent != null && window != null) {
                String packageName = r.getActivityInfo().packageName;
                WindowManager.LayoutParams lp = window.getAttributes();
                int sharedId = SharedDisplayManager.getOverrideSharedId(SharedDisplayManager.SharedParams.create(packageName, intent, lp));
                int activityFlags = ActivityInfoManager.getActivityFlags(intent);
                if (lp != null) {
                    if (SharedDisplayManager.enable()) {
                        lp.sharedId = sharedId;
                    }
                    lp.type = 1;
                    lp.intentFlags = activityFlags;
                    boolean fullscreen = (activityFlags & 64) == 64;
                    boolean isHome = (activityFlags & 536870912) == 536870912;
                    boolean isPanel = (activityFlags & 256) == 256;
                    boolean isApplet = (activityFlags & 512) == 512;
                    if ((activityFlags & 1024) == 1024) {
                    }
                    boolean isAutomatic = (activityFlags & 134217728) == 134217728;
                    boolean isSecondary = (activityFlags & 268435456) == 268435456;
                    boolean physicalFullscreen = (activityFlags & 32) == 32;
                    if (isPanel) {
                        lp.type = 6;
                        i = 5;
                    } else if (fullscreen) {
                        lp.type = 6;
                        lp.flags |= 1024;
                        lp.systemUiVisibility |= 4;
                        lp.systemUiVisibility |= 1024;
                        lp.systemUiVisibility |= 2;
                        lp.systemUiVisibility |= 512;
                        i = 5;
                    } else if (isHome) {
                        i = 5;
                        lp.type = 5;
                    } else {
                        i = 5;
                        if (isSecondary) {
                            lp.type = 8;
                        }
                    }
                    if (SharedDisplayManager.sharedValid(sharedId)) {
                        lp.type = 10;
                    }
                    if (isApplet) {
                        lp.type = 12;
                    }
                    if (physicalFullscreen) {
                        if (!isHome) {
                            i = 6;
                        }
                        lp.type = i;
                        lp.flags |= 1024;
                        lp.systemUiVisibility |= 4;
                        lp.systemUiVisibility |= 1024;
                        lp.systemUiVisibility |= 2;
                        lp.systemUiVisibility |= 512;
                    }
                    if (isAutomatic) {
                        lp.type = 1;
                        lp.xpFlags |= 8;
                    } else {
                        lp.xpFlags &= -9;
                    }
                    if (fullscreen) {
                        lp.xpFlags |= 4;
                    } else {
                        lp.xpFlags &= -5;
                    }
                    if ((1073741824 & activityFlags) != 1073741824) {
                        lp.flags &= -513;
                    }
                    lp.copyFrom(getOverrideLayoutParams(lp));
                    xpLogger.i(TAG, "getOverrideLayoutParams lp=" + lp.toString() + " sharedId=" + lp.sharedId);
                    return lp;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public static WindowManager.LayoutParams getOverrideLayoutParams(WindowManager.LayoutParams lp, Dialog dialog) {
        WindowManager.LayoutParams olp;
        if (lp != null && dialog != null) {
            Context context = dialog.getContext();
            String packageName = context != null ? context.getPackageName() : "";
            String className = dialog.getClass().getName();
            Activity owner = getDialogOwnerActivity(dialog);
            boolean miniProgram = MiniProgramManager.isMiniProgramDialog(owner);
            boolean sharedApplication = SharedDisplayManager.isSharedApplication(xpActivityInfo.create(packageName, className));
            boolean isApplicationAlert = isApplicationAlertWindowType(lp);
            if (isApplicationAlert && owner != null && (olp = owner.getWindow().getAttributes()) != null) {
                lp.parentType = olp.type;
                lp.flags = olp.flags;
                lp.systemUiVisibility = olp.systemUiVisibility;
                lp.subtreeSystemUiVisibility = olp.subtreeSystemUiVisibility;
                lp.intentFlags = olp.intentFlags;
                int i = olp.type;
                if (i != 6) {
                    if (i != 10) {
                        if (i == 12) {
                            miniProgram = true;
                        }
                    } else {
                        sharedApplication = true;
                    }
                }
            }
            if (sharedApplication) {
                lp.type = 10;
                lp.xpFlags |= 1;
                lp.parentType = 10;
            }
            if (miniProgram) {
                lp.type = 12;
                lp.xpFlags |= 1;
                lp.parentType = 12;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("getOverrideLayoutParams lp.type=");
            sb.append(lp.type);
            sb.append(" lp.parentType=");
            sb.append(lp.parentType);
            sb.append(" owner=");
            sb.append(owner != null ? owner.getComponentName() : "");
            xpLogger.log(TAG, sb.toString());
        }
        return lp;
    }

    public static WindowManager.LayoutParams getOverrideLayoutParams(WindowManager.LayoutParams lp, PopupWindow popup) {
        if (lp != null && popup != null) {
            try {
                View content = popup.getContentView();
                String packageName = content != null ? content.getContext().getBasePackageName() : "";
                boolean systemApplication = ActivityInfoManager.isSystemApplication(packageName);
                if (!systemApplication) {
                    IBinder b = ServiceManager.getService("package");
                    IPackageManager pm = IPackageManager.Stub.asInterface(b);
                    xpPackageInfo info = pm.getXpPackageInfo(packageName);
                    boolean isFullscreen = xpPackageInfo.isFullscreen(info);
                    if (isFullscreen) {
                        lp.flags = getWindowFlags(lp.flags, true, true, true, true, false);
                        lp.systemUiVisibility = getSystemUiVisibility(lp.systemUiVisibility, true, true, true, true, false);
                        lp.subtreeSystemUiVisibility = lp.systemUiVisibility;
                    }
                }
            } catch (Exception e) {
            }
        }
        return lp;
    }

    /* JADX WARN: Code restructure failed: missing block: B:92:0x0187, code lost:
        if (r1 != false) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0189, code lost:
        if (r8 == false) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x018c, code lost:
        r18 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.view.WindowManager.LayoutParams getOverrideLayoutParams(android.view.WindowManager.LayoutParams r23) {
        /*
            Method dump skipped, instructions count: 465
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.view.xpWindowManager.getOverrideLayoutParams(android.view.WindowManager$LayoutParams):android.view.WindowManager$LayoutParams");
    }

    public static Display getOverrideTargetDisplay(WindowManager.LayoutParams lp, Display display) {
        if (lp != null) {
            boolean overrideDisplay = false;
            int overrideDisplayId = -1;
            int i = lp.type;
            if (i == 2036) {
                if (display != null) {
                    int displayId = display.getDisplayId();
                    if (lp.displayId != -1 && lp.displayId != displayId) {
                        overrideDisplay = true;
                        overrideDisplayId = lp.displayId;
                    }
                }
            } else if ((i == 2042 || i == 2060 || i == 2061) && lp.displayId >= 0) {
                overrideDisplay = true;
                overrideDisplayId = lp.displayId;
            }
            if (overrideDisplay && overrideDisplayId >= 0) {
                try {
                    DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
                    Display rd = dm.getRealDisplay(overrideDisplayId);
                    if (rd != null) {
                        return rd;
                    }
                } catch (Exception e) {
                }
            }
        }
        return display;
    }

    public static int getOverrideSubtreeSystemUiVisibility(WindowManager.LayoutParams lp, int systemUiVisibility) {
        if (lp == null) {
            return systemUiVisibility;
        }
        int type = lp.type;
        if (type != 12) {
            return systemUiVisibility;
        }
        boolean currentFullscreen = isFullscreen(lp.systemUiVisibility | systemUiVisibility, lp.flags, lp.xpFlags);
        if (!currentFullscreen) {
            return systemUiVisibility;
        }
        int subtreeSystemUiVisibility = getSystemUiVisibility(systemUiVisibility, false, false, false, false, false);
        return subtreeSystemUiVisibility;
    }

    public static int getOverrideWindowType(int windowType, String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            boolean systemApplication = xpActivityManager.isSystemApplication(packageName);
            if (systemApplication) {
                return windowType;
            }
            if (windowType == 12) {
                return 7;
            }
        }
        return windowType;
    }

    public static int[] getOverrideRoundCorner(int[] roundCorner, WindowManager.LayoutParams lp) {
        if (lp != null && roundCorner != null && roundCorner.length == 3 && lp.type == 12) {
            boolean isDialog = (lp.xpFlags & 1) == 1;
            if (isDialog) {
                roundCorner[0] = 0;
                roundCorner[1] = 0;
                roundCorner[2] = 0;
            }
        }
        return roundCorner;
    }

    public static int getOverrideWallpaperLayer(int layer) {
        if (FeatureOption.FO_PROJECT_UI_TYPE == 2) {
            return 0;
        }
        return layer;
    }

    public static boolean isAutoscreen(int xpFlags) {
        return (xpFlags & 8) == 8;
    }

    public static boolean isFullscreen(int systemUiVisibility, int flags) {
        boolean fullscreen = hasFullscreenFlag(systemUiVisibility, flags);
        boolean hideNavigationBar = hasHideNavigationFlag(systemUiVisibility, flags);
        return fullscreen && hideNavigationBar;
    }

    public static boolean isFullscreen(int systemUiVisibility, int flags, int xpFlags) {
        boolean fullscreen = hasFullscreenFlag(systemUiVisibility, flags);
        boolean hideNavigationBar = hasHideNavigationFlag(systemUiVisibility, flags);
        boolean xpFullscreen = (xpFlags & 4) == 4;
        boolean xpAutoscreen = (xpFlags & 8) == 8;
        if (xpAutoscreen) {
            return xpFullscreen;
        }
        return (fullscreen && hideNavigationBar) || xpFullscreen;
    }

    public static boolean isFullscreen(WindowManager.LayoutParams lp, int privateFlags) {
        boolean lpFullscreen = false;
        boolean pfFullscreen = false;
        if (lp != null) {
            int systemUiVisibility = lp.systemUiVisibility | lp.subtreeSystemUiVisibility;
            boolean lpFullscreen2 = false | isFullscreen(systemUiVisibility, lp.flags, lp.xpFlags);
            lpFullscreen = lpFullscreen2 | ((lp.intentFlags & 64) == 64);
        }
        if (privateFlags > 0) {
            pfFullscreen = false | ((privateFlags & 64) == 64);
        }
        return lpFullscreen || pfFullscreen;
    }

    public static boolean hasFullscreenFlag(int systemUiVisibility, int flags) {
        if ((systemUiVisibility & 4) == 4 || (flags & 1024) == 1024) {
            return true;
        }
        return false;
    }

    public static boolean hasHideNavigationFlag(int systemUiVisibility, int flags) {
        if ((systemUiVisibility & 2) == 2 || (systemUiVisibility & 512) == 512) {
            return true;
        }
        return false;
    }

    public static int getSystemUiVisibility(int systemUiVisibility, boolean immersive, boolean fullscreen, boolean layoutFullscreen, boolean hideNavigation, boolean translucentSystemUI) {
        int systemUiVisibility2;
        int systemUiVisibility3;
        int systemUiVisibility4;
        int systemUiVisibility5;
        if (immersive) {
            systemUiVisibility2 = systemUiVisibility | 4096;
        } else {
            systemUiVisibility2 = systemUiVisibility & (-2049) & (-4097);
        }
        if (fullscreen) {
            systemUiVisibility3 = systemUiVisibility2 | 4;
        } else {
            systemUiVisibility3 = systemUiVisibility2 & (-5);
        }
        if (layoutFullscreen) {
            systemUiVisibility4 = systemUiVisibility3 | 1024;
        } else {
            systemUiVisibility4 = systemUiVisibility3 & (-1025);
        }
        if (hideNavigation) {
            systemUiVisibility5 = systemUiVisibility4 | 2 | 512;
        } else {
            systemUiVisibility5 = systemUiVisibility4 & (-3) & (-513);
        }
        if (translucentSystemUI) {
            return systemUiVisibility5 | 32776;
        }
        return systemUiVisibility5 & (-32777);
    }

    public static int getWindowFlags(int flags, boolean immersive, boolean fullscreen, boolean layoutFullscreen, boolean hideNavigation, boolean translucentSystemUI) {
        int flags2;
        if (fullscreen) {
            flags2 = (flags | 1024) & (-2049);
        } else {
            flags2 = flags & (-1025) & (-2049);
        }
        if (translucentSystemUI) {
            return flags2 | 134217728;
        }
        return flags2 & (-134217729);
    }

    public static String toString(WindowManager.LayoutParams lp) {
        if (lp == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer("");
        buffer.append("packageName=");
        buffer.append(lp.packageName);
        buffer.append(" type=");
        buffer.append(lp.type);
        buffer.append(" parentType=");
        buffer.append(lp.parentType);
        buffer.append(" x=");
        buffer.append(lp.x);
        buffer.append(" y=");
        buffer.append(lp.y);
        buffer.append(" w=");
        buffer.append(lp.width);
        buffer.append(" h=");
        buffer.append(lp.height);
        buffer.append(" alpha=");
        buffer.append(lp.alpha);
        buffer.append(" fullscreen=");
        buffer.append(isFullscreen(lp.systemUiVisibility, lp.flags, lp.xpFlags));
        buffer.append(" sharedId=");
        buffer.append(lp.sharedId);
        buffer.append(" displayId=");
        buffer.append(lp.displayId);
        buffer.append(" dialog=");
        buffer.append(isApplicationAlertWindowType(lp));
        buffer.append(" flags=0x");
        buffer.append(Integer.toHexString(lp.flags));
        buffer.append(" xpFlags=0x");
        buffer.append(Integer.toHexString(lp.xpFlags));
        buffer.append(" intentFlags=0x");
        buffer.append(Integer.toHexString(lp.intentFlags));
        buffer.append(" privateFlags=0x");
        buffer.append(Integer.toHexString(lp.privateFlags));
        return buffer.toString();
    }

    public static boolean isDesktopHome() {
        return FeatureOption.FO_HOME_DESKTOP_SUPPORT;
    }

    public static boolean shouldAlwaysVisible(int activityType) {
        return activityType == 2 && isDesktopHome();
    }

    public static boolean shouldVisibleWhenPaused(int pausingWindow, int resumingFlags) {
        if (pausingWindow != 5) {
            return false;
        }
        boolean isPanel = (resumingFlags & 256) == 256;
        boolean isDialog = (resumingFlags & 128) == 128;
        return isPanel || isDialog;
    }

    public static boolean shouldVisibleWhenPaused(Context context, ComponentName component) {
        if (context == null || component == null) {
            return false;
        }
        int flags = ActivityInfoManager.getActivityFlags(xpActivityInfo.create(component));
        boolean isPanel = (flags & 256) == 256;
        boolean isDialog = (flags & 128) == 128;
        return isPanel || isDialog;
    }

    public static boolean keepActivityShown(ActivityThread.ActivityClientRecord r, boolean show) {
        if (isDesktopHome() && r != null && r.getIntent().hasCategory(Intent.CATEGORY_HOME)) {
            return true;
        }
        return show;
    }

    public static boolean skipSystemUiVisibility(String processName) {
        String[] strArr;
        if (!TextUtils.isEmpty(processName)) {
            for (String val : SKIP_SYSTEMUI_VISIBILITY_PROCESS) {
                if (processName.equals(val)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean packagesLunchFullscreen(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            IBinder b = ServiceManager.getService("package");
            IPackageManager pm = IPackageManager.Stub.asInterface(b);
            xpPackageInfo info = pm.getXpPackageInfo(packageName);
            boolean fullscreen = xpPackageInfo.isFullscreen(info);
            return fullscreen;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isComponentValid(ComponentName component) {
        return (component == null || TextUtils.isEmpty(component.getPackageName()) || TextUtils.isEmpty(component.getClassName())) ? false : true;
    }

    public static Rect getDisplayBounds(Context context) {
        if (context != null) {
            try {
                Rect rect = new Rect();
                DisplayMetrics metrics = new DisplayMetrics();
                DisplayManagerGlobal.getInstance().getRealDisplay(0).getRealMetrics(metrics);
                rect.set(0, 0, metrics.widthPixels, metrics.heightPixels);
                return rect;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static Rect getDisplayBounds() {
        Rect rect = new Rect();
        rect.setEmpty();
        try {
            Rect out = getWindowManager().getDisplayBounds();
            rect.set(out);
        } catch (Exception e) {
        }
        return rect;
    }

    public static Size getMeasuredWindowSize(Context context, WindowManager.LayoutParams lp) {
        Rect bounds;
        Rect bounds2;
        if (context == null || lp == null) {
            return null;
        }
        int i = lp.type;
        if (i == 12) {
            try {
                boolean isDialog = isAlertWindowType(lp);
                if (isDialog && (bounds = getWindowManager().getXuiRectByType(lp.type)) != null) {
                    return new Size(bounds.width(), bounds.height());
                }
            } catch (Exception e) {
            }
        } else if (i == 2005) {
            String packageName = !TextUtils.isEmpty(lp.packageName) ? lp.packageName : context.getBasePackageName();
            boolean isApplet = MiniProgramManager.isMiniProgram(packageName);
            if (!isApplet && (bounds2 = getDisplayBounds()) != null) {
                return new Size(bounds2.width(), bounds2.height());
            }
        }
        return null;
    }

    public static WindowInfo getWindowInfo(WindowManager.LayoutParams windowLp) {
        return getWindowInfo(windowLp, null);
    }

    public static WindowInfo getWindowInfo(WindowManager.LayoutParams windowLp, WindowManager.LayoutParams parentLp) {
        int parentUiVisibility;
        Rect rect;
        Rect rect2;
        try {
            IBinder binder = ServiceManager.getService(Context.WINDOW_SERVICE);
            IWindowManager wm = binder != null ? IWindowManager.Stub.asInterface(binder) : null;
            if (wm != null && windowLp != null) {
                try {
                    WindowInfo info = new WindowInfo();
                    int sharedId = windowLp.sharedId;
                    int activityFlags = windowLp.intentFlags;
                    int systemUiVisibility = windowLp.systemUiVisibility | windowLp.subtreeSystemUiVisibility;
                    if (parentLp == null) {
                        parentUiVisibility = 0;
                    } else {
                        try {
                            parentUiVisibility = parentLp.systemUiVisibility | parentLp.subtreeSystemUiVisibility;
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    info.windowType = windowLp.type;
                    info.parentType = parentLp != null ? parentLp.type : windowLp.parentType;
                    boolean panel = (activityFlags & 256) == 256;
                    boolean windowFullscreen = isFullscreen(systemUiVisibility, windowLp.flags, parentLp != null ? parentLp.xpFlags : 0);
                    boolean parentFullscreen = isFullscreen(parentUiVisibility, parentLp != null ? parentLp.flags : 0, parentLp != null ? parentLp.xpFlags : 0);
                    boolean physicalFullscreen = (activityFlags & 32) == 32;
                    boolean application = isApplicationWindowType(windowLp);
                    boolean systemAlert = isSystemAlertWindowType(windowLp);
                    boolean applicationAlert = isApplicationAlertWindowType(windowLp);
                    WindowFrameModel model = SharedDisplayManager.getWindowFrame(windowLp);
                    if (model == null) {
                        return null;
                    }
                    Rect bounds = model.windowBounds;
                    try {
                        if (application) {
                            info.roundCorner = wm.getXuiRoundCorner(2);
                            info.rect = model.contentBounds;
                            info.dimmerBounds = windowFullscreen ? model.unrestrictedBounds : model.windowBounds;
                            if (panel && SharedDisplayManager.sharedValid(sharedId)) {
                                info.dimmerBounds = model.unrestrictedBounds;
                            }
                            int i = info.windowType;
                            if (i != 5) {
                                if (i == 6) {
                                    info.touchRegion = windowFullscreen ? model.unrestrictedBounds : bounds;
                                } else if (i != 10) {
                                    if (i == 12) {
                                        info.touchRegion = bounds;
                                    }
                                } else if (rectValid(model.contentBounds)) {
                                    info.touchRegion = windowFullscreen ? model.unrestrictedBounds : model.contentBounds;
                                }
                            } else if (physicalFullscreen) {
                                info.touchRegion = new Rect(model.physicalBounds);
                                return info;
                            } else {
                                Rect appBounds = wm.getXuiRectByType(2);
                                if (appBounds != null && bounds != null) {
                                    info.touchRegion = new Rect(appBounds);
                                    if (!bounds.equals(appBounds)) {
                                        info.touchRegion.bottom = bounds.bottom;
                                    }
                                    return info;
                                }
                            }
                        } else if (systemAlert) {
                            boolean overlayWindow = info.windowType == 2038;
                            WindowFrameModel frameModel = getTargetWindowFrame(2008, windowLp);
                            if (frameModel != null) {
                                info.roundCorner = wm.getXuiRoundCorner(2);
                                info.rect = frameModel.contentBounds;
                                info.dimmerBounds = model.unrestrictedBounds;
                                if (!overlayWindow) {
                                    info.touchRegion = model.unrestrictedBounds;
                                }
                            }
                        } else if (applicationAlert) {
                            int type = info.parentType != 0 ? info.parentType : info.windowType;
                            WindowFrameModel frameModel2 = getTargetWindowFrame(type, windowLp);
                            if (frameModel2 != null) {
                                info.roundCorner = wm.getXuiRoundCorner(type);
                                info.rect = frameModel2.contentBounds;
                                if (!windowFullscreen && !parentFullscreen) {
                                    rect2 = info.rect;
                                    info.dimmerBounds = rect2;
                                }
                                rect2 = model.unrestrictedBounds;
                                info.dimmerBounds = rect2;
                            }
                            int i2 = info.windowType;
                            if (i2 != 9) {
                                if (i2 != 10) {
                                    if (i2 == 12) {
                                        if (windowFullscreen) {
                                            rect = model.unrestrictedBounds;
                                        } else {
                                            rect = bounds;
                                        }
                                        info.touchRegion = rect;
                                    }
                                } else if (rectValid(model.contentBounds)) {
                                    info.touchRegion = windowFullscreen ? model.unrestrictedBounds : model.contentBounds;
                                    info.dimmerBounds = windowFullscreen ? model.unrestrictedBounds : model.contentBounds;
                                }
                            } else if (windowFullscreen) {
                                info.touchRegion = model.unrestrictedBounds;
                            } else if (info.parentType == 5) {
                                WindowInfo homeInfo = getWindowInfo(new WindowManager.LayoutParams(5));
                                if (homeInfo != null) {
                                    info.touchRegion = homeInfo.touchRegion;
                                }
                            } else {
                                info.touchRegion = windowFullscreen ? model.unrestrictedBounds : wm.getXuiRectByType(2);
                            }
                        } else {
                            info.roundCorner = null;
                            info.rect = model.contentBounds;
                            info.dimmerBounds = windowFullscreen ? null : model.windowBounds;
                            info.touchRegion = windowFullscreen ? model.unrestrictedBounds : info.touchRegion;
                        }
                        return info;
                    } catch (Exception e2) {
                        return null;
                    }
                } catch (Exception e3) {
                    return null;
                }
            }
            return null;
        } catch (Exception e4) {
            return null;
        }
    }

    public static WindowFrameModel getTargetWindowFrame(int type, WindowManager.LayoutParams lp) {
        if (lp != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.copyFrom(lp);
            params.type = type;
            return SharedDisplayManager.getWindowFrame(params);
        }
        return null;
    }

    public static boolean rectValid(Rect rect) {
        return (rect == null || rect.isEmpty()) ? false : true;
    }

    public static boolean isAlertWindowType(WindowManager.LayoutParams lp) {
        return isSystemAlertWindowType(lp) || isApplicationAlertWindowType(lp);
    }

    public static boolean isApplicationWindowType(int windowType) {
        if (windowType >= 1 && windowType <= 99) {
            return true;
        }
        return false;
    }

    public static boolean isApplicationWindowType(WindowManager.LayoutParams lp) {
        if (lp != null) {
            int i = lp.type;
            if (i != 10 && i != 12) {
                switch (i) {
                }
            }
            boolean isDialog = (lp.xpFlags & 1) == 1;
            return !isDialog;
        }
        return false;
    }

    public static boolean isSystemAlertWindowType(int windowType) {
        if (windowType == 2002 || windowType == 2003 || windowType == 2010 || windowType == 2038 || windowType == 2047 || windowType == 2048) {
            return true;
        }
        switch (windowType) {
            case 2006:
            case 2007:
            case 2008:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSystemAlertWindowType(WindowManager.LayoutParams lp) {
        if (lp != null) {
            return isSystemAlertWindowType(lp.type);
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0015, code lost:
        if (r1 != 12) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isApplicationAlertWindowType(android.view.WindowManager.LayoutParams r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L24
            int r1 = r4.type
            r2 = 1
            if (r1 == r2) goto L19
            r3 = 2
            if (r1 == r3) goto L19
            r3 = 9
            if (r1 == r3) goto L18
            r3 = 10
            if (r1 == r3) goto L19
            r3 = 12
            if (r1 == r3) goto L19
            goto L24
        L18:
            return r2
        L19:
            int r1 = r4.xpFlags
            r1 = r1 & r2
            if (r1 != r2) goto L20
            r1 = r2
            goto L21
        L20:
            r1 = r0
        L21:
            if (r1 == 0) goto L24
            return r2
        L24:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaopeng.view.xpWindowManager.isApplicationAlertWindowType(android.view.WindowManager$LayoutParams):boolean");
    }

    public static boolean isDialogWindowType(WindowManager.LayoutParams lp) {
        return isSystemDialogWindowType(lp) || isApplicationAlertWindowType(lp);
    }

    public static boolean isSystemDialogWindowType(WindowManager.LayoutParams lp) {
        if (lp != null) {
            return isSystemDialogWindowType(lp.type);
        }
        return false;
    }

    public static boolean isSystemDialogWindowType(int windowType) {
        if (windowType == 2002 || windowType == 2003 || windowType == 2008 || windowType == 2010 || windowType == 2047 || windowType == 2048) {
            return true;
        }
        return false;
    }

    public static boolean isTranslucentOrFloating(WindowManager.LayoutParams lp) {
        if (lp == null) {
            return false;
        }
        int xpFlags = lp.xpFlags;
        boolean isFloating = (xpFlags & 64) == 64;
        boolean isTranslucent = (xpFlags & 128) == 128;
        return isFloating || isTranslucent;
    }

    public static boolean isTranslucentOrFloating(TypedArray attributes) {
        return ActivityInfo.isTranslucentOrFloating(attributes);
    }

    public static boolean shouldNotTouchModal(int windowType) {
        if (SharedDisplayManager.enable()) {
            return isSystemAlertWindowType(windowType);
        }
        return false;
    }

    public static boolean useWindowAnimations(String packageName) {
        return ActivityInfoManager.isSystemApplication(packageName);
    }

    public static Activity getDialogOwnerActivity(Dialog dialog) {
        if (dialog != null) {
            try {
                Context context = dialog.getContext();
                Activity activity = dialog.getOwnerActivity();
                while (activity == null && context != null) {
                    if (context instanceof Activity) {
                        activity = (Activity) context;
                    } else if (context instanceof ContextWrapper) {
                        context = ((ContextWrapper) context).getBaseContext();
                    } else {
                        context = null;
                    }
                }
                return activity;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static int getDialogOwnerActivityType(Dialog dialog) {
        Activity activity = getDialogOwnerActivity(dialog);
        if (activity == null) {
            return 0;
        }
        int windowType = activity.getWindow().getAttributes().type;
        return windowType;
    }

    public static boolean canAddToastWindow(int uid, String packageName) {
        boolean systemUid = uid == 1000;
        boolean systemPkg = ActivityInfoManager.isSystemApplication(packageName);
        return systemUid || systemPkg;
    }

    public static boolean canResizeToFullscreen(Dialog dialog) {
        if (dialog != null && dialog.getWindow() != null) {
            try {
                Activity owner = getDialogOwnerActivity(dialog);
                WindowManager.LayoutParams dlp = dialog.getWindow().getAttributes();
                if (dlp != null && owner != null && owner.getWindow() != null) {
                    WindowManager.LayoutParams olp = owner.getWindow().getAttributes();
                    boolean isOwnerFullscreen = isFullscreen(olp.systemUiVisibility, olp.flags, olp.xpFlags);
                    if (isOwnerFullscreen) {
                        return true;
                    }
                    if (dlp.type == 10) {
                        return false;
                    }
                    return true;
                }
                return true;
            } catch (Exception e) {
                return true;
            }
        }
        return true;
    }

    public static boolean shouldCropRoundCorner(WindowManager.LayoutParams lp) {
        if (lp == null) {
            return false;
        }
        boolean isApplication = isApplicationWindowType(lp);
        boolean isSystemApplication = ActivityInfoManager.isSystemApplication(lp.packageName);
        return isApplication && !isSystemApplication;
    }

    public static void setRoundCorner(SurfaceControl sc) {
        setRoundCorner(sc, ROUND_CORNER_RADIUS);
    }

    public static void setRoundCorner(SurfaceControl sc, int radius) {
        if (sc != null) {
            sc.setCornerRadius(radius);
        }
    }

    public static long getToastDurationMillis(int duration) {
        if (duration != 0) {
            if (duration != 1) {
                if (duration != 2) {
                    return TOAST_DURATION_SHORT;
                }
                return TOAST_DURATION_LONGER;
            }
            return TOAST_DURATION_LONG;
        }
        return TOAST_DURATION_SHORT;
    }

    public static IWindowManager getWindowManager() {
        IBinder b = ServiceManager.getService(Context.WINDOW_SERVICE);
        return IWindowManager.Stub.asInterface(b);
    }

    public static void triggerWindowErrorEvent(Context context, Object object) {
        if (object != null && context != null) {
            try {
                WindowErrorInfo info = new WindowErrorInfo();
                if (object instanceof Dialog) {
                    Dialog dialog = (Dialog) object;
                    info.errorType = "dialog";
                    info.className = dialog.getClass().toString();
                    info.packageName = dialog.getContext().getBasePackageName();
                } else if (object instanceof ActivityInfo) {
                    ActivityInfo ai = (ActivityInfo) object;
                    ComponentName component = ai.getComponentName();
                    info.errorType = Context.ACTIVITY_SERVICE;
                    info.className = component != null ? component.getClassName() : "";
                    info.packageName = component != null ? component.getPackageName() : "";
                }
                if (!TextUtils.isEmpty(info.packageName) && !TextUtils.isEmpty(info.className)) {
                    sendWindowErrorBroadcast(context, info);
                }
            } catch (Exception e) {
            }
        }
    }

    public static void sendWindowErrorBroadcast(Context context, WindowErrorInfo info) {
        try {
            Intent intent = new Intent("com.xiaopeng.intent.action.XUI_WINDOW_ERROR");
            intent.addFlags(1344274432);
            if (info != null && !TextUtils.isEmpty(info.packageName)) {
                intent.setPackage(info.packageName);
                intent.putExtra("android.intent.extra.errorType", info.errorType);
                intent.putExtra("android.intent.extra.className", info.className);
                intent.putExtra("android.intent.extra.packageName", info.packageName);
                xpLogger.i(TAG, "sendWindowErrorBroadcast errorType=" + info.errorType + " className=" + info.className + " packageName=" + info.packageName);
            }
            context.sendBroadcastAsUser(intent, UserHandle.ALL);
        } catch (Exception e) {
        }
    }

    /* loaded from: classes3.dex */
    public static final class UnityWindowParams {
        public static boolean isDialogWindowType(int type) {
            if (type == 4 || type == 5 || type == 6 || type == 7) {
                return true;
            }
            return false;
        }

        public static boolean isDialogCancelable(int type) {
            if (type == 4 || type == 6) {
                return true;
            }
            return false;
        }

        public static boolean isDialogNotCancelable(int type) {
            if (type == 5 || type == 7) {
                return true;
            }
            return false;
        }

        public static boolean shouldConfirmSlideEvent(int mode) {
            if (mode != 2) {
                switch (mode) {
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 13:
                    case 14:
                    default:
                        return false;
                    case 12:
                        return true;
                }
            }
            return false;
        }

        public static boolean allowDialog(int mode, int type) {
            return type != 18;
        }
    }

    /* loaded from: classes3.dex */
    public static class UnityAppParams {
        public static final int App5DFilm = 11;
        public static final int AppAirCondition = 22;
        public static final int AppApPark = 18;
        public static final int AppApParkDesktop = 34;
        public static final int AppBSite = 28;
        public static final int AppCarContorl = 23;
        public static final int AppChildWatch = 21;
        public static final int AppClean = 13;
        public static final int AppDesktop = 16;
        public static final int AppFilm = 9;
        public static final int AppFull = 1;
        public static final int AppFullModel = 2;
        public static final int AppIQIY = 27;
        public static final int AppMakeup = 12;
        public static final int AppMeditation = 8;
        public static final int AppModelSuperSysDialog = 7;
        public static final int AppModelSysDialog = 5;
        public static final int AppMusic = 24;
        public static final int AppNoModelSuperSysDialog = 6;
        public static final int AppNoModelSysDialog = 4;
        public static final int AppNormal = 0;
        public static final int AppQuick = 3;
        public static final int AppQuick2 = 17;
        public static final int AppRescueMode = 20;
        public static final int AppSleep = 10;
        public static final int AppSystemUI = 15;
        public static final int AppThird = 19;
        public static final int AppVipSeat = 26;
        public static final int AppVoiceContainer = 25;
        public static final int AppWait = 14;
        public static final int AppWndMax = 29;
        public static final int DepthApp = 20;
        public static final int DepthAppMode = 40;
        public static final int DepthDesktop = 10;
        public static final int DepthSysDialog = 70;
        public static final int DepthSystemUI = 30;
        public static final int DepthToast = 80;
        public static final int Event_AppActive = 4;
        public static final int Event_AppClosing = 10;
        public static final int Event_AppCover = 6;
        public static final int Event_AppHalfCover = 7;
        public static final int Event_AppOpening = 11;
        public static final int Event_AppResume = 3;
        public static final int Event_AppStart = 1;
        public static final int Event_AppSupend = 2;
        public static final int Event_AppSwipBegin = 8;
        public static final int Event_AppSwipEnd = 9;
        public static final int Event_AppUnActive = 5;
        public static final int Major = 0;
        public static final int Minor = 1;
        public static final int SysDlgNormal = 0;
        public static final int SysDlgPhone = 1;
        public static final int SysDlgSupper = 3;
        public static final int SysDlgWarning = 2;
        public static final int WidthFull = 2;

        public static int ModelType(int appType) {
            if (appType == 2 || (appType >= 8 && appType <= 13)) {
                return appType;
            }
            return -1;
        }

        public static boolean IsFullModel(int appType) {
            return appType == 2 || (appType >= 8 && appType <= 11) || (appType >= 13 && appType <= 14);
        }

        public static boolean IsDropDown(int appType) {
            return appType == 3 || appType >= 17;
        }

        public static boolean IsPopupDialog(int appType) {
            return appType >= 4 && appType <= 7;
        }

        public static boolean IsModelDialog(int appType) {
            return appType == 5 || appType == 7;
        }

        public static boolean IsSmallApp(int appType) {
            return appType == 25;
        }
    }
}
