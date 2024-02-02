package android.content;

import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
/* loaded from: classes.dex */
public interface IContentProvider extends IInterface {
    public static final int APPLY_BATCH_TRANSACTION = 20;
    public static final int BULK_INSERT_TRANSACTION = 13;
    public static final int CALL_TRANSACTION = 21;
    public static final int CANONICALIZE_TRANSACTION = 25;
    public static final int CREATE_CANCELATION_SIGNAL_TRANSACTION = 24;
    public static final int DELETE_TRANSACTION = 4;
    public static final int GET_STREAM_TYPES_TRANSACTION = 22;
    public static final int GET_TYPE_TRANSACTION = 2;
    public static final int INSERT_TRANSACTION = 3;
    public static final int OPEN_ASSET_FILE_TRANSACTION = 15;
    public static final int OPEN_FILE_TRANSACTION = 14;
    public static final int OPEN_TYPED_ASSET_FILE_TRANSACTION = 23;
    private protected static final int QUERY_TRANSACTION = 1;
    public static final int REFRESH_TRANSACTION = 27;
    public static final int UNCANONICALIZE_TRANSACTION = 26;
    public static final int UPDATE_TRANSACTION = 10;
    private protected static final String descriptor = "android.content.IContentProvider";

    synchronized ContentProviderResult[] applyBatch(String str, ArrayList<ContentProviderOperation> arrayList) throws RemoteException, OperationApplicationException;

    /* JADX INFO: Access modifiers changed from: private */
    int bulkInsert(String str, Uri uri, ContentValues[] contentValuesArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    Bundle call(String str, String str2, String str3, Bundle bundle) throws RemoteException;

    synchronized Uri canonicalize(String str, Uri uri) throws RemoteException;

    synchronized ICancellationSignal createCancellationSignal() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int delete(String str, Uri uri, String str2, String[] strArr) throws RemoteException;

    synchronized String[] getStreamTypes(Uri uri, String str) throws RemoteException;

    synchronized String getType(Uri uri) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    Uri insert(String str, Uri uri, ContentValues contentValues) throws RemoteException;

    synchronized AssetFileDescriptor openAssetFile(String str, Uri uri, String str2, ICancellationSignal iCancellationSignal) throws RemoteException, FileNotFoundException;

    synchronized ParcelFileDescriptor openFile(String str, Uri uri, String str2, ICancellationSignal iCancellationSignal, IBinder iBinder) throws RemoteException, FileNotFoundException;

    synchronized AssetFileDescriptor openTypedAssetFile(String str, Uri uri, String str2, Bundle bundle, ICancellationSignal iCancellationSignal) throws RemoteException, FileNotFoundException;

    synchronized Cursor query(String str, Uri uri, String[] strArr, Bundle bundle, ICancellationSignal iCancellationSignal) throws RemoteException;

    synchronized boolean refresh(String str, Uri uri, Bundle bundle, ICancellationSignal iCancellationSignal) throws RemoteException;

    synchronized Uri uncanonicalize(String str, Uri uri) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int update(String str, Uri uri, ContentValues contentValues, String str2, String[] strArr) throws RemoteException;
}
