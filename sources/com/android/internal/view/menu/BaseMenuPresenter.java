package com.android.internal.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.view.menu.MenuPresenter;
import com.android.internal.view.menu.MenuView;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public abstract class BaseMenuPresenter implements MenuPresenter {
    private MenuPresenter.Callback mCallback;
    protected Context mContext;
    private int mId;
    protected LayoutInflater mInflater;
    private int mItemLayoutRes;
    protected MenuBuilder mMenu;
    private int mMenuLayoutRes;
    protected MenuView mMenuView;
    protected Context mSystemContext;
    protected LayoutInflater mSystemInflater;

    public abstract synchronized void bindItemView(MenuItemImpl menuItemImpl, MenuView.ItemView itemView);

    public synchronized BaseMenuPresenter(Context context, int menuLayoutRes, int itemLayoutRes) {
        this.mSystemContext = context;
        this.mSystemInflater = LayoutInflater.from(context);
        this.mMenuLayoutRes = menuLayoutRes;
        this.mItemLayoutRes = itemLayoutRes;
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized void initForMenu(Context context, MenuBuilder menu) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mMenu = menu;
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized MenuView getMenuView(ViewGroup root) {
        if (this.mMenuView == null) {
            this.mMenuView = (MenuView) this.mSystemInflater.inflate(this.mMenuLayoutRes, root, false);
            this.mMenuView.initialize(this.mMenu);
            updateMenuView(true);
        }
        return this.mMenuView;
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized void updateMenuView(boolean cleared) {
        ViewGroup parent = (ViewGroup) this.mMenuView;
        if (parent == null) {
            return;
        }
        int i = 0;
        if (this.mMenu != null) {
            this.mMenu.flagActionItems();
            ArrayList<MenuItemImpl> visibleItems = this.mMenu.getVisibleItems();
            int itemCount = visibleItems.size();
            int childIndex = 0;
            for (int childIndex2 = 0; childIndex2 < itemCount; childIndex2++) {
                MenuItemImpl item = visibleItems.get(childIndex2);
                if (shouldIncludeItem(childIndex, item)) {
                    View convertView = parent.getChildAt(childIndex);
                    MenuItemImpl oldItem = convertView instanceof MenuView.ItemView ? ((MenuView.ItemView) convertView).getItemData() : null;
                    View itemView = getItemView(item, convertView, parent);
                    if (item != oldItem) {
                        itemView.setPressed(false);
                        itemView.jumpDrawablesToCurrentState();
                    }
                    if (itemView != convertView) {
                        addItemView(itemView, childIndex);
                    }
                    childIndex++;
                }
            }
            i = childIndex;
        }
        while (i < parent.getChildCount()) {
            if (!filterLeftoverView(parent, i)) {
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void addItemView(View itemView, int childIndex) {
        ViewGroup currentParent = (ViewGroup) itemView.getParent();
        if (currentParent != null) {
            currentParent.removeView(itemView);
        }
        ((ViewGroup) this.mMenuView).addView(itemView, childIndex);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized boolean filterLeftoverView(ViewGroup parent, int childIndex) {
        parent.removeViewAt(childIndex);
        return true;
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized void setCallback(MenuPresenter.Callback cb) {
        this.mCallback = cb;
    }

    public synchronized MenuPresenter.Callback getCallback() {
        return this.mCallback;
    }

    public synchronized MenuView.ItemView createItemView(ViewGroup parent) {
        return (MenuView.ItemView) this.mSystemInflater.inflate(this.mItemLayoutRes, parent, false);
    }

    public synchronized View getItemView(MenuItemImpl item, View convertView, ViewGroup parent) {
        MenuView.ItemView itemView;
        if (convertView instanceof MenuView.ItemView) {
            itemView = (MenuView.ItemView) convertView;
        } else {
            itemView = createItemView(parent);
        }
        bindItemView(item, itemView);
        return (View) itemView;
    }

    public synchronized boolean shouldIncludeItem(int childIndex, MenuItemImpl item) {
        return true;
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        if (this.mCallback != null) {
            this.mCallback.onCloseMenu(menu, allMenusAreClosing);
        }
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized boolean onSubMenuSelected(SubMenuBuilder menu) {
        if (this.mCallback != null) {
            return this.mCallback.onOpenSubMenu(menu);
        }
        return false;
    }

    @Override // com.android.internal.view.menu.MenuPresenter
    public synchronized boolean flagActionItems() {
        return false;
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
        return this.mId;
    }

    public synchronized void setId(int id) {
        this.mId = id;
    }
}
