package android.location;

import android.annotation.UnsupportedAppUsage;
import android.util.SparseArray;
import java.util.Iterator;
import java.util.NoSuchElementException;

@Deprecated
/* loaded from: classes.dex */
public final class GpsStatus {
    private static final int BEIDOU_SVID_OFFSET = 200;
    private static final int GLONASS_SVID_OFFSET = 64;
    public static final int GPS_EVENT_FIRST_FIX = 3;
    public static final int GPS_EVENT_SATELLITE_STATUS = 4;
    public static final int GPS_EVENT_STARTED = 1;
    public static final int GPS_EVENT_STOPPED = 2;
    private static final int NUM_SATELLITES = 255;
    private static final int SBAS_SVID_OFFSET = -87;
    private int mTimeToFirstFix;
    private final SparseArray<GpsSatellite> mSatellites = new SparseArray<>();
    private Iterable<GpsSatellite> mSatelliteList = new Iterable<GpsSatellite>() { // from class: android.location.GpsStatus.1
        @Override // java.lang.Iterable
        public Iterator<GpsSatellite> iterator() {
            return new SatelliteIterator();
        }
    };

    @Deprecated
    /* loaded from: classes.dex */
    public interface Listener {
        void onGpsStatusChanged(int i);
    }

    @Deprecated
    /* loaded from: classes.dex */
    public interface NmeaListener {
        void onNmeaReceived(long j, String str);
    }

    /* loaded from: classes.dex */
    private final class SatelliteIterator implements Iterator<GpsSatellite> {
        private int mIndex = 0;
        private final int mSatellitesCount;

        SatelliteIterator() {
            this.mSatellitesCount = GpsStatus.this.mSatellites.size();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            while (this.mIndex < this.mSatellitesCount) {
                GpsSatellite satellite = (GpsSatellite) GpsStatus.this.mSatellites.valueAt(this.mIndex);
                if (satellite.mValid) {
                    return true;
                }
                this.mIndex++;
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public GpsSatellite next() {
            while (this.mIndex < this.mSatellitesCount) {
                GpsSatellite satellite = (GpsSatellite) GpsStatus.this.mSatellites.valueAt(this.mIndex);
                this.mIndex++;
                if (satellite.mValid) {
                    return satellite;
                }
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void setStatus(int svCount, int[] svidWithFlags, float[] cn0s, float[] elevations, float[] azimuths) {
        int i;
        clearSatellites();
        while (i < svCount) {
            int constellationType = (svidWithFlags[i] >> 4) & 15;
            int prn = svidWithFlags[i] >> 8;
            if (constellationType == 3) {
                prn += 64;
            } else if (constellationType == 5) {
                prn += 200;
            } else if (constellationType != 2) {
                i = (constellationType == 1 || constellationType == 4) ? 0 : i + 1;
            } else {
                prn += SBAS_SVID_OFFSET;
            }
            if (prn > 0 && prn <= 255) {
                GpsSatellite satellite = this.mSatellites.get(prn);
                if (satellite == null) {
                    satellite = new GpsSatellite(prn);
                    this.mSatellites.put(prn, satellite);
                }
                satellite.mValid = true;
                satellite.mSnr = cn0s[i];
                satellite.mElevation = elevations[i];
                satellite.mAzimuth = azimuths[i];
                satellite.mHasEphemeris = (svidWithFlags[i] & 1) != 0;
                satellite.mHasAlmanac = (2 & svidWithFlags[i]) != 0;
                satellite.mUsedInFix = (4 & svidWithFlags[i]) != 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStatus(GnssStatus status, int timeToFirstFix) {
        this.mTimeToFirstFix = timeToFirstFix;
        setStatus(status.mSvCount, status.mSvidWithFlags, status.mCn0DbHz, status.mElevations, status.mAzimuths);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    void setTimeToFirstFix(int ttff) {
        this.mTimeToFirstFix = ttff;
    }

    public int getTimeToFirstFix() {
        return this.mTimeToFirstFix;
    }

    public Iterable<GpsSatellite> getSatellites() {
        return this.mSatelliteList;
    }

    public int getMaxSatellites() {
        return 255;
    }

    private void clearSatellites() {
        int satellitesCount = this.mSatellites.size();
        for (int i = 0; i < satellitesCount; i++) {
            GpsSatellite satellite = this.mSatellites.valueAt(i);
            satellite.mValid = false;
        }
    }
}
