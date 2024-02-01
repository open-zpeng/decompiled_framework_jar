package android.appwidget;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViewsAdapter;
import android.widget.TextView;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class AppWidgetHostView extends FrameLayout {
    private static final LayoutInflater.Filter INFLATER_FILTER = new LayoutInflater.Filter() { // from class: android.appwidget.-$$Lambda$AppWidgetHostView$AzPWN1sIsRb7M-0Ss1rK2mksT-o
        @Override // android.view.LayoutInflater.Filter
        public final boolean onLoadClass(Class cls) {
            boolean isAnnotationPresent;
            isAnnotationPresent = cls.isAnnotationPresent(RemoteViews.RemoteView.class);
            return isAnnotationPresent;
        }
    };
    private static final String KEY_JAILED_ARRAY = "jail";
    static final boolean LOGD = false;
    static final String TAG = "AppWidgetHostView";
    static final int VIEW_MODE_CONTENT = 1;
    static final int VIEW_MODE_DEFAULT = 3;
    static final int VIEW_MODE_ERROR = 2;
    static final int VIEW_MODE_NOINIT = 0;
    public private protected int mAppWidgetId;
    private Executor mAsyncExecutor;
    Context mContext;
    public private protected AppWidgetProviderInfo mInfo;
    private CancellationSignal mLastExecutionSignal;
    int mLayoutId;
    private RemoteViews.OnClickHandler mOnClickHandler;
    Context mRemoteContext;
    View mView;
    int mViewMode;

    public AppWidgetHostView(Context context) {
        this(context, R.anim.fade_in, 17432577);
    }

    public synchronized AppWidgetHostView(Context context, RemoteViews.OnClickHandler handler) {
        this(context, R.anim.fade_in, 17432577);
        this.mOnClickHandler = handler;
    }

    public AppWidgetHostView(Context context, int animationIn, int animationOut) {
        super(context);
        this.mViewMode = 0;
        this.mLayoutId = -1;
        this.mContext = context;
        setIsRootNamespace(true);
    }

    public synchronized void setOnClickHandler(RemoteViews.OnClickHandler handler) {
        this.mOnClickHandler = handler;
    }

    public void setAppWidget(int appWidgetId, AppWidgetProviderInfo info) {
        this.mAppWidgetId = appWidgetId;
        this.mInfo = info;
        Rect padding = getDefaultPadding();
        setPadding(padding.left, padding.top, padding.right, padding.bottom);
        if (info != null) {
            String description = info.loadLabel(getContext().getPackageManager());
            if ((info.providerInfo.applicationInfo.flags & 1073741824) != 0) {
                description = Resources.getSystem().getString(com.android.internal.R.string.suspended_widget_accessibility, description);
            }
            setContentDescription(description);
        }
    }

    public static Rect getDefaultPaddingForWidget(Context context, ComponentName component, Rect padding) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(component.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return getDefaultPaddingForWidget(context, appInfo, padding);
    }

    public protected static Rect getDefaultPaddingForWidget(Context context, ApplicationInfo appInfo, Rect padding) {
        if (padding == null) {
            padding = new Rect(0, 0, 0, 0);
        } else {
            padding.set(0, 0, 0, 0);
        }
        if (appInfo != null && appInfo.targetSdkVersion >= 14) {
            Resources r = context.getResources();
            padding.left = r.getDimensionPixelSize(com.android.internal.R.dimen.default_app_widget_padding_left);
            padding.right = r.getDimensionPixelSize(com.android.internal.R.dimen.default_app_widget_padding_right);
            padding.top = r.getDimensionPixelSize(com.android.internal.R.dimen.default_app_widget_padding_top);
            padding.bottom = r.getDimensionPixelSize(com.android.internal.R.dimen.default_app_widget_padding_bottom);
        }
        return padding;
    }

    private synchronized Rect getDefaultPadding() {
        return getDefaultPaddingForWidget(this.mContext, this.mInfo == null ? null : this.mInfo.providerInfo.applicationInfo, (Rect) null);
    }

    public int getAppWidgetId() {
        return this.mAppWidgetId;
    }

    public AppWidgetProviderInfo getAppWidgetInfo() {
        return this.mInfo;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        SparseArray<Parcelable> jail = new SparseArray<>();
        super.dispatchSaveInstanceState(jail);
        Bundle bundle = new Bundle();
        bundle.putSparseParcelableArray(KEY_JAILED_ARRAY, jail);
        container.put(generateId(), bundle);
    }

    private synchronized int generateId() {
        int id = getId();
        return id == -1 ? this.mAppWidgetId : id;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        Parcelable parcelable = container.get(generateId());
        SparseArray<Parcelable> jail = null;
        if (parcelable instanceof Bundle) {
            jail = ((Bundle) parcelable).getSparseParcelableArray(KEY_JAILED_ARRAY);
        }
        if (jail == null) {
            jail = new SparseArray<>();
        }
        try {
            super.dispatchRestoreInstanceState(jail);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("failed to restoreInstanceState for widget id: ");
            sb.append(this.mAppWidgetId);
            sb.append(", ");
            sb.append(this.mInfo == null ? "null" : this.mInfo.provider);
            Log.e(TAG, sb.toString(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        try {
            super.onLayout(changed, left, top, right, bottom);
        } catch (RuntimeException e) {
            Log.e(TAG, "Remote provider threw runtime exception, using error view instead.", e);
            removeViewInLayout(this.mView);
            View child = getErrorView();
            prepareView(child);
            addViewInLayout(child, 0, child.getLayoutParams());
            measureChild(child, View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
            child.layout(0, 0, child.getMeasuredWidth() + this.mPaddingLeft + this.mPaddingRight, child.getMeasuredHeight() + this.mPaddingTop + this.mPaddingBottom);
            this.mView = child;
            this.mViewMode = 2;
        }
    }

    public void updateAppWidgetSize(Bundle newOptions, int minWidth, int minHeight, int maxWidth, int maxHeight) {
        updateAppWidgetSize(newOptions, minWidth, minHeight, maxWidth, maxHeight, false);
    }

    private protected void updateAppWidgetSize(Bundle newOptions, int minWidth, int minHeight, int maxWidth, int maxHeight, boolean ignorePadding) {
        Bundle newOptions2;
        if (newOptions == null) {
            newOptions2 = new Bundle();
        } else {
            newOptions2 = newOptions;
        }
        Rect padding = getDefaultPadding();
        float density = getResources().getDisplayMetrics().density;
        int xPaddingDips = (int) ((padding.left + padding.right) / density);
        int yPaddingDips = (int) ((padding.top + padding.bottom) / density);
        int newMinWidth = minWidth - (ignorePadding ? 0 : xPaddingDips);
        int newMinHeight = minHeight - (ignorePadding ? 0 : yPaddingDips);
        int newMaxWidth = maxWidth - (ignorePadding ? 0 : xPaddingDips);
        int newMaxHeight = maxHeight - (ignorePadding ? 0 : yPaddingDips);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this.mContext);
        Bundle oldOptions = widgetManager.getAppWidgetOptions(this.mAppWidgetId);
        boolean needsUpdate = false;
        needsUpdate = (newMinWidth == oldOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) && newMinHeight == oldOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT) && newMaxWidth == oldOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH) && newMaxHeight == oldOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)) ? true : true;
        if (needsUpdate) {
            newOptions2.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, newMinWidth);
            newOptions2.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, newMinHeight);
            newOptions2.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, newMaxWidth);
            newOptions2.putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, newMaxHeight);
            updateAppWidgetOptions(newOptions2);
        }
    }

    public void updateAppWidgetOptions(Bundle options) {
        AppWidgetManager.getInstance(this.mContext).updateAppWidgetOptions(this.mAppWidgetId, options);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        Context context = this.mRemoteContext != null ? this.mRemoteContext : this.mContext;
        return new FrameLayout.LayoutParams(context, attrs);
    }

    public void setExecutor(Executor executor) {
        if (this.mLastExecutionSignal != null) {
            this.mLastExecutionSignal.cancel();
            this.mLastExecutionSignal = null;
        }
        this.mAsyncExecutor = executor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void resetAppWidget(AppWidgetProviderInfo info) {
        setAppWidget(this.mAppWidgetId, info);
        this.mViewMode = 0;
        updateAppWidget(null);
    }

    public void updateAppWidget(RemoteViews remoteViews) {
        applyRemoteViews(remoteViews, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void applyRemoteViews(RemoteViews remoteViews, boolean useAsyncIfPossible) {
        boolean recycled = false;
        View content = null;
        Exception exception = null;
        if (this.mLastExecutionSignal != null) {
            this.mLastExecutionSignal.cancel();
            this.mLastExecutionSignal = null;
        }
        if (remoteViews == null) {
            if (this.mViewMode == 3) {
                return;
            }
            content = getDefaultView();
            this.mLayoutId = -1;
            this.mViewMode = 3;
        } else if (this.mAsyncExecutor != null && useAsyncIfPossible) {
            inflateAsync(remoteViews);
            return;
        } else {
            this.mRemoteContext = getRemoteContext();
            int layoutId = remoteViews.getLayoutId();
            if (0 == 0 && layoutId == this.mLayoutId) {
                try {
                    remoteViews.reapply(this.mContext, this.mView, this.mOnClickHandler);
                    content = this.mView;
                    recycled = true;
                } catch (RuntimeException e) {
                    exception = e;
                }
            }
            if (content == null) {
                try {
                    content = remoteViews.apply(this.mContext, this, this.mOnClickHandler);
                } catch (RuntimeException e2) {
                    exception = e2;
                }
            }
            this.mLayoutId = layoutId;
            this.mViewMode = 1;
        }
        applyContent(content, recycled, exception);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void applyContent(View content, boolean recycled, Exception exception) {
        if (content == null) {
            if (this.mViewMode == 2) {
                return;
            }
            if (exception != null) {
                Log.w(TAG, "Error inflating RemoteViews : " + exception.toString());
            }
            content = getErrorView();
            this.mViewMode = 2;
        }
        if (!recycled) {
            prepareView(content);
            addView(content);
        }
        if (this.mView != content) {
            removeView(this.mView);
            this.mView = content;
        }
    }

    private synchronized void inflateAsync(RemoteViews remoteViews) {
        this.mRemoteContext = getRemoteContext();
        int layoutId = remoteViews.getLayoutId();
        if (layoutId == this.mLayoutId && this.mView != null) {
            try {
                this.mLastExecutionSignal = remoteViews.reapplyAsync(this.mContext, this.mView, this.mAsyncExecutor, new ViewApplyListener(remoteViews, layoutId, true), this.mOnClickHandler);
            } catch (Exception e) {
            }
        }
        if (this.mLastExecutionSignal == null) {
            this.mLastExecutionSignal = remoteViews.applyAsync(this.mContext, this, this.mAsyncExecutor, new ViewApplyListener(remoteViews, layoutId, false), this.mOnClickHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ViewApplyListener implements RemoteViews.OnViewAppliedListener {
        private final boolean mIsReapply;
        private final int mLayoutId;
        private final RemoteViews mViews;

        public ViewApplyListener(RemoteViews views, int layoutId, boolean isReapply) {
            this.mViews = views;
            this.mLayoutId = layoutId;
            this.mIsReapply = isReapply;
        }

        @Override // android.widget.RemoteViews.OnViewAppliedListener
        public synchronized void onViewApplied(View v) {
            AppWidgetHostView.this.mLayoutId = this.mLayoutId;
            AppWidgetHostView.this.mViewMode = 1;
            AppWidgetHostView.this.applyContent(v, this.mIsReapply, null);
        }

        @Override // android.widget.RemoteViews.OnViewAppliedListener
        public synchronized void onError(Exception e) {
            if (!this.mIsReapply) {
                AppWidgetHostView.this.applyContent(null, false, e);
                return;
            }
            AppWidgetHostView.this.mLastExecutionSignal = this.mViews.applyAsync(AppWidgetHostView.this.mContext, AppWidgetHostView.this, AppWidgetHostView.this.mAsyncExecutor, new ViewApplyListener(this.mViews, this.mLayoutId, false), AppWidgetHostView.this.mOnClickHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void viewDataChanged(int viewId) {
        View v = findViewById(viewId);
        if (v != null && (v instanceof AdapterView)) {
            AdapterView<?> adapterView = (AdapterView) v;
            Adapter adapter = adapterView.getAdapter();
            if (adapter instanceof BaseAdapter) {
                BaseAdapter baseAdapter = (BaseAdapter) adapter;
                baseAdapter.notifyDataSetChanged();
            } else if (adapter == null && (adapterView instanceof RemoteViewsAdapter.RemoteAdapterConnectionCallback)) {
                ((RemoteViewsAdapter.RemoteAdapterConnectionCallback) adapterView).deferNotifyDataSetChanged();
            }
        }
    }

    protected synchronized Context getRemoteContext() {
        try {
            return this.mContext.createApplicationContext(this.mInfo.providerInfo.applicationInfo, 4);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Package name " + this.mInfo.providerInfo.packageName + " not found");
            return this.mContext;
        }
    }

    protected void prepareView(View view) {
        FrameLayout.LayoutParams requested = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (requested == null) {
            requested = new FrameLayout.LayoutParams(-1, -1);
        }
        requested.gravity = 17;
        view.setLayoutParams(requested);
    }

    protected View getDefaultView() {
        View defaultView = null;
        Exception exception = null;
        try {
            if (this.mInfo != null) {
                Context theirContext = getRemoteContext();
                this.mRemoteContext = theirContext;
                LayoutInflater inflater = ((LayoutInflater) theirContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).cloneInContext(theirContext);
                inflater.setFilter(INFLATER_FILTER);
                AppWidgetManager manager = AppWidgetManager.getInstance(this.mContext);
                Bundle options = manager.getAppWidgetOptions(this.mAppWidgetId);
                int layoutId = this.mInfo.initialLayout;
                if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY)) {
                    int category = options.getInt(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY);
                    if (category == 2) {
                        int kgLayoutId = this.mInfo.initialKeyguardLayout;
                        layoutId = kgLayoutId == 0 ? layoutId : kgLayoutId;
                    }
                }
                defaultView = inflater.inflate(layoutId, (ViewGroup) this, false);
            } else {
                Log.w(TAG, "can't inflate defaultView because mInfo is missing");
            }
        } catch (RuntimeException e) {
            exception = e;
        }
        if (exception != null) {
            Log.w(TAG, "Error inflating AppWidget " + this.mInfo + ": " + exception.toString());
        }
        if (defaultView == null) {
            return getErrorView();
        }
        return defaultView;
    }

    protected View getErrorView() {
        TextView tv = new TextView(this.mContext);
        tv.setText(com.android.internal.R.string.gadget_host_error_inflating);
        tv.setBackgroundColor(Color.argb(127, 0, 0, 0));
        return tv;
    }

    @Override // android.view.View
    public synchronized void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setClassName(AppWidgetHostView.class.getName());
    }
}
