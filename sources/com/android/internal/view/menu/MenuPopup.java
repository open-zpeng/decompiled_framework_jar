package com.android.internal.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
/* loaded from: classes3.dex */
public abstract class MenuPopup implements ShowableListMenu, MenuPresenter, AdapterView.OnItemClickListener {
    private Rect mEpicenterBounds;

    public abstract synchronized void addMenu(MenuBuilder menuBuilder);

    public abstract synchronized void setAnchorView(View view);

    public abstract synchronized void setForceShowIcon(boolean z);

    public abstract synchronized void setGravity(int i);

    public abstract synchronized void setHorizontalOffset(int i);

    public abstract synchronized void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener);

    public abstract synchronized void setShowTitle(boolean z);

    public abstract synchronized void setVerticalOffset(int i);

    public synchronized void setEpicenterBounds(Rect bounds) {
        this.mEpicenterBounds = bounds;
    }

    public synchronized Rect getEpicenterBounds() {
        return this.mEpicenterBounds;
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized void initForMenu(Context context, MenuBuilder menu) {
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized MenuView getMenuView(ViewGroup root) {
        throw new UnsupportedOperationException("MenuPopups manage their own views");
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized boolean expandItemActionView(MenuBuilder menu, MenuItemImpl item) {
        return false;
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
        return false;
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized int getId() {
        return 0;
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListAdapter outerAdapter = (ListAdapter) parent.getAdapter();
        MenuAdapter wrappedAdapter = toMenuAdapter(outerAdapter);
        wrappedAdapter.mAdapterMenu.performItemAction((MenuItem) outerAdapter.getItem(position), 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized int measureIndividualMenuWidth(ListAdapter adapter, ViewGroup parent, Context context, int maxAllowedWidth) {
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            if (parent == null) {
                parent = new FrameLayout(context);
            }
            itemView = adapter.getView(i, itemView, parent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            int itemWidth = itemView.getMeasuredWidth();
            if (itemWidth >= maxAllowedWidth) {
                return maxAllowedWidth;
            }
            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
        }
        return maxWidth;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized MenuAdapter toMenuAdapter(ListAdapter adapter) {
        if (adapter instanceof HeaderViewListAdapter) {
            return (MenuAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        }
        return (MenuAdapter) adapter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized boolean shouldPreserveIconSpacing(MenuBuilder menu) {
        int count = menu.size();
        for (int i = 0; i < count; i++) {
            MenuItem childItem = menu.getItem(i);
            if (childItem.isVisible() && childItem.getIcon() != null) {
                return true;
            }
        }
        return false;
    }
}
