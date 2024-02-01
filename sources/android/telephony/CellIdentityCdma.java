package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class CellIdentityCdma extends CellIdentity {
    private static final boolean DBG = false;
    private final int mBasestationId;
    private final int mLatitude;
    private final int mLongitude;
    private final int mNetworkId;
    private final int mSystemId;
    private static final String TAG = CellIdentityCdma.class.getSimpleName();
    public static final Parcelable.Creator<CellIdentityCdma> CREATOR = new Parcelable.Creator<CellIdentityCdma>() { // from class: android.telephony.CellIdentityCdma.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellIdentityCdma createFromParcel(Parcel in) {
            in.readInt();
            return CellIdentityCdma.createFromParcelBody(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CellIdentityCdma[] newArray(int size) {
            return new CellIdentityCdma[size];
        }
    };

    public synchronized CellIdentityCdma() {
        super(TAG, 2, null, null, null, null);
        this.mNetworkId = Integer.MAX_VALUE;
        this.mSystemId = Integer.MAX_VALUE;
        this.mBasestationId = Integer.MAX_VALUE;
        this.mLongitude = Integer.MAX_VALUE;
        this.mLatitude = Integer.MAX_VALUE;
    }

    private protected CellIdentityCdma(int nid, int sid, int bid, int lon, int lat) {
        this(nid, sid, bid, lon, lat, null, null);
    }

    public synchronized CellIdentityCdma(int nid, int sid, int bid, int lon, int lat, String alphal, String alphas) {
        super(TAG, 2, null, null, alphal, alphas);
        this.mNetworkId = nid;
        this.mSystemId = sid;
        this.mBasestationId = bid;
        if (!isNullIsland(lat, lon)) {
            this.mLongitude = lon;
            this.mLatitude = lat;
            return;
        }
        this.mLatitude = Integer.MAX_VALUE;
        this.mLongitude = Integer.MAX_VALUE;
    }

    private synchronized CellIdentityCdma(CellIdentityCdma cid) {
        this(cid.mNetworkId, cid.mSystemId, cid.mBasestationId, cid.mLongitude, cid.mLatitude, cid.mAlphaLong, cid.mAlphaShort);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized CellIdentityCdma copy() {
        return new CellIdentityCdma(this);
    }

    private synchronized boolean isNullIsland(int lat, int lon) {
        return Math.abs(lat) <= 1 && Math.abs(lon) <= 1;
    }

    public int getNetworkId() {
        return this.mNetworkId;
    }

    public int getSystemId() {
        return this.mSystemId;
    }

    public int getBasestationId() {
        return this.mBasestationId;
    }

    public int getLongitude() {
        return this.mLongitude;
    }

    public int getLatitude() {
        return this.mLatitude;
    }

    @Override // android.telephony.CellIdentity
    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mNetworkId), Integer.valueOf(this.mSystemId), Integer.valueOf(this.mBasestationId), Integer.valueOf(this.mLatitude), Integer.valueOf(this.mLongitude), Integer.valueOf(super.hashCode()));
    }

    @Override // android.telephony.CellIdentity
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof CellIdentityCdma) {
            CellIdentityCdma o = (CellIdentityCdma) other;
            return this.mNetworkId == o.mNetworkId && this.mSystemId == o.mSystemId && this.mBasestationId == o.mBasestationId && this.mLatitude == o.mLatitude && this.mLongitude == o.mLongitude && super.equals(other);
        }
        return false;
    }

    public String toString() {
        return TAG + ":{ mNetworkId=" + this.mNetworkId + " mSystemId=" + this.mSystemId + " mBasestationId=" + this.mBasestationId + " mLongitude=" + this.mLongitude + " mLatitude=" + this.mLatitude + " mAlphaLong=" + this.mAlphaLong + " mAlphaShort=" + this.mAlphaShort + "}";
    }

    @Override // android.telephony.CellIdentity, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, 2);
        dest.writeInt(this.mNetworkId);
        dest.writeInt(this.mSystemId);
        dest.writeInt(this.mBasestationId);
        dest.writeInt(this.mLongitude);
        dest.writeInt(this.mLatitude);
    }

    private synchronized CellIdentityCdma(Parcel in) {
        super(TAG, 2, in);
        this.mNetworkId = in.readInt();
        this.mSystemId = in.readInt();
        this.mBasestationId = in.readInt();
        this.mLongitude = in.readInt();
        this.mLatitude = in.readInt();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static synchronized CellIdentityCdma createFromParcelBody(Parcel in) {
        return new CellIdentityCdma(in);
    }
}
