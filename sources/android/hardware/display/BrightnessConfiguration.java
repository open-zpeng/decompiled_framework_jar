package android.hardware.display;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;
import com.android.internal.util.Preconditions;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

@SystemApi
/* loaded from: classes.dex */
public final class BrightnessConfiguration implements Parcelable {
    private static final String ATTR_CATEGORY = "category";
    private static final String ATTR_DESCRIPTION = "description";
    private static final String ATTR_LUX = "lux";
    private static final String ATTR_NITS = "nits";
    private static final String ATTR_PACKAGE_NAME = "package-name";
    public static final Parcelable.Creator<BrightnessConfiguration> CREATOR = new Parcelable.Creator<BrightnessConfiguration>() { // from class: android.hardware.display.BrightnessConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BrightnessConfiguration createFromParcel(Parcel in) {
            float[] lux = in.createFloatArray();
            float[] nits = in.createFloatArray();
            Builder builder = new Builder(lux, nits);
            int n = in.readInt();
            for (int i = 0; i < n; i++) {
                String packageName = in.readString();
                BrightnessCorrection correction = BrightnessCorrection.CREATOR.createFromParcel(in);
                builder.addCorrectionByPackageName(packageName, correction);
            }
            int n2 = in.readInt();
            for (int i2 = 0; i2 < n2; i2++) {
                int category = in.readInt();
                BrightnessCorrection correction2 = BrightnessCorrection.CREATOR.createFromParcel(in);
                builder.addCorrectionByCategory(category, correction2);
            }
            String description = in.readString();
            builder.setDescription(description);
            return builder.build();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BrightnessConfiguration[] newArray(int size) {
            return new BrightnessConfiguration[size];
        }
    };
    private static final String TAG_BRIGHTNESS_CORRECTION = "brightness-correction";
    private static final String TAG_BRIGHTNESS_CORRECTIONS = "brightness-corrections";
    private static final String TAG_BRIGHTNESS_CURVE = "brightness-curve";
    private static final String TAG_BRIGHTNESS_POINT = "brightness-point";
    private final Map<Integer, BrightnessCorrection> mCorrectionsByCategory;
    private final Map<String, BrightnessCorrection> mCorrectionsByPackageName;
    private final String mDescription;
    private final float[] mLux;
    private final float[] mNits;

    private BrightnessConfiguration(float[] lux, float[] nits, Map<String, BrightnessCorrection> correctionsByPackageName, Map<Integer, BrightnessCorrection> correctionsByCategory, String description) {
        this.mLux = lux;
        this.mNits = nits;
        this.mCorrectionsByPackageName = correctionsByPackageName;
        this.mCorrectionsByCategory = correctionsByCategory;
        this.mDescription = description;
    }

    public Pair<float[], float[]> getCurve() {
        float[] fArr = this.mLux;
        float[] copyOf = Arrays.copyOf(fArr, fArr.length);
        float[] fArr2 = this.mNits;
        return Pair.create(copyOf, Arrays.copyOf(fArr2, fArr2.length));
    }

    public BrightnessCorrection getCorrectionByPackageName(String packageName) {
        return this.mCorrectionsByPackageName.get(packageName);
    }

    public BrightnessCorrection getCorrectionByCategory(int category) {
        return this.mCorrectionsByCategory.get(Integer.valueOf(category));
    }

    public String getDescription() {
        return this.mDescription;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloatArray(this.mLux);
        dest.writeFloatArray(this.mNits);
        dest.writeInt(this.mCorrectionsByPackageName.size());
        for (Map.Entry<String, BrightnessCorrection> entry : this.mCorrectionsByPackageName.entrySet()) {
            String packageName = entry.getKey();
            BrightnessCorrection correction = entry.getValue();
            dest.writeString(packageName);
            correction.writeToParcel(dest, flags);
        }
        dest.writeInt(this.mCorrectionsByCategory.size());
        for (Map.Entry<Integer, BrightnessCorrection> entry2 : this.mCorrectionsByCategory.entrySet()) {
            int category = entry2.getKey().intValue();
            BrightnessCorrection correction2 = entry2.getValue();
            dest.writeInt(category);
            correction2.writeToParcel(dest, flags);
        }
        dest.writeString(this.mDescription);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("BrightnessConfiguration{[");
        int size = this.mLux.length;
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("(");
            sb.append(this.mLux[i]);
            sb.append(", ");
            sb.append(this.mNits[i]);
            sb.append(")");
        }
        sb.append("], {");
        for (Map.Entry<String, BrightnessCorrection> entry : this.mCorrectionsByPackageName.entrySet()) {
            sb.append("'" + entry.getKey() + "': " + entry.getValue() + ", ");
        }
        for (Map.Entry<Integer, BrightnessCorrection> entry2 : this.mCorrectionsByCategory.entrySet()) {
            sb.append(entry2.getKey() + ": " + entry2.getValue() + ", ");
        }
        sb.append("}, '");
        String str = this.mDescription;
        if (str != null) {
            sb.append(str);
        }
        sb.append("'}");
        return sb.toString();
    }

    public int hashCode() {
        int result = (((((((1 * 31) + Arrays.hashCode(this.mLux)) * 31) + Arrays.hashCode(this.mNits)) * 31) + this.mCorrectionsByPackageName.hashCode()) * 31) + this.mCorrectionsByCategory.hashCode();
        String str = this.mDescription;
        if (str != null) {
            return (result * 31) + str.hashCode();
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof BrightnessConfiguration) {
            BrightnessConfiguration other = (BrightnessConfiguration) o;
            return Arrays.equals(this.mLux, other.mLux) && Arrays.equals(this.mNits, other.mNits) && this.mCorrectionsByPackageName.equals(other.mCorrectionsByPackageName) && this.mCorrectionsByCategory.equals(other.mCorrectionsByCategory) && Objects.equals(this.mDescription, other.mDescription);
        }
        return false;
    }

    public void saveToXml(XmlSerializer serializer) throws IOException {
        serializer.startTag(null, TAG_BRIGHTNESS_CURVE);
        String str = this.mDescription;
        if (str != null) {
            serializer.attribute(null, "description", str);
        }
        for (int i = 0; i < this.mLux.length; i++) {
            serializer.startTag(null, TAG_BRIGHTNESS_POINT);
            serializer.attribute(null, ATTR_LUX, Float.toString(this.mLux[i]));
            serializer.attribute(null, ATTR_NITS, Float.toString(this.mNits[i]));
            serializer.endTag(null, TAG_BRIGHTNESS_POINT);
        }
        serializer.endTag(null, TAG_BRIGHTNESS_CURVE);
        serializer.startTag(null, TAG_BRIGHTNESS_CORRECTIONS);
        for (Map.Entry<String, BrightnessCorrection> entry : this.mCorrectionsByPackageName.entrySet()) {
            String packageName = entry.getKey();
            BrightnessCorrection correction = entry.getValue();
            serializer.startTag(null, TAG_BRIGHTNESS_CORRECTION);
            serializer.attribute(null, ATTR_PACKAGE_NAME, packageName);
            correction.saveToXml(serializer);
            serializer.endTag(null, TAG_BRIGHTNESS_CORRECTION);
        }
        for (Map.Entry<Integer, BrightnessCorrection> entry2 : this.mCorrectionsByCategory.entrySet()) {
            int category = entry2.getKey().intValue();
            BrightnessCorrection correction2 = entry2.getValue();
            serializer.startTag(null, TAG_BRIGHTNESS_CORRECTION);
            serializer.attribute(null, "category", Integer.toString(category));
            correction2.saveToXml(serializer);
            serializer.endTag(null, TAG_BRIGHTNESS_CORRECTION);
        }
        serializer.endTag(null, TAG_BRIGHTNESS_CORRECTIONS);
    }

    public static BrightnessConfiguration loadFromXml(XmlPullParser parser) throws IOException, XmlPullParserException {
        String description = null;
        List<Float> luxList = new ArrayList<>();
        List<Float> nitsList = new ArrayList<>();
        Map<String, BrightnessCorrection> correctionsByPackageName = new HashMap<>();
        Map<Integer, BrightnessCorrection> correctionsByCategory = new HashMap<>();
        int configDepth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, configDepth)) {
            if (TAG_BRIGHTNESS_CURVE.equals(parser.getName())) {
                description = parser.getAttributeValue(null, "description");
                int curveDepth = parser.getDepth();
                while (XmlUtils.nextElementWithin(parser, curveDepth)) {
                    if (TAG_BRIGHTNESS_POINT.equals(parser.getName())) {
                        float lux = loadFloatFromXml(parser, ATTR_LUX);
                        float nits = loadFloatFromXml(parser, ATTR_NITS);
                        luxList.add(Float.valueOf(lux));
                        nitsList.add(Float.valueOf(nits));
                    }
                }
            }
            if (TAG_BRIGHTNESS_CORRECTIONS.equals(parser.getName())) {
                int correctionsDepth = parser.getDepth();
                while (XmlUtils.nextElementWithin(parser, correctionsDepth)) {
                    if (TAG_BRIGHTNESS_CORRECTION.equals(parser.getName())) {
                        String packageName = parser.getAttributeValue(null, ATTR_PACKAGE_NAME);
                        String categoryText = parser.getAttributeValue(null, "category");
                        BrightnessCorrection correction = BrightnessCorrection.loadFromXml(parser);
                        if (packageName != null) {
                            correctionsByPackageName.put(packageName, correction);
                        } else if (categoryText != null) {
                            try {
                                int category = Integer.parseInt(categoryText);
                                correctionsByCategory.put(Integer.valueOf(category), correction);
                            } catch (NullPointerException | NumberFormatException e) {
                            }
                        }
                    }
                }
            }
        }
        int n = luxList.size();
        float[] lux2 = new float[n];
        float[] nits2 = new float[n];
        for (int i = 0; i < n; i++) {
            lux2[i] = luxList.get(i).floatValue();
            nits2[i] = nitsList.get(i).floatValue();
        }
        Builder builder = new Builder(lux2, nits2);
        builder.setDescription(description);
        for (Map.Entry<String, BrightnessCorrection> entry : correctionsByPackageName.entrySet()) {
            BrightnessCorrection correction2 = entry.getValue();
            builder.addCorrectionByPackageName(entry.getKey(), correction2);
        }
        for (Map.Entry<Integer, BrightnessCorrection> entry2 : correctionsByCategory.entrySet()) {
            int category2 = entry2.getKey().intValue();
            BrightnessCorrection correction3 = entry2.getValue();
            builder.addCorrectionByCategory(category2, correction3);
        }
        return builder.build();
    }

    private static float loadFloatFromXml(XmlPullParser parser, String attribute) {
        String string = parser.getAttributeValue(null, attribute);
        try {
            return Float.parseFloat(string);
        } catch (NullPointerException | NumberFormatException e) {
            return Float.NaN;
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private static final int MAX_CORRECTIONS_BY_CATEGORY = 20;
        private static final int MAX_CORRECTIONS_BY_PACKAGE_NAME = 20;
        private Map<Integer, BrightnessCorrection> mCorrectionsByCategory;
        private Map<String, BrightnessCorrection> mCorrectionsByPackageName;
        private float[] mCurveLux;
        private float[] mCurveNits;
        private String mDescription;

        public Builder(float[] lux, float[] nits) {
            Preconditions.checkNotNull(lux);
            Preconditions.checkNotNull(nits);
            if (lux.length == 0 || nits.length == 0) {
                throw new IllegalArgumentException("Lux and nits arrays must not be empty");
            }
            if (lux.length != nits.length) {
                throw new IllegalArgumentException("Lux and nits arrays must be the same length");
            }
            if (lux[0] != 0.0f) {
                throw new IllegalArgumentException("Initial control point must be for 0 lux");
            }
            Preconditions.checkArrayElementsInRange(lux, 0.0f, Float.MAX_VALUE, BrightnessConfiguration.ATTR_LUX);
            Preconditions.checkArrayElementsInRange(nits, 0.0f, Float.MAX_VALUE, BrightnessConfiguration.ATTR_NITS);
            checkMonotonic(lux, true, BrightnessConfiguration.ATTR_LUX);
            checkMonotonic(nits, false, BrightnessConfiguration.ATTR_NITS);
            this.mCurveLux = lux;
            this.mCurveNits = nits;
            this.mCorrectionsByPackageName = new HashMap();
            this.mCorrectionsByCategory = new HashMap();
        }

        public int getMaxCorrectionsByPackageName() {
            return 20;
        }

        public int getMaxCorrectionsByCategory() {
            return 20;
        }

        public Builder addCorrectionByPackageName(String packageName, BrightnessCorrection correction) {
            Objects.requireNonNull(packageName, "packageName must not be null");
            Objects.requireNonNull(correction, "correction must not be null");
            if (this.mCorrectionsByPackageName.size() >= getMaxCorrectionsByPackageName()) {
                throw new IllegalArgumentException("Too many corrections by package name");
            }
            this.mCorrectionsByPackageName.put(packageName, correction);
            return this;
        }

        public Builder addCorrectionByCategory(int category, BrightnessCorrection correction) {
            Objects.requireNonNull(correction, "correction must not be null");
            if (this.mCorrectionsByCategory.size() >= getMaxCorrectionsByCategory()) {
                throw new IllegalArgumentException("Too many corrections by category");
            }
            this.mCorrectionsByCategory.put(Integer.valueOf(category), correction);
            return this;
        }

        public Builder setDescription(String description) {
            this.mDescription = description;
            return this;
        }

        public BrightnessConfiguration build() {
            float[] fArr;
            float[] fArr2 = this.mCurveLux;
            if (fArr2 == null || (fArr = this.mCurveNits) == null) {
                throw new IllegalStateException("A curve must be set!");
            }
            return new BrightnessConfiguration(fArr2, fArr, this.mCorrectionsByPackageName, this.mCorrectionsByCategory, this.mDescription);
        }

        private static void checkMonotonic(float[] vals, boolean strictlyIncreasing, String name) {
            if (vals.length <= 1) {
                return;
            }
            float prev = vals[0];
            for (int i = 1; i < vals.length; i++) {
                if (prev > vals[i] || (prev == vals[i] && strictlyIncreasing)) {
                    String condition = strictlyIncreasing ? "strictly increasing" : "monotonic";
                    throw new IllegalArgumentException(name + " values must be " + condition);
                }
                prev = vals[i];
            }
        }
    }
}
