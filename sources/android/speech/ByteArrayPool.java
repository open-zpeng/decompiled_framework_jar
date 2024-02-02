package android.speech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
/* loaded from: classes2.dex */
public final class ByteArrayPool {
    protected static final Comparator<byte[]> BUF_COMPARATOR = new Comparator<byte[]>() { // from class: android.speech.ByteArrayPool.1
        @Override // java.util.Comparator
        public int compare(byte[] lhs, byte[] rhs) {
            return lhs.length - rhs.length;
        }
    };
    private static final int MAX_LIST_SIZE = 40;
    private final int mSizeLimit;
    private List<byte[]> mBuffersByLastUse = new LinkedList();
    private List<byte[]> mBuffersBySize = new ArrayList(40);
    private Object mSync = new Object();
    private int mCurrentSize = 0;

    public ByteArrayPool(int sizeLimit) {
        this.mSizeLimit = sizeLimit;
    }

    public byte[] getBuf(int len) {
        synchronized (this.mSync) {
            for (int i = 0; i < this.mBuffersBySize.size(); i++) {
                byte[] buf = this.mBuffersBySize.get(i);
                if (buf.length == len) {
                    this.mCurrentSize -= buf.length;
                    this.mBuffersBySize.remove(i);
                    this.mBuffersByLastUse.remove(buf);
                    return buf;
                }
            }
            return new byte[len];
        }
    }

    public void returnBuf(byte[] buf) {
        synchronized (this.mSync) {
            if (buf != null) {
                try {
                    if (buf.length <= this.mSizeLimit) {
                        this.mBuffersByLastUse.add(buf);
                        int pos = Collections.binarySearch(this.mBuffersBySize, buf, BUF_COMPARATOR);
                        if (pos < 0) {
                            pos = (-pos) - 1;
                        }
                        this.mBuffersBySize.add(pos, buf);
                        this.mCurrentSize += buf.length;
                        trim();
                    }
                } finally {
                }
            }
        }
    }

    private void trim() {
        synchronized (this.mSync) {
            while (this.mCurrentSize > this.mSizeLimit) {
                byte[] buf = this.mBuffersByLastUse.remove(0);
                this.mBuffersBySize.remove(buf);
                this.mCurrentSize -= buf.length;
            }
        }
    }

    public void releaseBuf() {
        synchronized (this.mSync) {
            this.mBuffersByLastUse.clear();
            this.mBuffersBySize.clear();
        }
    }
}
