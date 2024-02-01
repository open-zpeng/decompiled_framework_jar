package android.content;

import android.annotation.UnsupportedAppUsage;
import android.content.res.AssetFileDescriptor;
import android.database.BulkCursorDescriptor;
import android.database.Cursor;
import android.database.CursorToBulkCursorAdaptor;
import android.database.DatabaseUtils;
import android.database.IContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class ContentProviderNative extends Binder implements IContentProvider {
    public abstract String getProviderName();

    public ContentProviderNative() {
        attachInterface(this, IContentProvider.descriptor);
    }

    @UnsupportedAppUsage
    public static IContentProvider asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IContentProvider in = (IContentProvider) obj.queryLocalInterface(IContentProvider.descriptor);
        if (in != null) {
            return in;
        }
        return new ContentProviderProxy(obj);
    }

    @Override // android.os.Binder
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        String[] projection;
        int i = 0;
        try {
            if (code == 1) {
                data.enforceInterface(IContentProvider.descriptor);
                String callingPkg = data.readString();
                Uri url = Uri.CREATOR.createFromParcel(data);
                int num = data.readInt();
                if (num <= 0) {
                    projection = null;
                } else {
                    String[] projection2 = new String[num];
                    for (int i2 = 0; i2 < num; i2++) {
                        projection2[i2] = data.readString();
                    }
                    projection = projection2;
                }
                Bundle queryArgs = data.readBundle();
                IContentObserver observer = IContentObserver.Stub.asInterface(data.readStrongBinder());
                ICancellationSignal cancellationSignal = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                Cursor cursor = query(callingPkg, url, projection, queryArgs, cancellationSignal);
                if (cursor != null) {
                    CursorToBulkCursorAdaptor adaptor = new CursorToBulkCursorAdaptor(cursor, observer, getProviderName());
                    Cursor cursor2 = null;
                    BulkCursorDescriptor d = adaptor.getBulkCursorDescriptor();
                    CursorToBulkCursorAdaptor adaptor2 = null;
                    reply.writeNoException();
                    reply.writeInt(1);
                    d.writeToParcel(reply, 1);
                    if (0 != 0) {
                        adaptor2.close();
                    }
                    if (0 != 0) {
                        cursor2.close();
                    }
                } else {
                    reply.writeNoException();
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 2) {
                data.enforceInterface(IContentProvider.descriptor);
                Uri url2 = Uri.CREATOR.createFromParcel(data);
                String type = getType(url2);
                reply.writeNoException();
                reply.writeString(type);
                return true;
            } else if (code == 3) {
                data.enforceInterface(IContentProvider.descriptor);
                String callingPkg2 = data.readString();
                Uri url3 = Uri.CREATOR.createFromParcel(data);
                ContentValues values = ContentValues.CREATOR.createFromParcel(data);
                Uri out = insert(callingPkg2, url3, values);
                reply.writeNoException();
                Uri.writeToParcel(reply, out);
                return true;
            } else if (code == 4) {
                data.enforceInterface(IContentProvider.descriptor);
                String callingPkg3 = data.readString();
                Uri url4 = Uri.CREATOR.createFromParcel(data);
                String selection = data.readString();
                String[] selectionArgs = data.readStringArray();
                int count = delete(callingPkg3, url4, selection, selectionArgs);
                reply.writeNoException();
                reply.writeInt(count);
                return true;
            } else if (code != 10) {
                switch (code) {
                    case 13:
                        data.enforceInterface(IContentProvider.descriptor);
                        String callingPkg4 = data.readString();
                        Uri url5 = Uri.CREATOR.createFromParcel(data);
                        ContentValues[] values2 = (ContentValues[]) data.createTypedArray(ContentValues.CREATOR);
                        int count2 = bulkInsert(callingPkg4, url5, values2);
                        reply.writeNoException();
                        reply.writeInt(count2);
                        return true;
                    case 14:
                        data.enforceInterface(IContentProvider.descriptor);
                        String callingPkg5 = data.readString();
                        Uri url6 = Uri.CREATOR.createFromParcel(data);
                        String mode = data.readString();
                        ICancellationSignal signal = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                        IBinder callerToken = data.readStrongBinder();
                        ParcelFileDescriptor fd = openFile(callingPkg5, url6, mode, signal, callerToken);
                        reply.writeNoException();
                        if (fd != null) {
                            reply.writeInt(1);
                            fd.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 15:
                        data.enforceInterface(IContentProvider.descriptor);
                        String callingPkg6 = data.readString();
                        Uri url7 = Uri.CREATOR.createFromParcel(data);
                        String mode2 = data.readString();
                        ICancellationSignal signal2 = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                        AssetFileDescriptor fd2 = openAssetFile(callingPkg6, url7, mode2, signal2);
                        reply.writeNoException();
                        if (fd2 != null) {
                            reply.writeInt(1);
                            fd2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    default:
                        switch (code) {
                            case 20:
                                data.enforceInterface(IContentProvider.descriptor);
                                String callingPkg7 = data.readString();
                                String authority = data.readString();
                                int numOperations = data.readInt();
                                ArrayList<ContentProviderOperation> operations = new ArrayList<>(numOperations);
                                for (int i3 = 0; i3 < numOperations; i3++) {
                                    operations.add(i3, ContentProviderOperation.CREATOR.createFromParcel(data));
                                }
                                ContentProviderResult[] results = applyBatch(callingPkg7, authority, operations);
                                reply.writeNoException();
                                reply.writeTypedArray(results, 0);
                                return true;
                            case 21:
                                data.enforceInterface(IContentProvider.descriptor);
                                String callingPkg8 = data.readString();
                                String authority2 = data.readString();
                                String method = data.readString();
                                String stringArg = data.readString();
                                Bundle args = data.readBundle();
                                Bundle responseBundle = call(callingPkg8, authority2, method, stringArg, args);
                                reply.writeNoException();
                                reply.writeBundle(responseBundle);
                                return true;
                            case 22:
                                data.enforceInterface(IContentProvider.descriptor);
                                Uri url8 = Uri.CREATOR.createFromParcel(data);
                                String mimeTypeFilter = data.readString();
                                String[] types = getStreamTypes(url8, mimeTypeFilter);
                                reply.writeNoException();
                                reply.writeStringArray(types);
                                return true;
                            case 23:
                                data.enforceInterface(IContentProvider.descriptor);
                                String callingPkg9 = data.readString();
                                Uri url9 = Uri.CREATOR.createFromParcel(data);
                                String mimeType = data.readString();
                                Bundle opts = data.readBundle();
                                ICancellationSignal signal3 = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                                AssetFileDescriptor fd3 = openTypedAssetFile(callingPkg9, url9, mimeType, opts, signal3);
                                reply.writeNoException();
                                if (fd3 != null) {
                                    reply.writeInt(1);
                                    fd3.writeToParcel(reply, 1);
                                } else {
                                    reply.writeInt(0);
                                }
                                return true;
                            case 24:
                                data.enforceInterface(IContentProvider.descriptor);
                                ICancellationSignal cancellationSignal2 = createCancellationSignal();
                                reply.writeNoException();
                                reply.writeStrongBinder(cancellationSignal2.asBinder());
                                return true;
                            case 25:
                                data.enforceInterface(IContentProvider.descriptor);
                                String callingPkg10 = data.readString();
                                Uri url10 = Uri.CREATOR.createFromParcel(data);
                                Uri out2 = canonicalize(callingPkg10, url10);
                                reply.writeNoException();
                                Uri.writeToParcel(reply, out2);
                                return true;
                            case 26:
                                data.enforceInterface(IContentProvider.descriptor);
                                String callingPkg11 = data.readString();
                                Uri url11 = Uri.CREATOR.createFromParcel(data);
                                Uri out3 = uncanonicalize(callingPkg11, url11);
                                reply.writeNoException();
                                Uri.writeToParcel(reply, out3);
                                return true;
                            case 27:
                                data.enforceInterface(IContentProvider.descriptor);
                                String callingPkg12 = data.readString();
                                Uri url12 = Uri.CREATOR.createFromParcel(data);
                                Bundle args2 = data.readBundle();
                                ICancellationSignal signal4 = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                                boolean out4 = refresh(callingPkg12, url12, args2, signal4);
                                reply.writeNoException();
                                if (!out4) {
                                    i = -1;
                                }
                                reply.writeInt(i);
                                return true;
                            default:
                                return super.onTransact(code, data, reply, flags);
                        }
                }
            } else {
                data.enforceInterface(IContentProvider.descriptor);
                String callingPkg13 = data.readString();
                Uri url13 = Uri.CREATOR.createFromParcel(data);
                ContentValues values3 = ContentValues.CREATOR.createFromParcel(data);
                String selection2 = data.readString();
                String[] selectionArgs2 = data.readStringArray();
                int count3 = update(callingPkg13, url13, values3, selection2, selectionArgs2);
                reply.writeNoException();
                reply.writeInt(count3);
                return true;
            }
        } catch (Exception e) {
            DatabaseUtils.writeExceptionToParcel(reply, e);
            return true;
        }
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this;
    }
}
