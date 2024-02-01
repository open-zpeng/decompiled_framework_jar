package android.content.res;

import android.annotation.UnsupportedAppUsage;
import android.app.WindowConfiguration;
import android.app.slice.Slice;
import android.content.ConfigurationProto;
import android.content.ResourcesConfigurationProto;
import android.hardware.Camera;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.proto.ProtoOutputStream;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: classes.dex */
public final class Configuration implements Parcelable, Comparable<Configuration> {
    public static final int ASSETS_SEQ_UNDEFINED = 0;
    public static final int COLOR_MODE_HDR_MASK = 12;
    public static final int COLOR_MODE_HDR_NO = 4;
    public static final int COLOR_MODE_HDR_SHIFT = 2;
    public static final int COLOR_MODE_HDR_UNDEFINED = 0;
    public static final int COLOR_MODE_HDR_YES = 8;
    public static final int COLOR_MODE_UNDEFINED = 0;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_MASK = 3;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_NO = 1;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_UNDEFINED = 0;
    public static final int COLOR_MODE_WIDE_COLOR_GAMUT_YES = 2;
    public static final int DENSITY_DPI_ANY = 65534;
    public static final int DENSITY_DPI_NONE = 65535;
    public static final int DENSITY_DPI_UNDEFINED = 0;
    public static final int HARDKEYBOARDHIDDEN_NO = 1;
    public static final int HARDKEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int HARDKEYBOARDHIDDEN_YES = 2;
    public static final int KEYBOARDHIDDEN_NO = 1;
    public static final int KEYBOARDHIDDEN_SOFT = 3;
    public static final int KEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int KEYBOARDHIDDEN_YES = 2;
    public static final int KEYBOARD_12KEY = 3;
    public static final int KEYBOARD_NOKEYS = 1;
    public static final int KEYBOARD_QWERTY = 2;
    public static final int KEYBOARD_UNDEFINED = 0;
    public static final int MNC_ZERO = 65535;
    public static final int NATIVE_CONFIG_COLOR_MODE = 65536;
    public static final int NATIVE_CONFIG_DENSITY = 256;
    public static final int NATIVE_CONFIG_KEYBOARD = 16;
    public static final int NATIVE_CONFIG_KEYBOARD_HIDDEN = 32;
    public static final int NATIVE_CONFIG_LAYOUTDIR = 16384;
    public static final int NATIVE_CONFIG_LOCALE = 4;
    public static final int NATIVE_CONFIG_MCC = 1;
    public static final int NATIVE_CONFIG_MNC = 2;
    public static final int NATIVE_CONFIG_NAVIGATION = 64;
    public static final int NATIVE_CONFIG_ORIENTATION = 128;
    public static final int NATIVE_CONFIG_SCREEN_LAYOUT = 2048;
    public static final int NATIVE_CONFIG_SCREEN_SIZE = 512;
    public static final int NATIVE_CONFIG_SMALLEST_SCREEN_SIZE = 8192;
    public static final int NATIVE_CONFIG_TOUCHSCREEN = 8;
    public static final int NATIVE_CONFIG_UI_MODE = 4096;
    public static final int NATIVE_CONFIG_VERSION = 1024;
    public static final int NAVIGATIONHIDDEN_NO = 1;
    public static final int NAVIGATIONHIDDEN_UNDEFINED = 0;
    public static final int NAVIGATIONHIDDEN_YES = 2;
    public static final int NAVIGATION_DPAD = 2;
    public static final int NAVIGATION_NONAV = 1;
    public static final int NAVIGATION_TRACKBALL = 3;
    public static final int NAVIGATION_UNDEFINED = 0;
    public static final int NAVIGATION_WHEEL = 4;
    public static final int ORIENTATION_LANDSCAPE = 2;
    public static final int ORIENTATION_PORTRAIT = 1;
    @Deprecated
    public static final int ORIENTATION_SQUARE = 3;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int SCREENLAYOUT_COMPAT_NEEDED = 268435456;
    public static final int SCREENLAYOUT_LAYOUTDIR_LTR = 64;
    public static final int SCREENLAYOUT_LAYOUTDIR_MASK = 192;
    public static final int SCREENLAYOUT_LAYOUTDIR_RTL = 128;
    public static final int SCREENLAYOUT_LAYOUTDIR_SHIFT = 6;
    public static final int SCREENLAYOUT_LAYOUTDIR_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LONG_MASK = 48;
    public static final int SCREENLAYOUT_LONG_NO = 16;
    public static final int SCREENLAYOUT_LONG_UNDEFINED = 0;
    public static final int SCREENLAYOUT_LONG_YES = 32;
    public static final int SCREENLAYOUT_ROUND_MASK = 768;
    public static final int SCREENLAYOUT_ROUND_NO = 256;
    public static final int SCREENLAYOUT_ROUND_SHIFT = 8;
    public static final int SCREENLAYOUT_ROUND_UNDEFINED = 0;
    public static final int SCREENLAYOUT_ROUND_YES = 512;
    public static final int SCREENLAYOUT_SIZE_LARGE = 3;
    public static final int SCREENLAYOUT_SIZE_MASK = 15;
    public static final int SCREENLAYOUT_SIZE_NORMAL = 2;
    public static final int SCREENLAYOUT_SIZE_SMALL = 1;
    public static final int SCREENLAYOUT_SIZE_UNDEFINED = 0;
    public static final int SCREENLAYOUT_SIZE_XLARGE = 4;
    public static final int SCREENLAYOUT_UNDEFINED = 0;
    public static final int SCREEN_HEIGHT_DP_UNDEFINED = 0;
    public static final int SCREEN_WIDTH_DP_UNDEFINED = 0;
    public static final int SMALLEST_SCREEN_WIDTH_DP_UNDEFINED = 0;
    private static final String TAG = "Configuration";
    public static final int TOUCHSCREEN_FINGER = 3;
    public static final int TOUCHSCREEN_NOTOUCH = 1;
    @Deprecated
    public static final int TOUCHSCREEN_STYLUS = 2;
    public static final int TOUCHSCREEN_UNDEFINED = 0;
    public static final int UI_MODE_NIGHT_MASK = 48;
    public static final int UI_MODE_NIGHT_NO = 16;
    public static final int UI_MODE_NIGHT_UNDEFINED = 0;
    public static final int UI_MODE_NIGHT_YES = 32;
    public static final int UI_MODE_THEME_CLEAR = 63;
    public static final int UI_MODE_THEME_MASK = 192;
    public static final int UI_MODE_THEME_UNDEFINED = 0;
    public static final int UI_MODE_THEME_VALUEA = 64;
    public static final int UI_MODE_THEME_VALUEB = 128;
    public static final int UI_MODE_TYPE_APPLIANCE = 5;
    public static final int UI_MODE_TYPE_CAR = 3;
    public static final int UI_MODE_TYPE_DESK = 2;
    public static final int UI_MODE_TYPE_MASK = 15;
    public static final int UI_MODE_TYPE_NORMAL = 1;
    public static final int UI_MODE_TYPE_TELEVISION = 4;
    public static final int UI_MODE_TYPE_UNDEFINED = 0;
    public static final int UI_MODE_TYPE_VR_HEADSET = 7;
    public static final int UI_MODE_TYPE_WATCH = 6;
    private static final String XML_ATTR_APP_BOUNDS = "app_bounds";
    private static final String XML_ATTR_COLOR_MODE = "clrMod";
    private static final String XML_ATTR_DENSITY = "density";
    private static final String XML_ATTR_FONT_SCALE = "fs";
    private static final String XML_ATTR_HARD_KEYBOARD_HIDDEN = "hardKeyHid";
    private static final String XML_ATTR_KEYBOARD = "key";
    private static final String XML_ATTR_KEYBOARD_HIDDEN = "keyHid";
    private static final String XML_ATTR_LOCALES = "locales";
    private static final String XML_ATTR_MCC = "mcc";
    private static final String XML_ATTR_MNC = "mnc";
    private static final String XML_ATTR_NAVIGATION = "nav";
    private static final String XML_ATTR_NAVIGATION_HIDDEN = "navHid";
    private static final String XML_ATTR_ORIENTATION = "ori";
    private static final String XML_ATTR_ROTATION = "rot";
    private static final String XML_ATTR_SCREEN_HEIGHT = "height";
    private static final String XML_ATTR_SCREEN_LAYOUT = "scrLay";
    private static final String XML_ATTR_SCREEN_WIDTH = "width";
    private static final String XML_ATTR_SMALLEST_WIDTH = "sw";
    private static final String XML_ATTR_TOUCHSCREEN = "touch";
    private static final String XML_ATTR_UI_MODE = "ui";
    public int assetsSeq;
    public int colorMode;
    public int compatScreenHeightDp;
    public int compatScreenWidthDp;
    public int compatSmallestScreenWidthDp;
    public int densityDpi;
    public float fontScale;
    public int hardKeyboardHidden;
    public int keyboard;
    public int keyboardHidden;
    @Deprecated
    public Locale locale;
    private LocaleList mLocaleList;
    public int mcc;
    public int mnc;
    public int navigation;
    public int navigationHidden;
    public int orientation;
    public int screenHeightDp;
    public int screenLayout;
    public int screenWidthDp;
    @UnsupportedAppUsage
    public int seq;
    public int smallestScreenWidthDp;
    public int touchscreen;
    public int uiMode;
    @UnsupportedAppUsage
    public boolean userSetLocale;
    public final WindowConfiguration windowConfiguration;
    public static final Configuration EMPTY = new Configuration();
    public static final Parcelable.Creator<Configuration> CREATOR = new Parcelable.Creator<Configuration>() { // from class: android.content.res.Configuration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Configuration createFromParcel(Parcel source) {
            return new Configuration(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface NativeConfig {
    }

    public static int resetScreenLayout(int curLayout) {
        return ((-268435520) & curLayout) | 36;
    }

    public static int reduceScreenLayout(int curLayout, int longSizeDp, int shortSizeDp) {
        int screenLayoutSize;
        boolean screenLayoutCompatNeeded;
        boolean screenLayoutLong;
        if (longSizeDp < 470) {
            screenLayoutSize = 1;
            screenLayoutLong = false;
            screenLayoutCompatNeeded = false;
        } else {
            if (longSizeDp >= 960 && shortSizeDp >= 720) {
                screenLayoutSize = 4;
            } else if (longSizeDp >= 640 && shortSizeDp >= 480) {
                screenLayoutSize = 3;
            } else {
                screenLayoutSize = 2;
            }
            if (shortSizeDp > 321 || longSizeDp > 570) {
                screenLayoutCompatNeeded = true;
            } else {
                screenLayoutCompatNeeded = false;
            }
            if ((longSizeDp * 3) / 5 >= shortSizeDp - 1) {
                screenLayoutLong = true;
            } else {
                screenLayoutLong = false;
            }
        }
        if (!screenLayoutLong) {
            curLayout = (curLayout & (-49)) | 16;
        }
        if (screenLayoutCompatNeeded) {
            curLayout |= 268435456;
        }
        int curSize = curLayout & 15;
        if (screenLayoutSize < curSize) {
            return (curLayout & (-16)) | screenLayoutSize;
        }
        return curLayout;
    }

    public static String configurationDiffToString(int diff) {
        ArrayList<String> list = new ArrayList<>();
        if ((diff & 1) != 0) {
            list.add("CONFIG_MCC");
        }
        if ((diff & 2) != 0) {
            list.add("CONFIG_MNC");
        }
        if ((diff & 4) != 0) {
            list.add("CONFIG_LOCALE");
        }
        if ((diff & 8) != 0) {
            list.add("CONFIG_TOUCHSCREEN");
        }
        if ((diff & 16) != 0) {
            list.add("CONFIG_KEYBOARD");
        }
        if ((diff & 32) != 0) {
            list.add("CONFIG_KEYBOARD_HIDDEN");
        }
        if ((diff & 64) != 0) {
            list.add("CONFIG_NAVIGATION");
        }
        if ((diff & 128) != 0) {
            list.add("CONFIG_ORIENTATION");
        }
        if ((diff & 256) != 0) {
            list.add("CONFIG_SCREEN_LAYOUT");
        }
        if ((diff & 16384) != 0) {
            list.add("CONFIG_COLOR_MODE");
        }
        if ((diff & 512) != 0) {
            list.add("CONFIG_UI_MODE");
        }
        if ((diff & 1024) != 0) {
            list.add("CONFIG_SCREEN_SIZE");
        }
        if ((diff & 2048) != 0) {
            list.add("CONFIG_SMALLEST_SCREEN_SIZE");
        }
        if ((diff & 8192) != 0) {
            list.add("CONFIG_LAYOUT_DIRECTION");
        }
        if ((1073741824 & diff) != 0) {
            list.add("CONFIG_FONT_SCALE");
        }
        if ((Integer.MIN_VALUE & diff) != 0) {
            list.add("CONFIG_ASSETS_PATHS");
        }
        StringBuilder builder = new StringBuilder("{");
        int n = list.size();
        for (int i = 0; i < n; i++) {
            builder.append(list.get(i));
            if (i != n - 1) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    public boolean isLayoutSizeAtLeast(int size) {
        int cur = this.screenLayout & 15;
        return cur != 0 && cur >= size;
    }

    public Configuration() {
        this.windowConfiguration = new WindowConfiguration();
        unset();
    }

    public Configuration(Configuration o) {
        this.windowConfiguration = new WindowConfiguration();
        setTo(o);
    }

    private void fixUpLocaleList() {
        Locale locale;
        if ((this.locale == null && !this.mLocaleList.isEmpty()) || ((locale = this.locale) != null && !locale.equals(this.mLocaleList.get(0)))) {
            Locale locale2 = this.locale;
            this.mLocaleList = locale2 == null ? LocaleList.getEmptyLocaleList() : new LocaleList(locale2);
        }
    }

    public void setTo(Configuration o) {
        this.fontScale = o.fontScale;
        this.mcc = o.mcc;
        this.mnc = o.mnc;
        Locale locale = o.locale;
        this.locale = locale == null ? null : (Locale) locale.clone();
        o.fixUpLocaleList();
        this.mLocaleList = o.mLocaleList;
        this.userSetLocale = o.userSetLocale;
        this.touchscreen = o.touchscreen;
        this.keyboard = o.keyboard;
        this.keyboardHidden = o.keyboardHidden;
        this.hardKeyboardHidden = o.hardKeyboardHidden;
        this.navigation = o.navigation;
        this.navigationHidden = o.navigationHidden;
        this.orientation = o.orientation;
        this.screenLayout = o.screenLayout;
        this.colorMode = o.colorMode;
        this.uiMode = o.uiMode;
        this.screenWidthDp = o.screenWidthDp;
        this.screenHeightDp = o.screenHeightDp;
        this.smallestScreenWidthDp = o.smallestScreenWidthDp;
        this.densityDpi = o.densityDpi;
        this.compatScreenWidthDp = o.compatScreenWidthDp;
        this.compatScreenHeightDp = o.compatScreenHeightDp;
        this.compatSmallestScreenWidthDp = o.compatSmallestScreenWidthDp;
        this.assetsSeq = o.assetsSeq;
        this.seq = o.seq;
        this.windowConfiguration.setTo(o.windowConfiguration);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("{");
        sb.append(this.fontScale);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        int i = this.mcc;
        if (i != 0) {
            sb.append(i);
            sb.append("mcc");
        } else {
            sb.append("?mcc");
        }
        int i2 = this.mnc;
        if (i2 != 0) {
            sb.append(i2);
            sb.append("mnc");
        } else {
            sb.append("?mnc");
        }
        fixUpLocaleList();
        if (!this.mLocaleList.isEmpty()) {
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(this.mLocaleList);
        } else {
            sb.append(" ?localeList");
        }
        int layoutDir = this.screenLayout & 192;
        if (layoutDir != 0) {
            if (layoutDir == 64) {
                sb.append(" ldltr");
            } else if (layoutDir == 128) {
                sb.append(" ldrtl");
            } else {
                sb.append(" layoutDir=");
                sb.append(layoutDir >> 6);
            }
        } else {
            sb.append(" ?layoutDir");
        }
        if (this.smallestScreenWidthDp != 0) {
            sb.append(" sw");
            sb.append(this.smallestScreenWidthDp);
            sb.append("dp");
        } else {
            sb.append(" ?swdp");
        }
        if (this.screenWidthDp != 0) {
            sb.append(" w");
            sb.append(this.screenWidthDp);
            sb.append("dp");
        } else {
            sb.append(" ?wdp");
        }
        if (this.screenHeightDp != 0) {
            sb.append(" h");
            sb.append(this.screenHeightDp);
            sb.append("dp");
        } else {
            sb.append(" ?hdp");
        }
        if (this.densityDpi != 0) {
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            sb.append(this.densityDpi);
            sb.append("dpi");
        } else {
            sb.append(" ?density");
        }
        int i3 = this.screenLayout & 15;
        if (i3 == 0) {
            sb.append(" ?lsize");
        } else if (i3 == 1) {
            sb.append(" smll");
        } else if (i3 == 2) {
            sb.append(" nrml");
        } else if (i3 == 3) {
            sb.append(" lrg");
        } else if (i3 == 4) {
            sb.append(" xlrg");
        } else {
            sb.append(" layoutSize=");
            sb.append(this.screenLayout & 15);
        }
        int i4 = this.screenLayout & 48;
        if (i4 == 0) {
            sb.append(" ?long");
        } else if (i4 != 16) {
            if (i4 == 32) {
                sb.append(" long");
            } else {
                sb.append(" layoutLong=");
                sb.append(this.screenLayout & 48);
            }
        }
        int i5 = this.colorMode & 12;
        if (i5 == 0) {
            sb.append(" ?ldr");
        } else if (i5 != 4) {
            if (i5 == 8) {
                sb.append(" hdr");
            } else {
                sb.append(" dynamicRange=");
                sb.append(this.colorMode & 12);
            }
        }
        int i6 = this.colorMode & 3;
        if (i6 == 0) {
            sb.append(" ?wideColorGamut");
        } else if (i6 != 1) {
            if (i6 == 2) {
                sb.append(" widecg");
            } else {
                sb.append(" wideColorGamut=");
                sb.append(this.colorMode & 3);
            }
        }
        int i7 = this.orientation;
        if (i7 == 0) {
            sb.append(" ?orien");
        } else if (i7 == 1) {
            sb.append(" port");
        } else if (i7 == 2) {
            sb.append(" land");
        } else {
            sb.append(" orien=");
            sb.append(this.orientation);
        }
        switch (this.uiMode & 15) {
            case 0:
                sb.append(" ?uimode");
                break;
            case 1:
                break;
            case 2:
                sb.append(" desk");
                break;
            case 3:
                sb.append(" car");
                break;
            case 4:
                sb.append(" television");
                break;
            case 5:
                sb.append(" appliance");
                break;
            case 6:
                sb.append(" watch");
                break;
            case 7:
                sb.append(" vrheadset");
                break;
            default:
                sb.append(" uimode=");
                sb.append(this.uiMode & 15);
                break;
        }
        int i8 = this.uiMode & 48;
        if (i8 == 0) {
            sb.append(" ?night");
        } else if (i8 != 16) {
            if (i8 == 32) {
                sb.append(" night");
            } else {
                sb.append(" night=");
                sb.append(this.uiMode & 48);
            }
        }
        int i9 = this.uiMode & 192;
        if (i9 != 0) {
            if (i9 == 64) {
                sb.append(" themeA");
            } else if (i9 == 128) {
                sb.append(" themeB");
            }
        } else {
            sb.append(" ?theme");
        }
        int i10 = this.touchscreen;
        if (i10 == 0) {
            sb.append(" ?touch");
        } else if (i10 == 1) {
            sb.append(" -touch");
        } else if (i10 == 2) {
            sb.append(" stylus");
        } else if (i10 == 3) {
            sb.append(" finger");
        } else {
            sb.append(" touch=");
            sb.append(this.touchscreen);
        }
        int i11 = this.keyboard;
        if (i11 == 0) {
            sb.append(" ?keyb");
        } else if (i11 == 1) {
            sb.append(" -keyb");
        } else if (i11 == 2) {
            sb.append(" qwerty");
        } else if (i11 == 3) {
            sb.append(" 12key");
        } else {
            sb.append(" keys=");
            sb.append(this.keyboard);
        }
        int i12 = this.keyboardHidden;
        if (i12 == 0) {
            sb.append("/?");
        } else if (i12 == 1) {
            sb.append("/v");
        } else if (i12 == 2) {
            sb.append("/h");
        } else if (i12 == 3) {
            sb.append("/s");
        } else {
            sb.append("/");
            sb.append(this.keyboardHidden);
        }
        int i13 = this.hardKeyboardHidden;
        if (i13 == 0) {
            sb.append("/?");
        } else if (i13 == 1) {
            sb.append("/v");
        } else if (i13 == 2) {
            sb.append("/h");
        } else {
            sb.append("/");
            sb.append(this.hardKeyboardHidden);
        }
        int i14 = this.navigation;
        if (i14 == 0) {
            sb.append(" ?nav");
        } else if (i14 == 1) {
            sb.append(" -nav");
        } else if (i14 == 2) {
            sb.append(" dpad");
        } else if (i14 == 3) {
            sb.append(" tball");
        } else if (i14 == 4) {
            sb.append(" wheel");
        } else {
            sb.append(" nav=");
            sb.append(this.navigation);
        }
        int i15 = this.navigationHidden;
        if (i15 == 0) {
            sb.append("/?");
        } else if (i15 == 1) {
            sb.append("/v");
        } else if (i15 == 2) {
            sb.append("/h");
        } else {
            sb.append("/");
            sb.append(this.navigationHidden);
        }
        sb.append(" winConfig=");
        sb.append(this.windowConfiguration);
        if (this.assetsSeq != 0) {
            sb.append(" as.");
            sb.append(this.assetsSeq);
        }
        if (this.seq != 0) {
            sb.append(" s.");
            sb.append(this.seq);
        }
        sb.append('}');
        return sb.toString();
    }

    public void writeToProto(ProtoOutputStream protoOutputStream, long fieldId, boolean persisted, boolean critical) {
        WindowConfiguration windowConfiguration;
        long token = protoOutputStream.start(fieldId);
        if (!critical) {
            protoOutputStream.write(1108101562369L, this.fontScale);
            protoOutputStream.write(1155346202626L, this.mcc);
            protoOutputStream.write(1155346202627L, this.mnc);
            LocaleList localeList = this.mLocaleList;
            if (localeList != null) {
                protoOutputStream.write(1138166333460L, localeList.toLanguageTags());
            }
            protoOutputStream.write(1155346202629L, this.screenLayout);
            protoOutputStream.write(1155346202630L, this.colorMode);
            protoOutputStream.write(ConfigurationProto.TOUCHSCREEN, this.touchscreen);
            protoOutputStream.write(1155346202632L, this.keyboard);
            protoOutputStream.write(ConfigurationProto.KEYBOARD_HIDDEN, this.keyboardHidden);
            protoOutputStream.write(ConfigurationProto.HARD_KEYBOARD_HIDDEN, this.hardKeyboardHidden);
            protoOutputStream.write(ConfigurationProto.NAVIGATION, this.navigation);
            protoOutputStream.write(ConfigurationProto.NAVIGATION_HIDDEN, this.navigationHidden);
            protoOutputStream.write(ConfigurationProto.UI_MODE, this.uiMode);
            protoOutputStream.write(ConfigurationProto.SMALLEST_SCREEN_WIDTH_DP, this.smallestScreenWidthDp);
            protoOutputStream.write(ConfigurationProto.DENSITY_DPI, this.densityDpi);
            if (!persisted && (windowConfiguration = this.windowConfiguration) != null) {
                windowConfiguration.writeToProto(protoOutputStream, 1146756268051L);
            }
        }
        protoOutputStream.write(ConfigurationProto.ORIENTATION, this.orientation);
        protoOutputStream.write(ConfigurationProto.SCREEN_WIDTH_DP, this.screenWidthDp);
        protoOutputStream.write(ConfigurationProto.SCREEN_HEIGHT_DP, this.screenHeightDp);
        protoOutputStream.end(token);
    }

    public void writeToProto(ProtoOutputStream protoOutputStream, long fieldId) {
        writeToProto(protoOutputStream, fieldId, false, false);
    }

    public void writeToProto(ProtoOutputStream protoOutputStream, long fieldId, boolean critical) {
        writeToProto(protoOutputStream, fieldId, false, critical);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(3:(4:115|116|(2:118|(2:120|(2:122|(2:124|125)(2:127|128))(2:129|130))(2:131|132))(3:133|134|135)|126)|65|66) */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x02eb, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x02ec, code lost:
        r1 = r20;
        r13 = r21;
        r9 = r22;
        r21 = r10;
        r19 = r15;
        r15 = r23;
        r2 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x02fa, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x02fb, code lost:
        r1 = r20;
        r13 = r21;
        r9 = r22;
        r21 = r10;
        r19 = r15;
        r15 = r23;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:103:0x033b A[Catch: IllformedLocaleException -> 0x0360, all -> 0x03a9, TRY_ENTER, TryCatch #7 {all -> 0x03a9, blocks: (B:99:0x0312, B:100:0x0315, B:103:0x033b, B:112:0x038c, B:104:0x035c, B:110:0x0365, B:116:0x0394), top: B:151:0x0394 }] */
    /* JADX WARN: Removed duplicated region for block: B:104:0x035c A[Catch: IllformedLocaleException -> 0x0360, all -> 0x03a9, TRY_LEAVE, TryCatch #7 {all -> 0x03a9, blocks: (B:99:0x0312, B:100:0x0315, B:103:0x033b, B:112:0x038c, B:104:0x035c, B:110:0x0365, B:116:0x0394), top: B:151:0x0394 }] */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0417  */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v15, types: [android.util.proto.ProtoInputStream] */
    /* JADX WARN: Type inference failed for: r10v58 */
    /* JADX WARN: Type inference failed for: r10v60 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void readFromProto(android.util.proto.ProtoInputStream r27, long r28) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1116
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.Configuration.readFromProto(android.util.proto.ProtoInputStream, long):void");
    }

    public void writeResConfigToProto(ProtoOutputStream protoOutputStream, long fieldId, DisplayMetrics metrics) {
        int width;
        int height;
        if (metrics.widthPixels >= metrics.heightPixels) {
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        } else {
            width = metrics.heightPixels;
            height = metrics.widthPixels;
        }
        long token = protoOutputStream.start(fieldId);
        writeToProto(protoOutputStream, 1146756268033L);
        protoOutputStream.write(1155346202626L, Build.VERSION.RESOURCES_SDK_INT);
        protoOutputStream.write(1155346202627L, width);
        protoOutputStream.write(ResourcesConfigurationProto.SCREEN_HEIGHT_PX, height);
        protoOutputStream.end(token);
    }

    public static String uiModeToString(int uiMode) {
        switch (uiMode) {
            case 0:
                return "UI_MODE_TYPE_UNDEFINED";
            case 1:
                return "UI_MODE_TYPE_NORMAL";
            case 2:
                return "UI_MODE_TYPE_DESK";
            case 3:
                return "UI_MODE_TYPE_CAR";
            case 4:
                return "UI_MODE_TYPE_TELEVISION";
            case 5:
                return "UI_MODE_TYPE_APPLIANCE";
            case 6:
                return "UI_MODE_TYPE_WATCH";
            case 7:
                return "UI_MODE_TYPE_VR_HEADSET";
            default:
                return Integer.toString(uiMode);
        }
    }

    public void setToDefaults() {
        this.fontScale = 1.0f;
        this.mnc = 0;
        this.mcc = 0;
        this.mLocaleList = LocaleList.getEmptyLocaleList();
        this.locale = null;
        this.userSetLocale = false;
        this.touchscreen = 0;
        this.keyboard = 0;
        this.keyboardHidden = 0;
        this.hardKeyboardHidden = 0;
        this.navigation = 0;
        this.navigationHidden = 0;
        this.orientation = 0;
        this.screenLayout = 0;
        this.colorMode = 0;
        this.uiMode = 0;
        this.compatScreenWidthDp = 0;
        this.screenWidthDp = 0;
        this.compatScreenHeightDp = 0;
        this.screenHeightDp = 0;
        this.compatSmallestScreenWidthDp = 0;
        this.smallestScreenWidthDp = 0;
        this.densityDpi = 0;
        this.assetsSeq = 0;
        this.seq = 0;
        this.windowConfiguration.setToDefaults();
    }

    public void unset() {
        setToDefaults();
        this.fontScale = 0.0f;
    }

    @UnsupportedAppUsage
    @Deprecated
    public void makeDefault() {
        setToDefaults();
    }

    public int updateFrom(Configuration delta) {
        int i;
        int changed = 0;
        float f = delta.fontScale;
        if (f > 0.0f && this.fontScale != f) {
            changed = 0 | 1073741824;
            this.fontScale = f;
        }
        int i2 = delta.mcc;
        if (i2 != 0 && this.mcc != i2) {
            changed |= 1;
            this.mcc = i2;
        }
        int i3 = delta.mnc;
        if (i3 != 0 && this.mnc != i3) {
            changed |= 2;
            this.mnc = i3;
        }
        fixUpLocaleList();
        delta.fixUpLocaleList();
        if (!delta.mLocaleList.isEmpty() && !this.mLocaleList.equals(delta.mLocaleList)) {
            changed |= 4;
            this.mLocaleList = delta.mLocaleList;
            if (!delta.locale.equals(this.locale)) {
                this.locale = (Locale) delta.locale.clone();
                changed |= 8192;
                setLayoutDirection(this.locale);
            }
        }
        int deltaScreenLayoutDir = delta.screenLayout & 192;
        if (deltaScreenLayoutDir != 0) {
            int i4 = this.screenLayout;
            if (deltaScreenLayoutDir != (i4 & 192)) {
                this.screenLayout = (i4 & (-193)) | deltaScreenLayoutDir;
                changed |= 8192;
            }
        }
        if (delta.userSetLocale && (!this.userSetLocale || (changed & 4) != 0)) {
            changed |= 4;
            this.userSetLocale = true;
        }
        int i5 = delta.touchscreen;
        if (i5 != 0 && this.touchscreen != i5) {
            changed |= 8;
            this.touchscreen = i5;
        }
        int i6 = delta.keyboard;
        if (i6 != 0 && this.keyboard != i6) {
            changed |= 16;
            this.keyboard = i6;
        }
        int i7 = delta.keyboardHidden;
        if (i7 != 0 && this.keyboardHidden != i7) {
            changed |= 32;
            this.keyboardHidden = i7;
        }
        int i8 = delta.hardKeyboardHidden;
        if (i8 != 0 && this.hardKeyboardHidden != i8) {
            changed |= 32;
            this.hardKeyboardHidden = i8;
        }
        int i9 = delta.navigation;
        if (i9 != 0 && this.navigation != i9) {
            changed |= 64;
            this.navigation = i9;
        }
        int i10 = delta.navigationHidden;
        if (i10 != 0 && this.navigationHidden != i10) {
            changed |= 32;
            this.navigationHidden = i10;
        }
        int i11 = delta.orientation;
        if (i11 != 0 && this.orientation != i11) {
            changed |= 128;
            this.orientation = i11;
        }
        int i12 = delta.screenLayout;
        if ((i12 & 15) != 0) {
            int i13 = i12 & 15;
            int i14 = this.screenLayout;
            if (i13 != (i14 & 15)) {
                changed |= 256;
                this.screenLayout = (i12 & 15) | (i14 & (-16));
            }
        }
        int i15 = delta.screenLayout;
        if ((i15 & 48) != 0) {
            int i16 = i15 & 48;
            int i17 = this.screenLayout;
            if (i16 != (i17 & 48)) {
                changed |= 256;
                this.screenLayout = (i15 & 48) | (i17 & (-49));
            }
        }
        int i18 = delta.screenLayout;
        if ((i18 & 768) != 0) {
            int i19 = i18 & 768;
            int i20 = this.screenLayout;
            if (i19 != (i20 & 768)) {
                changed |= 256;
                this.screenLayout = (i18 & 768) | (i20 & (-769));
            }
        }
        int i21 = delta.screenLayout;
        int i22 = i21 & 268435456;
        int i23 = this.screenLayout;
        if (i22 != (i23 & 268435456) && i21 != 0) {
            changed |= 256;
            this.screenLayout = (i21 & 268435456) | ((-268435457) & i23);
        }
        int i24 = delta.colorMode;
        if ((i24 & 3) != 0) {
            int i25 = i24 & 3;
            int i26 = this.colorMode;
            if (i25 != (i26 & 3)) {
                changed |= 16384;
                this.colorMode = (i24 & 3) | (i26 & (-4));
            }
        }
        int i27 = delta.colorMode;
        if ((i27 & 12) != 0) {
            int i28 = i27 & 12;
            int i29 = this.colorMode;
            if (i28 != (i29 & 12)) {
                changed |= 16384;
                this.colorMode = (i27 & 12) | (i29 & (-13));
            }
        }
        int i30 = delta.uiMode;
        if (i30 >= 0 && i30 != 0 && (i = this.uiMode) != i30) {
            changed |= 512;
            if ((i30 & 15) != 0) {
                this.uiMode = (i30 & 15) | (i & (-16));
            }
            int i31 = delta.uiMode;
            if ((i31 & 48) != 0) {
                this.uiMode = (i31 & 48) | (this.uiMode & (-49));
            }
            int i32 = delta.uiMode;
            if ((i32 & 192) != 0) {
                this.uiMode = (i32 & 192) | (this.uiMode & (-193));
            }
        }
        int i33 = delta.screenWidthDp;
        if (i33 != 0 && this.screenWidthDp != i33) {
            changed |= 1024;
            this.screenWidthDp = i33;
        }
        int i34 = delta.screenHeightDp;
        if (i34 != 0 && this.screenHeightDp != i34) {
            changed |= 1024;
            this.screenHeightDp = i34;
        }
        int i35 = delta.smallestScreenWidthDp;
        if (i35 != 0 && this.smallestScreenWidthDp != i35) {
            changed |= 2048;
            this.smallestScreenWidthDp = i35;
        }
        int i36 = delta.densityDpi;
        if (i36 != 0 && this.densityDpi != i36) {
            changed |= 4096;
            this.densityDpi = i36;
        }
        int i37 = delta.compatScreenWidthDp;
        if (i37 != 0) {
            this.compatScreenWidthDp = i37;
        }
        int i38 = delta.compatScreenHeightDp;
        if (i38 != 0) {
            this.compatScreenHeightDp = i38;
        }
        int i39 = delta.compatSmallestScreenWidthDp;
        if (i39 != 0) {
            this.compatSmallestScreenWidthDp = i39;
        }
        int i40 = delta.assetsSeq;
        if (i40 != 0 && i40 != this.assetsSeq) {
            changed |= Integer.MIN_VALUE;
            this.assetsSeq = i40;
        }
        int i41 = delta.seq;
        if (i41 != 0) {
            this.seq = i41;
        }
        if (this.windowConfiguration.updateFrom(delta.windowConfiguration) != 0) {
            return changed | 536870912;
        }
        return changed;
    }

    public int diff(Configuration delta) {
        return diff(delta, false, false);
    }

    public int diffPublicOnly(Configuration delta) {
        return diff(delta, false, true);
    }

    public int diff(Configuration delta, boolean compareUndefined, boolean publicOnly) {
        int changed = 0;
        if ((compareUndefined || delta.fontScale > 0.0f) && this.fontScale != delta.fontScale) {
            changed = 0 | 1073741824;
        }
        if ((compareUndefined || delta.mcc != 0) && this.mcc != delta.mcc) {
            changed |= 1;
        }
        if ((compareUndefined || delta.mnc != 0) && this.mnc != delta.mnc) {
            changed |= 2;
        }
        fixUpLocaleList();
        delta.fixUpLocaleList();
        if ((compareUndefined || !delta.mLocaleList.isEmpty()) && !this.mLocaleList.equals(delta.mLocaleList)) {
            changed = changed | 4 | 8192;
        }
        int deltaScreenLayoutDir = delta.screenLayout & 192;
        if ((compareUndefined || deltaScreenLayoutDir != 0) && deltaScreenLayoutDir != (this.screenLayout & 192)) {
            changed |= 8192;
        }
        if ((compareUndefined || delta.touchscreen != 0) && this.touchscreen != delta.touchscreen) {
            changed |= 8;
        }
        if ((compareUndefined || delta.keyboard != 0) && this.keyboard != delta.keyboard) {
            changed |= 16;
        }
        if ((compareUndefined || delta.keyboardHidden != 0) && this.keyboardHidden != delta.keyboardHidden) {
            changed |= 32;
        }
        if ((compareUndefined || delta.hardKeyboardHidden != 0) && this.hardKeyboardHidden != delta.hardKeyboardHidden) {
            changed |= 32;
        }
        if ((compareUndefined || delta.navigation != 0) && this.navigation != delta.navigation) {
            changed |= 64;
        }
        if ((compareUndefined || delta.navigationHidden != 0) && this.navigationHidden != delta.navigationHidden) {
            changed |= 32;
        }
        if ((compareUndefined || delta.orientation != 0) && this.orientation != delta.orientation) {
            changed |= 128;
        }
        if ((compareUndefined || getScreenLayoutNoDirection(delta.screenLayout) != 0) && getScreenLayoutNoDirection(this.screenLayout) != getScreenLayoutNoDirection(delta.screenLayout)) {
            changed |= 256;
        }
        if ((compareUndefined || (delta.colorMode & 12) != 0) && (this.colorMode & 12) != (delta.colorMode & 12)) {
            changed |= 16384;
        }
        if ((compareUndefined || (delta.colorMode & 3) != 0) && (this.colorMode & 3) != (delta.colorMode & 3)) {
            changed |= 16384;
        }
        if ((compareUndefined || delta.uiMode != 0) && this.uiMode != delta.uiMode) {
            changed |= 512;
        }
        if ((compareUndefined || delta.screenWidthDp != 0) && this.screenWidthDp != delta.screenWidthDp) {
            changed |= 1024;
        }
        if ((compareUndefined || delta.screenHeightDp != 0) && this.screenHeightDp != delta.screenHeightDp) {
            changed |= 1024;
        }
        if ((compareUndefined || delta.smallestScreenWidthDp != 0) && this.smallestScreenWidthDp != delta.smallestScreenWidthDp) {
            changed |= 2048;
        }
        if ((compareUndefined || delta.densityDpi != 0) && this.densityDpi != delta.densityDpi) {
            changed |= 4096;
        }
        if ((compareUndefined || delta.assetsSeq != 0) && this.assetsSeq != delta.assetsSeq) {
            changed |= Integer.MIN_VALUE;
        }
        if (!publicOnly && this.windowConfiguration.diff(delta.windowConfiguration, compareUndefined) != 0) {
            return changed | 536870912;
        }
        return changed;
    }

    public static boolean needNewResources(int configChanges, int interestingChanges) {
        return (configChanges & (((Integer.MIN_VALUE | interestingChanges) | 512) | 1073741824)) != 0;
    }

    public boolean isOtherSeqNewer(Configuration other) {
        int i;
        if (other == null) {
            return false;
        }
        int i2 = other.seq;
        if (i2 == 0 || (i = this.seq) == 0) {
            return true;
        }
        int diff = i2 - i;
        return diff <= 65536 && diff > 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.fontScale);
        dest.writeInt(this.mcc);
        dest.writeInt(this.mnc);
        fixUpLocaleList();
        dest.writeParcelable(this.mLocaleList, flags);
        if (this.userSetLocale) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.touchscreen);
        dest.writeInt(this.keyboard);
        dest.writeInt(this.keyboardHidden);
        dest.writeInt(this.hardKeyboardHidden);
        dest.writeInt(this.navigation);
        dest.writeInt(this.navigationHidden);
        dest.writeInt(this.orientation);
        dest.writeInt(this.screenLayout);
        dest.writeInt(this.colorMode);
        dest.writeInt(this.uiMode);
        dest.writeInt(this.screenWidthDp);
        dest.writeInt(this.screenHeightDp);
        dest.writeInt(this.smallestScreenWidthDp);
        dest.writeInt(this.densityDpi);
        dest.writeInt(this.compatScreenWidthDp);
        dest.writeInt(this.compatScreenHeightDp);
        dest.writeInt(this.compatSmallestScreenWidthDp);
        dest.writeValue(this.windowConfiguration);
        dest.writeInt(this.assetsSeq);
        dest.writeInt(this.seq);
    }

    public void readFromParcel(Parcel source) {
        this.fontScale = source.readFloat();
        this.mcc = source.readInt();
        this.mnc = source.readInt();
        this.mLocaleList = (LocaleList) source.readParcelable(LocaleList.class.getClassLoader());
        this.locale = this.mLocaleList.get(0);
        this.userSetLocale = source.readInt() == 1;
        this.touchscreen = source.readInt();
        this.keyboard = source.readInt();
        this.keyboardHidden = source.readInt();
        this.hardKeyboardHidden = source.readInt();
        this.navigation = source.readInt();
        this.navigationHidden = source.readInt();
        this.orientation = source.readInt();
        this.screenLayout = source.readInt();
        this.colorMode = source.readInt();
        this.uiMode = source.readInt();
        this.screenWidthDp = source.readInt();
        this.screenHeightDp = source.readInt();
        this.smallestScreenWidthDp = source.readInt();
        this.densityDpi = source.readInt();
        this.compatScreenWidthDp = source.readInt();
        this.compatScreenHeightDp = source.readInt();
        this.compatSmallestScreenWidthDp = source.readInt();
        this.windowConfiguration.setTo((WindowConfiguration) source.readValue(null));
        this.assetsSeq = source.readInt();
        this.seq = source.readInt();
    }

    private Configuration(Parcel source) {
        this.windowConfiguration = new WindowConfiguration();
        readFromParcel(source);
    }

    @Override // java.lang.Comparable
    public int compareTo(Configuration that) {
        float a = this.fontScale;
        float b = that.fontScale;
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        int n = this.mcc - that.mcc;
        if (n != 0) {
            return n;
        }
        int n2 = this.mnc - that.mnc;
        if (n2 != 0) {
            return n2;
        }
        fixUpLocaleList();
        that.fixUpLocaleList();
        if (this.mLocaleList.isEmpty()) {
            if (!that.mLocaleList.isEmpty()) {
                return 1;
            }
        } else if (that.mLocaleList.isEmpty()) {
            return -1;
        } else {
            int minSize = Math.min(this.mLocaleList.size(), that.mLocaleList.size());
            for (int i = 0; i < minSize; i++) {
                Locale thisLocale = this.mLocaleList.get(i);
                Locale thatLocale = that.mLocaleList.get(i);
                int n3 = thisLocale.getLanguage().compareTo(thatLocale.getLanguage());
                if (n3 != 0) {
                    return n3;
                }
                int n4 = thisLocale.getCountry().compareTo(thatLocale.getCountry());
                if (n4 != 0) {
                    return n4;
                }
                int n5 = thisLocale.getVariant().compareTo(thatLocale.getVariant());
                if (n5 != 0) {
                    return n5;
                }
                int n6 = thisLocale.toLanguageTag().compareTo(thatLocale.toLanguageTag());
                if (n6 != 0) {
                    return n6;
                }
            }
            int n7 = this.mLocaleList.size() - that.mLocaleList.size();
            if (n7 != 0) {
                return n7;
            }
        }
        int minSize2 = this.touchscreen;
        int n8 = minSize2 - that.touchscreen;
        if (n8 != 0) {
            return n8;
        }
        int n9 = this.keyboard - that.keyboard;
        if (n9 != 0) {
            return n9;
        }
        int n10 = this.keyboardHidden - that.keyboardHidden;
        if (n10 != 0) {
            return n10;
        }
        int n11 = this.hardKeyboardHidden - that.hardKeyboardHidden;
        if (n11 != 0) {
            return n11;
        }
        int n12 = this.navigation - that.navigation;
        if (n12 != 0) {
            return n12;
        }
        int n13 = this.navigationHidden - that.navigationHidden;
        if (n13 != 0) {
            return n13;
        }
        int n14 = this.orientation - that.orientation;
        if (n14 != 0) {
            return n14;
        }
        int n15 = this.colorMode - that.colorMode;
        if (n15 != 0) {
            return n15;
        }
        int n16 = this.screenLayout - that.screenLayout;
        if (n16 != 0) {
            return n16;
        }
        int n17 = this.uiMode - that.uiMode;
        if (n17 != 0) {
            return n17;
        }
        int n18 = this.screenWidthDp - that.screenWidthDp;
        if (n18 != 0) {
            return n18;
        }
        int n19 = this.screenHeightDp - that.screenHeightDp;
        if (n19 != 0) {
            return n19;
        }
        int n20 = this.smallestScreenWidthDp - that.smallestScreenWidthDp;
        if (n20 != 0) {
            return n20;
        }
        int n21 = this.densityDpi - that.densityDpi;
        if (n21 != 0) {
            return n21;
        }
        int n22 = this.assetsSeq - that.assetsSeq;
        if (n22 != 0) {
            return n22;
        }
        int n23 = this.windowConfiguration.compareTo(that.windowConfiguration);
        return n23 != 0 ? n23 : n23;
    }

    public boolean equals(Configuration that) {
        if (that == null) {
            return false;
        }
        if (that != this && compareTo(that) != 0) {
            return false;
        }
        return true;
    }

    public boolean equals(Object that) {
        try {
            return equals((Configuration) that);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        int result = (17 * 31) + Float.floatToIntBits(this.fontScale);
        return (((((((((((((((((((((((((((((((((((result * 31) + this.mcc) * 31) + this.mnc) * 31) + this.mLocaleList.hashCode()) * 31) + this.touchscreen) * 31) + this.keyboard) * 31) + this.keyboardHidden) * 31) + this.hardKeyboardHidden) * 31) + this.navigation) * 31) + this.navigationHidden) * 31) + this.orientation) * 31) + this.screenLayout) * 31) + this.colorMode) * 31) + this.uiMode) * 31) + this.screenWidthDp) * 31) + this.screenHeightDp) * 31) + this.smallestScreenWidthDp) * 31) + this.densityDpi) * 31) + this.assetsSeq;
    }

    public LocaleList getLocales() {
        fixUpLocaleList();
        if (this.mLocaleList.isEmpty() && LocaleList.getLastDefaultLocale() != null) {
            setLocale(LocaleList.getLastDefaultLocale());
        }
        return this.mLocaleList;
    }

    public void setLocales(LocaleList locales) {
        this.mLocaleList = locales == null ? LocaleList.getEmptyLocaleList() : locales;
        this.locale = this.mLocaleList.get(0);
        setLayoutDirection(this.locale);
    }

    public void setLocale(Locale loc) {
        setLocales(loc == null ? LocaleList.getEmptyLocaleList() : new LocaleList(loc));
    }

    public void clearLocales() {
        this.mLocaleList = LocaleList.getEmptyLocaleList();
        this.locale = null;
    }

    public int getLayoutDirection() {
        return (this.screenLayout & 192) == 128 ? 1 : 0;
    }

    public void setLayoutDirection(Locale loc) {
        int layoutDirection = TextUtils.getLayoutDirectionFromLocale(loc) + 1;
        this.screenLayout = (this.screenLayout & (-193)) | (layoutDirection << 6);
    }

    private static int getScreenLayoutNoDirection(int screenLayout) {
        return screenLayout & (-193);
    }

    public boolean isScreenRound() {
        return (this.screenLayout & 768) == 512;
    }

    public boolean isScreenWideColorGamut() {
        return (this.colorMode & 3) == 2;
    }

    public boolean isScreenHdr() {
        return (this.colorMode & 12) == 8;
    }

    public static String localesToResourceQualifier(LocaleList locs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < locs.size(); i++) {
            Locale loc = locs.get(i);
            int l = loc.getLanguage().length();
            if (l != 0) {
                int s = loc.getScript().length();
                int c = loc.getCountry().length();
                int v = loc.getVariant().length();
                if (sb.length() != 0) {
                    sb.append(SmsManager.REGEX_PREFIX_DELIMITER);
                }
                if (l == 2 && s == 0 && ((c == 0 || c == 2) && v == 0)) {
                    sb.append(loc.getLanguage());
                    if (c == 2) {
                        sb.append("-r");
                        sb.append(loc.getCountry());
                    }
                } else {
                    sb.append("b+");
                    sb.append(loc.getLanguage());
                    if (s != 0) {
                        sb.append("+");
                        sb.append(loc.getScript());
                    }
                    if (c != 0) {
                        sb.append("+");
                        sb.append(loc.getCountry());
                    }
                    if (v != 0) {
                        sb.append("+");
                        sb.append(loc.getVariant());
                    }
                }
            }
        }
        return sb.toString();
    }

    @UnsupportedAppUsage
    public static String resourceQualifierString(Configuration config) {
        return resourceQualifierString(config, null);
    }

    public static String resourceQualifierString(Configuration config, DisplayMetrics metrics) {
        int width;
        int height;
        ArrayList<String> parts = new ArrayList<>();
        if (config.mcc != 0) {
            parts.add("mcc" + config.mcc);
            if (config.mnc != 0) {
                parts.add("mnc" + config.mnc);
            }
        }
        if (!config.mLocaleList.isEmpty()) {
            String resourceQualifier = localesToResourceQualifier(config.mLocaleList);
            if (!resourceQualifier.isEmpty()) {
                parts.add(resourceQualifier);
            }
        }
        int i = config.screenLayout & 192;
        if (i == 64) {
            parts.add("ldltr");
        } else if (i == 128) {
            parts.add("ldrtl");
        }
        if (config.smallestScreenWidthDp != 0) {
            parts.add(XML_ATTR_SMALLEST_WIDTH + config.smallestScreenWidthDp + "dp");
        }
        if (config.screenWidthDp != 0) {
            parts.add("w" + config.screenWidthDp + "dp");
        }
        if (config.screenHeightDp != 0) {
            parts.add("h" + config.screenHeightDp + "dp");
        }
        int i2 = config.screenLayout & 15;
        if (i2 == 1) {
            parts.add("small");
        } else if (i2 == 2) {
            parts.add("normal");
        } else if (i2 == 3) {
            parts.add(Slice.HINT_LARGE);
        } else if (i2 == 4) {
            parts.add("xlarge");
        }
        int i3 = config.screenLayout & 48;
        if (i3 == 16) {
            parts.add("notlong");
        } else if (i3 == 32) {
            parts.add("long");
        }
        int i4 = config.screenLayout & 768;
        if (i4 == 256) {
            parts.add("notround");
        } else if (i4 == 512) {
            parts.add("round");
        }
        int i5 = config.colorMode & 3;
        if (i5 == 1) {
            parts.add("nowidecg");
        } else if (i5 == 2) {
            parts.add("widecg");
        }
        int i6 = config.colorMode & 12;
        if (i6 == 4) {
            parts.add("lowdr");
        } else if (i6 == 8) {
            parts.add("highdr");
        }
        int i7 = config.orientation;
        if (i7 == 1) {
            parts.add("port");
        } else if (i7 == 2) {
            parts.add("land");
        }
        switch (config.uiMode & 15) {
            case 2:
                parts.add("desk");
                break;
            case 3:
                parts.add("car");
                break;
            case 4:
                parts.add("television");
                break;
            case 5:
                parts.add("appliance");
                break;
            case 6:
                parts.add("watch");
                break;
            case 7:
                parts.add("vrheadset");
                break;
        }
        int i8 = config.uiMode & 48;
        if (i8 == 16) {
            parts.add("notnight");
        } else if (i8 == 32) {
            parts.add(Camera.Parameters.SCENE_MODE_NIGHT);
        }
        int i9 = config.densityDpi;
        if (i9 != 0) {
            if (i9 == 120) {
                parts.add("ldpi");
            } else if (i9 == 160) {
                parts.add("mdpi");
            } else if (i9 == 213) {
                parts.add("tvdpi");
            } else if (i9 == 240) {
                parts.add("hdpi");
            } else if (i9 == 320) {
                parts.add("xhdpi");
            } else if (i9 == 480) {
                parts.add("xxhdpi");
            } else if (i9 == 640) {
                parts.add("xxxhdpi");
            } else {
                switch (i9) {
                    case DENSITY_DPI_ANY /* 65534 */:
                        parts.add("anydpi");
                        break;
                    case 65535:
                        parts.add("nodpi");
                        break;
                    default:
                        parts.add(config.densityDpi + "dpi");
                        break;
                }
            }
        }
        int i10 = config.touchscreen;
        if (i10 == 1) {
            parts.add("notouch");
        } else if (i10 == 3) {
            parts.add("finger");
        }
        int i11 = config.keyboardHidden;
        if (i11 == 1) {
            parts.add("keysexposed");
        } else if (i11 == 2) {
            parts.add("keyshidden");
        } else if (i11 == 3) {
            parts.add("keyssoft");
        }
        int i12 = config.keyboard;
        if (i12 == 1) {
            parts.add("nokeys");
        } else if (i12 == 2) {
            parts.add("qwerty");
        } else if (i12 == 3) {
            parts.add("12key");
        }
        int i13 = config.navigationHidden;
        if (i13 == 1) {
            parts.add("navexposed");
        } else if (i13 == 2) {
            parts.add("navhidden");
        }
        int i14 = config.navigation;
        if (i14 == 1) {
            parts.add("nonav");
        } else if (i14 == 2) {
            parts.add("dpad");
        } else if (i14 == 3) {
            parts.add("trackball");
        } else if (i14 == 4) {
            parts.add("wheel");
        }
        if (metrics != null) {
            if (metrics.widthPixels >= metrics.heightPixels) {
                width = metrics.widthPixels;
                height = metrics.heightPixels;
            } else {
                width = metrics.heightPixels;
                height = metrics.widthPixels;
            }
            parts.add(width + "x" + height);
        }
        parts.add(Telephony.BaseMmsColumns.MMS_VERSION + Build.VERSION.RESOURCES_SDK_INT);
        return TextUtils.join(NativeLibraryHelper.CLEAR_ABI_OVERRIDE, parts);
    }

    @UnsupportedAppUsage
    public static Configuration generateDelta(Configuration base, Configuration change) {
        Configuration delta = new Configuration();
        float f = base.fontScale;
        float f2 = change.fontScale;
        if (f != f2) {
            delta.fontScale = f2;
        }
        int i = base.mcc;
        int i2 = change.mcc;
        if (i != i2) {
            delta.mcc = i2;
        }
        int i3 = base.mnc;
        int i4 = change.mnc;
        if (i3 != i4) {
            delta.mnc = i4;
        }
        base.fixUpLocaleList();
        change.fixUpLocaleList();
        if (!base.mLocaleList.equals(change.mLocaleList)) {
            delta.mLocaleList = change.mLocaleList;
            delta.locale = change.locale;
        }
        int i5 = base.touchscreen;
        int i6 = change.touchscreen;
        if (i5 != i6) {
            delta.touchscreen = i6;
        }
        int i7 = base.keyboard;
        int i8 = change.keyboard;
        if (i7 != i8) {
            delta.keyboard = i8;
        }
        int i9 = base.keyboardHidden;
        int i10 = change.keyboardHidden;
        if (i9 != i10) {
            delta.keyboardHidden = i10;
        }
        int i11 = base.navigation;
        int i12 = change.navigation;
        if (i11 != i12) {
            delta.navigation = i12;
        }
        int i13 = base.navigationHidden;
        int i14 = change.navigationHidden;
        if (i13 != i14) {
            delta.navigationHidden = i14;
        }
        int i15 = base.orientation;
        int i16 = change.orientation;
        if (i15 != i16) {
            delta.orientation = i16;
        }
        int i17 = base.screenLayout & 15;
        int i18 = change.screenLayout;
        if (i17 != (i18 & 15)) {
            delta.screenLayout |= i18 & 15;
        }
        int i19 = base.screenLayout & 192;
        int i20 = change.screenLayout;
        if (i19 != (i20 & 192)) {
            delta.screenLayout |= i20 & 192;
        }
        int i21 = base.screenLayout & 48;
        int i22 = change.screenLayout;
        if (i21 != (i22 & 48)) {
            delta.screenLayout |= i22 & 48;
        }
        int i23 = base.screenLayout & 768;
        int i24 = change.screenLayout;
        if (i23 != (i24 & 768)) {
            delta.screenLayout |= i24 & 768;
        }
        int i25 = base.colorMode & 3;
        int i26 = change.colorMode;
        if (i25 != (i26 & 3)) {
            delta.colorMode |= i26 & 3;
        }
        int i27 = base.colorMode & 12;
        int i28 = change.colorMode;
        if (i27 != (i28 & 12)) {
            delta.colorMode |= i28 & 12;
        }
        int i29 = base.uiMode & 15;
        int i30 = change.uiMode;
        if (i29 != (i30 & 15)) {
            delta.uiMode |= i30 & 15;
        }
        int i31 = base.uiMode & 48;
        int i32 = change.uiMode;
        if (i31 != (i32 & 48)) {
            delta.uiMode |= i32 & 48;
        }
        int i33 = base.uiMode & 192;
        int i34 = change.uiMode;
        if (i33 != (i34 & 192)) {
            delta.uiMode |= i34 & 192;
        }
        int i35 = base.screenWidthDp;
        int i36 = change.screenWidthDp;
        if (i35 != i36) {
            delta.screenWidthDp = i36;
        }
        int i37 = base.screenHeightDp;
        int i38 = change.screenHeightDp;
        if (i37 != i38) {
            delta.screenHeightDp = i38;
        }
        int i39 = base.smallestScreenWidthDp;
        int i40 = change.smallestScreenWidthDp;
        if (i39 != i40) {
            delta.smallestScreenWidthDp = i40;
        }
        int i41 = base.densityDpi;
        int i42 = change.densityDpi;
        if (i41 != i42) {
            delta.densityDpi = i42;
        }
        int i43 = base.assetsSeq;
        int i44 = change.assetsSeq;
        if (i43 != i44) {
            delta.assetsSeq = i44;
        }
        if (!base.windowConfiguration.equals(change.windowConfiguration)) {
            delta.windowConfiguration.setTo(change.windowConfiguration);
        }
        return delta;
    }

    public static void readXmlAttrs(XmlPullParser parser, Configuration configOut) throws XmlPullParserException, IOException {
        configOut.fontScale = Float.intBitsToFloat(XmlUtils.readIntAttribute(parser, XML_ATTR_FONT_SCALE, 0));
        configOut.mcc = XmlUtils.readIntAttribute(parser, "mcc", 0);
        configOut.mnc = XmlUtils.readIntAttribute(parser, "mnc", 0);
        String localesStr = XmlUtils.readStringAttribute(parser, XML_ATTR_LOCALES);
        configOut.mLocaleList = LocaleList.forLanguageTags(localesStr);
        configOut.locale = configOut.mLocaleList.get(0);
        configOut.touchscreen = XmlUtils.readIntAttribute(parser, XML_ATTR_TOUCHSCREEN, 0);
        configOut.keyboard = XmlUtils.readIntAttribute(parser, "key", 0);
        configOut.keyboardHidden = XmlUtils.readIntAttribute(parser, XML_ATTR_KEYBOARD_HIDDEN, 0);
        configOut.hardKeyboardHidden = XmlUtils.readIntAttribute(parser, XML_ATTR_HARD_KEYBOARD_HIDDEN, 0);
        configOut.navigation = XmlUtils.readIntAttribute(parser, XML_ATTR_NAVIGATION, 0);
        configOut.navigationHidden = XmlUtils.readIntAttribute(parser, XML_ATTR_NAVIGATION_HIDDEN, 0);
        configOut.orientation = XmlUtils.readIntAttribute(parser, XML_ATTR_ORIENTATION, 0);
        configOut.screenLayout = XmlUtils.readIntAttribute(parser, XML_ATTR_SCREEN_LAYOUT, 0);
        configOut.colorMode = XmlUtils.readIntAttribute(parser, XML_ATTR_COLOR_MODE, 0);
        configOut.uiMode = XmlUtils.readIntAttribute(parser, XML_ATTR_UI_MODE, 0);
        configOut.screenWidthDp = XmlUtils.readIntAttribute(parser, "width", 0);
        configOut.screenHeightDp = XmlUtils.readIntAttribute(parser, "height", 0);
        configOut.smallestScreenWidthDp = XmlUtils.readIntAttribute(parser, XML_ATTR_SMALLEST_WIDTH, 0);
        configOut.densityDpi = XmlUtils.readIntAttribute(parser, XML_ATTR_DENSITY, 0);
    }

    public static void writeXmlAttrs(XmlSerializer xml, Configuration config) throws IOException {
        XmlUtils.writeIntAttribute(xml, XML_ATTR_FONT_SCALE, Float.floatToIntBits(config.fontScale));
        int i = config.mcc;
        if (i != 0) {
            XmlUtils.writeIntAttribute(xml, "mcc", i);
        }
        int i2 = config.mnc;
        if (i2 != 0) {
            XmlUtils.writeIntAttribute(xml, "mnc", i2);
        }
        config.fixUpLocaleList();
        if (!config.mLocaleList.isEmpty()) {
            XmlUtils.writeStringAttribute(xml, XML_ATTR_LOCALES, config.mLocaleList.toLanguageTags());
        }
        int i3 = config.touchscreen;
        if (i3 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_TOUCHSCREEN, i3);
        }
        int i4 = config.keyboard;
        if (i4 != 0) {
            XmlUtils.writeIntAttribute(xml, "key", i4);
        }
        int i5 = config.keyboardHidden;
        if (i5 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_KEYBOARD_HIDDEN, i5);
        }
        int i6 = config.hardKeyboardHidden;
        if (i6 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_HARD_KEYBOARD_HIDDEN, i6);
        }
        int i7 = config.navigation;
        if (i7 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_NAVIGATION, i7);
        }
        int i8 = config.navigationHidden;
        if (i8 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_NAVIGATION_HIDDEN, i8);
        }
        int i9 = config.orientation;
        if (i9 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_ORIENTATION, i9);
        }
        int i10 = config.screenLayout;
        if (i10 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_SCREEN_LAYOUT, i10);
        }
        int i11 = config.colorMode;
        if (i11 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_COLOR_MODE, i11);
        }
        int i12 = config.uiMode;
        if (i12 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_UI_MODE, i12);
        }
        int i13 = config.screenWidthDp;
        if (i13 != 0) {
            XmlUtils.writeIntAttribute(xml, "width", i13);
        }
        int i14 = config.screenHeightDp;
        if (i14 != 0) {
            XmlUtils.writeIntAttribute(xml, "height", i14);
        }
        int i15 = config.smallestScreenWidthDp;
        if (i15 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_SMALLEST_WIDTH, i15);
        }
        int i16 = config.densityDpi;
        if (i16 != 0) {
            XmlUtils.writeIntAttribute(xml, XML_ATTR_DENSITY, i16);
        }
    }
}
