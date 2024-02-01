package android.content.pm;

import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import java.util.List;

/* loaded from: classes.dex */
public abstract class ShortcutServiceInternal {

    /* loaded from: classes.dex */
    public interface ShortcutChangeListener {
        void onShortcutChanged(String str, int i);
    }

    public abstract void addListener(ShortcutChangeListener shortcutChangeListener);

    public abstract Intent[] createShortcutIntents(int i, String str, String str2, String str3, int i2, int i3, int i4);

    public abstract ParcelFileDescriptor getShortcutIconFd(int i, String str, String str2, String str3, int i2);

    public abstract int getShortcutIconResId(int i, String str, String str2, String str3, int i2);

    public abstract List<ShortcutInfo> getShortcuts(int i, String str, long j, String str2, List<String> list, ComponentName componentName, int i2, int i3, int i4, int i5);

    public abstract boolean hasShortcutHostPermission(int i, String str, int i2, int i3);

    public abstract boolean isForegroundDefaultLauncher(String str, int i);

    public abstract boolean isPinnedByCaller(int i, String str, String str2, String str3, int i2);

    public abstract boolean isRequestPinItemSupported(int i, int i2);

    public abstract void pinShortcuts(int i, String str, String str2, List<String> list, int i2);

    public abstract boolean requestPinAppWidget(String str, AppWidgetProviderInfo appWidgetProviderInfo, Bundle bundle, IntentSender intentSender, int i);

    public abstract void setShortcutHostPackage(String str, String str2, int i);
}
