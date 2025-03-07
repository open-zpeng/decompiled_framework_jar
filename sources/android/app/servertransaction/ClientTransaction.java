package android.app.servertransaction;

import android.annotation.UnsupportedAppUsage;
import android.app.ClientTransactionHandler;
import android.app.IApplicationThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;
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
    @UnsupportedAppUsage
    private List<ClientTransactionItem> mActivityCallbacks;
    private IBinder mActivityToken;
    private IApplicationThread mClient;
    private ActivityLifecycleItem mLifecycleStateRequest;

    public IApplicationThread getClient() {
        return this.mClient;
    }

    public void addCallback(ClientTransactionItem activityCallback) {
        if (this.mActivityCallbacks == null) {
            this.mActivityCallbacks = new ArrayList();
        }
        this.mActivityCallbacks.add(activityCallback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public List<ClientTransactionItem> getCallbacks() {
        return this.mActivityCallbacks;
    }

    @UnsupportedAppUsage
    public IBinder getActivityToken() {
        return this.mActivityToken;
    }

    @UnsupportedAppUsage
    @VisibleForTesting
    public ActivityLifecycleItem getLifecycleStateRequest() {
        return this.mLifecycleStateRequest;
    }

    public void setLifecycleStateRequest(ActivityLifecycleItem stateRequest) {
        this.mLifecycleStateRequest = stateRequest;
    }

    public void preExecute(ClientTransactionHandler clientTransactionHandler) {
        List<ClientTransactionItem> list = this.mActivityCallbacks;
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                this.mActivityCallbacks.get(i).preExecute(clientTransactionHandler, this.mActivityToken);
            }
        }
        ActivityLifecycleItem activityLifecycleItem = this.mLifecycleStateRequest;
        if (activityLifecycleItem != null) {
            activityLifecycleItem.preExecute(clientTransactionHandler, this.mActivityToken);
        }
    }

    public void schedule() throws RemoteException {
        this.mClient.scheduleTransaction(this);
    }

    private ClientTransaction() {
    }

    public static ClientTransaction obtain(IApplicationThread client, IBinder activityToken) {
        ClientTransaction instance = (ClientTransaction) ObjectPool.obtain(ClientTransaction.class);
        if (instance == null) {
            instance = new ClientTransaction();
        }
        instance.mClient = client;
        instance.mActivityToken = activityToken;
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public void recycle() {
        List<ClientTransactionItem> list = this.mActivityCallbacks;
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                this.mActivityCallbacks.get(i).recycle();
            }
            this.mActivityCallbacks.clear();
        }
        ActivityLifecycleItem activityLifecycleItem = this.mLifecycleStateRequest;
        if (activityLifecycleItem != null) {
            activityLifecycleItem.recycle();
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

    private ClientTransaction(Parcel in) {
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
        int result = (17 * 31) + Objects.hashCode(this.mActivityCallbacks);
        return (result * 31) + Objects.hashCode(this.mLifecycleStateRequest);
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.append((CharSequence) prefix).println("ClientTransaction{");
        pw.append((CharSequence) prefix).print("  callbacks=[");
        List<ClientTransactionItem> list = this.mActivityCallbacks;
        int size = list != null ? list.size() : 0;
        if (size > 0) {
            pw.println();
            for (int i = 0; i < size; i++) {
                pw.append((CharSequence) prefix).append("    ").println(this.mActivityCallbacks.get(i).toString());
            }
            pw.append((CharSequence) prefix).println("  ]");
        } else {
            pw.println("]");
        }
        PrintWriter append = pw.append((CharSequence) prefix).append("  stateRequest=");
        ActivityLifecycleItem activityLifecycleItem = this.mLifecycleStateRequest;
        append.println(activityLifecycleItem != null ? activityLifecycleItem.toString() : null);
        pw.append((CharSequence) prefix).println("}");
    }
}
