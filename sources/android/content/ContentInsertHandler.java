package android.content;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
/* loaded from: classes.dex */
public interface ContentInsertHandler extends ContentHandler {
    synchronized void insert(ContentResolver contentResolver, InputStream inputStream) throws IOException, SAXException;

    synchronized void insert(ContentResolver contentResolver, String str) throws SAXException;
}
