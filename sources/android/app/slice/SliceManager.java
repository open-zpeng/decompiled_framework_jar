package android.app.slice;

import android.app.slice.ISliceManager;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class SliceManager {
    public static final String ACTION_REQUEST_SLICE_PERMISSION = "com.android.intent.action.REQUEST_SLICE_PERMISSION";
    public static final String CATEGORY_SLICE = "android.app.slice.category.SLICE";
    public static final String SLICE_METADATA_KEY = "android.metadata.SLICE_URI";
    private static final String TAG = "SliceManager";
    private final Context mContext;
    private final IBinder mToken = new Binder();
    private final ISliceManager mService = ISliceManager.Stub.asInterface(ServiceManager.getServiceOrThrow("slice"));

    public SliceManager(Context context, Handler handler) throws ServiceManager.ServiceNotFoundException {
        this.mContext = context;
    }

    public void pinSlice(Uri uri, Set<SliceSpec> specs) {
        try {
            this.mService.pinSlice(this.mContext.getPackageName(), uri, (SliceSpec[]) specs.toArray(new SliceSpec[specs.size()]), this.mToken);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void pinSlice(Uri uri, List<SliceSpec> specs) {
        pinSlice(uri, new ArraySet(specs));
    }

    public void unpinSlice(Uri uri) {
        try {
            this.mService.unpinSlice(this.mContext.getPackageName(), uri, this.mToken);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hasSliceAccess() {
        try {
            return this.mService.hasSliceAccess(this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Set<SliceSpec> getPinnedSpecs(Uri uri) {
        try {
            return new ArraySet(Arrays.asList(this.mService.getPinnedSpecs(uri, this.mContext.getPackageName())));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<Uri> getPinnedSlices() {
        try {
            return Arrays.asList(this.mService.getPinnedSlices(this.mContext.getPackageName()));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Collection<Uri> getSliceDescendants(Uri uri) {
        ContentResolver resolver = this.mContext.getContentResolver();
        try {
            ContentProviderClient provider = resolver.acquireUnstableContentProviderClient(uri);
            Bundle extras = new Bundle();
            extras.putParcelable("slice_uri", uri);
            Bundle res = provider.call(SliceProvider.METHOD_GET_DESCENDANTS, null, extras);
            ArrayList parcelableArrayList = res.getParcelableArrayList(SliceProvider.EXTRA_SLICE_DESCENDANTS);
            $closeResource(null, provider);
            return parcelableArrayList;
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to get slice descendants", e);
            return Collections.emptyList();
        }
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    public Slice bindSlice(Uri uri, Set<SliceSpec> supportedSpecs) {
        Preconditions.checkNotNull(uri, "uri");
        ContentResolver resolver = this.mContext.getContentResolver();
        try {
            ContentProviderClient provider = resolver.acquireUnstableContentProviderClient(uri);
            if (provider == null) {
                Log.w(TAG, String.format("Unknown URI: %s", uri));
                if (provider != null) {
                    $closeResource(null, provider);
                }
                return null;
            }
            Bundle extras = new Bundle();
            extras.putParcelable("slice_uri", uri);
            extras.putParcelableArrayList(SliceProvider.EXTRA_SUPPORTED_SPECS, new ArrayList<>(supportedSpecs));
            Bundle res = provider.call(SliceProvider.METHOD_SLICE, null, extras);
            Bundle.setDefusable(res, true);
            if (res == null) {
                $closeResource(null, provider);
                return null;
            }
            Slice slice = (Slice) res.getParcelable("slice");
            $closeResource(null, provider);
            return slice;
        } catch (RemoteException e) {
            return null;
        }
    }

    @Deprecated
    public Slice bindSlice(Uri uri, List<SliceSpec> supportedSpecs) {
        return bindSlice(uri, new ArraySet(supportedSpecs));
    }

    public Uri mapIntentToUri(Intent intent) {
        ContentResolver resolver = this.mContext.getContentResolver();
        Uri staticUri = resolveStatic(intent, resolver);
        if (staticUri != null) {
            return staticUri;
        }
        String authority = getAuthority(intent);
        if (authority == null) {
            return null;
        }
        Uri uri = new Uri.Builder().scheme("content").authority(authority).build();
        try {
            ContentProviderClient provider = resolver.acquireUnstableContentProviderClient(uri);
            if (provider == null) {
                Log.w(TAG, String.format("Unknown URI: %s", uri));
                if (provider != null) {
                    $closeResource(null, provider);
                }
                return null;
            }
            Bundle extras = new Bundle();
            extras.putParcelable(SliceProvider.EXTRA_INTENT, intent);
            Bundle res = provider.call(SliceProvider.METHOD_MAP_ONLY_INTENT, null, extras);
            if (res == null) {
                $closeResource(null, provider);
                return null;
            }
            Uri uri2 = (Uri) res.getParcelable("slice");
            $closeResource(null, provider);
            return uri2;
        } catch (RemoteException e) {
            return null;
        }
    }

    private String getAuthority(Intent intent) {
        Intent queryIntent = new Intent(intent);
        if (!queryIntent.hasCategory(CATEGORY_SLICE)) {
            queryIntent.addCategory(CATEGORY_SLICE);
        }
        List<ResolveInfo> providers = this.mContext.getPackageManager().queryIntentContentProviders(queryIntent, 0);
        if (providers == null || providers.isEmpty()) {
            return null;
        }
        return providers.get(0).providerInfo.authority;
    }

    private Uri resolveStatic(Intent intent, ContentResolver resolver) {
        Preconditions.checkNotNull(intent, "intent");
        Preconditions.checkArgument((intent.getComponent() == null && intent.getPackage() == null && intent.getData() == null) ? false : true, "Slice intent must be explicit %s", intent);
        Uri intentData = intent.getData();
        if (intentData != null && SliceProvider.SLICE_TYPE.equals(resolver.getType(intentData))) {
            return intentData;
        }
        ResolveInfo resolve = this.mContext.getPackageManager().resolveActivity(intent, 128);
        if (resolve != null && resolve.activityInfo != null && resolve.activityInfo.metaData != null && resolve.activityInfo.metaData.containsKey(SLICE_METADATA_KEY)) {
            return Uri.parse(resolve.activityInfo.metaData.getString(SLICE_METADATA_KEY));
        }
        return null;
    }

    public Slice bindSlice(Intent intent, Set<SliceSpec> supportedSpecs) {
        Preconditions.checkNotNull(intent, "intent");
        Preconditions.checkArgument((intent.getComponent() == null && intent.getPackage() == null && intent.getData() == null) ? false : true, "Slice intent must be explicit %s", intent);
        ContentResolver resolver = this.mContext.getContentResolver();
        Uri staticUri = resolveStatic(intent, resolver);
        if (staticUri != null) {
            return bindSlice(staticUri, supportedSpecs);
        }
        String authority = getAuthority(intent);
        if (authority == null) {
            return null;
        }
        Uri uri = new Uri.Builder().scheme("content").authority(authority).build();
        try {
            ContentProviderClient provider = resolver.acquireUnstableContentProviderClient(uri);
            if (provider == null) {
                Log.w(TAG, String.format("Unknown URI: %s", uri));
                if (provider != null) {
                    $closeResource(null, provider);
                }
                return null;
            }
            Bundle extras = new Bundle();
            extras.putParcelable(SliceProvider.EXTRA_INTENT, intent);
            extras.putParcelableArrayList(SliceProvider.EXTRA_SUPPORTED_SPECS, new ArrayList<>(supportedSpecs));
            Bundle res = provider.call(SliceProvider.METHOD_MAP_INTENT, null, extras);
            if (res == null) {
                $closeResource(null, provider);
                return null;
            }
            Slice slice = (Slice) res.getParcelable("slice");
            $closeResource(null, provider);
            return slice;
        } catch (RemoteException e) {
            return null;
        }
    }

    @Deprecated
    public Slice bindSlice(Intent intent, List<SliceSpec> supportedSpecs) {
        return bindSlice(intent, new ArraySet(supportedSpecs));
    }

    public int checkSlicePermission(Uri uri, int pid, int uid) {
        try {
            return this.mService.checkSlicePermission(uri, this.mContext.getPackageName(), null, pid, uid, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void grantSlicePermission(String toPackage, Uri uri) {
        try {
            this.mService.grantSlicePermission(this.mContext.getPackageName(), toPackage, uri);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void revokeSlicePermission(String toPackage, Uri uri) {
        try {
            this.mService.revokeSlicePermission(this.mContext.getPackageName(), toPackage, uri);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void enforceSlicePermission(Uri uri, String pkg, int pid, int uid, String[] autoGrantPermissions) {
        try {
            if (UserHandle.isSameApp(uid, Process.myUid())) {
                return;
            }
            if (pkg == null) {
                throw new SecurityException("No pkg specified");
            }
            int result = this.mService.checkSlicePermission(uri, this.mContext.getPackageName(), pkg, pid, uid, autoGrantPermissions);
            if (result == -1) {
                throw new SecurityException("User " + uid + " does not have slice permission for " + uri + ".");
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void grantPermissionFromUser(Uri uri, String pkg, boolean allSlices) {
        try {
            this.mService.grantPermissionFromUser(uri, pkg, this.mContext.getPackageName(), allSlices);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
