package com.android.internal.view.menu;

import com.android.internal.view.menu.MenuPresenter;
/* loaded from: classes3.dex */
public interface MenuHelper {
    synchronized void dismiss();

    synchronized void setPresenterCallback(MenuPresenter.Callback callback);
}
