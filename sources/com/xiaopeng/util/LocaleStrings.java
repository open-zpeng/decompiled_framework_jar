package com.xiaopeng.util;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class LocaleStrings {
    private static final String TAG = "LocaleStrings";
    private static String mLang = ActivityThread.currentActivityThread().getApplication().getResources().getConfiguration().locale.getLanguage();
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
            }).filter(new Predicate() { // from class: com.xiaopeng.util.-$$Lambda$ysW_fvT8H1f4ly8te8Kd64ujJxw
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Objects.nonNull((String) obj);
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

    /* JADX INFO: Access modifiers changed from: private */
    public static String getPath(String lang) {
        String file = "/system/etc/xuiservice/locale/strings_" + lang + ".json";
        if (new File(file).exists()) {
            return file;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("/system/etc/xuiservice/locale/strings_");
        sb.append("zh-CN".equals(SystemProperties.get("ro.product.locale")) ? "zh" : "en");
        sb.append(".json");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void loadStringMap(String file) {
        String str;
        String str2;
        BufferedReader reader = null;
        StringBuilder buffer = new StringBuilder();
        try {
            try {
                List<String> fileContents = (List) Files.lines(Paths.get(file, new String[0])).filter(new Predicate() { // from class: com.xiaopeng.util.-$$Lambda$LocaleStrings$1o8NyIJtKfl6Tkp6Z_pM4lOM6SA
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return LocaleStrings.lambda$loadStringMap$1((String) obj);
                    }
                }).collect(Collectors.toCollection(new Supplier() { // from class: com.xiaopeng.util.-$$Lambda$OGSS2qx6njxlnp0dnKb4lA3jnw8
                    @Override // java.util.function.Supplier
                    public final Object get() {
                        return new ArrayList();
                    }
                }));
                for (String line : fileContents) {
                    try {
                        buffer.append(line);
                    } catch (Exception e) {
                        e = e;
                        Log.e(TAG, "loadStringMap failed, " + e);
                        try {
                            reader.close();
                        } catch (Exception e2) {
                            str2 = TAG;
                            str = "colse " + file + " failed, " + e2;
                            Log.e(str2, str);
                        }
                    } catch (Throwable th) {
                        th = th;
                        try {
                            reader.close();
                        } catch (Exception e3) {
                            Log.e(TAG, "colse " + file + " failed, " + e3);
                        }
                        throw th;
                    }
                }
                reader = new BufferedReader(new StringReader(buffer.toString()));
                mStringMap = parseJson((String) reader.lines().collect(Collectors.joining()));
                try {
                    reader.close();
                } catch (Exception e4) {
                    str2 = TAG;
                    str = "colse " + file + " failed, " + e4;
                    Log.e(str2, str);
                }
            } catch (Exception e5) {
                e = e5;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$loadStringMap$1(String line) {
        return !line.contains("//");
    }

    private void registerReceiver() {
        ActivityThread.currentActivityThread().getApplication().registerReceiver(new BroadcastReceiver() { // from class: com.xiaopeng.util.LocaleStrings.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String lang = context.getResources().getConfiguration().locale.getLanguage();
                if (!LocaleStrings.mLang.equals(lang)) {
                    Log.i(LocaleStrings.TAG, "onChangeLocale: " + LocaleStrings.mLang + " -> " + lang);
                    LocaleStrings.this.loadStringMap(LocaleStrings.getPath(lang));
                    String unused = LocaleStrings.mLang = lang;
                }
            }
        }, new IntentFilter(Intent.ACTION_LOCALE_CHANGED));
    }

    private LocaleStrings() {
        Log.i(TAG, "lanuage is " + mLang);
        loadStringMap(getPath(mLang));
        registerReceiver();
    }

    public void update() {
        String lang = ActivityThread.currentActivityThread().getApplication().getResources().getConfiguration().locale.getLanguage();
        if (!mLang.equals(lang)) {
            Log.i(TAG, "updateLocale: " + mLang + " -> " + lang);
            loadStringMap(getPath(lang));
            mLang = lang;
        }
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
