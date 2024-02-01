package android.provider;

import android.app.backup.FullBackup;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriPermission;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.provider.Contacts;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import libcore.io.IoUtils;
/* loaded from: classes2.dex */
public final class MediaStore {
    public static final String ACTION_IMAGE_CAPTURE = "android.media.action.IMAGE_CAPTURE";
    public static final String ACTION_IMAGE_CAPTURE_SECURE = "android.media.action.IMAGE_CAPTURE_SECURE";
    public static final String ACTION_VIDEO_CAPTURE = "android.media.action.VIDEO_CAPTURE";
    public static final String AUTHORITY = "media";
    private static final String CONTENT_AUTHORITY_SLASH = "content://media/";
    public static final String EXTRA_DURATION_LIMIT = "android.intent.extra.durationLimit";
    public static final String EXTRA_FINISH_ON_COMPLETION = "android.intent.extra.finishOnCompletion";
    public static final String EXTRA_FULL_SCREEN = "android.intent.extra.fullScreen";
    public static final String EXTRA_MEDIA_ALBUM = "android.intent.extra.album";
    public static final String EXTRA_MEDIA_ARTIST = "android.intent.extra.artist";
    public static final String EXTRA_MEDIA_FOCUS = "android.intent.extra.focus";
    public static final String EXTRA_MEDIA_GENRE = "android.intent.extra.genre";
    public static final String EXTRA_MEDIA_PLAYLIST = "android.intent.extra.playlist";
    public static final String EXTRA_MEDIA_RADIO_CHANNEL = "android.intent.extra.radio_channel";
    public static final String EXTRA_MEDIA_TITLE = "android.intent.extra.title";
    public static final String EXTRA_OUTPUT = "output";
    public static final String EXTRA_SCREEN_ORIENTATION = "android.intent.extra.screenOrientation";
    public static final String EXTRA_SHOW_ACTION_ICONS = "android.intent.extra.showActionIcons";
    public static final String EXTRA_SIZE_LIMIT = "android.intent.extra.sizeLimit";
    public static final String EXTRA_VIDEO_QUALITY = "android.intent.extra.videoQuality";
    public static final String INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH = "android.media.action.MEDIA_PLAY_FROM_SEARCH";
    public static final String INTENT_ACTION_MEDIA_SEARCH = "android.intent.action.MEDIA_SEARCH";
    @Deprecated
    public static final String INTENT_ACTION_MUSIC_PLAYER = "android.intent.action.MUSIC_PLAYER";
    public static final String INTENT_ACTION_STILL_IMAGE_CAMERA = "android.media.action.STILL_IMAGE_CAMERA";
    public static final String INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE = "android.media.action.STILL_IMAGE_CAMERA_SECURE";
    public static final String INTENT_ACTION_TEXT_OPEN_FROM_SEARCH = "android.media.action.TEXT_OPEN_FROM_SEARCH";
    public static final String INTENT_ACTION_VIDEO_CAMERA = "android.media.action.VIDEO_CAMERA";
    public static final String INTENT_ACTION_VIDEO_PLAY_FROM_SEARCH = "android.media.action.VIDEO_PLAY_FROM_SEARCH";
    public static final String MEDIA_IGNORE_FILENAME = ".nomedia";
    public static final String MEDIA_SCANNER_VOLUME = "volume";
    public static final String META_DATA_STILL_IMAGE_CAMERA_PREWARM_SERVICE = "android.media.still_image_camera_preview_service";
    public static final String PARAM_DELETE_DATA = "deletedata";
    public static final String RETRANSLATE_CALL = "update_titles";
    private static final String TAG = "MediaStore";
    public static final String UNHIDE_CALL = "unhide";
    public static final String UNKNOWN_STRING = "<unknown>";

    /* loaded from: classes2.dex */
    public interface MediaColumns extends BaseColumns {
        public static final String DATA = "_data";
        public static final String DATE_ADDED = "date_added";
        public static final String DATE_MODIFIED = "date_modified";
        public static final String DISPLAY_NAME = "_display_name";
        public static final String HEIGHT = "height";
        private protected static final String IS_DRM = "is_drm";
        public static final String MEDIA_SCANNER_NEW_OBJECT_ID = "media_scanner_new_object_id";
        public static final String MIME_TYPE = "mime_type";
        public static final String SIZE = "_size";
        public static final String TITLE = "title";
        public static final String WIDTH = "width";
    }

    /* loaded from: classes2.dex */
    public static final class Files {

        /* loaded from: classes2.dex */
        public interface FileColumns extends MediaColumns {
            private protected static final String FORMAT = "format";
            public static final String MEDIA_TYPE = "media_type";
            public static final int MEDIA_TYPE_AUDIO = 2;
            public static final int MEDIA_TYPE_IMAGE = 1;
            public static final int MEDIA_TYPE_NONE = 0;
            public static final int MEDIA_TYPE_PLAYLIST = 4;
            public static final int MEDIA_TYPE_VIDEO = 3;
            public static final String MIME_TYPE = "mime_type";
            public static final String PARENT = "parent";
            private protected static final String STORAGE_ID = "storage_id";
            public static final String TITLE = "title";
        }

        public static Uri getContentUri(String volumeName) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/file");
        }

        public static final Uri getContentUri(String volumeName, long rowId) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/file/" + rowId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Uri getMtpObjectsUri(String volumeName) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/object");
        }

        private protected static final Uri getMtpObjectsUri(String volumeName, long fileId) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/object/" + fileId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final Uri getMtpReferencesUri(String volumeName, long fileId) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/object/" + fileId + "/references");
        }

        public static final synchronized Uri getDirectoryUri(String volumeName) {
            return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/dir");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class InternalThumbnails implements BaseColumns {
        static final int DEFAULT_GROUP_ID = 0;
        private static final int FULL_SCREEN_KIND = 2;
        private static final int MICRO_KIND = 3;
        private static final int MINI_KIND = 1;
        private static byte[] sThumbBuf;
        private static final String[] PROJECTION = {"_id", "_data"};
        private static final Object sThumbBufLock = new Object();

        private synchronized InternalThumbnails() {
        }

        private static synchronized Bitmap getMiniThumbFromFile(Cursor c, Uri baseUri, ContentResolver cr, BitmapFactory.Options options) {
            Bitmap bitmap = null;
            Uri thumbUri = null;
            try {
                long thumbId = c.getLong(0);
                c.getString(1);
                thumbUri = ContentUris.withAppendedId(baseUri, thumbId);
                ParcelFileDescriptor pfdInput = cr.openFileDescriptor(thumbUri, FullBackup.ROOT_TREE_TOKEN);
                bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, options);
                pfdInput.close();
                return bitmap;
            } catch (FileNotFoundException ex) {
                Log.e(MediaStore.TAG, "couldn't open thumbnail " + thumbUri + "; " + ex);
                return bitmap;
            } catch (IOException ex2) {
                Log.e(MediaStore.TAG, "couldn't open thumbnail " + thumbUri + "; " + ex2);
                return bitmap;
            } catch (OutOfMemoryError ex3) {
                Log.e(MediaStore.TAG, "failed to allocate memory for thumbnail " + thumbUri + "; " + ex3);
                return bitmap;
            }
        }

        static synchronized void cancelThumbnailRequest(ContentResolver cr, long origId, Uri baseUri, long groupId) {
            Uri cancelUri = baseUri.buildUpon().appendQueryParameter("cancel", "1").appendQueryParameter("orig_id", String.valueOf(origId)).appendQueryParameter(Contacts.GroupMembership.GROUP_ID, String.valueOf(groupId)).build();
            Cursor c = null;
            try {
                c = cr.query(cancelUri, PROJECTION, null, null, null);
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Not initialized variable reg: 25, insn: 0x0257: MOVE  (r6 I:??[OBJECT, ARRAY]) = (r25 I:??[OBJECT, ARRAY] A[D('c' android.database.Cursor)]), block:B:150:0x0257 */
        /* JADX WARN: Not initialized variable reg: 25, insn: 0x025d: MOVE  (r6 I:??[OBJECT, ARRAY]) = (r25 I:??[OBJECT, ARRAY] A[D('c' android.database.Cursor)]), block:B:152:0x025b */
        /* JADX WARN: Removed duplicated region for block: B:167:0x028c  */
        /* JADX WARN: Removed duplicated region for block: B:173:0x029a  */
        /* JADX WARN: Removed duplicated region for block: B:184:0x012d A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:69:0x010f A[Catch: all -> 0x00be, SQLiteException -> 0x00c0, TRY_ENTER, TRY_LEAVE, TryCatch #19 {SQLiteException -> 0x00c0, all -> 0x00be, blocks: (B:44:0x00a4, B:46:0x00aa, B:69:0x010f), top: B:198:0x00a4 }] */
        /* JADX WARN: Removed duplicated region for block: B:74:0x0123  */
        /* JADX WARN: Type inference failed for: r1v0 */
        /* JADX WARN: Type inference failed for: r1v10 */
        /* JADX WARN: Type inference failed for: r1v12 */
        /* JADX WARN: Type inference failed for: r1v13 */
        /* JADX WARN: Type inference failed for: r1v19 */
        /* JADX WARN: Type inference failed for: r1v20 */
        /* JADX WARN: Type inference failed for: r1v21, types: [android.database.Cursor] */
        /* JADX WARN: Type inference failed for: r1v23 */
        /* JADX WARN: Type inference failed for: r1v24 */
        /* JADX WARN: Type inference failed for: r1v27, types: [android.media.MiniThumbFile] */
        /* JADX WARN: Type inference failed for: r1v32 */
        /* JADX WARN: Type inference failed for: r1v33 */
        /* JADX WARN: Type inference failed for: r1v35 */
        /* JADX WARN: Type inference failed for: r1v36 */
        /* JADX WARN: Type inference failed for: r1v4, types: [android.media.MiniThumbFile] */
        /* JADX WARN: Type inference failed for: r1v41 */
        /* JADX WARN: Type inference failed for: r1v42 */
        /* JADX WARN: Type inference failed for: r1v8 */
        /* JADX WARN: Type inference failed for: r1v9 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        static synchronized android.graphics.Bitmap getThumbnail(android.content.ContentResolver r26, long r27, long r29, int r31, android.graphics.BitmapFactory.Options r32, android.net.Uri r33, boolean r34) {
            /*
                Method dump skipped, instructions count: 674
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.InternalThumbnails.getThumbnail(android.content.ContentResolver, long, long, int, android.graphics.BitmapFactory$Options, android.net.Uri, boolean):android.graphics.Bitmap");
        }
    }

    /* loaded from: classes2.dex */
    public static final class Images {

        /* loaded from: classes2.dex */
        public interface ImageColumns extends MediaColumns {
            public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
            public static final String BUCKET_ID = "bucket_id";
            public static final String DATE_TAKEN = "datetaken";
            public static final String DESCRIPTION = "description";
            public static final String IS_PRIVATE = "isprivate";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";
            public static final String ORIENTATION = "orientation";
            public static final String PICASA_ID = "picasa_id";
        }

        /* loaded from: classes2.dex */
        public static final class Media implements ImageColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/image";
            public static final String DEFAULT_SORT_ORDER = "bucket_display_name";
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
                return cr.query(uri, projection, null, null, "bucket_display_name");
            }

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection, String where, String orderBy) {
                return cr.query(uri, projection, where, null, orderBy == null ? "bucket_display_name" : orderBy);
            }

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
                return cr.query(uri, projection, selection, selectionArgs, orderBy == null ? "bucket_display_name" : orderBy);
            }

            public static final Bitmap getBitmap(ContentResolver cr, Uri url) throws FileNotFoundException, IOException {
                InputStream input = cr.openInputStream(url);
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();
                return bitmap;
            }

            public static final String insertImage(ContentResolver cr, String imagePath, String name, String description) throws FileNotFoundException {
                FileInputStream stream = new FileInputStream(imagePath);
                try {
                    Bitmap bm = BitmapFactory.decodeFile(imagePath);
                    String ret = insertImage(cr, bm, name, description);
                    bm.recycle();
                    return ret;
                } finally {
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
            }

            private static final synchronized Bitmap StoreThumbnail(ContentResolver cr, Bitmap source, long id, float width, float height, int kind) {
                Matrix matrix = new Matrix();
                float scaleX = width / source.getWidth();
                float scaleY = height / source.getHeight();
                matrix.setScale(scaleX, scaleY);
                Bitmap thumb = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
                ContentValues values = new ContentValues(4);
                values.put("kind", Integer.valueOf(kind));
                values.put("image_id", Integer.valueOf((int) id));
                values.put("height", Integer.valueOf(thumb.getHeight()));
                values.put("width", Integer.valueOf(thumb.getWidth()));
                Uri url = cr.insert(Thumbnails.EXTERNAL_CONTENT_URI, values);
                try {
                    OutputStream thumbOut = cr.openOutputStream(url);
                    thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
                    thumbOut.close();
                    return thumb;
                } catch (FileNotFoundException e) {
                    return null;
                } catch (IOException e2) {
                    return null;
                }
            }

            /* JADX WARN: Removed duplicated region for block: B:24:0x0081  */
            /* JADX WARN: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public static final java.lang.String insertImage(android.content.ContentResolver r21, android.graphics.Bitmap r22, java.lang.String r23, java.lang.String r24) {
                /*
                    r8 = r21
                    r9 = r22
                    android.content.ContentValues r0 = new android.content.ContentValues
                    r0.<init>()
                    r10 = r0
                    java.lang.String r0 = "title"
                    r11 = r23
                    r10.put(r0, r11)
                    java.lang.String r0 = "description"
                    r12 = r24
                    r10.put(r0, r12)
                    java.lang.String r0 = "mime_type"
                    java.lang.String r1 = "image/jpeg"
                    r10.put(r0, r1)
                    r1 = 0
                    r13 = 0
                    r14 = r13
                    android.net.Uri r0 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI     // Catch: java.lang.Exception -> L6f
                    android.net.Uri r0 = r8.insert(r0, r10)     // Catch: java.lang.Exception -> L6f
                    r15 = r0
                    if (r9 == 0) goto L62
                    java.io.OutputStream r0 = r8.openOutputStream(r15)     // Catch: java.lang.Exception -> L60
                    r7 = r0
                    android.graphics.Bitmap$CompressFormat r0 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch: java.lang.Throwable -> L5a
                    r1 = 50
                    r9.compress(r0, r1, r7)     // Catch: java.lang.Throwable -> L5a
                    r7.close()     // Catch: java.lang.Exception -> L60
                    long r0 = android.content.ContentUris.parseId(r15)     // Catch: java.lang.Exception -> L60
                    r5 = r0
                    r0 = 1
                    android.graphics.Bitmap r2 = android.provider.MediaStore.Images.Thumbnails.getThumbnail(r8, r5, r0, r13)     // Catch: java.lang.Exception -> L60
                    r0 = 1112014848(0x42480000, float:50.0)
                    r16 = 1112014848(0x42480000, float:50.0)
                    r17 = 3
                    r1 = r8
                    r3 = r5
                    r18 = r5
                    r5 = r0
                    r6 = r16
                    r13 = r7
                    r7 = r17
                    StoreThumbnail(r1, r2, r3, r5, r6, r7)     // Catch: java.lang.Exception -> L60
                    goto L6e
                L5a:
                    r0 = move-exception
                    r13 = r7
                    r13.close()     // Catch: java.lang.Exception -> L60
                    throw r0     // Catch: java.lang.Exception -> L60
                L60:
                    r0 = move-exception
                    goto L71
                L62:
                    java.lang.String r0 = "MediaStore"
                    java.lang.String r1 = "Failed to create thumbnail, removing original"
                    android.util.Log.e(r0, r1)     // Catch: java.lang.Exception -> L60
                    r1 = 0
                    r8.delete(r15, r1, r1)     // Catch: java.lang.Exception -> L60
                    r15 = 0
                L6e:
                    goto L7f
                L6f:
                    r0 = move-exception
                    r15 = r1
                L71:
                    java.lang.String r1 = "MediaStore"
                    java.lang.String r2 = "Failed to insert image"
                    android.util.Log.e(r1, r2, r0)
                    if (r15 == 0) goto L7f
                    r1 = 0
                    r8.delete(r15, r1, r1)
                    r15 = 0
                L7f:
                    if (r15 == 0) goto L85
                    java.lang.String r14 = r15.toString()
                L85:
                    return r14
                */
                throw new UnsupportedOperationException("Method not decompiled: android.provider.MediaStore.Images.Media.insertImage(android.content.ContentResolver, android.graphics.Bitmap, java.lang.String, java.lang.String):java.lang.String");
            }

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/images/media");
            }
        }

        /* loaded from: classes2.dex */
        public static class Thumbnails implements BaseColumns {
            public static final String DATA = "_data";
            public static final String DEFAULT_SORT_ORDER = "image_id ASC";
            public static final int FULL_SCREEN_KIND = 2;
            public static final String HEIGHT = "height";
            public static final String IMAGE_ID = "image_id";
            public static final String KIND = "kind";
            public static final int MICRO_KIND = 3;
            public static final int MINI_KIND = 1;
            public static final String THUMB_DATA = "thumb_data";
            public static final String WIDTH = "width";
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
                return cr.query(uri, projection, null, null, DEFAULT_SORT_ORDER);
            }

            public static final Cursor queryMiniThumbnails(ContentResolver cr, Uri uri, int kind, String[] projection) {
                return cr.query(uri, projection, "kind = " + kind, null, DEFAULT_SORT_ORDER);
            }

            public static final Cursor queryMiniThumbnail(ContentResolver cr, long origId, int kind, String[] projection) {
                Uri uri = EXTERNAL_CONTENT_URI;
                return cr.query(uri, projection, "image_id = " + origId + " AND kind = " + kind, null, null);
            }

            public static void cancelThumbnailRequest(ContentResolver cr, long origId) {
                InternalThumbnails.cancelThumbnailRequest(cr, origId, EXTERNAL_CONTENT_URI, 0L);
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options) {
                return InternalThumbnails.getThumbnail(cr, origId, 0L, kind, options, EXTERNAL_CONTENT_URI, false);
            }

            public static void cancelThumbnailRequest(ContentResolver cr, long origId, long groupId) {
                InternalThumbnails.cancelThumbnailRequest(cr, origId, EXTERNAL_CONTENT_URI, groupId);
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, long groupId, int kind, BitmapFactory.Options options) {
                return InternalThumbnails.getThumbnail(cr, origId, groupId, kind, options, EXTERNAL_CONTENT_URI, false);
            }

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/images/thumbnails");
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class Audio {

        /* loaded from: classes2.dex */
        public interface AlbumColumns {
            public static final String ALBUM = "album";
            public static final String ALBUM_ART = "album_art";
            public static final String ALBUM_ID = "album_id";
            public static final String ALBUM_KEY = "album_key";
            public static final String ARTIST = "artist";
            public static final String FIRST_YEAR = "minyear";
            public static final String LAST_YEAR = "maxyear";
            public static final String NUMBER_OF_SONGS = "numsongs";
            public static final String NUMBER_OF_SONGS_FOR_ARTIST = "numsongs_by_artist";
        }

        /* loaded from: classes2.dex */
        public interface ArtistColumns {
            public static final String ARTIST = "artist";
            public static final String ARTIST_KEY = "artist_key";
            public static final String NUMBER_OF_ALBUMS = "number_of_albums";
            public static final String NUMBER_OF_TRACKS = "number_of_tracks";
        }

        /* loaded from: classes2.dex */
        public interface AudioColumns extends MediaColumns {
            public static final String ALBUM = "album";
            public static final String ALBUM_ARTIST = "album_artist";
            public static final String ALBUM_ID = "album_id";
            public static final String ALBUM_KEY = "album_key";
            public static final String ARTIST = "artist";
            public static final String ARTIST_ID = "artist_id";
            public static final String ARTIST_KEY = "artist_key";
            public static final String BOOKMARK = "bookmark";
            public static final String COMPILATION = "compilation";
            public static final String COMPOSER = "composer";
            public static final String DURATION = "duration";
            public static final String GENRE = "genre";
            public static final String IS_ALARM = "is_alarm";
            public static final String IS_MUSIC = "is_music";
            public static final String IS_NOTIFICATION = "is_notification";
            public static final String IS_PODCAST = "is_podcast";
            public static final String IS_RINGTONE = "is_ringtone";
            public static final String TITLE_KEY = "title_key";
            public static final String TITLE_RESOURCE_URI = "title_resource_uri";
            public static final String TRACK = "track";
            public static final String YEAR = "year";
        }

        /* loaded from: classes2.dex */
        public interface GenresColumns {
            public static final String NAME = "name";
        }

        /* loaded from: classes2.dex */
        public interface PlaylistsColumns {
            public static final String DATA = "_data";
            public static final String DATE_ADDED = "date_added";
            public static final String DATE_MODIFIED = "date_modified";
            public static final String NAME = "name";
        }

        public static String keyFor(String name) {
            if (name != null) {
                boolean sortfirst = false;
                if (name.equals(MediaStore.UNKNOWN_STRING)) {
                    return "\u0001";
                }
                if (name.startsWith("\u0001")) {
                    sortfirst = true;
                }
                String name2 = name.trim().toLowerCase();
                if (name2.startsWith("the ")) {
                    name2 = name2.substring(4);
                }
                if (name2.startsWith("an ")) {
                    name2 = name2.substring(3);
                }
                if (name2.startsWith("a ")) {
                    name2 = name2.substring(2);
                }
                if (name2.endsWith(", the") || name2.endsWith(",the") || name2.endsWith(", an") || name2.endsWith(",an") || name2.endsWith(", a") || name2.endsWith(",a")) {
                    name2 = name2.substring(0, name2.lastIndexOf(44));
                }
                String name3 = name2.replaceAll("[\\[\\]\\(\\)\"'.,?!]", "").trim();
                if (name3.length() > 0) {
                    StringBuilder b = new StringBuilder();
                    b.append('.');
                    int nl = name3.length();
                    for (int i = 0; i < nl; i++) {
                        b.append(name3.charAt(i));
                        b.append('.');
                    }
                    String key = DatabaseUtils.getCollationKey(b.toString());
                    if (sortfirst) {
                        return "\u0001" + key;
                    }
                    return key;
                }
                return "";
            }
            return null;
        }

        /* loaded from: classes2.dex */
        public static final class Media implements AudioColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/audio";
            public static final String DEFAULT_SORT_ORDER = "title_key";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/audio";
            public static final Uri EXTERNAL_CONTENT_URI;
            private static final String[] EXTERNAL_PATHS;
            public static final String EXTRA_MAX_BYTES = "android.provider.MediaStore.extra.MAX_BYTES";
            public static final Uri INTERNAL_CONTENT_URI;
            public static final String RECORD_SOUND_ACTION = "android.provider.MediaStore.RECORD_SOUND";

            static {
                String secondary_storage = System.getenv("SECONDARY_STORAGE");
                if (secondary_storage != null) {
                    EXTERNAL_PATHS = secondary_storage.split(SettingsStringUtil.DELIMITER);
                } else {
                    EXTERNAL_PATHS = new String[0];
                }
                INTERNAL_CONTENT_URI = getContentUri("internal");
                EXTERNAL_CONTENT_URI = getContentUri("external");
            }

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/audio/media");
            }

            public static Uri getContentUriForPath(String path) {
                String[] strArr;
                for (String ep : EXTERNAL_PATHS) {
                    if (path.startsWith(ep)) {
                        return EXTERNAL_CONTENT_URI;
                    }
                }
                return path.startsWith(Environment.getExternalStorageDirectory().getPath()) ? EXTERNAL_CONTENT_URI : INTERNAL_CONTENT_URI;
            }
        }

        /* loaded from: classes2.dex */
        public static final class Genres implements BaseColumns, GenresColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/genre";
            public static final String DEFAULT_SORT_ORDER = "name";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/genre";
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/audio/genres");
            }

            public static Uri getContentUriForAudioId(String volumeName, int audioId) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/audio/media/" + audioId + "/genres");
            }

            /* loaded from: classes2.dex */
            public static final class Members implements AudioColumns {
                public static final String AUDIO_ID = "audio_id";
                public static final String CONTENT_DIRECTORY = "members";
                public static final String DEFAULT_SORT_ORDER = "title_key";
                public static final String GENRE_ID = "genre_id";

                public static final Uri getContentUri(String volumeName, long genreId) {
                    return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/audio/genres/" + genreId + "/members");
                }
            }
        }

        /* loaded from: classes2.dex */
        public static final class Playlists implements BaseColumns, PlaylistsColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/playlist";
            public static final String DEFAULT_SORT_ORDER = "name";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/playlist";
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/audio/playlists");
            }

            /* loaded from: classes2.dex */
            public static final class Members implements AudioColumns {
                public static final String AUDIO_ID = "audio_id";
                public static final String CONTENT_DIRECTORY = "members";
                public static final String DEFAULT_SORT_ORDER = "play_order";
                public static final String PLAYLIST_ID = "playlist_id";
                public static final String PLAY_ORDER = "play_order";
                public static final String _ID = "_id";

                public static final Uri getContentUri(String volumeName, long playlistId) {
                    return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/audio/playlists/" + playlistId + "/members");
                }

                public static final boolean moveItem(ContentResolver res, long playlistId, int from, int to) {
                    Uri uri = getContentUri("external", playlistId).buildUpon().appendEncodedPath(String.valueOf(from)).appendQueryParameter("move", "true").build();
                    ContentValues values = new ContentValues();
                    values.put("play_order", Integer.valueOf(to));
                    return res.update(uri, values, null, null) != 0;
                }
            }
        }

        /* loaded from: classes2.dex */
        public static final class Artists implements BaseColumns, ArtistColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/artists";
            public static final String DEFAULT_SORT_ORDER = "artist_key";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/artist";
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/audio/artists");
            }

            /* loaded from: classes2.dex */
            public static final class Albums implements AlbumColumns {
                public static final Uri getContentUri(String volumeName, long artistId) {
                    return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/audio/artists/" + artistId + "/albums");
                }
            }
        }

        /* loaded from: classes2.dex */
        public static final class Albums implements BaseColumns, AlbumColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/albums";
            public static final String DEFAULT_SORT_ORDER = "album_key";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/album";
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/audio/albums");
            }
        }

        /* loaded from: classes2.dex */
        public static final class Radio {
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/radio";

            private synchronized Radio() {
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class Video {
        public static final String DEFAULT_SORT_ORDER = "_display_name";

        /* loaded from: classes2.dex */
        public interface VideoColumns extends MediaColumns {
            public static final String ALBUM = "album";
            public static final String ARTIST = "artist";
            public static final String BOOKMARK = "bookmark";
            public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
            public static final String BUCKET_ID = "bucket_id";
            public static final String CATEGORY = "category";
            public static final String DATE_TAKEN = "datetaken";
            public static final String DESCRIPTION = "description";
            public static final String DURATION = "duration";
            public static final String IS_PRIVATE = "isprivate";
            public static final String LANGUAGE = "language";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";
            public static final String RESOLUTION = "resolution";
            public static final String TAGS = "tags";
        }

        public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
            return cr.query(uri, projection, null, null, "_display_name");
        }

        /* loaded from: classes2.dex */
        public static final class Media implements VideoColumns {
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/video";
            public static final String DEFAULT_SORT_ORDER = "title";
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/video/media");
            }
        }

        /* loaded from: classes2.dex */
        public static class Thumbnails implements BaseColumns {
            public static final String DATA = "_data";
            public static final String DEFAULT_SORT_ORDER = "video_id ASC";
            public static final int FULL_SCREEN_KIND = 2;
            public static final String HEIGHT = "height";
            public static final String KIND = "kind";
            public static final int MICRO_KIND = 3;
            public static final int MINI_KIND = 1;
            public static final String VIDEO_ID = "video_id";
            public static final String WIDTH = "width";
            public static final Uri INTERNAL_CONTENT_URI = getContentUri("internal");
            public static final Uri EXTERNAL_CONTENT_URI = getContentUri("external");

            public static void cancelThumbnailRequest(ContentResolver cr, long origId) {
                InternalThumbnails.cancelThumbnailRequest(cr, origId, EXTERNAL_CONTENT_URI, 0L);
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options) {
                return InternalThumbnails.getThumbnail(cr, origId, 0L, kind, options, EXTERNAL_CONTENT_URI, true);
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, long groupId, int kind, BitmapFactory.Options options) {
                return InternalThumbnails.getThumbnail(cr, origId, groupId, kind, options, EXTERNAL_CONTENT_URI, true);
            }

            public static void cancelThumbnailRequest(ContentResolver cr, long origId, long groupId) {
                InternalThumbnails.cancelThumbnailRequest(cr, origId, EXTERNAL_CONTENT_URI, groupId);
            }

            public static Uri getContentUri(String volumeName) {
                return Uri.parse(MediaStore.CONTENT_AUTHORITY_SLASH + volumeName + "/video/thumbnails");
            }
        }
    }

    public static Uri getMediaScannerUri() {
        return Uri.parse("content://media/none/media_scanner");
    }

    public static String getVersion(Context context) {
        Cursor c = context.getContentResolver().query(Uri.parse("content://media/none/version"), null, null, null, null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(0);
                }
                return null;
            } finally {
                c.close();
            }
        }
        return null;
    }

    public static Uri getDocumentUri(Context context, Uri mediaUri) {
        try {
            ContentResolver resolver = context.getContentResolver();
            String path = getFilePath(resolver, mediaUri);
            List<UriPermission> uriPermissions = resolver.getPersistedUriPermissions();
            return getDocumentUri(resolver, path, uriPermissions);
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    private static synchronized String getFilePath(ContentResolver resolver, Uri mediaUri) throws RemoteException {
        ContentProviderClient client = resolver.acquireUnstableContentProviderClient(AUTHORITY);
        try {
            Cursor c = client.query(mediaUri, new String[]{"_data"}, null, null, null);
            if (c.getCount() == 0) {
                throw new IllegalStateException("Not found media file under URI: " + mediaUri);
            } else if (!c.moveToFirst()) {
                throw new IllegalStateException("Failed to move cursor to the first item.");
            } else {
                String path = c.getString(0);
                IoUtils.closeQuietly(c);
                if (client != null) {
                    $closeResource(null, client);
                }
                return path;
            }
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (client != null) {
                    $closeResource(th, client);
                }
                throw th2;
            }
        }
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

    private static synchronized Uri getDocumentUri(ContentResolver resolver, String path, List<UriPermission> uriPermissions) throws RemoteException {
        ContentProviderClient client = resolver.acquireUnstableContentProviderClient(DocumentsContract.EXTERNAL_STORAGE_PROVIDER_AUTHORITY);
        try {
            Bundle in = new Bundle();
            in.putParcelableList("com.android.externalstorage.documents.extra.uriPermissions", uriPermissions);
            Bundle out = client.call("getDocumentId", path, in);
            Uri uri = (Uri) out.getParcelable("uri");
            if (client != null) {
                $closeResource(null, client);
            }
            return uri;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (client != null) {
                    $closeResource(th, client);
                }
                throw th2;
            }
        }
    }
}
