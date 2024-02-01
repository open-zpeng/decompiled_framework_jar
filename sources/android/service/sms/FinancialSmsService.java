package android.service.sms;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.service.sms.IFinancialSmsService;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;

@SystemApi
/* loaded from: classes2.dex */
public abstract class FinancialSmsService extends Service {
    public static final String ACTION_FINANCIAL_SERVICE_INTENT = "android.service.sms.action.FINANCIAL_SERVICE_INTENT";
    public static final String EXTRA_SMS_MSGS = "sms_messages";
    private static final String TAG = "FinancialSmsService";
    private final Handler mHandler = new Handler(Looper.getMainLooper(), null, true);
    private FinancialSmsServiceWrapper mWrapper;

    @SystemApi
    public abstract CursorWindow onGetSmsMessages(Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    public void getSmsMessages(RemoteCallback callback, Bundle params) {
        Bundle data = new Bundle();
        CursorWindow smsMessages = onGetSmsMessages(params);
        if (smsMessages != null) {
            data.putParcelable(EXTRA_SMS_MSGS, smsMessages);
        }
        callback.sendResult(data);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mWrapper = new FinancialSmsServiceWrapper();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mWrapper;
    }

    /* loaded from: classes2.dex */
    private final class FinancialSmsServiceWrapper extends IFinancialSmsService.Stub {
        private FinancialSmsServiceWrapper() {
        }

        @Override // android.service.sms.IFinancialSmsService
        public void getSmsMessages(RemoteCallback callback, Bundle params) throws RemoteException {
            FinancialSmsService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: android.service.sms.-$$Lambda$FinancialSmsService$FinancialSmsServiceWrapper$XFtzKfY0m01I8W-d6pG7NlmdfiQ
                @Override // com.android.internal.util.function.TriConsumer
                public final void accept(Object obj, Object obj2, Object obj3) {
                    ((FinancialSmsService) obj).getSmsMessages((RemoteCallback) obj2, (Bundle) obj3);
                }
            }, FinancialSmsService.this, callback, params));
        }
    }
}
