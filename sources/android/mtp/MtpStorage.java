package android.mtp;

import android.os.storage.StorageVolume;
/* loaded from: classes2.dex */
public class MtpStorage {
    private final String mDescription;
    private final long mMaxFileSize;
    private final String mPath;
    private final boolean mRemovable;
    private final int mStorageId;

    public synchronized MtpStorage(StorageVolume volume, int storageId) {
        this.mStorageId = storageId;
        this.mPath = volume.getInternalPath();
        this.mDescription = volume.getDescription(null);
        this.mRemovable = volume.isRemovable();
        this.mMaxFileSize = volume.getMaxFileSize();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getStorageId() {
        return this.mStorageId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String getPath() {
        return this.mPath;
    }

    public final synchronized String getDescription() {
        return this.mDescription;
    }

    public final synchronized boolean isRemovable() {
        return this.mRemovable;
    }

    public synchronized long getMaxFileSize() {
        return this.mMaxFileSize;
    }
}
