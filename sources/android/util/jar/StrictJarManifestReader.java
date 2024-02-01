package android.util.jar;

import android.util.jar.StrictJarManifest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class StrictJarManifestReader {
    private final byte[] buf;
    private final int endOfMainSection;
    private Attributes.Name name;
    private int pos;
    private String value;
    private final HashMap<String, Attributes.Name> attributeNameCache = new HashMap<>();
    private final ByteArrayOutputStream valueBuffer = new ByteArrayOutputStream(80);
    private int consecutiveLineBreaks = 0;

    public StrictJarManifestReader(byte[] buf, Attributes main) throws IOException {
        this.buf = buf;
        while (readHeader()) {
            main.put(this.name, this.value);
        }
        this.endOfMainSection = this.pos;
    }

    public void readEntries(Map<String, Attributes> entries, Map<String, StrictJarManifest.Chunk> chunks) throws IOException {
        int mark = this.pos;
        while (readHeader()) {
            if (!Attributes.Name.NAME.equals(this.name)) {
                throw new IOException("Entry is not named");
            }
            String entryNameValue = this.value;
            Attributes entry = entries.get(entryNameValue);
            if (entry == null) {
                entry = new Attributes(12);
            }
            while (readHeader()) {
                entry.put(this.name, this.value);
            }
            if (chunks != null) {
                if (chunks.get(entryNameValue) != null) {
                    throw new IOException("A jar verifier does not support more than one entry with the same name");
                }
                chunks.put(entryNameValue, new StrictJarManifest.Chunk(mark, this.pos));
                mark = this.pos;
            }
            entries.put(entryNameValue, entry);
        }
    }

    public int getEndOfMainSection() {
        return this.endOfMainSection;
    }

    private boolean readHeader() throws IOException {
        if (this.consecutiveLineBreaks > 1) {
            this.consecutiveLineBreaks = 0;
            return false;
        }
        readName();
        this.consecutiveLineBreaks = 0;
        readValue();
        return this.consecutiveLineBreaks > 0;
    }

    private void readName() throws IOException {
        int mark = this.pos;
        while (this.pos < this.buf.length) {
            byte[] bArr = this.buf;
            int i = this.pos;
            this.pos = i + 1;
            if (bArr[i] == 58) {
                String nameString = new String(this.buf, mark, (this.pos - mark) - 1, StandardCharsets.US_ASCII);
                byte[] bArr2 = this.buf;
                int i2 = this.pos;
                this.pos = i2 + 1;
                if (bArr2[i2] != 32) {
                    throw new IOException(String.format("Invalid value for attribute '%s'", nameString));
                }
                try {
                    this.name = this.attributeNameCache.get(nameString);
                    if (this.name == null) {
                        this.name = new Attributes.Name(nameString);
                        this.attributeNameCache.put(nameString, this.name);
                        return;
                    }
                    return;
                } catch (IllegalArgumentException e) {
                    throw new IOException(e.getMessage());
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0064, code lost:
        r7.valueBuffer.write(r7.buf, r1, r2 - r1);
        r7.value = r7.valueBuffer.toString(java.nio.charset.StandardCharsets.UTF_8.name());
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x007b, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readValue() throws java.io.IOException {
        /*
            r7 = this;
            r0 = 0
            int r1 = r7.pos
            int r2 = r7.pos
            java.io.ByteArrayOutputStream r3 = r7.valueBuffer
            r3.reset()
        La:
            int r3 = r7.pos
            byte[] r4 = r7.buf
            int r4 = r4.length
            if (r3 >= r4) goto L64
            byte[] r3 = r7.buf
            int r4 = r7.pos
            int r5 = r4 + 1
            r7.pos = r5
            r3 = r3[r4]
            if (r3 == 0) goto L5c
            r4 = 10
            r5 = 1
            if (r3 == r4) goto L52
            r4 = 13
            if (r3 == r4) goto L4b
            r4 = 32
            if (r3 == r4) goto L2b
            goto L3e
        L2b:
            int r4 = r7.consecutiveLineBreaks
            if (r4 != r5) goto L3e
            java.io.ByteArrayOutputStream r4 = r7.valueBuffer
            byte[] r5 = r7.buf
            int r6 = r2 - r1
            r4.write(r5, r1, r6)
            int r1 = r7.pos
            r4 = 0
            r7.consecutiveLineBreaks = r4
            goto La
        L3e:
            int r4 = r7.consecutiveLineBreaks
            if (r4 < r5) goto L48
            int r4 = r7.pos
            int r4 = r4 - r5
            r7.pos = r4
            goto L64
        L48:
            int r2 = r7.pos
            goto La
        L4b:
            r0 = 1
            int r4 = r7.consecutiveLineBreaks
            int r4 = r4 + r5
            r7.consecutiveLineBreaks = r4
            goto La
        L52:
            if (r0 == 0) goto L56
            r0 = 0
            goto La
        L56:
            int r4 = r7.consecutiveLineBreaks
            int r4 = r4 + r5
            r7.consecutiveLineBreaks = r4
            goto La
        L5c:
            java.io.IOException r4 = new java.io.IOException
            java.lang.String r5 = "NUL character in a manifest"
            r4.<init>(r5)
            throw r4
        L64:
            java.io.ByteArrayOutputStream r3 = r7.valueBuffer
            byte[] r4 = r7.buf
            int r5 = r2 - r1
            r3.write(r4, r1, r5)
            java.io.ByteArrayOutputStream r3 = r7.valueBuffer
            java.nio.charset.Charset r4 = java.nio.charset.StandardCharsets.UTF_8
            java.lang.String r4 = r4.name()
            java.lang.String r3 = r3.toString(r4)
            r7.value = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.jar.StrictJarManifestReader.readValue():void");
    }
}
