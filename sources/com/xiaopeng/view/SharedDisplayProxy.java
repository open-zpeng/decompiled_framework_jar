package com.xiaopeng.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class SharedDisplayProxy {
    private static final String TAG = "SharedDisplayProxy";
    private List<OnSharedDisplayListener> mListeners = new ArrayList();
    private SharedDisplayListenerImpl mSharedDisplayListener = new SharedDisplayListenerImpl();
    private WindowManagerFactory mWindowFactory;

    public static SharedDisplayProxy create(Context context) {
        return new SharedDisplayProxy(context);
    }

    private SharedDisplayProxy(Context context) {
        this.mWindowFactory = WindowManagerFactory.create(context);
    }

    public void onInit() {
        Log.i(TAG, "onInit");
        this.mWindowFactory.registerSharedListener(this.mSharedDisplayListener);
    }

    public void onStop() {
        Log.i(TAG, "onStop");
        this.mWindowFactory.unregisterSharedListener(this.mSharedDisplayListener);
    }

    public void registerSharedListener(OnSharedDisplayListener listener) {
        Log.i(TAG, "registerSharedListener listener=" + listener);
        if (listener != null) {
            this.mListeners.add(listener);
        }
    }

    public void unregisterSharedListener(OnSharedDisplayListener listener) {
        Log.i(TAG, "unregisterSharedListener listener=" + listener);
        if (listener != null && this.mListeners.contains(listener)) {
            this.mListeners.remove(listener);
        }
    }

    public static boolean isPrimaryId(int sharedId) {
        return WindowManagerFactory.isPrimaryId(sharedId);
    }

    public int getSharedId(String packageName) {
        return this.mWindowFactory.getSharedId(packageName);
    }

    public void setSharedId(String packageName, int sharedId) {
        this.mWindowFactory.setSharedId(packageName, sharedId);
        Log.i(TAG, "onChanged packageName=" + packageName + " sharedId=" + sharedId);
    }

    public List<String> getSharedPackages() {
        return this.mWindowFactory.getSharedPackages();
    }

    public List<String> getFilterPackages(int sharedId) {
        return this.mWindowFactory.getFilterPackages(sharedId);
    }

    public void setSharedEvent(int event) {
        this.mWindowFactory.setSharedEvent(event);
        Log.i(TAG, "onChanged event=" + event);
    }

    public void setSharedEvent(int event, int sharedId) {
        this.mWindowFactory.setSharedEvent(event, sharedId);
        Log.i(TAG, "setSharedEvent event=" + event + " sharedId=" + sharedId);
    }

    public void setSharedEvent(int event, int sharedId, String extras) {
        this.mWindowFactory.setSharedEvent(event, sharedId, extras);
        Log.i(TAG, "setSharedEvent event=" + event + " sharedId=" + sharedId + " extras=" + extras);
    }

    public Rect getActivityBounds(String packageName, boolean fullscreen) {
        return this.mWindowFactory.getActivityBounds(packageName, fullscreen);
    }

    public String getTopActivity(int type, int id) {
        return this.mWindowFactory.getTopActivity(type, id);
    }

    public String getTopWindow() {
        return this.mWindowFactory.getTopWindow();
    }

    /* loaded from: classes3.dex */
    public interface OnSharedDisplayListener {
        default void onChanged(String packageName, int sharedId) {
        }

        default void onPositionChanged(String packageName, int event, int from, int to) {
        }

        default void onActivityChanged(int screenId, String property) {
        }

        default void onEventChanged(int event, String property) {
        }
    }

    /* loaded from: classes3.dex */
    private final class SharedDisplayListenerImpl extends SharedDisplayListener {
        private SharedDisplayListenerImpl() {
        }

        @Override // com.xiaopeng.view.SharedDisplayListener, com.xiaopeng.view.ISharedDisplayListener
        public void onChanged(String packageName, int sharedId) throws RemoteException {
            super.onChanged(packageName, sharedId);
            boolean primary = WindowManagerFactory.isPrimaryId(sharedId);
            Log.i(SharedDisplayProxy.TAG, "onChanged packageName=" + packageName + " sharedId=" + sharedId + " primary=" + primary);
            if (SharedDisplayProxy.this.mListeners != null) {
                for (OnSharedDisplayListener listener : SharedDisplayProxy.this.mListeners) {
                    if (listener != null) {
                        listener.onChanged(packageName, sharedId);
                    }
                }
            }
        }

        @Override // com.xiaopeng.view.SharedDisplayListener, com.xiaopeng.view.ISharedDisplayListener
        public void onPositionChanged(String packageName, int event, int from, int to) throws RemoteException {
            super.onPositionChanged(packageName, event, from, to);
            Log.i(SharedDisplayProxy.TAG, "onPositionChanged packageName=" + packageName + " event=" + event + " from=" + from + " to=" + to);
            if (SharedDisplayProxy.this.mListeners != null) {
                for (OnSharedDisplayListener listener : SharedDisplayProxy.this.mListeners) {
                    if (listener != null) {
                        listener.onPositionChanged(packageName, event, from, to);
                    }
                }
            }
        }

        @Override // com.xiaopeng.view.SharedDisplayListener, com.xiaopeng.view.ISharedDisplayListener
        public void onActivityChanged(int screenId, String property) throws RemoteException {
            super.onActivityChanged(screenId, property);
            Log.i(SharedDisplayProxy.TAG, "onActivityChanged screenId=" + screenId + " property=" + property);
            if (SharedDisplayProxy.this.mListeners != null) {
                for (OnSharedDisplayListener listener : SharedDisplayProxy.this.mListeners) {
                    if (listener != null) {
                        listener.onActivityChanged(screenId, property);
                    }
                }
            }
        }

        @Override // com.xiaopeng.view.SharedDisplayListener, com.xiaopeng.view.ISharedDisplayListener
        public void onEventChanged(int event, String property) throws RemoteException {
            super.onEventChanged(event, property);
            Log.i(SharedDisplayProxy.TAG, "onEventChanged event=" + event + " property=" + property);
            if (SharedDisplayProxy.this.mListeners != null) {
                for (OnSharedDisplayListener listener : SharedDisplayProxy.this.mListeners) {
                    if (listener != null) {
                        listener.onEventChanged(event, property);
                    }
                }
            }
        }
    }
}
