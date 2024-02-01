package android.mtp;

import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.mtp.MtpStorageManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForNative;
import com.google.android.collect.Sets;
import dalvik.system.CloseGuard;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

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
    @VisibleForNative
    private long mNativeContext;
    private MtpServer mServer;
    private static final String TAG = MtpDatabase.class.getSimpleName();
    private static final String[] ID_PROJECTION = {"_id"};
    private static final String[] PATH_PROJECTION = {"_data"};
    private final AtomicBoolean mClosed = new AtomicBoolean();
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final HashMap<String, MtpStorage> mStorageMap = new HashMap<>();
    private final SparseArray<MtpPropertyGroup> mPropertyGroupsByProperty = new SparseArray<>();
    private final SparseArray<MtpPropertyGroup> mPropertyGroupsByFormat = new SparseArray<>();
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
        AUDIO_PROPERTIES = new int[]{MtpConstants.PROPERTY_ARTIST, MtpConstants.PROPERTY_ALBUM_NAME, MtpConstants.PROPERTY_ALBUM_ARTIST, MtpConstants.PROPERTY_TRACK, MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE, MtpConstants.PROPERTY_DURATION, MtpConstants.PROPERTY_COMPOSER, MtpConstants.PROPERTY_AUDIO_WAVE_CODEC, MtpConstants.PROPERTY_BITRATE_TYPE, MtpConstants.PROPERTY_AUDIO_BITRATE, MtpConstants.PROPERTY_NUMBER_OF_CHANNELS, MtpConstants.PROPERTY_SAMPLE_RATE};
        VIDEO_PROPERTIES = new int[]{MtpConstants.PROPERTY_ARTIST, MtpConstants.PROPERTY_ALBUM_NAME, MtpConstants.PROPERTY_DURATION, MtpConstants.PROPERTY_DESCRIPTION};
        IMAGE_PROPERTIES = new int[]{MtpConstants.PROPERTY_DESCRIPTION};
        DEVICE_PROPERTIES = new int[]{MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER, MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME, MtpConstants.DEVICE_PROPERTY_IMAGE_SIZE, MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL, MtpConstants.DEVICE_PROPERTY_PERCEIVED_DEVICE_TYPE};
    }

    @VisibleForNative
    private int[] getSupportedObjectProperties(int format) {
        if (format != 12296 && format != 12297) {
            if (format != 12299) {
                if (format != 14337 && format != 14340 && format != 14343 && format != 14347) {
                    if (format != 47489 && format != 47492) {
                        if (format != 14353 && format != 14354) {
                            switch (format) {
                                case MtpConstants.FORMAT_WMA /* 47361 */:
                                case MtpConstants.FORMAT_OGG /* 47362 */:
                                case MtpConstants.FORMAT_AAC /* 47363 */:
                                    break;
                                default:
                                    return FILE_PROPERTIES;
                            }
                        }
                    }
                }
                return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(IMAGE_PROPERTIES)).toArray();
            }
            return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(VIDEO_PROPERTIES)).toArray();
        }
        return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(AUDIO_PROPERTIES)).toArray();
    }

    public static Uri getObjectPropertiesUri(int format, String volumeName) {
        if (format != 12296 && format != 12297) {
            if (format != 12299) {
                if (format != 14337 && format != 14340 && format != 14343 && format != 14347) {
                    if (format != 47489 && format != 47492) {
                        if (format != 14353 && format != 14354) {
                            switch (format) {
                                case MtpConstants.FORMAT_WMA /* 47361 */:
                                case MtpConstants.FORMAT_OGG /* 47362 */:
                                case MtpConstants.FORMAT_AAC /* 47363 */:
                                    break;
                                default:
                                    return MediaStore.Files.getContentUri(volumeName);
                            }
                        }
                    }
                }
                return MediaStore.Images.Media.getContentUri(volumeName);
            }
            return MediaStore.Video.Media.getContentUri(volumeName);
        }
        return MediaStore.Audio.Media.getContentUri(volumeName);
    }

    @VisibleForNative
    private int[] getSupportedDeviceProperties() {
        return DEVICE_PROPERTIES;
    }

    @VisibleForNative
    private int[] getSupportedPlaybackFormats() {
        return PLAYBACK_FORMATS;
    }

    @VisibleForNative
    private int[] getSupportedCaptureFormats() {
        return null;
    }

    public MtpDatabase(Context context, String[] subDirectories) {
        native_setup();
        this.mContext = (Context) Objects.requireNonNull(context);
        this.mMediaProvider = context.getContentResolver().acquireContentProviderClient(MediaStore.AUTHORITY);
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

            @Override // android.mtp.MtpStorageManager.MtpNotifier
            public void sendObjectInfoChanged(int id) {
                if (MtpDatabase.this.mServer != null) {
                    MtpDatabase.this.mServer.sendObjectInfoChanged(id);
                }
            }
        }, subDirectories == null ? null : Sets.newHashSet(subDirectories));
        initDeviceProperties(context);
        this.mDeviceType = SystemProperties.getInt("sys.usb.mtp.device_type", 0);
        this.mCloseGuard.open("close");
    }

    public void setServer(MtpServer server) {
        this.mServer = server;
        try {
            this.mContext.unregisterReceiver(this.mBatteryReceiver);
        } catch (IllegalArgumentException e) {
        }
        if (server != null) {
            this.mContext.registerReceiver(this.mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mManager.close();
        this.mCloseGuard.close();
        if (this.mClosed.compareAndSet(false, true)) {
            ContentProviderClient contentProviderClient = this.mMediaProvider;
            if (contentProviderClient != null) {
                contentProviderClient.close();
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

    public void addStorage(StorageVolume storage) {
        MtpStorage mtpStorage = this.mManager.addMtpStorage(storage);
        this.mStorageMap.put(storage.getPath(), mtpStorage);
        MtpServer mtpServer = this.mServer;
        if (mtpServer != null) {
            mtpServer.addStorage(mtpStorage);
        }
    }

    public void removeStorage(StorageVolume storage) {
        MtpStorage mtpStorage = this.mStorageMap.get(storage.getPath());
        if (mtpStorage == null) {
            return;
        }
        MtpServer mtpServer = this.mServer;
        if (mtpServer != null) {
            mtpServer.removeStorage(mtpStorage);
        }
        this.mManager.removeMtpStorage(mtpStorage);
        this.mStorageMap.remove(storage.getPath());
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0077, code lost:
        if (r6 != null) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x007a, code lost:
        r17.deleteDatabase("device-properties");
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void initDeviceProperties(android.content.Context r17) {
        /*
            r16 = this;
            r1 = r16
            r2 = r17
            java.lang.String r3 = "device-properties"
            r0 = 0
            java.lang.String r4 = "device-properties"
            android.content.SharedPreferences r5 = r2.getSharedPreferences(r4, r0)
            r1.mDeviceProperties = r5
            java.io.File r5 = r2.getDatabasePath(r4)
            boolean r6 = r5.exists()
            if (r6 == 0) goto L89
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.sqlite.SQLiteDatabase r0 = r2.openOrCreateDatabase(r4, r0, r8)     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L69
            r6 = r0
            if (r6 == 0) goto L5c
            java.lang.String r9 = "properties"
            java.lang.String r0 = "_id"
            java.lang.String r8 = "code"
            java.lang.String r10 = "value"
            java.lang.String[] r10 = new java.lang.String[]{r0, r8, r10}     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L69
            r11 = 0
            r12 = 0
            r13 = 0
            r14 = 0
            r15 = 0
            r8 = r6
            android.database.Cursor r0 = r8.query(r9, r10, r11, r12, r13, r14, r15)     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L69
            r7 = r0
            if (r7 == 0) goto L5c
            android.content.SharedPreferences r0 = r1.mDeviceProperties     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L69
            android.content.SharedPreferences$Editor r0 = r0.edit()     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L69
        L44:
            boolean r8 = r7.moveToNext()     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L69
            if (r8 == 0) goto L59
            r8 = 1
            java.lang.String r8 = r7.getString(r8)     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L69
            r9 = 2
            java.lang.String r9 = r7.getString(r9)     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L69
            r0.putString(r8, r9)     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L69
            goto L44
        L59:
            r0.commit()     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L69
        L5c:
            if (r7 == 0) goto L61
            r7.close()
        L61:
            if (r6 == 0) goto L7a
        L63:
            r6.close()
            goto L7a
        L67:
            r0 = move-exception
            goto L7e
        L69:
            r0 = move-exception
            java.lang.String r8 = android.mtp.MtpDatabase.TAG     // Catch: java.lang.Throwable -> L67
            java.lang.String r9 = "failed to migrate device properties"
            android.util.Log.e(r8, r9, r0)     // Catch: java.lang.Throwable -> L67
            if (r7 == 0) goto L77
            r7.close()
        L77:
            if (r6 == 0) goto L7a
            goto L63
        L7a:
            r2.deleteDatabase(r4)
            goto L89
        L7e:
            if (r7 == 0) goto L83
            r7.close()
        L83:
            if (r6 == 0) goto L88
            r6.close()
        L88:
            throw r0
        L89:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpDatabase.initDeviceProperties(android.content.Context):void");
    }

    @VisibleForNative
    private int beginSendObject(String path, int format, int parent, int storageId) {
        MtpStorageManager mtpStorageManager = this.mManager;
        MtpStorageManager.MtpObject parentObj = parent == 0 ? mtpStorageManager.getStorageRoot(storageId) : mtpStorageManager.getObject(parent);
        if (parentObj == null) {
            return -1;
        }
        Path objPath = Paths.get(path, new String[0]);
        return this.mManager.beginSendObject(parentObj, objPath.getFileName().toString(), format);
    }

    @VisibleForNative
    private void endSendObject(int handle, boolean succeeded) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null || !this.mManager.endSendObject(obj, succeeded)) {
            Log.e(TAG, "Failed to successfully end send object");
        } else if (succeeded) {
            MediaStore.scanFile(this.mContext, obj.getPath().toFile());
        }
    }

    @VisibleForNative
    private void rescanFile(String path, int handle, int format) {
        MediaStore.scanFile(this.mContext, new File(path));
    }

    @VisibleForNative
    private int[] getObjectList(int storageID, int format, int parent) {
        List<MtpStorageManager.MtpObject> objs = this.mManager.getObjects(parent, format, storageID);
        if (objs == null) {
            return null;
        }
        int[] ret = new int[objs.size()];
        for (int i = 0; i < objs.size(); i++) {
            ret[i] = objs.get(i).getId();
        }
        return ret;
    }

    @VisibleForNative
    private int getNumObjects(int storageID, int format, int parent) {
        List<MtpStorageManager.MtpObject> objs = this.mManager.getObjects(parent, format, storageID);
        if (objs == null) {
            return -1;
        }
        return objs.size();
    }

    @VisibleForNative
    private MtpPropertyList getObjectPropertyList(int handle, int format, int property, int groupCode, int depth) {
        MtpPropertyGroup propertyGroup;
        int handle2 = handle;
        int format2 = format;
        if (property == 0) {
            if (groupCode == 0) {
                return new MtpPropertyList(8198);
            }
            return new MtpPropertyList(MtpConstants.RESPONSE_SPECIFICATION_BY_GROUP_UNSUPPORTED);
        }
        int err = -1;
        int depth2 = depth;
        if (depth2 == -1 && (handle2 == 0 || handle2 == -1)) {
            handle2 = -1;
            depth2 = 0;
        }
        if (depth2 != 0 && depth2 != 1) {
            return new MtpPropertyList(MtpConstants.RESPONSE_SPECIFICATION_BY_DEPTH_UNSUPPORTED);
        }
        List<MtpStorageManager.MtpObject> objs = null;
        MtpStorageManager.MtpObject thisObj = null;
        if (handle2 == -1) {
            objs = this.mManager.getObjects(0, format2, -1);
            if (objs == null) {
                return new MtpPropertyList(8201);
            }
        } else if (handle2 != 0) {
            MtpStorageManager.MtpObject obj = this.mManager.getObject(handle2);
            if (obj == null) {
                return new MtpPropertyList(8201);
            }
            if (obj.getFormat() == format2 || format2 == 0) {
                thisObj = obj;
            }
        }
        if (handle2 == 0 || depth2 == 1) {
            if (handle2 == 0) {
                handle2 = -1;
            }
            objs = this.mManager.getObjects(handle2, format2, -1);
            if (objs == null) {
                return new MtpPropertyList(8201);
            }
        }
        if (objs == null) {
            objs = new ArrayList<>();
        }
        if (thisObj != null) {
            objs.add(thisObj);
        }
        MtpPropertyList ret = new MtpPropertyList(8193);
        for (MtpStorageManager.MtpObject obj2 : objs) {
            if (property == err) {
                if (format2 == 0 && handle2 != 0 && handle2 != err) {
                    format2 = obj2.getFormat();
                }
                propertyGroup = this.mPropertyGroupsByFormat.get(format2);
                if (propertyGroup == null) {
                    propertyGroup = new MtpPropertyGroup(getSupportedObjectProperties(format2));
                    this.mPropertyGroupsByFormat.put(format2, propertyGroup);
                }
            } else {
                propertyGroup = this.mPropertyGroupsByProperty.get(property);
                if (propertyGroup == null) {
                    int[] propertyList = {property};
                    propertyGroup = new MtpPropertyGroup(propertyList);
                    this.mPropertyGroupsByProperty.put(property, propertyGroup);
                }
            }
            int err2 = propertyGroup.getPropertyList(this.mMediaProvider, obj2.getVolumeName(), obj2, ret);
            if (err2 != 8193) {
                return new MtpPropertyList(err2);
            }
            err = -1;
        }
        return ret;
    }

    private int renameFile(int handle, String newName) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return 8201;
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
                    Uri objectsUri = MediaStore.Files.getMtpObjectsUri(obj.getVolumeName());
                    this.mMediaProvider.update(objectsUri, values, PATH_WHERE, whereArgs);
                } catch (RemoteException e2) {
                    Log.e(TAG, "RemoteException in mMediaProvider.update", e2);
                }
                if (obj.isDir()) {
                    if (oldPath.getFileName().startsWith(".") && !newPath.startsWith(".")) {
                        MediaStore.scanFile(this.mContext, newPath.toFile());
                        return 8193;
                    }
                    return 8193;
                } else if (oldPath.getFileName().toString().toLowerCase(Locale.US).equals(".nomedia") && !newPath.getFileName().toString().toLowerCase(Locale.US).equals(".nomedia")) {
                    MediaStore.scanFile(this.mContext, newPath.getParent().toFile());
                    return 8193;
                } else {
                    return 8193;
                }
            }
            return 8194;
        }
        return 8194;
    }

    @VisibleForNative
    private int beginMoveObject(int handle, int newParent, int newStorage) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        MtpStorageManager.MtpObject parent = newParent == 0 ? this.mManager.getStorageRoot(newStorage) : this.mManager.getObject(newParent);
        if (obj == null || parent == null) {
            return 8201;
        }
        boolean allowed = this.mManager.beginMoveObject(obj, parent);
        return allowed ? 8193 : 8194;
    }

    @VisibleForNative
    private void endMoveObject(int oldParent, int newParent, int oldStorage, int newStorage, int objId, boolean success) {
        MtpStorageManager.MtpObject object;
        MtpStorageManager.MtpObject object2;
        if (oldParent != 0) {
            object = this.mManager.getObject(oldParent);
        } else {
            object = this.mManager.getStorageRoot(oldStorage);
        }
        MtpStorageManager.MtpObject oldParentObj = object;
        if (newParent != 0) {
            object2 = this.mManager.getObject(newParent);
        } else {
            object2 = this.mManager.getStorageRoot(newStorage);
        }
        MtpStorageManager.MtpObject newParentObj = object2;
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
        if (!obj.getParent().isRoot()) {
            int parentId = findInMedia(newParentObj, path.getParent());
            if (parentId == -1) {
                deleteFromMedia(obj, oldPath, obj.isDir());
                return;
            }
            values.put("parent", Integer.valueOf(parentId));
        } else {
            values.put("parent", (Integer) 0);
        }
        String[] whereArgs = {oldPath.toString()};
        int parentId2 = -1;
        try {
            if (!oldParentObj.isRoot()) {
                try {
                    parentId2 = findInMedia(oldParentObj, oldPath.getParent());
                } catch (RemoteException e) {
                    e = e;
                    Log.e(TAG, "RemoteException in mMediaProvider.update", e);
                }
            }
        } catch (RemoteException e2) {
            e = e2;
        }
        try {
            if (!oldParentObj.isRoot() && parentId2 == -1) {
                MediaStore.scanFile(this.mContext, path.toFile());
            }
            Uri objectsUri = MediaStore.Files.getMtpObjectsUri(obj.getVolumeName());
            this.mMediaProvider.update(objectsUri, values, PATH_WHERE, whereArgs);
        } catch (RemoteException e3) {
            e = e3;
            Log.e(TAG, "RemoteException in mMediaProvider.update", e);
        }
    }

    @VisibleForNative
    private int beginCopyObject(int handle, int newParent, int newStorage) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        MtpStorageManager.MtpObject parent = newParent == 0 ? this.mManager.getStorageRoot(newStorage) : this.mManager.getObject(newParent);
        if (obj == null || parent == null) {
            return 8201;
        }
        return this.mManager.beginCopyObject(obj, parent);
    }

    @VisibleForNative
    private void endCopyObject(int handle, boolean success) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null || !this.mManager.endCopyObject(obj, success)) {
            Log.e(TAG, "Failed to end copy object");
        } else if (!success) {
        } else {
            MediaStore.scanFile(this.mContext, obj.getPath().toFile());
        }
    }

    @VisibleForNative
    private int setObjectProperty(int handle, int property, long intValue, String stringValue) {
        if (property == 56327) {
            return renameFile(handle, stringValue);
        }
        return MtpConstants.RESPONSE_OBJECT_PROP_NOT_SUPPORTED;
    }

    @VisibleForNative
    private int getDeviceProperty(int property, long[] outIntValue, char[] outStringValue) {
        switch (property) {
            case MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL /* 20481 */:
                outIntValue[0] = this.mBatteryLevel;
                outIntValue[1] = this.mBatteryScale;
                return 8193;
            case MtpConstants.DEVICE_PROPERTY_IMAGE_SIZE /* 20483 */:
                Display display = ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                int width = display.getMaximumSizeDimension();
                int height = display.getMaximumSizeDimension();
                String imageSize = Integer.toString(width) + "x" + Integer.toString(height);
                imageSize.getChars(0, imageSize.length(), outStringValue, 0);
                outStringValue[imageSize.length()] = 0;
                return 8193;
            case MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER /* 54273 */:
            case MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME /* 54274 */:
                String value = this.mDeviceProperties.getString(Integer.toString(property), "");
                int length = value.length();
                if (length > 255) {
                    length = 255;
                }
                value.getChars(0, length, outStringValue, 0);
                outStringValue[length] = 0;
                return 8193;
            case MtpConstants.DEVICE_PROPERTY_PERCEIVED_DEVICE_TYPE /* 54279 */:
                outIntValue[0] = this.mDeviceType;
                return 8193;
            default:
                return 8202;
        }
    }

    @VisibleForNative
    private int setDeviceProperty(int property, long intValue, String stringValue) {
        switch (property) {
            case MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER /* 54273 */:
            case MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME /* 54274 */:
                SharedPreferences.Editor e = this.mDeviceProperties.edit();
                e.putString(Integer.toString(property), stringValue);
                return e.commit() ? 8193 : 8194;
            default:
                return 8202;
        }
    }

    @VisibleForNative
    private boolean getObjectInfo(int handle, int[] outStorageFormatParent, char[] outName, long[] outCreatedModified) {
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

    @VisibleForNative
    private int getObjectFilePath(int handle, char[] outFilePath, long[] outFileLengthFormat) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return 8201;
        }
        String path = obj.getPath().toString();
        int pathLen = Integer.min(path.length(), 4096);
        path.getChars(0, pathLen, outFilePath, 0);
        outFilePath[pathLen] = 0;
        outFileLengthFormat[0] = obj.getSize();
        outFileLengthFormat[1] = obj.getFormat();
        return 8193;
    }

    private int getObjectFormat(int handle) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return -1;
        }
        return obj.getFormat();
    }

    @VisibleForNative
    private int beginDeleteObject(int handle) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return 8201;
        }
        if (!this.mManager.beginRemoveObject(obj)) {
            return 8194;
        }
        return 8193;
    }

    @VisibleForNative
    private void endDeleteObject(int handle, boolean success) {
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return;
        }
        if (!this.mManager.endRemoveObject(obj, success)) {
            Log.e(TAG, "Failed to end remove object");
        }
        if (success) {
            deleteFromMedia(obj, obj.getPath(), obj.isDir());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0054, code lost:
        if (r9 == null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0057, code lost:
        return r8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int findInMedia(android.mtp.MtpStorageManager.MtpObject r12, java.nio.file.Path r13) {
        /*
            r11 = this;
            java.lang.String r0 = r12.getVolumeName()
            android.net.Uri r0 = android.provider.MediaStore.Files.getMtpObjectsUri(r0)
            r8 = -1
            r9 = 0
            android.content.ContentProviderClient r1 = r11.mMediaProvider     // Catch: java.lang.Throwable -> L35 android.os.RemoteException -> L37
            java.lang.String[] r3 = android.mtp.MtpDatabase.ID_PROJECTION     // Catch: java.lang.Throwable -> L35 android.os.RemoteException -> L37
            java.lang.String r4 = "_data=?"
            r2 = 1
            java.lang.String[] r5 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> L35 android.os.RemoteException -> L37
            java.lang.String r2 = r13.toString()     // Catch: java.lang.Throwable -> L35 android.os.RemoteException -> L37
            r10 = 0
            r5[r10] = r2     // Catch: java.lang.Throwable -> L35 android.os.RemoteException -> L37
            r6 = 0
            r7 = 0
            r2 = r0
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L35 android.os.RemoteException -> L37
            r9 = r1
            if (r9 == 0) goto L2f
            boolean r1 = r9.moveToNext()     // Catch: java.lang.Throwable -> L35 android.os.RemoteException -> L37
            if (r1 == 0) goto L2f
            int r1 = r9.getInt(r10)     // Catch: java.lang.Throwable -> L35 android.os.RemoteException -> L37
            r8 = r1
        L2f:
            if (r9 == 0) goto L57
        L31:
            r9.close()
            goto L57
        L35:
            r1 = move-exception
            goto L58
        L37:
            r1 = move-exception
            java.lang.String r2 = android.mtp.MtpDatabase.TAG     // Catch: java.lang.Throwable -> L35
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L35
            r3.<init>()     // Catch: java.lang.Throwable -> L35
            java.lang.String r4 = "Error finding "
            r3.append(r4)     // Catch: java.lang.Throwable -> L35
            r3.append(r13)     // Catch: java.lang.Throwable -> L35
            java.lang.String r4 = " in MediaProvider"
            r3.append(r4)     // Catch: java.lang.Throwable -> L35
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L35
            android.util.Log.e(r2, r3)     // Catch: java.lang.Throwable -> L35
            if (r9 == 0) goto L57
            goto L31
        L57:
            return r8
        L58:
            if (r9 == 0) goto L5d
            r9.close()
        L5d:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpDatabase.findInMedia(android.mtp.MtpStorageManager$MtpObject, java.nio.file.Path):int");
    }

    private void deleteFromMedia(MtpStorageManager.MtpObject obj, Path path, boolean isDir) {
        Uri objectsUri = MediaStore.Files.getMtpObjectsUri(obj.getVolumeName());
        if (isDir) {
            try {
                ContentProviderClient contentProviderClient = this.mMediaProvider;
                contentProviderClient.delete(objectsUri, "_data LIKE ?1 AND lower(substr(_data,1,?2))=lower(?3)", new String[]{path + "/%", Integer.toString(path.toString().length() + 1), path.toString() + "/"});
            } catch (Exception e) {
                String str = TAG;
                Log.d(str, "Failed to delete " + path + " from MediaProvider");
                return;
            }
        }
        String[] whereArgs = {path.toString()};
        if (this.mMediaProvider.delete(objectsUri, PATH_WHERE, whereArgs) > 0) {
            if (!isDir && path.toString().toLowerCase(Locale.US).endsWith(".nomedia")) {
                MediaStore.scanFile(this.mContext, path.getParent().toFile());
                return;
            }
            return;
        }
        String str2 = TAG;
        Log.i(str2, "Mediaprovider didn't delete " + path);
    }

    @VisibleForNative
    private int[] getObjectReferences(int handle) {
        int handle2;
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj == null || (handle2 = findInMedia(obj, obj.getPath())) == -1) {
            return null;
        }
        Uri uri = MediaStore.Files.getMtpReferencesUri(obj.getVolumeName(), handle2);
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
                int[] array = result.stream().mapToInt($$Lambda$UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).toArray();
                c.close();
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

    @VisibleForNative
    private int setObjectReferences(int handle, int[] references) {
        int refHandle;
        MtpStorageManager.MtpObject obj = this.mManager.getObject(handle);
        if (obj != null) {
            int handle2 = findInMedia(obj, obj.getPath());
            int i = -1;
            if (handle2 == -1) {
                return 8194;
            }
            Uri uri = MediaStore.Files.getMtpReferencesUri(obj.getVolumeName(), handle2);
            ArrayList<ContentValues> valuesList = new ArrayList<>();
            int length = references.length;
            int i2 = 0;
            while (i2 < length) {
                int id = references[i2];
                MtpStorageManager.MtpObject refObj = this.mManager.getObject(id);
                if (refObj != null && (refHandle = findInMedia(refObj, refObj.getPath())) != i) {
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
            return 8193;
        }
        return 8201;
    }
}
