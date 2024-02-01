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
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.io.PrintWriter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes2.dex */
public final class AutofillServiceInfo {
    private static final String TAG = "AutofillServiceInfo";
    private static final String TAG_AUTOFILL_SERVICE = "autofill-service";
    private static final String TAG_COMPATIBILITY_PACKAGE = "compatibility-package";
    private final ArrayMap<String, Long> mCompatibilityPackages;
    private final ServiceInfo mServiceInfo;
    private final String mSettingsActivity;

    private static ServiceInfo getServiceInfoOrThrow(ComponentName comp, int userHandle) throws PackageManager.NameNotFoundException {
        try {
            ServiceInfo si = AppGlobals.getPackageManager().getServiceInfo(comp, 128, userHandle);
            if (si != null) {
                return si;
            }
        } catch (RemoteException e) {
        }
        throw new PackageManager.NameNotFoundException(comp.toString());
    }

    public AutofillServiceInfo(Context context, ComponentName comp, int userHandle) throws PackageManager.NameNotFoundException {
        this(context, getServiceInfoOrThrow(comp, userHandle));
    }

    public AutofillServiceInfo(Context context, ServiceInfo si) {
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
                afsAttributes.recycle();
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

    private ArrayMap<String, Long> parseCompatibilityPackages(XmlPullParser parser, Resources resources) throws IOException, XmlPullParserException {
        AttributeSet allAttributes;
        Long maxVersionCode;
        int outerDepth = parser.getDepth();
        ArrayMap<String, Long> compatibilityPackages = null;
        while (true) {
            int type = parser.next();
            if (type != 1 && (type != 3 || parser.getDepth() > outerDepth)) {
                if (type != 3 && type != 4 && TAG_COMPATIBILITY_PACKAGE.equals(parser.getName())) {
                    TypedArray cpAttributes = null;
                    try {
                        allAttributes = Xml.asAttributeSet(parser);
                    } catch (Throwable th) {
                        th = th;
                    }
                    try {
                        cpAttributes = resources.obtainAttributes(allAttributes, R.styleable.AutofillService_CompatibilityPackage);
                        String name = cpAttributes.getString(0);
                        if (TextUtils.isEmpty(name)) {
                            Log.e(TAG, "Invalid compatibility package:" + name);
                            XmlUtils.skipCurrentTag(parser);
                            cpAttributes.recycle();
                            break;
                        }
                        String maxVersionCodeStr = cpAttributes.getString(1);
                        if (maxVersionCodeStr != null) {
                            try {
                                maxVersionCode = Long.valueOf(Long.parseLong(maxVersionCodeStr));
                                if (maxVersionCode.longValue() < 0) {
                                    Log.e(TAG, "Invalid compatibility max version code:" + maxVersionCode);
                                    XmlUtils.skipCurrentTag(parser);
                                    cpAttributes.recycle();
                                    break;
                                }
                            } catch (NumberFormatException e) {
                                Log.e(TAG, "Invalid compatibility max version code:" + maxVersionCodeStr);
                                XmlUtils.skipCurrentTag(parser);
                                cpAttributes.recycle();
                            }
                        } else {
                            maxVersionCode = Long.MAX_VALUE;
                        }
                        if (compatibilityPackages == null) {
                            compatibilityPackages = new ArrayMap<>();
                        }
                        compatibilityPackages.put(name, maxVersionCode);
                        XmlUtils.skipCurrentTag(parser);
                        cpAttributes.recycle();
                    } catch (Throwable th2) {
                        th = th2;
                        XmlUtils.skipCurrentTag(parser);
                        if (cpAttributes != null) {
                            cpAttributes.recycle();
                        }
                        throw th;
                    }
                }
            }
        }
        return compatibilityPackages;
    }

    public ServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public String getSettingsActivity() {
        return this.mSettingsActivity;
    }

    public ArrayMap<String, Long> getCompatibilityPackages() {
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
        ArrayMap<String, Long> arrayMap = this.mCompatibilityPackages;
        builder.append((arrayMap == null || arrayMap.isEmpty()) ? false : true);
        builder.append("]");
        return builder.toString();
    }

    public void dump(String prefix, PrintWriter pw) {
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
