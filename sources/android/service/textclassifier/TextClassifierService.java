package android.service.textclassifier;

import android.Manifest;
import android.annotation.SystemApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.os.RemoteException;
import android.service.textclassifier.ITextClassifierService;
import android.service.textclassifier.TextClassifierService;
import android.text.TextUtils;
import android.util.Slog;
import android.view.textclassifier.ConversationActions;
import android.view.textclassifier.SelectionEvent;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassificationContext;
import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassificationSessionId;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextClassifierEvent;
import android.view.textclassifier.TextLanguage;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
import com.android.internal.util.Preconditions;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SystemApi
/* loaded from: classes2.dex */
public abstract class TextClassifierService extends Service {
    private static final String KEY_RESULT = "key_result";
    private static final String LOG_TAG = "TextClassifierService";
    public static final String SERVICE_INTERFACE = "android.service.textclassifier.TextClassifierService";
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper(), null, true);
    private final ExecutorService mSingleThreadExecutor = Executors.newSingleThreadExecutor();
    private final ITextClassifierService.Stub mBinder = new AnonymousClass1();

    /* loaded from: classes2.dex */
    public interface Callback<T> {
        void onFailure(CharSequence charSequence);

        void onSuccess(T t);
    }

    public abstract void onClassifyText(TextClassificationSessionId textClassificationSessionId, TextClassification.Request request, CancellationSignal cancellationSignal, Callback<TextClassification> callback);

    public abstract void onGenerateLinks(TextClassificationSessionId textClassificationSessionId, TextLinks.Request request, CancellationSignal cancellationSignal, Callback<TextLinks> callback);

    public abstract void onSuggestSelection(TextClassificationSessionId textClassificationSessionId, TextSelection.Request request, CancellationSignal cancellationSignal, Callback<TextSelection> callback);

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.service.textclassifier.TextClassifierService$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends ITextClassifierService.Stub {
        private final CancellationSignal mCancellationSignal = new CancellationSignal();

        AnonymousClass1() {
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onSuggestSelection(final TextClassificationSessionId sessionId, final TextSelection.Request request, final ITextClassifierCallback callback) {
            Preconditions.checkNotNull(request);
            Preconditions.checkNotNull(callback);
            TextClassifierService.this.mMainThreadHandler.post(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$1$mKOXH9oGuUFyRz-Oo15GnAPhABs
                @Override // java.lang.Runnable
                public final void run() {
                    TextClassifierService.AnonymousClass1.this.lambda$onSuggestSelection$0$TextClassifierService$1(sessionId, request, callback);
                }
            });
        }

        public /* synthetic */ void lambda$onSuggestSelection$0$TextClassifierService$1(TextClassificationSessionId sessionId, TextSelection.Request request, ITextClassifierCallback callback) {
            TextClassifierService.this.onSuggestSelection(sessionId, request, this.mCancellationSignal, new ProxyCallback(callback, null));
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onClassifyText(final TextClassificationSessionId sessionId, final TextClassification.Request request, final ITextClassifierCallback callback) {
            Preconditions.checkNotNull(request);
            Preconditions.checkNotNull(callback);
            TextClassifierService.this.mMainThreadHandler.post(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$1$LziW7ahHkWlZlAFekrEQR96QofM
                @Override // java.lang.Runnable
                public final void run() {
                    TextClassifierService.AnonymousClass1.this.lambda$onClassifyText$1$TextClassifierService$1(sessionId, request, callback);
                }
            });
        }

        public /* synthetic */ void lambda$onClassifyText$1$TextClassifierService$1(TextClassificationSessionId sessionId, TextClassification.Request request, ITextClassifierCallback callback) {
            TextClassifierService.this.onClassifyText(sessionId, request, this.mCancellationSignal, new ProxyCallback(callback, null));
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onGenerateLinks(final TextClassificationSessionId sessionId, final TextLinks.Request request, final ITextClassifierCallback callback) {
            Preconditions.checkNotNull(request);
            Preconditions.checkNotNull(callback);
            TextClassifierService.this.mMainThreadHandler.post(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$1$suS99xMAl9SLES4WhRmaub16wIc
                @Override // java.lang.Runnable
                public final void run() {
                    TextClassifierService.AnonymousClass1.this.lambda$onGenerateLinks$2$TextClassifierService$1(sessionId, request, callback);
                }
            });
        }

        public /* synthetic */ void lambda$onGenerateLinks$2$TextClassifierService$1(TextClassificationSessionId sessionId, TextLinks.Request request, ITextClassifierCallback callback) {
            TextClassifierService.this.onGenerateLinks(sessionId, request, this.mCancellationSignal, new ProxyCallback(callback, null));
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onSelectionEvent(final TextClassificationSessionId sessionId, final SelectionEvent event) {
            Preconditions.checkNotNull(event);
            TextClassifierService.this.mMainThreadHandler.post(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$1$-Nsl56ysLPoVPJ4Gu0VUwYCh4wE
                @Override // java.lang.Runnable
                public final void run() {
                    TextClassifierService.AnonymousClass1.this.lambda$onSelectionEvent$3$TextClassifierService$1(sessionId, event);
                }
            });
        }

        public /* synthetic */ void lambda$onSelectionEvent$3$TextClassifierService$1(TextClassificationSessionId sessionId, SelectionEvent event) {
            TextClassifierService.this.onSelectionEvent(sessionId, event);
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onTextClassifierEvent(final TextClassificationSessionId sessionId, final TextClassifierEvent event) {
            Preconditions.checkNotNull(event);
            TextClassifierService.this.mMainThreadHandler.post(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$1$bqy_LY0V0g3pGHWd_N7ARYwQWLY
                @Override // java.lang.Runnable
                public final void run() {
                    TextClassifierService.AnonymousClass1.this.lambda$onTextClassifierEvent$4$TextClassifierService$1(sessionId, event);
                }
            });
        }

        public /* synthetic */ void lambda$onTextClassifierEvent$4$TextClassifierService$1(TextClassificationSessionId sessionId, TextClassifierEvent event) {
            TextClassifierService.this.onTextClassifierEvent(sessionId, event);
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onDetectLanguage(final TextClassificationSessionId sessionId, final TextLanguage.Request request, final ITextClassifierCallback callback) {
            Preconditions.checkNotNull(request);
            Preconditions.checkNotNull(callback);
            TextClassifierService.this.mMainThreadHandler.post(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$1$lcpBFMoy_hRkYQ42cWViBMbNnMk
                @Override // java.lang.Runnable
                public final void run() {
                    TextClassifierService.AnonymousClass1.this.lambda$onDetectLanguage$5$TextClassifierService$1(sessionId, request, callback);
                }
            });
        }

        public /* synthetic */ void lambda$onDetectLanguage$5$TextClassifierService$1(TextClassificationSessionId sessionId, TextLanguage.Request request, ITextClassifierCallback callback) {
            TextClassifierService.this.onDetectLanguage(sessionId, request, this.mCancellationSignal, new ProxyCallback(callback, null));
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onSuggestConversationActions(final TextClassificationSessionId sessionId, final ConversationActions.Request request, final ITextClassifierCallback callback) {
            Preconditions.checkNotNull(request);
            Preconditions.checkNotNull(callback);
            TextClassifierService.this.mMainThreadHandler.post(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$1$Xkudza2Bh6W4NodH1DO-FiRgfuM
                @Override // java.lang.Runnable
                public final void run() {
                    TextClassifierService.AnonymousClass1.this.lambda$onSuggestConversationActions$6$TextClassifierService$1(sessionId, request, callback);
                }
            });
        }

        public /* synthetic */ void lambda$onSuggestConversationActions$6$TextClassifierService$1(TextClassificationSessionId sessionId, ConversationActions.Request request, ITextClassifierCallback callback) {
            TextClassifierService.this.onSuggestConversationActions(sessionId, request, this.mCancellationSignal, new ProxyCallback(callback, null));
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onCreateTextClassificationSession(final TextClassificationContext context, final TextClassificationSessionId sessionId) {
            Preconditions.checkNotNull(context);
            Preconditions.checkNotNull(sessionId);
            TextClassifierService.this.mMainThreadHandler.post(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$1$oecuM3n2XJWuEPg_O0hSZtoF0ls
                @Override // java.lang.Runnable
                public final void run() {
                    TextClassifierService.AnonymousClass1.this.lambda$onCreateTextClassificationSession$7$TextClassifierService$1(context, sessionId);
                }
            });
        }

        public /* synthetic */ void lambda$onCreateTextClassificationSession$7$TextClassifierService$1(TextClassificationContext context, TextClassificationSessionId sessionId) {
            TextClassifierService.this.onCreateTextClassificationSession(context, sessionId);
        }

        @Override // android.service.textclassifier.ITextClassifierService
        public void onDestroyTextClassificationSession(final TextClassificationSessionId sessionId) {
            TextClassifierService.this.mMainThreadHandler.post(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$1$fhIvecFpMXNthJWnvX-RvpNrPFA
                @Override // java.lang.Runnable
                public final void run() {
                    TextClassifierService.AnonymousClass1.this.lambda$onDestroyTextClassificationSession$8$TextClassifierService$1(sessionId);
                }
            });
        }

        public /* synthetic */ void lambda$onDestroyTextClassificationSession$8$TextClassifierService$1(TextClassificationSessionId sessionId) {
            TextClassifierService.this.onDestroyTextClassificationSession(sessionId);
        }
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        if (SERVICE_INTERFACE.equals(intent.getAction())) {
            return this.mBinder;
        }
        return null;
    }

    public void onDetectLanguage(TextClassificationSessionId sessionId, final TextLanguage.Request request, CancellationSignal cancellationSignal, final Callback<TextLanguage> callback) {
        this.mSingleThreadExecutor.submit(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$9kfVuo6FJ1uQiU277-n9JgliEEc
            @Override // java.lang.Runnable
            public final void run() {
                TextClassifierService.this.lambda$onDetectLanguage$0$TextClassifierService(callback, request);
            }
        });
    }

    public /* synthetic */ void lambda$onDetectLanguage$0$TextClassifierService(Callback callback, TextLanguage.Request request) {
        callback.onSuccess(getLocalTextClassifier().detectLanguage(request));
    }

    public void onSuggestConversationActions(TextClassificationSessionId sessionId, final ConversationActions.Request request, CancellationSignal cancellationSignal, final Callback<ConversationActions> callback) {
        this.mSingleThreadExecutor.submit(new Runnable() { // from class: android.service.textclassifier.-$$Lambda$TextClassifierService$OMrgO9sL3mlBJfpfxbmg7ieGoWk
            @Override // java.lang.Runnable
            public final void run() {
                TextClassifierService.this.lambda$onSuggestConversationActions$1$TextClassifierService(callback, request);
            }
        });
    }

    public /* synthetic */ void lambda$onSuggestConversationActions$1$TextClassifierService(Callback callback, ConversationActions.Request request) {
        callback.onSuccess(getLocalTextClassifier().suggestConversationActions(request));
    }

    @Deprecated
    public void onSelectionEvent(TextClassificationSessionId sessionId, SelectionEvent event) {
    }

    public void onTextClassifierEvent(TextClassificationSessionId sessionId, TextClassifierEvent event) {
    }

    public void onCreateTextClassificationSession(TextClassificationContext context, TextClassificationSessionId sessionId) {
    }

    public void onDestroyTextClassificationSession(TextClassificationSessionId sessionId) {
    }

    @Deprecated
    public final TextClassifier getLocalTextClassifier() {
        return getDefaultTextClassifierImplementation(this);
    }

    public static TextClassifier getDefaultTextClassifierImplementation(Context context) {
        TextClassificationManager tcm = (TextClassificationManager) context.getSystemService(TextClassificationManager.class);
        if (tcm != null) {
            return tcm.getTextClassifier(0);
        }
        return TextClassifier.NO_OP;
    }

    public static <T extends Parcelable> T getResponse(Bundle bundle) {
        return (T) bundle.getParcelable(KEY_RESULT);
    }

    public static ComponentName getServiceComponentName(Context context) {
        String packageName = context.getPackageManager().getSystemTextClassifierPackageName();
        if (TextUtils.isEmpty(packageName)) {
            Slog.d(LOG_TAG, "No configured system TextClassifierService");
            return null;
        }
        Intent intent = new Intent(SERVICE_INTERFACE).setPackage(packageName);
        ResolveInfo ri = context.getPackageManager().resolveService(intent, 1048576);
        if (ri == null || ri.serviceInfo == null) {
            Slog.w(LOG_TAG, String.format("Package or service not found in package %s for user %d", packageName, Integer.valueOf(context.getUserId())));
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

    /* loaded from: classes2.dex */
    private static final class ProxyCallback<T extends Parcelable> implements Callback<T> {
        private ITextClassifierCallback mTextClassifierCallback;

        /* synthetic */ ProxyCallback(ITextClassifierCallback x0, AnonymousClass1 x1) {
            this(x0);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.service.textclassifier.TextClassifierService.Callback
        public /* bridge */ /* synthetic */ void onSuccess(Object obj) {
            onSuccess((ProxyCallback<T>) ((Parcelable) obj));
        }

        private ProxyCallback(ITextClassifierCallback textClassifierCallback) {
            this.mTextClassifierCallback = (ITextClassifierCallback) Preconditions.checkNotNull(textClassifierCallback);
        }

        public void onSuccess(T result) {
            try {
                Bundle bundle = new Bundle(1);
                bundle.putParcelable(TextClassifierService.KEY_RESULT, result);
                this.mTextClassifierCallback.onSuccess(bundle);
            } catch (RemoteException e) {
                Slog.d(TextClassifierService.LOG_TAG, "Error calling callback");
            }
        }

        @Override // android.service.textclassifier.TextClassifierService.Callback
        public void onFailure(CharSequence error) {
            try {
                this.mTextClassifierCallback.onFailure();
            } catch (RemoteException e) {
                Slog.d(TextClassifierService.LOG_TAG, "Error calling callback");
            }
        }
    }
}
