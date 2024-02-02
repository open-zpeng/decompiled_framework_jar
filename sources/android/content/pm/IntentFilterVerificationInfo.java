package android.content.pm;

import android.annotation.SystemApi;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
@SystemApi
/* loaded from: classes.dex */
public final class IntentFilterVerificationInfo implements Parcelable {
    private static final String ATTR_DOMAIN_NAME = "name";
    private static final String ATTR_PACKAGE_NAME = "packageName";
    private static final String ATTR_STATUS = "status";
    private static final String TAG_DOMAIN = "domain";
    private ArraySet<String> mDomains;
    private int mMainStatus;
    private String mPackageName;
    private static final String TAG = IntentFilterVerificationInfo.class.getName();
    public static final Parcelable.Creator<IntentFilterVerificationInfo> CREATOR = new Parcelable.Creator<IntentFilterVerificationInfo>() { // from class: android.content.pm.IntentFilterVerificationInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IntentFilterVerificationInfo createFromParcel(Parcel source) {
            return new IntentFilterVerificationInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IntentFilterVerificationInfo[] newArray(int size) {
            return new IntentFilterVerificationInfo[size];
        }
    };

    public synchronized IntentFilterVerificationInfo() {
        this.mDomains = new ArraySet<>();
        this.mPackageName = null;
        this.mMainStatus = 0;
    }

    public synchronized IntentFilterVerificationInfo(String packageName, ArraySet<String> domains) {
        this.mDomains = new ArraySet<>();
        this.mPackageName = packageName;
        this.mDomains = domains;
        this.mMainStatus = 0;
    }

    public synchronized IntentFilterVerificationInfo(XmlPullParser parser) throws IOException, XmlPullParserException {
        this.mDomains = new ArraySet<>();
        readFromXml(parser);
    }

    public synchronized IntentFilterVerificationInfo(Parcel source) {
        this.mDomains = new ArraySet<>();
        readFromParcel(source);
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public int getStatus() {
        return this.mMainStatus;
    }

    public synchronized void setStatus(int s) {
        if (s >= 0 && s <= 3) {
            this.mMainStatus = s;
            return;
        }
        String str = TAG;
        Log.w(str, "Trying to set a non supported status: " + s);
    }

    public Set<String> getDomains() {
        return this.mDomains;
    }

    public synchronized void setDomains(ArraySet<String> list) {
        this.mDomains = list;
    }

    public synchronized String getDomainsString() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = this.mDomains.iterator();
        while (it.hasNext()) {
            String str = it.next();
            if (sb.length() > 0) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            sb.append(str);
        }
        return sb.toString();
    }

    synchronized String getStringFromXml(XmlPullParser parser, String attribute, String defaultValue) {
        String value = parser.getAttributeValue(null, attribute);
        if (value == null) {
            String msg = "Missing element under " + TAG + ": " + attribute + " at " + parser.getPositionDescription();
            Log.w(TAG, msg);
            return defaultValue;
        }
        return value;
    }

    synchronized int getIntFromXml(XmlPullParser parser, String attribute, int defaultValue) {
        String value = parser.getAttributeValue(null, attribute);
        if (TextUtils.isEmpty(value)) {
            String msg = "Missing element under " + TAG + ": " + attribute + " at " + parser.getPositionDescription();
            Log.w(TAG, msg);
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public synchronized void readFromXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        this.mPackageName = getStringFromXml(parser, "packageName", null);
        if (this.mPackageName == null) {
            Log.e(TAG, "Package name cannot be null!");
        }
        int status = getIntFromXml(parser, "status", -1);
        if (status == -1) {
            String str = TAG;
            Log.e(str, "Unknown status value: " + status);
        }
        this.mMainStatus = status;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        String tagName = parser.getName();
                        if (tagName.equals(TAG_DOMAIN)) {
                            String name = getStringFromXml(parser, "name", null);
                            if (!TextUtils.isEmpty(name)) {
                                this.mDomains.add(name);
                            }
                        } else {
                            String str2 = TAG;
                            Log.w(str2, "Unknown tag parsing IntentFilter: " + tagName);
                        }
                        XmlUtils.skipCurrentTag(parser);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public synchronized void writeToXml(XmlSerializer serializer) throws IOException {
        serializer.attribute(null, "packageName", this.mPackageName);
        serializer.attribute(null, "status", String.valueOf(this.mMainStatus));
        Iterator<String> it = this.mDomains.iterator();
        while (it.hasNext()) {
            String str = it.next();
            serializer.startTag(null, TAG_DOMAIN);
            serializer.attribute(null, "name", str);
            serializer.endTag(null, TAG_DOMAIN);
        }
    }

    public synchronized String getStatusString() {
        return getStatusStringFromValue(this.mMainStatus << 32);
    }

    public static synchronized String getStatusStringFromValue(long val) {
        StringBuilder sb = new StringBuilder();
        switch ((int) (val >> 32)) {
            case 1:
                sb.append("ask");
                break;
            case 2:
                sb.append("always : ");
                sb.append(Long.toHexString((-1) & val));
                break;
            case 3:
                sb.append("never");
                break;
            case 4:
                sb.append("always-ask");
                break;
            default:
                sb.append("undefined");
                break;
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private synchronized void readFromParcel(Parcel source) {
        this.mPackageName = source.readString();
        this.mMainStatus = source.readInt();
        ArrayList<String> list = new ArrayList<>();
        source.readStringList(list);
        this.mDomains.addAll(list);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPackageName);
        dest.writeInt(this.mMainStatus);
        dest.writeStringList(new ArrayList(this.mDomains));
    }
}
