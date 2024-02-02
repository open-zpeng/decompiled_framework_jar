package com.xiaopeng.sysconfig;
/* loaded from: classes3.dex */
public interface SysConfigManager {

    /* loaded from: classes3.dex */
    public interface SysConfigListener {
        void onSysConfigUpdated(String str);
    }

    void registerConfigUpdateListener(SysConfigListener sysConfigListener);

    void unregisterConfigUpdateListener(SysConfigListener sysConfigListener);
}
