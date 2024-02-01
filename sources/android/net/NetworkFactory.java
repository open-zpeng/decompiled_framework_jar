package android.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class NetworkFactory extends Handler {
    private static final int BASE = 536576;
    public static final int CMD_CANCEL_REQUEST = 536577;
    public static final int CMD_REQUEST_NETWORK = 536576;
    private static final int CMD_SET_FILTER = 536579;
    private static final int CMD_SET_SCORE = 536578;
    private static final boolean DBG = true;
    private static final boolean VDBG = false;
    private final String LOG_TAG;
    private NetworkCapabilities mCapabilityFilter;
    private final Context mContext;
    private Messenger mMessenger;
    private final SparseArray<NetworkRequestInfo> mNetworkRequests;
    private int mRefCount;
    private int mScore;

    private protected NetworkFactory(Looper looper, Context context, String logTag, NetworkCapabilities filter) {
        super(looper);
        this.mNetworkRequests = new SparseArray<>();
        this.mRefCount = 0;
        this.mMessenger = null;
        this.LOG_TAG = logTag;
        this.mContext = context;
        this.mCapabilityFilter = filter;
    }

    public synchronized void register() {
        log("Registering NetworkFactory");
        if (this.mMessenger == null) {
            this.mMessenger = new Messenger(this);
            ConnectivityManager.from(this.mContext).registerNetworkFactory(this.mMessenger, this.LOG_TAG);
        }
    }

    public synchronized void unregister() {
        log("Unregistering NetworkFactory");
        if (this.mMessenger != null) {
            ConnectivityManager.from(this.mContext).unregisterNetworkFactory(this.mMessenger);
            this.mMessenger = null;
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 536576:
                handleAddRequest((NetworkRequest) msg.obj, msg.arg1);
                return;
            case CMD_CANCEL_REQUEST /* 536577 */:
                handleRemoveRequest((NetworkRequest) msg.obj);
                return;
            case CMD_SET_SCORE /* 536578 */:
                handleSetScore(msg.arg1);
                return;
            case CMD_SET_FILTER /* 536579 */:
                handleSetFilter((NetworkCapabilities) msg.obj);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public class NetworkRequestInfo {
        public final NetworkRequest request;
        public boolean requested = false;
        public int score;

        public NetworkRequestInfo(NetworkRequest request, int score) {
            this.request = request;
            this.score = score;
        }

        public String toString() {
            return "{" + this.request + ", score=" + this.score + ", requested=" + this.requested + "}";
        }
    }

    @VisibleForTesting
    protected synchronized void handleAddRequest(NetworkRequest request, int score) {
        NetworkRequestInfo n = this.mNetworkRequests.get(request.requestId);
        if (n == null) {
            log("got request " + request + " with score " + score);
            n = new NetworkRequestInfo(request, score);
            this.mNetworkRequests.put(n.request.requestId, n);
        } else {
            n.score = score;
        }
        evalRequest(n);
    }

    @VisibleForTesting
    protected synchronized void handleRemoveRequest(NetworkRequest request) {
        NetworkRequestInfo n = this.mNetworkRequests.get(request.requestId);
        if (n != null) {
            this.mNetworkRequests.remove(request.requestId);
            if (n.requested) {
                releaseNetworkFor(n.request);
            }
        }
    }

    private synchronized void handleSetScore(int score) {
        this.mScore = score;
        evalRequests();
    }

    private synchronized void handleSetFilter(NetworkCapabilities netCap) {
        this.mCapabilityFilter = netCap;
        evalRequests();
    }

    public synchronized boolean acceptRequest(NetworkRequest request, int score) {
        return true;
    }

    protected synchronized void evalRequest(NetworkRequestInfo n) {
        if (!n.requested && n.score < this.mScore && n.request.networkCapabilities.satisfiedByNetworkCapabilities(this.mCapabilityFilter) && acceptRequest(n.request, n.score)) {
            needNetworkFor(n.request, n.score);
            n.requested = true;
        } else if (n.requested) {
            if (n.score > this.mScore || !n.request.networkCapabilities.satisfiedByNetworkCapabilities(this.mCapabilityFilter) || !acceptRequest(n.request, n.score)) {
                releaseNetworkFor(n.request);
                n.requested = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void evalRequests() {
        for (int i = 0; i < this.mNetworkRequests.size(); i++) {
            NetworkRequestInfo n = this.mNetworkRequests.valueAt(i);
            evalRequest(n);
        }
    }

    protected synchronized void reevaluateAllRequests() {
        post(new Runnable() { // from class: android.net.-$$Lambda$NetworkFactory$HfslgqyaKc_n0wXX5_qRYVZoGfI
            @Override // java.lang.Runnable
            public final void run() {
                NetworkFactory.this.evalRequests();
            }
        });
    }

    protected synchronized void startNetwork() {
    }

    protected synchronized void stopNetwork() {
    }

    protected synchronized void needNetworkFor(NetworkRequest networkRequest, int score) {
        int i = this.mRefCount + 1;
        this.mRefCount = i;
        if (i == 1) {
            startNetwork();
        }
    }

    protected synchronized void releaseNetworkFor(NetworkRequest networkRequest) {
        int i = this.mRefCount - 1;
        this.mRefCount = i;
        if (i == 0) {
            stopNetwork();
        }
    }

    public synchronized void addNetworkRequest(NetworkRequest networkRequest, int score) {
        sendMessage(obtainMessage(536576, new NetworkRequestInfo(networkRequest, score)));
    }

    public synchronized void removeNetworkRequest(NetworkRequest networkRequest) {
        sendMessage(obtainMessage(CMD_CANCEL_REQUEST, networkRequest));
    }

    private protected void setScoreFilter(int score) {
        sendMessage(obtainMessage(CMD_SET_SCORE, score, 0));
    }

    public synchronized void setCapabilityFilter(NetworkCapabilities netCap) {
        sendMessage(obtainMessage(CMD_SET_FILTER, new NetworkCapabilities(netCap)));
    }

    @VisibleForTesting
    protected synchronized int getRequestCount() {
        return this.mNetworkRequests.size();
    }

    protected synchronized void log(String s) {
        Log.d(this.LOG_TAG, s);
    }

    private protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        IndentingPrintWriter pw = new IndentingPrintWriter(writer, "  ");
        pw.println(toString());
        pw.increaseIndent();
        for (int i = 0; i < this.mNetworkRequests.size(); i++) {
            pw.println(this.mNetworkRequests.valueAt(i));
        }
        pw.decreaseIndent();
    }

    @Override // android.os.Handler
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append(this.LOG_TAG);
        sb.append(" - ScoreFilter=");
        sb.append(this.mScore);
        sb.append(", Filter=");
        sb.append(this.mCapabilityFilter);
        sb.append(", requests=");
        sb.append(this.mNetworkRequests.size());
        sb.append(", refCount=");
        sb.append(this.mRefCount);
        StringBuilder sb2 = sb.append("}");
        return sb2.toString();
    }
}
