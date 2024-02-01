package com.android.internal.util;

import android.app.slice.Slice;
import android.app.slice.SliceItem;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Xml;
import com.android.ims.ImsConfig;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: classes3.dex */
public class XmlUtils {
    private static final String STRING_ARRAY_SEPARATOR = ":";

    /* loaded from: classes3.dex */
    public interface ReadMapCallback {
        synchronized Object readThisUnknownObjectXml(XmlPullParser xmlPullParser, String str) throws XmlPullParserException, IOException;
    }

    /* loaded from: classes3.dex */
    public interface WriteMapCallback {
        synchronized void writeUnknownObject(Object obj, String str, XmlSerializer xmlSerializer) throws XmlPullParserException, IOException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void skipCurrentTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type == 3 && parser.getDepth() <= outerDepth) {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public static final synchronized int convertValueToList(CharSequence value, String[] options, int defaultValue) {
        if (value != null) {
            for (int i = 0; i < options.length; i++) {
                if (value.equals(options[i])) {
                    return i;
                }
            }
        }
        return defaultValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean convertValueToBoolean(CharSequence value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (!value.equals("1") && !value.equals("true") && !value.equals("TRUE")) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int convertValueToInt(CharSequence charSeq, int defaultValue) {
        if (charSeq == null) {
            return defaultValue;
        }
        String nm = charSeq.toString();
        int sign = 1;
        int index = 0;
        int len = nm.length();
        int base = 10;
        if ('-' == nm.charAt(0)) {
            sign = -1;
            index = 0 + 1;
        }
        if ('0' == nm.charAt(index)) {
            if (index == len - 1) {
                return 0;
            }
            char c = nm.charAt(index + 1);
            if ('x' == c || 'X' == c) {
                index += 2;
                base = 16;
            } else {
                index++;
                base = 8;
            }
        } else if ('#' == nm.charAt(index)) {
            index++;
            base = 16;
        }
        return Integer.parseInt(nm.substring(index), base) * sign;
    }

    public static synchronized int convertValueToUnsignedInt(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return parseUnsignedIntAttribute(value);
    }

    public static synchronized int parseUnsignedIntAttribute(CharSequence charSeq) {
        String value = charSeq.toString();
        int index = 0;
        int len = value.length();
        int base = 10;
        if ('0' == value.charAt(0)) {
            if (0 == len - 1) {
                return 0;
            }
            char c = value.charAt(0 + 1);
            if ('x' == c || 'X' == c) {
                index = 0 + 2;
                base = 16;
            } else {
                index = 0 + 1;
                base = 8;
            }
        } else if ('#' == value.charAt(0)) {
            index = 0 + 1;
            base = 16;
        }
        return (int) Long.parseLong(value.substring(index), base);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void writeMapXml(Map val, OutputStream out) throws XmlPullParserException, IOException {
        XmlSerializer serializer = new FastXmlSerializer();
        serializer.setOutput(out, StandardCharsets.UTF_8.name());
        serializer.startDocument(null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        writeMapXml(val, (String) null, serializer);
        serializer.endDocument();
    }

    public static final synchronized void writeListXml(List val, OutputStream out) throws XmlPullParserException, IOException {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, StandardCharsets.UTF_8.name());
        serializer.startDocument(null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        writeListXml(val, null, serializer);
        serializer.endDocument();
    }

    public static final synchronized void writeMapXml(Map val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        writeMapXml(val, name, out, null);
    }

    public static final synchronized void writeMapXml(Map val, String name, XmlSerializer out, WriteMapCallback callback) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "map");
        if (name != null) {
            out.attribute(null, "name", name);
        }
        writeMapXml(val, out, callback);
        out.endTag(null, "map");
    }

    public static final synchronized void writeMapXml(Map val, XmlSerializer out, WriteMapCallback callback) throws XmlPullParserException, IOException {
        if (val == null) {
            return;
        }
        Set<Map.Entry> s = val.entrySet();
        for (Map.Entry e : s) {
            writeValueXml(e.getValue(), (String) e.getKey(), out, callback);
        }
    }

    public static final synchronized void writeListXml(List val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, Slice.HINT_LIST);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        int N = val.size();
        for (int i = 0; i < N; i++) {
            writeValueXml(val.get(i), null, out);
        }
        out.endTag(null, Slice.HINT_LIST);
    }

    public static final synchronized void writeSetXml(Set val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "set");
        if (name != null) {
            out.attribute(null, "name", name);
        }
        for (Object v : val) {
            writeValueXml(v, null, out);
        }
        out.endTag(null, "set");
    }

    public static final synchronized void writeByteArrayXml(byte[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "byte-array");
        if (name != null) {
            out.attribute(null, "name", name);
        }
        int N = val.length;
        out.attribute(null, "num", Integer.toString(N));
        StringBuilder sb = new StringBuilder(val.length * 2);
        for (int b : val) {
            int h = (b >> 4) & 15;
            sb.append((char) (h >= 10 ? (97 + h) - 10 : 48 + h));
            int h2 = b & 15;
            sb.append((char) (h2 >= 10 ? (97 + h2) - 10 : 48 + h2));
        }
        out.text(sb.toString());
        out.endTag(null, "byte-array");
    }

    public static final synchronized void writeIntArrayXml(int[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "int-array");
        if (name != null) {
            out.attribute(null, "name", name);
        }
        int N = val.length;
        out.attribute(null, "num", Integer.toString(N));
        for (int i : val) {
            out.startTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
            out.attribute(null, "value", Integer.toString(i));
            out.endTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
        }
        out.endTag(null, "int-array");
    }

    public static final synchronized void writeLongArrayXml(long[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "long-array");
        if (name != null) {
            out.attribute(null, "name", name);
        }
        int N = val.length;
        out.attribute(null, "num", Integer.toString(N));
        for (long j : val) {
            out.startTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
            out.attribute(null, "value", Long.toString(j));
            out.endTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
        }
        out.endTag(null, "long-array");
    }

    public static final synchronized void writeDoubleArrayXml(double[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "double-array");
        if (name != null) {
            out.attribute(null, "name", name);
        }
        int N = val.length;
        out.attribute(null, "num", Integer.toString(N));
        for (double d : val) {
            out.startTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
            out.attribute(null, "value", Double.toString(d));
            out.endTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
        }
        out.endTag(null, "double-array");
    }

    public static final synchronized void writeStringArrayXml(String[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "string-array");
        if (name != null) {
            out.attribute(null, "name", name);
        }
        int N = val.length;
        out.attribute(null, "num", Integer.toString(N));
        for (String str : val) {
            out.startTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
            out.attribute(null, "value", str);
            out.endTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
        }
        out.endTag(null, "string-array");
    }

    public static final synchronized void writeBooleanArrayXml(boolean[] val, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        out.startTag(null, "boolean-array");
        if (name != null) {
            out.attribute(null, "name", name);
        }
        int N = val.length;
        out.attribute(null, "num", Integer.toString(N));
        for (boolean z : val) {
            out.startTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
            out.attribute(null, "value", Boolean.toString(z));
            out.endTag(null, ImsConfig.EXTRA_CHANGED_ITEM);
        }
        out.endTag(null, "boolean-array");
    }

    public static final synchronized void writeValueXml(Object v, String name, XmlSerializer out) throws XmlPullParserException, IOException {
        writeValueXml(v, name, out, null);
    }

    private static final synchronized void writeValueXml(Object v, String name, XmlSerializer out, WriteMapCallback callback) throws XmlPullParserException, IOException {
        String typeStr;
        if (v == null) {
            out.startTag(null, "null");
            if (name != null) {
                out.attribute(null, "name", name);
            }
            out.endTag(null, "null");
        } else if (v instanceof String) {
            out.startTag(null, "string");
            if (name != null) {
                out.attribute(null, "name", name);
            }
            out.text(v.toString());
            out.endTag(null, "string");
        } else {
            if (v instanceof Integer) {
                typeStr = SliceItem.FORMAT_INT;
            } else if (v instanceof Long) {
                typeStr = SliceItem.FORMAT_LONG;
            } else if (v instanceof Float) {
                typeStr = "float";
            } else if (v instanceof Double) {
                typeStr = "double";
            } else if (v instanceof Boolean) {
                typeStr = "boolean";
            } else if (v instanceof byte[]) {
                writeByteArrayXml((byte[]) v, name, out);
                return;
            } else if (v instanceof int[]) {
                writeIntArrayXml((int[]) v, name, out);
                return;
            } else if (v instanceof long[]) {
                writeLongArrayXml((long[]) v, name, out);
                return;
            } else if (v instanceof double[]) {
                writeDoubleArrayXml((double[]) v, name, out);
                return;
            } else if (v instanceof String[]) {
                writeStringArrayXml((String[]) v, name, out);
                return;
            } else if (v instanceof boolean[]) {
                writeBooleanArrayXml((boolean[]) v, name, out);
                return;
            } else if (v instanceof Map) {
                writeMapXml((Map) v, name, out);
                return;
            } else if (v instanceof List) {
                writeListXml((List) v, name, out);
                return;
            } else if (v instanceof Set) {
                writeSetXml((Set) v, name, out);
                return;
            } else if (v instanceof CharSequence) {
                out.startTag(null, "string");
                if (name != null) {
                    out.attribute(null, "name", name);
                }
                out.text(v.toString());
                out.endTag(null, "string");
                return;
            } else if (callback != null) {
                callback.writeUnknownObject(v, name, out);
                return;
            } else {
                throw new RuntimeException("writeValueXml: unable to write value " + v);
            }
            out.startTag(null, typeStr);
            if (name != null) {
                out.attribute(null, "name", name);
            }
            out.attribute(null, "value", v.toString());
            out.endTag(null, typeStr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final HashMap<String, ?> readMapXml(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, StandardCharsets.UTF_8.name());
        return (HashMap) readValueXml(parser, new String[1]);
    }

    public static final synchronized ArrayList readListXml(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, StandardCharsets.UTF_8.name());
        return (ArrayList) readValueXml(parser, new String[1]);
    }

    public static final synchronized HashSet readSetXml(InputStream in) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, null);
        return (HashSet) readValueXml(parser, new String[1]);
    }

    public static final synchronized HashMap<String, ?> readThisMapXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        return readThisMapXml(parser, endTag, name, null);
    }

    public static final synchronized HashMap<String, ?> readThisMapXml(XmlPullParser parser, String endTag, String[] name, ReadMapCallback callback) throws XmlPullParserException, IOException {
        HashMap<String, Object> map = new HashMap<>();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                Object val = readThisValueXml(parser, name, callback, false);
                map.put(name[0], val);
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return map;
                }
                throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final synchronized ArrayMap<String, ?> readThisArrayMapXml(XmlPullParser parser, String endTag, String[] name, ReadMapCallback callback) throws XmlPullParserException, IOException {
        ArrayMap<String, Object> map = new ArrayMap<>();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                Object val = readThisValueXml(parser, name, callback, true);
                map.put(name[0], val);
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return map;
                }
                throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final synchronized ArrayList readThisListXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        return readThisListXml(parser, endTag, name, null, false);
    }

    private static final synchronized ArrayList readThisListXml(XmlPullParser parser, String endTag, String[] name, ReadMapCallback callback, boolean arrayMap) throws XmlPullParserException, IOException {
        ArrayList list = new ArrayList();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                Object val = readThisValueXml(parser, name, callback, arrayMap);
                list.add(val);
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return list;
                }
                throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final synchronized HashSet readThisSetXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        return readThisSetXml(parser, endTag, name, null, false);
    }

    private static final synchronized HashSet readThisSetXml(XmlPullParser parser, String endTag, String[] name, ReadMapCallback callback, boolean arrayMap) throws XmlPullParserException, IOException {
        HashSet set = new HashSet();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                Object val = readThisValueXml(parser, name, callback, arrayMap);
                set.add(val);
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return set;
                }
                throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final synchronized byte[] readThisByteArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            byte[] array = new byte[num];
            int eventType = parser.getEventType();
            do {
                if (eventType == 4) {
                    if (num > 0) {
                        String values = parser.getText();
                        if (values == null || values.length() != num * 2) {
                            throw new XmlPullParserException("Invalid value found in byte-array: " + values);
                        }
                        for (int i = 0; i < num; i++) {
                            char nibbleHighChar = values.charAt(2 * i);
                            char nibbleLowChar = values.charAt((2 * i) + 1);
                            int nibbleHigh = nibbleHighChar > 'a' ? (nibbleHighChar - 'a') + 10 : nibbleHighChar - '0';
                            int nibbleLow = nibbleLowChar > 'a' ? (nibbleLowChar - 'a') + 10 : nibbleLowChar - '0';
                            array[i] = (byte) (((nibbleHigh & 15) << 4) | (nibbleLow & 15));
                        }
                    }
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                }
                eventType = parser.next();
            } while (eventType != 1);
            throw new XmlPullParserException("Document ended before " + endTag + " end tag");
        } catch (NullPointerException e) {
            throw new XmlPullParserException("Need num attribute in byte-array");
        } catch (NumberFormatException e2) {
            throw new XmlPullParserException("Not a number in num attribute in byte-array");
        }
    }

    public static final synchronized int[] readThisIntArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            parser.next();
            int[] array = new int[num];
            int i = 0;
            int eventType = parser.getEventType();
            do {
                if (eventType == 2) {
                    if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        try {
                            array[i] = Integer.parseInt(parser.getAttributeValue(null, "value"));
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    } else {
                        throw new XmlPullParserException("Expected item tag at: " + parser.getName());
                    }
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        i++;
                    } else {
                        throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                    }
                }
                eventType = parser.next();
            } while (eventType != 1);
            throw new XmlPullParserException("Document ended before " + endTag + " end tag");
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in int-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in int-array");
        }
    }

    public static final synchronized long[] readThisLongArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            parser.next();
            long[] array = new long[num];
            int i = 0;
            int eventType = parser.getEventType();
            do {
                if (eventType == 2) {
                    if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        try {
                            array[i] = Long.parseLong(parser.getAttributeValue(null, "value"));
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    } else {
                        throw new XmlPullParserException("Expected item tag at: " + parser.getName());
                    }
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        i++;
                    } else {
                        throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                    }
                }
                eventType = parser.next();
            } while (eventType != 1);
            throw new XmlPullParserException("Document ended before " + endTag + " end tag");
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in long-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in long-array");
        }
    }

    public static final synchronized double[] readThisDoubleArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            parser.next();
            double[] array = new double[num];
            int i = 0;
            int eventType = parser.getEventType();
            do {
                if (eventType == 2) {
                    if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        try {
                            array[i] = Double.parseDouble(parser.getAttributeValue(null, "value"));
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    } else {
                        throw new XmlPullParserException("Expected item tag at: " + parser.getName());
                    }
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        i++;
                    } else {
                        throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                    }
                }
                eventType = parser.next();
            } while (eventType != 1);
            throw new XmlPullParserException("Document ended before " + endTag + " end tag");
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in double-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in double-array");
        }
    }

    public static final synchronized String[] readThisStringArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            parser.next();
            String[] array = new String[num];
            int i = 0;
            int eventType = parser.getEventType();
            do {
                if (eventType == 2) {
                    if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        try {
                            array[i] = parser.getAttributeValue(null, "value");
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    } else {
                        throw new XmlPullParserException("Expected item tag at: " + parser.getName());
                    }
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        i++;
                    } else {
                        throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                    }
                }
                eventType = parser.next();
            } while (eventType != 1);
            throw new XmlPullParserException("Document ended before " + endTag + " end tag");
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in string-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in string-array");
        }
    }

    public static final synchronized boolean[] readThisBooleanArrayXml(XmlPullParser parser, String endTag, String[] name) throws XmlPullParserException, IOException {
        try {
            int num = Integer.parseInt(parser.getAttributeValue(null, "num"));
            parser.next();
            boolean[] array = new boolean[num];
            int i = 0;
            int eventType = parser.getEventType();
            do {
                if (eventType == 2) {
                    if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        try {
                            array[i] = Boolean.parseBoolean(parser.getAttributeValue(null, "value"));
                        } catch (NullPointerException e) {
                            throw new XmlPullParserException("Need value attribute in item");
                        } catch (NumberFormatException e2) {
                            throw new XmlPullParserException("Not a number in value attribute in item");
                        }
                    } else {
                        throw new XmlPullParserException("Expected item tag at: " + parser.getName());
                    }
                } else if (eventType == 3) {
                    if (parser.getName().equals(endTag)) {
                        return array;
                    }
                    if (parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        i++;
                    } else {
                        throw new XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                    }
                }
                eventType = parser.next();
            } while (eventType != 1);
            throw new XmlPullParserException("Document ended before " + endTag + " end tag");
        } catch (NullPointerException e3) {
            throw new XmlPullParserException("Need num attribute in string-array");
        } catch (NumberFormatException e4) {
            throw new XmlPullParserException("Not a number in num attribute in string-array");
        }
    }

    public static final synchronized Object readValueXml(XmlPullParser parser, String[] name) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (eventType != 2) {
            if (eventType == 3) {
                throw new XmlPullParserException("Unexpected end tag at: " + parser.getName());
            } else if (eventType == 4) {
                throw new XmlPullParserException("Unexpected text: " + parser.getText());
            } else {
                eventType = parser.next();
                if (eventType == 1) {
                    throw new XmlPullParserException("Unexpected end of document");
                }
            }
        }
        return readThisValueXml(parser, name, null, false);
    }

    private static final synchronized Object readThisValueXml(XmlPullParser parser, String[] name, ReadMapCallback callback, boolean arrayMap) throws XmlPullParserException, IOException {
        Object res;
        Object res2;
        int eventType;
        String valueName = parser.getAttributeValue(null, "name");
        String tagName = parser.getName();
        if (tagName.equals("null")) {
            res2 = null;
        } else if (tagName.equals("string")) {
            String value = "";
            while (true) {
                int eventType2 = parser.next();
                if (eventType2 != 1) {
                    if (eventType2 == 3) {
                        if (parser.getName().equals("string")) {
                            name[0] = valueName;
                            return value;
                        }
                        throw new XmlPullParserException("Unexpected end tag in <string>: " + parser.getName());
                    } else if (eventType2 == 4) {
                        value = value + parser.getText();
                    } else if (eventType2 == 2) {
                        throw new XmlPullParserException("Unexpected start tag in <string>: " + parser.getName());
                    }
                } else {
                    throw new XmlPullParserException("Unexpected end of document in <string>");
                }
            }
        } else {
            Object res3 = readThisPrimitiveValueXml(parser, tagName);
            if (res3 == null) {
                if (tagName.equals("byte-array")) {
                    Object res4 = readThisByteArrayXml(parser, "byte-array", name);
                    name[0] = valueName;
                    return res4;
                } else if (tagName.equals("int-array")) {
                    Object res5 = readThisIntArrayXml(parser, "int-array", name);
                    name[0] = valueName;
                    return res5;
                } else if (tagName.equals("long-array")) {
                    Object res6 = readThisLongArrayXml(parser, "long-array", name);
                    name[0] = valueName;
                    return res6;
                } else if (tagName.equals("double-array")) {
                    Object res7 = readThisDoubleArrayXml(parser, "double-array", name);
                    name[0] = valueName;
                    return res7;
                } else if (tagName.equals("string-array")) {
                    Object res8 = readThisStringArrayXml(parser, "string-array", name);
                    name[0] = valueName;
                    return res8;
                } else if (tagName.equals("boolean-array")) {
                    Object res9 = readThisBooleanArrayXml(parser, "boolean-array", name);
                    name[0] = valueName;
                    return res9;
                } else if (tagName.equals("map")) {
                    parser.next();
                    if (arrayMap) {
                        res = readThisArrayMapXml(parser, "map", name, callback);
                    } else {
                        res = readThisMapXml(parser, "map", name, callback);
                    }
                    name[0] = valueName;
                    return res;
                } else if (tagName.equals(Slice.HINT_LIST)) {
                    parser.next();
                    Object res10 = readThisListXml(parser, Slice.HINT_LIST, name, callback, arrayMap);
                    name[0] = valueName;
                    return res10;
                } else if (tagName.equals("set")) {
                    parser.next();
                    Object res11 = readThisSetXml(parser, "set", name, callback, arrayMap);
                    name[0] = valueName;
                    return res11;
                } else if (callback != null) {
                    Object res12 = callback.readThisUnknownObjectXml(parser, tagName);
                    name[0] = valueName;
                    return res12;
                } else {
                    throw new XmlPullParserException("Unknown tag: " + tagName);
                }
            }
            res2 = res3;
        }
        do {
            eventType = parser.next();
            if (eventType != 1) {
                if (eventType == 3) {
                    if (parser.getName().equals(tagName)) {
                        name[0] = valueName;
                        return res2;
                    }
                    throw new XmlPullParserException("Unexpected end tag in <" + tagName + ">: " + parser.getName());
                } else if (eventType == 4) {
                    throw new XmlPullParserException("Unexpected text in <" + tagName + ">: " + parser.getName());
                }
            } else {
                throw new XmlPullParserException("Unexpected end of document in <" + tagName + ">");
            }
        } while (eventType != 2);
        throw new XmlPullParserException("Unexpected start tag in <" + tagName + ">: " + parser.getName());
    }

    private static final synchronized Object readThisPrimitiveValueXml(XmlPullParser parser, String tagName) throws XmlPullParserException, IOException {
        try {
            if (tagName.equals(SliceItem.FORMAT_INT)) {
                return Integer.valueOf(Integer.parseInt(parser.getAttributeValue(null, "value")));
            }
            if (tagName.equals(SliceItem.FORMAT_LONG)) {
                return Long.valueOf(parser.getAttributeValue(null, "value"));
            }
            if (tagName.equals("float")) {
                return new Float(parser.getAttributeValue(null, "value"));
            }
            if (tagName.equals("double")) {
                return new Double(parser.getAttributeValue(null, "value"));
            }
            if (tagName.equals("boolean")) {
                return Boolean.valueOf(parser.getAttributeValue(null, "value"));
            }
            return null;
        } catch (NullPointerException e) {
            throw new XmlPullParserException("Need value attribute in <" + tagName + ">");
        } catch (NumberFormatException e2) {
            throw new XmlPullParserException("Not a number in value attribute in <" + tagName + ">");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void beginDocument(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException {
        int type;
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type != 2) {
            throw new XmlPullParserException("No start tag found");
        }
        if (!parser.getName().equals(firstElementName)) {
            throw new XmlPullParserException("Unexpected start tag: found " + parser.getName() + ", expected " + firstElementName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        int type;
        do {
            type = parser.next();
            if (type == 2) {
                return;
            }
        } while (type != 1);
    }

    public static synchronized boolean nextElementWithin(XmlPullParser parser, int outerDepth) throws IOException, XmlPullParserException {
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type == 3 && parser.getDepth() == outerDepth) {
                    return false;
                }
                if (type == 2 && parser.getDepth() == outerDepth + 1) {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public static synchronized int readIntAttribute(XmlPullParser in, String name, int defaultValue) {
        String value = in.getAttributeValue(null, name);
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static synchronized int readIntAttribute(XmlPullParser in, String name) throws IOException {
        String value = in.getAttributeValue(null, name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ProtocolException("problem parsing " + name + "=" + value + " as int");
        }
    }

    public static synchronized void writeIntAttribute(XmlSerializer out, String name, int value) throws IOException {
        out.attribute(null, name, Integer.toString(value));
    }

    public static synchronized long readLongAttribute(XmlPullParser in, String name, long defaultValue) {
        String value = in.getAttributeValue(null, name);
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static synchronized long readLongAttribute(XmlPullParser in, String name) throws IOException {
        String value = in.getAttributeValue(null, name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new ProtocolException("problem parsing " + name + "=" + value + " as long");
        }
    }

    public static synchronized void writeLongAttribute(XmlSerializer out, String name, long value) throws IOException {
        out.attribute(null, name, Long.toString(value));
    }

    public static synchronized float readFloatAttribute(XmlPullParser in, String name) throws IOException {
        String value = in.getAttributeValue(null, name);
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new ProtocolException("problem parsing " + name + "=" + value + " as long");
        }
    }

    public static synchronized void writeFloatAttribute(XmlSerializer out, String name, float value) throws IOException {
        out.attribute(null, name, Float.toString(value));
    }

    public static synchronized boolean readBooleanAttribute(XmlPullParser in, String name) {
        String value = in.getAttributeValue(null, name);
        return Boolean.parseBoolean(value);
    }

    public static synchronized boolean readBooleanAttribute(XmlPullParser in, String name, boolean defaultValue) {
        String value = in.getAttributeValue(null, name);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public static synchronized void writeBooleanAttribute(XmlSerializer out, String name, boolean value) throws IOException {
        out.attribute(null, name, Boolean.toString(value));
    }

    public static synchronized Uri readUriAttribute(XmlPullParser in, String name) {
        String value = in.getAttributeValue(null, name);
        if (value != null) {
            return Uri.parse(value);
        }
        return null;
    }

    public static synchronized void writeUriAttribute(XmlSerializer out, String name, Uri value) throws IOException {
        if (value != null) {
            out.attribute(null, name, value.toString());
        }
    }

    public static synchronized String readStringAttribute(XmlPullParser in, String name) {
        return in.getAttributeValue(null, name);
    }

    public static synchronized void writeStringAttribute(XmlSerializer out, String name, CharSequence value) throws IOException {
        if (value != null) {
            out.attribute(null, name, value.toString());
        }
    }

    public static synchronized byte[] readByteArrayAttribute(XmlPullParser in, String name) {
        String value = in.getAttributeValue(null, name);
        if (value == null) {
            return null;
        }
        return Base64.decode(value, 0);
    }

    public static synchronized void writeByteArrayAttribute(XmlSerializer out, String name, byte[] value) throws IOException {
        if (value != null) {
            out.attribute(null, name, Base64.encodeToString(value, 0));
        }
    }

    public static synchronized Bitmap readBitmapAttribute(XmlPullParser in, String name) {
        byte[] value = readByteArrayAttribute(in, name);
        if (value != null) {
            return BitmapFactory.decodeByteArray(value, 0, value.length);
        }
        return null;
    }

    @Deprecated
    public static synchronized void writeBitmapAttribute(XmlSerializer out, String name, Bitmap value) throws IOException {
        if (value != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            value.compress(Bitmap.CompressFormat.PNG, 90, os);
            writeByteArrayAttribute(out, name, os.toByteArray());
        }
    }
}
