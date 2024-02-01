package android.media.tv;

import android.annotation.SystemApi;
import android.content.Context;
import android.media.tv.TvInputManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import java.util.ArrayDeque;
import java.util.Queue;
/* loaded from: classes2.dex */
public class TvRecordingClient {
    private static final boolean DEBUG = false;
    private static final String TAG = "TvRecordingClient";
    private final RecordingCallback mCallback;
    private final Handler mHandler;
    private boolean mIsRecordingStarted;
    private boolean mIsTuned;
    private final Queue<Pair<String, Bundle>> mPendingAppPrivateCommands = new ArrayDeque();
    private TvInputManager.Session mSession;
    private MySessionCallback mSessionCallback;
    private final TvInputManager mTvInputManager;

    public TvRecordingClient(Context context, String tag, RecordingCallback callback, Handler handler) {
        this.mCallback = callback;
        this.mHandler = handler == null ? new Handler(Looper.getMainLooper()) : handler;
        this.mTvInputManager = (TvInputManager) context.getSystemService(Context.TV_INPUT_SERVICE);
    }

    public void tune(String inputId, Uri channelUri) {
        tune(inputId, channelUri, null);
    }

    public void tune(String inputId, Uri channelUri, Bundle params) {
        if (TextUtils.isEmpty(inputId)) {
            throw new IllegalArgumentException("inputId cannot be null or an empty string");
        }
        if (this.mIsRecordingStarted) {
            throw new IllegalStateException("tune failed - recording already started");
        }
        if (this.mSessionCallback != null && TextUtils.equals(this.mSessionCallback.mInputId, inputId)) {
            if (this.mSession != null) {
                this.mSession.tune(channelUri, params);
                return;
            }
            this.mSessionCallback.mChannelUri = channelUri;
            this.mSessionCallback.mConnectionParams = params;
            return;
        }
        resetInternal();
        this.mSessionCallback = new MySessionCallback(inputId, channelUri, params);
        if (this.mTvInputManager != null) {
            this.mTvInputManager.createRecordingSession(inputId, this.mSessionCallback, this.mHandler);
        }
    }

    public void release() {
        resetInternal();
    }

    private synchronized void resetInternal() {
        this.mSessionCallback = null;
        this.mPendingAppPrivateCommands.clear();
        if (this.mSession != null) {
            this.mSession.release();
            this.mSession = null;
        }
    }

    public void startRecording(Uri programUri) {
        if (!this.mIsTuned) {
            throw new IllegalStateException("startRecording failed - not yet tuned");
        }
        if (this.mSession != null) {
            this.mSession.startRecording(programUri);
            this.mIsRecordingStarted = true;
        }
    }

    public void stopRecording() {
        if (!this.mIsRecordingStarted) {
            Log.w(TAG, "stopRecording failed - recording not yet started");
        }
        if (this.mSession != null) {
            this.mSession.stopRecording();
        }
    }

    public void sendAppPrivateCommand(String action, Bundle data) {
        if (TextUtils.isEmpty(action)) {
            throw new IllegalArgumentException("action cannot be null or an empty string");
        }
        if (this.mSession != null) {
            this.mSession.sendAppPrivateCommand(action, data);
            return;
        }
        Log.w(TAG, "sendAppPrivateCommand - session not yet created (action \"" + action + "\" pending)");
        this.mPendingAppPrivateCommands.add(Pair.create(action, data));
    }

    /* loaded from: classes2.dex */
    public static abstract class RecordingCallback {
        public void onConnectionFailed(String inputId) {
        }

        public void onDisconnected(String inputId) {
        }

        public void onTuned(Uri channelUri) {
        }

        public void onRecordingStopped(Uri recordedProgramUri) {
        }

        public void onError(int error) {
        }

        @SystemApi
        public void onEvent(String inputId, String eventType, Bundle eventArgs) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class MySessionCallback extends TvInputManager.SessionCallback {
        Uri mChannelUri;
        Bundle mConnectionParams;
        final String mInputId;

        MySessionCallback(String inputId, Uri channelUri, Bundle connectionParams) {
            this.mInputId = inputId;
            this.mChannelUri = channelUri;
            this.mConnectionParams = connectionParams;
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onSessionCreated(TvInputManager.Session session) {
            if (this == TvRecordingClient.this.mSessionCallback) {
                TvRecordingClient.this.mSession = session;
                if (session != null) {
                    for (Pair<String, Bundle> command : TvRecordingClient.this.mPendingAppPrivateCommands) {
                        TvRecordingClient.this.mSession.sendAppPrivateCommand((String) command.first, (Bundle) command.second);
                    }
                    TvRecordingClient.this.mPendingAppPrivateCommands.clear();
                    TvRecordingClient.this.mSession.tune(this.mChannelUri, this.mConnectionParams);
                    return;
                }
                TvRecordingClient.this.mSessionCallback = null;
                if (TvRecordingClient.this.mCallback != null) {
                    TvRecordingClient.this.mCallback.onConnectionFailed(this.mInputId);
                    return;
                }
                return;
            }
            Log.w(TvRecordingClient.TAG, "onSessionCreated - session already created");
            if (session != null) {
                session.release();
            }
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        synchronized void onTuned(TvInputManager.Session session, Uri channelUri) {
            if (this == TvRecordingClient.this.mSessionCallback) {
                TvRecordingClient.this.mIsTuned = true;
                TvRecordingClient.this.mCallback.onTuned(channelUri);
                return;
            }
            Log.w(TvRecordingClient.TAG, "onTuned - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onSessionReleased(TvInputManager.Session session) {
            if (this == TvRecordingClient.this.mSessionCallback) {
                TvRecordingClient.this.mIsTuned = false;
                TvRecordingClient.this.mIsRecordingStarted = false;
                TvRecordingClient.this.mSessionCallback = null;
                TvRecordingClient.this.mSession = null;
                if (TvRecordingClient.this.mCallback != null) {
                    TvRecordingClient.this.mCallback.onDisconnected(this.mInputId);
                    return;
                }
                return;
            }
            Log.w(TvRecordingClient.TAG, "onSessionReleased - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onRecordingStopped(TvInputManager.Session session, Uri recordedProgramUri) {
            if (this == TvRecordingClient.this.mSessionCallback) {
                TvRecordingClient.this.mIsRecordingStarted = false;
                TvRecordingClient.this.mCallback.onRecordingStopped(recordedProgramUri);
                return;
            }
            Log.w(TvRecordingClient.TAG, "onRecordingStopped - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onError(TvInputManager.Session session, int error) {
            if (this == TvRecordingClient.this.mSessionCallback) {
                TvRecordingClient.this.mCallback.onError(error);
            } else {
                Log.w(TvRecordingClient.TAG, "onError - session not created");
            }
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onSessionEvent(TvInputManager.Session session, String eventType, Bundle eventArgs) {
            if (this == TvRecordingClient.this.mSessionCallback) {
                if (TvRecordingClient.this.mCallback != null) {
                    TvRecordingClient.this.mCallback.onEvent(this.mInputId, eventType, eventArgs);
                    return;
                }
                return;
            }
            Log.w(TvRecordingClient.TAG, "onSessionEvent - session not created");
        }
    }
}
