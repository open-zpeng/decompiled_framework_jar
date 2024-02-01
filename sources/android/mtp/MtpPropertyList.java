package android.mtp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToLongFunction;
/* loaded from: classes2.dex */
class MtpPropertyList {
    private int mCode;
    private List<Integer> mObjectHandles = new ArrayList();
    private List<Integer> mPropertyCodes = new ArrayList();
    private List<Integer> mDataTypes = new ArrayList();
    private List<Long> mLongValues = new ArrayList();
    private List<String> mStringValues = new ArrayList();

    public synchronized MtpPropertyList(int code) {
        this.mCode = code;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void append(int handle, int property, int type, long value) {
        this.mObjectHandles.add(Integer.valueOf(handle));
        this.mPropertyCodes.add(Integer.valueOf(property));
        this.mDataTypes.add(Integer.valueOf(type));
        this.mLongValues.add(Long.valueOf(value));
        this.mStringValues.add(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void append(int handle, int property, String value) {
        this.mObjectHandles.add(Integer.valueOf(handle));
        this.mPropertyCodes.add(Integer.valueOf(property));
        this.mDataTypes.add(65535);
        this.mStringValues.add(value);
        this.mLongValues.add(0L);
    }

    public synchronized int getCode() {
        return this.mCode;
    }

    public synchronized int getCount() {
        return this.mObjectHandles.size();
    }

    public synchronized int[] getObjectHandles() {
        return this.mObjectHandles.stream().mapToInt($$Lambda$MtpPropertyList$UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).toArray();
    }

    public synchronized int[] getPropertyCodes() {
        return this.mPropertyCodes.stream().mapToInt($$Lambda$MtpPropertyList$UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).toArray();
    }

    public synchronized int[] getDataTypes() {
        return this.mDataTypes.stream().mapToInt($$Lambda$MtpPropertyList$UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).toArray();
    }

    public synchronized long[] getLongValues() {
        return this.mLongValues.stream().mapToLong(new ToLongFunction() { // from class: android.mtp.-$$Lambda$MtpPropertyList$ELHKvd8JMVRD8rbALqYPKbDX2mM
            @Override // java.util.function.ToLongFunction
            public final long applyAsLong(Object obj) {
                long longValue;
                longValue = ((Long) obj).longValue();
                return longValue;
            }
        }).toArray();
    }

    public synchronized String[] getStringValues() {
        return (String[]) this.mStringValues.toArray(new String[0]);
    }
}
