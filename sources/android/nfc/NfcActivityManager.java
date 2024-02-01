package android.nfc;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.nfc.IAppCallback;
import android.nfc.NfcAdapter;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/* loaded from: classes2.dex */
public final class NfcActivityManager extends IAppCallback.Stub implements Application.ActivityLifecycleCallbacks {
    static final Boolean DBG = false;
    static final String TAG = "NFC";
    public private protected final NfcAdapter mAdapter;
    final List<NfcActivityState> mActivities = new LinkedList();
    final List<NfcApplicationState> mApps = new ArrayList(1);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class NfcApplicationState {
        final Application app;
        int refCount = 0;

        public NfcApplicationState(Application app) {
            this.app = app;
        }

        public synchronized void register() {
            this.refCount++;
            if (this.refCount == 1) {
                this.app.registerActivityLifecycleCallbacks(NfcActivityManager.this);
            }
        }

        public synchronized void unregister() {
            this.refCount--;
            if (this.refCount == 0) {
                this.app.unregisterActivityLifecycleCallbacks(NfcActivityManager.this);
            } else if (this.refCount < 0) {
                Log.e(NfcActivityManager.TAG, "-ve refcount for " + this.app);
            }
        }
    }

    synchronized NfcApplicationState findAppState(Application app) {
        for (NfcApplicationState appState : this.mApps) {
            if (appState.app == app) {
                return appState;
            }
        }
        return null;
    }

    synchronized void registerApplication(Application app) {
        NfcApplicationState appState = findAppState(app);
        if (appState == null) {
            appState = new NfcApplicationState(app);
            this.mApps.add(appState);
        }
        appState.register();
    }

    synchronized void unregisterApplication(Application app) {
        NfcApplicationState appState = findAppState(app);
        if (appState == null) {
            Log.e(TAG, "app was not registered " + app);
            return;
        }
        appState.unregister();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class NfcActivityState {
        Activity activity;
        boolean resumed;
        Binder token;
        NdefMessage ndefMessage = null;
        NfcAdapter.CreateNdefMessageCallback ndefMessageCallback = null;
        NfcAdapter.OnNdefPushCompleteCallback onNdefPushCompleteCallback = null;
        NfcAdapter.CreateBeamUrisCallback uriCallback = null;
        Uri[] uris = null;
        int flags = 0;
        int readerModeFlags = 0;
        NfcAdapter.ReaderCallback readerCallback = null;
        Bundle readerModeExtras = null;

        public NfcActivityState(Activity activity) {
            this.resumed = false;
            if (activity.getWindow().isDestroyed()) {
                throw new IllegalStateException("activity is already destroyed");
            }
            this.resumed = activity.isResumed();
            this.activity = activity;
            this.token = new Binder();
            NfcActivityManager.this.registerApplication(activity.getApplication());
        }

        public synchronized void destroy() {
            NfcActivityManager.this.unregisterApplication(this.activity.getApplication());
            this.resumed = false;
            this.activity = null;
            this.ndefMessage = null;
            this.ndefMessageCallback = null;
            this.onNdefPushCompleteCallback = null;
            this.uriCallback = null;
            this.uris = null;
            this.readerModeFlags = 0;
            this.token = null;
        }

        public String toString() {
            Uri[] uriArr;
            StringBuilder s = new StringBuilder("[").append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            s.append(this.ndefMessage);
            s.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            s.append(this.ndefMessageCallback);
            s.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            s.append(this.uriCallback);
            s.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            if (this.uris != null) {
                for (Uri uri : this.uris) {
                    s.append(this.onNdefPushCompleteCallback);
                    s.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    s.append(uri);
                    s.append("]");
                }
            }
            return s.toString();
        }
    }

    synchronized NfcActivityState findActivityState(Activity activity) {
        for (NfcActivityState state : this.mActivities) {
            if (state.activity == activity) {
                return state;
            }
        }
        return null;
    }

    synchronized NfcActivityState getActivityState(Activity activity) {
        NfcActivityState state;
        state = findActivityState(activity);
        if (state == null) {
            state = new NfcActivityState(activity);
            this.mActivities.add(state);
        }
        return state;
    }

    synchronized NfcActivityState findResumedActivityState() {
        for (NfcActivityState state : this.mActivities) {
            if (state.resumed) {
                return state;
            }
        }
        return null;
    }

    synchronized void destroyActivityState(Activity activity) {
        NfcActivityState activityState = findActivityState(activity);
        if (activityState != null) {
            activityState.destroy();
            this.mActivities.remove(activityState);
        }
    }

    public synchronized NfcActivityManager(NfcAdapter adapter) {
        this.mAdapter = adapter;
    }

    public synchronized void enableReaderMode(Activity activity, NfcAdapter.ReaderCallback callback, int flags, Bundle extras) {
        Binder token;
        boolean isResumed;
        NfcActivityState state = getActivityState(activity);
        state.readerCallback = callback;
        state.readerModeFlags = flags;
        state.readerModeExtras = extras;
        token = state.token;
        isResumed = state.resumed;
        if (isResumed) {
            setReaderMode(token, flags, extras);
        }
    }

    public synchronized void disableReaderMode(Activity activity) {
        Binder token;
        boolean isResumed;
        NfcActivityState state = getActivityState(activity);
        state.readerCallback = null;
        state.readerModeFlags = 0;
        state.readerModeExtras = null;
        token = state.token;
        isResumed = state.resumed;
        if (isResumed) {
            setReaderMode(token, 0, null);
        }
    }

    public synchronized void setReaderMode(Binder token, int flags, Bundle extras) {
        if (DBG.booleanValue()) {
            Log.d(TAG, "Setting reader mode");
        }
        try {
            NfcAdapter.sService.setReaderMode(token, this, flags, extras);
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    public synchronized void setNdefPushContentUri(Activity activity, Uri[] uris) {
        boolean isResumed;
        NfcActivityState state = getActivityState(activity);
        state.uris = uris;
        isResumed = state.resumed;
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public synchronized void setNdefPushContentUriCallback(Activity activity, NfcAdapter.CreateBeamUrisCallback callback) {
        boolean isResumed;
        NfcActivityState state = getActivityState(activity);
        state.uriCallback = callback;
        isResumed = state.resumed;
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public synchronized void setNdefPushMessage(Activity activity, NdefMessage message, int flags) {
        boolean isResumed;
        NfcActivityState state = getActivityState(activity);
        state.ndefMessage = message;
        state.flags = flags;
        isResumed = state.resumed;
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public synchronized void setNdefPushMessageCallback(Activity activity, NfcAdapter.CreateNdefMessageCallback callback, int flags) {
        boolean isResumed;
        NfcActivityState state = getActivityState(activity);
        state.ndefMessageCallback = callback;
        state.flags = flags;
        isResumed = state.resumed;
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public synchronized void setOnNdefPushCompleteCallback(Activity activity, NfcAdapter.OnNdefPushCompleteCallback callback) {
        boolean isResumed;
        NfcActivityState state = getActivityState(activity);
        state.onNdefPushCompleteCallback = callback;
        isResumed = state.resumed;
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    synchronized void requestNfcServiceCallback() {
        try {
            NfcAdapter.sService.setAppCallback(this);
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    synchronized void verifyNfcPermission() {
        try {
            NfcAdapter.sService.verifyNfcPermission();
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00ac A[Catch: all -> 0x00b7, TRY_LEAVE, TryCatch #2 {all -> 0x00b7, blocks: (B:28:0x0053, B:38:0x0088, B:29:0x0059, B:31:0x0061, B:33:0x0069, B:36:0x0072, B:37:0x007e, B:39:0x008d, B:44:0x00a5, B:46:0x00a8, B:48:0x00ac), top: B:65:0x0053 }] */
    @Override // android.nfc.IAppCallback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized android.nfc.BeamShareData createBeamShareData(byte r19) {
        /*
            Method dump skipped, instructions count: 209
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.createBeamShareData(byte):android.nfc.BeamShareData");
    }

    @Override // android.nfc.IAppCallback
    public synchronized void onNdefPushComplete(byte peerLlcpVersion) {
        NfcActivityState state = findResumedActivityState();
        if (state == null) {
            return;
        }
        NfcAdapter.OnNdefPushCompleteCallback callback = state.onNdefPushCompleteCallback;
        NfcEvent event = new NfcEvent(this.mAdapter, peerLlcpVersion);
        if (callback != null) {
            callback.onNdefPushComplete(event);
        }
    }

    @Override // android.nfc.IAppCallback
    public synchronized void onTagDiscovered(Tag tag) throws RemoteException {
        NfcActivityState state = findResumedActivityState();
        if (state == null) {
            return;
        }
        NfcAdapter.ReaderCallback callback = state.readerCallback;
        if (callback != null) {
            callback.onTagDiscovered(tag);
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        synchronized (this) {
            NfcActivityState state = findActivityState(activity);
            if (DBG.booleanValue()) {
                Log.d(TAG, "onResume() for " + activity + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + state);
            }
            if (state == null) {
                return;
            }
            state.resumed = true;
            Binder token = state.token;
            int readerModeFlags = state.readerModeFlags;
            Bundle readerModeExtras = state.readerModeExtras;
            if (readerModeFlags != 0) {
                setReaderMode(token, readerModeFlags, readerModeExtras);
            }
            requestNfcServiceCallback();
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        synchronized (this) {
            NfcActivityState state = findActivityState(activity);
            if (DBG.booleanValue()) {
                Log.d(TAG, "onPause() for " + activity + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + state);
            }
            if (state == null) {
                return;
            }
            state.resumed = false;
            Binder token = state.token;
            boolean readerModeFlagsSet = state.readerModeFlags != 0;
            if (readerModeFlagsSet) {
                setReaderMode(token, 0, null);
            }
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
        synchronized (this) {
            NfcActivityState state = findActivityState(activity);
            if (DBG.booleanValue()) {
                Log.d(TAG, "onDestroy() for " + activity + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + state);
            }
            if (state != null) {
                destroyActivityState(activity);
            }
        }
    }
}
