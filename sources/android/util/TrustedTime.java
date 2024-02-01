package android.util;
/* loaded from: classes2.dex */
public interface TrustedTime {
    private protected long currentTimeMillis();

    private protected boolean forceRefresh();

    private protected long getCacheAge();

    synchronized long getCacheCertainty();

    private protected boolean hasCache();
}
