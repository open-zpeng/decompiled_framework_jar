package android.os;

import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.util.LinkedList;
/* loaded from: classes2.dex */
public class Environment {
    public static String DIRECTORY_ALARMS = null;
    @Deprecated
    public static final String DIRECTORY_ANDROID = "Android";
    public static String DIRECTORY_DCIM = null;
    public static String DIRECTORY_DOCUMENTS = null;
    public static String DIRECTORY_DOWNLOADS = null;
    public static String DIRECTORY_MOVIES = null;
    public static String DIRECTORY_MUSIC = null;
    public static String DIRECTORY_NOTIFICATIONS = null;
    public static String DIRECTORY_PICTURES = null;
    public static String DIRECTORY_PODCASTS = null;
    public static String DIRECTORY_RINGTONES = null;
    public static final String DIR_ANDROID = "Android";
    private static final String DIR_CACHE = "cache";
    private static final String DIR_DATA = "data";
    private static final String DIR_FILES = "files";
    private static final String DIR_MEDIA = "media";
    private static final String DIR_OBB = "obb";
    private static final String ENV_EXTERNAL_STORAGE = "EXTERNAL_STORAGE";
    public static final int HAS_ALARMS = 8;
    public static final int HAS_ANDROID = 65536;
    public static final int HAS_DCIM = 256;
    public static final int HAS_DOCUMENTS = 512;
    public static final int HAS_DOWNLOADS = 128;
    public static final int HAS_MOVIES = 64;
    public static final int HAS_MUSIC = 1;
    public static final int HAS_NOTIFICATIONS = 16;
    public static final int HAS_OTHER = 131072;
    public static final int HAS_PICTURES = 32;
    public static final int HAS_PODCASTS = 2;
    public static final int HAS_RINGTONES = 4;
    public static final String MEDIA_BAD_REMOVAL = "bad_removal";
    public static final String MEDIA_CHECKING = "checking";
    public static final String MEDIA_EJECTING = "ejecting";
    public static final String MEDIA_MOUNTED = "mounted";
    public static final String MEDIA_MOUNTED_READ_ONLY = "mounted_ro";
    public static final String MEDIA_NOFS = "nofs";
    public static final String MEDIA_REMOVED = "removed";
    public static final String MEDIA_SHARED = "shared";
    public static final String MEDIA_UNKNOWN = "unknown";
    public static final String MEDIA_UNMOUNTABLE = "unmountable";
    public static final String MEDIA_UNMOUNTED = "unmounted";
    public static final String[] STANDARD_DIRECTORIES;
    private static final String TAG = "Environment";
    public protected static UserEnvironment sCurrentUser;
    private static boolean sUserRequired;
    private static final String ENV_ANDROID_ROOT = "ANDROID_ROOT";
    private static final File DIR_ANDROID_ROOT = getDirectory(ENV_ANDROID_ROOT, "/system");
    private static final String ENV_ANDROID_DATA = "ANDROID_DATA";
    private static final File DIR_ANDROID_DATA = getDirectory(ENV_ANDROID_DATA, "/data");
    private static final String ENV_ANDROID_EXPAND = "ANDROID_EXPAND";
    private static final File DIR_ANDROID_EXPAND = getDirectory(ENV_ANDROID_EXPAND, "/mnt/expand");
    private static final String ENV_ANDROID_STORAGE = "ANDROID_STORAGE";
    private static final File DIR_ANDROID_STORAGE = getDirectory(ENV_ANDROID_STORAGE, "/storage");
    private static final String ENV_DOWNLOAD_CACHE = "DOWNLOAD_CACHE";
    private static final File DIR_DOWNLOAD_CACHE = getDirectory(ENV_DOWNLOAD_CACHE, "/cache");
    private static final String ENV_OEM_ROOT = "OEM_ROOT";
    private static final File DIR_OEM_ROOT = getDirectory(ENV_OEM_ROOT, "/oem");
    private static final String ENV_ODM_ROOT = "ODM_ROOT";
    private static final File DIR_ODM_ROOT = getDirectory(ENV_ODM_ROOT, "/odm");
    private static final String ENV_VENDOR_ROOT = "VENDOR_ROOT";
    private static final File DIR_VENDOR_ROOT = getDirectory(ENV_VENDOR_ROOT, "/vendor");
    private static final String ENV_PRODUCT_ROOT = "PRODUCT_ROOT";
    private static final File DIR_PRODUCT_ROOT = getDirectory(ENV_PRODUCT_ROOT, "/product");

    static {
        initForCurrentUser();
        DIRECTORY_MUSIC = "Music";
        DIRECTORY_PODCASTS = "Podcasts";
        DIRECTORY_RINGTONES = "Ringtones";
        DIRECTORY_ALARMS = "Alarms";
        DIRECTORY_NOTIFICATIONS = "Notifications";
        DIRECTORY_PICTURES = "Pictures";
        DIRECTORY_MOVIES = "Movies";
        DIRECTORY_DOWNLOADS = "Download";
        DIRECTORY_DCIM = "DCIM";
        DIRECTORY_DOCUMENTS = "Documents";
        STANDARD_DIRECTORIES = new String[]{DIRECTORY_MUSIC, DIRECTORY_PODCASTS, DIRECTORY_RINGTONES, DIRECTORY_ALARMS, DIRECTORY_NOTIFICATIONS, DIRECTORY_PICTURES, DIRECTORY_MOVIES, DIRECTORY_DOWNLOADS, DIRECTORY_DCIM, DIRECTORY_DOCUMENTS};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initForCurrentUser() {
        int userId = UserHandle.myUserId();
        sCurrentUser = new UserEnvironment(userId);
    }

    /* loaded from: classes2.dex */
    public static class UserEnvironment {
        private final int mUserId;

        private protected UserEnvironment(int userId) {
            this.mUserId = userId;
        }

        private protected File[] getExternalDirs() {
            StorageVolume[] volumes = StorageManager.getVolumeList(this.mUserId, 256);
            File[] files = new File[volumes.length];
            for (int i = 0; i < volumes.length; i++) {
                files[i] = volumes[i].getPathFile();
            }
            return files;
        }

        @Deprecated
        private protected File getExternalStorageDirectory() {
            return getExternalDirs()[0];
        }

        @Deprecated
        private protected File getExternalStoragePublicDirectory(String type) {
            return buildExternalStoragePublicDirs(type)[0];
        }

        public synchronized File[] buildExternalStoragePublicDirs(String type) {
            return Environment.buildPaths(getExternalDirs(), type);
        }

        public synchronized File[] buildExternalStorageAndroidDataDirs() {
            return Environment.buildPaths(getExternalDirs(), "Android", "data");
        }

        public synchronized File[] buildExternalStorageAndroidObbDirs() {
            return Environment.buildPaths(getExternalDirs(), "Android", "obb");
        }

        public synchronized File[] buildExternalStorageAppDataDirs(String packageName) {
            return Environment.buildPaths(getExternalDirs(), "Android", "data", packageName);
        }

        public synchronized File[] buildExternalStorageAppMediaDirs(String packageName) {
            return Environment.buildPaths(getExternalDirs(), "Android", "media", packageName);
        }

        public synchronized File[] buildExternalStorageAppObbDirs(String packageName) {
            return Environment.buildPaths(getExternalDirs(), "Android", "obb", packageName);
        }

        public synchronized File[] buildExternalStorageAppFilesDirs(String packageName) {
            return Environment.buildPaths(getExternalDirs(), "Android", "data", packageName, Environment.DIR_FILES);
        }

        public synchronized File[] buildExternalStorageAppCacheDirs(String packageName) {
            return Environment.buildPaths(getExternalDirs(), "Android", "data", packageName, Environment.DIR_CACHE);
        }
    }

    public static File getRootDirectory() {
        return DIR_ANDROID_ROOT;
    }

    private protected static File getStorageDirectory() {
        return DIR_ANDROID_STORAGE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File getOemDirectory() {
        return DIR_OEM_ROOT;
    }

    public static synchronized File getOdmDirectory() {
        return DIR_ODM_ROOT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File getVendorDirectory() {
        return DIR_VENDOR_ROOT;
    }

    public static synchronized File getProductDirectory() {
        return DIR_PRODUCT_ROOT;
    }

    @Deprecated
    public static synchronized File getUserSystemDirectory(int userId) {
        return new File(new File(getDataSystemDirectory(), "users"), Integer.toString(userId));
    }

    @Deprecated
    public static synchronized File getUserConfigDirectory(int userId) {
        return new File(new File(new File(getDataDirectory(), "misc"), "user"), Integer.toString(userId));
    }

    public static File getDataDirectory() {
        return DIR_ANDROID_DATA;
    }

    public static synchronized File getDataDirectory(String volumeUuid) {
        if (TextUtils.isEmpty(volumeUuid)) {
            return DIR_ANDROID_DATA;
        }
        return new File("/mnt/expand/" + volumeUuid);
    }

    public static synchronized File getExpandDirectory() {
        return DIR_ANDROID_EXPAND;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File getDataSystemDirectory() {
        return new File(getDataDirectory(), StorageManager.UUID_SYSTEM);
    }

    public static synchronized File getDataSystemDeDirectory() {
        return buildPath(getDataDirectory(), "system_de");
    }

    public static synchronized File getDataSystemCeDirectory() {
        return buildPath(getDataDirectory(), "system_ce");
    }

    public static synchronized File getDataSystemCeDirectory(int userId) {
        return buildPath(getDataDirectory(), "system_ce", String.valueOf(userId));
    }

    public static synchronized File getDataSystemDeDirectory(int userId) {
        return buildPath(getDataDirectory(), "system_de", String.valueOf(userId));
    }

    public static synchronized File getDataMiscDirectory() {
        return new File(getDataDirectory(), "misc");
    }

    public static synchronized File getDataMiscCeDirectory() {
        return buildPath(getDataDirectory(), "misc_ce");
    }

    public static synchronized File getDataMiscCeDirectory(int userId) {
        return buildPath(getDataDirectory(), "misc_ce", String.valueOf(userId));
    }

    public static synchronized File getDataMiscDeDirectory(int userId) {
        return buildPath(getDataDirectory(), "misc_de", String.valueOf(userId));
    }

    private static synchronized File getDataProfilesDeDirectory(int userId) {
        return buildPath(getDataDirectory(), "misc", "profiles", "cur", String.valueOf(userId));
    }

    public static synchronized File getDataVendorCeDirectory(int userId) {
        return buildPath(getDataDirectory(), "vendor_ce", String.valueOf(userId));
    }

    public static synchronized File getDataVendorDeDirectory(int userId) {
        return buildPath(getDataDirectory(), "vendor_de", String.valueOf(userId));
    }

    public static synchronized File getDataRefProfilesDePackageDirectory(String packageName) {
        return buildPath(getDataDirectory(), "misc", "profiles", "ref", packageName);
    }

    public static synchronized File getDataProfilesDePackageDirectory(int userId, String packageName) {
        return buildPath(getDataProfilesDeDirectory(userId), packageName);
    }

    public static synchronized File getDataAppDirectory(String volumeUuid) {
        return new File(getDataDirectory(volumeUuid), "app");
    }

    public static synchronized File getDataUserCeDirectory(String volumeUuid) {
        return new File(getDataDirectory(volumeUuid), "user");
    }

    public static synchronized File getDataUserCeDirectory(String volumeUuid, int userId) {
        return new File(getDataUserCeDirectory(volumeUuid), String.valueOf(userId));
    }

    public static synchronized File getDataUserCePackageDirectory(String volumeUuid, int userId, String packageName) {
        return new File(getDataUserCeDirectory(volumeUuid, userId), packageName);
    }

    public static synchronized File getDataUserDeDirectory(String volumeUuid) {
        return new File(getDataDirectory(volumeUuid), "user_de");
    }

    public static synchronized File getDataUserDeDirectory(String volumeUuid, int userId) {
        return new File(getDataUserDeDirectory(volumeUuid), String.valueOf(userId));
    }

    public static synchronized File getDataUserDePackageDirectory(String volumeUuid, int userId, String packageName) {
        return new File(getDataUserDeDirectory(volumeUuid, userId), packageName);
    }

    public static synchronized File getDataPreloadsDirectory() {
        return new File(getDataDirectory(), "preloads");
    }

    public static synchronized File getDataPreloadsDemoDirectory() {
        return new File(getDataPreloadsDirectory(), "demo");
    }

    public static synchronized File getDataPreloadsAppsDirectory() {
        return new File(getDataPreloadsDirectory(), "apps");
    }

    public static synchronized File getDataPreloadsMediaDirectory() {
        return new File(getDataPreloadsDirectory(), "media");
    }

    public static synchronized File getDataPreloadsFileCacheDirectory(String packageName) {
        return new File(getDataPreloadsFileCacheDirectory(), packageName);
    }

    public static synchronized File getDataPreloadsFileCacheDirectory() {
        return new File(getDataPreloadsDirectory(), "file_cache");
    }

    public static File getExternalStorageDirectory() {
        throwIfUserRequired();
        return sCurrentUser.getExternalDirs()[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File getLegacyExternalStorageDirectory() {
        return new File(System.getenv(ENV_EXTERNAL_STORAGE));
    }

    private protected static File getLegacyExternalStorageObbDirectory() {
        return buildPath(getLegacyExternalStorageDirectory(), "Android", "obb");
    }

    public static synchronized boolean isStandardDirectory(String dir) {
        String[] strArr;
        for (String valid : STANDARD_DIRECTORIES) {
            if (valid.equals(dir)) {
                return true;
            }
        }
        return false;
    }

    public static synchronized int classifyExternalStorageDirectory(File dir) {
        File[] listFilesOrEmpty;
        int res = 0;
        for (File f : FileUtils.listFilesOrEmpty(dir)) {
            if (f.isFile() && isInterestingFile(f)) {
                res |= 131072;
            } else if (f.isDirectory() && hasInterestingFiles(f)) {
                String name = f.getName();
                if (DIRECTORY_MUSIC.equals(name)) {
                    res |= 1;
                } else if (DIRECTORY_PODCASTS.equals(name)) {
                    res |= 2;
                } else if (DIRECTORY_RINGTONES.equals(name)) {
                    res |= 4;
                } else if (DIRECTORY_ALARMS.equals(name)) {
                    res |= 8;
                } else if (DIRECTORY_NOTIFICATIONS.equals(name)) {
                    res |= 16;
                } else if (DIRECTORY_PICTURES.equals(name)) {
                    res |= 32;
                } else if (DIRECTORY_MOVIES.equals(name)) {
                    res |= 64;
                } else if (DIRECTORY_DOWNLOADS.equals(name)) {
                    res |= 128;
                } else if (DIRECTORY_DCIM.equals(name)) {
                    res |= 256;
                } else if (DIRECTORY_DOCUMENTS.equals(name)) {
                    res |= 512;
                } else {
                    res = "Android".equals(name) ? res | 65536 : res | 131072;
                }
            }
        }
        return res;
    }

    private static synchronized boolean hasInterestingFiles(File dir) {
        File[] listFilesOrEmpty;
        LinkedList<File> explore = new LinkedList<>();
        explore.add(dir);
        while (true) {
            if (explore.isEmpty()) {
                return false;
            }
            File dir2 = explore.pop();
            for (File f : FileUtils.listFilesOrEmpty(dir2)) {
                if (isInterestingFile(f)) {
                    return true;
                }
                if (f.isDirectory()) {
                    explore.add(f);
                }
            }
        }
    }

    private static synchronized boolean isInterestingFile(File file) {
        if (file.isFile()) {
            String name = file.getName().toLowerCase();
            return (name.endsWith(".exe") || name.equals("autorun.inf") || name.equals("launchpad.zip") || name.equals(MediaStore.MEDIA_IGNORE_FILENAME)) ? false : true;
        }
        return false;
    }

    public static File getExternalStoragePublicDirectory(String type) {
        throwIfUserRequired();
        return sCurrentUser.buildExternalStoragePublicDirs(type)[0];
    }

    private protected static File[] buildExternalStorageAndroidDataDirs() {
        throwIfUserRequired();
        return sCurrentUser.buildExternalStorageAndroidDataDirs();
    }

    private protected static File[] buildExternalStorageAppDataDirs(String packageName) {
        throwIfUserRequired();
        return sCurrentUser.buildExternalStorageAppDataDirs(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File[] buildExternalStorageAppMediaDirs(String packageName) {
        throwIfUserRequired();
        return sCurrentUser.buildExternalStorageAppMediaDirs(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File[] buildExternalStorageAppObbDirs(String packageName) {
        throwIfUserRequired();
        return sCurrentUser.buildExternalStorageAppObbDirs(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File[] buildExternalStorageAppFilesDirs(String packageName) {
        throwIfUserRequired();
        return sCurrentUser.buildExternalStorageAppFilesDirs(packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File[] buildExternalStorageAppCacheDirs(String packageName) {
        throwIfUserRequired();
        return sCurrentUser.buildExternalStorageAppCacheDirs(packageName);
    }

    public static File getDownloadCacheDirectory() {
        return DIR_DOWNLOAD_CACHE;
    }

    public static String getExternalStorageState() {
        File externalDir = sCurrentUser.getExternalDirs()[0];
        return getExternalStorageState(externalDir);
    }

    @Deprecated
    public static String getStorageState(File path) {
        return getExternalStorageState(path);
    }

    public static String getExternalStorageState(File path) {
        StorageVolume volume = StorageManager.getStorageVolume(path, UserHandle.myUserId());
        if (volume != null) {
            return volume.getState();
        }
        return "unknown";
    }

    public static boolean isExternalStorageRemovable() {
        File externalDir = sCurrentUser.getExternalDirs()[0];
        return isExternalStorageRemovable(externalDir);
    }

    public static boolean isExternalStorageRemovable(File path) {
        StorageVolume volume = StorageManager.getStorageVolume(path, UserHandle.myUserId());
        if (volume != null) {
            return volume.isRemovable();
        }
        throw new IllegalArgumentException("Failed to find storage device at " + path);
    }

    public static boolean isExternalStorageEmulated() {
        File externalDir = sCurrentUser.getExternalDirs()[0];
        return isExternalStorageEmulated(externalDir);
    }

    public static boolean isExternalStorageEmulated(File path) {
        StorageVolume volume = StorageManager.getStorageVolume(path, UserHandle.myUserId());
        if (volume != null) {
            return volume.isEmulated();
        }
        throw new IllegalArgumentException("Failed to find storage device at " + path);
    }

    static synchronized File getDirectory(String variableName, String defaultPath) {
        String path = System.getenv(variableName);
        return path == null ? new File(defaultPath) : new File(path);
    }

    public static synchronized void setUserRequired(boolean userRequired) {
        sUserRequired = userRequired;
    }

    private static synchronized void throwIfUserRequired() {
        if (sUserRequired) {
            Log.wtf(TAG, "Path requests must specify a user by using UserEnvironment", new Throwable());
        }
    }

    public static File[] buildPaths(File[] base, String... segments) {
        File[] result = new File[base.length];
        for (int i = 0; i < base.length; i++) {
            result[i] = buildPath(base[i], segments);
        }
        return result;
    }

    public static File buildPath(File base, String... segments) {
        File file;
        File cur = base;
        for (String segment : segments) {
            if (cur == null) {
                file = new File(segment);
            } else {
                file = new File(cur, segment);
            }
            cur = file;
        }
        return cur;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File maybeTranslateEmulatedPathToInternal(File path) {
        return StorageManager.maybeTranslateEmulatedPathToInternal(path);
    }
}
