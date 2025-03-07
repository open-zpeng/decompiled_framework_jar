package android.media;

import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseIntArray;

@Deprecated
/* loaded from: classes2.dex */
public abstract class MediaMetadataEditor {
    public static final int BITMAP_KEY_ARTWORK = 100;
    public static final int KEY_EDITABLE_MASK = 536870911;
    protected static final SparseIntArray METADATA_KEYS_TYPE = new SparseIntArray(17);
    protected static final int METADATA_TYPE_BITMAP = 2;
    protected static final int METADATA_TYPE_INVALID = -1;
    protected static final int METADATA_TYPE_LONG = 0;
    protected static final int METADATA_TYPE_RATING = 3;
    protected static final int METADATA_TYPE_STRING = 1;
    public static final int RATING_KEY_BY_OTHERS = 101;
    public static final int RATING_KEY_BY_USER = 268435457;
    private static final String TAG = "MediaMetadataEditor";
    protected long mEditableKeys;
    protected Bitmap mEditorArtwork;
    protected Bundle mEditorMetadata;
    protected MediaMetadata.Builder mMetadataBuilder;
    protected boolean mMetadataChanged = false;
    protected boolean mApplied = false;
    protected boolean mArtworkChanged = false;

    public abstract void apply();

    public synchronized void clear() {
        if (this.mApplied) {
            Log.e(TAG, "Can't clear a previously applied MediaMetadataEditor");
            return;
        }
        this.mEditorMetadata.clear();
        this.mEditorArtwork = null;
        this.mMetadataBuilder = new MediaMetadata.Builder();
    }

    public synchronized void addEditableKey(int key) {
        if (this.mApplied) {
            Log.e(TAG, "Can't change editable keys of a previously applied MetadataEditor");
            return;
        }
        if (key == 268435457) {
            this.mEditableKeys |= 536870911 & key;
            this.mMetadataChanged = true;
        } else {
            Log.e(TAG, "Metadata key " + key + " cannot be edited");
        }
    }

    public synchronized void removeEditableKeys() {
        if (this.mApplied) {
            Log.e(TAG, "Can't remove all editable keys of a previously applied MetadataEditor");
            return;
        }
        if (this.mEditableKeys != 0) {
            this.mEditableKeys = 0L;
            this.mMetadataChanged = true;
        }
    }

    public synchronized int[] getEditableKeys() {
        if (this.mEditableKeys == 268435457) {
            int[] keys = {268435457};
            return keys;
        }
        return null;
    }

    public synchronized MediaMetadataEditor putString(int key, String value) throws IllegalArgumentException {
        if (this.mApplied) {
            Log.e(TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        } else if (METADATA_KEYS_TYPE.get(key, -1) != 1) {
            throw new IllegalArgumentException("Invalid type 'String' for key " + key);
        } else {
            this.mEditorMetadata.putString(String.valueOf(key), value);
            this.mMetadataChanged = true;
            return this;
        }
    }

    public synchronized MediaMetadataEditor putLong(int key, long value) throws IllegalArgumentException {
        if (this.mApplied) {
            Log.e(TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        } else if (METADATA_KEYS_TYPE.get(key, -1) != 0) {
            throw new IllegalArgumentException("Invalid type 'long' for key " + key);
        } else {
            this.mEditorMetadata.putLong(String.valueOf(key), value);
            this.mMetadataChanged = true;
            return this;
        }
    }

    public synchronized MediaMetadataEditor putBitmap(int key, Bitmap bitmap) throws IllegalArgumentException {
        if (this.mApplied) {
            Log.e(TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        } else if (key != 100) {
            throw new IllegalArgumentException("Invalid type 'Bitmap' for key " + key);
        } else {
            this.mEditorArtwork = bitmap;
            this.mArtworkChanged = true;
            return this;
        }
    }

    public synchronized MediaMetadataEditor putObject(int key, Object value) throws IllegalArgumentException {
        if (this.mApplied) {
            Log.e(TAG, "Can't edit a previously applied MediaMetadataEditor");
            return this;
        }
        int i = METADATA_KEYS_TYPE.get(key, -1);
        if (i == 0) {
            if (value instanceof Long) {
                return putLong(key, ((Long) value).longValue());
            }
            throw new IllegalArgumentException("Not a non-null Long for key " + key);
        } else if (i == 1) {
            if (value != null && !(value instanceof String)) {
                throw new IllegalArgumentException("Not a String for key " + key);
            }
            return putString(key, (String) value);
        } else if (i != 2) {
            if (i == 3) {
                this.mEditorMetadata.putParcelable(String.valueOf(key), (Parcelable) value);
                this.mMetadataChanged = true;
                return this;
            }
            throw new IllegalArgumentException("Invalid key " + key);
        } else {
            if (value != null && !(value instanceof Bitmap)) {
                throw new IllegalArgumentException("Not a Bitmap for key " + key);
            }
            return putBitmap(key, (Bitmap) value);
        }
    }

    public synchronized long getLong(int key, long defaultValue) throws IllegalArgumentException {
        if (METADATA_KEYS_TYPE.get(key, -1) != 0) {
            throw new IllegalArgumentException("Invalid type 'long' for key " + key);
        }
        return this.mEditorMetadata.getLong(String.valueOf(key), defaultValue);
    }

    public synchronized String getString(int key, String defaultValue) throws IllegalArgumentException {
        if (METADATA_KEYS_TYPE.get(key, -1) != 1) {
            throw new IllegalArgumentException("Invalid type 'String' for key " + key);
        }
        return this.mEditorMetadata.getString(String.valueOf(key), defaultValue);
    }

    public synchronized Bitmap getBitmap(int key, Bitmap defaultValue) throws IllegalArgumentException {
        if (key != 100) {
            throw new IllegalArgumentException("Invalid type 'Bitmap' for key " + key);
        }
        return this.mEditorArtwork != null ? this.mEditorArtwork : defaultValue;
    }

    public synchronized Object getObject(int key, Object defaultValue) throws IllegalArgumentException {
        int i = METADATA_KEYS_TYPE.get(key, -1);
        if (i == 0) {
            if (this.mEditorMetadata.containsKey(String.valueOf(key))) {
                return Long.valueOf(this.mEditorMetadata.getLong(String.valueOf(key)));
            }
            return defaultValue;
        } else if (i == 1) {
            if (this.mEditorMetadata.containsKey(String.valueOf(key))) {
                return this.mEditorMetadata.getString(String.valueOf(key));
            }
            return defaultValue;
        } else {
            if (i != 2) {
                if (i == 3) {
                    if (this.mEditorMetadata.containsKey(String.valueOf(key))) {
                        return this.mEditorMetadata.getParcelable(String.valueOf(key));
                    }
                    return defaultValue;
                }
            } else if (key == 100) {
                return this.mEditorArtwork != null ? this.mEditorArtwork : defaultValue;
            }
            throw new IllegalArgumentException("Invalid key " + key);
        }
    }

    static {
        METADATA_KEYS_TYPE.put(0, 0);
        METADATA_KEYS_TYPE.put(14, 0);
        METADATA_KEYS_TYPE.put(9, 0);
        METADATA_KEYS_TYPE.put(8, 0);
        METADATA_KEYS_TYPE.put(1, 1);
        METADATA_KEYS_TYPE.put(13, 1);
        METADATA_KEYS_TYPE.put(7, 1);
        METADATA_KEYS_TYPE.put(2, 1);
        METADATA_KEYS_TYPE.put(3, 1);
        METADATA_KEYS_TYPE.put(15, 1);
        METADATA_KEYS_TYPE.put(4, 1);
        METADATA_KEYS_TYPE.put(5, 1);
        METADATA_KEYS_TYPE.put(6, 1);
        METADATA_KEYS_TYPE.put(11, 1);
        METADATA_KEYS_TYPE.put(100, 2);
        METADATA_KEYS_TYPE.put(101, 3);
        METADATA_KEYS_TYPE.put(268435457, 3);
    }
}
