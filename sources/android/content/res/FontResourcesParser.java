package android.content.res;

import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public class FontResourcesParser {
    private static final String TAG = "FontResourcesParser";

    /* loaded from: classes.dex */
    public interface FamilyResourceEntry {
    }

    /* loaded from: classes.dex */
    public static final class ProviderResourceEntry implements FamilyResourceEntry {
        private final List<List<String>> mCerts;
        private final String mProviderAuthority;
        private final String mProviderPackage;
        private final String mQuery;

        public ProviderResourceEntry(String authority, String pkg, String query, List<List<String>> certs) {
            this.mProviderAuthority = authority;
            this.mProviderPackage = pkg;
            this.mQuery = query;
            this.mCerts = certs;
        }

        public String getAuthority() {
            return this.mProviderAuthority;
        }

        public String getPackage() {
            return this.mProviderPackage;
        }

        public String getQuery() {
            return this.mQuery;
        }

        public List<List<String>> getCerts() {
            return this.mCerts;
        }
    }

    /* loaded from: classes.dex */
    public static final class FontFileResourceEntry {
        public static final int ITALIC = 1;
        public static final int RESOLVE_BY_FONT_TABLE = -1;
        public static final int UPRIGHT = 0;
        private final String mFileName;
        private int mItalic;
        private int mResourceId;
        private int mTtcIndex;
        private String mVariationSettings;
        private int mWeight;

        public FontFileResourceEntry(String fileName, int weight, int italic, String variationSettings, int ttcIndex) {
            this.mFileName = fileName;
            this.mWeight = weight;
            this.mItalic = italic;
            this.mVariationSettings = variationSettings;
            this.mTtcIndex = ttcIndex;
        }

        public String getFileName() {
            return this.mFileName;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public int getItalic() {
            return this.mItalic;
        }

        public String getVariationSettings() {
            return this.mVariationSettings;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }
    }

    /* loaded from: classes.dex */
    public static final class FontFamilyFilesResourceEntry implements FamilyResourceEntry {
        private final FontFileResourceEntry[] mEntries;

        public FontFamilyFilesResourceEntry(FontFileResourceEntry[] entries) {
            this.mEntries = entries;
        }

        public FontFileResourceEntry[] getEntries() {
            return this.mEntries;
        }
    }

    public static FamilyResourceEntry parse(XmlPullParser parser, Resources resources) throws XmlPullParserException, IOException {
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
        return readFamilies(parser, resources);
    }

    private static FamilyResourceEntry readFamilies(XmlPullParser parser, Resources resources) throws XmlPullParserException, IOException {
        parser.require(2, null, "font-family");
        String tag = parser.getName();
        if (tag.equals("font-family")) {
            return readFamily(parser, resources);
        }
        skip(parser);
        Log.e(TAG, "Failed to find font-family tag");
        return null;
    }

    private static FamilyResourceEntry readFamily(XmlPullParser parser, Resources resources) throws XmlPullParserException, IOException {
        AttributeSet attrs = Xml.asAttributeSet(parser);
        TypedArray array = resources.obtainAttributes(attrs, R.styleable.FontFamily);
        String authority = array.getString(0);
        String providerPackage = array.getString(2);
        boolean isArrayOfArrays = true;
        String query = array.getString(1);
        int certsId = array.getResourceId(3, 0);
        array.recycle();
        if (authority != null && providerPackage != null && query != null) {
            while (parser.next() != 3) {
                skip(parser);
            }
            List<List<String>> certs = null;
            if (certsId != 0) {
                TypedArray typedArray = resources.obtainTypedArray(certsId);
                if (typedArray.length() > 0) {
                    certs = new ArrayList<>();
                    if (typedArray.getResourceId(0, 0) == 0) {
                        isArrayOfArrays = false;
                    }
                    if (isArrayOfArrays) {
                        for (int i = 0; i < typedArray.length(); i++) {
                            int certId = typedArray.getResourceId(i, 0);
                            String[] certsArray = resources.getStringArray(certId);
                            List<String> certsList = Arrays.asList(certsArray);
                            certs.add(certsList);
                        }
                    } else {
                        String[] certsArray2 = resources.getStringArray(certsId);
                        List<String> certsList2 = Arrays.asList(certsArray2);
                        certs.add(certsList2);
                    }
                }
            }
            return new ProviderResourceEntry(authority, providerPackage, query, certs);
        }
        List<FontFileResourceEntry> fonts = new ArrayList<>();
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                String tag = parser.getName();
                if (tag.equals("font")) {
                    FontFileResourceEntry entry = readFont(parser, resources);
                    if (entry != null) {
                        fonts.add(entry);
                    }
                } else {
                    skip(parser);
                }
            }
        }
        if (fonts.isEmpty()) {
            return null;
        }
        return new FontFamilyFilesResourceEntry((FontFileResourceEntry[]) fonts.toArray(new FontFileResourceEntry[fonts.size()]));
    }

    private static FontFileResourceEntry readFont(XmlPullParser parser, Resources resources) throws XmlPullParserException, IOException {
        AttributeSet attrs = Xml.asAttributeSet(parser);
        TypedArray array = resources.obtainAttributes(attrs, R.styleable.FontFamilyFont);
        int weight = array.getInt(1, -1);
        int italic = array.getInt(2, -1);
        String variationSettings = array.getString(4);
        int ttcIndex = array.getInt(3, 0);
        String filename = array.getString(0);
        array.recycle();
        while (parser.next() != 3) {
            skip(parser);
        }
        if (filename == null) {
            return null;
        }
        return new FontFileResourceEntry(filename, weight, italic, variationSettings, ttcIndex);
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        int depth = 1;
        while (depth > 0) {
            int next = parser.next();
            if (next == 2) {
                depth++;
            } else if (next == 3) {
                depth--;
            }
        }
    }
}
