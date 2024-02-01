package android.telephony.ims.compat.stub;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import com.android.ims.ImsConfig;
import com.android.ims.ImsConfigListener;
import com.android.ims.internal.IImsConfig;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.HashMap;
/* loaded from: classes2.dex */
public class ImsConfigImplBase {
    private static final String TAG = "ImsConfigImplBase";
    ImsConfigStub mImsConfigStub;

    private protected ImsConfigImplBase(Context context) {
        this.mImsConfigStub = new ImsConfigStub(this, context);
    }

    public synchronized int getProvisionedValue(int item) throws RemoteException {
        return -1;
    }

    public synchronized String getProvisionedStringValue(int item) throws RemoteException {
        return null;
    }

    public synchronized int setProvisionedValue(int item, int value) throws RemoteException {
        return 1;
    }

    public synchronized int setProvisionedStringValue(int item, String value) throws RemoteException {
        return 1;
    }

    public synchronized void getFeatureValue(int feature, int network, ImsConfigListener listener) throws RemoteException {
    }

    public synchronized void setFeatureValue(int feature, int network, int value, ImsConfigListener listener) throws RemoteException {
    }

    public synchronized boolean getVolteProvisioned() throws RemoteException {
        return false;
    }

    public synchronized void getVideoQuality(ImsConfigListener listener) throws RemoteException {
    }

    public synchronized void setVideoQuality(int quality, ImsConfigListener listener) throws RemoteException {
    }

    private protected IImsConfig getIImsConfig() {
        return this.mImsConfigStub;
    }

    public final synchronized void notifyProvisionedValueChanged(int item, int value) {
        this.mImsConfigStub.updateCachedValue(item, value, true);
    }

    public final synchronized void notifyProvisionedValueChanged(int item, String value) {
        this.mImsConfigStub.updateCachedValue(item, value, true);
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    public static class ImsConfigStub extends IImsConfig.Stub {
        Context mContext;
        WeakReference<ImsConfigImplBase> mImsConfigImplBaseWeakReference;
        private HashMap<Integer, Integer> mProvisionedIntValue = new HashMap<>();
        private HashMap<Integer, String> mProvisionedStringValue = new HashMap<>();

        @VisibleForTesting
        public synchronized ImsConfigStub(ImsConfigImplBase imsConfigImplBase, Context context) {
            this.mContext = context;
            this.mImsConfigImplBaseWeakReference = new WeakReference<>(imsConfigImplBase);
        }

        @Override // com.android.ims.internal.IImsConfig
        public synchronized int getProvisionedValue(int item) throws RemoteException {
            if (this.mProvisionedIntValue.containsKey(Integer.valueOf(item))) {
                return this.mProvisionedIntValue.get(Integer.valueOf(item)).intValue();
            }
            int retVal = getImsConfigImpl().getProvisionedValue(item);
            if (retVal != -1) {
                updateCachedValue(item, retVal, false);
            }
            return retVal;
        }

        @Override // com.android.ims.internal.IImsConfig
        public synchronized String getProvisionedStringValue(int item) throws RemoteException {
            if (this.mProvisionedIntValue.containsKey(Integer.valueOf(item))) {
                return this.mProvisionedStringValue.get(Integer.valueOf(item));
            }
            String retVal = getImsConfigImpl().getProvisionedStringValue(item);
            if (retVal != null) {
                updateCachedValue(item, retVal, false);
            }
            return retVal;
        }

        @Override // com.android.ims.internal.IImsConfig
        public synchronized int setProvisionedValue(int item, int value) throws RemoteException {
            int retVal;
            this.mProvisionedIntValue.remove(Integer.valueOf(item));
            retVal = getImsConfigImpl().setProvisionedValue(item, value);
            if (retVal == 0) {
                updateCachedValue(item, value, true);
            } else {
                Log.d(ImsConfigImplBase.TAG, "Set provision value of " + item + " to " + value + " failed with error code " + retVal);
            }
            return retVal;
        }

        @Override // com.android.ims.internal.IImsConfig
        public synchronized int setProvisionedStringValue(int item, String value) throws RemoteException {
            int retVal;
            this.mProvisionedStringValue.remove(Integer.valueOf(item));
            retVal = getImsConfigImpl().setProvisionedStringValue(item, value);
            if (retVal == 0) {
                updateCachedValue(item, value, true);
            }
            return retVal;
        }

        @Override // com.android.ims.internal.IImsConfig
        public synchronized void getFeatureValue(int feature, int network, ImsConfigListener listener) throws RemoteException {
            getImsConfigImpl().getFeatureValue(feature, network, listener);
        }

        @Override // com.android.ims.internal.IImsConfig
        public synchronized void setFeatureValue(int feature, int network, int value, ImsConfigListener listener) throws RemoteException {
            getImsConfigImpl().setFeatureValue(feature, network, value, listener);
        }

        @Override // com.android.ims.internal.IImsConfig
        public synchronized boolean getVolteProvisioned() throws RemoteException {
            return getImsConfigImpl().getVolteProvisioned();
        }

        @Override // com.android.ims.internal.IImsConfig
        public synchronized void getVideoQuality(ImsConfigListener listener) throws RemoteException {
            getImsConfigImpl().getVideoQuality(listener);
        }

        @Override // com.android.ims.internal.IImsConfig
        public synchronized void setVideoQuality(int quality, ImsConfigListener listener) throws RemoteException {
            getImsConfigImpl().setVideoQuality(quality, listener);
        }

        private synchronized ImsConfigImplBase getImsConfigImpl() throws RemoteException {
            ImsConfigImplBase ref = this.mImsConfigImplBaseWeakReference.get();
            if (ref == null) {
                throw new RemoteException("Fail to get ImsConfigImpl");
            }
            return ref;
        }

        private synchronized void sendImsConfigChangedIntent(int item, int value) {
            sendImsConfigChangedIntent(item, Integer.toString(value));
        }

        private synchronized void sendImsConfigChangedIntent(int item, String value) {
            Intent configChangedIntent = new Intent(ImsConfig.ACTION_IMS_CONFIG_CHANGED);
            configChangedIntent.putExtra(ImsConfig.EXTRA_CHANGED_ITEM, item);
            configChangedIntent.putExtra("value", value);
            if (this.mContext != null) {
                this.mContext.sendBroadcast(configChangedIntent);
            }
        }

        protected synchronized void updateCachedValue(int item, int value, boolean notifyChange) {
            this.mProvisionedIntValue.put(Integer.valueOf(item), Integer.valueOf(value));
            if (notifyChange) {
                sendImsConfigChangedIntent(item, value);
            }
        }

        protected synchronized void updateCachedValue(int item, String value, boolean notifyChange) {
            this.mProvisionedStringValue.put(Integer.valueOf(item), value);
            if (notifyChange) {
                sendImsConfigChangedIntent(item, value);
            }
        }
    }
}
