package android.telecom.Logging;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.provider.Settings;
import android.telecom.Log;
import android.telecom.Logging.Session;
import android.util.Base64;
import com.android.internal.annotations.VisibleForTesting;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes2.dex */
public class SessionManager {
    public protected static final long DEFAULT_SESSION_TIMEOUT_MS = 30000;
    public protected static final String LOGGING_TAG = "Logging";
    public protected static final long SESSION_ID_ROLLOVER_THRESHOLD = 262144;
    public protected static final String TIMEOUTS_PREFIX = "telecom.";
    public protected Context mContext;
    public protected int sCodeEntryCounter = 0;
    @VisibleForTesting
    private protected ConcurrentHashMap<Integer, Session> mSessionMapper = new ConcurrentHashMap<>(100);
    @VisibleForTesting
    private protected java.lang.Runnable mCleanStaleSessions = new java.lang.Runnable() { // from class: android.telecom.Logging.-$$Lambda$SessionManager$VyH2gT1EjIvzDy_C9JfTT60CISM
        @Override // java.lang.Runnable
        public final void run() {
            r0.cleanupStaleSessions(SessionManager.this.getSessionCleanupTimeoutMs());
        }
    };
    public protected Handler mSessionCleanupHandler = new Handler(Looper.getMainLooper());
    @VisibleForTesting
    private protected ICurrentThreadId mCurrentThreadId = new ICurrentThreadId() { // from class: android.telecom.Logging.-$$Lambda$L5F_SL2jOCUETYvgdB36aGwY50E
        public final int get() {
            return Process.myTid();
        }
    };
    public protected ISessionCleanupTimeoutMs mSessionCleanupTimeoutMs = new ISessionCleanupTimeoutMs() { // from class: android.telecom.Logging.-$$Lambda$SessionManager$hhtZwTEbvO-fLNlAvB6Do9_2gW4
        public final long get() {
            return SessionManager.lambda$new$1(SessionManager.this);
        }
    };
    public protected List<ISessionListener> mSessionListeners = new ArrayList();

    /* loaded from: classes2.dex */
    public interface ICurrentThreadId {
        private protected synchronized int get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public interface ISessionCleanupTimeoutMs {
        private protected synchronized long get();
    }

    /* loaded from: classes2.dex */
    public interface ISessionIdQueryHandler {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized String getSessionId();
    }

    /* loaded from: classes2.dex */
    public interface ISessionListener {
        private protected synchronized void sessionComplete(String str, long j);
    }

    public static /* synthetic */ long lambda$new$1(SessionManager sessionManager) {
        if (sessionManager.mContext == null) {
            return 30000L;
        }
        return sessionManager.getCleanupTimeout(sessionManager.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setContext(Context context) {
        this.mContext = context;
    }

    public protected synchronized long getSessionCleanupTimeoutMs() {
        return this.mSessionCleanupTimeoutMs.get();
    }

    public protected synchronized void resetStaleSessionTimer() {
        this.mSessionCleanupHandler.removeCallbacksAndMessages(null);
        if (this.mCleanStaleSessions != null) {
            this.mSessionCleanupHandler.postDelayed(this.mCleanStaleSessions, getSessionCleanupTimeoutMs());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startSession(Session.Info info, String shortMethodName, String callerIdentification) {
        try {
            if (info == null) {
                startSession(shortMethodName, callerIdentification);
            } else {
                startExternalSession(info, shortMethodName);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startSession(String shortMethodName, String callerIdentification) {
        resetStaleSessionTimer();
        int threadId = getCallingThreadId();
        Session activeSession = this.mSessionMapper.get(Integer.valueOf(threadId));
        if (activeSession != null) {
            Session childSession = createSubsession(true);
            continueSession(childSession, shortMethodName);
            return;
        }
        Log.d(LOGGING_TAG, "START_SESSION", new Object[0]);
        Session newSession = new Session(getNextSessionID(), shortMethodName, System.currentTimeMillis(), false, callerIdentification);
        this.mSessionMapper.put(Integer.valueOf(threadId), newSession);
    }

    private protected synchronized void startExternalSession(Session.Info sessionInfo, String shortMethodName) {
        if (sessionInfo == null) {
            return;
        }
        int threadId = getCallingThreadId();
        Session threadSession = this.mSessionMapper.get(Integer.valueOf(threadId));
        if (threadSession != null) {
            Log.w(LOGGING_TAG, "trying to start an external session with a session already active.", new Object[0]);
            return;
        }
        Log.d(LOGGING_TAG, "START_EXTERNAL_SESSION", new Object[0]);
        Session externalSession = new Session("E-" + sessionInfo.sessionId, sessionInfo.methodPath, System.currentTimeMillis(), false, null);
        externalSession.setIsExternal(true);
        externalSession.markSessionCompleted(-1L);
        this.mSessionMapper.put(Integer.valueOf(threadId), externalSession);
        Session childSession = createSubsession();
        continueSession(childSession, shortMethodName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Session createSubsession() {
        return createSubsession(false);
    }

    public protected synchronized Session createSubsession(boolean isStartedFromActiveSession) {
        int threadId = getCallingThreadId();
        Session threadSession = this.mSessionMapper.get(Integer.valueOf(threadId));
        if (threadSession == null) {
            Log.d(LOGGING_TAG, "Log.createSubsession was called with no session active.", new Object[0]);
            return null;
        }
        Session newSubsession = new Session(threadSession.getNextChildId(), threadSession.getShortMethodName(), System.currentTimeMillis(), isStartedFromActiveSession, null);
        threadSession.addChild(newSubsession);
        newSubsession.setParentSession(threadSession);
        if (!isStartedFromActiveSession) {
            Log.v(LOGGING_TAG, "CREATE_SUBSESSION " + newSubsession.toString(), new Object[0]);
        } else {
            Log.v(LOGGING_TAG, "CREATE_SUBSESSION (Invisible subsession)", new Object[0]);
        }
        return newSubsession;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Session.Info getExternalSession() {
        int threadId = getCallingThreadId();
        Session threadSession = this.mSessionMapper.get(Integer.valueOf(threadId));
        if (threadSession == null) {
            Log.d(LOGGING_TAG, "Log.getExternalSession was called with no session active.", new Object[0]);
            return null;
        }
        return threadSession.getInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void cancelSubsession(Session subsession) {
        if (subsession == null) {
            return;
        }
        subsession.markSessionCompleted(-1L);
        endParentSessions(subsession);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void continueSession(Session subsession, String shortMethodName) {
        if (subsession == null) {
            return;
        }
        resetStaleSessionTimer();
        subsession.setShortMethodName(shortMethodName);
        subsession.setExecutionStartTimeMs(System.currentTimeMillis());
        Session parentSession = subsession.getParentSession();
        if (parentSession == null) {
            Log.i(LOGGING_TAG, "Log.continueSession was called with no session active for method " + shortMethodName, new Object[0]);
            return;
        }
        this.mSessionMapper.put(Integer.valueOf(getCallingThreadId()), subsession);
        if (!subsession.isStartedFromActiveSession()) {
            Log.v(LOGGING_TAG, "CONTINUE_SUBSESSION", new Object[0]);
        } else {
            Log.v(LOGGING_TAG, "CONTINUE_SUBSESSION (Invisible Subsession) with Method " + shortMethodName, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void endSession() {
        int threadId = getCallingThreadId();
        Session completedSession = this.mSessionMapper.get(Integer.valueOf(threadId));
        if (completedSession == null) {
            Log.w(LOGGING_TAG, "Log.endSession was called with no session active.", new Object[0]);
            return;
        }
        completedSession.markSessionCompleted(System.currentTimeMillis());
        if (!completedSession.isStartedFromActiveSession()) {
            Log.v(LOGGING_TAG, "END_SUBSESSION (dur: " + completedSession.getLocalExecutionTime() + " mS)", new Object[0]);
        } else {
            Log.v(LOGGING_TAG, "END_SUBSESSION (Invisible Subsession) (dur: " + completedSession.getLocalExecutionTime() + " ms)", new Object[0]);
        }
        Session parentSession = completedSession.getParentSession();
        this.mSessionMapper.remove(Integer.valueOf(threadId));
        endParentSessions(completedSession);
        if (parentSession != null && !parentSession.isSessionCompleted() && completedSession.isStartedFromActiveSession()) {
            this.mSessionMapper.put(Integer.valueOf(threadId), parentSession);
        }
    }

    public protected synchronized void endParentSessions(Session subsession) {
        if (!subsession.isSessionCompleted() || subsession.getChildSessions().size() != 0) {
            return;
        }
        Session parentSession = subsession.getParentSession();
        if (parentSession != null) {
            subsession.setParentSession(null);
            parentSession.removeChild(subsession);
            if (parentSession.isExternal()) {
                notifySessionCompleteListeners(subsession.getShortMethodName(), System.currentTimeMillis() - subsession.getExecutionStartTimeMilliseconds());
            }
            endParentSessions(parentSession);
            return;
        }
        long fullSessionTimeMs = System.currentTimeMillis() - subsession.getExecutionStartTimeMilliseconds();
        Log.d(LOGGING_TAG, "END_SESSION (dur: " + fullSessionTimeMs + " ms): " + subsession.toString(), new Object[0]);
        if (!subsession.isExternal()) {
            notifySessionCompleteListeners(subsession.getShortMethodName(), fullSessionTimeMs);
        }
    }

    public protected synchronized void notifySessionCompleteListeners(String methodName, long sessionTimeMs) {
        for (ISessionListener l : this.mSessionListeners) {
            l.sessionComplete(methodName, sessionTimeMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String getSessionId() {
        Session currentSession = this.mSessionMapper.get(Integer.valueOf(getCallingThreadId()));
        return currentSession != null ? currentSession.toString() : "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void registerSessionListener(ISessionListener l) {
        if (l != null) {
            this.mSessionListeners.add(l);
        }
    }

    public protected synchronized String getNextSessionID() {
        Integer nextId;
        int i = this.sCodeEntryCounter;
        this.sCodeEntryCounter = i + 1;
        nextId = Integer.valueOf(i);
        if (nextId.intValue() >= 262144) {
            restartSessionCounter();
            int i2 = this.sCodeEntryCounter;
            this.sCodeEntryCounter = i2 + 1;
            nextId = Integer.valueOf(i2);
        }
        return getBase64Encoding(nextId.intValue());
    }

    public protected synchronized void restartSessionCounter() {
        this.sCodeEntryCounter = 0;
    }

    public protected synchronized String getBase64Encoding(int number) {
        byte[] idByteArray = ByteBuffer.allocate(4).putInt(number).array();
        return Base64.encodeToString(Arrays.copyOfRange(idByteArray, 2, 4), 3);
    }

    public protected synchronized int getCallingThreadId() {
        return this.mCurrentThreadId.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @VisibleForTesting
    public synchronized void cleanupStaleSessions(long timeoutMs) {
        String logMessage = "Stale Sessions Cleaned:\n";
        boolean isSessionsStale = false;
        long currentTimeMs = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, Session>> it = this.mSessionMapper.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Session> entry = it.next();
            Session session = entry.getValue();
            if (currentTimeMs - session.getExecutionStartTimeMilliseconds() > timeoutMs) {
                it.remove();
                logMessage = logMessage + session.printFullSessionTree() + "\n";
                isSessionsStale = true;
            }
        }
        if (isSessionsStale) {
            Log.w(LOGGING_TAG, logMessage, new Object[0]);
        } else {
            Log.v(LOGGING_TAG, "No stale logging sessions needed to be cleaned...", new Object[0]);
        }
    }

    public protected synchronized long getCleanupTimeout(Context context) {
        return Settings.Secure.getLong(context.getContentResolver(), "telecom.stale_session_cleanup_timeout_millis", 30000L);
    }
}
