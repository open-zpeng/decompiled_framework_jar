package android.appwidget;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.widget.RemoteViews;
import com.android.internal.R;
import com.android.internal.appwidget.IAppWidgetHost;
import com.android.internal.appwidget.IAppWidgetService;
import java.lang.ref.WeakReference;
import java.util.List;
/* loaded from: classes.dex */
public class AppWidgetHost {
    static final int HANDLE_PROVIDERS_CHANGED = 3;
    static final int HANDLE_PROVIDER_CHANGED = 2;
    static final int HANDLE_UPDATE = 1;
    public private protected static final int HANDLE_VIEW_DATA_CHANGED = 4;
    public private protected static IAppWidgetService sService;
    private final Callbacks mCallbacks;
    private String mContextOpPackageName;
    private DisplayMetrics mDisplayMetrics;
    public protected final Handler mHandler;
    private final int mHostId;
    private RemoteViews.OnClickHandler mOnClickHandler;
    private final SparseArray<AppWidgetHostView> mViews;
    static final Object sServiceLock = new Object();
    static boolean sServiceInitialized = false;

    /* loaded from: classes.dex */
    static class Callbacks extends IAppWidgetHost.Stub {
        private final WeakReference<Handler> mWeakHandler;

        public synchronized Callbacks(Handler handler) {
            this.mWeakHandler = new WeakReference<>(handler);
        }

        public synchronized void updateAppWidget(int appWidgetId, RemoteViews views) {
            if (isLocalBinder() && views != null) {
                views = views.mo11clone();
            }
            Handler handler = this.mWeakHandler.get();
            if (handler == null) {
                return;
            }
            Message msg = handler.obtainMessage(1, appWidgetId, 0, views);
            msg.sendToTarget();
        }

        public synchronized void providerChanged(int appWidgetId, AppWidgetProviderInfo info) {
            if (isLocalBinder() && info != null) {
                info = info.m15clone();
            }
            Handler handler = this.mWeakHandler.get();
            if (handler == null) {
                return;
            }
            Message msg = handler.obtainMessage(2, appWidgetId, 0, info);
            msg.sendToTarget();
        }

        @Override // com.android.internal.appwidget.IAppWidgetHost
        public synchronized void providersChanged() {
            Handler handler = this.mWeakHandler.get();
            if (handler == null) {
                return;
            }
            handler.obtainMessage(3).sendToTarget();
        }

        @Override // com.android.internal.appwidget.IAppWidgetHost
        public synchronized void viewDataChanged(int appWidgetId, int viewId) {
            Handler handler = this.mWeakHandler.get();
            if (handler == null) {
                return;
            }
            Message msg = handler.obtainMessage(4, appWidgetId, viewId);
            msg.sendToTarget();
        }

        private static synchronized boolean isLocalBinder() {
            return Process.myPid() == Binder.getCallingPid();
        }
    }

    /* loaded from: classes.dex */
    class UpdateHandler extends Handler {
        public UpdateHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    AppWidgetHost.this.updateAppWidgetView(msg.arg1, (RemoteViews) msg.obj);
                    return;
                case 2:
                    AppWidgetHost.this.onProviderChanged(msg.arg1, (AppWidgetProviderInfo) msg.obj);
                    return;
                case 3:
                    AppWidgetHost.this.onProvidersChanged();
                    return;
                case 4:
                    AppWidgetHost.this.viewDataChanged(msg.arg1, msg.arg2);
                    return;
                default:
                    return;
            }
        }
    }

    public AppWidgetHost(Context context, int hostId) {
        this(context, hostId, null, context.getMainLooper());
    }

    private protected AppWidgetHost(Context context, int hostId, RemoteViews.OnClickHandler handler, Looper looper) {
        this.mViews = new SparseArray<>();
        this.mContextOpPackageName = context.getOpPackageName();
        this.mHostId = hostId;
        this.mOnClickHandler = handler;
        this.mHandler = new UpdateHandler(looper);
        this.mCallbacks = new Callbacks(this.mHandler);
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        bindService(context);
    }

    private static synchronized void bindService(Context context) {
        synchronized (sServiceLock) {
            if (sServiceInitialized) {
                return;
            }
            sServiceInitialized = true;
            PackageManager packageManager = context.getPackageManager();
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_APP_WIDGETS) || context.getResources().getBoolean(R.bool.config_enableAppWidgetService)) {
                IBinder b = ServiceManager.getService(Context.APPWIDGET_SERVICE);
                sService = IAppWidgetService.Stub.asInterface(b);
            }
        }
    }

    public void startListening() {
        int[] idsToUpdate;
        int i;
        if (sService == null) {
            return;
        }
        synchronized (this.mViews) {
            int N = this.mViews.size();
            idsToUpdate = new int[N];
            for (int i2 = 0; i2 < N; i2++) {
                idsToUpdate[i2] = this.mViews.keyAt(i2);
            }
        }
        try {
            List<PendingHostUpdate> updates = sService.startListening(this.mCallbacks, this.mContextOpPackageName, this.mHostId, idsToUpdate).getList();
            int N2 = updates.size();
            for (i = 0; i < N2; i++) {
                PendingHostUpdate update = updates.get(i);
                switch (update.type) {
                    case 0:
                        updateAppWidgetView(update.appWidgetId, update.views);
                        break;
                    case 1:
                        onProviderChanged(update.appWidgetId, update.widgetInfo);
                        break;
                    case 2:
                        viewDataChanged(update.appWidgetId, update.viewId);
                        break;
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    public void stopListening() {
        if (sService == null) {
            return;
        }
        try {
            sService.stopListening(this.mContextOpPackageName, this.mHostId);
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    public int allocateAppWidgetId() {
        if (sService == null) {
            return -1;
        }
        try {
            return sService.allocateAppWidgetId(this.mContextOpPackageName, this.mHostId);
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    public final void startAppWidgetConfigureActivityForResult(Activity activity, int appWidgetId, int intentFlags, int requestCode, Bundle options) {
        if (sService == null) {
            return;
        }
        try {
            IntentSender intentSender = sService.createAppWidgetConfigIntentSender(this.mContextOpPackageName, appWidgetId, intentFlags);
            if (intentSender != null) {
                activity.startIntentSenderForResult(intentSender, requestCode, null, 0, 0, 0, options);
                return;
            }
            throw new ActivityNotFoundException();
        } catch (IntentSender.SendIntentException e) {
            throw new ActivityNotFoundException();
        } catch (RemoteException e2) {
            throw new RuntimeException("system server dead?", e2);
        }
    }

    public int[] getAppWidgetIds() {
        if (sService == null) {
            return new int[0];
        }
        try {
            return sService.getAppWidgetIdsForHost(this.mContextOpPackageName, this.mHostId);
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    public void deleteAppWidgetId(int appWidgetId) {
        if (sService == null) {
            return;
        }
        synchronized (this.mViews) {
            this.mViews.remove(appWidgetId);
            try {
                sService.deleteAppWidgetId(this.mContextOpPackageName, appWidgetId);
            } catch (RemoteException e) {
                throw new RuntimeException("system server dead?", e);
            }
        }
    }

    public void deleteHost() {
        if (sService == null) {
            return;
        }
        try {
            sService.deleteHost(this.mContextOpPackageName, this.mHostId);
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    public static void deleteAllHosts() {
        if (sService == null) {
            return;
        }
        try {
            sService.deleteAllHosts();
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    public final AppWidgetHostView createView(Context context, int appWidgetId, AppWidgetProviderInfo appWidget) {
        if (sService == null) {
            return null;
        }
        AppWidgetHostView view = onCreateView(context, appWidgetId, appWidget);
        view.setOnClickHandler(this.mOnClickHandler);
        view.setAppWidget(appWidgetId, appWidget);
        synchronized (this.mViews) {
            this.mViews.put(appWidgetId, view);
        }
        try {
            RemoteViews views = sService.getAppWidgetViews(this.mContextOpPackageName, appWidgetId);
            view.updateAppWidget(views);
            return view;
        } catch (RemoteException e) {
            throw new RuntimeException("system server dead?", e);
        }
    }

    protected AppWidgetHostView onCreateView(Context context, int appWidgetId, AppWidgetProviderInfo appWidget) {
        return new AppWidgetHostView(context, this.mOnClickHandler);
    }

    protected void onProviderChanged(int appWidgetId, AppWidgetProviderInfo appWidget) {
        AppWidgetHostView v;
        appWidget.updateDimensions(this.mDisplayMetrics);
        synchronized (this.mViews) {
            v = this.mViews.get(appWidgetId);
        }
        if (v != null) {
            v.resetAppWidget(appWidget);
        }
    }

    protected void onProvidersChanged() {
    }

    synchronized void updateAppWidgetView(int appWidgetId, RemoteViews views) {
        AppWidgetHostView v;
        synchronized (this.mViews) {
            v = this.mViews.get(appWidgetId);
        }
        if (v != null) {
            v.updateAppWidget(views);
        }
    }

    synchronized void viewDataChanged(int appWidgetId, int viewId) {
        AppWidgetHostView v;
        synchronized (this.mViews) {
            v = this.mViews.get(appWidgetId);
        }
        if (v != null) {
            v.viewDataChanged(viewId);
        }
    }

    protected void clearViews() {
        synchronized (this.mViews) {
            this.mViews.clear();
        }
    }
}
