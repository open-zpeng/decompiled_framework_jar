package android.hardware.radio.V1_4;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: classes.dex */
public final class NrSignalStrength {
    public int csiRsrp;
    public int csiRsrq;
    public int csiSinr;
    public int ssRsrp;
    public int ssRsrq;
    public int ssSinr;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != NrSignalStrength.class) {
            return false;
        }
        NrSignalStrength other = (NrSignalStrength) otherObject;
        if (this.ssRsrp == other.ssRsrp && this.ssRsrq == other.ssRsrq && this.ssSinr == other.ssSinr && this.csiRsrp == other.csiRsrp && this.csiRsrq == other.csiRsrq && this.csiSinr == other.csiSinr) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.ssRsrp))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.ssRsrq))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.ssSinr))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.csiRsrp))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.csiRsrq))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.csiSinr))));
    }

    public final String toString() {
        return "{.ssRsrp = " + this.ssRsrp + ", .ssRsrq = " + this.ssRsrq + ", .ssSinr = " + this.ssSinr + ", .csiRsrp = " + this.csiRsrp + ", .csiRsrq = " + this.csiRsrq + ", .csiSinr = " + this.csiSinr + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(24L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<NrSignalStrength> readVectorFromParcel(HwParcel parcel) {
        ArrayList<NrSignalStrength> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            NrSignalStrength _hidl_vec_element = new NrSignalStrength();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.ssRsrp = _hidl_blob.getInt32(0 + _hidl_offset);
        this.ssRsrq = _hidl_blob.getInt32(4 + _hidl_offset);
        this.ssSinr = _hidl_blob.getInt32(8 + _hidl_offset);
        this.csiRsrp = _hidl_blob.getInt32(12 + _hidl_offset);
        this.csiRsrq = _hidl_blob.getInt32(16 + _hidl_offset);
        this.csiSinr = _hidl_blob.getInt32(20 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(24);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<NrSignalStrength> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 24);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 24);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.ssRsrp);
        _hidl_blob.putInt32(4 + _hidl_offset, this.ssRsrq);
        _hidl_blob.putInt32(8 + _hidl_offset, this.ssSinr);
        _hidl_blob.putInt32(12 + _hidl_offset, this.csiRsrp);
        _hidl_blob.putInt32(16 + _hidl_offset, this.csiRsrq);
        _hidl_blob.putInt32(20 + _hidl_offset, this.csiSinr);
    }
}
