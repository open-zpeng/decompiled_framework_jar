package android.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ActivityChooserModel;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import com.android.internal.R;
import com.android.internal.view.menu.ShowableListMenu;
/* loaded from: classes3.dex */
public class ActivityChooserView extends ViewGroup implements ActivityChooserModel.ActivityChooserModelClient {
    private static final String LOG_TAG = "ActivityChooserView";
    private final LinearLayout mActivityChooserContent;
    private final Drawable mActivityChooserContentBackground;
    private final ActivityChooserViewAdapter mAdapter;
    private final Callbacks mCallbacks;
    private int mDefaultActionButtonContentDescription;
    private final FrameLayout mDefaultActivityButton;
    private final ImageView mDefaultActivityButtonImage;
    private final FrameLayout mExpandActivityOverflowButton;
    private final ImageView mExpandActivityOverflowButtonImage;
    private int mInitialActivityCount;
    private boolean mIsAttachedToWindow;
    private boolean mIsSelectingDefaultActivity;
    private final int mListPopupMaxWidth;
    private ListPopupWindow mListPopupWindow;
    private final DataSetObserver mModelDataSetOberver;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    ActionProvider mProvider;

    public synchronized ActivityChooserView(Context context) {
        this(context, null);
    }

    public synchronized ActivityChooserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public synchronized ActivityChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public synchronized ActivityChooserView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mModelDataSetOberver = new DataSetObserver() { // from class: android.widget.ActivityChooserView.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                super.onChanged();
                ActivityChooserView.this.mAdapter.notifyDataSetChanged();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                super.onInvalidated();
                ActivityChooserView.this.mAdapter.notifyDataSetInvalidated();
            }
        };
        this.mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: android.widget.ActivityChooserView.2
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                if (ActivityChooserView.this.isShowingPopup()) {
                    if (!ActivityChooserView.this.isShown()) {
                        ActivityChooserView.this.getListPopupWindow().dismiss();
                        return;
                    }
                    ActivityChooserView.this.getListPopupWindow().show();
                    if (ActivityChooserView.this.mProvider != null) {
                        ActivityChooserView.this.mProvider.subUiVisibilityChanged(true);
                    }
                }
            }
        };
        this.mInitialActivityCount = 4;
        TypedArray attributesArray = context.obtainStyledAttributes(attrs, R.styleable.ActivityChooserView, defStyleAttr, defStyleRes);
        this.mInitialActivityCount = attributesArray.getInt(1, 4);
        Drawable expandActivityOverflowButtonDrawable = attributesArray.getDrawable(0);
        attributesArray.recycle();
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        inflater.inflate(R.layout.activity_chooser_view, (ViewGroup) this, true);
        this.mCallbacks = new Callbacks();
        this.mActivityChooserContent = (LinearLayout) findViewById(R.id.activity_chooser_view_content);
        this.mActivityChooserContentBackground = this.mActivityChooserContent.getBackground();
        this.mDefaultActivityButton = (FrameLayout) findViewById(R.id.default_activity_button);
        this.mDefaultActivityButton.setOnClickListener(this.mCallbacks);
        this.mDefaultActivityButton.setOnLongClickListener(this.mCallbacks);
        this.mDefaultActivityButtonImage = (ImageView) this.mDefaultActivityButton.findViewById(R.id.image);
        FrameLayout expandButton = (FrameLayout) findViewById(R.id.expand_activities_button);
        expandButton.setOnClickListener(this.mCallbacks);
        expandButton.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: android.widget.ActivityChooserView.3
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                info.setCanOpenPopup(true);
            }
        });
        expandButton.setOnTouchListener(new ForwardingListener(expandButton) { // from class: android.widget.ActivityChooserView.4
            @Override // android.widget.ForwardingListener
            public ShowableListMenu getPopup() {
                return ActivityChooserView.this.getListPopupWindow();
            }

            @Override // android.widget.ForwardingListener
            protected boolean onForwardingStarted() {
                ActivityChooserView.this.showPopup();
                return true;
            }

            @Override // android.widget.ForwardingListener
            protected boolean onForwardingStopped() {
                ActivityChooserView.this.dismissPopup();
                return true;
            }
        });
        this.mExpandActivityOverflowButton = expandButton;
        this.mExpandActivityOverflowButtonImage = (ImageView) expandButton.findViewById(R.id.image);
        this.mExpandActivityOverflowButtonImage.setImageDrawable(expandActivityOverflowButtonDrawable);
        this.mAdapter = new ActivityChooserViewAdapter();
        this.mAdapter.registerDataSetObserver(new DataSetObserver() { // from class: android.widget.ActivityChooserView.5
            @Override // android.database.DataSetObserver
            public void onChanged() {
                super.onChanged();
                ActivityChooserView.this.updateAppearance();
            }
        });
        Resources resources = context.getResources();
        this.mListPopupMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R.dimen.config_prefDialogWidth));
    }

    @Override // android.widget.ActivityChooserModel.ActivityChooserModelClient
    public synchronized void setActivityChooserModel(ActivityChooserModel dataModel) {
        this.mAdapter.setDataModel(dataModel);
        if (isShowingPopup()) {
            dismissPopup();
            showPopup();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setExpandActivityOverflowButtonDrawable(Drawable drawable) {
        this.mExpandActivityOverflowButtonImage.setImageDrawable(drawable);
    }

    public synchronized void setExpandActivityOverflowButtonContentDescription(int resourceId) {
        CharSequence contentDescription = this.mContext.getString(resourceId);
        this.mExpandActivityOverflowButtonImage.setContentDescription(contentDescription);
    }

    public synchronized void setProvider(ActionProvider provider) {
        this.mProvider = provider;
    }

    public synchronized boolean showPopup() {
        if (isShowingPopup() || !this.mIsAttachedToWindow) {
            return false;
        }
        this.mIsSelectingDefaultActivity = false;
        showPopupUnchecked(this.mInitialActivityCount);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void showPopupUnchecked(int maxActivityCount) {
        if (this.mAdapter.getDataModel() == null) {
            throw new IllegalStateException("No data model. Did you call #setDataModel?");
        }
        getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        boolean defaultActivityButtonShown = this.mDefaultActivityButton.getVisibility() == 0;
        int activityCount = this.mAdapter.getActivityCount();
        int maxActivityCountOffset = defaultActivityButtonShown ? 1 : 0;
        if (maxActivityCount != Integer.MAX_VALUE && activityCount > maxActivityCount + maxActivityCountOffset) {
            this.mAdapter.setShowFooterView(true);
            this.mAdapter.setMaxActivityCount(maxActivityCount - 1);
        } else {
            this.mAdapter.setShowFooterView(false);
            this.mAdapter.setMaxActivityCount(maxActivityCount);
        }
        ListPopupWindow popupWindow = getListPopupWindow();
        if (!popupWindow.isShowing()) {
            if (this.mIsSelectingDefaultActivity || !defaultActivityButtonShown) {
                this.mAdapter.setShowDefaultActivity(true, defaultActivityButtonShown);
            } else {
                this.mAdapter.setShowDefaultActivity(false, false);
            }
            int contentWidth = Math.min(this.mAdapter.measureContentWidth(), this.mListPopupMaxWidth);
            popupWindow.setContentWidth(contentWidth);
            popupWindow.show();
            if (this.mProvider != null) {
                this.mProvider.subUiVisibilityChanged(true);
            }
            popupWindow.getListView().setContentDescription(this.mContext.getString(R.string.activitychooserview_choose_application));
            popupWindow.getListView().setSelector(new ColorDrawable(0));
        }
    }

    public synchronized boolean dismissPopup() {
        if (isShowingPopup()) {
            getListPopupWindow().dismiss();
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
                return true;
            }
            return true;
        }
        return true;
    }

    public synchronized boolean isShowingPopup() {
        return getListPopupWindow().isShowing();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ActivityChooserModel dataModel = this.mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.registerObserver(this.mModelDataSetOberver);
        }
        this.mIsAttachedToWindow = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ActivityChooserModel dataModel = this.mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.unregisterObserver(this.mModelDataSetOberver);
        }
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        }
        if (isShowingPopup()) {
            dismissPopup();
        }
        this.mIsAttachedToWindow = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = this.mActivityChooserContent;
        if (this.mDefaultActivityButton.getVisibility() != 0) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(heightMeasureSpec), 1073741824);
        }
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(child.getMeasuredWidth(), child.getMeasuredHeight());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mActivityChooserContent.layout(0, 0, right - left, bottom - top);
        if (!isShowingPopup()) {
            dismissPopup();
        }
    }

    public synchronized ActivityChooserModel getDataModel() {
        return this.mAdapter.getDataModel();
    }

    public synchronized void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    public synchronized void setInitialActivityCount(int itemCount) {
        this.mInitialActivityCount = itemCount;
    }

    public synchronized void setDefaultActionButtonContentDescription(int resourceId) {
        this.mDefaultActionButtonContentDescription = resourceId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized ListPopupWindow getListPopupWindow() {
        if (this.mListPopupWindow == null) {
            this.mListPopupWindow = new ListPopupWindow(getContext());
            this.mListPopupWindow.setAdapter(this.mAdapter);
            this.mListPopupWindow.setAnchorView(this);
            this.mListPopupWindow.setModal(true);
            this.mListPopupWindow.setOnItemClickListener(this.mCallbacks);
            this.mListPopupWindow.setOnDismissListener(this.mCallbacks);
        }
        return this.mListPopupWindow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateAppearance() {
        if (this.mAdapter.getCount() > 0) {
            this.mExpandActivityOverflowButton.setEnabled(true);
        } else {
            this.mExpandActivityOverflowButton.setEnabled(false);
        }
        int activityCount = this.mAdapter.getActivityCount();
        int historySize = this.mAdapter.getHistorySize();
        if (activityCount == 1 || (activityCount > 1 && historySize > 0)) {
            this.mDefaultActivityButton.setVisibility(0);
            ResolveInfo activity = this.mAdapter.getDefaultActivity();
            PackageManager packageManager = this.mContext.getPackageManager();
            this.mDefaultActivityButtonImage.setImageDrawable(activity.loadIcon(packageManager));
            if (this.mDefaultActionButtonContentDescription != 0) {
                CharSequence label = activity.loadLabel(packageManager);
                String contentDescription = this.mContext.getString(this.mDefaultActionButtonContentDescription, label);
                this.mDefaultActivityButton.setContentDescription(contentDescription);
            }
        } else {
            this.mDefaultActivityButton.setVisibility(8);
        }
        if (this.mDefaultActivityButton.getVisibility() == 0) {
            this.mActivityChooserContent.setBackground(this.mActivityChooserContentBackground);
        } else {
            this.mActivityChooserContent.setBackground(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Callbacks implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnLongClickListener, PopupWindow.OnDismissListener {
        private Callbacks() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ActivityChooserViewAdapter adapter = (ActivityChooserViewAdapter) parent.getAdapter();
            int itemViewType = adapter.getItemViewType(position);
            switch (itemViewType) {
                case 0:
                    ActivityChooserView.this.dismissPopup();
                    if (!ActivityChooserView.this.mIsSelectingDefaultActivity) {
                        int position2 = ActivityChooserView.this.mAdapter.getShowDefaultActivity() ? position : position + 1;
                        Intent launchIntent = ActivityChooserView.this.mAdapter.getDataModel().chooseActivity(position2);
                        if (launchIntent != null) {
                            launchIntent.addFlags(524288);
                            ResolveInfo resolveInfo = ActivityChooserView.this.mAdapter.getDataModel().getActivity(position2);
                            startActivity(launchIntent, resolveInfo);
                            return;
                        }
                        return;
                    } else if (position > 0) {
                        ActivityChooserView.this.mAdapter.getDataModel().setDefaultActivity(position);
                        return;
                    } else {
                        return;
                    }
                case 1:
                    ActivityChooserView.this.showPopupUnchecked(Integer.MAX_VALUE);
                    return;
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view != ActivityChooserView.this.mDefaultActivityButton) {
                if (view == ActivityChooserView.this.mExpandActivityOverflowButton) {
                    ActivityChooserView.this.mIsSelectingDefaultActivity = false;
                    ActivityChooserView.this.showPopupUnchecked(ActivityChooserView.this.mInitialActivityCount);
                    return;
                }
                throw new IllegalArgumentException();
            }
            ActivityChooserView.this.dismissPopup();
            ResolveInfo defaultActivity = ActivityChooserView.this.mAdapter.getDefaultActivity();
            int index = ActivityChooserView.this.mAdapter.getDataModel().getActivityIndex(defaultActivity);
            Intent launchIntent = ActivityChooserView.this.mAdapter.getDataModel().chooseActivity(index);
            if (launchIntent != null) {
                launchIntent.addFlags(524288);
                startActivity(launchIntent, defaultActivity);
            }
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            if (view == ActivityChooserView.this.mDefaultActivityButton) {
                if (ActivityChooserView.this.mAdapter.getCount() > 0) {
                    ActivityChooserView.this.mIsSelectingDefaultActivity = true;
                    ActivityChooserView.this.showPopupUnchecked(ActivityChooserView.this.mInitialActivityCount);
                }
                return true;
            }
            throw new IllegalArgumentException();
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            notifyOnDismissListener();
            if (ActivityChooserView.this.mProvider != null) {
                ActivityChooserView.this.mProvider.subUiVisibilityChanged(false);
            }
        }

        private synchronized void notifyOnDismissListener() {
            if (ActivityChooserView.this.mOnDismissListener != null) {
                ActivityChooserView.this.mOnDismissListener.onDismiss();
            }
        }

        private synchronized void startActivity(Intent intent, ResolveInfo resolveInfo) {
            try {
                ActivityChooserView.this.mContext.startActivity(intent);
            } catch (RuntimeException e) {
                CharSequence appLabel = resolveInfo.loadLabel(ActivityChooserView.this.mContext.getPackageManager());
                String message = ActivityChooserView.this.mContext.getString(R.string.activitychooserview_choose_application_error, appLabel);
                Log.e(ActivityChooserView.LOG_TAG, message);
                Toast.makeText(ActivityChooserView.this.mContext, message, 0).show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ActivityChooserViewAdapter extends BaseAdapter {
        private static final int ITEM_VIEW_TYPE_ACTIVITY = 0;
        private static final int ITEM_VIEW_TYPE_COUNT = 3;
        private static final int ITEM_VIEW_TYPE_FOOTER = 1;
        public static final int MAX_ACTIVITY_COUNT_DEFAULT = 4;
        public static final int MAX_ACTIVITY_COUNT_UNLIMITED = Integer.MAX_VALUE;
        private ActivityChooserModel mDataModel;
        private boolean mHighlightDefaultActivity;
        private int mMaxActivityCount;
        private boolean mShowDefaultActivity;
        private boolean mShowFooterView;

        private ActivityChooserViewAdapter() {
            this.mMaxActivityCount = 4;
        }

        public synchronized void setDataModel(ActivityChooserModel dataModel) {
            ActivityChooserModel oldDataModel = ActivityChooserView.this.mAdapter.getDataModel();
            if (oldDataModel != null && ActivityChooserView.this.isShown()) {
                oldDataModel.unregisterObserver(ActivityChooserView.this.mModelDataSetOberver);
            }
            this.mDataModel = dataModel;
            if (dataModel != null && ActivityChooserView.this.isShown()) {
                dataModel.registerObserver(ActivityChooserView.this.mModelDataSetOberver);
            }
            notifyDataSetChanged();
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int position) {
            return (this.mShowFooterView && position == getCount() - 1) ? 1 : 0;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return 3;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            int activityCount = this.mDataModel.getActivityCount();
            if (!this.mShowDefaultActivity && this.mDataModel.getDefaultActivity() != null) {
                activityCount--;
            }
            int count = Math.min(activityCount, this.mMaxActivityCount);
            if (this.mShowFooterView) {
                return count + 1;
            }
            return count;
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            int itemViewType = getItemViewType(position);
            switch (itemViewType) {
                case 0:
                    if (!this.mShowDefaultActivity && this.mDataModel.getDefaultActivity() != null) {
                        position++;
                    }
                    return this.mDataModel.getActivity(position);
                case 1:
                    return null;
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            int itemViewType = getItemViewType(position);
            switch (itemViewType) {
                case 0:
                    if (convertView == null || convertView.getId() != 16909127) {
                        convertView = LayoutInflater.from(ActivityChooserView.this.getContext()).inflate(R.layout.activity_chooser_view_list_item, parent, false);
                    }
                    PackageManager packageManager = ActivityChooserView.this.mContext.getPackageManager();
                    ImageView iconView = (ImageView) convertView.findViewById(android.R.id.icon);
                    ResolveInfo activity = (ResolveInfo) getItem(position);
                    iconView.setImageDrawable(activity.loadIcon(packageManager));
                    TextView titleView = (TextView) convertView.findViewById(android.R.id.title);
                    titleView.setText(activity.loadLabel(packageManager));
                    if (this.mShowDefaultActivity && position == 0 && this.mHighlightDefaultActivity) {
                        convertView.setActivated(true);
                    } else {
                        convertView.setActivated(false);
                    }
                    return convertView;
                case 1:
                    if (convertView == null || convertView.getId() != 1) {
                        View convertView2 = LayoutInflater.from(ActivityChooserView.this.getContext()).inflate(R.layout.activity_chooser_view_list_item, parent, false);
                        convertView2.setId(1);
                        TextView titleView2 = (TextView) convertView2.findViewById(android.R.id.title);
                        titleView2.setText(ActivityChooserView.this.mContext.getString(R.string.activity_chooser_view_see_all));
                        return convertView2;
                    }
                    return convertView;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public synchronized int measureContentWidth() {
            int oldMaxActivityCount = this.mMaxActivityCount;
            this.mMaxActivityCount = Integer.MAX_VALUE;
            int contentWidth = 0;
            View itemView = null;
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            int count = getCount();
            for (int i = 0; i < count; i++) {
                itemView = getView(i, itemView, null);
                itemView.measure(widthMeasureSpec, heightMeasureSpec);
                contentWidth = Math.max(contentWidth, itemView.getMeasuredWidth());
            }
            this.mMaxActivityCount = oldMaxActivityCount;
            return contentWidth;
        }

        public synchronized void setMaxActivityCount(int maxActivityCount) {
            if (this.mMaxActivityCount != maxActivityCount) {
                this.mMaxActivityCount = maxActivityCount;
                notifyDataSetChanged();
            }
        }

        public synchronized ResolveInfo getDefaultActivity() {
            return this.mDataModel.getDefaultActivity();
        }

        public synchronized void setShowFooterView(boolean showFooterView) {
            if (this.mShowFooterView != showFooterView) {
                this.mShowFooterView = showFooterView;
                notifyDataSetChanged();
            }
        }

        public synchronized int getActivityCount() {
            return this.mDataModel.getActivityCount();
        }

        public synchronized int getHistorySize() {
            return this.mDataModel.getHistorySize();
        }

        public synchronized ActivityChooserModel getDataModel() {
            return this.mDataModel;
        }

        public synchronized void setShowDefaultActivity(boolean showDefaultActivity, boolean highlightDefaultActivity) {
            if (this.mShowDefaultActivity != showDefaultActivity || this.mHighlightDefaultActivity != highlightDefaultActivity) {
                this.mShowDefaultActivity = showDefaultActivity;
                this.mHighlightDefaultActivity = highlightDefaultActivity;
                notifyDataSetChanged();
            }
        }

        public synchronized boolean getShowDefaultActivity() {
            return this.mShowDefaultActivity;
        }
    }
}
