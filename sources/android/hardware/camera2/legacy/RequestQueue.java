package android.hardware.camera2.legacy;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.utils.SubmitInfo;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class RequestQueue {
    private static final long INVALID_FRAME = -1;
    private static final String TAG = "RequestQueue";
    private final List<Long> mJpegSurfaceIds;
    private BurstHolder mRepeatingRequest = null;
    private final ArrayDeque<BurstHolder> mRequestQueue = new ArrayDeque<>();
    private long mCurrentFrameNumber = 0;
    private long mCurrentRepeatingFrameNumber = -1;
    private int mCurrentRequestId = 0;

    /* loaded from: classes.dex */
    public final class RequestQueueEntry {
        private final BurstHolder mBurstHolder;
        private final Long mFrameNumber;
        private final boolean mQueueEmpty;

        public BurstHolder getBurstHolder() {
            return this.mBurstHolder;
        }

        public Long getFrameNumber() {
            return this.mFrameNumber;
        }

        public boolean isQueueEmpty() {
            return this.mQueueEmpty;
        }

        public RequestQueueEntry(BurstHolder burstHolder, Long frameNumber, boolean queueEmpty) {
            this.mBurstHolder = burstHolder;
            this.mFrameNumber = frameNumber;
            this.mQueueEmpty = queueEmpty;
        }
    }

    public RequestQueue(List<Long> jpegSurfaceIds) {
        this.mJpegSurfaceIds = jpegSurfaceIds;
    }

    public synchronized RequestQueueEntry getNext() {
        BurstHolder next = this.mRequestQueue.poll();
        boolean queueEmptied = next != null && this.mRequestQueue.size() == 0;
        if (next == null && this.mRepeatingRequest != null) {
            next = this.mRepeatingRequest;
            this.mCurrentRepeatingFrameNumber = this.mCurrentFrameNumber + next.getNumberOfRequests();
        }
        if (next == null) {
            return null;
        }
        RequestQueueEntry ret = new RequestQueueEntry(next, Long.valueOf(this.mCurrentFrameNumber), queueEmptied);
        this.mCurrentFrameNumber += next.getNumberOfRequests();
        return ret;
    }

    public synchronized long stopRepeating(int requestId) {
        long ret;
        ret = -1;
        if (this.mRepeatingRequest != null && this.mRepeatingRequest.getRequestId() == requestId) {
            this.mRepeatingRequest = null;
            ret = this.mCurrentRepeatingFrameNumber == -1 ? -1L : this.mCurrentRepeatingFrameNumber - 1;
            this.mCurrentRepeatingFrameNumber = -1L;
            Log.i(TAG, "Repeating capture request cancelled.");
        } else {
            Log.e(TAG, "cancel failed: no repeating request exists for request id: " + requestId);
        }
        return ret;
    }

    public synchronized long stopRepeating() {
        if (this.mRepeatingRequest == null) {
            Log.e(TAG, "cancel failed: no repeating request exists.");
            return -1L;
        }
        return stopRepeating(this.mRepeatingRequest.getRequestId());
    }

    public synchronized SubmitInfo submit(CaptureRequest[] requests, boolean repeating) {
        SubmitInfo info;
        int requestId = this.mCurrentRequestId;
        this.mCurrentRequestId = requestId + 1;
        BurstHolder burst = new BurstHolder(requestId, repeating, requests, this.mJpegSurfaceIds);
        long lastFrame = -1;
        if (burst.isRepeating()) {
            Log.i(TAG, "Repeating capture request set.");
            if (this.mRepeatingRequest != null) {
                lastFrame = this.mCurrentRepeatingFrameNumber == -1 ? -1L : this.mCurrentRepeatingFrameNumber - 1;
            }
            this.mCurrentRepeatingFrameNumber = -1L;
            this.mRepeatingRequest = burst;
        } else {
            this.mRequestQueue.offer(burst);
            lastFrame = calculateLastFrame(burst.getRequestId());
        }
        info = new SubmitInfo(requestId, lastFrame);
        return info;
    }

    private long calculateLastFrame(int requestId) {
        long total = this.mCurrentFrameNumber;
        Iterator<BurstHolder> it = this.mRequestQueue.iterator();
        while (it.hasNext()) {
            BurstHolder b = it.next();
            total += b.getNumberOfRequests();
            if (b.getRequestId() == requestId) {
                return total - 1;
            }
        }
        throw new IllegalStateException("At least one request must be in the queue to calculate frame number");
    }
}
