package android.media;

import java.io.Closeable;
import java.io.IOException;
/* loaded from: classes.dex */
public abstract class Media2DataSource implements Closeable {
    public abstract synchronized long getSize() throws IOException;

    public abstract synchronized int readAt(long j, byte[] bArr, int i, int i2) throws IOException;
}
