package android.view;

import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import java.util.Objects;
/* loaded from: classes2.dex */
public class DisplayAdjustments {
    public static final DisplayAdjustments DEFAULT_DISPLAY_ADJUSTMENTS = new DisplayAdjustments();
    private volatile CompatibilityInfo mCompatInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
    private Configuration mConfiguration;

    /* JADX INFO: Access modifiers changed from: private */
    public DisplayAdjustments() {
    }

    public synchronized DisplayAdjustments(Configuration configuration) {
        this.mConfiguration = new Configuration(configuration != null ? configuration : Configuration.EMPTY);
    }

    public synchronized DisplayAdjustments(DisplayAdjustments daj) {
        setCompatibilityInfo(daj.mCompatInfo);
        this.mConfiguration = new Configuration(daj.mConfiguration != null ? daj.mConfiguration : Configuration.EMPTY);
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    public synchronized CompatibilityInfo getCompatibilityInfo() {
        return this.mCompatInfo;
    }

    public synchronized void setConfiguration(Configuration configuration) {
        if (this == DEFAULT_DISPLAY_ADJUSTMENTS) {
            throw new IllegalArgumentException("setConfiguration: Cannot modify DEFAULT_DISPLAY_ADJUSTMENTS");
        }
        this.mConfiguration.setTo(configuration != null ? configuration : Configuration.EMPTY);
    }

    /* JADX INFO: Access modifiers changed from: private */
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
