package android.content.res;

import android.content.res.Resources;
import android.util.ArrayMap;
import android.util.LongSparseArray;
import java.lang.ref.WeakReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ThemedResourceCache<T> {
    private LongSparseArray<WeakReference<T>> mNullThemedEntries;
    public protected ArrayMap<Resources.ThemeKey, LongSparseArray<WeakReference<T>>> mThemedEntries;
    private LongSparseArray<WeakReference<T>> mUnthemedEntries;

    protected abstract synchronized boolean shouldInvalidateEntry(T t, int i);

    public synchronized void put(long key, Resources.Theme theme, T entry) {
        put(key, theme, entry, true);
    }

    public synchronized void put(long key, Resources.Theme theme, T entry, boolean usesTheme) {
        LongSparseArray<WeakReference<T>> entries;
        if (entry == null) {
            return;
        }
        synchronized (this) {
            try {
                if (!usesTheme) {
                    entries = getUnthemedLocked(true);
                } else {
                    entries = getThemedLocked(theme, true);
                }
                if (entries != null) {
                    entries.put(key, new WeakReference<>(entry));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public synchronized T get(long key, Resources.Theme theme) {
        WeakReference<T> unthemedEntry;
        WeakReference<T> themedEntry;
        LongSparseArray<WeakReference<T>> themedEntries = getThemedLocked(theme, false);
        if (themedEntries != null && (themedEntry = themedEntries.get(key)) != null) {
            return themedEntry.get();
        }
        LongSparseArray<WeakReference<T>> unthemedEntries = getUnthemedLocked(false);
        if (unthemedEntries != null && (unthemedEntry = unthemedEntries.get(key)) != null) {
            return unthemedEntry.get();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConfigurationChange(int configChanges) {
        prune(configChanges);
    }

    private synchronized LongSparseArray<WeakReference<T>> getThemedLocked(Resources.Theme t, boolean create) {
        if (t == null) {
            if (this.mNullThemedEntries == null && create) {
                this.mNullThemedEntries = new LongSparseArray<>(1);
            }
            return this.mNullThemedEntries;
        }
        if (this.mThemedEntries == null) {
            if (create) {
                this.mThemedEntries = new ArrayMap<>(1);
            } else {
                return null;
            }
        }
        Resources.ThemeKey key = t.getKey();
        LongSparseArray<WeakReference<T>> cache = this.mThemedEntries.get(key);
        if (cache == null && create) {
            LongSparseArray<WeakReference<T>> cache2 = new LongSparseArray<>(1);
            Resources.ThemeKey keyClone = key.m20clone();
            this.mThemedEntries.put(keyClone, cache2);
            return cache2;
        }
        return cache;
    }

    private synchronized LongSparseArray<WeakReference<T>> getUnthemedLocked(boolean create) {
        if (this.mUnthemedEntries == null && create) {
            this.mUnthemedEntries = new LongSparseArray<>(1);
        }
        return this.mUnthemedEntries;
    }

    private synchronized boolean prune(int configChanges) {
        boolean z;
        z = true;
        if (this.mThemedEntries != null) {
            for (int i = this.mThemedEntries.size() - 1; i >= 0; i--) {
                if (pruneEntriesLocked(this.mThemedEntries.valueAt(i), configChanges)) {
                    this.mThemedEntries.removeAt(i);
                }
            }
        }
        pruneEntriesLocked(this.mNullThemedEntries, configChanges);
        pruneEntriesLocked(this.mUnthemedEntries, configChanges);
        if (this.mThemedEntries != null || this.mNullThemedEntries != null || this.mUnthemedEntries != null) {
            z = false;
        }
        return z;
    }

    private synchronized boolean pruneEntriesLocked(LongSparseArray<WeakReference<T>> entries, int configChanges) {
        if (entries == null) {
            return true;
        }
        for (int i = entries.size() - 1; i >= 0; i--) {
            WeakReference<T> ref = entries.valueAt(i);
            if (ref == null || pruneEntryLocked(ref.get(), configChanges)) {
                entries.removeAt(i);
            }
        }
        int i2 = entries.size();
        return i2 == 0;
    }

    private synchronized boolean pruneEntryLocked(T entry, int configChanges) {
        return entry == null || (configChanges != 0 && shouldInvalidateEntry(entry, configChanges));
    }
}
