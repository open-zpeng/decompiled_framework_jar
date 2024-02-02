package android.graphics;

import android.graphics.fonts.FontVariationAxis;
import android.media.tv.TvContract;
import android.os.DropBoxManager;
import android.security.KeyChain;
import android.speech.tts.TextToSpeech;
import android.text.FontConfig;
import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class FontListParser {
    private static final Pattern FILENAME_WHITESPACE_PATTERN = Pattern.compile("^[ \\n\\r\\t]+|[ \\n\\r\\t]+$");

    /* JADX INFO: Access modifiers changed from: private */
    public static FontConfig parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, null);
            parser.nextTag();
            return readFamilies(parser);
        } finally {
            in.close();
        }
    }

    private static synchronized FontConfig readFamilies(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<FontConfig.Family> families = new ArrayList<>();
        List<FontConfig.Alias> aliases = new ArrayList<>();
        parser.require(2, null, "familyset");
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                String tag = parser.getName();
                if (tag.equals("family")) {
                    families.add(readFamily(parser));
                } else if (tag.equals(KeyChain.EXTRA_ALIAS)) {
                    aliases.add(readAlias(parser));
                } else {
                    skip(parser);
                }
            }
        }
        return new FontConfig((FontConfig.Family[]) families.toArray(new FontConfig.Family[families.size()]), (FontConfig.Alias[]) aliases.toArray(new FontConfig.Alias[aliases.size()]));
    }

    private static synchronized FontConfig.Family readFamily(XmlPullParser parser) throws XmlPullParserException, IOException {
        String name = parser.getAttributeValue(null, "name");
        String lang = parser.getAttributeValue(null, "lang");
        String[] langs = lang == null ? null : lang.split("\\s+");
        String variant = parser.getAttributeValue(null, TextToSpeech.Engine.KEY_PARAM_VARIANT);
        List<FontConfig.Font> fonts = new ArrayList<>();
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                String tag = parser.getName();
                if (tag.equals("font")) {
                    fonts.add(readFont(parser));
                } else {
                    skip(parser);
                }
            }
        }
        int intVariant = 0;
        if (variant != null) {
            if (variant.equals("compact")) {
                intVariant = 1;
            } else if (variant.equals("elegant")) {
                intVariant = 2;
            }
        }
        return new FontConfig.Family(name, (FontConfig.Font[]) fonts.toArray(new FontConfig.Font[fonts.size()]), langs, intVariant);
    }

    private static synchronized FontConfig.Font readFont(XmlPullParser parser) throws XmlPullParserException, IOException {
        String indexStr = parser.getAttributeValue(null, "index");
        int index = indexStr == null ? 0 : Integer.parseInt(indexStr);
        List<FontVariationAxis> axes = new ArrayList<>();
        String weightStr = parser.getAttributeValue(null, TvContract.PreviewPrograms.COLUMN_WEIGHT);
        int weight = weightStr == null ? 400 : Integer.parseInt(weightStr);
        boolean isItalic = "italic".equals(parser.getAttributeValue(null, "style"));
        String fallbackFor = parser.getAttributeValue(null, "fallbackFor");
        StringBuilder filename = new StringBuilder();
        while (true) {
            StringBuilder filename2 = filename;
            if (parser.next() != 3) {
                if (parser.getEventType() == 4) {
                    filename2.append(parser.getText());
                }
                if (parser.getEventType() == 2) {
                    String tag = parser.getName();
                    if (tag.equals("axis")) {
                        axes.add(readAxis(parser));
                    } else {
                        skip(parser);
                    }
                }
                filename = filename2;
            } else {
                String sanitizedName = FILENAME_WHITESPACE_PATTERN.matcher(filename2).replaceAll("");
                return new FontConfig.Font(sanitizedName, index, (FontVariationAxis[]) axes.toArray(new FontVariationAxis[axes.size()]), weight, isItalic, fallbackFor);
            }
        }
    }

    private static synchronized FontVariationAxis readAxis(XmlPullParser parser) throws XmlPullParserException, IOException {
        String tagStr = parser.getAttributeValue(null, DropBoxManager.EXTRA_TAG);
        String styleValueStr = parser.getAttributeValue(null, "stylevalue");
        skip(parser);
        return new FontVariationAxis(tagStr, Float.parseFloat(styleValueStr));
    }

    private static synchronized FontConfig.Alias readAlias(XmlPullParser parser) throws XmlPullParserException, IOException {
        int weight;
        String name = parser.getAttributeValue(null, "name");
        String toName = parser.getAttributeValue(null, "to");
        String weightStr = parser.getAttributeValue(null, TvContract.PreviewPrograms.COLUMN_WEIGHT);
        if (weightStr == null) {
            weight = 400;
        } else {
            weight = Integer.parseInt(weightStr);
        }
        skip(parser);
        return new FontConfig.Alias(name, toName, weight);
    }

    private static synchronized void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        int depth = 1;
        while (depth > 0) {
            switch (parser.next()) {
                case 2:
                    depth++;
                    break;
                case 3:
                    depth--;
                    break;
            }
        }
    }
}
