package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.os.RemoteException;
import android.util.Log;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsEcbmListener;
@SystemApi
/* loaded from: classes2.dex */
public class ImsEcbmImplBase {
    private static final String TAG = "ImsEcbmImplBase";
    private IImsEcbm mImsEcbm = new IImsEcbm.Stub() { // from class: android.telephony.ims.stub.ImsEcbmImplBase.1
        @Override // com.android.ims.internal.IImsEcbm
        public void setListener(IImsEcbmListener listener) {
            ImsEcbmImplBase.this.mListener = listener;
        }

        @Override // com.android.ims.internal.IImsEcbm
        public void exitEmergencyCallbackMode() {
            ImsEcbmImplBase.this.exitEmergencyCallbackMode();
        }
    };
    private IImsEcbmListener mListener;

    public synchronized IImsEcbm getImsEcbm() {
        return this.mImsEcbm;
    }

    public void exitEmergencyCallbackMode() {
        Log.d(TAG, "exitEmergencyCallbackMode() not implemented");
    }

    public final void enteredEcbm() {
        Log.d(TAG, "Entered ECBM.");
        if (this.mListener != null) {
            try {
                this.mListener.enteredECBM();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public final void exitedEcbm() {
        Log.d(TAG, "Exited ECBM.");
        if (this.mListener != null) {
            try {
                this.mListener.exitedECBM();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
