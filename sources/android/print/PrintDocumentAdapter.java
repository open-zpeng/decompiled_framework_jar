package android.print;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;

/* loaded from: classes2.dex */
public abstract class PrintDocumentAdapter {
    public static final String EXTRA_PRINT_PREVIEW = "EXTRA_PRINT_PREVIEW";

    public abstract void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle);

    public abstract void onWrite(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback);

    public void onStart() {
    }

    public void onFinish() {
    }

    /* loaded from: classes2.dex */
    public static abstract class WriteResultCallback {
        public void onWriteFinished(PageRange[] pages) {
        }

        public void onWriteFailed(CharSequence error) {
        }

        public void onWriteCancelled() {
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class LayoutResultCallback {
        public void onLayoutFinished(PrintDocumentInfo info, boolean changed) {
        }

        public void onLayoutFailed(CharSequence error) {
        }

        public void onLayoutCancelled() {
        }
    }
}
