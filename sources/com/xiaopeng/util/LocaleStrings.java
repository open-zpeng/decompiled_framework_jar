package com.xiaopeng.util;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;
import com.xiaopeng.sysconfig.SysConfigManager;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes3.dex */
public class LocaleStrings {
    private static final String TAG = "LocaleStrings";
    private static String mLang = new String();
    private static LocaleStrings sService = null;
    private static StringMap mStringMap = null;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class StringMap {
        private Map<String, List<String>> array;
        private Map<String, String> string;

        public StringMap(Map<String, String> string, Map<String, List<String>> array) {
            this.string = string;
            this.array = array;
        }

        public Map<String, String> getString() {
            return this.string;
        }

        public Map<String, List<String>> getArray() {
            return this.array;
        }
    }

    private StringMap parseJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject stringObject = jsonObject.getJSONObject("string");
        Map<String, String> string = new HashMap<>();
        for (String key : stringObject.keySet()) {
            string.put(key, stringObject.getString(key));
        }
        JSONObject arrayObject = jsonObject.getJSONObject("array");
        Map<String, List<String>> array = new HashMap<>();
        Iterator<String> iter = arrayObject.keys();
        while (iter.hasNext()) {
            String key2 = iter.next();
            final JSONArray jsonArray = arrayObject.getJSONArray(key2);
            List<String> stringList = (List) IntStream.range(0, jsonArray.length()).mapToObj(new IntFunction() { // from class: com.xiaopeng.util.-$$Lambda$LocaleStrings$HlrcvmQvjflXZxCFcQWsZML5Tqo
                @Override // java.util.function.IntFunction
                public final Object apply(int i) {
                    return LocaleStrings.lambda$parseJson$0(jsonArray, i);
                }
            }).filter(new Predicate() { // from class: com.xiaopeng.util.-$$Lambda$LocaleStrings$ysW_fvT8H1f4ly8te8Kd64ujJxw
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean nonNull;
                    nonNull = Objects.nonNull((String) obj);
                    return nonNull;
                }
            }).collect(Collectors.toList());
            array.put(key2, stringList);
        }
        return new StringMap(string, array);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String lambda$parseJson$0(JSONArray jsonArray, int i) {
        try {
            return jsonArray.getString(i);
        } catch (JSONException e) {
            return null;
        }
    }

    private String getConfigContent(String lang) {
        String content = xpSysConfigUtil.getConfigContent(getConfigFileName(lang));
        if (content == null) {
            String defaultLang = "zh-CN".equals(SystemProperties.get("ro.product.locale")) ? "zh" : "en";
            return xpSysConfigUtil.getConfigContent(getConfigFileName(defaultLang));
        }
        return content;
    }

    private String getConfigFileName(String lang) {
        return "locale/strings_" + lang + ".json";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void loadStringMap(String lang) {
        if (lang.equals(mLang)) {
            return;
        }
        loadStringMapInner(lang);
        mLang = lang;
    }

    private void loadStringMapInner(String lang) {
        String fileContent = getConfigContent(lang);
        try {
            BufferedReader reader = new BufferedReader(new StringReader(fileContent));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    if (!line.contains("//") && !line.contains("#version")) {
                        sb.append(line);
                    }
                } else {
                    mStringMap = parseJson(sb.toString());
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "loadStringMap failed, " + e);
        }
    }

    private void registerReceiver() {
        ActivityThread.currentActivityThread().getApplication().registerReceiver(new BroadcastReceiver() { // from class: com.xiaopeng.util.LocaleStrings.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                LocaleStrings.this.loadStringMap(LocaleStrings.this.getLang(context));
            }
        }, new IntentFilter(Intent.ACTION_LOCALE_CHANGED));
    }

    private void registerSysConfigUpdate() {
        xpSysConfigUtil.registerConfigUpdateListener(new SysConfigManager.SysConfigListener() { // from class: com.xiaopeng.util.-$$Lambda$LocaleStrings$oGtKpRWY4PCPNtKMEYcc5EV_6xY
            @Override // com.xiaopeng.sysconfig.SysConfigManager.SysConfigListener
            public final void onSysConfigUpdated(String str) {
                LocaleStrings.lambda$registerSysConfigUpdate$1(LocaleStrings.this, str);
            }
        });
    }

    public static /* synthetic */ void lambda$registerSysConfigUpdate$1(LocaleStrings localeStrings, String fileName) {
        if (("locale/strings_" + mLang + ".json").equals(fileName)) {
            Log.i(TAG, "file=" + fileName + "updated.");
            localeStrings.loadStringMapInner(mLang);
        }
    }

    private LocaleStrings() {
        loadStringMap(getLang());
        registerReceiver();
        registerSysConfigUpdate();
    }

    private String getLang() {
        return getLang(ActivityThread.currentActivityThread().getApplication());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getLang(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    public void update() {
        loadStringMap(getLang());
    }

    public synchronized String getString(String key) {
        return (mStringMap == null || !mStringMap.getString().containsKey(key)) ? null : mStringMap.getString().get(key);
    }

    public synchronized String[] getStringArray(String key) {
        return (mStringMap == null || !mStringMap.getArray().containsKey(key)) ? null : (String[]) mStringMap.getArray().get(key).toArray(new String[0]);
    }

    public static LocaleStrings getInstance() {
        if (sService == null) {
            synchronized (LocaleStrings.class) {
                if (sService == null) {
                    sService = new LocaleStrings();
                }
            }
        }
        return sService;
    }
}
