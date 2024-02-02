package android.print;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PrintDocumentAdapter;
import com.android.internal.R;
import java.io.File;
/* loaded from: classes2.dex */
public class PrintFileDocumentAdapter extends PrintDocumentAdapter {
    private static final String LOG_TAG = "PrintedFileDocAdapter";
    private final Context mContext;
    private final PrintDocumentInfo mDocumentInfo;
    private final File mFile;
    private WriteFileAsyncTask mWriteFileAsyncTask;

    static /* synthetic */ File access$000(PrintFileDocumentAdapter x0) {
        return x0.mFile;
    }

    static /* synthetic */ Context access$100(PrintFileDocumentAdapter x0) {
        return x0.mContext;
    }

    public synchronized PrintFileDocumentAdapter(Context context, File file, PrintDocumentInfo documentInfo) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null!");
        }
        if (documentInfo == null) {
            throw new IllegalArgumentException("documentInfo cannot be null!");
        }
        this.mContext = context;
        this.mFile = file;
        this.mDocumentInfo = documentInfo;
    }

    @Override // android.print.PrintDocumentAdapter
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback callback, Bundle metadata) {
        callback.onLayoutFinished(this.mDocumentInfo, false);
    }

    @Override // android.print.PrintDocumentAdapter
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback callback) {
        this.mWriteFileAsyncTask = new WriteFileAsyncTask(destination, cancellationSignal, callback);
        this.mWriteFileAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    /* loaded from: classes2.dex */
    private final class WriteFileAsyncTask extends AsyncTask<Void, Void, Void> {
        private final CancellationSignal mCancellationSignal;
        private final ParcelFileDescriptor mDestination;
        private final PrintDocumentAdapter.WriteResultCallback mResultCallback;

        public WriteFileAsyncTask(ParcelFileDescriptor destination, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback callback) {
            this.mDestination = destination;
            this.mResultCallback = callback;
            this.mCancellationSignal = cancellationSignal;
            this.mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: android.print.PrintFileDocumentAdapter.WriteFileAsyncTask.1
                @Override // android.os.CancellationSignal.OnCancelListener
                public void onCancel() {
                    WriteFileAsyncTask.this.cancel(true);
                }
            });
        }

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Found unreachable blocks
            	at jadx.core.dex.visitors.blocks.DominatorTree.sortBlocks(DominatorTree.java:35)
            	at jadx.core.dex.visitors.blocks.DominatorTree.compute(DominatorTree.java:25)
            	at jadx.core.dex.visitors.blocks.BlockProcessor.computeDominators(BlockProcessor.java:202)
            	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:45)
            	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
            */
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public java.lang.Void doInBackground(java.lang.Void... r7) {
            /*
                r6 = this;
                r0 = 0
                java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.io.IOException -> L3e android.os.OperationCanceledException -> L59
                android.print.PrintFileDocumentAdapter r2 = android.print.PrintFileDocumentAdapter.this     // Catch: java.io.IOException -> L3e android.os.OperationCanceledException -> L59
                java.io.File r2 = android.print.PrintFileDocumentAdapter.access$000(r2)     // Catch: java.io.IOException -> L3e android.os.OperationCanceledException -> L59
                r1.<init>(r2)     // Catch: java.io.IOException -> L3e android.os.OperationCanceledException -> L59
                java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L34
                android.os.ParcelFileDescriptor r3 = r6.mDestination     // Catch: java.lang.Throwable -> L34
                java.io.FileDescriptor r3 = r3.getFileDescriptor()     // Catch: java.lang.Throwable -> L34
                r2.<init>(r3)     // Catch: java.lang.Throwable -> L34
                android.os.CancellationSignal r3 = r6.mCancellationSignal     // Catch: java.lang.Throwable -> L27
                android.os.FileUtils.copy(r1, r2, r0, r3)     // Catch: java.lang.Throwable -> L27
                $closeResource(r0, r2)     // Catch: java.lang.Throwable -> L34
                $closeResource(r0, r1)     // Catch: java.io.IOException -> L3e android.os.OperationCanceledException -> L59
                goto L5a
            L24:
                r3 = move-exception
                r4 = r0
                goto L2d
            L27:
                r3 = move-exception
                throw r3     // Catch: java.lang.Throwable -> L29
            L29:
                r4 = move-exception
                r5 = r4
                r4 = r3
                r3 = r5
            L2d:
                $closeResource(r4, r2)     // Catch: java.lang.Throwable -> L34
                throw r3     // Catch: java.lang.Throwable -> L34
            L31:
                r2 = move-exception
                r3 = r0
                goto L3a
            L34:
                r2 = move-exception
                throw r2     // Catch: java.lang.Throwable -> L36
            L36:
                r3 = move-exception
                r5 = r3
                r3 = r2
                r2 = r5
            L3a:
                $closeResource(r3, r1)     // Catch: java.io.IOException -> L3e android.os.OperationCanceledException -> L59
                throw r2     // Catch: java.io.IOException -> L3e android.os.OperationCanceledException -> L59
            L3e:
                r1 = move-exception
                java.lang.String r2 = "PrintedFileDocAdapter"
                java.lang.String r3 = "Error writing data!"
                android.util.Log.e(r2, r3, r1)
                android.print.PrintDocumentAdapter$WriteResultCallback r2 = r6.mResultCallback
                android.print.PrintFileDocumentAdapter r3 = android.print.PrintFileDocumentAdapter.this
                android.content.Context r3 = android.print.PrintFileDocumentAdapter.access$100(r3)
                r4 = 17041151(0x10406ff, float:2.424959E-38)
                java.lang.String r3 = r3.getString(r4)
                r2.onWriteFailed(r3)
                goto L5b
            L59:
                r1 = move-exception
            L5a:
            L5b:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: android.print.PrintFileDocumentAdapter.WriteFileAsyncTask.doInBackground(java.lang.Void[]):java.lang.Void");
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

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public synchronized void onPostExecute(Void result) {
            this.mResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public synchronized void onCancelled(Void result) {
            this.mResultCallback.onWriteFailed(PrintFileDocumentAdapter.this.mContext.getString(R.string.write_fail_reason_cancelled));
        }
    }
}
