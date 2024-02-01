package android.printservice;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.os.Parcel;
import android.os.Parcelable;
@SystemApi
/* loaded from: classes2.dex */
public final class PrintServiceInfo implements Parcelable {
    private static final String TAG_PRINT_SERVICE = "print-service";
    private final String mAddPrintersActivityName;
    private final String mAdvancedPrintOptionsActivityName;
    private final String mId;
    private boolean mIsEnabled;
    private final ResolveInfo mResolveInfo;
    private final String mSettingsActivityName;
    private static final String LOG_TAG = PrintServiceInfo.class.getSimpleName();
    public static final Parcelable.Creator<PrintServiceInfo> CREATOR = new Parcelable.Creator<PrintServiceInfo>() { // from class: android.printservice.PrintServiceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrintServiceInfo createFromParcel(Parcel parcel) {
            return new PrintServiceInfo(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrintServiceInfo[] newArray(int size) {
            return new PrintServiceInfo[size];
        }
    };

    public synchronized PrintServiceInfo(Parcel parcel) {
        this.mId = parcel.readString();
        this.mIsEnabled = parcel.readByte() != 0;
        this.mResolveInfo = (ResolveInfo) parcel.readParcelable(null);
        this.mSettingsActivityName = parcel.readString();
        this.mAddPrintersActivityName = parcel.readString();
        this.mAdvancedPrintOptionsActivityName = parcel.readString();
    }

    public synchronized PrintServiceInfo(ResolveInfo resolveInfo, String settingsActivityName, String addPrintersActivityName, String advancedPrintOptionsActivityName) {
        this.mId = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name).flattenToString();
        this.mResolveInfo = resolveInfo;
        this.mSettingsActivityName = settingsActivityName;
        this.mAddPrintersActivityName = addPrintersActivityName;
        this.mAdvancedPrintOptionsActivityName = advancedPrintOptionsActivityName;
    }

    public ComponentName getComponentName() {
        return new ComponentName(this.mResolveInfo.serviceInfo.packageName, this.mResolveInfo.serviceInfo.name);
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00b1, code lost:
        if (r3 == null) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized android.printservice.PrintServiceInfo create(android.content.Context r12, android.content.pm.ResolveInfo r13) {
        /*
            r0 = 0
            r1 = 0
            r2 = 0
            r3 = 0
            android.content.pm.PackageManager r4 = r12.getPackageManager()
            android.content.pm.ServiceInfo r5 = r13.serviceInfo
            java.lang.String r6 = "android.printservice"
            android.content.res.XmlResourceParser r3 = r5.loadXmlMetaData(r4, r6)
            if (r3 == 0) goto Lba
            r5 = 0
            r6 = r5
        L14:
            r7 = 1
            if (r6 == r7) goto L20
            r8 = 2
            if (r6 == r8) goto L20
            int r7 = r3.next()     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            r6 = r7
            goto L14
        L20:
            java.lang.String r8 = r3.getName()     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            java.lang.String r9 = "print-service"
            boolean r9 = r9.equals(r8)     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            if (r9 != 0) goto L35
            java.lang.String r5 = android.printservice.PrintServiceInfo.LOG_TAG     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            java.lang.String r7 = "Ignoring meta-data that does not start with print-service tag"
            android.util.Log.e(r5, r7)     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            goto L5a
        L35:
            android.content.pm.ServiceInfo r9 = r13.serviceInfo     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            android.content.pm.ApplicationInfo r9 = r9.applicationInfo     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            android.content.res.Resources r9 = r4.getResourcesForApplication(r9)     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            android.util.AttributeSet r10 = android.util.Xml.asAttributeSet(r3)     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            int[] r11 = com.android.internal.R.styleable.PrintService     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            android.content.res.TypedArray r11 = r9.obtainAttributes(r10, r11)     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            java.lang.String r5 = r11.getString(r5)     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            r0 = r5
            java.lang.String r5 = r11.getString(r7)     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            r1 = r5
            r5 = 3
            java.lang.String r5 = r11.getString(r5)     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
            r2 = r5
            r11.recycle()     // Catch: java.lang.Throwable -> L60 android.content.pm.PackageManager.NameNotFoundException -> L62 org.xmlpull.v1.XmlPullParserException -> L80 java.io.IOException -> L9a
        L5a:
            if (r3 == 0) goto Lba
        L5c:
            r3.close()
            goto Lba
        L60:
            r5 = move-exception
            goto Lb4
        L62:
            r5 = move-exception
            java.lang.String r6 = android.printservice.PrintServiceInfo.LOG_TAG     // Catch: java.lang.Throwable -> L60
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L60
            r7.<init>()     // Catch: java.lang.Throwable -> L60
            java.lang.String r8 = "Unable to load resources for: "
            r7.append(r8)     // Catch: java.lang.Throwable -> L60
            android.content.pm.ServiceInfo r8 = r13.serviceInfo     // Catch: java.lang.Throwable -> L60
            java.lang.String r8 = r8.packageName     // Catch: java.lang.Throwable -> L60
            r7.append(r8)     // Catch: java.lang.Throwable -> L60
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> L60
            android.util.Log.e(r6, r7)     // Catch: java.lang.Throwable -> L60
            if (r3 == 0) goto Lba
            goto L5c
        L80:
            r5 = move-exception
            java.lang.String r6 = android.printservice.PrintServiceInfo.LOG_TAG     // Catch: java.lang.Throwable -> L60
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L60
            r7.<init>()     // Catch: java.lang.Throwable -> L60
            java.lang.String r8 = "Error reading meta-data:"
            r7.append(r8)     // Catch: java.lang.Throwable -> L60
            r7.append(r5)     // Catch: java.lang.Throwable -> L60
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> L60
            android.util.Log.w(r6, r7)     // Catch: java.lang.Throwable -> L60
            if (r3 == 0) goto Lba
            goto L5c
        L9a:
            r5 = move-exception
            java.lang.String r6 = android.printservice.PrintServiceInfo.LOG_TAG     // Catch: java.lang.Throwable -> L60
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L60
            r7.<init>()     // Catch: java.lang.Throwable -> L60
            java.lang.String r8 = "Error reading meta-data:"
            r7.append(r8)     // Catch: java.lang.Throwable -> L60
            r7.append(r5)     // Catch: java.lang.Throwable -> L60
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> L60
            android.util.Log.w(r6, r7)     // Catch: java.lang.Throwable -> L60
            if (r3 == 0) goto Lba
            goto L5c
        Lb4:
            if (r3 == 0) goto Lb9
            r3.close()
        Lb9:
            throw r5
        Lba:
            android.printservice.PrintServiceInfo r5 = new android.printservice.PrintServiceInfo
            r5.<init>(r13, r0, r1, r2)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: android.printservice.PrintServiceInfo.create(android.content.Context, android.content.pm.ResolveInfo):android.printservice.PrintServiceInfo");
    }

    public synchronized String getId() {
        return this.mId;
    }

    public synchronized boolean isEnabled() {
        return this.mIsEnabled;
    }

    public synchronized void setIsEnabled(boolean isEnabled) {
        this.mIsEnabled = isEnabled;
    }

    public synchronized ResolveInfo getResolveInfo() {
        return this.mResolveInfo;
    }

    public synchronized String getSettingsActivityName() {
        return this.mSettingsActivityName;
    }

    public synchronized String getAddPrintersActivityName() {
        return this.mAddPrintersActivityName;
    }

    public synchronized String getAdvancedOptionsActivityName() {
        return this.mAdvancedPrintOptionsActivityName;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flagz) {
        parcel.writeString(this.mId);
        parcel.writeByte(this.mIsEnabled ? (byte) 1 : (byte) 0);
        parcel.writeParcelable(this.mResolveInfo, 0);
        parcel.writeString(this.mSettingsActivityName);
        parcel.writeString(this.mAddPrintersActivityName);
        parcel.writeString(this.mAdvancedPrintOptionsActivityName);
    }

    public int hashCode() {
        return 31 + (this.mId == null ? 0 : this.mId.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PrintServiceInfo other = (PrintServiceInfo) obj;
        if (this.mId == null) {
            if (other.mId != null) {
                return false;
            }
        } else if (!this.mId.equals(other.mId)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "PrintServiceInfo{id=" + this.mId + "isEnabled=" + this.mIsEnabled + ", resolveInfo=" + this.mResolveInfo + ", settingsActivityName=" + this.mSettingsActivityName + ", addPrintersActivityName=" + this.mAddPrintersActivityName + ", advancedPrintOptionsActivityName=" + this.mAdvancedPrintOptionsActivityName + "}";
    }
}
