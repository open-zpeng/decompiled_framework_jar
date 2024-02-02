package android.appwidget;

import android.util.ArraySet;
/* loaded from: classes.dex */
public abstract class AppWidgetManagerInternal {
    public abstract synchronized ArraySet<String> getHostedWidgetPackages(int i);

    public abstract void unlockUser(int i);
}
