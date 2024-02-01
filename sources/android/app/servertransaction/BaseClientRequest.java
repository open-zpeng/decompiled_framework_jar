package android.app.servertransaction;

import android.app.ClientTransactionHandler;
import android.os.IBinder;
/* loaded from: classes.dex */
public interface BaseClientRequest extends ObjectPoolItem {
    synchronized void execute(ClientTransactionHandler clientTransactionHandler, IBinder iBinder, PendingTransactionActions pendingTransactionActions);

    synchronized default void preExecute(ClientTransactionHandler client, IBinder token) {
    }

    synchronized default void postExecute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
    }
}
