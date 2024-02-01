package com.android.internal.policy;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import java.lang.ref.WeakReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class DecorContext extends ContextThemeWrapper {
    private WeakReference<Context> mActivityContext;
    private Resources mActivityResources;
    private PhoneWindow mPhoneWindow;
    private WindowManager mWindowManager;

    public DecorContext(Context context, Context activityContext) {
        super(context, (Resources.Theme) null);
        this.mActivityContext = new WeakReference<>(activityContext);
        this.mActivityResources = activityContext.getResources();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPhoneWindow(PhoneWindow phoneWindow) {
        this.mPhoneWindow = phoneWindow;
        this.mWindowManager = null;
    }

    @Override // android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public Object getSystemService(String name) {
        if (Context.WINDOW_SERVICE.equals(name)) {
            if (this.mWindowManager == null) {
                WindowManagerImpl wm = (WindowManagerImpl) super.getSystemService(Context.WINDOW_SERVICE);
                this.mWindowManager = wm.createLocalWindowManager(this.mPhoneWindow);
            }
            return this.mWindowManager;
        }
        return super.getSystemService(name);
    }

    @Override // android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        Context activityContext = this.mActivityContext.get();
        if (activityContext != null) {
            this.mActivityResources = activityContext.getResources();
        }
        return this.mActivityResources;
    }

    @Override // android.view.ContextThemeWrapper, android.content.ContextWrapper, android.content.Context
    public AssetManager getAssets() {
        return this.mActivityResources.getAssets();
    }
}
