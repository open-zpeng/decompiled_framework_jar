package android.app.contentsuggestions;

import android.annotation.SystemApi;
import android.app.contentsuggestions.ContentSuggestionsManager;
import android.app.contentsuggestions.IClassificationsCallback;
import android.app.contentsuggestions.ISelectionsCallback;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.SyncResultReceiver;
import java.util.List;
import java.util.concurrent.Executor;

@SystemApi
/* loaded from: classes.dex */
public final class ContentSuggestionsManager {
    public static final String EXTRA_BITMAP = "android.contentsuggestions.extra.BITMAP";
    private static final int SYNC_CALLS_TIMEOUT_MS = 5000;
    private static final String TAG = ContentSuggestionsManager.class.getSimpleName();
    private final IContentSuggestionsManager mService;
    private final int mUser;

    /* loaded from: classes.dex */
    public interface ClassificationsCallback {
        void onContentClassificationsAvailable(int i, List<ContentClassification> list);
    }

    /* loaded from: classes.dex */
    public interface SelectionsCallback {
        void onContentSelectionsAvailable(int i, List<ContentSelection> list);
    }

    public ContentSuggestionsManager(int userId, IContentSuggestionsManager service) {
        this.mService = service;
        this.mUser = userId;
    }

    public void provideContextImage(int taskId, Bundle imageContextRequestExtras) {
        IContentSuggestionsManager iContentSuggestionsManager = this.mService;
        if (iContentSuggestionsManager == null) {
            Log.e(TAG, "provideContextImage called, but no ContentSuggestionsManager configured");
            return;
        }
        try {
            iContentSuggestionsManager.provideContextImage(this.mUser, taskId, imageContextRequestExtras);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void suggestContentSelections(SelectionsRequest request, Executor callbackExecutor, SelectionsCallback callback) {
        IContentSuggestionsManager iContentSuggestionsManager = this.mService;
        if (iContentSuggestionsManager == null) {
            Log.e(TAG, "suggestContentSelections called, but no ContentSuggestionsManager configured");
            return;
        }
        try {
            iContentSuggestionsManager.suggestContentSelections(this.mUser, request, new SelectionsCallbackWrapper(callback, callbackExecutor));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void classifyContentSelections(ClassificationsRequest request, Executor callbackExecutor, ClassificationsCallback callback) {
        IContentSuggestionsManager iContentSuggestionsManager = this.mService;
        if (iContentSuggestionsManager == null) {
            Log.e(TAG, "classifyContentSelections called, but no ContentSuggestionsManager configured");
            return;
        }
        try {
            iContentSuggestionsManager.classifyContentSelections(this.mUser, request, new ClassificationsCallbackWrapper(callback, callbackExecutor));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void notifyInteraction(String requestId, Bundle interaction) {
        IContentSuggestionsManager iContentSuggestionsManager = this.mService;
        if (iContentSuggestionsManager == null) {
            Log.e(TAG, "notifyInteraction called, but no ContentSuggestionsManager configured");
            return;
        }
        try {
            iContentSuggestionsManager.notifyInteraction(this.mUser, requestId, interaction);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public boolean isEnabled() {
        if (this.mService == null) {
            return false;
        }
        SyncResultReceiver receiver = new SyncResultReceiver(5000);
        try {
            this.mService.isEnabled(this.mUser, receiver);
            return receiver.getIntResult() != 0;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SelectionsCallbackWrapper extends ISelectionsCallback.Stub {
        private final SelectionsCallback mCallback;
        private final Executor mExecutor;

        SelectionsCallbackWrapper(SelectionsCallback callback, Executor executor) {
            this.mCallback = callback;
            this.mExecutor = executor;
        }

        @Override // android.app.contentsuggestions.ISelectionsCallback
        public void onContentSelectionsAvailable(final int statusCode, final List<ContentSelection> selections) {
            long identity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.app.contentsuggestions.-$$Lambda$ContentSuggestionsManager$SelectionsCallbackWrapper$1Py0lukljawDYbfwobeRIUDvpNM
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContentSuggestionsManager.SelectionsCallbackWrapper.this.lambda$onContentSelectionsAvailable$0$ContentSuggestionsManager$SelectionsCallbackWrapper(statusCode, selections);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }

        public /* synthetic */ void lambda$onContentSelectionsAvailable$0$ContentSuggestionsManager$SelectionsCallbackWrapper(int statusCode, List selections) {
            this.mCallback.onContentSelectionsAvailable(statusCode, selections);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ClassificationsCallbackWrapper extends IClassificationsCallback.Stub {
        private final ClassificationsCallback mCallback;
        private final Executor mExecutor;

        ClassificationsCallbackWrapper(ClassificationsCallback callback, Executor executor) {
            this.mCallback = callback;
            this.mExecutor = executor;
        }

        @Override // android.app.contentsuggestions.IClassificationsCallback
        public void onContentClassificationsAvailable(final int statusCode, final List<ContentClassification> classifications) {
            long identity = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new Runnable() { // from class: android.app.contentsuggestions.-$$Lambda$ContentSuggestionsManager$ClassificationsCallbackWrapper$bS71fhWJJl2gObzWDnBMzvYmM5w
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContentSuggestionsManager.ClassificationsCallbackWrapper.this.lambda$onContentClassificationsAvailable$0$ContentSuggestionsManager$ClassificationsCallbackWrapper(statusCode, classifications);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }

        public /* synthetic */ void lambda$onContentClassificationsAvailable$0$ContentSuggestionsManager$ClassificationsCallbackWrapper(int statusCode, List classifications) {
            this.mCallback.onContentClassificationsAvailable(statusCode, classifications);
        }
    }
}
