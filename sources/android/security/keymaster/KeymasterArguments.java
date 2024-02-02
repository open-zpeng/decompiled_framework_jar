package android.security.keymaster;

import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/* loaded from: classes2.dex */
public class KeymasterArguments implements Parcelable {
    public static final long UINT32_MAX_VALUE = 4294967295L;
    private static final long UINT32_RANGE = 4294967296L;
    private List<KeymasterArgument> mArguments;
    private static final BigInteger UINT64_RANGE = BigInteger.ONE.shiftLeft(64);
    public static final BigInteger UINT64_MAX_VALUE = UINT64_RANGE.subtract(BigInteger.ONE);
    private protected static final Parcelable.Creator<KeymasterArguments> CREATOR = new Parcelable.Creator<KeymasterArguments>() { // from class: android.security.keymaster.KeymasterArguments.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KeymasterArguments createFromParcel(Parcel in) {
            return new KeymasterArguments(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public KeymasterArguments[] newArray(int size) {
            return new KeymasterArguments[size];
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public KeymasterArguments() {
        this.mArguments = new ArrayList();
    }

    private synchronized KeymasterArguments(Parcel in) {
        this.mArguments = in.createTypedArrayList(KeymasterArgument.CREATOR);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addEnum(int tag, int value) {
        int tagType = KeymasterDefs.getTagType(tag);
        if (tagType != 268435456 && tagType != 536870912) {
            throw new IllegalArgumentException("Not an enum or repeating enum tag: " + tag);
        }
        addEnumTag(tag, value);
    }

    public void addEnums(int tag, int... values) {
        if (KeymasterDefs.getTagType(tag) != 536870912) {
            throw new IllegalArgumentException("Not a repeating enum tag: " + tag);
        }
        for (int value : values) {
            addEnumTag(tag, value);
        }
    }

    public synchronized int getEnum(int tag, int defaultValue) {
        if (KeymasterDefs.getTagType(tag) != 268435456) {
            throw new IllegalArgumentException("Not an enum tag: " + tag);
        }
        KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        return getEnumTagValue(arg);
    }

    public synchronized List<Integer> getEnums(int tag) {
        if (KeymasterDefs.getTagType(tag) != 536870912) {
            throw new IllegalArgumentException("Not a repeating enum tag: " + tag);
        }
        List<Integer> values = new ArrayList<>();
        for (KeymasterArgument arg : this.mArguments) {
            if (arg.tag == tag) {
                values.add(Integer.valueOf(getEnumTagValue(arg)));
            }
        }
        return values;
    }

    private synchronized void addEnumTag(int tag, int value) {
        this.mArguments.add(new KeymasterIntArgument(tag, value));
    }

    private synchronized int getEnumTagValue(KeymasterArgument arg) {
        return ((KeymasterIntArgument) arg).value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addUnsignedInt(int tag, long value) {
        int tagType = KeymasterDefs.getTagType(tag);
        if (tagType != 805306368 && tagType != 1073741824) {
            throw new IllegalArgumentException("Not an int or repeating int tag: " + tag);
        } else if (value < 0 || value > 4294967295L) {
            throw new IllegalArgumentException("Int tag value out of range: " + value);
        } else {
            this.mArguments.add(new KeymasterIntArgument(tag, (int) value));
        }
    }

    public synchronized long getUnsignedInt(int tag, long defaultValue) {
        if (KeymasterDefs.getTagType(tag) != 805306368) {
            throw new IllegalArgumentException("Not an int tag: " + tag);
        }
        KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        return ((KeymasterIntArgument) arg).value & 4294967295L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addUnsignedLong(int tag, BigInteger value) {
        int tagType = KeymasterDefs.getTagType(tag);
        if (tagType != 1342177280 && tagType != -1610612736) {
            throw new IllegalArgumentException("Not a long or repeating long tag: " + tag);
        }
        addLongTag(tag, value);
    }

    public synchronized List<BigInteger> getUnsignedLongs(int tag) {
        if (KeymasterDefs.getTagType(tag) != -1610612736) {
            throw new IllegalArgumentException("Tag is not a repeating long: " + tag);
        }
        List<BigInteger> values = new ArrayList<>();
        for (KeymasterArgument arg : this.mArguments) {
            if (arg.tag == tag) {
                values.add(getLongTagValue(arg));
            }
        }
        return values;
    }

    private synchronized void addLongTag(int tag, BigInteger value) {
        if (value.signum() == -1 || value.compareTo(UINT64_MAX_VALUE) > 0) {
            throw new IllegalArgumentException("Long tag value out of range: " + value);
        }
        this.mArguments.add(new KeymasterLongArgument(tag, value.longValue()));
    }

    private synchronized BigInteger getLongTagValue(KeymasterArgument arg) {
        return toUint64(((KeymasterLongArgument) arg).value);
    }

    public synchronized void addBoolean(int tag) {
        if (KeymasterDefs.getTagType(tag) != 1879048192) {
            throw new IllegalArgumentException("Not a boolean tag: " + tag);
        }
        this.mArguments.add(new KeymasterBooleanArgument(tag));
    }

    public synchronized boolean getBoolean(int tag) {
        if (KeymasterDefs.getTagType(tag) != 1879048192) {
            throw new IllegalArgumentException("Not a boolean tag: " + tag);
        }
        KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return false;
        }
        return true;
    }

    public synchronized void addBytes(int tag, byte[] value) {
        if (KeymasterDefs.getTagType(tag) != -1879048192) {
            throw new IllegalArgumentException("Not a bytes tag: " + tag);
        } else if (value == null) {
            throw new NullPointerException("value == nulll");
        } else {
            this.mArguments.add(new KeymasterBlobArgument(tag, value));
        }
    }

    public synchronized byte[] getBytes(int tag, byte[] defaultValue) {
        if (KeymasterDefs.getTagType(tag) != -1879048192) {
            throw new IllegalArgumentException("Not a bytes tag: " + tag);
        }
        KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        return ((KeymasterBlobArgument) arg).blob;
    }

    public synchronized void addDate(int tag, Date value) {
        if (KeymasterDefs.getTagType(tag) != 1610612736) {
            throw new IllegalArgumentException("Not a date tag: " + tag);
        } else if (value == null) {
            throw new NullPointerException("value == nulll");
        } else {
            if (value.getTime() < 0) {
                throw new IllegalArgumentException("Date tag value out of range: " + value);
            }
            this.mArguments.add(new KeymasterDateArgument(tag, value));
        }
    }

    public synchronized void addDateIfNotNull(int tag, Date value) {
        if (KeymasterDefs.getTagType(tag) != 1610612736) {
            throw new IllegalArgumentException("Not a date tag: " + tag);
        } else if (value != null) {
            addDate(tag, value);
        }
    }

    public synchronized Date getDate(int tag, Date defaultValue) {
        if (KeymasterDefs.getTagType(tag) != 1610612736) {
            throw new IllegalArgumentException("Tag is not a date type: " + tag);
        }
        KeymasterArgument arg = getArgumentByTag(tag);
        if (arg == null) {
            return defaultValue;
        }
        Date result = ((KeymasterDateArgument) arg).date;
        if (result.getTime() < 0) {
            throw new IllegalArgumentException("Tag value too large. Tag: " + tag);
        }
        return result;
    }

    private synchronized KeymasterArgument getArgumentByTag(int tag) {
        for (KeymasterArgument arg : this.mArguments) {
            if (arg.tag == tag) {
                return arg;
            }
        }
        return null;
    }

    public synchronized boolean containsTag(int tag) {
        return getArgumentByTag(tag) != null;
    }

    public synchronized int size() {
        return this.mArguments.size();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(this.mArguments);
    }

    private protected void readFromParcel(Parcel in) {
        in.readTypedList(this.mArguments, KeymasterArgument.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static synchronized BigInteger toUint64(long value) {
        if (value >= 0) {
            return BigInteger.valueOf(value);
        }
        return BigInteger.valueOf(value).add(UINT64_RANGE);
    }
}
