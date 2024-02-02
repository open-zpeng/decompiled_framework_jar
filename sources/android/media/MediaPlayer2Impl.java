package android.media;

import android.app.ActivityThread;
import android.app.backup.FullBackup;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioRouting;
import android.media.AudioTrack;
import android.media.MediaDrm;
import android.media.MediaPlayer2;
import android.media.MediaPlayer2Impl;
import android.media.MediaPlayerBase;
import android.media.MediaTimeProvider;
import android.media.SubtitleController;
import android.media.SubtitleTrack;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.io.IoBridge;
import libcore.io.Streams;
/* loaded from: classes.dex */
public final class MediaPlayer2Impl extends MediaPlayer2 {
    private static final int INVOKE_ID_ADD_EXTERNAL_SOURCE = 2;
    private static final int INVOKE_ID_ADD_EXTERNAL_SOURCE_FD = 3;
    private static final int INVOKE_ID_DESELECT_TRACK = 5;
    private static final int INVOKE_ID_GET_SELECTED_TRACK = 7;
    private static final int INVOKE_ID_GET_TRACK_INFO = 1;
    private static final int INVOKE_ID_SELECT_TRACK = 4;
    private static final int INVOKE_ID_SET_VIDEO_SCALE_MODE = 6;
    private static final int KEY_PARAMETER_AUDIO_ATTRIBUTES = 1400;
    private static final int MEDIA_AUDIO_ROUTING_CHANGED = 10000;
    private static final int MEDIA_BUFFERING_UPDATE = 3;
    private static final int MEDIA_DRM_INFO = 210;
    private static final int MEDIA_ERROR = 100;
    private static final int MEDIA_INFO = 200;
    private static final int MEDIA_META_DATA = 202;
    private static final int MEDIA_NOP = 0;
    private static final int MEDIA_NOTIFY_TIME = 98;
    private static final int MEDIA_PAUSED = 7;
    private static final int MEDIA_PLAYBACK_COMPLETE = 2;
    private static final int MEDIA_PREPARED = 1;
    private static final int MEDIA_SEEK_COMPLETE = 4;
    private static final int MEDIA_SET_VIDEO_SIZE = 5;
    private static final int MEDIA_SKIPPED = 9;
    private static final int MEDIA_STARTED = 6;
    private static final int MEDIA_STOPPED = 8;
    private static final int MEDIA_SUBTITLE_DATA = 201;
    private static final int MEDIA_TIMED_TEXT = 99;
    private static final int NEXT_SOURCE_STATE_ERROR = -1;
    private static final int NEXT_SOURCE_STATE_INIT = 0;
    private static final int NEXT_SOURCE_STATE_PREPARED = 2;
    private static final int NEXT_SOURCE_STATE_PREPARING = 1;
    private static final String TAG = "MediaPlayer2Impl";
    private boolean mActiveDrmScheme;
    private AtomicInteger mBufferedPercentageCurrent;
    private AtomicInteger mBufferedPercentageNext;
    private DataSourceDesc mCurrentDSD;
    private long mCurrentSrcId;
    @GuardedBy("mTaskLock")
    private Task mCurrentTask;
    private boolean mDrmConfigAllowed;
    private ArrayList<Pair<Executor, MediaPlayer2.DrmEventCallback>> mDrmEventCallbackRecords;
    private final Object mDrmEventCbLock;
    private DrmInfoImpl mDrmInfoImpl;
    private boolean mDrmInfoResolved;
    private final Object mDrmLock;
    private MediaDrm mDrmObj;
    private boolean mDrmProvisioningInProgress;
    private ProvisioningThread mDrmProvisioningThread;
    private byte[] mDrmSessionId;
    private UUID mDrmUUID;
    private ArrayList<Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback>> mEventCallbackRecords;
    private final Object mEventCbLock;
    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;
    private BitSet mInbandTrackIndices;
    private Vector<Pair<Integer, SubtitleTrack>> mIndexTrackPairs;
    private int mListenerContext;
    private long mNativeContext;
    private long mNativeSurfaceTexture;
    private List<DataSourceDesc> mNextDSDs;
    private boolean mNextSourcePlayPending;
    private int mNextSourceState;
    private long mNextSrcId;
    private MediaPlayer2.OnDrmConfigHelper mOnDrmConfigHelper;
    private MediaPlayer2.OnSubtitleDataListener mOnSubtitleDataListener;
    private Vector<InputStream> mOpenSubtitleSources;
    @GuardedBy("mTaskLock")
    private final List<Task> mPendingTasks;
    private AudioDeviceInfo mPreferredDevice;
    private boolean mPrepareDrmInProgress;
    @GuardedBy("mRoutingChangeListeners")
    private ArrayMap<AudioRouting.OnRoutingChangedListener, NativeRoutingEventHandlerDelegate> mRoutingChangeListeners;
    private boolean mScreenOnWhilePlaying;
    private int mSelectedSubtitleTrackIndex;
    private long mSrcIdGenerator;
    private boolean mStayAwake;
    private SubtitleController mSubtitleController;
    private MediaPlayer2.OnSubtitleDataListener mSubtitleDataListener;
    private SurfaceHolder mSurfaceHolder;
    private final Handler mTaskHandler;
    private final Object mTaskLock;
    private TimeProvider mTimeProvider;
    private volatile float mVolume;
    private PowerManager.WakeLock mWakeLock = null;
    private int mStreamType = Integer.MIN_VALUE;
    private final CloseGuard mGuard = CloseGuard.get();
    private final Object mSrcLock = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    public native void _attachAuxEffect(int i);

    private native int _getAudioStreamType() throws IllegalStateException;

    private native void _notifyAt(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public native void _pause() throws IllegalStateException;

    private native void _prepareDrm(byte[] bArr, byte[] bArr2);

    private native void _release();

    /* JADX INFO: Access modifiers changed from: private */
    public native void _releaseDrm();

    private native void _reset();

    /* JADX INFO: Access modifiers changed from: private */
    public final native void _seekTo(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public native void _setAudioSessionId(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public native void _setAuxEffectSendLevel(float f);

    /* JADX INFO: Access modifiers changed from: private */
    public native void _setBufferingParams(BufferingParams bufferingParams);

    /* JADX INFO: Access modifiers changed from: private */
    public native void _setPlaybackParams(PlaybackParams playbackParams);

    /* JADX INFO: Access modifiers changed from: private */
    public native void _setSyncParams(SyncParams syncParams);

    /* JADX INFO: Access modifiers changed from: private */
    public native void _setVideoSurface(Surface surface);

    /* JADX INFO: Access modifiers changed from: private */
    public native void _setVolume(float f, float f2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void _start() throws IllegalStateException;

    private native void _stop() throws IllegalStateException;

    private native Parcel getParameter(int i);

    private native void nativeHandleDataSourceCallback(boolean z, long j, Media2DataSource media2DataSource);

    private native void nativeHandleDataSourceFD(boolean z, long j, FileDescriptor fileDescriptor, long j2, long j3) throws IOException;

    private native void nativeHandleDataSourceUrl(boolean z, long j, Media2HTTPService media2HTTPService, String str, String[] strArr, String[] strArr2) throws IOException;

    private native void nativePlayNextDataSource(long j);

    private final native void native_enableDeviceCallback(boolean z);

    private final native void native_finalize();

    private native int native_getMediaPlayer2State();

    private final native boolean native_getMetadata(boolean z, boolean z2, Parcel parcel);

    private native PersistableBundle native_getMetrics();

    private final native int native_getRoutedDeviceId();

    private static final native void native_init();

    private final native int native_invoke(Parcel parcel, Parcel parcel2);

    private final native int native_setMetadataFilter(Parcel parcel);

    private final native boolean native_setOutputDevice(int i);

    private final native void native_setup(Object obj);

    /* JADX INFO: Access modifiers changed from: private */
    public static final native void native_stream_event_onStreamDataRequest(long j, long j2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    public static final native void native_stream_event_onStreamPresentationEnd(long j, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static final native void native_stream_event_onTearDown(long j, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void setLooping(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public native boolean setParameter(int i, Parcel parcel);

    public native void _prepare();

    @Override // android.media.MediaPlayer2
    public native int getAudioSessionId();

    @Override // android.media.MediaPlayer2
    public native BufferingParams getBufferingParams();

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public native long getCurrentPosition();

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public native long getDuration();

    @Override // android.media.MediaPlayer2
    public native PlaybackParams getPlaybackParams();

    @Override // android.media.MediaPlayer2
    public native SyncParams getSyncParams();

    @Override // android.media.MediaPlayer2
    public native int getVideoHeight();

    @Override // android.media.MediaPlayer2
    public native int getVideoWidth();

    @Override // android.media.MediaPlayer2
    public native boolean isLooping();

    @Override // android.media.MediaPlayer2
    public native boolean isPlaying();

    static /* synthetic */ long access$708(MediaPlayer2Impl x0) {
        long j = x0.mSrcIdGenerator;
        x0.mSrcIdGenerator = 1 + j;
        return j;
    }

    static {
        System.loadLibrary("media2_jni");
        native_init();
    }

    public synchronized MediaPlayer2Impl() {
        this.mSrcIdGenerator = 0L;
        long j = this.mSrcIdGenerator;
        this.mSrcIdGenerator = j + 1;
        this.mCurrentSrcId = j;
        long j2 = this.mSrcIdGenerator;
        this.mSrcIdGenerator = 1 + j2;
        this.mNextSrcId = j2;
        this.mNextSourceState = 0;
        this.mNextSourcePlayPending = false;
        this.mBufferedPercentageCurrent = new AtomicInteger(0);
        this.mBufferedPercentageNext = new AtomicInteger(0);
        this.mVolume = 1.0f;
        this.mDrmLock = new Object();
        this.mTaskLock = new Object();
        this.mPendingTasks = new LinkedList();
        this.mPreferredDevice = null;
        this.mRoutingChangeListeners = new ArrayMap<>();
        this.mIndexTrackPairs = new Vector<>();
        this.mInbandTrackIndices = new BitSet();
        this.mSelectedSubtitleTrackIndex = -1;
        this.mSubtitleDataListener = new MediaPlayer2.OnSubtitleDataListener() { // from class: android.media.MediaPlayer2Impl.25
            @Override // android.media.MediaPlayer2.OnSubtitleDataListener
            public void onSubtitleData(MediaPlayer2 mp, SubtitleData data) {
                int index = data.getTrackIndex();
                synchronized (MediaPlayer2Impl.this.mIndexTrackPairs) {
                    Iterator it = MediaPlayer2Impl.this.mIndexTrackPairs.iterator();
                    while (it.hasNext()) {
                        Pair<Integer, SubtitleTrack> p = (Pair) it.next();
                        if (p.first != 0 && ((Integer) p.first).intValue() == index && p.second != 0) {
                            SubtitleTrack track = (SubtitleTrack) p.second;
                            track.onData(data);
                        }
                    }
                }
            }
        };
        this.mEventCbLock = new Object();
        this.mEventCallbackRecords = new ArrayList<>();
        this.mDrmEventCbLock = new Object();
        this.mDrmEventCallbackRecords = new ArrayList<>();
        Looper looper = Looper.myLooper();
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            Looper looper2 = Looper.getMainLooper();
            if (looper2 != null) {
                this.mEventHandler = new EventHandler(this, looper2);
            } else {
                this.mEventHandler = null;
            }
        }
        this.mHandlerThread = new HandlerThread("MediaPlayer2TaskThread");
        this.mHandlerThread.start();
        this.mTaskHandler = new Handler(this.mHandlerThread.getLooper());
        this.mTimeProvider = new TimeProvider(this);
        this.mOpenSubtitleSources = new Vector<>();
        this.mGuard.open("close");
        native_setup(new WeakReference(this));
    }

    @Override // android.media.MediaPlayer2, java.lang.AutoCloseable
    public void close() {
        synchronized (this.mGuard) {
            release();
        }
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void play() {
        addTask(new Task(5, false) { // from class: android.media.MediaPlayer2Impl.1
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this.stayAwake(true);
                MediaPlayer2Impl.this._start();
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void prepare() {
        addTask(new Task(6, true) { // from class: android.media.MediaPlayer2Impl.2
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this._prepare();
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void pause() {
        addTask(new Task(4, false) { // from class: android.media.MediaPlayer2Impl.3
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this.stayAwake(false);
                MediaPlayer2Impl.this._pause();
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void skipToNext() {
        addTask(new Task(29, false) { // from class: android.media.MediaPlayer2Impl.4
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized long getBufferedPosition() {
        return (getDuration() * this.mBufferedPercentageCurrent.get()) / 100;
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized int getPlayerState() {
        int mediaplayer2State = getMediaPlayer2State();
        switch (mediaplayer2State) {
            case 1:
                return 0;
            case 2:
            case 3:
                return 1;
            case 4:
                return 2;
            default:
                return 3;
        }
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized int getBufferingState() {
        return 0;
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void setAudioAttributes(final AudioAttributes attributes) {
        addTask(new Task(16, false) { // from class: android.media.MediaPlayer2Impl.5
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                if (attributes == null) {
                    throw new IllegalArgumentException("Cannot set AudioAttributes to null");
                }
                Parcel pattributes = Parcel.obtain();
                attributes.writeToParcel(pattributes, 1);
                MediaPlayer2Impl.this.setParameter(1400, pattributes);
                pattributes.recycle();
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized AudioAttributes getAudioAttributes() {
        Parcel pattributes = getParameter(1400);
        AudioAttributes attributes = AudioAttributes.CREATOR.createFromParcel(pattributes);
        pattributes.recycle();
        return attributes;
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void setDataSource(final DataSourceDesc dsd) {
        addTask(new Task(19, false) { // from class: android.media.MediaPlayer2Impl.6
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                Preconditions.checkNotNull(dsd, "the DataSourceDesc cannot be null");
                synchronized (MediaPlayer2Impl.this.mSrcLock) {
                    MediaPlayer2Impl.this.mCurrentDSD = dsd;
                    MediaPlayer2Impl.this.mCurrentSrcId = MediaPlayer2Impl.access$708(MediaPlayer2Impl.this);
                    try {
                        MediaPlayer2Impl.this.handleDataSource(true, dsd, MediaPlayer2Impl.this.mCurrentSrcId);
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void setNextDataSource(final DataSourceDesc dsd) {
        addTask(new Task(22, false) { // from class: android.media.MediaPlayer2Impl.7
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                Preconditions.checkNotNull(dsd, "the DataSourceDesc cannot be null");
                synchronized (MediaPlayer2Impl.this.mSrcLock) {
                    MediaPlayer2Impl.this.mNextDSDs = new ArrayList(1);
                    MediaPlayer2Impl.this.mNextDSDs.add(dsd);
                    MediaPlayer2Impl.this.mNextSrcId = MediaPlayer2Impl.access$708(MediaPlayer2Impl.this);
                    MediaPlayer2Impl.this.mNextSourceState = 0;
                    MediaPlayer2Impl.this.mNextSourcePlayPending = false;
                }
                int state = MediaPlayer2Impl.this.getMediaPlayer2State();
                if (state != 1) {
                    synchronized (MediaPlayer2Impl.this.mSrcLock) {
                        MediaPlayer2Impl.this.prepareNextDataSource_l();
                    }
                }
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void setNextDataSources(final List<DataSourceDesc> dsds) {
        addTask(new Task(23, false) { // from class: android.media.MediaPlayer2Impl.8
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                if (dsds == null || dsds.size() == 0) {
                    throw new IllegalArgumentException("data source list cannot be null or empty.");
                }
                for (DataSourceDesc dsd : dsds) {
                    if (dsd == null) {
                        throw new IllegalArgumentException("DataSourceDesc in the source list cannot be null.");
                    }
                }
                synchronized (MediaPlayer2Impl.this.mSrcLock) {
                    MediaPlayer2Impl.this.mNextDSDs = new ArrayList(dsds);
                    MediaPlayer2Impl.this.mNextSrcId = MediaPlayer2Impl.access$708(MediaPlayer2Impl.this);
                    MediaPlayer2Impl.this.mNextSourceState = 0;
                    MediaPlayer2Impl.this.mNextSourcePlayPending = false;
                }
                int state = MediaPlayer2Impl.this.getMediaPlayer2State();
                if (state != 1) {
                    synchronized (MediaPlayer2Impl.this.mSrcLock) {
                        MediaPlayer2Impl.this.prepareNextDataSource_l();
                    }
                }
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized DataSourceDesc getCurrentDataSource() {
        DataSourceDesc dataSourceDesc;
        synchronized (this.mSrcLock) {
            dataSourceDesc = this.mCurrentDSD;
        }
        return dataSourceDesc;
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void loopCurrent(final boolean loop) {
        addTask(new Task(3, false) { // from class: android.media.MediaPlayer2Impl.9
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this.setLooping(loop);
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void setPlaybackSpeed(final float speed) {
        addTask(new Task(25, false) { // from class: android.media.MediaPlayer2Impl.10
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this._setPlaybackParams(MediaPlayer2Impl.this.getPlaybackParams().setSpeed(speed));
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized float getPlaybackSpeed() {
        return getPlaybackParams().getSpeed();
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized boolean isReversePlaybackSupported() {
        return false;
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void setPlayerVolume(final float volume) {
        addTask(new Task(26, false) { // from class: android.media.MediaPlayer2Impl.11
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this.mVolume = volume;
                MediaPlayer2Impl.this._setVolume(volume, volume);
            }
        });
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized float getPlayerVolume() {
        return this.mVolume;
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized float getMaxPlayerVolume() {
        return 1.0f;
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void registerPlayerEventCallback(Executor e, MediaPlayerBase.PlayerEventCallback cb) {
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void unregisterPlayerEventCallback(MediaPlayerBase.PlayerEventCallback cb) {
    }

    @Override // android.media.MediaPlayer2
    public synchronized Parcel newRequest() {
        Parcel parcel = Parcel.obtain();
        return parcel;
    }

    @Override // android.media.MediaPlayer2
    public synchronized void invoke(Parcel request, Parcel reply) {
        int retcode = native_invoke(request, reply);
        reply.setDataPosition(0);
        if (retcode != 0) {
            throw new RuntimeException("failure code: " + retcode);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.media.MediaPlayer2Impl$12  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass12 extends Task {
        final /* synthetic */ Object val$label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass12(int mediaCallType, boolean needToWaitForEventToComplete, Object obj) {
            super(mediaCallType, needToWaitForEventToComplete);
            this.val$label = obj;
        }

        @Override // android.media.MediaPlayer2Impl.Task
        void process() {
            synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                Iterator it = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                while (it.hasNext()) {
                    final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb = (Pair) it.next();
                    final Object obj = this.val$label;
                    ((Executor) cb.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$12$GAwhcv62KlexkkYkbjb8-qEksjI
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaPlayer2Impl.AnonymousClass12 anonymousClass12 = MediaPlayer2Impl.AnonymousClass12.this;
                            ((MediaPlayer2.MediaPlayer2EventCallback) cb.second).onCommandLabelReached(MediaPlayer2Impl.this, obj);
                        }
                    });
                }
            }
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void notifyWhenCommandLabelReached(Object label) {
        addTask(new AnonymousClass12(1003, false, label));
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setDisplay(SurfaceHolder sh) {
        Surface surface;
        this.mSurfaceHolder = sh;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setSurface(final Surface surface) {
        addTask(new Task(27, false) { // from class: android.media.MediaPlayer2Impl.13
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                if (MediaPlayer2Impl.this.mScreenOnWhilePlaying && surface != null) {
                    Log.w(MediaPlayer2Impl.TAG, "setScreenOnWhilePlaying(true) is ineffective for Surface");
                }
                MediaPlayer2Impl.this.mSurfaceHolder = null;
                MediaPlayer2Impl.this._setVideoSurface(surface);
                MediaPlayer2Impl.this.updateSurfaceScreenOn();
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setVideoScalingMode(final int mode) {
        addTask(new Task(1002, false) { // from class: android.media.MediaPlayer2Impl.14
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                if (!MediaPlayer2Impl.this.isVideoScalingModeSupported(mode)) {
                    String msg = "Scaling mode " + mode + " is not supported";
                    throw new IllegalArgumentException(msg);
                }
                Parcel request = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    request.writeInt(6);
                    request.writeInt(mode);
                    MediaPlayer2Impl.this.invoke(request, reply);
                } finally {
                    request.recycle();
                    reply.recycle();
                }
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized void clearPendingCommands() {
    }

    private synchronized void addTask(Task task) {
        synchronized (this.mTaskLock) {
            this.mPendingTasks.add(task);
            processPendingTask_l();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy("mTaskLock")
    public synchronized void processPendingTask_l() {
        if (this.mCurrentTask == null && !this.mPendingTasks.isEmpty()) {
            Task task = this.mPendingTasks.remove(0);
            this.mCurrentTask = task;
            this.mTaskHandler.post(task);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public synchronized void handleDataSource(boolean isCurrent, DataSourceDesc dsd, long srcId) throws IOException {
        Preconditions.checkNotNull(dsd, "the DataSourceDesc cannot be null");
        switch (dsd.getType()) {
            case 1:
                handleDataSource(isCurrent, srcId, dsd.getMedia2DataSource());
                return;
            case 2:
                handleDataSource(isCurrent, srcId, dsd.getFileDescriptor(), dsd.getFileDescriptorOffset(), dsd.getFileDescriptorLength());
                break;
            case 3:
                handleDataSource(isCurrent, srcId, dsd.getUriContext(), dsd.getUri(), dsd.getUriHeaders(), dsd.getUriCookies());
                return;
        }
    }

    private synchronized void handleDataSource(boolean isCurrent, long srcId, Context context, Uri uri, Map<String, String> headers, List<HttpCookie> cookies) throws IOException {
        ContentResolver resolver = context.getContentResolver();
        String scheme = uri.getScheme();
        String authority = ContentProvider.getAuthorityWithoutUserId(uri.getAuthority());
        if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            handleDataSource(isCurrent, srcId, uri.getPath(), (Map<String, String>) null, (List<HttpCookie>) null);
        } else if (!"content".equals(scheme) || !"settings".equals(authority)) {
            if (attemptDataSource(isCurrent, srcId, resolver, uri)) {
                return;
            }
            handleDataSource(isCurrent, srcId, uri.toString(), headers, cookies);
        } else {
            int type = RingtoneManager.getDefaultType(uri);
            Uri cacheUri = RingtoneManager.getCacheForType(type, context.getUserId());
            Uri actualUri = RingtoneManager.getActualDefaultRingtoneUri(context, type);
            if (attemptDataSource(isCurrent, srcId, resolver, cacheUri) || attemptDataSource(isCurrent, srcId, resolver, actualUri)) {
                return;
            }
            handleDataSource(isCurrent, srcId, uri.toString(), headers, cookies);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:31:0x0065
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private synchronized boolean attemptDataSource(boolean r24, long r25, android.content.ContentResolver r27, android.net.Uri r28) {
        /*
            r23 = this;
            r1 = r28
            java.lang.String r0 = "r"
            r2 = r27
            android.content.res.AssetFileDescriptor r0 = r2.openAssetFileDescriptor(r1, r0)     // Catch: java.lang.Throwable -> L63
            r3 = r0
            r4 = 0
            long r5 = r3.getDeclaredLength()     // Catch: java.lang.Throwable -> L4e
            r7 = 0
            int r0 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r0 >= 0) goto L2d
        L18:
            java.io.FileDescriptor r9 = r3.getFileDescriptor()     // Catch: java.lang.Throwable -> L4e
            r10 = 0
            r12 = 576460752303423487(0x7ffffffffffffff, double:3.7857669957336787E-270)
            r5 = r23
            r6 = r24
            r7 = r25
            r5.handleDataSource(r6, r7, r9, r10, r12)     // Catch: java.lang.Throwable -> L4e
            goto L43
        L2d:
            java.io.FileDescriptor r18 = r3.getFileDescriptor()     // Catch: java.lang.Throwable -> L4e
            long r19 = r3.getStartOffset()     // Catch: java.lang.Throwable -> L4e
            long r21 = r3.getDeclaredLength()     // Catch: java.lang.Throwable -> L4e
            r14 = r23
            r15 = r24
            r16 = r25
            r14.handleDataSource(r15, r16, r18, r19, r21)     // Catch: java.lang.Throwable -> L4e
        L43:
            r0 = 1
            if (r3 == 0) goto L49
            r3.close()     // Catch: java.lang.Throwable -> L63
        L49:
            return r0
        L4a:
            r0 = move-exception
            r5 = r4
            r4 = r0
            goto L51
        L4e:
            r0 = move-exception
            r4 = r0
            throw r4     // Catch: java.lang.Throwable -> L4a
        L51:
            if (r3 == 0) goto L62
            if (r5 == 0) goto L5f
            r3.close()     // Catch: java.lang.Throwable -> L59
            goto L62
        L59:
            r0 = move-exception
            r6 = r0
            r5.addSuppressed(r6)     // Catch: java.lang.Throwable -> L63
            goto L62
        L5f:
            r3.close()     // Catch: java.lang.Throwable -> L63
        L62:
            throw r4     // Catch: java.lang.Throwable -> L63
        L63:
            r0 = move-exception
            goto L68
        L65:
            r0 = move-exception
            r2 = r27
        L68:
            java.lang.String r3 = "MediaPlayer2Impl"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Couldn't open "
            r4.append(r5)
            r4.append(r1)
            java.lang.String r5 = ": "
            r4.append(r5)
            r4.append(r0)
            java.lang.String r4 = r4.toString()
            android.util.Log.w(r3, r4)
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer2Impl.attemptDataSource(boolean, long, android.content.ContentResolver, android.net.Uri):boolean");
    }

    private synchronized void handleDataSource(boolean isCurrent, long srcId, String path, Map<String, String> headers, List<HttpCookie> cookies) throws IOException {
        String[] keys = null;
        String[] values = null;
        if (headers != null) {
            keys = new String[headers.size()];
            values = new String[headers.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
                i++;
            }
        }
        handleDataSource(isCurrent, srcId, path, keys, values, cookies);
    }

    private synchronized void handleDataSource(boolean isCurrent, long srcId, String path, String[] keys, String[] values, List<HttpCookie> cookies) throws IOException {
        String path2;
        Uri uri = Uri.parse(path);
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            path2 = uri.getPath();
        } else if (scheme != null) {
            nativeHandleDataSourceUrl(isCurrent, srcId, Media2HTTPService.createHTTPService(path, cookies), path, keys, values);
            return;
        } else {
            path2 = path;
        }
        File file = new File(path2);
        if (file.exists()) {
            FileInputStream is = new FileInputStream(file);
            FileDescriptor fd = is.getFD();
            handleDataSource(isCurrent, srcId, fd, 0L, DataSourceDesc.LONG_MAX);
            is.close();
            return;
        }
        throw new IOException("handleDataSource failed.");
    }

    private synchronized void handleDataSource(boolean isCurrent, long srcId, FileDescriptor fd, long offset, long length) throws IOException {
        nativeHandleDataSourceFD(isCurrent, srcId, fd, offset, length);
    }

    private synchronized void handleDataSource(boolean isCurrent, long srcId, Media2DataSource dataSource) {
        nativeHandleDataSourceCallback(isCurrent, srcId, dataSource);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void prepareNextDataSource_l() {
        if (this.mNextDSDs == null || this.mNextDSDs.isEmpty() || this.mNextSourceState != 0) {
            return;
        }
        try {
            this.mNextSourceState = 1;
            handleDataSource(false, this.mNextDSDs.get(0), this.mNextSrcId);
        } catch (Exception e) {
            final Message msg2 = this.mEventHandler.obtainMessage(100, 1, -1010, null);
            final long nextSrcId = this.mNextSrcId;
            this.mEventHandler.post(new Runnable() { // from class: android.media.MediaPlayer2Impl.15
                @Override // java.lang.Runnable
                public void run() {
                    MediaPlayer2Impl.this.mEventHandler.handleMessage(msg2, nextSrcId);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void playNextDataSource_l() {
        if (this.mNextDSDs == null || this.mNextDSDs.isEmpty()) {
            return;
        }
        if (this.mNextSourceState == 2) {
            this.mCurrentDSD = this.mNextDSDs.get(0);
            this.mCurrentSrcId = this.mNextSrcId;
            this.mBufferedPercentageCurrent.set(this.mBufferedPercentageNext.get());
            this.mNextDSDs.remove(0);
            long j = this.mSrcIdGenerator;
            this.mSrcIdGenerator = 1 + j;
            this.mNextSrcId = j;
            this.mBufferedPercentageNext.set(0);
            this.mNextSourceState = 0;
            this.mNextSourcePlayPending = false;
            final long srcId = this.mCurrentSrcId;
            try {
                nativePlayNextDataSource(srcId);
                return;
            } catch (Exception e) {
                final Message msg2 = this.mEventHandler.obtainMessage(100, 1, -1010, null);
                this.mEventHandler.post(new Runnable() { // from class: android.media.MediaPlayer2Impl.16
                    @Override // java.lang.Runnable
                    public void run() {
                        MediaPlayer2Impl.this.mEventHandler.handleMessage(msg2, srcId);
                    }
                });
                return;
            }
        }
        if (this.mNextSourceState == 0) {
            prepareNextDataSource_l();
        }
        this.mNextSourcePlayPending = true;
    }

    private synchronized int getAudioStreamType() {
        if (this.mStreamType == Integer.MIN_VALUE) {
            this.mStreamType = _getAudioStreamType();
        }
        return this.mStreamType;
    }

    @Override // android.media.MediaPlayer2
    public synchronized void stop() {
        stayAwake(false);
        _stop();
    }

    @Override // android.media.MediaPlayer2, android.media.AudioRouting
    public boolean setPreferredDevice(AudioDeviceInfo deviceInfo) {
        if (deviceInfo != null && !deviceInfo.isSink()) {
            return false;
        }
        int preferredDeviceId = deviceInfo != null ? deviceInfo.getId() : 0;
        boolean status = native_setOutputDevice(preferredDeviceId);
        if (status) {
            synchronized (this) {
                this.mPreferredDevice = deviceInfo;
            }
        }
        return status;
    }

    @Override // android.media.MediaPlayer2, android.media.AudioRouting
    public AudioDeviceInfo getPreferredDevice() {
        AudioDeviceInfo audioDeviceInfo;
        synchronized (this) {
            audioDeviceInfo = this.mPreferredDevice;
        }
        return audioDeviceInfo;
    }

    @Override // android.media.MediaPlayer2, android.media.AudioRouting
    public AudioDeviceInfo getRoutedDevice() {
        int deviceId = native_getRoutedDeviceId();
        if (deviceId == 0) {
            return null;
        }
        AudioDeviceInfo[] devices = AudioManager.getDevicesStatic(2);
        for (int i = 0; i < devices.length; i++) {
            if (devices[i].getId() == deviceId) {
                return devices[i];
            }
        }
        return null;
    }

    @GuardedBy("mRoutingChangeListeners")
    private synchronized void enableNativeRoutingCallbacksLocked(boolean enabled) {
        if (this.mRoutingChangeListeners.size() == 0) {
            native_enableDeviceCallback(enabled);
        }
    }

    @Override // android.media.MediaPlayer2, android.media.AudioRouting
    public void addOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener listener, Handler handler) {
        synchronized (this.mRoutingChangeListeners) {
            if (listener != null) {
                try {
                    if (!this.mRoutingChangeListeners.containsKey(listener)) {
                        enableNativeRoutingCallbacksLocked(true);
                        this.mRoutingChangeListeners.put(listener, new NativeRoutingEventHandlerDelegate(this, listener, handler != null ? handler : this.mEventHandler));
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    @Override // android.media.MediaPlayer2, android.media.AudioRouting
    public void removeOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener listener) {
        synchronized (this.mRoutingChangeListeners) {
            if (this.mRoutingChangeListeners.containsKey(listener)) {
                this.mRoutingChangeListeners.remove(listener);
                enableNativeRoutingCallbacksLocked(false);
            }
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setWakeMode(Context context, int mode) {
        boolean washeld = false;
        if (SystemProperties.getBoolean("audio.offload.ignore_setawake", false)) {
            Log.w(TAG, "IGNORING setWakeMode " + mode);
            return;
        }
        if (this.mWakeLock != null) {
            if (this.mWakeLock.isHeld()) {
                washeld = true;
                this.mWakeLock.release();
            }
            this.mWakeLock = null;
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(536870912 | mode, MediaPlayer2Impl.class.getName());
        this.mWakeLock.setReferenceCounted(false);
        if (washeld) {
            this.mWakeLock.acquire();
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setScreenOnWhilePlaying(boolean screenOn) {
        if (this.mScreenOnWhilePlaying != screenOn) {
            if (screenOn && this.mSurfaceHolder == null) {
                Log.w(TAG, "setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder");
            }
            this.mScreenOnWhilePlaying = screenOn;
            updateSurfaceScreenOn();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void stayAwake(boolean awake) {
        if (this.mWakeLock != null) {
            if (awake && !this.mWakeLock.isHeld()) {
                this.mWakeLock.acquire();
            } else if (!awake && this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
        this.mStayAwake = awake;
        updateSurfaceScreenOn();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateSurfaceScreenOn() {
        if (this.mSurfaceHolder != null) {
            this.mSurfaceHolder.setKeepScreenOn(this.mScreenOnWhilePlaying && this.mStayAwake);
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized PersistableBundle getMetrics() {
        PersistableBundle bundle = native_getMetrics();
        return bundle;
    }

    @Override // android.media.MediaPlayer2
    public synchronized int getMediaPlayer2State() {
        return native_getMediaPlayer2State();
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setBufferingParams(final BufferingParams params) {
        addTask(new Task(1001, false) { // from class: android.media.MediaPlayer2Impl.17
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                Preconditions.checkNotNull(params, "the BufferingParams cannot be null");
                MediaPlayer2Impl.this._setBufferingParams(params);
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized PlaybackParams easyPlaybackParams(float rate, int audioMode) {
        PlaybackParams params = new PlaybackParams();
        params.allowDefaults();
        switch (audioMode) {
            case 0:
                params.setSpeed(rate).setPitch(1.0f);
                break;
            case 1:
                params.setSpeed(rate).setPitch(1.0f).setAudioFallbackMode(2);
                break;
            case 2:
                params.setSpeed(rate).setPitch(rate);
                break;
            default:
                String msg = "Audio playback mode " + audioMode + " is not supported";
                throw new IllegalArgumentException(msg);
        }
        return params;
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setPlaybackParams(final PlaybackParams params) {
        addTask(new Task(24, false) { // from class: android.media.MediaPlayer2Impl.18
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                Preconditions.checkNotNull(params, "the PlaybackParams cannot be null");
                MediaPlayer2Impl.this._setPlaybackParams(params);
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setSyncParams(final SyncParams params) {
        addTask(new Task(28, false) { // from class: android.media.MediaPlayer2Impl.19
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                Preconditions.checkNotNull(params, "the SyncParams cannot be null");
                MediaPlayer2Impl.this._setSyncParams(params);
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized void seekTo(final long msec, final int mode) {
        addTask(new Task(14, true) { // from class: android.media.MediaPlayer2Impl.20
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                if (mode < 0 || mode > 3) {
                    String msg = "Illegal seek mode: " + mode;
                    throw new IllegalArgumentException(msg);
                }
                long posMs = msec;
                if (posMs > 2147483647L) {
                    Log.w(MediaPlayer2Impl.TAG, "seekTo offset " + posMs + " is too large, cap to 2147483647");
                    posMs = 2147483647L;
                } else if (posMs < -2147483648L) {
                    Log.w(MediaPlayer2Impl.TAG, "seekTo offset " + posMs + " is too small, cap to -2147483648");
                    posMs = -2147483648L;
                }
                MediaPlayer2Impl.this._seekTo(posMs, mode);
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized MediaTimestamp getTimestamp() {
        try {
            return new MediaTimestamp(getCurrentPosition() * 1000, System.nanoTime(), isPlaying() ? getPlaybackParams().getSpeed() : 0.0f);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized Metadata getMetadata(boolean update_only, boolean apply_filter) {
        Parcel reply = Parcel.obtain();
        Metadata data = new Metadata();
        if (!native_getMetadata(update_only, apply_filter, reply)) {
            reply.recycle();
            return null;
        } else if (!data.parse(reply)) {
            reply.recycle();
            return null;
        } else {
            return data;
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized int setMetadataFilter(Set<Integer> allow, Set<Integer> block) {
        Parcel request = newRequest();
        int capacity = request.dataSize() + (4 * (allow.size() + 1 + 1 + block.size()));
        if (request.dataCapacity() < capacity) {
            request.setDataCapacity(capacity);
        }
        request.writeInt(allow.size());
        for (Integer t : allow) {
            request.writeInt(t.intValue());
        }
        request.writeInt(block.size());
        for (Integer t2 : block) {
            request.writeInt(t2.intValue());
        }
        return native_setMetadataFilter(request);
    }

    @Override // android.media.MediaPlayer2, android.media.MediaPlayerBase
    public synchronized void reset() {
        this.mSelectedSubtitleTrackIndex = -1;
        synchronized (this.mOpenSubtitleSources) {
            Iterator<InputStream> it = this.mOpenSubtitleSources.iterator();
            while (it.hasNext()) {
                InputStream is = it.next();
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            this.mOpenSubtitleSources.clear();
        }
        if (this.mSubtitleController != null) {
            this.mSubtitleController.reset();
        }
        if (this.mTimeProvider != null) {
            this.mTimeProvider.close();
            this.mTimeProvider = null;
        }
        synchronized (this.mEventCbLock) {
            this.mEventCallbackRecords.clear();
        }
        synchronized (this.mDrmEventCbLock) {
            this.mDrmEventCallbackRecords.clear();
        }
        stayAwake(false);
        _reset();
        if (this.mEventHandler != null) {
            this.mEventHandler.removeCallbacksAndMessages(null);
        }
        synchronized (this.mIndexTrackPairs) {
            this.mIndexTrackPairs.clear();
            this.mInbandTrackIndices.clear();
        }
        resetDrmState();
    }

    @Override // android.media.MediaPlayer2
    public synchronized void notifyAt(long mediaTimeUs) {
        _notifyAt(mediaTimeUs);
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setAudioSessionId(final int sessionId) {
        addTask(new Task(17, false) { // from class: android.media.MediaPlayer2Impl.21
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this._setAudioSessionId(sessionId);
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized void attachAuxEffect(final int effectId) {
        addTask(new Task(1, false) { // from class: android.media.MediaPlayer2Impl.22
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this._attachAuxEffect(effectId);
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setAuxEffectSendLevel(final float level) {
        addTask(new Task(18, false) { // from class: android.media.MediaPlayer2Impl.23
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this._setAuxEffectSendLevel(level);
            }
        });
    }

    /* loaded from: classes.dex */
    public static final class TrackInfoImpl extends MediaPlayer2.TrackInfo {
        static final Parcelable.Creator<TrackInfoImpl> CREATOR = new Parcelable.Creator<TrackInfoImpl>() { // from class: android.media.MediaPlayer2Impl.TrackInfoImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public TrackInfoImpl createFromParcel(Parcel in) {
                return new TrackInfoImpl(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public TrackInfoImpl[] newArray(int size) {
                return new TrackInfoImpl[size];
            }
        };
        final MediaFormat mFormat;
        final int mTrackType;

        public synchronized int getTrackType() {
            return this.mTrackType;
        }

        public synchronized String getLanguage() {
            String language = this.mFormat.getString("language");
            return language == null ? "und" : language;
        }

        @Override // android.media.MediaPlayer2.TrackInfo
        public synchronized MediaFormat getFormat() {
            if (this.mTrackType == 3 || this.mTrackType == 4) {
                return this.mFormat;
            }
            return null;
        }

        synchronized TrackInfoImpl(Parcel in) {
            this.mTrackType = in.readInt();
            String mime = in.readString();
            String language = in.readString();
            this.mFormat = MediaFormat.createSubtitleFormat(mime, language);
            if (this.mTrackType == 4) {
                this.mFormat.setInteger(MediaFormat.KEY_IS_AUTOSELECT, in.readInt());
                this.mFormat.setInteger(MediaFormat.KEY_IS_DEFAULT, in.readInt());
                this.mFormat.setInteger(MediaFormat.KEY_IS_FORCED_SUBTITLE, in.readInt());
            }
        }

        synchronized TrackInfoImpl(int type, MediaFormat format) {
            this.mTrackType = type;
            this.mFormat = format;
        }

        synchronized void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mTrackType);
            dest.writeString(getLanguage());
            if (this.mTrackType == 4) {
                dest.writeString(this.mFormat.getString(MediaFormat.KEY_MIME));
                dest.writeInt(this.mFormat.getInteger(MediaFormat.KEY_IS_AUTOSELECT));
                dest.writeInt(this.mFormat.getInteger(MediaFormat.KEY_IS_DEFAULT));
                dest.writeInt(this.mFormat.getInteger(MediaFormat.KEY_IS_FORCED_SUBTITLE));
            }
        }

        @Override // android.media.MediaPlayer2.TrackInfo
        public String toString() {
            StringBuilder out = new StringBuilder(128);
            out.append(getClass().getName());
            out.append('{');
            switch (this.mTrackType) {
                case 1:
                    out.append("VIDEO");
                    break;
                case 2:
                    out.append("AUDIO");
                    break;
                case 3:
                    out.append("TIMEDTEXT");
                    break;
                case 4:
                    out.append("SUBTITLE");
                    break;
                default:
                    out.append(IccCardConstants.INTENT_VALUE_ICC_UNKNOWN);
                    break;
            }
            out.append(", " + this.mFormat.toString());
            out.append("}");
            return out.toString();
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized List<MediaPlayer2.TrackInfo> getTrackInfo() {
        List<MediaPlayer2.TrackInfo> asList;
        TrackInfoImpl[] trackInfo = getInbandTrackInfoImpl();
        synchronized (this.mIndexTrackPairs) {
            TrackInfoImpl[] allTrackInfo = new TrackInfoImpl[this.mIndexTrackPairs.size()];
            for (int i = 0; i < allTrackInfo.length; i++) {
                Pair<Integer, SubtitleTrack> p = this.mIndexTrackPairs.get(i);
                if (p.first != null) {
                    allTrackInfo[i] = trackInfo[p.first.intValue()];
                } else {
                    SubtitleTrack track = p.second;
                    allTrackInfo[i] = new TrackInfoImpl(track.getTrackType(), track.getFormat());
                }
            }
            asList = Arrays.asList(allTrackInfo);
        }
        return asList;
    }

    private synchronized TrackInfoImpl[] getInbandTrackInfoImpl() throws IllegalStateException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInt(1);
            invoke(request, reply);
            TrackInfoImpl[] trackInfo = (TrackInfoImpl[]) reply.createTypedArray(TrackInfoImpl.CREATOR);
            return trackInfo;
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    private static synchronized boolean availableMimeTypeForExternalSource(String mimeType) {
        if ("application/x-subrip".equals(mimeType)) {
            return true;
        }
        return false;
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setSubtitleAnchor(SubtitleController controller, SubtitleController.Anchor anchor) {
        this.mSubtitleController = controller;
        this.mSubtitleController.setAnchor(anchor);
    }

    private synchronized void setSubtitleAnchor() {
        if (this.mSubtitleController == null && ActivityThread.currentApplication() != null) {
            final HandlerThread thread = new HandlerThread("SetSubtitleAnchorThread");
            thread.start();
            Handler handler = new Handler(thread.getLooper());
            handler.post(new Runnable() { // from class: android.media.MediaPlayer2Impl.24
                @Override // java.lang.Runnable
                public void run() {
                    Context context = ActivityThread.currentApplication();
                    MediaPlayer2Impl.this.mSubtitleController = new SubtitleController(context, MediaPlayer2Impl.this.mTimeProvider, MediaPlayer2Impl.this);
                    MediaPlayer2Impl.this.mSubtitleController.setAnchor(new SubtitleController.Anchor() { // from class: android.media.MediaPlayer2Impl.24.1
                        @Override // android.media.SubtitleController.Anchor
                        public void setSubtitleWidget(SubtitleTrack.RenderingWidget subtitleWidget) {
                        }

                        @Override // android.media.SubtitleController.Anchor
                        public Looper getSubtitleLooper() {
                            return Looper.getMainLooper();
                        }
                    });
                    thread.getLooper().quitSafely();
                }
            });
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.w(TAG, "failed to join SetSubtitleAnchorThread");
            }
        }
    }

    @Override // android.media.MediaPlayer2, android.media.SubtitleController.Listener
    public synchronized void onSubtitleTrackSelected(SubtitleTrack track) {
        if (this.mSelectedSubtitleTrackIndex >= 0) {
            try {
                selectOrDeselectInbandTrack(this.mSelectedSubtitleTrackIndex, false);
            } catch (IllegalStateException e) {
            }
            this.mSelectedSubtitleTrackIndex = -1;
        }
        setOnSubtitleDataListener(null);
        if (track == null) {
            return;
        }
        synchronized (this.mIndexTrackPairs) {
            Iterator<Pair<Integer, SubtitleTrack>> it = this.mIndexTrackPairs.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Pair<Integer, SubtitleTrack> p = it.next();
                if (p.first != null && p.second == track) {
                    this.mSelectedSubtitleTrackIndex = p.first.intValue();
                    break;
                }
            }
        }
        if (this.mSelectedSubtitleTrackIndex >= 0) {
            try {
                selectOrDeselectInbandTrack(this.mSelectedSubtitleTrackIndex, true);
            } catch (IllegalStateException e2) {
            }
            setOnSubtitleDataListener(this.mSubtitleDataListener);
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void addSubtitleSource(final InputStream is, final MediaFormat format) throws IllegalStateException {
        if (is != null) {
            synchronized (this.mOpenSubtitleSources) {
                this.mOpenSubtitleSources.add(is);
            }
        } else {
            Log.w(TAG, "addSubtitleSource called with null InputStream");
        }
        getMediaTimeProvider();
        final HandlerThread thread = new HandlerThread("SubtitleReadThread", 9);
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(new Runnable() { // from class: android.media.MediaPlayer2Impl.26
            private int addTrack() {
                SubtitleTrack track;
                if (is == null || MediaPlayer2Impl.this.mSubtitleController == null || (track = MediaPlayer2Impl.this.mSubtitleController.addTrack(format)) == null) {
                    return 901;
                }
                Scanner scanner = new Scanner(is, "UTF-8");
                String contents = scanner.useDelimiter("\\A").next();
                synchronized (MediaPlayer2Impl.this.mOpenSubtitleSources) {
                    MediaPlayer2Impl.this.mOpenSubtitleSources.remove(is);
                }
                scanner.close();
                synchronized (MediaPlayer2Impl.this.mIndexTrackPairs) {
                    MediaPlayer2Impl.this.mIndexTrackPairs.add(Pair.create(null, track));
                }
                Handler h = MediaPlayer2Impl.this.mTimeProvider.mEventHandler;
                Pair<SubtitleTrack, byte[]> trackData = Pair.create(track, contents.getBytes());
                Message m = h.obtainMessage(1, 4, 0, trackData);
                h.sendMessage(m);
                return 803;
            }

            @Override // java.lang.Runnable
            public void run() {
                int res = addTrack();
                if (MediaPlayer2Impl.this.mEventHandler != null) {
                    Message m = MediaPlayer2Impl.this.mEventHandler.obtainMessage(200, res, 0, null);
                    MediaPlayer2Impl.this.mEventHandler.sendMessage(m);
                }
                thread.getLooper().quitSafely();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void scanInternalSubtitleTracks() {
        setSubtitleAnchor();
        populateInbandTracks();
        if (this.mSubtitleController != null) {
            this.mSubtitleController.selectDefaultTrack();
        }
    }

    private synchronized void populateInbandTracks() {
        TrackInfoImpl[] tracks = getInbandTrackInfoImpl();
        synchronized (this.mIndexTrackPairs) {
            for (int i = 0; i < tracks.length; i++) {
                if (!this.mInbandTrackIndices.get(i)) {
                    this.mInbandTrackIndices.set(i);
                    if (tracks[i].getTrackType() == 4) {
                        SubtitleTrack track = this.mSubtitleController.addTrack(tracks[i].getFormat());
                        this.mIndexTrackPairs.add(Pair.create(Integer.valueOf(i), track));
                    } else {
                        this.mIndexTrackPairs.add(Pair.create(Integer.valueOf(i), null));
                    }
                }
            }
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void addTimedTextSource(String path, String mimeType) throws IOException {
        if (!availableMimeTypeForExternalSource(mimeType)) {
            String msg = "Illegal mimeType for timed text source: " + mimeType;
            throw new IllegalArgumentException(msg);
        }
        File file = new File(path);
        if (file.exists()) {
            FileInputStream is = new FileInputStream(file);
            FileDescriptor fd = is.getFD();
            addTimedTextSource(fd, mimeType);
            is.close();
            return;
        }
        throw new IOException(path);
    }

    @Override // android.media.MediaPlayer2
    public synchronized void addTimedTextSource(Context context, Uri uri, String mimeType) throws IOException {
        String scheme = uri.getScheme();
        if (scheme == null || scheme.equals(ContentResolver.SCHEME_FILE)) {
            addTimedTextSource(uri.getPath(), mimeType);
            return;
        }
        AssetFileDescriptor fd = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            fd = resolver.openAssetFileDescriptor(uri, FullBackup.ROOT_TREE_TOKEN);
            if (fd == null) {
                if (fd != null) {
                    fd.close();
                    return;
                }
                return;
            }
            addTimedTextSource(fd.getFileDescriptor(), mimeType);
            if (fd != null) {
                fd.close();
            }
        } catch (IOException e) {
            if (fd == null) {
                return;
            }
            fd.close();
        } catch (SecurityException e2) {
            if (fd == null) {
                return;
            }
            fd.close();
        } catch (Throwable th) {
            if (fd != null) {
                fd.close();
            }
            throw th;
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void addTimedTextSource(FileDescriptor fd, String mimeType) {
        addTimedTextSource(fd, 0L, DataSourceDesc.LONG_MAX, mimeType);
    }

    @Override // android.media.MediaPlayer2
    public synchronized void addTimedTextSource(FileDescriptor fd, final long offset, final long length, String mime) {
        if (!availableMimeTypeForExternalSource(mime)) {
            throw new IllegalArgumentException("Illegal mimeType for timed text source: " + mime);
        }
        try {
            final FileDescriptor dupedFd = Os.dup(fd);
            MediaFormat fFormat = new MediaFormat();
            fFormat.setString(MediaFormat.KEY_MIME, mime);
            fFormat.setInteger(MediaFormat.KEY_IS_TIMED_TEXT, 1);
            if (this.mSubtitleController == null) {
                setSubtitleAnchor();
            }
            if (!this.mSubtitleController.hasRendererFor(fFormat)) {
                Context context = ActivityThread.currentApplication();
                this.mSubtitleController.registerRenderer(new SRTRenderer(context, this.mEventHandler));
            }
            final SubtitleTrack track = this.mSubtitleController.addTrack(fFormat);
            synchronized (this.mIndexTrackPairs) {
                this.mIndexTrackPairs.add(Pair.create(null, track));
            }
            getMediaTimeProvider();
            final HandlerThread thread = new HandlerThread("TimedTextReadThread", 9);
            thread.start();
            Handler handler = new Handler(thread.getLooper());
            handler.post(new Runnable() { // from class: android.media.MediaPlayer2Impl.27
                private int addTrack() {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try {
                        try {
                            Os.lseek(dupedFd, offset, OsConstants.SEEK_SET);
                            byte[] buffer = new byte[4096];
                            long total = 0;
                            while (total < length) {
                                int bytesToRead = (int) Math.min(buffer.length, length - total);
                                int bytes = IoBridge.read(dupedFd, buffer, 0, bytesToRead);
                                if (bytes < 0) {
                                    break;
                                }
                                bos.write(buffer, 0, bytes);
                                total += bytes;
                            }
                            Handler h = MediaPlayer2Impl.this.mTimeProvider.mEventHandler;
                            Pair<SubtitleTrack, byte[]> trackData = Pair.create(track, bos.toByteArray());
                            Message m = h.obtainMessage(1, 4, 0, trackData);
                            h.sendMessage(m);
                            try {
                                Os.close(dupedFd);
                            } catch (ErrnoException e) {
                                Log.e(MediaPlayer2Impl.TAG, e.getMessage(), e);
                            }
                            return 803;
                        } catch (Throwable th) {
                            try {
                                Os.close(dupedFd);
                            } catch (ErrnoException e2) {
                                Log.e(MediaPlayer2Impl.TAG, e2.getMessage(), e2);
                            }
                            throw th;
                        }
                    } catch (Exception e3) {
                        Log.e(MediaPlayer2Impl.TAG, e3.getMessage(), e3);
                        try {
                            Os.close(dupedFd);
                        } catch (ErrnoException e4) {
                            Log.e(MediaPlayer2Impl.TAG, e4.getMessage(), e4);
                        }
                        return 900;
                    }
                }

                @Override // java.lang.Runnable
                public void run() {
                    int res = addTrack();
                    if (MediaPlayer2Impl.this.mEventHandler != null) {
                        Message m = MediaPlayer2Impl.this.mEventHandler.obtainMessage(200, res, 0, null);
                        MediaPlayer2Impl.this.mEventHandler.sendMessage(m);
                    }
                    thread.getLooper().quitSafely();
                }
            });
        } catch (ErrnoException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized int getSelectedTrack(int trackType) {
        SubtitleTrack subtitleTrack;
        if (this.mSubtitleController != null && ((trackType == 4 || trackType == 3) && (subtitleTrack = this.mSubtitleController.getSelectedTrack()) != null)) {
            synchronized (this.mIndexTrackPairs) {
                for (int i = 0; i < this.mIndexTrackPairs.size(); i++) {
                    if (this.mIndexTrackPairs.get(i).second == subtitleTrack && subtitleTrack.getTrackType() == trackType) {
                        return i;
                    }
                }
            }
        }
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInt(7);
            request.writeInt(trackType);
            invoke(request, reply);
            int inbandTrackIndex = reply.readInt();
            synchronized (this.mIndexTrackPairs) {
                for (int i2 = 0; i2 < this.mIndexTrackPairs.size(); i2++) {
                    Pair<Integer, SubtitleTrack> p = this.mIndexTrackPairs.get(i2);
                    if (p.first != null && p.first.intValue() == inbandTrackIndex) {
                        return i2;
                    }
                }
                return -1;
            }
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void selectTrack(final int index) {
        addTask(new Task(15, false) { // from class: android.media.MediaPlayer2Impl.28
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this.selectOrDeselectTrack(index, true);
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized void deselectTrack(final int index) {
        addTask(new Task(2, false) { // from class: android.media.MediaPlayer2Impl.29
            @Override // android.media.MediaPlayer2Impl.Task
            void process() {
                MediaPlayer2Impl.this.selectOrDeselectTrack(index, false);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void selectOrDeselectTrack(int index, boolean select) throws IllegalStateException {
        populateInbandTracks();
        try {
            Pair<Integer, SubtitleTrack> p = this.mIndexTrackPairs.get(index);
            SubtitleTrack track = p.second;
            if (track == null) {
                selectOrDeselectInbandTrack(p.first.intValue(), select);
            } else if (this.mSubtitleController == null) {
            } else {
                if (!select) {
                    if (this.mSubtitleController.getSelectedTrack() == track) {
                        this.mSubtitleController.selectTrack(null);
                        return;
                    } else {
                        Log.w(TAG, "trying to deselect track that was not selected");
                        return;
                    }
                }
                if (track.getTrackType() == 3) {
                    int ttIndex = getSelectedTrack(3);
                    synchronized (this.mIndexTrackPairs) {
                        if (ttIndex >= 0) {
                            try {
                                if (ttIndex < this.mIndexTrackPairs.size()) {
                                    Pair<Integer, SubtitleTrack> p2 = this.mIndexTrackPairs.get(ttIndex);
                                    if (p2.first != null && p2.second == null) {
                                        selectOrDeselectInbandTrack(p2.first.intValue(), false);
                                    }
                                }
                            } finally {
                            }
                        }
                    }
                }
                this.mSubtitleController.selectTrack(track);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private synchronized void selectOrDeselectInbandTrack(int index, boolean select) throws IllegalStateException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInt(select ? 4 : 5);
            request.writeInt(index);
            invoke(request, reply);
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    protected void finalize() throws Throwable {
        if (this.mGuard != null) {
            this.mGuard.warnIfOpen();
        }
        close();
        native_finalize();
    }

    private synchronized void release() {
        stayAwake(false);
        updateSurfaceScreenOn();
        synchronized (this.mEventCbLock) {
            this.mEventCallbackRecords.clear();
        }
        if (this.mHandlerThread != null) {
            this.mHandlerThread.quitSafely();
            this.mHandlerThread = null;
        }
        if (this.mTimeProvider != null) {
            this.mTimeProvider.close();
            this.mTimeProvider = null;
        }
        this.mOnSubtitleDataListener = null;
        this.mOnDrmConfigHelper = null;
        synchronized (this.mDrmEventCbLock) {
            this.mDrmEventCallbackRecords.clear();
        }
        resetDrmState();
        _release();
    }

    @Override // android.media.MediaPlayer2
    public synchronized MediaTimeProvider getMediaTimeProvider() {
        if (this.mTimeProvider == null) {
            this.mTimeProvider = new TimeProvider(this);
        }
        return this.mTimeProvider;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class EventHandler extends Handler {
        private MediaPlayer2Impl mMediaPlayer;

        public EventHandler(MediaPlayer2Impl mp, Looper looper) {
            super(looper);
            this.mMediaPlayer = mp;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            handleMessage(msg, 0L);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r2v52, types: [android.media.TimedText] */
        /* JADX WARN: Type inference failed for: r3v6, types: [android.media.MediaPlayer2Impl$DrmInfoImpl] */
        /* JADX WARN: Type inference failed for: r3v68, types: [android.media.TimedText] */
        /* JADX WARN: Type inference failed for: r3v9, types: [android.media.MediaPlayer2Impl$DrmInfoImpl] */
        public synchronized void handleMessage(Message msg, long srcId) {
            final ?? r3;
            final DataSourceDesc dsd;
            if (this.mMediaPlayer.mNativeContext == 0) {
                Log.w(MediaPlayer2Impl.TAG, "mediaplayer2 went away with unhandled events");
                return;
            }
            final int what = msg.arg1;
            final int extra = msg.arg2;
            int i = msg.what;
            if (i == 210) {
                if (msg.obj == null) {
                    Log.w(MediaPlayer2Impl.TAG, "MEDIA_DRM_INFO msg.obj=NULL");
                } else if (msg.obj instanceof Parcel) {
                    synchronized (MediaPlayer2Impl.this.mDrmLock) {
                        data = MediaPlayer2Impl.this.mDrmInfoImpl != null ? MediaPlayer2Impl.this.mDrmInfoImpl.makeCopy() : null;
                        r3 = data;
                    }
                    if (r3 != 0) {
                        synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                            Iterator it = MediaPlayer2Impl.this.mDrmEventCallbackRecords.iterator();
                            while (it.hasNext()) {
                                final Pair<Executor, MediaPlayer2.DrmEventCallback> cb = (Pair) it.next();
                                ((Executor) cb.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$XDpOSvYSapoVyl-BYW0W8pLfp3A
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        MediaPlayer2Impl.EventHandler eventHandler = MediaPlayer2Impl.EventHandler.this;
                                        ((MediaPlayer2.DrmEventCallback) cb.second).onDrmInfo(eventHandler.mMediaPlayer, MediaPlayer2Impl.this.mCurrentDSD, r3);
                                    }
                                });
                            }
                        }
                    }
                } else {
                    Log.w(MediaPlayer2Impl.TAG, "MEDIA_DRM_INFO msg.obj of unexpected type " + msg.obj);
                }
            } else if (i == 10000) {
                AudioManager.resetAudioPortGeneration();
                synchronized (MediaPlayer2Impl.this.mRoutingChangeListeners) {
                    for (NativeRoutingEventHandlerDelegate delegate : MediaPlayer2Impl.this.mRoutingChangeListeners.values()) {
                        delegate.notifyClient();
                    }
                }
            } else {
                switch (i) {
                    case 0:
                        return;
                    case 1:
                        try {
                            MediaPlayer2Impl.this.scanInternalSubtitleTracks();
                        } catch (RuntimeException e) {
                            Message msg2 = obtainMessage(100, 1, -1010, null);
                            sendMessage(msg2);
                        }
                        synchronized (MediaPlayer2Impl.this.mSrcLock) {
                            Log.i(MediaPlayer2Impl.TAG, "MEDIA_PREPARED: srcId=" + srcId + ", currentSrcId=" + MediaPlayer2Impl.this.mCurrentSrcId + ", nextSrcId=" + MediaPlayer2Impl.this.mNextSrcId);
                            if (srcId == MediaPlayer2Impl.this.mCurrentSrcId) {
                                dsd = MediaPlayer2Impl.this.mCurrentDSD;
                                MediaPlayer2Impl.this.prepareNextDataSource_l();
                            } else if (MediaPlayer2Impl.this.mNextDSDs != null && !MediaPlayer2Impl.this.mNextDSDs.isEmpty() && srcId == MediaPlayer2Impl.this.mNextSrcId) {
                                dsd = (DataSourceDesc) MediaPlayer2Impl.this.mNextDSDs.get(0);
                                MediaPlayer2Impl.this.mNextSourceState = 2;
                                if (MediaPlayer2Impl.this.mNextSourcePlayPending) {
                                    MediaPlayer2Impl.this.playNextDataSource_l();
                                }
                            } else {
                                dsd = null;
                            }
                        }
                        if (dsd != null) {
                            synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                                Iterator it2 = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                                while (it2.hasNext()) {
                                    final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb2 = (Pair) it2.next();
                                    ((Executor) cb2.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$a55WUDW_Ad0Vmi1x4yZhQXvPqdc
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            MediaPlayer2Impl.EventHandler eventHandler = MediaPlayer2Impl.EventHandler.this;
                                            ((MediaPlayer2.MediaPlayer2EventCallback) cb2.second).onInfo(eventHandler.mMediaPlayer, dsd, 100, 0);
                                        }
                                    });
                                }
                            }
                        }
                        synchronized (MediaPlayer2Impl.this.mTaskLock) {
                            if (MediaPlayer2Impl.this.mCurrentTask != null && MediaPlayer2Impl.this.mCurrentTask.mMediaCallType == 6 && MediaPlayer2Impl.this.mCurrentTask.mDSD == dsd && MediaPlayer2Impl.this.mCurrentTask.mNeedToWaitForEventToComplete) {
                                MediaPlayer2Impl.this.mCurrentTask.sendCompleteNotification(0);
                                MediaPlayer2Impl.this.mCurrentTask = null;
                                MediaPlayer2Impl.this.processPendingTask_l();
                            }
                        }
                        return;
                    case 2:
                        final DataSourceDesc dsd2 = MediaPlayer2Impl.this.mCurrentDSD;
                        synchronized (MediaPlayer2Impl.this.mSrcLock) {
                            if (srcId == MediaPlayer2Impl.this.mCurrentSrcId) {
                                Log.i(MediaPlayer2Impl.TAG, "MEDIA_PLAYBACK_COMPLETE: srcId=" + srcId + ", currentSrcId=" + MediaPlayer2Impl.this.mCurrentSrcId + ", nextSrcId=" + MediaPlayer2Impl.this.mNextSrcId);
                                MediaPlayer2Impl.this.playNextDataSource_l();
                            }
                        }
                        synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                            Iterator it3 = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                            while (it3.hasNext()) {
                                final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb3 = (Pair) it3.next();
                                ((Executor) cb3.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$SRqj_-_1CH9_ez58ikKgR8GPWEc
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        MediaPlayer2Impl.EventHandler eventHandler = MediaPlayer2Impl.EventHandler.this;
                                        ((MediaPlayer2.MediaPlayer2EventCallback) cb3.second).onInfo(eventHandler.mMediaPlayer, dsd2, 5, 0);
                                    }
                                });
                            }
                        }
                        MediaPlayer2Impl.this.stayAwake(false);
                        return;
                    case 3:
                        final int percent = msg.arg1;
                        synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                            if (srcId == MediaPlayer2Impl.this.mCurrentSrcId) {
                                MediaPlayer2Impl.this.mBufferedPercentageCurrent.set(percent);
                                Iterator it4 = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                                while (it4.hasNext()) {
                                    final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb4 = (Pair) it4.next();
                                    ((Executor) cb4.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$Dr_ImxKsZcrvP7slv6KPxdUdzXk
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            MediaPlayer2Impl.EventHandler eventHandler = MediaPlayer2Impl.EventHandler.this;
                                            ((MediaPlayer2.MediaPlayer2EventCallback) cb4.second).onInfo(eventHandler.mMediaPlayer, MediaPlayer2Impl.this.mCurrentDSD, 704, percent);
                                        }
                                    });
                                }
                            } else if (srcId == MediaPlayer2Impl.this.mNextSrcId && !MediaPlayer2Impl.this.mNextDSDs.isEmpty()) {
                                MediaPlayer2Impl.this.mBufferedPercentageNext.set(percent);
                                final DataSourceDesc nextDSD = (DataSourceDesc) MediaPlayer2Impl.this.mNextDSDs.get(0);
                                Iterator it5 = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                                while (it5.hasNext()) {
                                    final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb5 = (Pair) it5.next();
                                    ((Executor) cb5.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$hsCyoCNpv30l9tb7sOpVC4dnMy8
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            MediaPlayer2Impl.EventHandler eventHandler = MediaPlayer2Impl.EventHandler.this;
                                            ((MediaPlayer2.MediaPlayer2EventCallback) cb5.second).onInfo(eventHandler.mMediaPlayer, nextDSD, 704, percent);
                                        }
                                    });
                                }
                            }
                        }
                        return;
                    case 4:
                        synchronized (MediaPlayer2Impl.this.mTaskLock) {
                            if (MediaPlayer2Impl.this.mCurrentTask != null && MediaPlayer2Impl.this.mCurrentTask.mMediaCallType == 14 && MediaPlayer2Impl.this.mCurrentTask.mNeedToWaitForEventToComplete) {
                                MediaPlayer2Impl.this.mCurrentTask.sendCompleteNotification(0);
                                MediaPlayer2Impl.this.mCurrentTask = null;
                                MediaPlayer2Impl.this.processPendingTask_l();
                            }
                            break;
                        }
                    case 5:
                        final int width = msg.arg1;
                        final int height = msg.arg2;
                        synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                            Iterator it6 = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                            while (it6.hasNext()) {
                                final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb6 = (Pair) it6.next();
                                ((Executor) cb6.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$ALpPmFUNsJxKZK0N2HhQK6ZY4XM
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        MediaPlayer2Impl.EventHandler eventHandler = MediaPlayer2Impl.EventHandler.this;
                                        ((MediaPlayer2.MediaPlayer2EventCallback) cb6.second).onVideoSizeChanged(eventHandler.mMediaPlayer, MediaPlayer2Impl.this.mCurrentDSD, width, height);
                                    }
                                });
                            }
                        }
                        return;
                    case 6:
                    case 7:
                        TimeProvider timeProvider = MediaPlayer2Impl.this.mTimeProvider;
                        if (timeProvider != null) {
                            timeProvider.onPaused(msg.what == 7);
                            return;
                        }
                        return;
                    case 8:
                        TimeProvider timeProvider2 = MediaPlayer2Impl.this.mTimeProvider;
                        if (timeProvider2 != null) {
                            timeProvider2.onStopped();
                            return;
                        }
                        return;
                    case 9:
                        break;
                    default:
                        switch (i) {
                            case 98:
                                TimeProvider timeProvider3 = MediaPlayer2Impl.this.mTimeProvider;
                                if (timeProvider3 != null) {
                                    timeProvider3.onNotifyTime();
                                    return;
                                }
                                return;
                            case 99:
                                if (msg.obj instanceof Parcel) {
                                    Parcel parcel = (Parcel) msg.obj;
                                    data = new TimedText(parcel);
                                    parcel.recycle();
                                }
                                final ?? r2 = data;
                                synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                                    Iterator it7 = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                                    while (it7.hasNext()) {
                                        final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb7 = (Pair) it7.next();
                                        ((Executor) cb7.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$sx24vrhw_-7V07cadDNXlQ5kv04
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                MediaPlayer2Impl.EventHandler eventHandler = MediaPlayer2Impl.EventHandler.this;
                                                ((MediaPlayer2.MediaPlayer2EventCallback) cb7.second).onTimedText(eventHandler.mMediaPlayer, MediaPlayer2Impl.this.mCurrentDSD, r2);
                                            }
                                        });
                                    }
                                }
                                return;
                            case 100:
                                Log.e(MediaPlayer2Impl.TAG, "Error (" + msg.arg1 + "," + msg.arg2 + ")");
                                synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                                    Iterator it8 = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                                    while (it8.hasNext()) {
                                        final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb8 = (Pair) it8.next();
                                        ((Executor) cb8.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$5fCusDxj0OAxGzH6d86WnqVt8Rw
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                MediaPlayer2Impl.EventHandler eventHandler = MediaPlayer2Impl.EventHandler.this;
                                                ((MediaPlayer2.MediaPlayer2EventCallback) cb8.second).onError(eventHandler.mMediaPlayer, MediaPlayer2Impl.this.mCurrentDSD, what, extra);
                                            }
                                        });
                                        ((Executor) cb8.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$9rzGOSqsKQVeN_cdPvY8essrTyg
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                ((MediaPlayer2.MediaPlayer2EventCallback) cb8.second).onInfo(r0.mMediaPlayer, MediaPlayer2Impl.this.mCurrentDSD, 5, 0);
                                            }
                                        });
                                    }
                                }
                                MediaPlayer2Impl.this.stayAwake(false);
                                return;
                            default:
                                switch (i) {
                                    case 200:
                                        int i2 = msg.arg1;
                                        if (i2 == 2) {
                                            if (srcId == MediaPlayer2Impl.this.mCurrentSrcId) {
                                                MediaPlayer2Impl.this.prepareNextDataSource_l();
                                            }
                                        } else {
                                            switch (i2) {
                                                case 700:
                                                    Log.i(MediaPlayer2Impl.TAG, "Info (" + msg.arg1 + "," + msg.arg2 + ")");
                                                    break;
                                                case 701:
                                                case 702:
                                                    TimeProvider timeProvider4 = MediaPlayer2Impl.this.mTimeProvider;
                                                    if (timeProvider4 != null) {
                                                        timeProvider4.onBuffering(msg.arg1 == 701);
                                                        break;
                                                    }
                                                    break;
                                                default:
                                                    switch (i2) {
                                                        case 802:
                                                            try {
                                                                MediaPlayer2Impl.this.scanInternalSubtitleTracks();
                                                            } catch (RuntimeException e2) {
                                                                Message msg22 = obtainMessage(100, 1, -1010, null);
                                                                sendMessage(msg22);
                                                            }
                                                        case 803:
                                                            msg.arg1 = 802;
                                                            if (MediaPlayer2Impl.this.mSubtitleController != null) {
                                                                MediaPlayer2Impl.this.mSubtitleController.selectDefaultTrack();
                                                                break;
                                                            }
                                                            break;
                                                    }
                                                    break;
                                            }
                                        }
                                        synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                                            Iterator it9 = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                                            while (it9.hasNext()) {
                                                final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb9 = (Pair) it9.next();
                                                ((Executor) cb9.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$iPmZQ0HxMVwbBcbhgpHbun3WGTk
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        MediaPlayer2Impl.EventHandler eventHandler = MediaPlayer2Impl.EventHandler.this;
                                                        ((MediaPlayer2.MediaPlayer2EventCallback) cb9.second).onInfo(eventHandler.mMediaPlayer, MediaPlayer2Impl.this.mCurrentDSD, what, extra);
                                                    }
                                                });
                                            }
                                        }
                                        return;
                                    case 201:
                                        MediaPlayer2.OnSubtitleDataListener onSubtitleDataListener = MediaPlayer2Impl.this.mOnSubtitleDataListener;
                                        if (onSubtitleDataListener != null && (msg.obj instanceof Parcel)) {
                                            Parcel parcel2 = (Parcel) msg.obj;
                                            SubtitleData data = new SubtitleData(parcel2);
                                            parcel2.recycle();
                                            onSubtitleDataListener.onSubtitleData(this.mMediaPlayer, data);
                                            return;
                                        }
                                        return;
                                    case 202:
                                        if (msg.obj instanceof Parcel) {
                                            Parcel parcel3 = (Parcel) msg.obj;
                                            data = TimedMetaData.createTimedMetaDataFromParcel(parcel3);
                                            parcel3.recycle();
                                        }
                                        final TimedMetaData data2 = data;
                                        synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                                            Iterator it10 = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                                            while (it10.hasNext()) {
                                                final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb10 = (Pair) it10.next();
                                                ((Executor) cb10.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$EventHandler$5DmGtkuYQXExyXOBI9Qvu64NQ68
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        MediaPlayer2Impl.EventHandler eventHandler = MediaPlayer2Impl.EventHandler.this;
                                                        ((MediaPlayer2.MediaPlayer2EventCallback) cb10.second).onTimedMetaDataAvailable(eventHandler.mMediaPlayer, MediaPlayer2Impl.this.mCurrentDSD, data2);
                                                    }
                                                });
                                            }
                                        }
                                        return;
                                    default:
                                        Log.e(MediaPlayer2Impl.TAG, "Unknown message type " + msg.what);
                                        return;
                                }
                        }
                }
                TimeProvider timeProvider5 = MediaPlayer2Impl.this.mTimeProvider;
                if (timeProvider5 != null) {
                    timeProvider5.onSeekComplete(this.mMediaPlayer);
                }
            }
        }
    }

    private static synchronized void postEventFromNative(Object mediaplayer2_ref, final long srcId, int what, int arg1, int arg2, Object obj) {
        MediaPlayer2Impl mp = (MediaPlayer2Impl) ((WeakReference) mediaplayer2_ref).get();
        if (mp == null) {
            return;
        }
        if (what == 1) {
            synchronized (mp.mDrmLock) {
                mp.mDrmInfoResolved = true;
            }
        } else if (what != 200) {
            if (what == 210) {
                Log.v(TAG, "postEventFromNative MEDIA_DRM_INFO");
                if (obj instanceof Parcel) {
                    Parcel parcel = (Parcel) obj;
                    DrmInfoImpl drmInfo = new DrmInfoImpl(parcel);
                    synchronized (mp.mDrmLock) {
                        mp.mDrmInfoImpl = drmInfo;
                    }
                } else {
                    Log.w(TAG, "MEDIA_DRM_INFO msg.obj of unexpected type " + obj);
                }
            }
        } else if (arg1 == 2) {
            new Thread(new Runnable() { // from class: android.media.MediaPlayer2Impl.30
                @Override // java.lang.Runnable
                public void run() {
                    MediaPlayer2Impl.this.play();
                }
            }).start();
            Thread.yield();
        }
        if (mp.mEventHandler != null) {
            final Message m = mp.mEventHandler.obtainMessage(what, arg1, arg2, obj);
            mp.mEventHandler.post(new Runnable() { // from class: android.media.MediaPlayer2Impl.31
                @Override // java.lang.Runnable
                public void run() {
                    MediaPlayer2Impl.this.mEventHandler.handleMessage(m, srcId);
                }
            });
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setMediaPlayer2EventCallback(Executor executor, MediaPlayer2.MediaPlayer2EventCallback eventCallback) {
        if (eventCallback == null) {
            throw new IllegalArgumentException("Illegal null MediaPlayer2EventCallback");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Illegal null Executor for the MediaPlayer2EventCallback");
        }
        synchronized (this.mEventCbLock) {
            this.mEventCallbackRecords.add(new Pair<>(executor, eventCallback));
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void clearMediaPlayer2EventCallback() {
        synchronized (this.mEventCbLock) {
            this.mEventCallbackRecords.clear();
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setOnSubtitleDataListener(MediaPlayer2.OnSubtitleDataListener listener) {
        this.mOnSubtitleDataListener = listener;
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setOnDrmConfigHelper(MediaPlayer2.OnDrmConfigHelper listener) {
        synchronized (this.mDrmLock) {
            this.mOnDrmConfigHelper = listener;
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setDrmEventCallback(Executor executor, MediaPlayer2.DrmEventCallback eventCallback) {
        if (eventCallback == null) {
            throw new IllegalArgumentException("Illegal null MediaPlayer2EventCallback");
        }
        if (executor == null) {
            throw new IllegalArgumentException("Illegal null Executor for the MediaPlayer2EventCallback");
        }
        synchronized (this.mDrmEventCbLock) {
            this.mDrmEventCallbackRecords.add(new Pair<>(executor, eventCallback));
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized void clearDrmEventCallback() {
        synchronized (this.mDrmEventCbLock) {
            this.mDrmEventCallbackRecords.clear();
        }
    }

    @Override // android.media.MediaPlayer2
    public synchronized MediaPlayer2.DrmInfo getDrmInfo() {
        DrmInfoImpl drmInfo = null;
        synchronized (this.mDrmLock) {
            if (!this.mDrmInfoResolved && this.mDrmInfoImpl == null) {
                Log.v(TAG, "The Player has not been prepared yet");
                throw new IllegalStateException("The Player has not been prepared yet");
            }
            if (this.mDrmInfoImpl != null) {
                drmInfo = this.mDrmInfoImpl.makeCopy();
            }
        }
        return drmInfo;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0062, code lost:
        if (0 != 0) goto L33;
     */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00f7 A[ORIG_RETURN, RETURN] */
    @Override // android.media.MediaPlayer2
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void prepareDrm(java.util.UUID r9) throws android.media.UnsupportedSchemeException, android.media.ResourceBusyException, android.media.MediaPlayer2.ProvisioningNetworkErrorException, android.media.MediaPlayer2.ProvisioningServerErrorException {
        /*
            Method dump skipped, instructions count: 400
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer2Impl.prepareDrm(java.util.UUID):void");
    }

    @Override // android.media.MediaPlayer2
    public synchronized void releaseDrm() throws MediaPlayer2.NoDrmSchemeException {
        addTask(new Task(12, false) { // from class: android.media.MediaPlayer2Impl.32
            @Override // android.media.MediaPlayer2Impl.Task
            void process() throws MediaPlayer2.NoDrmSchemeException {
                synchronized (MediaPlayer2Impl.this.mDrmLock) {
                    Log.v(MediaPlayer2Impl.TAG, "releaseDrm:");
                    if (MediaPlayer2Impl.this.mActiveDrmScheme) {
                        try {
                            MediaPlayer2Impl.this._releaseDrm();
                            MediaPlayer2Impl.this.cleanDrmObj();
                            MediaPlayer2Impl.this.mActiveDrmScheme = false;
                        } catch (IllegalStateException e) {
                            Log.w(MediaPlayer2Impl.TAG, "releaseDrm: Exception ", e);
                            throw new IllegalStateException("releaseDrm: The player is not in a valid state.");
                        } catch (Exception e2) {
                            Log.e(MediaPlayer2Impl.TAG, "releaseDrm: Exception ", e2);
                        }
                    } else {
                        Log.e(MediaPlayer2Impl.TAG, "releaseDrm(): No active DRM scheme to release.");
                        throw new NoDrmSchemeExceptionImpl("releaseDrm: No active DRM scheme to release.");
                    }
                }
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized MediaDrm.KeyRequest getDrmKeyRequest(byte[] keySetId, byte[] initData, String mimeType, int keyType, Map<String, String> optionalParameters) throws MediaPlayer2.NoDrmSchemeException {
        byte[] scope;
        HashMap<String, String> hashMap;
        MediaDrm.KeyRequest request;
        Log.v(TAG, "getDrmKeyRequest:  keySetId: " + keySetId + " initData:" + initData + " mimeType: " + mimeType + " keyType: " + keyType + " optionalParameters: " + optionalParameters);
        synchronized (this.mDrmLock) {
            if (!this.mActiveDrmScheme) {
                Log.e(TAG, "getDrmKeyRequest NoDrmSchemeException");
                throw new NoDrmSchemeExceptionImpl("getDrmKeyRequest: Has to set a DRM scheme first.");
            }
            if (keyType != 3) {
                try {
                    scope = this.mDrmSessionId;
                } catch (NotProvisionedException e) {
                    Log.w(TAG, "getDrmKeyRequest NotProvisionedException: Unexpected. Shouldn't have reached here.");
                    throw new IllegalStateException("getDrmKeyRequest: Unexpected provisioning error.");
                } catch (Exception e2) {
                    Log.w(TAG, "getDrmKeyRequest Exception " + e2);
                    throw e2;
                }
            } else {
                scope = keySetId;
            }
            if (optionalParameters != null) {
                hashMap = new HashMap<>(optionalParameters);
            } else {
                hashMap = null;
            }
            HashMap<String, String> hmapOptionalParameters = hashMap;
            request = this.mDrmObj.getKeyRequest(scope, initData, mimeType, keyType, hmapOptionalParameters);
            Log.v(TAG, "getDrmKeyRequest:   --> request: " + request);
        }
        return request;
    }

    @Override // android.media.MediaPlayer2
    public synchronized byte[] provideDrmKeyResponse(byte[] keySetId, byte[] response) throws MediaPlayer2.NoDrmSchemeException, DeniedByServerException {
        byte[] scope;
        byte[] keySetResult;
        Log.v(TAG, "provideDrmKeyResponse: keySetId: " + keySetId + " response: " + response);
        synchronized (this.mDrmLock) {
            if (!this.mActiveDrmScheme) {
                Log.e(TAG, "getDrmKeyRequest NoDrmSchemeException");
                throw new NoDrmSchemeExceptionImpl("getDrmKeyRequest: Has to set a DRM scheme first.");
            }
            if (keySetId == null) {
                try {
                    scope = this.mDrmSessionId;
                } catch (NotProvisionedException e) {
                    Log.w(TAG, "provideDrmKeyResponse NotProvisionedException: Unexpected. Shouldn't have reached here.");
                    throw new IllegalStateException("provideDrmKeyResponse: Unexpected provisioning error.");
                } catch (Exception e2) {
                    Log.w(TAG, "provideDrmKeyResponse Exception " + e2);
                    throw e2;
                }
            } else {
                scope = keySetId;
            }
            keySetResult = this.mDrmObj.provideKeyResponse(scope, response);
            Log.v(TAG, "provideDrmKeyResponse: keySetId: " + keySetId + " response: " + response + " --> " + keySetResult);
        }
        return keySetResult;
    }

    @Override // android.media.MediaPlayer2
    public synchronized void restoreDrmKeys(final byte[] keySetId) throws MediaPlayer2.NoDrmSchemeException {
        addTask(new Task(13, false) { // from class: android.media.MediaPlayer2Impl.33
            @Override // android.media.MediaPlayer2Impl.Task
            void process() throws MediaPlayer2.NoDrmSchemeException {
                Log.v(MediaPlayer2Impl.TAG, "restoreDrmKeys: keySetId: " + keySetId);
                synchronized (MediaPlayer2Impl.this.mDrmLock) {
                    if (MediaPlayer2Impl.this.mActiveDrmScheme) {
                        try {
                            MediaPlayer2Impl.this.mDrmObj.restoreKeys(MediaPlayer2Impl.this.mDrmSessionId, keySetId);
                        } catch (Exception e) {
                            Log.w(MediaPlayer2Impl.TAG, "restoreKeys Exception " + e);
                            throw e;
                        }
                    } else {
                        Log.w(MediaPlayer2Impl.TAG, "restoreDrmKeys NoDrmSchemeException");
                        throw new NoDrmSchemeExceptionImpl("restoreDrmKeys: Has to set a DRM scheme first.");
                    }
                }
            }
        });
    }

    @Override // android.media.MediaPlayer2
    public synchronized String getDrmPropertyString(String propertyName) throws MediaPlayer2.NoDrmSchemeException {
        String value;
        Log.v(TAG, "getDrmPropertyString: propertyName: " + propertyName);
        synchronized (this.mDrmLock) {
            if (!this.mActiveDrmScheme && !this.mDrmConfigAllowed) {
                Log.w(TAG, "getDrmPropertyString NoDrmSchemeException");
                throw new NoDrmSchemeExceptionImpl("getDrmPropertyString: Has to prepareDrm() first.");
            }
            try {
                value = this.mDrmObj.getPropertyString(propertyName);
            } catch (Exception e) {
                Log.w(TAG, "getDrmPropertyString Exception " + e);
                throw e;
            }
        }
        Log.v(TAG, "getDrmPropertyString: propertyName: " + propertyName + " --> value: " + value);
        return value;
    }

    @Override // android.media.MediaPlayer2
    public synchronized void setDrmPropertyString(String propertyName, String value) throws MediaPlayer2.NoDrmSchemeException {
        Log.v(TAG, "setDrmPropertyString: propertyName: " + propertyName + " value: " + value);
        synchronized (this.mDrmLock) {
            if (!this.mActiveDrmScheme && !this.mDrmConfigAllowed) {
                Log.w(TAG, "setDrmPropertyString NoDrmSchemeException");
                throw new NoDrmSchemeExceptionImpl("setDrmPropertyString: Has to prepareDrm() first.");
            }
            try {
                this.mDrmObj.setPropertyString(propertyName, value);
            } catch (Exception e) {
                Log.w(TAG, "setDrmPropertyString Exception " + e);
                throw e;
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class DrmInfoImpl extends MediaPlayer2.DrmInfo {
        private Map<UUID, byte[]> mapPssh;
        private UUID[] supportedSchemes;

        @Override // android.media.MediaPlayer2.DrmInfo
        public synchronized Map<UUID, byte[]> getPssh() {
            return this.mapPssh;
        }

        @Override // android.media.MediaPlayer2.DrmInfo
        public synchronized List<UUID> getSupportedSchemes() {
            return Arrays.asList(this.supportedSchemes);
        }

        private synchronized DrmInfoImpl(Map<UUID, byte[]> Pssh, UUID[] SupportedSchemes) {
            this.mapPssh = Pssh;
            this.supportedSchemes = SupportedSchemes;
        }

        private synchronized DrmInfoImpl(Parcel parcel) {
            Log.v(MediaPlayer2Impl.TAG, "DrmInfoImpl(" + parcel + ") size " + parcel.dataSize());
            int psshsize = parcel.readInt();
            byte[] pssh = new byte[psshsize];
            parcel.readByteArray(pssh);
            Log.v(MediaPlayer2Impl.TAG, "DrmInfoImpl() PSSH: " + arrToHex(pssh));
            this.mapPssh = parsePSSH(pssh, psshsize);
            Log.v(MediaPlayer2Impl.TAG, "DrmInfoImpl() PSSH: " + this.mapPssh);
            int supportedDRMsCount = parcel.readInt();
            this.supportedSchemes = new UUID[supportedDRMsCount];
            for (int i = 0; i < supportedDRMsCount; i++) {
                byte[] uuid = new byte[16];
                parcel.readByteArray(uuid);
                this.supportedSchemes[i] = bytesToUUID(uuid);
                Log.v(MediaPlayer2Impl.TAG, "DrmInfoImpl() supportedScheme[" + i + "]: " + this.supportedSchemes[i]);
            }
            Log.v(MediaPlayer2Impl.TAG, "DrmInfoImpl() Parcel psshsize: " + psshsize + " supportedDRMsCount: " + supportedDRMsCount);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized DrmInfoImpl makeCopy() {
            return new DrmInfoImpl(this.mapPssh, this.supportedSchemes);
        }

        private synchronized String arrToHex(byte[] bytes) {
            String out = "0x";
            for (int i = 0; i < bytes.length; i++) {
                out = out + String.format("%02x", Byte.valueOf(bytes[i]));
            }
            return out;
        }

        private synchronized UUID bytesToUUID(byte[] uuid) {
            long msb = 0;
            long lsb = 0;
            for (int i = 0; i < 8; i++) {
                msb |= (uuid[i] & 255) << ((7 - i) * 8);
                lsb |= (uuid[i + 8] & 255) << (8 * (7 - i));
            }
            return new UUID(msb, lsb);
        }

        private synchronized Map<UUID, byte[]> parsePSSH(byte[] pssh, int psshsize) {
            int datalen;
            Map<UUID, byte[]> result = new HashMap<>();
            char c = 0;
            int numentries = 0;
            int numentries2 = psshsize;
            int len = 0;
            while (numentries2 > 0) {
                if (numentries2 < 16) {
                    Object[] objArr = new Object[2];
                    objArr[c] = Integer.valueOf(numentries2);
                    objArr[1] = Integer.valueOf(psshsize);
                    Log.w(MediaPlayer2Impl.TAG, String.format("parsePSSH: len is too short to parse UUID: (%d < 16) pssh: %d", objArr));
                    return null;
                }
                UUID uuid = bytesToUUID(Arrays.copyOfRange(pssh, len, len + 16));
                int i = len + 16;
                int len2 = numentries2 - 16;
                if (len2 < 4) {
                    Object[] objArr2 = new Object[2];
                    objArr2[c] = Integer.valueOf(len2);
                    objArr2[1] = Integer.valueOf(psshsize);
                    Log.w(MediaPlayer2Impl.TAG, String.format("parsePSSH: len is too short to parse datalen: (%d < 4) pssh: %d", objArr2));
                    return null;
                }
                byte[] subset = Arrays.copyOfRange(pssh, i, i + 4);
                if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                    datalen = ((subset[3] & 255) << 24) | ((subset[2] & 255) << 16) | ((subset[1] & 255) << 8) | (subset[0] & 255);
                } else {
                    datalen = ((subset[0] & 255) << 24) | ((subset[1] & 255) << 16) | ((subset[2] & 255) << 8) | (subset[3] & 255);
                }
                int i2 = i + 4;
                int len3 = len2 - 4;
                if (len3 < datalen) {
                    Log.w(MediaPlayer2Impl.TAG, String.format("parsePSSH: len is too short to parse data: (%d < %d) pssh: %d", Integer.valueOf(len3), Integer.valueOf(datalen), Integer.valueOf(psshsize)));
                    return null;
                }
                byte[] data = Arrays.copyOfRange(pssh, i2, i2 + datalen);
                len = i2 + datalen;
                numentries2 = len3 - datalen;
                Log.v(MediaPlayer2Impl.TAG, String.format("parsePSSH[%d]: <%s, %s> pssh: %d", Integer.valueOf(numentries), uuid, arrToHex(data), Integer.valueOf(psshsize)));
                numentries++;
                result.put(uuid, data);
                c = 0;
            }
            return result;
        }
    }

    /* loaded from: classes.dex */
    public static final class NoDrmSchemeExceptionImpl extends MediaPlayer2.NoDrmSchemeException {
        public synchronized NoDrmSchemeExceptionImpl(String detailMessage) {
            super(detailMessage);
        }
    }

    /* loaded from: classes.dex */
    public static final class ProvisioningNetworkErrorExceptionImpl extends MediaPlayer2.ProvisioningNetworkErrorException {
        public synchronized ProvisioningNetworkErrorExceptionImpl(String detailMessage) {
            super(detailMessage);
        }
    }

    /* loaded from: classes.dex */
    public static final class ProvisioningServerErrorExceptionImpl extends MediaPlayer2.ProvisioningServerErrorException {
        public synchronized ProvisioningServerErrorExceptionImpl(String detailMessage) {
            super(detailMessage);
        }
    }

    private synchronized void prepareDrm_createDrmStep(UUID uuid) throws UnsupportedSchemeException {
        Log.v(TAG, "prepareDrm_createDrmStep: UUID: " + uuid);
        try {
            this.mDrmObj = new MediaDrm(uuid);
            Log.v(TAG, "prepareDrm_createDrmStep: Created mDrmObj=" + this.mDrmObj);
        } catch (Exception e) {
            Log.e(TAG, "prepareDrm_createDrmStep: MediaDrm failed with " + e);
            throw e;
        }
    }

    private synchronized void prepareDrm_openSessionStep(UUID uuid) throws NotProvisionedException, ResourceBusyException {
        Log.v(TAG, "prepareDrm_openSessionStep: uuid: " + uuid);
        try {
            this.mDrmSessionId = this.mDrmObj.openSession();
            Log.v(TAG, "prepareDrm_openSessionStep: mDrmSessionId=" + this.mDrmSessionId);
            _prepareDrm(getByteArrayFromUUID(uuid), this.mDrmSessionId);
            Log.v(TAG, "prepareDrm_openSessionStep: _prepareDrm/Crypto succeeded");
        } catch (Exception e) {
            Log.e(TAG, "prepareDrm_openSessionStep: open/crypto failed with " + e);
            throw e;
        }
    }

    private static synchronized boolean setAudioOutputDeviceById(AudioTrack track, int deviceId) {
        if (track == null) {
            return false;
        }
        if (deviceId == 0) {
            track.setPreferredDevice(null);
            return true;
        }
        AudioDeviceInfo[] outputDevices = AudioManager.getDevicesStatic(2);
        for (AudioDeviceInfo device : outputDevices) {
            if (device.getId() == deviceId) {
                track.setPreferredDevice(device);
                return true;
            }
        }
        return false;
    }

    /* loaded from: classes.dex */
    private static class StreamEventCallback extends AudioTrack.StreamEventCallback {
        public long mJAudioTrackPtr;
        public long mNativeCallbackPtr;
        public long mUserDataPtr;

        public synchronized StreamEventCallback(long jAudioTrackPtr, long nativeCallbackPtr, long userDataPtr) {
            this.mJAudioTrackPtr = jAudioTrackPtr;
            this.mNativeCallbackPtr = nativeCallbackPtr;
            this.mUserDataPtr = userDataPtr;
        }

        @Override // android.media.AudioTrack.StreamEventCallback
        public synchronized void onTearDown(AudioTrack track) {
            MediaPlayer2Impl.native_stream_event_onTearDown(this.mNativeCallbackPtr, this.mUserDataPtr);
        }

        @Override // android.media.AudioTrack.StreamEventCallback
        public synchronized void onStreamPresentationEnd(AudioTrack track) {
            MediaPlayer2Impl.native_stream_event_onStreamPresentationEnd(this.mNativeCallbackPtr, this.mUserDataPtr);
        }

        @Override // android.media.AudioTrack.StreamEventCallback
        public synchronized void onStreamDataRequest(AudioTrack track) {
            MediaPlayer2Impl.native_stream_event_onStreamDataRequest(this.mJAudioTrackPtr, this.mNativeCallbackPtr, this.mUserDataPtr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ProvisioningThread extends Thread {
        public static final int TIMEOUT_MS = 60000;
        private Object drmLock;
        private boolean finished;
        private MediaPlayer2Impl mediaPlayer;
        private int status;
        private String urlStr;
        private UUID uuid;

        private ProvisioningThread() {
        }

        public synchronized int status() {
            return this.status;
        }

        public synchronized ProvisioningThread initialize(MediaDrm.ProvisionRequest request, UUID uuid, MediaPlayer2Impl mediaPlayer) {
            this.drmLock = mediaPlayer.mDrmLock;
            this.mediaPlayer = mediaPlayer;
            this.urlStr = request.getDefaultUrl() + "&signedRequest=" + new String(request.getData());
            this.uuid = uuid;
            this.status = 3;
            Log.v(MediaPlayer2Impl.TAG, "HandleProvisioninig: Thread is initialised url: " + this.urlStr);
            return this;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            boolean hasCallback;
            byte[] response = null;
            boolean provisioningSucceeded = false;
            try {
                URL url = new URL(this.urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try {
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(false);
                    connection.setDoInput(true);
                    connection.setConnectTimeout(60000);
                    connection.setReadTimeout(60000);
                    connection.connect();
                    response = Streams.readFully(connection.getInputStream());
                    Log.v(MediaPlayer2Impl.TAG, "HandleProvisioninig: Thread run: response " + response.length + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + response);
                    connection.disconnect();
                } catch (Exception e) {
                    this.status = 1;
                    Log.w(MediaPlayer2Impl.TAG, "HandleProvisioninig: Thread run: connect " + e + " url: " + url);
                    connection.disconnect();
                }
            } catch (Exception e2) {
                this.status = 1;
                Log.w(MediaPlayer2Impl.TAG, "HandleProvisioninig: Thread run: openConnection " + e2);
            }
            if (response != null) {
                try {
                    MediaPlayer2Impl.this.mDrmObj.provideProvisionResponse(response);
                    Log.v(MediaPlayer2Impl.TAG, "HandleProvisioninig: Thread run: provideProvisionResponse SUCCEEDED!");
                    provisioningSucceeded = true;
                } catch (Exception e3) {
                    this.status = 2;
                    Log.w(MediaPlayer2Impl.TAG, "HandleProvisioninig: Thread run: provideProvisionResponse " + e3);
                }
            }
            boolean succeeded = false;
            synchronized (MediaPlayer2Impl.this.mDrmEventCbLock) {
                hasCallback = !MediaPlayer2Impl.this.mDrmEventCallbackRecords.isEmpty();
            }
            int i = 3;
            if (hasCallback) {
                synchronized (this.drmLock) {
                    if (provisioningSucceeded) {
                        try {
                            succeeded = this.mediaPlayer.resumePrepareDrm(this.uuid);
                            if (succeeded) {
                                i = 0;
                            }
                            this.status = i;
                        } finally {
                        }
                    }
                    this.mediaPlayer.mDrmProvisioningInProgress = false;
                    this.mediaPlayer.mPrepareDrmInProgress = false;
                    if (!succeeded) {
                        MediaPlayer2Impl.this.cleanDrmObj();
                    }
                }
                synchronized (MediaPlayer2Impl.this.mDrmEventCbLock) {
                    Iterator it = MediaPlayer2Impl.this.mDrmEventCallbackRecords.iterator();
                    while (it.hasNext()) {
                        final Pair<Executor, MediaPlayer2.DrmEventCallback> cb = (Pair) it.next();
                        ((Executor) cb.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$ProvisioningThread$ghq9Dd9r2O6PXBn2hv4fhVAxaTQ
                            @Override // java.lang.Runnable
                            public final void run() {
                                ((MediaPlayer2.DrmEventCallback) cb.second).onDrmPrepared(r0.mediaPlayer, MediaPlayer2Impl.this.mCurrentDSD, MediaPlayer2Impl.ProvisioningThread.this.status);
                            }
                        });
                    }
                }
            } else {
                if (provisioningSucceeded) {
                    succeeded = this.mediaPlayer.resumePrepareDrm(this.uuid);
                    if (succeeded) {
                        i = 0;
                    }
                    this.status = i;
                }
                this.mediaPlayer.mDrmProvisioningInProgress = false;
                this.mediaPlayer.mPrepareDrmInProgress = false;
                if (!succeeded) {
                    MediaPlayer2Impl.this.cleanDrmObj();
                }
            }
            this.finished = true;
        }
    }

    private synchronized int HandleProvisioninig(UUID uuid) {
        boolean hasCallback;
        if (this.mDrmProvisioningInProgress) {
            Log.e(TAG, "HandleProvisioninig: Unexpected mDrmProvisioningInProgress");
            return 3;
        }
        MediaDrm.ProvisionRequest provReq = this.mDrmObj.getProvisionRequest();
        if (provReq == null) {
            Log.e(TAG, "HandleProvisioninig: getProvisionRequest returned null.");
            return 3;
        }
        Log.v(TAG, "HandleProvisioninig provReq  data: " + provReq.getData() + " url: " + provReq.getDefaultUrl());
        this.mDrmProvisioningInProgress = true;
        this.mDrmProvisioningThread = new ProvisioningThread().initialize(provReq, uuid, this);
        this.mDrmProvisioningThread.start();
        synchronized (this.mDrmEventCbLock) {
            hasCallback = true ^ this.mDrmEventCallbackRecords.isEmpty();
        }
        if (hasCallback) {
            return 0;
        }
        try {
            this.mDrmProvisioningThread.join();
        } catch (Exception e) {
            Log.w(TAG, "HandleProvisioninig: Thread.join Exception " + e);
        }
        int result = this.mDrmProvisioningThread.status();
        this.mDrmProvisioningThread = null;
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean resumePrepareDrm(UUID uuid) {
        Log.v(TAG, "resumePrepareDrm: uuid: " + uuid);
        try {
            prepareDrm_openSessionStep(uuid);
            this.mDrmUUID = uuid;
            this.mActiveDrmScheme = true;
            return true;
        } catch (Exception e) {
            Log.w(TAG, "HandleProvisioninig: Thread run _prepareDrm resume failed with " + e);
            return false;
        }
    }

    private synchronized void resetDrmState() {
        synchronized (this.mDrmLock) {
            Log.v(TAG, "resetDrmState:  mDrmInfoImpl=" + this.mDrmInfoImpl + " mDrmProvisioningThread=" + this.mDrmProvisioningThread + " mPrepareDrmInProgress=" + this.mPrepareDrmInProgress + " mActiveDrmScheme=" + this.mActiveDrmScheme);
            this.mDrmInfoResolved = false;
            this.mDrmInfoImpl = null;
            if (this.mDrmProvisioningThread != null) {
                try {
                    this.mDrmProvisioningThread.join();
                } catch (InterruptedException e) {
                    Log.w(TAG, "resetDrmState: ProvThread.join Exception " + e);
                }
                this.mDrmProvisioningThread = null;
            }
            this.mPrepareDrmInProgress = false;
            this.mActiveDrmScheme = false;
            cleanDrmObj();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void cleanDrmObj() {
        Log.v(TAG, "cleanDrmObj: mDrmObj=" + this.mDrmObj + " mDrmSessionId=" + this.mDrmSessionId);
        if (this.mDrmSessionId != null) {
            this.mDrmObj.closeSession(this.mDrmSessionId);
            this.mDrmSessionId = null;
        }
        if (this.mDrmObj != null) {
            this.mDrmObj.release();
            this.mDrmObj = null;
        }
    }

    private static final synchronized byte[] getByteArrayFromUUID(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] uuidBytes = new byte[16];
        for (int i = 0; i < 8; i++) {
            uuidBytes[i] = (byte) (msb >>> ((7 - i) * 8));
            uuidBytes[8 + i] = (byte) (lsb >>> (8 * (7 - i)));
        }
        return uuidBytes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isVideoScalingModeSupported(int mode) {
        return mode == 1 || mode == 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class TimeProvider implements MediaTimeProvider {
        private static final long MAX_EARLY_CALLBACK_US = 1000;
        private static final long MAX_NS_WITHOUT_POSITION_CHECK = 5000000000L;
        private static final int NOTIFY = 1;
        private static final int NOTIFY_SEEK = 3;
        private static final int NOTIFY_STOP = 2;
        private static final int NOTIFY_TIME = 0;
        private static final int NOTIFY_TRACK_DATA = 4;
        private static final String TAG = "MTP";
        private static final long TIME_ADJUSTMENT_RATE = 2;
        private boolean mBuffering;
        private EventHandler mEventHandler;
        private HandlerThread mHandlerThread;
        private long mLastReportedTime;
        private long mLastTimeUs;
        private MediaTimeProvider.OnMediaTimeListener[] mListeners;
        private MediaPlayer2Impl mPlayer;
        private boolean mRefresh;
        private long[] mTimes;
        private boolean mPaused = true;
        private boolean mStopped = true;
        private boolean mPausing = false;
        private boolean mSeeking = false;
        public boolean DEBUG = false;

        public synchronized TimeProvider(MediaPlayer2Impl mp) {
            this.mLastTimeUs = 0L;
            this.mRefresh = false;
            this.mPlayer = mp;
            try {
                getCurrentTimeUs(true, false);
            } catch (IllegalStateException e) {
                this.mRefresh = true;
            }
            Looper myLooper = Looper.myLooper();
            Looper looper = myLooper;
            if (myLooper == null) {
                Looper mainLooper = Looper.getMainLooper();
                looper = mainLooper;
                if (mainLooper == null) {
                    this.mHandlerThread = new HandlerThread("MediaPlayer2MTPEventThread", -2);
                    this.mHandlerThread.start();
                    looper = this.mHandlerThread.getLooper();
                }
            }
            this.mEventHandler = new EventHandler(looper);
            this.mListeners = new MediaTimeProvider.OnMediaTimeListener[0];
            this.mTimes = new long[0];
            this.mLastTimeUs = 0L;
        }

        private synchronized void scheduleNotification(int type, long delayUs) {
            if (this.mSeeking && type == 0) {
                return;
            }
            if (this.DEBUG) {
                Log.v(TAG, "scheduleNotification " + type + " in " + delayUs);
            }
            this.mEventHandler.removeMessages(1);
            Message msg = this.mEventHandler.obtainMessage(1, type, 0);
            this.mEventHandler.sendMessageDelayed(msg, (int) (delayUs / 1000));
        }

        public synchronized void close() {
            this.mEventHandler.removeMessages(1);
            if (this.mHandlerThread != null) {
                this.mHandlerThread.quitSafely();
                this.mHandlerThread = null;
            }
        }

        protected void finalize() {
            if (this.mHandlerThread != null) {
                this.mHandlerThread.quitSafely();
            }
        }

        public synchronized void onNotifyTime() {
            if (this.DEBUG) {
                Log.d(TAG, "onNotifyTime: ");
            }
            scheduleNotification(0, 0L);
        }

        public synchronized void onPaused(boolean paused) {
            if (this.DEBUG) {
                Log.d(TAG, "onPaused: " + paused);
            }
            if (this.mStopped) {
                this.mStopped = false;
                this.mSeeking = true;
                scheduleNotification(3, 0L);
            } else {
                this.mPausing = paused;
                this.mSeeking = false;
                scheduleNotification(0, 0L);
            }
        }

        public synchronized void onBuffering(boolean buffering) {
            if (this.DEBUG) {
                Log.d(TAG, "onBuffering: " + buffering);
            }
            this.mBuffering = buffering;
            scheduleNotification(0, 0L);
        }

        public synchronized void onStopped() {
            if (this.DEBUG) {
                Log.d(TAG, "onStopped");
            }
            this.mPaused = true;
            this.mStopped = true;
            this.mSeeking = false;
            this.mBuffering = false;
            scheduleNotification(2, 0L);
        }

        public synchronized void onSeekComplete(MediaPlayer2Impl mp) {
            this.mStopped = false;
            this.mSeeking = true;
            scheduleNotification(3, 0L);
        }

        public synchronized void onNewPlayer() {
            if (this.mRefresh) {
                synchronized (this) {
                    this.mStopped = false;
                    this.mSeeking = true;
                    this.mBuffering = false;
                    scheduleNotification(3, 0L);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void notifySeek() {
            MediaTimeProvider.OnMediaTimeListener[] onMediaTimeListenerArr;
            this.mSeeking = false;
            try {
                long timeUs = getCurrentTimeUs(true, false);
                if (this.DEBUG) {
                    Log.d(TAG, "onSeekComplete at " + timeUs);
                }
                for (MediaTimeProvider.OnMediaTimeListener listener : this.mListeners) {
                    if (listener == null) {
                        break;
                    }
                    listener.onSeek(timeUs);
                }
            } catch (IllegalStateException e) {
                if (this.DEBUG) {
                    Log.d(TAG, "onSeekComplete but no player");
                }
                this.mPausing = true;
                notifyTimedEvent(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void notifyTrackData(Pair<SubtitleTrack, byte[]> trackData) {
            SubtitleTrack track = trackData.first;
            byte[] data = trackData.second;
            track.onData(data, true, -1L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void notifyStop() {
            MediaTimeProvider.OnMediaTimeListener[] onMediaTimeListenerArr;
            for (MediaTimeProvider.OnMediaTimeListener listener : this.mListeners) {
                if (listener == null) {
                    break;
                }
                listener.onStop();
            }
        }

        private synchronized int registerListener(MediaTimeProvider.OnMediaTimeListener listener) {
            int i = 0;
            while (i < this.mListeners.length && this.mListeners[i] != listener && this.mListeners[i] != null) {
                i++;
            }
            if (i >= this.mListeners.length) {
                MediaTimeProvider.OnMediaTimeListener[] newListeners = new MediaTimeProvider.OnMediaTimeListener[i + 1];
                long[] newTimes = new long[i + 1];
                System.arraycopy(this.mListeners, 0, newListeners, 0, this.mListeners.length);
                System.arraycopy(this.mTimes, 0, newTimes, 0, this.mTimes.length);
                this.mListeners = newListeners;
                this.mTimes = newTimes;
            }
            if (this.mListeners[i] == null) {
                this.mListeners[i] = listener;
                this.mTimes[i] = -1;
            }
            return i;
        }

        @Override // android.media.MediaTimeProvider
        public synchronized void notifyAt(long timeUs, MediaTimeProvider.OnMediaTimeListener listener) {
            if (this.DEBUG) {
                Log.d(TAG, "notifyAt " + timeUs);
            }
            this.mTimes[registerListener(listener)] = timeUs;
            scheduleNotification(0, 0L);
        }

        @Override // android.media.MediaTimeProvider
        public synchronized void scheduleUpdate(MediaTimeProvider.OnMediaTimeListener listener) {
            if (this.DEBUG) {
                Log.d(TAG, "scheduleUpdate");
            }
            int i = registerListener(listener);
            if (!this.mStopped) {
                this.mTimes[i] = 0;
                scheduleNotification(0, 0L);
            }
        }

        @Override // android.media.MediaTimeProvider
        public synchronized void cancelNotifications(MediaTimeProvider.OnMediaTimeListener listener) {
            int i = 0;
            while (true) {
                if (i >= this.mListeners.length) {
                    break;
                } else if (this.mListeners[i] == listener) {
                    System.arraycopy(this.mListeners, i + 1, this.mListeners, i, (this.mListeners.length - i) - 1);
                    System.arraycopy(this.mTimes, i + 1, this.mTimes, i, (this.mTimes.length - i) - 1);
                    this.mListeners[this.mListeners.length - 1] = null;
                    this.mTimes[this.mTimes.length - 1] = -1;
                    break;
                } else if (this.mListeners[i] == null) {
                    break;
                } else {
                    i++;
                }
            }
            scheduleNotification(0, 0L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void notifyTimedEvent(boolean refreshTime) {
            long nowUs;
            long nowUs2;
            long nowUs3;
            try {
                nowUs = getCurrentTimeUs(refreshTime, true);
            } catch (IllegalStateException e) {
                this.mRefresh = true;
                this.mPausing = true;
                nowUs = getCurrentTimeUs(refreshTime, true);
            }
            long nextTimeUs = nowUs;
            if (this.mSeeking) {
                return;
            }
            int ix = 0;
            if (this.DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("notifyTimedEvent(");
                sb.append(this.mLastTimeUs);
                sb.append(" -> ");
                sb.append(nowUs);
                sb.append(") from {");
                long[] jArr = this.mTimes;
                int length = jArr.length;
                boolean first = true;
                int i = 0;
                while (i < length) {
                    long time = jArr[i];
                    long nowUs4 = nowUs;
                    if (time != -1) {
                        if (!first) {
                            sb.append(", ");
                        }
                        sb.append(time);
                        first = false;
                    }
                    i++;
                    nowUs = nowUs4;
                }
                nowUs2 = nowUs;
                sb.append("}");
                Log.d(TAG, sb.toString());
            } else {
                nowUs2 = nowUs;
            }
            Vector<MediaTimeProvider.OnMediaTimeListener> activatedListeners = new Vector<>();
            while (true) {
                int ix2 = ix;
                if (ix2 >= this.mTimes.length || this.mListeners[ix2] == null) {
                    break;
                }
                if (this.mTimes[ix2] > -1) {
                    if (this.mTimes[ix2] <= nowUs2 + 1000) {
                        activatedListeners.add(this.mListeners[ix2]);
                        if (this.DEBUG) {
                            Log.d(TAG, Environment.MEDIA_REMOVED);
                        }
                        this.mTimes[ix2] = -1;
                    } else if (nextTimeUs == nowUs2 || this.mTimes[ix2] < nextTimeUs) {
                        long nextTimeUs2 = this.mTimes[ix2];
                        nextTimeUs = nextTimeUs2;
                    }
                }
                ix = ix2 + 1;
            }
            if (nextTimeUs <= nowUs2 || this.mPaused) {
                nowUs3 = nowUs2;
                this.mEventHandler.removeMessages(1);
            } else {
                if (this.DEBUG) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("scheduling for ");
                    sb2.append(nextTimeUs);
                    sb2.append(" and ");
                    nowUs3 = nowUs2;
                    sb2.append(nowUs3);
                    Log.d(TAG, sb2.toString());
                } else {
                    nowUs3 = nowUs2;
                }
                this.mPlayer.notifyAt(nextTimeUs);
            }
            Iterator<MediaTimeProvider.OnMediaTimeListener> it = activatedListeners.iterator();
            while (it.hasNext()) {
                MediaTimeProvider.OnMediaTimeListener listener = it.next();
                listener.onTimedEvent(nowUs3);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x002e A[Catch: IllegalStateException -> 0x007e, all -> 0x00b6, TryCatch #0 {IllegalStateException -> 0x007e, blocks: (B:10:0x000d, B:12:0x0020, B:17:0x0028, B:19:0x002e, B:23:0x0040), top: B:53:0x000d, outer: #1 }] */
        @Override // android.media.MediaTimeProvider
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public synchronized long getCurrentTimeUs(boolean r8, boolean r9) throws java.lang.IllegalStateException {
            /*
                r7 = this;
                monitor-enter(r7)
                boolean r0 = r7.mPaused     // Catch: java.lang.Throwable -> Lb6
                if (r0 == 0) goto Lb
                if (r8 != 0) goto Lb
                long r0 = r7.mLastReportedTime     // Catch: java.lang.Throwable -> Lb6
                monitor-exit(r7)     // Catch: java.lang.Throwable -> Lb6
                return r0
            Lb:
                r0 = 1
                r1 = 0
                android.media.MediaPlayer2Impl r2 = r7.mPlayer     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                long r2 = r2.getCurrentPosition()     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                r4 = 1000(0x3e8, double:4.94E-321)
                long r2 = r2 * r4
                r7.mLastTimeUs = r2     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                android.media.MediaPlayer2Impl r2 = r7.mPlayer     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                boolean r2 = r2.isPlaying()     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                if (r2 == 0) goto L27
                boolean r2 = r7.mBuffering     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                if (r2 == 0) goto L25
                goto L27
            L25:
                r2 = r1
                goto L28
            L27:
                r2 = r0
            L28:
                r7.mPaused = r2     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                boolean r2 = r7.DEBUG     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                if (r2 == 0) goto L54
                java.lang.String r2 = "MTP"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                r3.<init>()     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                boolean r4 = r7.mPaused     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                if (r4 == 0) goto L3d
                java.lang.String r4 = "paused"
                goto L40
            L3d:
                java.lang.String r4 = "playing"
            L40:
                r3.append(r4)     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                java.lang.String r4 = " at "
                r3.append(r4)     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                long r4 = r7.mLastTimeUs     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                r3.append(r4)     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                java.lang.String r3 = r3.toString()     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
                android.util.Log.v(r2, r3)     // Catch: java.lang.IllegalStateException -> L7e java.lang.Throwable -> Lb6
            L54:
                if (r9 == 0) goto L76
                long r2 = r7.mLastTimeUs     // Catch: java.lang.Throwable -> Lb6
                long r4 = r7.mLastReportedTime     // Catch: java.lang.Throwable -> Lb6
                int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r2 >= 0) goto L76
                long r2 = r7.mLastReportedTime     // Catch: java.lang.Throwable -> Lb6
                long r4 = r7.mLastTimeUs     // Catch: java.lang.Throwable -> Lb6
                long r2 = r2 - r4
                r4 = 1000000(0xf4240, double:4.940656E-318)
                int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r2 <= 0) goto L7a
                r7.mStopped = r1     // Catch: java.lang.Throwable -> Lb6
                r7.mSeeking = r0     // Catch: java.lang.Throwable -> Lb6
                r0 = 3
                r1 = 0
                r7.scheduleNotification(r0, r1)     // Catch: java.lang.Throwable -> Lb6
                goto L7a
            L76:
                long r0 = r7.mLastTimeUs     // Catch: java.lang.Throwable -> Lb6
                r7.mLastReportedTime = r0     // Catch: java.lang.Throwable -> Lb6
            L7a:
                long r0 = r7.mLastReportedTime     // Catch: java.lang.Throwable -> Lb6
                monitor-exit(r7)     // Catch: java.lang.Throwable -> Lb6
                return r0
            L7e:
                r2 = move-exception
                boolean r3 = r7.mPausing     // Catch: java.lang.Throwable -> Lb6
                if (r3 == 0) goto Lb5
                r7.mPausing = r1     // Catch: java.lang.Throwable -> Lb6
                if (r9 == 0) goto L8f
                long r3 = r7.mLastReportedTime     // Catch: java.lang.Throwable -> Lb6
                long r5 = r7.mLastTimeUs     // Catch: java.lang.Throwable -> Lb6
                int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r1 >= 0) goto L93
            L8f:
                long r3 = r7.mLastTimeUs     // Catch: java.lang.Throwable -> Lb6
                r7.mLastReportedTime = r3     // Catch: java.lang.Throwable -> Lb6
            L93:
                r7.mPaused = r0     // Catch: java.lang.Throwable -> Lb6
                boolean r0 = r7.DEBUG     // Catch: java.lang.Throwable -> Lb6
                if (r0 == 0) goto Lb1
                java.lang.String r0 = "MTP"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lb6
                r1.<init>()     // Catch: java.lang.Throwable -> Lb6
                java.lang.String r3 = "illegal state, but pausing: estimating at "
                r1.append(r3)     // Catch: java.lang.Throwable -> Lb6
                long r3 = r7.mLastReportedTime     // Catch: java.lang.Throwable -> Lb6
                r1.append(r3)     // Catch: java.lang.Throwable -> Lb6
                java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> Lb6
                android.util.Log.d(r0, r1)     // Catch: java.lang.Throwable -> Lb6
            Lb1:
                long r0 = r7.mLastReportedTime     // Catch: java.lang.Throwable -> Lb6
                monitor-exit(r7)     // Catch: java.lang.Throwable -> Lb6
                return r0
            Lb5:
                throw r2     // Catch: java.lang.Throwable -> Lb6
            Lb6:
                r0 = move-exception
                monitor-exit(r7)     // Catch: java.lang.Throwable -> Lb6
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer2Impl.TimeProvider.getCurrentTimeUs(boolean, boolean):long");
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public class EventHandler extends Handler {
            public EventHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    int i = msg.arg1;
                    if (i != 0) {
                        switch (i) {
                            case 2:
                                TimeProvider.this.notifyStop();
                                return;
                            case 3:
                                TimeProvider.this.notifySeek();
                                return;
                            case 4:
                                TimeProvider.this.notifyTrackData((Pair) msg.obj);
                                return;
                            default:
                                return;
                        }
                    }
                    TimeProvider.this.notifyTimedEvent(true);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public abstract class Task implements Runnable {
        private DataSourceDesc mDSD;
        private final int mMediaCallType;
        private final boolean mNeedToWaitForEventToComplete;

        abstract synchronized void process() throws IOException, MediaPlayer2.NoDrmSchemeException;

        public Task(int mediaCallType, boolean needToWaitForEventToComplete) {
            this.mMediaCallType = mediaCallType;
            this.mNeedToWaitForEventToComplete = needToWaitForEventToComplete;
        }

        @Override // java.lang.Runnable
        public void run() {
            int status = 0;
            try {
                process();
            } catch (MediaPlayer2.NoDrmSchemeException e) {
                status = 5;
            } catch (IOException e2) {
                status = 4;
            } catch (IllegalArgumentException e3) {
                status = 2;
            } catch (IllegalStateException e4) {
                status = 1;
            } catch (SecurityException e5) {
                status = 3;
            } catch (Exception e6) {
                status = Integer.MIN_VALUE;
            }
            synchronized (MediaPlayer2Impl.this.mSrcLock) {
                this.mDSD = MediaPlayer2Impl.this.mCurrentDSD;
            }
            if (!this.mNeedToWaitForEventToComplete || status != 0) {
                sendCompleteNotification(status);
                synchronized (MediaPlayer2Impl.this.mTaskLock) {
                    MediaPlayer2Impl.this.mCurrentTask = null;
                    MediaPlayer2Impl.this.processPendingTask_l();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void sendCompleteNotification(final int status) {
            if (this.mMediaCallType != 1003) {
                synchronized (MediaPlayer2Impl.this.mEventCbLock) {
                    Iterator it = MediaPlayer2Impl.this.mEventCallbackRecords.iterator();
                    while (it.hasNext()) {
                        final Pair<Executor, MediaPlayer2.MediaPlayer2EventCallback> cb = (Pair) it.next();
                        ((Executor) cb.first).execute(new Runnable() { // from class: android.media.-$$Lambda$MediaPlayer2Impl$Task$FRvdJ9PUPHSq0Jucj91aL6zYEJY
                            @Override // java.lang.Runnable
                            public final void run() {
                                MediaPlayer2Impl.Task task = MediaPlayer2Impl.Task.this;
                                ((MediaPlayer2.MediaPlayer2EventCallback) cb.second).onCallCompleted(MediaPlayer2Impl.this, task.mDSD, task.mMediaCallType, status);
                            }
                        });
                    }
                }
            }
        }
    }
}
