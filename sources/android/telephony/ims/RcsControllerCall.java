package android.telephony.ims;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.ims.RcsControllerCall;
import android.telephony.ims.aidl.IRcs;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class RcsControllerCall {
    private final Context mContext;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface RcsServiceCall<R> {
        R methodOnIRcs(IRcs iRcs, String str) throws RemoteException;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface RcsServiceCallWithNoReturn {
        void methodOnIRcs(IRcs iRcs, String str) throws RemoteException;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsControllerCall(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <R> R call(RcsServiceCall<R> serviceCall) throws RcsMessageStoreException {
        IRcs iRcs = IRcs.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_RCS_SERVICE));
        if (iRcs == null) {
            throw new RcsMessageStoreException("Could not connect to RCS storage service");
        }
        try {
            return serviceCall.methodOnIRcs(iRcs, this.mContext.getOpPackageName());
        } catch (RemoteException exception) {
            throw new RcsMessageStoreException(exception.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void callWithNoReturn(final RcsServiceCallWithNoReturn serviceCall) throws RcsMessageStoreException {
        call(new RcsServiceCall() { // from class: android.telephony.ims.-$$Lambda$RcsControllerCall$lqKvRobLziMoZre7XkbJkfc5LEM
            @Override // android.telephony.ims.RcsControllerCall.RcsServiceCall
            public final Object methodOnIRcs(IRcs iRcs, String str) {
                return RcsControllerCall.lambda$callWithNoReturn$0(RcsControllerCall.RcsServiceCallWithNoReturn.this, iRcs, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object lambda$callWithNoReturn$0(RcsServiceCallWithNoReturn serviceCall, IRcs iRcs, String callingPackage) throws RemoteException {
        serviceCall.methodOnIRcs(iRcs, callingPackage);
        return null;
    }
}
