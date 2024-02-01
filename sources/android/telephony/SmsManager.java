package android.telephony;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CursorWindow;
import android.hardware.contexthub.V1_0.HostEndPoint;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.IFinancialSmsCallback;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.IMms;
import com.android.internal.telephony.ISms;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.SmsRawData;
import com.android.internal.util.FunctionalUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class SmsManager {
    public static final int CDMA_SMS_RECORD_LENGTH = 255;
    public static final int CELL_BROADCAST_RAN_TYPE_CDMA = 1;
    public static final int CELL_BROADCAST_RAN_TYPE_GSM = 0;
    public static final String EXTRA_MMS_DATA = "android.telephony.extra.MMS_DATA";
    public static final String EXTRA_MMS_HTTP_STATUS = "android.telephony.extra.MMS_HTTP_STATUS";
    public static final String EXTRA_SIM_SUBSCRIPTION_ID = "android.telephony.extra.SIM_SUBSCRIPTION_ID";
    public static final String EXTRA_SMS_MESSAGE = "android.telephony.extra.SMS_MESSAGE";
    public static final String EXTRA_STATUS = "android.telephony.extra.STATUS";
    public static final String MESSAGE_STATUS_READ = "read";
    public static final String MESSAGE_STATUS_SEEN = "seen";
    public static final String MMS_CONFIG_ALIAS_ENABLED = "aliasEnabled";
    public static final String MMS_CONFIG_ALIAS_MAX_CHARS = "aliasMaxChars";
    public static final String MMS_CONFIG_ALIAS_MIN_CHARS = "aliasMinChars";
    public static final String MMS_CONFIG_ALLOW_ATTACH_AUDIO = "allowAttachAudio";
    public static final String MMS_CONFIG_APPEND_TRANSACTION_ID = "enabledTransID";
    public static final String MMS_CONFIG_CLOSE_CONNECTION = "mmsCloseConnection";
    public static final String MMS_CONFIG_EMAIL_GATEWAY_NUMBER = "emailGatewayNumber";
    public static final String MMS_CONFIG_GROUP_MMS_ENABLED = "enableGroupMms";
    public static final String MMS_CONFIG_HTTP_PARAMS = "httpParams";
    public static final String MMS_CONFIG_HTTP_SOCKET_TIMEOUT = "httpSocketTimeout";
    public static final String MMS_CONFIG_MAX_IMAGE_HEIGHT = "maxImageHeight";
    public static final String MMS_CONFIG_MAX_IMAGE_WIDTH = "maxImageWidth";
    public static final String MMS_CONFIG_MAX_MESSAGE_SIZE = "maxMessageSize";
    public static final String MMS_CONFIG_MESSAGE_TEXT_MAX_SIZE = "maxMessageTextSize";
    public static final String MMS_CONFIG_MMS_DELIVERY_REPORT_ENABLED = "enableMMSDeliveryReports";
    public static final String MMS_CONFIG_MMS_ENABLED = "enabledMMS";
    public static final String MMS_CONFIG_MMS_READ_REPORT_ENABLED = "enableMMSReadReports";
    public static final String MMS_CONFIG_MULTIPART_SMS_ENABLED = "enableMultipartSMS";
    public static final String MMS_CONFIG_NAI_SUFFIX = "naiSuffix";
    public static final String MMS_CONFIG_NOTIFY_WAP_MMSC_ENABLED = "enabledNotifyWapMMSC";
    public static final String MMS_CONFIG_RECIPIENT_LIMIT = "recipientLimit";
    public static final String MMS_CONFIG_SEND_MULTIPART_SMS_AS_SEPARATE_MESSAGES = "sendMultipartSmsAsSeparateMessages";
    public static final String MMS_CONFIG_SHOW_CELL_BROADCAST_APP_LINKS = "config_cellBroadcastAppLinks";
    public static final String MMS_CONFIG_SMS_DELIVERY_REPORT_ENABLED = "enableSMSDeliveryReports";
    public static final String MMS_CONFIG_SMS_TO_MMS_TEXT_LENGTH_THRESHOLD = "smsToMmsTextLengthThreshold";
    public static final String MMS_CONFIG_SMS_TO_MMS_TEXT_THRESHOLD = "smsToMmsTextThreshold";
    public static final String MMS_CONFIG_SUBJECT_MAX_LENGTH = "maxSubjectLength";
    public static final String MMS_CONFIG_SUPPORT_HTTP_CHARSET_HEADER = "supportHttpCharsetHeader";
    public static final String MMS_CONFIG_SUPPORT_MMS_CONTENT_DISPOSITION = "supportMmsContentDisposition";
    public static final String MMS_CONFIG_UA_PROF_TAG_NAME = "uaProfTagName";
    public static final String MMS_CONFIG_UA_PROF_URL = "uaProfUrl";
    public static final String MMS_CONFIG_USER_AGENT = "userAgent";
    public static final int MMS_ERROR_CONFIGURATION_ERROR = 7;
    public static final int MMS_ERROR_HTTP_FAILURE = 4;
    public static final int MMS_ERROR_INVALID_APN = 2;
    public static final int MMS_ERROR_IO_ERROR = 5;
    public static final int MMS_ERROR_NO_DATA_NETWORK = 8;
    public static final int MMS_ERROR_RETRY = 6;
    public static final int MMS_ERROR_UNABLE_CONNECT_MMS = 3;
    public static final int MMS_ERROR_UNSPECIFIED = 1;
    private static final String NO_DEFAULT_EXTRA = "noDefault";
    private static final String PHONE_PACKAGE_NAME = "com.android.phone";
    public static final String REGEX_PREFIX_DELIMITER = ",";
    @SystemApi
    public static final int RESULT_CANCELLED = 23;
    @SystemApi
    public static final int RESULT_ENCODING_ERROR = 18;
    @SystemApi
    public static final int RESULT_ERROR_FDN_CHECK_FAILURE = 6;
    public static final int RESULT_ERROR_GENERIC_FAILURE = 1;
    public static final int RESULT_ERROR_LIMIT_EXCEEDED = 5;
    @SystemApi
    public static final int RESULT_ERROR_NONE = 0;
    public static final int RESULT_ERROR_NO_SERVICE = 4;
    public static final int RESULT_ERROR_NULL_PDU = 3;
    public static final int RESULT_ERROR_RADIO_OFF = 2;
    public static final int RESULT_ERROR_SHORT_CODE_NEVER_ALLOWED = 8;
    public static final int RESULT_ERROR_SHORT_CODE_NOT_ALLOWED = 7;
    @SystemApi
    public static final int RESULT_INTERNAL_ERROR = 21;
    @SystemApi
    public static final int RESULT_INVALID_ARGUMENTS = 11;
    @SystemApi
    public static final int RESULT_INVALID_SMSC_ADDRESS = 19;
    @SystemApi
    public static final int RESULT_INVALID_SMS_FORMAT = 14;
    @SystemApi
    public static final int RESULT_INVALID_STATE = 12;
    @SystemApi
    public static final int RESULT_MODEM_ERROR = 16;
    @SystemApi
    public static final int RESULT_NETWORK_ERROR = 17;
    @SystemApi
    public static final int RESULT_NETWORK_REJECT = 10;
    @SystemApi
    public static final int RESULT_NO_MEMORY = 13;
    @SystemApi
    public static final int RESULT_NO_RESOURCES = 22;
    @SystemApi
    public static final int RESULT_OPERATION_NOT_ALLOWED = 20;
    @SystemApi
    public static final int RESULT_RADIO_NOT_AVAILABLE = 9;
    @SystemApi
    public static final int RESULT_REQUEST_NOT_SUPPORTED = 24;
    public static final int RESULT_STATUS_SUCCESS = 0;
    public static final int RESULT_STATUS_TIMEOUT = 1;
    @SystemApi
    public static final int RESULT_SYSTEM_ERROR = 15;
    public static final int SMS_CATEGORY_FREE_SHORT_CODE = 1;
    public static final int SMS_CATEGORY_NOT_SHORT_CODE = 0;
    public static final int SMS_CATEGORY_POSSIBLE_PREMIUM_SHORT_CODE = 3;
    public static final int SMS_CATEGORY_PREMIUM_SHORT_CODE = 4;
    public static final int SMS_CATEGORY_STANDARD_SHORT_CODE = 2;
    public static final int SMS_MESSAGE_PERIOD_NOT_SPECIFIED = -1;
    public static final int SMS_MESSAGE_PRIORITY_NOT_SPECIFIED = -1;
    public static final int SMS_RECORD_LENGTH = 176;
    public static final int SMS_TYPE_INCOMING = 0;
    public static final int SMS_TYPE_OUTGOING = 1;
    public static final int STATUS_ON_ICC_FREE = 0;
    public static final int STATUS_ON_ICC_READ = 1;
    public static final int STATUS_ON_ICC_SENT = 5;
    public static final int STATUS_ON_ICC_UNREAD = 3;
    public static final int STATUS_ON_ICC_UNSENT = 7;
    private static final String TAG = "SmsManager";
    private static final SmsManager sInstance = new SmsManager(Integer.MAX_VALUE);
    private static final Object sLockObject = new Object();
    private static final Map<Integer, SmsManager> sSubInstances = new ArrayMap();
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mSubId;

    /* loaded from: classes2.dex */
    public static abstract class FinancialSmsCallback {
        public abstract void onFinancialSmsMessages(CursorWindow cursorWindow);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface SmsShortCodeCategory {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public interface SubscriptionResolverResult {
        void onFailure();

        void onSuccess(int i);
    }

    static /* synthetic */ ISms access$000() {
        return getISmsServiceOrThrow();
    }

    public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, true, ActivityThread.currentPackageName());
    }

    private void sendTextMessageInternal(final String destinationAddress, final String scAddress, final String text, final PendingIntent sentIntent, final PendingIntent deliveryIntent, final boolean persistMessage, final String packageName) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Invalid message body");
        }
        final Context context = ActivityThread.currentApplication().getApplicationContext();
        if (persistMessage) {
            resolveSubscriptionForOperation(new SubscriptionResolverResult() { // from class: android.telephony.SmsManager.1
                @Override // android.telephony.SmsManager.SubscriptionResolverResult
                public void onSuccess(int subId) {
                    ISms iSms = SmsManager.access$000();
                    try {
                        iSms.sendTextForSubscriber(subId, packageName, destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage);
                    } catch (RemoteException e) {
                        Log.e(SmsManager.TAG, "sendTextMessageInternal: Couldn't send SMS, exception - " + e.getMessage());
                        SmsManager.notifySmsGenericError(sentIntent);
                    }
                }

                @Override // android.telephony.SmsManager.SubscriptionResolverResult
                public void onFailure() {
                    SmsManager.notifySmsErrorNoDefaultSet(context, sentIntent);
                }
            });
            return;
        }
        ISms iSms = getISmsServiceOrThrow();
        try {
            iSms.sendTextForSubscriber(getSubscriptionId(), packageName, destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage);
        } catch (RemoteException e) {
            Log.e(TAG, "sendTextMessageInternal (no persist): Couldn't send SMS, exception - " + e.getMessage());
            notifySmsGenericError(sentIntent);
        }
    }

    public void sendTextMessageWithoutPersisting(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, false, ActivityThread.currentPackageName());
    }

    public void sendTextMessageWithSelfPermissions(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Invalid message body");
        }
        try {
            ISms iSms = getISmsServiceOrThrow();
            iSms.sendTextForSubscriberWithSelfPermissions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage);
        } catch (RemoteException e) {
            notifySmsGenericError(sentIntent);
        }
    }

    @UnsupportedAppUsage
    public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, int priority, boolean expectMore, int validityPeriod) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, true, priority, expectMore, validityPeriod);
    }

    private void sendTextMessageInternal(final String destinationAddress, final String scAddress, final String text, final PendingIntent sentIntent, final PendingIntent deliveryIntent, final boolean persistMessage, int priority, final boolean expectMore, int validityPeriod) {
        int priority2;
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Invalid message body");
        }
        if (priority >= 0 && priority <= 3) {
            priority2 = priority;
        } else {
            priority2 = -1;
        }
        final int finalPriority = priority2;
        final int finalValidity = (validityPeriod < 5 || validityPeriod > 635040) ? -1 : validityPeriod;
        final Context context = ActivityThread.currentApplication().getApplicationContext();
        if (persistMessage) {
            resolveSubscriptionForOperation(new SubscriptionResolverResult() { // from class: android.telephony.SmsManager.2
                @Override // android.telephony.SmsManager.SubscriptionResolverResult
                public void onSuccess(int subId) {
                    try {
                        ISms iSms = SmsManager.access$000();
                        if (iSms != null) {
                            iSms.sendTextForSubscriberWithOptions(subId, ActivityThread.currentPackageName(), destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage, finalPriority, expectMore, finalValidity);
                        }
                    } catch (RemoteException e) {
                        Log.e(SmsManager.TAG, "sendTextMessageInternal: Couldn't send SMS, exception - " + e.getMessage());
                        SmsManager.notifySmsGenericError(sentIntent);
                    }
                }

                @Override // android.telephony.SmsManager.SubscriptionResolverResult
                public void onFailure() {
                    SmsManager.notifySmsErrorNoDefaultSet(context, sentIntent);
                }
            });
            return;
        }
        try {
            ISms iSms = getISmsServiceOrThrow();
            if (iSms != null) {
                iSms.sendTextForSubscriberWithOptions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage, finalPriority, expectMore, finalValidity);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "sendTextMessageInternal(no persist): Couldn't send SMS, exception - " + e.getMessage());
            notifySmsGenericError(sentIntent);
        }
    }

    @UnsupportedAppUsage
    public void sendTextMessageWithoutPersisting(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, int priority, boolean expectMore, int validityPeriod) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, false, priority, expectMore, validityPeriod);
    }

    public void injectSmsPdu(byte[] pdu, String format, PendingIntent receivedIntent) {
        if (!format.equals("3gpp") && !format.equals("3gpp2")) {
            throw new IllegalArgumentException("Invalid pdu format. format must be either 3gpp or 3gpp2");
        }
        try {
            ISms iSms = ISms.Stub.asInterface(ServiceManager.getService("isms"));
            if (iSms != null) {
                iSms.injectSmsPduForSubscriber(getSubscriptionId(), pdu, format, receivedIntent);
            }
        } catch (RemoteException e) {
            if (receivedIntent != null) {
                try {
                    receivedIntent.send(2);
                } catch (PendingIntent.CanceledException e2) {
                }
            }
        }
    }

    public ArrayList<String> divideMessage(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text is null");
        }
        return SmsMessage.fragmentText(text, getSubscriptionId());
    }

    public void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, true, ActivityThread.currentPackageName());
    }

    public void sendMultipartTextMessageExternal(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, String packageName) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, true, ActivityThread.currentPackageName() == null ? packageName : ActivityThread.currentPackageName());
    }

    private void sendMultipartTextMessageInternal(final String destinationAddress, final String scAddress, final List<String> parts, final List<PendingIntent> sentIntents, final List<PendingIntent> deliveryIntents, final boolean persistMessage, final String packageName) {
        PendingIntent deliveryIntent;
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (parts == null || parts.size() < 1) {
            throw new IllegalArgumentException("Invalid message body");
        }
        if (parts.size() > 1) {
            final Context context = ActivityThread.currentApplication().getApplicationContext();
            if (persistMessage) {
                resolveSubscriptionForOperation(new SubscriptionResolverResult() { // from class: android.telephony.SmsManager.3
                    @Override // android.telephony.SmsManager.SubscriptionResolverResult
                    public void onSuccess(int subId) {
                        try {
                            ISms iSms = SmsManager.access$000();
                            iSms.sendMultipartTextForSubscriber(subId, packageName, destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessage);
                        } catch (RemoteException e) {
                            Log.e(SmsManager.TAG, "sendMultipartTextMessageInternal: Couldn't send SMS - " + e.getMessage());
                            SmsManager.notifySmsGenericError(sentIntents);
                        }
                    }

                    @Override // android.telephony.SmsManager.SubscriptionResolverResult
                    public void onFailure() {
                        SmsManager.notifySmsErrorNoDefaultSet(context, sentIntents);
                    }
                });
                return;
            }
            try {
                ISms iSms = getISmsServiceOrThrow();
                if (iSms != null) {
                    iSms.sendMultipartTextForSubscriber(getSubscriptionId(), packageName, destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessage);
                    return;
                }
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "sendMultipartTextMessageInternal: Couldn't send SMS - " + e.getMessage());
                notifySmsGenericError(sentIntents);
                return;
            }
        }
        PendingIntent sentIntent = null;
        if (sentIntents != null && sentIntents.size() > 0) {
            sentIntent = sentIntents.get(0);
        }
        if (deliveryIntents != null && deliveryIntents.size() > 0) {
            PendingIntent deliveryIntent2 = deliveryIntents.get(0);
            deliveryIntent = deliveryIntent2;
        } else {
            deliveryIntent = null;
        }
        sendTextMessageInternal(destinationAddress, scAddress, parts.get(0), sentIntent, deliveryIntent, true, packageName);
    }

    @SystemApi
    public void sendMultipartTextMessageWithoutPersisting(String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, false, ActivityThread.currentPackageName());
    }

    @UnsupportedAppUsage
    public void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, int priority, boolean expectMore, int validityPeriod) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, true, priority, expectMore, validityPeriod);
    }

    private void sendMultipartTextMessageInternal(final String destinationAddress, final String scAddress, final List<String> parts, final List<PendingIntent> sentIntents, final List<PendingIntent> deliveryIntents, final boolean persistMessage, int priority, final boolean expectMore, int validityPeriod) {
        PendingIntent deliveryIntent;
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (parts == null || parts.size() < 1) {
            throw new IllegalArgumentException("Invalid message body");
        }
        int priority2 = (priority < 0 || priority > 3) ? -1 : priority;
        int validityPeriod2 = (validityPeriod < 5 || validityPeriod > 635040) ? -1 : validityPeriod;
        if (parts.size() > 1) {
            final int finalPriority = priority2;
            final int finalValidity = validityPeriod2;
            final Context context = ActivityThread.currentApplication().getApplicationContext();
            if (persistMessage) {
                resolveSubscriptionForOperation(new SubscriptionResolverResult() { // from class: android.telephony.SmsManager.4
                    @Override // android.telephony.SmsManager.SubscriptionResolverResult
                    public void onSuccess(int subId) {
                        try {
                            ISms iSms = SmsManager.access$000();
                            if (iSms != null) {
                                iSms.sendMultipartTextForSubscriberWithOptions(subId, ActivityThread.currentPackageName(), destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessage, finalPriority, expectMore, finalValidity);
                            }
                        } catch (RemoteException e) {
                            Log.e(SmsManager.TAG, "sendMultipartTextMessageInternal: Couldn't send SMS - " + e.getMessage());
                            SmsManager.notifySmsGenericError(sentIntents);
                        }
                    }

                    @Override // android.telephony.SmsManager.SubscriptionResolverResult
                    public void onFailure() {
                        SmsManager.notifySmsErrorNoDefaultSet(context, sentIntents);
                    }
                });
                return;
            }
            try {
                ISms iSms = getISmsServiceOrThrow();
                if (iSms != null) {
                    try {
                        iSms.sendMultipartTextForSubscriberWithOptions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessage, finalPriority, expectMore, finalValidity);
                    } catch (RemoteException e) {
                        e = e;
                        Log.e(TAG, "sendMultipartTextMessageInternal (no persist): Couldn't send SMS - " + e.getMessage());
                        notifySmsGenericError(sentIntents);
                    }
                }
            } catch (RemoteException e2) {
                e = e2;
            }
        } else {
            PendingIntent sentIntent = null;
            if (sentIntents != null && sentIntents.size() > 0) {
                sentIntent = sentIntents.get(0);
            }
            if (deliveryIntents != null && deliveryIntents.size() > 0) {
                PendingIntent deliveryIntent2 = deliveryIntents.get(0);
                deliveryIntent = deliveryIntent2;
            } else {
                deliveryIntent = null;
            }
            sendTextMessageInternal(destinationAddress, scAddress, parts.get(0), sentIntent, deliveryIntent, persistMessage, priority2, expectMore, validityPeriod2);
        }
    }

    public void sendMultipartTextMessageWithoutPersisting(String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, int priority, boolean expectMore, int validityPeriod) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, false, priority, expectMore, validityPeriod);
    }

    public void sendDataMessage(final String destinationAddress, final String scAddress, final short destinationPort, final byte[] data, final PendingIntent sentIntent, final PendingIntent deliveryIntent) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Invalid message data");
        }
        final Context context = ActivityThread.currentApplication().getApplicationContext();
        resolveSubscriptionForOperation(new SubscriptionResolverResult() { // from class: android.telephony.SmsManager.5
            @Override // android.telephony.SmsManager.SubscriptionResolverResult
            public void onSuccess(int subId) {
                try {
                    ISms iSms = SmsManager.access$000();
                    iSms.sendDataForSubscriber(subId, ActivityThread.currentPackageName(), destinationAddress, scAddress, 65535 & destinationPort, data, sentIntent, deliveryIntent);
                } catch (RemoteException e) {
                    Log.e(SmsManager.TAG, "sendDataMessage: Couldn't send SMS - Exception: " + e.getMessage());
                    SmsManager.notifySmsGenericError(sentIntent);
                }
            }

            @Override // android.telephony.SmsManager.SubscriptionResolverResult
            public void onFailure() {
                SmsManager.notifySmsErrorNoDefaultSet(context, sentIntent);
            }
        });
    }

    public void sendDataMessageWithSelfPermissions(String destinationAddress, String scAddress, short destinationPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Invalid message data");
        }
        try {
            ISms iSms = getISmsServiceOrThrow();
            iSms.sendDataForSubscriberWithSelfPermissions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, destinationPort & HostEndPoint.BROADCAST, data, sentIntent, deliveryIntent);
        } catch (RemoteException e) {
            Log.e(TAG, "sendDataMessageWithSelfPermissions: Couldn't send SMS - Exception: " + e.getMessage());
            notifySmsGenericError(sentIntent);
        }
    }

    public static SmsManager getDefault() {
        return sInstance;
    }

    public static SmsManager getSmsManagerForSubscriptionId(int subId) {
        SmsManager smsManager;
        synchronized (sLockObject) {
            smsManager = sSubInstances.get(Integer.valueOf(subId));
            if (smsManager == null) {
                smsManager = new SmsManager(subId);
                sSubInstances.put(Integer.valueOf(subId), smsManager);
            }
        }
        return smsManager;
    }

    private SmsManager(int subId) {
        this.mSubId = subId;
    }

    public int getSubscriptionId() {
        try {
            return this.mSubId == Integer.MAX_VALUE ? getISmsServiceOrThrow().getPreferredSmsSubscription() : this.mSubId;
        } catch (RemoteException e) {
            return -1;
        }
    }

    private void resolveSubscriptionForOperation(final SubscriptionResolverResult resolverResult) {
        int subId = getSubscriptionId();
        boolean isSmsSimPickActivityNeeded = false;
        Context context = ActivityThread.currentApplication().getApplicationContext();
        try {
            ISms iSms = getISmsService();
            if (iSms != null) {
                isSmsSimPickActivityNeeded = iSms.isSmsSimPickActivityNeeded(subId);
            }
        } catch (RemoteException ex) {
            Log.e(TAG, "resolveSubscriptionForOperation", ex);
        }
        if (!isSmsSimPickActivityNeeded) {
            sendResolverResult(resolverResult, subId, false);
            return;
        }
        Log.d(TAG, "resolveSubscriptionForOperation isSmsSimPickActivityNeeded is true for package " + context.getPackageName());
        try {
            getITelephony().enqueueSmsPickResult(context.getOpPackageName(), new IIntegerConsumer.Stub() { // from class: android.telephony.SmsManager.6
                @Override // com.android.internal.telephony.IIntegerConsumer
                public void accept(int subId2) {
                    SmsManager.this.sendResolverResult(resolverResult, subId2, true);
                }
            });
        } catch (RemoteException ex2) {
            Log.e(TAG, "Unable to launch activity", ex2);
            sendResolverResult(resolverResult, subId, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendResolverResult(SubscriptionResolverResult resolverResult, int subId, boolean pickActivityShown) {
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            resolverResult.onSuccess(subId);
        } else if (getTargetSdkVersion() <= 28 && !pickActivityShown) {
            resolverResult.onSuccess(subId);
        } else {
            resolverResult.onFailure();
        }
    }

    private static int getTargetSdkVersion() {
        Context context = ActivityThread.currentApplication().getApplicationContext();
        try {
            int targetSdk = context.getPackageManager().getApplicationInfo(context.getOpPackageName(), 0).targetSdkVersion;
            return targetSdk;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    private static ITelephony getITelephony() {
        ITelephony binder = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
        if (binder == null) {
            throw new RuntimeException("Could not find Telephony Service.");
        }
        return binder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifySmsErrorNoDefaultSet(Context context, PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            Intent errorMessage = new Intent();
            errorMessage.putExtra(NO_DEFAULT_EXTRA, true);
            try {
                pendingIntent.send(context, 1, errorMessage);
            } catch (PendingIntent.CanceledException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifySmsErrorNoDefaultSet(Context context, List<PendingIntent> pendingIntents) {
        if (pendingIntents != null) {
            for (PendingIntent pendingIntent : pendingIntents) {
                Intent errorMessage = new Intent();
                errorMessage.putExtra(NO_DEFAULT_EXTRA, true);
                try {
                    pendingIntent.send(context, 1, errorMessage);
                } catch (PendingIntent.CanceledException e) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifySmsGenericError(PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            try {
                pendingIntent.send(1);
            } catch (PendingIntent.CanceledException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifySmsGenericError(List<PendingIntent> pendingIntents) {
        if (pendingIntents != null) {
            for (PendingIntent pendingIntent : pendingIntents) {
                try {
                    pendingIntent.send(1);
                } catch (PendingIntent.CanceledException e) {
                }
            }
        }
    }

    private static ISms getISmsServiceOrThrow() {
        ISms iSms = getISmsService();
        if (iSms == null) {
            throw new UnsupportedOperationException("Sms is not supported");
        }
        return iSms;
    }

    private static ISms getISmsService() {
        return ISms.Stub.asInterface(ServiceManager.getService("isms"));
    }

    @UnsupportedAppUsage
    public boolean copyMessageToIcc(byte[] smsc, byte[] pdu, int status) {
        if (pdu == null) {
            throw new IllegalArgumentException("pdu is NULL");
        }
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return false;
            }
            boolean success = iSms.copyMessageToIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), status, pdu, smsc);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean deleteMessageFromIcc(int messageIndex) {
        byte[] pdu = new byte[175];
        Arrays.fill(pdu, (byte) -1);
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return false;
            }
            boolean success = iSms.updateMessageOnIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), messageIndex, 0, pdu);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean updateMessageOnIcc(int messageIndex, int newStatus, byte[] pdu) {
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return false;
            }
            boolean success = iSms.updateMessageOnIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), messageIndex, newStatus, pdu);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public ArrayList<SmsMessage> getAllMessagesFromIcc() {
        List<SmsRawData> records = null;
        try {
            ISms iSms = getISmsService();
            if (iSms != null) {
                records = iSms.getAllMessagesFromIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName());
            }
        } catch (RemoteException e) {
        }
        return createMessageListFromRawRecords(records);
    }

    public boolean enableCellBroadcast(int messageIdentifier, int ranType) {
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return false;
            }
            boolean success = iSms.enableCellBroadcastForSubscriber(getSubscriptionId(), messageIdentifier, ranType);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean disableCellBroadcast(int messageIdentifier, int ranType) {
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return false;
            }
            boolean success = iSms.disableCellBroadcastForSubscriber(getSubscriptionId(), messageIdentifier, ranType);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean enableCellBroadcastRange(int startMessageId, int endMessageId, int ranType) {
        if (endMessageId < startMessageId) {
            throw new IllegalArgumentException("endMessageId < startMessageId");
        }
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return false;
            }
            boolean success = iSms.enableCellBroadcastRangeForSubscriber(getSubscriptionId(), startMessageId, endMessageId, ranType);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public boolean disableCellBroadcastRange(int startMessageId, int endMessageId, int ranType) {
        if (endMessageId < startMessageId) {
            throw new IllegalArgumentException("endMessageId < startMessageId");
        }
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return false;
            }
            boolean success = iSms.disableCellBroadcastRangeForSubscriber(getSubscriptionId(), startMessageId, endMessageId, ranType);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    private ArrayList<SmsMessage> createMessageListFromRawRecords(List<SmsRawData> records) {
        SmsMessage sms;
        ArrayList<SmsMessage> messages = new ArrayList<>();
        if (records != null) {
            int count = records.size();
            for (int i = 0; i < count; i++) {
                SmsRawData data = records.get(i);
                if (data != null && (sms = SmsMessage.createFromEfRecord(i + 1, data.getBytes(), getSubscriptionId())) != null) {
                    messages.add(sms);
                }
            }
        }
        return messages;
    }

    public boolean isImsSmsSupported() {
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return false;
            }
            boolean boSupported = iSms.isImsSmsSupportedForSubscriber(getSubscriptionId());
            return boSupported;
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getImsSmsFormat() {
        try {
            ISms iSms = getISmsService();
            if (iSms == null) {
                return "unknown";
            }
            String format = iSms.getImsSmsFormatForSubscriber(getSubscriptionId());
            return format;
        } catch (RemoteException e) {
            return "unknown";
        }
    }

    public static int getDefaultSmsSubscriptionId() {
        try {
            return getISmsService().getPreferredSmsSubscription();
        } catch (RemoteException e) {
            return -1;
        } catch (NullPointerException e2) {
            return -1;
        }
    }

    @UnsupportedAppUsage
    public boolean isSMSPromptEnabled() {
        try {
            ISms iSms = ISms.Stub.asInterface(ServiceManager.getService("isms"));
            return iSms.isSMSPromptEnabled();
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    public void sendMultimediaMessage(Context context, Uri contentUri, String locationUrl, Bundle configOverrides, PendingIntent sentIntent) {
        if (contentUri == null) {
            throw new IllegalArgumentException("Uri contentUri null");
        }
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms == null) {
                return;
            }
            iMms.sendMessage(getSubscriptionId(), ActivityThread.currentPackageName(), contentUri, locationUrl, configOverrides, sentIntent);
        } catch (RemoteException e) {
        }
    }

    public void downloadMultimediaMessage(Context context, String locationUrl, Uri contentUri, Bundle configOverrides, PendingIntent downloadedIntent) {
        if (TextUtils.isEmpty(locationUrl)) {
            throw new IllegalArgumentException("Empty MMS location URL");
        }
        if (contentUri == null) {
            throw new IllegalArgumentException("Uri contentUri null");
        }
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms == null) {
                return;
            }
            iMms.downloadMessage(getSubscriptionId(), ActivityThread.currentPackageName(), locationUrl, contentUri, configOverrides, downloadedIntent);
        } catch (RemoteException e) {
        }
    }

    public Uri importTextMessage(String address, int type, String text, long timestampMillis, boolean seen, boolean read) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.importTextMessage(ActivityThread.currentPackageName(), address, type, text, timestampMillis, seen, read);
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public Uri importMultimediaMessage(Uri contentUri, String messageId, long timestampSecs, boolean seen, boolean read) {
        if (contentUri == null) {
            throw new IllegalArgumentException("Uri contentUri null");
        }
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.importMultimediaMessage(ActivityThread.currentPackageName(), contentUri, messageId, timestampSecs, seen, read);
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean deleteStoredMessage(Uri messageUri) {
        if (messageUri == null) {
            throw new IllegalArgumentException("Empty message URI");
        }
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.deleteStoredMessage(ActivityThread.currentPackageName(), messageUri);
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean deleteStoredConversation(long conversationId) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.deleteStoredConversation(ActivityThread.currentPackageName(), conversationId);
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean updateStoredMessageStatus(Uri messageUri, ContentValues statusValues) {
        if (messageUri == null) {
            throw new IllegalArgumentException("Empty message URI");
        }
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.updateStoredMessageStatus(ActivityThread.currentPackageName(), messageUri, statusValues);
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean archiveStoredConversation(long conversationId, boolean archived) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.archiveStoredConversation(ActivityThread.currentPackageName(), conversationId, archived);
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public Uri addTextMessageDraft(String address, String text) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.addTextMessageDraft(ActivityThread.currentPackageName(), address, text);
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public Uri addMultimediaMessageDraft(Uri contentUri) {
        if (contentUri == null) {
            throw new IllegalArgumentException("Uri contentUri null");
        }
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.addMultimediaMessageDraft(ActivityThread.currentPackageName(), contentUri);
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public void sendStoredTextMessage(final Uri messageUri, final String scAddress, final PendingIntent sentIntent, final PendingIntent deliveryIntent) {
        if (messageUri == null) {
            throw new IllegalArgumentException("Empty message URI");
        }
        final Context context = ActivityThread.currentApplication().getApplicationContext();
        resolveSubscriptionForOperation(new SubscriptionResolverResult() { // from class: android.telephony.SmsManager.7
            @Override // android.telephony.SmsManager.SubscriptionResolverResult
            public void onSuccess(int subId) {
                try {
                    ISms iSms = SmsManager.access$000();
                    iSms.sendStoredText(subId, ActivityThread.currentPackageName(), messageUri, scAddress, sentIntent, deliveryIntent);
                } catch (RemoteException e) {
                    Log.e(SmsManager.TAG, "sendStoredTextMessage: Couldn't send SMS - Exception: " + e.getMessage());
                    SmsManager.notifySmsGenericError(sentIntent);
                }
            }

            @Override // android.telephony.SmsManager.SubscriptionResolverResult
            public void onFailure() {
                SmsManager.notifySmsErrorNoDefaultSet(context, sentIntent);
            }
        });
    }

    public void sendStoredMultipartTextMessage(final Uri messageUri, final String scAddress, final ArrayList<PendingIntent> sentIntents, final ArrayList<PendingIntent> deliveryIntents) {
        if (messageUri == null) {
            throw new IllegalArgumentException("Empty message URI");
        }
        final Context context = ActivityThread.currentApplication().getApplicationContext();
        resolveSubscriptionForOperation(new SubscriptionResolverResult() { // from class: android.telephony.SmsManager.8
            @Override // android.telephony.SmsManager.SubscriptionResolverResult
            public void onSuccess(int subId) {
                try {
                    ISms iSms = SmsManager.access$000();
                    iSms.sendStoredMultipartText(subId, ActivityThread.currentPackageName(), messageUri, scAddress, sentIntents, deliveryIntents);
                } catch (RemoteException e) {
                    Log.e(SmsManager.TAG, "sendStoredTextMessage: Couldn't send SMS - Exception: " + e.getMessage());
                    SmsManager.notifySmsGenericError(sentIntents);
                }
            }

            @Override // android.telephony.SmsManager.SubscriptionResolverResult
            public void onFailure() {
                SmsManager.notifySmsErrorNoDefaultSet(context, sentIntents);
            }
        });
    }

    public void sendStoredMultimediaMessage(Uri messageUri, Bundle configOverrides, PendingIntent sentIntent) {
        if (messageUri == null) {
            throw new IllegalArgumentException("Empty message URI");
        }
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                iMms.sendStoredMessage(getSubscriptionId(), ActivityThread.currentPackageName(), messageUri, configOverrides, sentIntent);
            }
        } catch (RemoteException e) {
        }
    }

    public void setAutoPersisting(boolean enabled) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                iMms.setAutoPersisting(ActivityThread.currentPackageName(), enabled);
            }
        } catch (RemoteException e) {
        }
    }

    public boolean getAutoPersisting() {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.getAutoPersisting();
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public Bundle getCarrierConfigValues() {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                return iMms.getCarrierConfigValues(getSubscriptionId());
            }
            return null;
        } catch (RemoteException e) {
            return null;
        }
    }

    public String createAppSpecificSmsToken(PendingIntent intent) {
        try {
            ISms iccSms = getISmsServiceOrThrow();
            return iccSms.createAppSpecificSmsToken(getSubscriptionId(), ActivityThread.currentPackageName(), intent);
        } catch (RemoteException ex) {
            ex.rethrowFromSystemServer();
            return null;
        }
    }

    public void getSmsMessagesForFinancialApp(Bundle params, Executor executor, FinancialSmsCallback callback) {
        try {
            ISms iccSms = getISmsServiceOrThrow();
            iccSms.getSmsMessagesForFinancialApp(getSubscriptionId(), ActivityThread.currentPackageName(), params, new AnonymousClass9(executor, callback));
        } catch (RemoteException ex) {
            ex.rethrowFromSystemServer();
        }
    }

    /* renamed from: android.telephony.SmsManager$9  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass9 extends IFinancialSmsCallback.Stub {
        final /* synthetic */ FinancialSmsCallback val$callback;
        final /* synthetic */ Executor val$executor;

        AnonymousClass9(Executor executor, FinancialSmsCallback financialSmsCallback) {
            this.val$executor = executor;
            this.val$callback = financialSmsCallback;
        }

        @Override // android.telephony.IFinancialSmsCallback
        public void onGetSmsMessagesForFinancialApp(final CursorWindow msgs) {
            final Executor executor = this.val$executor;
            final FinancialSmsCallback financialSmsCallback = this.val$callback;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$SmsManager$9$rvckWwRKQKxMC1PhWEkHayc_gf8
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    executor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$SmsManager$9$Ma-xGOTcrGGV8QvZI0NSA6WUbKA
                        @Override // java.lang.Runnable
                        public final void run() {
                            SmsManager.FinancialSmsCallback.this.onFinancialSmsMessages(r2);
                        }
                    });
                }
            });
        }
    }

    public String createAppSpecificSmsTokenWithPackageInfo(String prefixes, PendingIntent intent) {
        try {
            ISms iccSms = getISmsServiceOrThrow();
            return iccSms.createAppSpecificSmsTokenWithPackageInfo(getSubscriptionId(), ActivityThread.currentPackageName(), prefixes, intent);
        } catch (RemoteException ex) {
            ex.rethrowFromSystemServer();
            return null;
        }
    }

    public static Bundle getMmsConfig(BaseBundle config) {
        Bundle filtered = new Bundle();
        filtered.putBoolean("enabledTransID", config.getBoolean("enabledTransID"));
        filtered.putBoolean("enabledMMS", config.getBoolean("enabledMMS"));
        filtered.putBoolean("enableGroupMms", config.getBoolean("enableGroupMms"));
        filtered.putBoolean("enabledNotifyWapMMSC", config.getBoolean("enabledNotifyWapMMSC"));
        filtered.putBoolean("aliasEnabled", config.getBoolean("aliasEnabled"));
        filtered.putBoolean("allowAttachAudio", config.getBoolean("allowAttachAudio"));
        filtered.putBoolean("enableMultipartSMS", config.getBoolean("enableMultipartSMS"));
        filtered.putBoolean("enableSMSDeliveryReports", config.getBoolean("enableSMSDeliveryReports"));
        filtered.putBoolean("supportMmsContentDisposition", config.getBoolean("supportMmsContentDisposition"));
        filtered.putBoolean("sendMultipartSmsAsSeparateMessages", config.getBoolean("sendMultipartSmsAsSeparateMessages"));
        filtered.putBoolean("enableMMSReadReports", config.getBoolean("enableMMSReadReports"));
        filtered.putBoolean("enableMMSDeliveryReports", config.getBoolean("enableMMSDeliveryReports"));
        filtered.putBoolean("mmsCloseConnection", config.getBoolean("mmsCloseConnection"));
        filtered.putInt("maxMessageSize", config.getInt("maxMessageSize"));
        filtered.putInt("maxImageWidth", config.getInt("maxImageWidth"));
        filtered.putInt("maxImageHeight", config.getInt("maxImageHeight"));
        filtered.putInt("recipientLimit", config.getInt("recipientLimit"));
        filtered.putInt("aliasMinChars", config.getInt("aliasMinChars"));
        filtered.putInt("aliasMaxChars", config.getInt("aliasMaxChars"));
        filtered.putInt("smsToMmsTextThreshold", config.getInt("smsToMmsTextThreshold"));
        filtered.putInt("smsToMmsTextLengthThreshold", config.getInt("smsToMmsTextLengthThreshold"));
        filtered.putInt("maxMessageTextSize", config.getInt("maxMessageTextSize"));
        filtered.putInt("maxSubjectLength", config.getInt("maxSubjectLength"));
        filtered.putInt("httpSocketTimeout", config.getInt("httpSocketTimeout"));
        filtered.putString("uaProfTagName", config.getString("uaProfTagName"));
        filtered.putString("userAgent", config.getString("userAgent"));
        filtered.putString("uaProfUrl", config.getString("uaProfUrl"));
        filtered.putString("httpParams", config.getString("httpParams"));
        filtered.putString("emailGatewayNumber", config.getString("emailGatewayNumber"));
        filtered.putString("naiSuffix", config.getString("naiSuffix"));
        filtered.putBoolean("config_cellBroadcastAppLinks", config.getBoolean("config_cellBroadcastAppLinks"));
        filtered.putBoolean("supportHttpCharsetHeader", config.getBoolean("supportHttpCharsetHeader"));
        return filtered;
    }

    public int checkSmsShortCodeDestination(String destAddress, String countryIso) {
        try {
            ISms iccISms = getISmsServiceOrThrow();
            if (iccISms != null) {
                return iccISms.checkSmsShortCodeDestination(getSubscriptionId(), ActivityThread.currentPackageName(), destAddress, countryIso);
            }
            return 0;
        } catch (RemoteException e) {
            Log.e(TAG, "checkSmsShortCodeDestination() RemoteException", e);
            return 0;
        }
    }
}
