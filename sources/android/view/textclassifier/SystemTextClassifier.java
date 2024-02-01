package android.view.textclassifier;

import android.content.Context;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.textclassifier.ITextClassificationCallback;
import android.service.textclassifier.ITextClassifierService;
import android.service.textclassifier.ITextLinksCallback;
import android.service.textclassifier.ITextSelectionCallback;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: classes2.dex */
public final class SystemTextClassifier implements TextClassifier {
    private static final String LOG_TAG = "SystemTextClassifier";
    private final TextClassifier mFallback;
    private final ITextClassifierService mManagerService = ITextClassifierService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.TEXT_CLASSIFICATION_SERVICE));
    private final String mPackageName;
    private TextClassificationSessionId mSessionId;
    private final TextClassificationConstants mSettings;

    public synchronized SystemTextClassifier(Context context, TextClassificationConstants settings) throws ServiceManager.ServiceNotFoundException {
        this.mSettings = (TextClassificationConstants) Preconditions.checkNotNull(settings);
        this.mFallback = ((TextClassificationManager) context.getSystemService(TextClassificationManager.class)).getTextClassifier(0);
        this.mPackageName = (String) Preconditions.checkNotNull(context.getPackageName());
    }

    @Override // android.view.textclassifier.TextClassifier
    public TextSelection suggestSelection(TextSelection.Request request) {
        Preconditions.checkNotNull(request);
        TextClassifier.Utils.checkMainThread();
        try {
            TextSelectionCallback callback = new TextSelectionCallback();
            this.mManagerService.onSuggestSelection(this.mSessionId, request, callback);
            TextSelection selection = callback.mReceiver.get();
            if (selection != null) {
                return selection;
            }
        } catch (RemoteException | InterruptedException e) {
            Log.e(LOG_TAG, "Error suggesting selection for text. Using fallback.", e);
        }
        return this.mFallback.suggestSelection(request);
    }

    @Override // android.view.textclassifier.TextClassifier
    public TextClassification classifyText(TextClassification.Request request) {
        Preconditions.checkNotNull(request);
        TextClassifier.Utils.checkMainThread();
        try {
            TextClassificationCallback callback = new TextClassificationCallback();
            this.mManagerService.onClassifyText(this.mSessionId, request, callback);
            TextClassification classification = callback.mReceiver.get();
            if (classification != null) {
                return classification;
            }
        } catch (RemoteException | InterruptedException e) {
            Log.e(LOG_TAG, "Error classifying text. Using fallback.", e);
        }
        return this.mFallback.classifyText(request);
    }

    @Override // android.view.textclassifier.TextClassifier
    public TextLinks generateLinks(TextLinks.Request request) {
        Preconditions.checkNotNull(request);
        TextClassifier.Utils.checkMainThread();
        if (!this.mSettings.isSmartLinkifyEnabled() && request.isLegacyFallback()) {
            return TextClassifier.Utils.generateLegacyLinks(request);
        }
        try {
            request.setCallingPackageName(this.mPackageName);
            TextLinksCallback callback = new TextLinksCallback();
            this.mManagerService.onGenerateLinks(this.mSessionId, request, callback);
            TextLinks links = callback.mReceiver.get();
            if (links != null) {
                return links;
            }
        } catch (RemoteException | InterruptedException e) {
            Log.e(LOG_TAG, "Error generating links. Using fallback.", e);
        }
        return this.mFallback.generateLinks(request);
    }

    @Override // android.view.textclassifier.TextClassifier
    public void onSelectionEvent(SelectionEvent event) {
        Preconditions.checkNotNull(event);
        TextClassifier.Utils.checkMainThread();
        try {
            this.mManagerService.onSelectionEvent(this.mSessionId, event);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error reporting selection event.", e);
        }
    }

    @Override // android.view.textclassifier.TextClassifier
    public int getMaxGenerateLinksTextLength() {
        return this.mFallback.getMaxGenerateLinksTextLength();
    }

    @Override // android.view.textclassifier.TextClassifier
    public void destroy() {
        try {
            if (this.mSessionId != null) {
                this.mManagerService.onDestroyTextClassificationSession(this.mSessionId);
            }
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error destroying classification session.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void initializeRemoteSession(TextClassificationContext classificationContext, TextClassificationSessionId sessionId) {
        this.mSessionId = (TextClassificationSessionId) Preconditions.checkNotNull(sessionId);
        try {
            this.mManagerService.onCreateTextClassificationSession(classificationContext, this.mSessionId);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error starting a new classification session.", e);
        }
    }

    /* loaded from: classes2.dex */
    private static final class TextSelectionCallback extends ITextSelectionCallback.Stub {
        final ResponseReceiver<TextSelection> mReceiver;

        private synchronized TextSelectionCallback() {
            this.mReceiver = new ResponseReceiver<>();
        }

        @Override // android.service.textclassifier.ITextSelectionCallback
        public synchronized void onSuccess(TextSelection selection) {
            this.mReceiver.onSuccess(selection);
        }

        @Override // android.service.textclassifier.ITextSelectionCallback
        public synchronized void onFailure() {
            this.mReceiver.onFailure();
        }
    }

    /* loaded from: classes2.dex */
    private static final class TextClassificationCallback extends ITextClassificationCallback.Stub {
        final ResponseReceiver<TextClassification> mReceiver;

        private synchronized TextClassificationCallback() {
            this.mReceiver = new ResponseReceiver<>();
        }

        @Override // android.service.textclassifier.ITextClassificationCallback
        public synchronized void onSuccess(TextClassification classification) {
            this.mReceiver.onSuccess(classification);
        }

        @Override // android.service.textclassifier.ITextClassificationCallback
        public synchronized void onFailure() {
            this.mReceiver.onFailure();
        }
    }

    /* loaded from: classes2.dex */
    private static final class TextLinksCallback extends ITextLinksCallback.Stub {
        final ResponseReceiver<TextLinks> mReceiver;

        private synchronized TextLinksCallback() {
            this.mReceiver = new ResponseReceiver<>();
        }

        @Override // android.service.textclassifier.ITextLinksCallback
        public synchronized void onSuccess(TextLinks links) {
            this.mReceiver.onSuccess(links);
        }

        @Override // android.service.textclassifier.ITextLinksCallback
        public synchronized void onFailure() {
            this.mReceiver.onFailure();
        }
    }

    /* loaded from: classes2.dex */
    private static final class ResponseReceiver<T> {
        private final CountDownLatch mLatch;
        private T mResponse;

        private synchronized ResponseReceiver() {
            this.mLatch = new CountDownLatch(1);
        }

        public synchronized void onSuccess(T response) {
            this.mResponse = response;
            this.mLatch.countDown();
        }

        public synchronized void onFailure() {
            Log.e(SystemTextClassifier.LOG_TAG, "Request failed.", null);
            this.mLatch.countDown();
        }

        public synchronized T get() throws InterruptedException {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                this.mLatch.await(2L, TimeUnit.SECONDS);
            }
            return this.mResponse;
        }
    }
}
