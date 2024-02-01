package android.media;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.database.Cursor;
import android.media.IAudioService;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.database.SortCursor;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
/* loaded from: classes.dex */
public class RingtoneManager {
    public static final String ACTION_RINGTONE_PICKER = "android.intent.action.RINGTONE_PICKER";
    public static final String EXTRA_RINGTONE_AUDIO_ATTRIBUTES_FLAGS = "android.intent.extra.ringtone.AUDIO_ATTRIBUTES_FLAGS";
    public static final String EXTRA_RINGTONE_DEFAULT_URI = "android.intent.extra.ringtone.DEFAULT_URI";
    public static final String EXTRA_RINGTONE_EXISTING_URI = "android.intent.extra.ringtone.EXISTING_URI";
    @Deprecated
    public static final String EXTRA_RINGTONE_INCLUDE_DRM = "android.intent.extra.ringtone.INCLUDE_DRM";
    public static final String EXTRA_RINGTONE_PICKED_URI = "android.intent.extra.ringtone.PICKED_URI";
    public static final String EXTRA_RINGTONE_SHOW_DEFAULT = "android.intent.extra.ringtone.SHOW_DEFAULT";
    public static final String EXTRA_RINGTONE_SHOW_SILENT = "android.intent.extra.ringtone.SHOW_SILENT";
    public static final String EXTRA_RINGTONE_TITLE = "android.intent.extra.ringtone.TITLE";
    public static final String EXTRA_RINGTONE_TYPE = "android.intent.extra.ringtone.TYPE";
    public static final int ID_COLUMN_INDEX = 0;
    private static final String[] INTERNAL_COLUMNS = {"_id", "title", "\"" + MediaStore.Audio.Media.INTERNAL_CONTENT_URI + "\"", "title_key"};
    private static final String[] MEDIA_COLUMNS = {"_id", "title", "\"" + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "\"", "title_key"};
    private static final String TAG = "RingtoneManager";
    public static final int TITLE_COLUMN_INDEX = 1;
    public static final int TYPE_ALARM = 4;
    public static final int TYPE_ALL = 7;
    public static final int TYPE_NOTIFICATION = 2;
    public static final int TYPE_RINGTONE = 1;
    public static final int URI_COLUMN_INDEX = 2;
    private final Activity mActivity;
    private final Context mContext;
    public protected Cursor mCursor;
    private final List<String> mFilterColumns;
    private boolean mIncludeParentRingtones;
    private Ringtone mPreviousRingtone;
    private boolean mStopPreviousRingtone;
    private int mType;

    public RingtoneManager(Activity activity) {
        this(activity, false);
    }

    public synchronized RingtoneManager(Activity activity, boolean includeParentRingtones) {
        this.mType = 1;
        this.mFilterColumns = new ArrayList();
        this.mStopPreviousRingtone = true;
        this.mActivity = activity;
        this.mContext = activity;
        setType(this.mType);
        this.mIncludeParentRingtones = includeParentRingtones;
    }

    public RingtoneManager(Context context) {
        this(context, false);
    }

    public synchronized RingtoneManager(Context context, boolean includeParentRingtones) {
        this.mType = 1;
        this.mFilterColumns = new ArrayList();
        this.mStopPreviousRingtone = true;
        this.mActivity = null;
        this.mContext = context;
        setType(this.mType);
        this.mIncludeParentRingtones = includeParentRingtones;
    }

    public void setType(int type) {
        if (this.mCursor != null) {
            throw new IllegalStateException("Setting filter columns should be done before querying for ringtones.");
        }
        this.mType = type;
        setFilterColumnsList(type);
    }

    public int inferStreamType() {
        int i = this.mType;
        if (i != 2) {
            return i != 4 ? 2 : 4;
        }
        return 5;
    }

    public void setStopPreviousRingtone(boolean stopPreviousRingtone) {
        this.mStopPreviousRingtone = stopPreviousRingtone;
    }

    public boolean getStopPreviousRingtone() {
        return this.mStopPreviousRingtone;
    }

    public void stopPreviousRingtone() {
        if (this.mPreviousRingtone != null) {
            this.mPreviousRingtone.stop();
        }
    }

    @Deprecated
    public boolean getIncludeDrm() {
        return false;
    }

    @Deprecated
    public void setIncludeDrm(boolean includeDrm) {
        if (includeDrm) {
            Log.w(TAG, "setIncludeDrm no longer supported");
        }
    }

    public Cursor getCursor() {
        Cursor parentRingtonesCursor;
        if (this.mCursor != null && this.mCursor.requery()) {
            return this.mCursor;
        }
        ArrayList<Cursor> ringtoneCursors = new ArrayList<>();
        ringtoneCursors.add(getInternalRingtones());
        ringtoneCursors.add(getMediaRingtones());
        if (this.mIncludeParentRingtones && (parentRingtonesCursor = getParentProfileRingtones()) != null) {
            ringtoneCursors.add(parentRingtonesCursor);
        }
        SortCursor sortCursor = new SortCursor((Cursor[]) ringtoneCursors.toArray(new Cursor[ringtoneCursors.size()]), "title_key");
        this.mCursor = sortCursor;
        return sortCursor;
    }

    private synchronized Cursor getParentProfileRingtones() {
        Context parentContext;
        UserManager um = UserManager.get(this.mContext);
        UserInfo parentInfo = um.getProfileParent(this.mContext.getUserId());
        if (parentInfo != null && parentInfo.id != this.mContext.getUserId() && (parentContext = createPackageContextAsUser(this.mContext, parentInfo.id)) != null) {
            return new ExternalRingtonesCursorWrapper(getMediaRingtones(parentContext), parentInfo.id);
        }
        return null;
    }

    public Ringtone getRingtone(int position) {
        if (this.mStopPreviousRingtone && this.mPreviousRingtone != null) {
            this.mPreviousRingtone.stop();
        }
        this.mPreviousRingtone = getRingtone(this.mContext, getRingtoneUri(position), inferStreamType());
        return this.mPreviousRingtone;
    }

    public Uri getRingtoneUri(int position) {
        if (this.mCursor == null || !this.mCursor.moveToPosition(position)) {
            return null;
        }
        return getUriFromCursor(this.mCursor);
    }

    private static synchronized Uri getExistingRingtoneUriFromPath(Context context, String path) {
        String[] proj = {"_id"};
        String[] selectionArgs = {path};
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, "_data=? ", selectionArgs, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndex("_id"));
                    if (id == -1) {
                        if (cursor != null) {
                            $closeResource(null, cursor);
                        }
                        return null;
                    }
                    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    Uri withAppendedPath = Uri.withAppendedPath(uri, "" + id);
                    if (cursor != null) {
                        $closeResource(null, cursor);
                    }
                    return withAppendedPath;
                }
            } finally {
            }
        }
        if (cursor != null) {
            $closeResource(null, cursor);
        }
        return null;
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    private static synchronized Uri getUriFromCursor(Cursor cursor) {
        return ContentUris.withAppendedId(Uri.parse(cursor.getString(2)), cursor.getLong(0));
    }

    public int getRingtonePosition(Uri ringtoneUri) {
        if (ringtoneUri == null) {
            return -1;
        }
        Cursor cursor = getCursor();
        int cursorCount = cursor.getCount();
        if (!cursor.moveToFirst()) {
            return -1;
        }
        String previousUriString = null;
        Uri currentUri = null;
        for (int i = 0; i < cursorCount; i++) {
            String uriString = cursor.getString(2);
            if (currentUri == null || !uriString.equals(previousUriString)) {
                currentUri = Uri.parse(uriString);
            }
            if (ringtoneUri.equals(ContentUris.withAppendedId(currentUri, cursor.getLong(0)))) {
                return i;
            }
            cursor.move(1);
            previousUriString = uriString;
        }
        return -1;
    }

    public static Uri getValidRingtoneUri(Context context) {
        RingtoneManager rm = new RingtoneManager(context);
        Uri uri = getValidRingtoneUriFromCursorAndClose(context, rm.getInternalRingtones());
        if (uri == null) {
            return getValidRingtoneUriFromCursorAndClose(context, rm.getMediaRingtones());
        }
        return uri;
    }

    private static synchronized Uri getValidRingtoneUriFromCursorAndClose(Context context, Cursor cursor) {
        if (cursor != null) {
            Uri uri = null;
            if (cursor.moveToFirst()) {
                uri = getUriFromCursor(cursor);
            }
            cursor.close();
            return uri;
        }
        return null;
    }

    public protected Cursor getInternalRingtones() {
        return query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, INTERNAL_COLUMNS, constructBooleanTrueWhereClause(this.mFilterColumns), null, "title_key");
    }

    private synchronized Cursor getMediaRingtones() {
        return getMediaRingtones(this.mContext);
    }

    public protected Cursor getMediaRingtones(Context context) {
        if (context.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Process.myPid(), Process.myUid()) != 0) {
            Log.w(TAG, "No READ_EXTERNAL_STORAGE permission, ignoring ringtones on ext storage");
            return null;
        }
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED) || status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MEDIA_COLUMNS, constructBooleanTrueWhereClause(this.mFilterColumns), null, "title_key", context);
        }
        return null;
    }

    private synchronized void setFilterColumnsList(int type) {
        List<String> columns = this.mFilterColumns;
        columns.clear();
        if ((type & 1) != 0) {
            columns.add(MediaStore.Audio.AudioColumns.IS_RINGTONE);
        }
        if ((type & 2) != 0) {
            columns.add(MediaStore.Audio.AudioColumns.IS_NOTIFICATION);
        }
        if ((type & 4) != 0) {
            columns.add(MediaStore.Audio.AudioColumns.IS_ALARM);
        }
    }

    private static synchronized String constructBooleanTrueWhereClause(List<String> columns) {
        if (columns == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = columns.size() - 1; i >= 0; i--) {
            sb.append(columns.get(i));
            sb.append("=1 or ");
        }
        int i2 = columns.size();
        if (i2 > 0) {
            sb.setLength(sb.length() - 4);
        }
        sb.append(")");
        return sb.toString();
    }

    private synchronized Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return query(uri, projection, selection, selectionArgs, sortOrder, this.mContext);
    }

    private synchronized Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, Context context) {
        if (this.mActivity != null) {
            return this.mActivity.managedQuery(uri, projection, selection, selectionArgs, sortOrder);
        }
        return context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    public static Ringtone getRingtone(Context context, Uri ringtoneUri) {
        return getRingtone(context, ringtoneUri, -1);
    }

    public protected static Ringtone getRingtone(Context context, Uri ringtoneUri, int streamType) {
        try {
            Ringtone r = new Ringtone(context, true);
            if (streamType >= 0) {
                r.setStreamType(streamType);
            }
            r.setUri(ringtoneUri);
            return r;
        } catch (Exception ex) {
            Log.e(TAG, "Failed to open ringtone " + ringtoneUri + ": " + ex);
            return null;
        }
    }

    private synchronized File getRingtonePathFromUri(Uri uri) {
        String[] projection = {"_data"};
        setFilterColumnsList(7);
        String path = null;
        Cursor cursor = query(uri, projection, constructBooleanTrueWhereClause(this.mFilterColumns), null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    path = cursor.getString(cursor.getColumnIndex("_data"));
                }
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    if (cursor != null) {
                        $closeResource(th, cursor);
                    }
                    throw th2;
                }
            }
        }
        if (cursor != null) {
            $closeResource(null, cursor);
        }
        if (path != null) {
            return new File(path);
        }
        return null;
    }

    public static synchronized void disableSyncFromParent(Context userContext) {
        IBinder b = ServiceManager.getService("audio");
        IAudioService audioService = IAudioService.Stub.asInterface(b);
        try {
            audioService.disableRingtoneSync(userContext.getUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to disable ringtone sync.");
        }
    }

    public static synchronized void enableSyncFromParent(Context userContext) {
        Settings.Secure.putIntForUser(userContext.getContentResolver(), Settings.Secure.SYNC_PARENT_SOUNDS, 1, userContext.getUserId());
    }

    public static Uri getActualDefaultRingtoneUri(Context context, int type) {
        String setting = getSettingForType(type);
        if (setting == null) {
            return null;
        }
        String uriString = Settings.System.getStringForUser(context.getContentResolver(), setting, context.getUserId());
        Uri ringtoneUri = uriString != null ? Uri.parse(uriString) : null;
        if (ringtoneUri != null && ContentProvider.getUserIdFromUri(ringtoneUri) == context.getUserId()) {
            return ContentProvider.getUriWithoutUserId(ringtoneUri);
        }
        return ringtoneUri;
    }

    public static void setActualDefaultRingtoneUri(Context context, int type, Uri ringtoneUri) {
        String setting = getSettingForType(type);
        if (setting == null) {
            return;
        }
        ContentResolver resolver = context.getContentResolver();
        if (Settings.Secure.getIntForUser(resolver, Settings.Secure.SYNC_PARENT_SOUNDS, 0, context.getUserId()) == 1) {
            disableSyncFromParent(context);
        }
        if (!isInternalRingtoneUri(ringtoneUri)) {
            ringtoneUri = ContentProvider.maybeAddUserId(ringtoneUri, context.getUserId());
        }
        Settings.System.putStringForUser(resolver, setting, ringtoneUri != null ? ringtoneUri.toString() : null, context.getUserId());
        if (ringtoneUri != null) {
            Uri cacheUri = getCacheForType(type, context.getUserId());
            try {
                InputStream in = openRingtone(context, ringtoneUri);
                OutputStream out = resolver.openOutputStream(cacheUri);
                FileUtils.copy(in, out);
                if (out != null) {
                    $closeResource(null, out);
                }
                if (in != null) {
                    $closeResource(null, in);
                }
            } catch (IOException e) {
                Log.w(TAG, "Failed to cache ringtone: " + e);
            }
        }
    }

    private static synchronized boolean isInternalRingtoneUri(Uri uri) {
        return isRingtoneUriInStorage(uri, MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
    }

    private static synchronized boolean isExternalRingtoneUri(Uri uri) {
        return isRingtoneUriInStorage(uri, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
    }

    private static synchronized boolean isRingtoneUriInStorage(Uri ringtone, Uri storage) {
        Uri uriWithoutUserId = ContentProvider.getUriWithoutUserId(ringtone);
        if (uriWithoutUserId == null) {
            return false;
        }
        return uriWithoutUserId.toString().startsWith(storage.toString());
    }

    public synchronized boolean isCustomRingtone(Uri uri) {
        if (isExternalRingtoneUri(uri)) {
            File ringtoneFile = uri == null ? null : getRingtonePathFromUri(uri);
            File parent = ringtoneFile != null ? ringtoneFile.getParentFile() : null;
            if (parent == null) {
                return false;
            }
            String[] directories = {Environment.DIRECTORY_RINGTONES, Environment.DIRECTORY_NOTIFICATIONS, Environment.DIRECTORY_ALARMS};
            for (String directory : directories) {
                if (parent.equals(Environment.getExternalStoragePublicDirectory(directory))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public synchronized Uri addCustomExternalRingtone(Uri fileUri, int type) throws FileNotFoundException, IllegalArgumentException, IOException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new IOException("External storage is not mounted. Unable to install ringtones.");
        }
        String mimeType = this.mContext.getContentResolver().getType(fileUri);
        if (mimeType == null || !(mimeType.startsWith("audio/") || mimeType.equals("application/ogg"))) {
            throw new IllegalArgumentException("Ringtone file must have MIME type \"audio/*\". Given file has MIME type \"" + mimeType + "\"");
        }
        String subdirectory = getExternalDirectoryForType(type);
        File outFile = Utils.getUniqueExternalFile(this.mContext, subdirectory, Utils.getFileDisplayNameFromUri(this.mContext, fileUri), mimeType);
        InputStream input = this.mContext.getContentResolver().openInputStream(fileUri);
        try {
            OutputStream output = new FileOutputStream(outFile);
            FileUtils.copy(input, output);
            $closeResource(null, output);
            if (input != null) {
                $closeResource(null, input);
            }
            try {
                NewRingtoneScanner scanner = new NewRingtoneScanner(outFile);
                Uri take = scanner.take();
                $closeResource(null, scanner);
                return take;
            } catch (InterruptedException e) {
                throw new IOException("Audio file failed to scan as a ringtone", e);
            }
        } finally {
        }
    }

    private static final synchronized String getExternalDirectoryForType(int type) {
        if (type != 4) {
            switch (type) {
                case 1:
                    return Environment.DIRECTORY_RINGTONES;
                case 2:
                    return Environment.DIRECTORY_NOTIFICATIONS;
                default:
                    throw new IllegalArgumentException("Unsupported ringtone type: " + type);
            }
        }
        return Environment.DIRECTORY_ALARMS;
    }

    public synchronized boolean deleteExternalRingtone(Uri uri) {
        File ringtoneFile;
        if (isCustomRingtone(uri) && (ringtoneFile = getRingtonePathFromUri(uri)) != null) {
            try {
                if (this.mContext.getContentResolver().delete(uri, null, null) > 0) {
                    return ringtoneFile.delete();
                }
            } catch (SecurityException e) {
                Log.d(TAG, "Unable to delete custom ringtone", e);
            }
        }
        return false;
    }

    private static synchronized InputStream openRingtone(Context context, Uri uri) throws IOException {
        ContentResolver resolver = context.getContentResolver();
        try {
            return resolver.openInputStream(uri);
        } catch (IOException | SecurityException e) {
            Log.w(TAG, "Failed to open directly; attempting failover: " + e);
            IRingtonePlayer player = ((AudioManager) context.getSystemService(AudioManager.class)).getRingtonePlayer();
            try {
                return new ParcelFileDescriptor.AutoCloseInputStream(player.openRingtone(uri));
            } catch (Exception e2) {
                throw new IOException(e2);
            }
        }
    }

    private static synchronized String getSettingForType(int type) {
        if ((type & 1) != 0) {
            return Settings.System.RINGTONE;
        }
        if ((type & 2) != 0) {
            return Settings.System.NOTIFICATION_SOUND;
        }
        if ((type & 4) != 0) {
            return Settings.System.ALARM_ALERT;
        }
        return null;
    }

    public static synchronized Uri getCacheForType(int type) {
        return getCacheForType(type, UserHandle.getCallingUserId());
    }

    public static synchronized Uri getCacheForType(int type, int userId) {
        if ((type & 1) != 0) {
            return ContentProvider.maybeAddUserId(Settings.System.RINGTONE_CACHE_URI, userId);
        }
        if ((type & 2) != 0) {
            return ContentProvider.maybeAddUserId(Settings.System.NOTIFICATION_SOUND_CACHE_URI, userId);
        }
        if ((type & 4) != 0) {
            return ContentProvider.maybeAddUserId(Settings.System.ALARM_ALERT_CACHE_URI, userId);
        }
        return null;
    }

    public static boolean isDefault(Uri ringtoneUri) {
        return getDefaultType(ringtoneUri) != -1;
    }

    public static int getDefaultType(Uri defaultRingtoneUri) {
        Uri defaultRingtoneUri2 = ContentProvider.getUriWithoutUserId(defaultRingtoneUri);
        if (defaultRingtoneUri2 == null) {
            return -1;
        }
        if (defaultRingtoneUri2.equals(Settings.System.DEFAULT_RINGTONE_URI)) {
            return 1;
        }
        if (defaultRingtoneUri2.equals(Settings.System.DEFAULT_NOTIFICATION_URI)) {
            return 2;
        }
        if (!defaultRingtoneUri2.equals(Settings.System.DEFAULT_ALARM_ALERT_URI)) {
            return -1;
        }
        return 4;
    }

    public static Uri getDefaultUri(int type) {
        if ((type & 1) != 0) {
            return Settings.System.DEFAULT_RINGTONE_URI;
        }
        if ((type & 2) != 0) {
            return Settings.System.DEFAULT_NOTIFICATION_URI;
        }
        if ((type & 4) != 0) {
            return Settings.System.DEFAULT_ALARM_ALERT_URI;
        }
        return null;
    }

    /* loaded from: classes.dex */
    private class NewRingtoneScanner implements Closeable, MediaScannerConnection.MediaScannerConnectionClient {
        private File mFile;
        private MediaScannerConnection mMediaScannerConnection;
        private LinkedBlockingQueue<Uri> mQueue = new LinkedBlockingQueue<>(1);

        public NewRingtoneScanner(File file) {
            this.mFile = file;
            this.mMediaScannerConnection = new MediaScannerConnection(RingtoneManager.this.mContext, this);
            this.mMediaScannerConnection.connect();
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.mMediaScannerConnection.disconnect();
        }

        @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient
        public void onMediaScannerConnected() {
            this.mMediaScannerConnection.scanFile(this.mFile.getAbsolutePath(), null);
        }

        @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient, android.media.MediaScannerConnection.OnScanCompletedListener
        public void onScanCompleted(String path, Uri uri) {
            if (uri == null) {
                this.mFile.delete();
                return;
            }
            try {
                this.mQueue.put(uri);
            } catch (InterruptedException e) {
                Log.e(RingtoneManager.TAG, "Unable to put new ringtone Uri in queue", e);
            }
        }

        public synchronized Uri take() throws InterruptedException {
            return this.mQueue.take();
        }
    }

    private static synchronized Context createPackageContextAsUser(Context context, int userId) {
        try {
            return context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.of(userId));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to create package context", e);
            return null;
        }
    }
}
