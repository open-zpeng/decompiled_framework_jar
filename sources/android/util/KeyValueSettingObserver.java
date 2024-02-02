package android.util;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
/* loaded from: classes2.dex */
public abstract class KeyValueSettingObserver {
    private static final String TAG = "KeyValueSettingObserver";
    private final ContentObserver mObserver;
    private final KeyValueListParser mParser = new KeyValueListParser(',');
    private final ContentResolver mResolver;
    private final Uri mSettingUri;

    public abstract synchronized String getSettingValue(ContentResolver contentResolver);

    public abstract synchronized void update(KeyValueListParser keyValueListParser);

    public synchronized KeyValueSettingObserver(Handler handler, ContentResolver resolver, Uri uri) {
        this.mObserver = new SettingObserver(handler);
        this.mResolver = resolver;
        this.mSettingUri = uri;
    }

    public synchronized void start() {
        this.mResolver.registerContentObserver(this.mSettingUri, false, this.mObserver);
        setParserValue();
        update(this.mParser);
    }

    public synchronized void stop() {
        this.mResolver.unregisterContentObserver(this.mObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setParserValue() {
        String setting = getSettingValue(this.mResolver);
        try {
            this.mParser.setString(setting);
        } catch (IllegalArgumentException e) {
            Slog.e(TAG, "Malformed setting: " + setting);
        }
    }

    /* loaded from: classes2.dex */
    private class SettingObserver extends ContentObserver {
        private SettingObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            KeyValueSettingObserver.this.setParserValue();
            KeyValueSettingObserver.this.update(KeyValueSettingObserver.this.mParser);
        }
    }
}
