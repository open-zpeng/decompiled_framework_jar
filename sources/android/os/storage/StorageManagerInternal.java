package android.os.storage;
/* loaded from: classes2.dex */
public abstract class StorageManagerInternal {

    /* loaded from: classes2.dex */
    public interface ExternalStorageMountPolicy {
        synchronized int getMountMode(int i, String str);

        synchronized boolean hasExternalStorage(int i, String str);
    }

    public abstract synchronized void addExternalStoragePolicy(ExternalStorageMountPolicy externalStorageMountPolicy);

    public abstract synchronized int getExternalStorageMountMode(int i, String str);

    public abstract synchronized void onExternalStoragePolicyChanged(int i, String str);
}
