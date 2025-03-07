package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* loaded from: classes.dex */
public final class CardStatus {
    public int physicalSlotId;
    public android.hardware.radio.V1_0.CardStatus base = new android.hardware.radio.V1_0.CardStatus();
    public String atr = new String();
    public String iccid = new String();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != CardStatus.class) {
            return false;
        }
        CardStatus other = (CardStatus) otherObject;
        if (HidlSupport.deepEquals(this.base, other.base) && this.physicalSlotId == other.physicalSlotId && HidlSupport.deepEquals(this.atr, other.atr) && HidlSupport.deepEquals(this.iccid, other.iccid)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.base)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.physicalSlotId))), Integer.valueOf(HidlSupport.deepHashCode(this.atr)), Integer.valueOf(HidlSupport.deepHashCode(this.iccid)));
    }

    public final String toString() {
        return "{.base = " + this.base + ", .physicalSlotId = " + this.physicalSlotId + ", .atr = " + this.atr + ", .iccid = " + this.iccid + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(80L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<CardStatus> readVectorFromParcel(HwParcel parcel) {
        ArrayList<CardStatus> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 80, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            CardStatus _hidl_vec_element = new CardStatus();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 80);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.base.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
        this.physicalSlotId = _hidl_blob.getInt32(_hidl_offset + 40);
        this.atr = _hidl_blob.getString(_hidl_offset + 48);
        parcel.readEmbeddedBuffer(this.atr.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 48 + 0, false);
        this.iccid = _hidl_blob.getString(_hidl_offset + 64);
        parcel.readEmbeddedBuffer(this.iccid.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 64 + 0, false);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(80);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<CardStatus> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 80);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 80);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        this.base.writeEmbeddedToBlob(_hidl_blob, 0 + _hidl_offset);
        _hidl_blob.putInt32(40 + _hidl_offset, this.physicalSlotId);
        _hidl_blob.putString(48 + _hidl_offset, this.atr);
        _hidl_blob.putString(64 + _hidl_offset, this.iccid);
    }
}
