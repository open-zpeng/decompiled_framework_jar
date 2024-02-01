package android.view;

import android.os.Parcel;
import android.text.TextUtils;
import android.view.SurfaceControl;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Arrays;
import java.util.function.Supplier;

/* loaded from: classes3.dex */
public final class ImeInsetsSourceConsumer extends InsetsSourceConsumer {
    private EditorInfo mFocusedEditor;
    private boolean mHasWindowFocus;
    private EditorInfo mPreRenderedEditor;
    private boolean mShowOnNextImeRender;

    public ImeInsetsSourceConsumer(InsetsState state, Supplier<SurfaceControl.Transaction> transactionSupplier, InsetsController controller) {
        super(10, state, transactionSupplier, controller);
    }

    public void onPreRendered(EditorInfo info) {
        this.mPreRenderedEditor = info;
        if (this.mShowOnNextImeRender) {
            this.mShowOnNextImeRender = false;
            if (isServedEditorRendered()) {
                applyImeVisibility(true);
            }
        }
    }

    public void onServedEditorChanged(EditorInfo info) {
        if (isDummyOrEmptyEditor(info)) {
            this.mShowOnNextImeRender = false;
        }
        this.mFocusedEditor = info;
    }

    public void applyImeVisibility(boolean setVisible) {
        if (!this.mHasWindowFocus) {
            return;
        }
        this.mController.applyImeVisibility(setVisible);
    }

    @Override // android.view.InsetsSourceConsumer
    public void onWindowFocusGained() {
        this.mHasWindowFocus = true;
        getImm().registerImeConsumer(this);
    }

    @Override // android.view.InsetsSourceConsumer
    public void onWindowFocusLost() {
        this.mHasWindowFocus = false;
        getImm().unregisterImeConsumer(this);
    }

    @Override // android.view.InsetsSourceConsumer
    int requestShow(boolean fromIme) {
        if (fromIme) {
            return 0;
        }
        return getImm().requestImeShow(null) ? 1 : 2;
    }

    @Override // android.view.InsetsSourceConsumer
    void notifyHidden() {
        getImm().notifyImeHidden();
    }

    private boolean isDummyOrEmptyEditor(EditorInfo info) {
        return info == null || (info.fieldId <= 0 && info.inputType <= 0);
    }

    private boolean isServedEditorRendered() {
        EditorInfo editorInfo = this.mFocusedEditor;
        if (editorInfo == null || this.mPreRenderedEditor == null || isDummyOrEmptyEditor(editorInfo) || isDummyOrEmptyEditor(this.mPreRenderedEditor)) {
            return false;
        }
        return areEditorsSimilar(this.mFocusedEditor, this.mPreRenderedEditor);
    }

    @VisibleForTesting
    public static boolean areEditorsSimilar(EditorInfo info1, EditorInfo info2) {
        boolean areOptionsSimilar = info1.imeOptions == info2.imeOptions && info1.inputType == info2.inputType && TextUtils.equals(info1.packageName, info2.packageName);
        if (!areOptionsSimilar || !(info1.privateImeOptions != null ? info1.privateImeOptions.equals(info2.privateImeOptions) : true)) {
            return false;
        }
        if ((info1.extras == null && info2.extras == null) || info1.extras == info2.extras) {
            return true;
        }
        if ((info1.extras != null || info2.extras == null) && (info1.extras != null || info2.extras == null)) {
            if (info1.extras.hashCode() == info2.extras.hashCode() || info1.extras.equals(info1)) {
                return true;
            }
            if (info1.extras.size() != info2.extras.size()) {
                return false;
            }
            if (info1.extras.toString().equals(info2.extras.toString())) {
                return true;
            }
            Parcel parcel1 = Parcel.obtain();
            info1.extras.writeToParcel(parcel1, 0);
            parcel1.setDataPosition(0);
            Parcel parcel2 = Parcel.obtain();
            info2.extras.writeToParcel(parcel2, 0);
            parcel2.setDataPosition(0);
            return Arrays.equals(parcel1.createByteArray(), parcel2.createByteArray());
        }
        return false;
    }

    private InputMethodManager getImm() {
        return (InputMethodManager) this.mController.getViewRoot().mContext.getSystemService(InputMethodManager.class);
    }
}
