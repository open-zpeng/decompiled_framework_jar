package android.util;

import android.hardware.camera2.legacy.LegacyRequestMapper;
import java.io.UnsupportedEncodingException;
/* loaded from: classes2.dex */
public class Base64 {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int CRLF = 4;
    public static final int DEFAULT = 0;
    public static final int NO_CLOSE = 16;
    public static final int NO_PADDING = 1;
    public static final int NO_WRAP = 2;
    public static final int URL_SAFE = 8;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static abstract class Coder {
        public int op;
        public byte[] output;

        public abstract synchronized int maxOutputSize(int i);

        public abstract synchronized boolean process(byte[] bArr, int i, int i2, boolean z);

        synchronized Coder() {
        }
    }

    public static byte[] decode(String str, int flags) {
        return decode(str.getBytes(), flags);
    }

    public static byte[] decode(byte[] input, int flags) {
        return decode(input, 0, input.length, flags);
    }

    public static byte[] decode(byte[] input, int offset, int len, int flags) {
        Decoder decoder = new Decoder(flags, new byte[(len * 3) / 4]);
        if (!decoder.process(input, offset, len, true)) {
            throw new IllegalArgumentException("bad base-64");
        }
        if (decoder.op == decoder.output.length) {
            return decoder.output;
        }
        byte[] temp = new byte[decoder.op];
        System.arraycopy(decoder.output, 0, temp, 0, decoder.op);
        return temp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class Decoder extends Coder {
        private static final int[] DECODE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] DECODE_WEBSAFE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int EQUALS = -2;
        private static final int SKIP = -1;
        private final int[] alphabet;
        private int state;
        private int value;

        public synchronized Decoder(int flags, byte[] output) {
            this.output = output;
            this.alphabet = (flags & 8) == 0 ? DECODE : DECODE_WEBSAFE;
            this.state = 0;
            this.value = 0;
        }

        @Override // android.util.Base64.Coder
        public synchronized int maxOutputSize(int len) {
            return ((len * 3) / 4) + 10;
        }

        /* JADX WARN: Removed duplicated region for block: B:53:0x00e8  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x00ef  */
        /* JADX WARN: Removed duplicated region for block: B:69:0x00e5 A[SYNTHETIC] */
        @Override // android.util.Base64.Coder
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public synchronized boolean process(byte[] r12, int r13, int r14, boolean r15) {
            /*
                Method dump skipped, instructions count: 310
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.util.Base64.Decoder.process(byte[], int, int, boolean):boolean");
        }
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(encode(input, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static String encodeToString(byte[] input, int offset, int len, int flags) {
        try {
            return new String(encode(input, offset, len, flags), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] encode(byte[] input, int flags) {
        return encode(input, 0, input.length, flags);
    }

    public static byte[] encode(byte[] input, int offset, int len, int flags) {
        Encoder encoder = new Encoder(flags, null);
        int output_len = (len / 3) * 4;
        if (encoder.do_padding) {
            if (len % 3 > 0) {
                output_len += 4;
            }
        } else {
            switch (len % 3) {
                case 1:
                    output_len += 2;
                    break;
                case 2:
                    output_len += 3;
                    break;
            }
        }
        if (encoder.do_newline && len > 0) {
            output_len += (((len - 1) / 57) + 1) * (encoder.do_cr ? 2 : 1);
        }
        encoder.output = new byte[output_len];
        encoder.process(input, offset, len, true);
        return encoder.output;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class Encoder extends Coder {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final byte[] ENCODE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, LegacyRequestMapper.DEFAULT_JPEG_QUALITY, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
        private static final byte[] ENCODE_WEBSAFE = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, LegacyRequestMapper.DEFAULT_JPEG_QUALITY, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        public static final int LINE_GROUPS = 19;
        private final byte[] alphabet;
        private int count;
        public final boolean do_cr;
        public final boolean do_newline;
        public final boolean do_padding;
        private final byte[] tail;
        int tailLen;

        public synchronized Encoder(int flags, byte[] output) {
            this.output = output;
            this.do_padding = (flags & 1) == 0;
            this.do_newline = (flags & 2) == 0;
            this.do_cr = (flags & 4) != 0;
            this.alphabet = (flags & 8) == 0 ? ENCODE : ENCODE_WEBSAFE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = this.do_newline ? 19 : -1;
        }

        @Override // android.util.Base64.Coder
        public synchronized int maxOutputSize(int len) {
            return ((len * 8) / 5) + 10;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.util.Base64.Coder
        public synchronized boolean process(byte[] input, int offset, int len, boolean finish) {
            int p;
            int op;
            int p2;
            int p3;
            int p4;
            int p5;
            int op2;
            byte[] alphabet = this.alphabet;
            byte[] output = this.output;
            int op3 = 0;
            int count = this.count;
            int len2 = len + offset;
            int v = -1;
            switch (this.tailLen) {
                case 0:
                default:
                    p = offset;
                    break;
                case 1:
                    if (offset + 2 <= len2) {
                        int p6 = offset + 1;
                        int p7 = input[offset];
                        p = p6 + 1;
                        v = ((p7 & 255) << 8) | ((this.tail[0] & 255) << 16) | (input[p6] & 255);
                        this.tailLen = 0;
                        break;
                    }
                    p = offset;
                    break;
                case 2:
                    if (offset + 1 <= len2) {
                        int i = ((this.tail[0] & 255) << 16) | ((this.tail[1] & 255) << 8);
                        int p8 = offset + 1;
                        int p9 = input[offset];
                        v = i | (p9 & 255);
                        this.tailLen = 0;
                        p = p8;
                        break;
                    }
                    p = offset;
                    break;
            }
            if (v != -1) {
                int op4 = 0 + 1;
                output[0] = alphabet[(v >> 18) & 63];
                int op5 = op4 + 1;
                output[op4] = alphabet[(v >> 12) & 63];
                int op6 = op5 + 1;
                output[op5] = alphabet[(v >> 6) & 63];
                op3 = op6 + 1;
                output[op6] = alphabet[v & 63];
                count--;
                if (count == 0) {
                    if (this.do_cr) {
                        output[op3] = 13;
                        op3++;
                    }
                    int op7 = op3 + 1;
                    output[op3] = 10;
                    count = 19;
                    op3 = op7;
                }
            }
            while (p + 3 <= len2) {
                int v2 = ((input[p] & 255) << 16) | ((input[p + 1] & 255) << 8) | (input[p + 2] & 255);
                output[op3] = alphabet[(v2 >> 18) & 63];
                output[op3 + 1] = alphabet[(v2 >> 12) & 63];
                output[op3 + 2] = alphabet[(v2 >> 6) & 63];
                output[op3 + 3] = alphabet[v2 & 63];
                p += 3;
                op3 += 4;
                count--;
                if (count == 0) {
                    if (this.do_cr) {
                        output[op3] = 13;
                        op3++;
                    }
                    int op8 = op3 + 1;
                    output[op3] = 10;
                    count = 19;
                    op3 = op8;
                }
            }
            if (finish) {
                if (p - this.tailLen == len2 - 1) {
                    int t = 0;
                    if (this.tailLen > 0) {
                        int t2 = 0 + 1;
                        int t3 = this.tail[0];
                        p2 = p;
                        p5 = t3;
                        t = t2;
                    } else {
                        p2 = p + 1;
                        p5 = input[p];
                    }
                    int v3 = (p5 & 255) << 4;
                    this.tailLen -= t;
                    int op9 = op3 + 1;
                    output[op3] = alphabet[(v3 >> 6) & 63];
                    op3 = op9 + 1;
                    output[op9] = alphabet[v3 & 63];
                    if (this.do_padding) {
                        int op10 = op3 + 1;
                        output[op3] = 61;
                        op3 = op10 + 1;
                        output[op10] = 61;
                    }
                    if (this.do_newline) {
                        if (this.do_cr) {
                            op2 = op3 + 1;
                            output[op3] = 13;
                        } else {
                            op2 = op3;
                        }
                        op3 = op2 + 1;
                        output[op2] = 10;
                    }
                } else if (p - this.tailLen == len2 - 2) {
                    int t4 = 0;
                    if (this.tailLen > 1) {
                        int t5 = 0 + 1;
                        int t6 = this.tail[0];
                        p2 = p;
                        p3 = t6;
                        t4 = t5;
                    } else {
                        p2 = p + 1;
                        p3 = input[p];
                    }
                    int i2 = (p3 & 255) << 10;
                    if (this.tailLen > 0) {
                        p4 = this.tail[t4];
                        t4++;
                    } else {
                        int p10 = p2 + 1;
                        int p11 = input[p2];
                        p2 = p10;
                        p4 = p11;
                    }
                    int v4 = i2 | ((p4 & 255) << 2);
                    this.tailLen -= t4;
                    int op11 = op3 + 1;
                    output[op3] = alphabet[(v4 >> 12) & 63];
                    int op12 = op11 + 1;
                    output[op11] = alphabet[(v4 >> 6) & 63];
                    int op13 = op12 + 1;
                    output[op12] = alphabet[v4 & 63];
                    if (this.do_padding) {
                        output[op13] = 61;
                        op13++;
                    }
                    if (this.do_newline) {
                        if (this.do_cr) {
                            output[op13] = 13;
                            op13++;
                        }
                        output[op13] = 10;
                        op13++;
                    }
                    op3 = op13;
                } else if (this.do_newline && op3 > 0 && count != 19) {
                    if (this.do_cr) {
                        op = op3 + 1;
                        output[op3] = 13;
                    } else {
                        op = op3;
                    }
                    op3 = op + 1;
                    output[op] = 10;
                }
            } else if (p == len2 - 1) {
                byte[] bArr = this.tail;
                int i3 = this.tailLen;
                this.tailLen = i3 + 1;
                bArr[i3] = input[p];
            } else if (p == len2 - 2) {
                byte[] bArr2 = this.tail;
                int i4 = this.tailLen;
                this.tailLen = i4 + 1;
                bArr2[i4] = input[p];
                byte[] bArr3 = this.tail;
                int i5 = this.tailLen;
                this.tailLen = i5 + 1;
                bArr3[i5] = input[p + 1];
            }
            this.op = op3;
            this.count = count;
            return true;
        }
    }
}
