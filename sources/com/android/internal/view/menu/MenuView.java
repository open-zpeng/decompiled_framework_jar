package com.android.internal.view.menu;

import android.graphics.drawable.Drawable;
/* loaded from: classes3.dex */
public interface MenuView {

    /* loaded from: classes3.dex */
    public interface ItemView {
        /* JADX INFO: Access modifiers changed from: private */
        MenuItemImpl getItemData();

        synchronized void initialize(MenuItemImpl menuItemImpl, int i);

        synchronized boolean prefersCondensedTitle();

        synchronized void setCheckable(boolean z);

        synchronized void setChecked(boolean z);

        synchronized void setEnabled(boolean z);

        synchronized void setIcon(Drawable drawable);

        synchronized void setShortcut(boolean z, char c);

        synchronized void setTitle(CharSequence charSequence);

        synchronized boolean showsIcon();
    }

    /* JADX INFO: Access modifiers changed from: private */
    int getWindowAnimations();

    synchronized void initialize(MenuBuilder menuBuilder);
}
