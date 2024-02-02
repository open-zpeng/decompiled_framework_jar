package android.view.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
/* loaded from: classes2.dex */
public class CaptioningManager {
    private static final int DEFAULT_ENABLED = 0;
    private static final float DEFAULT_FONT_SCALE = 1.0f;
    private static final int DEFAULT_PRESET = 0;
    private final ContentObserver mContentObserver;
    private final ContentResolver mContentResolver;
    private final ArrayList<CaptioningChangeListener> mListeners = new ArrayList<>();
    private final Runnable mStyleChangedRunnable = new Runnable() { // from class: android.view.accessibility.CaptioningManager.1
        @Override // java.lang.Runnable
        public void run() {
            CaptioningManager.this.notifyUserStyleChanged();
        }
    };

    public synchronized CaptioningManager(Context context) {
        this.mContentResolver = context.getContentResolver();
        Handler handler = new Handler(context.getMainLooper());
        this.mContentObserver = new MyContentObserver(handler);
    }

    public final boolean isEnabled() {
        return Settings.Secure.getInt(this.mContentResolver, Settings.Secure.ACCESSIBILITY_CAPTIONING_ENABLED, 0) == 1;
    }

    public final synchronized String getRawLocale() {
        return Settings.Secure.getString(this.mContentResolver, Settings.Secure.ACCESSIBILITY_CAPTIONING_LOCALE);
    }

    public final Locale getLocale() {
        String rawLocale = getRawLocale();
        if (!TextUtils.isEmpty(rawLocale)) {
            String[] splitLocale = rawLocale.split("_");
            switch (splitLocale.length) {
                case 1:
                    return new Locale(splitLocale[0]);
                case 2:
                    return new Locale(splitLocale[0], splitLocale[1]);
                case 3:
                    return new Locale(splitLocale[0], splitLocale[1], splitLocale[2]);
                default:
                    return null;
            }
        }
        return null;
    }

    public final float getFontScale() {
        return Settings.Secure.getFloat(this.mContentResolver, Settings.Secure.ACCESSIBILITY_CAPTIONING_FONT_SCALE, 1.0f);
    }

    public synchronized int getRawUserStyle() {
        return Settings.Secure.getInt(this.mContentResolver, Settings.Secure.ACCESSIBILITY_CAPTIONING_PRESET, 0);
    }

    public CaptionStyle getUserStyle() {
        int preset = getRawUserStyle();
        if (preset == -1) {
            return CaptionStyle.getCustomStyle(this.mContentResolver);
        }
        return CaptionStyle.PRESETS[preset];
    }

    public void addCaptioningChangeListener(CaptioningChangeListener listener) {
        synchronized (this.mListeners) {
            if (this.mListeners.isEmpty()) {
                registerObserver(Settings.Secure.ACCESSIBILITY_CAPTIONING_ENABLED);
                registerObserver(Settings.Secure.ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR);
                registerObserver(Settings.Secure.ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR);
                registerObserver(Settings.Secure.ACCESSIBILITY_CAPTIONING_WINDOW_COLOR);
                registerObserver(Settings.Secure.ACCESSIBILITY_CAPTIONING_EDGE_TYPE);
                registerObserver(Settings.Secure.ACCESSIBILITY_CAPTIONING_EDGE_COLOR);
                registerObserver("accessibility_captioning_typeface");
                registerObserver(Settings.Secure.ACCESSIBILITY_CAPTIONING_FONT_SCALE);
                registerObserver(Settings.Secure.ACCESSIBILITY_CAPTIONING_LOCALE);
                registerObserver(Settings.Secure.ACCESSIBILITY_CAPTIONING_PRESET);
            }
            this.mListeners.add(listener);
        }
    }

    private synchronized void registerObserver(String key) {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(key), false, this.mContentObserver);
    }

    public void removeCaptioningChangeListener(CaptioningChangeListener listener) {
        synchronized (this.mListeners) {
            this.mListeners.remove(listener);
            if (this.mListeners.isEmpty()) {
                this.mContentResolver.unregisterContentObserver(this.mContentObserver);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void notifyEnabledChanged() {
        boolean enabled = isEnabled();
        synchronized (this.mListeners) {
            Iterator<CaptioningChangeListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                CaptioningChangeListener listener = it.next();
                listener.onEnabledChanged(enabled);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void notifyUserStyleChanged() {
        CaptionStyle userStyle = getUserStyle();
        synchronized (this.mListeners) {
            Iterator<CaptioningChangeListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                CaptioningChangeListener listener = it.next();
                listener.onUserStyleChanged(userStyle);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void notifyLocaleChanged() {
        Locale locale = getLocale();
        synchronized (this.mListeners) {
            Iterator<CaptioningChangeListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                CaptioningChangeListener listener = it.next();
                listener.onLocaleChanged(locale);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void notifyFontScaleChanged() {
        float fontScale = getFontScale();
        synchronized (this.mListeners) {
            Iterator<CaptioningChangeListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                CaptioningChangeListener listener = it.next();
                listener.onFontScaleChanged(fontScale);
            }
        }
    }

    /* loaded from: classes2.dex */
    private class MyContentObserver extends ContentObserver {
        private final Handler mHandler;

        public MyContentObserver(Handler handler) {
            super(handler);
            this.mHandler = handler;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            String uriPath = uri.getPath();
            String name = uriPath.substring(uriPath.lastIndexOf(47) + 1);
            if (Settings.Secure.ACCESSIBILITY_CAPTIONING_ENABLED.equals(name)) {
                CaptioningManager.this.notifyEnabledChanged();
            } else if (Settings.Secure.ACCESSIBILITY_CAPTIONING_LOCALE.equals(name)) {
                CaptioningManager.this.notifyLocaleChanged();
            } else if (Settings.Secure.ACCESSIBILITY_CAPTIONING_FONT_SCALE.equals(name)) {
                CaptioningManager.this.notifyFontScaleChanged();
            } else {
                this.mHandler.removeCallbacks(CaptioningManager.this.mStyleChangedRunnable);
                this.mHandler.post(CaptioningManager.this.mStyleChangedRunnable);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class CaptionStyle {
        private static final int COLOR_NONE_OPAQUE = 255;
        public static final int COLOR_UNSPECIFIED = 16777215;
        public static final int EDGE_TYPE_DEPRESSED = 4;
        public static final int EDGE_TYPE_DROP_SHADOW = 2;
        public static final int EDGE_TYPE_NONE = 0;
        public static final int EDGE_TYPE_OUTLINE = 1;
        public static final int EDGE_TYPE_RAISED = 3;
        public static final int EDGE_TYPE_UNSPECIFIED = -1;
        public static final int PRESET_CUSTOM = -1;
        public final int backgroundColor;
        public final int edgeColor;
        public final int edgeType;
        public final int foregroundColor;
        private final boolean mHasBackgroundColor;
        private final boolean mHasEdgeColor;
        private final boolean mHasEdgeType;
        private final boolean mHasForegroundColor;
        private final boolean mHasWindowColor;
        private Typeface mParsedTypeface;
        public final String mRawTypeface;
        public final int windowColor;
        private static final CaptionStyle WHITE_ON_BLACK = new CaptionStyle(-1, -16777216, 0, -16777216, 255, null);
        private static final CaptionStyle BLACK_ON_WHITE = new CaptionStyle(-16777216, -1, 0, -16777216, 255, null);
        private static final CaptionStyle YELLOW_ON_BLACK = new CaptionStyle(-256, -16777216, 0, -16777216, 255, null);
        private static final CaptionStyle YELLOW_ON_BLUE = new CaptionStyle(-256, -16776961, 0, -16777216, 255, null);
        private static final CaptionStyle UNSPECIFIED = new CaptionStyle(16777215, 16777215, -1, 16777215, 16777215, null);
        private protected static final CaptionStyle[] PRESETS = {WHITE_ON_BLACK, BLACK_ON_WHITE, YELLOW_ON_BLACK, YELLOW_ON_BLUE, UNSPECIFIED};
        private static final CaptionStyle DEFAULT_CUSTOM = WHITE_ON_BLACK;
        public static final CaptionStyle DEFAULT = WHITE_ON_BLACK;

        private synchronized CaptionStyle(int foregroundColor, int backgroundColor, int edgeType, int edgeColor, int windowColor, String rawTypeface) {
            this.mHasForegroundColor = hasColor(foregroundColor);
            this.mHasBackgroundColor = hasColor(backgroundColor);
            this.mHasEdgeType = edgeType != -1;
            this.mHasEdgeColor = hasColor(edgeColor);
            this.mHasWindowColor = hasColor(windowColor);
            this.foregroundColor = this.mHasForegroundColor ? foregroundColor : -1;
            this.backgroundColor = this.mHasBackgroundColor ? backgroundColor : -16777216;
            this.edgeType = this.mHasEdgeType ? edgeType : 0;
            this.edgeColor = this.mHasEdgeColor ? edgeColor : -16777216;
            this.windowColor = this.mHasWindowColor ? windowColor : 255;
            this.mRawTypeface = rawTypeface;
        }

        public static synchronized boolean hasColor(int packedColor) {
            return (packedColor >>> 24) != 0 || (16776960 & packedColor) == 0;
        }

        public synchronized CaptionStyle applyStyle(CaptionStyle overlay) {
            int newForegroundColor = overlay.hasForegroundColor() ? overlay.foregroundColor : this.foregroundColor;
            int newBackgroundColor = overlay.hasBackgroundColor() ? overlay.backgroundColor : this.backgroundColor;
            int newEdgeType = overlay.hasEdgeType() ? overlay.edgeType : this.edgeType;
            int newEdgeColor = overlay.hasEdgeColor() ? overlay.edgeColor : this.edgeColor;
            int newWindowColor = overlay.hasWindowColor() ? overlay.windowColor : this.windowColor;
            String newRawTypeface = overlay.mRawTypeface != null ? overlay.mRawTypeface : this.mRawTypeface;
            return new CaptionStyle(newForegroundColor, newBackgroundColor, newEdgeType, newEdgeColor, newWindowColor, newRawTypeface);
        }

        public boolean hasBackgroundColor() {
            return this.mHasBackgroundColor;
        }

        public boolean hasForegroundColor() {
            return this.mHasForegroundColor;
        }

        public boolean hasEdgeType() {
            return this.mHasEdgeType;
        }

        public boolean hasEdgeColor() {
            return this.mHasEdgeColor;
        }

        public boolean hasWindowColor() {
            return this.mHasWindowColor;
        }

        public Typeface getTypeface() {
            if (this.mParsedTypeface == null && !TextUtils.isEmpty(this.mRawTypeface)) {
                this.mParsedTypeface = Typeface.create(this.mRawTypeface, 0);
            }
            return this.mParsedTypeface;
        }

        public static synchronized CaptionStyle getCustomStyle(ContentResolver cr) {
            CaptionStyle defStyle = DEFAULT_CUSTOM;
            int foregroundColor = Settings.Secure.getInt(cr, Settings.Secure.ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR, defStyle.foregroundColor);
            int backgroundColor = Settings.Secure.getInt(cr, Settings.Secure.ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR, defStyle.backgroundColor);
            int edgeType = Settings.Secure.getInt(cr, Settings.Secure.ACCESSIBILITY_CAPTIONING_EDGE_TYPE, defStyle.edgeType);
            int edgeColor = Settings.Secure.getInt(cr, Settings.Secure.ACCESSIBILITY_CAPTIONING_EDGE_COLOR, defStyle.edgeColor);
            int windowColor = Settings.Secure.getInt(cr, Settings.Secure.ACCESSIBILITY_CAPTIONING_WINDOW_COLOR, defStyle.windowColor);
            String rawTypeface = Settings.Secure.getString(cr, "accessibility_captioning_typeface");
            if (rawTypeface == null) {
                rawTypeface = defStyle.mRawTypeface;
            }
            return new CaptionStyle(foregroundColor, backgroundColor, edgeType, edgeColor, windowColor, rawTypeface);
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class CaptioningChangeListener {
        public void onEnabledChanged(boolean enabled) {
        }

        public void onUserStyleChanged(CaptionStyle userStyle) {
        }

        public void onLocaleChanged(Locale locale) {
        }

        public void onFontScaleChanged(float fontScale) {
        }
    }
}
