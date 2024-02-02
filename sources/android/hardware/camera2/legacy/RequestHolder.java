package android.hardware.camera2.legacy;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.legacy.LegacyExceptionUtils;
import android.util.Log;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import java.util.Collection;
/* loaded from: classes.dex */
public class RequestHolder {
    public protected static final String TAG = "RequestHolder";
    public protected volatile boolean mFailed;
    public protected final long mFrameNumber;
    public protected final Collection<Long> mJpegSurfaceIds;
    public protected final int mNumJpegTargets;
    public protected final int mNumPreviewTargets;
    public protected boolean mOutputAbandoned;
    public protected final boolean mRepeating;
    public protected final CaptureRequest mRequest;
    public protected final int mRequestId;
    public protected final int mSubsequeceId;

    /* loaded from: classes.dex */
    public static final class Builder {
        public protected final Collection<Long> mJpegSurfaceIds;
        public protected final int mNumJpegTargets;
        public protected final int mNumPreviewTargets;
        public protected final boolean mRepeating;
        public protected final CaptureRequest mRequest;
        public protected final int mRequestId;
        public protected final int mSubsequenceId;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Builder(int requestId, int subsequenceId, CaptureRequest request, boolean repeating, Collection<Long> jpegSurfaceIds) {
            Preconditions.checkNotNull(request, "request must not be null");
            this.mRequestId = requestId;
            this.mSubsequenceId = subsequenceId;
            this.mRequest = request;
            this.mRepeating = repeating;
            this.mJpegSurfaceIds = jpegSurfaceIds;
            this.mNumJpegTargets = numJpegTargets(this.mRequest);
            this.mNumPreviewTargets = numPreviewTargets(this.mRequest);
        }

        public protected synchronized boolean jpegType(Surface s) throws LegacyExceptionUtils.BufferQueueAbandonedException {
            return LegacyCameraDevice.containsSurfaceId(s, this.mJpegSurfaceIds);
        }

        public protected synchronized boolean previewType(Surface s) throws LegacyExceptionUtils.BufferQueueAbandonedException {
            return !jpegType(s);
        }

        public protected synchronized int numJpegTargets(CaptureRequest request) {
            int count = 0;
            for (Surface s : request.getTargets()) {
                try {
                    if (jpegType(s)) {
                        count++;
                    }
                } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
                    Log.d(RequestHolder.TAG, "Surface abandoned, skipping...", e);
                }
            }
            return count;
        }

        public protected synchronized int numPreviewTargets(CaptureRequest request) {
            int count = 0;
            for (Surface s : request.getTargets()) {
                try {
                    if (previewType(s)) {
                        count++;
                    }
                } catch (LegacyExceptionUtils.BufferQueueAbandonedException e) {
                    Log.d(RequestHolder.TAG, "Surface abandoned, skipping...", e);
                }
            }
            return count;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized RequestHolder build(long frameNumber) {
            return new RequestHolder(this.mRequestId, this.mSubsequenceId, this.mRequest, this.mRepeating, frameNumber, this.mNumJpegTargets, this.mNumPreviewTargets, this.mJpegSurfaceIds);
        }
    }

    public protected synchronized RequestHolder(int requestId, int subsequenceId, CaptureRequest request, boolean repeating, long frameNumber, int numJpegTargets, int numPreviewTargets, Collection<Long> jpegSurfaceIds) {
        this.mFailed = false;
        this.mOutputAbandoned = false;
        this.mRepeating = repeating;
        this.mRequest = request;
        this.mRequestId = requestId;
        this.mSubsequeceId = subsequenceId;
        this.mFrameNumber = frameNumber;
        this.mNumJpegTargets = numJpegTargets;
        this.mNumPreviewTargets = numPreviewTargets;
        this.mJpegSurfaceIds = jpegSurfaceIds;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getRequestId() {
        return this.mRequestId;
    }

    private protected synchronized boolean isRepeating() {
        return this.mRepeating;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getSubsequeceId() {
        return this.mSubsequeceId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long getFrameNumber() {
        return this.mFrameNumber;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized CaptureRequest getRequest() {
        return this.mRequest;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Collection<Surface> getHolderTargets() {
        return getRequest().getTargets();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean hasJpegTargets() {
        return this.mNumJpegTargets > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean hasPreviewTargets() {
        return this.mNumPreviewTargets > 0;
    }

    private protected synchronized int numJpegTargets() {
        return this.mNumJpegTargets;
    }

    private protected synchronized int numPreviewTargets() {
        return this.mNumPreviewTargets;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean jpegType(Surface s) throws LegacyExceptionUtils.BufferQueueAbandonedException {
        return LegacyCameraDevice.containsSurfaceId(s, this.mJpegSurfaceIds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void failRequest() {
        Log.w(TAG, "Capture failed for request: " + getRequestId());
        this.mFailed = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean requestFailed() {
        return this.mFailed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setOutputAbandoned() {
        this.mOutputAbandoned = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isOutputAbandoned() {
        return this.mOutputAbandoned;
    }
}
