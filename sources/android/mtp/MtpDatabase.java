package android.mtp;

import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScanner;
import android.mtp.MtpStorageManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.google.android.collect.Sets;
import dalvik.system.CloseGuard;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;
/* loaded from: classes2.dex */
public class MtpDatabase implements AutoCloseable {
    private static final int[] AUDIO_PROPERTIES;
    private static final int[] DEVICE_PROPERTIES;
    private static final int[] FILE_PROPERTIES;
    private static final int[] IMAGE_PROPERTIES;
    private static final String NO_MEDIA = ".nomedia";
    private static final String PATH_WHERE = "_data=?";
    private static final int[] PLAYBACK_FORMATS;
    private static final int[] VIDEO_PROPERTIES;
    private int mBatteryLevel;
    private int mBatteryScale;
    private final Context mContext;
    private SharedPreferences mDeviceProperties;
    private int mDeviceType;
    private MtpStorageManager mManager;
    private final ContentProviderClient mMediaProvider;
    private final MediaScanner mMediaScanner;
    private long mNativeContext;
    private final Uri mObjectsUri;
    private MtpServer mServer;
    private final String mVolumeName;
    private static final String TAG = MtpDatabase.class.getSimpleName();
    private static final String[] ID_PROJECTION = {"_id"};
    private static final String[] PATH_PROJECTION = {"_data"};
    private final AtomicBoolean mClosed = new AtomicBoolean();
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final HashMap<String, MtpStorage> mStorageMap = new HashMap<>();
    private final HashMap<Integer, MtpPropertyGroup> mPropertyGroupsByProperty = new HashMap<>();
    private final HashMap<Integer, MtpPropertyGroup> mPropertyGroupsByFormat = new HashMap<>();
    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() { // from class: android.mtp.MtpDatabase.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                MtpDatabase.this.mBatteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int newLevel = intent.getIntExtra("level", 0);
                if (newLevel != MtpDatabase.this.mBatteryLevel) {
                    MtpDatabase.this.mBatteryLevel = newLevel;
                    if (MtpDatabase.this.mServer != null) {
                        MtpDatabase.this.mServer.sendDevicePropertyChanged(MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL);
                    }
                }
            }
        }
    };

    private final native void native_finalize();

    private final native void native_setup();

    static {
        System.loadLibrary("media_jni");
        PLAYBACK_FORMATS = new int[]{12288, 12289, 12292, 12293, 12296, 12297, 12299, MtpConstants.FORMAT_EXIF_JPEG, MtpConstants.FORMAT_TIFF_EP, MtpConstants.FORMAT_BMP, MtpConstants.FORMAT_GIF, MtpConstants.FORMAT_JFIF, MtpConstants.FORMAT_PNG, MtpConstants.FORMAT_TIFF, MtpConstants.FORMAT_WMA, MtpConstants.FORMAT_OGG, MtpConstants.FORMAT_AAC, MtpConstants.FORMAT_MP4_CONTAINER, MtpConstants.FORMAT_MP2, MtpConstants.FORMAT_3GP_CONTAINER, MtpConstants.FORMAT_ABSTRACT_AV_PLAYLIST, MtpConstants.FORMAT_WPL_PLAYLIST, MtpConstants.FORMAT_M3U_PLAYLIST, MtpConstants.FORMAT_PLS_PLAYLIST, MtpConstants.FORMAT_XML_DOCUMENT, MtpConstants.FORMAT_FLAC, MtpConstants.FORMAT_DNG, MtpConstants.FORMAT_HEIF};
        FILE_PROPERTIES = new int[]{MtpConstants.PROPERTY_STORAGE_ID, MtpConstants.PROPERTY_OBJECT_FORMAT, MtpConstants.PROPERTY_PROTECTION_STATUS, MtpConstants.PROPERTY_OBJECT_SIZE, MtpConstants.PROPERTY_OBJECT_FILE_NAME, MtpConstants.PROPERTY_DATE_MODIFIED, MtpConstants.PROPERTY_PERSISTENT_UID, MtpConstants.PROPERTY_PARENT_OBJECT, MtpConstants.PROPERTY_NAME, MtpConstants.PROPERTY_DISPLAY_NAME, MtpConstants.PROPERTY_DATE_ADDED};
        AUDIO_PROPERTIES = new int[]{MtpConstants.PROPERTY_ARTIST, MtpConstants.PROPERTY_ALBUM_NAME, MtpConstants.PROPERTY_ALBUM_ARTIST, MtpConstants.PROPERTY_TRACK, MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE, MtpConstants.PROPERTY_DURATION, MtpConstants.PROPERTY_GENRE, MtpConstants.PROPERTY_COMPOSER, MtpConstants.PROPERTY_AUDIO_WAVE_CODEC, MtpConstants.PROPERTY_BITRATE_TYPE, MtpConstants.PROPERTY_AUDIO_BITRATE, MtpConstants.PROPERTY_NUMBER_OF_CHANNELS, MtpConstants.PROPERTY_SAMPLE_RATE};
        VIDEO_PROPERTIES = new int[]{MtpConstants.PROPERTY_ARTIST, MtpConstants.PROPERTY_ALBUM_NAME, MtpConstants.PROPERTY_DURATION, MtpConstants.PROPERTY_DESCRIPTION};
        IMAGE_PROPERTIES = new int[]{MtpConstants.PROPERTY_DESCRIPTION};
        DEVICE_PROPERTIES = new int[]{MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER, MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME, MtpConstants.DEVICE_PROPERTY_IMAGE_SIZE, MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL, MtpConstants.DEVICE_PROPERTY_PERCEIVED_DEVICE_TYPE};
    }

    private synchronized int[] getSupportedObjectProperties(int format) {
        switch (format) {
            case 12296:
            case 12297:
            case MtpConstants.FORMAT_WMA /* 47361 */:
            case MtpConstants.FORMAT_OGG /* 47362 */:
            case MtpConstants.FORMAT_AAC /* 47363 */:
                return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(AUDIO_PROPERTIES)).toArray();
            case 12299:
            case MtpConstants.FORMAT_WMV /* 47489 */:
            case MtpConstants.FORMAT_3GP_CONTAINER /* 47492 */:
                return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(VIDEO_PROPERTIES)).toArray();
            case MtpConstants.FORMAT_EXIF_JPEG /* 14337 */:
            case MtpConstants.FORMAT_BMP /* 14340 */:
            case MtpConstants.FORMAT_GIF /* 14343 */:
            case MtpConstants.FORMAT_PNG /* 14347 */:
            case MtpConstants.FORMAT_DNG /* 14353 */:
            case MtpConstants.FORMAT_HEIF /* 14354 */:
                return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(IMAGE_PROPERTIES)).toArray();
            default:
                return FILE_PROPERTIES;
        }
    }

    private synchronized int[] getSupportedDeviceProperties() {
        return DEVICE_PROPERTIES;
    }

    private synchronized int[] getSupportedPlaybackFormats() {
        return PLAYBACK_FORMATS;
    }

    private synchronized int[] getSupportedCaptureFormats() {
        return null;
    }

    public synchronized MtpDatabase(Context context, String volumeName, String[] subDirectories) {
        native_setup();
        this.mContext = context;
        this.mMediaProvider = context.getContentResolver().acquireContentProviderClient(MediaStore.AUTHORITY);
        this.mVolumeName = volumeName;
        this.mObjectsUri = MediaStore.Files.getMtpObjectsUri(volumeName);
        this.mMediaScanner = new MediaScanner(context, this.mVolumeName);
        this.mManager = new MtpStorageManager(new MtpStorageManager.MtpNotifier() { // from class: android.mtp.MtpDatabase.2
            @Override // android.mtp.MtpStorageManager.MtpNotifier
            public void sendObjectAdded(int id) {
                if (MtpDatabase.this.mServer != null) {
                    MtpDatabase.this.mServer.sendObjectAdded(id);
                }
            }

            @Override // android.mtp.MtpStorageManager.MtpNotifier
            public void sendObjectRemoved(int id) {
                if (MtpDatabase.this.mServer != null) {
                    MtpDatabase.this.mServer.sendObjectRemoved(id);
                }
            }
        }, subDirectories == null ? null : Sets.newHashSet(subDirectories));
        initDeviceProperties(context);
        this.mDeviceType = SystemProperties.getInt("sys.usb.mtp.device_type", 0);
        this.mCloseGuard.open("close");
    }

    public synchronized void setServer(MtpServer server) {
        this.mServer = server;
        try {
            this.mContext.unregisterReceiver(this.mBatteryReceiver);
        } catch (IllegalArgumentException e) {
        }
        if (server != null) {
            this.mContext.registerReceiver(this.mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mManager.close();
        this.mCloseGuard.close();
        if (this.mClosed.compareAndSet(false, true)) {
            this.mMediaScanner.close();
            if (this.mMediaProvider != null) {
                this.mMediaProvider.close();
            }
            native_finalize();
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    public synchronized void addStorage(StorageVolume storage) {
        MtpStorage mtpStorage = this.mManager.addMtpStorage(storage);
        this.mStorageMap.put(storage.getPath(), mtpStorage);
        if (this.mServer != null) {
            this.mServer.addStorage(mtpStorage);
        }
    }

    public synchronized void removeStorage(StorageVolume storage) {
        MtpStorage mtpStorage = this.mStorageMap.get(storage.getPath());
        if (mtpStorage == null) {
            return;
        }
        if (this.mServer != null) {
            this.mServer.removeStorage(mtpStorage);
        }
        this.mManager.removeMtpStorage(mtpStorage);
        this.mStorageMap.remove(storage.getPath());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v5, types: [java.lang.String] */
    private synchronized void initDeviceProperties(Context context) {
        SQLiteDatabase db;
        boolean moveToNext;
        this.mDeviceProperties = context.getSharedPreferences("device-properties", 0);
        File databaseFile = context.getDatabasePath("device-properties");
        if (databaseFile.exists()) {
            SQLiteDatabase db2 = null;
            db2 = null;
            db2 = null;
            Cursor c = null;
            try {
                try {
                    db = context.openOrCreateDatabase("device-properties", 0, null);
                    if (db != null) {
                        try {
                            c = db.query("properties", new String[]{"_id", "code", "value"}, null, null, null, null, null);
                            db2 = "code";
                            if (c != null) {
                                SharedPreferences.Editor e = this.mDeviceProperties.edit();
                                while (true) {
                                    moveToNext = c.moveToNext();
                                    if (!moveToNext) {
                                        break;
                                    }
                                    String name = c.getString(1);
                                    String value = c.getString(2);
                                    e.putString(name, value);
                                }
                                e.commit();
                                db2 = moveToNext;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            db2 = db;
                            Log.e(TAG, "failed to migrate device properties", e);
                            if (c != null) {
                                c.close();
                            }
                            if (db2 != null) {
                                db2.close();
                            }
                            context.deleteDatabase("device-properties");
                        } catch (Throwable th) {
                            th = th;
                            if (c != null) {
                                c.close();
                            }
                            if (db != null) {
                                db.close();
                            }
                            throw th;
                        }
                    }
                    if (c != null) {
                        c.close();
                    }
                    if (db != null) {
                        db.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    db = db2;
                }
            } catch (Exception e3) {
                e = e3;
            }
            context.deleteDatabase("device-properties");
        }
    }

    private synchronized int beginSendObject(String path, int format, int parent, int storageId) {
        MtpStorageManager.MtpObject parentObj = parent == 0 ? this.mManager.getStorageRoot(storageId) : this.mManager.getObject(parent);
        if (parentObj == null) {
            return -1;
        }
        Path objPath = Paths.get(path, new String[0]);
        return this.mManager.beginSendObject(parentObj, objPath.getFileName().toString(), format);
    }

    private synchronized void endSendObject(int handle, boolean succeeded) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null || !this.mManager.endSendObject(obj, succeeded)) {
            Log.e(TAG, "Failed to successfully end send object");
        } else if (succeeded) {
            String path = obj.getPath().toString();
            int format = obj.getFormat();
            ContentValues values = new ContentValues();
            values.put("_data", path);
            values.put(Telephony.CellBroadcasts.MESSAGE_FORMAT, Integer.valueOf(format));
            values.put("_size", Long.valueOf(obj.getSize()));
            values.put("date_modified", Long.valueOf(obj.getModifiedTime()));
            try {
                if (obj.getParent().isRoot()) {
                    values.put("parent", (Integer) 0);
                } else {
                    int parentId = findInMedia(obj.getParent().getPath());
                    if (parentId != -1) {
                        values.put("parent", Integer.valueOf(parentId));
                    } else {
                        return;
                    }
                }
                Uri uri = this.mMediaProvider.insert(this.mObjectsUri, values);
                if (uri != null) {
                    rescanFile(path, Integer.parseInt(uri.getPathSegments().get(2)), format);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in beginSendObject", e);
            }
        }
    }

    private synchronized void rescanFile(String path, int handle, int format) {
        if (format == 47621) {
            String name = path;
            int lastSlash = name.lastIndexOf(47);
            if (lastSlash >= 0) {
                name = name.substring(lastSlash + 1);
            }
            if (name.endsWith(".pla")) {
                name = name.substring(0, name.length() - 4);
            }
            ContentValues values = new ContentValues(1);
            values.put("_data", path);
            values.put("name", name);
            values.put(Telephony.CellBroadcasts.MESSAGE_FORMAT, Integer.valueOf(format));
            values.put("date_modified", Long.valueOf(System.currentTimeMillis() / 1000));
            values.put(MediaStore.MediaColumns.MEDIA_SCANNER_NEW_OBJECT_ID, Integer.valueOf(handle));
            try {
                this.mMediaProvider.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values);
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in endSendObject", e);
                return;
            }
        }
        this.mMediaScanner.scanMtpFile(path, handle, format);
    }

    private synchronized int[] getObjectList(int storageID, int format, int parent) {
        Stream<MtpStorageManager.MtpObject> objectStream = this.mManager.getObjects(parent, format, storageID);
        if (objectStream == null) {
            return null;
        }
        return objectStream.mapToInt(new ToIntFunction() { // from class: android.mtp.-$$Lambda$iwOv5HKUnGm7PVU3weoI9-JmsXc
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((MtpStorageManager.MtpObject) obj).getId();
            }
        }).toArray();
    }

    private synchronized int getNumObjects(int storageID, int format, int parent) {
        Stream<MtpStorageManager.MtpObject> objectStream = this.mManager.getObjects(parent, format, storageID);
        if (objectStream == null) {
            return -1;
        }
        return (int) objectStream.count();
    }

    private synchronized MtpPropertyList getObjectPropertyList(int handle, int format, int property, int groupCode, int depth) {
        MtpPropertyGroup propertyGroup;
        int handle2 = handle;
        if (property == 0) {
            if (groupCode == 0) {
                return new MtpPropertyList(MtpConstants.RESPONSE_PARAMETER_NOT_SUPPORTED);
            }
            return new MtpPropertyList(MtpConstants.RESPONSE_SPECIFICATION_BY_GROUP_UNSUPPORTED);
        }
        int i = -1;
        int depth2 = depth;
        if (depth2 == -1 && (handle2 == 0 || handle2 == -1)) {
            handle2 = -1;
            depth2 = 0;
        }
        int i2 = 1;
        if (depth2 != 0 && depth2 != 1) {
            return new MtpPropertyList(MtpConstants.RESPONSE_SPECIFICATION_BY_DEPTH_UNSUPPORTED);
        }
        Stream<MtpStorageManager.MtpObject> objectStream = Stream.of((Object[]) new MtpStorageManager.MtpObject[0]);
        if (handle2 == -1) {
            objectStream = this.mManager.getObjects(0, format, -1);
            if (objectStream == null) {
                return new MtpPropertyList(MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE);
            }
        } else if (handle2 != 0) {
            MtpStorageManager.MtpObject obj = this.mManager.getObject(handle2);
            if (obj == null) {
                return new MtpPropertyList(MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE);
            }
            if (obj.getFormat() == format || format == 0) {
                objectStream = Stream.of(obj);
            }
        }
        if (handle2 == 0 || depth2 == 1) {
            if (handle2 == 0) {
                handle2 = -1;
            }
            Stream<MtpStorageManager.MtpObject> childStream = this.mManager.getObjects(handle2, format, -1);
            if (childStream == null) {
                return new MtpPropertyList(MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE);
            }
            objectStream = Stream.concat(objectStream, childStream);
        }
        MtpPropertyList ret = new MtpPropertyList(MtpConstants.RESPONSE_OK);
        for (MtpStorageManager.MtpObject obj2 : objectStream) {
            if (property == i) {
                propertyGroup = this.mPropertyGroupsByFormat.get(Integer.valueOf(obj2.getFormat()));
                if (propertyGroup == null) {
                    propertyGroup = new MtpPropertyGroup(this.mMediaProvider, this.mVolumeName, getSupportedObjectProperties(format));
                    this.mPropertyGroupsByFormat.put(Integer.valueOf(format), propertyGroup);
                }
            } else {
                int[] propertyList = new int[i2];
                propertyList[0] = property;
                propertyGroup = this.mPropertyGroupsByProperty.get(Integer.valueOf(property));
                if (propertyGroup == null) {
                    propertyGroup = new MtpPropertyGroup(this.mMediaProvider, this.mVolumeName, propertyList);
                    this.mPropertyGroupsByProperty.put(Integer.valueOf(property), propertyGroup);
                }
            }
            int err = propertyGroup.getPropertyList(obj2, ret);
            if (err != 8193) {
                return new MtpPropertyList(err);
            }
            i = -1;
            i2 = 1;
        }
        return ret;
    }

    private synchronized int renameFile(int handle, String newName) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
        }
        Path oldPath = obj.getPath();
        if (this.mManager.beginRenameObject(obj, newName)) {
            Path newPath = obj.getPath();
            boolean success = oldPath.toFile().renameTo(newPath.toFile());
            try {
                Os.access(oldPath.toString(), OsConstants.F_OK);
                Os.access(newPath.toString(), OsConstants.F_OK);
            } catch (ErrnoException e) {
            }
            if (!this.mManager.endRenameObject(obj, oldPath.getFileName().toString(), success)) {
                Log.e(TAG, "Failed to end rename object");
            }
            if (success) {
                ContentValues values = new ContentValues();
                values.put("_data", newPath.toString());
                String[] whereArgs = {oldPath.toString()};
                try {
                    this.mMediaProvider.update(this.mObjectsUri, values, PATH_WHERE, whereArgs);
                } catch (RemoteException e2) {
                    Log.e(TAG, "RemoteException in mMediaProvider.update", e2);
                }
                if (obj.isDir()) {
                    if (oldPath.getFileName().startsWith(".") && !newPath.startsWith(".")) {
                        try {
                            this.mMediaProvider.call(MediaStore.UNHIDE_CALL, newPath.toString(), null);
                            return MtpConstants.RESPONSE_OK;
                        } catch (RemoteException e3) {
                            String str = TAG;
                            Log.e(str, "failed to unhide/rescan for " + newPath);
                            return MtpConstants.RESPONSE_OK;
                        }
                    }
                    return MtpConstants.RESPONSE_OK;
                } else if (oldPath.getFileName().toString().toLowerCase(Locale.US).equals(".nomedia") && !newPath.getFileName().toString().toLowerCase(Locale.US).equals(".nomedia")) {
                    try {
                        this.mMediaProvider.call(MediaStore.UNHIDE_CALL, oldPath.getParent().toString(), null);
                        return MtpConstants.RESPONSE_OK;
                    } catch (RemoteException e4) {
                        String str2 = TAG;
                        Log.e(str2, "failed to unhide/rescan for " + newPath);
                        return MtpConstants.RESPONSE_OK;
                    }
                } else {
                    return MtpConstants.RESPONSE_OK;
                }
            }
            return 8194;
        }
        return 8194;
    }

    private synchronized int beginMoveObject(int handle, int newParent, int newStorage) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        MtpStorageManager.MtpObject parent = newParent == 0 ? this.mManager.getStorageRoot(newStorage) : this.mManager.getObject(newParent);
        if (obj == null || parent == null) {
            return MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
        }
        boolean allowed = this.mManager.beginMoveObject(obj, parent);
        if (allowed) {
            return MtpConstants.RESPONSE_OK;
        }
        return 8194;
    }

    private synchronized void endMoveObject(int oldParent, int newParent, int oldStorage, int newStorage, int objId, boolean success) {
        MtpStorageManager.MtpObject oldParentObj = oldParent == 0 ? this.mManager.getStorageRoot(oldStorage) : this.mManager.getObject(oldParent);
        MtpStorageManager.MtpObject newParentObj = newParent == 0 ? this.mManager.getStorageRoot(newStorage) : this.mManager.getObject(newParent);
        String name = this.mManager.getObject(objId).getName();
        if (newParentObj == null || oldParentObj == null || !this.mManager.endMoveObject(oldParentObj, newParentObj, name, success)) {
            Log.e(TAG, "Failed to end move object");
            return;
        }
        MtpStorageManager.MtpObject obj = this.mManager.getObject(objId);
        if (!success || obj == null) {
            return;
        }
        ContentValues values = new ContentValues();
        Path path = newParentObj.getPath().resolve(name);
        Path oldPath = oldParentObj.getPath().resolve(name);
        values.put("_data", path.toString());
        if (obj.getParent().isRoot()) {
            values.put("parent", (Integer) 0);
        } else {
            int parentId = findInMedia(path.getParent());
            if (parentId != -1) {
                values.put("parent", Integer.valueOf(parentId));
            } else {
                deleteFromMedia(oldPath, obj.isDir());
                return;
            }
        }
        String[] whereArgs = {oldPath.toString()};
        int parentId2 = -1;
        try {
            if (!oldParentObj.isRoot()) {
                try {
                    parentId2 = findInMedia(oldPath.getParent());
                } catch (RemoteException e) {
                    e = e;
                    Log.e(TAG, "RemoteException in mMediaProvider.update", e);
                }
            }
            try {
                if (!oldParentObj.isRoot() && parentId2 == -1) {
                    values.put(Telephony.CellBroadcasts.MESSAGE_FORMAT, Integer.valueOf(obj.getFormat()));
                    values.put("_size", Long.valueOf(obj.getSize()));
                    values.put("date_modified", Long.valueOf(obj.getModifiedTime()));
                    Uri uri = this.mMediaProvider.insert(this.mObjectsUri, values);
                    if (uri != null) {
                        rescanFile(path.toString(), Integer.parseInt(uri.getPathSegments().get(2)), obj.getFormat());
                    }
                }
                this.mMediaProvider.update(this.mObjectsUri, values, PATH_WHERE, whereArgs);
            } catch (RemoteException e2) {
                e = e2;
                Log.e(TAG, "RemoteException in mMediaProvider.update", e);
            }
        } catch (RemoteException e3) {
            e = e3;
        }
    }

    private synchronized int beginCopyObject(int handle, int newParent, int newStorage) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        MtpStorageManager.MtpObject parent = newParent == 0 ? this.mManager.getStorageRoot(newStorage) : this.mManager.getObject(newParent);
        if (obj == null || parent == null) {
            return MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
        }
        return this.mManager.beginCopyObject(obj, parent);
    }

    private synchronized void endCopyObject(int handle, boolean success) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null || !this.mManager.endCopyObject(obj, success)) {
            Log.e(TAG, "Failed to end copy object");
        } else if (!success) {
        } else {
            String path = obj.getPath().toString();
            int format = obj.getFormat();
            ContentValues values = new ContentValues();
            values.put("_data", path);
            values.put(Telephony.CellBroadcasts.MESSAGE_FORMAT, Integer.valueOf(format));
            values.put("_size", Long.valueOf(obj.getSize()));
            values.put("date_modified", Long.valueOf(obj.getModifiedTime()));
            try {
                if (obj.getParent().isRoot()) {
                    values.put("parent", (Integer) 0);
                } else {
                    int parentId = findInMedia(obj.getParent().getPath());
                    if (parentId != -1) {
                        values.put("parent", Integer.valueOf(parentId));
                    } else {
                        return;
                    }
                }
                if (obj.isDir()) {
                    this.mMediaScanner.scanDirectories(new String[]{path});
                    return;
                }
                Uri uri = this.mMediaProvider.insert(this.mObjectsUri, values);
                if (uri != null) {
                    rescanFile(path, Integer.parseInt(uri.getPathSegments().get(2)), format);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in beginSendObject", e);
            }
        }
    }

    private synchronized int setObjectProperty(int handle, int property, long intValue, String stringValue) {
        if (property == 56327) {
            return renameFile(handle, stringValue);
        }
        return MtpConstants.RESPONSE_OBJECT_PROP_NOT_SUPPORTED;
    }

    private synchronized int getDeviceProperty(int property, long[] outIntValue, char[] outStringValue) {
        if (property == 20481) {
            outIntValue[0] = this.mBatteryLevel;
            outIntValue[1] = this.mBatteryScale;
            return MtpConstants.RESPONSE_OK;
        } else if (property == 20483) {
            Display display = ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int width = display.getMaximumSizeDimension();
            int height = display.getMaximumSizeDimension();
            String imageSize = Integer.toString(width) + "x" + Integer.toString(height);
            imageSize.getChars(0, imageSize.length(), outStringValue, 0);
            outStringValue[imageSize.length()] = 0;
            return MtpConstants.RESPONSE_OK;
        } else if (property != 54279) {
            switch (property) {
                case MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER /* 54273 */:
                case MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME /* 54274 */:
                    String value = this.mDeviceProperties.getString(Integer.toString(property), "");
                    int length = value.length();
                    if (length > 255) {
                        length = 255;
                    }
                    value.getChars(0, length, outStringValue, 0);
                    outStringValue[length] = 0;
                    return MtpConstants.RESPONSE_OK;
                default:
                    return MtpConstants.RESPONSE_DEVICE_PROP_NOT_SUPPORTED;
            }
        } else {
            outIntValue[0] = this.mDeviceType;
            return MtpConstants.RESPONSE_OK;
        }
    }

    private synchronized int setDeviceProperty(int property, long intValue, String stringValue) {
        switch (property) {
            case MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER /* 54273 */:
            case MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME /* 54274 */:
                SharedPreferences.Editor e = this.mDeviceProperties.edit();
                e.putString(Integer.toString(property), stringValue);
                if (e.commit()) {
                    return MtpConstants.RESPONSE_OK;
                }
                return 8194;
            default:
                return MtpConstants.RESPONSE_DEVICE_PROP_NOT_SUPPORTED;
        }
    }

    private synchronized boolean getObjectInfo(int handle, int[] outStorageFormatParent, char[] outName, long[] outCreatedModified) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return false;
        }
        outStorageFormatParent[0] = obj.getStorageId();
        outStorageFormatParent[1] = obj.getFormat();
        outStorageFormatParent[2] = obj.getParent().isRoot() ? 0 : obj.getParent().getId();
        int nameLen = Integer.min(obj.getName().length(), 255);
        obj.getName().getChars(0, nameLen, outName, 0);
        outName[nameLen] = 0;
        outCreatedModified[0] = obj.getModifiedTime();
        outCreatedModified[1] = obj.getModifiedTime();
        return true;
    }

    private synchronized int getObjectFilePath(int handle, char[] outFilePath, long[] outFileLengthFormat) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
        }
        String path = obj.getPath().toString();
        int pathLen = Integer.min(path.length(), 4096);
        path.getChars(0, pathLen, outFilePath, 0);
        outFilePath[pathLen] = 0;
        outFileLengthFormat[0] = obj.getSize();
        outFileLengthFormat[1] = obj.getFormat();
        return MtpConstants.RESPONSE_OK;
    }

    private synchronized int getObjectFormat(int handle) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return -1;
        }
        return obj.getFormat();
    }

    private synchronized int beginDeleteObject(int handle) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
        }
        if (!this.mManager.beginRemoveObject(obj)) {
            return 8194;
        }
        return MtpConstants.RESPONSE_OK;
    }

    private synchronized void endDeleteObject(int handle, boolean success) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return;
        }
        if (!this.mManager.endRemoveObject(obj, success)) {
            Log.e(TAG, "Failed to end remove object");
        }
        if (success) {
            deleteFromMedia(obj.getPath(), obj.isDir());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x004c, code lost:
        if (r1 == null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x004f, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized int findInMedia(java.nio.file.Path r11) {
        /*
            r10 = this;
            r0 = -1
            r1 = 0
            android.content.ContentProviderClient r2 = r10.mMediaProvider     // Catch: java.lang.Throwable -> L2e android.os.RemoteException -> L30
            android.net.Uri r3 = r10.mObjectsUri     // Catch: java.lang.Throwable -> L2e android.os.RemoteException -> L30
            java.lang.String[] r4 = android.mtp.MtpDatabase.ID_PROJECTION     // Catch: java.lang.Throwable -> L2e android.os.RemoteException -> L30
            java.lang.String r5 = "_data=?"
            r6 = 1
            java.lang.String[] r6 = new java.lang.String[r6]     // Catch: java.lang.Throwable -> L2e android.os.RemoteException -> L30
            java.lang.String r7 = r11.toString()     // Catch: java.lang.Throwable -> L2e android.os.RemoteException -> L30
            r9 = 0
            r6[r9] = r7     // Catch: java.lang.Throwable -> L2e android.os.RemoteException -> L30
            r7 = 0
            r8 = 0
            android.database.Cursor r2 = r2.query(r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L2e android.os.RemoteException -> L30
            r1 = r2
            if (r1 == 0) goto L28
            boolean r2 = r1.moveToNext()     // Catch: java.lang.Throwable -> L2e android.os.RemoteException -> L30
            if (r2 == 0) goto L28
            int r2 = r1.getInt(r9)     // Catch: java.lang.Throwable -> L2e android.os.RemoteException -> L30
            r0 = r2
        L28:
            if (r1 == 0) goto L4f
        L2a:
            r1.close()
            goto L4f
        L2e:
            r2 = move-exception
            goto L50
        L30:
            r2 = move-exception
            java.lang.String r3 = android.mtp.MtpDatabase.TAG     // Catch: java.lang.Throwable -> L2e
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L2e
            r4.<init>()     // Catch: java.lang.Throwable -> L2e
            java.lang.String r5 = "Error finding "
            r4.append(r5)     // Catch: java.lang.Throwable -> L2e
            r4.append(r11)     // Catch: java.lang.Throwable -> L2e
            java.lang.String r5 = " in MediaProvider"
            r4.append(r5)     // Catch: java.lang.Throwable -> L2e
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L2e
            android.util.Log.e(r3, r4)     // Catch: java.lang.Throwable -> L2e
            if (r1 == 0) goto L4f
            goto L2a
        L4f:
            return r0
        L50:
            if (r1 == 0) goto L55
            r1.close()
        L55:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpDatabase.findInMedia(java.nio.file.Path):int");
    }

    private synchronized void deleteFromMedia(Path path, boolean isDir) {
        if (isDir) {
            try {
                ContentProviderClient contentProviderClient = this.mMediaProvider;
                Uri uri = this.mObjectsUri;
                contentProviderClient.delete(uri, "_data LIKE ?1 AND lower(substr(_data,1,?2))=lower(?3)", new String[]{path + "/%", Integer.toString(path.toString().length() + 1), path.toString() + "/"});
            } catch (Exception e) {
                String str = TAG;
                Log.d(str, "Failed to delete " + path + " from MediaProvider");
                return;
            }
        }
        String[] whereArgs = {path.toString()};
        if (this.mMediaProvider.delete(this.mObjectsUri, PATH_WHERE, whereArgs) > 0) {
            if (!isDir && path.toString().toLowerCase(Locale.US).endsWith(".nomedia")) {
                try {
                    String parentPath = path.getParent().toString();
                    this.mMediaProvider.call(MediaStore.UNHIDE_CALL, parentPath, null);
                    return;
                } catch (RemoteException e2) {
                    String str2 = TAG;
                    Log.e(str2, "failed to unhide/rescan for " + path);
                    return;
                }
            }
            return;
        }
        String str3 = TAG;
        Log.i(str3, "Mediaprovider didn't delete " + path);
    }

    private synchronized int[] getObjectReferences(int handle) {
        int handle2;
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null || (handle2 = findInMedia(obj.getPath())) == -1) {
            return null;
        }
        Uri uri = MediaStore.Files.getMtpReferencesUri(this.mVolumeName, handle2);
        Cursor c = null;
        try {
            try {
                c = this.mMediaProvider.query(uri, PATH_PROJECTION, null, null, null, null);
                if (c == null) {
                    if (c != null) {
                        c.close();
                    }
                    return null;
                }
                ArrayList<Integer> result = new ArrayList<>();
                while (c.moveToNext()) {
                    String refPath = c.getString(0);
                    MtpStorageManager.MtpObject refObj = this.mManager.getByPath(refPath);
                    if (refObj != null) {
                        result.add(Integer.valueOf(refObj.getId()));
                    }
                }
                int[] array = result.stream().mapToInt(new ToIntFunction() { // from class: android.mtp.-$$Lambda$MtpDatabase$UV1wDVoVlbcxpr8zevj_aMFtUGw
                    @Override // java.util.function.ToIntFunction
                    public final int applyAsInt(Object obj2) {
                        int intValue;
                        intValue = ((Integer) obj2).intValue();
                        return intValue;
                    }
                }).toArray();
                if (c != null) {
                    c.close();
                }
                return array;
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in getObjectList", e);
                if (c != null) {
                    c.close();
                }
                return null;
            }
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
            throw th;
        }
    }

    private synchronized int setObjectReferences(int handle, int[] references) {
        int refHandle;
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE;
        }
        int handle2 = findInMedia(obj.getPath());
        int i = -1;
        if (handle2 == -1) {
            return 8194;
        }
        Uri uri = MediaStore.Files.getMtpReferencesUri(this.mVolumeName, handle2);
        ArrayList<ContentValues> valuesList = new ArrayList<>();
        int length = references.length;
        int i2 = 0;
        while (i2 < length) {
            int id = references[i2];
            MtpStorageManager.MtpObject refObj = this.mManager.getObject(id);
            if (refObj != null && (refHandle = findInMedia(refObj.getPath())) != i) {
                ContentValues values = new ContentValues();
                values.put("_id", Integer.valueOf(refHandle));
                valuesList.add(values);
            }
            i2++;
            i = -1;
        }
        try {
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in setObjectReferences", e);
        }
        if (this.mMediaProvider.bulkInsert(uri, (ContentValues[]) valuesList.toArray(new ContentValues[0])) <= 0) {
            return 8194;
        }
        return MtpConstants.RESPONSE_OK;
    }
}
