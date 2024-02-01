package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.app.IApplicationThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class ClientTransaction implements Parcelable, ObjectPoolItem {
    public static final Parcelable.Creator<ClientTransaction> CREATOR = new Parcelable.Creator<ClientTransaction>() { // from class: android.app.servertransaction.ClientTransaction.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ClientTransaction createFromParcel(Parcel in) {
            return new ClientTransaction(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ClientTransaction[] newArray(int size) {
            return new ClientTransaction[size];
        }
    };
    public protected List<ClientTransactionItem> mActivityCallbacks;
    private IBinder mActivityToken;
    private IApplicationThread mClient;
    private ActivityLifecycleItem mLifecycleStateRequest;

    public synchronized IApplicationThread getClient() {
        return this.mClient;
    }

    public synchronized void addCallback(ClientTransactionItem activityCallback) {
        if (this.mActivityCallbacks == null) {
            this.mActivityCallbacks = new ArrayList();
        }
        this.mActivityCallbacks.add(activityCallback);
    }

    public private protected List<ClientTransactionItem> getCallbacks() {
        return this.mActivityCallbacks;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IBinder getActivityToken() {
        return this.mActivityToken;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @VisibleForTesting
    public ActivityLifecycleItem getLifecycleStateRequest() {
        return this.mLifecycleStateRequest;
    }

    public synchronized void setLifecycleStateRequest(ActivityLifecycleItem stateRequest) {
        this.mLifecycleStateRequest = stateRequest;
    }

    public synchronized void preExecute(ClientTransactionHandler clientTransactionHandler) {
        if (this.mActivityCallbacks != null) {
            int size = this.mActivityCallbacks.size();
            for (int i = 0; i < size; i++) {
                this.mActivityCallbacks.get(i).preExecute(clientTransactionHandler, this.mActivityToken);
            }
        }
        if (this.mLifecycleStateRequest != null) {
            this.mLifecycleStateRequest.preExecute(clientTransactionHandler, this.mActivityToken);
        }
    }

    public synchronized void schedule() throws RemoteException {
        this.mClient.scheduleTransaction(this);
    }

    private synchronized ClientTransaction() {
    }

    public static synchronized ClientTransaction obtain(IApplicationThread client, IBinder activityToken) {
        ClientTransaction instance = (ClientTransaction) ObjectPool.obtain(ClientTransaction.class);
        if (instance == null) {
            instance = new ClientTransaction();
        }
        instance.mClient = client;
        instance.mActivityToken = activityToken;
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public synchronized void recycle() {
        if (this.mActivityCallbacks != null) {
            int size = this.mActivityCallbacks.size();
            for (int i = 0; i < size; i++) {
                this.mActivityCallbacks.get(i).recycle();
            }
            this.mActivityCallbacks.clear();
        }
        if (this.mLifecycleStateRequest != null) {
            this.mLifecycleStateRequest.recycle();
            this.mLifecycleStateRequest = null;
        }
        this.mClient = null;
        this.mActivityToken = null;
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(this.mClient.asBinder());
        boolean writeActivityToken = this.mActivityToken != null;
        dest.writeBoolean(writeActivityToken);
        if (writeActivityToken) {
            dest.writeStrongBinder(this.mActivityToken);
        }
        dest.writeParcelable(this.mLifecycleStateRequest, flags);
        boolean writeActivityCallbacks = this.mActivityCallbacks != null;
        dest.writeBoolean(writeActivityCallbacks);
        if (writeActivityCallbacks) {
            dest.writeParcelableList(this.mActivityCallbacks, flags);
        }
    }

    private synchronized ClientTransaction(Parcel in) {
        this.mClient = (IApplicationThread) in.readStrongBinder();
        boolean readActivityToken = in.readBoolean();
        if (readActivityToken) {
            this.mActivityToken = in.readStrongBinder();
        }
        this.mLifecycleStateRequest = (ActivityLifecycleItem) in.readParcelable(getClass().getClassLoader());
        boolean readActivityCallbacks = in.readBoolean();
        if (readActivityCallbacks) {
            this.mActivityCallbacks = new ArrayList();
            in.readParcelableList(this.mActivityCallbacks, getClass().getClassLoader());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientTransaction other = (ClientTransaction) o;
        if (Objects.equals(this.mActivityCallbacks, other.mActivityCallbacks) && Objects.equals(this.mLifecycleStateRequest, other.mLifecycleStateRequest) && this.mClient == other.mClient && this.mActivityToken == other.mActivityToken) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (31 * 17) + Objects.hashCode(this.mActivityCallbacks);
        return (31 * result) + Objects.hashCode(this.mLifecycleStateRequest);
    }
}
