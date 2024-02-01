package android.view;

import android.os.Looper;
/* loaded from: classes2.dex */
public class BatchedInputEventReceiver extends InputEventReceiver {
    private final BatchedInputRunnable mBatchedInputRunnable;
    private boolean mBatchedInputScheduled;
    Choreographer mChoreographer;

    private protected BatchedInputEventReceiver(InputChannel inputChannel, Looper looper, Choreographer choreographer) {
        super(inputChannel, looper);
        this.mBatchedInputRunnable = new BatchedInputRunnable();
        this.mChoreographer = choreographer;
    }

    @Override // android.view.InputEventReceiver
    public synchronized void onBatchedInputEventPending() {
        scheduleBatchedInput();
    }

    @Override // android.view.InputEventReceiver
    public synchronized void dispose() {
        unscheduleBatchedInput();
        super.dispose();
    }

    synchronized void doConsumeBatchedInput(long frameTimeNanos) {
        if (this.mBatchedInputScheduled) {
            this.mBatchedInputScheduled = false;
            if (consumeBatchedInputEvents(frameTimeNanos) && frameTimeNanos != -1) {
                scheduleBatchedInput();
            }
        }
    }

    private synchronized void scheduleBatchedInput() {
        if (!this.mBatchedInputScheduled) {
            this.mBatchedInputScheduled = true;
            this.mChoreographer.postCallback(0, this.mBatchedInputRunnable, null);
        }
    }

    private synchronized void unscheduleBatchedInput() {
        if (this.mBatchedInputScheduled) {
            this.mBatchedInputScheduled = false;
            this.mChoreographer.removeCallbacks(0, this.mBatchedInputRunnable, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class BatchedInputRunnable implements Runnable {
        private BatchedInputRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            BatchedInputEventReceiver.this.doConsumeBatchedInput(BatchedInputEventReceiver.this.mChoreographer.getFrameTimeNanos());
        }
    }
}
