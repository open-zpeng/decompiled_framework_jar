package com.android.internal.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Property;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ActionMenuPresenter;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toolbar;
import com.android.internal.R;
import com.android.internal.view.menu.ActionMenuItem;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuPresenter;
/* loaded from: classes3.dex */
public class ToolbarWidgetWrapper implements DecorToolbar {
    private static final int AFFECTS_LOGO_MASK = 3;
    private static final long DEFAULT_FADE_DURATION_MS = 200;
    private static final String TAG = "ToolbarWidgetWrapper";
    private ActionMenuPresenter mActionMenuPresenter;
    private View mCustomView;
    private int mDefaultNavigationContentDescription;
    private Drawable mDefaultNavigationIcon;
    private int mDisplayOpts;
    private CharSequence mHomeDescription;
    private Drawable mIcon;
    private Drawable mLogo;
    private boolean mMenuPrepared;
    private Drawable mNavIcon;
    private int mNavigationMode;
    private Spinner mSpinner;
    private CharSequence mSubtitle;
    private View mTabView;
    private CharSequence mTitle;
    private boolean mTitleSet;
    private Toolbar mToolbar;
    private Window.Callback mWindowCallback;

    public synchronized ToolbarWidgetWrapper(Toolbar toolbar, boolean style) {
        this(toolbar, style, R.string.action_bar_up_description);
    }

    public synchronized ToolbarWidgetWrapper(Toolbar toolbar, boolean style, int defaultNavigationContentDescription) {
        this.mNavigationMode = 0;
        this.mDefaultNavigationContentDescription = 0;
        this.mToolbar = toolbar;
        this.mTitle = toolbar.getTitle();
        this.mSubtitle = toolbar.getSubtitle();
        this.mTitleSet = this.mTitle != null;
        this.mNavIcon = this.mToolbar.getNavigationIcon();
        TypedArray a = toolbar.getContext().obtainStyledAttributes(null, R.styleable.ActionBar, android.R.attr.actionBarStyle, 0);
        this.mDefaultNavigationIcon = a.getDrawable(13);
        if (style) {
            CharSequence title = a.getText(5);
            if (!TextUtils.isEmpty(title)) {
                setTitle(title);
            }
            CharSequence subtitle = a.getText(9);
            if (!TextUtils.isEmpty(subtitle)) {
                setSubtitle(subtitle);
            }
            Drawable logo = a.getDrawable(6);
            if (logo != null) {
                setLogo(logo);
            }
            Drawable icon = a.getDrawable(0);
            if (icon != null) {
                setIcon(icon);
            }
            if (this.mNavIcon == null && this.mDefaultNavigationIcon != null) {
                setNavigationIcon(this.mDefaultNavigationIcon);
            }
            setDisplayOptions(a.getInt(8, 0));
            int customNavId = a.getResourceId(10, 0);
            if (customNavId != 0) {
                setCustomView(LayoutInflater.from(this.mToolbar.getContext()).inflate(customNavId, (ViewGroup) this.mToolbar, false));
                setDisplayOptions(this.mDisplayOpts | 16);
            }
            int height = a.getLayoutDimension(4, 0);
            if (height > 0) {
                ViewGroup.LayoutParams lp = this.mToolbar.getLayoutParams();
                lp.height = height;
                this.mToolbar.setLayoutParams(lp);
            }
            int contentInsetStart = a.getDimensionPixelOffset(22, -1);
            int contentInsetEnd = a.getDimensionPixelOffset(23, -1);
            if (contentInsetStart >= 0 || contentInsetEnd >= 0) {
                this.mToolbar.setContentInsetsRelative(Math.max(contentInsetStart, 0), Math.max(contentInsetEnd, 0));
            }
            int titleTextStyle = a.getResourceId(11, 0);
            if (titleTextStyle != 0) {
                this.mToolbar.setTitleTextAppearance(this.mToolbar.getContext(), titleTextStyle);
            }
            int subtitleTextStyle = a.getResourceId(12, 0);
            if (subtitleTextStyle != 0) {
                this.mToolbar.setSubtitleTextAppearance(this.mToolbar.getContext(), subtitleTextStyle);
            }
            int popupTheme = a.getResourceId(26, 0);
            if (popupTheme != 0) {
                this.mToolbar.setPopupTheme(popupTheme);
            }
        } else {
            this.mDisplayOpts = detectDisplayOptions();
        }
        a.recycle();
        setDefaultNavigationContentDescription(defaultNavigationContentDescription);
        this.mHomeDescription = this.mToolbar.getNavigationContentDescription();
        this.mToolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: com.android.internal.widget.ToolbarWidgetWrapper.1
            final ActionMenuItem mNavItem;

            {
                this.mNavItem = new ActionMenuItem(ToolbarWidgetWrapper.this.mToolbar.getContext(), 0, 16908332, 0, 0, ToolbarWidgetWrapper.this.mTitle);
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (ToolbarWidgetWrapper.this.mWindowCallback != null && ToolbarWidgetWrapper.this.mMenuPrepared) {
                    ToolbarWidgetWrapper.this.mWindowCallback.onMenuItemSelected(0, this.mNavItem);
                }
            }
        });
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setDefaultNavigationContentDescription(int defaultNavigationContentDescription) {
        if (defaultNavigationContentDescription == this.mDefaultNavigationContentDescription) {
            return;
        }
        this.mDefaultNavigationContentDescription = defaultNavigationContentDescription;
        if (TextUtils.isEmpty(this.mToolbar.getNavigationContentDescription())) {
            setNavigationContentDescription(this.mDefaultNavigationContentDescription);
        }
    }

    private synchronized int detectDisplayOptions() {
        if (this.mToolbar.getNavigationIcon() == null) {
            return 11;
        }
        int opts = 11 | 4;
        this.mDefaultNavigationIcon = this.mToolbar.getNavigationIcon();
        return opts;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized ViewGroup getViewGroup() {
        return this.mToolbar;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized Context getContext() {
        return this.mToolbar.getContext();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean isSplit() {
        return false;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean hasExpandedActionView() {
        return this.mToolbar.hasExpandedActionView();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void collapseActionView() {
        this.mToolbar.collapseActionView();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setWindowCallback(Window.Callback cb) {
        this.mWindowCallback = cb;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setWindowTitle(CharSequence title) {
        if (!this.mTitleSet) {
            setTitleInt(title);
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized CharSequence getTitle() {
        return this.mToolbar.getTitle();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setTitle(CharSequence title) {
        this.mTitleSet = true;
        setTitleInt(title);
    }

    private synchronized void setTitleInt(CharSequence title) {
        this.mTitle = title;
        if ((this.mDisplayOpts & 8) != 0) {
            this.mToolbar.setTitle(title);
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized CharSequence getSubtitle() {
        return this.mToolbar.getSubtitle();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setSubtitle(CharSequence subtitle) {
        this.mSubtitle = subtitle;
        if ((this.mDisplayOpts & 8) != 0) {
            this.mToolbar.setSubtitle(subtitle);
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void initProgress() {
        Log.i(TAG, "Progress display unsupported");
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void initIndeterminateProgress() {
        Log.i(TAG, "Progress display unsupported");
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean canSplit() {
        return false;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setSplitView(ViewGroup splitView) {
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setSplitToolbar(boolean split) {
        if (split) {
            throw new UnsupportedOperationException("Cannot split an android.widget.Toolbar");
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setSplitWhenNarrow(boolean splitWhenNarrow) {
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean hasIcon() {
        return this.mIcon != null;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean hasLogo() {
        return this.mLogo != null;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setIcon(int resId) {
        setIcon(resId != 0 ? getContext().getDrawable(resId) : null);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setIcon(Drawable d) {
        this.mIcon = d;
        updateToolbarLogo();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setLogo(int resId) {
        setLogo(resId != 0 ? getContext().getDrawable(resId) : null);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setLogo(Drawable d) {
        this.mLogo = d;
        updateToolbarLogo();
    }

    private synchronized void updateToolbarLogo() {
        Drawable logo = null;
        if ((this.mDisplayOpts & 2) != 0) {
            if ((this.mDisplayOpts & 1) != 0) {
                logo = this.mLogo != null ? this.mLogo : this.mIcon;
            } else {
                logo = this.mIcon;
            }
        }
        this.mToolbar.setLogo(logo);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean canShowOverflowMenu() {
        return this.mToolbar.canShowOverflowMenu();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean isOverflowMenuShowing() {
        return this.mToolbar.isOverflowMenuShowing();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean isOverflowMenuShowPending() {
        return this.mToolbar.isOverflowMenuShowPending();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean showOverflowMenu() {
        return this.mToolbar.showOverflowMenu();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean hideOverflowMenu() {
        return this.mToolbar.hideOverflowMenu();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setMenuPrepared() {
        this.mMenuPrepared = true;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setMenu(Menu menu, MenuPresenter.Callback cb) {
        if (this.mActionMenuPresenter == null) {
            this.mActionMenuPresenter = new ActionMenuPresenter(this.mToolbar.getContext());
            this.mActionMenuPresenter.setId(R.id.action_menu_presenter);
        }
        this.mActionMenuPresenter.setCallback(cb);
        this.mToolbar.setMenu((MenuBuilder) menu, this.mActionMenuPresenter);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void dismissPopupMenus() {
        this.mToolbar.dismissPopupMenus();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized int getDisplayOptions() {
        return this.mDisplayOpts;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setDisplayOptions(int newOpts) {
        int oldOpts = this.mDisplayOpts;
        int changed = oldOpts ^ newOpts;
        this.mDisplayOpts = newOpts;
        if (changed != 0) {
            if ((changed & 4) != 0) {
                if ((newOpts & 4) != 0) {
                    updateHomeAccessibility();
                }
                updateNavigationIcon();
            }
            if ((changed & 3) != 0) {
                updateToolbarLogo();
            }
            if ((changed & 8) != 0) {
                if ((newOpts & 8) != 0) {
                    this.mToolbar.setTitle(this.mTitle);
                    this.mToolbar.setSubtitle(this.mSubtitle);
                } else {
                    this.mToolbar.setTitle((CharSequence) null);
                    this.mToolbar.setSubtitle((CharSequence) null);
                }
            }
            if ((changed & 16) != 0 && this.mCustomView != null) {
                if ((newOpts & 16) != 0) {
                    this.mToolbar.addView(this.mCustomView);
                } else {
                    this.mToolbar.removeView(this.mCustomView);
                }
            }
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setEmbeddedTabView(ScrollingTabContainerView tabView) {
        if (this.mTabView != null && this.mTabView.getParent() == this.mToolbar) {
            this.mToolbar.removeView(this.mTabView);
        }
        this.mTabView = tabView;
        if (tabView != null && this.mNavigationMode == 2) {
            this.mToolbar.addView(this.mTabView, 0);
            Toolbar.LayoutParams lp = (Toolbar.LayoutParams) this.mTabView.getLayoutParams();
            lp.width = -2;
            lp.height = -2;
            lp.gravity = 8388691;
            tabView.setAllowCollapse(true);
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean hasEmbeddedTabs() {
        return this.mTabView != null;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized boolean isTitleTruncated() {
        return this.mToolbar.isTitleTruncated();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setCollapsible(boolean collapsible) {
        this.mToolbar.setCollapsible(collapsible);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setHomeButtonEnabled(boolean enable) {
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized int getNavigationMode() {
        return this.mNavigationMode;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setNavigationMode(int mode) {
        int oldMode = this.mNavigationMode;
        if (mode != oldMode) {
            switch (oldMode) {
                case 1:
                    if (this.mSpinner != null && this.mSpinner.getParent() == this.mToolbar) {
                        this.mToolbar.removeView(this.mSpinner);
                        break;
                    }
                    break;
                case 2:
                    if (this.mTabView != null && this.mTabView.getParent() == this.mToolbar) {
                        this.mToolbar.removeView(this.mTabView);
                        break;
                    }
                    break;
            }
            this.mNavigationMode = mode;
            switch (mode) {
                case 0:
                    return;
                case 1:
                    ensureSpinner();
                    this.mToolbar.addView(this.mSpinner, 0);
                    return;
                case 2:
                    if (this.mTabView != null) {
                        this.mToolbar.addView(this.mTabView, 0);
                        Toolbar.LayoutParams lp = (Toolbar.LayoutParams) this.mTabView.getLayoutParams();
                        lp.width = -2;
                        lp.height = -2;
                        lp.gravity = 8388691;
                        return;
                    }
                    return;
                default:
                    throw new IllegalArgumentException("Invalid navigation mode " + mode);
            }
        }
    }

    private synchronized void ensureSpinner() {
        if (this.mSpinner == null) {
            this.mSpinner = new Spinner(getContext(), null, 16843479);
            Toolbar.LayoutParams lp = new Toolbar.LayoutParams(-2, -2, 8388627);
            this.mSpinner.setLayoutParams(lp);
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setDropdownParams(SpinnerAdapter adapter, AdapterView.OnItemSelectedListener listener) {
        ensureSpinner();
        this.mSpinner.setAdapter(adapter);
        this.mSpinner.setOnItemSelectedListener(listener);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setDropdownSelectedPosition(int position) {
        if (this.mSpinner == null) {
            throw new IllegalStateException("Can't set dropdown selected position without an adapter");
        }
        this.mSpinner.setSelection(position);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized int getDropdownSelectedPosition() {
        if (this.mSpinner != null) {
            return this.mSpinner.getSelectedItemPosition();
        }
        return 0;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized int getDropdownItemCount() {
        if (this.mSpinner != null) {
            return this.mSpinner.getCount();
        }
        return 0;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setCustomView(View view) {
        if (this.mCustomView != null && (this.mDisplayOpts & 16) != 0) {
            this.mToolbar.removeView(this.mCustomView);
        }
        this.mCustomView = view;
        if (view != null && (this.mDisplayOpts & 16) != 0) {
            this.mToolbar.addView(this.mCustomView);
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized View getCustomView() {
        return this.mCustomView;
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void animateToVisibility(int visibility) {
        Animator anim = setupAnimatorToVisibility(visibility, 200L);
        if (anim != null) {
            anim.start();
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized Animator setupAnimatorToVisibility(int visibility, long duration) {
        if (visibility == 8) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(this.mToolbar, (Property<Toolbar, Float>) View.ALPHA, 1.0f, 0.0f);
            anim.setDuration(duration);
            anim.addListener(new AnimatorListenerAdapter() { // from class: com.android.internal.widget.ToolbarWidgetWrapper.2
                private boolean mCanceled = false;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    if (!this.mCanceled) {
                        ToolbarWidgetWrapper.this.mToolbar.setVisibility(8);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation) {
                    this.mCanceled = true;
                }
            });
            return anim;
        } else if (visibility == 0) {
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(this.mToolbar, (Property<Toolbar, Float>) View.ALPHA, 0.0f, 1.0f);
            anim2.setDuration(duration);
            anim2.addListener(new AnimatorListenerAdapter() { // from class: com.android.internal.widget.ToolbarWidgetWrapper.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                    ToolbarWidgetWrapper.this.mToolbar.setVisibility(0);
                }
            });
            return anim2;
        } else {
            return null;
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setNavigationIcon(Drawable icon) {
        this.mNavIcon = icon;
        updateNavigationIcon();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setNavigationIcon(int resId) {
        setNavigationIcon(resId != 0 ? this.mToolbar.getContext().getDrawable(resId) : null);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setDefaultNavigationIcon(Drawable defaultNavigationIcon) {
        if (this.mDefaultNavigationIcon != defaultNavigationIcon) {
            this.mDefaultNavigationIcon = defaultNavigationIcon;
            updateNavigationIcon();
        }
    }

    private synchronized void updateNavigationIcon() {
        if ((this.mDisplayOpts & 4) != 0) {
            this.mToolbar.setNavigationIcon(this.mNavIcon != null ? this.mNavIcon : this.mDefaultNavigationIcon);
        } else {
            this.mToolbar.setNavigationIcon((Drawable) null);
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setNavigationContentDescription(CharSequence description) {
        this.mHomeDescription = description;
        updateHomeAccessibility();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setNavigationContentDescription(int resId) {
        setNavigationContentDescription(resId == 0 ? null : getContext().getString(resId));
    }

    private synchronized void updateHomeAccessibility() {
        if ((this.mDisplayOpts & 4) != 0) {
            if (TextUtils.isEmpty(this.mHomeDescription)) {
                this.mToolbar.setNavigationContentDescription(this.mDefaultNavigationContentDescription);
            } else {
                this.mToolbar.setNavigationContentDescription(this.mHomeDescription);
            }
        }
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void saveHierarchyState(SparseArray<Parcelable> toolbarStates) {
        this.mToolbar.saveHierarchyState(toolbarStates);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void restoreHierarchyState(SparseArray<Parcelable> toolbarStates) {
        this.mToolbar.restoreHierarchyState(toolbarStates);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setBackgroundDrawable(Drawable d) {
        this.mToolbar.setBackgroundDrawable(d);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized int getHeight() {
        return this.mToolbar.getHeight();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setVisibility(int visible) {
        this.mToolbar.setVisibility(visible);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized int getVisibility() {
        return this.mToolbar.getVisibility();
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized void setMenuCallbacks(MenuPresenter.Callback presenterCallback, MenuBuilder.Callback menuBuilderCallback) {
        this.mToolbar.setMenuCallbacks(presenterCallback, menuBuilderCallback);
    }

    @Override // com.android.internal.widget.DecorToolbar
    public synchronized Menu getMenu() {
        return this.mToolbar.getMenu();
    }
}
