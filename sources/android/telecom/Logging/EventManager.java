package android.telecom.Logging;

import android.provider.SettingsStringUtil;
import android.telecom.Log;
import android.telecom.Logging.EventManager;
import android.telecom.Logging.SessionManager;
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
    private protected static final int DEFAULT_EVENTS_TO_CACHE = 10;
    private protected static final String TAG = "Logging.Events";
    public protected SessionManager.ISessionIdQueryHandler mSessionIdHandler;
    private protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    public protected static final Object mSync = new Object();
    public protected final Map<Loggable, EventRecord> mCallEventRecordMap = new HashMap();
    public protected LinkedBlockingQueue<EventRecord> mEventRecords = new LinkedBlockingQueue<>(10);
    public protected List<EventListener> mEventListeners = new ArrayList();
    public protected final Map<String, List<TimedEventPair>> requestResponsePairs = new HashMap();

    /* loaded from: classes2.dex */
    public interface EventListener {
        private protected synchronized void eventRecordAdded(EventRecord eventRecord);
    }

    /* loaded from: classes2.dex */
    public interface Loggable {
        private protected synchronized String getDescription();

        private protected synchronized String getId();
    }

    /* loaded from: classes2.dex */
    public static class TimedEventPair {
        public protected static final long DEFAULT_TIMEOUT = 3000;
        public private protected String mName;
        public private protected String mRequest;
        public private protected String mResponse;
        public private protected long mTimeoutMillis;

        private protected synchronized TimedEventPair(String request, String response, String name) {
            this.mTimeoutMillis = DEFAULT_TIMEOUT;
            this.mRequest = request;
            this.mResponse = response;
            this.mName = name;
        }

        private protected synchronized TimedEventPair(String request, String response, String name, long timeoutMillis) {
            this.mTimeoutMillis = DEFAULT_TIMEOUT;
            this.mRequest = request;
            this.mResponse = response;
            this.mName = name;
            this.mTimeoutMillis = timeoutMillis;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void addRequestResponsePair(TimedEventPair p) {
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
        private protected Object data;
        private protected String eventId;
        private protected String sessionId;
        private protected long time;
        private protected final String timestampString;

        private protected synchronized Event(String eventId, String sessionId, long time, Object data) {
            this.eventId = eventId;
            this.sessionId = sessionId;
            this.time = time;
            this.timestampString = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).format(EventManager.DATE_TIME_FORMATTER);
            this.data = data;
        }
    }

    /* loaded from: classes2.dex */
    public class EventRecord {
        public protected final List<Event> mEvents = new LinkedList();
        public protected final Loggable mRecordEntry;

        /* loaded from: classes2.dex */
        public class EventTiming extends TimedEvent<String> {
            private protected String name;
            private protected long time;

            public EventTiming(String name, long time) {
                this.name = name;
                this.time = time;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public synchronized String getKey() {
                return this.name;
            }

            private protected synchronized long getTime() {
                return this.time;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public class PendingResponse {
            public private protected String name;
            public private protected String requestEventId;
            public private protected long requestEventTimeMillis;
            public private protected long timeoutMillis;

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

        private protected synchronized Loggable getRecordEntry() {
            return this.mRecordEntry;
        }

        private protected synchronized void addEvent(String event, String sessionId, Object data) {
            this.mEvents.add(new Event(event, sessionId, System.currentTimeMillis(), data));
            Log.i("Event", "RecordEntry %s: %s, %s", this.mRecordEntry.getId(), event, data);
        }

        private protected synchronized List<Event> getEvents() {
            return this.mEvents;
        }

        private protected synchronized List<EventTiming> extractEventTimings() {
            if (this.mEvents == null) {
                return Collections.emptyList();
            }
            LinkedList<EventTiming> result = new LinkedList<>();
            Map<String, PendingResponse> pendingResponses = new HashMap<>();
            Iterator<Event> it = this.mEvents.iterator();
            while (it.hasNext()) {
                Event event = it.next();
                if (EventManager.this.requestResponsePairs.containsKey(event.eventId)) {
                    for (TimedEventPair p : (List) EventManager.this.requestResponsePairs.get(event.eventId)) {
                        pendingResponses.put(p.mResponse, new PendingResponse(event.eventId, event.time, p.mTimeoutMillis, p.mName));
                        it = it;
                    }
                }
                Iterator<Event> it2 = it;
                PendingResponse pendingResponse = pendingResponses.remove(event.eventId);
                if (pendingResponse != null) {
                    long elapsedTime = event.time - pendingResponse.requestEventTimeMillis;
                    if (elapsedTime < pendingResponse.timeoutMillis) {
                        result.add(new EventTiming(pendingResponse.name, elapsedTime));
                    }
                }
                it = it2;
            }
            return result;
        }

        private protected synchronized void dump(IndentingPrintWriter pw) {
            EventRecord record;
            pw.print(this.mRecordEntry.getDescription());
            pw.increaseIndent();
            for (Event event : this.mEvents) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized EventManager(SessionManager.ISessionIdQueryHandler l) {
        this.mSessionIdHandler = l;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void event(Loggable recordEntry, String event, Object data) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dumpEvents(IndentingPrintWriter pw) {
        pw.println("Historical Events:");
        pw.increaseIndent();
        Iterator<EventRecord> it = this.mEventRecords.iterator();
        while (it.hasNext()) {
            EventRecord eventRecord = it.next();
            eventRecord.dump(pw);
        }
        pw.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dumpEventsTimeline(IndentingPrintWriter pw) {
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
            pw.print(",");
            pw.print(((Loggable) event.first).getId());
            pw.print(",");
            pw.print(((Event) event.second).eventId);
            pw.print(",");
            pw.println(((Event) event.second).data);
        }
        pw.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void changeEventCacheSize(int newSize) {
        LinkedBlockingQueue<EventRecord> oldEventLog = this.mEventRecords;
        this.mEventRecords = new LinkedBlockingQueue<>(newSize);
        this.mCallEventRecordMap.clear();
        oldEventLog.forEach(new Consumer() { // from class: android.telecom.Logging.-$$Lambda$EventManager$uddp6XAJ90VBwdTiuzBdSYelntQ
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                EventManager.lambda$changeEventCacheSize$1(EventManager.this, (EventManager.EventRecord) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$changeEventCacheSize$1(EventManager eventManager, EventRecord newRecord) {
        EventRecord record;
        Loggable recordEntry = newRecord.getRecordEntry();
        if (eventManager.mEventRecords.remainingCapacity() == 0 && (record = eventManager.mEventRecords.poll()) != null) {
            eventManager.mCallEventRecordMap.remove(record.getRecordEntry());
        }
        eventManager.mEventRecords.add(newRecord);
        eventManager.mCallEventRecordMap.put(recordEntry, newRecord);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void registerEventListener(EventListener e) {
        if (e != null) {
            synchronized (mSync) {
                this.mEventListeners.add(e);
            }
        }
    }

    @VisibleForTesting
    private protected synchronized LinkedBlockingQueue<EventRecord> getEventRecords() {
        return this.mEventRecords;
    }

    @VisibleForTesting
    private protected synchronized Map<Loggable, EventRecord> getCallEventRecordMap() {
        return this.mCallEventRecordMap;
    }

    public protected synchronized void addEventRecord(EventRecord newRecord) {
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
