package android.telephony.mbms;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.telecom.Logging.Session;
import android.telephony.MbmsDownloadSession;
import android.telephony.MbmsGroupCallSession;
import android.telephony.MbmsStreamingSession;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.List;

/* loaded from: classes2.dex */
public class MbmsUtils {
    private static final String LOG_TAG = "MbmsUtils";

    public static boolean isContainedIn(File parent, File child) {
        try {
            String parentPath = parent.getCanonicalPath();
            String childPath = child.getCanonicalPath();
            return childPath.startsWith(parentPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to resolve canonical paths: " + e);
        }
    }

    public static ComponentName toComponentName(ComponentInfo ci) {
        return new ComponentName(ci.packageName, ci.name);
    }

    public static ComponentName getOverrideServiceName(Context context, String serviceAction) {
        char c;
        String serviceComponent;
        String metaDataKey = null;
        int hashCode = serviceAction.hashCode();
        if (hashCode == -1374878107) {
            if (serviceAction.equals(MbmsStreamingSession.MBMS_STREAMING_SERVICE_ACTION)) {
                c = 1;
            }
            c = 65535;
        } else if (hashCode != -407466459) {
            if (hashCode == 1752202112 && serviceAction.equals(MbmsGroupCallSession.MBMS_GROUP_CALL_SERVICE_ACTION)) {
                c = 2;
            }
            c = 65535;
        } else {
            if (serviceAction.equals(MbmsDownloadSession.MBMS_DOWNLOAD_SERVICE_ACTION)) {
                c = 0;
            }
            c = 65535;
        }
        if (c == 0) {
            metaDataKey = MbmsDownloadSession.MBMS_DOWNLOAD_SERVICE_OVERRIDE_METADATA;
        } else if (c == 1) {
            metaDataKey = MbmsStreamingSession.MBMS_STREAMING_SERVICE_OVERRIDE_METADATA;
        } else if (c == 2) {
            metaDataKey = MbmsGroupCallSession.MBMS_GROUP_CALL_SERVICE_OVERRIDE_METADATA;
        }
        if (metaDataKey == null) {
            return null;
        }
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (appInfo.metaData == null || (serviceComponent = appInfo.metaData.getString(metaDataKey)) == null) {
                return null;
            }
            return ComponentName.unflattenFromString(serviceComponent);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static android.content.pm.ServiceInfo getMiddlewareServiceInfo(Context context, String serviceAction) {
        List<ResolveInfo> services;
        PackageManager packageManager = context.getPackageManager();
        Intent queryIntent = new Intent();
        queryIntent.setAction(serviceAction);
        ComponentName overrideService = getOverrideServiceName(context, serviceAction);
        if (overrideService == null) {
            services = packageManager.queryIntentServices(queryIntent, 1048576);
        } else {
            queryIntent.setComponent(overrideService);
            services = packageManager.queryIntentServices(queryIntent, 131072);
        }
        if (services == null || services.size() == 0) {
            Log.w(LOG_TAG, "No MBMS services found, cannot get service info");
            return null;
        } else if (services.size() > 1) {
            Log.w(LOG_TAG, "More than one MBMS service found, cannot get unique service");
            return null;
        } else {
            return services.get(0).serviceInfo;
        }
    }

    public static int startBinding(Context context, String serviceAction, ServiceConnection serviceConnection) {
        Intent bindIntent = new Intent();
        android.content.pm.ServiceInfo mbmsServiceInfo = getMiddlewareServiceInfo(context, serviceAction);
        if (mbmsServiceInfo == null) {
            return 1;
        }
        bindIntent.setComponent(toComponentName(mbmsServiceInfo));
        context.bindService(bindIntent, serviceConnection, 1);
        return 0;
    }

    public static File getEmbmsTempFileDirForService(Context context, String serviceId) {
        String sanitizedServiceId = serviceId.replaceAll("[^a-zA-Z0-9_]", Session.SESSION_SEPARATION_CHAR_CHILD);
        File embmsTempFileDir = MbmsTempFileProvider.getEmbmsTempFileDir(context);
        return new File(embmsTempFileDir, sanitizedServiceId);
    }
}
