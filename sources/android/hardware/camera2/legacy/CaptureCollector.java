package android.hardware.camera2.legacy;

import android.hardware.camera2.legacy.LegacyExceptionUtils;
import android.util.Log;
import android.util.MutableLong;
import android.util.Pair;
import android.view.Surface;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes.dex */
public class CaptureCollector {
    public protected static final boolean DEBUG = false;
    public protected static final int FLAG_RECEIVED_ALL_JPEG = 3;
    public protected static final int FLAG_RECEIVED_ALL_PREVIEW = 12;
    public protected static final int FLAG_RECEIVED_JPEG = 1;
    public protected static final int FLAG_RECEIVED_JPEG_TS = 2;
    public protected static final int FLAG_RECEIVED_PREVIEW = 4;
    public protected static final int FLAG_RECEIVED_PREVIEW_TS = 8;
    public protected static final int MAX_JPEGS_IN_FLIGHT = 1;
    public protected static final String TAG = "CaptureCollector";
    public protected final CameraDeviceState mDeviceState;
    public protected final int mMaxInFlight;
    public protected final ArrayDeque<CaptureHolder> mPreviewCaptureQueue;
    public protected final ArrayDeque<CaptureHolder> mPreviewProduceQueue;
    public protected final ArrayList<CaptureHolder> mCompletedRequests = new ArrayList<>();
    public protected final ReentrantLock mLock = new ReentrantLock();
    public protected int mInFlight = 0;
    public protected int mInFlightPreviews = 0;
    public protected final ArrayDeque<CaptureHolder> mJpegCaptureQueue = new ArrayDeque<>(1);
    public protected final ArrayDeque<CaptureHolder> mJpegProduceQueue = new ArrayDeque<>(1);
    public protected final TreeSet<CaptureHolder> mActiveRequests = new TreeSet<>();
    public protected final Condition mIsEmpty = this.mLock.newCondition();
    public protected final Condition mNotFull = this.mLock.newCondition();
    public protected final Condition mPreviewsEmpty = this.mLock.newCondition();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CaptureHolder implements Comparable<CaptureHolder> {
        public protected final LegacyRequest mLegacy;
        public protected final RequestHolder mRequest;
        private protected final boolean needsJpeg;
        private protected final boolean needsPreview;
        public protected long mTimestamp = 0;
        public protected int mReceivedFlags = 0;
        public protected boolean mHasStarted = false;
        public protected boolean mFailedJpeg = false;
        public protected boolean mFailedPreview = false;
        public protected boolean mCompleted = false;
        public protected boolean mPreviewCompleted = false;

        public CaptureHolder(RequestHolder request, LegacyRequest legacyHolder) {
            this.mRequest = request;
            this.mLegacy = legacyHolder;
            this.needsJpeg = request.hasJpegTargets();
            this.needsPreview = request.hasPreviewTargets();
        }

        private protected synchronized boolean isPreviewCompleted() {
            return (this.mReceivedFlags & 12) == 12;
        }

        private protected synchronized boolean isJpegCompleted() {
            return (this.mReceivedFlags & 3) == 3;
        }

        private protected synchronized boolean isCompleted() {
            return this.needsJpeg == isJpegCompleted() && this.needsPreview == isPreviewCompleted();
        }

        private protected synchronized void tryComplete() {
            if (!this.mPreviewCompleted && this.needsPreview && isPreviewCompleted()) {
                CaptureCollector.this.onPreviewCompleted();
                this.mPreviewCompleted = true;
            }
            if (isCompleted() && !this.mCompleted) {
                if (this.mFailedPreview || this.mFailedJpeg) {
                    if (!this.mHasStarted) {
                        this.mRequest.failRequest();
                        CaptureCollector.this.mDeviceState.setCaptureStart(this.mRequest, this.mTimestamp, 3);
                    } else {
                        for (Surface targetSurface : this.mRequest.getRequest().getTargets()) {
                            try {
                                if (this.mRequest.jpegType(targetSurface)) {
                                    if (this.mFailedJpeg) {
                                        CaptureCollector.this.mDeviceState.setCaptureResult(this.mRequest, null, 5, targetSurface);
                                    }
                                } else if (this.mFailedPreview) {
                                    CaptureCollector.this.mDeviceState.setCaptureResult(this.mRequest, null, 5, targetSurface);
                                }
                            } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
                                Log.e(CaptureCollector.TAG, "Unexpected exception when querying Surface: " + e);
                            }
                        }
                    }
                }
                CaptureCollector.this.onRequestCompleted(this);
                this.mCompleted = true;
            }
        }

        private protected synchronized void setJpegTimestamp(long timestamp) {
            if (!this.needsJpeg) {
                throw new IllegalStateException("setJpegTimestamp called for capture with no jpeg targets.");
            }
            if (isCompleted()) {
                throw new IllegalStateException("setJpegTimestamp called on already completed request.");
            }
            this.mReceivedFlags |= 2;
            if (this.mTimestamp == 0) {
                this.mTimestamp = timestamp;
            }
            if (!this.mHasStarted) {
                this.mHasStarted = true;
                CaptureCollector.this.mDeviceState.setCaptureStart(this.mRequest, this.mTimestamp, -1);
            }
            tryComplete();
        }

        private protected synchronized void setJpegProduced() {
            if (!this.needsJpeg) {
                throw new IllegalStateException("setJpegProduced called for capture with no jpeg targets.");
            }
            if (isCompleted()) {
                throw new IllegalStateException("setJpegProduced called on already completed request.");
            }
            this.mReceivedFlags |= 1;
            tryComplete();
        }

        private protected synchronized void setJpegFailed() {
            if (!this.needsJpeg || isJpegCompleted()) {
                return;
            }
            this.mFailedJpeg = true;
            this.mReceivedFlags = 1 | this.mReceivedFlags;
            this.mReceivedFlags |= 2;
            tryComplete();
        }

        private protected synchronized void setPreviewTimestamp(long timestamp) {
            if (!this.needsPreview) {
                throw new IllegalStateException("setPreviewTimestamp called for capture with no preview targets.");
            }
            if (isCompleted()) {
                throw new IllegalStateException("setPreviewTimestamp called on already completed request.");
            }
            this.mReceivedFlags |= 8;
            if (this.mTimestamp == 0) {
                this.mTimestamp = timestamp;
            }
            if (!this.needsJpeg && !this.mHasStarted) {
                this.mHasStarted = true;
                CaptureCollector.this.mDeviceState.setCaptureStart(this.mRequest, this.mTimestamp, -1);
            }
            tryComplete();
        }

        private protected synchronized void setPreviewProduced() {
            if (!this.needsPreview) {
                throw new IllegalStateException("setPreviewProduced called for capture with no preview targets.");
            }
            if (isCompleted()) {
                throw new IllegalStateException("setPreviewProduced called on already completed request.");
            }
            this.mReceivedFlags |= 4;
            tryComplete();
        }

        private protected synchronized void setPreviewFailed() {
            if (!this.needsPreview || isPreviewCompleted()) {
                return;
            }
            this.mFailedPreview = true;
            this.mReceivedFlags |= 4;
            this.mReceivedFlags |= 8;
            tryComplete();
        }

        /* JADX INFO: Access modifiers changed from: private */
        @Override // java.lang.Comparable
        public synchronized int compareTo(CaptureHolder captureHolder) {
            if (this.mRequest.getFrameNumber() > captureHolder.mRequest.getFrameNumber()) {
                return 1;
            }
            return this.mRequest.getFrameNumber() == captureHolder.mRequest.getFrameNumber() ? 0 : -1;
        }

        public boolean equals(Object o) {
            return (o instanceof CaptureHolder) && compareTo((CaptureHolder) o) == 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized CaptureCollector(int maxInFlight, CameraDeviceState deviceState) {
        this.mMaxInFlight = maxInFlight;
        this.mPreviewCaptureQueue = new ArrayDeque<>(this.mMaxInFlight);
        this.mPreviewProduceQueue = new ArrayDeque<>(this.mMaxInFlight);
        this.mDeviceState = deviceState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean queueRequest(RequestHolder holder, LegacyRequest legacy, long timeout, TimeUnit unit) throws InterruptedException {
        CaptureHolder h = new CaptureHolder(holder, legacy);
        long nanos = unit.toNanos(timeout);
        ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            if (!h.needsJpeg && !h.needsPreview) {
                throw new IllegalStateException("Request must target at least one output surface!");
            }
            if (h.needsJpeg) {
                while (this.mInFlight > 0) {
                    if (nanos <= 0) {
                        return false;
                    }
                    nanos = this.mIsEmpty.awaitNanos(nanos);
                }
                this.mJpegCaptureQueue.add(h);
                this.mJpegProduceQueue.add(h);
            }
            if (h.needsPreview) {
                while (this.mInFlight >= this.mMaxInFlight) {
                    if (nanos <= 0) {
                        return false;
                    }
                    nanos = this.mNotFull.awaitNanos(nanos);
                }
                this.mPreviewCaptureQueue.add(h);
                this.mPreviewProduceQueue.add(h);
                this.mInFlightPreviews++;
            }
            this.mActiveRequests.add(h);
            this.mInFlight++;
            return true;
        } finally {
            lock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean waitForEmpty(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        ReentrantLock lock = this.mLock;
        lock.lock();
        while (this.mInFlight > 0) {
            try {
                if (nanos > 0) {
                    nanos = this.mIsEmpty.awaitNanos(nanos);
                } else {
                    return false;
                }
            } finally {
                lock.unlock();
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean waitForPreviewsEmpty(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        ReentrantLock lock = this.mLock;
        lock.lock();
        while (this.mInFlightPreviews > 0) {
            try {
                if (nanos > 0) {
                    nanos = this.mPreviewsEmpty.awaitNanos(nanos);
                } else {
                    return false;
                }
            } finally {
                lock.unlock();
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean waitForRequestCompleted(RequestHolder holder, long timeout, TimeUnit unit, MutableLong timestamp) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        ReentrantLock lock = this.mLock;
        lock.lock();
        while (!removeRequestIfCompleted(holder, timestamp)) {
            try {
                if (nanos > 0) {
                    nanos = this.mNotFull.awaitNanos(nanos);
                } else {
                    return false;
                }
            } finally {
                lock.unlock();
            }
        }
        return true;
    }

    public protected synchronized boolean removeRequestIfCompleted(RequestHolder holder, MutableLong timestamp) {
        int i = 0;
        Iterator<CaptureHolder> it = this.mCompletedRequests.iterator();
        while (it.hasNext()) {
            CaptureHolder h = it.next();
            if (h.mRequest.equals(holder)) {
                timestamp.value = h.mTimestamp;
                this.mCompletedRequests.remove(i);
                return true;
            }
            i++;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized RequestHolder jpegCaptured(long timestamp) {
        ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            CaptureHolder h = this.mJpegCaptureQueue.poll();
            if (h == null) {
                Log.w(TAG, "jpegCaptured called with no jpeg request on queue!");
                return null;
            }
            h.setJpegTimestamp(timestamp);
            return h.mRequest;
        } finally {
            lock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Pair<RequestHolder, Long> jpegProduced() {
        ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            CaptureHolder h = this.mJpegProduceQueue.poll();
            if (h == null) {
                Log.w(TAG, "jpegProduced called with no jpeg request on queue!");
                return null;
            }
            h.setJpegProduced();
            return new Pair<>(h.mRequest, Long.valueOf(h.mTimestamp));
        } finally {
            lock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean hasPendingPreviewCaptures() {
        ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            return !this.mPreviewCaptureQueue.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Pair<RequestHolder, Long> previewCaptured(long timestamp) {
        ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            CaptureHolder h = this.mPreviewCaptureQueue.poll();
            if (h != null) {
                h.setPreviewTimestamp(timestamp);
                return new Pair<>(h.mRequest, Long.valueOf(h.mTimestamp));
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized RequestHolder previewProduced() {
        ReentrantLock lock = this.mLock;
        lock.lock();
        try {
            CaptureHolder h = this.mPreviewProduceQueue.poll();
            if (h == null) {
                Log.w(TAG, "previewProduced called with no preview request on queue!");
                return null;
            }
            h.setPreviewProduced();
            return h.mRequest;
        } finally {
            lock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001f, code lost:
        if (r1.compareTo2(r2) > 0) goto L6;
     */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0026 A[Catch: all -> 0x003d, TRY_LEAVE, TryCatch #0 {all -> 0x003d, blocks: (B:3:0x0005, B:13:0x0026, B:8:0x001b), top: B:19:0x0005 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void failNextPreview() {
        /*
            r5 = this;
            java.util.concurrent.locks.ReentrantLock r0 = r5.mLock
            r0.lock()
            java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector$CaptureHolder> r1 = r5.mPreviewCaptureQueue     // Catch: java.lang.Throwable -> L3d
            java.lang.Object r1 = r1.peek()     // Catch: java.lang.Throwable -> L3d
            android.hardware.camera2.legacy.CaptureCollector$CaptureHolder r1 = (android.hardware.camera2.legacy.CaptureCollector.CaptureHolder) r1     // Catch: java.lang.Throwable -> L3d
            java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector$CaptureHolder> r2 = r5.mPreviewProduceQueue     // Catch: java.lang.Throwable -> L3d
            java.lang.Object r2 = r2.peek()     // Catch: java.lang.Throwable -> L3d
            android.hardware.camera2.legacy.CaptureCollector$CaptureHolder r2 = (android.hardware.camera2.legacy.CaptureCollector.CaptureHolder) r2     // Catch: java.lang.Throwable -> L3d
            if (r1 != 0) goto L18
            goto L23
        L18:
            if (r2 != 0) goto L1b
            goto L21
        L1b:
            int r3 = r1.compareTo(r2)     // Catch: java.lang.Throwable -> L3d
            if (r3 > 0) goto L23
        L21:
            r3 = r1
            goto L24
        L23:
            r3 = r2
        L24:
            if (r3 == 0) goto L38
            java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector$CaptureHolder> r4 = r5.mPreviewCaptureQueue     // Catch: java.lang.Throwable -> L3d
            r4.remove(r3)     // Catch: java.lang.Throwable -> L3d
            java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector$CaptureHolder> r4 = r5.mPreviewProduceQueue     // Catch: java.lang.Throwable -> L3d
            r4.remove(r3)     // Catch: java.lang.Throwable -> L3d
            java.util.TreeSet<android.hardware.camera2.legacy.CaptureCollector$CaptureHolder> r4 = r5.mActiveRequests     // Catch: java.lang.Throwable -> L3d
            r4.remove(r3)     // Catch: java.lang.Throwable -> L3d
            r3.setPreviewFailed()     // Catch: java.lang.Throwable -> L3d
        L38:
            r0.unlock()
            return
        L3d:
            r1 = move-exception
            r0.unlock()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.CaptureCollector.failNextPreview():void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001f, code lost:
        if (r1.compareTo2(r2) > 0) goto L6;
     */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0026 A[Catch: all -> 0x003d, TRY_LEAVE, TryCatch #0 {all -> 0x003d, blocks: (B:3:0x0005, B:13:0x0026, B:8:0x001b), top: B:19:0x0005 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void failNextJpeg() {
        /*
            r5 = this;
            java.util.concurrent.locks.ReentrantLock r0 = r5.mLock
            r0.lock()
            java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector$CaptureHolder> r1 = r5.mJpegCaptureQueue     // Catch: java.lang.Throwable -> L3d
            java.lang.Object r1 = r1.peek()     // Catch: java.lang.Throwable -> L3d
            android.hardware.camera2.legacy.CaptureCollector$CaptureHolder r1 = (android.hardware.camera2.legacy.CaptureCollector.CaptureHolder) r1     // Catch: java.lang.Throwable -> L3d
            java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector$CaptureHolder> r2 = r5.mJpegProduceQueue     // Catch: java.lang.Throwable -> L3d
            java.lang.Object r2 = r2.peek()     // Catch: java.lang.Throwable -> L3d
            android.hardware.camera2.legacy.CaptureCollector$CaptureHolder r2 = (android.hardware.camera2.legacy.CaptureCollector.CaptureHolder) r2     // Catch: java.lang.Throwable -> L3d
            if (r1 != 0) goto L18
            goto L23
        L18:
            if (r2 != 0) goto L1b
            goto L21
        L1b:
            int r3 = r1.compareTo(r2)     // Catch: java.lang.Throwable -> L3d
            if (r3 > 0) goto L23
        L21:
            r3 = r1
            goto L24
        L23:
            r3 = r2
        L24:
            if (r3 == 0) goto L38
            java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector$CaptureHolder> r4 = r5.mJpegCaptureQueue     // Catch: java.lang.Throwable -> L3d
            r4.remove(r3)     // Catch: java.lang.Throwable -> L3d
            java.util.ArrayDeque<android.hardware.camera2.legacy.CaptureCollector$CaptureHolder> r4 = r5.mJpegProduceQueue     // Catch: java.lang.Throwable -> L3d
            r4.remove(r3)     // Catch: java.lang.Throwable -> L3d
            java.util.TreeSet<android.hardware.camera2.legacy.CaptureCollector$CaptureHolder> r4 = r5.mActiveRequests     // Catch: java.lang.Throwable -> L3d
            r4.remove(r3)     // Catch: java.lang.Throwable -> L3d
            r3.setJpegFailed()     // Catch: java.lang.Throwable -> L3d
        L38:
            r0.unlock()
            return
        L3d:
            r1 = move-exception
            r0.unlock()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.CaptureCollector.failNextJpeg():void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void failAll() {
        ReentrantLock lock = this.mLock;
        lock.lock();
        while (true) {
            try {
                CaptureHolder h = this.mActiveRequests.pollFirst();
                if (h != null) {
                    h.setPreviewFailed();
                    h.setJpegFailed();
                } else {
                    this.mPreviewCaptureQueue.clear();
                    this.mPreviewProduceQueue.clear();
                    this.mJpegCaptureQueue.clear();
                    this.mJpegProduceQueue.clear();
                    return;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized void onPreviewCompleted() {
        this.mInFlightPreviews--;
        if (this.mInFlightPreviews < 0) {
            throw new IllegalStateException("More preview captures completed than requests queued.");
        }
        if (this.mInFlightPreviews == 0) {
            this.mPreviewsEmpty.signalAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized void onRequestCompleted(CaptureHolder capture) {
        RequestHolder unused = capture.mRequest;
        this.mInFlight--;
        if (this.mInFlight < 0) {
            throw new IllegalStateException("More captures completed than requests queued.");
        }
        this.mCompletedRequests.add(capture);
        this.mActiveRequests.remove(capture);
        this.mNotFull.signalAll();
        if (this.mInFlight == 0) {
            this.mIsEmpty.signalAll();
        }
    }
}
