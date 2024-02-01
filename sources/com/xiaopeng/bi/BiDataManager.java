package com.xiaopeng.bi;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import com.xiaopeng.bi.BiDataManager;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.biutil.BiLogUploader;
import com.xiaopeng.biutil.BiLogUploaderFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class BiDataManager {
    private static final String BID_SHARED_DISPLAY_SLIDE = "B003";
    public static final int ID_INPUT_METHOD_START = 30000;
    public static final int ID_INPUT_METHOD_STAT_SHOW_HIDE = 30001;
    public static final int ID_KEY_EVENT_START = 20000;
    public static final int ID_MULTI_SWIPE_FROM_TOP = 10001;
    public static final int ID_SHARED_DISPLAY_SLIDE = 31001;
    public static final int ID_SHARED_DISPLAY_START = 31000;
    private static final String PID_SHARED_DISPLAY_SLIDE = "P10911";
    private static final boolean SYSTEM_BI_ENABLED = true;
    private static final boolean SYSTEM_BI_INPUT_METHOD_ENABLED = true;
    private static final boolean SYSTEM_BI_KEY_EVENT_ENABLED = false;
    private static final boolean SYSTEM_BI_SHARED_DISPLAY_ENABLED = true;
    private static final String TAG = "BiDataManager";
    public static final HashMap<Integer, BiData> sBiData = new HashMap<>();
    public static String PAGE_ID = "P10332";
    public static String BUTTON_ID_MULTI_SWIPE_FROM_TOP = "B0001";
    public static String BUTTON_ID_KEY_EVENT_KEYCODE_BOSS = "B0002";
    public static String BUTTON_ID_KEY_EVENT_REAR_LEFT_BOSS = "B0003";
    public static String BUTTON_ID_KEY_EVENT_REAR_RIGHT_BOSS = "B0004";
    public static String BUTTON_ID_IME_ACTION_SHOW_HIDE = "B001";
    public static String IME_KEY_PACKAGENAME = "Initiate_package_name";
    public static String IME_KEY_STARTTIME = "startTime";
    public static String IME_KEY_RESULT = "result";
    public static String IME_VALUE_SHOW = WifiEnterpriseConfig.ENGINE_ENABLE;
    public static String IME_VALUE_HIDE = WifiEnterpriseConfig.ENGINE_DISABLE;
    public static String BID_KEYCODE = "B002";
    private static BiLogUploader sBiLogUploader = BiLogUploaderFactory.create();

    static {
        initBiData();
    }

    public static void initBiData() {
        sBiData.put(30001, new BiData(PAGE_ID, BUTTON_ID_IME_ACTION_SHOW_HIDE));
        sBiData.put(Integer.valueOf((int) ID_SHARED_DISPLAY_SLIDE), new BiData(PID_SHARED_DISPLAY_SLIDE, BID_SHARED_DISPLAY_SLIDE));
    }

    public static void sendKeyStatData(int keycode) {
        sendStatData(keycode + 20000);
    }

    public static void sendStatData(int id) {
        BiData data = sBiData.getOrDefault(Integer.valueOf(id), null);
        if (data != null) {
            sendStatData(data.pid, data.bid, "", "", "");
        }
    }

    public static void sendStatData(int id, Map<String, String> content) {
        BiData data = sBiData.getOrDefault(Integer.valueOf(id), null);
        if (data != null) {
            sendStatData(data.pid, data.bid, content);
        }
    }

    public static void sendStatData(final String pid, final String bid, final Map<String, String> content) {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.bi.BiDataManager.1
            @Override // java.lang.Runnable
            public void run() {
                BiLog bilog = BiLogFactory.create(pid, bid);
                for (String key : content.keySet()) {
                    bilog.push(key, (String) content.get(key));
                }
                BiDataManager.sBiLogUploader.submit(bilog);
            }
        }, null, 0);
    }

    public static void sendStatData(final String bid, final Map<String, String> content) {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.bi.BiDataManager.2
            @Override // java.lang.Runnable
            public void run() {
                BiLog bilog = BiLogFactory.create(bid);
                for (String key : content.keySet()) {
                    bilog.push(key, (String) content.get(key));
                }
                BiDataManager.sBiLogUploader.submit(bilog);
            }
        }, null, 0);
    }

    private static void sendStatData(String pid, String bid, String type, String result, String source) {
    }

    /* loaded from: classes3.dex */
    public static final class ThreadUtils {
        private static final ExecutorService sThreadPool = Executors.newFixedThreadPool(4);

        public static void execute(final Runnable runnable, final Runnable callback, final int priority) {
            try {
                if (!sThreadPool.isShutdown()) {
                    sThreadPool.execute(new Runnable() { // from class: com.xiaopeng.bi.-$$Lambda$BiDataManager$ThreadUtils$NT-82nk_SVkae762AY5i5JRZ28A
                        @Override // java.lang.Runnable
                        public final void run() {
                            BiDataManager.ThreadUtils.lambda$execute$0(priority, runnable, callback);
                        }
                    });
                }
            } catch (Exception e) {
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$execute$0(int priority, Runnable runnable, Runnable callback) {
            Process.setThreadPriority(priority);
            runnable.run();
            if (callback != null) {
                new Handler(Looper.myLooper()).post(callback);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class BiData implements Parcelable {
        public static final Parcelable.Creator<BiData> CREATOR = new Parcelable.Creator<BiData>() { // from class: com.xiaopeng.bi.BiDataManager.BiData.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public BiData createFromParcel(Parcel source) {
                return new BiData(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public BiData[] newArray(int size) {
                return new BiData[size];
            }
        };
        public String bid;
        public String pid;

        public BiData(String bid) {
            this.pid = "";
            this.bid = bid;
        }

        public BiData(int id, String pid, String bid) {
            this.pid = pid;
            this.bid = bid;
        }

        public BiData(String pid, String bid) {
            this.pid = pid;
            this.bid = bid;
        }

        private BiData(Parcel source) {
            this.pid = source.readString();
            this.bid = source.readString();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int parcelableFlags) {
            dest.writeString(this.pid);
            dest.writeString(this.bid);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }
    }
}
