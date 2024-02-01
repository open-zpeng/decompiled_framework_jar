package android.app;

import android.app.LoadedApk;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IContentProvider;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.CompatResources;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.service.notification.ZenModeConfig;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.autofill.AutofillManager;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import libcore.io.Memory;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ContextImpl extends Context {
    private static final boolean DEBUG = false;
    static final int STATE_INITIALIZING = 1;
    static final int STATE_NOT_FOUND = 3;
    static final int STATE_READY = 2;
    static final int STATE_UNINITIALIZED = 0;
    private static final String TAG = "ContextImpl";
    private static final String XATTR_INODE_CACHE = "user.inode_cache";
    private static final String XATTR_INODE_CODE_CACHE = "user.inode_code_cache";
    @GuardedBy("ContextImpl.class")
    public protected static ArrayMap<String, ArrayMap<File, SharedPreferencesImpl>> sSharedPrefsCache;
    private final IBinder mActivityToken;
    public protected final String mBasePackageName;
    @GuardedBy("mSync")
    private File mCacheDir;
    public protected ClassLoader mClassLoader;
    @GuardedBy("mSync")
    private File mCodeCacheDir;
    public protected final ApplicationContentResolver mContentResolver;
    @GuardedBy("mSync")
    private File mDatabasesDir;
    private Display mDisplay;
    @GuardedBy("mSync")
    private File mFilesDir;
    public protected final int mFlags;
    private boolean mIsAutofillCompatEnabled;
    public private protected final ActivityThread mMainThread;
    @GuardedBy("mSync")
    private File mNoBackupFilesDir;
    public protected final String mOpPackageName;
    public private protected final LoadedApk mPackageInfo;
    public protected PackageManager mPackageManager;
    @GuardedBy("mSync")
    public protected File mPreferencesDir;
    public protected Resources mResources;
    private final ResourcesManager mResourcesManager;
    @GuardedBy("ContextImpl.class")
    public protected ArrayMap<String, File> mSharedPrefsPaths;
    private String mSplitName;
    private final UserHandle mUser;
    public protected int mThemeResource = 0;
    public protected Resources.Theme mTheme = null;
    private Context mReceiverRestrictedContext = null;
    private AutofillManager.AutofillClient mAutofillClient = null;
    private final Object mSync = new Object();
    public private protected final Object[] mServiceCache = SystemServiceRegistry.createServiceCache();
    final int[] mServiceInitializationStateArray = new int[this.mServiceCache.length];
    public protected Context mOuterContext = this;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    @interface ServiceInitializationState {
    }

    public private protected static ContextImpl getImpl(Context context) {
        Context nextContext;
        while ((context instanceof ContextWrapper) && (nextContext = ((ContextWrapper) context).getBaseContext()) != null) {
            context = nextContext;
        }
        return (ContextImpl) context;
    }

    @Override // android.content.Context
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    @Override // android.content.Context
    public Resources getResources() {
        return this.mResources;
    }

    @Override // android.content.Context
    public PackageManager getPackageManager() {
        if (this.mPackageManager != null) {
            return this.mPackageManager;
        }
        IPackageManager pm = ActivityThread.getPackageManager();
        if (pm != null) {
            ApplicationPackageManager applicationPackageManager = new ApplicationPackageManager(this, pm);
            this.mPackageManager = applicationPackageManager;
            return applicationPackageManager;
        }
        return null;
    }

    @Override // android.content.Context
    public ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    @Override // android.content.Context
    public Looper getMainLooper() {
        return this.mMainThread.getLooper();
    }

    @Override // android.content.Context
    public Executor getMainExecutor() {
        return this.mMainThread.getExecutor();
    }

    @Override // android.content.Context
    public Context getApplicationContext() {
        return this.mPackageInfo != null ? this.mPackageInfo.getApplication() : this.mMainThread.getApplication();
    }

    @Override // android.content.Context
    public void setTheme(int resId) {
        synchronized (this.mSync) {
            if (this.mThemeResource != resId) {
                this.mThemeResource = resId;
                initializeTheme();
            }
        }
    }

    public synchronized int getThemeResId() {
        int i;
        synchronized (this.mSync) {
            i = this.mThemeResource;
        }
        return i;
    }

    @Override // android.content.Context
    public Resources.Theme getTheme() {
        synchronized (this.mSync) {
            if (this.mTheme != null) {
                return this.mTheme;
            }
            this.mThemeResource = Resources.selectDefaultTheme(this.mThemeResource, getOuterContext().getApplicationInfo().targetSdkVersion);
            initializeTheme();
            return this.mTheme;
        }
    }

    private synchronized void initializeTheme() {
        if (this.mTheme == null) {
            this.mTheme = this.mResources.newTheme();
        }
        this.mTheme.applyStyle(this.mThemeResource, true);
    }

    @Override // android.content.Context
    public ClassLoader getClassLoader() {
        return this.mClassLoader != null ? this.mClassLoader : this.mPackageInfo != null ? this.mPackageInfo.getClassLoader() : ClassLoader.getSystemClassLoader();
    }

    @Override // android.content.Context
    public String getPackageName() {
        if (this.mPackageInfo != null) {
            return this.mPackageInfo.getPackageName();
        }
        return ZenModeConfig.SYSTEM_AUTHORITY;
    }

    public synchronized String getBasePackageName() {
        return this.mBasePackageName != null ? this.mBasePackageName : getPackageName();
    }

    public synchronized String getOpPackageName() {
        return this.mOpPackageName != null ? this.mOpPackageName : getBasePackageName();
    }

    @Override // android.content.Context
    public ApplicationInfo getApplicationInfo() {
        if (this.mPackageInfo != null) {
            return this.mPackageInfo.getApplicationInfo();
        }
        throw new RuntimeException("Not supported in system context");
    }

    @Override // android.content.Context
    public String getPackageResourcePath() {
        if (this.mPackageInfo != null) {
            return this.mPackageInfo.getResDir();
        }
        throw new RuntimeException("Not supported in system context");
    }

    @Override // android.content.Context
    public String getPackageCodePath() {
        if (this.mPackageInfo != null) {
            return this.mPackageInfo.getAppDir();
        }
        throw new RuntimeException("Not supported in system context");
    }

    @Override // android.content.Context
    public SharedPreferences getSharedPreferences(String name, int mode) {
        File file;
        if (this.mPackageInfo.getApplicationInfo().targetSdkVersion < 19 && name == null) {
            name = "null";
        }
        synchronized (ContextImpl.class) {
            if (this.mSharedPrefsPaths == null) {
                this.mSharedPrefsPaths = new ArrayMap<>();
            }
            file = this.mSharedPrefsPaths.get(name);
            if (file == null) {
                int oldMask = StrictMode.allowThreadDiskReadsMask();
                file = getSharedPreferencesPath(name);
                StrictMode.setThreadPolicyMask(oldMask);
                this.mSharedPrefsPaths.put(name, file);
            }
        }
        return getSharedPreferences(file, mode);
    }

    private protected SharedPreferences getSharedPreferences(File file, int mode) {
        synchronized (ContextImpl.class) {
            ArrayMap<File, SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
            SharedPreferencesImpl sp = cache.get(file);
            if (sp == null) {
                checkMode(mode);
                if (getApplicationInfo().targetSdkVersion >= 26 && isCredentialProtectedStorage() && !((UserManager) getSystemService(UserManager.class)).isUserUnlockingOrUnlocked(UserHandle.myUserId())) {
                    throw new IllegalStateException("SharedPreferences in credential encrypted storage are not available until after user is unlocked");
                }
                SharedPreferencesImpl sp2 = new SharedPreferencesImpl(file, mode);
                cache.put(file, sp2);
                return sp2;
            }
            if ((mode & 4) != 0 || getApplicationInfo().targetSdkVersion < 11) {
                sp.startReloadIfChangedUnexpectedly();
            }
            return sp;
        }
    }

    @GuardedBy("ContextImpl.class")
    private synchronized ArrayMap<File, SharedPreferencesImpl> getSharedPreferencesCacheLocked() {
        if (sSharedPrefsCache == null) {
            sSharedPrefsCache = new ArrayMap<>();
        }
        String packageName = getPackageName();
        ArrayMap<File, SharedPreferencesImpl> packagePrefs = sSharedPrefsCache.get(packageName);
        if (packagePrefs == null) {
            ArrayMap<File, SharedPreferencesImpl> packagePrefs2 = new ArrayMap<>();
            sSharedPrefsCache.put(packageName, packagePrefs2);
            return packagePrefs2;
        }
        return packagePrefs;
    }

    @Override // android.content.Context
    public synchronized void reloadSharedPreferences() {
        int i;
        ArrayList<SharedPreferencesImpl> spImpls = new ArrayList<>();
        synchronized (ContextImpl.class) {
            ArrayMap<File, SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
            i = 0;
            for (int i2 = 0; i2 < cache.size(); i2++) {
                SharedPreferencesImpl sp = cache.valueAt(i2);
                if (sp != null) {
                    spImpls.add(sp);
                }
            }
        }
        while (true) {
            int i3 = i;
            if (i3 < spImpls.size()) {
                spImpls.get(i3).startReloadIfChangedUnexpectedly();
                i = i3 + 1;
            } else {
                return;
            }
        }
    }

    private static synchronized int moveFiles(File sourceDir, File targetDir, final String prefix) {
        File[] sourceFiles = FileUtils.listFilesOrEmpty(sourceDir, new FilenameFilter() { // from class: android.app.ContextImpl.1
            @Override // java.io.FilenameFilter
            public boolean accept(File dir, String name) {
                return name.startsWith(prefix);
            }
        });
        int res = 0;
        for (File sourceFile : sourceFiles) {
            File targetFile = new File(targetDir, sourceFile.getName());
            Log.d(TAG, "Migrating " + sourceFile + " to " + targetFile);
            try {
                FileUtils.copyFileOrThrow(sourceFile, targetFile);
                FileUtils.copyPermissions(sourceFile, targetFile);
            } catch (IOException e) {
                Log.w(TAG, "Failed to migrate " + sourceFile + ": " + e);
                res = -1;
            }
            if (!sourceFile.delete()) {
                throw new IOException("Failed to clean up " + sourceFile);
                break;
            }
            if (res != -1) {
                res++;
            }
        }
        return res;
    }

    @Override // android.content.Context
    public boolean moveSharedPreferencesFrom(Context sourceContext, String name) {
        boolean z;
        synchronized (ContextImpl.class) {
            File source = sourceContext.getSharedPreferencesPath(name);
            File target = getSharedPreferencesPath(name);
            int res = moveFiles(source.getParentFile(), target.getParentFile(), source.getName());
            if (res > 0) {
                ArrayMap<File, SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
                cache.remove(source);
                cache.remove(target);
            }
            z = res != -1;
        }
        return z;
    }

    @Override // android.content.Context
    public boolean deleteSharedPreferences(String name) {
        boolean z;
        synchronized (ContextImpl.class) {
            File prefs = getSharedPreferencesPath(name);
            File prefsBackup = SharedPreferencesImpl.makeBackupFile(prefs);
            ArrayMap<File, SharedPreferencesImpl> cache = getSharedPreferencesCacheLocked();
            cache.remove(prefs);
            prefs.delete();
            prefsBackup.delete();
            z = (prefs.exists() || prefsBackup.exists()) ? false : true;
        }
        return z;
    }

    public protected File getPreferencesDir() {
        File ensurePrivateDirExists;
        synchronized (this.mSync) {
            if (this.mPreferencesDir == null) {
                this.mPreferencesDir = new File(getDataDir(), "shared_prefs");
            }
            ensurePrivateDirExists = ensurePrivateDirExists(this.mPreferencesDir);
        }
        return ensurePrivateDirExists;
    }

    @Override // android.content.Context
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        File f = makeFilename(getFilesDir(), name);
        return new FileInputStream(f);
    }

    @Override // android.content.Context
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        checkMode(mode);
        boolean append = (32768 & mode) != 0;
        File f = makeFilename(getFilesDir(), name);
        try {
            FileOutputStream fos = new FileOutputStream(f, append);
            setFilePermissionsFromMode(f.getPath(), mode, 0);
            return fos;
        } catch (FileNotFoundException e) {
            File parent = f.getParentFile();
            parent.mkdir();
            FileUtils.setPermissions(parent.getPath(), 505, -1, -1);
            FileOutputStream fos2 = new FileOutputStream(f, append);
            setFilePermissionsFromMode(f.getPath(), mode, 0);
            return fos2;
        }
    }

    @Override // android.content.Context
    public boolean deleteFile(String name) {
        File f = makeFilename(getFilesDir(), name);
        return f.delete();
    }

    private static synchronized File ensurePrivateDirExists(File file) {
        return ensurePrivateDirExists(file, 505, -1, null);
    }

    private static synchronized File ensurePrivateCacheDirExists(File file, String xattr) {
        int gid = UserHandle.getCacheAppGid(Process.myUid());
        return ensurePrivateDirExists(file, MetricsProto.MetricsEvent.FIELD_PROCESS_RECORD_PROCESS_NAME, gid, xattr);
    }

    private static synchronized File ensurePrivateDirExists(File file, int mode, int gid, String xattr) {
        if (!file.exists()) {
            String path = file.getAbsolutePath();
            try {
                Os.mkdir(path, mode);
                Os.chmod(path, mode);
                if (gid != -1) {
                    Os.chown(path, -1, gid);
                }
            } catch (ErrnoException e) {
                if (e.errno != OsConstants.EEXIST) {
                    Log.w(TAG, "Failed to ensure " + file + ": " + e.getMessage());
                }
            }
            if (xattr != null) {
                try {
                    StructStat stat = Os.stat(file.getAbsolutePath());
                    byte[] value = new byte[8];
                    Memory.pokeLong(value, 0, stat.st_ino, ByteOrder.nativeOrder());
                    Os.setxattr(file.getParentFile().getAbsolutePath(), xattr, value, 0);
                } catch (ErrnoException e2) {
                    Log.w(TAG, "Failed to update " + xattr + ": " + e2.getMessage());
                }
            }
        }
        return file;
    }

    @Override // android.content.Context
    public File getFilesDir() {
        File ensurePrivateDirExists;
        synchronized (this.mSync) {
            if (this.mFilesDir == null) {
                this.mFilesDir = new File(getDataDir(), "files");
            }
            ensurePrivateDirExists = ensurePrivateDirExists(this.mFilesDir);
        }
        return ensurePrivateDirExists;
    }

    @Override // android.content.Context
    public File getNoBackupFilesDir() {
        File ensurePrivateDirExists;
        synchronized (this.mSync) {
            if (this.mNoBackupFilesDir == null) {
                this.mNoBackupFilesDir = new File(getDataDir(), "no_backup");
            }
            ensurePrivateDirExists = ensurePrivateDirExists(this.mNoBackupFilesDir);
        }
        return ensurePrivateDirExists;
    }

    @Override // android.content.Context
    public File getExternalFilesDir(String type) {
        File[] dirs = getExternalFilesDirs(type);
        if (dirs == null || dirs.length <= 0) {
            return null;
        }
        return dirs[0];
    }

    @Override // android.content.Context
    public File[] getExternalFilesDirs(String type) {
        File[] ensureExternalDirsExistOrFilter;
        synchronized (this.mSync) {
            File[] dirs = Environment.buildExternalStorageAppFilesDirs(getPackageName());
            if (type != null) {
                dirs = Environment.buildPaths(dirs, type);
            }
            ensureExternalDirsExistOrFilter = ensureExternalDirsExistOrFilter(dirs);
        }
        return ensureExternalDirsExistOrFilter;
    }

    @Override // android.content.Context
    public File getObbDir() {
        File[] dirs = getObbDirs();
        if (dirs == null || dirs.length <= 0) {
            return null;
        }
        return dirs[0];
    }

    @Override // android.content.Context
    public File[] getObbDirs() {
        File[] ensureExternalDirsExistOrFilter;
        synchronized (this.mSync) {
            File[] dirs = Environment.buildExternalStorageAppObbDirs(getPackageName());
            ensureExternalDirsExistOrFilter = ensureExternalDirsExistOrFilter(dirs);
        }
        return ensureExternalDirsExistOrFilter;
    }

    @Override // android.content.Context
    public File getCacheDir() {
        File ensurePrivateCacheDirExists;
        synchronized (this.mSync) {
            if (this.mCacheDir == null) {
                this.mCacheDir = new File(getDataDir(), "cache");
            }
            ensurePrivateCacheDirExists = ensurePrivateCacheDirExists(this.mCacheDir, XATTR_INODE_CACHE);
        }
        return ensurePrivateCacheDirExists;
    }

    @Override // android.content.Context
    public File getCodeCacheDir() {
        File ensurePrivateCacheDirExists;
        synchronized (this.mSync) {
            if (this.mCodeCacheDir == null) {
                this.mCodeCacheDir = new File(getDataDir(), "code_cache");
            }
            ensurePrivateCacheDirExists = ensurePrivateCacheDirExists(this.mCodeCacheDir, XATTR_INODE_CODE_CACHE);
        }
        return ensurePrivateCacheDirExists;
    }

    @Override // android.content.Context
    public File getExternalCacheDir() {
        File[] dirs = getExternalCacheDirs();
        if (dirs == null || dirs.length <= 0) {
            return null;
        }
        return dirs[0];
    }

    @Override // android.content.Context
    public File[] getExternalCacheDirs() {
        File[] ensureExternalDirsExistOrFilter;
        synchronized (this.mSync) {
            File[] dirs = Environment.buildExternalStorageAppCacheDirs(getPackageName());
            ensureExternalDirsExistOrFilter = ensureExternalDirsExistOrFilter(dirs);
        }
        return ensureExternalDirsExistOrFilter;
    }

    @Override // android.content.Context
    public File[] getExternalMediaDirs() {
        File[] ensureExternalDirsExistOrFilter;
        synchronized (this.mSync) {
            File[] dirs = Environment.buildExternalStorageAppMediaDirs(getPackageName());
            ensureExternalDirsExistOrFilter = ensureExternalDirsExistOrFilter(dirs);
        }
        return ensureExternalDirsExistOrFilter;
    }

    @Override // android.content.Context
    public File getPreloadsFileCache() {
        return Environment.getDataPreloadsFileCacheDirectory(getPackageName());
    }

    @Override // android.content.Context
    public File getFileStreamPath(String name) {
        return makeFilename(getFilesDir(), name);
    }

    private protected File getSharedPreferencesPath(String name) {
        File preferencesDir = getPreferencesDir();
        return makeFilename(preferencesDir, name + ".xml");
    }

    @Override // android.content.Context
    public String[] fileList() {
        return FileUtils.listOrEmpty(getFilesDir());
    }

    @Override // android.content.Context
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return openOrCreateDatabase(name, mode, factory, null);
    }

    @Override // android.content.Context
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        checkMode(mode);
        File f = getDatabasePath(name);
        int flags = (mode & 8) != 0 ? 268435456 | 536870912 : 268435456;
        if ((mode & 16) != 0) {
            flags |= 16;
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase(f.getPath(), factory, flags, errorHandler);
        setFilePermissionsFromMode(f.getPath(), mode, 0);
        return db;
    }

    @Override // android.content.Context
    public boolean moveDatabaseFrom(Context sourceContext, String name) {
        boolean z;
        synchronized (ContextImpl.class) {
            File source = sourceContext.getDatabasePath(name);
            File target = getDatabasePath(name);
            z = moveFiles(source.getParentFile(), target.getParentFile(), source.getName()) != -1;
        }
        return z;
    }

    @Override // android.content.Context
    public boolean deleteDatabase(String name) {
        try {
            File f = getDatabasePath(name);
            return SQLiteDatabase.deleteDatabase(f);
        } catch (Exception e) {
            return false;
        }
    }

    @Override // android.content.Context
    public File getDatabasePath(String name) {
        File f;
        if (name.charAt(0) == File.separatorChar) {
            String dirPath = name.substring(0, name.lastIndexOf(File.separatorChar));
            File dir = new File(dirPath);
            f = new File(dir, name.substring(name.lastIndexOf(File.separatorChar)));
            if (!dir.isDirectory() && dir.mkdir()) {
                FileUtils.setPermissions(dir.getPath(), 505, -1, -1);
            }
        } else {
            f = makeFilename(getDatabasesDir(), name);
        }
        return f;
    }

    @Override // android.content.Context
    public String[] databaseList() {
        return FileUtils.listOrEmpty(getDatabasesDir());
    }

    private synchronized File getDatabasesDir() {
        File ensurePrivateDirExists;
        synchronized (this.mSync) {
            if (this.mDatabasesDir == null) {
                if (ZenModeConfig.SYSTEM_AUTHORITY.equals(getPackageName())) {
                    this.mDatabasesDir = new File("/data/system");
                } else {
                    this.mDatabasesDir = new File(getDataDir(), "databases");
                }
            }
            ensurePrivateDirExists = ensurePrivateDirExists(this.mDatabasesDir);
        }
        return ensurePrivateDirExists;
    }

    @Override // android.content.Context
    @Deprecated
    public Drawable getWallpaper() {
        return getWallpaperManager().getDrawable();
    }

    @Override // android.content.Context
    @Deprecated
    public Drawable peekWallpaper() {
        return getWallpaperManager().peekDrawable();
    }

    @Override // android.content.Context
    @Deprecated
    public int getWallpaperDesiredMinimumWidth() {
        return getWallpaperManager().getDesiredMinimumWidth();
    }

    @Override // android.content.Context
    @Deprecated
    public int getWallpaperDesiredMinimumHeight() {
        return getWallpaperManager().getDesiredMinimumHeight();
    }

    @Override // android.content.Context
    @Deprecated
    public void setWallpaper(Bitmap bitmap) throws IOException {
        getWallpaperManager().setBitmap(bitmap);
    }

    @Override // android.content.Context
    @Deprecated
    public void setWallpaper(InputStream data) throws IOException {
        getWallpaperManager().setStream(data);
    }

    @Override // android.content.Context
    @Deprecated
    public void clearWallpaper() throws IOException {
        getWallpaperManager().clear();
    }

    private synchronized WallpaperManager getWallpaperManager() {
        return (WallpaperManager) getSystemService(WallpaperManager.class);
    }

    @Override // android.content.Context
    public void startActivity(Intent intent) {
        warnIfCallingFromSystemProcess();
        startActivity(intent, null);
    }

    public synchronized void startActivityAsUser(Intent intent, UserHandle user) {
        startActivityAsUser(intent, null, user);
    }

    @Override // android.content.Context
    public void startActivity(Intent intent, Bundle options) {
        warnIfCallingFromSystemProcess();
        int targetSdkVersion = getApplicationInfo().targetSdkVersion;
        if ((intent.getFlags() & 268435456) == 0 && ((targetSdkVersion < 24 || targetSdkVersion >= 28) && (options == null || ActivityOptions.fromBundle(options).getLaunchTaskId() == -1))) {
            throw new AndroidRuntimeException("Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?");
        }
        this.mMainThread.getInstrumentation().execStartActivity(getOuterContext(), this.mMainThread.getApplicationThread(), (IBinder) null, (Activity) null, intent, -1, options);
    }

    public synchronized void startActivityAsUser(Intent intent, Bundle options, UserHandle user) {
        try {
            ActivityManager.getService().startActivityAsUser(this.mMainThread.getApplicationThread(), getBasePackageName(), intent, intent.resolveTypeIfNeeded(getContentResolver()), null, null, 0, 268435456, null, options, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void startActivities(Intent[] intents) {
        warnIfCallingFromSystemProcess();
        startActivities(intents, null);
    }

    @Override // android.content.Context
    public synchronized int startActivitiesAsUser(Intent[] intents, Bundle options, UserHandle userHandle) {
        if ((intents[0].getFlags() & 268435456) == 0) {
            throw new AndroidRuntimeException("Calling startActivities() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag on first Intent. Is this really what you want?");
        }
        return this.mMainThread.getInstrumentation().execStartActivitiesAsUser(getOuterContext(), this.mMainThread.getApplicationThread(), null, null, intents, options, userHandle.getIdentifier());
    }

    @Override // android.content.Context
    public void startActivities(Intent[] intents, Bundle options) {
        warnIfCallingFromSystemProcess();
        if ((intents[0].getFlags() & 268435456) == 0) {
            throw new AndroidRuntimeException("Calling startActivities() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag on first Intent. Is this really what you want?");
        }
        this.mMainThread.getInstrumentation().execStartActivities(getOuterContext(), this.mMainThread.getApplicationThread(), null, null, intents, options);
    }

    @Override // android.content.Context
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {
        startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, null);
    }

    @Override // android.content.Context
    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        String resolvedType = null;
        if (fillInIntent != null) {
            try {
                fillInIntent.migrateExtraStreamToClipData();
                fillInIntent.prepareToLeaveProcess(this);
                resolvedType = fillInIntent.resolveTypeIfNeeded(getContentResolver());
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        int result = ActivityManager.getService().startActivityIntentSender(this.mMainThread.getApplicationThread(), intent != null ? intent.getTarget() : null, intent != null ? intent.getWhitelistToken() : null, fillInIntent, resolvedType, null, null, 0, flagsMask, flagsValues, options);
        if (result == -96) {
            throw new IntentSender.SendIntentException();
        }
        Instrumentation.checkStartActivityResult(result, null);
    }

    @Override // android.content.Context
    public void sendBroadcast(Intent intent) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void sendBroadcast(Intent intent, String receiverPermission) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public synchronized void sendBroadcastMultiplePermissions(Intent intent, String[] receiverPermissions) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public synchronized void sendBroadcastAsUserMultiplePermissions(Intent intent, UserHandle user, String[] receiverPermissions) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void sendBroadcast(Intent intent, String receiverPermission, Bundle options) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, options, false, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void sendBroadcast(Intent intent, String receiverPermission, int appOp) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, appOp, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, true, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, -1, resultReceiver, scheduler, initialCode, initialData, initialExtras, null);
    }

    @Override // android.content.Context
    public void sendOrderedBroadcast(Intent intent, String receiverPermission, Bundle options, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, -1, resultReceiver, scheduler, initialCode, initialData, initialExtras, options);
    }

    public synchronized void sendOrderedBroadcast(Intent intent, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, appOp, resultReceiver, scheduler, initialCode, initialData, initialExtras, null);
    }

    synchronized void sendOrderedBroadcast(Intent intent, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras, Bundle options) {
        Handler scheduler2;
        warnIfCallingFromSystemProcess();
        IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (this.mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler2 = this.mMainThread.getHandler();
                } else {
                    scheduler2 = scheduler;
                }
                rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, this.mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler2 = this.mMainThread.getHandler();
                } else {
                    scheduler2 = scheduler;
                }
                rd = new LoadedApk.ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, null, false).getIIntentReceiver();
            }
        }
        IIntentReceiver rd2 = rd;
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd2, initialCode, initialData, initialExtras, receiverPermissions, appOp, options, true, false, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void sendBroadcastAsUser(Intent intent, UserHandle user) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {
        sendBroadcastAsUser(intent, user, receiverPermission, -1);
    }

    @Override // android.content.Context
    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, Bundle options) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, options, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, appOp, null, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcastAsUser(intent, user, receiverPermission, -1, null, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    public synchronized void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcastAsUser(intent, user, receiverPermission, appOp, null, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    public synchronized void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp, Bundle options, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        Handler scheduler2;
        IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (this.mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler2 = this.mMainThread.getHandler();
                } else {
                    scheduler2 = scheduler;
                }
                rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, this.mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler2 = this.mMainThread.getHandler();
                } else {
                    scheduler2 = scheduler;
                }
                rd = new LoadedApk.ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, null, false).getIIntentReceiver();
            }
        }
        IIntentReceiver rd2 = rd;
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd2, initialCode, initialData, initialExtras, receiverPermissions, appOp, options, true, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    @Deprecated
    public void sendStickyBroadcast(Intent intent) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, true, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    @Deprecated
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        Handler scheduler2;
        warnIfCallingFromSystemProcess();
        IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (this.mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler2 = this.mMainThread.getHandler();
                } else {
                    scheduler2 = scheduler;
                }
                rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, this.mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler2 = this.mMainThread.getHandler();
                } else {
                    scheduler2 = scheduler;
                }
                rd = new LoadedApk.ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, null, false).getIIntentReceiver();
            }
        }
        IIntentReceiver rd2 = rd;
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd2, initialCode, initialData, initialExtras, null, -1, null, true, true, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    @Deprecated
    public void removeStickyBroadcast(Intent intent) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        if (resolvedType != null) {
            intent = new Intent(intent);
            intent.setDataAndType(intent.getData(), resolvedType);
        }
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().unbroadcastIntent(this.mMainThread.getApplicationThread(), intent, getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    @Deprecated
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, true, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    @Deprecated
    public synchronized void sendStickyBroadcastAsUser(Intent intent, UserHandle user, Bundle options) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, options, false, true, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    @Deprecated
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        Handler scheduler2;
        IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (this.mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler2 = this.mMainThread.getHandler();
                } else {
                    scheduler2 = scheduler;
                }
                rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, this.mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler2 = this.mMainThread.getHandler();
                } else {
                    scheduler2 = scheduler;
                }
                rd = new LoadedApk.ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler2, null, false).getIIntentReceiver();
            }
        }
        IIntentReceiver rd2 = rd;
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd2, initialCode, initialData, initialExtras, null, -1, null, true, true, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    @Deprecated
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        if (resolvedType != null) {
            intent = new Intent(intent);
            intent.setDataAndType(intent.getData(), resolvedType);
        }
        try {
            intent.prepareToLeaveProcess(this);
            ActivityManager.getService().unbroadcastIntent(this.mMainThread.getApplicationThread(), intent, user.getIdentifier());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return registerReceiver(receiver, filter, null, null);
    }

    @Override // android.content.Context
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, int flags) {
        return registerReceiver(receiver, filter, null, null, flags);
    }

    @Override // android.content.Context
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return registerReceiverInternal(receiver, getUserId(), filter, broadcastPermission, scheduler, getOuterContext(), 0);
    }

    @Override // android.content.Context
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler, int flags) {
        return registerReceiverInternal(receiver, getUserId(), filter, broadcastPermission, scheduler, getOuterContext(), flags);
    }

    public synchronized Intent registerReceiverAsUser(BroadcastReceiver receiver, UserHandle user, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return registerReceiverInternal(receiver, user.getIdentifier(), filter, broadcastPermission, scheduler, getOuterContext(), 0);
    }

    private synchronized Intent registerReceiverInternal(BroadcastReceiver receiver, int userId, IntentFilter filter, String broadcastPermission, Handler scheduler, Context context, int flags) {
        Handler scheduler2;
        IIntentReceiver rd = null;
        if (receiver != null) {
            if (this.mPackageInfo != null && context != null) {
                if (scheduler == null) {
                    scheduler2 = this.mMainThread.getHandler();
                } else {
                    scheduler2 = scheduler;
                }
                rd = this.mPackageInfo.getReceiverDispatcher(receiver, context, scheduler2, this.mMainThread.getInstrumentation(), true);
            } else {
                if (scheduler == null) {
                    scheduler2 = this.mMainThread.getHandler();
                } else {
                    scheduler2 = scheduler;
                }
                rd = new LoadedApk.ReceiverDispatcher(receiver, context, scheduler2, null, true).getIIntentReceiver();
            }
        }
        if (ZenModeConfig.SYSTEM_AUTHORITY.equals(this.mBasePackageName)) {
            Slog.w(TAG, "android registerReceiver:" + receiver);
        }
        try {
            Intent intent = ActivityManager.getService().registerReceiver(this.mMainThread.getApplicationThread(), this.mBasePackageName, rd, filter, broadcastPermission, userId, flags);
            if (intent != null) {
                intent.setExtrasClassLoader(getClassLoader());
                intent.prepareToEnterProcess();
            }
            return intent;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void unregisterReceiver(BroadcastReceiver receiver) {
        if (this.mPackageInfo != null) {
            IIntentReceiver rd = this.mPackageInfo.forgetReceiverDispatcher(getOuterContext(), receiver);
            try {
                ActivityManager.getService().unregisterReceiver(rd);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new RuntimeException("Not supported in system context");
    }

    private synchronized void validateServiceIntent(Intent service) {
        if (service.getComponent() == null && service.getPackage() == null) {
            if (getApplicationInfo().targetSdkVersion >= 21) {
                IllegalArgumentException ex = new IllegalArgumentException("Service Intent must be explicit: " + service);
                throw ex;
            }
            Log.w(TAG, "Implicit intents with startService are not safe: " + service + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + Debug.getCallers(2, 3));
        }
    }

    @Override // android.content.Context
    public ComponentName startService(Intent service) {
        warnIfCallingFromSystemProcess();
        return startServiceCommon(service, false, this.mUser);
    }

    @Override // android.content.Context
    public ComponentName startForegroundService(Intent service) {
        warnIfCallingFromSystemProcess();
        return startServiceCommon(service, true, this.mUser);
    }

    @Override // android.content.Context
    public boolean stopService(Intent service) {
        warnIfCallingFromSystemProcess();
        return stopServiceCommon(service, this.mUser);
    }

    public synchronized ComponentName startServiceAsUser(Intent service, UserHandle user) {
        return startServiceCommon(service, false, user);
    }

    @Override // android.content.Context
    public synchronized ComponentName startForegroundServiceAsUser(Intent service, UserHandle user) {
        return startServiceCommon(service, true, user);
    }

    private synchronized ComponentName startServiceCommon(Intent service, boolean requireForeground, UserHandle user) {
        try {
            validateServiceIntent(service);
            service.prepareToLeaveProcess(this);
            ComponentName cn = ActivityManager.getService().startService(this.mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(getContentResolver()), requireForeground, getOpPackageName(), user.getIdentifier());
            if (cn != null) {
                if (cn.getPackageName().equals("!")) {
                    throw new SecurityException("Not allowed to start service " + service + " without permission " + cn.getClassName());
                } else if (cn.getPackageName().equals("!!")) {
                    throw new SecurityException("Unable to start service " + service + ": " + cn.getClassName());
                } else if (cn.getPackageName().equals("?")) {
                    throw new IllegalStateException("Not allowed to start service " + service + ": " + cn.getClassName());
                }
            }
            return cn;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public synchronized boolean stopServiceAsUser(Intent service, UserHandle user) {
        return stopServiceCommon(service, user);
    }

    private synchronized boolean stopServiceCommon(Intent service, UserHandle user) {
        try {
            validateServiceIntent(service);
            service.prepareToLeaveProcess(this);
            int res = ActivityManager.getService().stopService(this.mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(getContentResolver()), user.getIdentifier());
            if (res >= 0) {
                return res != 0;
            }
            throw new SecurityException("Not allowed to stop service " + service);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        warnIfCallingFromSystemProcess();
        return bindServiceCommon(service, conn, flags, this.mMainThread.getHandler(), getUser());
    }

    @Override // android.content.Context
    public boolean bindServiceAsUser(Intent service, ServiceConnection conn, int flags, UserHandle user) {
        return bindServiceCommon(service, conn, flags, this.mMainThread.getHandler(), user);
    }

    public synchronized boolean bindServiceAsUser(Intent service, ServiceConnection conn, int flags, Handler handler, UserHandle user) {
        if (handler == null) {
            throw new IllegalArgumentException("handler must not be null.");
        }
        return bindServiceCommon(service, conn, flags, handler, user);
    }

    @Override // android.content.Context
    public synchronized IServiceConnection getServiceDispatcher(ServiceConnection conn, Handler handler, int flags) {
        return this.mPackageInfo.getServiceDispatcher(conn, getOuterContext(), handler, flags);
    }

    @Override // android.content.Context
    public synchronized IApplicationThread getIApplicationThread() {
        return this.mMainThread.getApplicationThread();
    }

    @Override // android.content.Context
    public synchronized Handler getMainThreadHandler() {
        return this.mMainThread.getHandler();
    }

    private synchronized boolean bindServiceCommon(Intent service, ServiceConnection conn, int flags, Handler handler, UserHandle user) {
        int i = flags;
        if (conn == null) {
            throw new IllegalArgumentException("connection is null");
        }
        if (this.mPackageInfo == null) {
            throw new RuntimeException("Not supported in system context");
        }
        IServiceConnection sd = this.mPackageInfo.getServiceDispatcher(conn, getOuterContext(), handler, i);
        validateServiceIntent(service);
        try {
            IBinder token = getActivityToken();
            if (token == null && (i & 1) == 0 && this.mPackageInfo != null) {
                if (this.mPackageInfo.getApplicationInfo().targetSdkVersion < 14) {
                    i |= 32;
                }
            }
            int flags2 = i;
            try {
                service.prepareToLeaveProcess(this);
                int res = ActivityManager.getService().bindService(this.mMainThread.getApplicationThread(), getActivityToken(), service, service.resolveTypeIfNeeded(getContentResolver()), sd, flags2, getOpPackageName(), user.getIdentifier());
                if (res >= 0) {
                    return res != 0;
                }
                throw new SecurityException("Not allowed to bind to service " + service);
            } catch (RemoteException e) {
                e = e;
                throw e.rethrowFromSystemServer();
            }
        } catch (RemoteException e2) {
            e = e2;
        }
    }

    @Override // android.content.Context
    public void unbindService(ServiceConnection conn) {
        if (conn == null) {
            throw new IllegalArgumentException("connection is null");
        }
        if (this.mPackageInfo != null) {
            IServiceConnection sd = this.mPackageInfo.forgetServiceDispatcher(getOuterContext(), conn);
            try {
                ActivityManager.getService().unbindService(sd);
                return;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        throw new RuntimeException("Not supported in system context");
    }

    @Override // android.content.Context
    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
        if (arguments != null) {
            try {
                arguments.setAllowFds(false);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
        return ActivityManager.getService().startInstrumentation(className, profileFile, 0, arguments, null, null, getUserId(), null);
    }

    @Override // android.content.Context
    public Object getSystemService(String name) {
        return SystemServiceRegistry.getSystemService(this, name);
    }

    @Override // android.content.Context
    public String getSystemServiceName(Class<?> serviceClass) {
        return SystemServiceRegistry.getSystemServiceName(serviceClass);
    }

    @Override // android.content.Context
    public int checkPermission(String permission, int pid, int uid) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        IActivityManager am = ActivityManager.getService();
        if (am == null) {
            int appId = UserHandle.getAppId(uid);
            if (appId == 0 || appId == 1000) {
                Slog.w(TAG, "Missing ActivityManager; assuming " + uid + " holds " + permission);
                return 0;
            }
            Slog.w(TAG, "Missing ActivityManager; assuming " + uid + " does not hold " + permission);
            return -1;
        }
        try {
            return am.checkPermission(permission, pid, uid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized int checkPermission(String permission, int pid, int uid, IBinder callerToken) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        try {
            return ActivityManager.getService().checkPermissionWithToken(permission, pid, uid, callerToken);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public int checkCallingPermission(String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        int pid = Binder.getCallingPid();
        if (pid != Process.myPid()) {
            return checkPermission(permission, pid, Binder.getCallingUid());
        }
        return -1;
    }

    @Override // android.content.Context
    public int checkCallingOrSelfPermission(String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        return checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid());
    }

    @Override // android.content.Context
    public int checkSelfPermission(String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        return checkPermission(permission, Process.myPid(), Process.myUid());
    }

    private synchronized void enforce(String permission, int resultOfCheck, boolean selfToo, int uid, String message) {
        String str;
        String str2;
        if (resultOfCheck != 0) {
            StringBuilder sb = new StringBuilder();
            if (message != null) {
                str = message + ": ";
            } else {
                str = "";
            }
            sb.append(str);
            if (selfToo) {
                str2 = "Neither user " + uid + " nor current process has ";
            } else {
                str2 = "uid " + uid + " does not have ";
            }
            sb.append(str2);
            sb.append(permission);
            sb.append(".");
            throw new SecurityException(sb.toString());
        }
    }

    @Override // android.content.Context
    public void enforcePermission(String permission, int pid, int uid, String message) {
        enforce(permission, checkPermission(permission, pid, uid), false, uid, message);
    }

    @Override // android.content.Context
    public void enforceCallingPermission(String permission, String message) {
        enforce(permission, checkCallingPermission(permission), false, Binder.getCallingUid(), message);
    }

    @Override // android.content.Context
    public void enforceCallingOrSelfPermission(String permission, String message) {
        enforce(permission, checkCallingOrSelfPermission(permission), true, Binder.getCallingUid(), message);
    }

    @Override // android.content.Context
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        try {
            ActivityManager.getService().grantUriPermission(this.mMainThread.getApplicationThread(), toPackage, ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void revokeUriPermission(Uri uri, int modeFlags) {
        try {
            ActivityManager.getService().revokeUriPermission(this.mMainThread.getApplicationThread(), null, ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public void revokeUriPermission(String targetPackage, Uri uri, int modeFlags) {
        try {
            ActivityManager.getService().revokeUriPermission(this.mMainThread.getApplicationThread(), targetPackage, ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        try {
            return ActivityManager.getService().checkUriPermission(ContentProvider.getUriWithoutUserId(uri), pid, uid, modeFlags, resolveUserId(uri), null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.content.Context
    public synchronized int checkUriPermission(Uri uri, int pid, int uid, int modeFlags, IBinder callerToken) {
        try {
            return ActivityManager.getService().checkUriPermission(ContentProvider.getUriWithoutUserId(uri), pid, uid, modeFlags, resolveUserId(uri), callerToken);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private synchronized int resolveUserId(Uri uri) {
        return ContentProvider.getUserIdFromUri(uri, getUserId());
    }

    @Override // android.content.Context
    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        int pid = Binder.getCallingPid();
        if (pid != Process.myPid()) {
            return checkUriPermission(uri, pid, Binder.getCallingUid(), modeFlags);
        }
        return -1;
    }

    @Override // android.content.Context
    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return checkUriPermission(uri, Binder.getCallingPid(), Binder.getCallingUid(), modeFlags);
    }

    @Override // android.content.Context
    public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
        if ((modeFlags & 1) == 0 || !(readPermission == null || checkPermission(readPermission, pid, uid) == 0)) {
            if ((modeFlags & 2) == 0 || !(writePermission == null || checkPermission(writePermission, pid, uid) == 0)) {
                if (uri != null) {
                    return checkUriPermission(uri, pid, uid, modeFlags);
                }
                return -1;
            }
            return 0;
        }
        return 0;
    }

    private synchronized String uriModeFlagToString(int uriModeFlags) {
        StringBuilder builder = new StringBuilder();
        if ((uriModeFlags & 1) != 0) {
            builder.append("read and ");
        }
        if ((uriModeFlags & 2) != 0) {
            builder.append("write and ");
        }
        if ((uriModeFlags & 64) != 0) {
            builder.append("persistable and ");
        }
        if ((uriModeFlags & 128) != 0) {
            builder.append("prefix and ");
        }
        if (builder.length() > 5) {
            builder.setLength(builder.length() - 5);
            return builder.toString();
        }
        throw new IllegalArgumentException("Unknown permission mode flags: " + uriModeFlags);
    }

    private synchronized void enforceForUri(int modeFlags, int resultOfCheck, boolean selfToo, int uid, Uri uri, String message) {
        String str;
        String str2;
        if (resultOfCheck != 0) {
            StringBuilder sb = new StringBuilder();
            if (message != null) {
                str = message + ": ";
            } else {
                str = "";
            }
            sb.append(str);
            if (selfToo) {
                str2 = "Neither user " + uid + " nor current process has ";
            } else {
                str2 = "User " + uid + " does not have ";
            }
            sb.append(str2);
            sb.append(uriModeFlagToString(modeFlags));
            sb.append(" permission on ");
            sb.append(uri);
            sb.append(".");
            throw new SecurityException(sb.toString());
        }
    }

    @Override // android.content.Context
    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {
        enforceForUri(modeFlags, checkUriPermission(uri, pid, uid, modeFlags), false, uid, uri, message);
    }

    @Override // android.content.Context
    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {
        enforceForUri(modeFlags, checkCallingUriPermission(uri, modeFlags), false, Binder.getCallingUid(), uri, message);
    }

    @Override // android.content.Context
    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {
        enforceForUri(modeFlags, checkCallingOrSelfUriPermission(uri, modeFlags), true, Binder.getCallingUid(), uri, message);
    }

    @Override // android.content.Context
    public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {
        enforceForUri(modeFlags, checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags), false, uid, uri, message);
    }

    private synchronized void warnIfCallingFromSystemProcess() {
        if (Process.myUid() == 1000) {
            Slog.d(TAG, "Calling a method in the system process without a qualified user: " + Debug.getCallers(5));
        }
    }

    private static synchronized Resources createResources(IBinder activityToken, LoadedApk pi, String splitName, int displayId, Configuration overrideConfig, CompatibilityInfo compatInfo) {
        try {
            String[] splitResDirs = pi.getSplitPaths(splitName);
            ClassLoader classLoader = pi.getSplitClassLoader(splitName);
            return ResourcesManager.getInstance().getResources(activityToken, pi.getResDir(), splitResDirs, pi.getOverlayDirs(), pi.getApplicationInfo().sharedLibraryFiles, displayId, overrideConfig, compatInfo, classLoader);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Context createApplicationContext(ApplicationInfo application, int flags) throws PackageManager.NameNotFoundException {
        LoadedApk pi = this.mMainThread.getPackageInfo(application, this.mResources.getCompatibilityInfo(), 1073741824 | flags);
        if (pi != null) {
            ContextImpl c = new ContextImpl(this, this.mMainThread, pi, null, this.mActivityToken, new UserHandle(UserHandle.getUserId(application.uid)), flags, null);
            int displayId = this.mDisplay != null ? this.mDisplay.getDisplayId() : 0;
            c.setResources(createResources(this.mActivityToken, pi, null, displayId, null, getDisplayAdjustments(displayId).getCompatibilityInfo()));
            if (c.mResources != null) {
                return c;
            }
        }
        throw new PackageManager.NameNotFoundException("Application package " + application.packageName + " not found");
    }

    @Override // android.content.Context
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return createPackageContextAsUser(packageName, flags, this.mUser);
    }

    @Override // android.content.Context
    public Context createPackageContextAsUser(String packageName, int flags, UserHandle user) throws PackageManager.NameNotFoundException {
        if (packageName.equals(StorageManager.UUID_SYSTEM) || packageName.equals(ZenModeConfig.SYSTEM_AUTHORITY)) {
            return new ContextImpl(this, this.mMainThread, this.mPackageInfo, null, this.mActivityToken, user, flags, null);
        }
        LoadedApk pi = this.mMainThread.getPackageInfo(packageName, this.mResources.getCompatibilityInfo(), flags | 1073741824, user.getIdentifier());
        if (pi != null) {
            ContextImpl c = new ContextImpl(this, this.mMainThread, pi, null, this.mActivityToken, user, flags, null);
            int displayId = this.mDisplay != null ? this.mDisplay.getDisplayId() : 0;
            c.setResources(createResources(this.mActivityToken, pi, null, displayId, null, getDisplayAdjustments(displayId).getCompatibilityInfo()));
            if (c.mResources != null) {
                return c;
            }
        }
        throw new PackageManager.NameNotFoundException("Application package " + packageName + " not found");
    }

    @Override // android.content.Context
    public Context createContextForSplit(String splitName) throws PackageManager.NameNotFoundException {
        if (!this.mPackageInfo.getApplicationInfo().requestsIsolatedSplitLoading()) {
            return this;
        }
        ClassLoader classLoader = this.mPackageInfo.getSplitClassLoader(splitName);
        String[] paths = this.mPackageInfo.getSplitPaths(splitName);
        ContextImpl context = new ContextImpl(this, this.mMainThread, this.mPackageInfo, splitName, this.mActivityToken, this.mUser, this.mFlags, classLoader);
        int displayId = this.mDisplay != null ? this.mDisplay.getDisplayId() : 0;
        context.setResources(ResourcesManager.getInstance().getResources(this.mActivityToken, this.mPackageInfo.getResDir(), paths, this.mPackageInfo.getOverlayDirs(), this.mPackageInfo.getApplicationInfo().sharedLibraryFiles, displayId, null, this.mPackageInfo.getCompatibilityInfo(), classLoader));
        return context;
    }

    @Override // android.content.Context
    public Context createConfigurationContext(Configuration overrideConfiguration) {
        if (overrideConfiguration == null) {
            throw new IllegalArgumentException("overrideConfiguration must not be null");
        }
        ContextImpl context = new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mSplitName, this.mActivityToken, this.mUser, this.mFlags, this.mClassLoader);
        int displayId = this.mDisplay != null ? this.mDisplay.getDisplayId() : 0;
        context.setResources(createResources(this.mActivityToken, this.mPackageInfo, this.mSplitName, displayId, overrideConfiguration, getDisplayAdjustments(displayId).getCompatibilityInfo()));
        return context;
    }

    @Override // android.content.Context
    public Context createDisplayContext(Display display) {
        if (display == null) {
            throw new IllegalArgumentException("display must not be null");
        }
        ContextImpl context = new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mSplitName, this.mActivityToken, this.mUser, this.mFlags, this.mClassLoader);
        int displayId = display.getDisplayId();
        context.setResources(createResources(this.mActivityToken, this.mPackageInfo, this.mSplitName, displayId, null, getDisplayAdjustments(displayId).getCompatibilityInfo()));
        context.mDisplay = display;
        return context;
    }

    @Override // android.content.Context
    public Context createDeviceProtectedStorageContext() {
        int flags = (this.mFlags & (-17)) | 8;
        return new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mSplitName, this.mActivityToken, this.mUser, flags, this.mClassLoader);
    }

    @Override // android.content.Context
    public Context createCredentialProtectedStorageContext() {
        int flags = (this.mFlags & (-9)) | 16;
        return new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mSplitName, this.mActivityToken, this.mUser, flags, this.mClassLoader);
    }

    @Override // android.content.Context
    public boolean isRestricted() {
        return (this.mFlags & 4) != 0;
    }

    @Override // android.content.Context
    public boolean isDeviceProtectedStorage() {
        return (this.mFlags & 8) != 0;
    }

    @Override // android.content.Context
    public boolean isCredentialProtectedStorage() {
        return (this.mFlags & 16) != 0;
    }

    @Override // android.content.Context
    public synchronized boolean canLoadUnsafeResources() {
        return getPackageName().equals(getOpPackageName()) || (this.mFlags & 2) != 0;
    }

    private protected Display getDisplay() {
        if (this.mDisplay == null) {
            return this.mResourcesManager.getAdjustedDisplay(0, this.mResources);
        }
        return this.mDisplay;
    }

    @Override // android.content.Context
    public synchronized void updateDisplay(int displayId) {
        this.mDisplay = this.mResourcesManager.getAdjustedDisplay(displayId, this.mResources);
    }

    @Override // android.content.Context
    public synchronized DisplayAdjustments getDisplayAdjustments(int displayId) {
        return this.mResources.getDisplayAdjustments();
    }

    @Override // android.content.Context
    public File getDataDir() {
        File res;
        if (this.mPackageInfo != null) {
            if (isCredentialProtectedStorage()) {
                res = this.mPackageInfo.getCredentialProtectedDataDirFile();
            } else if (isDeviceProtectedStorage()) {
                res = this.mPackageInfo.getDeviceProtectedDataDirFile();
            } else {
                res = this.mPackageInfo.getDataDirFile();
            }
            if (res != null) {
                if (!res.exists() && Process.myUid() == 1000) {
                    Log.i(TAG, "Data directory doesn't exist for package " + getPackageName());
                }
                return res;
            }
            throw new RuntimeException("No data directory found for package " + getPackageName());
        }
        throw new RuntimeException("No package details found for package " + getPackageName());
    }

    @Override // android.content.Context
    public File getDir(String name, int mode) {
        checkMode(mode);
        File file = makeFilename(getDataDir(), "app_" + name);
        if (!file.exists()) {
            file.mkdir();
            setFilePermissionsFromMode(file.getPath(), mode, 505);
        }
        return file;
    }

    @Override // android.content.Context
    public UserHandle getUser() {
        return this.mUser;
    }

    @Override // android.content.Context
    public int getUserId() {
        return this.mUser.getIdentifier();
    }

    @Override // android.content.Context
    public synchronized AutofillManager.AutofillClient getAutofillClient() {
        return this.mAutofillClient;
    }

    @Override // android.content.Context
    public synchronized void setAutofillClient(AutofillManager.AutofillClient client) {
        this.mAutofillClient = client;
    }

    @Override // android.content.Context
    public synchronized boolean isAutofillCompatibilityEnabled() {
        return this.mIsAutofillCompatEnabled;
    }

    @Override // android.content.Context
    public void setAutofillCompatibilityEnabled(boolean autofillCompatEnabled) {
        this.mIsAutofillCompatEnabled = autofillCompatEnabled;
    }

    public private protected static ContextImpl createSystemContext(ActivityThread mainThread) {
        LoadedApk packageInfo = new LoadedApk(mainThread);
        ContextImpl context = new ContextImpl(null, mainThread, packageInfo, null, null, null, 0, null);
        context.setResources(packageInfo.getResources());
        context.mResources.updateConfiguration(context.mResourcesManager.getConfiguration(), context.mResourcesManager.getDisplayMetrics());
        return context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized ContextImpl createSystemUiContext(ContextImpl systemContext) {
        LoadedApk packageInfo = systemContext.mPackageInfo;
        ContextImpl context = new ContextImpl(null, systemContext.mMainThread, packageInfo, null, null, null, 0, null);
        context.setResources(createResources(null, packageInfo, null, 0, null, packageInfo.getCompatibilityInfo()));
        return context;
    }

    public private protected static ContextImpl createAppContext(ActivityThread mainThread, LoadedApk packageInfo) {
        if (packageInfo == null) {
            throw new IllegalArgumentException("packageInfo");
        }
        ContextImpl context = new ContextImpl(null, mainThread, packageInfo, null, null, null, 0, null);
        context.setResources(packageInfo.getResources());
        return context;
    }

    public private protected static ContextImpl createActivityContext(ActivityThread mainThread, LoadedApk packageInfo, ActivityInfo activityInfo, IBinder activityToken, int displayId, Configuration overrideConfiguration) {
        if (packageInfo != null) {
            String[] splitDirs = packageInfo.getSplitResDirs();
            ClassLoader classLoader = packageInfo.getClassLoader();
            if (packageInfo.getApplicationInfo().requestsIsolatedSplitLoading()) {
                Trace.traceBegin(8192L, "SplitDependencies");
                try {
                    try {
                        classLoader = packageInfo.getSplitClassLoader(activityInfo.splitName);
                        splitDirs = packageInfo.getSplitPaths(activityInfo.splitName);
                    } catch (PackageManager.NameNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    Trace.traceEnd(8192L);
                }
            }
            String[] splitDirs2 = splitDirs;
            ClassLoader classLoader2 = classLoader;
            ContextImpl context = new ContextImpl(null, mainThread, packageInfo, activityInfo.splitName, activityToken, null, 0, classLoader2);
            int displayId2 = displayId != -1 ? displayId : 0;
            CompatibilityInfo compatInfo = displayId2 == 0 ? packageInfo.getCompatibilityInfo() : CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
            ResourcesManager resourcesManager = ResourcesManager.getInstance();
            context.setResources(resourcesManager.createBaseActivityResources(activityToken, packageInfo.getResDir(), splitDirs2, packageInfo.getOverlayDirs(), packageInfo.getApplicationInfo().sharedLibraryFiles, displayId2, overrideConfiguration, compatInfo, classLoader2));
            context.mDisplay = resourcesManager.getAdjustedDisplay(displayId2, context.getResources());
            return context;
        }
        throw new IllegalArgumentException("packageInfo");
    }

    private synchronized ContextImpl(ContextImpl container, ActivityThread mainThread, LoadedApk packageInfo, String splitName, IBinder activityToken, UserHandle user, int flags, ClassLoader classLoader) {
        this.mSplitName = null;
        if ((flags & 24) == 0) {
            File dataDir = packageInfo.getDataDirFile();
            if (Objects.equals(dataDir, packageInfo.getCredentialProtectedDataDirFile())) {
                flags |= 16;
            } else if (Objects.equals(dataDir, packageInfo.getDeviceProtectedDataDirFile())) {
                flags |= 8;
            }
        }
        this.mMainThread = mainThread;
        this.mActivityToken = activityToken;
        this.mFlags = flags;
        this.mUser = user == null ? Process.myUserHandle() : user;
        this.mPackageInfo = packageInfo;
        this.mSplitName = splitName;
        this.mClassLoader = classLoader;
        this.mResourcesManager = ResourcesManager.getInstance();
        if (container != null) {
            this.mBasePackageName = container.mBasePackageName;
            this.mOpPackageName = container.mOpPackageName;
            setResources(container.mResources);
            this.mDisplay = container.mDisplay;
        } else {
            this.mBasePackageName = packageInfo.mPackageName;
            ApplicationInfo ainfo = packageInfo.getApplicationInfo();
            if (ainfo.uid == 1000 && ainfo.uid != Process.myUid()) {
                this.mOpPackageName = ActivityThread.currentPackageName();
            } else {
                this.mOpPackageName = this.mBasePackageName;
            }
        }
        this.mContentResolver = new ApplicationContentResolver(this, mainThread);
    }

    synchronized void setResources(Resources r) {
        if (r instanceof CompatResources) {
            ((CompatResources) r).setContext(this);
        }
        this.mResources = r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        this.mPackageInfo.installSystemApplicationInfo(info, classLoader);
    }

    public private protected final void scheduleFinalCleanup(String who, String what) {
        this.mMainThread.scheduleContextCleanup(this, who, what);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void performFinalCleanup(String who, String what) {
        this.mPackageInfo.removeContextRegistrations(getOuterContext(), who, what);
    }

    public private protected final Context getReceiverRestrictedContext() {
        if (this.mReceiverRestrictedContext != null) {
            return this.mReceiverRestrictedContext;
        }
        ReceiverRestrictedContext receiverRestrictedContext = new ReceiverRestrictedContext(getOuterContext());
        this.mReceiverRestrictedContext = receiverRestrictedContext;
        return receiverRestrictedContext;
    }

    public private protected final void setOuterContext(Context context) {
        this.mOuterContext = context;
    }

    public private protected final Context getOuterContext() {
        return this.mOuterContext;
    }

    private protected IBinder getActivityToken() {
        return this.mActivityToken;
    }

    private synchronized void checkMode(int mode) {
        if (getApplicationInfo().targetSdkVersion >= 24) {
            if ((mode & 1) != 0) {
                throw new SecurityException("MODE_WORLD_READABLE no longer supported");
            }
            if ((mode & 2) != 0) {
                throw new SecurityException("MODE_WORLD_WRITEABLE no longer supported");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void setFilePermissionsFromMode(String name, int mode, int extraPermissions) {
        int perms = 432 | extraPermissions;
        if ((mode & 1) != 0) {
            perms |= 4;
        }
        if ((mode & 2) != 0) {
            perms |= 2;
        }
        FileUtils.setPermissions(name, perms, -1, -1);
    }

    private synchronized File makeFilename(File base, String name) {
        if (name.indexOf(File.separatorChar) < 0) {
            return new File(base, name);
        }
        throw new IllegalArgumentException("File " + name + " contains a path separator");
    }

    private synchronized File[] ensureExternalDirsExistOrFilter(File[] dirs) {
        StorageManager sm = (StorageManager) getSystemService(StorageManager.class);
        File[] result = new File[dirs.length];
        for (int i = 0; i < dirs.length; i++) {
            File dir = dirs[i];
            if (!dir.exists() && !dir.mkdirs() && !dir.exists()) {
                try {
                    sm.mkdirs(dir);
                } catch (Exception e) {
                    Log.w(TAG, "Failed to ensure " + dir + ": " + e);
                    dir = null;
                }
            }
            result[i] = dir;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ApplicationContentResolver extends ContentResolver {
        public protected final ActivityThread mMainThread;

        public synchronized ApplicationContentResolver(Context context, ActivityThread mainThread) {
            super(context);
            this.mMainThread = (ActivityThread) Preconditions.checkNotNull(mainThread);
        }

        public private IContentProvider acquireProvider(Context context, String auth) {
            return this.mMainThread.acquireProvider(context, ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), true);
        }

        protected synchronized IContentProvider acquireExistingProvider(Context context, String auth) {
            return this.mMainThread.acquireExistingProvider(context, ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), true);
        }

        public synchronized boolean releaseProvider(IContentProvider provider) {
            return this.mMainThread.releaseProvider(provider, true);
        }

        protected synchronized IContentProvider acquireUnstableProvider(Context c, String auth) {
            return this.mMainThread.acquireProvider(c, ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), false);
        }

        public synchronized boolean releaseUnstableProvider(IContentProvider icp) {
            return this.mMainThread.releaseProvider(icp, false);
        }

        public synchronized void unstableProviderDied(IContentProvider icp) {
            this.mMainThread.handleUnstableProviderDied(icp.asBinder(), true);
        }

        @Override // android.content.ContentResolver
        public synchronized void appNotRespondingViaProvider(IContentProvider icp) {
            this.mMainThread.appNotRespondingViaProvider(icp.asBinder());
        }

        protected synchronized int resolveUserIdFromAuthority(String auth) {
            return ContentProvider.getUserIdFromAuthority(auth, getUserId());
        }
    }
}
