package android.view.textclassifier;

import android.metrics.LogMaker;
import android.view.textclassifier.SelectionSessionLogger;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.Preconditions;

/* loaded from: classes3.dex */
public final class TextClassifierEventTronLogger {
    private static final String TAG = "TCEventTronLogger";
    private final MetricsLogger mMetricsLogger;

    public TextClassifierEventTronLogger() {
        this(new MetricsLogger());
    }

    @VisibleForTesting
    public TextClassifierEventTronLogger(MetricsLogger metricsLogger) {
        this.mMetricsLogger = (MetricsLogger) Preconditions.checkNotNull(metricsLogger);
    }

    public void writeEvent(TextClassifierEvent event) {
        Preconditions.checkNotNull(event);
        int category = getCategory(event);
        if (category == -1) {
            Log.w(TAG, "Unknown category: " + event.getEventCategory());
            return;
        }
        LogMaker log = new LogMaker(category).setSubtype(getLogType(event)).addTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SESSION_ID, event.getResultId()).addTaggedData(MetricsProto.MetricsEvent.FIELD_TEXTCLASSIFIER_MODEL, getModelName(event));
        if (event.getScores().length >= 1) {
            log.addTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SCORE, Float.valueOf(event.getScores()[0]));
        }
        String[] entityTypes = event.getEntityTypes();
        if (entityTypes.length >= 1) {
            log.addTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_FIRST_ENTITY_TYPE, entityTypes[0]);
        }
        if (entityTypes.length >= 2) {
            log.addTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SECOND_ENTITY_TYPE, entityTypes[1]);
        }
        if (entityTypes.length >= 3) {
            log.addTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_THIRD_ENTITY_TYPE, entityTypes[2]);
        }
        TextClassificationContext eventContext = event.getEventContext();
        if (eventContext != null) {
            log.addTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_WIDGET_TYPE, eventContext.getWidgetType());
            log.addTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_WIDGET_VERSION, eventContext.getWidgetVersion());
            log.setPackageName(eventContext.getPackageName());
        }
        this.mMetricsLogger.write(log);
        debugLog(log);
    }

    private static String getModelName(TextClassifierEvent event) {
        if (event.getModelName() != null) {
            return event.getModelName();
        }
        return SelectionSessionLogger.SignatureParser.getModelName(event.getResultId());
    }

    private static int getCategory(TextClassifierEvent event) {
        int eventCategory = event.getEventCategory();
        if (eventCategory != 3) {
            if (eventCategory == 4) {
                return 1614;
            }
            return -1;
        }
        return 1615;
    }

    private static int getLogType(TextClassifierEvent event) {
        int eventType = event.getEventType();
        if (eventType != 6) {
            if (eventType != 13) {
                if (eventType != 19) {
                    if (eventType == 20) {
                        return 1619;
                    }
                    return 0;
                }
                return 1618;
            }
            return MetricsProto.MetricsEvent.ACTION_TEXT_SELECTION_SMART_SHARE;
        }
        return 1616;
    }

    private String toCategoryName(int category) {
        if (category != 1614) {
            if (category == 1615) {
                return "conversation_actions";
            }
            return "unknown";
        }
        return "language_detection";
    }

    private String toEventName(int logType) {
        if (logType != 1113) {
            if (logType != 1616) {
                if (logType != 1618) {
                    if (logType == 1619) {
                        return "actions_generated";
                    }
                    return "unknown";
                }
                return "manual_reply";
            }
            return "actions_shown";
        }
        return "smart_share";
    }

    private void debugLog(LogMaker log) {
        if (!Log.ENABLE_FULL_LOGGING) {
            return;
        }
        String id = String.valueOf(log.getTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SESSION_ID));
        String categoryName = toCategoryName(log.getCategory());
        String eventName = toEventName(log.getSubtype());
        String widgetType = String.valueOf(log.getTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_WIDGET_TYPE));
        String widgetVersion = String.valueOf(log.getTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_WIDGET_VERSION));
        String model = String.valueOf(log.getTaggedData(MetricsProto.MetricsEvent.FIELD_TEXTCLASSIFIER_MODEL));
        String firstEntityType = String.valueOf(log.getTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_FIRST_ENTITY_TYPE));
        String secondEntityType = String.valueOf(log.getTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SECOND_ENTITY_TYPE));
        String thirdEntityType = String.valueOf(log.getTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_THIRD_ENTITY_TYPE));
        String score = String.valueOf(log.getTaggedData(MetricsProto.MetricsEvent.FIELD_TEXT_CLASSIFIER_SCORE));
        Log.v(TAG, "writeEvent: id=" + id + ", category=" + categoryName + ", eventName=" + eventName + ", widgetType=" + widgetType + ", widgetVersion=" + widgetVersion + ", model=" + model + ", firstEntityType=" + firstEntityType + ", secondEntityType=" + secondEntityType + ", thirdEntityType=" + thirdEntityType + ", score=" + score);
    }
}
