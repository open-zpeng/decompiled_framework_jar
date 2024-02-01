package android.content;

import com.android.internal.telephony.PhoneConstants;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class UriMatcher {
    private static final int EXACT = 0;
    public static final int NO_MATCH = -1;
    private static final int NUMBER = 1;
    private static final int TEXT = 2;
    public protected ArrayList<UriMatcher> mChildren;
    private int mCode;
    public protected String mText;
    private int mWhich;

    public UriMatcher(int code) {
        this.mCode = code;
        this.mWhich = -1;
        this.mChildren = new ArrayList<>();
        this.mText = null;
    }

    private synchronized UriMatcher() {
        this.mCode = -1;
        this.mWhich = -1;
        this.mChildren = new ArrayList<>();
        this.mText = null;
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
                UriMatcher child2 = new UriMatcher();
                if (token.equals("#")) {
                    child2.mWhich = 1;
                } else if (token.equals(PhoneConstants.APN_TYPE_ALL)) {
                    child2.mWhich = 2;
                } else {
                    child2.mWhich = 0;
                }
                child2.mText = token;
                node.mChildren.add(child2);
                node = child2;
            }
            i++;
        }
        node.mCode = code;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0074 A[LOOP:0: B:9:0x001c->B:40:0x0074, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0073 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int match(android.net.Uri r17) {
        /*
            r16 = this;
            java.util.List r0 = r17.getPathSegments()
            int r1 = r0.size()
            r2 = r16
            if (r1 != 0) goto L17
            java.lang.String r3 = r17.getAuthority()
            if (r3 != 0) goto L17
            r3 = r16
            int r4 = r3.mCode
            return r4
        L17:
            r3 = r16
            r4 = -1
            r5 = r2
            r2 = r4
        L1c:
            if (r2 >= r1) goto L77
            if (r2 >= 0) goto L25
            java.lang.String r6 = r17.getAuthority()
            goto L2b
        L25:
            java.lang.Object r6 = r0.get(r2)
            java.lang.String r6 = (java.lang.String) r6
        L2b:
            java.util.ArrayList<android.content.UriMatcher> r7 = r5.mChildren
            if (r7 != 0) goto L30
            goto L77
        L30:
            r5 = 0
            int r8 = r7.size()
            r9 = 0
            r10 = r5
            r5 = r9
        L38:
            if (r5 >= r8) goto L70
            java.lang.Object r11 = r7.get(r5)
            android.content.UriMatcher r11 = (android.content.UriMatcher) r11
            int r12 = r11.mWhich
            switch(r12) {
                case 0: goto L61;
                case 1: goto L48;
                case 2: goto L46;
                default: goto L45;
            }
        L45:
            goto L6a
        L46:
            r10 = r11
            goto L6a
        L48:
            int r12 = r6.length()
            r13 = r9
        L4d:
            if (r13 >= r12) goto L5f
            char r14 = r6.charAt(r13)
            r15 = 48
            if (r14 < r15) goto L6a
            r15 = 57
            if (r14 <= r15) goto L5c
            goto L6a
        L5c:
            int r13 = r13 + 1
            goto L4d
        L5f:
            r10 = r11
            goto L6a
        L61:
            java.lang.String r12 = r11.mText
            boolean r12 = r12.equals(r6)
            if (r12 == 0) goto L6a
            r10 = r11
        L6a:
            if (r10 == 0) goto L6d
            goto L70
        L6d:
            int r5 = r5 + 1
            goto L38
        L70:
            r5 = r10
            if (r5 != 0) goto L74
            return r4
        L74:
            int r2 = r2 + 1
            goto L1c
        L77:
            int r2 = r5.mCode
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.UriMatcher.match(android.net.Uri):int");
    }
}
