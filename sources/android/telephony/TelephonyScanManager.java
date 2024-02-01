package android.telephony;

import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.TelephonyScanManager;
import android.util.SparseArray;
import com.android.internal.telephony.ITelephony;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class TelephonyScanManager {
    public static final int CALLBACK_RESTRICTED_SCAN_RESULTS = 4;
    public static final int CALLBACK_SCAN_COMPLETE = 3;
    public static final int CALLBACK_SCAN_ERROR = 2;
    public static final int CALLBACK_SCAN_RESULTS = 1;
    public static final int INVALID_SCAN_ID = -1;
    public static final String SCAN_RESULT_KEY = "scanResult";
    private static final String TAG = "TelephonyScanManager";
    private final Looper mLooper;
    private final Messenger mMessenger;
    private final SparseArray<NetworkScanInfo> mScanInfo = new SparseArray<>();

    /* loaded from: classes2.dex */
    public static abstract class NetworkScanCallback {
        public void onResults(List<CellInfo> results) {
        }

        public void onComplete() {
        }

        public void onError(int error) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class NetworkScanInfo {
        private final NetworkScanCallback mCallback;
        private final Executor mExecutor;
        private final NetworkScanRequest mRequest;

        NetworkScanInfo(NetworkScanRequest request, Executor executor, NetworkScanCallback callback) {
            this.mRequest = request;
            this.mExecutor = executor;
            this.mCallback = callback;
        }
    }

    public TelephonyScanManager() {
        HandlerThread thread = new HandlerThread(TAG);
        thread.start();
        this.mLooper = thread.getLooper();
        this.mMessenger = new Messenger(new AnonymousClass1(this.mLooper));
    }

    /* renamed from: android.telephony.TelephonyScanManager$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends Handler {
        AnonymousClass1(Looper x0) {
            super(x0);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            NetworkScanInfo nsi;
            Preconditions.checkNotNull(message, "message cannot be null");
            synchronized (TelephonyScanManager.this.mScanInfo) {
                nsi = (NetworkScanInfo) TelephonyScanManager.this.mScanInfo.get(message.arg2);
            }
            if (nsi != null) {
                final NetworkScanCallback callback = nsi.mCallback;
                Executor executor = nsi.mExecutor;
                if (callback == null) {
                    throw new RuntimeException("Failed to find NetworkScanCallback with id " + message.arg2);
                } else if (executor == null) {
                    throw new RuntimeException("Failed to find Executor with id " + message.arg2);
                } else {
                    int i = message.what;
                    if (i != 1) {
                        if (i == 2) {
                            try {
                                final int errorCode = message.arg1;
                                executor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$TelephonyScanManager$1$X9SMshZoHjJ6SzCbmgVMwQip2Q0
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        TelephonyScanManager.AnonymousClass1.lambda$handleMessage$1(errorCode, callback);
                                    }
                                });
                                return;
                            } catch (Exception e) {
                                Rlog.e(TelephonyScanManager.TAG, "Exception in networkscan callback onError", e);
                                return;
                            }
                        } else if (i == 3) {
                            try {
                                executor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$TelephonyScanManager$1$tGSpVQaVhc4GKIxjcECV-jCGYw4
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        TelephonyScanManager.AnonymousClass1.lambda$handleMessage$2(TelephonyScanManager.NetworkScanCallback.this);
                                    }
                                });
                                TelephonyScanManager.this.mScanInfo.remove(message.arg2);
                                return;
                            } catch (Exception e2) {
                                Rlog.e(TelephonyScanManager.TAG, "Exception in networkscan callback onComplete", e2);
                                return;
                            }
                        } else if (i != 4) {
                            Rlog.e(TelephonyScanManager.TAG, "Unhandled message " + Integer.toHexString(message.what));
                            return;
                        }
                    }
                    try {
                        Bundle b = message.getData();
                        Parcelable[] parcelables = b.getParcelableArray(TelephonyScanManager.SCAN_RESULT_KEY);
                        final CellInfo[] ci = new CellInfo[parcelables.length];
                        for (int i2 = 0; i2 < parcelables.length; i2++) {
                            ci[i2] = (CellInfo) parcelables[i2];
                        }
                        executor.execute(new Runnable() { // from class: android.telephony.-$$Lambda$TelephonyScanManager$1$jmXulbd8FzO5Qb8_Hi-Z6s_nJWI
                            @Override // java.lang.Runnable
                            public final void run() {
                                TelephonyScanManager.AnonymousClass1.lambda$handleMessage$0(ci, callback);
                            }
                        });
                        return;
                    } catch (Exception e3) {
                        Rlog.e(TelephonyScanManager.TAG, "Exception in networkscan callback onResults", e3);
                        return;
                    }
                }
            }
            throw new RuntimeException("Failed to find NetworkScanInfo with id " + message.arg2);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$handleMessage$0(CellInfo[] ci, NetworkScanCallback callback) {
            Rlog.d(TelephonyScanManager.TAG, "onResults: " + ci.toString());
            callback.onResults(Arrays.asList(ci));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$handleMessage$1(int errorCode, NetworkScanCallback callback) {
            Rlog.d(TelephonyScanManager.TAG, "onError: " + errorCode);
            callback.onError(errorCode);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$handleMessage$2(NetworkScanCallback callback) {
            Rlog.d(TelephonyScanManager.TAG, "onComplete");
            callback.onComplete();
        }
    }

    public NetworkScan requestNetworkScan(int subId, NetworkScanRequest request, Executor executor, NetworkScanCallback callback, String callingPackage) {
        try {
            ITelephony telephony = getITelephony();
            if (telephony != null) {
                synchronized (this.mScanInfo) {
                    int scanId = telephony.requestNetworkScan(subId, request, this.mMessenger, new Binder(), callingPackage);
                    if (scanId == -1) {
                        Rlog.e(TAG, "Failed to initiate network scan");
                        return null;
                    }
                    saveScanInfo(scanId, request, executor, callback);
                    return new NetworkScan(scanId, subId);
                }
            }
        } catch (RemoteException ex) {
            Rlog.e(TAG, "requestNetworkScan RemoteException", ex);
        } catch (NullPointerException ex2) {
            Rlog.e(TAG, "requestNetworkScan NPE", ex2);
        }
        return null;
    }

    private void saveScanInfo(int id, NetworkScanRequest request, Executor executor, NetworkScanCallback callback) {
        this.mScanInfo.put(id, new NetworkScanInfo(request, executor, callback));
    }

    private ITelephony getITelephony() {
        return ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
    }
}
