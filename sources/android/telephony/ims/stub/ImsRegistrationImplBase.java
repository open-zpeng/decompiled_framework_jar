package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Consumer;
@SystemApi
/* loaded from: classes2.dex */
public class ImsRegistrationImplBase {
    private static final String LOG_TAG = "ImsRegistrationImplBase";
    private static final int REGISTRATION_STATE_NOT_REGISTERED = 0;
    private static final int REGISTRATION_STATE_REGISTERED = 2;
    private static final int REGISTRATION_STATE_REGISTERING = 1;
    private static final int REGISTRATION_STATE_UNKNOWN = -1;
    public static final int REGISTRATION_TECH_IWLAN = 1;
    public static final int REGISTRATION_TECH_LTE = 0;
    public static final int REGISTRATION_TECH_NONE = -1;
    private final IImsRegistration mBinder = new IImsRegistration.Stub() { // from class: android.telephony.ims.stub.ImsRegistrationImplBase.1
        public int getRegistrationTechnology() throws RemoteException {
            return ImsRegistrationImplBase.this.getConnectionType();
        }

        public void addRegistrationCallback(IImsRegistrationCallback c) throws RemoteException {
            ImsRegistrationImplBase.this.addRegistrationCallback(c);
        }

        public void removeRegistrationCallback(IImsRegistrationCallback c) throws RemoteException {
            ImsRegistrationImplBase.this.removeRegistrationCallback(c);
        }
    };
    private final RemoteCallbackList<IImsRegistrationCallback> mCallbacks = new RemoteCallbackList<>();
    private final Object mLock = new Object();
    private int mConnectionType = -1;
    private int mRegistrationState = -1;
    private ImsReasonInfo mLastDisconnectCause = new ImsReasonInfo();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ImsRegistrationTech {
    }

    /* loaded from: classes2.dex */
    public static class Callback {
        public synchronized void onRegistered(int imsRadioTech) {
        }

        public synchronized void onRegistering(int imsRadioTech) {
        }

        public synchronized void onDeregistered(ImsReasonInfo info) {
        }

        public synchronized void onTechnologyChangeFailed(int imsRadioTech, ImsReasonInfo info) {
        }

        public synchronized void onSubscriberAssociatedUriChanged(Uri[] uris) {
        }
    }

    public final synchronized IImsRegistration getBinder() {
        return this.mBinder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void addRegistrationCallback(IImsRegistrationCallback c) throws RemoteException {
        this.mCallbacks.register(c);
        updateNewCallbackWithState(c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void removeRegistrationCallback(IImsRegistrationCallback c) {
        this.mCallbacks.unregister(c);
    }

    public final void onRegistered(final int imsRadioTech) {
        updateToState(imsRadioTech, 2);
        this.mCallbacks.broadcast(new Consumer() { // from class: android.telephony.ims.stub.-$$Lambda$ImsRegistrationImplBase$cWwTXSDsk-bWPbsDJYI--DUBMnE
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ImsRegistrationImplBase.lambda$onRegistered$0(imsRadioTech, (IImsRegistrationCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onRegistered$0(int imsRadioTech, IImsRegistrationCallback c) {
        try {
            c.onRegistered(imsRadioTech);
        } catch (RemoteException e) {
            Log.w(LOG_TAG, e + " onRegistrationConnected() - Skipping callback.");
        }
    }

    public final void onRegistering(final int imsRadioTech) {
        updateToState(imsRadioTech, 1);
        this.mCallbacks.broadcast(new Consumer() { // from class: android.telephony.ims.stub.-$$Lambda$ImsRegistrationImplBase$sbjuTvW-brOSWMR74UInSZEIQB0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ImsRegistrationImplBase.lambda$onRegistering$1(imsRadioTech, (IImsRegistrationCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onRegistering$1(int imsRadioTech, IImsRegistrationCallback c) {
        try {
            c.onRegistering(imsRadioTech);
        } catch (RemoteException e) {
            Log.w(LOG_TAG, e + " onRegistrationProcessing() - Skipping callback.");
        }
    }

    public final void onDeregistered(final ImsReasonInfo info) {
        updateToDisconnectedState(info);
        this.mCallbacks.broadcast(new Consumer() { // from class: android.telephony.ims.stub.-$$Lambda$ImsRegistrationImplBase$s7PspXVbCf1Q_WSzodP2glP9TjI
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ImsRegistrationImplBase.lambda$onDeregistered$2(ImsReasonInfo.this, (IImsRegistrationCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onDeregistered$2(ImsReasonInfo info, IImsRegistrationCallback c) {
        try {
            c.onDeregistered(info);
        } catch (RemoteException e) {
            Log.w(LOG_TAG, e + " onRegistrationDisconnected() - Skipping callback.");
        }
    }

    public final void onTechnologyChangeFailed(final int imsRadioTech, final ImsReasonInfo info) {
        this.mCallbacks.broadcast(new Consumer() { // from class: android.telephony.ims.stub.-$$Lambda$ImsRegistrationImplBase$wDtW65cPmn_jF6dfimhBTfdg1kI
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ImsRegistrationImplBase.lambda$onTechnologyChangeFailed$3(imsRadioTech, info, (IImsRegistrationCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onTechnologyChangeFailed$3(int imsRadioTech, ImsReasonInfo info, IImsRegistrationCallback c) {
        try {
            c.onTechnologyChangeFailed(imsRadioTech, info);
        } catch (RemoteException e) {
            Log.w(LOG_TAG, e + " onRegistrationChangeFailed() - Skipping callback.");
        }
    }

    public final void onSubscriberAssociatedUriChanged(final Uri[] uris) {
        this.mCallbacks.broadcast(new Consumer() { // from class: android.telephony.ims.stub.-$$Lambda$ImsRegistrationImplBase$wwtkoeOtGwMjG5I0-ZTfjNpGU-s
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ImsRegistrationImplBase.lambda$onSubscriberAssociatedUriChanged$4(uris, (IImsRegistrationCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onSubscriberAssociatedUriChanged$4(Uri[] uris, IImsRegistrationCallback c) {
        try {
            c.onSubscriberAssociatedUriChanged(uris);
        } catch (RemoteException e) {
            Log.w(LOG_TAG, e + " onSubscriberAssociatedUriChanged() - Skipping callback.");
        }
    }

    private synchronized void updateToState(int connType, int newState) {
        synchronized (this.mLock) {
            this.mConnectionType = connType;
            this.mRegistrationState = newState;
            this.mLastDisconnectCause = null;
        }
    }

    private synchronized void updateToDisconnectedState(ImsReasonInfo info) {
        synchronized (this.mLock) {
            updateToState(-1, 0);
            if (info != null) {
                this.mLastDisconnectCause = info;
            } else {
                Log.w(LOG_TAG, "updateToDisconnectedState: no ImsReasonInfo provided.");
                this.mLastDisconnectCause = new ImsReasonInfo();
            }
        }
    }

    @VisibleForTesting
    public final synchronized int getConnectionType() {
        int i;
        synchronized (this.mLock) {
            i = this.mConnectionType;
        }
        return i;
    }

    private synchronized void updateNewCallbackWithState(IImsRegistrationCallback c) throws RemoteException {
        int state;
        ImsReasonInfo disconnectInfo;
        synchronized (this.mLock) {
            state = this.mRegistrationState;
            disconnectInfo = this.mLastDisconnectCause;
        }
        switch (state) {
            case 0:
                c.onDeregistered(disconnectInfo);
                return;
            case 1:
                c.onRegistering(getConnectionType());
                return;
            case 2:
                c.onRegistered(getConnectionType());
                return;
            default:
                return;
        }
    }
}
