package android.location;

import android.annotation.SystemApi;

@SystemApi
/* loaded from: classes.dex */
public final class GnssCapabilities {
    public static final long GEOFENCING = 4;
    public static final long INVALID_CAPABILITIES = -1;
    public static final long LOW_POWER_MODE = 1;
    public static final long MEASUREMENTS = 8;
    public static final long MEASUREMENT_CORRECTIONS = 32;
    public static final long MEASUREMENT_CORRECTIONS_EXCESS_PATH_LENGTH = 128;
    public static final long MEASUREMENT_CORRECTIONS_LOS_SATS = 64;
    public static final long MEASUREMENT_CORRECTIONS_REFLECTING_PLANE = 256;
    public static final long NAV_MESSAGES = 16;
    public static final long SATELLITE_BLACKLIST = 2;
    private final long mGnssCapabilities;

    public static GnssCapabilities of(long gnssCapabilities) {
        return new GnssCapabilities(gnssCapabilities);
    }

    private GnssCapabilities(long gnssCapabilities) {
        this.mGnssCapabilities = gnssCapabilities;
    }

    public boolean hasLowPowerMode() {
        return hasCapability(1L);
    }

    public boolean hasSatelliteBlacklist() {
        return hasCapability(2L);
    }

    public boolean hasGeofencing() {
        return hasCapability(4L);
    }

    public boolean hasMeasurements() {
        return hasCapability(8L);
    }

    public boolean hasNavMessages() {
        return hasCapability(16L);
    }

    public boolean hasMeasurementCorrections() {
        return hasCapability(32L);
    }

    public boolean hasMeasurementCorrectionsLosSats() {
        return hasCapability(64L);
    }

    public boolean hasMeasurementCorrectionsExcessPathLength() {
        return hasCapability(128L);
    }

    public boolean hasMeasurementCorrectionsReflectingPane() {
        return hasCapability(256L);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("GnssCapabilities: ( ");
        if (hasLowPowerMode()) {
            sb.append("LOW_POWER_MODE ");
        }
        if (hasSatelliteBlacklist()) {
            sb.append("SATELLITE_BLACKLIST ");
        }
        if (hasGeofencing()) {
            sb.append("GEOFENCING ");
        }
        if (hasMeasurements()) {
            sb.append("MEASUREMENTS ");
        }
        if (hasNavMessages()) {
            sb.append("NAV_MESSAGES ");
        }
        if (hasMeasurementCorrections()) {
            sb.append("MEASUREMENT_CORRECTIONS ");
        }
        if (hasMeasurementCorrectionsLosSats()) {
            sb.append("MEASUREMENT_CORRECTIONS_LOS_SATS ");
        }
        if (hasMeasurementCorrectionsExcessPathLength()) {
            sb.append("MEASUREMENT_CORRECTIONS_EXCESS_PATH_LENGTH ");
        }
        if (hasMeasurementCorrectionsReflectingPane()) {
            sb.append("MEASUREMENT_CORRECTIONS_REFLECTING_PLANE ");
        }
        sb.append(")");
        return sb.toString();
    }

    private boolean hasCapability(long capability) {
        return (this.mGnssCapabilities & capability) == capability;
    }
}
