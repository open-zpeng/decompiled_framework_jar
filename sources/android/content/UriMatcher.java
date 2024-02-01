package android.content;

import android.annotation.UnsupportedAppUsage;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class UriMatcher {
    private static final int EXACT = 0;
    public static final int NO_MATCH = -1;
    private static final int NUMBER = 1;
    private static final int TEXT = 2;
    @UnsupportedAppUsage
    private ArrayList<UriMatcher> mChildren;
    private int mCode;
    @UnsupportedAppUsage
    private final String mText;
    private final int mWhich;

    public UriMatcher(int code) {
        this.mCode = code;
        this.mWhich = -1;
        this.mChildren = new ArrayList<>();
        this.mText = null;
    }

    private UriMatcher(int which, String text) {
        this.mCode = -1;
        this.mWhich = which;
        this.mChildren = new ArrayList<>();
        this.mText = text;
    }

    public void addURI(String authority, String path, int code) {
        if (code < 0) {
            throw new IllegalArgumentException("code " + code + " is invalid: it must be positive");
        }
        String[] tokens = null;
        if (path != null) {
            String newPath = path;
            if (path.length() > 1 && path.charAt(0) == '/') {
                newPath = path.substring(1);
            }
            tokens = newPath.split("/");
        }
        int numTokens = tokens != null ? tokens.length : 0;
        UriMatcher node = this;
        int i = -1;
        while (i < numTokens) {
            String token = i < 0 ? authority : tokens[i];
            ArrayList<UriMatcher> children = node.mChildren;
            int numChildren = children.size();
            int j = 0;
            while (true) {
                if (j >= numChildren) {
                    break;
                }
                UriMatcher child = children.get(j);
                if (!token.equals(child.mText)) {
                    j++;
                } else {
                    node = child;
                    break;
                }
            }
            if (j == numChildren) {
                UriMatcher child2 = createChild(token);
                node.mChildren.add(child2);
                node = child2;
            }
            i++;
        }
        node.mCode = code;
    }

    private static UriMatcher createChild(String token) {
        char c;
        int hashCode = token.hashCode();
        if (hashCode != 35) {
            if (hashCode == 42 && token.equals("*")) {
                c = 1;
            }
            c = 65535;
        } else {
            if (token.equals("#")) {
                c = 0;
            }
            c = 65535;
        }
        if (c != 0) {
            if (c == 1) {
                return new UriMatcher(2, "*");
            }
            return new UriMatcher(0, token);
        }
        return new UriMatcher(1, "#");
    }

    public int match(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        int li = pathSegments.size();
        UriMatcher node = this;
        if (li == 0 && uri.getAuthority() == null) {
            return this.mCode;
        }
        int i = -1;
        while (i < li) {
            String u = i < 0 ? uri.getAuthority() : pathSegments.get(i);
            ArrayList<UriMatcher> list = node.mChildren;
            if (list == null) {
                break;
            }
            node = null;
            int lj = list.size();
            for (int j = 0; j < lj; j++) {
                UriMatcher n = list.get(j);
                int i2 = n.mWhich;
                if (i2 != 0) {
                    if (i2 == 1) {
                        int lk = u.length();
                        int k = 0;
                        while (true) {
                            if (k < lk) {
                                char c = u.charAt(k);
                                if (c < '0' || c > '9') {
                                    break;
                                }
                                k++;
                            } else {
                                node = n;
                                break;
                            }
                        }
                    } else if (i2 == 2) {
                        node = n;
                    }
                } else if (n.mText.equals(u)) {
                    node = n;
                }
                if (node != null) {
                    break;
                }
            }
            if (node != null) {
                i++;
            } else {
                return -1;
            }
        }
        int i3 = node.mCode;
        return i3;
    }
}
