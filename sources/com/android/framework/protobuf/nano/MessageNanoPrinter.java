package com.android.framework.protobuf.nano;

import android.content.IntentFilter;
import android.telecom.Logging.Session;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/* loaded from: classes3.dex */
public final class MessageNanoPrinter {
    private static final String INDENT = "  ";
    private static final int MAX_STRING_LEN = 200;

    private MessageNanoPrinter() {
    }

    public static <T extends MessageNano> String print(T message) {
        if (message == null) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        try {
            print(null, message, new StringBuffer(), buf);
            return buf.toString();
        } catch (IllegalAccessException e) {
            return "Error printing proto: " + e.getMessage();
        } catch (InvocationTargetException e2) {
            return "Error printing proto: " + e2.getMessage();
        }
    }

    private static void print(String identifier, Object object, StringBuffer indentBuf, StringBuffer buf) throws IllegalAccessException, InvocationTargetException {
        Method[] methodArr;
        Field[] fieldArr;
        int i;
        if (object != null) {
            if (object instanceof MessageNano) {
                int origIndentBufLength = indentBuf.length();
                if (identifier != null) {
                    buf.append(indentBuf);
                    buf.append(deCamelCaseify(identifier));
                    buf.append(" <\n");
                    indentBuf.append(INDENT);
                }
                Class<?> clazz = object.getClass();
                Field[] fields = clazz.getFields();
                int length = fields.length;
                int i2 = 0;
                while (i2 < length) {
                    Field field = fields[i2];
                    int modifiers = field.getModifiers();
                    String fieldName = field.getName();
                    if ("cachedSize".equals(fieldName)) {
                        fieldArr = fields;
                        i = length;
                    } else if ((modifiers & 1) != 1 || (modifiers & 8) == 8) {
                        fieldArr = fields;
                        i = length;
                    } else if (fieldName.startsWith(Session.SESSION_SEPARATION_CHAR_CHILD)) {
                        fieldArr = fields;
                        i = length;
                    } else if (fieldName.endsWith(Session.SESSION_SEPARATION_CHAR_CHILD)) {
                        fieldArr = fields;
                        i = length;
                    } else {
                        Class<?> fieldType = field.getType();
                        Object value = field.get(object);
                        if (fieldType.isArray()) {
                            Class<?> arrayType = fieldType.getComponentType();
                            if (arrayType == Byte.TYPE) {
                                print(fieldName, value, indentBuf, buf);
                                fieldArr = fields;
                                i = length;
                            } else {
                                int len = value == null ? 0 : Array.getLength(value);
                                fieldArr = fields;
                                int i3 = 0;
                                while (i3 < len) {
                                    int i4 = length;
                                    Object elem = Array.get(value, i3);
                                    print(fieldName, elem, indentBuf, buf);
                                    i3++;
                                    length = i4;
                                }
                                i = length;
                            }
                        } else {
                            fieldArr = fields;
                            i = length;
                            print(fieldName, value, indentBuf, buf);
                        }
                    }
                    i2++;
                    length = i;
                    fields = fieldArr;
                }
                Method[] methods = clazz.getMethods();
                int length2 = methods.length;
                int i5 = 0;
                while (i5 < length2) {
                    Method method = methods[i5];
                    String name = method.getName();
                    if (!name.startsWith("set")) {
                        methodArr = methods;
                    } else {
                        String subfieldName = name.substring(3);
                        try {
                            try {
                                Method hazzer = clazz.getMethod("has" + subfieldName, new Class[0]);
                                if (((Boolean) hazzer.invoke(object, new Object[0])).booleanValue()) {
                                    try {
                                        methodArr = methods;
                                        try {
                                            Method getter = clazz.getMethod("get" + subfieldName, new Class[0]);
                                            print(subfieldName, getter.invoke(object, new Object[0]), indentBuf, buf);
                                        } catch (NoSuchMethodException e) {
                                        }
                                    } catch (NoSuchMethodException e2) {
                                        methodArr = methods;
                                    }
                                } else {
                                    methodArr = methods;
                                }
                            } catch (NoSuchMethodException e3) {
                                methodArr = methods;
                            }
                        } catch (NoSuchMethodException e4) {
                            methodArr = methods;
                        }
                    }
                    i5++;
                    methods = methodArr;
                }
                if (identifier != null) {
                    indentBuf.setLength(origIndentBufLength);
                    buf.append(indentBuf);
                    buf.append(">\n");
                }
            } else if (object instanceof Map) {
                Map<?, ?> map = (Map) object;
                String identifier2 = deCamelCaseify(identifier);
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    buf.append(indentBuf);
                    buf.append(identifier2);
                    buf.append(" <\n");
                    int origIndentBufLength2 = indentBuf.length();
                    indentBuf.append(INDENT);
                    print("key", entry.getKey(), indentBuf, buf);
                    print("value", entry.getValue(), indentBuf, buf);
                    indentBuf.setLength(origIndentBufLength2);
                    buf.append(indentBuf);
                    buf.append(">\n");
                }
            } else {
                String identifier3 = deCamelCaseify(identifier);
                buf.append(indentBuf);
                buf.append(identifier3);
                buf.append(": ");
                if (!(object instanceof String)) {
                    if (object instanceof byte[]) {
                        appendQuotedBytes((byte[]) object, buf);
                    } else {
                        buf.append(object);
                    }
                } else {
                    String stringMessage = sanitizeString((String) object);
                    buf.append("\"");
                    buf.append(stringMessage);
                    buf.append("\"");
                }
                buf.append("\n");
            }
        }
    }

    private static String deCamelCaseify(String identifier) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < identifier.length(); i++) {
            char currentChar = identifier.charAt(i);
            if (i == 0) {
                out.append(Character.toLowerCase(currentChar));
            } else if (Character.isUpperCase(currentChar)) {
                out.append('_');
                out.append(Character.toLowerCase(currentChar));
            } else {
                out.append(currentChar);
            }
        }
        return out.toString();
    }

    private static String sanitizeString(String str) {
        if (!str.startsWith(IntentFilter.SCHEME_HTTP) && str.length() > 200) {
            str = str.substring(0, 200) + "[...]";
        }
        return escapeString(str);
    }

    private static String escapeString(String str) {
        int strLen = str.length();
        StringBuilder b = new StringBuilder(strLen);
        for (int i = 0; i < strLen; i++) {
            char original = str.charAt(i);
            if (original < ' ' || original > '~' || original == '\"' || original == '\'') {
                b.append(String.format("\\u%04x", Integer.valueOf(original)));
            } else {
                b.append(original);
            }
        }
        return b.toString();
    }

    private static void appendQuotedBytes(byte[] bytes, StringBuffer builder) {
        if (bytes == null) {
            builder.append("\"\"");
            return;
        }
        builder.append('\"');
        for (byte b : bytes) {
            int ch = b & 255;
            if (ch == 92 || ch == 34) {
                builder.append('\\');
                builder.append((char) ch);
            } else if (ch < 32 || ch >= 127) {
                builder.append(String.format("\\%03o", Integer.valueOf(ch)));
            } else {
                builder.append((char) ch);
            }
        }
        builder.append('\"');
    }
}
