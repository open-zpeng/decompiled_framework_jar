package android.view.inputmethod;

import android.os.Bundle;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
/* loaded from: classes2.dex */
public final class InputConnectionInspector {
    private static final Map<Class, Integer> sMissingMethodsMap = Collections.synchronizedMap(new WeakHashMap());

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface MissingMethodFlags {
        public static final int CLOSE_CONNECTION = 64;
        public static final int COMMIT_CONTENT = 128;
        public static final int COMMIT_CORRECTION = 4;
        public static final int DELETE_SURROUNDING_TEXT_IN_CODE_POINTS = 16;
        public static final int GET_HANDLER = 32;
        public static final int GET_SELECTED_TEXT = 1;
        public static final int REQUEST_CURSOR_UPDATES = 8;
        public static final int SET_COMPOSING_REGION = 2;
    }

    public static synchronized int getMissingMethodFlags(InputConnection ic) {
        if (ic == null || (ic instanceof BaseInputConnection)) {
            return 0;
        }
        if (ic instanceof InputConnectionWrapper) {
            return ((InputConnectionWrapper) ic).getMissingMethodFlags();
        }
        return getMissingMethodFlagsInternal(ic.getClass());
    }

    public static synchronized int getMissingMethodFlagsInternal(Class clazz) {
        Integer cachedFlags = sMissingMethodsMap.get(clazz);
        if (cachedFlags != null) {
            return cachedFlags.intValue();
        }
        int flags = 0;
        if (!hasGetSelectedText(clazz)) {
            flags = 0 | 1;
        }
        if (!hasSetComposingRegion(clazz)) {
            flags |= 2;
        }
        if (!hasCommitCorrection(clazz)) {
            flags |= 4;
        }
        if (!hasRequestCursorUpdate(clazz)) {
            flags |= 8;
        }
        if (!hasDeleteSurroundingTextInCodePoints(clazz)) {
            flags |= 16;
        }
        if (!hasGetHandler(clazz)) {
            flags |= 32;
        }
        if (!hasCloseConnection(clazz)) {
            flags |= 64;
        }
        if (!hasCommitContent(clazz)) {
            flags |= 128;
        }
        sMissingMethodsMap.put(clazz, Integer.valueOf(flags));
        return flags;
    }

    private static synchronized boolean hasGetSelectedText(Class clazz) {
        try {
            Method method = clazz.getMethod("getSelectedText", Integer.TYPE);
            return !Modifier.isAbstract(method.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static synchronized boolean hasSetComposingRegion(Class clazz) {
        try {
            Method method = clazz.getMethod("setComposingRegion", Integer.TYPE, Integer.TYPE);
            return !Modifier.isAbstract(method.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static synchronized boolean hasCommitCorrection(Class clazz) {
        try {
            Method method = clazz.getMethod("commitCorrection", CorrectionInfo.class);
            return !Modifier.isAbstract(method.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static synchronized boolean hasRequestCursorUpdate(Class clazz) {
        try {
            Method method = clazz.getMethod("requestCursorUpdates", Integer.TYPE);
            return !Modifier.isAbstract(method.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static synchronized boolean hasDeleteSurroundingTextInCodePoints(Class clazz) {
        try {
            Method method = clazz.getMethod("deleteSurroundingTextInCodePoints", Integer.TYPE, Integer.TYPE);
            return !Modifier.isAbstract(method.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static synchronized boolean hasGetHandler(Class clazz) {
        try {
            Method method = clazz.getMethod("getHandler", new Class[0]);
            return !Modifier.isAbstract(method.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static synchronized boolean hasCloseConnection(Class clazz) {
        try {
            Method method = clazz.getMethod("closeConnection", new Class[0]);
            return !Modifier.isAbstract(method.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static synchronized boolean hasCommitContent(Class clazz) {
        try {
            Method method = clazz.getMethod("commitContent", InputContentInfo.class, Integer.TYPE, Bundle.class);
            return !Modifier.isAbstract(method.getModifiers());
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static synchronized String getMissingMethodFlagsAsString(int flags) {
        StringBuilder sb = new StringBuilder();
        boolean isEmpty = true;
        if ((flags & 1) != 0) {
            sb.append("getSelectedText(int)");
            isEmpty = false;
        }
        if ((flags & 2) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("setComposingRegion(int, int)");
            isEmpty = false;
        }
        if ((flags & 4) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("commitCorrection(CorrectionInfo)");
            isEmpty = false;
        }
        if ((flags & 8) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("requestCursorUpdate(int)");
            isEmpty = false;
        }
        if ((flags & 16) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("deleteSurroundingTextInCodePoints(int, int)");
            isEmpty = false;
        }
        if ((flags & 32) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("getHandler()");
        }
        if ((flags & 64) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("closeConnection()");
        }
        if ((flags & 128) != 0) {
            if (!isEmpty) {
                sb.append(",");
            }
            sb.append("commitContent(InputContentInfo, Bundle)");
        }
        return sb.toString();
    }
}
