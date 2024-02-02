package android.service.autofill;

import android.os.RemoteException;
/* loaded from: classes2.dex */
public final class FillCallback {
    private final IFillCallback mCallback;
    private boolean mCalled;
    private final int mRequestId;

    public synchronized FillCallback(IFillCallback callback, int requestId) {
        this.mCallback = callback;
        this.mRequestId = requestId;
    }

    public void onSuccess(FillResponse response) {
        assertNotCalled();
        this.mCalled = true;
        if (response != null) {
            response.setRequestId(this.mRequestId);
        }
        try {
            this.mCallback.onSuccess(response);
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public void onFailure(CharSequence message) {
        assertNotCalled();
        this.mCalled = true;
        try {
            this.mCallback.onFailure(this.mRequestId, message);
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
