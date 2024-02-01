package android.app.servertransaction;

import android.app.ActivityThread;
import android.app.ClientTransactionHandler;
import android.os.IBinder;
import android.util.IntArray;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;
/* loaded from: classes.dex */
public class TransactionExecutor {
    private static final boolean DEBUG_RESOLVER = false;
    private static final String TAG = "TransactionExecutor";
    private ClientTransactionHandler mTransactionHandler;
    private PendingTransactionActions mPendingActions = new PendingTransactionActions();
    private TransactionExecutorHelper mHelper = new TransactionExecutorHelper();

    public synchronized TransactionExecutor(ClientTransactionHandler clientTransactionHandler) {
        this.mTransactionHandler = clientTransactionHandler;
    }

    public synchronized void execute(ClientTransaction transaction) {
        IBinder token = transaction.getActivityToken();
        log("Start resolving transaction for client: " + this.mTransactionHandler + ", token: " + token);
        executeCallbacks(transaction);
        executeLifecycleState(transaction);
        this.mPendingActions.clear();
        log("End resolving transaction");
    }

    @VisibleForTesting
    public synchronized void executeCallbacks(ClientTransaction transaction) {
        List<ClientTransactionItem> callbacks = transaction.getCallbacks();
        if (callbacks == null) {
            return;
        }
        log("Resolving callbacks");
        IBinder token = transaction.getActivityToken();
        ActivityThread.ActivityClientRecord r = this.mTransactionHandler.getActivityClient(token);
        ActivityLifecycleItem finalStateRequest = transaction.getLifecycleStateRequest();
        int finalState = finalStateRequest != null ? finalStateRequest.getTargetState() : -1;
        int lastCallbackRequestingState = TransactionExecutorHelper.lastCallbackRequestingState(transaction);
        int size = callbacks.size();
        ActivityThread.ActivityClientRecord r2 = r;
        int i = 0;
        while (i < size) {
            ClientTransactionItem item = callbacks.get(i);
            log("Resolving callback: " + item);
            int postExecutionState = item.getPostExecutionState();
            int closestPreExecutionState = this.mHelper.getClosestPreExecutionState(r2, item.getPostExecutionState());
            if (closestPreExecutionState != -1) {
                cycleToPath(r2, closestPreExecutionState);
            }
            item.execute(this.mTransactionHandler, token, this.mPendingActions);
            item.postExecute(this.mTransactionHandler, token, this.mPendingActions);
            if (r2 == null) {
                r2 = this.mTransactionHandler.getActivityClient(token);
            }
            if (postExecutionState != -1 && r2 != null) {
                boolean shouldExcludeLastTransition = i == lastCallbackRequestingState && finalState == postExecutionState;
                cycleToPath(r2, postExecutionState, shouldExcludeLastTransition);
            }
            i++;
        }
    }

    private synchronized void executeLifecycleState(ClientTransaction transaction) {
        ActivityLifecycleItem lifecycleItem = transaction.getLifecycleStateRequest();
        if (lifecycleItem == null) {
            return;
        }
        log("Resolving lifecycle state: " + lifecycleItem);
        IBinder token = transaction.getActivityToken();
        ActivityThread.ActivityClientRecord r = this.mTransactionHandler.getActivityClient(token);
        if (r == null) {
            return;
        }
        cycleToPath(r, lifecycleItem.getTargetState(), true);
        lifecycleItem.execute(this.mTransactionHandler, token, this.mPendingActions);
        lifecycleItem.postExecute(this.mTransactionHandler, token, this.mPendingActions);
    }

    @VisibleForTesting
    public synchronized void cycleToPath(ActivityThread.ActivityClientRecord r, int finish) {
        cycleToPath(r, finish, false);
    }

    private synchronized void cycleToPath(ActivityThread.ActivityClientRecord r, int finish, boolean excludeLastState) {
        int start = r.getLifecycleState();
        log("Cycle from: " + start + " to: " + finish + " excludeLastState:" + excludeLastState);
        IntArray path = this.mHelper.getLifecyclePath(start, finish, excludeLastState);
        performLifecycleSequence(r, path);
    }

    private synchronized void performLifecycleSequence(ActivityThread.ActivityClientRecord r, IntArray path) {
        int size = path.size();
        for (int i = 0; i < size; i++) {
            int state = path.get(i);
            log("Transitioning to state: " + state);
            switch (state) {
                case 1:
                    this.mTransactionHandler.handleLaunchActivity(r, this.mPendingActions, null);
                    break;
                case 2:
                    this.mTransactionHandler.handleStartActivity(r, this.mPendingActions);
                    break;
                case 3:
                    this.mTransactionHandler.handleResumeActivity(r.token, false, r.isForward, "LIFECYCLER_RESUME_ACTIVITY");
                    break;
                case 4:
                    this.mTransactionHandler.handlePauseActivity(r.token, false, false, 0, 0, this.mPendingActions, "LIFECYCLER_PAUSE_ACTIVITY");
                    break;
                case 5:
                    this.mTransactionHandler.handleStopActivity(r.token, false, 0, this.mPendingActions, false, "LIFECYCLER_STOP_ACTIVITY");
                    break;
                case 6:
                    this.mTransactionHandler.handleDestroyActivity(r.token, false, 0, false, "performLifecycleSequence. cycling to:" + path.get(size - 1));
                    break;
                case 7:
                    this.mTransactionHandler.performRestartActivity(r.token, false);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected lifecycle state: " + state);
            }
        }
    }

    private static synchronized void log(String message) {
    }
}
