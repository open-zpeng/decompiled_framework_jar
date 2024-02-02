package android.telephony.data;

import android.app.Service;
import android.content.Intent;
import android.net.LinkProperties;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.Rlog;
import android.telephony.data.IDataService;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public abstract class DataService extends Service {
    private static final int DATA_SERVICE_CREATE_DATA_SERVICE_PROVIDER = 1;
    public static final String DATA_SERVICE_EXTRA_SLOT_ID = "android.telephony.data.extra.SLOT_ID";
    private static final int DATA_SERVICE_INDICATION_DATA_CALL_LIST_CHANGED = 11;
    public static final String DATA_SERVICE_INTERFACE = "android.telephony.data.DataService";
    private static final int DATA_SERVICE_REMOVE_ALL_DATA_SERVICE_PROVIDERS = 3;
    private static final int DATA_SERVICE_REMOVE_DATA_SERVICE_PROVIDER = 2;
    private static final int DATA_SERVICE_REQUEST_DEACTIVATE_DATA_CALL = 5;
    private static final int DATA_SERVICE_REQUEST_GET_DATA_CALL_LIST = 8;
    private static final int DATA_SERVICE_REQUEST_REGISTER_DATA_CALL_LIST_CHANGED = 9;
    private static final int DATA_SERVICE_REQUEST_SETUP_DATA_CALL = 4;
    private static final int DATA_SERVICE_REQUEST_SET_DATA_PROFILE = 7;
    private static final int DATA_SERVICE_REQUEST_SET_INITIAL_ATTACH_APN = 6;
    private static final int DATA_SERVICE_REQUEST_UNREGISTER_DATA_CALL_LIST_CHANGED = 10;
    public static final int REQUEST_REASON_HANDOVER = 3;
    public static final int REQUEST_REASON_NORMAL = 1;
    public static final int REQUEST_REASON_SHUTDOWN = 2;
    private static final String TAG = DataService.class.getSimpleName();
    private final DataServiceHandler mHandler;
    private final SparseArray<DataServiceProvider> mServiceMap = new SparseArray<>();
    @VisibleForTesting
    public final IDataServiceWrapper mBinder = new IDataServiceWrapper();
    private final HandlerThread mHandlerThread = new HandlerThread(TAG);

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface DeactivateDataReason {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface SetupDataReason {
    }

    public abstract synchronized DataServiceProvider createDataServiceProvider(int i);

    /* loaded from: classes2.dex */
    public class DataServiceProvider {
        private final List<IDataServiceCallback> mDataCallListChangedCallbacks = new ArrayList();
        private final int mSlotId;

        public DataServiceProvider(int slotId) {
            this.mSlotId = slotId;
        }

        public final synchronized int getSlotId() {
            return this.mSlotId;
        }

        public synchronized void setupDataCall(int accessNetworkType, DataProfile dataProfile, boolean isRoaming, boolean allowRoaming, int reason, LinkProperties linkProperties, DataServiceCallback callback) {
            callback.onSetupDataCallComplete(1, null);
        }

        public synchronized void deactivateDataCall(int cid, int reason, DataServiceCallback callback) {
            callback.onDeactivateDataCallComplete(1);
        }

        public synchronized void setInitialAttachApn(DataProfile dataProfile, boolean isRoaming, DataServiceCallback callback) {
            callback.onSetInitialAttachApnComplete(1);
        }

        public synchronized void setDataProfile(List<DataProfile> dps, boolean isRoaming, DataServiceCallback callback) {
            callback.onSetDataProfileComplete(1);
        }

        public synchronized void getDataCallList(DataServiceCallback callback) {
            callback.onGetDataCallListComplete(1, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void registerForDataCallListChanged(IDataServiceCallback callback) {
            synchronized (this.mDataCallListChangedCallbacks) {
                this.mDataCallListChangedCallbacks.add(callback);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void unregisterForDataCallListChanged(IDataServiceCallback callback) {
            synchronized (this.mDataCallListChangedCallbacks) {
                this.mDataCallListChangedCallbacks.remove(callback);
            }
        }

        public final synchronized void notifyDataCallListChanged(List<DataCallResponse> dataCallList) {
            synchronized (this.mDataCallListChangedCallbacks) {
                for (IDataServiceCallback callback : this.mDataCallListChangedCallbacks) {
                    DataService.this.mHandler.obtainMessage(11, this.mSlotId, 0, new DataCallListChangedIndication(dataCallList, callback)).sendToTarget();
                }
            }
        }

        protected synchronized void onDestroy() {
            this.mDataCallListChangedCallbacks.clear();
        }
    }

    /* loaded from: classes2.dex */
    private static final class SetupDataCallRequest {
        public final int accessNetworkType;
        public final boolean allowRoaming;
        public final IDataServiceCallback callback;
        public final DataProfile dataProfile;
        public final boolean isRoaming;
        public final LinkProperties linkProperties;
        public final int reason;

        synchronized SetupDataCallRequest(int accessNetworkType, DataProfile dataProfile, boolean isRoaming, boolean allowRoaming, int reason, LinkProperties linkProperties, IDataServiceCallback callback) {
            this.accessNetworkType = accessNetworkType;
            this.dataProfile = dataProfile;
            this.isRoaming = isRoaming;
            this.allowRoaming = allowRoaming;
            this.linkProperties = linkProperties;
            this.reason = reason;
            this.callback = callback;
        }
    }

    /* loaded from: classes2.dex */
    private static final class DeactivateDataCallRequest {
        public final IDataServiceCallback callback;
        public final int cid;
        public final int reason;

        synchronized DeactivateDataCallRequest(int cid, int reason, IDataServiceCallback callback) {
            this.cid = cid;
            this.reason = reason;
            this.callback = callback;
        }
    }

    /* loaded from: classes2.dex */
    private static final class SetInitialAttachApnRequest {
        public final IDataServiceCallback callback;
        public final DataProfile dataProfile;
        public final boolean isRoaming;

        synchronized SetInitialAttachApnRequest(DataProfile dataProfile, boolean isRoaming, IDataServiceCallback callback) {
            this.dataProfile = dataProfile;
            this.isRoaming = isRoaming;
            this.callback = callback;
        }
    }

    /* loaded from: classes2.dex */
    private static final class SetDataProfileRequest {
        public final IDataServiceCallback callback;
        public final List<DataProfile> dps;
        public final boolean isRoaming;

        synchronized SetDataProfileRequest(List<DataProfile> dps, boolean isRoaming, IDataServiceCallback callback) {
            this.dps = dps;
            this.isRoaming = isRoaming;
            this.callback = callback;
        }
    }

    /* loaded from: classes2.dex */
    private static final class DataCallListChangedIndication {
        public final IDataServiceCallback callback;
        public final List<DataCallResponse> dataCallList;

        synchronized DataCallListChangedIndication(List<DataCallResponse> dataCallList, IDataServiceCallback callback) {
            this.dataCallList = dataCallList;
            this.callback = callback;
        }
    }

    /* loaded from: classes2.dex */
    private class DataServiceHandler extends Handler {
        DataServiceHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            DataServiceCallback dataServiceCallback;
            int slotId = message.arg1;
            DataServiceProvider serviceProvider = (DataServiceProvider) DataService.this.mServiceMap.get(slotId);
            switch (message.what) {
                case 1:
                    DataServiceProvider serviceProvider2 = DataService.this.createDataServiceProvider(message.arg1);
                    if (serviceProvider2 != null) {
                        DataService.this.mServiceMap.put(slotId, serviceProvider2);
                        return;
                    }
                    return;
                case 2:
                    if (serviceProvider != null) {
                        serviceProvider.onDestroy();
                        DataService.this.mServiceMap.remove(slotId);
                        return;
                    }
                    return;
                case 3:
                    for (int i = 0; i < DataService.this.mServiceMap.size(); i++) {
                        DataServiceProvider serviceProvider3 = (DataServiceProvider) DataService.this.mServiceMap.get(i);
                        if (serviceProvider3 != null) {
                            serviceProvider3.onDestroy();
                        }
                    }
                    DataService.this.mServiceMap.clear();
                    return;
                case 4:
                    if (serviceProvider != null) {
                        SetupDataCallRequest setupDataCallRequest = (SetupDataCallRequest) message.obj;
                        int i2 = setupDataCallRequest.accessNetworkType;
                        DataProfile dataProfile = setupDataCallRequest.dataProfile;
                        boolean z = setupDataCallRequest.isRoaming;
                        boolean z2 = setupDataCallRequest.allowRoaming;
                        int i3 = setupDataCallRequest.reason;
                        LinkProperties linkProperties = setupDataCallRequest.linkProperties;
                        if (setupDataCallRequest.callback == null) {
                            dataServiceCallback = null;
                        } else {
                            dataServiceCallback = new DataServiceCallback(setupDataCallRequest.callback);
                        }
                        serviceProvider.setupDataCall(i2, dataProfile, z, z2, i3, linkProperties, dataServiceCallback);
                        return;
                    }
                    return;
                case 5:
                    if (serviceProvider != null) {
                        DeactivateDataCallRequest deactivateDataCallRequest = (DeactivateDataCallRequest) message.obj;
                        serviceProvider.deactivateDataCall(deactivateDataCallRequest.cid, deactivateDataCallRequest.reason, deactivateDataCallRequest.callback != null ? new DataServiceCallback(deactivateDataCallRequest.callback) : null);
                        return;
                    }
                    return;
                case 6:
                    if (serviceProvider != null) {
                        SetInitialAttachApnRequest setInitialAttachApnRequest = (SetInitialAttachApnRequest) message.obj;
                        serviceProvider.setInitialAttachApn(setInitialAttachApnRequest.dataProfile, setInitialAttachApnRequest.isRoaming, setInitialAttachApnRequest.callback != null ? new DataServiceCallback(setInitialAttachApnRequest.callback) : null);
                        return;
                    }
                    return;
                case 7:
                    if (serviceProvider != null) {
                        SetDataProfileRequest setDataProfileRequest = (SetDataProfileRequest) message.obj;
                        serviceProvider.setDataProfile(setDataProfileRequest.dps, setDataProfileRequest.isRoaming, setDataProfileRequest.callback != null ? new DataServiceCallback(setDataProfileRequest.callback) : null);
                        return;
                    }
                    return;
                case 8:
                    if (serviceProvider != null) {
                        serviceProvider.getDataCallList(new DataServiceCallback((IDataServiceCallback) message.obj));
                        return;
                    }
                    return;
                case 9:
                    if (serviceProvider != null) {
                        serviceProvider.registerForDataCallListChanged((IDataServiceCallback) message.obj);
                        return;
                    }
                    return;
                case 10:
                    if (serviceProvider != null) {
                        IDataServiceCallback callback = (IDataServiceCallback) message.obj;
                        serviceProvider.unregisterForDataCallListChanged(callback);
                        return;
                    }
                    return;
                case 11:
                    if (serviceProvider != null) {
                        DataCallListChangedIndication indication = (DataCallListChangedIndication) message.obj;
                        try {
                            indication.callback.onDataCallListChanged(indication.dataCallList);
                            return;
                        } catch (RemoteException e) {
                            DataService.this.loge("Failed to call onDataCallListChanged. " + e);
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public synchronized DataService() {
        this.mHandlerThread.start();
        this.mHandler = new DataServiceHandler(this.mHandlerThread.getLooper());
        log("Data service created");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (intent == null || !DATA_SERVICE_INTERFACE.equals(intent.getAction())) {
            loge("Unexpected intent " + intent);
            return null;
        }
        return this.mBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        this.mHandler.obtainMessage(3).sendToTarget();
        return false;
    }

    @Override // android.app.Service
    public void onDestroy() {
        this.mHandlerThread.quit();
    }

    /* loaded from: classes2.dex */
    private class IDataServiceWrapper extends IDataService.Stub {
        private IDataServiceWrapper() {
        }

        @Override // android.telephony.data.IDataService
        public synchronized void createDataServiceProvider(int slotId) {
            DataService.this.mHandler.obtainMessage(1, slotId, 0).sendToTarget();
        }

        @Override // android.telephony.data.IDataService
        public synchronized void removeDataServiceProvider(int slotId) {
            DataService.this.mHandler.obtainMessage(2, slotId, 0).sendToTarget();
        }

        @Override // android.telephony.data.IDataService
        public synchronized void setupDataCall(int slotId, int accessNetworkType, DataProfile dataProfile, boolean isRoaming, boolean allowRoaming, int reason, LinkProperties linkProperties, IDataServiceCallback callback) {
            DataService.this.mHandler.obtainMessage(4, slotId, 0, new SetupDataCallRequest(accessNetworkType, dataProfile, isRoaming, allowRoaming, reason, linkProperties, callback)).sendToTarget();
        }

        @Override // android.telephony.data.IDataService
        public synchronized void deactivateDataCall(int slotId, int cid, int reason, IDataServiceCallback callback) {
            DataService.this.mHandler.obtainMessage(5, slotId, 0, new DeactivateDataCallRequest(cid, reason, callback)).sendToTarget();
        }

        @Override // android.telephony.data.IDataService
        public synchronized void setInitialAttachApn(int slotId, DataProfile dataProfile, boolean isRoaming, IDataServiceCallback callback) {
            DataService.this.mHandler.obtainMessage(6, slotId, 0, new SetInitialAttachApnRequest(dataProfile, isRoaming, callback)).sendToTarget();
        }

        @Override // android.telephony.data.IDataService
        public synchronized void setDataProfile(int slotId, List<DataProfile> dps, boolean isRoaming, IDataServiceCallback callback) {
            DataService.this.mHandler.obtainMessage(7, slotId, 0, new SetDataProfileRequest(dps, isRoaming, callback)).sendToTarget();
        }

        @Override // android.telephony.data.IDataService
        public synchronized void getDataCallList(int slotId, IDataServiceCallback callback) {
            if (callback == null) {
                DataService.this.loge("getDataCallList: callback is null");
            } else {
                DataService.this.mHandler.obtainMessage(8, slotId, 0, callback).sendToTarget();
            }
        }

        @Override // android.telephony.data.IDataService
        public synchronized void registerForDataCallListChanged(int slotId, IDataServiceCallback callback) {
            if (callback == null) {
                DataService.this.loge("registerForDataCallListChanged: callback is null");
            } else {
                DataService.this.mHandler.obtainMessage(9, slotId, 0, callback).sendToTarget();
            }
        }

        @Override // android.telephony.data.IDataService
        public synchronized void unregisterForDataCallListChanged(int slotId, IDataServiceCallback callback) {
            if (callback == null) {
                DataService.this.loge("unregisterForDataCallListChanged: callback is null");
            } else {
                DataService.this.mHandler.obtainMessage(10, slotId, 0, callback).sendToTarget();
            }
        }
    }

    private synchronized void log(String s) {
        Rlog.d(TAG, s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void loge(String s) {
        Rlog.e(TAG, s);
    }
}
