package android.media.update;
@FunctionalInterface
/* loaded from: classes2.dex */
public interface ProviderCreator<T, U> {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized U createProvider(T t);
}
