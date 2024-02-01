package android.telephony;

import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.hardware.contexthub.V1_0.HostEndPoint;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.telephony.IMms;
import com.android.internal.telephony.ISms;
import com.android.internal.telephony.SmsRawData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/* loaded from: classes2.dex */
public final class SmsManager {
    public static final int CDMA_SMS_RECORD_LENGTH = 255;
    public static final int CELL_BROADCAST_RAN_TYPE_CDMA = 1;
    public static final int CELL_BROADCAST_RAN_TYPE_GSM = 0;
    private static final int DEFAULT_SUBSCRIPTION_ID = -1002;
    public static final String EXTRA_MMS_DATA = "android.telephony.extra.MMS_DATA";
    public static final String EXTRA_MMS_HTTP_STATUS = "android.telephony.extra.MMS_HTTP_STATUS";
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
    public static final String MMS_CONFIG_SIM_COUNTRY_ISO_OVERRIDE = "sim_country_iso_override_string";
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
    private static final String PHONE_PACKAGE_NAME = "com.android.phone";
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
    @SystemApi
    public static final int RESULT_SYSTEM_ERROR = 15;
    private static final int SMS_PICK = 2;
    public static final int SMS_RECORD_LENGTH = 176;
    public static final int SMS_TYPE_INCOMING = 0;
    public static final int SMS_TYPE_OUTGOING = 1;
    public static final int STATUS_ON_ICC_FREE = 0;
    public static final int STATUS_ON_ICC_READ = 1;
    public static final int STATUS_ON_ICC_SENT = 5;
    public static final int STATUS_ON_ICC_UNREAD = 3;
    public static final int STATUS_ON_ICC_UNSENT = 7;
    private static final String TAG = "SmsManager";
    public protected int mSubId;
    private static final SmsManager sInstance = new SmsManager(-1002);
    private static final Object sLockObject = new Object();
    private static final Map<Integer, SmsManager> sSubInstances = new ArrayMap();
    private static String DIALOG_TYPE_KEY = "dialog_type";

    public void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, true);
    }

    private synchronized void sendTextMessageInternal(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Invalid message body");
        }
        try {
            ISms iccISms = getISmsServiceOrThrow();
            iccISms.sendTextForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage);
        } catch (RemoteException e) {
        }
    }

    @SystemApi
    public void sendTextMessageWithoutPersisting(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, false);
    }

    public synchronized void sendTextMessageWithSelfPermissions(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Invalid message body");
        }
        try {
            ISms iccISms = getISmsServiceOrThrow();
            iccISms.sendTextForSubscriberWithSelfPermissions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage);
        } catch (RemoteException e) {
        }
    }

    private protected void sendTextMessage(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, int priority, boolean expectMore, int validityPeriod) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, true, priority, expectMore, validityPeriod);
    }

    private synchronized void sendTextMessageInternal(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Invalid message body");
        }
        if (priority >= 0 && priority <= 3) {
            if (validityPeriod < 5 || validityPeriod > 635040) {
                throw new IllegalArgumentException("Invalid validity period");
            }
            try {
                ISms iccISms = getISmsServiceOrThrow();
                if (iccISms != null) {
                    iccISms.sendTextForSubscriberWithOptions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, text, sentIntent, deliveryIntent, persistMessage, priority, expectMore, validityPeriod);
                    return;
                }
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        throw new IllegalArgumentException("Invalid priority");
    }

    private protected void sendTextMessageWithoutPersisting(String destinationAddress, String scAddress, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, int priority, boolean expectMore, int validityPeriod) {
        sendTextMessageInternal(destinationAddress, scAddress, text, sentIntent, deliveryIntent, false, priority, expectMore, validityPeriod);
    }

    public void injectSmsPdu(byte[] pdu, String format, PendingIntent receivedIntent) {
        if (!format.equals("3gpp") && !format.equals("3gpp2")) {
            throw new IllegalArgumentException("Invalid pdu format. format must be either 3gpp or 3gpp2");
        }
        try {
            ISms iccISms = ISms.Stub.asInterface(ServiceManager.getService("isms"));
            if (iccISms != null) {
                iccISms.injectSmsPduForSubscriber(getSubscriptionId(), pdu, format, receivedIntent);
            }
        } catch (RemoteException e) {
        }
    }

    public ArrayList<String> divideMessage(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text is null");
        }
        return SmsMessage.fragmentText(text);
    }

    public void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, true);
    }

    private synchronized void sendMultipartTextMessageInternal(String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessage) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (parts == null || parts.size() < 1) {
            throw new IllegalArgumentException("Invalid message body");
        }
        if (parts.size() > 1) {
            try {
                ISms iccISms = getISmsServiceOrThrow();
                iccISms.sendMultipartTextForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessage);
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        PendingIntent sentIntent = null;
        PendingIntent deliveryIntent = null;
        if (sentIntents != null && sentIntents.size() > 0) {
            sentIntent = sentIntents.get(0);
        }
        if (deliveryIntents != null && deliveryIntents.size() > 0) {
            deliveryIntent = deliveryIntents.get(0);
        }
        sendTextMessage(destinationAddress, scAddress, parts.get(0), sentIntent, deliveryIntent);
    }

    @SystemApi
    public void sendMultipartTextMessageWithoutPersisting(String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, false);
    }

    private protected void sendMultipartTextMessage(String destinationAddress, String scAddress, ArrayList<String> parts, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, int priority, boolean expectMore, int validityPeriod) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, true);
    }

    private synchronized void sendMultipartTextMessageInternal(String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (parts == null || parts.size() < 1) {
            throw new IllegalArgumentException("Invalid message body");
        }
        if (priority >= 0 && priority <= 3) {
            if (validityPeriod < 5 || validityPeriod > 635040) {
                throw new IllegalArgumentException("Invalid validity period");
            }
            if (parts.size() > 1) {
                try {
                    ISms iccISms = getISmsServiceOrThrow();
                    if (iccISms != null) {
                        iccISms.sendMultipartTextForSubscriberWithOptions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, parts, sentIntents, deliveryIntents, persistMessage, priority, expectMore, validityPeriod);
                        return;
                    }
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            PendingIntent sentIntent = null;
            PendingIntent deliveryIntent = null;
            if (sentIntents != null && sentIntents.size() > 0) {
                sentIntent = sentIntents.get(0);
            }
            if (deliveryIntents != null && deliveryIntents.size() > 0) {
                deliveryIntent = deliveryIntents.get(0);
            }
            sendTextMessageInternal(destinationAddress, scAddress, parts.get(0), sentIntent, deliveryIntent, persistMessage, priority, expectMore, validityPeriod);
            return;
        }
        throw new IllegalArgumentException("Invalid priority");
    }

    public synchronized void sendMultipartTextMessageWithoutPersisting(String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, int priority, boolean expectMore, int validityPeriod) {
        sendMultipartTextMessageInternal(destinationAddress, scAddress, parts, sentIntents, deliveryIntents, false, priority, expectMore, validityPeriod);
    }

    public void sendDataMessage(String destinationAddress, String scAddress, short destinationPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Invalid message data");
        }
        try {
            ISms iccISms = getISmsServiceOrThrow();
            iccISms.sendDataForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, destinationPort & HostEndPoint.BROADCAST, data, sentIntent, deliveryIntent);
        } catch (RemoteException e) {
        }
    }

    public synchronized void sendDataMessageWithSelfPermissions(String destinationAddress, String scAddress, short destinationPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        if (TextUtils.isEmpty(destinationAddress)) {
            throw new IllegalArgumentException("Invalid destinationAddress");
        }
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Invalid message data");
        }
        try {
            ISms iccISms = getISmsServiceOrThrow();
            iccISms.sendDataForSubscriberWithSelfPermissions(getSubscriptionId(), ActivityThread.currentPackageName(), destinationAddress, scAddress, destinationPort & HostEndPoint.BROADCAST, data, sentIntent, deliveryIntent);
        } catch (RemoteException e) {
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

    private synchronized SmsManager(int subId) {
        this.mSubId = subId;
    }

    public int getSubscriptionId() {
        int subId = this.mSubId == -1002 ? getDefaultSmsSubscriptionId() : this.mSubId;
        boolean isSmsSimPickActivityNeeded = false;
        Context context = ActivityThread.currentApplication().getApplicationContext();
        try {
            ISms iccISms = getISmsService();
            if (iccISms != null) {
                isSmsSimPickActivityNeeded = iccISms.isSmsSimPickActivityNeeded(subId);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Exception in getSubscriptionId");
        }
        if (isSmsSimPickActivityNeeded) {
            Log.d(TAG, "getSubscriptionId isSmsSimPickActivityNeeded is true");
            Intent intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.sim.SimDialogActivity");
            intent.addFlags(268435456);
            intent.putExtra(DIALOG_TYPE_KEY, 2);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e2) {
                Log.e(TAG, "Unable to launch Settings application.");
            }
        }
        return subId;
    }

    private static synchronized ISms getISmsServiceOrThrow() {
        ISms iccISms = getISmsService();
        if (iccISms == null) {
            throw new UnsupportedOperationException("Sms is not supported");
        }
        return iccISms;
    }

    private static synchronized ISms getISmsService() {
        return ISms.Stub.asInterface(ServiceManager.getService("isms"));
    }

    private protected boolean copyMessageToIcc(byte[] smsc, byte[] pdu, int status) {
        if (pdu == null) {
            throw new IllegalArgumentException("pdu is NULL");
        }
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return false;
            }
            boolean success = iccISms.copyMessageToIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), status, pdu, smsc);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected boolean deleteMessageFromIcc(int messageIndex) {
        byte[] pdu = new byte[175];
        Arrays.fill(pdu, (byte) -1);
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return false;
            }
            boolean success = iccISms.updateMessageOnIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), messageIndex, 0, pdu);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected boolean updateMessageOnIcc(int messageIndex, int newStatus, byte[] pdu) {
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return false;
            }
            boolean success = iccISms.updateMessageOnIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName(), messageIndex, newStatus, pdu);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected ArrayList<SmsMessage> getAllMessagesFromIcc() {
        List<SmsRawData> records = null;
        try {
            ISms iccISms = getISmsService();
            if (iccISms != null) {
                records = iccISms.getAllMessagesFromIccEfForSubscriber(getSubscriptionId(), ActivityThread.currentPackageName());
            }
        } catch (RemoteException e) {
        }
        return createMessageListFromRawRecords(records);
    }

    public synchronized boolean enableCellBroadcast(int messageIdentifier, int ranType) {
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return false;
            }
            boolean success = iccISms.enableCellBroadcastForSubscriber(getSubscriptionId(), messageIdentifier, ranType);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    public synchronized boolean disableCellBroadcast(int messageIdentifier, int ranType) {
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return false;
            }
            boolean success = iccISms.disableCellBroadcastForSubscriber(getSubscriptionId(), messageIdentifier, ranType);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected boolean enableCellBroadcastRange(int startMessageId, int endMessageId, int ranType) {
        if (endMessageId < startMessageId) {
            throw new IllegalArgumentException("endMessageId < startMessageId");
        }
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return false;
            }
            boolean success = iccISms.enableCellBroadcastRangeForSubscriber(getSubscriptionId(), startMessageId, endMessageId, ranType);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected boolean disableCellBroadcastRange(int startMessageId, int endMessageId, int ranType) {
        if (endMessageId < startMessageId) {
            throw new IllegalArgumentException("endMessageId < startMessageId");
        }
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return false;
            }
            boolean success = iccISms.disableCellBroadcastRangeForSubscriber(getSubscriptionId(), startMessageId, endMessageId, ranType);
            return success;
        } catch (RemoteException e) {
            return false;
        }
    }

    private synchronized ArrayList<SmsMessage> createMessageListFromRawRecords(List<SmsRawData> records) {
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

    public synchronized boolean isImsSmsSupported() {
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return false;
            }
            boolean boSupported = iccISms.isImsSmsSupportedForSubscriber(getSubscriptionId());
            return boSupported;
        } catch (RemoteException e) {
            return false;
        }
    }

    public synchronized String getImsSmsFormat() {
        try {
            ISms iccISms = getISmsService();
            if (iccISms == null) {
                return "unknown";
            }
            String format = iccISms.getImsSmsFormatForSubscriber(getSubscriptionId());
            return format;
        } catch (RemoteException e) {
            return "unknown";
        }
    }

    public static int getDefaultSmsSubscriptionId() {
        try {
            ISms iccISms = ISms.Stub.asInterface(ServiceManager.getService("isms"));
            return iccISms.getPreferredSmsSubscription();
        } catch (RemoteException e) {
            return -1;
        } catch (NullPointerException e2) {
            return -1;
        }
    }

    private protected boolean isSMSPromptEnabled() {
        try {
            ISms iccISms = ISms.Stub.asInterface(ServiceManager.getService("isms"));
            return iccISms.isSMSPromptEnabled();
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

    public synchronized Uri importTextMessage(String address, int type, String text, long timestampMillis, boolean seen, boolean read) {
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

    public synchronized Uri importMultimediaMessage(Uri contentUri, String messageId, long timestampSecs, boolean seen, boolean read) {
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

    public synchronized boolean deleteStoredMessage(Uri messageUri) {
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

    public synchronized boolean deleteStoredConversation(long conversationId) {
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

    public synchronized boolean updateStoredMessageStatus(Uri messageUri, ContentValues statusValues) {
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

    public synchronized boolean archiveStoredConversation(long conversationId, boolean archived) {
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

    public synchronized Uri addTextMessageDraft(String address, String text) {
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

    public synchronized Uri addMultimediaMessageDraft(Uri contentUri) {
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

    public synchronized void sendStoredTextMessage(Uri messageUri, String scAddress, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        if (messageUri == null) {
            throw new IllegalArgumentException("Empty message URI");
        }
        try {
            ISms iccISms = getISmsServiceOrThrow();
            iccISms.sendStoredText(getSubscriptionId(), ActivityThread.currentPackageName(), messageUri, scAddress, sentIntent, deliveryIntent);
        } catch (RemoteException e) {
        }
    }

    public synchronized void sendStoredMultipartTextMessage(Uri messageUri, String scAddress, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents) {
        if (messageUri == null) {
            throw new IllegalArgumentException("Empty message URI");
        }
        try {
            ISms iccISms = getISmsServiceOrThrow();
            iccISms.sendStoredMultipartText(getSubscriptionId(), ActivityThread.currentPackageName(), messageUri, scAddress, sentIntents, deliveryIntents);
        } catch (RemoteException e) {
        }
    }

    public synchronized void sendStoredMultimediaMessage(Uri messageUri, Bundle configOverrides, PendingIntent sentIntent) {
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

    public synchronized void setAutoPersisting(boolean enabled) {
        try {
            IMms iMms = IMms.Stub.asInterface(ServiceManager.getService("imms"));
            if (iMms != null) {
                iMms.setAutoPersisting(ActivityThread.currentPackageName(), enabled);
            }
        } catch (RemoteException e) {
        }
    }

    public synchronized boolean getAutoPersisting() {
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

    public static synchronized Bundle getMmsConfig(BaseBundle config) {
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
        filtered.putString("sim_country_iso_override_string", config.getString("sim_country_iso_override_string"));
        filtered.putBoolean("config_cellBroadcastAppLinks", config.getBoolean("config_cellBroadcastAppLinks"));
        filtered.putBoolean("supportHttpCharsetHeader", config.getBoolean("supportHttpCharsetHeader"));
        return filtered;
    }
}
