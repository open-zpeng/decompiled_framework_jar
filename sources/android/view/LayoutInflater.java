package android.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;
import com.android.internal.R;
import com.xiaopeng.util.DebugOption;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes2.dex */
public abstract class LayoutInflater {
    private static final String ATTR_LAYOUT = "layout";
    private static final String TAG_1995 = "blink";
    private static final String TAG_INCLUDE = "include";
    private static final String TAG_MERGE = "merge";
    private static final String TAG_REQUEST_FOCUS = "requestFocus";
    private static final String TAG_TAG = "tag";
    public private protected final Object[] mConstructorArgs;
    public private final Context mContext;
    public protected Factory mFactory;
    public protected Factory2 mFactory2;
    public protected boolean mFactorySet;
    private Filter mFilter;
    private HashMap<String, Boolean> mFilterMap;
    public protected Factory2 mPrivateFactory;
    private TypedValue mTempValue;
    private static final String TAG = LayoutInflater.class.getSimpleName();
    private static final boolean DEBUG = DebugOption.DEBUG_INFLATER;
    private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
    public private protected static final Class<?>[] mConstructorSignature = {Context.class, AttributeSet.class};
    public protected static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();
    public protected static final int[] ATTRS_THEME = {16842752};
    private static final ClassLoader BOOT_CLASS_LOADER = LayoutInflater.class.getClassLoader();

    /* loaded from: classes2.dex */
    public interface Factory {
        View onCreateView(String str, Context context, AttributeSet attributeSet);
    }

    /* loaded from: classes2.dex */
    public interface Factory2 extends Factory {
        View onCreateView(View view, String str, Context context, AttributeSet attributeSet);
    }

    /* loaded from: classes2.dex */
    public interface Filter {
        boolean onLoadClass(Class cls);
    }

    public abstract LayoutInflater cloneInContext(Context context);

    /* loaded from: classes2.dex */
    private static class FactoryMerger implements Factory2 {
        private final Factory mF1;
        private final Factory2 mF12;
        private final Factory mF2;
        private final Factory2 mF22;

        synchronized FactoryMerger(Factory f1, Factory2 f12, Factory f2, Factory2 f22) {
            this.mF1 = f1;
            this.mF2 = f2;
            this.mF12 = f12;
            this.mF22 = f22;
        }

        @Override // android.view.LayoutInflater.Factory
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            View v = this.mF1.onCreateView(name, context, attrs);
            return v != null ? v : this.mF2.onCreateView(name, context, attrs);
        }

        @Override // android.view.LayoutInflater.Factory2
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            View v = this.mF12 != null ? this.mF12.onCreateView(parent, name, context, attrs) : this.mF1.onCreateView(name, context, attrs);
            return v != null ? v : this.mF22 != null ? this.mF22.onCreateView(parent, name, context, attrs) : this.mF2.onCreateView(name, context, attrs);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LayoutInflater(Context context) {
        this.mConstructorArgs = new Object[2];
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LayoutInflater(LayoutInflater original, Context newContext) {
        this.mConstructorArgs = new Object[2];
        this.mContext = newContext;
        this.mFactory = original.mFactory;
        this.mFactory2 = original.mFactory2;
        this.mPrivateFactory = original.mPrivateFactory;
        setFilter(original.mFilter);
    }

    public static LayoutInflater from(Context context) {
        LayoutInflater LayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (LayoutInflater == null) {
            throw new AssertionError("LayoutInflater not found.");
        }
        return LayoutInflater;
    }

    public Context getContext() {
        return this.mContext;
    }

    public final Factory getFactory() {
        return this.mFactory;
    }

    public final Factory2 getFactory2() {
        return this.mFactory2;
    }

    public void setFactory(Factory factory) {
        if (this.mFactorySet) {
            throw new IllegalStateException("A factory has already been set on this LayoutInflater");
        }
        if (factory == null) {
            throw new NullPointerException("Given factory can not be null");
        }
        this.mFactorySet = true;
        if (this.mFactory == null) {
            this.mFactory = factory;
        } else {
            this.mFactory = new FactoryMerger(factory, null, this.mFactory, this.mFactory2);
        }
    }

    public void setFactory2(Factory2 factory) {
        if (this.mFactorySet) {
            throw new IllegalStateException("A factory has already been set on this LayoutInflater");
        }
        if (factory == null) {
            throw new NullPointerException("Given factory can not be null");
        }
        this.mFactorySet = true;
        if (this.mFactory == null) {
            this.mFactory2 = factory;
            this.mFactory = factory;
            return;
        }
        FactoryMerger factoryMerger = new FactoryMerger(factory, factory, this.mFactory, this.mFactory2);
        this.mFactory2 = factoryMerger;
        this.mFactory = factoryMerger;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPrivateFactory(Factory2 factory) {
        if (this.mPrivateFactory == null) {
            this.mPrivateFactory = factory;
        } else {
            this.mPrivateFactory = new FactoryMerger(factory, factory, this.mPrivateFactory, this.mPrivateFactory);
        }
    }

    public Filter getFilter() {
        return this.mFilter;
    }

    public void setFilter(Filter filter) {
        this.mFilter = filter;
        if (filter != null) {
            this.mFilterMap = new HashMap<>();
        }
    }

    public View inflate(int resource, ViewGroup root) {
        return inflate(resource, root, root != null);
    }

    public View inflate(XmlPullParser parser, ViewGroup root) {
        return inflate(parser, root, root != null);
    }

    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        Resources res = getContext().getResources();
        if (DEBUG) {
            String str = TAG;
            Log.d(str, "INFLATING from resource: \"" + res.getResourceName(resource) + "\" (" + Integer.toHexString(resource) + ")");
        }
        XmlResourceParser parser = res.getLayout(resource);
        try {
            return inflate(parser, root, attachToRoot);
        } finally {
            parser.close();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0035, code lost:
        if (r0 != 2) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0037, code lost:
        r0 = r22.getName();
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x003d, code lost:
        if (android.view.LayoutInflater.DEBUG == false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x003f, code lost:
        java.lang.System.out.println("**************************");
        java.lang.System.out.println("Creating root view: " + r0);
        java.lang.System.out.println("**************************");
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0069, code lost:
        if (android.view.LayoutInflater.TAG_MERGE.equals(r0) == false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x006b, code lost:
        if (r23 == null) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x006d, code lost:
        if (r24 == false) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006f, code lost:
        r10 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x007c, code lost:
        rInflate(r22, r23, r0, r0, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x008c, code lost:
        throw new android.view.InflateException("<merge /> can be used only with a valid ViewGroup root and attachToRoot=true");
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x008d, code lost:
        r10 = 1;
        r1 = createViewFromTag(r23, r0, r0, r0);
        r2 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0095, code lost:
        if (r23 == null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0099, code lost:
        if (android.view.LayoutInflater.DEBUG == false) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x009b, code lost:
        java.lang.System.out.println("Creating params from root: " + r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00b1, code lost:
        r2 = r23.generateLayoutParams(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00b6, code lost:
        if (r24 != false) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00b8, code lost:
        r1.setLayoutParams(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00bd, code lost:
        if (android.view.LayoutInflater.DEBUG == false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00bf, code lost:
        java.lang.System.out.println("-----> start inflating children");
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00c8, code lost:
        rInflateChildren(r22, r1, r0, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00cd, code lost:
        if (android.view.LayoutInflater.DEBUG == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00cf, code lost:
        java.lang.System.out.println("-----> done inflating children");
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00d6, code lost:
        if (r23 == null) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00d8, code lost:
        if (r24 == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00da, code lost:
        r23.addView(r1, r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00dd, code lost:
        if (r23 == null) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00df, code lost:
        if (r24 != false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00e1, code lost:
        r16 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00e3, code lost:
        r21.mConstructorArgs[0] = r0;
        r21.mConstructorArgs[r10] = null;
        android.os.Trace.traceEnd(8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00f2, code lost:
        return r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f3, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00f8, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00fc, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x011f, code lost:
        throw new android.view.InflateException(r22.getPositionDescription() + ": No start tag found!");
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x012d, code lost:
        r1 = new android.view.InflateException(r22.getPositionDescription() + ": " + r0.getMessage(), r0);
        r1.setStackTrace(android.view.LayoutInflater.EMPTY_STACK_TRACE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0153, code lost:
        throw r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0158, code lost:
        r1 = new android.view.InflateException(r0.getMessage(), r0);
        r1.setStackTrace(android.view.LayoutInflater.EMPTY_STACK_TRACE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0166, code lost:
        throw r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0168, code lost:
        r21.mConstructorArgs[0] = r0;
        r21.mConstructorArgs[r10] = null;
        android.os.Trace.traceEnd(8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0175, code lost:
        throw r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0 */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v10 */
    /* JADX WARN: Type inference failed for: r10v4 */
    /* JADX WARN: Type inference failed for: r10v6 */
    /* JADX WARN: Type inference failed for: r10v7 */
    /* JADX WARN: Type inference failed for: r10v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.view.View inflate(org.xmlpull.v1.XmlPullParser r22, android.view.ViewGroup r23, boolean r24) {
        /*
            Method dump skipped, instructions count: 381
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.LayoutInflater.inflate(org.xmlpull.v1.XmlPullParser, android.view.ViewGroup, boolean):android.view.View");
    }

    private final synchronized boolean verifyClassLoader(Constructor<? extends View> constructor) {
        ClassLoader constructorLoader = constructor.getDeclaringClass().getClassLoader();
        if (constructorLoader == BOOT_CLASS_LOADER) {
            return true;
        }
        ClassLoader cl = this.mContext.getClassLoader();
        while (constructorLoader != cl) {
            cl = cl.getParent();
            if (cl == null) {
                return false;
            }
        }
        return true;
    }

    public final View createView(String name, String prefix, AttributeSet attrs) throws ClassNotFoundException, InflateException {
        String str;
        String str2;
        String str3;
        String str4;
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if (constructor != null && !verifyClassLoader(constructor)) {
            constructor = null;
            sConstructorMap.remove(name);
        }
        Class cls = null;
        try {
            try {
                try {
                    try {
                        Trace.traceBegin(8L, name);
                        if (constructor == null) {
                            ClassLoader classLoader = this.mContext.getClassLoader();
                            if (prefix != null) {
                                str4 = prefix + name;
                            } else {
                                str4 = name;
                            }
                            cls = classLoader.loadClass(str4).asSubclass(View.class);
                            if (this.mFilter != null && cls != null) {
                                if (!this.mFilter.onLoadClass(cls)) {
                                    failNotAllowed(name, prefix, attrs);
                                }
                            }
                            constructor = cls.getConstructor(mConstructorSignature);
                            constructor.setAccessible(true);
                            sConstructorMap.put(name, constructor);
                        } else if (this.mFilter != null) {
                            Boolean allowedState = this.mFilterMap.get(name);
                            if (allowedState == null) {
                                ClassLoader classLoader2 = this.mContext.getClassLoader();
                                if (prefix != null) {
                                    str3 = prefix + name;
                                } else {
                                    str3 = name;
                                }
                                cls = classLoader2.loadClass(str3).asSubclass(View.class);
                                boolean allowed = cls != null && this.mFilter.onLoadClass(cls);
                                this.mFilterMap.put(name, Boolean.valueOf(allowed));
                                if (!allowed) {
                                    failNotAllowed(name, prefix, attrs);
                                }
                            } else if (allowedState.equals(Boolean.FALSE)) {
                                failNotAllowed(name, prefix, attrs);
                            }
                        }
                        Object lastContext = this.mConstructorArgs[0];
                        if (this.mConstructorArgs[0] == null) {
                            this.mConstructorArgs[0] = this.mContext;
                        }
                        Object[] args = this.mConstructorArgs;
                        args[1] = attrs;
                        View view = constructor.newInstance(args);
                        if (view instanceof ViewStub) {
                            ViewStub viewStub = (ViewStub) view;
                            viewStub.setLayoutInflater(cloneInContext((Context) args[0]));
                        }
                        this.mConstructorArgs[0] = lastContext;
                        return view;
                    } catch (ClassNotFoundException e) {
                        throw e;
                    }
                } catch (Exception e2) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(attrs.getPositionDescription());
                    sb.append(": Error inflating class ");
                    sb.append(cls == null ? MediaStore.UNKNOWN_STRING : cls.getName());
                    InflateException ie = new InflateException(sb.toString(), e2);
                    ie.setStackTrace(EMPTY_STACK_TRACE);
                    throw ie;
                }
            } catch (ClassCastException e3) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(attrs.getPositionDescription());
                sb2.append(": Class is not a View ");
                if (prefix != null) {
                    str2 = prefix + name;
                } else {
                    str2 = name;
                }
                sb2.append(str2);
                InflateException ie2 = new InflateException(sb2.toString(), e3);
                ie2.setStackTrace(EMPTY_STACK_TRACE);
                throw ie2;
            } catch (NoSuchMethodException e4) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(attrs.getPositionDescription());
                sb3.append(": Error inflating class ");
                if (prefix != null) {
                    str = prefix + name;
                } else {
                    str = name;
                }
                sb3.append(str);
                InflateException ie3 = new InflateException(sb3.toString(), e4);
                ie3.setStackTrace(EMPTY_STACK_TRACE);
                throw ie3;
            }
        } finally {
            Trace.traceEnd(8L);
        }
    }

    private synchronized void failNotAllowed(String name, String prefix, AttributeSet attrs) {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append(attrs.getPositionDescription());
        sb.append(": Class not allowed to be inflated ");
        if (prefix != null) {
            str = prefix + name;
        } else {
            str = name;
        }
        sb.append(str);
        throw new InflateException(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        return createView(name, "android.view.", attrs);
    }

    protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        return onCreateView(name, attrs);
    }

    public protected View createViewFromTag(View parent, String name, Context context, AttributeSet attrs) {
        return createViewFromTag(parent, name, context, attrs, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x005c A[Catch: Exception -> 0x0084, ClassNotFoundException -> 0x00a8, InflateException -> 0x00cc, TRY_LEAVE, TryCatch #3 {InflateException -> 0x00cc, ClassNotFoundException -> 0x00a8, Exception -> 0x0084, blocks: (B:15:0x0036, B:17:0x003a, B:24:0x004f, B:26:0x0053, B:28:0x005c, B:36:0x0078, B:19:0x0041, B:21:0x0045, B:30:0x0067, B:32:0x006d, B:34:0x0073), top: B:49:0x0036 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public private protected android.view.View createViewFromTag(android.view.View r7, java.lang.String r8, android.content.Context r9, android.util.AttributeSet r10, boolean r11) {
        /*
            r6 = this;
            java.lang.String r0 = "view"
            boolean r0 = r8.equals(r0)
            r1 = 0
            if (r0 == 0) goto L10
            java.lang.String r0 = "class"
            java.lang.String r8 = r10.getAttributeValue(r1, r0)
        L10:
            r0 = 0
            if (r11 != 0) goto L28
            int[] r2 = android.view.LayoutInflater.ATTRS_THEME
            android.content.res.TypedArray r2 = r9.obtainStyledAttributes(r10, r2)
            int r3 = r2.getResourceId(r0, r0)
            if (r3 == 0) goto L25
            android.view.ContextThemeWrapper r4 = new android.view.ContextThemeWrapper
            r4.<init>(r9, r3)
            r9 = r4
        L25:
            r2.recycle()
        L28:
            java.lang.String r2 = "blink"
            boolean r2 = r8.equals(r2)
            if (r2 == 0) goto L36
            android.view.LayoutInflater$BlinkLayout r0 = new android.view.LayoutInflater$BlinkLayout
            r0.<init>(r9, r10)
            return r0
        L36:
            android.view.LayoutInflater$Factory2 r2 = r6.mFactory2     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            if (r2 == 0) goto L41
            android.view.LayoutInflater$Factory2 r2 = r6.mFactory2     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            android.view.View r2 = r2.onCreateView(r7, r8, r9, r10)     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
        L40:
            goto L4d
        L41:
            android.view.LayoutInflater$Factory r2 = r6.mFactory     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            if (r2 == 0) goto L4c
            android.view.LayoutInflater$Factory r2 = r6.mFactory     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            android.view.View r2 = r2.onCreateView(r8, r9, r10)     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            goto L40
        L4c:
            r2 = r1
        L4d:
            if (r2 != 0) goto L5a
            android.view.LayoutInflater$Factory2 r3 = r6.mPrivateFactory     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            if (r3 == 0) goto L5a
            android.view.LayoutInflater$Factory2 r3 = r6.mPrivateFactory     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            android.view.View r3 = r3.onCreateView(r7, r8, r9, r10)     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            r2 = r3
        L5a:
            if (r2 != 0) goto L83
            java.lang.Object[] r3 = r6.mConstructorArgs     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            r3 = r3[r0]     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            java.lang.Object[] r4 = r6.mConstructorArgs     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            r4[r0] = r9     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            r4 = -1
            r5 = 46
            int r5 = r8.indexOf(r5)     // Catch: java.lang.Throwable -> L7d
            if (r4 != r5) goto L73
            android.view.View r1 = r6.onCreateView(r7, r8, r10)     // Catch: java.lang.Throwable -> L7d
        L71:
            r2 = r1
            goto L78
        L73:
            android.view.View r1 = r6.createView(r8, r1, r10)     // Catch: java.lang.Throwable -> L7d
            goto L71
        L78:
            java.lang.Object[] r1 = r6.mConstructorArgs     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            r1[r0] = r3     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            goto L83
        L7d:
            r1 = move-exception
            java.lang.Object[] r4 = r6.mConstructorArgs     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            r4[r0] = r3     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
            throw r1     // Catch: java.lang.Exception -> L84 java.lang.ClassNotFoundException -> La8 android.view.InflateException -> Lcc
        L83:
            return r2
        L84:
            r0 = move-exception
            android.view.InflateException r1 = new android.view.InflateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = r10.getPositionDescription()
            r2.append(r3)
            java.lang.String r3 = ": Error inflating class "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2, r0)
            java.lang.StackTraceElement[] r2 = android.view.LayoutInflater.EMPTY_STACK_TRACE
            r1.setStackTrace(r2)
            throw r1
        La8:
            r0 = move-exception
            android.view.InflateException r1 = new android.view.InflateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = r10.getPositionDescription()
            r2.append(r3)
            java.lang.String r3 = ": Error inflating class "
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2, r0)
            java.lang.StackTraceElement[] r2 = android.view.LayoutInflater.EMPTY_STACK_TRACE
            r1.setStackTrace(r2)
            throw r1
        Lcc:
            r0 = move-exception
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.LayoutInflater.createViewFromTag(android.view.View, java.lang.String, android.content.Context, android.util.AttributeSet, boolean):android.view.View");
    }

    final synchronized void rInflateChildren(XmlPullParser parser, View parent, AttributeSet attrs, boolean finishInflate) throws XmlPullParserException, IOException {
        rInflate(parser, parent, parent.getContext(), attrs, finishInflate);
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0075, code lost:
        if (r1 == false) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0077, code lost:
        r10.restoreDefaultFocus();
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x007a, code lost:
        if (r13 == false) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x007c, code lost:
        r10.onFinishInflate();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x007f, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    synchronized void rInflate(org.xmlpull.v1.XmlPullParser r9, android.view.View r10, android.content.Context r11, android.util.AttributeSet r12, boolean r13) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r8 = this;
            int r0 = r9.getDepth()
            r1 = 0
        L5:
            int r2 = r9.next()
            r3 = r2
            r4 = 3
            if (r2 != r4) goto L13
            int r2 = r9.getDepth()
            if (r2 <= r0) goto L75
        L13:
            r2 = 1
            if (r3 == r2) goto L75
            r4 = 2
            if (r3 == r4) goto L1a
            goto L5
        L1a:
            java.lang.String r4 = r9.getName()
            java.lang.String r5 = "requestFocus"
            boolean r5 = r5.equals(r4)
            if (r5 == 0) goto L2c
            r1 = 1
            consumeChildElements(r9)
            goto L6c
        L2c:
            java.lang.String r5 = "tag"
            boolean r5 = r5.equals(r4)
            if (r5 == 0) goto L39
            r8.parseViewTag(r9, r10, r12)
            goto L6c
        L39:
            java.lang.String r5 = "include"
            boolean r5 = r5.equals(r4)
            if (r5 == 0) goto L53
            int r2 = r9.getDepth()
            if (r2 == 0) goto L4b
            r8.parseInclude(r9, r11, r10, r12)
            goto L6c
        L4b:
            android.view.InflateException r2 = new android.view.InflateException
            java.lang.String r5 = "<include /> cannot be the root element"
            r2.<init>(r5)
            throw r2
        L53:
            java.lang.String r5 = "merge"
            boolean r5 = r5.equals(r4)
            if (r5 != 0) goto L6d
            android.view.View r5 = r8.createViewFromTag(r10, r4, r11, r12)
            r6 = r10
            android.view.ViewGroup r6 = (android.view.ViewGroup) r6
            android.view.ViewGroup$LayoutParams r7 = r6.generateLayoutParams(r12)
            r8.rInflateChildren(r9, r5, r12, r2)
            r6.addView(r5, r7)
        L6c:
            goto L5
        L6d:
            android.view.InflateException r2 = new android.view.InflateException
            java.lang.String r5 = "<merge /> must be the root element"
            r2.<init>(r5)
            throw r2
        L75:
            if (r1 == 0) goto L7a
            r10.restoreDefaultFocus()
        L7a:
            if (r13 == 0) goto L7f
            r10.onFinishInflate()
        L7f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.LayoutInflater.rInflate(org.xmlpull.v1.XmlPullParser, android.view.View, android.content.Context, android.util.AttributeSet, boolean):void");
    }

    private synchronized void parseViewTag(XmlPullParser parser, View view, AttributeSet attrs) throws XmlPullParserException, IOException {
        Context context = view.getContext();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewTag);
        int key = ta.getResourceId(1, 0);
        CharSequence value = ta.getText(0);
        view.setTag(key, value);
        ta.recycle();
        consumeChildElements(parser);
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0096, code lost:
        if (r0 != 2) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0098, code lost:
        r0 = r4.getName();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00a3, code lost:
        if (android.view.LayoutInflater.TAG_MERGE.equals(r0) == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00a5, code lost:
        r17 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00b3, code lost:
        rInflate(r4, r26, r15, r3, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00b6, code lost:
        r4 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00b9, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00ba, code lost:
        r4 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00bd, code lost:
        r17 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00d2, code lost:
        r1 = createViewFromTag(r26, r0, r15, r3, r14);
        r2 = (android.view.ViewGroup) r26;
        r3 = r15.obtainStyledAttributes(r27, com.android.internal.R.styleable.Include);
        r5 = r3.getResourceId(0, -1);
        r6 = r3.getInt(1, -1);
        r3.recycle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00eb, code lost:
        r21 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f1, code lost:
        r21 = r2.generateLayoutParams(r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00f4, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00f5, code lost:
        r4 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x013a, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x013b, code lost:
        r4 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x015d, code lost:
        throw new android.view.InflateException(r4.getPositionDescription() + ": No start tag found!");
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0163, code lost:
        r4.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0166, code lost:
        throw r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public protected void parseInclude(org.xmlpull.v1.XmlPullParser r24, android.content.Context r25, android.view.View r26, android.util.AttributeSet r27) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 414
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.LayoutInflater.parseInclude(org.xmlpull.v1.XmlPullParser, android.content.Context, android.view.View, android.util.AttributeSet):void");
    }

    static final synchronized void consumeChildElements(XmlPullParser parser) throws XmlPullParserException, IOException {
        int type;
        int currentDepth = parser.getDepth();
        do {
            type = parser.next();
            if (type == 3 && parser.getDepth() <= currentDepth) {
                return;
            }
        } while (type != 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class BlinkLayout extends FrameLayout {
        private static final int BLINK_DELAY = 500;
        private static final int MESSAGE_BLINK = 66;
        private boolean mBlink;
        private boolean mBlinkState;
        private final Handler mHandler;

        public synchronized BlinkLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.mHandler = new Handler(new Handler.Callback() { // from class: android.view.LayoutInflater.BlinkLayout.1
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message msg) {
                    if (msg.what == 66) {
                        if (BlinkLayout.this.mBlink) {
                            BlinkLayout.this.mBlinkState = !BlinkLayout.this.mBlinkState;
                            BlinkLayout.this.makeBlink();
                        }
                        BlinkLayout.this.invalidate();
                        return true;
                    }
                    return false;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void makeBlink() {
            Message message = this.mHandler.obtainMessage(66);
            this.mHandler.sendMessageDelayed(message, 500L);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.mBlink = true;
            this.mBlinkState = true;
            makeBlink();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.mBlink = false;
            this.mBlinkState = true;
            this.mHandler.removeMessages(66);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (this.mBlinkState) {
                super.dispatchDraw(canvas);
            }
        }
    }
}
