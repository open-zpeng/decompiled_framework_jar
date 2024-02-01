package android.telephony.ims.feature;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.util.Log;
import com.android.ims.internal.IImsFeatureStatusCallback;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

@SystemApi
/* loaded from: classes2.dex */
public abstract class ImsFeature {
    public static final String ACTION_IMS_SERVICE_DOWN = "com.android.ims.IMS_SERVICE_DOWN";
    public static final String ACTION_IMS_SERVICE_UP = "com.android.ims.IMS_SERVICE_UP";
    public static final int CAPABILITY_ERROR_GENERIC = -1;
    public static final int CAPABILITY_SUCCESS = 0;
    public static final String EXTRA_PHONE_ID = "android:phone_id";
    public static final int FEATURE_EMERGENCY_MMTEL = 0;
    public static final int FEATURE_INVALID = -1;
    public static final int FEATURE_MAX = 3;
    public static final int FEATURE_MMTEL = 1;
    public static final int FEATURE_RCS = 2;
    private static final String LOG_TAG = "ImsFeature";
    public static final int STATE_INITIALIZING = 1;
    public static final int STATE_READY = 2;
    public static final int STATE_UNAVAILABLE = 0;
    protected Context mContext;
    protected final Object mLock = new Object();
    private final Set<IImsFeatureStatusCallback> mStatusCallbacks = Collections.newSetFromMap(new WeakHashMap());
    private int mState = 0;
    private int mSlotId = -1;
    private final RemoteCallbackList<IImsCapabilityCallback> mCapabilityCallbacks = new RemoteCallbackList<>();
    private Capabilities mCapabilityStatus = new Capabilities();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface FeatureType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ImsCapabilityError {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ImsState {
    }

    public abstract void changeEnabledCapabilities(CapabilityChangeRequest capabilityChangeRequest, CapabilityCallbackProxy capabilityCallbackProxy);

    protected abstract IInterface getBinder();

    public abstract void onFeatureReady();

    public abstract void onFeatureRemoved();

    /* loaded from: classes2.dex */
    protected static class CapabilityCallbackProxy {
        private final IImsCapabilityCallback mCallback;

        public CapabilityCallbackProxy(IImsCapabilityCallback c) {
            this.mCallback = c;
        }

        public void onChangeCapabilityConfigurationError(int capability, int radioTech, int reason) {
            IImsCapabilityCallback iImsCapabilityCallback = this.mCallback;
            if (iImsCapabilityCallback == null) {
                return;
            }
            try {
                iImsCapabilityCallback.onChangeCapabilityConfigurationError(capability, radioTech, reason);
            } catch (RemoteException e) {
                Log.e(ImsFeature.LOG_TAG, "onChangeCapabilityConfigurationError called on dead binder.");
            }
        }
    }

    @SystemApi
    /* loaded from: classes2.dex */
    public static class Capabilities {
        protected int mCapabilities;

        public Capabilities() {
            this.mCapabilities = 0;
        }

        protected Capabilities(int capabilities) {
            this.mCapabilities = 0;
            this.mCapabilities = capabilities;
        }

        public void addCapabilities(int capabilities) {
            this.mCapabilities |= capabilities;
        }

        public void removeCapabilities(int capabilities) {
            this.mCapabilities &= ~capabilities;
        }

        public boolean isCapable(int capabilities) {
            return (this.mCapabilities & capabilities) == capabilities;
        }

        public Capabilities copy() {
            return new Capabilities(this.mCapabilities);
        }

        public int getMask() {
            return this.mCapabilities;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof Capabilities) {
                Capabilities that = (Capabilities) o;
                return this.mCapabilities == that.mCapabilities;
            }
            return false;
        }

        public int hashCode() {
            return this.mCapabilities;
        }

        public String toString() {
            return "Capabilities: " + Integer.toBinaryString(this.mCapabilities);
        }
    }

    public final void initialize(Context context, int slotId) {
        this.mContext = context;
        this.mSlotId = slotId;
    }

    public int getFeatureState() {
        int i;
        synchronized (this.mLock) {
            i = this.mState;
        }
        return i;
    }

    public final void setFeatureState(int state) {
        synchronized (this.mLock) {
            if (this.mState != state) {
                this.mState = state;
                notifyFeatureState(state);
            }
        }
    }

    @VisibleForTesting
    public void addImsFeatureStatusCallback(IImsFeatureStatusCallback c) {
        try {
            c.notifyImsFeatureStatus(getFeatureState());
            synchronized (this.mLock) {
                this.mStatusCallbacks.add(c);
            }
        } catch (RemoteException e) {
            Log.w(LOG_TAG, "Couldn't notify feature state: " + e.getMessage());
        }
    }

    @VisibleForTesting
    public void removeImsFeatureStatusCallback(IImsFeatureStatusCallback c) {
        synchronized (this.mLock) {
            this.mStatusCallbacks.remove(c);
        }
    }

    private void notifyFeatureState(int state) {
        synchronized (this.mLock) {
            Iterator<IImsFeatureStatusCallback> iter = this.mStatusCallbacks.iterator();
            while (iter.hasNext()) {
                IImsFeatureStatusCallback callback = iter.next();
                try {
                    Log.i(LOG_TAG, "notifying ImsFeatureState=" + state);
                    callback.notifyImsFeatureStatus(state);
                } catch (RemoteException e) {
                    iter.remove();
                    Log.w(LOG_TAG, "Couldn't notify feature state: " + e.getMessage());
                }
            }
        }
    }

    public final void addCapabilityCallback(IImsCapabilityCallback c) {
        this.mCapabilityCallbacks.register(c);
        try {
            c.onCapabilitiesStatusChanged(queryCapabilityStatus().mCapabilities);
        } catch (RemoteException e) {
            Log.w(LOG_TAG, "addCapabilityCallback: error accessing callback: " + e.getMessage());
        }
    }

    public final void removeCapabilityCallback(IImsCapabilityCallback c) {
        this.mCapabilityCallbacks.unregister(c);
    }

    @VisibleForTesting
    public Capabilities queryCapabilityStatus() {
        Capabilities copy;
        synchronized (this.mLock) {
            copy = this.mCapabilityStatus.copy();
        }
        return copy;
    }

    @VisibleForTesting
    public final void requestChangeEnabledCapabilities(CapabilityChangeRequest request, IImsCapabilityCallback c) {
        if (request == null) {
            throw new IllegalArgumentException("ImsFeature#requestChangeEnabledCapabilities called with invalid params.");
        }
        changeEnabledCapabilities(request, new CapabilityCallbackProxy(c));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void notifyCapabilitiesStatusChanged(Capabilities c) {
        synchronized (this.mLock) {
            this.mCapabilityStatus = c.copy();
        }
        int count = this.mCapabilityCallbacks.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                try {
                    this.mCapabilityCallbacks.getBroadcastItem(i).onCapabilitiesStatusChanged(c.mCapabilities);
                } catch (RemoteException e) {
                    Log.w(LOG_TAG, e + " notifyCapabilitiesStatusChanged() - Skipping callback.");
                }
            } finally {
                this.mCapabilityCallbacks.finishBroadcast();
            }
        }
    }
}
