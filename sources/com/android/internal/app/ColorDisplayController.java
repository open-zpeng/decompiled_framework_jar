package com.android.internal.app;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Slog;
import com.android.internal.R;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
/* loaded from: classes3.dex */
public final class ColorDisplayController {
    public static final int AUTO_MODE_CUSTOM = 1;
    public static final int AUTO_MODE_DISABLED = 0;
    public static final int AUTO_MODE_TWILIGHT = 2;
    public static final int COLOR_MODE_AUTOMATIC = 3;
    public static final int COLOR_MODE_BOOSTED = 1;
    public static final int COLOR_MODE_NATURAL = 0;
    public static final int COLOR_MODE_SATURATED = 2;
    private static final boolean DEBUG = false;
    private static final String TAG = "ColorDisplayController";
    private Callback mCallback;
    private final ContentObserver mContentObserver;
    private final Context mContext;
    private MetricsLogger mMetricsLogger;
    private final int mUserId;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface AutoMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface ColorMode {
    }

    public ColorDisplayController(Context context) {
        this(context, ActivityManager.getCurrentUser());
    }

    public ColorDisplayController(Context context, int userId) {
        this.mContext = context.getApplicationContext();
        this.mUserId = userId;
        this.mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.android.internal.app.ColorDisplayController.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                String setting = uri == null ? null : uri.getLastPathSegment();
                if (setting != null) {
                    ColorDisplayController.this.onSettingChanged(setting);
                }
            }
        };
    }

    public boolean isActivated() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_ACTIVATED, 0, this.mUserId) == 1;
    }

    public boolean setActivated(boolean activated) {
        if (isActivated() != activated) {
            Settings.Secure.putStringForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_LAST_ACTIVATED_TIME, LocalDateTime.now().toString(), this.mUserId);
        }
        return Settings.Secure.putIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_ACTIVATED, activated ? 1 : 0, this.mUserId);
    }

    public LocalDateTime getLastActivatedTime() {
        ContentResolver cr = this.mContext.getContentResolver();
        String lastActivatedTime = Settings.Secure.getStringForUser(cr, Settings.Secure.NIGHT_DISPLAY_LAST_ACTIVATED_TIME, this.mUserId);
        if (lastActivatedTime != null) {
            try {
                return LocalDateTime.parse(lastActivatedTime);
            } catch (DateTimeParseException e) {
                try {
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(lastActivatedTime)), ZoneId.systemDefault());
                } catch (NumberFormatException | DateTimeException e2) {
                    return null;
                }
            }
        }
        return null;
    }

    public int getAutoMode() {
        int autoMode = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_AUTO_MODE, -1, this.mUserId);
        if (autoMode == -1) {
            autoMode = this.mContext.getResources().getInteger(R.integer.config_defaultNightDisplayAutoMode);
        }
        if (autoMode != 0 && autoMode != 1 && autoMode != 2) {
            Slog.e(TAG, "Invalid autoMode: " + autoMode);
            return 0;
        }
        return autoMode;
    }

    public int getAutoModeRaw() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_AUTO_MODE, -1, this.mUserId);
    }

    public boolean setAutoMode(int autoMode) {
        if (autoMode != 0 && autoMode != 1 && autoMode != 2) {
            throw new IllegalArgumentException("Invalid autoMode: " + autoMode);
        }
        if (getAutoMode() != autoMode) {
            Settings.Secure.putStringForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_LAST_ACTIVATED_TIME, null, this.mUserId);
            getMetricsLogger().write(new LogMaker((int) MetricsProto.MetricsEvent.ACTION_NIGHT_DISPLAY_AUTO_MODE_CHANGED).setType(4).setSubtype(autoMode));
        }
        return Settings.Secure.putIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_AUTO_MODE, autoMode, this.mUserId);
    }

    public LocalTime getCustomStartTime() {
        int startTimeValue = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME, -1, this.mUserId);
        if (startTimeValue == -1) {
            startTimeValue = this.mContext.getResources().getInteger(R.integer.config_defaultNightDisplayCustomStartTime);
        }
        return LocalTime.ofSecondOfDay(startTimeValue / 1000);
    }

    public boolean setCustomStartTime(LocalTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("startTime cannot be null");
        }
        getMetricsLogger().write(new LogMaker(1310).setType(4).setSubtype(0));
        return Settings.Secure.putIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME, startTime.toSecondOfDay() * 1000, this.mUserId);
    }

    public LocalTime getCustomEndTime() {
        int endTimeValue = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME, -1, this.mUserId);
        if (endTimeValue == -1) {
            endTimeValue = this.mContext.getResources().getInteger(R.integer.config_defaultNightDisplayCustomEndTime);
        }
        return LocalTime.ofSecondOfDay(endTimeValue / 1000);
    }

    public boolean setCustomEndTime(LocalTime endTime) {
        if (endTime == null) {
            throw new IllegalArgumentException("endTime cannot be null");
        }
        getMetricsLogger().write(new LogMaker(1310).setType(4).setSubtype(1));
        return Settings.Secure.putIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME, endTime.toSecondOfDay() * 1000, this.mUserId);
    }

    public int getColorTemperature() {
        int colorTemperature = Settings.Secure.getIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_COLOR_TEMPERATURE, -1, this.mUserId);
        if (colorTemperature == -1) {
            colorTemperature = getDefaultColorTemperature();
        }
        int minimumTemperature = getMinimumColorTemperature();
        int maximumTemperature = getMaximumColorTemperature();
        if (colorTemperature < minimumTemperature) {
            return minimumTemperature;
        }
        if (colorTemperature > maximumTemperature) {
            return maximumTemperature;
        }
        return colorTemperature;
    }

    public boolean setColorTemperature(int colorTemperature) {
        return Settings.Secure.putIntForUser(this.mContext.getContentResolver(), Settings.Secure.NIGHT_DISPLAY_COLOR_TEMPERATURE, colorTemperature, this.mUserId);
    }

    private int getCurrentColorModeFromSystemProperties() {
        int displayColorSetting = SystemProperties.getInt("persist.sys.sf.native_mode", 0);
        if (displayColorSetting == 0) {
            return "1.0".equals(SystemProperties.get("persist.sys.sf.color_saturation")) ? 0 : 1;
        } else if (displayColorSetting == 1) {
            return 2;
        } else {
            if (displayColorSetting == 2) {
                return 3;
            }
            return -1;
        }
    }

    private boolean isColorModeAvailable(int colorMode) {
        int[] availableColorModes = this.mContext.getResources().getIntArray(R.array.config_availableColorModes);
        if (availableColorModes != null) {
            for (int mode : availableColorModes) {
                if (mode == colorMode) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getColorMode() {
        if (getAccessibilityTransformActivated()) {
            if (isColorModeAvailable(2)) {
                return 2;
            }
            if (isColorModeAvailable(3)) {
                return 3;
            }
        }
        int colorMode = Settings.System.getIntForUser(this.mContext.getContentResolver(), Settings.System.DISPLAY_COLOR_MODE, -1, this.mUserId);
        if (colorMode == -1) {
            colorMode = getCurrentColorModeFromSystemProperties();
        }
        if (!isColorModeAvailable(colorMode)) {
            if (colorMode == 1 && isColorModeAvailable(0)) {
                return 0;
            }
            if (colorMode == 2 && isColorModeAvailable(3)) {
                return 3;
            }
            if (colorMode == 3 && isColorModeAvailable(2)) {
                return 2;
            }
            return -1;
        }
        return colorMode;
    }

    public void setColorMode(int colorMode) {
        if (!isColorModeAvailable(colorMode)) {
            throw new IllegalArgumentException("Invalid colorMode: " + colorMode);
        }
        Settings.System.putIntForUser(this.mContext.getContentResolver(), Settings.System.DISPLAY_COLOR_MODE, colorMode, this.mUserId);
    }

    public int getMinimumColorTemperature() {
        return this.mContext.getResources().getInteger(R.integer.config_nightDisplayColorTemperatureMin);
    }

    public int getMaximumColorTemperature() {
        return this.mContext.getResources().getInteger(R.integer.config_nightDisplayColorTemperatureMax);
    }

    public int getDefaultColorTemperature() {
        return this.mContext.getResources().getInteger(R.integer.config_nightDisplayColorTemperatureDefault);
    }

    public boolean getAccessibilityTransformActivated() {
        ContentResolver cr = this.mContext.getContentResolver();
        return Settings.Secure.getIntForUser(cr, Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED, 0, this.mUserId) == 1 || Settings.Secure.getIntForUser(cr, "accessibility_display_daltonizer_enabled", 0, this.mUserId) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSettingChanged(String setting) {
        if (this.mCallback != null) {
            char c = 65535;
            switch (setting.hashCode()) {
                case -2038150513:
                    if (setting.equals(Settings.Secure.NIGHT_DISPLAY_AUTO_MODE)) {
                        c = 1;
                        break;
                    }
                    break;
                case -1761668069:
                    if (setting.equals(Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME)) {
                        c = 3;
                        break;
                    }
                    break;
                case -969458956:
                    if (setting.equals(Settings.Secure.NIGHT_DISPLAY_COLOR_TEMPERATURE)) {
                        c = 4;
                        break;
                    }
                    break;
                case -686921934:
                    if (setting.equals("accessibility_display_daltonizer_enabled")) {
                        c = 7;
                        break;
                    }
                    break;
                case -551230169:
                    if (setting.equals(Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED)) {
                        c = 6;
                        break;
                    }
                    break;
                case 800115245:
                    if (setting.equals(Settings.Secure.NIGHT_DISPLAY_ACTIVATED)) {
                        c = 0;
                        break;
                    }
                    break;
                case 1561688220:
                    if (setting.equals(Settings.System.DISPLAY_COLOR_MODE)) {
                        c = 5;
                        break;
                    }
                    break;
                case 1578271348:
                    if (setting.equals(Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    this.mCallback.onActivated(isActivated());
                    return;
                case 1:
                    this.mCallback.onAutoModeChanged(getAutoMode());
                    return;
                case 2:
                    this.mCallback.onCustomStartTimeChanged(getCustomStartTime());
                    return;
                case 3:
                    this.mCallback.onCustomEndTimeChanged(getCustomEndTime());
                    return;
                case 4:
                    this.mCallback.onColorTemperatureChanged(getColorTemperature());
                    return;
                case 5:
                    this.mCallback.onDisplayColorModeChanged(getColorMode());
                    return;
                case 6:
                case 7:
                    this.mCallback.onAccessibilityTransformChanged(getAccessibilityTransformActivated());
                    return;
                default:
                    return;
            }
        }
    }

    public void setListener(Callback callback) {
        Callback oldCallback = this.mCallback;
        if (oldCallback != callback) {
            this.mCallback = callback;
            if (callback == null) {
                this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
            } else if (oldCallback == null) {
                ContentResolver cr = this.mContext.getContentResolver();
                cr.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.NIGHT_DISPLAY_ACTIVATED), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.NIGHT_DISPLAY_AUTO_MODE), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.NIGHT_DISPLAY_CUSTOM_START_TIME), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.NIGHT_DISPLAY_CUSTOM_END_TIME), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.NIGHT_DISPLAY_COLOR_TEMPERATURE), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Settings.System.getUriFor(Settings.System.DISPLAY_COLOR_MODE), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED), false, this.mContentObserver, this.mUserId);
                cr.registerContentObserver(Settings.Secure.getUriFor("accessibility_display_daltonizer_enabled"), false, this.mContentObserver, this.mUserId);
            }
        }
    }

    private MetricsLogger getMetricsLogger() {
        if (this.mMetricsLogger == null) {
            this.mMetricsLogger = new MetricsLogger();
        }
        return this.mMetricsLogger;
    }

    public static boolean isAvailable(Context context) {
        return context.getResources().getBoolean(R.bool.config_nightDisplayAvailable);
    }

    /* loaded from: classes3.dex */
    public interface Callback {
        default void onActivated(boolean activated) {
        }

        default void onAutoModeChanged(int autoMode) {
        }

        default void onCustomStartTimeChanged(LocalTime startTime) {
        }

        default void onCustomEndTimeChanged(LocalTime endTime) {
        }

        default void onColorTemperatureChanged(int colorTemperature) {
        }

        default void onDisplayColorModeChanged(int displayColorMode) {
        }

        default void onAccessibilityTransformChanged(boolean state) {
        }
    }
}
