package android.net;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/* loaded from: classes2.dex */
public final class UriCodec {
    private static final char INVALID_INPUT_CHARACTER = 65533;

    private UriCodec() {
    }

    private static int hexCharToValue(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        }
        if ('a' <= c && c <= 'f') {
            return (c + '\n') - 97;
        }
        if ('A' <= c && c <= 'F') {
            return (c + '\n') - 65;
        }
        return -1;
    }

    private static URISyntaxException unexpectedCharacterException(String uri, String name, char unexpected, int index) {
        String nameString;
        if (name == null) {
            nameString = "";
        } else {
            nameString = " in [" + name + "]";
        }
        return new URISyntaxException(uri, "Unexpected character" + nameString + ": " + unexpected, index);
    }

    private static char getNextCharacter(String uri, int index, int end, String name) throws URISyntaxException {
        String nameString;
        if (index >= end) {
            if (name == null) {
                nameString = "";
            } else {
                nameString = " in [" + name + "]";
            }
            throw new URISyntaxException(uri, "Unexpected end of string" + nameString, index);
        }
        return uri.charAt(index);
    }

    public static String decode(String s, boolean convertPlus, Charset charset, boolean throwOnFailure) {
        StringBuilder builder = new StringBuilder(s.length());
        appendDecoded(builder, s, convertPlus, charset, throwOnFailure);
        return builder.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x008d, code lost:
        r1.put(r4);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void appendDecoded(java.lang.StringBuilder r10, java.lang.String r11, boolean r12, java.nio.charset.Charset r13, boolean r14) {
        /*
            java.nio.charset.CharsetDecoder r0 = r13.newDecoder()
            java.nio.charset.CodingErrorAction r1 = java.nio.charset.CodingErrorAction.REPLACE
            java.nio.charset.CharsetDecoder r0 = r0.onMalformedInput(r1)
            java.lang.String r1 = "�"
            java.nio.charset.CharsetDecoder r0 = r0.replaceWith(r1)
            java.nio.charset.CodingErrorAction r1 = java.nio.charset.CodingErrorAction.REPORT
            java.nio.charset.CharsetDecoder r0 = r0.onUnmappableCharacter(r1)
            int r1 = r11.length()
            java.nio.ByteBuffer r1 = java.nio.ByteBuffer.allocate(r1)
            r2 = 0
        L20:
            int r3 = r11.length()
            if (r2 >= r3) goto L92
            char r3 = r11.charAt(r2)
            int r2 = r2 + 1
            r4 = 37
            if (r3 == r4) goto L46
            r4 = 43
            if (r3 == r4) goto L3b
            flushDecodingByteAccumulator(r10, r0, r1, r14)
            r10.append(r3)
            goto L91
        L3b:
            flushDecodingByteAccumulator(r10, r0, r1, r14)
            if (r12 == 0) goto L42
            r4 = 32
        L42:
            r10.append(r4)
            goto L91
        L46:
            r4 = 0
            r5 = 0
        L48:
            r6 = 2
            if (r5 >= r6) goto L8d
            r6 = 65533(0xfffd, float:9.1831E-41)
            int r7 = r11.length()     // Catch: java.net.URISyntaxException -> L7d
            r8 = 0
            char r7 = getNextCharacter(r11, r2, r7, r8)     // Catch: java.net.URISyntaxException -> L7d
            r3 = r7
            int r2 = r2 + 1
            int r7 = hexCharToValue(r3)
            if (r7 >= 0) goto L76
            if (r14 != 0) goto L6a
            flushDecodingByteAccumulator(r10, r0, r1, r14)
            r10.append(r6)
            goto L8d
        L6a:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            int r9 = r2 + (-1)
            java.net.URISyntaxException r8 = unexpectedCharacterException(r11, r8, r3, r9)
            r6.<init>(r8)
            throw r6
        L76:
            int r6 = r4 * 16
            int r6 = r6 + r7
            byte r4 = (byte) r6
            int r5 = r5 + 1
            goto L48
        L7d:
            r7 = move-exception
            if (r14 != 0) goto L87
            flushDecodingByteAccumulator(r10, r0, r1, r14)
            r10.append(r6)
            return
        L87:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            r6.<init>(r7)
            throw r6
        L8d:
            r1.put(r4)
        L91:
            goto L20
        L92:
            flushDecodingByteAccumulator(r10, r0, r1, r14)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.UriCodec.appendDecoded(java.lang.StringBuilder, java.lang.String, boolean, java.nio.charset.Charset, boolean):void");
    }

    private static void flushDecodingByteAccumulator(StringBuilder builder, CharsetDecoder decoder, ByteBuffer byteBuffer, boolean throwOnFailure) {
        if (byteBuffer.position() == 0) {
            return;
        }
        byteBuffer.flip();
        try {
            try {
                builder.append((CharSequence) decoder.decode(byteBuffer));
            } catch (CharacterCodingException e) {
                if (throwOnFailure) {
                    throw new IllegalArgumentException(e);
                }
                builder.append(INVALID_INPUT_CHARACTER);
            }
        } finally {
            byteBuffer.flip();
            byteBuffer.limit(byteBuffer.capacity());
        }
    }
}
