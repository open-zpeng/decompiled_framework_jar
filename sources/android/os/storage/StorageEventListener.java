package android.os.storage;
/* loaded from: classes2.dex */
public class StorageEventListener {
    private protected StorageEventListener() {
    }

    private protected void onUsbMassStorageConnectionChanged(boolean connected) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStorageStateChanged(String path, String oldState, String newState) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVolumeStateChanged(VolumeInfo vol, int oldState, int newState) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVolumeRecordChanged(VolumeRecord rec) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVolumeForgotten(String fsUuid) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDiskScanned(DiskInfo disk, int volumeCount) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDiskDestroyed(DiskInfo disk) {
    }
}
