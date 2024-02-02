package android.media.session;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.media.IRemoteVolumeController;
import android.media.ISessionTokensListener;
import android.media.SessionToken2;
import android.media.session.IActiveSessionsListener;
import android.media.session.ICallback;
import android.media.session.IOnMediaKeyListener;
import android.media.session.IOnVolumeKeyLongPressListener;
import android.media.session.ISessionController;
import android.media.session.ISessionManager;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class MediaSessionManager {
    public static final int RESULT_MEDIA_KEY_HANDLED = 1;
    public static final int RESULT_MEDIA_KEY_NOT_HANDLED = 0;
    private static final String TAG = "SessionManager";
    private CallbackImpl mCallback;
    private Context mContext;
    private OnMediaKeyListenerImpl mOnMediaKeyListener;
    private OnVolumeKeyLongPressListenerImpl mOnVolumeKeyLongPressListener;
    private final ISessionManager mService;
    private CallbackImpl mXuiCallback;
    private final ArrayMap<OnActiveSessionsChangedListener, SessionsChangedWrapper> mListeners = new ArrayMap<>();
    private final ArrayMap<OnSessionTokensChangedListener, SessionTokensChangedWrapper> mSessionTokensListener = new ArrayMap<>();
    private final Object mLock = new Object();

    /* loaded from: classes.dex */
    public static abstract class Callback {
        public abstract synchronized void onAddressedPlayerChanged(ComponentName componentName);

        public abstract synchronized void onAddressedPlayerChanged(MediaSession.Token token);

        public abstract synchronized void onMediaKeyEventDispatched(KeyEvent keyEvent, ComponentName componentName);

        public abstract synchronized void onMediaKeyEventDispatched(KeyEvent keyEvent, MediaSession.Token token);
    }

    /* loaded from: classes.dex */
    public interface OnActiveSessionsChangedListener {
        void onActiveSessionsChanged(List<MediaController> list);
    }

    @SystemApi
    /* loaded from: classes.dex */
    public interface OnMediaKeyListener {
        boolean onMediaKey(KeyEvent keyEvent);
    }

    /* loaded from: classes.dex */
    public interface OnSessionTokensChangedListener {
        synchronized void onSessionTokensChanged(List<SessionToken2> list);
    }

    @SystemApi
    /* loaded from: classes.dex */
    public interface OnVolumeKeyLongPressListener {
        void onVolumeKeyLongPress(KeyEvent keyEvent);
    }

    public synchronized MediaSessionManager(Context context) {
        this.mContext = context;
        IBinder b = ServiceManager.getService(Context.MEDIA_SESSION_SERVICE);
        this.mService = ISessionManager.Stub.asInterface(b);
    }

    public synchronized ISession createSession(MediaSession.CallbackStub cbStub, String tag, int userId) throws RemoteException {
        return this.mService.createSession(this.mContext.getPackageName(), cbStub, tag, userId);
    }

    public List<MediaController> getActiveSessions(ComponentName notificationListener) {
        return getActiveSessionsForUser(notificationListener, UserHandle.myUserId());
    }

    private protected List<MediaController> getActiveSessionsForUser(ComponentName notificationListener, int userId) {
        ArrayList<MediaController> controllers = new ArrayList<>();
        try {
            List<IBinder> binders = this.mService.getSessions(notificationListener, userId);
            int size = binders.size();
            for (int i = 0; i < size; i++) {
                MediaController controller = new MediaController(this.mContext, ISessionController.Stub.asInterface(binders.get(i)));
                controllers.add(controller);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get active sessions: ", e);
        }
        return controllers;
    }

    public void addOnActiveSessionsChangedListener(OnActiveSessionsChangedListener sessionListener, ComponentName notificationListener) {
        addOnActiveSessionsChangedListener(sessionListener, notificationListener, null);
    }

    public void addOnActiveSessionsChangedListener(OnActiveSessionsChangedListener sessionListener, ComponentName notificationListener, Handler handler) {
        addOnActiveSessionsChangedListener(sessionListener, notificationListener, UserHandle.myUserId(), handler);
    }

    public synchronized void addOnActiveSessionsChangedListener(OnActiveSessionsChangedListener sessionListener, ComponentName notificationListener, int userId, Handler handler) {
        Handler handler2;
        if (sessionListener == null) {
            throw new IllegalArgumentException("listener may not be null");
        }
        if (handler == null) {
            handler2 = new Handler();
        } else {
            handler2 = handler;
        }
        synchronized (this.mLock) {
            if (this.mListeners.get(sessionListener) != null) {
                Log.w(TAG, "Attempted to add session listener twice, ignoring.");
                return;
            }
            SessionsChangedWrapper wrapper = new SessionsChangedWrapper(this.mContext, sessionListener, handler2);
            try {
                this.mService.addSessionsListener(wrapper.mStub, notificationListener, userId);
                this.mListeners.put(sessionListener, wrapper);
            } catch (RemoteException e) {
                Log.e(TAG, "Error in addOnActiveSessionsChangedListener.", e);
            }
        }
    }

    public void removeOnActiveSessionsChangedListener(OnActiveSessionsChangedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener may not be null");
        }
        synchronized (this.mLock) {
            SessionsChangedWrapper wrapper = this.mListeners.remove(listener);
            if (wrapper != null) {
                try {
                    this.mService.removeSessionsListener(wrapper.mStub);
                    wrapper.release();
                } catch (RemoteException e) {
                    Log.e(TAG, "Error in removeOnActiveSessionsChangedListener.", e);
                    wrapper.release();
                }
            }
        }
    }

    public synchronized void setRemoteVolumeController(IRemoteVolumeController rvc) {
        try {
            this.mService.setRemoteVolumeController(rvc);
        } catch (RemoteException e) {
            Log.e(TAG, "Error in setRemoteVolumeController.", e);
        }
    }

    public synchronized void dispatchMediaKeyEvent(KeyEvent keyEvent) {
        dispatchMediaKeyEvent(keyEvent, false);
    }

    public synchronized void dispatchMediaKeyEvent(KeyEvent keyEvent, boolean needWakeLock) {
        dispatchMediaKeyEventInternal(false, keyEvent, needWakeLock);
    }

    public synchronized void dispatchMediaKeyEventAsSystemService(KeyEvent keyEvent) {
        dispatchMediaKeyEventInternal(true, keyEvent, false);
    }

    private synchronized void dispatchMediaKeyEventInternal(boolean asSystemService, KeyEvent keyEvent, boolean needWakeLock) {
        try {
            this.mService.dispatchMediaKeyEvent(this.mContext.getPackageName(), asSystemService, keyEvent, needWakeLock);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to send key event.", e);
        }
    }

    public synchronized void dispatchVolumeKeyEvent(KeyEvent keyEvent, int stream, boolean musicOnly) {
        dispatchVolumeKeyEventInternal(false, keyEvent, stream, musicOnly);
    }

    public synchronized void dispatchVolumeKeyEventAsSystemService(KeyEvent keyEvent, int streamType) {
        dispatchVolumeKeyEventInternal(true, keyEvent, streamType, false);
    }

    private synchronized void dispatchVolumeKeyEventInternal(boolean asSystemService, KeyEvent keyEvent, int stream, boolean musicOnly) {
        try {
            this.mService.dispatchVolumeKeyEvent(this.mContext.getPackageName(), asSystemService, keyEvent, stream, musicOnly);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to send volume key event.", e);
        }
    }

    public synchronized void dispatchAdjustVolume(int suggestedStream, int direction, int flags) {
        try {
            this.mService.dispatchAdjustVolume(this.mContext.getPackageName(), suggestedStream, direction, flags);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to send adjust volume.", e);
        }
    }

    public boolean isTrustedForMediaControl(RemoteUserInfo userInfo) {
        if (userInfo == null) {
            throw new IllegalArgumentException("userInfo may not be null");
        }
        if (userInfo.getPackageName() == null) {
            return false;
        }
        try {
            return this.mService.isTrusted(userInfo.getPackageName(), userInfo.getPid(), userInfo.getUid());
        } catch (RemoteException e) {
            Log.wtf(TAG, "Cannot communicate with the service.", e);
            return false;
        }
    }

    public synchronized boolean createSession2(SessionToken2 token) {
        if (token == null) {
            return false;
        }
        try {
            return this.mService.createSession2(token.toBundle());
        } catch (RemoteException e) {
            Log.wtf(TAG, "Cannot communicate with the service.", e);
            return false;
        }
    }

    public synchronized void destroySession2(SessionToken2 token) {
        if (token == null) {
            return;
        }
        try {
            this.mService.destroySession2(token.toBundle());
        } catch (RemoteException e) {
            Log.wtf(TAG, "Cannot communicate with the service.", e);
        }
    }

    public synchronized List<SessionToken2> getActiveSessionTokens() {
        try {
            List<Bundle> bundles = this.mService.getSessionTokens(true, false, this.mContext.getPackageName());
            return toTokenList(bundles);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Cannot communicate with the service.", e);
            return Collections.emptyList();
        }
    }

    public synchronized List<SessionToken2> getSessionServiceTokens() {
        try {
            List<Bundle> bundles = this.mService.getSessionTokens(false, true, this.mContext.getPackageName());
            return toTokenList(bundles);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Cannot communicate with the service.", e);
            return Collections.emptyList();
        }
    }

    public synchronized List<SessionToken2> getAllSessionTokens() {
        try {
            List<Bundle> bundles = this.mService.getSessionTokens(false, false, this.mContext.getPackageName());
            return toTokenList(bundles);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Cannot communicate with the service.", e);
            return Collections.emptyList();
        }
    }

    public synchronized void addOnSessionTokensChangedListener(Executor executor, OnSessionTokensChangedListener listener) {
        addOnSessionTokensChangedListener(UserHandle.myUserId(), executor, listener);
    }

    public synchronized void addOnSessionTokensChangedListener(int userId, Executor executor, OnSessionTokensChangedListener listener) {
        if (executor == null) {
            throw new IllegalArgumentException("executor may not be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener may not be null");
        }
        synchronized (this.mLock) {
            if (this.mSessionTokensListener.get(listener) != null) {
                Log.w(TAG, "Attempted to add session listener twice, ignoring.");
                return;
            }
            SessionTokensChangedWrapper wrapper = new SessionTokensChangedWrapper(this.mContext, executor, listener);
            try {
                this.mService.addSessionTokensListener(wrapper.mStub, userId, this.mContext.getPackageName());
                this.mSessionTokensListener.put(listener, wrapper);
            } catch (RemoteException e) {
                Log.e(TAG, "Error in addSessionTokensListener.", e);
            }
        }
    }

    public synchronized void removeOnSessionTokensChangedListener(OnSessionTokensChangedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener may not be null");
        }
        synchronized (this.mLock) {
            SessionTokensChangedWrapper wrapper = this.mSessionTokensListener.remove(listener);
            if (wrapper != null) {
                try {
                    this.mService.removeSessionTokensListener(wrapper.mStub, this.mContext.getPackageName());
                    wrapper.release();
                } catch (RemoteException e) {
                    Log.e(TAG, "Error in removeSessionTokensListener.", e);
                    wrapper.release();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized List<SessionToken2> toTokenList(List<Bundle> bundles) {
        List<SessionToken2> tokens = new ArrayList<>();
        if (bundles != null) {
            for (int i = 0; i < bundles.size(); i++) {
                SessionToken2 token = SessionToken2.fromBundle(bundles.get(i));
                if (token != null) {
                    tokens.add(token);
                }
            }
        }
        return tokens;
    }

    public synchronized boolean isGlobalPriorityActive() {
        try {
            return this.mService.isGlobalPriorityActive();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to check if the global priority is active.", e);
            return false;
        }
    }

    @SystemApi
    public void setOnVolumeKeyLongPressListener(OnVolumeKeyLongPressListener listener, Handler handler) {
        synchronized (this.mLock) {
            try {
                try {
                    if (listener == null) {
                        this.mOnVolumeKeyLongPressListener = null;
                        this.mService.setOnVolumeKeyLongPressListener(null);
                    } else {
                        if (handler == null) {
                            handler = new Handler();
                        }
                        this.mOnVolumeKeyLongPressListener = new OnVolumeKeyLongPressListenerImpl(listener, handler);
                        this.mService.setOnVolumeKeyLongPressListener(this.mOnVolumeKeyLongPressListener);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to set volume key long press listener", e);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @SystemApi
    public void setOnMediaKeyListener(OnMediaKeyListener listener, Handler handler) {
        synchronized (this.mLock) {
            try {
                try {
                    if (listener == null) {
                        this.mOnMediaKeyListener = null;
                        this.mService.setOnMediaKeyListener(null);
                    } else {
                        if (handler == null) {
                            handler = new Handler();
                        }
                        this.mOnMediaKeyListener = new OnMediaKeyListenerImpl(listener, handler);
                        this.mService.setOnMediaKeyListener(this.mOnMediaKeyListener);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to set media key listener", e);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public synchronized void setCallback(Callback callback, Handler handler) {
        synchronized (this.mLock) {
            try {
                try {
                    if (callback == null) {
                        this.mCallback = null;
                        this.mService.setCallback(null);
                    } else {
                        if (handler == null) {
                            handler = new Handler();
                        }
                        this.mCallback = new CallbackImpl(callback, handler);
                        this.mService.setCallback(this.mCallback);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to set media key callback", e);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void setXuiCallback(Callback callback, Handler handler) {
        synchronized (this.mLock) {
            try {
                try {
                    if (callback == null) {
                        this.mXuiCallback = null;
                        this.mService.setXuiCallback(null);
                    } else {
                        if (handler == null) {
                            handler = new Handler();
                        }
                        this.mXuiCallback = new CallbackImpl(callback, handler);
                        this.mService.setXuiCallback(this.mXuiCallback);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to set xui media key callback", e);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class RemoteUserInfo {
        private final IBinder mCallerBinder;
        private final String mPackageName;
        private final int mPid;
        private final int mUid;

        public RemoteUserInfo(String packageName, int pid, int uid) {
            this(packageName, pid, uid, null);
        }

        public synchronized RemoteUserInfo(String packageName, int pid, int uid, IBinder callerBinder) {
            this.mPackageName = packageName;
            this.mPid = pid;
            this.mUid = uid;
            this.mCallerBinder = callerBinder;
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public int getPid() {
            return this.mPid;
        }

        public int getUid() {
            return this.mUid;
        }

        public boolean equals(Object obj) {
            if (obj instanceof RemoteUserInfo) {
                if (this == obj) {
                    return true;
                }
                RemoteUserInfo otherUserInfo = (RemoteUserInfo) obj;
                if (this.mCallerBinder == null || otherUserInfo.mCallerBinder == null) {
                    return false;
                }
                return this.mCallerBinder.equals(otherUserInfo.mCallerBinder);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.mPackageName, Integer.valueOf(this.mPid), Integer.valueOf(this.mUid));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SessionsChangedWrapper {
        private Context mContext;
        private Handler mHandler;
        private OnActiveSessionsChangedListener mListener;
        private final IActiveSessionsListener.Stub mStub = new IActiveSessionsListener.Stub() { // from class: android.media.session.MediaSessionManager.SessionsChangedWrapper.1
            @Override // android.media.session.IActiveSessionsListener
            public void onActiveSessionsChanged(final List<MediaSession.Token> tokens) {
                Handler handler = SessionsChangedWrapper.this.mHandler;
                if (handler != null) {
                    handler.post(new Runnable() { // from class: android.media.session.MediaSessionManager.SessionsChangedWrapper.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Context context = SessionsChangedWrapper.this.mContext;
                            if (context != null) {
                                ArrayList<MediaController> controllers = new ArrayList<>();
                                int size = tokens.size();
                                for (int i = 0; i < size; i++) {
                                    controllers.add(new MediaController(context, (MediaSession.Token) tokens.get(i)));
                                }
                                OnActiveSessionsChangedListener listener = SessionsChangedWrapper.this.mListener;
                                if (listener != null) {
                                    listener.onActiveSessionsChanged(controllers);
                                }
                            }
                        }
                    });
                }
            }
        };

        public synchronized SessionsChangedWrapper(Context context, OnActiveSessionsChangedListener listener, Handler handler) {
            this.mContext = context;
            this.mListener = listener;
            this.mHandler = handler;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void release() {
            this.mListener = null;
            this.mContext = null;
            this.mHandler = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SessionTokensChangedWrapper {
        private Context mContext;
        private Executor mExecutor;
        private OnSessionTokensChangedListener mListener;
        private final ISessionTokensListener.Stub mStub = new AnonymousClass1();

        public synchronized SessionTokensChangedWrapper(Context context, Executor executor, OnSessionTokensChangedListener listener) {
            this.mContext = context;
            this.mExecutor = executor;
            this.mListener = listener;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: android.media.session.MediaSessionManager$SessionTokensChangedWrapper$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass1 extends ISessionTokensListener.Stub {
            AnonymousClass1() {
            }

            @Override // android.media.ISessionTokensListener
            public void onSessionTokensChanged(final List<Bundle> bundles) {
                Executor executor = SessionTokensChangedWrapper.this.mExecutor;
                if (executor != null) {
                    executor.execute(new Runnable() { // from class: android.media.session.-$$Lambda$MediaSessionManager$SessionTokensChangedWrapper$1$wkYv3P0_Sdm0wRGnCFHp-AGf3Dw
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaSessionManager.SessionTokensChangedWrapper.AnonymousClass1.lambda$onSessionTokensChanged$0(MediaSessionManager.SessionTokensChangedWrapper.AnonymousClass1.this, bundles);
                        }
                    });
                }
            }

            public static /* synthetic */ void lambda$onSessionTokensChanged$0(AnonymousClass1 anonymousClass1, List bundles) {
                Context context = SessionTokensChangedWrapper.this.mContext;
                OnSessionTokensChangedListener listener = SessionTokensChangedWrapper.this.mListener;
                if (context != null && listener != null) {
                    listener.onSessionTokensChanged(MediaSessionManager.toTokenList(bundles));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void release() {
            this.mListener = null;
            this.mContext = null;
            this.mExecutor = null;
        }
    }

    /* loaded from: classes.dex */
    private static final class OnVolumeKeyLongPressListenerImpl extends IOnVolumeKeyLongPressListener.Stub {
        private Handler mHandler;
        private OnVolumeKeyLongPressListener mListener;

        public synchronized OnVolumeKeyLongPressListenerImpl(OnVolumeKeyLongPressListener listener, Handler handler) {
            this.mListener = listener;
            this.mHandler = handler;
        }

        @Override // android.media.session.IOnVolumeKeyLongPressListener
        public synchronized void onVolumeKeyLongPress(final KeyEvent event) {
            if (this.mListener == null || this.mHandler == null) {
                Log.w(MediaSessionManager.TAG, "Failed to call volume key long-press listener. Either mListener or mHandler is null");
            } else {
                this.mHandler.post(new Runnable() { // from class: android.media.session.MediaSessionManager.OnVolumeKeyLongPressListenerImpl.1
                    @Override // java.lang.Runnable
                    public void run() {
                        OnVolumeKeyLongPressListenerImpl.this.mListener.onVolumeKeyLongPress(event);
                    }
                });
            }
        }
    }

    /* loaded from: classes.dex */
    private static final class OnMediaKeyListenerImpl extends IOnMediaKeyListener.Stub {
        private Handler mHandler;
        private OnMediaKeyListener mListener;

        public synchronized OnMediaKeyListenerImpl(OnMediaKeyListener listener, Handler handler) {
            this.mListener = listener;
            this.mHandler = handler;
        }

        @Override // android.media.session.IOnMediaKeyListener
        public synchronized void onMediaKey(final KeyEvent event, final ResultReceiver result) {
            if (this.mListener == null || this.mHandler == null) {
                Log.w(MediaSessionManager.TAG, "Failed to call media key listener. Either mListener or mHandler is null");
            } else {
                this.mHandler.post(new Runnable() { // from class: android.media.session.MediaSessionManager.OnMediaKeyListenerImpl.1
                    @Override // java.lang.Runnable
                    public void run() {
                        boolean handled = OnMediaKeyListenerImpl.this.mListener.onMediaKey(event);
                        Log.d(MediaSessionManager.TAG, "The media key listener is returned " + handled);
                        if (result != null) {
                            result.send(handled ? 1 : 0, null);
                        }
                    }
                });
            }
        }
    }

    /* loaded from: classes.dex */
    private static final class CallbackImpl extends ICallback.Stub {
        private final Callback mCallback;
        private final Handler mHandler;

        public synchronized CallbackImpl(Callback callback, Handler handler) {
            this.mCallback = callback;
            this.mHandler = handler;
        }

        @Override // android.media.session.ICallback
        public synchronized void onMediaKeyEventDispatchedToMediaSession(final KeyEvent event, final MediaSession.Token sessionToken) {
            this.mHandler.post(new Runnable() { // from class: android.media.session.MediaSessionManager.CallbackImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    CallbackImpl.this.mCallback.onMediaKeyEventDispatched(event, sessionToken);
                }
            });
        }

        @Override // android.media.session.ICallback
        public synchronized void onMediaKeyEventDispatchedToMediaButtonReceiver(final KeyEvent event, final ComponentName mediaButtonReceiver) {
            this.mHandler.post(new Runnable() { // from class: android.media.session.MediaSessionManager.CallbackImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    CallbackImpl.this.mCallback.onMediaKeyEventDispatched(event, mediaButtonReceiver);
                }
            });
        }

        @Override // android.media.session.ICallback
        public synchronized void onAddressedPlayerChangedToMediaSession(final MediaSession.Token sessionToken) {
            this.mHandler.post(new Runnable() { // from class: android.media.session.MediaSessionManager.CallbackImpl.3
                @Override // java.lang.Runnable
                public void run() {
                    CallbackImpl.this.mCallback.onAddressedPlayerChanged(sessionToken);
                }
            });
        }

        @Override // android.media.session.ICallback
        public synchronized void onAddressedPlayerChangedToMediaButtonReceiver(final ComponentName mediaButtonReceiver) {
            this.mHandler.post(new Runnable() { // from class: android.media.session.MediaSessionManager.CallbackImpl.4
                @Override // java.lang.Runnable
                public void run() {
                    CallbackImpl.this.mCallback.onAddressedPlayerChanged(mediaButtonReceiver);
                }
            });
        }
    }
}
