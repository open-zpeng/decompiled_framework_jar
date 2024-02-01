package android.media;

import android.media.update.ApiLoader;
import android.media.update.VolumeProvider2Provider;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public abstract class VolumeProvider2 {
    public static final int VOLUME_CONTROL_ABSOLUTE = 2;
    public static final int VOLUME_CONTROL_FIXED = 0;
    public static final int VOLUME_CONTROL_RELATIVE = 1;
    private final VolumeProvider2Provider mProvider;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ControlType {
    }

    public synchronized VolumeProvider2(int controlType, int maxVolume, int currentVolume) {
        this.mProvider = ApiLoader.getProvider().createVolumeProvider2(this, controlType, maxVolume, currentVolume);
    }

    public synchronized VolumeProvider2Provider getProvider() {
        return this.mProvider;
    }

    public final synchronized int getControlType() {
        return this.mProvider.getControlType_impl();
    }

    public final synchronized int getMaxVolume() {
        return this.mProvider.getMaxVolume_impl();
    }

    public final synchronized int getCurrentVolume() {
        return this.mProvider.getCurrentVolume_impl();
    }

    public final synchronized void setCurrentVolume(int currentVolume) {
        this.mProvider.setCurrentVolume_impl(currentVolume);
    }

    public synchronized void onSetVolumeTo(int volume) {
    }

    public synchronized void onAdjustVolume(int direction) {
    }
}
