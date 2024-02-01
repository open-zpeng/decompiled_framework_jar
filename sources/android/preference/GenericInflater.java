package android.preference;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.preference.GenericInflater.Parent;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class GenericInflater<T, P extends Parent> {
    private static final Class[] mConstructorSignature = {Context.class, AttributeSet.class};
    private static final HashMap sConstructorMap = new HashMap();
    private final boolean DEBUG;
    private final Object[] mConstructorArgs;
    protected final Context mContext;
    private String mDefaultPackage;
    private Factory<T> mFactory;
    private boolean mFactorySet;

    /* loaded from: classes2.dex */
    public interface Factory<T> {
        synchronized T onCreateItem(String str, Context context, AttributeSet attributeSet);
    }

    /* loaded from: classes2.dex */
    public interface Parent<T> {
        synchronized void addItemFromInflater(T t);
    }

    public abstract synchronized GenericInflater cloneInContext(Context context);

    /* loaded from: classes2.dex */
    private static class FactoryMerger<T> implements Factory<T> {
        private final Factory<T> mF1;
        private final Factory<T> mF2;

        synchronized FactoryMerger(Factory<T> f1, Factory<T> f2) {
            this.mF1 = f1;
            this.mF2 = f2;
        }

        @Override // android.preference.GenericInflater.Factory
        public synchronized T onCreateItem(String name, Context context, AttributeSet attrs) {
            T v = this.mF1.onCreateItem(name, context, attrs);
            return v != null ? v : this.mF2.onCreateItem(name, context, attrs);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized GenericInflater(Context context) {
        this.DEBUG = false;
        this.mConstructorArgs = new Object[2];
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized GenericInflater(GenericInflater<T, P> original, Context newContext) {
        this.DEBUG = false;
        this.mConstructorArgs = new Object[2];
        this.mContext = newContext;
        this.mFactory = original.mFactory;
    }

    public synchronized void setDefaultPackage(String defaultPackage) {
        this.mDefaultPackage = defaultPackage;
    }

    public synchronized String getDefaultPackage() {
        return this.mDefaultPackage;
    }

    public synchronized Context getContext() {
        return this.mContext;
    }

    public final synchronized Factory<T> getFactory() {
        return this.mFactory;
    }

    public synchronized void setFactory(Factory<T> factory) {
        if (this.mFactorySet) {
            throw new IllegalStateException("A factory has already been set on this inflater");
        }
        if (factory == null) {
            throw new NullPointerException("Given factory can not be null");
        }
        this.mFactorySet = true;
        if (this.mFactory == null) {
            this.mFactory = factory;
        } else {
            this.mFactory = new FactoryMerger(factory, this.mFactory);
        }
    }

    public synchronized T inflate(int resource, P root) {
        return inflate(resource, (int) root, root != null);
    }

    public synchronized T inflate(XmlPullParser parser, P root) {
        return inflate(parser, (XmlPullParser) root, root != null);
    }

    public synchronized T inflate(int resource, P root, boolean attachToRoot) {
        XmlResourceParser parser = getContext().getResources().getXml(resource);
        try {
            return inflate((XmlPullParser) parser, (XmlResourceParser) root, attachToRoot);
        } finally {
            parser.close();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public synchronized T inflate(XmlPullParser parser, P root, boolean attachToRoot) {
        int type;
        T result;
        synchronized (this.mConstructorArgs) {
            AttributeSet attrs = Xml.asAttributeSet(parser);
            this.mConstructorArgs[0] = this.mContext;
            while (true) {
                try {
                    try {
                        try {
                            type = parser.next();
                            if (type == 2 || type == 1) {
                                break;
                            }
                        } catch (InflateException e) {
                            throw e;
                        }
                    } catch (XmlPullParserException e2) {
                        InflateException ex = new InflateException(e2.getMessage());
                        ex.initCause(e2);
                        throw ex;
                    }
                } catch (IOException e3) {
                    InflateException ex2 = new InflateException(parser.getPositionDescription() + ": " + e3.getMessage());
                    ex2.initCause(e3);
                    throw ex2;
                }
            }
            if (type != 2) {
                throw new InflateException(parser.getPositionDescription() + ": No start tag found!");
            }
            T xmlRoot = createItemFromTag(parser, parser.getName(), attrs);
            result = (T) onMergeRoots(root, attachToRoot, (Parent) xmlRoot);
            rInflate(parser, result, attrs);
        }
        return result;
    }

    public final synchronized T createItem(String name, String prefix, AttributeSet attrs) throws ClassNotFoundException, InflateException {
        String str;
        String str2;
        Constructor constructor = (Constructor) sConstructorMap.get(name);
        if (constructor == null) {
            try {
                ClassLoader classLoader = this.mContext.getClassLoader();
                if (prefix != null) {
                    str = prefix + name;
                } else {
                    str = name;
                }
                Class clazz = classLoader.loadClass(str);
                constructor = clazz.getConstructor(mConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name, constructor);
            } catch (ClassNotFoundException e) {
                throw e;
            } catch (NoSuchMethodException e2) {
                StringBuilder sb = new StringBuilder();
                sb.append(attrs.getPositionDescription());
                sb.append(": Error inflating class ");
                if (prefix != null) {
                    str2 = prefix + name;
                } else {
                    str2 = name;
                }
                sb.append(str2);
                InflateException ie = new InflateException(sb.toString());
                ie.initCause(e2);
                throw ie;
            } catch (Exception e3) {
                InflateException ie2 = new InflateException(attrs.getPositionDescription() + ": Error inflating class " + constructor.getClass().getName());
                ie2.initCause(e3);
                throw ie2;
            }
        }
        Object[] args = this.mConstructorArgs;
        args[1] = attrs;
        return constructor.newInstance(args);
    }

    protected synchronized T onCreateItem(String name, AttributeSet attrs) throws ClassNotFoundException {
        return createItem(name, this.mDefaultPackage, attrs);
    }

    private final synchronized T createItemFromTag(XmlPullParser parser, String name, AttributeSet attrs) {
        try {
            T item = this.mFactory == null ? null : this.mFactory.onCreateItem(name, this.mContext, attrs);
            if (item == null) {
                if (-1 == name.indexOf(46)) {
                    return onCreateItem(name, attrs);
                }
                return createItem(name, null, attrs);
            }
            return item;
        } catch (InflateException e) {
            throw e;
        } catch (ClassNotFoundException e2) {
            InflateException ie = new InflateException(attrs.getPositionDescription() + ": Error inflating class " + name);
            ie.initCause(e2);
            throw ie;
        } catch (Exception e3) {
            InflateException ie2 = new InflateException(attrs.getPositionDescription() + ": Error inflating class " + name);
            ie2.initCause(e3);
            throw ie2;
        }
    }

    private synchronized void rInflate(XmlPullParser parser, T parent, AttributeSet attrs) throws XmlPullParserException, IOException {
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if ((type != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2 && !onCreateCustomFromTag(parser, parent, attrs)) {
                    String name = parser.getName();
                    T item = createItemFromTag(parser, name, attrs);
                    ((Parent) parent).addItemFromInflater(item);
                    rInflate(parser, item, attrs);
                }
            } else {
                return;
            }
        }
    }

    protected synchronized boolean onCreateCustomFromTag(XmlPullParser parser, T parent, AttributeSet attrs) throws XmlPullParserException {
        return false;
    }

    protected synchronized P onMergeRoots(P givenRoot, boolean attachToGivenRoot, P xmlRoot) {
        return xmlRoot;
    }
}
