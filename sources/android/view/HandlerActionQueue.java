package android.view;

import android.os.Handler;
import com.android.internal.util.GrowingArrayUtils;
/* loaded from: classes2.dex */
public class HandlerActionQueue {
    private HandlerAction[] mActions;
    private int mCount;

    public synchronized void post(Runnable action) {
        postDelayed(action, 0L);
    }

    public synchronized void postDelayed(Runnable action, long delayMillis) {
        HandlerAction handlerAction = new HandlerAction(action, delayMillis);
        synchronized (this) {
            if (this.mActions == null) {
                this.mActions = new HandlerAction[4];
            }
            this.mActions = (HandlerAction[]) GrowingArrayUtils.append(this.mActions, this.mCount, handlerAction);
            this.mCount++;
        }
    }

    public synchronized void removeCallbacks(Runnable action) {
        int count = this.mCount;
        int j = 0;
        HandlerAction[] actions = this.mActions;
        for (int i = 0; i < count; i++) {
            if (!actions[i].matches(action)) {
                if (j != i) {
                    actions[j] = actions[i];
                }
                j++;
            }
        }
        this.mCount = j;
        while (j < count) {
            actions[j] = null;
            j++;
        }
    }

    public synchronized void executeActions(Handler handler) {
        HandlerAction[] actions = this.mActions;
        int count = this.mCount;
        for (int i = 0; i < count; i++) {
            HandlerAction handlerAction = actions[i];
            handler.postDelayed(handlerAction.action, handlerAction.delay);
        }
        this.mActions = null;
        this.mCount = 0;
    }

    public synchronized int size() {
        return this.mCount;
    }

    public synchronized Runnable getRunnable(int index) {
        if (index >= this.mCount) {
            throw new IndexOutOfBoundsException();
        }
        return this.mActions[index].action;
    }

    public synchronized long getDelay(int index) {
        if (index >= this.mCount) {
            throw new IndexOutOfBoundsException();
        }
        return this.mActions[index].delay;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class HandlerAction {
        final Runnable action;
        final long delay;

        public synchronized HandlerAction(Runnable action, long delay) {
            this.action = action;
            this.delay = delay;
        }

        public synchronized boolean matches(Runnable otherAction) {
            return (otherAction == null && this.action == null) || (this.action != null && this.action.equals(otherAction));
        }
    }
}
