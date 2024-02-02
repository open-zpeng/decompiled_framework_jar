package android.content.pm;

import android.annotation.SystemApi;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Printer;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.Collator;
import java.util.BitSet;
import java.util.Comparator;
/* loaded from: classes.dex */
public class PackageItemInfo {
    public static final int DUMP_FLAG_ALL = 3;
    public static final int DUMP_FLAG_APPLICATION = 2;
    public static final int DUMP_FLAG_DETAILS = 1;
    private static final int LINE_FEED_CODE_POINT = 10;
    private static final float MAX_LABEL_SIZE_PX = 500.0f;
    private static final int MAX_SAFE_LABEL_LENGTH = 50000;
    private static final int NBSP_CODE_POINT = 160;
    public static final int SAFE_LABEL_FLAG_FIRST_LINE = 4;
    public static final int SAFE_LABEL_FLAG_SINGLE_LINE = 2;
    public static final int SAFE_LABEL_FLAG_TRIM = 1;
    private static volatile boolean sForceSafeLabels = false;
    public int banner;
    public int icon;
    public int labelRes;
    public int logo;
    public Bundle metaData;
    public String name;
    public CharSequence nonLocalizedLabel;
    public String packageName;
    public int showUserIcon;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SafeLabelFlags {
    }

    private protected static void setForceSafeLabels(boolean forceSafeLabels) {
        sForceSafeLabels = forceSafeLabels;
    }

    public PackageItemInfo() {
        this.showUserIcon = UserInfo.NO_PROFILE_GROUP_ID;
    }

    public PackageItemInfo(PackageItemInfo orig) {
        this.name = orig.name;
        if (this.name != null) {
            this.name = this.name.trim();
        }
        this.packageName = orig.packageName;
        this.labelRes = orig.labelRes;
        this.nonLocalizedLabel = orig.nonLocalizedLabel;
        if (this.nonLocalizedLabel != null) {
            this.nonLocalizedLabel = this.nonLocalizedLabel.toString().trim();
        }
        this.icon = orig.icon;
        this.banner = orig.banner;
        this.logo = orig.logo;
        this.metaData = orig.metaData;
        this.showUserIcon = orig.showUserIcon;
    }

    public CharSequence loadLabel(PackageManager pm) {
        if (sForceSafeLabels) {
            return loadSafeLabel(pm);
        }
        return loadUnsafeLabel(pm);
    }

    public synchronized CharSequence loadUnsafeLabel(PackageManager pm) {
        CharSequence label;
        if (this.nonLocalizedLabel != null) {
            return this.nonLocalizedLabel;
        }
        if (this.labelRes != 0 && (label = pm.getText(this.packageName, this.labelRes, getApplicationInfo())) != null) {
            return label.toString().trim();
        }
        if (this.name != null) {
            return this.name;
        }
        return this.packageName;
    }

    @SystemApi
    public CharSequence loadSafeLabel(PackageManager pm) {
        String label = loadUnsafeLabel(pm).toString();
        String labelStr = Html.fromHtml(label).toString();
        int labelLength = Math.min(labelStr.length(), 50000);
        StringBuffer sb = new StringBuffer(labelLength);
        int offset = 0;
        while (offset < labelLength) {
            int codePoint = labelStr.codePointAt(offset);
            int type = Character.getType(codePoint);
            if (type == 13 || type == 15 || type == 14) {
                labelStr.substring(0, offset);
                break;
            }
            int charCount = Character.charCount(codePoint);
            if (type == 12) {
                sb.append(' ');
            } else {
                sb.append(labelStr.charAt(offset));
                if (charCount == 2) {
                    sb.append(labelStr.charAt(offset + 1));
                }
            }
            offset += charCount;
        }
        String labelStr2 = sb.toString().trim();
        if (labelStr2.isEmpty()) {
            return this.packageName;
        }
        TextPaint paint = new TextPaint();
        paint.setTextSize(42.0f);
        return TextUtils.ellipsize(labelStr2, paint, MAX_LABEL_SIZE_PX, TextUtils.TruncateAt.END);
    }

    private static synchronized boolean isNewline(int codePoint) {
        int type = Character.getType(codePoint);
        return type == 14 || type == 13 || codePoint == 10;
    }

    private static synchronized boolean isWhiteSpace(int codePoint) {
        return Character.isWhitespace(codePoint) || codePoint == 160;
    }

    /* loaded from: classes.dex */
    private static class StringWithRemovedChars {
        private final String mOriginal;
        private BitSet mRemovedChars;

        synchronized StringWithRemovedChars(String original) {
            this.mOriginal = original;
        }

        synchronized void removeRange(int firstRemoved, int firstNonRemoved) {
            if (this.mRemovedChars == null) {
                this.mRemovedChars = new BitSet(this.mOriginal.length());
            }
            this.mRemovedChars.set(firstRemoved, firstNonRemoved);
        }

        synchronized void removeAllCharBefore(int firstNonRemoved) {
            if (this.mRemovedChars == null) {
                this.mRemovedChars = new BitSet(this.mOriginal.length());
            }
            this.mRemovedChars.set(0, firstNonRemoved);
        }

        synchronized void removeAllCharAfter(int firstRemoved) {
            if (this.mRemovedChars == null) {
                this.mRemovedChars = new BitSet(this.mOriginal.length());
            }
            this.mRemovedChars.set(firstRemoved, this.mOriginal.length());
        }

        public String toString() {
            if (this.mRemovedChars == null) {
                return this.mOriginal;
            }
            StringBuilder sb = new StringBuilder(this.mOriginal.length());
            for (int i = 0; i < this.mOriginal.length(); i++) {
                if (!this.mRemovedChars.get(i)) {
                    sb.append(this.mOriginal.charAt(i));
                }
            }
            return sb.toString();
        }

        synchronized int length() {
            return this.mOriginal.length();
        }

        synchronized boolean isRemoved(int offset) {
            return this.mRemovedChars != null && this.mRemovedChars.get(offset);
        }

        synchronized int codePointAt(int offset) {
            return this.mOriginal.codePointAt(offset);
        }
    }

    public synchronized CharSequence loadSafeLabel(PackageManager pm, float ellipsizeDip, int flags) {
        boolean z = true;
        boolean onlyKeepFirstLine = (flags & 4) != 0;
        boolean forceSingleLine = (flags & 2) != 0;
        boolean trim = (flags & 1) != 0;
        Preconditions.checkNotNull(pm);
        Preconditions.checkArgument(ellipsizeDip >= 0.0f);
        Preconditions.checkFlagsArgument(flags, 7);
        if (onlyKeepFirstLine && forceSingleLine) {
            z = false;
        }
        Preconditions.checkArgument(z, "Cannot set SAFE_LABEL_FLAG_SINGLE_LINE and SAFE_LABEL_FLAG_FIRST_LINE at the same time");
        String label = loadUnsafeLabel(pm).toString();
        StringWithRemovedChars labelStr = new StringWithRemovedChars(Html.fromHtml(label).toString());
        int labelLength = labelStr.length();
        int firstTrailingWhiteSpace = -1;
        int firstTrailingWhiteSpace2 = -1;
        int firstNonWhiteSpace = 0;
        while (firstNonWhiteSpace < labelLength) {
            int codePoint = labelStr.codePointAt(firstNonWhiteSpace);
            int type = Character.getType(codePoint);
            int codePointLen = Character.charCount(codePoint);
            boolean isNewline = isNewline(codePoint);
            if (firstNonWhiteSpace > 50000 || (onlyKeepFirstLine && isNewline)) {
                labelStr.removeAllCharAfter(firstNonWhiteSpace);
                break;
            }
            if (forceSingleLine && isNewline) {
                labelStr.removeRange(firstNonWhiteSpace, firstNonWhiteSpace + codePointLen);
            } else if (type == 15 && !isNewline) {
                labelStr.removeRange(firstNonWhiteSpace, firstNonWhiteSpace + codePointLen);
            } else if (trim && !isWhiteSpace(codePoint)) {
                if (firstTrailingWhiteSpace2 == -1) {
                    firstTrailingWhiteSpace2 = firstNonWhiteSpace;
                }
                int firstTrailingWhiteSpace3 = firstNonWhiteSpace + codePointLen;
                firstTrailingWhiteSpace = firstTrailingWhiteSpace3;
            }
            firstNonWhiteSpace += codePointLen;
        }
        if (trim) {
            if (firstTrailingWhiteSpace2 == -1) {
                labelStr.removeAllCharAfter(0);
            } else {
                if (firstTrailingWhiteSpace2 > 0) {
                    labelStr.removeAllCharBefore(firstTrailingWhiteSpace2);
                }
                if (firstTrailingWhiteSpace < labelLength) {
                    labelStr.removeAllCharAfter(firstTrailingWhiteSpace);
                }
            }
        }
        if (ellipsizeDip == 0.0f) {
            return labelStr.toString();
        }
        TextPaint paint = new TextPaint();
        paint.setTextSize(42.0f);
        return TextUtils.ellipsize(labelStr.toString(), paint, ellipsizeDip, TextUtils.TruncateAt.END);
    }

    public Drawable loadIcon(PackageManager pm) {
        return pm.loadItemIcon(this, getApplicationInfo());
    }

    public Drawable loadUnbadgedIcon(PackageManager pm) {
        return pm.loadUnbadgedItemIcon(this, getApplicationInfo());
    }

    public Drawable loadBanner(PackageManager pm) {
        Drawable dr;
        if (this.banner != 0 && (dr = pm.getDrawable(this.packageName, this.banner, getApplicationInfo())) != null) {
            return dr;
        }
        return loadDefaultBanner(pm);
    }

    public synchronized Drawable loadDefaultIcon(PackageManager pm) {
        return pm.getDefaultActivityIcon();
    }

    protected synchronized Drawable loadDefaultBanner(PackageManager pm) {
        return null;
    }

    public Drawable loadLogo(PackageManager pm) {
        Drawable d;
        if (this.logo != 0 && (d = pm.getDrawable(this.packageName, this.logo, getApplicationInfo())) != null) {
            return d;
        }
        return loadDefaultLogo(pm);
    }

    protected synchronized Drawable loadDefaultLogo(PackageManager pm) {
        return null;
    }

    public XmlResourceParser loadXmlMetaData(PackageManager pm, String name) {
        int resid;
        if (this.metaData != null && (resid = this.metaData.getInt(name)) != 0) {
            return pm.getXml(this.packageName, resid, getApplicationInfo());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dumpFront(Printer pw, String prefix) {
        if (this.name != null) {
            pw.println(prefix + "name=" + this.name);
        }
        pw.println(prefix + "packageName=" + this.packageName);
        if (this.labelRes != 0 || this.nonLocalizedLabel != null || this.icon != 0 || this.banner != 0) {
            pw.println(prefix + "labelRes=0x" + Integer.toHexString(this.labelRes) + " nonLocalizedLabel=" + ((Object) this.nonLocalizedLabel) + " icon=0x" + Integer.toHexString(this.icon) + " banner=0x" + Integer.toHexString(this.banner));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dumpBack(Printer pw, String prefix) {
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeString(this.name);
        dest.writeString(this.packageName);
        dest.writeInt(this.labelRes);
        TextUtils.writeToParcel(this.nonLocalizedLabel, dest, parcelableFlags);
        dest.writeInt(this.icon);
        dest.writeInt(this.logo);
        dest.writeBundle(this.metaData);
        dest.writeInt(this.banner);
        dest.writeInt(this.showUserIcon);
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        if (this.name != null) {
            proto.write(1138166333441L, this.name);
        }
        proto.write(1138166333442L, this.packageName);
        if (this.labelRes != 0 || this.nonLocalizedLabel != null || this.icon != 0 || this.banner != 0) {
            proto.write(1120986464259L, this.labelRes);
            proto.write(1138166333444L, this.nonLocalizedLabel.toString());
            proto.write(1120986464261L, this.icon);
            proto.write(1120986464262L, this.banner);
        }
        proto.end(token);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PackageItemInfo(Parcel source) {
        this.name = source.readString();
        this.packageName = source.readString();
        this.labelRes = source.readInt();
        this.nonLocalizedLabel = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        this.icon = source.readInt();
        this.logo = source.readInt();
        this.metaData = source.readBundle();
        this.banner = source.readInt();
        this.showUserIcon = source.readInt();
    }

    protected synchronized ApplicationInfo getApplicationInfo() {
        return null;
    }

    /* loaded from: classes.dex */
    public static class DisplayNameComparator implements Comparator<PackageItemInfo> {
        private PackageManager mPM;
        private final Collator sCollator = Collator.getInstance();

        public DisplayNameComparator(PackageManager pm) {
            this.mPM = pm;
        }

        @Override // java.util.Comparator
        public final int compare(PackageItemInfo aa, PackageItemInfo ab) {
            CharSequence sa = aa.loadLabel(this.mPM);
            if (sa == null) {
                sa = aa.name;
            }
            CharSequence sb = ab.loadLabel(this.mPM);
            if (sb == null) {
                sb = ab.name;
            }
            return this.sCollator.compare(sa.toString(), sb.toString());
        }
    }
}
