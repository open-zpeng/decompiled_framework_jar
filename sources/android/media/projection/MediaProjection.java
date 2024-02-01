package android.media.projection;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.IMediaProjectionCallback;
import android.os.Handler;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Surface;
import java.util.Map;

/* loaded from: classes2.dex */
public final class MediaProjection {
    private static final String TAG = "MediaProjection";
    private final Map<Callback, CallbackRecord> mCallbacks = new ArrayMap();
    private final Context mContext;
    private final IMediaProjection mImpl;

    public MediaProjection(Context context, IMediaProjection impl) {
        this.mContext = context;
        this.mImpl = impl;
        try {
            this.mImpl.start(new MediaProjectionCallback());
        } catch (RemoteException e) {
            throw new RuntimeException("Failed to start media projection", e);
        }
    }

    public void registerCallback(Callback callback, Handler handler) {
        if (callback == null) {
            throw new IllegalArgumentException("callback should not be null");
        }
        if (handler == null) {
            handler = new Handler();
        }
        this.mCallbacks.put(callback, new CallbackRecord(callback, handler));
    }

    public void unregisterCallback(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback should not be null");
        }
        this.mCallbacks.remove(callback);
    }

    public VirtualDisplay createVirtualDisplay(String name, int width, int height, int dpi, boolean isSecure, Surface surface, VirtualDisplay.Callback callback, Handler handler) {
        DisplayManager dm = (DisplayManager) this.mContext.getSystemService(Context.DISPLAY_SERVICE);
        int flags = isSecure ? 4 : 0;
        return dm.createVirtualDisplay(this, name, width, height, dpi, surface, flags | 16 | 2, callback, handler, null);
    }

    public VirtualDisplay createVirtualDisplay(String name, int width, int height, int dpi, int flags, Surface surface, VirtualDisplay.Callback callback, Handler handler) {
        DisplayManager dm = (DisplayManager) this.mContext.getSystemService(Context.DISPLAY_SERVICE);
        return dm.createVirtualDisplay(this, name, width, height, dpi, surface, flags, callback, handler, null);
    }

    public void stop() {
        try {
            this.mImpl.stop();
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to stop projection", e);
        }
    }

    public IMediaProjection getProjection() {
        return this.mImpl;
    }

    /* loaded from: classes2.dex */
    public static abstract class Callback {
        public void onStop() {
        }
    }

    /* loaded from: classes2.dex */
    private final class MediaProjectionCallback extends IMediaProjectionCallback.Stub {
        private MediaProjectionCallback() {
        }

        @Override // android.media.projection.IMediaProjectionCallback
        public void onStop() {
            for (CallbackRecord cbr : MediaProjection.this.mCallbacks.values()) {
                cbr.onStop();
            }
        }
    }

    /* loaded from: classes2.dex */
    private static final class CallbackRecord {
        private final Callback mCallback;
        private final Handler mHandler;

        public CallbackRecord(Callback callback, Handler handler) {
            this.mCallback = callback;
            this.mHandler = handler;
        }

        public void onStop() {
            this.mHandler.post(new Runnable() { // from class: android.media.projection.MediaProjection.CallbackRecord.1
                @Override // java.lang.Runnable
                public void run() {
                    CallbackRecord.this.mCallback.onStop();
                }
            });
        }
    }
}
