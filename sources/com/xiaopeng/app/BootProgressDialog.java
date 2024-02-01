package com.xiaopeng.app;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.R;
/* loaded from: classes3.dex */
public class BootProgressDialog extends AlertDialog {
    private ImageView mDialogLoading;
    private TextView mDialogMessage;
    private Animation mRotateAnimation;

    public BootProgressDialog(Context context) {
        this(context, R.style.Theme_Dialog_BootProgress);
    }

    public BootProgressDialog(Context context, int themeResId) {
        super(context, R.style.Theme_Dialog_BootProgress);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.AlertDialog, android.app.Dialog
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.type = WindowManager.LayoutParams.TYPE_BOOT_PROGRESS;
        lp.width = -1;
        lp.height = -1;
        lp.gravity = 17;
        getWindow().getDecorView().setSystemUiVisibility(3846);
        lp.surfaceInsets.set(0, 0, 0, 0);
        getWindow().setAttributes(lp);
        setContentView(R.layout.boot_dialog_progress);
        this.mDialogLoading = (ImageView) findViewById(R.id.id_dialog_loading);
        this.mDialogMessage = (TextView) findViewById(R.id.id_dialog_message);
        this.mRotateAnimation = new RotateAnimation(0.0f, 359.0f, 1, 0.5f, 1, 0.5f);
        this.mRotateAnimation.setInterpolator(new LinearInterpolator());
        this.mRotateAnimation.setRepeatCount(-1);
        this.mRotateAnimation.setDuration(1000L);
    }

    @Override // android.app.AlertDialog
    public void setMessage(CharSequence message) {
        if (this.mDialogMessage != null) {
            this.mDialogMessage.setText(message);
        }
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
        if (this.mDialogLoading != null) {
            this.mDialogLoading.startAnimation(this.mRotateAnimation);
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        if (this.mDialogLoading != null) {
            this.mDialogLoading.clearAnimation();
        }
        super.dismiss();
    }
}
