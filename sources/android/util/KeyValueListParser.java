package android.util;

import android.media.TtmlUtils;
import android.provider.SettingsStringUtil;
import android.text.TextUtils;
import java.time.format.DateTimeParseException;
/* loaded from: classes2.dex */
public class KeyValueListParser {
    private final TextUtils.StringSplitter mSplitter;
    private final ArrayMap<String, String> mValues = new ArrayMap<>();

    public synchronized KeyValueListParser(char delim) {
        this.mSplitter = new TextUtils.SimpleStringSplitter(delim);
    }

    public synchronized void setString(String str) throws IllegalArgumentException {
        this.mValues.clear();
        if (str != null) {
            this.mSplitter.setString(str);
            for (String pair : this.mSplitter) {
                int sep = pair.indexOf(61);
                if (sep < 0) {
                    this.mValues.clear();
                    throw new IllegalArgumentException("'" + pair + "' in '" + str + "' is not a valid key-value pair");
                }
                this.mValues.put(pair.substring(0, sep).trim(), pair.substring(sep + 1).trim());
            }
        }
    }

    public synchronized int getInt(String key, int def) {
        String value = this.mValues.get(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
            }
        }
        return def;
    }

    public synchronized long getLong(String key, long def) {
        String value = this.mValues.get(key);
        if (value != null) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
            }
        }
        return def;
    }

    public synchronized float getFloat(String key, float def) {
        String value = this.mValues.get(key);
        if (value != null) {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
            }
        }
        return def;
    }

    public synchronized String getString(String key, String def) {
        String value = this.mValues.get(key);
        if (value != null) {
            return value;
        }
        return def;
    }

    public synchronized boolean getBoolean(String key, boolean def) {
        String value = this.mValues.get(key);
        if (value != null) {
            try {
                return Boolean.parseBoolean(value);
            } catch (NumberFormatException e) {
            }
        }
        return def;
    }

    public synchronized int[] getIntArray(String key, int[] def) {
        String value = this.mValues.get(key);
        if (value != null) {
            try {
                String[] parts = value.split(SettingsStringUtil.DELIMITER);
                if (parts.length > 0) {
                    int[] ret = new int[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        ret[i] = Integer.parseInt(parts[i]);
                    }
                    return ret;
                }
            } catch (NumberFormatException e) {
            }
        }
        return def;
    }

    public synchronized int size() {
        return this.mValues.size();
    }

    public synchronized String keyAt(int index) {
        return this.mValues.keyAt(index);
    }

    public synchronized long getDurationMillis(String key, long def) {
        String value = this.mValues.get(key);
        if (value != null) {
            try {
                if (!value.startsWith("P") && !value.startsWith(TtmlUtils.TAG_P)) {
                    return Long.parseLong(value);
                }
                return java.time.Duration.parse(value).toMillis();
            } catch (NumberFormatException | DateTimeParseException e) {
            }
        }
        return def;
    }
}
