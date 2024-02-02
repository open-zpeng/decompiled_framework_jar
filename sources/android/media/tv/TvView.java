package android.media.tv;

import android.Manifest;
import android.annotation.SystemApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.PlaybackParams;
import android.media.tv.TvInputManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
/* loaded from: classes2.dex */
public class TvView extends ViewGroup {
    private static final boolean DEBUG = false;
    private static final String TAG = "TvView";
    private static final int ZORDER_MEDIA = 0;
    private static final int ZORDER_MEDIA_OVERLAY = 1;
    private static final int ZORDER_ON_TOP = 2;
    private final AttributeSet mAttrs;
    private TvInputCallback mCallback;
    private Boolean mCaptionEnabled;
    private final int mDefStyleAttr;
    private final TvInputManager.Session.FinishedInputEventCallback mFinishedInputEventCallback;
    private final Handler mHandler;
    private OnUnhandledInputEventListener mOnUnhandledInputEventListener;
    private boolean mOverlayViewCreated;
    private Rect mOverlayViewFrame;
    private final Queue<Pair<String, Bundle>> mPendingAppPrivateCommands;
    private TvInputManager.Session mSession;
    private MySessionCallback mSessionCallback;
    private Float mStreamVolume;
    private Surface mSurface;
    private boolean mSurfaceChanged;
    private int mSurfaceFormat;
    private int mSurfaceHeight;
    private final SurfaceHolder.Callback mSurfaceHolderCallback;
    private SurfaceView mSurfaceView;
    private int mSurfaceViewBottom;
    private int mSurfaceViewLeft;
    private int mSurfaceViewRight;
    private int mSurfaceViewTop;
    private int mSurfaceWidth;
    private TimeShiftPositionCallback mTimeShiftPositionCallback;
    private final TvInputManager mTvInputManager;
    private boolean mUseRequestedSurfaceLayout;
    private int mWindowZOrder;
    private static final WeakReference<TvView> NULL_TV_VIEW = new WeakReference<>(null);
    private static final Object sMainTvViewLock = new Object();
    private static WeakReference<TvView> sMainTvView = NULL_TV_VIEW;

    /* loaded from: classes2.dex */
    public interface OnUnhandledInputEventListener {
        boolean onUnhandledInputEvent(InputEvent inputEvent);
    }

    public TvView(Context context) {
        this(context, null, 0);
    }

    public TvView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mHandler = new Handler();
        this.mPendingAppPrivateCommands = new ArrayDeque();
        this.mSurfaceHolderCallback = new SurfaceHolder.Callback() { // from class: android.media.tv.TvView.1
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                TvView.this.mSurfaceFormat = format;
                TvView.this.mSurfaceWidth = width;
                TvView.this.mSurfaceHeight = height;
                TvView.this.mSurfaceChanged = true;
                TvView.this.dispatchSurfaceChanged(TvView.this.mSurfaceFormat, TvView.this.mSurfaceWidth, TvView.this.mSurfaceHeight);
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder holder) {
                TvView.this.mSurface = holder.getSurface();
                TvView.this.setSessionSurface(TvView.this.mSurface);
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder holder) {
                TvView.this.mSurface = null;
                TvView.this.mSurfaceChanged = false;
                TvView.this.setSessionSurface(null);
            }
        };
        this.mFinishedInputEventCallback = new TvInputManager.Session.FinishedInputEventCallback() { // from class: android.media.tv.TvView.2
            @Override // android.media.tv.TvInputManager.Session.FinishedInputEventCallback
            public void onFinishedInputEvent(Object token, boolean handled) {
                ViewRootImpl viewRootImpl;
                if (handled) {
                    return;
                }
                InputEvent event = (InputEvent) token;
                if (!TvView.this.dispatchUnhandledInputEvent(event) && (viewRootImpl = TvView.this.getViewRootImpl()) != null) {
                    viewRootImpl.dispatchUnhandledInputEvent(event);
                }
            }
        };
        this.mAttrs = attrs;
        this.mDefStyleAttr = defStyleAttr;
        resetSurfaceView();
        this.mTvInputManager = (TvInputManager) getContext().getSystemService(Context.TV_INPUT_SERVICE);
    }

    public void setCallback(TvInputCallback callback) {
        this.mCallback = callback;
    }

    @SystemApi
    public void setMain() {
        synchronized (sMainTvViewLock) {
            sMainTvView = new WeakReference<>(this);
            if (hasWindowFocus() && this.mSession != null) {
                this.mSession.setMain();
            }
        }
    }

    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
        if (isMediaOverlay) {
            this.mWindowZOrder = 1;
            removeSessionOverlayView();
        } else {
            this.mWindowZOrder = 0;
            createSessionOverlayView();
        }
        if (this.mSurfaceView != null) {
            this.mSurfaceView.setZOrderOnTop(false);
            this.mSurfaceView.setZOrderMediaOverlay(isMediaOverlay);
        }
    }

    public void setZOrderOnTop(boolean onTop) {
        if (onTop) {
            this.mWindowZOrder = 2;
            removeSessionOverlayView();
        } else {
            this.mWindowZOrder = 0;
            createSessionOverlayView();
        }
        if (this.mSurfaceView != null) {
            this.mSurfaceView.setZOrderMediaOverlay(false);
            this.mSurfaceView.setZOrderOnTop(onTop);
        }
    }

    public void setStreamVolume(float volume) {
        this.mStreamVolume = Float.valueOf(volume);
        if (this.mSession == null) {
            return;
        }
        this.mSession.setStreamVolume(volume);
    }

    public void tune(String inputId, Uri channelUri) {
        tune(inputId, channelUri, null);
    }

    public void tune(String inputId, Uri channelUri, Bundle params) {
        if (TextUtils.isEmpty(inputId)) {
            throw new IllegalArgumentException("inputId cannot be null or an empty string");
        }
        synchronized (sMainTvViewLock) {
            if (sMainTvView.get() == null) {
                sMainTvView = new WeakReference<>(this);
            }
        }
        if (this.mSessionCallback != null && TextUtils.equals(this.mSessionCallback.mInputId, inputId)) {
            if (this.mSession != null) {
                this.mSession.tune(channelUri, params);
                return;
            }
            this.mSessionCallback.mChannelUri = channelUri;
            this.mSessionCallback.mTuneParams = params;
            return;
        }
        resetInternal();
        this.mSessionCallback = new MySessionCallback(inputId, channelUri, params);
        if (this.mTvInputManager != null) {
            this.mTvInputManager.createSession(inputId, this.mSessionCallback, this.mHandler);
        }
    }

    public void reset() {
        synchronized (sMainTvViewLock) {
            if (this == sMainTvView.get()) {
                sMainTvView = NULL_TV_VIEW;
            }
        }
        resetInternal();
    }

    private synchronized void resetInternal() {
        this.mSessionCallback = null;
        this.mPendingAppPrivateCommands.clear();
        if (this.mSession != null) {
            setSessionSurface(null);
            removeSessionOverlayView();
            this.mUseRequestedSurfaceLayout = false;
            this.mSession.release();
            this.mSession = null;
            resetSurfaceView();
        }
    }

    private protected void requestUnblockContent(TvContentRating unblockedRating) {
        unblockContent(unblockedRating);
    }

    @SystemApi
    public void unblockContent(TvContentRating unblockedRating) {
        if (this.mSession != null) {
            this.mSession.unblockContent(unblockedRating);
        }
    }

    public void setCaptionEnabled(boolean enabled) {
        this.mCaptionEnabled = Boolean.valueOf(enabled);
        if (this.mSession != null) {
            this.mSession.setCaptionEnabled(enabled);
        }
    }

    public void selectTrack(int type, String trackId) {
        if (this.mSession != null) {
            this.mSession.selectTrack(type, trackId);
        }
    }

    public List<TvTrackInfo> getTracks(int type) {
        if (this.mSession == null) {
            return null;
        }
        return this.mSession.getTracks(type);
    }

    public String getSelectedTrack(int type) {
        if (this.mSession == null) {
            return null;
        }
        return this.mSession.getSelectedTrack(type);
    }

    public void timeShiftPlay(String inputId, Uri recordedProgramUri) {
        if (TextUtils.isEmpty(inputId)) {
            throw new IllegalArgumentException("inputId cannot be null or an empty string");
        }
        synchronized (sMainTvViewLock) {
            if (sMainTvView.get() == null) {
                sMainTvView = new WeakReference<>(this);
            }
        }
        if (this.mSessionCallback != null && TextUtils.equals(this.mSessionCallback.mInputId, inputId)) {
            if (this.mSession != null) {
                this.mSession.timeShiftPlay(recordedProgramUri);
                return;
            } else {
                this.mSessionCallback.mRecordedProgramUri = recordedProgramUri;
                return;
            }
        }
        resetInternal();
        this.mSessionCallback = new MySessionCallback(inputId, recordedProgramUri);
        if (this.mTvInputManager != null) {
            this.mTvInputManager.createSession(inputId, this.mSessionCallback, this.mHandler);
        }
    }

    public void timeShiftPause() {
        if (this.mSession != null) {
            this.mSession.timeShiftPause();
        }
    }

    public void timeShiftResume() {
        if (this.mSession != null) {
            this.mSession.timeShiftResume();
        }
    }

    public void timeShiftSeekTo(long timeMs) {
        if (this.mSession != null) {
            this.mSession.timeShiftSeekTo(timeMs);
        }
    }

    public void timeShiftSetPlaybackParams(PlaybackParams params) {
        if (this.mSession != null) {
            this.mSession.timeShiftSetPlaybackParams(params);
        }
    }

    public void setTimeShiftPositionCallback(TimeShiftPositionCallback callback) {
        this.mTimeShiftPositionCallback = callback;
        ensurePositionTracking();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void ensurePositionTracking() {
        if (this.mSession == null) {
            return;
        }
        this.mSession.timeShiftEnablePositionTracking(this.mTimeShiftPositionCallback != null);
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

    public boolean dispatchUnhandledInputEvent(InputEvent event) {
        if (this.mOnUnhandledInputEventListener != null && this.mOnUnhandledInputEventListener.onUnhandledInputEvent(event)) {
            return true;
        }
        return onUnhandledInputEvent(event);
    }

    public boolean onUnhandledInputEvent(InputEvent event) {
        return false;
    }

    public void setOnUnhandledInputEventListener(OnUnhandledInputEventListener listener) {
        this.mOnUnhandledInputEventListener = listener;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (super.dispatchKeyEvent(event)) {
            return true;
        }
        if (this.mSession == null) {
            return false;
        }
        InputEvent copiedEvent = event.copy();
        int ret = this.mSession.dispatchInputEvent(copiedEvent, copiedEvent, this.mFinishedInputEventCallback, this.mHandler);
        return ret != 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (super.dispatchTouchEvent(event)) {
            return true;
        }
        if (this.mSession == null) {
            return false;
        }
        InputEvent copiedEvent = event.copy();
        int ret = this.mSession.dispatchInputEvent(copiedEvent, copiedEvent, this.mFinishedInputEventCallback, this.mHandler);
        return ret != 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTrackballEvent(MotionEvent event) {
        if (super.dispatchTrackballEvent(event)) {
            return true;
        }
        if (this.mSession == null) {
            return false;
        }
        InputEvent copiedEvent = event.copy();
        int ret = this.mSession.dispatchInputEvent(copiedEvent, copiedEvent, this.mFinishedInputEventCallback, this.mHandler);
        return ret != 0;
    }

    @Override // android.view.View
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (super.dispatchGenericMotionEvent(event)) {
            return true;
        }
        if (this.mSession == null) {
            return false;
        }
        InputEvent copiedEvent = event.copy();
        int ret = this.mSession.dispatchInputEvent(copiedEvent, copiedEvent, this.mFinishedInputEventCallback, this.mHandler);
        return ret != 0;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
        synchronized (sMainTvViewLock) {
            if (hasFocus) {
                try {
                    if (this == sMainTvView.get() && this.mSession != null && checkChangeHdmiCecActiveSourcePermission()) {
                        this.mSession.setMain();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        createSessionOverlayView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        removeSessionOverlayView();
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.mUseRequestedSurfaceLayout) {
            this.mSurfaceView.layout(this.mSurfaceViewLeft, this.mSurfaceViewTop, this.mSurfaceViewRight, this.mSurfaceViewBottom);
        } else {
            this.mSurfaceView.layout(0, 0, right - left, bottom - top);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mSurfaceView.measure(widthMeasureSpec, heightMeasureSpec);
        int width = this.mSurfaceView.getMeasuredWidth();
        int height = this.mSurfaceView.getMeasuredHeight();
        int childState = this.mSurfaceView.getMeasuredState();
        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState), resolveSizeAndState(height, heightMeasureSpec, childState << 16));
    }

    @Override // android.view.ViewGroup
    public boolean gatherTransparentRegion(Region region) {
        if (this.mWindowZOrder != 2 && region != null) {
            int width = getWidth();
            int height = getHeight();
            if (width > 0 && height > 0) {
                int[] location = new int[2];
                getLocationInWindow(location);
                int left = location[0];
                int top = location[1];
                region.op(left, top, left + width, top + height, Region.Op.UNION);
            }
        }
        return super.gatherTransparentRegion(region);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (this.mWindowZOrder != 2) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        super.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (this.mWindowZOrder != 2) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        super.dispatchDraw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        this.mSurfaceView.setVisibility(visibility);
        if (visibility == 0) {
            createSessionOverlayView();
        } else {
            removeSessionOverlayView();
        }
    }

    private synchronized void resetSurfaceView() {
        if (this.mSurfaceView != null) {
            this.mSurfaceView.getHolder().removeCallback(this.mSurfaceHolderCallback);
            removeView(this.mSurfaceView);
        }
        this.mSurface = null;
        this.mSurfaceView = new SurfaceView(getContext(), this.mAttrs, this.mDefStyleAttr) { // from class: android.media.tv.TvView.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.view.SurfaceView
            public void updateSurface() {
                super.updateSurface();
                TvView.this.relayoutSessionOverlayView();
            }
        };
        this.mSurfaceView.setSecure(true);
        this.mSurfaceView.getHolder().addCallback(this.mSurfaceHolderCallback);
        if (this.mWindowZOrder == 1) {
            this.mSurfaceView.setZOrderMediaOverlay(true);
        } else if (this.mWindowZOrder == 2) {
            this.mSurfaceView.setZOrderOnTop(true);
        }
        addView(this.mSurfaceView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setSessionSurface(Surface surface) {
        if (this.mSession == null) {
            return;
        }
        this.mSession.setSurface(surface);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchSurfaceChanged(int format, int width, int height) {
        if (this.mSession == null) {
            return;
        }
        this.mSession.dispatchSurfaceChanged(format, width, height);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void createSessionOverlayView() {
        if (this.mSession == null || !isAttachedToWindow() || this.mOverlayViewCreated || this.mWindowZOrder != 0) {
            return;
        }
        this.mOverlayViewFrame = getViewFrameOnScreen();
        this.mSession.createOverlayView(this, this.mOverlayViewFrame);
        this.mOverlayViewCreated = true;
    }

    private synchronized void removeSessionOverlayView() {
        if (this.mSession == null || !this.mOverlayViewCreated) {
            return;
        }
        this.mSession.removeOverlayView();
        this.mOverlayViewCreated = false;
        this.mOverlayViewFrame = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void relayoutSessionOverlayView() {
        if (this.mSession == null || !isAttachedToWindow() || !this.mOverlayViewCreated || this.mWindowZOrder != 0) {
            return;
        }
        Rect viewFrame = getViewFrameOnScreen();
        if (viewFrame.equals(this.mOverlayViewFrame)) {
            return;
        }
        this.mSession.relayoutOverlayView(viewFrame);
        this.mOverlayViewFrame = viewFrame;
    }

    private synchronized Rect getViewFrameOnScreen() {
        Rect frame = new Rect();
        getGlobalVisibleRect(frame);
        RectF frameF = new RectF(frame);
        getMatrix().mapRect(frameF);
        frameF.round(frame);
        return frame;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean checkChangeHdmiCecActiveSourcePermission() {
        return getContext().checkSelfPermission(Manifest.permission.CHANGE_HDMI_CEC_ACTIVE_SOURCE) == 0;
    }

    /* loaded from: classes2.dex */
    public static abstract class TimeShiftPositionCallback {
        public void onTimeShiftStartPositionChanged(String inputId, long timeMs) {
        }

        public void onTimeShiftCurrentPositionChanged(String inputId, long timeMs) {
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class TvInputCallback {
        public void onConnectionFailed(String inputId) {
        }

        public void onDisconnected(String inputId) {
        }

        public void onChannelRetuned(String inputId, Uri channelUri) {
        }

        public void onTracksChanged(String inputId, List<TvTrackInfo> tracks) {
        }

        public void onTrackSelected(String inputId, int type, String trackId) {
        }

        public void onVideoSizeChanged(String inputId, int width, int height) {
        }

        public void onVideoAvailable(String inputId) {
        }

        public void onVideoUnavailable(String inputId, int reason) {
        }

        public void onContentAllowed(String inputId) {
        }

        public void onContentBlocked(String inputId, TvContentRating rating) {
        }

        @SystemApi
        public void onEvent(String inputId, String eventType, Bundle eventArgs) {
        }

        public void onTimeShiftStatusChanged(String inputId, int status) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class MySessionCallback extends TvInputManager.SessionCallback {
        Uri mChannelUri;
        final String mInputId;
        Uri mRecordedProgramUri;
        Bundle mTuneParams;

        MySessionCallback(String inputId, Uri channelUri, Bundle tuneParams) {
            this.mInputId = inputId;
            this.mChannelUri = channelUri;
            this.mTuneParams = tuneParams;
        }

        MySessionCallback(String inputId, Uri recordedProgramUri) {
            this.mInputId = inputId;
            this.mRecordedProgramUri = recordedProgramUri;
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onSessionCreated(TvInputManager.Session session) {
            if (this == TvView.this.mSessionCallback) {
                TvView.this.mSession = session;
                if (session != null) {
                    for (Pair<String, Bundle> command : TvView.this.mPendingAppPrivateCommands) {
                        TvView.this.mSession.sendAppPrivateCommand((String) command.first, (Bundle) command.second);
                    }
                    TvView.this.mPendingAppPrivateCommands.clear();
                    synchronized (TvView.sMainTvViewLock) {
                        if (TvView.this.hasWindowFocus() && TvView.this == TvView.sMainTvView.get() && TvView.this.checkChangeHdmiCecActiveSourcePermission()) {
                            TvView.this.mSession.setMain();
                        }
                    }
                    if (TvView.this.mSurface != null) {
                        TvView.this.setSessionSurface(TvView.this.mSurface);
                        if (TvView.this.mSurfaceChanged) {
                            TvView.this.dispatchSurfaceChanged(TvView.this.mSurfaceFormat, TvView.this.mSurfaceWidth, TvView.this.mSurfaceHeight);
                        }
                    }
                    TvView.this.createSessionOverlayView();
                    if (TvView.this.mStreamVolume != null) {
                        TvView.this.mSession.setStreamVolume(TvView.this.mStreamVolume.floatValue());
                    }
                    if (TvView.this.mCaptionEnabled != null) {
                        TvView.this.mSession.setCaptionEnabled(TvView.this.mCaptionEnabled.booleanValue());
                    }
                    if (this.mChannelUri != null) {
                        TvView.this.mSession.tune(this.mChannelUri, this.mTuneParams);
                    } else {
                        TvView.this.mSession.timeShiftPlay(this.mRecordedProgramUri);
                    }
                    TvView.this.ensurePositionTracking();
                    return;
                }
                TvView.this.mSessionCallback = null;
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onConnectionFailed(this.mInputId);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onSessionCreated - session already created");
            if (session != null) {
                session.release();
            }
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onSessionReleased(TvInputManager.Session session) {
            if (this == TvView.this.mSessionCallback) {
                TvView.this.mOverlayViewCreated = false;
                TvView.this.mOverlayViewFrame = null;
                TvView.this.mSessionCallback = null;
                TvView.this.mSession = null;
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onDisconnected(this.mInputId);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onSessionReleased - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onChannelRetuned(TvInputManager.Session session, Uri channelUri) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onChannelRetuned(this.mInputId, channelUri);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onChannelRetuned - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onTracksChanged(TvInputManager.Session session, List<TvTrackInfo> tracks) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onTracksChanged(this.mInputId, tracks);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onTracksChanged - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onTrackSelected(TvInputManager.Session session, int type, String trackId) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onTrackSelected(this.mInputId, type, trackId);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onTrackSelected - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onVideoSizeChanged(TvInputManager.Session session, int width, int height) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onVideoSizeChanged(this.mInputId, width, height);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onVideoSizeChanged - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onVideoAvailable(TvInputManager.Session session) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onVideoAvailable(this.mInputId);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onVideoAvailable - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onVideoUnavailable(TvInputManager.Session session, int reason) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onVideoUnavailable(this.mInputId, reason);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onVideoUnavailable - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onContentAllowed(TvInputManager.Session session) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onContentAllowed(this.mInputId);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onContentAllowed - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onContentBlocked(TvInputManager.Session session, TvContentRating rating) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onContentBlocked(this.mInputId, rating);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onContentBlocked - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onLayoutSurface(TvInputManager.Session session, int left, int top, int right, int bottom) {
            if (this == TvView.this.mSessionCallback) {
                TvView.this.mSurfaceViewLeft = left;
                TvView.this.mSurfaceViewTop = top;
                TvView.this.mSurfaceViewRight = right;
                TvView.this.mSurfaceViewBottom = bottom;
                TvView.this.mUseRequestedSurfaceLayout = true;
                TvView.this.requestLayout();
                return;
            }
            Log.w(TvView.TAG, "onLayoutSurface - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onSessionEvent(TvInputManager.Session session, String eventType, Bundle eventArgs) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onEvent(this.mInputId, eventType, eventArgs);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onSessionEvent - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onTimeShiftStatusChanged(TvInputManager.Session session, int status) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mCallback != null) {
                    TvView.this.mCallback.onTimeShiftStatusChanged(this.mInputId, status);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onTimeShiftStatusChanged - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onTimeShiftStartPositionChanged(TvInputManager.Session session, long timeMs) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mTimeShiftPositionCallback != null) {
                    TvView.this.mTimeShiftPositionCallback.onTimeShiftStartPositionChanged(this.mInputId, timeMs);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onTimeShiftStartPositionChanged - session not created");
        }

        @Override // android.media.tv.TvInputManager.SessionCallback
        public synchronized void onTimeShiftCurrentPositionChanged(TvInputManager.Session session, long timeMs) {
            if (this == TvView.this.mSessionCallback) {
                if (TvView.this.mTimeShiftPositionCallback != null) {
                    TvView.this.mTimeShiftPositionCallback.onTimeShiftCurrentPositionChanged(this.mInputId, timeMs);
                    return;
                }
                return;
            }
            Log.w(TvView.TAG, "onTimeShiftCurrentPositionChanged - session not created");
        }
    }
}
