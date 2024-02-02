package android.view.animation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes2.dex */
public class AnimationUtils {
    private static final int SEQUENTIALLY = 1;
    private static final int TOGETHER = 0;
    private static ThreadLocal<AnimationState> sAnimationState = new ThreadLocal<AnimationState>() { // from class: android.view.animation.AnimationUtils.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public AnimationState initialValue() {
            return new AnimationState();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class AnimationState {
        boolean animationClockLocked;
        long currentVsyncTimeMillis;
        long lastReportedTimeMillis;

        private synchronized AnimationState() {
        }
    }

    public static void lockAnimationClock(long vsyncMillis) {
        AnimationState state = sAnimationState.get();
        state.animationClockLocked = true;
        state.currentVsyncTimeMillis = vsyncMillis;
    }

    public static void unlockAnimationClock() {
        sAnimationState.get().animationClockLocked = false;
    }

    public static long currentAnimationTimeMillis() {
        AnimationState state = sAnimationState.get();
        if (state.animationClockLocked) {
            return Math.max(state.currentVsyncTimeMillis, state.lastReportedTimeMillis);
        }
        state.lastReportedTimeMillis = SystemClock.uptimeMillis();
        return state.lastReportedTimeMillis;
    }

    public static Animation loadAnimation(Context context, int id) throws Resources.NotFoundException {
        XmlResourceParser parser = null;
        try {
            try {
                try {
                    parser = context.getResources().getAnimation(id);
                    return createAnimationFromXml(context, parser);
                } catch (IOException ex) {
                    Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
                    rnf.initCause(ex);
                    throw rnf;
                }
            } catch (XmlPullParserException ex2) {
                Resources.NotFoundException rnf2 = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
                rnf2.initCause(ex2);
                throw rnf2;
            }
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static synchronized Animation createAnimationFromXml(Context c, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createAnimationFromXml(c, parser, null, Xml.asAttributeSet(parser));
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00a3, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public protected static android.view.animation.Animation createAnimationFromXml(android.content.Context r7, org.xmlpull.v1.XmlPullParser r8, android.view.animation.AnimationSet r9, android.util.AttributeSet r10) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r0 = 0
            int r1 = r8.getDepth()
        L5:
            int r2 = r8.next()
            r3 = r2
            r4 = 3
            if (r2 != r4) goto L13
            int r2 = r8.getDepth()
            if (r2 <= r1) goto La3
        L13:
            r2 = 1
            if (r3 == r2) goto La3
            r2 = 2
            if (r3 == r2) goto L1a
            goto L5
        L1a:
            java.lang.String r2 = r8.getName()
            java.lang.String r4 = "set"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L34
            android.view.animation.AnimationSet r4 = new android.view.animation.AnimationSet
            r4.<init>(r7, r10)
            r0 = r4
            r4 = r0
            android.view.animation.AnimationSet r4 = (android.view.animation.AnimationSet) r4
            createAnimationFromXml(r7, r8, r4, r10)
            goto L81
        L34:
            java.lang.String r4 = "alpha"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L43
            android.view.animation.AlphaAnimation r4 = new android.view.animation.AlphaAnimation
            r4.<init>(r7, r10)
            r0 = r4
            goto L81
        L43:
            java.lang.String r4 = "scale"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L53
            android.view.animation.ScaleAnimation r4 = new android.view.animation.ScaleAnimation
            r4.<init>(r7, r10)
            r0 = r4
            goto L81
        L53:
            java.lang.String r4 = "rotate"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L63
            android.view.animation.RotateAnimation r4 = new android.view.animation.RotateAnimation
            r4.<init>(r7, r10)
            r0 = r4
            goto L81
        L63:
            java.lang.String r4 = "translate"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L73
            android.view.animation.TranslateAnimation r4 = new android.view.animation.TranslateAnimation
            r4.<init>(r7, r10)
            r0 = r4
            goto L81
        L73:
            java.lang.String r4 = "cliprect"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L88
            android.view.animation.ClipRectAnimation r4 = new android.view.animation.ClipRectAnimation
            r4.<init>(r7, r10)
            r0 = r4
        L81:
            if (r9 == 0) goto L86
            r9.addAnimation(r0)
        L86:
            goto L5
        L88:
            java.lang.RuntimeException r4 = new java.lang.RuntimeException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Unknown animation name: "
            r5.append(r6)
            java.lang.String r6 = r8.getName()
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            throw r4
        La3:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.animation.AnimationUtils.createAnimationFromXml(android.content.Context, org.xmlpull.v1.XmlPullParser, android.view.animation.AnimationSet, android.util.AttributeSet):android.view.animation.Animation");
    }

    public static LayoutAnimationController loadLayoutAnimation(Context context, int id) throws Resources.NotFoundException {
        XmlResourceParser parser = null;
        try {
            try {
                try {
                    parser = context.getResources().getAnimation(id);
                    return createLayoutAnimationFromXml(context, parser);
                } catch (IOException ex) {
                    Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
                    rnf.initCause(ex);
                    throw rnf;
                }
            } catch (XmlPullParserException ex2) {
                Resources.NotFoundException rnf2 = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
                rnf2.initCause(ex2);
                throw rnf2;
            }
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static synchronized LayoutAnimationController createLayoutAnimationFromXml(Context c, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createLayoutAnimationFromXml(c, parser, Xml.asAttributeSet(parser));
    }

    private static synchronized LayoutAnimationController createLayoutAnimationFromXml(Context c, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        LayoutAnimationController controller = null;
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if ((type != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    String name = parser.getName();
                    if ("layoutAnimation".equals(name)) {
                        controller = new LayoutAnimationController(c, attrs);
                    } else if ("gridLayoutAnimation".equals(name)) {
                        controller = new GridLayoutAnimationController(c, attrs);
                    } else {
                        throw new RuntimeException("Unknown layout animation name: " + name);
                    }
                }
            }
        }
        return controller;
    }

    public static Animation makeInAnimation(Context c, boolean fromLeft) {
        Animation a;
        if (fromLeft) {
            a = loadAnimation(c, 17432578);
        } else {
            a = loadAnimation(c, R.anim.slide_in_right);
        }
        a.setInterpolator(new DecelerateInterpolator());
        a.setStartTime(currentAnimationTimeMillis());
        return a;
    }

    public static Animation makeOutAnimation(Context c, boolean toRight) {
        Animation a;
        if (toRight) {
            a = loadAnimation(c, 17432579);
        } else {
            a = loadAnimation(c, R.anim.slide_out_left);
        }
        a.setInterpolator(new AccelerateInterpolator());
        a.setStartTime(currentAnimationTimeMillis());
        return a;
    }

    public static Animation makeInChildBottomAnimation(Context c) {
        Animation a = loadAnimation(c, R.anim.slide_in_child_bottom);
        a.setInterpolator(new AccelerateInterpolator());
        a.setStartTime(currentAnimationTimeMillis());
        return a;
    }

    public static Interpolator loadInterpolator(Context context, int id) throws Resources.NotFoundException {
        XmlResourceParser parser = null;
        try {
            try {
                parser = context.getResources().getAnimation(id);
                return createInterpolatorFromXml(context.getResources(), context.getTheme(), parser);
            } catch (IOException ex) {
                Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
                rnf.initCause(ex);
                throw rnf;
            } catch (XmlPullParserException ex2) {
                Resources.NotFoundException rnf2 = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
                rnf2.initCause(ex2);
                throw rnf2;
            }
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    public static synchronized Interpolator loadInterpolator(Resources res, Resources.Theme theme, int id) throws Resources.NotFoundException {
        XmlResourceParser parser = null;
        try {
            try {
                try {
                    parser = res.getAnimation(id);
                    return createInterpolatorFromXml(res, theme, parser);
                } catch (XmlPullParserException ex) {
                    Resources.NotFoundException rnf = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
                    rnf.initCause(ex);
                    throw rnf;
                }
            } catch (IOException ex2) {
                Resources.NotFoundException rnf2 = new Resources.NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
                rnf2.initCause(ex2);
                throw rnf2;
            }
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x00d9, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static synchronized android.view.animation.Interpolator createInterpolatorFromXml(android.content.res.Resources r8, android.content.res.Resources.Theme r9, org.xmlpull.v1.XmlPullParser r10) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r0 = 0
            int r1 = r10.getDepth()
        L5:
            int r2 = r10.next()
            r3 = r2
            r4 = 3
            if (r2 != r4) goto L13
            int r2 = r10.getDepth()
            if (r2 <= r1) goto Ld9
        L13:
            r2 = 1
            if (r3 == r2) goto Ld9
            r2 = 2
            if (r3 == r2) goto L1a
            goto L5
        L1a:
            android.util.AttributeSet r2 = android.util.Xml.asAttributeSet(r10)
            java.lang.String r4 = r10.getName()
            java.lang.String r5 = "linearInterpolator"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L32
            android.view.animation.LinearInterpolator r5 = new android.view.animation.LinearInterpolator
            r5.<init>()
            r0 = r5
            goto Lbc
        L32:
            java.lang.String r5 = "accelerateInterpolator"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L42
            android.view.animation.AccelerateInterpolator r5 = new android.view.animation.AccelerateInterpolator
            r5.<init>(r8, r9, r2)
            r0 = r5
            goto Lbc
        L42:
            java.lang.String r5 = "decelerateInterpolator"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L52
            android.view.animation.DecelerateInterpolator r5 = new android.view.animation.DecelerateInterpolator
            r5.<init>(r8, r9, r2)
            r0 = r5
            goto Lbc
        L52:
            java.lang.String r5 = "accelerateDecelerateInterpolator"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L61
            android.view.animation.AccelerateDecelerateInterpolator r5 = new android.view.animation.AccelerateDecelerateInterpolator
            r5.<init>()
            r0 = r5
            goto Lbc
        L61:
            java.lang.String r5 = "cycleInterpolator"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L70
            android.view.animation.CycleInterpolator r5 = new android.view.animation.CycleInterpolator
            r5.<init>(r8, r9, r2)
            r0 = r5
            goto Lbc
        L70:
            java.lang.String r5 = "anticipateInterpolator"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L7f
            android.view.animation.AnticipateInterpolator r5 = new android.view.animation.AnticipateInterpolator
            r5.<init>(r8, r9, r2)
            r0 = r5
            goto Lbc
        L7f:
            java.lang.String r5 = "overshootInterpolator"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L8f
            android.view.animation.OvershootInterpolator r5 = new android.view.animation.OvershootInterpolator
            r5.<init>(r8, r9, r2)
            r0 = r5
            goto Lbc
        L8f:
            java.lang.String r5 = "anticipateOvershootInterpolator"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L9e
            android.view.animation.AnticipateOvershootInterpolator r5 = new android.view.animation.AnticipateOvershootInterpolator
            r5.<init>(r8, r9, r2)
            r0 = r5
            goto Lbc
        L9e:
            java.lang.String r5 = "bounceInterpolator"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto Lad
            android.view.animation.BounceInterpolator r5 = new android.view.animation.BounceInterpolator
            r5.<init>()
            r0 = r5
            goto Lbc
        Lad:
            java.lang.String r5 = "pathInterpolator"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto Lbe
            android.view.animation.PathInterpolator r5 = new android.view.animation.PathInterpolator
            r5.<init>(r8, r9, r2)
            r0 = r5
        Lbc:
            goto L5
        Lbe:
            java.lang.RuntimeException r5 = new java.lang.RuntimeException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Unknown interpolator name: "
            r6.append(r7)
            java.lang.String r7 = r10.getName()
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            r5.<init>(r6)
            throw r5
        Ld9:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.animation.AnimationUtils.createInterpolatorFromXml(android.content.res.Resources, android.content.res.Resources$Theme, org.xmlpull.v1.XmlPullParser):android.view.animation.Interpolator");
    }
}
