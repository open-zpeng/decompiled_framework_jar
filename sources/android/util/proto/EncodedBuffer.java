package android.util.proto;

import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiNetworkScoreCache;
import android.os.BatteryStats;
import android.util.Log;
import com.android.internal.midi.MidiConstants;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public final class EncodedBuffer {
    private static final String TAG = "EncodedBuffer";
    private int mBufferCount;
    private final ArrayList<byte[]> mBuffers;
    private final int mChunkSize;
    private int mReadBufIndex;
    private byte[] mReadBuffer;
    private int mReadIndex;
    private int mReadLimit;
    private int mReadableSize;
    private int mWriteBufIndex;
    private byte[] mWriteBuffer;
    private int mWriteIndex;

    public EncodedBuffer() {
        this(0);
    }

    public EncodedBuffer(int chunkSize) {
        this.mBuffers = new ArrayList<>();
        this.mReadLimit = -1;
        this.mReadableSize = -1;
        this.mChunkSize = chunkSize <= 0 ? 8192 : chunkSize;
        this.mWriteBuffer = new byte[this.mChunkSize];
        this.mBuffers.add(this.mWriteBuffer);
        this.mBufferCount = 1;
    }

    public void startEditing() {
        this.mReadableSize = (this.mWriteBufIndex * this.mChunkSize) + this.mWriteIndex;
        this.mReadLimit = this.mWriteIndex;
        this.mWriteBuffer = this.mBuffers.get(0);
        this.mWriteIndex = 0;
        this.mWriteBufIndex = 0;
        this.mReadBuffer = this.mWriteBuffer;
        this.mReadBufIndex = 0;
        this.mReadIndex = 0;
    }

    public void rewindRead() {
        this.mReadBuffer = this.mBuffers.get(0);
        this.mReadBufIndex = 0;
        this.mReadIndex = 0;
    }

    public int getReadableSize() {
        return this.mReadableSize;
    }

    public int getReadPos() {
        return (this.mReadBufIndex * this.mChunkSize) + this.mReadIndex;
    }

    public void skipRead(int amount) {
        if (amount < 0) {
            throw new RuntimeException("skipRead with negative amount=" + amount);
        } else if (amount == 0) {
        } else {
            if (amount <= this.mChunkSize - this.mReadIndex) {
                this.mReadIndex += amount;
                return;
            }
            int amount2 = amount - (this.mChunkSize - this.mReadIndex);
            this.mReadIndex = amount2 % this.mChunkSize;
            if (this.mReadIndex == 0) {
                this.mReadIndex = this.mChunkSize;
                this.mReadBufIndex += amount2 / this.mChunkSize;
            } else {
                this.mReadBufIndex += 1 + (amount2 / this.mChunkSize);
            }
            this.mReadBuffer = this.mBuffers.get(this.mReadBufIndex);
        }
    }

    public byte readRawByte() {
        if (this.mReadBufIndex > this.mBufferCount || (this.mReadBufIndex == this.mBufferCount - 1 && this.mReadIndex >= this.mReadLimit)) {
            throw new IndexOutOfBoundsException("Trying to read too much data mReadBufIndex=" + this.mReadBufIndex + " mBufferCount=" + this.mBufferCount + " mReadIndex=" + this.mReadIndex + " mReadLimit=" + this.mReadLimit);
        }
        if (this.mReadIndex >= this.mChunkSize) {
            this.mReadBufIndex++;
            this.mReadBuffer = this.mBuffers.get(this.mReadBufIndex);
            this.mReadIndex = 0;
        }
        byte[] bArr = this.mReadBuffer;
        int i = this.mReadIndex;
        this.mReadIndex = i + 1;
        return bArr[i];
    }

    public long readRawUnsigned() {
        int bits = 0;
        long result = 0;
        do {
            byte b = readRawByte();
            result |= (b & Byte.MAX_VALUE) << bits;
            if ((b & 128) == 0) {
                return result;
            }
            bits += 7;
        } while (bits <= 64);
        throw new ProtoParseException("Varint too long -- " + getDebugString());
    }

    public int readRawFixed32() {
        return (readRawByte() & 255) | ((readRawByte() & 255) << 8) | ((readRawByte() & 255) << 16) | ((readRawByte() & 255) << 24);
    }

    private synchronized void nextWriteBuffer() {
        this.mWriteBufIndex++;
        if (this.mWriteBufIndex >= this.mBufferCount) {
            this.mWriteBuffer = new byte[this.mChunkSize];
            this.mBuffers.add(this.mWriteBuffer);
            this.mBufferCount++;
        } else {
            this.mWriteBuffer = this.mBuffers.get(this.mWriteBufIndex);
        }
        this.mWriteIndex = 0;
    }

    public void writeRawByte(byte val) {
        if (this.mWriteIndex >= this.mChunkSize) {
            nextWriteBuffer();
        }
        byte[] bArr = this.mWriteBuffer;
        int i = this.mWriteIndex;
        this.mWriteIndex = i + 1;
        bArr[i] = val;
    }

    public static int getRawVarint32Size(int val) {
        if ((val & WifiNetworkScoreCache.INVALID_NETWORK_SCORE) == 0) {
            return 1;
        }
        if ((val & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & val) == 0) {
            return 3;
        }
        return ((-268435456) & val) == 0 ? 4 : 5;
    }

    public void writeRawVarint32(int val) {
        while ((val & WifiNetworkScoreCache.INVALID_NETWORK_SCORE) != 0) {
            writeRawByte((byte) ((val & 127) | 128));
            val >>>= 7;
        }
        writeRawByte((byte) val);
    }

    public static int getRawZigZag32Size(int val) {
        return getRawVarint32Size(zigZag32(val));
    }

    public void writeRawZigZag32(int val) {
        writeRawVarint32(zigZag32(val));
    }

    public static int getRawVarint64Size(long val) {
        if (((-128) & val) == 0) {
            return 1;
        }
        if (((-16384) & val) == 0) {
            return 2;
        }
        if (((-2097152) & val) == 0) {
            return 3;
        }
        if (((-268435456) & val) == 0) {
            return 4;
        }
        if (((-34359738368L) & val) == 0) {
            return 5;
        }
        if (((-4398046511104L) & val) == 0) {
            return 6;
        }
        if (((-562949953421312L) & val) == 0) {
            return 7;
        }
        if ((BatteryStats.STEP_LEVEL_MODIFIED_MODE_MASK & val) == 0) {
            return 8;
        }
        return (Long.MIN_VALUE & val) == 0 ? 9 : 10;
    }

    public void writeRawVarint64(long val) {
        while (((-128) & val) != 0) {
            writeRawByte((byte) ((127 & val) | 128));
            val >>>= 7;
        }
        writeRawByte((byte) val);
    }

    public static int getRawZigZag64Size(long val) {
        return getRawVarint64Size(zigZag64(val));
    }

    public void writeRawZigZag64(long val) {
        writeRawVarint64(zigZag64(val));
    }

    public void writeRawFixed32(int val) {
        writeRawByte((byte) val);
        writeRawByte((byte) (val >> 8));
        writeRawByte((byte) (val >> 16));
        writeRawByte((byte) (val >> 24));
    }

    public void writeRawFixed64(long val) {
        writeRawByte((byte) val);
        writeRawByte((byte) (val >> 8));
        writeRawByte((byte) (val >> 16));
        writeRawByte((byte) (val >> 24));
        writeRawByte((byte) (val >> 32));
        writeRawByte((byte) (val >> 40));
        writeRawByte((byte) (val >> 48));
        writeRawByte((byte) (val >> 56));
    }

    public void writeRawBuffer(byte[] val) {
        if (val != null && val.length > 0) {
            writeRawBuffer(val, 0, val.length);
        }
    }

    public void writeRawBuffer(byte[] val, int offset, int length) {
        if (val == null) {
            return;
        }
        int amt = length < this.mChunkSize - this.mWriteIndex ? length : this.mChunkSize - this.mWriteIndex;
        if (amt > 0) {
            System.arraycopy(val, offset, this.mWriteBuffer, this.mWriteIndex, amt);
            this.mWriteIndex += amt;
            length -= amt;
            offset += amt;
        }
        while (length > 0) {
            nextWriteBuffer();
            int amt2 = length < this.mChunkSize ? length : this.mChunkSize;
            System.arraycopy(val, offset, this.mWriteBuffer, this.mWriteIndex, amt2);
            this.mWriteIndex += amt2;
            length -= amt2;
            offset += amt2;
        }
    }

    public void writeFromThisBuffer(int srcOffset, int size) {
        if (this.mReadLimit < 0) {
            throw new IllegalStateException("writeFromThisBuffer before startEditing");
        }
        if (srcOffset < getWritePos()) {
            throw new IllegalArgumentException("Can only move forward in the buffer -- srcOffset=" + srcOffset + " size=" + size + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + getDebugString());
        } else if (srcOffset + size > this.mReadableSize) {
            throw new IllegalArgumentException("Trying to move more data than there is -- srcOffset=" + srcOffset + " size=" + size + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + getDebugString());
        } else if (size == 0) {
        } else {
            if (srcOffset == (this.mWriteBufIndex * this.mChunkSize) + this.mWriteIndex) {
                if (size <= this.mChunkSize - this.mWriteIndex) {
                    this.mWriteIndex += size;
                    return;
                }
                int size2 = size - (this.mChunkSize - this.mWriteIndex);
                this.mWriteIndex = size2 % this.mChunkSize;
                if (this.mWriteIndex == 0) {
                    this.mWriteIndex = this.mChunkSize;
                    this.mWriteBufIndex += size2 / this.mChunkSize;
                } else {
                    this.mWriteBufIndex += 1 + (size2 / this.mChunkSize);
                }
                this.mWriteBuffer = this.mBuffers.get(this.mWriteBufIndex);
                return;
            }
            int readBufIndex = srcOffset / this.mChunkSize;
            byte[] readBuffer = this.mBuffers.get(readBufIndex);
            int readIndex = srcOffset % this.mChunkSize;
            while (size > 0) {
                if (this.mWriteIndex >= this.mChunkSize) {
                    nextWriteBuffer();
                }
                if (readIndex >= this.mChunkSize) {
                    readBufIndex++;
                    byte[] readBuffer2 = this.mBuffers.get(readBufIndex);
                    readBuffer = readBuffer2;
                    readIndex = 0;
                }
                int spaceInWriteBuffer = this.mChunkSize - this.mWriteIndex;
                int availableInReadBuffer = this.mChunkSize - readIndex;
                int amt = Math.min(size, Math.min(spaceInWriteBuffer, availableInReadBuffer));
                System.arraycopy(readBuffer, readIndex, this.mWriteBuffer, this.mWriteIndex, amt);
                this.mWriteIndex += amt;
                readIndex += amt;
                size -= amt;
            }
        }
    }

    public int getWritePos() {
        return (this.mWriteBufIndex * this.mChunkSize) + this.mWriteIndex;
    }

    public void rewindWriteTo(int writePos) {
        if (writePos > getWritePos()) {
            throw new RuntimeException("rewindWriteTo only can go backwards" + writePos);
        }
        this.mWriteBufIndex = writePos / this.mChunkSize;
        this.mWriteIndex = writePos % this.mChunkSize;
        if (this.mWriteIndex == 0 && this.mWriteBufIndex != 0) {
            this.mWriteIndex = this.mChunkSize;
            this.mWriteBufIndex--;
        }
        this.mWriteBuffer = this.mBuffers.get(this.mWriteBufIndex);
    }

    public int getRawFixed32At(int pos) {
        return (this.mBuffers.get(pos / this.mChunkSize)[pos % this.mChunkSize] & 255) | ((this.mBuffers.get((pos + 1) / this.mChunkSize)[(pos + 1) % this.mChunkSize] & 255) << 8) | ((this.mBuffers.get((pos + 2) / this.mChunkSize)[(pos + 2) % this.mChunkSize] & 255) << 16) | ((255 & this.mBuffers.get((pos + 3) / this.mChunkSize)[(pos + 3) % this.mChunkSize]) << 24);
    }

    public void editRawFixed32(int pos, int val) {
        this.mBuffers.get(pos / this.mChunkSize)[pos % this.mChunkSize] = (byte) val;
        this.mBuffers.get((pos + 1) / this.mChunkSize)[(pos + 1) % this.mChunkSize] = (byte) (val >> 8);
        this.mBuffers.get((pos + 2) / this.mChunkSize)[(pos + 2) % this.mChunkSize] = (byte) (val >> 16);
        this.mBuffers.get((pos + 3) / this.mChunkSize)[(pos + 3) % this.mChunkSize] = (byte) (val >> 24);
    }

    private static synchronized int zigZag32(int val) {
        return (val << 1) ^ (val >> 31);
    }

    private static synchronized long zigZag64(long val) {
        return (val << 1) ^ (val >> 63);
    }

    public byte[] getBytes(int size) {
        byte[] result = new byte[size];
        int bufCount = size / this.mChunkSize;
        int writeIndex = 0;
        int writeIndex2 = 0;
        while (writeIndex2 < bufCount) {
            System.arraycopy(this.mBuffers.get(writeIndex2), 0, result, writeIndex, this.mChunkSize);
            writeIndex += this.mChunkSize;
            writeIndex2++;
        }
        int lastSize = size - (this.mChunkSize * bufCount);
        if (lastSize > 0) {
            System.arraycopy(this.mBuffers.get(writeIndex2), 0, result, writeIndex, lastSize);
        }
        return result;
    }

    public int getChunkCount() {
        return this.mBuffers.size();
    }

    public int getWriteIndex() {
        return this.mWriteIndex;
    }

    public int getWriteBufIndex() {
        return this.mWriteBufIndex;
    }

    public String getDebugString() {
        return "EncodedBuffer( mChunkSize=" + this.mChunkSize + " mBuffers.size=" + this.mBuffers.size() + " mBufferCount=" + this.mBufferCount + " mWriteIndex=" + this.mWriteIndex + " mWriteBufIndex=" + this.mWriteBufIndex + " mReadBufIndex=" + this.mReadBufIndex + " mReadIndex=" + this.mReadIndex + " mReadableSize=" + this.mReadableSize + " mReadLimit=" + this.mReadLimit + " )";
    }

    public void dumpBuffers(String tag) {
        int N = this.mBuffers.size();
        int start = 0;
        for (int i = 0; i < N; i++) {
            start += dumpByteString(tag, "{" + i + "} ", start, this.mBuffers.get(i));
        }
    }

    public static void dumpByteString(String tag, String prefix, byte[] buf) {
        dumpByteString(tag, prefix, 0, buf);
    }

    private static synchronized int dumpByteString(String tag, String prefix, int start, byte[] buf) {
        StringBuffer sb = new StringBuffer();
        int length = buf.length;
        for (int i = 0; i < length; i++) {
            if (i % 16 == 0) {
                if (i != 0) {
                    Log.d(tag, sb.toString());
                    sb = new StringBuffer();
                }
                sb.append(prefix);
                sb.append('[');
                sb.append(start + i);
                sb.append(']');
                sb.append(' ');
            } else {
                sb.append(' ');
            }
            byte b = buf[i];
            byte c = (byte) ((b >> 4) & 15);
            if (c < 10) {
                sb.append((char) (48 + c));
            } else {
                sb.append((char) (87 + c));
            }
            byte d = (byte) (b & MidiConstants.STATUS_CHANNEL_MASK);
            if (d < 10) {
                sb.append((char) (48 + d));
            } else {
                sb.append((char) (87 + d));
            }
        }
        Log.d(tag, sb.toString());
        return length;
    }
}
