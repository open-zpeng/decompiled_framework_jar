package com.android.server;
/* loaded from: classes3.dex */
public final class AlarmManagerServiceDumpProto {
    private protected static final long ALARM_STATS = 2246267895847L;
    private protected static final long ALLOW_WHILE_IDLE_DISPATCHES = 2246267895848L;
    private protected static final long BROADCAST_REF_COUNT = 1120986464285L;
    private protected static final long CURRENT_TIME = 1112396529665L;
    private protected static final long DELAYED_ALARM_COUNT = 1120986464281L;
    private protected static final long DEVICE_IDLE_USER_WHITELIST_APP_IDS = 2220498092049L;
    private protected static final long ELAPSED_REALTIME = 1112396529666L;
    private protected static final long FORCE_APP_STANDBY_TRACKER = 1146756268038L;
    private protected static final long IS_INTERACTIVE = 1133871366151L;
    private protected static final long LAST_ALLOW_WHILE_IDLE_DISPATCH_TIMES = 2246267895844L;
    private protected static final long LAST_TIME_CHANGE_CLOCK_TIME = 1112396529667L;
    private protected static final long LAST_TIME_CHANGE_REALTIME = 1112396529668L;
    private protected static final long LISTENER_FINISH_COUNT = 1120986464289L;
    private protected static final long LISTENER_SEND_COUNT = 1120986464288L;
    private protected static final long MAX_DELAY_DURATION_MS = 1112396529691L;
    private protected static final long MAX_NON_INTERACTIVE_DURATION_MS = 1112396529692L;
    private protected static final long MAX_WAKEUP_DELAY_MS = 1112396529673L;
    private protected static final long NEXT_ALARM_CLOCK_METADATA = 2246267895826L;
    private protected static final long NEXT_WAKE_FROM_IDLE = 1146756268055L;
    private protected static final long OUTSTANDING_DELIVERIES = 2246267895842L;
    private protected static final long PAST_DUE_NON_WAKEUP_ALARMS = 2246267895832L;
    private protected static final long PENDING_ALARM_BATCHES = 2246267895827L;
    private protected static final long PENDING_IDLE_UNTIL = 1146756268053L;
    private protected static final long PENDING_INTENT_FINISH_COUNT = 1120986464287L;
    private protected static final long PENDING_INTENT_SEND_COUNT = 1120986464286L;
    private protected static final long PENDING_USER_BLOCKED_BACKGROUND_ALARMS = 2246267895828L;
    private protected static final long PENDING_WHILE_IDLE_ALARMS = 2246267895830L;
    private protected static final long RECENT_PROBLEMS = 1146756268069L;
    private protected static final long RECENT_WAKEUP_HISTORY = 2246267895849L;
    private protected static final long SETTINGS = 1146756268037L;
    private protected static final long TIME_CHANGE_EVENT_COUNT = 1112396529680L;
    private protected static final long TIME_SINCE_LAST_DISPATCH_MS = 1112396529674L;
    private protected static final long TIME_SINCE_LAST_WAKEUP_MS = 1112396529678L;
    private protected static final long TIME_SINCE_LAST_WAKEUP_SET_MS = 1112396529679L;
    private protected static final long TIME_SINCE_NON_INTERACTIVE_MS = 1112396529672L;
    private protected static final long TIME_UNTIL_NEXT_NON_WAKEUP_ALARM_MS = 1112396529676L;
    private protected static final long TIME_UNTIL_NEXT_NON_WAKEUP_DELIVERY_MS = 1112396529675L;
    private protected static final long TIME_UNTIL_NEXT_WAKEUP_MS = 1112396529677L;
    private protected static final long TOP_ALARMS = 2246267895846L;
    private protected static final long TOTAL_DELAY_TIME_MS = 1112396529690L;
    private protected static final long USE_ALLOW_WHILE_IDLE_SHORT_TIME = 2220498092067L;

    private protected synchronized AlarmManagerServiceDumpProto() {
    }

    /* loaded from: classes3.dex */
    public final class LastAllowWhileIdleDispatch {
        private protected static final long NEXT_ALLOWED_MS = 1112396529667L;
        private protected static final long TIME_MS = 1112396529666L;
        private protected static final long UID = 1120986464257L;

        public LastAllowWhileIdleDispatch() {
        }
    }

    /* loaded from: classes3.dex */
    public final class TopAlarm {
        private protected static final long FILTER = 1146756268035L;
        private protected static final long PACKAGE_NAME = 1138166333442L;
        private protected static final long UID = 1120986464257L;

        public TopAlarm() {
        }
    }

    /* loaded from: classes3.dex */
    public final class AlarmStat {
        private protected static final long BROADCAST = 1146756268033L;
        private protected static final long FILTERS = 2246267895810L;

        public AlarmStat() {
        }
    }
}
