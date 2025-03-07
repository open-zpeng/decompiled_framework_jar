package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: classes.dex */
public final class LceDataInfo {
    public byte confidenceLevel;
    public int lastHopCapacityKbps;
    public boolean lceSuspended;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != LceDataInfo.class) {
            return false;
        }
        LceDataInfo other = (LceDataInfo) otherObject;
        if (this.lastHopCapacityKbps == other.lastHopCapacityKbps && this.confidenceLevel == other.confidenceLevel && this.lceSuspended == other.lceSuspended) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.lastHopCapacityKbps))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(this.confidenceLevel))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.lceSuspended))));
    }

    public final String toString() {
        return "{.lastHopCapacityKbps = " + this.lastHopCapacityKbps + ", .confidenceLevel = " + ((int) this.confidenceLevel) + ", .lceSuspended = " + this.lceSuspended + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(8L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<LceDataInfo> readVectorFromParcel(HwParcel parcel) {
        ArrayList<LceDataInfo> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 8, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            LceDataInfo _hidl_vec_element = new LceDataInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 8);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.lastHopCapacityKbps = _hidl_blob.getInt32(0 + _hidl_offset);
        this.confidenceLevel = _hidl_blob.getInt8(4 + _hidl_offset);
        this.lceSuspended = _hidl_blob.getBool(5 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(8);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<LceDataInfo> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 8);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 8);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.lastHopCapacityKbps);
        _hidl_blob.putInt8(4 + _hidl_offset, this.confidenceLevel);
        _hidl_blob.putBool(5 + _hidl_offset, this.lceSuspended);
    }
}
