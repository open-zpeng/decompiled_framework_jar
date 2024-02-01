package android.view.textclassifier;

import android.view.textclassifier.SelectionSessionLogger;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
import com.android.internal.util.Preconditions;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class TextClassificationSession implements TextClassifier {
    static final boolean DEBUG_LOG_ENABLED = true;
    private static final String LOG_TAG = "TextClassificationSession";
    private final TextClassificationContext mClassificationContext;
    private final TextClassifier mDelegate;
    private boolean mDestroyed;
    private final SelectionEventHelper mEventHelper;
    private final TextClassificationSessionId mSessionId = new TextClassificationSessionId();

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextClassificationSession(TextClassificationContext context, TextClassifier delegate) {
        this.mClassificationContext = (TextClassificationContext) Preconditions.checkNotNull(context);
        this.mDelegate = (TextClassifier) Preconditions.checkNotNull(delegate);
        this.mEventHelper = new SelectionEventHelper(this.mSessionId, this.mClassificationContext);
        initializeRemoteSession();
    }

    @Override // android.view.textclassifier.TextClassifier
    public TextSelection suggestSelection(TextSelection.Request request) {
        checkDestroyed();
        return this.mDelegate.suggestSelection(request);
    }

    private void initializeRemoteSession() {
        if (this.mDelegate instanceof SystemTextClassifier) {
            ((SystemTextClassifier) this.mDelegate).initializeRemoteSession(this.mClassificationContext, this.mSessionId);
        }
    }

    @Override // android.view.textclassifier.TextClassifier
    public TextClassification classifyText(TextClassification.Request request) {
        checkDestroyed();
        return this.mDelegate.classifyText(request);
    }

    @Override // android.view.textclassifier.TextClassifier
    public TextLinks generateLinks(TextLinks.Request request) {
        checkDestroyed();
        return this.mDelegate.generateLinks(request);
    }

    @Override // android.view.textclassifier.TextClassifier
    public void onSelectionEvent(SelectionEvent event) {
        checkDestroyed();
        Preconditions.checkNotNull(event);
        if (this.mEventHelper.sanitizeEvent(event)) {
            this.mDelegate.onSelectionEvent(event);
        }
    }

    @Override // android.view.textclassifier.TextClassifier
    public void destroy() {
        this.mEventHelper.endSession();
        this.mDelegate.destroy();
        this.mDestroyed = true;
    }

    @Override // android.view.textclassifier.TextClassifier
    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    private void checkDestroyed() {
        if (this.mDestroyed) {
            throw new IllegalStateException("This TextClassification session has been destroyed");
        }
    }

    /* loaded from: classes2.dex */
    private static final class SelectionEventHelper {
        private final TextClassificationContext mContext;
        private int mInvocationMethod = 0;
        private SelectionEvent mPrevEvent;
        private final TextClassificationSessionId mSessionId;
        private SelectionEvent mSmartEvent;
        private SelectionEvent mStartEvent;

        SelectionEventHelper(TextClassificationSessionId sessionId, TextClassificationContext context) {
            this.mSessionId = (TextClassificationSessionId) Preconditions.checkNotNull(sessionId);
            this.mContext = (TextClassificationContext) Preconditions.checkNotNull(context);
        }

        boolean sanitizeEvent(SelectionEvent event) {
            updateInvocationMethod(event);
            modifyAutoSelectionEventType(event);
            if (event.getEventType() != 1 && this.mStartEvent == null) {
                Log.d(TextClassificationSession.LOG_TAG, "Selection session not yet started. Ignoring event");
                return false;
            }
            long now = System.currentTimeMillis();
            switch (event.getEventType()) {
                case 1:
                    Preconditions.checkArgument(event.getAbsoluteEnd() == event.getAbsoluteStart() + 1);
                    event.setSessionId(this.mSessionId);
                    this.mStartEvent = event;
                    break;
                case 2:
                case 5:
                    if (this.mPrevEvent != null && this.mPrevEvent.getAbsoluteStart() == event.getAbsoluteStart() && this.mPrevEvent.getAbsoluteEnd() == event.getAbsoluteEnd()) {
                        return false;
                    }
                    break;
                case 3:
                case 4:
                    this.mSmartEvent = event;
                    break;
            }
            event.setEventTime(now);
            if (this.mStartEvent != null) {
                event.setSessionId(this.mStartEvent.getSessionId()).setDurationSinceSessionStart(now - this.mStartEvent.getEventTime()).setStart(event.getAbsoluteStart() - this.mStartEvent.getAbsoluteStart()).setEnd(event.getAbsoluteEnd() - this.mStartEvent.getAbsoluteStart());
            }
            if (this.mSmartEvent != null) {
                event.setResultId(this.mSmartEvent.getResultId()).setSmartStart(this.mSmartEvent.getAbsoluteStart() - this.mStartEvent.getAbsoluteStart()).setSmartEnd(this.mSmartEvent.getAbsoluteEnd() - this.mStartEvent.getAbsoluteStart());
            }
            if (this.mPrevEvent != null) {
                event.setDurationSincePreviousEvent(now - this.mPrevEvent.getEventTime()).setEventIndex(this.mPrevEvent.getEventIndex() + 1);
            }
            this.mPrevEvent = event;
            return true;
        }

        void endSession() {
            this.mPrevEvent = null;
            this.mSmartEvent = null;
            this.mStartEvent = null;
        }

        private void updateInvocationMethod(SelectionEvent event) {
            event.setTextClassificationSessionContext(this.mContext);
            if (event.getInvocationMethod() == 0) {
                event.setInvocationMethod(this.mInvocationMethod);
            } else {
                this.mInvocationMethod = event.getInvocationMethod();
            }
        }

        private void modifyAutoSelectionEventType(SelectionEvent event) {
            switch (event.getEventType()) {
                case 3:
                case 4:
                case 5:
                    if (isPlatformLocalTextClassifierSmartSelection(event.getResultId())) {
                        if (event.getAbsoluteEnd() - event.getAbsoluteStart() > 1) {
                            event.setEventType(4);
                            return;
                        } else {
                            event.setEventType(3);
                            return;
                        }
                    }
                    event.setEventType(5);
                    return;
                default:
                    return;
            }
        }

        private static boolean isPlatformLocalTextClassifierSmartSelection(String signature) {
            return TextClassifier.DEFAULT_LOG_TAG.equals(SelectionSessionLogger.SignatureParser.getClassifierId(signature));
        }
    }
}
