package android.view;

import android.annotation.UnsupportedAppUsage;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import java.util.Objects;

/* loaded from: classes3.dex */
public class DisplayAdjustments {
    public static final DisplayAdjustments DEFAULT_DISPLAY_ADJUSTMENTS = new DisplayAdjustments();
    private volatile CompatibilityInfo mCompatInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
    private Configuration mConfiguration;

    @UnsupportedAppUsage
    public DisplayAdjustments() {
    }

    public DisplayAdjustments(Configuration configuration) {
        this.mConfiguration = new Configuration(configuration != null ? configuration : Configuration.EMPTY);
    }

    public DisplayAdjustments(DisplayAdjustments daj) {
        setCompatibilityInfo(daj.mCompatInfo);
        Configuration configuration = daj.mConfiguration;
        this.mConfiguration = new Configuration(configuration == null ? Configuration.EMPTY : configuration);
    }

    @UnsupportedAppUsage
    public void setCompatibilityInfo(CompatibilityInfo compatInfo) {
        if (this == DEFAULT_DISPLAY_ADJUSTMENTS) {
            throw new IllegalArgumentException("setCompatbilityInfo: Cannot modify DEFAULT_DISPLAY_ADJUSTMENTS");
        }
        if (compatInfo != null && (compatInfo.isScalingRequired() || !compatInfo.supportsScreen())) {
            this.mCompatInfo = compatInfo;
        } else {
            this.mCompatInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
        }
    }

    public CompatibilityInfo getCompatibilityInfo() {
        return this.mCompatInfo;
    }

    public void setConfiguration(Configuration configuration) {
        if (this == DEFAULT_DISPLAY_ADJUSTMENTS) {
            throw new IllegalArgumentException("setConfiguration: Cannot modify DEFAULT_DISPLAY_ADJUSTMENTS");
        }
        this.mConfiguration.setTo(configuration != null ? configuration : Configuration.EMPTY);
    }

    @UnsupportedAppUsage
    public Configuration getConfiguration() {
        return this.mConfiguration;
    }

    public int hashCode() {
        int hash = (17 * 31) + Objects.hashCode(this.mCompatInfo);
        return (hash * 31) + Objects.hashCode(this.mConfiguration);
    }

    public boolean equals(Object o) {
        if (o instanceof DisplayAdjustments) {
            DisplayAdjustments daj = (DisplayAdjustments) o;
            return Objects.equals(daj.mCompatInfo, this.mCompatInfo) && Objects.equals(daj.mConfiguration, this.mConfiguration);
        }
        return false;
    }
}
