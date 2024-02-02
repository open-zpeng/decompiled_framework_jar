package android.os;

import android.app.ActivityThread;
import android.content.Context;
import android.media.AudioAttributes;
import android.util.Log;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes2.dex */
public abstract class Vibrator {
    private static final String TAG = "Vibrator";
    public static final int VIBRATION_INTENSITY_HIGH = 3;
    public static final int VIBRATION_INTENSITY_LOW = 1;
    public static final int VIBRATION_INTENSITY_MEDIUM = 2;
    public static final int VIBRATION_INTENSITY_OFF = 0;
    private final int mDefaultHapticFeedbackIntensity;
    private final int mDefaultNotificationVibrationIntensity;
    private final String mPackageName;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface VibrationIntensity {
    }

    public abstract void cancel();

    public abstract boolean hasAmplitudeControl();

    public abstract boolean hasVibrator();

    public abstract synchronized void vibrate(int i, String str, VibrationEffect vibrationEffect, AudioAttributes audioAttributes);

    /* JADX INFO: Access modifiers changed from: private */
    public Vibrator() {
        this.mPackageName = ActivityThread.currentPackageName();
        Context ctx = ActivityThread.currentActivityThread().getSystemContext();
        this.mDefaultHapticFeedbackIntensity = loadDefaultIntensity(ctx, R.integer.config_defaultHapticFeedbackIntensity);
        this.mDefaultNotificationVibrationIntensity = loadDefaultIntensity(ctx, R.integer.config_defaultNotificationVibrationIntensity);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized Vibrator(Context context) {
        this.mPackageName = context.getOpPackageName();
        this.mDefaultHapticFeedbackIntensity = loadDefaultIntensity(context, R.integer.config_defaultHapticFeedbackIntensity);
        this.mDefaultNotificationVibrationIntensity = loadDefaultIntensity(context, R.integer.config_defaultNotificationVibrationIntensity);
    }

    private synchronized int loadDefaultIntensity(Context ctx, int resId) {
        if (ctx != null) {
            return ctx.getResources().getInteger(resId);
        }
        return 2;
    }

    public synchronized int getDefaultHapticFeedbackIntensity() {
        return this.mDefaultHapticFeedbackIntensity;
    }

    public synchronized int getDefaultNotificationVibrationIntensity() {
        return this.mDefaultNotificationVibrationIntensity;
    }

    @Deprecated
    public void vibrate(long milliseconds) {
        vibrate(milliseconds, (AudioAttributes) null);
    }

    @Deprecated
    public void vibrate(long milliseconds, AudioAttributes attributes) {
        try {
            VibrationEffect effect = VibrationEffect.createOneShot(milliseconds, -1);
            vibrate(effect, attributes);
        } catch (IllegalArgumentException iae) {
            Log.e(TAG, "Failed to create VibrationEffect", iae);
        }
    }

    @Deprecated
    public void vibrate(long[] pattern, int repeat) {
        vibrate(pattern, repeat, null);
    }

    @Deprecated
    public void vibrate(long[] pattern, int repeat, AudioAttributes attributes) {
        if (repeat < -1 || repeat >= pattern.length) {
            Log.e(TAG, "vibrate called with repeat index out of bounds (pattern.length=" + pattern.length + ", index=" + repeat + ")");
            throw new ArrayIndexOutOfBoundsException();
        }
        try {
            vibrate(VibrationEffect.createWaveform(pattern, repeat), attributes);
        } catch (IllegalArgumentException iae) {
            Log.e(TAG, "Failed to create VibrationEffect", iae);
        }
    }

    public void vibrate(VibrationEffect vibe) {
        vibrate(vibe, (AudioAttributes) null);
    }

    public void vibrate(VibrationEffect vibe, AudioAttributes attributes) {
        vibrate(Process.myUid(), this.mPackageName, vibe, attributes);
    }
}
