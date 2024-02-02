package android.media.update;
/* loaded from: classes2.dex */
public interface VolumeProvider2Provider {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getControlType_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getCurrentVolume_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getMaxVolume_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setCurrentVolume_impl(int i);
}
