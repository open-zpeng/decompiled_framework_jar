package android.widget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.android.internal.widget.IRemoteViewsFactory;
import java.util.HashMap;
/* loaded from: classes3.dex */
public abstract class RemoteViewsService extends Service {
    private static final String LOG_TAG = "RemoteViewsService";
    private static final HashMap<Intent.FilterComparison, RemoteViewsFactory> sRemoteViewFactories = new HashMap<>();
    private static final Object sLock = new Object();

    /* loaded from: classes3.dex */
    public interface RemoteViewsFactory {
        int getCount();

        long getItemId(int i);

        RemoteViews getLoadingView();

        RemoteViews getViewAt(int i);

        int getViewTypeCount();

        boolean hasStableIds();

        void onCreate();

        void onDataSetChanged();

        void onDestroy();
    }

    public abstract RemoteViewsFactory onGetViewFactory(Intent intent);

    /* loaded from: classes3.dex */
    private static class RemoteViewsFactoryAdapter extends IRemoteViewsFactory.Stub {
        private RemoteViewsFactory mFactory;
        private boolean mIsCreated;

        public synchronized RemoteViewsFactoryAdapter(RemoteViewsFactory factory, boolean isCreated) {
            this.mFactory = factory;
            this.mIsCreated = isCreated;
        }

        public synchronized boolean isCreated() {
            return this.mIsCreated;
        }

        public synchronized void onDataSetChanged() {
            try {
                this.mFactory.onDataSetChanged();
            } catch (Exception ex) {
                Thread t = Thread.currentThread();
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
        }

        @Override // com.android.internal.widget.IRemoteViewsFactory
        public synchronized void onDataSetChangedAsync() {
            onDataSetChanged();
        }

        public synchronized int getCount() {
            int count;
            count = 0;
            try {
                count = this.mFactory.getCount();
            } catch (Exception ex) {
                Thread t = Thread.currentThread();
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return count;
        }

        public synchronized RemoteViews getViewAt(int position) {
            RemoteViews rv;
            rv = null;
            try {
                rv = this.mFactory.getViewAt(position);
                if (rv != null) {
                    rv.setIsWidgetCollectionChild(true);
                }
            } catch (Exception ex) {
                Thread t = Thread.currentThread();
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return rv;
        }

        public synchronized RemoteViews getLoadingView() {
            RemoteViews rv;
            rv = null;
            try {
                rv = this.mFactory.getLoadingView();
            } catch (Exception ex) {
                Thread t = Thread.currentThread();
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return rv;
        }

        public synchronized int getViewTypeCount() {
            int count;
            count = 0;
            try {
                count = this.mFactory.getViewTypeCount();
            } catch (Exception ex) {
                Thread t = Thread.currentThread();
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return count;
        }

        public synchronized long getItemId(int position) {
            long id;
            id = 0;
            try {
                id = this.mFactory.getItemId(position);
            } catch (Exception ex) {
                Thread t = Thread.currentThread();
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return id;
        }

        public synchronized boolean hasStableIds() {
            boolean hasStableIds;
            hasStableIds = false;
            try {
                hasStableIds = this.mFactory.hasStableIds();
            } catch (Exception ex) {
                Thread t = Thread.currentThread();
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
            }
            return hasStableIds;
        }

        @Override // com.android.internal.widget.IRemoteViewsFactory
        public synchronized void onDestroy(Intent intent) {
            synchronized (RemoteViewsService.sLock) {
                Intent.FilterComparison fc = new Intent.FilterComparison(intent);
                if (RemoteViewsService.sRemoteViewFactories.containsKey(fc)) {
                    RemoteViewsFactory factory = (RemoteViewsFactory) RemoteViewsService.sRemoteViewFactories.get(fc);
                    try {
                        factory.onDestroy();
                    } catch (Exception ex) {
                        Thread t = Thread.currentThread();
                        Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, ex);
                    }
                    RemoteViewsService.sRemoteViewFactories.remove(fc);
                }
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        RemoteViewsFactory factory;
        boolean isCreated;
        RemoteViewsFactoryAdapter remoteViewsFactoryAdapter;
        synchronized (sLock) {
            Intent.FilterComparison fc = new Intent.FilterComparison(intent);
            if (!sRemoteViewFactories.containsKey(fc)) {
                factory = onGetViewFactory(intent);
                sRemoteViewFactories.put(fc, factory);
                factory.onCreate();
                isCreated = false;
            } else {
                factory = sRemoteViewFactories.get(fc);
                isCreated = true;
            }
            remoteViewsFactoryAdapter = new RemoteViewsFactoryAdapter(factory, isCreated);
        }
        return remoteViewsFactoryAdapter;
    }
}
