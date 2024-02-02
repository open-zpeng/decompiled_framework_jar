package android.provider;

import android.content.ActivityNotFoundException;
import android.content.ClipDescription;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BrowserContract;
import android.webkit.WebIconDatabase;
import com.android.internal.R;
/* loaded from: classes2.dex */
public class Browser {
    public static final String EXTRA_APPLICATION_ID = "com.android.browser.application_id";
    public static final String EXTRA_CREATE_NEW_TAB = "create_new_tab";
    public static final String EXTRA_HEADERS = "com.android.browser.headers";
    public static final String EXTRA_SHARE_FAVICON = "share_favicon";
    public static final String EXTRA_SHARE_SCREENSHOT = "share_screenshot";
    private protected static final int HISTORY_PROJECTION_BOOKMARK_INDEX = 4;
    private protected static final int HISTORY_PROJECTION_DATE_INDEX = 3;
    private protected static final int HISTORY_PROJECTION_FAVICON_INDEX = 6;
    private protected static final int HISTORY_PROJECTION_ID_INDEX = 0;
    public static final int HISTORY_PROJECTION_THUMBNAIL_INDEX = 7;
    private protected static final int HISTORY_PROJECTION_TITLE_INDEX = 5;
    public static final int HISTORY_PROJECTION_TOUCH_ICON_INDEX = 8;
    private protected static final int HISTORY_PROJECTION_URL_INDEX = 1;
    private protected static final int HISTORY_PROJECTION_VISITS_INDEX = 2;
    public static final String INITIAL_ZOOM_LEVEL = "browser.initialZoomLevel";
    private static final String LOGTAG = "browser";
    private static final int MAX_HISTORY_COUNT = 250;
    private protected static final int SEARCHES_PROJECTION_DATE_INDEX = 2;
    private protected static final int SEARCHES_PROJECTION_SEARCH_INDEX = 1;
    private protected static final int TRUNCATE_HISTORY_PROJECTION_ID_INDEX = 0;
    private protected static final int TRUNCATE_N_OLDEST = 5;
    private protected static final Uri BOOKMARKS_URI = Uri.parse("content://browser/bookmarks");
    private protected static final String[] HISTORY_PROJECTION = {"_id", "url", BrowserContract.HistoryColumns.VISITS, "date", "bookmark", "title", BrowserContract.ImageColumns.FAVICON, "thumbnail", "touch_icon", "user_entered"};
    private protected static final String[] TRUNCATE_HISTORY_PROJECTION = {"_id", "date"};
    private protected static final Uri SEARCHES_URI = Uri.parse("content://browser/searches");
    private protected static final String[] SEARCHES_PROJECTION = {"_id", "search", "date"};

    private protected static final void saveBookmark(Context c, String title, String url) {
    }

    public static final void sendString(Context context, String string) {
        sendString(context, string, context.getString(R.string.sendText));
    }

    private protected static final void sendString(Context c, String stringToSend, String chooserDialogTitle) {
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType(ClipDescription.MIMETYPE_TEXT_PLAIN);
        send.putExtra(Intent.EXTRA_TEXT, stringToSend);
        try {
            Intent i = Intent.createChooser(send, chooserDialogTitle);
            i.setFlags(268435456);
            c.startActivity(i);
        } catch (ActivityNotFoundException e) {
        }
    }

    private protected static final Cursor getAllBookmarks(ContentResolver cr) throws IllegalStateException {
        return new MatrixCursor(new String[]{"url"}, 0);
    }

    private protected static final Cursor getAllVisitedUrls(ContentResolver cr) throws IllegalStateException {
        return new MatrixCursor(new String[]{"url"}, 0);
    }

    private static final synchronized void addOrUrlEquals(StringBuilder sb) {
        sb.append(" OR url = ");
    }

    private static final synchronized Cursor getVisitedLike(ContentResolver cr, String url) {
        StringBuilder whereClause;
        boolean secure = false;
        String compareString = url;
        if (compareString.startsWith("http://")) {
            compareString = compareString.substring(7);
        } else if (compareString.startsWith("https://")) {
            compareString = compareString.substring(8);
            secure = true;
        }
        if (compareString.startsWith("www.")) {
            compareString = compareString.substring(4);
        }
        if (secure) {
            whereClause = new StringBuilder("url = ");
            DatabaseUtils.appendEscapedSQLString(whereClause, "https://" + compareString);
            addOrUrlEquals(whereClause);
            DatabaseUtils.appendEscapedSQLString(whereClause, "https://www." + compareString);
        } else {
            whereClause = new StringBuilder("url = ");
            DatabaseUtils.appendEscapedSQLString(whereClause, compareString);
            addOrUrlEquals(whereClause);
            String wwwString = "www." + compareString;
            DatabaseUtils.appendEscapedSQLString(whereClause, wwwString);
            addOrUrlEquals(whereClause);
            DatabaseUtils.appendEscapedSQLString(whereClause, "http://" + compareString);
            addOrUrlEquals(whereClause);
            DatabaseUtils.appendEscapedSQLString(whereClause, "http://" + wwwString);
        }
        return cr.query(BrowserContract.History.CONTENT_URI, new String[]{"_id", BrowserContract.HistoryColumns.VISITS}, whereClause.toString(), null, null);
    }

    private protected static final void updateVisitedHistory(ContentResolver cr, String url, boolean real) {
    }

    @Deprecated
    private protected static final String[] getVisitedHistory(ContentResolver cr) {
        return new String[0];
    }

    private protected static final void truncateHistory(ContentResolver cr) {
    }

    private protected static final boolean canClearHistory(ContentResolver cr) {
        return false;
    }

    private protected static final void clearHistory(ContentResolver cr) {
    }

    private protected static final void deleteHistoryTimeFrame(ContentResolver cr, long begin, long end) {
    }

    private protected static final void deleteFromHistory(ContentResolver cr, String url) {
    }

    private protected static final void addSearchUrl(ContentResolver cr, String search) {
    }

    private protected static final void clearSearches(ContentResolver cr) {
    }

    private protected static final void requestAllIcons(ContentResolver cr, String where, WebIconDatabase.IconListener listener) {
    }

    /* loaded from: classes2.dex */
    public static class BookmarkColumns implements BaseColumns {
        private protected static final String BOOKMARK = "bookmark";
        private protected static final String CREATED = "created";
        private protected static final String DATE = "date";
        private protected static final String FAVICON = "favicon";
        public static final String THUMBNAIL = "thumbnail";
        private protected static final String TITLE = "title";
        public static final String TOUCH_ICON = "touch_icon";
        private protected static final String URL = "url";
        public static final String USER_ENTERED = "user_entered";
        private protected static final String VISITS = "visits";

        private protected BookmarkColumns() {
        }
    }

    /* loaded from: classes2.dex */
    public static class SearchColumns implements BaseColumns {
        private protected static final String DATE = "date";
        private protected static final String SEARCH = "search";
        @Deprecated
        private protected static final String URL = "url";

        private protected SearchColumns() {
        }
    }
}
