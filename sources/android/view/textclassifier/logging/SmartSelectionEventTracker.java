package android.view.textclassifier.logging;

import android.annotation.UnsupportedAppUsage;
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

/* loaded from: classes3.dex */
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
    /* loaded from: classes3.dex */
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

    @UnsupportedAppUsage
    public SmartSelectionEventTracker(Context context, int widgetType) {
        this.mMetricsLogger = new MetricsLogger();
        this.mSmartIndices = new int[2];
        this.mPrevIndices = new int[2];
        this.mWidgetType = widgetType;
        this.mWidgetVersion = null;
        this.mContext = (Context) Preconditions.checkNotNull(context);
    }

    public SmartSelectionEventTracker(Context context, int widgetType, String widgetVersion) {
        this.mMetricsLogger = new MetricsLogger();
        this.mSmartIndices = new int[2];
        this.mPrevIndices = new int[2];
        this.mWidgetType = widgetType;
        this.mWidgetVersion = widgetVersion;
        this.mContext = (Context) Preconditions.checkNotNull(context);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x002b, code lost:
        if (r0 != 5) goto L18;
     */
    @android.annotation.UnsupportedAppUsage
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void logEvent(android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent r7) {
        /*
            r6 = this;
            com.android.internal.util.Preconditions.checkNotNull(r7)
            int r0 = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.access$000(r7)
            r1 = 1
            if (r0 == r1) goto L16
            java.lang.String r0 = r6.mSessionId
            if (r0 != 0) goto L16
            java.lang.String r0 = "SmartSelectEventTracker"
            java.lang.String r1 = "Selection session not yet started. Ignoring event"
            android.util.Log.d(r0, r1)
            return
        L16:
            long r2 = java.lang.System.currentTimeMillis()
            int r0 = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.access$000(r7)
            r4 = 0
            if (r0 == r1) goto L5c
            r5 = 2
            if (r0 == r5) goto L47
            r5 = 3
            if (r0 == r5) goto L2e
            r5 = 4
            if (r0 == r5) goto L2e
            r5 = 5
            if (r0 == r5) goto L47
            goto L7b
        L2e:
            r6.mSmartSelectionTriggered = r1
            java.lang.String r0 = r6.getModelName(r7)
            r6.mModelName = r0
            int[] r0 = r6.mSmartIndices
            int r5 = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.access$200(r7)
            r0[r4] = r5
            int[] r0 = r6.mSmartIndices
            int r4 = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.access$100(r7)
            r0[r1] = r4
            goto L7b
        L47:
            int[] r0 = r6.mPrevIndices
            r0 = r0[r4]
            int r4 = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.access$200(r7)
            if (r0 != r4) goto L7b
            int[] r0 = r6.mPrevIndices
            r0 = r0[r1]
            int r1 = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.access$100(r7)
            if (r0 != r1) goto L7b
            return
        L5c:
            java.lang.String r0 = r6.startNewSession()
            r6.mSessionId = r0
            int r0 = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.access$100(r7)
            int r5 = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.access$200(r7)
            int r5 = r5 + r1
            if (r0 != r5) goto L6e
            goto L6f
        L6e:
            r1 = r4
        L6f:
            com.android.internal.util.Preconditions.checkArgument(r1)
            int r0 = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.access$200(r7)
            r6.mOrigStart = r0
            r6.mSessionStartTime = r2
        L7b:
            r6.writeEvent(r7, r2)
            boolean r0 = android.view.textclassifier.logging.SmartSelectionEventTracker.SelectionEvent.access$300(r7)
            if (r0 == 0) goto L87
            r6.endSession()
        L87:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.textclassifier.logging.SmartSelectionEventTracker.logEvent(android.view.textclassifier.logging.SmartSelectionEventTracker$SelectionEvent):void");
    }

    private void writeEvent(SelectionEvent event, long now) {
        long j = this.mLastEventTime;
        long prevEventDelta = j != 0 ? now - j : 0L;
        LogMaker log = new LogMaker(1100).setType(getLogType(event)).setSubtype(1).setPackageName(this.mContext.getPackageName()).addTaggedData(1117, Long.valueOf(now - this.mSessionStartTime)).addTaggedData(1118, Long.valueOf(prevEventDelta)).addTaggedData(1120, Integer.valueOf(this.mIndex)).addTaggedData(1255, getWidgetTypeName()).addTaggedData(1262, this.mWidgetVersion).addTaggedData(1256, this.mModelName).addTaggedData(1254, event.mEntityType).addTaggedData(1252, Integer.valueOf(getSmartRangeDelta(this.mSmartIndices[0]))).addTaggedData(1253, Integer.valueOf(getSmartRangeDelta(this.mSmartIndices[1]))).addTaggedData(1250, Integer.valueOf(getRangeDelta(event.mStart))).addTaggedData(1251, Integer.valueOf(getRangeDelta(event.mEnd))).addTaggedData(1119, this.mSessionId);
        this.mMetricsLogger.write(log);
        debugLog(log);
        this.mLastEventTime = now;
        this.mPrevIndices[0] = event.mStart;
        this.mPrevIndices[1] = event.mEnd;
        this.mIndex++;
    }

    private String startNewSession() {
        endSession();
        this.mSessionId = createSessionId();
        return this.mSessionId;
    }

    private void endSession() {
        this.mOrigStart = 0;
        int[] iArr = this.mSmartIndices;
        iArr[1] = 0;
        iArr[0] = 0;
        int[] iArr2 = this.mPrevIndices;
        iArr2[1] = 0;
        iArr2[0] = 0;
        this.mIndex = 0;
        this.mSessionStartTime = 0L;
        this.mLastEventTime = 0L;
        this.mSmartSelectionTriggered = false;
        this.mModelName = getModelName(null);
        this.mSessionId = null;
    }

    private static int getLogType(SelectionEvent event) {
        int i = event.mEventType;
        if (i != 1) {
            if (i != 2) {
                if (i != 3) {
                    if (i != 4) {
                        if (i != 5) {
                            if (i != 200) {
                                if (i != 201) {
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
                                            return 0;
                                    }
                                }
                                return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_RESET;
                            }
                            return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SELECT_ALL;
                        }
                        return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_AUTO;
                    }
                    return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_MULTI;
                }
                return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SINGLE;
            }
            return 1102;
        }
        return 1101;
    }

    private static String getLogTypeString(int logType) {
        switch (logType) {
            case 1101:
                return "SELECTION_STARTED";
            case 1102:
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

    private int getRangeDelta(int offset) {
        return offset - this.mOrigStart;
    }

    private int getSmartRangeDelta(int offset) {
        if (this.mSmartSelectionTriggered) {
            return getRangeDelta(offset);
        }
        return 0;
    }

    private String getWidgetTypeName() {
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

    private String getModelName(SelectionEvent event) {
        return event == null ? "" : Objects.toString(event.mVersionTag, "");
    }

    private static String createSessionId() {
        return UUID.randomUUID().toString();
    }

    private static void debugLog(LogMaker log) {
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

    /* loaded from: classes3.dex */
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
        /* loaded from: classes3.dex */
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
        /* loaded from: classes3.dex */
        private @interface EventType {
            public static final int AUTO_SELECTION = 5;
            public static final int SELECTION_MODIFIED = 2;
            public static final int SELECTION_STARTED = 1;
            public static final int SMART_SELECTION_MULTI = 4;
            public static final int SMART_SELECTION_SINGLE = 3;
        }

        private SelectionEvent(int start, int end, int eventType, String entityType, String versionTag) {
            Preconditions.checkArgument(end >= start, "end cannot be less than start");
            this.mStart = start;
            this.mEnd = end;
            this.mEventType = eventType;
            this.mEntityType = (String) Preconditions.checkNotNull(entityType);
            this.mVersionTag = (String) Preconditions.checkNotNull(versionTag);
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionStarted(int start) {
            return new SelectionEvent(start, start + 1, 1, "", "");
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionModified(int start, int end) {
            return new SelectionEvent(start, end, 2, "", "");
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionModified(int start, int end, TextClassification classification) {
            String entityType;
            if (classification.getEntityCount() > 0) {
                entityType = classification.getEntity(0);
            } else {
                entityType = "";
            }
            String versionTag = getVersionInfo(classification.getId());
            return new SelectionEvent(start, end, 2, entityType, versionTag);
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionModified(int start, int end, TextSelection selection) {
            int eventType;
            String entityType;
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
            if (selection.getEntityCount() > 0) {
                entityType = selection.getEntity(0);
            } else {
                entityType = "";
            }
            String versionTag = getVersionInfo(selection.getId());
            return new SelectionEvent(start, end, eventType, entityType, versionTag);
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionAction(int start, int end, int actionType) {
            return new SelectionEvent(start, end, actionType, "", "");
        }

        @UnsupportedAppUsage
        public static SelectionEvent selectionAction(int start, int end, int actionType, TextClassification classification) {
            String entityType;
            if (classification.getEntityCount() > 0) {
                entityType = classification.getEntity(0);
            } else {
                entityType = "";
            }
            String versionTag = getVersionInfo(classification.getId());
            return new SelectionEvent(start, end, actionType, entityType, versionTag);
        }

        private static String getVersionInfo(String signature) {
            int start = signature.indexOf("|");
            int end = signature.indexOf("|", start);
            if (start >= 0 && end >= start) {
                return signature.substring(start, end);
            }
            return "";
        }

        private static String getSourceClassifier(String signature) {
            int end = signature.indexOf("|");
            if (end >= 0) {
                return signature.substring(0, end);
            }
            return "";
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isTerminal() {
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
