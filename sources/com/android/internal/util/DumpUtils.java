package com.android.internal.util;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.text.TextUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.function.Predicate;

/* loaded from: classes3.dex */
public final class DumpUtils {
    public static final ComponentName[] CRITICAL_SECTION_COMPONENTS = {new ComponentName("com.android.systemui", "com.android.systemui.SystemUIService")};
    private static final boolean DEBUG = false;
    private static final String TAG = "DumpUtils";

    /* loaded from: classes3.dex */
    public interface Dump {
        void dump(PrintWriter printWriter, String str);
    }

    private DumpUtils() {
    }

    public static void dumpAsync(Handler handler, final Dump dump, PrintWriter pw, final String prefix, long timeout) {
        final StringWriter sw = new StringWriter();
        if (handler.runWithScissors(new Runnable() { // from class: com.android.internal.util.DumpUtils.1
            @Override // java.lang.Runnable
            public void run() {
                PrintWriter lpw = new FastPrintWriter(sw);
                dump.dump(lpw, prefix);
                lpw.close();
            }
        }, timeout)) {
            pw.print(sw.toString());
        } else {
            pw.println("... timed out");
        }
    }

    private static void logMessage(PrintWriter pw, String msg) {
        pw.println(msg);
    }

    public static boolean checkDumpPermission(Context context, String tag, PrintWriter pw) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.DUMP) != 0) {
            logMessage(pw, "Permission Denial: can't dump " + tag + " from from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + " due to missing android.permission.DUMP permission");
            return false;
        }
        return true;
    }

    public static boolean checkUsageStatsPermission(Context context, String tag, PrintWriter pw) {
        int uid = Binder.getCallingUid();
        if (uid == 0 || uid == 1000 || uid == 1067 || uid == 2000) {
            return true;
        }
        if (context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) != 0) {
            logMessage(pw, "Permission Denial: can't dump " + tag + " from from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + " due to missing android.permission.PACKAGE_USAGE_STATS permission");
            return false;
        }
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        String[] pkgs = context.getPackageManager().getPackagesForUid(uid);
        if (pkgs != null) {
            for (String pkg : pkgs) {
                int noteOpNoThrow = appOps.noteOpNoThrow(43, uid, pkg);
                if (noteOpNoThrow == 0 || noteOpNoThrow == 3) {
                    return true;
                }
            }
        }
        logMessage(pw, "Permission Denial: can't dump " + tag + " from from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid() + " due to android:get_usage_stats app-op not allowed");
        return false;
    }

    public static boolean checkDumpAndUsageStatsPermission(Context context, String tag, PrintWriter pw) {
        return checkDumpPermission(context, tag, pw) && checkUsageStatsPermission(context, tag, pw);
    }

    public static boolean isPlatformPackage(String packageName) {
        return packageName != null && (packageName.equals("android") || packageName.startsWith("android.") || packageName.startsWith("com.android."));
    }

    public static boolean isPlatformPackage(ComponentName cname) {
        return cname != null && isPlatformPackage(cname.getPackageName());
    }

    public static boolean isPlatformPackage(ComponentName.WithComponentName wcn) {
        return wcn != null && isPlatformPackage(wcn.getComponentName());
    }

    public static boolean isNonPlatformPackage(String packageName) {
        return (packageName == null || isPlatformPackage(packageName)) ? false : true;
    }

    public static boolean isNonPlatformPackage(ComponentName cname) {
        return cname != null && isNonPlatformPackage(cname.getPackageName());
    }

    public static boolean isNonPlatformPackage(ComponentName.WithComponentName wcn) {
        return (wcn == null || isPlatformPackage(wcn.getComponentName())) ? false : true;
    }

    private static boolean isCriticalPackage(ComponentName cname) {
        if (cname == null) {
            return false;
        }
        int i = 0;
        while (true) {
            ComponentName[] componentNameArr = CRITICAL_SECTION_COMPONENTS;
            if (i >= componentNameArr.length) {
                return false;
            }
            if (!cname.equals(componentNameArr[i])) {
                i++;
            } else {
                return true;
            }
        }
    }

    public static boolean isPlatformCriticalPackage(ComponentName.WithComponentName wcn) {
        return wcn != null && isPlatformPackage(wcn.getComponentName()) && isCriticalPackage(wcn.getComponentName());
    }

    public static boolean isPlatformNonCriticalPackage(ComponentName.WithComponentName wcn) {
        return (wcn == null || !isPlatformPackage(wcn.getComponentName()) || isCriticalPackage(wcn.getComponentName())) ? false : true;
    }

    public static <TRec extends ComponentName.WithComponentName> Predicate<TRec> filterRecord(final String filterString) {
        if (TextUtils.isEmpty(filterString)) {
            return new Predicate() { // from class: com.android.internal.util.-$$Lambda$DumpUtils$D1OlZP6xIpu72ypnJd0fzx0wd6I
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DumpUtils.lambda$filterRecord$0((ComponentName.WithComponentName) obj);
                }
            };
        }
        if ("all".equals(filterString)) {
            return new Predicate() { // from class: com.android.internal.util.-$$Lambda$eRa1rlfDk6Og2yFeXGHqUGPzRF0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Objects.nonNull((ComponentName.WithComponentName) obj);
                }
            };
        }
        if ("all-platform".equals(filterString)) {
            return new Predicate() { // from class: com.android.internal.util.-$$Lambda$kVylv1rl9MOSbHFZoVyK5dl1kfY
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DumpUtils.isPlatformPackage((ComponentName.WithComponentName) obj);
                }
            };
        }
        if ("all-non-platform".equals(filterString)) {
            return new Predicate() { // from class: com.android.internal.util.-$$Lambda$JwOUSWW2-Jzu15y4Kn4JuPh8tWM
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DumpUtils.isNonPlatformPackage((ComponentName.WithComponentName) obj);
                }
            };
        }
        if ("all-platform-critical".equals(filterString)) {
            return new Predicate() { // from class: com.android.internal.util.-$$Lambda$grRTg3idX3yJe9Zyx-tmLBiD1DM
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DumpUtils.isPlatformCriticalPackage((ComponentName.WithComponentName) obj);
                }
            };
        }
        if ("all-platform-non-critical".equals(filterString)) {
            return new Predicate() { // from class: com.android.internal.util.-$$Lambda$TCbPpgWlKJUHZgFKCczglAvxLfw
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DumpUtils.isPlatformNonCriticalPackage((ComponentName.WithComponentName) obj);
                }
            };
        }
        final ComponentName filterCname = ComponentName.unflattenFromString(filterString);
        if (filterCname != null) {
            return new Predicate() { // from class: com.android.internal.util.-$$Lambda$DumpUtils$X8irOs5hfloCKy89_l1HRA1QeG0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DumpUtils.lambda$filterRecord$1(ComponentName.this, (ComponentName.WithComponentName) obj);
                }
            };
        }
        final int id = ParseUtils.parseIntWithBase(filterString, 16, -1);
        return new Predicate() { // from class: com.android.internal.util.-$$Lambda$DumpUtils$vCLO_0ezRxkpSERUWCFrJ0ph5jg
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DumpUtils.lambda$filterRecord$2(id, filterString, (ComponentName.WithComponentName) obj);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$filterRecord$0(ComponentName.WithComponentName rec) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$filterRecord$1(ComponentName filterCname, ComponentName.WithComponentName rec) {
        return rec != null && filterCname.equals(rec.getComponentName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$filterRecord$2(int id, String filterString, ComponentName.WithComponentName rec) {
        ComponentName cn = rec.getComponentName();
        return (id != -1 && System.identityHashCode(rec) == id) || cn.flattenToString().toLowerCase().contains(filterString.toLowerCase());
    }
}
