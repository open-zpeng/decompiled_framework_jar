package android.media;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Canvas;
import android.media.MediaTimeProvider;
import android.os.Handler;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

/* loaded from: classes2.dex */
public abstract class SubtitleTrack implements MediaTimeProvider.OnMediaTimeListener {
    private static final String TAG = "SubtitleTrack";
    private MediaFormat mFormat;
    private long mLastTimeMs;
    private long mLastUpdateTimeMs;
    private Runnable mRunnable;
    protected MediaTimeProvider mTimeProvider;
    protected boolean mVisible;
    protected final LongSparseArray<Run> mRunsByEndTime = new LongSparseArray<>();
    protected final LongSparseArray<Run> mRunsByID = new LongSparseArray<>();
    protected final Vector<Cue> mActiveCues = new Vector<>();
    public boolean DEBUG = false;
    protected Handler mHandler = new Handler();
    private long mNextScheduledTimeMs = -1;
    protected CueList mCues = new CueList();

    /* loaded from: classes2.dex */
    public interface RenderingWidget {

        /* loaded from: classes2.dex */
        public interface OnChangedListener {
            void onChanged(RenderingWidget renderingWidget);
        }

        @UnsupportedAppUsage
        void draw(Canvas canvas);

        @UnsupportedAppUsage
        void onAttachedToWindow();

        @UnsupportedAppUsage
        void onDetachedFromWindow();

        @UnsupportedAppUsage
        void setOnChangedListener(OnChangedListener onChangedListener);

        @UnsupportedAppUsage
        void setSize(int i, int i2);

        void setVisible(boolean z);
    }

    public abstract RenderingWidget getRenderingWidget();

    public abstract void onData(byte[] bArr, boolean z, long j);

    public abstract void updateView(Vector<Cue> vector);

    public SubtitleTrack(MediaFormat format) {
        this.mFormat = format;
        clearActiveCues();
        this.mLastTimeMs = -1L;
    }

    public final MediaFormat getFormat() {
        return this.mFormat;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onData(SubtitleData data) {
        long runID = data.getStartTimeUs() + 1;
        onData(data.getData(), true, runID);
        setRunDiscardTimeMs(runID, (data.getStartTimeUs() + data.getDurationUs()) / 1000);
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0007, code lost:
        if (r7.mLastUpdateTimeMs > r9) goto L3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected synchronized void updateActiveCues(boolean r8, long r9) {
        /*
            r7 = this;
            monitor-enter(r7)
            if (r8 != 0) goto L9
            long r0 = r7.mLastUpdateTimeMs     // Catch: java.lang.Throwable -> Lba
            int r0 = (r0 > r9 ? 1 : (r0 == r9 ? 0 : -1))
            if (r0 <= 0) goto Lc
        L9:
            r7.clearActiveCues()     // Catch: java.lang.Throwable -> Lba
        Lc:
            android.media.SubtitleTrack$CueList r0 = r7.mCues     // Catch: java.lang.Throwable -> Lba
            long r1 = r7.mLastUpdateTimeMs     // Catch: java.lang.Throwable -> Lba
            java.lang.Iterable r0 = r0.entriesBetween(r1, r9)     // Catch: java.lang.Throwable -> Lba
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> Lba
        L18:
            boolean r1 = r0.hasNext()     // Catch: java.lang.Throwable -> Lba
            if (r1 == 0) goto L9f
            java.lang.Object r1 = r0.next()     // Catch: java.lang.Throwable -> Lba
            android.util.Pair r1 = (android.util.Pair) r1     // Catch: java.lang.Throwable -> Lba
            S r2 = r1.second     // Catch: java.lang.Throwable -> Lba
            android.media.SubtitleTrack$Cue r2 = (android.media.SubtitleTrack.Cue) r2     // Catch: java.lang.Throwable -> Lba
            long r3 = r2.mEndTimeMs     // Catch: java.lang.Throwable -> Lba
            F r5 = r1.first     // Catch: java.lang.Throwable -> Lba
            java.lang.Long r5 = (java.lang.Long) r5     // Catch: java.lang.Throwable -> Lba
            long r5 = r5.longValue()     // Catch: java.lang.Throwable -> Lba
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 != 0) goto L61
            boolean r3 = r7.DEBUG     // Catch: java.lang.Throwable -> Lba
            if (r3 == 0) goto L50
            java.lang.String r3 = "SubtitleTrack"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lba
            r4.<init>()     // Catch: java.lang.Throwable -> Lba
            java.lang.String r5 = "Removing "
            r4.append(r5)     // Catch: java.lang.Throwable -> Lba
            r4.append(r2)     // Catch: java.lang.Throwable -> Lba
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> Lba
            android.util.Log.v(r3, r4)     // Catch: java.lang.Throwable -> Lba
        L50:
            java.util.Vector<android.media.SubtitleTrack$Cue> r3 = r7.mActiveCues     // Catch: java.lang.Throwable -> Lba
            r3.remove(r2)     // Catch: java.lang.Throwable -> Lba
            long r3 = r2.mRunID     // Catch: java.lang.Throwable -> Lba
            r5 = 0
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 != 0) goto L9d
            r0.remove()     // Catch: java.lang.Throwable -> Lba
            goto L9d
        L61:
            long r3 = r2.mStartTimeMs     // Catch: java.lang.Throwable -> Lba
            F r5 = r1.first     // Catch: java.lang.Throwable -> Lba
            java.lang.Long r5 = (java.lang.Long) r5     // Catch: java.lang.Throwable -> Lba
            long r5 = r5.longValue()     // Catch: java.lang.Throwable -> Lba
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 != 0) goto L96
            boolean r3 = r7.DEBUG     // Catch: java.lang.Throwable -> Lba
            if (r3 == 0) goto L89
            java.lang.String r3 = "SubtitleTrack"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lba
            r4.<init>()     // Catch: java.lang.Throwable -> Lba
            java.lang.String r5 = "Adding "
            r4.append(r5)     // Catch: java.lang.Throwable -> Lba
            r4.append(r2)     // Catch: java.lang.Throwable -> Lba
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> Lba
            android.util.Log.v(r3, r4)     // Catch: java.lang.Throwable -> Lba
        L89:
            long[] r3 = r2.mInnerTimesMs     // Catch: java.lang.Throwable -> Lba
            if (r3 == 0) goto L90
            r2.onTime(r9)     // Catch: java.lang.Throwable -> Lba
        L90:
            java.util.Vector<android.media.SubtitleTrack$Cue> r3 = r7.mActiveCues     // Catch: java.lang.Throwable -> Lba
            r3.add(r2)     // Catch: java.lang.Throwable -> Lba
            goto L9d
        L96:
            long[] r3 = r2.mInnerTimesMs     // Catch: java.lang.Throwable -> Lba
            if (r3 == 0) goto L9d
            r2.onTime(r9)     // Catch: java.lang.Throwable -> Lba
        L9d:
            goto L18
        L9f:
            android.util.LongSparseArray<android.media.SubtitleTrack$Run> r0 = r7.mRunsByEndTime     // Catch: java.lang.Throwable -> Lba
            int r0 = r0.size()     // Catch: java.lang.Throwable -> Lba
            if (r0 <= 0) goto Lb6
            android.util.LongSparseArray<android.media.SubtitleTrack$Run> r0 = r7.mRunsByEndTime     // Catch: java.lang.Throwable -> Lba
            r1 = 0
            long r2 = r0.keyAt(r1)     // Catch: java.lang.Throwable -> Lba
            int r0 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1))
            if (r0 > 0) goto Lb6
            r7.removeRunsByEndTimeIndex(r1)     // Catch: java.lang.Throwable -> Lba
            goto L9f
        Lb6:
            r7.mLastUpdateTimeMs = r9     // Catch: java.lang.Throwable -> Lba
            monitor-exit(r7)
            return
        Lba:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.SubtitleTrack.updateActiveCues(boolean, long):void");
    }

    private void removeRunsByEndTimeIndex(int ix) {
        Run run = this.mRunsByEndTime.valueAt(ix);
        while (run != null) {
            Cue cue = run.mFirstCue;
            while (cue != null) {
                this.mCues.remove(cue);
                Cue nextCue = cue.mNextInRun;
                cue.mNextInRun = null;
                cue = nextCue;
            }
            this.mRunsByID.remove(run.mRunID);
            Run nextRun = run.mNextRunAtEndTimeMs;
            run.mPrevRunAtEndTimeMs = null;
            run.mNextRunAtEndTimeMs = null;
            run = nextRun;
        }
        this.mRunsByEndTime.removeAt(ix);
    }

    protected void finalize() throws Throwable {
        int size = this.mRunsByEndTime.size();
        for (int ix = size - 1; ix >= 0; ix--) {
            removeRunsByEndTimeIndex(ix);
        }
        super.finalize();
    }

    private synchronized void takeTime(long timeMs) {
        this.mLastTimeMs = timeMs;
    }

    protected synchronized void clearActiveCues() {
        if (this.DEBUG) {
            Log.v(TAG, "Clearing " + this.mActiveCues.size() + " active cues");
        }
        this.mActiveCues.clear();
        this.mLastUpdateTimeMs = -1L;
    }

    protected void scheduleTimedEvents() {
        if (this.mTimeProvider != null) {
            this.mNextScheduledTimeMs = this.mCues.nextTimeAfter(this.mLastTimeMs);
            if (this.DEBUG) {
                Log.d(TAG, "sched @" + this.mNextScheduledTimeMs + " after " + this.mLastTimeMs);
            }
            MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
            long j = this.mNextScheduledTimeMs;
            mediaTimeProvider.notifyAt(j >= 0 ? j * 1000 : -1L, this);
        }
    }

    @Override // android.media.MediaTimeProvider.OnMediaTimeListener
    public void onTimedEvent(long timeUs) {
        if (this.DEBUG) {
            Log.d(TAG, "onTimedEvent " + timeUs);
        }
        synchronized (this) {
            long timeMs = timeUs / 1000;
            updateActiveCues(false, timeMs);
            takeTime(timeMs);
        }
        updateView(this.mActiveCues);
        scheduleTimedEvents();
    }

    @Override // android.media.MediaTimeProvider.OnMediaTimeListener
    public void onSeek(long timeUs) {
        if (this.DEBUG) {
            Log.d(TAG, "onSeek " + timeUs);
        }
        synchronized (this) {
            long timeMs = timeUs / 1000;
            updateActiveCues(true, timeMs);
            takeTime(timeMs);
        }
        updateView(this.mActiveCues);
        scheduleTimedEvents();
    }

    @Override // android.media.MediaTimeProvider.OnMediaTimeListener
    public void onStop() {
        synchronized (this) {
            if (this.DEBUG) {
                Log.d(TAG, "onStop");
            }
            clearActiveCues();
            this.mLastTimeMs = -1L;
        }
        updateView(this.mActiveCues);
        this.mNextScheduledTimeMs = -1L;
        MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
        if (mediaTimeProvider != null) {
            mediaTimeProvider.notifyAt(-1L, this);
        }
    }

    public void show() {
        if (this.mVisible) {
            return;
        }
        this.mVisible = true;
        RenderingWidget renderingWidget = getRenderingWidget();
        if (renderingWidget != null) {
            renderingWidget.setVisible(true);
        }
        MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
        if (mediaTimeProvider != null) {
            mediaTimeProvider.scheduleUpdate(this);
        }
    }

    public void hide() {
        if (!this.mVisible) {
            return;
        }
        MediaTimeProvider mediaTimeProvider = this.mTimeProvider;
        if (mediaTimeProvider != null) {
            mediaTimeProvider.cancelNotifications(this);
        }
        RenderingWidget renderingWidget = getRenderingWidget();
        if (renderingWidget != null) {
            renderingWidget.setVisible(false);
        }
        this.mVisible = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized boolean addCue(Cue cue) {
        this.mCues.add(cue);
        if (cue.mRunID != 0) {
            Run run = this.mRunsByID.get(cue.mRunID);
            if (run == null) {
                run = new Run();
                this.mRunsByID.put(cue.mRunID, run);
                run.mEndTimeMs = cue.mEndTimeMs;
            } else if (run.mEndTimeMs < cue.mEndTimeMs) {
                run.mEndTimeMs = cue.mEndTimeMs;
            }
            cue.mNextInRun = run.mFirstCue;
            run.mFirstCue = cue;
        }
        long nowMs = -1;
        if (this.mTimeProvider != null) {
            try {
                nowMs = this.mTimeProvider.getCurrentTimeUs(false, true) / 1000;
            } catch (IllegalStateException e) {
            }
        }
        if (this.DEBUG) {
            Log.v(TAG, "mVisible=" + this.mVisible + ", " + cue.mStartTimeMs + " <= " + nowMs + ", " + cue.mEndTimeMs + " >= " + this.mLastTimeMs);
        }
        if (this.mVisible && cue.mStartTimeMs <= nowMs && cue.mEndTimeMs >= this.mLastTimeMs) {
            if (this.mRunnable != null) {
                this.mHandler.removeCallbacks(this.mRunnable);
            }
            final long thenMs = nowMs;
            this.mRunnable = new Runnable() { // from class: android.media.SubtitleTrack.1
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (track) {
                        SubtitleTrack.this.mRunnable = null;
                        SubtitleTrack.this.updateActiveCues(true, thenMs);
                        SubtitleTrack.this.updateView(SubtitleTrack.this.mActiveCues);
                    }
                }
            };
            if (this.mHandler.postDelayed(this.mRunnable, 10L)) {
                if (this.DEBUG) {
                    Log.v(TAG, "scheduling update");
                }
            } else if (this.DEBUG) {
                Log.w(TAG, "failed to schedule subtitle view update");
            }
            return true;
        }
        if (this.mVisible && cue.mEndTimeMs >= this.mLastTimeMs && (cue.mStartTimeMs < this.mNextScheduledTimeMs || this.mNextScheduledTimeMs < 0)) {
            scheduleTimedEvents();
        }
        return false;
    }

    public synchronized void setTimeProvider(MediaTimeProvider timeProvider) {
        if (this.mTimeProvider == timeProvider) {
            return;
        }
        if (this.mTimeProvider != null) {
            this.mTimeProvider.cancelNotifications(this);
        }
        this.mTimeProvider = timeProvider;
        if (this.mTimeProvider != null) {
            this.mTimeProvider.scheduleUpdate(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class CueList {
        private static final String TAG = "CueList";
        public boolean DEBUG = false;
        private SortedMap<Long, Vector<Cue>> mCues = new TreeMap();

        private boolean addEvent(Cue cue, long timeMs) {
            Vector<Cue> cues = this.mCues.get(Long.valueOf(timeMs));
            if (cues == null) {
                cues = new Vector<>(2);
                this.mCues.put(Long.valueOf(timeMs), cues);
            } else if (cues.contains(cue)) {
                return false;
            }
            cues.add(cue);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeEvent(Cue cue, long timeMs) {
            Vector<Cue> cues = this.mCues.get(Long.valueOf(timeMs));
            if (cues != null) {
                cues.remove(cue);
                if (cues.size() == 0) {
                    this.mCues.remove(Long.valueOf(timeMs));
                }
            }
        }

        public void add(Cue cue) {
            long[] jArr;
            if (cue.mStartTimeMs >= cue.mEndTimeMs || !addEvent(cue, cue.mStartTimeMs)) {
                return;
            }
            long lastTimeMs = cue.mStartTimeMs;
            if (cue.mInnerTimesMs != null) {
                for (long timeMs : cue.mInnerTimesMs) {
                    if (timeMs > lastTimeMs && timeMs < cue.mEndTimeMs) {
                        addEvent(cue, timeMs);
                        lastTimeMs = timeMs;
                    }
                }
            }
            addEvent(cue, cue.mEndTimeMs);
        }

        public void remove(Cue cue) {
            long[] jArr;
            removeEvent(cue, cue.mStartTimeMs);
            if (cue.mInnerTimesMs != null) {
                for (long timeMs : cue.mInnerTimesMs) {
                    removeEvent(cue, timeMs);
                }
            }
            removeEvent(cue, cue.mEndTimeMs);
        }

        public Iterable<Pair<Long, Cue>> entriesBetween(final long lastTimeMs, final long timeMs) {
            return new Iterable<Pair<Long, Cue>>() { // from class: android.media.SubtitleTrack.CueList.1
                @Override // java.lang.Iterable
                public Iterator<Pair<Long, Cue>> iterator() {
                    if (CueList.this.DEBUG) {
                        Log.d(CueList.TAG, "slice (" + lastTimeMs + ", " + timeMs + "]=");
                    }
                    try {
                        return new EntryIterator(CueList.this.mCues.subMap(Long.valueOf(lastTimeMs + 1), Long.valueOf(timeMs + 1)));
                    } catch (IllegalArgumentException e) {
                        return new EntryIterator(null);
                    }
                }
            };
        }

        public long nextTimeAfter(long timeMs) {
            try {
                SortedMap<Long, Vector<Cue>> tail = this.mCues.tailMap(Long.valueOf(1 + timeMs));
                if (tail == null) {
                    return -1L;
                }
                return tail.firstKey().longValue();
            } catch (IllegalArgumentException e) {
                return -1L;
            } catch (NoSuchElementException e2) {
                return -1L;
            }
        }

        /* loaded from: classes2.dex */
        class EntryIterator implements Iterator<Pair<Long, Cue>> {
            private long mCurrentTimeMs;
            private boolean mDone;
            private Pair<Long, Cue> mLastEntry;
            private Iterator<Cue> mLastListIterator;
            private Iterator<Cue> mListIterator;
            private SortedMap<Long, Vector<Cue>> mRemainingCues;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return !this.mDone;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Pair<Long, Cue> next() {
                if (this.mDone) {
                    throw new NoSuchElementException("");
                }
                this.mLastEntry = new Pair<>(Long.valueOf(this.mCurrentTimeMs), this.mListIterator.next());
                Iterator<Cue> it = this.mListIterator;
                this.mLastListIterator = it;
                if (!it.hasNext()) {
                    nextKey();
                }
                return this.mLastEntry;
            }

            @Override // java.util.Iterator
            public void remove() {
                long[] jArr;
                if (this.mLastListIterator == null || this.mLastEntry.second.mEndTimeMs != this.mLastEntry.first.longValue()) {
                    throw new IllegalStateException("");
                }
                this.mLastListIterator.remove();
                this.mLastListIterator = null;
                if (((Vector) CueList.this.mCues.get(this.mLastEntry.first)).size() == 0) {
                    CueList.this.mCues.remove(this.mLastEntry.first);
                }
                Cue cue = this.mLastEntry.second;
                CueList.this.removeEvent(cue, cue.mStartTimeMs);
                if (cue.mInnerTimesMs != null) {
                    for (long timeMs : cue.mInnerTimesMs) {
                        CueList.this.removeEvent(cue, timeMs);
                    }
                }
            }

            public EntryIterator(SortedMap<Long, Vector<Cue>> cues) {
                if (CueList.this.DEBUG) {
                    Log.v(CueList.TAG, cues + "");
                }
                this.mRemainingCues = cues;
                this.mLastListIterator = null;
                nextKey();
            }

            private void nextKey() {
                do {
                    try {
                        if (this.mRemainingCues == null) {
                            throw new NoSuchElementException("");
                        }
                        this.mCurrentTimeMs = this.mRemainingCues.firstKey().longValue();
                        this.mListIterator = this.mRemainingCues.get(Long.valueOf(this.mCurrentTimeMs)).iterator();
                        try {
                            this.mRemainingCues = this.mRemainingCues.tailMap(Long.valueOf(this.mCurrentTimeMs + 1));
                        } catch (IllegalArgumentException e) {
                            this.mRemainingCues = null;
                        }
                        this.mDone = false;
                    } catch (NoSuchElementException e2) {
                        this.mDone = true;
                        this.mRemainingCues = null;
                        this.mListIterator = null;
                        return;
                    }
                    this.mDone = true;
                    this.mRemainingCues = null;
                    this.mListIterator = null;
                    return;
                } while (!this.mListIterator.hasNext());
            }
        }

        CueList() {
        }
    }

    /* loaded from: classes2.dex */
    public static class Cue {
        public long mEndTimeMs;
        public long[] mInnerTimesMs;
        public Cue mNextInRun;
        public long mRunID;
        public long mStartTimeMs;

        public void onTime(long timeMs) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void finishedRun(long runID) {
        Run run;
        if (runID != 0 && runID != -1 && (run = this.mRunsByID.get(runID)) != null) {
            run.storeByEndTimeMs(this.mRunsByEndTime);
        }
    }

    public void setRunDiscardTimeMs(long runID, long timeMs) {
        Run run;
        if (runID != 0 && runID != -1 && (run = this.mRunsByID.get(runID)) != null) {
            run.mEndTimeMs = timeMs;
            run.storeByEndTimeMs(this.mRunsByEndTime);
        }
    }

    public int getTrackType() {
        if (getRenderingWidget() == null) {
            return 3;
        }
        return 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class Run {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        public long mEndTimeMs;
        public Cue mFirstCue;
        public Run mNextRunAtEndTimeMs;
        public Run mPrevRunAtEndTimeMs;
        public long mRunID;
        private long mStoredEndTimeMs;

        private Run() {
            this.mEndTimeMs = -1L;
            this.mRunID = 0L;
            this.mStoredEndTimeMs = -1L;
        }

        public void storeByEndTimeMs(LongSparseArray<Run> runsByEndTime) {
            int ix = runsByEndTime.indexOfKey(this.mStoredEndTimeMs);
            if (ix >= 0) {
                if (this.mPrevRunAtEndTimeMs == null) {
                    Run run = this.mNextRunAtEndTimeMs;
                    if (run == null) {
                        runsByEndTime.removeAt(ix);
                    } else {
                        runsByEndTime.setValueAt(ix, run);
                    }
                }
                removeAtEndTimeMs();
            }
            long j = this.mEndTimeMs;
            if (j >= 0) {
                this.mPrevRunAtEndTimeMs = null;
                this.mNextRunAtEndTimeMs = runsByEndTime.get(j);
                Run run2 = this.mNextRunAtEndTimeMs;
                if (run2 != null) {
                    run2.mPrevRunAtEndTimeMs = this;
                }
                runsByEndTime.put(this.mEndTimeMs, this);
                this.mStoredEndTimeMs = this.mEndTimeMs;
            }
        }

        public void removeAtEndTimeMs() {
            Run prev = this.mPrevRunAtEndTimeMs;
            Run run = this.mPrevRunAtEndTimeMs;
            if (run != null) {
                run.mNextRunAtEndTimeMs = this.mNextRunAtEndTimeMs;
                this.mPrevRunAtEndTimeMs = null;
            }
            Run run2 = this.mNextRunAtEndTimeMs;
            if (run2 != null) {
                run2.mPrevRunAtEndTimeMs = prev;
                this.mNextRunAtEndTimeMs = null;
            }
        }
    }
}
