package android.app;

import android.annotation.SystemApi;
import android.app.VrManager;
import android.content.ComponentName;
import android.os.Handler;
import android.os.RemoteException;
import android.service.vr.IPersistentVrStateCallbacks;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.util.ArrayMap;
import java.util.Map;
@SystemApi
/* loaded from: classes.dex */
public class VrManager {
    private Map<VrStateCallback, CallbackEntry> mCallbackMap = new ArrayMap();
    public protected final IVrManager mService;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CallbackEntry {
        final VrStateCallback mCallback;
        final Handler mHandler;
        final IVrStateCallbacks mStateCallback = new AnonymousClass1();
        final IPersistentVrStateCallbacks mPersistentStateCallback = new AnonymousClass2();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: android.app.VrManager$CallbackEntry$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass1 extends IVrStateCallbacks.Stub {
            AnonymousClass1() {
            }

            @Override // android.service.vr.IVrStateCallbacks
            public void onVrStateChanged(final boolean enabled) {
                CallbackEntry.this.mHandler.post(new Runnable() { // from class: android.app.-$$Lambda$VrManager$CallbackEntry$1$rgUBVVG1QhelpvAp8W3UQHDHJdU
                    @Override // java.lang.Runnable
                    public final void run() {
                        VrManager.CallbackEntry.this.mCallback.onVrStateChanged(enabled);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: android.app.VrManager$CallbackEntry$2  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass2 extends IPersistentVrStateCallbacks.Stub {
            AnonymousClass2() {
            }

            @Override // android.service.vr.IPersistentVrStateCallbacks
            public void onPersistentVrStateChanged(final boolean enabled) {
                CallbackEntry.this.mHandler.post(new Runnable() { // from class: android.app.-$$Lambda$VrManager$CallbackEntry$2$KvHLIXm3-7igcOqTEl46YdjhHMk
                    @Override // java.lang.Runnable
                    public final void run() {
                        VrManager.CallbackEntry.this.mCallback.onPersistentVrStateChanged(enabled);
                    }
                });
            }
        }

        synchronized CallbackEntry(VrStateCallback callback, Handler handler) {
            this.mCallback = callback;
            this.mHandler = handler;
        }
    }

    public synchronized VrManager(IVrManager service) {
        this.mService = service;
    }

    private protected void registerVrStateCallback(VrStateCallback callback, Handler handler) {
        if (callback == null || this.mCallbackMap.containsKey(callback)) {
            return;
        }
        CallbackEntry entry = new CallbackEntry(callback, handler);
        this.mCallbackMap.put(callback, entry);
        try {
            this.mService.registerListener(entry.mStateCallback);
            this.mService.registerPersistentVrStateListener(entry.mPersistentStateCallback);
        } catch (RemoteException e) {
            try {
                unregisterVrStateCallback(callback);
            } catch (Exception e2) {
                e.rethrowFromSystemServer();
            }
        }
    }

    private protected void unregisterVrStateCallback(VrStateCallback callback) {
        CallbackEntry entry = this.mCallbackMap.remove(callback);
        if (entry != null) {
            try {
                this.mService.unregisterListener(entry.mStateCallback);
            } catch (RemoteException e) {
            }
            try {
                this.mService.unregisterPersistentVrStateListener(entry.mPersistentStateCallback);
            } catch (RemoteException e2) {
            }
        }
    }

    public synchronized boolean getVrModeEnabled() {
        try {
            return this.mService.getVrModeState();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    private protected boolean getPersistentVrModeEnabled() {
        try {
            return this.mService.getPersistentVrModeEnabled();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public void setPersistentVrModeEnabled(boolean enabled) {
        try {
            this.mService.setPersistentVrModeEnabled(enabled);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    private protected void setVr2dDisplayProperties(Vr2dDisplayProperties vr2dDisplayProp) {
        try {
            this.mService.setVr2dDisplayProperties(vr2dDisplayProp);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setAndBindVrCompositor(ComponentName componentName) {
        try {
            this.mService.setAndBindCompositor(componentName == null ? null : componentName.flattenToString());
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public synchronized void setStandbyEnabled(boolean standby) {
        try {
            this.mService.setStandbyEnabled(standby);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void setVrInputMethod(ComponentName componentName) {
        try {
            this.mService.setVrInputMethod(componentName);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }
}
