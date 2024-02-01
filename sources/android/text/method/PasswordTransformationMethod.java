package android.text.method;

import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.GetChars;
import android.text.NoCopySpan;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UpdateLayout;
import android.view.View;
import java.lang.ref.WeakReference;
/* loaded from: classes2.dex */
public class PasswordTransformationMethod implements TransformationMethod, TextWatcher {
    public protected static char DOT = 8226;
    public protected static PasswordTransformationMethod sInstance;

    @Override // android.text.method.TransformationMethod
    public CharSequence getTransformation(CharSequence source, View view) {
        if (source instanceof Spannable) {
            Spannable sp = (Spannable) source;
            ViewReference[] vr = (ViewReference[]) sp.getSpans(0, sp.length(), ViewReference.class);
            for (ViewReference viewReference : vr) {
                sp.removeSpan(viewReference);
            }
            removeVisibleSpans(sp);
            sp.setSpan(new ViewReference(view), 0, 0, 34);
        }
        return new PasswordCharSequence(source);
    }

    public static PasswordTransformationMethod getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = new PasswordTransformationMethod();
        return sInstance;
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s instanceof Spannable) {
            Spannable sp = (Spannable) s;
            ViewReference[] vr = (ViewReference[]) sp.getSpans(0, s.length(), ViewReference.class);
            if (vr.length == 0) {
                return;
            }
            View v = null;
            for (int i = 0; v == null && i < vr.length; i++) {
                v = (View) vr[i].get();
            }
            if (v == null) {
                return;
            }
            int pref = TextKeyListener.getInstance().getPrefs(v.getContext());
            if ((pref & 8) != 0 && count > 0) {
                removeVisibleSpans(sp);
                if (count == 1) {
                    sp.setSpan(new Visible(sp, this), start, start + count, 33);
                }
            }
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
    }

    @Override // android.text.method.TransformationMethod
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {
        if (!focused && (sourceText instanceof Spannable)) {
            Spannable sp = (Spannable) sourceText;
            removeVisibleSpans(sp);
        }
    }

    private static synchronized void removeVisibleSpans(Spannable sp) {
        int i = 0;
        Visible[] old = (Visible[]) sp.getSpans(0, sp.length(), Visible.class);
        while (true) {
            int i2 = i;
            int i3 = old.length;
            if (i2 < i3) {
                sp.removeSpan(old[i2]);
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    /* loaded from: classes2.dex */
    private static class PasswordCharSequence implements CharSequence, GetChars {
        private CharSequence mSource;

        public synchronized PasswordCharSequence(CharSequence source) {
            this.mSource = source;
        }

        @Override // java.lang.CharSequence
        public int length() {
            return this.mSource.length();
        }

        @Override // java.lang.CharSequence
        public char charAt(int i) {
            if (this.mSource instanceof Spanned) {
                Spanned sp = (Spanned) this.mSource;
                int st = sp.getSpanStart(TextKeyListener.ACTIVE);
                int en = sp.getSpanEnd(TextKeyListener.ACTIVE);
                if (i >= st && i < en) {
                    return this.mSource.charAt(i);
                }
                int a = 0;
                Visible[] visible = (Visible[]) sp.getSpans(0, sp.length(), Visible.class);
                while (true) {
                    int a2 = a;
                    int a3 = visible.length;
                    if (a2 >= a3) {
                        break;
                    }
                    if (sp.getSpanStart(visible[a2].mTransformer) >= 0) {
                        int st2 = sp.getSpanStart(visible[a2]);
                        int en2 = sp.getSpanEnd(visible[a2]);
                        if (i >= st2 && i < en2) {
                            return this.mSource.charAt(i);
                        }
                    }
                    a = a2 + 1;
                }
            }
            return PasswordTransformationMethod.DOT;
        }

        @Override // java.lang.CharSequence
        public CharSequence subSequence(int start, int end) {
            char[] buf = new char[end - start];
            getChars(start, end, buf, 0);
            return new String(buf);
        }

        @Override // java.lang.CharSequence
        public String toString() {
            return subSequence(0, length()).toString();
        }

        @Override // android.text.GetChars
        public void getChars(int start, int end, char[] dest, int off) {
            TextUtils.getChars(this.mSource, start, end, dest, off);
            int st = -1;
            int en = -1;
            int nvisible = 0;
            int[] starts = null;
            int[] ends = null;
            if (this.mSource instanceof Spanned) {
                Spanned sp = (Spanned) this.mSource;
                st = sp.getSpanStart(TextKeyListener.ACTIVE);
                en = sp.getSpanEnd(TextKeyListener.ACTIVE);
                Visible[] visible = (Visible[]) sp.getSpans(0, sp.length(), Visible.class);
                nvisible = visible.length;
                starts = new int[nvisible];
                ends = new int[nvisible];
                for (int i = 0; i < nvisible; i++) {
                    if (sp.getSpanStart(visible[i].mTransformer) >= 0) {
                        starts[i] = sp.getSpanStart(visible[i]);
                        ends[i] = sp.getSpanEnd(visible[i]);
                    }
                }
            }
            for (int i2 = start; i2 < end; i2++) {
                if (i2 < st || i2 >= en) {
                    boolean visible2 = false;
                    int a = 0;
                    while (true) {
                        if (a >= nvisible) {
                            break;
                        } else if (i2 < starts[a] || i2 >= ends[a]) {
                            a++;
                        } else {
                            visible2 = true;
                            break;
                        }
                    }
                    if (!visible2) {
                        dest[(i2 - start) + off] = PasswordTransformationMethod.DOT;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class Visible extends Handler implements UpdateLayout, Runnable {
        private Spannable mText;
        private PasswordTransformationMethod mTransformer;

        public synchronized Visible(Spannable sp, PasswordTransformationMethod ptm) {
            this.mText = sp;
            this.mTransformer = ptm;
            postAtTime(this, SystemClock.uptimeMillis() + 1500);
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mText.removeSpan(this);
        }
    }

    /* loaded from: classes2.dex */
    private static class ViewReference extends WeakReference<View> implements NoCopySpan {
        public synchronized ViewReference(View v) {
            super(v);
        }
    }
}
