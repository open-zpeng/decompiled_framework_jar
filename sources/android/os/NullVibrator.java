package android.os;

import android.media.AudioAttributes;
/* loaded from: classes2.dex */
public class NullVibrator extends Vibrator {
    private static final NullVibrator sInstance = new NullVibrator();

    private synchronized NullVibrator() {
    }

    public static synchronized NullVibrator getInstance() {
        return sInstance;
    }

    @Override // android.os.Vibrator
    public boolean hasVibrator() {
        return false;
    }

    @Override // android.os.Vibrator
    public boolean hasAmplitudeControl() {
        return false;
    }

    @Override // android.os.Vibrator
    public synchronized void vibrate(int uid, String opPkg, VibrationEffect effect, AudioAttributes attributes) {
    }

    @Override // android.os.Vibrator
    public void cancel() {
    }
}
