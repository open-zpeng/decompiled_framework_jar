package android.provider;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.SyncStateContract;
import android.util.Pair;
/* loaded from: classes2.dex */
public class BrowserContract {
    public static final String AUTHORITY = "com.android.browser";
    private protected static final Uri AUTHORITY_URI = Uri.parse("content://com.android.browser");
    public static final String CALLER_IS_SYNCADAPTER = "caller_is_syncadapter";
    public static final String PARAM_LIMIT = "limit";

    /* loaded from: classes2.dex */
    public static final class Accounts {
        public static final String ACCOUNT_NAME = "account_name";
        public static final String ACCOUNT_TYPE = "account_type";
        private protected static final Uri CONTENT_URI = BrowserContract.AUTHORITY_URI.buildUpon().appendPath(AccountManager.KEY_ACCOUNTS).build();
        public static final String ROOT_ID = "root_id";
    }

    /* loaded from: classes2.dex */
    interface BaseSyncColumns {
        public static final String SYNC1 = "sync1";
        public static final String SYNC2 = "sync2";
        public static final String SYNC3 = "sync3";
        public static final String SYNC4 = "sync4";
        public static final String SYNC5 = "sync5";
    }

    /* loaded from: classes2.dex */
    interface CommonColumns {
        public static final String DATE_CREATED = "created";
        public static final String TITLE = "title";
        public static final String URL = "url";
        public static final String _ID = "_id";
    }

    /* loaded from: classes2.dex */
    interface HistoryColumns {
        public static final String DATE_LAST_VISITED = "date";
        public static final String USER_ENTERED = "user_entered";
        public static final String VISITS = "visits";
    }

    /* loaded from: classes2.dex */
    interface ImageColumns {
        public static final String FAVICON = "favicon";
        public static final String THUMBNAIL = "thumbnail";
        public static final String TOUCH_ICON = "touch_icon";
    }

    /* loaded from: classes2.dex */
    interface ImageMappingColumns {
        public static final String IMAGE_ID = "image_id";
        public static final String URL = "url";
    }

    /* loaded from: classes2.dex */
    interface SyncColumns extends BaseSyncColumns {
        public static final String ACCOUNT_NAME = "account_name";
        public static final String ACCOUNT_TYPE = "account_type";
        public static final String DATE_MODIFIED = "modified";
        public static final String DIRTY = "dirty";
        public static final String SOURCE_ID = "sourceid";
        public static final String VERSION = "version";
    }

    /* loaded from: classes2.dex */
    public static final class ChromeSyncColumns {
        public static final String CLIENT_UNIQUE = "sync4";
        public static final String FOLDER_NAME_BOOKMARKS = "google_chrome_bookmarks";
        public static final String FOLDER_NAME_BOOKMARKS_BAR = "bookmark_bar";
        public static final String FOLDER_NAME_OTHER_BOOKMARKS = "other_bookmarks";
        public static final String FOLDER_NAME_ROOT = "google_chrome";
        public static final String SERVER_UNIQUE = "sync3";

        private synchronized ChromeSyncColumns() {
        }
    }

    /* loaded from: classes2.dex */
    public static final class Bookmarks implements CommonColumns, ImageColumns, SyncColumns {
        public static final int BOOKMARK_TYPE_BOOKMARK = 1;
        public static final int BOOKMARK_TYPE_BOOKMARK_BAR_FOLDER = 3;
        public static final int BOOKMARK_TYPE_FOLDER = 2;
        public static final int BOOKMARK_TYPE_MOBILE_FOLDER = 5;
        public static final int BOOKMARK_TYPE_OTHER_FOLDER = 4;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/bookmark";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/bookmark";
        private protected static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "bookmarks");
        private protected static final Uri CONTENT_URI_DEFAULT_FOLDER = Uri.withAppendedPath(CONTENT_URI, "folder");
        public static final String INSERT_AFTER = "insert_after";
        public static final String INSERT_AFTER_SOURCE_ID = "insert_after_source";
        public static final String IS_DELETED = "deleted";
        public static final String IS_FOLDER = "folder";
        public static final String PARAM_ACCOUNT_NAME = "acct_name";
        public static final String PARAM_ACCOUNT_TYPE = "acct_type";
        public static final String PARENT = "parent";
        public static final String PARENT_SOURCE_ID = "parent_source";
        public static final String POSITION = "position";
        public static final String QUERY_PARAMETER_SHOW_DELETED = "show_deleted";
        public static final String TYPE = "type";

        private synchronized Bookmarks() {
        }

        private protected static final Uri buildFolderUri(long folderId) {
            return ContentUris.withAppendedId(CONTENT_URI_DEFAULT_FOLDER, folderId);
        }
    }

    /* loaded from: classes2.dex */
    public static final class History implements CommonColumns, HistoryColumns, ImageColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/browser-history";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/browser-history";
        private protected static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "history");

        private synchronized History() {
        }
    }

    /* loaded from: classes2.dex */
    public static final class Searches {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/searches";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/searches";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "searches");
        public static final String DATE = "date";
        public static final String SEARCH = "search";
        public static final String _ID = "_id";

        private synchronized Searches() {
        }
    }

    /* loaded from: classes2.dex */
    public static final class SyncState implements SyncStateContract.Columns {
        public static final String CONTENT_DIRECTORY = "syncstate";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "syncstate");

        private synchronized SyncState() {
        }

        public static synchronized byte[] get(ContentProviderClient provider, Account account) throws RemoteException {
            return SyncStateContract.Helpers.get(provider, CONTENT_URI, account);
        }

        public static synchronized Pair<Uri, byte[]> getWithUri(ContentProviderClient provider, Account account) throws RemoteException {
            return SyncStateContract.Helpers.getWithUri(provider, CONTENT_URI, account);
        }

        public static synchronized void set(ContentProviderClient provider, Account account, byte[] data) throws RemoteException {
            SyncStateContract.Helpers.set(provider, CONTENT_URI, account, data);
        }

        public static synchronized ContentProviderOperation newSetOperation(Account account, byte[] data) {
            return SyncStateContract.Helpers.newSetOperation(CONTENT_URI, account, data);
        }
    }

    /* loaded from: classes2.dex */
    public static final class Images implements ImageColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/images";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/images";
        private protected static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "images");
        public static final String DATA = "data";
        public static final int IMAGE_TYPE_FAVICON = 1;
        public static final int IMAGE_TYPE_PRECOMPOSED_TOUCH_ICON = 2;
        public static final int IMAGE_TYPE_TOUCH_ICON = 4;
        public static final String TYPE = "type";
        public static final String URL = "url_key";

        private synchronized Images() {
        }
    }

    /* loaded from: classes2.dex */
    public static final class ImageMappings implements ImageMappingColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/image_mappings";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/image_mappings";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "image_mappings");

        private synchronized ImageMappings() {
        }
    }

    /* loaded from: classes2.dex */
    public static final class Combined implements CommonColumns, HistoryColumns, ImageColumns {
        private protected static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "combined");
        public static final String IS_BOOKMARK = "bookmark";

        private synchronized Combined() {
        }
    }

    /* loaded from: classes2.dex */
    public static final class Settings {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BrowserContract.AUTHORITY_URI, "settings");
        public static final String KEY = "key";
        public static final String KEY_SYNC_ENABLED = "sync_enabled";
        public static final String VALUE = "value";

        private synchronized Settings() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x0038, code lost:
            r0.close();
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public static synchronized boolean isSyncEnabled(android.content.Context r7) {
            /*
                r0 = 0
                android.content.ContentResolver r1 = r7.getContentResolver()     // Catch: java.lang.Throwable -> L3c
                android.net.Uri r2 = android.provider.BrowserContract.Settings.CONTENT_URI     // Catch: java.lang.Throwable -> L3c
                java.lang.String r3 = "value"
                java.lang.String[] r3 = new java.lang.String[]{r3}     // Catch: java.lang.Throwable -> L3c
                java.lang.String r4 = "key=?"
                java.lang.String r5 = "sync_enabled"
                java.lang.String[] r5 = new java.lang.String[]{r5}     // Catch: java.lang.Throwable -> L3c
                r6 = 0
                android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L3c
                r0 = r1
                r1 = 0
                if (r0 == 0) goto L35
                boolean r2 = r0.moveToFirst()     // Catch: java.lang.Throwable -> L3c
                if (r2 != 0) goto L27
                goto L35
            L27:
                int r2 = r0.getInt(r1)     // Catch: java.lang.Throwable -> L3c
                if (r2 == 0) goto L2f
                r1 = 1
            L2f:
                if (r0 == 0) goto L34
                r0.close()
            L34:
                return r1
            L35:
                if (r0 == 0) goto L3b
                r0.close()
            L3b:
                return r1
            L3c:
                r1 = move-exception
                if (r0 == 0) goto L42
                r0.close()
            L42:
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: android.provider.BrowserContract.Settings.isSyncEnabled(android.content.Context):boolean");
        }

        public static synchronized void setSyncEnabled(Context context, boolean enabled) {
            ContentValues values = new ContentValues();
            values.put("key", KEY_SYNC_ENABLED);
            values.put("value", Integer.valueOf(enabled ? 1 : 0));
            context.getContentResolver().insert(CONTENT_URI, values);
        }
    }
}
