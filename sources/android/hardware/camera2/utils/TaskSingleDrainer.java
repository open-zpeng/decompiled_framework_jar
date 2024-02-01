package android.hardware.camera2.utils;

import android.hardware.camera2.utils.TaskDrainer;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class TaskSingleDrainer {
    private final Object mSingleTask = new Object();
    private final TaskDrainer<Object> mTaskDrainer;

    public synchronized TaskSingleDrainer(Executor executor, TaskDrainer.DrainListener listener) {
        this.mTaskDrainer = new TaskDrainer<>(executor, listener);
    }

    public synchronized TaskSingleDrainer(Executor executor, TaskDrainer.DrainListener listener, String name) {
        this.mTaskDrainer = new TaskDrainer<>(executor, listener, name);
    }

    public synchronized void taskStarted() {
        this.mTaskDrainer.taskStarted(this.mSingleTask);
    }

    public synchronized void beginDrain() {
        this.mTaskDrainer.beginDrain();
    }

    public synchronized void taskFinished() {
        this.mTaskDrainer.taskFinished(this.mSingleTask);
    }
}
