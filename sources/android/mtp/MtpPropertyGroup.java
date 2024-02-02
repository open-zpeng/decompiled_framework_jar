package android.mtp;

import android.content.ContentProviderClient;
import android.database.Cursor;
import android.mtp.MtpStorageManager;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import java.util.ArrayList;
/* loaded from: classes2.dex */
class MtpPropertyGroup {
    private static final String PATH_WHERE = "_data=?";
    private static final String TAG = MtpPropertyGroup.class.getSimpleName();
    private String[] mColumns;
    private final Property[] mProperties;
    private final ContentProviderClient mProvider;
    private final Uri mUri;
    private final String mVolumeName;

    private native String format_date_time(long j);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class Property {
        int code;
        int column;
        int type;

        Property(int code, int type, int column) {
            this.code = code;
            this.type = type;
            this.column = column;
        }
    }

    public synchronized MtpPropertyGroup(ContentProviderClient provider, String volumeName, int[] properties) {
        this.mProvider = provider;
        this.mVolumeName = volumeName;
        this.mUri = MediaStore.Files.getMtpObjectsUri(volumeName);
        int count = properties.length;
        ArrayList<String> columns = new ArrayList<>(count);
        columns.add("_id");
        this.mProperties = new Property[count];
        for (int i = 0; i < count; i++) {
            this.mProperties[i] = createProperty(properties[i], columns);
        }
        int count2 = columns.size();
        this.mColumns = new String[count2];
        for (int i2 = 0; i2 < count2; i2++) {
            this.mColumns[i2] = columns.get(i2);
        }
    }

    private synchronized Property createProperty(int code, ArrayList<String> columns) {
        int type;
        String column = null;
        switch (code) {
            case MtpConstants.PROPERTY_STORAGE_ID /* 56321 */:
                type = 6;
                break;
            case MtpConstants.PROPERTY_OBJECT_FORMAT /* 56322 */:
                type = 4;
                break;
            case MtpConstants.PROPERTY_PROTECTION_STATUS /* 56323 */:
                type = 4;
                break;
            case MtpConstants.PROPERTY_OBJECT_SIZE /* 56324 */:
                type = 8;
                break;
            case MtpConstants.PROPERTY_OBJECT_FILE_NAME /* 56327 */:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DATE_MODIFIED /* 56329 */:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_PARENT_OBJECT /* 56331 */:
                type = 6;
                break;
            case MtpConstants.PROPERTY_PERSISTENT_UID /* 56385 */:
                type = 10;
                break;
            case MtpConstants.PROPERTY_NAME /* 56388 */:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ARTIST /* 56390 */:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DESCRIPTION /* 56392 */:
                column = "description";
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DATE_ADDED /* 56398 */:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DURATION /* 56457 */:
                column = "duration";
                type = 6;
                break;
            case MtpConstants.PROPERTY_TRACK /* 56459 */:
                column = MediaStore.Audio.AudioColumns.TRACK;
                type = 4;
                break;
            case MtpConstants.PROPERTY_GENRE /* 56460 */:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_COMPOSER /* 56470 */:
                column = MediaStore.Audio.AudioColumns.COMPOSER;
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE /* 56473 */:
                column = MediaStore.Audio.AudioColumns.YEAR;
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ALBUM_NAME /* 56474 */:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_ALBUM_ARTIST /* 56475 */:
                column = MediaStore.Audio.AudioColumns.ALBUM_ARTIST;
                type = 65535;
                break;
            case MtpConstants.PROPERTY_DISPLAY_NAME /* 56544 */:
                type = 65535;
                break;
            case MtpConstants.PROPERTY_BITRATE_TYPE /* 56978 */:
            case MtpConstants.PROPERTY_NUMBER_OF_CHANNELS /* 56980 */:
                type = 4;
                break;
            case MtpConstants.PROPERTY_SAMPLE_RATE /* 56979 */:
            case MtpConstants.PROPERTY_AUDIO_WAVE_CODEC /* 56985 */:
            case MtpConstants.PROPERTY_AUDIO_BITRATE /* 56986 */:
                type = 6;
                break;
            default:
                type = 0;
                String str = TAG;
                Log.e(str, "unsupported property " + code);
                break;
        }
        if (column != null) {
            columns.add(column);
            return new Property(code, type, columns.size() - 1);
        }
        return new Property(code, type, -1);
    }

    private synchronized String queryAudio(String path, String column) {
        Cursor c = null;
        try {
            c = this.mProvider.query(MediaStore.Audio.Media.getContentUri(this.mVolumeName), new String[]{column}, PATH_WHERE, new String[]{path}, null, null);
            if (c == null || !c.moveToNext()) {
                if (c != null) {
                    c.close();
                }
                return "";
            }
            String string = c.getString(0);
            if (c != null) {
                c.close();
            }
            return string;
        } catch (Exception e) {
            if (c != null) {
                c.close();
            }
            return "";
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
            throw th;
        }
    }

    private synchronized String queryGenre(String path) {
        Cursor c = null;
        try {
            c = this.mProvider.query(MediaStore.Audio.Genres.getContentUri(this.mVolumeName), new String[]{"name"}, PATH_WHERE, new String[]{path}, null, null);
            if (c == null || !c.moveToNext()) {
                if (c != null) {
                    c.close();
                }
                return "";
            }
            String string = c.getString(0);
            if (c != null) {
                c.close();
            }
            return string;
        } catch (Exception e) {
            if (c != null) {
                c.close();
            }
            return "";
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
            throw th;
        }
    }

    public synchronized int getPropertyList(MtpStorageManager.MtpObject object, MtpPropertyList list) {
        int id = object.getId();
        String path = object.getPath().toString();
        Property[] propertyArr = this.mProperties;
        int length = propertyArr.length;
        Cursor c = null;
        int i = 0;
        while (i < length) {
            Property property = propertyArr[i];
            if (property.column != -1 && c == null) {
                try {
                    c = this.mProvider.query(this.mUri, this.mColumns, PATH_WHERE, new String[]{path}, null, null);
                    if (c != null && !c.moveToNext()) {
                        c.close();
                        c = null;
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "Mediaprovider lookup failed");
                }
            }
            Cursor c2 = c;
            switch (property.code) {
                case MtpConstants.PROPERTY_STORAGE_ID /* 56321 */:
                    list.append(id, property.code, property.type, object.getStorageId());
                    break;
                case MtpConstants.PROPERTY_OBJECT_FORMAT /* 56322 */:
                    list.append(id, property.code, property.type, object.getFormat());
                    break;
                case MtpConstants.PROPERTY_PROTECTION_STATUS /* 56323 */:
                    list.append(id, property.code, property.type, 0L);
                    break;
                case MtpConstants.PROPERTY_OBJECT_SIZE /* 56324 */:
                    list.append(id, property.code, property.type, object.getSize());
                    break;
                case MtpConstants.PROPERTY_OBJECT_FILE_NAME /* 56327 */:
                case MtpConstants.PROPERTY_NAME /* 56388 */:
                case MtpConstants.PROPERTY_DISPLAY_NAME /* 56544 */:
                    list.append(id, property.code, object.getName());
                    break;
                case MtpConstants.PROPERTY_DATE_MODIFIED /* 56329 */:
                case MtpConstants.PROPERTY_DATE_ADDED /* 56398 */:
                    list.append(id, property.code, format_date_time(object.getModifiedTime()));
                    break;
                case MtpConstants.PROPERTY_PARENT_OBJECT /* 56331 */:
                    list.append(id, property.code, property.type, object.getParent().isRoot() ? 0L : object.getParent().getId());
                    break;
                case MtpConstants.PROPERTY_PERSISTENT_UID /* 56385 */:
                    long puid = (object.getPath().toString().hashCode() << 32) + object.getModifiedTime();
                    list.append(id, property.code, property.type, puid);
                    break;
                case MtpConstants.PROPERTY_ARTIST /* 56390 */:
                    list.append(id, property.code, queryAudio(path, "artist"));
                    break;
                case MtpConstants.PROPERTY_TRACK /* 56459 */:
                    int track = 0;
                    if (c2 != null) {
                        track = c2.getInt(property.column);
                    }
                    list.append(id, property.code, 4, track % 1000);
                    break;
                case MtpConstants.PROPERTY_GENRE /* 56460 */:
                    String genre = queryGenre(path);
                    if (genre == null) {
                        break;
                    } else {
                        list.append(id, property.code, genre);
                        break;
                    }
                case MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE /* 56473 */:
                    int year = 0;
                    if (c2 != null) {
                        year = c2.getInt(property.column);
                    }
                    String dateTime = Integer.toString(year) + "0101T000000";
                    list.append(id, property.code, dateTime);
                    break;
                case MtpConstants.PROPERTY_ALBUM_NAME /* 56474 */:
                    list.append(id, property.code, queryAudio(path, "album"));
                    break;
                case MtpConstants.PROPERTY_BITRATE_TYPE /* 56978 */:
                case MtpConstants.PROPERTY_NUMBER_OF_CHANNELS /* 56980 */:
                    list.append(id, property.code, 4, 0L);
                    break;
                case MtpConstants.PROPERTY_SAMPLE_RATE /* 56979 */:
                case MtpConstants.PROPERTY_AUDIO_WAVE_CODEC /* 56985 */:
                case MtpConstants.PROPERTY_AUDIO_BITRATE /* 56986 */:
                    list.append(id, property.code, 6, 0L);
                    break;
                default:
                    int i2 = property.type;
                    if (i2 == 0) {
                        list.append(id, property.code, property.type, 0L);
                        break;
                    } else if (i2 == 65535) {
                        String value = "";
                        if (c2 != null) {
                            value = c2.getString(property.column);
                        }
                        list.append(id, property.code, value);
                        break;
                    } else {
                        long longValue = 0;
                        if (c2 != null) {
                            longValue = c2.getLong(property.column);
                        }
                        list.append(id, property.code, property.type, longValue);
                        break;
                    }
            }
            i++;
            c = c2;
        }
        if (c != null) {
            c.close();
            return MtpConstants.RESPONSE_OK;
        }
        return MtpConstants.RESPONSE_OK;
    }
}
