package android.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.media.AudioAttributes;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.session.ISessionControllerCallback;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.mtp.MtpConstants;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class MediaController {
    private static final int MSG_DESTROYED = 8;
    private static final int MSG_EVENT = 1;
    private static final int MSG_UPDATE_EXTRAS = 7;
    private static final int MSG_UPDATE_METADATA = 3;
    private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
    private static final int MSG_UPDATE_QUEUE = 5;
    private static final int MSG_UPDATE_QUEUE_TITLE = 6;
    private static final int MSG_UPDATE_VOLUME = 4;
    private static final String TAG = "MediaController";
    private final ArrayList<MessageHandler> mCallbacks;
    private boolean mCbRegistered;
    private final CallbackStub mCbStub;
    private final Context mContext;
    private final Object mLock;
    private String mPackageName;
    private final ISessionController mSessionBinder;
    private String mTag;
    private final MediaSession.Token mToken;
    private final TransportControls mTransportControls;

    public synchronized MediaController(Context context, ISessionController sessionBinder) {
        this.mCbStub = new CallbackStub(this);
        this.mCallbacks = new ArrayList<>();
        this.mLock = new Object();
        this.mCbRegistered = false;
        if (sessionBinder == null) {
            throw new IllegalArgumentException("Session token cannot be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.mSessionBinder = sessionBinder;
        this.mTransportControls = new TransportControls();
        this.mToken = new MediaSession.Token(sessionBinder);
        this.mContext = context;
    }

    public MediaController(Context context, MediaSession.Token token) {
        this(context, token.getBinder());
    }

    public TransportControls getTransportControls() {
        return this.mTransportControls;
    }

    public boolean dispatchMediaButtonEvent(KeyEvent keyEvent) {
        return dispatchMediaButtonEventInternal(false, keyEvent);
    }

    public synchronized boolean dispatchMediaButtonEventAsSystemService(KeyEvent keyEvent) {
        return dispatchMediaButtonEventInternal(true, keyEvent);
    }

    private synchronized boolean dispatchMediaButtonEventInternal(boolean asSystemService, KeyEvent keyEvent) {
        if (keyEvent == null) {
            throw new IllegalArgumentException("KeyEvent may not be null");
        }
        if (KeyEvent.isMediaKey(keyEvent.getKeyCode())) {
            try {
                return this.mSessionBinder.sendMediaButton(this.mContext.getPackageName(), this.mCbStub, asSystemService, keyEvent);
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public synchronized void dispatchVolumeButtonEventAsSystemService(KeyEvent keyEvent) {
        switch (keyEvent.getAction()) {
            case 0:
                int direction = 0;
                int keyCode = keyEvent.getKeyCode();
                if (keyCode != 164) {
                    switch (keyCode) {
                        case 24:
                            direction = 1;
                            break;
                        case 25:
                            direction = -1;
                            break;
                    }
                } else {
                    direction = 101;
                }
                try {
                    this.mSessionBinder.adjustVolume(this.mContext.getPackageName(), this.mCbStub, true, direction, 1);
                    break;
                } catch (RemoteException e) {
                    Log.wtf(TAG, "Error calling adjustVolumeBy", e);
                    break;
                }
            case 1:
                break;
            default:
                return;
        }
        try {
            this.mSessionBinder.adjustVolume(this.mContext.getPackageName(), this.mCbStub, true, 0, MtpConstants.OPERATION_GET_DEVICE_PROP_DESC);
        } catch (RemoteException e2) {
            Log.wtf(TAG, "Error calling adjustVolumeBy", e2);
        }
    }

    public PlaybackState getPlaybackState() {
        try {
            return this.mSessionBinder.getPlaybackState();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getPlaybackState.", e);
            return null;
        }
    }

    public MediaMetadata getMetadata() {
        try {
            return this.mSessionBinder.getMetadata();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getMetadata.", e);
            return null;
        }
    }

    public List<MediaSession.QueueItem> getQueue() {
        try {
            ParceledListSlice queue = this.mSessionBinder.getQueue();
            if (queue != null) {
                return queue.getList();
            }
            return null;
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getQueue.", e);
            return null;
        }
    }

    public CharSequence getQueueTitle() {
        try {
            return this.mSessionBinder.getQueueTitle();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getQueueTitle", e);
            return null;
        }
    }

    public Bundle getExtras() {
        try {
            return this.mSessionBinder.getExtras();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getExtras", e);
            return null;
        }
    }

    public int getRatingType() {
        try {
            return this.mSessionBinder.getRatingType();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getRatingType.", e);
            return 0;
        }
    }

    public long getFlags() {
        try {
            return this.mSessionBinder.getFlags();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getFlags.", e);
            return 0L;
        }
    }

    public PlaybackInfo getPlaybackInfo() {
        try {
            ParcelableVolumeInfo result = this.mSessionBinder.getVolumeAttributes();
            return new PlaybackInfo(result.volumeType, result.audioAttrs, result.controlType, result.maxVolume, result.currentVolume);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getAudioInfo.", e);
            return null;
        }
    }

    public PendingIntent getSessionActivity() {
        try {
            return this.mSessionBinder.getLaunchPendingIntent();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getPendingIntent.", e);
            return null;
        }
    }

    public MediaSession.Token getSessionToken() {
        return this.mToken;
    }

    public void setVolumeTo(int value, int flags) {
        try {
            this.mSessionBinder.setVolumeTo(this.mContext.getPackageName(), this.mCbStub, value, flags);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling setVolumeTo.", e);
        }
    }

    public void adjustVolume(int direction, int flags) {
        try {
            this.mSessionBinder.adjustVolume(this.mContext.getPackageName(), this.mCbStub, false, direction, flags);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling adjustVolumeBy.", e);
        }
    }

    public void registerCallback(Callback callback) {
        registerCallback(callback, null);
    }

    public void registerCallback(Callback callback, Handler handler) {
        Handler handler2;
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        if (handler == null) {
            handler2 = new Handler();
        } else {
            handler2 = handler;
        }
        synchronized (this.mLock) {
            addCallbackLocked(callback, handler2);
        }
    }

    public void unregisterCallback(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        synchronized (this.mLock) {
            removeCallbackLocked(callback);
        }
    }

    public void sendCommand(String command, Bundle args, ResultReceiver cb) {
        if (TextUtils.isEmpty(command)) {
            throw new IllegalArgumentException("command cannot be null or empty");
        }
        try {
            this.mSessionBinder.sendCommand(this.mContext.getPackageName(), this.mCbStub, command, args, cb);
        } catch (RemoteException e) {
            Log.d(TAG, "Dead object in sendCommand.", e);
        }
    }

    public String getPackageName() {
        if (this.mPackageName == null) {
            try {
                this.mPackageName = this.mSessionBinder.getPackageName();
            } catch (RemoteException e) {
                Log.d(TAG, "Dead object in getPackageName.", e);
            }
        }
        return this.mPackageName;
    }

    public synchronized String getTag() {
        if (this.mTag == null) {
            try {
                this.mTag = this.mSessionBinder.getTag();
            } catch (RemoteException e) {
                Log.d(TAG, "Dead object in getTag.", e);
            }
        }
        return this.mTag;
    }

    synchronized ISessionController getSessionBinder() {
        return this.mSessionBinder;
    }

    private protected boolean controlsSameSession(MediaController other) {
        return other != null && this.mSessionBinder.asBinder() == other.getSessionBinder().asBinder();
    }

    private synchronized void addCallbackLocked(Callback cb, Handler handler) {
        if (getHandlerForCallbackLocked(cb) != null) {
            Log.w(TAG, "Callback is already added, ignoring");
            return;
        }
        MessageHandler holder = new MessageHandler(handler.getLooper(), cb);
        this.mCallbacks.add(holder);
        holder.mRegistered = true;
        if (!this.mCbRegistered) {
            try {
                this.mSessionBinder.registerCallbackListener(this.mContext.getPackageName(), this.mCbStub);
                this.mCbRegistered = true;
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in registerCallback", e);
            }
        }
    }

    private synchronized boolean removeCallbackLocked(Callback cb) {
        boolean success = false;
        int i = this.mCallbacks.size();
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            MessageHandler handler = this.mCallbacks.get(i);
            if (cb == handler.mCallback) {
                this.mCallbacks.remove(i);
                success = true;
                handler.mRegistered = false;
            }
        }
        if (this.mCbRegistered && this.mCallbacks.size() == 0) {
            try {
                this.mSessionBinder.unregisterCallbackListener(this.mCbStub);
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in removeCallbackLocked");
            }
            this.mCbRegistered = false;
        }
        return success;
    }

    private synchronized MessageHandler getHandlerForCallbackLocked(Callback cb) {
        if (cb == null) {
            throw new IllegalArgumentException("Callback cannot be null");
        }
        for (int i = this.mCallbacks.size() - 1; i >= 0; i--) {
            MessageHandler handler = this.mCallbacks.get(i);
            if (cb == handler.mCallback) {
                return handler;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final synchronized void postMessage(int what, Object obj, Bundle data) {
        synchronized (this.mLock) {
            for (int i = this.mCallbacks.size() - 1; i >= 0; i--) {
                this.mCallbacks.get(i).post(what, obj, data);
            }
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Callback {
        public void onSessionDestroyed() {
        }

        public void onSessionEvent(String event, Bundle extras) {
        }

        public void onPlaybackStateChanged(PlaybackState state) {
        }

        public void onMetadataChanged(MediaMetadata metadata) {
        }

        public void onQueueChanged(List<MediaSession.QueueItem> queue) {
        }

        public void onQueueTitleChanged(CharSequence title) {
        }

        public void onExtrasChanged(Bundle extras) {
        }

        public void onAudioInfoChanged(PlaybackInfo info) {
        }
    }

    /* loaded from: classes.dex */
    public final class TransportControls {
        private static final String TAG = "TransportController";

        private TransportControls() {
        }

        public void prepare() {
            try {
                MediaController.this.mSessionBinder.prepare(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling prepare.", e);
            }
        }

        public void prepareFromMediaId(String mediaId, Bundle extras) {
            if (!TextUtils.isEmpty(mediaId)) {
                try {
                    MediaController.this.mSessionBinder.prepareFromMediaId(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, mediaId, extras);
                    return;
                } catch (RemoteException e) {
                    Log.wtf(TAG, "Error calling prepare(" + mediaId + ").", e);
                    return;
                }
            }
            throw new IllegalArgumentException("You must specify a non-empty String for prepareFromMediaId.");
        }

        public void prepareFromSearch(String query, Bundle extras) {
            if (query == null) {
                query = "";
            }
            try {
                MediaController.this.mSessionBinder.prepareFromSearch(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, query, extras);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling prepare(" + query + ").", e);
            }
        }

        public void prepareFromUri(Uri uri, Bundle extras) {
            if (uri != null && !Uri.EMPTY.equals(uri)) {
                try {
                    MediaController.this.mSessionBinder.prepareFromUri(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, uri, extras);
                    return;
                } catch (RemoteException e) {
                    Log.wtf(TAG, "Error calling prepare(" + uri + ").", e);
                    return;
                }
            }
            throw new IllegalArgumentException("You must specify a non-empty Uri for prepareFromUri.");
        }

        public void play() {
            try {
                MediaController.this.mSessionBinder.play(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling play.", e);
            }
        }

        public void playFromMediaId(String mediaId, Bundle extras) {
            if (!TextUtils.isEmpty(mediaId)) {
                try {
                    MediaController.this.mSessionBinder.playFromMediaId(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, mediaId, extras);
                    return;
                } catch (RemoteException e) {
                    Log.wtf(TAG, "Error calling play(" + mediaId + ").", e);
                    return;
                }
            }
            throw new IllegalArgumentException("You must specify a non-empty String for playFromMediaId.");
        }

        public void playFromSearch(String query, Bundle extras) {
            if (query == null) {
                query = "";
            }
            try {
                MediaController.this.mSessionBinder.playFromSearch(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, query, extras);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling play(" + query + ").", e);
            }
        }

        public void playFromUri(Uri uri, Bundle extras) {
            if (uri != null && !Uri.EMPTY.equals(uri)) {
                try {
                    MediaController.this.mSessionBinder.playFromUri(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, uri, extras);
                    return;
                } catch (RemoteException e) {
                    Log.wtf(TAG, "Error calling play(" + uri + ").", e);
                    return;
                }
            }
            throw new IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
        }

        public void skipToQueueItem(long id) {
            try {
                MediaController.this.mSessionBinder.skipToQueueItem(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, id);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling skipToItem(" + id + ").", e);
            }
        }

        public void pause() {
            try {
                MediaController.this.mSessionBinder.pause(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling pause.", e);
            }
        }

        public void stop() {
            try {
                MediaController.this.mSessionBinder.stop(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling stop.", e);
            }
        }

        public void seekTo(long pos) {
            try {
                MediaController.this.mSessionBinder.seekTo(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, pos);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling seekTo.", e);
            }
        }

        public void fastForward() {
            try {
                MediaController.this.mSessionBinder.fastForward(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling fastForward.", e);
            }
        }

        public void skipToNext() {
            try {
                MediaController.this.mSessionBinder.next(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling next.", e);
            }
        }

        public void rewind() {
            try {
                MediaController.this.mSessionBinder.rewind(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling rewind.", e);
            }
        }

        public void skipToPrevious() {
            try {
                MediaController.this.mSessionBinder.previous(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling previous.", e);
            }
        }

        public void setRating(Rating rating) {
            try {
                MediaController.this.mSessionBinder.rate(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, rating);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling rate.", e);
            }
        }

        public void sendCustomAction(PlaybackState.CustomAction customAction, Bundle args) {
            if (customAction == null) {
                throw new IllegalArgumentException("CustomAction cannot be null.");
            }
            sendCustomAction(customAction.getAction(), args);
        }

        public void sendCustomAction(String action, Bundle args) {
            if (!TextUtils.isEmpty(action)) {
                try {
                    MediaController.this.mSessionBinder.sendCustomAction(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, action, args);
                    return;
                } catch (RemoteException e) {
                    Log.d(TAG, "Dead object in sendCustomAction.", e);
                    return;
                }
            }
            throw new IllegalArgumentException("CustomAction cannot be null.");
        }
    }

    /* loaded from: classes.dex */
    public static final class PlaybackInfo {
        public static final int PLAYBACK_TYPE_LOCAL = 1;
        public static final int PLAYBACK_TYPE_REMOTE = 2;
        private final AudioAttributes mAudioAttrs;
        private final int mCurrentVolume;
        private final int mMaxVolume;
        private final int mVolumeControl;
        private final int mVolumeType;

        public synchronized PlaybackInfo(int type, AudioAttributes attrs, int control, int max, int current) {
            this.mVolumeType = type;
            this.mAudioAttrs = attrs;
            this.mVolumeControl = control;
            this.mMaxVolume = max;
            this.mCurrentVolume = current;
        }

        public int getPlaybackType() {
            return this.mVolumeType;
        }

        public AudioAttributes getAudioAttributes() {
            return this.mAudioAttrs;
        }

        public int getVolumeControl() {
            return this.mVolumeControl;
        }

        public int getMaxVolume() {
            return this.mMaxVolume;
        }

        public int getCurrentVolume() {
            return this.mCurrentVolume;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class CallbackStub extends ISessionControllerCallback.Stub {
        private final WeakReference<MediaController> mController;

        public synchronized CallbackStub(MediaController controller) {
            this.mController = new WeakReference<>(controller);
        }

        @Override // android.media.session.ISessionControllerCallback
        public synchronized void onSessionDestroyed() {
            MediaController controller = this.mController.get();
            if (controller != null) {
                controller.postMessage(8, null, null);
            }
        }

        @Override // android.media.session.ISessionControllerCallback
        public synchronized void onEvent(String event, Bundle extras) {
            MediaController controller = this.mController.get();
            if (controller != null) {
                controller.postMessage(1, event, extras);
            }
        }

        @Override // android.media.session.ISessionControllerCallback
        public synchronized void onPlaybackStateChanged(PlaybackState state) {
            MediaController controller = this.mController.get();
            if (controller != null) {
                controller.postMessage(2, state, null);
            }
        }

        @Override // android.media.session.ISessionControllerCallback
        public synchronized void onMetadataChanged(MediaMetadata metadata) {
            MediaController controller = this.mController.get();
            if (controller != null) {
                controller.postMessage(3, metadata, null);
            }
        }

        @Override // android.media.session.ISessionControllerCallback
        public synchronized void onQueueChanged(ParceledListSlice parceledQueue) {
            List<MediaSession.QueueItem> queue = parceledQueue == null ? null : parceledQueue.getList();
            MediaController controller = this.mController.get();
            if (controller != null) {
                controller.postMessage(5, queue, null);
            }
        }

        @Override // android.media.session.ISessionControllerCallback
        public synchronized void onQueueTitleChanged(CharSequence title) {
            MediaController controller = this.mController.get();
            if (controller != null) {
                controller.postMessage(6, title, null);
            }
        }

        @Override // android.media.session.ISessionControllerCallback
        public synchronized void onExtrasChanged(Bundle extras) {
            MediaController controller = this.mController.get();
            if (controller != null) {
                controller.postMessage(7, extras, null);
            }
        }

        @Override // android.media.session.ISessionControllerCallback
        public synchronized void onVolumeInfoChanged(ParcelableVolumeInfo pvi) {
            MediaController controller = this.mController.get();
            if (controller != null) {
                PlaybackInfo info = new PlaybackInfo(pvi.volumeType, pvi.audioAttrs, pvi.controlType, pvi.maxVolume, pvi.currentVolume);
                controller.postMessage(4, info, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class MessageHandler extends Handler {
        private final Callback mCallback;
        private boolean mRegistered;

        public synchronized MessageHandler(Looper looper, Callback cb) {
            super(looper, null, true);
            this.mRegistered = false;
            this.mCallback = cb;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (!this.mRegistered) {
                return;
            }
            switch (msg.what) {
                case 1:
                    this.mCallback.onSessionEvent((String) msg.obj, msg.getData());
                    return;
                case 2:
                    this.mCallback.onPlaybackStateChanged((PlaybackState) msg.obj);
                    return;
                case 3:
                    this.mCallback.onMetadataChanged((MediaMetadata) msg.obj);
                    return;
                case 4:
                    this.mCallback.onAudioInfoChanged((PlaybackInfo) msg.obj);
                    return;
                case 5:
                    this.mCallback.onQueueChanged((List) msg.obj);
                    return;
                case 6:
                    this.mCallback.onQueueTitleChanged((CharSequence) msg.obj);
                    return;
                case 7:
                    this.mCallback.onExtrasChanged((Bundle) msg.obj);
                    return;
                case 8:
                    this.mCallback.onSessionDestroyed();
                    return;
                default:
                    return;
            }
        }

        public synchronized void post(int what, Object obj, Bundle data) {
            Message msg = obtainMessage(what, obj);
            msg.setData(data);
            msg.sendToTarget();
        }
    }
}
