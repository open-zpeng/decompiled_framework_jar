package android.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.media.AudioAttributes;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.VolumeProvider;
import android.media.session.ISessionCallback;
import android.media.session.ISessionController;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaSession {
    public static final int FLAG_EXCLUSIVE_GLOBAL_PRIORITY = 65536;
    @Deprecated
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    @Deprecated
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
    public static final int INVALID_PID = -1;
    public static final int INVALID_UID = -1;
    private static final String TAG = "MediaSession";
    private boolean mActive;
    private final ISession mBinder;
    public protected CallbackMessageHandler mCallback;
    private final CallbackStub mCbStub;
    private final MediaController mController;
    private final Object mLock;
    private final int mMaxBitmapSize;
    private PlaybackState mPlaybackState;
    private final Token mSessionToken;
    private VolumeProvider mVolumeProvider;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SessionFlags {
    }

    public MediaSession(Context context, String tag) {
        this(context, tag, UserHandle.myUserId());
    }

    public synchronized MediaSession(Context context, String tag, int userId) {
        this.mLock = new Object();
        this.mActive = false;
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null.");
        }
        if (TextUtils.isEmpty(tag)) {
            throw new IllegalArgumentException("tag cannot be null or empty");
        }
        this.mMaxBitmapSize = context.getResources().getDimensionPixelSize(R.dimen.config_mediaMetadataBitmapMaxSize);
        this.mCbStub = new CallbackStub(this);
        MediaSessionManager manager = (MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE);
        try {
            this.mBinder = manager.createSession(this.mCbStub, tag, userId);
            this.mSessionToken = new Token(this.mBinder.getController());
            this.mController = new MediaController(context, this.mSessionToken);
        } catch (RemoteException e) {
            throw new RuntimeException("Remote error creating session.", e);
        }
    }

    public void setCallback(Callback callback) {
        setCallback(callback, null);
    }

    public void setCallback(Callback callback, Handler handler) {
        synchronized (this.mLock) {
            if (this.mCallback != null) {
                this.mCallback.mCallback.mSession = null;
                this.mCallback.removeCallbacksAndMessages(null);
            }
            if (callback == null) {
                this.mCallback = null;
                return;
            }
            if (handler == null) {
                handler = new Handler();
            }
            callback.mSession = this;
            CallbackMessageHandler msgHandler = new CallbackMessageHandler(handler.getLooper(), callback);
            this.mCallback = msgHandler;
        }
    }

    public void setSessionActivity(PendingIntent pi) {
        try {
            this.mBinder.setLaunchPendingIntent(pi);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Failure in setLaunchPendingIntent.", e);
        }
    }

    public void setMediaButtonReceiver(PendingIntent mbr) {
        try {
            this.mBinder.setMediaButtonReceiver(mbr);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Failure in setMediaButtonReceiver.", e);
        }
    }

    public void setFlags(int flags) {
        try {
            this.mBinder.setFlags(flags);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Failure in setFlags.", e);
        }
    }

    public void setPlaybackToLocal(AudioAttributes attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null for local playback.");
        }
        try {
            this.mBinder.setPlaybackToLocal(attributes);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Failure in setPlaybackToLocal.", e);
        }
    }

    public void setPlaybackToRemote(VolumeProvider volumeProvider) {
        if (volumeProvider == null) {
            throw new IllegalArgumentException("volumeProvider may not be null!");
        }
        synchronized (this.mLock) {
            this.mVolumeProvider = volumeProvider;
        }
        volumeProvider.setCallback(new VolumeProvider.Callback() { // from class: android.media.session.MediaSession.1
            @Override // android.media.VolumeProvider.Callback
            public void onVolumeChanged(VolumeProvider volumeProvider2) {
                MediaSession.this.notifyRemoteVolumeChanged(volumeProvider2);
            }
        });
        try {
            this.mBinder.setPlaybackToRemote(volumeProvider.getVolumeControl(), volumeProvider.getMaxVolume());
            this.mBinder.setCurrentVolume(volumeProvider.getCurrentVolume());
        } catch (RemoteException e) {
            Log.wtf(TAG, "Failure in setPlaybackToRemote.", e);
        }
    }

    public void setActive(boolean active) {
        if (this.mActive == active) {
            return;
        }
        try {
            this.mBinder.setActive(active);
            this.mActive = active;
        } catch (RemoteException e) {
            Log.wtf(TAG, "Failure in setActive.", e);
        }
    }

    public boolean isActive() {
        return this.mActive;
    }

    public void sendSessionEvent(String event, Bundle extras) {
        if (TextUtils.isEmpty(event)) {
            throw new IllegalArgumentException("event cannot be null or empty");
        }
        try {
            this.mBinder.sendEvent(event, extras);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error sending event", e);
        }
    }

    public void release() {
        try {
            this.mBinder.destroy();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error releasing session: ", e);
        }
    }

    public Token getSessionToken() {
        return this.mSessionToken;
    }

    public MediaController getController() {
        return this.mController;
    }

    public void setPlaybackState(PlaybackState state) {
        this.mPlaybackState = state;
        try {
            this.mBinder.setPlaybackState(state);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Dead object in setPlaybackState.", e);
        }
    }

    public void setMetadata(MediaMetadata metadata) {
        if (metadata != null) {
            metadata = new MediaMetadata.Builder(metadata, this.mMaxBitmapSize).build();
        }
        try {
            this.mBinder.setMetadata(metadata);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Dead object in setPlaybackState.", e);
        }
    }

    public void setQueue(List<QueueItem> queue) {
        try {
            this.mBinder.setQueue(queue == null ? null : new ParceledListSlice(queue));
        } catch (RemoteException e) {
            Log.wtf("Dead object in setQueue.", e);
        }
    }

    public void setQueueTitle(CharSequence title) {
        try {
            this.mBinder.setQueueTitle(title);
        } catch (RemoteException e) {
            Log.wtf("Dead object in setQueueTitle.", e);
        }
    }

    public void setRatingType(int type) {
        try {
            this.mBinder.setRatingType(type);
        } catch (RemoteException e) {
            Log.e(TAG, "Error in setRatingType.", e);
        }
    }

    public void setExtras(Bundle extras) {
        try {
            this.mBinder.setExtras(extras);
        } catch (RemoteException e) {
            Log.wtf("Dead object in setExtras.", e);
        }
    }

    public final MediaSessionManager.RemoteUserInfo getCurrentControllerInfo() {
        if (this.mCallback == null || this.mCallback.mCurrentControllerInfo == null) {
            throw new IllegalStateException("This should be called inside of MediaSession.Callback methods");
        }
        return this.mCallback.mCurrentControllerInfo;
    }

    public synchronized void notifyRemoteVolumeChanged(VolumeProvider provider) {
        synchronized (this.mLock) {
            if (provider != null) {
                if (provider == this.mVolumeProvider) {
                    try {
                        this.mBinder.setCurrentVolume(provider.getCurrentVolume());
                        return;
                    } catch (RemoteException e) {
                        Log.e(TAG, "Error in notifyVolumeChanged", e);
                        return;
                    }
                }
            }
            Log.w(TAG, "Received update from stale volume provider");
        }
    }

    private protected String getCallingPackage() {
        if (this.mCallback == null || this.mCallback.mCurrentControllerInfo == null) {
            return null;
        }
        return this.mCallback.mCurrentControllerInfo.getPackageName();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchPrepare(MediaSessionManager.RemoteUserInfo caller) {
        postToCallback(caller, 3, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchPrepareFromMediaId(MediaSessionManager.RemoteUserInfo caller, String mediaId, Bundle extras) {
        postToCallback(caller, 4, mediaId, extras);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchPrepareFromSearch(MediaSessionManager.RemoteUserInfo caller, String query, Bundle extras) {
        postToCallback(caller, 5, query, extras);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchPrepareFromUri(MediaSessionManager.RemoteUserInfo caller, Uri uri, Bundle extras) {
        postToCallback(caller, 6, uri, extras);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchPlay(MediaSessionManager.RemoteUserInfo caller) {
        postToCallback(caller, 7, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchPlayFromMediaId(MediaSessionManager.RemoteUserInfo caller, String mediaId, Bundle extras) {
        postToCallback(caller, 8, mediaId, extras);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchPlayFromSearch(MediaSessionManager.RemoteUserInfo caller, String query, Bundle extras) {
        postToCallback(caller, 9, query, extras);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchPlayFromUri(MediaSessionManager.RemoteUserInfo caller, Uri uri, Bundle extras) {
        postToCallback(caller, 10, uri, extras);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchSkipToItem(MediaSessionManager.RemoteUserInfo caller, long id) {
        postToCallback(caller, 11, Long.valueOf(id), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchPause(MediaSessionManager.RemoteUserInfo caller) {
        postToCallback(caller, 12, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchStop(MediaSessionManager.RemoteUserInfo caller) {
        postToCallback(caller, 13, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchNext(MediaSessionManager.RemoteUserInfo caller) {
        postToCallback(caller, 14, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchPrevious(MediaSessionManager.RemoteUserInfo caller) {
        postToCallback(caller, 15, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchFastForward(MediaSessionManager.RemoteUserInfo caller) {
        postToCallback(caller, 16, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchRewind(MediaSessionManager.RemoteUserInfo caller) {
        postToCallback(caller, 17, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchSeekTo(MediaSessionManager.RemoteUserInfo caller, long pos) {
        postToCallback(caller, 18, Long.valueOf(pos), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchRate(MediaSessionManager.RemoteUserInfo caller, Rating rating) {
        postToCallback(caller, 19, rating, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchCustomAction(MediaSessionManager.RemoteUserInfo caller, String action, Bundle args) {
        postToCallback(caller, 20, action, args);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchMediaButton(MediaSessionManager.RemoteUserInfo caller, Intent mediaButtonIntent) {
        postToCallback(caller, 2, mediaButtonIntent, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchMediaButtonDelayed(MediaSessionManager.RemoteUserInfo info, Intent mediaButtonIntent, long delay) {
        postToCallbackDelayed(info, 23, mediaButtonIntent, null, delay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchAdjustVolume(MediaSessionManager.RemoteUserInfo caller, int direction) {
        postToCallback(caller, 21, Integer.valueOf(direction), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchSetVolumeTo(MediaSessionManager.RemoteUserInfo caller, int volume) {
        postToCallback(caller, 22, Integer.valueOf(volume), null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchCommand(MediaSessionManager.RemoteUserInfo caller, String command, Bundle args, ResultReceiver resultCb) {
        Command cmd = new Command(command, args, resultCb);
        postToCallback(caller, 1, cmd, null);
    }

    private synchronized void postToCallback(MediaSessionManager.RemoteUserInfo caller, int what, Object obj, Bundle data) {
        postToCallbackDelayed(caller, what, obj, data, 0L);
    }

    private synchronized void postToCallbackDelayed(MediaSessionManager.RemoteUserInfo caller, int what, Object obj, Bundle data, long delay) {
        synchronized (this.mLock) {
            if (this.mCallback != null) {
                this.mCallback.post(caller, what, obj, data, delay);
            }
        }
    }

    public static synchronized boolean isActiveState(int state) {
        switch (state) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
                return true;
            case 7:
            default:
                return false;
        }
    }

    /* loaded from: classes.dex */
    public static final class Token implements Parcelable {
        public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() { // from class: android.media.session.MediaSession.Token.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Token createFromParcel(Parcel in) {
                return new Token(ISessionController.Stub.asInterface(in.readStrongBinder()));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Token[] newArray(int size) {
                return new Token[size];
            }
        };
        private ISessionController mBinder;

        public synchronized Token(ISessionController binder) {
            this.mBinder = binder;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStrongBinder(this.mBinder.asBinder());
        }

        public int hashCode() {
            int result = (31 * 1) + (this.mBinder == null ? 0 : this.mBinder.asBinder().hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Token other = (Token) obj;
            if (this.mBinder == null) {
                if (other.mBinder != null) {
                    return false;
                }
            } else if (!this.mBinder.asBinder().equals(other.mBinder.asBinder())) {
                return false;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized ISessionController getBinder() {
            return this.mBinder;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Callback {
        private CallbackMessageHandler mHandler;
        private boolean mMediaPlayPauseKeyPending;
        private MediaSession mSession;

        public void onCommand(String command, Bundle args, ResultReceiver cb) {
        }

        public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
            KeyEvent ke;
            if (this.mSession != null && this.mHandler != null && Intent.ACTION_MEDIA_BUTTON.equals(mediaButtonIntent.getAction()) && (ke = (KeyEvent) mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)) != null && ke.getAction() == 0) {
                PlaybackState state = this.mSession.mPlaybackState;
                long validActions = state == null ? 0L : state.getActions();
                int keyCode = ke.getKeyCode();
                if (keyCode == 79 || keyCode == 85) {
                    if (ke.getRepeatCount() > 0) {
                        handleMediaPlayPauseKeySingleTapIfPending();
                    } else if (this.mMediaPlayPauseKeyPending) {
                        this.mHandler.removeMessages(23);
                        this.mMediaPlayPauseKeyPending = false;
                        if ((validActions & 32) != 0) {
                            onSkipToNext();
                        }
                    } else {
                        this.mMediaPlayPauseKeyPending = true;
                        this.mSession.dispatchMediaButtonDelayed(this.mSession.getCurrentControllerInfo(), mediaButtonIntent, ViewConfiguration.getDoubleTapTimeout());
                    }
                    return true;
                }
                handleMediaPlayPauseKeySingleTapIfPending();
                int keyCode2 = ke.getKeyCode();
                switch (keyCode2) {
                    case 86:
                        if ((1 & validActions) != 0) {
                            onStop();
                            return true;
                        }
                        break;
                    case 87:
                        if ((validActions & 32) != 0) {
                            onSkipToNext();
                            return true;
                        }
                        break;
                    case 88:
                        if ((16 & validActions) != 0) {
                            onSkipToPrevious();
                            return true;
                        }
                        break;
                    case 89:
                        if ((8 & validActions) != 0) {
                            onRewind();
                            return true;
                        }
                        break;
                    case 90:
                        if ((64 & validActions) != 0) {
                            onFastForward();
                            return true;
                        }
                        break;
                    default:
                        switch (keyCode2) {
                            case 126:
                                if ((4 & validActions) != 0) {
                                    onPlay();
                                    return true;
                                }
                                break;
                            case 127:
                                if ((2 & validActions) != 0) {
                                    onPause();
                                    return true;
                                }
                                break;
                        }
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void handleMediaPlayPauseKeySingleTapIfPending() {
            boolean isPlaying;
            boolean canPlay;
            if (!this.mMediaPlayPauseKeyPending) {
                return;
            }
            boolean canPause = false;
            this.mMediaPlayPauseKeyPending = false;
            this.mHandler.removeMessages(23);
            PlaybackState state = this.mSession.mPlaybackState;
            long validActions = state == null ? 0L : state.getActions();
            if (state == null || state.getState() != 3) {
                isPlaying = false;
            } else {
                isPlaying = true;
            }
            if ((516 & validActions) == 0) {
                canPlay = false;
            } else {
                canPlay = true;
            }
            if ((514 & validActions) != 0) {
                canPause = true;
            }
            if (isPlaying && canPause) {
                onPause();
            } else if (!isPlaying && canPlay) {
                onPlay();
            }
        }

        public void onPrepare() {
        }

        public void onPrepareFromMediaId(String mediaId, Bundle extras) {
        }

        public void onPrepareFromSearch(String query, Bundle extras) {
        }

        public void onPrepareFromUri(Uri uri, Bundle extras) {
        }

        public void onPlay() {
        }

        public void onPlayFromSearch(String query, Bundle extras) {
        }

        public void onPlayFromMediaId(String mediaId, Bundle extras) {
        }

        public void onPlayFromUri(Uri uri, Bundle extras) {
        }

        public void onSkipToQueueItem(long id) {
        }

        public void onPause() {
        }

        public void onSkipToNext() {
        }

        public void onSkipToPrevious() {
        }

        public void onFastForward() {
        }

        public void onRewind() {
        }

        public void onStop() {
        }

        public void onSeekTo(long pos) {
        }

        public void onSetRating(Rating rating) {
        }

        public void onCustomAction(String action, Bundle extras) {
        }
    }

    /* loaded from: classes.dex */
    public static class CallbackStub extends ISessionCallback.Stub {
        private WeakReference<MediaSession> mMediaSession;

        public synchronized CallbackStub(MediaSession session) {
            this.mMediaSession = new WeakReference<>(session);
        }

        private static synchronized MediaSessionManager.RemoteUserInfo createRemoteUserInfo(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            return new MediaSessionManager.RemoteUserInfo(packageName, pid, uid, caller != null ? caller.asBinder() : null);
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onCommand(String packageName, int pid, int uid, ISessionControllerCallback caller, String command, Bundle args, ResultReceiver cb) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchCommand(createRemoteUserInfo(packageName, pid, uid, caller), command, args, cb);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onMediaButton(String packageName, int pid, int uid, Intent mediaButtonIntent, int sequenceNumber, ResultReceiver cb) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                try {
                    session.dispatchMediaButton(createRemoteUserInfo(packageName, pid, uid, null), mediaButtonIntent);
                } finally {
                    if (cb != null) {
                        cb.send(sequenceNumber, null);
                    }
                }
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onMediaButtonFromController(String packageName, int pid, int uid, ISessionControllerCallback caller, Intent mediaButtonIntent) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchMediaButton(createRemoteUserInfo(packageName, pid, uid, caller), mediaButtonIntent);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onPrepare(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchPrepare(createRemoteUserInfo(packageName, pid, uid, caller));
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onPrepareFromMediaId(String packageName, int pid, int uid, ISessionControllerCallback caller, String mediaId, Bundle extras) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchPrepareFromMediaId(createRemoteUserInfo(packageName, pid, uid, caller), mediaId, extras);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onPrepareFromSearch(String packageName, int pid, int uid, ISessionControllerCallback caller, String query, Bundle extras) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchPrepareFromSearch(createRemoteUserInfo(packageName, pid, uid, caller), query, extras);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onPrepareFromUri(String packageName, int pid, int uid, ISessionControllerCallback caller, Uri uri, Bundle extras) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchPrepareFromUri(createRemoteUserInfo(packageName, pid, uid, caller), uri, extras);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onPlay(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchPlay(createRemoteUserInfo(packageName, pid, uid, caller));
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onPlayFromMediaId(String packageName, int pid, int uid, ISessionControllerCallback caller, String mediaId, Bundle extras) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchPlayFromMediaId(createRemoteUserInfo(packageName, pid, uid, caller), mediaId, extras);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onPlayFromSearch(String packageName, int pid, int uid, ISessionControllerCallback caller, String query, Bundle extras) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchPlayFromSearch(createRemoteUserInfo(packageName, pid, uid, caller), query, extras);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onPlayFromUri(String packageName, int pid, int uid, ISessionControllerCallback caller, Uri uri, Bundle extras) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchPlayFromUri(createRemoteUserInfo(packageName, pid, uid, caller), uri, extras);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onSkipToTrack(String packageName, int pid, int uid, ISessionControllerCallback caller, long id) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchSkipToItem(createRemoteUserInfo(packageName, pid, uid, caller), id);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onPause(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchPause(createRemoteUserInfo(packageName, pid, uid, caller));
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onStop(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchStop(createRemoteUserInfo(packageName, pid, uid, caller));
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onNext(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchNext(createRemoteUserInfo(packageName, pid, uid, caller));
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onPrevious(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchPrevious(createRemoteUserInfo(packageName, pid, uid, caller));
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onFastForward(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchFastForward(createRemoteUserInfo(packageName, pid, uid, caller));
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onRewind(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchRewind(createRemoteUserInfo(packageName, pid, uid, caller));
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onSeekTo(String packageName, int pid, int uid, ISessionControllerCallback caller, long pos) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchSeekTo(createRemoteUserInfo(packageName, pid, uid, caller), pos);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onRate(String packageName, int pid, int uid, ISessionControllerCallback caller, Rating rating) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchRate(createRemoteUserInfo(packageName, pid, uid, caller), rating);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onCustomAction(String packageName, int pid, int uid, ISessionControllerCallback caller, String action, Bundle args) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchCustomAction(createRemoteUserInfo(packageName, pid, uid, caller), action, args);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onAdjustVolume(String packageName, int pid, int uid, ISessionControllerCallback caller, int direction) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchAdjustVolume(createRemoteUserInfo(packageName, pid, uid, caller), direction);
            }
        }

        @Override // android.media.session.ISessionCallback
        public synchronized void onSetVolumeTo(String packageName, int pid, int uid, ISessionControllerCallback caller, int value) {
            MediaSession session = this.mMediaSession.get();
            if (session != null) {
                session.dispatchSetVolumeTo(createRemoteUserInfo(packageName, pid, uid, caller), value);
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class QueueItem implements Parcelable {
        public static final Parcelable.Creator<QueueItem> CREATOR = new Parcelable.Creator<QueueItem>() { // from class: android.media.session.MediaSession.QueueItem.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public QueueItem createFromParcel(Parcel p) {
                return new QueueItem(p);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public QueueItem[] newArray(int size) {
                return new QueueItem[size];
            }
        };
        public static final int UNKNOWN_ID = -1;
        private final MediaDescription mDescription;
        public protected final long mId;

        public QueueItem(MediaDescription description, long id) {
            if (description == null) {
                throw new IllegalArgumentException("Description cannot be null.");
            }
            if (id == -1) {
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
            this.mDescription = description;
            this.mId = id;
        }

        private synchronized QueueItem(Parcel in) {
            this.mDescription = MediaDescription.CREATOR.createFromParcel(in);
            this.mId = in.readLong();
        }

        public MediaDescription getDescription() {
            return this.mDescription;
        }

        public long getQueueId() {
            return this.mId;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            this.mDescription.writeToParcel(dest, flags);
            dest.writeLong(this.mId);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public String toString() {
            return "MediaSession.QueueItem {Description=" + this.mDescription + ", Id=" + this.mId + " }";
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof QueueItem)) {
                return false;
            }
            QueueItem item = (QueueItem) o;
            if (this.mId != item.mId || !Objects.equals(this.mDescription, item.mDescription)) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Command {
        public final String command;
        public final Bundle extras;
        public final ResultReceiver stub;

        public synchronized Command(String command, Bundle extras, ResultReceiver stub) {
            this.command = command;
            this.extras = extras;
            this.stub = stub;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CallbackMessageHandler extends Handler {
        private static final int MSG_ADJUST_VOLUME = 21;
        private static final int MSG_COMMAND = 1;
        private static final int MSG_CUSTOM_ACTION = 20;
        private static final int MSG_FAST_FORWARD = 16;
        private static final int MSG_MEDIA_BUTTON = 2;
        private static final int MSG_NEXT = 14;
        private static final int MSG_PAUSE = 12;
        private static final int MSG_PLAY = 7;
        private static final int MSG_PLAY_MEDIA_ID = 8;
        private static final int MSG_PLAY_PAUSE_KEY_DOUBLE_TAP_TIMEOUT = 23;
        private static final int MSG_PLAY_SEARCH = 9;
        private static final int MSG_PLAY_URI = 10;
        private static final int MSG_PREPARE = 3;
        private static final int MSG_PREPARE_MEDIA_ID = 4;
        private static final int MSG_PREPARE_SEARCH = 5;
        private static final int MSG_PREPARE_URI = 6;
        private static final int MSG_PREVIOUS = 15;
        private static final int MSG_RATE = 19;
        private static final int MSG_REWIND = 17;
        private static final int MSG_SEEK_TO = 18;
        private static final int MSG_SET_VOLUME = 22;
        private static final int MSG_SKIP_TO_ITEM = 11;
        private static final int MSG_STOP = 13;
        private Callback mCallback;
        private MediaSessionManager.RemoteUserInfo mCurrentControllerInfo;

        public CallbackMessageHandler(Looper looper, Callback callback) {
            super(looper, null, true);
            this.mCallback = callback;
            this.mCallback.mHandler = this;
        }

        public synchronized void post(MediaSessionManager.RemoteUserInfo caller, int what, Object obj, Bundle data, long delayMs) {
            Pair<MediaSessionManager.RemoteUserInfo, Object> objWithCaller = Pair.create(caller, obj);
            Message msg = obtainMessage(what, objWithCaller);
            msg.setData(data);
            if (delayMs > 0) {
                sendMessageDelayed(msg, delayMs);
            } else {
                sendMessage(msg);
            }
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            VolumeProvider vp;
            VolumeProvider vp2;
            this.mCurrentControllerInfo = (MediaSessionManager.RemoteUserInfo) ((Pair) msg.obj).first;
            Object obj = ((Pair) msg.obj).second;
            switch (msg.what) {
                case 1:
                    Command cmd = (Command) obj;
                    this.mCallback.onCommand(cmd.command, cmd.extras, cmd.stub);
                    break;
                case 2:
                    this.mCallback.onMediaButtonEvent((Intent) obj);
                    break;
                case 3:
                    this.mCallback.onPrepare();
                    break;
                case 4:
                    this.mCallback.onPrepareFromMediaId((String) obj, msg.getData());
                    break;
                case 5:
                    this.mCallback.onPrepareFromSearch((String) obj, msg.getData());
                    break;
                case 6:
                    this.mCallback.onPrepareFromUri((Uri) obj, msg.getData());
                    break;
                case 7:
                    this.mCallback.onPlay();
                    break;
                case 8:
                    this.mCallback.onPlayFromMediaId((String) obj, msg.getData());
                    break;
                case 9:
                    this.mCallback.onPlayFromSearch((String) obj, msg.getData());
                    break;
                case 10:
                    this.mCallback.onPlayFromUri((Uri) obj, msg.getData());
                    break;
                case 11:
                    this.mCallback.onSkipToQueueItem(((Long) obj).longValue());
                    break;
                case 12:
                    this.mCallback.onPause();
                    break;
                case 13:
                    this.mCallback.onStop();
                    break;
                case 14:
                    this.mCallback.onSkipToNext();
                    break;
                case 15:
                    this.mCallback.onSkipToPrevious();
                    break;
                case 16:
                    this.mCallback.onFastForward();
                    break;
                case 17:
                    this.mCallback.onRewind();
                    break;
                case 18:
                    this.mCallback.onSeekTo(((Long) obj).longValue());
                    break;
                case 19:
                    this.mCallback.onSetRating((Rating) obj);
                    break;
                case 20:
                    this.mCallback.onCustomAction((String) obj, msg.getData());
                    break;
                case 21:
                    synchronized (MediaSession.this.mLock) {
                        vp = MediaSession.this.mVolumeProvider;
                    }
                    if (vp != null) {
                        vp.onAdjustVolume(((Integer) obj).intValue());
                        break;
                    }
                    break;
                case 22:
                    synchronized (MediaSession.this.mLock) {
                        vp2 = MediaSession.this.mVolumeProvider;
                    }
                    if (vp2 != null) {
                        vp2.onSetVolumeTo(((Integer) obj).intValue());
                        break;
                    }
                    break;
                case 23:
                    this.mCallback.handleMediaPlayPauseKeySingleTapIfPending();
                    break;
            }
            this.mCurrentControllerInfo = null;
        }
    }
}
