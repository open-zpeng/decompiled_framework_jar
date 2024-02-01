package android.content.pm;
/* loaded from: classes.dex */
public interface RegisteredServicesCacheListener<V> {
    synchronized void onServiceChanged(V v, int i, boolean z);
}
