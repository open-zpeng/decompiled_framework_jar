package android.hardware.camera2.utils;

import com.android.internal.util.Preconditions;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class TaskDrainer<T> {
    private static final String TAG = "TaskDrainer";
    private final boolean DEBUG;
    private boolean mDrainFinished;
    private boolean mDraining;
    private final Set<T> mEarlyFinishedTaskSet;
    private final Executor mExecutor;
    private final DrainListener mListener;
    private final Object mLock;
    private final String mName;
    private final Set<T> mTaskSet;

    /* loaded from: classes.dex */
    public interface DrainListener {
        synchronized void onDrained();
    }

    public synchronized TaskDrainer(Executor executor, DrainListener listener) {
        this.DEBUG = false;
        this.mTaskSet = new HashSet();
        this.mEarlyFinishedTaskSet = new HashSet();
        this.mLock = new Object();
        this.mDraining = false;
        this.mDrainFinished = false;
        this.mExecutor = (Executor) Preconditions.checkNotNull(executor, "executor must not be null");
        this.mListener = (DrainListener) Preconditions.checkNotNull(listener, "listener must not be null");
        this.mName = null;
    }

    public synchronized TaskDrainer(Executor executor, DrainListener listener, String name) {
        this.DEBUG = false;
        this.mTaskSet = new HashSet();
        this.mEarlyFinishedTaskSet = new HashSet();
        this.mLock = new Object();
        this.mDraining = false;
        this.mDrainFinished = false;
        this.mExecutor = (Executor) Preconditions.checkNotNull(executor, "executor must not be null");
        this.mListener = (DrainListener) Preconditions.checkNotNull(listener, "listener must not be null");
        this.mName = name;
    }

    public synchronized void taskStarted(T task) {
        synchronized (this.mLock) {
            if (this.mDraining) {
                throw new IllegalStateException("Can't start more tasks after draining has begun");
            }
            if (!this.mEarlyFinishedTaskSet.remove(task) && !this.mTaskSet.add(task)) {
                throw new IllegalStateException("Task " + task + " was already started");
            }
        }
    }

    public synchronized void taskFinished(T task) {
        synchronized (this.mLock) {
            if (!this.mTaskSet.remove(task) && !this.mEarlyFinishedTaskSet.add(task)) {
                throw new IllegalStateException("Task " + task + " was already finished");
            }
            checkIfDrainFinished();
        }
    }

    public synchronized void beginDrain() {
        synchronized (this.mLock) {
            if (!this.mDraining) {
                this.mDraining = true;
                checkIfDrainFinished();
            }
        }
    }

    private synchronized void checkIfDrainFinished() {
        if (this.mTaskSet.isEmpty() && this.mDraining && !this.mDrainFinished) {
            this.mDrainFinished = true;
            postDrained();
        }
    }

    private synchronized void postDrained() {
        this.mExecutor.execute(new Runnable() { // from class: android.hardware.camera2.utils.-$$Lambda$TaskDrainer$Jb53sDskEXp_qIjiikQeCRx0wJs
            @Override // java.lang.Runnable
            public final void run() {
                TaskDrainer.this.mListener.onDrained();
            }
        });
    }
}
