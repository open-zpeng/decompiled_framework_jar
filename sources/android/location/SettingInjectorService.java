package android.location;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
/* loaded from: classes.dex */
public abstract class SettingInjectorService extends Service {
    public static final String ACTION_INJECTED_SETTING_CHANGED = "android.location.InjectedSettingChanged";
    public static final String ACTION_SERVICE_INTENT = "android.location.SettingInjectorService";
    public static final String ATTRIBUTES_NAME = "injected-location-setting";
    public static final String ENABLED_KEY = "enabled";
    public static final String MESSENGER_KEY = "messenger";
    public static final String META_DATA_NAME = "android.location.SettingInjectorService";
    private static final String TAG = "SettingInjectorService";
    private final String mName;

    protected abstract boolean onGetEnabled();

    @Deprecated
    protected abstract String onGetSummary();

    public SettingInjectorService(String name) {
        this.mName = name;
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public final void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override // android.app.Service
    public final int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        stopSelf(startId);
        return 2;
    }

    private synchronized void onHandleIntent(Intent intent) {
        try {
            boolean enabled = onGetEnabled();
            sendStatus(intent, enabled);
        } catch (RuntimeException e) {
            sendStatus(intent, true);
            throw e;
        }
    }

    private synchronized void sendStatus(Intent intent, boolean enabled) {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putBoolean("enabled", enabled);
        message.setData(bundle);
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, this.mName + ": received " + intent + ", enabled=" + enabled + ", sending message: " + message);
        }
        Messenger messenger = (Messenger) intent.getParcelableExtra("messenger");
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            Log.e(TAG, this.mName + ": sending dynamic status failed", e);
        }
    }
}
