package android.content.pm;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: classes.dex */
public interface XmlSerializerAndParser<T> {
    /* JADX INFO: Access modifiers changed from: private */
    T createFromXml(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException;

    /* JADX INFO: Access modifiers changed from: private */
    void writeAsXml(T t, XmlSerializer xmlSerializer) throws IOException;
}
