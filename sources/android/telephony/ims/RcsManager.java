package android.telephony.ims;

import android.content.Context;

/* loaded from: classes2.dex */
public class RcsManager {
    private final RcsMessageStore mRcsMessageStore;

    public RcsManager(Context context) {
        this.mRcsMessageStore = new RcsMessageStore(context);
    }

    public RcsMessageStore getRcsMessageStore() {
        return this.mRcsMessageStore;
    }
}
