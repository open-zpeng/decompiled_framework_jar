package android.media;

import android.util.IntArray;
/* loaded from: classes.dex */
public abstract class AudioManagerInternal {

    /* loaded from: classes.dex */
    public interface RingerModeDelegate {
        synchronized boolean canVolumeDownEnterSilent();

        synchronized int getRingerModeAffectedStreams(int i);

        synchronized int onSetRingerModeExternal(int i, int i2, String str, int i3, VolumePolicy volumePolicy);

        synchronized int onSetRingerModeInternal(int i, int i2, String str, int i3, VolumePolicy volumePolicy);
    }

    public abstract synchronized void adjustStreamVolumeForUid(int i, int i2, int i3, String str, int i4);

    public abstract synchronized void adjustSuggestedStreamVolumeForUid(int i, int i2, int i3, String str, int i4);

    public abstract synchronized int getRingerModeInternal();

    public abstract synchronized void setAccessibilityServiceUids(IntArray intArray);

    public abstract synchronized void setRingerModeDelegate(RingerModeDelegate ringerModeDelegate);

    public abstract synchronized void setRingerModeInternal(int i, String str);

    public abstract synchronized void setStreamVolumeForUid(int i, int i2, int i3, String str, int i4);

    public abstract synchronized void silenceRingerModeInternal(String str);

    public abstract synchronized void updateRingerModeAffectedStreamsInternal();
}
