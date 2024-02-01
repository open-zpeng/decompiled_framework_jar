package com.xiaopeng.view;

import android.os.RemoteException;
import com.xiaopeng.view.ISharedDisplayListener;
/* loaded from: classes3.dex */
public class SharedDisplayListener extends ISharedDisplayListener.Stub {
    @Override // com.xiaopeng.view.ISharedDisplayListener
    public void onChanged(String packageName, int sharedId) throws RemoteException {
    }

    @Override // com.xiaopeng.view.ISharedDisplayListener
    public void onPositionChanged(String packageName, int event, int from, int to) throws RemoteException {
    }

    @Override // com.xiaopeng.view.ISharedDisplayListener
    public void onActivityChanged(int screenId, String property) throws RemoteException {
    }

    @Override // com.xiaopeng.view.ISharedDisplayListener
    public void onEventChanged(int event, String property) throws RemoteException {
    }
}
