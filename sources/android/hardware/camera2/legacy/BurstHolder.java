package android.hardware.camera2.legacy;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.legacy.RequestHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class BurstHolder {
    private static final String TAG = "BurstHolder";
    private final boolean mRepeating;
    private final ArrayList<RequestHolder.Builder> mRequestBuilders = new ArrayList<>();
    private final int mRequestId;

    public BurstHolder(int requestId, boolean repeating, CaptureRequest[] requests, Collection<Long> jpegSurfaceIds) {
        int i = 0;
        for (CaptureRequest r : requests) {
            this.mRequestBuilders.add(new RequestHolder.Builder(requestId, i, r, repeating, jpegSurfaceIds));
            i++;
        }
        this.mRepeating = repeating;
        this.mRequestId = requestId;
    }

    public int getRequestId() {
        return this.mRequestId;
    }

    public boolean isRepeating() {
        return this.mRepeating;
    }

    public int getNumberOfRequests() {
        return this.mRequestBuilders.size();
    }

    public List<RequestHolder> produceRequestHolders(long frameNumber) {
        ArrayList<RequestHolder> holders = new ArrayList<>();
        int i = 0;
        Iterator<RequestHolder.Builder> it = this.mRequestBuilders.iterator();
        while (it.hasNext()) {
            RequestHolder.Builder b = it.next();
            holders.add(b.build(i + frameNumber));
            i++;
        }
        return holders;
    }
}
