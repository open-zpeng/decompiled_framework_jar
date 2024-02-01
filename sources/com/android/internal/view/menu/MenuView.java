package com.android.internal.view.menu;

import android.annotation.UnsupportedAppUsage;
import android.graphics.drawable.Drawable;

/* loaded from: classes3.dex */
public interface MenuView {

    /* loaded from: classes3.dex */
    public interface ItemView {
        @UnsupportedAppUsage
        MenuItemImpl getItemData();

        void initialize(MenuItemImpl menuItemImpl, int i);

        boolean prefersCondensedTitle();

        void setCheckable(boolean z);

        void setChecked(boolean z);

        void setEnabled(boolean z);

        void setIcon(Drawable drawable);

        void setShortcut(boolean z, char c);

        void setTitle(CharSequence charSequence);

        boolean showsIcon();
    }

    @UnsupportedAppUsage
    int getWindowAnimations();

    void initialize(MenuBuilder menuBuilder);
}
