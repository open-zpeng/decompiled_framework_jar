package android.service.notification;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Contacts;
import android.provider.Settings;
import android.provider.SettingsStringUtil;
import android.provider.Telephony;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.R;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.telephony.IccCardConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: classes2.dex */
public class ZenModeConfig implements Parcelable {
    private static final String ALLOW_ATT_ALARMS = "alarms";
    private static final String ALLOW_ATT_CALLS = "calls";
    private static final String ALLOW_ATT_CALLS_FROM = "callsFrom";
    private static final String ALLOW_ATT_EVENTS = "events";
    private static final String ALLOW_ATT_FROM = "from";
    private static final String ALLOW_ATT_MEDIA = "media";
    private static final String ALLOW_ATT_MESSAGES = "messages";
    private static final String ALLOW_ATT_MESSAGES_FROM = "messagesFrom";
    private static final String ALLOW_ATT_REMINDERS = "reminders";
    private static final String ALLOW_ATT_REPEAT_CALLERS = "repeatCallers";
    private static final String ALLOW_ATT_SCREEN_OFF = "visualScreenOff";
    private static final String ALLOW_ATT_SCREEN_ON = "visualScreenOn";
    private static final String ALLOW_ATT_SYSTEM = "system";
    private static final String ALLOW_TAG = "allow";
    private static final String AUTOMATIC_TAG = "automatic";
    private static final String CONDITION_ATT_FLAGS = "flags";
    private static final String CONDITION_ATT_ICON = "icon";
    private static final String CONDITION_ATT_ID = "id";
    private static final String CONDITION_ATT_LINE1 = "line1";
    private static final String CONDITION_ATT_LINE2 = "line2";
    private static final String CONDITION_ATT_STATE = "state";
    private static final String CONDITION_ATT_SUMMARY = "summary";
    public static final String COUNTDOWN_PATH = "countdown";
    private static final int DAY_MINUTES = 1440;
    private static final boolean DEFAULT_ALLOW_ALARMS = true;
    private static final boolean DEFAULT_ALLOW_CALLS = true;
    private static final boolean DEFAULT_ALLOW_EVENTS = false;
    private static final boolean DEFAULT_ALLOW_MEDIA = true;
    private static final boolean DEFAULT_ALLOW_MESSAGES = false;
    private static final boolean DEFAULT_ALLOW_REMINDERS = false;
    private static final boolean DEFAULT_ALLOW_REPEAT_CALLERS = true;
    private static final boolean DEFAULT_ALLOW_SYSTEM = false;
    private static final int DEFAULT_CALLS_SOURCE = 2;
    private static final boolean DEFAULT_CHANNELS_BYPASSING_DND = false;
    private static final int DEFAULT_SOURCE = 1;
    private static final int DEFAULT_SUPPRESSED_VISUAL_EFFECTS = 0;
    private static final String DISALLOW_ATT_VISUAL_EFFECTS = "visualEffects";
    private static final String DISALLOW_TAG = "disallow";
    public static final String EVENT_PATH = "event";
    public static final String IS_ALARM_PATH = "alarm";
    private static final String MANUAL_TAG = "manual";
    public static final int MAX_SOURCE = 2;
    private static final int MINUTES_MS = 60000;
    private static final String RULE_ATT_COMPONENT = "component";
    private static final String RULE_ATT_CONDITION_ID = "conditionId";
    private static final String RULE_ATT_CREATION_TIME = "creationTime";
    private static final String RULE_ATT_ENABLED = "enabled";
    private static final String RULE_ATT_ENABLER = "enabler";
    private static final String RULE_ATT_ID = "ruleId";
    private static final String RULE_ATT_NAME = "name";
    private static final String RULE_ATT_SNOOZING = "snoozing";
    private static final String RULE_ATT_ZEN = "zen";
    public static final String SCHEDULE_PATH = "schedule";
    private static final int SECONDS_MS = 1000;
    public static final int SOURCE_ANYONE = 0;
    public static final int SOURCE_CONTACT = 1;
    public static final int SOURCE_STAR = 2;
    private static final String STATE_ATT_CHANNELS_BYPASSING_DND = "areChannelsBypassingDnd";
    private static final String STATE_TAG = "state";
    public static final String SYSTEM_AUTHORITY = "android";
    public static final int XML_VERSION = 8;
    private static final String ZEN_ATT_USER = "user";
    private static final String ZEN_ATT_VERSION = "version";
    public static final String ZEN_TAG = "zen";
    private static final int ZERO_VALUE_MS = 10000;
    private protected boolean allowAlarms;
    public boolean allowCalls;
    public int allowCallsFrom;
    public boolean allowEvents;
    public boolean allowMedia;
    public boolean allowMessages;
    public int allowMessagesFrom;
    public boolean allowReminders;
    public boolean allowRepeatCallers;
    public boolean allowSystem;
    public boolean areChannelsBypassingDnd;
    private protected ArrayMap<String, ZenRule> automaticRules;
    public ZenRule manualRule;
    public int suppressedVisualEffects;
    public int user;
    public int version;
    private static String TAG = "ZenModeConfig";
    public static final String EVERY_NIGHT_DEFAULT_RULE_ID = "EVERY_NIGHT_DEFAULT_RULE";
    public static final String EVENTS_DEFAULT_RULE_ID = "EVENTS_DEFAULT_RULE";
    public static final List<String> DEFAULT_RULE_IDS = Arrays.asList(EVERY_NIGHT_DEFAULT_RULE_ID, EVENTS_DEFAULT_RULE_ID);
    public static final int[] ALL_DAYS = {1, 2, 3, 4, 5, 6, 7};
    public static final int[] MINUTE_BUCKETS = generateMinuteBuckets();
    public static final Parcelable.Creator<ZenModeConfig> CREATOR = new Parcelable.Creator<ZenModeConfig>() { // from class: android.service.notification.ZenModeConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ZenModeConfig createFromParcel(Parcel source) {
            return new ZenModeConfig(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ZenModeConfig[] newArray(int size) {
            return new ZenModeConfig[size];
        }
    };

    private protected ZenModeConfig() {
        this.allowAlarms = true;
        this.allowMedia = true;
        this.allowSystem = false;
        this.allowCalls = true;
        this.allowRepeatCallers = true;
        this.allowMessages = false;
        this.allowReminders = false;
        this.allowEvents = false;
        this.allowCallsFrom = 2;
        this.allowMessagesFrom = 1;
        this.user = 0;
        this.suppressedVisualEffects = 0;
        this.areChannelsBypassingDnd = false;
        this.automaticRules = new ArrayMap<>();
    }

    public synchronized ZenModeConfig(Parcel source) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        this.allowAlarms = true;
        this.allowMedia = true;
        this.allowSystem = false;
        this.allowCalls = true;
        this.allowRepeatCallers = true;
        this.allowMessages = false;
        this.allowReminders = false;
        this.allowEvents = false;
        this.allowCallsFrom = 2;
        this.allowMessagesFrom = 1;
        this.user = 0;
        this.suppressedVisualEffects = 0;
        this.areChannelsBypassingDnd = false;
        this.automaticRules = new ArrayMap<>();
        if (source.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.allowCalls = z;
        if (source.readInt() == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.allowRepeatCallers = z2;
        if (source.readInt() == 1) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.allowMessages = z3;
        if (source.readInt() == 1) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.allowReminders = z4;
        if (source.readInt() == 1) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.allowEvents = z5;
        this.allowCallsFrom = source.readInt();
        this.allowMessagesFrom = source.readInt();
        this.user = source.readInt();
        this.manualRule = (ZenRule) source.readParcelable(null);
        int len = source.readInt();
        if (len > 0) {
            String[] ids = new String[len];
            ZenRule[] rules = new ZenRule[len];
            source.readStringArray(ids);
            source.readTypedArray(rules, ZenRule.CREATOR);
            for (int i = 0; i < len; i++) {
                this.automaticRules.put(ids[i], rules[i]);
            }
        }
        if (source.readInt() == 1) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.allowAlarms = z6;
        if (source.readInt() == 1) {
            z7 = true;
        } else {
            z7 = false;
        }
        this.allowMedia = z7;
        if (source.readInt() == 1) {
            z8 = true;
        } else {
            z8 = false;
        }
        this.allowSystem = z8;
        this.suppressedVisualEffects = source.readInt();
        this.areChannelsBypassingDnd = source.readInt() == 1;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.allowCalls ? 1 : 0);
        dest.writeInt(this.allowRepeatCallers ? 1 : 0);
        dest.writeInt(this.allowMessages ? 1 : 0);
        dest.writeInt(this.allowReminders ? 1 : 0);
        dest.writeInt(this.allowEvents ? 1 : 0);
        dest.writeInt(this.allowCallsFrom);
        dest.writeInt(this.allowMessagesFrom);
        dest.writeInt(this.user);
        dest.writeParcelable(this.manualRule, 0);
        if (!this.automaticRules.isEmpty()) {
            int len = this.automaticRules.size();
            String[] ids = new String[len];
            ZenRule[] rules = new ZenRule[len];
            for (int i = 0; i < len; i++) {
                ids[i] = this.automaticRules.keyAt(i);
                rules[i] = this.automaticRules.valueAt(i);
            }
            dest.writeInt(len);
            dest.writeStringArray(ids);
            dest.writeTypedArray(rules, 0);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.allowAlarms ? 1 : 0);
        dest.writeInt(this.allowMedia ? 1 : 0);
        dest.writeInt(this.allowSystem ? 1 : 0);
        dest.writeInt(this.suppressedVisualEffects);
        dest.writeInt(this.areChannelsBypassingDnd ? 1 : 0);
    }

    public String toString() {
        return ZenModeConfig.class.getSimpleName() + "[user=" + this.user + ",allowAlarms=" + this.allowAlarms + ",allowMedia=" + this.allowMedia + ",allowSystem=" + this.allowSystem + ",allowReminders=" + this.allowReminders + ",allowEvents=" + this.allowEvents + ",allowCalls=" + this.allowCalls + ",allowRepeatCallers=" + this.allowRepeatCallers + ",allowMessages=" + this.allowMessages + ",allowCallsFrom=" + sourceToString(this.allowCallsFrom) + ",allowMessagesFrom=" + sourceToString(this.allowMessagesFrom) + ",suppressedVisualEffects=" + this.suppressedVisualEffects + ",areChannelsBypassingDnd=" + this.areChannelsBypassingDnd + ",\nautomaticRules=" + rulesToString() + ",\nmanualRule=" + this.manualRule + ']';
    }

    private String rulesToString() {
        if (this.automaticRules.isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.automaticRules.size() * 28);
        buffer.append('{');
        for (int i = 0; i < this.automaticRules.size(); i++) {
            if (i > 0) {
                buffer.append(",\n");
            }
            Object value = this.automaticRules.valueAt(i);
            buffer.append(value);
        }
        buffer.append('}');
        return buffer.toString();
    }

    private synchronized Diff diff(ZenModeConfig to) {
        Diff d = new Diff();
        if (to != null) {
            if (this.user != to.user) {
                d.addLine("user", Integer.valueOf(this.user), Integer.valueOf(to.user));
            }
            if (this.allowAlarms != to.allowAlarms) {
                d.addLine("allowAlarms", Boolean.valueOf(this.allowAlarms), Boolean.valueOf(to.allowAlarms));
            }
            if (this.allowMedia != to.allowMedia) {
                d.addLine("allowMedia", Boolean.valueOf(this.allowMedia), Boolean.valueOf(to.allowMedia));
            }
            if (this.allowSystem != to.allowSystem) {
                d.addLine("allowSystem", Boolean.valueOf(this.allowSystem), Boolean.valueOf(to.allowSystem));
            }
            if (this.allowCalls != to.allowCalls) {
                d.addLine("allowCalls", Boolean.valueOf(this.allowCalls), Boolean.valueOf(to.allowCalls));
            }
            if (this.allowReminders != to.allowReminders) {
                d.addLine("allowReminders", Boolean.valueOf(this.allowReminders), Boolean.valueOf(to.allowReminders));
            }
            if (this.allowEvents != to.allowEvents) {
                d.addLine("allowEvents", Boolean.valueOf(this.allowEvents), Boolean.valueOf(to.allowEvents));
            }
            if (this.allowRepeatCallers != to.allowRepeatCallers) {
                d.addLine("allowRepeatCallers", Boolean.valueOf(this.allowRepeatCallers), Boolean.valueOf(to.allowRepeatCallers));
            }
            if (this.allowMessages != to.allowMessages) {
                d.addLine("allowMessages", Boolean.valueOf(this.allowMessages), Boolean.valueOf(to.allowMessages));
            }
            if (this.allowCallsFrom != to.allowCallsFrom) {
                d.addLine("allowCallsFrom", Integer.valueOf(this.allowCallsFrom), Integer.valueOf(to.allowCallsFrom));
            }
            if (this.allowMessagesFrom != to.allowMessagesFrom) {
                d.addLine("allowMessagesFrom", Integer.valueOf(this.allowMessagesFrom), Integer.valueOf(to.allowMessagesFrom));
            }
            if (this.suppressedVisualEffects != to.suppressedVisualEffects) {
                d.addLine("suppressedVisualEffects", Integer.valueOf(this.suppressedVisualEffects), Integer.valueOf(to.suppressedVisualEffects));
            }
            ArraySet<String> allRules = new ArraySet<>();
            addKeys(allRules, this.automaticRules);
            addKeys(allRules, to.automaticRules);
            int N = allRules.size();
            for (int i = 0; i < N; i++) {
                String rule = allRules.valueAt(i);
                ZenRule toRule = null;
                ZenRule fromRule = this.automaticRules != null ? this.automaticRules.get(rule) : null;
                if (to.automaticRules != null) {
                    toRule = to.automaticRules.get(rule);
                }
                ZenRule.appendDiff(d, "automaticRule[" + rule + "]", fromRule, toRule);
            }
            ZenRule.appendDiff(d, "manualRule", this.manualRule, to.manualRule);
            if (this.areChannelsBypassingDnd != to.areChannelsBypassingDnd) {
                d.addLine(STATE_ATT_CHANNELS_BYPASSING_DND, Boolean.valueOf(this.areChannelsBypassingDnd), Boolean.valueOf(to.areChannelsBypassingDnd));
            }
            return d;
        }
        return d.addLine("config", "delete");
    }

    public static synchronized Diff diff(ZenModeConfig from, ZenModeConfig to) {
        if (from == null) {
            Diff d = new Diff();
            if (to != null) {
                d.addLine("config", "insert");
            }
            return d;
        }
        return from.diff(to);
    }

    private static synchronized <T> void addKeys(ArraySet<T> set, ArrayMap<T, ?> map) {
        if (map != null) {
            for (int i = 0; i < map.size(); i++) {
                set.add(map.keyAt(i));
            }
        }
    }

    public synchronized boolean isValid() {
        if (isValidManualRule(this.manualRule)) {
            int N = this.automaticRules.size();
            for (int i = 0; i < N; i++) {
                if (!isValidAutomaticRule(this.automaticRules.valueAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static synchronized boolean isValidManualRule(ZenRule rule) {
        return rule == null || (Settings.Global.isValidZenMode(rule.zenMode) && sameCondition(rule));
    }

    private static synchronized boolean isValidAutomaticRule(ZenRule rule) {
        return (rule == null || TextUtils.isEmpty(rule.name) || !Settings.Global.isValidZenMode(rule.zenMode) || rule.conditionId == null || !sameCondition(rule)) ? false : true;
    }

    private static synchronized boolean sameCondition(ZenRule rule) {
        if (rule == null) {
            return false;
        }
        return rule.conditionId == null ? rule.condition == null : rule.condition == null || rule.conditionId.equals(rule.condition.id);
    }

    private static synchronized int[] generateMinuteBuckets() {
        int[] buckets = new int[15];
        buckets[0] = 15;
        buckets[1] = 30;
        buckets[2] = 45;
        for (int i = 1; i <= 12; i++) {
            buckets[2 + i] = 60 * i;
        }
        return buckets;
    }

    public static synchronized String sourceToString(int source) {
        switch (source) {
            case 0:
                return "anyone";
            case 1:
                return Contacts.AUTHORITY;
            case 2:
                return "stars";
            default:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
    }

    public boolean equals(Object o) {
        if (o instanceof ZenModeConfig) {
            if (o == this) {
                return true;
            }
            ZenModeConfig other = (ZenModeConfig) o;
            return other.allowAlarms == this.allowAlarms && other.allowMedia == this.allowMedia && other.allowSystem == this.allowSystem && other.allowCalls == this.allowCalls && other.allowRepeatCallers == this.allowRepeatCallers && other.allowMessages == this.allowMessages && other.allowCallsFrom == this.allowCallsFrom && other.allowMessagesFrom == this.allowMessagesFrom && other.allowReminders == this.allowReminders && other.allowEvents == this.allowEvents && other.user == this.user && Objects.equals(other.automaticRules, this.automaticRules) && Objects.equals(other.manualRule, this.manualRule) && other.suppressedVisualEffects == this.suppressedVisualEffects && other.areChannelsBypassingDnd == this.areChannelsBypassingDnd;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Boolean.valueOf(this.allowAlarms), Boolean.valueOf(this.allowMedia), Boolean.valueOf(this.allowSystem), Boolean.valueOf(this.allowCalls), Boolean.valueOf(this.allowRepeatCallers), Boolean.valueOf(this.allowMessages), Integer.valueOf(this.allowCallsFrom), Integer.valueOf(this.allowMessagesFrom), Boolean.valueOf(this.allowReminders), Boolean.valueOf(this.allowEvents), Integer.valueOf(this.user), this.automaticRules, this.manualRule, Integer.valueOf(this.suppressedVisualEffects), Boolean.valueOf(this.areChannelsBypassingDnd));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized String toDayList(int[] days) {
        if (days == null || days.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < days.length; i++) {
            if (i > 0) {
                sb.append('.');
            }
            sb.append(days[i]);
        }
        return sb.toString();
    }

    private static synchronized int[] tryParseDayList(String dayList, String sep) {
        if (dayList == null) {
            return null;
        }
        String[] tokens = dayList.split(sep);
        if (tokens.length == 0) {
            return null;
        }
        int[] rt = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            int day = tryParseInt(tokens[i], -1);
            if (day == -1) {
                return null;
            }
            rt[i] = day;
        }
        return rt;
    }

    private static synchronized int tryParseInt(String value, int defValue) {
        if (TextUtils.isEmpty(value)) {
            return defValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    private static synchronized long tryParseLong(String value, long defValue) {
        if (TextUtils.isEmpty(value)) {
            return defValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    public static synchronized ZenModeConfig readXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != 2 || !"zen".equals(parser.getName())) {
            return null;
        }
        ZenModeConfig rt = new ZenModeConfig();
        rt.version = safeInt(parser, "version", 8);
        rt.user = safeInt(parser, "user", rt.user);
        boolean readSuppressedEffects = false;
        while (true) {
            int type = parser.next();
            if (type != 1) {
                String tag = parser.getName();
                if (type == 3 && "zen".equals(tag)) {
                    return rt;
                }
                if (type == 2) {
                    if (ALLOW_TAG.equals(tag)) {
                        rt.allowCalls = safeBoolean(parser, ALLOW_ATT_CALLS, true);
                        rt.allowRepeatCallers = safeBoolean(parser, ALLOW_ATT_REPEAT_CALLERS, true);
                        rt.allowMessages = safeBoolean(parser, ALLOW_ATT_MESSAGES, false);
                        rt.allowReminders = safeBoolean(parser, ALLOW_ATT_REMINDERS, false);
                        rt.allowEvents = safeBoolean(parser, ALLOW_ATT_EVENTS, false);
                        int from = safeInt(parser, ALLOW_ATT_FROM, -1);
                        int callsFrom = safeInt(parser, ALLOW_ATT_CALLS_FROM, -1);
                        int messagesFrom = safeInt(parser, ALLOW_ATT_MESSAGES_FROM, -1);
                        if (isValidSource(callsFrom) && isValidSource(messagesFrom)) {
                            rt.allowCallsFrom = callsFrom;
                            rt.allowMessagesFrom = messagesFrom;
                        } else if (isValidSource(from)) {
                            Slog.i(TAG, "Migrating existing shared 'from': " + sourceToString(from));
                            rt.allowCallsFrom = from;
                            rt.allowMessagesFrom = from;
                        } else {
                            rt.allowCallsFrom = 2;
                            rt.allowMessagesFrom = 1;
                        }
                        rt.allowAlarms = safeBoolean(parser, ALLOW_ATT_ALARMS, true);
                        rt.allowMedia = safeBoolean(parser, "media", true);
                        rt.allowSystem = safeBoolean(parser, "system", false);
                        Boolean allowWhenScreenOff = unsafeBoolean(parser, ALLOW_ATT_SCREEN_OFF);
                        if (allowWhenScreenOff != null) {
                            readSuppressedEffects = true;
                            if (allowWhenScreenOff.booleanValue()) {
                                rt.suppressedVisualEffects |= 12;
                            }
                        }
                        Boolean allowWhenScreenOn = unsafeBoolean(parser, ALLOW_ATT_SCREEN_ON);
                        if (allowWhenScreenOn != null) {
                            readSuppressedEffects = true;
                            if (allowWhenScreenOn.booleanValue()) {
                                rt.suppressedVisualEffects |= 16;
                            }
                        }
                        if (readSuppressedEffects) {
                            Slog.d(TAG, "Migrated visual effects to " + rt.suppressedVisualEffects);
                        }
                    } else if (DISALLOW_TAG.equals(tag) && !readSuppressedEffects) {
                        rt.suppressedVisualEffects = safeInt(parser, DISALLOW_ATT_VISUAL_EFFECTS, 0);
                    } else if (MANUAL_TAG.equals(tag)) {
                        rt.manualRule = readRuleXml(parser);
                    } else if (AUTOMATIC_TAG.equals(tag)) {
                        String id = parser.getAttributeValue(null, RULE_ATT_ID);
                        ZenRule automaticRule = readRuleXml(parser);
                        if (id != null && automaticRule != null) {
                            automaticRule.id = id;
                            rt.automaticRules.put(id, automaticRule);
                        }
                    } else if ("state".equals(tag)) {
                        rt.areChannelsBypassingDnd = safeBoolean(parser, STATE_ATT_CHANNELS_BYPASSING_DND, false);
                    }
                }
            } else {
                throw new IllegalStateException("Failed to reach END_DOCUMENT");
            }
        }
    }

    public synchronized void writeXml(XmlSerializer out, Integer version) throws IOException {
        out.startTag(null, "zen");
        out.attribute(null, "version", Integer.toString(version == null ? 8 : version.intValue()));
        out.attribute(null, "user", Integer.toString(this.user));
        out.startTag(null, ALLOW_TAG);
        out.attribute(null, ALLOW_ATT_CALLS, Boolean.toString(this.allowCalls));
        out.attribute(null, ALLOW_ATT_REPEAT_CALLERS, Boolean.toString(this.allowRepeatCallers));
        out.attribute(null, ALLOW_ATT_MESSAGES, Boolean.toString(this.allowMessages));
        out.attribute(null, ALLOW_ATT_REMINDERS, Boolean.toString(this.allowReminders));
        out.attribute(null, ALLOW_ATT_EVENTS, Boolean.toString(this.allowEvents));
        out.attribute(null, ALLOW_ATT_CALLS_FROM, Integer.toString(this.allowCallsFrom));
        out.attribute(null, ALLOW_ATT_MESSAGES_FROM, Integer.toString(this.allowMessagesFrom));
        out.attribute(null, ALLOW_ATT_ALARMS, Boolean.toString(this.allowAlarms));
        out.attribute(null, "media", Boolean.toString(this.allowMedia));
        out.attribute(null, "system", Boolean.toString(this.allowSystem));
        out.endTag(null, ALLOW_TAG);
        out.startTag(null, DISALLOW_TAG);
        out.attribute(null, DISALLOW_ATT_VISUAL_EFFECTS, Integer.toString(this.suppressedVisualEffects));
        out.endTag(null, DISALLOW_TAG);
        if (this.manualRule != null) {
            out.startTag(null, MANUAL_TAG);
            writeRuleXml(this.manualRule, out);
            out.endTag(null, MANUAL_TAG);
        }
        int N = this.automaticRules.size();
        for (int i = 0; i < N; i++) {
            String id = this.automaticRules.keyAt(i);
            ZenRule automaticRule = this.automaticRules.valueAt(i);
            out.startTag(null, AUTOMATIC_TAG);
            out.attribute(null, RULE_ATT_ID, id);
            writeRuleXml(automaticRule, out);
            out.endTag(null, AUTOMATIC_TAG);
        }
        out.startTag(null, "state");
        out.attribute(null, STATE_ATT_CHANNELS_BYPASSING_DND, Boolean.toString(this.areChannelsBypassingDnd));
        out.endTag(null, "state");
        out.endTag(null, "zen");
    }

    public static synchronized ZenRule readRuleXml(XmlPullParser parser) {
        ZenRule rt = new ZenRule();
        rt.enabled = safeBoolean(parser, "enabled", true);
        rt.snoozing = safeBoolean(parser, RULE_ATT_SNOOZING, false);
        rt.name = parser.getAttributeValue(null, "name");
        String zen = parser.getAttributeValue(null, "zen");
        rt.zenMode = tryParseZenMode(zen, -1);
        if (rt.zenMode == -1) {
            String str = TAG;
            Slog.w(str, "Bad zen mode in rule xml:" + zen);
            return null;
        }
        rt.conditionId = safeUri(parser, RULE_ATT_CONDITION_ID);
        rt.component = safeComponentName(parser, "component");
        rt.creationTime = safeLong(parser, "creationTime", 0L);
        rt.enabler = parser.getAttributeValue(null, RULE_ATT_ENABLER);
        rt.condition = readConditionXml(parser);
        if (rt.zenMode != 1 && Condition.isValidId(rt.conditionId, SYSTEM_AUTHORITY)) {
            String str2 = TAG;
            Slog.i(str2, "Updating zenMode of automatic rule " + rt.name);
            rt.zenMode = 1;
        }
        return rt;
    }

    public static synchronized void writeRuleXml(ZenRule rule, XmlSerializer out) throws IOException {
        out.attribute(null, "enabled", Boolean.toString(rule.enabled));
        out.attribute(null, RULE_ATT_SNOOZING, Boolean.toString(rule.snoozing));
        if (rule.name != null) {
            out.attribute(null, "name", rule.name);
        }
        out.attribute(null, "zen", Integer.toString(rule.zenMode));
        if (rule.component != null) {
            out.attribute(null, "component", rule.component.flattenToString());
        }
        if (rule.conditionId != null) {
            out.attribute(null, RULE_ATT_CONDITION_ID, rule.conditionId.toString());
        }
        out.attribute(null, "creationTime", Long.toString(rule.creationTime));
        if (rule.enabler != null) {
            out.attribute(null, RULE_ATT_ENABLER, rule.enabler);
        }
        if (rule.condition != null) {
            writeConditionXml(rule.condition, out);
        }
    }

    public static synchronized Condition readConditionXml(XmlPullParser parser) {
        Uri id = safeUri(parser, "id");
        if (id != null) {
            String summary = parser.getAttributeValue(null, "summary");
            String line1 = parser.getAttributeValue(null, CONDITION_ATT_LINE1);
            String line2 = parser.getAttributeValue(null, CONDITION_ATT_LINE2);
            int icon = safeInt(parser, "icon", -1);
            int state = safeInt(parser, "state", -1);
            int flags = safeInt(parser, "flags", -1);
            try {
                return new Condition(id, summary, line1, line2, icon, state, flags);
            } catch (IllegalArgumentException e) {
                Slog.w(TAG, "Unable to read condition xml", e);
                return null;
            }
        }
        return null;
    }

    public static synchronized void writeConditionXml(Condition c, XmlSerializer out) throws IOException {
        out.attribute(null, "id", c.id.toString());
        out.attribute(null, "summary", c.summary);
        out.attribute(null, CONDITION_ATT_LINE1, c.line1);
        out.attribute(null, CONDITION_ATT_LINE2, c.line2);
        out.attribute(null, "icon", Integer.toString(c.icon));
        out.attribute(null, "state", Integer.toString(c.state));
        out.attribute(null, "flags", Integer.toString(c.flags));
    }

    public static synchronized boolean isValidHour(int val) {
        return val >= 0 && val < 24;
    }

    public static synchronized boolean isValidMinute(int val) {
        return val >= 0 && val < 60;
    }

    private static synchronized boolean isValidSource(int source) {
        return source >= 0 && source <= 2;
    }

    private static synchronized Boolean unsafeBoolean(XmlPullParser parser, String att) {
        String val = parser.getAttributeValue(null, att);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return Boolean.valueOf(Boolean.parseBoolean(val));
    }

    private static synchronized boolean safeBoolean(XmlPullParser parser, String att, boolean defValue) {
        String val = parser.getAttributeValue(null, att);
        return safeBoolean(val, defValue);
    }

    private static synchronized boolean safeBoolean(String val, boolean defValue) {
        return TextUtils.isEmpty(val) ? defValue : Boolean.parseBoolean(val);
    }

    private static synchronized int safeInt(XmlPullParser parser, String att, int defValue) {
        String val = parser.getAttributeValue(null, att);
        return tryParseInt(val, defValue);
    }

    private static synchronized ComponentName safeComponentName(XmlPullParser parser, String att) {
        String val = parser.getAttributeValue(null, att);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return ComponentName.unflattenFromString(val);
    }

    private static synchronized Uri safeUri(XmlPullParser parser, String att) {
        String val = parser.getAttributeValue(null, att);
        if (TextUtils.isEmpty(val)) {
            return null;
        }
        return Uri.parse(val);
    }

    private static synchronized long safeLong(XmlPullParser parser, String att, long defValue) {
        String val = parser.getAttributeValue(null, att);
        return tryParseLong(val, defValue);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public synchronized ZenModeConfig copy() {
        Parcel parcel = Parcel.obtain();
        try {
            writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            return new ZenModeConfig(parcel);
        } finally {
            parcel.recycle();
        }
    }

    public synchronized NotificationManager.Policy toNotificationPolicy() {
        int priorityCategories = 0;
        if (this.allowCalls) {
            priorityCategories = 0 | 8;
        }
        if (this.allowMessages) {
            priorityCategories |= 4;
        }
        if (this.allowEvents) {
            priorityCategories |= 2;
        }
        if (this.allowReminders) {
            priorityCategories |= 1;
        }
        if (this.allowRepeatCallers) {
            priorityCategories |= 16;
        }
        if (this.allowAlarms) {
            priorityCategories |= 32;
        }
        if (this.allowMedia) {
            priorityCategories |= 64;
        }
        if (this.allowSystem) {
            priorityCategories |= 128;
        }
        int priorityCallSenders = sourceToPrioritySenders(this.allowCallsFrom, 1);
        int priorityMessageSenders = sourceToPrioritySenders(this.allowMessagesFrom, 1);
        return new NotificationManager.Policy(priorityCategories, priorityCallSenders, priorityMessageSenders, this.suppressedVisualEffects, this.areChannelsBypassingDnd ? 1 : 0);
    }

    public static synchronized ScheduleCalendar toScheduleCalendar(Uri conditionId) {
        ScheduleInfo schedule = tryParseScheduleConditionId(conditionId);
        if (schedule == null || schedule.days == null || schedule.days.length == 0) {
            return null;
        }
        ScheduleCalendar sc = new ScheduleCalendar();
        sc.setSchedule(schedule);
        sc.setTimeZone(TimeZone.getDefault());
        return sc;
    }

    private static synchronized int sourceToPrioritySenders(int source, int def) {
        switch (source) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return def;
        }
    }

    private static synchronized int prioritySendersToSource(int prioritySenders, int def) {
        switch (prioritySenders) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return def;
        }
    }

    public synchronized void applyNotificationPolicy(NotificationManager.Policy policy) {
        if (policy == null) {
            return;
        }
        this.allowAlarms = (policy.priorityCategories & 32) != 0;
        this.allowMedia = (policy.priorityCategories & 64) != 0;
        this.allowSystem = (policy.priorityCategories & 128) != 0;
        this.allowEvents = (policy.priorityCategories & 2) != 0;
        this.allowReminders = (policy.priorityCategories & 1) != 0;
        this.allowCalls = (policy.priorityCategories & 8) != 0;
        this.allowMessages = (policy.priorityCategories & 4) != 0;
        this.allowRepeatCallers = (policy.priorityCategories & 16) != 0;
        this.allowCallsFrom = prioritySendersToSource(policy.priorityCallSenders, this.allowCallsFrom);
        this.allowMessagesFrom = prioritySendersToSource(policy.priorityMessageSenders, this.allowMessagesFrom);
        if (policy.suppressedVisualEffects != -1) {
            this.suppressedVisualEffects = policy.suppressedVisualEffects;
        }
        if (policy.state != -1) {
            this.areChannelsBypassingDnd = (policy.state & 1) != 0;
        }
    }

    public static synchronized Condition toTimeCondition(Context context, int minutesFromNow, int userHandle) {
        return toTimeCondition(context, minutesFromNow, userHandle, false);
    }

    public static synchronized Condition toTimeCondition(Context context, int minutesFromNow, int userHandle, boolean shortVersion) {
        long now = System.currentTimeMillis();
        long millis = minutesFromNow == 0 ? JobInfo.MIN_BACKOFF_MILLIS : 60000 * minutesFromNow;
        return toTimeCondition(context, now + millis, minutesFromNow, userHandle, shortVersion);
    }

    public static synchronized Condition toTimeCondition(Context context, long time, int minutes, int userHandle, boolean shortVersion) {
        String string;
        String line2;
        String line1;
        CharSequence formattedTime = getFormattedTime(context, time, isToday(time), userHandle);
        Resources res = context.getResources();
        if (minutes < 60) {
            int summaryResId = shortVersion ? R.plurals.zen_mode_duration_minutes_summary_short : R.plurals.zen_mode_duration_minutes_summary;
            string = res.getQuantityString(summaryResId, minutes, Integer.valueOf(minutes), formattedTime);
            int line1ResId = shortVersion ? R.plurals.zen_mode_duration_minutes_short : R.plurals.zen_mode_duration_minutes;
            line1 = res.getQuantityString(line1ResId, minutes, Integer.valueOf(minutes), formattedTime);
            line2 = res.getString(R.string.zen_mode_until, formattedTime);
        } else if (minutes >= 1440) {
            string = res.getString(R.string.zen_mode_until, formattedTime);
            line2 = string;
            line1 = string;
        } else {
            int num = Math.round(minutes / 60.0f);
            int summaryResId2 = shortVersion ? R.plurals.zen_mode_duration_hours_summary_short : R.plurals.zen_mode_duration_hours_summary;
            string = res.getQuantityString(summaryResId2, num, Integer.valueOf(num), formattedTime);
            int line1ResId2 = shortVersion ? R.plurals.zen_mode_duration_hours_short : R.plurals.zen_mode_duration_hours;
            line1 = res.getQuantityString(line1ResId2, num, Integer.valueOf(num), formattedTime);
            line2 = res.getString(R.string.zen_mode_until, formattedTime);
        }
        String summary = string;
        Uri id = toCountdownConditionId(time, false);
        return new Condition(id, summary, line1, line2, 0, 1, 1);
    }

    public static synchronized Condition toNextAlarmCondition(Context context, long alarm, int userHandle) {
        boolean isSameDay = isToday(alarm);
        CharSequence formattedTime = getFormattedTime(context, alarm, isSameDay, userHandle);
        Resources res = context.getResources();
        String line1 = res.getString(R.string.zen_mode_until, formattedTime);
        Uri id = toCountdownConditionId(alarm, true);
        return new Condition(id, "", line1, "", 0, 1, 1);
    }

    public static synchronized CharSequence getFormattedTime(Context context, long time, boolean isSameDay, int userHandle) {
        StringBuilder sb = new StringBuilder();
        sb.append(!isSameDay ? "EEE " : "");
        sb.append(DateFormat.is24HourFormat(context, userHandle) ? "Hm" : "hma");
        String skeleton = sb.toString();
        String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), skeleton);
        return DateFormat.format(pattern, time);
    }

    public static synchronized boolean isToday(long time) {
        GregorianCalendar now = new GregorianCalendar();
        GregorianCalendar endTime = new GregorianCalendar();
        endTime.setTimeInMillis(time);
        if (now.get(1) == endTime.get(1) && now.get(2) == endTime.get(2) && now.get(5) == endTime.get(5)) {
            return true;
        }
        return false;
    }

    public static synchronized Uri toCountdownConditionId(long time, boolean alarm) {
        return new Uri.Builder().scheme(Condition.SCHEME).authority(SYSTEM_AUTHORITY).appendPath(COUNTDOWN_PATH).appendPath(Long.toString(time)).appendPath("alarm").appendPath(Boolean.toString(alarm)).build();
    }

    public static synchronized long tryParseCountdownConditionId(Uri conditionId) {
        if (Condition.isValidId(conditionId, SYSTEM_AUTHORITY) && conditionId.getPathSegments().size() >= 2 && COUNTDOWN_PATH.equals(conditionId.getPathSegments().get(0))) {
            try {
                return Long.parseLong(conditionId.getPathSegments().get(1));
            } catch (RuntimeException e) {
                String str = TAG;
                Slog.w(str, "Error parsing countdown condition: " + conditionId, e);
                return 0L;
            }
        }
        return 0L;
    }

    public static synchronized boolean isValidCountdownConditionId(Uri conditionId) {
        return tryParseCountdownConditionId(conditionId) != 0;
    }

    public static synchronized boolean isValidCountdownToAlarmConditionId(Uri conditionId) {
        if (tryParseCountdownConditionId(conditionId) == 0 || conditionId.getPathSegments().size() < 4 || !"alarm".equals(conditionId.getPathSegments().get(2))) {
            return false;
        }
        try {
            return Boolean.parseBoolean(conditionId.getPathSegments().get(3));
        } catch (RuntimeException e) {
            String str = TAG;
            Slog.w(str, "Error parsing countdown alarm condition: " + conditionId, e);
            return false;
        }
    }

    public static synchronized Uri toScheduleConditionId(ScheduleInfo schedule) {
        Uri.Builder appendQueryParameter = new Uri.Builder().scheme(Condition.SCHEME).authority(SYSTEM_AUTHORITY).appendPath(SCHEDULE_PATH).appendQueryParameter("days", toDayList(schedule.days));
        Uri.Builder appendQueryParameter2 = appendQueryParameter.appendQueryParameter(Telephony.BaseMmsColumns.START, schedule.startHour + "." + schedule.startMinute);
        return appendQueryParameter2.appendQueryParameter("end", schedule.endHour + "." + schedule.endMinute).appendQueryParameter("exitAtAlarm", String.valueOf(schedule.exitAtAlarm)).build();
    }

    public static synchronized boolean isValidScheduleConditionId(Uri conditionId) {
        try {
            ScheduleInfo info = tryParseScheduleConditionId(conditionId);
            if (info == null || info.days == null || info.days.length == 0) {
                return false;
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
    }

    private protected static ScheduleInfo tryParseScheduleConditionId(Uri conditionId) {
        boolean isSchedule = conditionId != null && Condition.SCHEME.equals(conditionId.getScheme()) && SYSTEM_AUTHORITY.equals(conditionId.getAuthority()) && conditionId.getPathSegments().size() == 1 && SCHEDULE_PATH.equals(conditionId.getPathSegments().get(0));
        if (!isSchedule) {
            return null;
        }
        int[] start = tryParseHourAndMinute(conditionId.getQueryParameter(Telephony.BaseMmsColumns.START));
        int[] end = tryParseHourAndMinute(conditionId.getQueryParameter("end"));
        if (start == null || end == null) {
            return null;
        }
        ScheduleInfo rt = new ScheduleInfo();
        rt.days = tryParseDayList(conditionId.getQueryParameter("days"), "\\.");
        rt.startHour = start[0];
        rt.startMinute = start[1];
        rt.endHour = end[0];
        rt.endMinute = end[1];
        rt.exitAtAlarm = safeBoolean(conditionId.getQueryParameter("exitAtAlarm"), false);
        return rt;
    }

    public static synchronized ComponentName getScheduleConditionProvider() {
        return new ComponentName(SYSTEM_AUTHORITY, "ScheduleConditionProvider");
    }

    /* loaded from: classes2.dex */
    public static class ScheduleInfo {
        private protected int[] days;
        private protected int endHour;
        private protected int endMinute;
        public boolean exitAtAlarm;
        public long nextAlarm;
        private protected int startHour;
        private protected int startMinute;

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o) {
            if (o instanceof ScheduleInfo) {
                ScheduleInfo other = (ScheduleInfo) o;
                return ZenModeConfig.toDayList(this.days).equals(ZenModeConfig.toDayList(other.days)) && this.startHour == other.startHour && this.startMinute == other.startMinute && this.endHour == other.endHour && this.endMinute == other.endMinute && this.exitAtAlarm == other.exitAtAlarm;
            }
            return false;
        }

        public synchronized ScheduleInfo copy() {
            ScheduleInfo rt = new ScheduleInfo();
            if (this.days != null) {
                rt.days = new int[this.days.length];
                System.arraycopy(this.days, 0, rt.days, 0, this.days.length);
            }
            rt.startHour = this.startHour;
            rt.startMinute = this.startMinute;
            rt.endHour = this.endHour;
            rt.endMinute = this.endMinute;
            rt.exitAtAlarm = this.exitAtAlarm;
            rt.nextAlarm = this.nextAlarm;
            return rt;
        }

        public String toString() {
            return "ScheduleInfo{days=" + Arrays.toString(this.days) + ", startHour=" + this.startHour + ", startMinute=" + this.startMinute + ", endHour=" + this.endHour + ", endMinute=" + this.endMinute + ", exitAtAlarm=" + this.exitAtAlarm + ", nextAlarm=" + ts(this.nextAlarm) + '}';
        }

        protected static synchronized String ts(long time) {
            return new Date(time) + " (" + time + ")";
        }
    }

    public static synchronized Uri toEventConditionId(EventInfo event) {
        return new Uri.Builder().scheme(Condition.SCHEME).authority(SYSTEM_AUTHORITY).appendPath("event").appendQueryParameter("userId", Long.toString(event.userId)).appendQueryParameter("calendar", event.calendar != null ? event.calendar : "").appendQueryParameter("reply", Integer.toString(event.reply)).build();
    }

    public static synchronized boolean isValidEventConditionId(Uri conditionId) {
        return tryParseEventConditionId(conditionId) != null;
    }

    public static synchronized EventInfo tryParseEventConditionId(Uri conditionId) {
        boolean isEvent = true;
        if (conditionId == null || !Condition.SCHEME.equals(conditionId.getScheme()) || !SYSTEM_AUTHORITY.equals(conditionId.getAuthority()) || conditionId.getPathSegments().size() != 1 || !"event".equals(conditionId.getPathSegments().get(0))) {
            isEvent = false;
        }
        if (!isEvent) {
            return null;
        }
        EventInfo rt = new EventInfo();
        rt.userId = tryParseInt(conditionId.getQueryParameter("userId"), UserInfo.NO_PROFILE_GROUP_ID);
        rt.calendar = conditionId.getQueryParameter("calendar");
        if (TextUtils.isEmpty(rt.calendar) || tryParseLong(rt.calendar, -1L) != -1) {
            rt.calendar = null;
        }
        rt.reply = tryParseInt(conditionId.getQueryParameter("reply"), 0);
        return rt;
    }

    public static synchronized ComponentName getEventConditionProvider() {
        return new ComponentName(SYSTEM_AUTHORITY, "EventConditionProvider");
    }

    /* loaded from: classes2.dex */
    public static class EventInfo {
        public static final int REPLY_ANY_EXCEPT_NO = 0;
        public static final int REPLY_YES = 2;
        public static final int REPLY_YES_OR_MAYBE = 1;
        public String calendar;
        public int reply;
        public int userId = UserInfo.NO_PROFILE_GROUP_ID;

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o) {
            if (o instanceof EventInfo) {
                EventInfo other = (EventInfo) o;
                return this.userId == other.userId && Objects.equals(this.calendar, other.calendar) && this.reply == other.reply;
            }
            return false;
        }

        public synchronized EventInfo copy() {
            EventInfo rt = new EventInfo();
            rt.userId = this.userId;
            rt.calendar = this.calendar;
            rt.reply = this.reply;
            return rt;
        }

        public static synchronized int resolveUserId(int userId) {
            return userId == -10000 ? ActivityManager.getCurrentUser() : userId;
        }
    }

    private static synchronized int[] tryParseHourAndMinute(String value) {
        int i;
        if (!TextUtils.isEmpty(value) && (i = value.indexOf(46)) >= 1 && i < value.length() - 1) {
            int hour = tryParseInt(value.substring(0, i), -1);
            int minute = tryParseInt(value.substring(i + 1), -1);
            if (isValidHour(hour) && isValidMinute(minute)) {
                return new int[]{hour, minute};
            }
            return null;
        }
        return null;
    }

    private static synchronized int tryParseZenMode(String value, int defValue) {
        int rt = tryParseInt(value, defValue);
        return Settings.Global.isValidZenMode(rt) ? rt : defValue;
    }

    public static synchronized String newRuleId() {
        return UUID.randomUUID().toString().replace(NativeLibraryHelper.CLEAR_ABI_OVERRIDE, "");
    }

    public static synchronized String getOwnerCaption(Context context, String owner) {
        CharSequence seq;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(owner, 0);
            if (info != null && (seq = info.loadLabel(pm)) != null) {
                String str = seq.toString().trim();
                if (str.length() > 0) {
                    return str;
                }
                return "";
            }
            return "";
        } catch (Throwable e) {
            Slog.w(TAG, "Error loading owner caption", e);
            return "";
        }
    }

    public static synchronized String getConditionSummary(Context context, ZenModeConfig config, int userHandle, boolean shortVersion) {
        return getConditionLine(context, config, userHandle, false, shortVersion);
    }

    private static synchronized String getConditionLine(Context context, ZenModeConfig config, int userHandle, boolean useLine1, boolean shortVersion) {
        if (config == null) {
            return "";
        }
        String summary = "";
        if (config.manualRule != null) {
            Uri id = config.manualRule.conditionId;
            if (config.manualRule.enabler != null) {
                summary = getOwnerCaption(context, config.manualRule.enabler);
            } else if (id == null) {
                summary = context.getString(R.string.zen_mode_forever);
            } else {
                long time = tryParseCountdownConditionId(id);
                Condition c = config.manualRule.condition;
                if (time > 0) {
                    long now = System.currentTimeMillis();
                    long span = time - now;
                    c = toTimeCondition(context, time, Math.round(((float) span) / 60000.0f), userHandle, shortVersion);
                }
                String rt = c == null ? "" : useLine1 ? c.line1 : c.summary;
                summary = TextUtils.isEmpty(rt) ? "" : rt;
            }
        }
        for (ZenRule automaticRule : config.automaticRules.values()) {
            if (automaticRule.isAutomaticActive()) {
                if (summary.isEmpty()) {
                    String summary2 = automaticRule.name;
                    summary = summary2;
                } else {
                    summary = context.getResources().getString(R.string.zen_mode_rule_name_combination, summary, automaticRule.name);
                }
            }
        }
        return summary;
    }

    /* loaded from: classes2.dex */
    public static class ZenRule implements Parcelable {
        public static final Parcelable.Creator<ZenRule> CREATOR = new Parcelable.Creator<ZenRule>() { // from class: android.service.notification.ZenModeConfig.ZenRule.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ZenRule createFromParcel(Parcel source) {
                return new ZenRule(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ZenRule[] newArray(int size) {
                return new ZenRule[size];
            }
        };
        public ComponentName component;
        public Condition condition;
        private protected Uri conditionId;
        private protected long creationTime;
        private protected boolean enabled;
        public String enabler;
        public String id;
        private protected String name;
        private protected boolean snoozing;
        private protected int zenMode;

        public synchronized ZenRule() {
        }

        public synchronized ZenRule(Parcel source) {
            this.enabled = source.readInt() == 1;
            this.snoozing = source.readInt() == 1;
            if (source.readInt() == 1) {
                this.name = source.readString();
            }
            this.zenMode = source.readInt();
            this.conditionId = (Uri) source.readParcelable(null);
            this.condition = (Condition) source.readParcelable(null);
            this.component = (ComponentName) source.readParcelable(null);
            if (source.readInt() == 1) {
                this.id = source.readString();
            }
            this.creationTime = source.readLong();
            if (source.readInt() == 1) {
                this.enabler = source.readString();
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.enabled ? 1 : 0);
            dest.writeInt(this.snoozing ? 1 : 0);
            if (this.name != null) {
                dest.writeInt(1);
                dest.writeString(this.name);
            } else {
                dest.writeInt(0);
            }
            dest.writeInt(this.zenMode);
            dest.writeParcelable(this.conditionId, 0);
            dest.writeParcelable(this.condition, 0);
            dest.writeParcelable(this.component, 0);
            if (this.id != null) {
                dest.writeInt(1);
                dest.writeString(this.id);
            } else {
                dest.writeInt(0);
            }
            dest.writeLong(this.creationTime);
            if (this.enabler != null) {
                dest.writeInt(1);
                dest.writeString(this.enabler);
                return;
            }
            dest.writeInt(0);
        }

        public String toString() {
            return ZenRule.class.getSimpleName() + "[id=" + this.id + ",enabled=" + String.valueOf(this.enabled).toUpperCase() + ",snoozing=" + this.snoozing + ",name=" + this.name + ",zenMode=" + Settings.Global.zenModeToString(this.zenMode) + ",conditionId=" + this.conditionId + ",condition=" + this.condition + ",component=" + this.component + ",creationTime=" + this.creationTime + ",enabler=" + this.enabler + ']';
        }

        public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1138166333441L, this.id);
            proto.write(1138166333442L, this.name);
            proto.write(1112396529667L, this.creationTime);
            proto.write(1133871366148L, this.enabled);
            proto.write(1138166333445L, this.enabler);
            proto.write(1133871366150L, this.snoozing);
            proto.write(1159641169927L, this.zenMode);
            if (this.conditionId != null) {
                proto.write(1138166333448L, this.conditionId.toString());
            }
            if (this.condition != null) {
                this.condition.writeToProto(proto, 1146756268041L);
            }
            if (this.component != null) {
                this.component.writeToProto(proto, 1146756268042L);
            }
            proto.end(token);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized void appendDiff(Diff d, String item, ZenRule from, ZenRule to) {
            if (d == null) {
                return;
            }
            if (from == null) {
                if (to == null) {
                    return;
                }
                d.addLine(item, "insert");
                return;
            }
            from.appendDiff(d, item, to);
        }

        private synchronized void appendDiff(Diff d, String item, ZenRule to) {
            if (to != null) {
                if (this.enabled != to.enabled) {
                    d.addLine(item, "enabled", Boolean.valueOf(this.enabled), Boolean.valueOf(to.enabled));
                }
                if (this.snoozing != to.snoozing) {
                    d.addLine(item, ZenModeConfig.RULE_ATT_SNOOZING, Boolean.valueOf(this.snoozing), Boolean.valueOf(to.snoozing));
                }
                if (!Objects.equals(this.name, to.name)) {
                    d.addLine(item, "name", this.name, to.name);
                }
                if (this.zenMode != to.zenMode) {
                    d.addLine(item, "zenMode", Integer.valueOf(this.zenMode), Integer.valueOf(to.zenMode));
                }
                if (!Objects.equals(this.conditionId, to.conditionId)) {
                    d.addLine(item, ZenModeConfig.RULE_ATT_CONDITION_ID, this.conditionId, to.conditionId);
                }
                if (!Objects.equals(this.condition, to.condition)) {
                    d.addLine(item, Condition.SCHEME, this.condition, to.condition);
                }
                if (!Objects.equals(this.component, to.component)) {
                    d.addLine(item, "component", this.component, to.component);
                }
                if (!Objects.equals(this.id, to.id)) {
                    d.addLine(item, "id", this.id, to.id);
                }
                if (this.creationTime != to.creationTime) {
                    d.addLine(item, "creationTime", Long.valueOf(this.creationTime), Long.valueOf(to.creationTime));
                }
                if (this.enabler != to.enabler) {
                    d.addLine(item, ZenModeConfig.RULE_ATT_ENABLER, this.enabler, to.enabler);
                    return;
                }
                return;
            }
            d.addLine(item, "delete");
        }

        public boolean equals(Object o) {
            if (o instanceof ZenRule) {
                if (o == this) {
                    return true;
                }
                ZenRule other = (ZenRule) o;
                return other.enabled == this.enabled && other.snoozing == this.snoozing && Objects.equals(other.name, this.name) && other.zenMode == this.zenMode && Objects.equals(other.conditionId, this.conditionId) && Objects.equals(other.condition, this.condition) && Objects.equals(other.component, this.component) && Objects.equals(other.id, this.id) && other.creationTime == this.creationTime && Objects.equals(other.enabler, this.enabler);
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Boolean.valueOf(this.enabled), Boolean.valueOf(this.snoozing), this.name, Integer.valueOf(this.zenMode), this.conditionId, this.condition, this.component, this.id, Long.valueOf(this.creationTime), this.enabler);
        }

        public synchronized boolean isAutomaticActive() {
            return this.enabled && !this.snoozing && this.component != null && isTrueOrUnknown();
        }

        public synchronized boolean isTrueOrUnknown() {
            return this.condition != null && (this.condition.state == 1 || this.condition.state == 2);
        }
    }

    /* loaded from: classes2.dex */
    public static class Diff {
        private final ArrayList<String> lines = new ArrayList<>();

        public String toString() {
            StringBuilder sb = new StringBuilder("Diff[");
            int N = this.lines.size();
            for (int i = 0; i < N; i++) {
                if (i > 0) {
                    sb.append(",\n");
                }
                sb.append(this.lines.get(i));
            }
            sb.append(']');
            return sb.toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Diff addLine(String item, String action) {
            ArrayList<String> arrayList = this.lines;
            arrayList.add(item + SettingsStringUtil.DELIMITER + action);
            return this;
        }

        public synchronized Diff addLine(String item, String subitem, Object from, Object to) {
            return addLine(item + "." + subitem, from, to);
        }

        public synchronized Diff addLine(String item, Object from, Object to) {
            return addLine(item, from + "->" + to);
        }
    }

    public static synchronized boolean areAllPriorityOnlyNotificationZenSoundsMuted(NotificationManager.Policy policy) {
        boolean allowReminders = (policy.priorityCategories & 1) != 0;
        boolean allowCalls = (policy.priorityCategories & 8) != 0;
        boolean allowMessages = (policy.priorityCategories & 4) != 0;
        boolean allowEvents = (policy.priorityCategories & 2) != 0;
        boolean allowRepeatCallers = (policy.priorityCategories & 16) != 0;
        boolean areChannelsBypassingDnd = (policy.state & 1) != 0;
        return (allowReminders || allowCalls || allowMessages || allowEvents || allowRepeatCallers || areChannelsBypassingDnd) ? false : true;
    }

    public static synchronized boolean isZenOverridingRinger(int zen, ZenModeConfig zenConfig) {
        if (zen == 2 || zen == 3) {
            return true;
        }
        return zen == 1 && areAllPriorityOnlyNotificationZenSoundsMuted(zenConfig);
    }

    public static synchronized boolean areAllPriorityOnlyNotificationZenSoundsMuted(ZenModeConfig config) {
        return (config.allowReminders || config.allowCalls || config.allowMessages || config.allowEvents || config.allowRepeatCallers || config.areChannelsBypassingDnd) ? false : true;
    }

    public static synchronized boolean areAllZenBehaviorSoundsMuted(ZenModeConfig config) {
        return (config.allowAlarms || config.allowMedia || config.allowSystem || !areAllPriorityOnlyNotificationZenSoundsMuted(config)) ? false : true;
    }

    public static synchronized String getDescription(Context context, boolean zenOn, ZenModeConfig config, boolean describeForeverCondition) {
        if (!zenOn || config == null) {
            return null;
        }
        String secondaryText = "";
        long latestEndTime = -1;
        if (config.manualRule != null) {
            Uri id = config.manualRule.conditionId;
            if (config.manualRule.enabler != null) {
                String appName = getOwnerCaption(context, config.manualRule.enabler);
                if (!appName.isEmpty()) {
                    secondaryText = appName;
                }
            } else if (id == null) {
                if (!describeForeverCondition) {
                    return null;
                }
                return context.getString(R.string.zen_mode_forever);
            } else {
                latestEndTime = tryParseCountdownConditionId(id);
                if (latestEndTime > 0) {
                    CharSequence formattedTime = getFormattedTime(context, latestEndTime, isToday(latestEndTime), context.getUserId());
                    secondaryText = context.getString(R.string.zen_mode_until, formattedTime);
                }
            }
        }
        for (ZenRule automaticRule : config.automaticRules.values()) {
            if (automaticRule.isAutomaticActive()) {
                if (isValidEventConditionId(automaticRule.conditionId) || isValidScheduleConditionId(automaticRule.conditionId)) {
                    long endTime = parseAutomaticRuleEndTime(context, automaticRule.conditionId);
                    if (endTime > latestEndTime) {
                        latestEndTime = endTime;
                        secondaryText = automaticRule.name;
                    }
                } else {
                    return automaticRule.name;
                }
            }
        }
        if (secondaryText.equals("")) {
            return null;
        }
        return secondaryText;
    }

    private static synchronized long parseAutomaticRuleEndTime(Context context, Uri id) {
        if (isValidEventConditionId(id)) {
            return Long.MAX_VALUE;
        }
        if (isValidScheduleConditionId(id)) {
            ScheduleCalendar schedule = toScheduleCalendar(id);
            long endTimeMs = schedule.getNextChangeTime(System.currentTimeMillis());
            if (schedule.exitAtAlarm()) {
                long nextAlarm = getNextAlarm(context);
                schedule.maybeSetNextAlarm(System.currentTimeMillis(), nextAlarm);
                if (schedule.shouldExitForAlarm(endTimeMs)) {
                    return nextAlarm;
                }
            }
            return endTimeMs;
        }
        return -1L;
    }

    private static synchronized long getNextAlarm(Context context) {
        AlarmManager alarms = (AlarmManager) context.getSystemService("alarm");
        AlarmManager.AlarmClockInfo info = alarms.getNextAlarmClock(context.getUserId());
        if (info != null) {
            return info.getTriggerTime();
        }
        return 0L;
    }
}
