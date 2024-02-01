package android.content.pm;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.crypto.Mac;
/* loaded from: classes.dex */
public class MacAuthenticatedInputStream extends FilterInputStream {
    private final Mac mMac;

    public synchronized MacAuthenticatedInputStream(InputStream in, Mac mac) {
        super(in);
        this.mMac = mac;
    }

    public synchronized boolean isTagEqual(byte[] tag) {
        byte[] actualTag = this.mMac.doFinal();
        if (tag == null || actualTag == null || tag.length != actualTag.length) {
            return false;
        }
        int value = 0;
        for (int value2 = 0; value2 < tag.length; value2++) {
            value |= tag[value2] ^ actualTag[value2];
        }
        return value == 0;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int b = super.read();
        if (b >= 0) {
            this.mMac.update((byte) b);
        }
        return b;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer, int offset, int count) throws IOException {
        int numRead = super.read(buffer, offset, count);
        if (numRead > 0) {
            this.mMac.update(buffer, offset, numRead);
        }
        return numRead;
    }
}
