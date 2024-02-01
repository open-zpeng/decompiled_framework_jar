package android.content;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.RequiresPermission;
import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.UriGrantsManager;
import android.content.IContentService;
import android.content.ISyncStatusObserver;
import android.content.SyncRequest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.CrossProcessCursorWrapper;
import android.database.Cursor;
import android.database.IContentObserver;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.system.Int32Ref;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import com.android.internal.util.MimeIconUtils;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public abstract class ContentResolver implements ContentInterface {
    public static final String ANY_CURSOR_ITEM_TYPE = "vnd.android.cursor.item/*";
    public static final String CONTENT_SERVICE_NAME = "content";
    public static final String CURSOR_DIR_BASE_TYPE = "vnd.android.cursor.dir";
    public static final String CURSOR_ITEM_BASE_TYPE = "vnd.android.cursor.item";
    public static final String DEPRECATE_DATA_PREFIX = "/mnt/content/";
    private static final boolean ENABLE_CONTENT_SAMPLE = false;
    public static final String EXTRA_HONORED_ARGS = "android.content.extra.HONORED_ARGS";
    public static final String EXTRA_REFRESH_SUPPORTED = "android.content.extra.REFRESH_SUPPORTED";
    public static final String EXTRA_SIZE = "android.content.extra.SIZE";
    public static final String EXTRA_TOTAL_COUNT = "android.content.extra.TOTAL_COUNT";
    public static final String MIME_TYPE_DEFAULT = "application/octet-stream";
    public static final int NOTIFY_SKIP_NOTIFY_FOR_DESCENDANTS = 2;
    public static final int NOTIFY_SYNC_TO_NETWORK = 1;
    public static final String QUERY_ARG_LIMIT = "android:query-arg-limit";
    public static final String QUERY_ARG_OFFSET = "android:query-arg-offset";
    public static final String QUERY_ARG_SORT_COLLATION = "android:query-arg-sort-collation";
    public static final String QUERY_ARG_SORT_COLUMNS = "android:query-arg-sort-columns";
    public static final String QUERY_ARG_SORT_DIRECTION = "android:query-arg-sort-direction";
    public static final String QUERY_ARG_SQL_GROUP_BY = "android:query-arg-sql-group-by";
    public static final String QUERY_ARG_SQL_HAVING = "android:query-arg-sql-having";
    public static final String QUERY_ARG_SQL_LIMIT = "android:query-arg-sql-limit";
    public static final String QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection";
    public static final String QUERY_ARG_SQL_SELECTION_ARGS = "android:query-arg-sql-selection-args";
    public static final String QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sort-order";
    public static final int QUERY_SORT_DIRECTION_ASCENDING = 0;
    public static final int QUERY_SORT_DIRECTION_DESCENDING = 1;
    public static final String SCHEME_ANDROID_RESOURCE = "android.resource";
    public static final String SCHEME_CONTENT = "content";
    public static final String SCHEME_FILE = "file";
    private static final int SLOW_THRESHOLD_MILLIS = 500;
    public static final int SYNC_ERROR_AUTHENTICATION = 2;
    public static final int SYNC_ERROR_CONFLICT = 5;
    public static final int SYNC_ERROR_INTERNAL = 8;
    public static final int SYNC_ERROR_IO = 3;
    public static final int SYNC_ERROR_PARSE = 4;
    @UnsupportedAppUsage
    public static final int SYNC_ERROR_SYNC_ALREADY_IN_PROGRESS = 1;
    public static final int SYNC_ERROR_TOO_MANY_DELETIONS = 6;
    public static final int SYNC_ERROR_TOO_MANY_RETRIES = 7;
    public static final int SYNC_EXEMPTION_NONE = 0;
    public static final int SYNC_EXEMPTION_PROMOTE_BUCKET = 1;
    public static final int SYNC_EXEMPTION_PROMOTE_BUCKET_WITH_TEMP = 2;
    @Deprecated
    public static final String SYNC_EXTRAS_ACCOUNT = "account";
    public static final String SYNC_EXTRAS_DISALLOW_METERED = "allow_metered";
    public static final String SYNC_EXTRAS_DISCARD_LOCAL_DELETIONS = "discard_deletions";
    public static final String SYNC_EXTRAS_DO_NOT_RETRY = "do_not_retry";
    public static final String SYNC_EXTRAS_EXPECTED_DOWNLOAD = "expected_download";
    public static final String SYNC_EXTRAS_EXPECTED_UPLOAD = "expected_upload";
    public static final String SYNC_EXTRAS_EXPEDITED = "expedited";
    @Deprecated
    public static final String SYNC_EXTRAS_FORCE = "force";
    public static final String SYNC_EXTRAS_IGNORE_BACKOFF = "ignore_backoff";
    public static final String SYNC_EXTRAS_IGNORE_SETTINGS = "ignore_settings";
    public static final String SYNC_EXTRAS_INITIALIZE = "initialize";
    public static final String SYNC_EXTRAS_MANUAL = "force";
    public static final String SYNC_EXTRAS_OVERRIDE_TOO_MANY_DELETIONS = "deletions_override";
    public static final String SYNC_EXTRAS_PRIORITY = "sync_priority";
    public static final String SYNC_EXTRAS_REQUIRE_CHARGING = "require_charging";
    public static final String SYNC_EXTRAS_UPLOAD = "upload";
    public static final int SYNC_OBSERVER_TYPE_ACTIVE = 4;
    public static final int SYNC_OBSERVER_TYPE_ALL = Integer.MAX_VALUE;
    public static final int SYNC_OBSERVER_TYPE_PENDING = 2;
    public static final int SYNC_OBSERVER_TYPE_SETTINGS = 1;
    @UnsupportedAppUsage
    public static final int SYNC_OBSERVER_TYPE_STATUS = 8;
    public static final String SYNC_VIRTUAL_EXTRAS_EXEMPTION_FLAG = "v_exemption";
    private static final String TAG = "ContentResolver";
    @UnsupportedAppUsage
    private static volatile IContentService sContentService;
    @UnsupportedAppUsage
    private final Context mContext;
    @UnsupportedAppUsage
    final String mPackageName;
    private final Random mRandom;
    final int mTargetSdkVersion;
    final ContentInterface mWrapped;
    public static final boolean DEPRECATE_DATA_COLUMNS = StorageManager.hasIsolatedStorage();
    public static final Intent ACTION_SYNC_CONN_STATUS_CHANGED = new Intent("com.android.sync.SYNC_CONN_STATUS_CHANGED");
    private static final String[] SYNC_ERROR_NAMES = {"already-in-progress", "authentication-error", "io-error", "parse-error", "conflict", "too-many-deletions", "too-many-retries", "internal-error"};

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface NotifyFlags {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface QueryCollator {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SortDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SyncExemption {
    }

    @UnsupportedAppUsage
    protected abstract IContentProvider acquireProvider(Context context, String str);

    @UnsupportedAppUsage
    protected abstract IContentProvider acquireUnstableProvider(Context context, String str);

    @UnsupportedAppUsage
    public abstract boolean releaseProvider(IContentProvider iContentProvider);

    @UnsupportedAppUsage
    public abstract boolean releaseUnstableProvider(IContentProvider iContentProvider);

    @UnsupportedAppUsage
    public abstract void unstableProviderDied(IContentProvider iContentProvider);

    public static String syncErrorToString(int error) {
        if (error >= 1) {
            String[] strArr = SYNC_ERROR_NAMES;
            if (error <= strArr.length) {
                return strArr[error - 1];
            }
        }
        return String.valueOf(error);
    }

    public static int syncErrorStringToInt(String error) {
        int n = SYNC_ERROR_NAMES.length;
        for (int i = 0; i < n; i++) {
            if (SYNC_ERROR_NAMES[i].equals(error)) {
                return i + 1;
            }
        }
        if (error != null) {
            try {
                return Integer.parseInt(error);
            } catch (NumberFormatException e) {
                Log.d(TAG, "error parsing sync error: " + error);
                return 0;
            }
        }
        return 0;
    }

    public ContentResolver(Context context) {
        this(context, null);
    }

    public ContentResolver(Context context, ContentInterface wrapped) {
        this.mRandom = new Random();
        this.mContext = context != null ? context : ActivityThread.currentApplication();
        this.mPackageName = this.mContext.getOpPackageName();
        this.mTargetSdkVersion = this.mContext.getApplicationInfo().targetSdkVersion;
        this.mWrapped = wrapped;
    }

    public static ContentResolver wrap(ContentInterface wrapped) {
        Preconditions.checkNotNull(wrapped);
        return new ContentResolver(null, wrapped) { // from class: android.content.ContentResolver.1
            @Override // android.content.ContentResolver
            public void unstableProviderDied(IContentProvider icp) {
                throw new UnsupportedOperationException();
            }

            @Override // android.content.ContentResolver
            public boolean releaseUnstableProvider(IContentProvider icp) {
                throw new UnsupportedOperationException();
            }

            @Override // android.content.ContentResolver
            public boolean releaseProvider(IContentProvider icp) {
                throw new UnsupportedOperationException();
            }

            @Override // android.content.ContentResolver
            protected IContentProvider acquireUnstableProvider(Context c, String name) {
                throw new UnsupportedOperationException();
            }

            @Override // android.content.ContentResolver
            protected IContentProvider acquireProvider(Context c, String name) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static ContentResolver wrap(ContentProvider wrapped) {
        return wrap((ContentInterface) wrapped);
    }

    public static ContentResolver wrap(ContentProviderClient wrapped) {
        return wrap((ContentInterface) wrapped);
    }

    @UnsupportedAppUsage
    protected IContentProvider acquireExistingProvider(Context c, String name) {
        return acquireProvider(c, name);
    }

    public void appNotRespondingViaProvider(IContentProvider icp) {
        throw new UnsupportedOperationException("appNotRespondingViaProvider");
    }

    @Override // android.content.ContentInterface
    public final String getType(Uri url) {
        Preconditions.checkNotNull(url, "url");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.getType(url);
            }
            IContentProvider provider = acquireExistingProvider(url);
            try {
                if (provider != null) {
                    return provider.getType(url);
                }
                if ("content".equals(url.getScheme())) {
                    try {
                        String type = ActivityManager.getService().getProviderMimeType(ContentProvider.getUriWithoutUserId(url), resolveUserId(url));
                        return type;
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    } catch (Exception e2) {
                        Log.w(TAG, "Failed to get type for: " + url + " (" + e2.getMessage() + ")");
                        return null;
                    }
                }
                return null;
            } catch (RemoteException e3) {
                return null;
            } catch (Exception e4) {
                Log.w(TAG, "Failed to get type for: " + url + " (" + e4.getMessage() + ")");
                return null;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e5) {
            return null;
        }
    }

    @Override // android.content.ContentInterface
    public String[] getStreamTypes(Uri url, String mimeTypeFilter) {
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(mimeTypeFilter, "mimeTypeFilter");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.getStreamTypes(url, mimeTypeFilter);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                return null;
            }
            try {
                return provider.getStreamTypes(url, mimeTypeFilter);
            } catch (RemoteException e) {
                return null;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e2) {
            return null;
        }
    }

    public final Cursor query(@RequiresPermission.Read Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return query(uri, projection, selection, selectionArgs, sortOrder, null);
    }

    public final Cursor query(@RequiresPermission.Read Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        Bundle queryArgs = createSqlQueryBundle(selection, selectionArgs, sortOrder);
        return query(uri, projection, queryArgs, cancellationSignal);
    }

    @Override // android.content.ContentInterface
    public final Cursor query(@RequiresPermission.Read Uri uri, String[] projection, Bundle queryArgs, CancellationSignal cancellationSignal) {
        ICancellationSignal remoteCancellationSignal;
        Preconditions.checkNotNull(uri, "uri");
        try {
            if (this.mWrapped != null) {
                try {
                    return this.mWrapped.query(uri, projection, queryArgs, cancellationSignal);
                } catch (RemoteException e) {
                    return null;
                }
            }
            IContentProvider unstableProvider = acquireUnstableProvider(uri);
            if (unstableProvider == null) {
                return null;
            }
            IContentProvider stableProvider = null;
            Cursor qCursor = null;
            try {
                long startTime = SystemClock.uptimeMillis();
                if (cancellationSignal != null) {
                    cancellationSignal.throwIfCanceled();
                    ICancellationSignal remoteCancellationSignal2 = unstableProvider.createCancellationSignal();
                    cancellationSignal.setRemote(remoteCancellationSignal2);
                    remoteCancellationSignal = remoteCancellationSignal2;
                } else {
                    remoteCancellationSignal = null;
                }
                try {
                    qCursor = unstableProvider.query(this.mPackageName, uri, projection, queryArgs, remoteCancellationSignal);
                } catch (DeadObjectException e2) {
                    unstableProviderDied(unstableProvider);
                    stableProvider = acquireProvider(uri);
                    if (stableProvider == null) {
                        if (0 != 0) {
                            qCursor.close();
                        }
                        if (cancellationSignal != null) {
                            cancellationSignal.setRemote(null);
                        }
                        releaseUnstableProvider(unstableProvider);
                        if (stableProvider != null) {
                            releaseProvider(stableProvider);
                        }
                        return null;
                    }
                    qCursor = stableProvider.query(this.mPackageName, uri, projection, queryArgs, remoteCancellationSignal);
                }
                if (qCursor == null) {
                    if (qCursor != null) {
                        qCursor.close();
                    }
                    if (cancellationSignal != null) {
                        cancellationSignal.setRemote(null);
                    }
                    releaseUnstableProvider(unstableProvider);
                    if (stableProvider != null) {
                        releaseProvider(stableProvider);
                    }
                    return null;
                }
                qCursor.getCount();
                long durationMillis = SystemClock.uptimeMillis() - startTime;
                maybeLogQueryToEventLog(durationMillis, uri, projection, queryArgs);
                IContentProvider provider = stableProvider != null ? stableProvider : acquireProvider(uri);
                CursorWrapperInner wrapper = new CursorWrapperInner(qCursor, provider);
                Cursor qCursor2 = null;
                if (0 != 0) {
                    qCursor2.close();
                }
                if (cancellationSignal != null) {
                    cancellationSignal.setRemote(null);
                }
                releaseUnstableProvider(unstableProvider);
                if (0 != 0) {
                    releaseProvider(null);
                }
                return wrapper;
            } catch (RemoteException e3) {
                if (qCursor != null) {
                    qCursor.close();
                }
                if (cancellationSignal != null) {
                    cancellationSignal.setRemote(null);
                }
                releaseUnstableProvider(unstableProvider);
                if (stableProvider != null) {
                    releaseProvider(stableProvider);
                }
                return null;
            } catch (Throwable th) {
                if (qCursor != null) {
                    qCursor.close();
                }
                if (cancellationSignal != null) {
                    cancellationSignal.setRemote(null);
                }
                releaseUnstableProvider(unstableProvider);
                if (stableProvider != null) {
                    releaseProvider(stableProvider);
                }
                throw th;
            }
        } catch (RemoteException e4) {
        }
    }

    public final Uri canonicalizeOrElse(Uri uri) {
        Uri res = canonicalize(uri);
        return res != null ? res : uri;
    }

    @Override // android.content.ContentInterface
    public final Uri canonicalize(Uri url) {
        Preconditions.checkNotNull(url, "url");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.canonicalize(url);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                return null;
            }
            try {
                return provider.canonicalize(this.mPackageName, url);
            } catch (RemoteException e) {
                return null;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e2) {
            return null;
        }
    }

    @Override // android.content.ContentInterface
    public final Uri uncanonicalize(Uri url) {
        Preconditions.checkNotNull(url, "url");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.uncanonicalize(url);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                return null;
            }
            try {
                return provider.uncanonicalize(this.mPackageName, url);
            } catch (RemoteException e) {
                return null;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e2) {
            return null;
        }
    }

    @Override // android.content.ContentInterface
    public final boolean refresh(Uri url, Bundle args, CancellationSignal cancellationSignal) {
        Preconditions.checkNotNull(url, "url");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.refresh(url, args, cancellationSignal);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                return false;
            }
            ICancellationSignal remoteCancellationSignal = null;
            if (cancellationSignal != null) {
                try {
                    cancellationSignal.throwIfCanceled();
                    remoteCancellationSignal = provider.createCancellationSignal();
                    cancellationSignal.setRemote(remoteCancellationSignal);
                } catch (RemoteException e) {
                    return false;
                } finally {
                    releaseProvider(provider);
                }
            }
            return provider.refresh(this.mPackageName, url, args, remoteCancellationSignal);
        } catch (RemoteException e2) {
            return false;
        }
    }

    public final InputStream openInputStream(Uri uri) throws FileNotFoundException {
        Preconditions.checkNotNull(uri, "uri");
        String scheme = uri.getScheme();
        if (SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            OpenResourceIdResult r = getResourceId(uri);
            try {
                InputStream stream = r.r.openRawResource(r.id);
                return stream;
            } catch (Resources.NotFoundException e) {
                throw new FileNotFoundException("Resource does not exist: " + uri);
            }
        } else if (SCHEME_FILE.equals(scheme)) {
            return new FileInputStream(uri.getPath());
        } else {
            AssetFileDescriptor fd = openAssetFileDescriptor(uri, "r", null);
            if (fd == null) {
                return null;
            }
            try {
                return fd.createInputStream();
            } catch (IOException e2) {
                throw new FileNotFoundException("Unable to create stream");
            }
        }
    }

    public final OutputStream openOutputStream(Uri uri) throws FileNotFoundException {
        return openOutputStream(uri, "w");
    }

    public final OutputStream openOutputStream(Uri uri, String mode) throws FileNotFoundException {
        AssetFileDescriptor fd = openAssetFileDescriptor(uri, mode, null);
        if (fd == null) {
            return null;
        }
        try {
            return fd.createOutputStream();
        } catch (IOException e) {
            throw new FileNotFoundException("Unable to create stream");
        }
    }

    @Override // android.content.ContentInterface
    public final ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        try {
            return this.mWrapped != null ? this.mWrapped.openFile(uri, mode, signal) : openFileDescriptor(uri, mode, signal);
        } catch (RemoteException e) {
            return null;
        }
    }

    public final ParcelFileDescriptor openFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        return openFileDescriptor(uri, mode, null);
    }

    public final ParcelFileDescriptor openFileDescriptor(Uri uri, String mode, CancellationSignal cancellationSignal) throws FileNotFoundException {
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.openFile(uri, mode, cancellationSignal);
            }
            AssetFileDescriptor afd = openAssetFileDescriptor(uri, mode, cancellationSignal);
            if (afd == null) {
                return null;
            }
            if (afd.getDeclaredLength() < 0) {
                return afd.getParcelFileDescriptor();
            }
            try {
                afd.close();
            } catch (IOException e) {
            }
            throw new FileNotFoundException("Not a whole file");
        } catch (RemoteException e2) {
            return null;
        }
    }

    @Override // android.content.ContentInterface
    public final AssetFileDescriptor openAssetFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        try {
            return this.mWrapped != null ? this.mWrapped.openAssetFile(uri, mode, signal) : openAssetFileDescriptor(uri, mode, signal);
        } catch (RemoteException e) {
            return null;
        }
    }

    public final AssetFileDescriptor openAssetFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        return openAssetFileDescriptor(uri, mode, null);
    }

    public final AssetFileDescriptor openAssetFileDescriptor(Uri uri, String mode, CancellationSignal cancellationSignal) throws FileNotFoundException {
        ICancellationSignal remoteCancellationSignal;
        AssetFileDescriptor fd;
        AssetFileDescriptor fd2;
        Preconditions.checkNotNull(uri, "uri");
        Preconditions.checkNotNull(mode, "mode");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.openAssetFile(uri, mode, cancellationSignal);
            }
            String scheme = uri.getScheme();
            if (SCHEME_ANDROID_RESOURCE.equals(scheme)) {
                if (!"r".equals(mode)) {
                    throw new FileNotFoundException("Can't write resources: " + uri);
                }
                OpenResourceIdResult r = getResourceId(uri);
                try {
                    return r.r.openRawResourceFd(r.id);
                } catch (Resources.NotFoundException e) {
                    throw new FileNotFoundException("Resource does not exist: " + uri);
                }
            } else if (SCHEME_FILE.equals(scheme)) {
                ParcelFileDescriptor pfd = ParcelFileDescriptor.open(new File(uri.getPath()), ParcelFileDescriptor.parseMode(mode));
                return new AssetFileDescriptor(pfd, 0L, -1L);
            } else if ("r".equals(mode)) {
                return openTypedAssetFileDescriptor(uri, "*/*", null, cancellationSignal);
            } else {
                IContentProvider unstableProvider = acquireUnstableProvider(uri);
                if (unstableProvider == null) {
                    throw new FileNotFoundException("No content provider: " + uri);
                }
                IContentProvider stableProvider = null;
                try {
                    if (cancellationSignal != null) {
                        try {
                            cancellationSignal.throwIfCanceled();
                            ICancellationSignal remoteCancellationSignal2 = unstableProvider.createCancellationSignal();
                            cancellationSignal.setRemote(remoteCancellationSignal2);
                            remoteCancellationSignal = remoteCancellationSignal2;
                        } catch (RemoteException e2) {
                            throw new FileNotFoundException("Failed opening content provider: " + uri);
                        } catch (FileNotFoundException e3) {
                            throw e3;
                        }
                    } else {
                        remoteCancellationSignal = null;
                    }
                    try {
                        fd2 = unstableProvider.openAssetFile(this.mPackageName, uri, mode, remoteCancellationSignal);
                    } catch (DeadObjectException e4) {
                        unstableProviderDied(unstableProvider);
                        stableProvider = acquireProvider(uri);
                        if (stableProvider == null) {
                            throw new FileNotFoundException("No content provider: " + uri);
                        }
                        AssetFileDescriptor fd3 = stableProvider.openAssetFile(this.mPackageName, uri, mode, remoteCancellationSignal);
                        if (fd3 == null) {
                            if (cancellationSignal != null) {
                                cancellationSignal.setRemote(null);
                            }
                            releaseProvider(stableProvider);
                            releaseUnstableProvider(unstableProvider);
                            return null;
                        }
                        fd = fd3;
                    }
                    if (fd2 == null) {
                        if (cancellationSignal != null) {
                            cancellationSignal.setRemote(null);
                        }
                        if (0 != 0) {
                            releaseProvider(null);
                        }
                        releaseUnstableProvider(unstableProvider);
                        return null;
                    }
                    fd = fd2;
                    if (stableProvider == null) {
                        stableProvider = acquireProvider(uri);
                    }
                    releaseUnstableProvider(unstableProvider);
                    unstableProvider = null;
                    ParcelFileDescriptor pfd2 = new ParcelFileDescriptorInner(fd.getParcelFileDescriptor(), stableProvider);
                    stableProvider = null;
                    return new AssetFileDescriptor(pfd2, fd.getStartOffset(), fd.getDeclaredLength());
                } finally {
                    if (cancellationSignal != null) {
                        cancellationSignal.setRemote(null);
                    }
                    if (0 != 0) {
                        releaseProvider(null);
                    }
                    if (unstableProvider != null) {
                        releaseUnstableProvider(unstableProvider);
                    }
                }
            }
        } catch (RemoteException e5) {
            return null;
        }
    }

    @Override // android.content.ContentInterface
    public final AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts, CancellationSignal signal) throws FileNotFoundException {
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.openTypedAssetFile(uri, mimeTypeFilter, opts, signal);
            }
            return openTypedAssetFileDescriptor(uri, mimeTypeFilter, opts, signal);
        } catch (RemoteException e) {
            return null;
        }
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts) throws FileNotFoundException {
        return openTypedAssetFileDescriptor(uri, mimeType, opts, null);
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts, CancellationSignal cancellationSignal) throws FileNotFoundException {
        ICancellationSignal remoteCancellationSignal;
        AssetFileDescriptor fd;
        AssetFileDescriptor fd2;
        Preconditions.checkNotNull(uri, "uri");
        Preconditions.checkNotNull(mimeType, "mimeType");
        try {
            if (this.mWrapped != null) {
                try {
                    return this.mWrapped.openTypedAssetFile(uri, mimeType, opts, cancellationSignal);
                } catch (RemoteException e) {
                    return null;
                }
            }
            IContentProvider unstableProvider = acquireUnstableProvider(uri);
            if (unstableProvider == null) {
                throw new FileNotFoundException("No content provider: " + uri);
            }
            IContentProvider stableProvider = null;
            try {
                if (cancellationSignal != null) {
                    try {
                        cancellationSignal.throwIfCanceled();
                        ICancellationSignal remoteCancellationSignal2 = unstableProvider.createCancellationSignal();
                        cancellationSignal.setRemote(remoteCancellationSignal2);
                        remoteCancellationSignal = remoteCancellationSignal2;
                    } catch (RemoteException e2) {
                        throw new FileNotFoundException("Failed opening content provider: " + uri);
                    } catch (FileNotFoundException e3) {
                        throw e3;
                    }
                } else {
                    remoteCancellationSignal = null;
                }
                try {
                    fd2 = unstableProvider.openTypedAssetFile(this.mPackageName, uri, mimeType, opts, remoteCancellationSignal);
                } catch (DeadObjectException e4) {
                    unstableProviderDied(unstableProvider);
                    stableProvider = acquireProvider(uri);
                    if (stableProvider == null) {
                        throw new FileNotFoundException("No content provider: " + uri);
                    }
                    AssetFileDescriptor fd3 = stableProvider.openTypedAssetFile(this.mPackageName, uri, mimeType, opts, remoteCancellationSignal);
                    if (fd3 == null) {
                        if (cancellationSignal != null) {
                            cancellationSignal.setRemote(null);
                        }
                        releaseProvider(stableProvider);
                        releaseUnstableProvider(unstableProvider);
                        return null;
                    }
                    fd = fd3;
                }
                if (fd2 == null) {
                    if (cancellationSignal != null) {
                        cancellationSignal.setRemote(null);
                    }
                    if (0 != 0) {
                        releaseProvider(null);
                    }
                    releaseUnstableProvider(unstableProvider);
                    return null;
                }
                fd = fd2;
                if (stableProvider == null) {
                    stableProvider = acquireProvider(uri);
                }
                releaseUnstableProvider(unstableProvider);
                unstableProvider = null;
                ParcelFileDescriptor pfd = new ParcelFileDescriptorInner(fd.getParcelFileDescriptor(), stableProvider);
                stableProvider = null;
                return new AssetFileDescriptor(pfd, fd.getStartOffset(), fd.getDeclaredLength());
            } finally {
                if (cancellationSignal != null) {
                    cancellationSignal.setRemote(null);
                }
                if (0 != 0) {
                    releaseProvider(null);
                }
                if (unstableProvider != null) {
                    releaseUnstableProvider(unstableProvider);
                }
            }
        } catch (RemoteException e5) {
        }
    }

    /* loaded from: classes.dex */
    public class OpenResourceIdResult {
        @UnsupportedAppUsage
        public int id;
        @UnsupportedAppUsage
        public Resources r;

        public OpenResourceIdResult() {
        }
    }

    @UnsupportedAppUsage
    public OpenResourceIdResult getResourceId(Uri uri) throws FileNotFoundException {
        int id;
        String authority = uri.getAuthority();
        if (TextUtils.isEmpty(authority)) {
            throw new FileNotFoundException("No authority: " + uri);
        }
        try {
            Resources r = this.mContext.getPackageManager().getResourcesForApplication(authority);
            List<String> path = uri.getPathSegments();
            if (path == null) {
                throw new FileNotFoundException("No path: " + uri);
            }
            int len = path.size();
            if (len == 1) {
                try {
                    id = Integer.parseInt(path.get(0));
                } catch (NumberFormatException e) {
                    throw new FileNotFoundException("Single path segment is not a resource ID: " + uri);
                }
            } else if (len == 2) {
                id = r.getIdentifier(path.get(1), path.get(0), authority);
            } else {
                throw new FileNotFoundException("More than two path segments: " + uri);
            }
            if (id == 0) {
                throw new FileNotFoundException("No resource found for: " + uri);
            }
            OpenResourceIdResult res = new OpenResourceIdResult();
            res.r = r;
            res.id = id;
            return res;
        } catch (PackageManager.NameNotFoundException e2) {
            throw new FileNotFoundException("No package found for authority: " + uri);
        }
    }

    @Override // android.content.ContentInterface
    public final Uri insert(@RequiresPermission.Write Uri url, ContentValues values) {
        Preconditions.checkNotNull(url, "url");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.insert(url, values);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                throw new IllegalArgumentException("Unknown URL " + url);
            }
            try {
                long startTime = SystemClock.uptimeMillis();
                Uri createdRow = provider.insert(this.mPackageName, url, values);
                long durationMillis = SystemClock.uptimeMillis() - startTime;
                maybeLogUpdateToEventLog(durationMillis, url, "insert", null);
                return createdRow;
            } catch (RemoteException e) {
                return null;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e2) {
            return null;
        }
    }

    @Override // android.content.ContentInterface
    public ContentProviderResult[] applyBatch(String authority, ArrayList<ContentProviderOperation> operations) throws RemoteException, OperationApplicationException {
        Preconditions.checkNotNull(authority, ContactsContract.Directory.DIRECTORY_AUTHORITY);
        Preconditions.checkNotNull(operations, "operations");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.applyBatch(authority, operations);
            }
            ContentProviderClient provider = acquireContentProviderClient(authority);
            if (provider == null) {
                throw new IllegalArgumentException("Unknown authority " + authority);
            }
            try {
                return provider.applyBatch(operations);
            } finally {
                provider.release();
            }
        } catch (RemoteException e) {
            return null;
        }
    }

    @Override // android.content.ContentInterface
    public final int bulkInsert(@RequiresPermission.Write Uri url, ContentValues[] values) {
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(values, "values");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.bulkInsert(url, values);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                throw new IllegalArgumentException("Unknown URL " + url);
            }
            try {
                long startTime = SystemClock.uptimeMillis();
                int rowsCreated = provider.bulkInsert(this.mPackageName, url, values);
                long durationMillis = SystemClock.uptimeMillis() - startTime;
                maybeLogUpdateToEventLog(durationMillis, url, "bulkinsert", null);
                return rowsCreated;
            } catch (RemoteException e) {
                return 0;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e2) {
            return 0;
        }
    }

    @Override // android.content.ContentInterface
    public final int delete(@RequiresPermission.Write Uri url, String where, String[] selectionArgs) {
        Preconditions.checkNotNull(url, "url");
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.delete(url, where, selectionArgs);
            }
            IContentProvider provider = acquireProvider(url);
            if (provider == null) {
                throw new IllegalArgumentException("Unknown URL " + url);
            }
            try {
                long startTime = SystemClock.uptimeMillis();
                int rowsDeleted = provider.delete(this.mPackageName, url, where, selectionArgs);
                long durationMillis = SystemClock.uptimeMillis() - startTime;
                maybeLogUpdateToEventLog(durationMillis, url, "delete", where);
                return rowsDeleted;
            } catch (RemoteException e) {
                return -1;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e2) {
            return 0;
        }
    }

    @Override // android.content.ContentInterface
    public final int update(@RequiresPermission.Write Uri uri, ContentValues values, String where, String[] selectionArgs) {
        Preconditions.checkNotNull(uri, "uri");
        try {
            if (this.mWrapped != null) {
                try {
                    return this.mWrapped.update(uri, values, where, selectionArgs);
                } catch (RemoteException e) {
                    return 0;
                }
            }
            IContentProvider provider = acquireProvider(uri);
            if (provider == null) {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
            try {
                long startTime = SystemClock.uptimeMillis();
                int rowsUpdated = provider.update(this.mPackageName, uri, values, where, selectionArgs);
                long durationMillis = SystemClock.uptimeMillis() - startTime;
                maybeLogUpdateToEventLog(durationMillis, uri, AccountManager.USER_DATA_EXTRA_UPDATE, where);
                return rowsUpdated;
            } catch (RemoteException e2) {
                return -1;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e3) {
            return 0;
        }
    }

    public final Bundle call(Uri uri, String method, String arg, Bundle extras) {
        return call(uri.getAuthority(), method, arg, extras);
    }

    @Override // android.content.ContentInterface
    public final Bundle call(String authority, String method, String arg, Bundle extras) {
        Preconditions.checkNotNull(authority, ContactsContract.Directory.DIRECTORY_AUTHORITY);
        Preconditions.checkNotNull(method, CalendarContract.RemindersColumns.METHOD);
        try {
            if (this.mWrapped != null) {
                return this.mWrapped.call(authority, method, arg, extras);
            }
            IContentProvider provider = acquireProvider(authority);
            if (provider == null) {
                throw new IllegalArgumentException("Unknown authority " + authority);
            }
            try {
                Bundle res = provider.call(this.mPackageName, authority, method, arg, extras);
                Bundle.setDefusable(res, true);
                return res;
            } catch (RemoteException e) {
                return null;
            } finally {
                releaseProvider(provider);
            }
        } catch (RemoteException e2) {
            return null;
        }
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireProvider(Uri uri) {
        String auth;
        if ("content".equals(uri.getScheme()) && (auth = uri.getAuthority()) != null) {
            return acquireProvider(this.mContext, auth);
        }
        return null;
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireExistingProvider(Uri uri) {
        String auth;
        if ("content".equals(uri.getScheme()) && (auth = uri.getAuthority()) != null) {
            return acquireExistingProvider(this.mContext, auth);
        }
        return null;
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireProvider(String name) {
        if (name == null) {
            return null;
        }
        return acquireProvider(this.mContext, name);
    }

    public final IContentProvider acquireUnstableProvider(Uri uri) {
        if ("content".equals(uri.getScheme())) {
            String auth = uri.getAuthority();
            if (auth != null) {
                return acquireUnstableProvider(this.mContext, uri.getAuthority());
            }
            return null;
        }
        return null;
    }

    @UnsupportedAppUsage
    public final IContentProvider acquireUnstableProvider(String name) {
        if (name == null) {
            return null;
        }
        return acquireUnstableProvider(this.mContext, name);
    }

    public final ContentProviderClient acquireContentProviderClient(Uri uri) {
        Preconditions.checkNotNull(uri, "uri");
        IContentProvider provider = acquireProvider(uri);
        if (provider != null) {
            return new ContentProviderClient(this, provider, uri.getAuthority(), true);
        }
        return null;
    }

    public final ContentProviderClient acquireContentProviderClient(String name) {
        Preconditions.checkNotNull(name, "name");
        IContentProvider provider = acquireProvider(name);
        if (provider != null) {
            return new ContentProviderClient(this, provider, name, true);
        }
        return null;
    }

    public final ContentProviderClient acquireUnstableContentProviderClient(Uri uri) {
        Preconditions.checkNotNull(uri, "uri");
        IContentProvider provider = acquireUnstableProvider(uri);
        if (provider != null) {
            return new ContentProviderClient(this, provider, uri.getAuthority(), false);
        }
        return null;
    }

    public final ContentProviderClient acquireUnstableContentProviderClient(String name) {
        Preconditions.checkNotNull(name, "name");
        IContentProvider provider = acquireUnstableProvider(name);
        if (provider != null) {
            return new ContentProviderClient(this, provider, name, false);
        }
        return null;
    }

    public final void registerContentObserver(Uri uri, boolean notifyForDescendants, ContentObserver observer) {
        Preconditions.checkNotNull(uri, "uri");
        Preconditions.checkNotNull(observer, "observer");
        registerContentObserver(ContentProvider.getUriWithoutUserId(uri), notifyForDescendants, observer, ContentProvider.getUserIdFromUri(uri, this.mContext.getUserId()));
    }

    @UnsupportedAppUsage
    public final void registerContentObserver(Uri uri, boolean notifyForDescendents, ContentObserver observer, int userHandle) {
        try {
            getContentService().registerContentObserver(uri, notifyForDescendents, observer.getContentObserver(), userHandle, this.mTargetSdkVersion);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public final void unregisterContentObserver(ContentObserver observer) {
        Preconditions.checkNotNull(observer, "observer");
        try {
            IContentObserver contentObserver = observer.releaseContentObserver();
            if (contentObserver != null) {
                getContentService().unregisterContentObserver(contentObserver);
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void notifyChange(Uri uri, ContentObserver observer) {
        notifyChange(uri, observer, true);
    }

    public void notifyChange(Uri uri, ContentObserver observer, boolean syncToNetwork) {
        Preconditions.checkNotNull(uri, "uri");
        notifyChange(ContentProvider.getUriWithoutUserId(uri), observer, syncToNetwork, ContentProvider.getUserIdFromUri(uri, this.mContext.getUserId()));
    }

    public void notifyChange(Uri uri, ContentObserver observer, int flags) {
        Preconditions.checkNotNull(uri, "uri");
        notifyChange(ContentProvider.getUriWithoutUserId(uri), observer, flags, ContentProvider.getUserIdFromUri(uri, this.mContext.getUserId()));
    }

    public void notifyChange(Uri uri, ContentObserver observer, boolean syncToNetwork, int userHandle) {
        try {
            getContentService().notifyChange(uri, observer == null ? null : observer.getContentObserver(), observer != null && observer.deliverSelfNotifications(), syncToNetwork ? 1 : 0, userHandle, this.mTargetSdkVersion, this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void notifyChange(Uri uri, ContentObserver observer, int flags, int userHandle) {
        try {
            getContentService().notifyChange(uri, observer == null ? null : observer.getContentObserver(), observer != null && observer.deliverSelfNotifications(), flags, userHandle, this.mTargetSdkVersion, this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void takePersistableUriPermission(Uri uri, int modeFlags) {
        Preconditions.checkNotNull(uri, "uri");
        try {
            UriGrantsManager.getService().takePersistableUriPermission(ContentProvider.getUriWithoutUserId(uri), modeFlags, null, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void takePersistableUriPermission(String toPackage, Uri uri, int modeFlags) {
        Preconditions.checkNotNull(toPackage, "toPackage");
        Preconditions.checkNotNull(uri, "uri");
        try {
            UriGrantsManager.getService().takePersistableUriPermission(ContentProvider.getUriWithoutUserId(uri), modeFlags, toPackage, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void releasePersistableUriPermission(Uri uri, int modeFlags) {
        Preconditions.checkNotNull(uri, "uri");
        try {
            UriGrantsManager.getService().releasePersistableUriPermission(ContentProvider.getUriWithoutUserId(uri), modeFlags, null, resolveUserId(uri));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<UriPermission> getPersistedUriPermissions() {
        try {
            return UriGrantsManager.getService().getUriPermissions(this.mPackageName, true, true).getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<UriPermission> getOutgoingPersistedUriPermissions() {
        try {
            return UriGrantsManager.getService().getUriPermissions(this.mPackageName, false, true).getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<UriPermission> getOutgoingUriPermissions() {
        try {
            return UriGrantsManager.getService().getUriPermissions(this.mPackageName, false, false).getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void startSync(Uri uri, Bundle extras) {
        Account account = null;
        if (extras != null) {
            String accountName = extras.getString("account");
            if (!TextUtils.isEmpty(accountName)) {
                account = new Account(accountName, "com.google");
            }
            extras.remove("account");
        }
        requestSync(account, uri != null ? uri.getAuthority() : null, extras);
    }

    public static void requestSync(Account account, String authority, Bundle extras) {
        requestSyncAsUser(account, authority, UserHandle.myUserId(), extras);
    }

    public static void requestSyncAsUser(Account account, String authority, int userId, Bundle extras) {
        if (extras == null) {
            throw new IllegalArgumentException("Must specify extras.");
        }
        SyncRequest request = new SyncRequest.Builder().setSyncAdapter(account, authority).setExtras(extras).syncOnce().build();
        try {
            getContentService().syncAsUser(request, userId, ActivityThread.currentPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void requestSync(SyncRequest request) {
        try {
            getContentService().sync(request, ActivityThread.currentPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void validateSyncExtrasBundle(Bundle extras) {
        try {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                if (value != null && !(value instanceof Long) && !(value instanceof Integer) && !(value instanceof Boolean) && !(value instanceof Float) && !(value instanceof Double) && !(value instanceof String) && !(value instanceof Account)) {
                    throw new IllegalArgumentException("unexpected value type: " + value.getClass().getName());
                }
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException exc) {
            throw new IllegalArgumentException("error unparceling Bundle", exc);
        }
    }

    @Deprecated
    public void cancelSync(Uri uri) {
        cancelSync(null, uri != null ? uri.getAuthority() : null);
    }

    public static void cancelSync(Account account, String authority) {
        try {
            getContentService().cancelSync(account, authority, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void cancelSyncAsUser(Account account, String authority, int userId) {
        try {
            getContentService().cancelSyncAsUser(account, authority, null, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static SyncAdapterType[] getSyncAdapterTypes() {
        try {
            return getContentService().getSyncAdapterTypes();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static SyncAdapterType[] getSyncAdapterTypesAsUser(int userId) {
        try {
            return getContentService().getSyncAdapterTypesAsUser(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static String[] getSyncAdapterPackagesForAuthorityAsUser(String authority, int userId) {
        try {
            return getContentService().getSyncAdapterPackagesForAuthorityAsUser(authority, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean getSyncAutomatically(Account account, String authority) {
        try {
            return getContentService().getSyncAutomatically(account, authority);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean getSyncAutomaticallyAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().getSyncAutomaticallyAsUser(account, authority, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void setSyncAutomatically(Account account, String authority, boolean sync) {
        setSyncAutomaticallyAsUser(account, authority, sync, UserHandle.myUserId());
    }

    public static void setSyncAutomaticallyAsUser(Account account, String authority, boolean sync, int userId) {
        try {
            getContentService().setSyncAutomaticallyAsUser(account, authority, sync, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void addPeriodicSync(Account account, String authority, Bundle extras, long pollFrequency) {
        validateSyncExtrasBundle(extras);
        if (invalidPeriodicExtras(extras)) {
            throw new IllegalArgumentException("illegal extras were set");
        }
        try {
            getContentService().addPeriodicSync(account, authority, extras, pollFrequency);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean invalidPeriodicExtras(Bundle extras) {
        return extras.getBoolean("force", false) || extras.getBoolean(SYNC_EXTRAS_DO_NOT_RETRY, false) || extras.getBoolean(SYNC_EXTRAS_IGNORE_BACKOFF, false) || extras.getBoolean(SYNC_EXTRAS_IGNORE_SETTINGS, false) || extras.getBoolean(SYNC_EXTRAS_INITIALIZE, false) || extras.getBoolean("force", false) || extras.getBoolean(SYNC_EXTRAS_EXPEDITED, false);
    }

    public static void removePeriodicSync(Account account, String authority, Bundle extras) {
        validateSyncExtrasBundle(extras);
        try {
            getContentService().removePeriodicSync(account, authority, extras);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void cancelSync(SyncRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        try {
            getContentService().cancelRequest(request);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static List<PeriodicSync> getPeriodicSyncs(Account account, String authority) {
        try {
            return getContentService().getPeriodicSyncs(account, authority, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int getIsSyncable(Account account, String authority) {
        try {
            return getContentService().getIsSyncable(account, authority);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static int getIsSyncableAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().getIsSyncableAsUser(account, authority, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void setIsSyncable(Account account, String authority, int syncable) {
        try {
            getContentService().setIsSyncable(account, authority, syncable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void setIsSyncableAsUser(Account account, String authority, int syncable, int userId) {
        try {
            getContentService().setIsSyncableAsUser(account, authority, syncable, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean getMasterSyncAutomatically() {
        try {
            return getContentService().getMasterSyncAutomatically();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean getMasterSyncAutomaticallyAsUser(int userId) {
        try {
            return getContentService().getMasterSyncAutomaticallyAsUser(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void setMasterSyncAutomatically(boolean sync) {
        setMasterSyncAutomaticallyAsUser(sync, UserHandle.myUserId());
    }

    public static void setMasterSyncAutomaticallyAsUser(boolean sync, int userId) {
        try {
            getContentService().setMasterSyncAutomaticallyAsUser(sync, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean isSyncActive(Account account, String authority) {
        if (account == null) {
            throw new IllegalArgumentException("account must not be null");
        }
        if (authority == null) {
            throw new IllegalArgumentException("authority must not be null");
        }
        try {
            return getContentService().isSyncActive(account, authority, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public static SyncInfo getCurrentSync() {
        try {
            List<SyncInfo> syncs = getContentService().getCurrentSyncs();
            if (syncs.isEmpty()) {
                return null;
            }
            return syncs.get(0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static List<SyncInfo> getCurrentSyncs() {
        try {
            return getContentService().getCurrentSyncs();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static List<SyncInfo> getCurrentSyncsAsUser(int userId) {
        try {
            return getContentService().getCurrentSyncsAsUser(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public static SyncStatusInfo getSyncStatus(Account account, String authority) {
        try {
            return getContentService().getSyncStatus(account, authority, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public static SyncStatusInfo getSyncStatusAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().getSyncStatusAsUser(account, authority, null, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean isSyncPending(Account account, String authority) {
        return isSyncPendingAsUser(account, authority, UserHandle.myUserId());
    }

    public static boolean isSyncPendingAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().isSyncPendingAsUser(account, authority, null, userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static Object addStatusChangeListener(int mask, final SyncStatusObserver callback) {
        if (callback == null) {
            throw new IllegalArgumentException("you passed in a null callback");
        }
        try {
            ISyncStatusObserver.Stub observer = new ISyncStatusObserver.Stub() { // from class: android.content.ContentResolver.2
                @Override // android.content.ISyncStatusObserver
                public void onStatusChanged(int which) throws RemoteException {
                    SyncStatusObserver.this.onStatusChanged(which);
                }
            };
            getContentService().addStatusChangeListener(mask, observer);
            return observer;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static void removeStatusChangeListener(Object handle) {
        if (handle == null) {
            throw new IllegalArgumentException("you passed in a null handle");
        }
        try {
            getContentService().removeStatusChangeListener((ISyncStatusObserver.Stub) handle);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void putCache(Uri key, Bundle value) {
        try {
            getContentService().putCache(this.mContext.getPackageName(), key, value, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public Bundle getCache(Uri key) {
        try {
            Bundle bundle = getContentService().getCache(this.mContext.getPackageName(), key, this.mContext.getUserId());
            if (bundle != null) {
                bundle.setClassLoader(this.mContext.getClassLoader());
            }
            return bundle;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getTargetSdkVersion() {
        return this.mTargetSdkVersion;
    }

    private int samplePercentForDuration(long durationMillis) {
        if (durationMillis < 500) {
            return ((int) ((100 * durationMillis) / 500)) + 1;
        }
        return 100;
    }

    private void maybeLogQueryToEventLog(long durationMillis, Uri uri, String[] projection, Bundle queryArgs) {
    }

    private void maybeLogUpdateToEventLog(long durationMillis, Uri uri, String operation, String selection) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class CursorWrapperInner extends CrossProcessCursorWrapper {
        private final CloseGuard mCloseGuard;
        private final IContentProvider mContentProvider;
        private final AtomicBoolean mProviderReleased;

        CursorWrapperInner(Cursor cursor, IContentProvider contentProvider) {
            super(cursor);
            this.mProviderReleased = new AtomicBoolean();
            this.mCloseGuard = CloseGuard.get();
            this.mContentProvider = contentProvider;
            this.mCloseGuard.open("close");
        }

        @Override // android.database.CursorWrapper, android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.mCloseGuard.close();
            super.close();
            if (this.mProviderReleased.compareAndSet(false, true)) {
                ContentResolver.this.releaseProvider(this.mContentProvider);
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class ParcelFileDescriptorInner extends ParcelFileDescriptor {
        private final IContentProvider mContentProvider;
        private final AtomicBoolean mProviderReleased;

        ParcelFileDescriptorInner(ParcelFileDescriptor pfd, IContentProvider icp) {
            super(pfd);
            this.mProviderReleased = new AtomicBoolean();
            this.mContentProvider = icp;
        }

        @Override // android.os.ParcelFileDescriptor
        public void releaseResources() {
            if (this.mProviderReleased.compareAndSet(false, true)) {
                ContentResolver.this.releaseProvider(this.mContentProvider);
            }
        }
    }

    @UnsupportedAppUsage
    public static IContentService getContentService() {
        if (sContentService != null) {
            return sContentService;
        }
        IBinder b = ServiceManager.getService("content");
        sContentService = IContentService.Stub.asInterface(b);
        return sContentService;
    }

    @UnsupportedAppUsage
    public String getPackageName() {
        return this.mPackageName;
    }

    public int resolveUserId(Uri uri) {
        return ContentProvider.getUserIdFromUri(uri, this.mContext.getUserId());
    }

    public int getUserId() {
        return this.mContext.getUserId();
    }

    @Deprecated
    public Drawable getTypeDrawable(String mimeType) {
        return getTypeInfo(mimeType).getIcon().loadDrawable(this.mContext);
    }

    public final MimeTypeInfo getTypeInfo(String mimeType) {
        Objects.requireNonNull(mimeType);
        return MimeIconUtils.getTypeInfo(mimeType);
    }

    /* loaded from: classes.dex */
    public static final class MimeTypeInfo {
        private final CharSequence mContentDescription;
        private final Icon mIcon;
        private final CharSequence mLabel;

        public MimeTypeInfo(Icon icon, CharSequence label, CharSequence contentDescription) {
            this.mIcon = (Icon) Objects.requireNonNull(icon);
            this.mLabel = (CharSequence) Objects.requireNonNull(label);
            this.mContentDescription = (CharSequence) Objects.requireNonNull(contentDescription);
        }

        public Icon getIcon() {
            return this.mIcon;
        }

        public CharSequence getLabel() {
            return this.mLabel;
        }

        public CharSequence getContentDescription() {
            return this.mContentDescription;
        }
    }

    public static Bundle createSqlQueryBundle(String selection, String[] selectionArgs, String sortOrder) {
        if (selection == null && selectionArgs == null && sortOrder == null) {
            return null;
        }
        Bundle queryArgs = new Bundle();
        if (selection != null) {
            queryArgs.putString(QUERY_ARG_SQL_SELECTION, selection);
        }
        if (selectionArgs != null) {
            queryArgs.putStringArray(QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);
        }
        if (sortOrder != null) {
            queryArgs.putString(QUERY_ARG_SQL_SORT_ORDER, sortOrder);
        }
        return queryArgs;
    }

    public static String createSqlSortClause(Bundle queryArgs) {
        String[] columns = queryArgs.getStringArray(QUERY_ARG_SORT_COLUMNS);
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("Can't create sort clause without columns.");
        }
        String query = TextUtils.join(", ", columns);
        int collation = queryArgs.getInt(QUERY_ARG_SORT_COLLATION, 3);
        if (collation == 0 || collation == 1) {
            query = query + " COLLATE NOCASE";
        }
        int sortDir = queryArgs.getInt(QUERY_ARG_SORT_DIRECTION, Integer.MIN_VALUE);
        if (sortDir != Integer.MIN_VALUE) {
            if (sortDir != 0) {
                if (sortDir == 1) {
                    return query + " DESC";
                }
                throw new IllegalArgumentException("Unsupported sort direction value. See ContentResolver documentation for details.");
            }
            return query + " ASC";
        }
        return query;
    }

    public Bitmap loadThumbnail(Uri uri, Size size, CancellationSignal signal) throws IOException {
        return loadThumbnail(this, uri, size, signal, 1);
    }

    public static Bitmap loadThumbnail(final ContentInterface content, final Uri uri, final Size size, final CancellationSignal signal, final int allocator) throws IOException {
        Objects.requireNonNull(content);
        Objects.requireNonNull(uri);
        Objects.requireNonNull(size);
        final Bundle opts = new Bundle();
        opts.putParcelable(EXTRA_SIZE, Point.convert(size));
        final Int32Ref orientation = new Int32Ref(0);
        Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(new Callable() { // from class: android.content.-$$Lambda$ContentResolver$7ILY1SWNxC2xhk-fQUG6tAXW9Ik
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return ContentResolver.lambda$loadThumbnail$0(ContentInterface.this, uri, opts, signal, orientation);
            }
        }), new ImageDecoder.OnHeaderDecodedListener() { // from class: android.content.-$$Lambda$ContentResolver$RVw7W0M7r0cGmbYi8rAG5GKxq4M
            @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
            public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                ContentResolver.lambda$loadThumbnail$1(allocator, signal, size, imageDecoder, imageInfo, source);
            }
        });
        if (orientation.value != 0) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix m = new Matrix();
            m.setRotate(orientation.value, width / 2, height / 2);
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, m, false);
        }
        return bitmap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ AssetFileDescriptor lambda$loadThumbnail$0(ContentInterface content, Uri uri, Bundle opts, CancellationSignal signal, Int32Ref orientation) throws Exception {
        AssetFileDescriptor afd = content.openTypedAssetFile(uri, "image/*", opts, signal);
        Bundle extras = afd.getExtras();
        orientation.value = extras != null ? extras.getInt(DocumentsContract.EXTRA_ORIENTATION, 0) : 0;
        return afd;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$loadThumbnail$1(int allocator, CancellationSignal signal, Size size, ImageDecoder decoder, ImageDecoder.ImageInfo info, ImageDecoder.Source source) {
        decoder.setAllocator(allocator);
        if (signal != null) {
            signal.throwIfCanceled();
        }
        int widthSample = info.getSize().getWidth() / size.getWidth();
        int heightSample = info.getSize().getHeight() / size.getHeight();
        int sample = Math.min(widthSample, heightSample);
        if (sample > 1) {
            decoder.setTargetSampleSize(sample);
        }
    }

    public static void onDbCorruption(String tag, String message, Throwable stacktrace) {
        try {
            getContentService().onDbCorruption(tag, message, Log.getStackTraceString(stacktrace));
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public static Uri translateDeprecatedDataPath(String path) {
        String ssp = "//" + path.substring(DEPRECATE_DATA_PREFIX.length());
        return Uri.parse(new Uri.Builder().scheme("content").encodedOpaquePart(ssp).build().toString());
    }

    public static String translateDeprecatedDataPath(Uri uri) {
        return DEPRECATE_DATA_PREFIX + uri.getEncodedSchemeSpecificPart().substring(2);
    }
}
