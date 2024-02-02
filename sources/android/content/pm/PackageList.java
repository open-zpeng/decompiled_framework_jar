package android.content.pm;

import android.content.pm.PackageManagerInternal;
import com.android.server.LocalServices;
import java.util.List;
/* loaded from: classes.dex */
public class PackageList implements PackageManagerInternal.PackageListObserver, AutoCloseable {
    private final List<String> mPackageNames;
    private final PackageManagerInternal.PackageListObserver mWrappedObserver;

    public synchronized PackageList(List<String> packageNames, PackageManagerInternal.PackageListObserver observer) {
        this.mPackageNames = packageNames;
        this.mWrappedObserver = observer;
    }

    @Override // android.content.pm.PackageManagerInternal.PackageListObserver
    public synchronized void onPackageAdded(String packageName) {
        if (this.mWrappedObserver != null) {
            this.mWrappedObserver.onPackageAdded(packageName);
        }
    }

    @Override // android.content.pm.PackageManagerInternal.PackageListObserver
    public synchronized void onPackageRemoved(String packageName) {
        if (this.mWrappedObserver != null) {
            this.mWrappedObserver.onPackageRemoved(packageName);
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() throws Exception {
        ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).removePackageListObserver(this);
    }

    public synchronized List<String> getPackageNames() {
        return this.mPackageNames;
    }
}
