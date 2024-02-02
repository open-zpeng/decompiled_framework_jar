package com.android.internal.view.menu;

import android.content.Context;
import android.os.Parcelable;
import android.view.ViewGroup;
/* loaded from: classes3.dex */
public interface MenuPresenter {

    /* loaded from: classes3.dex */
    public interface Callback {
        synchronized void onCloseMenu(MenuBuilder menuBuilder, boolean z);

        /* JADX INFO: Access modifiers changed from: private */
        boolean onOpenSubMenu(MenuBuilder menuBuilder);
    }

    synchronized boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl);

    synchronized boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl);

    synchronized boolean flagActionItems();

    synchronized int getId();

    synchronized MenuView getMenuView(ViewGroup viewGroup);

    synchronized void initForMenu(Context context, MenuBuilder menuBuilder);

    synchronized void onCloseMenu(MenuBuilder menuBuilder, boolean z);

    synchronized void onRestoreInstanceState(Parcelable parcelable);

    synchronized Parcelable onSaveInstanceState();

    synchronized boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder);

    synchronized void setCallback(Callback callback);

    synchronized void updateMenuView(boolean z);
}
