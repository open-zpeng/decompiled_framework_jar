package android.nfc;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;

/* loaded from: classes2.dex */
public final class NfcManager {
    private final NfcAdapter mAdapter;

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public NfcManager(Context context) {
        NfcAdapter adapter;
        Context context2 = context.getApplicationContext();
        if (context2 == null) {
            throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
        }
        try {
            adapter = NfcAdapter.getNfcAdapter(context2);
        } catch (UnsupportedOperationException e) {
            adapter = null;
        }
        this.mAdapter = adapter;
    }

    public NfcAdapter getDefaultAdapter() {
        return this.mAdapter;
    }
}
