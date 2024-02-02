package com.xiaopeng.biutil;

import android.util.Log;
import org.json.JSONObject;
/* loaded from: classes3.dex */
public class BiLog {
    private static final String TAG = "BiLog";
    private JSONObject head = new JSONObject();
    protected JSONObject params = new JSONObject();

    public BiLog(String moduleId, String pageId, String buttonId) {
        try {
            this.head.put("moduleId", moduleId);
            this.head.put("pageId", pageId);
            this.head.put("buttonId", buttonId);
            this.head.put("happenTime", System.currentTimeMillis());
        } catch (Exception e) {
            Log.e(TAG, "BiLog failed " + e);
        }
    }

    public BiLog(String pageId, String buttonId) {
        try {
            this.head.put("pageId", pageId);
            this.head.put("buttonId", buttonId);
            this.head.put("happenTime", System.currentTimeMillis());
        } catch (Exception e) {
            Log.e(TAG, "BiLog failed " + e);
        }
    }

    public BiLog(String buttonId) {
        try {
            this.head.put("buttonId", buttonId);
            this.head.put("happenTime", System.currentTimeMillis());
        } catch (Exception e) {
            Log.e(TAG, "BiLog failed " + e);
        }
    }

    public void push(String name, String value) {
        try {
            this.params.put(name, value);
        } catch (Exception e) {
            Log.e(TAG, "push failed " + e);
        }
    }

    public String getString() {
        try {
            JSONObject jsonLog = new JSONObject(this.head.toString());
            jsonLog.put("params", this.params);
            String logstr = jsonLog.toString();
            return logstr;
        } catch (Exception e) {
            Log.e(TAG, "getString failed " + e);
            return null;
        }
    }
}
