package android.view.textclassifier.logging;

import android.content.Context;
import android.metrics.LogMaker;
import android.util.Log;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextSelection;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import java.util.UUID;
/* loaded from: classes2.dex */
public final class SmartSelectionEventTracker {
    private static final String CUSTOM_EDITTEXT = "customedit";
    private static final String CUSTOM_TEXTVIEW = "customview";
    private static final String CUSTOM_UNSELECTABLE_TEXTVIEW = "nosel-customview";
    private static final boolean DEBUG_LOG_ENABLED = true;
    private static final String EDITTEXT = "edittext";
    private static final String EDIT_WEBVIEW = "edit-webview";
    private static final int ENTITY_TYPE = 1254;
    private static final int EVENT_END = 1251;
    private static final int EVENT_START = 1250;
    private static final int INDEX = 1120;
    private static final String LOG_TAG = "SmartSelectEventTracker";
    private static final int MODEL_NAME = 1256;
    private static final int PREV_EVENT_DELTA = 1118;
    private static final int SESSION_ID = 1119;
    private static final int SMART_END = 1253;
    private static final int SMART_START = 1252;
    private static final int START_EVENT_DELTA = 1117;
    private static final String TEXTVIEW = "textview";
    private static final String UNKNOWN = "unknown";
    private static final String UNSELECTABLE_TEXTVIEW = "nosel-textview";
    private static final String WEBVIEW = "webview";
    private static final int WIDGET_TYPE = 1255;
    private static final int WIDGET_VERSION = 1262;
    private static final String ZERO = "0";
    private final Context mContext;
    private int mIndex;
    private long mLastEventTime;
    private final MetricsLogger mMetricsLogger;
    private String mModelName;
    private int mOrigStart;
    private final int[] mPrevIndices;
    private String mSessionId;
    private long mSessionStartTime;
    private final int[] mSmartIndices;
    private boolean mSmartSelectionTriggered;
    private final int mWidgetType;
    private final String mWidgetVersion;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface WidgetType {
        public static final int CUSTOM_EDITTEXT = 7;
        public static final int CUSTOM_TEXTVIEW = 6;
        public static final int CUSTOM_UNSELECTABLE_TEXTVIEW = 8;
        public static final int EDITTEXT = 3;
        public static final int EDIT_WEBVIEW = 4;
        public static final int TEXTVIEW = 1;
        public static final int UNSELECTABLE_TEXTVIEW = 5;
        public static final int UNSPECIFIED = 0;
        public static final int WEBVIEW = 2;
    }

    private protected SmartSelectionEventTracker(Context context, int widgetType) {
        this.mMetricsLogger = new MetricsLogger();
        this.mSmartIndices = new int[2];
        this.mPrevIndices = new int[2];
        this.mWidgetType = widgetType;
        this.mWidgetVersion = null;
        this.mContext = (Context) Preconditions.checkNotNull(context);
    }

    public synchronized SmartSelectionEventTracker(Context context, int widgetType, String widgetVersion) {
        this.mMetricsLogger = new MetricsLogger();
        this.mSmartIndices = new int[2];
        this.mPrevIndices = new int[2];
        this.mWidgetType = widgetType;
        this.mWidgetVersion = widgetVersion;
        this.mContext = (Context) Preconditions.checkNotNull(context);
    }

    private protected void logEvent(SelectionEvent event) {
        Preconditions.checkNotNull(event);
        if (event.mEventType != 1 && this.mSessionId == null) {
            Log.d(LOG_TAG, "Selection session not yet started. Ignoring event");
            return;
        }
        long now = System.currentTimeMillis();
        switch (event.mEventType) {
            case 1:
                this.mSessionId = startNewSession();
                Preconditions.checkArgument(event.mEnd == event.mStart + 1);
                this.mOrigStart = event.mStart;
                this.mSessionStartTime = now;
                break;
            case 2:
            case 5:
                if (this.mPrevIndices[0] == event.mStart && this.mPrevIndices[1] == event.mEnd) {
                    return;
                }
                break;
            case 3:
            case 4:
                this.mSmartSelectionTriggered = true;
                this.mModelName = getModelName(event);
                this.mSmartIndices[0] = event.mStart;
                this.mSmartIndices[1] = event.mEnd;
                break;
        }
        writeEvent(event, now);
        if (event.isTerminal()) {
            endSession();
        }
    }

    private synchronized void writeEvent(SelectionEvent event, long now) {
        long prevEventDelta = this.mLastEventTime != 0 ? now - this.mLastEventTime : 0L;
        LogMaker log = new LogMaker(1100).setType(getLogType(event)).setSubtype(1).setPackageName(this.mContext.getPackageName()).addTaggedData(1117, Long.valueOf(now - this.mSessionStartTime)).addTaggedData(1118, Long.valueOf(prevEventDelta)).addTaggedData(1120, Integer.valueOf(this.mIndex)).addTaggedData(1255, getWidgetTypeName()).addTaggedData(1262, this.mWidgetVersion).addTaggedData(1256, this.mModelName).addTaggedData(1254, event.mEntityType).addTaggedData(1252, Integer.valueOf(getSmartRangeDelta(this.mSmartIndices[0]))).addTaggedData(1253, Integer.valueOf(getSmartRangeDelta(this.mSmartIndices[1]))).addTaggedData(1250, Integer.valueOf(getRangeDelta(event.mStart))).addTaggedData(1251, Integer.valueOf(getRangeDelta(event.mEnd))).addTaggedData(1119, this.mSessionId);
        this.mMetricsLogger.write(log);
        debugLog(log);
        this.mLastEventTime = now;
        this.mPrevIndices[0] = event.mStart;
        this.mPrevIndices[1] = event.mEnd;
        this.mIndex++;
    }

    private synchronized String startNewSession() {
        endSession();
        this.mSessionId = createSessionId();
        return this.mSessionId;
    }

    private synchronized void endSession() {
        this.mOrigStart = 0;
        int[] iArr = this.mSmartIndices;
        this.mSmartIndices[1] = 0;
        iArr[0] = 0;
        int[] iArr2 = this.mPrevIndices;
        this.mPrevIndices[1] = 0;
        iArr2[0] = 0;
        this.mIndex = 0;
        this.mSessionStartTime = 0L;
        this.mLastEventTime = 0L;
        this.mSmartSelectionTriggered = false;
        this.mModelName = getModelName(null);
        this.mSessionId = null;
    }

    private static synchronized int getLogType(SelectionEvent event) {
        int i = event.mEventType;
        switch (i) {
            case 1:
                return 1101;
            case 2:
                return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_MODIFY;
            case 3:
                return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SINGLE;
            case 4:
                return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_MULTI;
            case 5:
                return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_AUTO;
            default:
                switch (i) {
                    case 100:
                        return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_OVERTYPE;
                    case 101:
                        return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_COPY;
                    case 102:
                        return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_PASTE;
                    case 103:
                        return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_CUT;
                    case 104:
                        return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SHARE;
                    case 105:
                        return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SHARE;
                    case 106:
                        return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_DRAG;
                    case 107:
                        return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_ABANDON;
                    case 108:
                        return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_OTHER;
                    default:
                        switch (i) {
                            case 200:
                                return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SELECT_ALL;
                            case 201:
                                return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_RESET;
                            default:
                                return 0;
                        }
                }
        }
    }

    private static synchronized String getLogTypeString(int logType) {
        switch (logType) {
            case 1101:
                return "SELECTION_STARTED";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_MODIFY /* 1102 */:
                return "SELECTION_MODIFIED";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SELECT_ALL /* 1103 */:
                return "SELECT_ALL";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_RESET /* 1104 */:
                return "RESET";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SINGLE /* 1105 */:
                return "SMART_SELECTION_SINGLE";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_MULTI /* 1106 */:
                return "SMART_SELECTION_MULTI";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_AUTO /* 1107 */:
                return "AUTO_SELECTION";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_OVERTYPE /* 1108 */:
                return "OVERTYPE";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_COPY /* 1109 */:
                return "COPY";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_PASTE /* 1110 */:
                return "PASTE";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_CUT /* 1111 */:
                return "CUT";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SHARE /* 1112 */:
                return "SHARE";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SHARE /* 1113 */:
                return "SMART_SHARE";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_DRAG /* 1114 */:
                return "DRAG";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_ABANDON /* 1115 */:
                return "ABANDON";
            case MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_OTHER /* 1116 */:
                return "OTHER";
            default:
                return "unknown";
        }
    }

    private synchronized int getRangeDelta(int offset) {
        return offset - this.mOrigStart;
    }

    private synchronized int getSmartRangeDelta(int offset) {
        if (this.mSmartSelectionTriggered) {
            return getRangeDelta(offset);
        }
        return 0;
    }

    private synchronized String getWidgetTypeName() {
        switch (this.mWidgetType) {
            case 1:
                return "textview";
            case 2:
                return "webview";
            case 3:
                return "edittext";
            case 4:
                return "edit-webview";
            case 5:
                return "nosel-textview";
            case 6:
                return "customview";
            case 7:
                return "customedit";
            case 8:
                return "nosel-customview";
            default:
                return "unknown";
        }
    }

    private synchronized String getModelName(SelectionEvent event) {
        if (event == null) {
            return "";
        }
        return Objects.toString(event.mVersionTag, "");
    }

    private static synchronized String createSessionId() {
        return UUID.randomUUID().toString();
    }

    private static synchronized void debugLog(LogMaker log) {
        String widget;
        String widgetType = Objects.toString(log.getTaggedData(1255), "unknown");
        String widgetVersion = Objects.toString(log.getTaggedData(1262), "");
        if (widgetVersion.isEmpty()) {
            widget = widgetType;
        } else {
            widget = widgetType + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + widgetVersion;
        }
        int index = Integer.parseInt(Objects.toString(log.getTaggedData(1120), "0"));
        if (log.getType() == 1101) {
            String sessionId = Objects.toString(log.getTaggedData(1119), "");
            Log.d(LOG_TAG, String.format("New selection session: %s (%s)", widget, sessionId.substring(sessionId.lastIndexOf(NativeLibraryHelper.CLEAR_ABI_OVERRIDE) + 1)));
        }
        String model = Objects.toString(log.getTaggedData(1256), "unknown");
        String entity = Objects.toString(log.getTaggedData(1254), "unknown");
        String type = getLogTypeString(log.getType());
        int smartStart = Integer.parseInt(Objects.toString(log.getTaggedData(1252), "0"));
        int smartEnd = Integer.parseInt(Objects.toString(log.getTaggedData(1253), "0"));
        int eventStart = Integer.parseInt(Objects.toString(log.getTaggedData(1250), "0"));
        int eventEnd = Integer.parseInt(Objects.toString(log.getTaggedData(1251), "0"));
        Log.d(LOG_TAG, String.format("%2d: %s/%s, range=%d,%d - smart_range=%d,%d (%s/%s)", Integer.valueOf(index), type, entity, Integer.valueOf(eventStart), Integer.valueOf(eventEnd), Integer.valueOf(smartStart), Integer.valueOf(smartEnd), widget, model));
    }

    /* loaded from: classes2.dex */
    public static final class SelectionEvent {
        private static final String NO_VERSION_TAG = "";
        public static final int OUT_OF_BOUNDS = Integer.MAX_VALUE;
        public static final int OUT_OF_BOUNDS_NEGATIVE = Integer.MIN_VALUE;
        private final int mEnd;
        private final String mEntityType;
        private int mEventType;
        private final int mStart;
        private final String mVersionTag;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface ActionType {
            public static final int ABANDON = 107;
            public static final int COPY = 101;
            public static final int CUT = 103;
            public static final int DRAG = 106;
            public static final int OTHER = 108;
            public static final int OVERTYPE = 100;
            public static final int PASTE = 102;
            public static final int RESET = 201;
            public static final int SELECT_ALL = 200;
            public static final int SHARE = 104;
            public static final int SMART_SHARE = 105;
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        private @interface EventType {
            public static final int AUTO_SELECTION = 5;
            public static final int SELECTION_MODIFIED = 2;
            public static final int SELECTION_STARTED = 1;
            public static final int SMART_SELECTION_MULTI = 4;
            public static final int SMART_SELECTION_SINGLE = 3;
        }

        private synchronized SelectionEvent(int start, int end, int eventType, String entityType, String versionTag) {
            Preconditions.checkArgument(end >= start, "end cannot be less than start");
            this.mStart = start;
            this.mEnd = end;
            this.mEventType = eventType;
            this.mEntityType = (String) Preconditions.checkNotNull(entityType);
            this.mVersionTag = (String) Preconditions.checkNotNull(versionTag);
        }

        private protected static SelectionEvent selectionStarted(int start) {
            return new SelectionEvent(start, start + 1, 1, "", "");
        }

        private protected static SelectionEvent selectionModified(int start, int end) {
            return new SelectionEvent(start, end, 2, "", "");
        }

        private protected static SelectionEvent selectionModified(int start, int end, TextClassification classification) {
            String entityType = classification.getEntityCount() > 0 ? classification.getEntity(0) : "";
            String versionTag = getVersionInfo(classification.getId());
            return new SelectionEvent(start, end, 2, entityType, versionTag);
        }

        private protected static SelectionEvent selectionModified(int start, int end, TextSelection selection) {
            int eventType;
            boolean smartSelection = getSourceClassifier(selection.getId()).equals(TextClassifier.DEFAULT_LOG_TAG);
            if (smartSelection) {
                if (end - start > 1) {
                    eventType = 4;
                } else {
                    eventType = 3;
                }
            } else {
                eventType = 5;
            }
            int eventType2 = eventType;
            String entityType = selection.getEntityCount() > 0 ? selection.getEntity(0) : "";
            String versionTag = getVersionInfo(selection.getId());
            return new SelectionEvent(start, end, eventType2, entityType, versionTag);
        }

        private protected static SelectionEvent selectionAction(int start, int end, int actionType) {
            return new SelectionEvent(start, end, actionType, "", "");
        }

        private protected static SelectionEvent selectionAction(int start, int end, int actionType, TextClassification classification) {
            String entityType = classification.getEntityCount() > 0 ? classification.getEntity(0) : "";
            String versionTag = getVersionInfo(classification.getId());
            return new SelectionEvent(start, end, actionType, entityType, versionTag);
        }

        private static synchronized String getVersionInfo(String signature) {
            int start = signature.indexOf("|");
            int end = signature.indexOf("|", start);
            if (start >= 0 && end >= start) {
                return signature.substring(start, end);
            }
            return "";
        }

        private static synchronized String getSourceClassifier(String signature) {
            int end = signature.indexOf("|");
            if (end >= 0) {
                return signature.substring(0, end);
            }
            return "";
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean isTerminal() {
            switch (this.mEventType) {
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107:
                case 108:
                    return true;
                default:
                    return false;
            }
        }
    }
}
