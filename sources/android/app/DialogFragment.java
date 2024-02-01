package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import com.android.internal.R;
import java.io.FileDescriptor;
import java.io.PrintWriter;

@Deprecated
/* loaded from: classes.dex */
public class DialogFragment extends Fragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
    private static final String SAVED_BACK_STACK_ID = "android:backStackId";
    private static final String SAVED_CANCELABLE = "android:cancelable";
    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
    private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
    private static final String SAVED_STYLE = "android:style";
    private static final String SAVED_THEME = "android:theme";
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_NO_FRAME = 2;
    public static final int STYLE_NO_INPUT = 3;
    public static final int STYLE_NO_TITLE = 1;
    Dialog mDialog;
    @UnsupportedAppUsage
    boolean mDismissed;
    @UnsupportedAppUsage
    boolean mShownByMe;
    @UnsupportedAppUsage
    boolean mViewDestroyed;
    int mStyle = 0;
    int mTheme = 0;
    boolean mCancelable = true;
    boolean mShowsDialog = true;
    @UnsupportedAppUsage
    int mBackStackId = -1;

    public void setStyle(int style, int theme) {
        this.mStyle = style;
        int i = this.mStyle;
        if (i == 2 || i == 3) {
            this.mTheme = R.style.Theme_DeviceDefault_Dialog_NoFrame;
        }
        if (theme != 0) {
            this.mTheme = theme;
        }
    }

    public void show(FragmentManager manager, String tag) {
        this.mDismissed = false;
        this.mShownByMe = true;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commit();
    }

    @UnsupportedAppUsage
    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        this.mDismissed = false;
        this.mShownByMe = true;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    public int show(FragmentTransaction transaction, String tag) {
        this.mDismissed = false;
        this.mShownByMe = true;
        transaction.add(this, tag);
        this.mViewDestroyed = false;
        this.mBackStackId = transaction.commit();
        return this.mBackStackId;
    }

    public void dismiss() {
        dismissInternal(false);
    }

    public void dismissAllowingStateLoss() {
        dismissInternal(true);
    }

    void dismissInternal(boolean allowStateLoss) {
        if (this.mDismissed) {
            return;
        }
        this.mDismissed = true;
        this.mShownByMe = false;
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.dismiss();
            this.mDialog = null;
        }
        this.mViewDestroyed = true;
        if (this.mBackStackId >= 0) {
            getFragmentManager().popBackStack(this.mBackStackId, 1);
            this.mBackStackId = -1;
            return;
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(this);
        if (allowStateLoss) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public int getTheme() {
        return this.mTheme;
    }

    public void setCancelable(boolean cancelable) {
        this.mCancelable = cancelable;
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.setCancelable(cancelable);
        }
    }

    public boolean isCancelable() {
        return this.mCancelable;
    }

    public void setShowsDialog(boolean showsDialog) {
        this.mShowsDialog = showsDialog;
    }

    public boolean getShowsDialog() {
        return this.mShowsDialog;
    }

    @Override // android.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!this.mShownByMe) {
            this.mDismissed = false;
        }
    }

    @Override // android.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (!this.mShownByMe && !this.mDismissed) {
            this.mDismissed = true;
        }
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mShowsDialog = this.mContainerId == 0;
        if (savedInstanceState != null) {
            this.mStyle = savedInstanceState.getInt(SAVED_STYLE, 0);
            this.mTheme = savedInstanceState.getInt(SAVED_THEME, 0);
            this.mCancelable = savedInstanceState.getBoolean(SAVED_CANCELABLE, true);
            this.mShowsDialog = savedInstanceState.getBoolean(SAVED_SHOWS_DIALOG, this.mShowsDialog);
            this.mBackStackId = savedInstanceState.getInt(SAVED_BACK_STACK_ID, -1);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x003c  */
    @Override // android.app.Fragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.view.LayoutInflater onGetLayoutInflater(android.os.Bundle r4) {
        /*
            r3 = this;
            boolean r0 = r3.mShowsDialog
            if (r0 != 0) goto L9
            android.view.LayoutInflater r0 = super.onGetLayoutInflater(r4)
            return r0
        L9:
            android.app.Dialog r0 = r3.onCreateDialog(r4)
            r3.mDialog = r0
            int r0 = r3.mStyle
            r1 = 1
            if (r0 == r1) goto L26
            r2 = 2
            if (r0 == r2) goto L26
            r2 = 3
            if (r0 == r2) goto L1b
            goto L2b
        L1b:
            android.app.Dialog r0 = r3.mDialog
            android.view.Window r0 = r0.getWindow()
            r2 = 24
            r0.addFlags(r2)
        L26:
            android.app.Dialog r0 = r3.mDialog
            r0.requestWindowFeature(r1)
        L2b:
            android.app.Dialog r0 = r3.mDialog
            java.lang.String r1 = "layout_inflater"
            if (r0 == 0) goto L3c
            android.content.Context r0 = r0.getContext()
            java.lang.Object r0 = r0.getSystemService(r1)
            android.view.LayoutInflater r0 = (android.view.LayoutInflater) r0
            return r0
        L3c:
            android.app.FragmentHostCallback r0 = r3.mHost
            android.content.Context r0 = r0.getContext()
            java.lang.Object r0 = r0.getSystemService(r1)
            android.view.LayoutInflater r0 = (android.view.LayoutInflater) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.DialogFragment.onGetLayoutInflater(android.os.Bundle):android.view.LayoutInflater");
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme());
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialog) {
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        if (!this.mViewDestroyed) {
            dismissInternal(true);
        }
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle dialogState;
        super.onActivityCreated(savedInstanceState);
        if (!this.mShowsDialog) {
            return;
        }
        View view = getView();
        if (view != null) {
            if (view.getParent() != null) {
                throw new IllegalStateException("DialogFragment can not be attached to a container view");
            }
            this.mDialog.setContentView(view);
        }
        Activity activity = getActivity();
        if (activity != null) {
            this.mDialog.setOwnerActivity(activity);
        }
        this.mDialog.setCancelable(this.mCancelable);
        if (!this.mDialog.takeCancelAndDismissListeners("DialogFragment", this, this)) {
            throw new IllegalStateException("You can not set Dialog's OnCancelListener or OnDismissListener");
        }
        if (savedInstanceState != null && (dialogState = savedInstanceState.getBundle(SAVED_DIALOG_STATE_TAG)) != null) {
            this.mDialog.onRestoreInstanceState(dialogState);
        }
    }

    @Override // android.app.Fragment
    public void onStart() {
        super.onStart();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            this.mViewDestroyed = false;
            dialog.show();
        }
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        Bundle dialogState;
        super.onSaveInstanceState(outState);
        Dialog dialog = this.mDialog;
        if (dialog != null && (dialogState = dialog.onSaveInstanceState()) != null) {
            outState.putBundle(SAVED_DIALOG_STATE_TAG, dialogState);
        }
        int i = this.mStyle;
        if (i != 0) {
            outState.putInt(SAVED_STYLE, i);
        }
        int i2 = this.mTheme;
        if (i2 != 0) {
            outState.putInt(SAVED_THEME, i2);
        }
        boolean z = this.mCancelable;
        if (!z) {
            outState.putBoolean(SAVED_CANCELABLE, z);
        }
        boolean z2 = this.mShowsDialog;
        if (!z2) {
            outState.putBoolean(SAVED_SHOWS_DIALOG, z2);
        }
        int i3 = this.mBackStackId;
        if (i3 != -1) {
            outState.putInt(SAVED_BACK_STACK_ID, i3);
        }
    }

    @Override // android.app.Fragment
    public void onStop() {
        super.onStop();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.hide();
        }
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            this.mViewDestroyed = true;
            dialog.dismiss();
            this.mDialog = null;
        }
    }

    @Override // android.app.Fragment
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        writer.print(prefix);
        writer.println("DialogFragment:");
        writer.print(prefix);
        writer.print("  mStyle=");
        writer.print(this.mStyle);
        writer.print(" mTheme=0x");
        writer.println(Integer.toHexString(this.mTheme));
        writer.print(prefix);
        writer.print("  mCancelable=");
        writer.print(this.mCancelable);
        writer.print(" mShowsDialog=");
        writer.print(this.mShowsDialog);
        writer.print(" mBackStackId=");
        writer.println(this.mBackStackId);
        writer.print(prefix);
        writer.print("  mDialog=");
        writer.println(this.mDialog);
        writer.print(prefix);
        writer.print("  mViewDestroyed=");
        writer.print(this.mViewDestroyed);
        writer.print(" mDismissed=");
        writer.print(this.mDismissed);
        writer.print(" mShownByMe=");
        writer.println(this.mShownByMe);
    }
}
