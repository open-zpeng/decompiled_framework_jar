package android.telecom.Logging;

import android.provider.SettingsStringUtil;
import android.telecom.Log;
import android.telecom.Logging.EventManager;
import android.telecom.Logging.SessionManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Pair;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.ToLongFunction;

/* loaded from: classes2.dex */
public class EventManager {
    @VisibleForTesting
    public static final int DEFAULT_EVENTS_TO_CACHE = 10;
    public static final String TAG = "Logging.Events";
    private SessionManager.ISessionIdQueryHandler mSessionIdHandler;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final Object mSync = new Object();
    private final Map<Loggable, EventRecord> mCallEventRecordMap = new HashMap();
    private LinkedBlockingQueue<EventRecord> mEventRecords = new LinkedBlockingQueue<>(10);
    private List<EventListener> mEventListeners = new ArrayList();
    private final Map<String, List<TimedEventPair>> requestResponsePairs = new HashMap();

    /* loaded from: classes2.dex */
    public interface EventListener {
        void eventRecordAdded(EventRecord eventRecord);
    }

    /* loaded from: classes2.dex */
    public interface Loggable {
        String getDescription();

        String getId();
    }

    /* loaded from: classes2.dex */
    public static class TimedEventPair {
        private static final long DEFAULT_TIMEOUT = 3000;
        String mName;
        String mRequest;
        String mResponse;
        long mTimeoutMillis;

        public TimedEventPair(String request, String response, String name) {
            this.mTimeoutMillis = DEFAULT_TIMEOUT;
            this.mRequest = request;
            this.mResponse = response;
            this.mName = name;
        }

        public TimedEventPair(String request, String response, String name, long timeoutMillis) {
            this.mTimeoutMillis = DEFAULT_TIMEOUT;
            this.mRequest = request;
            this.mResponse = response;
            this.mName = name;
            this.mTimeoutMillis = timeoutMillis;
        }
    }

    public void addRequestResponsePair(TimedEventPair p) {
        if (this.requestResponsePairs.containsKey(p.mRequest)) {
            this.requestResponsePairs.get(p.mRequest).add(p);
            return;
        }
        ArrayList<TimedEventPair> responses = new ArrayList<>();
        responses.add(p);
        this.requestResponsePairs.put(p.mRequest, responses);
    }

    /* loaded from: classes2.dex */
    public static class Event {
        public Object data;
        public String eventId;
        public String sessionId;
        public long time;
        public final String timestampString;

        public Event(String eventId, String sessionId, long time, Object data) {
            this.eventId = eventId;
            this.sessionId = sessionId;
            this.time = time;
            this.timestampString = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).format(EventManager.DATE_TIME_FORMATTER);
            this.data = data;
        }
    }

    /* loaded from: classes2.dex */
    public class EventRecord {
        private final List<Event> mEvents = Collections.synchronizedList(new LinkedList());
        private final Loggable mRecordEntry;

        /* loaded from: classes2.dex */
        public class EventTiming extends TimedEvent<String> {
            public String name;
            public long time;

            public EventTiming(String name, long time) {
                this.name = name;
                this.time = time;
            }

            @Override // android.telecom.Logging.TimedEvent
            public String getKey() {
                return this.name;
            }

            @Override // android.telecom.Logging.TimedEvent
            public long getTime() {
                return this.time;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public class PendingResponse {
            String name;
            String requestEventId;
            long requestEventTimeMillis;
            long timeoutMillis;

            public PendingResponse(String requestEventId, long requestEventTimeMillis, long timeoutMillis, String name) {
                this.requestEventId = requestEventId;
                this.requestEventTimeMillis = requestEventTimeMillis;
                this.timeoutMillis = timeoutMillis;
                this.name = name;
            }
        }

        public EventRecord(Loggable recordEntry) {
            this.mRecordEntry = recordEntry;
        }

        public Loggable getRecordEntry() {
            return this.mRecordEntry;
        }

        public void addEvent(String event, String sessionId, Object data) {
            this.mEvents.add(new Event(event, sessionId, System.currentTimeMillis(), data));
            Log.i("Event", "RecordEntry %s: %s, %s", this.mRecordEntry.getId(), event, data);
        }

        public List<Event> getEvents() {
            return new LinkedList(this.mEvents);
        }

        public List<EventTiming> extractEventTimings() {
            Iterator<Event> it;
            if (this.mEvents == null) {
                return Collections.emptyList();
            }
            LinkedList<EventTiming> result = new LinkedList<>();
            Map<String, PendingResponse> pendingResponses = new HashMap<>();
            synchronized (this.mEvents) {
                Iterator<Event> it2 = this.mEvents.iterator();
                while (it2.hasNext()) {
                    Event event = it2.next();
                    if (EventManager.this.requestResponsePairs.containsKey(event.eventId)) {
                        Iterator it3 = ((List) EventManager.this.requestResponsePairs.get(event.eventId)).iterator();
                        while (it3.hasNext()) {
                            TimedEventPair p = (TimedEventPair) it3.next();
                            pendingResponses.put(p.mResponse, new PendingResponse(event.eventId, event.time, p.mTimeoutMillis, p.mName));
                            it3 = it3;
                            it2 = it2;
                        }
                        it = it2;
                    } else {
                        it = it2;
                    }
                    PendingResponse pendingResponse = pendingResponses.remove(event.eventId);
                    if (pendingResponse != null) {
                        long elapsedTime = event.time - pendingResponse.requestEventTimeMillis;
                        if (elapsedTime < pendingResponse.timeoutMillis) {
                            result.add(new EventTiming(pendingResponse.name, elapsedTime));
                        }
                    }
                    it2 = it;
                }
            }
            return result;
        }

        public void dump(IndentingPrintWriter pw) {
            EventRecord record;
            pw.print(this.mRecordEntry.getDescription());
            pw.increaseIndent();
            for (Event event : getEvents()) {
                pw.print(event.timestampString);
                pw.print(" - ");
                pw.print(event.eventId);
                if (event.data != null) {
                    pw.print(" (");
                    Object data = event.data;
                    if ((data instanceof Loggable) && (record = (EventRecord) EventManager.this.mCallEventRecordMap.get(data)) != null) {
                        data = "RecordEntry " + record.mRecordEntry.getId();
                    }
                    pw.print(data);
                    pw.print(")");
                }
                if (!TextUtils.isEmpty(event.sessionId)) {
                    pw.print(SettingsStringUtil.DELIMITER);
                    pw.print(event.sessionId);
                }
                pw.println();
            }
            pw.println("Timings (average for this call, milliseconds):");
            pw.increaseIndent();
            Map<String, Double> avgEventTimings = EventTiming.averageTimings(extractEventTimings());
            List<String> eventNames = new ArrayList<>(avgEventTimings.keySet());
            Collections.sort(eventNames);
            for (String eventName : eventNames) {
                pw.printf("%s: %.2f\n", eventName, avgEventTimings.get(eventName));
            }
            pw.decreaseIndent();
            pw.decreaseIndent();
        }
    }

    public EventManager(SessionManager.ISessionIdQueryHandler l) {
        this.mSessionIdHandler = l;
    }

    public void event(Loggable recordEntry, String event, Object data) {
        String currentSessionID = this.mSessionIdHandler.getSessionId();
        if (recordEntry == null) {
            Log.i(TAG, "Non-call EVENT: %s, %s", event, data);
            return;
        }
        synchronized (this.mEventRecords) {
            if (!this.mCallEventRecordMap.containsKey(recordEntry)) {
                EventRecord newRecord = new EventRecord(recordEntry);
                addEventRecord(newRecord);
            }
            EventRecord record = this.mCallEventRecordMap.get(recordEntry);
            record.addEvent(event, currentSessionID, data);
        }
    }

    public void event(Loggable recordEntry, String event, String format, Object... args) {
        String msg;
        if (args != null) {
            try {
            } catch (IllegalFormatException ife) {
                Log.e(this, ife, "IllegalFormatException: formatString='%s' numArgs=%d", format, Integer.valueOf(args.length));
                msg = format + " (An error occurred while formatting the message.)";
            }
            if (args.length != 0) {
                msg = String.format(Locale.US, format, args);
                event(recordEntry, event, msg);
            }
        }
        msg = format;
        event(recordEntry, event, msg);
    }

    public void dumpEvents(IndentingPrintWriter pw) {
        pw.println("Historical Events:");
        pw.increaseIndent();
        Iterator<EventRecord> it = this.mEventRecords.iterator();
        while (it.hasNext()) {
            EventRecord eventRecord = it.next();
            eventRecord.dump(pw);
        }
        pw.decreaseIndent();
    }

    public void dumpEventsTimeline(IndentingPrintWriter pw) {
        pw.println("Historical Events (sorted by time):");
        List<Pair<Loggable, Event>> events = new ArrayList<>();
        Iterator<EventRecord> it = this.mEventRecords.iterator();
        while (it.hasNext()) {
            EventRecord er = it.next();
            for (Event ev : er.getEvents()) {
                events.add(new Pair<>(er.getRecordEntry(), ev));
            }
        }
        Comparator<Pair<Loggable, Event>> byEventTime = Comparator.comparingLong(new ToLongFunction() { // from class: android.telecom.Logging.-$$Lambda$EventManager$weOtitr8e1cZeiy1aDSqzNoKaY8
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                long j;
                j = ((EventManager.Event) ((Pair) obj).second).time;
                return j;
            }
        });
        events.sort(byEventTime);
        pw.increaseIndent();
        for (Pair<Loggable, Event> event : events) {
            pw.print(((Event) event.second).timestampString);
            pw.print(SmsManager.REGEX_PREFIX_DELIMITER);
            pw.print(((Loggable) event.first).getId());
            pw.print(SmsManager.REGEX_PREFIX_DELIMITER);
            pw.print(((Event) event.second).eventId);
            pw.print(SmsManager.REGEX_PREFIX_DELIMITER);
            pw.println(((Event) event.second).data);
        }
        pw.decreaseIndent();
    }

    public void changeEventCacheSize(int newSize) {
        LinkedBlockingQueue<EventRecord> oldEventLog = this.mEventRecords;
        this.mEventRecords = new LinkedBlockingQueue<>(newSize);
        this.mCallEventRecordMap.clear();
        oldEventLog.forEach(new Consumer() { // from class: android.telecom.Logging.-$$Lambda$EventManager$uddp6XAJ90VBwdTiuzBdSYelntQ
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                EventManager.this.lambda$changeEventCacheSize$1$EventManager((EventManager.EventRecord) obj);
            }
        });
    }

    public /* synthetic */ void lambda$changeEventCacheSize$1$EventManager(EventRecord newRecord) {
        EventRecord record;
        Loggable recordEntry = newRecord.getRecordEntry();
        if (this.mEventRecords.remainingCapacity() == 0 && (record = this.mEventRecords.poll()) != null) {
            this.mCallEventRecordMap.remove(record.getRecordEntry());
        }
        this.mEventRecords.add(newRecord);
        this.mCallEventRecordMap.put(recordEntry, newRecord);
    }

    public void registerEventListener(EventListener e) {
        if (e != null) {
            synchronized (mSync) {
                this.mEventListeners.add(e);
            }
        }
    }

    @VisibleForTesting
    public LinkedBlockingQueue<EventRecord> getEventRecords() {
        return this.mEventRecords;
    }

    @VisibleForTesting
    public Map<Loggable, EventRecord> getCallEventRecordMap() {
        return this.mCallEventRecordMap;
    }

    private void addEventRecord(EventRecord newRecord) {
        EventRecord record;
        Loggable recordEntry = newRecord.getRecordEntry();
        if (this.mEventRecords.remainingCapacity() == 0 && (record = this.mEventRecords.poll()) != null) {
            this.mCallEventRecordMap.remove(record.getRecordEntry());
        }
        this.mEventRecords.add(newRecord);
        this.mCallEventRecordMap.put(recordEntry, newRecord);
        synchronized (mSync) {
            for (EventListener l : this.mEventListeners) {
                l.eventRecordAdded(newRecord);
            }
        }
    }
}
