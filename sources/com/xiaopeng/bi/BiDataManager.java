package com.xiaopeng.bi;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.text.TextUtils;
import com.xiaopeng.bi.BiDataManager;
import com.xiaopeng.biutil.BiLog;
import com.xiaopeng.biutil.BiLogFactory;
import com.xiaopeng.biutil.BiLogUploader;
import com.xiaopeng.biutil.BiLogUploaderFactory;
import com.xiaopeng.util.xpLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes3.dex */
public class BiDataManager {
    public static final int ID_KEY_EVENT_START = 20000;
    public static final int ID_MULTI_SWIPE_FROM_TOP = 10001;
    private static final boolean SYSTEM_BI_ENABLED = false;
    private static final String TAG = "BiDataManager";
    public static final HashMap<Integer, BiData> sBiData = new HashMap<>();
    public static String BID_KEYCODE = "B002";
    private static BiLogUploader sBiLogUploader = BiLogUploaderFactory.create();

    static {
        initBiData();
    }

    public static void initBiData() {
    }

    public static void sendKeyStatData(int keycode) {
    }

    public static void sendStatData(int id) {
    }

    public static void sendStatData(final String bid, final Map<String, String> content) {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.bi.BiDataManager.1
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

    private static void sendStatData(final String pid, final String bid, final String type, final String result, final String source) {
        if (!TextUtils.isEmpty(bid)) {
            ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.bi.BiDataManager.2
                @Override // java.lang.Runnable
                public void run() {
                    BiLog log = BiLogFactory.create(pid, bid);
                    BiLogUploader uploader = BiLogUploaderFactory.create();
                    uploader.submit(log);
                    xpLogger.i(BiDataManager.TAG, "sendStatData pid:" + pid + " bid:" + bid + " type:" + type + " result:" + result + " source:" + source);
                }
            }, null, 0);
        }
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
            this.bid = bid;
        }

        public BiData(int id, String pid, String bid) {
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
