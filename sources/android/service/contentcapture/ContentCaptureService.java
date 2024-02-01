package android.service.contentcapture;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.service.contentcapture.IContentCaptureService;
import android.service.contentcapture.IContentCaptureServiceCallback;
import android.util.Log;
import android.util.Slog;
import android.util.SparseIntArray;
import android.util.StatsLog;
import android.view.contentcapture.ContentCaptureCondition;
import android.view.contentcapture.ContentCaptureContext;
import android.view.contentcapture.ContentCaptureEvent;
import android.view.contentcapture.ContentCaptureHelper;
import android.view.contentcapture.ContentCaptureSessionId;
import android.view.contentcapture.DataRemovalRequest;
import android.view.contentcapture.IContentCaptureDirectManager;
import android.view.contentcapture.MainContentCaptureSession;
import com.android.internal.os.IResultReceiver;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SystemApi
/* loaded from: classes2.dex */
public abstract class ContentCaptureService extends Service {
    public static final String SERVICE_INTERFACE = "android.service.contentcapture.ContentCaptureService";
    public static final String SERVICE_META_DATA = "android.content_capture";
    private static final String TAG = ContentCaptureService.class.getSimpleName();
    private IContentCaptureServiceCallback mCallback;
    private Handler mHandler;
    private long mLastCallerMismatchLog;
    private long mCallerMismatchTimeout = 1000;
    private final IContentCaptureService mServerInterface = new AnonymousClass1();
    private final IContentCaptureDirectManager mClientInterface = new AnonymousClass2();
    private final SparseIntArray mSessionUids = new SparseIntArray();

    /* renamed from: android.service.contentcapture.ContentCaptureService$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends IContentCaptureService.Stub {
        AnonymousClass1() {
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onConnected(IBinder callback, boolean verbose, boolean debug) {
            ContentCaptureHelper.sVerbose = verbose;
            ContentCaptureHelper.sDebug = debug;
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: android.service.contentcapture.-$$Lambda$ContentCaptureService$1$iP7RXM_Va9lafd6bT9eXRx_D47Q
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((ContentCaptureService) obj).handleOnConnected((IBinder) obj2);
                }
            }, ContentCaptureService.this, callback));
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onDisconnected() {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: android.service.contentcapture.-$$Lambda$ContentCaptureService$1$wPMOb7AM5r-kHmuyl3SBSylaH1A
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((ContentCaptureService) obj).handleOnDisconnected();
                }
            }, ContentCaptureService.this));
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onSessionStarted(ContentCaptureContext context, int sessionId, int uid, IResultReceiver clientReceiver, int initialState) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new HexConsumer() { // from class: android.service.contentcapture.-$$Lambda$ContentCaptureService$1$PaMsQkJwdUJ1lCgOOaLG9Bm09t8
                @Override // com.android.internal.util.function.HexConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
                    ((ContentCaptureService) obj).handleOnCreateSession((ContentCaptureContext) obj2, ((Integer) obj3).intValue(), ((Integer) obj4).intValue(), (IResultReceiver) obj5, ((Integer) obj6).intValue());
                }
            }, ContentCaptureService.this, context, Integer.valueOf(sessionId), Integer.valueOf(uid), clientReceiver, Integer.valueOf(initialState)));
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onActivitySnapshot(int sessionId, SnapshotData snapshotData) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: android.service.contentcapture.-$$Lambda$ContentCaptureService$1$NhSHlL57JqxWNJ8QcsuGxEhxv1Y
                @Override // com.android.internal.util.function.TriConsumer
                public final void accept(Object obj, Object obj2, Object obj3) {
                    ((ContentCaptureService) obj).handleOnActivitySnapshot(((Integer) obj2).intValue(), (SnapshotData) obj3);
                }
            }, ContentCaptureService.this, Integer.valueOf(sessionId), snapshotData));
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onSessionFinished(int sessionId) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: android.service.contentcapture.-$$Lambda$ContentCaptureService$1$jkZQ77YuBlPDClOdklQb8tj8Kpw
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((ContentCaptureService) obj).handleFinishSession(((Integer) obj2).intValue());
                }
            }, ContentCaptureService.this, Integer.valueOf(sessionId)));
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onDataRemovalRequest(DataRemovalRequest request) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: android.service.contentcapture.-$$Lambda$ContentCaptureService$1$sJuAS4AaQcXaSFkQpSEmVLBqyvw
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((ContentCaptureService) obj).handleOnDataRemovalRequest((DataRemovalRequest) obj2);
                }
            }, ContentCaptureService.this, request));
        }

        @Override // android.service.contentcapture.IContentCaptureService
        public void onActivityEvent(ActivityEvent event) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: android.service.contentcapture.-$$Lambda$ContentCaptureService$1$V1mxGgTDjVVHroIjJrHvYfUHCKE
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((ContentCaptureService) obj).handleOnActivityEvent((ActivityEvent) obj2);
                }
            }, ContentCaptureService.this, event));
        }
    }

    /* renamed from: android.service.contentcapture.ContentCaptureService$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass2 extends IContentCaptureDirectManager.Stub {
        AnonymousClass2() {
        }

        @Override // android.view.contentcapture.IContentCaptureDirectManager
        public void sendEvents(ParceledListSlice events, int reason, ContentCaptureOptions options) {
            ContentCaptureService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new QuintConsumer() { // from class: android.service.contentcapture.-$$Lambda$ContentCaptureService$2$nqaNcni5MOtmyGkMJfxu_qUHOk4
                @Override // com.android.internal.util.function.QuintConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                    ((ContentCaptureService) obj).handleSendEvents(((Integer) obj2).intValue(), (ParceledListSlice) obj3, ((Integer) obj4).intValue(), (ContentCaptureOptions) obj5);
                }
            }, ContentCaptureService.this, Integer.valueOf(Binder.getCallingUid()), events, Integer.valueOf(reason), options));
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mHandler = new Handler(Looper.getMainLooper(), null, true);
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        if (SERVICE_INTERFACE.equals(intent.getAction())) {
            return this.mServerInterface.asBinder();
        }
        String str = TAG;
        Log.w(str, "Tried to bind to wrong intent (should be android.service.contentcapture.ContentCaptureService: " + intent);
        return null;
    }

    public final void setContentCaptureWhitelist(Set<String> packages, Set<ComponentName> activities) {
        IContentCaptureServiceCallback callback = this.mCallback;
        if (callback == null) {
            Log.w(TAG, "setContentCaptureWhitelist(): no server callback");
            return;
        }
        try {
            callback.setContentCaptureWhitelist(ContentCaptureHelper.toList(packages), ContentCaptureHelper.toList(activities));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public final void setContentCaptureConditions(String packageName, Set<ContentCaptureCondition> conditions) {
        IContentCaptureServiceCallback callback = this.mCallback;
        if (callback == null) {
            Log.w(TAG, "setContentCaptureConditions(): no server callback");
            return;
        }
        try {
            callback.setContentCaptureConditions(packageName, ContentCaptureHelper.toList(conditions));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void onConnected() {
        String str = TAG;
        Slog.i(str, "bound to " + getClass().getName());
    }

    public void onCreateContentCaptureSession(ContentCaptureContext context, ContentCaptureSessionId sessionId) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            Log.v(str, "onCreateContentCaptureSession(id=" + sessionId + ", ctx=" + context + ")");
        }
    }

    public void onContentCaptureEvent(ContentCaptureSessionId sessionId, ContentCaptureEvent event) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            Log.v(str, "onContentCaptureEventsRequest(id=" + sessionId + ")");
        }
    }

    public void onDataRemovalRequest(DataRemovalRequest request) {
        if (ContentCaptureHelper.sVerbose) {
            Log.v(TAG, "onDataRemovalRequest()");
        }
    }

    public void onActivitySnapshot(ContentCaptureSessionId sessionId, SnapshotData snapshotData) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            Log.v(str, "onActivitySnapshot(id=" + sessionId + ")");
        }
    }

    public void onActivityEvent(ActivityEvent event) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            Log.v(str, "onActivityEvent(): " + event);
        }
    }

    public void onDestroyContentCaptureSession(ContentCaptureSessionId sessionId) {
        if (ContentCaptureHelper.sVerbose) {
            String str = TAG;
            Log.v(str, "onDestroyContentCaptureSession(id=" + sessionId + ")");
        }
    }

    public final void disableSelf() {
        if (ContentCaptureHelper.sDebug) {
            Log.d(TAG, "disableSelf()");
        }
        IContentCaptureServiceCallback callback = this.mCallback;
        if (callback == null) {
            Log.w(TAG, "disableSelf(): no server callback");
            return;
        }
        try {
            callback.disableSelf();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void onDisconnected() {
        String str = TAG;
        Slog.i(str, "unbinding from " + getClass().getName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Service
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.print("Debug: ");
        pw.print(ContentCaptureHelper.sDebug);
        pw.print(" Verbose: ");
        pw.println(ContentCaptureHelper.sVerbose);
        int size = this.mSessionUids.size();
        pw.print("Number sessions: ");
        pw.println(size);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                pw.print("  ");
                pw.print(this.mSessionUids.keyAt(i));
                pw.print(": uid=");
                pw.println(this.mSessionUids.valueAt(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnConnected(IBinder callback) {
        this.mCallback = IContentCaptureServiceCallback.Stub.asInterface(callback);
        onConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnDisconnected() {
        onDisconnected();
        this.mCallback = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnCreateSession(ContentCaptureContext context, int sessionId, int uid, IResultReceiver clientReceiver, int initialState) {
        int stateFlags;
        this.mSessionUids.put(sessionId, uid);
        onCreateContentCaptureSession(context, new ContentCaptureSessionId(sessionId));
        int clientFlags = context.getFlags();
        int stateFlags2 = 0;
        if ((clientFlags & 2) != 0) {
            stateFlags2 = 0 | 32;
        }
        if ((clientFlags & 1) != 0) {
            stateFlags2 |= 64;
        }
        if (stateFlags2 == 0) {
            stateFlags = initialState;
        } else {
            stateFlags = stateFlags2 | 4;
        }
        setClientState(clientReceiver, stateFlags, this.mClientInterface.asBinder());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSendEvents(int uid, ParceledListSlice<ContentCaptureEvent> parceledEvents, int reason, ContentCaptureOptions options) {
        List<ContentCaptureEvent> events = parceledEvents.getList();
        if (events.isEmpty()) {
            Log.w(TAG, "handleSendEvents() received empty list of events");
            return;
        }
        FlushMetrics metrics = new FlushMetrics();
        ComponentName activityComponent = null;
        int lastSessionId = 0;
        ContentCaptureSessionId sessionId = null;
        for (int i = 0; i < events.size(); i++) {
            ContentCaptureEvent event = events.get(i);
            if (handleIsRightCallerFor(event, uid)) {
                int sessionIdInt = event.getSessionId();
                if (sessionIdInt != lastSessionId) {
                    sessionId = new ContentCaptureSessionId(sessionIdInt);
                    if (i != 0) {
                        writeFlushMetrics(sessionIdInt, activityComponent, metrics, options, reason);
                        metrics.reset();
                    }
                    lastSessionId = sessionIdInt;
                }
                ContentCaptureContext clientContext = event.getContentCaptureContext();
                if (activityComponent == null && clientContext != null) {
                    activityComponent = clientContext.getActivityComponent();
                }
                int type = event.getType();
                if (type == -2) {
                    this.mSessionUids.delete(sessionIdInt);
                    onDestroyContentCaptureSession(sessionId);
                    metrics.sessionFinished++;
                } else if (type == -1) {
                    clientContext.setParentSessionId(event.getParentSessionId());
                    this.mSessionUids.put(sessionIdInt, uid);
                    onCreateContentCaptureSession(clientContext, sessionId);
                    metrics.sessionStarted++;
                } else if (type == 1) {
                    onContentCaptureEvent(sessionId, event);
                    metrics.viewAppearedCount++;
                } else if (type == 2) {
                    onContentCaptureEvent(sessionId, event);
                    metrics.viewDisappearedCount++;
                } else if (type == 3) {
                    onContentCaptureEvent(sessionId, event);
                    metrics.viewTextChangedCount++;
                } else {
                    onContentCaptureEvent(sessionId, event);
                }
            }
        }
        writeFlushMetrics(lastSessionId, activityComponent, metrics, options, reason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnActivitySnapshot(int sessionId, SnapshotData snapshotData) {
        onActivitySnapshot(new ContentCaptureSessionId(sessionId), snapshotData);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFinishSession(int sessionId) {
        this.mSessionUids.delete(sessionId);
        onDestroyContentCaptureSession(new ContentCaptureSessionId(sessionId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnDataRemovalRequest(DataRemovalRequest request) {
        onDataRemovalRequest(request);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnActivityEvent(ActivityEvent event) {
        onActivityEvent(event);
    }

    private boolean handleIsRightCallerFor(ContentCaptureEvent event, int uid) {
        int sessionId;
        int type = event.getType();
        if (type == -2 || type == -1) {
            sessionId = event.getParentSessionId();
        } else {
            sessionId = event.getSessionId();
        }
        if (this.mSessionUids.indexOfKey(sessionId) < 0) {
            if (ContentCaptureHelper.sVerbose) {
                String str = TAG;
                Log.v(str, "handleIsRightCallerFor(" + event + "): no session for " + sessionId + ": " + this.mSessionUids);
            }
            return false;
        }
        int rightUid = this.mSessionUids.get(sessionId);
        if (rightUid != uid) {
            String str2 = TAG;
            Log.e(str2, "invalid call from UID " + uid + ": session " + sessionId + " belongs to " + rightUid);
            long now = System.currentTimeMillis();
            if (now - this.mLastCallerMismatchLog > this.mCallerMismatchTimeout) {
                StatsLog.write(206, getPackageManager().getNameForUid(rightUid), getPackageManager().getNameForUid(uid));
                this.mLastCallerMismatchLog = now;
            }
            return false;
        }
        return true;
    }

    public static void setClientState(IResultReceiver clientReceiver, int sessionState, IBinder binder) {
        Bundle extras;
        if (binder != null) {
            try {
                extras = new Bundle();
                extras.putBinder(MainContentCaptureSession.EXTRA_BINDER, binder);
            } catch (RemoteException e) {
                String str = TAG;
                Slog.w(str, "Error async reporting result to client: " + e);
                return;
            }
        } else {
            extras = null;
        }
        clientReceiver.send(sessionState, extras);
    }

    private void writeFlushMetrics(int sessionId, ComponentName app, FlushMetrics flushMetrics, ContentCaptureOptions options, int flushReason) {
        IContentCaptureServiceCallback iContentCaptureServiceCallback = this.mCallback;
        if (iContentCaptureServiceCallback == null) {
            Log.w(TAG, "writeSessionFlush(): no server callback");
            return;
        }
        try {
            iContentCaptureServiceCallback.writeSessionFlush(sessionId, app, flushMetrics, options, flushReason);
        } catch (RemoteException e) {
            String str = TAG;
            Log.e(str, "failed to write flush metrics: " + e);
        }
    }
}
