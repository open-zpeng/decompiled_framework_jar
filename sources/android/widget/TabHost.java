package android.widget;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TabWidget;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes3.dex */
public class TabHost extends FrameLayout implements ViewTreeObserver.OnTouchModeChangeListener {
    private static final int TABWIDGET_LOCATION_BOTTOM = 3;
    private static final int TABWIDGET_LOCATION_LEFT = 0;
    private static final int TABWIDGET_LOCATION_RIGHT = 2;
    private static final int TABWIDGET_LOCATION_TOP = 1;
    public private int mCurrentTab;
    private View mCurrentView;
    protected LocalActivityManager mLocalActivityManager;
    public protected OnTabChangeListener mOnTabChangeListener;
    private FrameLayout mTabContent;
    private View.OnKeyListener mTabKeyListener;
    private int mTabLayoutId;
    public protected List<TabSpec> mTabSpecs;
    private TabWidget mTabWidget;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public interface ContentStrategy {
        synchronized View getContentView();

        synchronized void tabClosed();
    }

    /* loaded from: classes3.dex */
    private interface IndicatorStrategy {
        synchronized View createIndicatorView();
    }

    /* loaded from: classes3.dex */
    public interface OnTabChangeListener {
        void onTabChanged(String str);
    }

    /* loaded from: classes3.dex */
    public interface TabContentFactory {
        View createTabContent(String str);
    }

    public TabHost(Context context) {
        super(context);
        this.mTabSpecs = new ArrayList(2);
        this.mCurrentTab = -1;
        this.mCurrentView = null;
        this.mLocalActivityManager = null;
        initTabHost();
    }

    public TabHost(Context context, AttributeSet attrs) {
        this(context, attrs, 16842883);
    }

    public TabHost(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TabHost(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
        this.mTabSpecs = new ArrayList(2);
        this.mCurrentTab = -1;
        this.mCurrentView = null;
        this.mLocalActivityManager = null;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabWidget, defStyleAttr, defStyleRes);
        this.mTabLayoutId = a.getResourceId(4, 0);
        a.recycle();
        if (this.mTabLayoutId == 0) {
            this.mTabLayoutId = R.layout.tab_indicator_holo;
        }
        initTabHost();
    }

    private synchronized void initTabHost() {
        setFocusableInTouchMode(true);
        setDescendantFocusability(262144);
        this.mCurrentTab = -1;
        this.mCurrentView = null;
    }

    public TabSpec newTabSpec(String tag) {
        if (tag == null) {
            throw new IllegalArgumentException("tag must be non-null");
        }
        return new TabSpec(tag);
    }

    public void setup() {
        this.mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        if (this.mTabWidget == null) {
            throw new RuntimeException("Your TabHost must have a TabWidget whose id attribute is 'android.R.id.tabs'");
        }
        this.mTabKeyListener = new View.OnKeyListener() { // from class: android.widget.TabHost.1
            @Override // android.view.View.OnKeyListener
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (!KeyEvent.isModifierKey(keyCode) && keyCode != 66) {
                    switch (keyCode) {
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                            break;
                        default:
                            switch (keyCode) {
                                case 61:
                                case 62:
                                    break;
                                default:
                                    TabHost.this.mTabContent.requestFocus(2);
                                    return TabHost.this.mTabContent.dispatchKeyEvent(event);
                            }
                    }
                }
                return false;
            }
        };
        this.mTabWidget.setTabSelectionListener(new TabWidget.OnTabSelectionChanged() { // from class: android.widget.TabHost.2
            @Override // android.widget.TabWidget.OnTabSelectionChanged
            public void onTabSelectionChanged(int tabIndex, boolean clicked) {
                TabHost.this.setCurrentTab(tabIndex);
                if (clicked) {
                    TabHost.this.mTabContent.requestFocus(2);
                }
            }
        });
        this.mTabContent = (FrameLayout) findViewById(android.R.id.tabcontent);
        if (this.mTabContent == null) {
            throw new RuntimeException("Your TabHost must have a FrameLayout whose id attribute is 'android.R.id.tabcontent'");
        }
    }

    @Override // android.view.View
    public synchronized void sendAccessibilityEventInternal(int eventType) {
    }

    public void setup(LocalActivityManager activityGroup) {
        setup();
        this.mLocalActivityManager = activityGroup;
    }

    @Override // android.view.ViewTreeObserver.OnTouchModeChangeListener
    public void onTouchModeChanged(boolean isInTouchMode) {
    }

    public void addTab(TabSpec tabSpec) {
        if (tabSpec.mIndicatorStrategy == null) {
            throw new IllegalArgumentException("you must specify a way to create the tab indicator.");
        }
        if (tabSpec.mContentStrategy == null) {
            throw new IllegalArgumentException("you must specify a way to create the tab content");
        }
        View tabIndicator = tabSpec.mIndicatorStrategy.createIndicatorView();
        tabIndicator.setOnKeyListener(this.mTabKeyListener);
        if (tabSpec.mIndicatorStrategy instanceof ViewIndicatorStrategy) {
            this.mTabWidget.setStripEnabled(false);
        }
        this.mTabWidget.addView(tabIndicator);
        this.mTabSpecs.add(tabSpec);
        if (this.mCurrentTab == -1) {
            setCurrentTab(0);
        }
    }

    public void clearAllTabs() {
        this.mTabWidget.removeAllViews();
        initTabHost();
        this.mTabContent.removeAllViews();
        this.mTabSpecs.clear();
        requestLayout();
        invalidate();
    }

    public TabWidget getTabWidget() {
        return this.mTabWidget;
    }

    public int getCurrentTab() {
        return this.mCurrentTab;
    }

    public String getCurrentTabTag() {
        if (this.mCurrentTab >= 0 && this.mCurrentTab < this.mTabSpecs.size()) {
            return this.mTabSpecs.get(this.mCurrentTab).getTag();
        }
        return null;
    }

    public View getCurrentTabView() {
        if (this.mCurrentTab >= 0 && this.mCurrentTab < this.mTabSpecs.size()) {
            return this.mTabWidget.getChildTabViewAt(this.mCurrentTab);
        }
        return null;
    }

    public View getCurrentView() {
        return this.mCurrentView;
    }

    public void setCurrentTabByTag(String tag) {
        int count = this.mTabSpecs.size();
        for (int i = 0; i < count; i++) {
            if (this.mTabSpecs.get(i).getTag().equals(tag)) {
                setCurrentTab(i);
                return;
            }
        }
    }

    public FrameLayout getTabContentView() {
        return this.mTabContent;
    }

    private synchronized int getTabWidgetLocation() {
        if (this.mTabWidget.getOrientation() == 1) {
            int location = this.mTabContent.getLeft() < this.mTabWidget.getLeft() ? 2 : 0;
            return location;
        }
        int location2 = this.mTabContent.getTop() < this.mTabWidget.getTop() ? 3 : 1;
        return location2;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCodeShouldChangeFocus;
        int directionShouldChangeFocus;
        int soundEffect;
        boolean handled = super.dispatchKeyEvent(event);
        if (!handled && event.getAction() == 0 && this.mCurrentView != null && this.mCurrentView.isRootNamespace() && this.mCurrentView.hasFocus()) {
            int tabWidgetLocation = getTabWidgetLocation();
            if (tabWidgetLocation == 0) {
                keyCodeShouldChangeFocus = 21;
                directionShouldChangeFocus = 17;
                soundEffect = 1;
            } else {
                switch (tabWidgetLocation) {
                    case 2:
                        keyCodeShouldChangeFocus = 22;
                        directionShouldChangeFocus = 66;
                        soundEffect = 3;
                        break;
                    case 3:
                        keyCodeShouldChangeFocus = 20;
                        directionShouldChangeFocus = 130;
                        soundEffect = 4;
                        break;
                    default:
                        keyCodeShouldChangeFocus = 19;
                        directionShouldChangeFocus = 33;
                        soundEffect = 2;
                        break;
                }
            }
            if (event.getKeyCode() == keyCodeShouldChangeFocus && this.mCurrentView.findFocus().focusSearch(directionShouldChangeFocus) == null) {
                this.mTabWidget.getChildTabViewAt(this.mCurrentTab).requestFocus();
                playSoundEffect(soundEffect);
                return true;
            }
        }
        return handled;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        if (this.mCurrentView != null) {
            this.mCurrentView.dispatchWindowFocusChanged(hasFocus);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return TabHost.class.getName();
    }

    public void setCurrentTab(int index) {
        if (index < 0 || index >= this.mTabSpecs.size() || index == this.mCurrentTab) {
            return;
        }
        if (this.mCurrentTab != -1) {
            this.mTabSpecs.get(this.mCurrentTab).mContentStrategy.tabClosed();
        }
        this.mCurrentTab = index;
        TabSpec spec = this.mTabSpecs.get(index);
        this.mTabWidget.focusCurrentTab(this.mCurrentTab);
        this.mCurrentView = spec.mContentStrategy.getContentView();
        if (this.mCurrentView.getParent() == null) {
            this.mTabContent.addView(this.mCurrentView, new ViewGroup.LayoutParams(-1, -1));
        }
        if (!this.mTabWidget.hasFocus()) {
            this.mCurrentView.requestFocus();
        }
        invokeOnTabChangeListener();
    }

    public void setOnTabChangedListener(OnTabChangeListener l) {
        this.mOnTabChangeListener = l;
    }

    private synchronized void invokeOnTabChangeListener() {
        if (this.mOnTabChangeListener != null) {
            this.mOnTabChangeListener.onTabChanged(getCurrentTabTag());
        }
    }

    /* loaded from: classes3.dex */
    public class TabSpec {
        public protected ContentStrategy mContentStrategy;
        public protected IndicatorStrategy mIndicatorStrategy;
        private final String mTag;

        private TabSpec(String tag) {
            this.mTag = tag;
        }

        public TabSpec setIndicator(CharSequence label) {
            this.mIndicatorStrategy = new LabelIndicatorStrategy(label);
            return this;
        }

        public TabSpec setIndicator(CharSequence label, Drawable icon) {
            this.mIndicatorStrategy = new LabelAndIconIndicatorStrategy(label, icon);
            return this;
        }

        public TabSpec setIndicator(View view) {
            this.mIndicatorStrategy = new ViewIndicatorStrategy(view);
            return this;
        }

        public TabSpec setContent(int viewId) {
            this.mContentStrategy = new ViewIdContentStrategy(viewId);
            return this;
        }

        public TabSpec setContent(TabContentFactory contentFactory) {
            this.mContentStrategy = new FactoryContentStrategy(this.mTag, contentFactory);
            return this;
        }

        public TabSpec setContent(Intent intent) {
            this.mContentStrategy = new IntentContentStrategy(this.mTag, intent);
            return this;
        }

        public String getTag() {
            return this.mTag;
        }
    }

    /* loaded from: classes3.dex */
    private class LabelIndicatorStrategy implements IndicatorStrategy {
        private final CharSequence mLabel;

        private LabelIndicatorStrategy(CharSequence label) {
            this.mLabel = label;
        }

        @Override // android.widget.TabHost.IndicatorStrategy
        public synchronized View createIndicatorView() {
            Context context = TabHost.this.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tabIndicator = inflater.inflate(TabHost.this.mTabLayoutId, (ViewGroup) TabHost.this.mTabWidget, false);
            TextView tv = (TextView) tabIndicator.findViewById(android.R.id.title);
            tv.setText(this.mLabel);
            if (context.getApplicationInfo().targetSdkVersion <= 4) {
                tabIndicator.setBackgroundResource(R.drawable.tab_indicator_v4);
                tv.setTextColor(context.getColorStateList(R.color.tab_indicator_text_v4));
            }
            return tabIndicator;
        }
    }

    /* loaded from: classes3.dex */
    private class LabelAndIconIndicatorStrategy implements IndicatorStrategy {
        private final Drawable mIcon;
        private final CharSequence mLabel;

        private LabelAndIconIndicatorStrategy(CharSequence label, Drawable icon) {
            this.mLabel = label;
            this.mIcon = icon;
        }

        @Override // android.widget.TabHost.IndicatorStrategy
        public synchronized View createIndicatorView() {
            Context context = TabHost.this.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tabIndicator = inflater.inflate(TabHost.this.mTabLayoutId, (ViewGroup) TabHost.this.mTabWidget, false);
            TextView tv = (TextView) tabIndicator.findViewById(android.R.id.title);
            ImageView iconView = (ImageView) tabIndicator.findViewById(android.R.id.icon);
            boolean bindIcon = true;
            boolean exclusive = iconView.getVisibility() == 8;
            if (exclusive && !TextUtils.isEmpty(this.mLabel)) {
                bindIcon = false;
            }
            tv.setText(this.mLabel);
            if (bindIcon && this.mIcon != null) {
                iconView.setImageDrawable(this.mIcon);
                iconView.setVisibility(0);
            }
            if (context.getApplicationInfo().targetSdkVersion <= 4) {
                tabIndicator.setBackgroundResource(R.drawable.tab_indicator_v4);
                tv.setTextColor(context.getColorStateList(R.color.tab_indicator_text_v4));
            }
            return tabIndicator;
        }
    }

    /* loaded from: classes3.dex */
    private class ViewIndicatorStrategy implements IndicatorStrategy {
        private final View mView;

        private ViewIndicatorStrategy(View view) {
            this.mView = view;
        }

        @Override // android.widget.TabHost.IndicatorStrategy
        public synchronized View createIndicatorView() {
            return this.mView;
        }
    }

    /* loaded from: classes3.dex */
    private class ViewIdContentStrategy implements ContentStrategy {
        private final View mView;

        private ViewIdContentStrategy(int viewId) {
            this.mView = TabHost.this.mTabContent.findViewById(viewId);
            if (this.mView != null) {
                this.mView.setVisibility(8);
                return;
            }
            throw new RuntimeException("Could not create tab content because could not find view with id " + viewId);
        }

        @Override // android.widget.TabHost.ContentStrategy
        public synchronized View getContentView() {
            this.mView.setVisibility(0);
            return this.mView;
        }

        @Override // android.widget.TabHost.ContentStrategy
        public synchronized void tabClosed() {
            this.mView.setVisibility(8);
        }
    }

    /* loaded from: classes3.dex */
    private class FactoryContentStrategy implements ContentStrategy {
        private TabContentFactory mFactory;
        private View mTabContent;
        private final CharSequence mTag;

        public FactoryContentStrategy(CharSequence tag, TabContentFactory factory) {
            this.mTag = tag;
            this.mFactory = factory;
        }

        @Override // android.widget.TabHost.ContentStrategy
        public synchronized View getContentView() {
            if (this.mTabContent == null) {
                this.mTabContent = this.mFactory.createTabContent(this.mTag.toString());
            }
            this.mTabContent.setVisibility(0);
            return this.mTabContent;
        }

        @Override // android.widget.TabHost.ContentStrategy
        public synchronized void tabClosed() {
            this.mTabContent.setVisibility(8);
        }
    }

    /* loaded from: classes3.dex */
    private class IntentContentStrategy implements ContentStrategy {
        private final Intent mIntent;
        private View mLaunchedView;
        private final String mTag;

        private IntentContentStrategy(String tag, Intent intent) {
            this.mTag = tag;
            this.mIntent = intent;
        }

        private protected View getContentView() {
            if (TabHost.this.mLocalActivityManager == null) {
                throw new IllegalStateException("Did you forget to call 'public void setup(LocalActivityManager activityGroup)'?");
            }
            Window w = TabHost.this.mLocalActivityManager.startActivity(this.mTag, this.mIntent);
            View wd = w != null ? w.getDecorView() : null;
            if (this.mLaunchedView != wd && this.mLaunchedView != null && this.mLaunchedView.getParent() != null) {
                TabHost.this.mTabContent.removeView(this.mLaunchedView);
            }
            this.mLaunchedView = wd;
            if (this.mLaunchedView != null) {
                this.mLaunchedView.setVisibility(0);
                this.mLaunchedView.setFocusableInTouchMode(true);
                ((ViewGroup) this.mLaunchedView).setDescendantFocusability(262144);
            }
            return this.mLaunchedView;
        }

        private protected void tabClosed() {
            if (this.mLaunchedView != null) {
                this.mLaunchedView.setVisibility(8);
            }
        }
    }
}
