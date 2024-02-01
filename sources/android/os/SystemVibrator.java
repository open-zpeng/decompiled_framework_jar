package android.os;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.IVibratorService;
import android.util.Log;
/* loaded from: classes2.dex */
public class SystemVibrator extends Vibrator {
    private static final String TAG = "Vibrator";
    private final IVibratorService mService;
    private final Binder mToken;

    private protected SystemVibrator() {
        this.mToken = new Binder();
        this.mService = IVibratorService.Stub.asInterface(ServiceManager.getService(Context.VIBRATOR_SERVICE));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SystemVibrator(Context context) {
        super(context);
        this.mToken = new Binder();
        this.mService = IVibratorService.Stub.asInterface(ServiceManager.getService(Context.VIBRATOR_SERVICE));
    }

    @Override // android.os.Vibrator
    public boolean hasVibrator() {
        if (this.mService == null) {
            Log.w(TAG, "Failed to vibrate; no vibrator service.");
            return false;
        }
        try {
            return this.mService.hasVibrator();
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override // android.os.Vibrator
    public boolean hasAmplitudeControl() {
        if (this.mService == null) {
            Log.w(TAG, "Failed to check amplitude control; no vibrator service.");
            return false;
        }
        try {
            return this.mService.hasAmplitudeControl();
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override // android.os.Vibrator
    public synchronized void vibrate(int uid, String opPkg, VibrationEffect effect, AudioAttributes attributes) {
        if (this.mService == null) {
            Log.w(TAG, "Failed to vibrate; no vibrator service.");
            return;
        }
        try {
            this.mService.vibrate(uid, opPkg, effect, usageForAttributes(attributes), this.mToken);
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to vibrate.", e);
        }
    }

    private static synchronized int usageForAttributes(AudioAttributes attributes) {
        if (attributes != null) {
            return attributes.getUsage();
        }
        return 0;
    }

    @Override // android.os.Vibrator
    public void cancel() {
        if (this.mService == null) {
            return;
        }
        try {
            this.mService.cancelVibrate(this.mToken);
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to cancel vibration.", e);
        }
    }
}
