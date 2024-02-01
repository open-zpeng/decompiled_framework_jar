package android.provider;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsConferenceState;
import android.text.TextUtils;
import android.util.Patterns;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.SmsApplication;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes2.dex */
public final class Telephony {
    private static final String TAG = "Telephony";

    /* loaded from: classes2.dex */
    public interface BaseMmsColumns extends BaseColumns {
        @Deprecated
        public static final String ADAPTATION_ALLOWED = "adp_a";
        @Deprecated
        public static final String APPLIC_ID = "apl_id";
        @Deprecated
        public static final String AUX_APPLIC_ID = "aux_apl_id";
        @Deprecated
        public static final String CANCEL_ID = "cl_id";
        @Deprecated
        public static final String CANCEL_STATUS = "cl_st";
        public static final String CONTENT_CLASS = "ct_cls";
        public static final String CONTENT_LOCATION = "ct_l";
        public static final String CONTENT_TYPE = "ct_t";
        public static final String CREATOR = "creator";
        public static final String DATE = "date";
        public static final String DATE_SENT = "date_sent";
        public static final String DELIVERY_REPORT = "d_rpt";
        public static final String DELIVERY_TIME = "d_tm";
        @Deprecated
        public static final String DELIVERY_TIME_TOKEN = "d_tm_tok";
        @Deprecated
        public static final String DISTRIBUTION_INDICATOR = "d_ind";
        @Deprecated
        public static final String DRM_CONTENT = "drm_c";
        @Deprecated
        public static final String ELEMENT_DESCRIPTOR = "e_des";
        public static final String EXPIRY = "exp";
        @Deprecated
        public static final String LIMIT = "limit";
        public static final String LOCKED = "locked";
        @Deprecated
        public static final String MBOX_QUOTAS = "mb_qt";
        @Deprecated
        public static final String MBOX_QUOTAS_TOKEN = "mb_qt_tok";
        @Deprecated
        public static final String MBOX_TOTALS = "mb_t";
        @Deprecated
        public static final String MBOX_TOTALS_TOKEN = "mb_t_tok";
        public static final String MESSAGE_BOX = "msg_box";
        public static final int MESSAGE_BOX_ALL = 0;
        public static final int MESSAGE_BOX_DRAFTS = 3;
        public static final int MESSAGE_BOX_FAILED = 5;
        public static final int MESSAGE_BOX_INBOX = 1;
        public static final int MESSAGE_BOX_OUTBOX = 4;
        public static final int MESSAGE_BOX_SENT = 2;
        public static final String MESSAGE_CLASS = "m_cls";
        @Deprecated
        public static final String MESSAGE_COUNT = "m_cnt";
        public static final String MESSAGE_ID = "m_id";
        public static final String MESSAGE_SIZE = "m_size";
        public static final String MESSAGE_TYPE = "m_type";
        public static final String MMS_VERSION = "v";
        @Deprecated
        public static final String MM_FLAGS = "mm_flg";
        @Deprecated
        public static final String MM_FLAGS_TOKEN = "mm_flg_tok";
        @Deprecated
        public static final String MM_STATE = "mm_st";
        @Deprecated
        public static final String PREVIOUSLY_SENT_BY = "p_s_by";
        @Deprecated
        public static final String PREVIOUSLY_SENT_DATE = "p_s_d";
        public static final String PRIORITY = "pri";
        @Deprecated
        public static final String QUOTAS = "qt";
        public static final String READ = "read";
        public static final String READ_REPORT = "rr";
        public static final String READ_STATUS = "read_status";
        @Deprecated
        public static final String RECOMMENDED_RETRIEVAL_MODE = "r_r_mod";
        @Deprecated
        public static final String RECOMMENDED_RETRIEVAL_MODE_TEXT = "r_r_mod_txt";
        @Deprecated
        public static final String REPLACE_ID = "repl_id";
        @Deprecated
        public static final String REPLY_APPLIC_ID = "r_apl_id";
        @Deprecated
        public static final String REPLY_CHARGING = "r_chg";
        @Deprecated
        public static final String REPLY_CHARGING_DEADLINE = "r_chg_dl";
        @Deprecated
        public static final String REPLY_CHARGING_DEADLINE_TOKEN = "r_chg_dl_tok";
        @Deprecated
        public static final String REPLY_CHARGING_ID = "r_chg_id";
        @Deprecated
        public static final String REPLY_CHARGING_SIZE = "r_chg_sz";
        public static final String REPORT_ALLOWED = "rpt_a";
        public static final String RESPONSE_STATUS = "resp_st";
        public static final String RESPONSE_TEXT = "resp_txt";
        public static final String RETRIEVE_STATUS = "retr_st";
        public static final String RETRIEVE_TEXT = "retr_txt";
        public static final String RETRIEVE_TEXT_CHARSET = "retr_txt_cs";
        public static final String SEEN = "seen";
        @Deprecated
        public static final String SENDER_VISIBILITY = "s_vis";
        @Deprecated
        public static final String START = "start";
        public static final String STATUS = "st";
        @Deprecated
        public static final String STATUS_TEXT = "st_txt";
        @Deprecated
        public static final String STORE = "store";
        @Deprecated
        public static final String STORED = "stored";
        @Deprecated
        public static final String STORE_STATUS = "store_st";
        @Deprecated
        public static final String STORE_STATUS_TEXT = "store_st_txt";
        public static final String SUBJECT = "sub";
        public static final String SUBJECT_CHARSET = "sub_cs";
        public static final String SUBSCRIPTION_ID = "sub_id";
        public static final String TEXT_ONLY = "text_only";
        public static final String THREAD_ID = "thread_id";
        @Deprecated
        public static final String TOTALS = "totals";
        public static final String TRANSACTION_ID = "tr_id";
    }

    /* loaded from: classes2.dex */
    public interface CanonicalAddressesColumns extends BaseColumns {
        public static final String ADDRESS = "address";
    }

    /* loaded from: classes2.dex */
    public interface CarrierColumns extends BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://carrier_information/carrier");
        public static final String EXPIRATION_TIME = "expiration_time";
        public static final String KEY_IDENTIFIER = "key_identifier";
        public static final String KEY_TYPE = "key_type";
        public static final String LAST_MODIFIED = "last_modified";
        public static final String MCC = "mcc";
        public static final String MNC = "mnc";
        public static final String MVNO_MATCH_DATA = "mvno_match_data";
        public static final String MVNO_TYPE = "mvno_type";
        public static final String PUBLIC_KEY = "public_key";
    }

    /* loaded from: classes2.dex */
    public interface TextBasedSmsColumns {
        public static final String ADDRESS = "address";
        public static final String BODY = "body";
        public static final String CREATOR = "creator";
        public static final String DATE = "date";
        public static final String DATE_SENT = "date_sent";
        public static final String ERROR_CODE = "error_code";
        public static final String LOCKED = "locked";
        public static final int MESSAGE_TYPE_ALL = 0;
        public static final int MESSAGE_TYPE_DRAFT = 3;
        public static final int MESSAGE_TYPE_FAILED = 5;
        public static final int MESSAGE_TYPE_INBOX = 1;
        public static final int MESSAGE_TYPE_OUTBOX = 4;
        public static final int MESSAGE_TYPE_QUEUED = 6;
        public static final int MESSAGE_TYPE_SENT = 2;
        public static final String MTU = "mtu";
        public static final String PERSON = "person";
        public static final String PROTOCOL = "protocol";
        public static final String READ = "read";
        public static final String REPLY_PATH_PRESENT = "reply_path_present";
        public static final String SEEN = "seen";
        public static final String SERVICE_CENTER = "service_center";
        public static final String STATUS = "status";
        public static final int STATUS_COMPLETE = 0;
        public static final int STATUS_FAILED = 64;
        public static final int STATUS_NONE = -1;
        public static final int STATUS_PENDING = 32;
        public static final String SUBJECT = "subject";
        public static final String SUBSCRIPTION_ID = "sub_id";
        public static final String THREAD_ID = "thread_id";
        public static final String TYPE = "type";
    }

    /* loaded from: classes2.dex */
    public interface ThreadsColumns extends BaseColumns {
        public static final String ARCHIVED = "archived";
        public static final String DATE = "date";
        public static final String ERROR = "error";
        public static final String HAS_ATTACHMENT = "has_attachment";
        public static final String MESSAGE_COUNT = "message_count";
        public static final String READ = "read";
        public static final String RECIPIENT_IDS = "recipient_ids";
        public static final String SNIPPET = "snippet";
        public static final String SNIPPET_CHARSET = "snippet_cs";
        public static final String TYPE = "type";
    }

    private synchronized Telephony() {
    }

    /* loaded from: classes2.dex */
    public static final class Sms implements BaseColumns, TextBasedSmsColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://sms");
        public static final String DEFAULT_SORT_ORDER = "date DESC";

        private synchronized Sms() {
        }

        public static String getDefaultSmsPackage(Context context) {
            ComponentName component = SmsApplication.getDefaultSmsApplication(context, false);
            if (component != null) {
                return component.getPackageName();
            }
            return null;
        }

        public static synchronized Cursor query(ContentResolver cr, String[] projection) {
            return cr.query(CONTENT_URI, projection, null, null, "date DESC");
        }

        private protected static Cursor query(ContentResolver cr, String[] projection, String where, String orderBy) {
            return cr.query(CONTENT_URI, projection, where, null, orderBy == null ? "date DESC" : orderBy);
        }

        private protected static Uri addMessageToUri(ContentResolver resolver, Uri uri, String address, String body, String subject, Long date, boolean read, boolean deliveryReport) {
            return addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, uri, address, body, subject, date, read, deliveryReport, -1L);
        }

        private protected static Uri addMessageToUri(int subId, ContentResolver resolver, Uri uri, String address, String body, String subject, Long date, boolean read, boolean deliveryReport) {
            return addMessageToUri(subId, resolver, uri, address, body, subject, date, read, deliveryReport, -1L);
        }

        private protected static Uri addMessageToUri(ContentResolver resolver, Uri uri, String address, String body, String subject, Long date, boolean read, boolean deliveryReport, long threadId) {
            return addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, uri, address, body, subject, date, read, deliveryReport, threadId);
        }

        private protected static Uri addMessageToUri(int subId, ContentResolver resolver, Uri uri, String address, String body, String subject, Long date, boolean read, boolean deliveryReport, long threadId) {
            ContentValues values = new ContentValues(8);
            Rlog.v(Telephony.TAG, "Telephony addMessageToUri sub id: " + subId);
            values.put("sub_id", Integer.valueOf(subId));
            values.put("address", address);
            if (date != null) {
                values.put("date", date);
            }
            values.put("read", Integer.valueOf(read ? 1 : 0));
            values.put(TextBasedSmsColumns.SUBJECT, subject);
            values.put("body", body);
            if (deliveryReport) {
                values.put("status", (Integer) 32);
            }
            if (threadId != -1) {
                values.put("thread_id", Long.valueOf(threadId));
            }
            return resolver.insert(uri, values);
        }

        private protected static boolean moveMessageToFolder(Context context, Uri uri, int folder, int error) {
            if (uri == null) {
                return false;
            }
            boolean markAsUnread = false;
            boolean markAsRead = false;
            switch (folder) {
                case 1:
                case 3:
                    break;
                case 2:
                case 4:
                    markAsRead = true;
                    break;
                case 5:
                case 6:
                    markAsUnread = true;
                    break;
                default:
                    return false;
            }
            ContentValues values = new ContentValues(3);
            values.put("type", Integer.valueOf(folder));
            if (markAsUnread) {
                values.put("read", (Integer) 0);
            } else if (markAsRead) {
                values.put("read", (Integer) 1);
            }
            values.put(TextBasedSmsColumns.ERROR_CODE, Integer.valueOf(error));
            if (1 != SqliteWrapper.update(context, context.getContentResolver(), uri, values, null, null)) {
                return false;
            }
            return true;
        }

        private protected static boolean isOutgoingFolder(int messageType) {
            return messageType == 5 || messageType == 4 || messageType == 2 || messageType == 6;
        }

        /* loaded from: classes2.dex */
        public static final class Inbox implements BaseColumns, TextBasedSmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://sms/inbox");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private synchronized Inbox() {
            }

            private protected static Uri addMessage(ContentResolver resolver, String address, String body, String subject, Long date, boolean read) {
                return Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, CONTENT_URI, address, body, subject, date, read, false);
            }

            private protected static Uri addMessage(int subId, ContentResolver resolver, String address, String body, String subject, Long date, boolean read) {
                return Sms.addMessageToUri(subId, resolver, CONTENT_URI, address, body, subject, date, read, false);
            }
        }

        /* loaded from: classes2.dex */
        public static final class Sent implements BaseColumns, TextBasedSmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://sms/sent");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private synchronized Sent() {
            }

            private protected static Uri addMessage(ContentResolver resolver, String address, String body, String subject, Long date) {
                return Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, CONTENT_URI, address, body, subject, date, true, false);
            }

            private protected static Uri addMessage(int subId, ContentResolver resolver, String address, String body, String subject, Long date) {
                return Sms.addMessageToUri(subId, resolver, CONTENT_URI, address, body, subject, date, true, false);
            }
        }

        /* loaded from: classes2.dex */
        public static final class Draft implements BaseColumns, TextBasedSmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://sms/draft");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private synchronized Draft() {
            }

            private protected static Uri addMessage(ContentResolver resolver, String address, String body, String subject, Long date) {
                return Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, CONTENT_URI, address, body, subject, date, true, false);
            }

            private protected static Uri addMessage(int subId, ContentResolver resolver, String address, String body, String subject, Long date) {
                return Sms.addMessageToUri(subId, resolver, CONTENT_URI, address, body, subject, date, true, false);
            }
        }

        /* loaded from: classes2.dex */
        public static final class Outbox implements BaseColumns, TextBasedSmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://sms/outbox");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private synchronized Outbox() {
            }

            private protected static Uri addMessage(ContentResolver resolver, String address, String body, String subject, Long date, boolean deliveryReport, long threadId) {
                return Sms.addMessageToUri(SubscriptionManager.getDefaultSmsSubscriptionId(), resolver, CONTENT_URI, address, body, subject, date, true, deliveryReport, threadId);
            }

            public static synchronized Uri addMessage(int subId, ContentResolver resolver, String address, String body, String subject, Long date, boolean deliveryReport, long threadId) {
                return Sms.addMessageToUri(subId, resolver, CONTENT_URI, address, body, subject, date, true, deliveryReport, threadId);
            }
        }

        /* loaded from: classes2.dex */
        public static final class Conversations implements BaseColumns, TextBasedSmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://sms/conversations");
            public static final String DEFAULT_SORT_ORDER = "date DESC";
            public static final String MESSAGE_COUNT = "msg_count";
            public static final String SNIPPET = "snippet";

            private synchronized Conversations() {
            }
        }

        /* loaded from: classes2.dex */
        public static final class Intents {
            public static final String ACTION_CHANGE_DEFAULT = "android.provider.Telephony.ACTION_CHANGE_DEFAULT";
            public static final String ACTION_DEFAULT_SMS_PACKAGE_CHANGED = "android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED";
            public static final String ACTION_EXTERNAL_PROVIDER_CHANGE = "android.provider.action.EXTERNAL_PROVIDER_CHANGE";
            public static final String ACTION_SMS_MMS_DB_CREATED = "android.provider.action.SMS_MMS_DB_CREATED";
            public static final String ACTION_SMS_MMS_DB_LOST = "android.provider.action.SMS_MMS_DB_LOST";
            public static final String DATA_SMS_RECEIVED_ACTION = "android.intent.action.DATA_SMS_RECEIVED";
            public static final String EXTRA_IS_CORRUPTED = "android.provider.extra.IS_CORRUPTED";
            public static final String EXTRA_IS_DEFAULT_SMS_APP = "android.provider.extra.IS_DEFAULT_SMS_APP";
            public static final String EXTRA_IS_INITIAL_CREATE = "android.provider.extra.IS_INITIAL_CREATE";
            public static final String EXTRA_PACKAGE_NAME = "package";
            public static final String MMS_DOWNLOADED_ACTION = "android.provider.Telephony.MMS_DOWNLOADED";
            public static final int RESULT_SMS_DUPLICATED = 5;
            public static final int RESULT_SMS_GENERIC_ERROR = 2;
            public static final int RESULT_SMS_HANDLED = 1;
            public static final int RESULT_SMS_OUT_OF_MEMORY = 3;
            public static final int RESULT_SMS_UNSUPPORTED = 4;
            public static final String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";
            public static final String SIM_FULL_ACTION = "android.provider.Telephony.SIM_FULL";
            public static final String SMS_CARRIER_PROVISION_ACTION = "android.provider.Telephony.SMS_CARRIER_PROVISION";
            public static final String SMS_CB_RECEIVED_ACTION = "android.provider.Telephony.SMS_CB_RECEIVED";
            public static final String SMS_DELIVER_ACTION = "android.provider.Telephony.SMS_DELIVER";
            private protected static final String SMS_EMERGENCY_CB_RECEIVED_ACTION = "android.provider.Telephony.SMS_EMERGENCY_CB_RECEIVED";
            public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
            public static final String SMS_REJECTED_ACTION = "android.provider.Telephony.SMS_REJECTED";
            public static final String SMS_SERVICE_CATEGORY_PROGRAM_DATA_RECEIVED_ACTION = "android.provider.Telephony.SMS_SERVICE_CATEGORY_PROGRAM_DATA_RECEIVED";
            public static final String WAP_PUSH_DELIVER_ACTION = "android.provider.Telephony.WAP_PUSH_DELIVER";
            public static final String WAP_PUSH_RECEIVED_ACTION = "android.provider.Telephony.WAP_PUSH_RECEIVED";

            private synchronized Intents() {
            }

            public static SmsMessage[] getMessagesFromIntent(Intent intent) {
                try {
                    Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
                    if (messages == null) {
                        Rlog.e(Telephony.TAG, "pdus does not exist in the intent");
                        return null;
                    }
                    String format = intent.getStringExtra(CellBroadcasts.MESSAGE_FORMAT);
                    int subId = intent.getIntExtra(PhoneConstants.SUBSCRIPTION_KEY, SubscriptionManager.getDefaultSmsSubscriptionId());
                    Rlog.v(Telephony.TAG, " getMessagesFromIntent sub_id : " + subId);
                    int pduCount = messages.length;
                    SmsMessage[] msgs = new SmsMessage[pduCount];
                    for (int i = 0; i < pduCount; i++) {
                        byte[] pdu = (byte[]) messages[i];
                        msgs[i] = SmsMessage.createFromPdu(pdu, format);
                        if (msgs[i] != null) {
                            msgs[i].setSubId(subId);
                        }
                    }
                    return msgs;
                } catch (ClassCastException e) {
                    Rlog.e(Telephony.TAG, "getMessagesFromIntent: " + e);
                    return null;
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class Threads implements ThreadsColumns {
        public static final int BROADCAST_THREAD = 1;
        public static final int COMMON_THREAD = 0;
        public protected static final String[] ID_PROJECTION = {"_id"};
        public protected static final Uri THREAD_ID_CONTENT_URI = Uri.parse("content://mms-sms/threadID");
        public static final Uri CONTENT_URI = Uri.withAppendedPath(MmsSms.CONTENT_URI, "conversations");
        public static final Uri OBSOLETE_THREADS_URI = Uri.withAppendedPath(CONTENT_URI, "obsolete");

        private synchronized Threads() {
        }

        public static long getOrCreateThreadId(Context context, String recipient) {
            Set<String> recipients = new HashSet<>();
            recipients.add(recipient);
            return getOrCreateThreadId(context, recipients);
        }

        public static long getOrCreateThreadId(Context context, Set<String> recipients) {
            Uri.Builder uriBuilder = THREAD_ID_CONTENT_URI.buildUpon();
            for (String recipient : recipients) {
                if (Mms.isEmailAddress(recipient)) {
                    recipient = Mms.extractAddrSpec(recipient);
                }
                uriBuilder.appendQueryParameter("recipient", recipient);
            }
            Uri uri = uriBuilder.build();
            Cursor cursor = SqliteWrapper.query(context, context.getContentResolver(), uri, ID_PROJECTION, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        return cursor.getLong(0);
                    }
                    Rlog.e(Telephony.TAG, "getOrCreateThreadId returned no rows!");
                } finally {
                    cursor.close();
                }
            }
            Rlog.e(Telephony.TAG, "getOrCreateThreadId failed with " + recipients.size() + " recipients");
            throw new IllegalArgumentException("Unable to find or allocate a thread ID.");
        }
    }

    /* loaded from: classes2.dex */
    public static final class Mms implements BaseMmsColumns {
        public static final String DEFAULT_SORT_ORDER = "date DESC";
        public static final Uri CONTENT_URI = Uri.parse("content://mms");
        public static final Uri REPORT_REQUEST_URI = Uri.withAppendedPath(CONTENT_URI, "report-request");
        public static final Uri REPORT_STATUS_URI = Uri.withAppendedPath(CONTENT_URI, "report-status");
        private protected static final Pattern NAME_ADDR_EMAIL_PATTERN = Pattern.compile("\\s*(\"[^\"]*\"|[^<>\"]+)\\s*<([^<>]+)>\\s*");

        private synchronized Mms() {
        }

        public static synchronized Cursor query(ContentResolver cr, String[] projection) {
            return cr.query(CONTENT_URI, projection, null, null, "date DESC");
        }

        public static synchronized Cursor query(ContentResolver cr, String[] projection, String where, String orderBy) {
            return cr.query(CONTENT_URI, projection, where, null, orderBy == null ? "date DESC" : orderBy);
        }

        private protected static String extractAddrSpec(String address) {
            Matcher match = NAME_ADDR_EMAIL_PATTERN.matcher(address);
            if (match.matches()) {
                return match.group(2);
            }
            return address;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isEmailAddress(String address) {
            if (TextUtils.isEmpty(address)) {
                return false;
            }
            String s = extractAddrSpec(address);
            Matcher match = Patterns.EMAIL_ADDRESS.matcher(s);
            return match.matches();
        }

        private protected static boolean isPhoneNumber(String number) {
            if (TextUtils.isEmpty(number)) {
                return false;
            }
            Matcher match = Patterns.PHONE.matcher(number);
            return match.matches();
        }

        /* loaded from: classes2.dex */
        public static final class Inbox implements BaseMmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://mms/inbox");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private synchronized Inbox() {
            }
        }

        /* loaded from: classes2.dex */
        public static final class Sent implements BaseMmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://mms/sent");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private synchronized Sent() {
            }
        }

        /* loaded from: classes2.dex */
        public static final class Draft implements BaseMmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://mms/drafts");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private synchronized Draft() {
            }
        }

        /* loaded from: classes2.dex */
        public static final class Outbox implements BaseMmsColumns {
            public static final Uri CONTENT_URI = Uri.parse("content://mms/outbox");
            public static final String DEFAULT_SORT_ORDER = "date DESC";

            private synchronized Outbox() {
            }
        }

        /* loaded from: classes2.dex */
        public static final class Addr implements BaseColumns {
            public static final String ADDRESS = "address";
            public static final String CHARSET = "charset";
            public static final String CONTACT_ID = "contact_id";
            public static final String MSG_ID = "msg_id";
            public static final String TYPE = "type";

            private synchronized Addr() {
            }
        }

        /* loaded from: classes2.dex */
        public static final class Part implements BaseColumns {
            public static final String CHARSET = "chset";
            public static final String CONTENT_DISPOSITION = "cd";
            public static final String CONTENT_ID = "cid";
            public static final String CONTENT_LOCATION = "cl";
            public static final String CONTENT_TYPE = "ct";
            public static final String CT_START = "ctt_s";
            public static final String CT_TYPE = "ctt_t";
            public static final String FILENAME = "fn";
            public static final String MSG_ID = "mid";
            public static final String NAME = "name";
            public static final String SEQ = "seq";
            public static final String TEXT = "text";
            public static final String _DATA = "_data";

            private synchronized Part() {
            }
        }

        /* loaded from: classes2.dex */
        public static final class Rate {
            public static final Uri CONTENT_URI = Uri.withAppendedPath(Mms.CONTENT_URI, TextToSpeech.Engine.KEY_PARAM_RATE);
            public static final String SENT_TIME = "sent_time";

            private synchronized Rate() {
            }
        }

        /* loaded from: classes2.dex */
        public static final class Intents {
            public static final String CONTENT_CHANGED_ACTION = "android.intent.action.CONTENT_CHANGED";
            public static final String DELETED_CONTENTS = "deleted_contents";

            private synchronized Intents() {
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class MmsSms implements BaseColumns {
        public static final int ERR_TYPE_GENERIC = 1;
        public static final int ERR_TYPE_GENERIC_PERMANENT = 10;
        public static final int ERR_TYPE_MMS_PROTO_PERMANENT = 12;
        public static final int ERR_TYPE_MMS_PROTO_TRANSIENT = 3;
        public static final int ERR_TYPE_SMS_PROTO_PERMANENT = 11;
        public static final int ERR_TYPE_SMS_PROTO_TRANSIENT = 2;
        public static final int ERR_TYPE_TRANSPORT_FAILURE = 4;
        public static final int MMS_PROTO = 1;
        public static final int NO_ERROR = 0;
        public static final int SMS_PROTO = 0;
        public static final String TYPE_DISCRIMINATOR_COLUMN = "transport_type";
        public static final Uri CONTENT_URI = Uri.parse("content://mms-sms/");
        public static final Uri CONTENT_CONVERSATIONS_URI = Uri.parse("content://mms-sms/conversations");
        public static final Uri CONTENT_FILTER_BYPHONE_URI = Uri.parse("content://mms-sms/messages/byphone");
        public static final Uri CONTENT_UNDELIVERED_URI = Uri.parse("content://mms-sms/undelivered");
        public static final Uri CONTENT_DRAFT_URI = Uri.parse("content://mms-sms/draft");
        public static final Uri CONTENT_LOCKED_URI = Uri.parse("content://mms-sms/locked");
        public static final Uri SEARCH_URI = Uri.parse("content://mms-sms/search");

        private synchronized MmsSms() {
        }

        /* loaded from: classes2.dex */
        public static final class PendingMessages implements BaseColumns {
            public static final Uri CONTENT_URI = Uri.withAppendedPath(MmsSms.CONTENT_URI, ImsConferenceState.STATUS_PENDING);
            public static final String DUE_TIME = "due_time";
            public static final String ERROR_CODE = "err_code";
            public static final String ERROR_TYPE = "err_type";
            public static final String LAST_TRY = "last_try";
            public static final String MSG_ID = "msg_id";
            public static final String MSG_TYPE = "msg_type";
            public static final String PROTO_TYPE = "proto_type";
            public static final String RETRY_INDEX = "retry_index";
            public static final String SUBSCRIPTION_ID = "pending_sub_id";

            private synchronized PendingMessages() {
            }
        }

        /* loaded from: classes2.dex */
        public static final class WordsTable {
            public static final String ID = "_id";
            public static final String INDEXED_TEXT = "index_text";
            public static final String SOURCE_ROW_ID = "source_id";
            public static final String TABLE_ID = "table_to_use";

            private synchronized WordsTable() {
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class Carriers implements BaseColumns {
        public static final String APN = "apn";
        public static final String APN_SET_ID = "apn_set_id";
        public static final String AUTH_TYPE = "authtype";
        @Deprecated
        public static final String BEARER = "bearer";
        @Deprecated
        public static final String BEARER_BITMASK = "bearer_bitmask";
        public static final int CARRIER_DELETED = 5;
        public static final int CARRIER_DELETED_BUT_PRESENT_IN_XML = 6;
        public static final int CARRIER_EDITED = 4;
        public static final String CARRIER_ENABLED = "carrier_enabled";
        public static final String CURRENT = "current";
        public static final String DEFAULT_SORT_ORDER = "name ASC";
        public static final String EDITED = "edited";
        public static final String ENFORCE_KEY = "enforced";
        public static final String MAX_CONNS = "max_conns";
        public static final String MAX_CONNS_TIME = "max_conns_time";
        public static final String MCC = "mcc";
        public static final String MMSC = "mmsc";
        public static final String MMSPORT = "mmsport";
        public static final String MMSPROXY = "mmsproxy";
        public static final String MNC = "mnc";
        public static final String MODEM_COGNITIVE = "modem_cognitive";
        public static final String MTU = "mtu";
        public static final String MVNO_MATCH_DATA = "mvno_match_data";
        public static final String MVNO_TYPE = "mvno_type";
        public static final String NAME = "name";
        public static final String NETWORK_TYPE_BITMASK = "network_type_bitmask";
        public static final int NO_SET_SET = 0;
        public static final String NUMERIC = "numeric";
        public static final String OWNED_BY = "owned_by";
        public static final int OWNED_BY_DPC = 0;
        public static final int OWNED_BY_OTHERS = 1;
        public static final String PASSWORD = "password";
        public static final String PORT = "port";
        public static final String PROFILE_ID = "profile_id";
        public static final String PROTOCOL = "protocol";
        public static final String PROXY = "proxy";
        public static final String ROAMING_PROTOCOL = "roaming_protocol";
        public static final String SERVER = "server";
        public static final String SUBSCRIPTION_ID = "sub_id";
        public static final String TYPE = "type";
        public static final int UNEDITED = 0;
        public static final String USER = "user";
        public static final int USER_DELETED = 2;
        public static final int USER_DELETED_BUT_PRESENT_IN_XML = 3;
        public static final String USER_EDITABLE = "user_editable";
        public static final int USER_EDITED = 1;
        public static final String USER_VISIBLE = "user_visible";
        public static final String WAIT_TIME = "wait_time";
        public static final Uri CONTENT_URI = Uri.parse("content://telephony/carriers");
        public static final Uri DPC_URI = Uri.parse("content://telephony/carriers/dpc");
        public static final Uri FILTERED_URI = Uri.parse("content://telephony/carriers/filtered");
        public static final Uri ENFORCE_MANAGED_URI = Uri.parse("content://telephony/carriers/enforce_managed");

        private synchronized Carriers() {
        }
    }

    /* loaded from: classes2.dex */
    public static final class CellBroadcasts implements BaseColumns {
        public static final String CID = "cid";
        public static final String DEFAULT_SORT_ORDER = "date DESC";
        public static final String DELIVERY_TIME = "date";
        public static final String LANGUAGE_CODE = "language";
        public static final String MESSAGE_BODY = "body";
        public static final String MESSAGE_PRIORITY = "priority";
        public static final String MESSAGE_READ = "read";
        public static final String PLMN = "plmn";
        public static final String SERIAL_NUMBER = "serial_number";
        public static final String V1_MESSAGE_CODE = "message_code";
        public static final String V1_MESSAGE_IDENTIFIER = "message_id";
        public static final Uri CONTENT_URI = Uri.parse("content://cellbroadcasts");
        public static final String GEOGRAPHICAL_SCOPE = "geo_scope";
        public static final String LAC = "lac";
        public static final String SERVICE_CATEGORY = "service_category";
        public static final String MESSAGE_FORMAT = "format";
        public static final String ETWS_WARNING_TYPE = "etws_warning_type";
        public static final String CMAS_MESSAGE_CLASS = "cmas_message_class";
        public static final String CMAS_CATEGORY = "cmas_category";
        public static final String CMAS_RESPONSE_TYPE = "cmas_response_type";
        public static final String CMAS_SEVERITY = "cmas_severity";
        public static final String CMAS_URGENCY = "cmas_urgency";
        public static final String CMAS_CERTAINTY = "cmas_certainty";
        public static final String[] QUERY_COLUMNS = {"_id", GEOGRAPHICAL_SCOPE, "plmn", LAC, "cid", "serial_number", SERVICE_CATEGORY, "language", "body", "date", "read", MESSAGE_FORMAT, "priority", ETWS_WARNING_TYPE, CMAS_MESSAGE_CLASS, CMAS_CATEGORY, CMAS_RESPONSE_TYPE, CMAS_SEVERITY, CMAS_URGENCY, CMAS_CERTAINTY};

        private synchronized CellBroadcasts() {
        }
    }

    /* loaded from: classes2.dex */
    public static final class ServiceStateTable {
        public static final String AUTHORITY = "service-state";
        public static final String CDMA_DEFAULT_ROAMING_INDICATOR = "cdma_default_roaming_indicator";
        public static final String CDMA_ERI_ICON_INDEX = "cdma_eri_icon_index";
        public static final String CDMA_ERI_ICON_MODE = "cdma_eri_icon_mode";
        public static final String CDMA_ROAMING_INDICATOR = "cdma_roaming_indicator";
        public static final Uri CONTENT_URI = Uri.parse("content://service-state/");
        public static final String CSS_INDICATOR = "css_indicator";
        public static final String DATA_OPERATOR_ALPHA_LONG = "data_operator_alpha_long";
        public static final String DATA_OPERATOR_ALPHA_SHORT = "data_operator_alpha_short";
        public static final String DATA_OPERATOR_NUMERIC = "data_operator_numeric";
        public static final String DATA_REG_STATE = "data_reg_state";
        public static final String DATA_ROAMING_TYPE = "data_roaming_type";
        public static final String IS_DATA_ROAMING_FROM_REGISTRATION = "is_data_roaming_from_registration";
        public static final String IS_EMERGENCY_ONLY = "is_emergency_only";
        public static final String IS_MANUAL_NETWORK_SELECTION = "is_manual_network_selection";
        public static final String IS_USING_CARRIER_AGGREGATION = "is_using_carrier_aggregation";
        public static final String NETWORK_ID = "network_id";
        public static final String RIL_DATA_RADIO_TECHNOLOGY = "ril_data_radio_technology";
        public static final String RIL_VOICE_RADIO_TECHNOLOGY = "ril_voice_radio_technology";
        public static final String SYSTEM_ID = "system_id";
        public static final String VOICE_OPERATOR_ALPHA_LONG = "voice_operator_alpha_long";
        public static final String VOICE_OPERATOR_ALPHA_SHORT = "voice_operator_alpha_short";
        public static final String VOICE_OPERATOR_NUMERIC = "voice_operator_numeric";
        public static final String VOICE_REG_STATE = "voice_reg_state";
        public static final String VOICE_ROAMING_TYPE = "voice_roaming_type";

        private synchronized ServiceStateTable() {
        }

        public static Uri getUriForSubscriptionIdAndField(int subscriptionId, String field) {
            return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(subscriptionId)).appendEncodedPath(field).build();
        }

        public static Uri getUriForSubscriptionId(int subscriptionId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(subscriptionId)).build();
        }

        public static synchronized ContentValues getContentValuesForServiceState(ServiceState state) {
            ContentValues values = new ContentValues();
            values.put(VOICE_REG_STATE, Integer.valueOf(state.getVoiceRegState()));
            values.put(DATA_REG_STATE, Integer.valueOf(state.getDataRegState()));
            values.put(VOICE_ROAMING_TYPE, Integer.valueOf(state.getVoiceRoamingType()));
            values.put(DATA_ROAMING_TYPE, Integer.valueOf(state.getDataRoamingType()));
            values.put(VOICE_OPERATOR_ALPHA_LONG, state.getVoiceOperatorAlphaLong());
            values.put(VOICE_OPERATOR_ALPHA_SHORT, state.getVoiceOperatorAlphaShort());
            values.put(VOICE_OPERATOR_NUMERIC, state.getVoiceOperatorNumeric());
            values.put(DATA_OPERATOR_ALPHA_LONG, state.getDataOperatorAlphaLong());
            values.put(DATA_OPERATOR_ALPHA_SHORT, state.getDataOperatorAlphaShort());
            values.put(DATA_OPERATOR_NUMERIC, state.getDataOperatorNumeric());
            values.put(IS_MANUAL_NETWORK_SELECTION, Boolean.valueOf(state.getIsManualSelection()));
            values.put(RIL_VOICE_RADIO_TECHNOLOGY, Integer.valueOf(state.getRilVoiceRadioTechnology()));
            values.put(RIL_DATA_RADIO_TECHNOLOGY, Integer.valueOf(state.getRilDataRadioTechnology()));
            values.put(CSS_INDICATOR, Integer.valueOf(state.getCssIndicator()));
            values.put(NETWORK_ID, Integer.valueOf(state.getCdmaNetworkId()));
            values.put("system_id", Integer.valueOf(state.getCdmaSystemId()));
            values.put(CDMA_ROAMING_INDICATOR, Integer.valueOf(state.getCdmaRoamingIndicator()));
            values.put(CDMA_DEFAULT_ROAMING_INDICATOR, Integer.valueOf(state.getCdmaDefaultRoamingIndicator()));
            values.put(CDMA_ERI_ICON_INDEX, Integer.valueOf(state.getCdmaEriIconIndex()));
            values.put(CDMA_ERI_ICON_MODE, Integer.valueOf(state.getCdmaEriIconMode()));
            values.put(IS_EMERGENCY_ONLY, Boolean.valueOf(state.isEmergencyOnly()));
            values.put(IS_DATA_ROAMING_FROM_REGISTRATION, Boolean.valueOf(state.getDataRoamingFromRegistration()));
            values.put(IS_USING_CARRIER_AGGREGATION, Boolean.valueOf(state.isUsingCarrierAggregation()));
            return values;
        }
    }

    /* loaded from: classes2.dex */
    public static final class CarrierId implements BaseColumns {
        public static final String AUTHORITY = "carrier_id";
        public static final String CARRIER_ID = "carrier_id";
        public static final String CARRIER_NAME = "carrier_name";
        public static final Uri CONTENT_URI = Uri.parse("content://carrier_id");

        /* loaded from: classes2.dex */
        public static final class All implements BaseColumns {
            public static final String APN = "apn";
            public static final Uri CONTENT_URI = Uri.parse("content://carrier_id/all");
            public static final String GID1 = "gid1";
            public static final String GID2 = "gid2";
            public static final String ICCID_PREFIX = "iccid_prefix";
            public static final String IMSI_PREFIX_XPATTERN = "imsi_prefix_xpattern";
            public static final String MCCMNC = "mccmnc";
            public static final String PLMN = "plmn";
            public static final String SPN = "spn";
        }

        private synchronized CarrierId() {
        }

        public static Uri getUriForSubscriptionId(int subscriptionId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(subscriptionId)).build();
        }
    }
}
