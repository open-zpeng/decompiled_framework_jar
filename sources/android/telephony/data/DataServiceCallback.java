package android.telephony.data;

import android.os.RemoteException;
import android.telephony.Rlog;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.List;
/* loaded from: classes2.dex */
public class DataServiceCallback {
    public static final int RESULT_ERROR_BUSY = 3;
    public static final int RESULT_ERROR_ILLEGAL_STATE = 4;
    public static final int RESULT_ERROR_INVALID_ARG = 2;
    public static final int RESULT_ERROR_UNSUPPORTED = 1;
    public static final int RESULT_SUCCESS = 0;
    private static final String TAG = DataServiceCallback.class.getSimpleName();
    private final WeakReference<IDataServiceCallback> mCallback;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ResultCode {
    }

    public synchronized DataServiceCallback(IDataServiceCallback callback) {
        this.mCallback = new WeakReference<>(callback);
    }

    public synchronized void onSetupDataCallComplete(int result, DataCallResponse response) {
        IDataServiceCallback callback = this.mCallback.get();
        if (callback != null) {
            try {
                callback.onSetupDataCallComplete(result, response);
            } catch (RemoteException e) {
                Rlog.e(TAG, "Failed to onSetupDataCallComplete on the remote");
            }
        }
    }

    public synchronized void onDeactivateDataCallComplete(int result) {
        IDataServiceCallback callback = this.mCallback.get();
        if (callback != null) {
            try {
                callback.onDeactivateDataCallComplete(result);
            } catch (RemoteException e) {
                Rlog.e(TAG, "Failed to onDeactivateDataCallComplete on the remote");
            }
        }
    }

    public synchronized void onSetInitialAttachApnComplete(int result) {
        IDataServiceCallback callback = this.mCallback.get();
        if (callback != null) {
            try {
                callback.onSetInitialAttachApnComplete(result);
            } catch (RemoteException e) {
                Rlog.e(TAG, "Failed to onSetInitialAttachApnComplete on the remote");
            }
        }
    }

    public synchronized void onSetDataProfileComplete(int result) {
        IDataServiceCallback callback = this.mCallback.get();
        if (callback != null) {
            try {
                callback.onSetDataProfileComplete(result);
            } catch (RemoteException e) {
                Rlog.e(TAG, "Failed to onSetDataProfileComplete on the remote");
            }
        }
    }

    public synchronized void onGetDataCallListComplete(int result, List<DataCallResponse> dataCallList) {
        IDataServiceCallback callback = this.mCallback.get();
        if (callback != null) {
            try {
                callback.onGetDataCallListComplete(result, dataCallList);
            } catch (RemoteException e) {
                Rlog.e(TAG, "Failed to onGetDataCallListComplete on the remote");
            }
        }
    }

    public synchronized void onDataCallListChanged(List<DataCallResponse> dataCallList) {
        IDataServiceCallback callback = this.mCallback.get();
        if (callback != null) {
            try {
                callback.onDataCallListChanged(dataCallList);
            } catch (RemoteException e) {
                Rlog.e(TAG, "Failed to onDataCallListChanged on the remote");
            }
        }
    }
}
