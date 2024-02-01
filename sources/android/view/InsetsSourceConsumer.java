package android.view;

import android.view.SurfaceControl;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Supplier;

/* loaded from: classes3.dex */
public class InsetsSourceConsumer {
    protected final InsetsController mController;
    private InsetsSourceControl mSourceControl;
    private final InsetsState mState;
    private final Supplier<SurfaceControl.Transaction> mTransactionSupplier;
    private final int mType;
    protected boolean mVisible;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    @interface ShowResult {
        public static final int SHOW_DELAYED = 1;
        public static final int SHOW_FAILED = 2;
        public static final int SHOW_IMMEDIATELY = 0;
    }

    public InsetsSourceConsumer(int type, InsetsState state, Supplier<SurfaceControl.Transaction> transactionSupplier, InsetsController controller) {
        this.mType = type;
        this.mState = state;
        this.mTransactionSupplier = transactionSupplier;
        this.mController = controller;
        this.mVisible = InsetsState.getDefaultVisibility(type);
    }

    public void setControl(InsetsSourceControl control) {
        if (this.mSourceControl == control) {
            return;
        }
        this.mSourceControl = control;
        applyHiddenToControl();
        if (applyLocalVisibilityOverride()) {
            this.mController.notifyVisibilityChanged();
        }
        if (this.mSourceControl == null) {
            this.mController.notifyControlRevoked(this);
        }
    }

    @VisibleForTesting
    public InsetsSourceControl getControl() {
        return this.mSourceControl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getType() {
        return this.mType;
    }

    @VisibleForTesting
    public void show() {
        setVisible(true);
    }

    @VisibleForTesting
    public void hide() {
        setVisible(false);
    }

    public void onWindowFocusGained() {
    }

    public void onWindowFocusLost() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean applyLocalVisibilityOverride() {
        if (this.mSourceControl == null || this.mState.getSource(this.mType).isVisible() == this.mVisible) {
            return false;
        }
        this.mState.getSource(this.mType).setVisible(this.mVisible);
        return true;
    }

    @VisibleForTesting
    public boolean isVisible() {
        return this.mVisible;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int requestShow(boolean fromController) {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyHidden() {
    }

    private void setVisible(boolean visible) {
        if (this.mVisible == visible) {
            return;
        }
        this.mVisible = visible;
        applyHiddenToControl();
        applyLocalVisibilityOverride();
        this.mController.notifyVisibilityChanged();
    }

    private void applyHiddenToControl() {
        if (this.mSourceControl == null) {
            return;
        }
        SurfaceControl.Transaction t = this.mTransactionSupplier.get();
        if (this.mVisible) {
            t.show(this.mSourceControl.getLeash());
        } else {
            t.hide(this.mSourceControl.getLeash());
        }
        t.apply();
    }
}
