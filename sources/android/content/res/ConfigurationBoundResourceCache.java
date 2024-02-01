package android.content.res;

import android.annotation.UnsupportedAppUsage;
import android.content.res.Resources;

/* loaded from: classes.dex */
public class ConfigurationBoundResourceCache<T> extends ThemedResourceCache<ConstantState<T>> {
    @Override // android.content.res.ThemedResourceCache
    @UnsupportedAppUsage
    public /* bridge */ /* synthetic */ void onConfigurationChange(int i) {
        super.onConfigurationChange(i);
    }

    @Override // android.content.res.ThemedResourceCache
    public /* bridge */ /* synthetic */ boolean shouldInvalidateEntry(Object obj, int i) {
        return shouldInvalidateEntry((ConstantState) ((ConstantState) obj), i);
    }

    public T getInstance(long key, Resources resources, Resources.Theme theme) {
        ConstantState<T> entry = (ConstantState) get(key, theme);
        if (entry != null) {
            return entry.newInstance(resources, theme);
        }
        return null;
    }

    public boolean shouldInvalidateEntry(ConstantState<T> entry, int configChanges) {
        return Configuration.needNewResources(configChanges, entry.getChangingConfigurations());
    }
}
