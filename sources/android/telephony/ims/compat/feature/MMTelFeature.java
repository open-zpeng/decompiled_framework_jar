package android.telephony.ims.compat.feature;

import android.app.PendingIntent;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.stub.ImsEcbmImplBase;
import android.telephony.ims.stub.ImsMultiEndpointImplBase;
import android.telephony.ims.stub.ImsUtImplBase;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSessionListener;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMMTelFeature;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsRegistrationListener;
import com.android.ims.internal.IImsUt;
/* loaded from: classes2.dex */
public class MMTelFeature extends ImsFeature {
    private final Object mLock = new Object();
    private final IImsMMTelFeature mImsMMTelBinder = new IImsMMTelFeature.Stub() { // from class: android.telephony.ims.compat.feature.MMTelFeature.1
        @Override // com.android.ims.internal.IImsMMTelFeature
        public int startSession(PendingIntent incomingCallIntent, IImsRegistrationListener listener) throws RemoteException {
            int startSession;
            synchronized (MMTelFeature.this.mLock) {
                startSession = MMTelFeature.this.startSession(incomingCallIntent, listener);
            }
            return startSession;
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public void endSession(int sessionId) throws RemoteException {
            synchronized (MMTelFeature.this.mLock) {
                MMTelFeature.this.endSession(sessionId);
            }
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public boolean isConnected(int callSessionType, int callType) throws RemoteException {
            boolean isConnected;
            synchronized (MMTelFeature.this.mLock) {
                isConnected = MMTelFeature.this.isConnected(callSessionType, callType);
            }
            return isConnected;
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public boolean isOpened() throws RemoteException {
            boolean isOpened;
            synchronized (MMTelFeature.this.mLock) {
                isOpened = MMTelFeature.this.isOpened();
            }
            return isOpened;
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public int getFeatureStatus() throws RemoteException {
            int featureState;
            synchronized (MMTelFeature.this.mLock) {
                featureState = MMTelFeature.this.getFeatureState();
            }
            return featureState;
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public void addRegistrationListener(IImsRegistrationListener listener) throws RemoteException {
            synchronized (MMTelFeature.this.mLock) {
                MMTelFeature.this.addRegistrationListener(listener);
            }
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public void removeRegistrationListener(IImsRegistrationListener listener) throws RemoteException {
            synchronized (MMTelFeature.this.mLock) {
                MMTelFeature.this.removeRegistrationListener(listener);
            }
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public ImsCallProfile createCallProfile(int sessionId, int callSessionType, int callType) throws RemoteException {
            ImsCallProfile createCallProfile;
            synchronized (MMTelFeature.this.mLock) {
                createCallProfile = MMTelFeature.this.createCallProfile(sessionId, callSessionType, callType);
            }
            return createCallProfile;
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public IImsCallSession createCallSession(int sessionId, ImsCallProfile profile) throws RemoteException {
            IImsCallSession createCallSession;
            synchronized (MMTelFeature.this.mLock) {
                createCallSession = MMTelFeature.this.createCallSession(sessionId, profile, null);
            }
            return createCallSession;
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public IImsCallSession getPendingCallSession(int sessionId, String callId) throws RemoteException {
            IImsCallSession pendingCallSession;
            synchronized (MMTelFeature.this.mLock) {
                pendingCallSession = MMTelFeature.this.getPendingCallSession(sessionId, callId);
            }
            return pendingCallSession;
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public IImsUt getUtInterface() throws RemoteException {
            IImsUt iImsUt;
            synchronized (MMTelFeature.this.mLock) {
                ImsUtImplBase implBase = MMTelFeature.this.getUtInterface();
                iImsUt = implBase != null ? implBase.getInterface() : null;
            }
            return iImsUt;
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public IImsConfig getConfigInterface() throws RemoteException {
            IImsConfig configInterface;
            synchronized (MMTelFeature.this.mLock) {
                configInterface = MMTelFeature.this.getConfigInterface();
            }
            return configInterface;
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public void turnOnIms() throws RemoteException {
            synchronized (MMTelFeature.this.mLock) {
                MMTelFeature.this.turnOnIms();
            }
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public void turnOffIms() throws RemoteException {
            synchronized (MMTelFeature.this.mLock) {
                MMTelFeature.this.turnOffIms();
            }
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public IImsEcbm getEcbmInterface() throws RemoteException {
            IImsEcbm imsEcbm;
            synchronized (MMTelFeature.this.mLock) {
                ImsEcbmImplBase implBase = MMTelFeature.this.getEcbmInterface();
                imsEcbm = implBase != null ? implBase.getImsEcbm() : null;
            }
            return imsEcbm;
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public void setUiTTYMode(int uiTtyMode, Message onComplete) throws RemoteException {
            synchronized (MMTelFeature.this.mLock) {
                MMTelFeature.this.setUiTTYMode(uiTtyMode, onComplete);
            }
        }

        @Override // com.android.ims.internal.IImsMMTelFeature
        public IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException {
            IImsMultiEndpoint iImsMultiEndpoint;
            synchronized (MMTelFeature.this.mLock) {
                ImsMultiEndpointImplBase implBase = MMTelFeature.this.getMultiEndpointInterface();
                iImsMultiEndpoint = implBase != null ? implBase.getIImsMultiEndpoint() : null;
            }
            return iImsMultiEndpoint;
        }
    };

    private protected MMTelFeature() {
    }

    @Override // android.telephony.ims.compat.feature.ImsFeature
    public final synchronized IImsMMTelFeature getBinder() {
        return this.mImsMMTelBinder;
    }

    public synchronized int startSession(PendingIntent incomingCallIntent, IImsRegistrationListener listener) {
        return 0;
    }

    public synchronized void endSession(int sessionId) {
    }

    public synchronized boolean isConnected(int callSessionType, int callType) {
        return false;
    }

    public synchronized boolean isOpened() {
        return false;
    }

    public synchronized void addRegistrationListener(IImsRegistrationListener listener) {
    }

    public synchronized void removeRegistrationListener(IImsRegistrationListener listener) {
    }

    public synchronized ImsCallProfile createCallProfile(int sessionId, int callSessionType, int callType) {
        return null;
    }

    public synchronized IImsCallSession createCallSession(int sessionId, ImsCallProfile profile, IImsCallSessionListener listener) {
        return null;
    }

    public synchronized IImsCallSession getPendingCallSession(int sessionId, String callId) {
        return null;
    }

    public synchronized ImsUtImplBase getUtInterface() {
        return null;
    }

    public synchronized IImsConfig getConfigInterface() {
        return null;
    }

    public synchronized void turnOnIms() {
    }

    public synchronized void turnOffIms() {
    }

    public synchronized ImsEcbmImplBase getEcbmInterface() {
        return null;
    }

    public synchronized void setUiTTYMode(int uiTtyMode, Message onComplete) {
    }

    public synchronized ImsMultiEndpointImplBase getMultiEndpointInterface() {
        return null;
    }

    @Override // android.telephony.ims.compat.feature.ImsFeature
    public synchronized void onFeatureReady() {
    }

    @Override // android.telephony.ims.compat.feature.ImsFeature
    public synchronized void onFeatureRemoved() {
    }
}
