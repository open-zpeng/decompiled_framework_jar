package android.service.textclassifier;

import android.Manifest;
import android.annotation.SystemApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.RemoteException;
import android.service.textclassifier.ITextClassifierService;
import android.text.TextUtils;
import android.util.Slog;
import android.view.textclassifier.SelectionEvent;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassificationContext;
import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassificationSessionId;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
import com.android.internal.util.Preconditions;
@SystemApi
/* loaded from: classes2.dex */
public abstract class TextClassifierService extends Service {
    private static final String LOG_TAG = "TextClassifierService";
    @SystemApi
    public static final String SERVICE_INTERFACE = "android.service.textclassifier.TextClassifierService";
    private final ITextClassifierService.Stub mBinder = new ITextClassifierService.Stub() { // from class: android.service.textclassifier.TextClassifierService.1
        private final CancellationSignal mCancellationSignal = new CancellationSignal();

        @Override // android.service.textclassifier.ITextClassifierService
        public void onSuggestSelection(TextClassificationSessionId sessionId, TextSelection.Request request, final ITextSelectionCallback callback) throws RemoteException {
            Preconditions.checkNotNull(request);
            Preconditions.checkNotNull(callback);
            TextClassifierService.this.onSuggestSelection(request.getText(), request.getStartIndex(), request.getEndIndex(), TextSelection.Options.from(sessionId, request), this.mCancellationSignal, new Callback<TextSelection>() { // from class: android.service.textclassifier.TextClassifierService.1.1
                @Override // android.service.textclassifier.TextClassifierService.Callback
                public void onSuccess(TextSelection result) {
                    try {
                        callback.onSuccess(result);
                    } catch (RemoteException e) {
                        Slog.d(TextClassifierService.LOG_TAG, "Error calling callback");
                    }
                }

                @Override // android.service.textclassifier.TextClassifierService.Callback
                public void onFailure(CharSequence error) {
                    try {
                        if (callback.asBinder().isBinderAlive()) {
                            callback.onFailure();
                        }
                    } catch (RemoteException e) {
                        Slog.d(TextClassifierService.LOG_TAG, "Error calling callback");
                    }
                }
            });
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onClassifyText(TextClassificationSessionId sessionId, TextClassification.Request request, final ITextClassificationCallback callback) throws RemoteException {
            Preconditions.checkNotNull(request);
            Preconditions.checkNotNull(callback);
            TextClassifierService.this.onClassifyText(request.getText(), request.getStartIndex(), request.getEndIndex(), TextClassification.Options.from(sessionId, request), this.mCancellationSignal, new Callback<TextClassification>() { // from class: android.service.textclassifier.TextClassifierService.1.2
                @Override // android.service.textclassifier.TextClassifierService.Callback
                public void onSuccess(TextClassification result) {
                    try {
                        callback.onSuccess(result);
                    } catch (RemoteException e) {
                        Slog.d(TextClassifierService.LOG_TAG, "Error calling callback");
                    }
                }

                @Override // android.service.textclassifier.TextClassifierService.Callback
                public void onFailure(CharSequence error) {
                    try {
                        callback.onFailure();
                    } catch (RemoteException e) {
                        Slog.d(TextClassifierService.LOG_TAG, "Error calling callback");
                    }
                }
            });
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onGenerateLinks(TextClassificationSessionId sessionId, TextLinks.Request request, final ITextLinksCallback callback) throws RemoteException {
            Preconditions.checkNotNull(request);
            Preconditions.checkNotNull(callback);
            TextClassifierService.this.onGenerateLinks(request.getText(), TextLinks.Options.from(sessionId, request), this.mCancellationSignal, new Callback<TextLinks>() { // from class: android.service.textclassifier.TextClassifierService.1.3
                @Override // android.service.textclassifier.TextClassifierService.Callback
                public void onSuccess(TextLinks result) {
                    try {
                        callback.onSuccess(result);
                    } catch (RemoteException e) {
                        Slog.d(TextClassifierService.LOG_TAG, "Error calling callback");
                    }
                }

                @Override // android.service.textclassifier.TextClassifierService.Callback
                public void onFailure(CharSequence error) {
                    try {
                        callback.onFailure();
                    } catch (RemoteException e) {
                        Slog.d(TextClassifierService.LOG_TAG, "Error calling callback");
                    }
                }
            });
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onSelectionEvent(TextClassificationSessionId sessionId, SelectionEvent event) throws RemoteException {
            Preconditions.checkNotNull(event);
            TextClassifierService.this.onSelectionEvent(sessionId, event);
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onCreateTextClassificationSession(TextClassificationContext context, TextClassificationSessionId sessionId) throws RemoteException {
            Preconditions.checkNotNull(context);
            Preconditions.checkNotNull(sessionId);
            TextClassifierService.this.onCreateTextClassificationSession(context, sessionId);
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onDestroyTextClassificationSession(TextClassificationSessionId sessionId) throws RemoteException {
            TextClassifierService.this.onDestroyTextClassificationSession(sessionId);
        }
    };

    @SystemApi
    /* loaded from: classes2.dex */
    public interface Callback<T> {
        void onFailure(CharSequence charSequence);

        void onSuccess(T t);
    }

    public abstract void onClassifyText(TextClassificationSessionId textClassificationSessionId, TextClassification.Request request, CancellationSignal cancellationSignal, Callback<TextClassification> callback);

    public abstract void onGenerateLinks(TextClassificationSessionId textClassificationSessionId, TextLinks.Request request, CancellationSignal cancellationSignal, Callback<TextLinks> callback);

    public abstract void onSuggestSelection(TextClassificationSessionId textClassificationSessionId, TextSelection.Request request, CancellationSignal cancellationSignal, Callback<TextSelection> callback);

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        if (SERVICE_INTERFACE.equals(intent.getAction())) {
            return this.mBinder;
        }
        return null;
    }

    public synchronized void onSuggestSelection(CharSequence text, int selectionStartIndex, int selectionEndIndex, TextSelection.Options options, CancellationSignal cancellationSignal, Callback<TextSelection> callback) {
        TextSelection.Request request;
        TextClassificationSessionId sessionId = options.getSessionId();
        if (options.getRequest() != null) {
            request = options.getRequest();
        } else {
            request = new TextSelection.Request.Builder(text, selectionStartIndex, selectionEndIndex).setDefaultLocales(options.getDefaultLocales()).build();
        }
        onSuggestSelection(sessionId, request, cancellationSignal, callback);
    }

    public synchronized void onClassifyText(CharSequence text, int startIndex, int endIndex, TextClassification.Options options, CancellationSignal cancellationSignal, Callback<TextClassification> callback) {
        TextClassification.Request request;
        TextClassificationSessionId sessionId = options.getSessionId();
        if (options.getRequest() != null) {
            request = options.getRequest();
        } else {
            request = new TextClassification.Request.Builder(text, startIndex, endIndex).setDefaultLocales(options.getDefaultLocales()).setReferenceTime(options.getReferenceTime()).build();
        }
        onClassifyText(sessionId, request, cancellationSignal, callback);
    }

    public synchronized void onGenerateLinks(CharSequence text, TextLinks.Options options, CancellationSignal cancellationSignal, Callback<TextLinks> callback) {
        TextLinks.Request request;
        TextClassificationSessionId sessionId = options.getSessionId();
        if (options.getRequest() != null) {
            request = options.getRequest();
        } else {
            request = new TextLinks.Request.Builder(text).setDefaultLocales(options.getDefaultLocales()).setEntityConfig(options.getEntityConfig()).build();
        }
        onGenerateLinks(sessionId, request, cancellationSignal, callback);
    }

    public void onSelectionEvent(TextClassificationSessionId sessionId, SelectionEvent event) {
    }

    public void onCreateTextClassificationSession(TextClassificationContext context, TextClassificationSessionId sessionId) {
    }

    public void onDestroyTextClassificationSession(TextClassificationSessionId sessionId) {
    }

    public final TextClassifier getLocalTextClassifier() {
        TextClassificationManager tcm = (TextClassificationManager) getSystemService(TextClassificationManager.class);
        if (tcm != null) {
            return tcm.getTextClassifier(0);
        }
        return TextClassifier.NO_OP;
    }

    public static synchronized ComponentName getServiceComponentName(Context context) {
        String packageName = context.getPackageManager().getSystemTextClassifierPackageName();
        if (TextUtils.isEmpty(packageName)) {
            Slog.d(LOG_TAG, "No configured system TextClassifierService");
            return null;
        }
        Intent intent = new Intent(SERVICE_INTERFACE).setPackage(packageName);
        ResolveInfo ri = context.getPackageManager().resolveService(intent, 1048576);
        if (ri == null || ri.serviceInfo == null) {
            Slog.w(LOG_TAG, String.format("Package or service not found in package %s", packageName));
            return null;
        }
        ServiceInfo si = ri.serviceInfo;
        String permission = si.permission;
        if (Manifest.permission.BIND_TEXTCLASSIFIER_SERVICE.equals(permission)) {
            return si.getComponentName();
        }
        Slog.w(LOG_TAG, String.format("Service %s should require %s permission. Found %s permission", si.getComponentName(), Manifest.permission.BIND_TEXTCLASSIFIER_SERVICE, si.permission));
        return null;
    }
}
