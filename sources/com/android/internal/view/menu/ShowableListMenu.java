package com.android.internal.view.menu;

import android.widget.ListView;
/* loaded from: classes3.dex */
public interface ShowableListMenu {
    synchronized void dismiss();

    synchronized ListView getListView();

    synchronized boolean isShowing();

    synchronized void show();
}
