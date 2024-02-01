package com.android.internal.widget;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuPresenter;
/* loaded from: classes3.dex */
public interface DecorToolbar {
    synchronized void animateToVisibility(int i);

    synchronized boolean canShowOverflowMenu();

    synchronized boolean canSplit();

    synchronized void collapseActionView();

    synchronized void dismissPopupMenus();

    synchronized Context getContext();

    synchronized View getCustomView();

    synchronized int getDisplayOptions();

    synchronized int getDropdownItemCount();

    synchronized int getDropdownSelectedPosition();

    synchronized int getHeight();

    synchronized Menu getMenu();

    synchronized int getNavigationMode();

    synchronized CharSequence getSubtitle();

    synchronized CharSequence getTitle();

    synchronized ViewGroup getViewGroup();

    synchronized int getVisibility();

    synchronized boolean hasEmbeddedTabs();

    synchronized boolean hasExpandedActionView();

    synchronized boolean hasIcon();

    synchronized boolean hasLogo();

    synchronized boolean hideOverflowMenu();

    synchronized void initIndeterminateProgress();

    synchronized void initProgress();

    synchronized boolean isOverflowMenuShowPending();

    synchronized boolean isOverflowMenuShowing();

    synchronized boolean isSplit();

    synchronized boolean isTitleTruncated();

    synchronized void restoreHierarchyState(SparseArray<Parcelable> sparseArray);

    synchronized void saveHierarchyState(SparseArray<Parcelable> sparseArray);

    synchronized void setBackgroundDrawable(Drawable drawable);

    synchronized void setCollapsible(boolean z);

    synchronized void setCustomView(View view);

    synchronized void setDefaultNavigationContentDescription(int i);

    synchronized void setDefaultNavigationIcon(Drawable drawable);

    synchronized void setDisplayOptions(int i);

    synchronized void setDropdownParams(SpinnerAdapter spinnerAdapter, AdapterView.OnItemSelectedListener onItemSelectedListener);

    synchronized void setDropdownSelectedPosition(int i);

    synchronized void setEmbeddedTabView(ScrollingTabContainerView scrollingTabContainerView);

    synchronized void setHomeButtonEnabled(boolean z);

    synchronized void setIcon(int i);

    synchronized void setIcon(Drawable drawable);

    synchronized void setLogo(int i);

    synchronized void setLogo(Drawable drawable);

    synchronized void setMenu(Menu menu, MenuPresenter.Callback callback);

    synchronized void setMenuCallbacks(MenuPresenter.Callback callback, MenuBuilder.Callback callback2);

    synchronized void setMenuPrepared();

    synchronized void setNavigationContentDescription(int i);

    synchronized void setNavigationContentDescription(CharSequence charSequence);

    synchronized void setNavigationIcon(int i);

    synchronized void setNavigationIcon(Drawable drawable);

    synchronized void setNavigationMode(int i);

    synchronized void setSplitToolbar(boolean z);

    synchronized void setSplitView(ViewGroup viewGroup);

    synchronized void setSplitWhenNarrow(boolean z);

    synchronized void setSubtitle(CharSequence charSequence);

    synchronized void setTitle(CharSequence charSequence);

    synchronized void setVisibility(int i);

    synchronized void setWindowCallback(Window.Callback callback);

    synchronized void setWindowTitle(CharSequence charSequence);

    synchronized Animator setupAnimatorToVisibility(int i, long j);

    synchronized boolean showOverflowMenu();
}
