package android.os.caton;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.ComponentName;
import android.os.Debug;
import android.os.RemoteException;
import android.os.caton.Caton;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes2.dex */
public class BlockHandler {
    private static final String MAIN_THREAD = "main";
    private static final String TAG = "OSBlockHandler";
    private static final String THREAD_TAG = "-----";
    private static ExecutorService mExecutorService = Executors.newFixedThreadPool(1);
    private Caton.Callback mCallback;
    private Collector mCollector;
    private StringBuilder mStackBuilder = new StringBuilder(4096);
    private List<String> mStackInfoList = new ArrayList();

    public BlockHandler(Collector collector, Caton.Callback callback) {
        this.mCollector = collector;
        this.mCallback = callback;
    }

    public void notifyBlockOccurs(boolean needCheckAnr, String packageName, long... blockArgs) {
        if (this.mCallback != null && !Debug.isDebuggerConnected()) {
            StackTraceElement[][] stackTraces = this.mCollector.getStackTraceInfo();
            int[] stacktraceRepeats = this.mCollector.getStackTraceRepeats();
            mExecutorService.execute(getRunnable(stackTraces, stacktraceRepeats, needCheckAnr, packageName, blockArgs));
        }
    }

    private Runnable getRunnable(final StackTraceElement[][] stackTraces, final int[] stacktraceRepeats, final boolean needCheckAnr, final String packageName, final long... blockArgs) {
        Runnable r = new Runnable() { // from class: android.os.caton.BlockHandler.1
            @Override // java.lang.Runnable
            public void run() {
                if (!BlockHandler.isApplicationInBackground(packageName)) {
                    BlockHandler.this.mStackInfoList.clear();
                    StackTraceElement[][] stackTraceElementArr = stackTraces;
                    if (stackTraceElementArr != null && stackTraceElementArr.length > 0) {
                        int i = 0;
                        int stackSize = 0;
                        for (StackTraceElement[] elementArray : stackTraceElementArr) {
                            if (elementArray != null && elementArray.length > 0) {
                                if (BlockHandler.this.mStackBuilder.length() > 0) {
                                    BlockHandler.this.mStackBuilder.delete(0, BlockHandler.this.mStackBuilder.length());
                                }
                                StringBuilder sb = BlockHandler.this.mStackBuilder;
                                sb.append(BlockHandler.THREAD_TAG);
                                sb.append(BlockHandler.MAIN_THREAD);
                                sb.append(" repeat ");
                                sb.append(stacktraceRepeats[i]);
                                sb.append("\n");
                                int size = 0;
                                for (StackTraceElement element : elementArray) {
                                    size += element.toString().length();
                                    StringBuilder sb2 = BlockHandler.this.mStackBuilder;
                                    sb2.append("\tat ");
                                    sb2.append(element.toString());
                                    sb2.append("\n");
                                }
                            }
                            stackSize += BlockHandler.this.mStackBuilder.toString().length();
                            BlockHandler.this.mStackInfoList.add(BlockHandler.this.mStackBuilder.toString());
                            i++;
                        }
                    }
                    String[] stackTraces2 = (String[]) BlockHandler.this.mStackInfoList.toArray(new String[0]);
                    if (stackTraces2.length != 0) {
                        BlockHandler.this.mCallback.onBlockOccurs(stackTraces2, needCheckAnr, packageName, blockArgs);
                    }
                }
            }
        };
        return r;
    }

    public static boolean isApplicationInBackground(String packageName) {
        try {
            List<ActivityManager.RunningTaskInfo> tasks = ActivityTaskManager.getService().getTasks(1);
            if (tasks != null && !tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                Log.i(TAG, "top Activity is " + topActivity.getPackageName());
                if (!topActivity.getPackageName().equals(packageName)) {
                    return true;
                }
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    public void startMonitor() {
        this.mCollector.start();
    }

    public void stopMonitor() {
        this.mCollector.stop();
    }
}
