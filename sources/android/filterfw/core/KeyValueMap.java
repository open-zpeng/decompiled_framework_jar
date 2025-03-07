package android.filterfw.core;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class KeyValueMap extends HashMap<String, Object> {
    public void setKeyValues(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new RuntimeException("Key-Value arguments passed into setKeyValues must be an alternating list of keys and values!");
        }
        for (int i = 0; i < keyValues.length; i += 2) {
            if (!(keyValues[i] instanceof String)) {
                throw new RuntimeException("Key-value argument " + i + " must be a key of type String, but found an object of type " + keyValues[i].getClass() + "!");
            }
            String key = (String) keyValues[i];
            Object value = keyValues[i + 1];
            put(key, value);
        }
    }

    public static KeyValueMap fromKeyValues(Object... keyValues) {
        KeyValueMap result = new KeyValueMap();
        result.setKeyValues(keyValues);
        return result;
    }

    public String getString(String key) {
        Object result = get(key);
        if (result != null) {
            return (String) result;
        }
        return null;
    }

    public int getInt(String key) {
        Object result = get(key);
        return (result != null ? (Integer) result : null).intValue();
    }

    public float getFloat(String key) {
        Object result = get(key);
        return (result != null ? (Float) result : null).floatValue();
    }

    @Override // java.util.AbstractMap
    public String toString() {
        StringWriter writer = new StringWriter();
        for (Map.Entry<String, Object> entry : entrySet()) {
            Object value = entry.getValue();
            String valueString = value instanceof String ? "\"" + value + "\"" : value.toString();
            writer.write(entry.getKey() + " = " + valueString + ";\n");
        }
        return writer.toString();
    }
}
