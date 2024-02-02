package android.text;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.provider.UserDictionary;
import android.view.View;
import com.android.internal.R;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes2.dex */
public class AutoText {
    private static final int DEFAULT = 14337;
    private static final int INCREMENT = 1024;
    private static final int RIGHT = 9300;
    private static final int TRIE_C = 0;
    private static final int TRIE_CHILD = 2;
    private static final int TRIE_NEXT = 3;
    private static final char TRIE_NULL = 65535;
    private static final int TRIE_OFF = 1;
    private static final int TRIE_ROOT = 0;
    private static final int TRIE_SIZEOF = 4;
    private static AutoText sInstance = new AutoText(Resources.getSystem());
    private static Object sLock = new Object();
    private Locale mLocale;
    private int mSize;
    private String mText;
    private char[] mTrie;
    private char mTrieUsed;

    private synchronized AutoText(Resources resources) {
        this.mLocale = resources.getConfiguration().locale;
        init(resources);
    }

    private static synchronized AutoText getInstance(View view) {
        AutoText instance;
        Resources res = view.getContext().getResources();
        Locale locale = res.getConfiguration().locale;
        synchronized (sLock) {
            instance = sInstance;
            if (!locale.equals(instance.mLocale)) {
                instance = new AutoText(res);
                sInstance = instance;
            }
        }
        return instance;
    }

    public static String get(CharSequence src, int start, int end, View view) {
        return getInstance(view).lookup(src, start, end);
    }

    public static int getSize(View view) {
        return getInstance(view).getSize();
    }

    private synchronized int getSize() {
        return this.mSize;
    }

    private synchronized String lookup(CharSequence src, int start, int end) {
        char c = this.mTrie[0];
        for (int here = start; here < end; here++) {
            char c2 = src.charAt(here);
            while (true) {
                if (c == 65535) {
                    break;
                } else if (c2 != this.mTrie[c + 0]) {
                    c = this.mTrie[c + 3];
                } else if (here == end - 1 && this.mTrie[c + 1] != 65535) {
                    char c3 = this.mTrie[c + 1];
                    int len = this.mText.charAt(c3);
                    return this.mText.substring(c3 + 1, c3 + 1 + len);
                } else {
                    c = this.mTrie[c + 2];
                }
            }
            if (c == 65535) {
                return null;
            }
        }
        return null;
    }

    private synchronized void init(Resources r) {
        char off;
        XmlResourceParser parser = r.getXml(R.xml.autotext);
        StringBuilder right = new StringBuilder((int) RIGHT);
        this.mTrie = new char[14337];
        this.mTrie[0] = TRIE_NULL;
        this.mTrieUsed = (char) 1;
        try {
            try {
                XmlUtils.beginDocument(parser, "words");
                while (true) {
                    XmlUtils.nextElement(parser);
                    String element = parser.getName();
                    if (element == null || !element.equals(UserDictionary.Words.WORD)) {
                        break;
                    }
                    String src = parser.getAttributeValue(null, "src");
                    if (parser.next() == 4) {
                        String dest = parser.getText();
                        if (dest.equals("")) {
                            off = 0;
                        } else {
                            off = (char) right.length();
                            right.append((char) dest.length());
                            right.append(dest);
                        }
                        add(src, off);
                    }
                }
                r.flushLayoutCache();
                parser.close();
                this.mText = right.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (XmlPullParserException e2) {
                throw new RuntimeException(e2);
            }
        } catch (Throwable th) {
            parser.close();
            throw th;
        }
    }

    private synchronized void add(String src, char off) {
        int slen = src.length();
        this.mSize++;
        int herep = 0;
        for (int herep2 = 0; herep2 < slen; herep2++) {
            char c = src.charAt(herep2);
            int herep3 = herep;
            boolean found = false;
            while (true) {
                if (this.mTrie[herep3] == 65535) {
                    break;
                } else if (c != this.mTrie[this.mTrie[herep3] + 0]) {
                    herep3 = this.mTrie[herep3] + 3;
                } else if (herep2 == slen - 1) {
                    this.mTrie[this.mTrie[herep3] + 1] = off;
                    return;
                } else {
                    herep3 = this.mTrie[herep3] + 2;
                    found = true;
                }
            }
            if (found) {
                herep = herep3;
            } else {
                char node = newTrieNode();
                this.mTrie[herep3] = node;
                this.mTrie[this.mTrie[herep3] + 0] = c;
                this.mTrie[this.mTrie[herep3] + 1] = TRIE_NULL;
                this.mTrie[this.mTrie[herep3] + 3] = TRIE_NULL;
                this.mTrie[this.mTrie[herep3] + 2] = TRIE_NULL;
                if (herep2 == slen - 1) {
                    this.mTrie[this.mTrie[herep3] + 1] = off;
                    return;
                }
                herep = this.mTrie[herep3] + 2;
            }
        }
    }

    private synchronized char newTrieNode() {
        if (this.mTrieUsed + 4 > this.mTrie.length) {
            char[] copy = new char[this.mTrie.length + 1024];
            System.arraycopy(this.mTrie, 0, copy, 0, this.mTrie.length);
            this.mTrie = copy;
        }
        char ret = this.mTrieUsed;
        this.mTrieUsed = (char) (this.mTrieUsed + 4);
        return ret;
    }
}
