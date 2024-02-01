package android.telephony.ims.compat.stub;

import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsData;
import android.telephony.ims.ImsSsInfo;
import com.android.ims.internal.IImsUt;
import com.android.ims.internal.IImsUtListener;
/* loaded from: classes2.dex */
public class ImsUtListenerImplBase extends IImsUtListener.Stub {
    private protected ImsUtListenerImplBase() {
    }

    public synchronized void utConfigurationUpdated(IImsUt ut, int id) throws RemoteException {
    }

    public synchronized void utConfigurationUpdateFailed(IImsUt ut, int id, ImsReasonInfo error) throws RemoteException {
    }

    public synchronized void utConfigurationQueried(IImsUt ut, int id, Bundle ssInfo) throws RemoteException {
    }

    public synchronized void utConfigurationQueryFailed(IImsUt ut, int id, ImsReasonInfo error) throws RemoteException {
    }

    public synchronized void utConfigurationCallBarringQueried(IImsUt ut, int id, ImsSsInfo[] cbInfo) throws RemoteException {
    }

    public synchronized void utConfigurationCallForwardQueried(IImsUt ut, int id, ImsCallForwardInfo[] cfInfo) throws RemoteException {
    }

    public synchronized void utConfigurationCallWaitingQueried(IImsUt ut, int id, ImsSsInfo[] cwInfo) throws RemoteException {
    }

    @Override // com.android.ims.internal.IImsUtListener
    public synchronized void onSupplementaryServiceIndication(ImsSsData ssData) {
    }
}
