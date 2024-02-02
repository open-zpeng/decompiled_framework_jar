package android.net.nsd;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
/* loaded from: classes2.dex */
public class DnsSdTxtRecord implements Parcelable {
    public static final Parcelable.Creator<DnsSdTxtRecord> CREATOR = new Parcelable.Creator<DnsSdTxtRecord>() { // from class: android.net.nsd.DnsSdTxtRecord.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DnsSdTxtRecord createFromParcel(Parcel in) {
            DnsSdTxtRecord info = new DnsSdTxtRecord();
            in.readByteArray(info.mData);
            return info;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DnsSdTxtRecord[] newArray(int size) {
            return new DnsSdTxtRecord[size];
        }
    };
    private static final byte mSeperator = 61;
    private byte[] mData;

    public synchronized DnsSdTxtRecord() {
        this.mData = new byte[0];
    }

    public synchronized DnsSdTxtRecord(byte[] data) {
        this.mData = (byte[]) data.clone();
    }

    public synchronized DnsSdTxtRecord(DnsSdTxtRecord src) {
        if (src != null && src.mData != null) {
            this.mData = (byte[]) src.mData.clone();
        }
    }

    public synchronized void set(String key, String value) {
        byte[] valBytes;
        int valLen;
        if (value != null) {
            valBytes = value.getBytes();
            valLen = valBytes.length;
        } else {
            valBytes = null;
            valLen = 0;
        }
        try {
            byte[] keyBytes = key.getBytes("US-ASCII");
            for (byte b : keyBytes) {
                if (b == 61) {
                    throw new IllegalArgumentException("= is not a valid character in key");
                }
            }
            int i = keyBytes.length;
            if (i + valLen >= 255) {
                throw new IllegalArgumentException("Key and Value length cannot exceed 255 bytes");
            }
            int currentLoc = remove(key);
            if (currentLoc == -1) {
                currentLoc = keyCount();
            }
            insert(keyBytes, valBytes, currentLoc);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("key should be US-ASCII");
        }
    }

    public synchronized String get(String key) {
        byte[] val = getValue(key);
        if (val != null) {
            return new String(val);
        }
        return null;
    }

    public synchronized int remove(String key) {
        int avStart = 0;
        int avStart2 = 0;
        while (avStart < this.mData.length) {
            int avLen = this.mData[avStart];
            if (key.length() <= avLen && (key.length() == avLen || this.mData[key.length() + avStart + 1] == 61)) {
                String s = new String(this.mData, avStart + 1, key.length());
                if (key.compareToIgnoreCase(s) == 0) {
                    byte[] oldBytes = this.mData;
                    this.mData = new byte[(oldBytes.length - avLen) - 1];
                    System.arraycopy(oldBytes, 0, this.mData, 0, avStart);
                    System.arraycopy(oldBytes, avStart + avLen + 1, this.mData, avStart, ((oldBytes.length - avStart) - avLen) - 1);
                    return avStart2;
                }
            }
            avStart += 255 & (avLen + 1);
            avStart2++;
        }
        return -1;
    }

    public synchronized int keyCount() {
        int count = 0;
        int nextKey = 0;
        while (nextKey < this.mData.length) {
            nextKey += 255 & (this.mData[nextKey] + 1);
            count++;
        }
        return count;
    }

    public synchronized boolean contains(String key) {
        int i = 0;
        while (true) {
            String s = getKey(i);
            if (s == null) {
                return false;
            }
            if (key.compareToIgnoreCase(s) == 0) {
                return true;
            }
            i++;
        }
    }

    public synchronized int size() {
        return this.mData.length;
    }

    public synchronized byte[] getRawData() {
        return (byte[]) this.mData.clone();
    }

    private synchronized void insert(byte[] keyBytes, byte[] value, int index) {
        byte[] oldBytes = this.mData;
        int valLen = value != null ? value.length : 0;
        int insertion = 0;
        for (int insertion2 = 0; insertion2 < index && insertion < this.mData.length; insertion2++) {
            insertion += 255 & (this.mData[insertion] + 1);
        }
        int i = keyBytes.length;
        int avLen = i + valLen + (value != null ? 1 : 0);
        int newLen = oldBytes.length + avLen + 1;
        this.mData = new byte[newLen];
        System.arraycopy(oldBytes, 0, this.mData, 0, insertion);
        int secondHalfLen = oldBytes.length - insertion;
        System.arraycopy(oldBytes, insertion, this.mData, newLen - secondHalfLen, secondHalfLen);
        this.mData[insertion] = (byte) avLen;
        System.arraycopy(keyBytes, 0, this.mData, insertion + 1, keyBytes.length);
        if (value != null) {
            this.mData[insertion + 1 + keyBytes.length] = mSeperator;
            System.arraycopy(value, 0, this.mData, keyBytes.length + insertion + 2, valLen);
        }
    }

    private synchronized String getKey(int index) {
        int avStart = 0;
        for (int i = 0; i < index && avStart < this.mData.length; i++) {
            avStart += this.mData[avStart] + 1;
        }
        if (avStart < this.mData.length) {
            int avLen = this.mData[avStart];
            int aLen = 0;
            while (aLen < avLen && this.mData[avStart + aLen + 1] != 61) {
                aLen++;
            }
            return new String(this.mData, avStart + 1, aLen);
        }
        return null;
    }

    private synchronized byte[] getValue(int index) {
        int avStart = 0;
        for (int avStart2 = 0; avStart2 < index && avStart < this.mData.length; avStart2++) {
            avStart += this.mData[avStart] + 1;
        }
        if (avStart >= this.mData.length) {
            return null;
        }
        int avLen = this.mData[avStart];
        for (int aLen = 0; aLen < avLen; aLen++) {
            if (this.mData[avStart + aLen + 1] == 61) {
                byte[] value = new byte[(avLen - aLen) - 1];
                System.arraycopy(this.mData, avStart + aLen + 2, value, 0, (avLen - aLen) - 1);
                return value;
            }
        }
        return null;
    }

    private synchronized String getValueAsString(int index) {
        byte[] value = getValue(index);
        if (value != null) {
            return new String(value);
        }
        return null;
    }

    private synchronized byte[] getValue(String forKey) {
        int i = 0;
        while (true) {
            String s = getKey(i);
            if (s != null) {
                if (forKey.compareToIgnoreCase(s) != 0) {
                    i++;
                } else {
                    return getValue(i);
                }
            } else {
                return null;
            }
        }
    }

    public String toString() {
        String av;
        String result = null;
        int i = 0;
        while (true) {
            String a = getKey(i);
            if (a == null) {
                break;
            }
            String av2 = "{" + a;
            String val = getValueAsString(i);
            if (val != null) {
                av = av2 + "=" + val + "}";
            } else {
                av = av2 + "}";
            }
            if (result == null) {
                result = av;
            } else {
                result = result + ", " + av;
            }
            i++;
        }
        return result != null ? result : "";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DnsSdTxtRecord)) {
            return false;
        }
        DnsSdTxtRecord record = (DnsSdTxtRecord) o;
        return Arrays.equals(record.mData, this.mData);
    }

    public int hashCode() {
        return Arrays.hashCode(this.mData);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(this.mData);
    }
}
