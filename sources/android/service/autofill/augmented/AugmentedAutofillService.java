package android.service.autofill.augmented;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.SettingsStringUtil;
import android.service.autofill.augmented.IAugmentedAutofillService;
import android.service.autofill.augmented.PresentationParams;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.view.autofill.IAugmentedAutofillManagerClient;
import android.view.autofill.IAutofillWindowPresenter;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.NonaConsumer;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SystemApi
/* loaded from: classes2.dex */
public abstract class AugmentedAutofillService extends Service {
    public static final String SERVICE_INTERFACE = "android.service.autofill.augmented.AugmentedAutofillService";
    private static final String TAG = AugmentedAutofillService.class.getSimpleName();
    static boolean sDebug = !Build.IS_USER;
    static boolean sVerbose = false;
    private SparseArray<AutofillProxy> mAutofillProxies;
    private Handler mHandler;
    private final IAugmentedAutofillService mInterface = new AnonymousClass1();
    private ComponentName mServiceComponentName;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.service.autofill.augmented.AugmentedAutofillService$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends IAugmentedAutofillService.Stub {
        AnonymousClass1() {
        }

        @Override // android.service.autofill.augmented.IAugmentedAutofillService
        public void onConnected(boolean debug, boolean verbose) {
            AugmentedAutofillService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: android.service.autofill.augmented.-$$Lambda$AugmentedAutofillService$1$4dXh5Zwc8KxDD9bV1LFhgo3zrgk
                @Override // com.android.internal.util.function.TriConsumer
                public final void accept(Object obj, Object obj2, Object obj3) {
                    ((AugmentedAutofillService) obj).handleOnConnected(((Boolean) obj2).booleanValue(), ((Boolean) obj3).booleanValue());
                }
            }, AugmentedAutofillService.this, Boolean.valueOf(debug), Boolean.valueOf(verbose)));
        }

        @Override // android.service.autofill.augmented.IAugmentedAutofillService
        public void onDisconnected() {
            AugmentedAutofillService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: android.service.autofill.augmented.-$$Lambda$AugmentedAutofillService$1$D2Ct4Bd0D1M8vONZTBmU9zstEFI
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((AugmentedAutofillService) obj).handleOnDisconnected();
                }
            }, AugmentedAutofillService.this));
        }

        @Override // android.service.autofill.augmented.IAugmentedAutofillService
        public void onFillRequest(int sessionId, IBinder client, int taskId, ComponentName componentName, AutofillId focusedId, AutofillValue focusedValue, long requestTime, IFillCallback callback) {
            AugmentedAutofillService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new NonaConsumer() { // from class: android.service.autofill.augmented.-$$Lambda$AugmentedAutofillService$1$mgzh8N5GuvmPXfqMBgjw-Q27Ij0
                @Override // com.android.internal.util.function.NonaConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9) {
                    ((AugmentedAutofillService) obj).handleOnFillRequest(((Integer) obj2).intValue(), (IBinder) obj3, ((Integer) obj4).intValue(), (ComponentName) obj5, (AutofillId) obj6, (AutofillValue) obj7, ((Long) obj8).longValue(), (IFillCallback) obj9);
                }
            }, AugmentedAutofillService.this, Integer.valueOf(sessionId), client, Integer.valueOf(taskId), componentName, focusedId, focusedValue, Long.valueOf(requestTime), callback));
        }

        @Override // android.service.autofill.augmented.IAugmentedAutofillService
        public void onDestroyAllFillWindowsRequest() {
            AugmentedAutofillService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: android.service.autofill.augmented.-$$Lambda$AugmentedAutofillService$1$LSvI4QN2NxJLegcZI0BFIvKwp6o
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((AugmentedAutofillService) obj).handleOnDestroyAllFillWindowsRequest();
                }
            }, AugmentedAutofillService.this));
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mHandler = new Handler(Looper.getMainLooper(), null, true);
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        this.mServiceComponentName = intent.getComponent();
        if (SERVICE_INTERFACE.equals(intent.getAction())) {
            return this.mInterface.asBinder();
        }
        String str = TAG;
        Log.w(str, "Tried to bind to wrong intent (should be android.service.autofill.augmented.AugmentedAutofillService: " + intent);
        return null;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: android.service.autofill.augmented.-$$Lambda$AugmentedAutofillService$zZAmNDLQX4rUV_yTGug25y4E6gA
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((AugmentedAutofillService) obj).handleOnUnbind();
            }
        }, this));
        return false;
    }

    public void onConnected() {
    }

    public void onFillRequest(FillRequest request, CancellationSignal cancellationSignal, FillController controller, FillCallback callback) {
    }

    public void onDisconnected() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnConnected(boolean debug, boolean verbose) {
        if (sDebug || debug) {
            String str = TAG;
            Log.d(str, "handleOnConnected(): debug=" + debug + ", verbose=" + verbose);
        }
        sDebug = debug;
        sVerbose = verbose;
        onConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnDisconnected() {
        onDisconnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnFillRequest(int sessionId, IBinder client, int taskId, ComponentName componentName, AutofillId focusedId, AutofillValue focusedValue, long requestTime, IFillCallback callback) {
        ICancellationSignal transport;
        IFillCallback iFillCallback;
        CancellationSignal cancellationSignal;
        AutofillProxy proxy;
        if (this.mAutofillProxies == null) {
            this.mAutofillProxies = new SparseArray<>();
        }
        ICancellationSignal transport2 = CancellationSignal.createTransport();
        CancellationSignal cancellationSignal2 = CancellationSignal.fromTransport(transport2);
        AutofillProxy proxy2 = this.mAutofillProxies.get(sessionId);
        if (proxy2 == null) {
            transport = transport2;
            AutofillProxy proxy3 = new AutofillProxy(sessionId, client, taskId, this.mServiceComponentName, componentName, focusedId, focusedValue, requestTime, callback, cancellationSignal2, null);
            this.mAutofillProxies.put(sessionId, proxy3);
            iFillCallback = callback;
            proxy = proxy3;
            cancellationSignal = cancellationSignal2;
        } else {
            transport = transport2;
            if (sDebug) {
                String str = TAG;
                Log.d(str, "Reusing proxy for session " + sessionId);
            }
            iFillCallback = callback;
            cancellationSignal = cancellationSignal2;
            proxy2.update(focusedId, focusedValue, iFillCallback, cancellationSignal);
            proxy = proxy2;
        }
        try {
            iFillCallback.onCancellable(transport);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
        onFillRequest(new FillRequest(proxy), cancellationSignal, new FillController(proxy), new FillCallback(proxy));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnDestroyAllFillWindowsRequest() {
        SparseArray<AutofillProxy> sparseArray = this.mAutofillProxies;
        if (sparseArray != null) {
            int size = sparseArray.size();
            for (int i = 0; i < size; i++) {
                int sessionId = this.mAutofillProxies.keyAt(i);
                AutofillProxy proxy = this.mAutofillProxies.valueAt(i);
                if (proxy == null) {
                    String str = TAG;
                    Log.w(str, "No proxy for session " + sessionId);
                    return;
                }
                if (proxy.mCallback != null) {
                    try {
                        if (!proxy.mCallback.isCompleted()) {
                            proxy.mCallback.cancel();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "failed to check current pending request status", e);
                    }
                }
                proxy.destroy();
            }
            this.mAutofillProxies.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnUnbind() {
        SparseArray<AutofillProxy> sparseArray = this.mAutofillProxies;
        if (sparseArray == null) {
            if (sDebug) {
                Log.d(TAG, "onUnbind(): no proxy to destroy");
                return;
            }
            return;
        }
        int size = sparseArray.size();
        if (sDebug) {
            String str = TAG;
            Log.d(str, "onUnbind(): destroying " + size + " proxies");
        }
        for (int i = 0; i < size; i++) {
            AutofillProxy proxy = this.mAutofillProxies.valueAt(i);
            try {
                proxy.destroy();
            } catch (Exception e) {
                String str2 = TAG;
                Log.w(str2, "error destroying " + proxy);
            }
        }
        this.mAutofillProxies = null;
    }

    @Override // android.app.Service
    protected final void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.print("Service component: ");
        pw.println(ComponentName.flattenToShortString(this.mServiceComponentName));
        SparseArray<AutofillProxy> sparseArray = this.mAutofillProxies;
        if (sparseArray != null) {
            int size = sparseArray.size();
            pw.print("Number proxies: ");
            pw.println(size);
            for (int i = 0; i < size; i++) {
                int sessionId = this.mAutofillProxies.keyAt(i);
                AutofillProxy proxy = this.mAutofillProxies.valueAt(i);
                pw.print(i);
                pw.print(") SessionId=");
                pw.print(sessionId);
                pw.println(SettingsStringUtil.DELIMITER);
                proxy.dump("  ", pw);
            }
        }
        dump(pw, args);
    }

    protected void dump(PrintWriter pw, String[] args) {
        pw.print(getClass().getName());
        pw.println(": nothing to dump");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class AutofillProxy {
        static final int REPORT_EVENT_NO_RESPONSE = 1;
        static final int REPORT_EVENT_UI_DESTROYED = 3;
        static final int REPORT_EVENT_UI_SHOWN = 2;
        public final ComponentName componentName;
        @GuardedBy({"mLock"})
        private IFillCallback mCallback;
        private CancellationSignal mCancellationSignal;
        private final IAugmentedAutofillManagerClient mClient;
        @GuardedBy({"mLock"})
        private FillWindow mFillWindow;
        private long mFirstOnSuccessTime;
        private final long mFirstRequestTime;
        @GuardedBy({"mLock"})
        private AutofillId mFocusedId;
        @GuardedBy({"mLock"})
        private AutofillValue mFocusedValue;
        @GuardedBy({"mLock"})
        private AutofillId mLastShownId;
        private final Object mLock;
        private String mServicePackageName;
        private final int mSessionId;
        @GuardedBy({"mLock"})
        private PresentationParams.SystemPopupPresentationParams mSmartSuggestion;
        private long mUiFirstDestroyedTime;
        private long mUiFirstShownTime;
        public final int taskId;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        @interface ReportEvent {
        }

        /* synthetic */ AutofillProxy(int x0, IBinder x1, int x2, ComponentName x3, ComponentName x4, AutofillId x5, AutofillValue x6, long x7, IFillCallback x8, CancellationSignal x9, AnonymousClass1 x10) {
            this(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9);
        }

        private AutofillProxy(int sessionId, IBinder client, int taskId, ComponentName serviceComponentName, ComponentName componentName, AutofillId focusedId, AutofillValue focusedValue, long requestTime, IFillCallback callback, CancellationSignal cancellationSignal) {
            this.mLock = new Object();
            this.mSessionId = sessionId;
            this.mClient = IAugmentedAutofillManagerClient.Stub.asInterface(client);
            this.mCallback = callback;
            this.taskId = taskId;
            this.componentName = componentName;
            this.mServicePackageName = serviceComponentName.getPackageName();
            this.mFocusedId = focusedId;
            this.mFocusedValue = focusedValue;
            this.mFirstRequestTime = requestTime;
            this.mCancellationSignal = cancellationSignal;
        }

        public PresentationParams.SystemPopupPresentationParams getSmartSuggestionParams() {
            synchronized (this.mLock) {
                if (this.mSmartSuggestion != null && this.mFocusedId.equals(this.mLastShownId)) {
                    return this.mSmartSuggestion;
                }
                try {
                    Rect rect = this.mClient.getViewCoordinates(this.mFocusedId);
                    if (rect == null) {
                        if (AugmentedAutofillService.sDebug) {
                            String str = AugmentedAutofillService.TAG;
                            Log.d(str, "getViewCoordinates(" + this.mFocusedId + ") returned null");
                        }
                        return null;
                    }
                    this.mSmartSuggestion = new PresentationParams.SystemPopupPresentationParams(this, rect);
                    this.mLastShownId = this.mFocusedId;
                    return this.mSmartSuggestion;
                } catch (RemoteException e) {
                    String str2 = AugmentedAutofillService.TAG;
                    Log.w(str2, "Could not get coordinates for " + this.mFocusedId);
                    return null;
                }
            }
        }

        public void autofill(List<Pair<AutofillId, AutofillValue>> pairs) throws RemoteException {
            int size = pairs.size();
            List<AutofillId> ids = new ArrayList<>(size);
            List<AutofillValue> values = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Pair<AutofillId, AutofillValue> pair = pairs.get(i);
                ids.add(pair.first);
                values.add(pair.second);
            }
            this.mClient.autofill(this.mSessionId, ids, values);
        }

        public void setFillWindow(FillWindow fillWindow) {
            synchronized (this.mLock) {
                this.mFillWindow = fillWindow;
            }
        }

        public FillWindow getFillWindow() {
            FillWindow fillWindow;
            synchronized (this.mLock) {
                fillWindow = this.mFillWindow;
            }
            return fillWindow;
        }

        public void requestShowFillUi(int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) throws RemoteException {
            if (this.mCancellationSignal.isCanceled()) {
                if (AugmentedAutofillService.sVerbose) {
                    Log.v(AugmentedAutofillService.TAG, "requestShowFillUi() not showing because request is cancelled");
                    return;
                }
                return;
            }
            this.mClient.requestShowFillUi(this.mSessionId, this.mFocusedId, width, height, anchorBounds, presenter);
        }

        public void requestHideFillUi() throws RemoteException {
            this.mClient.requestHideFillUi(this.mSessionId, this.mFocusedId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void update(AutofillId focusedId, AutofillValue focusedValue, IFillCallback callback, CancellationSignal cancellationSignal) {
            synchronized (this.mLock) {
                this.mFocusedId = focusedId;
                this.mFocusedValue = focusedValue;
                if (this.mCallback != null) {
                    try {
                        if (!this.mCallback.isCompleted()) {
                            this.mCallback.cancel();
                        }
                    } catch (RemoteException e) {
                        Log.e(AugmentedAutofillService.TAG, "failed to check current pending request status", e);
                    }
                    Log.d(AugmentedAutofillService.TAG, "mCallback is updated.");
                }
                this.mCallback = callback;
                this.mCancellationSignal = cancellationSignal;
            }
        }

        public AutofillId getFocusedId() {
            AutofillId autofillId;
            synchronized (this.mLock) {
                autofillId = this.mFocusedId;
            }
            return autofillId;
        }

        public AutofillValue getFocusedValue() {
            AutofillValue autofillValue;
            synchronized (this.mLock) {
                autofillValue = this.mFocusedValue;
            }
            return autofillValue;
        }

        public void report(int event) {
            if (AugmentedAutofillService.sVerbose) {
                String str = AugmentedAutofillService.TAG;
                Log.v(str, "report(): " + event);
            }
            long duration = -1;
            int type = 0;
            if (event == 1) {
                type = 10;
                if (this.mFirstOnSuccessTime == 0) {
                    this.mFirstOnSuccessTime = SystemClock.elapsedRealtime();
                    duration = this.mFirstOnSuccessTime - this.mFirstRequestTime;
                    if (AugmentedAutofillService.sDebug) {
                        String str2 = AugmentedAutofillService.TAG;
                        Log.d(str2, "Service responded nothing in " + TimeUtils.formatDuration(duration));
                    }
                }
                try {
                    this.mCallback.onSuccess();
                } catch (RemoteException e) {
                    String str3 = AugmentedAutofillService.TAG;
                    Log.e(str3, "Error reporting success: " + e);
                }
            } else if (event == 2) {
                type = 1;
                if (this.mUiFirstShownTime == 0) {
                    this.mUiFirstShownTime = SystemClock.elapsedRealtime();
                    duration = this.mUiFirstShownTime - this.mFirstRequestTime;
                    if (AugmentedAutofillService.sDebug) {
                        String str4 = AugmentedAutofillService.TAG;
                        Log.d(str4, "UI shown in " + TimeUtils.formatDuration(duration));
                    }
                }
            } else if (event != 3) {
                String str5 = AugmentedAutofillService.TAG;
                Log.w(str5, "invalid event reported: " + event);
            } else {
                type = 2;
                if (this.mUiFirstDestroyedTime == 0) {
                    this.mUiFirstDestroyedTime = SystemClock.elapsedRealtime();
                    duration = this.mUiFirstDestroyedTime - this.mFirstRequestTime;
                    if (AugmentedAutofillService.sDebug) {
                        String str6 = AugmentedAutofillService.TAG;
                        Log.d(str6, "UI destroyed in " + TimeUtils.formatDuration(duration));
                    }
                }
            }
            Helper.logResponse(type, this.mServicePackageName, this.componentName, this.mSessionId, duration);
        }

        public void dump(String prefix, PrintWriter pw) {
            pw.print(prefix);
            pw.print("sessionId: ");
            pw.println(this.mSessionId);
            pw.print(prefix);
            pw.print("taskId: ");
            pw.println(this.taskId);
            pw.print(prefix);
            pw.print("component: ");
            pw.println(this.componentName.flattenToShortString());
            pw.print(prefix);
            pw.print("focusedId: ");
            pw.println(this.mFocusedId);
            if (this.mFocusedValue != null) {
                pw.print(prefix);
                pw.print("focusedValue: ");
                pw.println(this.mFocusedValue);
            }
            if (this.mLastShownId != null) {
                pw.print(prefix);
                pw.print("lastShownId: ");
                pw.println(this.mLastShownId);
            }
            pw.print(prefix);
            pw.print("client: ");
            pw.println(this.mClient);
            String prefix2 = prefix + "  ";
            if (this.mFillWindow != null) {
                pw.print(prefix);
                pw.println("window:");
                this.mFillWindow.dump(prefix2, pw);
            }
            if (this.mSmartSuggestion != null) {
                pw.print(prefix);
                pw.println("smartSuggestion:");
                this.mSmartSuggestion.dump(prefix2, pw);
            }
            long j = this.mFirstOnSuccessTime;
            if (j > 0) {
                pw.print(prefix);
                pw.print("response time: ");
                TimeUtils.formatDuration(j - this.mFirstRequestTime, pw);
                pw.println();
            }
            long responseTime = this.mUiFirstShownTime;
            if (responseTime > 0) {
                pw.print(prefix);
                pw.print("UI rendering time: ");
                TimeUtils.formatDuration(responseTime - this.mFirstRequestTime, pw);
                pw.println();
            }
            long uiRenderingTime = this.mUiFirstDestroyedTime;
            if (uiRenderingTime > 0) {
                long uiTotalTime = uiRenderingTime - this.mFirstRequestTime;
                pw.print(prefix);
                pw.print("UI life time: ");
                TimeUtils.formatDuration(uiTotalTime, pw);
                pw.println();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void destroy() {
            synchronized (this.mLock) {
                if (this.mFillWindow != null) {
                    if (AugmentedAutofillService.sDebug) {
                        Log.d(AugmentedAutofillService.TAG, "destroying window");
                    }
                    this.mFillWindow.destroy();
                    this.mFillWindow = null;
                }
            }
        }
    }
}
