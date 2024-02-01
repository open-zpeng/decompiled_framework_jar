package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsConfigCallback;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.function.Consumer;
@SystemApi
/* loaded from: classes2.dex */
public class ImsConfigImplBase {
    public static final int CONFIG_RESULT_FAILED = 1;
    public static final int CONFIG_RESULT_SUCCESS = 0;
    public static final int CONFIG_RESULT_UNKNOWN = -1;
    private static final String TAG = "ImsConfigImplBase";
    private final RemoteCallbackList<IImsConfigCallback> mCallbacks = new RemoteCallbackList<>();
    ImsConfigStub mImsConfigStub = new ImsConfigStub(this);

    @VisibleForTesting
    /* loaded from: classes2.dex */
    public static class ImsConfigStub extends IImsConfig.Stub {
        WeakReference<ImsConfigImplBase> mImsConfigImplBaseWeakReference;
        private HashMap<Integer, Integer> mProvisionedIntValue = new HashMap<>();
        private HashMap<Integer, String> mProvisionedStringValue = new HashMap<>();

        @VisibleForTesting
        public synchronized ImsConfigStub(ImsConfigImplBase imsConfigImplBase) {
            this.mImsConfigImplBaseWeakReference = new WeakReference<>(imsConfigImplBase);
        }

        public synchronized void addImsConfigCallback(IImsConfigCallback c) throws RemoteException {
            getImsConfigImpl().addImsConfigCallback(c);
        }

        public synchronized void removeImsConfigCallback(IImsConfigCallback c) throws RemoteException {
            getImsConfigImpl().removeImsConfigCallback(c);
        }

        public synchronized int getConfigInt(int item) throws RemoteException {
            if (this.mProvisionedIntValue.containsKey(Integer.valueOf(item))) {
                return this.mProvisionedIntValue.get(Integer.valueOf(item)).intValue();
            }
            int retVal = getImsConfigImpl().getConfigInt(item);
            if (retVal != -1) {
                updateCachedValue(item, retVal, false);
            }
            return retVal;
        }

        public synchronized String getConfigString(int item) throws RemoteException {
            if (this.mProvisionedIntValue.containsKey(Integer.valueOf(item))) {
                return this.mProvisionedStringValue.get(Integer.valueOf(item));
            }
            String retVal = getImsConfigImpl().getConfigString(item);
            if (retVal != null) {
                updateCachedValue(item, retVal, false);
            }
            return retVal;
        }

        public synchronized int setConfigInt(int item, int value) throws RemoteException {
            int retVal;
            this.mProvisionedIntValue.remove(Integer.valueOf(item));
            retVal = getImsConfigImpl().setConfig(item, value);
            if (retVal == 0) {
                updateCachedValue(item, value, true);
            } else {
                Log.d(ImsConfigImplBase.TAG, "Set provision value of " + item + " to " + value + " failed with error code " + retVal);
            }
            return retVal;
        }

        public synchronized int setConfigString(int item, String value) throws RemoteException {
            int retVal;
            this.mProvisionedStringValue.remove(Integer.valueOf(item));
            retVal = getImsConfigImpl().setConfig(item, value);
            if (retVal == 0) {
                updateCachedValue(item, value, true);
            }
            return retVal;
        }

        private synchronized ImsConfigImplBase getImsConfigImpl() throws RemoteException {
            ImsConfigImplBase ref = this.mImsConfigImplBaseWeakReference.get();
            if (ref == null) {
                throw new RemoteException("Fail to get ImsConfigImpl");
            }
            return ref;
        }

        private synchronized void notifyImsConfigChanged(int item, int value) throws RemoteException {
            getImsConfigImpl().notifyConfigChanged(item, value);
        }

        private synchronized void notifyImsConfigChanged(int item, String value) throws RemoteException {
            getImsConfigImpl().notifyConfigChanged(item, value);
        }

        protected synchronized void updateCachedValue(int item, int value, boolean notifyChange) throws RemoteException {
            this.mProvisionedIntValue.put(Integer.valueOf(item), Integer.valueOf(value));
            if (notifyChange) {
                notifyImsConfigChanged(item, value);
            }
        }

        protected synchronized void updateCachedValue(int item, String value, boolean notifyChange) throws RemoteException {
            this.mProvisionedStringValue.put(Integer.valueOf(item), value);
            if (notifyChange) {
                notifyImsConfigChanged(item, value);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Callback extends IImsConfigCallback.Stub {
        public final synchronized void onIntConfigChanged(int item, int value) throws RemoteException {
            onConfigChanged(item, value);
        }

        public final synchronized void onStringConfigChanged(int item, String value) throws RemoteException {
            onConfigChanged(item, value);
        }

        public synchronized void onConfigChanged(int item, int value) {
        }

        public synchronized void onConfigChanged(int item, String value) {
        }
    }

    public synchronized ImsConfigImplBase(Context context) {
    }

    public ImsConfigImplBase() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void addImsConfigCallback(IImsConfigCallback c) {
        this.mCallbacks.register(c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void removeImsConfigCallback(IImsConfigCallback c) {
        this.mCallbacks.unregister(c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final synchronized void notifyConfigChanged(final int item, final int value) {
        if (this.mCallbacks == null) {
            return;
        }
        this.mCallbacks.broadcast(new Consumer() { // from class: android.telephony.ims.stub.-$$Lambda$ImsConfigImplBase$yL4863k-FoQyqg_FX2mWsLMqbyA
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ImsConfigImplBase.lambda$notifyConfigChanged$0(item, value, (IImsConfigCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$notifyConfigChanged$0(int item, int value, IImsConfigCallback c) {
        try {
            c.onIntConfigChanged(item, value);
        } catch (RemoteException e) {
            Log.w(TAG, "notifyConfigChanged(int): dead binder in notify, skipping.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void notifyConfigChanged(final int item, final String value) {
        if (this.mCallbacks == null) {
            return;
        }
        this.mCallbacks.broadcast(new Consumer() { // from class: android.telephony.ims.stub.-$$Lambda$ImsConfigImplBase$GAuYvQ8qBc7KgCJhNp4Pt4j5t-0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ImsConfigImplBase.lambda$notifyConfigChanged$1(item, value, (IImsConfigCallback) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$notifyConfigChanged$1(int item, String value, IImsConfigCallback c) {
        try {
            c.onStringConfigChanged(item, value);
        } catch (RemoteException e) {
            Log.w(TAG, "notifyConfigChanged(string): dead binder in notify, skipping.");
        }
    }

    public synchronized IImsConfig getIImsConfig() {
        return this.mImsConfigStub;
    }

    public final void notifyProvisionedValueChanged(int item, int value) {
        try {
            this.mImsConfigStub.updateCachedValue(item, value, true);
        } catch (RemoteException e) {
            Log.w(TAG, "notifyProvisionedValueChanged(int): Framework connection is dead.");
        }
    }

    public final void notifyProvisionedValueChanged(int item, String value) {
        try {
            this.mImsConfigStub.updateCachedValue(item, value, true);
        } catch (RemoteException e) {
            Log.w(TAG, "notifyProvisionedValueChanged(string): Framework connection is dead.");
        }
    }

    public int setConfig(int item, int value) {
        return 1;
    }

    public int setConfig(int item, String value) {
        return 1;
    }

    public int getConfigInt(int item) {
        return -1;
    }

    public String getConfigString(int item) {
        return null;
    }
}
