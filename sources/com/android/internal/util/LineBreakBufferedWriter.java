package com.android.internal.util;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
/* loaded from: classes3.dex */
public class LineBreakBufferedWriter extends PrintWriter {
    private char[] buffer;
    private int bufferIndex;
    private final int bufferSize;
    private int lastNewline;
    private final String lineSeparator;

    public LineBreakBufferedWriter(Writer out, int bufferSize) {
        this(out, bufferSize, 16);
    }

    public LineBreakBufferedWriter(Writer out, int bufferSize, int initialCapacity) {
        super(out);
        this.lastNewline = -1;
        this.buffer = new char[Math.min(initialCapacity, bufferSize)];
        this.bufferIndex = 0;
        this.bufferSize = bufferSize;
        this.lineSeparator = System.getProperty("line.separator");
    }

    @Override // java.io.PrintWriter, java.io.Writer, java.io.Flushable
    public void flush() {
        writeBuffer(this.bufferIndex);
        this.bufferIndex = 0;
        super.flush();
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(int c) {
        if (this.bufferIndex < this.buffer.length) {
            this.buffer[this.bufferIndex] = (char) c;
            this.bufferIndex++;
            if (((char) c) == '\n') {
                this.lastNewline = this.bufferIndex;
                return;
            }
            return;
        }
        write(new char[]{(char) c}, 0, 1);
    }

    @Override // java.io.PrintWriter
    public void println() {
        write(this.lineSeparator);
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(char[] buf, int off, int len) {
        while (this.bufferIndex + len > this.bufferSize) {
            int maxLength = this.bufferSize - this.bufferIndex;
            int nextNewLine = -1;
            for (int nextNewLine2 = 0; nextNewLine2 < maxLength; nextNewLine2++) {
                if (buf[off + nextNewLine2] == '\n') {
                    if (this.bufferIndex + nextNewLine2 >= this.bufferSize) {
                        break;
                    }
                    nextNewLine = nextNewLine2;
                }
            }
            if (nextNewLine == -1) {
                if (this.lastNewline != -1) {
                    writeBuffer(this.lastNewline);
                    removeFromBuffer(this.lastNewline + 1);
                    this.lastNewline = -1;
                } else {
                    int rest = this.bufferSize - this.bufferIndex;
                    appendToBuffer(buf, off, rest);
                    writeBuffer(this.bufferIndex);
                    this.bufferIndex = 0;
                    off += rest;
                    len -= rest;
                }
            } else {
                appendToBuffer(buf, off, nextNewLine);
                writeBuffer(this.bufferIndex);
                this.bufferIndex = 0;
                this.lastNewline = -1;
                off += nextNewLine + 1;
                len -= nextNewLine + 1;
            }
        }
        if (len > 0) {
            appendToBuffer(buf, off, len);
            for (int i = len - 1; i >= 0; i--) {
                if (buf[off + i] == '\n') {
                    this.lastNewline = (this.bufferIndex - len) + i;
                    return;
                }
            }
        }
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(String s, int off, int len) {
        while (this.bufferIndex + len > this.bufferSize) {
            int maxLength = this.bufferSize - this.bufferIndex;
            int nextNewLine = -1;
            for (int nextNewLine2 = 0; nextNewLine2 < maxLength; nextNewLine2++) {
                if (s.charAt(off + nextNewLine2) == '\n') {
                    if (this.bufferIndex + nextNewLine2 >= this.bufferSize) {
                        break;
                    }
                    nextNewLine = nextNewLine2;
                }
            }
            if (nextNewLine == -1) {
                if (this.lastNewline != -1) {
                    writeBuffer(this.lastNewline);
                    removeFromBuffer(this.lastNewline + 1);
                    this.lastNewline = -1;
                } else {
                    int rest = this.bufferSize - this.bufferIndex;
                    appendToBuffer(s, off, rest);
                    writeBuffer(this.bufferIndex);
                    this.bufferIndex = 0;
                    off += rest;
                    len -= rest;
                }
            } else {
                appendToBuffer(s, off, nextNewLine);
                writeBuffer(this.bufferIndex);
                this.bufferIndex = 0;
                this.lastNewline = -1;
                off += nextNewLine + 1;
                len -= nextNewLine + 1;
            }
        }
        if (len > 0) {
            appendToBuffer(s, off, len);
            for (int i = len - 1; i >= 0; i--) {
                if (s.charAt(off + i) == '\n') {
                    this.lastNewline = (this.bufferIndex - len) + i;
                    return;
                }
            }
        }
    }

    private void appendToBuffer(char[] buf, int off, int len) {
        if (this.bufferIndex + len > this.buffer.length) {
            ensureCapacity(this.bufferIndex + len);
        }
        System.arraycopy(buf, off, this.buffer, this.bufferIndex, len);
        this.bufferIndex += len;
    }

    private void appendToBuffer(String s, int off, int len) {
        if (this.bufferIndex + len > this.buffer.length) {
            ensureCapacity(this.bufferIndex + len);
        }
        s.getChars(off, off + len, this.buffer, this.bufferIndex);
        this.bufferIndex += len;
    }

    private void ensureCapacity(int capacity) {
        int newSize = (this.buffer.length * 2) + 2;
        if (newSize < capacity) {
            newSize = capacity;
        }
        this.buffer = Arrays.copyOf(this.buffer, newSize);
    }

    private void removeFromBuffer(int i) {
        int rest = this.bufferIndex - i;
        if (rest > 0) {
            System.arraycopy(this.buffer, this.bufferIndex - rest, this.buffer, 0, rest);
            this.bufferIndex = rest;
            return;
        }
        this.bufferIndex = 0;
    }

    private void writeBuffer(int length) {
        if (length > 0) {
            super.write(this.buffer, 0, length);
        }
    }
}
