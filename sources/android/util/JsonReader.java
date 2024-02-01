package android.util;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import libcore.internal.StringPool;

/* loaded from: classes2.dex */
public final class JsonReader implements Closeable {
    private static final String FALSE = "false";
    private static final String TRUE = "true";
    private final Reader in;
    private String name;
    private boolean skipping;
    private JsonToken token;
    private String value;
    private int valueLength;
    private int valuePos;
    private final StringPool stringPool = new StringPool();
    private boolean lenient = false;
    private final char[] buffer = new char[1024];
    private int pos = 0;
    private int limit = 0;
    private int bufferStartLine = 1;
    private int bufferStartColumn = 1;
    private final List<JsonScope> stack = new ArrayList();

    public JsonReader(Reader in) {
        push(JsonScope.EMPTY_DOCUMENT);
        this.skipping = false;
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.in = in;
    }

    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public void beginArray() throws IOException {
        expect(JsonToken.BEGIN_ARRAY);
    }

    public void endArray() throws IOException {
        expect(JsonToken.END_ARRAY);
    }

    public void beginObject() throws IOException {
        expect(JsonToken.BEGIN_OBJECT);
    }

    public void endObject() throws IOException {
        expect(JsonToken.END_OBJECT);
    }

    private void expect(JsonToken expected) throws IOException {
        peek();
        if (this.token != expected) {
            throw new IllegalStateException("Expected " + expected + " but was " + peek());
        }
        advance();
    }

    public boolean hasNext() throws IOException {
        peek();
        return (this.token == JsonToken.END_OBJECT || this.token == JsonToken.END_ARRAY) ? false : true;
    }

    public JsonToken peek() throws IOException {
        JsonToken jsonToken = this.token;
        if (jsonToken != null) {
            return jsonToken;
        }
        switch (peekStack()) {
            case EMPTY_DOCUMENT:
                replaceTop(JsonScope.NONEMPTY_DOCUMENT);
                JsonToken firstToken = nextValue();
                if (!this.lenient && this.token != JsonToken.BEGIN_ARRAY && this.token != JsonToken.BEGIN_OBJECT) {
                    throw new IOException("Expected JSON document to start with '[' or '{' but was " + this.token);
                }
                return firstToken;
            case EMPTY_ARRAY:
                return nextInArray(true);
            case NONEMPTY_ARRAY:
                return nextInArray(false);
            case EMPTY_OBJECT:
                return nextInObject(true);
            case DANGLING_NAME:
                return objectValue();
            case NONEMPTY_OBJECT:
                return nextInObject(false);
            case NONEMPTY_DOCUMENT:
                try {
                    JsonToken token = nextValue();
                    if (this.lenient) {
                        return token;
                    }
                    throw syntaxError("Expected EOF");
                } catch (EOFException e) {
                    JsonToken jsonToken2 = JsonToken.END_DOCUMENT;
                    this.token = jsonToken2;
                    return jsonToken2;
                }
            case CLOSED:
                throw new IllegalStateException("JsonReader is closed");
            default:
                throw new AssertionError();
        }
    }

    private JsonToken advance() throws IOException {
        peek();
        JsonToken result = this.token;
        this.token = null;
        this.value = null;
        this.name = null;
        return result;
    }

    public String nextName() throws IOException {
        peek();
        if (this.token != JsonToken.NAME) {
            throw new IllegalStateException("Expected a name but was " + peek());
        }
        String result = this.name;
        advance();
        return result;
    }

    public String nextString() throws IOException {
        peek();
        if (this.token != JsonToken.STRING && this.token != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected a string but was " + peek());
        }
        String result = this.value;
        advance();
        return result;
    }

    public boolean nextBoolean() throws IOException {
        peek();
        if (this.token != JsonToken.BOOLEAN) {
            throw new IllegalStateException("Expected a boolean but was " + this.token);
        }
        boolean result = this.value == TRUE;
        advance();
        return result;
    }

    public void nextNull() throws IOException {
        peek();
        if (this.token != JsonToken.NULL) {
            throw new IllegalStateException("Expected null but was " + this.token);
        }
        advance();
    }

    public double nextDouble() throws IOException {
        peek();
        if (this.token != JsonToken.STRING && this.token != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected a double but was " + this.token);
        }
        double result = Double.parseDouble(this.value);
        advance();
        return result;
    }

    public long nextLong() throws IOException {
        long result;
        peek();
        if (this.token != JsonToken.STRING && this.token != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected a long but was " + this.token);
        }
        try {
            result = Long.parseLong(this.value);
        } catch (NumberFormatException e) {
            double asDouble = Double.parseDouble(this.value);
            long result2 = (long) asDouble;
            if (result2 != asDouble) {
                throw new NumberFormatException(this.value);
            }
            result = result2;
        }
        advance();
        return result;
    }

    public int nextInt() throws IOException {
        int result;
        peek();
        if (this.token != JsonToken.STRING && this.token != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected an int but was " + this.token);
        }
        try {
            result = Integer.parseInt(this.value);
        } catch (NumberFormatException e) {
            double asDouble = Double.parseDouble(this.value);
            int result2 = (int) asDouble;
            if (result2 != asDouble) {
                throw new NumberFormatException(this.value);
            }
            result = result2;
        }
        advance();
        return result;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.value = null;
        this.token = null;
        this.stack.clear();
        this.stack.add(JsonScope.CLOSED);
        this.in.close();
    }

    public void skipValue() throws IOException {
        this.skipping = true;
        try {
            if (!hasNext() || peek() == JsonToken.END_DOCUMENT) {
                throw new IllegalStateException("No element left to skip");
            }
            int count = 0;
            do {
                JsonToken token = advance();
                if (token != JsonToken.BEGIN_ARRAY && token != JsonToken.BEGIN_OBJECT) {
                    if (token == JsonToken.END_ARRAY || token == JsonToken.END_OBJECT) {
                        count--;
                        continue;
                    }
                }
                count++;
            } while (count != 0);
        } finally {
            this.skipping = false;
        }
    }

    private JsonScope peekStack() {
        List<JsonScope> list = this.stack;
        return list.get(list.size() - 1);
    }

    private JsonScope pop() {
        List<JsonScope> list = this.stack;
        return list.remove(list.size() - 1);
    }

    private void push(JsonScope newTop) {
        this.stack.add(newTop);
    }

    private void replaceTop(JsonScope newTop) {
        List<JsonScope> list = this.stack;
        list.set(list.size() - 1, newTop);
    }

    private JsonToken nextInArray(boolean firstElement) throws IOException {
        if (firstElement) {
            replaceTop(JsonScope.NONEMPTY_ARRAY);
        } else {
            int nextNonWhitespace = nextNonWhitespace();
            if (nextNonWhitespace != 44) {
                if (nextNonWhitespace != 59) {
                    if (nextNonWhitespace == 93) {
                        pop();
                        JsonToken jsonToken = JsonToken.END_ARRAY;
                        this.token = jsonToken;
                        return jsonToken;
                    }
                    throw syntaxError("Unterminated array");
                }
                checkLenient();
            }
        }
        int nextNonWhitespace2 = nextNonWhitespace();
        if (nextNonWhitespace2 != 44 && nextNonWhitespace2 != 59) {
            if (nextNonWhitespace2 == 93) {
                if (firstElement) {
                    pop();
                    JsonToken jsonToken2 = JsonToken.END_ARRAY;
                    this.token = jsonToken2;
                    return jsonToken2;
                }
            } else {
                this.pos--;
                return nextValue();
            }
        }
        checkLenient();
        this.pos--;
        this.value = "null";
        JsonToken jsonToken3 = JsonToken.NULL;
        this.token = jsonToken3;
        return jsonToken3;
    }

    private JsonToken nextInObject(boolean firstElement) throws IOException {
        if (firstElement) {
            if (nextNonWhitespace() == 125) {
                pop();
                JsonToken jsonToken = JsonToken.END_OBJECT;
                this.token = jsonToken;
                return jsonToken;
            }
            this.pos--;
        } else {
            int nextNonWhitespace = nextNonWhitespace();
            if (nextNonWhitespace != 44 && nextNonWhitespace != 59) {
                if (nextNonWhitespace == 125) {
                    pop();
                    JsonToken jsonToken2 = JsonToken.END_OBJECT;
                    this.token = jsonToken2;
                    return jsonToken2;
                }
                throw syntaxError("Unterminated object");
            }
        }
        int quote = nextNonWhitespace();
        if (quote != 34) {
            if (quote == 39) {
                checkLenient();
            } else {
                checkLenient();
                this.pos--;
                this.name = nextLiteral(false);
                if (this.name.isEmpty()) {
                    throw syntaxError("Expected name");
                }
                replaceTop(JsonScope.DANGLING_NAME);
                JsonToken jsonToken3 = JsonToken.NAME;
                this.token = jsonToken3;
                return jsonToken3;
            }
        }
        this.name = nextString((char) quote);
        replaceTop(JsonScope.DANGLING_NAME);
        JsonToken jsonToken32 = JsonToken.NAME;
        this.token = jsonToken32;
        return jsonToken32;
    }

    private JsonToken objectValue() throws IOException {
        int nextNonWhitespace = nextNonWhitespace();
        if (nextNonWhitespace != 58) {
            if (nextNonWhitespace == 61) {
                checkLenient();
                if (this.pos < this.limit || fillBuffer(1)) {
                    char[] cArr = this.buffer;
                    int i = this.pos;
                    if (cArr[i] == '>') {
                        this.pos = i + 1;
                    }
                }
            } else {
                throw syntaxError("Expected ':'");
            }
        }
        replaceTop(JsonScope.NONEMPTY_OBJECT);
        return nextValue();
    }

    private JsonToken nextValue() throws IOException {
        int c = nextNonWhitespace();
        if (c != 34) {
            if (c != 39) {
                if (c == 91) {
                    push(JsonScope.EMPTY_ARRAY);
                    JsonToken jsonToken = JsonToken.BEGIN_ARRAY;
                    this.token = jsonToken;
                    return jsonToken;
                } else if (c == 123) {
                    push(JsonScope.EMPTY_OBJECT);
                    JsonToken jsonToken2 = JsonToken.BEGIN_OBJECT;
                    this.token = jsonToken2;
                    return jsonToken2;
                } else {
                    this.pos--;
                    return readLiteral();
                }
            }
            checkLenient();
        }
        this.value = nextString((char) c);
        JsonToken jsonToken3 = JsonToken.STRING;
        this.token = jsonToken3;
        return jsonToken3;
    }

    private boolean fillBuffer(int minimum) throws IOException {
        int i;
        int i2;
        int i3 = 0;
        while (true) {
            i = this.pos;
            if (i3 >= i) {
                break;
            }
            if (this.buffer[i3] == '\n') {
                this.bufferStartLine++;
                this.bufferStartColumn = 1;
            } else {
                this.bufferStartColumn++;
            }
            i3++;
        }
        int i4 = this.limit;
        if (i4 != i) {
            this.limit = i4 - i;
            char[] cArr = this.buffer;
            System.arraycopy(cArr, i, cArr, 0, this.limit);
        } else {
            this.limit = 0;
        }
        this.pos = 0;
        do {
            Reader reader = this.in;
            char[] cArr2 = this.buffer;
            int i5 = this.limit;
            int total = reader.read(cArr2, i5, cArr2.length - i5);
            if (total == -1) {
                return false;
            }
            this.limit += total;
            if (this.bufferStartLine == 1 && (i2 = this.bufferStartColumn) == 1 && this.limit > 0 && this.buffer[0] == 65279) {
                this.pos++;
                this.bufferStartColumn = i2 - 1;
            }
        } while (this.limit < minimum);
        return true;
    }

    private int getLineNumber() {
        int result = this.bufferStartLine;
        for (int i = 0; i < this.pos; i++) {
            if (this.buffer[i] == '\n') {
                result++;
            }
        }
        return result;
    }

    private int getColumnNumber() {
        int result = this.bufferStartColumn;
        for (int i = 0; i < this.pos; i++) {
            if (this.buffer[i] == '\n') {
                result = 1;
            } else {
                result++;
            }
        }
        return result;
    }

    private int nextNonWhitespace() throws IOException {
        while (true) {
            if (this.pos < this.limit || fillBuffer(1)) {
                char[] cArr = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                char c = cArr[i];
                if (c != '\t' && c != '\n' && c != '\r' && c != ' ') {
                    if (c == '#') {
                        checkLenient();
                        skipToEndOfLine();
                    } else if (c == '/') {
                        if (this.pos == this.limit && !fillBuffer(1)) {
                            return c;
                        }
                        checkLenient();
                        char[] cArr2 = this.buffer;
                        int i2 = this.pos;
                        char peek = cArr2[i2];
                        if (peek == '*') {
                            this.pos = i2 + 1;
                            if (!skipTo("*/")) {
                                throw syntaxError("Unterminated comment");
                            }
                            this.pos += 2;
                        } else if (peek == '/') {
                            this.pos = i2 + 1;
                            skipToEndOfLine();
                        } else {
                            return c;
                        }
                    } else {
                        return c;
                    }
                }
            } else {
                throw new EOFException("End of input");
            }
        }
    }

    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private void skipToEndOfLine() throws IOException {
        char c;
        do {
            if (this.pos < this.limit || fillBuffer(1)) {
                char[] cArr = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                c = cArr[i];
                if (c == '\r') {
                    return;
                }
            } else {
                return;
            }
        } while (c != '\n');
    }

    private boolean skipTo(String toFind) throws IOException {
        while (true) {
            if (this.pos + toFind.length() <= this.limit || fillBuffer(toFind.length())) {
                for (int c = 0; c < toFind.length(); c++) {
                    if (this.buffer[this.pos + c] != toFind.charAt(c)) {
                        break;
                    }
                }
                return true;
            }
            return false;
            int c2 = this.pos;
            this.pos = c2 + 1;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0054, code lost:
        if (r0 != null) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0056, code lost:
        r0 = new java.lang.StringBuilder();
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x005c, code lost:
        r0.append(r7.buffer, r1, r7.pos - r1);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String nextString(char r8) throws java.io.IOException {
        /*
            r7 = this;
            r0 = 0
        L1:
            int r1 = r7.pos
        L3:
            int r2 = r7.pos
            int r3 = r7.limit
            r4 = 1
            if (r2 >= r3) goto L54
            char[] r3 = r7.buffer
            int r5 = r2 + 1
            r7.pos = r5
            char r2 = r3[r2]
            if (r2 != r8) goto L35
            boolean r5 = r7.skipping
            if (r5 == 0) goto L1c
            java.lang.String r3 = "skipped!"
            return r3
        L1c:
            if (r0 != 0) goto L29
            libcore.internal.StringPool r5 = r7.stringPool
            int r6 = r7.pos
            int r6 = r6 - r1
            int r6 = r6 - r4
            java.lang.String r3 = r5.get(r3, r1, r6)
            return r3
        L29:
            int r5 = r7.pos
            int r5 = r5 - r1
            int r5 = r5 - r4
            r0.append(r3, r1, r5)
            java.lang.String r3 = r0.toString()
            return r3
        L35:
            r3 = 92
            if (r2 != r3) goto L53
            if (r0 != 0) goto L41
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r0 = r3
        L41:
            char[] r3 = r7.buffer
            int r5 = r7.pos
            int r5 = r5 - r1
            int r5 = r5 - r4
            r0.append(r3, r1, r5)
            char r3 = r7.readEscapeCharacter()
            r0.append(r3)
            int r1 = r7.pos
        L53:
            goto L3
        L54:
            if (r0 != 0) goto L5c
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r0 = r2
        L5c:
            char[] r2 = r7.buffer
            int r3 = r7.pos
            int r3 = r3 - r1
            r0.append(r2, r1, r3)
            boolean r1 = r7.fillBuffer(r4)
            if (r1 == 0) goto L6b
            goto L1
        L6b:
            java.lang.String r1 = "Unterminated string"
            java.io.IOException r1 = r7.syntaxError(r1)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.JsonReader.nextString(char):java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x004f, code lost:
        checkLenient();
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x008c, code lost:
        if (r0 != null) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x008e, code lost:
        r6.valuePos = r6.pos;
        r1 = null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String nextLiteral(boolean r7) throws java.io.IOException {
        /*
            r6 = this;
            r0 = 0
            r1 = -1
            r6.valuePos = r1
            r1 = 0
            r6.valueLength = r1
            r2 = 0
        L8:
            int r3 = r6.pos
            int r4 = r3 + r2
            int r5 = r6.limit
            if (r4 >= r5) goto L53
            char[] r4 = r6.buffer
            int r3 = r3 + r2
            char r3 = r4[r3]
            r4 = 9
            if (r3 == r4) goto L52
            r4 = 10
            if (r3 == r4) goto L52
            r4 = 12
            if (r3 == r4) goto L52
            r4 = 13
            if (r3 == r4) goto L52
            r4 = 32
            if (r3 == r4) goto L52
            r4 = 35
            if (r3 == r4) goto L4f
            r4 = 44
            if (r3 == r4) goto L52
            r4 = 47
            if (r3 == r4) goto L4f
            r4 = 61
            if (r3 == r4) goto L4f
            r4 = 123(0x7b, float:1.72E-43)
            if (r3 == r4) goto L52
            r4 = 125(0x7d, float:1.75E-43)
            if (r3 == r4) goto L52
            r4 = 58
            if (r3 == r4) goto L52
            r4 = 59
            if (r3 == r4) goto L4f
            switch(r3) {
                case 91: goto L52;
                case 92: goto L4f;
                case 93: goto L52;
                default: goto L4c;
            }
        L4c:
            int r2 = r2 + 1
            goto L8
        L4f:
            r6.checkLenient()
        L52:
            goto L8a
        L53:
            char[] r3 = r6.buffer
            int r3 = r3.length
            if (r2 >= r3) goto L68
            int r3 = r2 + 1
            boolean r3 = r6.fillBuffer(r3)
            if (r3 == 0) goto L61
            goto L8
        L61:
            char[] r3 = r6.buffer
            int r4 = r6.limit
            r3[r4] = r1
            goto L8a
        L68:
            if (r0 != 0) goto L70
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r0 = r3
        L70:
            char[] r3 = r6.buffer
            int r4 = r6.pos
            r0.append(r3, r4, r2)
            int r3 = r6.valueLength
            int r3 = r3 + r2
            r6.valueLength = r3
            int r3 = r6.pos
            int r3 = r3 + r2
            r6.pos = r3
            r2 = 0
            r3 = 1
            boolean r3 = r6.fillBuffer(r3)
            if (r3 != 0) goto Lbf
        L8a:
            if (r7 == 0) goto L94
            if (r0 != 0) goto L94
            int r1 = r6.pos
            r6.valuePos = r1
            r1 = 0
            goto Lb4
        L94:
            boolean r1 = r6.skipping
            if (r1 == 0) goto L9c
            java.lang.String r1 = "skipped!"
            goto Lb4
        L9c:
            if (r0 != 0) goto La9
            libcore.internal.StringPool r1 = r6.stringPool
            char[] r3 = r6.buffer
            int r4 = r6.pos
            java.lang.String r1 = r1.get(r3, r4, r2)
            goto Lb4
        La9:
            char[] r1 = r6.buffer
            int r3 = r6.pos
            r0.append(r1, r3, r2)
            java.lang.String r1 = r0.toString()
        Lb4:
            int r3 = r6.valueLength
            int r3 = r3 + r2
            r6.valueLength = r3
            int r3 = r6.pos
            int r3 = r3 + r2
            r6.pos = r3
            return r1
        Lbf:
            goto L8
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.JsonReader.nextLiteral(boolean):java.lang.String");
    }

    public String toString() {
        return getClass().getSimpleName() + " near " + ((Object) getSnippet());
    }

    private char readEscapeCharacter() throws IOException {
        if (this.pos == this.limit && !fillBuffer(1)) {
            throw syntaxError("Unterminated escape sequence");
        }
        char[] cArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        char escaped = cArr[i];
        if (escaped != 'b') {
            if (escaped != 'f') {
                if (escaped != 'n') {
                    if (escaped != 'r') {
                        if (escaped != 't') {
                            if (escaped == 'u') {
                                if (this.pos + 4 > this.limit && !fillBuffer(4)) {
                                    throw syntaxError("Unterminated escape sequence");
                                }
                                String hex = this.stringPool.get(this.buffer, this.pos, 4);
                                this.pos += 4;
                                return (char) Integer.parseInt(hex, 16);
                            }
                            return escaped;
                        }
                        return '\t';
                    }
                    return '\r';
                }
                return '\n';
            }
            return '\f';
        }
        return '\b';
    }

    private JsonToken readLiteral() throws IOException {
        this.value = nextLiteral(true);
        if (this.valueLength == 0) {
            throw syntaxError("Expected literal value");
        }
        this.token = decodeLiteral();
        if (this.token == JsonToken.STRING) {
            checkLenient();
        }
        return this.token;
    }

    private JsonToken decodeLiteral() throws IOException {
        int i = this.valuePos;
        if (i == -1) {
            return JsonToken.STRING;
        }
        if (this.valueLength == 4) {
            char[] cArr = this.buffer;
            if ('n' == cArr[i] || 'N' == cArr[i]) {
                char[] cArr2 = this.buffer;
                int i2 = this.valuePos;
                if ('u' == cArr2[i2 + 1] || 'U' == cArr2[i2 + 1]) {
                    char[] cArr3 = this.buffer;
                    int i3 = this.valuePos;
                    if ('l' == cArr3[i3 + 2] || 'L' == cArr3[i3 + 2]) {
                        char[] cArr4 = this.buffer;
                        int i4 = this.valuePos;
                        if ('l' == cArr4[i4 + 3] || 'L' == cArr4[i4 + 3]) {
                            this.value = "null";
                            return JsonToken.NULL;
                        }
                    }
                }
            }
        }
        if (this.valueLength == 4) {
            char[] cArr5 = this.buffer;
            int i5 = this.valuePos;
            if ('t' == cArr5[i5] || 'T' == cArr5[i5]) {
                char[] cArr6 = this.buffer;
                int i6 = this.valuePos;
                if ('r' == cArr6[i6 + 1] || 'R' == cArr6[i6 + 1]) {
                    char[] cArr7 = this.buffer;
                    int i7 = this.valuePos;
                    if ('u' == cArr7[i7 + 2] || 'U' == cArr7[i7 + 2]) {
                        char[] cArr8 = this.buffer;
                        int i8 = this.valuePos;
                        if ('e' == cArr8[i8 + 3] || 'E' == cArr8[i8 + 3]) {
                            this.value = TRUE;
                            return JsonToken.BOOLEAN;
                        }
                    }
                }
            }
        }
        if (this.valueLength == 5) {
            char[] cArr9 = this.buffer;
            int i9 = this.valuePos;
            if ('f' == cArr9[i9] || 'F' == cArr9[i9]) {
                char[] cArr10 = this.buffer;
                int i10 = this.valuePos;
                if ('a' == cArr10[i10 + 1] || 'A' == cArr10[i10 + 1]) {
                    char[] cArr11 = this.buffer;
                    int i11 = this.valuePos;
                    if ('l' == cArr11[i11 + 2] || 'L' == cArr11[i11 + 2]) {
                        char[] cArr12 = this.buffer;
                        int i12 = this.valuePos;
                        if ('s' == cArr12[i12 + 3] || 'S' == cArr12[i12 + 3]) {
                            char[] cArr13 = this.buffer;
                            int i13 = this.valuePos;
                            if ('e' == cArr13[i13 + 4] || 'E' == cArr13[i13 + 4]) {
                                this.value = FALSE;
                                return JsonToken.BOOLEAN;
                            }
                        }
                    }
                }
            }
        }
        this.value = this.stringPool.get(this.buffer, this.valuePos, this.valueLength);
        return decodeNumber(this.buffer, this.valuePos, this.valueLength);
    }

    private JsonToken decodeNumber(char[] chars, int offset, int length) {
        int i;
        char c;
        int i2 = offset;
        char c2 = chars[i2];
        if (c2 == '-') {
            i2++;
            c2 = chars[i2];
        }
        if (c2 == '0') {
            i = i2 + 1;
            c = chars[i];
        } else if (c2 >= '1' && c2 <= '9') {
            i = i2 + 1;
            c = chars[i];
            while (c >= '0' && c <= '9') {
                i++;
                c = chars[i];
            }
        } else {
            return JsonToken.STRING;
        }
        if (c == '.') {
            i++;
            c = chars[i];
            while (c >= '0' && c <= '9') {
                i++;
                c = chars[i];
            }
        }
        if (c == 'e' || c == 'E') {
            int i3 = i + 1;
            char c3 = chars[i3];
            if (c3 == '+' || c3 == '-') {
                i3++;
                c3 = chars[i3];
            }
            if (c3 >= '0' && c3 <= '9') {
                i = i3 + 1;
                char c4 = chars[i];
                while (c4 >= '0' && c4 <= '9') {
                    i++;
                    c4 = chars[i];
                }
            } else {
                return JsonToken.STRING;
            }
        }
        if (i == offset + length) {
            return JsonToken.NUMBER;
        }
        return JsonToken.STRING;
    }

    private IOException syntaxError(String message) throws IOException {
        throw new MalformedJsonException(message + " at line " + getLineNumber() + " column " + getColumnNumber());
    }

    private CharSequence getSnippet() {
        StringBuilder snippet = new StringBuilder();
        int beforePos = Math.min(this.pos, 20);
        snippet.append(this.buffer, this.pos - beforePos, beforePos);
        int afterPos = Math.min(this.limit - this.pos, 20);
        snippet.append(this.buffer, this.pos, afterPos);
        return snippet;
    }
}
