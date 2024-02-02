package android.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import org.apache.harmony.xml.ExpatReader;
import org.kxml2.io.KXmlParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: classes2.dex */
public class Xml {
    public static String FEATURE_RELAXED = "http://xmlpull.org/v1/doc/features.html#relaxed";

    public static void parse(String xml, ContentHandler contentHandler) throws SAXException {
        try {
            ExpatReader expatReader = new ExpatReader();
            expatReader.setContentHandler(contentHandler);
            expatReader.parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static void parse(Reader in, ContentHandler contentHandler) throws IOException, SAXException {
        ExpatReader expatReader = new ExpatReader();
        expatReader.setContentHandler(contentHandler);
        expatReader.parse(new InputSource(in));
    }

    public static void parse(InputStream in, Encoding encoding, ContentHandler contentHandler) throws IOException, SAXException {
        ExpatReader expatReader = new ExpatReader();
        expatReader.setContentHandler(contentHandler);
        InputSource source = new InputSource(in);
        source.setEncoding(encoding.expatName);
        expatReader.parse(source);
    }

    public static XmlPullParser newPullParser() {
        try {
            KXmlParser parser = new KXmlParser();
            parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-docdecl", true);
            parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
            return parser;
        } catch (XmlPullParserException e) {
            throw new AssertionError();
        }
    }

    public static XmlSerializer newSerializer() {
        try {
            return XmlSerializerFactory.instance.newSerializer();
        } catch (XmlPullParserException e) {
            throw new AssertionError(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class XmlSerializerFactory {
        static final String TYPE = "org.kxml2.io.KXmlParser,org.kxml2.io.KXmlSerializer";
        static final XmlPullParserFactory instance;

        synchronized XmlSerializerFactory() {
        }

        static {
            try {
                instance = XmlPullParserFactory.newInstance(TYPE, null);
            } catch (XmlPullParserException e) {
                throw new AssertionError(e);
            }
        }
    }

    /* loaded from: classes2.dex */
    public enum Encoding {
        US_ASCII("US-ASCII"),
        UTF_8("UTF-8"),
        UTF_16("UTF-16"),
        ISO_8859_1("ISO-8859-1");
        
        final String expatName;

        Encoding(String expatName) {
            this.expatName = expatName;
        }
    }

    public static Encoding findEncodingByName(String encodingName) throws UnsupportedEncodingException {
        Encoding[] values;
        if (encodingName == null) {
            return Encoding.UTF_8;
        }
        for (Encoding encoding : Encoding.values()) {
            if (encoding.expatName.equalsIgnoreCase(encodingName)) {
                return encoding;
            }
        }
        throw new UnsupportedEncodingException(encodingName);
    }

    public static AttributeSet asAttributeSet(XmlPullParser parser) {
        if (parser instanceof AttributeSet) {
            return (AttributeSet) parser;
        }
        return new XmlPullAttributes(parser);
    }
}
