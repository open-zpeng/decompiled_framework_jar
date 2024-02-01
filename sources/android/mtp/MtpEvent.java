package android.mtp;

/* loaded from: classes2.dex */
public class MtpEvent {
    public static final int EVENT_CANCEL_TRANSACTION = 16385;
    public static final int EVENT_CAPTURE_COMPLETE = 16397;
    public static final int EVENT_DEVICE_INFO_CHANGED = 16392;
    public static final int EVENT_DEVICE_PROP_CHANGED = 16390;
    public static final int EVENT_DEVICE_RESET = 16395;
    public static final int EVENT_OBJECT_ADDED = 16386;
    public static final int EVENT_OBJECT_INFO_CHANGED = 16391;
    public static final int EVENT_OBJECT_PROP_CHANGED = 51201;
    public static final int EVENT_OBJECT_PROP_DESC_CHANGED = 51202;
    public static final int EVENT_OBJECT_REFERENCES_CHANGED = 51203;
    public static final int EVENT_OBJECT_REMOVED = 16387;
    public static final int EVENT_REQUEST_OBJECT_TRANSFER = 16393;
    public static final int EVENT_STORAGE_INFO_CHANGED = 16396;
    public static final int EVENT_STORE_ADDED = 16388;
    public static final int EVENT_STORE_FULL = 16394;
    public static final int EVENT_STORE_REMOVED = 16389;
    public static final int EVENT_UNDEFINED = 16384;
    public static final int EVENT_UNREPORTED_STATUS = 16398;
    private int mEventCode = 16384;
    private int mParameter1;
    private int mParameter2;
    private int mParameter3;

    private MtpEvent() {
    }

    public int getEventCode() {
        return this.mEventCode;
    }

    public int getParameter1() {
        return this.mParameter1;
    }

    public int getParameter2() {
        return this.mParameter2;
    }

    public int getParameter3() {
        return this.mParameter3;
    }

    public int getObjectHandle() {
        int i = this.mEventCode;
        if (i != 16386) {
            if (i != 16387) {
                if (i != 16391) {
                    if (i != 16393) {
                        if (i != 51201) {
                            if (i == 51203) {
                                return this.mParameter1;
                            }
                            throw new IllegalParameterAccess("objectHandle", i);
                        }
                        return this.mParameter1;
                    }
                    return this.mParameter1;
                }
                return this.mParameter1;
            }
            return this.mParameter1;
        }
        return this.mParameter1;
    }

    public int getStorageId() {
        int i = this.mEventCode;
        if (i != 16388) {
            if (i != 16389) {
                if (i != 16394) {
                    if (i == 16396) {
                        return this.mParameter1;
                    }
                    throw new IllegalParameterAccess("storageID", i);
                }
                return this.mParameter1;
            }
            return this.mParameter1;
        }
        return this.mParameter1;
    }

    public int getDevicePropCode() {
        int i = this.mEventCode;
        if (i == 16390) {
            return this.mParameter1;
        }
        throw new IllegalParameterAccess("devicePropCode", i);
    }

    public int getTransactionId() {
        int i = this.mEventCode;
        if (i == 16397) {
            return this.mParameter1;
        }
        throw new IllegalParameterAccess("transactionID", i);
    }

    public int getObjectPropCode() {
        int i = this.mEventCode;
        switch (i) {
            case EVENT_OBJECT_PROP_CHANGED /* 51201 */:
                return this.mParameter2;
            case EVENT_OBJECT_PROP_DESC_CHANGED /* 51202 */:
                return this.mParameter1;
            default:
                throw new IllegalParameterAccess("objectPropCode", i);
        }
    }

    public int getObjectFormatCode() {
        int i = this.mEventCode;
        if (i == 51202) {
            return this.mParameter2;
        }
        throw new IllegalParameterAccess("objectFormatCode", i);
    }

    /* loaded from: classes2.dex */
    private static class IllegalParameterAccess extends UnsupportedOperationException {
        public IllegalParameterAccess(String propertyName, int eventCode) {
            super("Cannot obtain " + propertyName + " for the event: " + eventCode + ".");
        }
    }
}
