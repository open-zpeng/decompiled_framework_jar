package android.nfc.cardemulation;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes2.dex */
public final class NfcFServiceInfo implements Parcelable {
    public static final Parcelable.Creator<NfcFServiceInfo> CREATOR = new Parcelable.Creator<NfcFServiceInfo>() { // from class: android.nfc.cardemulation.NfcFServiceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NfcFServiceInfo createFromParcel(Parcel source) {
            ResolveInfo info = ResolveInfo.CREATOR.createFromParcel(source);
            String description = source.readString();
            String systemCode = source.readString();
            String dynamicSystemCode = null;
            if (source.readInt() != 0) {
                dynamicSystemCode = source.readString();
            }
            String dynamicSystemCode2 = dynamicSystemCode;
            String nfcid2 = source.readString();
            String dynamicNfcid2 = null;
            if (source.readInt() != 0) {
                dynamicNfcid2 = source.readString();
            }
            int uid = source.readInt();
            String t3tPmm = source.readString();
            NfcFServiceInfo service = new NfcFServiceInfo(info, description, systemCode, dynamicSystemCode2, nfcid2, dynamicNfcid2, uid, t3tPmm);
            return service;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NfcFServiceInfo[] newArray(int size) {
            return new NfcFServiceInfo[size];
        }
    };
    private static final String DEFAULT_T3T_PMM = "FFFFFFFFFFFFFFFF";
    static final String TAG = "NfcFServiceInfo";
    final String mDescription;
    String mDynamicNfcid2;
    String mDynamicSystemCode;
    final String mNfcid2;
    final ResolveInfo mService;
    final String mSystemCode;
    final String mT3tPmm;
    final int mUid;

    public synchronized NfcFServiceInfo(ResolveInfo info, String description, String systemCode, String dynamicSystemCode, String nfcid2, String dynamicNfcid2, int uid, String t3tPmm) {
        this.mService = info;
        this.mDescription = description;
        this.mSystemCode = systemCode;
        this.mDynamicSystemCode = dynamicSystemCode;
        this.mNfcid2 = nfcid2;
        this.mDynamicNfcid2 = dynamicNfcid2;
        this.mUid = uid;
        this.mT3tPmm = t3tPmm;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [android.content.res.XmlResourceParser, java.lang.String] */
    public synchronized NfcFServiceInfo(PackageManager pm, ResolveInfo info) throws XmlPullParserException, IOException {
        int i;
        int depth;
        boolean z;
        ServiceInfo si = info.serviceInfo;
        XmlResourceParser parser = 0;
        try {
            try {
                parser = si.loadXmlMetaData(pm, HostNfcFService.SERVICE_META_DATA);
                if (parser == null) {
                    throw new XmlPullParserException("No android.nfc.cardemulation.host_nfcf_service meta-data");
                }
                int eventType = parser.getEventType();
                while (true) {
                    i = 1;
                    if (eventType == 2 || eventType == 1) {
                        break;
                    }
                    eventType = parser.next();
                }
                if (!"host-nfcf-service".equals(parser.getName())) {
                    throw new XmlPullParserException("Meta-data does not start with <host-nfcf-service> tag");
                }
                Resources res = pm.getResourcesForApplication(si.applicationInfo);
                AttributeSet attrs = Xml.asAttributeSet(parser);
                TypedArray sa = res.obtainAttributes(attrs, R.styleable.HostNfcFService);
                this.mService = info;
                this.mDescription = sa.getString(0);
                this.mDynamicSystemCode = parser;
                this.mDynamicNfcid2 = parser;
                sa.recycle();
                String systemCode = null;
                String nfcid2 = null;
                String t3tPmm = null;
                int depth2 = parser.getDepth();
                while (true) {
                    int depth3 = depth2;
                    int eventType2 = parser.next();
                    if (eventType2 == 3) {
                        depth = depth3;
                        if (parser.getDepth() <= depth) {
                            break;
                        }
                    } else {
                        depth = depth3;
                    }
                    if (eventType2 == i) {
                        break;
                    }
                    String tagName = parser.getName();
                    if (eventType2 == 2 && "system-code-filter".equals(tagName) && systemCode == null) {
                        TypedArray a = res.obtainAttributes(attrs, R.styleable.SystemCodeFilter);
                        systemCode = a.getString(0).toUpperCase();
                        if (!NfcFCardEmulation.isValidSystemCode(systemCode) && !systemCode.equalsIgnoreCase(WifiEnterpriseConfig.EMPTY_VALUE)) {
                            Log.e(TAG, "Invalid System Code: " + systemCode);
                            systemCode = null;
                        }
                        a.recycle();
                    } else if (eventType2 == 2 && "nfcid2-filter".equals(tagName) && nfcid2 == null) {
                        TypedArray a2 = res.obtainAttributes(attrs, R.styleable.Nfcid2Filter);
                        String nfcid22 = a2.getString(0).toUpperCase();
                        if (!nfcid22.equalsIgnoreCase("RANDOM") && !nfcid22.equalsIgnoreCase(WifiEnterpriseConfig.EMPTY_VALUE) && !NfcFCardEmulation.isValidNfcid2(nfcid22)) {
                            Log.e(TAG, "Invalid NFCID2: " + nfcid22);
                            nfcid22 = null;
                        }
                        nfcid2 = nfcid22;
                        a2.recycle();
                    } else {
                        if (eventType2 == 2 && tagName.equals("t3tPmm-filter") && t3tPmm == null) {
                            TypedArray a3 = res.obtainAttributes(attrs, R.styleable.T3tPmmFilter);
                            z = false;
                            t3tPmm = a3.getString(0).toUpperCase();
                            a3.recycle();
                        } else {
                            z = false;
                        }
                        depth2 = depth;
                        i = 1;
                    }
                    depth2 = depth;
                    i = 1;
                }
                this.mSystemCode = systemCode == null ? WifiEnterpriseConfig.EMPTY_VALUE : systemCode;
                this.mNfcid2 = nfcid2 == null ? WifiEnterpriseConfig.EMPTY_VALUE : nfcid2;
                this.mT3tPmm = t3tPmm == null ? DEFAULT_T3T_PMM : t3tPmm;
                this.mUid = si.applicationInfo.uid;
            } catch (PackageManager.NameNotFoundException e) {
                throw new XmlPullParserException("Unable to create context for: " + si.packageName);
            }
        } finally {
            if (parser != 0) {
                parser.close();
            }
        }
    }

    public synchronized ComponentName getComponent() {
        return new ComponentName(this.mService.serviceInfo.packageName, this.mService.serviceInfo.name);
    }

    public synchronized String getSystemCode() {
        return this.mDynamicSystemCode == null ? this.mSystemCode : this.mDynamicSystemCode;
    }

    public synchronized void setOrReplaceDynamicSystemCode(String systemCode) {
        this.mDynamicSystemCode = systemCode;
    }

    public synchronized String getNfcid2() {
        return this.mDynamicNfcid2 == null ? this.mNfcid2 : this.mDynamicNfcid2;
    }

    public synchronized void setOrReplaceDynamicNfcid2(String nfcid2) {
        this.mDynamicNfcid2 = nfcid2;
    }

    public synchronized String getDescription() {
        return this.mDescription;
    }

    public synchronized int getUid() {
        return this.mUid;
    }

    public synchronized String getT3tPmm() {
        return this.mT3tPmm;
    }

    public synchronized CharSequence loadLabel(PackageManager pm) {
        return this.mService.loadLabel(pm);
    }

    public synchronized Drawable loadIcon(PackageManager pm) {
        return this.mService.loadIcon(pm);
    }

    public String toString() {
        StringBuilder out = new StringBuilder("NfcFService: ");
        out.append(getComponent());
        out.append(", description: " + this.mDescription);
        out.append(", System Code: " + this.mSystemCode);
        if (this.mDynamicSystemCode != null) {
            out.append(", dynamic System Code: " + this.mDynamicSystemCode);
        }
        out.append(", NFCID2: " + this.mNfcid2);
        if (this.mDynamicNfcid2 != null) {
            out.append(", dynamic NFCID2: " + this.mDynamicNfcid2);
        }
        out.append(", T3T PMM:" + this.mT3tPmm);
        return out.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof NfcFServiceInfo) {
            NfcFServiceInfo thatService = (NfcFServiceInfo) o;
            return thatService.getComponent().equals(getComponent()) && thatService.mSystemCode.equalsIgnoreCase(this.mSystemCode) && thatService.mNfcid2.equalsIgnoreCase(this.mNfcid2) && thatService.mT3tPmm.equalsIgnoreCase(this.mT3tPmm);
        }
        return false;
    }

    public int hashCode() {
        return getComponent().hashCode();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        this.mService.writeToParcel(dest, flags);
        dest.writeString(this.mDescription);
        dest.writeString(this.mSystemCode);
        dest.writeInt(this.mDynamicSystemCode != null ? 1 : 0);
        if (this.mDynamicSystemCode != null) {
            dest.writeString(this.mDynamicSystemCode);
        }
        dest.writeString(this.mNfcid2);
        dest.writeInt(this.mDynamicNfcid2 != null ? 1 : 0);
        if (this.mDynamicNfcid2 != null) {
            dest.writeString(this.mDynamicNfcid2);
        }
        dest.writeInt(this.mUid);
        dest.writeString(this.mT3tPmm);
    }

    public synchronized void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("    " + getComponent() + " (Description: " + getDescription() + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("    System Code: ");
        sb.append(getSystemCode());
        pw.println(sb.toString());
        pw.println("    NFCID2: " + getNfcid2());
        pw.println("    T3tPmm: " + getT3tPmm());
    }
}
