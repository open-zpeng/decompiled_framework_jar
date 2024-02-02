package android.service.autofill;

import android.Manifest;
import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.metrics.LogMaker;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto;
import java.io.IOException;
import java.io.PrintWriter;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes2.dex */
public final class AutofillServiceInfo {
    private static final String TAG = "AutofillServiceInfo";
    private static final String TAG_AUTOFILL_SERVICE = "autofill-service";
    private static final String TAG_COMPATIBILITY_PACKAGE = "compatibility-package";
    private final ArrayMap<String, Long> mCompatibilityPackages;
    private final ServiceInfo mServiceInfo;
    private final String mSettingsActivity;

    private static synchronized ServiceInfo getServiceInfoOrThrow(ComponentName comp, int userHandle) throws PackageManager.NameNotFoundException {
        try {
            ServiceInfo si = AppGlobals.getPackageManager().getServiceInfo(comp, 128, userHandle);
            if (si != null) {
                return si;
            }
        } catch (RemoteException e) {
        }
        throw new PackageManager.NameNotFoundException(comp.toString());
    }

    public synchronized AutofillServiceInfo(Context context, ComponentName comp, int userHandle) throws PackageManager.NameNotFoundException {
        this(context, getServiceInfoOrThrow(comp, userHandle));
    }

    public synchronized AutofillServiceInfo(Context context, ServiceInfo si) {
        if (!Manifest.permission.BIND_AUTOFILL_SERVICE.equals(si.permission)) {
            if (Manifest.permission.BIND_AUTOFILL.equals(si.permission)) {
                Log.w(TAG, "AutofillService from '" + si.packageName + "' uses unsupported permission " + Manifest.permission.BIND_AUTOFILL + ". It works for now, but might not be supported on future releases");
                new MetricsLogger().write(new LogMaker((int) MetricsProto.MetricsEvent.AUTOFILL_INVALID_PERMISSION).setPackageName(si.packageName));
            } else {
                Log.w(TAG, "AutofillService from '" + si.packageName + "' does not require permission " + Manifest.permission.BIND_AUTOFILL_SERVICE);
                throw new SecurityException("Service does not require permission android.permission.BIND_AUTOFILL_SERVICE");
            }
        }
        this.mServiceInfo = si;
        XmlResourceParser parser = si.loadXmlMetaData(context.getPackageManager(), AutofillService.SERVICE_META_DATA);
        if (parser == null) {
            this.mSettingsActivity = null;
            this.mCompatibilityPackages = null;
            return;
        }
        String settingsActivity = null;
        ArrayMap<String, Long> compatibilityPackages = null;
        try {
            Resources resources = context.getPackageManager().getResourcesForApplication(si.applicationInfo);
            for (int type = 0; type != 1 && type != 2; type = parser.next()) {
            }
            if (TAG_AUTOFILL_SERVICE.equals(parser.getName())) {
                AttributeSet allAttributes = Xml.asAttributeSet(parser);
                TypedArray afsAttributes = resources.obtainAttributes(allAttributes, R.styleable.AutofillService);
                settingsActivity = afsAttributes.getString(0);
                if (afsAttributes != null) {
                    afsAttributes.recycle();
                }
                compatibilityPackages = parseCompatibilityPackages(parser, resources);
            } else {
                Log.e(TAG, "Meta-data does not start with autofill-service tag");
            }
        } catch (PackageManager.NameNotFoundException | IOException | XmlPullParserException e) {
            Log.e(TAG, "Error parsing auto fill service meta-data", e);
        }
        this.mSettingsActivity = settingsActivity;
        this.mCompatibilityPackages = compatibilityPackages;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0092, code lost:
        if (r2 != null) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00af, code lost:
        if (r2 == null) goto L9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized android.util.ArrayMap<java.lang.String, java.lang.Long> parseCompatibilityPackages(org.xmlpull.v1.XmlPullParser r13, android.content.res.Resources r14) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
            r12 = this;
            r0 = 0
            int r1 = r13.getDepth()
        L5:
            int r2 = r13.next()
            r3 = r2
            r4 = 1
            if (r2 == r4) goto Lda
            r2 = 3
            if (r3 != r2) goto L16
            int r5 = r13.getDepth()
            if (r5 <= r1) goto Lda
        L16:
            if (r3 == r2) goto L5
            r2 = 4
            if (r3 != r2) goto L1c
            goto L5
        L1c:
            java.lang.String r2 = "compatibility-package"
            java.lang.String r5 = r13.getName()
            boolean r2 = r2.equals(r5)
            if (r2 == 0) goto L5
            r2 = 0
            android.util.AttributeSet r5 = android.util.Xml.asAttributeSet(r13)     // Catch: java.lang.Throwable -> Ld0
            int[] r6 = com.android.internal.R.styleable.AutofillService_CompatibilityPackage     // Catch: java.lang.Throwable -> Ld0
            android.content.res.TypedArray r6 = r14.obtainAttributes(r5, r6)     // Catch: java.lang.Throwable -> Ld0
            r2 = r6
            r6 = 0
            java.lang.String r6 = r2.getString(r6)     // Catch: java.lang.Throwable -> Ld0
            boolean r7 = android.text.TextUtils.isEmpty(r6)     // Catch: java.lang.Throwable -> Ld0
            if (r7 == 0) goto L5f
            java.lang.String r4 = "AutofillServiceInfo"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld0
            r7.<init>()     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r8 = "Invalid compatibility package:"
            r7.append(r8)     // Catch: java.lang.Throwable -> Ld0
            r7.append(r6)     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> Ld0
            android.util.Log.e(r4, r7)     // Catch: java.lang.Throwable -> Ld0
            com.android.internal.util.XmlUtils.skipCurrentTag(r13)
            if (r2 == 0) goto Lda
        L5a:
            r2.recycle()
            goto Lda
        L5f:
            java.lang.String r4 = r2.getString(r4)     // Catch: java.lang.Throwable -> Ld0
            if (r4 == 0) goto Lb2
            long r7 = java.lang.Long.parseLong(r4)     // Catch: java.lang.NumberFormatException -> L95 java.lang.Throwable -> Ld0
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch: java.lang.NumberFormatException -> L95 java.lang.Throwable -> Ld0
            long r8 = r7.longValue()     // Catch: java.lang.Throwable -> Ld0
            r10 = 0
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 >= 0) goto Lbb
            java.lang.String r8 = "AutofillServiceInfo"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld0
            r9.<init>()     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r10 = "Invalid compatibility max version code:"
            r9.append(r10)     // Catch: java.lang.Throwable -> Ld0
            r9.append(r7)     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r9 = r9.toString()     // Catch: java.lang.Throwable -> Ld0
            android.util.Log.e(r8, r9)     // Catch: java.lang.Throwable -> Ld0
            com.android.internal.util.XmlUtils.skipCurrentTag(r13)
            if (r2 == 0) goto Lda
            goto L5a
        L95:
            r7 = move-exception
            java.lang.String r8 = "AutofillServiceInfo"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld0
            r9.<init>()     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r10 = "Invalid compatibility max version code:"
            r9.append(r10)     // Catch: java.lang.Throwable -> Ld0
            r9.append(r4)     // Catch: java.lang.Throwable -> Ld0
            java.lang.String r9 = r9.toString()     // Catch: java.lang.Throwable -> Ld0
            android.util.Log.e(r8, r9)     // Catch: java.lang.Throwable -> Ld0
            com.android.internal.util.XmlUtils.skipCurrentTag(r13)
            if (r2 == 0) goto Lda
            goto L5a
        Lb2:
            r7 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch: java.lang.Throwable -> Ld0
        Lbb:
            if (r0 != 0) goto Lc3
            android.util.ArrayMap r8 = new android.util.ArrayMap     // Catch: java.lang.Throwable -> Ld0
            r8.<init>()     // Catch: java.lang.Throwable -> Ld0
            r0 = r8
        Lc3:
            r0.put(r6, r7)     // Catch: java.lang.Throwable -> Ld0
            com.android.internal.util.XmlUtils.skipCurrentTag(r13)
            if (r2 == 0) goto Lce
            r2.recycle()
        Lce:
            goto L5
        Ld0:
            r4 = move-exception
            com.android.internal.util.XmlUtils.skipCurrentTag(r13)
            if (r2 == 0) goto Ld9
            r2.recycle()
        Ld9:
            throw r4
        Lda:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.service.autofill.AutofillServiceInfo.parseCompatibilityPackages(org.xmlpull.v1.XmlPullParser, android.content.res.Resources):android.util.ArrayMap");
    }

    public synchronized ServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public synchronized String getSettingsActivity() {
        return this.mSettingsActivity;
    }

    public synchronized ArrayMap<String, Long> getCompatibilityPackages() {
        return this.mCompatibilityPackages;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append("[");
        builder.append(this.mServiceInfo);
        builder.append(", settings:");
        builder.append(this.mSettingsActivity);
        builder.append(", hasCompatPckgs:");
        builder.append((this.mCompatibilityPackages == null || this.mCompatibilityPackages.isEmpty()) ? false : true);
        builder.append("]");
        return builder.toString();
    }

    public synchronized void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("Component: ");
        pw.println(getServiceInfo().getComponentName());
        pw.print(prefix);
        pw.print("Settings: ");
        pw.println(this.mSettingsActivity);
        pw.print(prefix);
        pw.print("Compat packages: ");
        pw.println(this.mCompatibilityPackages);
    }
}
