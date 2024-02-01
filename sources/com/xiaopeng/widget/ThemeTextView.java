package com.xiaopeng.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.TextView;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes3.dex */
public class ThemeTextView extends TextView {
    private ThemeViewModel mThemeViewModel;

    public ThemeTextView(Context context) {
        super(context);
        this.mThemeViewModel = ThemeViewModel.create(context, null, 0, 0);
    }

    public ThemeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mThemeViewModel = ThemeViewModel.create(context, attrs, 0, 0);
    }

    public ThemeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mThemeViewModel = ThemeViewModel.create(context, attrs, defStyleAttr, 0);
    }

    public ThemeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mThemeViewModel = ThemeViewModel.create(context, attrs, defStyleAttr, defStyleRes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mThemeViewModel.onConfigurationChanged(this, newConfig);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mThemeViewModel.onAttachedToWindow(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mThemeViewModel.onDetachedFromWindow(this);
    }
}
