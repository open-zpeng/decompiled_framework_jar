package android.app.servertransaction;

import android.app.ActivityThread;
import android.util.IntArray;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;
/* loaded from: classes.dex */
public class TransactionExecutorHelper {
    private static final int DESTRUCTION_PENALTY = 10;
    private static final int[] ON_RESUME_PRE_EXCUTION_STATES = {2, 4};
    private IntArray mLifecycleSequence = new IntArray(6);

    @VisibleForTesting
    public synchronized IntArray getLifecyclePath(int start, int finish, boolean excludeLastState) {
        if (start == -1 || finish == -1) {
            throw new IllegalArgumentException("Can't resolve lifecycle path for undefined state");
        }
        if (start == 7 || finish == 7) {
            throw new IllegalArgumentException("Can't start or finish in intermittent RESTART state");
        }
        if (finish == 0 && start != finish) {
            throw new IllegalArgumentException("Can only start in pre-onCreate state");
        }
        this.mLifecycleSequence.clear();
        if (finish >= start) {
            for (int i = start + 1; i <= finish; i++) {
                this.mLifecycleSequence.add(i);
            }
        } else if (start == 4 && finish == 3) {
            this.mLifecycleSequence.add(3);
        } else {
            if (start <= 5) {
                int i2 = 2;
                if (finish >= 2) {
                    for (int i3 = start + 1; i3 <= 5; i3++) {
                        this.mLifecycleSequence.add(i3);
                    }
                    this.mLifecycleSequence.add(7);
                    while (true) {
                        int i4 = i2;
                        if (i4 > finish) {
                            break;
                        }
                        this.mLifecycleSequence.add(i4);
                        i2 = i4 + 1;
                    }
                }
            }
            for (int i5 = start + 1; i5 <= 6; i5++) {
                this.mLifecycleSequence.add(i5);
            }
            for (int i6 = 1; i6 <= finish; i6++) {
                this.mLifecycleSequence.add(i6);
            }
        }
        if (excludeLastState && this.mLifecycleSequence.size() != 0) {
            this.mLifecycleSequence.remove(this.mLifecycleSequence.size() - 1);
        }
        return this.mLifecycleSequence;
    }

    @VisibleForTesting
    public synchronized int getClosestPreExecutionState(ActivityThread.ActivityClientRecord r, int postExecutionState) {
        if (postExecutionState != -1) {
            if (postExecutionState == 3) {
                return getClosestOfStates(r, ON_RESUME_PRE_EXCUTION_STATES);
            }
            throw new UnsupportedOperationException("Pre-execution states for state: " + postExecutionState + " is not supported.");
        }
        return -1;
    }

    @VisibleForTesting
    public synchronized int getClosestOfStates(ActivityThread.ActivityClientRecord r, int[] finalStates) {
        if (finalStates == null || finalStates.length == 0) {
            return -1;
        }
        int currentState = r.getLifecycleState();
        int closestState = -1;
        int shortestPath = Integer.MAX_VALUE;
        for (int i = 0; i < finalStates.length; i++) {
            getLifecyclePath(currentState, finalStates[i], false);
            int pathLength = this.mLifecycleSequence.size();
            if (pathInvolvesDestruction(this.mLifecycleSequence)) {
                pathLength += 10;
            }
            if (shortestPath > pathLength) {
                shortestPath = pathLength;
                closestState = finalStates[i];
            }
        }
        return closestState;
    }

    public static synchronized ActivityLifecycleItem getLifecycleRequestForCurrentState(ActivityThread.ActivityClientRecord r) {
        int prevState = r.getLifecycleState();
        switch (prevState) {
            case 4:
                ActivityLifecycleItem lifecycleItem = PauseActivityItem.obtain();
                return lifecycleItem;
            case 5:
                ActivityLifecycleItem lifecycleItem2 = StopActivityItem.obtain(r.isVisibleFromServer(), 0);
                return lifecycleItem2;
            default:
                ActivityLifecycleItem lifecycleItem3 = ResumeActivityItem.obtain(false);
                return lifecycleItem3;
        }
    }

    private static synchronized boolean pathInvolvesDestruction(IntArray lifecycleSequence) {
        int size = lifecycleSequence.size();
        for (int i = 0; i < size; i++) {
            if (lifecycleSequence.get(i) == 6) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized int lastCallbackRequestingState(ClientTransaction transaction) {
        List<ClientTransactionItem> callbacks = transaction.getCallbacks();
        if (callbacks == null || callbacks.size() == 0) {
            return -1;
        }
        int lastRequestedState = -1;
        int lastRequestingCallback = -1;
        for (int i = callbacks.size() - 1; i >= 0; i--) {
            ClientTransactionItem callback = callbacks.get(i);
            int postExecutionState = callback.getPostExecutionState();
            if (postExecutionState != -1) {
                if (lastRequestedState != -1 && lastRequestedState != postExecutionState) {
                    break;
                }
                lastRequestedState = postExecutionState;
                lastRequestingCallback = i;
            }
        }
        return lastRequestingCallback;
    }
}
