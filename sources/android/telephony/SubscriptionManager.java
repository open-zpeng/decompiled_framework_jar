package android.telephony;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.BroadcastOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.INetworkPolicyManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.MediaStore;
import android.telephony.SubscriptionManager;
import android.telephony.euicc.EuiccManager;
import android.util.DisplayMetrics;
import android.util.Log;
import com.android.internal.telephony.IOnSubscriptionsChangedListener;
import com.android.internal.telephony.ISetOpportunisticDataCallback;
import com.android.internal.telephony.ISub;
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class SubscriptionManager {
    public static final String ACCESS_RULES = "access_rules";
    public static final String ACCESS_RULES_FROM_CARRIER_CONFIGS = "access_rules_from_carrier_configs";
    public static final String ACTION_DEFAULT_SMS_SUBSCRIPTION_CHANGED = "android.telephony.action.DEFAULT_SMS_SUBSCRIPTION_CHANGED";
    public static final String ACTION_DEFAULT_SUBSCRIPTION_CHANGED = "android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED";
    public static final String ACTION_MANAGE_SUBSCRIPTION_PLANS = "android.telephony.action.MANAGE_SUBSCRIPTION_PLANS";
    public static final String ACTION_REFRESH_SUBSCRIPTION_PLANS = "android.telephony.action.REFRESH_SUBSCRIPTION_PLANS";
    public static final String ACTION_SUBSCRIPTION_PLANS_CHANGED = "android.telephony.action.SUBSCRIPTION_PLANS_CHANGED";
    public static final String CARD_ID = "card_id";
    public static final String CARRIER_ID = "carrier_id";
    public static final String CARRIER_NAME = "carrier_name";
    public static final String CB_ALERT_REMINDER_INTERVAL = "alert_reminder_interval";
    public static final String CB_ALERT_SOUND_DURATION = "alert_sound_duration";
    public static final String CB_ALERT_SPEECH = "enable_alert_speech";
    public static final String CB_ALERT_VIBRATE = "enable_alert_vibrate";
    public static final String CB_AMBER_ALERT = "enable_cmas_amber_alerts";
    public static final String CB_CHANNEL_50_ALERT = "enable_channel_50_alerts";
    public static final String CB_CMAS_TEST_ALERT = "enable_cmas_test_alerts";
    public static final String CB_EMERGENCY_ALERT = "enable_emergency_alerts";
    public static final String CB_ETWS_TEST_ALERT = "enable_etws_test_alerts";
    public static final String CB_EXTREME_THREAT_ALERT = "enable_cmas_extreme_threat_alerts";
    public static final String CB_OPT_OUT_DIALOG = "show_cmas_opt_out_dialog";
    public static final String CB_SEVERE_THREAT_ALERT = "enable_cmas_severe_threat_alerts";
    public static final String COLOR = "color";
    public static final int COLOR_1 = 0;
    public static final int COLOR_2 = 1;
    public static final int COLOR_3 = 2;
    public static final int COLOR_4 = 3;
    public static final int COLOR_DEFAULT = 0;
    public static final String DATA_ENABLED_OVERRIDE_RULES = "data_enabled_override_rules";
    public static final String DATA_ROAMING = "data_roaming";
    public static final int DATA_ROAMING_DEFAULT = 0;
    public static final int DATA_ROAMING_DISABLE = 0;
    public static final int DATA_ROAMING_ENABLE = 1;
    private static final boolean DBG = false;
    public static final int DEFAULT_NAME_RES = 17039374;
    public static final int DEFAULT_PHONE_INDEX = Integer.MAX_VALUE;
    public static final int DEFAULT_SIM_SLOT_INDEX = Integer.MAX_VALUE;
    public static final int DEFAULT_SUBSCRIPTION_ID = Integer.MAX_VALUE;
    public static final String DISPLAY_NAME = "display_name";
    public static final int DISPLAY_NUMBER_DEFAULT = 1;
    public static final int DISPLAY_NUMBER_FIRST = 1;
    public static final String DISPLAY_NUMBER_FORMAT = "display_number_format";
    public static final int DISPLAY_NUMBER_LAST = 2;
    public static final int DISPLAY_NUMBER_NONE = 0;
    public static final int DUMMY_SUBSCRIPTION_ID_BASE = -2;
    public static final String EHPLMNS = "ehplmns";
    public static final String ENHANCED_4G_MODE_ENABLED = "volte_vt_enabled";
    public static final String EXTRA_SUBSCRIPTION_INDEX = "android.telephony.extra.SUBSCRIPTION_INDEX";
    public static final String GROUP_OWNER = "group_owner";
    public static final String GROUP_UUID = "group_uuid";
    public static final String HPLMNS = "hplmns";
    public static final String ICC_ID = "icc_id";
    public static final String IMSI = "imsi";
    public static final int INVALID_PHONE_INDEX = -1;
    public static final int INVALID_SIM_SLOT_INDEX = -1;
    public static final int INVALID_SUBSCRIPTION_ID = -1;
    public static final String ISO_COUNTRY_CODE = "iso_country_code";
    public static final String IS_EMBEDDED = "is_embedded";
    public static final String IS_METERED = "is_metered";
    public static final String IS_OPPORTUNISTIC = "is_opportunistic";
    public static final String IS_REMOVABLE = "is_removable";
    private static final String LOG_TAG = "SubscriptionManager";
    public static final int MAX_SUBSCRIPTION_ID_VALUE = 2147483646;
    public static final String MCC = "mcc";
    public static final String MCC_STRING = "mcc_string";
    public static final int MIN_SUBSCRIPTION_ID_VALUE = 0;
    public static final String MNC = "mnc";
    public static final String MNC_STRING = "mnc_string";
    public static final String NAME_SOURCE = "name_source";
    public static final int NAME_SOURCE_CARRIER = 3;
    public static final int NAME_SOURCE_DEFAULT_SOURCE = 0;
    public static final int NAME_SOURCE_SIM_PNN = 4;
    public static final int NAME_SOURCE_SIM_SPN = 1;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static final int NAME_SOURCE_USER_INPUT = 2;
    public static final String NUMBER = "number";
    public static final String PROFILE_CLASS = "profile_class";
    @SystemApi
    public static final int PROFILE_CLASS_DEFAULT = -1;
    @SystemApi
    public static final int PROFILE_CLASS_OPERATIONAL = 2;
    @SystemApi
    public static final int PROFILE_CLASS_PROVISIONING = 1;
    @SystemApi
    public static final int PROFILE_CLASS_TESTING = 0;
    @SystemApi
    public static final int PROFILE_CLASS_UNSET = -1;
    public static final int SIM_NOT_INSERTED = -1;
    public static final int SIM_PROVISIONED = 0;
    public static final String SIM_PROVISIONING_STATUS = "sim_provisioning_status";
    public static final String SIM_SLOT_INDEX = "sim_id";
    public static final int SLOT_INDEX_FOR_REMOTE_SIM_SUB = -1;
    public static final String SUBSCRIPTION_TYPE = "subscription_type";
    public static final int SUBSCRIPTION_TYPE_LOCAL_SIM = 0;
    public static final int SUBSCRIPTION_TYPE_REMOTE_SIM = 1;
    public static final String SUB_DEFAULT_CHANGED_ACTION = "android.intent.action.SUB_DEFAULT_CHANGED";
    public static final String UNIQUE_KEY_SUBSCRIPTION_ID = "_id";
    private static final boolean VDBG = false;
    public static final String VT_IMS_ENABLED = "vt_ims_enabled";
    public static final String WFC_IMS_ENABLED = "wfc_ims_enabled";
    public static final String WFC_IMS_MODE = "wfc_ims_mode";
    public static final String WFC_IMS_ROAMING_ENABLED = "wfc_ims_roaming_enabled";
    public static final String WFC_IMS_ROAMING_MODE = "wfc_ims_roaming_mode";
    @Deprecated
    public static final String WHITE_LISTED_APN_DATA = "white_listed_apn_data";
    private final Context mContext;
    private volatile INetworkPolicyManager mNetworkPolicy;
    @UnsupportedAppUsage
    public static final Uri CONTENT_URI = Uri.parse("content://telephony/siminfo");
    @SystemApi
    public static final Uri WFC_ENABLED_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "wfc");
    @SystemApi
    public static final Uri ADVANCED_CALLING_ENABLED_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "advanced_calling");
    @SystemApi
    public static final Uri WFC_MODE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "wfc_mode");
    @SystemApi
    public static final Uri WFC_ROAMING_MODE_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "wfc_roaming_mode");
    @SystemApi
    public static final Uri VT_ENABLED_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "vt_enabled");
    @SystemApi
    public static final Uri WFC_ROAMING_ENABLED_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "wfc_roaming_enabled");

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public interface CallISubMethodHelper {
        int callMethod(ISub iSub) throws RemoteException;
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ProfileClass {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface SimDisplayNameSource {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface SubscriptionType {
    }

    public static Uri getUriForSubscriptionId(int subscriptionId) {
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(subscriptionId));
    }

    /* loaded from: classes2.dex */
    public static class OnSubscriptionsChangedListener {
        IOnSubscriptionsChangedListener callback;
        private final Handler mHandler;

        /* loaded from: classes2.dex */
        private class OnSubscriptionsChangedListenerHandler extends Handler {
            OnSubscriptionsChangedListenerHandler() {
            }

            OnSubscriptionsChangedListenerHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                OnSubscriptionsChangedListener.this.onSubscriptionsChanged();
            }
        }

        public OnSubscriptionsChangedListener() {
            this.callback = new IOnSubscriptionsChangedListener.Stub() { // from class: android.telephony.SubscriptionManager.OnSubscriptionsChangedListener.1
                @Override // com.android.internal.telephony.IOnSubscriptionsChangedListener
                public void onSubscriptionsChanged() {
                    OnSubscriptionsChangedListener.this.mHandler.sendEmptyMessage(0);
                }
            };
            this.mHandler = new OnSubscriptionsChangedListenerHandler();
        }

        public OnSubscriptionsChangedListener(Looper looper) {
            this.callback = new IOnSubscriptionsChangedListener.Stub() { // from class: android.telephony.SubscriptionManager.OnSubscriptionsChangedListener.1
                @Override // com.android.internal.telephony.IOnSubscriptionsChangedListener
                public void onSubscriptionsChanged() {
                    OnSubscriptionsChangedListener.this.mHandler.sendEmptyMessage(0);
                }
            };
            this.mHandler = new OnSubscriptionsChangedListenerHandler(looper);
        }

        public void onSubscriptionsChanged() {
        }

        private void log(String s) {
            Rlog.d(SubscriptionManager.LOG_TAG, s);
        }
    }

    @UnsupportedAppUsage
    public SubscriptionManager(Context context) {
        this.mContext = context;
    }

    @Deprecated
    public static SubscriptionManager from(Context context) {
        return (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
    }

    private final INetworkPolicyManager getNetworkPolicy() {
        if (this.mNetworkPolicy == null) {
            this.mNetworkPolicy = INetworkPolicyManager.Stub.asInterface(ServiceManager.getService(Context.NETWORK_POLICY_SERVICE));
        }
        return this.mNetworkPolicy;
    }

    public void addOnSubscriptionsChangedListener(OnSubscriptionsChangedListener listener) {
        Context context = this.mContext;
        String pkgName = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.addOnSubscriptionsChangedListener(pkgName, listener.callback);
            }
        } catch (RemoteException ex) {
            Log.e(LOG_TAG, "Remote exception ITelephonyRegistry " + ex);
        }
    }

    public void removeOnSubscriptionsChangedListener(OnSubscriptionsChangedListener listener) {
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.removeOnSubscriptionsChangedListener(pkgForDebug, listener.callback);
            }
        } catch (RemoteException ex) {
            Log.e(LOG_TAG, "Remote exception ITelephonyRegistry " + ex);
        }
    }

    /* loaded from: classes2.dex */
    public static class OnOpportunisticSubscriptionsChangedListener {
        IOnSubscriptionsChangedListener callback = new AnonymousClass1();
        private Executor mExecutor;

        public void onOpportunisticSubscriptionsChanged() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setExecutor(Executor executor) {
            this.mExecutor = executor;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: android.telephony.SubscriptionManager$OnOpportunisticSubscriptionsChangedListener$1  reason: invalid class name */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 extends IOnSubscriptionsChangedListener.Stub {
            AnonymousClass1() {
            }

            @Override // com.android.internal.telephony.IOnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                long identity = Binder.clearCallingIdentity();
                try {
                    OnOpportunisticSubscriptionsChangedListener.this.mExecutor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$SubscriptionManager$OnOpportunisticSubscriptionsChangedListener$1$3LINuEtkXs3dEn49nQkzD0NIY3E
                        @Override // java.lang.Runnable
                        public final void run() {
                            SubscriptionManager.OnOpportunisticSubscriptionsChangedListener.AnonymousClass1.this.lambda$onSubscriptionsChanged$0$SubscriptionManager$OnOpportunisticSubscriptionsChangedListener$1();
                        }
                    });
                } finally {
                    Binder.restoreCallingIdentity(identity);
                }
            }

            public /* synthetic */ void lambda$onSubscriptionsChanged$0$SubscriptionManager$OnOpportunisticSubscriptionsChangedListener$1() {
                OnOpportunisticSubscriptionsChangedListener.this.onOpportunisticSubscriptionsChanged();
            }
        }

        private void log(String s) {
            Rlog.d(SubscriptionManager.LOG_TAG, s);
        }
    }

    public void addOnOpportunisticSubscriptionsChangedListener(Executor executor, OnOpportunisticSubscriptionsChangedListener listener) {
        if (executor == null || listener == null) {
            return;
        }
        Context context = this.mContext;
        String pkgName = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        listener.setExecutor(executor);
        try {
            ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.addOnOpportunisticSubscriptionsChangedListener(pkgName, listener.callback);
            }
        } catch (RemoteException ex) {
            Log.e(LOG_TAG, "Remote exception ITelephonyRegistry " + ex);
        }
    }

    public void removeOnOpportunisticSubscriptionsChangedListener(OnOpportunisticSubscriptionsChangedListener listener) {
        Preconditions.checkNotNull(listener, "listener cannot be null");
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        try {
            ITelephonyRegistry tr = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
            if (tr != null) {
                tr.removeOnSubscriptionsChangedListener(pkgForDebug, listener.callback);
            }
        } catch (RemoteException ex) {
            Log.e(LOG_TAG, "Remote exception ITelephonyRegistry " + ex);
        }
    }

    public SubscriptionInfo getActiveSubscriptionInfo(int subId) {
        if (!isValidSubscriptionId(subId)) {
            return null;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return null;
            }
            SubscriptionInfo subInfo = iSub.getActiveSubscriptionInfo(subId, this.mContext.getOpPackageName());
            return subInfo;
        } catch (RemoteException e) {
            return null;
        }
    }

    public SubscriptionInfo getActiveSubscriptionInfoForIccIndex(String iccId) {
        if (iccId == null) {
            logd("[getActiveSubscriptionInfoForIccIndex]- null iccid");
            return null;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return null;
            }
            SubscriptionInfo result = iSub.getActiveSubscriptionInfoForIccId(iccId, this.mContext.getOpPackageName());
            return result;
        } catch (RemoteException e) {
            return null;
        }
    }

    public SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int slotIndex) {
        if (!isValidSlotIndex(slotIndex)) {
            logd("[getActiveSubscriptionInfoForSimSlotIndex]- invalid slotIndex");
            return null;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return null;
            }
            SubscriptionInfo result = iSub.getActiveSubscriptionInfoForSimSlotIndex(slotIndex, this.mContext.getOpPackageName());
            return result;
        } catch (RemoteException e) {
            return null;
        }
    }

    @UnsupportedAppUsage
    public List<SubscriptionInfo> getAllSubscriptionInfoList() {
        List<SubscriptionInfo> result = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getAllSubInfoList(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    public List<SubscriptionInfo> getActiveSubscriptionInfoList() {
        return getActiveSubscriptionInfoList(true);
    }

    public List<SubscriptionInfo> getActiveSubscriptionInfoList(boolean userVisibleOnly) {
        List<SubscriptionInfo> activeList = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                activeList = iSub.getActiveSubscriptionInfoList(this.mContext.getOpPackageName());
            }
        } catch (RemoteException e) {
        }
        if (!userVisibleOnly || activeList == null) {
            return activeList;
        }
        return (List) activeList.stream().filter(new Predicate() { // from class: android.telephony.-$$Lambda$SubscriptionManager$R_uORt9bKcmEo6JnjiGP2KgjIOQ
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return SubscriptionManager.this.lambda$getActiveSubscriptionInfoList$0$SubscriptionManager((SubscriptionInfo) obj);
            }
        }).collect(Collectors.toList());
    }

    @SystemApi
    public List<SubscriptionInfo> getAvailableSubscriptionInfoList() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return null;
            }
            List<SubscriptionInfo> result = iSub.getAvailableSubscriptionInfoList(this.mContext.getOpPackageName());
            return result;
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<SubscriptionInfo> getAccessibleSubscriptionInfoList() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return null;
            }
            List<SubscriptionInfo> result = iSub.getAccessibleSubscriptionInfoList(this.mContext.getOpPackageName());
            return result;
        } catch (RemoteException e) {
            return null;
        }
    }

    @SystemApi
    public void requestEmbeddedSubscriptionInfoListRefresh() {
        int cardId = TelephonyManager.from(this.mContext).getCardIdForDefaultEuicc();
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.requestEmbeddedSubscriptionInfoListRefresh(cardId);
            }
        } catch (RemoteException e) {
            logd("requestEmbeddedSubscriptionInfoListFresh for card = " + cardId + " failed.");
        }
    }

    @SystemApi
    public void requestEmbeddedSubscriptionInfoListRefresh(int cardId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.requestEmbeddedSubscriptionInfoListRefresh(cardId);
            }
        } catch (RemoteException e) {
            logd("requestEmbeddedSubscriptionInfoListFresh for card = " + cardId + " failed.");
        }
    }

    @UnsupportedAppUsage
    public int getAllSubscriptionInfoCount() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return 0;
            }
            int result = iSub.getAllSubInfoCount(this.mContext.getOpPackageName());
            return result;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public int getActiveSubscriptionInfoCount() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return 0;
            }
            int result = iSub.getActiveSubInfoCount(this.mContext.getOpPackageName());
            return result;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public int getActiveSubscriptionInfoCountMax() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return 0;
            }
            int result = iSub.getActiveSubInfoCountMax();
            return result;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public Uri addSubscriptionInfoRecord(String iccId, int slotIndex) {
        if (iccId == null) {
            logd("[addSubscriptionInfoRecord]- null iccId");
        }
        if (!isValidSlotIndex(slotIndex)) {
            logd("[addSubscriptionInfoRecord]- invalid slotIndex");
        }
        addSubscriptionInfoRecord(iccId, null, slotIndex, 0);
        return null;
    }

    public void addSubscriptionInfoRecord(String uniqueId, String displayName, int slotIndex, int subscriptionType) {
        if (uniqueId == null) {
            Log.e(LOG_TAG, "[addSubscriptionInfoRecord]- uniqueId is null");
            return;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                Log.e(LOG_TAG, "[addSubscriptionInfoRecord]- ISub service is null");
                return;
            }
            int result = iSub.addSubInfo(uniqueId, displayName, slotIndex, subscriptionType);
            if (result < 0) {
                Log.e(LOG_TAG, "Adding of subscription didn't succeed: error = " + result);
                return;
            }
            logd("successfully added new subscription");
        } catch (RemoteException e) {
        }
    }

    public void removeSubscriptionInfoRecord(String uniqueId, int subscriptionType) {
        if (uniqueId == null) {
            Log.e(LOG_TAG, "[addSubscriptionInfoRecord]- uniqueId is null");
            return;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                Log.e(LOG_TAG, "[removeSubscriptionInfoRecord]- ISub service is null");
                return;
            }
            int result = iSub.removeSubInfo(uniqueId, subscriptionType);
            if (result < 0) {
                Log.e(LOG_TAG, "Removal of subscription didn't succeed: error = " + result);
                return;
            }
            logd("successfully removed subscription");
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public int setIconTint(final int tint, final int subId) {
        return setSubscriptionPropertyHelper(subId, "setIconTint", new CallISubMethodHelper() { // from class: android.telephony.-$$Lambda$SubscriptionManager$RmtPOPFQV3mOx5HejDzImseJ0Qg
            @Override // android.telephony.SubscriptionManager.CallISubMethodHelper
            public final int callMethod(ISub iSub) {
                int iconTint;
                iconTint = iSub.setIconTint(tint, subId);
                return iconTint;
            }
        });
    }

    @UnsupportedAppUsage
    public int setDisplayName(final String displayName, final int subId, final int nameSource) {
        return setSubscriptionPropertyHelper(subId, "setDisplayName", new CallISubMethodHelper() { // from class: android.telephony.-$$Lambda$SubscriptionManager$OS3WICha4HbZhTnWrKCxeu6dr6g
            @Override // android.telephony.SubscriptionManager.CallISubMethodHelper
            public final int callMethod(ISub iSub) {
                int displayNameUsingSrc;
                displayNameUsingSrc = iSub.setDisplayNameUsingSrc(displayName, subId, nameSource);
                return displayNameUsingSrc;
            }
        });
    }

    @UnsupportedAppUsage
    public int setDisplayNumber(final String number, final int subId) {
        if (number == null) {
            logd("[setDisplayNumber]- fail");
            return -1;
        }
        return setSubscriptionPropertyHelper(subId, "setDisplayNumber", new CallISubMethodHelper() { // from class: android.telephony.-$$Lambda$SubscriptionManager$3ws2BzXOcyDc-7TPZx2HIBCIjbs
            @Override // android.telephony.SubscriptionManager.CallISubMethodHelper
            public final int callMethod(ISub iSub) {
                int displayNumber;
                displayNumber = iSub.setDisplayNumber(number, subId);
                return displayNumber;
            }
        });
    }

    @UnsupportedAppUsage
    public int setDataRoaming(final int roaming, final int subId) {
        return setSubscriptionPropertyHelper(subId, "setDataRoaming", new CallISubMethodHelper() { // from class: android.telephony.-$$Lambda$SubscriptionManager$xw48SQFFAHLgpsIZZWeq63fMykw
            @Override // android.telephony.SubscriptionManager.CallISubMethodHelper
            public final int callMethod(ISub iSub) {
                int dataRoaming;
                dataRoaming = iSub.setDataRoaming(roaming, subId);
                return dataRoaming;
            }
        });
    }

    public static int getSlotIndex(int subscriptionId) {
        isValidSubscriptionId(subscriptionId);
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return -1;
            }
            int result = iSub.getSlotIndex(subscriptionId);
            return result;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int[] getSubscriptionIds(int slotIndex) {
        return getSubId(slotIndex);
    }

    @UnsupportedAppUsage
    public static int[] getSubId(int slotIndex) {
        if (!isValidSlotIndex(slotIndex)) {
            logd("[getSubId]- fail");
            return null;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return null;
            }
            int[] subId = iSub.getSubId(slotIndex);
            return subId;
        } catch (RemoteException e) {
            return null;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static int getPhoneId(int subId) {
        if (!isValidSubscriptionId(subId)) {
            return -1;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return -1;
            }
            int result = iSub.getPhoneId(subId);
            return result;
        } catch (RemoteException e) {
            return -1;
        }
    }

    private static void logd(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    private static void loge(String msg) {
        Rlog.e(LOG_TAG, msg);
    }

    public static int getDefaultSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return -1;
            }
            int subId = iSub.getDefaultSubId();
            return subId;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public static int getDefaultVoiceSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return -1;
            }
            int subId = iSub.getDefaultVoiceSubId();
            return subId;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public void setDefaultVoiceSubId(int subId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultVoiceSubId(subId);
            }
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public SubscriptionInfo getDefaultVoiceSubscriptionInfo() {
        return getActiveSubscriptionInfo(getDefaultVoiceSubscriptionId());
    }

    @UnsupportedAppUsage
    public static int getDefaultVoicePhoneId() {
        return getPhoneId(getDefaultVoiceSubscriptionId());
    }

    public static int getDefaultSmsSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return -1;
            }
            int subId = iSub.getDefaultSmsSubId();
            return subId;
        } catch (RemoteException e) {
            return -1;
        }
    }

    @SystemApi
    public void setDefaultSmsSubId(int subscriptionId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultSmsSubId(subscriptionId);
            }
        } catch (RemoteException ex) {
            ex.rethrowFromSystemServer();
        }
    }

    public SubscriptionInfo getDefaultSmsSubscriptionInfo() {
        return getActiveSubscriptionInfo(getDefaultSmsSubscriptionId());
    }

    @UnsupportedAppUsage
    public int getDefaultSmsPhoneId() {
        return getPhoneId(getDefaultSmsSubscriptionId());
    }

    public static int getDefaultDataSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return -1;
            }
            int subId = iSub.getDefaultDataSubId();
            return subId;
        } catch (RemoteException e) {
            return -1;
        }
    }

    @SystemApi
    public void setDefaultDataSubId(int subscriptionId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setDefaultDataSubId(subscriptionId);
            }
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public SubscriptionInfo getDefaultDataSubscriptionInfo() {
        return getActiveSubscriptionInfo(getDefaultDataSubscriptionId());
    }

    @UnsupportedAppUsage
    public int getDefaultDataPhoneId() {
        return getPhoneId(getDefaultDataSubscriptionId());
    }

    public void clearSubscriptionInfo() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.clearSubInfo();
            }
        } catch (RemoteException e) {
        }
    }

    public boolean allDefaultsSelected() {
        return isValidSubscriptionId(getDefaultDataSubscriptionId()) && isValidSubscriptionId(getDefaultSmsSubscriptionId()) && isValidSubscriptionId(getDefaultVoiceSubscriptionId());
    }

    public static boolean isValidSubscriptionId(int subscriptionId) {
        return subscriptionId > -1;
    }

    public static boolean isUsableSubscriptionId(int subscriptionId) {
        return isUsableSubIdValue(subscriptionId);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static boolean isUsableSubIdValue(int subId) {
        return subId >= 0 && subId <= 2147483646;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static boolean isValidSlotIndex(int slotIndex) {
        return slotIndex >= 0 && slotIndex < TelephonyManager.getDefault().getSimCount();
    }

    @UnsupportedAppUsage
    public static boolean isValidPhoneId(int phoneId) {
        return phoneId >= 0 && phoneId < TelephonyManager.getDefault().getPhoneCount();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static void putPhoneIdAndSubIdExtra(Intent intent, int phoneId) {
        int[] subIds = getSubId(phoneId);
        if (subIds != null && subIds.length > 0) {
            putPhoneIdAndSubIdExtra(intent, phoneId, subIds[0]);
            return;
        }
        logd("putPhoneIdAndSubIdExtra: no valid subs");
        intent.putExtra("phone", phoneId);
    }

    @UnsupportedAppUsage
    public static void putPhoneIdAndSubIdExtra(Intent intent, int phoneId, int subId) {
        intent.putExtra(PhoneConstants.SUBSCRIPTION_KEY, subId);
        intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", subId);
        intent.putExtra("phone", phoneId);
    }

    @UnsupportedAppUsage
    public int[] getActiveSubscriptionIdList() {
        return getActiveSubscriptionIdList(true);
    }

    public int[] getActiveSubscriptionIdList(boolean visibleOnly) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                int[] subId = iSub.getActiveSubIdList(visibleOnly);
                if (subId != null) {
                    return subId;
                }
            }
        } catch (RemoteException e) {
        }
        return new int[0];
    }

    public boolean isNetworkRoaming(int subId) {
        int phoneId = getPhoneId(subId);
        if (phoneId < 0) {
            return false;
        }
        return TelephonyManager.getDefault().isNetworkRoaming(subId);
    }

    public static int getSimStateForSlotIndex(int slotIndex) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return 0;
            }
            int simState = iSub.getSimStateForSlotIndex(slotIndex);
            return simState;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public static void setSubscriptionProperty(int subId, String propKey, String propValue) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.setSubscriptionProperty(subId, propKey, propValue);
            }
        } catch (RemoteException e) {
        }
    }

    private static String getSubscriptionProperty(int subId, String propKey, Context context) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return null;
            }
            String resultValue = iSub.getSubscriptionProperty(subId, propKey, context.getOpPackageName());
            return resultValue;
        } catch (RemoteException e) {
            return null;
        }
    }

    public static boolean getBooleanSubscriptionProperty(int subId, String propKey, boolean defValue, Context context) {
        String result = getSubscriptionProperty(subId, propKey, context);
        if (result != null) {
            try {
                return Integer.parseInt(result) == 1;
            } catch (NumberFormatException e) {
                logd("getBooleanSubscriptionProperty NumberFormat exception");
            }
        }
        return defValue;
    }

    public static int getIntegerSubscriptionProperty(int subId, String propKey, int defValue, Context context) {
        String result = getSubscriptionProperty(subId, propKey, context);
        if (result != null) {
            try {
                return Integer.parseInt(result);
            } catch (NumberFormatException e) {
                logd("getBooleanSubscriptionProperty NumberFormat exception");
            }
        }
        return defValue;
    }

    @UnsupportedAppUsage
    public static Resources getResourcesForSubId(Context context, int subId) {
        return getResourcesForSubId(context, subId, false);
    }

    public static Resources getResourcesForSubId(Context context, int subId, boolean useRootLocale) {
        SubscriptionInfo subInfo = from(context).getActiveSubscriptionInfo(subId);
        Configuration config = context.getResources().getConfiguration();
        Configuration newConfig = new Configuration();
        newConfig.setTo(config);
        if (subInfo != null) {
            newConfig.mcc = subInfo.getMcc();
            newConfig.mnc = subInfo.getMnc();
            if (newConfig.mnc == 0) {
                newConfig.mnc = 65535;
            }
        }
        if (useRootLocale) {
            newConfig.setLocale(Locale.ROOT);
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        DisplayMetrics newMetrics = new DisplayMetrics();
        newMetrics.setTo(metrics);
        return new Resources(context.getResources().getAssets(), newMetrics, newConfig);
    }

    public boolean isActiveSubscriptionId(int subscriptionId) {
        return isActiveSubId(subscriptionId);
    }

    @UnsupportedAppUsage
    public boolean isActiveSubId(int subId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.isActiveSubId(subId, this.mContext.getOpPackageName());
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    public List<SubscriptionPlan> getSubscriptionPlans(int subId) {
        try {
            SubscriptionPlan[] subscriptionPlans = getNetworkPolicy().getSubscriptionPlans(subId, this.mContext.getOpPackageName());
            return subscriptionPlans == null ? Collections.emptyList() : Arrays.asList(subscriptionPlans);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSubscriptionPlans(int subId, List<SubscriptionPlan> plans) {
        try {
            getNetworkPolicy().setSubscriptionPlans(subId, (SubscriptionPlan[]) plans.toArray(new SubscriptionPlan[plans.size()]), this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private String getSubscriptionPlansOwner(int subId) {
        try {
            return getNetworkPolicy().getSubscriptionPlansOwner(subId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSubscriptionOverrideUnmetered(int subId, boolean overrideUnmetered, long timeoutMillis) {
        int overrideValue = overrideUnmetered ? 1 : 0;
        try {
            getNetworkPolicy().setSubscriptionOverride(subId, 1, overrideValue, timeoutMillis, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setSubscriptionOverrideCongested(int subId, boolean overrideCongested, long timeoutMillis) {
        int overrideValue = overrideCongested ? 2 : 0;
        try {
            getNetworkPolicy().setSubscriptionOverride(subId, 2, overrideValue, timeoutMillis, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Intent createManageSubscriptionIntent(int subId) {
        String owner = getSubscriptionPlansOwner(subId);
        if (owner == null) {
            return null;
        }
        List<SubscriptionPlan> plans = getSubscriptionPlans(subId);
        if (plans.isEmpty()) {
            return null;
        }
        Intent intent = new Intent(ACTION_MANAGE_SUBSCRIPTION_PLANS);
        intent.setPackage(owner);
        intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", subId);
        if (this.mContext.getPackageManager().queryIntentActivities(intent, 65536).isEmpty()) {
            return null;
        }
        return intent;
    }

    private Intent createRefreshSubscriptionIntent(int subId) {
        String owner = getSubscriptionPlansOwner(subId);
        if (owner == null) {
            return null;
        }
        List<SubscriptionPlan> plans = getSubscriptionPlans(subId);
        if (plans.isEmpty()) {
            return null;
        }
        Intent intent = new Intent(ACTION_REFRESH_SUBSCRIPTION_PLANS);
        intent.addFlags(268435456);
        intent.setPackage(owner);
        intent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", subId);
        if (this.mContext.getPackageManager().queryBroadcastReceivers(intent, 0).isEmpty()) {
            return null;
        }
        return intent;
    }

    public boolean isSubscriptionPlansRefreshSupported(int subId) {
        return createRefreshSubscriptionIntent(subId) != null;
    }

    public void requestSubscriptionPlansRefresh(int subId) {
        Intent intent = createRefreshSubscriptionIntent(subId);
        BroadcastOptions options = BroadcastOptions.makeBasic();
        options.setTemporaryAppWhitelistDuration(TimeUnit.MINUTES.toMillis(1L));
        this.mContext.sendBroadcast(intent, (String) null, options.toBundle());
    }

    public boolean canManageSubscription(SubscriptionInfo info) {
        return canManageSubscription(info, this.mContext.getPackageName());
    }

    public boolean canManageSubscription(SubscriptionInfo info, String packageName) {
        if (info == null || info.getAllAccessRules() == null) {
            return false;
        }
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 134217728);
            for (UiccAccessRule rule : info.getAllAccessRules()) {
                if (rule.getCarrierPrivilegeStatus(packageInfo) == 1) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            logd("Unknown package: " + packageName);
            return false;
        }
    }

    @SystemApi
    public void setPreferredDataSubscriptionId(int subId, boolean needValidation, Executor executor, Consumer<Integer> callback) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return;
            }
            ISetOpportunisticDataCallback callbackStub = new AnonymousClass1(executor, callback);
            iSub.setPreferredDataSubscriptionId(subId, needValidation, callbackStub);
        } catch (RemoteException e) {
        }
    }

    /* renamed from: android.telephony.SubscriptionManager$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends ISetOpportunisticDataCallback.Stub {
        final /* synthetic */ Consumer val$callback;
        final /* synthetic */ Executor val$executor;

        AnonymousClass1(Executor executor, Consumer consumer) {
            this.val$executor = executor;
            this.val$callback = consumer;
        }

        @Override // com.android.internal.telephony.ISetOpportunisticDataCallback
        public void onComplete(final int result) {
            final Consumer consumer;
            final Executor executor = this.val$executor;
            if (executor == null || (consumer = this.val$callback) == null) {
                return;
            }
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.telephony.-$$Lambda$SubscriptionManager$1$qFZ-q9KyfPAkHTrQPCRyO6OQ_pc
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    executor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$SubscriptionManager$1$oi86t06gqdSgTtWgRmCc5dJIfEs
                        @Override // java.lang.Runnable
                        public final void run() {
                            r1.accept(Integer.valueOf(r2));
                        }
                    });
                }
            });
        }
    }

    public int getPreferredDataSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return Integer.MAX_VALUE;
            }
            int preferredSubId = iSub.getPreferredDataSubscriptionId();
            return preferredSubId;
        } catch (RemoteException e) {
            return Integer.MAX_VALUE;
        }
    }

    public List<SubscriptionInfo> getOpportunisticSubscriptions() {
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        List<SubscriptionInfo> subInfoList = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                subInfoList = iSub.getOpportunisticSubscriptions(pkgForDebug);
            }
        } catch (RemoteException e) {
        }
        if (subInfoList == null) {
            return new ArrayList<>();
        }
        return subInfoList;
    }

    public void switchToSubscription(int subId, PendingIntent callbackIntent) {
        Preconditions.checkNotNull(callbackIntent, "callbackIntent cannot be null");
        EuiccManager euiccManager = new EuiccManager(this.mContext);
        euiccManager.switchToSubscription(subId, callbackIntent);
    }

    public boolean setOpportunistic(final boolean opportunistic, final int subId) {
        return setSubscriptionPropertyHelper(subId, "setOpportunistic", new CallISubMethodHelper() { // from class: android.telephony.-$$Lambda$SubscriptionManager$NazcIP1h3U0cfnY--L174e3u4tk
            @Override // android.telephony.SubscriptionManager.CallISubMethodHelper
            public final int callMethod(ISub iSub) {
                return SubscriptionManager.this.lambda$setOpportunistic$5$SubscriptionManager(opportunistic, subId, iSub);
            }
        }) == 1;
    }

    public /* synthetic */ int lambda$setOpportunistic$5$SubscriptionManager(boolean opportunistic, int subId, ISub iSub) throws RemoteException {
        return iSub.setOpportunistic(opportunistic, subId, this.mContext.getOpPackageName());
    }

    public ParcelUuid createSubscriptionGroup(List<Integer> subIdList) {
        Preconditions.checkNotNull(subIdList, "can't create group for null subId list");
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        ParcelUuid groupUuid = null;
        int[] subIdArray = subIdList.stream().mapToInt(new ToIntFunction() { // from class: android.telephony.-$$Lambda$SubscriptionManager$W41XrJh1c8ZX_i9kWtj1rBU9l8o
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                int intValue;
                intValue = ((Integer) obj).intValue();
                return intValue;
            }
        }).toArray();
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                groupUuid = iSub.createSubscriptionGroup(subIdArray, pkgForDebug);
            } else if (!isSystemProcess()) {
                throw new IllegalStateException("telephony service is null.");
            }
        } catch (RemoteException ex) {
            loge("createSubscriptionGroup RemoteException " + ex);
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
        return groupUuid;
    }

    public void addSubscriptionsIntoGroup(List<Integer> subIdList, ParcelUuid groupUuid) {
        Preconditions.checkNotNull(subIdList, "subIdList can't be null.");
        Preconditions.checkNotNull(groupUuid, "groupUuid can't be null.");
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        int[] subIdArray = subIdList.stream().mapToInt(new ToIntFunction() { // from class: android.telephony.-$$Lambda$SubscriptionManager$D5_PmvQ13e0qLtSnBvNd4R7l2qA
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                int intValue;
                intValue = ((Integer) obj).intValue();
                return intValue;
            }
        }).toArray();
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.addSubscriptionsIntoGroup(subIdArray, groupUuid, pkgForDebug);
            } else if (!isSystemProcess()) {
                throw new IllegalStateException("telephony service is null.");
            }
        } catch (RemoteException ex) {
            loge("addSubscriptionsIntoGroup RemoteException " + ex);
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
    }

    private boolean isSystemProcess() {
        return Process.myUid() == 1000;
    }

    public void removeSubscriptionsFromGroup(List<Integer> subIdList, ParcelUuid groupUuid) {
        Preconditions.checkNotNull(subIdList, "subIdList can't be null.");
        Preconditions.checkNotNull(groupUuid, "groupUuid can't be null.");
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        int[] subIdArray = subIdList.stream().mapToInt(new ToIntFunction() { // from class: android.telephony.-$$Lambda$SubscriptionManager$EEe2NsDpuDogw8-UijVBhj7Vuhk
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                int intValue;
                intValue = ((Integer) obj).intValue();
                return intValue;
            }
        }).toArray();
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                iSub.removeSubscriptionsFromGroup(subIdArray, groupUuid, pkgForDebug);
            } else if (!isSystemProcess()) {
                throw new IllegalStateException("telephony service is null.");
            }
        } catch (RemoteException ex) {
            loge("removeSubscriptionsFromGroup RemoteException " + ex);
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
    }

    public List<SubscriptionInfo> getSubscriptionsInGroup(ParcelUuid groupUuid) {
        Preconditions.checkNotNull(groupUuid, "groupUuid can't be null");
        Context context = this.mContext;
        String pkgForDebug = context != null ? context.getOpPackageName() : MediaStore.UNKNOWN_STRING;
        List<SubscriptionInfo> result = null;
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                result = iSub.getSubscriptionsInGroup(groupUuid, pkgForDebug);
            } else if (!isSystemProcess()) {
                throw new IllegalStateException("telephony service is null.");
            }
        } catch (RemoteException ex) {
            loge("removeSubscriptionsFromGroup RemoteException " + ex);
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
            }
        }
        return result;
    }

    /* renamed from: isSubscriptionVisible */
    public boolean lambda$getActiveSubscriptionInfoList$0$SubscriptionManager(SubscriptionInfo info) {
        if (info == null) {
            return false;
        }
        if (info.getGroupUuid() == null || !info.isOpportunistic()) {
            return true;
        }
        return TelephonyManager.from(this.mContext).hasCarrierPrivileges(info.getSubscriptionId()) || canManageSubscription(info);
    }

    public List<SubscriptionInfo> getSelectableSubscriptionInfoList() {
        List<SubscriptionInfo> availableList = getAvailableSubscriptionInfoList();
        if (availableList == null) {
            return null;
        }
        List<SubscriptionInfo> selectableList = new ArrayList<>();
        Map<ParcelUuid, SubscriptionInfo> groupMap = new HashMap<>();
        for (SubscriptionInfo info : availableList) {
            if (lambda$getActiveSubscriptionInfoList$0$SubscriptionManager(info)) {
                ParcelUuid groupUuid = info.getGroupUuid();
                if (groupUuid == null) {
                    selectableList.add(info);
                } else if (!groupMap.containsKey(groupUuid) || (groupMap.get(groupUuid).getSimSlotIndex() == -1 && info.getSimSlotIndex() != -1)) {
                    selectableList.remove(groupMap.get(groupUuid));
                    selectableList.add(info);
                    groupMap.put(groupUuid, info);
                }
            }
        }
        return selectableList;
    }

    @SystemApi
    public boolean setSubscriptionEnabled(int subscriptionId, boolean enable) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.setSubscriptionEnabled(enable, subscriptionId);
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    @SystemApi
    public boolean isSubscriptionEnabled(int subscriptionId) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.isSubscriptionEnabled(subscriptionId);
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    @SystemApi
    public int getEnabledSubscriptionId(int slotIndex) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return -1;
            }
            int subId = iSub.getEnabledSubscriptionId(slotIndex);
            return subId;
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean setAlwaysAllowMmsData(int subId, boolean alwaysAllow) {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.setAlwaysAllowMmsData(subId, alwaysAllow);
            }
            return false;
        } catch (RemoteException ex) {
            if (!isSystemProcess()) {
                ex.rethrowAsRuntimeException();
                return false;
            }
            return false;
        }
    }

    private int setSubscriptionPropertyHelper(int subId, String methodName, CallISubMethodHelper helper) {
        if (!isValidSubscriptionId(subId)) {
            logd("[" + methodName + "]- fail");
            return -1;
        }
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub == null) {
                return 0;
            }
            int result = helper.callMethod(iSub);
            return result;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public static int getActiveDataSubscriptionId() {
        try {
            ISub iSub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
            if (iSub != null) {
                return iSub.getActiveDataSubscriptionId();
            }
            return -1;
        } catch (RemoteException e) {
            return -1;
        }
    }
}
