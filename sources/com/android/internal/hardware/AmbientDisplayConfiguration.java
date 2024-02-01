package com.android.internal.hardware;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import com.android.internal.R;
/* loaded from: classes3.dex */
public class AmbientDisplayConfiguration {
    private final boolean mAlwaysOnByDefault;
    private final Context mContext;

    public AmbientDisplayConfiguration(Context context) {
        this.mContext = context;
        this.mAlwaysOnByDefault = this.mContext.getResources().getBoolean(R.bool.config_dozeAlwaysOnEnabled);
    }

    public boolean enabled(int user) {
        return pulseOnNotificationEnabled(user) || pulseOnPickupEnabled(user) || pulseOnDoubleTapEnabled(user) || pulseOnLongPressEnabled(user) || alwaysOnEnabled(user);
    }

    public boolean available() {
        return pulseOnNotificationAvailable() || pulseOnPickupAvailable() || pulseOnDoubleTapAvailable();
    }

    public boolean pulseOnNotificationEnabled(int user) {
        return boolSettingDefaultOn("doze_enabled", user) && pulseOnNotificationAvailable();
    }

    public boolean pulseOnNotificationAvailable() {
        return ambientDisplayAvailable();
    }

    public boolean pulseOnPickupEnabled(int user) {
        boolean settingEnabled = boolSettingDefaultOn(Settings.Secure.DOZE_PULSE_ON_PICK_UP, user);
        return (settingEnabled || alwaysOnEnabled(user)) && pulseOnPickupAvailable();
    }

    public boolean pulseOnPickupAvailable() {
        return dozePulsePickupSensorAvailable() && ambientDisplayAvailable();
    }

    public boolean dozePulsePickupSensorAvailable() {
        return this.mContext.getResources().getBoolean(R.bool.config_dozePulsePickup);
    }

    public boolean pulseOnPickupCanBeModified(int user) {
        return !alwaysOnEnabled(user);
    }

    public boolean pulseOnDoubleTapEnabled(int user) {
        return boolSettingDefaultOn(Settings.Secure.DOZE_PULSE_ON_DOUBLE_TAP, user) && pulseOnDoubleTapAvailable();
    }

    public boolean pulseOnDoubleTapAvailable() {
        return doubleTapSensorAvailable() && ambientDisplayAvailable();
    }

    public boolean doubleTapSensorAvailable() {
        return !TextUtils.isEmpty(doubleTapSensorType());
    }

    public String doubleTapSensorType() {
        return this.mContext.getResources().getString(R.string.config_dozeDoubleTapSensorType);
    }

    public String longPressSensorType() {
        return this.mContext.getResources().getString(R.string.config_dozeLongPressSensorType);
    }

    public boolean pulseOnLongPressEnabled(int user) {
        return pulseOnLongPressAvailable() && boolSettingDefaultOff(Settings.Secure.DOZE_PULSE_ON_LONG_PRESS, user);
    }

    private boolean pulseOnLongPressAvailable() {
        return !TextUtils.isEmpty(longPressSensorType());
    }

    public boolean alwaysOnEnabled(int user) {
        return boolSetting(Settings.Secure.DOZE_ALWAYS_ON, user, this.mAlwaysOnByDefault ? 1 : 0) && alwaysOnAvailable() && !accessibilityInversionEnabled(user);
    }

    public boolean alwaysOnAvailable() {
        return (alwaysOnDisplayDebuggingEnabled() || alwaysOnDisplayAvailable()) && ambientDisplayAvailable();
    }

    public boolean alwaysOnAvailableForUser(int user) {
        return alwaysOnAvailable() && !accessibilityInversionEnabled(user);
    }

    public String ambientDisplayComponent() {
        return this.mContext.getResources().getString(R.string.config_dozeComponent);
    }

    public boolean accessibilityInversionEnabled(int user) {
        return boolSettingDefaultOff(Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED, user);
    }

    public boolean ambientDisplayAvailable() {
        return !TextUtils.isEmpty(ambientDisplayComponent());
    }

    private boolean alwaysOnDisplayAvailable() {
        return this.mContext.getResources().getBoolean(R.bool.config_dozeAlwaysOnDisplayAvailable);
    }

    private boolean alwaysOnDisplayDebuggingEnabled() {
        return SystemProperties.getBoolean("debug.doze.aod", false) && Build.IS_DEBUGGABLE;
    }

    private boolean boolSettingDefaultOn(String name, int user) {
        return boolSetting(name, user, 1);
    }

    private boolean boolSettingDefaultOff(String name, int user) {
        return boolSetting(name, user, 0);
    }

    private boolean boolSetting(String name, int user, int def) {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), name, def, user) != 0;
    }
}
