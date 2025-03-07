package android.hardware.radio.V1_3;

import android.hardware.radio.V1_0.RadioError;
import android.hardware.radio.V1_0.RadioResponseType;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: classes.dex */
public final class RadioResponseInfoModem {
    public int error;
    public boolean isEnabled;
    public int serial;
    public int type;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != RadioResponseInfoModem.class) {
            return false;
        }
        RadioResponseInfoModem other = (RadioResponseInfoModem) otherObject;
        if (this.type == other.type && this.serial == other.serial && this.error == other.error && this.isEnabled == other.isEnabled) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.serial))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.error))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isEnabled))));
    }

    public final String toString() {
        return "{.type = " + RadioResponseType.toString(this.type) + ", .serial = " + this.serial + ", .error = " + RadioError.toString(this.error) + ", .isEnabled = " + this.isEnabled + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(16L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<RadioResponseInfoModem> readVectorFromParcel(HwParcel parcel) {
        ArrayList<RadioResponseInfoModem> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 16, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            RadioResponseInfoModem _hidl_vec_element = new RadioResponseInfoModem();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 16);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.type = _hidl_blob.getInt32(0 + _hidl_offset);
        this.serial = _hidl_blob.getInt32(4 + _hidl_offset);
        this.error = _hidl_blob.getInt32(8 + _hidl_offset);
        this.isEnabled = _hidl_blob.getBool(12 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(16);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<RadioResponseInfoModem> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 16);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 16);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.type);
        _hidl_blob.putInt32(4 + _hidl_offset, this.serial);
        _hidl_blob.putInt32(8 + _hidl_offset, this.error);
        _hidl_blob.putBool(12 + _hidl_offset, this.isEnabled);
    }
}
