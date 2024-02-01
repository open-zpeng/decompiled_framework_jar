package com.xiaopeng.util;

import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class xpTextUtils {
    public static String getValue(File file) {
        if (file != null) {
            try {
                if (file.exists() && file.isFile()) {
                    StringBuilder builder = new StringBuilder();
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                    BufferedReader br = new BufferedReader(reader);
                    while (true) {
                        String line = br.readLine();
                        if (line != null) {
                            builder.append(line);
                        } else {
                            br.close();
                            reader.close();
                            return builder.toString();
                        }
                    }
                } else {
                    return "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static Object getValue(String name, JSONObject object) {
        if (object != null && !TextUtils.isEmpty(name) && object.has(name)) {
            try {
                return object.get(name);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static <T> T getValue(String name, JSONObject jsonObject, T defaultValue) {
        try {
            if (!TextUtils.isEmpty(name) && jsonObject != null) {
                return !jsonObject.has(name) ? defaultValue : (T) jsonObject.get(name);
            }
            return defaultValue;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getValue(String name, String jsonText, T defaultValue) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(jsonText)) {
            return defaultValue;
        }
        JSONObject jsonObject = toJsonObject(jsonText);
        return (T) getValue(name, jsonObject, defaultValue);
    }

    public static String[] getArrays(File file) {
        if (file != null) {
            try {
                if (file.exists() && file.isFile()) {
                    List<String> list = new ArrayList<>();
                    new StringBuilder();
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                    BufferedReader br = new BufferedReader(reader);
                    while (true) {
                        String line = br.readLine();
                        if (line != null) {
                            if (!TextUtils.isEmpty(line)) {
                                list.add(line);
                            }
                        } else {
                            br.close();
                            reader.close();
                            return (String[]) list.toArray(new String[list.size()]);
                        }
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String getValueByVersion(File a, File b, String version) {
        if (TextUtils.isEmpty(version)) {
            return null;
        }
        String aValue = getValue(a);
        String bValue = getValue(b);
        if (TextUtils.isEmpty(aValue) && TextUtils.isEmpty(bValue)) {
            return null;
        }
        if (!TextUtils.isEmpty(aValue) && TextUtils.isEmpty(bValue)) {
            return new JSONObject(aValue).toString();
        }
        if (TextUtils.isEmpty(aValue) && !TextUtils.isEmpty(bValue)) {
            return new JSONObject(bValue).toString();
        }
        if (!TextUtils.isEmpty(aValue) && !TextUtils.isEmpty(bValue)) {
            JSONObject aJson = new JSONObject(aValue);
            JSONObject bJson = new JSONObject(bValue);
            double aVersion = toDouble(getValue(version, aJson), Double.valueOf((double) FeatureOption.FO_BOOT_POLICY_CPU)).doubleValue();
            double bVersion = toDouble(getValue(version, bJson), Double.valueOf((double) FeatureOption.FO_BOOT_POLICY_CPU)).doubleValue();
            if (aVersion >= bVersion) {
                return aJson.toString();
            }
            return bJson.toString();
        }
        return null;
    }

    public static <K, V> String toJsonString(HashMap<K, V> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        try {
            JSONObject object = new JSONObject();
            for (K k : values.keySet()) {
                String _k = String.valueOf(k);
                V _v = !TextUtils.isEmpty(_k) ? values.get(_k) : null;
                if (_v != null) {
                    object.put(_k, _v);
                }
            }
            return object.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String toJsonString(JSONObject[] objects) {
        if (objects == null) {
            try {
                if (objects.length != 0) {
                    JSONArray array = new JSONArray();
                    for (JSONObject object : objects) {
                        if (object != null) {
                            array.put(object);
                        }
                    }
                    return array.toString();
                }
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    public static JSONObject toJsonObject(String jsonText) {
        if (TextUtils.isEmpty(jsonText)) {
            return null;
        }
        try {
            return new JSONObject(jsonText);
        } catch (Exception e) {
            return null;
        }
    }

    public static JSONObject[] toJsonObjects(String jsonText) {
        if (TextUtils.isEmpty(jsonText)) {
            return null;
        }
        try {
            JSONArray array = new JSONArray(jsonText);
            int length = array.length();
            JSONObject[] objects = new JSONObject[length];
            for (int i = 0; i < length; i++) {
                objects[i] = array.getJSONObject(i);
            }
            return objects;
        } catch (Exception e) {
            return null;
        }
    }

    public static int[] toIntArray(String[] arrays) {
        if (arrays != null) {
            try {
                if (arrays.length > 0) {
                    return Arrays.asList(arrays).stream().mapToInt(new ToIntFunction() { // from class: com.xiaopeng.util.-$$Lambda$wddj3-hVVrg0MkscpMtYt3BzY8Y
                        @Override // java.util.function.ToIntFunction
                        public final int applyAsInt(Object obj) {
                            return Integer.parseInt((String) obj);
                        }
                    }).toArray();
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static double[] toDoubleArray(String[] arrays) {
        if (arrays != null) {
            try {
                if (arrays.length > 0) {
                    return Arrays.asList(arrays).stream().mapToDouble(new ToDoubleFunction() { // from class: com.xiaopeng.util.-$$Lambda$KeqLprO1LGYsuaXq6E-hGlF2J2A
                        @Override // java.util.function.ToDoubleFunction
                        public final double applyAsDouble(Object obj) {
                            return Double.parseDouble((String) obj);
                        }
                    }).toArray();
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static String[] toStringArray(String content, String regex) {
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(regex)) {
            return content.split(regex);
        }
        return null;
    }

    public static Boolean toBoolean(Object value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            String stringValue = (String) value;
            if ("true".equalsIgnoreCase(stringValue)) {
                return true;
            }
            if ("false".equalsIgnoreCase(stringValue)) {
                return false;
            }
        }
        return defaultValue;
    }

    public static Double toDouble(Object value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public static Integer toInteger(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        }
        if (value instanceof String) {
            try {
                return Integer.valueOf((int) Double.parseDouble((String) value));
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public static Long toLong(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return Long.valueOf(((Number) value).longValue());
        }
        if (value instanceof String) {
            try {
                return Long.valueOf((long) Double.parseDouble((String) value));
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public static String toString(Object value) {
        if (value instanceof String) {
            return (String) value;
        }
        if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }

    public static double checkDouble(double d) throws JSONException {
        if (!Double.isInfinite(d) && !Double.isNaN(d)) {
            return d;
        }
        throw new JSONException("Forbidden numeric value: " + d);
    }

    public static JSONException typeMismatch(Object indexOrName, Object actual, String requiredType) throws JSONException {
        if (actual == null) {
            throw new JSONException("Value at " + indexOrName + " is null.");
        }
        throw new JSONException("Value " + actual + " at " + indexOrName + " of type " + actual.getClass().getName() + " cannot be converted to " + requiredType);
    }

    public static JSONException typeMismatch(Object actual, String requiredType) throws JSONException {
        if (actual == null) {
            throw new JSONException("Value is null.");
        }
        throw new JSONException("Value " + actual + " of type " + actual.getClass().getName() + " cannot be converted to " + requiredType);
    }
}
