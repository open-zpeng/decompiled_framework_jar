package android.os.caton;

import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.caton.Caton;
import android.util.Log;
import com.xiaopeng.cardiagnosis.aidl.IDealCaton;
import java.io.File;

/* loaded from: classes2.dex */
public class BugHunter {
    private static final long ANR_TRIGGER_TIME = 5000;
    private static final String SEPARATOR = "#";
    private static final String TAG = "OSBugHunter";
    private static IDealCaton dealCatonService;
    private static volatile boolean isInited = false;
    private static boolean connected = false;
    private static ServiceConnection serviceConnection = new ServiceConnection() { // from class: android.os.caton.BugHunter.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(BugHunter.TAG, "onServiceDisconnected succed");
            IDealCaton unused = BugHunter.dealCatonService = IDealCaton.Stub.asInterface(service);
            boolean unused2 = BugHunter.connected = true;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            Log.i(BugHunter.TAG, "onServiceDisconnected fail");
            IDealCaton unused = BugHunter.dealCatonService = null;
            boolean unused2 = BugHunter.connected = false;
        }
    };

    public static synchronized void init() {
        synchronized (BugHunter.class) {
            if (isInited) {
                return;
            }
            isInited = true;
            init(false, true);
        }
    }

    public static synchronized void init(boolean strictMode, boolean enableLogcat) {
        synchronized (BugHunter.class) {
            try {
                initCaton(strictMode, enableLogcat);
                bindService();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private static synchronized void initCaton(boolean strictMode, boolean enableLogcat) {
        synchronized (BugHunter.class) {
            long interval = 400;
            long threshold = 400;
            if (isUserVersion()) {
                interval = 60000;
                threshold = 1000;
            } else if (strictMode) {
                interval = 100;
                threshold = 100;
            } else {
                File flagFile = new File("/sdcard/Log/catonflag");
                if (flagFile.exists()) {
                    interval = 100;
                    threshold = 100;
                }
            }
            Caton.Builder builder = new Caton.Builder().monitorMode(Caton.MonitorMode.LOOPER).loggingEnabled(enableLogcat).collectInterval(interval).thresholdTime(threshold).callback(new Caton.Callback() { // from class: android.os.caton.BugHunter.1
                @Override // android.os.caton.Caton.Callback
                public void onBlockOccurs(String[] stackTraces, boolean isAnr, String packageName, long... blockArgs) {
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        int count = 0;
                        int len = stackTraces.length;
                        int i = len - 1;
                        while (true) {
                            if (i < 0) {
                                break;
                            }
                            count++;
                            if (count == len) {
                                stringBuilder.append(stackTraces[i]);
                                break;
                            }
                            stringBuilder.append(stackTraces[i]);
                            stringBuilder.append(BugHunter.SEPARATOR);
                            i--;
                        }
                        BugHunter.blockOccurs(stringBuilder.toString(), stackTraces[0], isAnr, packageName, blockArgs[0], blockArgs[1]);
                    } catch (Exception e) {
                        Log.i(BugHunter.TAG, e.toString());
                    }
                }
            });
            Caton.initialize(builder);
        }
    }

    private static void bindService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.xiaopeng.cardiagnosis", "com.xiaopeng.cardiagnosis.service.DealCatonService"));
        Context context = ActivityThread.currentApplication();
        context.bindService(intent, serviceConnection, 1);
    }

    public static void blockOccurs(String stackTraces, String stackTracesforMd5, boolean isAnr, String packageName, long stuckElapseTime, long threadElapseTime) {
        try {
            if (!connected) {
                bindService();
            }
            if (dealCatonService != null) {
                Log.e(TAG, "dealCaton stackTraces size = " + stackTraces.length());
                dealCatonService.dealCaton(stackTraces, stackTracesforMd5, isAnr, packageName, stuckElapseTime, threadElapseTime);
            }
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
    }

    public static void startMonitor() {
        if (Caton.getBlockHandler() != null) {
            Caton.getBlockHandler().startMonitor();
        }
    }

    public static void stopMonitor() {
        if (Caton.getBlockHandler() != null) {
            Caton.getBlockHandler().stopMonitor();
        }
    }

    public static void notifyCaton(long messageElapseTime, long threadElapseTime, String packageName) {
        if (Caton.getBlockHandler() != null) {
            Caton.getBlockHandler().notifyBlockOccurs(messageElapseTime >= 5000, packageName, messageElapseTime, threadElapseTime);
        }
    }

    public static boolean isUserVersion() {
        return "user".equals(Build.TYPE);
    }
}
