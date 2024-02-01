package android.net.wifi.aware;

import java.nio.BufferOverflowException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import libcore.io.Memory;
/* loaded from: classes2.dex */
public class TlvBufferUtils {
    private synchronized TlvBufferUtils() {
    }

    /* loaded from: classes2.dex */
    public static class TlvConstructor {
        private byte[] mArray;
        private int mArrayLength;
        private int mLengthSize;
        private int mPosition;
        private int mTypeSize;

        public synchronized TlvConstructor(int typeSize, int lengthSize) {
            if (typeSize < 0 || typeSize > 2 || lengthSize <= 0 || lengthSize > 2) {
                throw new IllegalArgumentException("Invalid sizes - typeSize=" + typeSize + ", lengthSize=" + lengthSize);
            }
            this.mTypeSize = typeSize;
            this.mLengthSize = lengthSize;
        }

        public synchronized TlvConstructor wrap(byte[] array) {
            this.mArray = array;
            this.mArrayLength = array == null ? 0 : array.length;
            return this;
        }

        public synchronized TlvConstructor allocate(int capacity) {
            this.mArray = new byte[capacity];
            this.mArrayLength = capacity;
            return this;
        }

        public synchronized TlvConstructor allocateAndPut(List<byte[]> list) {
            if (list != null) {
                int size = 0;
                for (byte[] field : list) {
                    size += this.mTypeSize + this.mLengthSize;
                    if (field != null) {
                        size += field.length;
                    }
                }
                allocate(size);
                for (byte[] field2 : list) {
                    putByteArray(0, field2);
                }
            }
            return this;
        }

        public synchronized TlvConstructor putByte(int type, byte b) {
            checkLength(1);
            addHeader(type, 1);
            byte[] bArr = this.mArray;
            int i = this.mPosition;
            this.mPosition = i + 1;
            bArr[i] = b;
            return this;
        }

        public synchronized TlvConstructor putByteArray(int type, byte[] array, int offset, int length) {
            checkLength(length);
            addHeader(type, length);
            if (length != 0) {
                System.arraycopy(array, offset, this.mArray, this.mPosition, length);
            }
            this.mPosition += length;
            return this;
        }

        public synchronized TlvConstructor putByteArray(int type, byte[] array) {
            return putByteArray(type, array, 0, array == null ? 0 : array.length);
        }

        public synchronized TlvConstructor putZeroLengthElement(int type) {
            checkLength(0);
            addHeader(type, 0);
            return this;
        }

        public synchronized TlvConstructor putShort(int type, short data) {
            checkLength(2);
            addHeader(type, 2);
            Memory.pokeShort(this.mArray, this.mPosition, data, ByteOrder.BIG_ENDIAN);
            this.mPosition += 2;
            return this;
        }

        public synchronized TlvConstructor putInt(int type, int data) {
            checkLength(4);
            addHeader(type, 4);
            Memory.pokeInt(this.mArray, this.mPosition, data, ByteOrder.BIG_ENDIAN);
            this.mPosition += 4;
            return this;
        }

        public synchronized TlvConstructor putString(int type, String data) {
            byte[] bytes = null;
            int length = 0;
            if (data != null) {
                bytes = data.getBytes();
                length = bytes.length;
            }
            return putByteArray(type, bytes, 0, length);
        }

        public synchronized byte[] getArray() {
            return Arrays.copyOf(this.mArray, getActualLength());
        }

        private synchronized int getActualLength() {
            return this.mPosition;
        }

        private synchronized void checkLength(int dataLength) {
            if (this.mPosition + this.mTypeSize + this.mLengthSize + dataLength > this.mArrayLength) {
                throw new BufferOverflowException();
            }
        }

        private synchronized void addHeader(int type, int length) {
            if (this.mTypeSize == 1) {
                this.mArray[this.mPosition] = (byte) type;
            } else if (this.mTypeSize == 2) {
                Memory.pokeShort(this.mArray, this.mPosition, (short) type, ByteOrder.BIG_ENDIAN);
            }
            this.mPosition += this.mTypeSize;
            if (this.mLengthSize == 1) {
                this.mArray[this.mPosition] = (byte) length;
            } else if (this.mLengthSize == 2) {
                Memory.pokeShort(this.mArray, this.mPosition, (short) length, ByteOrder.BIG_ENDIAN);
            }
            this.mPosition += this.mLengthSize;
        }
    }

    /* loaded from: classes2.dex */
    public static class TlvElement {
        public int length;
        public int offset;
        public byte[] refArray;
        public int type;

        private synchronized TlvElement(int type, int length, byte[] refArray, int offset) {
            this.type = type;
            this.length = length;
            this.refArray = refArray;
            this.offset = offset;
            if (offset + length > refArray.length) {
                throw new BufferOverflowException();
            }
        }

        public synchronized byte getByte() {
            if (this.length != 1) {
                throw new IllegalArgumentException("Accesing a byte from a TLV element of length " + this.length);
            }
            return this.refArray[this.offset];
        }

        public synchronized short getShort() {
            if (this.length != 2) {
                throw new IllegalArgumentException("Accesing a short from a TLV element of length " + this.length);
            }
            return Memory.peekShort(this.refArray, this.offset, ByteOrder.BIG_ENDIAN);
        }

        public synchronized int getInt() {
            if (this.length != 4) {
                throw new IllegalArgumentException("Accesing an int from a TLV element of length " + this.length);
            }
            return Memory.peekInt(this.refArray, this.offset, ByteOrder.BIG_ENDIAN);
        }

        public synchronized String getString() {
            return new String(this.refArray, this.offset, this.length);
        }
    }

    /* loaded from: classes2.dex */
    public static class TlvIterable implements Iterable<TlvElement> {
        private byte[] mArray;
        private int mArrayLength;
        private int mLengthSize;
        private int mTypeSize;

        public synchronized TlvIterable(int typeSize, int lengthSize, byte[] array) {
            if (typeSize < 0 || typeSize > 2 || lengthSize <= 0 || lengthSize > 2) {
                throw new IllegalArgumentException("Invalid sizes - typeSize=" + typeSize + ", lengthSize=" + lengthSize);
            }
            this.mTypeSize = typeSize;
            this.mLengthSize = lengthSize;
            this.mArray = array;
            this.mArrayLength = array == null ? 0 : array.length;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            boolean first = true;
            Iterator<TlvElement> it = iterator();
            while (it.hasNext()) {
                TlvElement tlv = it.next();
                if (!first) {
                    builder.append(",");
                }
                first = false;
                builder.append(" (");
                if (this.mTypeSize != 0) {
                    builder.append("T=" + tlv.type + ",");
                }
                builder.append("L=" + tlv.length + ") ");
                if (tlv.length == 0) {
                    builder.append("<null>");
                } else if (tlv.length == 1) {
                    builder.append((int) tlv.getByte());
                } else if (tlv.length == 2) {
                    builder.append((int) tlv.getShort());
                } else if (tlv.length == 4) {
                    builder.append(tlv.getInt());
                } else {
                    builder.append("<bytes>");
                }
                if (tlv.length != 0) {
                    builder.append(" (S='" + tlv.getString() + "')");
                }
            }
            builder.append("]");
            return builder.toString();
        }

        public synchronized List<byte[]> toList() {
            List<byte[]> list = new ArrayList<>();
            Iterator<TlvElement> it = iterator();
            while (it.hasNext()) {
                TlvElement tlv = it.next();
                list.add(Arrays.copyOfRange(tlv.refArray, tlv.offset, tlv.offset + tlv.length));
            }
            return list;
        }

        @Override // java.lang.Iterable
        public Iterator<TlvElement> iterator() {
            return new Iterator<TlvElement>() { // from class: android.net.wifi.aware.TlvBufferUtils.TlvIterable.1
                private int mOffset = 0;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.mOffset < TlvIterable.this.mArrayLength;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public TlvElement next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    short s = 0;
                    if (TlvIterable.this.mTypeSize == 1) {
                        s = TlvIterable.this.mArray[this.mOffset];
                    } else if (TlvIterable.this.mTypeSize == 2) {
                        s = Memory.peekShort(TlvIterable.this.mArray, this.mOffset, ByteOrder.BIG_ENDIAN);
                    }
                    this.mOffset += TlvIterable.this.mTypeSize;
                    short s2 = 0;
                    if (TlvIterable.this.mLengthSize == 1) {
                        s2 = TlvIterable.this.mArray[this.mOffset];
                    } else if (TlvIterable.this.mLengthSize == 2) {
                        s2 = Memory.peekShort(TlvIterable.this.mArray, this.mOffset, ByteOrder.BIG_ENDIAN);
                    }
                    this.mOffset += TlvIterable.this.mLengthSize;
                    TlvElement tlv = new TlvElement(s, s2, TlvIterable.this.mArray, this.mOffset);
                    this.mOffset += s2;
                    return tlv;
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    public static synchronized boolean isValid(byte[] array, int typeSize, int lengthSize) {
        if (typeSize < 0 || typeSize > 2) {
            throw new IllegalArgumentException("Invalid arguments - typeSize must be 0, 1, or 2: typeSize=" + typeSize);
        } else if (lengthSize <= 0 || lengthSize > 2) {
            throw new IllegalArgumentException("Invalid arguments - lengthSize must be 1 or 2: lengthSize=" + lengthSize);
        } else if (array == null) {
            return true;
        } else {
            int nextTlvIndex = 0;
            while (nextTlvIndex + typeSize + lengthSize <= array.length) {
                int nextTlvIndex2 = nextTlvIndex + typeSize;
                if (lengthSize == 1) {
                    nextTlvIndex = nextTlvIndex2 + array[nextTlvIndex2] + lengthSize;
                } else {
                    nextTlvIndex = nextTlvIndex2 + Memory.peekShort(array, nextTlvIndex2, ByteOrder.BIG_ENDIAN) + lengthSize;
                }
            }
            if (nextTlvIndex == array.length) {
                return true;
            }
            return false;
        }
    }
}
