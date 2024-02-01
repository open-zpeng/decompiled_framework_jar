package android.telecom;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.SettingsStringUtil;
import android.telecom.Logging.EventManager;
import android.telecom.Logging.Session;
import android.telecom.Logging.SessionManager;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.util.IndentingPrintWriter;
import java.util.IllegalFormatException;
import java.util.Locale;
/* loaded from: classes2.dex */
public class Log {
    private static final int EVENTS_TO_CACHE = 10;
    private static final int EVENTS_TO_CACHE_DEBUG = 20;
    private static final long EXTENDED_LOGGING_DURATION_MILLIS = 1800000;
    private static final boolean FORCE_LOGGING = false;
    private static EventManager sEventManager;
    private static SessionManager sSessionManager;
    @VisibleForTesting
    public static String TAG = "TelecomFramework";
    public static boolean DEBUG = isLoggable(3);
    public static boolean INFO = isLoggable(4);
    public static boolean VERBOSE = isLoggable(2);
    public static boolean WARN = isLoggable(5);
    public static boolean ERROR = isLoggable(6);
    private static final boolean USER_BUILD = Build.IS_USER;
    private static final Object sSingletonSync = new Object();
    private static boolean sIsUserExtendedLoggingEnabled = false;
    private static long sUserExtendedLoggingStopTime = 0;

    private synchronized Log() {
    }

    public static void d(String prefix, String format, Object... args) {
        if (sIsUserExtendedLoggingEnabled) {
            maybeDisableLogging();
            Slog.i(TAG, buildMessage(prefix, format, args));
        } else if (DEBUG) {
            Slog.d(TAG, buildMessage(prefix, format, args));
        }
    }

    public static void d(Object objectPrefix, String format, Object... args) {
        if (sIsUserExtendedLoggingEnabled) {
            maybeDisableLogging();
            Slog.i(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        } else if (DEBUG) {
            Slog.d(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        }
    }

    public static void i(String prefix, String format, Object... args) {
        if (INFO) {
            Slog.i(TAG, buildMessage(prefix, format, args));
        }
    }

    public static void i(Object objectPrefix, String format, Object... args) {
        if (INFO) {
            Slog.i(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        }
    }

    public static void v(String prefix, String format, Object... args) {
        if (sIsUserExtendedLoggingEnabled) {
            maybeDisableLogging();
            Slog.i(TAG, buildMessage(prefix, format, args));
        } else if (VERBOSE) {
            Slog.v(TAG, buildMessage(prefix, format, args));
        }
    }

    public static void v(Object objectPrefix, String format, Object... args) {
        if (sIsUserExtendedLoggingEnabled) {
            maybeDisableLogging();
            Slog.i(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        } else if (VERBOSE) {
            Slog.v(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        }
    }

    public static void w(String prefix, String format, Object... args) {
        if (WARN) {
            Slog.w(TAG, buildMessage(prefix, format, args));
        }
    }

    public static void w(Object objectPrefix, String format, Object... args) {
        if (WARN) {
            Slog.w(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args));
        }
    }

    public static void e(String prefix, Throwable tr, String format, Object... args) {
        if (ERROR) {
            Slog.e(TAG, buildMessage(prefix, format, args), tr);
        }
    }

    public static void e(Object objectPrefix, Throwable tr, String format, Object... args) {
        if (ERROR) {
            Slog.e(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args), tr);
        }
    }

    public static void wtf(String prefix, Throwable tr, String format, Object... args) {
        Slog.wtf(TAG, buildMessage(prefix, format, args), tr);
    }

    public static void wtf(Object objectPrefix, Throwable tr, String format, Object... args) {
        Slog.wtf(TAG, buildMessage(getPrefixFromObject(objectPrefix), format, args), tr);
    }

    public static void wtf(String prefix, String format, Object... args) {
        String msg = buildMessage(prefix, format, args);
        Slog.wtf(TAG, msg, new IllegalStateException(msg));
    }

    public static void wtf(Object objectPrefix, String format, Object... args) {
        String msg = buildMessage(getPrefixFromObject(objectPrefix), format, args);
        Slog.wtf(TAG, msg, new IllegalStateException(msg));
    }

    public static synchronized void setSessionContext(Context context) {
        getSessionManager().setContext(context);
    }

    public static synchronized void startSession(String shortMethodName) {
        getSessionManager().startSession(shortMethodName, null);
    }

    public static synchronized void startSession(Session.Info info, String shortMethodName) {
        getSessionManager().startSession(info, shortMethodName, null);
    }

    public static synchronized void startSession(String shortMethodName, String callerIdentification) {
        getSessionManager().startSession(shortMethodName, callerIdentification);
    }

    public static synchronized void startSession(Session.Info info, String shortMethodName, String callerIdentification) {
        getSessionManager().startSession(info, shortMethodName, callerIdentification);
    }

    public static synchronized Session createSubsession() {
        return getSessionManager().createSubsession();
    }

    public static synchronized Session.Info getExternalSession() {
        return getSessionManager().getExternalSession();
    }

    public static synchronized void cancelSubsession(Session subsession) {
        getSessionManager().cancelSubsession(subsession);
    }

    public static synchronized void continueSession(Session subsession, String shortMethodName) {
        getSessionManager().continueSession(subsession, shortMethodName);
    }

    public static synchronized void endSession() {
        getSessionManager().endSession();
    }

    public static synchronized void registerSessionListener(SessionManager.ISessionListener l) {
        getSessionManager().registerSessionListener(l);
    }

    public static synchronized String getSessionId() {
        synchronized (sSingletonSync) {
            if (sSessionManager != null) {
                return getSessionManager().getSessionId();
            }
            return "";
        }
    }

    public static synchronized void addEvent(EventManager.Loggable recordEntry, String event) {
        getEventManager().event(recordEntry, event, null);
    }

    public static synchronized void addEvent(EventManager.Loggable recordEntry, String event, Object data) {
        getEventManager().event(recordEntry, event, data);
    }

    public static void addEvent(EventManager.Loggable recordEntry, String event, String format, Object... args) {
        getEventManager().event(recordEntry, event, format, args);
    }

    public static synchronized void registerEventListener(EventManager.EventListener e) {
        getEventManager().registerEventListener(e);
    }

    public static synchronized void addRequestResponsePair(EventManager.TimedEventPair p) {
        getEventManager().addRequestResponsePair(p);
    }

    public static synchronized void dumpEvents(IndentingPrintWriter pw) {
        synchronized (sSingletonSync) {
            if (sEventManager != null) {
                getEventManager().dumpEvents(pw);
            } else {
                pw.println("No Historical Events Logged.");
            }
        }
    }

    public static synchronized void dumpEventsTimeline(IndentingPrintWriter pw) {
        synchronized (sSingletonSync) {
            if (sEventManager != null) {
                getEventManager().dumpEventsTimeline(pw);
            } else {
                pw.println("No Historical Events Logged.");
            }
        }
    }

    public static synchronized void setIsExtendedLoggingEnabled(boolean isExtendedLoggingEnabled) {
        if (sIsUserExtendedLoggingEnabled == isExtendedLoggingEnabled) {
            return;
        }
        if (sEventManager != null) {
            sEventManager.changeEventCacheSize(isExtendedLoggingEnabled ? 20 : 10);
        }
        sIsUserExtendedLoggingEnabled = isExtendedLoggingEnabled;
        if (sIsUserExtendedLoggingEnabled) {
            sUserExtendedLoggingStopTime = System.currentTimeMillis() + 1800000;
        } else {
            sUserExtendedLoggingStopTime = 0L;
        }
    }

    private static synchronized EventManager getEventManager() {
        if (sEventManager == null) {
            synchronized (sSingletonSync) {
                if (sEventManager == null) {
                    sEventManager = new EventManager(new SessionManager.ISessionIdQueryHandler() { // from class: android.telecom.-$$Lambda$qa4s1Fm2YuohEunaJUJcmJXDXG0
                        public final String getSessionId() {
                            return Log.getSessionId();
                        }
                    });
                    return sEventManager;
                }
            }
        }
        return sEventManager;
    }

    @VisibleForTesting
    public static synchronized SessionManager getSessionManager() {
        if (sSessionManager == null) {
            synchronized (sSingletonSync) {
                if (sSessionManager == null) {
                    sSessionManager = new SessionManager();
                    return sSessionManager;
                }
            }
        }
        return sSessionManager;
    }

    public static synchronized void setTag(String tag) {
        TAG = tag;
        DEBUG = isLoggable(3);
        INFO = isLoggable(4);
        VERBOSE = isLoggable(2);
        WARN = isLoggable(5);
        ERROR = isLoggable(6);
    }

    private static synchronized void maybeDisableLogging() {
        if (sIsUserExtendedLoggingEnabled && sUserExtendedLoggingStopTime < System.currentTimeMillis()) {
            sUserExtendedLoggingStopTime = 0L;
            sIsUserExtendedLoggingEnabled = false;
        }
    }

    public static synchronized boolean isLoggable(int level) {
        return android.util.Log.isLoggable(TAG, level);
    }

    public static synchronized String piiHandle(Object pii) {
        if (pii == null || VERBOSE) {
            return String.valueOf(pii);
        }
        StringBuilder sb = new StringBuilder();
        if (pii instanceof Uri) {
            Uri uri = (Uri) pii;
            String scheme = uri.getScheme();
            if (!TextUtils.isEmpty(scheme)) {
                sb.append(scheme);
                sb.append(SettingsStringUtil.DELIMITER);
            }
            String textToObfuscate = uri.getSchemeSpecificPart();
            int i = 0;
            if (PhoneAccount.SCHEME_TEL.equals(scheme)) {
                while (true) {
                    int i2 = i;
                    if (i2 >= textToObfuscate.length()) {
                        break;
                    }
                    char c = textToObfuscate.charAt(i2);
                    sb.append(PhoneNumberUtils.isDialable(c) ? PhoneConstants.APN_TYPE_ALL : Character.valueOf(c));
                    i = i2 + 1;
                }
            } else if ("sip".equals(scheme)) {
                while (true) {
                    int i3 = i;
                    if (i3 >= textToObfuscate.length()) {
                        break;
                    }
                    char c2 = textToObfuscate.charAt(i3);
                    if (c2 != '@' && c2 != '.') {
                        c2 = '*';
                    }
                    sb.append(c2);
                    i = i3 + 1;
                }
            } else {
                sb.append(pii(pii));
            }
        }
        return sb.toString();
    }

    public static synchronized String pii(Object pii) {
        if (pii == null || VERBOSE) {
            return String.valueOf(pii);
        }
        return "***";
    }

    private static synchronized String getPrefixFromObject(Object obj) {
        return obj == null ? "<null>" : obj.getClass().getSimpleName();
    }

    private static String buildMessage(String prefix, String format, Object... args) {
        String sessionPostfix;
        String msg;
        String sessionName = getSessionId();
        if (TextUtils.isEmpty(sessionName)) {
            sessionPostfix = "";
        } else {
            sessionPostfix = ": " + sessionName;
        }
        if (args != null) {
            try {
            } catch (IllegalFormatException ife) {
                e(TAG, (Throwable) ife, "Log: IllegalFormatException: formatString='%s' numArgs=%d", format, Integer.valueOf(args.length));
                msg = format + " (An error occurred while formatting the message.)";
            }
            if (args.length != 0) {
                msg = String.format(Locale.US, format, args);
                return String.format(Locale.US, "%s: %s%s", prefix, msg, sessionPostfix);
            }
        }
        msg = format;
        return String.format(Locale.US, "%s: %s%s", prefix, msg, sessionPostfix);
    }
}
