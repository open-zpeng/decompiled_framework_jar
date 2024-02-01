package android.content;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.UnsupportedAppUsage;
import android.app.AppOpsManager;
import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes.dex */
public abstract class ContentProvider implements ContentInterface, ComponentCallbacks2 {
    private static final String TAG = "ContentProvider";
    @UnsupportedAppUsage
    private String[] mAuthorities;
    @UnsupportedAppUsage
    private String mAuthority;
    private ThreadLocal<String> mCallingPackage;
    @UnsupportedAppUsage
    private Context mContext;
    private boolean mExported;
    private int mMyUid;
    private boolean mNoPerms;
    @UnsupportedAppUsage
    private PathPermission[] mPathPermissions;
    @UnsupportedAppUsage
    private String mReadPermission;
    private boolean mSingleUser;
    private Transport mTransport;
    @UnsupportedAppUsage
    private String mWritePermission;

    /* loaded from: classes.dex */
    public interface PipeDataWriter<T> {
        void writeDataToPipe(ParcelFileDescriptor parcelFileDescriptor, Uri uri, String str, Bundle bundle, T t);
    }

    @Override // android.content.ContentInterface
    public abstract int delete(Uri uri, String str, String[] strArr);

    @Override // android.content.ContentInterface
    public abstract String getType(Uri uri);

    @Override // android.content.ContentInterface
    public abstract Uri insert(Uri uri, ContentValues contentValues);

    public abstract boolean onCreate();

    public abstract Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2);

    @Override // android.content.ContentInterface
    public abstract int update(Uri uri, ContentValues contentValues, String str, String[] strArr);

    public ContentProvider() {
        this.mContext = null;
        this.mTransport = new Transport();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public ContentProvider(Context context, String readPermission, String writePermission, PathPermission[] pathPermissions) {
        this.mContext = null;
        this.mTransport = new Transport();
        this.mContext = context;
        this.mReadPermission = readPermission;
        this.mWritePermission = writePermission;
        this.mPathPermissions = pathPermissions;
    }

    @UnsupportedAppUsage
    public static ContentProvider coerceToLocalContentProvider(IContentProvider abstractInterface) {
        if (abstractInterface instanceof Transport) {
            return ((Transport) abstractInterface).getContentProvider();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Transport extends ContentProviderNative {
        volatile ContentInterface mInterface;
        volatile AppOpsManager mAppOpsManager = null;
        volatile int mReadOp = -1;
        volatile int mWriteOp = -1;

        Transport() {
            this.mInterface = ContentProvider.this;
        }

        ContentProvider getContentProvider() {
            return ContentProvider.this;
        }

        @Override // android.content.ContentProviderNative
        public String getProviderName() {
            return getContentProvider().getClass().getName();
        }

        @Override // android.content.IContentProvider
        public Cursor query(String callingPkg, Uri uri, String[] projection, Bundle queryArgs, ICancellationSignal cancellationSignal) {
            Uri uri2 = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            if (enforceReadPermission(callingPkg, uri2, null) == 0) {
                Trace.traceBegin(1048576L, "query");
                String original = ContentProvider.this.setCallingPackage(callingPkg);
                try {
                    try {
                        return this.mInterface.query(uri2, projection, queryArgs, CancellationSignal.fromTransport(cancellationSignal));
                    } catch (RemoteException e) {
                        throw e.rethrowAsRuntimeException();
                    }
                } finally {
                    ContentProvider.this.setCallingPackage(original);
                    Trace.traceEnd(1048576L);
                }
            } else if (projection == null) {
                String original2 = ContentProvider.this.setCallingPackage(callingPkg);
                try {
                    try {
                        Cursor cursor = this.mInterface.query(uri2, projection, queryArgs, CancellationSignal.fromTransport(cancellationSignal));
                        if (cursor == null) {
                            return null;
                        }
                        return new MatrixCursor(cursor.getColumnNames(), 0);
                    } finally {
                        ContentProvider.this.setCallingPackage(original2);
                    }
                } catch (RemoteException e2) {
                    throw e2.rethrowAsRuntimeException();
                }
            } else {
                return new MatrixCursor(projection, 0);
            }
        }

        @Override // android.content.IContentProvider
        public String getType(Uri uri) {
            Uri uri2 = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            Trace.traceBegin(1048576L, "getType");
            try {
                try {
                    return this.mInterface.getType(uri2);
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public Uri insert(String callingPkg, Uri uri, ContentValues initialValues) {
            Uri uri2 = ContentProvider.this.validateIncomingUri(uri);
            int userId = ContentProvider.getUserIdFromUri(uri2);
            Uri uri3 = ContentProvider.this.maybeGetUriWithoutUserId(uri2);
            if (enforceWritePermission(callingPkg, uri3, null) != 0) {
                String original = ContentProvider.this.setCallingPackage(callingPkg);
                try {
                    return ContentProvider.this.rejectInsert(uri3, initialValues);
                } finally {
                    ContentProvider.this.setCallingPackage(original);
                }
            }
            Trace.traceBegin(1048576L, "insert");
            String original2 = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                try {
                    return ContentProvider.maybeAddUserId(this.mInterface.insert(uri3, initialValues), userId);
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                ContentProvider.this.setCallingPackage(original2);
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public int bulkInsert(String callingPkg, Uri uri, ContentValues[] initialValues) {
            Uri uri2 = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            if (enforceWritePermission(callingPkg, uri2, null) != 0) {
                return 0;
            }
            Trace.traceBegin(1048576L, "bulkInsert");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                try {
                    return this.mInterface.bulkInsert(uri2, initialValues);
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public ContentProviderResult[] applyBatch(String callingPkg, String authority, ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
            ContentProvider.this.validateIncomingAuthority(authority);
            int numOperations = operations.size();
            int[] userIds = new int[numOperations];
            int i = 0;
            while (true) {
                if (i < numOperations) {
                    ContentProviderOperation operation = operations.get(i);
                    Uri uri = operation.getUri();
                    userIds[i] = ContentProvider.getUserIdFromUri(uri);
                    Uri uri2 = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
                    if (!Objects.equals(operation.getUri(), uri2)) {
                        operation = new ContentProviderOperation(operation, uri2);
                        operations.set(i, operation);
                    }
                    if (operation.isReadOperation() && enforceReadPermission(callingPkg, uri2, null) != 0) {
                        throw new OperationApplicationException("App op not allowed", 0);
                    }
                    if (!operation.isWriteOperation() || enforceWritePermission(callingPkg, uri2, null) == 0) {
                        i++;
                    } else {
                        throw new OperationApplicationException("App op not allowed", 0);
                    }
                } else {
                    Trace.traceBegin(1048576L, "applyBatch");
                    String original = ContentProvider.this.setCallingPackage(callingPkg);
                    try {
                        try {
                            ContentProviderResult[] results = this.mInterface.applyBatch(authority, operations);
                            if (results != null) {
                                for (int i2 = 0; i2 < results.length; i2++) {
                                    if (userIds[i2] != -2) {
                                        results[i2] = new ContentProviderResult(results[i2], userIds[i2]);
                                    }
                                }
                            }
                            return results;
                        } catch (RemoteException e) {
                            throw e.rethrowAsRuntimeException();
                        }
                    } finally {
                        ContentProvider.this.setCallingPackage(original);
                        Trace.traceEnd(1048576L);
                    }
                }
            }
        }

        @Override // android.content.IContentProvider
        public int delete(String callingPkg, Uri uri, String selection, String[] selectionArgs) {
            Uri uri2 = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            if (enforceWritePermission(callingPkg, uri2, null) != 0) {
                return 0;
            }
            Trace.traceBegin(1048576L, "delete");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                try {
                    return this.mInterface.delete(uri2, selection, selectionArgs);
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public int update(String callingPkg, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            Uri uri2 = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            if (enforceWritePermission(callingPkg, uri2, null) != 0) {
                return 0;
            }
            Trace.traceBegin(1048576L, AccountManager.USER_DATA_EXTRA_UPDATE);
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                try {
                    return this.mInterface.update(uri2, values, selection, selectionArgs);
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public ParcelFileDescriptor openFile(String callingPkg, Uri uri, String mode, ICancellationSignal cancellationSignal, IBinder callerToken) throws FileNotFoundException {
            Uri uri2 = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            enforceFilePermission(callingPkg, uri2, mode, callerToken);
            Trace.traceBegin(1048576L, "openFile");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                try {
                    return this.mInterface.openFile(uri2, mode, CancellationSignal.fromTransport(cancellationSignal));
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public AssetFileDescriptor openAssetFile(String callingPkg, Uri uri, String mode, ICancellationSignal cancellationSignal) throws FileNotFoundException {
            Uri uri2 = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            enforceFilePermission(callingPkg, uri2, mode, null);
            Trace.traceBegin(1048576L, "openAssetFile");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                try {
                    return this.mInterface.openAssetFile(uri2, mode, CancellationSignal.fromTransport(cancellationSignal));
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public Bundle call(String callingPkg, String authority, String method, String arg, Bundle extras) {
            ContentProvider.this.validateIncomingAuthority(authority);
            Bundle.setDefusable(extras, true);
            Trace.traceBegin(1048576L, "call");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                try {
                    return this.mInterface.call(authority, method, arg, extras);
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public String[] getStreamTypes(Uri uri, String mimeTypeFilter) {
            Uri uri2 = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            Trace.traceBegin(1048576L, "getStreamTypes");
            try {
                try {
                    return this.mInterface.getStreamTypes(uri2, mimeTypeFilter);
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public AssetFileDescriptor openTypedAssetFile(String callingPkg, Uri uri, String mimeType, Bundle opts, ICancellationSignal cancellationSignal) throws FileNotFoundException {
            Bundle.setDefusable(opts, true);
            Uri uri2 = ContentProvider.this.maybeGetUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            enforceFilePermission(callingPkg, uri2, "r", null);
            Trace.traceBegin(1048576L, "openTypedAssetFile");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                try {
                    return this.mInterface.openTypedAssetFile(uri2, mimeType, opts, CancellationSignal.fromTransport(cancellationSignal));
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public ICancellationSignal createCancellationSignal() {
            return CancellationSignal.createTransport();
        }

        @Override // android.content.IContentProvider
        public Uri canonicalize(String callingPkg, Uri uri) {
            Uri uri2 = ContentProvider.this.validateIncomingUri(uri);
            int userId = ContentProvider.getUserIdFromUri(uri2);
            Uri uri3 = ContentProvider.getUriWithoutUserId(uri2);
            if (enforceReadPermission(callingPkg, uri3, null) != 0) {
                return null;
            }
            Trace.traceBegin(1048576L, "canonicalize");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                try {
                    return ContentProvider.maybeAddUserId(this.mInterface.canonicalize(uri3), userId);
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public Uri uncanonicalize(String callingPkg, Uri uri) {
            Uri uri2 = ContentProvider.this.validateIncomingUri(uri);
            int userId = ContentProvider.getUserIdFromUri(uri2);
            Uri uri3 = ContentProvider.getUriWithoutUserId(uri2);
            if (enforceReadPermission(callingPkg, uri3, null) != 0) {
                return null;
            }
            Trace.traceBegin(1048576L, "uncanonicalize");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                try {
                    return ContentProvider.maybeAddUserId(this.mInterface.uncanonicalize(uri3), userId);
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576L);
            }
        }

        @Override // android.content.IContentProvider
        public boolean refresh(String callingPkg, Uri uri, Bundle args, ICancellationSignal cancellationSignal) throws RemoteException {
            Uri uri2 = ContentProvider.getUriWithoutUserId(ContentProvider.this.validateIncomingUri(uri));
            if (enforceReadPermission(callingPkg, uri2, null) != 0) {
                return false;
            }
            Trace.traceBegin(1048576L, "refresh");
            String original = ContentProvider.this.setCallingPackage(callingPkg);
            try {
                return this.mInterface.refresh(uri2, args, CancellationSignal.fromTransport(cancellationSignal));
            } finally {
                ContentProvider.this.setCallingPackage(original);
                Trace.traceEnd(1048576L);
            }
        }

        private void enforceFilePermission(String callingPkg, Uri uri, String mode, IBinder callerToken) throws FileNotFoundException, SecurityException {
            if (mode != null && mode.indexOf(119) != -1) {
                if (enforceWritePermission(callingPkg, uri, callerToken) != 0) {
                    throw new FileNotFoundException("App op not allowed");
                }
            } else if (enforceReadPermission(callingPkg, uri, callerToken) != 0) {
                throw new FileNotFoundException("App op not allowed");
            }
        }

        private int enforceReadPermission(String callingPkg, Uri uri, IBinder callerToken) throws SecurityException {
            int mode = ContentProvider.this.enforceReadPermissionInner(uri, callingPkg, callerToken);
            if (mode != 0) {
                return mode;
            }
            return noteProxyOp(callingPkg, this.mReadOp);
        }

        private int enforceWritePermission(String callingPkg, Uri uri, IBinder callerToken) throws SecurityException {
            int mode = ContentProvider.this.enforceWritePermissionInner(uri, callingPkg, callerToken);
            if (mode != 0) {
                return mode;
            }
            return noteProxyOp(callingPkg, this.mWriteOp);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int noteProxyOp(String callingPkg, int op) {
            if (op != -1) {
                int mode = this.mAppOpsManager.noteProxyOp(op, callingPkg);
                if (mode == 3) {
                    return 1;
                }
                return mode;
            }
            return 0;
        }
    }

    boolean checkUser(int pid, int uid, Context context) {
        return UserHandle.getUserId(uid) == context.getUserId() || this.mSingleUser || context.checkPermission(Manifest.permission.INTERACT_ACROSS_USERS, pid, uid) == 0;
    }

    private int checkPermissionAndAppOp(String permission, String callingPkg, IBinder callerToken) {
        if (getContext().checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid(), callerToken) == 0) {
            return this.mTransport.noteProxyOp(callingPkg, AppOpsManager.permissionToOpCode(permission));
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int enforceReadPermissionInner(Uri uri, String callingPkg, IBinder callerToken) throws SecurityException {
        String missingPerm;
        int strongestMode;
        String suffix;
        boolean allowDefaultRead;
        String missingPerm2;
        Context context = getContext();
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String missingPerm3 = null;
        int strongestMode2 = 0;
        if (UserHandle.isSameApp(uid, this.mMyUid)) {
            return 0;
        }
        if (this.mExported && checkUser(pid, uid, context)) {
            String componentPerm = getReadPermission();
            if (componentPerm != null) {
                int mode = checkPermissionAndAppOp(componentPerm, callingPkg, callerToken);
                if (mode == 0) {
                    return 0;
                }
                missingPerm3 = componentPerm;
                strongestMode2 = Math.max(0, mode);
            }
            boolean allowDefaultRead2 = componentPerm == null;
            PathPermission[] pps = getPathPermissions();
            if (pps == null) {
                allowDefaultRead = allowDefaultRead2;
            } else {
                String path = uri.getPath();
                allowDefaultRead = allowDefaultRead2;
                int strongestMode3 = strongestMode2;
                String missingPerm4 = missingPerm3;
                for (PathPermission pp : pps) {
                    String pathPerm = pp.getReadPermission();
                    if (pathPerm != null && pp.match(path)) {
                        int mode2 = checkPermissionAndAppOp(pathPerm, callingPkg, callerToken);
                        if (mode2 == 0) {
                            return 0;
                        }
                        allowDefaultRead = false;
                        missingPerm2 = pathPerm;
                        strongestMode3 = Math.max(strongestMode3, mode2);
                    } else {
                        missingPerm2 = missingPerm4;
                    }
                    missingPerm4 = missingPerm2;
                }
                String missingPerm5 = missingPerm4;
                strongestMode2 = strongestMode3;
                missingPerm3 = missingPerm5;
            }
            if (allowDefaultRead) {
                return 0;
            }
            missingPerm = missingPerm3;
            strongestMode = strongestMode2;
        } else {
            missingPerm = null;
            strongestMode = 0;
        }
        int callingUserId = UserHandle.getUserId(uid);
        Uri userUri = (!this.mSingleUser || UserHandle.isSameUser(this.mMyUid, uid)) ? uri : maybeAddUserId(uri, callingUserId);
        if (context.checkUriPermission(userUri, pid, uid, 1, callerToken) == 0) {
            return 0;
        }
        if (strongestMode == 1) {
            return 1;
        }
        if (Manifest.permission.MANAGE_DOCUMENTS.equals(this.mReadPermission)) {
            suffix = " requires that you obtain access using ACTION_OPEN_DOCUMENT or related APIs";
        } else if (this.mExported) {
            suffix = " requires " + missingPerm + ", or grantUriPermission()";
        } else {
            suffix = " requires the provider be exported, or grantUriPermission()";
        }
        throw new SecurityException("Permission Denial: reading " + getClass().getName() + " uri " + uri + " from pid=" + pid + ", uid=" + uid + suffix);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int enforceWritePermissionInner(Uri uri, String callingPkg, IBinder callerToken) throws SecurityException {
        String missingPerm;
        int strongestMode;
        boolean allowDefaultWrite;
        String missingPerm2;
        Context context = getContext();
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();
        String missingPerm3 = null;
        int strongestMode2 = 0;
        if (UserHandle.isSameApp(uid, this.mMyUid)) {
            return 0;
        }
        if (this.mExported && checkUser(pid, uid, context)) {
            String componentPerm = getWritePermission();
            if (componentPerm != null) {
                int mode = checkPermissionAndAppOp(componentPerm, callingPkg, callerToken);
                if (mode == 0) {
                    return 0;
                }
                missingPerm3 = componentPerm;
                strongestMode2 = Math.max(0, mode);
            }
            boolean allowDefaultWrite2 = componentPerm == null;
            PathPermission[] pps = getPathPermissions();
            if (pps == null) {
                allowDefaultWrite = allowDefaultWrite2;
            } else {
                String path = uri.getPath();
                allowDefaultWrite = allowDefaultWrite2;
                int strongestMode3 = strongestMode2;
                String missingPerm4 = missingPerm3;
                for (PathPermission pp : pps) {
                    String pathPerm = pp.getWritePermission();
                    if (pathPerm != null && pp.match(path)) {
                        int mode2 = checkPermissionAndAppOp(pathPerm, callingPkg, callerToken);
                        if (mode2 == 0) {
                            return 0;
                        }
                        allowDefaultWrite = false;
                        missingPerm2 = pathPerm;
                        strongestMode3 = Math.max(strongestMode3, mode2);
                    } else {
                        missingPerm2 = missingPerm4;
                    }
                    missingPerm4 = missingPerm2;
                }
                String missingPerm5 = missingPerm4;
                strongestMode2 = strongestMode3;
                missingPerm3 = missingPerm5;
            }
            if (allowDefaultWrite) {
                return 0;
            }
            missingPerm = missingPerm3;
            strongestMode = strongestMode2;
        } else {
            missingPerm = null;
            strongestMode = 0;
        }
        if (context.checkUriPermission(uri, pid, uid, 2, callerToken) == 0) {
            return 0;
        }
        if (strongestMode == 1) {
            return 1;
        }
        String failReason = this.mExported ? " requires " + missingPerm + ", or grantUriPermission()" : " requires the provider be exported, or grantUriPermission()";
        throw new SecurityException("Permission Denial: writing " + getClass().getName() + " uri " + uri + " from pid=" + pid + ", uid=" + uid + failReason);
    }

    public final Context getContext() {
        return this.mContext;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String setCallingPackage(String callingPackage) {
        String original = this.mCallingPackage.get();
        this.mCallingPackage.set(callingPackage);
        onCallingPackageChanged();
        return original;
    }

    public final String getCallingPackage() {
        String pkg = this.mCallingPackage.get();
        if (pkg != null) {
            this.mTransport.mAppOpsManager.checkPackage(Binder.getCallingUid(), pkg);
        }
        return pkg;
    }

    public final String getCallingPackageUnchecked() {
        return this.mCallingPackage.get();
    }

    public void onCallingPackageChanged() {
    }

    /* loaded from: classes.dex */
    public final class CallingIdentity {
        public final long binderToken;
        public final String callingPackage;

        public CallingIdentity(long binderToken, String callingPackage) {
            this.binderToken = binderToken;
            this.callingPackage = callingPackage;
        }
    }

    public final CallingIdentity clearCallingIdentity() {
        return new CallingIdentity(Binder.clearCallingIdentity(), setCallingPackage(null));
    }

    public final void restoreCallingIdentity(CallingIdentity identity) {
        Binder.restoreCallingIdentity(identity.binderToken);
        this.mCallingPackage.set(identity.callingPackage);
    }

    protected final void setAuthorities(String authorities) {
        if (authorities != null) {
            if (authorities.indexOf(59) == -1) {
                this.mAuthority = authorities;
                this.mAuthorities = null;
                return;
            }
            this.mAuthority = null;
            this.mAuthorities = authorities.split(";");
        }
    }

    protected final boolean matchesOurAuthorities(String authority) {
        String str = this.mAuthority;
        if (str != null) {
            return str.equals(authority);
        }
        String[] strArr = this.mAuthorities;
        if (strArr != null) {
            int length = strArr.length;
            for (int i = 0; i < length; i++) {
                if (this.mAuthorities[i].equals(authority)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    protected final void setReadPermission(String permission) {
        this.mReadPermission = permission;
    }

    public final String getReadPermission() {
        return this.mReadPermission;
    }

    protected final void setWritePermission(String permission) {
        this.mWritePermission = permission;
    }

    public final String getWritePermission() {
        return this.mWritePermission;
    }

    protected final void setPathPermissions(PathPermission[] permissions) {
        this.mPathPermissions = permissions;
    }

    public final PathPermission[] getPathPermissions() {
        return this.mPathPermissions;
    }

    @UnsupportedAppUsage
    public final void setAppOps(int readOp, int writeOp) {
        if (!this.mNoPerms) {
            Transport transport = this.mTransport;
            transport.mReadOp = readOp;
            transport.mWriteOp = writeOp;
        }
    }

    public AppOpsManager getAppOpsManager() {
        return this.mTransport.mAppOpsManager;
    }

    public final void setTransportLoggingEnabled(boolean enabled) {
        if (enabled) {
            this.mTransport.mInterface = new LoggingContentInterface(getClass().getSimpleName(), this);
            return;
        }
        this.mTransport.mInterface = this;
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
    }

    @Override // android.content.ComponentCallbacks2
    public void onTrimMemory(int level) {
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        return query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override // android.content.ContentInterface
    public Cursor query(Uri uri, String[] projection, Bundle queryArgs, CancellationSignal cancellationSignal) {
        Bundle queryArgs2 = queryArgs != null ? queryArgs : Bundle.EMPTY;
        String sortClause = queryArgs2.getString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER);
        if (sortClause == null && queryArgs2.containsKey(ContentResolver.QUERY_ARG_SORT_COLUMNS)) {
            sortClause = ContentResolver.createSqlSortClause(queryArgs2);
        }
        return query(uri, projection, queryArgs2.getString(ContentResolver.QUERY_ARG_SQL_SELECTION), queryArgs2.getStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS), sortClause, cancellationSignal);
    }

    @Override // android.content.ContentInterface
    public Uri canonicalize(Uri url) {
        return null;
    }

    @Override // android.content.ContentInterface
    public Uri uncanonicalize(Uri url) {
        return url;
    }

    @Override // android.content.ContentInterface
    public boolean refresh(Uri uri, Bundle args, CancellationSignal cancellationSignal) {
        return false;
    }

    public Uri rejectInsert(Uri uri, ContentValues values) {
        return uri.buildUpon().appendPath(WifiEnterpriseConfig.ENGINE_DISABLE).build();
    }

    @Override // android.content.ContentInterface
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numValues = values.length;
        for (ContentValues contentValues : values) {
            insert(uri, contentValues);
        }
        return numValues;
    }

    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        throw new FileNotFoundException("No files supported by provider at " + uri);
    }

    @Override // android.content.ContentInterface
    public ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        return openFile(uri, mode);
    }

    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        ParcelFileDescriptor fd = openFile(uri, mode);
        if (fd != null) {
            return new AssetFileDescriptor(fd, 0L, -1L);
        }
        return null;
    }

    @Override // android.content.ContentInterface
    public AssetFileDescriptor openAssetFile(Uri uri, String mode, CancellationSignal signal) throws FileNotFoundException {
        return openAssetFile(uri, mode);
    }

    protected final ParcelFileDescriptor openFileHelper(Uri uri, String mode) throws FileNotFoundException {
        Cursor c = query(uri, new String[]{"_data"}, null, null, null);
        int count = c != null ? c.getCount() : 0;
        if (count != 1) {
            if (c != null) {
                c.close();
            }
            if (count == 0) {
                throw new FileNotFoundException("No entry for " + uri);
            }
            throw new FileNotFoundException("Multiple items at " + uri);
        }
        c.moveToFirst();
        int i = c.getColumnIndex("_data");
        String path = i >= 0 ? c.getString(i) : null;
        c.close();
        if (path == null) {
            throw new FileNotFoundException("Column _data not found.");
        }
        int modeBits = ParcelFileDescriptor.parseMode(mode);
        return ParcelFileDescriptor.open(new File(path), modeBits);
    }

    @Override // android.content.ContentInterface
    public String[] getStreamTypes(Uri uri, String mimeTypeFilter) {
        return null;
    }

    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts) throws FileNotFoundException {
        if ("*/*".equals(mimeTypeFilter)) {
            return openAssetFile(uri, "r");
        }
        String baseType = getType(uri);
        if (baseType != null && ClipDescription.compareMimeTypes(baseType, mimeTypeFilter)) {
            return openAssetFile(uri, "r");
        }
        throw new FileNotFoundException("Can't open " + uri + " as type " + mimeTypeFilter);
    }

    @Override // android.content.ContentInterface
    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts, CancellationSignal signal) throws FileNotFoundException {
        return openTypedAssetFile(uri, mimeTypeFilter, opts);
    }

    public <T> ParcelFileDescriptor openPipeHelper(final Uri uri, final String mimeType, final Bundle opts, final T args, final PipeDataWriter<T> func) throws FileNotFoundException {
        try {
            final ParcelFileDescriptor[] fds = ParcelFileDescriptor.createPipe();
            AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>() { // from class: android.content.ContentProvider.1
                @Override // android.os.AsyncTask
                protected Object doInBackground(Object... params) {
                    func.writeDataToPipe(fds[1], uri, mimeType, opts, args);
                    try {
                        fds[1].close();
                        return null;
                    } catch (IOException e) {
                        Log.w(ContentProvider.TAG, "Failure closing pipe", e);
                        return null;
                    }
                }
            };
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
            return fds[0];
        } catch (IOException e) {
            throw new FileNotFoundException("failure making pipe");
        }
    }

    protected boolean isTemporary() {
        return false;
    }

    @UnsupportedAppUsage
    public IContentProvider getIContentProvider() {
        return this.mTransport;
    }

    @UnsupportedAppUsage
    public void attachInfoForTesting(Context context, ProviderInfo info) {
        attachInfo(context, info, true);
    }

    public void attachInfo(Context context, ProviderInfo info) {
        attachInfo(context, info, false);
    }

    private void attachInfo(Context context, ProviderInfo info, boolean testing) {
        Transport transport;
        this.mNoPerms = testing;
        this.mCallingPackage = new ThreadLocal<>();
        if (this.mContext == null) {
            this.mContext = context;
            if (context != null && (transport = this.mTransport) != null) {
                transport.mAppOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            }
            this.mMyUid = Process.myUid();
            if (info != null) {
                setReadPermission(info.readPermission);
                setWritePermission(info.writePermission);
                setPathPermissions(info.pathPermissions);
                this.mExported = info.exported;
                this.mSingleUser = (info.flags & 1073741824) != 0;
                setAuthorities(info.authority);
            }
            onCreate();
        }
    }

    @Override // android.content.ContentInterface
    public ContentProviderResult[] applyBatch(String authority, ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        return applyBatch(operations);
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        int numOperations = operations.size();
        ContentProviderResult[] results = new ContentProviderResult[numOperations];
        for (int i = 0; i < numOperations; i++) {
            results[i] = operations.get(i).apply(this, results, i);
        }
        return results;
    }

    @Override // android.content.ContentInterface
    public Bundle call(String authority, String method, String arg, Bundle extras) {
        return call(method, arg, extras);
    }

    public Bundle call(String method, String arg, Bundle extras) {
        return null;
    }

    public void shutdown() {
        Log.w(TAG, "implement ContentProvider shutdown() to make sure all database connections are gracefully shutdown");
    }

    public void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.println("nothing to dump");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void validateIncomingAuthority(String authority) throws SecurityException {
        String message;
        if (!matchesOurAuthorities(getAuthorityWithoutUserId(authority))) {
            String message2 = "The authority " + authority + " does not match the one of the contentProvider: ";
            if (this.mAuthority != null) {
                message = message2 + this.mAuthority;
            } else {
                message = message2 + Arrays.toString(this.mAuthorities);
            }
            throw new SecurityException(message);
        }
    }

    @VisibleForTesting
    public Uri validateIncomingUri(Uri uri) throws SecurityException {
        int userId;
        String auth = uri.getAuthority();
        if (!this.mSingleUser && (userId = getUserIdFromAuthority(auth, -2)) != -2 && userId != this.mContext.getUserId()) {
            throw new SecurityException("trying to query a ContentProvider in user " + this.mContext.getUserId() + " with a uri belonging to user " + userId);
        }
        validateIncomingAuthority(auth);
        String encodedPath = uri.getEncodedPath();
        if (encodedPath != null && encodedPath.indexOf("//") != -1) {
            Uri normalized = uri.buildUpon().encodedPath(encodedPath.replaceAll("//+", "/")).build();
            Log.w(TAG, "Normalized " + uri + " to " + normalized + " to avoid possible security issues");
            return normalized;
        }
        return uri;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Uri maybeGetUriWithoutUserId(Uri uri) {
        if (this.mSingleUser) {
            return uri;
        }
        return getUriWithoutUserId(uri);
    }

    public static int getUserIdFromAuthority(String auth, int defaultUserId) {
        int end;
        if (auth == null || (end = auth.lastIndexOf(64)) == -1) {
            return defaultUserId;
        }
        String userIdString = auth.substring(0, end);
        try {
            return Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            Log.w(TAG, "Error parsing userId.", e);
            return -10000;
        }
    }

    public static int getUserIdFromAuthority(String auth) {
        return getUserIdFromAuthority(auth, -2);
    }

    public static int getUserIdFromUri(Uri uri, int defaultUserId) {
        return uri == null ? defaultUserId : getUserIdFromAuthority(uri.getAuthority(), defaultUserId);
    }

    public static int getUserIdFromUri(Uri uri) {
        return getUserIdFromUri(uri, -2);
    }

    public static String getAuthorityWithoutUserId(String auth) {
        if (auth == null) {
            return null;
        }
        int end = auth.lastIndexOf(64);
        return auth.substring(end + 1);
    }

    public static Uri getUriWithoutUserId(Uri uri) {
        if (uri == null) {
            return null;
        }
        Uri.Builder builder = uri.buildUpon();
        builder.authority(getAuthorityWithoutUserId(uri.getAuthority()));
        return builder.build();
    }

    public static boolean uriHasUserId(Uri uri) {
        if (uri == null) {
            return false;
        }
        return !TextUtils.isEmpty(uri.getUserInfo());
    }

    @UnsupportedAppUsage
    public static Uri maybeAddUserId(Uri uri, int userId) {
        if (uri == null) {
            return null;
        }
        if (userId != -2 && "content".equals(uri.getScheme()) && !uriHasUserId(uri)) {
            Uri.Builder builder = uri.buildUpon();
            builder.encodedAuthority("" + userId + "@" + uri.getEncodedAuthority());
            return builder.build();
        }
        return uri;
    }
}
