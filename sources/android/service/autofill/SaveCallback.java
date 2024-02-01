package android.service.autofill;

import android.content.IntentSender;
import android.os.RemoteException;
import com.android.internal.util.Preconditions;
/* loaded from: classes2.dex */
public final class SaveCallback {
    private final ISaveCallback mCallback;
    private boolean mCalled;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized SaveCallback(ISaveCallback callback) {
        this.mCallback = callback;
    }

    public void onSuccess() {
        onSuccessInternal(null);
    }

    public void onSuccess(IntentSender intentSender) {
        onSuccessInternal((IntentSender) Preconditions.checkNotNull(intentSender));
    }

    private synchronized void onSuccessInternal(IntentSender intentSender) {
        assertNotCalled();
        this.mCalled = true;
        try {
            this.mCallback.onSuccess(intentSender);
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public void onFailure(CharSequence message) {
        assertNotCalled();
        this.mCalled = true;
        try {
            this.mCallback.onFailure(message);
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    private synchronized void assertNotCalled() {
        if (this.mCalled) {
            throw new IllegalStateException("Already called");
        }
    }
}
