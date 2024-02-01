package android.util;

import android.os.Parcel;
import java.util.Objects;

/* loaded from: classes2.dex */
public final class TimestampedValue<T> {
    private final long mReferenceTimeMillis;
    private final T mValue;

    public TimestampedValue(long referenceTimeMillis, T value) {
        this.mReferenceTimeMillis = referenceTimeMillis;
        this.mValue = value;
    }

    public long getReferenceTimeMillis() {
        return this.mReferenceTimeMillis;
    }

    public T getValue() {
        return this.mValue;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimestampedValue<?> that = (TimestampedValue) o;
        if (this.mReferenceTimeMillis == that.mReferenceTimeMillis && Objects.equals(this.mValue, that.mValue)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mReferenceTimeMillis), this.mValue);
    }

    public String toString() {
        return "TimestampedValue{mReferenceTimeMillis=" + this.mReferenceTimeMillis + ", mValue=" + this.mValue + '}';
    }

    public static <T> TimestampedValue<T> readFromParcel(Parcel in, ClassLoader classLoader, Class<? extends T> valueClass) {
        long referenceTimeMillis = in.readLong();
        Object readValue = in.readValue(classLoader);
        if (readValue != null && !valueClass.isAssignableFrom(readValue.getClass())) {
            throw new RuntimeException("Value was of type " + readValue.getClass() + " is not assignable to " + valueClass);
        }
        return new TimestampedValue<>(referenceTimeMillis, readValue);
    }

    public static void writeToParcel(Parcel dest, TimestampedValue<?> timestampedValue) {
        dest.writeLong(((TimestampedValue) timestampedValue).mReferenceTimeMillis);
        dest.writeValue(((TimestampedValue) timestampedValue).mValue);
    }

    public static long referenceTimeDifference(TimestampedValue<?> one, TimestampedValue<?> two) {
        return ((TimestampedValue) one).mReferenceTimeMillis - ((TimestampedValue) two).mReferenceTimeMillis;
    }
}
