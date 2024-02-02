package android.media;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.drm.DrmManagerClient;
import android.graphics.BitmapFactory;
import android.media.MediaFile;
import android.net.Uri;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Telephony;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.RootElement;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.Xml;
import dalvik.system.CloseGuard;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
/* loaded from: classes.dex */
public class MediaScanner implements AutoCloseable {
    private static final String ALARMS_DIR = "/alarms/";
    private static final int DATE_MODIFIED_PLAYLISTS_COLUMN_INDEX = 2;
    private static final String DEFAULT_RINGTONE_PROPERTY_PREFIX = "ro.config.";
    private static final boolean ENABLE_BULK_INSERTS = true;
    private static final int FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX = 3;
    private static final int FILES_PRESCAN_FORMAT_COLUMN_INDEX = 2;
    private static final int FILES_PRESCAN_ID_COLUMN_INDEX = 0;
    private static final int FILES_PRESCAN_PATH_COLUMN_INDEX = 1;
    public protected static final String[] FILES_PRESCAN_PROJECTION;
    private static final String[] ID3_GENRES;
    private static final int ID_PLAYLISTS_COLUMN_INDEX = 0;
    private static final String[] ID_PROJECTION;
    public static final String LAST_INTERNAL_SCAN_FINGERPRINT = "lastScanFingerprint";
    private static final String MUSIC_DIR = "/music/";
    private static final String NOTIFICATIONS_DIR = "/notifications/";
    private static final int PATH_PLAYLISTS_COLUMN_INDEX = 1;
    private static final String[] PLAYLIST_MEMBERS_PROJECTION;
    private static final String PODCAST_DIR = "/podcasts/";
    private static final String PRODUCT_SOUNDS_DIR = "/product/media/audio";
    private static final String RINGTONES_DIR = "/ringtones/";
    public static final String SCANNED_BUILD_PREFS_NAME = "MediaScanBuild";
    private static final String SYSTEM_SOUNDS_DIR = "/system/media/audio";
    private static final String TAG = "MediaScanner";
    private static HashMap<String, String> mMediaPaths;
    private static HashMap<String, String> mNoMediaPaths;
    private static String sLastInternalScanFingerprint;
    public protected final Uri mAudioUri;
    public protected final Context mContext;
    public protected String mDefaultAlarmAlertFilename;
    private boolean mDefaultAlarmSet;
    public protected String mDefaultNotificationFilename;
    private boolean mDefaultNotificationSet;
    public protected String mDefaultRingtoneFilename;
    private boolean mDefaultRingtoneSet;
    public protected final Uri mFilesUri;
    private final Uri mFilesUriNoNotify;
    private final Uri mImagesUri;
    public protected MediaInserter mMediaInserter;
    private final ContentProviderClient mMediaProvider;
    private int mMtpObjectHandle;
    private long mNativeContext;
    private int mOriginalCount;
    public protected final String mPackageName;
    private final Uri mPlaylistsUri;
    private final boolean mProcessGenres;
    private final boolean mProcessPlaylists;
    private final Uri mVideoUri;
    private final String mVolumeName;
    private final AtomicBoolean mClosed = new AtomicBoolean();
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
    private final ArrayList<PlaylistEntry> mPlaylistEntries = new ArrayList<>();
    private final ArrayList<FileEntry> mPlayLists = new ArrayList<>();
    private DrmManagerClient mDrmManagerClient = null;
    public protected final MyMediaScannerClient mClient = new MyMediaScannerClient();

    private final native void native_finalize();

    private static final native void native_init();

    private final native void native_setup();

    private native void processDirectory(String str, MediaScannerClient mediaScannerClient);

    /* JADX INFO: Access modifiers changed from: private */
    public native boolean processFile(String str, String str2, MediaScannerClient mediaScannerClient);

    public protected native void setLocale(String str);

    public native byte[] extractAlbumArt(FileDescriptor fileDescriptor);

    static {
        System.loadLibrary("media_jni");
        native_init();
        FILES_PRESCAN_PROJECTION = new String[]{"_id", "_data", Telephony.CellBroadcasts.MESSAGE_FORMAT, "date_modified"};
        ID_PROJECTION = new String[]{"_id"};
        PLAYLIST_MEMBERS_PROJECTION = new String[]{MediaStore.Audio.Playlists.Members.PLAYLIST_ID};
        ID3_GENRES = new String[]{"Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "Britpop", null, "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "JPop", "Synthpop"};
        mNoMediaPaths = new HashMap<>();
        mMediaPaths = new HashMap<>();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FileEntry {
        int mFormat;
        long mLastModified;
        public private protected boolean mLastModifiedChanged = false;
        String mPath;
        public private protected long mRowId;

        public private protected FileEntry(long rowId, String path, long lastModified, int format) {
            this.mRowId = rowId;
            this.mPath = path;
            this.mLastModified = lastModified;
            this.mFormat = format;
        }

        public String toString() {
            return this.mPath + " mRowId: " + this.mRowId;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PlaylistEntry {
        long bestmatchid;
        int bestmatchlevel;
        String path;

        private synchronized PlaylistEntry() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MediaScanner(Context c, String volumeName) {
        native_setup();
        this.mContext = c;
        this.mPackageName = c.getPackageName();
        this.mVolumeName = volumeName;
        this.mBitmapOptions.inSampleSize = 1;
        this.mBitmapOptions.inJustDecodeBounds = true;
        setDefaultRingtoneFileNames();
        this.mMediaProvider = this.mContext.getContentResolver().acquireContentProviderClient(MediaStore.AUTHORITY);
        if (sLastInternalScanFingerprint == null) {
            SharedPreferences scanSettings = this.mContext.getSharedPreferences(SCANNED_BUILD_PREFS_NAME, 0);
            sLastInternalScanFingerprint = scanSettings.getString(LAST_INTERNAL_SCAN_FINGERPRINT, new String());
        }
        this.mAudioUri = MediaStore.Audio.Media.getContentUri(volumeName);
        this.mVideoUri = MediaStore.Video.Media.getContentUri(volumeName);
        this.mImagesUri = MediaStore.Images.Media.getContentUri(volumeName);
        this.mFilesUri = MediaStore.Files.getContentUri(volumeName);
        this.mFilesUriNoNotify = this.mFilesUri.buildUpon().appendQueryParameter("nonotify", "1").build();
        if (!volumeName.equals("internal")) {
            this.mProcessPlaylists = true;
            this.mProcessGenres = true;
            this.mPlaylistsUri = MediaStore.Audio.Playlists.getContentUri(volumeName);
        } else {
            this.mProcessPlaylists = false;
            this.mProcessGenres = false;
            this.mPlaylistsUri = null;
        }
        Locale locale = this.mContext.getResources().getConfiguration().locale;
        if (locale != null) {
            String language = locale.getLanguage();
            String country = locale.getCountry();
            if (language != null) {
                if (country != null) {
                    setLocale(language + "_" + country);
                } else {
                    setLocale(language);
                }
            }
        }
        this.mCloseGuard.open("close");
    }

    private synchronized void setDefaultRingtoneFileNames() {
        this.mDefaultRingtoneFilename = SystemProperties.get("ro.config.ringtone");
        this.mDefaultNotificationFilename = SystemProperties.get("ro.config.notification_sound");
        this.mDefaultAlarmAlertFilename = SystemProperties.get("ro.config.alarm_alert");
    }

    /* JADX INFO: Access modifiers changed from: public */
    public boolean isDrmEnabled() {
        String prop = SystemProperties.get("drm.service.enabled");
        return prop != null && prop.equals("true");
    }

    /* loaded from: classes.dex */
    private class MyMediaScannerClient implements MediaScannerClient {
        private String mAlbum;
        private String mAlbumArtist;
        private String mArtist;
        private int mCompilation;
        private String mComposer;
        private long mDate;
        private final SimpleDateFormat mDateFormatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        private int mDuration;
        private long mFileSize;
        public protected int mFileType;
        private String mGenre;
        private int mHeight;
        public protected boolean mIsDrm;
        private long mLastModified;
        public protected String mMimeType;
        public protected boolean mNoMedia;
        public protected String mPath;
        private boolean mScanSuccess;
        private String mTitle;
        private int mTrack;
        private int mWidth;
        private String mWriter;
        private int mYear;

        public MyMediaScannerClient() {
            this.mDateFormatter.setTimeZone(TimeZone.getTimeZone(Time.TIMEZONE_UTC));
        }

        private protected FileEntry beginFile(String path, String mimeType, long lastModified, long fileSize, boolean isDirectory, boolean noMedia) {
            boolean z;
            boolean z2;
            boolean noMedia2;
            MediaFile.MediaFileType mediaFileType;
            this.mMimeType = mimeType;
            this.mFileType = 0;
            this.mFileSize = fileSize;
            this.mIsDrm = false;
            this.mScanSuccess = true;
            if (!isDirectory) {
                if (!noMedia && MediaScanner.isNoMediaFile(path)) {
                    noMedia2 = true;
                } else {
                    noMedia2 = noMedia;
                }
                this.mNoMedia = noMedia2;
                if (mimeType != null) {
                    this.mFileType = MediaFile.getFileTypeForMimeType(mimeType);
                }
                if (this.mFileType == 0 && (mediaFileType = MediaFile.getFileType(path)) != null) {
                    this.mFileType = mediaFileType.fileType;
                    if (this.mMimeType == null) {
                        this.mMimeType = mediaFileType.mimeType;
                    }
                }
                if (MediaScanner.this.isDrmEnabled() && MediaFile.isDrmFileType(this.mFileType)) {
                    this.mFileType = getFileTypeFromDrm(path);
                }
            }
            FileEntry entry = MediaScanner.this.makeEntryFor(path);
            long delta = entry != null ? lastModified - entry.mLastModified : 0L;
            if (delta <= 1 && delta >= -1) {
                z = false;
            } else {
                z = true;
            }
            boolean wasModified = z;
            if (entry == null || wasModified) {
                if (!wasModified) {
                    z2 = true;
                    entry = new FileEntry(0L, path, lastModified, isDirectory ? 12289 : 0);
                } else {
                    entry.mLastModified = lastModified;
                    z2 = true;
                }
                entry.mLastModifiedChanged = z2;
            }
            if (MediaScanner.this.mProcessPlaylists && MediaFile.isPlayListFileType(this.mFileType)) {
                MediaScanner.this.mPlayLists.add(entry);
                return null;
            }
            this.mArtist = null;
            this.mAlbumArtist = null;
            this.mAlbum = null;
            this.mTitle = null;
            this.mComposer = null;
            this.mGenre = null;
            this.mTrack = 0;
            this.mYear = 0;
            this.mDuration = 0;
            this.mPath = path;
            this.mDate = 0L;
            this.mLastModified = lastModified;
            this.mWriter = null;
            this.mCompilation = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            return entry;
        }

        private protected void scanFile(String path, long lastModified, long fileSize, boolean isDirectory, boolean noMedia) {
            doScanFile(path, null, lastModified, fileSize, isDirectory, false, noMedia);
        }

        /* JADX WARN: Removed duplicated region for block: B:116:0x00cb A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:62:0x012a A[Catch: RemoteException -> 0x01a4, TryCatch #3 {RemoteException -> 0x01a4, blocks: (B:62:0x012a, B:63:0x0130, B:65:0x013d, B:69:0x0148, B:71:0x014d, B:75:0x0158, B:77:0x015e, B:81:0x0169, B:83:0x016f, B:87:0x017a, B:89:0x0180, B:97:0x0193, B:60:0x011e), top: B:118:0x011e }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private protected android.net.Uri doScanFile(java.lang.String r21, java.lang.String r22, long r23, long r25, boolean r27, boolean r28, boolean r29) {
            /*
                Method dump skipped, instructions count: 446
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.MyMediaScannerClient.doScanFile(java.lang.String, java.lang.String, long, long, boolean, boolean, boolean):android.net.Uri");
        }

        private synchronized long parseDate(String date) {
            try {
                return this.mDateFormatter.parse(date).getTime();
            } catch (ParseException e) {
                return 0L;
            }
        }

        private synchronized int parseSubstring(String s, int start, int defaultValue) {
            int length = s.length();
            if (start == length) {
                return defaultValue;
            }
            int start2 = start + 1;
            int start3 = s.charAt(start);
            if (start3 < 48 || start3 > 57) {
                return defaultValue;
            }
            int result = start3 - 48;
            while (start2 < length) {
                int start4 = start2 + 1;
                char ch = s.charAt(start2);
                if (ch < '0' || ch > '9') {
                    return result;
                }
                result = (result * 10) + (ch - '0');
                start2 = start4;
            }
            return result;
        }

        private protected void handleStringTag(String name, String value) {
            if (name.equalsIgnoreCase("title") || name.startsWith("title;")) {
                this.mTitle = value;
            } else if (name.equalsIgnoreCase("artist") || name.startsWith("artist;")) {
                this.mArtist = value.trim();
            } else if (name.equalsIgnoreCase("albumartist") || name.startsWith("albumartist;") || name.equalsIgnoreCase("band") || name.startsWith("band;")) {
                this.mAlbumArtist = value.trim();
            } else if (name.equalsIgnoreCase("album") || name.startsWith("album;")) {
                this.mAlbum = value.trim();
            } else if (!name.equalsIgnoreCase(MediaStore.Audio.AudioColumns.COMPOSER) && !name.startsWith("composer;")) {
                if (MediaScanner.this.mProcessGenres && (name.equalsIgnoreCase(MediaStore.Audio.AudioColumns.GENRE) || name.startsWith("genre;"))) {
                    this.mGenre = getGenreName(value);
                    return;
                }
                if (name.equalsIgnoreCase(MediaStore.Audio.AudioColumns.YEAR) || name.startsWith("year;")) {
                    this.mYear = parseSubstring(value, 0, 0);
                } else if (name.equalsIgnoreCase("tracknumber") || name.startsWith("tracknumber;")) {
                    int num = parseSubstring(value, 0, 0);
                    this.mTrack = ((this.mTrack / 1000) * 1000) + num;
                } else if (name.equalsIgnoreCase("discnumber") || name.equals("set") || name.startsWith("set;")) {
                    int num2 = parseSubstring(value, 0, 0);
                    this.mTrack = (num2 * 1000) + (this.mTrack % 1000);
                } else if (name.equalsIgnoreCase("duration")) {
                    this.mDuration = parseSubstring(value, 0, 0);
                } else if (name.equalsIgnoreCase("writer") || name.startsWith("writer;")) {
                    this.mWriter = value.trim();
                } else if (name.equalsIgnoreCase(MediaStore.Audio.AudioColumns.COMPILATION)) {
                    this.mCompilation = parseSubstring(value, 0, 0);
                } else if (name.equalsIgnoreCase("isdrm")) {
                    this.mIsDrm = parseSubstring(value, 0, 0) == 1;
                } else if (name.equalsIgnoreCase("date")) {
                    this.mDate = parseDate(value);
                } else if (name.equalsIgnoreCase("width")) {
                    this.mWidth = parseSubstring(value, 0, 0);
                } else if (name.equalsIgnoreCase("height")) {
                    this.mHeight = parseSubstring(value, 0, 0);
                }
            } else {
                this.mComposer = value.trim();
            }
        }

        private synchronized boolean convertGenreCode(String input, String expected) {
            String output = getGenreName(input);
            if (output.equals(expected)) {
                return true;
            }
            Log.d(MediaScanner.TAG, "'" + input + "' -> '" + output + "', expected '" + expected + "'");
            return false;
        }

        private synchronized void testGenreNameConverter() {
            convertGenreCode("2", "Country");
            convertGenreCode("(2)", "Country");
            convertGenreCode("(2", "(2");
            convertGenreCode("2 Foo", "Country");
            convertGenreCode("(2) Foo", "Country");
            convertGenreCode("(2 Foo", "(2 Foo");
            convertGenreCode("2Foo", "2Foo");
            convertGenreCode("(2)Foo", "Country");
            convertGenreCode("200 Foo", "Foo");
            convertGenreCode("(200) Foo", "Foo");
            convertGenreCode("200Foo", "200Foo");
            convertGenreCode("(200)Foo", "Foo");
            convertGenreCode("200)Foo", "200)Foo");
            convertGenreCode("200) Foo", "200) Foo");
        }

        public synchronized String getGenreName(String genreTagValue) {
            if (genreTagValue == null) {
                return null;
            }
            int length = genreTagValue.length();
            if (length > 0) {
                boolean parenthesized = false;
                StringBuffer number = new StringBuffer();
                int i = 0;
                while (i < length) {
                    char c = genreTagValue.charAt(i);
                    if (i == 0 && c == '(') {
                        parenthesized = true;
                    } else if (!Character.isDigit(c)) {
                        break;
                    } else {
                        number.append(c);
                    }
                    i++;
                }
                char charAfterNumber = i < length ? genreTagValue.charAt(i) : ' ';
                if ((parenthesized && charAfterNumber == ')') || (!parenthesized && Character.isWhitespace(charAfterNumber))) {
                    try {
                        short genreIndex = Short.parseShort(number.toString());
                        if (genreIndex >= 0) {
                            if (genreIndex < MediaScanner.ID3_GENRES.length && MediaScanner.ID3_GENRES[genreIndex] != null) {
                                return MediaScanner.ID3_GENRES[genreIndex];
                            }
                            if (genreIndex == 255) {
                                return null;
                            }
                            if (genreIndex < 255 && i + 1 < length) {
                                if (parenthesized && charAfterNumber == ')') {
                                    i++;
                                }
                                String ret = genreTagValue.substring(i).trim();
                                if (ret.length() != 0) {
                                    return ret;
                                }
                            } else {
                                return number.toString();
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
            return genreTagValue;
        }

        private synchronized boolean processImageFile(String path) {
            try {
                MediaScanner.this.mBitmapOptions.outWidth = 0;
                MediaScanner.this.mBitmapOptions.outHeight = 0;
                BitmapFactory.decodeFile(path, MediaScanner.this.mBitmapOptions);
                this.mWidth = MediaScanner.this.mBitmapOptions.outWidth;
                this.mHeight = MediaScanner.this.mBitmapOptions.outHeight;
                if (this.mWidth > 0) {
                    return this.mHeight > 0;
                }
                return false;
            } catch (Throwable th) {
                return false;
            }
        }

        private protected void setMimeType(String mimeType) {
            if ("audio/mp4".equals(this.mMimeType) && mimeType.startsWith("video")) {
                return;
            }
            this.mMimeType = mimeType;
            this.mFileType = MediaFile.getFileTypeForMimeType(mimeType);
        }

        public protected ContentValues toValues() {
            ContentValues map = new ContentValues();
            map.put("_data", this.mPath);
            map.put("title", this.mTitle);
            map.put("date_modified", Long.valueOf(this.mLastModified));
            map.put("_size", Long.valueOf(this.mFileSize));
            map.put("mime_type", this.mMimeType);
            map.put("is_drm", Boolean.valueOf(this.mIsDrm));
            String resolution = null;
            if (this.mWidth > 0 && this.mHeight > 0) {
                map.put("width", Integer.valueOf(this.mWidth));
                map.put("height", Integer.valueOf(this.mHeight));
                resolution = this.mWidth + "x" + this.mHeight;
            }
            if (!this.mNoMedia) {
                if (MediaFile.isVideoFileType(this.mFileType)) {
                    map.put("artist", (this.mArtist == null || this.mArtist.length() <= 0) ? MediaStore.UNKNOWN_STRING : this.mArtist);
                    map.put("album", (this.mAlbum == null || this.mAlbum.length() <= 0) ? MediaStore.UNKNOWN_STRING : this.mAlbum);
                    map.put("duration", Integer.valueOf(this.mDuration));
                    if (resolution != null) {
                        map.put(MediaStore.Video.VideoColumns.RESOLUTION, resolution);
                    }
                    if (this.mDate > 0) {
                        map.put("datetaken", Long.valueOf(this.mDate));
                    }
                } else if (!MediaFile.isImageFileType(this.mFileType) && this.mScanSuccess && MediaFile.isAudioFileType(this.mFileType)) {
                    map.put("artist", (this.mArtist == null || this.mArtist.length() <= 0) ? MediaStore.UNKNOWN_STRING : this.mArtist);
                    map.put(MediaStore.Audio.AudioColumns.ALBUM_ARTIST, (this.mAlbumArtist == null || this.mAlbumArtist.length() <= 0) ? null : this.mAlbumArtist);
                    map.put("album", (this.mAlbum == null || this.mAlbum.length() <= 0) ? MediaStore.UNKNOWN_STRING : this.mAlbum);
                    map.put(MediaStore.Audio.AudioColumns.COMPOSER, this.mComposer);
                    map.put(MediaStore.Audio.AudioColumns.GENRE, this.mGenre);
                    if (this.mYear != 0) {
                        map.put(MediaStore.Audio.AudioColumns.YEAR, Integer.valueOf(this.mYear));
                    }
                    map.put(MediaStore.Audio.AudioColumns.TRACK, Integer.valueOf(this.mTrack));
                    map.put("duration", Integer.valueOf(this.mDuration));
                    map.put(MediaStore.Audio.AudioColumns.COMPILATION, Integer.valueOf(this.mCompilation));
                }
                if (!this.mScanSuccess) {
                    map.put("media_type", (Integer) 0);
                }
            }
            return map;
        }

        /* JADX WARN: Removed duplicated region for block: B:104:0x020a  */
        /* JADX WARN: Removed duplicated region for block: B:113:0x0233  */
        /* JADX WARN: Removed duplicated region for block: B:134:0x0292  */
        /* JADX WARN: Removed duplicated region for block: B:153:0x02e2  */
        /* JADX WARN: Removed duplicated region for block: B:75:0x018a  */
        /* JADX WARN: Removed duplicated region for block: B:79:0x0196  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x019d  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x01be  */
        /* JADX WARN: Removed duplicated region for block: B:96:0x01e4  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public protected android.net.Uri endFile(android.media.MediaScanner.FileEntry r19, boolean r20, boolean r21, boolean r22, boolean r23, boolean r24) throws android.os.RemoteException {
            /*
                Method dump skipped, instructions count: 781
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.MyMediaScannerClient.endFile(android.media.MediaScanner$FileEntry, boolean, boolean, boolean, boolean, boolean):android.net.Uri");
        }

        private synchronized boolean doesPathHaveFilename(String path, String filename) {
            int pathFilenameStart = path.lastIndexOf(File.separatorChar) + 1;
            int filenameLength = filename.length();
            return path.regionMatches(pathFilenameStart, filename, 0, filenameLength) && pathFilenameStart + filenameLength == path.length();
        }

        private synchronized void setRingtoneIfNotSet(String settingName, Uri uri, long rowId) {
            if (!MediaScanner.this.wasRingtoneAlreadySet(settingName)) {
                ContentResolver cr = MediaScanner.this.mContext.getContentResolver();
                String existingSettingValue = Settings.System.getString(cr, settingName);
                if (TextUtils.isEmpty(existingSettingValue)) {
                    Uri settingUri = Settings.System.getUriFor(settingName);
                    Uri ringtoneUri = ContentUris.withAppendedId(uri, rowId);
                    RingtoneManager.setActualDefaultRingtoneUri(MediaScanner.this.mContext, RingtoneManager.getDefaultType(settingUri), ringtoneUri);
                }
                Settings.System.putInt(cr, MediaScanner.this.settingSetIndicatorName(settingName), 1);
            }
        }

        public protected int getFileTypeFromDrm(String path) {
            if (MediaScanner.this.isDrmEnabled()) {
                if (MediaScanner.this.mDrmManagerClient == null) {
                    MediaScanner.this.mDrmManagerClient = new DrmManagerClient(MediaScanner.this.mContext);
                }
                if (!MediaScanner.this.mDrmManagerClient.canHandle(path, (String) null)) {
                    return 0;
                }
                this.mIsDrm = true;
                String drmMimetype = MediaScanner.this.mDrmManagerClient.getOriginalMimeType(path);
                if (drmMimetype == null) {
                    return 0;
                }
                this.mMimeType = drmMimetype;
                int resultFileType = MediaFile.getFileTypeForMimeType(drmMimetype);
                return resultFileType;
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isSystemSoundWithMetadata(String path) {
        if (path.startsWith("/system/media/audio/alarms/") || path.startsWith("/system/media/audio/ringtones/") || path.startsWith("/system/media/audio/notifications/") || path.startsWith("/product/media/audio/alarms/") || path.startsWith("/product/media/audio/ringtones/") || path.startsWith("/product/media/audio/notifications/")) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String settingSetIndicatorName(String base) {
        return base + "_set";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean wasRingtoneAlreadySet(String name) {
        ContentResolver cr = this.mContext.getContentResolver();
        String indicatorName = settingSetIndicatorName(name);
        try {
            return Settings.System.getInt(cr, indicatorName) != 0;
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:60:0x0145
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    public protected void prescan(java.lang.String r26, boolean r27) throws android.os.RemoteException {
        /*
            Method dump skipped, instructions count: 445
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.prescan(java.lang.String, boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class MediaBulkDeleter {
        final Uri mBaseUri;
        final ContentProviderClient mProvider;
        StringBuilder whereClause = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>(100);

        public synchronized MediaBulkDeleter(ContentProviderClient provider, Uri baseUri) {
            this.mProvider = provider;
            this.mBaseUri = baseUri;
        }

        public synchronized void delete(long id) throws RemoteException {
            if (this.whereClause.length() != 0) {
                this.whereClause.append(",");
            }
            this.whereClause.append("?");
            ArrayList<String> arrayList = this.whereArgs;
            arrayList.add("" + id);
            if (this.whereArgs.size() > 100) {
                flush();
            }
        }

        public synchronized void flush() throws RemoteException {
            int size = this.whereArgs.size();
            if (size > 0) {
                String[] foo = new String[size];
                String[] foo2 = (String[]) this.whereArgs.toArray(foo);
                ContentProviderClient contentProviderClient = this.mProvider;
                Uri uri = this.mBaseUri;
                contentProviderClient.delete(uri, "_id IN (" + this.whereClause.toString() + ")", foo2);
                this.whereClause.setLength(0);
                this.whereArgs.clear();
            }
        }
    }

    public protected void postscan(String[] directories) throws RemoteException {
        if (this.mProcessPlaylists) {
            processPlayLists();
        }
        this.mPlayLists.clear();
    }

    private synchronized void releaseResources() {
        if (this.mDrmManagerClient != null) {
            this.mDrmManagerClient.close();
            this.mDrmManagerClient = null;
        }
    }

    public synchronized void scanDirectories(String[] directories) {
        try {
            try {
                try {
                    System.currentTimeMillis();
                    prescan(null, true);
                    System.currentTimeMillis();
                    this.mMediaInserter = new MediaInserter(this.mMediaProvider, 500);
                    for (String str : directories) {
                        processDirectory(str, this.mClient);
                    }
                    this.mMediaInserter.flushAll();
                    this.mMediaInserter = null;
                    System.currentTimeMillis();
                    postscan(directories);
                    System.currentTimeMillis();
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in MediaScanner.scan()", e);
                }
            } catch (SQLException e2) {
                Log.e(TAG, "SQLException in MediaScanner.scan()", e2);
            } catch (UnsupportedOperationException e3) {
                Log.e(TAG, "UnsupportedOperationException in MediaScanner.scan()", e3);
            }
        } finally {
            releaseResources();
        }
    }

    private protected Uri scanSingleFile(String path, String mimeType) {
        try {
            prescan(path, true);
            File file = new File(path);
            if (file.exists() && file.canRead()) {
                long lastModifiedSeconds = file.lastModified() / 1000;
                return this.mClient.doScanFile(path, mimeType, lastModifiedSeconds, file.length(), false, true, isNoMediaPath(path));
            }
            return null;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in MediaScanner.scanFile()", e);
            return null;
        } finally {
            releaseResources();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isNoMediaFile(String path) {
        int lastSlash;
        File file = new File(path);
        if (!file.isDirectory() && (lastSlash = path.lastIndexOf(47)) >= 0 && lastSlash + 2 < path.length()) {
            if (path.regionMatches(lastSlash + 1, "._", 0, 2)) {
                return true;
            }
            if (path.regionMatches(true, path.length() - 4, ".jpg", 0, 4)) {
                if (path.regionMatches(true, lastSlash + 1, "AlbumArt_{", 0, 10) || path.regionMatches(true, lastSlash + 1, "AlbumArt.", 0, 9)) {
                    return true;
                }
                int length = (path.length() - lastSlash) - 1;
                if ((length == 17 && path.regionMatches(true, lastSlash + 1, "AlbumArtSmall", 0, 13)) || (length == 10 && path.regionMatches(true, lastSlash + 1, "Folder", 0, 6))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static synchronized void clearMediaPathCache(boolean clearMediaPaths, boolean clearNoMediaPaths) {
        synchronized (MediaScanner.class) {
            if (clearMediaPaths) {
                try {
                    mMediaPaths.clear();
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (clearNoMediaPaths) {
                mNoMediaPaths.clear();
            }
        }
    }

    private protected static boolean isNoMediaPath(String path) {
        if (path == null) {
            return false;
        }
        if (path.indexOf("/.") >= 0) {
            return true;
        }
        int firstSlash = path.lastIndexOf(47);
        if (firstSlash <= 0) {
            return false;
        }
        String parent = path.substring(0, firstSlash);
        synchronized (MediaScanner.class) {
            if (mNoMediaPaths.containsKey(parent)) {
                return true;
            }
            if (!mMediaPaths.containsKey(parent)) {
                int offset = 1;
                while (offset >= 0) {
                    int slashIndex = path.indexOf(47, offset);
                    if (slashIndex > offset) {
                        slashIndex++;
                        File file = new File(path.substring(0, slashIndex) + MediaStore.MEDIA_IGNORE_FILENAME);
                        if (file.exists()) {
                            mNoMediaPaths.put(parent, "");
                            return true;
                        }
                    }
                    offset = slashIndex;
                }
                mMediaPaths.put(parent, "");
            }
            return isNoMediaFile(path);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0137  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void scanMtpFile(java.lang.String r25, int r26, int r27) {
        /*
            Method dump skipped, instructions count: 318
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.scanMtpFile(java.lang.String, int, int):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x003d, code lost:
        r3.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0049, code lost:
        if (r3 == null) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004c, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x003b, code lost:
        if (r3 != null) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public private protected android.media.MediaScanner.FileEntry makeEntryFor(java.lang.String r18) {
        /*
            r17 = this;
            r1 = r17
            r2 = 0
            r3 = r2
            java.lang.String r7 = "_data=?"
            r0 = 1
            java.lang.String[] r8 = new java.lang.String[r0]     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            r0 = 0
            r8[r0] = r18     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            android.content.ContentProviderClient r4 = r1.mMediaProvider     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            android.net.Uri r5 = r1.mFilesUriNoNotify     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            java.lang.String[] r6 = android.media.MediaScanner.FILES_PRESCAN_PROJECTION     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            r9 = 0
            r10 = 0
            android.database.Cursor r4 = r4.query(r5, r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            r3 = r4
            boolean r4 = r3.moveToFirst()     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            if (r4 == 0) goto L3b
            long r10 = r3.getLong(r0)     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            r0 = 2
            int r15 = r3.getInt(r0)     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            r0 = 3
            long r13 = r3.getLong(r0)     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            android.media.MediaScanner$FileEntry r0 = new android.media.MediaScanner$FileEntry     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            r9 = r0
            r12 = r18
            r9.<init>(r10, r12, r13, r15)     // Catch: java.lang.Throwable -> L41 android.os.RemoteException -> L48
            if (r3 == 0) goto L3a
            r3.close()
        L3a:
            return r0
        L3b:
            if (r3 == 0) goto L4c
        L3d:
            r3.close()
            goto L4c
        L41:
            r0 = move-exception
            if (r3 == 0) goto L47
            r3.close()
        L47:
            throw r0
        L48:
            r0 = move-exception
            if (r3 == 0) goto L4c
            goto L3d
        L4c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaScanner.makeEntryFor(java.lang.String):android.media.MediaScanner$FileEntry");
    }

    private synchronized int matchPaths(String path1, String path2) {
        int start2;
        int end1 = path1.length();
        int end2 = path2.length();
        int result = 0;
        int end12 = end1;
        while (true) {
            int end22 = end2;
            if (end12 <= 0 || end22 <= 0) {
                break;
            }
            int slash1 = path1.lastIndexOf(47, end12 - 1);
            int slash2 = path2.lastIndexOf(47, end22 - 1);
            int backSlash1 = path1.lastIndexOf(92, end12 - 1);
            int backSlash2 = path2.lastIndexOf(92, end22 - 1);
            int start1 = slash1 > backSlash1 ? slash1 : backSlash1;
            int start22 = slash2 > backSlash2 ? slash2 : backSlash2;
            int start12 = start1 < 0 ? 0 : start1 + 1;
            if (start22 >= 0) {
                start2 = start22 + 1;
            } else {
                start2 = 0;
            }
            int length = end12 - start12;
            if (end22 - start2 != length || !path1.regionMatches(true, start12, path2, start2, length)) {
                break;
            }
            result++;
            end12 = start12 - 1;
            end2 = start2 - 1;
        }
        return result;
    }

    private synchronized boolean matchEntries(long rowId, String data) {
        int len = this.mPlaylistEntries.size();
        boolean done = true;
        for (int i = 0; i < len; i++) {
            PlaylistEntry entry = this.mPlaylistEntries.get(i);
            if (entry.bestmatchlevel != Integer.MAX_VALUE) {
                done = false;
                if (data.equalsIgnoreCase(entry.path)) {
                    entry.bestmatchid = rowId;
                    entry.bestmatchlevel = Integer.MAX_VALUE;
                } else {
                    int matchLength = matchPaths(data, entry.path);
                    if (matchLength > entry.bestmatchlevel) {
                        entry.bestmatchid = rowId;
                        entry.bestmatchlevel = matchLength;
                    }
                }
            }
        }
        return done;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void cachePlaylistEntry(String line, String playListDirectory) {
        PlaylistEntry entry = new PlaylistEntry();
        int entryLength = line.length();
        while (entryLength > 0 && Character.isWhitespace(line.charAt(entryLength - 1))) {
            entryLength--;
        }
        if (entryLength < 3) {
            return;
        }
        boolean fullPath = false;
        if (entryLength < line.length()) {
            line = line.substring(0, entryLength);
        }
        char ch1 = line.charAt(0);
        if (ch1 == '/' || (Character.isLetter(ch1) && line.charAt(1) == ':' && line.charAt(2) == '\\')) {
            fullPath = true;
        }
        if (!fullPath) {
            line = playListDirectory + line;
        }
        entry.path = line;
        this.mPlaylistEntries.add(entry);
    }

    private synchronized void processCachedPlaylist(Cursor fileList, ContentValues values, Uri playlistUri) {
        int i;
        long rowId;
        String data;
        fileList.moveToPosition(-1);
        do {
            if (!fileList.moveToNext()) {
                break;
            }
            rowId = fileList.getLong(0);
            data = fileList.getString(1);
        } while (!matchEntries(rowId, data));
        int len = this.mPlaylistEntries.size();
        int index = 0;
        for (i = 0; i < len; i++) {
            PlaylistEntry entry = this.mPlaylistEntries.get(i);
            if (entry.bestmatchlevel > 0) {
                try {
                    values.clear();
                    values.put("play_order", Integer.valueOf(index));
                    values.put("audio_id", Long.valueOf(entry.bestmatchid));
                    this.mMediaProvider.insert(playlistUri, values);
                    index++;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in MediaScanner.processCachedPlaylist()", e);
                    return;
                }
            }
        }
        this.mPlaylistEntries.clear();
    }

    private synchronized void processM3uPlayList(String path, String playListDirectory, Uri uri, ContentValues values, Cursor fileList) {
        BufferedReader reader = null;
        try {
            try {
                try {
                    File f = new File(path);
                    if (f.exists()) {
                        reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)), 8192);
                        this.mPlaylistEntries.clear();
                        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                            if (line.length() > 0 && line.charAt(0) != '#') {
                                cachePlaylistEntry(line, playListDirectory);
                            }
                        }
                        processCachedPlaylist(fileList, values, uri);
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException in MediaScanner.processM3uPlayList()", e);
                }
            } catch (IOException e2) {
                Log.e(TAG, "IOException in MediaScanner.processM3uPlayList()", e2);
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    reader.close();
                } catch (IOException e3) {
                    Log.e(TAG, "IOException in MediaScanner.processM3uPlayList()", e3);
                }
            }
            throw th;
        }
    }

    private synchronized void processPlsPlayList(String path, String playListDirectory, Uri uri, ContentValues values, Cursor fileList) {
        int equals;
        BufferedReader reader = null;
        try {
            try {
                try {
                    File f = new File(path);
                    if (f.exists()) {
                        reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)), 8192);
                        this.mPlaylistEntries.clear();
                        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                            if (line.startsWith("File") && (equals = line.indexOf(61)) > 0) {
                                cachePlaylistEntry(line.substring(equals + 1), playListDirectory);
                            }
                        }
                        processCachedPlaylist(fileList, values, uri);
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException in MediaScanner.processPlsPlayList()", e);
                }
            } catch (IOException e2) {
                Log.e(TAG, "IOException in MediaScanner.processPlsPlayList()", e2);
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    reader.close();
                } catch (IOException e3) {
                    Log.e(TAG, "IOException in MediaScanner.processPlsPlayList()", e3);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class WplHandler implements ElementListener {
        final ContentHandler handler;
        String playListDirectory;

        public WplHandler(String playListDirectory, Uri uri, Cursor fileList) {
            this.playListDirectory = playListDirectory;
            RootElement root = new RootElement("smil");
            Element body = root.getChild("body");
            Element seq = body.getChild("seq");
            Element media = seq.getChild(MediaStore.AUTHORITY);
            media.setElementListener(this);
            this.handler = root.getContentHandler();
        }

        @Override // android.sax.StartElementListener
        public void start(Attributes attributes) {
            String path = attributes.getValue("", "src");
            if (path != null) {
                MediaScanner.this.cachePlaylistEntry(path, this.playListDirectory);
            }
        }

        @Override // android.sax.EndElementListener
        public void end() {
        }

        synchronized ContentHandler getContentHandler() {
            return this.handler;
        }
    }

    private synchronized void processWplPlayList(String path, String playListDirectory, Uri uri, ContentValues values, Cursor fileList) {
        FileInputStream fis = null;
        try {
            try {
                try {
                    try {
                        File f = new File(path);
                        if (f.exists()) {
                            fis = new FileInputStream(f);
                            this.mPlaylistEntries.clear();
                            Xml.parse(fis, Xml.findEncodingByName("UTF-8"), new WplHandler(playListDirectory, uri, fileList).getContentHandler());
                            processCachedPlaylist(fileList, values, uri);
                        }
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (SAXException e) {
                        e.printStackTrace();
                        if (fis != null) {
                            fis.close();
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    if (fis != null) {
                        fis.close();
                    }
                }
            } catch (IOException e3) {
                Log.e(TAG, "IOException in MediaScanner.processWplPlayList()", e3);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    fis.close();
                } catch (IOException e4) {
                    Log.e(TAG, "IOException in MediaScanner.processWplPlayList()", e4);
                }
            }
            throw th;
        }
    }

    private synchronized void processPlayList(FileEntry entry, Cursor fileList) throws RemoteException {
        Uri uri;
        Uri membersUri;
        String path = entry.mPath;
        ContentValues values = new ContentValues();
        int lastSlash = path.lastIndexOf(47);
        if (lastSlash < 0) {
            throw new IllegalArgumentException("bad path " + path);
        }
        long rowId = entry.mRowId;
        String name = values.getAsString("name");
        if (name == null && (name = values.getAsString("title")) == null) {
            int lastDot = path.lastIndexOf(46);
            name = lastDot < 0 ? path.substring(lastSlash + 1) : path.substring(lastSlash + 1, lastDot);
        }
        values.put("name", name);
        values.put("date_modified", Long.valueOf(entry.mLastModified));
        if (rowId == 0) {
            values.put("_data", path);
            uri = this.mMediaProvider.insert(this.mPlaylistsUri, values);
            rowId = ContentUris.parseId(uri);
            membersUri = Uri.withAppendedPath(uri, "members");
        } else {
            uri = ContentUris.withAppendedId(this.mPlaylistsUri, rowId);
            this.mMediaProvider.update(uri, values, null, null);
            membersUri = Uri.withAppendedPath(uri, "members");
            this.mMediaProvider.delete(membersUri, null, null);
        }
        Uri membersUri2 = membersUri;
        String playListDirectory = path.substring(0, lastSlash + 1);
        MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(path);
        int fileType = mediaFileType != null ? mediaFileType.fileType : 0;
        if (fileType == 41) {
            processM3uPlayList(path, playListDirectory, membersUri2, values, fileList);
        } else if (fileType != 42) {
            if (fileType == 43) {
                processWplPlayList(path, playListDirectory, membersUri2, values, fileList);
            }
        } else {
            processPlsPlayList(path, playListDirectory, membersUri2, values, fileList);
        }
    }

    private synchronized void processPlayLists() throws RemoteException {
        Iterator<FileEntry> iterator = this.mPlayLists.iterator();
        Cursor fileList = null;
        try {
            fileList = this.mMediaProvider.query(this.mFilesUri, FILES_PRESCAN_PROJECTION, "media_type=2", null, null, null);
            while (iterator.hasNext()) {
                FileEntry entry = iterator.next();
                if (entry.mLastModifiedChanged) {
                    processPlayList(entry, fileList);
                }
            }
            if (fileList == null) {
                return;
            }
        } catch (RemoteException e) {
            if (fileList == null) {
                return;
            }
        } catch (Throwable th) {
            if (fileList != null) {
                fileList.close();
            }
            throw th;
        }
        fileList.close();
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mCloseGuard.close();
        if (this.mClosed.compareAndSet(false, true)) {
            this.mMediaProvider.close();
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
}
