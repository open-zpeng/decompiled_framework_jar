package android.content;

import android.content.pm.RegisteredServicesCache;
import android.content.pm.XmlSerializerAndParser;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.SparseArray;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: classes.dex */
public class SyncAdaptersCache extends RegisteredServicesCache<SyncAdapterType> {
    private static final String ATTRIBUTES_NAME = "sync-adapter";
    private static final String SERVICE_INTERFACE = "android.content.SyncAdapter";
    private static final String SERVICE_META_DATA = "android.content.SyncAdapter";
    private static final String TAG = "Account";
    private static final MySerializer sSerializer = new MySerializer();
    @GuardedBy("mServicesLock")
    private SparseArray<ArrayMap<String, String[]>> mAuthorityToSyncAdapters;

    private protected SyncAdaptersCache(Context context) {
        super(context, "android.content.SyncAdapter", "android.content.SyncAdapter", ATTRIBUTES_NAME, sSerializer);
        this.mAuthorityToSyncAdapters = new SparseArray<>();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.content.pm.RegisteredServicesCache
    public synchronized SyncAdapterType parseServiceAttributes(Resources res, String packageName, AttributeSet attrs) {
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.SyncAdapter);
        try {
            String authority = sa.getString(2);
            String accountType = sa.getString(1);
            if (!TextUtils.isEmpty(authority) && !TextUtils.isEmpty(accountType)) {
                boolean userVisible = sa.getBoolean(3, true);
                boolean supportsUploading = sa.getBoolean(4, true);
                boolean isAlwaysSyncable = sa.getBoolean(6, false);
                boolean allowParallelSyncs = sa.getBoolean(5, false);
                String settingsActivity = sa.getString(0);
                return new SyncAdapterType(authority, accountType, userVisible, supportsUploading, isAlwaysSyncable, allowParallelSyncs, settingsActivity, packageName);
            }
            return null;
        } finally {
            sa.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.content.pm.RegisteredServicesCache
    public synchronized void onServicesChangedLocked(int userId) {
        synchronized (this.mServicesLock) {
            ArrayMap<String, String[]> adapterMap = this.mAuthorityToSyncAdapters.get(userId);
            if (adapterMap != null) {
                adapterMap.clear();
            }
        }
        super.onServicesChangedLocked(userId);
    }

    public synchronized String[] getSyncAdapterPackagesForAuthority(String authority, int userId) {
        synchronized (this.mServicesLock) {
            ArrayMap<String, String[]> adapterMap = this.mAuthorityToSyncAdapters.get(userId);
            if (adapterMap == null) {
                adapterMap = new ArrayMap<>();
                this.mAuthorityToSyncAdapters.put(userId, adapterMap);
            }
            if (adapterMap.containsKey(authority)) {
                return adapterMap.get(authority);
            }
            Collection<RegisteredServicesCache.ServiceInfo<SyncAdapterType>> serviceInfos = getAllServices(userId);
            ArrayList<String> packages = new ArrayList<>();
            for (RegisteredServicesCache.ServiceInfo<SyncAdapterType> serviceInfo : serviceInfos) {
                if (authority.equals(((RegisteredServicesCache.ServiceInfo) serviceInfo).type.authority) && ((RegisteredServicesCache.ServiceInfo) serviceInfo).componentName != null) {
                    packages.add(((RegisteredServicesCache.ServiceInfo) serviceInfo).componentName.getPackageName());
                }
            }
            String[] syncAdapterPackages = new String[packages.size()];
            packages.toArray(syncAdapterPackages);
            adapterMap.put(authority, syncAdapterPackages);
            return syncAdapterPackages;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.content.pm.RegisteredServicesCache
    public synchronized void onUserRemoved(int userId) {
        synchronized (this.mServicesLock) {
            this.mAuthorityToSyncAdapters.remove(userId);
        }
        super.onUserRemoved(userId);
    }

    /* loaded from: classes.dex */
    static class MySerializer implements XmlSerializerAndParser<SyncAdapterType> {
        synchronized MySerializer() {
        }

        public synchronized void writeAsXml(SyncAdapterType item, XmlSerializer out) throws IOException {
            out.attribute(null, ContactsContract.Directory.DIRECTORY_AUTHORITY, item.authority);
            out.attribute(null, "accountType", item.accountType);
        }

        /* renamed from: createFromXml */
        public synchronized SyncAdapterType m18createFromXml(XmlPullParser parser) throws IOException, XmlPullParserException {
            String authority = parser.getAttributeValue(null, ContactsContract.Directory.DIRECTORY_AUTHORITY);
            String accountType = parser.getAttributeValue(null, "accountType");
            return SyncAdapterType.newKey(authority, accountType);
        }
    }
}
