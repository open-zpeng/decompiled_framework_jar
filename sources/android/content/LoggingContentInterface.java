package android.content;

import android.accounts.AccountManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes.dex */
public class LoggingContentInterface implements ContentInterface {
    private final ContentInterface delegate;
    private final String tag;

    public LoggingContentInterface(String tag, ContentInterface delegate) {
        this.tag = tag;
        this.delegate = delegate;
    }

    /* loaded from: classes.dex */
    private class Logger implements AutoCloseable {
        private final StringBuilder sb = new StringBuilder();

        public Logger(String method, Object... args) {
            for (Object arg : args) {
                if (arg instanceof Bundle) {
                    ((Bundle) arg).size();
                }
            }
            StringBuilder sb = this.sb;
            sb.append("callingUid=");
            sb.append(Binder.getCallingUid());
            sb.append(' ');
            this.sb.append(method);
            StringBuilder sb2 = this.sb;
            sb2.append('(');
            sb2.append(deepToString(args));
            sb2.append(')');
        }

        private String deepToString(Object value) {
            if (value != null && value.getClass().isArray()) {
                return Arrays.deepToString((Object[]) value);
            }
            return String.valueOf(value);
        }

        public <T> T setResult(T res) {
            if (res instanceof Cursor) {
                this.sb.append('\n');
                DatabaseUtils.dumpCursor((Cursor) res, this.sb);
            } else {
                StringBuilder sb = this.sb;
                sb.append(" = ");
                sb.append(deepToString(res));
            }
            return res;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            Log.v(LoggingContentInterface.this.tag, this.sb.toString());
        }
    }

    @Override // android.content.ContentInterface
    public Cursor query(Uri uri, String[] projection, Bundle queryArgs, CancellationSignal cancellationSignal) throws RemoteException {
        Logger l = new Logger("query", uri, projection, queryArgs, cancellationSignal);
        try {
            try {
                Cursor cursor = (Cursor) l.setResult(this.delegate.query(uri, projection, queryArgs, cancellationSignal));
                $closeResource(null, l);
                return cursor;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
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

    @Override // android.content.ContentInterface
    public String getType(Uri uri) throws RemoteException {
        Logger l = new Logger("getType", uri);
        try {
            try {
                String str = (String) l.setResult(this.delegate.getType(uri));
                $closeResource(null, l);
                return str;
            } catch (Throwable res) {
                try {
                    throw res;
                } catch (Throwable th) {
                    $closeResource(res, l);
                    throw th;
                }
            }
        } catch (Exception res2) {
            l.setResult(res2);
            throw res2;
        }
    }

    @Override // android.content.ContentInterface
    public String[] getStreamTypes(Uri uri, String mimeTypeFilter) throws RemoteException {
        Logger l = new Logger("getStreamTypes", uri, mimeTypeFilter);
        try {
            try {
                String[] strArr = (String[]) l.setResult(this.delegate.getStreamTypes(uri, mimeTypeFilter));
                $closeResource(null, l);
                return strArr;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
        }
    }

    @Override // android.content.ContentInterface
    public Uri canonicalize(Uri uri) throws RemoteException {
        Logger l = new Logger("canonicalize", uri);
        try {
            try {
                Uri uri2 = (Uri) l.setResult(this.delegate.canonicalize(uri));
                $closeResource(null, l);
                return uri2;
            } catch (Throwable res) {
                try {
                    throw res;
                } catch (Throwable th) {
                    $closeResource(res, l);
                    throw th;
                }
            }
        } catch (Exception res2) {
            l.setResult(res2);
            throw res2;
        }
    }

    @Override // android.content.ContentInterface
    public Uri uncanonicalize(Uri uri) throws RemoteException {
        Logger l = new Logger("uncanonicalize", uri);
        try {
            try {
                Uri uri2 = (Uri) l.setResult(this.delegate.uncanonicalize(uri));
                $closeResource(null, l);
                return uri2;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
        }
    }

    @Override // android.content.ContentInterface
    public boolean refresh(Uri uri, Bundle args, CancellationSignal cancellationSignal) throws RemoteException {
        Logger l = new Logger("refresh", uri, args, cancellationSignal);
        try {
            try {
                boolean booleanValue = ((Boolean) l.setResult(Boolean.valueOf(this.delegate.refresh(uri, args, cancellationSignal)))).booleanValue();
                $closeResource(null, l);
                return booleanValue;
            } catch (Throwable res) {
                try {
                    throw res;
                } catch (Throwable th) {
                    $closeResource(res, l);
                    throw th;
                }
            }
        } catch (Exception res2) {
            l.setResult(res2);
            throw res2;
        }
    }

    @Override // android.content.ContentInterface
    public Uri insert(Uri uri, ContentValues initialValues) throws RemoteException {
        Logger l = new Logger("insert", uri, initialValues);
        try {
            try {
                Uri uri2 = (Uri) l.setResult(this.delegate.insert(uri, initialValues));
                $closeResource(null, l);
                return uri2;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
        }
    }

    @Override // android.content.ContentInterface
    public int bulkInsert(Uri uri, ContentValues[] initialValues) throws RemoteException {
        Logger l = new Logger("bulkInsert", uri, initialValues);
        try {
            try {
                int intValue = ((Integer) l.setResult(Integer.valueOf(this.delegate.bulkInsert(uri, initialValues)))).intValue();
                $closeResource(null, l);
                return intValue;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
        }
    }

    @Override // android.content.ContentInterface
    public int delete(Uri uri, String selection, String[] selectionArgs) throws RemoteException {
        Logger l = new Logger("delete", uri, selection, selectionArgs);
        try {
            try {
                int intValue = ((Integer) l.setResult(Integer.valueOf(this.delegate.delete(uri, selection, selectionArgs)))).intValue();
                $closeResource(null, l);
                return intValue;
            } catch (Throwable res) {
                try {
                    throw res;
                } catch (Throwable th) {
                    $closeResource(res, l);
                    throw th;
                }
            }
        } catch (Exception res2) {
            l.setResult(res2);
            throw res2;
        }
    }

    @Override // android.content.ContentInterface
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) throws RemoteException {
        Logger l = new Logger(AccountManager.USER_DATA_EXTRA_UPDATE, uri, values, selection, selectionArgs);
        try {
            try {
                int intValue = ((Integer) l.setResult(Integer.valueOf(this.delegate.update(uri, values, selection, selectionArgs)))).intValue();
                $closeResource(null, l);
                return intValue;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
        }
    }

    @Override // android.content.ContentInterface
    public ParcelFileDescriptor openFile(Uri uri, String mode, CancellationSignal signal) throws RemoteException, FileNotFoundException {
        Logger l = new Logger("openFile", uri, mode, signal);
        try {
            try {
                ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) l.setResult(this.delegate.openFile(uri, mode, signal));
                $closeResource(null, l);
                return parcelFileDescriptor;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
        }
    }

    @Override // android.content.ContentInterface
    public AssetFileDescriptor openAssetFile(Uri uri, String mode, CancellationSignal signal) throws RemoteException, FileNotFoundException {
        Logger l = new Logger("openAssetFile", uri, mode, signal);
        try {
            try {
                AssetFileDescriptor assetFileDescriptor = (AssetFileDescriptor) l.setResult(this.delegate.openAssetFile(uri, mode, signal));
                $closeResource(null, l);
                return assetFileDescriptor;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
        }
    }

    @Override // android.content.ContentInterface
    public AssetFileDescriptor openTypedAssetFile(Uri uri, String mimeTypeFilter, Bundle opts, CancellationSignal signal) throws RemoteException, FileNotFoundException {
        Logger l = new Logger("openTypedAssetFile", uri, mimeTypeFilter, opts, signal);
        try {
            try {
                AssetFileDescriptor assetFileDescriptor = (AssetFileDescriptor) l.setResult(this.delegate.openTypedAssetFile(uri, mimeTypeFilter, opts, signal));
                $closeResource(null, l);
                return assetFileDescriptor;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
        }
    }

    @Override // android.content.ContentInterface
    public ContentProviderResult[] applyBatch(String authority, ArrayList<ContentProviderOperation> operations) throws RemoteException, OperationApplicationException {
        Logger l = new Logger("applyBatch", authority, operations);
        try {
            try {
                ContentProviderResult[] contentProviderResultArr = (ContentProviderResult[]) l.setResult(this.delegate.applyBatch(authority, operations));
                $closeResource(null, l);
                return contentProviderResultArr;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
        }
    }

    @Override // android.content.ContentInterface
    public Bundle call(String authority, String method, String arg, Bundle extras) throws RemoteException {
        Logger l = new Logger("call", authority, method, arg, extras);
        try {
            try {
                Bundle bundle = (Bundle) l.setResult(this.delegate.call(authority, method, arg, extras));
                $closeResource(null, l);
                return bundle;
            } catch (Exception res) {
                l.setResult(res);
                throw res;
            }
        } catch (Throwable res2) {
            try {
                throw res2;
            } catch (Throwable th) {
                $closeResource(res2, l);
                throw th;
            }
        }
    }
}
